package com.jeprolab.models;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.*;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

public class JeproLabCategoryModel extends JeproLabModel {
    public int category_id;

    public int language_id;

    public int laboratory_id;

    public Map<String, String> name;

    public boolean published = true;

    public int position;

    public Map<String, String> description;

    public int parent_id;

    public int depth_level;

    public int n_left;

    public int n_right;

    public List<Integer> laboratory_list_ids = new ArrayList<>();

    private int totalAnalyzes;

    public Map<String, String> link_rewrite;

    public Map<String, String> meta_title;
    public Map<String, String> meta_keywords;
    public Map<String, String> meta_description;

    public Date date_add;
    public Date date_upd;

    //public $lab_list_ids;

    public boolean is_root_category;
    public boolean getLabFromContext = false;

    public int default_laboratory_id;

    public int groupBox[];

    private Pagination pagination;

    public String image_path = "default";
    public String image_dir = "";

    public static boolean multiLang = true;
    public static boolean multiLangLab = true;


    protected boolean deleted = true;

    protected boolean do_not_regenerate_nested_tree = true;

    private Map<Integer,JeproLabLanguageModel> languages;

    protected static Map<String, String> _links =new HashMap<>();

    public JeproLabCategoryModel() {
        this(0, 0, 0);
    }

    public JeproLabCategoryModel(int categoryId) {
        this(categoryId, 0, 0);
    }

    public JeproLabCategoryModel(int categoryId, int langId) {
        this(categoryId, langId, 0);
    }

    public JeproLabCategoryModel(int categoryId, int langId, int labId) {

        if(langId > 0){
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId)) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if((labId > 0) && this.isMultiLab()){
            this.laboratory_id = labId;
            this.getLabFromContext = false;
        }

        if(this.isMultiLab() && (this.laboratory_id <= 0)){
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        name = new HashMap<>();
        description = new HashMap<>();
        link_rewrite = new HashMap<>();
        meta_title = new HashMap<>();
        meta_keywords = new HashMap<>();
        meta_description = new HashMap<>();
        Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

        if(categoryId > 0){
            /** load category from data base if id provided **/
            String cacheKey = "jeprolab_model_category_" + categoryId + "_" + langId + "_" + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
                String where = "";
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (";
                    query += "category.category_id = category_lang.category_id AND category_lang.lang_id = " + langId + ")";
                    if((this.laboratory_id > 0)&& JeproLabCategoryModel.multiLangLab){
                        where += " AND category_lang.lab_id = " +  this.laboratory_id;
                    }
                }
                /** Get Lab information **/
                if(JeproLabLaboratoryModel.isTableAssociated("category")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " AS lab ON ( category.";
                    query += "category_id = lab.category_id AND lab.lab_id = " + this.laboratory_id + ")";
                }
                query += " WHERE category.category_id = " + categoryId + where;

                dataBaseObject.setQuery(query);
                ResultSet categoryData = dataBaseObject.loadObject();
                try{
                    //JeproLabCategoryModel category = new JeproLabCategoryModel();
                    while(categoryData.next()){
                        this.category_id = categoryData.getInt("category_id");
                        this.parent_id = categoryData.getInt("parent_id");
                        this.default_laboratory_id = categoryData.getInt("default_lab_id");
                        this.depth_level = categoryData.getInt("depth_level");
                        this.n_left = categoryData.getInt("n_left");
                        this.n_right = categoryData.getInt("n_right");
                        this.published = categoryData.getInt("published") > 0;
                        this.date_add = categoryData.getDate("date_add");
                        this.date_upd = categoryData.getDate("date_upd");
                        this.position = categoryData.getInt("position");
                        this.is_root_category = categoryData.getInt("is_root") > 0;
                        if(langId <= 0){
                            query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + categoryId;
                            query += ((this.laboratory_id > 0 && this.isLangMultiLab()) ? " AND " + dataBaseObject.quoteName("lab_id") + " = " + this.laboratory_id : "");

                            dataBaseObject.setQuery(query);
                            ResultSet categoryLangData = dataBaseObject.loadObject();
                            while(categoryLangData.next()){
                                int languageId = categoryLangData.getInt("lang_id");
                                String categoryName = categoryLangData.getString("name");
                                String categoryDescription = categoryLangData.getString("description");
                                String linkRewrite = categoryLangData.getString("link_rewrite");
                                String metaTitle = categoryLangData.getString("meta_title");
                                String metaKeywords = categoryLangData.getString("meta_keywords");
                                String metaDescription = categoryLangData.getString("meta_description");
                                Iterator langIt = languages.entrySet().iterator();
                                while(langIt.hasNext()){
                                    Map.Entry lang = (Map.Entry)langIt.next();
                                    JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
                                    if(langId == language.language_id){
                                        this.name.put("lang_" + languageId, categoryName);
                                        this.description.put("lang_" + languageId, categoryDescription);
                                        this.link_rewrite.put("lang_" + languageId, linkRewrite);
                                        this.meta_title.put("lang_" + languageId, metaTitle);
                                        this.meta_keywords.put("lang_" + languageId, metaKeywords);
                                        this.meta_description.put("lang_" + languageId, metaDescription);
                                    }
                                }
                                /*foreach($category_lang_data as $row){
                                    foreach ($row as $key => $value){
                                        if(array_key_exists($key, $this) && $key != "category_id"){
                                            if(!isset($category_data->{$key}) || !is_array($category_data->{$key})){
                                                $category_data->{$key} = array();
                                            }
                                            $category_data->{$key}[$row->lang_id] = $value;
                                        }
                                    }
                                }*/
                            }
                        }else {
                            /** set data for a given  language **/

                        }

                    }
                    JeproLabCache.getInstance().store(cacheKey, this);
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }
            }else{
                JeproLabCategoryModel category = (JeproLabCategoryModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.category_id = category.category_id;
                this.parent_id = category.parent_id;
                this.default_laboratory_id = category.default_laboratory_id;
                this.depth_level = category.depth_level;
                this.n_left = category.n_left;
                this.n_right = category.n_right;
                this.date_add = category.date_add;
                this.date_upd = category.date_upd;
                this.position = category.position;
                this.is_root_category = category.is_root_category;
                this.name = category.name;
                this.description = category.description;
                this.link_rewrite = category.link_rewrite;
                this.meta_title = category.meta_title;
                this.meta_keywords = category.meta_keywords;
                this.meta_description = category.meta_description;
            }
        }

        //this.image_id = (file_exists(JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY +  this.category_id + ".jpg")) ? this.category_id : 0;
        this.image_dir = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY;
    }

    public static ResultSet getCategories(){
        return getCategories(null);
    }

    public static ResultSet getCategories(String sqlSort){
        JeproLabContext context = JeproLabContext.getContext(); //NULL, $sql_sort = ""
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        //$app = JFactory.getApplication();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        /*$option = $app->input->get("option");
        $view = $app->input->get("view");


        /*int limit = context.listLimit;
        limit_start = context.listLimitStart; */
        int langId = context.language.language_id;
        boolean categoryPublished = false; //$app->getUserStateFromRequest($option. $view. ".published", "published", 0, "string");

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
        query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id");
        query += " = category_lang." + staticDataBaseObject.quoteName("category_id")+ JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") WHERE 1";
        query += (langId > 0 ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId : "") + (categoryPublished ? " AND " + staticDataBaseObject.quoteName("published") + "= 1" : "" );
        query += (langId < 0 ? " GROUP BY category.category_id " : "") + ((sqlSort != null && !sqlSort.equals("")) ? sqlSort : " ORDER BY category." + staticDataBaseObject.quoteName("depth_level") + " ASC, category_lab." + staticDataBaseObject.quoteName("position") + " ASC");


        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    public static JeproLabCategoryModel getRootCategory(){
        return getRootCategory(0, null);
    }

    public static JeproLabCategoryModel getRootCategory(int langId){
        return getRootCategory(langId, null);
    }

    public static JeproLabCategoryModel getRootCategory(int langId, JeproLabLaboratoryModel lab){
        JeproLabContext context = JeproLabContext.getContext();
        if(langId <= 0){ langId = context.language.language_id; }

        if(lab == null){
            if(JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT){
                lab = new JeproLabLaboratoryModel(JeproLabSettingModel.getIntValue("default_lab"));
            }else{
                lab = context.laboratory;
            }
        }else{
            return new JeproLabCategoryModel(lab.getCategoryId(), langId);
        }

        boolean has_more_than_one_root_category = JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1;
        JeproLabCategoryModel category;
        if (JeproLabLaboratoryModel.isFeaturePublished() && has_more_than_one_root_category && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT){
            category = JeproLabCategoryModel.getTopCategory(langId);
        }else{
            category = new JeproLabCategoryModel(lab.getCategoryId(), langId);
        }
        return category;
    }

    public static List getNestedCategories(){
        return getNestedCategories(0, 0, false);
    }

    public static List getNestedCategories(int rootCategoryId){
        return getNestedCategories(rootCategoryId, 0, false);
    }

    public static List getNestedCategories(int rootCategoryId, int langId){
        return getNestedCategories(rootCategoryId, langId, false);
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published){
        return getNestedCategories(rootCategoryId, langId, published, null);
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[]){
        return getNestedCategories(rootCategoryId, langId, published, groups, true);
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[], boolean useLabRestriction){
        return getNestedCategories(rootCategoryId, langId, published, groups, useLabRestriction, "");
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[], boolean useLabRestriction, String sqlFilter){
        return getNestedCategories(rootCategoryId, langId, published, groups, useLabRestriction, sqlFilter, "");
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[], boolean useLabRestriction, String sqlFilter, String sqlSort){
        return getNestedCategories(rootCategoryId, langId, published, groups, useLabRestriction, sqlFilter, sqlSort, "");
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean categoryPublished, int groups[], boolean useLabRestriction, String sqlFilter, String sqlSort, String sqlLimit){
            if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(rootCategoryId <= 0) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_THE_ROOT_CATEGORY_ID_MUST_BE_GREATER_THAN_0_LABEL"));
        }

        String cacheGroupKey = "";
        String groupFilter = "";

        if(groups != null && JeproLabGroupModel.isFeaturePublished()){
            if(groups.length > 0){
                for(int i = 0; i < groups.length -1; i++){
                    cacheGroupKey += groups[i] + "_";
                    groupFilter += groups[i] + ", ";
                }
                cacheGroupKey += groups[groups.length -1];
                groupFilter += groups[groups.length -1];
            }
        }

        String cacheKey = "jeprolab_category_getNestedCategories_" + JeproLabTools.md5((rootCategoryId + "_" + langId + "_" + categoryPublished + "_" + (groups != null && JeproLabGroupModel.isFeaturePublished() ? cacheGroupKey : "")));

        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            String query = "SELECT category.*, category_lang.* FROM " +  staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
            query += (useLabRestriction ? JeproLabLaboratoryModel.addSqlAssociation("category") : "") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
            query += " AS category_lang ON category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
            String selector = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_group") + " AS category_group ON category.";
            selector += staticDataBaseObject.quoteName("category_id") + " = category_group." + staticDataBaseObject.quoteName("category_id");
            query += (groups != null && JeproLabGroupModel.isFeaturePublished() ? selector : "");
            selector = " RIGHT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category_2 ON category_2." + staticDataBaseObject.quoteName("category_id");
            selector += " = " + rootCategoryId + " AND category." + staticDataBaseObject.quoteName("n_left") + " >= category_2." + staticDataBaseObject.quoteName("n_left");
            selector += " AND category." + staticDataBaseObject.quoteName("n_right") + " <= category_2." + staticDataBaseObject.quoteName("n_right");
            query += ((rootCategoryId > 0) ? selector : "") + " WHERE 1 " + sqlFilter + (langId > 0 ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId : "");
            query += (categoryPublished ? " AND category." + staticDataBaseObject.quoteName("published") + " = 1" : "") ;

            query += (groups != null && JeproLabGroupModel.isFeaturePublished() ? " AND category_group."+ staticDataBaseObject.quoteName("group_id") + " IN (" + groupFilter + ") " : "");
            selector = " GROUP BY category." + staticDataBaseObject.quoteName("category_id");
            query += (langId <= 0 || (groups != null && JeproLabGroupModel.isFeaturePublished()) ? selector : "");
            query += (!sqlSort.equals("") ? sqlSort : " ORDER BY category." + staticDataBaseObject.quoteName("depth_level") + " ASC");
            query += (sqlSort.equals("") && useLabRestriction ? ", category_lab." + staticDataBaseObject.quoteName("position") + " ASC" : "");
            query += (!sqlLimit.equals("") ? sqlLimit : "");

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObject();

            List<JeproLabCategoryModel> categories = new ArrayList<>();

            if (rootCategoryId <= 0){
                rootCategoryId = JeproLabCategoryModel.getRootCategory().category_id;
            }
            if(resultSet != null) {
                try{
                    JeproLabCategoryModel category;
                    while(resultSet.next()){
                        category = new JeproLabCategoryModel();
                        category.category_id = resultSet.getInt("category_id");
                        category.parent_id = resultSet.getInt("parent_id");
                        category.default_laboratory_id = resultSet.getInt("default_lab_id");
                        /*category = resultSet.get("");
                        category = resultSet.get("");
                        category = resultSet.get("");
                        category = resultSet.get("");
                        category = resultSet.get(""); */
                        categories.add(category);
                    }
                }catch(SQLException ignored){

                }
                /*foreach($result as $row) {
                    $current =&$buff[$row -> category_id];
                    $current = $row;

                    if ($row -> category_id == $root_category) {
                        $categories[$row -> category_id] =&$current;
                    } else {
                        $buff[$row -> parent_id]->children[$row -> category_id] =&$current;
                    }
                }*/
            }

            JeproLabCache.getInstance().store(cacheKey, categories);
        }

        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static ResultSet getRootCategories(){
        return getRootCategories(0, true);
    }

