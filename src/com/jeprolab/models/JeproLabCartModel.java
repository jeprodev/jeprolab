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

    /** @var bool True if the customer wants a recycled package */
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

    public boolean allow_separated_package = false;

    public String delivery_option;

    public boolean checkedTos = false;

    public List<JeproLabAnalyzeModel> _analyzes;

    /** @var Customer|null */
    protected static JeproLabCustomerModel customer = null;

    protected static Map<Integer, JeproLabAddressModel> addresses = new HashMap<>();
    protected static Map<Integer, Integer> _cache_number_addresses = new HashMap<>();

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
                        this.allow_separated_package = cartSet.getInt("allow_separated_package") > 0;
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
                this.allow_separated_package = cart.allow_separated_package;
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

    public boolean addCart(){
        if (this.language_id <= 0) {
            this.language_id = JeproLabSettingModel.getIntValue("default_lang");
        }
        if (this.laboratory_id <= 0) {
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        /*$return = parent::add($autodate, $null_values);
        Hook::exec('actionCartSave'); */
        //todo edit edit adding
        return true;
    }

    public boolean updateCart(){
        if (JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)) {
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)) {
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }

        /*this._analyzes = null;
        $return = parent::update($null_values);
        Hook::exec('actionCartSave');*/
        //todo
        return true;
    }

    public static List getCustomerCarts(int customerId){
        return getCustomerCarts(customerId, true);
    }

    public static List getCustomerCarts(int customerId, boolean withRequest){
        if (staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart") + " AS cart WHERE cart.";
        query += staticDataBaseObject.quoteName("customer_id") + " = " + customerId ;
        query += (!withRequest ? " AND NOT EXISTS (SELECT 1 FROM "  + staticDataBaseObject.quoteName("#__jeprolab_request") + " AS request WHERE request."
                + staticDataBaseObject.quoteName("cart_id") + " = customer." + staticDataBaseObject.quoteName("cart_id") : "");
        query += " ORDER BY customer." + staticDataBaseObject.quoteName("date_add") + " DESC";

        staticDataBaseObject.setQuery(query);
        ResultSet cartSet = staticDataBaseObject.loadObjectList();
        List carts = new ArrayList<>();

        if(cartSet != null){
            try {
                JeproLabCartModel cart;
                while (cartSet.next()){
                    cart = new JeproLabCartModel();
                    cart.cart_id = cartSet.getInt("cart_id");

                    carts.add(cart);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }
        }

        return carts;
    }

    public static void cacheSomeAttributesLists(List<Integer> analyzeAttributeList, int langId){
        if (!JeproLabCombinationModel.isFeaturePublished()) {
            List<Integer> analyzeAttributeImplode = new ArrayList<>();
            String analyzeAttributeImplodeList = "";
            for (Integer analyzeAttributeId : analyzeAttributeList) {
                /*if ((int) $id_product_attribute && !array_key_exists($id_product_attribute.
                '-'.$id_lang, self::$_attributesLists)){
                    $pa_implode[]=(int) $id_product_attribute;
                    self::$_attributesLists[(int) $id_product_attribute. '-'.$id_lang]=
                    array('attributes' = > '', 'attributes_small' =>'');
                }*/
            }

            if (analyzeAttributeImplode.size() > 0) {
                if (staticDataBaseObject != null) {
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }

                String query = "SELECT analyze_attribute_combination." + staticDataBaseObject.quoteName("analyze_attribute_id");
                query += ", attribute_group_lang." + staticDataBaseObject.quoteName("public_name") + " AS public_group_name, ";
                query += "attribute_lang." + staticDataBaseObject.quoteName("name") + " AS attribute_name FROM ";
                query += staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination") + " AS analyze_attribute_combination";
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute") + " AS attribute ON attribute.";
                query += staticDataBaseObject.quoteName("attribute_id") + " = analyze_attribute_combination." + staticDataBaseObject.quoteName("attribute_id");
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute_group") + " AS attribute_group ON(attribute_group.";
                query += staticDataBaseObject.quoteName("attribute_group_id") + " = attribute." + staticDataBaseObject.quoteName("attribute_group_id");
                query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute_lang") + " AS attribute_lang ON (attribute_lang.";
                query += staticDataBaseObject.quoteName("attribute_id") + " = attribute_lang." + staticDataBaseObject.quoteName("attribute_id");
                query += " AND attribute_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_attribute_group_lang") + " AS attribute_group_lang ON (attribute_group.";
                query += staticDataBaseObject.quoteName("attribute_group_id") + " = attribute_group_lang." + staticDataBaseObject.quoteName("attribute_group_id");
                query += " AND attribute_group_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE analyze_attribute_combination.";
                query += staticDataBaseObject.quoteName("analyze_attribute_id") + " IN (" + analyzeAttributeImplodeList + ") ORDER BY attribute_group.";
                query += staticDataBaseObject.quoteName("position") + " ASC, attribute." + staticDataBaseObject.quoteName("position") + " ASC ";

                staticDataBaseObject.setQuery(query);
                ResultSet attributeListSet = staticDataBaseObject.loadObjectList();

                if (attributeListSet != null) {
                    try {
                        while (attributeListSet.next()) {
                            /*JeproLabCartModel._attributesLists.put();
                            self::$_attributesLists[$row['id_product_attribute']. '-'.$id_lang]['attributes'].=
                            $row['public_group_name']. ' : '.$row['attribute_name']. ', ';
                            self::$_attributesLists[$row['id_product_attribute']. '-'.$id_lang]['attributes_small'].=
                            $row['attribute_name']. ', '; */
                        }
                    } catch (SQLException ignored) {
                        ignored.printStackTrace();
                    }
                }


        /*foreach ($pa_implode as $id_product_attribute) {
        self::$_attributesLists[$id_product_attribute.'-'.$id_lang]['attributes'] = rtrim(
                self::$_attributesLists[$id_product_attribute.'-'.$id_lang]['attributes'],
        ', '
        );

        self::$_attributesLists[$id_product_attribute.'-'.$id_lang]['attributes_small'] = rtrim(
                self::$_attributesLists[$id_product_attribute.'-'.$id_lang]['attributes_small'],
        ', '
        );
    } */
            }
        }
    }

    public static int getNumberOfAnalyzes(int cartId){
        if(JeproLabCartModel._number_of_analyzes.containsKey(cartId)){
            return JeproLabCartModel._number_of_analyzes.get(cartId);
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT SUM(" + staticDataBaseObject.quoteName("quantity") + ") AS qty FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
        query += " WHERE " + staticDataBaseObject.quoteName("cart_id") + " = " + cartId;

        staticDataBaseObject.setQuery(query);
        int analyzes = (int)staticDataBaseObject.loadValue("qty");
        JeproLabCartModel._number_of_analyzes.put(cartId, analyzes);
        return analyzes;
    }

    public boolean addCartRule(int cartRuleId){
        // You can't add a cart rule that does not exist
        JeproLabCartRuleModel cartRule = new JeproLabCartRuleModel(cartRuleId, JeproLabContext.getContext().language.language_id);

        if (cartRule == null || cartRule.cart_rule_id <= 0) {
            return false;
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT cart_rule_id FROM " + dataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " WHERE ";
        query += dataBaseObject.quoteName("cart_rule_id") + " = " + cartRuleId + " AND " + dataBaseObject.quoteName("cart_id");
        query += " = " + this.cart_id;

        dataBaseObject.setQuery(query);

        if(dataBaseObject.loadValue("cart_rule_id") <= 0){
            return false;
        }

        // Add the cart rule to the cart
        query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + "(" + dataBaseObject.quoteName("cart_id");
        query += ", " + dataBaseObject.quoteName("cart_rule_id") + ") VALUES (" + this.cart_id + ", " + cartRuleId + ")";

        dataBaseObject.setQuery(query);

        if (!dataBaseObject.query(false)) {
            return false;
        }

        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_ALL);
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_SHIPPING);
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_REDUCTION);
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_GIFT);

        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_ALL + "_ids");
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_SHIPPING + "_ids");
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_REDUCTION + "_ids");
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_request_cart_rules_ids_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_GIFT + "_ids");

        if (cartRule.gift_analyze > 0) {
            this.updateQuantity(1, cartRule.gift_analyze, cartRule.gift_analyze_attribute, false, "up", 0, null, false);
        }

        return true;
    }

    //public boolean updateQty(int quantity, int analyzeId, int analyzeAttributeId = null, int customizationId = false, String operator = 'up', int deliveryAddressId = 0, JeproLabLaboratoryModel lab = null, boolean autoAddCartRule = true){

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

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator, int deliveryAddressId){
        return updateQuantity(quantity, analyzeId, analyzeAttributeId, customizationId, operator, deliveryAddressId, null, true);
    }

    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator, int deliveryAddressId, JeproLabLaboratoryModel lab){
        return updateQuantity(quantity, analyzeId, analyzeAttributeId, customizationId, operator, deliveryAddressId, lab, true);
    }

    /**
     * Update product quantity
     *
     * @param quantity Quantity to add (or subtract)
     * @param analyzeId Product ID
     * @param analyzeAttributeId Attribute ID if needed
     * @param operator Indicate if quantity must be increased or decreased
     */
    public boolean updateQuantity(int quantity, int analyzeId, int analyzeAttributeId, int customizationId, String operator, int deliveryAddressId, JeproLabLaboratoryModel lab, boolean autoAddCartRule){
        if (lab == null) {
            lab = JeproLabContext.getContext().laboratory;
        }

        if (JeproLabContext.getContext().customer.customer_id > 0) {
            if (deliveryAddressId == 0 && this.delivery_address_id > 0){ // The $id_address_delivery is null, use the cart delivery address
                deliveryAddressId = this.delivery_address_id;
            }else if(deliveryAddressId == 0) {
                // The $id_address_delivery is null, get the default customer address
                deliveryAddressId = JeproLabAddressModel.getCustomerFirstAddressId(JeproLabContext.getContext().customer.customer_id);
            }else if(!JeproLabCustomerModel.customerHasAddress(JeproLabContext.getContext().customer.customer_id, deliveryAddressId)) {
                // The $id_address_delivery must be linked with customer
                deliveryAddressId = 0;
            }
        }

        JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId, false, JeproLabSettingModel.getIntValue("default_lang"), lab.laboratory_id);

        if (analyzeAttributeId > 0) {
            JeproLabCombinationModel combination = new JeproLabCombinationModel(analyzeAttributeId);
            if(combination.analyze_id != analyzeId) {
                return false;
            }
        }

        /* If we have a product combination, the minimal quantity is set with the one of this combination */
        int minimalQuantity;
        if (analyzeAttributeId > 0) {
            minimalQuantity = JeproLabAnalyzeModel.getAttributeMinimalQuantity(analyzeAttributeId);
        } else {
            minimalQuantity = analyze.minimal_quantity;
        }

        if (analyze.analyze_id <= 0) {
            die(Tools::displayError ());
        }

        if (JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)){
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)){
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }
        int newQty;
        String qty;
