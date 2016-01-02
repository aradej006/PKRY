package com.pkry.db;


import com.pkry.db.model.services.AuthService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class DbModule {

    @Inject
    AuthService authService;

    @PostConstruct
    public void init(){
        authService.toString();//tost
    }

    public int checkLogin(String login) {
        return 4;
    }

    public boolean checkPassword(String login, String password, String passwordIndexes) {
    return true;
    }

    public boolean checkAD(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        return true;
    }

    public String getAccount(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        return "Account";
    }

    public boolean checkMoney(String login, double money){
        return true;
    }

    public boolean doTransfer(String login, double money){
        return true;
    }
}
