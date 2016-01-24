package com.jeprolab.assets.extend.controls.tree;

import java.util.List;

/**
 *
 * Created by jeprodev on 24/01/2013.
 */
public class JeproCategoryTree extends JeproTree {

    private int root_category_id;

    private boolean use_lab_restriction;

    private List disabled_categories, selected_categories;

    private String input_name;

    public JeproCategoryTree(){

    }

    public JeproCategoryTree(String treeTitle, int rootCategoryId, int langId, boolean useLabRestriction){
        super();
        DEFAULT_TEMPLATE = "category_tree";
        DEFAULT_NODE_FOLDER_TEMPLATE = "radio_node_folder";
        DEFAULT_NODE_ITEM_TEMPLATE = "radio_node_item";
        if(treeTitle != null && !treeTitle.equals("")){
            this.setTreeTitle(treeTitle);
        }

        if(rootCategoryId > 0){
            this.setRootCategory(rootCategoryId);
        }

        this.setTreeLang(langId);
        this.setLaboratoryRestriction(useLabRestriction);
    }

    public JeproCategoryTree setTreeLang(int langId){
        this.language_id = langId;
        return this;
    }

    public JeproCategoryTree setRootCategory(int categoryId){
        this.root_category_id = categoryId;
        return this;
    }

    public JeproCategoryTree setLaboratoryRestriction(boolean useRestriction){
        this.use_lab_restriction = useRestriction;
        return this;
    }

    public JeproCategoryTree setSelectedCategories(List value){
        /*if (value.isEmpty()) {
            throw new Exception("Selected categories value must be an array");
        }*/
        this.selected_categories = value;
        return this;
    }

    public JeproCategoryTree setInputName(String value) {
        this.input_name = value;
        return this;
    }
}