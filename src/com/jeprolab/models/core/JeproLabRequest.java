package com.jeprolab.models.core;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabRequest {
    private String request;

    public void setRequest(String value){
        request = value;
    }

    public String getValue(String key){
        return "";
    }

    public String getValue(String key, String defaultValue){
        return defaultValue;
    }

    public int getIntValue(String key){
        return 1;
    }

    public int getIntValue(String key, int defaultValue){
        return defaultValue;
    }
}