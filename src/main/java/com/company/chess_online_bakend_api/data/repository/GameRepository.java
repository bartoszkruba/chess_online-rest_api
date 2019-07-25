package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Room;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends PagingAndSortingRepository<Game, Long> {

    Optional<Game> findGameByRoom(Room room);
}
