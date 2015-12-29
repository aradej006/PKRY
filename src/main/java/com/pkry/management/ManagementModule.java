package com.pkry.management;

import com.pkry.db.DbModule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arade on 29-Dec-15.
 */
@Named
@RequestScoped
public class ManagementModule {

    @Inject
    DbModule dbModule;

}
