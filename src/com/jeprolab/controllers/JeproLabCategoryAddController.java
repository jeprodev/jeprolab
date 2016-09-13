package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.extend.controls.tree.JeproCategoryTree;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabGroupModel;
import com.jeprolab.models.JeproLabImageModel;
import com.jeprolab.models.JeproLabSettingModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabCategoryAddController extends JeproLabController{
    private Button saveCategoryBtn, cancelBtn;
    JeproLabCategoryModel category = null;
    private CheckBox checkAll;
    @FXML
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
    public GridPane jeproLabCategoryFormLayout, jeproLabCategoryAssociatedLaboratoriesLayout, jeproLabCategoryAssociatedGroupLayout;
    public TabPane jeproLabCategoryTabPane;
    public Tab jeproLabCategoryInformationTab, jeproLabCategoryAssociatedLaboratoriesTab, jeproLabCategoryAssociatedGroupTab;
    public Tab jeproLabCategorySubCategoriesTab;

    public TableView<JeproLabCategoryController.JeproLabCategoryRecord> jeproLabSubCategoryTableView;
    public TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean> jeproLabSubCategoryCheckBoxColumn;
    public TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean> jeproLabSubCategoryStatusColumn;
    public TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, String> jeproLabSubCategoryDescriptionColumn, jeproLabSubCategoryNameColumn;
    public TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Integer> jeproLabSubCategoryIndexColumn, jeproLabSubCategoryPositionColumn;
    public TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, HBox> jeproLabSubCategoryActionColumn;

    public JeproImageChooser jeproLabCategoryImageChooser;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        formWidth = 2 * (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 15;
        jeproLabCategoryFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabCategoryFormWrapper.setLayoutX(posX);
        jeproLabCategoryFormWrapper.setLayoutY(posY);

        jeproLabCategoryFormContainerWrapper.setLayoutY(40);
        jeproLabCategoryFormContainerWrapper.setPrefWidth(formWidth);

        jeproLabCategoryFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );
        jeproLabCategoryFormLayout.setLayoutY(15);
        jeproLabCategoryTabPane.setPrefWidth(formWidth);
        VBox.setMargin(jeproLabCategoryFormLayout, new Insets(10, 5, 0, 5));

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        formTitleLabel.setAlignment(Pos.CENTER);
        formTitleLabel.getStyleClass().add("form-title");

        jeproLabCategoryFormTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabCategoryInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));


        jeproLabCategoryAssociatedLaboratoriesTab.setText(bundle.getString("JEPROLAB_ASSOCIATED_LABORATORIES_LABEL"));
        jeproLabCategoryAssociatedGroupTab.setText(bundle.getString("JEPROLAB_ASSOCIATED_GROUPS_LABEL"));
        jeproLabAddCategoryFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " +  bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabCategoryNameLabel.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        jeproLabCategoryNameLabel.getStyleClass().add("input-label");
        jeproLabPublishedCategoryLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabPublishedCategoryLabel.getStyleClass().add("input-label");
        jeproLabCategoryParentLabel.setText(bundle.getString("JEPROLAB_PARENT_LABEL"));
        jeproLabCategoryParentLabel.getStyleClass().add("input-label");
        jeproLabCategoryDescriptionLabel.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabCategoryDescriptionLabel.getStyleClass().add("input-label");
        jeproLabCategoryImageChooserLabel.setText(bundle.getString("JEPROLAB_CHOOSE_IMAGE_LABEL"));
        jeproLabCategoryImageChooserLabel.getStyleClass().add("input-label");
        jeproLabCategoryMetaTileLabel.setText(bundle.getString("JEPROLAB_META_TITLE_LABEL"));
        jeproLabCategoryMetaTileLabel.getStyleClass().add("input-label");
        jeproLabCategoryMetaDescriptionLabel.setText(bundle.getString("JEPROLAB_META_DESCRIPTION_LABEL"));
        jeproLabCategoryMetaDescriptionLabel.getStyleClass().add("input-label");
        jeproLabCategoryMetaKeyWordLabel.setText(bundle.getString("JEPROLAB_META_KEYWORD_LABEL"));
        jeproLabCategoryMetaKeyWordLabel.getStyleClass().add("input-label");
        jeproLabCategoryLinkRewriteLabel.setText(bundle.getString("JEPROLAB_LINK_REWRITE_LABEL"));
        jeproLabCategoryLinkRewriteLabel.getStyleClass().add("input-label");
        jeproLabCategoryIsRootLabel.setText(bundle.getString("JEPROLAB_IS_ROOT_LABEL"));
        jeproLabCategoryIsRootLabel.getStyleClass().add("input-label");

        jeproLabCategoryAssociatedLabsLabel.setText(bundle.getString("JEPROLAB_ASSOCIATED_LABORATORIES_LABEL"));
        jeproLabCategoryAssociatedLabsLabel.setPrefSize(formWidth - 20, 25);
        jeproLabCategoryAssociatedLabsLabel.setAlignment(Pos.CENTER);
        jeproLabCategoryAssociatedLabsLabel.getStyleClass().addAll("form-panel-title-gray", "input-label");
        VBox.setMargin(jeproLabCategoryAssociatedLabsLabel, new Insets(0, 10, 0, 10));

        jeproLabCategoryAllowedGroupLabel.setText(bundle.getString("JEPROLAB_ALLOWED_GROUP_LABEL"));
        jeproLabCategoryAllowedGroupLabel.setPrefSize(formWidth - 20, 25);
        jeproLabCategoryAllowedGroupLabel.setAlignment(Pos.CENTER);
        jeproLabCategoryAllowedGroupLabel.getStyleClass().addAll("form-panel-title-gray", "input-label");
        VBox.setMargin(jeproLabCategoryAllowedGroupLabel, new Insets(0, 10, 0, 10));

        jeproLabCategoryLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryLabel.getStyleClass().add("input-label");

        jeproLabCategoryDescription.setTextPrefSize(760, 90);
        /*for(TextArea field : jeproLabCategoryDescription.getFields()){
            field.setWrapText(true);
        }*/

        //GridPane.setMargin(jeproLabCategoryInformationLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryNameLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabPublishedCategoryLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryParentLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryDescriptionLabel, new Insets(5, 0, 15, 10));
        GridPane.setValignment(jeproLabCategoryDescriptionLabel, VPos.TOP);
        GridPane.setValignment(jeproLabCategoryParentLabel, VPos.TOP);
        GridPane.setMargin(jeproLabCategoryParent, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryImageChooserLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryMetaTileLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryMetaDescriptionLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryMetaKeyWordLabel, new Insets(5, 0, 10, 10));
        GridPane.setMargin(jeproLabCategoryLinkRewriteLabel, new Insets(5, 0, 10, 20));
        GridPane.setMargin(jeproLabCategoryIsRootLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryAssociatedLabsLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryAllowedGroupLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryDescription, new Insets(10, 0, 15, 0));
        if(context == null) {
            context = JeproLabContext.getContext();
        }
        context.controller = this;

        initializeSubCategories();
    }

    @Override
    public void initializeContent(){
        int labId = context.laboratory.laboratory_id;
        JeproLabCategoryModel category = this.loadCategory();
        if(category != null){
            int selectedCategoryId;
            if((category.parent_id > 0) && category.isParentCategoryAvailable(labId)){
                selectedCategoryId = category.parent_id;
            }else {
                selectedCategoryId = JeproLab.request.getIntValue("parent_id", JeproLabCategoryModel.getRootCategory().category_id);
            }
            String imagePath = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY +  category.category_id + ".jpg";
            String imageLink = JeproLabImageModel.JeproLabImageManager.thumbNail(imagePath, "category_" + category.category_id + ".jpg", 350, "jpg", true, true);
            File imageFile = new File(imagePath);
            int imageSize = (imageFile.exists() && !imageFile.isDirectory()) ? (int)(imageFile.length()/1000) : 0;

            boolean sharedCategory = ((category.category_id > 0) &&  category.hasMultiLabEntries());

        }

        JeproLabGroupModel unidentifiedGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("unidentified_group"));
        JeproLabGroupModel guestGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("guest_group"));
        JeproLabGroupModel customerGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("customer_group"));

        List<JeproLabGroupModel> groups = JeproLabGroupModel.getGroups(context.language.language_id);
        //JeproCategoryTree categoryTree = new JeproCategoryTree();
        List<Integer> selectedCategories = new ArrayList<>();
        if(category.parent_id > 0 && category.isParentCategoryAvailable(labId)){
            selectedCategories.add(category.parent_id);
        }else{
            int parentId = JeproLab.request.getRequest().containsKey("parent_id") ? Integer.parseInt(JeproLab.request.getRequest().get("parent_id")) : JeproLabCategoryModel.getRootCategory().category_id;
            selectedCategories.add(parentId);
        }
        jeproLabCategoryParent.setTreeTemplate("associated_categories").setSelectedCategories(selectedCategories).setUseCheckBox(true);
        jeproLabCategoryParent.setTreeTitle(bundle.getString("JEPROLAB_PARENT_CATEGORY_LABEL")).setTreeWidth(750).render();

        if(category.category_id > 0){
            formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_LABEL") + " "+ bundle.getString("JEPROLAB_CATEGORY_LABEL"));
            jeproLabCategoryName.setText(category.name);
            //jeproLabCategoryParent.setSelectedCategories(category.parent_id);
            jeproLabCategoryDescription.setText(category.description);
            jeproLabPublishedCategory.setSelected(category.published);
            jeproLabCategoryIsRoot.setSelected(category.is_root_category);

            jeproLabCategoryMetaDescription.setText(category.meta_description);
            jeproLabCategoryMetaKeyWord.setText(category.meta_keywords);
            jeproLabCategoryMetaTile.setText(category.meta_title);
            jeproLabCategoryLinkRewrite.setText(category.link_rewrite);
        }else{
            jeproLabCategoryName.clearFields();
            //jeproLabCategoryParent.setSelectedCategories(category.parent_id);
            jeproLabCategoryDescription.clearFields();
            jeproLabPublishedCategory.setSelected(false);
            jeproLabCategoryIsRoot.setSelected(false);

            jeproLabCategoryMetaDescription.clearFields();
            jeproLabCategoryMetaKeyWord.clearFields();
            jeproLabCategoryMetaTile.clearFields();
            jeproLabCategoryLinkRewrite.clearFields();
        }

        if(!category.is_root_category){
            jeproLabCategoryIsRootLabel.setVisible(false);
            jeproLabCategoryIsRoot.setVisible(false);
        }else{
            jeproLabCategoryIsRoot.setVisible(true);
            jeproLabCategoryIsRootLabel.setVisible(true);
            jeproLabCategoryIsRoot.setSelected(true);
        }

        jeproLabCategoryIsRoot.setDisable(true);
        JeproLab.request.setRequest("category_id=" + category.category_id);
        List<JeproLabCategoryModel> subCategories = JeproLabCategoryModel.getCategories();

        if(!subCategories.isEmpty()){
            ObservableList<JeproLabCategoryController.JeproLabCategoryRecord> categoryList = FXCollections.observableArrayList();
            categoryList.addAll(subCategories.stream().map(JeproLabCategoryController.JeproLabCategoryRecord::new).collect(Collectors.toList()));
            jeproLabSubCategoryTableView.getItems().clear();
            jeproLabSubCategoryTableView.setItems(categoryList);
        }else{
            jeproLabSubCategoryTableView.getItems().clear();
        }
        jeproLabCategoryTabPane.getSelectionModel().select(jeproLabCategoryInformationTab);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveCategoryBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"),new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if(category != null && category.category_id > 0){
            saveCategoryBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        }else {
            category = new JeproLabCategoryModel();
            category.saveCategory();
        }
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"),  new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.setSpacing(4);
        commandWrapper.getChildren().addAll(saveCategoryBtn, cancelBtn);
        addCommandEventListener();
    }

    private void addCommandEventListener(){
        saveCategoryBtn.setOnAction(event -> {
            if(category.category_id > 0){
                category.update();
            }else{
                category.saveCategory();
            }
        });
    }

    private void initializeSubCategories(){
        double remainingWidth = (0.98 * formWidth) - 108;

        jeproLabCategorySubCategoriesTab.setText(bundle.getString("JEPROLAB_SUB_CATEGORIES_LABEL"));
        jeproLabSubCategoryTableView.setPrefWidth(0.98 * formWidth);
        jeproLabSubCategoryTableView.setPrefHeight(520);
        VBox.setMargin(jeproLabSubCategoryTableView, new Insets(0, (0.01 * formWidth), 0, (0.01 * formWidth)));

        jeproLabSubCategoryTableView.setLayoutY(20);
        jeproLabSubCategoryIndexColumn.setText("#");
        jeproLabSubCategoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("categoryIndex"));
        checkAll = new CheckBox();
        jeproLabSubCategoryIndexColumn.setPrefWidth(30);
        jeproLabSubCategoryCheckBoxColumn.setGraphic(checkAll);
        jeproLabSubCategoryCheckBoxColumn.setPrefWidth(25);

        Callback<TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCategoryController.JeproLabCheckBoxCell();
        jeproLabSubCategoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);
        jeproLabSubCategoryStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabSubCategoryStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>> statusCellFactory = param -> new JeproLabCategoryController.JeproLabStatusCell();
        jeproLabSubCategoryStatusColumn.setCellFactory(statusCellFactory);
        jeproLabSubCategoryNameColumn.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        jeproLabSubCategoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        jeproLabSubCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        jeproLabSubCategoryDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabSubCategoryDescriptionColumn.setPrefWidth(0.56 * remainingWidth);
        jeproLabSubCategoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));

        jeproLabSubCategoryPositionColumn.setText(bundle.getString("JEPROLAB_POSITION_LABEL"));
        jeproLabSubCategoryPositionColumn.setPrefWidth(0.1 * remainingWidth);
        tableCellAlign(jeproLabSubCategoryPositionColumn, Pos.CENTER);
        jeproLabSubCategoryPositionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryPosition"));

        jeproLabSubCategoryActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabSubCategoryActionColumn.setPrefWidth(0.09 * remainingWidth);
        Callback<TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, HBox>, TableCell<JeproLabCategoryController.JeproLabCategoryRecord, HBox>> actionFactory = param -> new JeproLabCategoryController.JeproLabActionCell();
        jeproLabSubCategoryActionColumn.setCellFactory(actionFactory);
    }

    private JeproLabCategoryModel loadCategory(){
        int categoryId = JeproLab.request.getRequest().containsKey("category_id") ? Integer.parseInt(JeproLab.request.getRequest().get("category_id")) : 0;
        int isRoot = JeproLab.request.getRequest().containsKey("is_root_category") ? Integer.parseInt(JeproLab.request.getRequest().get("is_root_category")) : 0;
        if(context == null) {
            context = JeproLabContext.getContext();
        }

        JeproLabCategoryModel category;
        if(categoryId > 0){
            category = new JeproLabCategoryModel(categoryId);
            if(category.category_id <= 0){
                return new JeproLabCategoryModel();
            }
            if(isRoot > 0){
                category.is_root_category = true;
            }
            return category;
        }else{
            category = new JeproLabCategoryModel();
            if(isRoot > 0){
                category.is_root_category = true;
            }
            return category;
        }
    }

}
