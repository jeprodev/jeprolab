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
import java.util.HashMap;
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
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " AS address ";
                query += " WHERE address.address_id = " + addressId;

                dataBaseObject.setQuery(query);
                ResultSet addressSet = dataBaseObject.loadObjectList();
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
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("country_id") + ", " + dataBaseObject.quoteName("state_id");
            query += ", " + dataBaseObject.quoteName("vat_number") + ", " + dataBaseObject.quoteName("postcode") + " FROM ";
            query += dataBaseObject.quoteName("#__jeproLab_address") + " WHERE " + dataBaseObject.quoteName("address_id") + " = " + addressId;

            dataBaseObject.setQuery(query);
            result = dataBaseObject.loadObjectList();
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("address_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_address");
            query += " AS address WHERE " + dataBaseObject.quoteName("address_id") + " = " + addressId;

            dataBaseObject.setQuery(query);
            boolean addressExists = (int)dataBaseObject.loadValue("address_id") > 0;
            JeproLabCache.getInstance().store(cacheKey, addressExists);

            return addressExists;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }
}
