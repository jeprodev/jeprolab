package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JeproLabCountryModel  extends JeproLabModel{
    public int country_id = 0;

    public int lang_id = 0;

    public int lab_id = 0;

    public int zone_id = 0;

    public int currency_id = 0;

    public char[] isoCode = new char[3];

    public Map<String, String> name;

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

    private static Pagination pagination = null;



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
        if(lang_id > 0){
            this.lang_id = (JeproLabLanguageModel.checkLanguage(lang_id) ? lang_id : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(lab_id > 0  && this.isMultiLab()){
            this.lab_id = lab_id;
            this.getLabFromContext = false;
        }

        if(this.isMultiLab() && this.lab_id <= 0){
            this.lab_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        name = new HashMap<>();
        Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

        if(country_id > 0){
            String cacheKey = "jeproshop_country_model_" + country_id + '_' + lang_id + '_' + lab_id;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeproshop_country") + " AS country ";

                //Get language data
                if(lang_id > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeproshop_country_lang") + " AS country_lang ON (country_lang.";
                    query +=  dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id");
                    query += " AND country_lang." + dataBaseObject.quoteName("lang_id") + " = " + lang_id + ")";
                }

                if(JeproLabLaboratoryModel.isTableAssociated("country")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeproshop_country_shop") + " AS country_shop ON (country_lab.";
                    query += dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id") + " AND country_lab.";
                    query += dataBaseObject.quoteName("shop_id") + " = " + lab_id + ") ";
                }

                dataBaseObject.setQuery(query);
                ResultSet countryData = dataBaseObject.loadObject();

                try {
                    while(countryData.next()) {
                        this.country_id =  countryData.getInt("country_id");
                        this.zone_id = countryData.getInt("zone_id");
                        this.currency_id = countryData.getInt("currency_id");
                        this.isoCode = countryData.getString("iso_code").toCharArray();
                        this.callPrefix = countryData.getString("call_prefix");
                        this.published = countryData.getInt("published") > 0;
                        this.containsStates = countryData.getInt("contains_states") > 0;
                        this.needIdentificationNumber = countryData.getInt("need_identification_number") > 0;
                        this.needZipCode = countryData.getInt("need_zip_code") > 0;
                        this.zipCodeFormat = countryData.getString("zip_code_format");
                        this.displayTaxLabel = countryData.getInt("display_tax_label") > 0;

                        if(lang_id > 0){
                            //country.n
                        }

                        if (lang_id <= 0) {
                            query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeproshop_country_lang");
                            query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + country_id;

                            dataBaseObject.setQuery(query);
                            ResultSet countryLangData = dataBaseObject.loadObject();
                            while(countryLangData.next()) {
                                int langId = countryLangData.getInt("lang_id");
                                String countryName = countryLangData.getString("name");
                                Iterator langIt = languages.entrySet().iterator();
                                while(langIt.hasNext()){
                                    Map.Entry lang = (Map.Entry)langIt.next();
                                    JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
                                    if(langId == language.language_id){
                                        name.put("lang_" + langId, countryName);
                                    }
                                }

                            }
                        }
                        JeproLabCache.getInstance().store(cacheKey, this);
                    }
                }catch (SQLException ignored){

                }
            }else{
                JeproLabCountryModel country = (JeproLabCountryModel) JeproLabCache.getInstance().retrieve(cacheKey);
            }
            //this = country;
        }
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
            query = "SELECT SQL_CALC_FOUND_ROWS country." + staticDataBaseObject.quoteName("country_id") + ", country_lang." + staticDataBaseObject.quoteName("name");
            query += " AS name, country." + staticDataBaseObject.quoteName("iso_code") + ", country." + staticDataBaseObject.quoteName("call_prefix") + ", country.";
            query += staticDataBaseObject.quoteName("published") + ",zone." + staticDataBaseObject.quoteName("zone_id") + " AS zone_id, zone." + staticDataBaseObject.quoteName("name");
            query += " AS zone_name FROM " + staticDataBaseObject.quoteName("#__jeprolab_country") + " AS country LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country_lang." + staticDataBaseObject.quoteName("country_id");
            query += " = country." + staticDataBaseObject.quoteName("country_id") + " AND country_lang." + staticDataBaseObject.quoteName("lang_id") + " =  " + lang_id;
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
                    country.name.put("lang_" + lang_id, countriesResultSet.getString("name"));
                    //country.isoCode = countriesResultSet.getString("iso_code");
                    country.callPrefix = countriesResultSet.getString("call_prefix");
                    country.published = countriesResultSet.getInt("published") > 0;
                    //country.containsStates = countriesResultSet.getInt("contains_states") > 0;
                    //country.needIdentificationNumber = countriesResultSet.getInt("need_identification_number") > 0;
                    //country.needZipCode = countriesResultSet.getInt("need_zip_code") > 0;
                    //country.displayTaxLabel = countriesResultSet.getInt("display_tax_label") > 0;
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

            }catch (SQLException igno){ igno.printStackTrace();}


            if(use_limit){
                limit_start = limit_start -limit;
                if(limit_start < 0){ break; }
            }else{ break; }
        }while(countries.isEmpty());

        pagination = new Pagination(); //$total, $limit_start, $limit);
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

  /*  public List getCountriesList(int lang_id, boolean published, boolean containsStates, boolean listStates){
        return getCountries(lang_id , published, containsStates, listStates);
    }*/

    public boolean isMultiLab(){
        return JeproLabLaboratoryModel.isTableAssociated("country") || !this.multiLangLab;
    }

    public List getCountryList(){
        List countries = new ArrayList<>();
        return countries;
    }

    public Pagination getPagination(){
        return pagination;
    }
}