package com.pkry.db.model;

/**
 * Created by arade on 04-Jan-16.
 */
public class DbKey {

    private String login;
    private String key;
    private String initVector;

    public DbKey(String login) {
        this.login = login;
    }

    public DbKey(String login, String key, String initVector) {
        this.login = login;
        this.key = key;
        this.initVector = initVector;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInitVector() {
        return initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbKey dbKey = (DbKey) o;

        return !(login != null ? !login.equals(dbKey.login) : dbKey.login != null);

    }

}
