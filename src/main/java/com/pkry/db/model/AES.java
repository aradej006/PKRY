package com.pkry.db.model;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;

/**
 * Class AES is responsible for encrypting information in data base by AES algorithm.
 */
@Named
@ApplicationScoped
public class AES {

    /**
     * List of DBKey class with private keys
     */
    List<DbKey> keys;

    /**
     * Object of Chiper class
     */
    private Cipher cipher;

    /**
     * Object of KeyGenetator class that allows to create keys.
     */
    private KeyGenerator keyGenerator;

    @PostConstruct
    public void init(){
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypting function, that encrypts received text.
     * @param secretKey key which is used to encrypt data.
     * @param plainText plain text to encrypt.
     * @return encrypted text.
     */
    public String encrypt(DbKey secretKey, String plainText){
        if( plainText == null ) return null;
        byte[] plainTextByte = plainText.getBytes();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey.getKey(),new IvParameterSpec(secretKey.getIV().getBytes("UTF-8")));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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

    /**
     * Function decrypts received, encrypted text.
     * @param secretKey key to use to decrypt data.
     * @param encryptedText encrypted text to encrypt.
     * @return plain, decrypted text.
     */
    public String decrypt(DbKey secretKey, String encryptedText){
        if( encryptedText == null ) return null;

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey.getKey(),new IvParameterSpec(secretKey.getIV().getBytes("UTF-8")));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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

    /**
     * Gets the key that is assigned to the login. If there is no key assigned for the login, new one is created and
     * then returned.
     * @param login user's login
     * @return secret key assigned for the login.
     */
    public DbKey getKey(String login){
        if( keys != null && keys.contains(new DbKey(login))) return findByLogin(login);

        if(keys==null) keys = new LinkedList<DbKey>();

        DbKey key = new DbKey(login, keyGenerator.generateKey());
        keys.add(key);
        return key;
    }

    /**
     * Searches for the key assigned for the login.
     * @param login user's login
     * @return returns a key assigned to that login
     */
    private DbKey findByLogin(String login){
        for (DbKey key : keys) {
            if(key.getLogin().equals(login))
                return key;
        }
        return null;
    }

    /**
     * Gets the list of keys.
     * @return list of keys.
     */
    public List<DbKey> getKeys(){
        return keys;
    }

}
