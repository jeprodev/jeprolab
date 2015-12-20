package com.jeprolab.assets.tools;


import com.jeprolab.models.*;

import java.sql.Connection;

public class JeproLabContext implements Cloneable {
    private static JeproLabContext instance;

    public JeproLabCountryModel country;

    public JeproLabEmployeeModel employee;

    public JeproLabCookie cookie;

    public JeproLabLaboratoryModel laboratory;

    public JeproLabLanguageModel language;

    public JeproLabCurrencyModel currency;

    public Connection connection = null;

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