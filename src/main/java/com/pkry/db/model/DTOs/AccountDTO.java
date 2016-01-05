package com.pkry.db.model.DTOs;

/**
 * Created by arade on 03-Jan-16.
 */
public class AccountDTO {

    String balance;

    String currency;

    String number;

    AuthDTO authDTO;

    OwnerDTO ownerDTO;

    public OwnerDTO getOwnerDTO() {
        return ownerDTO;
    }

    public void setOwnerDTO(OwnerDTO ownerDTO) {
        this.ownerDTO = ownerDTO;
    }

    public AuthDTO getAuthDTO() {
        return authDTO;
    }

    public void setAuthDTO(AuthDTO authDTO) {
        this.authDTO = authDTO;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
