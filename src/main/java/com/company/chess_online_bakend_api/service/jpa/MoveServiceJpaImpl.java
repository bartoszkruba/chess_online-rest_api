/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.converter.command.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.command.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.converter.command.move.MoveToMoveCommand;
import com.company.chess_online_bakend_api.data.model.*;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.ForbiddenException;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.exception.InvalidMoveException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.MoveService;
import com.company.chess_online_bakend_api.util.BoardUtil;
import com.company.chess_online_bakend_api.util.GameUtil;
import com.company.chess_online_bakend_api.util.PositionUtils;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MoveServiceJpaImpl implements MoveService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final GameToGameCommand gameToGameCommand;
    private final GameCommandToGame gameCommandToGame;

    private final MoveToMoveCommand moveToMoveCommand;

    @Autowired
    public MoveServiceJpaImpl(GameRepository gameRepository, UserRepository userRepository,
                              RoomRepository roomRepository, GameToGameCommand gameToGameCommand,
                              GameCommandToGame gameCommandToGame, MoveToMoveCommand moveToMoveCommand) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.gameToGameCommand = gameToGameCommand;
        this.gameCommandToGame = gameCommandToGame;
        this.moveToMoveCommand = moveToMoveCommand;
    }

    @Override
    public Set<MoveCommand> getPossibleMoves(String from, Long gameId) throws MoveGeneratorException {
        log.debug("Getting valid moves for game with id " + gameId + ", from position: " + from);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " does not exist"));

        Board board = new Board();
        board.loadFromFen(game.getFenNotation());

        MoveList validMoves = MoveGenerator.generateLegalMoves(board);
        String[] movesArray = validMoves.toString().split(" ");
        Stream<String> moveStream = Arrays.stream(movesArray);

        if (from != null) {
            moveStream = moveStream.filter(move -> move.startsWith(from.toLowerCase()));
        }

        return moveStream.map(move -> MoveCommand.builder()
                .from(move.substring(0, 2).toUpperCase())
                .to(move.substring(2, 4).toUpperCase())
                .build())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MoveCommand performMove(String username, Long gameId, String from, String to) throws MoveGeneratorException {
        log.debug("Performing move for game with id " + gameId + ", from position: " + from + " to: " + to);

        // Checking if game exist
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " does not exist"));

        Board board = validateMoveAction(game, username, from, to);

        MoveCommand moveCommand = updateGameWithMove(game, from, to, board);

        return moveCommand;
    }

    @Override
    @Transactional
    public List<MoveCommand> getGameMoves(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " does not exist"));

        return game.getMoves()
                .stream()
                .sorted(Comparator.comparing(BaseEntity::getCreated))
                .map(moveToMoveCommand::convert)
                .collect(Collectors.toList());
    }

    private Board validateMoveAction(Game game, String username, String from, String to) throws MoveGeneratorException {
        log.debug("Validating move");

        // Checking if user exist
        userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist"));

        User whitePlayer = game.getWhitePlayer();
        User blackPlayer = game.getBlackPlayer();

        // Checking if both players have joined
        if (whitePlayer == null || blackPlayer == null) {
            throw new InvalidMoveException("There is no other player");
        }

        // Checking if player is part of the game
        if (!whitePlayer.getUsername().equals(username) && !blackPlayer.getUsername().equals(username)) {
            throw new ForbiddenException("You are not authorized to make moves");
        }

        // Changing game status to STARTED if game not started yet and user is white
        if (game.getStatus() == GameStatus.WAITNG_TO_START && whitePlayer.getUsername().equals(username)) {
            game.setStatus(GameStatus.STARTED);
        } else if (game.getStatus() == GameStatus.WAITNG_TO_START && !whitePlayer.getUsername().equals(username)) {
            throw new InvalidMoveException("It is not your turn");
        }

        // Checking if game already finished
        if (game.getStatus() != GameStatus.STARTED) {
            throw new InvalidMoveException("Game already finished");
        }

        if (game.getTurn() == 0) {
            game.setTurn(1);
        }

        // Checking if it is players turn
        if (game.getTurn() % 2 == 0 && !blackPlayer.getUsername().equals(username)) {
            throw new InvalidMoveException("It is not your turn");
        } else if (game.getTurn() % 2 != 0 && !whitePlayer.getUsername().equals(username)) {
            throw new InvalidMoveException("It is not your turn");
        }

        // Building board model for checking valid moves
        Board board = new Board();
        board.loadFromFen(game.getFenNotation());

        // Checking if move is valid
        MoveList validMoves = MoveGenerator.generateLegalMoves(board);

        if (!validMoves.toString().contains((from + to).toLowerCase())) {
            throw new InvalidMoveException("Invalid move: " + from + " to: " + to);
        }

        return board;
    }

    private MoveCommand updateGameWithMove(Game game, String from, String to, Board board) {

        HorizontalPosition hPos = PositionUtils.getHorizontalPosition(from);
        VerticalPosition vPos = PositionUtils.getVerticalPosition(from);

        Piece piece = game.getBoard().getPieces()
                .stream()
                .filter(p -> p.getHorizontalPosition() == hPos)
                .filter(p -> p.getVerticalPosition() == vPos)
                .findFirst()
                .orElseThrow(() -> new InvalidMoveException("Start position is empty"));

        log.debug("Updating game with new move and status");

        // Moving piece on Board Model
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.fromValue(from.toUpperCase()),
                Square.fromValue(to.toUpperCase())));

        // Moving piece on Database model
        BoardUtil.movePiece(piece, game.getBoard(), to);

        game.setFenNotation(board.getFen());
        game.increaseTurnCount();

        Move move = Move.builder()
                .pieceType(piece.getPieceType())
                .pieceColor(piece.getPieceColor())
                .horizontalStartPosition(PositionUtils.getHorizontalPosition(from))
                .verticalStartPosition(PositionUtils.getVerticalPosition(from))
                .horizontalEndPosition(PositionUtils.getHorizontalPosition(to))
                .verticalEndPosition(PositionUtils.getVerticalPosition(to))
                .moveCount(game.getTurn() - 1).build();

        game.addMove(move);

        if (board.isMated()) {
            Room room = roomRepository.findRoomByGame(game)
                    .orElseThrow(() -> new RuntimeException("Something went wrong with game logic"));
            room.setGame(GameUtil.initNewGameBetweenPlayers(game.getWhitePlayer(), game.getBlackPlayer()));

            // TODO: 2019-07-21 Archive old game
            game.setIsCheckmate(true);
            move.setIsCheckmate(true);
            gameRepository.save(game);

            // Getting moves from database before they are deleted by saving room (Lazy initiation)
            game.getMoves();

            roomRepository.save(room);

            return moveToMoveCommand.convert(game.getMoves()
                    .stream().max(Comparator.comparing(BaseEntity::getCreated))
                    .orElseThrow(() -> new RuntimeException("Something went really wrong with game and move logic")));

        } else if (board.isDraw()) {
            Room room = roomRepository.findRoomByGame(game)
                    .orElseThrow(() -> new RuntimeException("Something went wrong with game logic"));
            room.setGame(GameUtil.initNewGameBetweenPlayers(game.getWhitePlayer(), game.getBlackPlayer()));

            // TODO: 2019-07-21 Archive old game
            game.setIsDraw(true);
            move.setIsDraw(true);
            gameRepository.save(game);

            // Getting moves from database before they are deleted by saving room (Lazy initiation)
            game.getMoves();

            roomRepository.save(room);

            return moveToMoveCommand.convert(game.getMoves()
                    .stream().max(Comparator.comparing(BaseEntity::getCreated))
                    .orElseThrow(() -> new RuntimeException("Something went really wrong with game and move logic")));
        } else if (board.isKingAttacked()) {
            game.setIsKingAttacked(true);
            move.setIsKingAttacked(true);
        }

        gameRepository.save(game);


        var moveCommand = moveToMoveCommand.convert(game.getMoves()
                .stream().max(Comparator.comparing(BaseEntity::getCreated))
                .orElseThrow(() -> new RuntimeException("Something went really wrong with game and move logic")));

        // TODO: 2019-07-20 send socket message about the game

        return moveCommand;
    }

    @Override
    public Move findById(Long aLong) {
        return null;
    }

    @Override
    public Move save(Move object) {
        return null;
    }

    @Override
    public Set<Move> findAll() {
        return null;
    }

    @Override
    public void delete(Move object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
