package com.pkry.user.Server;

import com.pkry.management.ManagementModule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserModule {

    @Inject
    ManagementModule managementModule;
}
