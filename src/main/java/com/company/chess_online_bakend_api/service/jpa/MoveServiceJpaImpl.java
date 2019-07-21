package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.converter.move.MoveToMoveCommand;
import com.company.chess_online_bakend_api.data.model.*;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.exception.InvalidMoveException;
import com.company.chess_online_bakend_api.exception.UnauthorizedException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.service.MoveService;
import com.company.chess_online_bakend_api.util.BoardUtil;
import com.company.chess_online_bakend_api.util.PositionUtils;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MoveServiceJpaImpl implements MoveService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private final GameToGameCommand gameToGameCommand;
    private final GameCommandToGame gameCommandToGame;

    private final MoveToMoveCommand moveToMoveCommand;

    @Autowired
    public MoveServiceJpaImpl(GameRepository gameRepository, UserRepository userRepository,
                              GameToGameCommand gameToGameCommand, GameCommandToGame gameCommandToGame,
                              MoveToMoveCommand moveToMoveCommand) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
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


    // TODO: 2019-07-20 write tests
    // Test cases:
    // No Game
    // No Username
    // Not Players Turn
    // Player not part of the Game
    // Only one Player
    // Game finished
    // Invalid move
    @Override
    public MoveCommand performMove(String username, Long gameId, String from, String to) throws MoveGeneratorException {
        log.debug("Performing move for game with id " + gameId + ", from position: " + from + " to: " + to);

        // Checking if game exist
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " does not exist"));

        Piece foundPiece = validateMoveAction(game, username, from, to);

        MoveCommand moveCommand = updateGameWithMove(game, foundPiece, from, to);

        return moveCommand;
    }

    private Piece validateMoveAction(Game game, String username, String from, String to) throws MoveGeneratorException {
        log.debug("Validating move");

        // Checking if user exist
        userRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist"));

        User whitePlayer = game.getWhitePlayer();
        User blackPlayer = game.getBlackPlayer();

        // Checking if player is part of the game
        if (whitePlayer != null && whitePlayer.getUsername() != null && !whitePlayer.getUsername().equals(username)
                && blackPlayer != null && blackPlayer.getUsername() != null &&
                !blackPlayer.getUsername().equals(username)) {
            throw new UnauthorizedException("You are not authorized to make moves");
        }

        // Changing game status to STARTED if there are two players joined
        if (game.getStatus() == GameStatus.WAITNG_TO_START && whitePlayer != null & blackPlayer != null) {
            game.setStatus(GameStatus.STARTED);
        }

        // Checking if game not finished or there is just one player
        if (game.getStatus() != GameStatus.STARTED) {
            throw new InvalidMoveException("Game not started yet or already finished");
        }

        HorizontalPosition hPos = PositionUtils.getHorizontalPosition(from);
        VerticalPosition vPos = PositionUtils.getVerticalPosition(from);

        Piece foundPiece = game.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == hPos)
                .filter(piece -> piece.getVerticalPosition() == vPos)
                .findFirst()
                .orElseThrow(() -> new InvalidMoveException("Start position is empty"));

        // Checking if it is players turn
        if (game.getTurn() % 2 == 0 && blackPlayer != null && blackPlayer.getUsername() != null
                && !blackPlayer.getUsername().equals(username)) {
            throw new InvalidMoveException("It is not your turn");
        } else if (whitePlayer != null && whitePlayer.getUsername() != null &&
                !whitePlayer.getUsername().equals(username)) {
            throw new InvalidMoveException("It is not your turn");
        }

        // Building board model for checking valid moves
        Board board = new Board();
        board.loadFromFen(game.getFenNotation());

        // Checking if move is valid
        MoveList validMoves = MoveGenerator.generateLegalMoves(board);
        if (!validMoves.toString().contains(from + to)) {
            throw new InvalidMoveException("Invalid move: " + from + " to: " + to);
        }

        return foundPiece;
    }

    private MoveCommand updateGameWithMove(Game game, Piece piece, String from, String to) {
        Board board = new Board();
        board.loadFromFen(game.getFenNotation());

        // Moving piece on Board Model
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.fromValue(from), Square.fromValue(to)));
        // Moving piece on Database model
        BoardUtil.movePiece(piece, game.getBoard(), to);

        // TODO: 2019-07-20 check if draw or checkmate or king is attacked

        game.setFenNotation(board.getFen());

        if (game.getTurn() == null || game.getTurn() == 0) {
            game.setTurn(2);
        } else {
            game.increaseTurnCount();
        }

        Move move = Move.builder()
                .pieceType(piece.getPieceType())
                .pieceColor(piece.getPieceColor())
                .horizontalStartPosition(PositionUtils.getHorizontalPosition(from))
                .verticalStartPosition(PositionUtils.getVerticalPosition(from))
                .horizontalEndPosition(PositionUtils.getHorizontalPosition(to))
                .verticalEndPosition(PositionUtils.getVerticalPosition(to))
                .moveCount(game.getTurn() - 1).build();
        game.addMove(move);

        gameRepository.save(game);

        // TODO: 2019-07-20 send socket message about the game

        return moveToMoveCommand.convert(game.getMoves()
                .stream().max(Comparator.comparing(BaseEntity::getCreated))
                .orElseThrow(() -> new RuntimeException("Something went really wrong with game and move logic")));
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
