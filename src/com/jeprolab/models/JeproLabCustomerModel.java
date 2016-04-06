package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
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

    protected static int customer_group = 0;

    protected static Map<Integer, Integer> _defaultGroupId = new HashMap<>();
    protected static Map<Integer, Boolean> _customerHasAddress = new HashMap<>();

    public JeproLabCustomerModel(){
        this(0);
    }

    public JeproLabCustomerModel(int customerId){
        if(customerId > 0) {
            ResultSet results = null;
            String query = "";
            String cache_id = "jeprolab_customer_model_" + customerId + ((this.laboratory_id != 0) ? "_" + this.laboratory_id : "");
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
        this.default_group_id = JeproLabSettingModel.getIntValue("customer_group");
    }
/*
    public function add($autodate = true, $null_values = true)
    {
        $this->id_shop = ($this->id_shop) ? $this->id_shop : Context::getContext()->shop->id;
        $this->id_shop_group = ($this->id_shop_group) ? $this->id_shop_group : Context::getContext()->shop->id_shop_group;
        $this->id_lang = ($this->id_lang) ? $this->id_lang : Context::getContext()->language->id;
        $this->birthday = (empty($this->years) ? $this->birthday : (int)$this->years.'-'.(int)$this->months.'-'.(int)$this->days);
        $this->secure_key = md5(uniqid(rand(), true));
        $this->last_passwd_gen = date('Y-m-d H:i:s', strtotime('-'.Configuration::get('PS_PASSWD_TIME_FRONT').'minutes'));

        if ($this->newsletter && !Validate::isDate($this->newsletter_date_add)) {
        $this->newsletter_date_add = date('Y-m-d H:i:s');
    }

        if ($this->id_default_group == Configuration::get('PS_CUSTOMER_GROUP')) {
        if ($this->is_guest) {
            $this->id_default_group = (int)Configuration::get('PS_GUEST_GROUP');
        } else {
            $this->id_default_group = (int)Configuration::get('PS_CUSTOMER_GROUP');
        }
    }

        /* Can't create a guest customer, if this feature is disabled * /
        if ($this->is_guest && !Configuration::get('PS_GUEST_CHECKOUT_ENABLED')) {
        return false;
    }
        $success = parent::add($autodate, $null_values);
        $this->updateGroup($this->groupBox);
        return $success;
    }

    public function update($nullValues = false)
    {
        $this->birthday = (empty($this->years) ? $this->birthday : (int)$this->years.'-'.(int)$this->months.'-'.(int)$this->days);

        if ($this->newsletter && !Validate::isDate($this->newsletter_date_add)) {
        $this->newsletter_date_add = date('Y-m-d H:i:s');
    }
        if (isset(Context::getContext()->controller) && Context::getContext()->controller->controller_type == 'admin') {
        $this->updateGroup($this->groupBox);
    }

        if ($this->deleted) {
            $addresses = $this->getAddresses((int)Configuration::get('PS_LANG_DEFAULT'));
            foreach ($addresses as $address) {
                $obj = new Address((int)$address['id_address']);
                $obj->delete();
            }
        }

        return parent::update(true);
    }

    public function delete()
    {
        if (!count(Order::getCustomerOrders((int)$this->id))) {
        $addresses = $this->getAddresses((int)Configuration::get('PS_LANG_DEFAULT'));
        foreach ($addresses as $address) {
            $obj = new Address((int)$address['id_address']);
            $obj->delete();
        }
    }
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'customer_group` WHERE `id_customer` = '.(int)$this->id);
        Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'message WHERE id_customer='.(int)$this->id);
        Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'specific_price WHERE id_customer='.(int)$this->id);
        Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'compare WHERE id_customer='.(int)$this->id);

        $carts = Db::getInstance()->executes('SELECT id_cart
            FROM '._DB_PREFIX_.'cart
        WHERE id_customer='.(int)$this->id);
        if ($carts) {
            foreach ($carts as $cart) {
                Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'cart WHERE id_cart='.(int)$cart['id_cart']);
                Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'cart_product WHERE id_cart='.(int)$cart['id_cart']);
            }
        }

        $cts = Db::getInstance()->executes('SELECT id_customer_thread
            FROM '._DB_PREFIX_.'customer_thread
        WHERE id_customer='.(int)$this->id);
        if ($cts) {
            foreach ($cts as $ct) {
                Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'customer_thread WHERE id_customer_thread='.(int)$ct['id_customer_thread']);
                Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'customer_message WHERE id_customer_thread='.(int)$ct['id_customer_thread']);
            }
        }

        CartRule::deleteByIdCustomer((int)$this->id);
        return parent::delete();
    }

    /**
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
        $this->id = $result['id_customer'];
        foreach ($result as $key => $value) {
        if (property_exists($this, $key)) {
            $this->{$key} = $value;
        }
    }
        return $this;
    }

    /**
     * Retrieve customers by email address
     *
     * @param $email
     * @return array
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
                    customer.note = customersSet.;
                    customer.default_group_id = customersSet.getInt("default_group_id");
                    customer.language_id = customersSet.getInt("lang_id");
                    customer.title = customersSet.;
                    customer.lastname = customersSet.getString("lastname");
                    customer.firstname = customersSet.getString("firstname");
                    customer.birthday = JeproLabTools.getDate(customersSet.getString("birthday"));
                    customer.optin = customersSet.;
                    customer.is_guest = customersSet.;
                    customer.website = customersSet.getString("website");
                    customer.company = customersSet.getString("company");
                    customer.siret = customersSet.getString("siret");
                    customer.ape = customersSet.;
                    customer.published = customersSet.getInt("published") > 0;
                    customer.state_id = customersSet.getInt("state_id");
                    customer.postcode = customersSet.getString("postcode");
                    customer.geolocation_country_id = customersSet.;
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
     * Check id the customer is active or not
     *
     * @return bool customer validity
     * /
    public static function isBanned($id_customer)
    {
        if (!Validate::isUnsignedId($id_customer)) {
        return true;
    }
        $cache_id = 'Customer::isBanned_'.(int)$id_customer;
        if (!Cache::isStored($cache_id)) {
        $result = (bool)!Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
                SELECT `id_customer`
                FROM `'._DB_PREFIX_.'customer`
        WHERE `id_customer` = \''.(int)$id_customer.'\'
        AND active = 1
        AND `deleted` = 0');
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Check if e-mail is already registered in database
     *
     * @param string $email e-mail
     * @param $return_id boolean
     * @param $ignore_guest boolean, to exclude guest customer
     * @return Customer ID if found, false otherwise
     * /
    public static function customerExists($email, $return_id = false, $ignore_guest = true)
    {
        if (!Validate::isEmail($email)) {
        if (defined('_PS_MODE_DEV_') && _PS_MODE_DEV_) {
            die(Tools::displayError('Invalid email'));
        }
        return false;
    }

        $result = Db::getInstance()->getValue('
            SELECT `id_customer`
            FROM `'._DB_PREFIX_.'customer`
        WHERE `email` = \''.pSQL($email).'\'
        '.Shop::addSqlRestriction(Shop::SHARE_CUSTOMER).'
        '.($ignore_guest ? ' AND `is_guest` = 0' : ''));
        return ($return_id ? (int)$result : (bool)$result);
    }

    /**
     * Check if an address is owned by a customer
     *
     * @param int $id_customer Customer ID
     * @param int $id_address Address ID
     * @return bool result
     * /
    public static function customerHasAddress($id_customer, $id_address)
    {
        $key = (int)$id_customer.'-'.(int)$id_address;
        if (!array_key_exists($key, self::$_customerHasAddress)) {
            self::$_customerHasAddress[$key] = (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                    SELECT `id_address`
                    FROM `'._DB_PREFIX_.'address`
            WHERE `id_customer` = '.(int)$id_customer.'
            AND `id_address` = '.(int)$id_address.'
            AND `deleted` = 0');
        }
        return self::$_customerHasAddress[$key];
    } */

    public static void resetAddressCache(int customerId, int addressId){
        String cacheKey = customerId + "_" + addressId;
        if (JeproLabCustomerModel._customerHasAddress.containsKey(cacheKey)) {
            JeproLabCustomerModel._customerHasAddress.remove(cacheKey);
        }
    }

    /*
     * Return customer addresses
     *
     * @param int $id_lang Language ID
     * @return array Addresses
     * /
    public function getAddresses($id_lang)
    {
        $share_order = (bool)Context::getContext()->shop->getGroup()->share_order;
        $cache_id = 'Customer::getAddresses'.(int)$this->id.'-'.(int)$id_lang.'-'.$share_order;
        if (!Cache::isStored($cache_id)) {
        $sql = 'SELECT DISTINCT a.*, cl.`name` AS country, s.name AS state, s.iso_code AS state_iso
        FROM `'._DB_PREFIX_.'address` a
        LEFT JOIN `'._DB_PREFIX_.'country` c ON (a.`id_country` = c.`id_country`)
        LEFT JOIN `'._DB_PREFIX_.'country_lang` cl ON (c.`id_country` = cl.`id_country`)
        LEFT JOIN `'._DB_PREFIX_.'state` s ON (s.`id_state` = a.`id_state`)
        '.($share_order ? '' : Shop::addSqlAssociation('country', 'c')).'
        WHERE `id_lang` = '.(int)$id_lang.' AND `id_customer` = '.(int)$this->id.' AND a.`deleted` = 0';

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Count the number of addresses for a customer
     *
     * @param int $id_customer Customer ID
     * @return int Number of addresses
     * /
    public static function getAddressesTotalById($id_customer)
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
        WHERE o.`id_customer` = '.(int)$this->id.'
        AND o.valid = 1');

        $result2 = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT MAX(c.`date_add`) AS last_visit
            FROM `'._DB_PREFIX_.'guest` g
        LEFT JOIN `'._DB_PREFIX_.'connections` c ON c.id_guest = g.id_guest
        WHERE g.`id_customer` = '.(int)$this->id);

        $result3 = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT (YEAR(CURRENT_DATE)-YEAR(c.`birthday`)) - (RIGHT(CURRENT_DATE, 5)<RIGHT(c.`birthday`, 5)) AS age
            FROM `'._DB_PREFIX_.'customer` c
        WHERE c.`id_customer` = '.(int)$this->id);

        $result['last_visit'] = $result2['last_visit'];
        $result['age'] = ($result3['age'] != date('Y') ? $result3['age'] : '--');
        return $result;
    }

    public function getLastEmails()
    {
        if (!$this->id) {
            return array();
        }
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT m.*, l.name as language
            FROM `'._DB_PREFIX_.'mail` m
        LEFT JOIN `'._DB_PREFIX_.'lang` l ON m.id_lang = l.id_lang
        WHERE `recipient` = "'.pSQL($this->email).'"
        ORDER BY m.date_add DESC
        LIMIT 10');
    }

    public function getLastConnections()
    {
        if (!$this->id) {
            return array();
        }
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT c.id_connections, c.date_add, COUNT(cp.id_page) AS pages, TIMEDIFF(MAX(cp.time_end), c.date_add) as time, http_referer,INET_NTOA(ip_address) as ipaddress
            FROM `'._DB_PREFIX_.'guest` g
        LEFT JOIN `'._DB_PREFIX_.'connections` c ON c.id_guest = g.id_guest
        LEFT JOIN `'._DB_PREFIX_.'connections_page` cp ON c.id_connections = cp.id_connections
        WHERE g.`id_customer` = '.(int)$this->id.'
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
     * /
    public function updateGroup($list)
    {
        if ($list && !empty($list)) {
            $this->cleanGroups();
            $this->addGroups($list);
        } else {
            $this->addGroups(array($this->id_default_group));
        }
    }

    public function cleanGroups()
    {
        return Db::getInstance()->delete('customer_group', 'id_customer = '.(int)$this->id);
    }

    public function addGroups($groups)
    {
        foreach ($groups as $group) {
        $row = array('id_customer' => (int)$this->id, 'id_group' => (int)$group);
        Db::getInstance()->insert('customer_group', $row, false, true, Db::INSERT_IGNORE);
    }
    }

    public static function getGroupsStatic($id_customer)
    {
        if (!Group::isFeatureActive()) {
        return array(Configuration::get('PS_CUSTOMER_GROUP'));
    }

        if ($id_customer == 0) {
            self::$_customer_groups[$id_customer] = array((int)Configuration::get('PS_UNIDENTIFIED_GROUP'));
        }

        if (!isset(self::$_customer_groups[$id_customer])) {
        self::$_customer_groups[$id_customer] = array();
        $result = Db::getInstance()->executeS('
                SELECT cg.`id_group`
                FROM '._DB_PREFIX_.'customer_group cg
        WHERE cg.`id_customer` = '.(int)$id_customer);
        foreach ($result as $group) {
            self::$_customer_groups[$id_customer][] = (int)$group['id_group'];
        }
    }
        return self::$_customer_groups[$id_customer];
    }

    public function getGroups()
    {
        return Customer::getGroupsStatic((int)$this->id);
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
        WHERE o.valid = 1 AND o.`id_customer` = '.(int)$this->id);
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
            UPDATE `'._DB_PREFIX_.bqSQL($this->def['table']).'`
        SET `date_upd` = NOW()
        WHERE `'.bqSQL($this->def['primary']).'` = '.(int)$this->id);
    }


    public function isGuest()
    {
        return (bool)$this->is_guest;
    }

    public function transformToCustomer($id_lang, $password = null)
    {
        if (!$this->isGuest()) {
            return false;
        }
        if (empty($password)) {
            $password = Tools::passwdGen(8, 'RANDOM');
        }
        if (!Validate::isPasswd($password)) {
        return false;
    }

        $this->is_guest = 0;
        $this->passwd = Tools::encrypt($password);
        $this->cleanGroups();
        $this->addGroups(array(Configuration::get('PS_CUSTOMER_GROUP'))); // add default customer group
        if ($this->update()) {
            $vars = array(
                    '{firstname}' => $this->firstname,
                    '{lastname}' => $this->lastname,
                    '{email}' => $this->email,
                    '{passwd}' => $password
            );

            Mail::Send(
                    (int)$id_lang,
                    'guest_to_customer',
                    Mail::l('Your guest account has been transformed into a customer account', (int)$id_lang),
            $vars,
                    $this->email,
                    $this->firstname.' '.$this->lastname,
                    null,
                    null,
                    null,
                    null,
                    _PS_MAIL_DIR_,
                    false,
                    (int)$this->id_shop
            );
            return true;
        }
        return false;
    }

    public function setWsPasswd($passwd)
    {
        if ($this->id == 0 || $this->passwd != $passwd) {
            $this->passwd = Tools::encrypt($passwd);
        }
        return true;
    }

    /**
     * Check customer informations and return customer validity
     *
     * @since 1.5.0
     * @param bool $with_guest
     * @return bool customer validity
     * /
    public function isLogged($with_guest = false)
    {
        if (!$with_guest && $this->is_guest == 1) {
            return false;
        }

        /* Customer is valid only if it can be load and if object password is the same as database one * /
        return ($this->logged == 1 && $this->id && Validate::isUnsignedId($this->id) && Customer::checkPassword($this->id, $this->passwd));
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

        $this->logged = 0;

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

        $this->logged = 0;

        Hook::exec('actionCustomerLogoutAfter', array('customer' => $this));
    }

    public function getLastCart($with_order = true)
    {
        $carts = Cart::getCustomerCarts((int)$this->id, $with_order);
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
        $query->where('o.id_customer = '.(int)$this->id);
        $total_paid = (float)Db::getInstance()->getValue($query->build());

        $query = new DbQuery();
        $query->select('SUM(op.amount)');
        $query->from('order_payment', 'op');
        $query->leftJoin('order_invoice_payment', 'oip', 'op.id_order_payment = oip.id_order_payment');
        $query->leftJoin('orders', 'o', 'oip.id_order = o.id_order');
        $query->groupBy('o.id_customer');
        $query->where('o.id_customer = '.(int)$this->id);
        $total_rest = (float)Db::getInstance()->getValue($query->build());

        return $total_paid - $total_rest;
    }

    public function getWsGroups()
    {
        return Db::getInstance()->executeS('
            SELECT cg.`id_group` as id
            FROM '._DB_PREFIX_.'customer_group cg
        '.Shop::addSqlAssociation('group', 'cg').'
        WHERE cg.`id_customer` = '.(int)$this->id
        );
    }

    public function setWsGroups($result)
    {
        $groups = array();
        foreach ($result as $row) {
        $groups[] = $row['id'];
    }
        $this->cleanGroups();
        $this->addGroups($groups);
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