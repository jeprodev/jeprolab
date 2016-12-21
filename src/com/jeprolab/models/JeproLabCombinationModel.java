package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCombinationModel extends JeproLabModel{
    public int analyze_id;

    public int analyze_attribute_id;

    public String reference;

    public String supplier_reference;

    public String location;

    public String ean13;

    public String upc;

    public float wholesale_price;

    public float price;

    public float unit_price_impact;

    public float ecotax;

    public int minimal_quantity = 1;

    public int quantity;

    public double weight;

    public boolean default_on;

    public Date available_date;

    private static boolean feature_active = false;

    public JeproLabCombinationModel(){
        this(0);
    }

    public JeproLabCombinationModel(int analyzeAttributeId){}

    /**
     * This method is allow to know if a feature is active
     *
     * @return bool
     */
    public static boolean isFeaturePublished(){
        if (!feature_active ) {
            feature_active = JeproLabSettingModel.getIntValue("combination_feature_active") > 0;
        }
        return feature_active;
    }

    public List<JeproLabAttributeModel> getAttributesName(int langId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT attribute_lang.* FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute_combination");
        query += " AS analyze_combination JOIN " + dataBaseObject.quoteName("#__jeprolab_attribute_lang") + " AS attribute_lang";
        query += " ON (analyze_combination.attribute_id = attribute_lang.attribute_id AND attribute_lang.lang_id = " + langId;
        query += ") WHERE analyze_combination.analyze_attribute_id = " + this.analyze_attribute_id;

        dataBaseObject.setQuery(query);
        ResultSet attributeSet = dataBaseObject.loadObjectList();
        List<JeproLabAttributeModel> attributeList = new ArrayList<>();
        if(attributeSet != null){
            try{
                JeproLabAttributeModel attribute;
                while(attributeSet.next()){
                    attribute = new JeproLabAttributeModel();
                    attribute.name.put("lang_" + langId, attributeSet.getString("name"));
                    attributeList.add(attribute);
                }
            }catch (SQLException ignored){

            }
        }
        return attributeList;
    }

    public boolean delete(){
        /*if (!parent::delete()) {
        return false;
    }

        // Removes the product from StockAvailable, for the current shop
        StockAvailable::removeProductFromStockAvailable((int)$this->id_product, (int)$this->id);

        if ($specific_prices = SpecificPrice::getByProductId((int)$this->id_product, (int)$this->id)) {
        foreach ($specific_prices as $specific_price) {
            $price = new SpecificPrice((int)$specific_price['id_specific_price']);
            $price->delete();
        }
    }

        if (!$this->hasMultishopEntries() && !$this->deleteAssociations()) {
            return false;
        }

        $this->deleteFromSupplier($this->id_product);
        Product::updateDefaultAttribute($this->id_product);
        Tools::clearColorListCache((int)$this->id_product);
*/
        return true;
    }

    public static class JeproLabCustomizationModel extends  JeproLabModel{
        public int customization_id;

        /** @var int */
        public int analyze_attribute_id;
        /** @var int */
        public int delivery_address_id;
        /** @var int */
        public int cart_id;
        /** @var int */
        public int analyze_id;
        /** @var int */
        public int quantity;
        /** @var int */
        public int quantity_refunded;
        /** @var int */
        public int quantity_returned;
        /** @var bool */
        public boolean in_cart;

        /**
         * This method is allow to know if a feature is used or active
         * @since 1.5.0.1
         * @return bool
         */
        public static boolean isFeaturePublished(){
            return JeproLabSettingModel.getIntValue("customization_feature_active") > 0;
        }

        /**
         * Get price of Customization
         *
         * @param customizationId Customization ID
         *
         * @return float|int Price of customization
         * /
        public static function getCustomizationPrice($idCustomization)
        {
            if (!(int) $idCustomization) {
                return 0;
            }

            return (float) Db::getInstance()->getValue('
            SELECT SUM(`price`) FROM `'._DB_PREFIX_.'customized_data`
            WHERE `id_customization` = '.(int) $idCustomization
            );
        }

        /**
         * Get weight of Customization
         *
         * @param customizationId Customization ID
         *
         * @return float|int Weight
         */
        public static float getCustomizationWeight(int customizationId){
            if (customizationId <= 0) {
                return 0;
            }

            if(dataBaseObject == null){ dataBaseObject = JeproLabFactory.getDataBaseConnector(); }

            String query = "SELECT SUM(" + dataBaseObject.quoteName("weight") + ") FROM " + dataBaseObject.quoteName("#__jeprolab_customized_data");
            query += " WHERE " + dataBaseObject.quoteName("customization_id") + " = " + customizationId;

            dataBaseObject.setQuery(query);
            return (float)dataBaseObject.loadValue("weight");
        }

    }

}
