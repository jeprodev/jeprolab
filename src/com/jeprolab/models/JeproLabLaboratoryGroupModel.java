package com.jeprolab.models;


import jeprolab.assets.tools.JeproLabDataBaseConnector;

import java.util.ArrayList;

public class JeproLabLaboratoryGroupModel extends JeproLabModel {
    public String name;

    public int lab_group_id = 0;

    public int share_customer, share_result, share_request;

    public boolean published = true, deleted = false;

    public JeproLabLaboratoryGroupModel(){

    }

    public static ArrayList<JeproLabLaboratoryGroupModel> getLabGroups(){
        return getLabGroups(true);
    }

    public static ArrayList<JeproLabLaboratoryGroupModel> getLabGroups(boolean activated){
        staticDataBaseObject = JeproLabDataBaseConnector.getDataBaseObject();
        return new ArrayList<>();
    }
}