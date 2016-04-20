package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.controllers.JeproLabCategoryController;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    protected static Map<Integer, ResultSet> zones_ids  = new HashMap<>();
    protected static Map<Integer, ResultSet> countries_ids = new HashMap<>();

    private static Pagination pagination;

    public JeproLabAddressModel(){
        this(0, 0);
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

                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    'id_customer' =>        array('type' => JeproLabAddressModel.TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_manufacturer' =>    array('type' => JeproLabAddressModel.TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_supplier' =>        array('type' => JeproLabAddressModel.TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_warehouse' =>        array('type' => JeproLabAddressModel.TYPE_INT, 'validate' => 'isNullOrUnsignedId', 'copy_post' => false),
    'id_country' =>        array('type' => JeproLabAddressModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_state' =>            array('type' => JeproLabAddressModel.TYPE_INT, 'validate' => 'isNullOrUnsignedId'),
    'alias' =>                array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isGenericName', 'required' => true, 'size' => 32),
    'company' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isGenericName', 'size' => 64),
    'lastname' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isName', 'required' => true, 'size' => 32),
    'firstname' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isName', 'required' => true, 'size' => 32),
    'vat_number' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isGenericName'),
    'address1' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isAddress', 'required' => true, 'size' => 128),
    'address2' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isAddress', 'size' => 128),
    'postcode' =>            array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isPostCode', 'size' => 12),
    'city' =>                array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isCityName', 'required' => true, 'size' => 64),
    'other' =>                array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isMessage', 'size' => 300),
    'phone' =>                array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isPhoneNumber', 'size' => 32),
    'phone_mobile' =>        array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isPhoneNumber', 'size' => 32),
    'dni' =>                array('type' => JeproLabAddressModel.TYPE_STRING, 'validate' => 'isDniLite', 'size' => 16),
    'deleted' =>            array('type' => JeproLabAddressModel.TYPE_BOOL, 'validate' => 'isBool', 'copy_post' => false),
    'date_add' =>            array('type' => JeproLabAddressModel.TYPE_DATE, 'validate' => 'isDate', 'copy_post' => false),
    'date_upd' =>            array('type' => JeproLabAddressModel.TYPE_DATE, 'validate' => 'isDate', 'copy_post' => false),
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
     */
    public boolean add(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        Map<String, String> addressPost = JeproLab.request.getPost();
        int countryId = addressPost.containsKey("country_id") ? Integer.parseInt(addressPost.get("country_id")) : 0 ;
        int stateId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int customerId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int manufacturerId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int supplierId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int warehouseId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        String localAlias = addressPost.containsKey("alias") ? addressPost.get("alias") : "" ;
        String localCompany = addressPost.containsKey("company") ? addressPost.get("company") : "";
        String localLastName = addressPost.containsKey("lastname") ? addressPost.get("lastname") : "";
        String localFirstName = addressPost.containsKey("firstname") ? addressPost.get("firstname") : "";
        String localAddress1 = addressPost.containsKey("address_1") ? addressPost.get("address_1") : "";
        String localAddress2 = addressPost.containsKey("address_2") ? addressPost.get("address_2") : "";
        String localPostCode = addressPost.containsKey("post_code") ? addressPost.get("post_code") : "";
        String localCity = addressPost.containsKey("city") ? addressPost.get("city") : "";
        String localOther = addressPost.containsKey("other") ? addressPost.get("other") : "";
        String localPhone = addressPost.containsKey("phone") ? addressPost.get("phone") : "";
        String localMobilePhone = addressPost.containsKey("mobile_phone") ? addressPost.get("mobile_phone") : "";
        String vatNumber = addressPost.containsKey("vat_number") ? addressPost.get("vat_number") : "";
        String currentDate = dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"));

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_address") + "(" + dataBaseObject.quoteName("country_id") + ", ";
        query += dataBaseObject.quoteName("state_id") + ", " + dataBaseObject.quoteName("customer_id") + ", " + dataBaseObject.quoteName("manufacturer_id");
        query += ", " + dataBaseObject.quoteName("supplier_id") +  ", " + dataBaseObject.quoteName("warehouse_id") + ", " ;
        query += dataBaseObject.quoteName("alias") + ", " + dataBaseObject.quoteName("company") + ", " + dataBaseObject.quoteName("lastname") + ", " + dataBaseObject.quoteName("firstname");
        query += ", " + dataBaseObject.quoteName("address1") + ", " + dataBaseObject.quoteName("address2") + ", " + dataBaseObject.quoteName("postcode");
        query += ", " + dataBaseObject.quoteName("city") + ", " + dataBaseObject.quoteName("other") + ", " + dataBaseObject.quoteName("phone");
        query += ", " + dataBaseObject.quoteName("phone_mobile") + ", " + dataBaseObject.quoteName("vat_number") + ", " + dataBaseObject.quoteName("date_add");
        query += ", " + dataBaseObject.quoteName("date_upd") + ") VALUES (" + countryId + ", " + stateId + ", " + customerId  + ", " + manufacturerId + ", " + supplierId + ", " + warehouseId + ", ";
        query += dataBaseObject.quote(localAlias) + ", " + dataBaseObject.quote(localCompany) + ", " + dataBaseObject.quote(localLastName) + ", ";
        query += dataBaseObject.quote(localFirstName) + ", " + dataBaseObject.quote(localAddress1) + ", " + dataBaseObject.quote(localAddress2) + ", ";
        query += dataBaseObject.quote(localPostCode) + ", " + dataBaseObject.quote(localCity) + ", " + dataBaseObject.quote(localOther) + ", ";
        query += dataBaseObject.quote(localPhone) + ", " + dataBaseObject.quote(localMobilePhone) + ", " + dataBaseObject.quote(vatNumber) + "," + currentDate + ", " + currentDate + ")";

        dataBaseObject.setQuery(query);
        if (!dataBaseObject.query(true)) {
            return false;
        }

        if (customerId > 0) {
            JeproLabCustomerModel.resetAddressCache(customerId, dataBaseObject.getGeneratedKey());
        }
        return true;
    }

    public boolean update(){
        // Empty related caches
        if (JeproLabAddressModel.countries_ids.containsKey(this.address_id)) {
            JeproLabAddressModel.countries_ids.remove(this.address_id);
        }
        if (JeproLabAddressModel.zones_ids.containsKey(this.address_id)) {
            JeproLabAddressModel.zones_ids.remove(this.address_id);
        }

        if (this.customer_id > 0){
            JeproLabCustomerModel.resetAddressCache(this.customer_id, this.address_id);
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        Map<String, String> addressPost = JeproLab.request.getPost();
        int countryId = addressPost.containsKey("country_id") ? Integer.parseInt(addressPost.get("country_id")) : 0 ;
        int stateId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int customerId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int manufacturerId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int supplierId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        int warehouseId = addressPost.containsKey("") ? Integer.parseInt(addressPost.get("")) : 0 ;
        String localAlias = addressPost.containsKey("alias") ? addressPost.get("alias") : "" ;
        String localCompany = addressPost.containsKey("company") ? addressPost.get("company") : "";
        String localLastName = addressPost.containsKey("lastname") ? addressPost.get("lastname") : "";
        String localFirstName = addressPost.containsKey("firstname") ? addressPost.get("firstname") : "";
        String localAddress1 = addressPost.containsKey("address_1") ? addressPost.get("address_1") : "";
        String localAddress2 = addressPost.containsKey("address_2") ? addressPost.get("address_2") : "";
        String localPostCode = addressPost.containsKey("post_code") ? addressPost.get("post_code") : "";
        String localCity = addressPost.containsKey("city") ? addressPost.get("city") : "";
        String localOther = addressPost.containsKey("other") ? addressPost.get("other") : "";
        String localPhone = addressPost.containsKey("phone") ? addressPost.get("phone") : "";
        String localMobilePhone = addressPost.containsKey("mobile_phone") ? addressPost.get("mobile_phone") : "";
        String vatNumber = addressPost.containsKey("vat_number") ? addressPost.get("vat_number") : "";
        String currentDate = dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"));

        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_address") + " SET " + dataBaseObject.quoteName("country_id") + " = ";
        query += countryId + ", " + dataBaseObject.quoteName("state_id") + " = " + stateId + ", " + dataBaseObject.quoteName("customer_id") + " = ";
        query += customerId + ", " + dataBaseObject.quoteName("manufacturer_id") + " = " + manufacturerId + ", " + dataBaseObject.quoteName("supplier_id");
        query += " = " + supplierId + ", " + dataBaseObject.quoteName("warehouse_id") + " = " + warehouseId + ", " + dataBaseObject.quoteName("alias");
        query += " = " + dataBaseObject.quote(localAlias) + ", " + dataBaseObject.quoteName("company") + " = " + dataBaseObject.quote(localCompany) + ", ";
        query += dataBaseObject.quoteName("lastname") + " = " + dataBaseObject.quote(localLastName) + ", " + dataBaseObject.quoteName("address1") + " = ";
        query += dataBaseObject.quote(localAddress1) + ", " + dataBaseObject.quoteName("address2") + " = " + dataBaseObject.quote(localAddress2) + ", ";
        query += dataBaseObject.quoteName("postcode") + " = " + dataBaseObject.quote(localPostCode) + ", " + dataBaseObject.quoteName("city") + " = ";
        query += dataBaseObject.quote(localCity) +  ", " + dataBaseObject.quoteName("other") + " = " + dataBaseObject.quote(localOther) + ", ";
        query += dataBaseObject.quoteName("phone") + " = " + dataBaseObject.quote(localPhone) + ", " + dataBaseObject.quoteName("phone_mobile") + " = ";
        query += dataBaseObject.quote(localMobilePhone) + ", " + dataBaseObject.quoteName("firstname") + " = " + dataBaseObject.quote(localFirstName) + ", ";
        query += dataBaseObject.quoteName("vat_number") + " = " + dataBaseObject.quote(vatNumber) + ", " + dataBaseObject.quoteName("date_upd")+ "  = ";
        query += currentDate + " WHERE " + dataBaseObject.quoteName("address_id") + " = " + this.address_id;
        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }

    /**
     *
     */
    public boolean delete(){
        if (this.customer_id > 0) {
            JeproLabCustomerModel.resetAddressCache(this.customer_id, this.address_id);
        }

        if (!this.isUsed()) {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " WHERE ";
            query += dataBaseObject.quoteName("address_id") + " = " + this.address_id;

            dataBaseObject.setQuery(query);
            return dataBaseObject.query(false);
        } else {
            this.deleted = true;
            return this.update();
        }
    }

    /*
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
            return array_merge($errors, VatNumber::WebServiceCheck(this.vat_number));
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

        if (isset(JeproLabAddressModel.$_idZones[$id_address])) {
        return JeproLabAddressModel.$_idZones[$id_address];
    }

        $id_zone = Hook::exec('actionGetIDZoneByAddressID', array('id_address' => $id_address));

        if (is_numeric($id_zone)) {
            JeproLabAddressModel.$_idZones[$id_address] = (int)$id_zone;
            return JeproLabAddressModel.$_idZones[$id_address];
        }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT s.`id_zone` AS id_zone_state, c.`id_zone`
            FROM `'._DB_PREFIX_.'address` a
        LEFT JOIN `'._DB_PREFIX_.'country` c ON c.`id_country` = a.`id_country`
        LEFT JOIN `'._DB_PREFIX_.'state` s ON s.`id_state` = a.`id_state`
        WHERE a.`id_address` = '.(int)$id_address);

        JeproLabAddressModel.$_idZones[$id_address] = (int)((int)$result['id_zone_state'] ? $result['id_zone_state'] : $result['id_zone']);
        return JeproLabAddressModel.$_idZones[$id_address];
    }

    /**
     * Check if country is active for a given address
     *
     * @param addressId Address id for which we want to get country status
     * @return int Country status
     */
    public static boolean isCountryActiveByAddressId(int addressId){
        if (addressId <= 0) {
            return false;
        }

        String cacheKey = "jeprolab_address_isCountryActiveById_" + addressId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT country." + staticDataBaseObject.quoteName("published") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
            query += " AS address LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_country") + " AS country ON country.";
            query += staticDataBaseObject.quoteName("country_id") + " = address." + staticDataBaseObject.quoteName("country_id") + " WHERE address.";
            query += staticDataBaseObject.quoteName("address_id") + " = " + addressId;

            staticDataBaseObject.setQuery(query);
            boolean result = staticDataBaseObject.loadValue("published") > 0;
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Check if address is used (at least one order placed)
     *
     * @return int Order count for this address
     */
    public boolean isUsed(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "ELECT COUNT(" + dataBaseObject.quoteName("request_id") + ") AS used FROM " + dataBaseObject.quoteName("#__jeprolab_request");
        query += " WHERE "  + dataBaseObject.quoteName("delivery_address_id") + " = " + this.address_id + " OR " + dataBaseObject.quoteName("invoice_addres_id");
        query += " = " + this.address_id;

        dataBaseObject.setQuery(query);

        return dataBaseObject.loadValue("used") > 0;
    }

    public static ResultSet getCountryAndState(int addressId){
        if (JeproLabAddressModel.countries_ids.containsKey(addressId)){
            return JeproLabAddressModel.countries_ids.get(addressId);
        }
        ResultSet  result;
        if (addressId > 0) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("country_id") + ", " + staticDataBaseObject.quoteName("state_id");
            query += ", " + staticDataBaseObject.quoteName("vat_number") + ", " + staticDataBaseObject.quoteName("postcode") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeproLab_address") + " WHERE " + staticDataBaseObject.quoteName("address_id") + " = " + addressId;

            staticDataBaseObject.setQuery(query);
            result = staticDataBaseObject.loadObject();
            JeproLabAddressModel.countries_ids.put(addressId, result);
        } else {
            result = null;
        }

        return result;
    }

    /**
     * Specify if an address is already in base
     *
     * @param addressId Address id
     * @return bool
     */
    public static boolean addressExists(int addressId){
        String cacheKey = "jeprolab_address_exists_" + addressId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("address_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
            query += " AS address WHERE " + staticDataBaseObject.quoteName("address_id") + " = " + addressId;

            staticDataBaseObject.setQuery(query);
            boolean addressExists = (int)staticDataBaseObject.loadValue("address_id") > 0;
            JeproLabCache.getInstance().store(cacheKey, addressExists);

            return addressExists;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static int getFirstCustomerAddressId(int customerId){
        return getFirstCustomerAddressId(customerId, true);
    }

    public static int getFirstCustomerAddressId(int customerId, boolean active){
        if (customerId <= 0) {
            return 0;
        }
        String cacheKey = "jeprolab_address_getFirstCustomerAddressId_" + customerId + "_" + (active ? 1 : 0);
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("address_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND " + staticDataBaseObject.quoteName("deleted");
            query += " = 0 " + (active ? " AND " + staticDataBaseObject.quoteName("active") + " = 1" : "");

            staticDataBaseObject.setQuery(query);
            int result = (int)staticDataBaseObject.loadValue("address_id");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static JeproLabAddressModel initialize() {
        return initialize(0, false);
    }

    public static JeproLabAddressModel initialize(int addressId) {
        return initialize(addressId, false);
    }

    /**
     * Initialize an address corresponding to the specified id address or if empty to the
     * default shop configuration
     *
     * @param addressId address id
     * @param withGeolocation with geolocation
     * @return JeproLabAddressModel address
     *
     */
    public static JeproLabAddressModel initialize(int addressId, boolean withGeolocation) {
        JeproLabContext context = JeproLabContext.getContext();
        String contextHash;
        if (addressId > 0) {
            contextHash = addressId + "";
        } else if (withGeolocation && (context.customer.geolocation_country_id > 0)) {
            contextHash = JeproLabTools.md5(context.customer.geolocation_country_id + "_" + context.customer.state_id + "_" + context.customer.postcode);
        } else {
            contextHash = JeproLabTools.md5(context.country.country_id + "");
        }

        String cacheKey = "jeprolab_address_initialize_" + contextHash;
        JeproLabAddressModel address;

        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            // if an id_address has been specified retrieve the address
            if (addressId > 0) {
                address = new JeproLabAddressModel(addressId);

                if (address.address_id <= 0) {
                    JeproLabTools.displayWarning(500, JeproLab.getBundle().getString("JEPROLAB_LABEL") + addressId); //'Invalid address #'.(int)$id_address);
                }
            } else if (withGeolocation && (context.customer.geolocation_country_id > 0)) {
                address = new JeproLabAddressModel();
                address.country_id = context.customer.geolocation_country_id;
                address.state_id   = context.customer.state_id;
                address.postcode   = context.customer.postcode;
            } else {
                // set the default address
                address = new JeproLabAddressModel();
                address.country_id = context.country.country_id;
                address.state_id   = 0;
                address.postcode   = "";
            }
            JeproLabCache.getInstance().store(cacheKey, address);
            return address;
        }

        return (JeproLabAddressModel)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static JeproLabAddressModel getAddressByCustomerId(int customerId, boolean main) {
        return new JeproLabAddressModel();
    }

    /**
     * Returns address id for a given supplier id
     *
     * @param supplierId supplier id
     * @return int address id
     */
    public static int getAddressIdBySupplierId(int supplierId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("address_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_address");
        query += " WHERE " + staticDataBaseObject.quoteName("supplier_id") + " = " + supplierId + " AND " + staticDataBaseObject.quoteName("deleted");
        query += " = 0 AND " + staticDataBaseObject.quoteName("deleted") + " = 0 AND " + staticDataBaseObject.quoteName("customer_id") + " = 0 AND ";
        query += staticDataBaseObject.quoteName("manufacturer_id") + " = 0 AND " + staticDataBaseObject.quoteName("warehouse_id") + " = 0";
        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("address_id");

    }

    public static boolean aliasExist(String alias, int addressId, int customerId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT COUNT(*) AS address FROM " + staticDataBaseObject.quoteName("#__jeprolab_address") + " WHERE ";
        query += staticDataBaseObject.quoteName("alias") + " = " + staticDataBaseObject.quote(alias) + " AND " ;
        query += staticDataBaseObject.quoteName("address_id") + " = " + addressId + " AND " + staticDataBaseObject.quoteName("customer_id");
        query += " = " + customerId + " AND " + staticDataBaseObject.quoteName("deleted") + " = 0";

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadValue("address") > 0;

    }
/*
    public function getFieldsRequiredDB()
    {
        this.cacheFieldsRequiredDatabase(false);
        if (isset(JeproLabAddressModel.$fieldsRequiredDatabase['Address'])) {
        return JeproLabAddressModel.$fieldsRequiredDatabase['Address'];
    }
        return array();
    } */
}