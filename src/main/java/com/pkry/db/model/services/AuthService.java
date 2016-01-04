package com.pkry.db.model.services;

import com.pkry.db.model.AES;
import com.pkry.db.model.DbKey;
import com.pkry.db.model.repositories.AuthRepository;
import com.pkry.db.model.entities.Account;
import com.pkry.db.model.entities.Address;
import com.pkry.db.model.entities.Auth;
import com.pkry.db.model.entities.Owner;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    AES aes;

    @PostConstruct
    public void init() {
        authRepository.toString();
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
            owner.setPesel("32095545624");

            Account account = new Account();
            account.setCurrency("PLN");
            account.setBalance(4343.23);
            account.setNumber("12345678912345678912345678");
            account.setOwner(owner);
            owner.setAccount(account);

            Auth auth = new Auth();
            auth.setLogin("Adrian");
            auth.setPassword("Radej");
            auth.setAccount(account);
            account.setAuth(auth);



            authRepository.save(auth);

            encrypt(auth);

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
            owner.setPesel("78788513465");

            Account account = new Account();
            account.setCurrency("PLN");
            account.setBalance(8947569.5);
            account.setNumber("00000000000000000000000000");
            account.setOwner(owner);
            owner.setAccount(account);

            Auth auth = new Auth();
            auth.setLogin("Daniel");
            auth.setPassword("Radej");
            auth.setAccount(account);
            account.setAuth(auth);

            authRepository.save(auth);
        }

    }

    public List<Auth> getAuthByLogin(String login) {
        return authRepository.findByLogin(login);
    }

    public void save(Auth auth){
        authRepository.save(auth);
    }

    public List<Auth> getAuthByAccount_Number(String accountNumber){
        return authRepository.findByAccount_Number(accountNumber);
    }

    private Auth encrypt(Auth auth){
        Auth encrypt = new Auth();
        DbKey key = aes.getKey(auth.getLogin());
        encrypt.setPassword(AES.encrypt(key, auth.getPassword()));
        encrypt.setLogin(AES.encrypt(key, auth.getLogin()));
        return encrypt;
    }

}
