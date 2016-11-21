package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCartModel extends JeproLabModel{
    public int cart_id;

    public int laboratory_group_id;

    public int laboratory_id;

    /** @var int Customer delivery address ID */
    public int delivery_address_id;

    /** @var int Customer invoicing address ID */
    public int invoice_address_id;

    /** @var int Customer currency ID */
    public int currency_id;

    /** @var int Customer ID */
    public int customer_id;

    /** @var int Guest ID */
    public int guest_id ;

    /** @var int Language ID */
    public int language_id;

    /** @var bool True if the customer wants a recycled */
    public boolean recyclable = false;

    /** @var bool True if the customer wants a gift wrapping */
    public boolean gift = false;

    /** @var string Gift message if specified */
    public String gift_message;

    /** @var bool Mobile Theme */
    public boolean mobile_theme;

    /** @var string Object creation date */
    public Date date_add;

    /** @var string secure_key */
    public String secure_key;

    /** @var int Carrier ID */
    public int carrier_id = 0;

    /* @var string Object last modification date */
    public Date date_upd;

    public boolean allow_separated_result = false;

    public String delivery_option;

    public boolean checkedTos = false;

    public List<JeproLabAnalyzeModel> _analyzes;

    /** @var Customer|null */
    protected static JeproLabCustomerModel customer = null;

    protected static Map<Integer, Float> _total_weight = new HashMap<>();
    protected int tax_calculation_method = JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED;

    protected static Map<Integer, Integer>_number_of_analyzes = new HashMap<>();

    public final static int ONLY_ANALYZES = 1;
    public final static int ONLY_DISCOUNTS = 2;
    public final static int BOTH = 3;
    public final static int BOTH_WITHOUT_SHIPPING = 4;
    public final static int ONLY_SHIPPING = 5;
    public final static int ONLY_WRAPPING = 6;
    public final static int ONLY_ANALYZES_WITHOUT_SHIPPING = 7;
    public final static int ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING = 8;

    public JeproLabCartModel(){
        this(0, 0);
    }

    public JeproLabCartModel(int cartId){
        this(cartId, 0);
    }

    public JeproLabCartModel(int cartId , int langId){
        if (langId > 0) {
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(cartId > 0){
            String cacheKey = "jeprolab_cart_model_" + cartId + "_" + langId;

            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROm " + dataBaseObject.quoteName("#__jeprolab_cart") + " AS cart WHERE cart_id = " + cartId;
                dataBaseObject.setQuery(query);

                ResultSet cartSet = dataBaseObject.loadObjectList();
                try{
                    if(cartSet.next()){
                        this.cart_id = cartSet.getInt("cart_id");
                        this.laboratory_group_id = cartSet.getInt("lab_group_id");
                        this.laboratory_id = cartSet.getInt("lab_id");
                        this.delivery_option = cartSet.getString("delivery_option");
                        this.language_id = cartSet.getInt("lang_id");
                        this.delivery_address_id = cartSet.getInt("delivery_address_id");
                        this.invoice_address_id = cartSet.getInt("invoice_address_id");
                        this.currency_id = cartSet.getInt("currency_id");
                        this.customer_id = cartSet.getInt("customer_id");
                        this.guest_id = cartSet.getInt("guest_id");
                        this.secure_key = cartSet.getString("secure_key");
                        this.recyclable = cartSet.getInt("recyclable") > 0;
                        this.gift = cartSet.getBoolean("gift");
                        this.gift_message = cartSet.getString("gift_message");
                        this.mobile_theme = cartSet.getInt("mobile_theme") > 0;
                        this.allow_separated_result = cartSet.getInt("allow_separated_result") > 0;
                        this.date_add = cartSet.getDate("date_add");
                        this.date_upd = cartSet.getDate("date_upd");

                        JeproLabCache.getInstance().store(cacheKey, this);
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
            }else{
                JeproLabCartModel cart = (JeproLabCartModel) JeproLabCache.getInstance().retrieve(cacheKey);
                this.cart_id = cart.cart_id;
                this.laboratory_group_id = cart.laboratory_group_id;
                this.laboratory_id = cart.laboratory_id;
                this.delivery_option = cart.delivery_option;
                this.language_id = cart.language_id;
                this.delivery_address_id = cart.delivery_address_id;
                this.invoice_address_id = cart.invoice_address_id;
                this.currency_id = cart.currency_id;
                this.customer_id = cart.customer_id;
                this.guest_id = cart.guest_id;
                this.secure_key = cart.secure_key;
                this.recyclable = cart.recyclable;
                this.gift = cart.gift;
                this.gift_message = cart.gift_message;
                this.mobile_theme = cart.mobile_theme;
                this.allow_separated_result = cart.allow_separated_result;
                this.date_add = cart.date_add;
                this.date_upd = cart.date_upd;
            }
        }

        if (this.customer_id > 0) {
            JeproLabCustomerModel customer;
            if ((JeproLabContext.getContext().customer != null && JeproLabContext.getContext().customer.customer_id >0) && JeproLabContext.getContext().customer.customer_id == this.customer_id) {
                customer = JeproLabContext.getContext().customer;
            } else {
                customer = new JeproLabCustomerModel(this.customer_id);
            }

            JeproLabCartModel.customer = customer;

            if ((!this.secure_key.equals("") || this.secure_key.equals("-1")) && !customer.secure_key.equals("")) {
                this.secure_key = customer.secure_key;
                this.saveCart();
            }
        }

        this.setTaxCalculationMethod();
    }

    /**
     * Set Tax calculation method
     */
    public void setTaxCalculationMethod(){
        this.tax_calculation_method = JeproLabGroupModel.getPriceDisplayMethod(JeproLabGroupModel.getCurrent().group_id);
    }

    /**
     * Saves current object to database (add or update)
     *
     * @return bool Insertion result
     */
    public boolean saveCart(){
        return (this.cart_id > 0 ? this.updateCart() : this.addCart());
    }

    /**
     * Adds current JeproLabCartModel as a new Object to the database
     *
     * @return whether the cart has been successfully added.
     */
    public boolean addCart(){
        if (this.language_id <= 0) {
            this.language_id = JeproLabSettingModel.getIntValue("default_lang");
        }
        if (this.laboratory_id <= 0) {
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_cart") + "(" + staticDataBaseObject.quoteName("lab_group_id");
        query += ", " + staticDataBaseObject.quoteName("lab_id") +  ", " + staticDataBaseObject.quoteName("carrier_id") + ", ";
        query += staticDataBaseObject.quoteName("delivery_option") + ", " + staticDataBaseObject.quoteName("lang_id") + ", ";
        query += staticDataBaseObject.quoteName("delivery_address_id") +  ", " + staticDataBaseObject.quoteName("invoice_address_id");
        query += ", " + staticDataBaseObject.quoteName("currency_id") +  ", " + staticDataBaseObject.quoteName("customer_id");
        query += ", " + staticDataBaseObject.quoteName("guest_id") +  ", " + staticDataBaseObject.quoteName("secure_key");
        query += ", " + staticDataBaseObject.quoteName("recyclable") +  ", " + staticDataBaseObject.quoteName("gift") + ", ";
        query += staticDataBaseObject.quoteName("gift_message") +  ", " + staticDataBaseObject.quoteName("mobile_theme") + ", ";
        query += staticDataBaseObject.quoteName("allow_separated_result")  +  ", " + staticDataBaseObject.quoteName("date_add");
        query += ", " + staticDataBaseObject.quoteName("date_upd") + ") VALUES (" + this.laboratory_group_id + ", " + this.laboratory_id + ", " + this.carrier_id;
        query += ", " + staticDataBaseObject.quote(this.delivery_option) + ", " + this.language_id + ", " + this.delivery_address_id + ", ";
        query += this.invoice_address_id + ", " + this.currency_id + ", " + this.customer_id + ", " + this.guest_id + ", ";
        query += staticDataBaseObject.quote(this.secure_key) + ", " + (this.recyclable ? 1 : 0) + ", " + (this.gift ? 1 : 0) + ", ";
        query += staticDataBaseObject.quote(this.gift_message) + ", " + (this.mobile_theme ? 1 : 0) + ", " + (this.allow_separated_result ? 1 : 0);
        query += ", " + staticDataBaseObject.quote(JeproLabTools.date()) + ", " + staticDataBaseObject.quote(JeproLabTools.date()) + ") ";

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(true);

        this.cart_id = staticDataBaseObject.getGeneratedKey();
        return (this.cart_id > 0);
    }

    public boolean updateCart(){
        if (JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)) {
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)) {
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }

        this._analyzes = null;
        /*$return = parent::update($null_values);
        Hook::exec('actionCartSave');*/
        //todo
        return true;
    }

    /**
     * Update the address Id of the cart
     *
     * @param addressId current address id to change
     * @param newAddressId new address Id
     */
    public void updateAddressId(int addressId, int newAddressId){
        boolean toBeUpdated = false;
        if(this.invoice_address_id == 0 || this.invoice_address_id == addressId){
            toBeUpdated = true;
            this.invoice_address_id = newAddressId;
        }

        if(this.delivery_address_id == 0 || this.delivery_address_id == addressId){
            toBeUpdated = true;
            this.delivery_address_id = newAddressId;
        }

        if(toBeUpdated){ this.updateCart(); }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query =  "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyses") + " SET " + staticDataBaseObject.quoteName("delivery_address_id");
        query += " = " + newAddressId + ", " + " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND ";
        query += staticDataBaseObject.quoteName("delivery_address_id") + " = " + addressId;

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);

        query =  "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " SET " + staticDataBaseObject.quoteName("delivery_address_id");
        query += " = " + newAddressId + ", " + " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND ";
        query += staticDataBaseObject.quoteName("delivery_address_id") + " = " + addressId;

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);
    }

    /**
     * Delete current JeproLabCartModel from the database
     */
    public boolean delete(){
        if(this.requestExists()){
            return false;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT customized_data." + staticDataBaseObject.quoteName("value") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customized_data");
        query += " AS customized_data INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " AS customization ON customization." ;
        query += staticDataBaseObject.quoteName("customization_id") + " = customized_data." + staticDataBaseObject.quoteName("customization_id") + " WHERE ";
        query += " customized_data." + staticDataBaseObject.quoteName("type") + " = 0 AND customization." + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();

        try{
            while(resultSet.next()){

            }
        }catch(SQLException ignored){
            ignored.printStackTrace();
        }finally {
            try {
                JeproLabDataBaseConnector.getInstance().closeConnexion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_customized_data") + " WHERE " + staticDataBaseObject.quoteName("customized_id");
        query += " IN( SELECT " + staticDataBaseObject.quoteName("customization_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab__customization");
        query += " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);

        query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);

        query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;
        staticDataBaseObject.setQuery(query);

        if(staticDataBaseObject.query(false)) {
            query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;
            staticDataBaseObject.setQuery(query);
            if(!staticDataBaseObject.query(false)){
                return false;
            }
        }else{
            return false;
        }

        this.clearCache("cart", this.cart_id);
        boolean result = true;

        /*/ remove association to multi-lab table
        List<Integer> labIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
        if(labIds.size() > 0){
            query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_")
            for (int labId : labIds){

            }
        } */
        return  result;
    }

    /**
     *
     * @param cartIds list of cart to be deleted
     * @return boolean
     */
    public boolean deleteSelection(List<Integer> cartIds){
        boolean result = true;
        for(int cartId : cartIds){
            this.cart_id = cartId;
            result &= this.delete();
        }
        return result;
    }
   /*
    public boolean toggleStatus(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE FROM "  + staticDataBaseObject.quoteName("#__jeprolab_ca")
    }
*/


    public static float getTaxesAverageUsed(){
        return getTaxesAverageUsed(null);
    }

    public static float getTaxesAverageUsed(JeproLabCartModel cart){
        if(cart == null){
            cart = new JeproLabCartModel();
        }

        if(!JeproLabTools.isLoadedObject(cart, "cart_id")){
            JeproLabTools.displayBarMessage(500, JeproLab.getBundle().getString("JEPROLAB_CART_NOT_AVAILABLE_MESSAGE"));
        }

        if(JeproLabSettingModel.getIntValue("use_tax") <= 0){
            return 0;
        }

        List<JeproLabAnalyzeModel> analyzes = cart.getAnalyzes();
        float totalAnalyzesAverage = 0;
        float taxRatio = 0;
        if(analyzes.size() <= 0){
            return 0;
        }

        int addressId;

        for (JeproLabAnalyzeModel analyze : analyzes){
            if(JeproLabSettingModel.getStringValue("tax_address_type").equals("invoice_address_id")){
                addressId = cart.invoice_address_id;
            }else{
                addressId = analyze.delivery_address_id;
            }

            if(!JeproLabAddressModel.addressExists(addressId)){
                addressId = 0;
            }

            totalAnalyzesAverage += analyze.analyze_price.total_with_tax;
            taxRatio += analyze.analyze_price.total_with_tax * JeproLabTaxModel.getAnalyzeTaxRate(analyze.analyze_id, addressId);
        }

        if(totalAnalyzesAverage > 0){
            return taxRatio / totalAnalyzesAverage;
        }
        return 0;
    }


    public float getAverageAnalyzesTaxRate(){ return getAverageAnalyzesTaxRate(0, 0); }
    public float getAverageAnalyzesTaxRate(float cartAmountTaxExcluded){ return  getAverageAnalyzesTaxRate(cartAmountTaxExcluded, 0); }

    /**
     *
     * @param cartAmountTaxExcluded
     * @param cartAmountTaxIncluded
     * @return
     */
    public float getAverageAnalyzesTaxRate(float cartAmountTaxExcluded, float cartAmountTaxIncluded){
        if(cartAmountTaxExcluded == 0){
            cartAmountTaxIncluded = this.getRequestTotal(true, JeproLabCartModel.ONLY_ANALYZES);
            cartAmountTaxExcluded = this.getRequestTotal(false, JeproLabCartModel.ONLY_ANALYZES);
        }

        float cartVatAmount = cartAmountTaxIncluded - cartAmountTaxExcluded;
        if(cartVatAmount == 0 || cartAmountTaxExcluded == 0){
            return 0;
        }else{
            return (JeproLabTools.roundPrice(cartVatAmount/ cartAmountTaxExcluded, 3));
        }
    }

    public List<JeproLabCartRuleModel> getCartRules(){
        return getCartRules(JeproLabCartRuleModel.FILTER_ACTION_ALL);
    }


    public List<JeproLabCartRuleModel> getCartRules(int filter){
        if(!JeproLabCartRuleModel.isFeaturePublished() || this.cart_id <= 0){
            return  new ArrayList<>();
        }

        String cacheKey = "jeprolab_cart_get_cart_rules_" + this.cart_id + "_" + filter;
        List<JeproLabCartRuleModel> cartRules;
        if(!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT cart_rule.*, cart_rule_lang." + staticDataBaseObject.quoteName("lang_id") + ", cart_rule_lang.";
            query += staticDataBaseObject.quoteName("name") + ", cart_cart_rule." + staticDataBaseObject.quoteName("cart_id") + " FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " AS cart_cart_rule LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule ON (cart_cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lang") + " AS cart_rule_lang.";
            query += " ON (cart_rule_lang." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " AND cart_rule_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + this.language_id + ") WHERE ";
            query += staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id ;
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_SHIPPING ? " AND " + staticDataBaseObject.quoteName("free_shipping") + " = 1 " : "");
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_GIFT ? " AND " + staticDataBaseObject.quoteName("gift_analyze") + " != 0" : "");
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_REDUCTION ? " AND " + staticDataBaseObject.quoteName("reduction_percent") + " != 0 OR " + staticDataBaseObject.quoteName("reduction_amount") + " != 0" :  "");
            query += " ORDER BY cart_rule_lang." + staticDataBaseObject.quoteName("priority") + " ASC";

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObjectList();
            cartRules = new ArrayList<>();
            if(resultSet != null){
                try{
                    JeproLabCartRuleModel cartRule;
                    JeproLabContext virtualContext = JeproLabContext.getContext().cloneContext();
                    virtualContext.cart = this;
                    while (resultSet.next()){
                        cartRule = new JeproLabCartRuleModel();
                        cartRule.cart_rule_id = resultSet.getInt("cart_rule_id");

                        cartRule.language_id = resultSet.getInt("lang_id");
                        cartRule.name = new HashMap<>();
                        cartRule.name.put("lang_" + cartRule.language_id, resultSet.getString("name"));

                        cartRule.real_value = cartRule.getContextualValue(true, virtualContext, filter);
                        cartRule.value_tax_excluded = cartRule.getContextualValue(false, virtualContext, filter);

                        cartRules.add(cartRule);
                    }
                }catch(SQLException | CloneNotSupportedException ignored){
                    ignored.printStackTrace();
                } finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            cartRules = (List<JeproLabCartRuleModel>)JeproLabCache.getInstance().retrieve(cacheKey);
        }

        return cartRules;
    }

    public getDiscounts(){
        return JeproLabCartRuleModel.getCustomerHighLightedDiscounts(this.language_id, this.customer_id, this);
    }


    public List<Integer> getRequestCartRulesIds(){
        return getRequestCartRulesIds(JeproLabCartRuleModel.FILTER_ACTION_ALL);
    }

    public List<Integer> getRequestCartRulesIds(int filter){
        String cacheKey = "jeprolab_cart_get_request_cart_rules_ids_" + this.cart_id + "_" + filter + "_ids";
        if(!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " ;
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " AS cart_cart_rule LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule ON (cart_cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lang") + " AS cart_rule_lang ON (cart_rule_lang.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " AND cart_rule_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + this.language_id + ") WHERE ";
            query += staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id ;
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_SHIPPING ? " AND " + staticDataBaseObject.quoteName("free_shipping") +  " = 1 " : "");
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_GIFT ? " AND " + staticDataBaseObject.quoteName("gift_analyze") +  " != 0 " : "");
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_REDUCTION ? " AND " + staticDataBaseObject.quoteName("reduction_percent") +  " != 0 OR " + staticDataBaseObject.quoteName("reduction_amount") + " != 0" : "");
            query += " ORDER BY cart_rule." + staticDataBaseObject.quoteName("priority") + " ASC";

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObjectList();
            List<Integer>cartRulesIds = new ArrayList<>();
            if(resultSet != null){
                try{

                    while (resultSet.next()){
                        cartRulesIds.add(resultSet.getInt("cart_rule_id"));
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return cartRulesIds;
        }else{
            return (List<Integer>)JeproLabCache.getInstance().retrieve(cacheKey);
        }
    }

    /**
     * Get amount of Customer Discounts
     */
    public int getDiscountsCustomer(int cartRuleId){
        if(!JeproLabCartRuleModel.isFeaturePublished()){
            return 0;
        }

        String cacheKey = "jeprolab_cart_model_get_discounts_customer_" + this.cart_id + "_" + cartRuleId;
        if(!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT COUNT(*) rules FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " WHERE ";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = " + cartRuleId + " AND " + staticDataBaseObject.quoteName("cart_id");
            query += " = " + this.cart_id;

            staticDataBaseObject.setQuery(query);
            int numberOfRule = (int)staticDataBaseObject.loadValue("rules");
            JeproLabCache.getInstance().store(cacheKey, numberOfRule);
            return numberOfRule;
        }
        return (int) JeproLabCache.getInstance().retrieve(cacheKey);
    }


    public List<JeproLabAnalyzeModel> getAnalyzes(){ return getAnalyzes(false, 0, 0); }
    public List<JeproLabAnalyzeModel> getAnalyzes(boolean refresh){ return getAnalyzes(refresh, 0, 0); }
    public List<JeproLabAnalyzeModel> getAnalyzes(boolean refresh, int analyzeId){ return getAnalyzes(refresh, analyzeId, 0);}
    public List<JeproLabAnalyzeModel> getAnalyzes(boolean refresh, int analyzeId, int countryId){
        if(this.cart_id <= 0){
            return new ArrayList<>();
        }
        List<JeproLabAnalyzeModel> analyzeList;
        if(this._analyzes != null && !refresh){
            if(analyzeId > 0){
                analyzeList = new ArrayList<>();
                analyzeList.addAll(_analyzes.stream().filter(analyze -> analyze.analyze_id == analyzeId).collect(Collectors.toList()));
                return analyzeList;
            }
            return this._analyzes;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String selectQuery = "SELECT cart_analyze." + staticDataBaseObject.quoteName("analyze_attribute_id") + ", cart_analyze.";
        selectQuery += staticDataBaseObject.quoteName("analyze_id") + ", cart_analyze." + staticDataBaseObject.quoteName("quantity");
        selectQuery += " AS cart_quantity, cart_analyze." + staticDataBaseObject.quoteName("lab_id") + ", cart_analyze.";
        selectQuery += staticDataBaseObject.quoteName("customization_id") + ", analyze_lang." + staticDataBaseObject.quoteName("name");
        selectQuery += ", analyze." + staticDataBaseObject.quoteName("is_virtual") + ", analyze_lang." + staticDataBaseObject.quoteName("short_description");
        selectQuery += ", analyze_lang." + staticDataBaseObject.quoteName("available_now") + ", analyze_lang." + staticDataBaseObject.quoteName("available_later");
        selectQuery += ", analyze_lab." + staticDataBaseObject.quoteName("default_category_id") + ", analyze." + staticDataBaseObject.quoteName("supplier_id");
        selectQuery += ", analyze.";
        //TODO continue edit
        return  new ArrayList<>();
    }

    /**
     * Apply analyze calculations
     */
    protected JeproLabAnalyzeModel applyAnalyzeCalculations(JeproLabAnalyzeModel analyze, JeproLabContext labContext){
        return applyAnalyzeCalculations(analyze, labContext, 0);
    }

    protected JeproLabAnalyzeModel applyAnalyzeCalculations(JeproLabAnalyzeModel analyze, JeproLabContext labContext, int analyzeQuantiy){
        if(analyzeQuantiy == 0){
            analyzeQuantiy = analyze.cart_quantity;
        }

        if(analyze.attribute_eco_tax > 0){
            analyze.eco_tax = analyzeQuantiy.attribute_eco_tax;
        }

        analyze.stock_quantity = analyze.quantity;

        float customizationWeight = JeproLabCustomerModel.getCustomizationWeight(analyze.customization_id);

        if(analyze.analyze_attribute_id > 0 &&  analyze.weight_attribute > 0){
            analyze.weight_attribute += customizationWeight;
            analyze.weight = analyze.weight_attribute;
        }else{
            analyze.weight += customizationWeight;
        }

        int addressId;
        if(JeproLabSettingModel.getStringValue("tax_address_type").equals("invoice_address_id")) {
            addressId = this.invoice_address_id;
        }else{
            addressId = this.delivery_address_id;
        }

        if(!JeproLabAddressModel.addressExists(addressId)){
            addressId = 0;
        }

        if(labContext.laboratory.laboratory_id != analyze.laboratory_id){
            labContext.laboratory = new JeproLabLaboratoryModel(analyze.laboratory_id);
        }

        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId, true);
        int taxRulesGroupId = JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyze.analyze_id, labContext);
        JeproLabTaxModel.JeproLabTaxCalculator taxCalculator = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, taxRulesGroupId).getTaxCalculator();

        JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice = new JeproLabPriceModel.JeproLabSpecificPriceModel();

        analyze.analyze_price.price_with_out_reduction = JeproLabAnalyzeModel.getStaticPrice(
                analyze.analyze_id, true, (analyze.analyze_attribute_id > 0 ? analyze.analyze_attribute_id : 0), 6,
                false, false, analyzeQuantiy, false, (this.customer_id > 0 ? this.customer_id : 0), this.cart_id,
                addressId, specificPrice, true, true, labContext, true, analyze.customization_id
        );

        analyze.analyze_price.price_with_reduction = JeproLabAnalyzeModel.getStaticPrice(
                analyze.analyze_id, true, (analyze.analyze_attribute_id >  0 ? analyze.analyze_attribute_id : 0), 6,
                false, true, analyzeQuantiy, false, (this.customer_id > 0 ? this.customer_id : 0), this.cart_id,
                addressId, specificPrice, true, true, labContext, true, analyze.customization_id
        );

        analyze.analyze_price.price = JeproLabAnalyzeModel.getStaticPrice(
                analyze.analyze_id, false, (analyze.analyze_attribute_id > 0 ? analyze.analyze_attribute_id : 0), 6,
                false, true, analyzeQuantiy, false, (this.customer_id > 0 ? this.customer_id : 0), this.cart_id,
                addressId, specificPrice, true, true, labContext, true, analyze.customization_id
        );

        switch (JeproLabSettingModel.getIntValue("round_type")){
            case JeproLabRequestModel.ROUND_TOTAL :
                analyze.analyze_price.total = analyze.analyze_price.total * analyzeQuantiy;
                analyze.analyze_price.total_with_tax = analyze.analyze_price.price_with_reduction * analyzeQuantiy;
                break;
            case JeproLabRequestModel.ROUND_LINE:
                analyze.analyze_price.total = JeproLabTools.roundPrice(analyze.analyze_price.price_with_reduction_without_tax * analyzeQuantiy,
                        JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                analyze.analyze_price.total_with_tax = JeproLabTools.roundPrice(analyze.analyze_price.price_with_reduction,
                        JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                break;
            case JeproLabRequestModel.ROUND_ITEM:
            default:
                analyze.analyze_price.total = JeproLabTools.roundPrice(analyze.analyze_price.price_with_out_reduction,
                        JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION) * analyzeQuantiy;
                analyze.analyze_price.total_with_tax = JeproLabTools.roundPrice(analyze.analyze_price.price_with_reduction,
                        JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION) * analyzeQuantiy;
        }

        analyze.analyze_price.price_with_out_tax = analyze.analyze_price.price_with_reduction;
        //TODO analyze.short_description = JeproLabTools.s

        // Check if a image associated with the attribute exists
        if(analyze.analyze_attribute_id > 0){
            row = JeproLabImageModel.getBestImageAttribute(analyze.laboratory_id, this.language_id, analyze.analyze_id, analyze.analyze_attribute_id);
            if(row){

            }
        }

        analyze.analyze_price.reduction_applies = specificPrice.reduction > 0;
        analyze.analyze_price.quantity_discount_applies = (analyzeQuantiy >= specificPrice.from_quantity);
        analyze.image_id = JeproLabAnalyzeModel.defineAnalyzeImage(analyze, this.language_id);
        analyze.allow_tight_request = JeproLabAnalyzeModel.isAvailableWhenOutOfStock(analyze.out_of_stock);
        analyze.features = JeproLabAnalyzeModel.getStaticFeatures(analyze.analyze_id);

        if(JeproLabCartModel._attributes_lists.containsKey(analyze.analyze_attribute_id + "_" + this.language_id)){
            Jepro
        }
        return JeproLabAnalyzeModel.getTaxesInformation(analyze, labContext);
    }


    public static void cacheSomeAttributesLists(List<Integer> analyzeAttributeIds, int langId){
        if(JeproLabCombinationModel.isFeaturePublished()){
            List<Integer> implodedIds = new ArrayList<>();
            for(int analyzeAttributeId : analyzeAttributeIds){
                if(analyzeAttributeId > 0 && !JeproLabCartModel._attributes_lists.containsKey(analyzeAttributeId + "_" + langId)){
                    implodedIds.add(analyzeAttributeId);
                    HashMap<String, String> attribute = new HashMap<>();
                    attribute.put("attributes", "");
                    attribute.put("attributes_small", "");
                    JeproLabCartRuleModel._attributes_lists.put(analyzeAttributeId + "_" + langId, attribute);
                }
            }

            if(implodedIds.size() > 0){
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT analyze_attribute_combination." + staticDataBaseObject.quoteName("analyze_attribute_id") + ", attribute_group_lang.";
                query += staticDataBaseObject.quoteName("public_name") + " AS public_group_name, attribute_lang." + staticDataBaseObject.quoteName("name");
                query += " AS attribute_name FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination") + " AS analyze_attribute_combination ";
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute") + " AS attribute ON (attribute." + staticDataBaseObject.quoteName("attribute_id");
                query += " = analyze_attribute_combination." + staticDataBaseObject.quoteName("attribute_id") + ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute_group");
                query += " AS attribute_group ON (attribute_group." + staticDataBaseObject.quoteName("attribute_group_id") + " = attribute.";
                query += staticDataBaseObject.quoteName("attribute_id") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute_lang") ;
                query += " AS attribute_lang ON (attribute_lang." + staticDataBaseObject.quoteName("attribute_id") + " = attribute." + staticDataBaseObject.quoteName("attribute_id");
                query += " AND attribute_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute_group_lang");
                query += " AS attribute_group_lang ON (attribute_group." + staticDataBaseObject.quoteName("attribute_group_id") + " = attribute_group_lang.";
                query += staticDataBaseObject.quoteName("attribute_group_id") + " AND attribute_group_lang." + staticDataBaseObject.quoteName("attribute_group_id");
                query += " AND attribute_group_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE analyze_attribute_combination.";
                query += staticDataBaseObject.quoteName("analyze_attribute_id") + " IN (" + implodedIds + ") ORDER BY attribute_group." + staticDataBaseObject.quoteName("position");
                query += " ASC, attribute." + staticDataBaseObject.quoteName("position") + " ASC ";

                staticDataBaseObject.setQuery(query);
                ResultSet resultSet = staticDataBaseObject.loadObjectList();

                if(resultSet != null){
                    try{
                        int analyzeAttributeId;
                        HashMap<String, String> attributeMaP;
                        while(resultSet.next()){
                            analyzeAttributeId = resultSet.getInt("analyze_attribute_id");
                            attributeMaP = new HashMap<>();
                            attributeMaP.put("attributes", resultSet.getString("public_group_name") + " : " + resultSet.getString("attribute_name"));
                            attributeMaP.put("attributes_small", resultSet.getString("attribute_name"));
                            JeproLabCartRuleModel._attributes_lists.put(analyzeAttributeId + "_" + langId, attributeMaP);
                        }
                    }catch (SQLException ignored){
                        ignored.printStackTrace();
                    }finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }


            }
        }
    }

    /**
     * Check if addresses in the JeproLabCartModel are still valid and update with the next valid address id found
     *
     */
    public boolean checkAndUpdateAddresses(){
        boolean needToBeUpdated = false;
        if(this.delivery_address_id != 0 && !JeproLabAddressModel.isValid(this.delivery_address_id)){
            this.delivery_address_id = 0;
            needToBeUpdated = true;
        }
        if(this.invoice_address_id != 0 && !JeproLabAddressModel.isValid(this.invoice_address_id)){
            this.invoice_address_id = 0;
            needToBeUpdated = true;
        }

        if(needToBeUpdated && this.cart_id > 0){
            return this.updateCart();
        }
        return true;
    }


    /**
     * Get the number of analyzes in the cart
     *
     */
    public int getNumberOfAnalyzes(){
        if(this.cart_id <= 0){
            return  0;
        }
        return JeproLabCartModel.getNumberOfAnalyzes(this.cart_id);
    }

    /**
     *
     */
    public static int getNumberOfAnalyzes(int cartId){
        if(JeproLabCartModel._number_of_analyzes.containsKey(cartId) && JeproLabCartModel._number_of_analyzes.get(cartId) != null){
            return JeproLabCartModel._number_of_analyzes.get(cartId);
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT SUM(" + staticDataBaseObject.quoteName("quantity") + ") AS qty FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE " ;
        query += staticDataBaseObject.quoteName("cart_id") + " = " + cartId;

        staticDataBaseObject.setQuery(query);

        int count = (int)staticDataBaseObject.loadValue("qty");
        JeproLabCartModel._number_of_analyzes.put(cartId, count);

        return count;
    }

    /**
     * Add JeproLabCartRuleModel to the JeproLabCartModel
     */
    public boolean addCartRule(int cartRuleId){
        JeproLabCartRuleModel cartRule = new JeproLabCartRuleModel(cartRuleId);

        if(!JeproLabTools.isLoadedObject(cartRule, "cart_rule_id")){
            return false;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule");
        query += " WHERE " + staticDataBaseObject.quoteName("cart_rule_id") + " = " + cartRuleId + " AND " + staticDataBaseObject.quoteName("cart_id");
        query += this.cart_id;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObjectList();
        if(resultSet != null){
            try{
                if (resultSet.next()){
                    return false;
                }else{
                    query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + "(" + staticDataBaseObject.quoteName("cart_id");
                    query += ",  " + staticDataBaseObject.quoteName("cart_rule_id") + ") VALUES (" + this.cart_id + ", " + cartRuleId + ")";

                    staticDataBaseObject.setQuery(query);
                    if(!staticDataBaseObject.query(false)) {
                        return false;
                    }

                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_ALL);
                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_SHIPPING);
                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_REDUCTION);
                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_GIFT);

                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_GIFT + "_ids");
                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_ALL + "_ids");
                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_REDUCTION + "_ids");
                    JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_SHIPPING + "_ids");

                    if(cartRule.gift_analyze){
                        this.updateQuantity(1, cartRule.gift_anlyze, cartRule.gift_analyze_attribute, false, "up", null, false);
                    }
                    return true;
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean containsAnalyze(int analyzeId){
        return containsAnalyze(analyzeId, 0, 0, 0);
    }

    public boolean containsAnalyze(int analyzeId, int analyzeAttributeId){
        return containsAnalyze(analyzeId, analyzeAttributeId, 0, 0);
    }

    public boolean containsAnalyze(int analyzeId, int analyzeAttributeId, int customizationId){
        return containsAnalyze(analyzeId, analyzeAttributeId, customizationId, 0);
    }

    public boolean containsAnalyze(int analyzeId, int analyzeAttributeId, int customizationId, int addressDeliveryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT cart_analyze." + staticDataBaseObject.quoteName("quantity") + " AS qty FROM " ;
        query += staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS cart_analyze ";

        if(customizationId > 0){
            query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " AS customization ON (customization.";
            query += staticDataBaseObject.quoteName("analyze_id") + " = cart_analyze." + staticDataBaseObject.quoteName("analyze_id") ;
            query += " AND customization." + staticDataBaseObject.quoteName("analyze_attribute_id") + " = cart_analyze.";
            query += staticDataBaseObject.quoteName("analyze_attribute_id") + ") ";
        }

        query += " WHERE cart_analyze." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND cart_analyze.";
        query += staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId + " AND cart_analyze.";
        query += staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

        if(JeproLabSettingModel.getIntValue("allow_multiple_result") > 0 && this.isMultiDeliveryAddress()){
            query += " AND customization." + staticDataBaseObject.quoteName("customization_id");
        }

        if(customizationId > 0){
            query += " AND customization." + staticDataBaseObject.quoteName("customization_id");
        }

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadValue("qty") > 0;
    }

    public boolean updateQuantity(int quantity, int analyzeId){
        return updateQuantity(quantity, analyzeId, 0, 0, "up", 0, null, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId){
        return updateQuantity(quantity, analyzeId, analyzeAttributeId, 0, "up", 0, null, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId){
        return updateQuantity(quantity, analyzeId, analyzeAttributeId, customizationId, "up", 0, null, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator){
        return updateQuantity(quantity, analyzeId, analyzeAttributeId, customizationId, operator, 0, null, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator, int addressId){
        return  updateQuantity(quantity, analyzeId, analyzeAttributeId, customizationId, operator, addressId, null, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator, int addressId, JeproLabLaboratoryModel lab){
        return updateQuantity(quantity, analyzeId, analyzeAttributeId, customizationId, operator, addressId, lab, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator, int addressId, JeproLabLaboratoryModel lab, boolean autoAddCartRule){
        if(lab == null) {
            lab = JeproLabContext.getContext().laboratory;
        }

        if(JeproLabContext.getContext().customer.customer_id > 0){
            if(addressId == 0 && this.delivery_address_id > 0){
                addressId = this.delivery_address_id;
            }else if(addressId == 0){
                addressId = JeproLabAddressModel.getCustomerFirstAddressId(JeproLabContext.getContext().customer.customer_id)
            }else if(!JeproLabCustomerModel.customerHasAddress(JeproLabContext.getContext().customer.customer_id, addressId)){
                addressId = 0;
            }
        }

        JeproLabCombinationModel combination;

        if(analyzeAttributeId > 0){
            combination = new JeproLabCombinationModel(analyzeAttributeId);
            if(combination.analyze_id != analyzeId){
                return false;
            }
        }

        JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId, false, JeproLabSettingModel.getIntValue("default_lang"), lab.laboratory_id);
        int minimalQuantity;

        /** If we have an analyze combination, the minimal quantity is set with the one of this combination **/
        if(analyzeAttributeId <= 0){
            minimalQuantity = JeproLabAttributeModel.getAttributeMinimalQuantity(analyzeAttributeId);
        }else{
            minimalQuantity = analyze.minmal_quantity;
        }

        if(!JeproLabTools.isLoadedObject(analyze, "analyze_id")){
            //todo
        }

        if(JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)){
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if(!JeproLabCartModel._total_weight.containsKey(this.cart_id)){
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }

        if(quantity <= 0){
            return this.deleteAnalyze(analyzeId, analyzeAttributeId, customizationId);
        }else if(!analyze.available_for_order){
            return false;
        }else {
            boolean result = this.containsAnalyze(analyzeId, analyzeAttributeId, customizationId, addressId);
            /**
             * update quantity if analyze already exists
             */
            if(result){
                if(operator.equals("up")){
                    if(staticDataBaseObject == null){
                        staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query = "SELECT stock." + staticDataBaseObject.quoteName("out_of_stock") + ", IFNULL(stock.";
                    query += staticDataBaseObject.quoteName("quantity") + ", 0) as quantity FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze");
                    query += " AS analyze " + JeproLabAnalyzeModel.queryStock("analyze", analyzeAttributeId, true, lab) + " WHERE analyze.";
                    query += staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

                    staticDataBaseObject.setQuery(query);
                    ResultSet resultSet = staticDataBaseObject.loadObjectList();
                    int analyzeQuantity = 0; //(int)staticDataBaseObject.loadValue("quantity");
                    int outOfStock = 0;
                    if(resultSet != null){
                        try{
                            while(resultSet.next()){
                                analyzeQuantity = resultSet.getInt("quantity");
                                outOfStock = resultSet.getInt("out_of_stock");
                            }
                        }catch(SQLException ignored){
                            ignored.printStackTrace();
                        }finally {
                            try{
                                JeproLabDataBaseConnector.getInstance().closeConnexion();
                            } catch (Exception ignored) {
                                ignored.printStackTrace();
                            }
                        }
                    }
                    if(JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId)){
                        analyzeQuantity = JeproLabAnalyzeModel.JeproLabAnalyzePackModel.getQuantity(analyzeId, analyzeAttributeId);
                    }

                    int newQuantity = quantity; //// TODO: 11/21/16

                    if(!JeproLabAnalyzeModel.isAvailableWhenOutOfStock(outOfStock)){
                        if(newQuantity > analyzeQuantity){
                            return false;
                        }
                    }
                }
            }
        }
    }

    public static class JeproLabCartRuleModel extends JeproLabModel{
        public int cart_rule_id;

        public int language_id;

        public Map<String, String> name = new HashMap<>();
        /* Filters used when retrieving the cart rules applied to a cart of when calculating the value of a reduction */
        public static final int FILTER_ACTION_ALL = 1;
        public static final int FILTER_ACTION_SHIPPING = 2;
        public static final int FILTER_ACTION_REDUCTION = 3;
        public static final int FILTER_ACTION_GIFT = 4;
        public static final int FILTER_ACTION_ALL_NOCAP = 5;

        public final String BO_ORDER_CODE_PREFIX = "BO_ORDER_";


        /**
         * @param customerId the customer unique identifier
         * @return bool
         */
        public static boolean deleteByCustomerId(int customerId){
            boolean result = true;
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule ";
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId;

            staticDataBaseObject.setQuery(query);
            result = staticDataBaseObject.query(false);

            return result;
        }

        /* When an entity associated to a analyze rule (product, category, attribute, supplier, manuacturer...) is deleted, the product rules must be updated */
        public static boolean cleanAnalyzeRuleIntegrity(String type, List<Integer> list){
            String[] fieldTypes = {"analyzes", "categories", "attributes", "manufacturers", "suppliers"};
            // Type must be available in the 'type' enum of the table cart_rule_product_rule
            if (!Arrays.asList(fieldTypes).contains(type)) {
                return false;
            }
/*
        // This check must not be removed because this var is used a few lines below
        $list = (is_array($list) ? implode(',', array_map('intval', $list)) : (int)$list);
        if (!preg_match('/^[0-9,]+$/', $list)) {
            return false;
        }

        // Delete associated restrictions on cart rules
        String query = "DELETE cart_rule_analyze_rule_value FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule");
        query += " AS cart_rule_analyze_rule LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value");
        query += " AS cart_rule_analyze_rule_value ON cart_rule_analyze_rule." + staticDataBaseObject.quoteName("analyze_rule_id") + " =";
        query += "cart_rule_analyze_rule_value." + staticDataBaseObject.quoteName("analyze_rule_id") + " WHERE cart_rule_analyze_rule.";
        query += staticDataBaseObject.quoteName("type") + " = " + staticDataBaseObject.quoteName(type) + " AND cart_rule_analyze_rule_value.";
        query += staticDataBaseObject.quoteName("item_id") + " IN (" + itemList + ")";

        staticDataBaseObject.setQuery(query);

        Db::getInstance()->execute('
            '); // $list is checked a few lines above

        // Delete the product rules that does not have any values
        if (staticDataBaseObject.executeQuery() > 0) {
            query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule") + " NOT EXISTS (SELECT";
            query += " 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value") + " WHERE ";
            query += staticDataBaseObject.quoteName(".'cart_rule_product_rule`.`id_product_rule` = `'.dataBaseObject.quoteName(".'cart_rule_product_rule_value`.`id_product_rule`)');
        }
        // If the product rules were the only conditions of a product rule group, delete the product rule group
        if (Db::getInstance()->Affected_Rows() > 0) {
        Db::getInstance()->delete('cart_rule_product_rule_group', 'NOT EXISTS (SELECT 1 FROM `'.dataBaseObject.quoteName(".'cart_rule_product_rule`
        WHERE `'.dataBaseObject.quoteName(".'cart_rule_product_rule`.`id_product_rule_group` = `'.dataBaseObject.quoteName(".'cart_rule_product_rule_group`.`id_product_rule_group`)');
    }

        // If the product rule group were the only restrictions of a cart rule, update de cart rule restriction cache
        if (Db::getInstance()->Affected_Rows() > 0){
            query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_group") + " AS cart_rule_analyze_rule_group ";
            query += " ON cart_rule.cart_rule_id = cart_rule_analyze_rule_group.cart_rule_id SET analyze_restriction = IF(";
            query += " cart_rule_analyze_rule_group.analyze_rule_group_id IS NULL, 0, 1)";

            staticDataBaseObject.setQuery(query);
            staticDataBaseObject.query(false);
        }*/
            return true;
        }
    }
}