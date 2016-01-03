package com.pkry.db.model.translators;

import com.pkry.db.model.DTOs.*;
import com.pkry.db.model.entities.*;

/**
 * Created by arade on 03-Jan-16.
 */
public final class Translator {

    public static Auth toEntity(AuthDTO authDTO){
        Auth auth = new Auth();
        auth.setPassword(authDTO.getPassword());
        auth.setLogin(authDTO.getLogin());
        auth.setAccount(toEntity(authDTO.getAccountDTO()));
        auth.getAccount().setAuth(auth);
        for (AuthSessionDTO authSessionDTO : authDTO.getAuthSessionDTOList()) {
            AuthSession authSession = toEntity(authSessionDTO);
            auth.addAuthSession(authSession);
            authSession.setAuth(auth);
        }
        return null;

    }

    public static AuthSession toEntity(AuthSessionDTO authSessionDTO){
        AuthSession authSession = new AuthSession();
        authSession.setMaxSessionTime(authSessionDTO.getMaxSessionTime());
        authSession.setSessionId(authSessionDTO.getSessionId());
        authSession.setStartTime(authSessionDTO.getStartTime());
        authSession.setUpdateTime(authSessionDTO.getUpdateTime());
        return authSession;
    }

    public static Account toEntity(AccountDTO accountDTO){
        Account account = new Account();
        account.setNumber(accountDTO.getNumber());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(accountDTO.getCurrency());
        account.setOwner(toEntity(accountDTO.getOwnerDTO()));
        account.getOwner().setAccount(account);
        return account;
    }

    public static Owner toEntity(OwnerDTO ownerDTO){
        Owner owner = new Owner();
        owner.setPesel(ownerDTO.getPesel());
        owner.setBirthDate(ownerDTO.getBirthDate());
        owner.setLastname(ownerDTO.getLastname());
        owner.setFirstname(ownerDTO.getFirstname());
        owner.setAddress(toEntity(ownerDTO.getAddressDTO()));
        owner.getAddress().setOwner(owner);
        return owner;
    }

    public static Address toEntity(AddressDTO addressDTO){
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setPostCode(addressDTO.getPostCode());
        address.setFlatNumber(addressDTO.getFlatNumber());
        address.setCity(addressDTO.getCity());
        address.setBuildingNumber(addressDTO.getBuildingNumber());
        return address;
    }



}
