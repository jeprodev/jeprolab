package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabGroupModel extends JeproLabModel{
    public int group_id;

    public int language_id;

    public int laboratory_id;

    /** @var string Lastname */
    public Map<String, String> name = new HashMap<>();

    /** @var string Reduction */
    public float reduction;

    /** @var int Price display method (JeproLabTaxManagerFactory inc/JeproLabTaxManagerFactory exc) */
    public int price_display_method;

    /** @var bool Show prices */
    public boolean show_prices = true;

    /** @var string Object creation date */
    public Date date_add;

    /** @var string Object last modification date */
    public Date date_upd;

    protected static boolean group_feature_active = false;

    protected static Map<Integer, JeproLabGroupModel> groups = new HashMap<>();
    protected static int unidentified_group = 0;
    protected static int customer_group = 0;

    protected static Map<String, Float> cache_reduction = new HashMap<>();
    protected static Map<Integer, Integer> group_price_display_method = new HashMap<>();

    public JeproLabGroupModel(){ this(0, 0, 0); }

    public JeproLabGroupModel(int groupId){ this(groupId, 0, 0); }

    public JeproLabGroupModel(int groupId, int langId){ this(groupId, langId, 0); }

    public JeproLabGroupModel(int groupId, int langId, int labId){
        if(langId > 0){
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(groupId > 0){
            String cacheKey = "jeprolab_group_model_" + groupId + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_group") + " AS grp ";
            }
        }
        //parent::__construct($id, $id_lang, $id_shop);
        if (this.group_id > 0 && !group_price_display_method.containsKey(this.group_id)){
            group_price_display_method.put(this.group_id, this.price_display_method);
        }
    }

    /**
     * This method is allow to know if a feature is used or active
     * @since 1.5.0.1
     * @return bool
     */
    public static boolean isFeaturePublished() {
        if (!group_feature_active) {
            group_feature_active = JeproLabSettingModel.getIntValue("group_feature_active") > 0;
        }
        return group_feature_active;
    }

    /**
     * Return current group object
     * Use context
     *
     * @return group Group object
     */
    public static JeproLabGroupModel getCurrent(){
        if (unidentified_group == 0) {
            unidentified_group = JeproLabSettingModel.getIntValue("unidentified_group");
        }

        if (customer_group == 0) {
            customer_group = JeproLabSettingModel.getIntValue("customer_group");
        }

        JeproLabCustomerModel customer = JeproLabContext.getContext().customer;
        int groupId;
        if (customer != null && customer.customer_id > 0){
            groupId = customer.default_group_id;
        } else {
            groupId = unidentified_group;
        }

        if (!groups.containsKey(groupId)) {
            groups.put(groupId, new JeproLabGroupModel(groupId));
        }

        if (!groups.get(groupId).isAssociatedToLaboratory(JeproLabContext.getContext().laboratory.laboratory_id)) {
            groupId = customer_group;
            if (!groups.containsKey(groupId)){
                groups.put(groupId, new JeproLabGroupModel(groupId));
            }
        }

        return groups.get(groupId);
    }

    public boolean isAssociatedToLaboratory(){
        return isAssociatedToLaboratory(0);
    }

    /**
     * Checks if current object is associated to a shop.
     *
     * @param labId laboratory filter
     * @return bool
     */
    public boolean isAssociatedToLaboratory(int labId){
        if (labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        String cacheKey = "jeprolab_model_laboratory_group_" + this.group_id + "_" + labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("lab_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_group_lab");
            query += " WHERE " + dataBaseObject.quoteName("group_id") + " = " + this.group_id + " AND lab_id = " + labId;

            dataBaseObject.setQuery(query);

            boolean isAssociated = ((int)dataBaseObject.loadValue("lab_id")) > 0;

            JeproLabCache.getInstance().store(cacheKey, isAssociated);
            return isAssociated;
        }

        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }
}
