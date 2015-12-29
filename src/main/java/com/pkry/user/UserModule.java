package com.pkry.user;
import com.pkry.management.ManagementModule;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserModule{

    @Inject
    ManagementModule managementModule;

    public String Login(String login){
        return managementModule.Login(login);
    }
    public String Password(String login, String password, String passwordIndexes){
        return managementModule.insertPassword(login, password, passwordIndexes);
    }
    public String AD(String login, String password, String passwordIndexes, String AD, String ADIndexes){
        return managementModule.insertAD(login, password,passwordIndexes,AD, ADIndexes);
    }
    public String doTransfer(String data){
        return managementModule.doTransfer(data);
    }
}
