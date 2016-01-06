package com.pkry.db.model.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Auth entity keeps information about bank account, login, password, and about the sessions the user had
 * in order to modify their bank account.
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Account account;

    public void addAuthSession(AuthSession authSession) {
        if (authSessionList == null) authSessionList = new LinkedList<AuthSession>();
        authSessionList.add(authSession);
    }

    public AuthSession getNewestSession() {
        if( authSessionList != null && authSessionList.size() > 0 ){
            int newestIndex = 0;
            Date newest = authSessionList.get(0).getStartTime();
            for (int i = 0; i < authSessionList.size(); i++) {
                if (newest.before(authSessionList.get(i).getStartTime())) {
                    newestIndex = i;
                    newest = authSessionList.get(i).getStartTime();
                }
            }
            return authSessionList.get(newestIndex);
        }else{
            return null;
        }

    }

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