/*
        Hook::exec('actionBeforeCartUpdateQty', array(
            'cart' => $this,
            'product' => $product,
            'id_product_attribute' => $id_product_attribute,
            'id_customization' => $id_customization,
            'quantity' => $quantity,
            'operator' => $operator,
            'id_address_delivery' => $id_address_delivery,
            'shop' => $shop,
            'auto_add_cart_rule' => $auto_add_cart_rule,
        ));
*/
        if (quantity <= 0) {
            return this.deleteAnalyze(analyzeId, analyzeAttributeId, customizationId);
        } else if(!analyze.available_for_order || (JeproLabSettingModel.getIntValue("catalog_mode") && !defined('_PS_ADMIN_DIR_'))) {
            return false;
        } else {
            /* Check if the product is already in the cart */
            boolean result = this.containsAnalyze(analyzeId, analyzeAttributeId, customizationId, deliveryAddressId);

            /* Update quantity if product already exist */
            if (result) {
                if (operator.equals("up")) {
                    if (dataBaseObject == null) {
                        dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query = "SELECT stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity FROM " + dataBaseObject.quoteName("__jeprolab_analyze");
                    query += " AS analyze " + JeproLabAnalyzeModel.queryStock("analyze", analyzeAttributeId, true, lab) + " WHERE analyze.";
                    query += dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

                    dataBaseObject.setQuery(query);
                    ResultSet resultSet = dataBaseObject.loadObjectList();

                    if (resultSet != null) {
                        try {
                            while (resultSet.next()) {
                                int analyzeQuantity = resultSet.getInt("quantity");
                                // Quantity for product pack
                                if (JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId)) {
                                    analyzeQuantity = JeproLabAnalyzeModel.JeproLabAnalyzePackModel.getQuantity(analyzeId, analyzeAttributeId);
                                }

                                newQty = analyzeQuantity + quantity;
                                qty = "+ " + quantity;

                                if (!JeproLabAnalyzeModel.isAvailableWhenOutOfStock(resultSet.getInt("out_of_stock"))) {
                                    if (newQty > analyzeQuantity) {
                                        return false;
                                    }
                                }
                            }
                        } catch (SQLException ignored) {
                            ignored.printStackTrace();
                        } finally {
                            try {
                                JeproLabDataBaseConnector.getInstance().closeConnexion();
                            } catch (Exception ignored) {
                                ignored.printStackTrace();
                            }
                        }
                    }
                } else if (operator.equals("down")) {
                    qty = "- " + quantity;
                    newQty = (int) $result['quantity'] - quantity;
                    if (newQty < minimalQuantity && minimalQuantity > 1) {
                        return -1;
                    }
                } else {
                    return false;
                }

                /* Delete product from cart */
                if (newQty <= 0) {
                    return this.deleteAnalyze(analyzeId, analyzeAttributeId, customizationId);
                }else if(newQty < minimalQuantity) {
                    return -1;
                }else{
                    String query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " SET " + dataBaseObject.quoteName("quantity");
                    query += " = " + dataBaseObject.quoteName("quantity") + qty + ", "  + dataBaseObject.quoteName("date_add") + " = NOW() WHERE ";
                    query += dataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND " + dataBaseObject.quoteName("cart_id") + " = " + this.cart_id.;
                    query += ((analyzeAttributeId > 0) ? " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                    query += (JeproLabSettingModel.getIntValue("allow_multi_lab") > 0 && this.isMultiAddressDelivery() ?
                            " AND " + dataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId : "" ) + " LIMIT 1";

                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);
                }
            } else if (operator.equals("up")) {
            /* Add product to the cart */
                String query = "SELECT stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity FROM " + dataBaseObject.quoteName("#__jeprolab_analyze");
                query += " AS analyze " + JeproLabAnalyzeModel.queryStock("analyze", analyzeAttributeId, true, lab) + " WHERE analyze.";
                query += dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

                dataBaseObject.setQuery(query);
                ResultSet resultSet = dataBaseObject.loadObjectList();

                if (resultSet != null) {
                    try {
                        if (resultSet.next()) {
                            // Quantity for product pack
                            if (JeproLabAnalyzeModel.JeproLabAnalyzePackModel.isPack(analyzeId)) {
                                $result2['quantity'] = JeproLabAnalyzeModel.JeproLabAnalyzePackModel.getQuantity(analyzeId, analyzeAttributeId);
                            }

                            if (!JeproLabAnalyzeModel.isAvailableWhenOutOfStock(resultSet.getInt("out_of_stock"))) {
                                if (quantity > resultSet.getInt("quantity")) {
                                    return false;
                                }
                            }

                            if (quantity < minimalQuantity) {
                                return -1;
                            }

                            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + "( " + dataBaseObject.quoteName("analyze_id");
                            query += ", " + dataBaseObject.quoteName("analyze_attribute_id") + ", " + dataBaseObject.quoteName("cart_id") + ", " ;
                            query += ", " + dataBaseObject.quoteName("delivery_address_id") + ", " + dataBaseObject.quoteName("lab_id") + ", ";
                            query +=  dataBaseObject.quoteName("quantity") + ", " + dataBaseObject.quoteName("date_add") + ") VALUES (" + analyzeId;
                            query += ", " + analyzeAttributeId + ", " + this.cart_id + ", " + deliveryAddressId + ", " + lab.laboratory_id ;
                            query += ", " + quantity  + ", " + dataBaseObject.quote(JeproLabTools.date("Y-m-d H:i:s")) + ") ";

                            dataBaseObject.setQuery(query);
                            boolean resultAdd = dataBaseObject.query(false);
                            if (!resultAdd) {
                                return false;
                            }
                        }
                    } catch (SQLException ignored) {
                        ignored.printStackTrace();
                    } finally {
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }
            }
        }

        // refresh cache of self::_products
        this._analyzes = this.getAnalyzes(true);
        this.update();
        JeproLabContext context = null;
        try {
            context = JeproLabContext.getContext().cloneContext();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        context.cart = this;
        JeproLabCache.getInstance().remove("get_contextual_value_*");
        if (autoAddCartRule) {
            JeproLabCartRuleModel.autoAddToCart(context);
        }

        if (analyze.customizable) {
            return this.updateCustomizationQuantity(quantity, customizationId, analyzeId, analyzeAttributeId, deliveryAddressId, operator);
        } else {
            return true;
        }
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(){
        return getAnalyzes(false, 0, 0);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(boolean refresh){
        return getAnalyzes(refresh, 0, 0);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(boolean refresh, int analyzeId){
        return getAnalyzes(refresh, analyzeId, 0);
    }

    /**
     * Return cart products
     *
     * @return List<JeproLabAnalyzeModel>
     */
    public List<JeproLabAnalyzeModel> getAnalyzes(boolean refresh, int analyzeId, int countryId){
        if (!(this.cart_id > 0)){
            return new ArrayList<>();
        }
        List<JeproLabAnalyzeModel> analyzes = new ArrayList<>();
        // Analyze cache must be strictly compared to NULL, or else an empty cart will add dozens of queries
        if ((this._analyzes != null) && !refresh) {
            // Return product row with specified ID if it exists
            if (analyzeId > 0) {
                for (JeproLabAnalyzeModel analyze : this._analyzes) {
                    if (analyze.analyze_id == analyzeId) {
                        analyzes.add(analyze);
                        return analyzes;
                    }
                }
                return new ArrayList<>();
            }
            return this._analyzes;
        }

        // Build query
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        // Build SELECT
        String query = "SELECT cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id") + ", cart_analyze.";
        query += dataBaseObject.quoteName("analyze_id") + ", cart_analyze." + dataBaseObject.quoteName("quantity");
        query += " AS cart_quantity, cart_analyze." + dataBaseObject.quoteName("lab_id") + ", analyze_lang.";
        query += dataBaseObject.quoteName("name") + ", analyze." + dataBaseObject.quoteName("is_virtual") + ", analyze_lang.";
        query += dataBaseObject.quoteName("short_description") + ", analyze_lang." + dataBaseObject.quoteName("available_now");
        query += ", analyze_lang." + dataBaseObject.quoteName("available_later") + ", analyze_lab." + dataBaseObject.quoteName("default_category_id");
        query += ", analyze." + dataBaseObject.quoteName("supplier_id") + ", analyze." + dataBaseObject.quoteName("manufacturer_id");
        query += ", analyze_lab." + dataBaseObject.quoteName("on_sale") + ", analyze_lab." + dataBaseObject.quoteName("ecotax");
        query += ", analyze_lab." + dataBaseObject.quoteName("additional_shipping_cost") + ", analyze_lab." + dataBaseObject.quoteName("available_for_request");
        query += ", analyze_lab." + dataBaseObject.quoteName("price") + ", analyze_lab." + dataBaseObject.quoteName("published") + ", analyze_lab." ;
        query += dataBaseObject.quoteName("unity") + ", analyze_lab." + dataBaseObject.quoteName("unit_price_ratio") + ", stock.";
        query += dataBaseObject.quoteName("quantity") + " AS quantity_available, analyze." + dataBaseObject.quoteName("width") +  ", analyze.";
        query += dataBaseObject.quoteName("height") + ", analyze." + dataBaseObject.quoteName("depth") + ", stock." + dataBaseObject.quoteName("out_of_stock");
        query += ", analyze." + dataBaseObject.quoteName("weight") + ", analyze." + dataBaseObject.quoteName("date_add") + ", analyze.";
        query += dataBaseObject.quoteName("date_upd") + ", IFNULL(stock.quantity, 0) as quantity, analyze_lang." + dataBaseObject.quoteName("link_rewrite");
        query += ", category_lang." + dataBaseObject.quoteName("link_rewrite") + " AS category, CONCAT(LPAD(cart_analyze." + dataBaseObject.quoteName("analyze_id");
        query += ", 10, 0), LPAD(IFNULL(cart_analyze." + dataBaseObject.quoteName("attribute_analyze_id") + ", 0), 10, 0), IFNULL(cart_analyze.";
        query += dataBaseObject.quoteName("delivery_address_id") + ", 0)) AS unique_id, cart_analyze." + dataBaseObject.quoteName("delivery_address_id");
        query += ", analyze_lab.advanced_stock_management, analyze_supplier.product_supplier_reference supplier_reference, image_lab.";
        query += dataBaseObject.quoteName("image_id") + ", image_lang." + dataBaseObject.quoteName("legend");
        if(JeproLabCombinationModel.JeproLabCustomizationModel.isFeaturePublished()){
            query += ", customization." + dataBaseObject.quoteName("customization_id") + ", customization." + dataBaseObject.quoteName("quantity") ;
            query += " AS customization_quantity ";
        }else{
            query += " NULL AS customization_quantity, 0 AS customization_id";
        }

        if(JeproLabCombinationModel.isFeaturePublished()){
            query += ", analyze_attribute_lab." + dataBaseObject.quoteName("price") + " AS price_attribute, analyze_attribute_lab.";
            query += dataBaseObject.quoteName("ecotax") + " AS eco_tax_attr, IF (IFNULL(analyze_attribute." + dataBaseObject.quoteName("reference");
            query += ", '') = '', analyze." + dataBaseObject.quoteName("reference") + ", analyze_attribute." + dataBaseObject.quoteName("reference") ;
            query += ") AS reference, (analyze." + dataBaseObject.quoteName("weight") + "+ analyze_attribute." + dataBaseObject.quoteName("weight");
            query += " AS weight_attribute, IF (IFNULL(analyze_attribute." + dataBaseObject.quoteName("ean13") + ", '') = '', analyze.";
            query += dataBaseObject.quoteName("ean13") + ", analyze_attribute." + dataBaseObject.quoteName("ean13") + " AS ean13, IF (IFNULL(analyze_attribute.";
            query += dataBaseObject.quoteName("upc") + ", '') = '', analyze." + dataBaseObject.quoteName("upc") + ", analyze_attribute.";
            query += dataBaseObject.quoteName("upc") + ") AS upc, IFNULL(analyze_attribute_lab." + dataBaseObject.quoteName("minimal_quantity");
            query += ", analyze_lab." + dataBaseObject.quoteName("minimal_quantity") + ") AS minimal_quantity, IF(analyze_attribute_lab.wholesale_price > 0, ";
            query += " analyze_attribute_lab.wholesale_price, analyze_lab." + dataBaseObject.quoteName("wholesale_price") + ")  wholesale_price ";
        }else{
            query += "analyze." + dataBaseObject.quoteName("reference") + " AS reference, analyze." + dataBaseObject.quoteName("ean13");
            query += ", analyze." + dataBaseObject.quoteName("upc") + " AS upc, analyze_lab." + dataBaseObject.quoteName("minimal_quantity") ;
            query += " AS minimal_quantity, analyze_lab." + dataBaseObject.quoteName("wholesale_price") + " AS wholesale_price ";
        }
        //query += ;

        // Build FROM
        query += " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS cart_analyze LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze");
        query += " AS analyze ON (analyze." + dataBaseObject.quoteName("analyze_id") + " = cart_analyze." + dataBaseObject.quoteName("analyze_id");
        query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyze_lab." + dataBaseObject.quoteName("lab_id");
        query += " = cart_analyze." + dataBaseObject.quoteName("lab_id") + " AND analyze_lab." + dataBaseObject.quoteName("analyze_id");
        query += " analyze." + dataBaseObject.quoteName("analyze_id") + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ";
        query += " ON (analyze_lang." + dataBaseObject.quoteName("analyze_id") + " = analyze." + dataBaseObject.quoteName("analyze_id") + " AND analyze_lang.";
        query += dataBaseObject.quoteName("lang_id") + " = " + this.language_id + JeproLabLaboratoryModel.addSqlRestriction("analyze_lang", "ON (cart_analyze." + dataBaseObject.quoteName("analyze_id"));
        query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (analyze_lab." + dataBaseObject.quoteName("default_category_id");
        query += " = category_lang." + dataBaseObject.quoteName("category_id") + " AND category_lang." + dataBaseObject.quoteName("lang_id") + " = ";
        query += this.language_id + JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang", " ON (cart_analyze." + dataBaseObject.quoteName("lab_id") + ") ");
        query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_supplier") + " AS analyze_supplier ON (analyze_supplier." + dataBaseObject.quoteName("analyze_id");
        query += " = cart_analyze." + dataBaseObject.quoteName("analyze_id") + " AND analyze_supplier." + dataBaseObject.quoteName("analyze_id");
        query += " AND analyze_supplier." + dataBaseObject.quoteName("analyze_attribute_id") + " = cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id");
        query += " AND analyze_supplier." + dataBaseObject.quoteName("supplier_id") + " = analyze." + dataBaseObject.quoteName("supplier_id");
        query += " LEFT JOIN " + JeproLabAnalyzeModel.queryStock("cart_analyze") + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_image_lab");
        query += " AS image_lab ON (image_lab." + dataBaseObject.quoteName("analyze_id") + " = analyze." + dataBaseObject.quoteName("analyze_id") + " AND image_lab.";
        query += dataBaseObject.quoteName("cover") + " = 1 AND image_lab." + dataBaseObject.quoteName("lab_id") + " = " + this.laboratory_id + ") LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_image_lang") + " AS image_lang ON (image_lang." + dataBaseObject.quoteName("image_id") + " = image_lab.";
        query += dataBaseObject.quoteName("image_id") + " AND image_lang." + dataBaseObject.quoteName("lang_id") + " = " + this.language_id + ") ";
        if(JeproLabCombinationModel.JeproLabCustomizationModel.isFeaturePublished()){
            query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_customization") + " AS customization ON (analyze." + dataBaseObject.quoteName("analyze_id");
            query += " = customization." + dataBaseObject.quoteName("analyze_id") + " AND cart_product." + dataBaseObject.quoteName("analyze_attribute_id");
            query += " = customization." + dataBaseObject.quoteName("analyze_attribute_id") + " AND customization." + dataBaseObject.quoteName("cart_id") + " = ";
            query += this.cart_id;
        }


        if(JeproLabCombinationModel.isFeaturePublished()){
            query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute ON (analyze_attribute.";
            query += dataBaseObject.quoteName("analyze_attribute_id") + " = cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id");
            query += " ) LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lab") + " AS analyze_attribute_lab ON( analyze_attribute_lab.";
            query += dataBaseObject.quoteName("lab_id") + " = cart_analyze." + dataBaseObject.quoteName("lab_id") + " AND analyze_attribute_lab.";
            query += dataBaseObject.quoteName("analyze_attribute_id") + " = analyze_attribute." + dataBaseObject.quoteName("analyze_attribute_id") + ")";
        }


        /** where clause **/
        query += " WHERE cart_analyze." + dataBaseObject.quoteName("cart_id")  + " = " + this.cart_id;
        if(analyzeId > 0) {
            query += " AND cart_analyze." + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
        }

        query += " AND analyze." + dataBaseObject.quoteName("analyze_id") + " IS NOT NULL ";

        query += " ORDER BY cart_analyze." + dataBaseObject.quoteName("date_add");
        query += " AND cart_analyze." + dataBaseObject.quoteName("analyze_id") + " AND cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id") + " ASC ";

        if(JeproLabCombinationModel.JeproLabCustomizationModel.isFeaturePublished()){
            query += " GROUP BY cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id") + ", cart_analyze." + dataBaseObject.quoteName("analyze_id");
            query += ", cart_analyze." + dataBaseObject.quoteName("lab_id");
        }

        dataBaseObject.setQuery(query);
        ResultSet cartAnalyzeSet = dataBaseObject.loadObjectList();

        // Reset the cache before the following return, or else an empty cart will add dozens of queries
        List<Integer> analyzeIds = new ArrayList<>();
        List<Integer> analyzeAttributeIds = new ArrayList<>();
        if(cartAnalyzeSet != null) {
            try {
                JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice;
                while (cartAnalyzeSet.next()){
                    analyzeIds.add(cartAnalyzeSet.getInt("analyze_id"));
                    analyzeAttributeIds.add(cartAnalyzeSet.getInt("analyze_attribute_id"));

                    specificPrice = JeproLabPriceModel.JeproLabSpecificPriceModel.getSpecificPrice(
                            cartAnalyzeSet.getInt("analyze_id"), this.laboratory_id, this.currency_id,
                            countryId, this.laboratory_group_id, cartAnalyzeSet.getInt("cart_quantity"),
                            cartAnalyzeSet.getInt("analyze_attribute_id"), this.customer_id, this.cart_id);
                    /*Map<Stri>
                    if(specificPrice != null) {
                        $reductionTypeRow = array('reduction_type' = > specificPrice.reduction_type);
                    } else {
                        $reduction_type_row = array('reduction_type' = > 0);
                    }

                    $result[$key] = array_merge($row, $reduction_type_row); */
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }
        }
        // Thus you can avoid one query per product, because there will be only one query for all the products of the cart
        JeproLabAnalyzeModel.cacheAnalyzesFeatures(analyzeIds);
        JeproLabCartModel.cacheSomeAttributesLists(analyzeAttributeIds, this.language_id);

        this._analyzes = new ArrayList<>();
        /*if (empty($result)) {
            return array();
        }*/
        int taxAddressId = JeproLabSettingModel.getStringValue("delivery_address").equals("delivery_address") ? this.delivery_address_id : this.invoice_address_id;
        float ecoTaxRate = JeproLabTaxModel.getAnalyzeEcoTaxRate(taxAddressId);
        boolean applyEcoTax = JeproLabAnalyzeModel._taxCalculationMethod == JeproLabConfigurationSettings.JEPROLAB_TAX_INCLUDED && (JeproLabSettingModel.getIntValue("use_tax") > 0);
        JeproLabContext cartLabContext;
        try{
            cartLabContext = JeproLabContext.getContext().cloneContext();
        }catch (CloneNotSupportedException ignored){
            ignored.printStackTrace();
        }

        if(cartAnalyzeSet != null) {
            try {
                int cartQuantity;
                JeproLabAnalyzeModel analyze;
                while (cartAnalyzeSet.next()) {
                    analyze = new JeproLabAnalyzeModel();
                    //if (isset($row['ecotax_attr']) && $row['ecotax_attr'] > 0) {
                        analyze.eco_tax = cartAnalyzeSet.getFloat("eco_tax_attr");
                    //}

                    analyze.stock_quantity = cartAnalyzeSet.getInt("quantity");
                    analyze.quantity = cartAnalyzeSet.getInt("cart_quantity");

                    /*if (isset($row['id_product_attribute']) && (int) $row['id_product_attribute'] && isset($row['weight_attribute'])) {
                        $row['weight'] = (float) $row['weight_attribute'];
                    } */
                    int addressId = JeproLabSettingModel.getStringValue("tax_address_type").equals("invoice_address_id") ? this.invoice_address_id : this.delivery_address_id;


                    if (!JeproLabAddressModel.addressExists(addressId)) {
                        addressId = 0;
                    }

                    if (cartLabContext.laboratory.laboratory_id != analyze.laboratory_id) {
                        cartLabContext.laboratory = new JeproLabLaboratoryModel(analyze.laboratory_id);
                    }

                    JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId, true);
                    int taxRulesGroupId = JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(cartAnalyzeSet.getInt("analyze_id"), cartLabContext);
                    JeproLabTaxModel.JeproLabTaxCalculator taxCalculator = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, taxRulesGroupId).getTaxCalculator();

                    cartQuantity = cartAnalyzeSet.getInt("cart_quantity");

                    analyze.analyze_price.price_without_reduction = JeproLabAnalyzeModel.getStaticPrice(
                            cartAnalyzeSet.getInt("analyze_id"), true,
                            ((cartAnalyzeSet.getInt("analyze_attribute_id") > 0) ? cartAnalyzeSet.getInt("analyze_attribute_id") : 0),
                            6, null, false, false, cartQuantity, false, (this.customer_id > 0 ? this.customer_id : 0),
                            this.cart_id, addressId, $specific_price_output, true, true, cartLabContext
                    );

                    analyze.analyze_price.price_with_reduction = JeproLabAnalyzeModel.getStaticPrice(
                            cartAnalyzeSet.getInt("analyze_id"), true,
                            ((cartAnalyzeSet.getInt("analyze_attribute_id") > 0) ? cartAnalyzeSet.getInt("analyze_attribute_id") : 0),
                            6, null, false, true, cartQuantity, false, (this.customer_id > 0 ? this.customer_id : 0),
                            this.cart_id, addressId, $specific_price_output, true, true, cartLabContext
                    );

                    analyze.analyze_price.price = analyze.analyze_price.price_with_reduction_without_tax = JeproLabAnalyzeModel.getStaticPrice(
                            cartAnalyzeSet.getInt("analyze_id"), false,
                            ((cartAnalyzeSet.getInt("analyze_attribute_id") > 0) ? cartAnalyzeSet.getInt("analyze_attribute_id") : 0),
                            6, null, false, true, cartQuantity, false, (this.customer_id > 0 ? this.customer_id : 0),
                            (int) this.cart_id, addressId, specific_price_output, true, true, cartLabContext
                    );

                    float priceWithReductionWithOutTax = cartAnalyzeSet.getFloat("price_with_reduction_without_tax");
                    float priceWithReduction = cartAnalyzeSet.getFloat("price_with_reduction");

                    switch (JeproLabSettingModel.getIntValue("round_type")) {
                        case JeproLabRequestModel.ROUND_TOTAL:
                            analyze.analyze_price.total = priceWithReductionWithOutTax * cartQuantity;
                            analyze.analyze_price.total_with_tax = priceWithReduction * cartQuantity;
                            break;
                        case JeproLabRequestModel.ROUND_LINE:
                            analyze.analyze_price.total = JeproLabTools.roundPrice(priceWithReductionWithOutTax * cartQuantity, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                            analyze.analyze_price.total_with_tax = JeproLabTools.roundPrice(priceWithReduction * cartQuantity, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                            break;

                        case JeproLabRequestModel.ROUND_ITEM:
                        default:
                            analyze.analyze_price.total = JeproLabTools.roundPrice(priceWithReductionWithOutTax, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION) * cartQuantity;
                            analyze.analyze_price.total_with_tax = JeproLabTools.roundPrice(priceWithReduction, JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION) * cartQuantity;
                            break;
                    }

                    analyze.analyze_price.price_with_out_reductiongtrf = priceWithReduction;
                    analyze.short_description = Tools::nl2br ($row['description_short']);

                    // check if a image associated with the attribute exists
                    if (cartAnalyzeSet.getInt("analyze_attribute_id") > 0) {
                        Map<String, String> rowData = JeproLabImageModel.getBestImageAttribute(analyze.laboratory_id, this.language_id, cartAnalyzeSet.getInt("analyze_id"), cartAnalyzeSet.getInt("analyze_attribute_id"));
                        if (!rowData.isEmpty()) {
                            if(rowData.containsKey("image_id")){
                                analyze.image_name = rowData.get("image_id");
                            }
                            //analyze = array_merge($row, $row2);
                        }
                    }

                    analyze.reduction_applies = ($specific_price_output && (float) $specific_price_output['reduction']);
                    analyze.quantity_discount_applies = ($specific_price_output && $row['cart_quantity'] >= (int) $specific_price_output['from_quantity']);
                    analyze.image_name = JeproLabAnalyzeModel.defineAnalyzeImage(analyze, this.language_id);
                    analyze.allow_out_of_stock_request = JeproLabAnalyzeModel.isAvailableWhenOutOfStock(cartAnalyzeSet.getInt("out_of_stock"));
                    analyze.features = JeproLabAnalyzeModel.getStaticFeatures(cartAnalyzeSet.getInt("analyze_id"));

                    if (JeproLabCartModel._attributesLists.containsKey(cartAnalyzeSet.getInt("analyze_attribute_id") + "_" + this.language_id)) {
                        //$row = array_merge($row, self::$_attributesLists[$row['id_product_attribute']. '-'. this.language_id]);
                    }

                    analyze = JeproLabAnalyzeModel.getTaxesInformation(analyze, cartLabContext);

                    this._analyzes.add(analyze);
                }
            } catch (SQLException ignored) {
                ignored.printStackTrace();
            }
        }
        return this._analyzes;
    }
