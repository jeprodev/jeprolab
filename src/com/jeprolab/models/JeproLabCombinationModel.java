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

}
