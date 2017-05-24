package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCarrierModel extends JeproLabModel{
    public int carrier_id;

    /** @var int common id for carrier historization */
    public String reference = "";

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

    public static final int JEPROLAB_TAKE_THE_UPPER_RANGE = 1;
    public static final int JEPROLAB_DEACTIVATE_CARRIER = 0;

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
                labId = (labId > 0) ? labId : JeproLabContext.getContext().laboratory.laboratory_id;
                String query = "SELECT carrier.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " AS carrier ";
                if(langId > 0){
                    query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_lang") + " AS carrier_lang ON (carrier." ;
                    query += JeproLabDataBaseConnector.quoteName("carrier_id") + " = carrier_lang." + JeproLabDataBaseConnector.quoteName("carrier_id");
                    query += " AND carrier_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + " AND carrier_lang.";
                    query += JeproLabDataBaseConnector.quoteName("lab_id") + " = " + labId;
                }
                query += " WHERE carrier." + JeproLabDataBaseConnector.quoteName("carrier_id") + " = " + carrierId;

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                
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

                            this.delay = new ConcurrentHashMap<>();
                        }
                    }catch(SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        closeDataBaseConnection(dataBaseObject);
                    }
                }else{
                    closeDataBaseConnection(dataBaseObject);
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
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id") + " FROM ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_tax_rules_group_lab") + " WHERE ";
            query += JeproLabDataBaseConnector.quoteName("carrier_id") + " = " + carrierId + " AND lab_id = " + JeproLabContext.getContext().laboratory.laboratory_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
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

    public static List<JeproLabCarrierModel> getCarriers(int langId, boolean active, boolean delete, int zoneId, List<Integer> groupIds){
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


        String query = "SELECT carrier.*, carrier_lang.delay FROM  " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " AS carrier LEFT JOIN ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_lang") + " AS carrier_lang ON (carrier." + JeproLabDataBaseConnector.quoteName("carrier_id");
        query += "= carrier_lang." + JeproLabDataBaseConnector.quoteName("carrier_id") + " AND carrier_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = ";
        query += langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("carrier_lang") + ")  LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_zone");
        query += " AS carrier_zone ON (carrier_zone." + JeproLabDataBaseConnector.quoteName("carrier_id") + " = carrier." + JeproLabDataBaseConnector.quoteName("carrier_id") + ")";
        query += (zoneId > 0 ? "LEFT JOIN  " + JeproLabDataBaseConnector.quoteName("#__jeprolab_zone") + " AS zone ON (zone." + JeproLabDataBaseConnector.quoteName("zone_id") + " = " + zoneId + ")" : "");
        query += JeproLabLaboratoryModel.addSqlAssociation("carrier") + " WHERE carrier." + JeproLabDataBaseConnector.quoteName("deleted") + " = " + (delete ? 1 : 0);

        if (active) {
            query += " AND carrier." + JeproLabDataBaseConnector.quoteName("published") + " = 1 ";
        }
        if (zoneId > 0) {
            query += " AND carrier_zone." + JeproLabDataBaseConnector.quoteName("zone_id") + " = " + zoneId + " AND zone." + JeproLabDataBaseConnector.quoteName("published") + " = 1 ";
        }

        if(groupIds.size() > 0) {
            query += " AND EXISTS (SELECT 1 FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_group") + " WHERE '." + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_group");
            query += "." + JeproLabDataBaseConnector.quoteName("carrier_id") + " = carrier." + JeproLabDataBaseConnector.quoteName("carrier_id") + " AND " ;
            query += JeproLabDataBaseConnector.quoteName("group_id") + "  IN (";
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
                query += " AND carrier." + JeproLabDataBaseConnector.quoteName("is_module") + " = 0 ";
                break;
            case 2 :
                query += " AND carrier." + JeproLabDataBaseConnector.quoteName("is_module") + " = 1 ";
                break;
            case 3 :
                query += " AND carrier." + JeproLabDataBaseConnector.quoteName("is_module") + " = 1 AND carrier.";
                query += JeproLabDataBaseConnector.quoteName("need_range") + " = 1 ";
                break;
            case 4 :
                query += " AND(carrier." + JeproLabDataBaseConnector.quoteName("is_module") + "= 0 OR carrier.";
                query += JeproLabDataBaseConnector.quoteName("need_range") + " = 1) = 0 OR carrier." + JeproLabDataBaseConnector.quoteName("need_range") + " = 1) ";
                break;
        }
        query += " GROUP BY carrier." + JeproLabDataBaseConnector.quoteName("carrier_id") + " ORDER BY carrier." + JeproLabDataBaseConnector.quoteName("position") + " ASC";


        String cacheKey = "jeprolab_carrier_model_get_carriers_" + JeproLabTools.md5(query);
        List<JeproLabCarrierModel> carriers = new ArrayList<>();
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
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
            }else{
                closeDataBaseConnection(dataBaseObject);
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

    public void add(){
        if(this.position <= 0){
            this.position = JeproLabCarrierModel.getHigherPosition() + 1;
        }

        String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " (" + JeproLabDataBaseConnector.quoteName("reference");
        query += ", " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id") + ", " + JeproLabDataBaseConnector.quoteName("name") + ", ";
        query += JeproLabDataBaseConnector.quoteName("published") + ", " + JeproLabDataBaseConnector.quoteName("deleted") + ", " + JeproLabDataBaseConnector.quoteName("url");
        query += ", " + JeproLabDataBaseConnector.quoteName("add_handling_cost") + ", " + JeproLabDataBaseConnector.quoteName("range_behavior") + ", ";
        query += JeproLabDataBaseConnector.quoteName("is_module") + ", " + JeproLabDataBaseConnector.quoteName("is_free") + ", " + JeproLabDataBaseConnector.quoteName("shipping_external");
        query += ", " + JeproLabDataBaseConnector.quoteName("need_range") + ", " + JeproLabDataBaseConnector.quoteName("external_module_name") + ", ";
        query += JeproLabDataBaseConnector.quoteName("shipping_method") + ", " + JeproLabDataBaseConnector.quoteName("position") + ", " + JeproLabDataBaseConnector.quoteName("max_width");
        query += ", " + JeproLabDataBaseConnector.quoteName("max_height") + ", " + JeproLabDataBaseConnector.quoteName("max_depth") + ", " + JeproLabDataBaseConnector.quoteName("max_weight");
        query += ", " + JeproLabDataBaseConnector.quoteName("grade") +  ") VALUES (" + JeproLabDataBaseConnector.quote(this.reference)  + ", " + this.tax_rules_group_id + ", " ;
        query += JeproLabDataBaseConnector.quote(this.name) + ", " + (this.published ? 1 : 0) + ", " + (this.deleted ? 1 : 0) + ", " + JeproLabDataBaseConnector.quote(this.url);
        query += ", " + (this.add_handling_cost ? 1 : 0) + ", " + this.range_behavior + ", " + (this.is_module ? 1 : 0) + ", " + (this.is_free ? 1 : 0) + ", " + (this.shipping_external ? 1 : 0);
        query += ", " + (this.need_range ? 1 : 0) + ", " + JeproLabDataBaseConnector.quote(this.external_module_name) + ", " + this.shipping_method + ", " + this.position + ", " + this.max_width;
        query += ", " + this.max_height + ", " + this.max_depth + ", " + this.max_weight + ", " + this.grade + ")";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        if(dataBaseObject.query(query, true)){
            this.carrier_id = dataBaseObject.getGeneratedKey();
        }

        if(countDeletedCarrier() == 1){
            JeproLabSettingModel.updateValue("default_carrier_id", this.carrier_id);
        }

        for(int labId : JeproLabLaboratoryModel.getContextListLaboratoryIds()){
            /*foreach (Module::getPaymentModules() as $module) {
                Db::getInstance()->execute('
                    INSERT INTO `'._DB_PREFIX_.'module_'.bqSQL('carrier').'`
                (`id_module`, `id_shop`, `id_'.bqSQL('reference').'`)
                VALUES ('.(int) $module['id_module'].','.(int) $shopId.','.(int) $this->id.')'
                );
            }*/
        }
        closeDataBaseConnection(dataBaseObject);
    }

    private int countDeletedCarrier(){
        String query = "SELECT COUNT(" + JeproLabDataBaseConnector.quoteName("carrier_id") + ") AS carriers FROM ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " WHERE "+ JeproLabDataBaseConnector.quoteName("deleted") + " = 0";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        int carriers = (int)dataBaseObject.loadValue(query, "carriers");
        closeDataBaseConnection(dataBaseObject);
        return carriers;
    }

    public boolean delete(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " WHERE " + JeproLabDataBaseConnector.quoteName("carrier_id");
        query += " = " + this.carrier_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        JeproLabCarrierModel.cleanPositions();
        return true;
    }


    public boolean update() {
        if (this.position <= 0) {
            this.position = JeproLabCarrierModel.getHigherPosition() + 1;
        }

        /*String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " SET " + JeproLabDataBaseConnector.quoteName("reference");
        query += " = " + this.reference + ", " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id") + " = " + this.tax_rules_group_id + ", " ;
        query += JeproLabDataBaseConnector.quoteName("name") + " = " + JeproLabDataBaseConnector.quote(this.name) +  ", " + JeproLabDataBaseConnector.quoteName("published");
        query += " = " + (this.published ? 1  : 0) + ", " + JeproLabDataBaseConnector.quoteName("deleted") + " = " + (this.deleted ? 1 : 0) + ", " ;
        query += JeproLabDataBaseConnector.quoteName("url") + " = " + JeproLabDataBaseConnector.quote(this.url) + ", " + JeproLabDataBaseConnector.quoteName("add_handling_cost") ;
        query += " = " + (this.add_handling_cost ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("range_behavior") + " = " + this.range_behavior;
        query += ", " + JeproLabDataBaseConnector.quoteName("is_module")  + " = " + (this.is_module ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("is_free");
        query += " = " + (this.is_free ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("shipping_external") + " = " + (this.shipping_external ? 1 : 0);
        query += ", " + JeproLabDataBaseConnector.quoteName("need_range") + " = " + (this.need_range ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("external_module_name");
        query += " = " + JeproLabDataBaseConnector.quote(this.external_module_name) + ", " + JeproLabDataBaseConnector.quoteName("shipping_method") + " = " + this.shipping_method;
        query += ", " + JeproLabDataBaseConnector.quoteName("position") + " = " + this.position + ", " + JeproLabDataBaseConnector.quoteName("max_width") + " = ";
        query += this.max_width + ", " + JeproLabDataBaseConnector.quoteName("max_height") + " = " + this.max_height + ", " + JeproLabDataBaseConnector.quoteName("max_depth");
        query += " = " + this.max_depth + ", " + JeproLabDataBaseConnector.quoteName("max_weight") + " = " + this.max_weight + ", " + JeproLabDataBaseConnector.quoteName("grade");
        query += " = " + this.grade + " WHERE " + JeproLabDataBaseConnector.quoteName("carrier_id") + " = " + this.carrier_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result; */
        return false;
    }

    /**
     * Gets the highest carrier position.
     *
     * @return int $position
     */
    public static int getHigherPosition(){
        String query = "SELECT MAX(" + JeproLabDataBaseConnector.quoteName("position") + ") AS position FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier");
        query += " WHERE " + JeproLabDataBaseConnector.quoteName("deleted") + " = 0";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        int position = (int)dataBaseObject.loadValue(query, "position");
        closeDataBaseConnection(dataBaseObject);
        return position;
    }

    /**
     * Reorder Carrier positions
     * Called after deleting a Carrier.
     *
     * @return bool isCleaned
     */
    public static boolean cleanPositions(){
        boolean isCleaned = true;
        
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("carrier_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier");
        query += " WHERE " + JeproLabDataBaseConnector.quoteName("deleted") + " = 0 ORDER BY "  + JeproLabDataBaseConnector.quoteName("position") + " ASC";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet positionSet = dataBaseObject.loadObjectList(query);
        
        if(positionSet != null){
            try {
                int i = 0;
                while (positionSet.next()){
                    query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " SET ";
                    query += JeproLabDataBaseConnector.quoteName("position") + " = " + i++ + " WHERE " ;
                    query += JeproLabDataBaseConnector.quoteName("carrier_id") + " = " + positionSet.getInt("carrier_id");
                    
                    isCleaned &= dataBaseObject.query(query, false);
                }
            }catch(SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }else{
            closeDataBaseConnection(dataBaseObject);
        }
        return isCleaned;
    }

    /**
     * For a given {product, warehouse}, gets the carrier available.
     *
     * @param analyzeId         int analyze id
     * @param warehouseId       Warehouse ID
     * @param retrieveAddressId Retrieval Address ID
     *
     * @return array Available Carriers
     */
    public static List<JeproLabCarrierModel> getAvailableCarrierList(int analyzeId, int warehouseId, int retrieveAddressId){
        return getAvailableCarrierList(analyzeId, warehouseId, retrieveAddressId, JeproLabSettingModel.getIntValue("default_lab_id"));
    }

    /**
     * For a given {product, warehouse}, gets the carrier available.
     *
     * @param analyzeId         int analyze id
     * @param warehouseId       Warehouse ID
     * @param retrieveAddressId Retrieval Address ID
     * @param labId             Laboratory ID
     *
     * @return array Available Carriers
     */
    public static List<JeproLabCarrierModel> getAvailableCarrierList(int analyzeId, int warehouseId, int retrieveAddressId, int labId){
        return getAvailableCarrierList(analyzeId, warehouseId, retrieveAddressId, labId, null);
    }

/**
     * For a given {product, warehouse}, gets the carrier available.
     *
     * @param analyzeId         int analyze id
     * @param warehouseId       Warehouse ID
     * @param retrieveAddressId Retrieval Address ID
     * @param labId             Laboratory ID
     *
     * @return array Available Carriers
     */
    public static List<JeproLabCarrierModel> getAvailableCarrierList(int analyzeId, int warehouseId, int retrieveAddressId, int labId, JeproLabCartModel cart){
        return getAvailableCarrierList(analyzeId, warehouseId, retrieveAddressId, labId, cart,  null);
    }


    /**
     * For a given {product, warehouse}, gets the carrier available.
     *
     * @param analyzeId         int analyze id
     * @param warehouseId       Warehouse ID
     * @param retrieveAddressId Retrieval Address ID
     * @param labId             Laboratory ID
     * @param cart              Cart object
     * @param error             contain an error message if an error occurs
     *
     * @return array Available Carriers
     */
    public static List<JeproLabCarrierModel> getAvailableCarrierList(int analyzeId, int warehouseId, int retrieveAddressId, int labId, JeproLabCartModel cart, List<String> error){
        int defaultCountryId = JeproLabSettingModel.getIntValue("default_country");
/*
        if ($ps_country_default === null) {
            $ps_country_default = Configuration::get('PS_COUNTRY_DEFAULT');
        }* /

        if (labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        if (cart == null) {
            cart = JeproLabContext.getContext().cart;
        }

        /*if (is_null($error) || !is_array($error)) {
            $error = array();
        }* /

        int addressId = (int) ((retrieveAddressId != 0) ? retrieAddressId : cart.delivery_address_id);
        int zoneId;
        if (addressId > 0) {
            zoneId = JeproLabAddressModel.getZoneIdByAddressId(addressId);

            // Check the country of the address is activated
            if (!JeproLabAddressModel.isCountryActiveByAddressId(addressId)) {
                return new ArrayList<>();
            }
        } else {
            JeproLabCountryModel country = new JeproLabCountryModel(defaultCountryId);
            zoneId = country.zone_id;
        }

        // Does the product is linked with carriers?
        String cacheKey = "jeprolab_Carrier_model_get_available_carrier_list_" + analyzeId + "_" + labId;
        List<Integer> carriers;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("carrier_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_carrier");
            query += " AS analyze_carrier INNER JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " AS carrier ON (carrier.";
            query += JeproLabDataBaseConnector.quoteName("carrier_id") + " = analyze_carrier." + JeproLabDataBaseConnector.quoteName("carrier_id");
            query += " AND carrier." + JeproLabDataBaseConnector.quoteName("deleted") + " AND carrier." + JeproLabDataBaseConnector.quoteName("published");
            query += " = 1) WHERE analyze_carrier." + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId + " AND analyze_carrier.";
            query += JeproLabDataBaseConnector.quoteName("lab_id") + " = " + labId;

            if (dataBaseObject == null) {
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            ResultSet carriersSet = dataBaseObject.loadObjectList(query);
            carriers = new ArrayList<>();
            if (carriersSet != null) {
                try {

                    while (carriersSet.next()) {
                        carriers.add(carriersSet.getInt("carrier_id"));
                    }
                } catch (SQLException ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
            JeproLabCache.getInstance().store(cacheKey, carriers);
        } else {
            carriers = (List<Integer>)JeproLabCache.getInstance().retrieve(cacheKey);
        }

        List<JeproLabCarrierModel> carrierList = new ArrayList<>();
        if(!carriers.isEmpty()){
            //the product is linked with carriers
            for(int carrierId : carriers) { //check if the linked carriers are available in current zone
                if (JeproLabCarrierModel.checkCarrierZone(carrierId, zoneId)) {
                    carrierList[$carrier['id_carrier']] = $carrier['id_carrier'];
                }
            }
            if (carrierList.isEmpty()){
                return carrierList;
            }//no linked carrier are available for this zone
        }

        // The product is not directly linked with a carrier
        // Get all the carriers linked to a warehouse
        if (warehouseId) {
            JeproLabWarehouseModel warehouse = new JeproLabWarehouseModel(warehouseId);
            warehouseCarrierList = warehouse.getCarriers();
        }

        List availableCarrierList = new ArrayList();
        cacheKey = "jeprolab_carrier_get_available_carrier_list_get_carriers_for_request_" + zoneId + "_" + cart.cart_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            $customer = new Customer($cart -> id_customer);
            $carrier_error = array();
            $carriers = Carrier::getCarriersForOrder ($id_zone, $customer -> getGroups(), $cart, $carrier_error);
            JeproLabCache.getInstance().store(cacheKey, array($carriers, $carrier_error));
        } else {
            list($carriers, $carrier_error) = Cache::retrieve ($cache_id);
        }

        $error = array_merge($error, $carrier_error);

        foreach ($carriers as $carrier) {
        $available_carrier_list[$carrier['id_carrier']] = $carrier['id_carrier'];
    }

        if ($carrier_list) {
            $carrier_list = array_intersect($available_carrier_list, $carrier_list);
        } else {
            $carrier_list = $available_carrier_list;
        }

        if (isset($warehouse_carrier_list)) {
            $carrier_list = array_intersect($carrier_list, $warehouse_carrier_list);
        }

        $cart_quantity = 0;
        $cart_weight = 0;

        for(cart.getProducts(false, false) as $cart_product) {
        if ($cart_product['id_product'] == $product->id) {
            $cart_quantity += $cart_product['cart_quantity'];
        }
        if (isset($cart_product['weight_attribute']) && $cart_product['weight_attribute'] > 0) {
            $cart_weight += ($cart_product['weight_attribute'] * $cart_product['cart_quantity']);
        } else {
            $cart_weight += ($cart_product['weight'] * $cart_product['cart_quantity']);
        }
    }

        if ($product->width > 0 || $product->height > 0 || $product->depth > 0 || $product->weight > 0 || $cart_weight > 0) {
            foreach ($carrier_list as $key => $id_carrier) {
                $carrier = new Carrier($id_carrier);

                // Get the sizes of the carrier and the product and sort them to check if the carrier can take the product.
                $carrier_sizes = array((int) $carrier->max_width, (int) $carrier->max_height, (int) $carrier->max_depth);
                $product_sizes = array((int) $product->width, (int) $product->height, (int) $product->depth);
                rsort($carrier_sizes, SORT_NUMERIC);
                rsort($product_sizes, SORT_NUMERIC);

                if (($carrier_sizes[0] > 0 && $carrier_sizes[0] < $product_sizes[0])
                    || ($carrier_sizes[1] > 0 && $carrier_sizes[1] < $product_sizes[1])
                    || ($carrier_sizes[2] > 0 && $carrier_sizes[2] < $product_sizes[2])) {
                    $error[$carrier->id] = Carrier::SHIPPING_SIZE_EXCEPTION;
                    unset($carrier_list[$key]);
                }

                if ($carrier->max_weight > 0 && ($carrier->max_weight < $product->weight * $cart_quantity || $carrier->max_weight < $cart_weight)) {
                    $error[$carrier->id] = Carrier::SHIPPING_WEIGHT_EXCEPTION;
                    unset($carrier_list[$key]);
                }
            }
        } */

        return new ArrayList<JeproLabCarrierModel>(); //carrierList;
    }

    public boolean setGroups(List<Integer> groups){
        return  setGroups(groups, true);
    }

    /**
     * Set Carrier Groups.
     *
     * @param groups    Carrier Groups
     * @param delete    Delete all previously Carrier Groups which
     *                      were linked to this Carrier
     *
     * @return bool Whether the Carrier Groups have been successfully set
     */
    public boolean setGroups(List<Integer> groups, boolean delete){
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        if (delete) {
            String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_group") + " AS carrier_group WHERE ";
            query += JeproLabDataBaseConnector.quoteName("carrier_id") + " = " + this.carrier_id;

            dataBaseObject.query(query, false);
        }

        if (groups.isEmpty()){
            return true;
        }

        String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier_group") + "(";
        query += JeproLabDataBaseConnector.quoteName("carrier_id") + ", " + JeproLabDataBaseConnector.quoteName("group_id");
        query += ") VALUES (";
        boolean result = true;
        for(int groupId : groups) {
            result &= dataBaseObject.query((query + this.carrier_id + ", " + groupId + ") "), true);
        }
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Moves a carrier.
     *
     * @param way      Up (1) or Down (0)
     * @param position Current position of the Carrier
     *
     * @return bool Whether the update was successful
     */
    public boolean updatePosition(int way, int position){
        String query  = "SELECT " + JeproLabDataBaseConnector.quoteName("carrier_id") + ", " + JeproLabDataBaseConnector.quoteName("position");
        query += " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " WHERE " + JeproLabDataBaseConnector.quoteName("deleted");
        query += " = 0 ORDER BY " + JeproLabDataBaseConnector.quoteName("position") + " ASC";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet positionSet = dataBaseObject.loadObjectList(query);
        if (positionSet != null) {
            Map<String, Integer> movedCarrier = null;
            try {
                while (positionSet.next()) {
                    if (positionSet.getInt("carrier_id") == this.carrier_id) {
                        movedCarrier = new HashMap<>();
                        movedCarrier.put("carrier_id", positionSet.getInt("carrier_id"));
                        movedCarrier.put("position", positionSet.getInt("position"));
                    }
                }

                if (movedCarrier != null) {
                    query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " SET ";
                    query += JeproLabDataBaseConnector.quoteName("position") + " = " + JeproLabDataBaseConnector.quoteName("position");
                    query += (way == 1 ? "- 1" : "+ 1") + " WHERE " + JeproLabDataBaseConnector.quoteName("position");
                    query += (way == 1 ? "> " + movedCarrier.get("position") + " AND " + JeproLabDataBaseConnector.quoteName("position") +
                        " <= " + position : " < " + movedCarrier.get("position") + " AND " + JeproLabDataBaseConnector.quoteName("position") +
                        " >= " + position + " AND " + JeproLabDataBaseConnector.quoteName("deleted") + " = 0");
                    boolean result = dataBaseObject.query(query, false);

                    query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_carrier") + " SET ";
                    query += JeproLabDataBaseConnector.quoteName("position") + " = " + position + " WHERE ";
                    query += JeproLabDataBaseConnector.quoteName("carrier_id") + " = " + movedCarrier.get("carrier_id");

                    result &= dataBaseObject.query(query, false);
                    return result;
                }
            } catch (SQLException ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
            } finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }else{
            closeDataBaseConnection(dataBaseObject);
        }
        return false;
    }


    public static boolean assignGroupToAllCarriers(List<Integer> groupList){
        return assignGroupToAllCarriers(groupList, new ArrayList<>());
    }

    /**
     * Assign one (ore more) group to all carriers.
     *
     *
     * @param groupList Group ID or array of Group IDs
     * @param exception     List of Carrier IDs to ignore
     *
     * @return bool
     */
    public static boolean assignGroupToAllCarriers(List<Integer> groupList, List<String> exception){
        /*if (!is_array($id_group_list)) {
            $id_group_list = array($id_group_list);
        }

        $id_group_list = array_map('intval', $id_group_list);
        $exception = array_map('intval', $exception);

        Db::getInstance()->execute('
        DELETE FROM `'._DB_PREFIX_.'carrier_group`
        WHERE `id_group` IN ('.implode(',', $id_group_list).')');

        $carrier_list = Db::getInstance()->executeS('
        SELECT id_carrier FROM `'._DB_PREFIX_.'carrier`
        WHERE deleted = 0
        '.(is_array($exception) && count($exception) > 0 ? 'AND id_carrier NOT IN ('.implode(',', $exception).')' : ''));

        if ($carrier_list) {
            $data = array();
            foreach ($carrier_list as $carrier) {
                foreach ($id_group_list as $id_group) {
                    $data[] = array(
                        'id_carrier' => $carrier['id_carrier'],
                        'id_group' => $id_group,
                    );
                }
            }

            return Db::getInstance()->insert('carrier_group', $data, false, false, Db::INSERT);
        } */

        return true;
    }
}
