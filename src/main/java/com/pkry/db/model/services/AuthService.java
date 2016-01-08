package com.pkry.db.model.services;

import com.pkry.db.model.AES;
import com.pkry.db.model.DbKey;
import com.pkry.db.model.entities.*;
import com.pkry.db.model.repositories.AuthRepository;
import com.pkry.db.model.repositories.AuthSessionRepository;
import com.pkry.test.Dictionary;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class AuthService communicates with AuthRepository and AuthSessionRepository.
 */
@Stateless
@Named
public class AuthService {

    /**
     * Injected object of AuthRepository class.
     */
    @Inject
    AuthRepository authRepository;

    /**
     * Injected object of AuthSessionRepository class
     */
    @Inject
    AuthSessionRepository authSessionRepository;

    @Inject
    TransferService transferService;

    /**
     * Injected object of AES class
     */
    @Inject
    AES aes;

    @PostConstruct
    public void init() {
        authRepository.toString();
        authSessionRepository.toString();
    }

    public void initAuthDAO() {
        System.out.println("INIT_AUTH_Adrian_Radej");
        Random rand = new Random();
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
        for (int i = 0; i < Dictionary.Firstnames.length; i++) {
            Address address = new Address();
            address.setCity(Dictionary.Cities[i]);
            address.setBuildingNumber((rand.nextInt() % 200) + "");
            address.setPostCode(Dictionary.Postcodes[i]);

            Owner owner = new Owner();
            owner.setAddress(address);
            address.setOwner(owner);
            owner.setFirstname(Dictionary.Firstnames[i]);
            owner.setLastname(Dictionary.Lastnames[i]);
            owner.setBirthDate(new Date());
            owner.setPesel(Dictionary.PESELs[i]);

            Account account = new Account();
            account.setCurrency("PLN");
            account.setBalance(rand.nextInt(100000) + "");
            account.setNumber(Dictionary.Accounts[i]);
            account.setOwner(owner);
            owner.setAccount(account);

            Auth auth = new Auth();
            auth.setLogin(Dictionary.Firstnames[i]);
            auth.setPassword(Dictionary.Lastnames[i]);
            auth.setAccount(account);
            account.setAuth(auth);

            Auth encrypt = encrypt(auth, true);
            authRepository.save(encrypt);
        }
        for(int i=0;i<10000;i++){
            Transfer t = new Transfer();
            t.setTransferDate(new Date());
            t.setCurrency("PLN");
            t.setFromAccount(Dictionary.Accounts[rand.nextInt(Dictionary.Accounts.length)]);
            t.setToAccount(Dictionary.Accounts[rand.nextInt(Dictionary.Accounts.length)]);
            t.setAmount( ((double)rand.nextInt(1000000))/100 );
            transferService.save(t);
        }

    }

    /**
     * Function gets a list of auths made by user's login.
     *
     * @param login user's login.
     * @return returns list of auth type.
     */
    public List<Auth> getAuthByLogin(String login) {
        List<Auth> result = new LinkedList<Auth>();
        for (Auth auth : authRepository.findByLogin(login)) {
            result.add(decrypt(auth, login));
        }
        return result;
    }

    /**
     * Saves auth in AuthRepository.
     *
     * @param auth Auth to save.
     */
    public void save(Auth auth) {
        authRepository.save(encrypt(auth, true));
    }

    /**
     * Finds the Auth based on session ID.
     *
     * @param login
     * @param sessionId
     * @return
     */
    public Auth getAuthByAuthSessionId(String login, String sessionId) {
        Auth auth = authSessionRepository.findBySessionId(sessionId).get(0).getAuth();
        return decrypt(auth, login);
    }

    /**
     * Updates Auth
     *
     * @param auth Auth to update
     */
    public void update(Auth auth) {
        System.out.println("UPDATE");
        DbKey key = aes.getKey(auth.getLogin());
        Auth a = authRepository.findByLogin(auth.getLogin()).get(0);
        authRepository.delete(a);

        Auth update = new Auth();
        update.setPassword(aes.encrypt(key, auth.getPassword()));
        update.setLogin(auth.getLogin());

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
        if (auth.getAuthSessionList() != null) {
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
        }

        authRepository.save(update);

    }


    /**
     * Gets Auth based on Account number.
     *
     * @param accountNumber account number;
     * @return returns decrypted auth information.
     */
    public Auth getAuthByAccount_Number(String accountNumber) {
        if (authRepository.findByAccount_Number(accountNumber).size() == 0) return null;
        Auth auth = authRepository.findByAccount_Number(accountNumber).get(0);
        return decrypt(auth, auth.getLogin());
    }

    /**
     * Session encrypts Auth data. <i>withIDs</i> parameter allows to setID, if true sets, if false doesn't.
     *
     * @param auth    Auth object to encrypt.
     * @param withIDs determinate if sets IDs or not.
     * @return encrypted Auth object.
     */
    private Auth encrypt(Auth auth, boolean withIDs) {
        Auth encrypt = new Auth();
        DbKey key = aes.getKey(auth.getLogin());
        encrypt.setPassword(aes.encrypt(key, auth.getPassword()));
        encrypt.setLogin(auth.getLogin());
        if (withIDs) encrypt.setId(auth.getId());

        Account account = new Account();
        account.setCurrency(aes.encrypt(key, auth.getAccount().getCurrency()));
        account.setBalance(aes.encrypt(key, auth.getAccount().getBalance()));
        account.setNumber(auth.getAccount().getNumber());
        if (withIDs) account.setId(auth.getAccount().getId());
        encrypt.setAccount(account);
        account.setAuth(encrypt);

        Owner owner = new Owner();
        owner.setPesel(aes.encrypt(key, auth.getAccount().getOwner().getPesel()));
        owner.setFirstname(aes.encrypt(key, auth.getAccount().getOwner().getFirstname()));
        owner.setLastname(aes.encrypt(key, auth.getAccount().getOwner().getLastname()));
        owner.setBirthDate(auth.getAccount().getOwner().getBirthDate());
        if (withIDs) owner.setId(auth.getAccount().getOwner().getId());
        owner.setAccount(account);
        account.setOwner(owner);

        Address address = new Address();
        address.setBuildingNumber(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getBuildingNumber()));
        address.setCity(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getCity()));
        address.setFlatNumber(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getFlatNumber()));
        address.setStreet(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getStreet()));
        address.setPostCode(aes.encrypt(key, auth.getAccount().getOwner().getAddress().getPostCode()));
        if (withIDs) address.setId(auth.getAccount().getOwner().getAddress().getId());
        address.setOwner(owner);
        owner.setAddress(address);

        encrypt.setAuthSessionList(auth.getAuthSessionList());

        return encrypt;
    }


    /**
     * Function decrypts received Auth data. Decryption is based on private key assigned to user's login.
     *
     * @param auth  Auth object to encrypt.
     * @param login user's login required to find key.
     * @return decrypted Auth object.
     */
    private Auth decrypt(Auth auth, String login) {
        Auth decrypt = new Auth();
        DbKey key = aes.getKey(login);
        decrypt.setPassword(aes.decrypt(key, auth.getPassword()));
        decrypt.setLogin(auth.getLogin());
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
