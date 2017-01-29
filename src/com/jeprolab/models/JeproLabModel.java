package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabModel {
    public boolean get_lab_from_context = false;

    protected static Pagination pagination;

    protected static JeproLabDataBaseConnector dataBaseObject;

    public JeproLabModel(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
    }

    public static Pagination getPagination(){
        return pagination;
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
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName(table + "_id") + " FROM " + dataBaseObject.quoteName("__jeprolab_" + table);

        query += (hasPublishColumn ? " WHERE " + dataBaseObject.quoteName("published") + " = 1" : "");
        dataBaseObject.setQuery(query);


        return ((int)dataBaseObject.loadValue(table + "_id") > 0 );
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
