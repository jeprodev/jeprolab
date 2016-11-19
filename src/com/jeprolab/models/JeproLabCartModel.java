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

    protected static Map<Integer, JeproLabAnalyzeModel>_number_of_analyzes = new HashMap<>();

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