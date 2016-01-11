package com.jeprolab.models;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    protected static ArrayList<JeproLabLaboratoryGroupModel> labGroups;

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
     */
    public static String SHARE_CUSTOMER = "share_customer";
    public static String SHARE_REQUEST = "share_request";
    public static String SHARE_RESULTS = "share_results";

    public static int LAB_CONTEXT = 1;
    public static int GROUP_CONTEXT = 2;
    public static int ALL_CONTEXT = 4;

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
            try {
                dataBaseObject = JeproLabDataBaseConnector.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        try {
            staticDataBaseObject = JeproLabDataBaseConnector.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!feature_published){
            String query = "SELECT COUNT(*) FROM " + staticDataBaseObject.quoteName("#__jeprolab_lab");
            staticDataBaseObject.setQuery(query);

            int nbObject = 0;
            try {
                ResultSet data = staticDataBaseObject.loadObject();
                while (data.next()) {
                    nbObject++;
                }
            }catch (SQLException sqlE){}

            feature_published = (JeproLabSettingModel.getIntValue("multilab_feature_active") > 0) && (nbObject > 1);
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

    public static String addSqlRestriction(){
        return addSqlRestriction("");
    }

    public static String addSqlRestriction(String share){
        return addSqlRestriction(share, "");
    }

    public static String addSqlRestriction(String share, String alias){
        if(alias != null && !alias.equals("")){
            alias += ".";
        }

        //JeproLabLaboratoryGroupModel labGroup = JeproLabLaboratoryModel.getLabGroupFromLab(JeproLabLaboratoryModel.getContextLabID(), false);
        String restriction;
       //if (share.equals(JeproLabLaboratoryModel.SHARE_CUSTOMER) && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && labGroup.share_customer){
            restriction = " AND " + alias + "lab_group_id = " +  JeproLabLaboratoryModel.getContextLabGroupId();
        /* }else{
            restriction = " AND " + alias + "lab_id IN (" +  JeproLabLaboratoryModel.getContextListLabIds(share) + ") ";
        } */
        return restriction;
    }
/*
    public static String addSqlAssociation(String table){
        return addSqlAssociation(table, true);
    }

    public static String addSqlAssociation(String table, boolean innerJoin){
        return addSqlAssociation(table, innerJoin, null);
    }

    public static String addSqlAssociation(String table, boolean innerJoin, String onFilter){
        return addSqlAssociation(table, innerJoin, onFilter, false);
    }

    public static String addSqlAssociation(String table, boolean innerJoin, String onFilter, boolean forceNotDefault){
        JeproLabDataBaseConnector dbc = JeproLabFactory.getDataBaseConnector();
        String tableAlias = table + "_lab";
        String outputAlias;
        if(strpos($table, ".") !== false){
            list($tableAlias, $table) = table.split(".");
        }

        if(table.equals("group")){ outputAlias = "grp"; }
        else{ outputAlias = table; }

        associatedTable = JeproLabLaboratoryModel.getAssociatedTable(table);
        if(associatedTable == null || associatedTable.type != "lab"){ return ""; }

        String query = ((innerJoin) ? " INNER " : " LEFT ") + "JOIN " + dbc.quoteName("#__jeprolab_" + table + "_lab") + " AS ";
        query += tableAlias + " ON( " + tableAlias + "." +  table + "_id = " + outputAlias + "."  + table + "_id";

        if((int)JeproLabLaboratoryModel.context_lab_id){
            query += " AND " + tableAlias + ".lab_id = " + (int)JeproLabLaboratoryModel.context_lab_id;
        }else if(JeproLabLaboratoryModel.checkDefaultLabId(table) && !forceNotDefault){
            query += " AND " + tableAlias + ".lab_id = " + outputAlias + ".default_lab_id";
        }else{
            query += " AND " + tableAlias + ".lab_id IN (" . implode(', ', JeproLabLaboratoryModel.getContextListLabIds()) + ")" ;
        }
        query += ((onFilter != null && !onFilter.equals("")) ? " AND " + onFilter : "" ) + ")";
        return query;
    }
*/
    public static List getContextListLabIds(){
        return getContextListLabIds("");
    }

    public static List getContextListLabIds(String share){
        List list;
        if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
            list = (share != null && !share.equals("")) ? JeproLabLaboratoryModel.getSharedLabs(JeproLabLaboratoryModel.getContextLabId(), share) : new ArrayList(JeproLabLaboratoryModel.getContextLabId());
        } else if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT) {
            list = JeproLabLaboratoryModel.getLabs(true, JeproLabLaboratoryModel.getContextLabGroupId(), true);
        }else{
            list = JeproLabLaboratoryModel.getLabs(true, 0, true);
        }
        return list;
    }

    public static boolean checkDefaultLabId(String table){
        if(!JeproLabLaboratoryModel.isInitialized){
            JeproLabLaboratoryModel.init();
        }
        return JeproLabLaboratoryModel.defaultLabTablesId.contains(table);
    }

    public static List getLabs(){
        return getLabs(true);
    }

    public static List getLabs(boolean published){
        return getLabs(published, 0);
    }

    public static List getLabs(boolean published, int labGroupId){
        return getLabs(published, labGroupId, false);
    }

    public static List getLabs(boolean published, int labGroupId, boolean getAsListIds){
        JeproLabLaboratoryModel.cacheLabs();

        ArrayList results = new ArrayList();
        /*todoforeach (JeproLabLaboratoryModel.labs as $group_id => $group_data){
            foreach ($group_data['labs'] as $lab_id => $lab_data){
                if((!$published || $lab_data->published) && (!$lab_group_id || $lab_group_id == $group_id)){
                    if ($get_as_list_id){
                        $results[$lab_id] = $lab_id;
                    }else{
                        $results[$lab_id] = $lab_data;
                    }
                }
            }
        }*/
        return results;
    }

    public static List getSharedLabs(int labId, String labType){
        if (!labType.equals(JeproLabLaboratoryModel.SHARE_CUSTOMER) || !labType.equals(JeproLabLaboratoryModel.SHARE_REQUEST) || !labType.equals(JeproLabLaboratoryModel.SHARE_RESULTS)){
            //die('Wrong argument ($type) in Lab::getSharedLabs() method');
        }

        JeproLabLaboratoryModel.cacheLabs();
        for(JeproLabLaboratoryGroupModel laboratoryGroup : JeproLabLaboratoryModel.labGroups){
            /* todoif (array_key_exists($lab_id, $group_data['labs']) && $group_data[$type]){
                return array_keys($group_data['labs']);
            }*/
        }
        return new ArrayList(labId);
    }

    public static void cacheLabs(){
        cacheLabs(false);
    }

    public static void cacheLabs(boolean refresh){
        if(!(!(JeproLabLaboratoryModel.labGroups == null) || !JeproLabLaboratoryModel.labGroups.isEmpty()) && !refresh) {
            JeproLabLaboratoryModel.labGroups = new ArrayList();
            JeproLabDataBaseConnector dbc = JeproLabFactory.getDataBaseConnector();
            String from = "";
            String where = "";

            JeproLabEmployeeModel employee = JeproLabContext.getContext().employee;

            // If the profile isn't a superAdmin
            if (JeproLabTools.isLoadedObject(employee, "employee_id")){ // && employee.profile_id != _PS_ADMIN_PROFILE_){
                from += " LEFT JOIN " + dbc.quoteName("#__jeprolab_employee_lab") + " AS employee_lab ON employee_lab.lab_id = lab.lab_id";
                where += " AND employee_lab.employee_id = " + employee.employee_id;
            }

            String query = "SELECT lab_group.*, lab.*, lab_group.lab_group_name AS group_name, lab.lab_name AS lab_name, lab.published, ";
            query += "lab_url.domain, lab_url.ssl_domain, lab_url.physical_uri, lab_url.virtual_uri FROM " + dbc.quoteName("#__jeprolab_lab_group");
            query += " AS lab_group LEFT JOIN " + dbc.quoteName("#__jeprolab_lab") + " AS lab ON lab.lab_group_id = lab_group.lab_group_id  LEFT JOIN ";
            query += dbc.quoteName("#__jeprolab_lab_url") + " AS lab_url ON lab.lab_id = lab_url.lab_id AND lab_url.main = 1 " + from + " WHERE lab.deleted";
            query += " = 0 AND lab_group.deleted = 0 " + where + " ORDER BY lab_group.lab_group_name, lab.lab_name";

            dbc.setQuery(query);
            ResultSet results = dbc.loadObject();

            try {
                JeproLabLaboratoryGroupModel labGroup;
                while(results.next()){ System.out.print(results);
                    /*if(!JeproLabLaboratoryModel.labGroups.contains(results)){
                        labGroup = new JeproLabLaboratoryGroupModel();
                        labGroup.laboratory_group_id = results.getInt("lab_group_id");
                        labGroup.name = results.getString("group_name");
                        labGroup.share_customers = results.getInt("share_customer");
                        labGroup.share_results = results.getInt("share_results");
                        labGroup.share_requests = results.getInt("share_request");
                        labGroup.laboratories = new ArrayList<>();
                    }
                    /*foreach($results as $row) {
                        if (!isset(self::$labs[$row -> lab_group_id])){
                            self::$labs[$row -> lab_group_id]=array(
                                    'lab_group_id' = > $row -> lab_group_id,
                                    'name' =>$row -> group_name,
                                    'share_customer' =>$row -> share_customer,
                                    'share_orders' =>$row -> share_orders,
                                    'share_results' =>$row -> share_results,
                                    'labs' =>array()
                            );

                            self::$labs[$row -> lab_group_id]['labs'][$row -> lab_id]=$row; /*array(
							'lab_id' => $row->lab_id,
							'lab_group_id' => $row->lab_group_id,
							'name' => $row->lab_name,
							'theme_id' => $row->theme_id,
							'category_id' => $row->category_id,
							'domain' => $row->domain,
							'ssl_domain' =>	$row->ssl_domain,
							'uri' =>  $row->physical_uri . $row->virtual_uri,
							'published' => $row->published
					);* /
                        }
                    }*/
                }
            }catch (SQLException sqlExc){}
        }
    }

    public static int getLabGroupFromLab(int labId, boolean asIds){
        JeproLabLaboratoryModel.cacheLabs();
        for(JeproLabLaboratoryGroupModel lab : JeproLabLaboratoryModel.labGroups){

        }
        return 0;
    }

    public static int getContextLabID(){
        return getContextLabID(false);
    }

    public static int getContextLabID(boolean nullValueWithoutMultiLab){
        if(nullValueWithoutMultiLab && !JeproLabLaboratoryModel.isFeaturePublished()){
            return 0;
        }
        return JeproLabLaboratoryModel.contextLabId;
    }
}