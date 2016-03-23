package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 01/02/14.
 */
public class JeproLabGroupReductionModel extends JeproLabModel{
    public int group_id;
    public int category_id;
    public float reduction;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'group_reduction',
                    'primary' => 'id_group_reduction',
                    'fields' => array(
                    'id_group' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_category' =>    array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'reduction' =>        array('type' => self::TYPE_FLOAT, 'validate' => 'isPrice', 'required' => true),
    ),
            );
*/
    protected static Map<String, Float> reduction_cache = new HashMap<>();

    public JeproLabGroupReductionModel(int groupReductionId){

    }
/*
    public function add($autodate = true, $null_values = false)
    {
        return (parent::add($autodate, $null_values) && $this->_setCache());
    }

    public function update($null_values = false)
    {
        return (parent::update($null_values) && $this->_updateCache());
    }

    public function delete()
    {
        $products = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT cp.`id_product`
            FROM `'._DB_PREFIX_.'category_product` cp
        WHERE cp.`id_category` = '.(int)$this->id_category
        );

        $ids = array();
        foreach ($products as $row) {
        $ids[] = $row['id_product'];
    }

        if ($ids) {
            Db::getInstance()->delete('product_group_reduction_cache', 'id_product IN ('.implode(', ', $ids).')');
        }
        return (parent::delete());
    }

    protected function _clearCache()
    {
        return Db::getInstance()->delete('product_group_reduction_cache', 'id_group = '.(int)$this->id_group);
    }
*/
    protected boolean setCache(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category_analyze." + dataBaseObject.quoteName("analyze_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_analyzecategory");
        query += " category_analyze WHERE category_analyze." + dataBaseObject.quoteName("category_id")+ " = "+ this.category_id;

        dataBaseObject.setQuery(query);
        ResultSet analyzeSet = dataBaseObject.loadObject();
        boolean result = true;
        if(analyzeSet != null){
            try{
                while (analyzeSet.next()){
                    query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_analyze_group_reduction_cache") + "(" + dataBaseObject.quoteName("analyze_id");
                    query += ", " + dataBaseObject.quoteName("group_id") + ", " + dataBaseObject.quoteName("reduction") + ") VALUES (" + analyzeSet.getInt("analyze_id");
                    query += ", " + this.group_id + ", " + this.reduction + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("reduction") + " = IF ( VALUES(";
                    query += dataBaseObject.quoteName("reduction") + ") > " + dataBaseObject.quoteName("reduction") + ", VALUES(" + dataBaseObject.quoteName("reduction");
                    query += "), " + dataBaseObject.quoteName("reduction") + ")";

                    dataBaseObject.setQuery(query);
                    result &= dataBaseObject.query(false);
                }
                return result;
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
/*
    protected function _updateCache()
    {
        $products = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT cp.`id_product`
            FROM `'._DB_PREFIX_.'category_product` cp
        WHERE cp.`id_category` = '.(int)$this->id_category,
        false);

        $ids = array();
        foreach ($products as $product) {
        $ids[] = $product['id_product'];
    }

        $result = true;
        if ($ids) {
            $result &= Db::getInstance()->update('product_group_reduction_cache', array(
                            'reduction' => (float)$this->reduction,
                    ), 'id_product IN('.implode(', ', $ids).') AND id_group = '.(int)$this->id_group);
        }

        return $result;
    }

    public static function getGroupReductions($id_group, $id_lang)
    {
        $lang = $id_lang.Shop::addSqlRestrictionOnLang('cl');
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT gr.`id_group_reduction`, gr.`id_group`, gr.`id_category`, gr.`reduction`, cl.`name` AS category_name
            FROM `'._DB_PREFIX_.'group_reduction` gr
        LEFT JOIN `'._DB_PREFIX_.'category_lang` cl ON (cl.`id_category` = gr.`id_category` AND cl.`id_lang` = '.(int)$lang.')
        WHERE `id_group` = '.(int)$id_group
        );
    }
*/
    public static float getValueForAnalyze(int analyzeId, int groupId){
        if(!JeproLabGroupModel.isFeaturePublished()){
            return 0;
        }
        String cacheKey =  "jeprolab_get_value_for_analyze_" + analyzeId + "_" + groupId;

        if (!JeproLabGroupReductionModel.reduction_cache.containsKey(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT "  + staticDataBaseObject.quoteName("reduction") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_group_reduction_cache");
            query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND " + staticDataBaseObject.quoteName("group_id") + " = " +  groupId;

            staticDataBaseObject.quoteName(query);
            float reduction = (float)staticDataBaseObject.loadValue("reduction");
            JeproLabGroupReductionModel.reduction_cache.put(cacheKey, reduction);
        }
        // Should return string (decimal in database) and not a float
        return JeproLabGroupReductionModel.reduction_cache.get(cacheKey);
    }
/*
    public static function doesExist($id_group, $id_category){
        return (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT `id_group`
            FROM `'._DB_PREFIX_.'group_reduction`
        WHERE `id_group` = '.(int)$id_group.' AND `id_category` = '.(int)$id_category);
    }
*/
    public static ResultSet getGroupsByCategoryId(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT gr." + staticDataBaseObject.quoteName("group_id") + " AS group_id , gr." + staticDataBaseObject.quoteName("reduction");
        query += "AS group_reduction_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_group_reduction") + " AS gr WHERE " + staticDataBaseObject.quoteName("category_id");
        query += " = "  + categoryId;

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    /*
     * @deprecated 1.5.3.0
     * @param int $id_category
     * @return array|null
     * /
    public static function getGroupByCategoryId($id_category)
    {
        Tools::displayAsDeprecated('Use GroupReduction::getGroupsByCategoryId($id_category)');
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
            SELECT gr.`id_group` as id_group, gr.`reduction` as reduction, id_group_reduction
            FROM `'._DB_PREFIX_.'group_reduction` gr
        WHERE `id_category` = '.(int)$id_category, false);
    }
*/
    public static ResultSet getGroupsReductionByCategoryId(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT gr." + staticDataBaseObject.quoteName("group_reduction_id") + " as group_reduction_id, group_id FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_group_reduction") + " AS gr WHERE " + staticDataBaseObject.quoteName("category_id");
        query += " = " + categoryId;

        staticDataBaseObject.setQuery(query);

        return staticDataBaseObject.loadObject();
    }



    public static boolean setAnalyzeReduction(int analyzeId){
        return setAnalyzeReduction(analyzeId, 0, 0, 0);
    }

    public static boolean setAnalyzeReduction(int analyzeId, int groupId){
        return setAnalyzeReduction(analyzeId, groupId, 0, 0);
    }

    public static boolean setAnalyzeReduction(int analyzeId, int groupId, int categoryId){
        return setAnalyzeReduction(analyzeId, groupId, categoryId, 0);
    }

    public static boolean setAnalyzeReduction(int analyzeId, int groupId, int categoryId, float reduction){
        boolean result = true;
        JeproLabGroupReductionModel.deleteAnalyzeReduction(analyzeId);

        List<Integer> categories = JeproLabAnalyzeModel.getAnalyzeCategories(analyzeId);

        if (categories.size() > 0) {
            for(int id : categories){
                ResultSet reductions = JeproLabGroupReductionModel.getGroupsByCategoryId(id);
                if (reductions != null){
                    try {
                        JeproLabGroupReductionModel currentGroupReduction;
                        while (reductions.next()) {
                            currentGroupReduction = new JeproLabGroupReductionModel(reductions.getInt("group_reduction_id"));
                            result &= currentGroupReduction.setCache();
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
                }
            }
        }
        return result;
    }

    public static void deleteAnalyzeReduction(int analyzeId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_group_reduction_cache") + " WHERE ";
        query += staticDataBaseObject.quoteName("analyze_id") + " = "+ analyzeId;

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);
    }
/*
    public static function duplicateReduction($id_product_old, $id_product)
    {
        $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executes('
            SELECT pgr.`id_product`, pgr.`id_group`, pgr.`reduction`
            FROM `'._DB_PREFIX_.'product_group_reduction_cache` pgr
        WHERE pgr.`id_product` = '.(int)$id_product_old
        );

        if (!$res) {
            return true;
        }

        $query = '';

        foreach ($res as $row) {
        $query .= 'INSERT INTO `'._DB_PREFIX_.'product_group_reduction_cache` (`id_product`, `id_group`, `reduction`) VALUES ';
        $query .= '('.(int)$id_product.', '.(int)$row['id_group'].', '.(float)$row['reduction'].') ON DUPLICATE KEY UPDATE `reduction` = '.(float)$row['reduction'].';';
    }

        return Db::getInstance()->execute($query);
    }
*/
    public static boolean deleteCategory(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_group_reduction") + " WHERE ";
        query += staticDataBaseObject.quoteName("category_id") + " = " + categoryId;
        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }
}