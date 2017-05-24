package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabModel {
    public boolean get_lab_from_context = false;

    //protected static JeproLabDataBaseConnector dataBaseObject;

    /*public JeproLabModel(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
    }*/

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
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName(table + "_id") + " FROM " + JeproLabDataBaseConnector.quoteName("__jeprolab_" + table);

        query += (hasPublishColumn ? " WHERE " + JeproLabDataBaseConnector.quoteName("published") + " = 1" : "");

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return ((int)dataBaseObject.loadValue(query, table + "_id") > 0 );
    }

    public boolean updateField(String table, String field, String value, String condition, String type){
        String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_" + table) + " SET " + JeproLabDataBaseConnector.quoteName(field);
        switch(type){
            case "int" :
                query +=  " = " + Integer.parseInt(value);
                break;
        }
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query((query + condition), false);
        closeDataBaseConnection(dataBaseObject);
        return result;
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

    protected static void closeDataBaseConnection(JeproLabDataBaseConnector dbo){
        if(dbo != null) {
            try {
                JeproLabFactory.removeConnection(dbo);
            } catch (Exception ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                System.out.println("Unable to remove conection current connection ");
            }
        }
    }
}
