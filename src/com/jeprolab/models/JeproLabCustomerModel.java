package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.controllers.JeproLabCategoryController;
import com.jeprolab.controllers.JeproLabCustomerController;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JeproLabCustomerModel  extends JeproLabModel{
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
            if (!JeproLabCustomerController.isStored(cacheKey)) {
                if(dataBaseObject == null) {
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customer") + " AS customer ";

                /** Get customer lab information  ** /
                if (JeproLabLaboratoryModel.isTableAssociated("customer")) {
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_customer_lab") + " AS customer_lab ON (customer_lab.";
                    query += dataBaseObject.quoteName("customer_id") + " = customer." + dataBaseObject.quoteName("customer_id") + " AND customer_lab.";
                    query += dataBaseObject.quoteName("lab_id") + " = " +
                } */

                query += " WHERE customer." + dataBaseObject.quoteName("customer_id") + " = " + customerId;
                dataBaseObject.setQuery(query);
                //dataBaseObject.loadObject();
                ResultSet customerSet = dataBaseObject.loadObject();
                if(customerSet != null){
                    try{
                        if(customerSet.next()){
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
                            //customer.birthday = JeproLabTools.getDate(customersSet.getString("birthday"));
                            this.optin = customerSet.getInt("optin") > 0;
                            this.is_guest = customerSet.getInt("is_guest") == 2;
                            this.website = customerSet.getString("website");
                            this.company = customerSet.getString("company");
                            this.siret = customerSet.getString("siret");
                            this.ape = customerSet.getString("ape");
                            this.published = customerSet.getInt("published") > 0;
                            /*customer.state_id = customersSet.getInt("state_id");
                            customer.postcode = customersSet.getString("postcode");
                            /*customer.geolocation_country_id = customersSet.;
                            customer.date_add = JeproLabTools.getDate(customersSet.getString("date_add"));
                            customer.date_upd = JeproLabTools.getDate(customersSet.getString("date_upd")); */
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
                //customer.birthday = JeproLabTools.getDate(customersSet.getString("birthday"));
                this.optin =customer.optin;
                this.is_guest = customer.is_guest;
                this.website = customer.website;
                this.company = customer.company;
                this.siret = customer.siret;
                this.ape = customer.ape;
                this.published = customer.published;
                    /*customer.state_id = customersSet.getInt("state_id");
                    customer.postcode = customersSet.getString("postcode");
                    /*customer.geolocation_country_id = customersSet.;
                    customer.date_add = JeproLabTools.getDate(customersSet.getString("date_add"));
                    customer.date_upd = JeproLabTools.getDate(customersSet.getString("date_upd")); */
            }
        }
        this.default_group_id = JeproLabSettingModel.getIntValue("customer_group");
    }

    public boolean add(){
        this.laboratory_id = this.laboratory_id > 0 ? this.laboratory_id : JeproLabContext.getContext().laboratory.laboratory_id;
        this.laboratory_group_id = (this.laboratory_group_id > 0) ? this.laboratory_group_id : JeproLabContext.getContext().laboratory.laboratory_group_id;
        this.language_id = (this.language_id > 0) ? this.language_id : JeproLabContext.getContext().language.language_id;
        this.birthday = ((this.years == 0) ? this.birthday : JeproLabTools.getDate(this.years + "-" + this.months + "-" + this.days));
        //this.secure_key = JeproLabTools.md5(JeproLabTools.uniqid(Math.random(), true));
        //this.last_passwd_gen = JeproLabTools.getDate("Y-m-d H:i:s", strtotime("-" + JeproLabSettingModel.getStringValue("password_front_time") + "minutes"));

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
            return false;
        }

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_customer") + "(" + dataBaseObject.quoteName("");
        //boolean success = parent::add($autodate, $null_values);
        this.updateGroup(this.group_box);
        return true;
    }

    public boolean update(){
        this.birthday = ((this.years == 0) ? this.birthday : JeproLabTools.getDate(this.years + "-" + this.months + "-" + this.days));

        if (this.news_letter && !JeproLabTools.isDate(this.news_letter_date_add)) {
            this.news_letter_date_add = JeproLabTools.getDate("Y-m-d H:i:s");
        }
        this.updateGroup(this.group_box);


        if (this.deleted) {
            List<JeproLabAddressModel> addresses = this.getAddresses(JeproLabSettingModel.getIntValue("default_lang"));
            addresses.forEach(com.jeprolab.models.JeproLabAddressModel::delete);
        }

        //return parent::update(true);
        return true;
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

        ResultSet cartSet = dataBaseObject.loadObject();
        if (cartSet != null) {
            try {
                int cartId;
                while (cartSet.next()) {
                    cartId = cartSet.getInt("cart_id");
                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart") + " WHERE " + dataBaseObject.quoteName("cart_id") + " = " + cartId;
                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);

                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart_product") + " WHERE cart_id = " + cartId;
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

        ResultSet threadIdsSet = dataBaseObject.loadObject();
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
        //JeproLabCartRuleModel.deleteByCustomerId(this.customer_id);
        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customer");
        query += " WHERE " + dataBaseObject.quoteName("customer_id") + " = " + this.customer_id;

        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);
    }

    /*
     * Return customers list
     *
     * @param null|bool $only_active Returns only active customers when true
     * @return array Customers
     * /
    public static function getCustomers($only_active = null)
    {
        $sql = 'SELECT `id_customer`, `email`, `firstname`, `lastname`
        FROM `'._DB_PREFIX_.'customer`
        WHERE 1 '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).
        ($only_active ? ' AND `active` = 1' : '').'
        ORDER BY `id_customer` ASC';
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
    }

    /**
     * Return customer instance from its e-mail (optionnaly check password)
     *
     * @param string $email e-mail
     * @param string $passwd Password is also checked if specified
     * @return Customer instance
     * /
    public function getByEmail($email, $passwd = null, $ignore_guest = true)
    {
        if (!Validate::isEmail($email) || ($passwd && !Validate::isPasswd($passwd))) {
        die(Tools::displayError());
    }

        $result = Db::getInstance()->getRow('
            SELECT *
                    FROM `'._DB_PREFIX_.'customer`
        WHERE `email` = \''.pSQL($email).'\'
        '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).'
        '.(isset($passwd) ? 'AND `passwd` = \''.pSQL(Tools::encrypt($passwd)).'\'' : '').'
        AND `deleted` = 0
        '.($ignore_guest ? ' AND `is_guest` = 0' : ''));

        if (!$result) {
            return false;
        }
        this.id = $result['id_customer'];
        foreach ($result as $key => $value) {
        if (property_exists($this, $key)) {
            this.{$key} = $value;
        }
    }
        return $this;
    }

    /**
     * Retrieve customers by email address
     *
     * @param email customer email address
     * @return List
     */
    public static List<JeproLabCustomerModel> getCustomersByEmail(String email){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer") + " WHERE ";
        query += staticDataBaseObject.quoteName("email") + " = " + staticDataBaseObject.quote(email);
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);

        staticDataBaseObject.setQuery(query);
        ResultSet customersSet = staticDataBaseObject.loadObject();

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
                    customer.birthday = JeproLabTools.getDate(customersSet.getString("birthday"));
                    customer.optin = customersSet.getInt("optin") > 0;
                    customer.is_guest = customersSet.getInt("is_guest") == 2;
                    customer.website = customersSet.getString("website");
                    customer.company = customersSet.getString("company");
                    customer.siret = customersSet.getString("siret");
                    customer.ape = customersSet.getString("ape");
                    customer.published = customersSet.getInt("published") > 0;
                    customer.state_id = customersSet.getInt("state_id");
                    customer.postcode = customersSet.getString("postcode");
                    //customer.geolocation_country_id = customersSet.;
                    customer.date_add = JeproLabTools.getDate(customersSet.getString("date_add"));
                    customer.date_upd = JeproLabTools.getDate(customersSet.getString("date_upd"));
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
        ResultSet customersSet = staticDataBaseObject.loadObject();

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

    /**
     * Check id the customer is active or not
     *
     * @return bool customer validity
     */
    public static boolean isBanned(int customerId){
        if (customerId <= 0) {
            return true;
        }
        String cacheKey = "jeprolab_customer_isBanned_" + customerId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("customer_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND " + staticDataBaseObject.quoteName("published");
            query += " = 1 AND " + staticDataBaseObject.quoteName("deleted") + " = 0 ";

            staticDataBaseObject.setQuery(query);
            boolean result = !((int)staticDataBaseObject.loadValue("customer_id") > 0);
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Check if e-mail is already registered in database
     *
     * @param email e-mail
     * @return Customer ID if found, false otherwise
     */
    public static int customerExists(String email){
        return customerExists(email, true);
    }

    /**
     * Check if e-mail is already registered in database
     *
     * @param email e-mail
     * @return Customer ID if found, false otherwise
     */
    public static boolean doesCustomerExist(String email){
        return (customerExists(email, true) > 0);
    }

    /*
     * Check if e-mail is already registered in database
     *
     * @param email e-mail
     * @param ignoreGuest boolean
     * @return Customer ID if found, false otherwise
     * /
    public static boolean customerExists(String email, boolean ignoreGuest){
        return (customerExists(email, true) > 0);
    }

    /**
     * Check if e-mail is already registered in database
     *
     * @param email e-mail
     * @param ignoreGuest boolean, to exclude guest customer
     * @return Customer ID if found, false otherwise
     */
    public static int customerExists(String email, boolean ignoreGuest){
        if (!JeproLabTools.isEmail(email)) {
            /*if (defined('_PS_MODE_DEV_') && _PS_MODE_DEV_) {
                die(Tools::displayError('Invalid email'));
            }*/
            return 0;
        }

        if (staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("customer_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer");
        query += " WHERE " + staticDataBaseObject.quoteName("email") + " = " + staticDataBaseObject.quote(email) ;
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER);
        query += (ignoreGuest ? " AND " + staticDataBaseObject.quoteName("is_guest") + " = 0 " : "");

        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("customer_id");
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
        if (staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (!JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)) {
            String query = "SELECT " + staticDataBaseObject.quoteName("address_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND " + staticDataBaseObject.quoteName("address_id");
            query += " = " + addressId + " AND "  + staticDataBaseObject.quoteName("deleted") + " = 0 ";

            staticDataBaseObject.setQuery(query);

            JeproLabCustomerModel._customerHasAddress.put(cacheKey, staticDataBaseObject.loadValue("address_id") > 0);

        }
        return JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey);
    }

    public static void resetAddressCache(int customerId, int addressId){
        String cacheKey = customerId + "_" + addressId;
        if (JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)) {
            JeproLabCustomerModel._customerHasAddress.remove(cacheKey);
        }
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
            ResultSet resultSet = dataBaseObject.loadObject();
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

    /*
     * Count the number of addresses for a customer
     *
     * @param int $id_customer Customer ID
     * @return int Number of addresses
     * /
    public static function getAddressesTotalByCustomerId(int customerId)
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT COUNT(`id_address`)
            FROM `'._DB_PREFIX_.'address`
        WHERE `id_customer` = '.(int)$id_customer.'
        AND `deleted` = 0'
        );
    }

    /**
     * Check if customer password is the right one
     *
     * @param string $passwd Password
     * @return bool result
     * /
    public static function checkPassword($id_customer, $passwd)
    {
        if (!Validate::isUnsignedId($id_customer) || !Validate::isMd5($passwd)) {
        die(Tools::displayError());
    }
        $cache_id = 'Customer::checkPassword'.(int)$id_customer.'-'.$passwd;
        if (!Cache::isStored($cache_id)) {
        $result = (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                SELECT `id_customer`
                FROM `'._DB_PREFIX_.'customer`
        WHERE `id_customer` = '.(int)$id_customer.'
        AND `passwd` = \''.pSQL($passwd).'\'');
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Light back office search for customers
     *
     * @param string $query Searched string
     * @param null|int $limit Limit query results
     * @return array|false|mysqli_result|null|PDOStatement|resource Corresponding customers
     * @throws PrestaShopDatabaseException
     * /
    public static function searchByName($query, $limit = null)
    {
        $sql_base = 'SELECT *
        FROM `'._DB_PREFIX_.'customer`';
        $sql = '('.$sql_base.' WHERE `email` LIKE \'%'.pSQL($query).'%\' '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).')';
        $sql .= ' UNION ('.$sql_base.' WHERE `id_customer` = '.(int)$query.' '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).')';
        $sql .= ' UNION ('.$sql_base.' WHERE `lastname` LIKE \'%'.pSQL($query).'%\' '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).')';
        $sql .= ' UNION ('.$sql_base.' WHERE `firstname` LIKE \'%'.pSQL($query).'%\' '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).')';

        if ($limit) {
            $sql .= ' LIMIT 0, '.(int)$limit;
        }

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
    }


    /**
     * Search for customers by ip address
     *
     * @param string $ip Searched string
     * /
    public static function searchByIp($ip)
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT DISTINCT c.*
                    FROM `'._DB_PREFIX_.'customer` c
        LEFT JOIN `'._DB_PREFIX_.'guest` g ON g.id_customer = c.id_customer
        LEFT JOIN `'._DB_PREFIX_.'connections` co ON g.id_guest = co.id_guest
        WHERE co.`ip_address` = \''.(int)ip2long(trim($ip)).'\'');
    }

    /**
     * Return several useful statistics about customer
     *
     * @return array Stats
     * /
    public function getStats()
    {
        $result = Db::getInstance()->getRow('
            SELECT COUNT(`id_order`) AS nb_orders, SUM(`total_paid` / o.`conversion_rate`) AS total_orders
        FROM `'._DB_PREFIX_.'orders` o
        WHERE o.`id_customer` = '.(int)this.id.'
        AND o.valid = 1');

        $result2 = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT MAX(c.`date_add`) AS last_visit
            FROM `'._DB_PREFIX_.'guest` g
        LEFT JOIN `'._DB_PREFIX_.'connections` c ON c.id_guest = g.id_guest
        WHERE g.`id_customer` = '.(int)this.id);

        $result3 = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT (YEAR(CURRENT_DATE)-YEAR(c.`birthday`)) - (RIGHT(CURRENT_DATE, 5)<RIGHT(c.`birthday`, 5)) AS age
            FROM `'._DB_PREFIX_.'customer` c
        WHERE c.`id_customer` = '.(int)this.id);

        $result['last_visit'] = $result2['last_visit'];
        $result['age'] = ($result3['age'] != date('Y') ? $result3['age'] : '--');
        return $result;
    }

    public function getLastEmails()
    {
        if (!this.id) {
            return array();
        }
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT m.*, l.name as language
            FROM `'._DB_PREFIX_.'mail` m
        LEFT JOIN `'._DB_PREFIX_.'lang` l ON m.id_lang = l.id_lang
        WHERE `recipient` = "'.pSQL(this.email).'"
        ORDER BY m.date_add DESC
        LIMIT 10');
    }

    public function getLastConnections()
    {
        if (!this.id) {
            return array();
        }
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT c.id_connections, c.date_add, COUNT(cp.id_page) AS pages, TIMEDIFF(MAX(cp.time_end), c.date_add) as time, http_referer,INET_NTOA(ip_address) as ipaddress
            FROM `'._DB_PREFIX_.'guest` g
        LEFT JOIN `'._DB_PREFIX_.'connections` c ON c.id_guest = g.id_guest
        LEFT JOIN `'._DB_PREFIX_.'connections_page` cp ON c.id_connections = cp.id_connections
        WHERE g.`id_customer` = '.(int)this.id.'
        GROUP BY c.`id_connections`
        ORDER BY c.date_add DESC
        LIMIT 10');
    }

    /*
    * Specify if a customer already in base
    *
    * @param $id_customer Customer id
    * @return bool
    */
    /* DEPRECATED * /
    public function customerIdExists($id_customer)
    {
        return Customer::customerIdExistsStatic((int)$id_customer);
    }

    public static function customerIdExistsStatic($id_customer)
    {
        $cache_id = 'Customer::customerIdExistsStatic'.(int)$id_customer;
        if (!Cache::isStored($cache_id)) {
        $result = (int)Db::getInstance()->getValue('
                SELECT `id_customer`
                FROM '._DB_PREFIX_.'customer c
        WHERE c.`id_customer` = '.(int)$id_customer);
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Update customer groups associated to the object
     *
     * @param array $list groups
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
            ResultSet groupSet = staticDataBaseObject.loadObject();
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

    /**
     * @deprecated since 1.5
     * /
    public function isUsed()
    {
        Tools::displayAsDeprecated();
        return false;
    }

    public function getBoughtProducts()
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT * FROM `'._DB_PREFIX_.'orders` o
        LEFT JOIN `'._DB_PREFIX_.'order_detail` od ON o.id_order = od.id_order
        WHERE o.valid = 1 AND o.`id_customer` = '.(int)this.id);
    }
*/
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

    public static String getCompanyByCustomerId(int customerId) {
        return "";
    }

    public static String getEmailByCustomerId(int customerId) {
        return "";
    }

    public static String getNameByCustomerId(int customerId) {
        return  "";
    }
/*
    public static function getCurrentCountry($id_customer, Cart $cart = null)
    {
        if (!$cart) {
            $cart = Context::getContext()->cart;
        }
        if (!$cart || !$cart->{Configuration::get('PS_TAX_ADDRESS_TYPE')}) {
            $id_address = (int)Db::getInstance()->getValue('
                    SELECT `id_address`
                    FROM `'._DB_PREFIX_.'address`
            WHERE `id_customer` = '.(int)$id_customer.'
            AND `deleted` = 0 ORDER BY `id_address`'
            );
        } else {
            $id_address = $cart->{Configuration::get('PS_TAX_ADDRESS_TYPE')};
        }
        $ids = Address::getCountryAndState($id_address);
        return (int)$ids['id_country'] ? $ids['id_country'] : Configuration::get('PS_COUNTRY_DEFAULT');
    }

    public function toggleStatus()
    {
        parent::toggleStatus();

        /* Change status to active/inactive * /
        return Db::getInstance()->execute('
            UPDATE `'._DB_PREFIX_.bqSQL(this.def['table']).'`
        SET `date_upd` = NOW()
        WHERE `'.bqSQL(this.def['primary']).'` = '.(int)this.id);
    }


    public function isGuest()
    {
        return (bool)this.is_guest;
    }

    public function transformToCustomer($id_lang, $password = null)
    {
        if (!this.isGuest()) {
            return false;
        }
        if (empty($password)) {
            $password = Tools::passwdGen(8, 'RANDOM');
        }
        if (!Validate::isPasswd($password)) {
        return false;
    }

        this.is_guest = 0;
        this.passwd = Tools::encrypt($password);
        this.cleanGroups();
        this.addGroups(array(Configuration::get('PS_CUSTOMER_GROUP'))); // add default customer group
        if (this.update()) {
            $vars = array(
                    '{firstname}' => this.firstname,
                    '{lastname}' => this.lastname,
                    '{email}' => this.email,
                    '{passwd}' => $password
            );

            Mail::Send(
                    (int)$id_lang,
                    'guest_to_customer',
                    Mail::l('Your guest account has been transformed into a customer account', (int)$id_lang),
            $vars,
                    this.email,
                    this.firstname.' '.this.lastname,
                    null,
                    null,
                    null,
                    null,
                    _PS_MAIL_DIR_,
                    false,
                    (int)this.id_shop
            );
            return true;
        }
        return false;
    }

    public function setWsPasswd($passwd)
    {
        if (this.id == 0 || this.passwd != $passwd) {
            this.passwd = Tools::encrypt($passwd);
        }
        return true;
    }

    /**
     * Check customer information and return customer validity
     *
     * @since 1.5.0
     * @param bool $with_guest
     * @return bool customer validity
     * /
    public function isLogged($with_guest = false)
    {
        if (!$with_guest && this.is_guest == 1) {
            return false;
        }

        /* Customer is valid only if it can be load and if object password is the same as database one * /
        return (this.logged == 1 && this.id && Validate::isUnsignedId(this.id) && Customer::checkPassword(this.id, this.passwd));
    }

    /**
     * Logout
     *
     * @since 1.5.0
     * /
    public function logout()
    {
        Hook::exec('actionCustomerLogoutBefore', array('customer' => $this));

        if (isset(Context::getContext()->cookie)) {
        Context::getContext()->cookie->logout();
    }

        this.logged = 0;

        Hook::exec('actionCustomerLogoutAfter', array('customer' => $this));
    }

    /**
     * Soft logout, delete everything links to the customer
     * but leave there affiliate's informations
     *
     * @since 1.5.0
     * /
    public function mylogout()
    {
        Hook::exec('actionCustomerLogoutBefore', array('customer' => $this));

        if (isset(Context::getContext()->cookie)) {
        Context::getContext()->cookie->mylogout();
    }

        this.logged = 0;

        Hook::exec('actionCustomerLogoutAfter', array('customer' => $this));
    }

    public function getLastCart($with_order = true)
    {
        $carts = Cart::getCustomerCarts((int)this.id, $with_order);
        if (!count($carts)) {
            return false;
        }
        $cart = array_shift($carts);
        $cart = new Cart((int)$cart['id_cart']);
        return ($cart->nbProducts() === 0 ? (int)$cart->id : false);
    }

    public function getOutstanding()
    {
        $query = new DbQuery();
        $query->select('SUM(oi.total_paid_tax_incl)');
        $query->from('order_invoice', 'oi');
        $query->leftJoin('orders', 'o', 'oi.id_order = o.id_order');
        $query->groupBy('o.id_customer');
        $query->where('o.id_customer = '.(int)this.id);
        $total_paid = (float)Db::getInstance()->getValue($query->build());

        $query = new DbQuery();
        $query->select('SUM(op.amount)');
        $query->from('order_payment', 'op');
        $query->leftJoin('order_invoice_payment', 'oip', 'op.id_order_payment = oip.id_order_payment');
        $query->leftJoin('orders', 'o', 'oip.id_order = o.id_order');
        $query->groupBy('o.id_customer');
        $query->where('o.id_customer = '.(int)this.id);
        $total_rest = (float)Db::getInstance()->getValue($query->build());

        return $total_paid - $total_rest;
    }

    public function getWsGroups()
    {
        return Db::getInstance()->executeS('
            SELECT cg.`id_group` as id
            FROM '._DB_PREFIX_.'customer_group cg
        '.Shop::addSqlAssociation('group', 'cg').'
        WHERE cg.`id_customer` = '.(int)this.id
        );
    }

    public function setWsGroups($result)
    {
        $groups = array();
        foreach ($result as $row) {
        $groups[] = $row['id'];
    }
        this.cleanGroups();
        this.addGroups($groups);
        return true;
    }

    /**
     * @see ObjectModel::getWebserviceObjectList()
     * /
    public function getWebserviceObjectList($sql_join, $sql_filter, $sql_sort, $sql_limit)
    {
        $sql_filter .= Shop::addSqlRestriction(Shop::SHARE_CUSTOMER, 'main');
        return parent::getWebserviceObjectList($sql_join, $sql_filter, $sql_sort, $sql_limit);
    } */

}