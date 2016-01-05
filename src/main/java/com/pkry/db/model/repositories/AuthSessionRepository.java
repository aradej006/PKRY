package com.pkry.db.model.repositories;

import com.pkry.db.model.entities.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by arade on 05-Jan-16.
 */
@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSession, Long>{

    List<AuthSession> findBySessionId(String sessionId);

}
