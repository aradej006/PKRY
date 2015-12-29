package com.pkry.db;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class LoginDB {

    public boolean login(String username, String password){
        return false;
    }


}
