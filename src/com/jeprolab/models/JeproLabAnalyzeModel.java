package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabAnalyzeModel extends JeproLabModel{
    public int analyze_id;

    public int language_id;

    public int laboratory_id;

    /** @var string Tax name */
    public String tax_name;

    /** @var string Tax rate */
    public float tax_rate;

    /** @var int Manufacturer id */
    public int manufacturer_id;

    /** @var int Supplier id */
    public int supplier_id;

    public int technician_id;

    /** @var int default Category id */
    public int default_category_id;

    /** @var int default Shop id */
    public int default_laboratory_id;

    /** @var string Manufacturer name */
    public String manufacturer_name;

    /** @var string Supplier name */
    public String supplier_name;

    /** @var string Name */
    public Map<String, String> name;

    /** @var string Long description */
    public Map<String, String> description;

    /** @var string Short description */
    public Map<String, String> short_description;

    /** @var int Quantity available */
    public int quantity = 0;

    /** @var float Price in euros */
    public float price = 0;

    public JeproLabPriceModel.JeproLabSpecificPriceModel specific_price = null;

    /** @var float Additional shipping cost */
    public float additional_shipping_cost = 0;

    /** @var float Wholesale Price in euros */
    public float wholesale_price = 0;

    /** @var bool on_sale */
    public boolean on_sale = false;

    /** @var bool online_only */
    public boolean online_only = false;

    /** @var string unity */
    public String unity = null;

    /** @var float price for analyze's unity */
    public float unit_price;

    /** @var float price for analyze's unity ratio */
    public float unit_price_ratio = 0;

    /** @var float EcoTax */
    public float eco_tax = 0;

    /** @var string Reference */
    public String reference;

    /** @var string Supplier Reference */
    public String supplier_reference;

    /** @var string Location */
    public String location;

    /** @var string Width in default width unit */
    public double width = 0;

    /** @var string Height in default height unit */
    public double height = 0;

    /** @var string Depth in default depth unit */
    public double depth = 0;

    /** @var string Weight in default weight unit */
    public double weight = 0;

    /** @var string Ean-13 barcode */
    public String ean13;

    /** @var string Upc barcode */
    public String upc;

    /** @var string Friendly URL */
    public Map<String, String> link_rewrite;

    /** @var string Meta tag description */
    public Map<String, String> meta_description;

    /** @var string Meta tag keywords */
    public Map<String, String> meta_keywords;

    /** @var string Meta tag title */
    public Map<String, String> meta_title;

    /** @var bool Product status */
    public int quantity_discount = 0;

    /** @var bool Product customization */
    public boolean customizable;

    /** @var bool Product is new */
    public boolean is_new = false;

    /** @var int Number of uploadable files (concerning customizable analyzes) */
    public int uploadable_files;

    /** @var int Number of text fields */
    public int text_fields;

    /** @var bool Product status */
    public boolean published = true;

    public boolean get_lab_from_context = false;

    public String redirect_type = "";

    /** @var bool Product statuts */
    public int analyze_redirected_id = 0;

    /** @var bool Product available for order */
    public boolean available_for_order = true;

    /** @var string Object available order date */
    public Date available_date; //= '0000-00-00';

    /** @var int delay */
    public int delay;

    /** @var bool Show price of Product */
    public boolean show_price = true;

    /** @var bool is the analyze indexed in the search index? * /
    public $indexed = 0;

    /** @var string ENUM('both', 'catalog', 'search', 'none') front office visibility */
    public String visibility = "both";

    /** @var string Object creation date */
    public Date date_add;

    /** @var string Object last modification date */
    public Date date_upd;

    /*** @var array Tags */
    public Map<String, String> tags;

    /**
     * @var float Base price of the analyze
     */
    public float base_price;

    public int tax_rules_group_id = 1;

    /**
     * @since 1.5.0
     * @var bool Tells if the analyze uses the advanced stock management
     */
    public boolean advanced_stock_management = false;
    public boolean out_of_stock;
    public boolean depends_on_stock;

    public boolean isFullyLoaded = false;

    public boolean cache_is_pack;
    public boolean cache_has_attachments;
    public boolean is_virtual;
    public int analyze_attribute_id;
    public int analyze_pack_attribute_id;
    public int cache_default_attribute;
    private List<Integer> laboratory_list_id = new ArrayList<>();
    private Map<Integer, JeproLabLanguageModel> languages = null;

    /**
     * @var string If analyze is populated, this property contain the rewrite link of the default category
     */
    public String category_name;

    /**
     * @var int tell the type of stock management to apply on the pack
     */
    public int pack_stock_type = 3;
    public int pack_analyze_attribute_id = 0;
    public int pack_quantity = 0;

    public static int _taxCalculationMethod = 0;
    protected static Map<String, Float> _prices = new HashMap<>();
    protected static Map<String, Map<Integer, Map<String,Float>>> _pricesLevel2 = new HashMap<>();
    //protected static $_incat = array();

    protected static Map<Integer, Map<Integer, Integer>> combinations = new HashMap<>();

    protected static boolean stock_management = false;
    protected static boolean order_out_of_stock = false;

    private static JeproLabPriceModel.JeproLabSpecificPriceModel static_specific_price;

    protected static boolean multi_lang_lab = true;
    protected static JeproLabAddressModel address = null;
    protected static JeproLabContext static_context = null;

    /**
     * Note:  prefix is "ANALYZE_TYPE" because TYPE_ is used in ObjectModel (definition)
     */
    public static final int SIMPLE_ANALYZE = 0;
    public static final int PACK_ANALYZE = 1;

    public JeproLabAnalyzeModel(){
        this(0, false, 0, 0, null);
    }

    public JeproLabAnalyzeModel(int analyzeId){
        this(analyzeId, false, 0, 0, null);
    }

    public JeproLabAnalyzeModel(int analyzeId, boolean full){
        this(analyzeId, full, 0, 0, null);
    }

    public JeproLabAnalyzeModel(int analyzeId, boolean full, int langId){
        this(analyzeId, full, langId, 0, null);
    }

    public JeproLabAnalyzeModel(int analyzeId, boolean full, int langId, int labId){
        this(analyzeId, full, langId, labId, null);
    }

    public JeproLabAnalyzeModel(int analyzeId, boolean full, int langId, int labId, JeproLabContext context){
        if (langId > 0 ) {
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId)) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if (labId > 0) {
            this.laboratory_id  = labId;
            this.get_lab_from_context = false;
        }

        if (this.laboratory_id <= 0) {
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        if (analyzeId > 0) {
            String cacheKey = "jeprolab_analyze_model_" + analyzeId + "_" + (full ? "true" : "false") + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT analyze_item.* FROM " + dataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze_item ";
                String where = "";
                if(langId > 0){
                    query += "LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (";
                    query += "analyze_item.analyze_id = analyze_lang.analyze_id AND analyze_lang.lang_id = " + langId + ")";
                    if(this.laboratory_id > 0 && JeproLabAnalyzeModel.multi_lang_lab){
                        where = " AND analyze_lang.lab_id = " + this.laboratory_id;
                    }
                }
                if(JeproLabLaboratoryModel.isTableAssociated("analyze")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyze_item.analyze_id = ";
                    query += " analyze_lab.analyze_id AND analyze_lab.lab_id = " + this.laboratory_id +  ")";
                }
                query += " WHERE analyze_item.analyze_id = " + analyzeId + where;

                dataBaseObject.setQuery(query);
                ResultSet analyzeResult = dataBaseObject.loadObjectList();

                if(analyzeResult != null){
                    try {
                        if (analyzeResult.next()) {
                            this.analyze_id = analyzeResult.getInt("analyze_id");
                            this.supplier_id = analyzeResult.getInt("supplier_id");
                            //this.technician_id = analyzeResult.getInt("technician_id");
                            this.manufacturer_id = analyzeResult.getInt("manufacturer_id");
                            this.default_category_id = analyzeResult.getInt("default_category_id");
                            this.default_laboratory_id = analyzeResult.getInt("default_lab_id");
                            this.tax_rules_group_id = analyzeResult.getInt("tax_rules_group_id");
                            this.on_sale = analyzeResult.getInt("on_sale") > 0;
                            this.online_only = analyzeResult.getInt("online_only") > 0;
                            this.ean13 = analyzeResult.getString("ean13");
                            this.upc = analyzeResult.getString("upc");
                            this.unity = analyzeResult.getString("unity");
                            this.unit_price = analyzeResult.getFloat("price");
                            this.unit_price_ratio = analyzeResult.getFloat("unit_price_ratio");
                            this.additional_shipping_cost = analyzeResult.getFloat("additional_shipping_cost");
                            this.reference = analyzeResult.getString("reference");
                            this.supplier_reference = analyzeResult.getString("supplier_reference");
                            this.location = analyzeResult.getString("location");
                            this.width = analyzeResult.getFloat("width");
                            this.height = analyzeResult.getFloat("height");
                            this.weight = analyzeResult.getFloat("weight");
                            this.out_of_stock = analyzeResult.getInt("out_of_stock") > 0;
                            this.quantity_discount = analyzeResult.getInt("quantity_discount");
                            this.customizable = analyzeResult.getInt("customizable") > 0;
                            this.uploadable_files = analyzeResult.getInt("uploadable_files");
                            this.text_fields = analyzeResult.getInt("text_fields");
                            this.delay = analyzeResult.getInt("delay");
                            this.published = analyzeResult.getInt("published") > 0;

                            this.name = new HashMap<>();
                            this.short_description = new HashMap<>();
                            this.description = new HashMap<>();
                            this.meta_keywords = new HashMap<>();
                            this.meta_description = new HashMap<>();
                            this.meta_title = new HashMap<>();
                            this.link_rewrite = new HashMap<>();

                            if(langId <= 0) {
                                if (languages == null) {
                                    languages = JeproLabLanguageModel.getLanguages();
                                }
                                query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_lang") + " WHERE analyze_id = ";
                                query += analyzeId + ((this.laboratory_id > 0 ) ? " AND " + dataBaseObject.quoteName("lab_id") + " = " + this.laboratory_id : "");
                                dataBaseObject.setQuery(query);
                                ResultSet analyzeLangSet = dataBaseObject.loadObjectList();
                                if(analyzeLangSet != null) {
                                    while(analyzeLangSet.next()) {
                                        for (Object o : languages.entrySet()) {
                                            Map.Entry lang = (Map.Entry) o;
                                            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                                            if(analyzeLangSet.getInt("lang_id") == language.language_id){
                                                this.name.put("lang_" + language.language_id, analyzeLangSet.getString("name"));
                                                this.description.put("lang_" + language.language_id, analyzeLangSet.getString("description"));
                                                this.short_description.put("lang_" + language.language_id, analyzeLangSet.getString("short_description"));
                                                this.meta_description.put("lang_" + language.language_id, analyzeLangSet.getString("meta_description"));
                                                this.meta_keywords.put("lang_" + language.language_id, analyzeLangSet.getString("meta_keywords"));
                                                //this.meta_description.put("lang_" + language.language_id, analyzeResult.getString("name"));
                                                this.meta_title.put("lang_" + language.language_id, analyzeLangSet.getString("meta_title"));
                                                this.link_rewrite.put("lang_" + language.language_id, analyzeLangSet.getString("link_rewrite"));
                                            }
                                        }
                                    }

                                }
                            }else{
                                this.name.put("lang_" + langId, analyzeResult.getString("name"));
                                this.description.put("lang_" + langId, analyzeResult.getString("description"));
                                this.short_description.put("lang_" + langId, analyzeResult.getString("short_description"));
                                this.meta_description.put("lang_" + langId, analyzeResult.getString("meta_description"));
                                this.meta_keywords.put("lang_" + langId, analyzeResult.getString("meta_keywords"));
                                //this.meta_description.put("lang_" + langId, analyzeResult.getString("name"));
                                this.meta_title.put("lang_" + langId, analyzeResult.getString("meta_title"));
                                this.link_rewrite.put("lang_" + langId, analyzeResult.getString("link_rewrite"));
                            }
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
                }
            }else{
                JeproLabAnalyzeModel analyzeModel = (JeproLabAnalyzeModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.analyze_id = analyzeModel.analyze_id;
                this.supplier_id = analyzeModel.supplier_id;
                this.technician_id = analyzeModel.technician_id;
                this.manufacturer_id = analyzeModel.manufacturer_id;
                this.default_category_id = analyzeModel.default_category_id;
                this.default_laboratory_id = analyzeModel.default_laboratory_id;
                this.tax_rules_group_id = analyzeModel.tax_rules_group_id;
                this.on_sale = analyzeModel.on_sale;
                this.online_only = analyzeModel.online_only;
                this.ean13 = analyzeModel.ean13;
                this.upc = analyzeModel.upc;
                this.unity = analyzeModel.unity;
                this.unit_price = analyzeModel.unit_price;
                this.unit_price_ratio = analyzeModel.unit_price_ratio;
                this.additional_shipping_cost = analyzeModel.additional_shipping_cost;
                this.reference = analyzeModel.reference;
                this.supplier_reference = analyzeModel.supplier_reference;
                this.location = analyzeModel.location;
                this.width = analyzeModel.width;
                this.height = analyzeModel.height;
                this.weight = analyzeModel.weight;
                this.out_of_stock = analyzeModel.out_of_stock;
                this.quantity_discount = analyzeModel.quantity_discount;
                this.customizable = analyzeModel.customizable;
                this.uploadable_files = analyzeModel.uploadable_files;
                this.text_fields = analyzeModel.text_fields;
                this.published = analyzeModel.published;
                this.name = analyzeModel.name;
                this.short_description = analyzeModel.short_description;
                this.description = analyzeModel.description;
                this.meta_keywords = analyzeModel.meta_keywords;
                this.meta_description = analyzeModel.meta_description;
                this.meta_title = analyzeModel.meta_title;
                this.link_rewrite = analyzeModel.link_rewrite;
            }

        }

        if (full && this.analyze_id > 0) {
            if (context == null) {
                context = JeproLabContext.getContext();
            }

            this.isFullyLoaded = full;
            //this.tax_name = 'deprecated'; // The applicable JeproLabTaxManagerFactory may be BOTH the analyze one AND the state one (moreover this variable is some deadcode)
            //this.manufacturer_name = JeproLabManufacturerModel.getNameById(this.manufacturer_id);
            //this.supplier_name = JeproLabSupplierModel.getNameById(this.supplier_id);
            int addressId = 0;
            if(context.cart != null){
                if(JeproLabSettingModel.getStringValue("tax_address_type").equals("invoice_address_id") && context.cart.invoice_address_id > 0) {
                    addressId = context.cart.invoice_address_id;
                }else if(JeproLabSettingModel.getStringValue("tax_address_type").equals("delivery_address_id") && context.cart.delivery_address_id > 0){
                    addressId = context.cart.delivery_address_id;
                }
            }
            this.tax_rate = this.getTaxesRate(new JeproLabAddressModel(addressId));

            this.is_new = this.isNew();

            // Keep base price
            this.base_price = this.price;

            this.price = JeproLabAnalyzeModel.getStaticPrice(this.analyze_id, false, 0, 6, false, true, 1, false, 0, 0, 0);
            specific_price = JeproLabAnalyzeModel.static_specific_price;
            this.unit_price = (this.unit_price_ratio != 0  ? this.price / this.unit_price_ratio : 0);
            if (this.analyze_id > 0) {
                this.tags = JeproLabTagModel.getAnalyzeTags(this.analyze_id);
            }

            //this.loadStockData();
        }

        if (this.default_category_id > 0) {
            this.category_name = JeproLabCategoryModel.getLinkRewrite(this.default_category_id, langId);
        }
    }

    public boolean isNew(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT analyze_item.analyze_id FROM " + dataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze_item ";
        query += JeproLabLaboratoryModel.addSqlAssociation("analyze") + " WHERE analyze_item.analyze_id = " + this.analyze_id ;
        query += " AND DATEDIFF( analyze_lab." + dataBaseObject.quoteName("date_add") + ", DATE_SUB('" + JeproLabTools.date("Y-m-d");
        query += " 00:00:00' , INTERVAL " + (JeproLabSettingModel.getIntValue("number") > 0 ? JeproLabSettingModel.getIntValue("") : 20);
        query += " DAY ) ) > 0 ";

        dataBaseObject.setQuery(query);
        ResultSet analyzes = dataBaseObject.loadObjectList();
        int total = 0;
        try{
            while (analyzes.next()){
                total += 1;
            }
        }catch (SQLException ignored){

        }
        return total > 0;
    }

    public static float getStaticPrice(int analyzeId){
        return getStaticPrice(analyzeId, true, 0, 6, false, true, 1, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax){
        return getStaticPrice(analyzeId, useTax, 0, 6, false, true, 1, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, 6, false, true, 1, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals = 6, false, true, 1, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, true, 1, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, 1, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, quantity, false, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity, boolean forceAssociateTax){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,onlyReduction,  useReduction, quantity, forceAssociateTax, 0, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity, boolean forceAssociateTax, int customerId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, 0, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, 0, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId, int addressId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, addressId, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId, int addressId, boolean withEcoTax){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, addressId, withEcoTax, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, boolean withEcoTax, boolean useGroupReduction){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction , quantity, forceAssociatedTax, customerId, cartId, addressId, withEcoTax, useGroupReduction, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, boolean withEcoTax, boolean useGroupReduction, JeproLabContext context){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, quantity, forceAssociatedTax, customerId, cartId, addressId, withEcoTax, useGroupReduction, context, true);
    }

    /**
     * Returns analyze price
     *
     * @param analyzeId  Analyze id
     * @param useTax     With taxes or not (optional)
     * @param analyzeAttributeId  Analyze attribute id (optional).
     *                            If set to false, do not apply the combination price impact.
     *                            NULL does apply the default combination price impact.
     * @param decimals            Number of decimals (optional)
     * @param onlyReduction       Returns only the reduction amount
     * @param useReduction        Set if the returned amount will include reduction
     * @param quantity            Required for quantity discount application (default value: 1)
     * @param forceAssociatedTax  DEPRECATED - NOT USED Force to apply the associated JeproLabTaxManagerFactory.
     *                                        Only works when the parameter $usetax is true
     * @param customerId           Customer ID (for customer group reduction)
     * @param cartId               Cart ID. Required when the cookie is not accessible
     *                                        (e.g., inside a payment module, a cron task...)
     * @param addressId            Customer address ID. Required for price (JeproLabTaxManagerFactory included)
     *                                        calculation regarding the guest localization
     *
     * @param withEcoTax           Insert ecotax in price output.
     * @param useGroupReduction  use reduction based on customer group
     * @param context             Current context
     * @param useCustomerPrice  use customer price
     * @return float             Analyze price
     */
    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, boolean withEcoTax, boolean useGroupReduction, JeproLabContext context, boolean useCustomerPrice){
        if (context == null){
            context = JeproLabContext.getContext();
        }

        JeproLabCartModel currentCart = context.cart;

        if (analyzeId <= 0){
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        // Initializations
        int groupId = 0;
        if (customerId > 0) {
            groupId = JeproLabCustomerModel.getDefaultGroupId(customerId);
        }
        if (groupId <= 0) {
            groupId = JeproLabGroupModel.getCurrent().group_id;
        }

        // If there is cart in context or if the specified id_cart is different from the context cart id
        if ((currentCart == null) || (cartId > 0 && currentCart.cart_id != cartId)) {
            /*
            * When a user (e.g., guest, customer, Google...) is on PrestaShop, he has already its cart as the global (see /init.php)
            * When a non-user calls directly this method (e.g., payment module...) is on PrestaShop, he does not have already it BUT knows the cart ID
            * When called from the back office, cart ID can be in-existent
            */
            if (cartId > 0 && context.employee != null) {
                JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_LABEL"));
            }
            currentCart = new JeproLabCartModel(cartId);
            // Store cart in context to avoid multiple instantiations in BO
            if (context.cart.cart_id <= 0) {
                context.cart = currentCart;
            }
        }

        int cartQuantity = 0;
        if (cartId > 0) {
            String cacheKey = "jeprolab_analyze_get_static_price_" + analyzeId + "_" + cartId;
            cartQuantity = (int)JeproLabCache.getInstance().retrieve(cacheKey);
            if (!JeproLabCache.getInstance().isStored(cacheKey) || (cartQuantity != quantity)) {
                if(staticDataBaseObject == null){
                    staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT SUM(" + staticDataBaseObject.quoteName("quantity") + ") AS quantity FROM " + staticDataBaseObject.quoteName("#__jeprolab_cart_analyze");
                query += " AS cart_analyze WHERE "  + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND ";
                query += staticDataBaseObject.quoteName("cart_id") + " = " + cartId;

                staticDataBaseObject.setQuery(query);

                cartQuantity = (int)staticDataBaseObject.loadValue("quantity");
                JeproLabCache.getInstance().store(cacheKey, cartQuantity);
            } else {
                cartQuantity = (int)JeproLabCache.getInstance().retrieve(cacheKey);
            }
        }

        int currencyId = (context.currency.currency_id > 0 ) ? context.currency.currency_id : JeproLabSettingModel.getIntValue("default_currency");

        // retrieve address information
        int countryId = context.country.country_id;
        int stateId = 0;
        String zipCode = "";

        if (addressId <= 0 && (currentCart != null) && currentCart.cart_id > 0) {
            if(JeproLabSettingModel.getStringValue("tax_address_type").equals("address_delivery_id")) {
                addressId = currentCart.delivery_address_id;
            }else {
                addressId = currentCart.invoice_address_id;
            }
        }

        int addressInfoCountryId = 0;
        int addressInfoStateId = 0;
        String addressInfoZipCode = "";
        int addressInfoVatNumber = 0;

        if (addressId > 0) {
            ResultSet addressInfo = JeproLabAddressModel.getCountryAndState(addressId);

            try {
                while(addressInfo.next()){
                    addressInfoCountryId = addressInfo.getInt("country_id");
                    addressInfoStateId = addressInfo.getInt("state_id");
                    addressInfoZipCode = addressInfo.getString("postcode");
                    addressInfoVatNumber = addressInfo.getInt("vat_number");
                }
            }catch (SQLException ignored){

            }
            if (addressInfoCountryId > 0) {
                countryId = addressInfoCountryId;
                stateId = addressInfoStateId;
                zipCode = addressInfoZipCode;
            }
        } else if (context.customer.geolocation_country_id > 0) {
            countryId = context.customer.geolocation_country_id;
            stateId = context.customer.state_id;
            zipCode = context.customer.postcode;
        }

        if (JeproLabTaxModel.excludeTaxOption()){
            useTax = false;
        }

        if (useTax && (addressInfoVatNumber <= 0) && addressInfoCountryId != JeproLabSettingModel.getIntValue("vat_number_country") && JeproLabSettingModel.getIntValue("vat_number_management") > 0){
            useTax = false;
        }

        if ((customerId <= 0) && (context.customer != null && context.customer.customer_id > 0)){
            customerId = context.customer.customer_id;
        }

        return JeproLabAnalyzeModel.priceCalculation(
                context.laboratory.laboratory_id, analyzeId, analyzeAttributeId, countryId, stateId, zipCode, currencyId,
                groupId, quantity, useTax, decimals, onlyReduction, useReduction, withEcoTax,
                useGroupReduction, customerId, useCustomerPrice, cartId, cartQuantity
        );
    }

    public static float priceCalculation(int labId, int analyzeId, int analyzeAttributeId, int countryId, int stateId, String zipCode, int currencyId, int groupId, int quantity, boolean useTax, int decimals, boolean onlyReduction, boolean useReduction, boolean withEcoTax,  boolean useGroupReduction){
        return priceCalculation(labId, analyzeId, analyzeAttributeId, countryId, stateId, zipCode, currencyId, groupId, quantity, useTax, decimals, onlyReduction, useReduction, withEcoTax, useGroupReduction, 0, true, 0, 0);
    }

    public static float priceCalculation(int labId, int analyzeId, int analyzeAttributeId, int countryId, int stateId, String zipCode, int currencyId, int groupId, int quantity, boolean useTax, int decimals, boolean onlyReduction, boolean useReduction, boolean withEcoTax,  boolean useGroupReduction,  int customerId){
        return priceCalculation(labId, analyzeId, analyzeAttributeId, countryId, stateId, zipCode, currencyId, groupId, quantity, useTax, decimals, onlyReduction, useReduction, withEcoTax, useGroupReduction, customerId, true, 0, 0);
    }

    public static float priceCalculation(int labId, int analyzeId, int analyzeAttributeId, int countryId, int stateId, String zipCode, int currencyId, int groupId, int quantity, boolean useTax, int decimals, boolean onlyReduction, boolean useReduction, boolean withEcoTax,  boolean useGroupReduction,  int customerId, boolean useCustomerPrice){
        return priceCalculation(labId, analyzeId, analyzeAttributeId, countryId, stateId, zipCode, currencyId, groupId, quantity, useTax, decimals, onlyReduction, useReduction, withEcoTax, useGroupReduction, customerId, useCustomerPrice, 0, 0);
    }

    public static float priceCalculation(int labId, int analyzeId, int analyzeAttributeId, int countryId, int stateId, String zipCode, int currencyId, int groupId, int quantity, boolean useTax, int decimals, boolean onlyReduction, boolean useReduction, boolean withEcoTax,  boolean useGroupReduction,  int customerId, boolean useCustomerPrice, int cartId){
        return priceCalculation(labId, analyzeId, analyzeAttributeId, countryId, stateId, zipCode, currencyId, groupId, quantity, useTax, decimals, onlyReduction, useReduction, withEcoTax, useGroupReduction, customerId, useCustomerPrice, cartId, 0);
    }

    /**
     * Price calculation / Get analyze price
     *
     * @param labId  Laboratory id
     * @param analyzeId Analyze id
     * @param analyzeAttributeId analyze attribute id
     * @param countryId Country id
     * @param stateId State id
     * @param zipCode the address zip code
     * @param currencyId Currency id
     * @param groupId Group id
     * @param quantity Quantity Required for Specific prices : quantity discount application
     * @param useTax with (1) or without (0) JeproLabTaxManagerFactory
     * @param decimals Number of decimals returned
     * @param onlyReduction Returns only the reduction amount
     * @param useReduction Set if the returned amount will include reduction
     * @param withEcoTax insert ecoTax in price output.
     * @param useGroupReduction group reduction
     * @param customerId customer id
     * @param useCustomerPrice customer price
     * @param cartId cart id
     * @param realQuantity real quantity
     * @return float Product price
     **/
    public static float priceCalculation(int labId, int analyzeId, int analyzeAttributeId, int countryId, int stateId, String zipCode, int currencyId, int groupId, int quantity, boolean useTax, int decimals, boolean onlyReduction, boolean useReduction, boolean withEcoTax,  boolean useGroupReduction,  int customerId, boolean useCustomerPrice, int cartId, int realQuantity){
        if (address == null) {
            address = new JeproLabAddressModel();
        }

        if (static_context == null) {
            static_context = JeproLabContext.getContext().clone();
        }

        if (labId > 0 && static_context.laboratory.laboratory_id != labId) {
            static_context.laboratory = new JeproLabLaboratoryModel(labId);
        }

        if (!useCustomerPrice) {
            customerId = 0;
        }

        if (analyzeAttributeId <= 0) {
            analyzeAttributeId = JeproLabAnalyzeModel.getDefaultAttribute(analyzeId);
        }

        JeproLabContext context = JeproLabContext.getContext();

        String cacheKey = analyzeId + "_" + labId +"_" + currencyId + "_" + countryId + "_" + stateId + "_" + zipCode + "_" + groupId;
        cacheKey += "_" + quantity + "_" + analyzeAttributeId + "_" + (withEcoTax ? 1 : 0 ) + "_" + customerId + "_";
        cacheKey += (useGroupReduction ? 1 : 0) + "_" + cartId + "_" + realQuantity +  "_" + (onlyReduction ? "1" :"0") + "_" + (useReduction ? 1 :0);
        cacheKey += "_" + (useTax ? 1 : 0) + "_" + decimals;

        // reference parameter is filled before any returns
        static_specific_price = JeproLabPriceModel.JeproLabSpecificPriceModel.getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, customerId, cartId, realQuantity);

        if (JeproLabAnalyzeModel._prices.containsKey(cacheKey)){
            /* Affect reference before returning cache */
            if (static_specific_price.price > 0){
                static_specific_price.price = JeproLabAnalyzeModel._prices.get(cacheKey);
            }
            return JeproLabAnalyzeModel._prices.get(cacheKey);
        }

        // fetch price & attribute price
        String cacheKey_2 = analyzeId + "_" + labId;
        if (!JeproLabAnalyzeModel._pricesLevel2.containsKey(cacheKey_2)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String select = "SELECT analyze_lab." + staticDataBaseObject.quoteName("price") + ", analyze_lab." + staticDataBaseObject.quoteName("ecotax");
            String from = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze_item";
            String innerJoin = " INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyze.analyze_id = ";
            innerJoin += " analyze_lab.analyze_id AND analyze_lab.lab_id = " + labId + ") ";
            String where = " WHERE analyze_item." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
            String leftJoin = "";
            if (JeproLabCombinationModel.isFeaturePublished()) {
                select += "IFNULL(analyze_attribute_lab.analyze_attribute_id, 0) AS analyze_attribute_id, analyze_attribute_lab.";
                select += staticDataBaseObject.quoteName("price") + " AS attribute_price, analyze_attribute_lab.default_on ";
                leftJoin += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_lab") + " AS analyze_attribute_lab ON (";
                leftJoin += "analyze_attribute_lab.analyze_id = analyze_item.analyze_id AND analyze_attribute_lab.lab_id = " + labId + ")";
            } else {
                select += " 0 as analyze_attribute_id ";
            }
            String query = select + from + leftJoin + innerJoin + where;
            staticDataBaseObject.setQuery(query);
            ResultSet results = staticDataBaseObject.loadObjectList();

            try {
                int resultAnalyzeAttributeId;
                Map<String, Float> result;
                Map<Integer, Map<String, Float>> resultList = new HashMap<>();
                while(results.next()){
                    result = new HashMap<>();
                    resultAnalyzeAttributeId = results.getInt("analyze_attribute_id");
                    result.put("price", results.getFloat("price"));
                    result.put("ecotax", results.getFloat("ecotax"));
                    result.put("attribute_price", results.getFloat("attribute_price"));
                    result.put("default_on", (results.getInt("default_on") > 0 ) ? (float)1.0 : (float)0.0);
                    resultList.put(resultAnalyzeAttributeId, result);

                }
                JeproLabAnalyzeModel._pricesLevel2.put(cacheKey_2, resultList);
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!JeproLabAnalyzeModel._pricesLevel2.get(cacheKey_2).containsKey(analyzeAttributeId)) {
            return 0;
        }

        Map<String, Float> result = JeproLabAnalyzeModel._pricesLevel2.get(cacheKey_2).get(analyzeAttributeId);
        float price;
        if (static_specific_price == null || static_specific_price.price < 0) {
            price = result.get("price");
        } else {
            price = static_specific_price.price;
        }
        // convert only if the specific price is in the default currency (id_currency = 0)
        if (static_specific_price == null || !(static_specific_price.price >= 0 && static_specific_price.currency_id > 0)) {
            price = JeproLabTools.convertPrice(price, currencyId);
            if (static_specific_price.price > 0) {
                static_specific_price.price = price;
            }
        }

        // Attribute price
        if (result != null && (static_specific_price == null || static_specific_price.analyze_attribute_id <= 0 || static_specific_price.price < 0)) {
            float resultAttributePrice = result.get("attribute_price") > 0 ? result.get("attribute_price") : 0;
            float attributePrice = JeproLabTools.convertPrice( resultAttributePrice, currencyId);
            // If you want the default combination, please use NULL value instead
            if (analyzeAttributeId > 0) {
                price += attributePrice;
            }
        }

        // Tax
        address.country_id = countryId;
        address.state_id = stateId;
        address.postcode = zipCode;

        JeproLabTaxModel.JeproLabTaxRulesManager taxManager = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyzeId, context));
        JeproLabTaxModel.JeproLabTaxCalculator analyzeTaxCalculator = taxManager.getTaxCalculator();

        // Add Tax
        if (useTax) {
            price = analyzeTaxCalculator.addTaxes(price);
        }

        // Eco Tax
        float ecoTax;
        if (result != null && ((result.get("ecotax") != null && result.get("ecotax") > 0) || result.get("attribute_ecotax") != null) && withEcoTax) {
            ecoTax = result.get("ecotax");
            if (result.get("attribute_ecotax") != null && result.get("attribute_ecotax") > 0) {
                ecoTax = result.get("attribute_ecotax");
            }

            if (currencyId > 0) {
                ecoTax = JeproLabTools.convertPrice(ecoTax, currencyId);
            }
            if (useTax) {
                // re-init the JeproLabTaxManagerFactory manager for ecotax handling
                taxManager = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, JeproLabSettingModel.getIntValue("ecotax_tax_rules_group_id"));
                JeproLabTaxModel.JeproLabTaxCalculator ecotaxTaxCalculator = taxManager.getTaxCalculator();
                price += ecotaxTaxCalculator.addTaxes(ecoTax);
            } else {
                price += ecoTax;
            }
        }

        // Reduction
        float specificPriceReduction = 0;
        if ((onlyReduction || useReduction) && static_specific_price != null){
            if (static_specific_price.reduction_type.equals("amount")){
                float reductionAmount = static_specific_price.reduction;

                if (static_specific_price.currency_id <= 0) {
                    reductionAmount = JeproLabTools.convertPrice(reductionAmount, currencyId);
                }

                specificPriceReduction = reductionAmount;

                // Adjust taxes if required

                if (!useTax && static_specific_price.reduction_tax > 0) {
                    specificPriceReduction= analyzeTaxCalculator.removeTaxes(specificPriceReduction);
                }
                if (useTax && static_specific_price.reduction_tax <= 0) {
                    specificPriceReduction = analyzeTaxCalculator.addTaxes(specificPriceReduction);
                }
            } else {
                specificPriceReduction = price * static_specific_price.reduction;
            }
        }

        if (useReduction) {
            price -= specificPriceReduction;
        }

        // Group reduction
        if (useGroupReduction) {
            float reductionFromCategory = JeproLabGroupModel.JeproLabGroupReductionModel.getValueForAnalyze(analyzeId, groupId);
            float groupReduction = 0;
            if (reductionFromCategory > 0) {
                groupReduction = price * reductionFromCategory;
            } else { // apply group reduction if there is no group reduction for this category
                float reduction = JeproLabGroupModel.getReductionByGroupId(groupId);
                groupReduction = ( reduction != 0) ? (price * reduction / 100) : 0;
            }

            price -= groupReduction;
        }

        if (onlyReduction) {
            return JeproLabTools.roundPrice(specificPriceReduction, decimals);
        }

        price = JeproLabTools.roundPrice(price, decimals);

        if (price < 0) {
            price = 0;
        }

        JeproLabAnalyzeModel._prices.put(cacheKey, price);
        return JeproLabAnalyzeModel._prices.get(cacheKey);
    }


    public float getTaxesRate(){
        return getTaxesRate(null);
    }

    /**
     * Returns JeproLabTaxManagerFactory rate.
     *
     * @param address addres to get tax from
     * @return float The total taxes rate applied to the analyze
     */
    public float getTaxesRate(JeproLabAddressModel address) {
        if (address != null && address.country_id <= 0) {
            address = JeproLabAddressModel.initialize();
        }

        JeproLabTaxModel.JeproLabTaxRulesManager taxManager = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, this.tax_rules_group_id);
        JeproLabTaxModel.JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }

    public static int getTaxRulesGroupIdByAnalyzeId(int analyzeId){
        return getTaxRulesGroupIdByAnalyzeId(analyzeId, null);
    }

    public static int getTaxRulesGroupIdByAnalyzeId(int analyzeId, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        String cacheKey = "jeprolab_analyze_tax_rules_group_id_" + analyzeId + "_" + context.laboratory.laboratory_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("tax_rules_group_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lab");
            query +=" WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND lab_id = " + context.laboratory.laboratory_id;
            staticDataBaseObject.setQuery(query);
            int result = (int)staticDataBaseObject.loadValue("tax_rules_group_id");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static int getDefaultAttribute(int analyzeId){
        return getDefaultAttribute(analyzeId, 0, false);
    }

    public static int getDefaultAttribute(int analyzeId, int minimumQuantity){
        return getDefaultAttribute(analyzeId, minimumQuantity, false);
    }
    /**
     * Get the default attribute for a analyze
     *
     * @return int Attributes list
     */
    public static int getDefaultAttribute(int analyzeId, int minimumQuantity, boolean reset){
        if (!JeproLabCombinationModel.isFeaturePublished()) {
            return 0;
        }

        if (reset && combinations.containsKey(analyzeId)){
            combinations.remove(analyzeId);
        }

        if (!combinations.containsKey(analyzeId)) {
            combinations.put(analyzeId, new HashMap<>());
        }
        if (combinations.get(analyzeId).containsKey(minimumQuantity)) {
            return combinations.get(analyzeId).get(minimumQuantity);
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT analyze_attribute.analyze_attribute_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute");
        query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute") + " WHERE analyze_attribute.analyze_id = " + analyzeId;

        staticDataBaseObject.setQuery(query);
        int resultNoFilter = (int)staticDataBaseObject.loadValue("analyze_attribute_id");
        if (resultNoFilter <= 0) {
            combinations.get(analyzeId).put(minimumQuantity, 0);
            return 0;
        }

        query = "SELECT analyze_attribute_lab.analyze_attribute_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute");
        query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
        query += (minimumQuantity > 0 ? JeproLabAnalyzeModel.queryStock("analyze_attribute") : "") + " WHERE analyze_attribute_lab.default_on = 1";
        query += (minimumQuantity > 0 ? " AND IFNULL (stock.quantity, 0) >= " + minimumQuantity : "") + " AND analyze_attribute.analyze_id = " + analyzeId;

        staticDataBaseObject.setQuery(query);
        int result = (int)staticDataBaseObject.loadValue("analyze_attribute_id");

        if (result <= 0) {
            query = "SELECT analyze_attribute_lab.analyze_attribute_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute");
            query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
            query += (minimumQuantity > 0 ? JeproLabAnalyzeModel.queryStock("analyze_attribute") : "") + " WHERE  analyze_attribute.analyze_id =" + analyzeId;
            query += (minimumQuantity > 0 ? " AND IFNULL (stock.quantity, 0) >= " + minimumQuantity : "");
            staticDataBaseObject.setQuery(query);
            result = (int)staticDataBaseObject.loadValue("analyze_attribute_id");
        }

        if (result <= 0) {
            query = "SELECT analyze_attribute_lab.analyze_attribute_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute");
            query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
            query += (minimumQuantity > 0 ? JeproLabAnalyzeModel.queryStock("analyze_attribute") : "") + " WHERE analyze_attribute_lab.default_on = 1";
            query += " AND analyze_attribute.analyze_id = " + analyzeId;

            staticDataBaseObject.setQuery(query);
            result = (int)staticDataBaseObject.loadValue("analyze_attribute_id");
        }

        if (result <= 0) {
            result = resultNoFilter;
        }

        combinations.get(analyzeId).put(minimumQuantity, result);
        return result;
    }

    public boolean isAssociatedToLaboratory(){
        return isAssociatedToLaboratory(0);
    }

    /**
     * Checks if current object is associated to a laboratory.
     *
     * @param labId laboratory Id
     * @return bool
     */
    public boolean isAssociatedToLaboratory(int labId){
        if (labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        boolean associated = false;
        String cacheKey = "jeprolab_model_lab_analyze_" + this.analyze_id + "_" + labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if (dataBaseObject == null) {
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT lab_id FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_lab") + " WHERE " + dataBaseObject.quoteName("analyze_id");
            query += " = " + this.analyze_id + " AND lab_id = " + labId;
            dataBaseObject.setQuery(query);
            associated = dataBaseObject.loadValue("lab_id") > 0;


            JeproLabCache.getInstance().store(cacheKey, associated);
            return associated;
        }

        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static String queryStock(String analyzeAlias){
        return queryStock(analyzeAlias, null, false, null);
    }

    public static String queryStock(String analyzeAlias, Object analyzeAttribute){
        return queryStock(analyzeAlias, analyzeAttribute, false, null);
    }

    public static String queryStock(String analyzeAlias, Object analyzeAttribute, boolean innerJoin){
        return queryStock(analyzeAlias,analyzeAttribute, innerJoin, null);
    }

    /**
     * Create JOIN query with 'stock_available' table
     *
     * @param analyzeAlias Alias of analyze table
     * @param analyzeAttribute If string : alias of PA table ; if int : value of PA ; if null : nothing about PA
     * @param innerJoin LEFT JOIN or INNER JOIN
     * @param lab
     * @return string
     */
    public static String queryStock(String analyzeAlias, Object analyzeAttribute, boolean innerJoin, JeproLabLaboratoryModel lab){
        String query = ((innerJoin) ? " INNER " : " LEFT ") + "JOIN " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " AS stock ";
        query += " ON (stock.analyze_id = " + analyzeAlias + ".analyze_id";

        if (analyzeAttribute != null){
            if (!JeproLabCombinationModel.isFeaturePublished()) {
                query += " AND stock.analyze_attribute_id = 0";
            } else if (analyzeAttribute instanceof Integer) {
                query += " AND stock.analyze_attribute_id = " + analyzeAttribute;
            } else if (analyzeAttribute instanceof String) {
                query += " AND stock.analyze_attribute_id = IFNULL(`'.bqSQL($analyze_attribute).'`.analyze_attribute_id, 0)";
            }
        }

        query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(lab, "stock") + ") ";

        return query;
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(){
        return getAnalyzeList(null);
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(JeproLabContext context){
        Map<String, String> request = JeproLab.request.getRequest();

        if(context == null){ context = JeproLabContext.getContext(); }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        int limit = request.containsKey("limit") ? Integer.parseInt(request.get("limit")) : 20;
        int limitStart = request.containsKey("limit_start") ? Integer.parseInt(request.get("limit_start")) : 1;
        int langId = request.containsKey("lang_id") ? Integer.parseInt(request.get("lang_id")) : context.language.language_id;
        int labId = request.containsKey("lab_id") ? Integer.parseInt(request.get("lab_id")) : context.laboratory.laboratory_id;
        //$lab_group_id = $app->getUserStateFromRequest($option. $view. '.lab_group_id', 'lab_group_id', $context->lab->lab_group_id, 'int');
        //$category_id = $app->getUserStateFromRequest($option. $view. '.cat_id', 'cat_id', 0, 'int');
        String orderBy = request.containsKey("order_by") ? request.get("order_by") : "date_add";
        String orderWay = request.containsKey("order_way") ? request.get("order_way") : "ASC";
        boolean publishedAnalyze = request.containsKey("published") ? Integer.parseInt(request.get("published")) > 0 : false;
        //$analyze_attribute_id = $app->getUserStateFromRequest($option. $view. '.analyze_attribute_id', 'analyze_attribute_id', 0, 'int');

        JeproLabCategoryModel category;

        if(JeproLabLaboratoryModel.isFeaturePublished() && context.cookie.analyzes_filter_category_id > 0){
            category = new JeproLabCategoryModel(context.cookie.analyzes_filter_category_id);
            if(!category.existsInLaboratories(context.laboratory.laboratory_id)){
                context.cookie.analyzes_filter_category_id = 0;
                try {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Join categories table
        int categoryId =  request.containsKey("analyze_filter_category_lang!name") ? Integer.parseInt(request.get("analyze_filter_category_lang!name")) : 0;
        int currentCategoryId = 0;
        if(categoryId > 0){
            category = new JeproLabCategoryModel(categoryId);
            request.put("analyze_filter_category_lang!name", category.name.get("lang_" + context.language.language_id));
        }else {
            categoryId = request.containsKey("category_id") ? Integer.parseInt(request.get("category_id")) : 0;

            if (categoryId > 0) {
                currentCategoryId = categoryId;
                context.cookie.analyzes_filter_category_id = categoryId;
            } else if(context.cookie.analyzes_filter_category_id > 0){
                categoryId = context.cookie.analyzes_filter_category_id;
                currentCategoryId = categoryId;
            }

            if (currentCategoryId > 0) {
                category = new JeproLabCategoryModel(currentCategoryId);
            } else {
                category = new JeproLabCategoryModel();
            }
        }
        boolean joinCategory = false;
        if(category.category_id > 0){// && empty($filter)){
            joinCategory = true;
        }
        //String groupFilter = ""; //, selectFilter = "";
        if(JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
            labId = context.laboratory.laboratory_id;
        }else{
            labId = JeproLabSettingModel.getIntValue("default_lab");
        }

        String joinFilter = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_image") + " AS image ON (image.";
        joinFilter += staticDataBaseObject.quoteName("analyze_id") + " = analyze_item." + staticDataBaseObject.quoteName("analyze_id");
        joinFilter += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_stock_available") + " AS stock_available ON (stock_available.";
        joinFilter += staticDataBaseObject.quoteName("analyze_id") + " = analyze_item." + staticDataBaseObject.quoteName("analyze_id") ;
        joinFilter += " AND stock_available." + staticDataBaseObject.quoteName("analyze_attribute_id");
        joinFilter += " = 0 " + JeproLabStockAvailableModel.addSqlLaboratoryRestriction(null, "stock_available") + ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lab");
        joinFilter += " AS analyze_lab ON (analyze_item." + staticDataBaseObject.quoteName("analyze_id") + " = analyze_lab." + staticDataBaseObject.quoteName("analyze_id")+ " AND analyze_lab." + staticDataBaseObject.quoteName("lab_id");
        joinFilter += " = " + labId + ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (";
        joinFilter += " analyze_lang." + staticDataBaseObject.quoteName("lang_id") + " = category_lang." + staticDataBaseObject.quoteName("lang_id") + " AND category_lang.";
        joinFilter += staticDataBaseObject.quoteName("lab_id") + " = " + labId + ") LEFT JOIN  " + staticDataBaseObject.quoteName("#__jeprolab_lab") + " AS lab ON (lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId;
        joinFilter += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_image_lab") + " AS image_lab ON (image_lab." + staticDataBaseObject.quoteName("image_id") + " = image." + staticDataBaseObject.quoteName("image_id");
        joinFilter += " AND image_lab." + staticDataBaseObject.quoteName("cover") + " = 1 AND image_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId + ") LEFT JOIN  " + staticDataBaseObject.quoteName("#__jeprolab_analyze_download");
        joinFilter += " AS analyze_download ON (analyze_download." + staticDataBaseObject.quoteName("analyze_id") + " = analyze_item." + staticDataBaseObject.quoteName("analyze_id") + ") ";

        String selectFilter = ", lab." + staticDataBaseObject.quoteName("lab_name") + " AS lab_name, analyze_item." + staticDataBaseObject.quoteName("default_lab_id") + ", MAX(image_lab." + staticDataBaseObject.quoteName("image_id") + ") AS image_id, category_lang.";
        selectFilter += staticDataBaseObject.quoteName("name") + " AS category_name, analyze_lab." + staticDataBaseObject.quoteName("price") + ", 0 AS final_price, analyze_item." + staticDataBaseObject.quoteName("is_virtual") + ", analyze_download.";
        selectFilter += staticDataBaseObject.quoteName("nb_downloadable") + ", stock_available." + staticDataBaseObject.quoteName("quantity") + " AS stock_available_quantity, analyze_lab." + staticDataBaseObject.quoteName("published");
        selectFilter += ", IF(stock_available." + staticDataBaseObject.quoteName("quantity") + " <= 0, 1, 0) badge_danger";

        if(joinCategory){
            joinFilter += " INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_category") + " analyze_category ON (analyze_category." +  staticDataBaseObject.quoteName("analyze_id") + " = analyze_item.";
            joinFilter +=  staticDataBaseObject.quoteName("analyze_id") + " AND analyze_category." + staticDataBaseObject.quoteName("category_id") + " = " + category.category_id + ") ";
            selectFilter += " , analyze_category." + staticDataBaseObject.quoteName("position");
        }

        String groupFilter = " GROUP BY analyze_lab." + staticDataBaseObject.quoteName("analyze_id");

        boolean useLimit = true;
        if (limit == 0) {
            useLimit = false;
        }

        // Add SQL shop restriction
        String selectLabFilter = "";
        String joinLabFilter = "";
        String whereLabFilter = "";
        String whereFilter = "";
        String filter = "";
        if (!context.controller.laboratory_link_type.equals("")){
            selectLabFilter = ", lab.lab_name AS lab_name ";
            joinLabFilter = ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_lab") + " AS lab ON analyze_item.lab_id = lab.lab_id";
            //whereLabFilter = JeproLabLaboratoryModel.addSqlRestriction($this->shopShareDatas, "analyze"); //, $this->shopLinkType
        }

        /* Query in order to get results with all fields */
        String langJoinFilter = "";
        if (langId > 0){
            langJoinFilter = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (analyze_lang.";
            langJoinFilter += staticDataBaseObject.quoteName("analyze_id") + " = analyze_item." + staticDataBaseObject.quoteName("analyze_id") + " AND analyze_lang.";
            langJoinFilter += staticDataBaseObject.quoteName("lang_id") + " = " + langId;

            if (!JeproLabLaboratoryModel.isFeaturePublished()) {
                langJoinFilter += " AND analyze_lang." + staticDataBaseObject.quoteName("lab_id") + " = 1";
            }else if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                //$langJoinFilter += " AND analyze_lang." . staticDataBaseObject.quoteName("lab_id") . " = " . (int)$shop_lang_id;
            }else{
                langJoinFilter += " AND analyze_lang." + staticDataBaseObject.quoteName("lab_id") + " = analyze_item.default_lab_id";
            }
            langJoinFilter += ")";
        }

        if (context.controller.multi_laboratory_context && JeproLabLaboratoryModel.isTableAssociated("analyze")){
            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.ALL_CONTEXT || !context.employee.isSuperAdmin()){
                boolean testJoinFilter = false; // !preg_match("/`?".preg_quote("#__jeprolab_analyze_lab") + "`? *analyze_lab/", joinFilter);
                if (JeproLabLaboratoryModel.isFeaturePublished() && testJoinFilter && JeproLabLaboratoryModel.isTableAssociated("analyze")){
                    whereFilter += " AND analyze_item.analyze_id IN (	SELECT analyze_lab.analyze_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lab");
                    List<Integer> laboratoryIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
                    String laboratories = "";
                    if(laboratoryIds.size() > 0){
                        for(int ind : laboratoryIds){
                            laboratories += ind + ", ";
                        }
                        laboratories = laboratories.endsWith(", ") ? laboratories.substring(0, laboratories.length() - 2) : laboratories;
                    }
                    whereFilter += " AS analyze_lab WHERE analyze_lab.lab_id IN (" + laboratories + "))";

                }
            }
        }

        String havingClauseFilter = "";
        /*if (isset($filterHaving) || isset($having)){
            havingClause = " HAVING ";
            if (isset($filterHaving)){
                $having_clause .= ltrim($filterHaving, " AND ");
            }
            if (isset($having)){ havingClause += $having + " "; }
        }*/
        List<JeproLabAnalyzeModel> analyzes = new ArrayList<>();
        int total = 0;

        do{
            String query = "SELECT analyze_item." + staticDataBaseObject.quoteName("analyze_id") + ", analyze_lang." + staticDataBaseObject.quoteName("name") + ", analyze_item.";
            query += staticDataBaseObject.quoteName("reference") + selectFilter + selectLabFilter + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze_item ";
            query += langJoinFilter + joinFilter + joinLabFilter + " WHERE 1 " + whereFilter + filter + whereLabFilter +  groupFilter + havingClauseFilter + " ORDER BY ";
            query += (JeproLabTools.strReplace(orderBy, "`", "").equals("analyze_id") ? "analyze_item." : " analyze_item.") + staticDataBaseObject.quoteName(orderBy) + " " + orderWay;

            staticDataBaseObject.setQuery(query);
            total = 0;

            ResultSet analyzeSet = staticDataBaseObject.loadObjectList();
            if(analyzeSet != null) {
                try {
                    while(analyzeSet.next()){ total += 1; }

                    query += (useLimit ? " LIMIT " + limitStart + ", " + limit : "");

                    staticDataBaseObject.setQuery(query);
                    analyzeSet = staticDataBaseObject.loadObjectList();
                    JeproLabAnalyzeModel analyze;
                    while(analyzeSet.next()){
                        analyze = new JeproLabAnalyzeModel();
                        analyze.analyze_id = analyzeSet.getInt("analyze_id");
                        /*analyze.supplier_id = analyzeSet.getInt("supplier_id");
                        analyze.technician_id = analyzeSet.getInt("technician_id");
                        analyze.manufacturer_id = analyzeSet.getInt("manufacturer_id");
                        analyze.default_category_id = analyzeSet.getInt("default_category_id");
                        analyze.default_laboratory_id = analyzeSet.getInt("default_laboratory_id");
                        analyze.tax_rules_group_id = analyzeSet.getInt("tax_rules_group_id"); * /
                        analyze.on_sale = analyzeSet.getInt("on_sale") > 0;
                        analyze.online_only = analyzeSet.getInt("online_only") > 0;
                        analyze.ean13 = analyzeSet.getString("ean13");
                        analyze.upc = analyzeSet.getString("upc");
                        analyze.unity = analyzeSet.getString("unity");
                        analyze.unit_price = analyzeSet.getFloat("unit_price");
                        analyze.unit_price_ratio = analyzeSet.getFloat("unit_price_ratio");
                        analyze.additional_shipping_cost = analyzeSet.getFloat("additional_shipping_cost"); */
                        analyze.reference = analyzeSet.getString("reference");
                        analyze.category_name = analyzeSet.getString("category_name");
                        /*analyze.supplier_reference = analyzeSet.getString("supplier_reference");
                        analyze.location = analyzeSet.getString("location");
                        analyze.width = analyzeSet.getFloat("width");
                        analyze.height = analyzeSet.getFloat("height");
                        analyze.weight = analyzeSet.getFloat("weight");
                        analyze.out_of_stock = analyzeSet.getInt("out_of_stock") > 0;
                        analyze.quantity_discount = analyzeSet.getInt("quantity_discount");
                        analyze.customizable = analyzeSet.getInt("customizable") > 0;
                        analyze.uploadable_files = analyzeSet.getInt("uploadable_files");
                        analyze.text_fields = analyzeSet.getInt("text_fields");
                        analyze.delay = analyzeSet.getInt("delay");*/
                        analyze.published = analyzeSet.getInt("published") > 0;
                        if(analyze.name == null){ analyze.name = new HashMap<>(); }
                        analyze.name.put("lang_" + langId, analyzeSet.getString("name"));

                        analyzes.add(analyze);
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
            }

            if(useLimit){
                limitStart = limitStart - limit;
                if(limitStart < 0){ break; }
            }else{ break; }
        }while(analyzes.isEmpty());

        //$this->pagination = new JPagination($total, $limit_start, $limit);
        return analyzes;
    }
}
