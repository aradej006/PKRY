package com.pkry.db;


import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.entities.Account;
import com.pkry.db.model.entities.Auth;
import com.pkry.db.model.entities.AuthSession;
import com.pkry.db.model.entities.Transfer;
import com.pkry.db.model.repositories.TransferRepo;
import com.pkry.db.model.services.AuthService;
import com.pkry.db.model.services.TransferService;
import com.pkry.db.model.translators.Translator;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Named
@ApplicationScoped
public class DbModule {

    @Inject
    AuthService authService;

    @Inject
    TransferService transferService;


    @PostConstruct
    public void init() {
        authService.toString();
        authService.initAuthDAO();

        transferService.toString();
    }

    public int checkLogin(String login) {
        List<Auth> authList = authService.getAuthByLogin(login);
        if (authList.size() == 0) return 0;
        else {
            return authList.get(0).getPassword().length();
        }
    }

    public boolean checkPassword(String login, String password, String passwordIndexes) {
        String passwd = authService.getAuthByLogin(login).get(0).getPassword();
        String[] passwdIndexes = passwordIndexes.split(",");

        for (int i = 0; i < passwdIndexes.length; i++) {
            if (password.charAt(i) != passwd.charAt(Integer.parseInt(passwdIndexes[i]) - 1))
                return false;
        }
        return true;
    }

    public String checkAD(String login, String password, String passwordIndexes, String ad, String adIndexes) {
        Auth auth = authService.getAuthByLogin(login).get(0);
        String[] adIndex = adIndexes.split(",");

        if (checkPassword(login, password, passwordIndexes)) {
            for (int i = 0; i < adIndex.length; i++) {
                if (auth.getAccount().getOwner().getPesel().charAt(Integer.parseInt(adIndex[i]) - 1) != ad.charAt(i))
                    return "ERROR INCORRECT_AD";
            }

            AuthSession authSession = new AuthSession();
            Date date = new Date(System.currentTimeMillis());
            authSession.setUp(true);
            authSession.setUpdateTime(date);
            authSession.setStartTime(date);
            authSession.setMaxSessionTime(1000);
            authSession.setSessionId(RandomStringUtils.randomAlphanumeric(32));
            authSession.setAuth(auth);
            auth.addAuthSession(authSession);
            authService.update(auth);
            return authSession.getSessionId();
        } else {
            return "ERROR INCORRECT_AD";
        }

    }

    public AccountDTO getAccount(String sessionId, String login) throws Exception {
        Auth auth = authService.getAuthByAuthSessionId(login, sessionId);
        updateSession(auth);
        if (!auth.getNewestSession().isUp())
            throw new Exception("ERROR SESSION EXPIRED");

        return Translator.toDTO(auth.getAccount());

    }

    public String checkMoney(String login, String sessionId, double money) {
        Auth auth = authService.getAuthByLogin(login).get(0);
        if (!auth.getNewestSession().getSessionId().equals(sessionId)) {
            return "ERROR BAD SESSION";
        }
        if (updateSession(auth).equals("ACTIVE")) {
            if (Double.parseDouble(auth.getAccount().getBalance()) >= money) {
                return "MONEY OK";
            } else {
                return "ERROR LACK OF MONEY";
            }
        } else {
            return "ERROR SESSION EXPIRED";
        }
    }

    public String doTransfer(String login, double money, String accountNumber) {
        if (authService.getAuthByAccount_Number(accountNumber) != null) {
            Auth authFrom = authService.getAuthByLogin(login).get(0);
            Auth authTo = authService.getAuthByAccount_Number(accountNumber);
            if (updateSession(authFrom).equals("ACTIVE")) {
                authFrom.getAccount().setBalance(String.format("%.2f", Double.parseDouble(authFrom.getAccount().getBalance()) - money));
                authTo.getAccount().setBalance(String.format("%.2f", Double.parseDouble(authTo.getAccount().getBalance()) + money));
                // save history
                authService.update(authFrom);
                authService.update(authTo);
                Transfer transfer = new Transfer();
                transfer.setFromAccount(authFrom.getAccount().getNumber());
                transfer.setToAccount(authTo.getAccount().getNumber());
                transfer.setCurrency(authFrom.getAccount().getCurrency());
                transfer.setAmount(money);
                transfer.setTransferDate(new Date());
                transferService.save(transfer);
                return "TRANSFER DONE";
            } else {
                return "ERROR SESSION EXPIRED";
            }
        } else {
            return "ERROR DESTINATION NUMBER IS NOT CORRECT";
        }
    }

    private String updateSession(Auth auth) {
        boolean isup = auth.getNewestSession().isUp();
        if (auth.getNewestSession().isUp()) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.sss");
            Date d1 = new Date(auth.getNewestSession().getUpdateTime().getTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(d1);
            cal.add(Calendar.SECOND, auth.getNewestSession().getMaxSessionTime());
            auth.getNewestSession().setUpdateTime(new Date(System.currentTimeMillis()));

            if (cal.getTime().after(new Date(System.currentTimeMillis()))) {
                authService.update(auth);
                return "ACTIVE";
            }
            auth.getNewestSession().setUp(false);
            authService.update(auth);
            return "ERROR EXPIRED";
        }
        return "ERROR EXPIRED";
    }


    public boolean logout(String login, String sessionId) {
        if (authService.getAuthByAuthSessionId(login, sessionId) != null) {
            Auth auth = authService.getAuthByLogin(login).get(0);
            if (auth.getNewestSession().isUp()) {
                auth.getNewestSession().setUp(false);
                authService.update(auth);
                return true;
            } else
                return false;
        }
        return false;
    }

    public List<Transfer> getHistory(String login, String sessionId) throws Exception {
        if (authService.getAuthByAuthSessionId(login, sessionId) != null) {
            Auth auth = authService.getAuthByLogin(login).get(0);
            if (!auth.getNewestSession().isUp()) {
                throw new Exception("ERROR SESSION EXPIRED");
            }

            List<Transfer> from = transferService.findByFromAccount(auth.getAccount().getNumber());
            List<Transfer> to = transferService.findByToAccount(auth.getAccount().getNumber());
            List<Transfer> all = new LinkedList<Transfer>();
            if (from != null && !from.isEmpty()) {
                for (Transfer transfer : from) {
                    all.add(transfer);
                }
            }
            if (to != null && !to.isEmpty()) {
                for (Transfer transfer : to) {
                    all.add(transfer);
                }
            }
            return all;
        }
        return null;
    }
}
