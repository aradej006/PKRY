package com.pkry.db.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by arade on 03-Jan-16.
 */
@Entity
public class Transfer {

    @Id
    @GeneratedValue
    Long id;

    String fromAccount;

    String toAccount;

    Date transferDate;

    Double amount;

    String currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
