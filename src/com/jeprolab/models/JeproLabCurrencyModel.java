package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JeproLabCurrencyModel extends JeproLabModel {
    public int currency_id;

    /** @var string Name */
    public Map<String, String> name;

    /** @var string Iso code */
    public String iso_code;

    /** @var string Iso code numeric */
    public int iso_code_num;

    /** @var string Symbol for short display */
    public String sign;

    /** @var int bool used for displaying blank between sign and price */
    public boolean blank;

    /** @var string exchange rate from euros */
    public double conversion_rate;

    /** @var bool True if currency has been deleted (staying in database as deleted) */
    public boolean deleted = false;

    /** @var int ID used for displaying prices */
    public int format;

    /** @var int bool Display decimals on prices */
    public boolean decimals;

    /** @var int bool active */
    public boolean published;

    /**
     * contains the sign to display before price, according to its format
     * @var string
     */
    public String prefix = null;
    /**
     * contains the sign to display after price, according to its format
     * @var string
     */
    public String suffix = null;

    public JeproLabCurrencyModel(){
        this(0);
    }

    public JeproLabCurrencyModel(int currencyId){
        this.prefix =  this.format % 2 != 0 ? this.sign + " " : "";
        this.suffix = this.format % 2 == 0 ? " " + this.sign : "";
        if (this.conversion_rate <= 0) {
            this.conversion_rate = 1;
        }
    }

    /**
     * Overriding check if currency rate is not empty and if currency with the same iso code already exists.
     * If it's true, currency is not added.
     *
     * @see ObjectModelCore::add()
     */
    public boolean addCurrency($autodate = true, $nullValues = false){
        if ((float)this.conversion_rate <= 0) {
            return false;
        }

        if(JeproLabCurrencyModel.exists(this.iso_code, this.iso_code_num)){
            return false;
        }else{
            boolean result = false;
            parent::add($autodate, $nullValues);
            return result;
        }
    }

    public function updateCurrency($autodate = true, $nullValues = false){
        if ((float)this.conversion_rate <= 0) {
            return false;
        }
        return parent::update($autodate, $nullValues);
    }

    public static boolean exists(String isoCode){
        return exists(isoCode, 0);
    }

    /**
     * Check if a currency already exists.
     *
     * @param isoCode String for iso code
     * @return bool
     */
    public static boolean exists(String isoCode, int labId){
        return JeproLabCurrencyModel.getIdByIsoCode(isoCode, labId) > 0;
    }

    public static boolean exists(int isoCodeNum){
        return exists(isoCodeNum, 0);
    }

    /**
     * Check if a currency already exists.
     *
     * @param isoCodeNum int for iso code number
     * @return bool
     */
    public static boolean exists(int isoCodeNum, int labId){
        int currencyId = JeproLabCurrencyModel.getIdByIsoCodeNum(isoCodeNum, labId);
        return currencyId > 0;
    }


    public function deleteSelection($selection)
    {
        if (!is_array($selection)) {
            return false;
        }

        $res = array();
        foreach ($selection as $id) {
        $obj = new JeproLabCurrencyModel((int)$id);
        $res[$id] = $obj->delete();
    }

        foreach ($res as $value) {
        if (!$value) {
            return false;
        }
    }
        return true;
    }

    public boolean delete() {
        if (this.currency_id == JeproLabSettingModel.getIntValue("default_currency")) {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("currency_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_currency");
            query += " WHERE " + dataBaseObject.quoteName("currency_id") + " != " + (this.currency_id) + " AND " + dataBaseObject.quoteName("deleted");
            query += " = 0";

            dataBaseObject.setQuery(query);
            ResultSet currencySet = dataBaseObject.loadObject();
            int currencyId = 0;
            try{
                while(currencySet.next()){
                    currencyId = currencySet.getInt("currency_id");
                }
            }catch (SQLException ignored){
                currencyId = 0;
            }

            if (currencyId <= 0) {
                return false;
            }
            JeproLabSettingModel.updateValue("default_currency", currencyId);
        }
        this.deleted = true;
        return this.update();
    }

    public String getCurrencySign(){
        return getCurrencySign(null);
    }
    /**
     * Return formated sign
     *
     * @param side String left or right
     * @return string formatted sign
     */
    public String getCurrencySign(String side) {
        if (side == null){
            return this.sign;
        }

        String currencySign;
        switch (side) {
            case "left" :
                if(this.format == 1){
                    currencySign = this.sign + " ";
                }else if(this.format == 2){
                    currencySign = "";
                }else if(this.format == 3){
                    currencySign = this.sign + " ";
                }else if(this.format == 4){
                    currencySign = "";
                }else if(this.format == 5){
                    currencySign = "";
                }
                break;
            case "right" :
                if(this.format == 1 || this.format == 3){
                    currencySign = "";
                }else{
                    currencySign = " " + this.sign;
                }
                break;
            default:
                currencySign = this.sign;
                break;
        }
        return currencySign;
    }

    /**
     * Return available currencies
     *
     * @return array Currencies
     */
    public static function getCurrencies($object = false, $active = true, $group_by = false)
    {
        $tab = Db::getInstance()->executeS('
            SELECT *
                    FROM `'._DB_PREFIX_.'currency` c
        '.Shop::addSqlAssociation('currency', 'c').
        ' WHERE `deleted` = 0'.
        ($active ? ' AND c.`active` = 1' : '').
        ($group_by ? ' GROUP BY c.`id_currency`' : '').
        ' ORDER BY `name` ASC');
        if ($object) {
            foreach ($tab as $key => $currency) {
                $tab[$key] = JeproLabCurrencyModel.getCurrencyInstance($currency['id_currency']);
            }
        }
        return $tab;
    }

    public static function getCurrenciesByIdShop($id_shop = 0)
    {
        return Db::getInstance()->executeS('
            SELECT *
                    FROM `'._DB_PREFIX_.'currency` c
        LEFT JOIN `'._DB_PREFIX_.'currency_shop` cs ON (cs.`id_currency` = c.`id_currency`)
        '.($id_shop ? ' WHERE cs.`id_shop` = '.(int)$id_shop : '').'
        ORDER BY `name` ASC');
    }


    public static function getPaymentCurrenciesSpecial($id_module, $id_shop = null)
    {
        if (is_null($id_shop)) {
            $id_shop = Context::getContext()->shop->id;
        }

        $sql = 'SELECT *
        FROM '._DB_PREFIX_.'module_currency
        WHERE id_module = '.(int)$id_module.'
        AND id_shop ='.(int)$id_shop;
        return Db::getInstance()->getRow($sql);
    }

    public static function getPaymentCurrencies($id_module, $id_shop = null)
    {
        if (is_null($id_shop)) {
            $id_shop = Context::getContext()->shop->id;
        }

        $sql = 'SELECT c.*
        FROM `'._DB_PREFIX_.'module_currency` mc
        LEFT JOIN `'._DB_PREFIX_.'currency` c ON c.`id_currency` = mc.`id_currency`
        WHERE c.`deleted` = 0
        AND mc.`id_module` = '.(int)$id_module.'
        AND c.`active` = 1
        AND mc.id_shop = '.(int)$id_shop.'
        ORDER BY c.`name` ASC';
        return Db::getInstance()->executeS($sql);
    }

    public static function checkPaymentCurrencies($id_module, $id_shop = null)
    {
        if (empty($id_module)) {
            return false;
        }

        if (is_null($id_shop)) {
            $id_shop = Context::getContext()->shop->id;
        }

        $sql = 'SELECT *
        FROM `'._DB_PREFIX_.'module_currency`
        WHERE `id_module` = '.(int)$id_module.'
        AND `id_shop` = '.(int)$id_shop;
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
    }

    public static JeproLabCurrencyModel getCurrency(int currencyId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency WHERE ";
        query += staticDataBaseObject.quoteName("deleted") + " = 0 AND " + staticDataBaseObject.quoteName("currency_id") ;
        query += " = " + currencyId;
        staticDataBaseObject.setQuery(query);
        ResultSet currencySet = staticDataBaseObject.loadObject();
        JeproLabCurrencyModel currency = new JeproLabCurrencyModel();
        try{
            while (currencySet.next()){
                currency.currency_id = currencySet.getInt("currency_id");
                /*currency. = currencySet
                currency.currency_id = currencySet
                currency.currency_id = currencySet
                currency.currency_id = currencySet*/
            }
        }catch (SQLException ignored){
            ignored.printStackTrace();
        }
        return currency;
    }

    public static int getIdByIsoCode(String isoCode){
        return getIdByIsoCode(isoCode, 0);
    }

    /**
     * @param isoCode
     * @param labId
     * @return int
     */
    public static int getIdByIsoCode(String isoCode, int labId){
        String cacheKey = "jeproLab_currency_model_get_id_by_iso_code_" + .pSQL(isoCode).'-'.(int)$id_shop;
        if (!Cache::isStored($cache_id)) {
        $query = JeproLabCurrencyModel.getIdByQuery($id_shop);
        $query->where('iso_code = \''.pSQL($iso_code).'\'');

        $result = (int)Db::getInstance(_PS_USE_SQL_SLAVE_) -> getValue($query->build());
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    public static int getIdByIsoCodeNum(int isoCodeNum){
        return getIdByIsoCodeNum(isoCodeNum, 0);
    }

    /**
     * @param isoCodeNum
     * @param labId
     * @return int
     */
    public static int getIdByIsoCodeNum(int isoCodeNum, int labId){
        String query = JeproLabCurrencyModel.getIdByQuery(labId);
        query += " AND " + staticDataBaseObject.quoteName("iso_code_num") + " = " + staticDataBaseObject.secureData(isoCodeNum);

        staticDataBaseObject.setQuery(query);
        return (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query->build());
    }

    public static String getIdByQuery(){
        return getIdByQuery(0);
    }

    /**
     * @param labId
     * @return DbQuery
     */
    public static String getIdByQuery(int labId){
        String query = "SELECT currency.currency_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ";
        String where = " WHERE currency.deleted = 0 ";
        if(JeproLabLaboratoryModel.isFeaturePublished() && labId > 0){
            query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON (currency.";
            query += "currency_id = currency_lab.currency_id) ";
            where += " AND lab_id = " + labId;
        }
        return query + where;
    }

    /**
     * Refresh the currency exchange rate
     * The XML file define exchange rate for each from a default currency ($isoCodeSource).
     *
     * @param SimpleXMLElement $data XML content which contains all the exchange rates
     * @param string $isoCodeSource The default currency used in the XML file
     * @param Currency $defaultCurrency The default currency object
     */
    public function refreshCurrency($data, $isoCodeSource, JeproLabCurrencyModel defaultCurrency){
        // fetch the exchange rate of the default currency
        $exchange_rate = 1;
        $tmp = this.conversion_rate;
        if ($defaultCurrency->iso_code != $isoCodeSource) {
            foreach ($data->currency as $currency) {
                if ($currency['iso_code'] == $defaultCurrency->iso_code) {
                    $exchange_rate = round((float)$currency['rate'], 6);
                    break;
                }
            }
        }

        if (defaultCurrency.iso_code.equals(this.iso_code)) {
            this.conversion_rate = 1;
        } else {
            if (this.iso_code == $isoCodeSource) {
                $rate = 1;
            } else {
                foreach ($data->currency as $obj) {
                    if (this.iso_code == strval($obj['iso_code'])) {
                        $rate = (float)$obj['rate'];
                        break;
                    }
                }
            }

            if (isset($rate)) {
                this.conversion_rate = round($rate / $exchange_rate, 6);
            }
        }

        if ($tmp != this.conversion_rate) {
            this.updateCurrency();
        }
    }

    public static JeproLabCurrencyModel getDefaultCurrency(){
        int currencyId = (int)JeproLabSettingModel.getIntValue("default_currency");
        if (currencyId == 0) {
            return null;
        }

        return new JeproLabCurrencyModel(currencyId);
    }

    public static void refreshCurrencies(){
        // Parse
        if (!$feed = Tools::simplexml_load_file(_PS_CURRENCY_FEED_URL_)) {
            Tools::displayError('Cannot parse feed.');
        }

        // Default feed currency (EUR)
        $isoCodeSource = strval($feed->source['iso_code']);
        JeproLabCurrencyModel defaultCurrency = JeproLabCurrencyModel.getDefaultCurrency();
        if (defaultCurrency == null || defaultCurrency.currency_id <= 0) {
            Tools::displayError('No default currency');
        }

        List currencies = JeproLabCurrencyModel.getCurrencies(true, false, true);
        for(JeproLabCurrencyModel currency : currencies) {
            /** @var Currency $currency */
            if (currency.currency_id != defaultCurrency.currency_id) {
                currency.refreshCurrency($feed -> list, $isoCodeSource, $default_currency);
            }
        }
    }

    /**
     * Get current currency
     *
     * @return JeproLabCurrencyModel
     */
    public static JeproLabCurrencyModel getCurrentCurrency(){
        return JeproLabContext.getContext().currency;
    }

    public static JeproLabCurrencyModel getCurrencyInstance(int currencyId){
        if (!JeproLabCurrencyModel.currencies.contains(currencyId)) {
            JeproLabCurrencyModel.currencies.put(currencyId, new JeproLabCurrencyModel(currencyId));
        }
        return JeproLabCurrencyModel.currencies.get(currencyId);
    }

    public double getConversationRate(){
        return (this.currency_id != JeproLabSettingModel.getIntValue("default_currency") )? this.conversion_rate : 1;
    }

    public static int countActiveCurrencies(){
        return countActiveCurrencies(0);
    }

    public static int countActiveCurrencies(int labId){
        if (labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        if (!JeproLabCurrencyModel.activeCurrencies.contains(labId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT COUNT(DISTINCT currency.currency_id) FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency");
            query += " AS currency LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON (";
            query += "currency_lab.currency_id = currency.currency_id AND currency_lab.lab_id = " + labId + ") WHERE currency.";
            query += staticDataBaseObject.quoteName("published") + " = 1";

            staticDataBaseObject.setQuery(query);
            int val = staticDataBaseObject.loadValue();
            JeproLabCurrencyModel.activeCurrencies.put(labId, val);
        }
        return JeproLabCategoryModel.activeCurrencies.get(labId);
    }

    public static boolean isMultiCurrencyActivated(){
        return isMultiCurrencyActivated(0);
    }

    public static boolean isMultiCurrencyActivated(int labId){
        return (JeproLabCurrencyModel.countActiveCurrencies(labId) > 1);
    }
}