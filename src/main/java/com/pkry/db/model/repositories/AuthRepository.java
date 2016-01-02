package com.pkry.db.model.repositories;

import com.pkry.db.model.entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by arade on 29-Dec-15.
 */
@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    List<Auth> findByLogin(String login);
    List<Auth> findById(Long id);
}
