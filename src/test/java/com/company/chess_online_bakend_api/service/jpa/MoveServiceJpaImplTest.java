package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.converter.move.MoveToMoveCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.*;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import com.company.chess_online_bakend_api.exception.ForbiddenException;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.exception.InvalidMoveException;
import com.company.chess_online_bakend_api.exception.UserNotFoundException;
import com.company.chess_online_bakend_api.util.GameUtil;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveServiceJpaImplTest {

    @Mock
    GameRepository gameRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    MoveToMoveCommand moveToMoveCommand;

    @InjectMocks
    MoveServiceJpaImpl moveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPossibleMoves() throws MoveGeneratorException {
        Game game = GameUtil.initNewGame();
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        MoveCommand moveCommand1 = MoveCommand.builder().from("B1").to("C3").build();
        MoveCommand moveCommand2 = MoveCommand.builder().from("B1").to("A3").build();

        Set<MoveCommand> moveCommands = moveService.getPossibleMoves("B1", 1L);

        assertEquals(2, moveCommands.size());
        assertTrue(moveCommands.contains(moveCommand1));
        assertTrue(moveCommands.contains(moveCommand2));

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void getPossibleMovesNullFrom() throws MoveGeneratorException {
        Game game = GameUtil.initNewGame();
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        MoveCommand moveCommand1 = MoveCommand.builder().from("B1").to("C3").build();
        MoveCommand moveCommand2 = MoveCommand.builder().from("B1").to("A3").build();

        Set<MoveCommand> moveCommands = moveService.getPossibleMoves(null, 1L);

        assertEquals(20, moveCommands.size());
        assertTrue(moveCommands.contains(moveCommand1));
        assertTrue(moveCommands.contains(moveCommand2));

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void getPossibleMovesInvalidGameID() throws MoveGeneratorException {

        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> moveService.getPossibleMoves(null, 1L));
        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void performMoveInvalidGameId() throws MoveGeneratorException {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> {
            moveService.performMove("", 1L, "", "");
        });

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);
        verifyZeroInteractions(userRepository);
        verifyZeroInteractions(roomRepository);
        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveInvalidUsername() {
        String username = "username";

        when(gameRepository.findById(1L)).thenReturn(Optional.of(Game.builder().build()));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            moveService.performMove(username, 1L, "", "");
        });

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);
        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveOnlyOnePlayerWhite() {
        String username = "username";

        Game game = Game.builder()
                .id(1L)
                .whitePlayer(User.builder().build()).build();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(User.builder().build()));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 1L, "", "");
        });

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveOnlyOnePlayerBlack() {
        String username = "username";

        Game game = Game.builder()
                .id(1L)
                .blackPlayer(User.builder().build()).build();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(User.builder().build()));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 1L, "", "");
        });

        verify(gameRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveUserNotPartOfTheGame() {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username("user2").build();

        Game game = Game.builder().id(3L).whitePlayer(user1).blackPlayer(user2).build();

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username))
                .thenReturn(Optional.of(User.builder().username(username).build()));

        assertThrows(ForbiddenException.class, () -> {
            moveService.performMove(username, 3L, "", "");
        });

        verify(gameRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performFirstMoveNotWhite() {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = Game.builder()
                .id(3L)
                .whitePlayer(user1)
                .blackPlayer(user2)
                .status(GameStatus.WAITNG_TO_START).build();

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 3L, "", "");
        });

        verify(gameRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveGameFinished() {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = Game.builder()
                .id(3L)
                .whitePlayer(user1)
                .blackPlayer(user2)
                .status(GameStatus.FINISHED).build();

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 3L, "", "");
        });

        verify(gameRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveNotPlayerTurnWhite() {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = Game.builder()
                .id(3L)
                .blackPlayer(user1)
                .whitePlayer(user2)
                .turn(2)
                .status(GameStatus.STARTED).build();

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 3L, "", "");
        });

        verify(gameRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performMoveNotPlayersTurnBlack() {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = Game.builder()
                .id(3L)
                .whitePlayer(user1)
                .blackPlayer(user2)
                .turn(3)
                .status(GameStatus.STARTED).build();

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 3L, "", "");
        });

        verify(gameRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performInvalidMove() {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = GameUtil.initNewGameBetweenPlayers(user2, user1);
        game.setId(3L);

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        assertThrows(InvalidMoveException.class, () -> {
            moveService.performMove(username, 3L, "D2", "D8");
        });

        verify(gameRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verifyZeroInteractions(moveToMoveCommand);
    }

    @Test
    void performValidMoveWhite() throws MoveGeneratorException {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = GameUtil.initNewGameBetweenPlayers(user2, user1);
        game.setId(3L);

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        Game gameAfterMove = GameUtil.initNewGameBetweenPlayers(user2, user1);
        gameAfterMove.setId(3L);
        gameAfterMove.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == HorizontalPosition.D)
                .filter(piece -> piece.getVerticalPosition() == VerticalPosition.TWO)
                .forEach(piece -> {
                    piece.setVerticalPosition(VerticalPosition.THREE);
                    piece.increaseMoveCount();
                });
        gameAfterMove.setFenNotation("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1");
        gameAfterMove.increaseTurnCount();
        gameAfterMove.setStatus(GameStatus.STARTED);
        Move generatedMove = Move.builder()
                .moveCount(1)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.PAWN)
                .horizontalStartPosition(HorizontalPosition.D)
                .verticalStartPosition(VerticalPosition.TWO)
                .horizontalEndPosition(HorizontalPosition.D)
                .verticalEndPosition(VerticalPosition.THREE).build();
        gameAfterMove.addMove(generatedMove);

        when(gameRepository.save(gameAfterMove)).thenReturn(gameAfterMove);

        MoveCommand returnedMove = MoveCommand.builder().id(1L).build();

        when(moveToMoveCommand.convert(gameAfterMove.getMoves().get(0))).thenReturn(returnedMove);

        MoveCommand moveCommand = moveService.performMove(username, 3L, "D2", "D3");

        assertEquals(returnedMove, moveCommand);

        verify(gameRepository, times(1)).findById(3L);
        verify(gameRepository, times(1)).save(any());
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verify(moveToMoveCommand, times(1)).convert(gameAfterMove.getMoves().get(0));
        verifyNoMoreInteractions(moveToMoveCommand);
    }

    @Test
    void performValidMoveBlack() throws MoveGeneratorException {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = GameUtil.initNewGameBetweenPlayers(user2, user1);
        game.setId(3L);
        game.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == HorizontalPosition.D)
                .filter(piece -> piece.getVerticalPosition() == VerticalPosition.TWO)
                .forEach(piece -> {
                    piece.setVerticalPosition(VerticalPosition.THREE);
                    piece.increaseMoveCount();
                });

        game.setFenNotation("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1");

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        Game gameAfterMove = GameUtil.initNewGameBetweenPlayers(user2, user1);
        gameAfterMove.setId(3L);
        gameAfterMove.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == HorizontalPosition.D)
                .filter(piece -> piece.getVerticalPosition() == VerticalPosition.TWO)
                .forEach(piece -> {
                    piece.setVerticalPosition(VerticalPosition.THREE);
                    piece.increaseMoveCount();
                });

        gameAfterMove.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == HorizontalPosition.D)
                .filter(piece -> piece.getVerticalPosition() == VerticalPosition.SEVEN)
                .forEach(piece -> {
                    piece.setVerticalPosition(VerticalPosition.SIX);
                    piece.increaseMoveCount();
                });

        gameAfterMove.setFenNotation("rnbqkbnr/ppp1pppp/3p4/8/8/3P4/PPP1PPPP/RNBQKBNR w KQkq - 0 1");
        gameAfterMove.increaseTurnCount();
        gameAfterMove.increaseTurnCount();
        gameAfterMove.setStatus(GameStatus.STARTED);
        Move generatedMove = Move.builder()
                .moveCount(1)
                .pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.PAWN)
                .horizontalStartPosition(HorizontalPosition.D)
                .verticalStartPosition(VerticalPosition.SEVEN)
                .horizontalEndPosition(HorizontalPosition.D)
                .verticalEndPosition(VerticalPosition.SIX).build();
        gameAfterMove.addMove(generatedMove);

        when(gameRepository.save(gameAfterMove)).thenReturn(gameAfterMove);

        MoveCommand returnedMove = MoveCommand.builder().id(1L).build();

        when(moveToMoveCommand.convert(gameAfterMove.getMoves().get(0))).thenReturn(returnedMove);

        MoveCommand moveCommand = moveService.performMove(username, 3L, "D7", "D6");

        assertEquals(returnedMove, moveCommand);

        verify(gameRepository, times(1)).findById(3L);
        verify(gameRepository, times(1)).save(any());
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verify(moveToMoveCommand, times(1)).convert(gameAfterMove.getMoves().get(0));
        verifyNoMoreInteractions(moveToMoveCommand);
    }

    @Test
    void performCheckMove() throws MoveGeneratorException {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = generateCheckPossibleBoard(user2, user1);
        game.setId(3L);

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));

        Game gameAfterMove = generateCheckPossibleBoard(user2, user1);
        gameAfterMove.setId(3L);
        gameAfterMove.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == HorizontalPosition.D)
                .filter(piece -> piece.getVerticalPosition() == VerticalPosition.ONE)
                .forEach(piece -> {
                    piece.setHorizontalPosition(HorizontalPosition.A);
                    piece.setVerticalPosition(VerticalPosition.FOUR);
                    piece.increaseMoveCount();
                });

        Board board = new Board();
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.C2, Square.C3));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.D7, Square.D6));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.D1, Square.A4));

        gameAfterMove.setFenNotation(board.getFen());
        gameAfterMove.increaseTurnCount();
        gameAfterMove.setIsKingAttacked(true);
        Move generatedMove = Move.builder()
                .isKingAttacked(true)
                .moveCount(3)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.QUEEN)
                .horizontalStartPosition(HorizontalPosition.D)
                .verticalStartPosition(VerticalPosition.ONE)
                .horizontalEndPosition(HorizontalPosition.A)
                .verticalEndPosition(VerticalPosition.FOUR).build();
        gameAfterMove.addMove(generatedMove);

        when(gameRepository.save(gameAfterMove)).thenReturn(gameAfterMove);

        MoveCommand returnedMove = MoveCommand.builder().id(1L).isKingAttacked(true).build();

        when(moveToMoveCommand.convert(gameAfterMove.getMoves().get(0))).thenReturn(returnedMove);

        MoveCommand moveCommand = moveService.performMove(username, 3L, "D1", "A4");

        assertEquals(returnedMove, moveCommand);

        verify(gameRepository, times(1)).findById(3L);
        verify(gameRepository, times(1)).save(any());
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verifyZeroInteractions(roomRepository);

        verify(moveToMoveCommand, times(1)).convert(gameAfterMove.getMoves().get(0));
        verifyNoMoreInteractions(moveToMoveCommand);
    }

    @Test
    void performCheckmateMove() throws MoveGeneratorException {
        String username = "username";

        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username(username).build();

        Game game = generateCheckMatePossibleBoard(user1, user2);
        game.setId(3L);

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(userRepository.findByUsernameLike(username)).thenReturn(Optional.of(user2));
        when(roomRepository.findRoomByGame(any())).thenReturn(Optional.of(Room.builder().id(1L).build()));

        Game gameAfterMove = generateCheckMatePossibleBoard(user1, user2);
        gameAfterMove.setId(3L);
        gameAfterMove.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == HorizontalPosition.D)
                .filter(piece -> piece.getVerticalPosition() == VerticalPosition.EIGHT)
                .forEach(piece -> {
                    piece.setHorizontalPosition(HorizontalPosition.H);
                    piece.setVerticalPosition(VerticalPosition.FOUR);
                    piece.increaseMoveCount();
                });

        Board board = new Board();
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.F2, Square.F3));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.E7, Square.E5));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.G2, Square.G4));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.D8, Square.H4));

        gameAfterMove.setFenNotation(board.getFen());
        gameAfterMove.increaseTurnCount();
        gameAfterMove.setIsCheckmate(true);
        Move generatedMove = Move.builder()
                .isCheckmate(true)
                .moveCount(4)
                .pieceColor(PieceColor.BLACK)
                .pieceType(PieceType.QUEEN)
                .horizontalStartPosition(HorizontalPosition.D)
                .verticalStartPosition(VerticalPosition.EIGHT)
                .horizontalEndPosition(HorizontalPosition.H)
                .verticalEndPosition(VerticalPosition.FOUR).build();
        gameAfterMove.addMove(generatedMove);

        when(gameRepository.save(gameAfterMove)).thenReturn(gameAfterMove);

        MoveCommand returnedMove = MoveCommand.builder().id(1L).isKingAttacked(true).build();

        when(moveToMoveCommand.convert(gameAfterMove.getMoves().get(0))).thenReturn(returnedMove);

        MoveCommand moveCommand = moveService.performMove(username, 3L, "D8", "H4");

        assertEquals(returnedMove, moveCommand);

        verify(gameRepository, times(1)).findById(3L);
        verify(gameRepository, times(1)).save(any());
        verifyNoMoreInteractions(gameRepository);

        verify(userRepository, times(1)).findByUsernameLike(username);
        verifyNoMoreInteractions(userRepository);

        verify(roomRepository, times(1)).findRoomByGame(any());
        verify(roomRepository, times(1)).save(any());
        verifyNoMoreInteractions(roomRepository);

        verify(moveToMoveCommand, times(1)).convert(gameAfterMove.getMoves().get(0));
        verifyNoMoreInteractions(moveToMoveCommand);
    }

    private Game generateCheckPossibleBoard(User white, User black) {
        Game game = GameUtil.initNewGameBetweenPlayers(white, black);
        game.setTurn(3);
        game.setStatus(GameStatus.STARTED);

        Board board = new Board();
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.C2, Square.C3));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.D7, Square.D6));

        game.setFenNotation(board.getFen());

        movePiece(game, HorizontalPosition.C, VerticalPosition.TWO, HorizontalPosition.C, VerticalPosition.THREE);
        movePiece(game, HorizontalPosition.D, VerticalPosition.SEVEN, HorizontalPosition.D, VerticalPosition.SIX);

        return game;
    }

    private Game generateCheckMatePossibleBoard(User white, User black) {
        Game game = GameUtil.initNewGameBetweenPlayers(white, black);
        game.setTurn(4);
        game.setStatus(GameStatus.STARTED);

        Board board = new Board();
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.F2, Square.F3));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.E7, Square.E5));
        board.doMove(new com.github.bhlangonijr.chesslib.move.Move(Square.G2, Square.G4));

        game.setFenNotation(board.getFen());

        movePiece(game, HorizontalPosition.F, VerticalPosition.TWO, HorizontalPosition.F, VerticalPosition.THREE);
        movePiece(game, HorizontalPosition.E, VerticalPosition.SEVEN, HorizontalPosition.D, VerticalPosition.FIVE);
        movePiece(game, HorizontalPosition.G, VerticalPosition.TWO, HorizontalPosition.G, VerticalPosition.FOUR);

        return game;
    }


    private void movePiece(Game game, HorizontalPosition hPosFrom, VerticalPosition vPosFrom,
                           HorizontalPosition hPosTo, VerticalPosition vPosTo) {

        game.getBoard().getPieces()
                .stream()
                .filter(piece -> piece.getHorizontalPosition() == hPosFrom)
                .filter(piece -> piece.getVerticalPosition() == vPosFrom)
                .forEach(piece -> {
                    piece.setHorizontalPosition(hPosTo);
                    piece.setVerticalPosition(vPosTo);
                    piece.increaseMoveCount();
                });
    }
}