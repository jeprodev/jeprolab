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

    public static boolean isCurrentlyUsed(String table){
        return isCurrentlyUsed(table, false);
    }


    /**
     * Checks if an object type exists in the database.
     *
     * @param table            Name of table linked to entity
     * @param hasPublishColumn True if the table has an active column
     *
     * @return bool
     */
    public static boolean isCurrentlyUsed(String table, boolean hasPublishColumn){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName(table + "_id") + " FROM " + staticDataBaseObject.quoteName("__jeprolab_" + table);

        query += (hasPublishColumn ? " WHERE " + staticDataBaseObject.quoteName("published") + " = 1" : "");
        staticDataBaseObject.setQuery(query);


        return ((int)staticDataBaseObject.loadValue(table + "_id") > 0 );
    }

}