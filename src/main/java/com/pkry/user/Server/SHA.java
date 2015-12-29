package com.pkry.user.Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {

    public String hash(String data)throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());

        return hash.toString();
    }
}
