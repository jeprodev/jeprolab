package com.jeprolab.models;

import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by jeprodev on 09/01/2016.
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

    public boolean should_exclude_gifts_discount  = false;

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

    protected static Map<String,List> _package_list = new HashMap<>();

    protected static Map<String, HashMap<String, String>> _attributes_lists = new HashMap<>();
    protected static Map<Integer, List> _delivery_option_list = new HashMap<>();
    protected static Map<Integer, JeproLabCarrierModel> _carriers = new HashMap<>();
    protected static Map<Integer, JeproLabAddressModel> _addresses = new HashMap<>();

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
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
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

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_cart") + "(" + dataBaseObject.quoteName("lab_group_id");
        query += ", " + dataBaseObject.quoteName("lab_id") +  ", " + dataBaseObject.quoteName("carrier_id") + ", ";
        query += dataBaseObject.quoteName("delivery_option") + ", " + dataBaseObject.quoteName("lang_id") + ", ";
        query += dataBaseObject.quoteName("delivery_address_id") +  ", " + dataBaseObject.quoteName("invoice_address_id");
        query += ", " + dataBaseObject.quoteName("currency_id") +  ", " + dataBaseObject.quoteName("customer_id");
        query += ", " + dataBaseObject.quoteName("guest_id") +  ", " + dataBaseObject.quoteName("secure_key");
        query += ", " + dataBaseObject.quoteName("recyclable") +  ", " + dataBaseObject.quoteName("gift") + ", ";
        query += dataBaseObject.quoteName("gift_message") +  ", " + dataBaseObject.quoteName("mobile_theme") + ", ";
        query += dataBaseObject.quoteName("allow_separated_result")  +  ", " + dataBaseObject.quoteName("date_add");
        query += ", " + dataBaseObject.quoteName("date_upd") + ") VALUES (" + this.laboratory_group_id + ", " + this.laboratory_id + ", " + this.carrier_id;
        query += ", " + dataBaseObject.quote(this.delivery_option) + ", " + this.language_id + ", " + this.delivery_address_id + ", ";
        query += this.invoice_address_id + ", " + this.currency_id + ", " + this.customer_id + ", " + this.guest_id + ", ";
        query += dataBaseObject.quote(this.secure_key) + ", " + (this.recyclable ? 1 : 0) + ", " + (this.gift ? 1 : 0) + ", ";
        query += dataBaseObject.quote(this.gift_message) + ", " + (this.mobile_theme ? 1 : 0) + ", " + (this.allow_separated_result ? 1 : 0);
        query += ", " + dataBaseObject.quote(JeproLabTools.date()) + ", " + dataBaseObject.quote(JeproLabTools.date()) + ") ";

        dataBaseObject.setQuery(query);
        dataBaseObject.query(true);

        this.cart_id = dataBaseObject.getGeneratedKey();
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

    public static class JeproLabCartRuleModel  extends JeproLabModel{
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
        String query = "DELETE cart_rule_analyze_rule_value FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule");
        query += " AS cart_rule_analyze_rule LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value");
        query += " AS cart_rule_analyze_rule_value ON cart_rule_analyze_rule." + dataBaseObject.quoteName("analyze_rule_id") + " =";
        query += "cart_rule_analyze_rule_value." + dataBaseObject.quoteName("analyze_rule_id") + " WHERE cart_rule_analyze_rule.";
        query += dataBaseObject.quoteName("type") + " = " + dataBaseObject.quoteName(type) + " AND cart_rule_analyze_rule_value.";
        query += dataBaseObject.quoteName("item_id") + " IN (" + itemList + ")";

        dataBaseObject.setQuery(query);

        Db::getInstance()->execute('
            '); // $list is checked a few lines above

        // Delete the product rules that does not have any values
        if (dataBaseObject.executeQuery() > 0) {
            query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule") + " NOT EXISTS (SELECT";
            query += " 1 FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value") + " WHERE ";
            query += dataBaseObject.quoteName(".'cart_rule_product_rule`.`id_product_rule` = `'.dataBaseObject.quoteName(".'cart_rule_product_rule_value`.`id_product_rule`)');
        }
        // If the product rules were the only conditions of a product rule group, delete the product rule group
        if (Db::getInstance()->Affected_Rows() > 0) {
        Db::getInstance()->delete('cart_rule_product_rule_group', 'NOT EXISTS (SELECT 1 FROM `'.dataBaseObject.quoteName(".'cart_rule_product_rule`
        WHERE `'.dataBaseObject.quoteName(".'cart_rule_product_rule`.`id_product_rule_group` = `'.dataBaseObject.quoteName(".'cart_rule_product_rule_group`.`id_product_rule_group`)');
    }

        // If the product rule group were the only restrictions of a cart rule, update de cart rule restriction cache
        if (Db::getInstance()->Affected_Rows() > 0){
            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_group") + " AS cart_rule_analyze_rule_group ";
            query += " ON cart_rule.cart_rule_id = cart_rule_analyze_rule_group.cart_rule_id SET analyze_restriction = IF(";
            query += " cart_rule_analyze_rule_group.analyze_rule_group_id IS NULL, 0, 1)";

            dataBaseObject.setQuery(query);
            dataBaseObject.query(false);
        }*/
            return true;
        }
    }
}
