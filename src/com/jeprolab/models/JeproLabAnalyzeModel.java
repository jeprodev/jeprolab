package com.jeprolab.models;

import com.jeprolab.JeproLab;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
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

    public boolean is_gift = false;

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

    public int stock_quantity = 0;

    public int minimal_quantity = 0;

    /** @var float Price in euros */
    public JeproLabPriceModel analyze_price = null;

    public JeproLabPriceModel.JeproLabSpecificPriceModel specific_price = null;

    /** @var float Additional shipping cost */
    public float additional_shipping_cost = 0;

    /** @var float Wholesale Price in euros */
    public float wholesale_price = 0;

    /** @var bool on_sale */
    public boolean on_sale = false;

    public boolean in_stock = false;

    public boolean reduction_applied;

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

    public float attribute_weight;

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

    public Map<String, String> available_now;

    public Map<String, String> available_later;

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

    public float attribute_eco_tax;

    /** @var bool Analyze status */
    public boolean published = true;

    public boolean get_lab_from_context = false;

    public String redirect_type = "";

    /** @var bool Analyze status */
    public int analyze_redirected_id = 0;

    /** @var bool Product available for order */
    public boolean available_for_order = true;

    /** @var string Object available order date */
    public Date available_date; //= '0000-00-00';

    /** @var int delay */
    public int delay;

    /** @var bool Show price of Product */
    public boolean show_price = true;

    /** @var bool is the analyze indexed in the search index? */
    public boolean indexed = false;

    /** @var string ENUM('both', 'catalog', 'search', 'none') front office visibility */
    public String visibility = "both";

    /** @var string Object creation date */
    public Date date_add;

    /** @var string Object last modification date */
    public Date date_upd;

    /*** @var array Tags */
    public Map<String, String> tags;

    public int cart_quantity = 0;

    public int delivery_address_id;

    public int customization_id = 0;

    /**
     * @var float Base price of the analyze
     */
    public float base_price;

    public int tax_rules_group_id = 1;

    public List<Integer> analyzeMethods = new ArrayList<>();

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
    public boolean cache_default_attribute;
    private List<Integer> laboratory_ids = new ArrayList<>();
    private Map<Integer, JeproLabLanguageModel> languages = null;

    /**
     * @var string If analyze is populated, this property contain the rewrite link of the default category
     */
    public String category_name;

    public String image_name;

    /**
     * @var int tell the type of stock management to apply on the pack
     */
    public int pack_stock_type = 3;
    public int pack_analyze_attribute_id = 0;
    public int pack_quantity = 0;

    public static int _tax_calculation_method = 0;
    protected static Map<String, Float> _prices = new HashMap<>();
    protected static Map<String, Map<Integer, Map<String,Float>>> _pricesLevel2 = new HashMap<>();
    //protected static $_incat = array();

    protected static Map<Integer, Map<Integer, Integer>> combinations = new HashMap<>();

    protected static boolean stock_management = false;
    protected static boolean request_out_of_stock = false;

    private static JeproLabPriceModel.JeproLabSpecificPriceModel static_specific_price;

    protected static boolean multi_lang_lab = true;
    protected static JeproLabAddressModel address = null;
    protected static JeproLabContext static_context = null;

    public static Map<Integer, List<JeproLabFeatureModel.JeproLabFeatureValueModel>> _cache_features = new HashMap<>();

    /**
     * Note:  prefix is "ANALYZE_TYPE" because TYPE_ is used in ObjectModel (definition)
     */
    public static final int SIMPLE_ANALYZE = 0;
    public static final int PACK_ANALYZE = 1;

    public static final int CUSTOMIZE_FILE = 0;
    public static final int CUSTOMIZE_TEXTFIELD = 1;

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
                String query = "SELECT analyse.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " AS analyse ";
                String where = "";
                if(langId > 0){
                    query += "LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (";
                    query += "analyse.analyze_id = analyze_lang.analyze_id AND analyze_lang.lang_id = " + langId + ")";
                    if(this.laboratory_id > 0 && JeproLabAnalyzeModel.multi_lang_lab){
                        where = " AND analyze_lang.lab_id = " + this.laboratory_id;
                    }
                }
                if(JeproLabLaboratoryModel.isTableAssociated("analyze")){
                    query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyse.analyze_id = ";
                    query += " analyze_lab.analyze_id AND analyze_lab.lab_id = " + this.laboratory_id +  ")";
                }
                query += " WHERE analyse.analyze_id = " + analyzeId + where;

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                ResultSet analyzeResult = dataBaseObject.loadObjectList(query);

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
                            this.indexed = analyzeResult.getInt("indexed") > 0;
                            this.published = analyzeResult.getInt("published") > 0;
                            this.analyzeMethods =  JeproLabMethodModel.getAnalyzeMethodIdsByAnalyzeId(this.analyze_id);

                            this.name = new HashMap<>();
                            this.short_description = new HashMap<>();
                            this.description = new HashMap<>();
                            this.meta_keywords = new HashMap<>();
                            this.meta_description = new HashMap<>();
                            this.meta_title = new HashMap<>();
                            this.link_rewrite = new HashMap<>();
                            this.available_later = new HashMap<>();
                            this.available_now = new HashMap<>();

                            this.analyze_price = new JeproLabPriceModel();

                            if(langId <= 0) {
                                if (languages == null) {
                                    languages = JeproLabLanguageModel.getLanguages();
                                }
                                query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + " WHERE analyze_id = ";
                                query += analyzeId + ((this.laboratory_id > 0 ) ? " AND " + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + this.laboratory_id : "");
                                //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                                ResultSet analyzeLangSet = dataBaseObject.loadObjectList(query);
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
                                                this.available_now.put("lang_" + language.language_id, analyzeLangSet.getString("available_now"));
                                                this.available_later.put("lang_" + language.language_id, analyzeLangSet.getString("available_later"));
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
                                this.available_later.put("lang_" + langId, analyzeResult.getString("available_later"));
                                this.available_now.put("lang_" + langId, analyzeResult.getString("available_now"));
                                this.meta_title.put("lang_" + langId, analyzeResult.getString("meta_title"));
                                this.link_rewrite.put("lang_" + langId, analyzeResult.getString("link_rewrite"));
                            }
                            JeproLabCache.getInstance().store(cacheKey, this);
                        }
                    }catch(SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        closeDataBaseConnection(dataBaseObject);
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
                this.available_later = analyzeModel.available_later;
                this.available_now = analyzeModel.available_now;
                this.analyzeMethods = analyzeModel.analyzeMethods;
            }

        }
        this.analyze_price = new JeproLabPriceModel();

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
            this.base_price = this.analyze_price.price;

            this.analyze_price.price = JeproLabAnalyzeModel.getStaticPrice(this.analyze_id, false, 0, 6, false, true, 1, false, 0, 0, 0);
            specific_price = JeproLabAnalyzeModel.static_specific_price;
            this.unit_price = (this.unit_price_ratio != 0  ? this.analyze_price.price / this.unit_price_ratio : 0);
            if (this.analyze_id > 0) {
                this.tags = JeproLabTagModel.getAnalyzeTags(this.analyze_id);
            }

            this.loadStockData();
        }

        if (this.default_category_id > 0) {
            this.category_name = JeproLabCategoryModel.getLinkRewrite(this.default_category_id, langId);
        }
    }

    /**
     * Fill the variables used for stock management
     */
    public void loadStockData() {
        if(this.analyze_id > 0){
            /* / By default, the analyze quantity correspond to the available quantity to sell in the current lab
            this.quantity = JeproLabStockAvailableModel.getQuantityAvailableByAnalyzeId(this.analyze_id, 0);
            this.out_of_stock = JeproLabStockAvailableModel.outOfStock(this.analyze_id);
            this.depends_on_stock = JeproLabStockAvailableModel.dependsOnStock(this.analyze_id);

            if (JeproLabContext.getContext().laboratory.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT && JeproLabContext.getContext().laboratory.getContextLaboratoryGroup().share_stocks){
                this.advanced_stock_management = this.useAdvancedStockManagement();
            } */
        }
    }

    public float getTaxesRate(){
        return getTaxesRate(null);
    }

    /**
     * Returns JeproLabTaxManagerFactory rate.
     *
     * @param address address to get tax from a given address
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

    /**
     * Compute analyze price given it id
     * @param analyzeId analyze id
     * @return price of the analyze
     */
    public static float getStaticPrice(int analyzeId){
        return getStaticPrice(analyzeId, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax){
        return getStaticPrice(analyzeId, useTax, 0);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, 6);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, false);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, 1);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, quantity, false);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity, boolean forceAssociateTax){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,onlyReduction,  useReduction, quantity, forceAssociateTax, 0, 0, 0, null, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity, boolean forceAssociateTax, int customerId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, 0, 0, null, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, 0, null, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId, int addressId){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, addressId, null, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId, int addressId, JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals,  onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, addressId, specificPrice, true, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociateTax, int customerId, int cartId, int addressId, JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice, boolean withEcoTax){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction , quantity, forceAssociateTax, customerId, cartId, addressId, specificPrice, withEcoTax, true, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice, boolean withEcoTax, boolean useGroupReduction){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction , quantity, forceAssociatedTax, customerId, cartId, addressId, specificPrice, withEcoTax, useGroupReduction, null, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice, boolean withEcoTax, boolean useGroupReduction, JeproLabContext context){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, quantity, forceAssociatedTax, customerId, cartId, addressId, specificPrice, withEcoTax, useGroupReduction, context, true);
    }

    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction,  boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice, boolean withEcoTax, boolean useGroupReduction, JeproLabContext context, boolean useCustomerPrice){
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction, quantity, forceAssociatedTax, customerId, cartId, addressId, specificPrice, withEcoTax, useGroupReduction, context, useCustomerPrice, 0);
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
    public static float getStaticPrice(int analyzeId, boolean useTax, int analyzeAttributeId, int decimals, boolean onlyReduction, boolean useReduction, int quantity, boolean forceAssociatedTax, int customerId, int cartId, int addressId, JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice, boolean withEcoTax, boolean useGroupReduction, JeproLabContext context, boolean useCustomerPrice, int customizationId){
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
            * When a user (e.g., guest, customer, Google...) is on JeproLab, he has already its cart as the global (see /init.php)
            * When a non-user calls directly this method (e.g., payment module...) is on JeproLab, he does not have already it BUT knows the cart ID
            * When called from the back office, cart ID can be in-existent
            */
            if (cartId > 0 && context.employee != null) {
                JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_NOT_AN_EMPLOYEE_YOU_SHOULD_LOGIN_IN_LABEL"));
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
                String query = "SELECT SUM(" + JeproLabDataBaseConnector.quoteName("quantity") + ") AS quantity FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_cart_analyze");
                query += " AS cart_analyze WHERE "  + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId + " AND ";
                query += JeproLabDataBaseConnector.quoteName("cart_id") + " = " + cartId;

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                cartQuantity = (int)dataBaseObject.loadValue(query, "quantity");
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
        String addressInfoVatNumber = "";

        if (addressId > 0) {
            Map<String, String> addressInfo = JeproLabAddressModel.getCountryAndState(addressId);

            try {
                assert addressInfo != null;
                addressInfoCountryId = Integer.parseInt(addressInfo.get("country_id"));
                addressInfoStateId = Integer.parseInt(addressInfo.get("state_id"));
                addressInfoZipCode = addressInfo.get("postcode");
                addressInfoVatNumber = addressInfo.get("vat_number");
            }catch (NullPointerException ignored){

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

        if (useTax && (addressInfoVatNumber.equals("")) && addressInfoCountryId != JeproLabSettingModel.getIntValue("vat_number_country") && JeproLabSettingModel.getIntValue("vat_number_management") > 0){
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
            String select = "SELECT analyze_lab." + JeproLabDataBaseConnector.quoteName("price") + ", analyze_lab." + JeproLabDataBaseConnector.quoteName("ecotax");
            String from = " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " AS analyse";
            String innerJoin = " INNER JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyze.analyze_id = ";
            innerJoin += " analyze_lab.analyze_id AND analyze_lab.lab_id = " + labId + ") ";
            String where = " WHERE analyse." + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId;
            String leftJoin = "";
            if (JeproLabCombinationModel.isFeaturePublished()) {
                select += "IFNULL(analyze_attribute_lab.analyze_attribute_id, 0) AS analyze_attribute_id, analyze_attribute_lab.";
                select += JeproLabDataBaseConnector.quoteName("price") + " AS attribute_price, analyze_attribute_lab.default_on ";
                leftJoin += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute_lab") + " AS analyze_attribute_lab ON (";
                leftJoin += "analyze_attribute_lab.analyze_id = analyse.analyze_id AND analyze_attribute_lab.lab_id = " + labId + ")";
            } else {
                select += " 0 as analyze_attribute_id ";
            }
            String query = select + from + leftJoin + innerJoin + where;
            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet results = dataBaseObject.loadObjectList(query);

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
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
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


    public static JeproLabAnalyzeModel getTaxesInformation(JeproLabAnalyzeModel analyze){
        return getTaxesInformation(analyze, null);
    }

    public static JeproLabAnalyzeModel getTaxesInformation(JeproLabAnalyzeModel analyze, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        if (address == null) {
            address = new JeproLabAddressModel();
        }

        address.country_id = context.country.country_id;
        address.state_id = 0;
        address.postcode = "";

        JeproLabTaxModel.JeproLabTaxRulesManager taxManager = JeproLabTaxModel.JeproLabTaxManagerFactory.getManager(address, JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyze.analyze_id, context));
        analyze.tax_rate = taxManager.getTaxCalculator().getTotalRate();
        analyze.tax_name = taxManager.getTaxCalculator().getTaxesName();

        return analyze;
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
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab");
            query +=" WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId + " AND lab_id = " + context.laboratory.laboratory_id;
            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            int result = (int)dataBaseObject.loadValue(query, "tax_rules_group_id");
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public boolean isNew(){
        String query = "SELECT analyse.analyze_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " AS analyse ";
        query += JeproLabLaboratoryModel.addSqlAssociation("analyze") + " WHERE analyse.analyze_id = " + this.analyze_id ;
        query += " AND DATEDIFF( analyze_lab." + JeproLabDataBaseConnector.quoteName("date_add") + ", DATE_SUB('" + JeproLabTools.date("Y-m-d");
        query += " 00:00:00' , INTERVAL " + (JeproLabSettingModel.getIntValue("number") > 0 ? JeproLabSettingModel.getIntValue("") : 20);
        query += " DAY ) ) > 0 ";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet analyzes = dataBaseObject.loadObjectList(query);
        int total = 0;
        try{
            while (analyzes.next()){
                total += 1;
            }
        }catch (SQLException ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }finally {
            closeDataBaseConnection(dataBaseObject);
        }
        return total > 0;
    }


    /**
     *
     * Created by jeprodev on 09/01/2016.
     */
    public static  class JeproLabMethodModel extends JeproLabModel {
        public int method_id;

        public String name;

        public String code;

        public float threshold;

        public String unit;

        public JeproLabMethodModel(){
            this(0);
        }

        public JeproLabMethodModel(int methodId){
            if(methodId > 0){
                String cacheKey = "jeprolab_";
                if(!JeproLabCache.getInstance().isStored(cacheKey)){
                    String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_method") + " WHERE " + JeproLabDataBaseConnector.quoteName("method_id") + " = " + methodId;

                    JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    ResultSet methodSet = dataBaseObject.loadObjectList(query);
                    if(methodSet != null){
                        try{
                            //JeproLabMethodModel method;
                            if(methodSet.next()){
                                //method = new JeproLabMethodModel();
                                this.method_id = methodSet.getInt("method_id");
                                this.name = methodSet.getString("name");
                                this.code = methodSet.getString("code");
                                this.threshold = methodSet.getFloat("threshold");
                                this.unit = methodSet.getString("unit");
                                JeproLabCache.getInstance().store(cacheKey, this);
                            }
                        }catch(SQLException ignored){
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }finally {
                            closeDataBaseConnection(dataBaseObject);
                        }
                    }
                }else{
                    JeproLabMethodModel method = (JeproLabMethodModel)JeproLabCache.getInstance().retrieve(cacheKey);
                    this.method_id = method.method_id;
                    this.name = method.name;
                    this.code = method.code;
                    this.threshold = method.threshold;
                    this.unit = method.unit;
                }
            }
        }

        /**
         * Get list of available methods
         */
        public static List<JeproLabMethodModel> getMethods(){
            String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_method");

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet methodSet = dataBaseObject.loadObjectList(query);

            List<JeproLabMethodModel> methods = new ArrayList<>();

            if(methodSet != null){
                try{
                    JeproLabMethodModel method;
                    while(methodSet.next()){
                        method = new JeproLabMethodModel();
                        method.method_id = methodSet.getInt("method_id");
                        method.name = methodSet.getString("name");
                        method.code = methodSet.getString("code");
                        method.threshold = methodSet.getFloat("threshold");
                        method.unit = methodSet.getString("unit");
                        methods.add(method);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }

            return methods;
        }

        public void save(){
            String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_method") + "(" + JeproLabDataBaseConnector.quoteName("name") + ", ";
            query += JeproLabDataBaseConnector.quoteName("code") + ", " + JeproLabDataBaseConnector.quoteName("threshold") + ", " + JeproLabDataBaseConnector.quoteName("unit");
            query += ") VALUES (" + JeproLabDataBaseConnector.quote(this.name) + ", " + JeproLabDataBaseConnector.quote(this.code) + ", " + this.threshold + ", ";
            query += JeproLabDataBaseConnector.quote(this.unit) + ")";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            dataBaseObject.query(query, true);
            this.method_id = dataBaseObject.getGeneratedKey();
            closeDataBaseConnection(dataBaseObject);
        }

        public void update(){
            String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_method") + " SET " + JeproLabDataBaseConnector.quoteName("name") + " = ";
            query += JeproLabDataBaseConnector.quote(this.name) + ", " + JeproLabDataBaseConnector.quoteName("code") + " = " + JeproLabDataBaseConnector.quote(this.code);
            query += ", " + JeproLabDataBaseConnector.quoteName("threshold") + " = " + this.threshold + ", " + JeproLabDataBaseConnector.quoteName("unit");
            query += " = " + JeproLabDataBaseConnector.quote(this.unit) + " WHERE " + JeproLabDataBaseConnector.quoteName("method_id") + " = " + this.method_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            dataBaseObject.query(query, false);
        }

        public static List<Integer> getAnalyzeMethodIdsByAnalyzeId(int analyzeId){
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("method_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_method");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet methodSet = dataBaseObject.loadObjectList(query);

            List<Integer> methodIds = new ArrayList<>();
            if(methodSet != null){
                try{
                    while(methodSet.next()){
                        methodIds.add(methodSet.getInt("method_id"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }
            return methodIds;
        }
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

        String query = "SELECT analyze_attribute.analyze_attribute_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute");
        query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute") + " WHERE analyze_attribute.analyze_id = " + analyzeId;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        int resultNoFilter = (int)dataBaseObject.loadValue(query, "analyze_attribute_id", false);
        if (resultNoFilter <= 0) {
            combinations.get(analyzeId).put(minimumQuantity, 0);
            return 0;
        }

        query = "SELECT analyze_attribute_lab.analyze_attribute_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute");
        query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
        query += (minimumQuantity > 0 ? JeproLabAnalyzeModel.queryStock("analyze_attribute") : "") + " WHERE analyze_attribute_lab.default_on = 1";
        query += (minimumQuantity > 0 ? " AND IFNULL (stock.quantity, 0) >= " + minimumQuantity : "") + " AND analyze_attribute.analyze_id = " + analyzeId;

        //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        int result = (int)dataBaseObject.loadValue(query, "analyze_attribute_id", false);

        if (result <= 0) {
            query = "SELECT analyze_attribute_lab.analyze_attribute_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute");
            query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
            query += (minimumQuantity > 0 ? JeproLabAnalyzeModel.queryStock("analyze_attribute") : "") + " WHERE  analyze_attribute.analyze_id =" + analyzeId;
            query += (minimumQuantity > 0 ? " AND IFNULL (stock.quantity, 0) >= " + minimumQuantity : "");
            //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            result = (int)dataBaseObject.loadValue(query, "analyze_attribute_id", false);
        }

        if (result <= 0) {
            query = "SELECT analyze_attribute_lab.analyze_attribute_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute");
            query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
            query += (minimumQuantity > 0 ? JeproLabAnalyzeModel.queryStock("analyze_attribute") : "") + " WHERE analyze_attribute_lab.default_on = 1";
            query += " AND analyze_attribute.analyze_id = " + analyzeId;

            //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            result = (int)dataBaseObject.loadValue(query, "analyze_attribute_id", false);
        }

        if (result <= 0) {
            result = resultNoFilter;
        }

        closeDataBaseConnection(dataBaseObject);

        combinations.get(analyzeId).put(minimumQuantity, result);
        return result;
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
        String query = ((innerJoin) ? " INNER " : " LEFT ") + "JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_stock_available") + " AS stock ";
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

        query += JeproLabStockModel.JeproLabStockAvailableModel.addSqlLaboratoryRestriction(lab, "stock") + ") ";

        return query;
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(){
        return getAnalyzeList(1);
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber){
        return getAnalyzeList(pageNumber, 0);
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber, int categoryId){
        return getAnalyzeList(pageNumber, categoryId, 0);
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber, int categoryId, int labId){
        return getAnalyzeList(pageNumber, categoryId, labId, false);
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber, int categoryId, int labId, boolean publishedAnalyze){
        return getAnalyzeList(pageNumber, categoryId, labId, publishedAnalyze, "");
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber, int categoryId, int labId, boolean publishedAnalyze, String orderWay){
        return getAnalyzeList(pageNumber, categoryId, labId, publishedAnalyze, orderWay, "");
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber, int categoryId, int labId, boolean publishedAnalyze, String orderWay, String orderBy){
        return getAnalyzeList(pageNumber, categoryId, labId, publishedAnalyze, orderWay, orderBy, null);
    }

    public static List<JeproLabAnalyzeModel> getAnalyzeList(int pageNumber, int categoryId, int labId, boolean publishedAnalyze, String orderWay, String orderBy, JeproLabContext context) {
        if (context == null) {
            context = JeproLabContext.getContext();
        }

        //int limit = JeproLabConfigurationSettings.LIST_LIMIT;
        //int limitStart = (pageNumber - 1) * JeproLabConfigurationSettings.LIST_LIMIT;
        int langId = context.language.language_id;
        if (orderBy.equals("")) {
            orderBy = "date_add";
        }

        if (orderWay.equals("")) {
            orderWay = " ASC";
        }
        //labId = (labId != 0) ? labId : context.laboratory.laboratory_id;

        JeproLabCategoryModel category;
        if (JeproLabLaboratoryModel.isFeaturePublished() && categoryId > 0) {
            category = new JeproLabCategoryModel(categoryId);
            if (!category.existsInLaboratories(context.laboratory.laboratory_id)) {
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
                JeproLab.getInstance().getApplicationForms().analyzeForm.controller.initializeContent();
            }
        }


        int currentCategoryId = 0;
        if (categoryId > 0) {
            category = new JeproLabCategoryModel(categoryId);
        } else {
            if (categoryId > 0) {
                currentCategoryId = categoryId;
            }
            if (currentCategoryId > 0) {
                category = new JeproLabCategoryModel(currentCategoryId);
            } else {
                category = new JeproLabCategoryModel();
            }
        }
        context.category = category;

        boolean joinCategory = false;
        if (category.category_id > 0) {
            joinCategory = true;
        }

        if (labId == 0) {
            if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                labId = context.laboratory.laboratory_id;
            } else {
                labId = JeproLabSettingModel.getIntValue("default_lab");
            }
        } else if (labId != context.laboratory.laboratory_id) {
            context.laboratory = new JeproLabLaboratoryModel(labId);
        }

        String joinFilter = " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_image") + " AS image ON (image.";
        joinFilter += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyse." + JeproLabDataBaseConnector.quoteName("analyze_id");
        joinFilter += ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_stock_available") + " AS stock_available ON (stock_available.";
        joinFilter += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyse." + JeproLabDataBaseConnector.quoteName("analyze_id");
        joinFilter += " AND stock_available." + JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + " = 0 ";
        joinFilter += JeproLabStockModel.JeproLabStockAvailableModel.addSqlLaboratoryRestriction(null, "stock_available");
        joinFilter += ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyse.";
        joinFilter += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyze_lab." + JeproLabDataBaseConnector.quoteName("analyze_id");
        joinFilter += " AND analyze_lab." + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + labId + ") LEFT JOIN ";
        joinFilter += JeproLabDataBaseConnector.quoteName("#__jeprolab_category_lang") + " AS category_lang ON ( analyze_lang.";
        joinFilter += JeproLabDataBaseConnector.quoteName("lang_id") + " = category_lang." + JeproLabDataBaseConnector.quoteName("lang_id");
        joinFilter += " AND category_lang." + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + labId + ") LEFT JOIN  ";
        joinFilter += JeproLabDataBaseConnector.quoteName("#__jeprolab_lab") + " AS lab ON (lab." + JeproLabDataBaseConnector.quoteName("lab_id");
        joinFilter += " = " + labId + ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_image_lab") + " AS image_lab ON (image_lab.";
        joinFilter += JeproLabDataBaseConnector.quoteName("image_id") + " = image." + JeproLabDataBaseConnector.quoteName("image_id") + " AND image_lab.";
        joinFilter += JeproLabDataBaseConnector.quoteName("cover") + " = 1 AND image_lab." + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + labId;
        joinFilter += ") LEFT JOIN  " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_download") + " AS analyze_download ON (analyze_download.";
        joinFilter += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyse." + JeproLabDataBaseConnector.quoteName("analyze_id") + ") ";

        String selectFilter = ", lab." + JeproLabDataBaseConnector.quoteName("lab_name") + " AS lab_name, analyse." + JeproLabDataBaseConnector.quoteName("default_lab_id") + ", MAX(image_lab." + JeproLabDataBaseConnector.quoteName("image_id") + ") AS image_id, category_lang.";
        selectFilter += JeproLabDataBaseConnector.quoteName("name") + " AS category_name, analyze_lab." + JeproLabDataBaseConnector.quoteName("price") + ", 0 AS final_price, analyse." + JeproLabDataBaseConnector.quoteName("is_virtual") + ", analyze_download.";
        selectFilter += JeproLabDataBaseConnector.quoteName("nb_downloadable") + ", stock_available." + JeproLabDataBaseConnector.quoteName("quantity") + " AS stock_available_quantity, analyze_lab." + JeproLabDataBaseConnector.quoteName("published");
        selectFilter += ", IF(stock_available." + JeproLabDataBaseConnector.quoteName("quantity") + " <= 0, 1, 0) badge_danger";

        if (joinCategory) {
            joinFilter += " INNER JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_category") + " analyze_category ON (analyze_category." + JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyse.";
            joinFilter += JeproLabDataBaseConnector.quoteName("analyze_id") + " AND analyze_category." + JeproLabDataBaseConnector.quoteName("category_id") + " = " + category.category_id + ") ";
            selectFilter += " , analyze_category." + JeproLabDataBaseConnector.quoteName("position");
        }

        String groupFilter = " GROUP BY analyze_lab." + JeproLabDataBaseConnector.quoteName("analyze_id");

        // Add SQL shop restriction
        String selectLabFilter = "";
        String joinLabFilter = "";
        String whereLabFilter = "";
        String whereFilter = "";
        String filter = "";
        if (!context.controller.laboratory_link_type.equals("")) {
            selectLabFilter = ", lab.lab_name AS lab_name ";
            joinLabFilter = ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_lab") + " AS lab ON analyse.lab_id = lab.lab_id";
            //whereLabFilter = JeproLabLaboratoryModel.addSqlRestriction($this->shopShareDatas, "analyze"); //, $this->shopLinkType
        }

        /* Query in order to get results with all fields */
        String langJoinFilter = "";
        if (langId > 0) {
            langJoinFilter = " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (analyze_lang.";
            langJoinFilter += JeproLabDataBaseConnector.quoteName("analyze_id") + " = analyse." + JeproLabDataBaseConnector.quoteName("analyze_id") + " AND analyze_lang.";
            langJoinFilter += JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId;

            if (!JeproLabLaboratoryModel.isFeaturePublished()) {
                langJoinFilter += " AND analyze_lang." + JeproLabDataBaseConnector.quoteName("lab_id") + " = 1";
            } else if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                //$langJoinFilter += " AND analyze_lang." . JeproLabDataBaseConnector.quoteName("lab_id") . " = " . (int)$shop_lang_id;
            } else {
                langJoinFilter += " AND analyze_lang." + JeproLabDataBaseConnector.quoteName("lab_id") + " = analyse.default_lab_id";
            }
            langJoinFilter += ")";
        }

        if (context.controller.multi_laboratory_context && JeproLabLaboratoryModel.isTableAssociated("analyze")) {
            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.ALL_CONTEXT || !context.employee.isSuperAdmin()) {
                boolean testJoinFilter = false; // !preg_match("/`?".preg_quote("#__jeprolab_analyze_lab") + "`? *analyze_lab/", joinFilter);
                if (JeproLabLaboratoryModel.isFeaturePublished() && testJoinFilter && JeproLabLaboratoryModel.isTableAssociated("analyze")) {
                    whereFilter += " AND analyse.analyze_id IN (	SELECT analyze_lab.analyze_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab");
                    List<Integer> laboratoryIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
                    String laboratories = "";
                    if (laboratoryIds.size() > 0) {
                        for (int ind : laboratoryIds) {
                            laboratories += ind + ", ";
                        }
                        laboratories = laboratories.endsWith(", ") ? laboratories.substring(0, laboratories.length() - 2) : laboratories;
                    }
                    whereFilter += " AS analyze_lab WHERE analyze_lab.lab_id IN (" + laboratories + "))";

                }
            }
        }

        whereFilter += (publishedAnalyze ? "analyse." + JeproLabDataBaseConnector.quoteName("published") + " = 1 " : "");

        String havingClauseFilter = "";
        /*if (isset($filterHaving) || isset($having)){
            havingClause = " HAVING ";
            if (isset($filterHaving)){
                $having_clause .= ltrim($filterHaving, " AND ");
            }
            if (isset($having)){ havingClause += $having + " "; }
        }*/


        List<JeproLabAnalyzeModel> analyzes = new ArrayList<>();

        ResultSet analyzeSet;


        String query = "SELECT analyse." + JeproLabDataBaseConnector.quoteName("analyze_id") + ", analyze_lang." + JeproLabDataBaseConnector.quoteName("name") + ", analyse.";
        query += JeproLabDataBaseConnector.quoteName("reference") + selectFilter + selectLabFilter + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " AS analyse ";
        query += langJoinFilter + joinFilter + joinLabFilter + " WHERE 1 " + whereFilter + filter + whereLabFilter + groupFilter + havingClauseFilter + " ORDER BY ";
        query += (JeproLabTools.strReplace(orderBy, "`", "").equals("analyze_id") ? "analyse." : " analyse.") + JeproLabDataBaseConnector.quoteName(orderBy) + " " + orderWay;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        analyzeSet = dataBaseObject.loadObjectList(query);

        if (analyzeSet != null) {
            try {
                JeproLabAnalyzeModel analyze;
                while (analyzeSet.next()) {
                    analyze = new JeproLabAnalyzeModel();
                    analyze.analyze_id = analyzeSet.getInt("analyze_id");
                    analyze.reference = analyzeSet.getString("reference");
                    analyze.published = analyzeSet.getInt("published") > 0;
                    analyze.category_name = analyzeSet.getString("category_name");
                    analyze.base_price = analyzeSet.getFloat("price");
                    analyze.analyze_price.price = analyzeSet.getFloat("final_price");
                    if (analyze.name == null) {
                        analyze.name = new HashMap<>();
                    }
                    analyze.name.put("lang_" + langId, analyzeSet.getString("name"));
                    analyzes.add(analyze);
                }
            } catch (SQLException ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            } finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }


        return analyzes;
    }

    public boolean delete(){
        /**
         * It is NOT possible to delete a analyze if there are currently:
         * - physical stock for this analyze
         * - supply order(s) for this analyze
         */
        if ((JeproLabSettingModel.getIntValue("advanced_stock_management") > 1)&& this.advanced_stock_management){
            JeproLabStockModel.JeproLabStockManager stockManager = JeproLabStockModel.JeproLabStockManagerFactory.getManager();
            int physicalQuantity = stockManager.getAnalyzePhysicalQuantities(this.analyze_id, 0);
            int realQuantity = stockManager.getAnalyzeRealQuantities(this.analyze_id, 0);
            if (physicalQuantity > 0) {
                return false;
            }
            if(realQuantity > physicalQuantity){
                return false;
            }
/*
            $warehouse_analyze_locations = Adapter_ServiceLocator::get('Core_Foundation_Database_EntityManager')->getRepository('WarehouseProductLocation')->findByIdProduct(this.analyze_id);
            foreach ($warehouse_analyze_locations as $warehouse_analyze_location) {
                $warehouse_analyze_location.delete();
             }

            $stocks = Adapter_ServiceLocator::get('Core_Foundation_Database_EntityManager')->getRepository('Stock')->findByIdProduct(this.id);
            foreach ($stocks as $stock) {
                stock.delete();
            } */
        }
        this.clearCache("analyze", this.analyze_id);
        boolean result = true;
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id");
        query += " = " + this.analyze_id + " AND " + JeproLabDataBaseConnector.quoteName("lab_id") + " = ";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        for(Integer labId : JeproLabLaboratoryModel.getContextListLaboratoryIds()){
            //dataBaseObject.setQuery;
            result &= dataBaseObject.query((query + labId), false, false, false);
        }

        if(result && this.hasMultiLaboratoryEntries()){
            query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;
            //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            result &= dataBaseObject.query(query, false, false, false);
        }

        if(!result){ return result; }

        query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id");
        query += " = " + this.analyze_id + " AND " + JeproLabDataBaseConnector.quoteName("lang_id") + " = ";
        Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            //dataBaseObject.setQuery;
            result &= dataBaseObject.query((query + language.language_id), false, false, false);
        }

        // Removes the analyze from StockAvailable, for the current lab
        JeproLabStockModel.JeproLabStockAvailableModel.removeAnalyzeFromStockAvailable(this.analyze_id);
        result &= (this.deleteAnalyzeAttributes() && this.deleteImages() && this.deleteAnalyzesScene());
        // If there are still entries in analyze_lab, don't remove completely the analyze
        if (this.hasMultiLaboratoryEntries()) {
            return true;
        }

        //Hook::exec('actionProductDelete', array('id_analyze' => (int)this.id, 'analyze' => $this));
        return !(!result || !JeproLabGroupModel.JeproLabGroupReductionModel.deleteAnalyzeReduction(this.analyze_id) ||
            !this.deleteCategories(true) ||
            !this.deleteAnalyzeFeatures() ||
            !this.deleteTags() ||
            !this.deleteCartAnalyzes() ||
            !this.deleteAttributesImpacts() ||
            !this.deleteAttachments(false) ||
            !this.deleteCustomization() ||
            !JeproLabPriceModel.JeproLabSpecificPriceModel.deleteByAnalyzeId(this.analyze_id) ||
            !this.deletePack() ||
            !this.deleteAnalyzeSale() ||
            !this.deleteSearchIndexes() ||
            !this.deleteAccessories() ||
            !this.deleteFromAccessories() ||
            !this.deleteFromSupplier() ||
            !this.deleteDownload() ||
            !this.deleteFromCartRules());
    }

    /**
     * Checks if there is more than one entry in associated shop table for current object.
     *
     * @return bool
     */
    public boolean hasMultiLaboratoryEntries(){
        if (!JeproLabLaboratoryModel.isTableAssociated("analyze") || !JeproLabLaboratoryModel.isFeaturePublished()) {
            return false;
        }
        String query = "SELECT COUNT(*) AS labs FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_lab") + " WHERE ";
        query += JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        return ((int)dataBaseObject.loadValue(query, "labs") > 1);
    }

    /**
     * Delete analyze pack details
     *
     * @return array Deletion result
     */
    public boolean deletePack(){
        String query = "'DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_pack") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_pack_id");
        query += " = " + this.analyze_id + " OR " + JeproLabDataBaseConnector.quoteName("analyze_item_id") + " = " + this.analyze_id;
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze customizations
     *
     * @return array Deletion result
     */
    public boolean deleteCustomization(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_customization_field") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;
        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
/*
        query = "DELETE `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'customization_field_lang` FROM `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'customization_field_lang` LEFT JOIN `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'customization_field`
                ON ('.JeproLabDataBaseConnector.quoteName("#__jeprolab.'customization_field.id_customization_field = '.JeproLabDataBaseConnector.quoteName("#__jeprolab.'customization_field_lang.id_customization_field)
                WHERE '.JeproLabDataBaseConnector.quoteName("#__jeprolab.'customization_field.id_customization_field IS NULL";
        data */
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze sales
     *
     * @return array Deletion result
     */
    public boolean deleteAnalyzeSale(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_sale") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze in its scenes
     *
     * @return array Deletion result
     */
    public boolean deleteAnalyzesScene(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_scene_analyzes") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    public boolean deleteAttachments(){
        return deleteAttachments(true);
    }

    /**
     * Delete analyze attachments
     *
     * @param updateAttachmentCache If set to true attachment cache will be updated
     * @return array Deletion result
     */
    public boolean deleteAttachments(boolean updateAttachmentCache){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attachment") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_ide") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);

        if (updateAttachmentCache) {
            JeproLabAnalyzeModel.updateCacheAttachment(this.analyze_id);
        }
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze indexed words
     *
     * @return array Deletion result
     */
    public boolean deleteSearchIndexes(){
        String query = "DELETE "; //`'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_index`, `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_word`
        /*FROM `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_index` JOIN `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_word`
        WHERE `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_index`.`id_analyze` = '.(int)this.id.'
        AND `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_word`.`id_word` = `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'search_index`.id_word'
        );*/

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze accessories
     *
     * @return mixed Deletion result
     */
    public boolean deleteAccessories(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_accessory") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze from other analyzes accessories
     *
     * @return mixed Deletion result
     */
    public boolean deleteFromAccessories(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_accessory") + " WHERE " +  JeproLabDataBaseConnector.quoteName("analyze_id_2") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Remove all downloadable files for analyze and its attributes
     *
     * @return bool
     */
    public boolean deleteDownload(){
        boolean result = true;
        /*$collection_download = new PrestaShopCollection('ProductDownload');
        $collection_download->where('id_analyze', '=', this.id);
        foreach ($collection_download as $analyze_download) {
            /** @var ProductDownload $analyze_download * /
            $result &= analyze_download->delete($analyze_download->checkFile());
        }*/
        return result;
    }

    public boolean deleteSelection(List<Integer> analyzes){
        boolean result = true;
        if (analyzes.isEmpty() && (analyzes.size() > 0)) {
            /*/ Deleting analyzes can be quite long on a cheap server. Let's say 1.5 seconds by analyze (I've seen it!).
            if (intval(ini_get('max_execution_time')) < round($count * 1.5)) {
                ini_set('max_execution_time', round($count * 1.5));
            }

            for(Integer analyzeId : analyzes) {
                JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId);
                result &= analyze.delete();
            }*/
        }
        return result;
    }

    public boolean deleteFromCartRules(){
        List<Integer> analyzeIds = new ArrayList<>();
        analyzeIds.add(this.analyze_id);
        JeproLabCartModel.JeproLabCartRuleModel.cleanAnalyzeRuleIntegrity("analyzes", analyzeIds);
        return true;
    }

    public boolean deleteFromSupplier(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_supplier") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") +  " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze images from database
     *
     * @return bool success
     */
    public boolean deleteImages(){
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("image_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_image");
        query += " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet imageSet = dataBaseObject.loadObjectList(query);

        boolean status = true;
        if (imageSet != null) {
            try {
                JeproLabImageModel image;
                while(imageSet.next()){
                    image = new JeproLabImageModel(imageSet.getInt("image_id"));
                    status &= image.delete();
                }
            }catch(SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }
        }
        return status;
    }

    /**
     * Delete analyze from cart
     *
     * @return array Deletion result
     */
    public boolean deleteCartAnalyzes(){
        String query = "DELETE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_cart_analyze") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Del all default attributes for analyze
     */
    public boolean deleteDefaultAttributes(){
        /*return ObjectModel::updateMultilabTable('Combination', array(
         'default_on' => null,
         ), 'a.`id_analyze` = '.(int)this.id);**/
        return true;
    }

    /**
     * Delete analyze attributes
     *
     * @return array Deletion result
     */
    public boolean deleteAnalyzeAttributes(){

    /*Hook::exec('actionProductAttributeDelete', array('id_analyze_attribute' => 0, 'id_analyze' => (int)this.id, 'deleteAllAttributes' => true));

    $result = true;
        List<JeproLabCombinationModel> combinations = new PrestaShopCollection('Combination');
        $combinations->where('id_analyze', '=', this.id);
        foreach ($combinations as $combination) {
        $result &= $combination->delete();
        }
        JeproLabPriceModel.JeproLabSpecificPriceRuleModel.applyAllRules(array((int) this.id)); */
        //Tools::clearColorListCache(this.id);

        //closeDataBaseConnection(dataBaseObject);
        return true; //$result;
    }

    /**
     * Delete analyze attributes impacts
     *
     * @return bool
     */
    public boolean deleteAttributesImpacts(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_attribute_impact") + " WHERE ";
        query += JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /**
     * Delete analyze features
     *
     * @return array Deletion result
     */
    public boolean deleteAnalyzeFeatures(){
        List<Integer> analyzeIds = new ArrayList<>();
        analyzeIds.add(this.analyze_id);
        JeproLabPriceModel.JeproLabSpecificPriceRuleModel.applyAllRules(analyzeIds);
        return this.deleteFeatures();
    }

    /**
     * Delete a analyze attributes combination
     *
     * @param analyzeAttributeId JepeoLabAnalyzeModel attribute id
     * @return array Deletion result
     */
    public boolean deleteAttributeCombination( int analyzeAttributeId){
        if(this.analyze_id <= 0|| analyzeAttributeId <= 0 ) {
            return false;
        }

        JeproLabCombinationModel combination = new JeproLabCombinationModel(analyzeAttributeId);
        boolean result = combination.delete();
        List<Integer> analyzeIds = new ArrayList<>();
        analyzeIds.add(this.analyze_id);
        JeproLabPriceModel.JeproLabSpecificPriceRuleModel.applyAllRules(analyzeIds);
        return result;
    }

    /**
     * Delete features
     *
     */
    public boolean deleteFeatures(){
        // List analyzes features
        String query = "SELECT analyze.*, feature.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_feature_analyze") + " AS analyze LEFT JOIN ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_feature_value") + "AS feature ON (feature." + JeproLabDataBaseConnector.quoteName("feature_value_id");
        query += " = analyze." + JeproLabDataBaseConnector.quoteName("feature_value_id") + ") WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet featureSet = dataBaseObject.loadObjectList(query);

        if(featureSet != null){
        /*foreach ($features as $tab) {
            // Delete analyze custom features
            if ($tab['custom']) {
                Db::getInstance()->execute('
                        DELETE FROM `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'feature_value`
                WHERE `id_feature_value` = '.(int)$tab['id_feature_value']);
                Db::getInstance()->execute('
                        DELETE FROM `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'feature_value_lang`
                WHERE `id_feature_value` = '.(int)$tab['id_feature_value']);
        $features = Db::getInstance()->executeS('

        }
        }*/
        }
        /*/ Delete analyze features
        $result = Db::getInstance()->execute('
        DELETE FROM `'.JeproLabDataBaseConnector.quoteName("#__jeprolab.'feature_analyze`
        WHERE `id_analyze` = '.(int)this.id);

        SpecificPriceRule::applyAllRules(array((int)this.id));*/
        closeDataBaseConnection(dataBaseObject);
        return true; //($result);
    }

    /**
     * Delete analyzes tags entries without delete tags for webservice usage
     *
     * @return array Deletion result
     */
    public boolean deleteWebServicesTags(){
        String query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_tag") + " WHERE ";
        query += JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        boolean result = dataBaseObject.query(query, false);
        closeDataBaseConnection(dataBaseObject);
        return result;
    }

    /*
     * @deprecated 1.5.0.10
     * @see JeproLabAnalyzeModel.deleteAttributeCombination()
     * @param int $id_analyze_attribute
     * /
    public function deleteAttributeCombination($id_analyze_attribute){
        Tools::displayAsDeprecated('Use JeproLabAnalyzeModel.deleteAttributeCombination($id_analyze_attribute)');
        return this.deleteAttributeCombination($id_analyze_attribute);
        }
    */

    public boolean deleteCategories(){
        return  deleteCategories(false);
    }

    /**
     * Delete all association to category where product is indexed
     *
     * @param cleanPositions clean category positions after deletion
     * @return array Deletion result
     */
    public boolean deleteCategories(boolean cleanPositions){
        if (cleanPositions) {
            /*$result = Db::getInstance()->executeS(
                    'SELECT `id_category`, `position`
                    FROM `'._DB_PREFIX_.'category_product`
            WHERE `id_product` = '.(int)$this->id
            );*/
        }

        /*boolean result = Db::getInstance()->delete('category_product', 'id_product = '.(int)$this->id);
        if ($clean_positions === true && is_array($result)) {
            foreach ($result as $row) {
                $return &= $this->cleanPositions((int)$row['id_category'], (int)$row['position']);
            }
        }
*/
        return true; //result;
    }

    /**
     * Delete products tags entries
     *
     * @return array Deletion result
     */
    public boolean deleteTags(){
        return JeproLabTagModel.deleteTagsForAnalyze(this.analyze_id);
    }

    public static boolean updateCacheAttachment(int analyzeId){
        /*$value = (bool)Db::getInstance()->getValue('
            SELECT id_attachment
            FROM '.JeproLabDataBaseConnector.quoteName("#__jeprolab.'analyze_attachment
        WHERE id_analyze='.(int)$id_analyze);
        return Db::getInstance()->update(
            'analyze',
            array('cache_has_attachments' => (int)$value),
            'id_analyze = '.(int)$id_analyze
        );*/
        return true;
    }

    public Map<Integer, String> getMethods(){
        String query = "SELECT method.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_method") + " AS method LEFT JOIN " +  JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_method");
        query += " AS analyze_method ON( method." + JeproLabDataBaseConnector.quoteName("method_id") + " = analyze_method." + JeproLabDataBaseConnector.quoteName("method_id") + ") WHERE analyze_method.";
        query += JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet methodSet = dataBaseObject.loadObjectList(query);
        Map<Integer, String> methodList = new HashMap<>();
        if(methodSet != null){
            try{
                while(methodSet.next()){
                    methodList.put(methodSet.getInt("method_id"), methodSet.getString("code"));
                }
            }catch(SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }else{
            closeDataBaseConnection(dataBaseObject);
        }
        return methodList;
    }

    public void removeMethod(int methodId){
        synchronized(this) {
            int index = 0;
            for (Integer i : analyzeMethods) {
                if (i == methodId) {
                    analyzeMethods.remove(index);
                    break;
                }
                index++;
            }
        }
    }

    public void addMethod(int methodId){
        synchronized(this) {
            analyzeMethods.add(methodId);
        }
    }

    public void updateMethods(){
        /**
         * remove method before update in o
         */
        String query = "DELETE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_method") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, false);

        query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_method") + "(" + JeproLabDataBaseConnector.quoteName("analyze_id");
        query += ", " + JeproLabDataBaseConnector.quoteName("method_id") + ") VALUES (" + this.analyze_id + ", ";

        for(Integer methodId : analyzeMethods){
            //dataBaseObject.setQuery;
            dataBaseObject.query((query + methodId + ")"), false);
        }

        closeDataBaseConnection(dataBaseObject);
    }

    public static int getAttributeMinimalQuantity(int analyzeAttributeId) {
        //TODO Edit me
        return 1;
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
            String query = "SELECT lab_id FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id");
            query += " = " + this.analyze_id + " AND lab_id = " + labId;
            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            associated = dataBaseObject.loadValue(query, "lab_id") > 0;

            JeproLabCache.getInstance().store(cacheKey, associated);
            closeDataBaseConnection(dataBaseObject);
            return associated;
        }

        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public void save(){
        JeproLabContext context = JeproLabContext.getContext();
        if(languages == null) {
            languages = JeproLabLanguageModel.getLanguages();
        }
        this.checkAnalyze();
        this.removeTaxFromEcoTax();
        List<Integer> labIds = new ArrayList<>();
        if(JeproLabLaboratoryModel.isTableAssociated("analyze")){
            labIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
            if(this.laboratory_ids.size() > 0){
                labIds = this.laboratory_ids;
            }
        }

        if(JeproLabLaboratoryModel.checkDefaultLabId("analyze")){
            int minValue =  labIds.get(0);
            for (Integer value : labIds){
                if(value < minValue) {
                    minValue = value;
                }
            }
            this.default_laboratory_id = minValue;
        }

        String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + "("  + JeproLabDataBaseConnector.quoteName("reference") + ", " ;
        query += JeproLabDataBaseConnector.quoteName("ean13") + ", " + JeproLabDataBaseConnector.quoteName("upc") + ", "  + JeproLabDataBaseConnector.quoteName("published") + ", ";
        query += JeproLabDataBaseConnector.quoteName("redirect_type") + ", " + JeproLabDataBaseConnector.quoteName("visibility") + ", " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id");
        query += ", " + JeproLabDataBaseConnector.quoteName("delay") + ", "  + JeproLabDataBaseConnector.quoteName("available_for_order") + ", "  + JeproLabDataBaseConnector.quoteName("show_price") + ", ";
        query += JeproLabDataBaseConnector.quoteName("online_only") + ", " + JeproLabDataBaseConnector.quoteName("default_lab_id") + ", " + JeproLabDataBaseConnector.quoteName("analyze_redirected_id");
        query += ", " + JeproLabDataBaseConnector.quoteName("date_add") + ", " + JeproLabDataBaseConnector.quoteName("date_upd") +  ", " + JeproLabDataBaseConnector.quoteName("available_date");
        query += ") VALUES(" + JeproLabDataBaseConnector.quote(this.reference, true) + ", " + JeproLabDataBaseConnector.quote(this.ean13, true) + ", " + JeproLabDataBaseConnector.quote(this.upc, true);
        query += ", " + (this.published ? 1 : 0) + ", " + JeproLabDataBaseConnector.quote(this.redirect_type, true) + ", " + JeproLabDataBaseConnector.quote(this.visibility, true) + ", ";
        query += this.tax_rules_group_id + ", " + this.delay + ", " + (this.available_for_order ? 1 : 0) + ", " + (this.show_price ? 1 : 0) + ", " + (this.online_only ? 1 :0);
        query += ", " + default_laboratory_id + ", " + this.analyze_redirected_id + ", " + JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"))+ ", ";
        query += JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + ", " + JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"))+ ") ";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, true);
        this.analyze_id = dataBaseObject.getGeneratedKey();

        if(JeproLabLaboratoryModel.isTableAssociated("analyze")) {
            String dateAdd = JeproLabTools.date("yyyy-MM-dd hh:mm:ss");
            /* Laboratory fields */
            for(int labId : labIds){
                query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lab") + "( " + JeproLabDataBaseConnector.quoteName("analyze_id") + ", ";
                query += JeproLabDataBaseConnector.quoteName("lab_id") + ", " + JeproLabDataBaseConnector.quoteName("online_only") +  ", " + JeproLabDataBaseConnector.quoteName("published");
                query += ", " + JeproLabDataBaseConnector.quoteName("redirect_type") + ", " + JeproLabDataBaseConnector.quoteName("analyze_redirected_id") + ", ";
                query += JeproLabDataBaseConnector.quoteName("available_for_order") + ",  " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id") + ", " + JeproLabDataBaseConnector.quoteName("delay");
                query += ", " + JeproLabDataBaseConnector.quoteName("show_price") + ", " + JeproLabDataBaseConnector.quoteName("visibility") + ", " + JeproLabDataBaseConnector.quoteName("available_date");
                query += ", " + JeproLabDataBaseConnector.quoteName("date_add") + ", " + JeproLabDataBaseConnector.quoteName("date_upd") + ") VALUES( " +this.analyze_id + ", "  + labId + ", ";
                query += this.online_only + ", " + (this.published ? 1 : 0) + ", " + JeproLabDataBaseConnector.quote(this.redirect_type) + ", 0, " + (this.available_for_order ? 1 : 0);
                query += ", " +  this.tax_rules_group_id + ", "  + this.delay + ", " + (this.show_price ? 1 : 0) + ", " + JeproLabDataBaseConnector.quote(this.visibility) + ", " ;
                query += JeproLabDataBaseConnector.quote(dateAdd) + ", " + JeproLabDataBaseConnector.quote(dateAdd) + ", " + JeproLabDataBaseConnector.quote(dateAdd)+ ") ";

                //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                dataBaseObject.query(query, false, false, false);
                /* Multilingual fields */
                for (Object o : languages.entrySet()) {
                    Map.Entry lang = (Map.Entry) o;
                    JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();

                    query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + "(" + JeproLabDataBaseConnector.quoteName("analyze_id") + ", ";
                    query += JeproLabDataBaseConnector.quoteName("lab_id") + ", " + JeproLabDataBaseConnector.quoteName("lang_id") + ", " + JeproLabDataBaseConnector.quoteName("description");
                    query += ", " + JeproLabDataBaseConnector.quoteName("link_rewrite") + ", " + JeproLabDataBaseConnector.quoteName("meta_description") + ", " + JeproLabDataBaseConnector.quoteName("meta_keywords");
                    query += ", " + JeproLabDataBaseConnector.quoteName("meta_title") + ", " + JeproLabDataBaseConnector.quoteName("short_description") + ", " + JeproLabDataBaseConnector.quoteName("name");
                    query += ") VALUES (" + this.analyze_id + ", " + labId + ", " + language.language_id + ", " + JeproLabDataBaseConnector.quote(this.description.get("lang_" + language.language_id));
                    query += ", " + JeproLabDataBaseConnector.quote(this.link_rewrite.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quote(this.meta_description.get("lang_" + language.language_id));
                    query += ", " + JeproLabDataBaseConnector.quote(this.meta_keywords.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quote(this.meta_title.get("lang_" + language.language_id));
                    query += ", " + JeproLabDataBaseConnector.quote(this.short_description.get("lang_" + language.language_id)) + ", "+ JeproLabDataBaseConnector.quote(this.name.get("lang_" + language.language_id)) + ")";

                    //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    dataBaseObject.query(query, false, false, false);
                }
            }
        }

        //JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(analyzeId);
        JeproLabStockModel.JeproLabStockAvailableModel.setAnalyzeOutOfStock(this.analyze_id, 2);

        this.setGroupReduction();
        /* Hook::exec('actionProductSave', array('id_analyze' => (int)this.id, 'analyze' => $this)); *
        analyze.addCarriers();
        analyze.updateAccessories();
        analyze.updatePackItems();
        //analyze.updateDownloadProduct();

        if(JeproLabSettingModel.getIntValue("use_advanced_stock_management_on_new_analyze") > 0 && JeproLabSettingModel.getIntValue("advanced_stock_management") > 0){
            analyze.advanced_stock_management = true;
            JeproLabStockModel.JeproLabStockAvailableModel.setAnalyzeDependsOnStock(analyze.analyze_id, true, context.laboratory.laboratory_id, 0);
            this.update();
        }

        if(!context.controller.has_errors){
            if(JeproLabSettingModel.getIntValue("default_warehouse_new_analyze") != 0 && JeproLabSettingModel.getIntValue("advanced_stock_management") > 0){
               /* JeproLabWarehouseAnalyzeLocationModel warehouseLocationEntity = new JeproLabWarehouseAnalyzeLocationModel();
                warehouseLocationEntity.analyze_id = analyze.analyze_id;
                warehouseLocationEntity.analyze_attribute_id = 0;
                warehouseLocationEntity.warehouse_id = JeproLabSettingModel.getIntValue("default_warehouse_new_analyze");
                warehouseLocationEntity.location = JeproLabDataBaseConnector.quote("");
                warehouseLocationEntity.save();* /
            }
        } */
        closeDataBaseConnection(dataBaseObject);
        this.updateMethods();
    }

    public void update(){
        if(this.available_date == null){
            this.available_date = this.date_add;
        }

        String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze") + " SET " + JeproLabDataBaseConnector.quoteName("supplier_id") + " = " + this.supplier_id;
        query += ", " + JeproLabDataBaseConnector.quoteName("manufacturer_id") + " = " + this.manufacturer_id + ", " + JeproLabDataBaseConnector.quoteName("default_category_id") + " = ";
        query += this.default_category_id + ", " + JeproLabDataBaseConnector.quoteName("default_lab_id") + " = " + this.default_laboratory_id+ ", " + JeproLabDataBaseConnector.quoteName("upc");
        query += " = " + JeproLabDataBaseConnector.quote(this.upc) + ", " + JeproLabDataBaseConnector.quoteName("tax_rules_group_id") + " = " + this.tax_rules_group_id + ", ";
        query += JeproLabDataBaseConnector.quoteName("on_sale") + " = " + (this.on_sale ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("online_only") + " = " + (this.online_only ? 1 : 0);
        query += ", " + JeproLabDataBaseConnector.quoteName("ean13") + " = " + JeproLabDataBaseConnector.quote(this.ean13) + ", " + JeproLabDataBaseConnector.quoteName("ecotax") + " = ";
        query += this.eco_tax + ", " + JeproLabDataBaseConnector.quoteName("quantity") + " = " + this.quantity + ", " + JeproLabDataBaseConnector.quoteName("minimal_quantity") + " = ";
        query += this.minimal_quantity + ", " + JeproLabDataBaseConnector.quoteName("price") + " = " + this.analyze_price.price + ", " + JeproLabDataBaseConnector.quoteName("wholesale_price") + " = ";
        query += this.wholesale_price + ", " + JeproLabDataBaseConnector.quoteName("unity") + " = " +  this.unity + ", " + JeproLabDataBaseConnector.quoteName("unit_price_ratio") + " = ";
        query += this.unit_price_ratio + ", " + JeproLabDataBaseConnector.quoteName("additional_shipping_cost") + " = " + this.additional_shipping_cost + ", " + JeproLabDataBaseConnector.quoteName("reference");
        query += " = " + JeproLabDataBaseConnector.quote(this.reference) + ", " + JeproLabDataBaseConnector.quoteName("supplier_reference") + " = " + JeproLabDataBaseConnector.quote(this.supplier_reference);
        query += ", " + JeproLabDataBaseConnector.quoteName("location") + " = " + this.location + ", " + JeproLabDataBaseConnector.quoteName("width") + " = " + this.weight + ", " + JeproLabDataBaseConnector.quoteName("height");
        query += " = " + this.height + ", " + JeproLabDataBaseConnector.quoteName("depth") + " = " + this.depth + ", " + JeproLabDataBaseConnector.quoteName("weight") + " = " + this.weight + ", ";
        query += JeproLabDataBaseConnector.quoteName("out_of_stock") + " = " + (this.out_of_stock ? 1 : 0) +  ", " + JeproLabDataBaseConnector.quoteName("quantity_discount") + " = " + this.quantity_discount;
        query += ", " + JeproLabDataBaseConnector.quoteName("customizable") +  " = " + (this.customizable ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("uploadable_files") + " = ";
        query += this.uploadable_files + ", " + JeproLabDataBaseConnector.quoteName("text_fields") + " = " + this.text_fields +  ", " + JeproLabDataBaseConnector.quoteName("published") + " = ";
        query += (this.published ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("redirect_type") + " = " + JeproLabDataBaseConnector.quote(this.redirect_type) + ", " + JeproLabDataBaseConnector.quoteName("analyze_redirected_id");
        query += " = " + this.analyze_redirected_id + ", "; /* + ", " + JeproLabDataBaseConnector.quoteName("available_for_order") + " = " + JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd", this.available_date));// + ", "; */
        query += JeproLabDataBaseConnector.quoteName("delay") + " = " + this.delay + ", " + JeproLabDataBaseConnector.quoteName("show_price") + " = " + (this.show_price ? 1 : 0) + ", ";
        query += JeproLabDataBaseConnector.quoteName("indexed") + " = " + (this.indexed ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("visibility") + " = " + JeproLabDataBaseConnector.quote(this.visibility)  + ", ";
        query += JeproLabDataBaseConnector.quoteName("cache_is_pack") + " = " + (this.cache_is_pack ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("cache_has_attachments") + " = " + (this.cache_has_attachments ? 1 : 0);
        query += ", " + JeproLabDataBaseConnector.quoteName("is_virtual") + " = " + (this.is_virtual ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("cache_default_attribute") + " = ";
        query += (this.cache_default_attribute ? 1 : 0) + ", " + JeproLabDataBaseConnector.quoteName("date_upd") + " = " + JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + ", ";
        query +=  JeproLabDataBaseConnector.quoteName("advanced_stock_management") + " = " +(this.advanced_stock_management ? 1 : 0) + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id");
        query += " = " + this.analyze_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, false, false, false);

        /**
         * Update analyze language information
         */
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }

        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + " SET " + JeproLabDataBaseConnector.quoteName("name") + " = ";
            query += JeproLabDataBaseConnector.quote(this.name.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("description");
            query += " = " + JeproLabDataBaseConnector.quote(this.description.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("short_description");
            query += " = " + JeproLabDataBaseConnector.quote(this.short_description.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("link_rewrite") +  " = ";
            query += JeproLabDataBaseConnector.quote(this.link_rewrite.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("meta_description") + " = ";
            query += JeproLabDataBaseConnector.quote(this.meta_description.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("meta_keywords") + " = ";
            query += JeproLabDataBaseConnector.quote(this.meta_keywords.get("lang_" + language.language_id)) +  ", " + JeproLabDataBaseConnector.quoteName("meta_title") + " = ";
            query += JeproLabDataBaseConnector.quote(this.meta_title.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("available_now") + " = ";
            query += JeproLabDataBaseConnector.quote(this.available_now.get("lang_" + language.language_id)) + ", " + JeproLabDataBaseConnector.quoteName("available_later") + " = ";
            query += JeproLabDataBaseConnector.quote(this.available_later.get("lang_" + language.language_id)) + " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = ";
            query += this.analyze_id + " AND " + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + language.language_id;

            //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            dataBaseObject.query(query, false, false, false);
        }

        this.setGroupReduction();
/*
        // Sync stock Reference, EAN13 and UPC
        if (JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT') && StockAvailable::dependsOnStock(this.id, JeproLabContext.getContext()->lab->id)) {
        Db::getInstance()->update('stock', array(
                        'reference' => pSQL(this.reference),
                'ean13'     => pSQL(this.ean13),
                'upc'        => pSQL(this.upc),
        ), 'id_analyze = '.(int)this.id.' AND id_analyze_attribute = 0'); *
    }

        Hook::exec('actionProductSave', array('id_analyze' => (int)this.id, 'analyze' => $this));
        Hook::exec('actionProductUpdate', array('id_analyze' => (int)this.id, 'analyze' => $this));
        if (this.getType() == JeproLabAnalyzeModel.PTYPE_VIRTUAL && this.active && !JeproLabSettingModel.get('PS_VIRTUAL_PROD_FEATURE_ACTIVE')) {
        JeproLabSettingModel.updateGlobalValue('PS_VIRTUAL_PROD_FEATURE_ACTIVE', '1');
    }
*/
        closeDataBaseConnection(dataBaseObject);
    }

    /**
     * delete all items in pack, then check if type_analyze value is 2.
     * if yes, add the pack items from input "inputPackItems"
     *
     */
    public void updatePackItems(){
        /*JeproshopProductPack::deleteItems($this->analyze_id);
        // lines format: QTY x ID-QTY x ID
        $app = JFactory::getApplication();
        $data = JRequest::get('post');
        $input_data = isset($data['information']) ?  $data['information'] : $data['jform'];
        if($input_data['analyze_type'] == JeproshopProductModelProduct::PACKAGE_PRODUCT){
            $this->setDefaultAttribute(0); //reset cache_default_attribute
            $items = $app->input->get('input_pack_items');
            $lines = array_unique(explode('-', $items));
            // lines is an array of string with format : QTYxID
            if (count($lines)){
                foreach ($lines as $line){
                    if (!empty($line)){
                        list($qty, $item_id) = explode('x', $line);
                        if ($qty > 0 && isset($item_id)){
                            if(JeproshopProductPack::isPack((int)$item_id)){
                                $this->context->controller->has_errors  = JText::_('COM_JEPROSHOP_YOU_CANT_ADD_PRODUCT_PACKS_INTO_A_PACK_MESSAGE');
                            }elseif (!JeproshopProductPack::addItem((int)$this->analyze_id, (int)$item_id, (int)$qty)){
                                $this->context->controller->has_errors  = JText::_('COM_JEPROSHOP_AN_ERROR_OCCURRED_WHILE_ATTEMPTING_TO_ADD_PRODUCTS_TO_THE_PACK_MESSAGE');
                            }
                        }
                    }
                }
            }
        }*/
    }

    public void addCarriers(){
        /*$app = JFactory::getApplication();
        $db = JFactory::getDBO();
        if (!isset($analyze)){
            $analyze = new JeproshopProductModelProduct((int)$app->input->get('analyze_id'));
        }
        if (JeproshopTools::isLoadedObject($analyze, 'analyze_id')){
            $carriers = array();
            $input = JRequest::get('post');
            $analyze_data = $input['shipping'];
            if (isset($analyze_data['selected_carriers[]'])){
                $carriers = $analyze_data['selected_carriers[]'];
            }

            $query = "UPDATE " . JeproLabDataBaseConnector.quoteName('#__jeprolab_analyze') . " SET " . JeproLabDataBaseConnector.quoteName('width') . " = " . (float)$analyze_data['width'] . ", " . JeproLabDataBaseConnector.quoteName('height') . " = ";
            $query .= (float)$analyze_data['height'] . ", " . JeproLabDataBaseConnector.quoteName('weight') . " = " . (float)$analyze_data['weight'] . ", " . JeproLabDataBaseConnector.quoteName('additional_shipping_cost') . " = ";
            $query .= (float)$analyze_data['additional_shipping_cost'] . " WHERE " . JeproLabDataBaseConnector.quoteName('analyze_id') . " = " . (int)$analyze->analyze_id;

            dataBaseObject.setQuery($query);
            dataBaseObject.query();

            if(count($carriers)){
                $query = "DELETE FROM " . JeproLabDataBaseConnector.quoteName('#__jeprolab_analyze_carrier') . " WHERE analyze_id = " . (int)$analyze->analyze_id . " AND shop_id = " . (int)$analyze->shop_id;

                dataBaseObject.setQuery($query);
                dataBaseObject.query();
                foreach ($carriers as $carrier){
                    $query = "INSERT INGORE INTO " . JeproLabDataBaseConnector.quoteName('#__jeprolab_analyze_carrier') . JeproLabDataBaseConnector.quoteName('analyze_id') . ", ";
                    $query .= JeproLabDataBaseConnector.quoteName('carrier_reference_id') . ", " . JeproLabDataBaseConnector.quoteName('shop_id') . " VALUES (" . (int)$analyze->analyze_id;
                    $query .= ", " . (int)$carrier . ", " . (int)$analyze->shop_id . ") ";

                    dataBaseObject.setQuery($query);
                    dataBaseObject.query();
                }
            }
        }*/
    }

    /**
     * Update analyze accessories
     */
    public void updateAccessories(){
       /* $app = JFactory::getApplication();
        $this->deleteAccessories();
        $accessories = $app->input->get('input_accessories');
        if($accessories){
            $accessories_id = array_unique(explode('-', $accessories));
            if (count($accessories_id)){
                array_pop($accessories_id);
                $this->changeAccessories($accessories_id);
            }
        }*/
    }

    /**
     * Set Group reduction if needed
     */
    public boolean setGroupReduction() {
        return JeproLabGroupModel.JeproLabGroupReductionModel.setAnalyzeReduction(this.analyze_id);
    }

    protected void removeTaxFromEcoTax(){
        removeTaxFromEcoTax(this.eco_tax);
    }

    protected void removeTaxFromEcoTax(float ecoTax){
        if (ecoTax > 0) {
            this.eco_tax = JeproLabTools.roundPrice(ecoTax/(1 + JeproLabTaxModel.getAnalyzeEcoTaxRate()/100), 6);
        }
    }

    public static List<JeproLabFeatureModel.JeproLabFeatureValueModel> getStaticFeatures(int analyzeId){
        if (!JeproLabFeatureModel.isFeaturePublished()) {
            return new ArrayList<>();
        }
        if (!JeproLabAnalyzeModel._cache_features.containsKey(analyzeId)) {
            //self::$_cacheFeatures[$id_product] = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            String query = "SELECT feature_analyze." + JeproLabDataBaseConnector.quoteName("feature_id") + ", feature_analyze.";
            query += JeproLabDataBaseConnector.quoteName("analyze_id") + ", feature_analyze." + JeproLabDataBaseConnector.quoteName("feature_value_id");
            query += ", custom FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_feature_analyze") + " AS feature_analyze LEFT JOIN ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_feature_value") + " AS feature_value ON (feature_analyze." ;
            query += JeproLabDataBaseConnector.quoteName("feature_value_id") + " = feature_value." + JeproLabDataBaseConnector.quoteName("feature_value_id");
            query += ") WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet featureSet = dataBaseObject.loadObjectList(query);
            List<JeproLabFeatureModel.JeproLabFeatureValueModel> featureValues = new ArrayList<>();
            if(featureSet != null){
                try{
                    JeproLabFeatureModel.JeproLabFeatureValueModel feature;
                    while(featureSet.next()){
                        feature = new JeproLabFeatureModel.JeproLabFeatureValueModel();
                        feature.feature_id = featureSet.getInt("feature_id");
                        feature.feature_value_id = featureSet.getInt("feature_value_id");
                        feature.analyze_id = featureSet.getInt("analyze_id");
                        feature.custom = featureSet.getInt("custom") > 0;
                        featureValues.add(feature);
                    }
                    JeproLabAnalyzeModel._cache_features.put(analyzeId, featureValues);
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }else {
                closeDataBaseConnection(dataBaseObject);
            }
        }
        return JeproLabAnalyzeModel._cache_features.get(analyzeId);
    }

    public static boolean isAvailableWhenOutOfStock(int outOfStock){
        int stockManagement = JeproLabSettingModel.getIntValue("stock_management");

        if (!( stockManagement <= 0)){
            return true;
        } else {
            int orderOutOfStock = JeproLabSettingModel.getIntValue("order_out_of_stock");

            return outOfStock == 2 ? (orderOutOfStock > 0) : (outOfStock > 0);
        }
    }

    /**
     * getProductCategories return an array of categories which this analyze belongs to
     *
     * @return List of categories id
     */
    public static List<Integer> getAnalyzeCategories(){
        return getAnalyzeCategories(0);
    }

    /**
     * getProductCategories return an array of categories which this analyze belongs to
     *
     * @return List of categories id
     */
    public static List<Integer> getAnalyzeCategories(int analyzeId){
        String cacheKey = "jeprolab_analyze_model_get_analyze_categories_" + analyzeId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            List<Integer> categoryIds = new ArrayList<>();

            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("category_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_category");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet categorySet = dataBaseObject.loadObjectList(query);

            if (categorySet != null) {
                try {
                    while(categorySet.next()){
                        categoryIds.add(categorySet.getInt("category_id"));
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }else {
                closeDataBaseConnection(dataBaseObject);
            }
            JeproLabCache.getInstance().store(cacheKey, categoryIds);
            return categoryIds;
        }
        return (List<Integer>)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    private void checkAnalyze(){
        // $className = 'Product';
        // @todo : the call_user_func seems to contains only statics values (className = 'Product')
        //$rules = call_user_func(array($this->className, 'getValidationRules'), $this->className);
        JeproLabLanguageModel defaultLanguage = new JeproLabLanguageModel(JeproLabSettingModel.getIntValue("default_lang"));
        if(this.languages == null) {
            languages = JeproLabLanguageModel.getLanguages();
        }/*
        // Check required fields
        foreach ($rules['required'] as $field) {
            if (!$this->isProductFieldUpdated($field)) {
                continue;
            }

            if (($value = Tools::getValue($field)) == false && $value != '0') {
                if (Tools::getValue('id_'.$this->table) && $field == 'passwd') {
                    continue;
                }
                $this->errors[] = sprintf(
                        Tools::displayError('The %s field is required.'),
                        call_user_func(array($className, 'displayFieldName'), $field, $className)
                );
            }
        }

        // Check multilingual required fields
        foreach ($rules['requiredLang'] as $fieldLang) {
            if ($this->isProductFieldUpdated($fieldLang, $default_language->id) && !Tools::getValue($fieldLang.'_'.$default_language->id)) {
                $this->errors[] = sprintf(
                        Tools::displayError('This %1$s field is required at least in %2$s'),
                        call_user_func(array($className, 'displayFieldName'), $fieldLang, $className),
                        $default_language->name
                );
            }
        }

        // Check fields sizes
        foreach ($rules['size'] as $field => $maxLength) {
            if ($this->isProductFieldUpdated($field) && ($value = Tools::getValue($field)) && Tools::strlen($value) > $maxLength) {
                $this->errors[] = sprintf(
                        Tools::displayError('The %1$s field is too long (%2$d chars max).'),
                        call_user_func(array($className, 'displayFieldName'), $field, $className),
                        $maxLength
                );
            }
        }

        if (Tools::getIsset('description_short') && $this->isProductFieldUpdated('description_short')) {
            $saveShort = Tools::getValue('description_short');
            $_POST['description_short'] = strip_tags(Tools::getValue('description_short'));
        }

        // Check description short size without html
        $limit = (int)Configuration::get('PS_PRODUCT_SHORT_DESC_LIMIT');
        if ($limit <= 0) {
            $limit = 400;
        }
        foreach ($languages as $language) {
            if ($this->isProductFieldUpdated('description_short', $language['id_lang']) && ($value = Tools::getValue('description_short_'.$language['id_lang']))) {
                if (Tools::strlen(strip_tags($value)) > $limit) {
                    $this->errors[] = sprintf(
                            Tools::displayError('This %1$s field (%2$s) is too long: %3$d chars max (current count %4$d).'),
                            call_user_func(array($className, 'displayFieldName'), 'description_short'),
                            $language['name'],
                            $limit,
                            Tools::strlen(strip_tags($value))
                    );
                }
            }
        }

        // Check multilingual fields sizes
        foreach ($rules['sizeLang'] as $fieldLang => $maxLength) {
            foreach ($languages as $language) {
                $value = Tools::getValue($fieldLang.'_'.$language['id_lang']);
                if ($value && Tools::strlen($value) > $maxLength) {
                    $this->errors[] = sprintf(
                            Tools::displayError('The %1$s field is too long (%2$d chars max).'),
                            call_user_func(array($className, 'displayFieldName'), $fieldLang, $className),
                            $maxLength
                    );
                }
            }
        }

        if ($this->isProductFieldUpdated('description_short') && isset($_POST['description_short'])) {
            $_POST['description_short'] = $saveShort;
        }

        // Check fields validity
        foreach ($rules['validate'] as $field => $function) {
            if ($this->isProductFieldUpdated($field) && ($value = Tools::getValue($field))) {
                $res = true;
                if (Tools::strtolower($function) == 'iscleanhtml') {
                    if (!Validate::$function($value, (int)Configuration::get('PS_ALLOW_HTML_IFRAME'))) {
                        $res = false;
                    }
                } elseif (!Validate::$function($value)) {
                    $res = false;
                }

                if (!$res) {
                    $this->errors[] = sprintf(
                            Tools::displayError('The %s field is invalid.'),
                            call_user_func(array($className, 'displayFieldName'), $field, $className)
                    );
                }
            }
        }
        // Check multilingual fields validity
        foreach ($rules['validateLang'] as $fieldLang => $function) {
            foreach ($languages as $language) {
                if ($this->isProductFieldUpdated($fieldLang, $language['id_lang']) && ($value = Tools::getValue($fieldLang.'_'.$language['id_lang']))) {
                    if (!Validate::$function($value, (int)Configuration::get('PS_ALLOW_HTML_IFRAME'))) {
                        $this->errors[] = sprintf(
                                Tools::displayError('The %1$s field (%2$s) is invalid.'),
                                call_user_func(array($className, 'displayFieldName'), $fieldLang, $className),
                                $language['name']
                        );
                    }
                }
            }
        }

        // Categories
        if ($this->isProductFieldUpdated('id_category_default') && (!Tools::isSubmit('categoryBox') || !count(Tools::getValue('categoryBox')))) {
            $this->errors[] = $this->l('Products must be in at least one category.');
        }

        if ($this->isProductFieldUpdated('id_category_default') && (!is_array(Tools::getValue('categoryBox')) || !in_array(Tools::getValue('id_category_default'), Tools::getValue('categoryBox')))) {
            $this->errors[] = $this->l('This analyze must be in the default category.');
        }

        // Tags
        foreach ($languages as $language) {
            if ($value = Tools::getValue('tags_'.$language['id_lang'])) {
                if (!Validate::isTagsList($value)) {
                    $this->errors[] = sprintf(
                            Tools::displayError('The tags list (%s) is invalid.'),
                            $language['name']
                    );
                }
            }
        } */
    }




    public static class JeproLabAnalyzePackModel   extends JeproLabAnalyzeModel{
        protected static Map<Integer, List<JeproLabAnalyzeModel>> cachePackItems = new HashMap<>();
        protected static Map<Integer, Boolean> cacheIsPack = new HashMap<>();
        protected static Map<String, Boolean> cacheIsPacked = new HashMap<>();

        /**
         * Is analyze a pack?
         *
         * @param analyzeId analyze Id
         * @return bool
         */
        public static boolean isPack(int analyzeId){
            if (!JeproLabAnalyzePackModel.isFeaturePublished() || analyzeId <= 0) {
                return false;
            }

            if (!JeproLabAnalyzePackModel.cacheIsPack.containsKey(analyzeId)){
                String query = "SELECT COUNT(*) AS pack FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_pack") + " WHERE ";
                query += " analyze_pack_id = " + analyzeId;

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                JeproLabAnalyzePackModel.cacheIsPack.put(analyzeId, dataBaseObject.loadValue(query, "pack") > 0);

                closeDataBaseConnection(dataBaseObject);
            }
            return JeproLabAnalyzePackModel.cacheIsPack.get(analyzeId);
        }

        /**
         * This method is allow to know if a feature is used or active
         * @since 1.5.0.1
         * @return bool
         */
        public static boolean isFeaturePublished(){
            return JeproLabSettingModel.getIntValue("pack_feature_active") > 0;
        }

        /**
         *
         * @param analyzeId analyze id
         * @param langId laboratory id
         * @return List of items
         */
        public static List<JeproLabAnalyzeModel> getItems(int analyzeId, int langId){
            if (!JeproLabAnalyzePackModel.isFeaturePublished()){
                return new ArrayList<>();
            }

            if (JeproLabAnalyzePackModel.cachePackItems.containsKey(analyzeId)){
                return JeproLabAnalyzePackModel.cachePackItems.get(analyzeId);
            }
            String query = "SELECT analyze_item_id, analyze_attribute_item_id, quantity FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_pack");
            query += " WHERE analyze_pack_id = " + analyzeId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet analyzeSet = dataBaseObject.loadObjectList(query);
            //$result = Db::getInstance()->executeS('`'.("#__jeprolab_.'pack` where id_product_pack = '.(int)$id_product);
            List<JeproLabAnalyzeModel> items = new ArrayList<>();
            if(analyzeSet != null) {
                try {
                    JeproLabAnalyzeModel analyze;
                    while(analyzeSet.next()) {
                        analyze = new JeproLabAnalyzeModel(analyzeSet.getInt("analyze_item_id"), false, langId);
                        analyze.loadStockData();
                        analyze.pack_quantity = analyzeSet.getInt("quantity");
                        analyze.pack_analyze_attribute_id = analyzeSet.getInt("analyze_attribute_item_id") > 0 ? analyzeSet.getInt("analyze_attribute_item_id"): 0;
                        if (analyzeSet.getInt("analyze_attribute_item_id") > 0) {
                            query = "SELECT attribute_group_lang." + JeproLabDataBaseConnector.quoteName("name") + " AS group_name, attribute_lang.";
                            query += JeproLabDataBaseConnector.quoteName("name") + " AS attribute_name FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute");
                            query += " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute") + " LEFT JOIN ";
                            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute_combination") + " AS analyze_attribute_combination ON analyze_attribute_combination.";
                            query += JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + " = analyze_attribute." + JeproLabDataBaseConnector.quoteName("analyze_attribute_id");
                            query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_attribute") + " AS attribute ON attribute." + JeproLabDataBaseConnector.quoteName("attribute_id");
                            query += " = analyze_attribute_combination."  + JeproLabDataBaseConnector.quoteName("attribute_id") + " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_attribute_group");
                            query += " AS attribute_group ON attribute_group." + JeproLabDataBaseConnector.quoteName("attribute_group_id") + " = attribute." + JeproLabDataBaseConnector.quoteName("attribute_group_id");
                            query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_attribute_lang") + " AS attribute_lang ON (attribute." + JeproLabDataBaseConnector.quoteName("attribute_id");
                            query += " = attribute_lang." + JeproLabDataBaseConnector.quoteName("attribute_id") + " AND attribute_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = ";
                            query += JeproLabContext.getContext().language.language_id + ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_attribute_group_lang") +  " AS attribute_group_lang ";
                            query += " attribute_group_lang ON (attribute_group." + JeproLabDataBaseConnector.quoteName("attribute_group_id") + " = attribute_group_lang.'=";
                            query += JeproLabDataBaseConnector.quoteName("attribute_group_id") + " AND attribute_group_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = ";
                            query += JeproLabContext.getContext().language.language_id + ") WHERE analyze_attribute." + JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + " = ";
                            query += analyzeSet.getInt("analyze_attribute_item_id") + " GROUP BY analyze_attribute." + JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + ", attribute_group.";
                            query += JeproLabDataBaseConnector.quoteName("attribute_group_id") + " ORDER BY analyze_attribute."  + JeproLabDataBaseConnector.quoteName("analyze_attribute_id");

                            //JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                            ResultSet combinationSet = dataBaseObject.loadObjectList(query);
                            if(combinationSet != null){
                                while (combinationSet.next()) {
                                    analyze.name.put("lang_" + langId, analyze.name.get("lang_" + langId) + "_" + combinationSet.getString("group_name") + "_" + combinationSet.getString("attribute_name"));
                                }
                            }
                        }
                        items.add(analyze);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }else{
                closeDataBaseConnection(dataBaseObject);
            }
            JeproLabAnalyzePackModel.cachePackItems.put(analyzeId, items);// = $array_result;
            return JeproLabAnalyzePackModel.cachePackItems.get(analyzeId);
        }

        public static int getQuantity(int analyzeId, int analyzeAttributeId) {
            //TODO edit me
            return 0;
        }
    }
}
