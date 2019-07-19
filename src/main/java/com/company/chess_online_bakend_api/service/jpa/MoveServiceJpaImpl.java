package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Move;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.exception.GameNotFoundException;
import com.company.chess_online_bakend_api.service.MoveService;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MoveServiceJpaImpl implements MoveService {

    private final GameRepository gameRepository;

    @Autowired
    public MoveServiceJpaImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
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
    public MoveCommand move(String username, Long gameId, String from, String to) {
        return null;
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
