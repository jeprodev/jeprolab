package com.jeprolab.models;


import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;

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
     * @param state_id Country ID
     * @return string State name
     * /
    public static function getNameById($state_id){
        if (!$state_id)
            return false;
        $cache_id = 'jeprolab_state_get_name_by_id_'. (int)$state_id;
        if (!JeprolabCache::isStored($cache_id)) {
            $db = JFactory::getDBO();
            $query = "SELECT " . $db->quoteName('name') . "	FROM " . $db->quoteName('#__jeprolab_state') . " WHERE " . $db->quoteName('state_id') . "= " . (int)$state_id;

            $db->setQuery($query);
            $result = $db->loadResult();
            JeprolabCache::store($cache_id, $result);
        }
        return JeprolabCache::retrieve($cache_id);
    }

    public function isMultiLab(){
        return (JeprolabLabModelLab::isTableAssociated('state') || !empty($this->multiLangLab));
    }

    public function getStateList(JeprolabContext $context = NULL){
        jimport('joomla.html.pagination');
        $db = JFactory::getDBO();
        $app = JFactory::getApplication();
        $option = $app->input->get('option');
        $view = $app->input->get('view');

        if(!$context){ $context = JeprolabContext::getContext(); }
    }

    public static function getStates($lang_id = false, $published = false) {
        $db = JFactory::getDBO();

        $query = "SELECT " . $db->quoteName('state_id') . ",  " . $db->quoteName('country_id') . ", " . $db->quoteName('zone_id') . ", " . $db->quoteName('iso_code') . ", " .  $db->quoteName('name');
        $query .= ", " . $db->quote('published') . " FROM " . $db->quoteName('#__jeprolab_state') .($published ? " WHERE " . $db->quoteName('published') . " = 1" : "") . " ORDER BY " . $db->quoteName('name') . " ASC ";

        $db->setQuery($query);
        return $db->loadObjectList();
    }

    /**
     * Get a state id with its name
     *
     * @param state_id Country ID
     * @return integer state id
     * /
    public static function getIdByName($state_id)
    {
        if (empty($state))
            return false;
        $cache_id = 'State::getNameById_'.pSQL($state);
        if (!Cache::isStored($cache_id))
        {
            $result = (int)Db::getInstance()->getValue('
                SELECT `id_state`
                FROM `'._DB_PREFIX_.'state`
            WHERE `name` LIKE \''.pSQL($state).'\'
            ');
            Cache::store($cache_id, $result);
        }
        return Cache::retrieve($cache_id);
    }

    /**
     * Get a state id with its iso code
     *
     * @param iso_code Iso code
     * @return integer state id
     * /
    public static function getIdByIso($iso_code, $id_country = null)
    {
        return Db::getInstance()->getValue('
            SELECT `id_state`
            FROM `'._DB_PREFIX_.'state`
        WHERE `iso_code` = \''.pSQL($iso_code).'\'
        '.($id_country ? 'AND `id_country` = '.(int)$id_country : ''));
    }

    /**
     * Delete a state only if is not in use
     *
     * @return boolean
     * /
    public function delete()
    {
        if (!$this->isUsed())
        {
            // Database deletion
            $result = Db::getInstance()->delete($this->def['table'], '`'.$this->def['primary'].'` = '.(int)$this->id);
            if (!$result)
                return false;

            // Database deletion for multilingual fields related to the object
            if (!empty($this->def['multilang']))
                Db::getInstance()->delete(bqSQL($this->def['table']).'_lang', '`'.$this->def['primary'].'` = '.(int)$this->id);
            return $result;
        }
        else
            return false;
    }

    /**
     * Check if a state is used
     *
     * @return boolean
     */
    public boolean isUsed()
    {
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

        String query = "SELECT COUNT(*) FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " WHERE " + dataBaseObject.quoteName("state_id") + " = " + this.state_id;
        int result = 0;

        dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObject();
        try{
            while(resultSet.next()){ result += 1; }
        }catch (SQLException ignored){}
        return result;
    }
/*
    public static function getStatesByIdCountry($id_country)
    {
        if (empty($id_country))
            die(Tools::displayError());

        return Db::getInstance()->executeS('
            SELECT *
                    FROM `'._DB_PREFIX_.'state` s
        WHERE s.`id_country` = '.(int)$id_country
        );
    }

    public static function hasCounties($id_state)
    {
        return count(County::getCounties((int)$id_state));
    }

    public static function getZoneId($id_state)
    {
        if (!Validate::isUnsignedId($id_state))
        die(Tools::displayError());

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT `id_zone`
            FROM `'._DB_PREFIX_.'state`
        WHERE `id_state` = '.(int)$id_state
        );
    }

    /**
     * @param $ids_states
     * @param $id_zone
     * @return bool
     * /
    public function affectZoneToSelection($ids_states, $id_zone)
    {
        // cast every array values to int (security)
        $ids_states = array_map('intval', $ids_states);
        return Db::getInstance()->execute('
            UPDATE `'._DB_PREFIX_.'state` SET `id_zone` = '.(int)$id_zone.' WHERE `id_state` IN ('.implode(',', $ids_states).')
        ');
    } **/
}