package com.jeprolab.models;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;

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
    public $format;

    /** @var int bool Display decimals on prices */
    public boolean decimals;

    /** @var int bool active */
    public boolean published;


    public JeproLabCurrencyModel(){

    }

    public JeproLabCurrencyModel(int currencyId){

    }

    /**
     * Overriding check if currency rate is not empty and if currency with the same iso code already exists.
     * If it's true, currency is not added.
     *
     * @see ObjectModelCore::add()
     */
    public boolean addCurrency($autodate = true, $nullValues = false)
    {
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

    /**
     * Check if a curency already exists.
     *
     * @param int|string $iso_code int for iso code number string for iso code
     * @return bool
     */
    public static boolean exists($iso_code, $iso_code_num, int labId = 0)
    {
        if (is_int($iso_code)) {
            $id_currency_exists = JeproLabCurrencyModel.getIdByIsoCodeNum((int)$iso_code_num, (int)$id_shop);
        } else {
            $id_currency_exists = JeproLabCurrencyModel.getIdByIsoCode($iso_code, (int)$id_shop);
        }

        if ($id_currency_exists) {
            return true;
        } else {
            return false;
        }
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

    public function delete()
    {
        if (this.id == Configuration::get('PS_CURRENCY_DEFAULT')) {
        $result = Db::getInstance()->getRow('SELECT `id_currency` FROM '._DB_PREFIX_.'currency WHERE `id_currency` != '.(int)(this.id).' AND `deleted` = 0');
        if (!$result['id_currency']) {
            return false;
        }
        Configuration::updateValue('PS_CURRENCY_DEFAULT', $result['id_currency']);
    }
        this.deleted = true;
        return this.update();
    }

    /**
     * Return formated sign
     *
     * @param string $side left or right
     * @return string formated sign
     */
    public function getCurrencySign($side = null)
    {
        if (!$side) {
            return this.sign;
        }
        $formated_strings = array(
                'left' => this.sign.' ',
            'right' => ' '.this.sign
        );

        $formats = array(
                1 => array('left' => &$formated_strings['left'], 'right' => ''),
        2 => array('left' => '', 'right' => &$formated_strings['right']),
        3 => array('left' => &$formated_strings['left'], 'right' => ''),
        4 => array('left' => '', 'right' => &$formated_strings['right']),
        5 => array('left' => '', 'right' => &$formated_strings['right'])
        );
        if (isset($formats[this.format][$side])) {
            return ($formats[this.format][$side]);
        }
        return this.sign;
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

    public static function getCurrency($id_currency)
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT *
                    FROM `'._DB_PREFIX_.'currency`
        WHERE `deleted` = 0
        AND `id_currency` = '.(int)($id_currency));
    }

    /**
     * @param $iso_code
     * @param int $id_shop
     * @return int
     */
    public static function getIdByIsoCode($iso_code, $id_shop = 0){
        $cache_id = 'JeproLabCurrencyModel.getIdByIsoCode_'.pSQL($iso_code).'-'.(int)$id_shop;
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