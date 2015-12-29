package com.pkry.management;

import com.pkry.db.LoginDB;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class Login {

    @Inject
    LoginDB loginDB;

    public boolean login(String username, String password){
        return loginDB.login(username,password);
    }


}
