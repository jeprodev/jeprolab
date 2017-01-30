package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabFactory;

import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabAttributeModel extends JeproLabModel{
    public int attribute_id;
    /** @var int Group id which attribute belongs */
    public int attribute_group_id;

    /** @var string Name */
    public Map<String, String> name;
    public String color;
    public int position;
    public boolean is_default;

    /**
     * Get minimal quantity for product with attributes quantity
     *
     * @param analyzeAttributeId JeproLabAttributeModel Attribute ID
     *
     * @return mixed Minimal quantity or false if no result
     */
    public static int getAttributeMinimalQuantity(int analyzeAttributeId){
        if(dataBaseObject == null){ dataBaseObject = JeproLabFactory.getDataBaseConnector(); }
        String query = "SELECT " + dataBaseObject.quoteName("minimal_quantity") + " FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute_lab");
        query += " AS analyze_attribute_lab WHERE " + dataBaseObject.quoteName("lab_id") + " = " + JeproLabContext.getContext().laboratory.laboratory_id;
        query += " AND " + dataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;

        dataBaseObject.setQuery(query);
        int minimalQuantity = (int)dataBaseObject.loadValue("minimal_quantity");

        if (minimalQuantity > 1) {
            return minimalQuantity;
        }

        return 0;
    }
}
