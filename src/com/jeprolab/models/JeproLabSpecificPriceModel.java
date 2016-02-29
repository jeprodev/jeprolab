package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 05/02/14.
 */
public class JeproLabSpecificPriceModel extends JeproLabModel{
    public int analyze_id;
    public int specific_price_id = 0;
    public int specific_price_rule_id = 0;
    public int cart_id = 0;
    public int analyze_attribute_id;
    public int laboratory_id;
    public int laboratory_group_id;
    public int currency_id;
    public int country_id;
    public int group_id;
    public int customer_id;
    public float price;
    public int from_quantity;
    public float reduction;
    public float reduction_tax = 1;
    public String reduction_type;
    public Date from;
    public Date to;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'specific_price',
                    'primary' => 'id_specific_price',
                    'fields' => array(
                    'id_shop_group' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId'),
    'id_shop' =>                array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_cart' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_product' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_product_attribute' =>    array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId'),
    'id_currency' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_specific_price_rule' =>    array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId'),
    'id_country' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_group' =>                array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_customer' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'price' =>                    array('type' => JeproLabSpecificPriceModel.TYPE_FLOAT, 'validate' => 'isNegativePrice', 'required' => true),
    'from_quantity' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isUnsignedInt', 'required' => true),
    'reduction' =>                array('type' => JeproLabSpecificPriceModel.TYPE_FLOAT, 'validate' => 'isPrice', 'required' => true),
    'reduction_tax' =>            array('type' => JeproLabSpecificPriceModel.TYPE_INT, 'validate' => 'isBool', 'required' => true),
    'reduction_type' =>        array('type' => JeproLabSpecificPriceModel.TYPE_STRING, 'validate' => 'isReductionType', 'required' => true),
    'from' =>                    array('type' => JeproLabSpecificPriceModel.TYPE_DATE, 'validate' => 'isDateFormat', 'required' => true),
    'to' =>                    array('type' => JeproLabSpecificPriceModel.TYPE_DATE, 'validate' => 'isDateFormat', 'required' => true),
    ),
            );

    protected $webserviceParameters = array(
            'objectsNodeName' => 'specific_prices',
                    'objectNodeName' => 'specific_price',
                    'fields' => array(
                    'id_shop_group' =>            array('xlink_resource' => 'shop_groups'),
    'id_shop' =>                array('xlink_resource' => 'shops', 'required' => true),
    'id_cart' =>                array('xlink_resource' => 'carts', 'required' => true),
    'id_product' =>            array('xlink_resource' => 'products', 'required' => true),
    'id_product_attribute' =>        array('xlink_resource' => 'product_attributes'),
    'id_currency' =>            array('xlink_resource' => 'currencies', 'required' => true),
    'id_country' =>            array('xlink_resource' => 'countries', 'required' => true),
    'id_group' =>                array('xlink_resource' => 'groups', 'required' => true),
    'id_customer' =>            array('xlink_resource' => 'customers', 'required' => true),
    ),
            );

