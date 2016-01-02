package com.pkry.db.model.services;

import com.pkry.db.model.repositories.AuthRepository;
import com.pkry.db.model.entities.Account;
import com.pkry.db.model.entities.Address;
import com.pkry.db.model.entities.Auth;
import com.pkry.db.model.entities.Owner;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by arade on 29-Dec-15.
 */
@Stateless
@Named
public class AuthService {

    @Inject
    AuthRepository authRepository;

    @PostConstruct
    public void init() {
        authRepository.toString();
    }

    public void initAuthDAO(){
        System.out.println("INIT_AUTH_DAO");
        if(authRepository.findAll().size()==0){
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
        }
    }

    public void test() {

    }

}
