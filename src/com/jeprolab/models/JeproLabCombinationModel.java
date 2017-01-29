package com.jeprolab.models;

import java.sql.Date;

/**
 *
 * Created by jeprodev on 09/01/2016.
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
}
