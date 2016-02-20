package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.controllers.JeproLabCategoryController;
import com.jeprolab.models.core.JeproLabFactory;
import com.jeprolab.models.tax.JeproLabTaxCalculator;
import com.jeprolab.models.tax.JeproLabTaxManagerFactory;
import com.jeprolab.models.tax.JeproLabTaxRulesManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 01/02/14.
 */
public class JeproLabAnalyzeModel extends JeproLabModel {
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

    /** @var int Minimal quantity for add to cart * /
    public $minimal_quantity = 1;

    /** @var string available_now * /
    public $available_now;

    /** @var string available_later * /
    public $available_later;

    /** @var float Price in euros */
    public float price = 0;

    public JeproLabSpecificPriceModel specificPrice = null;

    /** @var float Additional shipping cost */
    public float additional_shipping_cost = 0;

    /** @var float Wholesale Price in euros * /
    public $wholesale_price = 0;

    /** @var bool on_sale */
    public boolean on_sale = false;

    /** @var bool online_only */
    public boolean online_only = false;

    /** @var string unity */
    public String unity = null;

    /** @var float price for product's unity */
    public float unit_price;

    /** @var float price for product's unity ratio */
    public float unit_price_ratio = 0;

    /** @var float EcoTax */
    public float ecoTax = 0;

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
    public boolean newAnalyze = false;

    /** @var int Number of uploadable files (concerning customizable products) */
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

    /** @var string Enumerated (enum) product condition (new, used, refurbished) * /
    public $condition;

    /** @var bool Show price of Product */
    public boolean show_price = true;

    /** @var bool is the product indexed in the search index? * /
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
     * @var float Base price of the product
     * @deprecated 1.6.0.13
     */
    public float base_price;

    public int tax_rules_group_id = 1;

    /**
     * We keep this variable for retrocompatibility for themes
     * @deprecated 1.5.0
     */
    public int default_color_id = 0;

    /**
     * @since 1.5.0
     * @var bool Tells if the product uses the advanced stock management
     */
    public boolean advanced_stock_management = false;
    public boolean out_of_stock;
    public boolean depends_on_stock;

    public boolean isFullyLoaded = false;

    public boolean cache_is_pack;
    public boolean cache_has_attachments;
    public boolean is_virtual;
    public int analyze_pack_attribute_id;
    public int cache_default_attribute;
    private List<Integer> laboratory_list_id = new ArrayList<>();
    private Map<Integer, JeproLabLanguageModel> languages = null;

    /**
     * @var string If product is populated, this property contain the rewrite link of the default category
     */
    public String category;

    /**
     * @var int tell the type of stock management to apply on the pack
     * /
    public $pack_stock_type = 3;
     */
    public static int _taxCalculationMethod = 0;
    protected static Map<String, Float> _prices = new HashMap<>();
    protected static Map<String, Map<Integer, Map<String,Float>>> _pricesLevel2 = new HashMap<>();
    //protected static $_incat = array();

    protected static Map<Integer, Map<Integer, Integer>> combinations = new HashMap<>();

    private static JeproLabSpecificPriceModel static_specific_price;
    /**
     * @since 1.5.6.1
     * @var array $_cart_quantity is deprecated since 1.5.6.1
     * /
    protected static $_cart_quantity = array();

    protected static $_tax_rules_group = array();
    protected static $_cacheFeatures = array();
    protected static $_frontFeaturesCache = array();
    protected static $producPropertiesCache = array();
*/
    protected static boolean multi_lang_lab = true;
    protected static JeproLabAddressModel address = null;
    protected static JeproLabContext static_context = null;
    /* @var array cache stock data in getStock() method * /
    protected static $cacheStock = array();

    public static $definition = array(
            'table' => 'product',
                    'primary' => 'id_product',
                    'multilang' => true,
                    'multilang_shop' => true,
                    'fields' => array(
            /* Classic fields * /
                    'id_shop_default' =>            array('type' => JeproLabAnalyzeModel.TYPE_INT, 'validate' => 'isUnsignedId'),
    'id_manufacturer' =>            array('type' => JeproLabAnalyzeModel.TYPE_INT, 'validate' => 'isUnsignedId'),
    'id_supplier' =>                array('type' => JeproLabAnalyzeModel.TYPE_INT, 'validate' => 'isUnsignedId'),
    'reference' =>                    array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'validate' => 'isReference', 'size' => 32),
    'supplier_reference' =>        array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'validate' => 'isReference', 'size' => 32),
    'location' =>                    array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'validate' => 'isReference', 'size' => 64),
    'width' =>                        array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'validate' => 'isUnsignedFloat'),
    'height' =>                    array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'validate' => 'isUnsignedFloat'),
    'depth' =>                        array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'validate' => 'isUnsignedFloat'),
    'weight' =>                    array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'validate' => 'isUnsignedFloat'),
    'quantity_discount' =>            array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'validate' => 'isBool'),
    'ean13' =>                        array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'validate' => 'isEan13', 'size' => 13),
    'upc' =>                        array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'validate' => 'isUpc', 'size' => 12),
    'cache_is_pack' =>                array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'validate' => 'isBool'),
    'cache_has_attachments' =>        array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'validate' => 'isBool'),
    'is_virtual' =>                array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'validate' => 'isBool'),

            /* Shop fields * /
    'id_category_default' =>        array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedId'),
    'id_tax_rules_group' =>        array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedId'),
    'on_sale' =>                    array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'online_only' =>                array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'ecotax' =>                    array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'shop' => true, 'validate' => 'isPrice'),
    'minimal_quantity' =>            array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedInt'),
    'price' =>                        array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'shop' => true, 'validate' => 'isPrice', 'required' => true),
    'wholesale_price' =>            array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'shop' => true, 'validate' => 'isPrice'),
    'unity' =>                        array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'shop' => true, 'validate' => 'isString'),
    'unit_price_ratio' =>            array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'shop' => true),
    'additional_shipping_cost' =>    array('type' => JeproLabAnalyzeModel.TYPE_FLOAT, 'shop' => true, 'validate' => 'isPrice'),
    'customizable' =>                array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedInt'),
    'text_fields' =>                array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedInt'),
    'uploadable_files' =>            array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedInt'),
    'active' =>                    array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'redirect_type' =>                array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'shop' => true, 'validate' => 'isString'),
    'id_product_redirected' =>        array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedId'),
    'available_for_order' =>        array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'available_date' =>            array('type' => JeproLabAnalyzeModel.TYPE_DATE, 'shop' => true, 'validate' => 'isDateFormat'),
    'condition' =>                    array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'shop' => true, 'validate' => 'isGenericName', 'values' => array('new', 'used', 'refurbished'), 'default' => 'new'),
            'show_price' =>                array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'indexed' =>                    array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'visibility' =>                array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'shop' => true, 'validate' => 'isProductVisibility', 'values' => array('both', 'catalog', 'search', 'none'), 'default' => 'both'),
            'cache_default_attribute' =>    array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true),
    'advanced_stock_management' =>    array('type' => JeproLabAnalyzeModel.TYPE_BOOL, 'shop' => true, 'validate' => 'isBool'),
    'date_add' =>                    array('type' => JeproLabAnalyzeModel.TYPE_DATE, 'shop' => true, 'validate' => 'isDate'),
    'date_upd' =>                    array('type' => JeproLabAnalyzeModel.TYPE_DATE, 'shop' => true, 'validate' => 'isDate'),
    'pack_stock_type' =>            array('type' => JeproLabAnalyzeModel.TYPE_INT, 'shop' => true, 'validate' => 'isUnsignedInt'),

            /* Lang fields * /
    'meta_description' =>            array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'size' => 255),
    'meta_keywords' =>                array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'size' => 255),
    'meta_title' =>                array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'size' => 128),
    'link_rewrite' =>    array(
            'type' => JeproLabAnalyzeModel.TYPE_STRING,
            'lang' => true,
                    'validate' => 'isLinkRewrite',
                    'required' => true,
                    'size' => 128,
                    'ws_modifier' => array(
                    'http_method' => WebserviceRequest::HTTP_POST,
            'modifier' => 'modifierWsLinkRewrite'
    )
    ),
            'name' =>                        array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'lang' => true, 'validate' => 'isCatalogName', 'required' => true, 'size' => 128),
    'description' =>                array('type' => JeproLabAnalyzeModel.TYPE_HTML, 'lang' => true, 'validate' => 'isCleanHtml'),
    'description_short' =>            array('type' => JeproLabAnalyzeModel.TYPE_HTML, 'lang' => true, 'validate' => 'isCleanHtml'),
    'available_now' =>                array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'size' => 255),
    'available_later' =>            array('type' => JeproLabAnalyzeModel.TYPE_STRING, 'lang' => true, 'validate' => 'IsGenericName', 'size' => 255),
    ),
            'associations' => array(
            'manufacturer' =>                array('type' => JeproLabAnalyzeModel.HAS_ONE),
    'supplier' =>                    array('type' => JeproLabAnalyzeModel.HAS_ONE),
    'default_category' =>            array('type' => JeproLabAnalyzeModel.HAS_ONE, 'field' => 'id_category_default', 'object' => 'Category'),
    'tax_rules_group' =>            array('type' => JeproLabAnalyzeModel.HAS_ONE),
    'categories' =>                    array('type' => JeproLabAnalyzeModel.HAS_MANY, 'field' => 'id_category', 'object' => 'Category', 'association' => 'category_product'),
    'stock_availables' =>            array('type' => JeproLabAnalyzeModel.HAS_MANY, 'field' => 'id_stock_available', 'object' => 'StockAvailable', 'association' => 'stock_availables'),
    ),
            );

    protected $webserviceParameters = array(
            'objectMethods' => array(
            'add' => 'addWs',
            'update' => 'updateWs'
    ),
    'objectNodeNames' => 'products',
            'fields' => array(
            'id_manufacturer' => array(
            'xlink_resource' => 'manufacturers'
    ),
    'id_supplier' => array(
            'xlink_resource' => 'suppliers'
    ),
    'id_category_default' => array(
            'xlink_resource' => 'categories'
    ),
    'new' => array(),
    'cache_default_attribute' => array(),
    'id_default_image' => array(
            'getter' => 'getCoverWs',
                    'setter' => 'setCoverWs',
                    'xlink_resource' => array(
                    'resourceName' => 'images',
            'subResourceName' => 'products'
    )
    ),
            'id_default_combination' => array(
            'getter' => 'getWsDefaultCombination',
                    'setter' => 'setWsDefaultCombination',
                    'xlink_resource' => array(
                    'resourceName' => 'combinations'
    )
    ),
            'id_tax_rules_group' => array(
            'xlink_resource' => array(
            'resourceName' => 'tax_rule_groups'
    )
    ),
            'position_in_category' => array(
            'getter' => 'getWsPositionInCategory',
                    'setter' => 'setWsPositionInCategory'
    ),
    'manufacturer_name' => array(
            'getter' => 'getWsManufacturerName',
                    'setter' => false
    ),
    'quantity' => array(
            'getter' => false,
                    'setter' => false
    ),
    'type' => array(
            'getter' => 'getWsType',
                    'setter' => 'setWsType',
    ),
    ),
            'associations' => array(
            'categories' => array(
            'resource' => 'category',
            'fields' => array(
                    'id' => array('required' => true),
    )
            ),
            'images' => array(
            'resource' => 'image',
                    'fields' => array('id' => array())
            ),
            'combinations' => array(
            'resource' => 'combination',
                    'fields' => array(
                    'id' => array('required' => true),
    )
            ),
            'product_option_values' => array(
            'resource' => 'product_option_value',
                    'fields' => array(
                    'id' => array('required' => true),
    )
            ),
            'product_features' => array(
            'resource' => 'product_feature',
                    'fields' => array(
                    'id' => array('required' => true),
    'id_feature_value' => array(
            'required' => true,
                    'xlink_resource' => 'product_feature_values'
    ),
    )
            ),
            'tags' => array('resource' => 'tag',
                                    'fields' => array(
                                    'id' => array('required' => true),
    )),
            'stock_availables' => array('resource' => 'stock_available',
                                                'fields' => array(
                                                'id' => array('required' => true),
    'id_product_attribute' => array('required' => true),
    ),
            'setter' => false
            ),
            'accessories' => array(
            'resource' => 'product',
                    'api' => 'products',
                    'fields' => array(
                    'id' => array(
                    'required' => true,
            'xlink_resource' => 'product'),
    )
            ),
            'product_bundle' => array(
            'resource' => 'product',
                    'api' => 'products',
                    'fields' => array(
                    'id' => array('required' => true),
    'quantity' => array(),
    ),
            ),
            ),
            );

    const CUSTOMIZE_FILE = 0;
    const CUSTOMIZE_TEXTFIELD = 1;

    /**
     * Note:  prefix is "PTYPE" because TYPE_ is used in ObjectModel (definition)
     */
    public static final int SIMPLE_ANALYZE = 0;
    public static final int PACK_ANALYZE = 1;
    //const PTYPE_VIRTUAL = 2;

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

        if (labId > 0  && this.isMultiLab()) {
            this.laboratory_id  = labId;
            this.get_lab_from_context = false;
        }

        if (this.isMultiLab() && this.laboratory_id <= 0) {
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        if (analyzeId > 0) {
            String cacheKey = "jeprolab_analyze_model_" + analyzeId + "_" + (full ? "true" : "false") + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT analyze.* FROM " + dataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ";
                String where = "";
                if(langId > 0){
                    query += "LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (";
                    query += "analyze.analyze_id = analyze_lang.analyze_id AND analyze_lang.lang_id = " + langId + ")";
                    if(this.laboratory_id > 0 && JeproLabAnalyzeModel.multi_lang_lab){
                        where = " AND analyze_lang.lab_id = " + this.laboratory_id;
                    }
                }

                if(JeproLabLaboratoryModel.isTableAssociated("analyze")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyze.analyze_id = ";
                    query += " analyze_lab.analyze_id AND analyze_lab.lab_id = " + this.laboratory_id +  ")";
                }
                query += " WHERE analyze.analyze_id = " + analyzeId + where;

                dataBaseObject.setQuery(query);
                ResultSet analyzeResult = dataBaseObject.loadObject();
                try{
                    while(analyzeResult.next()){
                        this.analyze_id = analyzeResult.getInt("analyze_id");
                        this.supplier_id = analyzeResult.getInt("supplier_id");
                        this.technician_id = analyzeResult.getInt("technician_id");
                        this.manufacturer_id = analyzeResult.getInt("manufacturer_id");
                        this.default_category_id = analyzeResult.getInt("default_category_id");
                        this.default_laboratory_id = analyzeResult.getInt("default_laboratory_id");
                        this.tax_rules_group_id = analyzeResult.getInt("tax_rules_group_id");
                        this.on_sale = analyzeResult.getInt("on_sale") > 0;
                        this.online_only = analyzeResult.getInt("online_only") > 0;
                        this.ean13 = analyzeResult.getString("ean13");
                        this.upc = analyzeResult.getString("upc");
                        this.unity = analyzeResult.getString("unity");
                        this.unit_price = analyzeResult.getFloat("unit_price");
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
                        this.published = analyzeResult.getInt("published") > 0;
                    }
                }catch(SQLException ignored){

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
            }
        }

        if (full && this.analyze_id > 0) {
            if (context == null) {
                context = JeproLabContext.getContext();
            }

            this.isFullyLoaded = full;
            //this.tax_name = 'deprecated'; // The applicable JeproLabTaxManagerFactory may be BOTH the product one AND the state one (moreover this variable is some deadcode)
            this.manufacturer_name = JeproLabManufacturerModel.getNameById(this.manufacturer_id);
            this.supplier_name = JeproLabSupplierModel.getNameById((int) this.supplier_id);
            int addressId = 0;
            /*if (s_object(context.cart) && context.cart.{JeproLabSettingModel.get('PS_TAX_ADDRESS_TYPE')} != null) {
                $address = context.cart->{JeproLabSettingModel.get('PS_TAX_ADDRESS_TYPE')};
            } */

            this.tax_rate = this.getTaxesRate(new JeproLabAddressModel(addressId));

            this.newAnalyze = this.isNew();

            // Keep base price
            this.base_price = this.price;

            this.price = JeproLabAnalyzeModel.getStaticPrice(this.analyze_id, false, 0, 6, false, true, 1, false, 0, 0, 0);
            specificPrice = JeproLabAnalyzeModel.static_specific_price;
            this.unit_price = (this.unit_price_ratio != 0  ? this.price / this.unit_price_ratio : 0);
            if (this.analyze_id > 0) {
                this.tags = JeproLabTagModel.getAnalyzeTags(this.analyze_id);
            }

            this.loadStockData();
        }

        if (this.default_category_id > 0) {
            this.category = JeproLabCategoryModel.getLinkRewrite(this.default_category_id, langId);
        }
    }

    public boolean isMultiLab(){
        return JeproLabLaboratoryModel.isTableAssociated("analyse") || JeproLabCategoryModel.multiLangLab;
    }

    public boolean isLangMultiLab(){
        return JeproLabCategoryModel.multiLang && JeproLabCategoryModel.multiLangLab;
    }

    /*
     * @see ObjectModel::getFieldsShop()
     * @return array
     * /
    public function getFieldsShop()
    {
        $fields = parent::getFieldsShop();
        if (is_null(this.update_fields) || (!empty(this.update_fields['price']) && !empty(this.update_fields['unit_price']))) {
            $fields['unit_price_ratio'] = (float)this.unit_price > 0 ? this.price / this.unit_price : 0;
        }
        $fields['unity'] = pSQL(this.unity);

        return $fields;
    }
*/

