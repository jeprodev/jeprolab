package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeproLabLanguageModel extends JeproLabModel{
    public int language_id;

    public String name;

    public JeproLabLanguageModel(int lang_id){

    }

    public static void loadLanguages() {

    }

    public static List getLanguages(){
        return new ArrayList<>();
    }

    public boolean isAssociatedToLab(){
        return  isAssociatedToLab(0);
    }

    public boolean isAssociatedToLab(int labId){
        if(labId == 0){
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        boolean isAssociated = false;

        String cacheId =  "jeprolab_model_lab_language_" + this.language_id + "_" + labId;
        if(!JeproLabCache.getInstance().isStored(cacheId)){
            JeproLabDataBaseConnector dataBaseConnector = JeproLabFactory.getDataBaseConnector();
            String query = "SELECT lab_id FROM " + dataBaseConnector.quoteName("#__jeprolab_language_lab") + " WHERE ";
            query += dataBaseConnector.quoteName("lang_id") + " = " + this.language_id + " AND " + dataBaseConnector.quoteName("lab_id") + labId;

            dataBaseConnector.setQuery(query);
            ResultSet object = dataBaseConnector.loadObject();
            try{
                while(object.next()){
                    isAssociated = object.getInt("lab_id") > 0;
                }
            }catch(SQLException exc){
                isAssociated = false;
            }
            JeproLabCache.getInstance().store(cacheId, isAssociated);
        }else{
            isAssociated = (boolean)JeproLabCache.getInstance().retrieve(cacheId);
        }
        return isAssociated;
    }
}