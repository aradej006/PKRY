package com.pkry.db.model.services;

import com.pkry.db.model.AES;
import com.pkry.db.model.DbKey;
import com.pkry.db.model.entities.*;
import com.pkry.db.model.repositories.AuthRepository;
import com.pkry.db.model.repositories.AuthSessionRepository;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by arade on 29-Dec-15.
 */
@Stateless
@Named
public class AuthService {

    @Inject
    AuthRepository authRepository;

    @Inject
    AuthSessionRepository authSessionRepository;

    @Inject
    AES aes;

    @PostConstruct
    public void init() {
        authRepository.toString();
        authSessionRepository.toString();
    }

    public void initAuthDAO() {
        System.out.println("INIT_AUTH_Adrian_Radej");
        if (authRepository.findAll().size() == 0) {
            Address address = new Address();
            address.setCity("Warszawa");
            address.setBuildingNumber("5");
            address.setFlatNumber("61");
            address.setPostCode("02-649");
            address.setStreet("Marzanny");

            Owner owner = new Owner();
            owner.setAddress(address);
            address.setOwner(owner);
            owner.setFirstname("Adrian");
            owner.setLastname("Radej");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                owner.setBirthDate(sdf.parse("30/08/1993"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            owner.setPesel("00000000000");

            Account account = new Account();
            account.setCurrency("PLN");
            account.setBalance("4343.23");
            account.setNumber("12345678912345678912345678");
            account.setOwner(owner);
            owner.setAccount(account);

            Auth auth = new Auth();
            auth.setLogin("Adrian");
            auth.setPassword("Radej");
            auth.setAccount(account);
            account.setAuth(auth);

            Auth encrypt = encrypt(auth, true);
            authRepository.save(encrypt);

        }
        if (authRepository.findAll().size() == 1) {
            Address address = new Address();
            address.setCity("Tarnawa Mała");
            address.setBuildingNumber("55");
            address.setPostCode("23-465");

            Owner owner = new Owner();
            owner.setAddress(address);
            address.setOwner(owner);
            owner.setFirstname("Daniel");
            owner.setLastname("Radej");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                owner.setBirthDate(sdf.parse("06/08/1992"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            owner.setPesel("11111111111");

            Account account = new Account();
            account.setCurrency("PLN");
            account.setBalance("8947569.5");
            account.setNumber("00000000000000000000000000");
            account.setOwner(owner);
            owner.setAccount(account);

            Auth auth = new Auth();
            auth.setLogin("Daniel");
            auth.setPassword("Radej");
            auth.setAccount(account);
            account.setAuth(auth);

            Auth encrypt = encrypt(auth, true);
            authRepository.save(encrypt);
        }

    }

    public List<Auth> getAuthByLogin(String login) {
        List<Auth> result = new LinkedList<Auth>();
        DbKey key = aes.getKey(login);
        String encLogin = aes.encrypt(key, login);
        for (Auth auth : authRepository.findByLogin(encLogin)) {
            result.add(decrypt(auth, login));
        }
        return result;
    }

    public void save(Auth auth){
        authRepository.save(encrypt(auth, true));
    }

    public Auth getAuthByAuthSessionId(String login, String sessionId){
        Auth auth = authSessionRepository.findBySessionId(sessionId).get(0).getAuth();
        return decrypt(auth, login);
    }


    public void update(Auth auth){
        System.out.println("UPDATE");
        DbKey key = aes.getKey(auth.getLogin());
        String encLogin = aes.encrypt(key, auth.getLogin());
        Auth a = authRepository.findByLogin(encLogin).get(0);
        authRepository.delete(a);

        Auth update = new Auth();
        update.setPassword(aes.encrypt(key, auth.getPassword()));
        update.setLogin(aes.encrypt(key, auth.getLogin()));

        Account account = new Account();
        account.setCurrency(aes.encrypt(key, auth.getAccount().getCurrency()));
        account.setBalance(aes.encrypt(key, auth.getAccount().getBalance()));
        account.setNumber(auth.getAccount().getNumber());
        update.setAccount(account);
        account.setAuth(update);

        Owner owner = new Owner();
        owner.setPesel(aes.encrypt(key, auth.getAccount().getOwner().getPesel()));
        owner.setFirstname(aes.encrypt(key, auth.getAccount().getOwner().getFirstname()));
        owner.setLastname(aes.encrypt(key, auth.getAccount().getOwner().getLastname()));
        owner.setBirthDate(auth.getAccount().getOwner().getBirthDate());
        owner.setAccount(account);
        account.setOwner(owner);

        Address address = new Address();
        address.setBuildingNumber(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getBuildingNumber()));
        address.setCity(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getCity()));
        address.setFlatNumber(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getFlatNumber()));
        address.setStreet(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getStreet()));
        address.setPostCode(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getPostCode()));
        address.setOwner(owner);
        owner.setAddress(address);

