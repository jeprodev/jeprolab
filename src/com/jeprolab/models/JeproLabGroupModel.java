package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
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
    public String reduction;

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
    //protected static $cache_reduction = array();
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
/*
    public static function getGroups($id_lang, $id_shop = false){
        $shop_criteria = '';
        if ($id_shop) {
            $shop_criteria = Shop::addSqlAssociation('group', 'g');
        }

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT DISTINCT g.`id_group`, g.`reduction`, g.`price_display_method`, gl.`name`
            FROM `'._DB_PREFIX_.'group` g
        LEFT JOIN `'._DB_PREFIX_.'group_lang` AS gl ON (g.`id_group` = gl.`id_group` AND gl.`id_lang` = '.(int)$id_lang.')
        '.$shop_criteria.'
        ORDER BY g.`id_group` ASC');
    }

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

    public static function getReductionByIdGroup($id_group)
    {
        if (!isset(self::$cache_reduction['group'][$id_group])) {
        self::$cache_reduction['group'][$id_group] = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                SELECT `reduction`
                FROM `'._DB_PREFIX_.'group`
        WHERE `id_group` = '.(int)$id_group);
    }
        return self::$cache_reduction['group'][$id_group];
    }

    public static function getPriceDisplayMethod($id_group)
    {
        if (!isset(Group::$group_price_display_method[$id_group])) {
        self::$group_price_display_method[$id_group] = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                SELECT `price_display_method`
                FROM `'._DB_PREFIX_.'group`
        WHERE `id_group` = '.(int)$id_group);
    }
        return self::$group_price_display_method[$id_group];
    }

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

    /*
     * Return current group object
     * Use context
     *
     * @return Group Group object
     * /
    public static JeproLabGroupModel getCurrent(){
        if (unidentified_group == 0) {
            unidentified_group = Configuration::get('PS_UNIDENTIFIED_GROUP');
        }

        if (customer_group == 0) {
            customer_group = Configuration::get('PS_CUSTOMER_GROUP');
        }

        $customer = Context::getContext()->customer;
        if (Validate::isLoadedObject($customer)) {
        $id_group = (int)$customer->id_default_group;
    } else {
        $id_group = (int)$ps_unidentified_group;
    }

        if (!isset($groups[$id_group])) {
            $groups[$id_group] = new Group($id_group);
        }

        if (!$groups[$id_group]->isAssociatedToShop(Context::getContext()->shop->id)) {
        $id_group = (int)$ps_customer_group;
        if (!isset($groups[$id_group])) {
            $groups[$id_group] = new Group($id_group);
        }
    }

        return $groups[$id_group];
    }

    /**
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

        }
        return groups;
    }
}