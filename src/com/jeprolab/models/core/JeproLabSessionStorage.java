package com.jeprolab.models.core;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabSessionStorage {
    /**
     *
     */
    protected static JeproLabSessionStorage instance;

    public void register(){

    }

    public static JeproLabSessionStorage getInstance(String storeN, JeproLabSessionOption options){
        return instance;
    }
}