*/
    protected static Map<String, JeproLabSpecificPriceModel> _specificPriceCache = new HashMap<>();
    protected static Map<String, List<Integer>> _filterOutCache = new HashMap<>();
    protected static Map<Integer, String> _cache_priorities = new HashMap<>();
    protected static Map<String, Boolean> _no_specific_values = new HashMap<>();

    private static boolean feature_active = false;


    /*
     * Flush local cache
     * /
    protected function flushCache() {
        SpecificPrice::$_specificPriceCache = array();
        SpecificPrice::$_filterOutCache = array();
        SpecificPrice::$_cache_priorities = array();
        SpecificPrice::$_no_specific_values = array();
        Product::flushPriceCache();
    }

    public function add($autodate = true, $nullValues = false)
    {
        if (parent::add($autodate, $nullValues)) {
        // Flush cache when we adding a new specific price
        $this->flushCache();
        // Set cache of feature detachable to true
        Configuration::updateGlobalValue('PS_SPECIFIC_PRICE_FEATURE_ACTIVE', '1');
        return true;
    }
        return false;
    }

    public function update($null_values = false)
    {
        if (parent::update($null_values)) {
        // Flush cache when we updating a new specific price
        $this->flushCache();
        return true;
    }
        return false;
    }

    public function delete()
    {
        if (parent::delete()) {
        // Flush cache when we deletind a new specific price
        $this->flushCache();
        // Refresh cache of feature detachable
        Configuration::updateGlobalValue('PS_SPECIFIC_PRICE_FEATURE_ACTIVE', SpecificPrice::isCurrentlyUsed($this->def['table']));
        return true;
    }
        return false;
    }
*/
    public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId){
        return getSpecificPricesByAnalyzeId(analyzeId, 0, 0);
    }

    public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId, int analyzeAttributeId){
        return getSpecificPricesByAnalyzeId(analyzeId, analyzeAttributeId, 0);
    }

    public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId, int analyzeAttributeId, int cartId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
        query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND analyze_attribute_id = " + analyzeAttributeId : "" );
        query += (cartId > 0 ? " AND cart_id = " + cartId : "" );

        staticDataBaseObject.setQuery(query);
        ResultSet specificSet = staticDataBaseObject.loadObject();
        List<JeproLabSpecificPriceModel> specificList = new ArrayList<>();
        if(specificSet != null){
            try{
                JeproLabSpecificPriceModel specificPrice;
                while(specificSet.next()){
                    specificPrice = new JeproLabSpecificPriceModel();
                    specificPrice.specific_price_id = specificSet.getInt("specific_price_id");
                    specificPrice.specific_price_rule_id = specificSet.getInt("specific_price_rule_id");
                    specificPrice.cart_id = specificSet.getInt("cart_id");
                    specificPrice.analyze_id = specificSet.getInt("analyze_id");
                    specificPrice.analyze_attribute_id = specificSet.getInt("analyze_attribute_id");
                    specificPrice.laboratory_id = specificSet.getInt("laboratory_id");
                    specificPrice.laboratory_group_id = specificSet.getInt("laboratory_group_id");
                    specificPrice.currency_id = specificSet.getInt("currency_id");
                    specificPrice.country_id = specificSet.getInt("country_id");
                    specificPrice.customer_id = specificSet.getInt("customer_id");
                    specificPrice.group_id = specificSet.getInt("group_id");
                    specificPrice.price = specificSet.getFloat("price");
                    specificPrice.from_quantity = specificSet.getInt("from_quantity");
                    specificPrice.reduction = specificSet.getFloat("reduction");
                    specificPrice.reduction_type = specificSet.getString("reduction_type");
                    specificPrice.from = specificSet.getDate("from");
                    specificPrice.to = specificSet.getDate("to");
                    //specificPrice = specificSet.get("");
                    //specificPrice = specificSet.get("");
                    specificList.add(specificPrice);
                }
            }catch (SQLException ignored){

            }
        }

        return specificList;
    }
