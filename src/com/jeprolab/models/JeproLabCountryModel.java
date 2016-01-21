package com.jeprolab.models;


import javafx.scene.control.Pagination;

public class JeproLabCountryModel  extends JeproLabModel{
    public int country_id = 0;

    public int lang_id = 0;

    public int lab_id = 0;

    public int zone_id = 0;

    public int currency_id = 0;

    public char[] isoCode = new char[3];

    public String name;

    public int callPrefix;

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

    /**
     * @brief Return available countries
     *
     * @param integer $lang_id Language ID
     * @param boolean $published return only active countries
     * @param boolean $contain_states return only country with states
     * @param boolean $list_states Include the states list with the returned list
     *
     * @return Array Countries and corresponding zones
     */
    public static function getStaticCountries($lang_id, $published = false, $contain_states = false, $list_states = true) {
        $countries = array();
        $db = JFactory::getDBO();

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
            if (isset($countries[$row->country_id]) && $row->published == 1) /* Does not keep the state if its country has been disabled and not selected */
                $countries[$row->country_id]->states[] = $row;
        }
        return $countries;
    }

    public function isMultiLab(){
        return JeprolabLabModelLab::isTableAssociated('country') || !empty($this->multiLangLab);
    }

    public function getCountryList(JeprolabContext $context = NULL){
        jimport('joomla.html.pagination');
        $db = JFactory::getDBO();
        $app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');

        if(!$context){ $context = JeprolabContext::getContext(); }

        $limit = $app->getUserStateFromRequest('global.list.limit', 'limit', $app->getCfg('list_limit'), 'int');
        $limit_start = $app->getUserStateFromRequest($option. $view. '.limit_start', 'limit_start', 0, 'int');
        $lang_id = $app->getUserStateFromRequest($option. $view. '.lang_id', 'lang_id', $context->language->lang_id, 'int');
        $lab_id = $app->getUserStateFromRequest($option. $view. '.lab_id', 'lab_id', $context->lab->lab_id, 'int');
        $lab_group_id = $app->getUserStateFromRequest($option. $view. '.lab_group_id', 'lab_group_id', $context->lab->lab_group_id, 'int');
        $category_id = $app->getUserStateFromRequest($option. $view. '.cat_id', 'cat_id', 0, 'int');
        $order_by = $app->getUserStateFromRequest($option. $view. '.order_by', 'order_by', 'date_add', 'string');
        $order_way = $app->getUserStateFromRequest($option. $view. '.order_way', 'order_way', 'ASC', 'string');
        $published = $app->getUserStateFromRequest($option. $view. '.published', 'published', 0, 'string');
        $product_attribute_id = $app->getUserStateFromRequest($option. $view. '.product_attribute_id', 'product_attribute_id', 0, 'int');

        $use_limit = true;
        if ($limit === false)
            $use_limit = false;

        do{
            $query = "SELECT SQL_CALC_FOUND_ROWS country." . $db->quoteName('country_id') . ", country_lang." . $db->quoteName('name');
            $query .= " AS name, country." . $db->quoteName('iso_code') . ", country." . $db->quoteName('call_prefix') . ", country.";
            $query .= $db->quoteName('published') . ",zone." . $db->quoteName('zone_id'). " AS zone, zone." . $db->quoteName('name');
            $query .= " AS zone_name FROM " . $db->quoteName('#__jeprolab_country') . " AS country LEFT JOIN ";
            $query .= $db->quoteName('#__jeprolab_country_lang') . " AS country_lang ON (country_lang." . $db->quoteName('country_id');
            $query .= " = country." . $db->quoteName('country_id') . " AND country_lang." . $db->quoteName('lang_id') . " = " . $lang_id;
            $query .= ") LEFT JOIN " . $db->quoteName('#__jeprolab_zone') . " AS zone ON (zone." . $db->quoteName('zone_id') . " = country.";
            $query .= $db->quoteName('zone_id') .") WHERE 1 ORDER BY country." . $db->quoteName('country_id');

            $db->setQuery($query);
            $total = count($db->loadObjectList());

            $query .= (($use_limit === true) ? " LIMIT " .(int)$limit_start . ", " .(int)$limit : "");

            $db->setQuery($query);
            $countries = $db->loadObjectList();

            if($use_limit == true){
                $limit_start = (int)$limit_start -(int)$limit;
                if($limit_start < 0){ break; }
            }else{ break; }
        }while(empty($countries));

        $this->pagination = new JPagination($total, $limit_start, $limit);
        return $countries;
    }

    public function getPagination(){
        return $this->pagination;
    }

}