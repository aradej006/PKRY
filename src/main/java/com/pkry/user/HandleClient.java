package com.pkry.user;

import com.pkry.db.model.DTOs.AccountDTO;
import com.pkry.user.server.Handle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class handles requests from the user
 */
@Named
@ApplicationScoped
public class HandleClient implements Handle{

    /**
     * Injected object of UserModule class
     */
    @Inject
    UserModule userModule;

    @PostConstruct
    public void init(){
        System.out.println("USERMODULE " + userModule.toString());
    }

    /**
     * function handles messages got from the user
     * @param data message
     * @return if the request is correct returns some piece of data, otherwise an error message
     */
    public String handle(String data) {
        String [] msg = data.split(" ");

        System.out.println(data);

        if(data.contains("Login")) {
            return "PasswordIndexes " + userModule.Login(msg[1]);
        }else if(data.contains("Password")) {
            return "PESELIndexes " + userModule.insertPassword(msg[1], msg[2], msg[3]);
        }else if(data.contains("PESELNumbers")){
            System.out.println(data);
            if(userModule.insertAD(msg[1], msg[2], msg[3], msg[4], msg[5])!=null){

              return AccountToString(userModule.insertAD(msg[1], msg[2], msg[3], msg[4], msg[5]));
            }else {
                return "ERROR";
            }
        }else if(data.contains("DoTransfer")){
            return userModule.doTransfer(msg[1], Double.parseDouble(msg[2]), msg[3]);
        }
        else if(data.contains("Logout")){

            if(userModule.logout(msg[1])){
                return "LOGOUT";
            }else{
                return "ERROR";
            }
        }

        return "ERROR";
    }

    /**
     * Converts object of type AccountDTO to string to send to the client
     * @param accountDTO
     * @return
     */
    private String AccountToString(AccountDTO accountDTO) {
//        return "LoggedIn " + accountDTO.getBalance().toString() + " " + accountDTO.getCurrency()
//                + " " + accountDTO.getOwnerDTO().toString();
        return "LoggedIn ";
    }
}
