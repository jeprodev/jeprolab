package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public function add($autodate = true, $null_values = false) {
        if (this.position <= 0) {
            this.position = JeproLabCarrierModel.getHigherPosition () + 1;
        }
        if (!parent::add ($autodate, $null_values)||!Validate::isLoadedObject ($this)){
            return false;
        }
        if (!$count = Db::getInstance () -> getValue('SELECT count(`id_carrier`) FROM `'._DB_PREFIX_. this.def['table'].
        '` WHERE `deleted` = 0' )){
            return false;
        }
        if ($count == 1) {
            Configuration::updateValue ('PS_CARRIER_DEFAULT', (int) this.id);
        }

        // Register reference
        Db::getInstance () -> execute('UPDATE `'._DB_PREFIX_. this.def['table']. '` SET `id_reference` = '. this.id.
        ' WHERE `id_carrier` = '. this.id);

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
        return (Db::getInstance () -> execute('DELETE FROM '._DB_PREFIX_. 'cart_rule_carrier WHERE id_carrier = '.
        (int) this.id)&&
        this.deleteTaxRulesGroup(JeproLabLaboratoryModel.getLaboratoryIds(true, null, true)));
    }

    /**
     * Change carrier id in delivery prices when updating a carrier
     *
     * @param oldId Old id carrier
     */
    public void setConfiguration(int oldId){
        Db::getInstance()->execute('UPDATE `'._DB_PREFIX_.'delivery` SET `id_carrier` = '.(int)this.id.' WHERE `id_carrier` = '.(int)$id_old);
    }

    /**
     * Get delivery prices for a given order
     *
     * @param float $total_weight Total order weight
     * @param int zoneId Zone ID (for customer delivery address)
     * @return float Delivery price
     */
    public function getDeliveryPriceByWeight($total_weight, int zoneId) {
        carrierId = (int) this.id;
        $cache_key = carrierId. '_'.$total_weight. '_'.zoneId;
        if (!isset(self::$price_by_weight[$cache_key])){
            $sql = 'SELECT d.`price`
            FROM`'._DB_PREFIX_.' delivery`d
            LEFT JOIN`'._DB_PREFIX_.' range_weight`w ON (d.`id_range_weight`=w.`id_range_weight`)
            WHERE d.`id_zone`='.(int)zoneId.'
            AND '.(float)$total_weight.' >= w.`delimiter1`
            AND '.(float)$total_weight.' < w.`delimiter2`
            AND d.`id_carrier`='.carrierId.'
            '.JeproLabCarrierModel.sqlDeliveryRangeShop(' range_weight ').'
            ORDER BY w.`delimiter1`ASC ';
            $result = Db::getInstance (_PS_USE_SQL_SLAVE_) -> getRow($sql);
            if (!isset($result['price'])) {
                self::$price_by_weight[$cache_key]=this.getMaxDeliveryPriceByWeight(zoneId);
            } else {
                self::$price_by_weight[$cache_key]=$result['price'];
            }
        }

        $price_by_weight = Hook::exec
        ('actionDeliveryPriceByWeight', array('id_carrier' = > carrierId, 'total_weight' =>$total_weight, 'id_zone' =>
        zoneId));
        if (is_numeric($price_by_weight)) {
            self::$price_by_weight[$cache_key]=$price_by_weight;
        }

        return self::$price_by_weight[$cache_key];
    }

    public static boolean checkDeliveryPriceByWeight(int carrierId, float totalWeight, int zoneId) {
        carrierId = (int) carrierId;
        $cache_key = carrierId. '_'.$total_weight. '_'.zoneId;
        if (!isset(self::$price_by_weight2[$cache_key])){
            $sql = 'SELECT d.`price`
            FROM`'._DB_PREFIX_.' delivery`d
            LEFT JOIN`'._DB_PREFIX_.' range_weight`w ON d.`id_range_weight`=w.`id_range_weight`
            WHERE d.`id_zone`='.(int)zoneId.'
            AND '.(float)$total_weight.' >= w.`delimiter1`
            AND '.(float)$total_weight.' < w.`delimiter2`
            AND d.`id_carrier`='.carrierId.'
            '.JeproLabCarrierModel.sqlDeliveryRangeShop(' range_weight ').'
            ORDER BY w.`delimiter1`ASC ';
            $result = Db::getInstance (_PS_USE_SQL_SLAVE_) -> getRow($sql);
            self::$price_by_weight2[$cache_key]=(isset($result['price']));
        }

        $price_by_weight = Hook::exec
        ('actionDeliveryPriceByWeight', array('id_carrier' = > carrierId, 'total_weight' =>$total_weight, 'id_zone' =>
        zoneId));
        if (is_numeric($price_by_weight)) {
            self::$price_by_weight2[$cache_key]=$price_by_weight;
        }

        return self::$price_by_weight2[$cache_key];
    }


    public function getMaxDeliveryPriceByWeight(zoneId)
    {
        cacheKey = 'JeproLabCarrierModel.getMaxDeliveryPriceByWeight_'.(int)$this->id.'-'.(int)zoneId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $sql = 'SELECT d.`price`
        FROM `'._DB_PREFIX_.'delivery` d
        INNER JOIN `'._DB_PREFIX_.'range_weight` w ON d.`id_range_weight` = w.`id_range_weight`
        WHERE d.`id_zone` = '.(int)zoneId.'
        AND d.`id_carrier` = '.(int)$this->id.'
        '.JeproLabCarrierModel.sqlDeliveryRangeShop('range_weight').'
        ORDER BY w.`delimiter2` DESC';
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql);
        JeproLabCache.getInstance().store(cacheKey, $result);
        return $result;
    }
        return JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Get delivery prices for a given order
     *
     * @param float $order_total Order total to pay
     * @param int zoneId Zone id (for customer delivery address)
     * @param int|null $id_currency
     * @return float Delivery price
     */
    public function getDeliveryPriceByPrice($order_total, zoneId, $id_currency = null)
    {
        carrierId = (int)$this->id;
        $cache_key = $this->id.'_'.$order_total.'_'.zoneId.'_'.$id_currency;
        if (!isset(self::$price_by_price[$cache_key])) {
        if (!empty($id_currency)) {
            $order_total = Tools::convertPrice($order_total, $id_currency, false);
        }

        $sql = 'SELECT d.`price`
        FROM `'._DB_PREFIX_.'delivery` d
        LEFT JOIN `'._DB_PREFIX_.'range_price` r ON d.`id_range_price` = r.`id_range_price`
        WHERE d.`id_zone` = '.(int)zoneId.'
        AND '.(float)$order_total.' >= r.`delimiter1`
        AND '.(float)$order_total.' < r.`delimiter2`
        AND d.`id_carrier` = '.carrierId.'
        '.JeproLabCarrierModel.sqlDeliveryRangeShop('range_price').'
        ORDER BY r.`delimiter1` ASC';
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow($sql);
        if (!isset($result['price'])) {
            self::$price_by_price[$cache_key] = $this->getMaxDeliveryPriceByPrice(zoneId);
        } else {
            self::$price_by_price[$cache_key] = $result['price'];
        }
    }

        $price_by_price = Hook::exec('actionDeliveryPriceByPrice', array('id_carrier' => carrierId, 'order_total' => $order_total, 'id_zone' => zoneId));
        if (is_numeric($price_by_price)) {
            self::$price_by_price[$cache_key] = $price_by_price;
        }

        return self::$price_by_price[$cache_key];
    }

    /**
     * Check delivery prices for a given order
     *
     * @param int carrierId
     * @param float $order_total Order total to pay
     * @param int zoneId Zone id (for customer delivery address)
     * @param int|null $id_currency
     * @return float Delivery price
     */
    public static function checkDeliveryPriceByPrice(carrierId, $order_total, zoneId, $id_currency = null)
    {
        carrierId = (int)carrierId;
        $cache_key = carrierId.'_'.$order_total.'_'.zoneId.'_'.$id_currency;
        if (!isset(self::$price_by_price2[$cache_key])) {
        if (!empty($id_currency)) {
            $order_total = Tools::convertPrice($order_total, $id_currency, false);
        }

        $sql = 'SELECT d.`price`
        FROM `'._DB_PREFIX_.'delivery` d
        LEFT JOIN `'._DB_PREFIX_.'range_price` r ON d.`id_range_price` = r.`id_range_price`
        WHERE d.`id_zone` = '.(int)zoneId.'
        AND '.(float)$order_total.' >= r.`delimiter1`
        AND '.(float)$order_total.' < r.`delimiter2`
        AND d.`id_carrier` = '.carrierId.'
        '.JeproLabCarrierModel.sqlDeliveryRangeShop('range_price').'
        ORDER BY r.`delimiter1` ASC';
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow($sql);
        self::$price_by_price2[$cache_key] = (isset($result['price']));
    }

        $price_by_price = Hook::exec('actionDeliveryPriceByPrice', array('id_carrier' => carrierId, 'order_total' => $order_total, 'id_zone' => zoneId));
        if (is_numeric($price_by_price)) {
            self::$price_by_price2[$cache_key] = $price_by_price;
        }

        return self::$price_by_price2[$cache_key];
    }

    public function getMaxDeliveryPriceByPrice(zoneId)
    {
        cacheKey = 'JeproLabCarrierModel.getMaxDeliveryPriceByPrice_'.(int)$this->id.'-'.(int)zoneId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $sql = 'SELECT d.`price`
        FROM `'._DB_PREFIX_.'delivery` d
        INNER JOIN `'._DB_PREFIX_.'range_price` r ON d.`id_range_price` = r.`id_range_price`
        WHERE d.`id_zone` = '.(int)zoneId.'
        AND d.`id_carrier` = '.(int)$this->id.'
        '.JeproLabCarrierModel.sqlDeliveryRangeShop('range_price').'
        ORDER BY r.`delimiter2` DESC';
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql);
        JeproLabCache.getInstance().store(cacheKey, $result);
    }
        return JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Get delivery prices for a given shipping method (price/weight)
     *
     * @param string $rangeTable Table name (price or weight)
     * @return array Delivery prices
     */
    public static function getDeliveryPriceByRanges($range_table, carrierId)
    {
        $sql = 'SELECT d.`id_'.bqSQL($range_table).'`, d.id_carrier, d.id_zone, d.price
        FROM '._DB_PREFIX_.'delivery d
        LEFT JOIN `'._DB_PREFIX_.bqSQL($range_table).'` r ON r.`id_'.bqSQL($range_table).'` = d.`id_'.bqSQL($range_table).'`
        WHERE d.id_carrier = '.(int)carrierId.'
        AND d.`id_'.bqSQL($range_table).'` IS NOT NULL
        AND d.`id_'.bqSQL($range_table).'` != 0
        '.JeproLabCarrierModel.sqlDeliveryRangeShop($range_table).'
        ORDER BY r.delimiter1';
        return Db::getInstance()->executeS($sql);
    }

    /**
     * Get all carriers in a given language
     *
     * @param int langId Language id
     * @param $modules_filters, possible values:
    PS_CARRIERS_ONLY
    CARRIERS_MODULE
    CARRIERS_MODULE_NEED_RANGE
    PS_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE
    ALL_CARRIERS
     * @param bool $active Returns only active carriers when true
     * @return array Carriers
     */
    public static function getCarriers(langId, $active = false, $delete = false, zoneId = false, $ids_group = null, $modules_filters = self::PS_CARRIERS_ONLY){
        // Filter by groups and no groups => return empty array
        if ($ids_group && (!is_array($ids_group) || !count($ids_group))) {
            return array();
        }

        $sql = '
        SELECT c.*, cl.delay
        FROM `'._DB_PREFIX_.'carrier` c
        LEFT JOIN `'._DB_PREFIX_.'carrier_lang` cl ON (c.`id_carrier` = cl.`id_carrier` AND cl.`id_lang` = '.(int)langId.Shop::addSqlRestrictionOnLang('cl').')
        LEFT JOIN `'._DB_PREFIX_.'carrier_zone` cz ON (cz.`id_carrier` = c.`id_carrier`)'.
        (zoneId ? 'LEFT JOIN `'._DB_PREFIX_.'zone` z ON (z.`id_zone` = '.(int)zoneId.')' : '').'
        '.Shop::addSqlAssociation('carrier', 'c').'
        WHERE c.`deleted` = '.($delete ? '1' : '0');
        if ($active) {
            $sql .= ' AND c.`active` = 1 ';
        }
        if (zoneId) {
            $sql .= ' AND cz.`id_zone` = '.(int)zoneId.' AND z.`active` = 1 ';
        }
        if ($ids_group) {
            $sql .= ' AND EXISTS (SELECT 1 FROM '._DB_PREFIX_.'carrier_group
            WHERE '._DB_PREFIX_.'carrier_group.id_carrier = c.id_carrier
            AND id_group IN ('.implode(',', array_map('intval', $ids_group)).')) ';
        }

        switch ($modules_filters) {
            case 1 :
                $sql .= ' AND c.is_module = 0 ';
                break;
            case 2 :
                $sql .= ' AND c.is_module = 1 ';
                break;
            case 3 :
                $sql .= ' AND c.is_module = 1 AND c.need_range = 1 ';
                break;
            case 4 :
                $sql .= ' AND (c.is_module = 0 OR c.need_range = 1) ';
                break;
        }
        $sql .= ' GROUP BY c.`id_carrier` ORDER BY c.`position` ASC';

        cacheKey = 'JeproLabCarrierModel.getCarriers_'.md5($sql);
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $carriers = Db::getInstance()->executeS($sql);
        JeproLabCache.getInstance().store(cacheKey, $carriers);
    } else {
        $carriers = JeproLabCache.getInstance().retrieve(cacheKey);
    }

        foreach ($carriers as $key => $carrier) {
        if ($carrier['name'] == '0') {
            $carriers[$key]['name'] = JeproLabCarrierModel.getCarrierNameFromShopName();
        }
    }
        return $carriers;
    }

    public static function getDeliveredCountries(langId, $active_countries = false, $active_carriers = false, $contain_states = null)
    {
        if (!Validate::isBool($active_countries) || !Validate::isBool($active_carriers)) {
        die(Tools::displayError());
    }

        $states = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT s.*
                    FROM `'._DB_PREFIX_.'state` s
        ORDER BY s.`name` ASC');

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT cl.*,c.*, cl.`name` AS country, zz.`name` AS zone
            FROM `'._DB_PREFIX_.'country` c'.
        Shop::addSqlAssociation('country', 'c').'
        LEFT JOIN `'._DB_PREFIX_.'country_lang` cl ON (c.`id_country` = cl.`id_country` AND cl.`id_lang` = '.(int)langId.')
        INNER JOIN (`'._DB_PREFIX_.'carrier_zone` cz INNER JOIN `'._DB_PREFIX_.'carrier` cr ON ( cr.id_carrier = cz.id_carrier AND cr.deleted = 0 '.
        ($active_carriers ? 'AND cr.active = 1) ' : ') ').'
        LEFT JOIN `'._DB_PREFIX_.'zone` zz ON cz.id_zone = zz.id_zone) ON zz.`id_zone` = c.`id_zone`
        WHERE 1
        '.($active_countries ? 'AND c.active = 1' : '').'
        '.(!is_null($contain_states) ? 'AND c.`contains_states` = '.(int)$contain_states : '').'
        ORDER BY cl.name ASC');

        $countries = array();
        foreach ($result as &$country) {
        $countries[$country['id_country']] = $country;
    }
        foreach ($states as &$state) {
        if (isset($countries[$state['id_country']])) { /* Does not keep the state if its country has been disabled and not selected */
            if ($state['active'] == 1) {
                $countries[$state['id_country']]['states'][] = $state;
            }
        }
    }

        return $countries;
    }

    public static int getDefaultCarrierSelection(List carriers) {
        return getDefaultCarrierSelection(carriers, 0);
    }

    /**
     * Return the default carrier to use
     *
     * @param array $carriers
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
            //$carrier = new Carrier((int) $row['id_carrier']);
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
            $row['price'] = ((shippingMethod == JeproLabCarrierModel.SHIPPING_METHOD_FREE) ? 0 : $cart -> getPackageShippingCost((int) $row['id_carrier'], true, null, null, zoneId));
            $row['price_tax_exc'] = ((shippingMethod == JeproLabCarrierModel.SHIPPING_METHOD_FREE) ? 0 : $cart -> getPackageShippingCost((int) $row['id_carrier'], false, null, null, zoneId));
            $row['img'] = file_exists(_PS_SHIP_IMG_DIR_. (int) $row['id_carrier']. '.jpg' )?_THEME_SHIP_DIR_.
            (int) $row['id_carrier']. '.jpg':'';

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
    public function getZones(){
        return Db::getInstance()->executeS('
            SELECT *
                    FROM `'._DB_PREFIX_.'carrier_zone` cz
        LEFT JOIN `'._DB_PREFIX_.'zone` z ON cz.`id_zone` = z.`id_zone`
        WHERE cz.`id_carrier` = '.(int)$this->id);
    }

    /**
     * Get a specific zones
     *
     * @return array Zone
     */
    public JeproLabCountryModel.JeproLabZoneModel getZone(int zoneId){
        return Db::getInstance()->executeS('
            SELECT *
                    FROM `'._DB_PREFIX_.'carrier_zone`
        WHERE `id_carrier` = '.(int)$this->id.'
        AND `id_zone` = '.(int)zoneId);
    }

    /**
     * Gets the highest carrier position
     *
     * @since 1.5.0
     * @return int $position
     */
    public static int getHigherPosition()
    {
        $sql = 'SELECT MAX(`position`)
        FROM `'._DB_PREFIX_.'carrier`
        WHERE `deleted` = 0';
        $position = DB::getInstance()->getValue($sql);
        return (is_numeric($position)) ? $position : -1;
    }

    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId)
    {
        return getAvailableCarrierList(analyze, warehouseId, deliveryAddressId, 0, null, new ArrayList<Integer>());
    }

    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId, int labId)
    {
        return getAvailableCarrierList(analyze, warehouseId, deliveryAddressId, labId, null, new ArrayList<Integer>());
    }

    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId, int labId, JeproLabCartModel cart)
    {
        return getAvailableCarrierList(analyze, warehouseId, deliveryAddressId, labId, cart, new ArrayList<Integer>());
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
    public static List<JeproLabCarrierModel> getAvailableCarrierList(JeproLabAnalyzeModel analyze, int warehouseId, int deliveryAddressId, int labId, JeproLabCartModel cart, &$error = array()){
        static $ps_country_default = null;

        if ($ps_country_default === null) {
            $ps_country_default = Configuration::get('PS_COUNTRY_DEFAULT');
        }

        if (is_null(labId)) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        if (is_null($cart)) {
            $cart = JeproLabContext.getContext().cart;
        }

        if (is_null($error) || !is_array($error)) {
            $error = array();
        }

        int addressId = ((deliveryAddressId != 0) ? deliveryAddressId :  cart.delivery_address_id);
        if (addressId > 0) {
            zoneId = JeproLabAddressModel.getZoneIdByAddressId(addressId);

            // Check the country of the address is activated
            if (!Address::isCountryActiveById($id_address)) {
                return array();
            }
        } else {
            $country = new Country($ps_country_default);
            zoneId = $country->id_zone;
        }

        // Does the product is linked with carriers?
        String cacheKey = 'JeproLabCarrierModel.getAvailableCarrierList_'.(int)$product->id.'-'.(int)labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
        $query = new DbQuery();
        $query->select('id_carrier');
        $query->from('product_carrier', 'pc');
        $query->innerJoin(
                'carrier',
                'c',
                'c.id_reference = pc.id_carrier_reference AND c.deleted = 0 AND c.active = 1'
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
                if (JeproLabCarrierModel.checkCarrierZone($carrier['id_carrier'], zoneId)) {
                    $carrier_list[$carrier['id_carrier']] = $carrier['id_carrier'];
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
        $available_carrier_list[$carrier['id_carrier']] = $carrier['id_carrier'];
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
    public static function assignGroupToAllCarriers($id_group_list, $exception = null)
    {
        if (!is_array($id_group_list)) {
            $id_group_list = array($id_group_list);
        }

        Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'carrier_group`
        WHERE `id_group` IN ('.join(',', $id_group_list).')');

        $carrier_list = Db::getInstance()->executeS('
            SELECT id_carrier FROM `'._DB_PREFIX_.'carrier`
        WHERE deleted = 0
        '.(is_array($exception) ? 'AND id_carrier NOT IN ('.join(',', $exception).')' : ''));

        if ($carrier_list) {
            $data = array();
            foreach ($carrier_list as $carrier) {
                foreach ($id_group_list as $id_group) {
                    $data[] = array(
                            'id_carrier' => $carrier['id_carrier'],
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
    
    public function setGroups($groups, boolean delete = true){
        if ($delete) {
            Db::getInstance()->execute('DELETE FROM '._DB_PREFIX_.'carrier_group WHERE id_carrier = '.(int)$this->id);
        }
        if (!is_array($groups) || !count($groups)) {
            return true;
        }
        $sql = 'INSERT INTO '._DB_PREFIX_.'carrier_group (id_carrier, id_group) VALUES ';
        foreach ($groups as $id_group) {
            $sql. = '('. (int) $this -> id. ', '. (int) $id_group. '),';
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
    public static function getCarrierNameFromShopName()
    {
        return str_replace(
                array('#', ';'),
                '',
                Configuration::get('PS_SHOP_NAME')
        );
    }

    /**
     * Add zone
     */
    public boolean addZone(int zoneId){
        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }
        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_carrier_zone") + "( " ;
        query += staticDataBaseObject.quoteName("carrier_id") + ", " + staticDataBaseObject.quoteName("zone_id");
        query += ") VALUES (" + this.carrier_id + ", " + zoneId + ") ";
        staticDataBaseObject.setQuery(query);
        if (staticDataBaseObject.query(true)) {
        // Get all ranges for this carrier
        $ranges_price = RangePrice::getRanges($this->id);
        $ranges_weight = RangeWeight::getRanges($this->id);
        // Create row in ps_delivery table
        if (count($ranges_price) || count($ranges_weight)) {
            $sql = 'INSERT INTO `'._DB_PREFIX_.'delivery` (`id_carrier`, `id_range_price`, `id_range_weight`, `id_zone`, `price`) VALUES ';
            if (count($ranges_price)) {
                foreach ($ranges_price as $range) {
                    $sql .= '('.(int)$this->id.', '.(int)$range['id_range_price'].', 0, '.(int)zoneId.', 0),';
                }
            }

            if (count($ranges_weight)) {
                foreach ($ranges_weight as $range) {
                    $sql .= '('.(int)$this->id.', 0, '.(int)$range['id_range_weight'].', '.(int)zoneId.', 0),';
                }
            }
            $sql = rtrim($sql, ',');

            return Db::getInstance()->execute($sql);
        }
        return true;
    }
    return false;
}

    /**
     * Delete zone
     */
    public function deleteZone(zoneId)
    {
        if (Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'carrier_zone`
        WHERE `id_carrier` = '.(int)$this->id.'
        AND `id_zone` = '.(int)zoneId.' LIMIT 1
        ')) {
        return Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'delivery`
        WHERE `id_carrier` = '.(int)$this->id.'
        AND `id_zone` = '.(int)zoneId);
    }
return false;
        }

/**
 * Gets a specific group
 *
 * @since 1.5.0
 * @return array Group
 */
public function getGroups()
        {
        return Db::getInstance()->executeS('
        SELECT id_group
        FROM '._DB_PREFIX_.'carrier_group
        WHERE id_carrier='.(int)$this->id);
        }

/**
 * Clean delivery prices (weight/price)
 *
 * @param string $rangeTable Table name to clean (weight or price according to shipping method)
 * @return bool Deletion result
 */
public function deleteDeliveryPrice($range_table)
        {
        $where = '`id_carrier` = '.(int)$this->id.' AND (`id_'.bqSQL($range_table).'` IS NOT NULL OR `id_'.bqSQL($range_table).'` = 0) ';

        if (Shop::getContext() == Shop::CONTEXT_ALL) {
        $where .= 'AND id_shop IS NULL AND id_shop_group IS NULL';
        } elseif (Shop::getContext() == Shop::CONTEXT_GROUP) {
        $where .= 'AND id_shop IS NULL AND id_shop_group = '.(int)Shop::getContextShopGroupID();
        } else {
        $where .= 'AND id_shop = '.(int)Shop::getContextShopID();
        }

        return Db::getInstance()->delete('delivery', $where);
        }

/**
 * Add new delivery prices
 *
 * @param array $priceList Prices list in multiple arrays (changed to array since 1.5.0)
 * @return bool Insertion result
 */
public function addDeliveryPrice($price_list, $delete = false)
        {
        if (!$price_list) {
        return false;
        }

        $keys = array_keys($price_list[0]);
        if (!in_array('id_shop', $keys)) {
        $keys[] = 'id_shop';
        }
        if (!in_array('id_shop_group', $keys)) {
        $keys[] = 'id_shop_group';
        }

        $sql = 'INSERT INTO `'._DB_PREFIX_.'delivery` ('.implode(', ', $keys).') VALUES ';
        foreach ($price_list as $values) {
        if (!isset($values['id_shop'])) {
        $values['id_shop'] = (Shop::getContext() == Shop::CONTEXT_SHOP) ? Shop::getContextShopID() : null;
        }
        if (!isset($values['id_shop_group'])) {
        $values['id_shop_group'] = (Shop::getContext() != Shop::CONTEXT_ALL) ? Shop::getContextShopGroupID() : null;
        }

        if ($delete) {
        Db::getInstance()->execute(
        'DELETE FROM `'._DB_PREFIX_.'delivery`
        WHERE '.(is_null($values['id_shop']) ? 'ISNULL(`id_shop`) ' : 'id_shop = '.(int)$values['id_shop']).'
        AND '.(is_null($values['id_shop_group']) ? 'ISNULL(`id_shop`) ' : 'id_shop_group='.(int)$values['id_shop_group']).'
        AND id_carrier='.(int)$values['id_carrier'].
        ($values['id_range_price'] !== null ? ' AND id_range_price='.(int)$values['id_range_price'] : ' AND (ISNULL(`id_range_price`) OR `id_range_price` = 0)').
        ($values['id_range_weight'] !== null ? ' AND id_range_weight='.(int)$values['id_range_weight'] : ' AND (ISNULL(`id_range_weight`) OR `id_range_weight` = 0)').'
        AND id_zone='.(int)$values['id_zone']
        );
        }

        $sql .= '(';
        foreach ($values as $v) {
        if (is_null($v)) {
        $sql .= 'NULL';
        } elseif (is_int($v) || is_float($v)) {
        $sql .= $v;
        } else {
        $sql .= '\''.$v.'\'';
        }
        $sql .= ', ';
        }
        $sql = rtrim($sql, ', ').'), ';
        }
        $sql = rtrim($sql, ', ');
        return Db::getInstance()->execute($sql);
        }

    /**
     * Copy old carrier informations when update carrier
     *
     * @param int $oldId Old id carrier (copy from that id)
     */
    public function copyCarrierData($old_id){
        if (!Validate::isUnsignedId($old_id)) {
        throw new PrestaShopException('Incorrect identifier for carrier');
        }

        if (!$this->id) {
        return false;
        }

        $old_logo = _PS_SHIP_IMG_DIR_.'/'.(int)$old_id.'.jpg';
        if (file_exists($old_logo)) {
        copy($old_logo, _PS_SHIP_IMG_DIR_.'/'.(int)$this->id.'.jpg');
        }

        $old_tmp_logo = _PS_TMP_IMG_DIR_.'/carrier_mini_'.(int)$old_id.'.jpg';
        if (file_exists($old_tmp_logo)) {
        if (!isset($_FILES['logo'])) {
        copy($old_tmp_logo, _PS_TMP_IMG_DIR_.'/carrier_mini_'.$this->id.'.jpg');
        }
        unlink($old_tmp_logo);
        }

        // Copy existing ranges price
        foreach (array('range_price', 'range_weight') as $range) {
        $res = Db::getInstance()->executeS('
        SELECT `id_'.$range.'` as id_range, `delimiter1`, `delimiter2`
        FROM `'._DB_PREFIX_.$range.'`
        WHERE `id_carrier` = '.(int)$old_id);
        if (count($res)) {
        foreach ($res as $val) {
        Db::getInstance()->execute('
        INSERT INTO `'._DB_PREFIX_.$range.'` (`id_carrier`, `delimiter1`, `delimiter2`)
        VALUES ('.$this->id.','.(float)$val['delimiter1'].','.(float)$val['delimiter2'].')');
        $range_id = (int)Db::getInstance()->Insert_ID();

        $range_price_id = ($range == 'range_price') ? $range_id : 'NULL';
        $range_weight_id = ($range == 'range_weight') ? $range_id : 'NULL';

        Db::getInstance()->execute('
        INSERT INTO `'._DB_PREFIX_.'delivery` (`id_carrier`, `id_shop`, `id_shop_group`, `id_range_price`, `id_range_weight`, `id_zone`, `price`) (
        SELECT '.(int)$this->id.', `id_shop`, `id_shop_group`, '.(int)$range_price_id.', '.(int)$range_weight_id.', `id_zone`, `price`
        FROM `'._DB_PREFIX_.'delivery`
        WHERE `id_carrier` = '.(int)$old_id.'
        AND `id_'.$range.'` = '.(int)$val['id_range'].'
        )
        ');
        }
        }
        }

        // Copy existing zones
        $res = Db::getInstance()->executeS('
        SELECT *
        FROM `'._DB_PREFIX_.'carrier_zone`
        WHERE id_carrier = '.(int)$old_id);
        foreach ($res as $val) {
        Db::getInstance()->execute('
        INSERT INTO `'._DB_PREFIX_.'carrier_zone` (`id_carrier`, `id_zone`)
        VALUES ('.$this->id.','.(int)$val['id_zone'].')
        ');
        }

        //Copy default carrier
        if (Configuration::get('PS_CARRIER_DEFAULT') == $old_id) {
        Configuration::updateValue('PS_CARRIER_DEFAULT', (int)$this->id);
        }

        // Copy reference
        $id_reference = Db::getInstance()->getValue('
        SELECT `id_reference`
        FROM `'._DB_PREFIX_.$this->def['table'].'`
        WHERE id_carrier = '.(int)$old_id);
        Db::getInstance()->execute('
        UPDATE `'._DB_PREFIX_.$this->def['table'].'`
        SET `id_reference` = '.(int)$id_reference.'
        WHERE `id_carrier` = '.(int)$this->id);

        $this->id_reference = (int)$id_reference;

        // Copy tax rules group
        Db::getInstance()->execute('INSERT INTO `'._DB_PREFIX_.'carrier_tax_rules_group_shop` (`id_carrier`, `id_tax_rules_group`, `id_shop`)
        (SELECT '.(int)$this->id.', `id_tax_rules_group`, `id_shop`
        FROM `'._DB_PREFIX_.'carrier_tax_rules_group_shop`
        WHERE `id_carrier`='.(int)$old_id.')');
        }

    /**
     * Get carrier using the reference id
     */
    public static JeproLabCarrierModel getCarrierByReference(int referenceId){
        // @todo class var $table must became static. here I have to use 'carrier' because this method is static
        int carrierId = Db::getInstance()->getValue('SELECT `id_carrier` FROM `'._DB_PREFIX_.'carrier`
        WHERE id_reference = '.(int)$id_reference.' AND deleted = 0 ORDER BY id_carrier DESC');
        if (!carrierId) {
        return false;
        }
        return new JeproLabCarrierModel(carrierId);
    }

    /**
     * Check if carrier is used (at least one order placed)
     *
     * @return int Order count for this carrier
     */
    public function isUsed(){
        $row = Db::getInstance()->getRow('
        SELECT COUNT(`id_carrier`) AS total
        FROM `'._DB_PREFIX_.'orders`
        WHERE `id_carrier` = '.(int)$this->id);

        return (int)$row['total'];
    }

    public int getShippingMethod() {
        if (this.is_free) {
            return JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_FREE;
        }

        int method = this.shipping_method;

        if (this.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_DEFAULT) {
            // backward compatibility
            if ((int) Configuration::get ('PS_SHIPPING_METHOD' )){
                method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT;
            }else{
                method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE;
            }
        }

        return method;
    }

    public function getRangeTable() {
        int shippingMethod = this.getShippingMethod();
        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
            return 'range_weight';
        }else if(shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
            return 'range_price';
        }
        return false;
    }

    public function getRangeObject() {
        return getRangeObject(0);
    }

    public function getRangeObject(int shippingMethod) {
        if (shippingMethod <= 0) {
            shippingMethod = this.getShippingMethod();
        }

        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
            return new RangeWeight();
        }
        elseif(shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
            return new RangePrice();
        }
        return false;
    }

    public function getRangeSuffix() { return getRangeSuffix(null); }

    public function getRangeSuffix(JeproLabCurrencyModel currency) {
        if (!currency) {
            currency = JeproLabContext.getContext().currency;
        }
        $suffix = Configuration::get ('PS_WEIGHT_UNIT' );
        if (this.getShippingMethod() == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
            $suffix = currency.sign;
        }
        return $suffix;
    }

    public int getTaxRulesGroupId(){
        return JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(this.carrier_id, null);
    }

    public int getTaxRulesGroupId(JeproLabContext context){
        return JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(this.carrier_id, context);
    }

    public function deleteTaxRulesGroup(array $shops = null) {
        if (!$shops) {
            $shops = Shop::getContextListShopID ();
        }

        $where = 'id_carrier = '. (int) $this -> id;
        if ($shops) {
            $where. = ' AND id_shop IN('.implode(', ', array_map('intval', $shops)). ')';
        }
        return Db::getInstance () -> delete('carrier_tax_rules_group_shop', $where);
    }

    public function setTaxRulesGroup($id_tax_rules_group, $all_shops = false) {
        if (!Validate::isUnsignedId ($id_tax_rules_group)){
            die(Tools::displayError ());
        }

        if (!$all_shops) {
            $shops = Shop::getContextListShopID ();
        } else {
            $shops = Shop::getShops (true, null, true);
        }

        $this -> deleteTaxRulesGroup($shops);

        $values = array();
        foreach($shops as labId) {
            $values[]=array(
                    'id_carrier' = > (int) $this -> id,
                    'id_tax_rules_group' =>(int) $id_tax_rules_group,
                    'id_shop' =>(int) labId,
            );
        }
        JeproLabCache.getInstance().clean('carrier_id_tax_rules_group_'. (int) $this -> id. '_'.
        (int) JeproLabContext.getContext().shop->id);
        return Db::getInstance () -> insert('carrier_tax_rules_group_shop', $values);
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

    /**
     * This tricky method generates a sql clause to check if ranged data are overloaded by multishop
     *
     * @since 1.5.0
     * @param string $rangeTable
     * @return string
     */
    public static function sqlDeliveryRangeShop($range_table, $alias = 'd'){
        if (Shop::getContext() == Shop::CONTEXT_ALL) {
        $where = 'AND d2.id_shop IS NULL AND d2.id_shop_group IS NULL';
        } elseif (Shop::getContext() == Shop::CONTEXT_GROUP) {
        $where = 'AND ((d2.id_shop_group IS NULL OR d2.id_shop_group = '.Shop::getContextShopGroupID().') AND d2.id_shop IS NULL)';
        } else {
        $where = 'AND (d2.id_shop = '.Shop::getContextShopID().' OR (d2.id_shop_group = '.Shop::getContextShopGroupID().'
        AND d2.id_shop IS NULL) OR (d2.id_shop_group IS NULL AND d2.id_shop IS NULL))';
        }

        $sql = 'AND '.$alias.'.id_delivery = (
        SELECT d2.id_delivery
        FROM '._DB_PREFIX_.'delivery d2
        WHERE d2.id_carrier = `'.bqSQL($alias).'`.id_carrier
        AND d2.id_zone = `'.bqSQL($alias).'`.id_zone
        AND d2.`id_'.bqSQL($range_table).'` = `'.bqSQL($alias).'`.`id_'.bqSQL($range_table).'`
        '.$where.'
        ORDER BY d2.id_shop DESC, d2.id_shop_group DESC
        LIMIT 1
        )';
        return $sql;
    }

    /**
     * Moves a carrier
     *
     * @since 1.5.0
     * @param bool $way Up (1) or Down (0)
     * @param int $position
     * @return bool Update result
     */
    public boolean updatePosition($way, $position){
        if (!$res = Db::getInstance()->executeS(
        'SELECT `id_carrier`, `position`
        FROM `'._DB_PREFIX_.'carrier`
        WHERE `deleted` = 0
        ORDER BY `position` ASC'
        )) {
        return false;
        }

        foreach ($res as $carrier) {
        if ((int)$carrier['id_carrier'] == (int)$this->id) {
        $moved_carrier = $carrier;
        }
        }

        if (!isset($moved_carrier) || !isset($position)) {
            return false;
        }

        // < and > statements rather than BETWEEN operator
        // since BETWEEN is treated differently according to databases
        return (Db::getInstance()->execute('
        UPDATE `'._DB_PREFIX_.'carrier`
        SET `position`= `position` '.($way ? '- 1' : '+ 1').'
        WHERE `position`
        '.($way
        ? '> '.(int)$moved_carrier['position'].' AND `position` <= '.(int)$position
        : '< '.(int)$moved_carrier['position'].' AND `position` >= '.(int)$position.'
        AND `deleted` = 0'))
        && Db::getInstance()->execute('
        UPDATE `'._DB_PREFIX_.'carrier`
        SET `position` = '.(int)$position.'
        WHERE `id_carrier` = '.(int)$moved_carrier['id_carrier']));
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
