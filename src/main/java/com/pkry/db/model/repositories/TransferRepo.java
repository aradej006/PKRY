package com.pkry.db.model.repositories;

import com.pkry.db.model.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by arade on 03-Jan-16.
 */
@Repository
public interface TransferRepo extends JpaRepository<Transfer, Long>{

    List<Transfer> findByFromAccount(String fromAccount);

}
