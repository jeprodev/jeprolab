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
import java.util.ListIterator;

public class JeproLabLaboratoryModel  extends JeproLabModel{
    public int laboratory_id = 0;

    public int laboratory_group_id = 0;

    public int lang_id = 0;

    public int category_id = 0;

    public JeproLabLaboratoryGroupModel laboratory_group;

    public String name;

    public int theme_id = 0;

    public String theme_name;

    public String theme_directory = "default";

    public String physical_uri;

    public String virtual_uri;

    public String domain;

    public String ssl_domain;

    private boolean multiLang = true;

    public boolean published;

    public boolean deleted;

    protected static ArrayList<JeproLabLaboratoryGroupModel> labGroups;

    protected static boolean isInitialized = false;

    protected static int context_laboratory_id;

    private static ArrayList<String> defaultLabTablesId = new ArrayList<>();
    private static ArrayList<String> associatedTables = new ArrayList<>();
    /**
     * @var int Stores the current lab context
     */
    private static int labContext;

    private static int contextLabId;
    private static int contextLabGroupId;

    private static JeproLabLaboratoryGroupModel context_laboratory_group;

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
        if( langId > 0){
            this.lang_id = (JeproLabLanguageModel.checkLanguage(langId)) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if(laboratoryId > 0){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            /** Loading laboratory from database if id is supplied **/
            String cacheKey = "jeprolab_laboratory_model_" + laboratoryId + ((langId > 0) ? "_" + langId : "");
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT laboratory.*, laboratory_url.physical_uri as physical_uri, laboratory_url.virtual_uri as virtual_uri, ";
                query += "laboratory_url.domain as domain, laboratory_url.ssl_domain as ssl_domain, theme.theme_id as theme_id, theme.theme_name";
                query += " as theme_name, theme.directory as theme_directory FROM " + dataBaseObject.quoteName("#__jeprolab_lab") + " AS laboratory ";
                query += "LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_lab_url") + " AS laboratory_url ON (laboratory_url.lab_id = laboratory.";
                query += "lab_id) LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_theme") + " AS theme ON (theme.theme_id = laboratory.theme_id)";
                query += " WHERE laboratory." + dataBaseObject.quoteName("lab_id")  + " = " + laboratoryId;

