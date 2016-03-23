package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JeproLabGroupModel extends JeproLabModel {
    public int group_id;

    public int language_id;

    public int laboratory_id;

    /** @var string Lastname */
    public Map<String, String> name = new HashMap<>();

    /** @var string Reduction */
    public float reduction;

    /** @var int Price display method (JeproLabTaxManagerFactory inc/JeproLabTaxManagerFactory exc) */
    public int price_display_method;

    /** @var bool Show prices */
    public boolean show_prices = true;

    /** @var string Object creation date */
    public Date date_add;

    /** @var string Object last modification date */
    public Date date_upd;

    protected static boolean group_feature_active = false;

    protected static Map<Integer, JeproLabGroupModel> groups = new HashMap<>();
    protected static int unidentified_group = 0;
    protected static int customer_group = 0;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'group',
                    'primary' => 'id_group',
                    'multilang' => true,
                    'fields' => array(
                    'reduction' =>                array('type' => self::TYPE_FLOAT, 'validate' => 'isFloat'),
    'price_display_method' =>    array('type' => self::TYPE_INT, 'validate' => 'isPriceDisplayMethod', 'required' => true),
    'show_prices' =>            array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
    'date_add' =>                array('type' => self::TYPE_DATE, 'validate' => 'isDate'),
    'date_upd' =>                array('type' => self::TYPE_DATE, 'validate' => 'isDate'),

            /* Lang fields * /
    'name' =>                    array('type' => self::TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'required' => true, 'size' => 32),
    ),
            );
*/
    protected static Map<String, Float> cache_reduction = new HashMap<>();
    protected static Map<Integer, Integer>group_price_display_method = new HashMap<>();

    //protected $webserviceParameters = array();

    public JeproLabGroupModel(){ this(0, 0, 0); }
    
    public JeproLabGroupModel(int groupId){ this(groupId, 0, 0); }
    
    public JeproLabGroupModel(int groupId, int langId){ this(groupId, langId, 0); }
    
    public JeproLabGroupModel(int groupId, int langId, int labId){
        if(langId > 0){
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(groupId > 0){
            String cacheKey = "jeprolab_group_model_" + groupId + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_group") + " AS grp ";
            }
        }
        //parent::__construct($id, $id_lang, $id_shop);
        if (this.group_id > 0 && !group_price_display_method.containsKey(this.group_id)){
            group_price_display_method.put(this.group_id, this.price_display_method);
        }
    }

    public static List<JeproLabGroupModel> getGroups(int langId){
        return getGroups(langId, 0);
    }
    public static List<JeproLabGroupModel> getGroups(int langId, int labId){
        String labCriteria = "";
        if (labId > 0) {
            labCriteria = JeproLabLaboratoryModel.addSqlAssociation("group");
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT DISTINCT grp." + staticDataBaseObject.quoteName("group_id") + ", grp." + staticDataBaseObject.quoteName("reduction");
        query += ", grp." + staticDataBaseObject.quoteName("price_display_method") + ", grp_lang." + staticDataBaseObject.quoteName("name") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_group") + " AS grp LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_group_lang");
        query += " AS grp_lang ON (grp." + staticDataBaseObject.quoteName("group_id") + " = grp_lang." + staticDataBaseObject.quoteName("group_id");
        query += " AND grp_lang." + staticDataBaseObject.quoteName("lang_id") + " = " +  langId + ") " + labCriteria + " ORDER BY grp.";
        query += staticDataBaseObject.quoteName("group_id") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet groupSet = staticDataBaseObject.loadObject();
        List<JeproLabGroupModel> groupList = new ArrayList<>();

        try{
            JeproLabGroupModel group;
            while(groupSet.next()){
                group = new JeproLabGroupModel();
                group.group_id = groupSet.getInt("group_id");
                group.reduction = groupSet.getFloat("reduction");
                group.price_display_method = groupSet.getInt("price_display_method");
                if(langId > 0){
                    group.name.put("lang_" + langId, groupSet.getString("name"));
                }
                //group = groupSet.get("");
                groupList.add(group);
            }
        }catch(SQLException ignored){
            ignored.printStackTrace();
        }finally {
            try {
                JeproLabDataBaseConnector.getInstance().closeConnexion();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groupList;
    }
/*
    public function getCustomers($count = false, $start = 0, $limit = 0, $shop_filtering = false)
    {
        if ($count) {
            return Db::getInstance()->getValue('
                    SELECT COUNT(*)
            FROM `'._DB_PREFIX_.'customer_group` cg
            LEFT JOIN `'._DB_PREFIX_.'customer` c ON (cg.`id_customer` = c.`id_customer`)
            WHERE cg.`id_group` = '.(int)this.id.'
            '.($shop_filtering ? Shop::addSqlRestriction(Shop::SHARE_CUSTOMER) : '').'
            AND c.`deleted` != 1');
        }
        return Db::getInstance()->executeS('
            SELECT cg.`id_customer`, c.*
                    FROM `'._DB_PREFIX_.'customer_group` cg
        LEFT JOIN `'._DB_PREFIX_.'customer` c ON (cg.`id_customer` = c.`id_customer`)
        WHERE cg.`id_group` = '.(int)this.id.'
        AND c.`deleted` != 1
        '.($shop_filtering ? Shop::addSqlRestriction(Shop::SHARE_CUSTOMER) : '').'
        ORDER BY cg.`id_customer` ASC
        '.($limit > 0 ? 'LIMIT '.(int)$start.', '.(int)$limit : ''));
    }

    public static function getReduction($id_customer = null)
    {
        if (!isset(self::$cache_reduction['customer'][(int)$id_customer])) {
        $id_group = $id_customer ? Customer::getDefaultGroupId((int)$id_customer) : (int)Group::getCurrent()->id;
        self::$cache_reduction['customer'][(int)$id_customer] = Group::getReductionByIdGroup($id_group);
    }
        return self::$cache_reduction['customer'][(int)$id_customer];
    }
*/
    public static float getReductionByGroupId(int groupId){
        String cacheKey = "jeprolab_group_get_reduction_group_" + groupId;
        if (!JeproLabGroupModel.cache_reduction.containsKey(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("reduction") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_group");
            query += " WHERE " + staticDataBaseObject.quoteName("group_id") + " = " + groupId;

            staticDataBaseObject.setQuery(query);
            float reduction = (float)staticDataBaseObject.loadValue("reduction");
            JeproLabGroupModel.cache_reduction.put(cacheKey, reduction);
    }
        return (float)JeproLabGroupModel.cache_reduction.get(cacheKey);
    }

    public static int getPriceDisplayMethod(int groupId){
        if (!JeproLabGroupModel.group_price_display_method.containsKey(groupId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("price_display_method") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_group");
            query += " WHERE " + staticDataBaseObject.quoteName("group_id") + " = " + groupId;

            staticDataBaseObject.setQuery(query);

            JeproLabGroupModel.group_price_display_method.put(groupId, (int)staticDataBaseObject.loadValue("price_display_method"));
        }
        return JeproLabGroupModel.group_price_display_method.get(groupId);
    }
/*
    public static function getDefaultPriceDisplayMethod()
    {
        return Group::getPriceDisplayMethod((int)Configuration::get('PS_CUSTOMER_GROUP'));
    }

    public function add($autodate = true, $null_values = false)
    {
        Configuration::updateGlobalValue('PS_GROUP_FEATURE_ACTIVE', '1');
        if (parent::add($autodate, $null_values)) {
        Category::setNewGroupForHome((int)this.id);
        Carrier::assignGroupToAllCarriers((int)this.id);
        return true;
    }
        return false;
    }

    public function update($autodate = true, $null_values = false)
    {
        if (!Configuration::getGlobalValue('PS_GROUP_FEATURE_ACTIVE') && this.reduction > 0) {
        Configuration::updateGlobalValue('PS_GROUP_FEATURE_ACTIVE', 1);
    }
        return parent::update($autodate, $null_values);
    }

    public function delete()
    {
        if (this.id == (int)Configuration::get('PS_CUSTOMER_GROUP')) {
        return false;
    }
        if (parent::delete()) {
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'cart_rule_group` WHERE `id_group` = '.(int)this.id);
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'customer_group` WHERE `id_group` = '.(int)this.id);
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'category_group` WHERE `id_group` = '.(int)this.id);
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'group_reduction` WHERE `id_group` = '.(int)this.id);
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'product_group_reduction_cache` WHERE `id_group` = '.(int)this.id);
        this.truncateModulesRestrictions(this.id);

        // Add default group (id 3) to customers without groups
        Db::getInstance()->execute('INSERT INTO `'._DB_PREFIX_.'customer_group` (
        SELECT c.id_customer, '.(int)Configuration::get('PS_CUSTOMER_GROUP').' FROM `'._DB_PREFIX_.'customer` c
        LEFT JOIN `'._DB_PREFIX_.'customer_group` cg
        ON cg.id_customer = c.id_customer
        WHERE cg.id_customer IS NULL)');

        // Set to the customer the default group
        // Select the minimal id from customer_group
        Db::getInstance()->execute('UPDATE `'._DB_PREFIX_.'customer` cg
        SET id_default_group =
                IFNULL((
                        SELECT min(id_group) FROM `'._DB_PREFIX_.'customer_group`
        WHERE id_customer = cg.id_customer),
        '.(int)Configuration::get('PS_CUSTOMER_GROUP').')
        WHERE `id_default_group` = '.(int)this.id);

        return true;
    }
        return false;
    }

    /**
     * This method is allow to know if a feature is used or active
     * @since 1.5.0.1
     * @return bool
     */
    public static boolean isFeaturePublished() {
        if (!group_feature_active) {
            group_feature_active = JeproLabSettingModel.getIntValue("group_feature_active") > 0;
        }
        return group_feature_active;
    }

    /**
     * This method is allow to know if there are other groups than the default ones
        *
     * @return bool
     */
    public static boolean isCurrentlyUsed(){ //String table = null, $has_active_column = false
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT COUNT(*) AS num FROM " + staticDataBaseObject.quoteName("#__jeprolab_group");
        staticDataBaseObject.setQuery(query);
        int val = (int)staticDataBaseObject.loadValue("num");
        return val > 3;
    }

    /*
     * Truncate all modules restrictions for the group
     *
     * @param groupId
     * @return bool
     * /
    public static function truncateModulesRestrictions(int groupId)
    {
        return Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'module_group`
        WHERE `id_group` = '.(int)$id_group);
    }

    /*
     * Truncate all restrictions by module
     *
     * @param int $id_module
     * @return bool
     * /
    public static function truncateRestrictionsByModule($id_module)
    {
        return Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'module_group`
        WHERE `id_module` = '.(int)$id_module);
    }

    /*
     * Adding restrictions modules to the group with id $id_group
     * @param $id_group
     * @param $modules
     * @param array $shops
     * @return bool
     * /
    public static function addModulesRestrictions($id_group, $modules, $shops = array(1))
    {
        if (!is_array($modules) || !count($modules) || !is_array($shops) || !count($shops)) {
            return false;
        }

        // Delete all record for this group
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'module_group` WHERE `id_group` = '.(int)$id_group);

        $sql = 'INSERT INTO `'._DB_PREFIX_.'module_group` (`id_module`, `id_shop`, `id_group`) VALUES ';
        foreach ($modules as $module) {
        foreach ($shops as $shop) {
            $sql .= '("'.(int)$module.'", "'.(int)$shop.'", "'.(int)$id_group.'"),';
        }
    }
        $sql = rtrim($sql, ',');

        return (bool)Db::getInstance()->execute($sql);
    }

    /*
     * Add restrictions for a new module.
     * We authorize every groups to the new module
     *
     * @param int $id_module
     * @param array $shops
     * @return bool
     * /
    public static function addRestrictionsForModule($id_module, $shops = array(1))
    {
        if (!is_array($shops) || !count($shops)) {
            return false;
        }

        $res = true;
        foreach ($shops as $shop) {
        $res &= Db::getInstance()->execute('
                INSERT INTO `'._DB_PREFIX_.'module_group` (`id_module`, `id_shop`, `id_group`)
        (SELECT '.(int)$id_module.', '.(int)$shop.', id_group FROM `'._DB_PREFIX_.'group`)');
    }
        return $res;
    }

    /**
     * Return current group object
     * Use context
     *
     * @return group Group object
     */
    public static JeproLabGroupModel getCurrent(){
        if (unidentified_group == 0) {
            unidentified_group = JeproLabSettingModel.getIntValue("unidentified_group");
        }

        if (customer_group == 0) {
            customer_group = JeproLabSettingModel.getIntValue("customer_group");
        }

        JeproLabCustomerModel customer = JeproLabContext.getContext().customer;
        int groupId;
        if (customer != null && customer.customer_id > 0){
            groupId = customer.default_group_id;
        } else {
            groupId = unidentified_group;
        }

        if (!groups.containsKey(groupId)) {
            groups.put(groupId, new JeproLabGroupModel(groupId));
        }

        if (!groups.get(groupId).isAssociatedToLaboratory(JeproLabContext.getContext().laboratory.laboratory_id)) {
            groupId = customer_group;
            if (!groups.containsKey(groupId)){
                groups.put(groupId, new JeproLabGroupModel(groupId));
            }
        }

        return groups.get(groupId);
    }

    /*
     * Light back office search for Group
     *
     * @param request Searched string
     * @return array Corresponding groups
     */
    public static List<JeproLabGroupModel> searchByName(String request){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT grp.*, grp_lang.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_group") +  " AS grp LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_group_lang") + " AS grp_lang ON (grp."  + staticDataBaseObject.quoteName("group_id");
        query += " = grp_lang." + staticDataBaseObject.quoteName("group_id") + ") WHERE " + staticDataBaseObject.quoteName("name");
        query += " = " + staticDataBaseObject.quote(request, true);
        staticDataBaseObject.setQuery(query);
        ResultSet result = staticDataBaseObject.loadObject();
        List<JeproLabGroupModel> groups = new ArrayList<>();
        try{
            JeproLabGroupModel group;
            while(result.next()){
                group = new JeproLabGroupModel();
                group.group_id = result.getInt("group_id");
                groups.add(group);
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
        return groups;
    }

    public boolean isAssociatedToLaboratory(){
        return isAssociatedToLaboratory(0);
    }

    /**
     * Checks if current object is associated to a shop.
     *
     * @param labId laboratory filter
     * @return bool
     */
    public boolean isAssociatedToLaboratory(int labId){
        if (labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        String cacheKey = "jeprolab_model_laboratory_group_" + this.group_id + "_" + labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("lab_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_group_lab");
            query += " WHERE " + dataBaseObject.quoteName("group_id") + " = " + this.group_id + " AND lab_id = " + labId;

            dataBaseObject.setQuery(query);

            boolean isAssociated = ((int)dataBaseObject.loadValue("lab_id")) > 0;

            JeproLabCache.getInstance().store(cacheKey, isAssociated);
            return isAssociated;
        }

        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

}