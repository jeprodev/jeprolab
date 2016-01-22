package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
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

    /*public function saveZone(){
        $db = JFactory::getDBO();

        $input_data = '';

        $query = "INSERT INTO " . $db->quoteName("#__jeprolab_zone') . "(";
    }

    public List getZoneList(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        JeproLabContext context = JeproLabContext.getContext();
        $app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');

        if(!$context){ $context = JeprolabContext::getContext(); }

        $limit = $app->getUserStateFromRequest('global.list.limit', 'limit', $app->getCfg('list_limit'), 'int');
        $limit_start = $app->getUserStateFromRequest($option. $view. '.limit_start', 'limit_start', 0, 'int');
        $lang_id = $app->getUserStateFromRequest($option. $view. '.lang_id', 'lang_id', $context->language->lang_id, 'int');
        $lab_id = $app->getUserStateFromRequest($option. $view. '.lab_id', 'lab_id', $context->lab->lab_id, 'int');
        $lab_group_id = $app->getUserStateFromRequest($option. $view. '.lab_group_id', 'lab_group_id', $context->lab->lab_group_id, 'int');
        $allow_delivery = $app->getUserStateFromRequest($option. $view. '.allow_delivery', 'allow_delivery', 0, 'int');

        $use_limit = true;
        if ($limit === false)
            $use_limit = false;

        String query;
        do{
            query = "SELECT SQL_CALC_FOUND_ROWS zone." +  dataBaseObject.quoteName("zone_id") + ", zone." +  dataBaseObject.quoteName("name");
            query += " AS zone_name, zone." +  dataBaseObject.quoteName("allow_delivery") + " FROM " + dataBaseObject.quoteName("#__jeprolab_zone");
            query += (allow_delivery ? " WHERE zone.allow_delivery = 1 " : "") + " AS zone ORDER BY " + dataBaseObject.quoteName("name") + " ASC ";

            dataBaseObject.setQuery(query);
            $total = count($db->loadObjectList());

            $query .= (($use_limit === true) ? " LIMIT " .(int)$limit_start . ", " .(int)$limit : "");

            $db->setQuery($query);
            $zones = $db->loadObjectList();

            if($use_limit == true){
                $limit_start = (int)$limit_start -(int)$limit;
                if($limit_start < 0){ break; }
            }else{ break; }
        }while(empty($zones));

        $this->pagination = new JPagination($total, $limit_start, $limit);
        return $zones;
    }

    public function getPagination(){
        return $this->pagination;
    }

    public static List<JeproLabZoneModel> getZones(){
        return getZones(false);
    }

    /**
     * Get all available geographical zones
     *
     * @param allow_delivery boolean
     * @return type
     * /
    public static List getZones(boolean allow_delivery){
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

            }
            JeproLabCache.getInstance().store(cacheKey, zones);
        }
        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Get a zone ID from its default language name
     *
     * @param name
     * @return zone_id
     * /
    public static int getIdByName(String name){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("zone_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_zone");
        query += " WHERE " + staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(name);

        staticDataBaseObject.setQuery(query);
        return $db->loadResult();
    }

    /**
     * Delete a zone
     *
     * @return boolean Deletion result
     * /
    public boolean delete() {
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "";
        if (parent::delete()) {
            // Delete regarding delivery preferences
            query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_carrier_zone") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;

            dataBaseObject.setQuery(query);
            $result = $db->query();
            query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_delivery") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
            dataBaseObject.setQuery(query);
            $result &= $db->query();

            // Update Country & state zone with 0
            query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_country") + " SET "  + dataBaseObject.quoteName("zone_id") + " = 0 WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
            dataBaseObject.setQuery(query);
            ResultSet values = dataBaseObject.query();
            result &= $db->query();
            query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_state") + " SET "  + dataBaseObject.quoteName("zone_id") + " = 0 WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
            dataBaseObject.setQuery(query);
            $result &= $db->query();

            return $result;
        }

        return false;
    } */
}