                dataBaseObject.setQuery(query);
                ResultSet laboratoryData = dataBaseObject.loadObject();
                try {
                    while(laboratoryData.next()) {
                        this.laboratory_id = laboratoryData.getInt("laboratory_id");
                        this.laboratory_group_id = laboratoryData.getInt("laboratory_group_id");
                        this.category_id = laboratoryData.getInt("category_id");
                        this.theme_id = laboratoryData.getInt("theme_id");
                        this.theme_name = laboratoryData.getString("theme_name");
                        this.theme_directory = laboratoryData.getString("theme_directory");
                        this.name = laboratoryData.getString("name");
                        this.published = laboratoryData.getInt("published") > 1;
                        this.deleted = laboratoryData.getInt("deleted") > 1;
                        this.physical_uri = laboratoryData.getString("physical_uri");
                        this.virtual_uri = laboratoryData.getString("virtual_uri");
                        this.domain = laboratoryData.getString("domain");
                        this.ssl_domain = laboratoryData.getString("ssl_domain");
                    }
                    JeproLabCache.getInstance().store(cacheKey, this);
                }catch(SQLException ignored){

                }
            }else{
                JeproLabLaboratoryModel laboratory = (JeproLabLaboratoryModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.laboratory_id = laboratory.laboratory_id;
                this.laboratory_group_id = laboratory.laboratory_group_id;
                this.category_id = laboratory.category_id;
                this.theme_id = laboratory.theme_id;
                this.theme_name = laboratory.theme_name;
                this.theme_directory = laboratory.theme_directory;
                this.name = laboratory.name;
                this.published = laboratory.published;
                this.deleted = laboratory.deleted;
                this.physical_uri = laboratory.physical_uri;
                this.virtual_uri = laboratory.virtual_uri;
                this.domain = laboratory.domain;
                this.ssl_domain = laboratory.ssl_domain;
            }
        }
    }


    public static JeproLabLaboratoryModel initialize() {
        int labId = 0;
        if(JeproLabContext.getContext().laboratory != null) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        if(labId <= 0) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            //find current lab from url
            String foundedUri = "";
            String host = "";
            //$request_uri = rawurldecode($_SERVER['REQUEST_URI']);

            String query = "SELECT lab." + staticDataBaseObject.quoteName("lab_id") +  ", CONCAT(lab_url." + staticDataBaseObject.quoteName("physical_uri");
            query += ", lab_url." + staticDataBaseObject.quoteName("virtual_uri") + ") AS uri, lab_url." + staticDataBaseObject.quoteName("domain") +  ", lab_url.";
            query += staticDataBaseObject.quoteName("main") +  " FROM " + staticDataBaseObject.quoteName("#__jeprolab_lab_url") + " AS lab_url LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_lab") + " AS lab ON (lab.lab_id = lab_url.lab_id) WHERE (lab_url.domain = ";
            query += staticDataBaseObject.quote(host) + " OR lab_url.ssl_domain = " + staticDataBaseObject.quote(host) + ") AND lab.published = 1 AND ";
            query += "lab.deleted = 0 ORDER BY LENGTH (CONCAT(lab_url.physical_uri, lab_url.virtual_uri)) DESC";

            staticDataBaseObject.setQuery(query);
            ResultSet results = staticDataBaseObject.loadObject();
            boolean through = false;
            List resultLabs = new ArrayList<>();
            boolean isMainUri = false;

            try {
                while(results.next()) {
                    //if (preg_match('#^'.preg_quote($result -> uri, '#'). '#i', $request_uri)){
                        through = true;
                        labId = results.getInt("lab_id");
                        foundedUri = results.getString("uri");
                        if (results.getInt("main") > 0) {
                            isMainUri = true;
                        }
                        break;
                    //}
                }
            }catch(SQLException ignored){

            }
            /** If an URL was found and it's not the main URL, redirect to main url  **/
            /* if (through && labId > 0 && !isMainUri) {
                foreach($results as $result) {
                    if (results.getInt("lab_id") == labId && $result -> main) {
                        $request_uri = substr($request_uri, strlen($found_uri));
                        $url = str_replace('//', '/', $result -> domain.$result->uri.$request_uri);
                        String redirectType = JeproLabSettingModel.getIntValue("canonical_redirect") == 2 ? "301" : "302";

                        System.exit(0);
                    }
                }
            }*/

        }

        JeproLabLaboratoryModel laboratory;

        if(labId <= 0){
            laboratory = new JeproLabLaboratoryModel(labId);
        }else{
            laboratory = new JeproLabLaboratoryModel(labId);
            JeproLabLaboratoryModel defaultLab;
            if(laboratory.laboratory_id < 0 || !laboratory.published){
                defaultLab = new JeproLabLaboratoryModel(JeproLabSettingModel.getIntValue("default_lab"));
                if(defaultLab.laboratory_id <= 0){
                    JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_NO_LABORATORY_FOUND_MESSAGE"));
                }
            }else if(laboratory.physical_uri.equals("")){
                defaultLab = new JeproLabLaboratoryModel(JeproLabSettingModel.getIntValue("default_lab"));
                laboratory.physical_uri = defaultLab.physical_uri;
                laboratory.virtual_uri = defaultLab.virtual_uri;
            }
        }

        JeproLabLaboratoryModel.context_laboratory_id = laboratory.laboratory_id;
        JeproLabLaboratoryModel.contextLabGroupId = laboratory.laboratory_group_id;
        JeproLabLaboratoryModel.labContext = JeproLabLaboratoryModel.LAB_CONTEXT;
        return laboratory;
    }

    private static void init(){
        JeproLabLaboratoryModel.defaultLabTablesId.add("analyze");
        JeproLabLaboratoryModel.defaultLabTablesId.add("category");
        associatedTables.add("analyze");
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
        return associatedTables.contains(table);
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
            }catch (SQLException sqlE){sqlE.printStackTrace();}

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

        JeproLabLaboratoryGroupModel labGroup = JeproLabLaboratoryModel.getLaboratoryGroupFromLaboratory(JeproLabLaboratoryModel.getContextLaboratoryId());
        String restriction = "";
        if (share.equals(JeproLabLaboratoryModel.SHARE_CUSTOMER) && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && labGroup.share_customers){
            restriction += " AND " + alias + "lab_group_id = " +  JeproLabLaboratoryModel.getContextLabGroupId();
        }else{
            List<Integer> listRestriction = JeproLabLaboratoryModel.getContextListLaboratoryIds(share);
            int listSize = listRestriction.size();
            if(listSize > 0) {
                String labIds = "";
                for(int labId : listRestriction){
                    labIds += labId + ", ";
                }
                labIds = labIds.substring(0, labIds.length() - 3);
                restriction = " AND " + alias + "lab_id IN (" + labIds +") ";
            }
        }
        return restriction;
    }

    public static String addSqlRestrictionOnLang(){
        return addSqlRestrictionOnLang("", 0);
    }

    public static String addSqlRestrictionOnLang(String alias){
        return addSqlRestrictionOnLang(alias, 0);
    }

    public static String addSqlRestrictionOnLang(String alias, int labId){
        if((JeproLabContext.getContext().laboratory != null) && labId <= 0){
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        if(labId <= 0){
            labId = JeproLabSettingModel.getIntValue("default_lab");
        }
       if(staticDataBaseObject == null){
           staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
       }
        return " AND " + (!alias.equals("") ? alias + "." : "") + staticDataBaseObject.quoteName("lab_id") + " = " + labId;
    }

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
        if(table.contains(".")){
            tableAlias = table.substring(0, table.indexOf("."));
            table = table.substring(table.indexOf("."), table.length() - 1);
        }

        if(table.equals("group")){ outputAlias = "grp"; }
        else if(table.equals("analyze")){ outputAlias = "analze"; }
        else{ outputAlias = table; }

        boolean isAssociatedToTable = JeproLabLaboratoryModel.getAssociatedTable(table);
        if(!isAssociatedToTable){ return ""; }

        String query = ((innerJoin) ? " INNER " : " LEFT ") + "JOIN " + dbc.quoteName("#__jeprolab_" + table + "_lab") + " AS ";
        query += tableAlias + " ON( " + tableAlias + "." +  table + "_id = " + outputAlias + "."  + table + "_id";

        if(JeproLabLaboratoryModel.context_laboratory_id > 0){
            query += " AND " + tableAlias + ".lab_id = " + JeproLabLaboratoryModel.context_laboratory_id;
        }else if(JeproLabLaboratoryModel.checkDefaultLabId(table) && !forceNotDefault){
            query += " AND " + tableAlias + ".lab_id = " + outputAlias + ".default_lab_id";
        }else{
            //query += " AND " + tableAlias + ".lab_id IN (" . implode(', ', JeproLabLaboratoryModel.getContextListLabIds()) + ")" ;
        }
        query += ((onFilter != null && !onFilter.equals("")) ? " AND " + onFilter : "" ) + ")";
        return query;
    }

    public static boolean getAssociatedTable(String table){
        if(!JeproLabLaboratoryModel.isInitialized){
            JeproLabLaboratoryModel.init();
        }
        return JeproLabLaboratoryModel.associatedTables.contains(table);
    }

    public static List<Integer> getContextListLaboratoryIds(){
        return getContextListLaboratoryIds("");
    }

    public static List<Integer> getContextListLaboratoryIds(String share){
        List<Integer> list = new ArrayList<>();
        if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
            if(share != null && !share.equals("")) {
                list = JeproLabLaboratoryModel.getSharedLaboratories(JeproLabLaboratoryModel.getContextLabId(), share);
            }else{
                list.add(JeproLabLaboratoryModel.getContextLabId());
            }
        } else if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT) {
            list = JeproLabLaboratoryModel.getLaboratoryIds(true, JeproLabLaboratoryModel.getContextLabGroupId());
        }else{
            list = JeproLabLaboratoryModel.getLaboratoryIds(true, 0);
        }
        return list;
    }

    public static boolean checkDefaultLabId(String table){
        if(!JeproLabLaboratoryModel.isInitialized){
            JeproLabLaboratoryModel.init();
        }
        return JeproLabLaboratoryModel.defaultLabTablesId.contains(table);
    }

    public static List<Integer> getLaboratoryIds(){
        return getLaboratoryIds(true);
    }

    public static List<Integer> getLaboratoryIds(boolean published){
        return getLaboratoryIds(published, 0);
    }

    public static List<Integer> getLaboratoryIds(boolean published, int labGroupId){
        JeproLabLaboratoryModel.cacheLaboratories();

        List results = new ArrayList();
        for(JeproLabLaboratoryGroupModel labGroup : JeproLabLaboratoryGroupModel.getLaboratoryGroups()){
            for(JeproLabLaboratoryModel lab : labGroup.laboratories){
                if ((!published || lab.published) && (labGroup.laboratory_group_id < 0 || labGroup.laboratory_group_id == labGroupId)) {
                    results.add(lab.laboratory_id);

                }
            }
        }
        return results;
    }

    public static List<JeproLabLaboratoryModel> getLaboratories(){
        return getLaboratories(true);
    }

    public static List<JeproLabLaboratoryModel> getLaboratories(boolean published){
        return getLaboratories(published, 0);
    }

    public static List<JeproLabLaboratoryModel> getLaboratories(boolean published, int labGroupId){
        return getLaboratories(published, labGroupId, false);
    }

    public static List<JeproLabLaboratoryModel> getLaboratories(boolean published, int labGroupId, boolean getAsListIds){
        JeproLabLaboratoryModel.cacheLaboratories();

        List<JeproLabLaboratoryModel> results = new ArrayList();
        /*todo foreach (JeproLabLaboratoryModel.labs as $group_id => $group_data){
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

    public static List<Integer> getSharedLaboratories(int labId, String labType){
        if (!labType.equals(JeproLabLaboratoryModel.SHARE_CUSTOMER) || !labType.equals(JeproLabLaboratoryModel.SHARE_REQUEST) || !labType.equals(JeproLabLaboratoryModel.SHARE_RESULTS)){
            //die('Wrong argument ($type) in Lab::getSharedLabs() method');
        }

        JeproLabLaboratoryModel.cacheLaboratories();
        for(JeproLabLaboratoryGroupModel laboratoryGroup : JeproLabLaboratoryModel.labGroups){
            /* todo if (array_key_exists($lab_id, $group_data['labs']) && $group_data[$type]){
                return array_keys($group_data['labs']);
            }*/
        }
        return new ArrayList<>(labId);
    }

    public static void cacheLaboratories(){
        cacheLaboratories(false);
    }

    public static void cacheLaboratories(boolean refresh){
        if(JeproLabLaboratoryModel.labGroups == null){
            JeproLabLaboratoryModel.labGroups = new ArrayList();
        }
        if((JeproLabLaboratoryModel.labGroups.isEmpty())  || refresh) {

            if(staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String from = "";
            String where = "";

            JeproLabEmployeeModel employee = JeproLabContext.getContext().employee;

            //to do set back with login If the profile isn't a superAdmin
            /*if (employee.employee_id > 0){ // && employee.profile_id != _PS_ADMIN_PROFILE_){
                from += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_employee_lab") + " AS employee_lab ON employee_lab.lab_id = lab.lab_id";
                where += " AND employee_lab.employee_id = " + employee.employee_id;
            } */

            String query = "SELECT lab_group.*, lab.*, lab_group.lab_group_name AS group_name, lab.lab_name AS lab_name, lab.published, ";
            query += "lab_url.domain, lab_url.ssl_domain, lab_url.physical_uri, lab_url.virtual_uri FROM " + staticDataBaseObject.quoteName("#__jeprolab_lab_group");
            query += " AS lab_group LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_lab") + " AS lab ON lab.lab_group_id = lab_group.lab_group_id  LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_lab_url") + " AS lab_url ON lab.lab_id = lab_url.lab_id AND lab_url.main = 1 " + from + " WHERE lab.deleted";
            query += " = 0 AND lab_group.deleted = 0 " + where + " ORDER BY lab_group.lab_group_name, lab.lab_name";

            staticDataBaseObject.setQuery(query);
            ResultSet results = staticDataBaseObject.loadObject();

            try {
                JeproLabLaboratoryGroupModel labGroup;
                while(results.next()){
                    int labGroupId = results.getInt("lab_group_id");
                    if(!JeproLabLaboratoryModel.labGroups.contains(labGroupId)){
                        labGroup = new JeproLabLaboratoryGroupModel();
                        labGroup.laboratory_group_id = results.getInt("lab_group_id");
                        labGroup.name = results.getString("group_name");
                        labGroup.share_customers = results.getInt("share_customer") > 0;
                        labGroup.share_results = results.getInt("share_results") > 0;
                        labGroup.share_requests = results.getInt("share_request") > 0;
                        labGroup.laboratories = new ArrayList<>();
                        labGroups.add(labGroupId, labGroup);
                    }

                    JeproLabLaboratoryGroupModel labGroupModel = labGroups.get(labGroupId);
                    JeproLabLaboratoryModel lab = new JeproLabLaboratoryModel();
                    lab.laboratory_id  = results.getInt("lab_id");
                    /*lab = results.get("");
                    lab = results.get("");
                    lab = results.get("");
                    lab = results.get("");
                    lab = results.get("");
                    lab = results.get("");
                    lab.published = results.getInt("published") > 0; */
                    labGroupModel.laboratories.add(lab);
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
            }catch (SQLException ignored){}
        }
    }

    public static int getLaboratoryGroupIdFromLaboratory(int labId){
        return getLaboratoryGroupFromLaboratory(labId).laboratory_group_id;
    }

    public static JeproLabLaboratoryGroupModel getLaboratoryGroupFromLaboratory(int labId){
        JeproLabLaboratoryModel.cacheLaboratories();
        for(JeproLabLaboratoryGroupModel labGroup : JeproLabLaboratoryModel.labGroups){
            if(labGroup.laboratories.contains(labId)){
                return labGroup;
            }
        }
        return new JeproLabLaboratoryGroupModel();
    }

    public static int getContextLaboratoryId(){
        return getContextLaboratoryId(false);
    }

    public static int getContextLaboratoryId(boolean nullValueWithoutMultiLab){
        if(nullValueWithoutMultiLab && !JeproLabLaboratoryModel.isFeaturePublished()){
            return JeproLabSettingModel.getIntValue("default_lab");
        }
        return JeproLabLaboratoryModel.contextLabId;
    }

    /**
     * Get group of current shop
     *
     * @return JeproLabLaboratoryGroupModel
     */
    public JeproLabLaboratoryGroupModel getLaboratoryGroup(){
        if (this.laboratory_group == null) {
            this.laboratory_group = new JeproLabLaboratoryGroupModel(this.laboratory_group_id);
        }
        return this.laboratory_group;
    }

    public static JeproLabLaboratoryGroupModel getContextLaboratoryGroup(){
        if (context_laboratory_group == null) {
            context_laboratory_group = new JeproLabLaboratoryGroupModel(contextLabGroupId);
        }
        return context_laboratory_group;
    }
}