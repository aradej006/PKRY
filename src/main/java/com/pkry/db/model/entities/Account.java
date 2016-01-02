package com.pkry.db.model.entities;

import javax.persistence.*;

/**
 * Created by arade on 29-Dec-15.
 */
@Entity
public class Account {

    @Id
    @GeneratedValue
    Long id;

    Double balance;

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
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
