package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;
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
public class JeproLabAddressModel extends JeproLabModel{
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

    /** @var string First-name */
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

    protected static Map<Integer, Map<String, String>> zones_ids  = new HashMap<>();
    protected static Map<Integer, Map<String, String>> countries_ids = new HashMap<>();

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
                String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address") + " AS address ";
                query += " WHERE address.address_id = " + addressId;

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                ResultSet addressSet = dataBaseObject.loadObjectList(query);
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
                        this.mobile_phone = addressSet.getString("phone_mobile");
                        this.vat_number = addressSet.getString("vat_number");
                        this.dni = addressSet.getString("dni");
                        this.date_add =addressSet.getDate("date_add");
                        this.date_upd = addressSet.getDate("date_upd");
                        this.published = addressSet.getInt("published") > 0;
                        this.deleted = addressSet.getInt("deleted") > 0;
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }else{
                JeproLabAddressModel addressModel = (JeproLabAddressModel) JeproLabCache.getInstance().retrieve(cacheKey);
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
        if(langId > 0){
            this.country = JeproLabCountryModel.getCountryNameByCountryId((langId > 0) ? langId : JeproLabSettingModel.getIntValue("default_lang"), this.country_id);
        }
    }

    public static Map<String, String> getCountryAndState(int addressId){
        if (JeproLabAddressModel.countries_ids.containsKey(addressId)){
            return JeproLabAddressModel.countries_ids.get(addressId);
        }
        ResultSet  result;
        if (addressId > 0) {
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("country_id") + ", " + JeproLabDataBaseConnector.quoteName("state_id");
            query += ", " + JeproLabDataBaseConnector.quoteName("vat_number") + ", " + JeproLabDataBaseConnector.quoteName("postcode") + " FROM ";
            query += JeproLabDataBaseConnector.quoteName("#__jeproLab_address") + " WHERE " + JeproLabDataBaseConnector.quoteName("address_id") + " = " + addressId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            result = dataBaseObject.loadObjectList(query);
            if(result != null){
                try{
                    Map<String, String> resultItem;
                    if(result.next()){
                        resultItem = new HashMap<>();
                        resultItem.put("country_id", result.getString("country_id"));
                        resultItem.put("state_id", result.getString("state_id"));
                        resultItem.put("vat_number", result.getString("vat_number"));
                        JeproLabAddressModel.countries_ids.put(addressId, resultItem);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }
            return JeproLabAddressModel.countries_ids.get(addressId);
        } else {
            return null;
        }
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
        boolean exists = addressId > 0 && JeproLabAddressModel.addressExists(addressId);
        String contextHash;
        if (exists) {
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
            if (exists) {
                address = new JeproLabAddressModel(addressId);

                if (address.address_id <= 0) {
                    JeproLabTools.displayWarning(500, JeproLab.getBundle().getString("JEPROLAB_INVALID_ADDRESS_ID_LABEL") + addressId); //'Invalid address #'.(int)$id_address);
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

    /**
     * Specify if an address is already in base
     *
     * @param addressId Address id
     * @return bool
     */
    public static boolean addressExists(int addressId){
        String cacheKey = "jeprolab_address_exists_" + addressId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("address_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
            query += " AS address WHERE " + JeproLabDataBaseConnector.quoteName("address_id") + " = " + addressId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            boolean addressExists = (int)dataBaseObject.loadValue(query, "address_id") > 0;
            JeproLabCache.getInstance().store(cacheKey, addressExists);

            return addressExists;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Check if the address is valid
     *
     * @param addressId Address id
     *
     * @return bool The address is valid
     */
    public static boolean isValid(int addressId){
        String query = "SELECT address." + JeproLabDataBaseConnector.quoteName("address_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
        query += " AS address WHERE address." + JeproLabDataBaseConnector.quoteName("address_id") + " = " + addressId + " AND address." + JeproLabDataBaseConnector.quoteName("deleted");
        query += " = 0 AND address." + JeproLabDataBaseConnector.quoteName("published") + " = 1 ";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return ((int)dataBaseObject.loadValue(query, "address_id")) > 0;
    }

    /**
     *
     */
    public void add(){
        String currentDate = JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"));

        String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address") + "(" + JeproLabDataBaseConnector.quoteName("country_id") + ", ";
        query += JeproLabDataBaseConnector.quoteName("state_id") + ", " + JeproLabDataBaseConnector.quoteName("customer_id") + ", " + JeproLabDataBaseConnector.quoteName("manufacturer_id");
        query += ", " + JeproLabDataBaseConnector.quoteName("supplier_id") +  ", " + JeproLabDataBaseConnector.quoteName("warehouse_id") + ", " ;
        query += JeproLabDataBaseConnector.quoteName("alias") + ", " + JeproLabDataBaseConnector.quoteName("company") + ", " + JeproLabDataBaseConnector.quoteName("lastname") + ", " + JeproLabDataBaseConnector.quoteName("firstname");
        query += ", " + JeproLabDataBaseConnector.quoteName("address1") + ", " + JeproLabDataBaseConnector.quoteName("address2") + ", " + JeproLabDataBaseConnector.quoteName("postcode");
        query += ", " + JeproLabDataBaseConnector.quoteName("city") + ", " + JeproLabDataBaseConnector.quoteName("other") + ", " + JeproLabDataBaseConnector.quoteName("phone");
        query += ", " + JeproLabDataBaseConnector.quoteName("phone_mobile") + ", " + JeproLabDataBaseConnector.quoteName("vat_number") + ", " + JeproLabDataBaseConnector.quoteName("date_add");
        query += ", " + JeproLabDataBaseConnector.quoteName("date_upd") + ") VALUES (" + this.country_id + ", " + this.state_id + ", " + this.customer_id  + ", " + this.manufacturer_id + ", " + this.supplier_id + ", " + this.warehouse_id + ", ";
        query += JeproLabDataBaseConnector.quote(this.alias) + ", " + JeproLabDataBaseConnector.quote(this.company) + ", " + JeproLabDataBaseConnector.quote(this.lastname) + ", ";
        query += JeproLabDataBaseConnector.quote(this.firstname) + ", " + JeproLabDataBaseConnector.quote(this.address1) + ", " + JeproLabDataBaseConnector.quote(this.address2) + ", ";
        query += JeproLabDataBaseConnector.quote(this.postcode) + ", " + JeproLabDataBaseConnector.quote(this.city) + ", " + JeproLabDataBaseConnector.quote(this.other) + ", ";
        query += JeproLabDataBaseConnector.quote(this.phone) + ", " + JeproLabDataBaseConnector.quote(this.mobile_phone) + ", " + JeproLabDataBaseConnector.quote(this.vat_number) + "," + currentDate + ", " + currentDate + ")";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, true);
        this.address_id = dataBaseObject.getGeneratedKey();

        if (this.customer_id > 0) {
            JeproLabCustomerModel.resetAddressCache(this.customer_id, dataBaseObject.getGeneratedKey());
        }
    }

    /**
     *
     */
    public boolean delete(){
        if (this.customer_id > 0) {
            JeproLabCustomerModel.resetAddressCache(this.customer_id, this.address_id);
        }

        if (!this.isUsed()) {
            String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address") + " WHERE ";
            query += JeproLabDataBaseConnector.quoteName("address_id") + " = " + this.address_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            return dataBaseObject.query(query, false);
        } else {
            this.deleted = true;
            this.update();
        }
        return true;
    }

    /**
     * Check if address is used (at least one order placed)
     *
     * @return int Order count for this address
     */
    public boolean isUsed(){
        String query = "ELECT COUNT(" + JeproLabDataBaseConnector.quoteName("request_id") + ") AS used FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request");
        query += " WHERE "  + JeproLabDataBaseConnector.quoteName("delivery_address_id") + " = " + this.address_id + " OR " + JeproLabDataBaseConnector.quoteName("invoice_addres_id");
        query += " = " + this.address_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return dataBaseObject.loadValue(query, "used") > 0;
    }

    public void update(){
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

        String currentDate = JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"));

        String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address") + " SET " + JeproLabDataBaseConnector.quoteName("country_id") + " = ";
        query += this.country_id + ", " + JeproLabDataBaseConnector.quoteName("state_id") + " = " + this.state_id + ", " + JeproLabDataBaseConnector.quoteName("customer_id") + " = ";
        query += this.customer_id + ", " + JeproLabDataBaseConnector.quoteName("manufacturer_id") + " = " + this.manufacturer_id + ", " + JeproLabDataBaseConnector.quoteName("supplier_id");
        query += " = " + this.supplier_id + ", " + JeproLabDataBaseConnector.quoteName("warehouse_id") + " = " + this.warehouse_id + ", " + JeproLabDataBaseConnector.quoteName("alias");
        query += " = " + JeproLabDataBaseConnector.quote(this.alias) + ", " + JeproLabDataBaseConnector.quoteName("company") + " = " + JeproLabDataBaseConnector.quote(this.company) + ", ";
        query += JeproLabDataBaseConnector.quoteName("lastname") + " = " + JeproLabDataBaseConnector.quote(this.lastname) + ", " + JeproLabDataBaseConnector.quoteName("address1") + " = ";
        query += JeproLabDataBaseConnector.quote(this.address1) + ", " + JeproLabDataBaseConnector.quoteName("address2") + " = " + JeproLabDataBaseConnector.quote(this.address2) + ", ";
        query += JeproLabDataBaseConnector.quoteName("postcode") + " = " + JeproLabDataBaseConnector.quote(this.postcode) + ", " + JeproLabDataBaseConnector.quoteName("city") + " = ";
        query += JeproLabDataBaseConnector.quote(this.city) +  ", " + JeproLabDataBaseConnector.quoteName("other") + " = " + JeproLabDataBaseConnector.quote(this.other) + ", ";
        query += JeproLabDataBaseConnector.quoteName("phone") + " = " + JeproLabDataBaseConnector.quote(this.phone) + ", " + JeproLabDataBaseConnector.quoteName("phone_mobile") + " = ";
        query += JeproLabDataBaseConnector.quote(this.mobile_phone) + ", " + JeproLabDataBaseConnector.quoteName("firstname") + " = " + JeproLabDataBaseConnector.quote(this.firstname) + ", ";
        query += JeproLabDataBaseConnector.quoteName("vat_number") + " = " + JeproLabDataBaseConnector.quote(this.vat_number) + ", " + JeproLabDataBaseConnector.quoteName("date_upd")+ "  = ";
        query += currentDate + " WHERE " + JeproLabDataBaseConnector.quoteName("address_id") + " = " + this.address_id;
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, false);
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
            String query = "SELECT country." + JeproLabDataBaseConnector.quoteName("published") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
            query += " AS address LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_country") + " AS country ON country.";
            query += JeproLabDataBaseConnector.quoteName("country_id") + " = address." + JeproLabDataBaseConnector.quoteName("country_id") + " WHERE address.";
            query += JeproLabDataBaseConnector.quoteName("address_id") + " = " + addressId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            boolean result = dataBaseObject.loadValue(query, "published") > 0;
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }


    /**
     * Get zone id for a given address
     *
     * @param addressId Address id for which we want to get zone id
     * @return int Zone id
     */
    public static int getZoneIdByAddressId(int addressId){
        if (addressId <= 0) {
            return 0;
        }

        if (JeproLabAddressModel.zones_ids.containsKey(addressId)){
            return Integer.parseInt(JeproLabAddressModel.zones_ids.get(addressId).get("zone_id"));
        }

        /*int zoneId = Hook::exec('actionGetIDZoneByAddressID', array('id_address' => $id_address));

        if (zoneId > 0){
            Map<String, String> zoneSet = new HashMap<>();
            zoneSet.put("zone_id", zoneId);
            JeproLabAddressModel.zones_ids.put(addressId, zoneSet);
            return Integer.parseInt(JeproLabAddressModel.zones_ids.get(addressId).get("zone_id"));
        }*/

        String query = "SELECT state." + JeproLabDataBaseConnector.quoteName("zone_id") + " AS state_zone_id, country.";
        query += JeproLabDataBaseConnector.quoteName("zone_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
        query += " AS address LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_country") + " AS country ON country.";
        query += JeproLabDataBaseConnector.quoteName("country_id") + " = address." + JeproLabDataBaseConnector.quoteName("country_id");
        query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_state") + " AS state ON state.";
        query += JeproLabDataBaseConnector.quoteName("state_id") + " = address." + JeproLabDataBaseConnector.quoteName("state_id");
        query += " WHERE address." + JeproLabDataBaseConnector.quoteName("address_id") + " = " + addressId;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet zoneSet = dataBaseObject.loadObjectList(query);
        if(zoneSet != null){
            try{
                if(zoneSet.next()) {
                    JeproLabAddressModel.zones_ids.get(addressId).put("zone_id",
                        (zoneSet.getInt("state_zone_id") > 0) ? zoneSet.getString("state_zone_id") : zoneSet.getString("zone_id"));
                }
            }catch(SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }
        return Integer.parseInt(JeproLabAddressModel.zones_ids.get(addressId).get("zone_id"));
    }

    public static JeproLabAddressModel getAddressByCustomerId(int customerId, boolean main){
        if(main){
            return new JeproLabAddressModel(getCustomerFirstAddressId(customerId));
        }else {
            return new JeproLabAddressModel(getAddressIdBySCustomerId(customerId));
        }
    }

    /**
     * Returns address id for a given customer id
     *
     * @param customerId supplier id
     * @return int address id
     */
    public static int getAddressIdBySCustomerId(int customerId){
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("address_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
        query += " WHERE " + JeproLabDataBaseConnector.quoteName("customer_id") + " = " + customerId + " AND " + JeproLabDataBaseConnector.quoteName("deleted");
        query += " = 0 AND " + JeproLabDataBaseConnector.quoteName("published") + " = 0 AND " + JeproLabDataBaseConnector.quoteName("supplier_id") + " = 0 AND ";
        query += JeproLabDataBaseConnector.quoteName("manufacturer_id") + " = 0 AND " + JeproLabDataBaseConnector.quoteName("warehouse_id") + " = 0";
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return (int)dataBaseObject.loadValue(query, "address_id");
    }

    /**
     * Returns address id for a given supplier id
     *
     * @param supplierId supplier id
     * @return int address id
     */
    public static int getAddressIdBySupplierId(int supplierId){
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("address_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
        query += " WHERE " + JeproLabDataBaseConnector.quoteName("supplier_id") + " = " + supplierId + " AND " + JeproLabDataBaseConnector.quoteName("deleted");
        query += " = 0 AND " + JeproLabDataBaseConnector.quoteName("published") + " = 1 AND " + JeproLabDataBaseConnector.quoteName("customer_id") + " = 0 AND ";
        query += JeproLabDataBaseConnector.quoteName("manufacturer_id") + " = 0 AND " + JeproLabDataBaseConnector.quoteName("warehouse_id") + " = 0";
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return (int)dataBaseObject.loadValue(query, "address_id");
    }

    public static boolean aliasExist(String alias, int addressId, int customerId){
        String query = "SELECT COUNT(*) AS address FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address") + " WHERE ";
        query += JeproLabDataBaseConnector.quoteName("alias") + " = " + JeproLabDataBaseConnector.quote(alias) + " AND " ;
        query += JeproLabDataBaseConnector.quoteName("address_id") + " = " + addressId + " AND " + JeproLabDataBaseConnector.quoteName("customer_id");
        query += " = " + customerId + " AND " + JeproLabDataBaseConnector.quoteName("deleted") + " = 0";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return dataBaseObject.loadValue(query, "address") > 0;
    }

    public static int getCustomerFirstAddressId(int customerId){
        return getCustomerFirstAddressId(customerId, true);
    }

    public static int getCustomerFirstAddressId(int customerId, boolean active){
        if (customerId <= 0) {
            return 0;
        }
        String cacheKey = "jeprolab_address_getFirstCustomerAddressId_" + customerId + "_" + (active ? 1 : 0);
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("address_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("customer_id") + " = " + customerId + " AND " + JeproLabDataBaseConnector.quoteName("deleted");
            query += " = 0 " + (active ? " AND " + JeproLabDataBaseConnector.quoteName("published") + " = 1" : "");

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            int result = (int)dataBaseObject.loadValue(query, "address_id");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static List<JeproLabAddressModel> getAddresses(){
        return getAddresses(JeproLabContext.getContext().language.language_id);
    }

    public static List<JeproLabAddressModel> getAddresses(int langId){
        return getAddresses(langId, "address_id");
    }

    public static List<JeproLabAddressModel> getAddresses(int langId, String orderBy){
        return getAddresses(langId, orderBy, "ASC");
    }

    public static List<JeproLabAddressModel> getAddresses(int langId, String orderBy, String orderWay) {
        String orderByFilter = orderBy.replace("`", "");

        String query = "SELECT SQL_CALC_FOUND_ROWS address." + JeproLabDataBaseConnector.quoteName("address_id");
        query += ", address." + JeproLabDataBaseConnector.quoteName("firstname") + ", address." + JeproLabDataBaseConnector.quoteName("lastname");
        query += ", address." + JeproLabDataBaseConnector.quoteName("address1") + ", address." + JeproLabDataBaseConnector.quoteName("postcode");
        query += ", address." + JeproLabDataBaseConnector.quoteName("city") + ", country_lang." + JeproLabDataBaseConnector.quoteName("name");
        query += " AS country FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_address") + " AS address LEFT JOIN ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_country_lang") + " AS country_lang ON(country_lang.";
        query += JeproLabDataBaseConnector.quoteName("country_id") + " = address." + JeproLabDataBaseConnector.quoteName("country_id");
        query += " AND country_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") LEFT JOIN ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_customer") + " AS customer ON address." + JeproLabDataBaseConnector.quoteName("customer_id");
        query += " = customer." + JeproLabDataBaseConnector.quoteName("customer_id") + " WHERE address.customer_id != 0 ";
        query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER, "customer");
        query += " ORDER BY " + (orderByFilter.equals("address_id") ? "address." : "") + orderBy + " " + orderWay;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet addressesSet = dataBaseObject.loadObjectList(query);
        List<JeproLabAddressModel> addresses = new ArrayList<>();

        if (addressesSet != null) {
            try {
                JeproLabAddressModel address;
                while (addressesSet.next()) {
                    address = new JeproLabAddressModel();
                    address.address_id = addressesSet.getInt("address_id");
                    address.firstname = addressesSet.getString("firstname");
                    address.lastname = addressesSet.getString("lastname");
                    address.address1 = addressesSet.getString("address1");
                    address.postcode = addressesSet.getString("postcode");
                    address.city = addressesSet.getString("city");
                    address.country = addressesSet.getString("country");

                    addresses.add(address);
                }
            }catch(SQLException ignored) {
                ignored.printStackTrace();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }

        return addresses;
    }


    public class JeproLabStreetTypeModel {
    }
}
