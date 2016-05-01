package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
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

    public boolean updateField(String table, String field, String value, String condition, String type){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_" + table) + " SET " + dataBaseObject.quoteName(field);
        switch(type){
            case "int" :
                query +=  " = " + Integer.parseInt(value);
                break;
        }

        dataBaseObject.setQuery(query + condition);
        return dataBaseObject.query(false);
    }

    public void clearCache(String table, int id){
        clearCache(table, id, false);
    }

    /**
     * Clears cache entries that have this object's ID.
     *
     * @param all If true, clears cache for all objects
     */
    public void clearCache(String table, int id, boolean all){
        if (all) {
            JeproLabCache.getInstance().remove("jeprolab_" + table + "_model_*");
        } else if (id > 0) {
            JeproLabCache.getInstance().remove("jeprolab_" + table + "_model_" + id + "_*");
        }
    }
}
