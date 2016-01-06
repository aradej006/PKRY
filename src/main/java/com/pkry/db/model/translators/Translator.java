package com.pkry.db.model.translators;

import com.pkry.db.model.DTOs.*;
import com.pkry.db.model.entities.*;

/**
 * A Translator class translates DTOs to entity or other way around.
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
        authSession.setUp(authSessionDTO.isUp());
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

    public static AuthDTO toDTO(Auth auth){
        AuthDTO authDTO = new AuthDTO();
        authDTO.setPassword(auth.getPassword());
        authDTO.setLogin(auth.getLogin());
        authDTO.setAccountDTO(toDTO(auth.getAccount()));
        authDTO.getAccountDTO().setAuthDTO(authDTO);
        for (AuthSession authSession : auth.getAuthSessionList()) {
            AuthSessionDTO authSessionDTO = toDTO(authSession);
            authDTO.addAuthSession(authSessionDTO);
            authSessionDTO.setAuthDTO(authDTO);
        }
        return null;

    }

    public static AuthSessionDTO toDTO(AuthSession authSession){
        AuthSessionDTO authSessionDTO = new AuthSessionDTO();
        authSessionDTO.setMaxSessionTime(authSession.getMaxSessionTime());
        authSessionDTO.setSessionId(authSession.getSessionId());
        authSessionDTO.setStartTime(authSession.getStartTime());
        authSessionDTO.setUpdateTime(authSession.getUpdateTime());
        authSessionDTO.setUp(authSession.isUp());
        return authSessionDTO;
    }

    public static AccountDTO toDTO(Account account){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumber(account.getNumber());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setCurrency(account.getCurrency());
        accountDTO.setOwnerDTO(toDTO(account.getOwner()));
        accountDTO.getOwnerDTO().setAccountDTO(accountDTO);
        return accountDTO;
    }

    public static OwnerDTO toDTO(Owner owner){
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setPesel(owner.getPesel());
        ownerDTO.setBirthDate(owner.getBirthDate());
        ownerDTO.setLastname(owner.getLastname());
        ownerDTO.setFirstname(owner.getFirstname());
        ownerDTO.setAddressDTO(toDTO(owner.getAddress()));
        ownerDTO.getAddressDTO().setOwnerDTO(ownerDTO);
        return ownerDTO;
    }

    public static AddressDTO toDTO(Address address){
        AddressDTO addressDTO = new AddressDTO();
        address.setStreet(address.getStreet());
        address.setPostCode(address.getPostCode());
        address.setFlatNumber(address.getFlatNumber());
        address.setCity(address.getCity());
        address.setBuildingNumber(address.getBuildingNumber());
        return addressDTO;
    }

}
