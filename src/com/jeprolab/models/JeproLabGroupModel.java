package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.data.cache.JeproLabDataCache;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.Date;
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
public class JeproLabGroupModel extends JeproLabModel{
    public int group_id;

    public int language_id;

    public int laboratory_id;

    /** @var string Lastname */
    public Map<String, String> name = new HashMap<>();

    public int members;

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
                String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group") + " AS grp ";
                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                closeDataBaseConnection(dataBaseObject);
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

    public static List<JeproLabGroupModel> getGroups(int langId){
        return getGroups(langId, 0);
    }

    public static List<JeproLabGroupModel> getGroups(int langId, int labId){
        String labCriteria = "";
        if (labId > 0) {
            labCriteria = JeproLabLaboratoryModel.addSqlAssociation("group");
        }

        String query = "SELECT DISTINCT grp." + JeproLabDataBaseConnector.quoteName("group_id") + ", grp." + JeproLabDataBaseConnector.quoteName("reduction");
        query += ", grp." + JeproLabDataBaseConnector.quoteName("price_display_method") + ", grp_lang." + JeproLabDataBaseConnector.quoteName("name") + " FROM ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_group") + " AS grp LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group_lang");
        query += " AS grp_lang ON (grp." + JeproLabDataBaseConnector.quoteName("group_id") + " = grp_lang." + JeproLabDataBaseConnector.quoteName("group_id");
        query += " AND grp_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " +  langId + ") " + labCriteria + " ORDER BY grp.";
        query += JeproLabDataBaseConnector.quoteName("group_id") + " ASC";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet groupSet = dataBaseObject.loadObjectList(query);
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
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }finally {
            try {
                JeproLabFactory.removeConnection(dataBaseObject);
            }catch (Exception ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
            }
        }
        return groupList;
    }

    public static float getReductionByGroupId(int groupId){
        String cacheKey = "jeprolab_group_get_reduction_group_" + groupId;
        if (!JeproLabGroupModel.cache_reduction.containsKey(cacheKey)) {
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("reduction") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("group_id") + " = " + groupId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            float reduction = (float)dataBaseObject.loadValue(query, "reduction");
            JeproLabGroupModel.cache_reduction.put(cacheKey, reduction);
            closeDataBaseConnection(dataBaseObject);
        }
        return JeproLabGroupModel.cache_reduction.get(cacheKey);
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

    public static int getPriceDisplayMethod(int groupId){
        if (!JeproLabGroupModel.group_price_display_method.containsKey(groupId)){
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("price_display_method") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("group_id") + " = " + groupId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            JeproLabGroupModel.group_price_display_method.put(groupId, (int)dataBaseObject.loadValue(query, "price_display_method"));
            closeDataBaseConnection(dataBaseObject);
        }
        return JeproLabGroupModel.group_price_display_method.get(groupId);
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

            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("lab_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group_lab");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("group_id") + " = " + this.group_id + " AND lab_id = " + labId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            boolean isAssociated = ((int)dataBaseObject.loadValue(query, "lab_id")) > 0;
            closeDataBaseConnection(dataBaseObject);

            JeproLabCache.getInstance().store(cacheKey, isAssociated);
            return isAssociated;
        }

        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static class JeproLabGroupReductionModel extends JeproLabModel {
        public int group_reduction_id;

        public int group_id;

        public int category_id;
        public float reduction;

        protected static Map<String, Float> reduction_cache = new HashMap<>();

        public JeproLabGroupReductionModel(){
            this(0);
        }

        public JeproLabGroupReductionModel(int groupReductionId){

        }

        public static List<JeproLabGroupReductionModel> getGroupsByCategoryId(int categoryId){
            String query = "SELECT gr." + JeproLabDataBaseConnector.quoteName("group_id") + " AS group_id , gr." + JeproLabDataBaseConnector.quoteName("reduction");
            query += "AS group_reduction_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group_reduction") + " AS gr WHERE " + JeproLabDataBaseConnector.quoteName("category_id");
            query += " = "  + categoryId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            List<JeproLabGroupReductionModel> groupList = new ArrayList<>();
            ResultSet groupSet = dataBaseObject.loadObjectList(query);
            if(groupSet != null){
                try {
                    JeproLabGroupReductionModel currentGroupReduction;
                    while (groupSet.next()) {
                        currentGroupReduction = new JeproLabGroupReductionModel();
                        currentGroupReduction.group_reduction_id = groupSet.getInt("group_reduction_id");
                        currentGroupReduction.group_id = groupSet.getInt("group_id");
                        currentGroupReduction.reduction = groupSet.getFloat("reduction");
                        groupList.add(currentGroupReduction);
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            closeDataBaseConnection(dataBaseObject);
            return groupList;
        }

        public static List<JeproLabGroupReductionModel> getGroupsReductionByCategoryId(int categoryId){
            String query = "SELECT gr." + JeproLabDataBaseConnector.quoteName("group_reduction_id") + " as group_reduction_id, group_id FROM ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_group_reduction") + " AS gr WHERE " + JeproLabDataBaseConnector.quoteName("category_id");
            query += " = " + categoryId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            ResultSet resultSet = dataBaseObject.loadObjectList(query);
            List<JeproLabGroupReductionModel> reductions = new ArrayList<>();

            if(resultSet != null){
                try {
                    JeproLabGroupReductionModel reduction;
                    while (resultSet.next()){
                        reduction = new JeproLabGroupReductionModel();
                        reduction.group_id = resultSet.getInt("group_id");
                        reduction.group_reduction_id = resultSet.getInt("group_id");
                        reductions.add(reduction);
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return reductions;
        }

        public static boolean deleteCategory(int categoryId){
            String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_group_reduction") + " WHERE ";
            query += JeproLabDataBaseConnector.quoteName("category_id") + " = " + categoryId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            boolean result = dataBaseObject.query(query, false);
            closeDataBaseConnection(dataBaseObject);
            return result;
        }

        public static float getValueForAnalyze(int analyzeId, int groupId){
            if(!JeproLabGroupModel.isFeaturePublished()){
                return 0;
            }
            String cacheKey =  "jeprolab_get_value_for_analyze_" + analyzeId + "_" + groupId;

            if (!JeproLabGroupReductionModel.reduction_cache.containsKey(cacheKey)) {
                String query = "SELECT "  + JeproLabDataBaseConnector.quoteName("reduction") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_group_reduction_cache");
                query += " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId + " AND " + JeproLabDataBaseConnector.quoteName("group_id") + " = " +  groupId;

                JeproLabDataBaseConnector.quoteName(query);
                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                float reduction = (float)dataBaseObject.loadValue(query, "reduction");
                JeproLabGroupReductionModel.reduction_cache.put(cacheKey, reduction);
            }
            // Should return string (decimal in database) and not a float
            return JeproLabGroupReductionModel.reduction_cache.get(cacheKey);
        }

        public static boolean deleteAnalyzeReduction(int analyzeId){
            String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_group_reduction_cache") + " WHERE ";
            query += JeproLabDataBaseConnector.quoteName("analyze_id") + " = "+ analyzeId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            return dataBaseObject.query(query, false);
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
            List<JeproLabGroupReductionModel> reductions;
            if (categories.size() > 0) {
                for(int id : categories){
                    reductions = JeproLabGroupReductionModel.getGroupsByCategoryId(id);
                    for(JeproLabGroupReductionModel reductionModel :reductions){
                        result &= reductionModel.setCache();
                    }
                }
            }
            return result;
        }

        protected boolean setCache(){
            String query = "SELECT category_analyze." + JeproLabDataBaseConnector.quoteName("analyze_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyzecategory");
            query += " category_analyze WHERE category_analyze." + JeproLabDataBaseConnector.quoteName("category_id")+ " = "+ this.category_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet analyzeSet = dataBaseObject.loadObjectList(query);
            boolean result = true;
            if(analyzeSet != null){
                try{
                    while (analyzeSet.next()){
                        query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_group_reduction_cache") + "(" + JeproLabDataBaseConnector.quoteName("analyze_id");
                        query += ", " + JeproLabDataBaseConnector.quoteName("group_id") + ", " + JeproLabDataBaseConnector.quoteName("reduction") + ") VALUES (" + analyzeSet.getInt("analyze_id");
                        query += ", " + this.group_id + ", " + this.reduction + ") ON DUPLICATE KEY UPDATE " + JeproLabDataBaseConnector.quoteName("reduction") + " = IF ( VALUES(";
                        query += JeproLabDataBaseConnector.quoteName("reduction") + ") > " + JeproLabDataBaseConnector.quoteName("reduction") + ", VALUES(" + JeproLabDataBaseConnector.quoteName("reduction");
                        query += "), " + JeproLabDataBaseConnector.quoteName("reduction") + ")";

                        //dataBaseObject.setQuery(query);
                        result &= dataBaseObject.query(query, false);
                    }
                    return result;
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return true;
        }
    }
}
