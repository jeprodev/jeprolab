package com.jeprolab.models;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

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
public class JeproLabCurrencyModel extends  JeproLabModel{
    public int currency_id;

    /** @var string Name */
    public String name;

    public int laboratory_id;

    /** @var string Iso code */
    public String iso_code;

    /** @var string Iso code numeric */
    public int iso_code_num;

    /** @var string Symbol for short display */
    public String sign;

    /** @var int bool used for displaying blank between sign and price */
    public boolean blank;

    /** @var string exchange rate from euros */
    public float conversion_rate;

    /** @var bool True if currency has been deleted (staying in database as deleted) */
    public boolean deleted = false;

    /** @var int ID used for displaying prices */
    public int format;

    /** @var int bool Display decimals on prices */
    public int decimals;

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

    /** @var array JeproLabCurrencyModel cache */
    protected static Map<Integer, JeproLabCurrencyModel> currencies = new HashMap<>();
    protected static Map<Integer, Integer> activeCurrencies = new HashMap<>();

    public JeproLabCurrencyModel(){
        this(0);
    }

    public JeproLabCurrencyModel(int currencyId){
        if(currencyId > 0){
            String cacheKey = "jeprolab_currency_model_" + currencyId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ";
                if(JeproLabLaboratoryModel.isTableAssociated("currency")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON( currency.";
                    query += "currency_id = currency_lab.currency_id AND currency_lab.lab_id = " + this.laboratory_id + ")";
                }
                query += " WHERE currency.currency_id = " + currencyId ;
                dataBaseObject.setQuery(query);
                ResultSet resultSet = dataBaseObject.loadObjectList();
                if(resultSet != null){
                    try{
                        if(resultSet.next()){
                            this.currency_id = resultSet.getInt("currency_id");
                            this.iso_code_num = resultSet.getInt("iso_code_num");
                            this.iso_code = resultSet.getString("iso_code");
                            this.sign = resultSet.getString("sign");
                            this.blank = resultSet.getInt("blank") > 0;
                            this.format = resultSet.getInt("format");
                            //this = resultSet.get("");
                            this.decimals = resultSet.getInt("decimals");
                            this.conversion_rate = resultSet.getFloat("conversion_rate");
                            this.deleted = resultSet.getInt("deleted") > 0;
                            this.published = resultSet.getInt("published") > 0;
                        }
                    }catch(SQLException ignored){
                        ignored.printStackTrace();
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
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
     */
    public boolean add(){
        if (this.conversion_rate <= 0) {
            return false;
        }
        return JeproLabCurrencyModel.exists(this.iso_code, this.iso_code_num) ? false : save();
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
            ResultSet currencySet = dataBaseObject.loadObjectList();
            int currencyId = 0;
            try{
                while(currencySet.next()){
                    currencyId = currencySet.getInt("currency_id");
                }
            }catch (SQLException ignored){
                currencyId = 0;
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (currencyId <= 0) {
                return false;
            }
            JeproLabSettingModel.updateValue("default_currency", currencyId);
        }
        this.deleted = true;
        return this.updateField("currency", "deleted", "1", " WHERE " + dataBaseObject.quoteName("currency_id") + " = " + this.currency_id, "int");
    }

    public boolean update(){
        if (this.conversion_rate <= 0) {
            return false;
        }
        return parent::update($autodate, $nullValues);
    }

    public boolean deleteSelection(List<Integer> selection){
        if (selection.isEmpty()){
            return false;
        }

        boolean res = true;
        JeproLabCurrencyModel currency;
        for(Integer id : selection) {
            currency = new JeproLabCurrencyModel(id);
            res &= currency.delete();
        }
        return res;
    }

    public static boolean exists(String isoCode, int isoCodeNum){
        return JeproLabCurrencyModel.exists(isoCode, isoCodeNum, 0);
    }


    /**
     * Check if a currency already exists.
     *
     * @param isoCode int for iso code number string for iso code
     * @return bool
     */
    public static boolean exists(String isoCode, int isoCodeNum, int labId){
        boolean currencyExists;
        if (isoCodeNum > 0){
            currencyExists = JeproLabCurrencyModel.getCurrencyIdByIsoCodeNum(isoCodeNum, labId) > 0;
        } else {
            currencyExists = JeproLabCurrencyModel.getCurrencyIdByIsoCode(isoCode, labId) > 0;
        }

        return currencyExists;
    }

    public static int getCurrencyIdByIsoCodeNum(int isoCodeNum){
        return getCurrencyIdByIsoCodeNum(isoCodeNum, 0);
    }

    /**
     * @param isoCodeNum
     * @param labId
     * @return int
     */
    public static int getCurrencyIdByIsoCodeNum(int isoCodeNum, int labId){
        String cacheKey = "jeprolab_currency_get_currency_id_by_iso_code_num" + isoCodeNum + "_" + labId;
        String where = "";
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if (staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT currency." + staticDataBaseObject.quoteName("currency_id") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ";
            if (JeproLabLaboratoryModel.isFeaturePublished() && labId > 0) {
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON(currency_lab.";
                query += staticDataBaseObject.quoteName("currency_id") + " = currency." + staticDataBaseObject.quoteName("currency_id");
                where += " AND currency." + staticDataBaseObject.quoteName("lab_id") + " = " + labId;

            }
            query += " WHERE currency." + staticDataBaseObject.quoteName("deleted") + " = 0  AND currency." + staticDataBaseObject.quoteName("iso_code_num");
            query += " = " + staticDataBaseObject.quote(String.valueOf(isoCodeNum)) + where;

            staticDataBaseObject.setQuery(query);
            int result = (int) staticDataBaseObject.loadValue("currency_id");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static int getCurrencyIdByIsoCode(String isoCode){
        return getCurrencyIdByIsoCode(isoCode, 0);
    }

    /**
     * @param isoCode
     * @param labId
     * @return int
     */
    public static int getCurrencyIdByIsoCode(String isoCode, int labId){
        String cacheKey = "jeprolab_currency_get_currency_id_by_iso_code_" + isoCode + "_" + labId;
        String where = "";
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if (staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT currency." + staticDataBaseObject.quoteName("currency_id") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ";
            if (JeproLabLaboratoryModel.isFeaturePublished() && labId > 0) {
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON(currency_lab.";
                query += staticDataBaseObject.quoteName("currency_id") + " = currency." + staticDataBaseObject.quoteName("currency_id");
                where += " AND currency." + staticDataBaseObject.quoteName("lab_id") + " = " + labId;

            }
            query += " WHERE currency." + staticDataBaseObject.quoteName("deleted") + " = 0  AND currency." + staticDataBaseObject.quoteName("iso_code");
            query += " = " + staticDataBaseObject.quote(isoCode) + where;

            staticDataBaseObject.setQuery(query);
            int result = (int)staticDataBaseObject.loadValue("currency_id");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }


    public static function getPaymentSpecialCurrencies(int moduleId){
        return getPaymentSpecialCurrencies(moduleId, 0);
    }

    public static function getPaymentSpecialCurrencies(int moduleId, int labId){
        if (labId < 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_module_currency") + " AS module_currency ";
        query += " WHERE module_id = " + moduleId + " AND lab_id = " + labId;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();
        //return Db::getInstance()->getRow($sql);
    }

    public static function getPaymentCurrencies(int moduleId){
        return getPaymentCurrencies(moduleId, 0);
    }

    public static function getPaymentCurrencies(int moduleId, int labId){
        if (labId <= 0){
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        String query = "SELECT currency.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_module_currency") + " AS module_currency LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ON currency." + staticDataBaseObject.quoteName("currency_id");
        query += " = module_currency." + staticDataBaseObject.quoteName("currency_id") + " WHERE currency." + staticDataBaseObject.quoteName("deleted");
        query += " = 0 AND module_currency." + staticDataBaseObject.quoteName("module_id") + " = " + moduleId + " AND currency.";
        query += staticDataBaseObject.quoteName("published") + " = 1 AND module_currency." + staticDataBaseObject.quoteName("lab_id") + " = ";
        query += labId + " ORDER BY currency." + staticDataBaseObject.quoteName("name") +  " ASC";
        return Db::getInstance()->executeS($sql);
    }

    public static function checkPaymentCurrencies(int moduleId){
        return checkPaymentCurrencies(moduleId, 0);
    }

    public static function checkPaymentCurrencies(int moduleId, int labId){
        if (moduleId <= 0) {
            return false;
        }

        if (labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query  = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_module_currency") + " AS module_currency ";
        query += " WHERE " + staticDataBaseObject.quoteName("module_id") + " = " + moduleId + " AND " + staticDataBaseObject.quoteName("lab_id");
        query += " = " + labId;
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
    }

    public static JeproLabCurrencyModel getCurrency(int currencyId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency") + " WHERE ";
        query += staticDataBaseObject.quoteName("deleted") + " = 0 AND " + staticDataBaseObject.quoteName("currency_id");
        query += " = " + currencyId;

        staticDataBaseObject.setQuery(query);
        ResultSet currencySet = staticDataBaseObject.loadObjectList();
        JeproLabCurrencyModel currency = new JeproLabCurrencyModel();
        if(currencySet != null){
            try{
                if(currencySet.next()){
                    currency.currency_id = currencySet.getInt("currency_id");

                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return currency;
    }

    /**
     * Refresh the currency exchange rate
     * The XML file define exchange rate for each from a default currency ($isoCodeSource).
     *
     * @param SimpleXMLElement $data XML content which contains all the exchange rates
     * @param isoCodeSource The default currency used in the XML file
     * @param defaultCurrency The default currency object
     */
    public function refreshCurrency($data, String isoCodeSource, JeproLabCurrencyModel defaultCurrency){
        // fetch the exchange rate of the default currency
        float exchangeRate = 1;
        float tmp = this.conversion_rate;
        if (!defaultCurrency.iso_code.equals(isoCodeSource)){
            for(JeproLabCurrencyModel currency : $data->currency){
                if(currency.iso_code.equals(defaultCurrency.iso_code)){
                    exchangeRate = round(currency['rate'], 6);
                    break;
                }
            }
        }

        if (defaultCurrency.iso_code.equals(this.iso_code)){
            this.conversion_rate = 1;
        } else {
            float rate;
            if (this.iso_code.equals(isoCodeSource)){
                rate = 1;
            } else {
                foreach ($data->currency as $obj) {
                    if ($this->iso_code == strval($obj['iso_code'])) {
                        rate = (float)$obj['rate'];
                        break;
                    }
                }
            }

            if(rate > 0) {
                this.conversion_rate = round(rate / exchangeRate, 6);
            }
        }

        if (tmp != this.conversion_rate){
            this.update();
        }
    }

    public static JeproLabCurrencyModel getDefaultCurrency(){
        int currencyId = JeproLabSettingModel.getIntValue("default_currency");
        if (currencyId <= 0) {
            return null;
        }

        return new JeproLabCurrencyModel(currencyId);
    }

    public static void refreshCurrencies() {
        // Parse
        $feed = JeproLabTools.simpleXmlLoadFile(JeproLabConfigurationSettings.JEPROLAB_CURRENCY_FEED_URL);
        if (feed == null){
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_CAN_NOT_PARSE_FEED_MESSAGE"));
        }

        // Default feed currency (EUR)
        $isoCodeSource = strval($feed -> source['iso_code']);
        JeproLabCurrencyModel defaultCurrency = JeproLabCurrencyModel.getDefaultCurrency();
        if (defaultCurrency == null || defaultCurrency.currency_id <= 0){
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_NO_DEFAULT_CURRENCY_LABEL"));
        }

        List<JeproLabCurrencyModel> localCurrencies = JeproLabCurrencyModel.getCurrencies (true, false, true);
        for(JeproLabCurrencyModel currency : localCurrencies) {
            /** @var Currency $currency */
            if (currency.currency_id != defaultCurrency.currency_id) {
                currency.refreshCurrency($feed -> list, isoCodeSource, defaultCurrency);
            }
        }
    }

    public String getCurrencySign(){
        return getCurrencySign(null);
    }

    /**
     * Return formatted sign
     *
     * @param side String left or right
     * @return string formatted sign
     */
    public String getCurrencySign(String side) {
        if (side == null){
            return this.sign;
        }

        String currencySign = this.sign;
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

    public static List<JeproLabCurrencyModel> getCurrencies(){
        return getCurrencies(false, true, false);
    }

    public static List<JeproLabCurrencyModel> getCurrencies(boolean object, boolean published){
        return getCurrencies(object, published, true);
    }

    /**
     * Return available currencies
     *
     * @return array Currencies
     */
    public static List<JeproLabCurrencyModel> getCurrencies(boolean object, boolean published, boolean groupBy){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ";
        query += JeproLabLaboratoryModel.addSqlAssociation("currency") + " WHERE " + staticDataBaseObject.quoteName("deleted");
        query += " = 0 " + (published ? " AND currency." + staticDataBaseObject.quoteName("published") + " = 1" : "");
        query += (groupBy ? " GROUP BY currency." + staticDataBaseObject.quoteName("currency_id") : "") + " ORDER BY ";
        query += staticDataBaseObject.quoteName("name" ) + " ASC ";

        staticDataBaseObject.setQuery(query);
        ResultSet currencySet = staticDataBaseObject.loadObjectList();
        List<JeproLabCurrencyModel> currencies = new ArrayList<>();
        try{
            int currencyId;
            JeproLabCurrencyModel currency;
            while(currencySet.next()) {
                currencyId = currencySet.getInt("currency_id");
                if(object) {
                    currency = JeproLabCurrencyModel.getCurrencyInstance(currencyId);
                }else{
                    currency = new JeproLabCurrencyModel();
                    currency.currency_id = currencyId;
                    currency.name = currencySet.getString("name");
                    currency.iso_code = currencySet.getString("iso_code");
                    currency.iso_code_num = currencySet.getInt("iso_code_num");
                    currency.sign = currencySet.getString("sign");
                    currency.blank = currencySet.getInt("blank") > 0;
                    currency.format = currencySet.getInt("format");
                    currency.decimals = currencySet.getInt("decimals");
                    currency.conversion_rate = currencySet.getFloat("conversion_rate");
                    currency.deleted = currencySet.getInt("deleted") > 0;
                    currency.published = currencySet.getInt("published") > 0;
                }
                currencies.add(currency);
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
        return currencies;
    }

    public static List<JeproLabCurrencyModel> getCurrenciesByLaboratoryId(){
        return getCurrenciesByLaboratoryId(0);
    }

    public static List<JeproLabCurrencyModel> getCurrenciesByLaboratoryId(int labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency") + " AS currency LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON (currency_lab.";
        query += staticDataBaseObject.quoteName("currency_id") + " = currency." + staticDataBaseObject.quoteName("currency_id");
        query += ") " + (labId > 0 ? " WHERE currency_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId : "");
        query += " ORDER BY " + staticDataBaseObject.quoteName("name") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet currencySet = staticDataBaseObject.loadObjectList();
        JeproLabCurrencyModel currency;
        List<JeproLabCurrencyModel> currencies = new ArrayList<>();
        try{
            while (currencySet.next()){
                currency = new JeproLabCurrencyModel();
                currency.currency_id = currencySet.getInt("currency_id");
                currency.name = currencySet.getString("name");
                currency.iso_code = currencySet.getString("iso_code");
                currency.iso_code_num = currencySet.getInt("iso_code_num");
                currency.sign = currencySet.getString("sign");
                currency.blank = currencySet.getInt("blank") > 0;
                currency.format = currencySet.getInt("format");
                currency.decimals = currencySet.getInt("decimals");
                currency.conversion_rate = currencySet.getFloat("conversion_rate");
                currency.deleted = currencySet.getInt("deleted") > 0;
                currency.published = currencySet.getInt("published") > 0;
                currencies.add(currency);
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
        return currencies;
    }

    public static JeproLabCurrencyModel getCurrentCurrency(){
        return JeproLabContext.getContext().currency;
    }

    public static JeproLabCurrencyModel getCurrencyInstance(int currencyId){
        if (!JeproLabCurrencyModel.currencies.containsKey(currencyId)) {
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

        if (!JeproLabCurrencyModel.activeCurrencies.containsKey(labId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT COUNT(DISTINCT currency.currency_id) AS ids FROM " + staticDataBaseObject.quoteName("#__jeprolab_currency");
            query += " AS currency LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON (";
            query += "currency_lab.currency_id = currency.currency_id AND currency_lab.lab_id = " + labId + ") WHERE currency.";
            query += staticDataBaseObject.quoteName("published") + " = 1";

            staticDataBaseObject.setQuery(query);
            int val = (int)staticDataBaseObject.loadValue("ids");
            JeproLabCurrencyModel.activeCurrencies.put(labId, val);
        }
        return JeproLabCurrencyModel.activeCurrencies.get(labId);
    }

    public static boolean isMultiCurrencyActivated(){
        return isMultiCurrencyActivated(0);
    }

    public static boolean isMultiCurrencyActivated(int labId){
        return (JeproLabCurrencyModel.countActiveCurrencies(labId) > 1);
    }

    /**
     * Replace letters of zip code format And check this format on the zip code
     * @param zipCode
     * @return (bool)
     */
    public function checkZipCode(String zipCode){
        $zip_regexp = '/^'.$this->zip_code_format.'$/ui';
        $zip_regexp = str_replace(' ', '( |)', $zipRegexp);
        $zip_regexp = str_replace('-', '(-|)', $zipRegexp);
        $zip_regexp = str_replace('N', '[0-9]', $zipRegexp);
        $zip_regexp = str_replace('L', '[a-zA-Z]', $zipRegexp);
        $zip_regexp = str_replace('C', $this->iso_code, $zipRegexp);

        return (bool)preg_match($zip_regexp, $zip_code);
    }

    public boolean save(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "";

        dataBaseObject.setQuery(query);
        boolean saved = dataBaseObject.query(true);
        this.currency_id = dataBaseObject.getGeneratedKey();
        return saved;
    }
}