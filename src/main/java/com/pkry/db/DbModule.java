package com.pkry.db;


import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.entities.Account;
import com.pkry.db.model.entities.Auth;
import com.pkry.db.model.entities.AuthSession;
import com.pkry.db.model.entities.Transfer;
import com.pkry.db.model.repositories.TransferRepo;
import com.pkry.db.model.services.AuthService;
import com.pkry.db.model.services.TransferService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ApplicationScoped
public class DbModule {

    @Inject
    AuthService authService;

    @Inject
    TransferService transferService;


    @PostConstruct
    public void init() {
        authService.toString();
        authService.initAuthDAO();

        transferService.toString();
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
        String []passwdIndexes = passwordIndexes.split(",");

        for (int i=0; i< passwdIndexes.length; i++){
            if(password.charAt(i)!= passwd.charAt(Integer.parseInt(passwdIndexes[i])))
                return false;
        }
//
//        for (int i = 0; i < passwordIndexes.length(); i++) {
//            if (passwd.charAt(Integer.parseInt("" + passwordIndexes.charAt(i))) != password.charAt(i)) {
//                return false;
//            }
//        }
        return true;
    }

    public boolean checkAD(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        Auth auth = authService.getAuthByLogin(login).get(0);
        String []adIndex = adIndexes.split(",");

        if(checkPassword(login, password, passwordIndexes)) {
            for(int i=0; i< adIndex.length; i++){
                if(auth.getAccount().getOwner().getPesel().charAt(Integer.parseInt(adIndex[i])) !=ad.charAt(i) )
                    return false;
        }

//        if (checkPassword(login, password, passwordIndexes)) {
//            for (int i = 0; i < adIndexes.length(); i++) {
//                if (auth.getAccount().getOwner().getPesel().charAt(Integer.parseInt("" + adIndexes.charAt(i))) != ad.charAt(i)) {
//                    return false;
//                }
//            }
            AuthSession authSession = new AuthSession();
            Date date = new Date(System.currentTimeMillis());
            authSession.setUp(true);
            authSession.setUpdateTime(date);
            authSession.setStartTime(date);
            authSession.setMaxSessionTime(1000);
            authSession.setSessionId("asda2dadw23dsadw2e");
            authSession.setAuth(auth);
            auth.addAuthSession(authSession);
            authService.save(auth);
            return true;
        } else {
            return false;
        }

    }

    public AccountDTO getAccount(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        return new AccountDTO();
    }

    public String checkMoney(String login, double money) {
        Auth auth = authService.getAuthByLogin(login).get(0);
        if (updateSession(auth).equals("ACTIVE")) {
            if (auth.getAccount().getBalance() >= money) {
                return "MONEY OK";
            } else {
                return "LACK OF MONEY";
            }
        } else {
            return "SESSION EXPIRED";
        }
    }

    public String doTransfer(String login, double money, String accountNumber) {
        if (authService.getAuthByAccount_Number(accountNumber).size() > 0) {
            Auth authFrom = authService.getAuthByLogin(login).get(0);
            Auth authTo = authService.getAuthByAccount_Number(accountNumber).get(0);
            if (updateSession(authFrom).equals("ACTIVE")) {
                authFrom.getAccount().setBalance(authFrom.getAccount().getBalance() - money);
                authTo.getAccount().setBalance(authTo.getAccount().getBalance() + money);
                // save history
                Transfer transfer = new Transfer();
                transfer.setFromAccount(authFrom.getAccount().getNumber());
                transfer.setToAccount(authTo.getAccount().getNumber());
                transfer.setCurrency(authFrom.getAccount().getCurrency());
                transfer.setAmount(money);
                transfer.setTransferDate(new Date());
                transferService.save(transfer);
                return "TRANSFER DONE";
            } else {
                return "SESSION EXPIRED";
            }
        } else {
            return "DESTINATION NUBER IS NOT CORRECT";
        }
    }

    private String updateSession(Auth auth) {
        boolean isup = auth.getNewestSession().isUp();
        if(auth.getNewestSession().isUp()){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm");
            Date d1 = new Date(auth.getNewestSession().getUpdateTime().getTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(d1);
            cal.add(Calendar.SECOND, auth.getNewestSession().getMaxSessionTime());


            if (cal.getTime().after(new Date(System.currentTimeMillis()))) {
                authService.save(auth);
                return "ACTIVE";
            }
            auth.getNewestSession().setUp(false);
            return "EXPIRED";
        }
        return "EXPIRED";
    }


    public boolean logout(String login) {
        Auth auth = authService.getAuthByLogin(login).get(0);
        auth.getNewestSession().setUp(false);
        return true;
    }
}
