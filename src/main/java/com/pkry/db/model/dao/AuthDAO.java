package com.pkry.db.model.dao;

import com.pkry.db.model.entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by arade on 29-Dec-15.
 */
@Repository
public interface AuthDAO extends JpaRepository<Auth, Long>{
//    List<Auth> findByLogin(String login);
}
