package com.pkry.user;

import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.user.server.Server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class ServerModule {

    @Inject
    UserModule userModule;

    @Inject
    Server server;

    @PostConstruct
    public void init() {

        System.out.println("WorkingSERVER");
//        test();
        System.out.println("KONIEC");

        server.setPort(7000);
        if (!server.isRunning()) server.start();
    }

    @PreDestroy
    public void destroy() {
        server.close();
    }

    public String clientLogin(String login) {
        return userModule.Login(login);
    }

    public String clientPassword(String login, String password, String passwordIndexes) {
        return userModule.insertPassword(login, password, passwordIndexes);
    }

    public String clientAD(String login, String password, String passwordIndexes, String AD, String ADIndexes) {
        return userModule.insertAD(login, password, passwordIndexes, AD, ADIndexes);
    }

    public String clientDoTransfer(String login,String sessionId, Double money, String toAccount) {
        return userModule.doTransfer(login, sessionId, money, toAccount);
    }

    public boolean clientLogout(String login, String sessionId) {
        return userModule.logout(login, sessionId);
    }

    public String handleClientMessage(String data) {
        String message = null;
        if (data.contains("Login")) {
            String[] array = data.split(" ");
            message = clientLogin(array[1]);
        } else if (data.contains("Password")) {
            String[] array = data.split(" ");
            message = clientPassword(array[1], array[2], array[3]);
        } else if (data.contains("AD")) {
            String[] array = data.split(" ");
            String accountDTO = clientAD(array[1], array[2], array[3], array[4], array[5]);
        } else if (data.contains("DoTransfer")) {
            String[] array = data.split(" ");

            message = clientDoTransfer(array[1],array[2], Double.parseDouble(array[3]), array[4]);
        } else if (data.contains("Logout")) {
            String[] array = data.split(" ");
            if (clientLogout(array[1], array[2])) {
                message = "LogoutOK";
            } else {
                message = "LogoutFalse";
            }
        }
        return message;
    }

    public String handleAD(String data) {
        String accountDTO = null;
        if (data.contains("AD")) {
            String[] array = data.split(" ");
            accountDTO = clientAD(array[1], array[2], array[3], array[4], array[5]);
        }
        return accountDTO;
    }

    public void test() {
        String publicKey = null;
        String passwordIndexes = null;
        String ADIndexes = null;
        String account = null;
        String transfer = null;
        String logout = null;
        String login = "Adrian";
        String sessionId = null;
        AccountDTO accountDTO = null;

        System.out.println("LOGIN");
        passwordIndexes = handleClientMessage("Login" + " " + login);
        passwordIndexes = "1,2,3";
        System.out.println("PASSWORD");
        ADIndexes = handleClientMessage("Password" + " " + login + " " + "Rad" + " " + passwordIndexes);
        ADIndexes = "1,2,3";
        System.out.println("AD");
        sessionId = handleAD("AD" + ' ' + login + " " + "Rad" + " " + passwordIndexes + " 000" + " " + ADIndexes);
        System.out.println("GET_ACCOUNT");
        accountDTO = userModule.getAccount(sessionId, login);
        System.out.println("DoTRANSFER");
        transfer = handleClientMessage("DoTransfer" + " " + login + " " + sessionId +  " " + "1000" + " 00000000000000000000000000");
        logout = handleClientMessage("Logout" + " " + login + " " + sessionId);
        System.out.println(transfer);
        System.out.println(logout);
    }
}
