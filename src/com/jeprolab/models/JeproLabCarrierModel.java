package com.jeprolab.models;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCarrierModel extends JeproLabModel{
    public int carrier_id;

    /** @var int common id for carrier historization */
    public int reference_id;

    /** @var string Name */
    public String name;

    /** @var string URL with a '@' for */
    public String url;

    /** @var string Delay needed to deliver customer */
    public String delay;

    /** @var bool Carrier statues */
    public boolean published = true;

    /** @var bool True if carrier has been deleted (staying in database as deleted) */
    public boolean deleted = false;

    /** @var bool Active or not the shipping handling */
    public boolean shipping_handling = true;

    /** @var int Behavior taken for unknown range */
    public int range_behavior;

    /** @var bool Carrier module */
    public boolean is_module;

    /** @var bool Free carrier */
    public boolean is_free = false;

    /** @var int shipping behavior: by weight or by price */
    public int shipping_method = 0;

    /** @var bool Shipping external */
    public boolean shipping_external = false;

    /** @var string Shipping external */
    public String external_module_name = null;

    /** @var bool Need Range */
    public boolean need_range = false;

    /** @var int Position */
    public int position;

    /** @var int maximum package width managed by the transporter */
    public int max_width;

    /** @var int maximum package height managed by the transporter */
    public int max_height;

    /** @var int maximum package deep managed by the transporter */
    public int max_depth;

    /** @var int maximum package weight managed by the transporter */
    public int max_weight;

    /** @var int grade of the shipping delay (0 for longest, 9 for shortest) */
    public int grade;

    /**
     * getCarriers method filter
     */
    public static final int JEPROLAB_CARRIERS_ONLY = 1;
    public static final int JEPROLAB_CARRIERS_MODULE = 2;
    public static final int JEPROLAB_CARRIERS_MODULE_NEED_RANGE = 3;
    public static final int JEPROLAB_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE = 4;
    public static final int JEPROLAB_ALL_CARRIERS = 5;

    public static final int JEPROLAB_SHIPPING_METHOD_DEFAULT = 0;
    public static final int JEPROLAB_SHIPPING_METHOD_WEIGHT = 1;
    public static final int JEPROLAB_SHIPPING_METHOD_PRICE = 2;
    public static final int JEPROLAB_SHIPPING_METHOD_FREE = 3;

    public static final int JEPROLAB_SHIPPING_PRICE_EXCEPTION = 0;
    public static final int JEPROLAB_SHIPPING_WEIGHT_EXCEPTION = 1;
    public static final int JEPROLAB_SHIPPING_SIZE_EXCEPTION = 2;

    public static final int JEPROLAB_SORT_BY_PRICE = 0;
    public static final int JEPROLAB_SORT_BY_POSITION = 1;

    public static final int JEPROLAB_SORT_BY_ASC = 0;
    public static final int JEPROLAB_SORT_BY_DESC = 1;

    private static Map<String, Float> _price_by_weight = new HashMap<>();

    public JeproLabCarrierModel(){ this(0, 0); }
    
    public JeproLabCarrierModel(int carrierId){ this(carrierId, 0); }

    public JeproLabCarrierModel(int carrierId, int langId){
        parent::__construct($id, langId);

        if (this.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_DEFAULT) {
            this.shipping_method = ((int)Configuration::get('PS_SHIPPING_METHOD') ? JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT : JeproLabCarrierModel.SHIPPING_METHOD_PRICE);
        }


        if (this.carrier_id > 0) {
            this.tax_rules_group_id = this.getTaxRulesGroupId(JeproLabContext.getContext());
        }

        if (this.name == '0') {
            this.name = JeproLabCarrierModel.getCarrierNameFromLaboratoryName();
        }

        this.image_dir = _PS_SHIP_IMG_DIR_;
    }

    public boolean add($autodate = true, $null_values = false) {
        if (this.position <= 0) {
            this.position = JeproLabCarrierModel.getHigherPosition () + 1;
        }
        if (!parent::add ($autodate, $null_values)||!Validate::isLoadedObject (this)){
            return false;
        }
        if (!$count = Db::getInstance () -> getValue('SELECT count(")carier_id")) FROM ")'." + staticDataBaseObject.quoteName("#__jeprolab_. this.def['table'].
        '") WHERE ")deleted") = 0' )){
            return false;
        }
        if ($count == 1) {
            Configuration::updateValue ('PS_CARRIER_DEFAULT', (int) this.carrier_id);
        }

        // Register reference
        Db::getInstance () -> execute('UPDATE ")'." + staticDataBaseObject.quoteName("#__jeprolab_. this.def['table']. '") SET ")id_reference") = '. this.id.
        ' WHERE ")carier_id") = '. this.id);

        return true;
    }

    /**
     * @since 1.5.0
     * @see ObjectModel::delete()
     */
    public boolean delete() {
        if (!parent::delete ()){
            return false;
        }
        JeproLabCarrierModel.cleanPositions();
        return (Db::getInstance () -> execute('DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_carrier WHERE carier_id = '.
        (int) this.id)&&
        this.deleteTaxRulesGroup(JeproLabLaboratoryModel.getLaboratoryIds(true, null, true)));
    }

    /**
     * Change carrier id in delivery prices when updating a carrier
     *
     * @param oldId Old id carrier
     */
    public void setConfiguration(int oldId){
        Db::getInstance()->execute('UPDATE  " + staticDataBaseObject.quoteName("#__jeprolab_delivery") SET ")carier_id") = '.(int)this.id.' WHERE ")carier_id") = '.(int)$id_old);
    }

    /**
     * Get delivery prices for a given order
     *
     * @param totalWeight Total order weight
     * @param zoneId Zone ID (for customer delivery address)
     * @return float Delivery price
     */
    public float getDeliveryPriceByWeight(float totalWeight, int zoneId) {
        int carrierId = this.carrier_id;
        String cacheKey = carrierId + "_" + totalWeight + "_" + zoneId;
        if (!isset(JeproLabCarrierModel._price_by_weight[cacheKey])){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT delivery." + staticDataBaseObject.quoteName("price") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_delivery");
            query += " AS delivery LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_range_weight") + " AS range_weight ON (delivery.";
            query += staticDataBaseObject.quoteName("range_weight_id") + " = range_weight." + staticDataBaseObject.quoteName("range_weight_id");
            query += ") WHERE delivery." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + " AND " + totalWeight + " >= range_weight.";
            query += staticDataBaseObject.quoteName("delimiter1") + " AND " + totalWeight + " < range_weight." + staticDataBaseObject.quoteName("delimiter2");
            query += " AND delivery." + staticDataBaseObject.quoteName("carier_id")='.carrierId.'
            '.JeproLabCarrierModel.sqlDeliveryRangeShop(' range_weight ').'
            ORDER BY w." + staticDataBaseObject.quoteName("delimiter1")ASC ';
            $result = Db::getInstance (_PS_USE_SQL_SLAVE_) -> getRow($sql);
            if (!isset($result['price'])) {
                JeproLabCarrierModel.$price_by_weight[cacheKey]=this.getMaxDeliveryPriceByWeight(zoneId);
            } else {
                JeproLabCarrierModel.$price_by_weight[cacheKey]=$result['price'];
            }
        }

        $price_by_weight = Hook::exec
        ('actionDeliveryPriceByWeight', array('carier_id' = > carrierId, 'total_weight' =>$total_weight, 'zone_id' =>
        zoneId));
        if (is_numeric($price_by_weight)) {
            JeproLabCarrierModel._price_by_weight[cacheKey]=$price_by_weight;
        }

        return JeproLabCarrierModel._price_by_weight.get(cacheKey);
    }

    public static boolean checkDeliveryPriceByWeight(int carrierId, float totalWeight, int zoneId) {
        String cacheKey = carrierId + "_" + totalWeight + "_" + zoneId;
        if (!isset(JeproLabCarrierModel.$price_by_weight2[cacheKey])){
            $sql = 'SELECT d." + staticDataBaseObject.quoteName("price")
            FROM " + staticDataBaseObject.quoteName("#__jeprolab_ delivery")d
            LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_ range_weight")w ON d." + staticDataBaseObject.quoteName("id_range_weight")=w." + staticDataBaseObject.quoteName("id_range_weight")
            WHERE d." + staticDataBaseObject.quoteName("zone_id")='.(int)zoneId.'
            AND '.(float)$total_weight.' >= w." + staticDataBaseObject.quoteName("delimiter1")
            AND '.(float)$total_weight.' < w." + staticDataBaseObject.quoteName("delimiter2")
            AND d." + staticDataBaseObject.quoteName("carier_id")='.carrierId.'
            '.JeproLabCarrierModel.sqlDeliveryRangeShop(' range_weight ').'
            ORDER BY w." + staticDataBaseObject.quoteName("delimiter1")ASC ';
            $result = Db::getInstance (_PS_USE_SQL_SLAVE_) -> getRow($sql);
            JeproLabCarrierModel._price_by_weight2[cacheKey]=(isset($result['price']));
        }

        $price_by_weight = Hook::exec
        ('actionDeliveryPriceByWeight', array('carier_id' = > carrierId, 'total_weight' =>$total_weight, 'zone_id' =>
        zoneId));
        if (is_numeric($price_by_weight)) {
            JeproLabCarrierModel._price_by_weight2[cacheKey]=$price_by_weight;
        }

        return JeproLabCarrierModel._price_by_weight2[cacheKey];
    }


    public float getMaxDeliveryPriceByWeight(int zoneId){
        String cacheKey = 'JeproLabCarrierModel.getMaxDeliveryPriceByWeight_' + this.carrier_id + "_" + zoneId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $sql = 'SELECT d." + staticDataBaseObject.quoteName("price")
        FROM  " + staticDataBaseObject.quoteName("#__jeprolab_delivery") d
        INNER JOIN  " + staticDataBaseObject.quoteName("#__jeprolab_range_weight") w ON d." + staticDataBaseObject.quoteName("id_range_weight") = w." + staticDataBaseObject.quoteName("id_range_weight")
        WHERE d." + staticDataBaseObject.quoteName("zone_id") = '.(int)zoneId.'
        AND d." + staticDataBaseObject.quoteName("carier_id") = '.(int)this.carrier_id.'
        '.JeproLabCarrierModel.sqlDeliveryRangeShop('range_weight').'
        ORDER BY w." + staticDataBaseObject.quoteName("delimiter2") DESC';
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql);
        JeproLabCache.getInstance().store(cacheKey, $result);
        return $result;
    }
        return JeproLabCache.getInstance().retrieve(cacheKey);
    }


    public float getDeliveryPriceByPrice(float requestTotal, int zoneId){
        return getDeliveryPriceByPrice(requestTotal, zoneId, 0);
    }

    /**
     * Get delivery prices for a given order
     *
     * @param requestTotal Order total to pay
     * @param zoneId Zone id (for customer delivery address)
     * @param currencyId
     * @return float Delivery price
     */
    public float getDeliveryPriceByPrice(float requestTotal, int zoneId, int currencyId){
        int carrierId = this.carrier_id;
        String cacheKey = this.carrier_id + "_" + requestTotal + "_" + zoneId + "_" +  currencyId;
        if (!isset(JeproLabCarrierModel.$price_by_price[cacheKey])){
            if (!empty(currencyId)) {
                requestTotal = Tools::convertPrice (requestTotal, currencyId, false);
            }
            if(staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT delivery." + staticDataBaseObject.quoteName("price") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_delivery");
            query += " AS  delivery LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_range_price") + " AS range_price ON delivery.";
            query += staticDataBaseObject.quoteName("range_price_id") + " = range_price." + staticDataBaseObject.quoteName("range_price_id");
            query += " WHERE delivery." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + " AND " + requestTotal + " >= range_price.";
            query += staticDataBaseObject.quoteName("delimiter1") + " AND " + requestTotal + " < range_total." + staticDataBaseObject.quoteName("delimiter2");
            query += AND d." + staticDataBaseObject.quoteName("carier_id")='.carrierId.'
            '.JeproLabCarrierModel.sqlDeliveryRangeShop(' range_price ').'
            ORDER BY r." + staticDataBaseObject.quoteName("delimiter1")ASC ';
            $result = Db::getInstance (_PS_USE_SQL_SLAVE_) -> getRow($sql);
            if (!isset($result['price'])) {
                JeproLabCarrierModel.$price_by_price[cacheKey]=this -> getMaxDeliveryPriceByPrice(zoneId);
            } else {
                JeproLabCarrierModel.$price_by_price[cacheKey]=$result['price'];
            }
        }

        $price_by_price = Hook::exec('actionDeliveryPriceByPrice', array('carier_id' => carrierId, 'order_total' => requestTotal, 'zone_id' => zoneId));
        if (is_numeric($price_by_price)) {
            JeproLabCarrierModel._price_by_price.put(cacheKey, priceByPrice);
        }

        return JeproLabCarrierModel._price_by_price.get(cacheKey);
    }

    /**
     * Check delivery prices for a given order
     *
     * @param carrierId
     * @param requestTotal Order total to pay
     * @param zoneId Zone id (for customer delivery address)
     * @param currencyId
     * @return float Delivery price
     */
    public static function checkDeliveryPriceByPrice(int carrierId, float requestTotal, int zoneId, int currencyId = null) {
        String cacheKey = carrierId + "_" + requestTotal + "_" + zoneId + "_" + currencyId;
        if (!isset(JeproLabCarrierModel.$price_by_price2[cacheKey])){
            if (!empty(currencyId)) {
                requestTotal = Tools::convertPrice (requestTotal, currencyId, false);
            }
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT delivery." + staticDataBaseObject.quoteName("price") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_delivery");
            query += " AS delivery LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_range_price") + " AS range_price ON delivery.";
            query += staticDataBaseObject.quoteName("range_price_id") + " = range_price." + staticDataBaseObject.quoteName("range_price_id")
            WHERE d." + staticDataBaseObject.quoteName("zone_id")='.(int)zoneId.'
            AND '.(float)requestTotal.' >= r." + staticDataBaseObject.quoteName("delimiter1")
            AND '.(float)requestTotal.' < r." + staticDataBaseObject.quoteName("delimiter2")
            AND d." + staticDataBaseObject.quoteName("carier_id")='.carrierId.'
            '.JeproLabCarrierModel.sqlDeliveryRangeShop(' range_price ').'
            ORDER BY r." + staticDataBaseObject.quoteName("delimiter1")ASC ';
            $result = Db::getInstance (_PS_USE_SQL_SLAVE_) -> getRow($sql);
            JeproLabCarrierModel.$price_by_price2[cacheKey]=(isset($result['price']));
        }

        $price_by_price = Hook::exec ('actionDeliveryPriceByPrice', array('carier_id' = > carrierId, 'order_total' =>
        requestTotal, 'zone_id' =>zoneId));
        if (is_numeric($price_by_price)) {
            JeproLabCarrierModel.$price_by_price2[cacheKey]=$price_by_price;
        }

        return JeproLabCarrierModel.$price_by_price2[cacheKey];
    }

    public float getMaxDeliveryPriceByPrice(int zoneId) {
        String cacheKey = "jeprolab_carrier_model_get_max_delivery_price_by_price_" + this.carrier_id + "_" + zoneId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT delivery." + staticDataBaseObject.quoteName("price") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_delivery");
            query += " AS delivery INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_range_price") + " AS range_price ON delivery.";
            query += staticDataBaseObject.quoteName("range_price_id") + " = range_price." + staticDataBaseObject.quoteName("range_price_id");
            query += " WHERE delivery." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + " AND delivery." + staticDataBaseObject.quoteName("carrier_id");
            query += "= " + this.carrier_id + JeproLabCarrierModel.sqlDeliveryRangeLaboratory("range_price") + " ORDER BY range_price.";
            query += staticDataBaseObject.quoteName("delimiter2") + " DESC ";

            staticDataBaseObject.setQuery(query);
            float price = (float)staticDataBaseObject.loadValue("price");

            JeproLabCache.getInstance().store(cacheKey, price);
        }
        return (float)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Get delivery prices for a given shipping method (price/weight)
     *
     * @param rangeTable Table name (price or weight)
     * @return array Delivery prices
     */
    public static List<Map<String, Float>> getDeliveryPriceByRanges(String rangeTable, int carrierId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT delivery." + staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id");
        query += ", delivery." + staticDataBaseObject.quoteName("carrier_id") + ", delivery." + staticDataBaseObject.quoteName("zone_id");
        query += ", delivery." + staticDataBaseObject.quoteName("price") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_delivery");
        query += " AS delivery LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_" + staticDataBaseObject.quote(rangeTable) + "_id");
        query += " AS range_table ON range_table." + staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id") ;
        query += " = delivery." + staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id") + " WHERE delivery.";
        query += staticDataBaseObject.quoteName("carrier_id") + " = " + carrierId + " AND delivery." + staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id");.
        query += ") IS NOT NULL AND delivery." + staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id");
        query += " != 0 " + JeproLabCarrierModel.sqlDeliveryRangeLaboratory(rangeTable) + " ORDER BY range_table.delimiter1";

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();
        List<Map<String, Float>> pricesByRanges = new ArrayList<>();
        if(resultSet != null) {
            Map<String, Float> data;
            try {
                while(resultSet.next()){
                    data = new HashMap<>();
                    data.put(rangeTable + "_id", resultSet.getFloat(rangeTable + "_id"));
                    data.put("carrier_id", resultSet.getFloat("carrier_id"));
                    data.put("zone_id", resultSet.getFloat("zone_id"));
                    data.put("price", resultSet.getFloat("price"));

                    pricesByRanges.add(data);
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
        return pricesByRanges;
    }

    /**
     * Get all carriers in a given language
     *
     * @param langId Language id
     * @param $modules_filters, possible values:
    PS_CARRIERS_ONLY
    CARRIERS_MODULE
    CARRIERS_MODULE_NEED_RANGE
    PS_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE
    ALL_CARRIERS
     * @param bool $active Returns only active carriers when true
     * @return array Carriers
     */
    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active = false, boolean delete = false, int zoneId = false, $ids_group = null, int modulesFilters = JeproLabCarrierModel.CARRIERS_ONLY){
        // Filter by groups and no groups => return empty array
        if ($ids_group && (!is_array($ids_group) || !count($ids_group))) {
            return array();
        }

        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        String query = "SELECT carrier.*, carrier_lang.delay FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_carrier_lang") + " AS carrier_lang ON (carrier." + staticDataBaseObject.quoteName("carrier_id");
        query += "= carrier_lang." + staticDataBaseObject.quoteName("carrier_id") + " AND carrier_lang." + staticDataBaseObject.quoteName("lang_id") + " = ";
        query += langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("carrier_lang") + ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone");
        query += " AS carrier_zone ON (carrier_zone." + staticDataBaseObject.quoteName("carrier_id") + " = carrier." + staticDataBaseObject.quoteName("carrier_id") + ")";
        query += (zoneId > 0 ? "LEFT JOIN  " + staticDataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON (zone." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + ")" : "");
        query += JeproLabLaboratoryModel.addSqlAssociation("carrier") + " WHERE carrier." + staticDataBaseObject.quoteName("deleted") + " = " + (delete ? 1 : 0);

        if (active) {
            query += " AND carrier." + staticDataBaseObject.quoteName("published") + " = 1 ";
        }
        if (zoneId > 0) {
            query += " AND carrier_zone." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + " AND zone." + staticDataBaseObject.quoteName("published") + " = 1 ";
        }
        if ($ids_group) {
            query += " AND EXISTS (SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier_group")
            WHERE '." + staticDataBaseObject.quoteName("#__jeprolab_.'carrier_group.carier_id = c.carier_id
            AND id_group IN ('.implode(',', array_map('intval', $ids_group)).')) ';
        }

        switch (modulesFilters) {
            case 1 :
                query += " AND carrier." + staticDataBaseObject.quoteName("is_module") + " = 0 ";
                break;
            case 2 :
                query += " AND carrier." + staticDataBaseObject.quoteName("is_module") + " = 1 ";
                break;
            case 3 :
                query += " AND carrier." + staticDataBaseObject.quoteName("is_module") + " = 1 AND carrier.";
                query += staticDataBaseObject.quoteName("need_range") + " = 1 ";
                break;
            case 4 :
                query += " AND(carrier." + staticDataBaseObject.quoteName("is_module") + "= 0 OR carrier.";
                query += staticDataBaseObject.quoteName("need_range") + " = 1) + "= 0 OR c.need_range = 1) ';
                break;
        }
        query += " GROUP BY carrier." + staticDataBaseObject.quoteName("carrier_id") + " ORDER BY carrier." + staticDataBaseObject.quoteName("position") + " ASC";

        String cacheKey = 'JeproLabCarrierModel.getCarriers_'.md5($sql);
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            $carriers = Db::getInstance () -> executeS($sql);
            JeproLabCache.getInstance().store(cacheKey, $carriers);
        } else {
            $carriers = JeproLabCache.getInstance().retrieve(cacheKey);
        }

        foreach ($carriers as $key => $carrier){
            if ($carrier['name'] == '0') {
                $carriers[$key]['name'] = JeproLabCarrierModel.getCarrierNameFromLaboratoryName();
            }
        }
        return $carriers;
    }

    public static List getDeliveredCountries(int langId){
        return getDeliveredCountries(langId, false, false, false);
    }

    public static List getDeliveredCountries(int langId, boolean activeCountries){
        return getDeliveredCountries(langId, activeCountries, false, false);
    }

    public static List getDeliveredCountries(int langId, boolean activeCountries, boolean activeCarriers){
        return getDeliveredCountries(langId, activeCountries, activeCarriers, false);
    }

    public static List<JeproLabCountryModel> getDeliveredCountries(int langId, boolean activeCountries, boolean activeCarriers, boolean containsStates){
        String query = "SELECT state.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_state") + " AS state ORDER BY state.";
        query += staticDataBaseObject.quoteName("name") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet statesSet = staticDataBaseObject.loadObjectList();
        List<JeproLabCountryModel.JeproLabStateModel> states = new ArrayList<>();
        if(statesSet != null){
            try{
                JeproLabCountryModel.JeproLabStateModel state;
                while(statesSet.next()){
                    state = new JeproLabCountryModel.JeproLabStateModel();
                    //state.state_id = statesSet.getInt("state_id");
                    states.add(state);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }
        }

        //$result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
        query = "SELECT country_lang.*, country.*, country_lang." + staticDataBaseObject.quoteName("name") + " AS country_name, zone.";
        query += staticDataBaseObject.quoteName("name") + " AS zone_name FROM  " + staticDataBaseObject.quoteName("#__jeprolab_country") ;
        query += " AS country " + JeproLabLaboratoryModel.addSqlAssociation("country") + " LEFT JOIN  " + staticDataBaseObject.quoteName("#__jeprolab_country_lang");
        query += " AS country_lang ON (country." + staticDataBaseObject.quoteName("country_id") + " = country_lang." + staticDataBaseObject.quoteName("country_id");
        query += " AND country_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") INNER JOIN ( " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") ;
        query += " AS carrier_zone INNER JOIN  " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier ON (carrier.";
        query += staticDataBaseObject.quoteName("carrier_id") + " = carrier_zone." + staticDataBaseObject.quoteName("carrier_id") + " AND carrier.";
        query += staticDataBaseObject.quoteName("deleted") + " = 0 " + (activeCarriers ? "AND carrier." + staticDataBaseObject.quoteName("published") + " = 1) " : ") ");
        query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON carrier_zone." + staticDataBaseObject.quoteName("zone_id");
        query += " = zone." + staticDataBaseObject.quoteName("zone_id") + " ON zone." + staticDataBaseObject.quoteName("zone_id") + " = country.";
        query += staticDataBaseObject.quoteName("zone_id") + " WHERE 1 " + (activeCountries ? " AND country." + staticDataBaseObject.quoteName("published") + " = 1" : "");
        query += (containsStates ? " AND country." + staticDataBaseObject.quoteName("contains_states") + " = 1 " : "") + " ORDER BY country_lang.name ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet countriesSet = staticDataBaseObject.loadObjectList();

        List<JeproLabCountryModel> countries = new ArrayList<>();

        if(countriesSet != null) {
            try {
                JeproLabCountryModel country;
                while(countriesSet.next()){
                    country = new JeproLabCountryModel();
                    if(containsStates) {
                        for (JeproLabCountryModel.JeproLabStateModel state : states) {
                            if ((country.country_id == state.country_id) && state.published && country.states.contains(state)) {
                                country.states.add(state);
                            }
                        }
                    }
                    countries.add(country);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }

        return countries;
    }

    public static int getDefaultCarrierSelection(List carriers) {
        return getDefaultCarrierSelection(carriers, 0);
    }

    /**
     * Return the default carrier to use
     *
     * @param carriers
     * @param defaultCarrierId the last carrier selected
     * @return number the id of the default carrier
     */
    public static int getDefaultCarrierSelection(List<JeproLabCarrierModel> carriers, int defaultCarrierId){
        if (carriers == null || carriers.size() == 0) {
            return 0;
        }

        if(defaultCarrierId != 0) {
            for(JeproLabCarrierModel carrier : carriers){
                if (carrier.carrier_id == defaultCarrierId) {
                    return carrier.carrier_id;
                }
            }
        }

        for(JeproLabCarrierModel carrier : carriers) {
            if (carrier.carrier_id == JeproLabSettingModel.getIntValue("default_carrier")){
                return carrier.carrier_id;
            }
        }

        return (int)carriers.get(0).carrier_id;
    }

    public static function getCarriersForRequest(int zoneId){
        return getCarriersForRequest(zoneId, null, null, new ArrayList<String>());
    }

    public static function getCarriersForRequest(int zoneId, List<Integer> groups){
        return getCarriersForRequest(zoneId, groups, null, new ArrayList<String>());
    }

    public static function getCarriersForRequest(int zoneId, List<Integer> groups, JeproLabCartModel cart){
        return getCarriersForRequest(zoneId, groups, cart, new ArrayList<String>());
    }

    /**
     *
     * @param zoneId
     * @param groups group of the customer
     * @param array &$error contain an error message if an error occurs
     * @return Array
     */
    public static function getCarriersForRequest(int zoneId, List<Integer> groups, JeproLabCartModel cart, List<String> error){
        JeproLabContext context = JeproLabContext.getContext();
        int langId = context.language.language_id;
        if (cart == null){
            cart = context.cart;
        }
        int currencyId = 0;
        if(context.currency != null){
            currencyId = context.currency.currency_id;
        }

        List<JeproLabCarrierModel> carriers;
        if (groups.size() > 0) {
            carriers = JeproLabCarrierModel.getCarriers(langId, true, false, zoneId, groups, JeproLabCarrierModel.JEPROLAB_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE);
        } else {
            groups = new ArrayList<>();
            groups.add(JeproLabSettingModel.getIntValue("unidentified_group"));
            carriers = JeproLabCarrierModel.getCarriers(langId, true, false, zoneId, groups, JeproLabCarrierModel.JEPROLAB_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE);
        }
        $results_array = array();

        int shippingMethod;
        for(JeproLabCarrierModel carrier : carriers){
            //$carrier = new Carrier((int) $row['carier_id']);
            shippingMethod = carrier.getShippingMethod();
            if (shippingMethod != JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_FREE) {
                // Get only carriers that are compliant with shipping method
                if ((shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT && !carrier.getMaxDeliveryPriceByWeight(zoneId))){
                    $error[$carrier -> id] = JeproLabCarrierModel.JEPROLAB_SHIPPING_WEIGHT_EXCEPTION;
                    unset($result[$k]);
                    continue;
                }
                if ((shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE && !carrier.getMaxDeliveryPriceByPrice(zoneId))){
                    $error[$carrier -> id] = JeproLabCarrierModel.JEPROLAB_SHIPPING_PRICE_EXCEPTION;
                    unset($result[$k]);
                    continue;
                }

                // If out-of-range behavior carrier is set on "Desactivate carrier"
                if ($row['range_behavior']) {
                    // Get id zone
                    if (zoneId <= 0) {
                        zoneId = JeproLabCountryModel.getZoneId(JeproLabCountryModel.getDefaultCountryId());
                    }

                    // Get only carriers that have a range compatible with cart
                    if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT
                            && (!JeproLabCarrierModel.checkDeliveryPriceByWeight(carrier.carrier_id, cart.getTotalWeight(), zoneId))){
                        $error[$carrier -> id] = JeproLabCarrierModel.JEPROLAB_SHIPPING_WEIGHT_EXCEPTION;
                        unset($result[$k]);
                        continue;
                    }

                    if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE
                            && (!JeproLabCarrierModel.checkDeliveryPriceByPrice(carrier.carrier_id, cart.getRequestTotal(true, JeproLabCartModel.BOTH_WITHOUT_SHIPPING), zoneId, currencyId)))
                    {
                        $error[$carrier -> id] = JeproLabCarrierModel.SHIPPING_PRICE_EXCEPTION;
                        unset($result[$k]);
                        continue;
                    }
                }
            }

            $row['name'] = (strval($row['name']) != '0' ? $row['name'] : JeproLabCarrierModel.getCarrierNameFromLaboratoryName());
            $row['price'] = ((shippingMethod == JeproLabCarrierModel.SHIPPING_METHOD_FREE) ? 0 : $cart -> getPackageShippingCost((int) $row['carier_id'], true, null, null, zoneId));
            $row['price_tax_exc'] = ((shippingMethod == JeproLabCarrierModel.SHIPPING_METHOD_FREE) ? 0 : $cart -> getPackageShippingCost((int) $row['carier_id'], false, null, null, zoneId));
            $row['img'] = file_exists(_PS_SHIP_IMG_DIR_. (int) $row['carier_id']. '.jpg' )?_THEME_SHIP_DIR_.
            (int) $row['carier_id']. '.jpg':'';

            // If price is false, then the carrier is unavailable (carrier module)
            if ($row['price'] == = false) {
                unset($result[$k]);
                continue;
            }
            $results_array[]=$row;
        }

        // if we have to sort carriers by price
        $prices = array();
        if (Configuration::get('PS_CARRIER_DEFAULT_SORT') == JeproLabCarrierModel.JEPROLAB_SORT_BY_PRICE) {
        foreach ($results_array as $r) {
            $prices[] = $r['price'];
        }
        if (Configuration::get('PS_CARRIER_DEFAULT_ORDER') == JeproLabCarrierModel.JEPROLAB_SORT_BY_ASC){
            array_multisort($prices, SORT_ASC, SORT_NUMERIC, $results_array);
        } else {
            array_multisort($prices, SORT_DESC, SORT_NUMERIC, $results_array);
        }
    }

        return $results_array;
    }

    public static boolean checkCarrierZone(int carrierId, int zoneId){
        String cacheKey = "jeprolab_carrier_model_check_carrier_zone_" + carrierId + "_" +zoneId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }
            String query = "SELECT carrier." + staticDataBaseObject.quoteName("carrier_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier");
            query += " AS carrier LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone")  + " AS carrier_zone ON (carrier_zone.";
            query += staticDataBaseObject.quoteName("carrier_id") + " = carrier." + staticDataBaseObject.quoteName("carrier_id") + ") LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON (zone." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId;
            query += ") WHERE carrier." + staticDataBaseObject.quoteName("carrier_id") + " = " + carrierId + " AND carrier." ;
            query += staticDataBaseObject.quoteName("deleted") + " = 0 AND carrier." + staticDataBaseObject.quoteName("published") + " = 1 AND carrier_zone.";
            query += staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + " AND zone." + staticDataBaseObject.quoteName("published") + " = 1 ";

            staticDataBaseObject.setQuery(query);
            JeproLabCache.getInstance().store(cacheKey, staticDataBaseObject.loadValue("carrier_id") > 0);
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Get all zones
     *
     * @return array Zones
     */
    public List<JeproLabCountryModel.JeproLabZoneModel> getZones(){
        //return Db::getInstance()->executeS('
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + " AS carrier_zone LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON carrier_zone." + staticDataBaseObject.quoteName("zone_id");
        query += " = zone." + staticDataBaseObject.quoteName("zone_id") + " WHERE carrier_zone." + staticDataBaseObject.quoteName("carrier_id");
        query += " = " + this.carrier_id;

        staticDataBaseObject.setQuery(query);
        ResultSet zonesSet = staticDataBaseObject.loadObjectList();
        List<JeproLabCountryModel.JeproLabZoneModel> zones = new ArrayList<>();

        if(zonesSet != null){
            JeproLabCountryModel.JeproLabZoneModel zone;
            try{
                while(zonesSet.next()){
                    zone = new JeproLabCountryModel.JeproLabZoneModel();
                    zones.add(zone);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return zones;
    }

    /**
     * Get a specific zones
     *
     * @return array Zone
     */
    public JeproLabCountryModel.JeproLabZoneModel getZone(int zoneId){
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }
        //return Db::getInstance()->executeS('
        String query = "SELECT * FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + " WHERE ";
        query += staticDataBaseObject.quoteName("carrier_id") + " = " + this.carrier_id + " AND " ;
        query += staticDataBaseObject.quoteName("zone_id") + " = " + zoneId;
    }

    /**
     * Gets the highest carrier position
     *
     * @since 1.5.0
     * @return int $position
     */
    public static int getHigherPosition(){
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }
        String query = "SELECT MAX(" + staticDataBaseObject.quoteName("position") + " AS pos FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier");
        query += " WHERE " + staticDataBaseObject.quoteName("deleted") + " = 0 ";

        staticDataBaseObject.setQuery(query);
        int position = (int)staticDataBaseObject.loadValue("pos");
        return (position > 0) ? position : -1;
    }

    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId){
        return getAvailableCarrierList(analyze, warehouseId, deliveryAddressId, 0, null, new ArrayList<>());
    }

    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId, int labId){
        return getAvailableCarrierList(analyze, warehouseId, deliveryAddressId, labId, null, new ArrayList<>());
    }

    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId, int labId, JeproLabCartModel cart){
        return getAvailableCarrierList(analyze, warehouseId, deliveryAddressId, labId, cart, new ArrayList<>());
    }

    /**
     * For a given {product, warehouse}, gets the carrier available
     *
     * @param analyze The id of the product, or an array with at least the package size and weight
     * @param warehouseId
     * @param deliveryAddressId
     * @param labId
     * @param cart
     * @param array   &$error  contain an error message if an error occurs
     * @return array
     * @throws PrestaShopDatabaseException
     */
    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId, int labId, JeproLabCartModel cart, List<Integer> errors){
        static $ps_country_default = null;

        if ($ps_country_default === null) {
            JeproLabCarrierModel.defaul_country_id = Configuration::get('PS_COUNTRY_DEFAULT');
        }

        if (is_null(labId)) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        if (cart == null) {
            cart = JeproLabContext.getContext().cart;
        }

        if (is_null($error) || !is_array($error)) {
            $error = array();
        }

        int addressId = ((deliveryAddressId != 0) ? deliveryAddressId :  cart.delivery_address_id);
        int zoneId;
        if (addressId > 0) {
            zoneId = JeproLabAddressModel.getZoneIdByAddressId(addressId);

            // Check the country of the address is activated
            if (!JeproLabAddressModel.isCountryActiveByAddressId(addressId)) {
                return array();
            }
        } else {
            JeproLabCountryModel country = new JeproLabCountryModel($ps_country_default);
            zoneId = country.zone_id;
        }

        // Does the product is linked with carriers?
        String cacheKey = 'JeproLabCarrierModel.getAvailableCarrierList_'.(int)$product->id.'-'.(int)labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $query = new DbQuery();
        $query->select('carier_id');
        $query->from('product_carrier', 'pc');
        $query->innerJoin(
                'carrier',
                'c',
                'c.id_reference = pc.carier_id_reference AND c.deleted = 0 AND c.active = 1'
        );
        $query->where('pc.id_product = '.(int)$product->id);
        $query->where('pc.id_shop = '.(int)labId);

        $carriers_for_product = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
        JeproLabCache.getInstance().store(cacheKey, $carriers_for_product);
    } else {
        $carriers_for_product = JeproLabCache.getInstance().retrieve(cacheKey);
    }

        $carrier_list = array();
        if (!empty($carriers_for_product)) {
            //the product is linked with carriers
            foreach ($carriers_for_product as $carrier) { //check if the linked carriers are available in current zone
                if (JeproLabCarrierModel.checkCarrierZone(carrier.carrier_id, zoneId)) {
                    $carrier_list[carrier.carrier_id] = carrier.carrier_id;
                }
            }
            if (empty($carrier_list)) {
                return array();
            }//no linked carrier are available for this zone
        }

        // The product is not dirrectly linked with a carrier
        // Get all the carriers linked to a warehouse
        if ($id_warehouse) {
            $warehouse = new Warehouse($id_warehouse);
            $warehouse_carrier_list = $warehouse->getCarriers();
        }

        $available_carrier_list = array();
        cacheKey = 'JeproLabCarrierModel.getAvailableCarrierList_getCarriersForOrder_'.(int)zoneId.'-'.(int)$cart->id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $customer = new Customer($cart->id_customer);
        $carrier_error = array();
        $carriers = JeproLabCarrierModel.getCarriersForOrder(zoneId, $customer->getGroups(), $cart, $carrier_error);
        JeproLabCache.getInstance().store(cacheKey, array($carriers, $carrier_error));
    } else {
        list($carriers, $carrier_error) = JeproLabCache.getInstance().retrieve(cacheKey);
    }

        $error = array_merge($error, $carrier_error);

        foreach ($carriers as $carrier) {
        $available_carrier_list[carrier.carrier_id] = carrier.carrier_id;
    }

        if ($carrier_list) {
            $carrier_list = array_intersect($available_carrier_list, $carrier_list);
        } else {
            $carrier_list = $available_carrier_list;
        }

        if (isset($warehouse_carrier_list)) {
            $carrier_list = array_intersect($carrier_list, $warehouse_carrier_list);
        }

        $cart_quantity = 0;
        $cart_weight = 0;

        foreach ($cart->getProducts(false, false) as $cart_product) {
        if ($cart_product['id_product'] == $product->id) {
            $cart_quantity += $cart_product['cart_quantity'];
        }
        if (isset($cart_product['weight_attribute']) && $cart_product['weight_attribute'] > 0) {
            $cart_weight += ($cart_product['weight_attribute'] * $cart_product['cart_quantity']);
        } else {
            $cart_weight += ($cart_product['weight'] * $cart_product['cart_quantity']);
        }
    }

        if ($product->width > 0 || $product->height > 0 || $product->depth > 0 || $product->weight > 0 || $cart_weight > 0) {
            foreach ($carrier_list as $key => carrierId) {
                $carrier = new Carrier(carrierId);

                // Get the sizes of the carrier and the product and sort them to check if the carrier can take the product.
                $carrier_sizes = array((int)$carrier->max_width, (int)$carrier->max_height, (int)$carrier->max_depth);
                $product_sizes = array((int)$product->width, (int)$product->height, (int)$product->depth);
                rsort($carrier_sizes, SORT_NUMERIC);
                rsort($product_sizes, SORT_NUMERIC);

                if (($carrier_sizes[0] > 0 && $carrier_sizes[0] < $product_sizes[0])
                        || ($carrier_sizes[1] > 0 && $carrier_sizes[1] < $product_sizes[1])
                        || ($carrier_sizes[2] > 0 && $carrier_sizes[2] < $product_sizes[2])) {
                    $error[$carrier->id] = JeproLabCarrierModel.SHIPPING_SIZE_EXCEPTION;
                    unset($carrier_list[$key]);
                }

                if ($carrier->max_weight > 0 && ($carrier->max_weight < $product->weight * $cart_quantity || $carrier->max_weight < $cart_weight)) {
                    $error[$carrier->id] = JeproLabCarrierModel.SHIPPING_WEIGHT_EXCEPTION;
                    unset($carrier_list[$key]);
                }
            }
        }
        return $carrier_list;
    }

    /**
     * Assign one (ore more) group to all carriers
     *
     * @since 1.5.0
     * @param int|array $id_group_list group id or list of group ids
     * @param array $exception list of id carriers to ignore
     */
    public static boolean assignGroupToAllCarriers($id_group_list, $exception = null)
    {
        if (!is_array($id_group_list)) {
            $id_group_list = array($id_group_list);
        }

        Db::getInstance()->execute('
            DELETE FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier_group")
        WHERE ")id_group") IN ('.join(',', $id_group_list).')');

        $carrier_list = Db::getInstance()->executeS('
            SELECT carier_id FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier")
        WHERE deleted = 0
        '.(is_array($exception) ? 'AND carier_id NOT IN ('.join(',', $exception).')' : ''));

        if ($carrier_list) {
            $data = array();
            foreach ($carrier_list as $carrier) {
                foreach ($id_group_list as $id_group) {
                    $data[] = array(
                            'carier_id' => carrier.carrier_id,
                            'id_group' => $id_group,
                    );
                }
            }
            return Db::getInstance()->insert('carrier_group', $data, false, false, Db::INSERT);
        }

        return true;
    }

    public function setGroups($groups){
        setGroups(groups, true);
    }
    
    public function setGroups($groups, boolean delete){
        if (delete) {
            Db::getInstance()->execute('DELETE FROM '." + staticDataBaseObject.quoteName("#__jeprolab_.'carrier_group WHERE carier_id = '.(int)this.carrier_id);
        }
        if (!is_array($groups) || !count($groups)) {
            return true;
        }
        $sql = 'INSERT INTO '." + staticDataBaseObject.quoteName("#__jeprolab_.'carrier_group (carier_id, id_group) VALUES ';
        foreach ($groups as $id_group) {
            $sql. = '('. (int) this -> id. ', '. (int) $id_group. '),';
        }

        return Db::getInstance()->execute(rtrim($sql, ','));
    }

    /**
     * Return the carrier name from the shop name (e.g. if the carrier name is '0').
     *
     * The returned carrier name is the shop name without '#' and ';' because this is not the same validation.
     *
     * @return string Carrier name
     */
    public static String getCarrierNameFromLaboratoryName(){
        return str_replace(
                array('#', ';'),
                '',
                Configuration::get('PS_SHOP_NAME')
        );
    }

    /**
     * Add zone
     */
    public boolean addZone(int zoneId) {
        if (staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + "( ";
        query += staticDataBaseObject.quoteName("carrier_id") + ", " + staticDataBaseObject.quoteName("zone_id");
        query += ") VALUES (" + this.carrier_id + ", " + zoneId + ") ";
        staticDataBaseObject.setQuery(query);
        if (staticDataBaseObject.query(true)) {
            // Get all ranges for this carrier
            List rangesPrice = JeproLabRangePrice.getRanges(this.carrier_id);
            List rangesWeight = JeproLabRangeWeight.getRanges(this.carrier_id);
            // Create row in ps_delivery table
            if (rangesPrice.size() > 0 || rangesWeight.size() > 0) {
                query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_delivery") + "(" + staticDataBaseObject.quoteName("carrier_id");
                query += ", " + staticDataBaseObject.quoteName("range_price_id") + ", " + staticDataBaseObject.quoteName("range_weight_id") + ", ";
                query += staticDataBaseObject.quoteName("zone_id") + ", " + staticDataBaseObject.quoteName("price") + ") VALUES ";
                if (rangesPrice.size() > 0) {
                    foreach($ranges_price as $range) {
                        $sql. = '('. (int) this.carrier_id. ', '. (int) $range['id_range_price']. ', 0, '. (int) zoneId.
                        ', 0),';
                    }
                }

                if (rangesWeight.size() > 0){
                    foreach($ranges_weight as $range) {
                        $sql. = '('. (int) this.carrier_id. ', 0, '. (int) $range['id_range_weight']. ', '.
                        (int) zoneId. ', 0),';
                    }
                }
                $sql = rtrim($sql, ',' );
                staticDataBaseObject.setQuery(query);
                return staticDataBaseObject.query(false);
            }
            return true;
        }
        return false;
    }

    /**
     * Delete zone
     */
    public boolean deleteZone(int zoneId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + " WHERE " + staticDataBaseObject.quoteName("carrier_id") ;
        query += " = " + this.carrier_id + " AND " + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + " LIMIT 1";

        staticDataBaseObject.setQuery(query);
        if (staticDataBaseObject.query(false)){
            query = "DELETE FROM  " + staticDataBaseObject.quoteName("#__jeprolab_delivery") + " WHERE " + staticDataBaseObject.quoteName("carrier_id");
            query += " = " + this.carrier_id + " AND " + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId;

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }
        return false;
    }

    /**
     * Gets a specific group
     *
     * @since 1.5.0
     * @return array Group
     */
    public List<Integer> getGroups(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier_group");
        query += " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + this.carrier_id;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();
        List<Integer> groups = new ArrayList<>();
        if(resultSet != null){
            try{
                while(resultSet.next()){
                    groups.add(resultSet.getInt("group_id"));
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }
        }
        return groups;
    }

    /**
     * Clean delivery prices (weight/price)
     *
     * @param rangeTable Table name to clean (weight or price according to shipping method)
     * @return bool Deletion result
     */
    public boolean deleteDeliveryPrice(String rangeTable) {
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String where = " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + this.carrier_id + " AND " ;
        where += staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable + "_id")) + ") IS NOT NULL OR ";
        where += staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id") + " = 0) ";

        if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.ALL_CONTEXT){
            where += " AND " + staticDataBaseObject.quoteName("lab_id") + " IS NULL AND " + staticDataBaseObject.quoteName("lab_group_id") + " IS NULL";
        }else if(JeproLabLaboratoryModel.getLabContext () == JeproLabLaboratoryModel.GROUP_CONTEXT){
            where += " AND " + staticDataBaseObject.quoteName("lab_id") + " IS NULL AND " + staticDataBaseObject.quoteName("lab_group_id");
            where += " = " + JeproLabLaboratoryModel.getContextLabGroupId();
        }else{
            where += " AND " + staticDataBaseObject.quoteName("lab_id") + " = " + JeproLabLaboratoryModel.getContextLabId();
        }

        String query = "DELETE  " + staticDataBaseObject.quoteName("#__jeprolab_delivery") + where;

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    /**
     * Add new delivery prices
     *
     * @param array $priceList Prices list in multiple arrays (changed to array since 1.5.0)
     * @return bool Insertion result
     */
    public boolean addDeliveryPrice($price_list, $delete = false) {
        if (!$price_list) {
            return false;
        }
        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_delivery");

        $keys = array_keys($price_list[0]);
        if (!in_array('id_shop', $keys)) {
            $keys[]='id_shop';
        }
        if (!in_array('id_shop_group', $keys)) {
            $keys[]='id_shop_group';
        }

        query =  ('.implode(', ', $keys).
        ') VALUES ';
        foreach($price_list as $values) {
            if (!isset($values['id_shop'])) {
                $values['id_shop'] = (JeproLabLaboratoryModel.getContext () == JeproLabLaboratoryModel.CONTEXT_SHOP)?JeproLabLaboratoryModel.getContextShopID ():null;
            }
            if (!isset($values['id_shop_group'])) {
                $values['id_shop_group'] = (JeproLabLaboratoryModel.getContext () != JeproLabLaboratoryModel.CONTEXT_ALL)?JeproLabLaboratoryModel.getContextShopGroupID ():
                null;
            }

            if ($delete) {
                Db::getInstance () -> execute(
                        'DELETE FROM  " + staticDataBaseObject.quoteName("#__jeprolab_delivery")
                        WHERE '.(is_null($values[' id_shop ']) ? ' ISNULL(")id_shop")) ' : ' id_shop = '.(int)$values['
                id_shop ']).'
                AND '.(is_null($values[' id_shop_group ']) ? ' ISNULL(")id_shop")) ' : ' id_shop_group = '.(int)$values['
                id_shop_group ']).'
                AND carier_id = '.(int)$values[' carier_id '].
                ($values['id_range_price'] != = null ? ' AND id_range_price='. (int) $values['id_range_price']:
                ' AND (ISNULL(")id_range_price")) OR ")id_range_price") = 0)' ).
                ($values['id_range_weight'] != = null ? ' AND id_range_weight='. (int) $values['id_range_weight']:
                ' AND (ISNULL(")id_range_weight")) OR ")id_range_weight") = 0)' ).'
                AND zone_id = '.(int)$values[' zone_id ']
                );
            }

            $sql. = '(';
            foreach($values as $v) {
                if (is_null($v)) {
                    $sql. = 'NULL';
                } elseif(is_int($v) || is_float($v)) {
                    $sql. = $v;
                }else{
                    $sql. = '\''.$v. '\'';
                }
                $sql. = ', ';
            }
            $sql = rtrim($sql, ', ' ). '), ';
        }
        $sql = rtrim($sql, ', ' );
        return Db::getInstance () -> execute($sql);
    }

    /**
     * Copy old carrier information when update carrier
     *
     * @param oldId Old id carrier (copy from that id)
     */
    public boolean copyCarrierData(int oldId){
        if (this.carrier_id <= 0 || oldId <= 0) {
            return false;
        }
        String query;
        ResultSet resultSet;
        int rangeId;

        String oldLogoPath = JeproLabConfigurationSettings.JEPROLAB_SHIPPING_IMAGE_DIRECTORY + File.separator + oldId + ".jpg";
        if (file_exists($old_logo)) {
        copy($old_logo, _PS_SHIP_IMG_DIR_.'/'.(int)this.carrier_id + ".jpg");
        }

        $old_tmp_logo = _PS_TMP_IMG_DIR + "carrier_mini_" + " + oldId + ".jpg"";
        if (file_exists($old_tmp_logo)) {
        if (!isset($_FILES['logo'])) {
        copy($old_tmp_logo, _PS_TMP_IMG_DIR_.'/carrier_mini_'.this.carrier_id.'.jpg');
        }
        unlink($old_tmp_logo);
        }

        // Copy existing ranges price
        List<String> rangeTypes = new ArrayList<>();
        rangeTypes.add("range_price");
        rangeTypes.add("range_weight");
        String rangePriceId, rangeWeightId;

        for(String rangeType : rangeTypes){
        //$res = Db::getInstance()->executeS('
            query = "SELECT " + staticDataBaseObject.quoteName(rangeType + "_id") + " AS range_id, " + staticDataBaseObject.quoteName("delimiter_1");
            query += ", " + staticDataBaseObject.quoteName("delimiter_2") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_" + rangeType);
            query += " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + oldId;

            staticDataBaseObject.setQuery(query);
            resultSet = staticDataBaseObject.loadObjectList();
            if(resultSet != null){
                try {
                    while(resultSet.next()){
                        query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_" + rangeType) + "(" + staticDataBaseObject.quoteName("carrier_id");
                        query += ", " + staticDataBaseObject.quoteName("delimiter_1") + ", " + staticDataBaseObject.quoteName("delimiter_2") + ") VALUES (";
                        query += this.carrier_id + ", " + resultSet.getFloat("delimiter_1") + ", " + resultSet.getFloat("delimiter_2") + ")";

                        staticDataBaseObject.setQuery(query);
                        staticDataBaseObject.query(true);
                        rangeId = staticDataBaseObject.getGeneratedKey();

                        rangePriceId = (rangeType.equals("range_price")) ? String.valueOf(rangeId) : "NULL";
                        rangeWeightId = (rangeType.equals("range_weight")) ? String.valueOf(rangeId) : "NULL";

                        query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_delivery") + "(" + staticDataBaseObject.quoteName("carrier_id");
                        query += ", " + staticDataBaseObject.quoteName("lab_id") + ", " + staticDataBaseObject.quoteName("lab_group_id") + ", ";
                        query += staticDataBaseObject.quoteName("range_price_id") + ", " + staticDataBaseObject.quoteName("range_weight_id") + ", ";
                        query += staticDataBaseObject.quoteName("zone_id") + ", " + staticDataBaseObject.quoteName("price") + ") VALUES ( SELECT ";
                        query += staticDataBaseObject.quoteName("carrier_id") + ", " + staticDataBaseObject.quoteName("lab_id") + ", ";
                        query += staticDataBaseObject.quoteName("lab_group_id") + ", " + rangePriceId + ", " + rangeWeightId + ", ";
                        query += staticDataBaseObject.quoteName("zone_id") + ", " + staticDataBaseObject.quoteName("price") + " FROM ";
                        query += staticDataBaseObject.quoteName("#__jeprolab_delivery") + " WHERE " + staticDataBaseObject.quoteName("carrier_id");
                        query += " = " + oldId + " AND " + staticDataBaseObject.quoteName(rangeType + "_id") + " = " + resultSet.getInt("range_id") + ") ";

                        staticDataBaseObject.setQuery(query);
                        staticDataBaseObject.query(false);
                    }
                }catch(SQLException ignored){
                    ignored.printStackTrace();
                }
            }
        }

        // Copy existing zones
        query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + " WHERE " ;
        query += staticDataBaseObject.quoteName("carrier_id") + " = " + oldId;

        staticDataBaseObject.setQuery(query);
        resultSet = staticDataBaseObject.loadObjectList();
        if(resultSet != null){
            try{
                while(resultSet.next()) {
                    query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + "(" + staticDataBaseObject.quoteName("carrier_id");
                    query += ", " + staticDataBaseObject.quoteName("zone_id") + ") VALUES (" + this.carrier_id + ", " + resultSet.getInt("'zone_id") + ")";

                    staticDataBaseObject.setQuery(query);
                    staticDataBaseObject.query(false);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }

        //Copy default carrier
        if(JeproLabSettingModel.getIntValue("default_carrier") == oldId) {
            JeproLabSettingModel.updateValue("default_carrier", this.carrier_id);
        }

        // Copy reference
        query = "SELECT " + staticDataBaseObject.quoteName("reference_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier");
        query += " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + oldId;

        staticDataBaseObject.setQuery(query);
        this.reference_id = (int)staticDataBaseObject.loadValue("reference_id");

        //Db::getInstance()->execute('
        query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " SET " + staticDataBaseObject.quoteName("reference_id");
        query += " = " + this.reference_id + " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + this.carrier_id;

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);


        // Copy tax rules group
        query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_shop") + "(" + staticDataBaseObject.quoteName("carrier_id");
        query += ", " + staticDataBaseObject.quoteName("tax_rules_group_id") + ", " + staticDataBaseObject.quoteName("lab_id") + ") VALUES (";
        query += "SELECT " + staticDataBaseObject.quoteName("carrier_id") + ", " + staticDataBaseObject.quoteName("tax_rules_group_id") +  ", ";
        query += staticDataBaseObject.quoteName("lab_id") + " FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_lab");
        query += " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + oldId + ")";

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    /**
     * Get carrier using the reference id
     */
    public static JeproLabCarrierModel getCarrierByReference(int referenceId){
        // @todo class var $table must became static. here I have to use 'carrier' because this method is static
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("carrier_id") + " FROM  " + staticDataBaseObject.quoteName("#__jeprolab_carrier");
        query += " WHERE " + staticDataBaseObject.quoteName("reference_id") + " = " + referenceId + " AND " + staticDataBaseObject.quoteName("deleted");
        query += " = 0 ORDER BY " + staticDataBaseObject.quoteName("carrier_id") + " DESC";

        staticDataBaseObject.setQuery(query);
        int carrierId = (int)staticDataBaseObject.loadValue("carrier_id");
        if (carrierId <= 0) {
            return new JeproLabCarrierModel();
        }
        return new JeproLabCarrierModel(carrierId);
    }

    /**
     * Check if carrier is used (at least one order placed)
     *
     * @return int Order count for this carrier
     */
    public int inUse(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT COUNT(" + staticDataBaseObject.quoteName("carrier_id") + ") AS total FROM " + staticDataBaseObject.quoteName("#__jeprolab_request");
        query += " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + this.carrier_id;

        staticDataBaseObject.setQuery(query)
        return (int)staticDataBaseObject.loadValue("total");
    }

    public int getShippingMethod() {
        if (this.is_free) {
            return JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_FREE;
        }

        int method = this.shipping_method;

        if (this.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_DEFAULT) {
            // backward compatibility
            if (JeproLabSettingModel.getIntValue("shipping_method") > 0){
                method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT;
            }else{
                method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE;
            }
        }

        return method;
    }

    public String getRangeTable() {
        int shippingMethod = this.getShippingMethod();
        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
            return "range_weight";
        }else if(shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
            return "range_price";
        }
        return "";
    }

    public function getRangeObject() {
        return getRangeObject(0);
    }

    public function getRangeObject(int shippingMethod) {
        if (shippingMethod <= 0) {
            shippingMethod = this.getShippingMethod();
        }

        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
            return new JeproLabRangeWeight();
        }else if(shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
            return new JeproLabRangePrice();
        }
        return null;
    }

    public String getRangeSuffix() { return getRangeSuffix(null); }

    public String getRangeSuffix(JeproLabCurrencyModel currency) {
        if (currency == null) {
            currency = JeproLabContext.getContext().currency;
        }
        String suffix = JeproLabSettingModel.getStringValue("weight_unit");
        if (this.getShippingMethod() == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
            suffix = currency.sign;
        }
        return suffix;
    }

    public int getTaxRulesGroupId(){
        return JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(this.carrier_id, null);
    }

    public int getTaxRulesGroupId(JeproLabContext context){
        return JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(this.carrier_id, context);
    }

    public boolean deleteTaxRulesGroupId(){
        return deleteTaxRulesGroupId(null);
    }

    public boolean deleteTaxRulesGroupId(List<Integer> labIds) {
        if (labIds == null) {
            labIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
        }

        String where = staticDataBaseObject.quoteName("carrier_id") +  " = " + this.carrier_id;
        if (labIds.size() > 0) {
            where += " AND " + staticDataBaseObject.quoteName("lab_id") + " IN(";
            for(int labId : labIds){
                where += labId + ", ";
            }
            where = (where.endsWith(", ") ? where.substring(0, where.length() - 2) : where) + ") ";
        }
        String query = "DELETE " + staticDataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_lab");
        staticDataBaseObject.setQuery(query + where);
        return staticDataBaseObject.query(false);
    }

    public boolean setTaxRulesGroupId(int taxRulesGroupId){
        return setTaxRulesGroupId(taxRulesGroupId, false);
    }

    public boolean setTaxRulesGroupId(int taxRulesGroupId, boolean allLabs){
        List<Integer> labIds;
        if (!allLabs) {
            labIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
        } else {
            labIds = JeproLabLaboratoryModel.getLaboratoryIds(true, 0);
        }

        this.deleteTaxRulesGroupId(labIds);

        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_lab") + "(";
        query += staticDataBaseObject.quoteName("carrier_id") + ", " + staticDataBaseObject.quoteName("tax_rules_group_id");
        query += ", " + staticDataBaseObject.quoteName("lab_id") + ") VALUES (" ;
        boolean result = true;
        for(int labId : labIds) {
            staticDataBaseObject.setQuery(query + this.carrier_id + ", " + taxRulesGroupId + ", " + labId + ") ");
            result &= staticDataBaseObject.query(false);
        }
        String cacheKey = "carrier_tax_rules_group_id_" + this.carrier_id + "_" + JeproLabContext.getContext().laboratory.laboratory_id
        JeproLabCache.getInstance().remove(cacheKey);
        return result;
    }

    /**
     * Returns the taxes rate associated to the carrier
     *
     * @since 1.5
     * @param address
     * @return
     */
    public float getTaxesRate(JeproLabAddressModel address) {
        JeproLabTaxModel.JeproLabTaxCalculator taxCalculator = this.getTaxCalculator(address);
        return taxCalculator.getTotalRate();
    }

    /**
     * Returns the taxes calculator associated to the carrier
     *
     * @since 1.5
     * @param address
     * @return
     */
    public JeproLabTaxModel.JeproLabTaxCalculator getTaxCalculator(JeproLabAddressModel address, $id_order= null, $use_average_tax_of_products= false) {
        if ($use_average_tax_of_products) {
            return Adapter_ServiceLocator::get ('AverageTaxOfProductsTaxCalculator' )->setIdOrder($id_order);
        } else {
            JeproLabTaxModel.JeproLabTaxRulesManager taxManager = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, this.getTaxRulesGroupId());
            return taxManager.getTaxCalculator();
        }
    }

    public static String sqlDeliveryRangeLaboratory(String rangeTable){
        return sqlDeliveryRangeLaboratory(rangeTable, "delivery");
    }

    /**
     * This tricky method generates a sql clause to check if ranged data are overloaded by multi-lab
     *
     * @since 1.5.0
     * @param rangeTable
     * @return string
     */
    public static String sqlDeliveryRangeLaboratory(String rangeTable, String alias){
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }
        String where;
        if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.ALL_CONTEXT) {
            where = " AND delivery_2." + staticDataBaseObject.quoteName("lab_id") + " IS NULL AND delivery_2.";
            where += staticDataBaseObject.quoteName("lab_group_id") + " IS NULL";
        } else if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT) {
            where = " AND ((delivery_2." + staticDataBaseObject.quoteName("lab_group_d") + " IS NULL OR delivery_2.";
            where += staticDataBaseObject.quoteName("lab_group_id") + " = " + JeproLabLaboratoryModel.getContextLabGroupId();
            where += ") AND delivery_2." + staticDataBaseObject.quoteName("lab_id") + " IS NULL)";
        } else {
            where = " AND (delivery_2." + staticDataBaseObject.quoteName("lab_id") + " = " + JeproLabLaboratoryModel.getContextLabGroupId();
            where += " OR (delivery_2." + staticDataBaseObject.quoteName("lab_group_id") + " = " + JeproLabLaboratoryModel.getContextLabGroupId();
            where += " AND delivery_2." + staticDataBaseObject.quoteName("lab_id") + " IS NULL) OR (delivery_2." + staticDataBaseObject.quoteName("lab_group_id");
            where += " IS NULL AND delivery_2." + staticDataBaseObject.quoteName("lab_id") + " IS NULL)) ";
        }

        String query = " AND " + alias + "." +staticDataBaseObject.quoteName("delivery_id") + " = (SELECT delivery_2.";
        query += staticDataBaseObject.quoteName("delivery_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_delivery");
        query += " AS delivery_2 WHERE delivery_2." + staticDataBaseObject.quoteName("carrier_id") + " = " + staticDataBaseObject.quote(alias);
        query += "." + staticDataBaseObject.quoteName("carrier_id") + " AND delivery_2." + staticDataBaseObject.quoteName("zone_id");
        query += " = " + staticDataBaseObject.quote(alias) + "." + staticDataBaseObject.quoteName("zone_id") + " AND delivery2." ;
        query += staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id") + " = " + staticDataBaseObject.quote(alias);
        query += "." + staticDataBaseObject.quoteName(staticDataBaseObject.quote(rangeTable) + "_id") + where + " ORDER BY delivery_2.";
        query += staticDataBaseObject.quoteName("lab_id") + " DESC, delivery_2." + staticDataBaseObject.quoteName("lab_group_id") + " DESC LIMIT 1";

        return query;
    }

    /**
     * Moves a carrier
     *
     * @since 1.5.0
     * @param way Up (1) or Down (0)
     * @param position new value
     * @return bool Update result
     */
    public boolean updatePosition(boolean way, int position){
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        String query = "SELECT " + staticDataBaseObject.quoteName("carrier_id") + ", " + staticDataBaseObject.quoteName("position");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " WHERE " + staticDataBaseObject.quoteName("deleted");
        query += " = 0 ORDER BY " + staticDataBaseObject.quoteName("position") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();
        if (resultSet != null) {
            return false;
        }

        try {
            JeproLabCarrierModel movedCarrier = null;
            while(resultSet.next()) {
                if (resultSet.getInt("carrier_id") == this.carrier_id) {
                    movedCarrier = new JeproLabCarrierModel(resultSet.getInt("carrier_id"));
                }
            }

            if(movedCarrier == null || position <= 0){
                return false;
            }

            query = "UPDATE  " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " SET " + staticDataBaseObject.quoteName("position");
            query += " = " + position + (way ? " - 1 " : "+ 1") + " WHERE " + staticDataBaseObject.quoteName("position") ;
            query += (way ? " > " + movedCarrier.position + " AND " + staticDataBaseObject.quoteName("position") + " <= " + position
                    : " < " + movedCarrier.position + " AND " + staticDataBaseObject.quoteName("position") + " >= " + position + " AND "
                    + staticDataBaseObject.quoteName("deleted") + " = 0" );

            staticDataBaseObject.setQuery(query);
            boolean result = staticDataBaseObject.query(false);

            query = "UPDATE  " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " SET " + staticDataBaseObject.quoteName("position");
            query += " = " + position + " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + movedCarrier.carrier_id;

            staticDataBaseObject.setQuery(query);
            return result && staticDataBaseObject.query(false);
        }catch (SQLException ignored){
            ignored.printStackTrace();
        }finally {
            try{
                JeproLabDataBaseConnector.getInstance().closeConnexion();
            }catch(Exception ignored){
                ignored.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Reorders carrier positions.
     * Called after deleting a carrier.
     *
     * @since 1.5.0
     * @return bool $return
     */
    public static boolean cleanPositions(){
        boolean result = true;
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        String query = "SELECT  " + staticDataBaseObject.quoteName("carrier_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_carrier");
        query += " WHERE " + staticDataBaseObject.quoteName("deleted") + " = 0  ORDER BY " + staticDataBaseObject.quoteName("position") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();

        if(resultSet != null){
            int i = 0;
            try{
                while(resultSet.next()){
                    query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " SET " + staticDataBaseObject.quoteName("position");
                    query += " = " + (i++) + " WHERE " + staticDataBaseObject.quoteName("carrier_id") + " = " + resultSet.getInt("carrier_id");

                    staticDataBaseObject.setQuery(query);
                    result = staticDataBaseObject.query(false);
                }
            }catch (SQLException ignored) {
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return result;
    }

    public static int getTaxRulesGroupIdByCarrierId(int carrierId){
        return getTaxRulesGroupIdByCarrierId(carrierId, null);
    }

    public static int getTaxRulesGroupIdByCarrierId(int carrierId, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        String cacheKey = "jeprolab_carrier_tax_rules_group_id_" + carrierId + "_" + context.laboratory.laboratory_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT " + staticDataBaseObject.quoteName("tax_rules_group_id") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_lab") + " WHERE ";
            query += staticDataBaseObject.quoteName("carrier_id") + " = " + carrierId + " AND lab_id = " + JeproLabContext.getContext().laboratory.laboratory_id;

            staticDataBaseObject.setQuery(query);
            int result = (int)staticDataBaseObject.loadValue("tax_rules_group_d");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

}