    protected void removeTaxFromEcoTax(){
        Float ecoTax = JeproLab.request.getPost().containsKey("ecotax") ? Float.parseFloat(JeproLab.request.getPost().get("ecotax")) : 0;
        if (ecoTax > 0) {
            ecoTax = JeproLabTools.roundPrice(ecoTax/(1 + JeproLabTaxModel.getAnalyzeEcotaxRate()/100), 6);
            JeproLab.request.getPost().put("ecotax", ecoTax.toString());
        }
    }
    public int save(){
        Map<String, String> post = JeproLab.request.getPost();

        JeproLabContext context = JeproLabContext.getContext();
        if(languages == null) {
            languages = JeproLabLanguageModel.getLanguages();
        }
        checkAnalyze();
        removeTaxFromEcoTax();
        List<Integer> labIds = new ArrayList<>();
        if(JeproLabLaboratoryModel.isTableAssociated("analyze")){
            labIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
            if(this.laboratory_list_id.size() > 0){
                labIds = this.laboratory_list_id;
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
        String analyzeReference = post.get("reference");
        String analyzeEan13 = post.get("ean13");
        String analyzeUpc = post.get("upc");
        int analyzePublished = Integer.parseInt(post.get("published"));
        String analyzeRedirectType = post.get("redirect_type");
        int availableForOrder = Integer.parseInt(post.get("available_for_order"));
        int showPrice = Integer.parseInt(post.get("show_price"));
        int onlineOnly = 0; //Integer.parseInt(post.get("online_only"));
        String analyzeVisibility = post.get("visibility");
        int analyzeRedirectId = Integer.parseInt(post.get("redirect_id"));
        String analyzeDelay = post.get("delay");

        //this.date_add =  ;

        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_analyze") + "("  + dataBaseObject.quoteName("reference") + ", " ;
        query +=  dataBaseObject.quoteName("ean13") + ", " + dataBaseObject.quoteName("upc") + ", "  + dataBaseObject.quoteName("published");
        query += ", "  + dataBaseObject.quoteName("redirect_type") + ", " + dataBaseObject.quoteName("visibility") + ", " ;
        query += dataBaseObject.quoteName("delay") + ", "  + dataBaseObject.quoteName("available_for_order") + ", "  + dataBaseObject.quoteName("show_price") + ", ";
        query += dataBaseObject.quoteName("online_only") + ", " + dataBaseObject.quoteName("default_lab_id") + ", " + dataBaseObject.quoteName("analyze_redirected_id");
        query += ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd") + " ) VALUES(" + dataBaseObject.quote(analyzeReference, true) ;
        query += ", " + dataBaseObject.quote(analyzeEan13, true) + ", " + dataBaseObject.quote(analyzeUpc, true) + ", " + analyzePublished;
        query += ", " + dataBaseObject.quote(analyzeRedirectType, true) + ", " + dataBaseObject.quote(analyzeVisibility, true) + ", " + analyzeDelay;
        query += ", " + availableForOrder + ", " + showPrice + ", " + onlineOnly + ", " + default_laboratory_id + ", " + analyzeRedirectId + ", ";
        query += dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"))+ ", " + dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + ") ";

        System.out.println(query);
        /*if (!parent::add($autodate, $null_values)) {
        return false;
    }

        $id_shop_list = JeproLabLaboratoryModel.getContextListShopID();
        if (this.getType() == JeproLabAnalyzeModel.PTYPE_VIRTUAL) {
            foreach ($id_shop_list as $value) {
                StockAvailable::setProductOutOfStock((int)this.id, 1, $value);
            }

            if (this.active && !JeproLabSettingModel.get('PS_VIRTUAL_PROD_FEATURE_ACTIVE')) {
                JeproLabSettingModel.updateGlobalValue('PS_VIRTUAL_PROD_FEATURE_ACTIVE', '1');
            }
        } else {
            foreach ($id_shop_list as $value) {
                StockAvailable::setProductOutOfStock((int)this.id, 2, $value);
            }
        }

        this.setGroupReduction();
        Hook::exec('actionProductSave', array('id_product' => (int)this.id, 'product' => $this)); */
        return 0;
    }

    public boolean update(){
        /*$return = parent::update($null_values);
        this.setGroupReduction();

        // Sync stock Reference, EAN13 and UPC
        if (JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT') && StockAvailable::dependsOnStock(this.id, JeproLabContext.getContext()->shop->id)) {
        Db::getInstance()->update('stock', array(
                        'reference' => pSQL(this.reference),
                'ean13'     => pSQL(this.ean13),
                'upc'        => pSQL(this.upc),
        ), 'id_product = '.(int)this.id.' AND id_product_attribute = 0');
    }

        Hook::exec('actionProductSave', array('id_product' => (int)this.id, 'product' => $this));
        Hook::exec('actionProductUpdate', array('id_product' => (int)this.id, 'product' => $this));
        if (this.getType() == JeproLabAnalyzeModel.PTYPE_VIRTUAL && this.active && !JeproLabSettingModel.get('PS_VIRTUAL_PROD_FEATURE_ACTIVE')) {
        JeproLabSettingModel.updateGlobalValue('PS_VIRTUAL_PROD_FEATURE_ACTIVE', '1');
    }
*/
        return true;
    }

    public static void initPricesComputation(){
        initPricesComputation(0);
    }

    public static void initPricesComputation(int customerId){
        if (customerId > 0) {
            JeproLabCustomerModel customer = new JeproLabCustomerModel(customerId);
            if (customer.customer_id <= 0) {
                JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_CUSTOMER_NOT_FOUND_MESSAGE"));
            }
            JeproLabAnalyzeModel._taxCalculationMethod = JeproLabGroupModel.getPriceDisplayMethod(customer.default_group_id);
            JeproLabCartModel currentCart = JeproLabContext.getContext().cart;
            int addressId = 0;
            if (currentCart != null && currentCart.cart_id > 0) {
                if(JeproLabSettingModel.getStringValue("tax_address_type").equals("delivery_address_id")) {
                    addressId = currentCart.delivery_address_id;
                }else{
                    addressId = currentCart.invoice_address_id;
                }
            }
            ResultSet addressInfo = JeproLabAddressModel.getCountryAndState(addressId);
            String vatNumber = "";
            int countryId = 0;
            if(addressInfo != null){
                try{
                    if(addressInfo.next()){
                        vatNumber = addressInfo.getString("vat_number");
                        countryId = addressInfo.getInt("country_id");
                    }
                }catch(SQLException ignored){

                }
            }

            if (JeproLabAnalyzeModel._taxCalculationMethod != JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED && !vatNumber.equals("")
                    && countryId != JeproLabSettingModel.getIntValue("vat_number_country") && JeproLabSettingModel.getIntValue("vat_number_management") > 0) {
                JeproLabAnalyzeModel._taxCalculationMethod = JeproLabConfigurationSettings.JEPROLAB_TAX_EXCLUDED;
            }
        } else {
            JeproLabAnalyzeModel._taxCalculationMethod = JeproLabGroupModel.getPriceDisplayMethod(JeproLabGroupModel.getCurrent().group_id);
        }
    }

    public static int getTaxCalculationMethod(){
        return getTaxCalculationMethod(0);
    }

    public static int getTaxCalculationMethod(int customerId){
        if (JeproLabAnalyzeModel._taxCalculationMethod <= 0 || customerId > 0) {
            JeproLabAnalyzeModel.initPricesComputation(customerId);
        }

        return JeproLabAnalyzeModel._taxCalculationMethod;
    }

    /**
     * Move a product inside its category
     * @param way Up (1)  or Down (0)
     * @param position
     * return boolean Update result
     */
    public boolean updatePosition(boolean way, int position){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        int categoryId = JeproLab.request.getPost().containsKey("category_id") ? Integer.parseInt(JeproLab.request.getPost().get("category_id")) : 1;
        String query = "SELECT category_analyze." + dataBaseObject.quoteName("analyze_id") + ", category_analyze." + dataBaseObject.quoteName("position");
        query += ", category_analyze." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_category") ;
        query += " AS category_analyze WHERE category_analyze." + dataBaseObject.quoteName("category_id") + " = " + categoryId + " ORDER BY ";
        query += " category_analyze." + dataBaseObject.quoteName("position") + " ASC ";

        boolean result = true;
        dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObject();
        List<JeproLabAnalyzeModel> analyzeList = new ArrayList<>();
        if(resultSet != null){
            try{
                int movedAnalyzeId = 0;
                int movedAnalyzePosition = 0;
                int movedAnalyzeCategoryId = 0;
                while(resultSet.next()){
                    if(this.analyze_id == resultSet.getInt("analyze_id")) {
                        movedAnalyzeId = resultSet.getInt("analyze_id");
                        movedAnalyzeCategoryId = resultSet.getInt("category_id");
                        movedAnalyzePosition = resultSet.getInt("position");
                    }
                }

                if(movedAnalyzeId <= 0 || position <= 0) {
                    return false;
                }

                // < and > statements rather than BETWEEN operator
                // since BETWEEN is treated differently according to databases
                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_analyze_category") + " AS category_analyze INNER JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze");
                query += " AS analyze ON (analyze." + dataBaseObject.quoteName("analyze_id") + " = category_analyze." + dataBaseObject.quoteName("analyze_id") + ") " ;
                query += JeproLabLaboratoryModel.addSqlAssociation("analyze") + " SET category_analyze." + dataBaseObject.quoteName("position") + " = " + dataBaseObject.quoteName("position");
                query += (way ? "- 1" : "+ 1") + ", analyze." + dataBaseObject.quoteName("date_upd") + " = " + JeproLabTools.date("Y-m-d H:i:s") + ", analyze_lab.";
                query += dataBaseObject.quoteName("date_upd") +  " = " + JeproLabTools.date("Y-m-d H:i:s") + " WHERE category_analyze." + dataBaseObject.quoteName("position");
                query += (way ? "> " + movedAnalyzePosition + " AND " + dataBaseObject.quoteName("position") +  " <= "  + position : " < " + movedAnalyzePosition + " AND " + dataBaseObject.quoteName("position") + " >= " + position);
                query += " AND " + dataBaseObject.quoteName("category_id") + " = " + movedAnalyzeCategoryId;

                dataBaseObject.setQuery(query);
                result &= dataBaseObject.query();

                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_analyze_category") + " AS category_analyze INNER JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze");
                query += " AS analyze ON (analyze." + dataBaseObject.quoteName("analyze_id") + " = category_analyze." + dataBaseObject.quoteName("analyze_id") + ") ";
                query += JeproLabLaboratoryModel.addSqlAssociation("analyze") + " SET category_analyze." + dataBaseObject.quoteName("position") + " = " + position ;
                query += ", analyze." + dataBaseObject.quoteName("date_upd") + " = " + JeproLabTools.date("Y-m-d H:i:s") + ", analyze_lab." + dataBaseObject.quoteName("date_upd");
                query += " = " + JeproLabTools.date("Y-m-d H:i:s") + " WHERE category_analyze." + dataBaseObject.quoteName("analyze_id") + " = " + movedAnalyzeId;
                query += " AND category_analyze." + dataBaseObject.quoteName("category_id") + " = " + movedAnalyzeCategoryId;
                dataBaseObject.setQuery(query);
                result &= dataBaseObject.query();
                //Hook::exec('actionProductUpdate', array('id_product' => (int)this.id, 'product' => $this));
                return result;
            }catch (SQLException ignored){
                return false;
            }
        }else{
            return false;
        }
    }
/*
    public static function cleanPositions(int categoryId){
        return cleanPositions(categoryId, 0);
    }

     /**
     * Reorder product position in category $id_category.
     * Call it after deleting a product from a category.
     *
     * @param categoryId
     * /
    public static function cleanPositions(int categoryId, int position){
        boolean result = true;

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("analyze_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_category");
        query += " WHERE " + staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

        if (position <= 0){
            query += " ORDER BY " + staticDataBaseObject.quoteName("position");

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObject();

            int $total = 0;
            if(resultSet != null){
                try{
                    while (resultSet.next()){
                        ++$total;
                    }
                    for ($i = 0; $i < $total; $i++) {
                        $return &= Db::getInstance()->update(
                                'category_product',
                                array('position' => $i),
                                '`id_category` = '.(int)$id_category.' AND `id_product` = '.(int)$result[$i]['id_product']
                        );
                        $return &= Db::getInstance()->execute(
                                'UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p'.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
                                        SET p.`date_upd` = "'.date('Y-m-d H:i:s').'", product_shop.`date_upd` = "'.date('Y-m-d H:i:s').'"
                        WHERE p.`id_product` = '.(int)$result[$i]['id_product']
                        );
                    }
                }catch (SQLException ignored){

                }
            }
        } else {
            query += 
            $result = Db::getInstance()->executeS('
                    SELECT `id_product`
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
            WHERE `id_category` = '.(int)$id_category.' AND `position` > '.(int)$position.'
            ORDER BY `position`
            ');
            $total = count($result);
            $return &= Db::getInstance()->update(
                    'category_product',
                    array('position' => array('type' => 'sql', 'value' => '`position`-1')),
            '`id_category` = '.(int)$id_category.' AND `position` > '.(int)$position
            );

            for ($i = 0; $i < $total; $i++) {
                $return &= Db::getInstance()->execute(
                        'UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p'.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
                SET p.`date_upd` = "'.date('Y-m-d H:i:s').'", product_shop.`date_upd` = "'.date('Y-m-d H:i:s').'"
                WHERE p.`id_product` = '.(int)$result[$i]['id_product']
                );
            }
        }
        return $return;
    }
*/
    public static int getDefaultAttribute(int analyzeId){
        return getDefaultAttribute(analyzeId, 0, false);
    }

    public static int getDefaultAttribute(int analyzeId, int minimumQuantity){
        return getDefaultAttribute(analyzeId, minimumQuantity, false);
    }
    /**
     * Get the default attribute for a product
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
/*
    public function setAvailableDate($available_date = '0000-00-00')
    {
        if (Validate::isDateFormat($available_date) && this.available_date != $available_date) {
        this.available_date = $available_date;
        return this.update();
    }
        return false;
    }

    /**
     * For a given id_product and id_product_attribute, return available date
     *
     * @param int $id_product
     * @param int $id_product_attribute Optional
     * @return string/null
     * /
    public static function getAvailableDate($id_product, $id_product_attribute = null)
    {
        $sql = 'SELECT';

        if ($id_product_attribute === null) {
            $sql .= ' p.`available_date`';
        } else {
            $sql .= ' IF(pa.`available_date` = "0000-00-00", p.`available_date`, pa.`available_date`) AS available_date';
        }

        $sql .= ' FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p';

        if ($id_product_attribute !== null) {
            $sql .= ' LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa ON (pa.`id_product` = p.`id_product`)';
        }

        $sql .= JeproLabLaboratoryModel.addSqlAssociation('product', 'p');

        if ($id_product_attribute !== null) {
            $sql .= JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa');
        }

        $sql .= ' WHERE p.`id_product` = '.(int)$id_product;

        if ($id_product_attribute !== null) {
            $sql .= ' AND pa.`id_product` = '.(int)$id_product.' AND pa.`id_product_attribute` = '.(int)$id_product_attribute;
        }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql);

        if ($result == '0000-00-00') {
            $result = null;
        }

        return $result;
    }

    public static function updateIsVirtual($id_product, $is_virtual = true)
    {
        Db::getInstance()->update('product', array(
                    'is_virtual' => (bool)$is_virtual,
            ), 'id_product = '.(int)$id_product);
    }

    /**
     * @see ObjectModel::validateField()
     * /
    public function validateField($field, $value, $id_lang = null, $skip = array(), $human_errors = false)
    {
        if ($field == 'description_short') {
            $limit = (int)JeproLabSettingModel.get('PS_PRODUCT_SHORT_DESC_LIMIT');
            if ($limit <= 0) {
                $limit = 800;
            }

            $size_without_html = Tools::strlen(strip_tags($value));
            $size_with_html = Tools::strlen($value);
            this.def['fields']['description_short']['size'] = $limit + $size_with_html - $size_without_html;
        }
        return parent::validateField($field, $value, $id_lang, $skip, $human_errors);
    }

    public function toggleStatus() {
        //test if the product is active and if redirect_type is empty string and set default value to id_product_redirected & redirect_type
        //  /!\ after parent::toggleStatus() active will be false, that why we set 404 by default :p
        if (this.active) {
            //case where active will be false after parent::toggleStatus()
            this.id_product_redirected = 0;
            this.redirect_type = '404';
        } else {
            //case where active will be true after parent::toggleStatus()
            this.id_product_redirected = 0;
            this.redirect_type = '';
        }
        return parent::toggleStatus();
    }

    public function delete()
    {
        /*
         * @since 1.5.0
         * It is NOT possible to delete a product if there are currently:
         * - physical stock for this product
         * - supply order(s) for this product
         * /
        if (JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT') && this.advanced_stock_management) {
        $stock_manager = StockManagerFactory::getManager();
        $physical_quantity = $stock_manager->getProductPhysicalQuantities(this.id, 0);
        $real_quantity = $stock_manager->getProductRealQuantities(this.id, 0);
        if ($physical_quantity > 0) {
            return false;
        }
        if ($real_quantity > $physical_quantity) {
            return false;
        }

        $warehouse_product_locations = Adapter_ServiceLocator::get('Core_Foundation_Database_EntityManager')->getRepository('WarehouseProductLocation')->findByIdProduct(this.id);
        foreach ($warehouse_product_locations as $warehouse_product_location) {
            $warehouse_product_location->delete();
        }

        $stocks = Adapter_ServiceLocator::get('Core_Foundation_Database_EntityManager')->getRepository('Stock')->findByIdProduct(this.id);
        foreach ($stocks as $stock) {
            $stock->delete();
        }
    }
        $result = parent::delete();

        // Removes the product from StockAvailable, for the current shop
        StockAvailable::removeProductFromStockAvailable(this.id);
        $result &= (this.deleteProductAttributes() && this.deleteImages() && this.deleteSceneProducts());
        // If there are still entries in product_shop, don't remove completely the product
        if (this.hasMultishopEntries()) {
            return true;
        }

        Hook::exec('actionProductDelete', array('id_product' => (int)this.id, 'product' => $this));
        if (!$result ||
                !GroupReduction::deleteProductReduction(this.id) ||
            !this.deleteCategories(true) ||
                    !this.deleteProductFeatures() ||
                            !this.deleteTags() ||
                                    !this.deleteCartProducts() ||
                                            !this.deleteAttributesImpacts() ||
                                                    !this.deleteAttachments(false) ||
                                                            !this.deleteCustomization() ||
                                                                    !SpecificPrice::deleteByProductId((int)this.id) ||
            !this.deletePack() ||
                    !this.deleteProductSale() ||
                            !this.deleteSearchIndexes() ||
                                    !this.deleteAccessories() ||
                                            !this.deleteFromAccessories() ||
                                                    !this.deleteFromSupplier() ||
                                                            !this.deleteDownload() ||
                                                                    !this.deleteFromCartRules()) {
        return false;
    }

        return true;
    }

    public function deleteSelection($products)
    {
        $return = 1;
        if (is_array($products) && ($count = count($products))) {
            // Deleting products can be quite long on a cheap server. Let's say 1.5 seconds by product (I've seen it!).
            if (intval(ini_get('max_execution_time')) < round($count * 1.5)) {
                ini_set('max_execution_time', round($count * 1.5));
            }

            foreach ($products as $id_product) {
                $product = new Product((int)$id_product);
                $return &= $product->delete();
            }
        }
        return $return;
    }

    public boolean deleteFromCartRules(){
        JeproLabCartRuleModel.cleanAnalyzeRuleIntegrity('products', this.id);
        return true;
    }

    public function deleteFromSupplier()
    {
        return Db::getInstance()->delete('product_supplier', 'id_product = '.(int)this.id);
    }

    /**
     * addToCategories add this product to the category/ies if not exists.
     *
     * @param mixed $categories id_category or array of id_category
     * @return bool true if succeed
     * /
    public function addToCategories($categories = array())
    {
        if (empty($categories)) {
            return false;
        }

        if (!is_array($categories)) {
            $categories = array($categories);
        }

        if (!count($categories)) {
            return false;
        }

        $categories = array_map('intval', $categories);

        $current_categories = this.getCategories();
        $current_categories = array_map('intval', $current_categories);

        // for new categ, put product at last position
        $res_categ_new_pos = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT id_category, MAX(position)+1 newPos
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
        WHERE `id_category` IN('.implode(',', $categories).')
        GROUP BY id_category');
        foreach ($res_categ_new_pos as $array) {
        $new_categories[(int)$array['id_category']] = (int)$array['newPos'];
    }

        $new_categ_pos = array();
        foreach ($categories as $id_category) {
        $new_categ_pos[$id_category] = isset($new_categories[$id_category]) ? $new_categories[$id_category] : 0;
    }

        $product_cats = array();

        foreach ($categories as $new_id_categ) {
        if (!in_array($new_id_categ, $current_categories)) {
            $product_cats[] = array(
                    'id_category' => (int)$new_id_categ,
                    'id_product' => (int)this.id,
                    'position' => (int)$new_categ_pos[$new_id_categ],
            );
        }
    }

        Db::getInstance()->insert('category_product', $product_cats);
        return true;
    }

    /**
     * Update categories to index product into
     *
     * @param string $productCategories Categories list to index product into
     * @param bool $keeping_current_pos (deprecated, no more used)
     * @return array Update/insertion result
     * /
    public function updateCategories($categories, $keeping_current_pos = false)
    {
        if (empty($categories)) {
            return false;
        }

        $result = Db::getInstance()->executeS('
            SELECT c.`id_category`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category` c ON (c.`id_category` = cp.`id_category`)
        '.JeproLabLaboratoryModel.addSqlAssociation('category', 'c', true, null, true).'
        WHERE cp.`id_category` NOT IN ('.implode(',', array_map('intval', $categories)).')
        AND cp.id_product = '.this.id
        );

        // if none are found, it's an error
        if (!is_array($result)) {
            return false;
        }

        foreach ($result as $categ_to_delete) {
        this.deleteCategory($categ_to_delete['id_category']);
    }

        if (!this.addToCategories($categories)) {
            return false;
        }

        SpecificPriceRule::applyAllRules(array((int)this.id));
        return true;
    }

    /**
     * deleteCategory delete this product from the category $id_category
     *
     * @param mixed $id_category
     * @param mixed $clean_positions
     * @return bool
     * /
    public function deleteCategory($id_category, $clean_positions = true)
    {
        $result = Db::getInstance()->executeS(
            'SELECT `id_category`, `position`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
        WHERE `id_product` = '.(int)this.id.'
        AND id_category = '.(int)$id_category.''
        );

        $return = Db::getInstance()->delete('category_product', 'id_product = '.(int)this.id.' AND id_category = '.(int)$id_category);
        if ($clean_positions === true) {
            foreach ($result as $row) {
                this.cleanPositions((int)$row['id_category'], (int)$row['position']);
            }
        }
        SpecificPriceRule::applyAllRules(array((int)this.id));
        return $return;
    }

    /**
     * Delete all association to category where product is indexed
     *
     * @param bool $clean_positions clean category positions after deletion
     * @return array Deletion result
     * /
    public function deleteCategories($clean_positions = false)
    {
        if ($clean_positions === true) {
            $result = Db::getInstance()->executeS(
                    'SELECT `id_category`, `position`
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
            WHERE `id_product` = '.(int)this.id
            );
        }

        $return = Db::getInstance()->delete('category_product', 'id_product = '.(int)this.id);
        if ($clean_positions === true && is_array($result)) {
            foreach ($result as $row) {
                $return &= this.cleanPositions((int)$row['id_category'], (int)$row['position']);
            }
        }

        return $return;
    }

    /**
     * Delete products tags entries
     *
     * @return array Deletion result
     * /
    public function deleteTags()
    {
        return Tag::deleteTagsForProduct((int)this.id);
    }

    /**
     * Delete product from cart
     *
     * @return array Deletion result
     * /
    public function deleteCartProducts()
    {
        return Db::getInstance()->delete('cart_product', 'id_product = '.(int)this.id);
    }

    /**
     * Delete product images from database
     *
     * @return bool success
     * /
    public function deleteImages()
    {
        $result = Db::getInstance()->executeS('
            SELECT `id_image`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'image`
        WHERE `id_product` = '.(int)this.id
        );

        $status = true;
        if ($result) {
            foreach ($result as $row) {
                $image = new Image($row['id_image']);
                $status &= $image->delete();
            }
        }
        return $status;
    }

    /**
     * @deprecated 1.5.0 Use Combination::getPrice()
     * /
    public static function getProductAttributePrice($id_product_attribute)
    {
        return Combination::getPrice($id_product_attribute);
    }

    /**
     * Get all available products
     *
     * @param int $id_lang Language id
     * @param int $start Start number
     * @param int $limit Number of products to return
     * @param string $order_by Field for ordering
     * @param string $order_way Way for ordering (ASC or DESC)
     * @return array Products details
     * /
    public static function getProducts($id_lang, $start, $limit, $order_by, $order_way, $id_category = false,
                                       $only_active = false, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $front = true;
        if (!in_array(context.controller->controller_type, array('front', 'modulefront'))) {
            $front = false;
        }

        if (!Validate::isOrderBy($order_by) || !Validate::isOrderWay($order_way)) {
        die(Tools::displayError());
    }
        if ($order_by == 'id_product' || $order_by == 'price' || $order_by == 'date_add' || $order_by == 'date_upd') {
            $order_by_prefix = 'p';
        } elseif ($order_by == 'name') {
        $order_by_prefix = 'pl';
    } elseif ($order_by == 'position') {
        $order_by_prefix = 'c';
    }

        if (strpos($order_by, '.') > 0) {
            $order_by = explode('.', $order_by);
            $order_by_prefix = $order_by[0];
            $order_by = $order_by[1];
        }
        $sql = 'SELECT p.*, product_shop.*, pl.* , m.`name` AS manufacturer_name, s.`name` AS supplier_name
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (p.`id_product` = pl.`id_product` '.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'manufacturer` m ON (m.`id_manufacturer` = p.`id_manufacturer`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'supplier` s ON (s.`id_supplier` = p.`id_supplier`)'.
        ($id_category ? 'LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` c ON (c.`id_product` = p.`id_product`)' : '').'
        WHERE pl.`id_lang` = '.(int)$id_lang.
        ($id_category ? ' AND c.`id_category` = '.(int)$id_category : '').
        ($front ? ' AND product_shop.`visibility` IN ("both", "catalog")' : '').
        ($only_active ? ' AND product_shop.`active` = 1' : '').'
        ORDER BY '.(isset($order_by_prefix) ? pSQL($order_by_prefix).'.' : '').'`'.pSQL($order_by).'` '.pSQL($order_way).
        ($limit > 0 ? ' LIMIT '.(int)$start.','.(int)$limit : '');
        $rq = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
        if ($order_by == 'price') {
            Tools::orderbyPrice($rq, $order_way);
        }

        foreach ($rq as &$row) {
        $row = JeproLabAnalyzeModel.getTaxesInformations($row);
    }

        return ($rq);
    }

    public static function getSimpleProducts($id_lang, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $front = true;
        if (!in_array(context.controller->controller_type, array('front', 'modulefront'))) {
            $front = false;
        }

        $sql = 'SELECT p.`id_product`, pl.`name`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (p.`id_product` = pl.`id_product` '.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').')
        WHERE pl.`id_lang` = '.(int)$id_lang.'
        '.($front ? ' AND product_shop.`visibility` IN ("both", "catalog")' : '').'
        ORDER BY pl.`name`';
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
    }
*/
    public boolean isNew(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT analyze.analyze_id FROM " + dataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ";
        query += JeproLabLaboratoryModel.addSqlAssociation("product") + " WHERE analyze.analyze_id = " + this.analyze_id ;
        query += " AND DATEDIFF( analyze_lab." + dataBaseObject.quoteName("date_add") + ", DATE_SUB('" + JeproLabTools.date("Y-m-d");
        query += " 00:00:00' , INTERVAL " + (JeproLabSettingModel.getIntValue("number") > 0 ? JeproLabSettingModel.getIntValue("") : 20);
        query += " DAY ) ) > 0 ";

        dataBaseObject.setQuery(query);
        ResultSet analyzes = dataBaseObject.loadObject();
        int total = 0;
        try{
            while (analyzes.next()){
                total += 1;
            }
        }catch (SQLException ignored){

        }
        return total > 0;
    }
/*
    public function productAttributeExists($attributes_list, $current_product_attribute = false, Context context = null, $all_shops = false, $return_id = false)
    {
        if (!Combination::isFeatureActive()) {
        return false;
    }
        if (context === null) {
            context = JeproLabContext.getContext();
        }
        $result = Db::getInstance()->executeS(
            'SELECT pac.`id_attribute`, pac.`id_product_attribute`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop` pas ON (pas.id_product_attribute = pa.id_product_attribute)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        WHERE 1 '.(!$all_shops ? ' AND pas.id_shop ='.(int)context.shop->id : '').' AND pa.`id_product` = '.(int)this.id.
        ($all_shops ? ' GROUP BY pac.id_attribute, pac.id_product_attribute ' : '')
        );

        /* If something's wrong * /
        if (!$result || empty($result)) {
            return false;
        }
        /* Product attributes simulation * /
        $product_attributes = array();
        foreach ($result as $product_attribute) {
        $product_attributes[$product_attribute['id_product_attribute']][] = $product_attribute['id_attribute'];
    }
        /* Checking product's attribute existence * /
        foreach ($product_attributes as $key => $product_attribute) {
        if (count($product_attribute) == count($attributes_list)) {
            $diff = false;
            for ($i = 0; $diff == false && isset($product_attribute[$i]); $i++) {
                if (!in_array($product_attribute[$i], $attributes_list) || $key == $current_product_attribute) {
                    $diff = true;
                }
            }
            if (!$diff) {
                if ($return_id) {
                    return $key;
                }
                return true;
            }
        }
    }

        return false;
    }

    /**
     * addProductAttribute is deprecated
     *
     * The quantity params now set StockAvailable for the current shop with the specified quantity
     * The supplier_reference params now set the supplier reference of the default supplier of the product if possible
     *
     * @see StockManager if you want to manage real stock
     * @see StockAvailable if you want to manage available quantities for sale on your shop(s)
     * @see ProductSupplier for manage supplier reference(s)
     *
     * @deprecated since 1.5.0
     * /
    public function addProductAttribute($price, $weight, $unit_impact, ecoTax, $quantity, $id_images, $reference,
                                        $id_supplier = null, $ean13, $default, $location = null, $upc = null, $minimal_quantity = 1)
    {
        Tools::displayAsDeprecated();

        $id_product_attribute = this.addAttribute(
                $price, $weight, $unit_impact, ecoTax, $id_images,
                $reference, $ean13, $default, $location, $upc, $minimal_quantity
        );

        if (!$id_product_attribute) {
            return false;
        }

        StockAvailable::setQuantity(this.id, $id_product_attribute, $quantity);
        //Try to set the default supplier reference
        this.addSupplierReference($id_supplier, $id_product_attribute);
        return $id_product_attribute;
    }

    public function generateMultipleCombinations($combinations, $attributes)
    {
        $res = true;
        $default_on = 1;
        foreach ($combinations as $key => $combination) {
        $id_combination = (int)this.productAttributeExists($attributes[$key], false, null, true, true);
        $obj = new Combination($id_combination);

        if ($id_combination) {
            $obj->minimal_quantity = 1;
            $obj->available_date = '0000-00-00';
        }

        foreach ($combination as $field => $value) {
            $obj->$field = $value;
        }

        $obj->default_on = $default_on;
        $default_on = 0;
        this.setAvailableDate();

        $obj->save();

        if (!$id_combination) {
            $attribute_list = array();
            foreach ($attributes[$key] as $id_attribute) {
                $attribute_list[] = array(
                        'id_product_attribute' => (int)$obj->id,
                        'id_attribute' => (int)$id_attribute
                );
            }
            $res &= Db::getInstance()->insert('product_attribute_combination', $attribute_list);
        }
    }

        return $res;
    }

    /**
     * @param int $quantity DEPRECATED
     * @param string $supplier_reference DEPRECATED
     * /
    public function addCombinationEntity($wholesale_price, $price, $weight, $unit_impact, ecoTax, $quantity,
                                         $id_images, $reference, $id_supplier, $ean13, $default, $location = null, $upc = null, $minimal_quantity = 1, array $id_shop_list = array(), $available_date = null)
    {
        $id_product_attribute = this.addAttribute(
                $price, $weight, $unit_impact, ecoTax, $id_images,
                $reference, $ean13, $default, $location, $upc, $minimal_quantity, $id_shop_list, $available_date);
        this.addSupplierReference($id_supplier, $id_product_attribute);
        $result = ObjectModel::updateMultishopTable('Combination', array(
            'wholesale_price' => (float)$wholesale_price,
        ), 'a.id_product_attribute = '.(int)$id_product_attribute);

        if (!$id_product_attribute || !$result) {
            return false;
        }

        return $id_product_attribute;
    }

    /**
     * @deprecated 1.5.5.0
     * @param $attributes
     * @param bool $set_default
     * @return array
     * /
    public function addProductAttributeMultiple($attributes, $set_default = true)
    {
        Tools::displayAsDeprecated();
        $return = array();
        $default_value = 1;
        foreach ($attributes as &$attribute) {
        $obj = new Combination();
        foreach ($attribute as $key => $value) {
            $obj->$key = $value;
        }

        if ($set_default) {
            $obj->default_on = $default_value;
            $default_value = 0;
            // if we add a combination for this shop and this product does not use the combination feature in other shop,
            // we clone the default combination in every shop linked to this product
            if (!this.hasAttributesInOtherShops()) {
                $id_shop_list_array = JeproLabAnalyzeModel.getShopsByProduct(this.id);
                $id_shop_list = array();
                foreach ($id_shop_list_array as $array_shop) {
                    $id_shop_list[] = $array_shop['id_shop'];
                }
                $obj->id_shop_list = $id_shop_list;
            }
        }
        $obj->add();
        $return[] = $obj->id;
    }

        return $return;
    }

    /**
     * Del all default attributes for product
     * /
    public function deleteDefaultAttributes()
    {
        return ObjectModel::updateMultishopTable('Combination', array(
            'default_on' => null,
        ), 'a.`id_product` = '.(int)this.id);
    }

    public function setDefaultAttribute($id_product_attribute)
    {
        $result = ObjectModel::updateMultishopTable('Combination', array(
            'default_on' => 1
        ), 'a.`id_product` = '.(int)this.id.' AND a.`id_product_attribute` = '.(int)$id_product_attribute);

        $result &= ObjectModel::updateMultishopTable('product', array(
            'cache_default_attribute' => (int)$id_product_attribute,
        ), 'a.`id_product` = '.(int)this.id);
        this.cache_default_attribute = (int)$id_product_attribute;
        return $result;
    }

    public static function updateDefaultAttribute($id_product)
    {
        $id_default_attribute = (int)JeproLabAnalyzeModel.getDefaultAttribute($id_product, 0, true);

        $result = Db::getInstance()->update('product_shop', array(
                    'cache_default_attribute' => $id_default_attribute,
            ), 'id_product = '.(int)$id_product.JeproLabLaboratoryModel.addSqlRestriction());

        $result &= Db::getInstance()->update('product', array(
                    'cache_default_attribute' => $id_default_attribute,
            ), 'id_product = '.(int)$id_product);

        if ($result && $id_default_attribute) {
            return $id_default_attribute;
        } else {
            return $result;
        }
    }

    /**
     * Update a product attribute
     *
     * @deprecated since 1.5
     * @see updateAttribute() to use instead
     * @see ProductSupplier for manage supplier reference(s)
     *
     * /
    public function updateProductAttribute($id_product_attribute, $wholesale_price, $price, $weight, $unit, ecoTax,
                                           $id_images, $reference, $id_supplier = null, $ean13, $default, $location = null, $upc = null, $minimal_quantity, $available_date)
    {
        Tools::displayAsDeprecated();

        $return = this.updateAttribute(
                $id_product_attribute, $wholesale_price, $price, $weight, $unit, ecoTax,
                $id_images, $reference, $ean13, $default, $location = null, $upc = null, $minimal_quantity, $available_date
        );
        this.addSupplierReference($id_supplier, $id_product_attribute);

        return $return;
    }

    /**
     * Sets or updates Supplier Reference
     *
     * @param int $id_supplier
     * @param int $id_product_attribute
     * @param string $supplier_reference
     * @param float $price
     * @param int $id_currency
     * /
    public function addSupplierReference($id_supplier, $id_product_attribute, $supplier_reference = null, $price = null, $id_currency = null)
    {
        //in some case we need to add price without supplier reference
        if ($supplier_reference === null) {
            $supplier_reference = '';
        }

        //Try to set the default supplier reference
        if (($id_supplier > 0) && (this.id > 0)) {
            $id_product_supplier = (int)ProductSupplier::getIdByProductAndSupplier(this.id, $id_product_attribute, $id_supplier);

            $product_supplier = new ProductSupplier($id_product_supplier);

            if (!$id_product_supplier) {
                $product_supplier->id_product = (int)this.id;
                $product_supplier->id_product_attribute = (int)$id_product_attribute;
                $product_supplier->id_supplier = (int)$id_supplier;
            }

            $product_supplier->product_supplier_reference = pSQL($supplier_reference);
            $product_supplier->product_supplier_price_te = !is_null($price) ? (float)$price : (float)$product_supplier->product_supplier_price_te;
            $product_supplier->id_currency = !is_null($id_currency) ? (int)$id_currency : (int)$product_supplier->id_currency;
            $product_supplier->save();
        }
    }

    /**
     * Update a product attribute
     *
     * @param int $id_product_attribute Product attribute id
     * @param float $wholesale_price Wholesale price
     * @param float $price Additional price
     * @param float $weight Additional weight
     * @param float $unit
     * @param float ecoTax Additional ecotax
     * @param int $id_image Image id
     * @param string $reference Reference
     * @param string $ean13 Ean-13 barcode
     * @param int $default Default On
     * @param string $upc Upc barcode
     * @param string $minimal_quantity Minimal quantity
     * @return array Update result
     * /
    public function updateAttribute($id_product_attribute, $wholesale_price, $price, $weight, $unit, ecoTax,
                                    $id_images, $reference, $ean13, $default, $location = null, $upc = null, $minimal_quantity = null, $available_date = null, $update_all_fields = true, array $id_shop_list = array())
    {
        $combination = new Combination($id_product_attribute);

        if (!$update_all_fields) {
            $combination->setFieldsToUpdate(array(
                            'price' => !is_null($price),
                    'wholesale_price' => !is_null($wholesale_price),
                    'ecotax' => !is_null(ecoTax),
                    'weight' => !is_null($weight),
                    'unit_price_impact' => !is_null($unit),
                    'default_on' => !is_null($default),
                    'minimal_quantity' => !is_null($minimal_quantity),
                    'available_date' => !is_null($available_date),
            ));
        }

        $price = str_replace(',', '.', $price);
        $weight = str_replace(',', '.', $weight);

        $combination->price = (float)$price;
        $combination->wholesale_price = (float)$wholesale_price;
        $combination->ecotax = (float)ecoTax;
        $combination->weight = (float)$weight;
        $combination->unit_price_impact = (float)$unit;
        $combination->reference = pSQL($reference);
        $combination->location = pSQL($location);
        $combination->ean13 = pSQL($ean13);
        $combination->upc = pSQL($upc);
        $combination->default_on = (int)$default;
        $combination->minimal_quantity = (int)$minimal_quantity;
        $combination->available_date = $available_date ? pSQL($available_date) : '0000-00-00';

        if (count($id_shop_list)) {
            $combination->id_shop_list = $id_shop_list;
        }

        $combination->save();

        if (is_array($id_images) && count($id_images)) {
            $combination->setImages($id_images);
        }

        $id_default_attribute = (int)JeproLabAnalyzeModel.updateDefaultAttribute(this.id);
        if ($id_default_attribute) {
            this.cache_default_attribute = $id_default_attribute;
        }

        // Sync stock Reference, EAN13 and UPC for this attribute
        if (JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT') && StockAvailable::dependsOnStock(this.id, JeproLabContext.getContext()->shop->id)) {
        Db::getInstance()->update('stock', array(
                        'reference' => pSQL($reference),
                'ean13'     => pSQL($ean13),
                'upc'        => pSQL($upc),
        ), 'id_product = '.this.id.' AND id_product_attribute = '.(int)$id_product_attribute);
    }

        Hook::exec('actionProductAttributeUpdate', array('id_product_attribute' => (int)$id_product_attribute));
        Tools::clearColorListCache(this.id);

        return true;
    }

    /**
     * Add a product attribute
     * @since 1.5.0.1
     *
     * @param float $price Additional price
     * @param float $weight Additional weight
     * @param float ecoTax Additional ecotax
     * @param int $id_images Image ids
     * @param string $reference Reference
     * @param string $location Location
     * @param string $ean13 Ean-13 barcode
     * @param bool $default Is default attribute for product
     * @param int $minimal_quantity Minimal quantity to add to cart
     * @return mixed $id_product_attribute or false
     * /
    public function addAttribute($price, $weight, $unit_impact, ecoTax, $id_images, $reference, $ean13,
                                 $default, $location = null, $upc = null, $minimal_quantity = 1, array $id_shop_list = array(), $available_date = null)
    {
        if (!this.id) {
            return;
        }

        $price = str_replace(',', '.', $price);
        $weight = str_replace(',', '.', $weight);

        $combination = new Combination();
        $combination->id_product = (int)this.id;
        $combination->price = (float)$price;
        $combination->ecotax = (float)ecoTax;
        $combination->quantity = 0;
        $combination->weight = (float)$weight;
        $combination->unit_price_impact = (float)$unit_impact;
        $combination->reference = pSQL($reference);
        $combination->location = pSQL($location);
        $combination->ean13 = pSQL($ean13);
        $combination->upc = pSQL($upc);
        $combination->default_on = (int)$default;
        $combination->minimal_quantity = (int)$minimal_quantity;
        $combination->available_date = $available_date;

        if (count($id_shop_list)) {
            $combination->id_shop_list = array_unique($id_shop_list);
        }

        $combination->add();

        if (!$combination->id) {
            return false;
        }

        $total_quantity = (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT SUM(quantity) as quantity
            FROM '.staticDataBaseObject.quoteName("#__jeprolab.'stock_available
        WHERE id_product = '.(int)this.id.'
        AND id_product_attribute <> 0 '
        );

        if (!$total_quantity) {
            Db::getInstance()->update('stock_available', array('quantity' => 0), '`id_product` = '.this.id);
        }

        $id_default_attribute = JeproLabAnalyzeModel.updateDefaultAttribute(this.id);

        if ($id_default_attribute) {
            this.cache_default_attribute = $id_default_attribute;
            if (!$combination->available_date) {
                this.setAvailableDate();
            }
        }

        if (!empty($id_images)) {
            $combination->setImages($id_images);
        }

        Tools::clearColorListCache(this.id);

        if (JeproLabSettingModel.get('PS_DEFAULT_WAREHOUSE_NEW_PRODUCT') != 0 && JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT')) {
        $warehouse_location_entity = new WarehouseProductLocation();
        $warehouse_location_entity->id_product = this.id;
        $warehouse_location_entity->id_product_attribute = (int)$combination->id;
        $warehouse_location_entity->id_warehouse = JeproLabSettingModel.get('PS_DEFAULT_WAREHOUSE_NEW_PRODUCT');
        $warehouse_location_entity->location = pSQL('');
        $warehouse_location_entity->save();
    }

        return (int)$combination->id;
    }


    /**
     * @deprecated since 1.5.0
     * /
    public function updateQuantityProductWithAttributeQuantity()
    {
        Tools::displayAsDeprecated();

        return Db::getInstance()->execute('
            UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'product`
        SET `quantity` = IFNULL(
            (
                    SELECT SUM(`quantity`)
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute`
        WHERE `id_product` = '.(int)this.id.'
        ), \'0\')
        WHERE `id_product` = '.(int)this.id);
    }
    /**
     * Delete product attributes
     *
     * @return array Deletion result
     * /
    public function deleteProductAttributes()
    {
        Hook::exec('actionProductAttributeDelete', array('id_product_attribute' => 0, 'id_product' => (int)this.id, 'deleteAllAttributes' => true));

        $result = true;
        $combinations = new PrestaShopCollection('Combination');
        $combinations->where('id_product', '=', this.id);
        foreach ($combinations as $combination) {
        $result &= $combination->delete();
    }
        SpecificPriceRule::applyAllRules(array((int)this.id));
        Tools::clearColorListCache(this.id);
        return $result;
    }

    /**
     * Delete product attributes impacts
     *
     * @return bool
     * /
    public function deleteAttributesImpacts()
    {
        return Db::getInstance()->execute(
            'DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_impact`
        WHERE `id_product` = '.(int)this.id
        );
    }

    /**
     * Delete product features
     *
     * @return array Deletion result
     * /
    public function deleteProductFeatures()
    {
        SpecificPriceRule::applyAllRules(array((int)this.id));
        return this.deleteFeatures();
    }


    public static function updateCacheAttachment($id_product)
    {
        $value = (bool)Db::getInstance()->getValue('
            SELECT id_attachment
            FROM '.staticDataBaseObject.quoteName("#__jeprolab.'product_attachment
        WHERE id_product='.(int)$id_product);
        return Db::getInstance()->update(
            'product',
            array('cache_has_attachments' => (int)$value),
            'id_product = '.(int)$id_product
        );
    }

    /**
     * Delete product attachments
     *
     * @param bool $update_cache If set to true attachment cache will be updated
     * @return array Deletion result
     * /
    public function deleteAttachments($update_attachment_cache = true)
    {
        $res = Db::getInstance()->execute('
            DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attachment`
        WHERE `id_product` = '.(int)this.id
        );

        if (isset($update_attachment_cache) && (bool)$update_attachment_cache === true) {
            JeproLabAnalyzeModel.updateCacheAttachment((int)this.id);
        }

        return $res;
    }

    /**
     * Delete product customizations
     *
     * @return array Deletion result
     * /
    public function deleteCustomization()
    {
        return (
                Db::getInstance()->execute(
            'DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        WHERE `id_product` = '.(int)this.id
        )
        &&
        Db::getInstance()->execute(
            'DELETE `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang` LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        ON ('.staticDataBaseObject.quoteName("#__jeprolab.'customization_field.id_customization_field = '.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang.id_customization_field)
        WHERE '.staticDataBaseObject.quoteName("#__jeprolab.'customization_field.id_customization_field IS NULL'
        )
        );
    }

    /**
     * Delete product pack details
     *
     * @return array Deletion result
     * /
    public function deletePack()
    {
        return Db::getInstance()->execute(
            'DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'pack`
        WHERE `id_product_pack` = '.(int)this.id.'
        OR `id_product_item` = '.(int)this.id
        );
    }

    /**
     * Delete product sales
     *
     * @return array Deletion result
     * /
    public function deleteProductSale()
    {
        return Db::getInstance()->execute(
            'DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_sale`
        WHERE `id_product` = '.(int)this.id
        );
    }

    /**
     * Delete product in its scenes
     *
     * @return array Deletion result
     * /
    public function deleteSceneProducts()
    {
        return Db::getInstance()->execute(
            'DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'scene_products`
        WHERE `id_product` = '.(int)this.id
        );
    }

    /**
     * Delete product indexed words
     *
     * @return array Deletion result
     * /
    public function deleteSearchIndexes()
    {
        return (
                Db::getInstance()->execute(
            'DELETE `'.staticDataBaseObject.quoteName("#__jeprolab.'search_index`, `'.staticDataBaseObject.quoteName("#__jeprolab.'search_word`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'search_index` JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'search_word`
        WHERE `'.staticDataBaseObject.quoteName("#__jeprolab.'search_index`.`id_product` = '.(int)this.id.'
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'search_word`.`id_word` = `'.staticDataBaseObject.quoteName("#__jeprolab.'search_index`.id_word'
        )
        );
    }

    /**
     * Add a product attributes combinaison
     *
     * @param int $id_product_attribute Product attribute id
     * @param array $attributes Attributes to forge combinaison
     * @return array Insertion result
     * @deprecated since 1.5.0.7
     * /
    public function addAttributeCombinaison($id_product_attribute, $attributes)
    {
        Tools::displayAsDeprecated();
        if (!is_array($attributes)) {
            die(Tools::displayError());
        }
        if (!count($attributes)) {
            return false;
        }

        $combination = new Combination((int)$id_product_attribute);
        return $combination->setAttributes($attributes);
    }

    /**
     * @deprecated 1.5.5.0
     * @param $id_attributes
     * @param $combinations
     * @return bool
     * @throws PrestaShopDatabaseException
     * /
    public function addAttributeCombinationMultiple($id_attributes, $combinations)
    {
        Tools::displayAsDeprecated();
        $attributes_list = array();
        foreach ($id_attributes as $nb => $id_product_attribute) {
        if (isset($combinations[$nb])) {
            foreach ($combinations[$nb] as $id_attribute) {
                $attributes_list[] = array(
                        'id_product_attribute' => (int)$id_product_attribute,
                        'id_attribute' => (int)$id_attribute,
                );
            }
        }
    }

        return Db::getInstance()->insert('product_attribute_combination', $attributes_list);
    }


    /**
     * Delete a product attributes combination
     *
     * @param int $id_product_attribute Product attribute id
     * @return array Deletion result
     * /
    public function deleteAttributeCombination($id_product_attribute)
    {
        if (!this.id || !$id_product_attribute || !is_numeric($id_product_attribute)) {
            return false;
        }

        Hook::exec(
            'deleteProductAttribute',
            array(
                    'id_product_attribute' => $id_product_attribute,
            'id_product' => this.id,
            'deleteAllAttributes' => false
        )
        );

        $combination = new Combination($id_product_attribute);
        $res = $combination->delete();
        SpecificPriceRule::applyAllRules(array((int)this.id));
        return $res;
    }

    /**
     * Delete features
     *
     * /
    public function deleteFeatures()
    {
        // List products features
        $features = Db::getInstance()->executeS('
            SELECT p.*, f.*
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product` as p
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value` as f ON (f.`id_feature_value` = p.`id_feature_value`)
        WHERE `id_product` = '.(int)this.id);
        foreach ($features as $tab) {
        // Delete product custom features
        if ($tab['custom']) {
            Db::getInstance()->execute('
                    DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value`
            WHERE `id_feature_value` = '.(int)$tab['id_feature_value']);
            Db::getInstance()->execute('
                    DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value_lang`
            WHERE `id_feature_value` = '.(int)$tab['id_feature_value']);
        }
    }
        // Delete product features
        $result = Db::getInstance()->execute('
            DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product`
        WHERE `id_product` = '.(int)this.id);

        SpecificPriceRule::applyAllRules(array((int)this.id));
        return ($result);
    }

    /**
     * Get all available product attributes resume
     *
     * @param int $id_lang Language id
     * @return array Product attributes combinations
     * /
    public function getAttributesResume($id_lang, $attribute_value_separator = ' - ', $attribute_separator = ', ')
    {
        if (!Combination::isFeatureActive()) {
        return array();
    }

        $combinations = Db::getInstance()->executeS('SELECT pa.*, product_attribute_shop.*
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)this.id.'
        GROUP BY pa.`id_product_attribute`');

        if (!$combinations) {
            return false;
        }

        $product_attributes = array();
        foreach ($combinations as $combination) {
        $product_attributes[] = (int)$combination['id_product_attribute'];
    }

        $lang = Db::getInstance()->executeS('SELECT pac.id_product_attribute, GROUP_CONCAT(agl.`name`, \''.pSQL($attribute_value_separator).'\',al.`name` ORDER BY agl.`id_attribute_group` SEPARATOR \''.pSQL($attribute_separator).'\') as attribute_designation
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a ON a.`id_attribute` = pac.`id_attribute`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group` ag ON ag.`id_attribute_group` = a.`id_attribute_group`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl ON (ag.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)$id_lang.')
        WHERE pac.id_product_attribute IN ('.implode(',', $product_attributes).')
        GROUP BY pac.id_product_attribute');

        foreach ($lang as $k => $row) {
        $combinations[$k]['attribute_designation'] = $row['attribute_designation'];
    }

        //Get quantity of each variations
        foreach ($combinations as $key => $row) {
        $cache_key = $row['id_product'].'_'.$row['id_product_attribute'].'_quantity';

        if (!Cache::isStored($cache_key)) {
            $result = StockAvailable::getQuantityAvailableByProduct($row['id_product'], $row['id_product_attribute']);
            Cache::store(
                    $cache_key,
                    $result
            );
            $combinations[$key]['quantity'] = $result;
        } else {
            $combinations[$key]['quantity'] = Cache::retrieve($cache_key);
        }
    }

        return $combinations;
    }

    /**
     * Get all available product attributes combinations
     *
     * @param int $id_lang Language id
     * @return array Product attributes combinations
     * /
    public function getAttributeCombinations($id_lang)
    {
        if (!Combination::isFeatureActive()) {
        return array();
    }

        $sql = 'SELECT pa.*, product_attribute_shop.*, ag.`id_attribute_group`, ag.`is_color_group`, agl.`name` AS group_name, al.`name` AS attribute_name,
        a.`id_attribute`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac ON pac.`id_product_attribute` = pa.`id_product_attribute`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a ON a.`id_attribute` = pac.`id_attribute`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group` ag ON ag.`id_attribute_group` = a.`id_attribute_group`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl ON (ag.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)$id_lang.')
        WHERE pa.`id_product` = '.(int)this.id.'
        GROUP BY pa.`id_product_attribute`, ag.`id_attribute_group`
        ORDER BY pa.`id_product_attribute`';

        $res = Db::getInstance()->executeS($sql);

        //Get quantity of each variations
        foreach ($res as $key => $row) {
        $cache_key = $row['id_product'].'_'.$row['id_product_attribute'].'_quantity';

        if (!Cache::isStored($cache_key)) {
            Cache::store(
                    $cache_key,
                    StockAvailable::getQuantityAvailableByProduct($row['id_product'], $row['id_product_attribute'])
            );
        }

        $res[$key]['quantity'] = Cache::retrieve($cache_key);
    }

        return $res;
    }

    /**
     * Get product attribute combination by id_product_attribute
     *
     * @param int $id_product_attribute
     * @param int $id_lang Language id
     * @return array Product attribute combination by id_product_attribute
     * /
    public function getAttributeCombinationsById($id_product_attribute, $id_lang)
    {
        if (!Combination::isFeatureActive()) {
        return array();
    }
        $sql = 'SELECT pa.*, product_attribute_shop.*, ag.`id_attribute_group`, ag.`is_color_group`, agl.`name` AS group_name, al.`name` AS attribute_name,
        a.`id_attribute`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac ON pac.`id_product_attribute` = pa.`id_product_attribute`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a ON a.`id_attribute` = pac.`id_attribute`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group` ag ON ag.`id_attribute_group` = a.`id_attribute_group`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl ON (ag.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)$id_lang.')
        WHERE pa.`id_product` = '.(int)this.id.'
        AND pa.`id_product_attribute` = '.(int)$id_product_attribute.'
        GROUP BY pa.`id_product_attribute`, ag.`id_attribute_group`
        ORDER BY pa.`id_product_attribute`';

        $res = Db::getInstance()->executeS($sql);

        //Get quantity of each variations
        foreach ($res as $key => $row) {
        $cache_key = $row['id_product'].'_'.$row['id_product_attribute'].'_quantity';

        if (!Cache::isStored($cache_key)) {
            $result = StockAvailable::getQuantityAvailableByProduct($row['id_product'], $row['id_product_attribute']);
            Cache::store(
                    $cache_key,
                    $result
            );
            $res[$key]['quantity'] = $result;
        } else {
            $res[$key]['quantity'] = Cache::retrieve($cache_key);
        }
    }

        return $res;
    }

    public function getCombinationImages($id_lang)
    {
        if (!Combination::isFeatureActive()) {
        return false;
    }

        $product_attributes = Db::getInstance()->executeS(
            'SELECT `id_product_attribute`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute`
        WHERE `id_product` = '.(int)this.id
        );

        if (!$product_attributes) {
            return false;
        }

        $ids = array();

        foreach ($product_attributes as $product_attribute) {
        $ids[] = (int)$product_attribute['id_product_attribute'];
    }

        $result = Db::getInstance()->executeS('
            SELECT pai.`id_image`, pai.`id_product_attribute`, il.`legend`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_image` pai
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_lang` il ON (il.`id_image` = pai.`id_image`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image` i ON (i.`id_image` = pai.`id_image`)
        WHERE pai.`id_product_attribute` IN ('.implode(', ', $ids).') AND il.`id_lang` = '.(int)$id_lang.' ORDER by i.`position`'
        );

        if (!$result) {
            return false;
        }

        $images = array();

        foreach ($result as $row) {
        $images[$row['id_product_attribute']][] = $row;
    }

        return $images;
    }

    public static function getCombinationImageById($id_product_attribute, $id_lang)
    {
        if (!Combination::isFeatureActive() || !$id_product_attribute) {
        return false;
    }

        $result = Db::getInstance()->executeS('
            SELECT pai.`id_image`, pai.`id_product_attribute`, il.`legend`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_image` pai
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_lang` il ON (il.`id_image` = pai.`id_image`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image` i ON (i.`id_image` = pai.`id_image`)
        WHERE pai.`id_product_attribute` = '.(int)$id_product_attribute.' AND il.`id_lang` = '.(int)$id_lang.' ORDER by i.`position` LIMIT 1'
        );

        if (!$result) {
            return false;
        }

        return $result[0];
    }

    /**
     * Check if product has attributes combinations
     *
     * @return int Attributes combinations number
     */
    public int hasAttributes(){
        if (!JeproLabCombinationModel.isFeaturePublished()) {
            return 0;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT COUNT(*) AS attributes FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute ";
        query += JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute") + " WHERE analyze_attribute." + dataBaseObject.quoteName("analyze_id");
        query += " = " + this.analyze_id;

        dataBaseObject.setQuery(query);
        return (int)dataBaseObject.loadValue("attributes");
    }

    /*
     * Get new products
     *
     * @param langId Language id
     * @param pageNumber Start from (optional)
     * @param nbProducts Number of products to return (optional)
     * @return array New products
     * /
    public static function getNewProducts($id_lang, $page_number = 0, $nb_products = 10, $count = false, $order_by = null, $order_way = null, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $front = true;
        if (!in_array(context.controller->controller_type, array('front', 'modulefront'))) {
            $front = false;
        }

        if ($page_number < 0) {
            $page_number = 0;
        }
        if ($nb_products < 1) {
            $nb_products = 10;
        }
        if (empty($order_by) || $order_by == 'position') {
            $order_by = 'date_add';
        }
        if (empty($order_way)) {
            $order_way = 'DESC';
        }
        if ($order_by == 'id_product' || $order_by == 'price' || $order_by == 'date_add' || $order_by == 'date_upd') {
            $order_by_prefix = 'product_shop';
        } elseif ($order_by == 'name') {
        $order_by_prefix = 'pl';
    }
        if (!Validate::isOrderBy($order_by) || !Validate::isOrderWay($order_way)) {
        die(Tools::displayError());
    }

        $sql_groups = '';
        if (Group::isFeatureActive()) {
        $groups = FrontController::getCurrentCustomerGroups();
        $sql_groups = ' AND EXISTS(SELECT 1 FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_group` cg ON (cp.id_category = cg.id_category AND cg.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '= 1').')
        WHERE cp.`id_product` = p.`id_product`)';
    }

        if (strpos($order_by, '.') > 0) {
            $order_by = explode('.', $order_by);
            $order_by_prefix = $order_by[0];
            $order_by = $order_by[1];
        }

        if ($count) {
            $sql = 'SELECT COUNT(p.`id_product`) AS nb
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
            '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
            WHERE product_shop.`active` = 1
            AND product_shop.`date_add` > "'.date('Y-m-d', strtotime("_".(JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') ? (int)JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') : 20).' DAY')).'"
            '.($front ? ' AND product_shop.`visibility` IN ("both", "catalog")' : '').'
            '.$sql_groups;
            return (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql);
        }

        $sql = new DbQuery();
        $sql->select(
                'p.*, product_shop.*, stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity, pl.`description`, pl.`description_short`, pl.`link_rewrite`, pl.`meta_description`,
                pl.`meta_keywords`, pl.`meta_title`, pl.`name`, pl.`available_now`, pl.`available_later`, image_shop.`id_image` id_image, il.`legend`, m.`name` AS manufacturer_name,
                product_shop.`date_add` > "'.date('Y-m-d', strtotime("_".(JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') ? (int)JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') : 20).' DAY')).'" as new'
        );

        $sql->from('product', 'p');
        $sql->join(JeproLabLaboratoryModel.addSqlAssociation('product', 'p'));
        $sql->leftJoin('product_lang', 'pl', '
                p.`id_product` = pl.`id_product`
        AND pl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl')
        );
        $sql->leftJoin('image_shop', 'image_shop', 'image_shop.`id_product` = p.`id_product` AND image_shop.cover=1 AND image_shop.id_shop='.(int)context.shop->id);
        $sql->leftJoin('image_lang', 'il', 'image_shop.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang);
        $sql->leftJoin('manufacturer', 'm', 'm.`id_manufacturer` = p.`id_manufacturer`');

        $sql->where('product_shop.`active` = 1');
        if ($front) {
            $sql->where('product_shop.`visibility` IN ("both", "catalog")');
        }
        $sql->where('product_shop.`date_add` > "'.date('Y-m-d', strtotime("_".(JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') ? (int)JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') : 20).' DAY')).'"');
        if (Group::isFeatureActive()) {
        $groups = FrontController::getCurrentCustomerGroups();
        $sql->where('EXISTS(SELECT 1 FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_group` cg ON (cp.id_category = cg.id_category AND cg.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '= 1').')
        WHERE cp.`id_product` = p.`id_product`)');
    }

        $sql->orderBy((isset($order_by_prefix) ? pSQL($order_by_prefix).'.' : '').'`'.pSQL($order_by).'` '.pSQL($order_way));
        $sql->limit($nb_products, $page_number * $nb_products);

        if (Combination::isFeatureActive()) {
        $sql->select('product_attribute_shop.minimal_quantity AS product_attribute_minimal_quantity, IFNULL(product_attribute_shop.id_product_attribute,0) id_product_attribute');
        $sql->leftJoin('product_attribute_shop', 'product_attribute_shop', 'p.`id_product` = product_attribute_shop.`id_product` AND product_attribute_shop.`default_on` = 1 AND product_attribute_shop.id_shop='.(int)context.shop->id);
    }
        $sql->join(JeproLabAnalyzeModel.sqlStock('p', 0));

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);

        if (!$result) {
            return false;
        }

        if ($order_by == 'price') {
            Tools::orderbyPrice($result, $order_way);
        }

        $products_ids = array();
        foreach ($result as $row) {
        $products_ids[] = $row['id_product'];
    }
        // Thus you can avoid one query per product, because there will be only one query for all the products of the cart
        JeproLabAnalyzeModel.cacheFrontFeatures($products_ids, $id_lang);
        return JeproLabAnalyzeModel.getProductsProperties((int)$id_lang, $result);
    }

    protected static function _getProductIdByDate($beginning, $ending, Context context = null, $with_combination = false)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $id_address = context.cart->{JeproLabSettingModel.get('PS_TAX_ADDRESS_TYPE')};
        $ids = Address::getCountryAndState($id_address);
        $id_country = $ids['id_country'] ? (int)$ids['id_country'] : (int)JeproLabSettingModel.get('PS_COUNTRY_DEFAULT');

        return SpecificPrice::getProductIdByDate(
            context.shop->id,
            context.currency->id,
            $id_country,
            context.customer->id_default_group,
            $beginning,
            $ending,
            0,
            $with_combination
        );
    }

    /**
     * Get a random special
     *
     * @param int $id_lang Language id
     * @return array Special
     * /
    public static function getRandomSpecial($id_lang, $beginning = false, $ending = false, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $front = true;
        if (!in_array(context.controller->controller_type, array('front', 'modulefront'))) {
            $front = false;
        }

        $current_date = date('Y-m-d H:i:00');
        $product_reductions = JeproLabAnalyzeModel._getProductIdByDate((!$beginning ? $current_date : $beginning), (!$ending ? $current_date : $ending), context, true);

        if ($product_reductions) {
            $ids_products = '';
            foreach ($product_reductions as $product_reduction) {
                $ids_products .= '('.(int)$product_reduction['id_product'].','.($product_reduction['id_product_attribute'] ? (int)$product_reduction['id_product_attribute'] :'0').'),';
            }

            $ids_products = rtrim($ids_products, ',');
            Db::getInstance()->execute('CREATE TEMPORARY TABLE `'.staticDataBaseObject.quoteName("#__jeprolab.'product_reductions` (id_product INT UNSIGNED NOT NULL DEFAULT 0, id_product_attribute INT UNSIGNED NOT NULL DEFAULT 0) ENGINE=MEMORY', false);
            if ($ids_products) {
                Db::getInstance()->execute('INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'product_reductions` VALUES '.$ids_products, false);
            }

            $groups = FrontController::getCurrentCustomerGroups();
            $sql_groups = ' AND EXISTS(SELECT 1 FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
            JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_group` cg ON (cp.id_category = cg.id_category AND cg.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '= 1').')
            WHERE cp.`id_product` = p.`id_product`)';

            // Please keep 2 distinct queries because RAND() is an awful way to achieve this result
            $sql = 'SELECT product_shop.id_product, IFNULL(product_attribute_shop.id_product_attribute,0) id_product_attribute
            FROM
            `'.staticDataBaseObject.quoteName("#__jeprolab.'product_reductions` pr,
            `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
            '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
            LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop` product_attribute_shop
            ON (p.`id_product` = product_attribute_shop.`id_product` AND product_attribute_shop.`default_on` = 1 AND product_attribute_shop.id_shop='.(int)context.shop->id.')
            WHERE p.id_product=pr.id_product AND (pr.id_product_attribute = 0 OR product_attribute_shop.id_product_attribute = pr.id_product_attribute) AND product_shop.`active` = 1
            '.$sql_groups.'
            '.($front ? ' AND product_shop.`visibility` IN ("both", "catalog")' : '').'
            ORDER BY RAND()';

            $result = Db::getInstance()->getRow($sql);

            Db::getInstance()->execute('DROP TEMPORARY TABLE `'.staticDataBaseObject.quoteName("#__jeprolab.'product_reductions`', false);

            if (!$id_product = $result['id_product']) {
                return false;
            }

            // no group by needed : there's only one attribute with cover=1 for a given id_product + shop
            $sql = 'SELECT p.*, product_shop.*, stock.`out_of_stock` out_of_stock, pl.`description`, pl.`description_short`,
            pl.`link_rewrite`, pl.`meta_description`, pl.`meta_keywords`, pl.`meta_title`, pl.`name`, pl.`available_now`, pl.`available_later`,
            p.`ean13`, p.`upc`, image_shop.`id_image` id_image, il.`legend`,
            DATEDIFF(product_shop.`date_add`, DATE_SUB("'.date('Y-m-d').' 00:00:00",
                    INTERVAL '.(Validate::isUnsignedInt(JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT')) ? JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') : 20).'
            DAY)) > 0 AS new
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
            LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (
                    p.`id_product` = pl.`id_product`
            AND pl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').'
            )
            '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
            LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_shop` image_shop
            ON (image_shop.`id_product` = p.`id_product` AND image_shop.cover=1 AND image_shop.id_shop='.(int)context.shop->id.')
            LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_lang` il ON (image_shop.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang.')
            '.JeproLabAnalyzeModel.sqlStock('p', 0).'
            WHERE p.id_product = '.(int)$id_product;

            $row = Db::getInstance(_PS_USE_SQL_SLAVE_)->getRow($sql);
            if (!$row) {
                return false;
            }

            $row['id_product_attribute'] = (int)$result['id_product_attribute'];
            return JeproLabAnalyzeModel.getProductProperties($id_lang, $row);
        } else {
            return false;
        }
    }

    /**
     * Get prices drop
     *
     * @param int $id_lang Language id
     * @param int $pageNumber Start from (optional)
     * @param int $nbProducts Number of products to return (optional)
     * @param bool $count Only in order to get total number (optional)
     * @return array Prices drop
     * /
    public static function getPricesDrop($id_lang, $page_number = 0, $nb_products = 10, $count = false,
                                         $order_by = null, $order_way = null, $beginning = false, $ending = false, Context context = null)
    {
        if (!Validate::isBool($count)) {
        die(Tools::displayError());
    }

        if (!context) {
            context = JeproLabContext.getContext();
        }
        if ($page_number < 0) {
            $page_number = 0;
        }
        if ($nb_products < 1) {
            $nb_products = 10;
        }
        if (empty($order_by) || $order_by == 'position') {
            $order_by = 'price';
        }
        if (empty($order_way)) {
            $order_way = 'DESC';
        }
        if ($order_by == 'id_product' || $order_by == 'price' || $order_by == 'date_add' || $order_by == 'date_upd') {
            $order_by_prefix = 'product_shop';
        } elseif ($order_by == 'name') {
        $order_by_prefix = 'pl';
    }
        if (!Validate::isOrderBy($order_by) || !Validate::isOrderWay($order_way)) {
        die(Tools::displayError());
    }
        $current_date = date('Y-m-d H:i:00');
        $ids_product = JeproLabAnalyzeModel._getProductIdByDate((!$beginning ? $current_date : $beginning), (!$ending ? $current_date : $ending), context);

        $tab_id_product = array();
        foreach ($ids_product as $product) {
        if (is_array($product)) {
            $tab_id_product[] = (int)$product['id_product'];
        } else {
            $tab_id_product[] = (int)$product;
        }
    }

        $front = true;
        if (!in_array(context.controller->controller_type, array('front', 'modulefront'))) {
            $front = false;
        }

        $sql_groups = '';
        if (Group::isFeatureActive()) {
        $groups = FrontController::getCurrentCustomerGroups();
        $sql_groups = ' AND EXISTS(SELECT 1 FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_group` cg ON (cp.id_category = cg.id_category AND cg.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '= 1').')
        WHERE cp.`id_product` = p.`id_product`)';
    }

        if ($count) {
            return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
                    SELECT COUNT(DISTINCT p.`id_product`)
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
            '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
            WHERE product_shop.`active` = 1
            AND product_shop.`show_price` = 1
            '.($front ? ' AND product_shop.`visibility` IN ("both", "catalog")' : '').'
            '.((!$beginning && !$ending) ? 'AND p.`id_product` IN('.((is_array($tab_id_product) && count($tab_id_product)) ? implode(', ', $tab_id_product) : 0).')' : '').'
            '.$sql_groups);
        }

        if (strpos($order_by, '.') > 0) {
            $order_by = explode('.', $order_by);
            $order_by = pSQL($order_by[0]).'.`'.pSQL($order_by[1]).'`';
        }

        $sql = '
        SELECT
        p.*, product_shop.*, stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity, pl.`description`, pl.`description_short`, pl.`available_now`, pl.`available_later`,
        IFNULL(product_attribute_shop.id_product_attribute, 0) id_product_attribute,
            pl.`link_rewrite`, pl.`meta_description`, pl.`meta_keywords`, pl.`meta_title`,
        pl.`name`, image_shop.`id_image` id_image, il.`legend`, m.`name` AS manufacturer_name,
        DATEDIFF(
                p.`date_add`,
                DATE_SUB(
                        "'.date('Y-m-d').' 00:00:00",
                        INTERVAL '.(Validate::isUnsignedInt(JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT')) ? JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') : 20).' DAY
        )
        ) > 0 AS new
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop` product_attribute_shop
        ON (p.`id_product` = product_attribute_shop.`id_product` AND product_attribute_shop.`default_on` = 1 AND product_attribute_shop.id_shop='.(int)context.shop->id.')
        '.JeproLabAnalyzeModel.sqlStock('p', 0, false, context.shop).'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (
            p.`id_product` = pl.`id_product`
        AND pl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').'
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_shop` image_shop
        ON (image_shop.`id_product` = p.`id_product` AND image_shop.cover=1 AND image_shop.id_shop='.(int)context.shop->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_lang` il ON (image_shop.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'manufacturer` m ON (m.`id_manufacturer` = p.`id_manufacturer`)
        WHERE product_shop.`active` = 1
        AND product_shop.`show_price` = 1
        '.($front ? ' AND p.`visibility` IN ("both", "catalog")' : '').'
        '.((!$beginning && !$ending) ? ' AND p.`id_product` IN ('.((is_array($tab_id_product) && count($tab_id_product)) ? implode(', ', $tab_id_product) : 0).')' : '').'
        '.$sql_groups.'
        ORDER BY '.(isset($order_by_prefix) ? pSQL($order_by_prefix).'.' : '').pSQL($order_by).' '.pSQL($order_way).'
        LIMIT '.(int)($page_number * $nb_products).', '.(int)$nb_products;

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);

        if (!$result) {
            return false;
        }

        if ($order_by == 'price') {
            Tools::orderbyPrice($result, $order_way);
        }

        return JeproLabAnalyzeModel.getProductsProperties($id_lang, $result);
    }


    /**
     * getProductCategories return an array of categories which this product belongs to
     *
     * @return array of categories
     * /
    public static function getProductCategories($id_product = '')
    {
        $cacheKey = 'JeproLabAnalyzeModel.getProductCategories_'.(int)$id_product;
        if (!Cache::isStored($cacheKey)) {
        $ret = array();

        $row = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
                SELECT `id_category` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
        WHERE `id_product` = '.(int)$id_product
        );

        if ($row) {
            foreach ($row as $val) {
                $ret[] = $val['id_category'];
            }
        }
        Cache::store($cacheKey, $ret);
        return $ret;
    }
        return Cache::retrieve($cacheKey);
    }

    public static function getProductCategoriesFull($id_product = '', $id_lang = null)
    {
        if (!$id_lang) {
            $id_lang = JeproLabContext.getContext()->language->id;
        }

        $ret = array();
        $row = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT cp.`id_category`, cl.`name`, cl.`link_rewrite` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category` c ON (c.id_category = cp.id_category)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_lang` cl ON (cp.`id_category` = cl.`id_category`'.JeproLabLaboratoryModel.addSqlRestrictionOnLang('cl').')
        '.JeproLabLaboratoryModel.addSqlAssociation('category', 'c').'
        WHERE cp.`id_product` = '.(int)$id_product.'
        AND cl.`id_lang` = '.(int)$id_lang
        );

        foreach ($row as $val) {
        $ret[$val['id_category']] = $val;
    }

        return $ret;
    }

    /**
     * getCategories return an array of categories which this product belongs to
     *
     * @return array of categories
     * /
    public function getCategories()
    {
        return JeproLabAnalyzeModel.getProductCategories(this.id);
    }

    /**
     * Gets carriers assigned to the product
     * /
    public function getCarriers()
    {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT c.*
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_carrier` pc
        INNER JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'carrier` c
        ON (c.`id_reference` = pc.`id_carrier_reference` AND c.`deleted` = 0)
        WHERE pc.`id_product` = '.(int)this.id.'
        AND pc.`id_shop` = '.(int)this.id_shop);
    }

    /**
     * Sets carriers assigned to the product
     * /
    public function setCarriers($carrier_list)
    {
        $data = array();

        foreach ($carrier_list as $carrier) {
        $data[] = array(
                'id_product' => (int)this.id,
                'id_carrier_reference' => (int)$carrier,
                'id_shop' => (int)this.id_shop
        );
    }
        Db::getInstance()->execute(
            'DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_carrier`
        WHERE id_product = '.(int)this.id.'
        AND id_shop = '.(int)this.id_shop
        );

        $unique_array = array();
        foreach ($data as $sub_array) {
        if (!in_array($sub_array, $unique_array)) {
            $unique_array[] = $sub_array;
        }
    }

        if (count($unique_array)) {
            Db::getInstance()->insert('product_carrier', $unique_array, false, true, Db::INSERT_IGNORE);
        }
    }

    /**
     * Get product images and legends
     *
     * @param int $id_lang Language id for multilingual legends
     * @return array Product images and legends
     * /
    public function getImages($id_lang, Context context = null)
    {
        return Db::getInstance()->executeS('
            SELECT image_shop.`cover`, i.`id_image`, il.`legend`, i.`position`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'image` i
        '.JeproLabLaboratoryModel.addSqlAssociation('image', 'i').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_lang` il ON (i.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang.')
        WHERE i.`id_product` = '.(int)this.id.'
        ORDER BY `position`'
        );
    }

    /**
     * Get product cover image
     *
     * @return array Product cover image
     * /
    public static function getCover($id_product, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }
        $cacheKey = 'JeproLabAnalyzeModel.getCover_'.(int)$id_product."_".(int)context.shop->id;
        if (!Cache::isStored($cacheKey)) {
        $sql = 'SELECT image_shop.`id_image`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'image` i
        '.JeproLabLaboratoryModel.addSqlAssociation('image', 'i').'
        WHERE i.`id_product` = '.(int)$id_product.'
        AND image_shop.`cover` = 1';
        $result = Db::getInstance()->getRow($sql);
        Cache::store($cacheKey, $result);
        return $result;
    }
        return Cache::retrieve($cacheKey);
    } */

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
        return getStaticPrice(analyzeId, useTax, analyzeAttributeId, decimals, onlyReduction, useReduction , quantity, forceAssociatedTax, customerId, cartId, addressId, withEcoTax, useGroupReduction, context, true);
    }

    /**
     * Returns product price
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
     * Price calculation / Get product price
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
        static_specific_price = JeproLabSpecificPriceModel.getSpecificPrice(analyzeId, labId, currencyId, countryId, groupId, quantity, analyzeAttributeId, customerId, cartId, realQuantity);

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
            String from = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze";
            String innerJoin = " INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lab") + " AS analyze_lab ON (analyze.analyze_id = ";
            innerJoin += " analyze_lab.analyze_id AND analyze_lab.lab_id = " + labId + ") ";
            String where = " WHERE analyze." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
            String leftJoin = "";
            if (JeproLabCombinationModel.isFeaturePublished()) {
                select += "IFNULL(analyze_attribute_lab.analyze_attribute_id, 0) AS analyze_attribute_id, analyze_attribute_lab.";
                select += staticDataBaseObject.quoteName("price") + " AS attribute_price, analyze_attribute_lab.default_on ";
                leftJoin += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_lab") + " AS product_attribute_lab ON (";
                leftJoin += "analyze_attribute_lab.analyze_id = analyze.analyze_id AND analyze_attribute_lab.lab_id = " + labId + ")";
            } else {
                select += " 0 as analyze_attribute_id ";
            }
            String query = select + from + leftJoin + innerJoin + where;
            staticDataBaseObject.setQuery(query);
            ResultSet results = staticDataBaseObject.loadObject();

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

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyzeId, context));
        JeproLabTaxCalculator analyzeTaxCalculator = taxManager.getTaxCalculator();

        // Add Tax
        if (useTax) {
            price = analyzeTaxCalculator.addTaxes(price);
        }

        // Eco Tax
        float ecoTax;
        if (((result.get("ecotax") != null && result.get("ecotax") > 0) || result.get("attribute_ecotax") != null) && withEcoTax) {
            ecoTax = result.get("ecotax");
            if (result.get("attribute_ecotax") != null && result.get("attribute_ecotax") > 0) {
                ecoTax = result.get("attribute_ecotax");
            }

            if (currencyId > 0) {
                ecoTax = JeproLabTools.convertPrice(ecoTax, currencyId);
            }
            if (useTax) {
                // re-init the JeproLabTaxManagerFactory manager for ecotax handling
                taxManager = JeproLabTaxManagerFactory.getManager( address, JeproLabSettingModel.getIntValue("ecotax_tax_rules_group_id"));
                JeproLabTaxCalculator ecotaxTaxCalculator = taxManager.getTaxCalculator();
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
            float reductionFromCategory = JeproLabGroupReductionModel.getValueForAnalyze(analyzeId, groupId);
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
/*
    public static function convertAndFormatPrice(price, $currency = false, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }
        if (!$currency) {
            $currency = context.currency;
        }
        return Tools::displayPrice(Tools::convertPrice($price, $currency), $currency);
    }

    public static function isDiscounted($id_product, $quantity = 1, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $id_group = context.customer->id_default_group;
        $cart_quantity = !context.cart ? 0 : Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT SUM(`quantity`)
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'cart_product`
        WHERE `id_product` = '.(int)$id_product.' AND `id_cart` = '.(int)context.cart->id
        );
        $quantity = $cart_quantity ? $cart_quantity : $quantity;

        $id_currency = (int)context.currency->id;
        $ids = Address::getCountryAndState((int)context.cart->{JeproLabSettingModel.get('PS_TAX_ADDRESS_TYPE')});
        $id_country = $ids['id_country'] ? (int)$ids['id_country'] : (int)JeproLabSettingModel.get('PS_COUNTRY_DEFAULT');
        return (bool)SpecificPrice::getSpecificPrice((int)$id_product, context.shop->id, $id_currency, $id_country, $id_group, $quantity, null, 0, 0, $quantity);
    }

    /**
     * Get product price
     * Same as static function getPriceStatic, no need to specify product id
     *
     * @param bool $JeproLabTaxManagerFactory With taxes or not (optional)
     * @param int $id_product_attribute Product attribute id (optional)
     * @param int $decimals Number of decimals (optional)
     * @param int $divisor Util when paying many time without fees (optional)
     * @return float Product price in euros
     * /
    public function getPrice($JeproLabTaxManagerFactory = true, $id_product_attribute = null, $decimals = 6,
                             $divisor = null, $only_reduc = false, $usereduc = true, $quantity = 1)
    {
        return JeproLabAnalyzeModel.getPriceStatic((int)this.id, $JeproLabTaxManagerFactory, $id_product_attribute, $decimals, $divisor, $only_reduc, $usereduc, $quantity);
    }

    public function getPublicPrice($JeproLabTaxManagerFactory = true, $id_product_attribute = null, $decimals = 6,
                                   $divisor = null, $only_reduc = false, $usereduc = true, $quantity = 1)
    {
        $specific_price_output = null;
        return JeproLabAnalyzeModel.getPriceStatic((int)this.id, $JeproLabTaxManagerFactory, $id_product_attribute, $decimals, $divisor, $only_reduc, $usereduc, $quantity,
            false, null, null, null, $specific_price_output, true, true, null, false);
    }

    public function getIdProductAttributeMostExpensive()
    {
        if (!Combination::isFeatureActive()) {
        return 0;
    }

        return (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT pa.`id_product_attribute`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)this.id.'
        ORDER BY product_attribute_shop.`price` DESC');
    }

    public function getDefaultIdProductAttribute()
    {
        if (!Combination::isFeatureActive()) {
        return 0;
    }

        return (int)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
            SELECT pa.`id_product_attribute`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)this.id.'
        AND product_attribute_shop.default_on = 1'
        );
    }

    public function getPriceWithoutReduct($notax = false, $id_product_attribute = false, $decimals = 6)
    {
        return JeproLabAnalyzeModel.getPriceStatic((int)this.id, !$notax, $id_product_attribute, $decimals, null, false, false);
    }

    /**
     * Display price with right format and currency
     *
     * @param array $params Params
     * @param $smarty Smarty object
     * @return string Price with right format and currency
     * /
    public static function convertPrice($params, &$smarty)
    {
        return Tools::displayPrice($params['price'], JeproLabContext.getContext()->currency);
    }

    /**
     * Convert price with currency
     *
     * @param array $params
     * @param object $smarty DEPRECATED
     * @return string Ambigous <string, mixed, Ambigous <number, string>>
     * /
    public static function convertPriceWithCurrency($params, &$smarty)
    {
        return Tools::displayPrice($params['price'], $params['currency'], false);
    }

    public static function displayWtPrice($params, &$smarty)
    {
        return Tools::displayPrice($params['p'], JeproLabContext.getContext()->currency);
    }

    /**
     * Display WT price with currency
     *
     * @param array $params
     * @param Smarty $smarty DEPRECATED
     * @return string Ambigous <string, mixed, Ambigous <number, string>>
     * /
    public static function displayWtPriceWithCurrency($params, &$smarty)
    {
        return Tools::displayPrice($params['price'], $params['currency'], false);
    }

    /**
     * Get available product quantities
     *
     * @param int $id_product Product id
     * @param int $id_product_attribute Product attribute id (optional)
     * @return int Available quantities
     * /
    public static function getQuantity($id_product, $id_product_attribute = null, $cache_is_pack = null)
    {
        if ((int)$cache_is_pack || ($cache_is_pack === null && JeproLabAnalyzePackModel.isPack((int)$id_product))) {
        if (!JeproLabAnalyzePackModel.isInStock((int)$id_product)) {
            return 0;
        }
    }

        // @since 1.5.0
        return (StockAvailable::getQuantityAvailableByProduct($id_product, $id_product_attribute));
    } */

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
     * @param analyzeAlias Alias of product table
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
                query += " AND stock.analyze_attribute_id = IFNULL(`'.bqSQL($product_attribute).'`.analyze_attribute_id, 0)";
            }
        }

