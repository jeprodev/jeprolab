package com.jeprolab.models;


import com.jeprolab.models.core.JeproLabFactory;
import org.apache.commons.collections4.map.HashedMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class JeproLabSettingModel extends JeproLabModel{
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
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT value FROM " + staticDataBaseObject.quoteName("#__jeprolab_setting") + " WHERE ";
        query += staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(key);

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    public static void loadSettings() {
        SETTINGS = new HashedMap<>();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT setting." + staticDataBaseObject.quoteName("name") + ", setting." + staticDataBaseObject.quoteName("value");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeproshop_setting") + " AS setting";

        staticDataBaseObject.setQuery(query);
        ResultSet settingsParams = staticDataBaseObject.loadObject();
        try{
            while(settingsParams.next()){
                String key = settingsParams.getString("name");
                String value = settingsParams.getString("value");
                SETTINGS.put(key, value);
            }
        }catch (SQLException ignored){

        }
        /*if(!$settings = $db->loadObjectList()){ return; }

        foreach($settings as $setting){
            if(!isset(self::$_SETTINGS)){
                self::$_SETTINGS = array('global' => array(), 'group' => array(), 'shop' => array());
            }

            if(isset($setting->shop_id)){
                self::$_SETTINGS['shop'][$setting->shop_id][$setting->name] = $setting->value;
            }elseif(isset($setting->shop_group_id)){
                self::$_SETTINGS['group'][$setting->shop_group_id][$setting->name] = $setting->value;
            }else{
                self::$_SETTINGS['global'][$setting->name] = $setting->value ;
            }
        }*/
    }

    public static int getIntValue(String key){
        if(SETTINGS == null || SETTINGS.isEmpty()){
            JeproLabSettingModel.loadSettings();
        }

        if(SETTINGS.containsKey(key)){
            return Integer.parseInt(SETTINGS.get(key).toString());
        }else{
            int value = 20;
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
}