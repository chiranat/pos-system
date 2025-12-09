package com.chiranat.pos.repository;

import com.chiranat.pos.model.DiningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiningSessionRepository extends JpaRepository<DiningSession, UUID> {
    Optional<DiningSession> findByAccessToken(String accessToken);
    List<DiningSession> findByStatus(DiningSession.SessionStatus status);
    Optional<DiningSession> findByTableIdAndStatus(UUID tableId, DiningSession.SessionStatus status);
}
