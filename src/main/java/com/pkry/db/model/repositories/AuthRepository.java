package com.pkry.db.model.repositories;

import com.pkry.db.model.entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Auth Repository
 */

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    List<Auth> findByLogin(String login);
    List<Auth> findByAccount_Number(String accountNumber);
    List<Auth> findById(Long id);
}
