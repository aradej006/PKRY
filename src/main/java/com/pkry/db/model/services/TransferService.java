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
@Stateless
@Named
public class TransferService {

    @Inject
    TransferRepo transferRepo;

    @PostConstruct
    public void init(){
        transferRepo.toString();
    }

    public void save(Transfer transfer){
        transferRepo.save(transfer);
    }

    public List<Transfer> findByFromAccount(String fromAccount){
        return  transferRepo.findByFromAccount(fromAccount);
    }




}
