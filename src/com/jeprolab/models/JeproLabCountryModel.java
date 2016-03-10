package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
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

    public char[] iso_code = new char[3];

    public Map<String, String> name;

    public String call_prefix;

    public boolean published = false;

    public boolean contains_states = false;

    public boolean need_identification_number = false;

    public boolean need_zip_code = false;

    public boolean display_tax_label = false;

    public boolean get_lab_from_context = false;

    public boolean multi_lang = true;

    public boolean multi_lang_lab = true;

    public String zip_code_format;

    private static Pagination pagination = null;



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
            this.lang_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(labId > 0  && this.isMultiLab()){
            this.lab_id = labId;
            this.get_lab_from_context = false;
        }

        if(this.isMultiLab() && this.lab_id <= 0){
            this.lab_id = JeproLabContext.getContext().laboratory.laboratory_id;
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

                dataBaseObject.setQuery(query);
                ResultSet countryData = dataBaseObject.loadObject();

                try {
                    while(countryData.next()) {
                        this.country_id =  countryData.getInt("country_id");
                        this.zone_id = countryData.getInt("zone_id");
                        this.currency_id = countryData.getInt("currency_id");
                        this.iso_code = countryData.getString("iso_code").toCharArray();
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

                            dataBaseObject.setQuery(query);
                            ResultSet countryLangData = dataBaseObject.loadObject();
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

                }
            }else{
                JeproLabCountryModel country = (JeproLabCountryModel) JeproLabCache.getInstance().retrieve(cacheKey);
            }
            //this = country;
        }
    }

    public static List<JeproLabCountryModel> getCountries(){
        int lang_id = JeproLabContext.getContext().language.language_id;
        return getCountries(lang_id, false, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId){
        return getCountries(langId, false, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId, boolean published){
        return getCountries(langId, published, false, true);
    }

    public static List<JeproLabCountryModel> getCountries(int langId, boolean published, boolean containsStates){
        return getCountries(langId, published, containsStates, true);
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
    public static List<JeproLabCountryModel> getCountries(int langId, boolean published, boolean containStates, boolean listStates) {
        List<JeproLabCountryModel> countries = new ArrayList<>();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        /*$app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');*/

        JeproLabContext context = JeproLabContext.getContext();

        int limit = context.listLimit;
        int limit_start = context.listLimitStart;
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
            query = "SELECT SQL_CALC_FOUND_ROWS country." + staticDataBaseObject.quoteName("country_id") + ", country_lang." + staticDataBaseObject.quoteName("name");
            query += " AS name, country." + staticDataBaseObject.quoteName("iso_code") + ", country." + staticDataBaseObject.quoteName("call_prefix") + ", country.";
            query += staticDataBaseObject.quoteName("published") + ",zone." + staticDataBaseObject.quoteName("zone_id") + " AS zone_id, zone." + staticDataBaseObject.quoteName("name");
            query += " AS zone_name FROM " + staticDataBaseObject.quoteName("#__jeprolab_country") + " AS country LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country_lang." + staticDataBaseObject.quoteName("country_id");
            query += " = country." + staticDataBaseObject.quoteName("country_id") + " AND country_lang." + staticDataBaseObject.quoteName("lang_id") + " =  " + langId;
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
                    country.name.put("lang_" + langId, countriesResultSet.getString("name"));
                    //country.isoCode = countriesResultSet.getString("iso_code");
                    country.call_prefix = countriesResultSet.getString("call_prefix");
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
        return JeproLabLaboratoryModel.isTableAssociated("country") || !this.multi_lang_lab;
    }

    public List getCountryList(){
        List countries = new ArrayList<>();
        return countries;
    }

    public Pagination getPagination(){
        return pagination;
    }

/*
    public function delete()
    {
        if (!parent::delete()) {
        return false;
    }
        return Db::getInstance()->execute('DELETE FROM '._staticDataBaseObject.quoteName("#__jeprolab_.'cart_rule_country WHERE id_country = '.(int)$this->id);
    }

    public static function getCountriesByIdShop($id_lab, $id_lang)
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->ExecuteS('
		SELECT *
		FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country` c
		LEFT JOIN `'._staticDataBaseObject.quoteName("#__jeprolab_.'country_lab` cs ON (cs.`id_country`= c.`id_country`)
		LEFT JOIN `'._staticDataBaseObject.quoteName("#__jeprolab_.'country_lang` cl ON (c.`id_country` = cl.`id_country` AND cl.`id_lang` = '.(int)$id_lang.')
		WHERE `id_lab` = '.(int)$id_lab);
    }

    /**
     * Get a country ID with its iso code
     *
     * @param string $iso_code Country iso code
     * @param bool $active return only active coutries
     * @return int Country ID
     * /
public static function getByIso($iso_code, $active = false)
{
    if (!Validate::isLanguageIsoCode($iso_code)) {
    die(Tools::displayError());
}
    $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
        SELECT `id_country`
        FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
    WHERE `iso_code` = \''.pSQL(strtoupper($iso_code)).'\''
        .($active ? ' AND active = 1' : '')
    );

    if (isset($result['id_country']))
    {
        return (int)$result['id_country'];
    }
    return false;
}

    public static function getZoneId(int countryId){
        if (!Validate::isUnsignedId($id_country)) {
        die(Tools::displayError());
    }

        if (isset(self::$_idZones[$id_country])) {
        return (int)self::$_idZones[$id_country];
    }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT `id_zone`
            FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
        WHERE `id_country` = '.(int)$id_country);

        if (isset($result['id_zone']))
        {
            self::$_idZones[$id_country] = (int)$result['id_zone'];
            return (int)$result['id_zone'];
        }
        return false;
    }
*/
    /**
     * Get a country name with its ID
     *
     * @param langId Language ID
     * @param countryId Country ID
     * @return string Country name
     */
    public static String getNameById(int langId, int countryId){
        String cacheKey = "jeprolab_country_get_name_by_country_id_" + countryId + "_" + langId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("name") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_country_lang");
            query += " WHERE " + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND " + staticDataBaseObject.quoteName("country_id");
            query += " = " + countryId;

            staticDataBaseObject.setQuery(query);
            String result = staticDataBaseObject.loadStringValue("name");
            JeproLabCache.getInstance().store(cacheKey, result);

            return result;
        }
        return (String)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /*
     * Get a country iso with its ID
     *
     * @param int $id_country Country ID
     * @return string Country iso
     * /
    public static function getIsoById($id_country)
    {
        if (!isset(Country::$cache_iso_by_id[$id_country])) {
        Country::$cache_iso_by_id[$id_country] = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                SELECT `iso_code`
                FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
        WHERE `id_country` = '.(int)$id_country);
    }
        if (isset(Country::$cache_iso_by_id[$id_country]))
        {
            return Country::$cache_iso_by_id[$id_country];
        }
        return false;
    }*/

    public static int getIdByName(String countryName){
        return getIdByName(countryName, JeproLabContext.getContext().language.language_id);
    }
    /**
     * Get a country id with its name
     *
     * @param countryName Country Name
     * @param langId Language ID
     *
     * @return int Country ID
     */
    public static int getIdByName(String countryName, int langId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("country_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_country_lang");
        query += " WHERE " + staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(countryName);
        if (langId > 0) {
            query += " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId;
        }

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObject();

        if(resultSet != null) {
            try {
                if (resultSet.next()) {
                    return resultSet.getInt("country_id");
                }
            }catch (SQLException ignored){

            }
        }
        return 0;
    }
/*
    public static function getNeedZipCode($id_country)
    {
        if (!(int)$id_country) {
            return false;
        }

        return (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT `need_zip_code`
            FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
        WHERE `id_country` = '.(int)$id_country);
    }

    public static function getZipCodeFormat($id_country)
    {
        if (!(int)$id_country) {
            return false;
        }

        $zip_code_format = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT `zip_code_format`
            FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
        WHERE `id_country` = '.(int)$id_country);

        if (isset($zip_code_format) && $zip_code_format)
        {
            return $zip_code_format;
        }
        return false;
    }

    /**
     * Returns the default country Id
     *
     * @deprecated as of 1.5 use $context->country->id instead
     * @return int default country id
     *
    public static function getDefaultCountryId()
    {
        Tools::displayAsDeprecated();
        return Context::getContext()->country->id;
    }
*/
    public static List<JeproLabCountryModel> getCountriesByZoneId(int zoneId, int langId){
        if (zoneId <= 0  || langId <= 0){
            JeproLabTools.displayError(500, "");
        }

        String query = "SELECT DISTINCT country.*, country_lang.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_country") + " AS country";
        query += JeproLabLaboratoryModel.addSqlAssociation("country", false) + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_state");
        query += " AS state ON (state." + staticDataBaseObject.quoteName("country_id") + " = country." + staticDataBaseObject.quoteName("country_id");
        query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country." + staticDataBaseObject.quoteName("country_id");
        query += " = country_lang." + staticDataBaseObject.quoteName("country_id") + ") WHERE (country." + staticDataBaseObject.quoteName("zone_id");
        query += " = " + zoneId + " OR state." + staticDataBaseObject.quoteName("zone_id") + " = " + zoneId + ") AND " + staticDataBaseObject.quoteName("lang_id");
        query += " = " + langId;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObject();
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

            }
        }
        return countries;
    }
