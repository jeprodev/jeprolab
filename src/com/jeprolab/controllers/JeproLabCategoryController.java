package com.jeprolab.controllers;


import com.jeprolab.JeproLab;

import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabLaboratoryModel;
import com.jeprolab.models.JeproLabSettingModel;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JeproLabCategoryController extends JeproLabController {
    JeproLabCategoryModel category;
    private Pagination pagination;
    @FXML
    public TableView categoryTableView;
    public TableColumn categoryIndexColumn, categoryCheckBoxColumn, categoryStatusColumn, categoryNameColumn;
    public TableColumn categoryDescriptionColumn, categoryPositionColumn, categoryActionColumn;
/*
    public Label jeproLabCategoryNameLabel, jeproLabPublishedCategoryLabel, jeproLabCategoryParentLabel, jeproLabCategoryDescriptionLabel;
    public Label jeproLabCategoryImageChooserLabel, jeproLabCategoryMetaTileLabel, jeproLabCategoryMetaDescriptionLabel;
    public Label jeproLabCategoryMetaKeyWordLabel, jeproLabCategoryLinkRewriteLabel, jeproLabCategoryIsRootLabel, jeproLabCategoryAssociatedLabsLabel;
    public Label jeproLabCategoryAllowedGroupLabel, jeproLabCategoryLabel, jeproLabAddCategoryFormTitle;
    public JeproMultiLangTextArea jeproLabCategoryDescription;
    public JeproCategoryTree jeproLabCategoryParent;
    public JeproFormPanel jeproLabCategoryFormWrapper;
    public JeproFormPanelTitle jeproLabCategoryFormTitleWrapper;
    public JeproFormPanelContainer jeproLabCategoryFormContainerWrapper;
    public JeproSwitchButton jeproLabPublishedCategory, jeproLabCategoryIsRoot;
    public JeproMultiLangTextField jeproLabCategoryName, jeproLabCategoryMetaTile,  jeproLabCategoryMetaKeyWord;
    public JeproMultiLangTextField jeproLabCategoryMetaDescription, jeproLabCategoryLinkRewrite;
    public GridPane jeproLabCategoryFormLayout;*/

    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        if (context.category_id > 0 && !this.task.equals("delete")) {
                this.category = new JeproLabCategoryModel(context.category_id);
        } else {
            if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                category = new JeproLabCategoryModel(context.laboratory.category_id);
            } else if (JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && JeproLabSettingModel.getIntValue("multilab_feature_active") > 1 && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() > 1) {
                category = JeproLabCategoryModel.getTopCategory();
            } else {
                category = new JeproLabCategoryModel(JeproLabSettingModel.getIntValue("root_category"));
            }
        }
        if(!isInitialized){
            isInitialized = true;
            if (category != null && !category.isAssociatedToLaboratory() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                this.task = "edit";
                this.context.category_id = category.category_id;
                try {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 75;
        categoryTableView.setPrefWidth(0.98 * JeproLab.APP_WIDTH);
        categoryTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        categoryTableView.setLayoutY(20);
        categoryIndexColumn.setText("#");
        categoryIndexColumn.setPrefWidth(20);
        categoryCheckBoxColumn.setText("");
        categoryCheckBoxColumn.setPrefWidth(25);
        categoryStatusColumn.setText("");
        categoryStatusColumn.setPrefWidth(30);
        categoryNameColumn.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        categoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        categoryDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        categoryDescriptionColumn.setPrefWidth(0.6 * remainingWidth);
        categoryPositionColumn.setText(bundle.getString("JEPROLAB_POSITION_LABEL"));
        categoryPositionColumn.setPrefWidth(0.06 * remainingWidth);
        categoryActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        categoryActionColumn.setPrefWidth(0.09 * remainingWidth);

        initializeContent();
    }

    @Override
    protected void initializeContent(){
        int categoryId = 0;

        if(categoryId > 0){
            this.category = new JeproLabCategoryModel(categoryId);
        }else{
            if(JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                this.category = new JeproLabCategoryModel(context.laboratory.category_id);
            }else if((JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1) && (JeproLabSettingModel.getIntValue("multilab_feature_active") > 0) && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() != 1){
                this.category = JeproLabCategoryModel.getTopCategory();
            }else{
                this.category = new JeproLabCategoryModel(JeproLabSettingModel.getIntValue("root_category"));
            }
        }

        if(category.category_id > 0 && !category.isAssociatedToLaboratory() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
            JeproLab.request.setRequest("view=category&task=edit&category_id=" + context.laboratory.getCategoryId() + "&" + JeproLabTools.getCategoryToken() + "=1");
        }
        /** getting and showing items list **/
        List<JeproLabCategoryModel> categoryTree ;
        if(!JeproLabLaboratoryModel.isFeaturePublished() || JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && context.category_id > 0){
            JeproLabCategoryModel categoryTr = this.category.getTopCategory();
            categoryTree = new ArrayList<>();
            categoryTree.add(categoryTr);
        }else{
            categoryTree = this.category.getParentsCategories();
            JeproLabCategoryModel categoryTr = categoryTree.get(categoryTree.size() - 1);
            if(categoryTree != null && !JeproLabLaboratoryModel.isFeaturePublished() && (categoryTr != null && categoryTr.parent_id != 0) ){

            }
        }

        JeproLabCategoryModel categoryModel = new JeproLabCategoryModel();
        List<JeproLabCategoryModel> categories = categoryModel.getCategoriesList();
        pagination = categoryModel.getPagination();
    }

    @Override
    protected void renderDetails(){

    }

    @Override
    public void updateToolBar(){}

    private static class JeproLabCategory {
        /*private categoryIndex;
        private categoryCheckBox;
        private categoryStatus;
        private categoryName;
        private categoryDescription;
        private categoryPosition;
        private categoryActions; */
        public JeproLabCategory(JeproLabCategoryModel categoryModel){

        }
    }
}