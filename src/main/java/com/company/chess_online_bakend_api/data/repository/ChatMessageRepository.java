package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends PagingAndSortingRepository<ChatMessage, Long> {
    Page<ChatMessage> findByRoom(Room room, Pageable pageable);

    Long countByRoom(Room room);
}
