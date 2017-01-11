package com.jeprolab.models;

import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
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

    public String image_path = "default";
    public String image_dir = "";

    public int groupBox[];
    public List<Integer> check_box_lab_associated_category = new ArrayList<>();

    protected boolean deleted = true;

    protected boolean do_not_regenerate_nested_tree = true;

    private Map<Integer,JeproLabLanguageModel> languages;

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
        //todo complete
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

    public static List<JeproLabCategoryModel> getCategoriesWithoutParent(){
        String cacheKey = "jeprolab_category_get_Categories_Without_parent_" + JeproLabContext.getContext().language.language_id;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            List<JeproLabCategoryModel> categories = new ArrayList<>();
            String query = "SELECT DISTINCT category.* FROM " + dataBaseObject.quoteName("#__jeprolab_category") + " AS category";
            query += "	LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_lang") + " AS category_lang ON (category.";
            query += dataBaseObject.quoteName("category_id") + " = category_lang." + dataBaseObject.quoteName("category_id") ;
            query += " AND category_lang." + dataBaseObject.quoteName("lang_id") + " = " + JeproLabContext.getContext().language.language_id;
            query += ") WHERE " + dataBaseObject.quoteName("depth_level") + " = 1";

            dataBaseObject.setQuery(query);
            ResultSet result = dataBaseObject.loadObjectList();
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
                JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored) {
                    JeproLabTools.logExceptionMessage(Level.WARN, ignored);
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + dataBaseObject.quoteName("category_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category");
            query += "	WHERE " + dataBaseObject.quoteName("parent_id") + " = 0";
            dataBaseObject.setQuery(query);
            ResultSet categoryData = dataBaseObject.loadObjectList();
            int categoryId = 0;
            try{
                while(categoryData.next()){
                    categoryId = categoryData.getInt("category_id");
                }
            }catch (SQLException ignored){
                JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored) {
                    JeproLabTools.logExceptionMessage(Level.WARN, ignored);
                }
            }
            JeproLabCache.getInstance().store(cacheKey, new JeproLabCategoryModel(categoryId, langId));
        }
        return (JeproLabCategoryModel)JeproLabCache.getInstance().retrieve(cacheKey);
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
            String groupsList = "";
            List<Integer> groups = JeproLabCustomerModel.getCurrentCustomerGroups();
            for(Integer id : groups){
                groupsList += id + ", ";
            }
            groupsList = (groupsList.endsWith(", ") ? groupsList.substring(0, groupsList.length() - 3) : groupsList);
            sqlGroupsJoin = " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_category_group") + " category_group ON (category_group.";
            sqlGroupsJoin += dataBaseObject.quoteName("category_id") + " = category." + dataBaseObject.quoteName("category_id") + ")";

            sqlGroupsWhere = " AND category_group." + dataBaseObject.quoteName("group_id") ;
            sqlGroupsWhere += (groups.size() > 0 ? " IN (" + groupsList + ") "  : " = " + JeproLabGroupModel.getCurrent().group_id);
        }

        String query = "SELECT category.*, category_lang." + dataBaseObject.quoteName("lang_id") + ", category_lang." + dataBaseObject.quoteName("name");
        query += ", category_lang." + dataBaseObject.quoteName("description") + ", category_lang." + dataBaseObject.quoteName("link_rewrite");
        query += ", category_lang." + dataBaseObject.quoteName("meta_title") + ", category_lang." + dataBaseObject.quoteName("meta_keywords") ;
        query += ", category_lang." + dataBaseObject.quoteName("meta_description") + " FROM " + dataBaseObject.quoteName("#__jeprolab_category");
        query += " AS category " + JeproLabLaboratoryModel.addSqlAssociation("category") + " LEFT JOIN "  + dataBaseObject.quoteName("#__jeprolab_category_lang");
        query += " AS category_lang ON (category." + dataBaseObject.quoteName("category_id") + " = category_lang." + dataBaseObject.quoteName("category_id");
        query += " AND " + dataBaseObject.quoteName("lang_id") + " = " + langId + " " + JeproLabLaboratoryModel.addSqlRestrictionOnLang("category_lang");
        query += " ) " + sqlGroupsJoin + " WHERE " + dataBaseObject.quoteName("parent_id") + " = " + this.category_id ;
        query += (active ? " AND " + dataBaseObject.quoteName("published") + " = 1 " : "") + sqlGroupsWhere + " GROUP BY category.";
        query += dataBaseObject.quoteName("category_id") + " ORDER BY " + dataBaseObject.quoteName("depth_level") + " ASC, category_lab.";
        query += dataBaseObject.quoteName("position") + " ASC";

        dataBaseObject.setQuery(query);

        //$formattedMedium = JeproLabImageModel.JeproLabImageTypeModel.getFormattedName("medium");
        ResultSet categorySet = dataBaseObject.loadObjectList();

        List<JeproLabCategoryModel> categories = new ArrayList<>();

        if(categorySet != null){
            try {
                JeproLabCategoryModel category;
                String imagePath;
                File imageFile;
                while(categorySet.next()){
                    category = new JeproLabCategoryModel();
                    category.category_id = categorySet.getInt("category_id");
                    imagePath = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY + category.category_id + ".jpg";
                    imageFile = new File(imagePath);
                    category.image_path = imageFile.exists() ? String.valueOf(category.category_id) : JeproLabLanguageModel.getIsoCodeByLanguageId(langId) + "_default";

                    categories.add(category);
                }
            }catch (SQLException ignored){
                JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored){
                    JeproLabTools.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return categories;
    }
}
