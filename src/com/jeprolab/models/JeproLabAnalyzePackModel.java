package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 16/02/14.
 */
public class JeproLabAnalyzePackModel extends JeproLabAnalyzeModel {
    //protected static $cachePackItems = array();
    protected static Map<Integer, Boolean> cacheIsPack = new HashMap<>();
    protected static Map<String, Boolean> cacheIsPacked = new HashMap<>();

    /**
     * Is analyze a pack?
     *
     * @param analyzeId analyze Id
     * @return bool
     */
    public static boolean isPack(int analyzeId){
        if (!JeproLabAnalyzePackModel.isFeaturePublished() || analyzeId <= 0) {
            return false;
        }

        if (!JeproLabAnalyzePackModel.cacheIsPack.containsKey(analyzeId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT COUNT(*) AS pack FROM " + staticDataBaseObject.quoteName("#__jeprolab_pack") + " WHERE ";
            query += " analyze_pack_id = " + analyzeId;

            staticDataBaseObject.setQuery(query);
            JeproLabAnalyzePackModel.cacheIsPack.put(analyzeId, staticDataBaseObject.loadValue("pack") > 0);
        }
        return JeproLabAnalyzePackModel.cacheIsPack.get(analyzeId);
    }

    public static boolean isPacked(int analyzeId){
        return JeproLabAnalyzePackModel.isPacked(analyzeId, 0);
    }

    /**
     * Is analyze in a pack?
     * If analyzeAttributeId specified, then will restrict search on the given combination,
     * else this method will match a product if at least one of all its combination is in a pack.
     *
     * @param analyzeId analyze Id
     * @param analyzeAttributeId Optional combination of the product
     * @return bool
     */
    public static boolean isPacked(int analyzeId, int analyzeAttributeId){
        if (!JeproLabAnalyzePackModel.isFeaturePublished()){
            return false;
        }
        String cacheKey = analyzeId + "_"  + analyzeAttributeId;

        if (!JeproLabAnalyzePackModel.cacheIsPacked.containsKey(cacheKey)) {
            if(staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT COUNT(*) AS pack FROM " + staticDataBaseObject.quoteName("#__jeprolab_pack") + " WHERE analyze_item_id = " + analyzeId;
            if(analyzeAttributeId > 0){
                query += " AND analyze_attribute_id = " + analyzeAttributeId;
            }
            staticDataBaseObject.setQuery(query);

            JeproLabAnalyzePackModel.cacheIsPacked.put(cacheKey, staticDataBaseObject.loadValue("pack") > 0);

            return JeproLabAnalyzePackModel.cacheIsPacked.get(cacheKey);
        }
        return JeproLabAnalyzePackModel.cacheIsPacked.get(cacheKey);
    }
/*
    public static function noPackPrice($id_product)
    {
        $sum = 0;
        $price_display_method = !JeproLabAnalyzePackModel._taxCalculationMethod;
        $items = JeproLabAnalyzePackModel.getItems($id_product, JeproLabSettingModel.getInt('PS_LANG_DEFAULT'));
        foreach ($items as $item) {
        /** @var Product $item * /
        $sum += $item->getPrice($price_display_method) * $item->pack_quantity;
    }

        return $sum;
    }

    public static function noPackWholesalePrice($id_product)
    {
        $sum = 0;
        $items = JeproLabAnalyzePackModel.getItems($id_product, JeproLabSettingModel.get('PS_LANG_DEFAULT'));
        foreach ($items as $item) {
        $sum += $item->wholesale_price * $item->pack_quantity;
    }
        return $sum;
    }

    public static function getItems($id_product, $id_lang)
    {
        if (!JeproLabAnalyzePackModel.isFeatureActive()) {
        return array();
    }

        if (array_key_exists($id_product, JeproLabAnalyzePackModel.cachePackItems)) {
            return JeproLabAnalyzePackModel.cachePackItems[$id_product];
        }
        $result = Db::getInstance()->executeS('SELECT id_product_item, id_product_attribute_item, quantity FROM `'._DB_PREFIX_.'pack` where id_product_pack = '.(int)$id_product);
        $array_result = array();
        foreach ($result as $row) {
        $p = new Product($row['id_product_item'], false, $id_lang);
        $p->loadStockData();
        $p->pack_quantity = $row['quantity'];
        $p->id_pack_product_attribute = (isset($row['id_product_attribute_item']) && $row['id_product_attribute_item'] ? $row['id_product_attribute_item'] : 0);
        if (isset($row['id_product_attribute_item']) && $row['id_product_attribute_item']) {
            $sql = 'SELECT agl.`name` AS group_name, al.`name` AS attribute_name
            FROM `'._DB_PREFIX_.'product_attribute` pa
            '.Shop::addSqlAssociation('product_attribute', 'pa').'
            LEFT JOIN `'._DB_PREFIX_.'product_attribute_combination` pac ON pac.`id_product_attribute` = pa.`id_product_attribute`
            LEFT JOIN `'._DB_PREFIX_.'attribute` a ON a.`id_attribute` = pac.`id_attribute`
            LEFT JOIN `'._DB_PREFIX_.'attribute_group` ag ON ag.`id_attribute_group` = a.`id_attribute_group`
            LEFT JOIN `'._DB_PREFIX_.'attribute_lang` al ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)Context::getContext()->language->id.')
            LEFT JOIN `'._DB_PREFIX_.'attribute_group_lang` agl ON (ag.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)Context::getContext()->language->id.')
            WHERE pa.`id_product_attribute` = '.$row['id_product_attribute_item'].'
            GROUP BY pa.`id_product_attribute`, ag.`id_attribute_group`
            ORDER BY pa.`id_product_attribute`';

            $combinations = Db::getInstance()->executeS($sql);
            foreach ($combinations as $k => $combination) {
                $p->name .= ' '.$combination['group_name'].'-'.$combination['attribute_name'];
            }
        }
        $array_result[] = $p;
    }
        JeproLabAnalyzePackModel.cachePackItems[$id_product] = $array_result;
        return JeproLabAnalyzePackModel.cachePackItems[$id_product];
    }

    public static function isInStock($id_product)
    {
        if (!JeproLabAnalyzePackModel.isFeatureActive()) {
        return true;
    }

        $items = JeproLabAnalyzePackModel.getItems((int)$id_product, JeproLabSettingsModel.get('PS_LANG_DEFAULT'));

        foreach ($items as $item) {
        /** @var Product $item * /
        // Updated for 1.5.0
        if (Product::getQuantity($item->id) < $item->pack_quantity && !$item->isAvailableWhenOutOfStock((int)$item->out_of_stock)) {
            return false;
        }
    }
        return true;
    }

    public static function getItemTable($id_product, $id_lang, $full = false)
    {
        if (!JeproLabAnalyzePackModel.isFeatureActive()) {
        return array();
    }

        $context = Context::getContext();

        $sql = 'SELECT p.*, product_shop.*, pl.*, image_shop.`id_image` id_image, il.`legend`, cl.`name` AS category_default, a.quantity AS pack_quantity, product_shop.`id_category_default`, a.id_product_pack, a.id_product_attribute_item
        FROM `'._DB_PREFIX_.'pack` a
        LEFT JOIN `'._DB_PREFIX_.'product` p ON p.id_product = a.id_product_item
        LEFT JOIN `'._DB_PREFIX_.'product_lang` pl
        ON p.id_product = pl.id_product
        AND pl.`id_lang` = '.(int)$id_lang.Shop::addSqlRestrictionOnLang('pl').'
        LEFT JOIN `'._DB_PREFIX_.'image_shop` image_shop
        ON (image_shop.`id_product` = p.`id_product` AND image_shop.cover=1 AND image_shop.id_shop='.(int)$context->shop->id.')
        LEFT JOIN `'._DB_PREFIX_.'image_lang` il ON (image_shop.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang.')
        '.Shop::addSqlAssociation('product', 'p').'
        LEFT JOIN `'._DB_PREFIX_.'category_lang` cl
        ON product_shop.`id_category_default` = cl.`id_category`
        AND cl.`id_lang` = '.(int)$id_lang.Shop::addSqlRestrictionOnLang('cl').'
        WHERE product_shop.`id_shop` = '.(int)$context->shop->id.'
        AND a.`id_product_pack` = '.(int)$id_product.'
        GROUP BY a.`id_product_item`, a.`id_product_attribute_item`';

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);

        foreach ($result as &$line) {
        if (Combination::isFeatureActive() && isset($line['id_product_attribute_item']) && $line['id_product_attribute_item']) {
            $line['cache_default_attribute'] = $line['id_product_attribute'] = $line['id_product_attribute_item'];

            $sql = 'SELECT agl.`name` AS group_name, al.`name` AS attribute_name,  pai.`id_image` AS id_product_attribute_image
            FROM `'._DB_PREFIX_.'product_attribute` pa
            '.Shop::addSqlAssociation('product_attribute', 'pa').'
            LEFT JOIN `'._DB_PREFIX_.'product_attribute_combination` pac ON pac.`id_product_attribute` = '.$line['id_product_attribute_item'].'
            LEFT JOIN `'._DB_PREFIX_.'attribute` a ON a.`id_attribute` = pac.`id_attribute`
            LEFT JOIN `'._DB_PREFIX_.'attribute_group` ag ON ag.`id_attribute_group` = a.`id_attribute_group`
            LEFT JOIN `'._DB_PREFIX_.'attribute_lang` al ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)Context::getContext()->language->id.')
            LEFT JOIN `'._DB_PREFIX_.'attribute_group_lang` agl ON (ag.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)Context::getContext()->language->id.')
            LEFT JOIN `'._DB_PREFIX_.'product_attribute_image` pai ON ('.$line['id_product_attribute_item'].' = pai.`id_product_attribute`)
            WHERE pa.`id_product` = '.(int)$line['id_product'].' AND pa.`id_product_attribute` = '.$line['id_product_attribute_item'].'
            GROUP BY pa.`id_product_attribute`, ag.`id_attribute_group`
            ORDER BY pa.`id_product_attribute`';

            $attr_name = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);

            if (isset($attr_name[0]['id_product_attribute_image']) && $attr_name[0]['id_product_attribute_image']) {
                $line['id_image'] = $attr_name[0]['id_product_attribute_image'];
            }
            $line['name'] .= "\n";
            foreach ($attr_name as $value) {
                $line['name'] .= ' '.$value['group_name'].'-'.$value['attribute_name'];
            }
        }
        $line = Product::getTaxesInformations($line);
    }

        if (!$full) {
            return $result;
        }

        $array_result = array();
        foreach ($result as $prow) {
        if (!JeproLabAnalyzePackModel.isPack($prow['id_product'])) {
            $prow['id_product_attribute'] = (int)$prow['id_product_attribute_item'];
            $array_result[] = Product::getProductProperties($id_lang, $prow);
        }
    }
        return $array_result;
    }

    public static function getPacksTable($id_product, $id_lang, $full = false, $limit = null)
    {
        if (!JeproLabAnalyzePackModel.isFeatureActive()) {
        return array();
    }

        $packs = Db::getInstance()->getValue('
            SELECT GROUP_CONCAT(a.`id_product_pack`)
            FROM `'._DB_PREFIX_.'pack` a
        WHERE a.`id_product_item` = '.(int)$id_product);

        if (!(int)$packs) {
            return array();
        }

        $context = Context::getContext();

        $sql = '
        SELECT p.*, product_shop.*, pl.*, image_shop.`id_image` id_image, il.`legend`, IFNULL(product_attribute_shop.id_product_attribute, 0) id_product_attribute
        FROM `'._DB_PREFIX_.'product` p
        NATURAL LEFT JOIN `'._DB_PREFIX_.'product_lang` pl
        '.Shop::addSqlAssociation('product', 'p').'
        LEFT JOIN `'._DB_PREFIX_.'product_attribute_shop` product_attribute_shop
        ON (p.`id_product` = product_attribute_shop.`id_product` AND product_attribute_shop.`default_on` = 1 AND product_attribute_shop.id_shop='.(int)$context->shop->id.')
        LEFT JOIN `'._DB_PREFIX_.'image_shop` image_shop
        ON (image_shop.`id_product` = p.`id_product` AND image_shop.cover=1 AND image_shop.id_shop='.(int)$context->shop->id.')
        LEFT JOIN `'._DB_PREFIX_.'image_lang` il ON (image_shop.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang.')
        WHERE pl.`id_lang` = '.(int)$id_lang.'
        '.Shop::addSqlRestrictionOnLang('pl').'
        AND p.`id_product` IN ('.$packs.')
        GROUP BY p.id_product';
        if ($limit) {
            $sql .= ' LIMIT '.(int)$limit;
        }
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
        if (!$full) {
            return $result;
        }

        $array_result = array();
        foreach ($result as $row) {
        if (!JeproLabAnalyzePackModel.isPacked($row['id_product'])) {
            $array_result[] = Product::getProductProperties($id_lang, $row);
        }
    }
        return $array_result;
    }

    public static function deleteItems($id_product)
    {
        return Db::getInstance()->update('product', array('cache_is_pack' => 0), 'id_product = '.(int)$id_product) &&
        Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'pack` WHERE `id_product_pack` = '.(int)$id_product) &&
        JeproLabSettingsModel.updateGlobalValue('PS_PACK_FEATURE_ACTIVE', JeproLabAnalyzePackModel.isCurrentlyUsed());
    }

    /**
     * Add an item to the pack
     *
     * @param int $id_product
     * @param int $id_item
     * @param int $qty
     * @param int     $id_attribute_item
     * @return bool true if everything was fine
     * @throws PrestaShopDatabaseException
     * /
    public static function addItem($id_product, $id_item, $qty, $id_attribute_item = 0)
    {
        $id_attribute_item = (int)$id_attribute_item ? (int)$id_attribute_item : Product::getDefaultAttribute((int)$id_item);
        return Db::getInstance()->update('product', array('cache_is_pack' => 1), 'id_product = '.(int)$id_product) &&
        Db::getInstance()->insert('pack', array(
                    'id_product_pack' => (int)$id_product,
            'id_product_item' => (int)$id_item,
            'id_product_attribute_item' => (int)$id_attribute_item,
            'quantity' => (int)$qty
        ))
        && JeproLabSettingsModel.updateGlobalValue('PS_PACK_FEATURE_ACTIVE', '1');
    }

    public static function duplicate($id_product_old, $id_product_new)
    {
        Db::getInstance()->execute('INSERT INTO `'._DB_PREFIX_.'pack` (`id_product_pack`, `id_product_item`, `id_product_attribute_item`, `quantity`)
        (SELECT '.(int)$id_product_new.', `id_product_item`, `id_product_attribute_item`, `quantity` FROM `'._DB_PREFIX_.'pack` WHERE `id_product_pack` = '.(int)$id_product_old.')');

        // If return query result, a non-pack product will return false
        return true;
    }

    /**
     * This method is allow to know if a feature is used or active
     * @since 1.5.0.1
     * @return bool
     */
    public static boolean isFeaturePublished(){
        return JeproLabSettingModel.getIntValue("pack_feature_active") > 0;
    }

    /*
     * This method is allow to know if a Pack entity is currently used
     * @since 1.5.0
     * @param $table
     * @param $has_active_column
     * @return bool
     * /
    public static function isCurrentlyUsed($table = null, $has_active_column = false)
    {
        // We dont't use the parent method because the identifier isn't id_pack
        return (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT `id_product_pack`
            FROM `'._DB_PREFIX_.'pack`
        ');
    }

    /**
     * For a given pack, tells if it has at least one product using the advanced stock management
     *
     * @param int $id_product id_pack
     * @return bool
     * /
    public static function usesAdvancedStockManagement($id_product)
    {
        if (!JeproLabAnalyzePackModel.isPack($id_product)) {
        return false;
    }

        $products = JeproLabAnalyzePackModel.getItems($id_product, JeproLabSettingsModel.get('PS_LANG_DEFAULT'));
        foreach ($products as $product) {
        // if one product uses the advanced stock management
        if ($product->advanced_stock_management == 1) {
            return true;
        }
    }
        // not used
        return false;
    }

    /**
     * For a given pack, tells if all products using the advanced stock management
     *
     * @param int $id_product id_pack
     * @return bool
     * /
    public static function allUsesAdvancedStockManagement($id_product)
    {
        if (!JeproLabAnalyzePackModel.isPack($id_product)) {
        return false;
    }

        $products = JeproLabAnalyzePackModel.getItems($id_product, JeproLabSettingsModel.get('PS_LANG_DEFAULT'));
        foreach ($products as $product) {
        // if one product uses the advanced stock management
        if ($product->advanced_stock_management == 0) {
            return false;
        }
    }
        // not used
        return true;
    }

    /**
     * Returns Packs that conatins the given product in the right declinaison.
     *
     * @param integer $id_item Product item id that could be contained in a|many pack(s)
     * @param integer $id_attribute_item The declinaison of the product
     * @param integer $id_lang
     * @return array[Product] Packs that contains the given product
     * /
    public static function getPacksContainingItem($id_item, $id_attribute_item, $id_lang)
    {
        if (!JeproLabAnalyzePackModel.isFeatureActive() || !$id_item) {
        return array();
    }

        $query = 'SELECT `id_product_pack`, `quantity` FROM `'._DB_PREFIX_.'pack`
        WHERE `id_product_item` = '.((int)$id_item);
        if (Combination::isFeatureActive()) {
        $query .= ' AND `id_product_attribute_item` = '.((int)$id_attribute_item);
    }
        $result = Db::getInstance()->executeS($query);
        $array_result = array();
        foreach ($result as $row) {
        $p = new Product($row['id_product_pack'], true, $id_lang);
        $p->loadStockData();
        $p->pack_item_quantity = $row['quantity']; // Specific need from StockAvailable::updateQuantity()
        $array_result[] = $p;
    }
        return $array_result;
    }*/
}