package com.jeprolab.models;


import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
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
            ignored.printStackTrace();
        }finally {
            try {
                JeproLabDataBaseConnector.getInstance().closeConnexion();
            }catch (Exception e) {
                e.printStackTrace();
            }
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

    /*
     * Update configuration key and value into database (automatically insert if key does not exist)
     *
     * Values are inserted/updated directly using SQL, because using (Configuration) ObjectModel
     * may not insert values correctly (for example, HTML is escaped, when it should not be).
     * @TODO Fix saving HTML values in Configuration model
     *
     * @param string $key Key
     * @param mixed $values $values is an array if the configuration is multilingual, a single string else.
     * @param bool $html Specify if html is authorized in value
     * @param int $id_shop_group
     * @param int $id_shop
     * @return bool Update result
     * /
    public static boolean updateValue(String key, values, $html = false, $id_shop_group = null, $id_shop = null){
        if (!Validate::isConfigName($key)) {
        die(sprintf(Tools::displayError('[%s] is not a valid configuration key'), Tools::htmlentitiesUTF8($key)));
    }

        if ($id_shop === null || !Shop::isFeatureActive()) {
        $id_shop = Shop::getContextShopID(true);
    }
        if ($id_shop_group === null || !Shop::isFeatureActive()) {
        $id_shop_group = Shop::getContextShopGroupID(true);
    }

        if (!is_array($values)) {
            $values = array($values);
        }

        if ($html) {
            foreach ($values as &$value) {
                $value = Tools::purifyHTML($value);
            }
            unset($value);
        }

        $result = true;
        foreach ($values as $lang => $value) {
        $stored_value = Configuration::get($key, $lang, $id_shop_group, $id_shop);
        // if there isn't a $stored_value, we must insert $value
        if ((!is_numeric($value) && $value === $stored_value) || (is_numeric($value) && $value == $stored_value && Configuration::hasKey($key, $lang))) {
            continue;
        }

        // If key already exists, update value
        if (Configuration::hasKey($key, $lang, $id_shop_group, $id_shop)) {
            if (!$lang) {
                // Update config not linked to lang
                $result &= Db::getInstance()->update(self::$definition['table'], array(
                        'value' => pSQL($value, $html),
                        'date_upd' => date('Y-m-d H:i:s'),
                ), '`name` = \''.pSQL($key).'\''.Configuration::sqlRestriction($id_shop_group, $id_shop), 1, true);
            } else {
                // Update multi lang
                $sql = 'UPDATE `'._DB_PREFIX_.bqSQL(self::$definition['table']).'_lang` cl
                SET cl.value = \''.pSQL($value, $html).'\',
                cl.date_upd = NOW()
                WHERE cl.id_lang = '.(int)$lang.'
                AND cl.`'.bqSQL(self::$definition['primary']).'` = (
                        SELECT c.`'.bqSQL(self::$definition['primary']).'`
                FROM `'._DB_PREFIX_.bqSQL(self::$definition['table']).'` c
                WHERE c.name = \''.pSQL($key).'\''
                        .Configuration::sqlRestriction($id_shop_group, $id_shop)
                .')';
                $result &= Db::getInstance()->execute($sql);
            }
        }
        // If key does not exists, create it
        else {
            if (!$configID = Configuration::getIdByName($key, $id_shop_group, $id_shop)) {
                $now = date('Y-m-d H:i:s');
                $data = array(
                        'id_shop_group' => $id_shop_group ? (int)$id_shop_group : null,
                        'id_shop'       => $id_shop ? (int)$id_shop : null,
                        'name'          => pSQL($key),
                        'value'         => $lang ? null : pSQL($value, $html),
                        'date_add'      => $now,
                        'date_upd'      => $now,
                );
                $result &= Db::getInstance()->insert(self::$definition['table'], $data, true);
                $configID = Db::getInstance()->Insert_ID();
            }

            if ($lang) {
                $result &= Db::getInstance()->insert(self::$definition['table'].'_lang', array(
                        self::$definition['primary'] => $configID,
                        'id_lang' => (int)$lang,
                        'value' => pSQL($value, $html),
                        'date_upd' => date('Y-m-d H:i:s'),
                ));
            }
        }
    }

        Configuration::set($key, $values, $id_shop_group, $id_shop);

        return $result;
    } */

    public static boolean updateValue(String key, Object values){
        return true;
    }



}