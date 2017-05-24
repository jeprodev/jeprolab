package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
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
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("minimal_quantity") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_attribute_lab");
        query += " AS analyze_attribute_lab WHERE " + JeproLabDataBaseConnector.quoteName("lab_id") + " = " + JeproLabContext.getContext().laboratory.laboratory_id;
        query += " AND " + JeproLabDataBaseConnector.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        int minimalQuantity = (int)dataBaseObject.loadValue(query, "minimal_quantity");

        if (minimalQuantity > 1) {
            return minimalQuantity;
        }
        closeDataBaseConnection(dataBaseObject);
        return 0;
    }
}
