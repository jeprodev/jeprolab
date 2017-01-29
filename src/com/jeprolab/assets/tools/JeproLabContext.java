package com.jeprolab.assets.tools;

import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.controllers.JeproLabController;
import com.jeprolab.models.*;
import com.jeprolab.views.JeproLabApplicationForms;
import org.apache.log4j.Level;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabContext implements Cloneable {
    private static JeproLabContext instance;

    public JeproLabController controller;

    public JeproLabCountryModel country;

    public JeproLabEmployeeModel employee;

    public JeproLabCustomerModel customer;

    public JeproLabCartModel cart;

    public JeproLabCategoryModel category;

    public JeproLabLaboratoryModel laboratory;

    public JeproLabLanguageModel language;

    public JeproLabCurrencyModel currency;

    public JeproLabApplicationForms.JeproLabApplicationForm last_form = null;

    /*public int list_limit = 20;

    public int list_limit_start = 0;* /

    public int category_id = 0;

    public String task = ""; */

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
        }catch (CloneNotSupportedException ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            //clonedContext = null;
        }
        return clonedContext;
    }
}
