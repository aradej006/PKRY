package com.pkry.db.model.services;

import com.pkry.db.model.entities.Transfer;
import com.pkry.db.model.repositories.TransferRepo;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by arade on 03-Jan-16.
 */

/**
 * TransferService communicates with TransferRepo and allows to save transfers of find the list of transfers sent from
 * the account received from other accounts.
 */
@Stateless
@Named
public class TransferService {

    /**
     * Injected object of TransferRepo class
     */
    @Inject
    TransferRepo transferRepo;

    @PostConstruct
    public void init(){
        transferRepo.toString();
    }

    /**
     * Saves transfer
     * @param transfer transfer to save
     */
    public void save(Transfer transfer){
        transferRepo.save(transfer);
    }

    /**
     * Finds the list of transfers made from the account.
     * @param fromAccount account's number.
     * @return list of transfers made from that account.
     */
    public List<Transfer> findByFromAccount(String fromAccount){
        return  transferRepo.findByFromAccount(fromAccount);
    }

    /**
     * Finds the list of transfers received by the account.
     * @param toAccount account's number.
     * @return list of transfers received by that account.
     */
    public List<Transfer> findByToAccount(String toAccount){
        return transferRepo.findByToAccount(toAccount);
    }



}
