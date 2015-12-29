package com.jeprolab.models;


import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

import java.util.ArrayList;
import java.util.List;

public class JeproLabLaboratoryGroupModel extends JeproLabModel {
    public String name;

    public int laboratory_group_id = 0;

    public int share_customers, share_results, share_requests;

    public boolean published = true, deleted = false;

    public List<JeproLabLaboratoryModel> laboratories;

    public JeproLabLaboratoryGroupModel(){

    }

    public static ArrayList<JeproLabLaboratoryGroupModel> getLabGroups(){
        return getLabGroups(true);
    }

    public static ArrayList<JeproLabLaboratoryGroupModel> getLabGroups(boolean activated){
        try {
            staticDataBaseObject = JeproLabDataBaseConnector.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}