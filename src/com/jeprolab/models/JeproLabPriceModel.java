package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import jdk.nashorn.internal.runtime.Context;
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
public class JeproLabPriceModel extends JeproLabModel{
    public int price_id;
    public float price;

    public float price_with_out_reduction = 0;

    public float total;
    public float total_with_tax;
    public float price_with_tax;
    public float price_with_out_tax;

    public float price_with_reduction = 0;
    public float price_with_reduction_without_tax = 0;

    public boolean reduction_applied;

    public boolean quantity_discount_applied;

    public JeproLabPriceModel(){
        this(0);
    }

    public JeproLabPriceModel(int priceId){

    }

    public static class JeproLabSpecificPriceModel extends JeproLabModel{
        public int analyze_id;
        public int specific_price_id = 0;
        public int specific_price_rule_id = 0;
        public int cart_id = 0;
        public int analyze_attribute_id;
        public int laboratory_id;
        public int laboratory_group_id;
        public int currency_id;
        public int country_id;
        public int group_id;
        public int customer_id;

        public int from_quantity;
        public float reduction;
        public float reduction_tax = 1;
        public String reduction_type;
        public Date from;
        public Date to;

        public float price = 0;

        protected static Map<String, JeproLabSpecificPriceModel> _specificPriceCache = new HashMap<>();
        protected static Map<String, List<Integer>> _filterOutCache = new HashMap<>();
        protected static Map<Integer, String> _cache_priorities = new HashMap<>();
        protected static Map<String, Boolean> _no_specific_values = new HashMap<>();

        private static boolean feature_active = false;