        for (AuthSession authSession : auth.getAuthSessionList()) {
            AuthSession authSession1 = new AuthSession();
            authSession1.setUp(authSession.isUp());
            authSession1.setSessionId(authSession.getSessionId());
            authSession1.setMaxSessionTime(authSession.getMaxSessionTime());
            authSession1.setStartTime(authSession.getStartTime());
            authSession1.setUpdateTime(authSession.getUpdateTime());
            authSession1.setAuth(update);
            update.addAuthSession(authSession1);
        }

        authRepository.save(update);

    }

    public List<Auth> getAuthByAccount_Number(String accountNumber){
        return authRepository.findByAccount_Number(accountNumber);
    }

    private Auth encrypt(Auth auth,boolean withIDs){
        Auth encrypt = new Auth();
        DbKey key = aes.getKey(auth.getLogin());
        encrypt.setPassword(aes.encrypt(key, auth.getPassword()));
        encrypt.setLogin(aes.encrypt(key, auth.getLogin()));
        if(withIDs) encrypt.setId(auth.getId());

        Account account = new Account();
        account.setCurrency(aes.encrypt(key, auth.getAccount().getCurrency()));
        account.setBalance(aes.encrypt(key, auth.getAccount().getBalance()));
        account.setNumber(auth.getAccount().getNumber());
        if(withIDs) account.setId(auth.getAccount().getId());
        encrypt.setAccount(account);
        account.setAuth(encrypt);

        Owner owner = new Owner();
        owner.setPesel(aes.encrypt(key, auth.getAccount().getOwner().getPesel()));
        owner.setFirstname(aes.encrypt(key, auth.getAccount().getOwner().getFirstname()));
        owner.setLastname(aes.encrypt(key, auth.getAccount().getOwner().getLastname()));
        owner.setBirthDate(auth.getAccount().getOwner().getBirthDate());
        if(withIDs) owner.setId(auth.getAccount().getOwner().getId());
        owner.setAccount(account);
        account.setOwner(owner);

        Address address = new Address();
        address.setBuildingNumber(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getBuildingNumber()));
        address.setCity(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getCity()));
        address.setFlatNumber(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getFlatNumber()));
        address.setStreet(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getStreet()));
        address.setPostCode(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getPostCode()));
        if(withIDs) address.setId(auth.getAccount().getOwner().getAddress().getId());
        address.setOwner(owner);
        owner.setAddress(address);

        encrypt.setAuthSessionList(auth.getAuthSessionList());

        return encrypt;
    }

    private Auth decrypt(Auth auth, String login){
        Auth decrypt = new Auth();
        DbKey key = aes.getKey(login);
        decrypt.setPassword(aes.decrypt(key, auth.getPassword()));
        decrypt.setLogin(aes.decrypt(key, auth.getLogin()));
        decrypt.setId(auth.getId());

        Account account = new Account();
        account.setCurrency(aes.decrypt(key, auth.getAccount().getCurrency()));
        account.setBalance(aes.decrypt(key, auth.getAccount().getBalance()));
        account.setNumber(auth.getAccount().getNumber());
        account.setId(auth.getAccount().getId());
        decrypt.setAccount(account);
        account.setAuth(decrypt);

        Owner owner = new Owner();
        owner.setPesel(aes.decrypt(key, auth.getAccount().getOwner().getPesel()));
        owner.setFirstname(aes.decrypt(key, auth.getAccount().getOwner().getFirstname()));
        owner.setLastname(aes.decrypt(key, auth.getAccount().getOwner().getLastname()));
        owner.setBirthDate(auth.getAccount().getOwner().getBirthDate());
        owner.setId(auth.getAccount().getOwner().getId());
        owner.setAccount(account);
        account.setOwner(owner);

        Address address = new Address();
        address.setBuildingNumber(aes.decrypt(key, auth.getAccount().getOwner().getAddress().getBuildingNumber()));
        address.setCity(aes.decrypt(key, auth.getAccount().getOwner().getAddress().getCity()));
        address.setFlatNumber(aes.decrypt(key, auth.getAccount().getOwner().getAddress().getFlatNumber()));
        address.setStreet(aes.decrypt(key, auth.getAccount().getOwner().getAddress().getStreet()));
        address.setPostCode(aes.decrypt(key, auth.getAccount().getOwner().getAddress().getPostCode()));
        address.setId(auth.getAccount().getOwner().getAddress().getId());
        address.setOwner(owner);
        owner.setAddress(address);

        decrypt.setAuthSessionList(auth.getAuthSessionList());

        return decrypt;
    }

}