/*
    public function isNeedDni()
    {
        return Country::isNeedDniByCountryId($this->id);
    }

    public static function isNeedDniByCountryId($id_country)
    {
        return (bool)Db::getInstance()->getValue('
            SELECT `need_identification_number`
            FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
        WHERE `id_country` = '.(int)$id_country);
    }

    public static function containsStates($id_country)
    {
        return (bool)Db::getInstance()->getValue('
            SELECT `contains_states`
            FROM `'._staticDataBaseObject.quoteName("#__jeprolab_.'country`
        WHERE `id_country` = '.(int)$id_country);
    }

    /**
     * @param $ids_countries
     * @param $id_zone
     * @return bool
     * /
    public function affectZoneToSelection($ids_countries, $id_zone)
    {
        // cast every array values to int (security)
        $ids_countries = array_map('intval', $ids_countries);
        return Db::getInstance()->execute('
            UPDATE `'._staticDataBaseObject.quoteName("#__jeprolab_.'country` SET `id_zone` = '.(int)$id_zone.' WHERE `id_country` IN ('.implode(',', $ids_countries).')
        ');
    }

    /*
     * Replace letters of zip code format And check this format on the zip code
     * @param $zip_code
     * @return (bool)
     * /
    public function checkZipCode($zip_code)
    {
        $zip_regexp = '/^'.$this->zip_code_format.'$/ui';
        $zip_regexp = str_replace(' ', '( |)', $zip_regexp);
        $zip_regexp = str_replace('-', '(-|)', $zip_regexp);
        $zip_regexp = str_replace('N', '[0-9]', $zip_regexp);
        $zip_regexp = str_replace('L', '[a-zA-Z]', $zip_regexp);
        $zip_regexp = str_replace('C', $this->iso_code, $zip_regexp);

        return (bool)preg_match($zip_regexp, $zip_code);
    }

    public static function addModuleRestrictions(array $labs = array(), array $countries = array(), array $modules = array())
    {
        if (!count($labs)) {
            $labs = Shop::getShops(true, null, true);
        }

        if (!count($countries)) {
            $countries = Country::getCountries((int)Context::getContext()->cookie->id_lang);
        }

        if (!count($modules)) {
            $modules = Module::getPaymentModules();
        }

        $sql = false;
        foreach ($labs as $id_lab) {
        foreach ($countries as $country) {
            foreach ($modules as $module) {
                $sql .= '('.(int)$module['id_module'].', '.(int)$id_lab.', '.(int)$country['id_country'].'),';
            }
        }
    }

        if ($sql) {
            $sql = 'INSERT IGNORE INTO `'._staticDataBaseObject.quoteName("#__jeprolab_.'module_country` (`id_module`, `id_lab`, `id_country`) VALUES '.rtrim($sql, ',');
            return Db::getInstance()->execute($sql);
        } else {
            return true;
        }
    }

    public function add($autodate = true, $null_values = false)
    {
        $return = parent::add($autodate, $null_values) && self::addModuleRestrictions(array(), array(array('id_country' => $this->id)), array());
        return $return;
    }

    */
}