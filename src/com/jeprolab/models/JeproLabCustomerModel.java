package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
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

    public String birthday = null;

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
    public String last_visit;

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
                            this.birthday = customerSet.getString("birthday");
                            this.optin = customerSet.getInt("optin") > 0;
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
                this.optin =customer.optin;
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
                    customer.firstname = customerSet.getString("firstname");
                    customer.lastname = customerSet.getString("lastname");
                    //customer.last_visit = customerSet.getString("lastname");
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

    /**
     * Retrieve customers by email address
     *
     * @param email customer email address
     * @return List
     */
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
                    customer.birthday = customersSet.getString("birthday");
                    customer.optin = customersSet.getInt("optin") > 0;
                    customer.is_guest = customersSet.getInt("is_guest") == 2;
                    customer.website = customersSet.getString("website");
                    customer.company = customersSet.getString("company");
                    customer.siret = customersSet.getString("siret");
                    customer.ape = customersSet.getString("ape");
                    customer.published = customersSet.getInt("published") > 0;
                    //customer.state_id = customersSet.getInt("state_id");
                    //customer.postcode = customersSet.getString("postcode");
                    //customer.geolocation_country_id = customersSet.;
                    customer.date_add = JeproLabTools.getDate(customersSet.getString("date_add"));
                    customer.date_upd = JeproLabTools.getDate(customersSet.getString("date_upd"));
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

    public void add(){
        this.laboratory_id = this.laboratory_id > 0 ? this.laboratory_id : JeproLabContext.getContext().laboratory.laboratory_id;
        this.laboratory_group_id = (this.laboratory_group_id > 0) ? this.laboratory_group_id : JeproLabContext.getContext().laboratory.laboratory_group_id;

        this.language_id = (this.language_id > 0) ? this.language_id : JeproLabContext.getContext().language.language_id;
        this.birthday = ((this.years == 0) ? this.birthday : (this.years + "-" + this.months + "-" + this.days));

        if (this.news_letter && !JeproLabTools.isDate(this.news_letter_date_add)) {
            this.news_letter_date_add = JeproLabTools.getDate("Y-m-d H:i:s");
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

        String dateAdded = JeproLabTools.date();
        String birthdayString = this.birthday != null ? "" : JeproLabTools.date();

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_customer") + "(" + dataBaseObject.quoteName("lab_id") + ", ";
        query +=  dataBaseObject.quoteName("lab_group_id") + ", " + dataBaseObject.quoteName("secure_key");
        query += ", " + dataBaseObject.quoteName("note") + ", " + dataBaseObject.quoteName("default_group_id") + ", " + dataBaseObject.quoteName("lang_id");
        query += ", " + dataBaseObject.quoteName("title") + ", " + dataBaseObject.quoteName("lastname") + ", " + dataBaseObject.quoteName("firstname");
        query += ", " + dataBaseObject.quoteName("birthday") + ", " + dataBaseObject.quoteName("email") + ", " + dataBaseObject.quoteName("optin");
        query += ", " + dataBaseObject.quoteName("is_guest") + ", " + dataBaseObject.quoteName("website") + ", " + dataBaseObject.quoteName("company");
        query += ", " + dataBaseObject.quoteName("siret") + ", " + dataBaseObject.quoteName("ape") + ", " + dataBaseObject.quoteName("newsletter");
        query += ", " + dataBaseObject.quoteName("published") + ", " + dataBaseObject.quoteName("deleted") + ", " + dataBaseObject.quoteName("state_id");
        query += ", " + dataBaseObject.quoteName("postcode") + ", " + dataBaseObject.quoteName("last_visit") + ", " + dataBaseObject.quoteName("geolocation_country_id");
        query += ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd");
        query += ") VALUES (" + this.laboratory_id + ", " + this.laboratory_group_id + ", " + dataBaseObject.quote(this.secure_key) + ", " + dataBaseObject.quote(this.note);
        query += ", " + this.default_group_id + ", " + this.language_id + ", " + dataBaseObject.quote(this.title) + ", " + dataBaseObject.quote(this.lastname);
        query += ", " + dataBaseObject.quote(this.firstname)  + ", " + dataBaseObject.quote(birthdayString) + ", " + dataBaseObject.quote(this.email) + ", ";
        query += (this.optin ? 1 : 0 ) + ", " + (this.is_guest ? 1 : 0) + dataBaseObject.quote(this.website) + dataBaseObject.quote(this.company) + ", ";
        query += dataBaseObject.quote(this.siret) + ", " + dataBaseObject.quote(this.ape) + ", " + (this.news_letter ? 1 : 0) + ", " + (this.published ? 1 : 0);
        query += ", " + (this.deleted ? 1 : 0) + ", " + this.state_id + ", " + dataBaseObject.quote(this.postcode) + dataBaseObject.quote(dateAdded) + ", ";
        query += this.geolocation_country_id + ", " + dataBaseObject.quote(dateAdded) + ", " + dataBaseObject.quoteName(dateAdded) + ")";

        dataBaseObject.setQuery(query);
        dataBaseObject.query(true);
        this.customer_id = dataBaseObject.getGeneratedKey();

        this.updateGroup(this.group_box);
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
        for(Integer groupId : groups) {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();  //$row = array('id_customer' => (int)this.id, 'id_group' => (int)$group);
            }
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

    public void update(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_customer") + " SET " + dataBaseObject.quoteName("lab_id") + " = ";
        query += this.laboratory_id + ", " + dataBaseObject.quoteName("lab_group_id") + " = " + this.laboratory_group_id + ", ";
        query += dataBaseObject.quoteName("secure_key") + " = " + dataBaseObject.quote(this.secure_key) + ", " + dataBaseObject.quoteName("note");
        query += " = " + dataBaseObject.quote(this.note) + ", " + dataBaseObject.quoteName("default_group_id") + " = " + this.default_group_id;
        query += ", " + dataBaseObject.quoteName("title") + " = " + dataBaseObject.quote(this.title) + ", " + dataBaseObject.quoteName("lastname");
        query +=  " = " + dataBaseObject.quote(this.lastname) + ", " + dataBaseObject.quoteName("firstname") + " = " + dataBaseObject.quote(this.firstname);
        query += ", " + dataBaseObject.quoteName("birthday") + " = " + dataBaseObject.quote(this.birthday) + ", " + dataBaseObject.quoteName("email");
        query += " = " + dataBaseObject.quote(this.email) + ", " + dataBaseObject.quoteName("optin") + " = " + ( this.optin ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("is_guest") + " = " + (this.is_guest ? 1 : 0) + ", " + dataBaseObject.quoteName("website") + " = ";
        query += dataBaseObject.quote(this.website) + ", " + dataBaseObject.quoteName("company") + " = " + dataBaseObject.quote(company) + ", ";
        query += dataBaseObject.quoteName("siret") + " = " + dataBaseObject.quote(this.siret) + ", " + dataBaseObject.quoteName("ape") + " = ";
        query += dataBaseObject.quote(this.ape) + ", " + dataBaseObject.quoteName("newsletter") + " = " + (this.news_letter ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("published") + " = " + (this.published ? 1 : 0) + ", " + dataBaseObject.quoteName("deleted") + " = " ;
        query += (this.deleted ? 1 : 0) + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

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
}
