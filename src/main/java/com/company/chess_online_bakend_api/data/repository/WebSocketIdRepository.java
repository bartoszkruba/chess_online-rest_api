/*
 * 8/31/19, 5:47 PM. Updated by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.WebSocketId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketIdRepository extends JpaRepository<WebSocketId, String> {
}
