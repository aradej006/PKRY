package com.pkry.user;
import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.management.ManagementModule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserModule{

    @Inject
    ManagementModule managementModule;

    public String Login(String login) {
        return managementModule.Login(login);
    }

    public String insertPassword(String login,String password, String passwordIndexes){
        return managementModule.insertPassword(login, password, passwordIndexes);
    }

    public AccountDTO insertAD(String login,String password, String passwordIndexes, String AD, String ADIndexes){
        return managementModule.insertAD(login, password, passwordIndexes, AD, ADIndexes);
    }

    public String doTransfer(String login, Double money, String toAccount){
        return managementModule.doTransfer(login,money, toAccount);
    }

    public boolean logout(String login){
        return managementModule.logout(login);
    }
}
