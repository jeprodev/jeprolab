package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.commons.collections4.map.HashedMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabSettingModel extends JeproLabModel {
    public int setting_id;

    /** @var string value **/
    public String value;

    /** @var string Object creation date Description **/
    public Date date_add;

    /** @var string Object last modification date Description **/
    public Date date_upd;

    /** @var array Setting cache **/
    protected static Map<String, Object> SETTINGS;

    /** @var array Vars types **/
    //protected static Array types = array();

    private static ResultSet getValue(String key){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT value FROM " + dataBaseObject.quoteName("#__jeprolab_setting") + " WHERE ";
        query += dataBaseObject.quoteName("name") + " = " + dataBaseObject.quote(key);

        dataBaseObject.setQuery(query);
        return dataBaseObject.loadObjectList();
    }

    public static void loadSettings() {
        SETTINGS = new HashedMap<>();
        if (dataBaseObject == null) {
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT setting." + dataBaseObject.quoteName("name") + ", setting." + dataBaseObject.quoteName("value");
        query += " FROM " + dataBaseObject.quoteName("#__jeprolab_setting") + " AS setting";

        dataBaseObject.setQuery(query);
        ResultSet settingsParams = dataBaseObject.loadObjectList();
        try {
            while (settingsParams.next()) {
                String key = settingsParams.getString("name");
                String value = settingsParams.getString("value");
                SETTINGS.put(key, value);
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                JeproLabDataBaseConnector.getInstance().closeConnexion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getIntValue(String key){
        if(SETTINGS == null || SETTINGS.isEmpty()){
            JeproLabSettingModel.loadSettings();
        }

        if(SETTINGS.containsKey(key)){
            return Integer.parseInt(SETTINGS.get(key).toString());
        }else{
            int value = 0;
            try{
                ResultSet valueSet = getValue(key);
                while(valueSet.next()){
                    value = valueSet.getInt("value");
                    SETTINGS.put(key, value);
                }
                return value;
            }catch (SQLException ignored){
                return 0;
            }

        }
    }

    public static float getFloatValue(String key){
        if(SETTINGS == null || SETTINGS.isEmpty()){
            JeproLabSettingModel.loadSettings();
        }

        if(SETTINGS.containsKey(key)){
            return Float.parseFloat(SETTINGS.get(key).toString());
        }else{
            float value = 0;
            try{
                ResultSet valueSet = getValue(key);
                while(valueSet.next()){
                    value = valueSet.getFloat("value");
                    SETTINGS.put(key, value);
                }
                return value;
            }catch (SQLException ignored){
                return 0;
            }

        }
    }

    public static String getStringValue(String key){
        if(SETTINGS == null || SETTINGS.isEmpty()){
            JeproLabSettingModel.loadSettings();
        }

        if(SETTINGS.containsKey(key)){
            return SETTINGS.get(key).toString();
        }else{
            try{
                ResultSet valueSet = getValue(key);
                if(valueSet.next()){
                    String value = valueSet.getString("value");
                    SETTINGS.put(key, value);
                    return value;
                }
            }catch (SQLException ignored){
                return "";
            }
            return "";
        }
    }

    public static boolean updateValue(String key, Object values){
        return true;
    }
}
