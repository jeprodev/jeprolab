package com.jeprolab.models.core;

import com.jeprolab.JeproLab;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabRequest {
    private static volatile Map<String, String> request, post;
    private static JeproLabRequest instance = null;

    private JeproLabRequest(){
        request = new HashMap<>();
        post = new HashMap<>();
        instance = this;
    }

    public void setRequest(String value){
        synchronized(this) {
            if (request == null) {
                request = new HashMap<>();
            } else {
                request.clear();
            }

            String[] queries;
            if (value.contains("&amp;")) {
                queries = value.split("&amp;");
            } else if (value.contains("&")) {
                queries = value.split("&");
            } else {
                queries = new String[1];
                queries[0] = value;
            }

            if (queries.length > 0) {
                for (String query : queries) {
                    String[] requestQuery = query.split("=");
                    request.put(requestQuery[0], requestQuery[1]);
                }
            }
        }
    }

    public void setPost(String value){
        synchronized(this) {
            if (post == null) {
                post = new HashMap<>();
            } else {
                post.clear();
            }

            String[] queries;
            if (value.contains("&amp;")) {
                queries = value.split("&amp;");
            } else if (value.contains("&")) {
                queries = value.split("&");
            } else {
                queries = new String[1];
                queries[0] = value;
            }

            if (queries.length > 0) {
                for (String query : queries){
                    String[] requestQuery = query.split("=");
                    if(requestQuery.length < 2 ){
                        post.put(requestQuery[0],  JeproLab.getBundle().getString("JEPROLAB_EMPTY_FIELD_HAS_TO_BE_COMPLETED_LABEL"));
                    }else {
                        post.put(requestQuery[0], requestQuery[1]);
                    }
                }
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

    public Map<String, String> getRequest(){
        if(request == null){
            request = new HashMap<>();
        }
        return request;
    }

    public Map<String, String> getPost(){
        if(post == null){
            post = new HashMap<>();
        }
        return post;
    }

    public static JeproLabRequest getInstance(){
        if(instance == null){
            new JeproLabRequest();
        }
        return instance;
    }
}