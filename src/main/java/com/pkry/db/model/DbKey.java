package com.pkry.db.model;

import javax.crypto.SecretKey;

/**
 * Created by arade on 04-Jan-16.
 */
public class DbKey {

    private String login;
    private SecretKey key;

    public DbKey(String login) {
        this.login = login;
    }

    public DbKey(String login, SecretKey key) {
        this.login = login;
        this.key = key;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbKey dbKey = (DbKey) o;

        return !(login != null ? !login.equals(dbKey.login) : dbKey.login != null);

    }

}
