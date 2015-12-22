package com.jeprolab.models.core;

import com.jeprolab.models.JeproLabEmployeeModel;

/**
 *
 * Created by jeprodev on 09/06/2014.
 */
public class JeproLabAuthenticationOption {
    public JeproLabEmployeeModel employee = null;

    public boolean remember = false;

    public String responseType = null;

    public boolean silent = false;

    public boolean secure = false;

    //public boolean silent = false;

    public int lifeTime = 0;

    public int length = 0;
}
