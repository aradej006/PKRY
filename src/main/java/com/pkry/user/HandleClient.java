package com.pkry.user;

import com.pkry.user.server.Handle;

/**
 * Created by arade on 04-Jan-16.
 */
public class HandleClient implements Handle{

    public String handle(String data) {

        System.out.println("RECIEVED "+ data);
        return "YUPI";
    }
}
