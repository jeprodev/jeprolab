package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 01/02/2014.
 */
public class JeproLabTagModel extends JeproLabModel{
    /** @var int Language id */
    public int language_id;

    /* @var string Name * /
    public $name;

    /**
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'tag',
                    'primary' => 'id_tag',
                    'fields' => array(
                    'id_lang' =>    array('type' => JeproLabTagModel.TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'name' =>        array('type' => JeproLabTagModel.TYPE_STRING, 'validate' => 'isGenericName', 'required' => true, 'size' => 32),
    ),
            );


    protected $webserviceParameters = array(
            'fields' => array(
            'id_lang' => array('xlink_resource' => 'languages'),
    ),
            );

    public function __construct($id = null, $name = null, $id_lang = null)
    {
        $this->def = Tag::getDefinition($this);
        $this->setDefinitionRetrocompatibility();

        if ($id) {
            parent::__construct($id);
        } elseif ($name && Validate::isGenericName($name) && $id_lang && Validate::isUnsignedId($id_lang)) {
        $row = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow('
                SELECT *
                        FROM `'._DB_PREFIX_.'tag` t
        WHERE `name` = \''.pSQL($name).'\' AND `id_lang` = '.(int)$id_lang);

        if ($row) {
            $this->id = (int)$row['id_tag'];
            $this->id_lang = (int)$row['id_lang'];
            $this->name = $row['name'];
        }
    }
    }

    public function add($autodate = true, $null_values = false)
    {
        if (!parent::add($autodate, $null_values)) {
        return false;
    } elseif (isset($_POST['products'])) {
        return $this->setProducts(Tools::getValue('products'));
    }
        return true;
    }

    /**
     * Add several tags in database and link it to a product
     *
     * @param int $id_lang Language id
     * @param int $id_product Product id to link tags with
     * @param string|array $tag_list List of tags, as array or as a string with comas
     * @return bool Operation success
     * /
    public static function addTags($id_lang, $id_product, $tag_list, $separator = ',')
    {
        if (!Validate::isUnsignedId($id_lang)) {
        return false;
    }

        if (!is_array($tag_list)) {
            $tag_list = array_filter(array_unique(array_map('trim', preg_split('#\\'.$separator.'#', $tag_list, null, PREG_SPLIT_NO_EMPTY))));
        }

        $list = array();
        if (is_array($tag_list)) {
            foreach ($tag_list as $tag) {
                if (!Validate::isGenericName($tag)) {
                    return false;
                }
                $tag = trim(Tools::substr($tag, 0, JeproLabTagModel.$definition['fields']['name']['size']));
                $tag_obj = new Tag(null, $tag, (int)$id_lang);

                /* Tag does not exist in database * /
                if (!Validate::isLoadedObject($tag_obj)) {
                    $tag_obj->name = $tag;
                    $tag_obj->id_lang = (int)$id_lang;
                    $tag_obj->add();
                }
                if (!in_array($tag_obj->id, $list)) {
                    $list[] = $tag_obj->id;
                }
            }
        }
        $data = '';
        foreach ($list as $tag) {
        $data .= '('.(int)$tag.','.(int)$id_product.','.(int)$id_lang.'),';
    }
        $data = rtrim($data, ',');

        $result = Db::getInstance()->execute('
            INSERT INTO `'._DB_PREFIX_.'product_tag` (`id_tag`, `id_product`, `id_lang`)
        VALUES '.$data);

        if ($list != array()) {
            JeproLabTagModel.updateTagCount($list);
        }

        return $result;
    }
*/
    public static void updateTagCount(List<Integer> tagList){
        /*if (!Module::getBatchMode()) {
        if ($tag_list != null) {
            $tag_list_query = ' AND pt.id_tag IN ('.implode(',', $tag_list).')';
            Db::getInstance()->execute('DELETE pt FROM `'._DB_PREFIX_.'tag_count` pt WHERE 1=1 '.$tag_list_query);
        } else {
            $tag_list_query = '';
        }



        Db::getInstance()->execute('REPLACE INTO `'._DB_PREFIX_.'tag_count` (id_group, id_tag, id_lang, id_shop, counter)
        SELECT cg.id_group, pt.id_tag, pt.id_lang, id_shop, COUNT(pt.id_tag) AS times
        FROM `'._DB_PREFIX_.'product_tag` pt
        INNER JOIN `'._DB_PREFIX_.'product_shop` product_shop
        USING (id_product)
        JOIN (SELECT DISTINCT id_group FROM `'._DB_PREFIX_.'category_group`) cg
        WHERE product_shop.`active` = 1
        AND EXISTS(SELECT 1 FROM `'._DB_PREFIX_.'category_product` cp
        LEFT JOIN `'._DB_PREFIX_.'category_group` cgo ON (cp.`id_category` = cgo.`id_category`)
        WHERE cgo.`id_group` = cg.id_group AND product_shop.`id_product` = cp.`id_product`)
        '.$tag_list_query.'
        GROUP BY pt.id_tag, pt.id_lang, cg.id_group, id_shop ORDER BY NULL');
        Db::getInstance()->execute('REPLACE INTO `'._DB_PREFIX_.'tag_count` (id_group, id_tag, id_lang, id_shop, counter)
        SELECT 0, pt.id_tag, pt.id_lang, id_shop, COUNT(pt.id_tag) AS times
        FROM `'._DB_PREFIX_.'product_tag` pt
        INNER JOIN `'._DB_PREFIX_.'product_shop` product_shop
        USING (id_product)
        WHERE product_shop.`active` = 1
        '.$tag_list_query.'
        GROUP BY pt.id_tag, pt.id_lang, id_shop ORDER BY NULL');
    }*/
    }
/*
    public static function getMainTags($id_lang, $nb = 10)
    {
        $context = Context::getContext();
        if (Group::isFeatureActive()) {
        $groups = FrontController::getCurrentCustomerGroups();
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
                SELECT t.name, counter AS times
                FROM `'._DB_PREFIX_.'tag_count` pt
        LEFT JOIN `'._DB_PREFIX_.'tag` t ON (t.id_tag = pt.id_tag)
        WHERE pt.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '= 1').'
        AND pt.`id_lang` = '.(int)$id_lang.' AND pt.`id_shop` = '.(int)$context->shop->id.'
        ORDER BY times DESC
        LIMIT '.(int)$nb);
    } else {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
                SELECT t.name, counter AS times
                FROM `'._DB_PREFIX_.'tag_count` pt
        LEFT JOIN `'._DB_PREFIX_.'tag` t ON (t.id_tag = pt.id_tag)
        WHERE pt.id_group = 0 AND pt.`id_lang` = '.(int)$id_lang.' AND pt.`id_shop` = '.(int)$context->shop->id.'
        ORDER BY times DESC
        LIMIT '.(int)$nb);
    }
    }
*/
    public static Map<String, String> getAnalyzeTags(int analyzeId) {
        if (staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tag." + staticDataBaseObject.quoteName("lang_id") + ", tag." + staticDataBaseObject.quoteName("name") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_tag") + " AS tag LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag");
        query += " AS analyze_tag ON (analyze_tag.tag_id = tag.tag_id) WHERE analyze_tag." + staticDataBaseObject.quoteName("analyze_id") + "= ";
        query += analyzeId;

