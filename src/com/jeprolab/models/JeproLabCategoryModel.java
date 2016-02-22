package com.jeprolab.models;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

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

    public String image_id = "default";
    public String image_dir = "";

    public static boolean multiLang = true;
    public static boolean multiLangLab = true;


    protected boolean deleted_category = true;

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
/*
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
        limit_start = context.listLimitStart; * /
        int langId = context.language.language_id;
        boolean published = false; //$app->getUserStateFromRequest($option. $view. ".published", "published", 0, "string");

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
        query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id");
        query += " = category_lang." + staticDataBaseObject.quoteName("category_id")+ JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") WHERE 1";
        query += (langId > 0 ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId : "") + (published ? " AND " + staticDataBaseObject.quoteName("published") + "= 1" : "" );
        query += (langId < 0 ? " GROUP BY category.category_id " : "") + ((sqlSort != null && !sqlSort.equals("")) ? sqlSort : " ORDER BY category." + staticDataBaseObject.quoteName("depth_level") + " ASC, category_lab." + staticDataBaseObject.quoteName("position") + " ASC");


        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }
*/
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
/*
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
        return getNestedCategories(rootCategoryId, langId, published);
    }

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[] = null, boolean useLabRestriction = true, String sqlFilter = "", String sqlSort = "", String sqlLimit = ""){
            if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(rootCategoryId <= 0) {
            die(Tools.displayError());
        }


        if(isset($groups) && JeproLabGroupModel.isFeaturePublished() && !is_array($groups)){
            $groups = (array)$groups;
        }

        String cacheKey = "jeprolab_category_getNestedCategories_" + md5((int)rootCategory.(int)langId + "_" +(int)$published + "_" +(int)$published
            + (isset($groups) && JeproLabGroupModel.isFeaturePublished() ? implode("", $groups) : ""));

        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            String query = "SELECT category.*, category_lang.* FROM " +  staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
            query += (useLabRestriction ? JeproLabLaboratoryModel.addSqlAssociation("category") : "") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
            query += " AS category_lang ON category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
            String selector = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_group") + " AS category_group ON category.";
            selector += staticDataBaseObject.quoteName("category_id") + " = category_group." + staticDataBaseObject.quoteName("category_id");
            query += (isset($groups) && JeproLabGroupModel.isFeaturePublished() ? selector : "");
            selector = " RIGHT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category_2 ON category_2." + staticDataBaseObject.quoteName("category_id");
            selector += " = " + rootCategoryId + " AND category." + staticDataBaseObject.quoteName("n_left") + " >= category_2." + staticDataBaseObject.quoteName("n_left");
            selector += " AND category." + staticDataBaseObject.quoteName("n_right") + " <= category_2." + staticDataBaseObject.quoteName("n_right");
            query += ((rootCategoryId > 0) ? selector : "") + " WHERE 1 " + sqlFilter + (langId > 0 ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId : "");
            query += (published ? " AND category." + staticDataBaseObject.quoteName("published") + " = 1" : "") ;

            query += (isset($groups) && JeproLabGroupModel.isFeaturePublished() ? " AND category_group."+ staticDataBaseObject.quoteName("group_id") + " IN (" + implode(",",  $groups) + ") " : "");
            selector = " GROUP BY category." + staticDataBaseObject.quoteName("category_id");
            query += (langId <= 0 || (isset($groups) && JeproLabGroupModel.isFeaturePublished()) ? selector : "");
            query += (sqlSort != "" ? sqlSort : " ORDER BY category." + staticDataBaseObject.quoteName("depth_level") + " ASC");
            query += (sqlSort == "" && useLabRestriction ? ", category_lab." + staticDataBaseObject.quoteName("position") + " ASC" : "");
            query += (sqlLimit != "" ? sqlLimit : "");

            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadObject();

            List categories = new ArrayList<>();
            $buff = array();
    
            if (rootCategoryId <= 0){
                rootCategoryId = JeproLabCategoryModel.getRootCategory().category_id;
            }
    
            foreach ($result as $row){
                $current = &$buff[$row->category_id];
                $current = $row;
    
                if ($row->category_id == $root_category)
                    $categories[$row->category_id] = &$current;
                else
                $buff[$row->parent_id]->children[$row->category_id] = &$current;
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
        query += staticDataBaseObject.quoteName("is_root_category") + " = 1 "  +(published ? "AND " + staticDataBaseObject.quoteName("published") + " = 1": "");

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadObject();
    }
*/
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
/*
    public static List<JeproLabCategoryModel> getCategoryInformation(int categoryIds[]){
        return getCategoryInformation(categoryIds, 0);
    }

    /**
     *
     * @param categoryIds
     * @param langId
     * @return Array
     * /
    public static List<JeproLabCategoryModel> getCategoryInformation(int categoryIds[], int langId){
        if (langId <= 0){
            langId = JeproLabContext.getContext().language.language_id;
        }

        if (categoryIds.length <= 0){ return new ArrayList<>(); }

        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." + staticDataBaseObject.quoteName("name");
        query += ", category_lang." + staticDataBaseObject.quoteName("link_rewrite") + ", category_lang." + staticDataBaseObject.quoteName("lang_id");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." ;
        query += staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") " + JeproLabLaboratoryModel.addSqlAssociation("category");
        query += " WHERE category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category.";
        query += staticDataBaseObject.quoteName("category_id") + " IN (" + implode(",", array_map("intval", categoryIds)) + ")";

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
     * @param labId
     * @return bool
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
/*
    public function getGroups(){
        String cacheKey = "jeprolab_category_getGroups_" + this.category_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT category_group." + dataBaseObject.quoteName("group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_group");
            query += " AS category_group WHERE category_group." + dataBaseObject.quoteName("category_id") + " = " + this.category_id;

            dataBaseObject.setQuery(query);
            $groups = dataBaseObject.loadObject();

            JeproLabCache.getInstance().store(cacheKey, $groups);
        }
        return JeproLabCache.getInstance().retrieve(cacheKey);
    }
*/
    public boolean isAssociatedToLaboratory(){
        return isAssociatedToLaboratory(0);
    }

    /**
     * Check if current object is associated to a lab
     *
     * @param  labId
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
     * @static
     * @param langId
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
                List category_tree = JeproLabCategoryModel.getChildren(item.category_id, context.language.language_id);
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

    public static List getChildren(int parentId, int langId){
        return getChildren(parentId, langId, true, 0);
    }

    public static List getChildren(int parentId, int langId, boolean published){
        return getChildren(parentId, langId, published, 0);
    }

    /**
     *
     * @param parentId category parent id
     * @param langId language filter
     * @param published status filter
     * @return array
     */
    public static List getChildren(int parentId, int langId, boolean published, int labId){
           String cacheKey = "jeprolab_category_get_children_" + parentId + "_" + langId + "_"+ (published ? 1 : 0) + "_" + labId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." ;
            query += staticDataBaseObject.quoteName("name") + ", category_lang." + staticDataBaseObject.quoteName("link_rewrite");
            query += ", category_lab." + staticDataBaseObject.quoteName("lab_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
            query += " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category.";
            query += staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id") ;
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") " + JeproLabLaboratoryModel.addSqlAssociation("category");
            query += " WHERE " + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category." + staticDataBaseObject.quoteName("parent_id");
            query += " = " + parentId +  (published ? " AND " + staticDataBaseObject.quoteName("published") + " = 1" : "") +" GROUP BY category.";
            query += staticDataBaseObject.quoteName("category_id") + "	ORDER BY category_lab." + staticDataBaseObject.quoteName("position") + " ASC ";
            List<JeproLabCategoryModel> children = new ArrayList<>();
            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadObject();
            try{
                JeproLabCategoryModel category;
                while(result.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = result.getInt("category_id");
                    category.laboratory_id = result.getInt("lab_id");
                    //category.name = result;
                    //category.link_rewrite = result;
                    //category = result;

                    children.add(category);
                }
            }catch (SQLException ignored){

            }
            JeproLabCache.getInstance().store(cacheKey, children);
        }
        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
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
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_PARENT_CATEGORY_DOES_NOT-EXISTS_MESSAGE"));
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

        $view = $app->input->get("view");
        int parentId = (int)$category_data["parent_id"];

        this.date_add = date("Y-m-d H:i:s");
        this.date_upd = date("Y-m-d H:i:s");

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

        if(!isset($view) || $view != "category"){
            $app->input->set("category_id", null);
            $app->redirect("index.php?option=com_jeprolab&view=category");
            return false;
        }

        if(!context.controller.has_errors){
            int categoryPublished = categoryData.containsKey("published") ? Integer.parseInt(categoryData.get("published")) : 0;

            if(!isset($depth_level_)){
                $depth_level = this.calculateDepthLevel();
            }

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
            query += ", " + dataBaseObject.quoteName("is_root_category") + ") VALUES (" + parentId + ", " + depthLevel + ", " + defaultLabId + ", ";
            query +=  categoryPublished + ", " + dataBaseObject.quote(this.date_add.toString()) + ", " + dataBaseObject.quote(this.date_upd.toString());
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
            List<Integer> labIds = Arrays.asList(categoryData.get("check_box_lab_associated_category").split(","));
            for(int labId : labIds){
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

        if (!this.doNotRegenerateNTree){
            JeproLabCategoryModel.regenerateEntireNestedTree();
        }
        this.updateGroup(this.groupBox);

        //if we create a new root category you have to associate to a lab before to add sub categories in. So we redirect to AdminCategories listing
        if(this.is_root_category){
            $link = JRoute._("index.php?option=com_jeprolab&view=category&category_id=" + JeproLabCategoryModel.getTopCategory().category_id + JeproLabTools.getCategoryToken());
            $message = "";
            $app->redirect($link, $message);
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

    /** this function return the number of category + 1 having $id_category_parent as parent.
     *
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


    /*
     * Updates level_depth for all children of the given id_category
     *
     * @param categoryId parent category
     */
    public void recalculateDepthLevel(int categoryId){
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
                this.recalculateDepthLevel(subCategoryId);
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
                    result &= dataBaseObject.query(true);
                }
            }else {
                labId = JeproLabContext.getContext().laboratory.laboratory_id;
                labId = labId > 0 ? labId : JeproLabSettingModel.getIntValue("default_lab");

                String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " ( " + dataBaseObject.quoteName("category_id");
                query += ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") + " VALUES (" + this.category_id;
                query += ", " + labId + ", " + position + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("position") + " = " + position;

                dataBaseObject.setQuery(query);
                result &= dataBaseObject.query(true);
            }
        }else{
            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " ("+ dataBaseObject.quoteName("category_id");
            query += ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") + ") VALUES (" + this.category_id;
            query += ", " + labId + ", " + position + ") ON DUPLICATE KEY UPDATE " + dataBaseObject.quoteName("position") + " = " + position;

            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(true);
        }
        return result;
    }

    /*
     * Update customer groups associated to the object
     *
     * @param list groups
     * /
    public void updateGroup(int list[]) {
        this.cleanGroups();
        if (list.length == 0) {
            list = array(JeproLabSettingModel.getIntValue("unidentified_group"), JeproLabSettingModel.getIntValue("guest_group"), JeproLabSettingModel.getIntValue("customer_group"));
        }
        this.addGroups(list);
    }

    public void addGroups(int groups[]){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query;
        for (int groupId : groups) {
            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_group") + " (" + dataBaseObject.quoteName("category_id") + ", ";
            query += dataBaseObject.quoteName("group_id") + ") VALUES (" + this.category_id + ", " + groupId + ")";

            dataBaseObject.setQuery(query);
            dataBaseObject.query();
        }
    }

    public void cleanGroups(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_group") + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        dataBaseObject.setQuery(query);
        dataBaseObject.query();
    }
*/
    protected static void subTree(Map<Integer, Map<String, List<Integer>>> categories, int categoryId, int n) {
        int left = n++;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (categories.get(categoryId).get("subcategories").size() > 0) {
            for(int subCategoryId : categories.get(categoryId).get("subcategories")){
                JeproLabCategoryModel.subTree(categories, subCategoryId, n);
            }
        }
        int right = n++;

        String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_category") + " SET n_left = "  + left +  ", n_right = "  + right;
        query += "	WHERE category_id = " + categoryId + " LIMIT 1";

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.query(false);
    }
/*
    public void updateCategory(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        $app = JFactory.getApplication();
        $input = JRequest.get("post");
        $category_data = $input["jform"];
        $languages = JeproLabLanguageModelLanguage.getLanguages();

        JeproLabContext context = JeproLabContext.getContext();

        if(!context.controller.has_errors){
            int categoryId = (int)$app->input->get("category_id");

            /** update category  * /
            if(categoryId > 0){
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
                    this.updateGroup(this.groupBox);
                    this.depth_level = this.calculateDepthLevel();

                    // If the parent category was changed, we don"t want to have 2 categories with the same position
                    if (this.getDuplicatedPosition()){
                        if ($category_data["check_box_lab_associated_category"]) {
                            foreach ($category_data["check_box_lab_associated_category"] as $associated_category_id => $row) {
                                foreach ($row as labId => $value) {
                                    this.addPosition(JeproLabCategoryModelCategory.getLastPosition((int)this.parent_id, (int)labId), (int)labId);
                                }
                            }
                        }else {
                            foreach (JeproLabLaboratoryModel.getLabs(true) as $lab) {
                                this.addPosition(max(1, JeproLabCategoryModelCategory.getLastPosition((int)this.parent_id, $lab->lab_id)), $lab->lab_id);
                            }
                        }
                    }
                    this.cleanPositions((int)this.parent_id);

                    this.clearCache();
                    this.date_upd = date("Y-m-d H:i:s");

                    $lab_list_ids = JeproLabLaboratoryModel.getContextListLabIds();
                    if(count(this.lab_list_ids) > 0){
                        $lab_list_ids = this.lab_list_ids;
                    }

                    if(JeproLabLaboratoryModel.checkDefaultLabId("category") && !this.default_lab_id){
                        this.default_lab_id = min($lab_list_ids);
                    }
                    $result = true;

                    String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " SET " + dataBaseObject.quoteName("n_left") + " = " + (int)this.n_left + ", " ;
                    query += dataBaseObject.quoteName("n_right") + " = " + (int)this.n_right + ", " + dataBaseObject.quoteName("depth_level") + " = " + (int)this.depth_level;
                    query += ", " + dataBaseObject.quoteName("published") + " = " + (int)$category_data["published"] + ", " + dataBaseObject.quoteName("default_lab_id") + " = " + (int)this.default_lab_id;
                    query += ", " + dataBaseObject.quoteName("is_root_category") + " = " + (int)$category_data["is_root_category"] + ", " + dataBaseObject.quoteName("position") + " = ";
                    query += (int)this.position + ", " + dataBaseObject.quoteName("date_upd") + " = " + dataBaseObject.quote(this.date_upd) + " WHERE " + dataBaseObject.quoteName("category_id");
                    query += " = " + (int)this.category_id;

                    dataBaseObject.setQuery(query);
                    $result &= dataBaseObject.query();

                    foreach($lab_list_ids as labId){
                        $where = " WHERE " + dataBaseObject.quoteName("category_id") + " = " + (int)this.category_id + " AND " + dataBaseObject.quoteName("lab_id") + " = " + (int)labId;
                        $select = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + $where;
                        dataBaseObject.setQuery($select);
                        $lab_exist = (dataBaseObject.loadObject()->category_id > 0);
                        if($lab_exist){
                            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " SET " + dataBaseObject.quoteName("position") + " = " + (int)this.position + $where;
                            dataBaseObject.setQuery(query);
                            $result &= dataBaseObject.query();
                        }elseif(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.CONTEXT_LAB){
                            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" + dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("lab_id") + ", " + dataBaseObject.quoteName("position") ;
                            query += ") VALUES (" + (int)this.category_id + ", " + (int)labId + ", "  + (int)this.position + ")";
                            dataBaseObject.setQuery(query);
                            $result &= dataBaseObject.query();
                        }

                        foreach($languages as $language) {
                            $where = " WHERE " + dataBaseObject.quoteName("category_id") + " = " + (int)this.category_id + " AND " + dataBaseObject.quoteName("lab_id");
                            $where += " = " + (int)labId + " AND " + dataBaseObject.quoteName("lang_id") + " = " + (int)$language->lang_id;
                            $select = "SELECT COUNT(*) FROM " + dataBaseObject.quoteNAme("#__jeprolab_category_lang") + $where;
                            dataBaseObject.setQuery($select);
                            $lang_exist = dataBaseObject.loadResult();

                            if($lang_exist) {
                                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " SET " + dataBaseObject.quoteName("name") + " = " + dataBaseObject.quote($category_data["name_" + $language->lang_id]) + ", ";
                                query += dataBaseObject.quoteName("description") + " = " + dataBaseObject.quote($category_data["description_" + $language->lang_id]) + ", " + dataBaseObject.quoteName("link_rewrite") + " = ";
                                query += dataBaseObject.quote($category_data["link_rewrite_" + $language->lang_id]) + ", " + dataBaseObject.quoteName("meta_title") + " = " + dataBaseObject.quote($category_data["meta_title_" + $language->lang_id]);
                                query += ", "  + dataBaseObject.quoteName("meta_keywords") + " = " + dataBaseObject.quote($category_data["meta_keywords_" + $language->lang_id]) + ", " + dataBaseObject.quoteName("meta_description");
                                query += " = " + dataBaseObject.quote($category_data["meta_description_" + $language->lang_id]) + $where;
                            }else{
                                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " (" + dataBaseObject.quoteName("name") + ", " + dataBaseObject.quoteName("description") + ", " + dataBaseObject.quoteName("link_rewrite");
                                query += ", " + dataBaseObject.quoteName("meta_title") + ", "  + dataBaseObject.quoteName("meta_keywords") + ", " + dataBaseObject.quoteName("meta_description") + ") VALUES (" + dataBaseObject.quote($category_data["name_" + $language->lang_id]);
                                query += ", " + dataBaseObject.quote($category_data["description_" + $language->lang_id]) + ", " + dataBaseObject.quote($category_data["link_rewrite_" + $language->lang_id]) + ", ";
                                query += dataBaseObject.quote($category_data["meta_title_" + $language->lang_id]) + ", " + dataBaseObject.quote($category_data["meta_keywords_" + $language->lang_id]) + ", ";
                                query += dataBaseObject.quote($category_data["meta_description_" + $language->lang_id]) + ") " + $where;
                            }
                            dataBaseObject.setQuery(query);
                            $result &= dataBaseObject.query();
                        }
                    }

                    if (!isset(this.doNotRegenerateNTree) || !this.doNotRegenerateNTree){
                        JeproLabCategoryModelCategory.regenerateEntireNestedtree();
                        this.recalculateLevelDepth(this.category_id);
                    }
                }
                $message = "";
                $link ="index.php?option=com_jeprolab&view=category&category_id=" + (int)this.category_id + "&task=edit" + JeproLabTools.getCategoryToken();
            }else{
                $message = "";
                $link = "index.php?option=com_jeprolab&view=category&category_id=" + (int)this.category_id + "&task=edit" + JeproLabTools.getCategoryToken();
            }
            $app->redirect($link, $message);
        }
    }

    /**
     * Search for another category with the same parent and the same position
     *
     * @return array first category found
     * /
    public int getDuplicatedPosition(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category." + dataBaseObject.quoteName("parent_id") + " = ";
        query += this.parent_id + " AND category_lab." + dataBaseObject.quoteName("position") + " = " + this.position + " AND category.";
        query += dataBaseObject.quoteName("category_id") + " != " + this.category_id;

        dataBaseObject.setQuery(query);
        ResultSet categories
        return dataBaseObject.loadObject()->category_id;
    }

    /**
     * cleanPositions keep order of category in $id_category_parent,
     * but remove duplicate position. Should not be used if positions
     * are clean at the beginning !
     *
     * @param mixed $category_parent_id
     * @return boolean true if succeed
     * /
    public static function cleanPositions($category_parent_id = null){
        if ($category_parent_id === null){  return true; }

        $return = true;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") + "	WHERE category." + dataBaseObject.quoteName("parent_id") + " = ";
        query += (int)$category_parent_id + " ORDER BY category_lab." + dataBaseObject.quoteName("position");

        dataBaseObject.setQuery(query);
        $result = dataBaseObject.loadObject();
        $count = count($result);
        for ($i = 0; $i < $count; $i++){
            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
            query += " SET category_lab." + dataBaseObject.quoteName("position") + " = " + (int)($i + 1) + " WHERE category." + dataBaseObject.quoteName("parent_id");
            query += " = " + (int)$category_parent_id + " AND category." + dataBaseObject.quoteName("category_id") + " = " +(int)$result[$i]->category_id;

            dataBaseObject.setQuery(query);
            $return &= dataBaseObject.query();
        }
        return $return;
    }

    public function clearCache($all = false){
        if ($all)
            JeproLabCache.clean("jeprolab_model_category_*");
        elseif(this.category_id)
        JeproLabCache.clean("jeprolab_model_category_" + (int)this.category_id."_*");
    }

    /**
     * Update categories for a lab
     *
     * @param string $categories Categories list to associate a lab
     * @param string labId Categories list to associate a lab
     * @return array Update/insertion result
     * /
    public static function updateFromLaboratory($categories, int labId){
        $lab = new JeproLabLaboratoryModel(labId);
        // if array is empty or if the default category is not selected, return false
        if (!is_array($categories) || !count($categories) || !in_array($lab->category_id, $categories))
            return false;

        // delete categories for this lab
        JeproLabCategoryModel.deleteCategoriesFromLaboratory(labId);

        // and add $categories to this lab
        return JeproLabCategoryModelCategory.addToLab($categories, labId);
    }

    /**
     * Delete category from lab $id_lab
     * @param int labId
     * @return bool
     * /
    public function deleteFromLaboratory(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + dataBaseObject.quoteName("lab_id") + " = " + (int)labId + " AND " + dataBaseObject.quoteName("category_id") + " = " + (int)this.category_id;
        dataBaseObject.setQuery(query);
        return dataBaseObject.query();
    }

    /**
     * Delete every categories
     * @param labId
     * @return bool
     * /
    public static function deleteCategoriesFromLaboratory(int labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + dataBaseObject.quoteName("lab_id") + " = " +(int)labId;
        dataBaseObject.setQuery(query);
        return dataBaseObject.query();
    }

    /**
     * Add some categories to a lab
     * @param array $categories
     * @param labId
     * @return bool
     * /
    public static function addToLaboratory(array $categories, labId){
        if (!is_array($categories)){ return false; }
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " (" + dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("lab_id") + ") VALUES ";
        $tab_categories = array();
        foreach ($categories as $category_id){
            $tab_categories[] = new JeproLabCategoryModelCategory($category_id);
            query += "(" +(int)$category_id + ", " +(int)labId + "),";
        }
        // removing last comma to avoid SQL error
        query = substr(query, 0, strlen(query) - 1);

        dataBaseObject.setQuery(query);
        $return = dataBaseObject.query();
        // we have to update position for every new entries
        foreach($tab_categories as $category)
        $category->addPosition(JeproLabCategoryModelCategory.getLastPosition($category->parent_id, labId), labId);

        return $return;
    }
*/
    public boolean existsInLaboratory(int labId){
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

    /*
     * Add association between lab and categories
     * @param labId
     * @return bool
     * /
    public function addLaboratory(int labId){
        $data = array();
        if (!$id_lab)
        {
            foreach (Lab.getLabs(false) as $lab)
            if (!this.existsInLab($lab["id_lab"]))
                $data[] = array(
                "id_category" => (int)this.id,
                "id_lab" => (int)$lab["id_lab"],
            );
        }
        elseif (!this.existsInLab($id_lab))
        $data[] = array(
                "id_category" => (int)this.id,
                "id_lab" => (int)$id_lab,
        );

        return Db.getInstance()->insert("category_lab", $data);
    }
*

    public static boolean inStaticLaboratory(int categoryId) {
        return inStaticLaboratory(categoryId, null);
    }

    public static boolean inStaticLaboratory(int categoryId, JeproLabLaboratoryModel laboratory) {
        if (laboratory == null || laboratory.laboratory_id <= 0)
            laboratory = JeproLabContext.getContext().laboratory;
        $interval = JeproLabCategoryModel.getInterval(laboratory.getCategoryId());
        if (!$interval) {
            return false;
        }
        $row = Db.getInstance(_PS_USE_SQL_SLAVE_)->getRow("SELECT nleft, nright FROM `"._DB_PREFIX_."category` WHERE category = " + categoryId);
        return ($row["nleft"] >= $interval["nleft"] && $row["nright"] <= $interval["nright"]);
    }

    public static function getUrlRewriteInformations(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT language." + staticDataBaseObject.quoteName("lang_id") + ", category_lang." + staticDataBaseObject.quoteName("link_rewrite");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__language");
        query += " AS language ON category." + staticDataBaseObject.quoteName("lang_id") + " = language." + staticDataBaseObject.quoteName("id");
        query += " WHERE category." + staticDataBaseObject.quoteName("category_id") + " = " + categoryId + "  AND language.";
        query += staticDataBaseObject.quoteName("published") + " = 1";

        staticDataBaseObject.setQuery(query);
        return Db.getInstance()->executeS("");
    }

    /**
     * Return n_left and n_right fields for a given category
     *
     * @param categoryId
     * @return array
     * /
    public static function getInterval(int categoryId){
        String cacheKey = "jeprolab_category_get_interval_" + categoryId;
        if (!JeproLabCache.getInstance().isStored(cacheKey))
        {
            $result = Db.getInstance()->getRow("
                SELECT nleft, nright, level_depth
                FROM "._DB_PREFIX_."category
            WHERE id_category = ".(int)$id);
            Cache.store(cacheKey, $result);
        }
        return JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public function cleanAssociatedProducts(){
        Db.getInstance()->execute("DELETE FROM `"._DB_PREFIX_."category_product` WHERE `id_category` = " + this.category_id);
    }

    public boolean addGroupsIfNoExist(int groupId) {
        $groups = this.getGroups();
        if (!in_array((int)$id_group, $groups))
            return this.addGroups(array((int)$id_group));
        return false;
    }

    public static function searchByPath(int langId, String path){
        return searchByPath(langId, path, false, false);
    }

    public static function searchByPath(int langId, String path, boolean objectToCreate){
        return searchByPath(langId, path, objectToCreate, false);
    }

    /**
     * Search with Pathes for categories
     *
     * @param langId Language ID
     * @param path of category
     * @param boolean $object_to_create a category
     * 	  * @param boolean $method_to_create a category
     * @return array Corresponding categories
     * /
    public static function searchByPath(int langId, String path, boolean objectToCreate, boolean methodToCreate){
        $categories = explode("/", trim($path));
        $category = $id_parent_category = false;

        if (is_array($categories) && count($categories))
            foreach($categories as $category_name)
        {
            if (parentCategoryId > 0) {
                $category = JeproLabCategoryModel.searchByNameAndParentCategoryId(langId, categoryName, parentCategoryId);
            }else {
                $category = JeproLabCategoryModel.searchByName(langId, categoryName, true);
            }

            if (!$category && objectToCreate && methodToCreate){
                call_user_func_array(array($object_to_create, $method_to_create), array($id_lang, $category_name , $id_parent_category));
                $category = JeproLabCategoryModelCategory.searchByPath($id_lang, $category_name);
            }

            if (isset($category["id_category"]) && $category["id_category"]) {
                parentCategoryId = (int) $category["id_category"];
            }
        }
        return $category;
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

    /*
     * Light back office search for categories
     *
     * @param integer langId Language ID
     * @param string query Searched string
     * @param boolean $unrestricted allows search without lang and includes first category and exact match
     * @return array Corresponding categories
     * /
    public static function searchByName(langId, query, $unrestricted = false){
        if ($unrestricted === true)
            return Db.getInstance()->getRow("
                SELECT c.*, cl.*
                        FROM `"._DB_PREFIX_."category` c
        LEFT JOIN `"._DB_PREFIX_."category_lang` cl ON (c.`id_category` = cl.`id_category` ".Lab.addSqlRestrictionOnLang("cl").")
        WHERE `name` LIKE \"".pSQL(query)."\"");
        else
        return Db.getInstance()->executeS("
                SELECT c.*, cl.*
                        FROM `"._DB_PREFIX_."category` c
        LEFT JOIN `"._DB_PREFIX_."category_lang` cl ON (c.`id_category` = cl.`id_category` AND `id_lang` = ".(int)langId +" ".Lab.addSqlRestrictionOnLang("cl").")
        WHERE `name` LIKE \"%".pSQL(query)."%\"
        AND c.`id_category` != ".(int)Configuration.get("PS_HOME_CATEGORY"));
    }

    /**
     * Retrieve category by name and parent category id
     *
     * @param integer $id_lang Language ID
     * @param string  $category_name Searched category name
     * @param integer $id_parent_category parent category ID
     * @return array Corresponding category
     * /
    public static function searchByNameAndParentCategoryId($id_lang, $category_name, $id_parent_category){
        return Db.getInstance()->getRow("
                SELECT c. *, cl. *
                        FROM `"._DB_PREFIX_." category` c
                LEFT JOIN `"._DB_PREFIX_." category_lang` cl
                ON(c.`id_category` = cl.`id_category`
                AND `id_lang` = ".(int)$id_lang.Lab.addSqlRestrictionOnLang("cl").")
        WHERE `name`  LIKE \"".pSQL($category_name)."\"
        AND c.`id_category` != ".(int)Configuration.get("PS_HOME_CATEGORY")."
        AND c.`id_parent` = ".(int)$id_parent_category);
    }

    /**
     * checkAccess return true if id_customer is in a group allowed to see this category.
     *
     * @param employeeId
     * @access public
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
/*
    public static function setNewGroupForHome(int groupId){
        if (!(int)$id_group)
            return false;

        return Db.getInstance()->execute("
            INSERT INTO `"._DB_PREFIX_."category_group` (`id_category`, `id_group`)
        VALUES(".(int)Context.getContext()->lab->getCategory().", ".(int)$id_group.")");
    }

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
     * @param int $parent_id
     * @param int $product_id
     * @param int langId
     * @return array
     * /
    public static function getChildrenWithNumberOfSelectedSubCategories(int parentId, $selected_category, langId, JeproLabLaboratoryModel $lab = null, $use_lab_context = true){
        if (!$lab)
            $lab = Context.getContext()->lab;

        $id_lab = $lab->id ? $lab->id : Configuration.get("PS_LAB_DEFAULT");
        $selected_cat = explode(",", str_replace(" ", "", $selected_category));
        $sql = "
        SELECT c.`id_category`, c.`level_depth`, cl.`name`,
        IF((
                SELECT COUNT(*)
        FROM `"._DB_PREFIX_."category` c2
        WHERE c2.`id_parent` = c.`id_category`
        ) > 0, 1, 0) AS has_children,
        ".($selected_cat ? "(
                SELECT count(c3.`id_category`)
        FROM `"._DB_PREFIX_."category` c3
        WHERE c3.`nleft` > c.`nleft`
        AND c3.`nright` < c.`nright`
        AND c3.`id_category`  IN (".implode(",", array_map("intval", $selected_cat)).")
        )" : "0")." AS nbSelectedSubCat
        FROM `"._DB_PREFIX_."category` c
        LEFT JOIN `"._DB_PREFIX_."category_lang` cl ON (c.`id_category` = cl.`id_category` ".Lab.addSqlRestrictionOnLang("cl", $id_lab).")
        LEFT JOIN `"._DB_PREFIX_."category_lab` cs ON (c.`id_category` = cs.`id_category` AND cs.`id_lab` = ".(int)$id_lab.")
        WHERE `id_lang` = ".(int)langId."
        AND c.`id_parent` = ".(int)$parent_id;
        if (Lab.getContext() == Lab.CONTEXT_LAB && $use_lab_context)
        $sql += " AND cs.`id_lab` = ".(int)$lab->id;
        if (!Lab.isFeatureActive() || Lab.getContext() == Lab.CONTEXT_LAB && $use_lab_context)
        $sql += " ORDER BY cs.`position` ASC";

        return Db.getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
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
                        SELECT MAX(cp." + dataBaseObject.quoteName("position") + " AS max FROM " + dataBaseObject.quoteName("#__jeprolab_category_product"). " cp
                WHERE cp." + dataBaseObject.quoteName("category_id") + " = " + (int)$id->category_id + ") AS tmp)"
                )) + ")";
            }
        }
        query = "INSERT IGNORE INTO " + staticDataBaseObject.quoteName("#__jeprolab_category_product") + " (" + staticDataBaseObject.quoteName("product_id");
        query += ", " + staticDataBaseObject.quoteName("category_id") + ", " + staticDataBaseObject.quoteName("position") + ") VALUES ".implode(",", $row);
        staticDataBaseObject.setQuery(query);
        boolean flag = staticDataBaseObject.query();

        return flag;
    }

    /**
     * Check if category can be moved in another one.
     * The category cannot be moved in a child category.
     *
     * @param categoryId current category
     * @param parentId Parent candidate
     * @return boolean Parent validity
     * /
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
            ResultSet result = staticDataBaseObject.loadObject();
            if (!isset($result->parent_id)) return false;
            if ($result->parent_id == $category_id) return false;
            if ($result->parent_id == JeproLabSettingModel.getIntValue("root_category")) return true;
            id = $result->parent_id;
        }
    }

    public function getLink(Link $link = null, $id_lang = null){
        if (!$link)
            $link = Context.getContext()->link;

        if (!$id_lang && is_array(this.link_rewrite))
            $id_lang = Context.getContext()->language->id;

        return $link->getCategoryLink($this, is_array(this.link_rewrite) ? this.link_rewrite[$id_lang] : this.link_rewrite, $id_lang);
    }


    /**
     * Return main categories
     *
     * @param integer langId Language ID
     * @param boolean $active return only active categories
     * @param bool labId
     * @return array categories
     * /
    public static function getHomeCategories(langId, $active = true, labId = false){
        return self.getChildren(JeproLabSettingModelSetting.getValue("root_category"), langId, $active, labId);
    }


    public jj getAllChildren(){
        return getAllChildren(0);
    }

    /**
     * Return an array of all children of the current category
     *
     * @param int langId
     * @return Collection of Category
     * /
    public function getAllChildren(int langId){
        if (langId <= 0) {
            langId = JeproLabContext.getContext().language.language_id;
        }
        $categories = new PrestaLabCollection("Category", $id_lang);
        $categories->where("nleft", ">", this.n_left);
        $categories->where("nright", "<", this.n_right);
        return $categories;
    }
*/
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
     * @param checkAccess
     * @param context
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
        query += JeproLabLaboratoryModel.addSqlAssociation("product_attribute", false, "product_attribute_lab.`default_on` = 1") ;
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


/*
    public static function getSimpleCategories(int langId){
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
        return dataBaseObject.loadObjectList();
    }

    public int getLaboratoryId(){
        return this.laboratory_id;
    }

    /**
     * Recursively add specified category children to $to_delete array
     *
     * @param array &$to_delete Array reference where categories ID will be saved
     * @param categoryId Parent category ID
     * /
    protected void recursiveDelete(&$to_delete, int categoryId){
        if (!is_array($to_delete) || categoryId <= 0)
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
                $to_delete[]= category.category_id;
                this.recursiveDelete($to_delete, category.category_id);
            }
        }catch(SQLException ignored){

        }
    }

    public boolean deleteLite(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        // @hook actionObject*DeleteBefore
        Hook.exec("actionObjectDeleteBefore", array("object" => $this));
        Hook.exec("actionObject".get_class($this)."DeleteBefore", array("object" => $this));

        this.clearCache();
        boolean result = true;
        // Remove association to multilab table
        if (JeproLabLaboratoryModel.isTableAssociated("category")){
            $lab_list_ids = JeproLabLaboratoryModel.getContextListLabIds();
            if (count(this.lab_list_ids)) {
                $lab_list_ids = this.lab_list_ids;
            }

            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + dataBaseObject.quoteName("category_id");
            query += " = " + (int)this.category_id + " AND " + dataBaseObject.quoteName("lab_id") + " IN(" + implode(", ", $lab_list_ids) + ")";
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query();
        }

        // Database deletion
        $has_multilab_entries = this.hasMultiLabEntries();
        if (result && !$has_multilab_entries) {
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + (int)this.category_id;
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query();
        }

        if (!result)
            return false;

        // Database deletion for multilingual fields related to the object
        if (!empty(this.def["multilang"]) && !$has_multilab_entries) {
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " WHERE ";
            query += dataBaseObject.quoteName("category_id") + " = " + this.category_id;
            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query();
        }
        // @hook actionObject*DeleteAfter
        Hook.exec("actionObjectDeleteAfter", array("object" => $this));
        Hook.exec("actionObject".get_class($this)."DeleteAfter", array("object" => $this));

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
                category.cleanAssociatedProducts();
                // Delete associated restrictions on cart rules
                JeproLabCartRuleModel.cleanProductRuleIntegrity("categories", array($category->category_id));
                JeproLabCategoryModel.cleanPositions(category.parent_id);
                /* Delete Categories in GroupReduction * /
                if (JeproLabGroupReductionModel.getGroupsReductionByCategoryId(category.category_id)) {
                    JeproLabGroupReductionModel.deleteCategory(category.category_id);
                }
            }
        }

        /* Rebuild the nested tree * /
        if (!this.hasMultiLabEntries() && (!isset(this.doNotRegenerateNestedTree) || !this.doNotRegenerateNestedTree))
            JeproLabCategoryModel.regenerateEntireNestedTree();

        Hook.exec("actionCategoryDelete", array("category" => $this));

        return true;
    }

    /**
     * Delete several categories from database
     *
     * return boolean Deletion result
     * @param categories
     * @return bool|int
     * /
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
     * /
    public function toggleStatus(){
        $result = parent.toggleStatus();
        Hook.exec("actionCategoryUpdate");
        return $result;
    }

    public function recurseLiteCategoryTree(){
        return recurseLiteCategoryTree(3, 0, 0, null);
    }

    public function recurseLiteCategoryTree(int maxDepth){
        return recurseLiteCategoryTree(maxDepth, 0, 0, null);
    }

    public function recurseLiteCategoryTree(int maxDepth, int currentDepth){
        return recurseLiteCategoryTree(maxDepth, currentDepth, 0);
    }

    public function recurseLiteCategoryTree(int maxDepth, int currentDepth, int langId){
        return recurseLiteCategoryTree(maxDepth, currentDepth, langId, null)
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
     * /
    public function recurseLiteCategoryTree(int maxDepth, int currentDepth, int langId, int excludedIdsArray[]){
        langId = (langId > 0) ? JeproLabContext.getContext().language.language_id : langId;

        $children = array();
        $subCategories = this.getSubCategories(langId, true);
        if ((maxDepth == 0 || currentDepth < maxDepth) && $subCategories && count($subCategories)) {
            for(JeproLabCategoryModel subCategory : subCategories) {
                if (subCategory.category_id <= 0) {
                    break;
                }else if (!is_array($excluded_ids_array) || !in_array($subCategory->category_id, excludedIdsArray)) {
                    JeproLabCategoryModel category = new JeproLabCategoryModel(subCategory.category_id, langId);
                    $children[] = category.recurseLiteCategoryTree(maxDepth, currentDepth + 1, langId, excludedIdsArray);
                }
            }
        }

        if (is_array(this.description)) {
            foreach(this.description as $language_id = > $description){
                this.description[$language_id] = JeproLabTools.getDescriptionClean($description);
            }
        }else {
            this.description = JeproLabTools.getCleanDescription(this.description);
        }
        return array(
                "category_id" => (int)this.category_id,
        "link" => JRoute._("index.php?option=com_jeprolab&view=category&category_id=" + this.category_id + "&link_rewrite=" + this.link_rewrite + "&" + JeproLabTools.getCategoryToken() + "=1"),
        "name" => this.name,
                "desc"=> this.description,
                "children" => $children
        );
    }

    public static function recurseCategory($categories, $current, $category_id = 1, $selected_id = 1){
        echo "<option value="".$category_id +""".(($selected_id == $category_id) ? " selected="selected"" : "").">".
                str_repeat("&nbsp;", $current["infos"]["depth_level"] * 5).stripslashes($current["infos"]["name"])."</option>";
        if (isset($categories[$category_id]))
            foreach (array_keys($categories[$category_id]) as $key)
        JeproLabCategoryModelCategory.recurseCategory($categories, $categories[$category_id][$key], $key, $selected_id);
    }*/
}