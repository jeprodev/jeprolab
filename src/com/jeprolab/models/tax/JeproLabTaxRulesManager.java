package com.jeprolab.models.tax;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabSettingModel;
import com.jeprolab.models.JeproLabTaxModel;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 04/02/14.
 */
public class JeproLabTaxRulesManager implements JeproLabTaxManagerInterface {
    public JeproLabAddressModel address;
    public String type;
    public JeproLabTaxCalculator tax_calculator;

    private static boolean tax_enabled = false;

    /**
     * @var Core_Business_ConfigurationInterface
     */
    //private $configurationManager;

    /**
     *
     * @param address
     * @param type An additional parameter for the tax manager (ex: tax rules id for TaxRuleTaxManager)
     */
    public JeproLabTaxRulesManager(JeproLabAddressModel address, String type){ //}, Core_Business_ConfigurationInterface $configurationManager = null)

        /*if ($configurationManager === null) {
            $this->configurationManager = Adapter_ServiceLocator::get('Core_Business_ConfigurationInterface');
        } else {
            $this->configurationManager = $configurationManager;
        }*/

        this.address = address;
        this.type = type;
    }

    /**
     * Returns true if this tax manager is available for this address
     *
     * @return bool
     */
    public static boolean isAvailableForThisAddress(JeproLabAddressModel address){
        return true; // default manager, available for all addresses
    }

    /**
     * Return the tax calculator associated to this address
     *
     * @return JeproLabTaxCalculator
     */
    public JeproLabTaxCalculator getTaxCalculator() {
        if (this.tax_calculator != null){
            return this.tax_calculator;
        }

        if (!tax_enabled) {
            tax_enabled = JeproLabSettingModel.getIntValue("use_tax") > 0; //$this->configurationManager->get('PS_TAX');
        }

        if (!tax_enabled) {
            return new JeproLabTaxCalculator(new ArrayList<JeproLabTaxModel>());
        }

        String postcode = "";

        if (!this.address.postcode.equals("")) {
            postcode = this.address.postcode;
        }

        String cacheKey = this.address.country_id + "_" + this.address.state_id + "_" + postcode + "_" + this.type;

        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            JeproLabDataBaseConnector dataBaseConnector = JeproLabFactory.getDataBaseConnector();
            String query = "SELECT tax_rule.* FROM " + dataBaseConnector.quoteName("#__jeprolab_tax_rule") + " AS tax_rule LEFT JOIN ";
            query += dataBaseConnector.quoteName("#__jeprolab_tax_rules_group") + " AS tax_rule_group ON (tax_rule." + dataBaseConnector.quoteName("tax_rules_group_id");
            query += " tax_rule_group." + dataBaseConnector.quoteName("tax_rules_group_id") + ") WHERE tax_rule_group." + dataBaseConnector.quoteName("published");
            query += " = 1 AND tax_rule." + dataBaseConnector.quoteName("country_id") + " = " + this.address.country_id + " AND tax_rule.";
            query += dataBaseConnector.quoteName("tax_rules_group_id") + " = " + this.type + " AND tax_rule." + dataBaseConnector.quoteName("state_id");
            query += " IN (0, " + this.address.state_id + ") AND (" + dataBaseConnector.quote(postcode) + " BETWEEN tax_rule.";
            query += dataBaseConnector.quoteName("zipcode_from") + " AND tax_rule." + dataBaseConnector.quoteName("zipcode_to");
            query += " OR (tax_rule." + dataBaseConnector.quoteName("zipcode_to") + " = '' AND tax_rule." + dataBaseConnector.quoteName("zipcode_from");
            query += " IN(0, " + dataBaseConnector.quote(postcode) + "))) ORDER BY tax_rule." + dataBaseConnector.quoteName("zipcode_from");
            query += " DESC, tax_rule." + dataBaseConnector.quoteName("zipcode_to") + " DESC, tax_rule." + dataBaseConnector.quoteName("state_id");
            query += " DESC, tax_rule." + dataBaseConnector.quoteName("country_id") + " DESC ";

            dataBaseConnector.setQuery(query);
            ResultSet taxesSet = dataBaseConnector.loadObject();

            int behavior = 0;
            boolean firstRow = true;
            List<JeproLabTaxModel> taxes = new ArrayList<>();
            try{
                JeproLabTaxModel tax;
                while(taxesSet.next()){
                    tax = new JeproLabTaxModel(taxesSet.getInt("tax_id"));
                    taxes.add(tax);
                    if(firstRow){
                        behavior = taxesSet.getInt("behavior");
                        firstRow = false;
                    }

                    if(taxesSet.getInt("behavior") == 0){
                        break;
                    }
                }
            }catch (SQLException ignored){

            }

            JeproLabTaxCalculator result = new JeproLabTaxCalculator(taxes, behavior);
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }

        return (JeproLabTaxCalculator)JeproLabCache.getInstance().retrieve(cacheKey);
    }
}
