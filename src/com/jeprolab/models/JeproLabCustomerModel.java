package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLaMail;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabApplication;
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

    public String password = "";

    public String birthday = null;

    public String email;

    public boolean allow_ads;

    public boolean is_guest;

    public boolean logged = false;

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
                    customer.allow_ads = customersSet.getInt("allow_ads") > 0;
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

    public static List<JeproLabCustomerModel> getCustomers(){
        return getCustomers(false);
    }

    /**
     * Return customers list
     *
     * @param onlyActive Returns only active customers when true
     * @return array Customers
     */
    public static List<JeproLabCustomerModel> getCustomers(boolean onlyActive){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();

        /*String query = "SELECT " + staticDataBaseObject.quoteName("customer_id") + ", " + staticDataBaseObject.quoteName("email");
        query += ", " + staticDataBaseObject.quoteName("firstname") + ", " + staticDataBaseObject.quoteName("lastname") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE 1 " + JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);
        query += (onlyActive ? " AND " + staticDataBaseObject.quoteName("published") + " = 1" : "") + " ORDER BY ";
        query += staticDataBaseObject.quoteName("customer_id") + " ASC";*/

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE 1 ";
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);
        query += (onlyActive ? " AND " + staticDataBaseObject.quoteName("published") + " = 1" : "") + " ORDER BY ";
        query += staticDataBaseObject.quoteName("customer_id") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet customerSet = staticDataBaseObject.loadObjectList();

        if(customerSet != null){
            try{
                JeproLabCustomerModel customer;
                while(customerSet.next()){
                    customer = new JeproLabCustomerModel();
                    customer.customer_id = customerSet.getInt("customer_id");
                    customer.email = customerSet.getString("email");
                    customer.title = customerSet.getString("title");
                    customer.firstname = customerSet.getString("firstname");
                    customer.lastname = customerSet.getString("lastname");
                    //customer.last_visit = JeproLabTools.getDate(customerSet.getString("lase");
                    customer.lastname = customerSet.getString("lastname");
                    customer.company = customerSet.getString("company");
                    customer.date_add = JeproLabTools.getDate(customerSet.getString("date_add"));
                    customer.date_upd = JeproLabTools.getDate(customerSet.getString("date_upd"));
                    customers.add(customer);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return  customers;
    }

    public static void resetAddressCache(int customerId, int addressId){
        String cacheKey = customerId + "_" + addressId;
        if (JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)) {
            JeproLabCustomerModel._customerHasAddress.remove(cacheKey);
        }
    }

    public static int getCustomerIdByEmail(String email){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT customer_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += staticDataBaseObject.quoteName("email") + " = " + staticDataBaseObject.quote(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("customer_id");
    }

    public JeproLabCustomerModel getCustomerByEmail(String email){
        return getCustomerByEmail(email, "");
    }

    public JeproLabCustomerModel getCustomerByEmail(String email, String passWord){
        return getCustomerByEmail(email, passWord, true);
    }

    /**
     * Return customer instance from its e-mail (optionally check password)
     *
     * @param email e-mail
     * @param passWord Password is also checked if specified
     * @return Customer instance
     */
    public JeproLabCustomerModel getCustomerByEmail(String email, String passWord, boolean ignoreGuest){
        if (!JeproLabTools.isEmail(email) || (!password.isEmpty() && !JeproLabTools.isPassWord(passWord))) {
            JeproLabTools.displayError(504, JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String encryptedPass = JeproLabTools.encrypt(passWord);

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE " + dataBaseObject.quoteName("email");
        query += " = '" + dataBaseObject.quoteName(email) + "' " + JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);
        query += (!password.isEmpty()) ? "AND " + dataBaseObject.quoteName("psswd") + " = " + dataBaseObject.quote(encryptedPass) : "";
        query += " AND " + dataBaseObject.quoteName("deleted") + " = 0" + (ignoreGuest ? " AND " + dataBaseObject.quoteName("is_guest") + " = 0 " : "");

        dataBaseObject.setQuery(query);
        ResultSet customerSet = dataBaseObject.loadObjectList();

        if (customerSet != null) {
            try{
                if(customerSet.next()){
                    JeproLabCustomerModel customer = new JeproLabCustomerModel();
                    customer.customer_id = customerSet.getInt("customer_id");
                    customer.laboratory_id = customerSet.getInt("lang_id");
                    customer.laboratory_group_id = customerSet.getInt("lab_group_id");
                    customer.secure_key = customerSet.getString("secure_key");
                    customer.note = customerSet.getString("note");
                    customer.default_group_id = customerSet.getInt("default_group_id");
                    customer.language_id = customerSet.getInt("lang_id");
                    customer.title = customerSet.getString("title");
                    customer.lastname = customerSet.getString("lastname");
                    customer.firstname = customerSet.getString("firstname");
                    customer.birthday = customerSet.getString("birthday");
                    customer.allow_ads = customerSet.getInt("allow_ads") > 0;
                    customer.is_guest = customerSet.getInt("is_guest") == 2;
                    customer.website = customerSet.getString("website");
                    customer.company = customerSet.getString("company");
                    customer.siret = customerSet.getString("siret");
                    customer.ape = customerSet.getString("ape");
                    customer.published = customerSet.getInt("published") > 0;
                    //customer.state_id = customersSet.getInt("state_id");
                    //customer.postcode = customersSet.getString("postcode");
                    //customer.geolocation_country_id = customersSet.;
                    customer.date_add = JeproLabTools.getDate(customerSet.getString("date_add"));
                    customer.date_upd = JeproLabTools.getDate(customerSet.getString("date_upd"));
                    return customer;
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

        return null;
    }

    /**
     * Retrieve customers by email address
     *
     * @  param email customer email address
     * @return List
     * /
    public static JeproLabCustomerModel getCustomerByEmail(String email){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += staticDataBaseObject.quoteName("email") + " = " + staticDataBaseObject.quote(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        staticDataBaseObject.setQuery(query);
        ResultSet customersSet = staticDataBaseObject.loadObjectList();
        JeproLabCustomerModel customer;
        if(customersSet != null){
            try{

                if(customersSet.next()){
                    customer = new JeproLabCustomerModel();
                    return customer;
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
        return null;
    } */

    public void add(){
        this.laboratory_id = this.laboratory_id > 0 ? this.laboratory_id : JeproLabContext.getContext().laboratory.laboratory_id;
        this.laboratory_group_id = (this.laboratory_group_id > 0) ? this.laboratory_group_id : JeproLabContext.getContext().laboratory.laboratory_group_id;

        this.language_id = (this.language_id > 0) ? this.language_id : JeproLabContext.getContext().language.language_id;
        this.birthday = ((this.years == 0) ? this.birthday : (this.years + "-" + this.months + "-" + this.days));

        if(this.news_letter){
            this.news_letter_date_add = new Date();
        }

        if (this.default_group_id == JeproLabSettingModel.getIntValue("customer_group")){
            if (this.is_guest) {
                this.default_group_id = JeproLabSettingModel.getIntValue("guest_group");
            } else {
                this.default_group_id = JeproLabSettingModel.getIntValue("customer_group");
            }
        }

        /* Can't create a guest customer, if this feature is disabled */
        if (this.is_guest && !(JeproLabSettingModel.getIntValue("guest_checkout_enabled") > 0)){
            //break;
        }

        if(this.title == null || (this.title.equals(""))){ this.title = "Mr"; }

        String dateAdded = JeproLabTools.date();
        String birthdayString = this.birthday != null ? "" : JeproLabTools.date();

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_customer") + "(" + dataBaseObject.quoteName("lab_id") + ", ";
        query +=  dataBaseObject.quoteName("lab_group_id") + ", " + dataBaseObject.quoteName("secure_key");
        query += ", " + dataBaseObject.quoteName("note") + ", " + dataBaseObject.quoteName("default_group_id") + ", " + dataBaseObject.quoteName("lang_id");
        query += ", " + dataBaseObject.quoteName("title") + ", " + dataBaseObject.quoteName("lastname") + ", " + dataBaseObject.quoteName("firstname");
        query += ", " + dataBaseObject.quoteName("birthday") + ", " + dataBaseObject.quoteName("email") + ", " + dataBaseObject.quoteName("allow_ads");
        query += ", " + dataBaseObject.quoteName("is_guest") + ", " + dataBaseObject.quoteName("website") + ", " + dataBaseObject.quoteName("company");
        query += ", " + dataBaseObject.quoteName("siret") + ", " + dataBaseObject.quoteName("ape") + ", " + dataBaseObject.quoteName("newsletter");
        query += ", " + dataBaseObject.quoteName("published") + ", " + dataBaseObject.quoteName("deleted") + ", " + dataBaseObject.quoteName("passwd");
        //query += ", " + dataBaseObject.quoteName("geolocation_country_id");
        query += ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd");
        query += ") VALUES (" + this.laboratory_id + ", " + this.laboratory_group_id + ", " + dataBaseObject.quote(this.secure_key) + ", " + dataBaseObject.quote(this.note);
        query += ", " + this.default_group_id + ", " + this.language_id + ", " + dataBaseObject.quote(this.title) + ", " + dataBaseObject.quote(this.lastname);
        query += ", " + dataBaseObject.quote(this.firstname)  + ", " + dataBaseObject.quote(birthdayString) + ", " + dataBaseObject.quote(this.email) + ", ";
        query += (this.allow_ads ? 1 : 0 ) + ", " + (this.is_guest ? 1 : 0) + ", " + dataBaseObject.quote(this.website) + ", " + dataBaseObject.quote(this.company) + ", ";
        query += dataBaseObject.quote(this.siret) + ", " + dataBaseObject.quote(this.ape) + ", " + (this.news_letter ? 1 : 0) + ", " + (this.published ? 1 : 0);
        query += ", " + (this.deleted ? 1 : 0) + ", " + dataBaseObject.quote(this.password) + ", ";
        query +=  dataBaseObject.quote(dateAdded) + ", " + dataBaseObject.quote(dateAdded) + ")";

        dataBaseObject.setQuery(query);
        dataBaseObject.query(true);
        this.customer_id = dataBaseObject.getGeneratedKey();

        this.updateGroup(this.group_box);

        if (this.news_letter && !JeproLabTools.isDate(this.news_letter_date_add)) {
            this.news_letter_date_add = new Date();
        }
    }

    /**
     * Update customer groups associated to the object
     *
     * @param list groups
     */
    public void updateGroup(List<Integer> list){
        if (list != null && !list.isEmpty()) {
            this.cleanGroups();
            this.addGroups(list);
        } else {
            list = new ArrayList<>();
            this.cleanGroups();
            list.add(this.default_group_id);
            this.addGroups(list);
        }
    }

    public void cleanGroups(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer_group") + " WHERE ";
        query += dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);
    }

    public void addGroups(List<Integer> groups){
        String query;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        for(Integer groupId : groups) {

            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_customer_group") + "(" + dataBaseObject.quoteName("customer_id");
            query += ", " + dataBaseObject.quoteName("group_id") + ") VALUES (" + this.customer_id + ", " + groupId + ")";
            dataBaseObject.setQuery(query);
            dataBaseObject.query(false);
        }
    }

    public static List<Integer> getStaticGroups(int customerId){
        List<Integer> groups;
        if (!JeproLabGroupModel.isFeaturePublished()) {
            groups = new ArrayList<>();
            groups.add(JeproLabSettingModel.getIntValue("customer_group"));
            return groups;
        }

        if (customerId == 0) {
            groups = new ArrayList<>();
            groups.add(JeproLabSettingModel.getIntValue("unidentified_group"));
            JeproLabCustomerModel._customer_groups.put(customerId, groups);
        }

        if (!JeproLabCustomerModel._customer_groups.containsKey(customerId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT customer_group." + staticDataBaseObject.quoteName("group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer_group");
            query += " AS customer_group WHERE customer_group." + staticDataBaseObject.quoteName("customer_id") + " = " + customerId;

            staticDataBaseObject.setQuery(query);
            ResultSet groupSet = staticDataBaseObject.loadObjectList();
            if(groupSet != null){
                try{
                    groups = new ArrayList<>();
                    while (groupSet.next()){
                        groups.add(groupSet.getInt("group_id"));
                    }
                    JeproLabCustomerModel._customer_groups.put(customerId, groups);
                    return groups;
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception exc){
                        exc.printStackTrace();
                    }
                }
            }
        }
        return JeproLabCustomerModel._customer_groups.get(customerId);
    }

    public List<Integer> getGroups(){
        return JeproLabCustomerModel.getStaticGroups(this.customer_id);
    }

    public boolean update(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(this.news_letter){
            this.news_letter_date_add = new Date();
        }

        if (this.news_letter && !JeproLabTools.isDate(this.news_letter_date_add)) {
            this.news_letter_date_add = new Date();;
        }
        this.updateGroup(this.group_box);


        if (this.deleted) {
            List<JeproLabAddressModel> addresses = this.getAddresses(JeproLabSettingModel.getIntValue("default_lang"));
            addresses.forEach(com.jeprolab.models.JeproLabAddressModel::delete);
        }

        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_customer") + " SET " + dataBaseObject.quoteName("lab_id") + " = ";
        query += this.laboratory_id + ", " + dataBaseObject.quoteName("lab_group_id") + " = " + this.laboratory_group_id + ", ";
        query += dataBaseObject.quoteName("secure_key") + " = " + dataBaseObject.quote(this.secure_key) + ", " + dataBaseObject.quoteName("note");
        query += " = " + dataBaseObject.quote(this.note) + ", " + dataBaseObject.quoteName("default_group_id") + " = " + this.default_group_id;
        query += ", " + dataBaseObject.quoteName("title") + " = " + dataBaseObject.quote(this.title) + ", " + dataBaseObject.quoteName("lastname");
        query +=  " = " + dataBaseObject.quote(this.lastname) + ", " + dataBaseObject.quoteName("firstname") + " = " + dataBaseObject.quote(this.firstname);
        query += ", " + dataBaseObject.quoteName("birthday") + " = " + dataBaseObject.quote(this.birthday) + ", " + dataBaseObject.quoteName("email");
        query += " = " + dataBaseObject.quote(this.email) + ", " + dataBaseObject.quoteName("allow_ads") + " = " + ( this.allow_ads ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("is_guest") + " = " + (this.is_guest ? 1 : 0) + ", " + dataBaseObject.quoteName("website") + " = ";
        query += dataBaseObject.quote(this.website) + ", " + dataBaseObject.quoteName("company") + " = " + dataBaseObject.quote(company) + ", ";
        query += dataBaseObject.quoteName("siret") + " = " + dataBaseObject.quote(this.siret) + ", " + dataBaseObject.quoteName("ape") + " = ";
        query += dataBaseObject.quote(this.ape) + ", " + dataBaseObject.quoteName("newsletter") + " = " + (this.news_letter ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("published") + " = " + (this.published ? 1 : 0) + ", " + dataBaseObject.quoteName("deleted") + " = " ;
        query += (this.deleted ? 1 : 0) + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        return  dataBaseObject.query(false);

        /*public int state_id;
        public String postcode;
        public String last_visit;
        public int geolocation_country_id;public int language_id;

        public List<Integer> group_box = new ArrayList<>();

        private int years, months, days;

        private Date last_passwd_gen;

        public Date date_add;
        public Date date_upd;
        public Date news_letter_date_add;*/
    }

    public void delete() {
        if (!(JeproLabRequestModel.getRequestsByCustomerId(this.customer_id).size() > 0)) {
            List<JeproLabAddressModel> addresses = this.getAddresses(JeproLabSettingModel.getIntValue("default_lang"));
            addresses.forEach(com.jeprolab.models.JeproLabAddressModel::delete);
        }

        if(dataBaseObject == null){ dataBaseObject = JeproLabFactory.getDataBaseConnector(); }
        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer_group") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_message") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE customer_id = " + this.customer_id;
        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_compare") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

        query = "SELECT cart_id FROM " + dataBaseObject.quoteName("#__jeprolab_cart") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        dataBaseObject.setQuery(query);

        ResultSet cartSet = dataBaseObject.loadObjectList();
        if (cartSet != null) {
            try {
                int cartId;
                while (cartSet.next()) {
                    cartId = cartSet.getInt("cart_id");
                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart") + " WHERE " + dataBaseObject.quoteName("cart_id") + " = " + cartId;
                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);

                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE cart_id = " + cartId;
                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);
                }
            } catch (SQLException ignored) {
                ignored.printStackTrace();
            } finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        query = "SELECT customer_thread_id FROM " + dataBaseObject.quoteName("#__jeprolab_customer_thread") + " WHERE customer_id = " + this.customer_id;
        dataBaseObject.setQuery(query);

        ResultSet threadIdsSet = dataBaseObject.loadObjectList();
        if(threadIdsSet != null) {
            try {
                int customerThreadId;
                while (threadIdsSet.next()) {
                    customerThreadId = threadIdsSet.getInt("customer_thread_id");
                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer_thread") + " WHERE customer_thread_id = " + customerThreadId;
                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);

                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer_message") + " WHERE customer_thread_id = " + customerThreadId;
                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);
                }
            } catch (SQLException ignored) {
                ignored.printStackTrace();
            } finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        JeproLabCartModel.JeproLabCartRuleModel.deleteByCustomerId(this.customer_id);
        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer");
        query += " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);
    }

    /**
     * Return customer addresses
     *
     * @param langId Language ID
     * @return array Addresses
     */
    public List<JeproLabAddressModel> getAddresses(int langId){
        boolean shareRequest = JeproLabContext.getContext().laboratory.getLaboratoryGroup().share_requests;
        String cacheKey = "jeprolab_customer_model_get_addresses_" + this.customer_id + "_" + langId + "_"  + (shareRequest ? 1 : 0);
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT DISTINCT address.*, country_lang." + dataBaseObject.quoteName("name") + " AS country, state.name AS state_name,";
            query += " state.iso_code AS state_iso FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " AS address LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_country")  + " AS country ON (address." + dataBaseObject.quoteName("country_id");
            query += " = country." + dataBaseObject.quoteName("country_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lang");
            query += " AS country_lang ON (country." + dataBaseObject.quoteName("country_id") + " = country_lang." + dataBaseObject.quoteName("country_id");
            query += ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_state") + " AS state ON (state." + dataBaseObject.quoteName("state_id") ;
            query += " = address." + dataBaseObject.quoteName("state_id") + (shareRequest ? "" : JeproLabLaboratoryModel.addSqlAssociation("country"));
            query += " WHERE " + dataBaseObject.quoteName("lang_id") + " = " + langId + " AND " + dataBaseObject.quoteName("customer_id") + " = ";
            query += this.customer_id + " AND address." + dataBaseObject.quoteName("deleted") + " = 0";

            dataBaseObject.setQuery(query);
            ResultSet resultSet = dataBaseObject.loadObjectList();
            List<JeproLabAddressModel> addressList = new ArrayList<>();

            if(resultSet != null){
                try {
                    JeproLabAddressModel address;
                    while(resultSet.next()){
                        address = new JeproLabAddressModel();
                        address.address_id = resultSet.getInt("address_id");
                        addressList.add(address);
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e){ e.printStackTrace(); }
                }
            }
            JeproLabCache.getInstance().store(cacheKey, addressList);
            return addressList;
        }
        return (List<JeproLabAddressModel>)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static String getNameByCustomerId(int customerId) {
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("firstname") + ", " + staticDataBaseObject.quoteName("lastname") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = ";
        query += customerId;

        staticDataBaseObject.setQuery(query);
        ResultSet customerSet = staticDataBaseObject.loadObjectList();
        if(customerSet != null){
            try{
                if(customerSet.next()){
                    return customerSet.getString("firstname") + " " + customerSet.getString("lastname").toUpperCase();
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return  "";
    }

    /**
     * Soft logout, delete everything links to the customer
     * but leave there affiliate's information
     *
     */
    public void logout(){
        //Hook::exec('actionCustomerLogoutBefore', array('customer' => $this));

        if (JeproLabContext.getContext().cookie != null){
            JeproLabContext.getContext().cookie.logout();
        }

        this.logged = false;

        //Hook::exec('actionCustomerLogoutAfter', array('customer' => $this));
    }

    public int getLastCart(){
        return getLastCart(true);
    }

    public int getLastCart(boolean withOrder ){
        List<JeproLabCartModel> carts = JeproLabCartModel.getCustomerCarts(this.customer_id, withOrder);
        if(carts.isEmpty()) {
            return 0;
        }
        JeproLabCartModel cart =  carts.get(0); // array_shift($carts);
        cart = new JeproLabCartModel(cart.cart_id);
        return (cart.getNumberOfAnalyzes() == 0 ? cart.cart_id : 0);
    }

    public static boolean customerExists(String email){
        return customerExists(email, true);
    }

    /**
     * Check if e-mail is already registered in database
     *
     * @param email e-mail
     * @param ignoreGuest boolean, to exclude guest customer
     * @return Customer ID if found, false otherwise
     */
    public static boolean customerExists(String email, boolean ignoreGuest){
        if (!JeproLabTools.isEmail(email)) {
        /*if (defined('_PS_MODE_DEV_') && _PS_MODE_DEV_) {
            die(Tools::displayError('Invalid email'));
        }*/
            return false;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("customer_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer");
        query += " WHERE " + staticDataBaseObject.quoteName("email") + " = " + staticDataBaseObject.quote(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER) ;
        query += (ignoreGuest ? " AND " + staticDataBaseObject.quoteName("is_guest") + " = 0 " : "") ;

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadValue("customer_id") > 0;
    }

    /**
     * Check if an address is owned by a customer
     *
     * @param customerId Customer ID
     * @param addressId Address ID
     * @return bool result
     */
    public static boolean customerHasAddress(int customerId, int addressId){
        String cacheKey = customerId + "_" + addressId;
        if (!JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quote("address_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND ";
            query += staticDataBaseObject.quoteName("address_id") + " = " + addressId + " AND " ;
            query += staticDataBaseObject.quoteName("deleted") + " =  0";

            staticDataBaseObject.setQuery(query);

            int address = (int)staticDataBaseObject.loadValue("address_id");
            JeproLabCustomerModel._customerHasAddress.put(cacheKey, (address > 0));
        }
        return JeproLabCustomerModel._customerHasAddress.get(cacheKey);
    }

    /*public static void resetAddressCache(int customerId, int addressId){
        String cacheKey = customerId + "_"  + addressId;
        if (JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)) {
            JeproLabCustomerModel._customerHasAddress.remove(cacheKey);
        }
    } */

    /**
     * Toggles object status in database
     *
     * @return bool Update result
     */
    public boolean toggleStatus(){
        if(dataBaseObject != null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        /* Change status to active/inactive */
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_customer") + " SET " + dataBaseObject.quoteName("date_upd");
        query += " = NOW(), " + dataBaseObject.quoteName("published") + (this.published ? 0 : 1) + " WHERE ";
        query += dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }


    public boolean isGuest(){
        return this.is_guest;
    }

    public boolean transformToCustomer(int langId){
        return this.transformToCustomer(langId, "");
    }

    public boolean transformToCustomer(int langId, String passWord){
        if (!this.isGuest()) {
            return false;
        }

        if(passWord.isEmpty()){
            passWord = JeproLabTools.passWordGen(8, "RANDOM");
        }

        if (!JeproLabTools.isPassWord(passWord)) {
            return false;
        }

        this.is_guest = false;
        this.password = JeproLabTools.encrypt(passWord);
        this.cleanGroups();
        List<Integer> groups = new ArrayList<>();
        groups.add(JeproLabSettingModel.getIntValue("customer_group"));
        this.addGroups(groups); // add default customer group
        if(this.update()){
            Map<String, String> vars = new HashMap<>();
            vars.put("firstname", this.firstname);
            vars.put("lastname", this.lastname);
            vars.put("email", this.email);
            vars.put("passwd", passWord);

            JeproLaMail.send(langId, JeproLab.getBundle().getString("JEPROLAB_GUEST_TO_CUSTOMER_LABEL"),
                    JeproLab.getBundle().getString("JEPROLAB_YOUR_ACCOUNT_HAS_BEEN_TRANSFORMED_INTO_A_CUSTOMER_ACCOUNT_MESSAGE"),
                    vars, this.email, this.firstname + " " + this.lastname.toUpperCase(), null, null, null, null,
                    JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, this.laboratory_id
            );
            return true;
        }
        return false;
    }

    public boolean isLogged(){
        return this.isLogged(false);
    }

    /**
     * Check customer information and return customer validity
     *
     * @since 1.5.0
     * @param withGuest
     * @return bool customer validity
     */
    public boolean isLogged(boolean withGuest){
        if (!withGuest && this.is_guest) {
            return false;
        }

        /* Customer is valid only if it can be load and if object password is the same as database one */
        return (this.logged && this.customer_id > 0 && JeproLabCustomerModel.checkPassWord(this.customer_id, this.password));
    }

    public static int getCurrentCountryId(int customerId){
        return getCurrentCountryId(customerId, null);
    }

    public static int getCurrentCountryId(int customerId, JeproLabCartModel cart){
        if (cart == null) {
            cart = JeproLabContext.getContext().cart;
        }
        int addressId;
        if (cart == null || ((JeproLabSettingModel.getStringValue("tax_address_type").equals("delivery_address_id")) ? cart.delivery_address_id <= 0 :  cart.invoice_address_id <= 0)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("address_id") + "FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND " + staticDataBaseObject.quoteName("deleted");
            query += " = 0 ORDER BY " + staticDataBaseObject.quoteName("address_id");

            staticDataBaseObject.setQuery(query);
            addressId = (int)staticDataBaseObject.loadValue("address_id");
        } else {
            addressId = (JeproLabSettingModel.getStringValue("tax_address_type").equals("delivery_address_id")) ? cart.delivery_address_id : cart.invoice_address_id;
        }
        Map<String, String> ids = JeproLabAddressModel.getCountryAndState(addressId);
        return ids.containsKey("country_id") ? Integer.parseInt(ids.get("country_id")) : JeproLabSettingModel.getIntValue("default_country");
    }

    public List<JeproLabRequestModel> getRequestedAnalyzes(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_request") + " AS request LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_request_detail") + " AS request_detail ON(request.";
        query += dataBaseObject.quoteName("request_id") + " = request_detail." + dataBaseObject.quoteName("request_id");
        query += " WHERE request." + dataBaseObject.quoteName("valid") + " = 1 AND request." + dataBaseObject.quoteName("customer_id");
        query += " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        List<JeproLabRequestModel> requestSetList = new ArrayList<>();
        ResultSet requestSet = dataBaseObject.loadObjectList();
        if(requestSet != null){
            try{
                JeproLabRequestModel request;
                while(requestSet.next()){
                    request = new JeproLabRequestModel();
                    requestSetList.add(request);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }
        }
        return requestSetList; /*Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT * FROM `'._DB_PREFIX_.'orders` o
        LEFT JOIN `'._DB_PREFIX_.'order_detail` od ON o.id_order = od.id_order
        WHERE o.valid = 1 AND o.`id_customer` = '.(int)$this->id); */
    }

    /**
     * Retrieve customers by email address
     *
     * @param email
     * @return array
     */
    public static List<JeproLabCustomerModel> getCustomersByEmail(String email){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += staticDataBaseObject.quoteName("email") + " = " + staticDataBaseObject.quoteName(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        staticDataBaseObject.setQuery(query);
        ResultSet customersSet = staticDataBaseObject.loadObjectList();
        List<JeproLabCustomerModel> customerList = new ArrayList<>();
        if(customersSet != null){
            try {
                JeproLabCustomerModel customer;
                while(customersSet.next()){
                    customer = new JeproLabCustomerModel();
                    customer.customer_id = customersSet.getInt("customer_id");
                    customerList.add(customer);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }
        }

        return customerList;
    }

    /**
     * Check if customer password is the right one
     *
     * @param passWord Password
     * @return bool result
     */
    public static boolean checkPassWord(int customerId, String passWord){
        if (customerId < 0 || !JeproLabTools.isMd5(passWord)) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_"));
        }
        String cacheKey = "jeprolab_customer_check_password_" + customerId + "_" + passWord;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if (staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("customer_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND ";
            query += staticDataBaseObject.quoteName("passwd") + " = " + staticDataBaseObject.quote(passWord);

            staticDataBaseObject.setQuery(query);
            boolean result = staticDataBaseObject.loadValue("customer_id") > 0;
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
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
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer_group");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + context.customer.customer_id;

            staticDataBaseObject.setQuery(query);
            ResultSet customerGroupSet = staticDataBaseObject.loadObjectList();

            if(customerGroupSet != null){
                try{
                    while(customerGroupSet.next()){
                        JeproLabCustomerModel.current_customer_groups.add(customerGroupSet.getInt("group_id"));
                    }
                }catch(SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch(Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
        }
        return JeproLabCustomerModel.current_customer_groups;
    }
}
