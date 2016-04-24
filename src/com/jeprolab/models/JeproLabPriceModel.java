package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabPriceModel extends JeproLabModel{
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
        public float price;
        public int from_quantity;
        public float reduction;
        public float reduction_tax = 1;
        public String reduction_type;
        public Date from;
        public Date to;

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
                query += " FROM " + staticDataBaseObject.quoteName("specific_price") + " WHERE " + staticDataBaseObject.quoteName("lab_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, labId) + " AND " + staticDataBaseObject.quoteName("currency_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, currencyId) + " AND " + staticDataBaseObject.quoteName("country_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, countryId) + " " + staticDataBaseObject.quoteName("group_id");
                query += JeproLabSpecificPriceModel.formatIntInQuery(0, groupId) + extraQuery + " AND IF(" + staticDataBaseObject.quoteName("from_quantity");
                query += " > 1, " + staticDataBaseObject.quoteName("from_quantity") + ", 0) <= ";
                query += (JeproLabSettingModel.getIntValue("quantity_discount_on_combination") > 0 || (cartId < 0) || (realQuantity <= 0)) ? quantity : Math.max(1, realQuantity);
                query += " ORDER BY " + staticDataBaseObject.quoteName("analyze_attribute_id") + " DESC, " + staticDataBaseObject.quoteName("from_quantity") + " DESC, ";
                query += staticDataBaseObject.quoteName("specific_price_rule_id") + " ASC, " + staticDataBaseObject.quoteName("score") + " DESC, ";
                query += staticDataBaseObject.quoteName("to") + " DESC, " + staticDataBaseObject.quoteName("from") + " DESC";
                System.out.println(query);
                staticDataBaseObject.setQuery(query);
                ResultSet resultSet = staticDataBaseObject.loadObjectList();
                JeproLabSpecificPriceModel specificPrice = new JeproLabSpecificPriceModel();
                try {
                    if(resultSet.next()){
                        //todo set fields
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
                    select += " IF (" + staticDataBaseObject.quoteName(field) + " = ";
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
            select = select.substring(0, select.length() - 2);
            return select + ") AS " + staticDataBaseObject.quoteName("score");
        }

        public static String getPriority(int analyzeId){
            if (!JeproLabSpecificPriceModel.isFeaturePublished()) {
                return JeproLabSettingModel.getStringValue("specific_price_priorities"); //'PS_SPECIFIC_PRICE_PRIORITIES'));
            }

            if (!JeproLabSpecificPriceModel._cache_priorities.containsKey(analyzeId)){
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }

                String query = "SELECT " + staticDataBaseObject.quoteName("priority") + ", " + staticDataBaseObject.quoteName("specific_price_priority_id");
                query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price_priority") + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
                query += " = " + analyzeId + " ORDER BY " + staticDataBaseObject.quoteName("specific_price_priority_id") + " DESC ";

                staticDataBaseObject.setQuery(query);

                JeproLabSpecificPriceModel._cache_priorities.put(analyzeId, staticDataBaseObject.loadStringValue("priority"));
            }

            String priority = JeproLabSpecificPriceModel._cache_priorities.get(analyzeId);

            if (priority.equals("")) {
                priority = JeproLabSettingModel.getStringValue("specific_price_priorities");
            }
            priority = "customer_id;" + priority;

            return priority;
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
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String extraQuery = " AND " + staticDataBaseObject.quoteName(fieldName) + " = 0 ";
            if (fieldValue == 0 || JeproLabSpecificPriceModel._no_specific_values.containsKey(fieldName)){
                return extraQuery;
            }
            String cacheKey  = "jeprolab_specific_price_filter_out_field_" + fieldName + "_" + threshold;
            List<Integer> specificList = new ArrayList<>();
            if (!JeproLabSpecificPriceModel._filterOutCache.containsKey(cacheKey)) {
                String queryCount = "SELECT COUNT(DISTINCT " + staticDataBaseObject.quoteName(fieldName) + ") AS filter_out FROM ";
                queryCount += staticDataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE " + staticDataBaseObject.quoteName(fieldName) + " != 0";
                staticDataBaseObject.setQuery(queryCount);

                int specificCount = (int)staticDataBaseObject.loadValue("filter_out");
                if (specificCount == 0) {
                    JeproLabSpecificPriceModel._no_specific_values.put(fieldName, true);

                    return extraQuery;
                }
                if (specificCount < threshold) {
                    String query  = "SELECT DISTINCT " + staticDataBaseObject.quoteName(fieldName) + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price");
                    query += " WHERE " + staticDataBaseObject.quoteName(fieldName) + " != 0";
                    staticDataBaseObject.setQuery(query);
                    ResultSet tmpSpecificSet = staticDataBaseObject.loadObjectList();

                    try{
                        while (tmpSpecificSet.next()){
                            specificList.add(tmpSpecificSet.getInt(fieldName));
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
                JeproLabSpecificPriceModel._filterOutCache.put(cacheKey, specificList);
            } else {
                specificList = (List)JeproLabSpecificPriceModel._filterOutCache.get(cacheKey);
            }

            // $specific_list is empty if the threshold is reached
            if (specificList.isEmpty() || specificList.contains(fieldValue)) {
                extraQuery = " AND " + staticDataBaseObject.quoteName(fieldName) + " " + JeproLabSpecificPriceModel.formatIntInQuery(0, fieldValue) +" ";
            }

            return extraQuery;
        }

        private static String formatIntInQuery(int firstValue, int secondValue) {
            if (firstValue != secondValue) {
                return " IN (" + firstValue + ", " + secondValue + ")";
            } else {
                return " = " + firstValue;
            }
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
            Date firstDate = new Date("Y-m-d 00:00:00");
            Date lastDate = new  Date("Y-m-d 23:59:59");
            Date now = new Date("Y-m-d H:i:00");
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

            int fromSpecificCount, toSpecificCount;
            if (ending == now && beginning == now) {
                String cacheKey = "jeprolab_specific_price_compute_extra_conditions" + firstDate.toString() + "_" + lastDate.toString();

                if (!JeproLabSpecificPriceModel._filterOutCache.containsKey(cacheKey)) {
                    String queryFromCount = "SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price");
                    queryFromCount += " WHERE " + staticDataBaseObject.quoteName("from") + " BETWEEN '" + firstDate.toString() + "' AND '" + lastDate.toString() + "'";
                    staticDataBaseObject.setQuery(queryFromCount);
                    fromSpecificCount = (int)staticDataBaseObject.loadValue("1"); //Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query_from_count);

                    String queryToCount = "SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE ";
                    queryToCount += staticDataBaseObject.quoteName("to") + " BETWEEN '" + firstDate.toString() + "' AND '" + lastDate.toString() + "'";
                    staticDataBaseObject.quoteName(queryToCount);

                    toSpecificCount  = (int)staticDataBaseObject.loadValue("1");
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

            extraQuery += " AND (" + staticDataBaseObject.quoteName("from") + " = '0000-00-00 00:00:00' OR '" + beginning + "' >= ";
            extraQuery += staticDataBaseObject.quoteName("from") + ") AND (" + staticDataBaseObject.quoteName("to") + " = '0000-00-00 00:00:00' OR ' ";
            extraQuery += ending + "' <= " + staticDataBaseObject.quoteName("to") + ")";

            return extraQuery;
        }

        public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId){
            return getSpecificPricesByAnalyzeId(analyzeId, 0, 0);
        }

        public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId, int analyzeAttributeId){
            return getSpecificPricesByAnalyzeId(analyzeId, analyzeAttributeId, 0);
        }

        public static List<JeproLabSpecificPriceModel> getSpecificPricesByAnalyzeId(int analyzeId, int analyzeAttributeId, int cartId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_specific_price") + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
            query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND analyze_attribute_id = " + analyzeAttributeId : "" );
            query += (cartId > 0 ? " AND cart_id = " + cartId : "" );

            staticDataBaseObject.setQuery(query);
            ResultSet specificSet = staticDataBaseObject.loadObjectList();
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
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.print("specificList");
            System.out.print(specificList);
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
    }
}
