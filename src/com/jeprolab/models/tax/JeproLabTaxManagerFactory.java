package com.jeprolab.models.tax;

import com.jeprolab.models.JeproLabAddressModel;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 04/02/2014.
 */
public class JeproLabTaxManagerFactory {
    protected static Map<String, JeproLabTaxRulesManager> cache_tax_manager = new HashMap<>();

    public static JeproLabTaxRulesManager getManager(JeproLabAddressModel address, int type){
        return getManager(address, type + "");
    }

    /**
     * Returns a tax manager able to handle this address
     *
     * @param address
     * @param type
     *
     * @return TaxManagerInterface
     */
    public static JeproLabTaxRulesManager getManager(JeproLabAddressModel address, String type){
        String cacheKey = JeproLabTaxManagerFactory.getCacheKey(address) + "_" + type;
        JeproLabTaxRulesManager taxManager;
        if (!JeproLabTaxManagerFactory.cache_tax_manager.containsKey(cacheKey)){
            taxManager = JeproLabTaxManagerFactory.execHookTaxManagerFactory(address, type);
            if (taxManager == null) {
                taxManager = new JeproLabTaxRulesManager(address, type);
            }

            JeproLabTaxManagerFactory.cache_tax_manager.put(cacheKey, taxManager);
        }
        return JeproLabTaxManagerFactory.cache_tax_manager.get(cacheKey);
    }

    /**
     * Check for a tax manager able to handle this type of address in the module list
     *
     * @param address
     * @param type
     *
     * @return TaxManagerInterface|false
     */
    public static JeproLabTaxRulesManager execHookTaxManagerFactory(JeproLabAddressModel address, String type){
        //$modules_infos = Hook::getModulesFromHook(Hook::getIdByName('taxManager'));
        JeproLabTaxRulesManager taxManager = null;
        /*
        foreach ($modules_infos as $module_infos) {
            $module_instance = Module::getInstanceByName($module_infos['name']);
            if (is_callable(array($module_instance, 'hookTaxManager'))) {
                $tax_manager = $module_instance->hookTaxManager(array(
                                'address' => $address,
                        'params' => $type
                ));
            }

            if(taxManager != null) {
                break;
            }
        } */
        return taxManager;
    }


    /**
     * Create a unique identifier for the address
     * @param address the address
     */
    protected static String getCacheKey(JeproLabAddressModel address){
        return address.country_id + "_" + address.state_id + "_" + address.postcode + "_" + address.vat_number + "_" + address.dni;
    }
}