/*
    public static function deleteByIdCart($id_cart, $id_product = false, $id_product_attribute = false)
    {
        return Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'specific_price`
        WHERE id_cart='.(int)$id_cart.
        ($id_product ? ' AND id_product='.(int)$id_product.' AND id_product_attribute='.(int)$id_product_attribute : ''));
    }

    public static function getIdsByProductId($id_product, $id_product_attribute = false, $id_cart = 0)
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT `id_specific_price`
            FROM `'._DB_PREFIX_.'specific_price`
        WHERE `id_product` = '.(int)$id_product.'
        AND id_product_attribute='.(int)$id_product_attribute.'
        AND id_cart='.(int)$id_cart);
    }

    /**
     * score generation for quantity discount
     */
    protected static String getScoreQuery(int analyzeId, int labId, int currencyId, int countryId, int groupId, int customerId){
        String select = "(";

        String priority = JeproLabSpecificPriceModel.getPriority(analyzeId);
        String[] priorityArr = priority.split(";");
        int index = 0;
        for(String field : priorityArr) {
            if (!field.equals("")) {
                select += " IF (" + staticDataBaseObject.quoteName(field) + " = ";
                switch (field) {
                    case "customer_id":
                        select += customerId;
                        break;
                    case "group_id":
                        select += groupId;
                        break;
                    case "country_id":
                        select += countryId;
                        break;
                    case "currency_id":
                        select += currencyId;
                        break;
                    case "lab_id":
                        select += labId;
                        break;
                }

                select += ", " + Math.pow(2, index + 1) + ", 0) + ";
            }
            index++;
        }
        select = select.substring(0, select.length() - 2);
        return select + ") AS " + staticDataBaseObject.quoteName("score");
    }

    public static String getPriority(int analyzeId){
        if (!JeproLabSpecificPriceModel.isFeaturePublished()) {
            return JeproLabSettingModel.getStringValue("specific_price_priorities"); //'PS_SPECIFIC_PRICE_PRIORITIES'));
        }

        if (!JeproLabSpecificPriceModel._cache_priorities.containsKey(analyzeId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("priority") + ", " + staticDataBaseObject.quoteName("specific_price_priority_id");
            query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price_priority") + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
            query += " = " + analyzeId + " ORDER BY " + staticDataBaseObject.quoteName("specific_price_priority_id") + " DESC ";

            staticDataBaseObject.setQuery(query);

            JeproLabSpecificPriceModel._cache_priorities.put(analyzeId, staticDataBaseObject.loadStringValue("priority"));
        }

        String priority = JeproLabSpecificPriceModel._cache_priorities.get(analyzeId);

        if (priority.equals("")) {
            priority = JeproLabSettingModel.getStringValue("specific_price_priorities");
        }
        priority = "customer_id;" + priority;

        return priority;
    }

    protected static String filterOutField(String fieldName, int fieldValue){
        return filterOutField(fieldName, fieldValue, 1000);
    }

    /**
     * Remove or add a field value to a query if values are present in the database (cache friendly)
     *
     * @param fieldName field name
     * @param fieldValue field value
     * @param threshold threshold
     * @return string
     */
    protected static String filterOutField(String fieldName, int fieldValue, int threshold){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String extraQuery = " AND " + staticDataBaseObject.quoteName(fieldName) + " = 0 ";
        if (fieldValue == 0 || JeproLabSpecificPriceModel._no_specific_values.containsKey(fieldName)){
            return extraQuery;
        }
        String cacheKey  = "jeprolab_specific_price_filter_out_field_" + fieldName + "_" + threshold;
        List<Integer> specificList = new ArrayList<>();
        if (!JeproLabSpecificPriceModel._filterOutCache.containsKey(cacheKey)) {
            String queryCount = "SELECT COUNT(DISTINCT " + staticDataBaseObject.quoteName(fieldName) + ") AS filter_out FROM ";
            queryCount += staticDataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE " + staticDataBaseObject.quoteName(fieldName) + " != 0";
            staticDataBaseObject.setQuery(queryCount);

            int specificCount = (int)staticDataBaseObject.loadValue("filter_out");
            if (specificCount == 0) {
                JeproLabSpecificPriceModel._no_specific_values.put(fieldName, true);

                return extraQuery;
            }
            if (specificCount < threshold) {
                String query  = "SELECT DISTINCT " + staticDataBaseObject.quoteName(fieldName) + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price");
                query += " WHERE " + staticDataBaseObject.quoteName(fieldName) + " != 0";
                staticDataBaseObject.setQuery(query);
                ResultSet tmpSpecificSet = staticDataBaseObject.loadObject();

                try{
                    while (tmpSpecificSet.next()){
                        specificList.add(tmpSpecificSet.getInt(fieldName));
                    }
                }catch (SQLException ignored){

                }
            }
            JeproLabSpecificPriceModel._filterOutCache.put(cacheKey, specificList);
        } else {
            specificList = (List)JeproLabSpecificPriceModel._filterOutCache.get(cacheKey);
        }

        // $specific_list is empty if the threshold is reached
        if (specificList.isEmpty() || specificList.contains(fieldValue)) {
            extraQuery = " AND " + staticDataBaseObject.quoteName(fieldName) + " " + JeproLabSpecificPriceModel.formatIntInQuery(0, fieldValue) +" ";
        }

        return extraQuery;
    }


    protected static String  computeExtraConditions(int analyzeId, int analyzeAttributeId, int customerId, int cartId){
        return computeExtraConditions(analyzeId, analyzeAttributeId, customerId, cartId, null, null);
    }

    protected static String  computeExtraConditions(int analyzeId, int analyzeAttributeId, int customerId, int cartId, Date beginning){
        return computeExtraConditions(analyzeId, analyzeAttributeId, customerId, cartId, beginning, null);
    }

    /**
     * Remove or add useless fields value depending on the values in the database (cache friendly)
     *
     * @param analyzeId
     * @param analyzeAttributeId
     * @param cartId
     * @param beginning
     * @param ending
     * @return string
     */
    protected static String  computeExtraConditions(int analyzeId, int analyzeAttributeId, int customerId, int cartId, Date beginning, Date ending){
        Date firstDate = new Date("Y-m-d 00:00:00");
        Date lastDate = new  Date("Y-m-d 23:59:59");
        Date now = new Date("Y-m-d H:i:00");
        if (beginning == null) {
            beginning = now;
        }
        if (ending == null) {
            ending = now;
        }

        String extraQuery = "";

        if (analyzeId > 0) {
            extraQuery += JeproLabSpecificPriceModel.filterOutField("analyze_id", analyzeId);
        }

        if (customerId > 0) {
            extraQuery += JeproLabSpecificPriceModel.filterOutField("customer_id", customerId);
        }

        if (analyzeAttributeId > 0) {
            extraQuery += JeproLabSpecificPriceModel.filterOutField("analyze_attribute_id", analyzeAttributeId);
        }

        if (cartId > 0) {
            extraQuery += JeproLabSpecificPriceModel.filterOutField("cart_id", cartId);
        }

        int fromSpecificCount, toSpecificCount;
        if (ending == now && beginning == now) {
            String cacheKey = "jeprolab_specific_price_compute_extra_conditions" + firstDate.toString() + "_" + lastDate.toString();

            if (!JeproLabSpecificPriceModel._filterOutCache.containsKey(cacheKey)) {
                String queryFromCount = "SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price");
                queryFromCount += " WHERE " + staticDataBaseObject.quoteName("from") + " BETWEEN '" + firstDate.toString() + "' AND '" + lastDate.toString() + "'";
                staticDataBaseObject.setQuery(queryFromCount);
                fromSpecificCount = (int)staticDataBaseObject.loadValue("1"); //Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query_from_count);

                String queryToCount = "SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE ";
                queryToCount += staticDataBaseObject.quoteName("to") + " BETWEEN '" + firstDate.toString() + "' AND '" + lastDate.toString() + "'";
                staticDataBaseObject.quoteName(queryToCount);

                toSpecificCount  = (int)staticDataBaseObject.loadValue("1");
                List<Integer> specificCount = new ArrayList<>();
                specificCount.add(fromSpecificCount);
                specificCount.add(toSpecificCount);
                JeproLabSpecificPriceModel._filterOutCache.put(cacheKey, specificCount);
            } else {
                List specificCount = JeproLabSpecificPriceModel._filterOutCache.get(cacheKey);
                fromSpecificCount = (int)specificCount.get(0);
                toSpecificCount = (int)specificCount.get(1);
            }
        } else {
            fromSpecificCount = toSpecificCount = 1;
        }

        // if the from and to is not reached during the current day, just change $ending & $beginning to any date of the day to improve the cache
        if (fromSpecificCount < 0 && toSpecificCount < 0){
            ending = beginning = firstDate;
        }

        extraQuery += " AND (" + staticDataBaseObject.quoteName("from") + " = '0000-00-00 00:00:00' OR '" + beginning + "' >= ";
        extraQuery += staticDataBaseObject.quoteName("from") + ") AND (" + staticDataBaseObject.quoteName("to") + " = '0000-00-00 00:00:00' OR ' ";
        extraQuery += ending + "' <= " + staticDataBaseObject.quoteName("to") + ")";

        return extraQuery;
    }

    private static String formatIntInQuery(int firstValue, int secondValue) {
        if (firstValue != secondValue) {
            return " IN (" + firstValue + ", " + secondValue + ")";
        } else {
            return " = " + firstValue;
        }
    }

    public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity){
        return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, 0, 0, 0, 0);
    }

    public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId){
        return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, 0, 0, 0);
    }

    public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId, int customerId){
        return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, customerId, 0, 0);
    }

    public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId, int customerId, int cartId){
        return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, customerId, cartId, 0);
    }

    public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId, int customerId, int cartId, int realQuantity){
        if (!JeproLabSpecificPriceModel.isFeaturePublished()) {
            return new JeproLabSpecificPriceModel();
        }
        /*
        ** The date is not taken into account for the cache, but this is for the better because it keeps the consistency for the whole script.
        ** The price must not change between the top and the bottom of the page
        */

        String cacheKey = analyzeId + "_" + labId + "_" + currencyId + "_" + countryId + "_" + groupId + "_" + quantity + "_" + analyzeAttributeId + "_" + cartId + "_" + customerId + "_" + realQuantity;
        if (!JeproLabSpecificPriceModel._specificPriceCache.containsKey(cacheKey)){
            String extraQuery = JeproLabSpecificPriceModel.computeExtraConditions(analyzeId, analyzeAttributeId, customerId, cartId);
            String query = "SELECT *, " + JeproLabSpecificPriceModel.getScoreQuery(analyzeId, labId, currencyId, countryId, groupId, customerId);
            query += " FROM " + staticDataBaseObject.quoteName("specific_price") + " WHERE " + staticDataBaseObject.quoteName("lab_id");
            query += JeproLabSpecificPriceModel.formatIntInQuery(0, labId) + " AND " + staticDataBaseObject.quoteName("currency_id");
            query += JeproLabSpecificPriceModel.formatIntInQuery(0, currencyId) + " AND " + staticDataBaseObject.quoteName("country_id");
            query += JeproLabSpecificPriceModel.formatIntInQuery(0, countryId) + " " + staticDataBaseObject.quoteName("group_id");
            query += JeproLabSpecificPriceModel.formatIntInQuery(0, groupId) + extraQuery + " AND IF(" + staticDataBaseObject.quoteName("from_quantity");
            query += " > 1, " + staticDataBaseObject.quoteName("from_quantity") + ", 0) <= ";
            query += (JeproLabSettingModel.getIntValue("quantity_discount_on_combination") > 0 || (cartId < 0) || (realQuantity <= 0)) ? quantity : Math.max(1, realQuantity);
            query += " ORDER BY " + staticDataBaseObject.quoteName("analyze_attribute_id") + " DESC, " + staticDataBaseObject.quoteName("from_quantity") + " DESC, ";
            query += staticDataBaseObject.quoteName("specific_price_rule_id") + " ASC, " + staticDataBaseObject.quoteName("score") + " DESC, ";
            query += staticDataBaseObject.quoteName("to") + " DESC, " + staticDataBaseObject.quoteName("from") + " DESC";
            System.out.println(query);
            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObject();
            JeproLabSpecificPriceModel specificPrice = new JeproLabSpecificPriceModel();
            try {
                if(resultSet.next()){
                    //todo set fields
                }
            }catch (SQLException ignored){

            }

            JeproLabSpecificPriceModel._specificPriceCache.put(cacheKey, specificPrice);
        }
        return (JeproLabSpecificPriceModel)JeproLabSpecificPriceModel._specificPriceCache.get(cacheKey);
    }
