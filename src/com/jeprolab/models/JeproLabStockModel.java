package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabStockModel extends JeproLabModel {

    public static class JeproLabStockAvailableModel extends JeproLabModel {
        /**
         * @var int identifier of the current product
         */
        public int analyze_id;

        /**
         * @var int identifier of product attribute if necessary
         */
        public int analyze_attribute_id;

        /**
         * @var int the shop associated to the current product and corresponding quantity
         */
        public int laboratory_id;

        /**
         * @var int the group shop associated to the current product and corresponding quantity
         */
        public int laboatory_group_id;

        /**
         * @var int the quantity available for sale
         */
        public int quantity = 0;

        /**
         * @var bool determine if the available stock value depends on physical stock
         */
        public boolean depends_on_stock = false;

        /**
         * @var bool determine if a product is out of stock - it was previously in Product class
         */
        public boolean out_of_stock = false;

        public static String addSqlLaboratoryRestriction() {
            return addSqlLaboratoryRestriction(null, "");
        }

        public static String addSqlLaboratoryRestriction(JeproLabLaboratoryModel lab) {
            return addSqlLaboratoryRestriction(lab, "");
        }

        /**
         * Add an sql restriction for shops fields - specific to StockAvailable
         *
         * @param lab   Optional : The shop ID
         * @param alias Optional : The current table alias
         * @return string|DbQuery DbQuery object or the sql restriction string
         */
        public static String addSqlLaboratoryRestriction(JeproLabLaboratoryModel lab, String alias) {
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
            } else if (lab.laboratory_id != context.laboratory.laboratory_id) {
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
                query = " AND " + alias + "lab_id = " + lab.laboratory_id + " AND " + alias + "lab_group_id = 0 ";
            }

            return query;
        }

        public static void setAnalyzeDependsOnStock(int analyzeId) {
            setAnalyzeDependsOnStock(analyzeId, true, 0, 0);
        }

        public static void setAnalyzeDependsOnStock(int analyzeId, boolean dependsOnStock) {
            setAnalyzeDependsOnStock(analyzeId, dependsOnStock, 0, 0);
        }

        public static void setAnalyzeDependsOnStock(int analyzeId, boolean dependsOnStock, int labId) {
            setAnalyzeDependsOnStock(analyzeId, dependsOnStock, labId, 0);
        }

        /**
         * For a given analyzeId, sets if stock available depends on stock
         *
         * @param analyzeId      analyze id
         * @param dependsOnStock Optional : true by default
         * @param labId          Optional : gets context by default
         */
        public static void setAnalyzeDependsOnStock(int analyzeId, boolean dependsOnStock, int labId, int analyzeAttributeId) {
            if (analyzeId > 0) {
                int existingId = JeproLabStockAvailableModel.getStockAvailableIdByAnalyzeId(analyzeId, analyzeAttributeId, labId);
                if (existingId > 0) {
                    String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_stock_available") + " SET ";
                    query += dataBaseObject.quoteName("depends_on_stock") + " = " + (dependsOnStock ? 1 : 0);
                    query += " WHERE " + dataBaseObject.quoteName("stock_available_id") + " = " + existingId;

                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);
                } else {
                    Map<String, Integer> queryParams = new HashMap<>();
                    queryParams.put("depends_on_stock", (dependsOnStock ? 1 : 0));
                    queryParams.put("analyze_id", analyzeId);
                    queryParams.put("analyze_attribute_id", analyzeAttributeId);

                    queryParams = JeproLabStockAvailableModel.addSqlLaboratoryParams(queryParams, labId);
                    String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_stock_available") + "(";
                    Iterator queryIterator = queryParams.entrySet().iterator();
                    String keyFields = "";
                    String valueFields = "";
                    while (queryIterator.hasNext()) {
                        Map.Entry field = (Map.Entry) queryIterator.next();
                        keyFields += dataBaseObject.quote(field.getKey().toString()) + ", ";
                        valueFields += field.getValue().toString() + ", ";
                    }
                    keyFields = keyFields.endsWith(", ") ? keyFields.substring(0, keyFields.length() - 3) : keyFields;
                    valueFields = valueFields.endsWith(", ") ? valueFields.substring(0, valueFields.length() - 3) : valueFields;
                    query += keyFields + ") VALUES( " + valueFields + ")";
                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);
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
                String query = "SELECT " + dataBaseObject.quoteName("#__jeprolab_stock_available") + " WHERE analyze_id = " + analyzeId;
                if (analyzeAttributeId > 0) {
                    query += " AND analyze_attribute_id = " + analyzeAttributeId;
                }


                query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(new JeproLabLaboratoryModel(labId));
                //dataBaseObject.setQuery(query);
                return (int) dataBaseObject.loadValue(query, "stock_available_id");
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
                if (JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId)) {
                    JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId);
                    if (analyze.analyze_id > 0) {
                        if (analyze.pack_stock_type == 1 || analyze.pack_stock_type == 2 || (analyze.pack_stock_type == 3 && JeproLabSettingModel.getIntValue("pack_stock_type") > 0)) {
                            List<JeproLabAnalyzeModel> analyzePacks = JeproLabAnalyzeModel.JeproLabAnalyzePackModel.getItems(analyzeId, JeproLabSettingModel.getIntValue("default_lang"));
                            for (JeproLabAnalyzeModel analyzePack : analyzePacks) {
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
                        JeproLabTools.logExceptionMessage(Level., ignored);
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception e) {
                            JeproLabTools.logExceptionMessage(Level., ignored);
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

                            query = "SELECT COUNT(*) stock FROM "  + dataBaseObject.quoteName("#__jeprolab_stock_available") + " WHERE " + dataBaseObject.quoteName("analyze_id");
                            query += " = " + analyzeId + " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;
                            query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(new JeproLabLaboratoryModel(labId));

                            dataBaseObject.setQuery(query);
                            int stock = (int)dataBaseObject.loadValue("stock");

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
                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_stock_available") + " SET " + dataBaseObject.quoteName("quantity") ;
                query += " => 0 WHERE " + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

                dataBaseObject.setQuery(query);
                dataBaseObject.query(false);
            } */

                JeproLabCache.getInstance().remove("jeprolab_stock_available_get_quantity_available_by_analyze_" + analyzeId + "*");
            }
        }

        public static Map<String, Integer> addSqlLaboratoryParams(Map<String, Integer> queryParams) {
            return addSqlLaboratoryParams(queryParams, 0);
        }

        /**
         * Add sql params for shops fields - specific to StockAvailable
         *
         * @param queryParams Reference to the params array
         * @param labId       Optional : The shop ID
         */
        public static Map<String, Integer> addSqlLaboratoryParams(Map<String, Integer> queryParams, int labId) {
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

        public static boolean removeAnalyzeFromStockAvailable(int analyzeId){
            return removeAnalyzeFromStockAvailable(analyzeId, 0, 0);
        }

        public static boolean removeAnalyzeFromStockAvailable(int analyzeId, int analyzeAttributeId){
            return removeAnalyzeFromStockAvailable(analyzeId, analyzeAttributeId, 0);
        }

        /**
         * Removes a given product from the stock available
         *
         * @param analyzeId
         * @param analyzeAttributeId Optional
         * @param labId or shop object Optional
         *
         * @return bool
         */
        public static boolean removeAnalyzeFromStockAvailable(int analyzeId, int analyzeAttributeId, int labId) {
            /*if (!Validate::isUnsignedId($id_product)) {
        return false;
        }

        if (JeproLabLaboratoryModel.getContext() == SHOP::CONTEXT_SHOP) {
        if (JeproLabLaboratoryModel.getContextShopGroup()->share_stock == 1) {
        $pa_sql = '';
        if ($id_product_attribute !== null) {
        $pa_sql = '_attribute';
        $id_product_attribute_sql = $id_product_attribute;
        } else {
        $id_product_attribute_sql = $id_product;
        }

        if ((int)Db::getInstance()->getValue('SELECT COUNT(*)
        FROM '._DB_PREFIX_.'product'.$pa_sql.'_shop
        WHERE id_product'.$pa_sql.'='.(int)$id_product_attribute_sql.'
        AND id_shop IN ('.implode(',', array_map('intval', JeproLabLaboratoryModel.getContextListShopID(SHOP::SHARE_STOCK))).')')) {
        return true;
        }
        }
        }

        $res = Db::getInstance()->execute('
        DELETE FROM '._DB_PREFIX_.'stock_available
        WHERE id_product = '.(int)$id_product.
        ($id_product_attribute ? ' AND id_product_attribute = '.(int)$id_product_attribute : '').
        StockAvailable::addSqlShopRestriction(null, $shop));

        if ($id_product_attribute) {
        if ($shop === null || !Validate::isLoadedObject($shop)) {
        $shop_datas = array();
        StockAvailable::addSqlShopParams($shop_datas);
        $id_shop = (int)$shop_datas['id_shop'];
        } else {
        $id_shop = (int)$shop->id;
        }

        $stock_available = new StockAvailable();
        $stock_available->id_product = (int)$id_product;
        $stock_available->id_product_attribute = (int)$id_product_attribute;
        $stock_available->id_shop = (int)$id_shop;
        $stock_available->postSave();
        }

        Cache::clean('StockAvailable::getQuantityAvailableByProduct_'.(int)$id_product.'*');
*/
            return true;//$res;
        }

        public static void setAnalyzeOutOfStock(int analyzeId) {
            setAnalyzeOutOfStock(analyzeId, 0, 0, 0);
        }

        public static void setAnalyzeOutOfStock(int analyzeId, int outOfStock) {
            setAnalyzeOutOfStock(analyzeId, outOfStock, 0, 0);
        }

        public static void setAnalyzeOutOfStock(int analyzeId, int outOfStock, int labId) {
            setAnalyzeOutOfStock(analyzeId, outOfStock, labId, 0);
        }

        /**
         * For a given id_product, sets if product is available out of stocks
         *
         * @param analyzeId  analyze id
         * @param outOfStock Optional false by default
         * @param labId      Optional gets context by default
         */
        public static void setAnalyzeOutOfStock(int analyzeId, int outOfStock, int labId, int analyzeAttributeId) {
            if (analyzeId > 0) {
                int existingId = JeproLabStockAvailableModel.getStockAvailableIdByAnalyzeId(analyzeId, analyzeAttributeId, labId);

                String query;
                if (existingId > 0) {
                    query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_stock_available") + " SET " + dataBaseObject.quoteName("stock_available");
                    query += " = " + outOfStock + ", " + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
                    query += (analyzeAttributeId > 0 ? " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                    query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(new JeproLabLaboratoryModel(labId));

                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);
                } else {
                    query = "INSERT " + dataBaseObject.quoteName("#__jeprolab_stock_available") + "(";
                    Map<String, Integer> queryParams = new HashMap<>();
                    queryParams.put("out_of_stock", outOfStock);
                    queryParams.put("analyze_id", analyzeId);
                    queryParams.put("analyze_attribute_id", analyzeAttributeId);

                    queryParams = JeproLabStockAvailableModel.addSqlLaboratoryParams(queryParams, labId);
                    Iterator queryIterator = queryParams.entrySet().iterator();
                    String keyFields = "";
                    String valueFields = "";
                    while (queryIterator.hasNext()) {
                        Map.Entry field = (Map.Entry) queryIterator.next();
                        keyFields += dataBaseObject.quote(field.getKey().toString()) + ", ";
                        valueFields += field.getValue().toString() + ", ";
                    }
                    keyFields = keyFields.endsWith(", ") ? keyFields.substring(0, keyFields.length() - 3) : keyFields;
                    valueFields = valueFields.endsWith(", ") ? valueFields.substring(0, valueFields.length() - 3) : valueFields;
                    query += keyFields + ") VALUES( " + valueFields + ")"; // ON DUPLICATE KEY UPDATE ";

                    //dataBaseObject.setQuery(query);
                    dataBaseObject.query(query, false);
                }
            }
        }




    }

    public static class JeproLabStockManagerFactory{
        /**
         * @var $stock_manager : instance of the current StockManager.
         */
        protected static JeproLabStockManager stock_manager = null;

        /**
         * Returns a StockManager
         *
         * @return StockManagerInterface
         */
        public static JeproLabStockManager getManager() {
            if (JeproLabStockManagerFactory.stock_manager == null){
                JeproLabStockManager stockManager = JeproLabStockManagerFactory.execStockManagerFactory();
                if (stockManager == null){
                    stockManager = new JeproLabStockManager();
                }
                JeproLabStockManagerFactory.stock_manager = stockManager;
            }
            return JeproLabStockManagerFactory.stock_manager;
        }

        /**
         *  Looks for a StockManager in the modules list.
         *
         *  @return StockManagerInterface
         */
        public static JeproLabStockManager execStockManagerFactory(){
            //$modules_infos = Hook::getModulesFromHook(Hook::getIdByName('stockManager'));
            JeproLabStockManager stockManager = null;
            //todo
            /*foreach ($modules_infos as $module_infos) {
                $module_instance = Module::getInstanceByName ($module_infos['name']);

                if (is_callable(array($module_instance, 'hookStockManager'))) {
                    $stock_manager = $module_instance -> hookStockManager();
                }

                if ($stock_manager) {
                    break;
                }
            }*/

            return stockManager;
        }
    }

    /**
     *
     * Created by jeprodev on 21/02/2014.
     */
    public static class JeproLabStockManager implements JeproLabStockManagerInterface{
        public int getAnalyzeRealQuantities(int analyzeId, int analyzeAttributeId) {
            return getAnalyzeRealQuantities(analyzeId, analyzeAttributeId, null, false);
        }

        public int getAnalyzeRealQuantities(int analyzeId, int analyzeAttributeId, List<Integer> warehouseIds) {
            return getAnalyzeRealQuantities(analyzeId, analyzeAttributeId, warehouseIds, false);
        }
        /**
         * @see JeproLabStockManagerInterface::getAnalyzeRealQuantities()
         */
        public int getAnalyzeRealQuantities(int analyzeId, int analyzeAttributeId, List<Integer> warehouseIds, boolean usable) {
            if (warehouseIds == null) {
                // in case warehouseIds is null
                warehouseIds = new ArrayList<>();
                /*if (!is_array($ids_warehouse)) {
                    $ids_warehouse = array($ids_warehouse);
                }

                // casts for security reason
                $ids_warehouse = array_map('intval', $ids_warehouse);*/
            }

            int clientRequestQuantity = 0;

            if (dataBaseObject == null) {
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("analyze_pack_id") + ", " + dataBaseObject.quoteName("quantity") + " FROM ";
            query += dataBaseObject.quoteName("#__jeprolab_pack") + " WHERE " + dataBaseObject.quoteName("analyze_item_id") + " = ";
            query += analyzeId + " AND " + dataBaseObject.quoteName("analyze_attribute_item_id") + " = " + analyzeAttributeId;

            //dataBaseObject.setQuery(query);
            ResultSet packItemsPckSet = dataBaseObject.loadObjectList(query);

            // check if product is present in a pack
            if (!JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId) && packItemsPckSet != null) {
                try {
                    JeproLabAnalyzeModel analyze;
                    while (packItemsPckSet.next()) {  //$in_pack as $value
                        analyze = new JeproLabAnalyzeModel(packItemsPckSet.getInt("analyze_pack_id"));
                        if ((analyze.analyze_id > 0) && (analyze.pack_stock_type == 1 || analyze.pack_stock_type == 2 || (analyze.pack_stock_type == 3 && JeproLabSettingModel.getIntValue("pack_stock_type") > 0))) {
                            query = "SELECT request_detail." + dataBaseObject.quoteName("analyze_quantity") + ", request_detail." + dataBaseObject.quoteName("analyze_quantity_refunded") + ", pack.";
                            query += dataBaseObject.quoteName("quantity") + " FROM " + dataBaseObject.quoteName("#__jeprolab_request_detail") + " AS request_detail LEFT JOIN ";
                            query += dataBaseObject.quoteName("#__jeprolab_request") + " AS request ON(request." + dataBaseObject.quoteName("request_id") + " = request_detail.";
                            query += dataBaseObject.quoteName("request_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_request_history") + " AS request_history ON (request_history.";
                            query += dataBaseObject.quoteName("request_id") + " = request." + dataBaseObject.quoteName("request_id") + " AND request_history." + dataBaseObject.quoteName("request_state_id");
                            query += " = request." + dataBaseObject.quoteName("current_state") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_request_state") + " AS request_state ON (request_state.";
                            query += dataBaseObject.quoteName("request_state_id") + " = request_history." + dataBaseObject.quoteName("request_state_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_pack");
                            query += " AS pack ON pack." + dataBaseObject.quoteName("analyze_item_id") + " = " + analyzeId + " AND pack." + dataBaseObject.quoteName("analyze_attribute_item_id") + " = ";
                            query += analyzeAttributeId + ") WHERE order_detail." + dataBaseObject.quoteName("analyze_id") + analyze.analyze_id + " AND request_state." + dataBaseObject.quoteName("terminated");
                            query += " != 1 AND request." + dataBaseObject.quoteName("valid") + " = 1 OR (request_state." + dataBaseObject.quoteName("request_state_id") + " != ";
                            query += JeproLabSettingModel.getIntValue("request_error_state") + " AND request_state." + dataBaseObject.quoteName("request_state_id") + " != ";
                            query += JeproLabSettingModel.getIntValue("request_cancel_state") + ") ";
                            if (warehouseIds.size() > 0) {
                                query += " AND request_detail." + dataBaseObject.quoteName("warehouse_id") + " IN (";
                                int index = 0;
                                for (Integer i : warehouseIds) {
                                    query += i + ((++index < warehouseIds.size()) ? ", " : "");
                                }
                                query += ") ";
                            }
                            query += " GROUP BY request_detail." + dataBaseObject.quoteName("request_detail_id");

                            //dataBaseObject.setQuery(query);
                            ResultSet requestDetailSet = dataBaseObject.loadObjectList(query);
                            if (requestDetailSet != null) {
                                while (requestDetailSet.next()) {
                                    clientRequestQuantity += ((requestDetailSet.getInt("analyze_quantity") - requestDetailSet.getInt("analyze_quantity_refunded")) * requestDetailSet.getInt("quantity"));
                                }
                            }
                        }
                    }
                } catch (SQLException ignored) {
                    ignored.printStackTrace();
                }
            }

                /*$query->select('od.product_quantity, od.product_quantity_refunded, pk.quantity');
                $query->from('order_detail', 'od');
                $query->leftjoin('orders', 'o', 'o.id_order = od.id_order');
                //$query->where('od.product_id = '.(int)$value['id_product_pack']);
                //$query->leftJoin('order_history', 'oh', 'oh.id_order = o.id_order AND oh.id_order_state = o.current_state');
                $query->leftJoin('order_state', 'os', 'os.id_order_state = oh.id_order_state');
                //$query->leftJoin('pack', 'pk', 'pk.id_product_item = '.(int)$id_product.' AND pk.id_product_attribute_item = '.($id_product_attribute ? (int)$id_product_attribute : '0').' AND id_product_pack = od.product_id');
                $query->where('os.shipped != 1');
                $query->where('o.valid = 1 OR (os.id_order_state != '.(int)Configuration::get('PS_OS_ERROR').'
                AND os.id_order_state != '.(int)Configuration::get('PS_OS_CANCELED').')');
                $query->groupBy('od.id_order_detail');
                if (count($ids_warehouse)) {
                    $query->where('od.id_warehouse IN('.implode(', ', $ids_warehouse).')');
                }* /
                $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
                if (count($res)) {
                    foreach ($res as $row) {
                        $client_orders_qty += ($row['product_quantity'] - $row['product_quantity_refunded']) * $row['quantity'];
                    }
                }
            }
        }
        }*/
            // skip if analyze is a pack without
            JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId);
            if (!JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId) || (JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId) && (analyze.analyze_id > 0) && analyze.pack_stock_type == 0 || analyze.pack_stock_type == 2 ||
                (analyze.pack_stock_type == 3 && (JeproLabSettingModel.getIntValue("pack_stock_type") == 0 || JeproLabSettingModel.getIntValue("pack_stock_type") == 2)))) {
                // Gets client_orders_qty
                query = "SELECT request_detail." + dataBaseObject.quoteName("analyze_quantity") + ", request_detail." + dataBaseObject.quoteName("analyze_quantity_refunded") + " FROM ";
                query += dataBaseObject.quoteName("#__jeprolab_request_detail") + " AS request_detail LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_request") + " AS request ON (";
                query += "request." + dataBaseObject.quoteName("request_id") +  " = request_detail." + dataBaseObject.quoteName("request_id") + ")";
               /* $query->select('od.product_quantity, od.product_quantity_refunded');
                $query->from('order_detail', 'od');
                $query->leftjoin('orders', 'o', 'o.id_order = od.id_order');
                $query->where('od.product_id = '.(int)$id_product);
                /*if (0 != $id_product_attribute) {
                    $query->where('od.product_attribute_id = '.(int)$id_product_attribute);
                }
                $query->leftJoin('order_history', 'oh', 'oh.id_order = o.id_order AND oh.id_order_state = o.current_state');
                $query->leftJoin('order_state', 'os', 'os.id_order_state = oh.id_order_state');
                $query->where('os.shipped != 1');
                $query->where('o.valid = 1 OR (os.id_order_state != '.(int)Configuration::get('PS_OS_ERROR').'
                AND os.id_order_state != '.(int)Configuration::get('PS_OS_CANCELED').')');
                $query->groupBy('od.id_order_detail');
                if (count($ids_warehouse)) {
                    $query->where('od.id_warehouse IN('.implode(', ', $ids_warehouse).')');
                }
                $res = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
                if (count($res)) {
                    foreach ($res as $row) {
                        $client_orders_qty += ($row['product_quantity'] - $row['product_quantity_refunded']);
                    }
                } */
            }

            // Gets supply_orders_qty
            //query =
/*
            $query->select('sod.quantity_expected, sod.quantity_received');
            $query->from('supply_order', 'so');
            $query->leftjoin('supply_order_detail', 'sod', 'sod.id_supply_order = so.id_supply_order');
            $query->leftjoin('supply_order_state', 'sos', 'sos.id_supply_order_state = so.id_supply_order_state');
            $query->where('sos.pending_receipt = 1');
            $query->where('sod.id_product = '.(int)$id_product.' AND sod.id_product_attribute = '.(int)$id_product_attribute);
            if (!is_null($ids_warehouse) && count($ids_warehouse)) {
                $query->where('so.id_warehouse IN('.implode(', ', $ids_warehouse).')');
            }*/

            //dataBaseObject.setQuery(query);
            ResultSet supplyRequestQuantities = dataBaseObject.loadObjectList(query);

            int supplyRequestQuantity = 0;
            if(supplyRequestQuantities != null) {
                try {
                    int expectedQuantity, receivedQuantity;
                    while(supplyRequestQuantities.next()) {
                        expectedQuantity = supplyRequestQuantities.getInt("quantity_expected");
                        receivedQuantity = supplyRequestQuantities.getInt("quantity_received");
                        if (expectedQuantity > receivedQuantity) {
                            supplyRequestQuantity += (expectedQuantity - receivedQuantity);
                        }
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }

            // Gets {physical OR usable}_qty
            int quantity = this.getAnalyzePhysicalQuantities(analyzeId, analyzeAttributeId, warehouseIds, usable);

            //real qty = actual qty in stock - current client orders + current supply orders
            return (quantity - clientRequestQuantity + supplyRequestQuantity);
        }

        /**
         * @see JeproLabStockManagerInterface::getAnalyzePhysicalQuantities()
         */
        public int getAnalyzePhysicalQuantities(int analyzeId, int analyzeAttributeId) {
            return getAnalyzePhysicalQuantities(analyzeId, analyzeAttributeId, null, false);
        }

        public int getAnalyzePhysicalQuantities(int analyzeId, int analyzeAttributeId, List<Integer> warehouseIds) {
            return getAnalyzePhysicalQuantities(analyzeId, analyzeAttributeId, warehouseIds, false);
        }

        public int getAnalyzePhysicalQuantities(int analyzeId, int analyzeAttributeId, List<Integer> warehouseIds, boolean usable) {
/*        if (!is_null($ids_warehouse)) {
        // in case $ids_warehouse is not an array
        if (!is_array($ids_warehouse)) {
        $ids_warehouse = array($ids_warehouse);
        }

        // casts for security reason
        $ids_warehouse = array_map('intval', $ids_warehouse);
        if (!count($ids_warehouse)) {
        return 0;
        }
        } else {
        $ids_warehouse = array();
        }

        $query = new DbQuery();
        $query->select('SUM('.($usable ? 's.usable_quantity' : 's.physical_quantity').')');
        $query->from('stock', 's');
        $query->where('s.id_product = '.(int)$id_product);
        if (0 != $id_product_attribute) {
        $query->where('s.id_product_attribute = '.(int)$id_product_attribute);
        }

        if (count($ids_warehouse)) {
        $query->where('s.id_warehouse IN('.implode(', ', $ids_warehouse).')');
        }
*/
            return  0; //(int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
        }
    }

    /**
     *
     * Created by jeprodev on 21/02/2016.
     */
    public interface JeproLabStockManagerInterface{

    }
}
