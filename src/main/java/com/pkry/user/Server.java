package com.pkry.user;

import com.pkry.db.DbModule;
import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.services.AuthService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.security.*;

@Startup
@Singleton
public class Server {

    @Inject
    UserModule userModule;

    private Key publicKey;
    private Key privateKey;

    @PostConstruct
    public void init() {

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println("WorkingSERVER");
        test();
        System.out.println("KONIEC");

    }

    public String doHash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());

        return hash.toString();
    }

    public Key getPublicKey() {
        return publicKey;
    }

    public String clientLogin(String login) {
        return userModule.Login(login);
    }

    public String clientPassword(String login,String password, String passwordIndexes){
        return userModule.insertPassword(login, password, passwordIndexes);
    }

    public AccountDTO clientAD(String login,String password, String passwordIndexes, String AD, String ADIndexes){
        return userModule.insertAD(login, password, passwordIndexes, AD, ADIndexes);
    }

    public boolean clientDoTransfer(String login, Double money){
        return userModule.doTransfer(login,money);
    }

    public boolean clientLogout(String login){
        return userModule.logout(login);
    }

    public String handleClientMessage(String data){
        String message = null;
        if(data.contains("GetPublicKey")){
            message = getPublicKey().toString();
        }
        else if(data.contains("Login")){
            String[] array = data.split(" ");
            message = clientLogin(array[1]);
            System.out.println(message);
        }
        else if(data.contains("Password")){
            String[] array = data.split(" ");
            message = clientPassword(array[1],array[2],array[3]);
        }
        else if(data.contains("AD")){
            String[] array = data.split(" ");
            AccountDTO accountDTO = clientAD(array[1],array[2],array[3],array[4],array[5]);
        }
        else if(data.contains("DoTransfer")){
            String[] array = data.split(" ");

            boolean okFlag = clientDoTransfer(array[1],Double.parseDouble(array[2]));
            if (okFlag)
                message = "UDAŁOSIĘ";
            else
                message = "NIEUDAŁOSIĘ";
        }

        else if(data.contains("Logout")){
            String[] array = data.split(" ");
            if(clientLogout(array[1])) {
                message = "LogoutOK";
            }
            else {
                message = "LogoutFalse";
            }
        }
        return message;
    }
    public AccountDTO handleAD(String data){
        AccountDTO accountDTO = null;
        if(data.contains("AD")) {
            String[] array = data.split(" ");
            accountDTO = clientAD(array[1],array[2],array[3],array[4],array[5]);
        }
        return accountDTO;
    }
    public void test(){
        String publicKey = null;
        String passwordIndexes = null;
        String ADIndexes = null;
        String account = null;
        String transfer = null;
        String logout = null;
        String login = "Adrian";
        AccountDTO accountDTO = null;

        publicKey = handleClientMessage("GetPublicKey");
        passwordIndexes = handleClientMessage("Login" + " " + login);
        ADIndexes = handleClientMessage("Password" + " " + login + " " + "Rad" + " " + passwordIndexes);
        accountDTO = handleAD("AD" + ' ' + login + " " + "Rad" + " " + passwordIndexes + "cyferki z peselu" + " " + ADIndexes);
        transfer = handleClientMessage("DoTransfer" + " " + login + " " + "21312231");
        logout = handleClientMessage("Logout" + " " + login);
        System.out.println(transfer);
        System.out.println(logout);
    }
}
