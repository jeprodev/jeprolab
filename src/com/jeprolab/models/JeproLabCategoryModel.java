package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.io.File;
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

        //this.image_path = (file_exists(JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY +  this.category_id + ".jpg")) ? this.category_id : 0;
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

    /**
     * Check if current category is a child of shop root category
     *
     * @since 1.5.0
     * @param lab
     * @return bool
     */
    public boolean inLaboratory(JeproLabLaboratoryModel lab){
        if (lab == null) {
            lab = JeproLabContext.getContext().laboratory;
        }
        Map<String, Integer> interval = JeproLabCategoryModel.getInterval(lab.getCategoryId());

        if (interval == null || interval.isEmpty()){
            return false;
        }
        return (this.n_left >= interval.get("n_left") && this.n_right <= interval.get("n_right"));
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

    public boolean delete(){
        if (this.category_id == 0 || this.category_id == JeproLabSettingModel.getIntValue("root_category")) {
            return false;
        }

        this.clearCache("category", this.category_id);

        List<JeproLabCategoryModel> deletedChildren = this.getAllChildren();
        List<JeproLabCategoryModel> allCategory = this.getAllChildren();
        allCategory.add(this);

        for(JeproLabCategoryModel category : allCategory) {
            /** @var Category $cat */
            category.deleteLite();
            if (!this.hasMultiLabEntries()) {
                category.deleteImage();
                category.cleanGroups();
                category.cleanAssociatedAnalyzes();
                // Delete associated restrictions on cart rules
                List<Integer> ids = new ArrayList<>();
                ids.add(category.category_id);
                JeproLabCartModel.JeproLabCartRuleModel.cleanAnalyzeRuleIntegrity("categories", ids);
                JeproLabCategoryModel.cleanPositions(category.parent_id);
                /* Delete Categories in GroupReduction */
                if (JeproLabGroupModel.JeproLabGroupReductionModel.getGroupsReductionByCategoryId(category.category_id) != null){
                    JeproLabGroupModel.JeproLabGroupReductionModel.deleteCategory(category.category_id);
                }
            }
        }

        /* Rebuild the nested tree */
        if (!this.hasMultiLabEntries() && !this.do_not_regenerate_nested_tree){
            JeproLabCategoryModel.regenerateEntireNestedTree();
        }

        //Hook::exec('actionCategoryDelete', array('category' => $this, 'deleted_children' => $deleted_children));

        return true;
    }

    public boolean deleteLite(){
        // Directly call the parent of delete, in order to avoid recursion
        // @hook actionObject*DeleteBefore
        //Hook::exec('actionObjectDeleteBefore', array('object' => $this));
        //Hook::exec('actionObject'.get_class($this).'DeleteBefore', array('object' => $this));

        this.clearCache("category", this.category_id);
        boolean result = true;
        // Remove association to multishop table
        if (JeproLabLaboratoryModel.isTableAssociated("category")){
            List<Integer> labIdList = JeproLabLaboratoryModel.getContextListLaboratoryIds();
            if(this.laboratory_list_ids.size() > 0){
                labIdList = this.laboratory_list_ids;
            }

            String query = "";
            for(Integer labId : labIdList){
                query += "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " ;
                query += dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND ";
                query += dataBaseObject.quoteName("lab_id") + " = " + labId;

                dataBaseObject.setQuery(query);
                result &= dataBaseObject.query(false);
            }
        }

        // Database deletion
        boolean hasMultiLabEntries = this.hasMultiLabEntries();
        if (result && !hasMultiLabEntries) {
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " WHERE " + dataBaseObject.quoteName("category_id");
            query += " = " + this.category_id;

            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);
        }

        if (!result) {
            return false;
        }

        // Database deletion for multilingual fields related to the object
        if (JeproLabCategoryModel.multiLang && !hasMultiLabEntries) {
            String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " WHERE " + dataBaseObject.quoteName("category_id");
            query += " = " + this.category_id;

            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);
        }

        // @hook actionObject*DeleteAfter
        //Hook::exec('actionObjectDeleteAfter', array('object' => $this));
        //Hook::exec('actionObject'.get_class($this).'DeleteAfter', array('object' => $this));

        return result;
    }

    public boolean deleteImage(){
        return deleteImage(false);
    }

    /**
     * Delete images associated with the object
     *
     * @param forceDelete
     *
     * @return bool
     */
    public boolean deleteImage(boolean forceDelete){
        if (this.category_id <= 0) {
            return false;
        }

        if (forceDelete || !this.hasMultiLabEntries()) {
            /* Deleting object images and thumbnails (cache) */
            String imagePath = this.image_dir + this.category_id + "." + this.image_format;
            File imageFile = new File(imagePath);
            if(!this.image_dir.equals("")) {
                if(imageFile.exists() && !imageFile.delete()) {
                    return false;
                }
            }
            imagePath = JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + "category" + this.category_id + "." + this.image_format;
            imageFile = new File(imagePath);
            if (imageFile.exists() && !imageFile.delete()){
                return false;
            }
            imagePath = JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + "category_mini" + this.category_id + "." + this.category_id;
            imageFile = new File(imagePath);

            if (imageFile.exists() && !imageFile.delete()){
                return false;
            }

            List<JeproLabImageModel.JeproLabImageTypeModel> types = JeproLabImageModel.JeproLabImageTypeModel.getImagesTypes();
            for(JeproLabImageModel.JeproLabImageTypeModel imageType : types) {
                imagePath = this.image_dir + this.category_id + "_" + imageType.name + "." + this.image_format;
                imageFile = new File(imagePath);
                if (imageFile.exists() && !imageFile.delete()){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean cleanPositions(){
        return cleanPositions(0);
    }

    /**
     * cleanPositions keep order of category in $id_category_parent,
     * but remove duplicate position. Should not be used if positions
     * are clean at the beginning !
     *
     * @param parentCategoryId
     * @return bool true if succeed
     */
    public static boolean cleanPositions(int parentCategoryId){
        if (parentCategoryId <= 0) {
            return false;
        }

        boolean result = true;
        //$result = Db::getInstance()->executeS(
        String query = "SELECT category."  + staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
        query += " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category." + staticDataBaseObject.quoteName("parent_id");
        query += " = " + parentCategoryId + " ORDER BY category_lab." + staticDataBaseObject.quoteName("position");

        staticDataBaseObject.setQuery(query);
        ResultSet categorySet = staticDataBaseObject.loadObjectList();

        if(categorySet != null){
            try{
                int index = 0;
                while(categorySet.next()){
                    query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
                    query += JeproLabLaboratoryModel.addSqlAssociation("category") + " SET category." + staticDataBaseObject.quoteName("position");
                    query += " = " + (index) + ", category_lab." + staticDataBaseObject.quoteName("position") + " = " + index + ", category.";
                    query += staticDataBaseObject.quoteName("date_upd") + " = " + JeproLabTools.date() + " WHERE category." ;
                    query += staticDataBaseObject.quoteName("parent_id") + " = " + parentCategoryId + " AND category.";
                    query += staticDataBaseObject.quoteName("category_id") + " = " + categorySet.getInt("category_id");

                    staticDataBaseObject.setQuery(query);
                    result &= staticDataBaseObject.query(false);
                    index += 1;
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }

        return result;
    }

    public boolean cleanAssociatedAnalyzes(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_analyze") + " WHERE ";
        query += dataBaseObject.quoteName("category_id") + " = " + this.category_id;

        dataBaseObject.setQuery(query);
        return dataBaseObject.query(false);
    }


    public List<JeproLabCategoryModel> getAllChildren(){
        return getAllChildren(0);
    }

    /**
     * Return an array of all children of the current category
     *
     * @param langId
     * @return Collection Collection of Category
     */
    public List<JeproLabCategoryModel> getAllChildren(int langId){
        if (langId <= 0){
            langId = JeproLabContext.getContext().language.language_id;
        }

        /*$categories = new PrestaShopCollection('Category', $id_lang);
        $categories->where('nleft', '>', $this->nleft);
        $categories->where('nright', '<', $this->nright); */
        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category_lang." + dataBaseObject.quoteName("category_id") + " = category." + dataBaseObject.quoteName("category_id");
        query += " AND category_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE " + dataBaseObject.quoteName("n_left") + " = ";
        query += this.n_left + " AND " + dataBaseObject.quoteName("n_right") + " = " + this.n_right;

        dataBaseObject.setQuery(query);
        List<JeproLabCategoryModel> categories = new ArrayList<>();
        ResultSet categorySet = dataBaseObject.loadObjectList();

        if(categorySet != null){
            try{
                JeproLabCategoryModel category;
                while(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    categories.add(category);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return categories;
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
            if((JeproLabSettingModel.getIntValue("multi_lab_feature_active") > 0) && (JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() == 1)){
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
        String join = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " AS category_lab ON (category.";
        join += staticDataBaseObject.quoteName("category_id") + " = category_lab." + staticDataBaseObject.quoteName("category_id") + " AND ";
        if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
            join += " category_lab.lab_id = " + context.laboratory.laboratory_id + ") ";
        }else{
            join += " category_lab.lab_id = category.default_lab_id)" ;
        }

        // we add restriction for lab
        String where = "";
        if(JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && JeproLabLaboratoryModel.isFeaturePublished()){
            where = " AND category_lab." + staticDataBaseObject.quoteName("lab_id") + " = " + JeproLabContext.getContext().laboratory.laboratory_id;
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
            joinLab = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " AS lab ON category.";
            joinLab += staticDataBaseObject.quoteName("category_id") + " = lab." + staticDataBaseObject.quoteName("category_id");
            whereLab = JeproLabLaboratoryModel.addSqlRestriction("1", "category");
        }

        if (context.controller.multi_laboratory_context && JeproLabLaboratoryModel.isTableAssociated("category")){
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
        String langJoin = " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (";
        langJoin += "category_lang." + staticDataBaseObject.quoteName("category_id") + " = category." + staticDataBaseObject.quoteName("category_id");
        langJoin += " AND category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId;
        if (context.laboratory.laboratory_id > 0){
            if (!JeproLabLaboratoryModel.isFeaturePublished()){
                langJoin += " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = 1";
            }else if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                langJoin +=  " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = " + context.laboratory.laboratory_id;
            }else{
                langJoin +=  " AND category_lang." + staticDataBaseObject.quoteName("lab_id") + " = category.default_lab_id";
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
                query += "category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." + staticDataBaseObject.quoteName("name") + ", category_lang." + staticDataBaseObject.quoteName("description");
                query += ", category." + staticDataBaseObject.quoteName("position") +" AS category_position, " + staticDataBaseObject.quoteName("published");
            }else{
                query += (langId > 0? " category_lang.*," : "") + " category.*";
            }
            //select
            query += (!select.equals("") ? select : "") + selectLab + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category " + langJoin + (!join.equals("") ? join + " " : "") ;
            query += joinLab + " WHERE 1 " + (!where.equals("") ? where + " " : "") +  "AND category.";
            query += staticDataBaseObject.quoteName("parent_id") +" = " + parentId + whereLab  + havingClause + " ORDER BY " + ((orderBy.replace("`", "").equals("category_id")) ? "category." : "") + " category.";
            query += orderBy + " " + orderWay + (!tmpTableFilter.equals("") ? ") tmpTable WHERE 1" + tmpTableFilter : "");

            staticDataBaseObject.setQuery(query);

            try{
                categoryResult = staticDataBaseObject.loadObjectList();
                while (categoryResult.next()) {
                    total = total + 1;
                }
                query += (useLimit ? " LIMIT " + limitStart + ", " + limit : "" );
                staticDataBaseObject.setQuery(query);
                categoryResult = staticDataBaseObject.loadObjectList();
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
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
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

        pagination = new Pagination(); //$total, $limitstart, $limit);*/
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
            if(this.depth_level <= 0) {
                this.depth_level = this.calculateDepthLevel();
            }

            int rootCategoryId = JeproLabSettingModel.getIntValue("root_category");
            if(this.is_root_category && rootCategoryId > 0){
                this.parent_id = rootCategoryId;
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
            query += ", " + dataBaseObject.quoteName("is_root") + ") VALUES (" + parentId + ", " + this.depth_level + ", " + defaultLabId + ", ";
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

    public boolean addGroupsIfNoExist(int groupId){
        List<Integer> groups = this.getGroups();
        if (!groups.contains(groupId)){
            int[] g = {groupId};
            return this.addGroups(g);
        }
        return false;
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

    /**
     * Add some categories to a shop
     * @param categories
     * @return bool
     */
    public static boolean addToLaboratory(List<Integer> categories, int labId){
        if (!categories.isEmpty()) {
            return false;
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_category") +  " (" + staticDataBaseObject.quoteName("category_id");
        query += ", " + staticDataBaseObject.quoteName("lab_id") + ") VALUES ";
        boolean result = true;

        JeproLabCategoryModel category;
        for(Integer categoryId : categories) {
            staticDataBaseObject.setQuery(query + " (" + categoryId + ", " + labId + ")");
            result &= staticDataBaseObject.query(false);
            category = new JeproLabCategoryModel(categoryId);
            category.addPosition(JeproLabCategoryModel.getLastPosition(category.parent_id, labId), labId);
        }
        return result;
    }

    /**
     * Delete category from shop labId
     * @param labId
     * @return bool
     */
    public boolean deleteFromLaboratory(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE " + dataBaseObject.quoteName("lab_id");
        query += " = " + labId + " AND " + dataBaseObject.quoteName("category_id") + " = " + this.category_id;

        dataBaseObject.setQuery(query);
        return  dataBaseObject.query(false);
    }

    /**
     * Delete every categories
     * @return bool
     */
    public static boolean deleteCategoriesFromLaboratory(int labId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE ";
        query += staticDataBaseObject.quoteName("lab_id") + " = " + labId;

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    public boolean existsInLaboratory(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_lab");
        query += " WHERE " + dataBaseObject.quoteName("category_id") + " = " + this.category_id + " AND " + dataBaseObject.quoteName("lab_id");
        query += " = " + labId;

        dataBaseObject.setQuery(query);
        return dataBaseObject.loadValue("category_id") > 0;
    }

    public static List<Integer> getLaboratoryIdsByCategoryId(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("lab_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category_lab") + " WHERE ";
        query += staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

        staticDataBaseObject.setQuery(query);
        ResultSet labSet = staticDataBaseObject.loadObjectList();
        List<Integer> labIds = new ArrayList<>();

        if(labSet != null){
            try{
                while (labSet.next()){
                    labIds.add(labSet.getInt("lab_id"));
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return labIds;
    }

    /**
     * Update categories for a shop
     *
     * @param categories Categories list to associate a shop
     * @param labId Categories list to associate a shop
     * @return array Update/insertion result
     */
    public static boolean updateFromLaboratory(List<Integer> categories, int labId){
        JeproLabLaboratoryModel lab = new JeproLabLaboratoryModel(labId);
        // if array is empty or if the default category is not selected, return false
        if(categories.isEmpty() || !categories.contains(lab.category_id)) {
            return false;
        }

        // delete categories for this shop
        JeproLabCategoryModel.deleteCategoriesFromLaboratory(labId);

        // and add $categories to this shop
        return JeproLabCategoryModel.addToLaboratory(categories, labId);
    }

    /**
     * Add association between shop and categories
     * @param labId
     * @return bool
     */
    public boolean addLaboratory(int labId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_category_lab") + "(" ;
        query += dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("lab_id") + ") VALUES (";
        boolean result = true;
        if (labId <= 0) {
            for(JeproLabLaboratoryModel lab : JeproLabLaboratoryModel.getLaboratories(false)) {
                if (!this.existsInLaboratory(lab.laboratory_id)) {
                    dataBaseObject.setQuery(query + this.category_id + ", " + lab.laboratory_id + ")");
                    result &= dataBaseObject.query(false);
                }
            }
        } else if (!this.existsInLaboratory(labId)) {
            dataBaseObject.setQuery(query + this.category_id + ", " + labId + ")");
            result &= dataBaseObject.query(false);
        }

        return result;
    }

    public static List<JeproLabCategoryModel> getRootCategories(){
        return getRootCategories(0, true);
    }

    public static List<JeproLabCategoryModel> getRootCategories(int langId){
        return getRootCategories(langId, true);
    }

    public static List<JeproLabCategoryModel> getRootCategories(int langId, boolean published){
        if(langId <= 0) {
            langId = JeproLabContext.getContext().language.language_id;
        }
        String query = "SELECT DISTINCT(category." + staticDataBaseObject.quoteName("category_id") + "), category_lang." + staticDataBaseObject.quoteName("name");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category_lang." + staticDataBaseObject.quoteName("category_id") + " = category." + staticDataBaseObject.quoteName("category_id");
        query += " AND category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE "  + staticDataBaseObject.quoteName("is_root_category");
        query += " = 1 " + (published ? " AND " + staticDataBaseObject.quoteName("published") + " = 1" : "");

        staticDataBaseObject.setQuery(query);
        ResultSet categorySet = staticDataBaseObject.loadObjectList();
        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(categorySet != null){
            try{
                JeproLabCategoryModel category;
                while(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    category.name.put("lang_" + categorySet.getInt("lang_id"), categorySet.getString("name"));
                    categories.add(category);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return categories;
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

    public static List<JeproLabCategoryModel> getCategoryInformation(List categoryIds){
        return getCategoryInformation(categoryIds, 0);
    }

    /**
     *
     * @param categoryIds
     * @param langId
     * @return Array
     */
    public static List<JeproLabCategoryModel> getCategoryInformation(List<Integer> categoryIds, int langId){
        if (langId <= 0) {
            langId = JeproLabContext.getContext().language.language_id;
        }

        if (categoryIds.isEmpty()) {
            return new ArrayList<>();
        }
        String categoryList = "";
        for(Integer id : categoryIds){
            categoryList += id + ", ";
        }
        categoryList = (categoryList.endsWith(", ") ? categoryList.substring(0, categoryList.length() - 3) : categoryList);

        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." + staticDataBaseObject.quoteName("name");
        query += ", category_lang." + staticDataBaseObject.quoteName("link_rewrite") + ", category_lang." + staticDataBaseObject.quoteName("lang_id");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + ") " + JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE ";
        query += " category_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category." + staticDataBaseObject.quoteName("category_id");
        query += " IN (" + categoryList + ") ";

        staticDataBaseObject.setQuery(query);
        ResultSet categorySet = staticDataBaseObject.loadObjectList();

        List<JeproLabCategoryModel> categories = new ArrayList<>();
        if(categorySet != null){
            try{
                JeproLabCategoryModel category;
                int languageId;
                while(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    languageId = categorySet.getInt("lang_id");
                    category.name.put("lang_" + languageId, categorySet.getString("name"));
                    category.link_rewrite.put("lang_" + languageId, categorySet.getString("link_rewrite"));
                    categories.add(category);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return categories;
    }

    /**
     * Search for another category with the same parent and the same position
     *
     * @return array first category found
     */
    public int getDuplicatePosition(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category");
        query += " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category." + dataBaseObject.quoteName("parent_id");
        query += " = " + this.parent_id + " AND category_lab." + dataBaseObject.quoteName("position") + " = " + this.position + " AND category.";
        query += dataBaseObject.quoteName("category_id") + " != " + this.category_id;

        dataBaseObject.setQuery(query);
        return (int)dataBaseObject.loadValue("category_id");
    }

    public static boolean inLaboratoryStatic(int categoryId){
        return inLaboratoryStatic(categoryId, null);
    }

    public static boolean inLaboratoryStatic(int categoryId, JeproLabLaboratoryModel lab){
        if (lab == null) {
            lab = JeproLabContext.getContext().laboratory;
        }

        Map<String, Integer> interval = JeproLabCategoryModel.getInterval(lab.getCategoryId());
        if (interval == null){
            return false;
        }

        String query = "SELECT n_left, n_right FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " WHERE category_id = " + categoryId;

        staticDataBaseObject.setQuery(query);
        ResultSet categorySet = staticDataBaseObject.loadObjectList();
        if(categorySet != null){
            try{
                if(categorySet.next()){
                    return (categorySet.getInt("n_left") >= interval.get("n_left") && categorySet.getInt("n_right") <= interval.get("n_right"));
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally{
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Return n_left and n_right fields for a given category
     *
     * @param categoryId
     * @return array
     */
    public static Map<String, Integer> getInterval(int categoryId){
        String cacheKey = "jeprolab_category_get_interval_" + categoryId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            Map<String, Integer> result = new HashMap<>();
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("n_left") + ", " + staticDataBaseObject.quoteName("n_right");
            query += ", " + staticDataBaseObject.quoteName("depth_level") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
            query += " WHERE " + staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

            staticDataBaseObject.setQuery(query);
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (Map<String, Integer>)JeproLabCache.getInstance().retrieve(cacheKey);
    }


    public boolean updatePosition(String way, int position){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT category." + dataBaseObject.quoteName("category_id") + ", category_lab." + dataBaseObject.quoteName("position");
        query += ", category." + dataBaseObject.quoteName("parent_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category");
        query += " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category." + dataBaseObject.quoteName("parent_id");
        query += " = " + this.parent_id + " ORDER BY category_lab." + dataBaseObject.quoteName("position") + " ASC";

        dataBaseObject.setQuery(query);
        ResultSet categorySet = dataBaseObject.loadObjectList();
        if (categorySet != null) {
            JeproLabCategoryModel movedCategory = null;
            try {
                int categoryId;
                while(categorySet.next()){
                    categoryId = categorySet.getInt("category_id");
                    if (categoryId == this.category_id) {
                        movedCategory = new JeproLabCategoryModel();
                        movedCategory.category_id = categoryId;
                        movedCategory.position = categorySet.getInt("position");
                        movedCategory.parent_id = categorySet.getInt("parent_id");
                    }
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }

            if (movedCategory == null) {
                return false;
            }
            // < and > statements rather than BETWEEN operator
            // since BETWEEN is treated differently according to databases
            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
            query += " SET category." + dataBaseObject.quoteName("position") + " = category." + dataBaseObject.quoteName("position");
            query += (way.equals("down")  ? "- 1 " : " + 1") + ", category_lab." + dataBaseObject.quoteName("position") + " = category_lab.";
            query += dataBaseObject.quoteName("position") + (way.equals("down") ? " - 1 " : " + 1 ") + ", category." + dataBaseObject.quoteName("date_upd");
            query += " = " + JeproLabTools.date("Y-m-d H:i:s") + " WHERE category_lab." + dataBaseObject.quoteName("position");
            query += ( way.equals("down") ? " > "  : " < " ) + movedCategory.position + " AND category_lab." + dataBaseObject.quoteName("position");
            query += ( way.equals("down") ? " <= "  : " >= " ) + position + " AND category." + dataBaseObject.quoteName("parent_id") + " = " + movedCategory.parent_id;

            dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.query(false);

            query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category");
            query += " SET category." + dataBaseObject.quoteName("position") + " = " + position + ", category_lab." + dataBaseObject.quoteName("position") ;
            query += " = " + position + ", category." + dataBaseObject.quoteName("date_upd") + " = " + JeproLabTools.date("Y-m-d H:i:s") + " WHERE category.";
            query += dataBaseObject.quoteName("parent_id") + " = " + movedCategory.parent_id + " AND category." + dataBaseObject.quoteName("category_id");
            query += " = " + movedCategory.category_id;

            dataBaseObject.setQuery(query);
            result &= dataBaseObject.query(false);

            /*Hook::exec('actionCategoryUpdate', array('category' => new Category($moved_category['id_category']))
            $result = (Db::getInstance()->execute('

            )
                    && Db::getInstance()->execute('
                    );*/
            return result;
        }
        return false;
    }

    public static boolean setNewGroupForHome(int groupId){
        if (groupId < 0) {
            return false;
        }
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "INSERT INTO " + staticDataBaseObject.quoteName("#__jeprolab_category_group") + " (" + staticDataBaseObject.quoteName("category_id");
        query += ", " +  staticDataBaseObject.quoteName("group_id") + ") VALUES (" + JeproLabContext.getContext().laboratory.getCategoryId() + ", ";
        query += groupId + ")";

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.query(false);
    }

    /**
     * checkAccess return true if id_customer is in a group allowed to see this category.
     *
     * @param customerId
     * @access public
     * @return bool true if access allowed for customer $id_customer
     */
    public boolean checkAccess(int customerId){
        String cacheKey = "jeprolab_category_check_access_" + this.category_id + "_" + customerId + (customerId <= 0 ? "_" + JeproLabGroupModel.getCurrent().group_id : "");
        String query;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if (customerId <= 0){
                query = "SELECT category_group." + dataBaseObject.quoteName("group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_group");
                query += " AS category_group WHERE category_group." + dataBaseObject.quoteName("category_id") + " = " + this.category_id ;
                query += " AND category_group." + dataBaseObject.quoteName("group_id") + " = " + JeproLabGroupModel.getCurrent().group_id;
            } else {
                query = "SELECT category_group." + dataBaseObject.quoteName("group_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category_group");
                query += " AS category_group INNER JOIN " + dataBaseObject.quoteName("#__jeprolab_customer_group") + " AS customer_group ON (customer_group.";
                query += dataBaseObject.quoteName("group_id") + " = category_group." + dataBaseObject.quoteName("group_id") + " AND customer_group.";
                query += dataBaseObject.quoteName("customer_id") + " = " + customerId + ") WHERE customer_group." + dataBaseObject.quoteName("category_id");
                query += " = " + this.category_id ;
            }
            dataBaseObject.setQuery(query);
            boolean result = dataBaseObject.loadValue("group_id") > 0;
            JeproLabCache.getInstance().store(cacheKey, result);
            return result;
        }
        return (boolean)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    /**
     * Specify if a category already in base
     *
     * @param categoryId Category id
     * @return bool
     */
    public static boolean categoryExists(int categoryId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("category_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category");
        query += " WHERE " + staticDataBaseObject.quoteName("category_id") + " = " + categoryId;

        staticDataBaseObject.setQuery(query);
        return staticDataBaseObject.loadValue("category_id") > 0;
    }

    public String getName(){
        return getName(0);
    }

    public String getName(int langId){
        if (langId <= 0) {
            if (this.name.containsKey("lang_" + JeproLabContext.getContext().language.language_id)) {
                langId = JeproLabContext.getContext().language.language_id;
            } else {
                langId = JeproLabSettingModel.getIntValue("default_lang");
            }
        }
        return this.name.containsKey("lang_" + langId) ? this.name.get("lang_" + langId): "";
    }

    public List<JeproLabCategoryModel> getAllParents(){
        return getAllParents(0);
    }

    /**
     * Return an array of all parents of the current category
     *
     * @param langId
     * @return list of Category's parents
     */
    public List<JeproLabCategoryModel> getAllParents(int langId){
        if (langId <= 0){
            langId = JeproLabContext.getContext().language.language_id;
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category." + dataBaseObject.quoteName("category_id") + " = category_lang." + dataBaseObject.quoteName("category_id") + " AND category_lang.";
        query += dataBaseObject.quoteName("lang_id") + " = " + langId + ")  WHERE category." + dataBaseObject.quoteName("n_left") + " = " + this.n_left + ", category." + dataBaseObject.quoteName("n_right") + " = " + this.n_right;

        dataBaseObject.setQuery(query);
        ResultSet categorySet = dataBaseObject.loadObjectList();
        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(categorySet != null){
            try{
                JeproLabCategoryModel category;
                while(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    category.parent_id = categorySet.getInt("parent_id");
                    categories.add(category);
                }
            }catch(SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }

        return categories;
    }

    /**
     * Copy products from a category to another
     *
     * @param oldId Source category ID
     * @param newId Destination category ID
     * @return bool Duplication result
     */
    public static boolean duplicateAnalyzeCategories(int oldId, int newId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        $sql = 'SELECT `id_category`
        FROM `'._DB_PREFIX_.'category_product`
        WHERE `id_product` = '.(int)$id_old;
        $result = Db::getInstance()->executeS($sql);

        $row = array();
        if ($result) {
            foreach ($result as $i) {
                $row[] = '('.implode(', ', array((int)$id_new, $i['id_category'], '(SELECT tmp.max + 1 FROM (
                        SELECT MAX(cp.`position`) AS max
                        FROM `'._DB_PREFIX_.'category_product` cp
                WHERE cp.`id_category`='.(int)$i['id_category'].') AS tmp)'
                )).')';
            }
        }

        $flag = Db::getInstance()->execute('
            INSERT IGNORE INTO `'._DB_PREFIX_.'category_product` (`id_product`, `id_category`, `position`)
        VALUES '.implode(',', $row)
        );
        return $flag;
    }

    /**
     * This method allow to return children categories with the number of sub children selected for a product
     *
     * @param int $id_parent
     * @param int $id_product
     * @param int $id_lang
     * @return array
     */
    public static function getChildrenWithNumberSelectedSubCategory($id_parent, $selected_cat, $id_lang, Shop $shop = null, $use_shop_context = true)
    {
        if (!$shop) {
            $shop = Context::getContext()->shop;
        }

        $id_shop = $shop->id ? $shop->id : Configuration::get('PS_SHOP_DEFAULT');
        $selected_cat = explode(',', str_replace(' ', '', $selected_cat));
        $sql = '
        SELECT c.`id_category`, c.`level_depth`, cl.`name`,
        IF((
                SELECT COUNT(*)
        FROM `'._DB_PREFIX_.'category` c2
        WHERE c2.`id_parent` = c.`id_category`
        ) > 0, 1, 0) AS has_children,
        '.($selected_cat ? '(
            SELECT count(c3.`id_category`)
        FROM `'._DB_PREFIX_.'category` c3
        WHERE c3.`nleft` > c.`nleft`
        AND c3.`nright` < c.`nright`
        AND c3.`id_category`  IN ('.implode(',', array_map('intval', $selected_cat)).')
        )' : '0').' AS nbSelectedSubCat
        FROM `'._DB_PREFIX_.'category` c
        LEFT JOIN `'._DB_PREFIX_.'category_lang` cl ON (c.`id_category` = cl.`id_category` '.Shop::addSqlRestrictionOnLang('cl', $id_shop).')
        LEFT JOIN `'._DB_PREFIX_.'category_shop` cs ON (c.`id_category` = cs.`id_category` AND cs.`id_shop` = '.(int)$id_shop.')
        WHERE `id_lang` = '.(int)$id_lang.'
        AND c.`id_parent` = '.(int)$id_parent;
        if (Shop::getContext() == Shop::CONTEXT_SHOP && $use_shop_context) {
        $sql .= ' AND cs.`id_shop` = '.(int)$shop->id;
    }
        if (!Shop::isFeatureActive() || Shop::getContext() == Shop::CONTEXT_SHOP && $use_shop_context) {
        $sql .= ' ORDER BY cs.`position` ASC';
    }

        return Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($sql);
    }

    /**
     *
     * @param int  $id_parent
     * @param int  $id_lang
     * @param bool $active
     * @param bool $id_shop
     * @return array
     */
    public static function hasChildren($id_parent, $id_lang, $active = true, $id_shop = false)
    {
        if (!Validate::isBool($active)) {
        die(Tools::displayError());
    }

        $cache_id = 'Category::hasChildren_'.(int)$id_parent.'-'.(int)$id_lang.'-'.(bool)$active.'-'.(int)$id_shop;
        if (!Cache::isStored($cache_id)) {
        $query = 'SELECT c.id_category, "" as name
        FROM `'._DB_PREFIX_.'category` c
        LEFT JOIN `'._DB_PREFIX_.'category_lang` cl ON (c.`id_category` = cl.`id_category`'.Shop::addSqlRestrictionOnLang('cl').')
        '.Shop::addSqlAssociation('category', 'c').'
        WHERE `id_lang` = '.(int)$id_lang.'
        AND c.`id_parent` = '.(int)$id_parent.'
        '.($active ? 'AND `active` = 1' : '').' LIMIT 1';
        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS($query);
        Cache::store($cache_id, $result);
        return $result;
    }
        return Cache::retrieve($cache_id);
    }

    /**
     * Return current category childs
     *
     * @param int $id_lang Language ID
     * @param bool $active return only active categories
     * @return array Categories
     */
    public function getSubCategories($id_lang, $active = true)
    {
        $sql_groups_where = '';
        $sql_groups_join = '';
        if (Group::isFeatureActive()) {
        $sql_groups_join = 'LEFT JOIN `'._DB_PREFIX_.'category_group` cg ON (cg.`id_category` = c.`id_category`)';
        $groups = FrontController::getCurrentCustomerGroups();
        $sql_groups_where = 'AND cg.`id_group` '.(count($groups) ? 'IN ('.implode(',', $groups).')' : '='.(int)Group::getCurrent()->id);
    }

        $result = Db::getInstance(_PS_USE_SQL_SLAVE_)->executeS('
            SELECT c.*, cl.id_lang, cl.name, cl.description, cl.link_rewrite, cl.meta_title, cl.meta_keywords, cl.meta_description
            FROM `'._DB_PREFIX_.'category` c
        '.Shop::addSqlAssociation('category', 'c').'
        LEFT JOIN `'._DB_PREFIX_.'category_lang` cl ON (c.`id_category` = cl.`id_category` AND `id_lang` = '.(int)$id_lang.' '.Shop::addSqlRestrictionOnLang('cl').')
        '.$sql_groups_join.'
        WHERE `id_parent` = '.(int)$this->id.'
        '.($active ? 'AND `active` = 1' : '').'
        '.$sql_groups_where.'
        GROUP BY c.`id_category`
        ORDER BY `level_depth` ASC, category_shop.`position` ASC');

        $formated_medium = ImageType::getFormatedName('medium');

        foreach ($result as &$row) {
        $row['id_image'] = Tools::file_exists_cache(_PS_CAT_IMG_DIR_.$row['id_category'].'.jpg') ? (int)$row['id_category'] : Language::getIsoById($id_lang).'-default';
        $row['legend'] = 'no picture';
    }
        return $result;
    }

    public static List<JeproLabCategoryModel> getSimpleCategories(int langId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang." + staticDataBaseObject.quoteName("name");
        query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." + staticDataBaseObject.quoteName("category_id") + " = category_lang." + staticDataBaseObject.quoteName("category_id");
        query += JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang") + JeproLabLaboratoryModel.addSqlAssociation("category") + " WHERE category_lang.";
        query += staticDataBaseObject.quoteName("lang_id") + " = " + langId + " AND category." + staticDataBaseObject.quoteName("category_id") + " != ";
        query += JeproLabSettingModel.getIntValue("root_category") + " GROUP BY category." + staticDataBaseObject.quoteName("category_id") + " ORDER BY category.";
        query += staticDataBaseObject.quoteName("category_id") + ", category_lab." + staticDataBaseObject.quoteName("position");

        staticDataBaseObject.setQuery(query);
        ResultSet categorySet = staticDataBaseObject.loadObjectList();
        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(categorySet != null){
            try{
                JeproLabCategoryModel category;
                while(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    String key = "lang_" + langId;
                    category.name.put(key, categorySet.getString("name"));
                    categories.add(category);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        return categories;
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(){
        return getAllCategoriesName(0, 0, true, null);
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId){
        return getAllCategoriesName(rootCategoryId, 0, true, null);
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId){
        return getAllCategoriesName(rootCategoryId, langId, true, null);
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId, boolean active){
        return getAllCategoriesName(rootCategoryId, langId, active, null);
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId, boolean active, List<Integer> groups){
        return getAllCategoriesName(rootCategoryId, langId, active, groups, true, "", "", "");
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId, boolean active, List<Integer> groups, boolean useLabRestriction){
        return getAllCategoriesName(rootCategoryId, langId, active, groups, useLabRestriction, "", "", "");
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId, boolean active, List<Integer> groups, boolean useLabRestriction, String sqlFilter){
        return getAllCategoriesName(rootCategoryId, langId, active, groups, useLabRestriction, sqlFilter, "", "");
    }

    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId, boolean active, List<Integer> groups, boolean useLabRestriction, String sqlFilter, String sqlSort){
        return getAllCategoriesName(rootCategoryId, langId, active, groups, useLabRestriction, sqlFilter, sqlSort, "");
    }
    public static List<JeproLabCategoryModel> getAllCategoriesName(int rootCategoryId, int langId, boolean active, List<Integer> groups, boolean useLabRestriction, String sqlFilter, String sqlSort, String sqlLimit){
        if (isset($root_category) && !Validate::isInt($root_category)){
            die(Tools::displayError ());
        }

        if (!Validate::isBool($active)){
            die(Tools::displayError ());
        }

        if (!groups.isEmpty() && JeproLabGroupModel.isFeaturePublished() && !is_array($groups)){
            $groups = (array) $groups;
        }
        String cacheKey = rootCategoryId + "_" + langId + "_" + (active ? "1" : "0") + "_" + (useLabRestriction ? "1" : "0") ;
        cacheKey += ((!groups.isEmpty() && JeproLabGroupModel.isFeaturePublished()) ? "_" + groupsList : "");

        cacheKey = "jeprolab_category_get_all_categories_name_" + JeproLabTools.md5(cacheKey);

        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT category." + staticDataBaseObject.quoteName("category_id") + ", category_lang."  + staticDataBaseObject.quoteName("name");
            query += " FROM "  + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category ";
            query += (useLabRestriction ? JeproLabLaboratoryModel.addSqlAssociation("category") : "") + " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_lang");
            query += " AS category_lang ON category." + staticDataBaseObject.quoteName("category_id") + " = category_lang.";
            query += staticDataBaseObject.quoteName("category_id") + JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
            query += (!groups.isEmpty() && JeproLabGroupModel.isFeaturePublished() ? " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category_group") +
                " AS category_group ON category." + staticDataBaseObject.quoteName("category_id") + " = category_group." + staticDataBaseObject.quoteName("category_id") : " " );
            query += ((rootCategoryId > 0) ? " RIGHT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_category") + " AS category_2 ON category_2." +
                    staticDataBaseObject.quoteName("category_id") +  " = " + rootCategoryId + " AND category." + staticDataBaseObject.quoteName("n_left") +
                    " >= category_2." + staticDataBaseObject.quoteName("n_left") + " AND category." + staticDataBaseObject.quoteName("n_right") + " <= category_2." +
                    staticDataBaseObject.quoteName("n_right") : " ") + " WHERE 1 " + sqlFilter +  " " + (langId > 0 ? " AND " + staticDataBaseObject.quoteName("lang_id") + " = " +
                    langId  : " ") + (active ? " AND category." + staticDataBaseObject.quoteName("published") + " = 1 " : " ") ;
            query += (!groups.isEmpty() && JeproLabGroupModel.isFeaturePublished()  ? " AND category_group." + staticDataBaseObject.quoteName("group_id") + " IN(" + groupList + ") " : " ");
            query += (langId <= 0 || (!groups.isEmpty() && JeproLabGroupModel.isFeaturePublished()) ? " GROUP BY category." + staticDataBaseObject.quoteName("category_id") : " ");
            query += (!sqlSort.equals("") ? sqlSort : " ORDER BY category." + staticDataBaseObject.quoteName("depth_level") + " ASC ") ;
            query += (sqlSort.equals("") && useLabRestriction ? ", category_lab." + staticDataBaseObject.quoteName("position") + " ASC " : " ");
            query += (!sqlLimit.equals(" ") ? sqlLimit : " ");

            staticDataBaseObject.setQuery(query);
            ResultSet categorySet = staticDataBaseObject.loadObjectList();
            List<JeproLabCategoryModel> categories = new ArrayList<>();

            if(categorySet != null){
                try{
                    JeproLabCategoryModel category;
                    while(categorySet.next()){
                        category = new JeproLabCategoryModel();
                        category.category_id = categorySet.getInt("category_id");
                        category.name.put("lang_" + langId, categorySet.getString("name"));
                        categories.add(category);
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }

            JeproLabCache.getInstance().store (cacheKey, categories);
            return categories;
        }else{
            return (List<JeproLabCategoryModel>)JeproLabCache.getInstance().retrieve(cacheKey);
        }
    }

    /**
     * Updates depth_level for all children of the given id_category
     *
     * @param categoryId parent category
     */
    public void recalculateDepthLevel(int categoryId){
        if (categoryId <= 0) {
            throw new PrestaShopException('id category is not numeric');
        }
        /* Gets all children */
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query;
        /* Gets level_depth */
        query = "SELECT " + dataBaseObject.quoteName("depth_level") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category");
        query += " WHERE " + dataBaseObject.quoteName("category_id") + " = " + categoryId;

        dataBaseObject.setQuery(query);
        int level = (int)dataBaseObject.loadValue("depth_level");

        query = "SELECT " + dataBaseObject.quoteName("category_id") + ", " + dataBaseObject.quoteName("parent_id") + ", ";
        query += dataBaseObject.quoteName("depth_level") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " WHERE ";
        query += dataBaseObject.quoteName("parent_id") + " = " + categoryId;

        dataBaseObject.setQuery(query);
        ResultSet categoriesSet = dataBaseObject.loadObjectList();
        /* Updates level_depth for all children */
        if(categoriesSet != null){
            try{
                while(categoriesSet.next()){
                    query = "UPDATE "  + dataBaseObject.quoteName("#__jeprolab_category") + " SET " + dataBaseObject.quoteName("depth_level");
                    query += " = " + level + 1 + " WHERE " + dataBaseObject.quoteName("category_id") + " = " + categoriesSet.getInt("category_id");

                    dataBaseObject.setQuery(query);
                    dataBaseObject.query(false);
                    /* Recursive call */
                    this.recalculateDepthLevel(categoriesSet.getInt("category_id"));
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }
        }
    }

    /**
     * Delete several categories from database
     *
     * return boolean Deletion result
     */
    public boolean deleteSelection(List<Integer> categories){
        boolean result = true;
        JeproLabCategoryModel category;
        for(Integer categoryId : categories) {
            category = new JeproLabCategoryModel(categoryId);
            if (category.isRootCategoryForALaboratory()){
                return false;
            } else {
                result &= category.delete();
            }
        }
        return result;
    }

    /**
     * Recursively add specified category childs to $to_delete array
     *
     * @param array &$to_delete Array reference where categories ID will be saved
     * @param categoryId Parent category ID
     */
    protected function recursiveDelete(&$to_delete, int categoryId){
        if (!is_array($to_delete) || !$id_category) {
            die(Tools::displayError());
        }

        String query = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category") ;
        query += " WHERE " + dataBaseObject.quoteName("parent_id") + " = " + categoryId;

        dataBaseObject.setQuery(query);
        ResultSet categorySet = dataBaseObject.loadObjectList();


        $result = Db::getInstance()->executeS('
            ;
        foreach ($result as $row) {
        $to_delete[] = (int)$row['id_category'];
        $this->recursiveDelete($toDelete, (int)$row['id_category']);
    }
    }

    public static function recurseCategory($categories, $current, int categoryId = null, int selectedId = 1){
        if (categoryId <= 0) {
            categoryId = JeproLabSettingModel.getIntValue("root_category");
        }

        echo '<option value="'.$id_category.'"'.(($id_selected == $id_category) ? ' selected="selected"' : '').'>'.
            str_repeat('&nbsp;', $current['infos']['level_depth'] * 5).stripslashes($current['infos']['name']).'</option>';
        if (isset($categories[$id_category])) {
            foreach (array_keys($categories[$id_category]) as $key) {
                Category::recurseCategory($categories, $categories[$id_category][$key], $key, $id_selected);
            }
        }
    }

    /**
     * Recursive scan of subcategories
     *
     * @param int $max_depth Maximum depth of the tree (i.e. 2 => 3 levels depth)
     * @param int $current_depth specify the current depth in the tree (don't use it, only for rucursivity!)
     * @param int $id_lang Specify the id of the language used
     * @param array $excluded_ids_array specify a list of ids to exclude of results
     *
     * @return array Subcategories lite tree
     */
    public function recurseLiteCategTree($max_depth = 3, $current_depth = 0, $id_lang = null, $excluded_ids_array = null)
    {
        $id_lang = is_null($id_lang) ? Context::getContext()->language->id : (int)$id_lang;

        $children = array();
        $subcats = $this->getSubCategories($id_lang, true);
        if (($max_depth == 0 || $current_depth < $max_depth) && $subcats && count($subcats)) {
            foreach ($subcats as &$subcat) {
                if (!$subcat['id_category']) {
                    break;
                } elseif (!is_array($excluded_ids_array) || !in_array($subcat['id_category'], $excluded_ids_array)) {
                    $categ = new Category($subcat['id_category'], $id_lang);
                    $children[] = $categ->recurseLiteCategTree($max_depth, $current_depth + 1, $id_lang, $excluded_ids_array);
                }
            }
        }

        if (is_array($this->description)) {
            foreach ($this->description as $lang => $description) {
                $this->description[$lang] = Category::getDescriptionClean($description);
            }
        } else {
            $this->description = Category::getDescriptionClean($this->description);
        }

        return array(
                'id' => (int)$this->id,
        'link' => Context::getContext()->link->getCategoryLink($this->id, $this->link_rewrite),
            'name' => $this->name,
            'desc'=> $this->description,
            'children' => $children
        );
    }

    /**
     * update category positions in parent
     *
     * @param mixed $null_values
     * @return bool
     */
    public function update($null_values = false){
        if (this.parent_id == this.category_id) {
            throw new PrestaShopException('a category cannot be its own parent');
        }

        if (this.is_root_category && this.parent_id != JeproLabSettingModel.getIntValue("root-category")){
            this.is_root_category = false;
        }

        // Update group selection
        this.updateGroup(this.groupBox);

        boolean changed = false;
        if(this.depth_level != this.calculateDepthLevel()) {
            this.depth_level = this.calculateDepthLevel();
            changed = true;
        }

        // If the parent category was changed, we don't want to have 2 categories with the same position
        if (!changed) {
            changed = this.getDuplicatePosition() > 0;
        }
        if (changed) {
            if (Tools::isSubmit('checkBoxShopAsso_category')) {
                foreach (Tools::getValue('checkBoxShopAsso_category') as $id_asso_object => $row) {
                    foreach ($row as $id_shop => $value) {
                        $this->addPosition((int)Category::getLastPosition((int)$this->id_parent, (int)$id_shop), (int)$id_shop);
                    }
                }
            } else {
                foreach (Shop::getShops(true) as $shop) {
                    $this->addPosition((int)Category::getLastPosition((int)$this->id_parent, $shop['id_shop']), $shop['id_shop']);
                }
            }
        }

        $ret = parent::update($null_values);
        if ($changed && (!isset($this->doNotRegenerateNTree) || !$this->doNotRegenerateNTree)) {
            JeproLabCategoryModel.cleanPositions(this.parent_id);
            JeproLabCategoryModel.regenerateEntireNestedTree();
            this.recalculateDepthLevel(this.category_id);
        }
        Hook::exec('actionCategoryUpdate', array('category' => $this));
        return $ret;
    }

}