        query += JeproLabStockAvailableModel.addSqlLaboratoryRestriction(lab, "stock") + ") ";

        return query;
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
            $this->errors[] = $this->l('This product must be in the default category.');
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

    /*
     * @deprecated since 1.5.0
     *
     * It's not possible to use this method with new stockManager and stockAvailable features
     * Now this method do nothing
     *
     * @see StockManager if you want to manage real stock
     * @see StockAvailable if you want to manage available quantities for sale on your shop(s)
     *
     * @deprecated 1.5.3.0
     * @return false
     * /
    public static function updateQuantity()
    {
        Tools::displayAsDeprecated();

        return false;
    }

    /**
     * @deprecated since 1.5.0
     *
     * It's not possible to use this method with new stockManager and stockAvailable features
     * Now this method do nothing
     *
     * @deprecated 1.5.3.0
     * @see StockManager if you want to manage real stock
     * @see StockAvailable if you want to manage available quantities for sale on your shop(s)
     * @return false
     * /
    public static function reinjectQuantities()
    {
        Tools::displayAsDeprecated();

        return false;
    }

    public static function isAvailableWhenOutOfStock($out_of_stock)
    {
        // @TODO 1.5.0 Update of STOCK_MANAGEMENT & ORDER_OUT_OF_STOCK
        static $ps_stock_management = null;
        if ($ps_stock_management === null) {
            $ps_stock_management = JeproLabSettingModel.get('PS_STOCK_MANAGEMENT');
        }

        if (!$ps_stock_management) {
            return true;
        } else {
            static $ps_order_out_of_stock = null;
            if ($ps_order_out_of_stock === null) {
                $ps_order_out_of_stock = JeproLabSettingModel.get('PS_ORDER_OUT_OF_STOCK');
            }

            return (int)$out_of_stock == 2 ? (int)$ps_order_out_of_stock : (int)$out_of_stock;
        }
    }

    /**
     * Check product availability
     *
     * @param int $qty Quantity desired
     * @return bool True if product is available with this quantity
     * /
    public function checkQty($qty)
    {
        if (JeproLabAnalyzePackModel.isPack((int)this.id) && !JeproLabAnalyzePackModel.isInStock((int)this.id)) {
        return false;
    }

        if (this.isAvailableWhenOutOfStock(StockAvailable::outOfStock(this.id))) {
        return true;
    }

        if (isset(this.id_product_attribute)) {
            $id_product_attribute = this.id_product_attribute;
        } else {
            $id_product_attribute = 0;
        }

        return ($qty <= StockAvailable::getQuantityAvailableByProduct(this.id, $id_product_attribute));
    }

    /**
     * Check if there is no default attribute and create it if not
     * /
    public function checkDefaultAttributes()
    {
        if (!this.id) {
            return false;
        }

        if (Db::getInstance()->getValue('SELECT COUNT(*)
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE product_attribute_shop.`default_on` = 1
        AND pa.`id_product` = '.(int)this.id) > JeproLabLaboratoryModel.getTotalShops(true)) {
        Db::getInstance()->execute('UPDATE '.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop product_attribute_shop, '.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute pa
        SET product_attribute_shop.default_on=NULL, pa.default_on = NULL
        WHERE product_attribute_shop.id_product_attribute=pa.id_product_attribute AND pa.id_product='.(int)this.id
            .JeproLabLaboratoryModel.addSqlRestriction(false, 'product_attribute_shop'));
    }

    $row = Db::getInstance()->getRow('
                                     SELECT pa.id_product
                                             FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
                                             '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
                                             WHERE product_attribute_shop.`default_on` = 1
                                             AND pa.`id_product` = '.(int)this.id
    );
    if ($row) {
        return true;
    }

    $mini = Db::getInstance()->getRow('
                                      SELECT MIN(pa.id_product_attribute) as `id_attr`
    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
    '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
    WHERE pa.`id_product` = '.(int)this.id
            );
    if (!$mini) {
        return false;
    }

    if (!ObjectModel::updateMultishopTable('Combination', array('default_on' => 1), 'a.id_product_attribute = '.(int)$mini['id_attr'])) {
        return false;
    }
    return true;
}

    public static function getAttributesColorList(Array $products, $have_stock = true)
    {
        if (!count($products)) {
            return array();
        }

        $id_lang = JeproLabContext.getContext()->language->id;

        $check_stock = !JeproLabSettingModel.get('PS_DISP_UNAVAILABLE_ATTR');
        if (!$res = Db::getInstance()->executeS('
            SELECT pa.`id_product`, a.`color`, pac.`id_product_attribute`, '.($check_stock ? 'SUM(IF(stock.`quantity` > 0, 1, 0))' : '0').' qty, a.`id_attribute`, al.`name`, IF(color = "", a.id_attribute, color) group_by
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').
        ($check_stock ? JeproLabAnalyzeModel.sqlStock('pa', 'pa') : '').'
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac ON (pac.`id_product_attribute` = product_attribute_shop.`id_product_attribute`)
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a ON (a.`id_attribute` = pac.`id_attribute`)
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)$id_lang.')
        JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group` ag ON (a.id_attribute_group = ag.`id_attribute_group`)
        WHERE pa.`id_product` IN ('.implode(array_map('intval', $products), ',').') AND ag.`is_color_group` = 1
        GROUP BY pa.`id_product`, a.`id_attribute`, `group_by`
        '.($check_stock ? 'HAVING qty > 0' : '').'
        ORDER BY a.`position` ASC;'
        )
        ) {
        return false;
    }

        $colors = array();
        foreach ($res as $row) {
        if (Tools::isEmpty($row['color']) && !@filemtime(_PS_COL_IMG_DIR_.$row['id_attribute'].'.jpg')) {
            continue;
        }

        $colors[(int)$row['id_product']][] = array('id_product_attribute' => (int)$row['id_product_attribute'], 'color' => $row['color'], 'id_product' => $row['id_product'], 'name' => $row['name'], 'id_attribute' => $row['id_attribute']);
    }

        return $colors;
    }

    /**
     * Get all available attribute groups
     *
     * @param langId Language id
     * @return array Attribute groups
     */
    public ResultSet getAttributesGroups(int langId){
        if (!JeproLabCombinationModel.isFeaturePublished()){
            return  null;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT attribute_group." + dataBaseObject.quoteName("attribute_group_id") + ", attribute_group." + dataBaseObject.quoteName("is_color_group");
        query += ", attribute_group_lang." + dataBaseObject.quoteName("name") + " AS group_name, attribute_group_lang." + dataBaseObject.quoteName("public_name");
        query += " AS public_group_name, attribute." + dataBaseObject.quoteName("attribute_id") + ", attribute_lang." + dataBaseObject.quoteName("name");
        query += " AS attribute_name, attribute." + dataBaseObject.quoteName("color") + " AS attribute_color, analyze_attribute_lab." + dataBaseObject.quoteName("analyze_attribute_id");
        query += ", IFNULL(stock.quantity, 0) as quantity, analyze_attribute_lab." + dataBaseObject.quoteName("price") + ", analyze_attribute_lab.";
        query += dataBaseObject.quoteName("ecotax") + ", analyze_attribute_lab." + dataBaseObject.quoteName("weight") + ", analyze_attribute_lab." ;
        query += dataBaseObject.quoteName("default_on") + ", analyze_attribute." + dataBaseObject.quoteName("reference") + ", analyze_attribute_lab.";
        query += dataBaseObject.quoteName("unit_price_impact") + ", analyze_attribute_lab." + dataBaseObject.quoteName("minimal_quantity") + ", analyze_attribute_lab.";
        query += dataBaseObject.quoteName("available_date") + ", attribute_group." + dataBaseObject.quoteName("group_type") + " FROM ";
        query += dataBaseObject.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute " + JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute");
        query += JeproLabAnalyzeModel.queryStock("analyze_attribute") + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination");
        query += " AS analyze_attribute_combination ON (analyze_attribute_combination." + dataBaseObject.quoteName("analyze_attribute_id") + " = analyze_attribute.";
        query += dataBaseObject.quoteName("analyze__attribute_id") + ") LEFT JOIN  " + dataBaseObject.quoteName("#__jeprolab_attribute") + " AS attribute";
        query += " ON (attribute." + dataBaseObject.quoteName("attribute_id") + " = analyze_attribute_combination." + dataBaseObject.quoteName("attribute_id") ;
        query += ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_attribute_group") + " AS attribute_group ON (attribute_group." + dataBaseObject.quoteName("attribute_group_id");
        query += " = attribute." + dataBaseObject.quoteName("attribute_group_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_attribute_lang") + " AS attribute_lang ON (attribute.";
        query += dataBaseObject.quoteName("attribute_id") + " = attribute_lang." + dataBaseObject.quoteName("attribute_id") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_attribute_group_lang");
        query += " AS attribute_group_lang ON (attribute_group." + dataBaseObject.quoteName("attribute_group_id") + " = attribute_group_lang." + dataBaseObject.quoteName("attribute_group_id") ;
        query += ") " + JeproLabLaboratoryModel.addSqlAssociation("attribute") + " WHERE analyze_attribute." + dataBaseObject.quoteName("analyze_id") + " = " + this.analyze_id;
        query += " AND attribute_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + " AND attribute_group_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId;
        query += " GROUP BY attribute_group_id, analyze_attribute_id ORDER BY attribute_group." + dataBaseObject.quoteName("position") + " ASC, attribute.";
        query += dataBaseObject.quoteName("position") + " ASC, " + " attribute_group_lang." + dataBaseObject.quoteName("name") + " ASC";

        dataBaseObject.setQuery(query);
        return dataBaseObject.loadObject();
    }

    /*
     * Delete product accessories
     *
     * @return mixed Deletion result
     * /
    public function deleteAccessories()
    {
        return Db::getInstance()->delete('accessory', 'id_product_1 = '.(int)this.id);
    }

    /**
     * Delete product from other products accessories
     *
     * @return mixed Deletion result
     * /
    public function deleteFromAccessories()
    {
        return Db::getInstance()->delete('accessory', 'id_product_2 = '.(int)this.id);
    }

    /**
     * Get product accessories (only names)
     *
     * @param int $id_lang Language id
     * @param int $id_product Product id
     * @return array Product accessories
     * /
    public static function getAccessoriesLight($id_lang, $id_product)
    {
        return Db::getInstance()->executeS('
            SELECT p.`id_product`, p.`reference`, pl.`name`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'accessory`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p ON (p.`id_product`= `id_product_2`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (
            p.`id_product` = pl.`id_product`
        AND pl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').'
        )
        WHERE `id_product_1` = '.(int)$id_product
        );
    }

    /**
     * Get product accessories
     *
     * @param int $id_lang Language id
     * @return array Product accessories
     * /
    public function getAccessories($id_lang, $active = true)
    {
        $sql = 'SELECT p.*, product_shop.*, stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity, pl.`description`, pl.`description_short`, pl.`link_rewrite`,
        pl.`meta_description`, pl.`meta_keywords`, pl.`meta_title`, pl.`name`, pl.`available_now`, pl.`available_later`,
        image_shop.`id_image` id_image, il.`legend`, m.`name` as manufacturer_name, cl.`name` AS category_default, IFNULL(product_attribute_shop.id_product_attribute, 0) id_product_attribute,
            DATEDIFF(
                    p.`date_add`,
                    DATE_SUB(
                            "'.date('Y-m-d').' 00:00:00",
                            INTERVAL '.(Validate::isUnsignedInt(JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT')) ? JeproLabSettingModel.get('PS_NB_DAYS_NEW_PRODUCT') : 20).' DAY
        )
        ) > 0 AS new
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'accessory`
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p ON p.`id_product` = `id_product_2`
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop` product_attribute_shop
        ON (p.`id_product` = product_attribute_shop.`id_product` AND product_attribute_shop.`default_on` = 1 AND product_attribute_shop.id_shop='.(int)this.id_shop.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (
            p.`id_product` = pl.`id_product`
        AND pl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').'
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_lang` cl ON (
            product_shop.`id_category_default` = cl.`id_category`
        AND cl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('cl').'
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_shop` image_shop
        ON (image_shop.`id_product` = p.`id_product` AND image_shop.cover=1 AND image_shop.id_shop='.(int)this.id_shop.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'image_lang` il ON (image_shop.`id_image` = il.`id_image` AND il.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'manufacturer` m ON (p.`id_manufacturer`= m.`id_manufacturer`)
        '.JeproLabAnalyzeModel.sqlStock('p', 0).'
        WHERE `id_product_1` = '.(int)this.id.
        ($active ? ' AND product_shop.`active` = 1 AND product_shop.`visibility` != \'none\'' : '').'
        GROUP BY product_shop.id_product';

        if (!$result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql)) {
        return false;
    }

        foreach ($result as &$row) {
        $row['id_product_attribute'] = JeproLabAnalyzeModel.getDefaultAttribute((int)$row['id_product']);
    }

        return this.getProductsProperties($id_lang, $result);
    }

    public static function getAccessoryById($accessory_id)
    {
        return Db::getInstance()->getRow('SELECT `id_product`, `name` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` WHERE `id_product` = '.(int)$accessory_id);
    }

    /**
     * Link accessories with product
     *
     * @param array $accessories_id Accessories ids
     * /
    public function changeAccessories($accessories_id)
    {
        foreach ($accessories_id as $id_product_2) {
        Db::getInstance()->insert('accessory', array(
                        'id_product_1' => (int)this.id,
                'id_product_2' => (int)$id_product_2
        ));
    }
    }

    /**
     * Add new feature to product
     * /
    public function addFeaturesCustomToDB($id_value, $lang, $cust)
    {
        $row = array('id_feature_value' => (int)$id_value, 'id_lang' => (int)$lang, 'value' => pSQL($cust));
        return Db::getInstance()->insert('feature_value_lang', $row);
    }

    public function addFeaturesToDB($id_feature, $id_value, $cust = 0)
    {
        if ($cust) {
            $row = array('id_feature' => (int)$id_feature, 'custom' => 1);
            Db::getInstance()->insert('feature_value', $row);
            $id_value = Db::getInstance()->Insert_ID();
        }
        $row = array('id_feature' => (int)$id_feature, 'id_product' => (int)this.id, 'id_feature_value' => (int)$id_value);
        Db::getInstance()->insert('feature_product', $row);
        SpecificPriceRule::applyAllRules(array((int)this.id));
        if ($id_value) {
            return ($id_value);
        }
    }

    public static function addFeatureProductImport($id_product, $id_feature, $id_feature_value)
    {
        return Db::getInstance()->execute('
            INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product` (`id_feature`, `id_product`, `id_feature_value`)
        VALUES ('.(int)$id_feature.', '.(int)$id_product.', '.(int)$id_feature_value.')
        ON DUPLICATE KEY UPDATE `id_feature_value` = '.(int)$id_feature_value
        );
    }

    /**
     * Select all features for the object
     *
     * @return array Array with feature product's data
     * /
    public function getFeatures()
    {
        return JeproLabAnalyzeModel.getFeaturesStatic((int)this.id);
    }

    public static function getFeaturesStatic($id_product)
    {
        if (!Feature::isFeatureActive()) {
        return array();
    }
        if (!array_key_exists($id_product, JeproLabAnalyzeModel.$_cacheFeatures)) {
            JeproLabAnalyzeModel.$_cacheFeatures[$id_product] = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
                    SELECT fp.id_feature, fp.id_product, fp.id_feature_value, custom
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product` fp
            LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value` fv ON (fp.id_feature_value = fv.id_feature_value)
            WHERE `id_product` = '.(int)$id_product
            );
        }
        return JeproLabAnalyzeModel.$_cacheFeatures[$id_product];
    }

    public static function cacheProductsFeatures($product_ids)
    {
        if (!Feature::isFeatureActive()) {
        return;
    }

        $product_implode = array();
        foreach ($product_ids as $id_product) {
        if ((int)$id_product && !array_key_exists($id_product, JeproLabAnalyzeModel.$_cacheFeatures)) {
            $product_implode[] = (int)$id_product;
        }
    }
        if (!count($product_implode)) {
            return;
        }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT id_feature, id_product, id_feature_value
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product`
        WHERE `id_product` IN ('.implode($product_implode, ',').')');
        foreach ($result as $row) {
        if (!array_key_exists($row['id_product'], JeproLabAnalyzeModel.$_cacheFeatures)) {
            JeproLabAnalyzeModel.$_cacheFeatures[$row['id_product']] = array();
        }
        JeproLabAnalyzeModel.$_cacheFeatures[$row['id_product']][] = $row;
    }
    }

    public static function cacheFrontFeatures($product_ids, $id_lang)
    {
        if (!Feature::isFeatureActive()) {
        return;
    }

        $product_implode = array();
        foreach ($product_ids as $id_product) {
        if ((int)$id_product && !array_key_exists($id_product."_".$id_lang, JeproLabAnalyzeModel.$_cacheFeatures)) {
            $product_implode[] = (int)$id_product;
        }
    }
        if (!count($product_implode)) {
            return;
        }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT id_product, name, value, pf.id_feature
            FROM '.staticDataBaseObject.quoteName("#__jeprolab.'feature_product pf
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'feature_lang fl ON (fl.id_feature = pf.id_feature AND fl.id_lang = '.(int)$id_lang.')
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'feature_value_lang fvl ON (fvl.id_feature_value = pf.id_feature_value AND fvl.id_lang = '.(int)$id_lang.')
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'feature f ON (f.id_feature = pf.id_feature)
        '.JeproLabLaboratoryModel.addSqlAssociation('feature', 'f').'
        WHERE `id_product` IN ('.implode($product_implode, ',').')
        ORDER BY f.position ASC');

        foreach ($result as $row) {
        if (!array_key_exists($row['id_product']."_".$id_lang, JeproLabAnalyzeModel.$_frontFeaturesCache)) {
            JeproLabAnalyzeModel.$_frontFeaturesCache[$row['id_product']."_".$id_lang] = array();
        }
        if (!isset(JeproLabAnalyzeModel.$_frontFeaturesCache[$row['id_product']."_".$id_lang][$row['id_feature']])) {
            JeproLabAnalyzeModel.$_frontFeaturesCache[$row['id_product']."_".$id_lang][$row['id_feature']] = $row;
        }
    }
    }

    /**
     * Admin panel product search
     *
     * @param int $id_lang Language id
     * @param string $query Search query
     * @return array Matching products
     * /
    public static function searchByName($id_lang, $query, Context context = null)
    {
        if (!context) {
            context = JeproLabContext.getContext();
        }

        $sql = new DbQuery();
        $sql->select('p.`id_product`, pl.`name`, p.`ean13`, p.`upc`, p.`active`, p.`reference`, m.`name` AS manufacturer_name, stock.`quantity`, product_shop.advanced_stock_management, p.`customizable`');
        $sql->from('product', 'p');
        $sql->join(JeproLabLaboratoryModel.addSqlAssociation('product', 'p'));
        $sql->leftJoin('product_lang', 'pl', '
                p.`id_product` = pl.`id_product`
        AND pl.`id_lang` = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl')
        );
        $sql->leftJoin('manufacturer', 'm', 'm.`id_manufacturer` = p.`id_manufacturer`');

        $where = 'pl.`name` LIKE \'%'.pSQL($query).'%\'
        OR p.`ean13` LIKE \'%'.pSQL($query).'%\'
        OR p.`upc` LIKE \'%'.pSQL($query).'%\'
        OR p.`reference` LIKE \'%'.pSQL($query).'%\'
        OR p.`supplier_reference` LIKE \'%'.pSQL($query).'%\'
        OR EXISTS(SELECT * FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_supplier` sp WHERE sp.`id_product` = p.`id_product` AND `product_supplier_reference` LIKE \'%'.pSQL($query).'%\')';

        $sql->orderBy('pl.`name` ASC');

        if (Combination::isFeatureActive()) {
        $where .= ' OR EXISTS(SELECT * FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` `pa` WHERE pa.`id_product` = p.`id_product` AND (pa.`reference` LIKE \'%'.pSQL($query).'%\'
        OR pa.`supplier_reference` LIKE \'%'.pSQL($query).'%\'
        OR pa.`ean13` LIKE \'%'.pSQL($query).'%\'
        OR pa.`upc` LIKE \'%'.pSQL($query).'%\'))';
    }
        $sql->where($where);
        $sql->join(JeproLabAnalyzeModel.sqlStock('p', 0));

        $result = Db::getInstance()->executeS($sql);

        if (!$result) {
            return false;
        }

        $results_array = array();
        foreach ($result as $row) {
        $row['price_tax_incl'] = JeproLabAnalyzeModel.getPriceStatic($row['id_product'], true, null, 2);
        $row['price_tax_excl'] = JeproLabAnalyzeModel.getPriceStatic($row['id_product'], false, null, 2);
        $results_array[] = $row;
    }
        return $results_array;
    }

    /**
     * Duplicate attributes when duplicating a product
     *
     * @param int $id_product_old Old product id
     * @param int $id_product_new New product id
     * /
    public static function duplicateAttributes($id_product_old, $id_product_new)
    {
        $return = true;
        $combination_images = array();

        $result = Db::getInstance()->executeS('
            SELECT pa.*, product_attribute_shop.*
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)$id_product_old
        );
        $combinations = array();

        foreach ($result as $row) {
        $id_product_attribute_old = (int)$row['id_product_attribute'];
        if (!isset($combinations[$id_product_attribute_old])) {
            $id_combination = null;
            $id_shop = null;
            $result2 = Db::getInstance()->executeS('
                    SELECT *
                            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination`
            WHERE `id_product_attribute` = '.$id_product_attribute_old
            );
        } else {
            $id_combination = (int)$combinations[$id_product_attribute_old];
            $id_shop = (int)$row['id_shop'];
            context_old = JeproLabLaboratoryModel.getContext();
            context_shop_id_old = JeproLabLaboratoryModel.getContextShopID();
            JeproLabLaboratoryModel.setContext(JeproLabLaboratoryModel.CONTEXT_SHOP, $id_shop);
        }

        $row['id_product'] = $id_product_new;
        unset($row['id_product_attribute']);

        $combination = new Combination($id_combination, null, $id_shop);
        foreach ($row as $k => $v) {
            $combination->$k = $v;
        }
        $return &= $combination->save();

        $id_product_attribute_new = (int)$combination->id;

        if ($result_images = JeproLabAnalyzeModel._getAttributeImageAssociations($id_product_attribute_old)) {
            $combination_images['old'][$id_product_attribute_old] = $result_images;
            $combination_images['new'][$id_product_attribute_new] = $result_images;
        }

        if (!isset($combinations[$id_product_attribute_old])) {
            $combinations[$id_product_attribute_old] = (int)$id_product_attribute_new;
            foreach ($result2 as $row2) {
                $row2['id_product_attribute'] = $id_product_attribute_new;
                $return &= Db::getInstance()->insert('product_attribute_combination', $row2);
            }
        } else {
            JeproLabLaboratoryModel.setContext(context_old, context_shop_id_old);
        }

        //Copy suppliers
        $result3 = Db::getInstance()->executeS('
                SELECT *
                        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_supplier`
        WHERE `id_product_attribute` = '.(int)$id_product_attribute_old.'
        AND `id_product` = '.(int)$id_product_old);

        foreach ($result3 as $row3) {
            unset($row3['id_product_supplier']);
            $row3['id_product'] = $id_product_new;
            $row3['id_product_attribute'] = $id_product_attribute_new;
            $return &= Db::getInstance()->insert('product_supplier', $row3);
        }
    }

        $impacts = JeproLabAnalyzeModel.getAttributesImpacts($id_product_old);

        if (is_array($impacts) && count($impacts)) {
            $impact_sql = 'INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_impact` (`id_product`, `id_attribute`, `weight`, `price`) VALUES ';

            foreach ($impacts as $id_attribute => $impact) {
                $impact_sql .= '('.(int)$id_product_new.', '.(int)$id_attribute.', '.(float)$impacts[$id_attribute]['weight'].', '
                        .(float)$impacts[$id_attribute]['price'].'),';
            }

            $impact_sql = substr_replace($impact_sql, '', -1);
            $impact_sql .= ' ON DUPLICATE KEY UPDATE `price` = VALUES(price), `weight` = VALUES(weight)';

            Db::getInstance()->execute($impact_sql);
        }

        return !$return ? false : $combination_images;
    }

    public static function getAttributesImpacts($id_product)
    {
        $return = array();
        $result = Db::getInstance()->executeS(
            'SELECT ai.`id_attribute`, ai.`price`, ai.`weight`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_impact` ai
        WHERE ai.`id_product` = '.(int)$id_product);

        if (!$result) {
            return array();
        }
        foreach ($result as $impact) {
        $return[$impact['id_attribute']]['price'] = (float)$impact['price'];
        $return[$impact['id_attribute']]['weight'] = (float)$impact['weight'];
    }
        return $return;
    }

    /**
     * Get product attribute image associations
     * @param int $id_product_attribute
     * @return array
     * /
    public static function _getAttributeImageAssociations($id_product_attribute)
    {
        $combination_images = array();
        $data = Db::getInstance()->executeS('
            SELECT `id_image`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_image`
        WHERE `id_product_attribute` = '.(int)$id_product_attribute);
        foreach ($data as $row) {
        $combination_images[] = (int)$row['id_image'];
    }
        return $combination_images;
    }

    public static function duplicateAccessories($id_product_old, $id_product_new)
    {
        $return = true;

        $result = Db::getInstance()->executeS('
            SELECT *
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'accessory`
        WHERE `id_product_1` = '.(int)$id_product_old);
        foreach ($result as $row) {
        $data = array(
                'id_product_1' => (int)$id_product_new,
                'id_product_2' => (int)$row['id_product_2']);
        $return &= Db::getInstance()->insert('accessory', $data);
    }
        return $return;
    }

    public static function duplicateTags($id_product_old, $id_product_new)
    {
        $tags = Db::getInstance()->executeS('SELECT `id_tag`, `id_lang` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_tag` WHERE `id_product` = '.(int)$id_product_old);
        if (!Db::getInstance()->NumRows()) {
        return true;
    }

        $data = array();
        foreach ($tags as $tag) {
        $data[] = array(
                'id_product' => (int)$id_product_new,
                'id_tag' => (int)$tag['id_tag'],
                'id_lang' => (int)$tag['id_lang'],
        );
    }

        return Db::getInstance()->insert('product_tag', $data);
    }

    public static function duplicateDownload($id_product_old, $id_product_new)
    {
        $sql = 'SELECT `display_filename`, `filename`, `date_add`, `date_expiration`, `nb_days_accessible`, `nb_downloadable`, `active`, `is_shareable`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_download`
        WHERE `id_product` = '.(int)$id_product_old;
        $results = Db::getInstance()->executeS($sql);
        if (!$results) {
            return true;
        }

        $data = array();
        foreach ($results as $row) {
        $new_filename = ProductDownload::getNewFilename();
        copy(_PS_DOWNLOAD_DIR_.$row['filename'], _PS_DOWNLOAD_DIR_.$new_filename);

        $data[] = array(
                'id_product' => (int)$id_product_new,
                'display_filename' => pSQL($row['display_filename']),
                'filename' => pSQL($new_filename),
                'date_expiration' => pSQL($row['date_expiration']),
                'nb_days_accessible' => (int)$row['nb_days_accessible'],
                'nb_downloadable' => (int)$row['nb_downloadable'],
                'active' => (int)$row['active'],
                'is_shareable' => (int)$row['is_shareable'],
                'date_add' => date('Y-m-d H:i:s')
        );
    }
        return Db::getInstance()->insert('product_download', $data);
    }

    public static function duplicateAttachments($id_product_old, $id_product_new)
    {
        // Get all ids attachments of the old product
        $sql = 'SELECT `id_attachment` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attachment` WHERE `id_product` = '.(int)$id_product_old;
        $results = Db::getInstance()->executeS($sql);

        if (!$results) {
            return true;
        }

        $data = array();

        // Prepare data of table product_attachment
        foreach ($results as $row) {
        $data[] = array(
                'id_product' => (int)$id_product_new,
                'id_attachment' => (int)$row['id_attachment']
        );
    }

        // Duplicate product attachement
        $res = Db::getInstance()->insert('product_attachment', $data);
        JeproLabAnalyzeModel.updateCacheAttachment((int)$id_product_new);
        return $res;
    }

    /**
     * Duplicate features when duplicating a product
     *
     * @param int $id_product_old Old product id
     * @param int $id_product_old New product id
     * /
    public static function duplicateFeatures($id_product_old, $id_product_new)
    {
        $return = true;

        $result = Db::getInstance()->executeS('
            SELECT *
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product`
        WHERE `id_product` = '.(int)$id_product_old);
        foreach ($result as $row) {
        $result2 = Db::getInstance()->getRow('
                SELECT *
                        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value`
        WHERE `id_feature_value` = '.(int)$row['id_feature_value']);
        // Custom feature value, need to duplicate it
        if ($result2['custom']) {
            $old_id_feature_value = $result2['id_feature_value'];
            unset($result2['id_feature_value']);
            $return &= Db::getInstance()->insert('feature_value', $result2);
            $max_fv = Db::getInstance()->getRow('
                    SELECT MAX(`id_feature_value`) AS nb
                    FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value`');
            $new_id_feature_value = $max_fv['nb'];

            foreach (Language::getIDs(false) as $id_lang) {
                $result3 = Db::getInstance()->getRow('
                        SELECT *
                                FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_value_lang`
                WHERE `id_feature_value` = '.(int)$old_id_feature_value.'
                AND `id_lang` = '.(int)$id_lang);

                if ($result3) {
                    $result3['id_feature_value'] = (int)$new_id_feature_value;
                    $result3['value'] = pSQL($result3['value']);
                    $return &= Db::getInstance()->insert('feature_value_lang', $result3);
                }
            }
            $row['id_feature_value'] = $new_id_feature_value;
        }

        $row['id_product'] = (int)$id_product_new;
        $return &= Db::getInstance()->insert('feature_product', $row);
    }
        return $return;
    }

    protected static function _getCustomizationFieldsNLabels($product_id, $id_shop = null)
    {
        if (!Customization::isFeatureActive()) {
        return false;
    }

        if (JeproLabLaboratoryModel.isFeatureActive() && !$id_shop) {
        $id_shop = (int)JeproLabContext.getContext()->shop->id;
    }

        $customizations = array();
        if (($customizations['fields'] = Db::getInstance()->executeS('
            SELECT `id_customization_field`, `type`, `required`
            FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        WHERE `id_product` = '.(int)$product_id.'
        ORDER BY `id_customization_field`')) === false) {
        return false;
    }

if (empty($customizations['fields'])) {
        return array();
        }

        $customization_field_ids = array();
        foreach ($customizations['fields'] as $customization_field) {
        $customization_field_ids[] = (int)$customization_field['id_customization_field'];
        }

        if (($customization_labels = Db::getInstance()->executeS('
        SELECT `id_customization_field`, `id_lang`, `name`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        WHERE `id_customization_field` IN ('.implode(', ', $customization_field_ids).')'.($id_shop ? ' AND cfl.`id_shop` = '.$id_shop : '').'
        ORDER BY `id_customization_field`')) === false) {
        return false;
        }

        foreach ($customization_labels as $customization_label) {
        $customizations['labels'][$customization_label['id_customization_field']][] = $customization_label;
        }

        return $customizations;
        }

public static function duplicateSpecificPrices($old_product_id, $product_id)
        {
        foreach (SpecificPrice::getIdsByProductId((int)$old_product_id) as $data) {
        $specific_price = new SpecificPrice((int)$data['id_specific_price']);
        if (!$specific_price->duplicate((int)$product_id)) {
        return false;
        }
        }
        return true;
        }

public static function duplicateCustomizationFields($old_product_id, $product_id)
        {
        // If customization is not activated, return success
        if (!Customization::isFeatureActive()) {
        return true;
        }
        if (($customizations = JeproLabAnalyzeModel._getCustomizationFieldsNLabels($old_product_id)) === false) {
        return false;
        }
        if (empty($customizations)) {
        return true;
        }
        foreach ($customizations['fields'] as $customization_field) {
            /* The new datas concern the new product * /
        $customization_field['id_product'] = (int)$product_id;
        $old_customization_field_id = (int)$customization_field['id_customization_field'];

        unset($customization_field['id_customization_field']);

        if (!Db::getInstance()->insert('customization_field', $customization_field)
        || !$customization_field_id = Db::getInstance()->Insert_ID()) {
        return false;
        }

        if (isset($customizations['labels'])) {
        foreach ($customizations['labels'][$old_customization_field_id] as $customization_label) {
        $data = array(
        'id_customization_field' => (int)$customization_field_id,
        'id_lang' => (int)$customization_label['id_lang'],
        'name' => pSQL($customization_label['name']),
        );

        if (!Db::getInstance()->insert('customization_field_lang', $data)) {
        return false;
        }
        }
        }
        }
        return true;
        }

/**
 * Adds suppliers from old product onto a newly duplicated product
 *
 * @param int $id_product_old
 * @param int $id_product_new
 * /
public static function duplicateSuppliers($id_product_old, $id_product_new)
        {
        $result = Db::getInstance()->executeS('
        SELECT *
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_supplier`
        WHERE `id_product` = '.(int)$id_product_old.' AND `id_product_attribute` = 0');

        foreach ($result as $row) {
        unset($row['id_product_supplier']);
        $row['id_product'] = $id_product_new;
        if (!Db::getInstance()->insert('product_supplier', $row)) {
        return false;
        }
        }

        return true;
        }

/**
 * Get the link of the product page of this product
 * /
public function getLink(Context context = null)
        {
        if (!context) {
        context = JeproLabContext.getContext();
        }
        return context.link->getProductLink($this);
        }

public function getTags($id_lang)
        {
        if (!this.isFullyLoaded && is_null(this.tags)) {
        this.tags = Tag::getProductTags(this.id);
        }

        if (!(this.tags && array_key_exists($id_lang, this.tags))) {
        return '';
        }

        $result = '';
        foreach (this.tags[$id_lang] as $tag_name) {
        $result .= $tag_name.', ';
        }

        return rtrim($result, ', ');
        }

public static function defineProductImage($row, $id_lang)
        {
        if (isset($row['id_image']) && $row['id_image']) {
        return $row['id_product']."_".$row['id_image'];
        }

        return Language::getIsoById((int)$id_lang).'-default';
        }

public static function getProductProperties($id_lang, $row, Context context = null)
        {
        if (!$row['id_product']) {
        return false;
        }

        if (context == null) {
        context = JeproLabContext.getContext();
        }

        $id_product_attribute = $row['id_product_attribute'] = (!empty($row['id_product_attribute']) ? (int)$row['id_product_attribute'] : null);

        // JeproLabAnalyzeModel.getDefaultAttribute is only called if id_product_attribute is missing from the SQL query at the origin of it:
        // consider adding it in order to avoid unnecessary queries
        $row['allow_oosp'] = JeproLabAnalyzeModel.isAvailableWhenOutOfStock($row['out_of_stock']);
        if (Combination::isFeatureActive() && $id_product_attribute === null
        && ((isset($row['cache_default_attribute']) && ($ipa_default = $row['cache_default_attribute']) !== null)
        || ($ipa_default = JeproLabAnalyzeModel.getDefaultAttribute($row['id_product'], !$row['allow_oosp'])))) {
        $id_product_attribute = $row['id_product_attribute'] = $ipa_default;
        }
        if (!Combination::isFeatureActive() || !isset($row['id_product_attribute'])) {
        $id_product_attribute = $row['id_product_attribute'] = 0;
        }

        // Tax
        $usetax = Tax::excludeTaxeOption();

        $cache_key = $row['id_product']."_".$id_product_attribute."_".$id_lang."_".(int)$usetax;
        if (isset($row['id_product_pack'])) {
        $cache_key .= '-pack'.$row['id_product_pack'];
        }

        if (isset(JeproLabAnalyzeModel.$producPropertiesCache[$cache_key])) {
        return array_merge($row, JeproLabAnalyzeModel.$producPropertiesCache[$cache_key]);
        }

        // Datas
        $row['category'] = Category::getLinkRewrite((int)$row['id_category_default'], (int)$id_lang);
        $row['link'] = context.link->getProductLink((int)$row['id_product'], $row['link_rewrite'], $row['category'], $row['ean13']);

        $row['attribute_price'] = 0;
        if ($id_product_attribute) {
        $row['attribute_price'] = (float)JeproLabAnalyzeModel.getProductAttributePrice($id_product_attribute);
        }

        $row['price_tax_exc'] = JeproLabAnalyzeModel.getPriceStatic(
        (int)$row['id_product'],
        false,
        $id_product_attribute,
        (JeproLabAnalyzeModel.$_taxCalculationMethod == PS_TAX_EXC ? 2 : 6)
        );

        if (JeproLabAnalyzeModel.$_taxCalculationMethod == PS_TAX_EXC) {
        $row['price_tax_exc'] = Tools::ps_round($row['price_tax_exc'], 2);
        $row['price'] = JeproLabAnalyzeModel.getPriceStatic(
        (int)$row['id_product'],
        true,
        $id_product_attribute,
        6
        );
        $row['price_without_reduction'] = JeproLabAnalyzeModel.getPriceStatic(
        (int)$row['id_product'],
        false,
        $id_product_attribute,
        2,
        null,
        false,
        false
        );
        } else {
        $row['price'] = Tools::ps_round(
        JeproLabAnalyzeModel.getPriceStatic(
        (int)$row['id_product'],
        true,
        $id_product_attribute,
        6
        ),
        (int)JeproLabSettingModel.get('PS_PRICE_DISPLAY_PRECISION')
        );
        $row['price_without_reduction'] = JeproLabAnalyzeModel.getPriceStatic(
        (int)$row['id_product'],
        true,
        $id_product_attribute,
        6,
        null,
        false,
        false
        );
        }

        $row['reduction'] = JeproLabAnalyzeModel.getPriceStatic(
        (int)$row['id_product'],
        (bool)$usetax,
        $id_product_attribute,
        6,
        null,
        true,
        true,
        1,
        true,
        null,
        null,
        null,
        $specific_prices
        );

        $row['specific_prices'] = $specific_prices;

        $row['quantity'] = JeproLabAnalyzeModel.getQuantity(
        (int)$row['id_product'],
        0,
        isset($row['cache_is_pack']) ? $row['cache_is_pack'] : null
        );

        $row['quantity_all_versions'] = $row['quantity'];

        if ($row['id_product_attribute']) {
        $row['quantity'] = JeproLabAnalyzeModel.getQuantity(
        (int)$row['id_product'],
        $id_product_attribute,
        isset($row['cache_is_pack']) ? $row['cache_is_pack'] : null
        );
        }

        $row['id_image'] = JeproLabAnalyzeModel.defineProductImage($row, $id_lang);
        $row['features'] = JeproLabAnalyzeModel.getFrontFeaturesStatic((int)$id_lang, $row['id_product']);

        $row['attachments'] = array();
        if (!isset($row['cache_has_attachments']) || $row['cache_has_attachments']) {
        $row['attachments'] = JeproLabAnalyzeModel.getAttachmentsStatic((int)$id_lang, $row['id_product']);
        }

        $row['virtual'] = ((!isset($row['is_virtual']) || $row['is_virtual']) ? 1 : 0);

        // Pack management
        $row['pack'] = (!isset($row['cache_is_pack']) ? JeproLabAnalyzePackModel.isPack($row['id_product']) : (int)$row['cache_is_pack']);
        $row['packItems'] = $row['pack'] ? JeproLabAnalyzePackModel.getItemTable($row['id_product'], $id_lang) : array();
        $row['nopackprice'] = $row['pack'] ? JeproLabAnalyzePackModel.noPackPrice($row['id_product']) : 0;
        if ($row['pack'] && !JeproLabAnalyzePackModel.isInStock($row['id_product'])) {
        $row['quantity'] = 0;
        }

        $row['customization_required'] = false;
        if (isset($row['customizable']) && $row['customizable'] && Customization::isFeatureActive()) {
        if (count(JeproLabAnalyzeModel.getRequiredCustomizableFieldsStatic((int)$row['id_product']))) {
        $row['customization_required'] = true;
        }
        }

        $row = JeproLabAnalyzeModel.getTaxesInformations($row, context);
        JeproLabAnalyzeModel.$producPropertiesCache[$cache_key] = $row;
        return JeproLabAnalyzeModel.$producPropertiesCache[$cache_key];
        }

public static function getTaxesInformations($row, Context context = null)
        {
static $address = null;

        if (context === null) {
        context = JeproLabContext.getContext();
        }
        if ($address === null) {
        $address = new Address();
        }

        $address->id_country = (int)context.country->id;
        $address->id_state = 0;
        $address->postcode = 0;

        $tax_manager = TaxManagerFactory::getManager($address, JeproLabAnalyzeModel.getIdTaxRulesGroupByIdProduct((int)$row['id_product'], context));
        $row['rate'] = $tax_manager->getTaxCalculator()->getTotalRate();
        $row['tax_name'] = $tax_manager->getTaxCalculator()->getTaxesName();

        return $row;
        }
* /
    public static List getAnalyzesProperties(int langId, List queryResult) {
        List resultsArray = new ArrayList<>();

        if (queryResult.size() > 0) {
            foreach($query_result as $row) {
                if ($row2 = JeproLabAnalyzeModel.getProductProperties(langId, $row)){
                    $results_array[]=$row2;
                }
            }
        }
        return resultsArray;
    }

   /**
    * Select all features for a given language
    *
    * @param langId Language id
    * @return array Array with feature's data
    * /
    public static function getFrontFeaturesStatic(int langId, int productId){
        if (!Feature::isFeatureActive()) {
        return array();
        }
        if (!array_key_exists($id_product."_".$id_lang, JeproLabAnalyzeModel.$_frontFeaturesCache)) {
        JeproLabAnalyzeModel.$_frontFeaturesCache[$id_product."_".$id_lang] = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
        SELECT name, value, pf.id_feature
        FROM '.staticDataBaseObject.quoteName("#__jeprolab.'feature_product pf
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'feature_lang fl ON (fl.id_feature = pf.id_feature AND fl.id_lang = '.(int)$id_lang.')
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'feature_value_lang fvl ON (fvl.id_feature_value = pf.id_feature_value AND fvl.id_lang = '.(int)$id_lang.')
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'feature f ON (f.id_feature = pf.id_feature AND fl.id_lang = '.(int)$id_lang.')
        '.JeproLabLaboratoryModel.addSqlAssociation('feature', 'f').'
        WHERE pf.id_product = '.(int)$id_product.'
        ORDER BY f.position ASC'
        );
        }
        return JeproLabAnalyzeModel.$_frontFeaturesCache[$id_product."_".$id_lang];
        }
/*
public function getFrontFeatures($id_lang)
        {
        return JeproLabAnalyzeModel.getFrontFeaturesStatic($id_lang, this.id);
        }

public static function getAttachmentsStatic($id_lang, $id_product)
        {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
        SELECT *
        FROM '.staticDataBaseObject.quoteName("#__jeprolab.'product_attachment pa
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'attachment a ON a.id_attachment = pa.id_attachment
        LEFT JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'attachment_lang al ON (a.id_attachment = al.id_attachment AND al.id_lang = '.(int)$id_lang.')
        WHERE pa.id_product = '.(int)$id_product);
        }

public function getAttachments($id_lang)
        {
        return JeproLabAnalyzeModel.getAttachmentsStatic($id_lang, this.id);
        }

    /*
    ** Customization management
    * /

public static function getAllCustomizedDatas($id_cart, $id_lang = null, $only_in_cart = true, $id_shop = null)
        {
        if (!Customization::isFeatureActive()) {
        return false;
        }

        // No need to query if there isn't any real cart!
        if (!$id_cart) {
        return false;
        }
        if (!$id_lang) {
        $id_lang = JeproLabContext.getContext()->language->id;
        }
        if (JeproLabLaboratoryModel.isFeatureActive() && !$id_shop) {
        $id_shop = (int)JeproLabContext.getContext()->shop->id;
        }


        if (!$result = Db::getInstance()->executeS('
        SELECT cd.`id_customization`, c.`id_address_delivery`, c.`id_product`, cfl.`id_customization_field`, c.`id_product_attribute`,
        cd.`type`, cd.`index`, cd.`value`, cfl.`name`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customized_data` cd
        NATURAL JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customization` c
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang` cfl ON (cfl.id_customization_field = cd.`index` AND id_lang = '.(int)$id_lang.
        ($id_shop ? ' AND cfl.`id_shop` = '.$id_shop : '').')
        WHERE c.`id_cart` = '.(int)$id_cart.
        ($only_in_cart ? ' AND c.`in_cart` = 1' : '').'
        ORDER BY `id_product`, `id_product_attribute`, `type`, `index`')) {
        return false;
        }

        $customized_datas = array();

        foreach ($result as $row) {
        $customized_datas[(int)$row['id_product']][(int)$row['id_product_attribute']][(int)$row['id_address_delivery']][(int)$row['id_customization']]['datas'][(int)$row['type']][] = $row;
        }

        if (!$result = Db::getInstance()->executeS(
        'SELECT `id_product`, `id_product_attribute`, `id_customization`, `id_address_delivery`, `quantity`, `quantity_refunded`, `quantity_returned`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization`
        WHERE `id_cart` = '.(int)$id_cart.($only_in_cart ? '
        AND `in_cart` = 1' : ''))) {
        return false;
        }

        foreach ($result as $row) {
        $customized_datas[(int)$row['id_product']][(int)$row['id_product_attribute']][(int)$row['id_address_delivery']][(int)$row['id_customization']]['quantity'] = (int)$row['quantity'];
        $customized_datas[(int)$row['id_product']][(int)$row['id_product_attribute']][(int)$row['id_address_delivery']][(int)$row['id_customization']]['quantity_refunded'] = (int)$row['quantity_refunded'];
        $customized_datas[(int)$row['id_product']][(int)$row['id_product_attribute']][(int)$row['id_address_delivery']][(int)$row['id_customization']]['quantity_returned'] = (int)$row['quantity_returned'];
        }

        return $customized_datas;
        }

public static function addCustomizationPrice(&$products, &$customized_datas)
        {
        if (!$customized_datas) {
        return;
        }

        foreach ($products as &$product_update) {
        if (!Customization::isFeatureActive()) {
        $product_update['customizationQuantityTotal'] = 0;
        $product_update['customizationQuantityRefunded'] = 0;
        $product_update['customizationQuantityReturned'] = 0;
        } else {
        $customization_quantity = 0;
        $customization_quantity_refunded = 0;
        $customization_quantity_returned = 0;

                /* Compatibility * /
        $product_id = isset($product_update['id_product']) ? (int)$product_update['id_product'] : (int)$product_update['product_id'];
        $product_attribute_id = isset($product_update['id_product_attribute']) ? (int)$product_update['id_product_attribute'] : (int)$product_update['product_attribute_id'];
        $id_address_delivery = (int)$product_update['id_address_delivery'];
        $product_quantity = isset($product_update['cart_quantity']) ? (int)$product_update['cart_quantity'] : (int)$product_update['product_quantity'];
        $price = isset($product_update['price']) ? $product_update['price'] : $product_update['product_price'];
        if (isset($product_update['price_wt']) && $product_update['price_wt']) {
        $price_wt = $product_update['price_wt'];
        } else {
        $price_wt = $price * (1 + ((isset($product_update['tax_rate']) ? $product_update['tax_rate'] : $product_update['rate']) * 0.01));
        }

        if (!isset($customized_datas[$product_id][$product_attribute_id][$id_address_delivery])) {
        $id_address_delivery = 0;
        }
        if (isset($customized_datas[$product_id][$product_attribute_id][$id_address_delivery])) {
        foreach ($customized_datas[$product_id][$product_attribute_id][$id_address_delivery] as $customization) {
        $customization_quantity += (int)$customization['quantity'];
        $customization_quantity_refunded += (int)$customization['quantity_refunded'];
        $customization_quantity_returned += (int)$customization['quantity_returned'];
        }
        }

        $product_update['customizationQuantityTotal'] = $customization_quantity;
        $product_update['customizationQuantityRefunded'] = $customization_quantity_refunded;
        $product_update['customizationQuantityReturned'] = $customization_quantity_returned;

        if ($customization_quantity) {
        $product_update['total_wt'] = $price_wt * ($product_quantity - $customization_quantity);
        $product_update['total_customization_wt'] = $price_wt * $customization_quantity;
        $product_update['total'] = $price * ($product_quantity - $customization_quantity);
        $product_update['total_customization'] = $price * $customization_quantity;
        }
        }
        }
        }

    /*
    ** Customization fields' label management
    * /

protected function _checkLabelField($field, $value)
        {
        if (!Validate::isLabel($value)) {
        return false;
        }
        $tmp = explode('_', $field);
        if (count($tmp) < 4) {
        return false;
        }
        return $tmp;
        }

protected function _deleteOldLabels()
        {
        $max = array(
        JeproLabAnalyzeModel.CUSTOMIZE_FILE => (int)this.uploadable_files,
        JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD => (int)this.text_fields
        );

        /* Get customization field ids * /
        if (($result = Db::getInstance()->executeS(
        'SELECT `id_customization_field`, `type`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        WHERE `id_product` = '.(int)this.id.'
        ORDER BY `id_customization_field`')
        ) === false) {
        return false;
        }

        if (empty($result)) {
        return true;
        }

        $customization_fields = array(
        JeproLabAnalyzeModel.CUSTOMIZE_FILE => array(),
        JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD => array()
        );

        foreach ($result as $row) {
        $customization_fields[(int)$row['type']][] = (int)$row['id_customization_field'];
        }

        $extra_file = count($customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_FILE]) - $max[JeproLabAnalyzeModel.CUSTOMIZE_FILE];
        $extra_text = count($customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD]) - $max[JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD];

        /* If too much inside the database, deletion * /
        if ($extra_file > 0 && count($customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_FILE]) - $extra_file >= 0 &&
        (!Db::getInstance()->execute(
        'DELETE `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`,`'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field` JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        WHERE `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`id_product` = '.(int)this.id.'
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`type` = '.JeproLabAnalyzeModel.CUSTOMIZE_FILE.'
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`.`id_customization_field` = `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`id_customization_field`
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`id_customization_field` >= '.(int)$customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_FILE][count($customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_FILE]) - $extra_file]
        ))) {
        return false;
        }

        if ($extra_text > 0 && count($customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD]) - $extra_text >= 0 &&
        (!Db::getInstance()->execute(
        'DELETE `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`,`'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field` JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        WHERE `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`id_product` = '.(int)this.id.'
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`type` = '.JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD.'
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`.`id_customization_field` = `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`id_customization_field`
        AND `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`.`id_customization_field` >= '.(int)$customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD][count($customization_fields[JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD]) - $extra_text]
        ))) {
        return false;
        }

        // Refresh cache of feature detachable
        JeproLabSettingModel.updateGlobalValue('PS_CUSTOMIZATION_FEATURE_ACTIVE', Customization::isCurrentlyUsed());

        return true;
        }

protected function _createLabel($languages, $type)
        {
        // Label insertion
        if (!Db::getInstance()->execute('
        INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field` (`id_product`, `type`, `required`)
        VALUES ('.(int)this.id.', '.(int)$type.', 0)') ||
        !$id_customization_field = (int)Db::getInstance()->Insert_ID()) {
        return false;
        }

        // Multilingual label name creation
        $values = '';

        foreach ($languages as $language) {
        foreach (JeproLabLaboratoryModel.getContextListShopID() as $id_shop) {
        $values .= '('.(int)$id_customization_field.', '.(int)$language['id_lang'].', '.$id_shop .',\'\'), ';
        }
        }

        $values = rtrim($values, ', ');
        if (!Db::getInstance()->execute('
        INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang` (`id_customization_field`, `id_lang`, `id_shop`, `name`)
        VALUES '.$values)) {
        return false;
        }

        // Set cache of feature detachable to true
        JeproLabSettingModel.updateGlobalValue('PS_CUSTOMIZATION_FEATURE_ACTIVE', '1');

        return true;
        }

public function createLabels($uploadable_files, $text_fields)
        {
        $languages = Language::getLanguages();
        if ((int)$uploadable_files > 0) {
        for ($i = 0; $i < (int)$uploadable_files; $i++) {
        if (!this._createLabel($languages, JeproLabAnalyzeModel.CUSTOMIZE_FILE)) {
        return false;
        }
        }
        }

        if ((int)$text_fields > 0) {
        for ($i = 0; $i < (int)$text_fields; $i++) {
        if (!this._createLabel($languages, JeproLabAnalyzeModel.CUSTOMIZE_TEXTFIELD)) {
        return false;
        }
        }
        }

        return true;
        }

public function updateLabels()
        {
        $has_required_fields = 0;
        foreach ($_POST as $field => $value) {
            /* Label update * /
        if (strncmp($field, 'label_', 6) == 0) {
        if (!$tmp = this._checkLabelField($field, $value)) {
        return false;
        }
                /* Multilingual label name update * /
        if (JeproLabLaboratoryModel.isFeatureActive()) {
        foreach (JeproLabLaboratoryModel.getContextListShopID() as $id_shop) {
        if (!Db::getInstance()->execute('INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        (`id_customization_field`, `id_lang`, `id_shop`, `name`) VALUES ('.(int)$tmp[2].', '.(int)$tmp[3].', '.$id_shop.', \''.pSQL($value).'\')
        ON DUPLICATE KEY UPDATE `name` = \''.pSQL($value).'\'')) {
        return false;
        }
        }
        } elseif (!Db::getInstance()->execute('
        INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang`
        (`id_customization_field`, `id_lang`, `name`) VALUES ('.(int)$tmp[2].', '.(int)$tmp[3].', \''.pSQL($value).'\')
        ON DUPLICATE KEY UPDATE `name` = \''.pSQL($value).'\'')) {
        return false;
        }

        $is_required = isset($_POST['require_'.(int)$tmp[1].'_'.(int)$tmp[2]]) ? 1 : 0;
        $has_required_fields |= $is_required;
                /* Require option update * /
        if (!Db::getInstance()->execute(
        'UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        SET `required` = '.(int)$is_required.'
        WHERE `id_customization_field` = '.(int)$tmp[2])) {
        return false;
        }
        }
        }

        if ($has_required_fields && !ObjectModel::updateMultishopTable('product', array('customizable' => 2), 'a.id_product = '.(int)this.id)) {
        return false;
        }

        if (!this._deleteOldLabels()) {
        return false;
        }

        return true;
        }

public function getCustomizationFields($id_lang = false, $id_shop = null)
        {
        if (!Customization::isFeatureActive()) {
        return false;
        }

        if (JeproLabLaboratoryModel.isFeatureActive() && !$id_shop) {
        $id_shop = (int)JeproLabContext.getContext()->shop->id;
        }

        if (!$result = Db::getInstance()->executeS('
        SELECT cf.`id_customization_field`, cf.`type`, cf.`required`, cfl.`name`, cfl.`id_lang`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field` cf
        NATURAL JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field_lang` cfl
        WHERE cf.`id_product` = '.(int)this.id.($id_lang ? ' AND cfl.`id_lang` = '.(int)$id_lang : '').
        ($id_shop ? ' AND cfl.`id_shop` = '.$id_shop : '').'
        ORDER BY cf.`id_customization_field`')) {
        return false;
        }

        if ($id_lang) {
        return $result;
        }

        $customization_fields = array();
        foreach ($result as $row) {
        $customization_fields[(int)$row['type']][(int)$row['id_customization_field']][(int)$row['id_lang']] = $row;
        }

        return $customization_fields;
        }

public function getCustomizationFieldIds()
        {
        if (!Customization::isFeatureActive()) {
        return array();
        }
        return Db::getInstance()->executeS('
        SELECT `id_customization_field`, `type`, `required`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        WHERE `id_product` = '.(int)this.id);
        }

public function getRequiredCustomizableFields()
        {
        if (!Customization::isFeatureActive()) {
        return array();
        }
        return JeproLabAnalyzeModel.getRequiredCustomizableFieldsStatic(this.id);
        }

public static function getRequiredCustomizableFieldsStatic($id)
        {
        if (!$id || !Customization::isFeatureActive()) {
        return array();
        }
        return Db::getInstance()->executeS('
        SELECT `id_customization_field`, `type`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'customization_field`
        WHERE `id_product` = '.(int)$id.'
        AND `required` = 1'
        );
        }

public function hasAllRequiredCustomizableFields(Context context = null)
        {
        if (!Customization::isFeatureActive()) {
        return true;
        }
        if (!context) {
        context = JeproLabContext.getContext();
        }

        $fields = context.cart->getProductCustomization(this.id, null, true);
        if (($required_fields = this.getRequiredCustomizableFields()) === false) {
        return false;
        }

        $fields_present = array();
        foreach ($fields as $field) {
        $fields_present[] = array('id_customization_field' => $field['index'], 'type' => $field['type']);
        }

        if (is_array($required_fields) && count($required_fields)) {
        foreach ($required_fields as $required_field) {
        if (!in_array($required_field, $fields_present)) {
        return false;
        }
        }
        }
        return true;
        }


/**
 * Checks if the product is in at least one of the submited categories
 *
 * @param int $id_product
 * @param array $categories array of category arrays
 * @return bool is the product in at least one category
 * /
public static function idIsOnCategoryId($id_product, $categories)
        {
        if (!((int)$id_product > 0) || !is_array($categories) || empty($categories)) {
        return false;
        }
        $sql = 'SELECT id_product FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` WHERE `id_product` = '.(int)$id_product.' AND `id_category` IN (';
        foreach ($categories as $category) {
        $sql .= (int)$category['id_category'].',';
        }
        $sql = rtrim($sql, ',').')';

        $hash = md5($sql);
        if (!isset(JeproLabAnalyzeModel.$_incat[$hash])) {
        if (!Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql)) {
        return false;
        }
        JeproLabAnalyzeModel.$_incat[$hash] = (Db::getInstance(_PS_USE_SQL_SLAVE_)->NumRows() > 0 ? true : false);
        }
        return JeproLabAnalyzeModel.$_incat[$hash];
        }

public function getNoPackPrice()
        {
        return JeproLabAnalyzePackModel.noPackPrice((int)this.id);
        }

public function checkAccess($id_customer)
        {
        return JeproLabAnalyzeModel.checkAccessStatic((int)this.id, (int)$id_customer);
        }

public static function checkAccessStatic($id_product, $id_customer)
        {
        if (!Group::isFeatureActive()) {
        return true;
        }

        $cacheKey = 'JeproLabAnalyzeModel.checkAccess_'.(int)$id_product."_".(int)$id_customer.(!$id_customer ? "_".(int)Group::getCurrent()->id : '');
        if (!Cache::isStored($cacheKey)) {
        if (!$id_customer) {
        $result = (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
        SELECT ctg.`id_group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        INNER JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_group` ctg ON (ctg.`id_category` = cp.`id_category`)
        WHERE cp.`id_product` = '.(int)$id_product.' AND ctg.`id_group` = '.(int)Group::getCurrent()->id);
        } else {
        $result = (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
        SELECT cg.`id_group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        INNER JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_group` ctg ON (ctg.`id_category` = cp.`id_category`)
        INNER JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'customer_group` cg ON (cg.`id_group` = ctg.`id_group`)
        WHERE cp.`id_product` = '.(int)$id_product.' AND cg.`id_customer` = '.(int)$id_customer);
        }

        Cache::store($cacheKey, $result);
        return $result;
        }
        return Cache::retrieve($cacheKey);
        }

/**
 * Add a stock movement for current product
 *
 * Since 1.5, this method only permit to add/remove available quantities of the current product in the current shop
 *
 * @see StockManager if you want to manage real stock
 * @see StockAvailable if you want to manage available quantities for sale on your shop(s)
 *
 * @deprecated since 1.5.0
 *
 * @param int $quantity
 * @param int $id_reason - useless
 * @param int $id_product_attribute
 * @param int $id_order - DEPRECATED
 * @param int $id_employee - DEPRECATED
 * @return bool
 * /
public function addStockMvt($quantity, $id_reason, $id_product_attribute = null, $id_order = null, $id_employee = null)
        {
        if (!this.id || !$id_reason) {
        return false;
        }

        if ($id_product_attribute == null) {
        $id_product_attribute = 0;
        }

        $reason = new StockMvtReason((int)$id_reason);
        if (!Validate::isLoadedObject($reason)) {
        return false;
        }

        $quantity = abs((int)$quantity) * $reason->sign;

        return StockAvailable::updateQuantity(this.id, $id_product_attribute, $quantity);
        }

/**
 * @deprecated since 1.5.0
 * /
public function getStockMvts($id_lang)
        {
        Tools::displayAsDeprecated();

        return Db::getInstance()->executeS('
        SELECT sm.id_stock_mvt, sm.date_add, sm.quantity, sm.id_order,
        CONCAT(pl.name, \' \', GROUP_CONCAT(IFNULL(al.name, \'\'), \'\')) product_name, CONCAT(e.lastname, \' \', e.firstname) employee, mrl.name reason
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'stock_mvt` sm
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (
        sm.id_product = pl.id_product
        AND pl.id_lang = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').'
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'stock_mvt_reason_lang` mrl ON (
        sm.id_stock_mvt_reason = mrl.id_stock_mvt_reason
        AND mrl.id_lang = '.(int)$id_lang.'
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'employee` e ON (
        e.id_employee = sm.id_employee
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac ON (
        pac.id_product_attribute = sm.id_product_attribute
        )
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al ON (
        al.id_attribute = pac.id_attribute
        AND al.id_lang = '.(int)$id_lang.'
        )
        WHERE sm.id_product='.(int)this.id.'
        GROUP BY sm.id_stock_mvt
        ');
        }

public static function getUrlRewriteInformations($id_product)
        {
        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
        SELECT pl.`id_lang`, pl.`link_rewrite`, p.`ean13`, cl.`link_rewrite` AS category_rewrite
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_lang` pl ON (p.`id_product` = pl.`id_product`'.JeproLabLaboratoryModel.addSqlRestrictionOnLang('pl').')
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'lang` l ON (pl.`id_lang` = l.`id_lang`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category_lang` cl ON (cl.`id_category` = product_shop.`id_category_default`  AND cl.`id_lang` = pl.`id_lang`'.JeproLabLaboratoryModel.addSqlRestrictionOnLang('cl').')
        WHERE p.`id_product` = '.(int)$id_product.'
        AND l.`active` = 1
        ');
        }
*/
    public int getTaxRulesGroupId(){
        return this.tax_rules_group_id;
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

    public float getTaxesRate(){
        return getTaxesRate(null);
    }
    /**
     * Returns JeproLabTaxManagerFactory rate.
     *
     * @param address
     * @return float The total taxes rate applied to the product
     */
    public float getTaxesRate(JeproLabAddressModel address) {
        if (address != null && address.country_id <= 0) {
            address = JeproLabAddressModel.initialize();
        }

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, this.tax_rules_group_id);
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }

/*
 * Webservice getter : get product features association
 *
 * @return array
 * /
public function getWsProductFeatures()
        {
        $rows = this.getFeatures();
        foreach ($rows as $keyrow => $row) {
        foreach ($row as $keyfeature => $feature) {
        if ($keyfeature == 'id_feature') {
        $rows[$keyrow]['id'] = $feature;
        unset($rows[$keyrow]['id_feature']);
        }
        unset($rows[$keyrow]['id_product']);
        unset($rows[$keyrow]['custom']);
        }
        asort($rows[$keyrow]);
        }
        return $rows;
        }

/**
 * Webservice setter : set product features association
 *
 * @param $product_features Product Feature ids
 * @return bool
 * /
public function setWsProductFeatures($product_features)
        {
        Db::getInstance()->execute('
        DELETE FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'feature_product`
        WHERE `id_product` = '.(int)this.id
        );
        foreach ($product_features as $product_feature) {
        this.addFeaturesToDB($product_feature['id'], $product_feature['id_feature_value']);
        }
        return true;
        }

/**
 * Webservice getter : get virtual field default combination
 *
 * @return int
 * /
public function getWsDefaultCombination()
        {
        return JeproLabAnalyzeModel.getDefaultAttribute(this.id);
        }

/**
 * Webservice setter : set virtual field default combination
 *
 * @param int $id_combination id default combination
 * @return bool
 * /
public function setWsDefaultCombination($id_combination)
        {
        this.deleteDefaultAttributes();
        return this.setDefaultAttribute((int)$id_combination);
        }

/**
 * Webservice getter : get category ids of current product for association
 *
 * @return array
 * /
public function getWsCategories()
        {
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS(
        'SELECT cp.`id_category` AS id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` cp
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'category` c ON (c.id_category = cp.id_category)
        '.JeproLabLaboratoryModel.addSqlAssociation('category', 'c').'
        WHERE cp.`id_product` = '.(int)this.id
        );
        return $result;
        }

/**
 * Webservice setter : set category ids of current product for association
 *
 * @param array $category_ids category ids
 * @return bool
 * /
public function setWsCategories($category_ids)
        {
        $ids = array();
        foreach ($category_ids as $value) {
        $ids[] = $value['id'];
        }
        if (this.deleteCategories()) {
        if ($ids) {
        $sql_values = '';
        $ids = array_map('intval', $ids);
        foreach ($ids as $position => $id) {
        $sql_values[] = '('.(int)$id.', '.(int)this.id.', '.(int)$position.')';
        }
        $result = Db::getInstance()->execute('
        INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product` (`id_category`, `id_product`, `position`)
        VALUES '.implode(',', $sql_values)
        );
        Hook::exec('updateProduct', array('id_product' => (int)this.id));
        return $result;
        }
        }
        Hook::exec('updateProduct', array('id_product' => (int)this.id));
        return true;
        }

/**
 * Webservice getter : get product accessories ids of current product for association
 *
 * @return array
 * /
public function getWsAccessories()
        {
        $result = Db::getInstance()->executeS(
        'SELECT p.`id_product` AS id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'accessory` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p ON (p.id_product = a.id_product_2)
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        WHERE a.`id_product_1` = '.(int)this.id
        );

        return $result;
        }

/**
 * Webservice setter : set product accessories ids of current product for association
 *
 * @param $accessories product ids
 * /
public function setWsAccessories($accessories)
        {
        this.deleteAccessories();
        foreach ($accessories as $accessory) {
        Db::getInstance()->execute('INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'accessory` (`id_product_1`, `id_product_2`) VALUES ('.(int)this.id.', '.(int)$accessory['id'].')');
        }

        return true;
        }

/**
 * Webservice getter : get combination ids of current product for association
 *
 * @return array
 * /
public function getWsCombinations()
        {
        $result = Db::getInstance()->executeS(
        'SELECT pa.`id_product_attribute` as id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)this.id
        );

        return $result;
        }

/**
 * Webservice setter : set combination ids of current product for association
 *
 * @param $combinations combination ids
 * /
public function setWsCombinations($combinations)
        {
        // No hook exec
        $ids_new = array();
        foreach ($combinations as $combination) {
        $ids_new[] = (int)$combination['id'];
        }

        $ids_orig = array();
        $original = Db::getInstance()->executeS(
        'SELECT pa.`id_product_attribute` as id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)this.id
        );

        if (is_array($original)) {
        foreach ($original as $id) {
        $ids_orig[] = $id['id'];
        }
        }

        $all_ids = array();
        $all = Db::getInstance()->executeS('SELECT pa.`id_product_attribute` as id FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa'));
        if (is_array($all)) {
        foreach ($all as $id) {
        $all_ids[] = $id['id'];
        }
        }

        $to_add = array();
        foreach ($ids_new as $id) {
        if (!in_array($id, $ids_orig)) {
        $to_add[] = $id;
        }
        }

        $to_delete = array();
        foreach ($ids_orig as $id) {
        if (!in_array($id, $ids_new)) {
        $to_delete[] = $id;
        }
        }

        // Delete rows
        if (count($to_delete) > 0) {
        foreach ($to_delete as $id) {
        $combination = new Combination($id);
        $combination->delete();
        }
        }

        foreach ($to_add as $id) {
        // Update id_product if exists else create
        if (in_array($id, $all_ids)) {
        Db::getInstance()->execute('UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` SET id_product = '.(int)this.id.' WHERE id_product_attribute='.$id);
        } else {
        Db::getInstance()->execute('INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` (`id_product`) VALUES ('.this.id.')');
        }
        }
        return true;
        }

/**
 * Webservice getter : get product option ids of current product for association
 *
 * @return array
 * /
public function getWsProductOptionValues()
        {
        $result = Db::getInstance()->executeS('SELECT DISTINCT pac.id_attribute as id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac ON (pac.id_product_attribute = pa.id_product_attribute)
        WHERE pa.id_product = '.(int)this.id);
        return $result;
        }

/**
 * Webservice getter : get virtual field position in category
 *
 * @return int
 * /
public function getWsPositionInCategory()
        {
        $result = Db::getInstance()->executeS('SELECT position
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
        WHERE id_category = '.(int)this.id_category_default.'
        AND id_product = '.(int)this.id);
        if (count($result) > 0) {
        return $result[0]['position'];
        }
        return '';
        }

/**
 * Webservice setter : set virtual field position in category
 *
 * @return bool
 * /
public function setWsPositionInCategory($position)
        {
        if ($position < 0) {
        WebserviceRequest::getInstance()->setError(500, Tools::displayError('You cannot set a negative position, the minimum for a position is 0.'), 134);
        }
        $result = Db::getInstance()->executeS('
        SELECT `id_product`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'category_product`
        WHERE `id_category` = '.(int)this.id_category_default.'
        ORDER BY `position`
        ');
        if (($position > 0) && ($position + 1 > count($result))) {
        WebserviceRequest::getInstance()->setError(500, Tools::displayError('You cannot set a position greater than the total number of products in the category, minus 1 (position numbering starts at 0).'), 135);
        }

        foreach ($result as &$value) {
        $value = $value['id_product'];
        }
        $current_position = this.getWsPositionInCategory();

        if ($current_position && isset($result[$current_position])) {
        $save = $result[$current_position];
        unset($result[$current_position]);
        array_splice($result, (int)$position, 0, $save);
        }

        foreach ($result as $position => $id_product) {
        Db::getInstance()->update('category_product', array(
        'position' => $position,
        ), '`id_category` = '.(int)this.id_category_default.' AND `id_product` = '.(int)$id_product);
        }
        return true;
        }

/**
 * Webservice getter : get virtual field id_default_image in category
 *
 * @return int
 * /
public function getCoverWs()
        {
        $result = this.getCover(this.id);
        return $result['id_image'];
        }

/**
 * Webservice setter : set virtual field id_default_image in category
 *
 * @return bool
 * /
public function setCoverWs($id_image)
        {
        Db::getInstance()->execute('UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'image_shop` image_shop, `'.staticDataBaseObject.quoteName("#__jeprolab.'image` i
        SET image_shop.`cover` = 0
        WHERE i.`id_product` = '.(int)this.id.' AND i.id_image = image_shop.id_image
        AND image_shop.id_shop='.(int)JeproLabContext.getContext()->shop->id);
        Db::getInstance()->execute('UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'image_shop`
        SET `cover` = 1 WHERE `id_image` = '.(int)$id_image);

        return true;
        }

/**
 * Webservice getter : get image ids of current product for association
 *
 * @return array
 * /
public function getWsImages()
        {
        return Db::getInstance()->executeS('
        SELECT i.`id_image` as id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'image` i
        '.JeproLabLaboratoryModel.addSqlAssociation('image', 'i').'
        WHERE i.`id_product` = '.(int)this.id.'
        ORDER BY i.`position`');
        }

public function getWsStockAvailables()
        {
        return Db::getInstance()->executeS('SELECT `id_stock_available` id, `id_product_attribute`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'stock_available`
        WHERE `id_product`='.(this.id).StockAvailable::addSqlShopRestriction());
        }

public function getWsTags()
        {
        return Db::getInstance()->executeS('
        SELECT `id_tag` as id
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_tag`
        WHERE `id_product` = '.(int)this.id);
        }

/**
 * Webservice setter : set tag ids of current product for association
 *
 * @param $tag_ids tag ids
 * /
public function setWsTags($tag_ids)
        {
        $ids = array();
        foreach ($tag_ids as $value) {
        $ids[] = $value['id'];
        }
        if (this.deleteWsTags()) {
        if ($ids) {
        $sql_values = '';
        $ids = array_map('intval', $ids);
        foreach ($ids as $position => $id) {
        $id_lang = Db::getInstance()->getValue('SELECT `id_lang` FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'tag` WHERE `id_tag`='.(int)$id);
        $sql_values[] = '('.(int)this.id.', '.(int)$id.', '.(int)$id_lang.')';
        }
        $result = Db::getInstance()->execute('
        INSERT INTO `'.staticDataBaseObject.quoteName("#__jeprolab.'product_tag` (`id_product`, `id_tag`, `id_lang`)
        VALUES '.implode(',', $sql_values)
        );
        return $result;
        }
        }
        return true;
        }

/**
 * Delete products tags entries without delete tags for webservice usage
 *
 * @return array Deletion result
 * /
public function deleteWsTags()
        {
        return Db::getInstance()->delete('product_tag', 'id_product = '.(int)this.id);
        }


public function getWsManufacturerName()
        {
        return Manufacturer::getNameById((int)this.id_manufacturer);
        }

public static function resetEcoTax()
        {
        return ObjectModel::updateMultishopTable('product', array(
        'ecotax' => 0,
        ));
        }

/**
 * Set Group reduction if needed
 * /
public function setGroupReduction()
        {
        return GroupReduction::setProductReduction(this.id);
        }

/**
 * Checks if reference exists
 * @return bool
 * /
public function existsRefInDatabase($reference)
        {
        $row = Db::getInstance()->getRow('
        SELECT `reference`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
        WHERE p.reference = "'.pSQL($reference).'"');

        return isset($row['reference']);
        }

/**
 * Get all product attributes ids
 *
 * @since 1.5.0
 * @param int $id_product the id of the product
 * @return array product attribute id list
 * /
public static function getProductAttributesIds($id_product, $shop_only = false)
        {
        return Db::getInstance()->executeS('
        SELECT pa.id_product_attribute
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa'.
        ($shop_only ? JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa') : '').'
        WHERE pa.`id_product` = '.(int)$id_product);
        }

/**
 * Get label by lang and value by lang too
 * @todo Remove existing module condition
 * @param int $id_product
 * @param int $product_attribute_id
 * @return array
 * /
public static function getAttributesParams($id_product, $id_product_attribute)
        {
        $id_lang = (int)JeproLabContext.getContext()->language->id;
        $id_shop = (int)JeproLabContext.getContext()->shop->id;
        $cacheKey = 'JeproLabAnalyzeModel.getAttributesParams_'.(int)$id_product."_".(int)$id_product_attribute."_".(int)$id_lang."_".(int)$id_shop;

        // if blocklayered module is installed we check if user has set custom attribute name
        if (Module::isInstalled('blocklayered') && Module::isEnabled('blocklayered')) {
        $nb_custom_values = Db::getInstance()->executeS('
        SELECT DISTINCT la.`id_attribute`, la.`url_name` as `name`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (a.`id_attribute` = pac.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'layered_indexable_attribute_lang_value` la
        ON (la.`id_attribute` = a.`id_attribute` AND la.`id_lang` = '.(int)$id_lang.')
        WHERE la.`url_name` IS NOT NULL AND la.`url_name` != \'\'
        AND pa.`id_product` = '.(int)$id_product.'
        AND pac.`id_product_attribute` = '.(int)$id_product_attribute);

        if (!empty($nb_custom_values)) {
        $tab_id_attribute = array();
        foreach ($nb_custom_values as $attribute) {
        $tab_id_attribute[] = $attribute['id_attribute'];

        $group = Db::getInstance()->executeS('
        SELECT a.`id_attribute`, g.`id_attribute_group`, g.`url_name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'layered_indexable_attribute_group_lang_value` g
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        ON (a.`id_attribute_group` = g.`id_attribute_group`)
        WHERE a.`id_attribute` = '.(int)$attribute['id_attribute'].'
        AND g.`id_lang` = '.(int)$id_lang.'
        AND g.`url_name` IS NOT NULL AND g.`url_name` != \'\'');
        if (empty($group)) {
        $group = Db::getInstance()->executeS('
        SELECT g.`id_attribute_group`, g.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` g
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        ON (a.`id_attribute_group` = g.`id_attribute_group`)
        WHERE a.`id_attribute` = '.(int)$attribute['id_attribute'].'
        AND g.`id_lang` = '.(int)$id_lang.'
        AND g.`name` IS NOT NULL');
        }
        $result[] = array_merge($attribute, $group[0]);
        }
        $values_not_custom = Db::getInstance()->executeS('
        SELECT DISTINCT a.`id_attribute`, a.`id_attribute_group`, al.`name`, agl.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al
        ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl
        ON (a.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (a.`id_attribute` = pac.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        WHERE pa.`id_product` = '.(int)$id_product.'
        AND pac.id_product_attribute = '.(int)$id_product_attribute.'
        AND a.`id_attribute` NOT IN('.implode(', ', $tab_id_attribute).')');
        return array_merge($values_not_custom, $result);
        }
        }

        if (!Cache::isStored($cacheKey)) {
        $result = Db::getInstance()->executeS('
        SELECT a.`id_attribute`, a.`id_attribute_group`, al.`name`, agl.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al
        ON (al.`id_attribute` = a.`id_attribute` AND al.`id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (pac.`id_attribute` = a.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pa.`id_product_attribute` = pac.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl
        ON (a.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)$id_lang.')
        WHERE pa.`id_product` = '.(int)$id_product.'
        AND pac.`id_product_attribute` = '.(int)$id_product_attribute.'
        AND agl.`id_lang` = '.(int)$id_lang);
        Cache::store($cacheKey, $result);
        } else {
        $result = Cache::retrieve($cacheKey);
        }
        return $result;
        }

/**
 * @todo Remove existing module condition
 * @param int $id_product
 * /
public static function getAttributesInformationsByProduct($id_product)
        {
        // if blocklayered module is installed we check if user has set custom attribute name
        if (Module::isInstalled('blocklayered') && Module::isEnabled('blocklayered')) {
        $nb_custom_values = Db::getInstance()->executeS('
        SELECT DISTINCT la.`id_attribute`, la.`url_name` as `attribute`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (a.`id_attribute` = pac.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'layered_indexable_attribute_lang_value` la
        ON (la.`id_attribute` = a.`id_attribute` AND la.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        WHERE la.`url_name` IS NOT NULL AND la.`url_name` != \'\'
        AND pa.`id_product` = '.(int)$id_product);

        if (!empty($nb_custom_values)) {
        $tab_id_attribute = array();
        foreach ($nb_custom_values as $attribute) {
        $tab_id_attribute[] = $attribute['id_attribute'];

        $group = Db::getInstance()->executeS('
        SELECT g.`id_attribute_group`, g.`url_name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'layered_indexable_attribute_group_lang_value` g
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        ON (a.`id_attribute_group` = g.`id_attribute_group`)
        WHERE a.`id_attribute` = '.(int)$attribute['id_attribute'].'
        AND g.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.'
        AND g.`url_name` IS NOT NULL AND g.`url_name` != \'\'');
        if (empty($group)) {
        $group = Db::getInstance()->executeS('
        SELECT g.`id_attribute_group`, g.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` g
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        ON (a.`id_attribute_group` = g.`id_attribute_group`)
        WHERE a.`id_attribute` = '.(int)$attribute['id_attribute'].'
        AND g.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.'
        AND g.`name` IS NOT NULL');
        }
        $result[] = array_merge($attribute, $group[0]);
        }
        $values_not_custom = Db::getInstance()->executeS('
        SELECT DISTINCT a.`id_attribute`, a.`id_attribute_group`, al.`name` as `attribute`, agl.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al
        ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl
        ON (a.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (a.`id_attribute` = pac.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        '.JeproLabLaboratoryModel.addSqlAssociation('attribute', 'pac').'
        WHERE pa.`id_product` = '.(int)$id_product.'
        AND a.`id_attribute` NOT IN('.implode(', ', $tab_id_attribute).')');
        $result = array_merge($values_not_custom, $result);
        } else {
        $result = Db::getInstance()->executeS('
        SELECT DISTINCT a.`id_attribute`, a.`id_attribute_group`, al.`name` as `attribute`, agl.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al
        ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl
        ON (a.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (a.`id_attribute` = pac.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        '.JeproLabLaboratoryModel.addSqlAssociation('attribute', 'pac').'
        WHERE pa.`id_product` = '.(int)$id_product);
        }
        } else {
        $result = Db::getInstance()->executeS('
        SELECT DISTINCT a.`id_attribute`, a.`id_attribute_group`, al.`name` as `attribute`, agl.`name` as `group`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute` a
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_lang` al
        ON (a.`id_attribute` = al.`id_attribute` AND al.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'attribute_group_lang` agl
        ON (a.`id_attribute_group` = agl.`id_attribute_group` AND agl.`id_lang` = '.(int)JeproLabContext.getContext()->language->id.')
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination` pac
        ON (a.`id_attribute` = pac.`id_attribute`)
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        ON (pac.`id_product_attribute` = pa.`id_product_attribute`)
        '.JeproLabLaboratoryModel.addSqlAssociation('product_attribute', 'pa').'
        '.JeproLabLaboratoryModel.addSqlAssociation('attribute', 'pac').'
        WHERE pa.`id_product` = '.(int)$id_product);
        }
        return $result;
        }

/**
 * Get the combination url anchor of the product
 *
 * @param int $id_product_attribute
 * @return string
 * /
public function getAnchor($id_product_attribute, $with_id = false)
        {
        $attributes = JeproLabAnalyzeModel.getAttributesParams(this.id, $id_product_attribute);
        $anchor = '#';
        $sep = JeproLabSettingModel.get('PS_ATTRIBUTE_ANCHOR_SEPARATOR');
        foreach ($attributes as &$a) {
        foreach ($a as &$b) {
        $b = str_replace($sep, '_', Tools::link_rewrite($b));
        }
        $anchor .= '/'.($with_id && isset($a['id_attribute']) && $a['id_attribute']? (int)$a['id_attribute'].$sep : '').$a['group'].$sep.$a['name'];
        }
        return $anchor;
        }
*/

    public static String getAnalyzeName(int analyzeId){
        return getAnalyzeName(analyzeId, 0, 0);
    }

    public static String getAnalyzeName(int analyzeId, int analyzeAttributeId){
        return getAnalyzeName(analyzeId, analyzeAttributeId, 0);
    }
    /**
     * Gets the name of a given analyze, in the given lang
     *
     * @param analyzeId
     * @param analyzeAttributeId Optional
     * @param langId Optional
     * @return string
     */
    public static String getAnalyzeName(int analyzeId, int analyzeAttributeId,int langId) {
        // use the lang in the context if $id_lang is not defined
        if (langId <= 0) {
            langId = JeproLabContext.getContext().language.language_id;
        }
        String select = "";
        String from = "";
        String leftJoin = "";
        String innerJoin = "";
        String where = "";

        // selects different names, if it is a combination
        if (analyzeAttributeId > 0) {
            select = "SELECT IFNULL(CONCAT(analyze_lang.name, ' : ', GROUP_CONCAT(DISTINCT attribute_group_lang." + staticDataBaseObject.quoteName("name");
            select += ", ' - ', attribute_lang.name SEPARATOR ', ')), analyze_lang.name) as name ";
        } else {
            select = "SELECT DISTINCT analyze_lang.name as name ";
        }

        // adds joins & where clauses for combinations
        if (analyzeAttributeId > 0) {
            from = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute ";
            String join = JeproLabLaboratoryModel.addSqlAssociation ("analyze_attribute");
            innerJoin = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (analyze_lang.";
            innerJoin += staticDataBaseObject.quoteName("analyze_id") + " = analyze_attribute." + staticDataBaseObject.quoteName("analyze_id");
            innerJoin += " AND analyze_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("analyze_lang");
            leftJoin = join + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination") + " AS analyze_attribute_combination ";
            leftJoin += " ON (analyze_attribute_combination.analyze_attribute_id = attribute_analyze.analyze_attribute_id) LEFT JOIN ";
            leftJoin += staticDataBaseObject.quoteName("#__jeprolab_attribute") + " AS attribute ON (attribute.attribute_id = analyze_attribute_combination.";
            leftJoin += "attribute_id) LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_attribute_lang") + " AS attribute_lang ON (";
            leftJoin += "attribute_lang.attribute_id = attribute.attribute_id AND attribute_lang.lang_id = " + langId + ") LEFT JOIN ";
            leftJoin += staticDataBaseObject.quoteName("#__jeprolab_attribute_group_lang") + " AS attribute_group_lang ON(attribute_group_lang.";
            leftJoin += "attribute_group_id = attribute.attribute_group_id AND attribute_group_lang.lang_id = " + langId;
            where = " WHERE analyze_attribute.analyze_id = " + analyzeId + " AND analyze_attribute.analyze_attribute_id = " + analyzeAttributeId;
        } else {
            from = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang";
            where = " WHERE analyze_lang.analyze_id = " + analyzeId + " AND analyze_lang." + staticDataBaseObject.quoteName("lang_id") + " = ";
            where += langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("analyze_lang");
        }
        staticDataBaseObject.setQuery(select + from + leftJoin + innerJoin + where);
        return staticDataBaseObject.loadStringValue("name");
    }
/*
public function addWs($autodate = true, $null_values = false)
        {
        $success = this.add($autodate, $null_values);
        if ($success && JeproLabSettingModel.get('PS_SEARCH_INDEXATION')) {
        Search::indexation(false, this.id);
        }
        return $success;
        }

public function updateWs($null_values = false)
        {
        $success = parent::update($null_values);
        if ($success && JeproLabSettingModel.get('PS_SEARCH_INDEXATION')) {
        Search::indexation(false, this.id);
        }
        Hook::exec('updateProduct', array('id_product' => (int)this.id));
        return $success;
        }

/**
 * For a given product, returns its real quantity
 *
 * @since 1.5.0
 * @param int $id_product
 * @param int $id_product_attribute
 * @param int $id_warehouse
 * @param int $id_shop
 * @return int real_quantity
 * /
public static function getRealQuantity($id_product, $id_product_attribute = 0, $id_warehouse = 0, $id_shop = null)
        {
static $manager = null;

        if (JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT') && is_null($manager)) {
        $manager = StockManagerFactory::getManager();
        }

        if (JeproLabSettingModel.get('PS_ADVANCED_STOCK_MANAGEMENT') && JeproLabAnalyzeModel.usesAdvancedStockManagement($id_product) &&
        StockAvailable::dependsOnStock($id_product, $id_shop)) {
        return $manager->getProductRealQuantities($id_product, $id_product_attribute, $id_warehouse, true);
        } else {
        return StockAvailable::getQuantityAvailableByProduct($id_product, $id_product_attribute, $id_shop);
        }
        }

/**
 * For a given product, tells if it uses the advanced stock management
 *
 * @since 1.5.0
 * @param int $id_product
 * @return bool
 * /
public static function usesAdvancedStockManagement($id_product)
        {
        $query = new DbQuery;
        $query->select('product_shop.advanced_stock_management');
        $query->from('product', 'p');
        $query->join(JeproLabLaboratoryModel.addSqlAssociation('product', 'p'));
        $query->where('p.id_product = '.(int)$id_product);

        return (bool)Db::getInstance()->getValue($query);
        }

/**
 * This method allows to flush price cache
 *
 * @since 1.5.0
 * /
public static function flushPriceCache()
        {
        JeproLabAnalyzeModel.$_prices = array();
        JeproLabAnalyzeModel.$_pricesLevel2 = array();
        }

/*
 * Get list of parent categories
 *
 * @since 1.5.0
 * @param int $id_lang
 * @return array
 * /
public function getParentCategories($id_lang = null)
        {
        if (!$id_lang) {
        $id_lang = JeproLabContext.getContext()->language->id;
        }

        $interval = Category::getInterval(this.id_category_default);
        $sql = new DbQuery();
        $sql->from('category', 'c');
        $sql->leftJoin('category_lang', 'cl', 'c.id_category = cl.id_category AND id_lang = '.(int)$id_lang.JeproLabLaboratoryModel.addSqlRestrictionOnLang('cl'));
        $sql->where('c.nleft <= '.(int)$interval['nleft'].' AND c.nright >= '.(int)$interval['nright']);
        $sql->orderBy('c.nleft');

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
        }
*/
    /**
     * Fill the variables used for stock management
     */
    public void loadStockData() {
        if (this.analyze_id > 0){
            // By default, the product quantity correspond to the available quantity to sell in the current shop
            this.quantity = JeproLabStockAvailableModel.getQuantityAvailableByAnalyzeId(this.analyze_id, 0);
            this.out_of_stock = JeproLabStockAvailableModel.outOfStock(this.analyze_id);
            this.depends_on_stock = JeproLabStockAvailableModel.dependsOnStock(this.analyze_id);

            if (JeproLabContext.getContext().laboratory.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT && JeproLabContext.getContext().laboratory.getContextLaboratoryGroup().share_stock){
                this.advanced_stock_management = this.useAdvancedStockManagement();
            }
        }
    }

    public boolean useAdvancedStockManagement() {
        if (dataBaseObject == null) {
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("advanced_stock_management") + " FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_lab");
        query += " WHERE analyze_id = " + this.analyze_id + JeproLabLaboratoryModel.addSqlRestriction();
        dataBaseObject.setQuery(query);
        return ((int)dataBaseObject.loadValue("advanced_stock_management") > 0);
    }
/*
public function setAdvancedStockManagement($value)
        {
        this.advanced_stock_management = (int)$value;
        if (JeproLabContext.getContext()->shop->getContext() == JeproLabLaboratoryModel.CONTEXT_GROUP && JeproLabContext.getContext()->shop->getContextShopGroup()->share_stock == 1) {
        Db::getInstance()->execute('
        UPDATE `'.staticDataBaseObject.quoteName("#__jeprolab.'product_shop`
        SET `advanced_stock_management`='.(int)$value.'
        WHERE id_product='.(int)this.id.JeproLabLaboratoryModel.addSqlRestriction()
        );
        } else {
        this.setFieldsToUpdate(array('advanced_stock_management' => true));
        this.save();
        }
        }

/**
 * get the default category according to the shop
 * /
public function getDefaultCategory()
        {
        $default_category = Db::getInstance()->getValue('
        SELECT product_shop.`id_category_default`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product` p
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').'
        WHERE p.`id_product` = '.(int)this.id);

        if (!$default_category) {
        return array('id_category_default' => JeproLabContext.getContext()->shop->id_category);
        } else {
        return $default_category;
        }
        }

public static function getShopsByProduct($id_product)
        {
        return Db::getInstance()->executeS('
        SELECT `id_shop`
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_shop`
        WHERE `id_product` = '.(int)$id_product);
        }

/*
 * Remove all downloadable files for product and its attributes
 *
 * @return bool
 * /
public function deleteDownload()
        {
        $result = true;
        $collection_download = new PrestaShopCollection('ProductDownload');
        $collection_download->where('id_product', '=', this.id);
        foreach ($collection_download as $product_download) {
        /** @var ProductDownload $product_download * /
        $result &= $product_download->delete($product_download->checkFile());
        }
        return $result;
        }

/*
 * @deprecated 1.5.0.10
 * @see JeproLabAnalyzeModel.getAttributeCombinations()
 * @param int $id_lang
 * /
public function getAttributeCombinaisons($id_lang)
        {
        Tools::displayAsDeprecated('Use JeproLabAnalyzeModel.getAttributeCombinations($id_lang)');
        return this.getAttributeCombinations($id_lang);
        }

/*
 * @deprecated 1.5.0.10
 * @see JeproLabAnalyzeModel.deleteAttributeCombination()
 * @param int $id_product_attribute
 * /
public function deleteAttributeCombinaison($id_product_attribute)
        {
        Tools::displayAsDeprecated('Use JeproLabAnalyzeModel.deleteAttributeCombination($id_product_attribute)');
        return this.deleteAttributeCombination($id_product_attribute);
        }

    /**
     * Get the product type (simple, virtual, pack)
     *
     * @return int
     */
    public int getType() {
        if (this.analyze_id > 0) {
            return JeproLabAnalyzeModel.SIMPLE_ANALYZE;
        }
        if (JeproLabAnalyzePackModel.isPack(this.analyze_id)) {
            return JeproLabAnalyzeModel.PACK_ANALYZE;
        }
        /*if (this.is_virtual) {
        return JeproLabAnalyzeModel.PTYPE_VIRTUAL;
        }*/

        return JeproLabAnalyzeModel.SIMPLE_ANALYZE;
    }
/*
public function hasAttributesInOtherShops()
        {
        return (bool)Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue('
        SELECT pa.id_product_attribute
        FROM `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute` pa
        LEFT JOIN `'.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop` pas ON (pa.`id_product_attribute` = pas.`id_product_attribute`)
        WHERE pa.`id_product` = '.(int)this.id
        );
        }
*/
    public static int getMostUsedTaxRulesGroupId() {
        if (staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT tax_rules_group_id FROM ( SELECT COUNT(*) n, analyze_lab.tax_rules_group_id FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze");
        query += " analze " + JeproLabLaboratoryModel.addSqlAssociation("analyze") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_rules_group");
        query += " AS tax_rules_group ON (analyze_lab.tax_rules_group_id = tax_rules_group.tax_rules_group_id) WHERE tax_rules_group.published = 1 ";
        query += " GROUP BY analyze_lab.tax_rules_group_id ORDER BY n DESC LIMIT 1 ) most_used";

        staticDataBaseObject.setQuery(query);
        return (int) staticDataBaseObject.loadValue("most_used");
    }

/*
 * For a given ean13 reference, returns the corresponding id
 *
 * @param string $ean13
 * @return int id
 * /
public static function getIdByEan13($ean13)
        {
        if (empty($ean13)) {
        return 0;
        }

        if (!Validate::isEan13($ean13)) {
        return 0;
        }

        $query = new DbQuery();
        $query->select('p.id_product');
        $query->from('product', 'p');
        $query->where('p.ean13 = \''.pSQL($ean13).'\'');

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->getValue($query);
        }

public function getWsType()
        {
        $type_information = array(
        JeproLabAnalyzeModel.PTYPE_SIMPLE => 'simple',
        JeproLabAnalyzeModel.PTYPE_PACK => 'pack',
        JeproLabAnalyzeModel.PTYPE_VIRTUAL => 'virtual',
        );
        return $type_information[this.getType()];
        }

    /*
        Create the link rewrite if not exists or invalid on product creation
    * /
public function modifierWsLinkRewrite()
        {
        foreach (this.name as $id_lang => $name) {
        if (empty(this.link_rewrite[$id_lang])) {
        this.link_rewrite[$id_lang] = Tools::link_rewrite($name);
        } elseif (!Validate::isLinkRewrite(this.link_rewrite[$id_lang])) {
        this.link_rewrite[$id_lang] = Tools::link_rewrite(this.link_rewrite[$id_lang]);
        }
        }

        return true;
        }

public function getWsProductBundle()
        {
        return Db::getInstance()->executeS('SELECT id_product_item as id, quantity FROM '.staticDataBaseObject.quoteName("#__jeprolab.'pack WHERE id_product_pack = '.(int)this.id);
        }

public function setWsType($type_str)
        {
        $reverse_type_information = array(
        'simple' => JeproLabAnalyzeModel.PTYPE_SIMPLE,
        'pack' => JeproLabAnalyzeModel.PTYPE_PACK,
        'virtual' => JeproLabAnalyzeModel.PTYPE_VIRTUAL,
        );

        if (!isset($reverse_type_information[$type_str])) {
        return false;
        }

        $type = $reverse_type_information[$type_str];

        if (JeproLabAnalyzePackModel.isPack((int)this.id) && $type != JeproLabAnalyzeModel.PTYPE_PACK) {
        JeproLabAnalyzePackModel.deleteItems(this.id);
        }

        this.cache_is_pack = ($type == JeproLabAnalyzeModel.PTYPE_PACK);
        this.is_virtual = ($type == JeproLabAnalyzeModel.PTYPE_VIRTUAL);

        return true;
        }
/*
public function setWsProductBundle($items)
        {
        if (this.is_virtual) {
        return false;
        }

        JeproLabAnalyzePackModel.deleteItems(this.id);

        foreach ($items as $item) {
        if ((int)$item['id'] > 0) {
        JeproLabAnalyzePackModel.addItem(this.id, (int)$item['id'], (int)$item['quantity']);
        }
        }
        return true;
        }

public function isColorUnavailable($id_attribute, $id_shop)
        {
        return Db::getInstance()->getValue('
        SELECT sa.id_product_attribute
        FROM '.staticDataBaseObject.quoteName("#__jeprolab.'stock_available sa
        WHERE id_product='.(int)this.id.' AND quantity <= 0
        '.StockAvailable::addSqlShopRestriction(null, $id_shop, 'sa').'
        AND EXISTS (
        SELECT 1
        FROM '.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute pa
        JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_shop product_attribute_shop
        ON (product_attribute_shop.id_product_attribute = pa.id_product_attribute AND product_attribute_shop.id_shop='.(int)$id_shop.')
        JOIN '.staticDataBaseObject.quoteName("#__jeprolab.'product_attribute_combination pac
        ON (pac.id_product_attribute AND product_attribute_shop.id_product_attribute)
        WHERE sa.id_product_attribute = pa.id_product_attribute AND pa.id_product='.(int)this.id.' AND pac.id_attribute='.(int)$id_attribute.'
        )'
        );
        }

public static function getColorsListCacheId($id_product, $full = true)
        {
        $cacheKey = 'productlist_colors';
        if ($id_product) {
        $cacheKey .= '|'.(int)$id_product;
        }

        if ($full) {
        $cacheKey .= '|'.(int)JeproLabContext.getContext()->shop->id.'|'.(int)JeproLabContext.getContext()->cookie->id_lang;
        }

        return $cacheKey;
        }

public static function setPackStockType($id_product, $pack_stock_type)
        {
        return Db::getInstance()->execute('UPDATE '.staticDataBaseObject.quoteName("#__jeprolab.'product p
        '.JeproLabLaboratoryModel.addSqlAssociation('product', 'p').' SET product_shop.pack_stock_type = '.(int)$pack_stock_type.' WHERE p.`id_product` = '.(int)$id_product);
        }*/
}