    public static ResultSet getRootCategories(int langId){
        return getRootCategories(langId, true);
    }

    public static ResultSet getRootCategories(int langId, boolean published){
        if (langId <= 0){
            langId = JeproLabContext.getContext().language.language_id;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT DISTINCT(category." + staticDataBaseObject.quoteName("category_id") + "), category_lang.";
        query += staticDataBaseObject.quoteName("name") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + "AS category";
        query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (";
        query += " category_lang." + staticDataBaseObject.quoteName("category_id") + " = category." + staticDataBaseObject.quoteName("category_id");
        query += " AND category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE ";
        query += staticDataBaseObject.quoteName("is_root") + " = 1 "  +(published ? "AND " + staticDataBaseObject.quoteName("published") + " = 1": "");

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    public static List getCategoriesWithoutParent(){
        String cacheKey = "jeprolab_category_get_Categories_Without_parent_" + JeproLabContext.getContext().language.language_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            List<JeproLabCategoryModel> categories = new ArrayList<>();
            String query = "SELECT DISTINCT category.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category";
            query += "	LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category.";
            query += staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id") ;
            query += " AND category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + JeproLabContext.getContext().language.language_id;
            query += ") WHERE " + staticDataBaseObject.quoteName("depth_level") + " = 1";

            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadObject();
            try{
                JeproLabCategoryModel category;
                while (result.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = result.getInt("category_id");
                    category.parent_id = result.getInt("parent_id");
                    category.default_laboratory_id = result.getInt("default_lab_id");
                    category.n_left = result.getInt("n_left");
                    category.n_right = result.getInt("n_right");
                    category.published = result.getInt("published") > 0;
                    category.position = result.getInt("position");
                    category.date_add = result.getDate("date_add");
                    category.date_upd = result.getDate("date_upd");
                    category.is_root_category = result.getInt("is_root") > 0;

                    //todo add language
                    categories.add(category);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }
            JeproLabCache.getInstance().store(cacheKey, categories);
        }
        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static List<JeproLabCategoryModel> getCategoryInformation(int categoryIds[]){
        return getCategoryInformation(categoryIds, 0);
    }

    /**
     *
     * @param categoryIds list categories
     * @param langId language filter id
     * @return Array
     */
    public static List<JeproLabCategoryModel> getCategoryInformation(int categoryIds[], int langId){
        if (langId <= 0){
            langId = JeproLabContext.getContext().language.language_id;
        }

        if (categoryIds.length <= 0){ return new ArrayList<>(); }

        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String categoriesList = "";
        if(categoryIds.length > 0) {
            for(int i = 0; i < (categoryIds.length - 1); i++){
                categoriesList += categoryIds[i] + ", ";
            }
            categoriesList += categoryIds[categoryIds.length - 1];
        }
        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." + staticDataBaseObject.quoteName("name");
        query += ", category_lang." + staticDataBaseObject.quoteName("link_rewrite") + ", category_lang." + staticDataBaseObject.quoteName("lang_id");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." ;
        query += staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") " + JeproLabLaboratoryModel.addSqlAssociation("category");
        query += " WHERE category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category.";
        query += staticDataBaseObject.quoteName("category_id") + " IN (" + categoriesList + ")";

        staticDataBaseObject.setQuery(query);
        ResultSet results = staticDataBaseObject.loadObject();
        try {
            JeproLabCategoryModel category;
            while(results.next()) {
                category = new JeproLabCategoryModel();
                category.category_id = results.getInt("category_d");
                category.name.put("lang_" + langId, results.getString("name"));
                category.language_id = results.getInt("lang_id");

                categories.add(category);
            }
        }catch(SQLException ignored){
            ignored.printStackTrace();
        }
        return categories;
    }

    /**
     * @param labId language filter id
     * @return boolean
     */
    public boolean isParentCategoryAvailable(int labId){
        if(labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        labId = labId > 0 ? labId : JeproLabSettingModel.getIntValue("default_lab");
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") + "AS category";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category_lab." + dataBaseObject.quoteName("lab_id") + " = " + labId;
        query += " AND category." + dataBaseObject.quoteName("parent_id") + " = " + this.parent_id;

        dataBaseObject.setQuery(query);
        //ResultSet result = dataBaseObject.loadObject();
        return dataBaseObject.loadValue("category_id") > 0;
    }

    public List<Integer> getGroups(){
        String cacheKey = "jeprolab_category_getGroups_" + this.category_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT category_group." + dataBaseObject.quoteName("group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_group");
            query += " AS category_group WHERE category_group." + dataBaseObject.quoteName("category_id") + " = " + this.category_id;

            dataBaseObject.setQuery(query);
            ResultSet groupSet = dataBaseObject.loadObject();
            List<Integer> groupList = new ArrayList<>();
            if(groupSet != null){
                try{
                    while(groupSet.next()){
                        groupList.add(groupSet.getInt("group_id"));
                    }
                }catch(SQLException ignored){

                }
            }

            JeproLabCache.getInstance().store(cacheKey, groupList);
        }
        return (List<Integer>)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public boolean isAssociatedToLaboratory(){
        return isAssociatedToLaboratory(0);
    }

    /**
     * Check if current object is associated to a lab
     *
     * @param  labId language filter id
     * @return bool
     */
    public boolean isAssociatedToLaboratory(int labId){
        if (labId <= 0){
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        String cacheKey = "jeprolab_category_model_lab_" + this.category_id + "_" + labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            boolean isAssociated = false;
            String query = "SELECT lab_id FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE ";
            query +=  dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND lab_id = " + labId;
            dataBaseObject.setQuery(query);
            ResultSet result = dataBaseObject.loadObject();
            try{
                while(result.next()){
                    isAssociated = result.getInt("lab_id") > 0;
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }

            JeproLabCache.getInstance().store(cacheKey, isAssociated);
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static JeproLabCategoryModel getTopCategory(){
        return getTopCategory(0);
    }

    /**
     *
     * @param langId language filter
     * @return JeproLabCategoryModel
     */
    public static JeproLabCategoryModel getTopCategory(int langId){
        if(langId < 0){
            langId = JeproLabContext.getContext().language.language_id;
        }
        String cacheKey = "jeprolab_category.getTopCategory_" + langId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
            query += "	WHERE " + staticDataBaseObject.quoteName("parent_id") + " = 0";
            staticDataBaseObject.setQuery(query);
            ResultSet categoryData = staticDataBaseObject.loadObject();
            int categoryId = 0;
            try{
                while(categoryData.next()){
                    categoryId = categoryData.getInt("category_id");
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }
            JeproLabCache.getInstance().store(cacheKey, new JeproLabCategoryModel(categoryId, langId));
        }
        return (JeproLabCategoryModel)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static String getLinkRewrite(int categoryId, int langId){
        if (categoryId <= 0 || langId <= 0){
            return "";
        }
        String cacheKey = categoryId + "_" + langId;
        if (!JeproLabCategoryModel._links.containsKey(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT category_lang." + staticDataBaseObject.quoteName("link_rewrite") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
            query += " AS category_lang WHERE " + staticDataBaseObject.quoteName("lang_id") + " = " + langId ;
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + " AND category_lang.";
            query += staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

            staticDataBaseObject.setQuery(query);
            JeproLabCategoryModel._links.put(cacheKey, staticDataBaseObject.loadStringValue("link_rewrite"));
        }
        return JeproLabCategoryModel._links.get(cacheKey);
    }

    public List<JeproLabCategoryModel> getCategoriesList(){
        if(dataBaseObject == null) {
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        JeproLabContext context = JeproLabContext.getContext();

        int limit = context.listLimit;
        int limitStart = context.listLimitStart;
        int langId = context.language.language_id;
        int labId = context.laboratory.laboratory_id;
        //int labGroupId = $app->getUserStateFromRequest($option. $view. ".lab_group_id", "lab_group_id", $context->lab->lab_group_id, "int");
        int categoryId = JeproLab.request.getIntValue("category_id", 0);
        String orderBy = JeproLab.request.getValue("order_by", "date_add");
        String orderWay = JeproLab.request.getValue("order_way", "ASC");
        boolean published = JeproLab.request.getIntValue("published", 0) > 0;

        int countCategoriesWithoutParent = JeproLabCategoryModel.getCategoriesWithoutParent().size();

        JeproLabCategoryModel topCategory = JeproLabCategoryModel.getTopCategory();
        int parentId = 0;
        if(categoryId > 0){
            JeproLabCategoryModel category = new JeproLabCategoryModel(categoryId);
            parentId = category.category_id;
        }else if(!JeproLabLaboratoryModel.isFeaturePublished() && countCategoriesWithoutParent > 1){
            parentId = topCategory.category_id;
        }else if(JeproLabLaboratoryModel.isFeaturePublished() && countCategoriesWithoutParent == 1){
            parentId = JeproLabSettingModel.getIntValue("root_category");
        }else if(JeproLabLaboratoryModel.isFeaturePublished() && countCategoriesWithoutParent > 1 && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT){
            if((JeproLabSettingModel.getIntValue("multilab_feature_active") > 0) && (JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() == 1)){
                parentId = context.laboratory.category_id;
            }else{
                parentId = topCategory.category_id;
            }
        }

        boolean explicitSelect = true;

        /* Manage default params values */
        boolean useLimit = true;
        if (limit <= 0) {
            useLimit = false;
        }
        String join = " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " AS category_lab ON (category.";
        join += dataBaseObject.quoteName("category_id") + " = category_lab." + dataBaseObject.quoteName("category_id") + " AND ";
        if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
            join += " category_lab.lab_id = " + context.laboratory.laboratory_id + ") ";
        }else{
            join += " category_lab.lab_id = category.default_lab_id)" ;
        }

        // we add restriction for lab
        String where = "";
        if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && JeproLabLaboratoryModel.isFeaturePublished()){
            where = " AND category_lab." + dataBaseObject.quoteName("lab_id") + " = " + JeproLabContext.getContext().laboratory.laboratory_id;
        }
        /* Check params validity */
        if (!JeproLabTools.isOrderBy(orderBy) || !JeproLabTools.isOrderWay(orderWay) || limitStart < 0 || limit < 0 || langId < 0){
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_GET_LIST_PARAMS_IS_NOT_VALID_MESSAGE"));
        }

        /* Cache * /
        if (orderBy.contains("/[.!]/")){
            orderBySplit = preg_split("/[.!]/", orderBy);
            orderBy = bqSQL(orderBySplit[0]) + ".`" + bqSQL(orderBySplit[1]) + "`";
        }else if(!orderBy.equals("")){
            orderBy = dataBaseObject.quoteName(dataBaseObject.quote(orderBy, true));
        }*/

        // Add SQL lab restriction
        String labLinkType = "";
        String selectLab = "";
        String joinLab = "";
        String whereLab = "";
        if (!labLinkType.equals("")){
            selectLab = ", lab.lab_name as lab_name ";
            joinLab = " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " AS lab ON category.";
            joinLab += dataBaseObject.quoteName("category_id") + " = lab." + dataBaseObject.quoteName("category_id");
            whereLab = JeproLabLaboratoryModel.addSqlRestriction("1", "category");
        }

        if (context.controller.multi_lab_context && JeproLabLaboratoryModel.isTableAssociated("category")){
            /*if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.ALL_CONTEXT || !context.employee.isSuperAdmin()){
                boolean testJoin = !preg_match("/`?"+ preg_quote("#__jeprolab_category_lab") + "`? *category_lab/", join);
                if (JeproLabLaboratoryModel.isFeaturePublished() && testJoin && JeproLabLaboratoryModel.isTableAssociated("category")){
                    where += " AND category.category_id IN ( SELECT category_lab.category_id FROM ";
                    where += dataBaseObject.quoteName("#__jeprolab_category__lab") + " AS category_lab WHERE category_lab.";
                    where += "lab_id IN (" + implode(", ", JeproLabLaboratoryModel.getContextListLabIds()) + ") )";
                }
            } */
        }

        String select = ", category_lab.position AS position ";
        String tmpTableFilter = "";

        /* Query in order to get results with all fields */
        String langJoin = " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (";
        langJoin += "category_lang." + dataBaseObject.quoteName("category_id") + " = category." + dataBaseObject.quoteName("category_id");
        langJoin += " AND category_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId;
        if (context.laboratory.laboratory_id > 0){
            if (!JeproLabLaboratoryModel.isFeaturePublished()){
                langJoin += " AND category_lang." + dataBaseObject.quoteName("lab_id") + " = 1";
            }else if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                langJoin +=  " AND category_lang." + dataBaseObject.quoteName("lab_id") + " = " + context.laboratory.laboratory_id;
            }else{
                langJoin +=  " AND category_lang." + dataBaseObject.quoteName("lab_id") + " = category.default_lab_id";
            }
        }
        langJoin += ") ";


        String havingClause = "";
        /*if (isset(this._filterHaving) || isset(this._having)){
            havingClause = " HAVING ";
            if (isset(this._filterHaving)){
                havingClause += ltrim(this._filterHaving, " AND ");
            }
            if(isset(this._having)){
                havingClause += this._having + " ";
            }
        }
*/
        String query;
        ResultSet categoryResult;
        List<JeproLabCategoryModel> categories = new ArrayList<>();
        do{
            int total = 0;
            query = "SELECT SQL_CALC_FOUND_ROWS " +(!tmpTableFilter.equals("") ? " * FROM (SELECT " : "");
            if (explicitSelect){
                query += "category." + dataBaseObject.quoteName("category_id") + ", category_lang." + dataBaseObject.quoteName("name") + ", category_lang." + dataBaseObject.quoteName("description");
                query += ", category." + dataBaseObject.quoteName("position") +" AS category_position, " + dataBaseObject.quoteName("published");
            }else{
                query += (langId > 0? " category_lang.*," : "") + " category.*";
            }
            //select
            query += (!select.equals("") ? select : "") + selectLab + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category " + langJoin + (!join.equals("") ? join + " " : "") ;
            query += joinLab + " WHERE 1 " + (!where.equals("") ? where + " " : "") +  "AND category.";
            query += dataBaseObject.quoteName("parent_id") +" = " + parentId + whereLab  + havingClause + " ORDER BY " + ((orderBy.replace("`", "").equals("category_id")) ? "category." : "") + " category.";
            query += orderBy + " " + orderWay + (!tmpTableFilter.equals("") ? ") tmpTable WHERE 1" + tmpTableFilter : "");

            dataBaseObject.setQuery(query);

            try{
                categoryResult = dataBaseObject.loadObject();
                while (categoryResult.next()) {
                    total = total + 1;
                }
                query += (useLimit ? " LIMIT " + limitStart + ", " + limit : "" );
                dataBaseObject.setQuery(query);
                categoryResult = dataBaseObject.loadObject();
                JeproLabCategoryModel category;
                while (categoryResult.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categoryResult.getInt("category_id");
                    category.published = categoryResult.getInt("published") > 0;
                    category.name.put("lang_" + langId, categoryResult.getString("name"));
                    category.description.put("lang_" + langId, categoryResult.getString("description"));
                    /*category = categoryResult;
                    category = categoryResult; */
                    categories.add(category);
                }
            }catch (SQLException ignored){

            }

            if (useLimit){
                limitStart = limitStart - limit;
                if (limitStart < 0){ break; }
            }else{ break; }
        } while (categories.isEmpty());

        if(!categories.isEmpty()){
            for(JeproLabCategoryModel item : categories){
                ResultSet categoryTree = JeproLabCategoryModel.getChildren(item.category_id, context.language.language_id);
                //item.set_view = (category_tree.size() > 0 ? 1 : 0);
            }
        }

        this.pagination = new Pagination(); //$total, $limitstart, $limit);*/
        return categories;
    }

    public boolean isMultiLab(){
        return JeproLabLaboratoryModel.isTableAssociated("category") || JeproLabCategoryModel.multiLangLab;
    }

    public boolean isLangMultiLab(){
        return JeproLabCategoryModel.multiLang && JeproLabCategoryModel.multiLangLab;
    }

    public Pagination getPagination(){
        return this.pagination;
    }

    public List<JeproLabCategoryModel> getParentsCategories(){
        return getParentsCategories(0);
    }

    /**
     * Get Each parent category of this category until the root category
     *
     * @param langId Language ID
     * @return array Corresponding categories
     */
    public List<JeproLabCategoryModel> getParentsCategories(int langId) {
        JeproLabContext context = JeproLabContext.getContext().clone();
        //context.laboratory = clone(context.laboratory);
        int categoryId = JeproLab.request.getIntValue("category_id");

        if (langId <= 0){ langId = context.language.language_id; }

        List<JeproLabCategoryModel> categories = new ArrayList<>();
        int currentId = this.category_id;
        if (JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && JeproLabSettingModel.getIntValue("multilab_feature_active") > 0 && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() != 1) {
            context.laboratory.category_id = JeproLabCategoryModel.getTopCategory().category_id;
        }else if (context.laboratory.laboratory_id <= 0) {
            context.laboratory = new JeproLabLaboratoryModel(JeproLabSettingModel.getIntValue("default_lab"));
        }

        int labId = context.laboratory.laboratory_id;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query;
        JeproLabCategoryModel rootCategory;
        do{
            query = "SELECT category.*, category_lang.* FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." + dataBaseObject.quoteName("category_id");
            query += " = category_lang." + dataBaseObject.quoteName("category_id") + " AND " + dataBaseObject.quoteName("lang_id") + " = " + langId;
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ")";
            if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " AS category_lab ON (category.";
                query += dataBaseObject.quoteName("category_id") + " = category_lab." + dataBaseObject.quoteName("category_id");
                query += " AND category_lab." + dataBaseObject.quoteName("lab_id") + " = " + labId + ")";
            }
            query += " WHERE category." + dataBaseObject.quoteName("category_id") + " = " + currentId;
            if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                query += " AND category_lab." + dataBaseObject.quoteName("lab_id") + " = "  + context.laboratory.laboratory_id;
            }
            rootCategory = JeproLabCategoryModel.getRootCategory();
            if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && ((categoryId <= 0) || (categoryId == rootCategory.category_id) || (rootCategory.category_id == context.laboratory.category_id))){
                query += " AND category." + dataBaseObject.quoteName("parent_id") + " != 0";
            }

            dataBaseObject.setQuery(query);
            ResultSet result = dataBaseObject.loadObject();

            try{
                JeproLabCategoryModel category;
                while(result.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = result.getInt("category_id");
                    category.parent_id = result.getInt("parent_id");
                    category.default_laboratory_id = result.getInt("default_lab_id");
                    category.depth_level = result.getInt("depth_level");
                    category.n_left = result.getInt("n_left");
                    category.n_right = result.getInt("n_right");
                    category.published = result.getInt("published") > 0;
                    category.date_add = result.getDate("date_add");
                    category.date_upd = result.getDate("date_add");
                    category.position = result.getInt("position");
                    category.is_root_category = result.getInt("is_root") > 0;
                    currentId = category.category_id;
                    categories.add(category);
                }
            }catch(SQLException ignored){

            }
            /*if ($result)
                $categories[] = $result;
            elseif (!$categories)
            $categories = array(); * /
            if($result->category_id == context.laboratory.category_id)
                return categories;*/
            //currentId = $result->parent_id;
        }while(categories.isEmpty());
        return  categories;
    }

    public static ResultSet getChildren(int parentId, int langId){
        return getChildren(parentId, langId, true, 0);
    }

    public static ResultSet getChildren(int parentId, int langId, boolean published){
        return getChildren(parentId, langId, published, 0);
    }

    /**
     *
     * @param parentId category parent id
     * @param langId language filter
     * @param published status filter
     * @return array
     */
    public static ResultSet getChildren(int parentId, int langId, boolean published, int labId){
        String cacheKey = "jeprolab_category_get_children_" + parentId + "_" + langId + "_"+ (published ? 1 : 0) + "_" + labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." ;
            query += staticDataBaseObject.quoteName("name") + ", category_lang." + staticDataBaseObject.quoteName("link_rewrite");
            query += (labId > 0 ? ", category_lab." + staticDataBaseObject.quoteName("lab_id") : "") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
            query += " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category.";
            query += staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id") ;
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") "
                    + (labId > 0 ? JeproLabLaboratoryModel.addSqlAssociation("category") : "");
            query += " WHERE " + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category." + staticDataBaseObject.quoteName("parent_id");
            query += " = " + parentId +  (published ? " AND " + staticDataBaseObject.quoteName("published") + " = 1" : "") +" GROUP BY category.";
            query += staticDataBaseObject.quoteName("category_id") + (labId > 0 ? " ORDER BY category_lab." + staticDataBaseObject.quoteName("position") + " ASC " : "");

            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadObject();

            JeproLabCache.getInstance().store(cacheKey, result);
        }
        return (ResultSet)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Check if there is more than one entries in associated lab table for current entity
     *
     *
     * @return bool
     */
    public boolean hasMultiLabEntries() {
        if (!JeproLabLaboratoryModel.isTableAssociated("category") || !JeproLabLaboratoryModel.isFeaturePublished()) {
            return false;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT COUNT(*) AS entries FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE ";
        query += dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        dataBaseObject.setQuery(query);

        return dataBaseObject.loadValue("entries") > 0;
    }

    /**
     * Get the depth level for the category
     * @return int Depth level
     */
    public int calculateDepthLevel(){
        /* Root category */
        if (this.parent_id < 0) {
            return 0;
        }
        JeproLabCategoryModel parentCategory = new JeproLabCategoryModel(this.parent_id);
        if (parentCategory.category_id <= 0){
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_PARENT_CATEGORY_DOES_NOT_EXISTS_MESSAGE"));
        }
        return parentCategory.depth_level + 1;
    }

    public void saveCategory(){
        Map<String, String> categoryData = JeproLab.request.getPost();
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        languages = JeproLabLanguageModel.getLanguages();
        int categoryId = categoryData.containsKey("category_id") ? Integer.parseInt(categoryData.get("category_id")) : 0;
        JeproLabContext context = JeproLabContext.getContext();

        //$view = $app->input->get("view");
        int parentId = categoryData.containsKey("parent_id") ? Integer.parseInt(categoryData.get("parent_id")) : 0;

        //this.date_add = date("Y-m-d H:i:s");
        //this.date_upd = date("Y-m-d H:i:s");

        //if true, we are in a root category creation
        if(parentId < 0){
            this.is_root_category = true;
            categoryData.put("depth_level", "1");
            parentId = JeproLabSettingModel.getIntValue("root_category");
        }

        if(categoryId > 0) {
            if (categoryId != parentId) {
                if (!JeproLabCategoryModel.checkBeforeMove(categoryId, parentId)) {
                    context.controller.has_errors = true;
                    JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_THE_CATEGORY_CANNOT_BE_MOVED_HERE_MESSAGE"));
                }
            }else{
                context.controller.has_errors = true;
                JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_THE_CATEGORY_CANNOT_BE_A_PARENT_OF_ITSELF_MESSAGE"));
            }
        }

        /*if(!isset($view) || $view != "category"){
            $app->input->set("category_id", null);
            $app->redirect("index.php?option=com_jeprolab&view=category");
            return false;
        }*/

        if(!context.controller.has_errors){
            int categoryPublished = categoryData.containsKey("published") ? Integer.parseInt(categoryData.get("published")) : 0;

            int depthLevel = this.calculateDepthLevel();


            int rootCategoryId = JeproLabSettingModel.getIntValue("root_category");
            if(this.is_root_category && rootCategoryId > 0){
                parentId = rootCategoryId;
            }

            List<Integer> labListIds = new ArrayList<>();
            if(JeproLabLaboratoryModel.isTableAssociated("category")){
                labListIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
                if(this.laboratory_list_ids.size() > 0){ labListIds = this.laboratory_list_ids; }
            }
            int defaultLabId = JeproLabContext.getContext().laboratory.laboratory_id;
            if(JeproLabLaboratoryModel.checkDefaultLabId("category")){
                final int[] minValue = {labListIds.get(0)};
                labListIds.stream().filter(val -> val < minValue[0]).forEach(val ->
                    minValue[0] = val
                );
                this.default_laboratory_id = minValue[0];
                defaultLabId = minValue[0];
            }

            int position = 0;

            String query =  "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category") + "(" + dataBaseObject.quoteName("parent_id");
            query += ", " + dataBaseObject.quoteName("depth_level") + ", " + dataBaseObject.quoteName("default_lab_id") + ", ";
            query += dataBaseObject.quoteName("published") + ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd");
            query += ", " + dataBaseObject.quoteName("is_root") + ") VALUES (" + parentId + ", " + depthLevel + ", " + defaultLabId + ", ";
            query +=  categoryPublished + ", " + dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + ", " + dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss"));
            query += ", " + (this.is_root_category ? 1 : 0) + ")";

            dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.query(true);
            if(result){
                this.category_id = dataBaseObject.getGeneratedKey();
                for(int labId : labListIds){
                    query = "INSERT INTO " +  dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" + dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("lab_id");
                    query += ", " + dataBaseObject.quoteName("position") + ") VALUES (" + this.category_id + ", " + labId + ", " + position + ")";

                    dataBaseObject.setQuery(query);
                    result &= dataBaseObject.query(false);
                    for (Object o : languages.entrySet()) {
                        Map.Entry lang = (Map.Entry) o;
                        JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();

                        String categoryDescription = categoryData.get("description_" + language.language_id);
                        String categoryName = categoryData.get("name_" + language.language_id);
                        String metaDescription = categoryData.get("meta_description_" + language.language_id);
                        String metaTitle = categoryData.get("meta_title_" + language.language_id);
                        String metaKeywords = categoryData.get("meta_keywords_" + language.language_id);
                        String linkRewrite = categoryData.get("meta" + language.language_id);
                        query = "INSERT INTO " +  dataBaseObject.quoteName("#__jeprolab_category_lang") + "(" + dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("lab_id");
                        query += ", " + dataBaseObject.quoteName("lang_id") + ", " + dataBaseObject.quoteName("name") + ", " + dataBaseObject.quoteName("description") + ", " + dataBaseObject.quoteName("link_rewrite");
                        query += ", " + dataBaseObject.quoteName("meta_title") + ", " + dataBaseObject.quoteName("meta_keywords") + ", " + dataBaseObject.quoteName("meta_description") + ") VALUES (";
                        query += this.category_id + ", " + labId + ", " + language.language_id + ", " + dataBaseObject.quote(categoryName);
                        query += ", " + dataBaseObject.quote(categoryDescription) + ", " + dataBaseObject.quote(linkRewrite) + ", ";
                        query += dataBaseObject.quote(metaTitle) + ", " + dataBaseObject.quote(metaKeywords) + ", " + dataBaseObject.quote(metaDescription) + ")";

                        dataBaseObject.setQuery(query);
                        result &= dataBaseObject.query(false);
                    }

                }
            }
        }

        if(categoryData.containsKey("check_box_lab_associated_category")){
            String[] associatedLabs = categoryData.get("check_box_lab_associated_category").split(",");
            for(String val : associatedLabs){
                int labId = Integer.parseInt(val);
                int lastPosition = JeproLabCategoryModel.getLastPosition(this.parent_id, labId);
                this.addPosition(lastPosition, labId);
            }
        }else{
            for(JeproLabLaboratoryModel laboratory : JeproLabLaboratoryModel.getLaboratories()){
                int lastPosition = JeproLabCategoryModel.getLastPosition(this.parent_id, laboratory.laboratory_id);
                if(lastPosition <= 0){
                    lastPosition = 1;
                }
                this.addPosition(lastPosition, laboratory.laboratory_id);
            }
        }

        if (!this.do_not_regenerate_nested_tree){
            JeproLabCategoryModel.regenerateEntireNestedTree();
        }
        if(this.groupBox != null) {
            this.updateGroup(this.groupBox);
        }
        //if we create a new root category you have to associate to a lab before to add sub categories in. So we redirect to AdminCategories listing
        if(this.is_root_category){
            JeproLab.request.setRequest("category_id=" + JeproLabCategoryModel.getTopCategory().category_id + JeproLabTools.getCategoryToken());
            //$link = JRoute._("index.php?option=com_jeprolab&view=category&" );
            //$message = "";
            try {
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().categoryForm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Re-calculate the values of all branches of the nested tree
     */
    public static void regenerateEntireNestedTree(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        int labId = JeproLabContext.getContext().laboratory.laboratory_id;
        labId = labId > 0 ? labId: JeproLabSettingModel.getIntValue("default_lab");

        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category." + staticDataBaseObject.quoteName("parent_id") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lab");
        query += " AS category_lab ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lab." + staticDataBaseObject.quoteName("category_id");
        query += " AND category_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId +  ") ORDER BY category.";
        query += staticDataBaseObject.quoteName("parent_id") + ", category_lab." + staticDataBaseObject.quoteName("position") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet categoriesSet = staticDataBaseObject.loadObject();

        Map<Integer, Map<String, List<Integer>>> categoriesArray = new HashMap<>();
        if(categoriesSet != null) {
            try{
                int parentId;
                while(categoriesSet.next()){
                    parentId = categoriesSet.getInt("parent_id");
                    if(!categoriesArray.containsKey(parentId)){
                        categoriesArray.put(parentId, new HashMap<>());
                        categoriesArray.get(parentId).put("subcategories", new ArrayList<>());
                        categoriesArray.get(parentId).get("subcategories").add(categoriesSet.getInt("category_id"));
                    }else{
                        categoriesArray.get(parentId).get("subcategories").add(categoriesSet.getInt("category_id"));
                    }
                }
                int n = 1;
                if(categoriesArray.containsKey(0) && categoriesArray.get(0).containsKey("subcategories")){
                    JeproLabCategoryModel.subTree(categoriesArray, categoriesArray.get(0).get("subcategories").get(0), 1);
                }
            }catch (SQLException ignored){

            }
        }
    }

    /**
     * this function return the number of category + 1 having $id_category_parent as parent.
     *
     * @param categoryParentId the parent category
     * @param labId laboratory id
     * @return int
     */
    public static int getLastPosition(int categoryParentId, int labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT MAX(category_lab." + staticDataBaseObject.quoteName("position") + ") AS max_pos FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " AS category_lab ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lab.";
        query += staticDataBaseObject.quoteName("category_id") + " AND category_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId + ") WHERE category.";
        query += staticDataBaseObject.quoteName("parent_id") + " = " + categoryParentId;

        staticDataBaseObject.setQuery(query);
        return (1 + (int)staticDataBaseObject.loadValue("max_pos"));
    }


    /**
     * Updates level_depth for all children of the given id_category
     *
     * @param categoryId parent category
     */
    public void reCalculateDepthLevel(int categoryId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (categoryId <= 0) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_CATEGORY_ID_IS_LESS_OR_EQUALS_TO_ZERO_MESSAGE"));
        }
        /* Gets all children */
        String query = "SELECT " + dataBaseObject.quoteName("category_id") + ", "  + dataBaseObject.quoteName("parent_id") + ", " + dataBaseObject.quoteName("depth_level") + " FROM ";
        query += dataBaseObject.quoteName("#__jeprolab_category") + " WHERE parent_id = " + categoryId ;

        dataBaseObject.setQuery(query);
        ResultSet categories = dataBaseObject.loadObject();

        /* Gets level_depth */
        query = "SELECT depth_level FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " WHERE category_id = " + categoryId;
        dataBaseObject.setQuery(query);
        int level = (int)dataBaseObject.loadValue("depth_level");
        /* Updates level_depth for all children */
        try {
            int subCategoryId;
            while(categories.next()) {
                subCategoryId = categories.getInt("category_id");
                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " SET depth_level = " + (level + 1);
                query += " WHERE category_id = " + subCategoryId;

                dataBaseObject.setQuery(query);
                dataBaseObject.query(false);
                /* Recursive call */
                this.reCalculateDepthLevel(subCategoryId);
            }
        }catch (SQLException ignored){

        }
    }

    public boolean addPosition(int position){
        return addPosition(position, 0);
    }

    public boolean addPosition(int position, int labId){
        boolean  result = true;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (labId == 0){
            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT){
                for(int laboratoryId : JeproLabLaboratoryModel.getContextListLaboratoryIds()) {
                    String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" + dataBaseObject.quoteName("category_id");
                    query += ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") + ") VALUES (" + this.category_id;
                    query += ", " + laboratoryId + ", " + position + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("position") + " = "  + position;

                    dataBaseObject.setQuery(query);
                    result &= dataBaseObject.query(false);
                }
            }else {
                labId = JeproLabContext.getContext().laboratory.laboratory_id;
                labId = labId > 0 ? labId : JeproLabSettingModel.getIntValue("default_lab");

                String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " ( " + dataBaseObject.quoteName("category_id");
                query += ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") + " VALUES (" + this.category_id;
                query += ", " + labId + ", " + position + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("position") + " = " + position;

                dataBaseObject.setQuery(query);
                result = dataBaseObject.query(false);
            }
        }else{
            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " ("+ dataBaseObject.quoteName("category_id");
            query += ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") + ") VALUES (" + this.category_id;
            query += ", " + labId + ", " + position + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("position") + " = " + position;

            dataBaseObject.setQuery(query);
            result = dataBaseObject.query(false);
        }
        return result;
    }

    /**
     * Update customer groups associated to the object
     *
     * @param list groups
     */
    public void updateGroup(int list[]) {
        this.cleanGroups();
        if (list.length == 0) {
            list = new int[3];
            list[0] = JeproLabSettingModel.getIntValue("unidentified_group");
            list[1] = JeproLabSettingModel.getIntValue("guest_group");
            list[3] = JeproLabSettingModel.getIntValue("customer_group");
        }
        this.addGroups(list);
    }

    public boolean addGroups(int groups[]){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query;
        boolean result = true;
        for (int groupId : groups) {
            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_group") + " (" + dataBaseObject.quoteName("category_id") + ", ";
            query += dataBaseObject.quoteName("group_id") + ") VALUES (" + this.category_id + ", " + groupId + ")";

            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);
        }
        return result;
    }

    public void cleanGroups(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_group") + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);
    }

    protected static void subTree(Map<Integer, Map<String, List<Integer>>> categories, int categoryId, int n) {
        int left = n++;
        if (staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (categories.get(categoryId).get("subcategories").size() > 0) {
            for (int subCategoryId : categories.get(categoryId).get("subcategories")) {
                JeproLabCategoryModel.subTree(categories, subCategoryId, n);
            }
        }
        int right = n + 1;

        String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_category") + " SET n_left = " + left + ", n_right = " + right;
        query += "	WHERE category_id = " + categoryId + " LIMIT 1";

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);
    }

    public void updateCategory(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        Map<String, String> categoryData = JeproLab.request.getPost();
        if(languages == null) {
            languages = JeproLabLanguageModel.getLanguages();
        }
        JeproLabContext context = JeproLabContext.getContext();

        if(!context.controller.has_errors){
            int categoryId = Integer.parseInt(categoryData.get("category_id"));
            int categoryPublished = Integer.parseInt(categoryData.get("published"));
            int isRootCategory = Integer.parseInt(categoryData.get("is_root_category"));

            /* update category  */
            if(categoryId > 0 && this.category_id == categoryId){
                //$category = new JeproLabCategoryModelCategory($category_id);
                if(this.category_id <= 0){
                    if(this.category_id == this.parent_id){
                        context.controller.has_errors = true;
                        JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_A_CATEGORY_CANNOT_BE_ITS_OWN_PARENT_LABEL"));
                    }
                    if(this.is_root_category){
                        this.parent_id = JeproLabSettingModel.getIntValue("root_category");
                    }
                    // Update group selection
                    if(this.groupBox != null) {
                        this.updateGroup(this.groupBox);
                    }
                    this.depth_level = this.calculateDepthLevel();

                    // If the parent category was changed, we don"t want to have 2 categories with the same position
                    if (this.getDuplicatedPosition() > 0){
                        if (categoryData.containsKey("check_box_lab_associated_category")){
                            String[] associatedLabs = categoryData.get("check_box_lab_associated_category").split(",");
                            for(String val : associatedLabs){
                                int labId = Integer.parseInt(val);
                                int lastPosition = JeproLabCategoryModel.getLastPosition(this.parent_id, labId);
                                this.addPosition(lastPosition, labId);
                            }
                            /*foreach ($category_data["check_box_lab_associated_category"] as $associated_category_id => $row) {
                                foreach ($row as labId => $value) {
                                    this.addPosition(JeproLabCategoryModel.getLastPosition((int)this.parent_id, labId), labId);
                                }
                            }*/
                        }else {
                            for (JeproLabLaboratoryModel lab : JeproLabLaboratoryModel.getLaboratories(true)){
                                this.addPosition(Math.max(1, JeproLabCategoryModel.getLastPosition(this.parent_id, lab.laboratory_id)), lab.laboratory_id);
                            }
                        }
                    }
                    JeproLabCategoryModel.cleanPositions(this.parent_id);

                    this.clearCache();

                    List<Integer> labListIds = new ArrayList<>();
                    if(JeproLabLaboratoryModel.isTableAssociated("category")){
                        labListIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
                        if(this.laboratory_list_ids.size() > 0){ labListIds = this.laboratory_list_ids; }
                    }

                    if(JeproLabLaboratoryModel.checkDefaultLabId("category") && this.default_laboratory_id < 0){
                        int minValue = labListIds.get(0);
                        for (Integer labListId : labListIds) {
                            if (labListId < minValue) minValue = labListId;
                        }
                        this.default_laboratory_id = minValue;
                    }


                    String where, select;

                    String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " SET " + dataBaseObject.quoteName("n_left") + " = " + this.n_left + ", " ;
                    query += dataBaseObject.quoteName("n_right") + " = " + this.n_right + ", " + dataBaseObject.quoteName("depth_level") + " = " + this.depth_level;
                    query += ", " + dataBaseObject.quoteName("published") + " = " + categoryPublished + ", " + dataBaseObject.quoteName("default_lab_id") + " = " + this.default_laboratory_id;
                    query += ", " + dataBaseObject.quoteName("is_root") + " = " + isRootCategory + ", " + dataBaseObject.quoteName("position") + " = ";
                    query += this.position + ", " + dataBaseObject.quoteName("date_upd") + " = " + dataBaseObject.quote(JeproLabTools.date("Y-m-d H:i:s")) + " WHERE " + dataBaseObject.quoteName("category_id");
                    query += " = " + this.category_id;

                    dataBaseObject.setQuery(query);
                    boolean result = dataBaseObject.query(false);

                    for(int labId : labListIds){
                        where = " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND " + dataBaseObject.quoteName("lab_id") + " = " + labId;
                        select = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + where;
                        dataBaseObject.setQuery(select);
                        boolean labDataExist = (dataBaseObject.loadValue("category_id") > 0);
                        if(labDataExist){
                            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " SET " + dataBaseObject.quoteName("position") + " = " + this.position + where;
                            dataBaseObject.setQuery(query);
                            result &= dataBaseObject.query(false);
                        }else if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" + dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") ;
                            query += ") VALUES (" + this.category_id + ", " + labId + ", "  + this.position + ")";
                            dataBaseObject.setQuery(query);
                            result &= dataBaseObject.query(false);
                        }

                        for (Object o : languages.entrySet()) {
                            Map.Entry lang = (Map.Entry) o;
                            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();

                            String categoryDescription = categoryData.get("description_" + language.language_id);
                            String categoryName = categoryData.get("name_" + language.language_id);
                            String metaDescription = categoryData.get("meta_description_" + language.language_id);
                            String metaTitle = categoryData.get("meta_title_" + language.language_id);
                            String metaKeywords = categoryData.get("meta_keywords_" + language.language_id);
                            String linkRewrite = categoryData.get("meta" + language.language_id);

                            where = " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND " + dataBaseObject.quoteName("lab_id");
                            where += " = " + labId + " AND " + dataBaseObject.quoteName("lang_id") + " = " + language.language_id;
                            select = "SELECT COUNT(*) val FROM " + dataBaseObject.quoteName("#__jeprolab_category_lang") + where;
                            dataBaseObject.setQuery(select);
                            boolean langDataExist = dataBaseObject.loadValue("val") > 0;

                            if(langDataExist) {
                                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " SET " + dataBaseObject.quoteName("name") + " = " + dataBaseObject.quote(categoryName) + ", ";
                                query += dataBaseObject.quoteName("description") + " = " + dataBaseObject.quote(categoryDescription) + ", " + dataBaseObject.quoteName("link_rewrite") + " = ";
                                query += dataBaseObject.quote(linkRewrite) + ", " + dataBaseObject.quoteName("meta_title") + " = " + dataBaseObject.quote(metaTitle);
                                query += ", "  + dataBaseObject.quoteName("meta_keywords") + " = " + dataBaseObject.quote(metaKeywords) + ", " + dataBaseObject.quoteName("meta_description");
                                query += " = " + dataBaseObject.quote(metaDescription) + where;
                            }else{
                                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " (" + dataBaseObject.quoteName("name") + ", " + dataBaseObject.quoteName("description") + ", " + dataBaseObject.quoteName("link_rewrite");
                                query += ", " + dataBaseObject.quoteName("meta_title") + ", "  + dataBaseObject.quoteName("meta_keywords") + ", " + dataBaseObject.quoteName("meta_description") + ") VALUES (" + dataBaseObject.quote(categoryName);
                                query += ", " + dataBaseObject.quote(categoryDescription) + ", " + dataBaseObject.quote(linkRewrite) + ", ";
                                query += dataBaseObject.quote(metaTitle) + ", " + dataBaseObject.quote(metaKeywords) + ", " + dataBaseObject.quote(metaDescription) + ") " + where;
                            }
                            dataBaseObject.setQuery(query);
                            result &= dataBaseObject.query(false);
                        }
                    }

                    if (!this.do_not_regenerate_nested_tree){
                        JeproLabCategoryModel.regenerateEntireNestedTree();
                        this.reCalculateDepthLevel(this.category_id);
                    }
                }
                JeproLab.request.setRequest("view=category&category_id=" + this.category_id + "&task=edit" + JeproLabTools.getCategoryToken() );

                //$message = "";
                //$link ="index.php?option=com_jeprolab&view=category&category_id=" + this.category_id + "&task=edit" +;
            }else{
                JeproLab.request.setRequest("view=category&category_id=" + this.category_id + "&task=edit" + JeproLabTools.getCategoryToken() );
            }
            try {
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Search for another category with the same parent and the same position
     *
     * @return array first category found
     */
    public int getDuplicatedPosition(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category." + dataBaseObject.quoteName("parent_id") + " = ";
        query += this.parent_id + " AND category_lab." + dataBaseObject.quoteName("position") + " = " + this.position + " AND category.";
        query += dataBaseObject.quoteName("category_id") + " != " + this.category_id;

        dataBaseObject.setQuery(query);

        return (int)dataBaseObject.loadValue("category_id");
    }

    public static boolean cleanPositions(){
        return cleanPositions(0);
    }

    /**
     * cleanPositions keep order of category in parent category id list,
     * but remove duplicate position. Should not be used if positions
     * are clean at the beginning !
     *
     * @param categoryParentId parent id
     * @return boolean true if succeed
     */
    public static boolean cleanPositions(int categoryParentId){
        if (categoryParentId == 0){  return true; }

        boolean result = true;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") + "	WHERE category." + staticDataBaseObject.quoteName("parent_id") + " = ";
        query += categoryParentId + " ORDER BY category_lab." + staticDataBaseObject.quoteName("position");

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObject();
        if(resultSet != null) {
            int index = 1;

            try {
                int categoryId;
                while(resultSet.next()) {
                    categoryId = resultSet.getInt("category_id");
                    query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
                    query += " SET category_lab." + staticDataBaseObject.quoteName("position") + " = " + index + " WHERE category." + staticDataBaseObject.quoteName("parent_id");
                    query += " = " + categoryParentId + " AND category." + staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

                    index++;
                    staticDataBaseObject.setQuery(query);
                    result &= staticDataBaseObject.query(false);
                }
            }catch(SQLException ignored){

            }
        }
        return result;
    }

    public void clearCache(){
        clearCache(false);
    }

    public void clearCache(boolean all){
        if (all) {
            JeproLabCache.getInstance().remove("jeprolab_model_category_*");
        }else if(this.category_id > 0) {
            JeproLabCache.getInstance().remove("jeprolab_model_category_" + this.category_id + "_*");
        }
    }

    /*
     * Update categories for a lab
     *
     * @param categories Categories list to associate a lab
     * @param labId Categories list to associate a lab
     * @return array Update/insertion result
     * /
    public static function updateFromLaboratory($categories, int labId){
        JeproLabLaboratoryModel lab = new JeproLabLaboratoryModel(labId);
        // if array is empty or if the default category is not selected, return false
        if (!is_array($categories) || !count($categories) || !in_array(lab.category_id, $categories))
            return false;

        // delete categories for this lab
        JeproLabCategoryModel.deleteCategoriesFromLaboratory(labId);

        // and add $categories to this lab
        return JeproLabCategoryModel.addToLaboratory(categories, labId);
    }

    /**
     * Delete category from lab $id_lab
     * @param labId language filter
     * @return bool
     */
    public boolean deleteFromLaboratory(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + dataBaseObject.quoteName("lab_id");
        query += " = " + labId + " AND " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }

    /**
     * Delete every categories
     * @param labId language filter
     * @return bool
     */
    public static boolean deleteCategoriesFromLaboratory(int labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + staticDataBaseObject.quoteName("lab_id") + " = " +labId;
        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    /**
     * Add some categories to a lab
     * @param categories list of categories id
     * @param labId laboratory id
     * @return bool
     */
    public static boolean addToLaboratory(List<Integer> categories, int labId){
        if (categories.size() == 0){ return false; }
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " (" + staticDataBaseObject.quoteName("category_id");
        query += ", " + staticDataBaseObject.quoteName("lab_id") + ") VALUES ";
        List<JeproLabCategoryModel> tabCategories = new ArrayList<>();
        for(int categoryId : categories){
            tabCategories.add(new JeproLabCategoryModel(categoryId));
            query += "(" + categoryId + ", " + labId + "),";
        }
        // removing last comma to avoid SQL error
        query = query.substring( 0, query.length() - 1);

        staticDataBaseObject.setQuery(query);
        boolean result = staticDataBaseObject.query(false);
        // we have to update position for every new entries
        for( JeproLabCategoryModel category : tabCategories) {
            category.addPosition(JeproLabCategoryModel.getLastPosition(category.parent_id, labId), labId);
        }
        return result;
    }

    /**
     * check if a given laboratory is associated with this category
     * @param labId laboratory id
     * @return boolean vakue
     */
    public boolean existsInLaboratories(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab");
        query += " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND " + dataBaseObject.quoteName("lab_id") + " = " + labId;

        dataBaseObject.setQuery(query);
        return dataBaseObject.loadValue("category_id") > 0;
    }

    public boolean isRootCategoryForALaboratory(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("lab_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_lab");
        query += " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        dataBaseObject.setQuery(query);
        return dataBaseObject.loadValue("lab_id") > 0;
    }

    public static List<Integer> getLaboratoriesByCategoryId(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<Integer> labIds = new ArrayList<>();
        String query = "SELECT " + staticDataBaseObject.quoteName("lab_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lab");
        query += " WHERE " + staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObject();
        try{
            while(resultSet.next()){
                labIds.add(resultSet.getInt("lab_id"));
            }
        }catch(SQLException ignored){

        }
        return labIds;
    }

    /**
     * Add association between lab and categories
     * @param labId laboratory id
     * @return bool
     */
    public boolean addLaboratory(int labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (labId <= 0){
            boolean result = true;
            for (JeproLabLaboratoryModel lab : JeproLabLaboratoryModel.getLaboratories(false)) {
                if (!this.existsInLaboratories(lab.laboratory_id)) {
                    String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" + staticDataBaseObject.quoteName("category_id");
                    query += ", " + staticDataBaseObject.quoteName("lab_id") + ") VALUES(" + this.category_id + ", " + lab.laboratory_id + ")";

                    staticDataBaseObject.setQuery(query);
                    result &= staticDataBaseObject.query(false);
                }
            }
            return result;
        }else if(!this.existsInLaboratories(labId)) {
            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" + staticDataBaseObject.quoteName("category_id");
            query += ", " + staticDataBaseObject.quoteName("lab_id") + ") VALUES(" + this.category_id + ", " + labId + ")";

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.query(false);
        }
        return false;
    }

    public static boolean inStaticLaboratory(int categoryId) {
        return inStaticLaboratory(categoryId, null);
    }

    public static boolean inStaticLaboratory(int categoryId, JeproLabLaboratoryModel laboratory) {
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (laboratory == null || laboratory.laboratory_id <= 0) {
            laboratory = JeproLabContext.getContext().laboratory;
        }
        ResultSet interval = JeproLabCategoryModel.getInterval(laboratory.getCategoryId());
        if (interval == null) {
            return false;
        }
        String query = "SELECT n_left, n_right FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " WHERE category_id =" + categoryId;
        staticDataBaseObject.setQuery(query);

        ResultSet categorySet = staticDataBaseObject.loadObject();
        if(categorySet != null){
            try{
                if(interval.next() && categorySet.next()){
                    return ((categorySet.getInt("n_left") >= interval.getInt("n_left")) && (categorySet.getInt("n_right") >= interval.getInt("n_right")));
                }
            }catch (SQLException ignored){
                return false;
            }
        }
        return false;
    }

    public static ResultSet getUrlRewriteInformations(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT language." + staticDataBaseObject.quoteName("lang_id") + ", category_lang." + staticDataBaseObject.quoteName("link_rewrite");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__language");
        query += " AS language ON category." + staticDataBaseObject.quoteName("lang_id") + " = language." + staticDataBaseObject.quoteName("id");
        query += " WHERE category." + staticDataBaseObject.quoteName("category_id") + " = " + categoryId + "  AND language.";
        query += staticDataBaseObject.quoteName("published") + " = 1";

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    /**
     * Return n_left and n_right fields for a given category
     *
     * @param categoryId category id
     * @return array
     */
    public static ResultSet getInterval(int categoryId){
        String cacheKey = "jeprolab_category_get_interval_" + categoryId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            String query = "SELECT n_left, n_right, depth_level FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
            query += "WHERE category_id = " + categoryId;

            staticDataBaseObject.setQuery(query);
            JeproLabCache.getInstance().store(cacheKey, staticDataBaseObject.loadObject());
        }
        return (ResultSet)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public boolean cleanAssociatedAnalyzes(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_analyze_category") + " AS analyze_category WHERE ";
        query += staticDataBaseObject.quoteName("category_id") + " = " + this.category_id;
        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    public boolean addGroupsIfNoExist(int groupId) {
        List<Integer> groups = this.getGroups();
        if (!groups.contains(groupId)){
            int[] g = new int[1];
            g[0] = groupId;
            return this.addGroups(g);
        }
        return false;
    }

    public static JeproLabCategoryModel searchByPath(int langId, String path){
        return searchByPath(langId, path, false, false);
    }

    public static JeproLabCategoryModel searchByPath(int langId, String path, boolean objectToCreate){
        return searchByPath(langId, path, objectToCreate, false);
    }

    /**
     * Search with Pathes for categories
     *
     * @param langId Language ID
     * @param path of category
     * @param objectToCreate a category
     * @param methodToCreate a category
     * @return array Corresponding categories
     */
    public static JeproLabCategoryModel searchByPath(int langId, String path, boolean objectToCreate, boolean methodToCreate){
        String[] categories = path.trim().split("/");
        JeproLabCategoryModel category = null;
        int parentCategoryId = 0;

        if (categories.length > 0) {
            for(String categoryName : categories ){
                if (parentCategoryId > 0) {
                    category = JeproLabCategoryModel.searchByNameAndParentCategoryId(langId, categoryName, parentCategoryId);
                } else {
                    ResultSet categorySet = JeproLabCategoryModel.searchByName(langId, categoryName, true);
                    if(categorySet != null){
                        try{
                            if(categorySet.next()){
                                category = new JeproLabCategoryModel(categorySet.getInt("category_id"));
                            }
                        }catch (SQLException ignored){

                        }
                    }
                }

                if (category == null && objectToCreate && methodToCreate) {
                    //call_user_func_array(array($object_to_create, $method_to_create), array($id_lang, $category_name, $id_parent_category));
                    category = JeproLabCategoryModel.searchByPath(langId, categoryName);
                }

                if (category.category_id > 0) {
                    parentCategoryId = category.category_id;
                }
            }
        }
        return category;
    }

    /**
     * Specify if a category already in base
     *
     * @param categoryId Category id
     * @return boolean
     */
    public static boolean categoryExists(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
        query += " AS category WHERE category." + staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

        staticDataBaseObject.setQuery(query);
        ResultSet resultSet = staticDataBaseObject.loadObject();
        int catId = 0;
        try{
            while(resultSet.next()){
                catId = resultSet.getInt("category_id");
            }
        }catch (SQLException ignored){
            catId = 0;
        }

        return catId > 0;
    }

    public String getName(){
        return getName(0);
    }

    public String getName(int langId){
        if (langId <= 0){
            if (this.name.containsKey("lang_" + JeproLabContext.getContext().language.language_id)) {
                return this.name.get("lang_" + JeproLabContext.getContext().language.language_id);
            }else {
                langId = JeproLabSettingModel.getIntValue("default_lang");
            }
        }
        return this.name.containsKey("lang_" + langId) ? this.name.get("lang_" + langId) : "";
    }

    public static ResultSet searchByName(int langId, String query){
        return searchByName(langId, query, false);
    }

    /**
     * Light back office search for categories
     *
     * @param langId Language ID
     * @param searchQuery Searched string
     * @param unRestricted allows search without lang and includes first category and exact match
     * @return array Corresponding categories
     */
    public static ResultSet searchByName(int langId, String searchQuery, boolean unRestricted){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT category.*, category_lang.* FROM " + staticDataBaseObject.quoteName("#_jeprolab_category") + " AS category LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON(category." + staticDataBaseObject.quoteName("category_id");
        query += " = category_lang." + staticDataBaseObject.quoteName("category_id");
        if (unRestricted) {
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") WHERE " + staticDataBaseObject.quoteName("name") + " LIKE ";
            query += staticDataBaseObject.quote(searchQuery) + ")";

            staticDataBaseObject.setQuery(query);
        }else{
            query += " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
            query += ") WHERE " + staticDataBaseObject.quoteName("name") + " LIKE %" + staticDataBaseObject.quote(searchQuery) + "% AND category.";
            query += staticDataBaseObject.quoteName("category_id") + " != " + JeproLabSettingModel.getIntValue("root_category");

            staticDataBaseObject.setQuery(query);
        }
        return staticDataBaseObject.loadObject();
    }

    /**
     * Retrieve category by name and parent category id
     *
     * @param langId Language ID
     * @param categoryName Searched category name
     * @param parentCategoryId parent category ID
     * @return array Corresponding category
     */
    public static JeproLabCategoryModel searchByNameAndParentCategoryId(int langId, String categoryName, int parentCategoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category.*, category_lang.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON(category." + staticDataBaseObject.quoteName("category_id");
        query += " = category_lang." + staticDataBaseObject.quoteName("category_id") + " AND " + staticDataBaseObject.quoteName("lang_id") + " = ";
        query += langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") WHERE " + staticDataBaseObject.quoteName("name");
        query += " LIKE " + staticDataBaseObject.quote(categoryName) + " AND category." + staticDataBaseObject.quoteName("category_id") + " != ";
        query += JeproLabSettingModel.getIntValue("root_category") + " AND category." + staticDataBaseObject.quoteName("parent_id") + " = " + parentCategoryId;

        staticDataBaseObject.setQuery(query);
        ResultSet categorySet = staticDataBaseObject.loadObject();
        if(categorySet != null){
            try{
                JeproLabCategoryModel category;
                if(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    category.parent_id = categorySet.getInt("parent_id");
                    category.default_laboratory_id = categorySet.getInt("default_lab_id");
                    category.depth_level = categorySet.getInt("depth_level");
                    category.n_left = categorySet.getInt("n_left");
                    category.n_right = categorySet.getInt("n_right");
                    category.date_add = categorySet.getDate("date_add");
                    category.date_upd = categorySet.getDate("date_upd");
                    category.published = categorySet.getInt("published") > 0;
                    category.position = categorySet.getInt("position");
                    category.is_root_category = categorySet.getInt("is_root") > 0 && category.parent_id == 0;
                    category.name.put("lang_" + langId,  categorySet.getString("name"));
                    category.description.put("lang_" + langId,  categorySet.getString("description"));
                    category.link_rewrite.put("lang_" + langId,  categorySet.getString("link_rewrite"));
                    category.meta_title.put("lang_" + langId,  categorySet.getString("meta_title"));
                    category.meta_keywords.put("lang_" + langId,  categorySet.getString("meta_keywords"));
                    category.meta_description.put("lang_" + langId, categorySet.getString("meta_description"));
                    return category;
                }
                return null;
            }catch (SQLException ignored){

            }
        }

        return null;
    }

    /**
     * checkAccess return true if id_customer is in a group allowed to see this category.
     *
     * @param employeeId employee id
     *
     * @return boolean true if access allowed for customer $id_customer
     */
    public boolean checkAccess(int employeeId){
        String cacheKey = "jeprolab_category_check_access_" + this.category_id + "_" + employeeId ; //+ (!$id_customer ? "-".(int)Group.getCurrent().group_id : "");

        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            String query = "SELECT category_group." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_group") + " AS category_group ";

            if (employeeId <= 0){
                query += " WHERE category_group." + dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND category_group." + dataBaseObject.quoteName("group_id") ;
                query += " = " + JeproLabGroupModel.getCurrent().group_id;

            }else{
                query += " INNER JOIN " + dataBaseObject.quote("#__users_group") + " AS user_group ON (user_group." + dataBaseObject.quoteName("group_id");
                query += " = category_group." + dataBaseObject.quoteName("group_id") + "user_group."  + dataBaseObject.quoteName("employee_id") + " = ";
                query += employeeId + ") WHERE category_group." + dataBaseObject.quoteName("category_id") +  " = " + this.category_id;
            }
            dataBaseObject.setQuery(query);

            JeproLabCache.getInstance().store(cacheKey, dataBaseObject.loadValue("category_id"));
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey) > 0;
    }

    public static boolean setNewGroupForHome(int groupId){
        if (groupId <= 0) {
            return false;
        }
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_category_group") + " (" + staticDataBaseObject.quoteName("category_id");
        query += ", " + staticDataBaseObject.quoteName("group_id") + ") VALUES(" + JeproLabContext.getContext().laboratory.getCategoryId() + ", " + groupId + ")";

        staticDataBaseObject.setQuery(query);

        return staticDataBaseObject.query(false);
    }
/*
    public function updatePosition($way, $position){
        if (!$res = Db.getInstance()->executeS("
            SELECT cp.`id_category`, category_lab.`position`, cp.`id_parent`
            FROM `"._DB_PREFIX_."category` cp
        ".Lab.addSqlAssociation("category", "cp")."
        WHERE cp.`id_parent` = ".(int)this.id_parent."
        ORDER BY category_lab.`position` ASC"
        ))
        return false;

        $moved_category = false;
        foreach ($res as $category)
        if ((int)$category["id_category"] == (int)this.id)
            $moved_category = $category;

        if ($moved_category === false || !$position)
            return false;
        // < and > statements rather than BETWEEN operator
        // since BETWEEN is treated differently according to databases
        $result = (Db.getInstance()->execute("
            UPDATE `"._DB_PREFIX_."category` c ".Lab.addSqlAssociation("category", "c")."
        SET category_lab.`position`= category_lab.`position` ".($way ? "- 1" : "+ 1")."
        WHERE category_lab.`position`
        ".($way
                ? "> ".(int)$moved_category["position"]." AND category_lab.`position` <= ".(int)$position
        : "< ".(int)$moved_category["position"]." AND category_lab.`position` >= ".(int)$position)."
        AND c.`id_parent`=".(int)$moved_category["id_parent"])
            && Db.getInstance()->execute("
            UPDATE `"._DB_PREFIX_."category` c ".Lab.addSqlAssociation("category", "c")."
        SET category_lab.`position` = ".(int)$position."
        WHERE c.`id_parent` = ".(int)$moved_category["id_parent"]."
        AND c.`id_category`=".(int)$moved_category["id_category"]));
        Hook.exec("actionCategoryUpdate");
        return $result;
    }

    /**
     * This method allow to return children categories with the number of sub children selected for a product
     *
     * @param parentId
     * @param int $product_id
     * @param langId
     * @return array
     * /
    public static ResultSet getChildrenWithNumberOfSelectedSubCategories(int parentId, $selectedCategory, int langId, JeproLabLaboratoryModel lab = null, boolean useLabContext = true){
        if (lab == null) {
            lab = JeproLabContext.getContext().laboratory;
        }
        int labId = lab.laboratory_id > 0 ? lab.laboratory_id : JeproLabSettingModel.getIntValue("default_lang");
        $selected_cat = explode(",", str_replace(" ", "", $selected_category));
        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category." +  staticDataBaseObject.quoteName("depth_level");
        query += ", category_lang." + staticDataBaseObject.quoteName("name") + ", IF(( SELECT COUNT(*) FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
        query += " AS category_twoc WHERE category_two." +  staticDataBaseObject.quoteName("parent_id") + " = category." +  staticDataBaseObject.quoteName("category_id");
        String categoryTreeFilter = "(SELECT count(c3.`id_category`)FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category_three WHERE ";
        categoryTreeFilter += "category_three." + staticDataBaseObject.quoteName("n_left") + " > category." + staticDataBaseObject.quoteName("n_left");
        categoryTreeFilter += " AND category_three." + staticDataBaseObject.quoteName("n_right") + " < category." + staticDataBaseObject.quoteName("n_right");
        categoryTreeFilter += " AND category_three." + staticDataBaseObject.quoteName("category_id") + " IN (".implode(",", array_map("intval", $selected_cat)) +") )";
        query += ") > 0, 1, 0) AS has_children, " + (selectedCategories ? categoryTreeFilter : "0") + " AS nbSelectedSubCat FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang", labId) + ") LEFT JOIN " + staticDataBaseObject.quoteName("#_jeprolab_category_lab");
        query += " AS category_lab ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lab." + staticDataBaseObject.quoteName("category_id");
        query += " AND category_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + labId + ") WHERE " + staticDataBaseObject.quoteName("lang_id") + " = ";
        query += langId + " AND category." + staticDataBaseObject.quoteName("parent_id") + " = " + parentId;

        if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && useLabContext) {
            query += " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = " + lab.laboratory_id;
        }
        if (!JeproLabLaboratoryModel.isFeaturePublished() || JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && useLabContext) {
            query += " ORDER BY cs." +  staticDataBaseObject.quoteName("position") + " ASC";
        }

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    /**
     * Copy products from a category to another
     *
     * @param oldId Source category ID
     * @param newId Destination category ID
     * @return boolean Duplication result
     * /
    public static boolean duplicateAnalyzeCategories(int oldId, int newId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_analyze");
        query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + oldId;
        staticDataBaseObject.setQuery(query);
        ResultSet result = staticDataBaseObject.loadObject();

        $row = array();
        if ($result) {
            foreach ($result as $id) {
                $row[] = "(" + implode(", ", array((int)$new_id, $id->category_id, "(SELECT tmp.max + 1 FROM (
                        SELECT MAX(cp." + dataBaseObject.quoteName("position") + " AS max FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_category"). " cp
                WHERE cp." + dataBaseObject.quoteName("category_id") + " = " + (int)$id->category_id + ") AS tmp)"
                )) + ")";
            }
        }
        query = "INSERT IGNORE INTO " + staticDataBaseObject.quoteName("#__jeprolab_category_product") + " (" + staticDataBaseObject.quoteName("product_id");
        query += ", " + staticDataBaseObject.quoteName("category_id") + ", " + staticDataBaseObject.quoteName("position") + ") VALUES ".implode(",", $row);
        staticDataBaseObject.setQuery(query);
        boolean flag = staticDataBaseObject.query(false);

        return flag;
    }

    /**
     * Check if category can be moved in another one.
     * The category cannot be moved in a child category.
     *
     * @param categoryId current category
     * @param parentId Parent candidate
     * @return boolean Parent validity
     */
    public static boolean checkBeforeMove(int categoryId, int parentId){
        if (categoryId == parentId) return false;
        if (parentId == JeproLabSettingModel.getIntValue("root_category")) return true;
        int id = parentId;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        while (true) {
            String query = "SELECT " + staticDataBaseObject.quoteName("parent_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
            query += " WHERE " + staticDataBaseObject.quoteName("category_id") + " = " + id;
            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObject();
            if(resultSet != null) {
                try {
                    int catParentId;
                    while(resultSet.next()) {
                        catParentId = resultSet.getInt("parent_id");
                        if (catParentId <= 0) return false;
                        if (catParentId == categoryId) return false;
                        if (catParentId == JeproLabSettingModel.getIntValue("root_category")) return true;
                        id = catParentId;
                    }
                }catch (SQLException ignored){

                }
            }
        }
    }

    /*public function getLink(Link $link = null, $id_lang = null){
    public function getLink(Link $link = null, $id_lang = null){* /
    public function getLink(Link $link = null, $id_lang = null){
        if (!$link)
            $link = Context.getContext()->link;

        if (!$id_lang && is_array(this.link_rewrite))
            $id_lang = Context.getContext()->language->id;

        return $link->getCategoryLink($this, is_array(this.link_rewrite) ? this.link_rewrite[$id_lang] : this.link_rewrite, $id_lang);
    }*/

    public static List<JeproLabCategoryModel> getHomeCategories(int langId){
        return  getHomeCategories(langId, true, 0);
    }

    public static List<JeproLabCategoryModel> getHomeCategories(int langId, boolean active){
        return  getHomeCategories(langId, active, 0);
    }

    /**
     * Return main categories
     *
     * @param langId Language ID
     * @param active return only active categories
     * @param labId language filter
     * @return array categories
     */
    public static List<JeproLabCategoryModel> getHomeCategories(int langId, boolean active, int labId){
        ResultSet resultSet = JeproLabCategoryModel.getChildren(JeproLabSettingModel.getIntValue("root_category"), langId, active, labId);
        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(resultSet != null){
            try{
                JeproLabCategoryModel category;
                while(resultSet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = resultSet.getInt("category_id");
                    /*category.parent_id = resultSet.getInt("parent_id");
                    //category.default_laboratory_id = resultSet.getInt("default_lab_id");
                    //category.depth_level = resultSet.getInt("depth_level");
                    //category.n_left = resultSet.getInt("n_left");
                    //category.n_right = resultSet.getInt("n_right");
                    category.published = resultSet.getInt("published") > 0;
                    category.date_add = resultSet.getDate("date_add");
                    category.date_upd = resultSet.getDate("date_upd");
                    category.position = resultSet.getInt("position");*/
                    if(category.name == null){
                        category.name = new HashMap<>();
                    }
                    category.name.put("lang_" + langId, resultSet.getString("name"));
                    if(category.link_rewrite == null){
                        category.link_rewrite = new HashMap<>();
                    }
                    category.link_rewrite.put("lang_" + langId, resultSet.getString("link_rewrite"));
                    categories.add(category);
                }
            }catch (SQLException ignored){
ignored.printStackTrace();
            }
        }

        return categories;
    }


    public List<JeproLabCategoryModel> getAllChildren(){
        return getAllChildren(0);
    }

    /**
     * Return an array of all children of the current category
     *
     * @param langId language filter id
     * @return Collection of Category
     */
    public List<JeproLabCategoryModel> getAllChildren(int langId){
        if (langId <= 0) {
            langId = JeproLabContext.getContext().language.language_id;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        List<JeproLabCategoryModel> categories = new ArrayList<>();

        String query = "SELECT category.*, category_lang.* FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." + dataBaseObject.quoteName("category_id");
        query += " = category_lang." + dataBaseObject.quoteName("category_id") + " AND category_lang." + dataBaseObject.quoteName("lang_id");
        query += " = " + langId + ") WHERE category.n_left > " + this.n_left + " AND category.n_right < " + this.n_right;

        dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObject();
        if(resultSet != null){
            try{
                JeproLabCategoryModel category;
                while(resultSet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = resultSet.getInt("category_id");
                    category.parent_id = resultSet.getInt("parent_id");
                    category.default_laboratory_id = resultSet.getInt("default_lab_id");
                    category.depth_level = resultSet.getInt("depth_level");
                    category.n_left = resultSet.getInt("n_left");
                    category.n_right = resultSet.getInt("n_right");
                    category.published = resultSet.getInt("published") > 0;
                    category.date_add = resultSet.getDate("date_add");
                    category.date_upd = resultSet.getDate("date_upd");
                    category.position = resultSet.getInt("position");
                    categories.add(category);
                }
            }catch (SQLException ignored){

            }
        }

        return categories;
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId){
        return getAnalyzes(langId, true, null);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess){
        return getAnalyzes(langId, checkAccess, null);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context){
        return getAnalyzes(langId, checkAccess, context, 0);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage){
        return getAnalyzes(langId, checkAccess, context, requestedPage, 20);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems){
        return getAnalyzes(langId, checkAccess, context, requestedPage, numberOfItems, "");
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems, String orderBy){
        return getAnalyzes(langId, checkAccess, context, requestedPage, numberOfItems, orderBy, "");
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems, String orderBy, String orderWay){
        return getAnalyzes(langId, checkAccess, context, requestedPage, numberOfItems, orderBy, orderWay, false);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems, String orderBy, String orderWay, boolean getTotal){
        return getAnalyzes(langId, checkAccess, context, requestedPage, numberOfItems, orderBy, orderWay, getTotal, true);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems, String orderBy, String orderWay, boolean getTotal, boolean published){
        return getAnalyzes(langId, checkAccess, context, requestedPage, numberOfItems, orderBy, orderWay, getTotal, published, false);
    }

    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems, String orderBy, String orderWay, boolean getTotal, boolean published, boolean random){
        return getAnalyzes(langId, checkAccess, context, requestedPage, numberOfItems, orderBy, orderWay, getTotal, published, random, true);
    }

    /**
     * Return current category products
     *
     * @param langId Language ID
     *
     * @param checkAccess check the user access
     * @param context current app context
     * @return mixed Products or number of products
     */
    public List<JeproLabAnalyzeModel> getAnalyzes(int langId, boolean checkAccess, JeproLabContext context, int requestedPage, int numberOfItems, String orderBy, String orderWay, boolean getTotal, boolean published, boolean random, boolean randomNumberAnalyzes){
        if(context == null) {
            context = JeproLabContext.getContext();
        }
        if (checkAccess && !this.checkAccess(context.employee.employee_id)) {
            return new ArrayList<>();
        }
        /*$front = true;
        if (!in_array($context->controller->controller_type, array("front", "modulefront")))
            $front = false;
*/
        if (requestedPage < 1){ requestedPage = 1; }

        if(orderBy == null || orderBy.equals("")) {
            orderBy = "position";
        }else {
            /* Fix for all modules which are now using lowercase values for "orderBy" parameter */
            orderBy = orderBy.toLowerCase();
        }

        if (orderWay == null || orderWay.equals("")) {
            orderWay = "ASC";
        }

        String orderByPrefix = "";
        switch (orderBy) {
            case "analyze_id":
            case "date_add":
            case "date_upd":
                orderByPrefix = "analyze";
                break;
            case "name":
                orderByPrefix = "analyze_lang";
                break;
            case "manufacturer":
                orderByPrefix = "manufacturer";
                orderBy = "name";
                break;
            case "position":
                orderByPrefix = "category_analyze";
                break;
        }
        if (orderBy.equals("price")) {
            orderBy = "order_price";
        }

        if (!JeproLabTools.isOrderBy(orderBy) || !JeproLabTools.isOrderWay(orderWay)) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_ORDER_bY_OR_ORDER_WAY_MESSAGE"));
        }

        //$id_supplier = (int)Tools.getValue("id_supplier");

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        /* Return only the number of analyzes */
        if (getTotal){
            String query = "SELECT COUNT(category_analyze." + dataBaseObject.quoteName("analyze_id") + ") AS total  FROM ";
            query += dataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze " + JeproLabLaboratoryModel.addSqlAssociation("analyze");
            query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_analyze") + " AS category_analyze ON (";
            query += "analyze." + dataBaseObject.quoteName("analyze_id") + " = category_analyze." + dataBaseObject.quoteName("analyze_id");
            query += " WHERE category_analyze." + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
            //($front ? " AND product_lab.`visibility` IN ("both", "catalog")" : "").
            query += (published ? " AND analyze_lab." + dataBaseObject.quoteName("published") + " = 1" : "");
            //($id_supplier ? "AND p.id_supplier = ".(int)$id_supplier : "");
            dataBaseObject.setQuery(query);
            totalAnalyzes = (int)dataBaseObject.loadValue("total");

        }

        String query = "SELECT analyze.*, analyze_lab.*, stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity, MAX(analyze_attribute_lab.";
        query += "analyze_attribute_id) analyze_attribute_id, analyze_attribute_lab.minimal_quantity AS analyze_attribute_minimal_quantity,";
        query += " analyze_lang." + dataBaseObject.quoteName("description") + ", analyze_lang." + dataBaseObject.quoteName("short_description");
        query += ", analyze_lang." + dataBaseObject.quoteName("available_now") + ", analyze_lang." + dataBaseObject.quoteName("available_later");
        query += ", analyze_lang." + dataBaseObject.quoteName("link_rewrite") + ", analyze_lang." + dataBaseObject.quoteName("meta_description");
        query += ", analyze_lang." + dataBaseObject.quoteName("meta_keywords") + ", analyze_lang." + dataBaseObject.quoteName("meta_title");
        query += ", analyze_lang." + dataBaseObject.quoteName("name") + ", MAX(image_lab." + dataBaseObject.quoteName("image_id") + ") AS image_id,";
        query += " image_lang." + dataBaseObject.quoteName("legend") + ", technician." + dataBaseObject.quoteName("name") + " AS technician_name,";
        query += " category_lang." + dataBaseObject.quoteName("name") + " AS default_category, DATEDIFF(analyze_lab." + dataBaseObject.quoteName("date_add");
        query += ", DATE_SUB(NOW(), INTERVAL " + (JeproLabSettingModel.getIntValue("number_days_new_analyze") > 0 ? JeproLabSettingModel.getIntValue("number_days_new_analyze") : 20);
        query += " DAY)) > 0 AS new, analyze_lab.price AS order_price FROM " + dataBaseObject.quoteName("#__jeprolab_category_analyze");
        query += " AS category_analyze LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze") + " AS analyze ON analyze." ;
        query += dataBaseObject.quoteName("analyze_id") + " = category_analyze." + dataBaseObject.quoteName("analyze_id") + JeproLabLaboratoryModel.addSqlAssociation("analyze");
        query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute") + " AS analyze_attribute ON (analyze.";
        query += dataBaseObject.quoteName("analyze_id") + " = analyze_attribute." + dataBaseObject.quoteName("analyze_id") + ") " ;
        query += JeproLabLaboratoryModel.addSqlAssociation("analyze_attribute", false, "analyze_attribute_lab.`default_on` = 1") ;
        query += JeproLabAnalyzeModel.queryStock("analyze", "analyze_attribute_lab", false, context.laboratory) + " LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (analyze_lab." + dataBaseObject.quoteName("default_category_id");
        query += " = category_lang." + dataBaseObject.quoteName("category_id") + " AND category_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId;
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang";
        query += " ON (analyze." + dataBaseObject.quoteName("analyze_id") + " = analyze_lang." + dataBaseObject.quoteName("analyze_id") + " AND analyze_lang.";
        query += dataBaseObject.quoteName("lang_id") + " = " + langId + JeproLabLaboratoryModel.addSqlRestrictionOnLang("analyze_lang") + ") LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_image") + " AS image ON (image." + dataBaseObject.quoteName("analyze_id") + " = analyze.";
        query += dataBaseObject.quoteName("analyze_id") + ") " + JeproLabLaboratoryModel.addSqlAssociation("image", false, "image_lab.cover=1") + " LEFT JOIN " ;
        query += dataBaseObject.quoteName("#__jeprolab_image_lang") + " AS image_lang ON (image_lab." + dataBaseObject.quoteName("image_id") + " = image_lang.";
        query += dataBaseObject.quoteName("image_id") + " AND image_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ") LEFT JOIN ";
        query += dataBaseObject.quoteName("#__jeprolab_manufacturer") + " AS manufacturer ON manufacturer." + dataBaseObject.quoteName("manufacturer_id");
        query += " = analyze." + dataBaseObject.quoteName("manufacturer_id") + " WHERE analyze_lab." + dataBaseObject.quoteName("lab_id") + " = ";
        query += context.laboratory.laboratory_id + " AND category_analyze." + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        query += (published ? " AND analyze_lab." + dataBaseObject.quoteName("published") + " = 1" : "") +" GROUP BY analyze_lab.analyze_id";
        //        +($front ? " AND product_lab.`visibility` IN ('both, "catalog")" : "")
        //        +($id_supplier ? " AND p.id_supplier = ".(int)$id_supplier : "")

        if (random) {
            query += " ORDER BY RAND() LIMIT " + randomNumberAnalyzes;
        } else {
            query += " ORDER BY " + (!orderByPrefix.equals("") ? orderByPrefix + "." :"");
            query += dataBaseObject.quoteName(orderBy);
            query += orderWay + " LIMIT " + ((requestedPage - 1) * numberOfItems) + ", " + numberOfItems;
        }
        dataBaseObject.setQuery(query);
        ResultSet result = dataBaseObject.loadObject();
        List analyzes = new ArrayList<>();

        try{
            while(result.next()){
//todo
            }
        }catch (SQLException ignored){
            return analyzes;
        }
        if (orderBy.equals("order_price")) {
            //JeproLabTools.orderByPrice(analyzes, orderWay);
        }
        if (analyzes.size() <= 0)
            return new ArrayList<>();

        /* Modify SQL result */
        //return JeproLabAnalyzeModel.getAnalyzesProperties(langId, analyzes);
        return analyzes;
    }



    public static ResultSet getSimpleCategories(int langId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." + staticDataBaseObject.quoteName("name");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") " + JeproLabLaboratoryModel.addSqlAssociation("category");
        query += " WHERE category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category." + staticDataBaseObject.quoteName("category_id");
        query += " != " + JeproLabSettingModel.getIntValue("root_category") + "	GROUP BY category.category_id ORDER BY category." + staticDataBaseObject.quoteName("category_id");
        query += ", category_lab." + staticDataBaseObject.quoteName("position");
        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }

    public int getLaboratoryId(){
        return this.laboratory_id;
    }

    /**
     * Recursively add specified category children to $to_delete array
     *
     * @param toDelete Array reference where categories ID will be saved
     * @param categoryId Parent category ID
     */
    protected void recursiveDelete(List<Integer> toDelete, int categoryId){
        if (categoryId <= 0)
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_LABEL"));

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category");
        query += " WHERE " + dataBaseObject.quoteName("parent_id") + " = " + categoryId;
        dataBaseObject.setQuery(query);
        ResultSet result = dataBaseObject.loadObject();

        try{
            JeproLabCategoryModel category;
            while(result.next()) {
                category = new JeproLabCategoryModel(result.getInt("category_id"));
                toDelete.add(category.category_id);
                this.recursiveDelete(toDelete, category.category_id);
            }
        }catch(SQLException ignored){

        }
    }

    public boolean deleteLite(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        // @hook actionObject*DeleteBefore
        //Hook.exec("actionObjectDeleteBefore", array("object" => $this));
        //Hook.exec("actionObject".get_class($this)."DeleteBefore", array("object" => $this));

        this.clearCache();
        boolean result = true;
        // Remove association to multilab table
        if (JeproLabLaboratoryModel.isTableAssociated("category")){
            List<Integer> labListIds = JeproLabLaboratoryModel.getContextListLaboratoryIds();
            if (this.laboratory_list_ids.size() > 0) {
                labListIds = this.laboratory_list_ids;
            }
            String labList = "";
            if(labListIds.size() > 0){
                for(int i = 0; i < labListIds.size() - 1; i++){
                    labList += labListIds.get(i) + ", ";
                }
                labList += labListIds.get(labListIds.size() - 1);
            }

            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + dataBaseObject.quoteName("category_id");
            query += " = " + this.category_id + " AND " + dataBaseObject.quoteName("lab_id") + " IN(" + labList + ")";
            dataBaseObject.setQuery(query);
            result = dataBaseObject.query(false);
        }

        // Database deletion
        boolean hasMultiLabEntries = this.hasMultiLabEntries();
        if (result && !hasMultiLabEntries) {
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);
        }

        if (!result)
            return false;

        // Database deletion for multilingual fields related to the object
        if (!hasMultiLabEntries) {
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " WHERE ";
            query += dataBaseObject.quoteName("category_id") + " = " + this.category_id;
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);
        }
        // @hook actionObject*DeleteAfter
        //Hook.exec("actionObjectDeleteAfter", array("object" => $this));
        //Hook.exec("actionObject".get_class($this)."DeleteAfter", array("object" => $this));

        return result;
    }

    public boolean deleteCategory(){
        if (this.category_id == 0 || this.category_id == 1 || this.is_root_category)
            return false;

        this.clearCache();

        List<JeproLabCategoryModel> allCategories = this.getAllChildren();
        allCategories.add(this);
        for(JeproLabCategoryModel category : allCategories){
            category.deleteLite();
            if (!this.hasMultiLabEntries()){
                category.deleteImage();
                category.cleanGroups();
                category.cleanAssociatedAnalyzes();
                // Delete associated restrictions on cart rules
                List<Integer> categoryList = new ArrayList<>();
                categoryList.add(category.category_id);
                JeproLabCartRuleModel.cleanAnalyzeRuleIntegrity("categories", categoryList);
                JeproLabCategoryModel.cleanPositions(category.parent_id);
                /* Delete Categories in GroupReduction */
                if (JeproLabGroupReductionModel.getGroupsReductionByCategoryId(category.category_id) != null) {
                    JeproLabGroupReductionModel.deleteCategory(category.category_id);
                }
            }
        }

        /* Rebuild the nested tree */
        if (!this.hasMultiLabEntries() && (!this.do_not_regenerate_nested_tree))
            JeproLabCategoryModel.regenerateEntireNestedTree();

        //Hook.exec("actionCategoryDelete", array("category" => $this));

        return true;
    }

    /**
     * Delete several categories from database
     *
     * return boolean Deletion result
     * @param categories categories list
     * @return bool|int
     */
    public boolean deleteSelection(int categories[]){
        boolean result = true;
        for (int categoryId :categories){
            JeproLabCategoryModel category = new JeproLabCategoryModel(categoryId);
            if (category.isRootCategoryForALaboratory())
                return false;
            else
                result &= category.deleteCategory();
        }
        return result;
    }

    /*
     * @see ObjectModel.toggleStatus()
     */
    public boolean toggleStatus(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + dataBaseObject.quote("#__jeprolab_category") + " SET " + dataBaseObject.quoteName("published");
        query += " = " + (this.published ? 0 : 1) + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;

        dataBaseObject.setQuery(query);

        return dataBaseObject.query(false);
    }

    public JeproLabSubCategory recurseLiteCategoryTree(){
        return recurseLiteCategoryTree(3, 0, 0, null);
    }

    public JeproLabSubCategory recurseLiteCategoryTree(int maxDepth){
        return recurseLiteCategoryTree(maxDepth, 0, 0, null);
    }

    public JeproLabSubCategory recurseLiteCategoryTree(int maxDepth, int currentDepth){
        return recurseLiteCategoryTree(maxDepth, currentDepth, 0);
    }

    public JeproLabSubCategory recurseLiteCategoryTree(int maxDepth, int currentDepth, int langId){
        return recurseLiteCategoryTree(maxDepth, currentDepth, langId, null);
    }

    /**
     * Recursive scan of subcategories
     *
     * @param maxDepth Maximum depth of the tree (i.e. 2 => 3 levels depth)
     * @param currentDepth specify the current depth in the tree (don"t use it, only for recursive!)
     * @param langId Specify the id of the language used
     * @param excludedIdsArray specify a list of ids to exclude of results
     *
     * @return array Subcategories lite tree
     */
    public JeproLabSubCategory recurseLiteCategoryTree(int maxDepth, int currentDepth, int langId, int excludedIdsArray[]){
        langId = (langId > 0) ? JeproLabContext.getContext().language.language_id : langId;

        List<JeproLabCategoryModel> children = new ArrayList<>();
        List<JeproLabCategoryModel> subCategories = this.getSubCategories(langId, true);
        if ((maxDepth == 0 || currentDepth < maxDepth) && subCategories != null && subCategories.size() > 0) {
            for(JeproLabCategoryModel subCategory : subCategories) {
                if (subCategory.category_id <= 0) {
                    break;
                }else if (excludedIdsArray.length == 0 || !Arrays.asList(excludedIdsArray).contains(subCategory.category_id)) {
                    JeproLabCategoryModel category = new JeproLabCategoryModel(subCategory.category_id, langId);
                    //children[] = category.recurseLiteCategoryTree(maxDepth, currentDepth + 1, langId, excludedIdsArray);
                }
            }
        }

        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            this.description.put("lang_" + language.language_id, JeproLabTools.getCleanDescription(this.description.get("lang_" + language.language_id)));
        }

        JeproLabSubCategory jeproLabSubCategory = new JeproLabSubCategory();
        jeproLabSubCategory.category = this;
        /*jeproLabSubCategory.cat_name = this.name;
        jeproLabSubCategory.cat_description = this.description;*/
        jeproLabSubCategory.children = children;
        /*return array(
                "category_id" => (int)this.category_id,
        "link" => JRoute._("index.php?option=com_jeprolab&view=category&category_id=" + this.category_id + "&link_rewrite=" + this.link_rewrite + "&" + JeproLabTools.getCategoryToken() + "=1"),
        "name" => this.name,
                "desc"=> this.description,
                "children" => $children
        );*/
        return jeproLabSubCategory;
    }

    public List<JeproLabCategoryModel> getSubCategories(int langId){
        return getSubCategories(langId, true);
    }

    /**
     * Return current category children
     *
     * @param langId Language ID
     * @param active return only active categories
     * @return array Categories
     */
    public List<JeproLabCategoryModel> getSubCategories(int langId, boolean active){
        String sqlGroupsWhere = "";
        String sqlGroupsJoin = "";
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (JeproLabGroupModel.isFeaturePublished()) {
            sqlGroupsJoin = " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_group") + " AS category_group ON (category_group.";
            sqlGroupsJoin += dataBaseObject.quoteName("category_id") + " = category." + dataBaseObject.quoteName("category_id") + ")";
            //$groups = FrontController::getCurrentCustomerGroups();
            //sqlGroupsWhere = "AND cg.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '='.(int)Group::getCurrent()->id);
        }

        String query = "SELECT category.*, category_lang.* FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." + dataBaseObject.quoteName("category_id") + " = category_lang." + dataBaseObject.quoteName("category_id");
        query += " AND " + dataBaseObject.quoteName("lang_id") + " = " + langId + " " + JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
        query += ") " + sqlGroupsJoin + " WHERE " + dataBaseObject.quoteName("parent_id") + " = " + this.category_id;
        query += (active ? " AND " + dataBaseObject.quoteName("published") + " = 1"  : "") + sqlGroupsWhere + " GROUP BY category.";
        query += dataBaseObject.quoteName("category_id") + " ORDER BY " + dataBaseObject.quoteName("depth_level") + " ASC, category_lab.";
        query += dataBaseObject.quoteName("position") + " ASC";

        dataBaseObject.setQuery(query);
        ResultSet resultSet = dataBaseObject.loadObject();

        String formattedMedium = JeproLabImageModel.JeproLabImageTypeModel.getFormattedName("medium");

        List<JeproLabCategoryModel> categoryList = new ArrayList<>();
        if(resultSet != null) {
            try{
                JeproLabCategoryModel category;
                while(resultSet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = resultSet.getInt("category_id");
                    category.parent_id = resultSet.getInt("parent_id");
                    category.default_laboratory_id = resultSet.getInt("default_lab_id");
                    category.depth_level = resultSet.getInt("depth_level");
                    category.n_left = resultSet.getInt("n_left");
                    category.n_right = resultSet.getInt("n_right");
                    //category = resultSet.get("");
                    //category = resultSet.get("");
                    String imagePath = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY + category.category_id + ".jpg";
                    category.image_path = JeproLabTools.fileExistsInCache(imagePath) ? imagePath : "0";//resultSet.get("");
                    /*$row['id_image'] = Tools::file_exists_cache (_PS_CAT_IMG_DIR_.$row['id_category']. '.jpg')?
                    (int) $row['id_category']:Language::getIsoById ($id_lang). '-default';
                    $row['legend'] = 'no picture';*/
                    categoryList.add(category);
                }
            }catch(SQLException ignored){

            }
        }
        return categoryList;
    }

/*
    public static function recurseCategory($categories, $current, $category_id = 1, $selected_id = 1){
        echo "<option value="".$category_id +""".(($selected_id == $category_id) ? " selected="selected"" : "").">".
                str_repeat("&nbsp;", $current["infos"]["depth_level"] * 5).stripslashes($current["infos"]["name"])."</option>";
        if (isset($categories[$category_id]))
            foreach (array_keys($categories[$category_id]) as $key)
        JeproLabCategoryModel.recurseCategory($categories, $categories[$category_id][$key], $key, $selected_id);
    }*/

    public boolean deleteImage(){
        return deleteImage(false);
    }

    /**
     * Delete images associated with the object
     *
     * @param forceDelete force delete
     *
     * @return bool
     */
    public boolean deleteImage(boolean forceDelete){
        if (this.category_id <= 0) {
            return false;
        }

        if (forceDelete || !this.hasMultiLabEntries()) {
            /* Deleting object images and thumbnails (cache) */
            String imagePath;
            File imageFile;
            if (!this.image_dir.equals("")) {
                imagePath = this.image_dir + this.category_id + "." + this.image_format;
                imageFile = new File(imagePath);
                if (imageFile.exists() &&!imageFile.delete()){
                    return false;
                }
            }
            imagePath = JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + "category_" + this.category_id + "." + this.image_format;
            imageFile = new File(imagePath);
            if (imageFile.exists() && !imageFile.delete()){
                return false;
            }

            imagePath = JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + "category_mini_" + this.category_id + "." + this.image_format;
            imageFile = new File(imagePath);
            if (imageFile.exists() && !imageFile.delete()){
                return false;
            }

            List<JeproLabImageModel.JeproLabImageTypeModel> types = JeproLabImageModel.JeproLabImageTypeModel.getImagesTypes();
            for(JeproLabImageModel.JeproLabImageTypeModel imageType :  types) {
                imagePath = this.image_dir + this.category_id + "_" + imageType.name.replace("\\", "") + "." + this.image_format ;
                imageFile = new File(imagePath);
                if (imageFile.exists() && !imageFile.delete()){
                    return false;
                }
            }
        }

        return true;
    }

    public class JeproLabSubCategory{
        public JeproLabCategoryModel category;
        public List<JeproLabCategoryModel> children;

        //public JeproLabSubCategory(J)
    }
}