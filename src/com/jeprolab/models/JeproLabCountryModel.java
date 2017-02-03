package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryModel extends JeproLabModel{
    public int country_id = 0;

    public int language_id = 0;

    public int laboratory_id = 0;

    public int zone_id = 0;

    public int currency_id = 0;

    public String iso_code = "";

    public Map<String, String> name;

    public String call_prefix;

    public boolean published = false;

    public boolean contains_states = false;

    public boolean need_identification_number = false;

    public boolean need_zip_code = false;

    public String zip_code_format;

    public boolean display_tax_label = false;

    protected static Map<Integer, Integer> zonesId = new HashMap<>();
    private static Map<Integer, String> cache_iso_by_id = new HashMap<>();

    public List<JeproLabStateModel> states;

    public JeproLabCountryModel(){
        this(0, 0, 0);
    }

    public JeproLabCountryModel(int countryId){
        this(countryId, 0, 0);
    }

    public JeproLabCountryModel(int countryId, int langId){
        this(countryId, langId, 0);
    }

    public JeproLabCountryModel(int countryId, int langId, int labId){
        if(langId > 0){
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(labId > 0){
            this.laboratory_id = labId;
            this.get_lab_from_context = false;
        }

        if(this.laboratory_id <= 0){
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        name = new HashMap<>();
        Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

        if(countryId > 0){
            String cacheKey = "jeprolab_country_model_" + countryId + '_' + langId + '_' + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_country") + " AS country ";

                //Get language data
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country_lang.";
                    query +=  dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id");
                    query += " AND country_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ")";
                }

                if(labId < 0 && JeproLabLaboratoryModel.isTableAssociated("country")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lab") + " AS country_lab ON (country_lab.";
                    query += dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id") + " AND country_lab.";
                    query += dataBaseObject.quoteName("lab_id") + " = " + labId + ") ";
                }
                query += " WHERE country.country_id = " + countryId;

                //dataBaseObject.setQuery(query);
                ResultSet countryData = dataBaseObject.loadObjectList(query);

                try {
                    if(countryData.next()) {
                        this.country_id =  countryData.getInt("country_id");
                        this.zone_id = countryData.getInt("zone_id");
                        this.currency_id = countryData.getInt("currency_id");
                        this.iso_code = countryData.getString("iso_code");
                        this.call_prefix = countryData.getString("call_prefix");
                        this.published = countryData.getInt("published") > 0;
                        this.contains_states = countryData.getInt("contains_states") > 0;
                        this.need_identification_number = countryData.getInt("need_identification_number") > 0;
                        this.need_zip_code = countryData.getInt("need_zip_code") > 0;
                        this.zip_code_format = countryData.getString("zip_code_format");
                        this.display_tax_label = countryData.getInt("display_tax_label") > 0;

                        if (langId <= 0) {
                            query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_country_lang");
                            query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + country_id;

                            //dataBaseObject.setQuery(query);
                            ResultSet countryLangData = dataBaseObject.loadObjectList(query);
                            while(countryLangData.next()) {
                                int languageId = countryLangData.getInt("lang_id");
                                String countryName = countryLangData.getString("name");
                                for (Object o : languages.entrySet()) {
                                    Map.Entry lang = (Map.Entry) o;
                                    JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                                    if (languageId == language.language_id) {
                                        name.put("lang_" + languageId, countryName);
                                    }
                                }
                            }
                        }else{
                            name.put("lang_" + langId, countryData.getString("name"));
                        }
                        JeproLabCache.getInstance().store(cacheKey, this);
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }else{
                JeproLabCountryModel country = (JeproLabCountryModel) JeproLabCache.getInstance().retrieve(cacheKey);
                this.country_id = country.country_id;
                this.zone_id = country.country_id;
                this.currency_id = country.currency_id;
                this.iso_code = country.iso_code;
                this.call_prefix = country.call_prefix;
                this.published = country.published;
                this.contains_states = country.contains_states;
                this.need_zip_code = country.need_zip_code;
                this.need_identification_number = country.need_identification_number;
                this.zip_code_format = country.zip_code_format;
                this.display_tax_label = country.display_tax_label;
                this.name = country.name;
            }
        }
    }

    /**
     * Get a country name with its ID
     *
     * @param langId Language ID
     * @param countryId Country ID
     * @return string Country name
     */
    public static String getCountryNameByCountryId(int langId, int countryId){
        String cacheKey = "jeprolab_country_get_name_by_country_id_" + countryId + "_" + langId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("name") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country_lang");
            query += " WHERE " + dataBaseObject.quoteName("lang_id") + " = " + langId + " AND " + dataBaseObject.quoteName("country_id");
            query += " = " + countryId;

            //dataBaseObject.setQuery(query);
            String result = dataBaseObject.loadStringValue(query, "name");
            JeproLabCache.getInstance().store(cacheKey, result);

            return result;
        }
        return (String)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static List<JeproLabCountryModel> getCountries(){
        int langId = JeproLabContext.getContext().language.language_id;
        return getCountries(langId, 0, false, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId){
        return getCountries(langId, 0, false, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId, int limitStart){
        return getCountries(langId, limitStart, false, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId, int limitStart, boolean published){
        return getCountries(langId, limitStart, published, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId, int limitStart, boolean published, boolean containsStates){
        return getCountries(langId, limitStart, published, containsStates, true);
    }

    /**
     * Return available countries
     *
     * @param langId Language ID
     * @param published return only active countries
     * @param containStates return only country with states
     * @param listStates Include the states list with the returned list
     *
     * @return Array Countries and corresponding zones
     */
    public static List<JeproLabCountryModel> getCountries(int langId, int limitStart, boolean published, boolean containStates, boolean listStates) {
        List<JeproLabCountryModel> countries = new ArrayList<>();
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        /*$app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');*/

        JeproLabContext context = JeproLabContext.getContext();

        int limit = JeproLabConfigurationSettings.LIST_LIMIT;
        //int limit_start = context.list_limit_start;
        langId = langId <= 0 ? context.language.language_id : langId;
        /*int lab_id = context.laboratory.laboratory_id;
        int lab_group_id = context.laboratory.laboratory_group_id;
        /* $category_id = $app->getUserStateFromRequest($option. $view. '.cat_id', 'cat_id', 0, 'int');
        $order_by = $app->getUserStateFromRequest($option. $view. '.order_by', 'order_by', 'date_add', 'string');
        $order_way = $app->getUserStateFromRequest($option. $view. '.order_way', 'order_way', 'ASC', 'string');
        $published = $app->getUserStateFromRequest($option. $view. '.published', 'published', 0, 'string');
        $product_attribute_id = $app->getUserStateFromRequest($option. $view. '.product_attribute_id', 'product_attribute_id', 0, 'int');
*/
        boolean use_limit = true;
        if (limit <= 0)
            use_limit = false;
        String query;
        int total;
        do{
            query = "SELECT SQL_CALC_FOUND_ROWS country." + dataBaseObject.quoteName("country_id") + ", country_lang." + dataBaseObject.quoteName("name");
            query += " AS name, country." + dataBaseObject.quoteName("iso_code") + ", country." + dataBaseObject.quoteName("call_prefix") + ", country.";
            query += dataBaseObject.quoteName("published") + ",zone." + dataBaseObject.quoteName("zone_id") + " AS zone_id, zone." + dataBaseObject.quoteName("name");
            query += " AS zone_name FROM " + dataBaseObject.quoteName("#__jeprolab_country") + " AS country LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country_lang." + dataBaseObject.quoteName("country_id");
            query += " = country." + dataBaseObject.quoteName("country_id") + " AND country_lang." + dataBaseObject.quoteName("lang_id") + " =  " + langId;
            query += ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON (zone." + dataBaseObject.quoteName("zone_id") + " = country.";
            query += dataBaseObject.quoteName("zone_id") + ") WHERE 1 ORDER BY country." + dataBaseObject.quoteName("country_id");

            //dataBaseObject.setQuery(query);
            total = 0;
            ResultSet result = dataBaseObject.loadObjectList(query);
            try{
                while(result.next()){ total += 1; }
                query += (use_limit ? " LIMIT " + limitStart + ", " + limit : "");

                //dataBaseObject.setQuery(query);
                ResultSet countriesResultSet = dataBaseObject.loadObjectList(query);
                JeproLabCountryModel country;
                while (countriesResultSet.next()){
                    country = new JeproLabCountryModel();
                    country.country_id = countriesResultSet.getInt("country_id");
                    country.zone_id = countriesResultSet.getInt("zone_id");
                    country.name.put("lang_" + langId, countriesResultSet.getString("name"));
                    country.iso_code = countriesResultSet.getString("iso_code");
                    country.call_prefix = countriesResultSet.getString("call_prefix");
                    country.published = countriesResultSet.getInt("published") > 0;
                    //country.contains_states = countriesResultSet.getInt("contains_states") > 0;
                    //country.need_identification_number = countriesResultSet.getInt("need_identification_number") > 0;
                    //country.need_zip_code = countriesResultSet.getInt("need_zip_code") > 0;
                    //country.display_tax_label = countriesResultSet.getInt("display_tax_label") > 0;
                    //country.name = countriesResultSet.getString("name");
                    /*country = countriesResultSet.;
                    country = countriesResultSet.;*/

                    /**
                     * Language management
                     *  /
                     Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();
                     Iterator langIt = languages.entrySet().iterator();
                     while(langIt.hasNext()) {
                     Map.Entry lang = (Map.Entry) langIt.next();
                     JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();

                     } */
                    countries.add(country);
                }

            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }


            if(use_limit){
                limitStart = limitStart -limit;
                if(limitStart < 0){ break; }
            }else{ break; }
        }while(countries.isEmpty());

        //pagination = new Pagination(); //$total, $limit_start, $limit);
        return countries;
/*
        $query = "SELECT country_lang.*, country.*, country_lang." . dataBaseObject.quoteName('name') . " AS country_name, zone." . dataBaseObject.quoteName('name');
        $query .= " AS zone_name FROM " . dataBaseObject.quoteName('#__jeprolab_country') . " AS country " . JeprolabLabModelLab::addSqlAssociation('country');
        $query .= "	LEFT JOIN " . dataBaseObject.quoteName('#__jeprolab_country_lang') . " AS country_lang ON (country." . dataBaseObject.quoteName('country_id') ;
        $query .= " = country_lang." . dataBaseObject.quoteName('country_id') . " AND country_lang." . dataBaseObject.quoteName('lang_id') . " = " .(int)$lang_id;
        $query .= ") LEFT JOIN " . dataBaseObject.quoteName('#__jeprolab_zone') . " AS zone ON (zone." . dataBaseObject.quoteName('zone_id') . " = country.";
        $query .= dataBaseObject.quoteName('zone_id') . ") WHERE 1 " .($published ? " AND country.published = 1" : "") ;
        $query .= ($contain_states ? " AND country." . dataBaseObject.quoteName('contains_states') . " = " .(int)$contain_states : "")." ORDER BY country_lang.name ASC";

        dataBaseObject.setQuery($query);
        $result = dataBaseObject.loadObjectList();
        foreach ($result as $row){ $countries[$row->country_id] = $row; }

        if ($list_states){
            $query = "SELECT * FROM " . dataBaseObject.quoteName('#__jeprolab_state') . " ORDER BY " . dataBaseObject.quoteName('name') . " ASC";

            dataBaseObject.setQuery($query);
            $result = dataBaseObject.loadObjectList();
            foreach ($result as $row)
            if (isset($countries[$row->country_id]) && $row->published == 1) /* Does not keep the state if its country has been disabled and not selected * /
                $countries[$row->country_id]->states[] = $row;
        } **/
    }

    /**
     * Get a country iso with its ID
     *
     * @param countryId Country ID
     * @return string Country iso
     */
    public static String getIsoCodeByCountryId(int countryId){
        if (!JeproLabCountryModel.cache_iso_by_id.containsKey(countryId)) {
            String query = "SELECT " + dataBaseObject.quoteName("iso_code") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country");
            query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + countryId ;

            //dataBaseObject.setQuery(query);
            JeproLabCountryModel.cache_iso_by_id.put(countryId, dataBaseObject.loadStringValue(query, "iso_code"));
        }
        if (JeproLabCountryModel.cache_iso_by_id.containsKey(countryId)){
            return JeproLabCountryModel.cache_iso_by_id.get(countryId);
        }
        return null;
    }

    public static int getCountryIdByCountryName(String countryName){
        return getCountryIdByCountryName(countryName, JeproLabContext.getContext().language.language_id);
    }

    /**
     * Get a country id with its name
     *
     * @param countryName Country Name
     * @param langId Language ID
     *
     * @return int Country ID
     */
    public static int getCountryIdByCountryName(String countryName, int langId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("country_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country_lang");
        query += " WHERE " + dataBaseObject.quoteName("name") + " = " + dataBaseObject.quote(countryName);
        if (langId > 0) {
            query += " AND " + dataBaseObject.quoteName("lang_id") + " = " + langId;
        }

        //dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObjectList(query);

        if(resultSet != null) {
            try {
                if (resultSet.next()) {
                    return resultSet.getInt("country_id");
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return 0;
    }

    public static boolean needZipCode(int countryId){
        if (countryId <= 0) {
            return false;
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("need_zip_code") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country");
        query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + countryId;

        //dataBaseObject.setQuery(query);

        return dataBaseObject.loadValue(query, "need_zip_code") > 0;
    }

    public static String getZipCodeFormat(int countryId){
        if (countryId <= 0) {
            return null;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("zip_code_format") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country");
        query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + countryId;

        //dataBaseObject.setQuery(query);

        return dataBaseObject.loadStringValue(query, "zip_code_format");
    }

    public static List<JeproLabCountryModel> getCountriesByZoneId(int zoneId, int langId){
        if (zoneId <= 0  || langId <= 0){
            JeproLabTools.displayError(500, "");
        }

        String query = "SELECT DISTINCT country.*, country_lang.* FROM " + dataBaseObject.quoteName("#__jeprolab_country") + " AS country";
        query += JeproLabLaboratoryModel.addSqlAssociation("country", false) + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_state");
        query += " AS state ON (state." + dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id");
        query += ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country." + dataBaseObject.quoteName("country_id");
        query += " = country_lang." + dataBaseObject.quoteName("country_id") + ") WHERE (country." + dataBaseObject.quoteName("zone_id");
        query += " = " + zoneId + " OR state." + dataBaseObject.quoteName("zone_id") + " = " + zoneId + ") AND " + dataBaseObject.quoteName("lang_id");
        query += " = " + langId;

        //dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObjectList(query);
        List<JeproLabCountryModel> countries = new ArrayList<>();
        if(resultSet != null){
            try{
                JeproLabCountryModel country;
                while(resultSet.next()){
                    country = new JeproLabCountryModel();
                    country.country_id = resultSet.getInt("country_id");
                    country.name.put("lang_" + langId, resultSet.getString("name"));
                    countries.add(country);
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return countries;
    }

    public boolean needDni(){
        return JeproLabCountryModel.needDniByCountryId(this.country_id);
    }

    public static boolean needDniByCountryId(int countryId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("need_identification_number") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country");
        query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + countryId;

        //dataBaseObject.setQuery(query);
        return dataBaseObject.loadValue(query, "need_identification_number") > 0;
    }



    public static boolean containsStates(int countryId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("contains_states") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country");
        query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + countryId;

        //dataBaseObject.setQuery(query);
        return dataBaseObject.loadValue(query, "contains_states") > 0;
    }

    /**
     * @param countriesId list county id
     * @param zoneId zone id
     */
    public void affectZoneToSelection(List<Integer> countriesId, int zoneId){
        // cast every array values to int (security)
        if(countriesId.size() > 0) {
            String countriesIds = "";
            for (Integer id : countriesId){
                countriesIds += id + ", ";
            }
            countriesIds = countriesIds.endsWith(", ") ? countriesIds.substring(0, countriesIds.length() - 2) : countriesIds;

            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_country") + " SET " + dataBaseObject.quoteName("zone_id");
            query += " = " + zoneId + " WHERE " + dataBaseObject.quoteName("country_id") + " IN (" + countriesIds + ")";

            //dataBaseObject.setQuery(query);

            dataBaseObject.query(query, false);
        }
    }

    public boolean save(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_country") + " (" + dataBaseObject.quoteName("zone_id") ;
        query += ", " +dataBaseObject.quoteName("currency_id") + ", " + dataBaseObject.quoteName("iso_code") + ", " ;
        query += dataBaseObject.quoteName("call_prefix") + ", " + dataBaseObject.quoteName("published") + ", ";
        query += dataBaseObject.quoteName("contains_states") + ", " + dataBaseObject.quoteName("need_identification_number") + ", ";
        query += dataBaseObject.quoteName("need_zip_code") + ", " + dataBaseObject.quoteName("zip_code_format") + ", ";
        query += dataBaseObject.quoteName("display_tax_label") + ") VALUES (" + this.zone_id + ", " + this.currency_id + ", ";
        query +=  ", " + dataBaseObject.quote(this.iso_code) + ", " + dataBaseObject.quote(this.call_prefix) + ", ";
        query += (this.published ? 1 : 0) + ", " +  " = " + (this.contains_states ? 1 : 0) + ", " + (this.need_identification_number ? 1 : 0);
        query += ", " + (this.need_zip_code ? 1 : 0) + ", " + dataBaseObject.quote(this.zip_code_format) + ", " + (this.display_tax_label ? 1 : 0) + ") ";

        //dataBaseObject.setQuery(query);
        boolean result = dataBaseObject.query(query, true);
        this.country_id = dataBaseObject.getGeneratedKey();

        /** saving country name for different language **/
        String countryName;
        for(Object o : JeproLabLanguageModel.getLanguages().entrySet()){
            JeproLabLanguageModel lang = (JeproLabLanguageModel)(((Map.Entry) o).getValue());
            countryName = this.name.containsKey("lang_" + lang.language_id) ? this.name.get("lang_" + lang.language_id) : "";
            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_country_lang") + " (" + dataBaseObject.quoteName("country_id");
            query += ", " + dataBaseObject.quoteName("lang_id") + dataBaseObject.quoteName("name") + ") VALUES (" + this.country_id;
            query += ", " + lang.language_id + ", " + dataBaseObject.quote(countryName) + ")";

            //dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(query, false);
        }
        return result;
    }

    public boolean update(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_country") + " SET " + dataBaseObject.quoteName("zone_id");
        query += " = " + this.zone_id + ", " + dataBaseObject.quoteName("currency_id") + " = " + this.currency_id + ", ";
        query += dataBaseObject.quoteName("iso_code") + " = " + dataBaseObject.quote(this.iso_code) + ", " + dataBaseObject.quoteName("call_prefix");
        query += " = " + dataBaseObject.quote(this.call_prefix) + ", " + dataBaseObject.quoteName("published") + " = " + (this.published ? 1 : 0);
        query += ", " + dataBaseObject.quoteName("contains_states") + " = " + (this.contains_states ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("need_identification_number") + " = " + (this.need_identification_number ? 1 : 0) + ", ";
        query += dataBaseObject.quoteName("need_zip_code") + " = " + (this.need_zip_code ? 1 : 0) + ", " + dataBaseObject.quoteName("zip_code_format");
        query += " = " + dataBaseObject.quote(this.zip_code_format) + ", " + dataBaseObject.quoteName("display_tax_label") + " = ";
        query += (this.display_tax_label ? 1 : 0) + " WHERE " + dataBaseObject.quoteName("country_id") + " = " + this.country_id;

        //dataBaseObject.setQuery(query);
        boolean result = dataBaseObject.query(query, false);

        /** saving country name for different language **/
        String countryName;
        for(Object o : JeproLabLanguageModel.getLanguages().entrySet()){
            JeproLabLanguageModel lang = (JeproLabLanguageModel)(((Map.Entry) o).getValue());
            countryName = this.name.containsKey("lang_" + lang.language_id) ? this.name.get("lang_" + lang.language_id) : "";
            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_country_lang") + " SET " + dataBaseObject.quoteName("name");
            query += " = " + dataBaseObject.quote(countryName) + " WHERE " + dataBaseObject.quoteName("country_id") + " = ";
            query += this.country_id + " AND " + dataBaseObject.quoteName("lang_id") + " = " + lang.language_id;

            //dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(query, false);
        }
        return result;
    }

    public static int getZoneId(int countryId){
        if (countryId <= 0){
            JeproLabTools.displayError(500, "");
        }

        if (JeproLabCountryModel.zonesId.containsKey(countryId)) {
            return JeproLabCountryModel.zonesId.get(countryId);
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("zone_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_country");
        query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + countryId;

        //dataBaseObject.setQuery(query);
        int result = (int)dataBaseObject.loadValue(query, "zone_id");
        JeproLabCountryModel.zonesId.put(countryId, result);

        return result;
    }

    public static List<JeproLabCountryModel> getCountriesByLaboratoryId(int labId, int langId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_country") + " AS country LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_country_lab") + " AS country_lab ON (country_lab." + dataBaseObject.quoteName("country_id");
        query += " = country." + dataBaseObject.quoteName("country_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lang");
        query += " AS country_lang ON (country." + dataBaseObject.quoteName("country_id") + " = country_lang.";
        query += dataBaseObject.quoteName("country_id") + " AND country_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId;
        query += ") WHERE " + dataBaseObject.quoteName("lab_id") + " = " + labId;

        //dataBaseObject.setQuery(query);
        ResultSet countrySet = dataBaseObject.loadObjectList(query);
        List<JeproLabCountryModel> countries = new ArrayList<>();
        if(countrySet != null){
            try{
                JeproLabCountryModel country;
                while(countrySet.next()){
                    country = new JeproLabCountryModel();
                    //todo
                    countries.add(country);
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch (Exception ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return countries;
    }

    public static int getCountryIdByIso(String isoCode){
        return getCountryIdByIso(isoCode, false);
    }

    /**
     * Get a country ID with its iso code
     *
     * @param isoCode Country iso code
     * @param active return only active countries
     * @return int Country ID
     */
    public static int getCountryIdByIso(String isoCode, boolean active) {
        if (!JeproLabTools.isLanguageIsoCode(isoCode)) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_WRONG_COUNTRY_CODE8MESSAGE")); //die(Tools::displayError());
        }
        if (dataBaseObject == null) {
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("country_id") + " FROM ";
        query += dataBaseObject.quoteName("#__jeprolab_country") + " WHERE " + dataBaseObject.quoteName("iso_code");
        query += " = " + dataBaseObject.quote(isoCode.toUpperCase()) + (active ? " AND published = 1" : "");

        //dataBaseObject.setQuery(query);
        return (int)dataBaseObject.loadValue(query, "country_id");
    }


    public static class JeproLabStateModel extends JeproLabModel{
        public int state_id;

        public int country_id;

        public boolean published;
    }


    public static class JeproLabZoneModel extends JeproLabModel{
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
                    String query = "SELECT zone.* FROM " + dataBaseObject.quoteName("#__jeprolab_zone") + " AS zone WHERE ";
                    query += dataBaseObject.quoteName("zone_id") + " = " + zoneId;
                    //dataBaseObject.setQuery(query);
                    ResultSet zoneData = dataBaseObject.loadObjectList(query);
                    if(zoneData != null){
                        try{
                            if(zoneData.next()){
                                this.zone_id = zoneData.getInt("zone_id");
                                this.name = zoneData.getString("name");
                                this.allow_delivery = zoneData.getInt("allow_delivery") > 0;
                            }
                        }catch (SQLException ignored){
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }finally {
                            try {
                                JeproLabFactory.removeConnection(dataBaseObject);
                            }catch (Exception ignored) {
                                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
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
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_zone") + (allow_delivery ? " WHERE allow_delivery = 1 " : "");
                query += " ORDER BY " + dataBaseObject.quoteName("zone_id") + " ASC ";

                //dataBaseObject.setQuery(query);
                ResultSet result = dataBaseObject.loadObjectList(query);

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
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
                JeproLabCache.getInstance().store(cacheKey, zones);
            }
            return (List<JeproLabZoneModel>)JeproLabCache.getInstance().retrieve(cacheKey);
        }

        /**
         * Get a zone ID from its default language name
         *
         * @param name zone name
         * @return zone_id
         */
        public static int getZoneIdByName(String name){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("zone_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_zone");
            query += " WHERE " + dataBaseObject.quoteName("name") + " = " + dataBaseObject.quote(name);

            //dataBaseObject.setQuery(query);
            return (int)dataBaseObject.loadValue(query, "zone_id");
        }

        /**
         * Get a zone ID from its default language name
         *
         * @param zoneId zone id
         * @return zone_id
         */
        public static String getNameByZoneId(int zoneId){
            if(zoneId <= 0){ return ""; }

            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("name") + " FROM " + dataBaseObject.quoteName("#__jeprolab_zone");
            query += " WHERE " + dataBaseObject.quoteName("zone_id") + " = " + zoneId;

            //dataBaseObject.setQuery(query);
            return dataBaseObject.loadStringValue(query, "name");
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
            //dataBaseObject.setQuery(query);
            if (dataBaseObject.query(query, false)) {
                // Delete regarding delivery preferences
                query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_carrier_zone") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;

                //dataBaseObject.setQuery(query);
                boolean result = dataBaseObject.query(query, false);

                query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_delivery") + " WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
                //dataBaseObject.setQuery(query);
                //todo result &= $db->query();

                // Update Country & state zone with 0
                query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_country") + " SET "  + dataBaseObject.quoteName("zone_id") + " = 0 WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
                //dataBaseObject.setQuery(query);
                result &= dataBaseObject.query(query, false);

                query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_state") + " SET "  + dataBaseObject.quoteName("zone_id") + " = 0 WHERE "  + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;
                //dataBaseObject.setQuery(query);
                result &= dataBaseObject.query(query, false);

                return result;
            }

            return false;
        }

        public boolean save(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_zone") + " (" + dataBaseObject.quoteName("name") + ", ";
            query += dataBaseObject.quoteName("allow_delivery") + ") VALUES (" + dataBaseObject.quote(this.name) + ", " ;
            query += (this.allow_delivery ? 1 : 0) + ")";

            //dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.query(query, true);
            this.zone_id = dataBaseObject.getGeneratedKey();
            return result;
        }

        public boolean update(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_zone") + " SET " + dataBaseObject.quoteName("name");
            query += " = " + dataBaseObject.quote(this.name) + ", " + dataBaseObject.quoteName("allow_delivery") + " = ";
            query += (this.allow_delivery ? 1 : 0) + " WHERE " + dataBaseObject.quoteName("zone_id") + " = " + this.zone_id;

            //dataBaseObject.setQuery(query);
            return dataBaseObject.query(query, false);
        }

    }
}
