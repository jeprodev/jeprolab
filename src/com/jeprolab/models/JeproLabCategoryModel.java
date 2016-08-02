package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCategoryModel extends JeproLabModel{
    public int category_id;

    public int language_id;

    public int laboratory_id;

    public Map<String, String> name;

    public boolean published = true;
    public boolean disabled = false;

    public int position;

    public Map<String, String> description;

    public int parent_id;

    public int depth_level;

    public int n_left;

    public int n_right;

    public int selected_children;

    public List<Integer> laboratory_list_ids = new ArrayList<>();
    public List<JeproLabCategoryModel> children = new ArrayList<>();

    private int totalAnalyzes;

    public Map<String, String> link_rewrite;

    public Map<String, String> meta_title;
    public Map<String, String> meta_keywords;
    public Map<String, String> meta_description;

    public Date date_add;
    public Date date_upd;

    public boolean is_root_category;
    public boolean getLabFromContext = false;

    public int default_laboratory_id;

    public int groupBox[];

    private static Pagination pagination;

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
                ResultSet categoryData = dataBaseObject.loadObjectList();
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
                            ResultSet categoryLangData = dataBaseObject.loadObjectList();
                            while(categoryLangData.next()){
                                int languageId = categoryLangData.getInt("lang_id");
                                String categoryName = categoryLangData.getString("name");
                                String categoryDescription = categoryLangData.getString("description");
                                String linkRewrite = categoryLangData.getString("link_rewrite");
                                String metaTitle = categoryLangData.getString("meta_title");
                                String metaKeywords = categoryLangData.getString("meta_keywords");
                                String metaDescription = categoryLangData.getString("meta_description");
                                for (Object o : languages.entrySet()) {
                                    Map.Entry lang = (Map.Entry) o;
                                    JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                                    if (languageId == language.language_id) {
                                        this.name.put("lang_" + languageId, categoryName);
                                        this.description.put("lang_" + languageId, categoryDescription);
                                        this.link_rewrite.put("lang_" + languageId, linkRewrite);
                                        this.meta_title.put("lang_" + languageId, metaTitle);
                                        this.meta_keywords.put("lang_" + languageId, metaKeywords);
                                        this.meta_description.put("lang_" + languageId, metaDescription);
                                    }
                                }
                            }
                        }else {
                            /** set data for a given  language **/
                            this.name.put("lang_" + langId, categoryData.getString("name"));
                            this.description.put("lang_" + langId, categoryData.getString("description"));
                            this.link_rewrite.put("lang_" + langId, categoryData.getString("link_rewrite"));
                            this.meta_title.put("lang_" + langId, categoryData.getString("meta_title"));
                            this.meta_keywords.put("lang_" + langId, categoryData.getString("meta_keywords"));
                            this.meta_description.put("lang_" + langId, categoryData.getString("meta_description"));
                        }

                    }
                    JeproLabCache.getInstance().store(cacheKey, this);
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                JeproLabCategoryModel category = (JeproLabCategoryModel) JeproLabCache.getInstance().retrieve(cacheKey);
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

    public boolean isMultiLab(){
        return JeproLabLaboratoryModel.isTableAssociated("category") || JeproLabCategoryModel.multiLangLab;
    }

    public boolean isLangMultiLab(){
        return JeproLabCategoryModel.multiLang && JeproLabCategoryModel.multiLangLab;
    }

    public static Pagination getPagination(){
        return pagination;
    }

    /**
     * check if a given laboratory is associated with this category
     * @param labId laboratory id
     * @return boolean value
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

    public static List<JeproLabCategoryModel> getCategoriesWithoutParent(){
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
            ResultSet result = staticDataBaseObject.loadObjectList();
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
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JeproLabCache.getInstance().store(cacheKey, categories);
        }
        return (List)JeproLabCache.getInstance().retrieve(cacheKey);
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
            ResultSet categoryData = staticDataBaseObject.loadObjectList();
            int categoryId = 0;
            try{
                while(categoryData.next()){
                    categoryId = categoryData.getInt("category_id");
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JeproLabCache.getInstance().store(cacheKey, new JeproLabCategoryModel(categoryId, langId));
        }
        return (JeproLabCategoryModel)JeproLabCache.getInstance().retrieve(cacheKey);
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
            ResultSet result = dataBaseObject.loadObjectList();
            try{
                while(result.next()){
                    isAssociated = result.getInt("lab_id") > 0;
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            JeproLabCache.getInstance().store(cacheKey, isAssociated);
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
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
        if (JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && JeproLabSettingModel.getIntValue("multi_lab_feature_active") > 0 && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() != 1) {
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
            ResultSet result = dataBaseObject.loadObjectList();

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
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
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

    public static List<JeproLabCategoryModel> getCategories(){
        if(staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        JeproLabContext context = JeproLabContext.getContext();
        int limit = context.list_limit;
        int limitStart = context.list_limit_start;
        int langId = context.language.language_id;
        int labId = context.laboratory.laboratory_id;
        String orderBy = JeproLab.request.getValue("order_by", "date_add");
        String orderWay = JeproLab.request.getValue("order_way", "ASC");

        boolean useLimit = true;
        if (limit <= 0) {
            useLimit = false;
        }

        int parentId = 0;
        int categoryId = JeproLab.request.getIntValue("category_id", context.category_id);
        int countCategoriesWithoutParent = JeproLabCategoryModel.getCategoriesWithoutParent().size();
        JeproLabCategoryModel topCategory = JeproLabCategoryModel.getTopCategory();

        if(categoryId > 0){
            JeproLabCategoryModel category = new JeproLabCategoryModel(categoryId);
            parentId = category.category_id;
        }else if(!JeproLabLaboratoryModel.isFeaturePublished() && countCategoriesWithoutParent > 1){
            parentId = topCategory.category_id;
        }else if(JeproLabLaboratoryModel.isFeaturePublished() && countCategoriesWithoutParent == 1){
            parentId = JeproLabSettingModel.getIntValue("root_category");
        }else if(JeproLabLaboratoryModel.isFeaturePublished() && countCategoriesWithoutParent > 1 && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT){
            if((JeproLabSettingModel.getIntValue("multi_lab_feature_active") > 0) && (JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() == 1)){
                parentId = context.laboratory.category_id;
            }else{
                parentId = topCategory.category_id;
            }
        }

        List<JeproLabCategoryModel> categories = new ArrayList<>();
        ResultSet categoryResult;

        String query = "SELECT SQL_CALC_FOUND_ROWS " + "category." + staticDataBaseObject.quoteName("category_id")  + ", category_lang.";
        query += staticDataBaseObject.quoteName("name") + ", category_lang." + staticDataBaseObject.quoteName("description") + ", category.";
        query += staticDataBaseObject.quoteName("position") +" AS category_position, " + staticDataBaseObject.quoteName("published");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN ";
        query += staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category_lang.";
        query += staticDataBaseObject.quoteName("category_id") + " = category." + staticDataBaseObject.quoteName("category_id");
        query += " AND category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId;
        if (context.laboratory.laboratory_id > 0){
            if (!JeproLabLaboratoryModel.isFeaturePublished()){
                query += " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = " + JeproLabSettingModel.getIntValue("default_lab");
            }else if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                query +=  " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = " + context.laboratory.laboratory_id;
            }else{
                query +=  " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = category.default_lab_id";
            }
        }
        query += ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " AS lab ON category.";
        query += staticDataBaseObject.quoteName("category_id") + " = lab." + staticDataBaseObject.quoteName("category_id");
        query += " WHERE category." + staticDataBaseObject.quoteName("parent_id") +" = " + parentId + JeproLabLaboratoryModel.addSqlRestriction("1", "category");
        query += " ORDER BY " + ((orderBy.replace("`", "").equals("category_id")) ? "category." : "") + " category." + orderBy + " " + orderWay;

        do{
            int total = 0;

            staticDataBaseObject.setQuery(query);
            categoryResult = staticDataBaseObject.loadObjectList();

            if(categoryResult != null) {
                try {
                    while (categoryResult.next()) {
                        total = total + 1;
                    }
                    staticDataBaseObject.setQuery(query + (useLimit ? " LIMIT " + limitStart + ", " + limit : ""));
                    categoryResult = staticDataBaseObject.loadObjectList();
                    JeproLabCategoryModel category;
                    while (categoryResult.next()) {
                        category = new JeproLabCategoryModel();
                        category.category_id = categoryResult.getInt("category_id");
                        category.published = categoryResult.getInt("published") > 0;
                        category.name.put("lang_" + langId, categoryResult.getString("name"));
                        category.description.put("lang_" + langId, categoryResult.getString("description"));
                        categories.add(category);
                    }
                }catch (SQLException ignored) {
                    ignored.printStackTrace();
                } finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (useLimit){
                limitStart = limitStart - limit;
                if (limitStart < 0){ break; }
            }else{ break; }
        }while (categories.isEmpty());
        return categories;
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
            ResultSet result = staticDataBaseObject.loadObjectList();

            JeproLabCache.getInstance().store(cacheKey, result);
        }
        return (ResultSet)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public boolean toggleStatus(){
        return toggleStatus(0, 0);
    }

    public boolean toggleStatus(int langId) {
        return toggleStatus(langId, 0);
    }

    public boolean toggleStatus(int langId, int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " SET " + dataBaseObject.quoteName("published");
        query += " = " + (this.published ? 0 : 1) + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;


        dataBaseObject.setQuery(query);
        this.published = !this.published;
        /** load category from data base if id provided **/
        String cacheKey = "jeprolab_model_category_" + this.category_id + "_" + langId + "_" + labId;
        if(JeproLabCache.getInstance().isStored(cacheKey)){
            JeproLabCategoryModel cat = (JeproLabCategoryModel)JeproLabCache.getInstance().retrieve(cacheKey);
            cat.published = this.published;
            JeproLabCache.getInstance().update(cacheKey, cat);
        }
        return dataBaseObject.query(false);
    }

    public static List<JeproLabCategoryModel> getNestedCategories(){
        return getNestedCategories(0, 0, false);
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId){
        return getNestedCategories(rootCategoryId, 0, false);
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId){
        return getNestedCategories(rootCategoryId, langId, false);
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId, boolean published){
        return getNestedCategories(rootCategoryId, langId, published, null);
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[]){
        return getNestedCategories(rootCategoryId, langId, published, groups, true);
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[], boolean useLabRestriction){
        return getNestedCategories(rootCategoryId, langId, published, groups, useLabRestriction, "");
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[], boolean useLabRestriction, String sqlFilter){
        return getNestedCategories(rootCategoryId, langId, published, groups, useLabRestriction, sqlFilter, "");
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId, boolean published, int groups[], boolean useLabRestriction, String sqlFilter, String sqlSort){
        return getNestedCategories(rootCategoryId, langId, published, groups, useLabRestriction, sqlFilter, sqlSort, "");
    }

    public static List<JeproLabCategoryModel> getNestedCategories(int rootCategoryId, int langId, boolean categoryPublished, int groups[], boolean useLabRestriction, String sqlFilter, String sqlSort, String sqlLimit){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(rootCategoryId <= 0) {
            rootCategoryId = JeproLabSettingModel.getIntValue("root_category");
            //JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_THE_ROOT_CATEGORY_ID_MUST_BE_GREATER_THAN_0_LABEL"));
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
            ResultSet resultSet = staticDataBaseObject.loadObjectList();

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

                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
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
            ResultSet groupSet = dataBaseObject.loadObjectList();
            List<Integer> groupList = new ArrayList<>();
            if(groupSet != null){
                try{
                    while(groupSet.next()){
                        groupList.add(groupSet.getInt("group_id"));
                    }
                }catch(SQLException ignored){

                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            JeproLabCache.getInstance().store(cacheKey, groupList);
        }
        return (List<Integer>)JeproLabCache.getInstance().retrieve(cacheKey);
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
            ResultSet resultSet = staticDataBaseObject.loadObjectList();
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
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Get the depth level for the category
     * @return int Depth level
     */
    public int calculateDepthLevel(){
        /* Root category */
        if (this.parent_id <= 1) {
            return 0;
        }
        JeproLabCategoryModel parentCategory = new JeproLabCategoryModel(this.parent_id);
        if (parentCategory.category_id <= 0){
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_PARENT_CATEGORY_DOES_NOT_EXISTS_MESSAGE"));
        }
        return parentCategory.depth_level + 1;
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
        ResultSet categoriesSet = staticDataBaseObject.loadObjectList();

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
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return categories;
    }

}
