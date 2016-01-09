package com.pkry.user;

import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.db.model.DTOs.OwnerDTO;
import com.pkry.db.model.entities.Transfer;
import com.pkry.user.server.Handle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * HandleClient class implements Handle interface and is responsible for handling messages from the client and sends
 * feedback to that clients with some information obtained from data base.
 */
@Named
@ApplicationScoped
public class HandleClient implements Handle {

    /**
     * Injected object of UserModule class.
     */
    @Inject
    UserModule userModule;

    @PostConstruct
    public void init() {
    }

    /**
     * Function from Handle interface that handles requests from client. Depending on the request sends some information
     * to UserModule to get feedback.
     *
     * @param data message received from client's application.
     * @return returns feedback information to the client with the requested information or information that an error
     * occurred.
     */
    public String handle(String data) {
        String[] msg = data.split(" ");
//        System.out.println(data);
        String message = "ERROR";

        if (data.contains("Login")) {
            message = "PasswordIndexes " + userModule.Login(msg[1]);
        } else if (data.contains("Password")) {
            message = "PESELIndexes " + userModule.insertPassword(msg[1], msg[2], msg[3]);
        } else if (data.contains("PESELNumbers")) {
            message = "LoggedIn " + userModule.insertAD(msg[1], msg[2], msg[3], msg[4], msg[5]);
        } else if (data.contains("DoTransfer")) {
            message = userModule.doTransfer(msg[1], msg[2], Double.parseDouble(msg[3]), msg[4]);
        } else if (data.contains("getaccount")) {
            try {
                message = accountToString(userModule.getAccount(msg[2], msg[1]));
            } catch (Exception e) {
                message = e.getMessage();
            }
        } else if (data.contains("gethistory")) {
            try {
                message = historyToString(userModule.getHistory(msg[1], msg[2]));
            } catch (Exception e) {
                message = e.getMessage();
            }
        } else if (data.contains("Logout")) {
            if (userModule.logout(msg[1], msg[2])) {
                message = "LOGOUT";
            } else {
                message = "ERROR";
            }
        }

//        System.out.println(message);
        return message;
    }

    /**
     * Converts AccountDTO object to String object to send it to the client.
     *
     * @param accountDTO AccountDTO object received from UserModule.
     * @return String object with information about the account, or error message if session expired.
     */
    private String accountToString(AccountDTO accountDTO) {
        if (accountDTO == null) return "ERROR SESSION EXPIRED";

        OwnerDTO owner = accountDTO.getOwnerDTO();
        return "account " + accountDTO.getBalance() + " " + accountDTO.getCurrency() + " "
                + accountDTO.getNumber() + " " + owner.getFirstname() + " " + owner.getLastname();
    }

    /**
     * Converts list of transactions to String object to send it to the client.
     *
     * @param history list of Transfer type of previous transactions
     * @return String object with previous transactions.
     */
    private String historyToString(List<Transfer> history) {
        StringBuilder transfers = new StringBuilder();
        transfers.append("history");
        if (history != null && !history.isEmpty()) {
            for (Transfer transfer : history) {
                transfers.append(" ");
                transfers.append(transfer.getAmount()).append(" ");
                transfers.append(transfer.getCurrency()).append(" ");
                transfers.append(transfer.getFromAccount()).append(" ");
                transfers.append(transfer.getToAccount()).append(" ");
                transfers.append(transfer.getTransferDate().getTime());
            }
        }
        return transfers.toString();
    }
}
