package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeproLabZoneModel extends JeproLabModel{
    public int zone_id = 0;

    public String name;

    public boolean allow_delivery;

    private Pagination pagination = null;

    public JeproLabZoneModel(){
        this(0);
    }

    public JeproLabZoneModel(int zoneId){
        if(zoneId > 0){
            String cacheKey =  "jeprolab_zone_model_" + zoneId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null) {
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeproshop_zone") + " AS zone WHERE ";
                query += dataBaseObject.quoteName("zone_id") + " = " + zoneId;
                dataBaseObject.setQuery(query);
                ResultSet zoneData = dataBaseObject.loadObject();
                if(zoneData != null){
                    try{
                        if(zoneData.next()){
                            this.zone_id = zoneData.getInt("zone_id");
                            this.name = zoneData.getString("name");
                            this.allow_delivery = zoneData.getInt("allow_delivery") > 0;
                        }
                    }catch (SQLException ignored){
                        ignored.printStackTrace();
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                JeproLabCache.getInstance().store(cacheKey, this);
            }else{
                JeproLabZoneModel zone = (JeproLabZoneModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.zone_id = zone.zone_id;
                this.name = zone.name;
                this.allow_delivery = zone.allow_delivery;
            }
        }
    }

    public Pagination getPagination(){
        return this.pagination;
    }

    public static List<JeproLabZoneModel> getZones(){
        return getZones(false);
    }

    /**
     * Get all available geographical zones
     *
     * @param allow_delivery boolean
     * @return type
     */
    public static List<JeproLabZoneModel> getZones(boolean allow_delivery){
        String cacheKey = "jeprolab_zone_model_get_zones_" +  (allow_delivery ? 1 : 0);
        List<JeproLabZoneModel> zones = new ArrayList<>();
        if(!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_zone") + (allow_delivery ? " WHERE allow_delivery = 1 " : "");
            query += " ORDER BY " + staticDataBaseObject.quoteName("name") + " ASC ";

            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadObject();

            try{
                JeproLabZoneModel zone;
                while(result.next()){
                    zone = new JeproLabZoneModel();
                    zone.zone_id = result.getInt("zone_id");
                    zone.name = result.getString("name");
                    zone.allow_delivery = result.getInt("allow_delivery") > 0;
                    zones.add(zone);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JeproLabCache.getInstance().store(cacheKey, zones);
        }
        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Get a zone ID from its default language name
     *
     * @param name zone name
     * @return zone_id
     */
    public static int getIdByName(String name){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("zone_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_zone");
        query += " WHERE " + staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(name);

        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("zone_id");
    }

    /**
     * Delete a zone
     *
     * @return boolean Deletion result
     */
    public boolean delete() {
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_zone") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
        dataBaseObject.setQuery(query);
        if (dataBaseObject.query(false)) {
            // Delete regarding delivery preferences
            query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_carrier_zone") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;

            dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.query(false);

            query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_delivery") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
            dataBaseObject.setQuery(query);
            //todo result &= $db->query();

            // Update Country & state zone with 0
            query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_country") + " SET "  + dataBaseObject.quoteName("zone_id") + " = 0 WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);

            query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_state") + " SET "  + dataBaseObject.quoteName("zone_id") + " = 0 WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);

            return result;
        }

        return false;
    }
}