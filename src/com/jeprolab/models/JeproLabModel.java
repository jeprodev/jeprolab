package com.jeprolab.models;


import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

public class JeproLabModel {
    protected JeproLabDataBaseConnector dataBaseObject;
    protected static JeproLabDataBaseConnector staticDataBaseObject;

    protected String image_format = "jpg";

    public JeproLabModel(){
        if(dataBaseObject == null || staticDataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
            staticDataBaseObject = dataBaseObject;
        }
    }


}