        staticDataBaseObject.setQuery(query);
        ResultSet tagSet = staticDataBaseObject.loadObject();
        Map<String, String> result = new HashMap<>();
        try {
            while(tagSet.next()) {
//todo
            }
        } catch (SQLException ignored) {

        }
        /*if (!$tmp = Db::getInstance (_PS_USE_SQL_SLAVE_) -> executeS(''))
        {
            return false;
        }
        $result = array();
        foreach($tmp as $tag) {
            $result[$tag['id_lang']][]=$tag['name'];
        }*/
        return result;
    }
/*
    public function getProducts($associated = true, Context $context = null)
    {
        if (!$context) {
            $context = Context::getContext();
        }
        $id_lang = $this->id_lang ? $this->id_lang : $context->language->id;

        if (!$this->id && $associated) {
            return array();
        }

        $in = $associated ? 'IN' : 'NOT IN';
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT pl.name, pl.id_product
            FROM `'._DB_PREFIX_.'product` p
        LEFT JOIN `'._DB_PREFIX_.'product_lang` pl ON p.id_product = pl.id_product'.Shop::addSqlRestrictionOnLang('pl').'
        '.Shop::addSqlAssociation('product', 'p').'
        WHERE pl.id_lang = '.(int)$id_lang.'
        AND product_shop.active = 1
        '.($this->id ? ('AND p.id_product '.$in.' (SELECT pt.id_product FROM `'._DB_PREFIX_.'product_tag` pt WHERE pt.id_tag = '.(int)$this->id.')') : '').'
        ORDER BY pl.name');
    }

    public function setProducts($array)
    {
        $result = Db::getInstance()->delete('product_tag', 'id_tag = '.(int)$this->id);
        if (is_array($array)) {
            $array = array_map('intval', $array);
            $result &= ObjectModel::updateMultishopTable('Product', array('indexed' => 0), 'a.id_product IN ('.implode(',', $array).')');
            $ids = array();
            foreach ($array as $id_product) {
                $ids[] = '('.(int)$id_product.','.(int)$this->id.','.(int)$this->id_lang.')';
            }

            if ($result) {
                $result &= Db::getInstance()->execute('INSERT INTO '._DB_PREFIX_.'product_tag (id_product, id_tag, id_lang) VALUES '.implode(',', $ids));
                if (Configuration::get('PS_SEARCH_INDEXATION')) {
                    $result &= Search::indexation(false);
                }
            }
        }
        JeproLabTagModel.updateTagCount(array((int)$this->id));
        return $result;
    }
*/
    public static boolean deleteTagsForAnalyze(int analyzeId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tag_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag") + " WHERE analyze_id = " + analyzeId;
        staticDataBaseObject.setQuery(query);
        ResultSet tagsSet = staticDataBaseObject.loadObject();

        query = "DELETE " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag") + " WHERE analyzeId = " + analyzeId;
        staticDataBaseObject.setQuery(query);
        boolean result = staticDataBaseObject.query(false);

        query = "DELETE " + staticDataBaseObject.quoteName("#__jeprolab_tag") + " NOT EXISTS (SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag");
        query += " WHERE " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag") + ".tag_id = " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag") + ".tag_id)";

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);

        List<Integer> tagList = new ArrayList<>();
        if(tagsSet != null){
            try{
                while(tagsSet.next()){
                    tagList.add(tagsSet.getInt("tag_id"));
                }
            }catch(SQLException ignored){

            }
        }

        if (tagList.size() > 0) {
            JeproLabTagModel.updateTagCount(tagList);
        }
        return result;
    }
}