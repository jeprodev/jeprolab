package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;
import com.jeprolab.models.tax.JeproLabTaxCalculator;
import com.jeprolab.models.tax.JeproLabTaxManagerFactory;
import com.jeprolab.models.tax.JeproLabTaxRulesManager;

import java.sql.ResultSet;

/**
 *
 * Created by jeprodev on 04/02/14.
 */
public class JeproLabTaxModel extends JeproLabModel {
    public int tax_id;


    /** @var string Name */
    public String name;

    /** @var float Rate (%) */
    public float rate;

    /** @var bool active state */
    public boolean published;

    /** @var bool true if the tax has been historized */
    public boolean deleted = false;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'tax',
                    'primary' => 'id_tax',
                    'multilang' => true,
                    'fields' => array(
                    'rate' =>            array('type' => self::TYPE_FLOAT, 'validate' => 'isFloat', 'required' => true),
    'active' =>        array('type' => self::TYPE_BOOL),
    'deleted' =>        array('type' => self::TYPE_BOOL),
            /* Lang fields * /
    'name' =>            array('type' => self::TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'required' => true, 'size' => 32),
    ),
            );


    protected static $_product_country_tax = array();
    protected static $_product_tax_via_rules = array();

    protected $webserviceParameters = array(
            'objectsNodeName' => 'taxes',
    );
    */

    public JeproLabTaxModel(){
        this(0, 0, 0);
    }

    public JeproLabTaxModel(int taxId){
        this(taxId, 0, 0);
    }

    public JeproLabTaxModel(int taxId, int langId){
        this(taxId, langId, 0);
    }

    public JeproLabTaxModel(int taxId, int langId, int labId){
        if(langId > 0){

        }

        if(taxId > 0){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String cacheKey = "jeprolab_tax_model_" + taxId + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){

            }else{
                JeproLabTaxModel taxModel = (JeproLabTaxModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.tax_id = taxModel.tax_id;
                this.published = taxModel.deleted;
                this.published = taxModel.published;
                this.rate = taxModel.rate;
            }
        }
    }

    public boolean delete(){
        /* Clean associations */
        JeproLabTaxRuleModel.deleteTaxRuleByTaxId(this.tax_id);

        if (this.isUsed()) {
            return this.historize();
        } else {
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_tax") + " WHERE ";
            query += staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

            dataBaseObject.setQuery(query);
            return dataBaseObject.query(false);
        }
    }

    /**
     * Save the object with the field deleted to true
     *
     *  @return bool
     */
    public boolean historize(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        this.deleted = true;
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax") + " SET " + staticDataBaseObject.quoteName("published");
        query += " = 0, " + staticDataBaseObject.quoteName("deleted") + " = 1 WHERE " + staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }

    public boolean toggleStatus(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        //this.deleted = true;
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax") + " SET " + staticDataBaseObject.quoteName("published");
        query += " = " + (this.published ? "0" : "1") + ", " + staticDataBaseObject.quoteName("deleted") + " = " + (this.deleted ? "0" : "1");
        query += " WHERE " + staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

        dataBaseObject.setQuery(query);

        if(dataBaseObject.query(false)){
            return this.onStatusChange();
        }
        return false;
    }

    public boolean selfAdd(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_tax") + "(" + dataBaseObject.quoteName("rate") + ", " + dataBaseObject.quoteName("published");
        query += ", " + dataBaseObject.quoteName("deleted") + ") VALUES(" + this.rate + ", " + this.published + ", " + this.deleted + ")";

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }

    public boolean update(){
        if (!this.deleted && this.isUsed()) {
            JeproLabTaxModel historizedTax = new JeproLabTaxModel(this.tax_id);
            historizedTax.historize();

            // remove the id in order to create a new object
            this.tax_id = 0;
            boolean res = this.selfAdd();

            // change tax id in the tax rule table
            res &= JeproLabTaxRuleModel.swapTaxId(historizedTax.tax_id, this.tax_id);
            return res;
        } else {
            if (dataBaseObject == null) {
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_tax") + " SET " + staticDataBaseObject.quoteName("published");
            query += " = " + (this.published ? "0" : "1") + ", " + staticDataBaseObject.quoteName("deleted") + " = " + (this.deleted ? "0" : "1");
            query += " WHERE " + staticDataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

            dataBaseObject.setQuery(query);
            return dataBaseObject.query(false) && this.onStatusChange();
        }
    }

    protected boolean onStatusChange() {
        return this.published || JeproLabTaxRuleModel.deleteTaxRuleByTaxId(this.tax_id);
    }

    /**
     * Returns true if the tax is used in an order details
     *
     * @return bool
     */
    public boolean isUsed(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("tax_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_order_detail_tax");
        query += " WHERE " + dataBaseObject.quoteName("tax_id") + " = " + this.tax_id;

        dataBaseObject.setQuery(query);
        return (int)dataBaseObject.loadValue("tax_id") > 0;
    }

    public static ResultSet getTaxes(){
        return getTaxes(0, true);
    }

    public static ResultSet getTaxes(int langId ){
        return getTaxes(langId, true);
    }

    /**
     * Get all available taxes
     *
     * @return array Taxes
     */
    public static ResultSet getTaxes(int langId, boolean activeOnly){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String selectQuery = "SELECT tax.tax_id, tax.rate";
        String fromQuery = " FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax") + " AS tax ";
        String whereQuery = " WHERE tax." + staticDataBaseObject.quoteName("deleted") + " != 1";
        String leftJoin = "", orderBy = "";

        if (langId > 0) {
            selectQuery = ", tax_lang.name, tax_lang.lang_id ";
            leftJoin += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_lang") + " AS tax_lang ON (tax_lang.";
            leftJoin += staticDataBaseObject.quoteName("tax_id") + " = tax." + staticDataBaseObject.quoteName("tax_id") ;
            leftJoin += " AND tax_lang." + staticDataBaseObject.quoteName("lang_id") +  " = " + langId + ") ";
            orderBy += staticDataBaseObject.quoteName("name") + " ASC";
        }

        if (activeOnly) {
            whereQuery += " AND tax." + staticDataBaseObject.quoteName("published") + " = 1";
        }
        staticDataBaseObject.setQuery(selectQuery + fromQuery + leftJoin + whereQuery + orderBy);

        return staticDataBaseObject.loadObject();
    }

    public static boolean excludeTaxOption(){
        return (JeproLabSettingModel.getIntValue("use_tax") > 0);
    }

    public static int getTaxIdByName(String taxName){
        return getTaxIdByName(taxName, true);
    }
    /**
     * Return the tax id associated to the specified name
     *
     * @param taxName tax name of a tax to be retrieved
     * @param active (true by default)
     */
    public static int getTaxIdByName(String taxName, boolean active){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tax." + staticDataBaseObject.quoteName("tax_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax");
        query += " AS tax LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_lang") + " As tax_lang ON (tax_lang.tax_id = tax.tax_id)";
        query += " WHERE tax_lang." + staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(taxName);
        query += (active ? " AND tax." + staticDataBaseObject.quoteName("published") + " = 1 " : "");

        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("tax_id");
    }

    public static float getAnalyzeEcoTaxRate(){
        return getAnalyzeEcoTaxRate(0);
    }

    /**
     * Returns the ecotax tax rate
     *
     * @param addressId address id
     * @return float $tax_rate
     */
    public static float getAnalyzeEcoTaxRate(int addressId){
        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, JeproLabSettingModel.getIntValue("ecotax_tax_rules_group_id"));
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }

    public static float getCarrierTaxRate(int carrierId){
        return getCarrierTaxRate(carrierId, 0);
    }

    /**
     * Returns the carrier tax rate
     *
     * @param carrierId carrier Id
     * @param addressId address id
     * @return float $tax_rate
     */
    public static float getCarrierTaxRate(int carrierId, int addressId){
        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);
        int taxRulesId = JeproLabCarrierModel.getTaxRulesGroupIdByCarrierId(carrierId);

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, taxRulesId);
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }

    /*
     * Return the product tax rate using the tax rules system
     *
     * @param analyzeId
     * @param int $id_country
     * @return Tax
     *
     * @deprecated since 1.5
     * /
    public static function getAnalyzeTaxRateThroughRules(int analyzeId, int countryIdy, int stateId, String zipCode){
        Tools::displayAsDeprecated();

        if (!isset(self::$_product_tax_via_rules[$id_product.'-'.$id_country.'-'.$id_state.'-'.$zipcode])) {
        $tax_rate = TaxRulesGroup::getTaxesRate((int)Product::getIdTaxRulesGroupByIdProduct((int)$id_product), (int)$id_country, (int)$id_state, $zipcode);
        self::$_product_tax_via_rules[$id_product.'-'.$id_country.'-'.$zipcode] = $tax_rate;
    }

        return self::$_product_tax_via_rules[$id_product.'-'.$id_country.'-'.$zipcode];
    } */

    public static float getAnalyzeTaxRate(int analyzeId ){
        return getAnalyzeTaxRate(analyzeId, 0, null);
    }

    public static float getAnalyzeTaxRate(int analyzeId, int addressId){
        return getAnalyzeTaxRate(analyzeId, addressId, null);
    }

    /**
     * Returns the product tax
     *
     * @param analyzeId analyze id
     * @param addressId address to get the tax from
     * @return Tax value
     */
    public static float getAnalyzeTaxRate(int analyzeId, int addressId, JeproLabContext context){
        if (context == null) {
            context = JeproLabContext.getContext();
        }

        JeproLabAddressModel address = JeproLabAddressModel.initialize(addressId);
        int taxRulesId = JeproLabAnalyzeModel.getTaxRulesGroupIdByAnalyzeId(analyzeId, context);

        JeproLabTaxRulesManager taxManager = JeproLabTaxManagerFactory.getManager(address, taxRulesId);
        JeproLabTaxCalculator taxCalculator = taxManager.getTaxCalculator();

        return taxCalculator.getTotalRate();
    }
}