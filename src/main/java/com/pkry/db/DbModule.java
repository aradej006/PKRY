package com.pkry.db;


import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.entities.Account;
import com.pkry.db.model.entities.Auth;
import com.pkry.db.model.services.AuthService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class DbModule {

    @Inject
    AuthService authService;

    @PostConstruct
    public void init() {
        authService.toString();
        authService.initAuthDAO();
    }

    public int checkLogin(String login) {
        List<Auth> authList = authService.getAuthByLogin(login);
        if (authList.size() == 0) return 0;
        else {
            return authList.get(0).getPassword().length();
        }
    }

    public boolean checkPassword(String login, String password, String passwordIndexes) {
        String passwd = authService.getAuthByLogin(login).get(0).getPassword();
        for (int i = 0; i < passwordIndexes.length(); i++) {
            if (passwd.charAt(Integer.parseInt("" + passwordIndexes.charAt(i))) != password.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkAD(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        Auth auth = authService.getAuthByLogin(login).get(0);
        if (checkPassword(login, password, passwordIndexes)) {
            for (int i = 0; i < adIndexes.length(); i++) {
                if (auth.getAccount().getOwner().getPesel().charAt(Integer.parseInt("" + adIndexes.charAt(i))) != ad.charAt(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public AccountDTO getAccount(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        return new AccountDTO();
    }

    public boolean checkMoney(String login, double money) {
        return true;
    }

    public boolean doTransfer(String login, double money) {
        return true;
    }


    public boolean logout(String login) {
        return true;
    }
}
