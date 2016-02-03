package com.jeprolab.models;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAddressModel extends JeproLabModel {
    public int address_id;
    /** @var int Customer id which address belongs to */
    public int customer_id = 0;

    /** @var int Manufacturer id which address belongs to */
    public int manufacturer_id = 0;

    /** @var int Supplier id which address belongs to */
    public int supplier_id = 0;

    public int language_id;

    /**
     * @since 1.5.0
     * @var int Warehouse id which address belongs to
     */
    public int warehouse_id = 0;

    /** @var int Country id */
    public int country_id;

    /** @var int State id */
    public int state_id;

    /** @var string Country name */
    public String country;

    /** @var string Alias (eg. Home, Work...) */
    public String alias;

    /** @var string Company (optional) */
    public String company;

    /** @var string Lastname */
    public String lastname;

    /** @var string Firstname */
    public String firstname;

    /** @var string Address first line */
    public String address1;

    /** @var string Address second line (optional) */
    public String address2;

    /** @var string Postal code */
    public String postcode;

    /** @var string City */
    public String city;

    /** @var string Any other useful information */
    public String other;

    /** @var string Phone number */
    public String phone;

    /** @var string Mobile phone number */
    public String mobile_phone;

    /** @var string VAT number */
    public String vat_number;

    /** @var string DNI number */
    public String dni;

    /** @var string Object creation date */
    public Date date_add;

    /** @var string Object last modification date */
    public Date date_upd;

    /** @var bool True if address has been deleted (staying in database as deleted) */
    public boolean deleted = false;

    public boolean published = false;

    //protected static $_idZones = array();
    //protected static $_idCountries = array();

    private static Pagination pagination;

    public JeproLabAddressModel(){

    }

    public JeproLabAddressModel(int addressId){
        this(addressId, 0);
    }

    /**
     * Build an address
     *
     * @param addressId Existing address id in order to load object (optional)
     */
    public JeproLabAddressModel(int addressId, int langId){
        if(addressId > 0){
            String cacheKey = "jeprolab_";
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " AS address ";
                query += " WHERE address.address_id = " + addressId;

                dataBaseObject.setQuery(query);
                ResultSet addressSet = dataBaseObject.loadObject();
                try{
                    while(addressSet.next()){
                        this.address_id = addressSet.getInt("address_id");
                        this.country_id = addressSet.getInt("country_id");
                        this.state_id = addressSet.getInt("state_id");
                        this.customer_id = addressSet.getInt("customer_id");
                        this.manufacturer_id = addressSet.getInt("manufacturer_id");
                        this.supplier_id = addressSet.getInt("supplier_id");
                        this.warehouse_id = addressSet.getInt("warehouse_id");
                        this.alias = addressSet.getString("alias");
                        this.company = addressSet.getString("company");
                        this.lastname = addressSet.getString("lastname");
                        this.firstname = addressSet.getString("firstname");
                        this.address1 = addressSet.getString("address1");
                        this.address2 = addressSet.getString("address2");
                        this.postcode = addressSet.getString("postcode");
                        this.city = addressSet.getString("city");
                        this.other = addressSet.getString("other");
                        this.phone = addressSet.getString("phone");
                        this.mobile_phone = addressSet.getString("mobile_phone");
                        this.vat_number = addressSet.getString("vat_number");
                        this.dni = addressSet.getString("dni");
                        this.date_add = addressSet.getDate("date_add");
                        this.date_upd = addressSet.getDate("date_upd");
                        this.published = addressSet.getInt("published") > 0;
                        this.deleted = addressSet.getInt("deleted") > 0;
                    }
                }catch (SQLException ignored){

                }
            }else{
                JeproLabAddressModel addressModel = (JeproLabAddressModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.address_id = addressModel.address_id;
                this.country_id = addressModel.country_id;
                this.state_id = addressModel.state_id;
                this.customer_id = addressModel.customer_id;
                this.manufacturer_id = addressModel.manufacturer_id;
                this.supplier_id = addressModel.supplier_id;
                this.warehouse_id = addressModel.warehouse_id;
                this.alias = addressModel.alias;
                this.company = addressModel.company;
                this.lastname = addressModel.lastname;
                this.firstname = addressModel.firstname;
                this.address1 = addressModel.address1;
                this.address2 = addressModel.address2;
                this.postcode = addressModel.postcode;
                this.city = addressModel.city;
                this.other = addressModel.other;
                this.phone = addressModel.phone;
                this.mobile_phone = addressModel.mobile_phone;
                this.vat_number = addressModel.vat_number;
                this.dni = addressModel.dni;
                this.date_add = addressModel.date_add;
                this.date_upd = addressModel.date_upd;
                this.published = addressModel.published;
                this.deleted = addressModel.deleted;
            }
        }
        /* Get and cache address country name */
        if(address_id > 0){
        this.country = JeproLabCountryModel.getNameById((langId > 0) ? langId : JeproLabSettingModel.getIntValue("default_lang"), this.country_id);
        }
    }

    public static ResultSet getAddressList(){
        int limit = JeproLabConfig.getListLimit();
        return getAddressList(limit);
    }

    public static ResultSet getAddressList(int limit){
        return getAddressList(limit, 0);
    }

    public static ResultSet getAddressList(int limit, int limitStart){
        return getAddressList(limit, limitStart, 1);
    }

    public static ResultSet getAddressList(int limit, int limitStart, int langId){
        return getAddressList(limit, limitStart, langId, "address_id");
    }

    public static ResultSet getAddressList(int limit, int limitStart, int langId, String orderBy){
        return getAddressList(limit, limitStart, langId, orderBy, "ASC");
    }

    public static ResultSet getAddressList(int limit, int limitStart, int langId, String orderBy, String orderWay){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        int total = 0;
        ResultSet addresses = null;
        boolean useLimit = true;
        if(limit <= 0){ useLimit = false; }
        String orderByFilter = orderBy.replace("`", "");
        try {
            do {
                String query = "SELECT SQL_CALC_FOUND_ROWS address." + staticDataBaseObject.quoteName("address_id");
                query += ", address." + staticDataBaseObject.quoteName("firstname") + ", address." + staticDataBaseObject.quoteName("lastname");
                query += ", address." + staticDataBaseObject.quoteName("address1") + ", address." + staticDataBaseObject.quoteName("postcode");
                query += ", address." + staticDataBaseObject.quoteName("city") + ", country_lang." + staticDataBaseObject.quoteName("name");
                query += " AS country FROM " + staticDataBaseObject.quoteName("#__jeprolab_address") + " AS address LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON(country_lang.";
                query += staticDataBaseObject.quoteName("country_id") + " = address." + staticDataBaseObject.quoteName("country_id");
                query += " AND country_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_customer") + " AS customer ON address." + staticDataBaseObject.quoteName("customer_id");
                query += " = customer." + staticDataBaseObject.quoteName("customer_id") + " WHERE address.customer_id != 0 ";
                query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER, "customer");
                query += " ORDER BY " + (orderByFilter.equals("address_id") ? "address." : "") + orderBy + " " + orderWay;

                staticDataBaseObject.setQuery(query);
                addresses = staticDataBaseObject.loadObject();

                while (addresses.next()) {
                    total++;
                }

                query += (useLimit ? " LIMIT " + limitStart + ", " + limit : " ");

                staticDataBaseObject.setQuery(query);
                addresses = staticDataBaseObject.loadObject();

                if(useLimit){
                    limitStart = limitStart - limit;
                    if(limitStart < 0){ break; }
                }else {
                    break;
                }
            } while (addresses == null);

            pagination = new Pagination();

        }catch (SQLException ignored){
            ignored.printStackTrace();
        }
        return  addresses;
    }



    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'address',
                    'primary' => 'id_address',
                    'fields' => array(
                    'id_customer' =>        array('type' => self::TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_manufacturer' =>    array('type' => self::TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_supplier' =>        array('type' => self::TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_warehouse' =>        array('type' => self::TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_country' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_state' =>            array('type' => self::TYPE_INT, 'validate' => 'isNullOrUnsignedId'),
    'alias' =>                array('type' => self::TYPE_STRING, 'validate' => 'isGenericName', 'required' => true, 'size' => 32),
    'company' =>            array('type' => self::TYPE_STRING, 'validate' => 'isGenericName', 'size' => 64),
    'lastname' =>            array('type' => self::TYPE_STRING, 'validate' => 'isName', 'required' => true, 'size' => 32),
    'firstname' =>            array('type' => self::TYPE_STRING, 'validate' => 'isName', 'required' => true, 'size' => 32),
    'vat_number' =>            array('type' => self::TYPE_STRING, 'validate' => 'isGenericName'),
    'address1' =>            array('type' => self::TYPE_STRING, 'validate' => 'isAddress', 'required' => true, 'size' => 128),
    'address2' =>            array('type' => self::TYPE_STRING, 'validate' => 'isAddress', 'size' => 128),
    'postcode' =>            array('type' => self::TYPE_STRING, 'validate' => 'isPostCode', 'size' => 12),
    'city' =>                array('type' => self::TYPE_STRING, 'validate' => 'isCityName', 'required' => true, 'size' => 64),
    'other' =>                array('type' => self::TYPE_STRING, 'validate' => 'isMessage', 'size' => 300),
    'phone' =>                array('type' => self::TYPE_STRING, 'validate' => 'isPhoneNumber', 'size' => 32),
    'phone_mobile' =>        array('type' => self::TYPE_STRING, 'validate' => 'isPhoneNumber', 'size' => 32),
    'dni' =>                array('type' => self::TYPE_STRING, 'validate' => 'isDniLite', 'size' => 16),
    'deleted' =>            array('type' => self::TYPE_BOOL, 'validate' => 'isBool', 'copy_post' => false),
    'date_add' =>            array('type' => self::TYPE_DATE, 'validate' => 'isDate', 'copy_post' => false),
    'date_upd' =>            array('type' => self::TYPE_DATE, 'validate' => 'isDate', 'copy_post' => false),
    ),
            );

    protected $_includeVars = array('addressType' => 'table');
    protected $_includeContainer = false;

    protected $webserviceParameters = array(
            'objectsNodeName' => 'addresses',
                    'fields' => array(
                    'id_customer' => array('xlink_resource'=> 'customers'),
    'id_manufacturer' => array('xlink_resource'=> 'manufacturers'),
    'id_supplier' => array('xlink_resource'=> 'suppliers'),
    'id_warehouse' => array('xlink_resource'=> 'warehouse'),
    'id_country' => array('xlink_resource'=> 'countries'),
    'id_state' => array('xlink_resource'=> 'states'),
    ),
            );



    /**
     * @see ObjectModel::add()
     * /
    public function add($autodate = true, $null_values = false)
    {
        if (!parent::add($autodate, $null_values)) {
        return false;
    }

        if (Validate::isUnsignedId($this->id_customer)) {
        Customer::resetAddressCache($this->id_customer, $this->id);
    }
        return true;
    }

    public function update($null_values = false)
    {
        // Empty related caches
        if (isset(self::$_idCountries[$this->id])) {
        unset(self::$_idCountries[$this->id]);
    }
        if (isset(self::$_idZones[$this->id])) {
        unset(self::$_idZones[$this->id]);
    }

        if (Validate::isUnsignedId($this->id_customer)) {
        Customer::resetAddressCache($this->id_customer, $this->id);
    }

        return parent::update($null_values);
    }

    /**
     * @see ObjectModel::delete()
     * /
    public function delete()
    {
        if (Validate::isUnsignedId($this->id_customer)) {
        Customer::resetAddressCache($this->id_customer, $this->id);
    }

        if (!$this->isUsed()) {
            return parent::delete();
        } else {
            $this->deleted = true;
            return $this->update();
        }
    }

    /**
     * Returns fields required for an address in an array hash
     * @return array hash values
     * /
    public static function getFieldsValidate()
    {
        $tmp_addr = new Address();
        $out = $tmp_addr->fieldsValidate;

        unset($tmp_addr);
        return $out;
    }

    /**
     * @see ObjectModel::validateController()
     * /
    public function validateController($htmlentities = true)
    {
        $errors = parent::validateController($htmlentities);
        if (!Configuration::get('VATNUMBER_MANAGEMENT') || !Configuration::get('VATNUMBER_CHECKING')) {
        return $errors;
    }
        include_once(_PS_MODULE_DIR_.'vatnumber/vatnumber.php');
        if (class_exists('VatNumber', false)) {
            return array_merge($errors, VatNumber::WebServiceCheck($this->vat_number));
        }
        return $errors;
    }
    /**
     * Get zone id for a given address
     *
     * @param int $id_address Address id for which we want to get zone id
     * @return int Zone id
     * /
    public static function getZoneById($id_address)
    {
        if (!isset($id_address) || empty($id_address)) {
            return false;
        }

        if (isset(self::$_idZones[$id_address])) {
        return self::$_idZones[$id_address];
    }

        $id_zone = Hook::exec('actionGetIDZoneByAddressID', array('id_address' => $id_address));

        if (is_numeric($id_zone)) {
            self::$_idZones[$id_address] = (int)$id_zone;
            return self::$_idZones[$id_address];
        }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT s.`id_zone` AS id_zone_state, c.`id_zone`
            FROM `'._DB_PREFIX_.'address` a
        LEFT JOIN `'._DB_PREFIX_.'country` c ON c.`id_country` = a.`id_country`
        LEFT JOIN `'._DB_PREFIX_.'state` s ON s.`id_state` = a.`id_state`
        WHERE a.`id_address` = '.(int)$id_address);

        self::$_idZones[$id_address] = (int)((int)$result['id_zone_state'] ? $result['id_zone_state'] : $result['id_zone']);
        return self::$_idZones[$id_address];
    }

    /**
     * Check if country is active for a given address
     *
     * @param int $id_address Address id for which we want to get country status
     * @return int Country status
     * /
    public static function isCountryActiveById($id_address)
    {
        if (!isset($id_address) || empty($id_address)) {
            return false;
        }

        $cache_id = 'Address::isCountryActiveById_'.(int)$id_address;
        if (!Cache::isStored($cache_id)) {
        $result = (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getvalue('
                SELECT c.`active`
                FROM `'._DB_PREFIX_.'address` a
        LEFT JOIN `'._DB_PREFIX_.'country` c ON c.`id_country` = a.`id_country`
        WHERE a.`id_address` = '.(int)$id_address);
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Check if address is used (at least one order placed)
     *
     * @return int Order count for this address
     * /
    public function isUsed()
    {
        $result = (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT COUNT(`id_order`) AS used
            FROM `'._DB_PREFIX_.'orders`
        WHERE `id_address_delivery` = '.(int)$this->id.'
        OR `id_address_invoice` = '.(int)$this->id);

        return $result > 0 ? (int)$result : false;
    }

    public static function getCountryAndState($id_address)
    {
        if (isset(self::$_idCountries[$id_address])) {
        return self::$_idCountries[$id_address];
    }
        if ($id_address) {
            $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
                    SELECT `id_country`, `id_state`, `vat_number`, `postcode` FROM `'._DB_PREFIX_.'address`
            WHERE `id_address` = '.(int)$id_address);
        } else {
            $result = false;
        }
        self::$_idCountries[$id_address] = $result;
        return $result;
    }

    /**
     * Specify if an address is already in base
     *
     * @param int $id_address Address id
     * @return bool
     * /
    public static function addressExists($id_address)
    {
        $key = 'address_exists_'.(int)$id_address;
        if (!Cache::isStored($key)) {
        $id_address = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('SELECT `id_address` FROM '._DB_PREFIX_.'address a WHERE a.`id_address` = '.(int)$id_address);
        Cache::store($key, (bool)$id_address);
        return (bool)$id_address;
    }
        return Cache::retrieve($key);
    }

    public static function getFirstCustomerAddressId($id_customer, $active = true)
    {
        if (!$id_customer) {
            return false;
        }
        $cache_id = 'Address::getFirstCustomerAddressId_'.(int)$id_customer.'-'.(bool)$active;
        if (!Cache::isStored($cache_id)) {
        $result = (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                SELECT `id_address`
                FROM `'._DB_PREFIX_.'address`
        WHERE `id_customer` = '.(int)$id_customer.' AND `deleted` = 0'.($active ? ' AND `active` = 1' : '')
        );
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Initialize an address corresponding to the specified id address or if empty to the
     * default shop configuration
     *
     * @param int $id_address
     * @param bool $with_geoloc
     * @return Address address
     *
     * @throws PrestaShopException
     * /
    public static function initialize($id_address = null, $with_geoloc = false)
    {
        $context = Context::getContext();

        if ($id_address) {
            $context_hash = (int)$id_address;
        } elseif ($with_geoloc && isset($context->customer->geoloc_id_country)) {
        $context_hash = md5((int)$context->customer->geoloc_id_country.'-'.(int)$context->customer->id_state.'-'.
                $context->customer->postcode);
    } else {
        $context_hash = md5((int)$context->country->id);
    }


        $cache_id = 'Address::initialize_'.$context_hash;

        if (!Cache::isStored($cache_id)) {
        // if an id_address has been specified retrieve the address
        if ($id_address) {
            $address = new Address((int)$id_address);

            if (!Validate::isLoadedObject($address)) {
                throw new PrestaShopException('Invalid address #'.(int)$id_address);
            }
        } elseif ($with_geoloc && isset($context->customer->geoloc_id_country)) {
            $address             = new Address();
            $address->id_country = (int)$context->customer->geoloc_id_country;
            $address->id_state   = (int)$context->customer->id_state;
            $address->postcode   = $context->customer->postcode;
        } else {
            // set the default address
            $address             = new Address();
            $address->id_country = (int)$context->country->id;
            $address->id_state   = 0;
            $address->postcode   = 0;
        }
        Cache::store($cache_id, $address);
        return $address;
    }

        return Cache::retrieve($cache_id);
    }

    /**
     * Returns id_address for a given id_supplier
     * @since 1.5.0
     * @param int $id_supplier
     * @return int $id_address
     * /
    public static function getAddressIdBySupplierId($id_supplier)
    {
        $query = new DbQuery();
        $query->select('id_address');
        $query->from('address');
        $query->where('id_supplier = '.(int)$id_supplier);
        $query->where('deleted = 0');
        $query->where('id_customer = 0');
        $query->where('id_manufacturer = 0');
        $query->where('id_warehouse = 0');
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
    }

    public static function aliasExist($alias, $id_address, $id_customer)
    {
        $query = new DbQuery();
        $query->select('count(*)');
        $query->from('address');
        $query->where('alias = \''.pSQL($alias).'\'');
        $query->where('id_address != '.(int)$id_address);
        $query->where('id_customer = '.(int)$id_customer);
        $query->where('deleted = 0');

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
    }

    public function getFieldsRequiredDB()
    {
        $this->cacheFieldsRequiredDatabase(false);
        if (isset(self::$fieldsRequiredDatabase['Address'])) {
        return self::$fieldsRequiredDatabase['Address'];
    }
        return array();
    } */
}