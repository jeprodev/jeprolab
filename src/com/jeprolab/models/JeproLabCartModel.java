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

    protected static Map<Integer, Float> _total_weight = new HashMap<>();
    protected int tax_calculation_method = JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED;

    protected static Map<Integer, JeproLabAnalyzeModel>_number_of_analyzes = new HashMap<>();

    public final static int ONLY_ANALYZES = 1;
    public final static int ONLY_DISCOUNTS = 2;
    public final static int BOTH = 3;
    public final static int BOTH_WITHOUT_SHIPPING = 4;
    public final static int ONLY_SHIPPING = 5;
    public final static int ONLY_WRAPPING = 6;
    public final static int ONLY_PRODUCTS_WITHOUT_SHIPPING = 7;
    public final static int ONLY_PHYSICAL_PRODUCTS_WITHOUT_SHIPPING = 8;

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

    public int getNumberOfAnalyzes(){
        return getNumberOfAnalyzes(false, 0, 0);
    }

    public int getNumberOfAnalyzes(boolean refresh){
        return getNumberOfAnalyzes(refresh, 0, 0);
    }

    public int getNumberOfAnalyzes(boolean refresh, int analyzeId){
        return getNumberOfAnalyzes(refresh, analyzeId, 0);
    }

    /**
     * Return cart products
     *
     * @result int
     */
    public int getNumberOfAnalyzes(boolean refresh, int analyzeId, int countryId){
        if (!(this.cart_id > 0)){
            return 0; //new ArrayList();
        }
        List analyzes = new ArrayList<>();
        // Analyze cache must be strictly compared to NULL, or else an empty cart will add dozens of queries
        if ((this._analyzes != null) && !refresh) {
            // Return product row with specified ID if it exists
            if (analyzeId > 0) {
                for (JeproLabAnalyzeModel analyze : this._analyzes) {
                    if (analyze.analyze_id == analyzeId) {
                        analyzes.add(analyze);
                        return analyzes.size();
                    }
                }
                return  0; //new ArrayList<>();
            }
            return this._analyzes.size();
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
        List analyzeIds = new ArrayList<>();
        List analyzeAttributeIds = new ArrayList<>();
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
        float ecotaxRate = JeproLabTaxModel.getAnalyzeEcoTaxRate(taxAddressId);
        boolean applyEcoTax = JeproLabAnalyzeModel._taxCalculationMethod == JeproLabConfigurationSettings.JEPROLAB_TAX_INCLUDED && (JeproLabSettingModel.getIntValue("use_tax") > 0);

        try{
            JeproLabContext cartLabContext = JeproLabContext.getContext().cloneContext();
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

/*
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

                    analyze.analyze_price.price_with_out_tax = priceWithReduction;
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

                    analyze = JeproLabAnalyzeModel.getTaxesInformation(analyze, cartLabContext); */

                    this._analyzes.add(analyze);
                }
            } catch (SQLException ignored) {
                ignored.printStackTrace();
            }
        }
        return this._analyzes.size();
    }
/*/
    public static function replaceZeroByLaboratoryName($echo, $tr){
        return ($echo == '0' ? JeproLabCarrierModel.getCarrierNameFromLaboratoryName() : $echo);
    } */


    public static class JeproLabCartRuleModel extends JeproLabModel{
        /* Filters used when retrieving the cart rules applied to a cart of when calculating the value of a reduction */
        public final int FILTER_ACTION_ALL = 1;
        public final int FILTER_ACTION_SHIPPING = 2;
        public final int FILTER_ACTION_REDUCTION = 3;
        public final int FILTER_ACTION_GIFT = 4;
        public final int FILTER_ACTION_ALL_NOCAP = 5;

        public final String BO_REQUEST_CODE_PREFIX = "BO_REQUEST_";

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
    }
}