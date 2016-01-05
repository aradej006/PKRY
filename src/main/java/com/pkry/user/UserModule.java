package com.pkry.user;
import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.entities.Transfer;
import com.pkry.management.ManagementModule;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * User Module that comunicates with Management Module
 */
@Named
@ApplicationScoped
public class UserModule{

    /**
     * Injected object of ManagementModule class
     */
    @Inject
    ManagementModule managementModule;

    /**
     * request to check if the login is correct and exists in the base
     * @param login login inserted by client
     * @return indexes of password to insert in next step of loging process obtained from Management Module
     */
    public String Login(String login) {
        return managementModule.Login(login);
    }

    /**
     * request to check if the password exists in the base and is assigned to the login
     * @param login login inserted by client
     * @param password password inserted by client
     * @param passwordIndexes requested indexes of the password
     * @return indexes of AD to insert in the next step of logging process obtained from Management Module
     */
    public String insertPassword(String login,String password, String passwordIndexes){
        return managementModule.insertPassword(login, password, passwordIndexes);
    }

    /**
     * request to check if the AD is correct and exists in the data base
     * @param login login inserted by client
     * @param password password inserted by client
     * @param passwordIndexes requested indexes of the password
     * @param AD AD inserted by the user
     * @param ADIndexes requested indexes of the AD
     * @return information about the client's account obtained from Management Module
     */
    public String insertAD(String login,String password, String passwordIndexes, String AD, String ADIndexes){
        return managementModule.insertAD(login, password, passwordIndexes, AD, ADIndexes);
    }


    public AccountDTO getAccount(String sessionId, String login){
        return managementModule.getAccountDTO(sessionId, login);
    }

    public List<Transfer> getHistory(String login, String sessionId){
        return managementModule.getHistory(login,sessionId);
    }

    /**
     * requests a money transfer
     * @param login login of the user who wants to make a money transfer
     * @param money the amount of money to send
     * @param toAccount destination account
     * @return information weather of not the transfer has been done
     */
    public String doTransfer(String login,String sessionId, Double money, String toAccount){
        return managementModule.doTransfer(login,sessionId,money, toAccount);
    }

    /**
     * requests a logout
     * @param login loging of user who whats to logout
     * @return returns true if logout was correct
     */
    public boolean logout(String login, String sessionId){
        return managementModule.logout(login, sessionId);
    }
}
