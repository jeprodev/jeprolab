package com.jeprolab.models;


import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeproLabLaboratoryGroupModel extends JeproLabModel {
    public String name;

    public int laboratory_group_id = 0;

    public boolean share_customers, share_results, share_requests;

    public boolean published = true, deleted = false;

    public List<JeproLabLaboratoryModel> laboratories;

    public JeproLabLaboratoryGroupModel(){

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
                laboratoryGroup.share_results = results.getInt("share_") > 0;
                laboratoryGroup.published = results.getInt("published") > 0;
                laboratoryGroup.deleted = results.getInt("deleted") > 0;
                laboratoryGroups.add(laboratoryGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laboratoryGroups;
    }
}