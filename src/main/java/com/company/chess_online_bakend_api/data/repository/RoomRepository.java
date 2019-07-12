package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.Room;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoomRepository extends PagingAndSortingRepository<Room, Long> {

    Optional<Room> findByNameLike(String name);
}
