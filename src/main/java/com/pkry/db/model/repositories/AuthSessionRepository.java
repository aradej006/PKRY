package com.pkry.db.model.repositories;

import com.pkry.db.model.entities.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Auth Session Repository
 */
@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSession, Long>{

    List<AuthSession> findBySessionId(String sessionId);

}
