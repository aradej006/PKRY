package com.pkry.user;

import com.pkry.management.ManagementModule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class UserModule {

    @Inject
    ManagementModule managementModule;
}
