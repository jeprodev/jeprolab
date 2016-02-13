package com.jeprolab.models.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabRequest {
    private Map<String, String> request;

    public void setRequest(String value){
        //synchronized
        if(request == null){
            request = new HashMap<>();
        }else{
            request.clear();
        }

        String[] queries = new String[0];
        if(value.contains("&amp;")){
            queries = value.split("&amp;");
        } else if(value.contains("&")){
            queries = value.split("&");
        }
        if(queries.length > 0) {
            for (String query : queries) {
                String[] requestQuery = query.split("=");
                request.put(requestQuery[0], requestQuery[1]);
            }
        }
    }

    public String getValue(String key){
        return getValue(key, "");
    }

    public String getValue(String key, String defaultValue){
        if(request != null && request.containsKey(key)){
            return request.get(key);
        }
        return defaultValue;
    }

    public int getIntValue(String key){
        return getIntValue(key, 0);
    }

    public int getIntValue(String key, int defaultValue){
        if(request != null && request.containsKey(key)){
            return Integer.parseInt(request.get(key));
        }
        return defaultValue;
    }
}