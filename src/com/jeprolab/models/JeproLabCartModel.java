package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
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

    private static Map<String, Float> _total_shipping;

    protected int tax_calculation_method = JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED;

    protected static Map<Integer, Integer>_number_of_analyzes = new HashMap<>();

    protected static Map<String, HashMap<String, String>> _attributes_lists = new HashMap<>();

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

    public boolean requestExists(){
        String cacheKey = "jeprolab_cart_model_request_exists_" + this.cart_id;
        if(!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }
            String query = "SELECT count(*) request FROM " + staticDataBaseObject.quoteName("#__jeprolab_request") ;
            query += " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

            staticDataBaseObject.setQuery(query);
            boolean result = (int)staticDataBaseObject.loadValue("request") > 0;
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }


    public float getAverageAnalyzesTaxRate(){ return getAverageAnalyzesTaxRate(0, 0); }

    public float getAverageAnalyzesTaxRate(float cartAmountTaxExcluded){ return  getAverageAnalyzesTaxRate(cartAmountTaxExcluded, 0); }

    /**
     *
     * @param cartAmountTaxExcluded price without tax
     * @param cartAmountTaxIncluded price with tax
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

    public float getDiscounts(){
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
            analyzeAttributeIds.stream().filter(analyzeAttributeId -> analyzeAttributeId > 0 && !JeproLabCartModel._attributes_lists.containsKey(analyzeAttributeId + "_" + langId)).forEach(analyzeAttributeId -> {
                implodedIds.add(analyzeAttributeId);
                HashMap<String, String> attribute = new HashMap<>();
                attribute.put("attributes", "");
                attribute.put("attributes_small", "");
                JeproLabCartRuleModel._attributes_lists.put(analyzeAttributeId + "_" + langId, attribute);
            });

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

                    if(cartRule.gift_analyze_id > 0){
                        this.updateQuantity(1, cartRule.gift_analyze_id, cartRule.gift_analyze_attribute_id, 0, "up", 0, null);
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
            minimalQuantity = analyze.minimal_quantity;
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

        String query, quantityQuery;
        int analyzeQuantity = 0, outOfStock, newQuantity;

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
                    query = "SELECT stock." + staticDataBaseObject.quoteName("out_of_stock") + ", IFNULL(stock.";
                    query += staticDataBaseObject.quoteName("quantity") + ", 0) as quantity FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze");
                    query += " AS analyze " + JeproLabAnalyzeModel.queryStock("analyze", analyzeAttributeId, true, lab) + " WHERE analyze.";
                    query += staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

                    staticDataBaseObject.setQuery(query);
                    ResultSet resultSet = staticDataBaseObject.loadObjectList();
                    analyzeQuantity = 0; //(int)staticDataBaseObject.loadValue("quantity");
                    outOfStock = 0;
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

                    newQuantity = quantity; //// TODO: 11/21/16

                    if(!JeproLabAnalyzeModel.isAvailableWhenOutOfStock(outOfStock)){
                        if(newQuantity > analyzeQuantity){
                            return false;
                        }
                    }
                }else if(operator.equals("down")){
                    quantityQuery = " - " + quantity;
                    newQuantity = //// TODO: 11/21/16
                    if(newQuantity < minimalQuantity && minimalQuantity > 1){
                        return false;
                    }
                }else{
                    return false;
                }

                /** remove analyze from cart **/
                if(newQuantity <= 0){
                    return this.deleteAnalyze(analyzeId, analyzeAttributeId, customizationId);
                }else if(newQuantity < minimalQuantity){
                    return false;
                }else{
                    if(staticDataBaseObject == null){
                        staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " SET " + staticDataBaseObject.quoteName("quantity");
                    query += " = " + staticDataBaseObject.quoteName("quantity") + quantityQuery + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
                    query += " = " + analyzeId + " AND " + staticDataBaseObject.quoteName("customization_id") + " = " + customizationId ;
                    query += (analyzeAttributeId > 0 ? " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                    query += " AND " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;
                    query += (JeproLabSettingModel.getIntValue("allow_multiple_result") && this.isMultiDeliveryAddress() ? " AND " + staticDataBaseObject.quoteName("delivery_address_id") + " = " + addressId : "");
                    query += " LIMIT 1";

                    staticDataBaseObject.setQuery(query);
                    staticDataBaseObject.query(false);
                }
            }else if(operator.equals("up")){
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                query = "SELECT stock." + staticDataBaseObject.quoteName("out_of_stock") + ", IFNULL(stock." + staticDataBaseObject.quoteName("out_of_stock");
                query += ", 0) AS quantity FROM "+ staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ";
                query += JeproLabAnalyzeModel.queryStock("analyze", analyzeAttributeId, true, lab) + " WHERE analyze.";
                query +=  staticDataBaseObject.quoteName("analyze_id") + " = " + addressId;

                ResultSet resultSet = staticDataBaseObject.loadObjectList();
                if(resultSet != null){
                    try{
                        while(resultSet.next()){
                            outOfStock = resultSet.getInt("out_of_stock");
                            analyzeQuantity = resultSet.getInt("quantity");
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

                //Pack analyze quantity
                if(JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId)){
                    analyzeQuantity = JeproLabAnalyzeModel.JeproLabAnalyzePackModel.getQuantity(analyzeId, analyzeAttributeId);
                }

                if(!JeproLabAnalyzeModel.isAvailableWhenOutOfStock(outOfStock)){
                    if(quantity > analyzeQuantity){
                        return false;
                    }
                }

                if(quantity < minimalQuantity){ return false; }

                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " (" + staticDataBaseObject.quoteName("analyze_id");
                query += ", " + staticDataBaseObject.quoteName("analyze_attribute_id") + ", " + staticDataBaseObject.quoteName("cart_id") + ", ";
                query += staticDataBaseObject.quoteName("delivery_address_id") + ", " + staticDataBaseObject.quoteName("lab_id") + ", ";
                query += staticDataBaseObject.quoteName("quantity") + ", " + staticDataBaseObject.quoteName("date_upd") + ", ";
                query += staticDataBaseObject.quoteName("customisation_id") + ") VALUES (" + analyzeId + ", " + analyzeAttributeId + ", ";
                query += this.cart_id + ", " + addressId + ", " + quantity + ", " + staticDataBaseObject.quoteName(JeproLabTools.date());
                query += ", " + customizationId + ")";

                staticDataBaseObject.setQuery(query);
                if(!staticDataBaseObject.query(false)){ return false; }
            }
        }

        /** refresh cache of analyzes **/
        this._analyzes = this.getAnalyzes(true);
        this.updateCart();
        try {
            JeproLabContext context = JeproLabContext.getContext().cloneContext();
            context.cart = this;
            JeproLabCache.getInstance().remove("get_context_value_*");
            if(autoAddCartRule){
                JeproLabCartRuleModel.autoAddToCart(context);
            }

            if(analyze.customizable){
                return this.updateCustomizationQuantity(quantity, customizationId, analyzeId, analyzeAttributeId, addressId, operator);
            }else{
                return true;
            }
        } catch (CloneNotSupportedException ignored) {
            ignored.printStackTrace();
        }
    }




    /**
     *
     * @param quantity int quantity to update
     * @param customizationId
     * @param analyzeId
     * @param analyzeAttributeId
     * @param addressId
     * @return
     */
    protected boolean updateCustomizationQuantity(int quantity, int customizationId, int analyzeId, int analyzeAttributeId, int addressId){
        return updateCustomizationQuantity(quantity, customizationId, analyzeId, analyzeAttributeId, addressId, "up");
    }

    /**
     *
     * @param quantity
     * @param customizationId
     * @param analyzeId
     * @param analyzeAttributeId
     * @param addressId
     * @param operator
     * @return
     */
    protected boolean updateCustomizationQuantity(int quantity, int customizationId, int analyzeId, int analyzeAttributeId, int addressId, String operator){
        String query;
        if(customizationId <= 0){
            List<JeproLabCombinationModel.JeproLabCustomizationModel> customizations = this.getAnalyzeCustomizations(analyzeId, null, true);
            for(JeproLabCombinationModel.JeproLabCustomizationModel customization : customizations){
                if(customization.quantity == 0){
                    if(staticDataBaseObject == null){
                        staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }

                    query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " SET " + staticDataBaseObject.quoteName("quantity");
                    query += " = " + quantity + ", " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId + ", ";
                    query += staticDataBaseObject.quoteName("delivery_address_id") + " = " + addressId + ", " + staticDataBaseObject.quoteName("in_cart");
                    query += " = 1 WHERE " + staticDataBaseObject.quoteName("customization_id") + " = " + customization.customization_id;

                    staticDataBaseObject.setQuery(query);
                    staticDataBaseObject.query(false);
                }
            }
        }

        if(customizationId > 0 && quantity < 1){
            return this.deleteCustomization(customizationId, analyzeId, analyzeAttributeId);
        }

        /** quantity update **/
        int customizationQuantity;
        if(customizationId > 0){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            query = "SELECT " + staticDataBaseObject.quoteName("quantity") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization");
            query += " WHERE " + staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

            staticDataBaseObject.setQuery(query);
            customizationQuantity = (int)staticDataBaseObject.loadValue("quantity");


            if(customizationQuantity > 0){
                if(operator.equals("down") && (customizationQuantity - quantity) < 0){
                    query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " WHERE ";
                    query += staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

                    staticDataBaseObject.quoteName(query);
                    return staticDataBaseObject.query(false);
                }

                query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " SET " + staticDataBaseObject.quoteName("quantity");
                query += " = " + staticDataBaseObject.quoteName("quantity") + (operator.equals("up") ? "+ " : "- ") + quantity + ", ";
                query += staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId + staticDataBaseObject.quoteName("delivery_address_id");
                query += " = " + staticDataBaseObject.quoteName("in_cart") + " = 1 WHERE " + staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

                staticDataBaseObject.setQuery(query);
                return staticDataBaseObject.query(false);
            }else{
                query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " SET " + staticDataBaseObject.quoteName("delivery_address_id");
                query += " = " + addressId + ", " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId + ", ";
                query += staticDataBaseObject.quoteName("in_cart") + " = 1 WHERE " + staticDataBaseObject.quoteName("customization_id") + " = " ;
                query += customizationId;

                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            }
        }
        this._analyzes = this.getAnalyzes(true);
        this.updateCart();
        return true;
    }


    public float getRequestTotal() {
        return getRequestTotal(true, JeproLabCartModel.BOTH, null, 0, true);
    }

    /**
     *
     * @param withTaxes get total price with tax
     * @return
     */
    public float getRequestTotal(boolean withTaxes) {
        return getRequestTotal(withTaxes, JeproLabCartModel.BOTH, null, 0, true);
    }

    /**
     *
     * @param withTaxes get total price with tax
     * @param type      Total type enum
     *                        - JeproLabCartModel.ONLY_ANALYZES
     *                        - JeproLabCartModel.ONLY_DISCOUNTS
     *                        - JeproLabCartModel.BOTH
     *                        - JeproLabCartModel.BOTH_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_SHIPPING
     *                        - JeproLabCartModel.ONLY_WRAPPING
     *                        - JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING
     * @return
     */
    public float getRequestTotal(boolean withTaxes, int type) {
        return getRequestTotal(withTaxes, type, null, 0, true);
    }

    /**
     *
     * @param withTaxes
     * @param type      Total type enum
     *                        - JeproLabCartModel.ONLY_ANALYZES
     *                        - JeproLabCartModel.ONLY_DISCOUNTS
     *                        - JeproLabCartModel.BOTH
     *                        - JeproLabCartModel.BOTH_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_SHIPPING
     *                        - JeproLabCartModel.ONLY_WRAPPING
     *                        - JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING
     * @param analyzes
     * @return
     */
    public float getRequestTotal(boolean withTaxes, int type, List<JeproLabAnalyzeModel> analyzes) {
        return getRequestTotal(withTaxes, type, analyzes, 0, true);
    }

    /**
     *
     * @param withTaxes With or without taxes
     * @param type      Total type enum
     *                        - JeproLabCartModel.ONLY_ANALYZES
     *                        - JeproLabCartModel.ONLY_DISCOUNTS
     *                        - JeproLabCartModel.BOTH
     *                        - JeproLabCartModel.BOTH_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_SHIPPING
     *                        - JeproLabCartModel.ONLY_WRAPPING
     *                        - JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING
     * @param analyzes
     * @param carrierId
     * @return
     */
    public float getRequestTotal(boolean withTaxes , int type, List<JeproLabAnalyzeModel> analyzes, int carrierId) {
        return getRequestTotal(withTaxes, type, analyzes, carrierId, true);
    }

    /**
     * This function returns the total cart amount
     *
     * @param withTaxes With or without taxes
     * @param type      Total type enum
     *                        - JeproLabCartModel.ONLY_ANALYZES
     *                        - JeproLabCartModel.ONLY_DISCOUNTS
     *                        - JeproLabCartModel.BOTH
     *                        - JeproLabCartModel.BOTH_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_SHIPPING
     *                        - JeproLabCartModel.ONLY_WRAPPING
     *                        - JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING
     *                        - JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING
     * @param analyzes
     * @param carrierId
     * @param useCache Allow using cache of the method CartRule::getContextualValue
     *
     * @return float Order total
     */
    public float getRequestTotal(boolean withTaxes, int type, List<JeproLabAnalyzeModel> analyzes, int carrierId, boolean useCache) {
        // Dependencies
        $price_calculator = ServiceLocator::get ('\\PrestaShop\\PrestaShop\\Adapter\\Product\\PriceCalculator');

        boolean useUcotax = JeproLabSettingModel.getIntValue("use_ecotax") > 0;
        int roundType = JeproLabSettingModel.getIntValue("round_type");
        int ecotaxTaxRulesGroupId = JeproLabSettingModel.getIntValue("ECOTAX_TAX_RULES_GROUP_ID");
        int computePrecision = JeproLabSettingModel.getIntValue("price_compute_precision");

        if(this.cart_id <= 0){ return 0; }

        List<Integer> typeArray = new ArrayList<>();
        typeArray.addAll(JeproLabCartModel.ONLY_ANALYZES, JeproLabCartModel.ONLY_DISCOUNTS, JeproLabCartModel.BOTH,
                JeproLabCartModel.BOTH_WITHOUT_SHIPPING, JeproLabCartModel.ONLY_SHIPPING, JeproLabCartModel.ONLY_WRAPPING,
                JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING, JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING);

        // Define virtual context to prevent case where the cart is not the in the global context
        try {
            JeproLabContext virtualContext = JeproLabContext.getContext().cloneContext();
            virtualContext.cart = this;

            if (!typeArray.contains(type)) {
               return 0; // die(Tools::displayError ());
            }

            boolean withShipping = (type == JeproLabCartModel.BOTH || type == JeproLabCartModel.ONLY_SHIPPING);

            // if cart rules are not used
            if (type == JeproLabCartModel.ONLY_DISCOUNTS && !JeproLabCartRuleModel.isFeaturePublished()){
                return 0;
            }

            // no shipping cost if is a cart with only virtuals analyzes
            boolean isVirtual = this.isVirtualCart();
            if (isVirtual && type == JeproLabCartModel.ONLY_SHIPPING) {
                return 0;
            }

            if (isVirtual && type == JeproLabCartModel.BOTH) {
                type = JeproLabCartModel.BOTH_WITHOUT_SHIPPING;
            }

            float shippingFees = 0;
            if (withShipping || type == JeproLabCartModel.ONLY_DISCOUNTS) {
                if (analyzes == null && carrierId == 0) {
                    shippingFees = this.getTotalShippingCost(null, withTaxes);
                } else {
                    shippingFees = this.getPackageShippingCost(carrierId, withTaxes, null, analyzes);
                }
            }

            if (type == JeproLabCartModel.ONLY_SHIPPING) {
                return shippingFees;
            }

            if (type == JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING) {
                type = JeproLabCartModel.ONLY_ANALYZES;
            }

            $param_product = true;
            if (analyzes == null) {
                $param_product = false;
                analyzes = this.getAnalyzes();
            }

            if (type == JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING){
                for(JeproLabAnalyzeModel analyze : analyzes){
                    if (analyze.is_virtual) {
                        analyzes.remove(analyze);
                    }
                }
                type = JeproLabCartModel.ONLY_ANALYZES;
            }

            float requestTotal = 0;
            if (JeproLabTaxModel.excludeTaxOption()){
                withTaxes = false;
            }

            Map<String, Float> analyzesTotal = new HashMap<>();
            float ecotaxTotal = 0;
            Map<String, Integer> analyzeLines = this.countAnalyzeLines(analyzes);

            for(JeproLabAnalyzeModel analyze : analyzes) {
                // products refer to the cart details

                if (analyze.is_gift) {
                    // products given away may appear twice if added manually
                    // so we prevent adding their subtotal twice if another line is found
                    String analyzeIndex = analyze.analyze_id + "_" + analyze.analyze_attribute_id;
                    if (analyzeLines.get(analyzeIndex) > 1) {
                        continue;
                    }
                }

                if (virtualContext.laboratory.laboratory_id != analyze.laboratory_id) {
                    virtualContext.laboratory = new JeproLabLaboratoryModel(analyze.laboratory_id);
                }

                int addressId = this.getAnalyzeAddressId(analyze);

                // The $null variable below is not used,
                // but it is necessary to pass it to getProductPrice because
                // it expects a reference.
                JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice = new JeproLabPriceModel.JeproLabSpecificPriceModel();
                float price = priceCalculator.getAnalyzePrice( analyze.analyze_id, withTaxes, analyze.analyze_attribute_id,
                        6, null, false, true, analyze.cart_quantity, false, this.customer_id > 0 ? this.customer_id : 0,
                        this.cart_id, addressId, specificPrice, useEcotax, true, virtualContext, true, analyze.customization_id
                );

                int taxRulesGroupId = this.findTaxRulesGroupId(withTaxes, analyze, virtualContext);

                if (roundType == JeproLabRequestModel.ROUND_ITEM || roundType == JeproLabRequestModel.ROUND_LINE){
                    if (!analyzesTotal.containsKey(taxRulesGroupId + "")) {
                        analyzesTotal.put(taxRulesGroupId + "", (float)0);
                    }
                }else if(!analyzesTotal.containsKey(taxRulesGroupId + "_" + addressId)){
                    analyzesTotal.put(taxRulesGroupId + "_" + addressId, (float)0);
                }

                switch (roundType) {
                    case JeproLabRequestModel.ROUND_TOTAL:
                        analyzesTotal.put(taxRulesGroupId + "_" + addressId, analyzesTotal.get(taxRulesGroupId + "_" + addressId) + (price * analyze.cart_quantity));
                        break;

                    case JeproLabRequestModel.ROUND_LINE:
                        float analyzePrice  = price * analyze.cart_quantity;
                        analyzesTotal.put(
                                taxRulesGroupId + "",
                                (analyzesTotal.get(taxRulesGroupId + "") + JeproLabTools.roundPrice(analyzePrice, computePrecision)));
                        break;

                    case JeproLabRequestModel.ROUND_ITEM:
                    default:
                        float analyzePrice = withTaxes ? taxCalculator.addTaxes(price) : price;
                        analyzesTotal.put(taxRulesGroupId + "",
                                (analyzesTotal.get(taxRulesGroupId + "") + (JeproLabTools.roundPrice(analyzePrice, computePrecision)* analyze.cart_quantity)));
                        break;
                }
            }

            for(Map.Entry<String, Float> entry : analyzesTotal.entrySet()){
                requestTotal += entry.getValue();
            }

            float requestTotalAnalyzes = requestTotal;

            if (type == JeproLabCartModel.ONLY_DISCOUNTS) {
                requestTotal = 0;
            }

            float wrappingFees = this.calculateWrappingFees(withTaxes, type);
            if (type == JeproLabCartModel.ONLY_WRAPPING) {
                return wrappingFees;
            }

            float requestTotalDiscount = 0;
            float requestShippingDiscount = 0;
            if (((type != JeproLabCartModel.ONLY_SHIPPING && type != JeproLabCartModel.ONLY_ANALYZES)) && JeproLabCartRuleModel.isFeaturePublished()){
                List<JeproLabCartRuleModel> cartRules = this.getTotalCalculationCartRules(type, withShipping);

                Map<String, Object> pack = new HashMap<>();
                pack.put("carrier_id", carrierId);
                pack.put("address_id", this.getDeliveryAddressId(analyzes));
                pack.put("analyzes", analyzes);

                // Then, calculate the contextual value for each one
                boolean flag = false;
                for(JeproLabCartRuleModel cartRule : cartRules) {
                    // If the cart rule offers free shipping, add the shipping cost
                    if ((withShipping || type == JeproLabCartModel.ONLY_DISCOUNTS) && cartRule.free_shipping && !flag){
                        requestShippingDiscount = JeproLabTools.roundPrice
                        (cartRule.getContextualValue(withTaxes, virtualContext, JeproLabCartRuleModel.FILTER_ACTION_SHIPPING, ($param_product ? $package : null), useCache), computePrecision);
                        flag = true;
                    }

                    // If the cart rule is a free gift, then add the free gift value only if the gift is in this package
                    if (!this.should_exclude_gifts_discount && cartRule.gift_analyze_id > 0){
                        boolean inRequest = false;
                        if (analyzes == null) {
                            inRequest = true;
                        } else {
                            for(JeproLabAnalyzeModel analyze : analyzes) {
                                if (cartRule.gift_analyze_id == analyze.analyze_id && cartRule.gift_analyze_attribute_id == analyze.analyze_attribute_id){
                                    inRequest = true;
                                }
                            }
                        }

                        if (inRequest) {
                            requestTotalDiscount += cartRule.getContextualValue(withTaxes, virtualContext, JeproLabCartRuleModel.FILTER_ACTION_GIFT, $package, useCache);
                        }
                    }

                    // If the cart rule offers a reduction, the amount is prorated (with the products in the package)
                    if (cartRule.reduction_percent > 0 || cartRule.reduction_amount > 0){
                        requestTotalDiscount += JeproLabTools.roundPrice
                        (cartRule.getContextualValue(withTaxes, virtualContext, JeproLabCartRuleModel.FILTER_ACTION_REDUCTION, $package, useCache), computePrecision)
                        ;
                    }
                }

                requestTotalDiscount = Math.min(JeproLabTools.roundPrice(requestTotalDiscount, 2), requestTotalAnalyzes)
                + requestShippingDiscount;
                requestTotal -= requestTotalDiscount;
            }

            if (type == JeproLabCartModel.BOTH) {
                requestTotal += shippingFees + wrappingFees;
            }

            if (requestTotal < 0 && type != JeproLabCartModel.ONLY_DISCOUNTS) {
                return 0;
            }

            if (type == JeproLabCartModel.ONLY_DISCOUNTS) {
                return requestTotalDiscount;
            }

            return JeproLabTools.roundPrice(requestTotal, computePrecision);
        }catch(CloneNotSupportedException ignored){
            ignored.printStackTrace();
        }
        return 0;
    }


    /**
     * Check if cart contains only virtual products
     *
     * @return bool true if is a virtual cart or false
     */
    public boolean isVirtualCart(){
        if (!JeproLabAnalyzeModel.JeproLabAnalyzeDownloadModel.isFeaturePublished()){
            return false;
        }

        if (!JeproLabCartModel._isVirtualCart.containsKey(this.cart_id)) {
            List<JeproLabAnalyzeModel> analyzes = this.getAnalyzes();
            if (analyzes.size() <= 0) {
                return false;
            }

            boolean isVirtual = true;
            for(JeproLabAnalyzeModel analyze : analyzes) {
                if (!analyze.is_virtual) {
                    isVirtual = false;
                }
            }
            JeproLabCartModel._isVirtualCart.put(this.cart_id, isVirtual);
        }

        return JeproLabCartModel._isVirtualCart.get(this.cart_id);
    }

    public float getTotalShippingCost($delivery_option = null){
        return getTotalShippingCost(true, null);
    }

    public float getTotalShippingCost(deliveryOption, boolean useTax){
        return getTotalShippingCost(deliveryOption, useTax, null);
    }

    /**
     * Return shipping total for the cart
     *
     * @param deliveryOption Array of the delivery option for each address
     * @param useTax         Use taxes
     * @param defaultCountry Default Country
     *
     * @return float Shipping total
     */
    public float getTotalShippingCost(deliveryOtion, boolean useTax, JeproLabCountryModel defaultCountry = null){

        if (JeproLabCartModel._total_shipping == null) {
            if (JeproLabContext.getContext().cookie.country_id){
                defaultCountry = new JeproLabCountryModel(JeproLabContext.getContext().cookie.country_id);
            }
            if (deliveryOption == null) {
                deliveryOption = this.getDeliveryOption(defaultCountry, false, false);
            }

            JeproLabCartModel._total_shipping = new HashMap<>();
            JeproLabCartModel._total_shipping.put("with_tax", (float) 0);
            JeproLabCartModel._total_shipping.put("with_out_tax", (float)0);
            deliveryOptionList = this.getDeliveryOptionList(defaultCountry);
            for(deliveryOption as $id_address => $key) {
                if (!deliveryOptionList.containsKey(addressId) || !deliveryOptionList.containsKey.get(addressId).containsKey(key)) {
                    continue;
                }

                JeproLabCartModel._total_shipping.put("with_tax",
                        (JeproLabCartModel._total_shipping.get("with_tax") + deliveryOptionList).get(key).get("total_price_with_tax"));
                //+= $delivery_option_list[$id_address][$key]['total_price_with_tax'];
                JeproLabCartModel._total_shipping.put("with_out_tax",
                        (JeproLabCartModel._total_shipping.get("with_out_tax") + deliveryOptionList.get(key).get("total_price_without_tax")));
                //+= $delivery_option_list[$id_address][$key]['total_price_without_tax'];
            }
        }

        return (useTax) ? JeproLabCartModel._total_shipping.get("with_tax") : JeproLabCartModel._total_shipping.get("with_out_tax");
    }

    /**
     * Get the delivery option selected, or if no delivery option was selected,
     * the cheapest option for each address
     *
     * @param defaultCountry       Default country
     * @param bool         $dontAutoSelectOptions Do not auto select delivery option
     * @param bool         $use_cache             Use cache
     *
     * @return array|bool|mixed Delivery option
     */
    public function getDeliveryOption(JeproLabCountryModel defaultCountry = null, boolean doNotAutoSelectOptions = false, boolean useCache = true) {
        static $cache=array();
        String cacheKey = (int) (is_object($default_country) ? $default_country -> id : 0). '-'. (int) $dontAutoSelectOptions;
        if (JeproLabCache.getInstance().isStored(cacheKey) && useCache) {
            return JeproLabCache.getInstance().retrieve(cacheKey);
        }

        deliveryOptionList = this.getDeliveryOptionList(defaultCountry);

        // The delivery option was selected
        if (isset($this -> delivery_option) && $this -> delivery_option != '') {
            deliveryOption = JeproLabTools.unSerialize(this.delivery_option);
            boolean validated = true;
            foreach($delivery_option as $id_address = > $key){
                if (!isset($delivery_option_list[$id_address][$key])) {
                    $validated = false;
                    break;
                }
            }

            if (validated) {
                $cache[$cache_id] = $delivery_option;
                return deliveryOption;
            }
        }

        if (doNotAutoSelectOptions) {
            return false;
        }

        // No delivery option selected or delivery option selected is not valid, get the better for all options
        $delivery_option = array();
        foreach($delivery_option_list as $id_address = > $options){
            foreach($options as $key = > $option){
                if (Configuration::get ('PS_CARRIER_DEFAULT') == -1 && $option['is_best_price']){
                    $delivery_option[$id_address] = $key;
                    break;
                }
                elseif(Configuration::get ('PS_CARRIER_DEFAULT') == -2 && $option['is_best_grade']){
                    $delivery_option[$id_address] = $key;
                    break;
                }
                elseif($option['unique_carrier'] && in_array(Configuration::get
                ('PS_CARRIER_DEFAULT'), array_keys($option['carrier_list']))){
                    $delivery_option[$id_address] = $key;
                    break;
                }
            }

            reset($options);
            if (!isset($delivery_option[$id_address])) {
                $delivery_option[$id_address] = key($options);
            }
        }

        $cache[$cache_id] = $delivery_option;

        return deliveryOption;
    }


    /**
     * Get all deliveries options available for the current cart
     * @param Country $default_country
     * @param bool $flush Force flushing cache
     *
     * @return array array(
     *                   0 => array( // First address
     *                       '12,' => array(  // First delivery option available for this address
     *                           carrier_list => array(
     *                               12 => array( // First carrier for this option
     *                                   'instance' => Carrier Object,
     *                                   'logo' => <url to the carriers logo>,
     *                                   'price_with_tax' => 12.4,
     *                                   'price_without_tax' => 12.4,
     *                                   'package_list' => array(
     *                                       1,
     *                                       3,
     *                                   ),
     *                               ),
     *                           ),
     *                           is_best_grade => true, // Does this option have the biggest grade (quick shipping) for this shipping address
     *                           is_best_price => true, // Does this option have the lower price for this shipping address
     *                           unique_carrier => true, // Does this option use a unique carrier
     *                           total_price_with_tax => 12.5,
     *                           total_price_without_tax => 12.5,
     *                           position => 5, // Average of the carrier position
     *                       ),
     *                   ),
     *               );
     *               If there are no carriers available for an address, return an empty  array
     */
    public function getDeliveryOptionList(Country $default_country = null, $flush = false)
    {
        static $cache = array();
        if (isset($cache[$this->id]) && !$flush) {
            return $cache[$this->id];
        }

        $delivery_option_list = array();
        $carriers_price = array();
        $carrier_collection = array();
        $package_list = $this->getPackageList($flush);

        // Foreach addresses
        foreach ($package_list as $id_address => $packages) {
        // Initialize vars
        $delivery_option_list[$id_address] = array();
        $carriers_price[$id_address] = array();
        $common_carriers = null;
        $best_price_carriers = array();
        $best_grade_carriers = array();
        $carriers_instance = array();

        // Get country
        if ($id_address) {
            $address = new Address($id_address);
            $country = new Country($address->id_country);
        } else {
            $country = $default_country;
        }

        // Foreach packages, get the carriers with best price, best position and best grade
        foreach ($packages as $id_package => $package) {
            // No carriers available
            if (count($packages) == 1 && count($package['carrier_list']) == 1 && current($package['carrier_list']) == 0) {
                $cache[$this->id] = array();
                return $cache[$this->id];
            }

            $carriers_price[$id_address][$id_package] = array();

            // Get all common carriers for each packages to the same address
            if (is_null($common_carriers)) {
                $common_carriers = $package['carrier_list'];
            } else {
                $common_carriers = array_intersect($common_carriers, $package['carrier_list']);
            }

            $best_price = null;
            $best_price_carrier = null;
            $best_grade = null;
            $best_grade_carrier = null;

            // Foreach carriers of the package, calculate his price, check if it the best price, position and grade
            foreach ($package['carrier_list'] as $id_carrier) {
                if (!isset($carriers_instance[$id_carrier])) {
                    $carriers_instance[$id_carrier] = new Carrier($id_carrier);
                }

                $price_with_tax = $this->getPackageShippingCost((int)$id_carrier, true, $country, $package['product_list']);
                $price_without_tax = $this->getPackageShippingCost((int)$id_carrier, false, $country, $package['product_list']);
                if (is_null($best_price) || $price_with_tax < $best_price) {
                    $best_price = $price_with_tax;
                    $best_price_carrier = $id_carrier;
                }
                $carriers_price[$id_address][$id_package][$id_carrier] = array(
                        'without_tax' => $price_without_tax,
                        'with_tax' => $price_with_tax);

                $grade = $carriers_instance[$id_carrier]->grade;
                if (is_null($best_grade) || $grade > $best_grade) {
                    $best_grade = $grade;
                    $best_grade_carrier = $id_carrier;
                }
            }

            $best_price_carriers[$id_package] = $best_price_carrier;
            $best_grade_carriers[$id_package] = $best_grade_carrier;
        }

        // Reset $best_price_carrier, it's now an array
        $best_price_carrier = array();
        $key = '';

        // Get the delivery option with the lower price
        foreach ($best_price_carriers as $id_package => $id_carrier) {
            $key .= $id_carrier.',';
            if (!isset($best_price_carrier[$id_carrier])) {
                $best_price_carrier[$id_carrier] = array(
                        'price_with_tax' => 0,
                        'price_without_tax' => 0,
                        'package_list' => array(),
                        'product_list' => array(),
                );
            }
            $best_price_carrier[$id_carrier]['price_with_tax'] += $carriers_price[$id_address][$id_package][$id_carrier]['with_tax'];
            $best_price_carrier[$id_carrier]['price_without_tax'] += $carriers_price[$id_address][$id_package][$id_carrier]['without_tax'];
            $best_price_carrier[$id_carrier]['package_list'][] = $id_package;
            $best_price_carrier[$id_carrier]['product_list'] = array_merge($best_price_carrier[$id_carrier]['product_list'], $packages[$id_package]['product_list']);
            $best_price_carrier[$id_carrier]['instance'] = $carriers_instance[$id_carrier];
            $real_best_price = !isset($real_best_price) || $real_best_price > $carriers_price[$id_address][$id_package][$id_carrier]['with_tax'] ?
                    $carriers_price[$id_address][$id_package][$id_carrier]['with_tax'] : $real_best_price;
            $real_best_price_wt = !isset($real_best_price_wt) || $real_best_price_wt > $carriers_price[$id_address][$id_package][$id_carrier]['without_tax'] ?
                    $carriers_price[$id_address][$id_package][$id_carrier]['without_tax'] : $real_best_price_wt;
        }

        // Add the delivery option with best price as best price
        $delivery_option_list[$id_address][$key] = array(
                'carrier_list' => $best_price_carrier,
                'is_best_price' => true,
                'is_best_grade' => false,
                'unique_carrier' => (count($best_price_carrier) <= 1)
        );

        // Reset $best_grade_carrier, it's now an array
        $best_grade_carrier = array();
        $key = '';

        // Get the delivery option with the best grade
        foreach ($best_grade_carriers as $id_package => $id_carrier) {
            $key .= $id_carrier.',';
            if (!isset($best_grade_carrier[$id_carrier])) {
                $best_grade_carrier[$id_carrier] = array(
                        'price_with_tax' => 0,
                        'price_without_tax' => 0,
                        'package_list' => array(),
                        'product_list' => array(),
                );
            }
            $best_grade_carrier[$id_carrier]['price_with_tax'] += $carriers_price[$id_address][$id_package][$id_carrier]['with_tax'];
            $best_grade_carrier[$id_carrier]['price_without_tax'] += $carriers_price[$id_address][$id_package][$id_carrier]['without_tax'];
            $best_grade_carrier[$id_carrier]['package_list'][] = $id_package;
            $best_grade_carrier[$id_carrier]['product_list'] = array_merge($best_grade_carrier[$id_carrier]['product_list'], $packages[$id_package]['product_list']);
            $best_grade_carrier[$id_carrier]['instance'] = $carriers_instance[$id_carrier];
        }

        // Add the delivery option with best grade as best grade
        if (!isset($delivery_option_list[$id_address][$key])) {
            $delivery_option_list[$id_address][$key] = array(
                    'carrier_list' => $best_grade_carrier,
                    'is_best_price' => false,
                    'unique_carrier' => (count($best_grade_carrier) <= 1)
            );
        }
        $delivery_option_list[$id_address][$key]['is_best_grade'] = true;

        // Get all delivery options with a unique carrier
        foreach ($common_carriers as $id_carrier) {
            $key = '';
            $package_list = array();
            $product_list = array();
            $price_with_tax = 0;
            $price_without_tax = 0;

            foreach ($packages as $id_package => $package) {
                $key .= $id_carrier.',';
                $price_with_tax += $carriers_price[$id_address][$id_package][$id_carrier]['with_tax'];
                $price_without_tax += $carriers_price[$id_address][$id_package][$id_carrier]['without_tax'];
                $package_list[] = $id_package;
                $product_list = array_merge($product_list, $package['product_list']);
            }

            if (!isset($delivery_option_list[$id_address][$key])) {
                $delivery_option_list[$id_address][$key] = array(
                        'is_best_price' => false,
                        'is_best_grade' => false,
                        'unique_carrier' => true,
                        'carrier_list' => array(
                        $id_carrier => array(
                        'price_with_tax' => $price_with_tax,
                        'price_without_tax' => $price_without_tax,
                        'instance' => $carriers_instance[$id_carrier],
                        'package_list' => $package_list,
                        'product_list' => $product_list,
                )
                )
                );
            } else {
                $delivery_option_list[$id_address][$key]['unique_carrier'] = (count($delivery_option_list[$id_address][$key]['carrier_list']) <= 1);
            }
        }
    }

        $cart_rules = CartRule::getCustomerCartRules(Context::getContext()->cookie->id_lang, Context::getContext()->cookie->id_customer, true, true, false, $this, true);

        $result = false;
        if ($this->id) {
            $result = Db::getInstance()->executeS('SELECT * FROM '._DB_PREFIX_.'cart_cart_rule WHERE id_cart = '.(int)$this->id);
        }

        $cart_rules_in_cart = array();

        if (is_array($result)) {
            foreach ($result as $row) {
                $cart_rules_in_cart[] = $row['id_cart_rule'];
            }
        }

        $total_products_wt = $this->getOrderTotal(true, Cart::ONLY_PRODUCTS);
        $total_products = $this->getOrderTotal(false, Cart::ONLY_PRODUCTS);

        $free_carriers_rules = array();

        $context = Context::getContext();
        foreach ($cart_rules as $cart_rule) {
        $total_price = $cart_rule['minimum_amount_tax'] ? $total_products_wt : $total_products;
        $total_price += $cart_rule['minimum_amount_tax'] && $cart_rule['minimum_amount_shipping'] ? $real_best_price : 0;
        $total_price += !$cart_rule['minimum_amount_tax'] && $cart_rule['minimum_amount_shipping'] ? $real_best_price_wt : 0;
        if ($cart_rule['free_shipping'] && $cart_rule['carrier_restriction']
                && in_array($cart_rule['id_cart_rule'], $cart_rules_in_cart)
                && $cart_rule['minimum_amount'] <= $total_price) {
            $cr = new CartRule((int)$cart_rule['id_cart_rule']);
            if (Validate::isLoadedObject($cr) &&
                    $cr->checkValidity($context, in_array((int)$cart_rule['id_cart_rule'], $cart_rules_in_cart), false, false)) {
                $carriers = $cr->getAssociatedRestrictions('carrier', true, false);
                if (is_array($carriers) && count($carriers) && isset($carriers['selected'])) {
                    foreach ($carriers['selected'] as $carrier) {
                        if (isset($carrier['id_carrier']) && $carrier['id_carrier']) {
                            $free_carriers_rules[] = (int)$carrier['id_carrier'];
                        }
                    }
                }
            }
        }
    }

        // For each delivery options :
        //    - Set the carrier list
        //    - Calculate the price
        //    - Calculate the average position
        foreach ($delivery_option_list as $id_address => $delivery_option) {
        foreach ($delivery_option as $key => $value) {
            $total_price_with_tax = 0;
            $total_price_without_tax = 0;
            $position = 0;
            foreach ($value['carrier_list'] as $id_carrier => $data) {
                $total_price_with_tax += $data['price_with_tax'];
                $total_price_without_tax += $data['price_without_tax'];
                $total_price_without_tax_with_rules = (in_array($id_carrier, $free_carriers_rules)) ? 0 : $total_price_without_tax;

                if (!isset($carrier_collection[$id_carrier])) {
                    $carrier_collection[$id_carrier] = new Carrier($id_carrier);
                }
                $delivery_option_list[$id_address][$key]['carrier_list'][$id_carrier]['instance'] = $carrier_collection[$id_carrier];

                if (file_exists(_PS_SHIP_IMG_DIR_.$id_carrier.'.jpg')) {
                    $delivery_option_list[$id_address][$key]['carrier_list'][$id_carrier]['logo'] = _THEME_SHIP_DIR_.$id_carrier.'.jpg';
                } else {
                    $delivery_option_list[$id_address][$key]['carrier_list'][$id_carrier]['logo'] = false;
                }

                $position += $carrier_collection[$id_carrier]->position;
            }
            $delivery_option_list[$id_address][$key]['total_price_with_tax'] = $total_price_with_tax;
            $delivery_option_list[$id_address][$key]['total_price_without_tax'] = $total_price_without_tax;
            $delivery_option_list[$id_address][$key]['is_free'] = !$total_price_without_tax_with_rules ? true : false;
            $delivery_option_list[$id_address][$key]['position'] = $position / count($value['carrier_list']);
        }
    }

        // Sort delivery option list
        foreach ($delivery_option_list as &$array) {
        uasort($array, array('Cart', 'sortDeliveryOptionList'));
    }

        $cache[$this->id] = $delivery_option_list;
        return $cache[$this->id];
    }


    public static class JeproLabCartRuleModel extends JeproLabModel{
        public int cart_rule_id;

        public int language_id;

        public int customer_id;

        public Date date_from;

        public Date date_to;

        public int quantity = 1;

        public int quantity_per_customer = 1;

        public int priority = 1;

        public int partial_use = 1;

        public String code;

        public float minimum_amount;

        public float minimum_amount_tax;

        public int minimum_amount_currency_id;

        public float minimum_amount_shipping;

        public int reduction_currency_id;

        public float reduction_percent;
        public float reduction_amount;
        public int reduction_analyze_id;
        public boolean reduction_tax = false;
        public reduction_;

        public boolean lab_restriction;

        public boolean country_restriction;

        public boolean group_restriction;

        public boolean cart_rule_restriction;

        public boolean carrier_restriction;

        public int gift_analyze_id;

        public int gift_analyze_attribute_id;

        public boolean published;

        public Date date_add;

        public Date date_up;


        public Map<String, String> name = new HashMap<>();
        public Map<String, String> description = new HashMap<>();


        /* Filters used when retrieving the cart rules applied to a cart of when calculating the value of a reduction */
        public static final int FILTER_ACTION_ALL = 1;
        public static final int FILTER_ACTION_SHIPPING = 2;
        public static final int FILTER_ACTION_REDUCTION = 3;
        public static final int FILTER_ACTION_GIFT = 4;
        public static final int FILTER_ACTION_ALL_NOCAP = 5;

        public final String BO_ORDER_CODE_PREFIX = "BO_ORDER_";

        private static boolean IS_FEATURE_PUBLISHED = false;


        /**
         *
         */
        public boolean add(){
            if(this.reduction_currency_id <= 0){
                this.reduction_currency_id = JeproLabSettingModel.getIntValue("default_currency");
            }

            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            return true;
        }

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

        /* When an entity associated to a analyze rule (analyze, category, attribute, supplier, manufacturer...) is deleted, the product rules must be updated */
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

        public static boolean isFeaturePublished(){
            if(!JeproLabCartRuleModel.IS_FEATURE_PUBLISHED){
                IS_FEATURE_PUBLISHED = JeproLabSettingModel.getIntValue("cart_rule_feature_active") > 0;
            }
            return IS_FEATURE_PUBLISHED;
        }

        public static List autoRemoveFromCart(){
            return autoRemoveFromCart(null);
        }

        /**
         * Automatically remove this CartRule from the Cart
         *
         * @param context|null $context Context instance
         *
         * @return array Error messages
         */
        public static List autoRemoveFromCart(JeproLabContext context){
            if (context == null) {
                context = JeproLabContext.getContext();
            }
            if (!JeproLabCartRuleModel.isFeaturePublished() || !JeproLabTools.isLoadedObject(context.cart, "cart_id")) {
                return new ArrayList<>();
            }

            List errors = new ArrayList<>();
            String error;
            for(JeproLabCartRuleModel cartRule : context.cart.getCartRules()) {
                error = cartRule.checkValidity(context, true);
                if (!error.equals("")){
                    context.cart.removeCartRule(cartRule.cart_rule_id);
                    context.cart.update();
                    errors.add(error);
                }
            }
            return errors;
        }

        /**
         * Make sure caches are empty
         * Must be called before calling multiple time getContextualValue()
         */
        public static void cleanCache(){
            JeproLabCartRuleModel.only_one_gift = new ArrayList<>();
        }

        /**
         * Get CartRule combinations
         *
         *
         * @return array CartRule search results
         */
        protected List getCartRuleCombinations() {
            return getCartRuleCombinations(0, 0, "");
        }


        /**
         * Get CartRule combinations
         *
         * @param offset Offset
         *
         * @return array CartRule search results
         */

        protected List getCartRuleCombinations(int offset){
            return getCartRuleCombinations(offset, 0, "");
        }

        /**
         * Get CartRule combinations
         *
         * @param offset Offset
         * @param limit Limit
         *
         * @return array CartRule search results
         */
        protected List getCartRuleCombinations(int offset, int limit){
            return getCartRuleCombinations(offset, limit, "");
        }

        /**
         * Get CartRule combinations
         *
         * @param offset Offset
         * @param limit Limit
         * @param search Search query
         *
         * @return array CartRule search results
         */
        protected List getCartRuleCombinations(int offset, int limit, String search){
            List combinations = new ArrayList<>();
            String queryLimit = "";
            if (offset >= 0 && limit >= 0) {
                queryLimit = " LIMIT " + offset + ", " + (limit+1);
            } else {
                queryLimit = "";
            }

            //$array['selected'] = Db::getInstance()->executeS('
            String query = "SELECT cart_rule.*, cart_rule_lang.*, 1 as selected FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule");
            query += " AS cart_rule LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lang") + " AS cart_rule_lang ON (cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_lang." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " AND cart_rule_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + JeproLabContext.getContext().language.language_id;
            query += ") WHERE cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " != " + this.cart_rule_id ;
            query += (!search.equals("") ? " AND cart_rule_lang." + staticDataBaseObject.quoteName("name") + " LIKE '%" + staticDataBaseObject.quote(search) + "%'" : "");
            query += " AND (cart_rule." + staticDataBaseObject.quoteName("cart_rule_restriction") + " = 0 OR EXISTS ( SELECT 1 FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination") + " WHERE cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " = " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination") + "." + staticDataBaseObject.quoteName("cart_rule_id_1");
            query += " AND " + this.cart_rule_id + " = " + staticDataBaseObject.quoteName("cart_rule_id_2") + " ) OR EXISTS (SELECT 1 FROM ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination") + " WHERE cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " = " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination") + "." + staticDataBaseObject.quoteName("cart_rule_id_2") ;
            query += " AND " + this.cart_rule_id + " = " + staticDataBaseObject.quoteName("cart_rule_id_1") + ") ) ORDER BY cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + queryLimit;

            staticDataBaseObject.setQuery(query);
            
            ResultSet resultSet = staticDataBaseObject.loadObjectList();

            //$array['unselected'] = Db::getInstance()->executeS('
            query = "SELECT cart_rule.*, cart_rule_lang.*, 1 as selected FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule");
            query += " As cart_rule INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lang") + " AS cart_rule_lang ON (cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_lang." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " AND cart_rule_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + JeproLabContext.getContext().language.language_id;
            query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination") + " AS crc1 ON (cart_rule." ;
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = crc1." + staticDataBaseObject.quoteName("cart_rule_id_1") + " AND crc1.";
            query += staticDataBaseObject.quoteName("cart_rule_id_2") + " = " + this.cart_rule_id + ") LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination") + " AS crc2 ON (cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = crc2." + staticDataBaseObject.quoteName("cart_rule__id_2");
            query += " AND crc2." + staticDataBaseObject.quoteName("cart_rule_id_1") + " = " + this.cart_rule_id + ") WHERE cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_restriction") + " = 1 AND cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") ;
            query += " != " + this.cart_rule_id + (!search.equals("") ? " AND cart_rule_lang." + staticDataBaseObject.quoteName("name") + " LIKE '%" + staticDataBaseObject.quote(search) + "%'" : "");
            query += " AND crc1." + staticDataBaseObject.quoteName("cart_rule_id_1") + " IS NULL AND crc2." + staticDataBaseObject.quoteName("cart_rule_id_1");
            query += " IS NULL  ORDER BY cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + queryLimit;

            staticDataBaseObject.setQuery(query);
            resultSet = staticDataBaseObject.loadObjectList();
            return combinations;
        }

        public static void autoAddToCart(){
            autoAddToCart(null);
        }

        public static void autoAddToCart(JeproLabContext context){
            if(context == null){
                context = JeproLabContext.getContext();
            }

            if(JeproLabCartRuleModel.isFeaturePublished() && JeproLabTools.isLoadedObject(context.cart, "cart_id")){
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }

                String query = "SELECT SQL_NO_CACHE cart_rule.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule LEFT JOIN ";
                query += staticDataBaseObject.quoteName("__jeprolab_cart_rule_lab") + " AS cart_rule_lab ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
                query += " = cart_rule_lab." + staticDataBaseObject.quoteName("cart_rule_id") ;
                query += (context.customer.customer_id <= 0 && JeproLabGroupModel.isFeaturePublished() ? " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_group crg")
                    + " ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_group." + staticDataBaseObject.quoteName("cart_rule_id")  : "");
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_carrier") + " AS cart_rule_carrier ON cart_rule.";
                query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_carrier." + staticDataBaseObject.quoteName("cart_rule_id") ;
                query += (context.cart.carrier_id > 0 ? " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier ON (carrier."
                    + staticDataBaseObject.quoteName("reference_id") + " = cart_rule_carrier." + staticDataBaseObject.quoteName("carrier_id") + " AND carrier."
                    + staticDataBaseObject.quoteName("deleted") + " = 0)" : "") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_country");
                query += " AS cart_rule_country ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_country." + staticDataBaseObject.quoteName("cart_rule_id");
                query += " WHERE cart_rule." + staticDataBaseObject.quoteName("published") + " = 1 AND cart_rule." + staticDataBaseObject.quoteName("code");
                query += "\"\" AND cart_rule." + staticDataBaseObject.quoteName("quantity") + " > 0 AND cart_rule." + staticDataBaseObject.quoteName("date_from");
                query += " < " + staticDataBaseObject.quote(JeproLabTools.date() + " AND cart_rule." + staticDataBaseObject.quoteName("date_to") + "  > ";
                query += staticDataBaseObject.quote(JeproLabTools.date()) + " AND (cart_rule." + staticDataBaseObject.quoteName("customer_id") + " = 0 ";
                query += (context.customer.customer_id > 0 ? " OR cart_rule." + staticDataBaseObject.quoteName("customer_id") + " = " + context.cart.customer_id : "");
                query += ") AND ( cart_rule." + staticDataBaseObject.quoteName("carrier_restriction") + " = 0 ";
                query += (context.cart.carrier_id > 0 ? " OR cart." + staticDataBaseObject.quoteName("carrier_id") + " = " + context.cart.carrier_id : "");
                query += " ) AND (cart_rule." + staticDataBaseObject.quoteName("lab_restriction") + " = 0 ";
                query += ((JeproLabLaboratoryModel.isFeaturePublished() && context.laboratory.laboratory_id > 0) ? " OR cart_rule_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + context.laboratory.laboratory_id : "");
                query += ") AND ( cart_rule." + staticDataBaseObject.quoteName("group_restriction") + " = 0 ";
                if(context.customer.customer_id > 0) {
                    query += " OR EXISTS ( SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer_group") + " AS customer_group ";
                    query += " INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_group") + " AS cart_rule_group ON customer_group.";
                    query += staticDataBaseObject.quoteName("group_id") + " = cart_rule_group." + staticDataBaseObject.quoteName("group_id") + " WHERE cart_rule.";
                    query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_group." + staticDataBaseObject.quoteName("cart_rule_id") + " AND customer_group.";
                    query += staticDataBaseObject.quoteName("customer_id") + " = " + context.customer.customer_id + " LIMIT 1 )";
                }else {
                    query += (JeproLabGroupModel.isFeaturePublished() ? " OR cart_rule_group." + staticDataBaseObject.quoteName("group_id") + " = "
                            + JeproLabSettingModel.getIntValue("unidentified_group") : "");
                }
                query += ") AND ( cart_rule." + staticDataBaseObject.quoteName("reduction_analyze") + " <= 0 OR EXISTS ( SELECT 1 FROM ";
                query += staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                query += " = cart_rule." + staticDataBaseObject.quoteName("reduction_analyze") + " AND " + staticDataBaseObject.quoteName("cart_id") + " = ";
                query += context.cart.cart_id + ") ) AND NOT EXISTS (SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " WHERE cart_rule.";
                query += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_cart_rule." + staticDataBaseObject.quoteName("cart_cart_rule_id")  + " AND " ;
                query += staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + ") ORDER BY " + staticDataBaseObject.quoteName("priority");

                staticDataBaseObject.setQuery(query);

                ResultSet resultSet = staticDataBaseObject.loadObjectList();
                if (resultSet != null) {
                    //List<JeproLabCartRuleModel> cartRules = ObjectModel::hydrateCollection('CartRule', $result);
                    try {
                        JeproLabCartRuleModel cartRule;
                        while (resultSet.next()){
                            /** @var CartRule $cart_rule */
                            if (cartRule.checkValidity(context, false, false)) {
                                context.cart.addCartRule(cartRule.cart_rule_id);
                            }
                        }
                            
                        
                    }catch (SQLException ignored){
                        ignored.printStackTrace();
                    }finally {
                        try{
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception ignored){
                            ignored.printStackTrace();
                        }
                    }
                }
            }
        }

        public float getContextualValue(boolean useTax){
            return getContextualValue(useTax, null, 0, null, true);
        }

        public float getContextualValue(boolean useTax, JeproLabContext context){
            return getContextualValue(useTax, context, 0, null, true);
        }

        public float getContextualValue(boolean useTax, JeproLabContext context, int filter){
            return getContextualValue(useTax, context, filter, null, true);
        }

        public float getContextualValue(boolean useTax, JeproLabContext context, int filter, Map<String, List> pack){
            return getContextualValue(useTax, context, filter, pack, true);
        }

        /**
         * The reduction value is POSITIVE
         *
         * @param useTax   Apply taxes
         * @param context   Context instance
         * @param useCache Allow using cache to avoid multiple free gift using multishipping
         *
         * @return float|int|string
         */
        public float getContextualValue(boolean useTax, JeproLabContext context, int filter, Map<String, List> pack, boolean useCache){
            if(!JeproLabCartRuleModel.isFeaturePublished()) {
                return 0;
            }
            if (context == null){
                context = JeproLabContext.getContext();
            }
            if (filter <= 0) {
                filter = JeproLabCartRuleModel.FILTER_ACTION_ALL;
            }

            List<JeproLabAnalyzeModel> allAnalyzes = context.cart.getAnalyzes();
            List<JeproLabAnalyzeModel> packageAnalyzes = ((pack == null) ? allAnalyzes : pack.get("products"));

            List<Integer> allCartRulesIds = context.cart.getRequestCartRulesIds();

            float cartAmountTaxIncluded = context.cart.getRequestTotal(true, JeproLabCartModel.ONLY_ANALYZES);
            float cartAmountTaxExcluded = context.cart.getRequestTotal(false, JeproLabCartModel.ONLY_ANALYZES);

            float reductionValue = 0;

            String cacheKey = "jeprolab_cart_model_get_contextual_value_" + this.cart_rule_id + "_" + (useTax ?  1 : 0) + "_";
            cacheKey += context.cart.cart_id + "_" + filter;
            for(JeproLabAnalyzeModel analyze : packageAnalyzes) {
                cacheKey += "_" + analyze.analyze_id + "_" + analyze.analyze_attribute_id ;
                cacheKey += (analyze.in_stock) ? "_" + analyze.in_stock : "";
            }

            if (JeproLabCache.getInstance().isStored(cacheKey)) {
                return (float)JeproLabCache.getInstance().retrieve (cacheKey);
            }

            // Free shipping on selected carriers
            if (this.free_shipping && (filter == JeproLabCartRuleModel.FILTER_ACTION_ALL || filter == JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP || filter == JeproLabCartRuleModel.FILTER_ACTION_SHIPPING))) {
                if (!this.carrier_restriction) {
                    reductionValue += context.cart.getRequestTotal(useTax, JeproLabCartModel.ONLY_SHIPPING, (pack == null) ? null : pack.get("products"), (pack == null) ? null : pack.get("carrier_id");
                } else {
                    if(staticDataBaseObject == null){
                        staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query = "SELECT cart_rule_carrier." + staticDataBaseObject.quoteName("cart_rule_id") + ", carrier.";
                    query += staticDataBaseObject.quoteName("carrier_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_carrier");
                    query += " AS cart_rule_carrier INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier ";
                    query += " ON (carrier." + staticDataBaseObject.quoteName("reference_id") + "  = cart_rule_carrier.";
                    query += staticDataBaseObject.quoteName("carrier_id") + " AND cart." + staticDataBaseObject.quoteName("deleted");
                    query += " = 0) WHERE cart_rule_carrier." + staticDataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id;
                    query += " AND carrier." + staticDataBaseObject.quoteName("carrier_id") + " = " + context.cart.carrier_id;

                    staticDataBaseObject.setQuery(query);
                    ResultSet resultSet = staticDataBaseObject.loadObjectList();

                    if (resultSet != null) {
                        try {
                            while(resultSet.next()){
                                reductionValue += context.cart.getCarrierCost(resultSet.getInt("carrier_id"), useTax, context.country);
                            }
                        }catch(SQLException ignored){
                            ignored.printStackTrace();
                        }finally {
                            try {
                                JeproLabDataBaseConnector.getInstance().closeConnexion();
                            }catch(Exception ignored){
                                ignored.printStackTrace();
                            }
                        }
                    }
                }
            }

            float requestTotal;

            if (filter == JeproLabCartRuleModel.FILTER_ACTION_ALL || filter == JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP || filter == JeproLabCartRuleModel.FILTER_ACTION_REDUCTION) {
                // Discount (%) on the whole order
                if(this.reduction_percent > 0 && this.reduction_analyze_id == 0) {
                    // Do not give a reduction on free analyzes!
                    requestTotal = context.cart.getRequestTotal(useTax, JeproLabCartModel.ONLY_ANALYZES, packageAnalyzes);
                    for(JeproLabCartRuleModel cartRule : context.cart.getCartRules(JeproLabCartRuleModel.FILTER_ACTION_GIFT)) {
                        requestTotal -= JeproLabTools.roundPrice(
                                cartRule.getContextualValue(useTax, context, JeproLabCartRuleModel.FILTER_ACTION_GIFT, pack),
                                JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                    }

                    // Remove products that are on special
                    if (this.reduction_exclude_special) {
                        for(JeproLabAnalyzeModel analyze : packageAnalyzes) {
                            if ($product['reduction_applies']) {
                                if (useTax) {
                                    requestTotal -= JeproLabTools.roundPrice(analyze.analyze_price.total_with_tax, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                                } else {
                                    requestTotal -= JeproLabTools.roundPrice(analyze.analyze_price.total, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                                }
                            }
                        }
                    }

                    reductionValue += requestTotal * this.reduction_percent / 100;
                }

                // Discount (%) on a specific product
                if (this.reduction_percent > 0 && this.reduction_analyze_id > 0) {
                    for (JeproLabAnalyzeModel analyze : packageAnalyzes) {
                        if (analyze.analyze_id == this.reduction_analyze_id && ((this.reduction_exclude_special && !$product['reduction_applies']) || !this.reduction_exclude_special)) {
                            reductionValue += (useTax ? analyze.analyze_price.total_with_tax : analyze.analyze_price.total) * this.reduction_percent / 100;
                        }
                    }
                }

                // Discount (%) on the cheapest product
                if (this.reduction_percent > 0 && this.reduction_analyze_id == -1) {
                    float minPrice = 0;
                    String cheapestAnalyze = "";
                    float price;
                    for(JeproLabAnalyzeModel analyze : allAnalyzes){
                        price = analyze.analyze_price.price;
                        if (useTax) {
                            // since later on we won't be able to know the product the cart rule was applied to,
                            // use average cart VAT for price_wt
                            price *= (1 + context.cart.getAverageAnalyzesTaxRate());
                        }

                        if (price > 0 && (minPrice == 0 || minPrice > price) && ((this.reduction_exclude_special && !$product['reduction_applies']) || !this.reduction_exclude_special)) {
                            minPrice = price;
                            cheapestAnalyze = analyze.analyze_id + "_" + analyze.analyze_attribute_id;
                        }
                    }

                    // Check if the cheapest product is in the package
                    boolean isInPack = false;
                    for (JeproLabAnalyzeModel analyze : packageAnalyzes) {
                        if (cheapestAnalyze.equals(analyze.analyze_id + "_" + analyze.analyze_attribute_id)|| (analyze.analyze_attribute_id + "_0").equals(cheapestAnalyze)) {
                            isInPack = true;
                        }
                    }
                    if (isInPack) {
                        reductionValue += minPrice * this.reduction_percent / 100;
                    }
                }

                // Discount (%) on the selection of products
                if (this.reduction_percent > 0 && this.reduction_analyze_id == -2) {
                    float selectedAnalyzesReduction = 0;
                    float price = 0;
                    float taxRate = 0
                    Map<String, List<JeproLabAnalyzeModel>> selectedAnalyzes = this.checkAnalyzeRestrictions(context, true);
                    if (selectedAnalyzes.size() > 0) {
                        for (JeproLabAnalyzeModel analyze : packageAnalyzes) {
                            if ((selectedAnalyzes.get(analyze.analyze_id + "_" + analyze.analyze_attribute_id).size() > 0
                            || selectedAnalyzes.get(analyze.analyze_id + "_0").size() > 0)
                            && ((this.reduction_exclude_special && !analyze.reduction_applies) || !this.reduction_exclude_special)) {
                                price = analyze.analyze_price.price;
                                if (useTax) {
                                    analyze = JeproLabAnalyzeModel.getTaxesInformation(analyze, context);
                                    taxRate = analyze.tax_rate / 100;
                                    price *= (1 + taxRate);
                                }

                                selectedAnalyzesReduction += price * analyze.cart_quantity;
                            }
                        }
                    }
                    reductionValue += selectedAnalyzesReduction * this.reduction_percent / 100;
                }

                // Discount (¤)
                if (this.reduction_amount > 0) {
                    float proportion = 1;
                    if (pack != null && allAnalyzes.size() > 0) {
                        float totalAnalyzes = context.cart.getRequestTotal(useTax, JeproLabCartModel.ONLY_ANALYZES);
                        if (totalAnalyzes > 0) {
                            proportion = context.cart.getRequestTotal(useTax, JeproLabCartModel.ONLY_ANALYZES, $package['products']) / $total_products;
                        }
                    }

                    float reductionAmount = this.reduction_amount;
                    // If we need to convert the voucher value to the cart currency
                    if ((context.currency != null) && this.reduction_currency_id != context.currency.currency_id) {
                        JeproLabCurrencyModel voucherCurrency = new JeproLabCurrencyModel(this.reduction_currency_id);

                        // First we convert the voucher value to the default currency
                        if (reductionAmount == 0 || voucherCurrency.conversion_rate == 0) {
                            reductionAmount = 0;
                        } else {
                            reductionAmount /= voucherCurrency.conversion_rate;
                        }

                        // Then we convert the voucher value in the default currency into the cart currency
                        reductionAmount *= context.currency.conversion_rate;
                        reductionAmount = JeproLabTools.roundPrice(reductionAmount, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                    }

                    // If it has the same tax application that you need, then it's the right value, whatever the product!
                    if (this.reduction_tax == useTax) {
                        // The reduction cannot exceed the products total, except when we do not want it to be limited (for the partial use calculation)
                        if (filter != JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP) {
                            float cartAmount = context.cart.getRequestTotal(useTax, JeproLabCartModel.ONLY_ANALYZES);
                            reductionAmount = Math.min(reductionAmount, cartAmount);
                        }
                        reductionValue += proportion * reductionAmount;
                    } else {
                        if (this.reduction_analyze_id > 0) {
                            float analyzePriceTaxIncluded, analyzePriceTaxExcluded, analyzeVatAmount;
                            for (JeproLabAnalyzeModel analyze : context.cart.getAnalyzes()) {
                                if (analyze.analyze_id == this.reduction_analyze_id) {
                                    analyzePriceTaxIncluded = analyze.analyze_price.price_with_tax;
                                    analyzePriceTaxExcluded = analyze.analyze_price.price;
                                    analyzeVatAmount = analyzePriceTaxIncluded - analyzePriceTaxExcluded;

                                    if (analyzeVatAmount == 0 || analyzePriceTaxExcluded == 0) {
                                        analyzeVatAmount = 0;
                                    } else {
                                        analyzeVatAmount = analyzeVatAmount / analyzePriceTaxExcluded;
                                    }

                                    if (this.reduction_tax && !useTax) {
                                        reductionValue += proportion * reductionAmount / (1 + analyzeVatAmount);
                                    } else if (!this.reduction_tax && useTax) {
                                        reductionValue += proportion * reductionAmount * (1 + analyzeVatAmount);
                                    }
                                }
                            }
                        }
                        // Discount (¤) on the whole order
                        else if (this.reduction_analyze_id == 0) {
                            cartAmountTaxExcluded = 0;
                            cartAmountTaxIncluded = 0;
                            float cartAverageVatRate = context.cart.getAverageAnalyzesTaxRate(cartAmountTaxExcluded, cartAmountTaxIncluded);

                            // The reduction cannot exceed the products total, except when we do not want it to be limited (for the partial use calculation)
                            if (filter != JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP) {
                                reductionAmount = Math.min(reductionAmount, this.reduction_tax ? cartAmountTaxIncluded : cartAmountTaxExcluded);
                            }

                            if (this.reduction_tax && !useTax) {
                                reductionValue += proportion * reductionAmount / (1 + cartAverageVatRate);
                            } else if (!this.reduction_tax && useTax) {
                                reductionValue += proportion * reductionAmount * (1 + cartAverageVatRate);
                            }
                        }
                        /*
                         * Reduction on the cheapest or on the selection is not really meaningful and has been disabled in the backend
                         * Please keep this code, so it won't be considered as a bug
                         * elseif (this.reduction_product == -1)
                         * elseif (this.reduction_product == -2)
                        */
                    }

                    // Take care of the other cart rules values if the filter allow it
                    if (filter != JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP) {
                        // Cart values
                        JeproLabCartModel cart = JeproLabContext.getContext().cart;

                        if (!JeproLabTools.isLoadedObject(cart, "cart_id")) {
                            cart = new JeproLabCartModel();
                        }

                        float cartAverageVatRate = cart.getAverageAnalyzesTaxRate();
                        float currentCartAmount = useTax ? cartAmountTaxIncluded : cartAmountTaxExcluded;
                        float previousReductionAmount;
                        JeproLabCartRuleModel previousCartRule;

                        for(int currentCartRuleId : allCartRulesIds) {
                            if (currentCartRuleId == this.cart_rule_id) {
                                break;
                            }

                            previousCartRule = new JeproLabCartRuleModel(currentCartRuleId);
                            previousReductionAmount = previousCartRule.reduction_amount;

                            if (previousCartRule.reduction_tax && !useTax) {
                                previousReductionAmount = proportion * previousReductionAmount / (1 + cartAverageVatRate);
                            } else if (!previousCartRule.reduction_tax && useTax) {
                                previousReductionAmount = proportion * previousReductionAmount * (1 + cartAverageVatRate);
                            }

                            currentCartAmount = Math.max(currentCartAmount - previousReductionAmount, 0);
                        }

                        reductionValue = Math.min(reductionValue, currentCartAmount);
                    }
                }
            }

            // Free gift
            if ((int)this.gift_product && (filter == JeproLabCartRuleModel.FILTER_ACTION_ALL || filter == JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP || filter == JeproLabCartRuleModel.FILTER_ACTION_GIFT)){
                int addressId = (is_null($package) ? 0 : $package['id_address']);
                for (JeproLabAnalyzeModel analyze : packageAnalyzes) {
                    if ($product['id_product'] == this.gift_product && ($product['id_product_attribute'] == this.gift_product_attribute || !(int)this.gift_product_attribute)) {
                        // The free gift coupon must be applied to one product only (needed for multi-shipping which manage multiple product lists)
                        if (!isset(JeproLabCartRuleModel.only_one_gift[this.id.'-'.this.gift_product])
                        || JeproLabCartRuleModel.only_one_gift[this.cart_rule_id + "_" + this.gift_product] == $id_address
                                || JeproLabCartRuleModel.only_one_gift[this.cart_rule_id + "_" + this.gift_product] == 0
                                || addressId == 0
                                || !useCache) {
                            reductionValue += (useTax ? $product['price_wt'] : $product['price']);
                            if (useCache && (!isset(JeproLabCartRuleModel.only_one_gift[this.cart_rule_id + "_" + this.gift_product]) || JeproLabCartRuleModel.only_one_gift[this.cart_rule_id + "_" + this.gift_product] == 0)) {
                                JeproLabCartRuleModel::$only_one_gift[this.cart_rule_id + "_" + this.gift_product] = addressId;
                            }
                            break;
                        }
                    }
                }
            }

            JeproLabCache.getInstance().store(cacheKey, reductionValue);
            return reductionValue;
        }


        /**
         *
         * @param filterCode code to be searched
         * @param langId lang id
         * @return List of cart_rules
         */
        public static List<JeproLabCartRuleModel> getCartRuleByCode(String filterCode, int langId){
            return getCartRuleByCode(filterCode, langId, false);
        }

        /**
         *
         * @param filterCode code to be searched
         * @param langId lang id
         * @param extended
         * @return List of cart_rules
         */
        public static List<JeproLabCartRuleModel> getCartRuleByCode(String filterCode, int langId, boolean extended){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String queryBase = "SELECT cart_rule.*, cart_rule_lang.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule");
            queryBase += " AS cart_rule LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lang") + " AS cart_rule_lang ";
            queryBase += " ON (cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_lang." ;
            queryBase += staticDataBaseObject.quoteName("cart_rule_id") + " AND cart_rule_lang." + staticDataBaseObject.quoteName("lang_id");
            queryBase += " = " + langId + ") ";
            String query;
            if(extended) {
                query = "(" + queryBase + " WHERE " + staticDataBaseObject.quoteName("code") + " LIKe '%" + filterCode + "%'  ) UNION (";
                query += queryBase + " WHERE " + staticDataBaseObject.quoteName("name") + " LIKE '%" + filterCode + "%' )";
            }else {
                query = queryBase + " WHERE " + staticDataBaseObject.quoteName("code") + " LIKe '%" + filterCode  + "%' " ;
            }

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObjectList();
            List<JeproLabCartRuleModel> cartRules = new ArrayList<>();
            if(resultSet != null){
                JeproLabCartRuleModel cartRule;
                try{
                    while(resultSet.next()){
                        cartRule = new JeproLabCartRuleModel();
                        cartRule.cart_rule_id = resultSet.getInt("cart_rule_id");
                        cartRules.add(cartRule);
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
            return cartRules;
        }
    }
}