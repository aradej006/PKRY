package com.pkry.user;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;

/**
 * Created by arade on 29-Dec-15.
 */

@Startup
@javax.ejb.Singleton
public class Server {

    @PostConstruct
    public void init(){
        System.out.println("SERVER-test");
    }


}
