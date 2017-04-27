package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCarrierModel extends JeproLabModel{
    public int carrier_id;

    /** @var int common id for carrier historization */
    public String reference;

    public int language_id;

    /** @var string Name */
    public String name;

    /** @var string URL with a '@' for */
    public String url;

    /** @var string Delay needed to deliver customer */
    public Map<String, String> delay;

    /** @var bool Carrier statues */
    public boolean published = true;

    /** @var bool True if carrier has been deleted (staying in database as deleted) */
    public boolean deleted = false;

    /** @var bool Active or not the shipping handling */
    public boolean add_handling_cost = true;

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

    public int tax_rules_group_id;

    public String image_dir;

    /** @var int Position */
    public int position;

    /** @var int maximum package width managed by the transporter */
    public float max_width;

    /** @var int maximum package height managed by the transporter */
    public float max_height;

    /** @var int maximum package deep managed by the transporter */
    public float max_depth;

    /** @var int maximum package weight managed by the transporter */
    public float max_weight;

    /** @var int grade of the shipping delay (0 for longest, 9 for shortest) */
    public int grade;

    public String logo_path;

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

    public static int default_country_id = 0;

    private static Map<String, Float> _price_by_weight = new HashMap<>();
    private static Map<String, Boolean> _price_by_weight_2 = new HashMap<>();
    private static Map<String, Float> _price_by_price = new HashMap<>();
    private static Map<String, Boolean> _price_by_price_2 = new HashMap<>();

    public JeproLabCarrierModel(){ this(0); }

    public JeproLabCarrierModel(int carrierId){ this(carrierId, 0); }

    public JeproLabCarrierModel(int carrierId, int langId){
        this(carrierId, langId, 0);
    }

    public JeproLabCarrierModel(int carrierId, int langId, int labId){
        if(langId > 0){
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(carrierId > 0){
            String cacheKey = "jeprolab_model_carrier_" + carrierId + "_" + langId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                labId = (labId > 0) ? labId : JeproLabContext.getContext().laboratory.laboratory_id;
                String query = "SELECT carrier.* FROM " + dataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier ";
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_carrier_lang") + " AS carrier_lang ON (carrier." ;
                    query += dataBaseObject.quoteName("carrier_id") + " = carrier_lang." + dataBaseObject.quoteName("carrier_id");
                    query += " AND carrier_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + " AND carrier_lang.";
                    query += dataBaseObject.quoteName("lab_id") + " = " + labId;
                }
                query += " WHERE carrier." + dataBaseObject.quoteName("carrier_id") + " = " + carrierId;

                ResultSet carrierSet = dataBaseObject.loadObjectList(query);
                if(carrierSet != null){
                    try{
                        if(carrierSet.next()){
                            this.carrier_id = carrierSet.getInt("carrier_id");
                            this.reference = carrierSet.getString("reference");
                            this.tax_rules_group_id = carrierSet.getInt("tax_rules_group_id");
                            this.url = carrierSet.getString("url");
                            this.name = carrierSet.getString("name");
                            this.published = carrierSet.getInt("published") > 0;
                            this.add_handling_cost = carrierSet.getInt("add_handling_cost") > 0;
                            this.shipping_external = carrierSet.getInt("shipping_external") > 0;
                            this.range_behavior = carrierSet.getInt("range_behavior");
                            this.is_module = carrierSet.getInt("is_module") > 1;
                            this.is_free = carrierSet.getInt("is_free") > 1;
                            this.need_range = carrierSet.getInt("need_range") > 0;
                            this.external_module_name = carrierSet.getString("external_module_name");
                            this.shipping_method = carrierSet.getInt("shipping_method");
                            this.position = carrierSet.getInt("position");
                            this.max_depth = carrierSet.getFloat("max_depth");
                            this.max_height = carrierSet.getFloat("max_height");
                            this.max_weight = carrierSet.getFloat("max_weight");
                            this.max_width = carrierSet.getFloat("max_width");
                            this.grade = carrierSet.getInt("grade");
                        }
                    }catch(SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        closeDataBaseConnection(dataBaseObject);
                    }
                }
            }else{
                JeproLabCarrierModel carrier = (JeproLabCarrierModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.carrier_id = carrier.carrier_id;
                this.reference = carrier.reference;
                this.tax_rules_group_id = carrier.tax_rules_group_id;
                this.url = carrier.url;
                this.name = carrier.name;
                this.published = carrier.published;
                this.add_handling_cost = carrier.add_handling_cost;
                this.shipping_external = carrier.shipping_external;
                this.range_behavior = carrier.range_behavior;
                this.is_module = carrier.is_module;
                this.is_free = carrier.is_free;
                this.need_range = carrier.need_range;
                this.external_module_name = carrier.external_module_name;
                this.shipping_method = carrier.shipping_method;
                this.position = carrier.position;
                this.max_depth = carrier.max_depth;
                this.max_height = carrier.max_height;
                this.max_weight = carrier.max_weight;
                this.max_width = carrier.max_width;
                this.grade = carrier.grade;
            }
        }

        if (this.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_DEFAULT) {
            this.shipping_method = (JeproLabSettingModel.getIntValue("shipping_method") > 0 ? JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT : JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE);
        }

        if (this.carrier_id > 0) {
            this.tax_rules_group_id = this.getTaxRulesGroupId(JeproLabContext.getContext());
        }

        if (this.name == null) {
            this.name = JeproLabCarrierModel.getCarrierNameFromLaboratoryName();
        }
    }

    public int getTaxRulesGroupId(){
        return JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(this.carrier_id, null);
    }

    public int getTaxRulesGroupId(JeproLabContext context){
        return JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(this.carrier_id, context);
    }

    public static int getTaxRulesGroupIdByCarrierId(int carrierId){
        return getTaxRulesGroupIdByCarrierId(carrierId, null);
    }

    public static int getTaxRulesGroupIdByCarrierId(int carrierId, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        String cacheKey = "jeprolab_carrier_tax_rules_group_id_" + carrierId + "_" + context.laboratory.laboratory_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT " + dataBaseObject.quoteName("tax_rules_group_id") + " FROM ";
            query += dataBaseObject.quoteName("#__jeprolab_carrier_tax_rules_group_lab") + " WHERE ";
            query += dataBaseObject.quoteName("carrier_id") + " = " + carrierId + " AND lab_id = " + JeproLabContext.getContext().laboratory.laboratory_id;

            int result = (int)dataBaseObject.loadValue(query, "tax_rules_group_d");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static List<JeproLabCarrierModel> getCarriers(int langId){
        return getCarriers(langId, false, false, 0, null, JeproLabCarrierModel.JEPROLAB_CARRIERS_ONLY);
    }

    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active){
        return getCarriers(langId, active, false, 0, null, JeproLabCarrierModel.JEPROLAB_CARRIERS_ONLY);
    }

    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active, boolean delete){
        return getCarriers(langId, active, delete, 0, null, JeproLabCarrierModel.JEPROLAB_CARRIERS_ONLY);
    }

    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active, boolean delete, int zoneId){
        return getCarriers(langId, active, delete, zoneId, null, JeproLabCarrierModel.JEPROLAB_CARRIERS_ONLY);
    }

    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active, boolean delete, int zoneId, List<Integer>groupIds){
        return getCarriers(langId, active, delete, zoneId, groupIds, JeproLabCarrierModel.JEPROLAB_CARRIERS_ONLY);
    }

    /**
     * Get all carriers in a given language
     *
     * @param langId Language id
     * @param modulesFilters, possible values:  JEPROLAB_CARRIERS_ONLY
     *                                          JEPROLAB_CARRIERS_MODULE
     *                                          JEPROLAB_CARRIERS_MODULE_NEED_RANGE
     *                                          JEPROLAB_CARRIERS_AND_CARRIER_MODULES_NEED_RANGE
     *                                          JEPROLAB_ALL_CARRIERS
     * @param active Returns only active carriers when true
     * @return array Carriers
     */
    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active, boolean delete, int zoneId, List<Integer> groupIds, int modulesFilters){
        // Filter by groups and no groups => return empty array
        if (groupIds == null){
            groupIds = new ArrayList<>();
        }

        if(dataBaseObject == null){ dataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        String query = "SELECT carrier.*, carrier_lang.delay FROM  " + dataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_carrier_lang") + " AS carrier_lang ON (carrier." + dataBaseObject.quoteName("carrier_id");
        query += "= carrier_lang." + dataBaseObject.quoteName("carrier_id") + " AND carrier_lang." + dataBaseObject.quoteName("lang_id") + " = ";
        query += langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("carrier_lang") + ")  LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_carrier_zone");
        query += " AS carrier_zone ON (carrier_zone." + dataBaseObject.quoteName("carrier_id") + " = carrier." + dataBaseObject.quoteName("carrier_id") + ")";
        query += (zoneId > 0 ? "LEFT JOIN  " + dataBaseObject.quoteName("#__jeprolab_zone") + " AS zone ON (zone." + dataBaseObject.quoteName("zone_id") + " = " + zoneId + ")" : "");
        query += JeproLabLaboratoryModel.addSqlAssociation("carrier") + " WHERE carrier." + dataBaseObject.quoteName("deleted") + " = " + (delete ? 1 : 0);

        if (active) {
            query += " AND carrier." + dataBaseObject.quoteName("published") + " = 1 ";
        }
        if (zoneId > 0) {
            query += " AND carrier_zone." + dataBaseObject.quoteName("zone_id") + " = " + zoneId + " AND zone." + dataBaseObject.quoteName("published") + " = 1 ";
        }

        if(groupIds.size() > 0) {
            query += " AND EXISTS (SELECT 1 FROM " + dataBaseObject.quoteName("#__jeprolab_carrier_group") + " WHERE '." + dataBaseObject.quoteName("#__jeprolab_carrier_group");
            query += "." + dataBaseObject.quoteName("carrier_id") + " = carrier." + dataBaseObject.quoteName("carrier_id") + " AND " ;
            query += dataBaseObject.quoteName("group_id") + "  IN (";
            String groupFilter = "";
            for(int groupId : groupIds){
                groupFilter += groupId + ", ";
            }
            groupFilter = groupFilter.endsWith(", ") ? groupFilter.substring(0, groupFilter.length() - 2) : groupFilter;
            query += groupFilter + ")";
            //.implode(',', array_map('intval', $ids_group)).')) ';
        }

        switch (modulesFilters) {
            case 1 :
                query += " AND carrier." + dataBaseObject.quoteName("is_module") + " = 0 ";
                break;
            case 2 :
                query += " AND carrier." + dataBaseObject.quoteName("is_module") + " = 1 ";
                break;
            case 3 :
                query += " AND carrier." + dataBaseObject.quoteName("is_module") + " = 1 AND carrier.";
                query += dataBaseObject.quoteName("need_range") + " = 1 ";
                break;
            case 4 :
                query += " AND(carrier." + dataBaseObject.quoteName("is_module") + "= 0 OR carrier.";
                query += dataBaseObject.quoteName("need_range") + " = 1) = 0 OR carrier." + dataBaseObject.quoteName("need_range") + " = 1) ";
                break;
        }
        query += " GROUP BY carrier." + dataBaseObject.quoteName("carrier_id") + " ORDER BY carrier." + dataBaseObject.quoteName("position") + " ASC";


        String cacheKey = "jeprolab_carrier_model_get_carriers_" + JeproLabTools.md5(query);
        List<JeproLabCarrierModel> carriers = new ArrayList<>();
        if (!JeproLabCache.getInstance().isStored(cacheKey)){

            ResultSet carrierSet = dataBaseObject.loadObjectList(query);
            if(carrierSet != null){
                JeproLabCarrierModel carrier;
                try{
                    while(carrierSet.next()) {
                        carrier = new JeproLabCarrierModel();
                        carrier.carrier_id = carrierSet.getInt("carrier_id");
                        carrier.reference = carrierSet.getString("reference");
                        carrier.tax_rules_group_id = carrierSet.getInt("tax_rules_group_id");
                        carrier.url = carrierSet.getString("url");
                        carrier.name = carrierSet.getString("name");
                        carrier.published = carrierSet.getInt("published") > 0;
                        carrier.add_handling_cost = carrierSet.getInt("add_handling_cost") > 0;
                        carrier.shipping_external = carrierSet.getInt("shipping_external") > 0;
                        carrier.range_behavior = carrierSet.getInt("range_behavior");
                        carrier.is_module = carrierSet.getInt("is_module") > 1;
                        carrier.is_free = carrierSet.getInt("is_free") > 1;
                        carrier.need_range = carrierSet.getInt("need_range") > 0;
                        carrier.external_module_name = carrierSet.getString("external_module_name");
                        carrier.shipping_method = carrierSet.getInt("shipping_method");
                        carrier.position = carrierSet.getInt("position");
                        carrier.max_depth = carrierSet.getFloat("max_depth");
                        carrier.max_height = carrierSet.getFloat("max_height");
                        carrier.max_weight = carrierSet.getFloat("max_weight");
                        carrier.max_width = carrierSet.getFloat("max_width");
                        carrier.grade = carrierSet.getInt("grade");
                        carriers.add(carrier);
                    }
                }catch (SQLException ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }
            //carriers = Db::getInstance () -> executeS($sql);
            JeproLabCache.getInstance().store(cacheKey, carriers);
        } else {
            carriers = (List<JeproLabCarrierModel>)JeproLabCache.getInstance().retrieve(cacheKey);
        }
           /*
        foreach ($carriers as $key => $carrier){
            if ($carrier['name'] == '0') {
                $carriers[$key]['name'] = JeproLabCarrierModel.getCarrierNameFromLaboratoryName();
            }
        }*/
        return carriers;
    }

    /**
     * Return the carrier name from the shop name (e.g. if the carrier name is '0').
     *
     * The returned carrier name is the shop name without '#' and ';' because this is not the same validation.
     *
     * @return string Carrier name
     */
    public static String getCarrierNameFromLaboratoryName(){
        /*return str_replace(
                array('#', ';'),
                '',
                Configuration::get('PS_SHOP_NAME')
        );*/
        return  "";
    }
}
