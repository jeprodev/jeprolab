package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
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

    public String password = "";

    public String birthday = null;

    public String email;

    public boolean allow_ads;

    public boolean is_guest;

    public boolean is_logged;

    public String website;

    public String company;

    public String siret;

    public String ape;

    public boolean news_letter;

    public boolean published;

    public boolean deleted;

    public int state_id;

    public String postcode;
    public Date last_visit;

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

    /**
     * @var array Holds current customer's groups.
     */
    protected static List<Integer> current_customer_groups = new ArrayList<>();

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
                            this.birthday = customerSet.getString("birthday");
                            this.allow_ads = customerSet.getInt("allow_ads") > 0;
                            this.is_guest = customerSet.getInt("is_guest") == 2;
                            this.website = customerSet.getString("website");
                            this.company = customerSet.getString("company");
                            this.siret = customerSet.getString("siret");
                            this.ape = customerSet.getString("ape");
                            this.published = customerSet.getInt("published") > 0;
                            //this.state_id = customerSet.getInt("state_id");
                            //this.postcode = customerSet.getString("postcode");
                            //this.geolocation_country_id = customerSet.getInt("");
                            this.date_add = customerSet.getDate("date_add");
                            this.date_upd = customerSet.getDate("date_upd");
                        }
                    }catch (SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception ignored) {
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
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
                this.allow_ads =customer.allow_ads;
                this.is_guest = customer.is_guest;
                this.website = customer.website;
                this.company = customer.company;
                this.siret = customer.siret;
                this.ape = customer.ape;
                this.published = customer.published;
                //this.state_id = customer.state_id;
                this.postcode = customer.postcode;
                //this.geolocation_country_id = customer.geolocation_country_id;
                this.date_add = customer.date_add;
                this.date_upd = customer.date_upd;
            }
        }
    }

    /**
     * Sets and returns customer groups that the current customer(visitor) belongs to.
     *
     * @return array
     */
    public static List<Integer> getCurrentCustomerGroups(){
        if (!JeproLabGroupModel.isFeaturePublished()) {
            return new ArrayList<>();
        }

        JeproLabContext context = JeproLabContext.getContext();
        if (context.customer == null || context.customer.customer_id <= 0) {
            return new ArrayList<>();
        }

        if (JeproLabCustomerModel.current_customer_groups == null && JeproLabCustomerModel.current_customer_groups.isEmpty()) {
            JeproLabCustomerModel.current_customer_groups = new ArrayList<>();
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_customer_group");
            query += " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + context.customer.customer_id;

            dataBaseObject.setQuery(query);
            ResultSet customerGroupSet = dataBaseObject.loadObjectList();

            if(customerGroupSet != null){
                try{
                    while(customerGroupSet.next()){
                        JeproLabCustomerModel.current_customer_groups.add(customerGroupSet.getInt("group_id"));
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch(Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
        }
        return JeproLabCustomerModel.current_customer_groups;
    }

    public static int getDefaultGroupId(int customerId) {
        if (!JeproLabGroupModel.isFeaturePublished()) {
            if (customer_group == 0) {
                customer_group = JeproLabSettingModel.getIntValue("customer_group");
            }
            return customer_group;
        }

        if (!JeproLabCustomerModel._defaultGroupId.containsKey(customerId)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("default_group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_customer");
            query += " WHERE " + dataBaseObject.quoteName("customer_id") +  " = " + customerId;

            dataBaseObject.setQuery(query);
            JeproLabCustomerModel._defaultGroupId.put(customerId, (int)dataBaseObject.loadValue("default_group_id"));
        }
        return JeproLabCustomerModel._defaultGroupId.get(customerId);
    }
}
