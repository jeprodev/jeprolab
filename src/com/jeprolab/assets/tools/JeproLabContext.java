package com.jeprolab.assets.tools;

//import java.sql.Connection;

import com.jeprolab.controllers.JeproLabController;
import com.jeprolab.models.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabContext implements Cloneable {
    private static JeproLabContext instance;

    public JeproLabCountryModel country;

    public JeproLabEmployeeModel employee;

    public JeproLabCustomerModel customer;

    public JeproLabCartModel cart;

    public JeproLabCookie cookie;

    public JeproLabLaboratoryModel laboratory;

    public JeproLabLanguageModel language;

    public JeproLabCurrencyModel currency;

    public JeproLabController controller;

    //public Connection connection = null;

    public int list_limit = 20;

    public int list_limit_start = 0;

    public int category_id = 0;

    public String task = "";

    public static JeproLabContext getContext(){
        if(instance == null){
            instance = new JeproLabContext();
        }
        return instance;
    }

    /**
     * Clone current context
     * @return JeproLabContext
     */
    public JeproLabContext cloneContext() throws CloneNotSupportedException {
        return clone();
    }

    @Override
    public JeproLabContext clone(){
        JeproLabContext clonedContext = null;
        try{
            clonedContext = (JeproLabContext)super.clone();
        }catch (CloneNotSupportedException cnse){
            cnse.printStackTrace(System.err);
            //clonedContext = null;
        }
        return clonedContext;
    }
}