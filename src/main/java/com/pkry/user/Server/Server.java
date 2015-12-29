package com.pkry.user.Server;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.security.PublicKey;

@Startup
@Singleton
public class Server {

    @Inject
    UserModule userModule;
    PublicKey publicKey;
    SHA sha;

    @PostConstruct
    public void init(){
        System.out.println("WorkingSERVER");
    }
}
