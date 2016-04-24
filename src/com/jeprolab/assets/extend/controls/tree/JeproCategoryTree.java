package com.jeprolab.assets.extend.controls.tree;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabSettingModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproCategoryTree extends JeproTree{
    private int root_category_id;

    private boolean use_lab_restriction;

    private List<Integer> disabled_categories, selected_categories;

    private String input_name;
    private List<JeproLabCategoryModel> categories;
    private TextField searchField;
    private Button searchButton;

    private TreeView categoryTreeView;

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

        this.setTreeLanguage(langId);
        this.setLaboratoryRestriction(useLabRestriction);
    }

    public JeproCategoryTree setContext(JeproLabContext value){
        this.context = value;
        return this;
    }

    public JeproCategoryTree setTreeTitle(String value){
        this.tree_title = value;
        return this;
    }

    public JeproCategoryTree setTreeLanguage(int langId){
        this.language_id = langId;
        return this;
    }

    public JeproCategoryTree setTreeTemplate(String template){
        this.tree_template = template;
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

    public JeproCategoryTree setSelectedCategories(List<Integer> value){
        if (value.isEmpty()) {
            JeproLabTools.displayError(500, "Selected categories value must be an array");
        }
        this.selected_categories = value;
        return this;
    }

    public JeproCategoryTree setDisabledCategories(List<Integer> values){
        this.disabled_categories = values;
        return this;
    }

    public JeproCategoryTree setTreeCategories(List<JeproLabCategoryModel> values){
        this.categories = values;
        return this;
    }

    public JeproCategoryTree setInputName(String value) {
        this.input_name = value;
        return this;
    }

    public JeproCategoryTree setTreeWidth(double treeWidth){
        this.tree_width = treeWidth;
        return this;
    }

    public List<Integer> getSelectedCategories(){
        if(this.selected_categories == null){
            this.selected_categories = new ArrayList<>();
        }
        return this.selected_categories;
    }

    public List<Integer> getDisabledCategories(){
        if(this.disabled_categories == null){
            this.disabled_categories = new ArrayList<>();
        }
        return this.disabled_categories;
    }

    public int getRootCategoryId(){
        return this.root_category_id;
    }

    public List<JeproLabCategoryModel> getCategories(){
        if(this.categories == null || this.categories.isEmpty()){
            this.setTreeCategories(JeproLabCategoryModel.getNestedCategories(
                    this.getRootCategoryId(), this.getTreeLanguage(), false, null, this.useLaboratoryRestriction()
            ));
        }
        return this.categories;
    }

    private int getSelectedChildNumbers(List<JeproLabCategoryModel> categories, List<Integer> selected){
        return getSelectedChildNumbers(categories, selected, null);
    }

    private int getSelectedChildNumbers(List<JeproLabCategoryModel> categories, List<Integer> selected, JeproLabCategoryModel parent){
        int selectedChildren = 0;

        for(JeproLabCategoryModel category : categories)	{
            if (parent != null && selected.contains(category.category_id)){	selectedChildren++; }

            if (category.children != null && !category.children.isEmpty()) {
                selectedChildren += this.getSelectedChildNumbers(category.children, selected, category);
            }
        }

        if(parent == null){ parent = new  JeproLabCategoryModel(); }
        if (parent.selected_children < 0) {
            parent.selected_children = 0;
        }
        parent.selected_children = selectedChildren;
        return selectedChildren;
    }

    public String getNodeFolderTemplate(){
        if(this.node_folder_template == null || this.node_folder_template.equals("")){
            this.setNodeFolderTemplate(this.DEFAULT_NODE_FOLDER_TEMPLATE);
        }
        return this.node_folder_template;
    }

    public String getNodeItemTemplate(){
        if(this.node_item_template == null || this.node_item_template.equals("")){
            this.setNodeItemTemplate(this.DEFAULT_NODE_ITEM_TEMPLATE);
        }
        return this.node_item_template;
    }

    public JeproCategoryTree setNodeFolderTemplate(String value){
        this.node_folder_template = value;
        return this;
    }

    public JeproCategoryTree setNodeItemTemplate(String value){
        this.node_item_template = value;
        return this;
    }

    private void disableCategories(List<JeproLabCategoryModel> categories){
        disableCategories(categories, null);
    }

    private void disableCategories(List<JeproLabCategoryModel> categories, List<Integer> disabledCategories){
        for(JeproLabCategoryModel category : categories){
            if ((disabledCategories != null && disabledCategories.size() > 0) && disabledCategories.contains(category.category_id)){
                category.disabled = true;
                if (category.children != null && category.children.size() > 0){
                    disableCategories(category.children);
                }
            }else if (category.children != null && category.children.size() > 0) {
                disableCategories(category.children, disabledCategories);
            }
        }
    }

    private void renderNodes(){
        render(null);
    }

    private void renderNodes(List<JeproLabCategoryModel> categories){
        if(categories == null){ categories = getCategories(); }
        int rootCategoryId = JeproLabSettingModel.getIntValue("root_category");
        int langId = JeproLabContext.getContext().language.language_id;
        String templateNodeFolder = this.getNodeFolderTemplate();
        String templateNodeItem = this.getNodeItemTemplate();
        for(JeproLabCategoryModel category : categories){
            if(!category.children.isEmpty()) {
                if (templateNodeFolder.equals("tree_node_folder")) {

                }else if(templateNodeFolder.equals("tree_node_folder_checkbox")){

                }else if(templateNodeFolder.equals("tree_node_folder_checkbox_labs")){

                }else if(templateNodeFolder.equals("tree_node_folder_radio")){

                }
            }else{
                if (templateNodeItem.equals("tree_node_item")) {

                }else if(templateNodeItem.equals("tree_node_item_checkbox")){

                }else if(templateNodeItem.equals("tree_node_item_checkbox_labs")){

                }else if(templateNodeItem.equals("tree_node_item_radio")){

                }
            }
        }

        if(this.getTreeTemplate().equals("associated_categories")){

        }
    }

    public void render(){
        render(null);
    }

    public void render(List<JeproLabCategoryModel> categories){
        Button collapseAll, expandAll;
        if(categories == null){ categories = getCategories(); }

        if(this.disabled_categories != null && !this.disabled_categories.isEmpty()){
            this.disableCategories(categories, this.getDisabledCategories());
        }

        if(this.selected_categories != null && !this.selected_categories.isEmpty()){
            this.getSelectedChildNumbers(categories, this.getSelectedCategories());
        }

        /** main rendering **/
        if(!this.getTreeTitle().equals("") || this.useToolBar()){
            toolBarPane = new Pane();
            HBox titleWrapper = new HBox(4);
            if(!this.getTreeTitle().equals("")){
                ImageView treeIcon = new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/tag.png")));
                Label treeTitleLabel = new Label(this.getTreeTitle());
                treeTitleLabel.getStyleClass().add("input-label");
                HBox.setMargin(treeIcon, new Insets(4, 4, 4, 4));
                HBox.setMargin(treeTitleLabel, new Insets(4, 4, 4, 4));
                titleWrapper.getChildren().addAll(treeIcon, treeTitleLabel);
            }

            if(this.useSearchBox()){
                HBox searchFormWrapper = new HBox(2);
                searchField = new TextField();
                searchField.setPromptText(JeproLab.getBundle().getString("JEPROLAB_FIND_A_CATEGORY_LABEL"));
                searchButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/view.png"))));
                searchButton.getStyleClass().add("icon-btn");
                searchFormWrapper.getChildren().addAll(searchField, searchButton);
                titleWrapper.getChildren().add(searchFormWrapper);
            }

            toolBarPane.getChildren().add(titleWrapper);
            toolBarPane.getStyleClass().add("form-panel-title-gray");
            toolBarPane.setPrefSize(this.getTreeWidth(), 25);
            this.getChildren().add(toolBarPane);
        }
        categoryTreeView = new TreeView();
        categoryTreeView.setPrefSize(this.getTreeWidth(), 200);
        this.renderNodes(categories);

        if(this.getTreeTemplate().equals("associated_categories")){

        }
        contentPane.getChildren().add(categoryTreeView);
        contentPane.setLayoutY(25);
        this.getChildren().add(contentPane);
        this.setPrefWidth(this.getTreeWidth());
    }

    public JeproCategoryTree setUseCheckBox(boolean useCheckBox){
        this.use_check_box = useCheckBox;
        return this;
    }

    public JeproCategoryTree setUseSearchBox(boolean useSearchBox){
        this.use_search_box = useSearchBox;
        return this;
    }
}
