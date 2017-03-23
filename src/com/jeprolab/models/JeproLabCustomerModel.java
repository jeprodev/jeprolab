package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.image.Image;
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

    public boolean allow_ads = false;

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
                //ataBaseObject.setQuery(query);
                ResultSet customerSet = dataBaseObject.loadObjectList(query);

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
                            JeproLabFactory.removeConnection(dataBaseObject);
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

            //dataBaseObject.setQuery(query);
            ResultSet customerGroupSet = dataBaseObject.loadObjectList(query);

            if(customerGroupSet != null){
                try{
                    while(customerGroupSet.next()){
                        JeproLabCustomerModel.current_customer_groups.add(customerGroupSet.getInt("group_id"));
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
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

            //dataBaseObject.setQuery(query);
            JeproLabCustomerModel._defaultGroupId.put(customerId, (int)dataBaseObject.loadValue(query, "default_group_id"));
            closeDataBaseConnection(dataBaseObject);
        }
        return JeproLabCustomerModel._defaultGroupId.get(customerId);
    }

    public static void resetAddressCache(int customerId, int addressId){
        String cacheKey = customerId + "_" + addressId;
        if (JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)) {
            JeproLabCustomerModel._customerHasAddress.remove(cacheKey);
        }
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
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();

        /*String query = "SELECT " + dataBaseObject.quoteName("customer_id") + ", " + dataBaseObject.quoteName("email");
        query += ", " + dataBaseObject.quoteName("firstname") + ", " + dataBaseObject.quoteName("lastname") + " FROM ";
        query += dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE 1 " + JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);
        query += (onlyActive ? " AND " + dataBaseObject.quoteName("published") + " = 1" : "") + " ORDER BY ";
        query += dataBaseObject.quoteName("customer_id") + " ASC";*/

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE 1 ";
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);
        query += (onlyActive ? " AND " + dataBaseObject.quoteName("published") + " = 1" : "") + " ORDER BY ";
        query += dataBaseObject.quoteName("customer_id") + " ASC";

        //dataBaseObject.setQuery(query);
        ResultSet customerSet = dataBaseObject.loadObjectList(query);

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
                    customer.date_add = customerSet.getDate("date_add");
                    customer.date_upd = customerSet.getDate("date_upd");
                    customers.add(customer);
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try{
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return  customers;
    }

    public void add(){
        this.laboratory_id = this.laboratory_id > 0 ? this.laboratory_id : JeproLabContext.getContext().laboratory.laboratory_id;
        this.laboratory_group_id = (this.laboratory_group_id > 0) ? this.laboratory_group_id : JeproLabContext.getContext().laboratory.laboratory_group_id;

        this.language_id = (this.language_id > 0) ? this.language_id : JeproLabContext.getContext().language.language_id;
        this.birthday = ((this.years == 0) ? this.birthday : (this.years + "-" + this.months + "-" + this.days));

        if(this.news_letter){
            this.news_letter_date_add = JeproLabTools.getDate();
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

        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, true);
        this.customer_id = dataBaseObject.getGeneratedKey();

        this.updateGroup(this.group_box);

        if (this.news_letter && !JeproLabTools.isDate(this.news_letter_date_add)) {
            this.news_letter_date_add = JeproLabTools.getDate();
        }
        closeDataBaseConnection(dataBaseObject);
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

        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
    }

    public void addGroups(List<Integer> groups){
        String query;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        for(Integer groupId : groups) {

            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_customer_group") + "(" + dataBaseObject.quoteName("customer_id");
            query += ", " + dataBaseObject.quoteName("group_id") + ") VALUES (" + this.customer_id + ", " + groupId + ")";
            //dataBaseObject.setQuery(query);
            dataBaseObject.query(query, false);
        }
        closeDataBaseConnection(dataBaseObject);
    }

    public void update(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(this.news_letter){
            this.news_letter_date_add = JeproLabTools.getDate();
        }

        if (this.news_letter && !JeproLabTools.isDate(this.news_letter_date_add)) {
            this.news_letter_date_add = JeproLabTools.getDate();
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

        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);

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
        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_message") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE customer_id = " + this.customer_id;
        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_compare") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);

        query = "SELECT cart_id FROM " + dataBaseObject.quoteName("#__jeprolab_cart") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;
        //dataBaseObject.setQuery(query);

        ResultSet cartSet = dataBaseObject.loadObjectList(query);
        if (cartSet != null) {
            try {
                int cartId;
                while (cartSet.next()) {
                    cartId = cartSet.getInt("cart_id");
                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart") + " WHERE " + dataBaseObject.quoteName("cart_id") + " = " + cartId;
                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);

                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart_product") + " WHERE cart_id = " + cartId;
                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);
                }
            } catch (SQLException ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            } finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                } catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }

        query = "SELECT customer_thread_id FROM " + dataBaseObject.quoteName("#__jeprolab_customer_thread") + " WHERE customer_id = " + this.customer_id;
        //dataBaseObject.setQuery(query);

        ResultSet threadIdsSet = dataBaseObject.loadObjectList(query);
        if(threadIdsSet != null) {
            try {
                int customerThreadId;
                while (threadIdsSet.next()) {
                    customerThreadId = threadIdsSet.getInt("customer_thread_id");
                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer_thread") + " WHERE customer_thread_id = " + customerThreadId;
                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);

                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer_message") + " WHERE customer_thread_id = " + customerThreadId;
                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);
                }
            } catch (SQLException ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            } finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                } catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        //JeproLabCartRuleModel.deleteByCustomerId(this.customer_id);
        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer");
        query += " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);
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
        if(!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT DISTINCT address.*, country_lang." + dataBaseObject.quoteName("name") + " AS country, state.name AS state_name,";
            query += " state.iso_code AS state_iso FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " AS address LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_country")  + " AS country ON (address." + dataBaseObject.quoteName("country_id");
            query += " = country." + dataBaseObject.quoteName("country_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lang");
            query += " AS country_lang ON (country." + dataBaseObject.quoteName("country_id") + " = country_lang." + dataBaseObject.quoteName("country_id");
            query += ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_state") + " AS state ON (state." + dataBaseObject.quoteName("state_id") ;
            query += " = address." + dataBaseObject.quoteName("state_id") + ") " + (shareRequest ? "" : JeproLabLaboratoryModel.addSqlAssociation("country"));
            query += " WHERE " + dataBaseObject.quoteName("lang_id") + " = " + langId + " AND " + dataBaseObject.quoteName("customer_id") + " = ";
            query += this.customer_id + " AND address." + dataBaseObject.quoteName("deleted") + " = 0";

            ResultSet resultSet = dataBaseObject.loadObjectList(query);
            List<JeproLabAddressModel> addressList = new ArrayList<>();

            if(resultSet != null){
                try {
                    JeproLabAddressModel address;
                    while(resultSet.next()){
                        address = new JeproLabAddressModel();
                        address.address_id = resultSet.getInt("address_id");
                        address.firstname = resultSet.getString("firstname");
                        address.lastname = resultSet.getString("lastname");
                        address.postcode = resultSet.getString("postcode");
                        address.city = resultSet.getString("city");
                        address.address1 = resultSet.getString("address1");
                        address.address2 = resultSet.getString("address2");
                        address.country = resultSet.getString("country");
                        addressList.add(address);
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            JeproLabCache.getInstance().store(cacheKey, addressList);
            return addressList;
        }
        return (List<JeproLabAddressModel>)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static String getNameByCustomerId(int customerId) {
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("firstname") + ", " + dataBaseObject.quoteName("lastname") + " FROM ";
        query += dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE " + dataBaseObject.quoteName("customer_id") + " = ";
        query += customerId;

        //dataBaseObject.setQuery(query);
        ResultSet customerSet = dataBaseObject.loadObjectList(query);
        if(customerSet != null){
            try{
                if(customerSet.next()){
                    return customerSet.getString("firstname") + " " + customerSet.getString("lastname").toUpperCase();
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try{
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return  "";
    }

    /**
     * Retrieve customers by company
     *
     * @param company customer company name
     * @return array
     */
    public static List<JeproLabCustomerModel> getCustomersByCompany(String company){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += dataBaseObject.quoteName("company") + " = " + dataBaseObject.quote(company);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        //dataBaseObject.setQuery(query);
        ResultSet customersSet = dataBaseObject.loadObjectList(query);

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
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return customers;
    }

    public static int getCustomerIdByEmail(String email){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT customer_id FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += dataBaseObject.quoteName("email") + " = " + dataBaseObject.quote(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        //dataBaseObject.setQuery(query);
        int customerId = (int)dataBaseObject.loadValue(query, "customer_id");
        closeDataBaseConnection(dataBaseObject);
        return customerId;
    }

    public static JeproLabCustomerModel searchCustomerByEmail(String email){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += dataBaseObject.quoteName("email") + " LIKE " + dataBaseObject.quote('%' + email + '%');

        ResultSet customerSet = dataBaseObject.loadObjectList(query);
        JeproLabCustomerModel customer = new JeproLabCustomerModel();
        if(customerSet != null){
            try{
                if(customerSet.next()){
                    customer.customer_id = customerSet.getInt("customer_id");
                    customer.firstname = customerSet.getString("firstname");
                    customer.lastname = customerSet.getString("lastname");
                }
            }catch(SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch(Exception ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return customer;
    }

    /**
     * Retrieve customers by email address
     *
     * @param email customer email address
     * @return List
     */
    public static JeproLabCustomerModel getCustomerByEmail(String email){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += dataBaseObject.quoteName("email") + " = " + dataBaseObject.quote(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        //dataBaseObject.setQuery(query);
        ResultSet customersSet = dataBaseObject.loadObjectList(query);
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
                    customer.allow_ads = customersSet.getInt("allow_ads") > 0;
                    customer.is_guest = customersSet.getInt("is_guest") == 2;
                    customer.website = customersSet.getString("website");
                    customer.company = customersSet.getString("company");
                    customer.siret = customersSet.getString("siret");
                    customer.ape = customersSet.getString("ape");
                    customer.published = customersSet.getInt("published") > 0;
                    //customer.state_id = customersSet.getInt("state_id");
                    //customer.postcode = customersSet.getString("postcode");
                    //customer.geolocation_country_id = customersSet.;
                    customer.date_add = customersSet.getDate("date_add");
                    customer.date_upd = customersSet.getDate("date_upd");
                    return customer;
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return null;
    }

    public Image getImage(){
        String filePath = "";
        return JeproLabTools.getImage(filePath);
    }
}
