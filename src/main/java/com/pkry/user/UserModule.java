package com.pkry.user;

import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.entities.Transfer;
import com.pkry.management.ManagementModule;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * User Module that comunicates with Management Module. This module receives messages from the Client.
 */
@Named
@ApplicationScoped
public class UserModule {

    /**
     * Injected object of ManagementModule class
     */
    @Inject
    ManagementModule managementModule;

    /**
     * Request to check if the login is correct and exists in the data base.
     *
     * @param login login inserted by client.
     * @return Indexes of the password to insert in next step of logging process obtained from Management Module
     */
    public String Login(String login) {
        return managementModule.Login(login);
    }

    /**
     * Request to check if the password exists in the base (so is correct).
     *
     * @param login           login inserted by client.
     * @param password        password inserted by client.
     * @param passwordIndexes requested indexes of the password.
     * @return indexes of additional information (AD) to insert in the next step of logging process obtained from Management Module.
     */
    public String insertPassword(String login, String password, String passwordIndexes) {
        return managementModule.insertPassword(login, password, passwordIndexes);
    }

    /**
     * Request to check if the additional information inserted by user is correct.
     *
     * @param login           login inserted by client.
     * @param password        password inserted by client.
     * @param passwordIndexes requested indexes of the password (the same as in the previous step of logging process).
     * @param AD              AD inserted by the user.
     * @param ADIndexes       requested indexes of the AD.
     * @return if AD is correct returns sessionId.
     */
    public String insertAD(String login, String password, String passwordIndexes, String AD, String ADIndexes) {
        return managementModule.insertAD(login, password, passwordIndexes, AD, ADIndexes);
    }

    /**
     * Request to obtain informations about user's account.
     *
     * @param sessionId ID of the current session.
     * @param login     user's login.
     * @return user's account assigned to that login.
     * @throws Exception if the session expired throws SESSION EXPIRED exception.
     */
    public AccountDTO getAccount(String sessionId, String login) throws Exception {
        return managementModule.getAccountDTO(sessionId, login);
    }

    /**
     * Request to obtain the history of previous transactions.
     *
     * @param login     user's login.
     * @param sessionId ID of the current session.
     * @return list of previous transactions made by client.
     * @throws Exception if the session expired throws SESSION EXPIRED exception.
     */
    public List<Transfer> getHistory(String login, String sessionId) throws Exception {
        return managementModule.getHistory(login, sessionId);
    }

    /**
     * User's request to make a money transfer to someone else's account..
     *
     * @param login     user's login.
     * @param sessionId ID of the current session.
     * @param money     amount of money to send.
     * @param toAccount number of destination account
     * @return information whether or not the transfer has been made (if not, also sends the cause).
     */
    public String doTransfer(String login, String sessionId, Double money, String toAccount) {
        return managementModule.doTransfer(login, sessionId, money, toAccount);
    }

    /**
     * Client's information that they want to logout from their bank account.
     *
     * @param login     user's login.
     * @param sessionId ID of the current session.
     * @return returns <i>true</i> if the logout was correct.
     */
    public boolean logout(String login, String sessionId) {
        return managementModule.logout(login, sessionId);
    }
}
