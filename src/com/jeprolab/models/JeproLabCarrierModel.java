package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCarrierModel extends JeproLabModel{
    /**
     * getCarriers method filter
     */
    public static final int JEPROLAB_CARRIERS_ONLY = 1;
    public static final int JEPROLAB_CARRIERS_MODULE = 2;
    public static final int JEPROLAB_CARRIERS_MODULE_NEED_RANGE = 3;
    public static final int JEPROLAB_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE = 4;
    public static final int JEPROLAB_ALL_CARRIERS = 5;

    public static final int JEPROLAB_SHIPPING_METHOD_DEFAULT = 0;
    public static final int JEPROLAB_SHIPPING_METHOD_WEIGHT = 1;
    public static final int JEPROLAB_SHIPPING_METHOD_PRICE = 2;
    public static final int JEPROLAB_SHIPPING_METHOD_FREE = 3;

    public static final int JEPROLAB_SHIPPING_PRICE_EXCEPTION = 0;
    public static final int JEPROLAB_SHIPPING_WEIGHT_EXCEPTION = 1;
    public static final int JEPROLAB_SHIPPING_SIZE_EXCEPTION = 2;

    public static final int JEPROLAB_SORT_BY_PRICE = 0;
    public static final int JEPROLAB_SORT_BY_POSITION = 1;

    public static final int JEPROLAB_SORT_BY_ASC = 0;
    public static final int JEPROLAB_SORT_BY_DESC = 1;

    /** @var int common id for carrier historization */
    public int reference_id;

    /** @var string Name */
    public String name;

    /** @var string URL with a '@' for */
    public String url;

    /** @var string Delay needed to deliver customer */
    public String delay;

    /** @var bool Carrier statuts */
    public boolean published = true;

    /** @var bool True if carrier has been deleted (staying in database as deleted) */
    public boolean deleted = false;

    /** @var bool Active or not the shipping handling */
    public boolean shipping_handling = true;

    /** @var int Behavior taken for unknown range */
    public int range_behavior;

    /** @var bool Carrier module */
    public boolean is_module;

    /** @var bool Free carrier */
    public boolean is_free = false;

    /** @var int shipping behavior: by weight or by price */
    public int shipping_method = 0;

    /** @var bool Shipping external */
    public boolean shipping_external = false;

    /** @var string Shipping external */
    public String external_module_name = null;

    /** @var bool Need Range */
    public boolean need_range = false;

    /** @var int Position */
    public int position;

    /** @var int maximum package width managed by the transporter */
    public int max_width;

    /** @var int maximum package height managed by the transporter */
    public int max_height;

    /** @var int maximum package deep managed by the transporter */
    public int max_depth;

    /** @var int maximum package weight managed by the transporter */
    public int max_weight;

    /** @var int grade of the shipping delay (0 for longest, 9 for shortest) */
    public int grade;

    public static int getTaxRulesGroupIdByCarrierId(int carrierId){
        return getTaxRulesGroupIdByCarrierId(carrierId, null);
    }

    public static int getTaxRulesGroupIdByCarrierId(int carrierId, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        String cacheKey = "jeprolab_carrier_tax_rules_group_id_" + carrierId + "_" + context.laboratory.laboratory_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT " + staticDataBaseObject.quoteName("tax_rules_group_id") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_lab") + " WHERE ";
            query += staticDataBaseObject.quoteName("carrier_id") + " = " + carrierId + " AND lab_id = " + JeproLabContext.getContext().laboratory.laboratory_id;

            staticDataBaseObject.setQuery(query);
            int result = (int)staticDataBaseObject.loadValue("tax_rules_group_d");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

}
