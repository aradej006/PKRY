package com.pkry.db.model.translators;

import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.DTOs.AddressDTO;
import com.pkry.db.model.DTOs.AuthDTO;
import com.pkry.db.model.DTOs.OwnerDTO;
import com.pkry.db.model.entities.Account;
import com.pkry.db.model.entities.Address;
import com.pkry.db.model.entities.Auth;
import com.pkry.db.model.entities.Owner;

/**
 * Created by arade on 03-Jan-16.
 */
public final class Translator {

    public Auth toEntity(AuthDTO authDTO){

        return null;

    }

    public Account toEntity(AccountDTO accountDTO){
        Account account = new Account();
        account.setNumber(accountDTO.getNumber());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(accountDTO.getCurrency());
        account.setOwner(toEntity(accountDTO.getOwnerDTO()));
        return account;
    }

    public Owner toEntity(OwnerDTO ownerDTO){
        Owner owner = new Owner();
        owner.setPesel(ownerDTO.getPesel());
        owner.setBirthDate(ownerDTO.getBirthDate());
        owner.setLastname(ownerDTO.getLastname());
        owner.setFirstname(ownerDTO.getFirstname());
        owner.setAddress(toEntity(ownerDTO.getAddressDTO()));
        return owner;
    }

    public Address toEntity(AddressDTO addressDTO){
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setPostCode(addressDTO.getPostCode());
        address.setFlatNumber(addressDTO.getFlatNumber());
        address.setCity(addressDTO.getCity());
        address.setBuildingNumber(addressDTO.getBuildingNumber());
        return address;
    }



}
