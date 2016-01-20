package com.jeprolab.models;


import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

public class JeproLabModel {
    protected JeproLabDataBaseConnector dataBaseObject;
    protected static JeproLabDataBaseConnector staticDataBaseObject;

    public JeproLabModel(){
        if(dataBaseObject == null || staticDataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
            staticDataBaseObject = dataBaseObject;
        }
    }
}