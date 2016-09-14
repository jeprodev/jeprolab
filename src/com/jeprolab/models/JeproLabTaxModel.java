package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabTaxModel extends JeproLabModel{
    public int tax_id;

    /** @var string Name */
    public Map<String, String> name;

    /** @var float Rate (%) */
    public float rate;

    /** @var bool active state */
    public boolean published;

    /** @var bool true if the tax has been historized */
    public boolean deleted = false;

    public JeproLabTaxModel(){
        this(0, 0, 0);
    }

    public JeproLabTaxModel(int taxId){
        this(taxId, 0, 0);
    }

    public JeproLabTaxModel(int taxId, int langId){
        this(taxId, langId, 0);
    }

    public JeproLabTaxModel(int taxId, int langId, int labId){
        if(taxId > 0){
            String cacheKey = "jeprolab_tax_model_" + taxId + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }

                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_tax") + " AS tax LEFT JOIN ";
                query += dataBaseObject.quoteName("#__jeprolab_tax_lang") + " AS tax_lang ON (tax_lang.tax_id = tax.tax_id";

                if(langId > 0){
                    query += " AND tax_lang.lang_id = " + langId ;
                }
                query += ") WHERE tax.tax_id = " + taxId;

                dataBaseObject.setQuery(query);
                ResultSet taxSet = dataBaseObject.loadObjectList();
                if(taxSet != null){
                    try{
                        if(taxSet.next()){
                            this.tax_id = taxSet.getInt("tax_id");
                            this.rate = taxSet.getFloat("rate");
                            this.published = taxSet.getInt("published") > 0;
                            this.deleted = taxSet.getInt("deleted") > 0;
                            this.name = new HashMap<>();
                            if(langId > 0) {
                                this.name.put("lang_" + langId, taxSet.getString("name"));
                            }else{
                                for (Object o : JeproLabLanguageModel.getLanguages().entrySet()) {
                                    Map.Entry lang = (Map.Entry) o;
                                    JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                                    if(taxSet.getInt("lang_id") == language.language_id) {
                                        this.name.put("lang_" + language.language_id, taxSet.getString("name"));
                                    }
                                }
                            }
                            JeproLabCache.getInstance().store(cacheKey, this);
                        }
                    }catch (SQLException ignored){
                        ignored.printStackTrace();
                    }finally {
                        try{
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch(Exception ignored){
                            ignored.printStackTrace();
                        }
                    }
                }

            }else{
                JeproLabTaxModel taxModel = (JeproLabTaxModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.tax_id = taxModel.tax_id;
                this.deleted = taxModel.deleted;
                this.published = taxModel.published;
                this.rate = taxModel.rate;
                this.name = taxModel.name;
            }
        }
    }

    public static boolean excludeTaxOption(){
        return (JeproLabSettingModel.getIntValue("use_tax") > 0);
    }

    public boolean delete(){
        /* Clean associations */
        JeproLabTaxRuleModel.deleteTaxRuleByTaxId(this.tax_id);

        if (this.isUsed()) {
            return this.historized();
        } else {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_tax") + " WHERE ";
            query += staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

            dataBaseObject.setQuery(query);
            return dataBaseObject.query(false);
        }
    }

    /**
     * Save the object with the field deleted to true
     *
     *  @return bool
     */
    public boolean historized(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        this.deleted = true;
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax") + " SET " + staticDataBaseObject.quoteName("published");
        query += " = 0, " + staticDataBaseObject.quoteName("deleted") + " = 1 WHERE " + staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }

    public boolean toggleStatus(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        //this.deleted = true;
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax") + " SET " + staticDataBaseObject.quoteName("published");
        query += " = " + (this.published ? "0" : "1") + ", " + staticDataBaseObject.quoteName("deleted") + " = " + (this.deleted ? "0" : "1");
        query += " WHERE " + staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

        dataBaseObject.setQuery(query);

        if(dataBaseObject.query(false)){
            return this.onStatusChange();
        }
        return false;
    }

    public boolean selfAdd(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_tax") + "(" + dataBaseObject.quoteName("rate") + ", " + dataBaseObject.quoteName("published");
        query += ", " + dataBaseObject.quoteName("deleted") + ") VALUES(" + this.rate + ", " + this.published + ", " + this.deleted + ")";

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }

    public boolean update(){
        if (!this.deleted && this.isUsed()) {
            JeproLabTaxModel historizedTax = new JeproLabTaxModel(this.tax_id);
            historizedTax.historized();

            // remove the id in order to create a new object
            this.tax_id = 0;
            boolean res = this.selfAdd();

            // change tax id in the tax rule table
            res &= JeproLabTaxRuleModel.swapTaxId(historizedTax.tax_id, this.tax_id);
            return res;
        } else {
            if (dataBaseObject == null) {
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax") + " SET " + staticDataBaseObject.quoteName("published");
            query += " = " + (this.published ? "0" : "1") + ", " + staticDataBaseObject.quoteName("deleted") + " = " + (this.deleted ? "0" : "1");
            query += " WHERE " + staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

            dataBaseObject.setQuery(query);
            return dataBaseObject.query(false) && this.onStatusChange();
        }
    }

    protected boolean onStatusChange() {
        return this.published || JeproLabTaxRuleModel.deleteTaxRuleByTaxId(this.tax_id);
    }

    /**
     * Returns true if the tax is used in an order details
     *
     * @return bool
     */
    public boolean isUsed(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("tax_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_order_detail_tax");
        query += " WHERE " + dataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

        dataBaseObject.setQuery(query);
        return (int)dataBaseObject.loadValue("tax_id") > 0;
    }

    public static ResultSet getTaxes(){
        return getTaxes(0, true);
    }

    public static ResultSet getTaxes(int langId ){
        return getTaxes(langId, true);
    }

    /**
     * Get all available taxes
     *
     * @return array Taxes
     */
    public static ResultSet getTaxes(int langId, boolean activeOnly){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String selectQuery = "SELECT tax.tax_id, tax.rate";
        String fromQuery = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax") + " AS tax ";
        String whereQuery = " WHERE tax." + staticDataBaseObject.quoteName("deleted") + " != 1";
        String leftJoin = "", orderBy = "";

        if (langId > 0) {
            selectQuery += ", tax_lang.name, tax_lang.lang_id ";
            leftJoin += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_lang") + " AS tax_lang ON (tax_lang.";
            leftJoin += staticDataBaseObject.quoteName("tax_id") + " = tax." + staticDataBaseObject.quoteName("tax_id") ;
            leftJoin += " AND tax_lang." + staticDataBaseObject.quoteName("lang_id") +  " = " + langId + ") ";
            orderBy += staticDataBaseObject.quoteName("name") + " ASC";
        }

        if (activeOnly) {
            whereQuery += " AND tax." + staticDataBaseObject.quoteName("published") + " = 1";
        }

        staticDataBaseObject.setQuery(selectQuery + fromQuery + leftJoin + whereQuery + orderBy);

        return staticDataBaseObject.loadObjectList();
    }

    public static List<JeproLabTaxModel> getTaxList(){
        return getTaxList(JeproLabContext.getContext().language.language_id, false);
    }

    public static List<JeproLabTaxModel> getTaxList(int langId){
        return getTaxList(langId, false);
    }

    public static List<JeproLabTaxModel> getTaxList(int langId, boolean activeOnly){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String selectQuery = "SELECT tax.*";
        String fromQuery = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax") + " AS tax ";
        String whereQuery = " WHERE tax." + staticDataBaseObject.quoteName("deleted") + " != 1";
        String leftJoin = "", orderBy = " ORDER BY ";

        if (langId > 0) {
            selectQuery += ", tax_lang.name, tax_lang.lang_id ";
            leftJoin += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_lang") + " AS tax_lang ON (tax_lang.";
            leftJoin += staticDataBaseObject.quoteName("tax_id") + " = tax." + staticDataBaseObject.quoteName("tax_id") ;
            leftJoin += " AND tax_lang." + staticDataBaseObject.quoteName("lang_id") +  " = " + langId + ") ";
            orderBy += staticDataBaseObject.quoteName("name") + " ASC";
        }

        if (activeOnly) {
            whereQuery += " AND tax." + staticDataBaseObject.quoteName("published") + " = 1";
        }

        staticDataBaseObject.setQuery(selectQuery + fromQuery + leftJoin + whereQuery + orderBy);

        ResultSet taxSet =  staticDataBaseObject.loadObjectList();
        List<JeproLabTaxModel> taxList = new ArrayList<>();

        if(taxSet != null){
            try{
                JeproLabTaxModel tax;
                while(taxSet.next()){
                    tax = new JeproLabTaxModel();
                    tax.tax_id = taxSet.getInt("tax_id");
                    tax.rate = taxSet.getFloat("rate");
                    tax.published = taxSet.getInt("published") > 0;
                    tax.name = new HashMap<>();
                    tax.name.put("lang_" + langId, taxSet.getString("name"));
                    taxList.add(tax);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return taxList;
    }

    public static int getTaxIdByName(String taxName){
        return getTaxIdByName(taxName, true);
    }
    /**
     * Return the tax id associated to the specified name
     *
     * @param taxName tax name of a tax to be retrieved
     * @param active (true by default)
     */
    public static int getTaxIdByName(String taxName, boolean active){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tax." + staticDataBaseObject.quoteName("tax_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax");
        query += " AS tax LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_lang") + " As tax_lang ON (tax_lang.tax_id = tax.tax_id)";
        query += " WHERE tax_lang." + staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(taxName);
        query += (active ? " AND tax." + staticDataBaseObject.quoteName("published") + " = 1 " : "");

        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("tax_id");
    }

    public static float getAnalyzeEcoTaxRate(){
        return getAnalyzeEcoTaxRate(0);
    }

    /**
     * Returns the ecotax tax rate
     *
     * @param addressId address id
     * @return float $tax_rate
     */
    public static float getAnalyzeEcoTaxRate(int addressId){
        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, JeproLabSettingModel.getIntValue("ecotax_tax_rules_group_id"));
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }

    public static float getCarrierTaxRate(int carrierId){
        return getCarrierTaxRate(carrierId, 0);
    }

    /**
     * Returns the carrier tax rate
     *
     * @param carrierId carrier Id
     * @param addressId address id
     * @return float $tax_rate
     */
    public static float getCarrierTaxRate(int carrierId, int addressId){
        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);
        int taxRulesId = JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(carrierId);

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, taxRulesId);
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }

    public static float getAnalyzeTaxRate(int analyzeId ){
        return getAnalyzeTaxRate(analyzeId, 0, null);
    }

    public static float getAnalyzeTaxRate(int analyzeId, int addressId){
        return getAnalyzeTaxRate(analyzeId, addressId, null);
    }

    /**
     * Returns the product tax
     *
     * @param analyzeId analyze id
     * @param addressId address to get the tax from
     * @return Tax value
     */
    public static float getAnalyzeTaxRate(int analyzeId, int addressId, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }

        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);
        int taxRulesId = JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyzeId, context);

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, taxRulesId);
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }




    public static class JeproLabTaxRuleModel extends JeproLabModel{
        public int tax_rules_group_id;
        public int country_id;
        public int state_id;
        public String zipcode_from;
        public String zipcode_to;
        public int tax_id;
        public int behavior;
        public String description;

        /**
         * Replace a tax_rule id by an other one in the tax_rule table
         *
         * @param oldId old tax id
         * @param newId new tax id
         */
        public static boolean swapTaxId(int oldId, int newId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_tax_rule") + " SET " + staticDataBaseObject.quoteName("tax_id");
            query += " = " + newId + " WHERE " + staticDataBaseObject.quoteName("tax_id") + " = "+ oldId;

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }

        public static boolean deleteTaxRuleByTaxId(int taxId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax_rule") + " AS tax_rule WHERE ";
            query += staticDataBaseObject.quoteName("tax_id") + " = " + taxId;
            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }
    }


    /**
     *
     * Created by jeprodev on 04/02/14.
     */
    public static class JeproLabTaxRulesGroupModel  extends JeproLabModel {
        public int tax_rules_group_id;

        public String name;

        /** @var bool active state */
        public boolean published;

        public boolean deleted = false;

        /** @var string Object creation date */
        public Date date_add;

        /** @var string Object last modification date */
        public Date date_upd;

        public JeproLabTaxRulesGroupModel(){
            this(0);
        }

        public JeproLabTaxRulesGroupModel(int taxRulesGroupId){
            if(taxRulesGroupId > 0){
                String cacheKey = "jeprolab_tax_rules_group_model_" + taxRulesGroupId;
                if(!JeproLabCache.getInstance().isStored(cacheKey)){
                    if(dataBaseObject == null){
                        dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }

                    String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " WHERE " + dataBaseObject.quoteName("tax_rules_group_id") + " = " + taxRulesGroupId;

                    dataBaseObject.setQuery(query);
                    ResultSet taxRulesGroupSet = dataBaseObject.loadObjectList();

                    if(taxRulesGroupSet != null){
                        try{
                            if(taxRulesGroupSet.next()){
                                this.tax_rules_group_id = taxRulesGroupSet.getInt("tax_rules_group_id");
                                this.name = taxRulesGroupSet.getString("name");
                                this.published = taxRulesGroupSet.getInt("published") > 0;
                                JeproLabCache.getInstance().store(cacheKey, this);
                            }
                        }catch(SQLException ignored){
                            ignored.printStackTrace();
                        }
                    }
                }else{
                    JeproLabTaxRulesGroupModel taxRulesGroup = (JeproLabTaxRulesGroupModel)JeproLabCache.getInstance().retrieve(cacheKey);
                    this.tax_rules_group_id = taxRulesGroup.tax_rules_group_id;
                    this.name = taxRulesGroup.name;
                    this.published = taxRulesGroup.published;
                }
            }
        }

        public static List<JeproLabTaxRulesGroupModel> getTaxRulesGroups(){
            return getTaxRulesGroups(true);
        }

        public static List<JeproLabTaxRulesGroupModel> getTaxRulesGroups(boolean onlyPublished){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT DISTINCT tax_rules_group." + staticDataBaseObject.quoteName("tax_rules_group_id") + ", tax_rules_group.name, ";
            query += "tax_rules_group.published FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " AS tax_rules_group ";
            query += JeproLabLaboratoryModel.addSqlAssociation("tax_rules_group") ;//+ " WHERE deleted = 0 " ;
            query += (onlyPublished ? " WHERE tax_rules_group." + staticDataBaseObject.quoteName("published") + " = 1 " : "") + " ORDER BY name ASC ";

            staticDataBaseObject.setQuery(query);
            ResultSet taxRulesGroups = staticDataBaseObject.loadObjectList();
            List<JeproLabTaxRulesGroupModel> taxRulesList = new ArrayList<>();
            try{
                JeproLabTaxRulesGroupModel taxRulesGroupModel;
                while(taxRulesGroups.next()){
                    taxRulesGroupModel = new JeproLabTaxRulesGroupModel();
                    taxRulesGroupModel.tax_rules_group_id = taxRulesGroups.getInt("tax_rules_group_id");
                    taxRulesGroupModel.published = taxRulesGroups.getInt("published") > 0;
                    taxRulesGroupModel.name = taxRulesGroups.getString("name");
                    taxRulesList.add(taxRulesGroupModel);
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
            return taxRulesList;
        }

        /**
         * @return array
         */
        public static Map<Integer, Float> getAssociatedTaxRatesByCountryId(int countryId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT tax_rules_group." + staticDataBaseObject.quoteName("tax_rules_group_id") + ", tax." + staticDataBaseObject.quoteName("rate") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " AS tax_rules_group LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_rule");
            query += " tax_rule ON (tax_rule." + staticDataBaseObject.quoteName("tax_rules_group_id") + " = tax_rules_group." + staticDataBaseObject.quoteName("tax_rules_group_id");
            query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax") + " AS tax ON (tax." + staticDataBaseObject.quoteName("tax_id") + " = tax_rule.";
            query += staticDataBaseObject.quoteName("tax_id") + ") WHERE tax_rule." + staticDataBaseObject.quoteName("country_id") + " = " + countryId + " AND tax_rule.";
            query += staticDataBaseObject.quoteName("state_id") + " = 0 AND 0 between " + staticDataBaseObject.quoteName("zipcode_from") + " AND " ;
            query += staticDataBaseObject.quoteName("zipcode_to");

            staticDataBaseObject.setQuery(query);
            ResultSet rows = staticDataBaseObject.loadObjectList();
            Map<Integer, Float> taxRates = new HashMap<>();

            try{
                while(rows.next()){
                    taxRates.put(rows.getInt("tax_rules_group_id"), rows.getFloat("rate"));
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
            return taxRates;
        }

        public boolean save(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " (" + dataBaseObject.quoteName("name");
            query += ", " + dataBaseObject.quoteName("published") + ") VALUES (" + dataBaseObject.quote(this.name) + ", ";
            query += (this.published ? 1 : 0) + ") ";

            dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.query(true);
            this.tax_rules_group_id = dataBaseObject.getGeneratedKey();
            return result;
        }

        public boolean update(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " SET " + dataBaseObject.quoteName("name");
            query += " = " + dataBaseObject.quote(this.name) + ", " + dataBaseObject.quoteName("published") + " = " + (this.published ? 1 : 0);
            query += " WHERE " + dataBaseObject.quoteName("tax_rules_group_id") + " = " + this.tax_rules_group_id;

            dataBaseObject.setQuery(query);
            return dataBaseObject.query(false);
        }

    }


    public static class JeproLabTaxCalculator {
        /**
         * COMBINE_METHOD sum taxes
         * eg: 100� * (10% + 15%)
         */
        private static int COMBINE_METHOD = 1;

        /**
         * ONE_AFTER_ANOTHER_METHOD apply taxes one after another
         * eg: (100� * 10%) * 15%
         */
        private static int ONE_AFTER_ANOTHER_METHOD = 2;

        /**
         * @var array $taxes
         */
        public List<JeproLabTaxModel> taxes;

        /**
         * @var int $computation_method (COMBINE_METHOD | ONE_AFTER_ANOTHER_METHOD)
         */
        public int computation_method;

        public JeproLabTaxCalculator(){
            this(new ArrayList<>());
        }

        public JeproLabTaxCalculator(List<JeproLabTaxModel> taxes){
            this(taxes, JeproLabTaxCalculator.COMBINE_METHOD);
        }

        /**
         * @param taxes list of taxes
         * @param computationMethod (COMBINE_METHOD | ONE_AFTER_ANOTHER_METHOD)
         */
        public JeproLabTaxCalculator(List<JeproLabTaxModel> taxes, int computationMethod){
            this.taxes = taxes;
            this.computation_method = computationMethod;
        }

        /**
         * Compute and add the taxes to the specified price
         *
         * @param priceTaxExcluded price tax excluded
         * @return float price with taxes
         */
        public float addTaxes(float priceTaxExcluded){
            return priceTaxExcluded * (1 + (this.getTotalRate() / 100));
        }


        /**
         * Compute and remove the taxes to the specified price
         *
         * @param priceTaxIncluded price tax inclusive
         * @return float price without taxes
         */
        public float removeTaxes(float priceTaxIncluded){
            return priceTaxIncluded / (1 + this.getTotalRate() / 100);
        }

        /**
         * @return taxes total taxes rate
         */
        public float getTotalRate(){
            float taxes = 0;
            if (this.computation_method == JeproLabTaxCalculator.ONE_AFTER_ANOTHER_METHOD) {
                taxes = 1;
                for(JeproLabTaxModel tax : this.taxes) {
                    taxes *= (1 + (Math.abs(tax.rate) / 100));
                }

                taxes = taxes - 1;
                taxes = taxes * 100;
            } else {
                for(JeproLabTaxModel tax : this.taxes) {
                    taxes += Math.abs(tax.rate);
                }
            }

            return taxes;
        }

        public String getTaxesName(){
            String name = "";
            int langId = JeproLabContext.getContext().language.language_id;
            for(JeproLabTaxModel tax : this.taxes) {
                name += tax.name.get(langId) + " - ";
            }

            //$name = rtrim($name, ' - ');

            return name;
        }
    }

    /**
     *
     * Created by jeprodev on 04/02/2014.
     */
    public static class JeproLabTaxManagerFactory {
        protected static Map<String, JeproLabTaxRulesManager> cache_tax_manager = new HashMap<>();

        public static JeproLabTaxRulesManager getManager(JeproLabAddressModel address, int type){
            return getManager(address, type + "");
        }

        /**
         * Returns a tax manager able to handle this address
         *
         * @param address address
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
            JeproLabTaxRulesManager taxManager = null;
            //$modules_infos = Hook::getModulesFromHook(Hook::getIdByName('taxManager'));

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


    /**
     *
     * Created by jeprodev on 04/02/14.
     */
    public static class JeproLabTaxRulesManager {
        public JeproLabAddressModel address;
        public String type;
        public JeproLabTaxCalculator tax_calculator;

        private static boolean tax_enabled = false;

        /**
         *
         * @param address
         * @param type An additional parameter for the tax manager (ex: tax rules id for TaxRuleTaxManager)
         */
        public JeproLabTaxRulesManager(JeproLabAddressModel address, String type){
            this.address = address;
            this.type = type;
        }

        /**
         * Returns true if this tax manager is available for this address
         *
         * @return bool
         */
        public static boolean isAvailableForThisAddress(JeproLabAddressModel address){
            return true; // default manager, available for all addresses
        }

        /**
         * Return the tax calculator associated to this address
         *
         * @return JeproLabTaxCalculator
         */
        public JeproLabTaxCalculator getTaxCalculator() {
            if (this.tax_calculator != null){
                return this.tax_calculator;
            }

            if (!tax_enabled) {
                tax_enabled = JeproLabSettingModel.getIntValue("use_tax") > 0; //$this->configurationManager->get('PS_TAX');
            }

            if (!tax_enabled) {
                return new JeproLabTaxCalculator(new ArrayList<JeproLabTaxModel>());
            }

            String postcode = "";

            if (this.address != null && !this.address.postcode.equals("")) {
                postcode = this.address.postcode;
            }

            String cacheKey = this.address.country_id + "_" + this.address.state_id + "_" + postcode + "_" + this.type;

            if (!JeproLabCache.getInstance().isStored(cacheKey)) {
                JeproLabDataBaseConnector dataBaseConnector = JeproLabFactory.getDataBaseConnector();
                String query = "SELECT tax_rule.* FROM " + dataBaseConnector.quoteName("#__jeprolab_tax_rule") + " AS tax_rule LEFT JOIN ";
                query += dataBaseConnector.quoteName("#__jeprolab_tax_rules_group") + " AS tax_rule_group ON (tax_rule." + dataBaseConnector.quoteName("tax_rules_group_id");
                query += " = tax_rule_group." + dataBaseConnector.quoteName("tax_rules_group_id") + ") WHERE tax_rule_group." + dataBaseConnector.quoteName("published");
                query += " = 1 AND tax_rule." + dataBaseConnector.quoteName("country_id") + " = " + this.address.country_id + " AND tax_rule.";
                query += dataBaseConnector.quoteName("tax_rules_group_id") + " = " + this.type + " AND tax_rule." + dataBaseConnector.quoteName("state_id");
                query += " IN (0, " + this.address.state_id + ") AND (" + dataBaseConnector.quote(postcode) + " BETWEEN tax_rule.";
                query += dataBaseConnector.quoteName("zipcode_from") + " AND tax_rule." + dataBaseConnector.quoteName("zipcode_to");
                query += " OR (tax_rule." + dataBaseConnector.quoteName("zipcode_to") + " = '' AND tax_rule." + dataBaseConnector.quoteName("zipcode_from");
                query += " IN(0, " + dataBaseConnector.quote(postcode) + "))) ORDER BY tax_rule." + dataBaseConnector.quoteName("zipcode_from");
                query += " DESC, tax_rule." + dataBaseConnector.quoteName("zipcode_to") + " DESC, tax_rule." + dataBaseConnector.quoteName("state_id");
                query += " DESC, tax_rule." + dataBaseConnector.quoteName("country_id") + " DESC ";

                dataBaseConnector.setQuery(query);
                ResultSet taxesSet = dataBaseConnector.loadObjectList();

                int behavior = 0;
                boolean firstRow = true;
                List<JeproLabTaxModel> taxes = new ArrayList<>();
                try{
                    JeproLabTaxModel tax;
                    while(taxesSet.next()){
                        tax = new JeproLabTaxModel(taxesSet.getInt("tax_id"));
                        taxes.add(tax);
                        if(firstRow){
                            behavior = taxesSet.getInt("behavior");
                            firstRow = false;
                        }

                        if(taxesSet.getInt("behavior") == 0){
                            break;
                        }
                    }
                }catch (SQLException ignored){

                }

                JeproLabTaxCalculator result = new JeproLabTaxCalculator(taxes, behavior);
                JeproLabCache.getInstance().store(cacheKey, result);
                return result;
            }

            return (JeproLabTaxCalculator)JeproLabCache.getInstance().retrieve(cacheKey);
        }

    }
}