/*
    public static function setPriorities($priorities)
    {
        $value = '';
        if (is_array($priorities)) {
            foreach ($priorities as $priority) {
                $value .= pSQL($priority).';';
            }
        }

        SpecificPrice::deletePriorities();

        return Configuration::updateValue('PS_SPECIFIC_PRICE_PRIORITIES', rtrim($value, ';'));
    }

    public static function deletePriorities()
    {
        return Db::getInstance()->execute('
            TRUNCATE `'._DB_PREFIX_.'specific_price_priority`
        ');
    }

    public static function setSpecificPriority($id_product, $priorities)
    {
        $value = '';
        foreach ($priorities as $priority) {
        $value .= pSQL($priority).';';
    }

        return Db::getInstance()->execute('
            INSERT INTO `'._DB_PREFIX_.'specific_price_priority` (`id_product`, `priority`)
        VALUES ('.(int)$id_product.',\''.pSQL(rtrim($value, ';')).'\')
        ON DUPLICATE KEY UPDATE `priority` = \''.pSQL(rtrim($value, ';')).'\'
        ');
    }

    public static function getQuantityDiscounts($id_product, $id_shop, $id_currency, $id_country, $id_group, $id_product_attribute = null, $all_combinations = false, $id_customer = 0)
    {
        if (!SpecificPrice::isFeatureActive()) {
        return array();
    }

        $query_extra = JeproLabSpecificPriceModel.computeExtraConditions($id_product, ((!$all_combinations)?$id_product_attribute:null), $id_customer, null);
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT *,
            '.SpecificPrice::_getScoreQuery($id_product, $id_shop, $id_currency, $id_country, $id_group, $id_customer).'
            FROM `'._DB_PREFIX_.'specific_price`
        WHERE
        `id_shop` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_shop).' AND
        `id_currency` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_currency).' AND
        `id_country` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_country).' AND
        `id_group` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_group).' '.$query_extra.'
        ORDER BY `from_quantity` ASC, `id_specific_price_rule` ASC, `score` DESC, `to` DESC, `from` DESC
        ', false, false);

        $targeted_prices = array();
        $last_quantity = array();

        while ($specific_price = Db::getInstance()->nextRow($result)) {
        if (!isset($last_quantity[(int)$specific_price['id_product_attribute']])) {
            $last_quantity[(int)$specific_price['id_product_attribute']] = $specific_price['from_quantity'];
        } elseif ($last_quantity[(int)$specific_price['id_product_attribute']] == $specific_price['from_quantity']) {
            continue;
        }

        $last_quantity[(int)$specific_price['id_product_attribute']] = $specific_price['from_quantity'];
        if ($specific_price['from_quantity'] > 1) {
            $targeted_prices[] = $specific_price;
        }
    }

        return $targeted_prices;
    }

    public static function getQuantityDiscount($id_product, $id_shop, $id_currency, $id_country, $id_group, $quantity, $id_product_attribute = null, $id_customer = 0)
    {
        if (!SpecificPrice::isFeatureActive()) {
        return array();
    }



        $query_extra = JeproLabSpecificPriceModel.computeExtraConditions($id_product, $id_product_attribute, $id_customer, null);
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT *,
            '.SpecificPrice::_getScoreQuery($id_product, $id_shop, $id_currency, $id_country, $id_group, $id_customer).'
            FROM `'._DB_PREFIX_.'specific_price`
        WHERE
        `id_shop` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_shop).' AND
        `id_currency` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_currency).' AND
        `id_country` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_country).' AND
        `id_group` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_group).' AND
        `from_quantity` >= '.(int)$quantity.' '.$query_extra.'
        ORDER BY `from_quantity` DESC, `score` DESC, `to` DESC, `from` DESC
        ');
    }

    public static function getProductIdByDate($id_shop, $id_currency, $id_country, $id_group, $beginning, $ending, $id_customer = 0, $with_combination_id = false)
    {
        if (!SpecificPrice::isFeatureActive()) {
        return array();
    }

        $query_extra = JeproLabSpecificPriceModel.computeExtraConditions(null, null, $id_customer, null, $beginning, $ending);
        $results = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT `id_product`, `id_product_attribute`
            FROM `'._DB_PREFIX_.'specific_price`
        WHERE	`id_shop` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_shop).' AND
        `id_currency` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_currency).' AND
        `id_country` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_country).' AND
        `id_group` '.JeproLabSpecificPriceModel.formatIntInQuery(0, $id_group).' AND
        `from_quantity` = 1 AND
        `reduction` > 0
        '.$query_extra);
        $ids_product = array();
        foreach($results as $key => $value) {
        $ids_product[] = $with_combination_id ? array('id_product' => (int)$value['id_product'], 'id_product_attribute' => (int)$value['id_product_attribute']) : (int)$value['id_product'];
    }

        return $ids_product;
    }

    public static function deleteByProductId($id_product)
    {
        if (Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'specific_price` WHERE `id_product` = '.(int)$id_product)) {
        // Refresh cache of feature detachable
        Configuration::updateGlobalValue('PS_SPECIFIC_PRICE_FEATURE_ACTIVE', SpecificPrice::isCurrentlyUsed('specific_price'));
        return true;
    }
        return false;
    }

    public function duplicate($id_product = false)
    {
        if ($id_product) {
            $this->id_product = (int)$id_product;
        }
        unset($this->id);
        return $this->add();
    }

    /**
     * This method is allow to know if a feature is used or active
     * @since 1.5.0.1
     * @return bool
     */
    public static boolean isFeaturePublished(){
        if (!feature_active) {
            feature_active = JeproLabSettingModel.getIntValue("specific_price_feature_active") > 0;
        }
        return feature_active;
    }
