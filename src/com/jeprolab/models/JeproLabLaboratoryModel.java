package com.jeprolab.models;


import jeprolab.JeproLab;
import jeprolab.assets.tools.JeproLabCache;
import jeprolab.assets.tools.JeproLabDataBaseConnector;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class JeproLabLaboratoryModel  extends JeproLabModel{
    public int laboratory_id = 0;

    public int laboratory_group_id = 0;

    public int lang_id = 0;

    public int category_id = 0;

    public String name;

    public int theme_id = 0;

    public String theme_name;
    public String theme_directory = "default";

    private boolean multiLang = true;

    protected static boolean isInitialized = false;

    private static ArrayList<String> defaultLabTablesId = new ArrayList<>();
    private static ArrayList<String> associatedTables = new ArrayList<>();
    /**
     * @var int Stores the current lab context
     */
    private static int labContext;

    private static int contextLabId;
    private static int contextLabGroupId;

    private static boolean feature_published = false;

    /**
     * some data can be shared between laboratories, like customers, request and results
     * /
    const SHARE_CUSTOMER = "share_customer";
    const SHARE_REQUEST = "share_request";
    const SHARE_RESULTS = "share_results";

    const LAB_CONTEXT = 1;
    const GROUP_CONTEXT = 2;
    const ALL_CONTEXT = 4;
*/
    public JeproLabLaboratoryModel(){
        this(0, 0);
    }

    public JeproLabLaboratoryModel(int laboratoryId){
        this(laboratoryId, 0);
    }

    public JeproLabLaboratoryModel(int laboratoryId, int langId){
        if(langId != 0 && langId > 0){
            //this.lang_id = (JeproLabLanguageModel.getLanguage(langId) != null) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if(laboratoryId > 0){
            dataBaseObject = JeproLabDataBaseConnector.getDataBaseObject();
            /** Loading laboratory from database if id is supplied ** /
            String cache_id = "jeprolab_laboratory_model_" + laboratoryId + ((langId > 0) ? "_" + langId : "");
            if(!JeproLabCache.isStored(cache_id)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_lab") + " AS laboratory";
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_lab_lang") + " AS laboratory_lang ON (laboratory.";
                    query += dataBaseObject.quoteName("lab_id") + " = laboratory_lang." + dataBaseObject.quoteName("lab_id") ;
                    query += " AND laboratory_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ")";
                }
                query += " WHERE laboratory." + dataBaseObject.quoteName("lab_id")  + " = " + laboratoryId;
                dataBaseObject.setQuery(query);
                ResultSet laboratoryData = dataBaseObject.loadObject();

                if(laboratoryData != null){
                    if((langId > 0) && this.multiLang){
                        query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_lab_lang") + " WHERE " ;
                        query += dataBaseObject.quoteName("lab_id") + " = " + laboratoryId;
                        dataBaseObject.setQuery(query);
                        ResultSet laboratoryDataLang = dataBaseObject.loadObjectList();
                    }
                }
            } */
        }
    }
    public static JeproLabLaboratoryModel initialize() {
        return null;
    }

    private static void init(){
        JeproLabLaboratoryModel.defaultLabTablesId.add("analyze");
        JeproLabLaboratoryModel.defaultLabTablesId.add("category");
        associatedTables.add("category");
        associatedTables.add("contact");
        associatedTables.add("country");
        associatedTables.add("currency");
        associatedTables.add("employee");
        associatedTables.add("image");
        associatedTables.add("lang");
        associatedTables.add("analyze");
        associatedTables.add("referrer");
        associatedTables.add("group");
        associatedTables.add("customer");
        associatedTables.add("tax_rules_group");
        associatedTables.add("zone");
        associatedTables.add("technician");

        for(String associatedTable : associatedTables){
            JeproLabLaboratoryModel.addTableAssociation(associatedTable);
        }

        JeproLabLaboratoryModel.isInitialized = true;
    }

    public static int getLabContext(){
        return labContext;
    }

    /**
     * Add table associated to laboratory
     *
     */
    private static boolean addTableAssociation(String associatedTable){
        return false;
    }

    public static boolean isTableAssociated(String table){
        if(!JeproLabLaboratoryModel.isInitialized){
            JeproLabLaboratoryModel.init();
        }
        return false;
    }
    /**
     * Get the current id of laboratory group if context is LAB_CONTEXT or GROUP_CONTEXT
     *
     * @return int
     */
    public static int getContextLabId(){
        return getContextLabId(false);
    }

    /**
     * Get the current id of laboratory group if context is LAB_CONTEXT or GROUP_CONTEXT
     * @param nullValueWithoutMultiLab boolean
     * @return int
     */

    public static int getContextLabId(boolean nullValueWithoutMultiLab){
        if(nullValueWithoutMultiLab && !JeproLabLaboratoryModel.isFeaturePublished()){
            return 0;
        }
        return JeproLabLaboratoryModel.contextLabId;
    }

    /**
     * Get the current id of laboratory group if context is LAB_CONTEXT or GROUP_CONTEXT
     *
     * @return int
     */
    public static int getContextLabGroupId(){
        return getContextLabGroupId(false);
    }

    /**
     * Get the current id of laboratory group if context is LAB_CONTEXT or GROUP_CONTEXT
     * @param nullValueWithoutMultiLab boolean
     * @return int
     */
    public static int getContextLabGroupId(boolean nullValueWithoutMultiLab){
        if(nullValueWithoutMultiLab && !JeproLabLaboratoryModel.isFeaturePublished()){
            return 0;
        }
        return JeproLabLaboratoryModel.contextLabGroupId;
    }

    public static boolean isFeaturePublished(){
        staticDataBaseObject = JeproLabDataBaseConnector.getDataBaseObject();
        if(!feature_published){
            String query = "SELECT COUNT(*) FROM " + staticDataBaseObject.quoteName("#__jeprolab_lab");
            //staticDataBaseObject.setQuery(query);

            //feature_published = JeproLabSettingModel.getIntValue("multilab_feature_active") && (staticDataBaseObject.loadResult() > 1);
        }

        return feature_published;
    }

    /**
     * get root category id of the laboratory
     * @return category_id int root category id
     */
    public int getCategoryId(){
        return (this.category_id > 0 ) ? this.category_id : 1;
    }
}