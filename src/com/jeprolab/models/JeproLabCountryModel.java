package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeproLabCountryModel  extends JeproLabModel{
    public int country_id = 0;

    public int lang_id = 0;

    public int lab_id = 0;

    public int zone_id = 0;

    public int currency_id = 0;

    public char[] isoCode = new char[3];

    public String name;

    public String callPrefix;

    public boolean published = false;

    public boolean containsStates = false;

    public boolean needIdentificationNumber = false;

    public boolean needZipCode = false;

    public boolean displayTaxLabel = false;

    public boolean getLabFromContext = false;

    public boolean multiLang = true;

    public boolean multiLangLab = true;

    public String zipCodeFormat;

    private Pagination pagination = null;

    public JeproLabCountryModel(){
        this(0, 0, 0);
    }

    public JeproLabCountryModel(int country_id){
        this(country_id, 0, 0);
    }

    public JeproLabCountryModel(int country_id, int lang_id){
        this(country_id, lang_id, 0);
    }

    public JeproLabCountryModel(int country_id, int lang_id, int lab_id){

    }

    public static List getCountries(){
        int lang_id = JeproLabContext.getContext().language.language_id;
        return getCountries(lang_id, false, false, true);
    }

    public static List getCountries(int lang_id){
        return getCountries(lang_id, false, false, true);
    }

    public static List getCountries(int lang_id, boolean published){
        return getCountries(lang_id, published, false, true);
    }

    public static List getCountries(int lang_id, boolean published, boolean containsStates){
        return getCountries(lang_id, published, containsStates, true);
    }

    /**
     * @brief Return available countries
     *
     * @param lang_id Language ID
     * @param published return only active countries
     * @param contain_states return only country with states
     * @param list_states Include the states list with the returned list
     *
     * @return Array Countries and corresponding zones
     */
    public static List getCountries(int lang_id, boolean published, boolean contain_states, boolean list_states) {
        List countries = new ArrayList<>();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        /*$app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');*/

        JeproLabContext context = JeproLabContext.getContext();

        int limit = context.listLimit;
        int limit_start = context.listLimitStart;
        lang_id = lang_id <= 0 ? context.language.language_id : lang_id;
        int lab_id = context.laboratory.laboratory_id;
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
            query = "SELECT SQL_CALC_FOUND_ROWS country." + staticDataBaseObject.quoteName("country_id") + ", country_lang." + staticDataBaseObject.quoteName("name");
            query += " AS name, country." + staticDataBaseObject.quoteName("iso_code") + ", country." + staticDataBaseObject.quoteName("call_prefix") + ", country.";
            query += staticDataBaseObject.quoteName("published") + ",zone." + staticDataBaseObject.quoteName("zone_id") + " AS zone, zone." + staticDataBaseObject.quoteName("name");
            query += " AS zone_name FROM " + staticDataBaseObject.quoteName("#__jeprolab_country") + " AS country LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country_lang." + staticDataBaseObject.quoteName("country_id");
            query += " = country." + staticDataBaseObject.quoteName("country_id") + " AND country_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + lang_id;
            query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON (zone." + staticDataBaseObject.quoteName("zone_id") + " = country.";
            query += staticDataBaseObject.quoteName("zone_id") + ") WHERE 1 ORDER BY country." + staticDataBaseObject.quoteName("country_id");

            staticDataBaseObject.setQuery(query);
            total = 0;
            ResultSet result = staticDataBaseObject.loadObject();
            try{
                while(result.next()){ total += 1; }
                query += (use_limit ? " LIMIT " + limit_start + ", " + limit : "");

                staticDataBaseObject.setQuery(query);
                ResultSet countriesResultSet = staticDataBaseObject.loadObject();
                JeproLabCountryModel country;
                while (countriesResultSet.next()){
                    country = new JeproLabCountryModel();
                    country.country_id = countriesResultSet.getInt("country_id");
                    country.zone_id = countriesResultSet.getInt("zone_id");
                    //country.isoCode = countriesResultSet.getString("iso_code");
                    country.callPrefix = countriesResultSet.getString("call_prefix");
                    country.published = countriesResultSet.getInt("published") > 0;
                    country.containsStates = countriesResultSet.getInt("contains_states") > 0;
                    country.needIdentificationNumber = countriesResultSet.getInt("need_identification_number") > 0;
                    country.needZipCode = countriesResultSet.getInt("need_zip_code") > 0;
                    country.displayTaxLabel = countriesResultSet.getInt("display_tax_label") > 0;
                    country.name = countriesResultSet.getString("name");
                    /*country = countriesResultSet.;
                    country = countriesResultSet.;*/
                    countries.add(country);
                }

            }catch (SQLException ignored){}


            if(use_limit){
                limit_start = limit_start -limit;
                if(limit_start < 0){ break; }
            }else{ break; }
        }while(countries.isEmpty());
/*
        $this->pagination = new JPagination($total, $limit_start, $limit);
        return $countries;

        $query = "SELECT country_lang.*, country.*, country_lang." . $db->quoteName('name') . " AS country_name, zone." . $db->quoteName('name');
        $query .= " AS zone_name FROM " . $db->quoteName('#__jeprolab_country') . " AS country " . JeprolabLabModelLab::addSqlAssociation('country');
        $query .= "	LEFT JOIN " . $db->quoteName('#__jeprolab_country_lang') . " AS country_lang ON (country." . $db->quoteName('country_id') ;
        $query .= " = country_lang." . $db->quoteName('country_id') . " AND country_lang." . $db->quoteName('lang_id') . " = " .(int)$lang_id;
        $query .= ") LEFT JOIN " . $db->quoteName('#__jeprolab_zone') . " AS zone ON (zone." . $db->quoteName('zone_id') . " = country.";
        $query .= $db->quoteName('zone_id') . ") WHERE 1 " .($published ? " AND country.published = 1" : "") ;
        $query .= ($contain_states ? " AND country." . $db->quoteName('contains_states') . " = " .(int)$contain_states : "")." ORDER BY country_lang.name ASC";

        $db->setQuery($query);
        $result = $db->loadObjectList();
        foreach ($result as $row){ $countries[$row->country_id] = $row; }

        if ($list_states){
            $query = "SELECT * FROM " . $db->quoteName('#__jeprolab_state') . " ORDER BY " . $db->quoteName('name') . " ASC";

            $db->setQuery($query);
            $result = $db->loadObjectList();
            foreach ($result as $row)
            if (isset($countries[$row->country_id]) && $row->published == 1) /* Does not keep the state if its country has been disabled and not selected * /
                $countries[$row->country_id]->states[] = $row;
        } **/
        return countries;
    }

  /*  public List getCountriesList(int lang_id, boolean published, boolean containsStates, boolean listStates){
        return getCountries(lang_id , published, containsStates, listStates);
    }*/

    public boolean isMultiLab(){
        return JeproLabLaboratoryModel.isTableAssociated("country") || !this.multiLangLab;
    }

    public void getCountryList(){

    }

    public Pagination getPagination(){
        return this.pagination;
    }
}