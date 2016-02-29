package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 21/02/2014.
 */
public class JeproLabWarehouseModel extends  JeproLabModel{
    /** @var int identifier of the warehouse */
    public int warehouse_id;

    /** @var int Id of the address associated to the warehouse */
    public int address_id;

    /** @var string Reference of the warehouse */
    public String reference;

    /** @var string Name of the warehouse */
    public String name;

    /** @var int Id of the employee who manages the warehouse */
    public int employee_id;

    /** @var int Id of the valuation currency of the warehouse */
    public int currency_id;

    /** @var bool True if warehouse has been deleted (hence, no deletion in DB) */
    public boolean deleted = false;

    /**
     * Describes the way a Warehouse is managed
     *
     * @var string enum WA|LIFO|FIFO
     */
    public String management_type;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'warehouse',
                    'primary' => 'id_warehouse',
                    'fields' => array(
                    'id_address' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'reference' =>            array('type' => self::TYPE_STRING, 'validate' => 'isString', 'required' => true, 'size' => 45),
    'name' =>                array('type' => self::TYPE_STRING, 'validate' => 'isString', 'required' => true, 'size' => 45),
    'id_employee' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'management_type' =>    array('type' => self::TYPE_STRING, 'validate' => 'isStockManagement', 'required' => true),
    'id_currency' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'deleted' =>            array('type' => self::TYPE_BOOL),
    ),
            );

    /**
     * @see ObjectModel::$webserviceParameters
     * /
    protected $webserviceParameters = array(
            'fields' => array(
            'id_address' => array('xlink_resource' => 'addresses'),
    'id_employee' => array('xlink_resource' => 'employees'),
    'id_currency' => array('xlink_resource' => 'currencies'),
    'valuation' => array('getter' => 'getWsStockValue', 'setter' => false),
    'deleted' => array(),
    ),
            'associations' => array(
            'stocks' => array(
            'resource' => 'stock',
            'fields' => array(
                    'id' => array(),
    ),
            ),
            'carriers' => array(
            'resource' => 'carrier',
                    'fields' => array(
                    'id' => array(),
    ),
            ),
            'shops' => array(
            'resource' => 'shop',
                    'fields' => array(
                    'id' => array(),
    'name' => array(),
    ),
            ),
            ),
            );

    /**
     * Gets the shops associated to the current warehouse
     *
     * @return array Shops (id, name)
     * /
    public function getShops()
    {
        $query = new DbQuery();
        $query->select('ws.id_shop, s.name');
        $query->from('warehouse_shop', 'ws');
        $query->leftJoin('shop', 's', 's.id_shop = ws.id_shop');
        $query->where($this->def['primary'].' = '.(int)$this->id);

        $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
        return $res;
    }

    /**
     * Gets the carriers associated to the current warehouse
     *
     * @return array Ids of the associated carriers
     * /
    public function getCarriers($return_reference = false)
    {
        $ids_carrier = array();

        $query = new DbQuery();
        if ($return_reference) {
            $query->select('wc.id_carrier');
        } else {
            $query->select('c.id_carrier');
        }
        $query->from('warehouse_carrier', 'wc');
        $query->innerJoin('carrier', 'c', 'c.id_reference = wc.id_carrier');
        $query->where($this->def['primary'].' = '.(int)$this->id);
        $query->where('c.deleted = 0');
        $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);

        if (!is_array($res)) {
            return $ids_carrier;
        }

        foreach ($res as $carriers) {
        foreach ($carriers as $carrier) {
            $ids_carrier[$carrier] = $carrier;
        }
    }

        return $ids_carrier;
    }

    /**
     * Sets the carriers associated to the current warehouse
     *
     * @param array $ids_carriers
     * /
    public function setCarriers($ids_carriers)
    {
        if (!is_array($ids_carriers)) {
            $ids_carriers = array();
        }

        $row_to_insert = array();
        foreach ($ids_carriers as $id_carrier) {
        $row_to_insert[] = array($this->def['primary'] => $this->id, 'id_carrier' => (int)$id_carrier);
    }

        Db::getInstance()->execute('
            DELETE FROM '._DB_PREFIX_.'warehouse_carrier
        WHERE '.$this->def['primary'].' = '.(int)$this->id);

        if ($row_to_insert) {
            Db::getInstance()->insert('warehouse_carrier', $row_to_insert);
        }
    }

    /**
     * For a given carrier, removes it from the warehouse/carrier association
     * If $id_warehouse is set, it only removes the carrier for this warehouse
     *
     * @param int $id_carrier Id of the carrier to remove
     * @param int $id_warehouse optional Id of the warehouse to filter
     * /
    public static function removeCarrier($id_carrier, $id_warehouse = null)
    {
        Db::getInstance()->execute('
            DELETE FROM '._DB_PREFIX_.'warehouse_carrier
        WHERE id_carrier = '.(int)$id_carrier.
        ($id_warehouse ? ' AND id_warehouse = '.(int)$id_warehouse : ''));
    }

    /**
     * Checks if a warehouse is empty - i.e. has no stock
     *
     * @return bool
     * /
    public function isEmpty()
    {
        $query = new DbQuery();
        $query->select('SUM(s.physical_quantity)');
        $query->from('stock', 's');
        $query->where($this->def['primary'].' = '.(int)$this->id);
        return (Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query) == 0);
    }

    /**
     * Checks if the given warehouse exists
     *
     * @param int $id_warehouse
     * @return bool Exists/Does not exist
     * /
    public static function exists($id_warehouse)
    {
        $query = new DbQuery();
        $query->select('id_warehouse');
        $query->from('warehouse');
        $query->where('id_warehouse = '.(int)$id_warehouse);
        $query->where('deleted = 0');
        return (Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query));
    }

    /**
     * For a given {product, product attribute} sets its location in the given warehouse
     * First, for the given parameters, it cleans the database before updating
     *
     * @param int $id_product ID of the product
     * @param int $id_product_attribute Use 0 if this product does not have attributes
     * @param int $id_warehouse ID of the warehouse
     * @param string $location Describes the location (no lang id required)
     * @return bool Success/Failure
     * /
    public static function setProductLocation($id_product, $id_product_attribute, $id_warehouse, $location)
    {
        Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'warehouse_product_location`
        WHERE `id_product` = '.(int)$id_product.'
        AND `id_product_attribute` = '.(int)$id_product_attribute.'
        AND `id_warehouse` = '.(int)$id_warehouse);

        $row_to_insert = array(
                'id_product' => (int)$id_product,
                'id_product_attribute' => (int)$id_product_attribute,
            'id_warehouse' => (int)$id_warehouse,
            'location' => pSQL($location),
        );

        return Db::getInstance()->insert('warehouse_product_location', $row_to_insert);
    }

    /**
     * Resets all product locations for this warehouse
     * /
    public function resetProductsLocations()
    {
        Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'warehouse_product_location`
        WHERE `id_warehouse` = '.(int)$this->id);
    }

    /**
     * For a given {product, product attribute} gets its location in the given warehouse
     *
     * @param int $id_product ID of the product
     * @param int $id_product_attribute Use 0 if this product does not have attributes
     * @param int $id_warehouse ID of the warehouse
     * @return string Location of the product
     * /
    public static function getProductLocation($id_product, $id_product_attribute, $id_warehouse)
    {
        $query = new DbQuery();
        $query->select('location');
        $query->from('warehouse_product_location');
        $query->where('id_warehouse = '.(int)$id_warehouse);
        $query->where('id_product = '.(int)$id_product);
        $query->where('id_product_attribute = '.(int)$id_product_attribute);

        return (Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query));
    } **/

    public static List getAnalyzeWarehouseList(int analyzeId){
        return getAnalyzeWarehouseList(analyzeId, 0, 0);
    }

    public static List getAnalyzeWarehouseList(int analyzeId, int analyzeAttributeId){
        return getAnalyzeWarehouseList(analyzeId, analyzeAttributeId, 0);
    }

    /**
     * For a given {product, product attribute} gets warehouse list
     *
     * @param analyzeId ID of the analyze id
     * @param analyzeAttributeId Optional, uses 0 if this product does not have attributes
     * @param labId Optional, ID of the shop. Uses the context shop id (@see Context::shop)
     * @return array Warehouses (ID, reference/name concatenated)
     */
    public static List getAnalyzeWarehouseList(int analyzeId, int analyzeAttributeId, int labId){
        // if it's a pack, returns warehouses if and only if some products use the advanced stock management
        boolean shareStock = false;
        JeproLabLaboratoryGroupModel labGroup;
        if (labId <= 0) {
            if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT){
                labGroup = JeproLabLaboratoryModel.getContextLaboratoryGroup();
            } else {
                labGroup = JeproLabContext.getContext().laboratory.getLaboratoryGroup();
                labId = JeproLabContext.getContext().laboratory.laboratory_id;
            }
            shareStock = labGroup.share_stocks;
        } else {
            labGroup = JeproLabLaboratoryModel.getLaboratoryGroupFromLaboratory(labId);
            shareStock = labGroup.share_stocks;
        }
        List<Integer> labIds;
        if (shareStock) {
            labIds = JeproLabLaboratoryModel.getLaboratoryIds(true, labGroup.laboratory_group_id);
        } else {
            labIds = new ArrayList<>();
            labIds.add(labId);
        }
        List warehouseList = new ArrayList<>();

        String query = "SELECT warehouse_analyze_location." + staticDataBaseObject.quoteName("warehouse_id") + ", CONCAT(warehouse.reference,";
        query += " '-', warehouse.name) as name FROM " + staticDataBaseObject.quoteName("#__jeprolab_warehouse_analyze_location") + " AS ";
        query += "warehouse_analyze_location INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_warehouse_lab") + " AS warehouse_lab ";
        query += " 0N (warehouse_lab.warehouse_id = warehouse_analyze_location.warehouse_id AND lab_id IN (" + labIds.toString() + ") ";
        query += " INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_warehouse") + " AS warehouse ON (warehouse.warehouse_id = ";
        query += "warehouse.warehouse_id WHERE analyze_id = " + analyzeId + " AND analyze_attribute_id = " + analyzeAttributeId + " AND ";
        query += "warehouse.deleted = 0 GROUP BY warehouse_analyze_location";

        staticDataBaseObject.setQuery(query);
        ResultSet warehouseSet = staticDataBaseObject.loadObject();


        return warehouseList;
    }

    public static ResultSet getWarehouses(){
        return getWarehouses(false, 0);
    }

    public static ResultSet getWarehouses(boolean ignoreLab){
        return getWarehouses(ignoreLab, 0);
    }

    /**
     * Gets available warehouses
     * It is possible via ignore_shop and id_shop to filter the list with shop id
     *
     * @param ignoreLab Optional, false by default - Allows to get only the warehouses that are associated to one/some shops (@see $id_shop)
     * @param labId Optional, Context::shop::Id by default - Allows to define a specific shop to filter.
     * @return array Warehouses (ID, reference/name concatenated)
     */
    public static ResultSet getWarehouses(boolean ignoreLab, int labId){
        if (!ignoreLab) {
            if (labId <= 0) {
                labId = JeproLabContext.getContext().laboratory.laboratory_id;
            }
        }

        String query = "SELECT warehouse.warehouse_id, CONCAT(reference, ' - ', name) as name FROM " + staticDataBaseObject.quoteName("#__jeprolab_warehouse") + " AS warehouse ";
        if (!ignoreLab) {
            query += " INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_warehouse_lab") + " AS warehouse_lab ON(warehouse_lab.";
            query += staticDataBaseObject.quoteName("warehouse_id") + " = warehouse." + staticDataBaseObject.quoteName("warehouse_id");
            query += " AND warehouse_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId;
        }
        query += " WHERE deleted = 0 ORDER BY reference ASC ";


        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    /**
     * Gets warehouses grouped by shops
     *
     * @return array (of array) Warehouses ID are grouped by shops ID
     */
    public static Map<Integer, List<Integer>> getWarehousesGroupedByLaboratories(){
        Map<Integer, List<Integer>> warehouseIds = new HashMap<>();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT warehouse_id, lab_id " + staticDataBaseObject.quoteName("#__jeprolab_warehouse_lab") + " ORDER BY lab_id";

        staticDataBaseObject.setQuery(query);
        ResultSet warehouseSet = staticDataBaseObject.loadObject();
        // queries to get warehouse ids grouped by shops
        if(warehouseSet != null) {
            try {
                int key;
                List<Integer> tempList;
                while(warehouseSet.next()){
                    key = warehouseSet.getInt("lab_id");
                    if(warehouseIds.containsKey(key)){
                        warehouseIds.get(key).add(warehouseSet.getInt("warehouse_id"));
                    }else{
                        tempList = new ArrayList<>();
                        tempList.add(warehouseSet.getInt("warehouse_id"));
                        warehouseIds.put(key, tempList);
                    }
                }
            }catch(SQLException ignored){

            }
        }

        return warehouseIds;
    }

    /**
     * Gets the number of products in the current warehouse
     *
     * @return int Number of different id_stock
     * /
    public function getNumberOfProducts()
    {
        $query = '
        SELECT COUNT(t.id_stock)
            FROM
                    (
                            SELECT s.id_stock
                            FROM '._DB_PREFIX_.'stock s
        WHERE s.id_warehouse = '.(int)$this->id.'
        GROUP BY s.id_product, s.id_product_attribute
        ) as t';

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
    }

    /**
     * Gets the number of quantities - for all products - in the current warehouse
     *
     * @return int Total Quantity
     * /
    public function getQuantitiesOfProducts()
    {
        $query = '
        SELECT SUM(s.physical_quantity)
            FROM '._DB_PREFIX_.'stock s
        WHERE s.id_warehouse = '.(int)$this->id;

        $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);

        return ($res ? $res : 0);
    }

    /**
     * Gets the value of the stock in the current warehouse
     *
     * @return int Value of the stock
     * /
    public function getStockValue()
    {
        $query = new DbQuery();
        $query->select('SUM(s.`price_te` * s.`physical_quantity`)');
        $query->from('stock', 's');
        $query->where('s.`id_warehouse` = '.(int)$this->id);

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
    }

    /**
     * For a given employee, gets the warehouse(s) he/she manages
     *
     * @param int $id_employee Manager ID
     * @return array ids_warehouse Ids of the warehouses
     * /
    public static function getWarehousesByEmployee($id_employee)
    {
        $query = new DbQuery();
        $query->select('w.id_warehouse');
        $query->from('warehouse', 'w');
        $query->where('w.id_employee = '.(int)$id_employee);

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
    }

    /**
     * For a given product, returns the warehouses it is stored in
     *
     * @param int $id_product Product Id
     * @param int $id_product_attribute Optional, Product Attribute Id - 0 by default (no attribues)
     * @return array Warehouses Ids and names
     * /
    public static function getWarehousesByAnalyzeId($id_product, $id_product_attribute = 0)
    {
        if (!$id_product && !$id_product_attribute) {
            return array();
        }

        $query = new DbQuery();
        $query->select('DISTINCT w.id_warehouse, CONCAT(w.reference, " - ", w.name) as name');
        $query->from('warehouse', 'w');
        $query->leftJoin('warehouse_product_location', 'wpl', 'wpl.id_warehouse = w.id_warehouse');
        if ($id_product) {
            $query->where('wpl.id_product = '.(int)$id_product);
        }
        if ($id_product_attribute) {
            $query->where('wpl.id_product_attribute = '.(int)$id_product_attribute);
        }
        $query->orderBy('w.reference ASC');

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
    }

    /**
     * For a given $id_warehouse, returns its name
     *
     * @param int $id_warehouse Warehouse Id
     * @return string Name
     * /
    public static function getWarehouseNameById($id_warehouse)
    {
        $query = new DbQuery();
        $query->select('name');
        $query->from('warehouse');
        $query->where('id_warehouse = '.(int)$id_warehouse);
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
    }

    /**
     * For a given pack, returns the warehouse it can be shipped from
     *
     * @param int $id_product
     * @return array|bool id_warehouse or false
     * /
    public static function getPackWarehouses($id_product, $id_shop = null)
    {
        if (!Pack::isPack($id_product)) {
        return false;
    }

        if (is_null($id_shop)) {
            $id_shop = Context::getContext()->shop->id;
        }

        // warehouses of the pack
        $pack_warehouses = WarehouseProductLocation::getCollection((int)$id_product);
        // products in the pack
        $products = Pack::getItems((int)$id_product, Configuration::get('PS_LANG_DEFAULT'));

        // array with all warehouses id to check
        $list = array();

        // fills $list
        foreach ($pack_warehouses as $pack_warehouse) {
        /** @var WarehouseProductLocation $pack_warehouse * /
        $list['pack_warehouses'][] = (int)$pack_warehouse->id_warehouse;
    }

        // for each products in the pack
        foreach ($products as $product) {
        if ($product->advanced_stock_management) {
            // gets the warehouses of one product
            $product_warehouses = Warehouse::getProductWarehouseList((int)$product->id, (int)$product->cache_default_attribute, (int)$id_shop);
            $list[(int)$product->id] = array();
            // fills array with warehouses for this product
            foreach ($product_warehouses as $product_warehouse) {
                $list[(int)$product->id][] = $product_warehouse['id_warehouse'];
            }
        }
    }

        $res = false;
        // returns final list
        if (count($list) > 1) {
            $res = call_user_func_array('array_intersect', $list);
        }
        return $res;
    }

    public function resetStockAvailable()
    {
        $products = WarehouseProductLocation::getProducts((int)$this->id);
        foreach ($products as $product) {
        StockAvailable::synchronize((int)$product['id_product']);
    }
    }

    /*********************************\
     *
     * Web services Specific Methods
     *
     ********************************* /

    /**
     * Webservice : gets the value of the warehouse
     * @return int
     * /
    public function getWsStockValue()
    {
        return $this->getStockValue();
    }

    /**
     * Webservice : gets the ids stock associated to this warehouse
     * @return array
     * /
    public function getWsStocks()
    {
        $query = new DbQuery();
        $query->select('s.id_stock as id');
        $query->from('stock', 's');
        $query->where('s.id_warehouse ='.(int)$this->id);

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
    }

    /**
     * Webservice : gets the ids shops associated to this warehouse
     * @return array
     * /
    public function getWsShops()
    {
        $query = new DbQuery();
        $query->select('ws.id_shop as id, s.name');
        $query->from('warehouse_shop', 'ws');
        $query->leftJoin('shop', 's', 's.id_shop = ws.id_shop');
        $query->where($this->def['primary'].' = '.(int)$this->id);

        $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
        return $res;
    }

    /**
     * Webservice : gets the ids carriers associated to this warehouse
     * @return array
     * /
    public function getWsCarriers()
    {
        $ids_carrier = array();

        $query = new DbQuery();
        $query->select('wc.id_carrier as id');
        $query->from('warehouse_carrier', 'wc');
        $query->where($this->def['primary'].' = '.(int)$this->id);

        $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);

        if (!is_array($res)) {
            return $ids_carrier;
        }

        foreach ($res as $carriers) {
        foreach ($carriers as $carrier) {
            $ids_carrier[] = $carrier;
        }
    }

        return $ids_carrier;
    }*/
}