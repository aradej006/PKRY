package com.pkry.db.model.repositories;

import com.pkry.db.model.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Transfer Repository
 */
@Repository
public interface TransferRepo extends JpaRepository<Transfer, Long>{

    List<Transfer> findByFromAccount(String fromAccount);
    List<Transfer> findByToAccount(String toAccount);

}
