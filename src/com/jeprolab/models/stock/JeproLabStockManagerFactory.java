package com.jeprolab.models.stock;


/**
 *
 * Created by jeprodev on 21/02/2014.
 */
public class JeproLabStockManagerFactory {
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