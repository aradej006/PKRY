package com.pkry.db.model;

import com.pkry.db.model.dao.AddressDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class AddressService {

    @Inject
    AddressDAO addressDAO;

    @PostConstruct
    public void init(){
        addressDAO.toString();
        addressDAO.findAll();
    }


}
