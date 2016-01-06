package com.pkry.db.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Account entity keeps information about user's account - balancy, currency which they have their money in, and accounts
 * bank number.
 */
@Entity
public class Account {

    @Id
    @GeneratedValue
    Long id;

    String balance;

    String currency;

    String number;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "account")
    Auth auth;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Owner owner;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
