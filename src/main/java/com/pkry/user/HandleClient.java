package com.pkry.user;

import com.pkry.user.server.Handle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 04-Jan-16.
 */
@Named
@RequestScoped
public class HandleClient implements Handle{

    @Inject
    UserModule userModule;

    @PostConstruct
    public void init(){
        System.out.println("USERMODULE " + userModule.toString());
    }

    public String handle(String data) {

        System.out.println("RECIEVED "+ data);
        return "YUPI";
    }
}
