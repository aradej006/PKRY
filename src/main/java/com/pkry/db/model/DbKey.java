package com.pkry.db.model;

import org.apache.commons.lang.RandomStringUtils;

import javax.crypto.SecretKey;

/**
 * Class DbKey is responsible for assigning the keys to logins.
 */
public class DbKey {

    /**
     * User's login
     */
    private String login;

    /**
     * A secret symmetric key.
     */
    private SecretKey key;

    private String IV;



    /**
     * Constructor of DBKey. Creates DBKey object.
     * @param login user's login.
     */
    public DbKey(String login) {
        this.login = login;
    }

    /**
     * Constructor of DBKey. Creates DBKey object.
     * @param login user's login.
     * @param key private key assigned to that login.
     */
    public DbKey(String login, SecretKey key) {
        this.login = login;
        this.key = key;
        IV = RandomStringUtils.randomAlphanumeric(16);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public String getIV() {
        return IV;
    }

    /**
     * Overrided equals method that allows to determinate whether or not obtained login maches the one in the key list
     * @param o object of Object class.
     * @return returns <i>true</i> if logins matches.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbKey dbKey = (DbKey) o;

        return !(login != null ? !login.equals(dbKey.login) : dbKey.login != null);

    }

}
