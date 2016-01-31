package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeproLabStateModel extends JeproLabModel{
    public int state_id = 0;

    public int country_id = 0;

    public int zone_id = 0;

    public char[] isoCode = new char[2];

    public String name;

    public boolean published = false;

    private Pagination pagination = null;

    public JeproLabStateModel(){
        this(0);
    }

    public JeproLabStateModel(int stateId){

    }

    /**
     * Get a state name with its ID
     *
     * @param stateId Country ID
     * @return string State name
     */
    public static String getNameById(int stateId){
        if (stateId <= 0) {
            return "";
        }
        String cacheKey = "jeprolab_state_get_name_by_id_" + stateId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("name") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_state");
            query += " WHERE " + staticDataBaseObject.quoteName("state_id") + " = " + stateId;

            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadResult();
            JeproLabCache.getInstance().store(cacheKey, $result);
        }
        return JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public boolean isMultiLab(){
        return (JeproLabLaboratoryModel.isTableAssociated("state") || !JeproLabStateModel.multiLangLab);
    }

    public function getStateList(){
        JeproLabContext context = JeproLabContext.getContext();
        jimport('joomla.html.pagination');
        $db = JFactory::getDBO();
        $app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');

        //if(!$context){ $context = JeproLabContext::getContext(); }
    }

    public static List<JeproLabStateModel> getStates(){
        return getStates(0, false);
    }

    public static List<JeproLabStateModel> getStates(int langId){
        return getStates(langId, false);
    }

    public static List<JeproLabStateModel> getStates(int langId, boolean published) {
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(langId <= 0){
            langId = JeproLabContext.getContext().language.language_id;
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("state_id") + ",  " + staticDataBaseObject.quoteName("country_id");
        query += ", " + staticDataBaseObject.quoteName("zone_id") + ", " + staticDataBaseObject.quoteName("iso_code") + ", " ;
        query += staticDataBaseObject.quoteName("name") + ", " + staticDataBaseObject.quote("published") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_state") + " WHERE " + staticDataBaseObject.quoteName("lang_id") + " = ";
        query += langId + (published ? " AND " + staticDataBaseObject.quoteName("published") + " = 1" : "");
        query += " ORDER BY " + staticDataBaseObject.quoteName("name") + " ASC ";

        staticDataBaseObject.setQuery(query);
        ResultSet results = staticDataBaseObject.loadObject();
        List<JeproLabStateModel> states = new ArrayList<>();
        try {
            JeproLabStateModel state;
            while (results.next()){
                state = new JeproLabStateModel();
                state.state_id = results.getInt("state_id");
                state.country_id = results.getInt("country_id");
                state.zone_id = results.getInt("zone_id");
                state.isoCode = results.getString("iso_code").toCharArray();
                state.name = results.getString("name");
                state.published = results.getInt("published") > 0;
                states.add(state);
            }
        }catch (SQLException ignored){

        }
        return states;
    }

    /**
     * Get a state id with its name
     *
     * @param stateName State name
     * @return integer state id
     */
    public static int getStateIdByName(String stateName){
        if (stateName == null || stateName.equals("")) {
            return 0;
        }
        String cacheKey = "jeprolab_State_getNameById_" + JeproLabTools.secureData(stateName);
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("state_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_state");
            query += " WHERE " + staticDataBaseObject.quoteName("name") + " LIKE " + staticDataBaseObject.secureData(stateName);

            staticDataBaseObject.setQuery(query);
            int result = (int)staticDataBaseObject.loadValue();

            JeproLabCache.getInstance().store(cacheKey, result);
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static int getStateIdByIsoCode(String isoCode){
        return getStateIdByIsoCode(isoCode, 0);
    }

    /**
     * Get a state id with its iso code
     *
     * @param isoCode Iso code
     * @param countryId
     * @return integer state id
     */
    public static int getStateIdByIsoCode(String isoCode, int countryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("state_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_state");
        query += " WHERE " + staticDataBaseObject.quoteName("iso_code") + " = " + staticDataBaseObject.secureData(isoCode);
        query += (countryId > 0) ? " AND " + staticDataBaseObject.quoteName("country_id") + " = " + countryId : "";

        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue();
    }

    /**
     * Delete a state only if is not in use
     *
     * @return boolean
     */
    public boolean delete(){
        if (!this.isUsed()){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            // Database deletion
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_state") + " WHERE " + dataBaseObject.quoteName("state_id") + " = " + this.state_id;
            dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.query();
            if (!result)
                return false;

            // Database deletion for multilingual fields related to the object
            if (!JeproLabStateModel.multiLang) {
                Db::getInstance () -> delete(bqSQL($this -> def['table']). '_lang', '`'.$this->def['primary']. '` = '.
                (int) $this -> id);
            }
            return result;
        }else {
            return false;
        }
    }

    /**
     * Check if a state is used
     *
     * @return boolean
     */
    public boolean isUsed(){
        return (this.countUsed() > 0);
    }

    /**
     * Returns the number of utilisation of a state
     *
     * @return integer count for this state
     */
    public int countUsed() {
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT COUNT(*) FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " WHERE ";
        query += dataBaseObject.quoteName("state_id") + " = " + this.state_id;
        int result = 0;

        dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObject();
        try{
            while(resultSet.next()){ result += 1; }
        }catch (SQLException ignored){}
        return result;
    }

    public static List<JeproLabStateModel> getStatesByCountryId(int countryId){
        if (countryId <= 0) {
            JeproLabTools.displayError();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_state") + " AS state WHERE"
        return Db::getInstance()->executeS('
            SELECT *
                    FROM `'._DB_PREFIX_.'state` s
        WHERE s.`id_country` = '.(int)$id_country
        );
    }

    public static boolean hasCounties( int stateId){
        return JeproLabCountyModel.getCounties(stateId).size() > 0;
    }

    public static int getZoneId(int stateId){
        if (stateId <= 0) {
            die(Tools::displayError ());
        }
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT `id_zone`
            FROM `'._DB_PREFIX_.'state`
        WHERE `id_state` = '.(int)$id_state
        );
    }

    /**
     * @param statesIds
     * @param zoneId
     * @return bool
     */
    public boolean    affectZoneToSelection(int statesIds[], int zoneId){
        // cast every array values to int (security)
        $ids_states = array_map('intval', $ids_states);
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_state") + " SET " + dataBaseObject.quoteName("state_id");
        query += " = " + zoneId + " WHERE " + dataBaseObject.quoteName("state_id") + " IN (" + statesIds.toString() + ")";

        dataBaseObject.setQuery(query);
        /*return Db::getInstance()->execute('
           = '.(int)$id_zone.' WHERE `id_state` IN ('.implode(',', $ids_states).')
        ');*/
        return dataBaseObject.query();
    }
}