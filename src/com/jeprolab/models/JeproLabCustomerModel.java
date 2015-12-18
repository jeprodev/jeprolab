package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.controllers.JeproLabCustomerController;

import java.sql.ResultSet;
import java.util.Date;

public class JeproLabCustomerModel  extends JeproLabModel{
    public int customer_id;

    public int lab_id;

    public int lab_group_id;

    public String secure_key;

    public String note;

    public int default_group_id;

    public int lang_id;

    public String title;

    public String lastname;

    public String firstname;

    public Date birthday = null;

    public boolean  optin;

    public boolean is_guest;

    public String website;

    public String company;

    public String siret;

    public String ape;

    public boolean published;

    public int state_id;

    public String postcode;

    public int geolocation_country_id;

    public Date date_add;
    public Date date_upd;

    public JeproLabCustomerModel(){
        this(0);
    }

    public JeproLabCustomerModel(int customerId){
        if(customerId > 0) {
            ResultSet results = null;
            String query = "";
            String cache_id = "jeprolab_customer_model_" + customerId + ((this.lab_id != 0) ? "_" + this.lab_id : "");
            if (!JeproLabCustomerController.isStored(cache_id)) {
                try {
                    dataBaseObject = JeproLabDataBaseConnector.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                query += "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " AS customer ";

                /** Get customer lab information  **/
                if (JeproLabLaboratoryModel.isTableAssociated("customer")) {
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_customer_lab") + " AS customer_lab ON (customer";
                    query += " = customer";
                }

                query += " WHERE customer." + dataBaseObject.quoteName("customer_id") + " = " + customerId;
                /*dataBaseObject.setQuery(query);
                dataBaseObject.query();
                results = dataBaseObject.getResultSet(); */
            }
        }
        //this.default_group_id = (int)JeproLabSettingModel.getValue("customer_group");
    }

    /** data manipulating methods **/

    /** static Methods ***/

}