/* /
    public static function replaceZeroByLaboratoryName($echo, $tr){
        return ($echo == '0' ? JeproLabCarrierModel.getCarrierNameFromLaboratoryName() : $echo);
    } */

    public boolean deleteAnalyze(int analyzeId){
        return deleteAnalyze(analyzeId, 0, 0, 0);
    }
    public boolean deleteAnalyze(int analyzeId, int analyzeAttributeId){
        return deleteAnalyze(analyzeId, analyzeAttributeId, 0, 0);
    }

    public boolean deleteAnalyze(int analyzeId, int analyzeAttributeId, int customizationId){
        return deleteAnalyze(analyzeId, analyzeAttributeId, customizationId, 0);
    }

    /**
     * Delete a analyze from the cart
     *
     * @param analyzeId Product ID
     * @param analyzeAttributeId Attribute ID if needed
     * @param customizationId Customization id
     * @return bool result
     */
    public boolean deleteAnalyze(int analyzeId, int analyzeAttributeId, int customizationId, int deliveryAddressId){
        if (JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)){
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)){
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query;

        if (customizationId > 0){
            query = "SELECT " + dataBaseObject.quoteName("quantity") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze");
            query += " WHERE " + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND " + dataBaseObject.quoteName("cart_id");
            query += " = " + this.cart_id + " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;

            dataBaseObject.setQuery(query);
            int analyzeTotalQuantity = (int)dataBaseObject.loadValue("quantity");

            query = "SELECT " + dataBaseObject.quoteName("quantity") + " FROM " + dataBaseObject.quoteName("#__jeprolab_customization");
            query += " WHERE "  + dataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND " + dataBaseObject.quoteName("analyze_d");
            query += " = " + analyzeId + " AND " +  dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;
            query += (deliveryAddressId > 0 ? " AND " + dataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId : "");

            dataBaseObject.setQuery(query);
            int customizationQuantity = (int)dataBaseObject.loadValue("quantity");

            if (!this.deleteCustomization(customizationId, analyzeId, analyzeAttributeId, deliveryAddressId)){
                return false;
            }

            // refresh cache of self::_products
            this._analyzes = this.getAnalyzes(true);
            return (customizationQuantity == analyzeTotalQuantity && this.deleteAnalyze(analyzeId, analyzeAttributeId, 0, deliveryAddressId);
        }

        /* Get customization quantity */
        query = "SELECT SUM(" + dataBaseObject.quoteName("quantity") + ") AS quantity FROM " + dataBaseObject.quoteName("#__jeprolab_customization");
        query += " WHERE " + dataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND " + dataBaseObject.quoteName("analyze_id") + " = ";
        query += analyzeId + " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;

        dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObjectList();

        if (resultSet == null){
            return false;
        }else{
            try{
                if(resultSet.next()){
                    /* If the product still possesses customization it does not have to be deleted */
                    if (Db::getInstance()->NumRows() && (int)$result['quantity']){
                        query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " SET " + dataBaseObject.quoteName("quantity`");
                        query += " = " + resultSet.getInt("quantity") + " WHERE " + dataBaseObject.quoteName("cart_id") + " = " + this.cart_id ;
                        query += " AND " + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
                        query += (analyzeAttributeId > 0  ? " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId :"");

                        dataBaseObject.setQuery(query);
                        return dataBaseObject.query(false);
                    }

                    /* Product deletion */
                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " WHERE " + dataBaseObject.quoteName("analyze_id");
                    query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                    query += " AND " + dataBaseObject.quoteName("cart_id") + " = " + this.cart_id;
                    query += (deliveryAddressId > 0 ? " AND " + dataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId : "");

                    dataBaseObject.setQuery(query);
                    boolean result = dataBaseObject.query(false);

                    if (result) {
                        result = this.update();
                        // refresh cache of self::_products
                        this._analyzes = this.getAnalyzes(true);
                        JeproLabCartRuleModel.autoRemoveFromCart();
                        JeproLabCartRuleModel.autoAddToCart();

                        return result;
                    }
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

        return false;
    }

    protected boolean deleteCustomization(int customizationId, int analyzeId, int analyzeAttributeId){
        return deleteCustomization(customizationId, analyzeId, analyzeAttributeId, 0);
    }

    /**
     * Delete a customization from the cart. If customization is a Picture,
     * then the image is also deleted
     *
     * @param customizationId
     * @return bool result
     */
    protected boolean deleteCustomization(int customizationId, int analyzeId, int analyzeAttributeId, int deliveryAddressId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        boolean result = true;

        String query = "SELECT * FROM "  + dataBaseObject.quoteName("#__jeprolab_customization") + " WHERE ";
        query  += dataBaseObject.quoteName("customization_id") + " = " + customizationId;

        dataBaseObject.setQuery(query);
        ResultSet customizationSet = dataBaseObject.loadObjectList();

        if (customizationSet != null) {
            try{
                if(customizationSet.next()){
                    query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_customized_data") + " WHERE ";
                    query += dataBaseObject.quoteName("customization_id") + " = " + customizationId;

                    dataBaseObject.setQuery(query);
                    ResultSet custData = dataBaseObject.loadObjectList();

                    // Delete customization picture if necessary
                    String type = custData.getString("type");
                    if(!type.equals("") && type.equals(String.valueOf(0))) {
                        result &= (@unlink(_PS_UPLOAD_DIR_.$cust_data['value']) && @unlink(_PS_UPLOAD_DIR_.$cust_data['value'].'_small'));
                    }

                    query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_customized_data") + " WHERE ";
                    query += dataBaseObject.quoteName("customization_id") + " = " + customizationId;

                    dataBaseObject.setQuery(query);
                    result &= dataBaseObject.query(false);

                    if (result){
                        query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " SET " + dataBaseObject.quoteName("quantity");
                        query += " = " + dataBaseObject.quoteName("quantity") + " - " + customizationSet.getInt("quantity") + " WHERE ";
                        query += dataBaseObject.quoteName("cart_id") + " = " + this.cart_id + " AND " + dataBaseObject.quoteName("analyze_id");
                        query += " = " + analyzeId + (analyzeAttributeId > 0 ? " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
                        query += " AND " + dataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId;

                        dataBaseObject.setQuery(query);
                        result &= dataBaseObject.query(false);
                    }

                    if (!result) {
                        return false;
                    }
                    query = "DELETE FROM "  + dataBaseObject.quoteName("#__jeprolab_customization") + " WHERE " ;
                    query += dataBaseObject.quoteName("customization_id") + " = " + customizationId;

                    dataBaseObject.setQuery(query);
                    return dataBaseObject.query(false);
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

        return true;
    }

    public boolean update(){
        if (JeproLabCartModel._number_of_analyzes.containsKey(this.cart_id)){
            JeproLabCartModel._number_of_analyzes.remove(this.cart_id);
        }

        if (JeproLabCartModel._total_weight.containsKey(this.cart_id)){
            JeproLabCartModel._total_weight.remove(this.cart_id);
        }

        this._analyzes = null;
        boolean result = parent::update($null_values);
        Hook::exec('actionCartSave');

        return result;
    }

    public boolean removeCartRule(int cartRuleId){
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_ALL);
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_SHIPPING);
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_REDUCTION);
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_GIFT);

        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_ALL + "_ids");
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_SHIPPING + "_ids");
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_REDUCTION + "_ids");
        JeproLabCache.getInstance().remove("jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + JeproLabCartRuleModel.FILTER_ACTION_GIFT + "_ids");

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " WHERE ";
        query += dataBaseObject.quoteName("cart_rule_id") + " = " + cartRuleId + " AND " + dataBaseObject.quoteName("cart_id");
        query += " = " + this.cart_id + " LIMIT 1";

        dataBaseObject.setQuery(query);
        boolean result = dataBaseObject.query(false);

        JeproLabCartRuleModel cartRule = new JeproLabCartRuleModel(cartRuleId, JeproLabSettingModel.getIntValue("default_lang"));
        if (cartRule.gift_analyze > 0) {
            this.updateQuantity(1, cartRule.gift_analyze, cartRule.gift_analyze_attribute, 0, "down", 0, null, false);
        }

        return result;
    }

    public List<JeproLabCartRuleModel> getCartRules(){
        return getCartRules(JeproLabCartRuleModel.FILTER_ACTION_ALL);
    }

    public List<JeproLabCartRuleModel> getCartRules(int filter){
        // If the cart has not been saved, then there can't be any cart rule applied
        if (!JeproLabCartRuleModel.isFeaturePublished() || this.cart_id <= 0){
            return new ArrayList<>();
        }

        String cacheKey = "jeprolab_cart_model_get_cart_rules_" + this.cart_id + "_" + filter;
        List<JeproLabCartRuleModel> cartRuleList = new ArrayList<>();
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if (dataBaseObject == null) {
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT cart_rule.*, cart_rule_lang." + dataBaseObject.quoteName("lang_id") + ", cart_rule_lang.";
            query += dataBaseObject.quoteName("name") + ", cart_cart_rule." + dataBaseObject.quoteName("cart_id") + " FROM ";
            query += dataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " AS cart_cart_rule LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule ON cart_cart_rule." + dataBaseObject.quoteName("cart_rule_id");
            query += " = cart_rule." + dataBaseObject.quoteName("cart_rule_id") + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_cart_rule_lang");
            query += " AS cart_rule_lang ON (cart_cart_rule." + dataBaseObject.quoteName("cart_rule_id") + " = cart_rule_lang.";
            query += dataBaseObject.quoteName("cart_rule_id") + " AND cart_rule_lang." + dataBaseObject.quoteName("lang_id") + " = " + this.language_id;
            query += ") WHERE " + dataBaseObject.quoteName("cart_id") + " = " + this.cart_id + (filter == JeproLabCartRuleModel.FILTER_ACTION_SHIPPING ? " AND free_shipping = 1" : "");
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_GIFT ? " AND gift_analyze != 0 " : "");
            query += (filter == JeproLabCartRuleModel.FILTER_ACTION_REDUCTION ? " AND (reduction_percent != 0 OR reduction_amount != 0)" : "");
            query += " ORDER by cart_rule.priority ASC";

            dataBaseObject.setQuery(query);
            ResultSet cartRuleSet = dataBaseObject.loadObjectList();


            if (cartRuleSet != null) {
                try {
                    JeproLabCartRuleModel cartRule;
                    while (cartRuleSet.next()) {
                        cartRule = new JeproLabCartRuleModel();
                    }
                } catch (SQLException ignored) {
                    ignored.printStackTrace();
                } finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
            JeproLabCache.getInstance().store(cacheKey, cartRuleList);
        } else {
            cartRuleList = (List<JeproLabCartRuleModel>) JeproLabCache.getInstance().retrieve(cacheKey);
        }

        // Define virtual context to prevent case where the cart is not the in the global context
        JeproLabContext virtualContext = null;
        try {
            virtualContext = JeproLabContext.getContext().cloneContext();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        virtualContext.cart = this;

        foreach ($result as &$row) {
            $row['obj'] = new CartRule($row['id_cart_rule'], (int) $this -> id_lang);
            $row['value_real'] = $row['obj']->getContextualValue(true, $virtual_context, $filter);
            $row['value_tax_exc'] = $row['obj']->getContextualValue(false, $virtual_context, $filter);
            // Retro compatibility < 1.5.0.2
            $row['id_discount'] = $row['id_cart_rule'];
            $row['description'] = $row['name'];
        }

        return cartRuleList;
    }

    public float getRequestTotal(){
        return getRequestTotal(true, JeproLabCartModel.BOTH, null, 0, true);
    }

    public float getRequestTotal(boolean withTaxes){
        return getRequestTotal(withTaxes, JeproLabCartModel.BOTH, null, 0, true);
    }

    public float getRequestTotal(boolean withTaxes, int type){
        return getRequestTotal(withTaxes, type, null, 0, true);
    }

    public float getRequestTotal(boolean withTaxes, int type, List<JeproLabAnalyzeModel> analyzes){
        return getRequestTotal(withTaxes, type, analyzes, 0, true);
    }

    public float getRequestTotal(boolean withTaxes, int type, List<JeproLabAnalyzeModel> analyzes, int carrierId){
        return getRequestTotal(withTaxes, type, analyzes, carrierId, true);
    }

    /**
     * This function returns the total cart amount
     *
     * Possible values for $type:
     * Cart::ONLY_PRODUCTS
     * Cart::ONLY_DISCOUNTS
     * Cart::BOTH
     * Cart::BOTH_WITHOUT_SHIPPING
     * Cart::ONLY_SHIPPING
     * Cart::ONLY_WRAPPING
     * Cart::ONLY_PRODUCTS_WITHOUT_SHIPPING
     * Cart::ONLY_PHYSICAL_PRODUCTS_WITHOUT_SHIPPING
     *
     * @param withTaxes With or without taxes
     * @param type Total type
     * @param useCache Allow using cache of the method CartRule::getContextualValue
     * @return float Order total
     */
    public float getRequestTotal(boolean withTaxes, int type, List<JeproLabAnalyzeModel>  analyzes, int carrierId, boolean useCache){
        // Dependencies
        $address_factory    = Adapter_ServiceLocator::get('Adapter_AddressFactory');
        $price_calculator    = Adapter_ServiceLocator::get('Adapter_ProductPriceCalculator');
        $configuration        = Adapter_ServiceLocator::get('Core_Business_ConfigurationInterface');

        String taxAddressType = JeproLabSettingModel.getStringValue("tax_address_type");
        boolean useEcoTax = JeproLabSettingModel.getIntValue("use_ecotax") > 0;
        int roundType = JeproLabSettingModel.getIntValue("PS_ROUND_TYPE");
        int ecoTaxTaxRulesGroupId = JeproLabSettingModel.getIntValue("PS_ECOTAX_TAX_RULES_GROUP_ID");
        int computePrecision = JeproLabSettingModel.getIntValue("PS_PRICE_COMPUTE_PRECISION_");

        if (this.cart_id < 0) {
            return 0;
        }

        List<Integer> cartTypes = new ArrayList<>();
        cartTypes.add(JeproLabCartModel.ONLY_ANALYZES);
        cartTypes.add(JeproLabCartModel.ONLY_DISCOUNTS);
        cartTypes.add(JeproLabCartModel.BOTH);
        cartTypes.add(JeproLabCartModel.BOTH_WITHOUT_SHIPPING);
        cartTypes.add(JeproLabCartModel.ONLY_SHIPPING);
        cartTypes.add(JeproLabCartModel.ONLY_WRAPPING);
        cartTypes.add(JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING);
        cartTypes.add(JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING);

        // Define virtual context to prevent case where the cart is not the in the global context
        JeproLabContext virtualContext = null;
        try {
            virtualContext = JeproLabContext.getContext().cloneContext();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        virtualContext.cart = this;

        if (!cartTypes.contains(type)){
            die(Tools::displayError());
        }

        boolean withShipping = type == JeproLabCartModel.BOTH || type == JeproLabCartModel.ONLY_SHIPPING;

        // if cart rules are not used
        if (type == JeproLabCartModel.ONLY_DISCOUNTS && !JeproLabCartRuleModel.isFeaturePublished()) {
            return 0;
        }

        // no shipping cost if is a cart with only virtuals products
        boolean virtual = this.isVirtualCart();
        if (virtual && type == JeproLabCartModel.ONLY_SHIPPING) {
            return 0;
        }

        if (virtual && type == JeproLabCartModel.BOTH) {
            type = JeproLabCartModel.BOTH_WITHOUT_SHIPPING;
        }

        float shippingFees;

        if (withShipping || type == JeproLabCartModel.ONLY_DISCOUNTS) {
            if((analyzes == null) && (carrierId <= 0)) {
                shippingFees = this.getTotalShippingCost(0, withTaxes);
            } else {
                shippingFees = this.getPackageShippingCost(carrierId, withTaxes, null, analyzes);
            }
        } else {
            shippingFees = 0;
        }

        if (type == JeproLabCartModel.ONLY_SHIPPING) {
            return shippingFees;
        }

        if (type == JeproLabCartModel.ONLY_ANALYZES_WITHOUT_SHIPPING) {
            type = JeproLabCartModel.ONLY_ANALYZES;
        }

        boolean paramAnalyze = true;
        if(analyzes == null) {
            paramAnalyze = false;
            analyzes = this.getAnalyzes();
        }

        if (type == JeproLabCartModel.ONLY_PHYSICAL_ANALYZES_WITHOUT_SHIPPING) {
            for(JeproLabAnalyzeModel analyze : analyzes){
                if (analyze.is_virtual) {
                    analyzes.remove(analyze);
                }
            }
            type = JeproLabCartModel.ONLY_ANALYZES;
        }

        float requestTotal = 0;
        if (JeproLabTaxModel.excludeTaxOption()) {
            withTaxes = false;
        }

        $products_total = array();
        float ecoTaxTotal = 0;
        int addressId;

        for(JeproLabAnalyzeModel analyze : analyzes) {
            // products refer to the cart details

            if (virtualContext.laboratory.laboratory_id != analyze.laboratory_id) {
                virtualContext.laboratory = new JeproLabLaboratoryModel(analyze.laboratory_id);
            }

            if (taxAddressType.equals("invoice_address_id")) {
                addressId = this.invoice_address_id;
            } else {
                addressId = (int) $product['id_address_delivery'];
            }

            // Get delivery address of the product from the cart
            if (!$address_factory -> addressExists(addressId)) {
                addressId = 0;
            }

            // The $null variable below is not used,
            // but it is necessary to pass it to getProductPrice because
            // it expects a reference.
            $null = null;
            float price = $price_calculator -> getAnalyzePrice(
                    analyze.analyze_id, withTaxes, analyze.analyze_attribute_id, 6, null, false, true,
                    analyze.cart_quantity, false, this.customer_id > 0 ? this.customer_id : null,
                    this.cart_id, addressId, null, useEcoTax, true, virtualContext
            );

            JeproLabAddressModel address = $address_factory -> findOrCreate(addressId, true);
            int taxRulesGroupId;
            JeproLabTaxModel.JeproLabTaxCalculator taxCalculator = null;

            if (withTaxes) {
                taxRulesGroupId = JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyze.analyze_id, virtualContext);
                taxCalculator = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, taxRulesGroupId).getTaxCalculator();
            } else {
                taxRulesGroupId = 0;
            }

            if (in_array($ps_round_type, array(Order::ROUND_ITEM, Order::ROUND_LINE))) {
                if (!isset($products_total[$id_tax_rules_group])) {
                    $products_total[$id_tax_rules_group] = 0;
                }
            }
            elseif(!isset($products_total[$id_tax_rules_group. '_'.$id_address])){
                $products_total[$id_tax_rules_group. '_'.$id_address]=0;
            }

            switch (roundType) {
                case Order::ROUND_TOTAL:
                    $products_total[$id_tax_rules_group. '_'.$id_address]+=$price * (int) $product['cart_quantity'];
                    break;

                case Order::ROUND_LINE:
                    $product_price = $price * $product['cart_quantity'];
                    $products_total[$id_tax_rules_group] += Tools::ps_round ($product_price, $compute_precision);
                    break;

                case Order::ROUND_ITEM:
                default:
                    $product_price = /*$with_taxes ? $tax_calculator->addTaxes($price) : */$price;
                    $products_total[$id_tax_rules_group] += Tools::ps_round ($product_price, $compute_precision)*(int) $product['cart_quantity'];
                    break;
            }
        }

        for($products_total as $key => $price){
            requestTotal += price;
        }

        $order_total_products = $order_total;

        if (type == JeproLabCartModel.ONLY_DISCOUNTS) {
            requestTotal = 0;
        }

        // Wrapping Fees
        float wrappingFees = 0;

        // With PS_ATCP_SHIPWRAP on the gift wrapping cost computation calls getOrderTotal with $type === Cart::ONLY_PRODUCTS, so the flag below prevents an infinite recursion.
        boolean includeGiftWrapping = (!$configuration->get('PS_ATCP_SHIPWRAP') || type != JeproLabCartModel.ONLY_ANALYZES);

        if(this.gift && includeGiftWrapping) {
            wrappingFees = JeproLabTools.convertPrice(JeproLabTools.roundPrice(this.getGiftWrappingPrice(withTaxes), computePrecision), Currency::getCurrencyInstance((int)this.id_currency));
        }
        if (type == JeproLabCartModel.ONLY_WRAPPING) {
            return wrappingFees;
        }

        float requestTotalDiscount = 0;
        float requestShippingDiscount = 0;
        List<JeproLabCartRuleModel> cartRules;
        boolean flag = false;
        if (!in_array($type, array(JeproLabCartModel.ONLY_SHIPPING, JeproLabCartModel.ONLY_ANALYZES)) && JeproLabCartRuleModel.isFeaturePublished()){
            // First, retrieve the cart rules associated to this "getOrderTotal"
            if (withShipping || type == JeproLabCartModel.ONLY_DISCOUNTS) {
                cartRules = this.getCartRules(JeproLabCartRuleModel.FILTER_ACTION_ALL);
            } else {
                cartRules = this.getCartRules(JeproLabCartRuleModel.FILTER_ACTION_REDUCTION);
                // Cart Rules array are merged manually in order to avoid doubles
                for(JeproLabCartRuleModel tmpCartRule : this.getCartRules(JeproLabCartRuleModel.FILTER_ACTION_GIFT)){
                    flag = false;
                    for(JeproLabCartRuleModel cartRule : cartRules){
                        if (tmpCartRule.cart_rule_id == cartRule.cart_rule_id) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        cartRules.add(tmpCartRule);
                    }
                }
            }

            int deliveryAddresId = 0;
            if (isset($products[0])) {
                deliveryAddresId = (is_null($products) ? this.delivery_address_id : $products[0]['id_address_delivery']);
            }
            Map<String, Object> analyzePpackage = new HashMap<>()array('id_carrier' = > $id_carrier, 'id_address' =>$id_address_delivery, 'products' =>$products);

            // Then, calculate the contextual value for each one
            flag = false;
            for(JeproLabCartRuleModel cartRule : cartRules){
                // If the cart rule offers free shipping, add the shipping cost
                if ((withShipping || type == JeproLabCartModel.ONLY_DISCOUNTS) && $cart_rule['obj']->free_shipping && !flag){
                    requestShippingDiscount = JeproLabTools.roundPrice($cart_rule['obj'] -> getContextualValue(withTaxes, virtualContext, JeproLabCartRuleModel.FILTER_ACTION_SHIPPING, (paramAnalyze ? $package : null), useCache), computePrecision);
                    flag = true;
                }

                // If the cart rule is a free gift, then add the free gift value only if the gift is in this package
                if ((int) $cart_rule['obj']->gift_product){
                    boolean inRequest = false;
                    if (is_null($products)) {
                        inRequest = true;
                    } else {
                        for($products as $product) {
                            if ($cart_rule['obj']->gift_product == $product['id_product'] && $cart_rule['obj']->
                            gift_product_attribute == $product['id_product_attribute']){
                                inRequest = true;
                            }
                        }
                    }

                    if (inRequest) {
                        requestTotalDiscount += $cart_rule['obj']->
                        getContextualValue(withTaxes, virtualContext, JeproLabCartRuleModel.FILTER_ACTION_GIFT, $package, useCache);
                    }
                }

                // If the cart rule offers a reduction, the amount is prorated (with the products in the package)
                if ($cart_rule['obj']->reduction_percent > 0 || $cart_rule['obj']->reduction_amount > 0){
                    requestTotalDiscount += JeproLabTools.roundPrice($cart_rule['obj'] -> getContextualValue(withTaxes, virtualContext, JeproLabCartRuleModel.FILTER_ACTION_REDUCTION, $package, useCache), computePrecision);
                }
            }
            requestTotalDiscount = Math.min(JeproLabTools.roundPrice(requestTotalDiscount, 2), requestTotalAnalyzes) + requestShippingDiscount;
            requestTotal -= requestTotalDiscount;
        }

        if (type == JeproLabCartModel.BOTH){
            requestTotal += shippingFees + wrappingFees;
        }

        if (requestTotal < 0 && type != JeproLabCartModel.ONLY_DISCOUNTS) {
            return 0;
        }

        if (type == JeproLabCartModel.ONLY_DISCOUNTS) {
            return requestTotalDiscount;
        }

        return JeproLabTools.roundPrice(requestTotal, computePrecision);
    }

    public float getGiftWrappingPrice(){
        return getGiftWrappingPrice(true, 0);
    }

    public float getGiftWrappingPrice(boolean withTaxes){
        return getGiftWrappingPrice(withTaxes, 0);
    }

    /**
     * Get the gift wrapping price
     * @param withTaxes With or without taxes
     * @return float wrapping price
     */
    public float getGiftWrappingPrice(boolean withTaxes, int addressId){
        float wrappingFees = (float)Configuration::get('PS_GIFT_WRAPPING_PRICE');

        if (wrappingFees <= 0) {
            return wrappingFees;
        }

        if (withTaxes){
            if (Configuration::get('PS_ATCP_SHIPWRAP')) {
                // With PS_ATCP_SHIPWRAP, wrapping fee is by default tax included
                // so nothing to do here.
            } else {
                if (!addresses.containsKey(this.cart_id)){
                    if (addressId <= 0){
                        addressId = JeproLabSettingModel.getStringValue("tax_address_type").equals("delivery_address_id") ? this.delivery_address_id : this.invoice_address_id;
                    }
                    try {
                        addresses.put(this.cart_id, JeproLabAddressModel.initialize(addressId));
                    } catch (Exception $e) {
                        addresses.put(this.cart_id, new JeproLabAddressModel());
                        addresses.get(this.cart_id).country_id = JeproLabSettingModel.getIntValue("default_country");
                    }
                }

                JeproLabTaxModel.JeproLabTaxRulesManager taxManager = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(addresses.get(this.cart_id), JeproLabSettingModel.getIntValue("gift_wrapping_rules_rules"));
                JeproLabTaxModel.JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();
                wrappingFees = taxCalculator.addTaxes(wrappingFees);
            }
        } else if (Configuration::get('PS_ATCP_SHIPWRAP')){
            // With PS_ATCP_SHIPWRAP, wrapping fee is by default tax included, so we convert it
            // when asked for the pre tax price.
            wrappingFees = JeproLabTools.roundPrice(
                    wrappingFees / (1 + this.getAverageAnalyzesTaxRate()),
                    JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION
            );
        }

        return wrappingFees;
    }

    public float getAverageAnalyzesTaxRate(){
        return getAverageAnalyzesTaxRate(0, 0);
    }

    public float getAverageAnalyzesTaxRate(float cartAmountTaxExcluded){
        return getAverageAnalyzesTaxRate(cartAmountTaxExcluded, 0);
    }

    /**
     * The arguments are optional and only serve as return values in case caller needs the details.
     */
    public float getAverageAnalyzesTaxRate(float cartAmountTaxExcluded, float cartAmountTaxIncluded){
        cartAmountTaxIncluded = this.getRequestTotal(true, JeproLabCartModel.ONLY_ANALYZES);
        cartAmountTaxExcluded = this.getRequestTotal(false, JeproLabCartModel.ONLY_ANALYZES);

        float cartVatAmount = cartAmountTaxIncluded - cartAmountTaxExcluded;

        if (cartVatAmount == 0 || cartAmountTaxExcluded == 0) {
            return 0;
        } else {
            return JeproLabTools.roundPrice(cartVatAmount / cartAmountTaxExcluded , 3);
        }
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

    public boolean containsAnalyze(int analyzeId, int analyzeAttributeId, int customizationId, int deliveryAddressId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT cart_analyze." + dataBaseObject.quoteName("quantity") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze");
        query += " AS cart_analyze ";

        if(customizationId > 0){
            query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_customization") + " AS customization ON (customization.";
            query += dataBaseObject.quoteName("analyze_id") + " = cart_analyze." + dataBaseObject.quoteName("analyze_id") + " AND ";
            query += "customization." + dataBaseObject.quoteName("analyze_attribute_id") + " = cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id") + ")";
        }

        query += " WHERE cart_analyze." + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND cart_analyze.";
        query += dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId + " AND cart_analyze.";
        query += dataBaseObject.quoteName("cart_id") + " = " + this.cart_id;
        if (Configuration::get('PS_ALLOW_MULTISHIPPING') && this.isMultiAddressDelivery()){
            query += " AND cart_analyze." + dataBaseObject.quoteName("delivery_address_id") + " = " + deliveryAddressId;
        }

        if(customizationId > 0){
            query += " AND customization." + dataBaseObject.quoteName("customization_id") + " = " + customizationId;
        }

        return Db::getInstance()->getRow($sql);
    }

    /**
     * Does the cart use multiple address
     * @return bool
     */
    public boolean isMultiAddressDelivery(){
        if (!JeproLabCartModel._cache_number_addresses.containsKey(this.cart_id)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT COUNT(DISTINCT " + dataBaseObject.quoteName("delivery_address_id") + ") AS addresses FROM ";
            query += dataBaseObject.quoteName("#__jeprolab_cart_analyzes") + " AS cart_analyze WHERE ";
            query += dataBaseObject.quoteName("cart_id") + " = " + this.cart_id;

            dataBaseObject.setQuery(query);
            JeproLabCartModel._cache_number_addresses.put(this.cart_id, (int)dataBaseObject.loadValue("addresses"));
        }
        return JeproLabCartModel._cache_number_addresses.get(this.cart_id) > 0;
    }

    public boolean isVirtualCart(){
        return isVirtualCart(false);
    }

    /**
     * Check if cart contains only virtual products
     *
     * @return bool true if is a virtual cart or false
     */
    public boolean isVirtualCart(boolean strict){
        if (!ProductDownload::isFeatureActive()){
            return false;
        }

        if (!isset(self::$_isVirtualCart[this.id])) {
        $products = this.getProducts();
        if (!count($products)) {
            return false;
        }

        $is_virtual = 1;
        foreach ($products as $product) {
            if (empty($product['is_virtual'])) {
                $is_virtual = 0;
            }
        }
        self::$_isVirtualCart[this.id] = (int)$is_virtual;
    }

        return self::$_isVirtualCart[this.id];
    }

    /**
     * Customization management
     */
    protected function updateCustomizationQuantity($quantity, $id_customization, $id_product, $id_product_attribute, $id_address_delivery, $operator = 'up')
    {
        // Link customization to product combination when it is first added to cart
        if (empty($id_customization)) {
            $customization = $this->getProductCustomization($id_product, null, true);
            foreach ($customization as $field) {
                if ($field['quantity'] == 0) {
                    Db::getInstance()->execute('
                            UPDATE `'._DB_PREFIX_.'customization`
                    SET `quantity` = '.(int)$quantity.',
                    `id_product_attribute` = '.(int)$id_product_attribute.',
                    `id_address_delivery` = '.(int)$id_address_delivery.',
                    `in_cart` = 1
                    WHERE `id_customization` = '.(int)$field['id_customization']);
                }
            }
        }

        /* Deletion */
        if (!empty($id_customization) && (int)$quantity < 1) {
            return $this->_deleteCustomization((int)$id_customization, (int)$id_product, (int)$id_product_attribute);
        }

        /* Quantity update */
        if (!empty($id_customization)) {
            $result = Db::getInstance()->getRow('SELECT `quantity` FROM `'._DB_PREFIX_.'customization` WHERE `id_customization` = '.(int)$id_customization);
            if ($result && Db::getInstance()->NumRows()) {
                if ($operator == 'down' && (int)$result['quantity'] - (int)$quantity < 1) {
                    return Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'customization` WHERE `id_customization` = '.(int)$id_customization);
                }

                return Db::getInstance()->execute('
                        UPDATE `'._DB_PREFIX_.'customization`
                SET
                `quantity` = `quantity` '.($operator == 'up' ? '+ ' : '- ').(int)$quantity.',
                `id_address_delivery` = '.(int)$id_address_delivery.',
                `in_cart` = 1
                WHERE `id_customization` = '.(int)$id_customization);
            } else {
                Db::getInstance()->execute('
                        UPDATE `'._DB_PREFIX_.'customization`
                SET `id_address_delivery` = '.(int)$id_address_delivery.',
                `in_cart` = 1
                WHERE `id_customization` = '.(int)$id_customization);
            }
        }
        // refresh cache of self::_products
        $this->_products = $this->getProducts(true);
        $this->update();
        return true;
    }






    public static class JeproLabCartRuleModel extends JeproLabModel{
        public int cart_rule_id;
        public String name;
        public int customer_id;
        public String description;
        public int quantity = 1;
        public int quantity_per_user = 1;
        public int priority = 1;
        public boolean partial_use = true;
        public String code;
        public float minimum_amount;
        public float minimum_amount_tax;
        public int minimum_amount_currency;
        public float minimum_amount_shipping;
        public boolean country_restriction;
        public boolean carrier_restriction;
        public boolean group_restriction;
        public boolean cart_rule_restriction;
        public boolean analyze_restriction;
        public boolean lab_restriction;
        public boolean free_shipping;
        public float reduction_percent;
        public float reduction_amount;
        public boolean reduction_tax;
        public int reduction_currency;
        public int reduction_analyze;
        public int gift_analyze;
        public int gift_analyze_attribute;
        public boolean highlight = false;
        public boolean published = true;

        public Date date_add;

        public Date date_upd;

        public Date date_from;

        public Date date_to;

        /* Filters used when retrieving the cart rules applied to a cart of when calculating the value of a reduction */
        public static final int FILTER_ACTION_ALL = 1;
        public static final int FILTER_ACTION_SHIPPING = 2;
        public static final int FILTER_ACTION_REDUCTION = 3;
        public static final int FILTER_ACTION_GIFT = 4;
        public static final int FILTER_ACTION_ALL_NOCAP = 5;

        public static final String BO_REQUEST_CODE_PREFIX = "BO_REQUEST_";
        public static boolean is_feature_active = false;

        public JeproLabCartRuleModel(){
            this(0, 0, 0);
        }

        public JeproLabCartRuleModel(int cartRuleId){
            this(cartRuleId, 0, 0);
        }

        public JeproLabCartRuleModel(int cartRuleId, int langId){
            this(cartRuleId, langId, 0);
        }


        /**
         * Builds the object
         *
         * @param int|null $id      If specified, loads and existing object from DB (optional).
         * @param int|null $id_lang Required if object is multilingual (optional).
         * @param int|null $id_shop ID shop for objects with multishop tables.
         *
         * @throws PrestaShopDatabaseException
         * @throws PrestaShopException
         */
        public JeproLabCartRuleModel(int cartRuleId, int langId, int labId){
            $class_name = get_class($this);
            if (!isset(ObjectModel::$loaded_classes[$class_name])) {
            this.def = ObjectModel::getDefinition($class_name);
            this.setDefinitionRetrocompatibility();
            if (!Validate::isTableOrIdentifier(this.def['primary']) || !Validate::isTableOrIdentifier(this.def['table'])) {
                throw new PrestaShopException('Identifier or table format not valid for class '.$class_name);
            }

            ObjectModel::$loaded_classes[$class_name] = get_object_vars($this);
        } else {
            foreach (ObjectModel::$loaded_classes[$class_name] as $key => $value) {
                this.{$key} = $value;
            }
        }

            if ($id_lang !== null) {
                this.id_lang = (Language::getLanguage($id_lang) !== false) ? $id_lang : Configuration::get('PS_LANG_DEFAULT');
            }

            if ($id_shop && this.isMultishop()) {
                this.id_shop = (int)$id_shop;
                this.get_shop_from_context = false;
            }

            if (this.isMultishop() && !this.id_shop) {
                this.la = Context::getContext()->shop->id;
            }

            if ($id) {
                $entity_mapper = Adapter_ServiceLocator::get("Adapter_EntityMapper");
                $entity_mapper->load($id, $id_lang, $this, this.def, this.id_shop, self::$cache_objects);
            }
        }


        /**
         * @param customerId
         * @return bool
         */
        public static boolean deleteByCustomerId(int customerId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule ";
            query += " WHERE " + staticDataBaseObject.quoteName("customer_id") + " = " + customerId;

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }

        /**
         *  When an entity associated to a analyze rule (product, category, attribute, supplier, manufacturer...)
         *  is deleted, the product rules must be updated
         **/
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
*/
            String itemList = "";
            for(int id : list){
                 itemList += id + ", ";
            }
            itemList = itemList.endsWith(", ") ? itemList.substring(0, itemList.length() - 3) : itemList;
            // Delete associated restrictions on cart rules
            String query = "DELETE cart_rule_analyze_rule_value FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule");
            query += " AS cart_rule_analyze_rule LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value");
            query += " AS cart_rule_analyze_rule_value ON cart_rule_analyze_rule." + staticDataBaseObject.quoteName("analyze_rule_id") + " =";
            query += "cart_rule_analyze_rule_value." + staticDataBaseObject.quoteName("analyze_rule_id") + " WHERE cart_rule_analyze_rule.";
            query += staticDataBaseObject.quoteName("type") + " = " + staticDataBaseObject.quoteName(type) + " AND cart_rule_analyze_rule_value.";
            query += staticDataBaseObject.quoteName("item_id") + " IN (" + itemList + ")";

            staticDataBaseObject.setQuery(query);

        /*/Db::getInstance()->execute('
            //'); // $list is checked a few lines above

        // Delete the product rules that does not have any values
        if (staticDataBaseObject.executeQuery() > 0) {
            query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule") + " NOT EXISTS (SELECT";
            query += " 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_value") + " WHERE ";
            query += "cart_rule_product_rule." + staticDataBaseObject.quoteName("product_rule_id") + " = cart_rule_product_rule_value.";
            query += staticDataBaseObject.quoteName("product_rule_id");
        }
        // If the product rules were the only conditions of a product rule group, delete the product rule group
        if (Db::getInstance()->Affected_Rows() > 0) {
            Db::getInstance()->delete('cart_rule_product_rule_group', 'NOT EXISTS (SELECT 1 FROM `'.dataBaseObject.quoteName(".'cart_rule_product_rule`
            WHERE `'.dataBaseObject.quoteName(".'cart_rule_product_rule`.`id_product_rule_group` = `'.dataBaseObject.quoteName(".'cart_rule_product_rule_group`.`id_product_rule_group`)');
            staticDataBaseObject.query(false);
        }

        // If the product rule group were the only restrictions of a cart rule, update de cart rule restriction cache
        if (Db::getInstance()->Affected_Rows() > 0){
                query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_analyze_rule_group") + " AS cart_rule_analyze_rule_group ";
                query += " ON cart_rule.cart_rule_id = cart_rule_analyze_rule_group.cart_rule_id SET analyze_restriction = IF(";
                query += " cart_rule_analyze_rule_group.analyze_rule_group_id IS NULL, 0, 1)";

                staticDataBaseObject.setQuery(query);
                staticDataBaseObject.query(false);
            } */
            return true;
        }

        public static function autoAddToCart(){
            return autoAddToCart(null);
        }


        /**
         * @param Context|null $context
         * @return mixed
         */
        public static function autoAddToCart(JeproLabContext context){
            if (context == null) {
                context = JeproLabContext.getContext();
            }

            if (!JeproLabCartRuleModel.isFeaturePublished() || (context.cart == null || context.cart.cart_id <= 0)){
                return;
            }

            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT SQL_NO_CACHE cart_rule.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule") + " AS cart_rule LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_cart_rule_lab") + " AS cart_rule_lab ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " = cart_rule_lab." + staticDataBaseObject.quoteName("cart_rule_id") ;
            query += (context.customer.customer_id <= 0 && JeproLabGroupModel.isFeaturePublished() ? " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_group") +
                    " AS cart_rule_group ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_group." + staticDataBaseObject.quoteName("cart_rule_id") : "");
            query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_carrier") + " AS cart_rule_carrier ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " = cart_rule_carrier." + staticDataBaseObject.quoteName("cart_rule_id") + (context.cart.carrier_id > 0 ? " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_carrier") +
                    " AS carrier ON (carrier." + staticDataBaseObject.quoteName("reference_id") + " = cart_rule_carrier." + staticDataBaseObject.quoteName("carrier_id") +
                    " AND carrier." + staticDataBaseObject.quoteName("deleted") + " = 0)"  : "" ) + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_country");
            query += " AS cart_rule_country ON cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_country." + staticDataBaseObject.quoteName("cart_rule_id");
            query += " WHERE cart_rule."  + staticDataBaseObject.quoteName("published") + " = 1 AND cart_rule." + staticDataBaseObject.quoteName("code") + " = '' AND cart_rule.";
            query += staticDataBaseObject.quoteName("quantity") + " > 0 AND cart_rule." + staticDataBaseObject.quoteName("date_from") + " < '" + JeproLabTools.date("Y-m-d H:i:s");
            query += "' AND cart_rule." + staticDataBaseObject.quoteName("date_to") + " > '" + JeproLabTools.date("Y-m-d H:i:s") + "' AND (cart_rule.";
            query += staticDataBaseObject.quoteName("customer_id") + " = 0 " + (context.customer.customer_id > 0 ? " OR cart_rule." + staticDataBaseObject.quoteName("customer_id") +
                    " = " + context.cart.customer_id : "") + ") AND ( cart_rule." + staticDataBaseObject.quoteName("carrier_restriction") + " = 0 " +
                    (context.cart.carrier_id > 0 ? " OR cart." + staticDataBaseObject.quoteName("carrier_id") + " = " + context.cart.carrier_id : "" ) + ") AND ( ";
            query += " cart_rule." + staticDataBaseObject.quoteName("lab_restriction") + " = 0 " ;
            query += ((JeproLabLaboratoryModel.isFeaturePublished() && context.laboratory.laboratory_id > 0) ? " OR cart_rule_lab." + staticDataBaseObject.quoteName("lab_id") +
                    " = " + context.laboratory.laboratory_id : "") + ") AND ( cr.`group_restriction` = 0 " + (context.customer.customer_id > 0 ? " OR EXISTS ( SELECT 1 FROM " +
                    staticDataBaseObject.quoteName("#__jeprolab_customer_group") + " AS customer_group INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_cart_rule_group") +
                    " AS cart_rule_group ON customer_group." + staticDataBaseObject.quoteName("group_id") + " = cart_rule_group." + staticDataBaseObject.quoteName("group_id") +
                    " WHERE cart_rule." + staticDataBaseObject.quoteName("cart_rule_id") + " = cart_rule_group." + staticDataBaseObject.quoteName("cart_rule_id") + " AND customer_group." +
                    staticDataBaseObject.quoteName("customer_id") + " = " + context.customer.customer_id + " LIMIT 1) " : (JeproLabGroupModel.isFeaturePublished() ? " OR cart_rule_group." +
                    staticDataBaseObject.quoteName("group_id") + " = " + JeproLabSettingModel.getIntValue("undefined_group") : "")) + ") AND (cart_rule." + staticDataBaseObject.quoteName("reduction_product") +
                    " <= 0 OR EXISTS ( SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze") + "  AS cart_analyze WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = cart_rule." +
                    staticDataBaseObject.quoteName("reduction_analyze") + " AND " + staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + ") ) AND NOT EXISTS (SELECT 1 FROM " +
                    staticDataBaseObject.quoteName("#__jeprolab_cart_cart_rule") + " AS cart_cart_rule WHERE cart_rule." + staticDataBaseObject.quoteName("cart_rule_id")  + " = cart_cart_rule." +
                    staticDataBaseObject.quoteName("cart_rule_id") + " AND " + staticDataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + ") ORDER BY priority ";

            staticDataBaseObject.setQuery(query);
            ResultSet cartRuleSet = staticDataBaseObject.loadObjectList();
            if (cartRuleSet != null) {
                $cart_rules = ObjectModel::hydrateCollection('CartRule', $result);
                if ($cart_rules) {
                    foreach ($cart_rules as $cart_rule) {
                        /** @var CartRule $cart_rule */
                        if (cartRule.checkValidity(context, false, false)) {
                            context.cart.addCartRule(cartRule.cart_rule_id);
                        }
                    }
                }
            }
        }

        public static function autoRemoveFromCart(){
            return autoRemoveFromCart(null);
        }

        public static function autoRemoveFromCart(JeproLabContext context){
            if (context == null) {
                context = JeproLabContext.getContext();
            }
            if (!JeproLabCartRuleModel.isFeaturePublished() || (context.cart == null || context.cart.cart_id <= 0)){
                return array();
            }

            static $errors = array();
            for(JeproLabCartRuleModel cartRule : context.cart.getCartRules()) {
                if ($error = $cart_rule['obj']->checkValidity(context, true)){
                    context.cart.removeCartRule($cart_rule['obj']->id);
                    context.cart.update();
                    $errors[]=$error;
                }
            }
            return $errors;
        }

        /**
         * @return bool
         */
        public static boolean isFeaturePublished(){
            if (!is_feature_active) {
                is_feature_active = JeproLabSettingModel.getIntValue("cart_rule_feature_active") > 0;
            }
            return is_feature_active;
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
         * @return bool|mixed|string
         */
        public boolean checkValidity(JeproLabContext context, boolean alreadyInCart, boolean displayError, boolean checkCarrier){
            if (!JeproLabCartRuleModel.isFeaturePublished()) {
                return false;
            }

            if(!this.published){
                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "This voucher is disabled");
            }

            if(this.quantity <= 0) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "This voucher has already been used");
            }
            if (strtotime(this.date_from) > time()) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "This voucher is not valid yet");
            }

            if (strtotime(this.date_to) < time()) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "This voucher has expired");
            }

            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query;
            if (context.cart.customer_id > 0){
                query = "SELECT count(*) AS qty FROM "  + dataBaseObject.quoteName("#__jeprolab_request") + " AS request LEFT JOIN ";
                query += dataBaseObject.quoteName("#__jeprolab_request_cart_rule") + " AS request_cart_rule ON request.";
                query += dataBaseObject.quoteName("request_id") + " = request_cart_rule." + dataBaseObject.quoteName("request_id");
                query += " WHERE request." + dataBaseObject.quoteName("customer_id") + " = " + context.cart.customer_id ;
                query += " AND request_cart_rule." + dataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id;
                query += " AND " + JeproLabSettingModel.getIntValue("request_status_error") + " != request.current_state";

                dataBaseObject.setQuery(query);
                int quantityUsed = (int)dataBaseObject.loadValue("qty");

                if(quantityUsed + 1 > this.quantity_per_user){
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher anymore (usage limit reached)");
                }
            }

            // Get an intersection of the customer groups and the cart rule groups (if the customer is not logged in, the default group is Visitors)
            if (this.group_restriction){
                query = "SELECT cart_rule_group." + dataBaseObject.quoteName("cart_rule_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_group");
                query += " AS cart_rule_group WHERE cart_rule_group." + dataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id + " AND cart_rule_group.";
                query += dataBaseObject.quoteName("group_id") + (context.cart.customer_id > 0 ? " IN (SELECT customer_group." + dataBaseObject.quoteName("group_id") +
                        " FROM " + dataBaseObject.quoteName("#__jeprolab_customer_group") + " AS customer_group WHERE customer_group." + dataBaseObject.quoteName("customer_id") +
                        " = " + context.cart.customer_id + ")" : " = " + JeproLabSettingModel.getIntValue("unidentified_group")) ;

                dataBaseObject.setQuery(query);
                int cartRuleId = (int)dataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher");
                }
            }

            // Check if the customer delivery address is usable with the cart rule
            if (this.country_restriction){
                if (context.cart.delivery_address_id <= 0){
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You must choose a delivery address before applying this voucher to your order");
                }

                query = "SELECT cart_rule_country." + dataBaseObject.quoteName("cart_rule_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_country");
                query += " AS cart_rule_country WHERE cart_rule_country." + dataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id ;
                query += " AND cart_rule_country." + dataBaseObject.quoteName("country_id") + " = (SELECT address." + dataBaseObject.quoteName("country_id");
                query += " FROM " + dataBaseObject.quoteName("#__jeprolab_address") + " AS address WHERE address." + dataBaseObject.quoteName("address_id");
                query += " = " + context.cart.delivery_address_id;

                dataBaseObject.setQuery(query);
                int cartRuleId = (int)dataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher in your country of delivery");
                }
            }

            // Check if the carrier chosen by the customer is usable with the cart rule
            if (this.carrier_restriction && checkCarrier) {
                if (context.cart.carrier_id <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You must choose a carrier before applying this voucher to your order");
                }
                query = "SELECT cart_rule_carrier." + dataBaseObject.quoteName("cart_rule_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_carrier");
                query += " AS cart_rule_carrier INNER JOIN " + dataBaseObject.quoteName("#__jeprolab_carrier") + " AS carrier ON (carrier.";
                query += dataBaseObject.quoteName("reference_id") + " = cart_rule_carrier." + dataBaseObject.quoteName("carrier_id") + " AND carrier.";
                query += dataBaseObject.quoteName("deleted") + " = 0) WHERE cart_rule_carrier." + dataBaseObject.quoteName("cart_rule_id") + " = ";
                query += this.cart_rule_id + " AND carrier." + dataBaseObject.quoteName("carrier_id") + " = " + context.cart.carrier_id;

                dataBaseObject.setQuery(query);
                int cartRuleId = (int)dataBaseObject.loadValue("cart_rule_id");

                if (cartRuleId <= 0) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher with this carrier");
                }
            }

            // Check if the cart rules apply to the shop browsed by the customer
            if (this.lab_restriction && context.laboratory.laboratory_id > 0 && JeproLabLaboratoryModel.isFeaturePublished()) {
                query = "SELECT cart_rule_lab." + dataBaseObject.quoteName("cart_rule_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_lab");
                query += " AS cart_rule_lab WHERE cart_rule_lab." + dataBaseObject.quoteName("cart_rule_id") + " = " + this.cart_rule_id + " AND cart_rule_lab.";
                query += dataBaseObject.quoteName("lab_id") + " = " + context.laboratory.laboratory_id;

                dataBaseObject.setQuery(query);
                int cartRuleId =  (int)dataBaseObject.loadValue("cart_rule_id");
                if (cartRuleId <=0){
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher");
                }
            }

            // Check if the products chosen by the customer are usable with the cart rule
            if(this.analyze_restriction) {
                $r = this.checkAnalyzeRestrictions(context, false, displayError, alreadyInCart);
                if ($r !== false && displayError) {
                    return $r;
                } else if (!$r && !displayError){
                    return false;
                }
            }

            // Check if the cart rule is only usable by a specific customer, and if the current customer is the right one
            if (this.customer_id > 0 && context.cart.customer_id != this.customer_id) {
                if (!JeproLabContext.getContext().customer.isLogged()) {
                    return (!displayError) ? false : (JeproLabTools.displayBarMessage(500, "You cannot use this voucher").' - '.Tools::displayError('Please log in first'));
                }
                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher");
            }

            if (this.minimum_amount > 0 && checkCarrier) {
                // Minimum amount is converted to the contextual currency
                float minimumAmount = this.minimum_amount;
                if(this.minimum_amount_currency != JeproLabContext.getContext().currency.currency_id){
                    minimumAmount = JeproLabTools.convertFullPrice(minimumAmount, new JeproLabCurrencyModel(this.minimum_amount_currency), JeproLabContext.getContext().currency);
                }

                int cartTotal = context.cart.getRequestTotal(this.minimum_amount_tax, JeproLabCartModel.ONLY_ANALYZES);
                if (this.minimum_amount_shipping > 0) {
                    cartTotal += context.cart.getRequestTotal(this.minimum_amount_tax, JeproLabCartModel.ONLY_SHIPPING);
                }
                List<JeproLabAnalyzeModel> analyzes = context.cart.getAnalyzes();
                List<JeproLabCartRuleModel> cartRules = context.cart.getCartRules();

                cartRules.stream().filter(cartRule -> cartRule.gift_analyze > 0).forEach(cartRule -> {
                    analyzes.stream().filter(analyze -> (analyze.gift) && analyze.analyze_id == cartRule.gift_analyze && analyze.analyze_attribute_id == cartRule.gift_analyze_attribute).forEach(analyze -> {
                        cartTotal = JeproLabTools.roundPrice(cartTotal - (this.minimum_amount_tax > 0 ? analyze.analyze_price.price_with_tax : analyze.analyze_price.price), context.currency.decimals * JeproLabConfigurationSettings.JEPROLAB_PRICE_DISPLAY_PRECISION);
                    });
                });

                if (cartTotal < minimumAmount) {
                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You have not reached the minimum amount required to use this voucher");
                }
            }

            /**
             *  This loop checks:
                - if the voucher is already in the cart
                - if a non compatible voucher is in the cart
                - if there are products in the cart (gifts excluded)
                Important note: this MUST be the last check, because if the tested cart rule has priority over a non combinable one in the cart, we will switch them
            */
            int nbAnalyzes = JeproLabCartModel.getNumberOfAnalyzes(context.cart.cart_id);
            List<JeproLabCartRuleModel> otherCartRules = new ArrayList<>();
            if(checkCarrier) {
                otherCartRules = context.cart.getCartRules();
            }

            if (otherCartRules.size() > 0) {
                for(JeproLabCartRuleModel otherCartRule : otherCartRules) {
                    if(otherCartRule.cart_rule_id == this.cart_rule_id && !alreadyInCart) {
                        return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "This voucher is already in your cart");
                    }

                    if(otherCartRule.gift_analyze > 0){
                        --nbAnalyzes;
                    }

                    if (this.cart_rule_restriction && otherCartRule.cart_rule_restriction && otherCartRule.cart_rule_id != this.cart_rule_id){
                        query = "SELECT " + dataBaseObject.quoteName("cart_rule_id_1") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_rule_combination");
                        query += " WHERE " + dataBaseObject.quoteName("cart_rule_id_1") + " = " + this.cart_rule_id + " AND " + dataBaseObject.quoteName("cart_rule_id_2");
                        query += " = " + otherCartRule.cart_rule_id + ") OR (cart_rule_id_2 = " + this.cart_rule_id + " AND cart_rule_id_1 = " + otherCartRule.cart_rule_id;
                        query += ") ";

                        dataBaseObject.setQuery(query);
                        int combinable = (int)dataBaseObject.loadValue("cart_rule_id_1");
                        if (combinable <= 0) {
                            JeproLabCartRuleModel cartRule = new JeproLabCartRuleModel(otherCartRule.cart_rule_id, context.cart.language_id);
                            // The cart rules are not combinable and the cart rule currently in the cart has priority over the one tested
                            if (cartRule.priority <= this.priority) {
                                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "This voucher is not combinable with an other voucher already in your cart:" + " " + cartRule.name);
                            }
                            // But if the cart rule that is tested has priority over the one in the cart, we remove the one in the cart and keep this new one
                            else {
                                context.cart.removeCartRule(cartRule.cart_rule_id);
                            }
                        }
                    }
                }
            }

            if (nbAnalyzes <= 0) {
                return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "Cart is empty");
            }

            if (!displayError) {
                return true;
            }
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context){
            return checkAnalyzeRestrictions(context, false, true, false);
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context, boolean returnAnalyzes){
            return checkAnalyzeRestrictions(context, returnAnalyzes, true, false);
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context, boolean returnAnalyzes, boolean displayError){
            return checkAnalyzeRestrictions(context, returnAnalyzes, displayError, false);
        }

        protected boolean checkAnalyzeRestrictions(JeproLabContext context, boolean returnAnalyzes, boolean displayError, boolean alreadyInCart){
            $selected_products = array();

            // Check if the products chosen by the customer are usable with the cart rule
            if(this.analyze_restriction) {
                analyzeRuleGroups = this.getAnalyzeRuleGroups();
                for($product_rule_groups as $id_product_rule_group => $product_rule_group) {
                    List<String> eligibleAnalyzeList = new ArrayList<>();
                    List<JeproLabAnalyzeModel> analyzes = new ArrayList<>();
                    if (context.cart != null && context.cart.cart_id > 0) {
                        eligibleAnalyzeList.addAll(context.cart.getAnalyzes().stream().map(analyze -> analyze.analyze_id + "_" + analyze.analyze_attribute_id).collect(Collectors.toList()));
                    }

                    if(eligibleAnalyzeList.size() <= 0){
                        return (!displayError) ? false : JeproLabTools.displayBarMessage(500, 'You cannot use this voucher in an empty cart');
                    }

                    analyzeRules = this.getAnalyzeRules($id_product_rule_group);
                    if(dataBaseObject == null){
                        dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query;
                    for(analyzeRule : analyzeRules) {
                        switch (analyzeRule.type) {
                            case "attributes":
                                query = "SELECT cart_analyze." + dataBaseObject.quoteName("quantity") + ", cart_analyze." + dataBaseObject.quoteName("analyze_id");
                                query += ", analyze_attribute_combination." + dataBaseObject.quoteName("attribute_id") + ", cart_analyze." ;
                                query += dataBaseObject.quoteName("analyze_attribute_id") + " FROM "  + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS ";
                                query += " cart_analyze LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination") ;
                                query += " AS analyze_attribute_combination ON cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id");
                                query += " = analyze_attribute_combination." + dataBaseObject.quoteName("analyze_attribute_id") + " WHERE cart_analyze.";
                                query += dataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + " AND cart_analyze." + dataBaseObject.quoteName("analyze_id");
                                query += " IN (" + , $eligible_products_list)) + ") AND cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id") ;
                                query += " > 0 ";

                                dataBaseObject.setQuery(query);
                                ResultSet resultSet = dataBaseObject.loadObjectList();
                                List attributeList = new ArrayList<>();

                                int countMatchingAnalyzes = 0;
                                $matching_products_list = array();
                                if(resultSet != null){
                                    try {
                                        while (resultSet.next()){
                                            if(analyzeRule.values.contains(resultSet.getInt("attribute_id"))){
                                                countMatchingAnalyzes += resultSet.getInt("quantity");
                                                if(alreadyInCart && this.gift_analyze == resultSet.getInt("analyze_id") && this.gift_analyze_attribute == resultSet.getInt("analyze_attribute_id")){
                                                    --countMatchingAnalyzes;
                                                }
                                                matchingAnalyzesList.add(resultSet.getInt("analyze_id") + "_" + resultSet.getInt("analyze_attribute_id"))
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

                                if(countMatchingAnalyzes < analzeRuleGroup.quantity){
                                    return (!displayError) ? false : JeproLabTools.displayBarMessage(500, "You cannot use this voucher with these products");
                                }
                                eligibleAnalyzeList = JeproLabCartRuleModel.array_uintersect(eligibleAnalyzeList, matchingAnalyzesList);
                            break;
                            case "analyzes":
                                query = "SELECT cart_analyze."  + dataBaseObject.quoteName("quantity") + ", cart_analyze." + dataBaseObject.quoteName("analyze_id");
                                query += " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze") + " AS cart_analyze WHERE cart_analyze.";
                                query += dataBaseObject.quoteName("cart_id") + " = " + context.cart.cart_id + " AND cart_analyze." + dataBaseObject.quoteName("analyze_id");
                                query += " IN (" + mplode(',', array_map('intval', $eligible_products_list)) + ")";

                                dataBaseObject.setQuery(query);
                                ResultSet cartAnalyzes = dataBaseObject.loadObjectList();
                                countMatchingAnalyzes = 0;
                                List matchingAnalyzesList = new ArrayList<>();
                                for(cartAnalyze : cartAnalyzes) { Process result set
                                    if (in_array($cart_product['id_product'], $product_rule['values'])) {
                                        countMatchingAnalyzes += $cart_product['quantity'];
                                        if (alreadyInCart && this.gift_analyze == $cart_product['id_product']) {
                                            --$count_matching_products;
                                        }
                                        $matching_products_list[]=$cart_product['id_product']. '-0';
                                    }
                                }
                            if ($count_matching_products < $product_rule_group['quantity']) {
                                return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                            }
                            $eligible_products_list = CartRule::array_uintersect($eligible_products_list, $matching_products_list);
                            break;
                            case "categories":
                                query = "SELECT cart_analyze." + dataBaseObject.quoteName("quantity") + ", cart_analyze.";
                                query += dataBaseObject.quoteName("analyze_id") + ", cart_analyze." + dataBaseObject.quoteName("analyze_attribute_id");
                                query += ", analyze_category." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_cart_analyze");
                                query += " AS cart_analyze LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_analyze") + " AS category_analyze ON ";
                                query += "cart_analyze." + dataBaseObject.quoteName("analyze_id") + " = category_analyze." + dataBaseObject.quoteName("analyze_id");
                                query += " WHERE cart_analyze." + dataBaseObject.quoteName("cart_id") + " = "  + context.cart.cart_id + " AND cart_analyze." ;
                                query += dataBaseObject.quoteName("analyze_id") + " IN (" + .implode(',', array_map('intval', $eligible_products_list)). + ") AND cart_analyze." + dataBaseObject.quoteName("analyze_id") + " <> " + this.gift_analyze;

                                dataBaseObject.setQuery(query);
                                ResultSet cartCategories = dataBaseObject.loadObjectList();
                                countMatchingAnalyzes = 0;
                                matchingAnalyzesList = new ArrayList();
                                for(cartCategories as $cart_category) {
                                if (in_array($cart_category['id_category'], $product_rule['values'])
                                        /**
                                         * We also check that the product is not already in the matching product list,
                                         * because there are doubles in the query results (when the product is in multiple categories)
                                         */
                                        && !in_array($cart_category['id_product'].'-'.$cart_category['id_product_attribute'], $matching_products_list)) {
                                    $count_matching_products += $cart_category['quantity'];
                                    $matching_products_list[] = $cart_category['id_product'].'-'.$cart_category['id_product_attribute'];
                                }
                            }
                            if ($count_matching_products < $product_rule_group['quantity']) {
                                return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                            }
                            // Attribute id is not important for this filter in the global list, so the ids are replaced by 0
                            foreach ($matching_products_list as &$matching_product) {
                                $matching_product = preg_replace('/^([0-9]+)-[0-9]+$/', '$1-0', $matching_product);
                            }
                            $eligible_products_list = CartRule::array_uintersect($eligible_products_list, $matching_products_list);
                            break;
                            case "manufacturers":
                                $cart_manufacturers = Db::getInstance()->executeS('
                                    SELECT cp.quantity, cp.`id_product`, p.`id_manufacturer`
                                    FROM `'._DB_PREFIX_.'cart_product` cp
                                LEFT JOIN `'._DB_PREFIX_.'product` p ON cp.id_product = p.id_product
                                WHERE cp.`id_cart` = '.(int)$context->cart->id.'
                                AND cp.`id_product` IN ('.implode(',', array_map('intval', $eligible_products_list)).')');
                                $count_matching_products = 0;
                                $matching_products_list = array();
                                foreach ($cart_manufacturers as $cart_manufacturer) {
                                if (in_array($cart_manufacturer['id_manufacturer'], $product_rule['values'])) {
                                    $count_matching_products += $cart_manufacturer['quantity'];
                                    $matching_products_list[] = $cart_manufacturer['id_product'].'-0';
                                }
                            }
                            if ($count_matching_products < $product_rule_group['quantity']) {
                                return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                            }
                            $eligible_products_list = CartRule::array_uintersect($eligible_products_list, $matching_products_list);
                            break;
                            case "suppliers":
                                $cart_suppliers = Db::getInstance()->executeS('
                                    SELECT cp.quantity, cp.`id_product`, p.`id_supplier`
                                    FROM `'._DB_PREFIX_.'cart_product` cp
                                LEFT JOIN `'._DB_PREFIX_.'product` p ON cp.id_product = p.id_product
                                WHERE cp.`id_cart` = '.(int)$context->cart->id.'
                                AND cp.`id_product` IN ('.implode(',', array_map('intval', $eligible_products_list)).')');
                                $count_matching_products = 0;
                                $matching_products_list = array();
                                foreach ($cart_suppliers as $cart_supplier) {
                                if (in_array($cart_supplier['id_supplier'], $product_rule['values'])) {
                                    $count_matching_products += $cart_supplier['quantity'];
                                    $matching_products_list[] = $cart_supplier['id_product'].'-0';
                                }
                            }
                            if ($count_matching_products < $product_rule_group['quantity']) {
                                return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                            }
                            $eligible_products_list = CartRule::array_uintersect($eligible_products_list, $matching_products_list);
                            break;
                        }

                        if (!count($eligible_products_list)) {
                            return (!displayError) ? false : Tools::displayError('You cannot use this voucher with these products');
                        }
                    }
                    $selected_products = array_merge($selected_products, $eligible_products_list);
                }
            }

            if ($return_products) {
                return $selected_products;
            }
            return (!displayError) ? true : false;
        }
    }
}