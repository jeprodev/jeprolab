package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabGroupModel extends JeproLabModel {
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
    protected static Map<Integer, Integer>group_price_display_method = new HashMap<>();

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

    public static int getPriceDisplayMethod(int groupId){
        if (!JeproLabGroupModel.group_price_display_method.containsKey(groupId)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("price_display_method") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_group");
            query += " WHERE " + staticDataBaseObject.quoteName("group_id") + " = " + groupId;

            staticDataBaseObject.setQuery(query);

            JeproLabGroupModel.group_price_display_method.put(groupId, (int)staticDataBaseObject.loadValue("price_display_method"));
        }
        return JeproLabGroupModel.group_price_display_method.get(groupId);
    }

    public static float getReductionByGroupId(int groupId){
        String cacheKey = "jeprolab_group_get_reduction_group_" + groupId;
        if (!JeproLabGroupModel.cache_reduction.containsKey(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("reduction") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_group");
            query += " WHERE " + staticDataBaseObject.quoteName("group_id") + " = " + groupId;

            staticDataBaseObject.setQuery(query);
            float reduction = (float)staticDataBaseObject.loadValue("reduction");
            JeproLabGroupModel.cache_reduction.put(cacheKey, reduction);
        }
        return (float)JeproLabGroupModel.cache_reduction.get(cacheKey);
    }

    public static List<JeproLabGroupModel> getGroups(int langId){
        return getGroups(langId, 0);
    }

    public static List<JeproLabGroupModel> getGroups(int langId, int labId){
        String labCriteria = "";
        if (labId > 0) {
            labCriteria = JeproLabLaboratoryModel.addSqlAssociation("group");
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT DISTINCT grp." + staticDataBaseObject.quoteName("group_id") + ", grp." + staticDataBaseObject.quoteName("reduction");
        query += ", grp." + staticDataBaseObject.quoteName("price_display_method") + ", grp_lang." + staticDataBaseObject.quoteName("name") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_group") + " AS grp LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_group_lang");
        query += " AS grp_lang ON (grp." + staticDataBaseObject.quoteName("group_id") + " = grp_lang." + staticDataBaseObject.quoteName("group_id");
        query += " AND grp_lang." + staticDataBaseObject.quoteName("lang_id") + " = " +  langId + ") " + labCriteria + " ORDER BY grp.";
        query += staticDataBaseObject.quoteName("group_id") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet groupSet = staticDataBaseObject.loadObjectList();
        List<JeproLabGroupModel> groupList = new ArrayList<>();

        try{
            JeproLabGroupModel group;
            while(groupSet.next()){
                group = new JeproLabGroupModel();
                group.group_id = groupSet.getInt("group_id");
                group.reduction = groupSet.getFloat("reduction");
                group.price_display_method = groupSet.getInt("price_display_method");
                if(langId > 0){
                    group.name.put("lang_" + langId, groupSet.getString("name"));
                }
                //group = groupSet.get("");
                groupList.add(group);
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
        return groupList;
    }




    public static class JeproLabGroupReductionModel extends JeproLabModel{
        public int group_id;
        public int category_id;
        public float reduction;

        protected static Map<String, Float> reduction_cache = new HashMap<>();

        public JeproLabGroupReductionModel(int groupReductionId){

        }

        public static float getValueForAnalyze(int analyzeId, int groupId){
            if(!JeproLabGroupModel.isFeaturePublished()){
                return 0;
            }
            String cacheKey =  "jeprolab_get_value_for_analyze_" + analyzeId + "_" + groupId;

            if (!JeproLabGroupReductionModel.reduction_cache.containsKey(cacheKey)) {
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }

                String query = "SELECT "  + staticDataBaseObject.quoteName("reduction") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_group_reduction_cache");
                query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND " + staticDataBaseObject.quoteName("group_id") + " = " +  groupId;

                staticDataBaseObject.quoteName(query);
                float reduction = (float)staticDataBaseObject.loadValue("reduction");
                JeproLabGroupReductionModel.reduction_cache.put(cacheKey, reduction);
            }
            // Should return string (decimal in database) and not a float
            return JeproLabGroupReductionModel.reduction_cache.get(cacheKey);
        }

        public static boolean setAnalyzeReduction(int analyzeId){
            return setAnalyzeReduction(analyzeId, 0, 0, 0);
        }

        public static boolean setAnalyzeReduction(int analyzeId, int groupId){
            return setAnalyzeReduction(analyzeId, groupId, 0, 0);
        }

        public static boolean setAnalyzeReduction(int analyzeId, int groupId, int categoryId){
            return setAnalyzeReduction(analyzeId, groupId, categoryId, 0);
        }

        public static boolean setAnalyzeReduction(int analyzeId, int groupId, int categoryId, float reduction){
            boolean result = true;
            JeproLabGroupReductionModel.deleteAnalyzeReduction(analyzeId);

            List<Integer> categories = JeproLabAnalyzeModel.getAnalyzeCategories(analyzeId);

            if (categories.size() > 0) {
                for(int id : categories){
                    ResultSet reductions = JeproLabGroupReductionModel.getGroupsByCategoryId(id);
                    if (reductions != null){
                        try {
                            JeproLabGroupReductionModel currentGroupReduction;
                            while (reductions.next()) {
                                currentGroupReduction = new JeproLabGroupReductionModel(reductions.getInt("group_reduction_id"));
                                result &= currentGroupReduction.setCache();
                            }
                        }catch (SQLException ignored){
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
            }
            return result;
        }

        public static void deleteAnalyzeReduction(int analyzeId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_group_reduction_cache") + " WHERE ";
            query += staticDataBaseObject.quoteName("analyze_id") + " = "+ analyzeId;

            staticDataBaseObject.setQuery(query);
            staticDataBaseObject.query(false);
        }

        public static ResultSet getGroupsReductionByCategoryId(int categoryId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT gr." + staticDataBaseObject.quoteName("group_reduction_id") + " as group_reduction_id, group_id FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_group_reduction") + " AS gr WHERE " + staticDataBaseObject.quoteName("category_id");
            query += " = " + categoryId;

            staticDataBaseObject.setQuery(query);

            return staticDataBaseObject.loadObjectList();
        }

        public static ResultSet getGroupsByCategoryId(int categoryId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT gr." + staticDataBaseObject.quoteName("group_id") + " AS group_id , gr." + staticDataBaseObject.quoteName("reduction");
            query += "AS group_reduction_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_group_reduction") + " AS gr WHERE " + staticDataBaseObject.quoteName("category_id");
            query += " = "  + categoryId;

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.loadObjectList();
        }

        protected boolean setCache(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT category_analyze." + dataBaseObject.quoteName("analyze_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_analyzecategory");
            query += " category_analyze WHERE category_analyze." + dataBaseObject.quoteName("category_id")+ " = "+ this.category_id;

            dataBaseObject.setQuery(query);
            ResultSet analyzeSet = dataBaseObject.loadObjectList();
            boolean result = true;
            if(analyzeSet != null){
                try{
                    while (analyzeSet.next()){
                        query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_analyze_group_reduction_cache") + "(" + dataBaseObject.quoteName("analyze_id");
                        query += ", " + dataBaseObject.quoteName("group_id") + ", " + dataBaseObject.quoteName("reduction") + ") VALUES (" + analyzeSet.getInt("analyze_id");
                        query += ", " + this.group_id + ", " + this.reduction + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("reduction") + " = IF ( VALUES(";
                        query += dataBaseObject.quoteName("reduction") + ") > " + dataBaseObject.quoteName("reduction") + ", VALUES(" + dataBaseObject.quoteName("reduction");
                        query += "), " + dataBaseObject.quoteName("reduction") + ")";

                        dataBaseObject.setQuery(query);
                        result &= dataBaseObject.query(false);
                    }
                    return result;
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
            return true;
        }



    }

}
