package com.pkry.db.model;

import com.pkry.db.model.entities.Auth;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Singleton;
import javax.inject.Named;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by arade on 04-Jan-16.
 */
@Singleton
@Named
public class AES {

    List<DbKey> keys;

    public static String encrypt(DbKey key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key.getInitVector().getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getKey().getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(DbKey key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key.getInitVector().getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getKey().getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public DbKey getKey(String login){
        if( keys != null && keys.contains(new DbKey(login))) return findByLogin(login);

        if(keys==null) keys = new LinkedList<DbKey>();
        DbKey key = new DbKey(login, generateKey(), generateInitialVector());
        keys.add(key);
        return key;
    }

    private String generateKey(){
        return RandomStringUtils.randomAlphanumeric(16);
    }

    private String generateInitialVector(){
        return RandomStringUtils.randomAlphanumeric(16);
    }

    private DbKey findByLogin(String login){
        for (DbKey key : keys) {
            if(key.getLogin().equals(login))
                return key;
        }
        return null;
    }

}
