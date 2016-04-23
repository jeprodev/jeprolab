package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCustomerModel extends JeproLabModel{
    public int customer_id;

    public int laboratory_id;

    public int laboratory_group_id;

    public String secure_key;

    public String note;

    public int default_group_id;

    public int language_id;

    public String title;

    public String lastname;

    public String firstname;

    public Date birthday = null;

    public String email;

    public boolean optin;

    public boolean is_guest;

    public String website;

    public String company;

    public String siret;

    public String ape;

    public boolean news_letter;

    public boolean published;

    public boolean deleted;

    public int state_id;

    public String postcode;

    public int geolocation_country_id;

    public List<Integer> group_box = new ArrayList<>();

    private int years, months, days;

    private Date last_passwd_gen;

    public Date date_add;
    public Date date_upd;
    public Date news_letter_date_add;

    protected static int customer_group = 0;

    protected static Map<Integer, Integer> _defaultGroupId = new HashMap<>();
    protected static Map<String, Boolean> _customerHasAddress = new HashMap<>();
    protected static Map<Integer, List<Integer>> _customer_groups = new HashMap<>();

    public JeproLabCustomerModel(){
        this(0);
    }

    public JeproLabCustomerModel(int customerId){
        if(customerId > 0) {
            String cacheKey = "jeprolab_customer_model_" + customerId + ((this.laboratory_id != 0) ? "_" + this.laboratory_id : "");
            if (!JeproLabCache.getInstance().isStored(cacheKey)) {
                if (dataBaseObject == null) {
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " AS customer ";
                query += " WHERE customer." + dataBaseObject.quoteName("customer_id") + " = " + customerId;
                dataBaseObject.setQuery(query);
                ResultSet customerSet = dataBaseObject.loadObjectList();

                if(customerSet != null){
                    try {
                        if (customerSet.next()) {
                            this.customer_id = customerSet.getInt("customer_id");
                            this.laboratory_id = customerSet.getInt("lang_id");
                            this.laboratory_group_id = customerSet.getInt("lab_group_id");
                            this.secure_key = customerSet.getString("secure_key");
                            this.note = customerSet.getString("note");
                            this.default_group_id = customerSet.getInt("default_group_id");
                            this.language_id = customerSet.getInt("lang_id");
                            this.title = customerSet.getString("title");
                            this.lastname = customerSet.getString("lastname");
                            this.firstname = customerSet.getString("firstname");
                            this.email = customerSet.getString("email");
                            this.birthday = JeproLabTools.getDate(customerSet.getString("birthday"));
                            this.optin = customerSet.getInt("optin") > 0;
                            this.is_guest = customerSet.getInt("is_guest") == 2;
                            this.website = customerSet.getString("website");
                            this.company = customerSet.getString("company");
                            this.siret = customerSet.getString("siret");
                            this.ape = customerSet.getString("ape");
                            this.published = customerSet.getInt("published") > 0;
                            this.state_id = customerSet.getInt("state_id");
                            this.postcode = customerSet.getString("postcode");
                            //this.geolocation_country_id = customerSet.getInt("");
                            this.date_add = JeproLabTools.getDate(customerSet.getString("date_add"));
                            this.date_upd = JeproLabTools.getDate(customerSet.getString("date_upd"));
                        }
                    }catch (SQLException ignored){
                        ignored.printStackTrace();
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    JeproLabCache.getInstance().store(cacheKey, this);
                }
            }else{
                JeproLabCustomerModel customer = (JeproLabCustomerModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.customer_id = customer.customer_id;
                this.laboratory_id = customer.laboratory_id;
                this.laboratory_group_id = customer.laboratory_group_id;
                this.secure_key = customer.secure_key;
                this.note = customer.note;
                this.default_group_id = customer.default_group_id;
                this.language_id = customer.language_id;
                this.title = customer.title;
                this.lastname = customer.lastname;
                this.firstname = customer.firstname;
                this.email = customer.email;
                this.birthday = customer.birthday;
                this.optin =customer.optin;
                this.is_guest = customer.is_guest;
                this.website = customer.website;
                this.company = customer.company;
                this.siret = customer.siret;
                this.ape = customer.ape;
                this.published = customer.published;
                this.state_id = customer.state_id;
                this.postcode = customer.postcode;
                //this.geolocation_country_id = customer.geolocation_country_id;
                this.date_add = customer.date_add;
                this.date_upd = customer.date_upd;
            }
        }
    }

    /**
     * Retrieve customers by company
     *
     * @param company customer company name
     * @return array
     */
    public static List<JeproLabCustomerModel> getCustomersByCompany(String company){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += staticDataBaseObject.quoteName("company") + " = " + staticDataBaseObject.quote(company);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        staticDataBaseObject.setQuery(query);
        ResultSet customersSet = staticDataBaseObject.loadObjectList();

        if(customersSet != null){
            try{
                JeproLabCustomerModel customer;
                while(customersSet.next()){
                    customer = new JeproLabCustomerModel();
                    customer.customer_id = customersSet.getInt("customer_id");
                    customer.laboratory_id = customersSet.getInt("lang_id");
                    customer.laboratory_group_id = customersSet.getInt("lab_group_id");
                    customer.secure_key = customersSet.getString("secure_key");
                    customer.note = customersSet.getString("note");
                    customer.default_group_id = customersSet.getInt("default_group_id");
                    customer.language_id = customersSet.getInt("lang_id");
                    customer.title = customersSet.getString("title");
                    customer.lastname = customersSet.getString("lastname");
                    customer.firstname = customersSet.getString("firstname");
                    //customer.birthday = JeproLabTools.getDate(customersSet.getString("birthday"));
                    customer.optin = customersSet.getInt("optin") > 0;
                    customer.is_guest = customersSet.getInt("is_guest") == 2;
                    customer.website = customersSet.getString("website");
                    customer.company = customersSet.getString("company");
                    customer.siret = customersSet.getString("siret");
                    customer.ape = customersSet.getString("ape");
                    customer.published = customersSet.getInt("published") > 0;
                    /*customer.state_id = customersSet.getInt("state_id");
                    customer.postcode = customersSet.getString("postcode");
                    /*customer.geolocation_country_id = customersSet.;
                    customer.date_add = JeproLabTools.getDate(customersSet.getString("date_add"));
                    customer.date_upd = JeproLabTools.getDate(customersSet.getString("date_upd")); */
                    customers.add(customer);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return customers;
    }

    public static int getDefaultGroupId(int customerId) {
        if (!JeproLabGroupModel.isFeaturePublished()) {
            if (customer_group == 0) {
                customer_group = JeproLabSettingModel.getIntValue("customer_group");
            }
            return customer_group;
        }

        if (!JeproLabCustomerModel._defaultGroupId.containsKey(customerId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("default_group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") +  " = " + customerId;

            staticDataBaseObject.setQuery(query);
            JeproLabCustomerModel._defaultGroupId.put(customerId, (int)staticDataBaseObject.loadValue("default_group_id"));
        }
        return JeproLabCustomerModel._defaultGroupId.get(customerId);
    }

}
