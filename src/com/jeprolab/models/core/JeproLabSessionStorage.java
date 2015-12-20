package com.jeprolab.models.core;

/**
 *
 * Created by jeprodev on 19/12/2015.
 */
public abstract class JeproLabSessionStorage {
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
