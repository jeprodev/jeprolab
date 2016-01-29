package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    //public $groupBox;

    private Pagination pagination;

    public String image_id = "default";
    public String image_dir = "";

    public static boolean multiLang = true;
    public static boolean multiLangLab = true;


    protected boolean deleted_category = true;

    //protected static $_links = array();

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

        //this.image_id = (file_exists(JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRRECTORY +  this.category_id + ".jpg")) ? this.category_id : 0;
        this.image_dir = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRRECTORY;
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
        boolean published = false; //$app->getUserStateFromRequest($option. $view. ".published", "published", 0, "string");

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
        query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id");
        query += " = category_lang." + staticDataBaseObject.quoteName("category_id")+ JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") WHERE 1";
        query += (langId > 0 ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId : "") + (published ? " AND " + staticDataBaseObject.quoteName("published") + "= 1" : "" );
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

    public static List getNestedCategories(int rootCategoryId, int langId, boolean published, $groups = null, boolean useLabRestriction = true, $sql_filter = "", $sql_sort = "", $sql_limit = ""){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if (isset($root_category) && !JeproLabTools.isInt($root_category))
        die(Tools.displayError());

        if (!JeproLabTools.isBool($published))
            die(Tools.displayError());

        if(isset($groups) && JeproLabGroupModel.isFeaturePublished() && !is_array($groups)){
            $groups = (array)$groups;
        }

        String cacheKey = "Category.getNestedCategories_".md5((int)$root_category.(int)$lang_id . "_" .(int)$published . "_" .(int)$published
            .(isset($groups) && JeproLabGroupModelGroup.isFeaturePublished() ? implode("", $groups) : ""));

        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            String query = "SELECT category.*, category_lang.* FROM " +  staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
            query += (useLabRestriction ? JeproLabLaboratoryModel.addSqlAssociation("category") : "") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
            query += " AS category_lang ON category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
            selector = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_group") . " AS category_group ON category.";
            selector += dataBaseObject->quoteName("category_id") . " = category_group." . dataBaseObject->quoteName("category_id");
            query += (isset($groups) && JeproLabGroupModel.isFeaturePublished() ? $selector : "");
            selector = " RIGHT JOIN " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category_2 ON category_2." . dataBaseObject->quoteName("category_id");
            selector += " = " . (int)$root_category . " AND category." . dataBaseObject->quoteName("n_left") . " >= category_2." + staticDataBaseObject.quoteName("n_left");
            selector += " AND category." . dataBaseObject->quoteName("n_right") . " <= category_2." . dataBaseObject->quoteName("n_right");
            query += (isset($root_category) ? $selector : "") . " WHERE 1 " . $sql_filter . ($lang_id ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " + langId : "");
            query += (published ? " AND category." + staticDataBaseObject.quoteName("published") + " = 1" : "") ;

            query += (isset($groups) && JeproLabGroupModel.isFeaturePublished() ? " AND category_group."+ staticDataBaseObject.quoteName("group_id") + " IN (" . implode(",",  $groups). ") " : "");
            selector = " GROUP BY category." + staticDataBaseObject.quoteName("category_id");
            query += (!$lang_id || (isset($groups) && JeproLabGroupModelGroup.isFeaturePublished()) ? $selector : "");
            query += ($sql_sort != "" ? $sql_sort : " ORDER BY category." + staticDataBaseObject.quoteName("depth_level") + " ASC");
            query += ($sql_sort == "" && $use_lab_restriction ? ", category_lab." + staticDataBaseObject.quoteName("position") + " ASC" : "");
            query += ($sql_limit != "" ? $sql_limit : "");

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
    public static List<JeproLabCategoryModel> getCategoryInformations(int categoryIds[]){
        return getCategoryInformations(categoryIds, 0);
    }

    /**
     *
     * @param categoryIds
     * @param langId
     * @return Array
     * /
    public static List<JeproLabCategoryModel> getCategoryInformations(int categoryIds[], int langId){
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

    /* *
     * @param labId
     * @return bool
     * /
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
        ResultSet result = dataBaseObject.loadObject();
        return (bool)dataBaseObject->loadResult();
    }

    public function getGroups(){
        String cacheKey = "jeprolab_category_getGroups_" + this.category_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT category_group." + dataBaseObject.quoteName("group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_group");
            query += " AS category_group WHERE category_group." + dataBaseObject.quoteName("category_id") + " = " + this.category_id;

            dataBaseObject.setQuery(query);
            $groups = dataBaseObject->loadObjectList();

            JeproLabCache.store(cacheKey, $groups);
        }
        return JeproLabCache.retrieve(cacheKey);
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
/*
    public static function getLinkRewrite($category_id, $lang_id){
        if (!JeproLabTools.isUnsignedInt($category_id) || !JeproLabTools.isUnsignedInt($lang_id)){
            return false;
        }

        if (!isset(self.$_links[$category_id . "_" . $lang_id])){
            if(staticDataBaseObject == null){
                staticataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            query = "SELECT category_lang." . dataBaseObject->quoteName("link_rewrite") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category_lang");
            query += " AS category_lang WHERE " . dataBaseObject->quoteName("lang_id") . " = " . (int)$lang_id ;
            query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") . " AND category_lang.";
            query += dataBaseObject->quoteName("category_id") . " = " .(int)$category_id;
            self.$_links[$category_id . "_" . $lang_id] = dataBaseObject->loadResult();
        }
        return self.$_links[$category_id . "_" . $lang_id];
    }

    public function getCategoriesList(){
        jimport("joomla.html.pagination");
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        $app = JFactory.getApplication();
        $option = $app->input->get("option");
        $view = $app->input->get("view");

        $context = JeproLabContext.getContext();

        $limit = $app->getUserStateFromRequest("global.list.limit", "limit", $app->getCfg("list_limit"), "int");
        $limitstart = $app->getUserStateFromRequest($option. $view. ".limit_start", "limit_start", 0, "int");
        $lang_id = $app->getUserStateFromRequest($option. $view. ".lang_id", "lang_id", $context->language->lang_id, "int");
        labId = $app->getUserStateFromRequest($option. $view. ".lab_id", "lab_id", $context->lab->lab_id, "int");
        $lab_group_id = $app->getUserStateFromRequest($option. $view. ".lab_group_id", "lab_group_id", $context->lab->lab_group_id, "int");
        $category_id = $app->getUserStateFromRequest($option. $view. ".category_id", "category_id", 0, "int");
        $order_by = $app->getUserStateFromRequest($option. $view. ".order_by", "order_by", "date_add", "string");
        $order_way = $app->getUserStateFromRequest($option. $view. ".order_way", "order_way", "ASC", "string");
        $published = $app->getUserStateFromRequest($option. $view. ".published", "published", 0, "string");

        $count_categories_without_parent = count(JeproLabCategoryModelCategory.getCategoriesWithoutParent());

        JeproLabCategoryModel topCategory = JeproLabCategoryModel.getTopCategory();
        int parentId = 0;
        if($category_id){
            $category = new JeproLabCategoryModel($category_id);
            $parent_id = $category->category_id;
        }elseif(!JeproLabLaboratoryModel.isFeaturePublished() && $count_categories_without_parent > 1){
            $parent_id = $top_category->category_id;
        }elseif(JeproLabLaboratoryModel.isFeaturePublished() && $count_categories_without_parent == 1){
            $parent_id = JeproLabSettingModel.getIntValue("root_category");
        }elseif(JeproLabLaboratoryModel.isFeaturePublished() && $count_categories_without_parent > 1 && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.CONTEXT_LAB){
            if(JeproLabSettingModelSetting.getValue("multilab_feature_active") && count(JeproLabLaboratoryModel.getLabs(true, null, true)) == 1){
                $parent_id = $context->lab->category_id;
            }else{
                $parent_id = $top_category->category_id;
            }
        }

        $explicitSelect = true;


        /* Manage default params values * /
        boolean useLimit = true;
        if ($limit == false)
            $use_limit = false;

        $join = " LEFT JOIN " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " AS category_lab ON (category." . dataBaseObject->quoteName("category_id") . " = category_lab." . dataBaseObject->quoteName("category_id") . " AND ";
        if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.CONTEXT_LAB){
            $join += " category_lab.lab_id = " . (int)$context->lab->lab_id . ") ";
        }else{
            $join += " category_lab.lab_id = category.default_lab_id)" ;
        }

        // we add restriction for lab
        if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.CONTEXT_LAB && JeproLabLaboratoryModel.isFeaturePublished()){
            $where = " AND category_lab." . dataBaseObject->quoteName("lab_id") . " = " . (int)JeproLabContext.getContext()->lab->lab_id;
        }
        /* Check params validity * /
        if (!JeproLabTools.isOrderBy($order_by) || !JeproLabTools.isOrderWay($order_way)
                || !is_numeric($limitstart) || !is_numeric($limit) || !JeproLabTools.isUnsignedInt($lang_id)){
            echo JError.raiseError(500,("get list params is not valid"));
        }

        /* Cache * /
        if (preg_match("/[.!]/", $order_by)){
            $order_by_split = preg_split("/[.!]/", $order_by);
            $order_by = bqSQL($order_by_split[0]).".`".bqSQL($order_by_split[1])."`";
        }elseif ($order_by){
            $order_by = dataBaseObject->quoteName(dataBaseObject->escape($order_by));
        }

        // Add SQL lab restriction
        $labLinkType = "";
        $select_lab = $join_lab = $where_lab = "";
        /*if ($labLinkType){
            $select_lab = ", lab.lab_name as lab_name ";
            $join_lab = " LEFT JOIN " ._DB_PREFIX_.this.labLinkType." lab
                            ON a.id_".this.labLinkType." = lab.id_".this.labLinkType;
            $where_lab = JeproLabLaboratoryModel.addSqlRestriction("1", "category");
        }* /

        if ($context->controller->multilab_context && JeproLabLaboratoryModel.isTableAssociated("category")){
            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.CONTEXT_ALL || !$context->employee->isSuperAdmin()){
                $test_join = !preg_match("/`?".preg_quote("#__jeprolab_category_lab")."`? *category_lab/", $join);
                if (JeproLabLaboratoryModel.isFeaturePublished() && $test_join && JeproLabLaboratoryModel.isTableAssociated("category")){
                    $where += " AND category.category_id IN ( SELECT category_lab.category_id FROM ";
                    $where += dataBaseObject->quoteName("#__jeprolab_category__lab") . " AS category_lab WHERE category_lab.";
                    $where += "lab_id IN (" . implode(", ", JeproLabLaboratoryModel.getContextListLabIds()). ") )";
                }
            }
        }

        $select = ", category_lab.position AS position ";
        $tmpTableFilter = "";

        /* Query in order to get results with all fields * /
        String langJoin = " LEFT JOIN " . dataBaseObject->quoteName("#__jeprolab_category_lang") . " AS category_lang ON (";
        $lang_join += "category_lang." . dataBaseObject->quoteName("category_id") . " = category." . dataBaseObject->quoteName("category_id");
        $lang_join += " AND category_lang." . dataBaseObject->quoteName("lang_id") . " = " .(int)$lang_id;
        if ($context->lab->lab_id){
            if (!JeproLabLaboratoryModel.isFeaturePublished()){
                $lang_join += " AND category_lang." . dataBaseObject->quoteName("lab_id") . " = 1";
            }elseif (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.CONTEXT_LAB){
                $lang_join +=  " AND category_lang." . dataBaseObject->quoteName("lab_id") . " = " .(int)$context->lab->lab_id;
            }else{
                $lang_join +=  " AND category_lang." . dataBaseObject->quoteName("lab_id") . " = category.default_lab_id";
            }
        }
        $lang_join += ") ";


        $having_clause = "";
        if (isset(this._filterHaving) || isset(this._having)){
            $having_clause = " HAVING ";
            if (isset(this._filterHaving)){
                $having_clause += ltrim(this._filterHaving, " AND ");
            }
            if(isset(this._having)){
                $having_clause += this._having." ";
            }
        }

        String query;
        do{
            query = "SELECT SQL_CALC_FOUND_ROWS " .($tmpTableFilter ? " * FROM (SELECT " : "");
            if ($explicitSelect){
                query += "category." + dataBaseObject->quoteName("category_id") . ", category_lang." . dataBaseObject->quoteName("name") . ", category_lang." . dataBaseObject->quoteName("description");
                query += " , category." + dataBaseObject->quoteName("position") ." AS category_position, " . dataBaseObject->quoteName("published");
            }else{
                query += ($lang_id ? " category_lang.*," : "") . " category.*";
            }
            query += (isset($select) ? rtrim($select, ", ") : "") . $select_lab . " FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category " . $lang_join . (isset($join) ? $join . " " : "") ;
            query += $join_lab . " WHERE 1 ". (isset($where) ? $where . " " : "") . (this.deleted_category ? " AND category." .dataBaseObject->quoteName("deleted") . " = 0 " : "") .  "AND " . dataBaseObject->quoteName("parent_id");
            query += "= " . (int)$parent_id . $where_lab .(isset($group) ? $group . " " : "") . $having_clause . " ORDER BY " . ((str_replace("`", "", $order_by) == "category_id") ? "category." : ""). " category.";
            query += $order_by . " " . dataBaseObject->escape($order_way) . ($tmpTableFilter ? ") tmpTable WHERE 1" . $tmpTableFilter : "");

            dataBaseObject->setQuery(query);
            $total = count(dataBaseObject->loadObjectList());
            query += (($use_limit === true) ? " LIMIT " . (int)$limitstart.", ".(int)$limit : "" );

            dataBaseObject->setQuery(query);
            $categories = dataBaseObject->loadObjectList();

            if ($use_limit === true){
                $limitstart = (int)$limitstart - (int)$limit;
                if ($limitstart < 0){ break; }
            }else{ break; }
        } while (empty($categories));

        if(!empty($categories)){
            foreach($categories as $item){
                $category_tree = JeproLabCategoryModelCategory.getChildren((int)$item->category_id, $context->language->lang_id);
                $item->set_view = (count($category_tree) ? 1 : 0);
            }
        }

        this.pagination = new Pagination(); //$total, $limitstart, $limit);
        return $categories;
    }
*/
    public boolean isMultiLab(){
        return JeproLabLaboratoryModel.isTableAssociated("category") || JeproLabCategoryModel.multiLangLab;
    }

    public boolean isLangMultiLab(){
        return JeproLabCategoryModel.multiLang && JeproLabCategoryModel.multiLangLab;
    }

    public Pagination getPagination(){
        return this.pagination;
    }
/*
    public hh getParentsCategories(){
        return getParentsCategories(0);
    }

    /**
     * Get Each parent category of this category until the root category
     *
     * @param integer $lang_id Language ID
     * @return array Corresponding categories
     * /
    public function getParentsCategories(int langId) {
        JeproLabContext context = JeproLabContext.getContext().clone();
        context.laboratory = clone(context.laboratory);
        int categoryId = JFactory.getApplication()->input->get("category_id");

        if (langId <= 0){ langId = context.language.language_id; }

        $categories = null;
        int currentId = this.category_id;
        if (count(JeproLabCategoryModel.getCategoriesWithoutParent()) > 1 && JeproLabSettingModel.getIntValue("multilab_feature_active") && count(JeproLabLaboratoryModel.getLabs(true, null, true)) != 1) {
            context.laboratory.category_id = JeproLabCategoryModel.getTopCategory().category_id;
        }elseif (context.laboratory.laboratory_id <= 0) {
            context.laboratory = new JeproLabLaboratoryModel(JeproLabSettingModel.getIntValue("default_lab"));
        }

        int labId = context.laboratory.laboratory_id;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query;
        JeproLabCategoryModel rootCategory;
        while (true){
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

            if ($result)
                $categories[] = $result;
            elseif (!$categories)
            $categories = array();
            if (!$result || ($result->category_id == $context->lab->category_id))
                return $categories;
            currentId = $result->parent_id;
        }
    }

    public List getChildren(int parentId, int langId){
        return getChildren(parentId, langId, true, 0);
    }

    public List getChildren(int parentId, int langId, boolean published){
        return getChildren(parentId, langId, published, 0);
    }

    /**
     *
     * @param parentId
     * @param langId
     * @param published
     * @return array
     * /
    public static List getChildren(int parentId, int langId, boolean published, int labId){
        /*if (!JeproLabTools.isBool($published)){
            die(JError.raiseError(JText._("JEPROLAB_WRONG_PARAMETER_SUPPLIED_MESSAGE")));
        }* /

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

            staticDataBaseObject.setQuery(query);
            ResultSet result = staticDataBaseObject.loadObject();
            JeproLabCache.getInstance().store(cacheKey, $result);
        }
        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Check if there is more than one entries in associated lab table for current entity
     *
     *
     * @return bool
     * /
    public boolean hasMultiLabEntries() {
        if (!JeproLabLaboratoryModel.isTableAssociated("category") || !JeproLabLaboratoryModel.isFeaturePublished()) {
            return false;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT COUNT(*) FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE ";
        query += dataBaseObject.quoteName("category_id") + " = " + this.category_id;
        dataBaseObject.setQuery(query);
        ResultSet result = dataBaseObject.loadObject();
        return (bool)dataBaseObject->loadResult();
    }

    /**
     * Get the depth level for the category
     * @return int Depth level
     * @throws JException
     * /
    public int calculateDepthLevel(){
        /* Root category * /
        if (this.parent_id < 0) {
            return 0;
        }
        JeproLabCategoryModel parentCategory = new JeproLabCategoryModel(this.parent_id);
        if (!JeproLabTools.isLoadedObject(parentCategory, "category_id")) {
            //throw new Exception("Parent category does not exist");
        }
        return parentCategory.depth_level + 1;
    }

    public function saveCategory(){
        $app = JFactory.getApplication();
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        $input = JRequest.get("post");
        $category_data = $input["jform"];
        $languages = JeproLabLanguageModelLanguage.getLanguages();
        int categoryId = $app->input->get("category_id");
        JeproLabContext context = JeproLabContext.getContext();

        $view = $app->input->get("view");
        int parentId = (int)$category_data["parent_id"];

        this.date_add = date("Y-m-d H:i:s");
        this.date_upd = date("Y-m-d H:i:s");

        //if true, we are in a root category creation
        if(parentId < 0){
            this.is_root_category = $category_data["depth_level"] = 1;
            parentId = JeproLabSettingModel.getIntValue("root_category");
        }

        if(categoryId > 0) {
            if (categoryId != parentId) {
                if (!JeproLabCategoryModel.checkBeforeMove(categoryId, parentId)) {
                    context.controller.has_errors = true;
                    JError.raiseError(500, JText._("JEPROLAB_THE_CATEGORY_CANNOT_BE_MOVED_HERE_MESSAGE"));
                }
            }else{
                context.controller.has_errors = true;
                JError.raiseError(500, JText._("JEPROLAB_THE_CATEGORY_CANNOT_BE_A_PARENT_OF_ITSELF_MESSAGE"));
            }
        }

        if(!isset($view) || $view != "category"){
            $app->input->set("category_id", null);
            $app->redirect("index.php?option=com_jeprolab&view=category");
            return false;
        }

        if(!context.controller.has_errors){
            $published = (int)$category_data["published"];

            if(!isset($depth_level_)){
                $depth_level = this.calculateDepthLevel();
            }

            int rootCategoryId = JeproLabSettingModel.getIntValue("root_category");
            if(this.is_root_category && rootCategoryId > 0){
                parentId = rootCategoryId;
            }

            $lab_list_ids = null;
            if(JeproLabLaboratoryModel.isTableAssociated("category")){
                $lab_list_ids = JeproLabLaboratoryModel.getContextListLabIds();
                if(count(this.lab_list_ids) > 0){ $lab_list_ids = this.lab_list_ids; }
            }

            if(JeproLabLaboratoryModel.checkDefaultLabId("category")){
                this.default_laboratory_id = min($lab_list_ids);
            }

            int defaultLabId = JeproLabContext.getContext().laboratory.laboratory_id;
            int position = 0;
            if(JeproLabLaboratoryModel.checkDefaultLabId("category")){
                defaultLabId = min($lab_list_ids);
            }

            $result =  true;
            String query =  "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category") + "(" + dataBaseObject.quoteName("parent_id");
            query += ", " + dataBaseObject.quoteName("depth_level") + ", " + dataBaseObject.quoteName("default_lab_id") + ", ";
            query += dataBaseObject.quoteName("published") + ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd");
            query += ", " + dataBaseObject.quoteName("is_root_category") + ") VALUES (" + parentId + ", " + depthLevel + ", " + defaultLabId + ", ";
            query +=  (int)$published + ", " + dataBaseObject.quote(this.date_add.toString()) + ", " + dataBaseObject.quote(this.date_upd.toString());
            query += ", " + (this.is_root_category ? 1 : 0) + ")";

            dataBaseObject.setQuery(query);
            ResultSet resultSet &= dataBaseObject.loadObject();
            if($result){
                this.category_id = dataBaseObject->insertid();
                foreach($lab_list_ids as labId){
                    query = "INSERT INTO " .  dataBaseObject->quoteName("#__jeprolab_category_lab") . "(" . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id");
                    query += ", " . dataBaseObject->quoteName("position") . ") VALUES (" . (int)this.category_id . ", " . (int)labId . ", " . (int)$position . ")";

                    dataBaseObject->setQuery(query);
                    $result &= dataBaseObject->query();
                    foreach($languages as $language){
                        query = "INSERT INTO " .  dataBaseObject->quoteName("#__jeprolab_category_lang") . "(" . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id");
                        query += ", " . dataBaseObject->quoteName("lang_id") . ", " . dataBaseObject->quoteName("name") . ", " . dataBaseObject->quoteName("description") . ", " . dataBaseObject->quoteName("link_rewrite");
                        query += ", " . dataBaseObject->quoteName("meta_title") . ", " . dataBaseObject->quoteName("meta_keywords") . ", " . dataBaseObject->quoteName("meta_description") . ") VALUES (";
                        query += (int)this.category_id . ", " . (int)labId . ", " . (int)$language->lang_id . ", " . dataBaseObject->quote($category_data["name_" . (int)$language->lang_id]);
                        query += ", " . dataBaseObject->quote($category_data["description_" . $language->lang_id]) . ", " . dataBaseObject->quote($category_data["link_rewrite_" . $language->lang_id]) . ", ";
                        query += dataBaseObject->quote($category_data["meta_title_" . $language->lang_id]) . ", " . dataBaseObject->quote($category_data["meta_keywords_" . $language->lang_id]) . ", ";
                        query += dataBaseObject->quote($category_data["meta_description_" . $language->lang_id]) . ")";

                        dataBaseObject->setQuery(query);
                        $result &= dataBaseObject->query();
                    }

                }
            }
        }

        if($category_data["check_box_lab_associated_category"]){
            foreach($category_data["check_box_lab_associated_category"] as labId => $value){
                int lastPosition = JeproLabCategoryModel.getLastPosition(this.parent_id, labId);
                this.addPosition(lastPosition, labId);
            }
        }else{
            for(JeproLabLaboratoryModel laboratory : JeproLabLaboratoryModel.getLabs()){
                int lastPosition = JeproLabCategoryModel.getLastPosition(this.parent_id, laboratory.laboratory_id);
                if(lastPosition <= 0){
                    lastPosition = 1;
                }
                this.addPosition(lastPosition, laboratory.laboratory_id);
            }
        }

        if (!isset(this.doNotRegenerateNTree) || !this.doNotRegenerateNTree) {
            JeproLabCategoryModel.regenerateEntireNestedTree();
        }
        this.updateGroup(this.groupBox);

        //if we create a new root category you have to associate to a lab before to add sub categories in. So we redirect to AdminCategories listing
        if(this.is_root_category){
            $link = JRoute._("index.php?option=com_jeprolab&view=category&category_id=" . (int)JeproLabCategoryModelCategory.getTopCategory()->category_id . JeproLabTools.getCategoryToken());
            $message = "";
            $app->redirect($link, $message);
        }
    }

    /**
     * Re-calculate the values of all branches of the nested tree
     * /
    public static function regenerateEntireNestedTree(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        labId = JeproLabContext.getContext()->lab->lab_id;
        labId = labId ? labId: JeproLabSettingModel.getIntValue("default_lab");

        query = "SELECT category." . dataBaseObject->quoteName("category_id") . ", category." . dataBaseObject->quoteName("parent_id") . " FROM ";
        query += dataBaseObject->quoteName("#__jeprolab_category") . " AS category LEFT JOIN " . dataBaseObject->quoteName("#__jeprolab_category_lab");
        query += " AS category_lab ON (category." . dataBaseObject->quoteName("category_id") . " = category_lab." . dataBaseObject->quoteName("category_id");
        query += " AND category_lab." . dataBaseObject->quoteName("lab_id") . " = " .(int)labId . ") ORDER BY category.";
        query += dataBaseObject->quoteName("parent_id") . ", category_lab." . dataBaseObject->quoteName("position") . " ASC";

        dataBaseObject->setQuery(query);
        $categories = dataBaseObject->loadObjectList();

        $categories_array = array();
        foreach ($categories as $category) {
            $categories_array[$category->parent_id]["subcategories"][] = $category->category_id;
        }
        $n = 1;

        if (isset($categories_array[0]) && $categories_array[0]["subcategories"]) {
            JeproLabCategoryModelCategory.subTree($categories_array, $categories_array[0]["subcategories"][0], $n);
        }
    }

    /** this function return the number of category + 1 having $id_category_parent as parent.
     *
     *
     * @param int $category_parent_id the parent category
     * @param int labId
     * @return int
     * /
    public static function getLastPosition($category_parent_id, labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT MAX(category_lab." . dataBaseObject->quoteName("position") . ") FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category LEFT JOIN ";
        query += dataBaseObject->quoteName("#__jeprolab_category_lab") . " AS category_lab ON (category." . dataBaseObject->quoteName("category_id") . " = category_lab.";
        query += dataBaseObject->quoteName("category_id") . " AND category_lab." . dataBaseObject->quoteName("lab_id") . " = " .(int)labId . ") WHERE category.";
        query += dataBaseObject->quoteName("parent_id") . " = " .(int)$category_parent_id;

        dataBaseObject->setQuery(query);
        return (1 + (int)dataBaseObject->loadResult());
    }


    /**
     * Updates level_depth for all children of the given id_category
     *
     * @param integer $category_id parent category
     * @throws JException
     * /
    public function recalculateDepthLevel($category_id){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (!is_numeric($category_id))
            throw new JException("category_id is not numeric");

        /* Gets all children * /
        query = "SELECT " . dataBaseObject->quoteName("category_id") . ", "  . dataBaseObject->quoteName("parent_id") . ", " . dataBaseObject->quoteName("depth_level") . " FROM ";
        query += dataBaseObject->quoteName("#__jeprolab_category") . " WHERE parent_id = " . (int)$category_id ;

        dataBaseObject->setQuery(query);
        $categories = dataBaseObject->loadObjectList();

        /* Gets level_depth * /
        query = "SELECT depth_level FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " WHERE category_id = " .(int)$category_id;
        dataBaseObject->setQuery(query);
        $level = dataBaseObject->loadObject()->depth_level;
        /* Updates level_depth for all children * /
        foreach ($categories as $sub_category){
            query = "UPDATE " . dataBaseObject->quoteName("#__jeprolab_category") . " SET depth_level = " . (int)($level + 1) . " WHERE category_id = " . (int)$sub_category->category_id;

            dataBaseObject->setQuery(query);
            dataBaseObject->query();
            /* Recursive call * /
            this.recalculateLevelDepth($sub_category->category_id);
        }
    }

    public function addPosition($position, labId = null){
        $return = true;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (is_null(labId)){
            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.CONTEXT_LAB) {
                foreach (JeproLabLaboratoryModel.getContextListLabIds() as labId) {
                    String query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_lab") . "(" . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id");
                    query += ", " . dataBaseObject->quoteName("position") . ") VALUES (" . (int)this.category_id . ", " . (int)labId . ", " . (int)$position . ") ";
                    query += "	ON DUPLICATE KEY UPDATE " . dataBaseObject->quoteName("position") . " = "  . (int)$position;

                    dataBaseObject->setQuery(query);
                    $return &= dataBaseObject->query();
                }
            }else {
                labId = JeproLabContext.getContext()->lab->lab_id;
                labId = labId ? labId : JeproLabSettingModelSetting.getValue("default_lab");

                query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " ( " . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id") . ", ";
                query += dataBaseObject->quoteName("position") . " VALUES (" . (int)this.category_id . ", " . (int)labId . ", " . (int)$position . ") ON DUPLICATE KEY UPDATE ";
                query += dataBaseObject->quoteName("position") . " = " . (int)$position;

                dataBaseObject->setQuery(query);
                $return &= dataBaseObject->query();
            }
        }
        else{
            query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " (" . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id");
            query += ", " . dataBaseObject->quoteName("position") . ") VALUES (" .(int)this.category_id.", ".(int)labId . ", " .(int)$position. ") ON DUPLICATE ";
            query += "KEY UPDATE " . dataBaseObject->quoteName("position") . " = " .(int)$position;

            dataBaseObject->setQuery(query);
            $return &= dataBaseObject->query();
        }
        return $return;
    }

    /**
     * Update customer groups associated to the object
     *
     * @param array $list groups
     * /
    public function updateGroup($list) {
        this.cleanGroups();
        if (empty($list)) {
            $list = array(JeproLabSettingModelSetting.getValue("unidentified_group"), JeproLabSettingModelSetting.getValue("guest_group"), JeproLabSettingModelSetting.getValue("customer_group"));
        }
        this.addGroups($list);
    }

    public function addGroups($groups){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        foreach ($groups as $group_id) {
            query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_group") . " (" . dataBaseObject->quoteName("category_id") .", ";
            query += dataBaseObject->quoteName("group_id") . ") VALUES (" . (int)this.category_id . ", " . (int)$group_id . ")";

            dataBaseObject->setQuery(query);
            dataBaseObject->query();
        }
    }

    public function cleanGroups(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        query = "DELETE FROM " . dataBaseObject->quoteName("#__jeprolab_category_group") . " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id;
        dataBaseObject->setQuery(query);
        dataBaseObject->query();
    }

    protected static void subTree(&$categories, int categoryId, &$n) {
        int left = $n++;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (isset($categories[(int)$category_id]["subcategories"])) {
            foreach ($categories[(int)$category_id]["subcategories"] as $subcategory_id) {
                JeproLabCategoryModelCategory.subTree($categories, (int)$subcategory_id, $n);
            }
        }
        int right = (int)$n++;

        String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_category") + " SET n_left = "  + left +  ", n_right = "  + right;
        query += "	WHERE category_id = " + categoryId + " LIMIT 1";

        staticDataBaseObject.setQuery(query);
        staticDataBaseObject.loadObject();
    }

    public void updateCategory(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        $app = JFactory.getApplication();
        $input = JRequest.get("post");
        $category_data = $input["jform"];
        $languages = JeproLabLanguageModelLanguage.getLanguages();

        $context = JeproLabContext.getContext();

        if(!$context->controller->has_errors){
            $category_id = (int)$app->input->get("category_id");

            /** update category  * /
            if(isset($category_id) && !empty($category_id)){
                //$category = new JeproLabCategoryModelCategory($category_id);
                if(JeproLabTools.isLoadedObject($this, "category_id")){
                    if(this.category_id == this.parent_id){
                        $context->controller->has_errors = true;
                        JError._(500, JText._("COM_JEPROLAB_A_CATEGORY_CANNOT_BE_ITS_OWN_PARENT_LABEL"));
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

                    String query = "UPDATE " . dataBaseObject->quoteName("#__jeprolab_category") . " SET " . dataBaseObject->quoteName("n_left") . " = " . (int)this.n_left . ", " ;
                    query += dataBaseObject->quoteName("n_right") . " = " . (int)this.n_right . ", " . dataBaseObject->quoteName("depth_level") . " = " . (int)this.depth_level;
                    query += ", " . dataBaseObject->quoteName("published") . " = " . (int)$category_data["published"] . ", " . dataBaseObject->quoteName("default_lab_id") . " = " . (int)this.default_lab_id;
                    query += ", " . dataBaseObject->quoteName("is_root_category") . " = " . (int)$category_data["is_root_category"] . ", " . dataBaseObject->quoteName("position") . " = ";
                    query += (int)this.position . ", " . dataBaseObject->quoteName("date_upd") . " = " . dataBaseObject->quote(this.date_upd) . " WHERE " . dataBaseObject->quoteName("category_id");
                    query += " = " . (int)this.category_id;

                    dataBaseObject.setQuery(query);
                    $result &= dataBaseObject->query();

                    foreach($lab_list_ids as labId){
                        $where = " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id . " AND " . dataBaseObject->quoteName("lab_id") . " = " . (int)labId;
                        $select = "SELECT " . dataBaseObject->quoteName("category_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category_lab") . $where;
                        dataBaseObject->setQuery($select);
                        $lab_exist = (dataBaseObject->loadObject()->category_id > 0);
                        if($lab_exist){
                            query = "UPDATE " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " SET " . dataBaseObject->quoteName("position") . " = " . (int)this.position . $where;
                            dataBaseObject->setQuery(query);
                            $result &= dataBaseObject->query();
                        }elseif(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.CONTEXT_LAB){
                            query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_lab") . "(" . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id") . ", " . dataBaseObject->quoteName("position") ;
                            query += ") VALUES (" . (int)this.category_id . ", " . (int)labId . ", "  . (int)this.position . ")";
                            dataBaseObject->setQuery(query);
                            $result &= dataBaseObject->query();
                        }

                        foreach($languages as $language) {
                            $where = " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id . " AND " . dataBaseObject->quoteName("lab_id");
                            $where += " = " . (int)labId . " AND " . dataBaseObject->quoteName("lang_id") . " = " . (int)$language->lang_id;
                            $select = "SELECT COUNT(*) FROM " . dataBaseObject->quoteNAme("#__jeprolab_category_lang") . $where;
                            dataBaseObject->setQuery($select);
                            $lang_exist = dataBaseObject->loadResult();

                            if($lang_exist) {
                                query = "UPDATE " . dataBaseObject->quoteName("#__jeprolab_category_lang") . " SET " . dataBaseObject->quoteName("name") . " = " . dataBaseObject->quote($category_data["name_" . $language->lang_id]) . ", ";
                                query += dataBaseObject->quoteName("description") . " = " . dataBaseObject->quote($category_data["description_" . $language->lang_id]) . ", " . dataBaseObject->quoteName("link_rewrite") . " = ";
                                query += dataBaseObject->quote($category_data["link_rewrite_" . $language->lang_id]) . ", " . dataBaseObject->quoteName("meta_title") . " = " . dataBaseObject->quote($category_data["meta_title_" . $language->lang_id]);
                                query += ", "  . dataBaseObject->quoteName("meta_keywords") . " = " . dataBaseObject->quote($category_data["meta_keywords_" . $language->lang_id]) . ", " . dataBaseObject->quoteName("meta_description");
                                query += " = " . dataBaseObject->quote($category_data["meta_description_" . $language->lang_id]) . $where;
                            }else{
                                query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_lang") . " (" . dataBaseObject->quoteName("name") . ", " . dataBaseObject->quoteName("description") . ", " . dataBaseObject->quoteName("link_rewrite");
                                query += ", " . dataBaseObject->quoteName("meta_title") . ", "  . dataBaseObject->quoteName("meta_keywords") . ", " . dataBaseObject->quoteName("meta_description") . ") VALUES (" . dataBaseObject->quote($category_data["name_" . $language->lang_id]);
                                query += ", " . dataBaseObject->quote($category_data["description_" . $language->lang_id]) . ", " . dataBaseObject->quote($category_data["link_rewrite_" . $language->lang_id]) . ", ";
                                query += dataBaseObject->quote($category_data["meta_title_" . $language->lang_id]) . ", " . dataBaseObject->quote($category_data["meta_keywords_" . $language->lang_id]) . ", ";
                                query += dataBaseObject->quote($category_data["meta_description_" . $language->lang_id]) . ") " . $where;
                            }
                            dataBaseObject->setQuery(query);
                            $result &= dataBaseObject->query();
                        }
                    }

                    if (!isset(this.doNotRegenerateNTree) || !this.doNotRegenerateNTree){
                        JeproLabCategoryModelCategory.regenerateEntireNestedtree();
                        this.recalculateLevelDepth(this.category_id);
                    }
                }
                $message = "";
                $link ="index.php?option=com_jeprolab&view=category&category_id=" . (int)this.category_id . "&task=edit" . JeproLabTools.getCategoryToken();
            }else{
                $message = "";
                $link = "index.php?option=com_jeprolab&view=category&category_id=" . (int)this.category_id . "&task=edit" . JeproLabTools.getCategoryToken();
            }
            $app->redirect($link, $message);
        }
    }

    /**
     * Search for another category with the same parent and the same position
     *
     * @return array first category found
     * /
    public function getDuplicatedPosition(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." . dataBaseObject->quoteName("category_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") . " WHERE category." . dataBaseObject->quoteName("parent_id") . " = ";
        query += (int)this.parent_id . " AND category_lab." . dataBaseObject->quoteName("position") . " = " .(int)this.position . " AND category.";
        query += dataBaseObject->quoteName("category_id") . " != " . (int)this.category_id;

        dataBaseObject->setQuery(query);
        return dataBaseObject->loadObject()->category_id;
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

        query = "SELECT category." . dataBaseObject->quoteName("category_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") . "	WHERE category." . dataBaseObject->quoteName("parent_id") . " = ";
        query += (int)$category_parent_id . " ORDER BY category_lab." . dataBaseObject->quoteName("position");

        dataBaseObject->setQuery(query);
        $result = dataBaseObject->loadObjezctList();
        $count = count($result);
        for ($i = 0; $i < $count; $i++){
            query = "UPDATE " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category " .JeproLabLaboratoryModel.addSqlAssociation("category");
            query += " SET category_lab." . dataBaseObject->quoteName("position") . " = " . (int)($i + 1) . " WHERE category." . dataBaseObject->quoteName("parent_id");
            query += " = " . (int)$category_parent_id . " AND category." . dataBaseObject->quoteName("category_id") . " = " .(int)$result[$i]->category_id;

            dataBaseObject->setQuery(query);
            $return &= dataBaseObject->query();
        }
        return $return;
    }

    public function clearCache($all = false){
        if ($all)
            JeproLabCache.clean("jeprolab_model_category_*");
        elseif (this.category_id)
        JeproLabCache.clean("jeprolab_model_category_" . (int)this.category_id."_*");
    }

    /**
     * Update categories for a lab
     *
     * @param string $categories Categories list to associate a lab
     * @param string labId Categories list to associate a lab
     * @return array Update/insertion result
     * /
    public static function updateFromLab($categories, labId){
        $lab = new JeproLabLaboratoryModel(labId);
        // if array is empty or if the default category is not selected, return false
        if (!is_array($categories) || !count($categories) || !in_array($lab->category_id, $categories))
            return false;

        // delete categories for this lab
        JeproLabCategoryModelCategory.deleteCategoriesFromLab(labId);

        // and add $categories to this lab
        return JeproLabCategoryModelCategory.addToLab($categories, labId);
    }

    /**
     * Delete category from lab $id_lab
     * @param int labId
     * @return bool
     * /
    public function deleteFromLab(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        query = "DELETE FROM " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " WHERE " . dataBaseObject->quoteName("lab_id") . " = " . (int)labId . " AND " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id;
        dataBaseObject->setQuery(query);
        return dataBaseObject->query();
    }

    /**
     * Delete every categories
     * @param labId
     * @return bool
     * /
    public static function deleteCategoriesFromLab(labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        query = "DELETE FROM " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " WHERE " . dataBaseObject->quoteName("lab_id") . " = " .(int)labId;
        dataBaseObject->setQuery(query);
        return dataBaseObject->query();
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
        query = "INSERT INTO " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " (" . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("lab_id") . ") VALUES ";
        $tab_categories = array();
        foreach ($categories as $category_id){
            $tab_categories[] = new JeproLabCategoryModelCategory($category_id);
            query += "(" .(int)$category_id . ", " .(int)labId . "),";
        }
        // removing last comma to avoid SQL error
        query = substr(query, 0, strlen(query) - 1);

        dataBaseObject->setQuery(query);
        $return = dataBaseObject->query();
        // we have to update position for every new entries
        foreach ($tab_categories as $category)
        $category->addPosition(JeproLabCategoryModelCategory.getLastPosition($category->parent_id, labId), labId);

        return $return;
    }

    public function existsInLab(labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        query = "SELECT " . dataBaseObject->quoteName("category_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id . " AND " . dataBaseObject->quoteName("lab_id") . " = " . (int)labId;

        dataBaseObject->setQuery(query);
        return (bool)dataBaseObject->loadResult();
    }

    public function isRootCategoryForALab(){
        return (bool)Db.getInstance(_PS_USE_SQL_SLAVE_)->getValue("
                SELECT `id_lab`
                FROM `"._DB_PREFIX_."lab`
        WHERE `id_category` = ".(int)this.id);
    }

    public static function getLabsByCategory($id_category){
        return Db.getInstance()->executeS("
                SELECT `id_lab`
                FROM `"._DB_PREFIX_."category_lab`
        WHERE `id_category` = ".(int)$id_category);
    }

    /**
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

    public static function inStaticLab($id_category, JeproLabLaboratoryModel laboratory = null) {
        if (laboratory == null || laboratory.laboratory_id <= 0)
            laboratory = Context.getContext()->lab;
        $interval = JeproLabCategoryModel.getInterval(laboratory.getCategoryId())
        if (!$interval) {
            return false;
        }
        $row = Db.getInstance(_PS_USE_SQL_SLAVE_)->getRow("SELECT nleft, nright FROM `"._DB_PREFIX_."category` WHERE id_category = ".(int)$id_category);
        return ($row["nleft"] >= $interval["nleft"] && $row["nright"] <= $interval["nright"]);
    }

    public static function getUrlRewriteInformations($id_category)
    {
        return Db.getInstance()->executeS("
            SELECT l.`id_lang`, c.`link_rewrite`
            FROM `"._DB_PREFIX_."category_lang` AS c
        LEFT JOIN  `"._DB_PREFIX_."lang` AS l ON c.`id_lang` = l.`id_lang`
        WHERE c.`id_category` = ".(int)$id_category."
        AND l.`active` = 1"
        );
    }

    /**
     * Return nleft and nright fields for a given category
     *
     * @since 1.5.0
     * @param int $id
     * @return array
     * /
    public static function getInterval($id){
        cacheKey = "Category.getInterval_".(int)$id;
        if (!Cache.isStored(cacheKey))
        {
            $result = Db.getInstance()->getRow("
                SELECT nleft, nright, level_depth
                FROM "._DB_PREFIX_."category
            WHERE id_category = ".(int)$id);
            Cache.store(cacheKey, $result);
        }
        return Cache.retrieve(cacheKey);
    }

    public function cleanAssociatedProducts(){
        Db.getInstance()->execute("DELETE FROM `"._DB_PREFIX_."category_product` WHERE `id_category` = ".(int)this.id);
    }

    public function addGroupsIfNoExist(int groupId) {
        $groups = this.getGroups();
        if (!in_array((int)$id_group, $groups))
            return this.addGroups(array((int)$id_group));
        return false;
    }

    /**
     * Search with Pathes for categories
     *
     * @param integer $id_lang Language ID
     * @param string $path of category
     * @param boolean $object_to_create a category
     * 	  * @param boolean $method_to_create a category
     * @return array Corresponding categories
     * /
    public static function searchByPath($id_lang, $path, $object_to_create = false, $method_to_create = false)
    {
        $categories = explode("/", trim($path));
        $category = $id_parent_category = false;

        if (is_array($categories) && count($categories))
            foreach($categories as $category_name)
        {
            if ($id_parent_category)
                $category = JeproLabCatetoryModelCategory.searchByNameAndParentCategoryId($id_lang, $category_name, $id_parent_category);
            else
            $category = JeproLabCategoryModelCategory.searchByName($id_lang, $category_name,true);

            if (!$category && $object_to_create && $method_to_create)
            {
                call_user_func_array(array($object_to_create, $method_to_create), array($id_lang, $category_name , $id_parent_category));
                $category = JeproLabCategoryModelCategory.searchByPath($id_lang, $category_name);
            }
            if (isset($category["id_category"]) && $category["id_category"])
                $id_parent_category = (int)$category["id_category"];
        }
        return $category;
    }

    /**
     * Specify if a category already in base
     *
     * @param int $id_category Category id
     * @return boolean
     * /
    public static function categoryExists($id_category){
        $row = Db.getInstance()->getRow("
                SELECT `id_category`
                FROM "._DB_PREFIX_."category c
        WHERE c.`id_category` = ".(int)$id_category);

        return isset($row["id_category"]);
    }

    public function getName($lang_id = null){
        if (!$lang_id){
            if (isset(this.name[JeproLabContext.getContext()->language->lang_id])) {
                $lang_id = JeproLabContext.getContext().language.lang_id;
            }else {
                $lang_id = (int)JeproLabSettingModelSetting.getValue("default_lang");
            }
        }
        return isset(this.name[$lang_id]) ? this.name[$lang_id] : "";
    }

    /**
     * Light back office search for categories
     *
     * @param integer $lang_id Language ID
     * @param string query Searched string
     * @param boolean $unrestricted allows search without lang and includes first category and exact match
     * @return array Corresponding categories
     * /
    public static function searchByName($lang_id, query, $unrestricted = false){
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
        LEFT JOIN `"._DB_PREFIX_."category_lang` cl ON (c.`id_category` = cl.`id_category` AND `id_lang` = ".(int)$lang_id ." ".Lab.addSqlRestrictionOnLang("cl").")
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
                SELECT c.*, cl.*
                        FROM `"._DB_PREFIX_."category` c
        LEFT JOIN `"._DB_PREFIX_."category_lang` cl
        ON (c.`id_category` = cl.`id_category`
        AND `id_lang` = ".(int)$id_lang.Lab.addSqlRestrictionOnLang("cl").")
        WHERE `name`  LIKE \"".pSQL($category_name)."\"
        AND c.`id_category` != ".(int)Configuration.get("PS_HOME_CATEGORY")."
        AND c.`id_parent` = ".(int)$id_parent_category);
    }

    /**
     * checkAccess return true if id_customer is in a group allowed to see this category.
     *
     * @param mixed $id_customer
     * @access public
     * @return boolean true if access allowed for customer $id_customer
     * /
    public function checkAccess($id_customer)
    {
        cacheKey = "Category.checkAccess_".(int)this.id."-".$id_customer.(!$id_customer ? "-".(int)Group.getCurrent()->id : "");
        if (!Cache.isStored(cacheKey))
        {
            if (!$id_customer)
                $result = (bool)Db.getInstance(_PS_USE_SQL_SLAVE_)->getValue("
                SELECT ctg.`id_group`
                FROM "._DB_PREFIX_."category_group ctg
            WHERE ctg.`id_category` = ".(int)this.id." AND ctg.`id_group` = ".(int)Group.getCurrent()->id);
            else
            $result = (bool)Db.getInstance(_PS_USE_SQL_SLAVE_)->getValue("
                SELECT ctg.`id_group`
                FROM "._DB_PREFIX_."category_group ctg
            INNER JOIN "._DB_PREFIX_."customer_group cg on (cg.`id_group` = ctg.`id_group` AND cg.`id_customer` = ".(int)$id_customer.")
            WHERE ctg.`id_category` = ".(int)this.id);
            Cache.store(cacheKey, $result);
        }
        return Cache.retrieve(cacheKey);
    }

    public static function setNewGroupForHome($id_group)
    {
        if (!(int)$id_group)
            return false;

        return Db.getInstance()->execute("
            INSERT INTO `"._DB_PREFIX_."category_group` (`id_category`, `id_group`)
        VALUES (".(int)Context.getContext()->lab->getCategory().", ".(int)$id_group.")");
    }

    public function updatePosition($way, $position)
    {
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
     * @param int $lang_id
     * @return array
     * /
    public static function getChildrenWithNumberOfSelectedSubCategories($parent_id, $selected_category, $lang_id, JeproLabLaboratoryModel $lab = null, $use_lab_context = true){
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
        WHERE `id_lang` = ".(int)$lang_id."
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
     * @param integer $old_id Source category ID
     * @param boolean $new_id Destination category ID
     * @return boolean Duplication result
     * /
    public static function duplicateProductCategories($old_id, $new_id){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        query = "SELECT " . dataBaseObject->quoteName("category_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category_product") . " WHERE " . dataBaseObject->quoteName("product_id") . " = " .(int)$old_id;
        dataBaseObject->setQuery(query);
        $result = dataBaseObject->loadObjectList();

        $row = array();
        if ($result) {
            foreach ($result as $id) {
                $row[] = "(" . implode(", ", array((int)$new_id, $id->category_id, "(SELECT tmp.max + 1 FROM (
                        SELECT MAX(cp." . dataBaseObject->quoteName("position") . " AS max FROM " . dataBaseObject->quoteName("#__jeprolab_category_product"). " cp
                WHERE cp." . dataBaseObject->quoteName("category_id") . " = " . (int)$id->category_id . ") AS tmp)"
                )) . ")";
            }
        }
        query = "INSERT IGNORE INTO " . dataBaseObject->quoteName("#__jeprolab_category_product") . " (" . dataBaseObject->quoteName("product_id") . ", " . dataBaseObject->quoteName("category_id") . ", " . dataBaseObject->quoteName("position") . ") VALUES ".implode(",", $row);
        dataBaseObject->setQuery(query);
        $flag = dataBaseObject->query();

        return $flag;
    }

    /**
     * Check if category can be moved in another one.
     * The category cannot be moved in a child category.
     *
     * @param integer $category_id current category
     * @param integer $parent_id Parent candidate
     * @return boolean Parent validity
     * /
    public static function checkBeforeMove($category_id, $parent_id){
        if ($category_id == $parent_id) return false;
        if ($parent_id == Configuration.get("PS_HOME_CATEGORY")) return true;
        $i = (int)$parent_id;
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        while (42) {
            String query = "SELECT " . dataBaseObject->quoteName("parent_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " WHERE " . dataBaseObject->quoteName("category_id") . " = " .(int)$i;
            dataBaseObject->setQuery(query);
            $result = dataBaseObject->loadObject();
            if (!isset($result->parent_id)) return false;
            if ($result->parent_id == $category_id) return false;
            if ($result->parent_id == JeproLabSettingModelSetting.getValue("root_category")) return true;
            $i = $result->parent_id;
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
     * @param integer $lang_id Language ID
     * @param boolean $active return only active categories
     * @param bool labId
     * @return array categories
     * /
    public static function getHomeCategories($lang_id, $active = true, labId = false){
        return self.getChildren(JeproLabSettingModelSetting.getValue("root_category"), $lang_id, $active, labId);
    }


    public jj getAllChildren(){
        return getAllChildren(0);
    }

    /**
     * Return an array of all children of the current category
     *
     * @param int $lang_id
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

    /**
     * Return current category products
     *
     * @param integer $lang_id Language ID
     *
     * @param bool $check_access
     * @param JeproLabContext $context
     * @return mixed Products or number of products
     * /
    public function getProducts($lang_id, $check_access = true, JeproLabContext $context = null) { //$p, $n, $order_by = null, $order_way = null, $get_total = false, $active = true, $random = false, $random_number_products = 1, ,
        if (!$context)
            $context = JeproLabContext.getContext();
        if ($check_access && !this.checkAccess($context->customer->customer_id))
            return false;

        $front = true;
        if (!in_array($context->controller->controller_type, array("front", "modulefront")))
            $front = false;

        if ($p < 1) $p = 1;

        if (empty($order_by))
            $order_by = "position";
        else
            /* Fix for all modules which are now using lowercase values for "orderBy" parameter * /
            $order_by = strtolower($order_by);

        if (empty($order_way))
            $order_way = "ASC";

        $order_by_prefix = false;
        if ($order_by == "id_product" || $order_by == "date_add" || $order_by == "date_upd")
            $order_by_prefix = "p";
        elseif ($order_by == "name")
        $order_by_prefix = "pl";
        elseif ($order_by == "manufacturer")
        {
            $order_by_prefix = "m";
            $order_by = "name";
        }
        elseif ($order_by == "position")
        $order_by_prefix = "cp";

        if ($order_by == "price")
            $order_by = "order_price";

        if (!Validate.isBool($active) || !Validate.isOrderBy($order_by) || !Validate.isOrderWay($order_way))
        die (Tools.displayError());

        $id_supplier = (int)Tools.getValue("id_supplier");

        /* Return only the number of products * /
        if ($get_total)
        {
            $sql = "SELECT COUNT(cp.`id_product`) AS total
            FROM `"._DB_PREFIX_."product` p
            ".Lab.addSqlAssociation("product", "p")."
            LEFT JOIN `"._DB_PREFIX_."category_product` cp ON p.`id_product` = cp.`id_product`
            WHERE cp.`id_category` = ".(int)this.id.
            ($front ? " AND product_lab.`visibility` IN ("both", "catalog")" : "").
            ($active ? " AND product_lab.`active` = 1" : "").
            ($id_supplier ? "AND p.id_supplier = ".(int)$id_supplier : "");
            return (int)Db.getInstance(_PS_USE_SQL_SLAVE_)->getValue($sql);
        }

        $sql = "SELECT p.*, product_lab.*, stock.out_of_stock, IFNULL(stock.quantity, 0) as quantity, MAX(product_attribute_lab.id_product_attribute) id_product_attribute, product_attribute_lab.minimal_quantity AS product_attribute_minimal_quantity, pl.`description`, pl.`description_short`, pl.`available_now`,
        pl.`available_later`, pl.`link_rewrite`, pl.`meta_description`, pl.`meta_keywords`, pl.`meta_title`, pl.`name`, MAX(image_lab.`id_image`) id_image,
                il.`legend`, m.`name` AS manufacturer_name, cl.`name` AS category_default,
        DATEDIFF(product_lab.`date_add`, DATE_SUB(NOW(),
                INTERVAL ".(Validate.isUnsignedInt(Configuration.get("PS_NB_DAYS_NEW_PRODUCT")) ? Configuration.get("PS_NB_DAYS_NEW_PRODUCT") : 20)."
        DAY)) > 0 AS new, product_lab.price AS orderprice
        FROM `"._DB_PREFIX_."category_product` cp
        LEFT JOIN `"._DB_PREFIX_."product` p
        ON p.`id_product` = cp.`id_product`
        ".Lab.addSqlAssociation("product", "p")."
        LEFT JOIN `"._DB_PREFIX_."product_attribute` pa
        ON (p.`id_product` = pa.`id_product`)
        ".Lab.addSqlAssociation("product_attribute", "pa", false, "product_attribute_lab.`default_on` = 1")."
        ".Product.sqlStock("p", "product_attribute_lab", false, $context->lab)."
        LEFT JOIN `"._DB_PREFIX_."category_lang` cl
        ON (product_lab.`id_category_default` = cl.`id_category`
        AND cl.`id_lang` = ".(int)$lang_id.Lab.addSqlRestrictionOnLang("cl").")
        LEFT JOIN `"._DB_PREFIX_."product_lang` pl
        ON (p.`id_product` = pl.`id_product`
        AND pl.`id_lang` = ".(int)$lang_id.Lab.addSqlRestrictionOnLang("pl").")
        LEFT JOIN `"._DB_PREFIX_."image` i
        ON (i.`id_product` = p.`id_product`)".
        Lab.addSqlAssociation("image", "i", false, "image_lab.cover=1")."
        LEFT JOIN `"._DB_PREFIX_."image_lang` il
        ON (image_lab.`id_image` = il.`id_image`
        AND il.`id_lang` = ".(int)$lang_id.")
        LEFT JOIN `"._DB_PREFIX_."manufacturer` m
        ON m.`id_manufacturer` = p.`id_manufacturer`
        WHERE product_lab.`id_lab` = ".(int)$context->lab->id."
        AND cp.`id_category` = ".(int)this.id
                .($active ? " AND product_lab.`active` = 1" : "")
                .($front ? " AND product_lab.`visibility` IN ("both", "catalog")" : "")
                .($id_supplier ? " AND p.id_supplier = ".(int)$id_supplier : "")
        ." GROUP BY product_lab.id_product";

        if ($random === true)
            $sql += " ORDER BY RAND() LIMIT ".(int)$random_number_products;
        else
        $sql += " ORDER BY ".(!empty($order_by_prefix) ? $order_by_prefix."." : "")."`".bqSQL($order_by)."` ".pSQL($order_way)."
        LIMIT ".(((int)$p - 1) * (int)$n).",".(int)$n;

        dataBaseObject->setQuery(query);
        $result = dataBaseObject->loadObjectList();
        if ($order_by == "order_price")
            JeproLabTools.orderByPrice($result, $order_way);

        if (!$result)
            return array();

        /* Modify SQL result * /
        return JeproLabAnalyzeModel.getProductsProperties($lang_id, $result);
    }


    public static function getSimpleCategories($lang_id){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT category." . dataBaseObject->quoteName("category_id") . ", category_lang." . dataBaseObject->quoteName("name") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " AS category LEFT JOIN " .dataBaseObject->quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." . dataBaseObject->quoteName("category_id") . " = category_lang." . dataBaseObject->quoteName("category_id") . JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") . ") ";
        query += JeproLabLaboratoryModel.addSqlAssociation("category") . " WHERE category_lang." . dataBaseObject->quoteName("lang_id") . " = " .(int)$lang_id . " AND category." . dataBaseObject->quoteName("category_id") . " != " . JeproLabSettingModelSetting.getValue("root_category");
        query += "	GROUP BY category.category_id ORDER BY category." . dataBaseObject->quoteName("category_id") . ", category_lab." . dataBaseObject->quoteName("position");
        dataBaseObject->setQuery(query);
        return dataBaseObject->loadObjectList();
    }

    public int getLaboratoryId(){
        return this.laboratory_id;
    }

    /**
     * Recursively add specified category children to $to_delete array
     *
     * @param array &$to_delete Array reference where categories ID will be saved
     * @param integer $category_id Parent category ID
     * /
    protected function recursiveDelete(&$to_delete, $category_id){
        if (!is_array($to_delete) || !$category_id)
            die(Tools.displayError());

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " . dataBaseObject->quoteName("category_id") . " FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " WHERE " . dataBaseObject->quoteName("parent_id") . " = " .(int)$category_id;
        dataBaseObject->setQuery(query);
        $result = dataBaseObject->loadObjectList();
        foreach ($result as $row){
            $to_delete[] = (int)$row->category_id;
            this.recursiveDelete($to_delete, (int)$row->category_id);
        }
    }

    public function deleteLite(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        // @hook actionObject*DeleteBefore
        Hook.exec("actionObjectDeleteBefore", array("object" => $this));
        Hook.exec("actionObject".get_class($this)."DeleteBefore", array("object" => $this));

        this.clearCache();
        $result = true;
        // Remove association to multilab table
        if (JeproLabLaboratoryModel.isTableAssociated("category")){
            $lab_list_ids = JeproLabLaboratoryModel.getContextListLabIds();
            if (count(this.lab_list_ids)) {
                $lab_list_ids = this.lab_list_ids;
            }

            query = "DELETE FROM " . dataBaseObject->quoteName("#__jeprolab_category_lab") . " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id . " AND " . dataBaseObject->quoteName("lab_id") . " IN(" . implode(", ", $lab_list_ids) . ")";
            dataBaseObject->setQuery(query);
            $result &= dataBaseObject->query();
        }

        // Database deletion
        $has_multilab_entries = this.hasMultilabEntries();
        if ($result && !$has_multilab_entries) {
            query = "DELETE FROM " . dataBaseObject->quoteName("#__jeprolab_category") . " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id;
            dataBaseObject->setQuery(query);
            $result &= dataBaseObject->query();
        }

        if (!$result)
            return false;

        // Database deletion for multilingual fields related to the object
        if (!empty(this.def["multilang"]) && !$has_multilab_entries) {
            query = "DELETE FROM " . dataBaseObject->quoteName("#__jeprolab_category_lang") . " WHERE " . dataBaseObject->quoteName("category_id") . " = " . (int)this.category_id;
            dataBaseObject->setQuery(query);
            $result &= dataBaseObject->query();
        }
        // @hook actionObject*DeleteAfter
        Hook.exec("actionObjectDeleteAfter", array("object" => $this));
        Hook.exec("actionObject".get_class($this)."DeleteAfter", array("object" => $this));

        return $result;
    }

    public boolean delete(){
        if (this.category_id == 0 || this.category_id == 1 || this.is_root_category)
            return false;

        this.clearCache();

        List<JeproLabCategoryModel> allCategories = this.getAllChildren();
        $allCategories[] = $this;
        for(JeproLabCategoryModel category : allCategories){
            category.deleteLite();
            if (!this.hasMultiLabEntries()){
                category.deleteImage();
                category.cleanGroups();
                category.cleanAssoProducts();
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
        if (!this.hasMultiLabEntries() && (!isset(this.doNotRegenerateNTree) || !this.doNotRegenerateNTree))
            JeproLabCategoryModel.regenerateEntireNtree();

        Hook.exec("actionCategoryDelete", array("category" => $this));

        return true;
    }

    /**
     * Delete several categories from database
     *
     * return boolean Deletion result
     * @param $categories
     * @return bool|int
     * /
    public function deleteSelection($categories){
        $return = 1;
        foreach ($categories as $category_id){
            $category = new JeproLabCategoryModelCategory($category_id);
            if ($category->isRootCategoryForALab())
                return false;
            else
                $return &= $category->delete();
        }
        return $return;
    }

    /**
     * @see ObjectModel.toggleStatus()
     * /
    public function toggleStatus(){
        $result = parent.toggleStatus();
        Hook.exec("actionCategoryUpdate");
        return $result;
    }

    /**
     * Recursive scan of subcategories
     *
     * @param integer $max_depth Maximum depth of the tree (i.e. 2 => 3 levels depth)
     * @param integer $current_depth specify the current depth in the tree (don"t use it, only for recursive!)
     * @param integer $lang_id Specify the id of the language used
     * @param array $excluded_ids_array specify a list of ids to exclude of results
     *
     * @return array Subcategories lite tree
     * /
    public function recurseLiteCategoryTree($max_depth = 3, $current_depth = 0, $lang_id = null, $excluded_ids_array = null){
        $lang_id = is_null($lang_id) ? JeproLabContext.getContext()->language->lang_id : (int)$lang_id;

        $children = array();
        $subCategories = this.getSubCategories($lang_id, true);
        if (($max_depth == 0 || $current_depth < $max_depth) && $subCategories && count($subCategories)) {
            foreach ($subCategories as &$subCategory) {
                if (!$subCategory->category_id)
                    break;
                else if (!is_array($excluded_ids_array) || !in_array($subCategory->category_id, $excluded_ids_array)) {
                    $category = new JeproLabCategoryModelCategory($subCategory->category_id, $lang_id);
                    $children[] = $category->recurseLiteCategoryTree($max_depth, $current_depth + 1, $lang_id, $excluded_ids_array);
                }
            }
        }

        if (is_array(this.description))
            foreach (this.description as $language_id => $description)
        this.description[$language_id] = JeproLabTools.getDescriptionClean($description);
        else
        this.description = JeproLabTools.getDescriptionClean(this.description);

        return array(
                "category_id" => (int)this.category_id,
        "link" => JRoute._("index.php?option=com_jeprolab&view=category&category_id=" . this.category_id . "&link_rewrite=" . this.link_rewrite . "&" . JeproLabTools.getCategoryToken() . "=1"),
        "name" => this.name,
                "desc"=> this.description,
                "children" => $children
        );
    }

    public static function recurseCategory($categories, $current, $category_id = 1, $selected_id = 1){
        echo "<option value="".$category_id .""".(($selected_id == $category_id) ? " selected="selected"" : "").">".
                str_repeat("&nbsp;", $current["infos"]["depth_level"] * 5).stripslashes($current["infos"]["name"])."</option>";
        if (isset($categories[$category_id]))
            foreach (array_keys($categories[$category_id]) as $key)
        JeproLabCategoryModelCategory.recurseCategory($categories, $categories[$category_id][$key], $key, $selected_id);
    }*/
}