package com.pkry.management;

import com.pkry.db.DbModule;
import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.entities.Transfer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Management module class that communicates with DbModule and send informations back toUserModule.
 */
@Named
@ApplicationScoped
public class ManagementModule {

    /**
     * Injected object of DbModule class
     */
    @Inject
    DbModule dbModule;

    /**
     * Executes the correctness of inserted login.
     *
     * @param login login inserted by client
     * @return if login exists in the base returns indexes of password to insert in next step of logging process
     */
    public String Login(String login) {
        if (dbModule.checkLogin(login) > 0)
            return generateIndexes(dbModule.checkLogin(login));
        return "ERROR BAD LOGIN";
    }

    /**
     * Executes the correctness of password.
     *
     * @param login           login inserted by client
     * @param password        password inserted by client
     * @param passwordIndexes requested indexes of the password
     * @return if the password matches the one assigned to the login returns indexes of AD to insert in the next step of logging process
     */
    public String insertPassword(String login, String password, String passwordIndexes) {
        if (dbModule.checkPassword(login, password, passwordIndexes))
            return generateIndexes(11);
        return "ERROR BAD PASSWORD";

    }

    /**
     * Executes the correctness of AD (Additional Informations)
     *
     * @param login           login inserted by client.
     * @param password        password inserted by client.
     * @param passwordIndexes requested indexes of the password.
     * @param AD              AD inserted by the user.
     * @param ADIndexes       requested indexes of the AD.
     * @return if the AD matches the one in the base correct returns sessionId.
     */
    public String insertAD(String login, String password, String passwordIndexes, String AD, String ADIndexes) {
        return dbModule.checkAD(login, password, passwordIndexes, AD, ADIndexes);
    }

    /**
     * Executes the request to obtain information about user's account.
     *
     * @param sessionId ID of the current session.
     * @param login     user's login.
     * @return user's account assigned to that login.
     * @throws Exception if the session expired throws SESSION EXPIRED exception.
     */
    public AccountDTO getAccountDTO(String sessionId, String login) throws Exception {
        return dbModule.getAccount(sessionId, login);
    }

    /**
     * executes the client's request to make a money transfer. Checks if the money transfer is possible.
     *
     * @param login     user's login.
     * @param sessionId ID of the current session.
     * @param money     amount of money to send.
     * @param toAccount number of destination account
     * @return information whether or not the transfer has been made (if not, also sends the cause).
     */
    public String doTransfer(String login, String sessionId, double money, String toAccount) {
        String message = dbModule.checkMoney(login, sessionId, money);
        if (message.equals("MONEY OK"))
            return dbModule.doTransfer(login, money, toAccount);
        return message;
    }

    /**
     * Function randomly generates indexes of password or AD that are requested to input by Client.
     *
     * @param passwordLength lenght of the password or AD
     * @return string containing the indexes
     */
    private String generateIndexes(int passwordLength) {
        Random rand = new Random();
        List<Integer> list = new ArrayList<Integer>();
        int indexCount = 0;
        int index;
        int max = passwordLength - 3 < 4 ? 4 : passwordLength - 3;
        while (indexCount <= 2 || indexCount >= max) {
            indexCount = rand.nextInt(passwordLength) + 1;
        }
        while (list.size() < indexCount) {
            index = rand.nextInt(passwordLength) + 1;
            if (!list.contains(index)) {
                list.add(index);
            }
        }
        Collections.sort(list);
        String indexes = list.toString().replace("[", "").replace("]", "").replace(" ", "");
        return indexes;
    }


    /**
     * Wxecutes the corectness of the request to logout from the server
     *
     * @param login user's login.
     * @return if the logout was correct returns <i>true</i>
     */
    public boolean logout(String login, String sessionId) {
        return dbModule.logout(login, sessionId);
    }

    public List<Transfer> getHistory(String login, String sessionId) throws Exception {
        return dbModule.getHistory(login, sessionId);
    }
}

