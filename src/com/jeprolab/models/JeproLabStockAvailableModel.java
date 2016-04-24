package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabStockAvailableModel extends JeproLabModel {
    /** @var int identifier of the current product */
    public int analyze_id;

    /** @var int identifier of product attribute if necessary */
    public int analyze_attribute_id;

    /** @var int the shop associated to the current product and corresponding quantity */
    public int laboratory_id;

    /** @var int the group shop associated to the current product and corresponding quantity */
    public int laboatory_group_id;

    /** @var int the quantity available for sale */
    public int quantity = 0;

    /** @var bool determine if the available stock value depends on physical stock */
    public boolean depends_on_stock = false;

    /** @var bool determine if a product is out of stock - it was previously in Product class */
    public boolean out_of_stock = false;

    public static String addSqlLaboratoryRestriction(){
        return addSqlLaboratoryRestriction(null, "");
    }

    public static String addSqlLaboratoryRestriction(JeproLabLaboratoryModel lab){
        return addSqlLaboratoryRestriction(lab, "");
    }

    /**
     * Add an sql restriction for shops fields - specific to StockAvailable
     *
     * @param lab Optional : The shop ID
     * @param alias Optional : The current table alias
     *
     * @return string|DbQuery DbQuery object or the sql restriction string
     */
    public static String addSqlLaboratoryRestriction(JeproLabLaboratoryModel lab, String alias){
        JeproLabContext context = JeproLabContext.getContext();

        if (!alias.equals("")) {
            alias += ".";
        }
        JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel labGroup;

        // if there is no $id_shop, gets the context one
        // get shop group too
        if (lab == null || lab.laboratory_id == context.laboratory.laboratory_id) {
            if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT) {
                labGroup = JeproLabLaboratoryModel.getContextLaboratoryGroup();
            } else {
                labGroup = context.laboratory.getLaboratoryGroup();
            }
            lab = context.laboratory;
        } else if(lab.laboratory_id != context.laboratory.laboratory_id){
            /** @var Shop $shop */
            labGroup = lab.getLaboratoryGroup();
        } else {
            //lab = new JeproLabLaboratoryModel($shop);
            labGroup = lab.getLaboratoryGroup();
        }
        String query;

        // if quantities are shared between shops of the group
        if (labGroup.share_stocks) {
            query = " AND " + alias + "lab_group_id = " + labGroup.laboratory_group_id + " AND " + alias + "lab_id = 0 ";

        } else {
            query = " AND " + alias + "lab_id = " + lab.laboratory_id + " AND " + alias + "lab_group_id = 0 " ;
        }

        return query;
    }

    public static void setAnalyzeDependsOnStock(int analyzeId){
        setAnalyzeDependsOnStock(analyzeId, true, 0, 0);
    }

    public static void setAnalyzeDependsOnStock(int analyzeId, boolean dependsOnStock){
        setAnalyzeDependsOnStock(analyzeId, dependsOnStock, 0, 0);
    }

    public static void setAnalyzeDependsOnStock(int analyzeId, boolean dependsOnStock, int labId){
        setAnalyzeDependsOnStock(analyzeId, dependsOnStock, labId, 0);
    }

    /**
     * For a given analyzeId, sets if stock available depends on stock
     *
     * @param analyzeId analyze id
     * @param dependsOnStock Optional : true by default
     * @param labId Optional : gets context by default
     */
    public static void setAnalyzeDependsOnStock(int analyzeId, boolean dependsOnStock, int labId, int analyzeAttributeId){
        if (analyzeId > 0) {
            int existingId = JeproLabStockAvailableModel.getStockAvailableIdByAnalyzeId(analyzeId, analyzeAttributeId, labId);
            if (existingId > 0) {
                String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " SET ";
                query += staticDataBaseObject.quoteName("depends_on_stock") + " = " + (dependsOnStock ? 1 : 0);
                query += " WHERE " + staticDataBaseObject.quoteName("stock_available_id") + " = " + existingId;

                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            } else {
                Map<String, Integer> queryParams = new HashMap<>();
                queryParams.put("depends_on_stock", (dependsOnStock ? 1 : 0));
                queryParams.put("analyze_id", analyzeId);
                queryParams.put("analyze_attribute_id",analyzeAttributeId );

                queryParams = JeproLabStockAvailableModel.addSqlLaboratoryParams(queryParams, labId);
                String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + "(";
                Iterator queryIterator = queryParams.entrySet().iterator();
                String keyFields = "";
                String valueFields = "";
                while(queryIterator.hasNext()){
                    Map.Entry field = (Map.Entry)queryIterator.next();
                    keyFields += staticDataBaseObject.quote(field.getKey().toString()) +  ", ";
                    valueFields += field.getValue().toString() + ", ";
                }
                keyFields = keyFields.endsWith(", ") ? keyFields.substring(0, keyFields.length() - 3) : keyFields;
                valueFields = valueFields.endsWith(", ") ? valueFields.substring(0, valueFields.length() - 3) : valueFields;
                query += keyFields + ") VALUES( " + valueFields + ")";
                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            }

            // depends on stock.. hence synchronizes
            if (dependsOnStock) {
                JeproLabStockAvailableModel.synchronize(analyzeId);
            }
        }
    }

    public static int getStockAvailableIdByAnalyzeId(int analyzeId) {
        return getStockAvailableIdByAnalyzeId(analyzeId, 0, 0);
    }

    public static int getStockAvailableIdByAnalyzeId(int analyzeId, int analyzeAttributeId) {
        return getStockAvailableIdByAnalyzeId(analyzeId, analyzeAttributeId, 0);
    }

    public static int getStockAvailableIdByAnalyzeId(int analyzeId, int analyzeAttributeId, int labId) {
        if (analyzeId > 0) {
            String query = "SELECT " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " WHERE analyze_id = " + analyzeId;
            if(analyzeAttributeId > 0){
                query += " AND analyze_attribute_id = " + analyzeAttributeId;
            }


            query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(new JeproLabLaboratoryModel(labId));
            staticDataBaseObject.setQuery(query);
            return (int)staticDataBaseObject.loadValue("stock_available_id");
        }
        return 0;
    }

    public static void synchronize(int analyzeId) {
        synchronize(analyzeId, 0);
    }

    /*
     * For a given id_product, synchronizes StockAvailable::quantity with Stock::usable_quantity
     *
     * @param int analyzeId
     */
    public static void synchronize(int analyzeId, int orderLabId) {
        if (analyzeId > 0) {
            //if analyze is pack recursive sync  analyze in pack
            if (JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId)){
                JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId);
                if (analyze.analyze_id > 0){
                    if (analyze.pack_stock_type == 1 || analyze.pack_stock_type == 2 || (analyze.pack_stock_type == 3 && JeproLabSettingModel.getIntValue("pack_stock_type") > 0)){
                        List<JeproLabAnalyzeModel> analyzePacks = JeproLabAnalyzeModel.JeproLabAnalyzePackModel.getItems(analyzeId, JeproLabSettingModel.getIntValue("default_lang"));
                        for(JeproLabAnalyzeModel analyzePack : analyzePacks) {
                            JeproLabStockAvailableModel.synchronize(analyzePack.analyze_id, orderLabId);
                        }
                    }
                }/*else{
                    return false;
                } */
            }

            /*
            // gets warehouse ids grouped by shops
            Map<Integer, List<Integer>> warehouseIds = JeproLabWarehouseModel.getWarehousesGroupedByLaboratories();
            List<Integer> orderWarehouses = new ArrayList<>();
            if (orderLabId > 0) {
                ResultSet warehouses = JeproLabWarehouseModel.getWarehouses(false, orderLabId);
                if(warehouses != null) {
                    try {
                        while(warehouses.next()) {
                            orderWarehouses.add(warehouses.getInt("warehouse_id"));
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
                }
            }

            //todo gets all analyze attributes ids
            /*List<Integer> analyzeAttributeIds = new JeproLabAnalyzeModel.getAnalyzeAttributesIds(analyzeId);
            /*foreach( as analyzeId_attribute){
                analyzeAttributeIds.add(analyzeId_attribute['id_product_attribute']);
            }* /

            // Allow the order when the analyze is out of stock?
            boolean outOfStock = JeproLabStockAvailableModel.outOfStock(analyzeId);

            JeproLabStockManager manager = JeproLabStockManagerFactory.getManager();
            String query;

            // loops on warehouseIds to synchronize quantities
            foreach($ids_warehouse as $id_shop = > $warehouses){
                // first, checks if the product depends on stock for the given shop $id_shop
                if (JeproLabStockAvailableModel.dependsOnStock(analyzeId, labId)){
                    // init quantity
                    $product_quantity = 0;

                    // if it's a simple product
                    if (analyzeAttributeIds.isEmpty()){
                        $allowedWarehouseForAnalyze = JeproLabWarehouseModel.getAnalyzeWarehouseList(analyzeId, 0, labId);
                        List allowedWarehouseForAnalyzeClean = new ArrayList<>();
                        foreach($allowed_warehouse_for_product as $warehouse) {
                            $allowed_warehouse_for_product_clean[]=(int) $warehouse['id_warehouse'];
                        }
                        allowedWarehouseForAnalyzeClean = array_intersect($allowed_warehouse_for_product_clean, $warehouses);
                        if ($order_id_shop != null && !count(array_intersect($allowed_warehouse_for_product_clean, $order_warehouses))) {
                            continue;
                        }

                        $product_quantity = manager.getAnalyzeRealQuantities(analyzeId, null, allowedWarehouseForAnalyzeClean, true);

                        Hook::exec ('actionUpdateQuantity',
                                array(
                                        'id_product' = > analyzeId,
                                'id_product_attribute' =>0,
                                'quantity' =>$product_quantity,
                                'id_shop' =>$id_shop
                        )
                        );
                    }else {
                        // else this product has attributes, hence loops on $ids_product_attribute
                        foreach($ids_product_attribute as analyzeId_attribute) {
                            $allowed_warehouse_for_combination = WareHouse::getProductWarehouseList
                            ((int) analyzeId, (int) analyzeId_attribute, (int) $id_shop);
                            $allowed_warehouse_for_combination_clean = array();
                            foreach($allowed_warehouse_for_combination as $warehouse) {
                                $allowed_warehouse_for_combination_clean[]=(int) $warehouse['id_warehouse'];
                            }
                            $allowed_warehouse_for_combination_clean = array_intersect($allowed_warehouse_for_combination_clean, $warehouses);
                            if ($order_id_shop != null && !count(array_intersect($allowed_warehouse_for_combination_clean, $order_warehouses))) {
                                continue;
                            }

                            int quantity = manager.getAnalyzeRealQuantities(analyzeId, analyzeAttributeIds, allowedWarehouseForCombinationClean, true);

                            query = "SELECT COUNT(*) stock FROM "  + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
                            query += " = " + analyzeId + " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;
                            query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(new JeproLabLaboratoryModel(labId));

                            staticDataBaseObject.setQuery(query);
                            int stock = (int)staticDataBaseObject.loadValue("stock");

                            if(stock > 0){
                                $query = array(
                                        'table' = > 'stock_available',
                                        'data' =>array('quantity' = > $quantity),
                                'where' =>'id_product = '. (int) analyzeId. ' AND id_product_attribute = '.
                                (int) $id_product_attribute.
                                        StockAvailable::addSqlShopRestriction (null, $id_shop)
                                );
                                Db::getInstance () -> update($query['table'], $query['data'], $query['where']);
                            }else{
                                $query = array(
                                        'table' = > 'stock_available',
                                        'data' =>array(
                                        'quantity' = > $quantity,
                                        'depends_on_stock' =>1,
                                        'out_of_stock' =>$out_of_stock,
                                        'id_product' =>(int) analyzeId,
                                        'id_product_attribute' =>(int) $id_product_attribute,
                                )
                                );
                                Map<String, Integer> queryParams = new HashMap<>();
                                queryParams.put("quantity", quantity);
                                queryParams.put("depends_on_stock", 1);
                                queryParams.put("out_of_stock", outOfStock);
                                queryParams.put("analyze_id", analyzeId);
                                queryParams.put("analyze_attribute_id", analyzeAttributeId);

                                JeproLabStockAvailableModel.addSqlLaboratoryParams($query['data'], $id_shop);
                                Db::getInstance () -> insert($query['table'], $query['data']);
                            }

                            $product_quantity += $quantity;

                            Hook::exec ('actionUpdateQuantity',
                                    array(
                                            'id_product' = > analyzeId,
                                    'id_product_attribute' =>$id_product_attribute,
                                    'quantity' =>$quantity,
                                    'id_shop' =>$id_shop
                            )
                            );
                        }
                    }
                    // updates
                    // if $id_product has attributes, it also updates the sum for all attributes
                    if (($order_id_shop != null && array_intersect($warehouses, $order_warehouses)) || $order_id_shop == null) {
                        $query = array(
                                'table' = > 'stock_available',
                                'data' =>array('quantity' = > $product_quantity),
                        'where' =>'id_product = '. (int) $id_product. ' AND id_product_attribute = 0'.
                                StockAvailable::addSqlShopRestriction (null, $id_shop)
                        );
                        Db::getInstance () -> update($query['table'], $query['data'], $query['where']);
                    }
                }
            }
            // In case there are no warehouses, removes analyze from StockAvailable
            if (warehouseIds.size() == 0 && JeproLabStockAvailableModel.dependsOnStock (analyzeId)){
                query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " SET " + staticDataBaseObject.quoteName("quantity") ;
                query += " => 0 WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            } */

            JeproLabCache.getInstance().remove("jeprolab_stock_available_get_quantity_available_by_analyze_" + analyzeId + "*" );
        }
    }

    public static Map<String, Integer> addSqlLaboratoryParams(Map<String, Integer> queryParams){
        return addSqlLaboratoryParams(queryParams, 0);
    }

    /**
     * Add sql params for shops fields - specific to StockAvailable
     *
     * @param queryParams Reference to the params array
     * @param labId Optional : The shop ID
     *
     */
    public static Map<String, Integer> addSqlLaboratoryParams(Map<String, Integer> queryParams, int labId){
        JeproLabContext context = JeproLabContext.getContext();
        boolean groupOk = false;
        JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel labGroup;
        // if there is no $id_shop, gets the context one
        // get shop group too
        if (labId <= 0) {

            if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT) {
                labGroup = JeproLabLaboratoryModel.getContextLaboratoryGroup();
            } else {
                labGroup = context.laboratory.getLaboratoryGroup();
                labId = context.laboratory.laboratory_id;
            }
        } else {
            JeproLabLaboratoryModel lab = new JeproLabLaboratoryModel(labId);
            labGroup = lab.getLaboratoryGroup();
        }

        // if quantities are shared between shops of the group
        if (labGroup.share_stocks) {
            queryParams.put("lab_group_id", labGroup.laboratory_group_id);
            queryParams.put("lab_id", 0);

            groupOk = true;
        } else {
            queryParams.put("lab_group_id", 0);
        }

        // if no group specific restriction, set simple shop restriction
        if (!groupOk) {
            queryParams.put("lab_id", labId);
        }
        return queryParams;
    }

    public static void setAnalyzeOutOfStock(int analyzeId){
        setAnalyzeOutOfStock(analyzeId, 0, 0, 0);
    }

    public static void setAnalyzeOutOfStock(int analyzeId, int outOfStock){
        setAnalyzeOutOfStock(analyzeId, outOfStock, 0, 0);
    }

    public static void setAnalyzeOutOfStock(int analyzeId, int outOfStock, int labId){
        setAnalyzeOutOfStock(analyzeId, outOfStock, labId, 0);
    }

    /**
     * For a given id_product, sets if product is available out of stocks
     *
     * @param analyzeId analyze id
     * @param outOfStock Optional false by default
     * @param labId Optional gets context by default
     */
    public static void setAnalyzeOutOfStock(int analyzeId, int outOfStock, int labId, int analyzeAttributeId){
        if (analyzeId > 0) {
            int existingId = JeproLabStockAvailableModel.getStockAvailableIdByAnalyzeId(analyzeId, analyzeAttributeId, labId);

            String query;
            if (existingId > 0) {
                query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " SET " + staticDataBaseObject.quoteName("stock_available");
                query += " = " + outOfStock + ", " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
                query += (analyzeAttributeId > 0 ? " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(new JeproLabLaboratoryModel(labId));

                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            } else {
                query = "INSERT " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + "(" ;
                Map<String, Integer> queryParams = new HashMap<>();
                queryParams.put("out_of_stock", outOfStock);
                queryParams.put("analyze_id", analyzeId);
                queryParams.put("analyze_attribute_id", analyzeAttributeId);

                queryParams = JeproLabStockAvailableModel.addSqlLaboratoryParams(queryParams, labId);
                Iterator queryIterator = queryParams.entrySet().iterator();
                String keyFields = "";
                String valueFields = "";
                while(queryIterator.hasNext()){
                    Map.Entry field = (Map.Entry)queryIterator.next();
                    keyFields += staticDataBaseObject.quote(field.getKey().toString()) +  ", ";
                    valueFields += field.getValue().toString() + ", ";
                }
                keyFields = keyFields.endsWith(", ") ? keyFields.substring(0, keyFields.length() - 3) : keyFields;
                valueFields = valueFields.endsWith(", ") ? valueFields.substring(0, valueFields.length() - 3) : valueFields;
                query += keyFields + ") VALUES( " + valueFields + ")"; // ON DUPLICATE KEY UPDATE ";

                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            }
        }
    }

}
