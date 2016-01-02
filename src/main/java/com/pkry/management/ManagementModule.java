package com.pkry.management;

import com.pkry.db.DbModule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class ManagementModule {

    int lenght;

    @Inject
    DbModule dbModule;

    public String Login(String login){
            if(dbModule.checkLogin(login)>0)
                return generateIndexes(dbModule.checkLogin(login));
        return  "error";
    }


    public String insertPassword(String login, String password, String passwordIndexes){
            if(dbModule.checkPassword(login, password, passwordIndexes))
                return generateIndexes(11);

        return "error";

    }

    public String insertAD(String login, String password, String passwordIndexes, String AD, String ADIndexes){
        if(dbModule.checkAD(login, password,passwordIndexes,AD, ADIndexes)){
            return dbModule.getAccount(login, password, passwordIndexes,AD, ADIndexes);

        }
    return null;
    }

    private String generateIndexes(int passwordLength) {




        //generowanie

        String indexes = "1,2,3";

        return indexes;
    }


}

