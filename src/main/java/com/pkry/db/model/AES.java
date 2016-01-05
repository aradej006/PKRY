package com.pkry.db.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

/**
 * Created by arade on 04-Jan-16.
 */
@Named
@ApplicationScoped
public class AES {

    List<DbKey> keys;

    private Cipher cipher;
    private KeyGenerator keyGenerator;

    @PostConstruct
    public void init(){
        try {
            cipher = Cipher.getInstance("AES");
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(DbKey secretKey, String plainText){
        if( plainText == null ) return null;
        byte[] plainTextByte = plainText.getBytes();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey.getKey());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encryptedByte = new byte[0];
        try {
            encryptedByte = cipher.doFinal(plainTextByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encryptedByte);
    }

    public String decrypt(DbKey secretKey, String encryptedText){
        if( encryptedText == null ) return null;

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey.getKey());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] decryptedByte = new byte[0];
        try {
            decryptedByte = cipher.doFinal(encryptedTextByte);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }

    public DbKey getKey(String login){
        if( keys != null && keys.contains(new DbKey(login))) return findByLogin(login);

        if(keys==null) keys = new LinkedList<DbKey>();

        DbKey key = new DbKey(login, keyGenerator.generateKey());
        keys.add(key);
        return key;
    }

    private DbKey findByLogin(String login){
        for (DbKey key : keys) {
            if(key.getLogin().equals(login))
                return key;
        }
        return null;
    }

    public List<DbKey> getKeys(){
        return keys;
    }

}
