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
    public String Password(String password){
        return managementModule.Password(password);
    }
    public String AD(String AD){
        return managementModule.AD(AD);
    }
    public String doTransfer(String data){
        return managementModule.doTransfer(data);
    }
}
