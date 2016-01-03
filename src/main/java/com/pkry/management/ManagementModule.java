package com.pkry.management;

import com.pkry.db.DbModule;
import com.pkry.db.model.DTOs.AccountDTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class ManagementModule {

    @Inject
    DbModule dbModule;

    public String Login(String login){
            if(dbModule.checkLogin(login)>0)
                return generateIndexes(dbModule.checkLogin(login));
        return  null;
    }


    public String insertPassword(String login, String password, String passwordIndexes){
            if(dbModule.checkPassword(login, password, passwordIndexes))
                return generateIndexes(11); //skoro pesel to raczej 11 na stałe wpisujemy, chyba że ma być to w jakimś pliku config czy cuś

        return null;

    }

    public AccountDTO insertAD(String login, String password, String passwordIndexes, String AD, String ADIndexes){
        if(dbModule.checkAD(login, password,passwordIndexes,AD, ADIndexes)){
            return dbModule.getAccount(login, password, passwordIndexes,AD, ADIndexes);

        }
    return null;
    }

    public boolean doTransfer(String login, double money){
        if(dbModule.checkMoney(login, money))
            return dbModule.doTransfer(login, money);
        return false;
    }
    private String generateIndexes(int passwordLength) {

        Random rand = new Random();
        List<Integer> list = new ArrayList<Integer>();
        int indexCount=0;
        int index;
        while(indexCount <=2 || indexCount >= 5) {
            indexCount = rand.nextInt(11)+1;
        }

        while(list.size() < indexCount){

            index = rand.nextInt(11)+1;
            if(!list.contains(index)){
                list.add(index);
            }

        }
        Collections.sort(list);

       // System.out.println(list.toString().replace("[", "").replace("]", ""));

        String indexes = "1,2,3";
//        String indexes1 = list.toString().replace("[", "").replace("]", "").replace(" ", "");

        return indexes;
    }


    public boolean logout(String login){
        return dbModule.logout(login);
    }


}