        public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity){
            return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, 0, 0, 0, 0);
        }

        public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId){
            return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, 0, 0, 0);
        }

        public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId, int customerId){
            return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, customerId, 0, 0);
        }

        public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId, int customerId, int cartId){
            return getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, customerId, cartId, 0);
        }

        public static JeproLabSpecificPriceModel getSpecificPrice(int analyzeId, int labId, int currencyId, int countryId, int groupId, int quantity, int analyzeAttributeId, int customerId, int cartId, int realQuantity){
            if (!JeproLabSpecificPriceModel.isFeaturePublished()) {
                return new JeproLabSpecificPriceModel();
            }
            /*
            ** The date is not taken into account for the cache, but this is for the better because it keeps the consistency for the whole script.
            ** The price must not change between the top and the bottom of the page
            */

            String cacheKey = analyzeId + "_" + labId + "_" + currencyId + "_" + countryId + "_" + groupId + "_" + quantity + "_" + analyzeAttributeId + "_" + cartId + "_" + customerId + "_" + realQuantity;
            if (!JeproLabSpecificPriceModel._specificPriceCache.containsKey(cacheKey)){
                String extraQuery = JeproLabSpecificPriceModel.computeExtraConditions(analyzeId, analyzeAttributeId, customerId, cartId);
                String query = "SELECT *, " + JeproLabSpecificPriceModel.getScoreQuery(analyzeId, labId, currencyId, countryId, groupId, customerId);
                query += " FROM " + JeproLabDataBaseConnector.quoteName("specific_price") + " WHERE " + JeproLabDataBaseConnector.quoteName("lab_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, labId) + " AND " + JeproLabDataBaseConnector.quoteName("currency_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, currencyId) + " AND " + JeproLabDataBaseConnector.quoteName("country_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, countryId) + " " + JeproLabDataBaseConnector.quoteName("group_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, groupId) + extraQuery + " AND IF(" + JeproLabDataBaseConnector.quoteName("from_quantity");
                query += " > 1, " + JeproLabDataBaseConnector.quoteName("from_quantity") + ", 0) <= ";
                query += (JeproLabSettingModel.getIntValue("quantity_discount_on_combination") > 0 || (cartId < 0) || (realQuantity <= 0)) ? quantity : Math.max(1, realQuantity);
                query += " ORDER BY " + JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + " DESC, " + JeproLabDataBaseConnector.quoteName("from_quantity") + " DESC, ";
                query += JeproLabDataBaseConnector.quoteName("specific_price_rule_id") + " ASC, " + JeproLabDataBaseConnector.quoteName("score") + " DESC, ";
                query += JeproLabDataBaseConnector.quoteName("to") + " DESC, " + JeproLabDataBaseConnector.quoteName("from") + " DESC";

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                ResultSet resultSet = dataBaseObject.loadObjectList(query);
                JeproLabSpecificPriceModel specificPrice = new JeproLabSpecificPriceModel();
                try {
                    if(resultSet.next()){
                        //todo set fields
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

                JeproLabSpecificPriceModel._specificPriceCache.put(cacheKey, specificPrice);
            }
            return JeproLabSpecificPriceModel._specificPriceCache.get(cacheKey);
        }

        /**
         * This method is allow to know if a feature is used or active
         *
         * @return bool
         */
        public static boolean isFeaturePublished(){
            if (!feature_active) {
                feature_active = JeproLabSettingModel.getIntValue("specific_price_feature_active") > 0;
            }
            return feature_active;
        }

        private static String formatIntInQuery(int firstValue, int secondValue) {
            if (firstValue != secondValue) {
                return " IN (" + firstValue + ", " + secondValue + ")";
            } else {
                return " = " + firstValue;
            }
        }

        /**
         * score generation for quantity discount
         */
        protected static String getScoreQuery(int analyzeId, int labId, int currencyId, int countryId, int groupId, int customerId){
            String select = "(";

            String priority = JeproLabSpecificPriceModel.getPriority(analyzeId);
            String[] priorityArr = priority.split(";");
            int index = 0;
            for(String field : priorityArr) {
                if (!field.equals("")) {
                    select += " IF (" + JeproLabDataBaseConnector.quoteName(field) + " = ";
                    switch (field) {
                        case "customer_id":
                            select += customerId;
                            break;
                        case "group_id":
                            select += groupId;
                            break;
                        case "country_id":
                            select += countryId;
                            break;
                        case "currency_id":
                            select += currencyId;
                            break;
                        case "lab_id":
                            select += labId;
                            break;
                    }

                    select += ", " + Math.pow(2, index + 1) + ", 0) + ";
                }
                index++;
            }
            select = select.substring(0, select.length() - 2)+ ") AS " + JeproLabDataBaseConnector.quoteName("score");
            //closeDataBaseConnection(dataBaseObject);
            return select ;
        }

        public static String getPriority(int analyzeId){
            if (!JeproLabSpecificPriceModel.isFeaturePublished()) {
                return JeproLabSettingModel.getStringValue("specific_price_priorities"); //'PS_SPECIFIC_PRICE_PRIORITIES'));
            }

            if (!JeproLabSpecificPriceModel._cache_priorities.containsKey(analyzeId)){

                String query = "SELECT " + JeproLabDataBaseConnector.quoteName("priority") + ", " + JeproLabDataBaseConnector.quoteName("specific_price_priority_id");
                query += " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_priority") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id");
                query += " = " + analyzeId + " ORDER BY " + JeproLabDataBaseConnector.quoteName("specific_price_priority_id") + " DESC ";

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

                JeproLabSpecificPriceModel._cache_priorities.put(analyzeId, dataBaseObject.loadStringValue(query, "priority"));
                closeDataBaseConnection(dataBaseObject);
            }

            String priority = JeproLabSpecificPriceModel._cache_priorities.get(analyzeId);

            if (priority.equals("")) {
                priority = JeproLabSettingModel.getStringValue("specific_price_priorities");
            }
            priority = "customer_id;" + priority;

            return priority;
        }

        protected static String computeExtraConditions(int analyzeId, int analyzeAttributeId, int customerId, int cartId){
            return computeExtraConditions(analyzeId, analyzeAttributeId, customerId, cartId, null, null);
        }

        protected static String  computeExtraConditions(int analyzeId, int analyzeAttributeId, int customerId, int cartId, Date beginning){
            return computeExtraConditions(analyzeId, analyzeAttributeId, customerId, cartId, beginning, null);
        }

        /**
         * Remove or add useless fields value depending on the values in the database (cache friendly)
         *
         * @param analyzeId analyze id
         * @param analyzeAttributeId analyze attribute id
         * @param cartId cart id
         * @param beginning beginning
         * @param ending period ending
         * @return string
         */
        protected static String  computeExtraConditions(int analyzeId, int analyzeAttributeId, int customerId, int cartId, Date beginning, Date ending){
            Date firstDate = JeproLabTools.getDate();
            Date lastDate = JeproLabTools.getDate();
            Date now = JeproLabTools.getDate();
            if (beginning == null) {
                beginning = now;
            }
            if (ending == null) {
                ending = now;
            }

            String extraQuery = "";

            if (analyzeId > 0) {
                extraQuery += JeproLabSpecificPriceModel.filterOutField("analyze_id", analyzeId);
            }

            if (customerId > 0) {
                extraQuery += JeproLabSpecificPriceModel.filterOutField("customer_id", customerId);
            }

            if (analyzeAttributeId > 0) {
                extraQuery += JeproLabSpecificPriceModel.filterOutField("analyze_attribute_id", analyzeAttributeId);
            }

            if (cartId > 0) {
                extraQuery += JeproLabSpecificPriceModel.filterOutField("cart_id", cartId);
            }
            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            int fromSpecificCount, toSpecificCount;
            if (ending == now && beginning == now) {
                String cacheKey = "jeprolab_specific_price_compute_extra_conditions" + firstDate.toString() + "_" + lastDate.toString();

                if (!JeproLabSpecificPriceModel._filterOutCache.containsKey(cacheKey)) {
                    String queryFromCount = "SELECT 1 FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price");
                    queryFromCount += " WHERE " + JeproLabDataBaseConnector.quoteName("from") + " BETWEEN '" + firstDate.toString() + "' AND '" + lastDate.toString() + "'";
                    //dataBaseObject.setQuery(queryFromCount);
                    fromSpecificCount = (int)dataBaseObject.loadValue(queryFromCount, "1"); //Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query_from_count);

                    String queryToCount = "SELECT 1 FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price") + " WHERE ";
                    queryToCount += JeproLabDataBaseConnector.quoteName("to") + " BETWEEN '" + firstDate.toString() + "' AND '" + lastDate.toString() + "'";
                    JeproLabDataBaseConnector.quoteName(queryToCount);

                    toSpecificCount  = (int)dataBaseObject.loadValue(queryToCount, "1");
                    List<Integer> specificCount = new ArrayList<>();
                    specificCount.add(fromSpecificCount);
                    specificCount.add(toSpecificCount);
                    JeproLabSpecificPriceModel._filterOutCache.put(cacheKey, specificCount);
                } else {
                    List specificCount = JeproLabSpecificPriceModel._filterOutCache.get(cacheKey);
                    fromSpecificCount = (int)specificCount.get(0);
                    toSpecificCount = (int)specificCount.get(1);
                }
            } else {
                fromSpecificCount = toSpecificCount = 1;
            }

            // if the from and to is not reached during the current day, just change $ending & $beginning to any date of the day to improve the cache
            if (fromSpecificCount < 0 && toSpecificCount < 0){
                ending = beginning = firstDate;
            }

            extraQuery += " AND (" + JeproLabDataBaseConnector.quoteName("from") + " = '0000-00-00 00:00:00' OR '" + beginning + "' >= ";
            extraQuery += JeproLabDataBaseConnector.quoteName("from") + ") AND (" + JeproLabDataBaseConnector.quoteName("to") + " = '0000-00-00 00:00:00' OR ' ";
            extraQuery += ending + "' <= " + JeproLabDataBaseConnector.quoteName("to") + ")";

            closeDataBaseConnection(dataBaseObject);

            return extraQuery;
        }

        protected static String filterOutField(String fieldName, int fieldValue){
            return filterOutField(fieldName, fieldValue, 1000);
        }

        /**
         * Remove or add a field value to a query if values are present in the database (cache friendly)
         *
         * @param fieldName field name
         * @param fieldValue field value
         * @param threshold threshold
         * @return string
         */
        protected static String filterOutField(String fieldName, int fieldValue, int threshold){
            String extraQuery = " AND " + JeproLabDataBaseConnector.quoteName(fieldName) + " = 0 ";
            if (fieldValue == 0 || JeproLabSpecificPriceModel._no_specific_values.containsKey(fieldName)){
                return extraQuery;
            }
            String cacheKey  = "jeprolab_specific_price_filter_out_field_" + fieldName + "_" + threshold;
            List<Integer> specificList = new ArrayList<>();

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            if (!JeproLabSpecificPriceModel._filterOutCache.containsKey(cacheKey)) {
                String queryCount = "SELECT COUNT(DISTINCT " + JeproLabDataBaseConnector.quoteName(fieldName) + ") AS filter_out FROM ";
                queryCount += JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price") + " WHERE " + JeproLabDataBaseConnector.quoteName(fieldName) + " != 0";

                int specificCount = (int)dataBaseObject.loadValue(queryCount, "filter_out");
                if (specificCount == 0) {
                    JeproLabSpecificPriceModel._no_specific_values.put(fieldName, true);

                    return extraQuery;
                }
                if (specificCount < threshold) {
                    String query  = "SELECT DISTINCT " + JeproLabDataBaseConnector.quoteName(fieldName) + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price");
                    query += " WHERE " + JeproLabDataBaseConnector.quoteName(fieldName) + " != 0";
                    //dataBaseObject.setQuery(query);
                    ResultSet tmpSpecificSet = dataBaseObject.loadObjectList(query);

                    if(tmpSpecificSet != null) {
                        try {
                            while (tmpSpecificSet.next()) {
                                specificList.add(tmpSpecificSet.getInt(fieldName));
                            }
                        } catch (SQLException ignored) {
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        } finally {
                            try {
                                JeproLabFactory.removeConnection(dataBaseObject);
                            } catch (Exception ignored) {
                                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                            }
                        }
                    }
                }
                JeproLabSpecificPriceModel._filterOutCache.put(cacheKey, specificList);
            } else {
                specificList = (List)JeproLabSpecificPriceModel._filterOutCache.get(cacheKey);
            }

            // $specific_list is empty if the threshold is reached
            if (specificList.isEmpty() || specificList.contains(fieldValue)) {
                extraQuery = " AND " + JeproLabDataBaseConnector.quoteName(fieldName) + " " + JeproLabSpecificPriceModel.formatIntInQuery(0, fieldValue) +" ";
            }
            closeDataBaseConnection(dataBaseObject);

            return extraQuery;
        }

        public boolean add(){
            String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price") + "(" + JeproLabDataBaseConnector.quoteName("specific_price_rule_id");
            query += ", " + JeproLabDataBaseConnector.quoteName("analyze_id") + ", " + JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + ", " + JeproLabDataBaseConnector.quoteName("customer_id");
            query += ", " + JeproLabDataBaseConnector.quoteName("laboratory_id") + ", " + JeproLabDataBaseConnector.quoteName("country_id") + ", " + JeproLabDataBaseConnector.quoteName("currency_id") + ", ";
            query += JeproLabDataBaseConnector.quoteName("group_id") + ", " + JeproLabDataBaseConnector.quoteName("from_quantity") + ", " + JeproLabDataBaseConnector.quoteName("price") + ", " ;
            query += JeproLabDataBaseConnector.quoteName("reduction_type") + ", " + JeproLabDataBaseConnector.quoteName("reduction_tax") + ", " + JeproLabDataBaseConnector.quoteName("reduction") + ", ";
            query += JeproLabDataBaseConnector.quoteName("from") + ", " + JeproLabDataBaseConnector.quoteName("to") + ") VALUES ( " + this.specific_price_rule_id + ", ";
            query += this.analyze_id + ", " + this.analyze_attribute_id + ", " + this.customer_id +  ", " + this.laboratory_id + ", " + this.country_id + ", ";
            query += this.currency_id + ", " + this.group_id + ", " + this.from_quantity + ", " + this.price + ", " + this.reduction_type + ", " + this.reduction;
            query += ", " + this.reduction + ", " + this.from + ", " + this.to + ") ";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            boolean result = dataBaseObject.query(query, false);
            closeDataBaseConnection(dataBaseObject);
            return result;
        }

        public static boolean deleteByAnalyzeId(int analyzeId){
            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId;

            if(dataBaseObject.query(query, false)){
                JeproLabSettingModel.updateValue("specific_price_active", JeproLabSpecificPriceModel.isCurrentlyUsed("specific_price"));
                return true;
            }
            closeDataBaseConnection(dataBaseObject);
            return false;
        }

        public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId){
            return getSpecificPricesByAnalyzeId(analyzeId, 0, 0);
        }

        public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId, int analyzeAttributeId){
            return getSpecificPricesByAnalyzeId(analyzeId, analyzeAttributeId, 0);
        }

        public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId, int analyzeAttributeId, int cartId){

            String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id");
            query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND analyze_attribute_id = " + analyzeAttributeId : "" );
            query += (cartId > 0 ? " AND cart_id = " + cartId : "" );

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet specificSet = dataBaseObject.loadObjectList(query);
            List<JeproLabSpecificPriceModel> specificList = new ArrayList<>();
            if(specificSet != null){
                try{
                    JeproLabSpecificPriceModel specificPrice;
                    while(specificSet.next()){
                        specificPrice = new JeproLabSpecificPriceModel();
                        specificPrice.specific_price_id = specificSet.getInt("specific_price_id");
                        specificPrice.specific_price_rule_id = specificSet.getInt("specific_price_rule_id");
                        specificPrice.cart_id = specificSet.getInt("cart_id");
                        specificPrice.analyze_id = specificSet.getInt("analyze_id");
                        specificPrice.analyze_attribute_id = specificSet.getInt("analyze_attribute_id");
                        specificPrice.laboratory_id = specificSet.getInt("laboratory_id");
                        specificPrice.laboratory_group_id = specificSet.getInt("laboratory_group_id");
                        specificPrice.currency_id = specificSet.getInt("currency_id");
                        specificPrice.country_id = specificSet.getInt("country_id");
                        specificPrice.customer_id = specificSet.getInt("customer_id");
                        specificPrice.group_id = specificSet.getInt("group_id");
                        specificPrice.price = specificSet.getFloat("price");
                        specificPrice.from_quantity = specificSet.getInt("from_quantity");
                        specificPrice.reduction = specificSet.getFloat("reduction");
                        specificPrice.reduction_type = specificSet.getString("reduction_type");
                        specificPrice.from = specificSet.getDate("from");
                        specificPrice.to = specificSet.getDate("to");
                        specificList.add(specificPrice);
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
            return specificList;
        }

    }


    public static class JeproLabSpecificPriceRuleModel extends JeproLabModel {
        public int specific_price_rule_id;
        public String name;
        public int laboratory_id;
        public int currency_id;
        public int country_id;
        public int group_id;
        public int from_quantity;
        public float price;
        public float reduction;
        public float reduction_tax;
        public String reduction_type;
        public Date from;
        public Date to;

        protected static boolean rules_application_enable = true;


        public JeproLabSpecificPriceRuleModel(int specificPriceRuleId){

        }

        public static void applyAllRules(int analyzeId) {
            List<Integer> analyzeList = new ArrayList<>();
            analyzeList.add(analyzeId);
            applyAllRules(analyzeList);
        }

        /**
         * @param analyzeList analyze ids
         */
        public static void applyAllRules(List<Integer> analyzeList) {
            if (JeproLabSpecificPriceRuleModel.rules_application_enable) {
                String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_rule");
                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                ResultSet rulesSet = dataBaseObject.loadObjectList(query);
                if(rulesSet != null){
                    try{
                        JeproLabSpecificPriceRuleModel priceRule;
                        while(rulesSet.next()){
                            priceRule = new JeproLabSpecificPriceRuleModel(rulesSet.getInt("specific_price_rule_id"));
                            priceRule.apply(analyzeList);
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
            }
        }

        public void apply(){
            apply(null);
        }

        public void apply(List<Integer> analyzeList){
            if (JeproLabSpecificPriceRuleModel.rules_application_enable && analyzeList != null) {
                this.resetApplication(analyzeList);
                List<JeproLabAnalyzeModel> analyzes = this.getAffectedAnalyzes(analyzeList);
                for(JeproLabAnalyzeModel analyze : analyzes) {
                    JeproLabSpecificPriceRuleModel.applyRuleToAnalyze(this.specific_price_rule_id, analyze.analyze_id, analyze.analyze_attribute_id);
                }
            }
        }

        public boolean resetApplication(List<Integer> analyzeList){
            String where = "";
            if (analyzeList != null && analyzeList.size() > 0) {
                String listIds = "";
                for(int analyzeId : analyzeList){
                    listIds += analyzeId + ", ";
                }
                listIds = listIds.endsWith(", ") ? listIds.substring(0, listIds.length()-2) : listIds;
                where += " AN analyze_id IN (" + listIds + ") ";
            }
            String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price");
            query += "WHERE specific_price_rule_id = " + this.specific_price_rule_id + where;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            boolean result = dataBaseObject.query(query, false);
            closeDataBaseConnection(dataBaseObject);
            return result;
        }

        /**
         * Return the product list affected by this specific rule.
         *
         * @param analyzeList list limitation.
         * @return array Affected products list IDs.
         */
        public List<JeproLabAnalyzeModel> getAffectedAnalyzes(List<Integer> analyzeList){
            Map<Integer, List<JeproLabSpecificPriceConditionModel>> conditionsGroup = this.getConditions();
            int currentLabId = JeproLabContext.getContext().laboratory.laboratory_id;

            List<JeproLabAnalyzeModel> analyzeResult = new ArrayList<>();

            if (conditionsGroup.size() > 0) {
                List<JeproLabSpecificPriceConditionModel> conditioGroup;
                String select, from, leftJoin, join, where;
                for(Map.Entry conditionEntry  : conditionsGroup.entrySet()) {
                    conditioGroup = (List<JeproLabSpecificPriceConditionModel>)conditionEntry.getValue();
                    // Base request
                    select = "SELECT analyze." + JeproLabDataBaseConnector.quoteName("analyze_id");
                    from = " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " analyze ";
                    leftJoin = " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab (analyze_lab.";
                    leftJoin += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyze." + JeproLabDataBaseConnector.quoteName("analyze_id");
                    where = " WHERE analyze_lab." + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + currentLabId;


                    boolean attributesJoinAdded = false;

                    /*/ Add the conditions
                    for(JeproLabSpecificPriceConditionModel condition : conditionsGroup) {
                        if (condition.type.equals("attribute")){
                            if (!attributesJoinAdded) {
                                select += ", analyze_attribute." + JeproLabDataBaseConnector.quoteName("analyze_attribute_id");
                                leftJoin += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute ON analyze_attribute.";
                                leftJoin += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyze." + JeproLabDataBaseConnector.quoteName("analyze_id");
                                join += JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute", false);

                                attributesJoinAdded = true;
                            }

                            leftJoin += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute_combination") + " AS analyze_attribute_combination ";
                            leftJoin += " ON analyze_attribute_combination." + JeproLabDataBaseConnector.quoteName("attribute_id") + " = " + condition.value;
                            //leftJoin('product_attribute_combination', 'pac'.(int)$id_condition, 'pa.`id_product_attribute` = pac'.(int)$id_condition.'.`id_product_attribute`')
                            where += " AND analyze_attribute_combination."'pac'.(int)$id_condition.'.`id_attribute` = '.(int)$condition['value']);
                        } else if (condition.type.equals("manufacturer")) {
                            $query->where('p.id_manufacturer = '.(int)$condition['value']);
                        } else if (condition.type.equals("category")) {
                            $query->leftJoin('category_product', 'cp'.(int)$id_condition, 'p.`id_product` = cp'.(int)$id_condition.'.`id_product`')
                            ->where('cp'.(int)$id_condition.'.id_category = '.(int)$condition['value']);
                        } else if (condition.type.equals("supplier")){
                            /where += " EXISTS( SELECT analyze_supplier."(int)$id_condition. JeproLabDataBaseConnector.quoteName("analyze_id") + " FROM ";
                            where += JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_supplier") + " AS analyze_supplier "
                            `'._DB_PREFIX_.'product_supplier` `ps'.(int)$id_condition.'`
                            WHERE
                            `p`.`id_product` = `ps'.(int)$id_condition.'`.`id_product`
                            AND `ps'.(int)$id_condition.'`.`id_supplier` = '.(int)$condition['value'].'
                            )');
                        } else if ($condition['type'] == 'feature') {
                            leftJoin('feature_product', 'fp'.(int)$id_condition, 'p.`id_product` = fp'.(int)$id_condition.'.`id_product`')
                            ->where('fp'.(int)$id_condition.'.`id_feature_value` = '.(int)$condition['value']);
                        }
                    }

                    // analyzes limitation
                    if (analyzeList != null && analyzeList.size() > 0) {
                        $query->where('p.`id_product` IN ('.implode(', ', array_map('intval', $products)).')');
                    }

                    // Force the column id_product_attribute if not requested
                    if (!attributesJoinAdded) {
                        select += " NULL AS " + JeproLabDataBaseConnector.quoteName("analyze_attribute_id");
                    }

                    $result = array_merge($result, Db::getInstance()->executeS($query)); */
                }
            } else {
                // All products without conditions
               /* if (analyzeList != null && analyzeList.size() > 0) {
                    String query = "SELECT analyze." + JeproLabDataBaseConnector.quoteName("analyze_id") + ", NULL AS " + JeproLabDataBaseConnector.quoteName("analyze_attribute_id");
                    query += " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " AS analyze LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab");
                    query += " AS analyze_lab ON(analyze." + JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyze_lab." + JeproLabDataBaseConnector.quoteName("analyze_id");
                    query += " ) WHERE analyze_lab." + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + currentLabId + " AND analyze." + JeproLabDataBaseConnector.quoteName("analyze_id") + " IN (" + analyzeIds + ") ";

                    dataBaseObject.setQuery(query);
                    ResultSet resultSet = dataBaseObject.loadObject();

                    ->select('NULL as `id_product_attribute`')
                    ->from('product', 'p')
                    ->leftJoin('product_shop', 'ps', 'p.`id_product` = ps.`id_product`')
                    ->where('ps.id_shop = '.(int)$current_shop_id);
                    $query->where('p.`id_product` IN ('.implode(', ', array_map('intval', $products)).')');
                    $result = Db::getInstance()->executeS($query);
                } else {
                    $result = array(array('id_product' => 0, 'id_product_attribute' => null));
                }
*/
            }

            return analyzeResult;
        }

        public Map<Integer, List<JeproLabSpecificPriceConditionModel>> getConditions(){
            String query = "SELECT condition_group.*, condition.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_rule_condition_group");
            query += " AS condition_group LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_rule_condition") + " AS condition ON (";
            query += "condition.specific_price_rule_condition_group_id = condition_group.specific_price_rule_condition_group_id) WHERE condition_group.";
            query += "specific_price_rule_id =" + this.specific_price_rule_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet conditions = dataBaseObject.loadObjectList(query);

            Map<Integer, List<JeproLabSpecificPriceConditionModel>> conditionsGroup = new HashMap<>();
            if (conditions != null) {
                try{
                    JeproLabSpecificPriceConditionModel condition;
                    List<JeproLabSpecificPriceConditionModel> priceCondition;
                    while(conditions.next()) {
                        condition = new JeproLabSpecificPriceConditionModel();
                        condition.specific_price_rule_condition_id = conditions.getInt("specific_price_rule_condition_id");
                        condition.specific_price_rule_condition_group_id = conditions.getInt("specific_price_rule_condition_group_id");
                        condition.type = conditions.getString("type");
                        if (conditions.getString("type").equals("attribute")){
                            query = "SELECT attribute_group_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_attribute") + " AS attribute ";
                            query += " WHERE attribute_id = " + conditions.getInt("value");
                            //dataBaseObject.setQuery(query);
                            condition.attribute_group_id = (int)dataBaseObject.loadValue(query, "attribute_group_id");
                        } else if (conditions.getString("type").equals("feature")){
                            query = "SELECT feature_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_feature_value") + " WHERE feature_value_id = " + conditions.getInt("value");
                            //dataBaseObject.setQuery(query);
                            condition.feature_id = (int)dataBaseObject.loadValue(query, "feature_id");
                        }
                        if(!conditionsGroup.containsKey(condition.specific_price_rule_condition_group_id)){
                            priceCondition = new ArrayList<>();
                            conditionsGroup.put(condition.specific_price_rule_condition_group_id, priceCondition);
                        }
                        priceCondition = conditionsGroup.get(condition.specific_price_rule_condition_group_id);
                        priceCondition.add(condition);
                        conditionsGroup.put(condition.specific_price_rule_condition_group_id , priceCondition);
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
            return conditionsGroup;
        }

        public static boolean applyRuleToAnalyze(int ruleId, int analyzeId){
            return applyRuleToAnalyze(ruleId, analyzeId, 0);
        }

        public static boolean applyRuleToAnalyze(int ruleId, int analyzeId, int analyzeAttributeId) {
            JeproLabSpecificPriceRuleModel priceRule = new JeproLabSpecificPriceRuleModel(ruleId);
            if (priceRule.specific_price_rule_id <= 0 ||analyzeId <= 0){
                return false;
            }

            JeproLabSpecificPriceModel specificPrice = new JeproLabSpecificPriceModel();
            specificPrice.specific_price_rule_id = priceRule.specific_price_rule_id;
            specificPrice.analyze_id = analyzeId;
            specificPrice.analyze_attribute_id = analyzeAttributeId;
            specificPrice.customer_id = 0;
            specificPrice.laboratory_id = priceRule.laboratory_id;
            specificPrice.country_id = priceRule.country_id;
            specificPrice.currency_id = priceRule.currency_id;
            specificPrice.group_id = priceRule.group_id;
            specificPrice.from_quantity = priceRule.from_quantity;
            specificPrice.price = priceRule.price;
            specificPrice.reduction_type = priceRule.reduction_type;
            specificPrice.reduction_tax = priceRule.reduction_tax;
            specificPrice.reduction = (priceRule.reduction_type.equals("percentage") ? priceRule.reduction / 100 : priceRule.reduction);
            specificPrice.from = priceRule.from;
            specificPrice.to = priceRule.to;

            return specificPrice.add();
        }

        public boolean delete(){
            this.deleteConditions();
            String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price") + " WHERE ";
            query += JeproLabDataBaseConnector.quoteName("specific_price_rule_id") + " = " + this.specific_price_rule_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            //Db::getInstance()->execute(''._DB_PREFIX_.'specific_price WHERE id_specific_price_rule='.(int)$this->id);
            boolean result = dataBaseObject.query(query, false); //parent::delete();
            closeDataBaseConnection(dataBaseObject);
            return result;
        }

        public boolean deleteConditions(){
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("specific_price_rule_condition_group_id") + " FROM ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_rule_condition_group") + " WHERE ";
            query += JeproLabDataBaseConnector.quoteName("specific_price_rule_id") + " = " + this.specific_price_rule_id;

            boolean result = true;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet conditionGroupIdsSet = dataBaseObject.loadObjectList(query);
            if(conditionGroupIdsSet != null){
                try{
                    query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_rule_condition_group") + " WHERE ";
                    query += JeproLabDataBaseConnector.quoteName("specific_price_rule_condition_group_id") + " = ";
                    String query1 = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_specific_price_rule_condition") + " WHERE " ;
                    query1 += JeproLabDataBaseConnector.quoteName("specific_price_rule_condition_group_id") + " = ";
                    int conditionGroupId;
                    while(conditionGroupIdsSet.next()){
                        conditionGroupId = conditionGroupIdsSet.getInt("specific_price_rule_condition_group_id");
                        //dataBaseObject.setQuery(query + conditionGroupId);
                        result &= dataBaseObject.query(query, false);
                        //dataBaseObject.setQuery(query1 + conditionGroupId);
                        result &= dataBaseObject.query(query, false);
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
            }
            return result;
        }

    }

    public static class JeproLabSpecificPriceConditionModel{
        public int specific_price_rule_condition_id;

        public int specific_price_rule_condition_group_id;

        public String type;

        public String value;

        public int attribute_group_id;

        public int feature_id;
    }
}
