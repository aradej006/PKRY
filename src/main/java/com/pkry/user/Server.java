package com.pkry.user;

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
    public void init(){

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
    }

    public String doHash(String data)throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());

        return hash.toString();
    }

    public void send(String data){

    }

    public Key getPublicKey(){
        return publicKey;
    }

    public String clientLogin(String data){
        return userModule.Login(data);
    }

    public String clientPassword(String data){
        return userModule.Password(data);
    }

    public String clientAD(String data){
        return userModule.AD(data);
    }

    public String clientDoTransfer(String data){
        return userModule.doTransfer(data);
    }

    public void handleClientMessage(String data){
        if(data.contains("GetPublicKey")){
            send(getPublicKey().toString());
        }
        else if(data.contains("Login")){
            send(clientLogin(data));
        }
        else if(data.contains("Password")){
            send(clientPassword(data));
        }
        else if(data.contains("AD")){
            send(clientAD(data));
        }
        else if(data.contains("DoTransfer")){
            send(clientDoTransfer(data));
        }
    }
}
