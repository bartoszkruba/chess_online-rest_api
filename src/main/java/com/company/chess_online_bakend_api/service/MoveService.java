package com.company.chess_online_bakend_api.service;

import com.company.chess_online_bakend_api.data.command.MoveCommand;
import com.company.chess_online_bakend_api.data.model.Move;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

import java.util.List;
import java.util.Set;

public interface MoveService extends CrudService<Move, Long> {

    Set<MoveCommand> getPossibleMoves(String from, Long gameId) throws MoveGeneratorException;

    MoveCommand performMove(String username, Long gameId, String from, String to) throws MoveGeneratorException;

    List<MoveCommand> getGameMoves(Long gameId);
}
