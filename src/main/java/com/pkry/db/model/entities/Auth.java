package com.pkry.db.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by arade on 29-Dec-15.
 */
@Entity
public class Auth {

    @Id
    @GeneratedValue
    Long id;

    String login;

    String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "auth")
    List<AuthSession> authSessionList;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<AuthSession> getAuthSessionList() {
        return authSessionList;
    }

    public void setAuthSessionList(List<AuthSession> authSessionList) {
        this.authSessionList = authSessionList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}