/*
    public static function exists($id_product, $id_product_attribute, $id_shop, $id_group, $id_country, $id_currency, $id_customer, $from_quantity, $from, $to, $rule = false)
    {
        $rule = ' AND `id_specific_price_rule`'.(!$rule ? '=0' : '!=0');
        return (int)Db::getInstance()->getValue('SELECT `id_specific_price`
            FROM '._DB_PREFIX_.'specific_price
        WHERE `id_product`='.(int)$id_product.' AND
        `id_product_attribute`='.(int)$id_product_attribute.' AND
        `id_shop`='.(int)$id_shop.' AND
        `id_group`='.(int)$id_group.' AND
        `id_country`='.(int)$id_country.' AND
        `id_currency`='.(int)$id_currency.' AND
        `id_customer`='.(int)$id_customer.' AND
        `from_quantity`='.(int)$from_quantity.' AND
        `from` >= \''.pSQL($from).'\' AND
        `to` <= \''.pSQL($to).'\''.$rule);
    } */


    public static class JeproLabSpecificPriceRuleModel extends JeproLabModel {
        public String name;
        public int laboratory_id;
        public int currency_id;
        public int country_id;
        public int group_id;
        public int from_quantity;
        public float price;
        public float reduction;
        public float reduction_tax;
        public String reduction_type;
        public Date from;
        public Date to;

        protected static boolean rules_application_enable = true;

        /*
         * @see ObjectModel::$definition
         * /
        public static $definition = array(
                'table' => 'specific_price_rule',
                        'primary' => 'id_specific_price_rule',
                        'fields' => array(
                        'name' =>            array('type' => self::TYPE_STRING, 'validate' => 'isGenericName', 'required' => true),
        'id_shop' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
        'id_country' =>    array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
        'id_currency' =>    array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
        'id_group' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
        'from_quantity' =>    array('type' => self::TYPE_INT, 'validate' => 'isUnsignedInt', 'required' => true),
        'price' =>        array('type' => self::TYPE_FLOAT, 'validate' => 'isNegativePrice', 'required' => true),
        'reduction' =>        array('type' => self::TYPE_FLOAT, 'validate' => 'isPrice', 'required' => true),
        'reduction_tax' =>            array('type' => self::TYPE_INT, 'validate' => 'isBool', 'required' => true),
        'reduction_type' => array('type' => self::TYPE_STRING, 'validate' => 'isReductionType', 'required' => true),
        'from' =>            array('type' => self::TYPE_DATE, 'validate' => 'isDateFormat', 'required' => false),
        'to' =>            array('type' => self::TYPE_DATE, 'validate' => 'isDateFormat', 'required' => false),
        ),
                );

        protected $webserviceParameters = array(
                'objectsNodeName' => 'specific_price_rules',
                        'objectNodeName' => 'specific_price_rule',
                        'fields' => array(
                        'id_shop' =>                array('xlink_resource' => 'shops', 'required' => true),
        'id_country' =>            array('xlink_resource' => 'countries', 'required' => true),
        'id_currency' =>            array('xlink_resource' => 'currencies', 'required' => true),
        'id_group' =>                array('xlink_resource' => 'groups', 'required' => true),
        ),
                );

        public function delete()
        {
            $this->deleteConditions();
            Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'specific_price WHERE id_specific_price_rule='.(int)$this->id);
            return parent::delete();
        }

        public function deleteConditions()
        {
            $ids_condition_group = Db::getInstance()->executeS('SELECT id_specific_price_rule_condition_group
                FROM '._DB_PREFIX_.'specific_price_rule_condition_group
            WHERE id_specific_price_rule='.(int)$this->id);
            if ($ids_condition_group) {
                foreach ($ids_condition_group as $row) {
                    Db::getInstance()->delete('specific_price_rule_condition_group', 'id_specific_price_rule_condition_group='.(int)$row['id_specific_price_rule_condition_group']);
                    Db::getInstance()->delete('specific_price_rule_condition', 'id_specific_price_rule_condition_group='.(int)$row['id_specific_price_rule_condition_group']);
                }
            }
        }

        public static function disableAnyApplication()
        {
            SpecificPriceRule::$rules_application_enable = false;
        }

        public static function enableAnyApplication()
        {
            SpecificPriceRule::$rules_application_enable = true;
        }

        public function addConditions($conditions)
        {
            if (!is_array($conditions)) {
                return;
            }

            $result = Db::getInstance()->insert('specific_price_rule_condition_group', array(
                        'id_specific_price_rule' =>    (int)$this->id
        ));
            if (!$result) {
                return false;
            }
            $id_specific_price_rule_condition_group = (int)Db::getInstance()->Insert_ID();
            foreach ($conditions as $condition) {
            $result = Db::getInstance()->insert('specific_price_rule_condition', array(
                            'id_specific_price_rule_condition_group' => (int)$id_specific_price_rule_condition_group,
                    'type' => pSQL($condition['type']),
                    'value' => (float)$condition['value'],
            ));
            if (!$result) {
                return false;
            }
        }
            return true;
        }

        public function apply($products = false)
        {
            if (!SpecificPriceRule::$rules_application_enable) {
                return;
            }

            $this->resetApplication($products);
            $products = $this->getAffectedProducts($products);
            foreach ($products as $product) {
            SpecificPriceRule::applyRuleToProduct((int)$this->id, (int)$product['id_product'], (int)$product['id_product_attribute']);
        }
        }

        public function resetApplication($products = false)
        {
            $where = '';
            if ($products && count($products)) {
                $where .= ' AND id_product IN ('.implode(', ', array_map('intval', $products)).')';
            }
            return Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'specific_price WHERE id_specific_price_rule='.(int)$this->id.$where);
        }*/

        public static void applyAllRules(int analyzeId) {
            List<Integer> analyzeList = new ArrayList<>();
            analyzeList.add(analyzeId);
            applyAllRules(analyzeList);
        }

        /**
         * @param analyzeList $products
         */
        public static void applyAllRules(List<Integer> analyzeList) {
            if (!JeproLabSpecificPriceRuleModel.rules_application_enable) {
                return;
            }

            /*$rules = new PrestaShopCollection('SpecificPriceRule');
            foreach ($rules as $rule) {
                /** @var SpecificPriceRule $rule * /
                $rule->apply($products);
            }*/
        }
/*
        public function getConditions()
        {
            $conditions = Db::getInstance()->executeS('
                SELECT g.*, c.*
                        FROM '._DB_PREFIX_.'specific_price_rule_condition_group g
            LEFT JOIN '._DB_PREFIX_.'specific_price_rule_condition c
            ON (c.id_specific_price_rule_condition_group = g.id_specific_price_rule_condition_group)
            WHERE g.id_specific_price_rule='.(int)$this->id
            );
            $conditions_group = array();
            if ($conditions) {
                foreach ($conditions as &$condition) {
                    if ($condition['type'] == 'attribute') {
                        $condition['id_attribute_group'] = Db::getInstance()->getValue('SELECT id_attribute_group
                                FROM '._DB_PREFIX_.'attribute
                        WHERE id_attribute='.(int)$condition['value']);
                    } elseif ($condition['type'] == 'feature') {
                        $condition['id_feature'] = Db::getInstance()->getValue('SELECT id_feature
                                FROM '._DB_PREFIX_.'feature_value
                        WHERE id_feature_value='.(int)$condition['value']);
                    }
                    $conditions_group[(int)$condition['id_specific_price_rule_condition_group']][] = $condition;
                }
            }
            return $conditions_group;
        }

        /**
         * Return the product list affected by this specific rule.
         *
         * @param bool|array $products Products list limitation.
         * @return array Affected products list IDs.
         * @throws PrestaShopDatabaseException
         * /
        public function getAffectedProducts($products = false)
        {
            $conditions_group = $this->getConditions();
            $current_shop_id = Context::getContext()->shop->id;

            $result = array();

            if ($conditions_group) {
                foreach ($conditions_group as $id_condition_group => $condition_group) {
                    // Base request
                    $query = new DbQuery();
                    $query->select('p.`id_product`')
                    ->from('product', 'p')
                    ->leftJoin('product_shop', 'ps', 'p.`id_product` = ps.`id_product`')
                    ->where('ps.id_shop = '.(int)$current_shop_id);

                    $attributes_join_added = false;

                    // Add the conditions
                    foreach ($condition_group as $id_condition => $condition) {
                        if ($condition['type'] == 'attribute') {
                            if (!$attributes_join_added) {
                                $query->select('pa.`id_product_attribute`')
                                ->leftJoin('product_attribute', 'pa', 'p.`id_product` = pa.`id_product`')
                                ->join(Shop::addSqlAssociation('product_attribute', 'pa', false));

                                $attributes_join_added = true;
                            }

                            $query->leftJoin('product_attribute_combination', 'pac'.(int)$id_condition, 'pa.`id_product_attribute` = pac'.(int)$id_condition.'.`id_product_attribute`')
                            ->where('pac'.(int)$id_condition.'.`id_attribute` = '.(int)$condition['value']);
                        } elseif ($condition['type'] == 'manufacturer') {
                            $query->where('p.id_manufacturer = '.(int)$condition['value']);
                        } elseif ($condition['type'] == 'category') {
                            $query->leftJoin('category_product', 'cp'.(int)$id_condition, 'p.`id_product` = cp'.(int)$id_condition.'.`id_product`')
                            ->where('cp'.(int)$id_condition.'.id_category = '.(int)$condition['value']);
                        } elseif ($condition['type'] == 'supplier') {
                            $query->where('EXISTS(
                                    SELECT
                                    `ps'.(int)$id_condition.'`.`id_product`
                            FROM
                            `'._DB_PREFIX_.'product_supplier` `ps'.(int)$id_condition.'`
                            WHERE
                            `p`.`id_product` = `ps'.(int)$id_condition.'`.`id_product`
                            AND `ps'.(int)$id_condition.'`.`id_supplier` = '.(int)$condition['value'].'
                            )');
                        } elseif ($condition['type'] == 'feature') {
                            $query->leftJoin('feature_product', 'fp'.(int)$id_condition, 'p.`id_product` = fp'.(int)$id_condition.'.`id_product`')
                            ->where('fp'.(int)$id_condition.'.`id_feature_value` = '.(int)$condition['value']);
                        }
                    }

                    // Products limitation
                    if ($products && count($products)) {
                        $query->where('p.`id_product` IN ('.implode(', ', array_map('intval', $products)).')');
                    }

                    // Force the column id_product_attribute if not requested
                    if (!$attributes_join_added) {
                        $query->select('NULL as `id_product_attribute`');
                    }

                    $result = array_merge($result, Db::getInstance()->executeS($query));
                }
            } else {
                // All products without conditions
                if ($products && count($products)) {
                    $query = new DbQuery();
                    $query->select('p.`id_product`')
                    ->select('NULL as `id_product_attribute`')
                    ->from('product', 'p')
                    ->leftJoin('product_shop', 'ps', 'p.`id_product` = ps.`id_product`')
                    ->where('ps.id_shop = '.(int)$current_shop_id);
                    $query->where('p.`id_product` IN ('.implode(', ', array_map('intval', $products)).')');
                    $result = Db::getInstance()->executeS($query);
                } else {
                    $result = array(array('id_product' => 0, 'id_product_attribute' => null));
                }

            }

            return $result;
        }

        public static function applyRuleToProduct($id_rule, $id_product, $id_product_attribute = null)
        {
            $rule = new SpecificPriceRule((int)$id_rule);
            if (!Validate::isLoadedObject($rule) || !Validate::isUnsignedInt($id_product)) {
            return false;
        }

            $specific_price = new SpecificPrice();
            $specific_price->id_specific_price_rule = (int)$rule->id;
            $specific_price->id_product = (int)$id_product;
            $specific_price->id_product_attribute = (int)$id_product_attribute;
            $specific_price->id_customer = 0;
            $specific_price->id_shop = (int)$rule->id_shop;
            $specific_price->id_country = (int)$rule->id_country;
            $specific_price->id_currency = (int)$rule->id_currency;
            $specific_price->id_group = (int)$rule->id_group;
            $specific_price->from_quantity = (int)$rule->from_quantity;
            $specific_price->price = (float)$rule->price;
            $specific_price->reduction_type = $rule->reduction_type;
            $specific_price->reduction_tax = $rule->reduction_tax;
            $specific_price->reduction = ($rule->reduction_type == 'percentage' ? $rule->reduction / 100 : (float)$rule->reduction);
            $specific_price->from = $rule->from;
            $specific_price->to = $rule->to;

            return $specific_price->add();
        }*/
    }
}
