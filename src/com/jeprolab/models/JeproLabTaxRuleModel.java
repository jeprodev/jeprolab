package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

/**
 *
 * Created by jeprodev on 04/02/14.
 */
public class JeproLabTaxRuleModel extends JeproLabModel{
    public int tax_rules_group_id;
    public int country_id;
    public int state_id;
    public String zipcode_from;
    public String zipcode_to;
    public int tax_id;
    public int behavior;
    public String description;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'tax_rule',
                    'primary' => 'id_tax_rule',
                    'fields' => array(
                    'id_tax_rules_group' => array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_country' =>        array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'id_state' =>            array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId'),
    'zipcode_from' =>        array('type' => self::TYPE_STRING, 'validate' => 'isPostCode'),
    'zipcode_to' =>        array('type' => self::TYPE_STRING, 'validate' => 'isPostCode'),
    'id_tax' =>            array('type' => self::TYPE_INT, 'validate' => 'isUnsignedId', 'required' => true),
    'behavior' =>            array('type' => self::TYPE_INT, 'validate' => 'isUnsignedInt'),
    'description' =>        array('type' => self::TYPE_STRING, 'validate' => 'isString'),
    ),
            );

    protected $webserviceParameters = array(
            'fields' => array(
            'id_tax_rules_group' => array('xlink_resource'=> 'tax_rule_groups'),
    'id_state' => array('xlink_resource'=> 'states'),
    'id_country' => array('xlink_resource'=> 'countries')
    ),
            );

    public static function deleteByGroupId($id_group)
    {
        if (empty($id_group)) {
            die(Tools::displayError());
        }

        return Db::getInstance()->execute('
            DELETE FROM `'._DB_PREFIX_.'tax_rule`
        WHERE `id_tax_rules_group` = '.(int)$id_group
        );
    }

    public static function retrieveById($id_tax_rule)
    {
        return Db::getInstance()->getRow('
            SELECT * FROM `'._DB_PREFIX_.'tax_rule`
        WHERE `id_tax_rule` = '.(int)$id_tax_rule);
    }

    public static function getTaxRulesByGroupId($id_lang, $id_group)
    {
        return Db::getInstance()->executeS('
            SELECT g.`id_tax_rule`,
            c.`name` AS country_name,
            s.`name` AS state_name,
            t.`rate`,
            g.`zipcode_from`, g.`zipcode_to`,
            g.`description`,
            g.`behavior`,
            g.`id_country`,
            g.`id_state`
            FROM `'._DB_PREFIX_.'tax_rule` g
        LEFT JOIN `'._DB_PREFIX_.'country_lang` c ON (g.`id_country` = c.`id_country` AND `id_lang` = '.(int)$id_lang.')
        LEFT JOIN `'._DB_PREFIX_.'state` s ON (g.`id_state` = s.`id_state`)
        LEFT JOIN `'._DB_PREFIX_.'tax` t ON (g.`id_tax` = t.`id_tax`)
        WHERE `id_tax_rules_group` = '.(int)$id_group.'
        ORDER BY `country_name` ASC, `state_name` ASC, `zipcode_from` ASC, `zipcode_to` ASC'
        );
    }
*/
    public static boolean deleteTaxRuleByTaxId(int taxId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_tax_rule") + " AS tax_rule WHERE ";
        query += staticDataBaseObject.quoteName("tax_id") + " = " + taxId;
        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    /*
     * @deprecated since 1.5
     * /
    public static function deleteTaxRuleByIdCounty($id_county)
    {
        Tools::displayAsDeprecated();
        return true;
    }

    /**
     * @param int $id_tax
     * @return bool
     * /
    public static function isTaxInUse($id_tax)
    {
        $cache_id = 'TaxRule::isTaxInUse_'.(int)$id_tax;
        if (!Cache::isStored($cache_id)) {
        $result = (int)Db::getInstance()->getValue('SELECT COUNT(*) FROM `'._DB_PREFIX_.'tax_rule` WHERE `id_tax` = '.(int)$id_tax);
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }


    /**
     * @param string $zipcode a range of zipcode (eg: 75000 / 75000-75015)
     * @return array an array containing two zipcode ordered by zipcode
     * /
    public function breakDownZipCode($zip_codes)
    {
        $zip_codes = preg_split('/-/', $zip_codes);

        $from = $zip_codes[0];
        $to = isset($zip_codes[1]) ? $zip_codes[1]: 0;
        if (count($zip_codes) == 2) {
            $from = $zip_codes[0];
            $to   = $zip_codes[1];
            if ($zip_codes[0] > $zip_codes[1]) {
                $from = $zip_codes[1];
                $to   = $zip_codes[0];
            } elseif ($zip_codes[0] == $zip_codes[1]) {
                $from = $zip_codes[0];
                $to   = 0;
            }
        } elseif (count($zip_codes) == 1) {
        $from = $zip_codes[0];
        $to = 0;
    }

        return array($from, $to);
    }

    /**
     * Replace a tax_rule id by an other one in the tax_rule table
     *
     * @param oldId old tax id
     * @param newId new tax id
     */
    public static boolean swapTaxId(int oldId, int newId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_tax_rule") + " SET " + staticDataBaseObject.quoteName("tax_id");
        query += " = " + newId + " WHERE " + staticDataBaseObject.quoteName("tax_id") + " = "+ oldId;

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }
}