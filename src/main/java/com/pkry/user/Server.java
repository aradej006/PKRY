package com.pkry.user;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Created by arade on 29-Dec-15.
 */
@Startup
@Singleton
public class Server {

    @Inject
    UserModule userModule;

    @PostConstruct
    public void init(){
        System.out.println("Working");
    }
}
