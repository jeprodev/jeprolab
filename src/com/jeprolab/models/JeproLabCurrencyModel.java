package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

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
public class JeproLabCurrencyModel extends JeproLabModel {
    public int currency_id;

    /** @var string Name */
    public String name;

    public int laboratory_id;

    /** @var string Iso code */
    public String iso_code;

    /** @var string Iso code numeric */
    public String iso_code_num;

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
                            this.iso_code_num = resultSet.getString("iso_code_num");
                            this.iso_code = resultSet.getString("iso_code");
                            this.sign = resultSet.getString("sign");
                            this.blank = resultSet.getInt("blank") > 0;
                            this.format = resultSet.getInt("format");
                            this.name = resultSet.getString("name");
                            this.decimals = resultSet.getInt("decimals");
                            this.conversion_rate = resultSet.getFloat("conversion_rate");
                            this.deleted = resultSet.getInt("deleted") > 0;
                            this.published = resultSet.getInt("published") > 0;

                            JeproLabCache.getInstance().store(cacheKey, this);
                        }
                    }catch(SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        try {
                            JeproLabFactory.removeConnection(dataBaseObject);
                        }catch (Exception ignored) {
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                        }
                    }
                }
            }else{
                JeproLabCurrencyModel currency = (JeproLabCurrencyModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.currency_id = currency.currency_id;
                this.iso_code_num = currency.iso_code_num;
                this.iso_code = currency.iso_code;
                this.sign = currency.sign;
                this.blank = currency.blank;
                this.format = currency.format;
                this.name = currency.name;
                this.decimals = currency.decimals;
                this.conversion_rate = currency.conversion_rate;
                this.deleted = currency.deleted;
                this.published = currency.published;
            }
        }
        this.prefix =  this.format % 2 != 0 ? this.sign + " " : "";
        this.suffix = this.format % 2 == 0 ? " " + this.sign : "";
        if (this.conversion_rate <= 0) {
            this.conversion_rate = 1;
        }
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
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
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
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_currency") + " AS currency ";
        query += JeproLabLaboratoryModel.addSqlAssociation("currency") + " WHERE " + dataBaseObject.quoteName("deleted");
        query += " = 0 " + (published ? " AND currency." + dataBaseObject.quoteName("published") + " = 1" : "");
        query += (groupBy ? " GROUP BY currency." + dataBaseObject.quoteName("currency_id") : "") + " ORDER BY ";
        query += dataBaseObject.quoteName("name") + " ASC ";

        dataBaseObject.setQuery(query);
        ResultSet currencySet = dataBaseObject.loadObjectList();
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
                    currency.iso_code_num = currencySet.getString("iso_code_num");
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
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }finally {
            try {
                JeproLabFactory.removeConnection(dataBaseObject);
            }catch (Exception ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
            }
        }
        return currencies;
    }

    public static List<JeproLabCurrencyModel> getCurrenciesByLaboratoryId(){
        return getCurrenciesByLaboratoryId(0);
    }

    public static List<JeproLabCurrencyModel> getCurrenciesByLaboratoryId(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_currency") + " AS currency LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON (currency_lab.";
        query += dataBaseObject.quoteName("currency_id") + " = currency." + dataBaseObject.quoteName("currency_id");
        query += ") " + (labId > 0 ? " WHERE currency_lab." + dataBaseObject.quoteName("lab_id") + " = " + labId : "");
        query += " ORDER BY " + dataBaseObject.quoteName("name") + " ASC";

        dataBaseObject.setQuery(query);
        ResultSet currencySet = dataBaseObject.loadObjectList();
        JeproLabCurrencyModel currency;
        List<JeproLabCurrencyModel> currencies = new ArrayList<>();
        try{
            while (currencySet.next()){
                currency = new JeproLabCurrencyModel();
                currency.currency_id = currencySet.getInt("currency_id");
                currency.name = currencySet.getString("name");
                currency.iso_code = currencySet.getString("iso_code");
                currency.iso_code_num = currencySet.getString("iso_code_num");
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
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }finally {
            try {
                JeproLabFactory.removeConnection(dataBaseObject);
            }catch (Exception ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT COUNT(DISTINCT currency.currency_id) AS ids FROM " + dataBaseObject.quoteName("#__jeprolab_currency");
            query += " AS currency LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_currency_lab") + " AS currency_lab ON (";
            query += "currency_lab.currency_id = currency.currency_id AND currency_lab.lab_id = " + labId + ") WHERE currency.";
            query += dataBaseObject.quoteName("published") + " = 1";

            dataBaseObject.setQuery(query);
            int val = (int)dataBaseObject.loadValue("ids");
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

    public boolean save(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_currency") + "(" + dataBaseObject.quoteName("name") + ", ";
        query += dataBaseObject.quoteName("iso_code") + ", " + dataBaseObject.quoteName("iso_code_num") + ", " + dataBaseObject.quoteName("sign");
        query += ", " + dataBaseObject.quoteName("blank") + ", " + dataBaseObject.quoteName("format") + ", " + dataBaseObject.quoteName("decimals") ;
        query += ", " + dataBaseObject.quoteName("conversion_rate") + ", " + dataBaseObject.quoteName("deleted") + ", " + dataBaseObject.quoteName("published");
        query += ") VALUES(" + dataBaseObject.quote(this.name) + ", " + dataBaseObject.quote(this.iso_code) + ", " + dataBaseObject.quote(this.iso_code_num);
        query += ", " + dataBaseObject.quote(this.sign) + ", " + (this.blank ? 1 : 0) + ", " + this.format + ", " + this.decimals + ", " ;
        query += this.conversion_rate + ", " + (this.deleted ? 1 : 0) + ", " + (this.published ? 1 : 0) + ")";

        dataBaseObject.setQuery(query);
        boolean result = dataBaseObject.query(true);
        this.currency_id = dataBaseObject.getGeneratedKey();
        return  result;
    }

    public boolean update(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_currency") + " SET " + dataBaseObject.quoteName("name") + " = ";
        query += dataBaseObject.quote(this.name) + ", " + dataBaseObject.quoteName("iso_code") + " = " + dataBaseObject.quote(this.iso_code);
        query += ", " + dataBaseObject.quoteName("iso_code_num") + " = " +  dataBaseObject.quote(this.iso_code_num) + ", " + dataBaseObject.quoteName("sign");
        query += " = " + dataBaseObject.quote(this.sign) + ", " + dataBaseObject.quoteName("blank") + " = " + (this.blank ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("format") + " = " + this.format + ", " + dataBaseObject.quoteName("decimals") + " = " + this.decimals;
        query += ", " + dataBaseObject.quoteName("conversion_rate") + " = " + this.conversion_rate + ", " + dataBaseObject.quoteName("deleted") ;
        query += " = " + (this.deleted ? 1 : 0) + ", " + dataBaseObject.quoteName("published")+ " = " + (this.published ? 1 : 0);
        query += " WHERE " + dataBaseObject.quoteName("currency_id") + " = "  + this.currency_id;

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }
}
