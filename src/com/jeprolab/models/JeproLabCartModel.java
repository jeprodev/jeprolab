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

    private static Map<String, Float> _total_shipping;

    protected int tax_calculation_method = JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED;

    protected static Map<Integer, Integer>_number_of_analyzes = new HashMap<>();

    protected static Map<Integer, Boolean> _is_virtual_cart = new HashMap<>();

    protected static Map<String, Boolean> _package_list = new HashMap<>();

    protected static Map<String, HashMap<String, String>> _attributes_lists = new HashMap<>();
    protected static Map<Integer, String> _delivery_option_list = new HashMap<>();
    protected static Map<Integer, JeproLabCarrierModel> _carriers = new HashMap<>();

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

        if(analyze.analyze_attribute_id > 0 &&  analyze.attribute_weight > 0){
            analyze.attribute_weight += customizationWeight;
            analyze.weight = analyze.attribute_weight;
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
            //Jepro
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

    public static String getTotalCart(int cartId){
        return getTotalCart(cartId, false, JeproLabCartModel.BOTH);
    }

    public static String getTotalCart(int cartId, boolean useTax){
        return getTotalCart(cartId, useTax, JeproLabCartModel.BOTH);
    }

    public static String getTotalCart(int cartId, boolean useTax, int type){
        JeproLabCartModel cart = new JeproLabCartModel(cartId);
        if (!JeproLabTools.isLoadedObject(cart, "cart_id")) {
            //die(Tools::displayError ());
        }

        boolean withTaxes = !useTax || cart.tax_calculation_method != JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED;
        return JeproLabTools.displayPrice(cart.getRequestTotal(withTaxes, type), JeproLabCurrencyModel.getCurrencyInstance(cart.currency_id), false);
    }

    public static String getOrderTotalUsingTaxCalculationMethod(int cartId){
        return JeproLabCartModel.getTotalCart(cartId, true);
    }

    public boolean deleteAnalyze(int analyzeId){
        return deleteAnalyze(analyzeId, 0, 0, 0);
    }

    public boolean deleteAnalyze(int analyzeId, int analyzeAttributeId){
        return deleteAnalyze(analyzeId, analyzeAttributeId, 0, 0);
    }

    public boolean deleteAnalyze(int analyzeId, int analyzeAttributeId, int customizationId){
        return deleteAnalyze(analyzeId, analyzeAttributeId, customizationId);
    }

    /**
     * Delete a product from the cart
     *
     * @param analyzeId
     * @param analyzeAttributeId Attribute ID if needed
     * @param customizationId Customization id
     * @return bool result
     */
    public boolean deleteAnalyze(int analyzeId, int analyzeAttributeId, int customizationId, int deliveryAddressId){
        if (JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)){
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)) {
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query;
        if(customizationId > 0) {
            query = "SELECT "  + staticDataBaseObject.quoteName("quantity") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
            query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND " + staticDataBaseObject.quoteName("cart_id");
            query += " = " + this.cart_id + " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;

            staticDataBaseObject.setQuery(query);
            int analyzeTotalQuantity = (int)staticDataBaseObject.loadValue("quantity");

            //$customization_quantity = (int)Db::getInstance()->getValue('
            query = "SELECT " + staticDataBaseObject.quoteName("quantity") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization");
            query += " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND "  + staticDataBaseObject.quoteName("analyze_id");
            query += " = " + analyzeId + " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId ;
            query += (deliveryAddressId > 0 ? " AND " + staticDataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId : "");

            staticDataBaseObject.setQuery(query);
            int customizationQuantity = (int)staticDataBaseObject.loadValue("quantity");

            if (!this.deleteCustomization(customizationId, analyzeId, analyzeAttributeId, deliveryAddressId)){
                return false;
            }

            // refresh cache of self::_products
            this._analyzes = this.getAnalyzes(true);
            return (customizationQuantity == analyzeTotalQuantity && this.deleteAnalyze(analyzeId, analyzeAttributeId, 0, deliveryAddressId));
        }

        /* Get customization quantity */
        //$result = Db::getInstance()->getRow('
        query = "SELECT SUM(" + staticDataBaseObject.quoteName("quantity") + ") AS quantity FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization");
        query += " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND " + staticDataBaseObject.quoteName("analyze_d") + " = ";
        query += analyzeId + " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;

        staticDataBaseObject.setQuery(query);
        int resultQuantity = (int)staticDataBaseObject.loadValue("quantity");
        if (resultQuantity <= 0) {
            return false;
        }

        /* If the product still possesses customization it does not have to be deleted */
        if (Db::getInstance()->NumRows() && resultQuantity > 0){
            //return Db::getInstance () -> execute('
            query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " SET " + staticDataBaseObject.quoteName("quantity");
            query += " = " + resultQuantity + " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND ";
            query += staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId ;
            query += (analyzeAttributeId > 0 ? " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }

        /* analyze deletion */
        //$result = Db::getInstance()->execute('
        query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE " + staticDataBaseObject.quoteName("analyze_id");
        query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
        query += " AND " + staticDataBaseObject.quoteName("cart_id") + " = "  + this.cart_id;
        query += (deliveryAddressId > 0 ? " AND " + staticDataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId : "");

        staticDataBaseObject.setQuery(query);
        if (staticDataBaseObject.query(false)) {
            boolean updated = this.updateCart();
            // refresh cache of self::_products
            this._analyzes = this.getAnalyzes(true);
            JeproLabCartRuleModel.autoRemoveFromCart();
            JeproLabCartRuleModel.autoAddToCart();

            return updated;
        }

        return false;
    }

    /**
     * Does the cart use multiple address
     * @return bool
     */
    public boolean isMultiDeliveryAddress(){
        if(staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT COUNT(DISTINCT " + staticDataBaseObject.quoteName("delivery_address_id") + ") AS address FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE " + staticDataBaseObject.quoteName("cart_id");
        query += " = " + this.cart_id;

        staticDataBaseObject.setQuery(query);

        return staticDataBaseObject.loadValue("addresses") > 1;
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

    protected boolean deleteCustomization(int customizationId, int analyzeId, int analyzeAttributeId){
        return deleteCustomization(customizationId, analyzeId, analyzeAttributeId, 0);
    }

    protected boolean deleteCustomization(int customizationId, int analyzeId, int analyzeAttributeId, int deliveryAddressId){
        boolean result = true;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " WHERE ";
        query += staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

        staticDataBaseObject.setQuery(query);
        ResultSet customizationSet = staticDataBaseObject.loadObjectList();

        if (customizationSet != null) {
            //$cust_data = Db::getInstance()->getRow(
            query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_customized_data") + " WHERE ";
            query += staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

            staticDataBaseObject.setQuery(query);
            ResultSet customDataSet = staticDataBaseObject.loadObjectList();

            // Delete customization picture if necessary
            if (isset($cust_data['type']) && $cust_data['type'] == 0) {
                $result &= (@unlink(_PS_UPLOAD_DIR_.$cust_data['value']) && @unlink(_PS_UPLOAD_DIR_.$cust_data['value'].'_small'));
            }

            //$result &= Db::getInstance()->execute(
            query  = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_customized_data") + " WHERE ";
            query += staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

            staticDataBaseObject.setQuery(query);
            result &= staticDataBaseObject.query(false);

            if (result) {
                //$result &= Db::getInstance()->execute(
                query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " SET " + staticDataBaseObject.quoteName("quantity");
                query += " = "  + staticDataBaseObject.quoteName("quantity") + " - " + customization['quantity'] + " WHERE ";
                query += staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND " + staticDataBaseObject.quoteName("analyze_id");
                query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND " + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                query += " AND " + staticDataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId;

                staticDataBaseObject.setQuery(query);
                result &= staticDataBaseObject.query(false);
            }

            if (!result) {
                return false;
            }

            query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " WHERE ";
            query += staticDataBaseObject.quoteName("customization_id") + " = " + customizationId;

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }

        return true;
    }

    public List<JeproLabCombinationModel.JeproLabCustomizationModel> getAnalyzeCustomizations(int analyzeId) {
        return getAnalyzeCustomizations(analyzeId, 0, false);
    }

    public List<JeproLabCombinationModel.JeproLabCustomizationModel> getAnalyzeCustomizations(int analyzeId, int type) {
        return getAnalyzeCustomizations(analyzeId, type, false);
    }

    /**
     * Return custom pictures in this cart for a specified product
     *
     * @param analyzeId
     * @param type only return customization of this type
     * @param notInCart only return customizations that are not in cart already
     * @return array result rows
     */
    public List<JeproLabCombinationModel.JeproLabCustomizationModel> getAnalyzeCustomizations(int analyzeId, int type, boolean notInCart){
        if (!JeproLabCombinationModel.JeproLabCustomizationModel.isFeaturePublished()) {
            return new ArrayList<>();
        }

        if(staticDataBaseObject == null){ staticDataBaseObject = JeproLabFactory.getDataBaseConnector(); }

        //$result = Db::getInstance()->executeS('
        String query = "SELECT customization." + staticDataBaseObject.quoteName("customization_id") + ", customized_data.";
        query += staticDataBaseObject.quoteName("index") + ", customized_data." + staticDataBaseObject.quoteName("value");
        query += ", customized_data." + staticDataBaseObject.quoteName("type") + ", customization." + staticDataBaseObject.quoteName("in_cart");
        query += ", customization." + staticDataBaseObject.quoteName("quantity") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization");
        query += " AS customization LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_customized_data") + " AS customized_data ON (customization.";
        query += staticDataBaseObject.quoteName("customization_id") + " = customized_data." + staticDataBaseObject.quoteName("customization_id");
        query += ") WHERE customization." + staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND customization.";
        query += staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId ;
        query += (type == JeproLabAnalyzeModel.CUSTOMIZE_FILE ? " AND " + staticDataBaseObject.quoteName("type") + " = " + JeproLabAnalyzeModel.CUSTOMIZE_FILE : "");
        query += (type == JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD ? " AND " + staticDataBaseObject.quoteName("type") + " = " + JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD : "");
        query += (notInCart ? " AND " + staticDataBaseObject.quoteName("in_cart") + " = 0 " : "");

        staticDataBaseObject.setQuery(query);
        ResultSet customizationSet = staticDataBaseObject.loadObjectList();

        List<JeproLabCombinationModel.JeproLabCustomizationModel> customizations = new ArrayList<>();
        if(customizationSet != null){
            try{
                JeproLabCombinationModel.JeproLabCustomizationModel customization;
                while(customizationSet.next()){
                    customization = new JeproLabCombinationModel.JeproLabCustomizationModel();
                    customization.customization_id = customizationSet.getInt("customization_id");
                    customizations.add(customization);
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
        return customizations;
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

        boolean useEcotax = JeproLabSettingModel.getIntValue("use_ecotax") > 0;
        int roundType = JeproLabSettingModel.getIntValue("round_type");
        int ecotaxTaxRulesGroupId = JeproLabSettingModel.getIntValue("ECOTAX_TAX_RULES_GROUP_ID");
        int computePrecision = JeproLabSettingModel.getIntValue("price_compute_precision");

        if(this.cart_id <= 0){ return 0; }

        List<Integer> typeArray = new ArrayList<>();
        typeArray.add(JeproLabCartModel.ONLY_ANALYZES);
        typeArray.add(JeproLabCartModel.ONLY_DISCOUNTS);
        typeArray.add(JeproLabCartModel.BOTH);
        typeArray.add(JeproLabCartModel.BOTH_WITHOUT_SHIPPING);
        typeArray.add(JeproLabCartModel.ONLY_SHIPPING);
        typeArray.add(JeproLabCartModel.ONLY_WRAPPING);
        typeArray.add(JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING);
        typeArray.add(JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING);

        // Define virtual context to prevent case where the cart is not the in the global context
        try {
            JeproLabContext virtualContext = JeproLabContext.getContext().cloneContext();
            virtualContext.cart = this;

            if (!typeArray.contains(type)) {
               return 0; // die(JeproLabTools.displayError ());
            }

            boolean withShipping = (type == JeproLabCartModel.BOTH || type == JeproLabCartModel.ONLY_SHIPPING);

            // if cart rules are not used
            if (type == JeproLabCartModel.ONLY_DISCOUNTS && !JeproLabCartRuleModel.isFeaturePublished()){
                return 0;
            }

            // no shipping cost if is a cart with only virtual analyzes
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
                float price = JeproLabAnalyzeModel.getStaticPrice(analyze.analyze_id, withTaxes, analyze.analyze_attribute_id,
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
                        analyzePrice = withTaxes ? taxCalculator.addTaxes(price) : price;
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

        if (!JeproLabCartModel._is_virtual_cart.containsKey(this.cart_id)) {
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
            JeproLabCartModel._is_virtual_cart.put(this.cart_id, isVirtual);
        }

        return JeproLabCartModel._is_virtual_cart.get(this.cart_id);
    }

    public float getTotalShippingCost(){
        return getTotalShippingCost(null, true, null);
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
    public float getTotalShippingCost(deliveryOtion, boolean useTax, JeproLabCountryModel defaultCountry){

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

    public float getPackageShippingCost(){
        return getPackageShippingCost(0, true, null, null, 0);
    }

    public float getPackageShippingCost(int carrierId){
        return getPackageShippingCost(carrierId, true, null, null, 0);
    }

    public float getPackageShippingCost(int carrierId , boolean useTax){
        return getPackageShippingCost(carrierId, useTax, null, null, 0);
    }

    public float getPackageShippingCost(int carrierId, boolean useTax, JeproLabCountryModel defaultCountry){
        return getPackageShippingCost(carrierId, useTax, defaultCountry, null, 0);
    }

    public float getPackageShippingCost(int carrierId, boolean useTax, JeproLabCountryModel defaultCountry, List analyzeList){
        return getPackageShippingCost(carrierId, useTax, defaultCountry, analyzeList, 0);
    }

    /**
     * Return package shipping cost
     *
     * @param carrierId      Carrier ID (default : current carrier)
     * @param useTax
     * @param defaultCountry
     * @param analyzeList    List of product concerned by the shipping.
     *                                      If null, all the product of the cart are used to calculate the shipping cost
     * @param zoneId
     *
     * @return float Shipping total
     */
    public float getPackageShippingCost(int carrierId, boolean useTax, JeproLabCountryModel defaultCountry, List<JeproLabAnalyzeModel> analyzeList, int zoneId){
        if (this.isVirtualCart()){
            return 0;
        }

        if(defaultCountry == null){
            defaultCountry = JeproLabContext.getContext().country;
        }

        if(analyzeList != null) {
            for(JeproLabAnalyzeModel analyze :analyzeList) {
                if (analyze.is_virtual) {
                    analyzeList.remove(analyze);
                }
            }
        }

        if (analyzeList == null) {
            analyzeList = this.getAnalyzes();
        }

        int addressId;
        if(JeproLabSettingModel.getStringValue("tax_address_type").equals("invoice_address_id")) {
            addressId = this.invoice_address_id;
        } else if (analyzeList.size() > 0) {
            addressId = analyzeList.get(0).delivery_address_id;
        } else {
            addressId = 0;
        }
        
        if (!JeproLabAddressModel.addressExists(addressId)) {
            addressId = 0;
        }

        if (carrierId == 0 && this.carrier_id > 0) {
            carrierId = this.carrier_id;
        }

        String cacheKey = "jeprolab_cart_model_get_package_shipping_cost_" + this.cart_id + "_" + addressId;
        cacheKey += "_" + carrierId + (useTax + "_" + defaultCountry.country_id;
        if(analyzeList != null){
            for(JeproLabAnalyzeModel analyze : analyzeList) {
                cacheKey += "_" + analyze.analyze_id  + "_" + analyze.analyze_attribute_id;
            }
        }

        if(JeproLabCache.getInstance().isStored(cacheKey)) {
            return (float)JeproLabCache.getInstance().retrieve(cacheKey);
        }

        // Order total in default currency without fees
        float requestTotal = this.getRequestTotal(true, JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING, analyzeList);

        // Start with shipping cost at 0
        float shippingCost = 0;
        // If no product added, return 0
        if (analyzeList.size() <= 0) {
            JeproLabCache.getInstance().store(cacheKey, shippingCost);
            return shippingCost;
        }

        if (zoneId <= 0) {
            // Get id zone
            if (!this.isMultiDeliveryAddress() && this.delivery_address_id > 0
                    && JeproLabCustomerModel.customerHasAddress(this.customer_id, this.delivery_address_id)) {
                zoneId = JeproLabAddressModel.getZoneIdByAddressId((int)this.delivery_address_id);
            } else {
                if (!JeproLabTools.isLoadedObject(defaultCountry, "country_id")) {
                    defaultCountry = new JeproLabCountryModel(JeproLabSettingModel.getIntValue("default_country"), JeproLabSettingModel.getIntValue("default_lang"));
                }

                zoneId = defaultCountry.zone_id;
            }
        }

        if (carrierId > 0 && !this.isCarrierInRange(carrierId, zoneId)) {
            carrierId = 0;
        }

        if (carrierId <= 0 && this.isCarrierInRange(JeproLabSettingModel.getIntValue("default_carrier"), zoneId)){
            carrierId = JeproLabSettingModel.getIntValue("default_carrier");
        }

        float totalPackageWithOutShippingTaxIncluded = this.getRequestTotal(true, JeproLabCartModel.BOTH_WITHOUT_SHIPPING, analyzeList);
        if (carrierId <= 0){
            List<JeproLabCarrierModel> carriers;
            if (this.customer_id > 0) {
                JeproLabCustomerModel customer = new JeproLabCustomerModel(this.customer_id);
                carriers = JeproLabCarrierModel.getCarriers(JeproLabSettingModel.getIntValue("default_lang"), true, false, zoneId, customer.getGroups());
                customer = null;
            } else {
                carriers = JeproLabCarrierModel.getCarriers(JeproLabSettingModel.getIntValue("default_lang"), true, false, zoneId);
            }
            int shippingMethod;

            for(JeproLabCarrierModel carrier : carriers) {
                if (carrier.carrier_id == JeproLabSettingModel.getIntValue("default_carrier")) {
                    continue;
                }

                if (!JeproLabCartModel._carriers.containsKey(carrier.carrier_id)){
                    JeproLabCartModel._carriers.put(carrier.carrier_id, new JeproLabCarrierModel(carrier.carrier_id));
                }

                /** @var Carrier $carrier */
                carrier = JeproLabCartModel._carriers.get(carrier.carrier_id);

                shippingMethod = carrier.getShippingMethod();
                // Get only carriers that are compliant with shipping method
                if ((shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT && !carrier.getMaxDeliveryPriceByWeight(zoneId))
                || (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE && !carrier.getMaxDeliveryPriceByPrice(zoneId))) {
                    unset($result[$k]);
                    continue;
                }

                // If out-of-range behavior carrier is set on "Desactivate carrier"
                float totalRequest = 0;
                if (carrier.range_behavior > 0) {
                    boolean checkDeliveryPriceByWeight = JeproLabCarrierModel.checkDeliveryPriceByWeight(carrier.carrier_id, this.getTotalWeight(), zoneId);

                    totalRequest = totalPackageWithOutShippingTaxIncluded;
                    boolean checkDeliveryPriceByPrice = JeproLabCarrierModel.checkDeliveryPriceByPrice(carrier.carrier_id, totalRequest, zoneId, this.currency_id);

                    // Get only carriers that have a range compatible with cart
                    if ((shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT && !checkDeliveryPriceByWeight)
                            || (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE && !checkDeliveryPriceByPrice)) {
                        unset($result[$k]);
                        continue;
                    }
                }

                float shipping;

                if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
                    shipping = carrier.getDeliveryPriceByWeight(this.getTotalWeight(analyzeList), zoneId);
                } else {
                    shipping = carrier.getDeliveryPriceByPrice(requestTotal, zoneId, this.currency_id);
                }

                if (!isset(minShippingPrice)) {
                    minShippingPrice = shipping;
                }

                if (shipping <= minShippingPrice) {
                    carrierId = carrier.carrier_id;
                    minShippingPrice = shipping;
                }
            }
        }

        if (carrierId <= 0 ){
            carrierId = JeproLabSettingModel.getIntValue("default_carrier");
        }

        if (!JeproLabCartModel._carriers.containsKey(carrierId)){
            JeproLabCartModel._carriers.put(carrierId, new JeproLabCarrierModel(carrierId, JeproLabSettingModel.getIntValue("default_lang")));
        }

        JeproLabCarrierModel carrier = JeproLabCartModel._carriers.get(carrierId);

        // No valid Carrier or carrierId <= 0 ?
        if (!JeproLabTools.isLoadedObject(carrier, "carrier_id")){
            JeproLabCache.getInstance().store(cacheKey, 0);
            return 0;
        }
        int shippingMethod = carrier.getShippingMethod();

        if (!carrier.published) {
            JeproLabCache.getInstance().store(cacheKey, shippingCost);
            return shippingCost;
        }

        // Free fees if free carrier
        if (carrier.is_free) {
            JeproLabCache.getInstance().store(cacheKey, 0);
            return 0;
        }

        // Select carrier tax
        float carrierTax = 0;
        if (useTax && !JeproLabTaxModel.excludeTaxOption()){
            JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);

            if (Configuration::get ('PS_ATCP_SHIPWRAP' )){
                // With PS_ATCP_SHIPWRAP, pre-tax price is deduced
                // from post tax price, so no $carrier_tax here
                // even though it sounds weird.
                carrierTax = carrier.getTaxesRate(address);
            }
        }

        //Map<String, Integer> configuration =
                //Configuration::getMultiple(array(
        float CONFIGURATION_SHIPPING_FREE_PRICE = JeproLabSettingModel.getFloatValue("shipping_free_price");
        float CONFIGURATION_SHIPPING_HANDLING = JeproLabSettingModel.getFloatValue("shipping_handling");
        int CONFIGURATION_SHIPPING_METHOD = JeproLabSettingModel.getIntValue("shipping_method");
        float CONFIGURATION_SHIPPING_FREE_WEIGHT = JeproLabSettingModel.getFloatValue("shipping_free_weight");
    //));

        // Free fees
        float freeFeesPrice = 0;
        if (CONFIGURATION_SHIPPING_FREE_PRICE > 0) {
            freeFeesPrice = JeproLabTools.convertPrice(CONFIGURATION_SHIPPING_FREE_PRICE, this.currency_id);
        }
        float orderTotalWithDiscounts = this.getRequestTotal(true, JeproLabCartModel.BOTH_WITHOUT_SHIPPING, null, 0, false);
        if (orderTotalWithDiscounts >= freeFeesPrice && freeFeesPrice > 0) {
            JeproLabCache.getInstance().store(cacheKey, shippingCost);
            return shippingCost;
        }

        if (CONFIGURATION_SHIPPING_FREE_WEIGHT > 0 && this.getTotalWeight() >= CONFIGURATION_SHIPPING_FREE_WEIGHT) {
            JeproLabCache.getInstance().store(cacheKey, shippingCost);
            return shippingCost;
        }

        // Get shipping cost using correct method
        if (carrier.range_behavior > 0) {
            if (zoneId <= 0) {
                // Get id zone
                if (this.delivery_address_id > 0 && JeproLabCustomerModel.customerHasAddress(this.customer_id, this.delivery_address_id)) {
                    zoneId = JeproLabAddressModel.getZoneIdByAddressId(this.delivery_address_id);
                } else {
                    zoneId = defaultCountry.zone_id;
                }
            }

            if ((shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT && !JeproLabCarrierModel.checkDeliveryPriceByWeight(carrier.carrier_id, this.getTotalWeight(), zoneId))
            || (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE && !JeproLabCarrierModel.checkDeliveryPriceByPrice(carrier.carrier_id, totalPackageWithOutShippingTaxIncluded, zoneId, this.currency_id)
            )) {
                shippingCost += 0;
            } else {
                if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
                    shippingCost += carrier.getDeliveryPriceByWeight(this.getTotalWeight(analyzeList), zoneId);
                } else { // by price
                    shippingCost += carrier.getDeliveryPriceByPrice(requestTotal, zoneId, (int)this.currency_id);
                }
            }
        } else {
            if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
                shippingCost += carrier.getDeliveryPriceByWeight(this.getTotalWeight(analyzeList), zoneId);
            } else {
                shippingCost += carrier.getDeliveryPriceByPrice(requestTotal, zoneId, this.currency_id);
            }
        }
        // Adding handling charges
        if (CONFIGURATION_SHIPPING_HANDLING > 0 && carrier.shipping_handling) {
            shippingCost += CONFIGURATION_SHIPPING_HANDLING;
        }

        // Additional Shipping Cost per analyze
        analyzeList.stream().filter(analyze -> !analyze.is_virtual).forEach(analyze -> {
            shippingCost += analyze.additional_shipping_cost * analyze.cart_quantity;
        });

        shippingCost = JeproLabTools.convertPrice(shippingCost, this.currency_id);

        /*/get external shipping cost from module
        if (carrier.shipping_external) {
            String moduleSame = carrier.external_module_name;

            /** @var CarrierModule $module * /
            $module = JeproLabModuleModel.getInstanceByName(moduleName);

            if (Validate::isLoadedObject($module)) {
                if (array_key_exists('id_carrier', $module)) {
                    $module->id_carrier = $carrier->id;
                }
                if ($carrier->need_range) {
                    if (method_exists($module, 'getPackageShippingCost')) {
                        shippingCost = $module->getPackageShippingCost($this, shippingCost, $products);
                    } else {
                        shippingCost = $module->getOrderShippingCost($this, shippingCost);
                    }
                } else {
                    shippingCost = $module->getOrderShippingCostExternal($this);
                }

                // Check if carrier is available
                if (shippingCost === false) {
                    JeproLabCache.getInstance().store(cacheKey, false);
                    return false;
                }
            } else {
                JeproLabCache.getInstance().store(cacheKey, false);
                return false;
            }
        }
* /
        if (Configuration::get('PS_ATCP_SHIPWRAP')){
            if (!useTax) {
                // With PS_ATCP_SHIPWRAP, we deduce the pre-tax price from the post-tax
                // price. This is on purpose and required in Germany.
                shippingCost /= (1 + this.getAverageAnalyzesTaxRate());
            }
        } else{
            // Apply tax
            if (useTax && carrierTax > 0) {
                shippingCost *= 1 + (carrierTax / 100);
            }
        }
*/
        shippingCost = JeproLabTools.roundPrice(shippingCost, 
                (JeproLabCurrencyModel.getCurrencyInstance(this.currency_id).decimals * JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION));
        JeproLabCache.getInstance().store(cacheKey, shippingCost);

        return shippingCost;
    }

    /**
     * isCarrierInRange
     *
     * Check if the specified carrier is in range
     *
     * @id_carrier int
     * @id_zone int
     */
    public boolean isCarrierInRange(int carrierId, int zoneId){
        JeproLabCarrierModel carrier = new JeproLabCarrierModel(carrierId, JeproLabSettingModel.getIntValue("default_lang"));
        int shippingMethod = carrier.getShippingMethod();
        if (carrier.range_behavior <= 0) {
            return true;
        }

        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_FREE) {
            return true;
        }

        boolean checkDeliveryPriceByWeight = JeproLabCarrierModel.checkDeliveryPriceByWeight(
            carrierId, this.getTotalWeight(), zoneId
        );

        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT && checkDeliveryPriceByWeight) {
            return true;
        }

        boolean checkDeliveryPriceByPrice = JeproLabCarrierModel.checkDeliveryPriceByPrice(
            carrierId,
            this.getRequestTotal(true, JeproLabCartModel.BOTH_WITHOUT_SHIPPING),
            zoneId, this.currency_id
        );

        if (shippingMethod == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE && checkDeliveryPriceByPrice) {
            return true;
        }

        return false;
    }


    public float getTotalWeight(){ return getTotalWeight(null); }

    /**
     * Return total Cart weight
     *
     * @return float Total Cart weight
     */
    public float getTotalWeight(List<JeproLabAnalyzeModel> analyzes){
        if(analyzes != null) {
            float totalWeight = 0;
            for(JeproLabAnalyzeModel analyze : analyzes) {
                if (analyze.attribute_weight <= 0){
                    totalWeight += analyze.weight * analyze.cart_quantity;
                } else {
                    totalWeight += analyze.attribute_weight * analyze.cart_quantity;
                }
            }
            return totalWeight;
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)) {
            this.updateAnalyzeWeight(this.cart_id);
        }

        return JeproLabCartModel._total_weight.get(this.cart_id);
    }

    /**
     * @param cartId
     */
    protected void updateAnalyzeWeight(int cartId){
        String query;
        float weighAnalyzeWithAttribute = 0;
        if (JeproLabCombinationModel.isFeaturePublished()) {
            //$weight_product_with_attribute = Db::getInstance()->getValue('
            query = "SELECT SUM((analyze." + staticDataBaseObject.quoteName("weight") + " + analyze_attribute." + staticDataBaseObject.quoteName("weight");
            query += ") * cart_analyze." + staticDataBaseObject.quoteName("quantity") + ") AS nb FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
            query += " AS cart_analyze LEFT JOIN " + staticDataBaseObject.quoteName("# _jeprolab_analyze") + " AS analyze ON (cart_analyze.";
            query += staticDataBaseObject.quoteName("analyze_id") + " = analyze." + staticDataBaseObject.quoteName("analyze_id") + ") LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute ON (cart_analyze.";
            query += staticDataBaseObject.quoteName("analyze_attribute_id") + " = analyze_attribute." + staticDataBaseObject.quoteName("analyze_attribute_id");
            query += ") WHERE (cart_analyze." + staticDataBaseObject.quoteName("analyze_attribute_id") + " IS NOT NULL AND cart_analyze.";
            query += staticDataBaseObject.quoteName("analyze_attribute_id") + " != 0) AND cart_analyze." + staticDataBaseObject.quoteName("cart_id") + " = " + cartId;

            staticDataBaseObject.setQuery(query);
            weighAnalyzeWithAttribute = (float)staticDataBaseObject.loadValue("nb");
        }

        // = Db::getInstance()->getValue('
        query = "SELECT SUM(analyze."  + staticDataBaseObject.quoteName("weight") + " * cart_quantity." + staticDataBaseObject.quoteName("quantity") ;
        query += " AS nb FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS cart_analyze LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ON (cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
        query += " = analyze." + staticDataBaseObject.quoteName("analyze_id") + ") WHERE (cart_analyze." + staticDataBaseObject.quoteName("analyze_attribute_id");
        query += " IS NULL OR cart_analyze." + staticDataBaseObject.quoteName("analyze_attribute_id") + " = 0) AND cart_analyze.";
        query += staticDataBaseObject.quoteName("cart_id") + " = " + cartId;

        staticDataBaseObject.setQuery(query);
        float weighAnalyzeWithoutAttribute = (float)staticDataBaseObject.loadValue("nb");

        //$weight_cart_customizations = Db::getInstance()->getValue('
        query = "SELECT SUM(customized_data." + staticDataBaseObject.quoteName("weight") + " * customization." + staticDataBaseObject.quoteName("quantity");
        query += ") AS nb FROM " + staticDataBaseObject.quoteName("#__jeprolab_customization") + " AS customization  LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_customized_data") + " AS customized_data ON (customization.";
        query += staticDataBaseObject.quoteName("customization_id") + " = customized_data." + staticDataBaseObject.quoteName("customization_id") ;
        query += ") WHERE customization." + staticDataBaseObject.quoteName("in_cart") + " = 1 AND customization.";
        query += staticDataBaseObject.quoteName("cart_id") + " = " + cartId;

        staticDataBaseObject.setQuery(query);
        float weighCartCustomizations = (float)staticDataBaseObject.loadValue("nb");
        JeproLabCartModel._total_weight.put(cartId, (weighAnalyzeWithAttribute + weighAnalyzeWithoutAttribute + weighCartCustomizations));
    }


    public function getDeliveryOption() {
        return getDeliveryOption(null, false, true);
    }

    public function getDeliveryOption(JeproLabCountryModel defaultCountry) {
        return getDeliveryOption(defaultCountry, false, true);
    }

    public function getDeliveryOption(JeproLabCountryModel defaultCountry, boolean doNotAutoSelectOptions) {
        return getDeliveryOption(defaultCountry, doNotAutoSelectOptions, true);
    }

    /**
     * Get the delivery option selected, or if no delivery option was selected,
     * the cheapest option for each address
     *
     * @param defaultCountry       Default country
     * @param doNotAutoSelectOptions Do not auto select delivery option
     * @param useCache             Use cache
     *
     * @return array|bool|mixed Delivery option
     */
    public function getDeliveryOption(JeproLabCountryModel defaultCountry, boolean doNotAutoSelectOptions, boolean useCache) {
        static $cache=array();
        String cacheKey = ((defaultCountry != null) ? defaultCountry.country_id : 0) + "_"  + (doNotAutoSelectOptions ? 1 : 0);
        if (JeproLabCache.getInstance().isStored(cacheKey) && useCache) {
            return JeproLabCache.getInstance().retrieve(cacheKey);
        }

        List deliveryOptionList = this.getDeliveryOptionList(defaultCountry);

        // The delivery option was selected
        if ((this.delivery_option != null) && $this -> delivery_option != '') {
            deliveryOption = JeproLabTools.unSerialize(this.delivery_option);
            boolean validated = true;
            for($delivery_option as $id_address = > $key){
                if (!isset($delivery_option_list[$id_address][$key])) {
                    validated = false;
                    break;
                }
            }

            if (validated) {
                $cache[cacheKey] = $delivery_option;
                return deliveryOption;
            }
        }

        if (doNotAutoSelectOptions) {
            return false;
        }

        // No delivery option selected or delivery option selected is not valid, get the better for all options
        $delivery_option = array();
        for($delivery_option_list as $id_address = > $options){
            foreach($options as $key = > $option){
                if(JeproLabSettingModel.getIntValue("default_carrier") == -1 && $option['is_best_price']){
                    $delivery_option[$id_address] = $key;
                    break;
                }else if(JeproLabSettingModel.getIntValue("default_carrier") == -2 && $option['is_best_grade']){
                    $delivery_option[$id_address] = $key;
                    break;
                }else if($option['unique_carrier'] && in_array(Configuration::get('PS_CARRIER_DEFAULT'), array_keys($option['carrier_list']))){
                    $delivery_option[$id_address] = $key;
                    break;
                }
            }

            reset($options);
            if (!isset($delivery_option[$id_address])) {
                $delivery_option[$id_address] = key($options);
            }
        }

        $cache[cacheKey] = $delivery_option;

        return deliveryOption;
    }

    public List getDeliveryOptionList(){
        return getDeliveryOptionList(null, false);
    }

    public List getDeliveryOptionList(JeproLabCountryModel defaultCountry){
        return getDeliveryOptionList(defaultCountry, false);
    }


    /**
     * Get all deliveries options available for the current cart
     * @param defaultCountry
     * @param flush Force flushing cache
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
    public List getDeliveryOptionList(JeproLabCountryModel defaultCountry, boolean flush){
        if (JeproLabCartModel._delivery_option_list.containsKey(this.cart_id) && !flush) {
            return JeproLabCartModel._delivery_option_list.get(this.cart_id);
        }

        $deliveryOptionList = array();
        $carriersPrice = array();
        $carrier_collection = array();
        packageList = this.getPackageList(flush);

        // Foreach addresses
        for($packageList as $id_address => $packages){
            // Initialize vars
            $delivery_option_list[$id_address] = array();
            $carriers_price[$id_address] = array();
            $common_carriers = null;
            $best_price_carriers = array();
            $best_grade_carriers = array();
            $carriers_instance = array();

            // Get country
            JeproLabCountryModel country;
            if (addressId > 0) {
                JeproLabAddressModel address = new JeproLabAddressModel(addressId);
                country = new JeproLabCountryModel(address.country_id);
            } else {
                country = defaultCountry;
            }

            // Foreach packages, get the carriers with best price, best position and best grade
            foreach($packages as $id_package = > $package){
                // No carriers available
                if (count($packages) == 1 && count($package['carrier_list']) == 1 && current($package['carrier_list']) == 0) {
                    $cache[$this -> id] = array();
                    return $cache[$this -> id];
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
                foreach($package['carrier_list']as carrierId) {
                    if (!isset($carriers_instance[carrierId])) {
                        $carriers_instance[carrierId] = new Carrier(carrierId);
                    }

                    priceWithTax = this.getPackageShippingCost(carrierId, true, country, $package['product_list']);
                    priceWithoutTax = this.getPackageShippingCost(carrierId, false, country, $package['product_list']);
                    if (is_null($best_price) || $price_with_tax < $best_price) {
                        $best_price = $price_with_tax;
                        $best_price_carrier = carrierId;
                    }
                    $carriers_price[$id_address][$id_package][carrierId] = array(
                            'without_tax' = > $price_without_tax,
                            'with_tax' =>$price_with_tax);

                    $grade = $carriers_instance[carrierId]->grade;
                    if (is_null($best_grade) || $grade > $best_grade) {
                        $best_grade = $grade;
                        $best_grade_carrier = carrierId;
                    }
                }

                $best_price_carriers[$id_package] = $best_price_carrier;
                $best_grade_carriers[$id_package] = $best_grade_carrier;
            }

            // Reset $best_price_carrier, it's now an array
            $best_price_carrier = array();
            $key = '';

            // Get the delivery option with the lower price
            foreach($best_price_carriers as $id_package = > carrierId){
                $key. = carrierId. ',';
                if (!isset($best_price_carrier[carrierId])) {
                    $best_price_carrier[carrierId] = array(
                            'price_with_tax' = > 0,
                            'price_without_tax' =>0,
                            'package_list' =>array(),
                            'product_list' =>array(),
                    );
                }
                $best_price_carrier[carrierId]['price_with_tax'] += $carriers_price[$id_address][$id_package][carrierId]['with_tax'];
                $best_price_carrier[carrierId]['price_without_tax'] += $carriers_price[$id_address][$id_package][carrierId]['without_tax'];
                $best_price_carrier[carrierId]['package_list'][]=$id_package;
                $best_price_carrier[carrierId]['product_list'] = array_merge($best_price_carrier[carrierId]['product_list'], $packages[$id_package]['product_list']);
                $best_price_carrier[carrierId]['instance'] = $carriers_instance[carrierId];
                $real_best_price = !isset($real_best_price) || $real_best_price > $carriers_price[$id_address][$id_package][carrierId]['with_tax'] ?
                        $carriers_price[$id_address][$id_package][carrierId]['with_tax'] : $real_best_price;
                $real_best_price_wt = !isset($real_best_price_wt) || $real_best_price_wt > $carriers_price[$id_address][$id_package][carrierId]['without_tax'] ?
                        $carriers_price[$id_address][$id_package][carrierId]['without_tax'] : $real_best_price_wt;
            }

            // Add the delivery option with best price as best price
            $delivery_option_list[$id_address][$key] = array(
                    'carrier_list' = > $best_price_carrier,
                    'is_best_price' =>true,
                    'is_best_grade' =>false,
                    'unique_carrier' =>(count($best_price_carrier) <= 1)
            );

            // Reset $best_grade_carrier, it's now an array
            $best_grade_carrier = array();
            $key = '';

            // Get the delivery option with the best grade
            foreach($best_grade_carriers as $id_package = > carrierId){
                $key. = carrierId. ',';
                if (!isset($best_grade_carrier[carrierId])) {
                    $best_grade_carrier[carrierId] = array(
                            'price_with_tax' = > 0,
                            'price_without_tax' =>0,
                            'package_list' =>array(),
                            'product_list' =>array(),
                    );
                }
                $best_grade_carrier[carrierId]['price_with_tax'] += $carriers_price[$id_address][$id_package][carrierId]['with_tax'];
                $best_grade_carrier[carrierId]['price_without_tax'] += $carriers_price[$id_address][$id_package][carrierId]['without_tax'];
                $best_grade_carrier[carrierId]['package_list'][]=$id_package;
                $best_grade_carrier[carrierId]['product_list'] = array_merge($best_grade_carrier[carrierId]['product_list'], $packages[$id_package]['product_list']);
                $best_grade_carrier[carrierId]['instance'] = $carriers_instance[carrierId];
            }

            // Add the delivery option with best grade as best grade
            if (!isset($delivery_option_list[$id_address][$key])) {
                $delivery_option_list[$id_address][$key] = array(
                        'carrier_list' = > $best_grade_carrier,
                        'is_best_price' =>false,
                        'unique_carrier' =>(count($best_grade_carrier) <= 1)
                );
            }
            $delivery_option_list[$id_address][$key]['is_best_grade'] = true;

            // Get all delivery options with a unique carrier
            foreach($common_carriers as carrierId) {
                $key = '';
                $package_list = array();
                analyzeList = array();
                $price_with_tax = 0;
                $price_without_tax = 0;

                foreach($packages as $id_package = > $package){
                    $key. = carrierId. ',';
                    $price_with_tax += $carriers_price[$id_address][$id_package][carrierId]['with_tax'];
                    $price_without_tax += $carriers_price[$id_address][$id_package][carrierId]['without_tax'];
                    $package_list[]=$id_package;
                    analyzeList = array_merge(analyzeList, $package['product_list']);
                }

                if (!isset($delivery_option_list[$id_address][$key])) {
                    $delivery_option_list[$id_address][$key] = array(
                            'is_best_price' = > false,
                            'is_best_grade' =>false,
                            'unique_carrier' =>true,
                            'carrier_list' =>array(
                            carrierId = > array(
                            'price_with_tax' = > $price_with_tax,
                            'price_without_tax' =>$price_without_tax,
                            'instance' =>$carriers_instance[carrierId],
                            'package_list' =>$package_list,
                            'product_list' =>analyzeList,
                    )
                    )
                    );
                } else {
                    $delivery_option_list[$id_address][$key]['unique_carrier'] = (count($delivery_option_list[$id_address][$key]['carrier_list']) <= 1);
                }
            }
        }

        List<JeproLabCartRuleModel> cartRules = JeproLabCartRuleModel.getCustomerCartRules(
                JeproLabContext.getContext().cookie.language_id,
                JeproLabContext.getContext().cookie.customer_id, true, true, false, this, true);

        boolean result = false;
        if (this.cart_id > 0) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " WHERE ";
            query += staticDataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

            staticDataBaseObject.setQuery(query);
            staticDataBaseObject.loadObjectList();
            ResultSet resultSet = staticDataBaseObject.loadObjectList();

            //List<Map<>>

            if(resultSet != null){
                try{
                    while(resultSet.next()){

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
        }

        $cart_rules_in_cart = array();

        if (is_array($result)) {
            foreach ($result as $row) {
                $cart_rules_in_cart[] = $row['id_cart_rule'];
            }
        }

        float totalAnalyzesWithTaxes = this.getRequestTotal(true, JeproLabCartModel.ONLY_ANALYZES);
        float totalAnalyzes = this.getRequestTotal(false, JeproLabCartModel.ONLY_ANALYZES);

        $free_carriers_rules = array();

        JeproLabContext context = JeproLabContext.getContext();
        float totalPrice;
        for(JeproLabCartRuleModel cartRule : cartRules) {
            totalPrice = cartRule.minimum_amount_tax > 0 ? totalAnalyzesWithTaxes : totalAnalyzes;
            totalPrice += cartRule.minimum_amount_tax > 0 && cartRule.minimum_amount_shipping > 0 ? realBestPrice : 0;
            totalPrice += cartRule.minimum_amount_tax <= 0 && cartRule.minimum_amount_shipping > 0 ? realBestPriceWithTaxes : 0;
            if (cartRule.free_shipping && cartRule.carrier_restriction
                    && in_array($cart_rule['id_cart_rule'], $cart_rules_in_cart)
                    && cartRule.minimum_amount <= totalPrice) {
                JeproLabCartRuleModel cr = new JeproLabCartRuleModel(cartRule.cart_rule_id);
                if (JeproLabTools.isLoadedObject(cr, "cart_rule_id") &&
                        cr.checkValidity(context, in_array((int) $cart_rule['id_cart_rule'], $cart_rules_in_cart), false, false)){
                    carriers = cr.getAssociatedRestrictions('carrier', true, false);
                    if (is_array($carriers) && count($carriers) && isset($carriers['selected'])) {
                        for(JeproLabCarrierModel carrier : $carriers['selected']) {
                            if (carrier.carrier_id) {
                                $free_carriers_rules[]=(int) carrier.carrier_id;
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
        for($delivery_option_list as $id_address => $delivery_option){
            foreach($delivery_option as $key = > $value){
                $total_price_with_tax = 0;
                $total_price_without_tax = 0;
                $position = 0;
                foreach($value['carrier_list']as carrierId = > $data){
                    $total_price_with_tax += $data['price_with_tax'];
                    $total_price_without_tax += $data['price_without_tax'];
                    $total_price_without_tax_with_rules = (in_array(carrierId, $free_carriers_rules)) ? 0 : $total_price_without_tax;

                    if (!isset($carrier_collection[carrierId])) {
                        $carrier_collection[carrierId] = new Carrier(carrierId);
                    }
                    $delivery_option_list[$id_address][$key]['carrier_list'][carrierId]['instance'] = $carrier_collection[carrierId];

                    if (file_exists(_PS_SHIP_IMG_DIR_.carrierId. '.jpg')){
                        $delivery_option_list[$id_address][$key]['carrier_list'][carrierId]['logo'] = _THEME_SHIP_DIR_.carrierId.
                        '.jpg';
                    }else{
                        $delivery_option_list[$id_address][$key]['carrier_list'][carrierId]['logo'] = false;
                    }

                    $position += $carrier_collection[carrierId]->position;
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

        JeproLabCartModel._delivery_option_list.put(this.cart_id, deliveryOptionList);
        return JeproLabCartModel._delivery_option_list.get(this.cart_id);
    }

    public function getPackageList(){ return getPackageList(false); }

    /**
     * Get products grouped by package and by addresses to be sent individualy (one package = one shipping cost).
     *
     * @return array array(
     *                   0 => array( // First address
     *                       0 => array(  // First package
     *                           'product_list' => array(...),
     *                           'carrier_list' => array(...),
     *                           'id_warehouse' => array(...),
     *                       ),
     *                   ),
     *               );
     * @todo Add avaibility check
     */
    public function getPackageList(boolean flush){
        String cacheKey = this.cart_id + "_" + this.delivery_address_id;
        if(JeproLabCartModel._package_list.containsKey(cacheKey) && JeproLabCartModel._package_list.get(cacheKey) != null && !flush) {
            return JeproLabCartModel._package_list.get(cacheKey);
        }

        List<JeproLabAnalyzeModel> analyzeList = this.getAnalyzes(flush);
        // Step 1 : Get analyze information (warehouse_list and carrier_list), count warehouse
        // Determine the best warehouse to determine the packages
        // For that we count the number of time we can use a warehouse for a specific delivery address
        Map<Integer, Map<Integer, Integer>> warehouseCountByAddress = new HashMap<>();

        boolean stockManagementActive = JeproLabSettingModel.getIntValue("advanced_stock_management") > 0;

        for(JeproLabAnalyzeModel analyze : analyzeList) {
            if (analyze.delivery_address_id == 0) {
                analyze.delivery_address_id = this.delivery_address_id;
            }

            if (!warehouseCountByAddress.containsKey(analyze.delivery_address_id)) {
                warehouseCountByAddress.put(analyze.delivery_address_id, new HashMap<Integer, Integer>());
            }

            $product['warehouse_list'] = array();
            List<JeproLabWarehouseModel> warehouseList;
            if(stockManagementActive && analyze.advanced_stock_management) {
                warehouseList = JeproLabWarehouseModel.getAnalyzeWarehouseList(analyze.analyze_id, analyze.analyze_attribute_id, this.laboratory_id);
                if (warehouseList.size() == 0) {
                    warehouseList = JeproLabWarehouseModel.getAnalyzeWarehouseList(analyze.analyze_id, analyze.analyze_attribute_id);
                }
                // Does the product is in stock ?
                // If yes, get only warehouse where the product is in stock

                warehouseInStock = array();
                JeproLabStockModel.JeproLabStockManager manager = JeproLabStockModel.JeproLabStockManagerFactory.getManager();
                int analyzeRealQuantities;
                for($warehouseList as $key = > $warehouse){
                    analyzeRealQuantities = manager.getAnalyzeRealQuantities(
                            analyze.analyze_id,
                            analyze.analyze_attribute_id,
                            array($warehouse['id_warehouse']),
                            true
                    );

                    if (analyzeRealQuantities > 0 || JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack (analyze.analyze_id)){
                        $warehouse_in_stock[]=$warehouse;
                    }
                }

                if (!empty($warehouse_in_stock)) {
                    $warehouse_list = $warehouse_in_stock;
                    analyze.in_stock = true;
                } else {
                    analyze.in_stock = false;
                }
            } else {
                //simulate default warehouse
                warehouseList = array(0 = > array('id_warehouse' = > 0));
                analyze.in_stock = JeproLabStockModel.JeproLabStockAvailableModel.getQuantityAvailableByAnalyze(analyze.analyze_id, analyze.analyze_attribute_id) > 0;
            }

            for(JeproLabWarehouseModel warehouse : warehouseList) {
                $product['warehouse_list'][$warehouse['id_warehouse']] = $warehouse['id_warehouse'];
                if (!warehouseCountByAddress.get(analyze.delivery_address_id).containsKey(warehouse.warehouse_id)){
                    warehouseCountByAddress.get(analyze.delivery_address_id).put(warehouse.warehouse_id, 0);
                }

                warehouseCountByAddress.get(analyze.delivery_address_id).put(
                        warehouse.warehouse_id, (warehouseCountByAddress.get(analyze.delivery_address_id).get(warehouse.warehouse_id)++)
                );
            }
        }
        unset($product);

        arsort($warehouse_count_by_address);

        // Step 2 : Group product by warehouse
        $grouped_by_warehouse = array();

        for(analyzeList as &$product) {
            if (!isset($grouped_by_warehouse[$product['id_address_delivery']])) {
                $grouped_by_warehouse[$product['id_address_delivery']] = array(
                        'in_stock' = > array(),
                        'out_of_stock' =>array(),
                );
            }

            $product['carrier_list'] = array();
            $id_warehouse = 0;
            foreach(warehouseCountByAddress[$product['id_address_delivery']]as $id_war = > $val){
                if (array_key_exists((int) $id_war, $product['warehouse_list'])) {
                    $product['carrier_list'] = JeproLabTools.array_replace
                    ($product['carrier_list'], Carrier::getAvailableCarrierList
                    (new Product($product['id_product']), $id_war, $product['id_address_delivery'], null, $this));
                    if (!$id_warehouse) {
                        $id_warehouse = (int) $id_war;
                    }
                }
            }

            if (!isset($grouped_by_warehouse[$product['id_address_delivery']]['in_stock'][$id_warehouse])) {
                $grouped_by_warehouse[$product['id_address_delivery']]['in_stock'][$id_warehouse] = array();
                $grouped_by_warehouse[$product['id_address_delivery']]['out_of_stock'][$id_warehouse] = array();
            }

            if (!$this -> allow_seperated_package) {
                $key = 'in_stock';
            } else {
                $key = $product['in_stock'] ? 'in_stock' : 'out_of_stock';
                $product_quantity_in_stock = StockAvailable::getQuantityAvailableByProduct
                ($product['id_product'], $product['id_product_attribute']);
                if ($product['in_stock'] && $product['cart_quantity'] > $product_quantity_in_stock) {
                    $out_stock_part = $product['cart_quantity'] - $product_quantity_in_stock;
                    $product_bis = $product;
                    $product_bis['cart_quantity'] = $out_stock_part;
                    $product_bis['in_stock'] = 0;
                    $product['cart_quantity'] -= $out_stock_part;
                    $grouped_by_warehouse[$product['id_address_delivery']]['out_of_stock'][$id_warehouse][]=
                    $product_bis;
                }
            }

            if (empty($product['carrier_list'])) {
                $product['carrier_list'] = array(0 = > 0);
            }

            $grouped_by_warehouse[$product['id_address_delivery']][$key][$id_warehouse][]=$product;
        }
        unset($product);

        // Step 3 : grouped product from grouped_by_warehouse by available carriers
        $grouped_by_carriers = array();
        for($grouped_by_warehouse as $id_address_delivery => $products_in_stock_list){
            if (!isset($grouped_by_carriers[$id_address_delivery])) {
                $grouped_by_carriers[$id_address_delivery] = array(
                        'in_stock' = > array(),
                        'out_of_stock' =>array(),
                );
            }
            foreach($products_in_stock_list as $key = > $warehouse_list){
                if (!isset($grouped_by_carriers[$id_address_delivery][$key])) {
                    $grouped_by_carriers[$id_address_delivery][$key] = array();
                }
                foreach($warehouse_list as $id_warehouse = > analyzeList){
                    if (!isset($grouped_by_carriers[$id_address_delivery][$key][$id_warehouse])) {
                        $grouped_by_carriers[$id_address_delivery][$key][$id_warehouse] = array();
                    }
                    foreach(analyzeList as $product) {
                        $package_carriers_key = implode(',', $product['carrier_list']);

                        if (!isset($grouped_by_carriers[$id_address_delivery][$key][$id_warehouse][$package_carriers_key])) {
                            $grouped_by_carriers[$id_address_delivery][$key][$id_warehouse][$package_carriers_key] = array(
                                    'product_list' = > array(),
                                    'carrier_list' =>$product['carrier_list'],
                                    'warehouse_list' =>$product['warehouse_list']
                            );
                        }

                        $grouped_by_carriers[$id_address_delivery][$key][$id_warehouse][$package_carriers_key]['product_list'][]=
                        $product;
                    }
                }
            }
        }

        $package_list = array();
        // Step 4 : merge product from grouped_by_carriers into $package to minimize the number of package
        for($grouped_by_carriers as $id_address_delivery => $products_in_stock_list){
            if (!isset($package_list[$id_address_delivery])) {
                $package_list[$id_address_delivery] = array(
                        'in_stock' = > array(),
                        'out_of_stock' =>array(),
                );
            }

            for($products_in_stock_list as $key = > $warehouse_list){
                if (!isset($package_list[$id_address_delivery][$key])) {
                    $package_list[$id_address_delivery][$key] = array();
                }
                // Count occurance of each carriers to minimize the number of packages
                $carrier_count = array();
                foreach($warehouse_list as $id_warehouse = > $products_grouped_by_carriers){
                    foreach($products_grouped_by_carriers as $data) {
                        foreach($data['carrier_list']as carrierId) {
                            if (!isset($carrier_count[carrierId])) {
                                $carrier_count[carrierId] = 0;
                            }
                            $carrier_count[carrierId]++;
                        }
                    }
                }
                arsort($carrier_count);
                for($warehouse_list as $id_warehouse = > $products_grouped_by_carriers){
                    if (!isset($package_list[$id_address_delivery][$key][$id_warehouse])) {
                        $package_list[$id_address_delivery][$key][$id_warehouse] = array();
                    }
                    foreach($products_grouped_by_carriers as $data) {
                        foreach($carrier_count as carrierId = > $rate){
                            if (array_key_exists(carrierId, $data['carrier_list'])) {
                                if (!isset($package_list[$id_address_delivery][$key][$id_warehouse][carrierId])) {
                                    $package_list[$id_address_delivery][$key][$id_warehouse][carrierId] = array(
                                            'carrier_list' = > $data['carrier_list'],
                                            'warehouse_list' =>$data['warehouse_list'],
                                            'product_list' =>array(),
                                    );
                                }
                                $package_list[$id_address_delivery][$key][$id_warehouse][carrierId]['carrier_list'] =
                                        array_intersect($package_list[$id_address_delivery][$key][$id_warehouse][carrierId]['carrier_list'], $data['carrier_list']);
                                $package_list[$id_address_delivery][$key][$id_warehouse][carrierId]['product_list'] =
                                        array_merge($package_list[$id_address_delivery][$key][$id_warehouse][carrierId]['product_list'], $data['product_list']);

                                break;
                            }
                        }
                    }
                }
            }
        }

        // Step 5 : Reduce depth of $package_list
        $final_package_list = array();
        for($package_list as $id_address_delivery => $products_in_stock_list){
            if (!isset($final_package_list[$id_address_delivery])) {
                $final_package_list[$id_address_delivery] = array();
            }

            for($products_in_stock_list as $key = > $warehouse_list){
                foreach($warehouse_list as $id_warehouse = > $products_grouped_by_carriers){
                    foreach($products_grouped_by_carriers as $data) {
                        $final_package_list[$id_address_delivery][]=array(
                                'product_list' = > $data['product_list'],
                                'carrier_list' =>$data['carrier_list'],
                                'warehouse_list' =>$data['warehouse_list'],
                                'id_warehouse' =>$id_warehouse,
                        );
                    }
                }
            }
        }
        $cache[$cache_key] = $final_package_list;
        return finalPackageList;
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

        public boolean analyze_restriction;

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

        public static Map<String, Integer> only_one_gift = new HashMap<>();


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
/*
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
                error = cartRule.checkValidity(context, true) ? "error for " + cartRule.cart_rule_id : "";
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
            JeproLabCartRuleModel.only_one_gift = new HashMap<>();
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
                    float taxRate = 0;
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
            if (this.gift_analyze_id > 0 && (filter == JeproLabCartRuleModel.FILTER_ACTION_ALL || filter == JeproLabCartRuleModel.FILTER_ACTION_ALL_NOCAP || filter == JeproLabCartRuleModel.FILTER_ACTION_GIFT)){
                int addressId = (is_null($package) ? 0 : $package['id_address']);
                for (JeproLabAnalyzeModel analyze : packageAnalyzes) {
                    if (analyze.analyze_id == this.gift_analyze_id && (analyze.analyze_attribute_id == this.gift_analyze_attribute_id || this.gift_analyze_attribute_id <= 0)) {
                        // The free gift coupon must be applied to one product only (needed for multi-shipping which manage multiple product lists)
                        if (!JeproLabCartRuleModel.only_one_gift.containsKey(this.cart_rule_id + "_" + this.gift_analyze_id)
                        || JeproLabCartRuleModel.only_one_gift.get(this.cart_rule_id + "_" + this.gift_analyze_id) == addressId
                                || JeproLabCartRuleModel.only_one_gift.get(this.cart_rule_id + "_" + this.gift_analyze_id) == 0
                                || addressId == 0
                                || !useCache) {
                            reductionValue += (useTax ? analyze.analyze_price.price_with_tax : analyze.analyze_price.price);
                            if (useCache && (!(JeproLabCartRuleModel.only_one_gift.containsKey(this.cart_rule_id + "_" + this.gift_analyze_id)) || JeproLabCartRuleModel.only_one_gift.containsKey(this.cart_rule_id + "_" + this.gift_analyze_id) == 0)) {
                                JeproLabCartRuleModel.only_one_gift.containsKey(this.cart_rule_id + "_" + this.gift_analyze_id) = addressId;
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

        public boolean checkValidity(JeproLabContext context){
            return checkValidity(context, false, true, true);
        }

        public boolean checkValidity(JeproLabContext context, boolean alreadyInCart){
            return checkValidity(context, alreadyInCart, true, true);
        }

        public boolean checkValidity(JeproLabContext context, boolean alreadyInCart, boolean displayError){
            return checkValidity(context, alreadyInCart, displayError, true);
        }

        /**
         * Check if this cart rule can be applied
         *
         * @param context
         * @param alreadyInCart Check if the voucher is already on the cart
         * @param displayError Display error
         * @return bool
         */
        public boolean checkValidity(JeproLabContext context, boolean alreadyInCart, boolean displayError, boolean checkCarrier){
            if (!JeproLabCartRuleModel.isFeaturePublished()) {
                return false;
            }

            if (!this.published){
                return (!displayError) ? false : JeproLabTools.displayBarMessage(400, 'This voucher is disabled');
            }
            
            if (this.quantity < 0) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(400, 'This voucher has already been used');
            }
            
            if (strtotime(this.date_from) > time()) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(400, 'This voucher is not valid yet');
            }
            if (strtotime(this.date_to) < time()) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(400, 'This voucher has expired');
            }
            String query;
            if(context.cart.customer_id > 0) {
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                //$quantityUsed = Db::getInstance()->getValue('
                query = "SELECT count(*) AS quantity FROM " + staticDataBaseObject.quoteName("#__jeprolab_request") + " AS request LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_request_cart_rule") + " AS request_cart_rule ON (request_cart_rule.";
                query += staticDataBaseObject.quoteName("request_id") + " = request." + staticDataBaseObject.quoteName("request_id");
                query += ") WHERE request." + staticDataBaseObject.quoteName("customer_id") + " = " + context.cart.customer_id ;
                query += " AND request_cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id + " AND ";
                query += JeproLabSettingModel.getStringValue("request_status_error") + " != request.";
                query += staticDataBaseObject.quoteName("current_status");
                
                staticDataBaseObject.setQuery(query);
                int quantityUsed = (int)staticDataBaseObject.loadValue("quantity");
                if (quantityUsed + 1 > this.quantity_per_customer) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400, 'You cannot use this voucher anymore (usage limit reached)');
                }
            }

            // Get an intersection of the customer groups and the cart rule groups (if the customer is not logged in, the default group is Visitors)
            if (this.group_restriction) {
                //$id_cart_rule = (int)Db::getInstance()->getValue('
                query = "SELECT cart_rule_group." + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " ;
                query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_group") + " AS cart_rule_group  WHERE cart_rule_group.";
                query += staticDataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id + " AND cart_rule_group.";
                query += staticDataBaseObject.quoteName("group_id") + (context.cart.customer_id > 0 ? " IN (SELECT customer_group." +
                        staticDataBaseObject.quoteName("group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_customer_group") +
                        " AS customer_group WHERE customer_group." + staticDataBaseObject.quoteName("customer_id") + " = " + context.cart.customer_id +
                        ")" : " = " + JeproLabSettingModel.getIntValue("unidentified_group"));

                staticDataBaseObject.setQuery(query);
                int cartRuleId = (int)staticDataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"You cannot use this voucher");
                }
            }

            // Check if the customer delivery address is usable with the cart rule
            if (this.country_restriction) {
                if (context.cart.delivery_address_id <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400, "You must choose a delivery address before applying this voucher to your order");
                }
                //$id_cart_rule = (int)Db::getInstance()->getValue('
                query = "SELECT cart_rule_country." + staticDataBaseObject.quoteName("cart_rule_id") + " FROM "  + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_country");
                query += " AS cart_rule_country WHERE cart_rule_country." + staticDataBaseObject.quoteName("cart_rule _id") + " = " + this.cart_rule_id + " AND cart_rule_country.";
                query += staticDataBaseObject.quoteName("country_id") + " = (SELECT address." + staticDataBaseObject.quoteName("country_id") + " FROM " ;
                query += staticDataBaseObject.quoteName("#__jeprolab_address") + " AS address WHERE aaddress." + staticDataBaseObject.quoteName("address_id");
                query += " = " + context.cart.delivery_address_id + " LIMIT 1)" ;
                
                staticDataBaseObject.setQuery(query);
                int cartRuleId = (int)staticDataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"You cannot use this voucher in your country of delivery");
                }
            }

            // Check if the carrier chosen by the customer is usable with the cart rule
            if (this.carrier_restriction && checkCarrier) {
                if (context.cart.carrier_id <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"You must choose a carrier before applying this voucher to your order");
                }
                //$id_cart_rule = (int)Db::getInstance()->getValue('
                query = "SELECT cart_rule_carrier." + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_carrier");
                query += " AS cart_rule_carrier INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier ON (carrier.";
                query += staticDataBaseObject.quoteName("reference_id") + " = cart_rule_carrier." + staticDataBaseObject.quoteName("carrier_id") + " AND carrier.";
                query += staticDataBaseObject.quoteName("deleted") + " = 0) WHERE cart_rule_carrier." + staticDataBaseObject.quoteName("cart_rule_id") + " = ";
                query += this.cart_rule_id + " AND carrier." + staticDataBaseObject.quoteName("carrier_id") + " = " + context.cart.carrier_id;


                staticDataBaseObject.setQuery(query);
                int cartRuleId = (int) staticDataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"You cannot use this voucher with this carrier");
                }
            }

            // Check if the cart rules apply to the laboratory browsed by the customer
            if (this.lab_restriction && context.laboratory.laboratory_id > 0 && JeproLabLaboratoryModel.isFeaturePublished()) {
                //$id_cart_rule = (int)Db::getInstance()->getValue('
                query = "SELECT cart_rule_lab." + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lab");
                query += " AS cart_rule_lab WHERE cart_rule_lab." + staticDataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id + " AND cart_rule_lab.";
                query += staticDataBaseObject.quoteName("lab_id") + " = " + context.laboratory.laboratory_id;

                staticDataBaseObject.setQuery(query);
                int cartRuleId = (int) staticDataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId < 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400, "You cannot use this voucher");
                }
            }

            // Check if the products chosen by the customer are usable with the cart rule
            if (this.analyze_restriction) {
                boolean restrictions = this.checkAnalyzeRestrictions(context, false, displayError, alreadyInCart);
                if (restrictions && displayError) {
                    return restrictions;
                } else if (!restrictions && !displayError) {
                    return false;
                }
            }

            // Check if the cart rule is only usable by a specific customer, and if the current customer is the right one
            if (this.customer_id > 0 && context.cart.customer_id != this.customer_id) {
                if (!JeproLabContext.getContext().customer.isLogged()) {
                    return (!displayError) ? false : (JeproLabTools.displayBarMessage(400,"You cannot use this voucher") + " - " + JeproLabTools.displayError("Please log in first"));
                }
                return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"You cannot use this voucher");
            }

            if (this.minimum_amount > 0 && checkCarrier) {
                // Minimum amount is converted to the contextual currency
                float minimumAmount = this.minimum_amount;
                if (this.minimum_amount_currency_id != JeproLabContext.getContext().currency.currency_id) {
                    minimumAmount = JeproLabTools.convertPriceFull(minimumAmount, new JeproLabCurrencyModel(this.minimum_amount_currency_id), JeproLabContext.getContext().currency);
                }

                float cartTotal = context.cart.getRequestTotal(this.minimum_amount_tax > 0, JeproLabCartModel.ONLY_ANALYZES);
                if (this.minimum_amount_shipping > 0) {
                    cartTotal += context.cart.getRequestTotal(this.minimum_amount_tax > 0, JeproLabCartModel.ONLY_SHIPPING);
                }
                List<JeproLabAnalyzeModel> analyzes = context.cart.getAnalyzes();
                List<JeproLabCartRuleModel> cartRules = context.cart.getCartRules();

                for(JeproLabCartRuleModel cartRule : cartRules) {
                    if (cartRule.gift_analyze_id > 0) {
                        for(JeproLabAnalyzeModel analyze : analyzes) {
                            if (empty($product['gift']) && analyze.analyze_id == cartRule.gift_analyze_id && analyze.analyze_attribute_id == cartRule.gift_analyze_attribute_id) {
                                cartTotal = JeproLabTools.roundPrice(
                                        cartTotal - (this.minimum_amount_tax > 0 ? analyze.analyze_price.price_with_tax : analyze.analyze_price.price),
                                        context.currency.decimals * JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                            }
                        }
                    }
                }

                if (cartTotal < minimumAmount) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"You have not reached the minimum amount required to use this voucher");
                }
            }

            /***
                This loop checks:
                - if the voucher is already in the cart
                - if a non compatible voucher is in the cart
                - if there are products in the cart (gifts excluded)
                Important note: this MUST be the last check, because if the tested cart rule has priority over a non combinable one in the cart, we will switch them
            */
            int numberOfAnalyzes = JeproLabCartModel.getNumberOfAnalyzes(context.cart.cart_id);
            List<JeproLabCartRuleModel> otherCartRules = new ArrayList<>();
            if (checkCarrier) {
                otherCartRules = context.cart.getCartRules();
            }
            if (otherCartRules.size() > 0) {
                JeproLabCartRuleModel cartRule;
                for(JeproLabCartRuleModel otherCartRule : otherCartRules) {
                    if (otherCartRule.cart_rule_id == this.cart_rule_id && !alreadyInCart) {
                        return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"This voucher is already in your cart");
                    }
                    if (otherCartRule.gift_analyze_id > 0) {
                        --numberOfAnalyzes;
                    }

                    if (this.cart_rule_restriction && otherCartRule.cart_rule_restriction && otherCartRule.cart_rule_id != this.cart_rule_id) {
                        query = "SELECT " + staticDataBaseObject.quoteName("cart_rule_id_1") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_combination");
                        query += " WHERE (" + staticDataBaseObject.quoteName("cart_rule_id_1") + " = " + this.cart_rule_id + " AND " + staticDataBaseObject.quoteName("cart_rule_id_2");
                        query += " = " + otherCartRule.cart_rule_id + ") OR (" + staticDataBaseObject.quoteName("cart_rule_id_2") + " = " + this.cart_rule_id + " AND ";
                        query += staticDataBaseObject.quoteName("cart_rule_id_1") + " = " + otherCartRule.cart_rule_id + ")";

                        staticDataBaseObject.setQuery(query);
                        int combine = (int)staticDataBaseObject.loadValue("cart_rule_id_1");
                        if (combine <= 0) {
                            cartRule = new JeproLabCartRuleModel(otherCartRule.cart_rule_id, context.cart.language_id);
                            // The cart rules are not combinable and the cart rule currently in the cart has priority over the one tested
                            if (cartRule.priority <= this.priority) {
                                return (!displayError) ? false : JeproLabTools.displayBarMessage(400,"This voucher is not combinable with an other voucher already in your cart: " + " " + cartRule.name.get("lang_" + cartRule.language_id));
                            }
                            // But if the cart rule that is tested has priority over the one in the cart, we remove the one in the cart and keep this new one
                            else {
                                context.cart.removeCartRule(cartRule.cart_rule_id);
                            }
                        }
                    }
                }
            }

            if (numberOfAnalyzes <= 0) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(400, "Cart is empty");
            }

            if (!displayError) {
                return true;
            }
        }

        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId){
            return getCustomerCartRules(langId, customerId, false, true, false, null, false, false);
        }

        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId, boolean activated){
            return getCustomerCartRules(langId, customerId, activated, true, false, null, false, false);
        }

        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId, boolean activated, boolean includeGeneric){
            return getCustomerCartRules(langId, customerId, activated, includeGeneric, false, null, false, false);
        }

        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId, boolean activated, boolean includeGeneric, boolean inStock){
            return getCustomerCartRules(langId, customerId, activated, includeGeneric, inStock, null, false, false);
        }

        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId, boolean activated, boolean includeGeneric, boolean inStock, JeproLabCartModel cart){
            return getCustomerCartRules(langId, customerId, activated, includeGeneric, inStock, cart, false, false);
        }

        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId, boolean activated, boolean includeGeneric, boolean inStock, JeproLabCartModel cart, boolean freeShippingOnly){
            return getCustomerCartRules(langId, customerId, activated, includeGeneric, inStock, cart, freeShippingOnly, false);
        }

        /**
         * @param langId
         * @param customerId
         * @param activated
         * @param includeGeneric
         * @param inStock
         * @param cart
         * @param freeShippingOnly
         * @param highLightOnly
         * @return array
         */
        public static List<JeproLabCartRuleModel> getCustomerCartRules(int langId, int customerId, boolean activated, boolean includeGeneric, boolean inStock, JeproLabCartModel cart, boolean freeShippingOnly, boolean highLightOnly){
            if (!JeproLabCartRuleModel.isFeaturePublished()) {
                return new ArrayList<>();
            }

            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String queryPart1 = "* FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule LEFT JOIN ";
            queryPart1 += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lang") + " AS cart_rule_lang ON (cart_rule_lang.";
            queryPart1 += staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            queryPart1 += " AND cart_rule_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") ";


            String queryPart2 = " AND cart_rule." + staticDataBaseObject.quoteName("date_from") + " < " + JeproLabTools.date("Y-m-d H:i:s");
            queryPart2 += " AND cart_rule." + staticDataBaseObject.quoteName("date_to") + " > " + JeproLabTools.date("Y-m-d H:i:s");
            queryPart2 += (activated ? "AND cart_rule." + staticDataBaseObject.quoteName("published") + " = 1" : "") ;
            queryPart2 += (inStock ? " AND cart_rule." + staticDataBaseObject.quoteName("quantity") + " > 0 " : "");

            if(freeShippingOnly) {
                queryPart2 += " AND " + staticDataBaseObject.quoteName("free_shipping") + " = 1 AND " + staticDataBaseObject.quoteName("carrier_restriction") + " = 1";
            }

            if (highLightOnly) {
                queryPart2 += " AND " + staticDataBaseObject.quoteName("highlight") + " = 1 AND "  + staticDataBaseObject.quoteName("code");
                queryPart2 += " NOT LIKE " + staticDataBaseObject.quote(JeproLabCartRuleModel.JEPROLAB_REQUEST_CODE_PREFIX) + "'%'";
            }

            String query = "(SELECT SQL_NO_CACHE " + queryPart1 + " WHERE cart_rule." + staticDataBaseObject.quoteName("customer_id") + " = ";
            query += customerId + " " + queryPart2 + ") UNION (SELECT " + queryPart1 + " WHERE cart_rule."  + staticDataBaseObject.quoteName("group_restriction");
            query += " = 1 " + queryPart2  + ")";

            if (includeGeneric && customerId != 0) {
                query += " UNION (SELECT " + queryPart1 + " WHERE cart_rule." + staticDataBaseObject.quoteName("customer_id") + " = 0 " + queryPart2 + ") ";
            }

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObjectList();

            //$result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql, true, false);

            if(resultSet == null) {
                return new ArrayList<>();
            }else{
                // Remove cart rule that does not match the customer groups
                List<Integer> customerGroups = JeproLabCustomerModel.getStaticGroups(customerId);
                List<JeproLabCartRuleModel> cartRules = new ArrayList<>();

                try{
                    JeproLabCartRuleModel cartRule;
                    while(resultSet.next()){
                        cartRule = new JeproLabCartRuleModel();
                        cartRule.cart_rule_id = resultSet.getInt("cart_rule_id");
                        cartRule.group_restriction = resultSet.getInt("group_restriction") > 0;
                        if(cartRule.group_restriction){
                            query = "SELECT " + staticDataBaseObject.quoteName("group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_group");
                            query += " AS cart_rule_group WHERE  cart_route_group." + staticDataBaseObject.quoteName("cart_rule_id") + " = " + cartRule.cart_rule_id;

                            staticDataBaseObject.setQuery(query);
                            ResultSet cartRuleGroupSet = staticDataBaseObject.loadObjectList();
                            if(cartRuleGroupSet != null) {
                                while (cartRuleGroupSet.next()){
                                    if(customerGroups.contains(cartRuleGroupSet.getInt("group_id"))){
                                        continue;
                                    }
                                }
                            }
                            unset($result[$key]);
                        }
                        cartRules.add(cartRule);
                    }

                    int quantityUsed;
                    for(cartRule : cartRules){
                        if (cartRule.quantity_per_customer > 0) {
                            quantityUsed = JeproLabRequestModel.getCustomerDiscounts(customerId, cartRule.cart_rule_id);
                            if (cart != null && cart.cart_id > 0) {
                                quantityUsed += cart.getDiscountsCustomer(cartRule.cart_rule_id);
                            }
                            cartRule.quantity_per_customer = cartRule.quantity_per_customer - quantityUsed;
                        } else {
                            cartRule.quantity_per_customer = 0;
                        }
                    }
                    unset($cart_rule);

                    for($result as $key => $cart_rule){
                        if ($cart_rule['shop_restriction']) {
                            $cartRuleShops = Db::getInstance () -> executeS('SELECT id_shop FROM '._DB_PREFIX_.
                            'cart_rule_shop WHERE id_cart_rule = '. (int) $cart_rule['id_cart_rule']);
                            foreach($cartRuleShops as $cartRuleShop) {
                                if (JeproLabLaboratoryModel.isFeaturePublished() && (cartRuleLab.laboratory_id == JeproLabContext.getContext().laboratory.laboratory_id)){
                                    continue 2;
                                }
                            }
                            unset($result[$key]);
                        }
                    }

                    if (isset($cart) && isset($cart->id)) {
                        JeproLabCartRuleModel cr;
                        for($result as $key = > $cart_rule){
                            if (cartRule.analyze_restriction) {
                                cr = new JeproLabCartRuleModel(cartRule.cart_rule_id);
                                boolean restriction = cr.checkAnalyzeRestrictions(JeproLabContext.getContext(), false, false);
                                if (restriction) {
                                    continue;
                                }
                                unset($result[$key]);
                            }
                        }
                    }

                    $result_bak = $result;
                    $result = array();
                    boolean countryRestriction = false;
                    for($result_bak as $key => $cart_rule) {
                        if(cartRule.country_restriction) {
                            $country_restriction = true;
                            //$countries = Db::getInstance()->ExecuteS('
                            query = "SELECT " + staticDataBaseObject.quoteName("country_id") + " FROM " + staticDataBaseObject.quoteName("__jeprolab_address");
                            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId + " AND " + staticDataBaseObject.quoteName("deleted") + "= 0";

                            staticDataBaseObject.setQuery(query);
                            ResultSet countriesSet = staticDataBaseObject.loadObjectList();

                            if (countriesSet != null) {
                                try{
                                    int cartRuleId;
                                    while(countriesSet.next()) {
                                        //$id_cart_rule = (bool)Db::getInstance()->getValue('
                                        query = "SELECT cart_rule_country." + staticDataBaseObject.quoteName("cart_rule_id");
                                        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_country");
                                        query += " AS cart_rule_country WHERE cart_rule_country." + staticDataBaseObject.quoteName("cart_rule_id");
                                        query += " = " + cartRule.cart_rule_id + " AND cart_rule_country." + staticDataBaseObject.quoteName("country_id");
                                        query += " = " + countriesSet.getInt("country_id");

                                        staticDataBaseObject.setQuery(query);
                                        cartRuleId = (int)staticDataBaseObject.loadValue("cart_rule_id");
                                        if (cartRuleId > 0) {
                                            $result[] = $result_bak[$key];
                                            break;
                                        }
                                    }
                                }catch(SQLException ignored){
                                    ignored.printStackTrace();
                                }
                            }
                        }else {
                            $result[]=$result_bak[$key];
                        }
                    }

                    if (!countryRestriction){   $result = $result_bak; }


                    unset($cart_rule);

                    return $result;
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
        }


        /**
         * @param customerId
         * @return bool
         */
        public boolean usedByCustomer(int customerId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_request_cart_rule");
            query += " AS request_cart_rule LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_request") + " AS request ON (request_cart_rule.";
            query += staticDataBaseObject.quoteName("request_d") + " = request." + staticDataBaseObject.quoteName("request_id") + " WHERE request_cart_rule.";
            query += staticDataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id + " AND request." + staticDataBaseObject.quoteName("customer_id");
            query += " = " + customerId;

            staticDataBaseObject.quoteName(query);
            return staticDataBaseObject.loadValue("cart_rule_id") > 0;
        }

        /**
         * @param name
         * @return bool
         */
        public static boolean cartRuleExists(String name){
            if (!JeproLabCartRuleModel.isFeaturePublished()) {
                return false;
            }

            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule");
            query += " WHERE " + staticDataBaseObject.quoteName("code") + " = " + staticDataBaseObject.quote(name);

            staticDataBaseObject.setQuery(query);

            return staticDataBaseObject.loadValue("cart_rule_id") > 0;
        }

        /**
         * @param customerId
         * @return bool
         * /
        public static boolean deleteByCustomerId(int customerId){
            boolean result = true;
            $cart_rules = new PrestaShopCollection('CartRule');
            $cart_rules->where('id_customer', '=', $id_customer);
            foreach ($cart_rules as $cart_rule) {
            $return &= $cart_rule->delete();
            }
            return result;
        }

        /**
         * @return array
         */
        public Map<Integer, Map<String, Object>> getAnalyzeRuleGroups(){
            if (!JeproLabTools.isLoadedObject(this, "cart_rule_id") || !this.analyze_restriction){
                return new HashMap<>();
            }

            Map<Integer, Map<String, Object>> analyzeRuleGroups = new HashMap<>();
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_group");
            query += " WHERE " + staticDataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id;

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObjectList();
            if(resultSet != null) {
                try {
                    Map<String, Object> values;
                    while(resultSet.next()) {
                        if (!analyzeRuleGroups.containsKey(resultSet.getInt("analyze_rule_group_id"))){
                            values = new HashMap<>();
                            values.put("analyze_rule_group_id", resultSet.getInt("analyze_rule_group_id"));
                            values.put("quantity", resultSet.getInt("quantity"));
                            analyzeRuleGroups.put(resultSet.getInt("analyze_rule_group_id"), values);
                        }
                        values = analyzeRuleGroups.get(resultSet.getInt("analyze_rule_rule_id"));
                        values.put("analyze_rules", this.getAnalyzeRules(resultSet.getInt("analyze_rule_rule_id")));
                        analyzeRuleGroups.put(resultSet.getInt("analyze_rule_group_id"), values);
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
            return analyzeRuleGroups;
        }

        /**
         * @param analyzeRuleGroupId
         * @return array ('type' => ? , 'values' => ?)
         */
        public Map<Integer, Map<String, Object>> getAnalyzeRules(int analyzeRuleGroupId){
            if (!JeproLabTools.isLoadedObject(this, "cart_rule_id") || this.analyze_restriction) {
                return new HashMap<>();
            }

            Map<Integer, Map<String, Object>> analyzeRules = new HashMap<>();
            //$results = Db::getInstance()->executeS('
            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule") + " AS analyze_rule ";
            query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value") + " AS analyze_rule_value ";
            query += " ON analyze_rule." + staticDataBaseObject.quoteName("analyze_rule_id") + " = analyze_rule_value." ;
            query += staticDataBaseObject.quoteName("analyze_rule_id") + " WHERE analyze_rule." + staticDataBaseObject.quoteName("analyze_rule_group_id");
            query += " = " + analyzeRuleGroupId;

            staticDataBaseObject.setQuery(query);
            ResultSet rulesSet = staticDataBaseObject.loadObjectList();
            if(rulesSet != null) {
                try {
                    List<Integer> values;
                    Map<String, Object> field;
                    while(rulesSet.next()) {
                        if (!analyzeRules.containsKey(rulesSet.getInt("analyze_rule_id"))){
                            values = new ArrayList<>();
                            field = new HashMap<>();
                            field.put("type", rulesSet.getString("type"));
                            field.put("values", values);
                            analyzeRules.put(rulesSet.getInt("analyze_rule_id"), field);
                        }
                        values = (List<Integer>)analyzeRules.get(rulesSet.getInt("analyze_rule_id")).get("values");
                        values.add(rulesSet.getInt("item_id"));
                        analyzeRules.get(rulesSet.getInt("analyze_rule_id")).put("values", values);
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch(Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
            return analyzeRules;
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context){
            return checkAnalyzeRestrictions(context, false, true, false);
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context, boolean setAnalyzes){
            return checkAnalyzeRestrictions(context, setAnalyzes, true, false);
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context, boolean setAnalyzes, boolean displayError){
            return checkAnalyzeRestrictions(context, setAnalyzes, displayError, false);
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context, boolean setAnalyzes, boolean displayError, boolean alreadyInCart){
            $selected_products = array();

            String query;
            // Check if the products chosen by the customer are usable with the cart rule
            if(this.analyze_restriction) {
                Map<Integer, Map<String, Object>> analyzeRuleGroups = this.getAnalyzeRuleGroups();
                List<String> eligibleAnalyzesList;
                List<JeproLabAnalyzeModel> analyzes;
                int analyzeRuleGroupId;
                for(Map.Entry entryGroup : analyzeRuleGroups.entrySet()){
                    analyzeRuleGroupId = (int)entryGroup.getKey();
                    eligibleAnalyzesList = new ArrayList<>();
                    if (context.cart != null){
                        analyzes = context.cart.getAnalyzes();
                        eligibleAnalyzesList.addAll(analyzes.stream().map(analyze -> analyze.analyze_id + "_" + analyze.analyze_attribute_id).collect(Collectors.toList()));
                    }
                    if (eligibleAnalyzesList.isEmpty()){
                        return (!displayError) ? false : JeproLabTools.displayBarMessage(400, "You cannot use this voucher in an empty cart");
                    }

                    int countMatchingAnalyzes = 0;
                    List matchingAnalyzesList = new ArrayList<>();
                    ResultSet cartAttributes;
                    Map<Integer, Map<String, Object>> analyzeRules = this.getAnalyzeRules(analyzeRuleGroupId);
                    String ruleType;
                    for(Map.Entry entry : analyzeRules.entrySet()){ //$product_rules as $product_rule) {
                        ruleType = entry.getKey().toString().equals("type") ? entry.getValue().toString() : "";
                        //entry.
                        switch (ruleType){
                            case "attributes" :
                                //$cart_attributes = Db::getInstance()->executeS('
                                query = "SELECT cart_analyze." + staticDataBaseObject.quoteName("quantity") + ", cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += ", analyze_attribute_combination." + staticDataBaseObject.quoteName("attribute_id") + ", cart_analyze." + staticDataBaseObject.quoteName("analyze_attribute_id");
                                query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS cart_analyze LEFT JOIN ";
                                query += staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination") + " AS analyze_attribute_combination ON cart_analyze.";
                                query += staticDataBaseObject.quoteName("analyze_attribute_id") + " = analyze_attribute_combination." + staticDataBaseObject.quoteName("attribute_attribute_id");
                                query += " WHERE cart_analyze." + staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + " AND cart_analyze.";
                                query += staticDataBaseObject.quoteName("analyze_id") + " IN (".implode(',', array_map('intval', $eligible_products_list)) + ") AND cart_analyze.";
                                query += staticDataBaseObject.quoteName("analyze_attribute_id") + "> 0";
                                
                                staticDataBaseObject.setQuery(query);
                                cartAttributes = staticDataBaseObject.loadObjectList();
                                //List<String> matchingAnalyzesList = new ArrayList<>();
                                if(cartAttributes !=null) {
                                    try {
                                        while(cartAttributes.next()){ // as $cart_attribute) {
                                            if (in_array(cartAttributes.getInt("attribute_id"), $product_rule['values'])) {
                                                countMatchingAnalyzes += cartAttributes.getInt("quantity");
                                                if (alreadyInCart && this.gift_analyze_id == cartAttributes.getInt("analyze_id")
                                                        && this.gift_analyze_attribute_id == cartAttributes.getInt("analyze_attribute_id")){
                                                    --countMatchingAnalyzes;
                                                }
                                                matchingAnalyzesList.add(cartAttributes.getInt("analyze_id") + "_" + cartAttributes.getInt("analyze_attribute_id"));
                                            }
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
                                if (countMatchingAnalyzes < $product_rule_group['quantity']) {
                                    return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                                }
                                eligibleAnalyzesList = JeproLabCartRuleModel.arrayIUntersect($eligible_products_list, $matching_products_list);
                            break;
                            case "analyzes":
                                //$cart_products = Db::getInstance()->executeS('
                                query = "SELECT cart_analyze." + staticDataBaseObject.quoteName("quantity") + ", cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS cart_analyze WHERE cart_analyze.";
                                query += staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + " AND cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += "IN (" .implode(',', array_map('intval', $eligible_products_list)) + ")";

                                countMatchingAnalyzes = 0;
                                //$matching_products_list = array();
                                staticDataBaseObject.setQuery(query);
                                ResultSet cartAnalyzesSet = staticDataBaseObject.loadObjectList();
                                if(cartAnalyzesSet != null) {
                                    try {
                                        while(cartAnalyzesSet.next()) {
                                            if (in_array($cart_product['id_product'], $product_rule['values'])) {
                                                countMatchingAnalyzes += cartAnalyzesSet.getInt("quantity");
                                                if (alreadyInCart && this.gift_analyze_id == cartAnalyzesSet.getInt("analyze_id")) {
                                                    --countMatchingAnalyzes;
                                                }
                                                matchingAnalyzesList.add(cartAnalyzesSet.getInt("anlyze_id") + "_0");
                                            }
                                        }
                                        if (countMatchingAnalyzes < $product_rule_group['quantity']) {
                                            return (!displayError) ? false : Tools::displayError
                                            ('You cannot use this voucher with these products');
                                        }
                                        eligibleAnalyzesList = JeproLabCartRuleModel.arrayUIntersect(eligibleAnalyzesList, matchingAnalyzesList);
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
                            break;
                            case "categories":
                                //$cart_categories = Db::getInstance()->executeS('
                                query = "SELECT cart_analyze." + staticDataBaseObject.quoteName("quantity") + ", cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += ", cart_analyze." + staticDataBaseObject.quoteName("analyze_attribute_id") + ", analyze_category." ;
                                query += staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
                                query += " AS cart_analyze LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_category") + " AS analyze_category ";
                                query += " ON analyze_category." + staticDataBaseObject.quoteName("analyze_id") + " = analyze_category.";
                                query += staticDataBaseObject.quoteName("analyze_id") + " WHERE analyze_category." + staticDataBaseObject.quoteName("cart_id");
                                query += " = "+ context.cart.cart_id + " AND analyze_category." + staticDataBaseObject.quoteName("analyze_id") + " IN (" ;
                                query += implode(',', array_map('intval', $eligible_products_list)) + ") AND analyze_category." + staticDataBaseObject.quoteName("analyze_id") + " <> ";
                                query += this.gift_analyze_id;

                                staticDataBaseObject.setQuery(query);
                                ResultSet cartCategorySet = staticDataBaseObject.loadObjectList();
                                countMatchingAnalyzes = 0;
                                matchingAnalyzesList = new ArrayList<>();
                                if(cartCategorySet != null) {
                                    try {
                                        String key;
                                        while(cartCategorySet.next()){
                                            key = cartCategorySet.getInt("analyze_id") + "_" + cartCategorySet.getInt("analyze_attribute_id");
                                            if (in_array(cartCategorySet.getInt("category_id"), $product_rule['values']) && !matchingAnalyzesList.contains(key)){
                                                /**
                                                 * We also check that the product is not already in the matching product list,
                                                 * because there are doubles in the query results (when the product is in multiple categories)
                                                 */
                                                countMatchingAnalyzes += cartCategorySet.getInt("quantity");
                                                matchingAnalyzesList.add(key);
                                            }
                                        }
                                        if (countMatchingAnalyzes < $product_rule_group['quantity']) {
                                            return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                                        }
                                        // Attribute id is not important for this filter in the global list, so the ids are replaced by 0
                                        foreach ($matching_products_list as &$matching_product) {
                                            $matching_product = preg_replace('/^([0-9]+)-[0-9]+$/', '$1-0', $matching_product);
                                        }
                                        eligibleAnalyzesList = JeproLabCartRuleModel.arrayUIntersect(eligibleAnalyzesList, matchingAnalyzesList);
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
                                break;
                            case "manufacturers":
                                //$cart_manufacturers = Db::getInstance()->executeS('
                                query = "SELECT cart_analyze." + staticDataBaseObject.quoteName("quantity") + ", cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += ", analyze." + staticDataBaseObject.quoteName("manufacturer_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
                                query += " AS cart_analyze LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ON cart_analyze.";
                                query += staticDataBaseObject.quoteName("analyze_id") + " = analyze." + staticDataBaseObject.quoteName("analyze_id") + " WHERE cart_analyze.";
                                query += staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + " AND cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += " IN (" +.implode(',', array_map('intval', $eligible_products_list)) + ")";

                                staticDataBaseObject.setQuery(query);
                                ResultSet cartManufacturersSet = staticDataBaseObject.loadObjectList();
                                countMatchingAnalyzes = 0;
                                //$matching_products_list = array();
                                if(cartManufacturersSet != null) {
                                    try {
                                        while(cartManufacturersSet.next()) {
                                            if (in_array($cart_manufacturer['id_manufacturer'], $product_rule['values'])) {
                                                countMatchingAnalyzes += cartManufacturersSet.getInt("quantity");
                                                matchingAnalyzesList.add(cartManufacturersSet.getInt("analyze_id") + "_0");
                                            }
                                        }
                                        if (countMatchingAnalyzes < $product_rule_group['quantity']) {
                                            return (!displayError) ? false : Tools::displayError
                                            ('You cannot use this voucher with these products');
                                        }

                                        eligibleAnalyzesList = JeproLabCartRuleModel.arrayUIntersect(eligibleAnalyzesList, matchingAnalyzesList);
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
                            break;
                            case "suppliers":
                                //$cart_suppliers = Db::getInstance()->executeS('
                                query = "SELECT cart_analyze." + staticDataBaseObject.quoteName("quantity") + ", cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += ", analyze." + staticDataBaseObject.quoteName("supplier_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
                                query += " AS cart_analyze LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ON cart_analyze.";
                                query += staticDataBaseObject.quoteName("analyze_id") + " = analyze." + staticDataBaseObject.quoteName("analyze_id") + " WHERE cart_analyze.";
                                query += staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + " AND cart_analyze." + staticDataBaseObject.quoteName("analyze_id");
                                query += " IN (" .implode(',', array_map('intval', $eligible_products_list)) + ")";

                                countMatchingAnalyzes = 0;

                                staticDataBaseObject.setQuery(query);
                                ResultSet cartSuppliers = staticDataBaseObject.loadObjectList();

                                if(cartSuppliers !=null) {
                                    try {
                                        while(cartSuppliers.next()) {
                                            if(analyzeRulesValues.contains(cartSuppliers.getInt("supplier_id"))){
                                                countMatchingAnalyzes += cartSuppliers.getInt("quantity");
                                                matchingAnalyzesList.add(cartSuppliers.getInt("analyze_id") + "_0");
                                            }
                                        }
                                        if(countMatchingAnalyzes < $product_rule_group['quantity']) {
                                            return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                                        }
                                        eligibleAnalyzesList = JeproLabCartRuleModel.arrayUIntersectCompare(eligibleAnalyzesList, matchingAnalyzesList);
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
                                /*foreach ($cart_suppliers as $cart_supplier) {
                                if (in_array($cart_supplier['id_supplier'], $product_rule['values'])) {
                                    $count_matching_products += $cart_supplier['quantity'];
                                    $matching_products_list[] = $cart_supplier['id_product'].'-0';
                                }
                            }*/

                            break;
                        }

                        if (eligibleAnalyzesList.size() == 0){
                            return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                        }
                    }
                    $selected_products = array_merge($selected_products, $eligible_products_list);
                }
            }
            /*
            if ($return_products) {
                return $selected_products;
            } */
            return (! displayError);
        }

        protected static List<String> arrayUIntersect(List<String> array1, List<String> array2){
            List<String> intersection = new ArrayList<>();
            for(String value1 : array1) {
                for(String value2 : array2) {
                    if (JeproLabCartRuleModel.arrayUIntersectCompare(value1, value2)){
                        intersection.add(value1);
                        break;
                    }
                }
            }
            return intersection;
        }

        protected static boolean arrayUIntersectCompare(String a, String b){
            if (a.equals(b)) {
                return true;
            }

            String[] aSplit = a.split("_"); //explode('-', $a);
            String[] bSplit = b.split("_"); //explode('-', $b);
            if (aSplit[0] == bSplit[0] && (Integer.parseInt(aSplit[1]) <= 0 || Integer.parseInt(bSplit[1]) <= 0)) {
                return true;
            }

            return false;
        }



        /**
         * Make sure caches are empty
         * Must be called before calling multiple time getContextualValue()
         * /
        public static function cleanCache(){
            self::$only_one_gift = array();
        }


        /**
         * @param type
         * @param activeOnly
         * @param bool   $i18n
         * @param offset
         * @param limit
         * @param string $search_cart_rule_name
         * @return array|bool
         * @throws PrestaShopDatabaseException
         * /
        public function getAssociatedRestrictions(String type, boolean activeOnly, boolean i18n, int offset = null, int limit = null, String searchCartRuleName = ''){
            $array = array('selected' => array(), 'unselected' => array());

            if (!in_array($type, array('country', 'carrier', 'group', 'cart_rule', 'shop'))) {
                return false;
            }

            $shop_list = '';
            if ($type == 'shop') {
                $shops = JeproLabContext.getContext().employee.getAssociatedLaboratories();
                if (count($shops)) {
                    $shop_list = ' AND t.id_shop IN ('.implode(array_map('intval', $shops), ',').') ';
                }
            }

            if ($offset !== null && $limit !== null) {
                $sql_limit = ' LIMIT '.(int)$offset.', '.(int)($limit+1);
            } else {
                $sql_limit = '';
            }

            if (!Validate::isLoadedObject($this) || this.{$type.'_restriction'} == 0) {
            $array['selected'] = Db::getInstance()->executeS('
                    SELECT t.*'.($i18n ? ', tl.*' : '').', 1 as selected
            FROM `'._DB_PREFIX_.$type.'` t
            '.($i18n ? 'LEFT JOIN `'._DB_PREFIX_.$type.'_lang` tl ON (t.id_'.$type.' = tl.id_'.$type.' AND tl.id_lang = '.(int)Context::getContext()->language->id.')' : '').'
            WHERE 1
            '.($active_only ? 'AND t.active = 1' : '').'
            '.(in_array($type, array('carrier', 'shop')) ? ' AND t.deleted = 0' : '').'
            '.($type == 'cart_rule' ? 'AND t.id_cart_rule != '.(int)this.id : '').
            $shop_list.
            (in_array($type, array('carrier', 'shop')) ? ' ORDER BY t.name ASC ' : '').
            (in_array($type, array('country', 'group', 'cart_rule')) && $i18n ? ' ORDER BY tl.name ASC ' : '').
                    $sql_limit);
        } else {
            if ($type == 'cart_rule') {
                $array = this.getCartRuleCombinations($offset, $limit, $search_cart_rule_name);
            } else {
                $resource = Db::getInstance()->query('
                        SELECT t.*'.($i18n ? ', tl.*' : '').', IF(crt.id_'.$type.' IS NULL, 0, 1) as selected
                FROM `'._DB_PREFIX_.$type.'` t
                '.($i18n ? 'LEFT JOIN `'._DB_PREFIX_.$type.'_lang` tl ON (t.id_'.$type.' = tl.id_'.$type.' AND tl.id_lang = '.(int)Context::getContext()->language->id.')' : '').'
                LEFT JOIN (SELECT id_'.$type.' FROM `'._DB_PREFIX_.'cart_rule_'.$type.'` WHERE id_cart_rule = '.(int)this.id.') crt ON t.id_'.($type == 'carrier' ? 'reference' : $type).' = crt.id_'.$type.'
                WHERE 1 '.($active_only ? ' AND t.active = 1' : '').
                $shop_list
                        .(in_array($type, array('carrier', 'shop')) ? ' AND t.deleted = 0' : '').
                (in_array($type, array('carrier', 'shop')) ? ' ORDER BY t.name ASC ' : '').
                (in_array($type, array('country', 'group', 'cart_rule')) && $i18n ? ' ORDER BY tl.name ASC ' : '').
                        $sql_limit,
                        false);
                while ($row = Db::getInstance()->nextRow($resource)) {
                    $array[($row['selected'] || this.{$type.'_restriction'} == 0) ? 'selected' : 'unselected'][] = $row;
                }
            }
        }
            return $array;
        }


        /**
         * Retrieves the id associated to the given code
         *
         * @param codeFilter
         * @return int|bool
         */
        public static int getCartRuleIdByCode(String codeFilter){
            if (!JeproLabTools.isCleanCode(codeFilter)) {
                return 0;
            }
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("cart_rule_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule");
            query += " WHERE " + staticDataBaseObject.quoteName("code") + " = " + staticDataBaseObject.quote(codeFilter);

            staticDataBaseObject.setQuery(query);
            return (int)staticDataBaseObject.loadValue("cart_rule_id");
        }


    }


}