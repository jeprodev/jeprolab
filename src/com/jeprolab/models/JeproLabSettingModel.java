package com.jeprolab.models;

import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Level;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
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

    private static ResultSet getValue(String key){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("value") + " FROM " + dataBaseObject.quoteName("#__jeprolab_setting");
        query += " WHERE " + dataBaseObject.quoteName("name") + " = " + dataBaseObject.quote(key);

        return dataBaseObject.loadObjectList(query);
    }

    public static void loadSettings() {
        SETTINGS = new HashedMap<>();
        if (dataBaseObject == null) {
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT setting." + dataBaseObject.quoteName("name") + ", setting." + dataBaseObject.quoteName("value");
        query += " FROM " + dataBaseObject.quoteName("#__jeprolab_setting") + " AS setting";

        ResultSet settingsParams = dataBaseObject.loadObjectList(query);
        if (settingsParams != null) {
            try {
                while (settingsParams.next()) {
                    String key = settingsParams.getString("name");
                    String value = settingsParams.getString("value");
                    SETTINGS.put(key, value);
                }
            } catch (SQLException ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            } finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                } catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
    }

    public static int getIntValue(String key){
        if(SETTINGS == null || SETTINGS.isEmpty()){
            JeproLabSettingModel.loadSettings();
        }

        if(SETTINGS.containsKey(key)){
            return Integer.parseInt(SETTINGS.get(key).toString());
        }else {
            int value = 0;
            ResultSet valueSet = getValue(key);
            if (valueSet != null) {
                try {
                    if (valueSet.next()) {
                        String valueData = valueSet.getString("value");
                        System.out.println(key + " value : " + valueData + " lenght : " + valueData.length());
                        value = Integer.parseInt(valueData);
                        SETTINGS.put(key, value);
                    }
                    return value;
                } catch (SQLException ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    return 0;
                }
            }
            return 0;
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

    public static boolean updateValue(String key, Object values){
        return true;
    }


}
