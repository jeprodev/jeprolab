package com.jeprolab.models;


import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeproLabLaboratoryGroupModel extends JeproLabModel {
    public String name;

    public int laboratory_group_id = 0;

    public boolean share_customers, share_results, share_requests, share_stock;

    public boolean published = true, deleted = false;

    public List<JeproLabLaboratoryModel> laboratories;

    public JeproLabLaboratoryGroupModel(){

    }

    public JeproLabLaboratoryGroupModel(int labGroupId){

    }

    public static List<JeproLabLaboratoryGroupModel> getLaboratoryGroups(){
        return getLaboratoryGroups(true);
    }

    public static List<JeproLabLaboratoryGroupModel> getLaboratoryGroups(boolean activated){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_lab_group") + " WHERE 1 ";
        if(activated){
            query += " AND " + staticDataBaseObject.quoteName("published") + " = " + (activated ? 1 : 0);
        }
        List<JeproLabLaboratoryGroupModel> laboratoryGroups = new ArrayList<>();
        staticDataBaseObject.setQuery(query);
        ResultSet results = staticDataBaseObject.loadObject();
        try {
            JeproLabLaboratoryGroupModel laboratoryGroup;
            while(results.next()){
                laboratoryGroup = new JeproLabLaboratoryGroupModel();
                laboratoryGroup.laboratory_group_id = results.getInt("lab_group_id");
                laboratoryGroup.name = results.getString("lab_group_name");
                laboratoryGroup.share_customers = results.getInt("share_customer") > 0;
                laboratoryGroup.share_requests = results.getInt("share_requests") > 0;
                laboratoryGroup.share_results = results.getInt("share_results") > 0;
                laboratoryGroup.share_stock = results.getInt("share_stock") > 0;
                laboratoryGroup.published = results.getInt("published") > 0;
                laboratoryGroup.deleted = results.getInt("deleted") > 0;
                laboratoryGroups.add(laboratoryGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laboratoryGroups;
    }

    /*
     * @see ObjectModel::getFields()
     * @return array
     * /
    public function getFields()
    {
        if (!$this->share_customer || !$this->share_stock) {
            $this->share_order = false;
        }

        return parent::getFields();
    }

    public static function getShopGroups($active = true)
    {
        $groups = new PrestaShopCollection('ShopGroup');
        $groups->where('deleted', '=', false);
        if ($active) {
            $groups->where('active', '=', true);
        }
        return $groups;
    }

    /**
     * @return int Total of shop groups
     * /
    public static function getTotalShopGroup($active = true)
    {
        return count(ShopGroup::getShopGroups($active));
    }

    public function haveShops()
    {
        return (bool)$this->getTotalShops();
    }

    public function getTotalShops()
    {
        $sql = 'SELECT COUNT(*)
        FROM '._DB_PREFIX_.'shop s
        WHERE id_shop_group='.(int)$this->id;
        return (int)Db::getInstance()->getValue($sql);
    }

    public static function getShopsFromGroup($id_group)
    {
        $sql = 'SELECT s.`id_shop`
        FROM '._DB_PREFIX_.'shop s
        WHERE id_shop_group='.(int)$id_group;
        return Db::getInstance()->executeS($sql);
    }

    /**
     * Return a group shop ID from group shop name
     *
     * @param string $name
     * @return int
     * /
    public static function getIdByName($name)
    {
        $sql = 'SELECT id_shop_group
        FROM '._DB_PREFIX_.'shop_group
        WHERE name = \''.pSQL($name).'\'';
        return (int)Db::getInstance()->getValue($sql);
    }

    /**
     * Detect dependency with customer or orders
     *
     * @param int $id_shop_group
     * @param string $check all|customer|order
     * @return bool
     * /
    public static function hasDependency($id_shop_group, $check = 'all')
    {
        $list_shops = Shop::getShops(false, $id_shop_group, true);
        if (!$list_shops) {
            return false;
        }

        if ($check == 'all' || $check == 'customer') {
            $total_customer = (int)Db::getInstance()->getValue('
                    SELECT count(*)
            FROM `'._DB_PREFIX_.'customer`
            WHERE `id_shop` IN ('.implode(', ', $list_shops).')'
            );
            if ($total_customer) {
                return true;
            }
        }

        if ($check == 'all' || $check == 'order') {
            $total_order = (int)Db::getInstance()->getValue('
                    SELECT count(*)
            FROM `'._DB_PREFIX_.'orders`
            WHERE `id_shop` IN ('.implode(', ', $list_shops).')'
            );
            if ($total_order) {
                return true;
            }
        }

        return false;
    }

    public function shopNameExists($name, $id_shop = false)
    {
        return Db::getInstance()->getValue('
            SELECT id_shop
            FROM '._DB_PREFIX_.'shop
        WHERE name = "'.pSQL($name).'"
        AND id_shop_group = '.(int)$this->id.'
        '.($id_shop ? 'AND id_shop != '.(int)$id_shop : '')
        );
    } */
}