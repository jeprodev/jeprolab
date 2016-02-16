package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 04/02/14.
 */
public class JeproLabTaxRulesGroupModel  extends JeproLabModel {
    public int tax_rules_group_id;

    public String name;

    /** @var bool active state */
    public boolean published;

    public boolean deleted = false;

    /** @var string Object creation date */
    public Date date_add;

    /** @var string Object last modification date */
    public Date date_upd;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'tax_rules_group',
                    'primary' => 'id_tax_rules_group',
                    'fields' => array(
                    'name' =>        array('type' => self::TYPE_STRING, 'validate' => 'isGenericName', 'required' => true, 'size' => 64),
    'active' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
    'deleted' =>    array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
    'date_add' =>    array('type' => self::TYPE_DATE, 'validate' => 'isDate'),
    'date_upd' =>    array('type' => self::TYPE_DATE, 'validate' => 'isDate'),
    ),
            );

    protected $webserviceParameters = array(
            'objectsNodeName' => 'tax_rule_groups',
                    'objectNodeName' => 'tax_rule_group',
                    'fields' => array(
    ),
    );

    protected static $_taxes = array();


    public function update($null_values = false)
    {
        if (!$this->deleted && $this->isUsed()) {
            $current_tax_rules_group = new TaxRulesGroup((int)$this->id);
            if ((!$new_tax_rules_group = $current_tax_rules_group->duplicateObject()) || !$current_tax_rules_group->historize($new_tax_rules_group)) {
                return false;
            }

            $this->id = (int)$new_tax_rules_group->id;
        }

        return parent::update($null_values);
    }

    /**
     * Save the object with the field deleted to true
     *
     *  @return bool
     * /
    public function historize(TaxRulesGroup $tax_rules_group)
    {
        $this->deleted = true;

        return parent::update() &&
            Db::getInstance()->execute('
            INSERT INTO '._DB_PREFIX_.'tax_rule
            (id_tax_rules_group, id_country, id_state, zipcode_from, zipcode_to, id_tax, behavior, description)
        (
                SELECT '.(int)$tax_rules_group->id.', id_country, id_state, zipcode_from, zipcode_to, id_tax, behavior, description
        FROM '._DB_PREFIX_.'tax_rule
        WHERE id_tax_rules_group='.(int)$this->id.'
        )') &&
        Db::getInstance()->execute('
            UPDATE '._DB_PREFIX_.'product
        SET id_tax_rules_group='.(int)$tax_rules_group->id.'
        WHERE id_tax_rules_group='.(int)$this->id) &&
        Db::getInstance()->execute('
            UPDATE '._DB_PREFIX_.'product_shop
        SET id_tax_rules_group='.(int)$tax_rules_group->id.'
        WHERE id_tax_rules_group='.(int)$this->id) &&
        Db::getInstance()->execute('
            UPDATE '._DB_PREFIX_.'carrier
        SET id_tax_rules_group='.(int)$tax_rules_group->id.'
        WHERE id_tax_rules_group='.(int)$this->id) &&
        Db::getInstance()->execute('
            UPDATE '._DB_PREFIX_.'carrier_tax_rules_group_shop
        SET id_tax_rules_group='.(int)$tax_rules_group->id.'
        WHERE id_tax_rules_group='.(int)$this->id);
    }

    public function getIdTaxRuleGroupFromHistorizedId($id_tax_rule)
    {
        $params = Db::getInstance()->getRow('
            SELECT id_country, id_state, zipcode_from, zipcode_to, id_tax, behavior
            FROM '._DB_PREFIX_.'tax_rule
        WHERE id_tax_rule='.(int)$id_tax_rule
        );

        return Db::getInstance()->getValue('
            SELECT id_tax_rule
            FROM '._DB_PREFIX_.'tax_rule
            WHERE
        id_tax_rules_group = '.(int)$this->id.' AND
            id_country='.(int)$params['id_country'].' AND id_state='.(int)$params['id_state'].' AND id_tax='.(int)$params['id_tax'].' AND
        zipcode_from=\''.pSQL($params['zipcode_from']).'\' AND zipcode_to=\''.pSQL($params['zipcode_to']).'\' AND behavior='.(int)$params['behavior']
        );
    }
*/

    public static List<JeproLabTaxRulesGroupModel> getTaxRulesGroups(){
        return getTaxRulesGroups(true);
    }

    public static List<JeproLabTaxRulesGroupModel> getTaxRulesGroups(boolean onlyPublished){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT DISTINCT tax_rules_group." + staticDataBaseObject.quoteName("tax_rules_group_id") + ", tax_rules_group.name, ";
        query += "tax_rules_group.published FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " AS tax_rules_group ";
        query += JeproLabLaboratoryModel.addSqlAssociation("tax_rules_group") ;//+ " WHERE deleted = 0 " ;
        query += (onlyPublished ? " WHERE tax_rules_group." + staticDataBaseObject.quoteName("published") + " = 1 " : "") + " ORDER BY name ASC ";

        staticDataBaseObject.setQuery(query);
        ResultSet taxRulesGroups = staticDataBaseObject.loadObject();
        List<JeproLabTaxRulesGroupModel> taxRulesList = new ArrayList<>();
        try{
            JeproLabTaxRulesGroupModel taxRulesGroupModel;
            while(taxRulesGroups.next()){
                taxRulesGroupModel = new JeproLabTaxRulesGroupModel();
                taxRulesGroupModel.tax_rules_group_id = taxRulesGroups.getInt("tax_rules_group_id");
                taxRulesGroupModel.published = taxRulesGroups.getInt("published") > 0;
                //taxRulesGroupModel. = taxRulesGroups.get("");
                taxRulesList.add(taxRulesGroupModel);
            }
        }catch (SQLException ignored){

        }
        return taxRulesList;
    }

    /*
     * @return array an array of tax rules group formatted as $id => $name
     * /
    public static function getTaxRulesGroupsForOptions()
    {
        $tax_rules[] = array('id_tax_rules_group' => 0, 'name' => Tools::displayError('No tax'));
        return array_merge($tax_rules, TaxRulesGroup::getTaxRulesGroups());
    }

    public function delete()
    {
        $res = Db::getInstance()->execute('DELETE FROM `'._DB_PREFIX_.'tax_rule` WHERE `id_tax_rules_group`='.(int)$this->id);
        return (parent::delete() && $res);
    }

    /**
     * @return array
     */
    public static Map<Integer, Float> getAssociatedTaxRatesByCountryId(int countryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tax_rules_group." + staticDataBaseObject.quoteName("tax_rules_group_id") + ", tax." + staticDataBaseObject.quoteName("rate") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_tax_rules_group") + " AS tax_rules_group LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax_rule");
        query += " tax_rule ON (tax_rule." + staticDataBaseObject.quoteName("tax_rules_group_id") + " = tax_rules_group." + staticDataBaseObject.quoteName("tax_rules_group_id");
        query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_tax") + " AS tax ON (tax." + staticDataBaseObject.quoteName("tax_id") + " = tax_rule.";
        query += staticDataBaseObject.quoteName("tax_id") + ") WHERE tax_rule." + staticDataBaseObject.quoteName("country_id") + " = " + countryId + " AND tax_rule.";
        query += staticDataBaseObject.quoteName("state_id") + " = 0 AND 0 between " + staticDataBaseObject.quoteName("zipcode_from") + " AND " ;
        query += staticDataBaseObject.quoteName("zipcode_to");

        staticDataBaseObject.setQuery(query);
        ResultSet rows = staticDataBaseObject.loadObject();
        Map<Integer, Float> taxRates = new HashMap<>();

        try{
            while(rows.next()){
                taxRates.put(rows.getInt("tax_rules_group_id"), rows.getFloat("rate"));
            }
        }catch(SQLException ignored){

        }

        return taxRates;
    }

    /**
     * Returns the tax rules group id corresponding to the name
     *
     * @param string $name
     * @return int id of the tax rules
     * /
    public static function getIdByName($name)
    {
        return Db::getInstance()->getValue(
            'SELECT `id_tax_rules_group`
            FROM `'._DB_PREFIX_.'tax_rules_group` rg
        WHERE `name` = \''.pSQL($name).'\''
        );
    }

    public function hasUniqueTaxRuleForCountry($id_country, $id_state, $id_tax_rule = false)
    {
        $rules = TaxRule::getTaxRulesByGroupId((int)Context::getContext()->language->id, (int)$this->id);
        foreach ($rules as $rule) {
        if ($rule['id_country'] == $id_country && $id_state == $rule['id_state'] && !$rule['behavior'] && (int)$id_tax_rule != $rule['id_tax_rule']) {
            return true;
        }
    }

        return false;
    }

    public function isUsed()
    {
        return Db::getInstance()->getValue('
            SELECT `id_tax_rules_group`
            FROM `'._DB_PREFIX_.'order_detail`
        WHERE `id_tax_rules_group` = '.(int)$this->id
        );
    }

    /**
     * @deprecated since 1.5
     * /
    public static function getTaxesRate($id_tax_rules_group, $id_country, $id_state, $zipcode)
    {
        Tools::displayAsDeprecated();
        $rate = 0;
        foreach (TaxRulesGroup::getTaxes($id_tax_rules_group, $id_country, $id_state, $zipcode) as $tax) {
        $rate += (float)$tax->rate;
    }

        return $rate;
    }

    /**
     * Return taxes associated to this para
     * @deprecated since 1.5
     * /
    public static function getTaxes($id_tax_rules_group, $id_country, $id_state, $id_county)
    {
        Tools::displayAsDeprecated();
        return array();
    }*/
}
