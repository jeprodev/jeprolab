package com.jeprolab.models;


import java.lang.reflect.Array;
import java.util.Date;

public class JeproLabSettingModel {
    public int setting_id;

    /** @var string value **/
    public String value;

    /** @var string Object creation date Description **/
    public Date date_add;

    /** @var string Object last modification date Description **/
    public Date date_upd;

    /** @var array Setting cache **/
    protected static Array $_SETTINGS;

    /** @var array Vars types **/
    //protected static Array types = array();

    public static String getValue(String key){
        return key;
    }

    public static void loadSettings() {

    }

    public static int getIntValue(String key){
        return 2;
    }
}