package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
                        JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception ignored) {
                            JeproLabTools.logExceptionMessage(Level.WARN, ignored);
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
}
