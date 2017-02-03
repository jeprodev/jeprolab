package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.extend.controls.tree.JeproCategoryTree;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCategoryAddController extends JeproLabController{
    private Button saveCategoryBtn, cancelBtn, laboratorySearchBtn, categorySearchBtn, groupsSearchBtn;
    private TextField laboratorySearchField, categorySearchField, groupsSearchField;
    private ComboBox<String> laboratorySearchFilter, categorySearchFilter, groupsSearchFilter;

    private Pagination laboratoryPagination, groupPagination, subCategoriesPagination;
    private ObservableList<JeproLabLaboratoryController.JeproLabLaboratoryRecord> laboratories;
    private ObservableList<JeproLabGroupController.JeproLabGroupRecord> groupList;
    private ObservableList<JeproLabCategoryController.JeproLabCategoryRecord> categoryList;
    private VBox jeproLabLaboratoryWrapper, jeproLabGroupWrapper, jeproLabSubCategoriesWrapper;
    private HBox jeproLabLaboratoriesSearchWrapper, jeproLabGroupSearchWrapper, jeproLabSubCategoriesSearchWrapper;
    //JeproLabCategoryModel category = null;
    private CheckBox checkAll, laboratoryCheckAll;
    private TableView<JeproLabCategoryController.JeproLabCategoryRecord> jeproLabSubCategoryTableView;
    private TableView<JeproLabLaboratoryController.JeproLabLaboratoryRecord> jeproLabAssociatedLaboratoriesTableView;
    private TableView<JeproLabGroupController.JeproLabGroupRecord> jeproLabAssociatedGroupTableView;
    //private TableView<JeproLabLaboratoryController.JeproLabLaboratoryRecord> jeproLabAssociatedLaboratoriesTableView;

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
    public JeproImageChooser jeproLabCategoryImageChooser;

    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        formWidth = 2 * (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH / 2) - (formWidth) / 2;
        double posY = 15;
        jeproLabCategoryFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabCategoryFormWrapper.setLayoutX(posX);
        jeproLabCategoryFormWrapper.setLayoutY(posY);

        jeproLabCategoryFormContainerWrapper.setLayoutY(40);
        jeproLabCategoryFormContainerWrapper.setPrefWidth(formWidth);

        jeproLabCategoryFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25),
            new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25)
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
        jeproLabAddCategoryFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
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

        /*jeproLabCategoryAssociatedLabsLabel.setText(bundle.getString("JEPROLAB_ASSOCIATED_LABORATORIES_LABEL"));
        jeproLabCategoryAssociatedLabsLabel.setPrefSize(formWidth - 20, 25);
        jeproLabCategoryAssociatedLabsLabel.setAlignment(Pos.CENTER);
        jeproLabCategoryAssociatedLabsLabel.getStyleClass().addAll("form-panel-title-gray", "input-label");
        VBox.setMargin(jeproLabCategoryAssociatedLabsLabel, new Insets(0, 10, 0, 10)); */

        /*jeproLabCategoryAllowedGroupLabel.setText(bundle.getString("JEPROLAB_ALLOWED_GROUP_LABEL"));
        jeproLabCategoryAllowedGroupLabel.setPrefSize(formWidth - 20, 25);
        jeproLabCategoryAllowedGroupLabel.setAlignment(Pos.CENTER);
        jeproLabCategoryAllowedGroupLabel.getStyleClass().addAll("form-panel-title-gray", "input-label");
        VBox.setMargin(jeproLabCategoryAllowedGroupLabel, new Insets(0, 10, 0, 10));*/

        jeproLabCategoryLabel.setText(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabCategoryLabel.getStyleClass().add("input-label");

        jeproLabCategoryDescription.setTextPrefSize(760, 90);

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
        //GridPane.setMargin(jeproLabCategoryAssociatedLabsLabel, new Insets(5, 0, 15, 10));
        //GridPane.setMargin(jeproLabCategoryAllowedGroupLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryDescription, new Insets(10, 0, 15, 0));

        initializeSubCategories();
        initializeAssociatedLaboratoryForm();
        initializeAssociatedGroupsForm();
    }

    private void initializeSubCategories(){
        double remainingWidth = (0.98 * formWidth) - 108;

        jeproLabCategorySubCategoriesTab.setText(bundle.getString("JEPROLAB_SUB_CATEGORIES_LABEL"));
        jeproLabSubCategoriesWrapper = new VBox(5);

        jeproLabSubCategoriesSearchWrapper = new HBox(5);
        categorySearchField = new TextField();
        categorySearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        categorySearchBtn = new Button();
        categorySearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        categorySearchFilter = new ComboBox<>();

        jeproLabSubCategoriesSearchWrapper.getChildren().addAll(categorySearchField, categorySearchFilter, categorySearchBtn);

        jeproLabCategorySubCategoriesTab.setContent(jeproLabSubCategoriesWrapper);

        jeproLabSubCategoryTableView = new TableView<>();
        jeproLabSubCategoryTableView.setPrefWidth(0.98 * formWidth);
        jeproLabSubCategoryTableView.setPrefHeight(520);
        VBox.setMargin(jeproLabSubCategoryTableView, new Insets(0, (0.01 * formWidth), 0, (0.01 * formWidth)));

        jeproLabSubCategoryTableView.setLayoutY(20);
        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Integer>  jeproLabSubCategoryIndexColumn = new TableColumn<>("#");
        jeproLabSubCategoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("categoryIndex"));
        checkAll = new CheckBox();
        jeproLabSubCategoryIndexColumn.setPrefWidth(30);

        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean> jeproLabSubCategoryCheckBoxColumn = new TableColumn<>();
        jeproLabSubCategoryCheckBoxColumn.setGraphic(checkAll);
        jeproLabSubCategoryCheckBoxColumn.setPrefWidth(25);

        Callback<TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCategoryController.JeproLabCheckBoxCell();
        jeproLabSubCategoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>jeproLabSubCategoryStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabSubCategoryStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryController.JeproLabCategoryRecord, Boolean>> statusCellFactory = param -> new JeproLabCategoryController.JeproLabStatusCell();
        jeproLabSubCategoryStatusColumn.setCellFactory(statusCellFactory);

        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, String> jeproLabSubCategoryNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        jeproLabSubCategoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        jeproLabSubCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, String> jeproLabSubCategoryDescriptionColumn = new TableColumn<>(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabSubCategoryDescriptionColumn.setPrefWidth(0.56 * remainingWidth);
        jeproLabSubCategoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));

        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, Integer>  jeproLabSubCategoryPositionColumn = new TableColumn<>(bundle.getString("JEPROLAB_POSITION_LABEL"));
        jeproLabSubCategoryPositionColumn.setPrefWidth(0.1 * remainingWidth);
        tableCellAlign(jeproLabSubCategoryPositionColumn, Pos.CENTER);
        jeproLabSubCategoryPositionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryPosition"));

        TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, HBox> jeproLabSubCategoryActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabSubCategoryActionColumn.setPrefWidth(0.09 * remainingWidth);
        Callback<TableColumn<JeproLabCategoryController.JeproLabCategoryRecord, HBox>, TableCell<JeproLabCategoryController.JeproLabCategoryRecord, HBox>> actionFactory = param -> new JeproLabCategoryController.JeproLabActionCell();
        jeproLabSubCategoryActionColumn.setCellFactory(actionFactory);

        jeproLabSubCategoryTableView.getColumns().addAll(
            jeproLabSubCategoryIndexColumn, jeproLabSubCategoryCheckBoxColumn, jeproLabSubCategoryStatusColumn,
            jeproLabSubCategoryNameColumn, jeproLabSubCategoryDescriptionColumn, jeproLabSubCategoryPositionColumn,
            jeproLabSubCategoryActionColumn
        );
    }

    private void initializeAssociatedLaboratoryForm(){
        jeproLabLaboratoryWrapper = new VBox(5);
        jeproLabLaboratoriesSearchWrapper = new HBox(5);
        jeproLabAssociatedLaboratoriesTableView = new TableView<>();
        jeproLabAssociatedLaboratoriesTableView.setPrefWidth(0.98 * formWidth);
        jeproLabAssociatedLaboratoriesTableView.setPrefHeight(JeproLabConfigurationSettings.LIST_LIMIT * rowHeight);
        laboratoryCheckAll = new CheckBox();
        double remainingWidth = 0;



        laboratorySearchField = new TextField();
        laboratorySearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        laboratorySearchFilter = new ComboBox<>();

        laboratorySearchBtn = new Button();
        laboratorySearchBtn.getStyleClass().addAll("icon-btn", "search-btn");
        jeproLabLaboratoriesSearchWrapper.getChildren().addAll(laboratorySearchField,laboratorySearchFilter, laboratorySearchBtn);




        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Integer> jeproLabLaboratoryIndexColumn = new TableColumn<>("#");
        jeproLabLaboratoryIndexColumn.setPrefWidth(30);
        tableCellAlign(jeproLabLaboratoryIndexColumn, Pos.CENTER_RIGHT);
        jeproLabLaboratoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryIndex"));

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Boolean> jeproLabLaboratoryCheckBoxColumn = new TableColumn<>();
        jeproLabLaboratoryCheckBoxColumn.setGraphic(laboratoryCheckAll);
        jeproLabLaboratoryCheckBoxColumn.setPrefWidth(25);
        Callback<TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Boolean>, TableCell<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabLaboratoryController.JeproLabCheckBoxCellFactory();
        jeproLabLaboratoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Button> jeproLabLaboratoryStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabLaboratoryStatusColumn.setPrefWidth(55);
        tableCellAlign(jeproLabLaboratoryStatusColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Button>, TableCell<JeproLabLaboratoryController.JeproLabLaboratoryRecord, Button>> statusCellFactory = params -> new JeproLabLaboratoryController.JeproLabStatusCellFactory();
        jeproLabLaboratoryStatusColumn.setCellFactory(statusCellFactory);

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, String> jeproLabLaboratoryNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryNameColumn.setPrefWidth(0.32 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryNameColumn, Pos.BOTTOM_LEFT);
        jeproLabLaboratoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryName"));

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, String> jeproLabLaboratoryGroupColumn = new TableColumn<>(bundle.getString("JEPROLAB_GROUP_LABEL"));
        jeproLabLaboratoryGroupColumn.setPrefWidth(0.28 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryGroupColumn, Pos.CENTER_LEFT);
        jeproLabLaboratoryGroupColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryGroup"));

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, String> jeproLabLaboratoryCategoryColumn = new TableColumn<>(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabLaboratoryCategoryColumn.setPrefWidth(0.23 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryCategoryColumn, Pos.CENTER_LEFT);
        jeproLabLaboratoryCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryCategory"));

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, String> jeproLabLaboratoryThemeColumn = new TableColumn<>(bundle.getString("JEPROLAB_THEME_LABEL"));
        jeproLabLaboratoryThemeColumn.setPrefWidth(0.17 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryThemeColumn, Pos.CENTER_LEFT);
        jeproLabLaboratoryThemeColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryTheme"));

        TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, HBox> jeproLabLaboratoryActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabLaboratoryActionColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabLaboratoryController.JeproLabLaboratoryRecord, HBox>, TableCell<JeproLabLaboratoryController.JeproLabLaboratoryRecord, HBox>> actionCellFactory = params -> new JeproLabLaboratoryController.JeproLabActionCellFactory();
        jeproLabLaboratoryActionColumn.setCellFactory(actionCellFactory);

        jeproLabAssociatedLaboratoriesTableView.getColumns().addAll(
            jeproLabLaboratoryIndexColumn, jeproLabLaboratoryCheckBoxColumn, jeproLabLaboratoryStatusColumn, jeproLabLaboratoryNameColumn,
            jeproLabLaboratoryGroupColumn, jeproLabLaboratoryCategoryColumn, jeproLabLaboratoryThemeColumn, jeproLabLaboratoryActionColumn
        );
    }

    private void initializeAssociatedGroupsForm(){
        jeproLabAssociatedGroupTableView = new TableView<>();

        groupsSearchBtn = new Button();
        groupsSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        groupsSearchField = new TextField();
        groupsSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        groupsSearchFilter = new ComboBox<>();

        jeproLabGroupSearchWrapper = new HBox(5);
        jeproLabGroupSearchWrapper.getChildren().addAll(groupsSearchField, groupsSearchFilter, groupsSearchBtn);

        jeproLabGroupWrapper = new VBox(5);
        jeproLabCategoryAssociatedGroupTab.setContent(jeproLabGroupWrapper);

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, Integer> jeproLabAssociatedGroupIndexTableColumn = new TableColumn<>("#");
        jeproLabAssociatedGroupIndexTableColumn.setPrefWidth(30);
        tableCellAlign(jeproLabAssociatedGroupIndexTableColumn, Pos.CENTER_RIGHT);
        jeproLabAssociatedGroupIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupIndex"));

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, Boolean> jeproLabAssociatedGroupCheckBoxTableColumn = new TableColumn<>();
        jeproLabAssociatedGroupCheckBoxTableColumn.setPrefWidth(25);
        tableCellAlign(jeproLabAssociatedGroupCheckBoxTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabGroupController.JeproLabGroupRecord, Boolean>, TableCell<JeproLabGroupController.JeproLabGroupRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabGroupController.JeproLabCheckBoxCellFactory();
        jeproLabAssociatedGroupCheckBoxTableColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, Boolean> jeproLabAssociatedGroupStatusTableColumn = new TableColumn<>();
        jeproLabAssociatedGroupStatusTableColumn.setPrefWidth(55);
        tableCellAlign(jeproLabAssociatedGroupStatusTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabGroupController.JeproLabGroupRecord, Boolean>, TableCell<JeproLabGroupController.JeproLabGroupRecord, Boolean>> statusCellFactory = params -> new JeproLabGroupController.JeproLabStatusCellFactory();
        jeproLabAssociatedGroupStatusTableColumn.setCellFactory(statusCellFactory);

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, String> jeproLabAssociatedGroupNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, String> jeproLabAssociatedGroupReductionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_REDUCTION_LABEL"));
        TableColumn<JeproLabGroupController.JeproLabGroupRecord, Integer> jeproLabAssociatedGroupMembersTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_MEMBERS_LABEL"));
        TableColumn<JeproLabGroupController.JeproLabGroupRecord, Button> jeproLabAssociatedGroupDisplayPriceTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_SHOW_PRICE_LABEL"));
        TableColumn<JeproLabGroupController.JeproLabGroupRecord, String> jeproLabAssociatedGroupCreatedDateTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, HBox> jeproLabAssociatedGroupActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabAssociatedGroupActionTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabGroupController.JeproLabGroupRecord, HBox>, TableCell<JeproLabGroupController.JeproLabGroupRecord, HBox>> actionCellFactory = params -> new JeproLabGroupController.JeproLabActionCellFactory();
        jeproLabAssociatedGroupActionTableColumn.setCellFactory(actionCellFactory);
    }

    @Override
    public void initializeContent(){
        initializeContent(JeproLabSettingModel.getIntValue("root_category"));
    }

    @Override
    public void initializeContent(int categoryId){
        if(context == null) {
            context = JeproLabContext.getContext();
        }
        int labId = context.laboratory.laboratory_id;

        this.loadCategory(categoryId, labId);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveCategoryBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"),new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"),  new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.setSpacing(4);
        commandWrapper.getChildren().addAll(saveCategoryBtn, cancelBtn);
        addCommandEventListener();
    }

    private void addCommandEventListener(){

    }

    private void loadCategory(int categoryId, int labId){
        Worker<Boolean> worker = new Task<Boolean>() {
            JeproLabCategoryModel category;
            List<Integer> selectedCategories;
            @Override
            protected Boolean call() throws Exception {
                category = new JeproLabCategoryModel(categoryId);
                if(categoryId > 0){
                    int selectedCategoryId;
                    if((category.parent_id > 0) && category.isParentCategoryAvailable(labId)){
                        selectedCategoryId = category.parent_id;
                    }else {
                        selectedCategoryId = JeproLabCategoryModel.getRootCategory().category_id;
                    }
                    String imagePath = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY +  category.category_id + ".jpg";
                    String imageLink = JeproLabImageModel.JeproLabImageManager.thumbNail(imagePath, "category_" + category.category_id + ".jpg", 350, "jpg", true, true);
                    File imageFile = new File(imagePath);
                    int imageSize = (imageFile.exists() && !imageFile.isDirectory()) ? (int)(imageFile.length()/1000) : 0;

                    boolean sharedCategory = ((category.category_id > 0) &&  category.hasMultiLabEntries());
                }
                selectedCategories = new ArrayList<>();
                if(category.parent_id > 0 && category.isParentCategoryAvailable(labId)){
                    selectedCategories.add(category.parent_id);
                }else{
                    int parentId = JeproLab.request.getRequest().containsKey("parent_id") ? Integer.parseInt(JeproLab.request.getRequest().get("parent_id")) : JeproLabCategoryModel.getRootCategory().category_id;
                    selectedCategories.add(parentId);
                }
                return null;
            }

            protected void cancelled(){
                super.cancelled();
            }

            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            protected void succeeded(){
                super.succeeded();
                loadAssociatedLaboratories(category);
                //loadAssociatedGroups(category);
                //loadAssociatedSubCategories(category);
                updateInformationTab(category, selectedCategories);
            }
        };
        new Thread((Task)worker).start();
    }

    private void loadAssociatedLaboratories(JeproLabCategoryModel category){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabLaboratoryModel> labs;
            List<Integer> selectedLabs;
            @Override
            protected Boolean call() throws Exception {
                labs = JeproLabLaboratoryModel.getLaboratories(true);
                return null;
            }

            protected void cancelled(){
                super.cancelled();
            }

            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            protected void succeeded(){
                super.succeeded();
                updateLaboratoryTableView(labs, selectedLabs);
            }
        };
        new Thread((Task)worker).start();
    }
/*
    private void loadAssociatedGroups(JeproLabCategoryModel category){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabGroupModel> groups;
            List<Integer> groupIds;
            @Override
            protected Boolean call() throws Exception {
                JeproLabGroupModel unidentifiedGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("unidentified_group"));
                JeproLabGroupModel guestGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("guest_group"));
                JeproLabGroupModel customerGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("customer_group"));

                groups = JeproLabGroupModel.getGroups(context.language.language_id);
                groupIds = new ArrayList<>();
                return null;
            }

            protected void cancelled(){
                super.cancelled();
            }

            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            protected void succeeded(){
                super.succeeded();
                updateGroupsTableView(groups, groupIds);
            }
        };
        new Thread((Task)worker).start();
    }

    private void loadAssociatedSubCategories(JeproLabCategoryModel category){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabCategoryModel> subCategories;
            @Override
            protected Boolean call() throws Exception {
                subCategories = JeproLabCategoryModel.getCategories();
                return null;
            }

            protected void cancelled(){
                super.cancelled();
            }

            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            protected void succeeded(){
                super.succeeded();

            }
        };
        new Thread((Task)worker).start();
    }
*/
    private void updateInformationTab(JeproLabCategoryModel category, List<Integer> selectedCategories){
        Platform.runLater(() -> {
            jeproLabCategoryParent.setTreeTemplate("associated_categories").setSelectedCategories(selectedCategories).setUseCheckBox(true);
            jeproLabCategoryParent.setTreeTitle(bundle.getString("JEPROLAB_PARENT_CATEGORY_LABEL")).setTreeWidth(750).render();
            if(category.category_id > 0){
                formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_LABEL") + " "+ bundle.getString("JEPROLAB_CATEGORY_LABEL"));
                saveCategoryBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
                jeproLabCategoryName.setText(category.name);
                jeproLabCategoryDescription.setText(category.description);
                jeproLabPublishedCategory.setSelected(category.published);
                jeproLabCategoryIsRoot.setSelected(category.is_root_category);

                jeproLabCategoryMetaDescription.setText(category.meta_description);
                jeproLabCategoryMetaKeyWord.setText(category.meta_keywords);
                jeproLabCategoryMetaTile.setText(category.meta_title);
                jeproLabCategoryLinkRewrite.setText(category.link_rewrite);
            }else{
                saveCategoryBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
                jeproLabCategoryName.clearFields();
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
        });

    }

    private void updateLaboratoryTableView(List<JeproLabLaboratoryModel> labs, List<Integer> selectedLabs){
        if(!labs.isEmpty()) {
            laboratories = FXCollections.observableArrayList();
            Platform.runLater(() -> {
                for(JeproLabLaboratoryModel lab : labs) {
                    laboratories.add(new JeproLabLaboratoryController.JeproLabLaboratoryRecord(lab, selectedLabs));
                }
                laboratoryPagination = new Pagination((labs.size()/JeproLabConfigurationSettings.LIST_LIMIT + 1), 0);
                laboratoryPagination.setPageFactory(this::createLaboratoryPages);
                jeproLabLaboratoryWrapper.getChildren().clear();
                jeproLabLaboratoryWrapper.getChildren().addAll(jeproLabLaboratoriesSearchWrapper, laboratoryPagination);
                //VBox.setMargin(jeproLabLaboratoriesSearchWrapper, new Insets(10, 0.01 * formWidth, 5, 0.01 * formWidth));
                //VBox.setMargin(laboratoryPagination, new Insets(10, 0.01 * formWidth, 5, 0.01 * formWidth));
                jeproLabCategoryAssociatedLaboratoriesTab.setContent(jeproLabLaboratoryWrapper);
            });
        }
    }

    private Node createLaboratoryPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (laboratories.size() - 1));
        jeproLabAssociatedLaboratoriesTableView.setItems(FXCollections.observableArrayList(laboratories.subList(fromIndex, toIndex)));
        return new Pane(jeproLabAssociatedLaboratoriesTableView);
    }
/*
    private void updateSubCategoriesTableView(List<JeproLabCategoryModel> cats, List<Integer> selectedCats){
        if(!cats.isEmpty()) {
            categoryList = FXCollections.observableArrayList();
            Platform.runLater(() -> {
                for(JeproLabCategoryModel cat : cats) {
                    categoryList.add(new JeproLabCategoryController.JeproLabCategoryRecord(cat, selectedCats));
                }
                subCategoriesPagination = new Pagination((cats.size()/JeproLabConfigurationSettings.LIST_LIMIT + 1), 0);
                subCategoriesPagination.setPageFactory(this::createSubCategoriesPages);
                jeproLabSubCategoriesWrapper.getChildren().clear();
                jeproLabSubCategoriesWrapper.getChildren().addAll(jeproLabSubCategoriesSearchWrapper, subCategoriesPagination);
            });
        }
    }

    private Node createSubCategoriesPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (laboratories.size() - 1));
        jeproLabAssociatedLaboratoriesTableView.setItems(FXCollections.observableArrayList(laboratories.subList(fromIndex, toIndex)));
        return new Pane(jeproLabAssociatedLaboratoriesTableView);
    }

    private void updateGroupsTableView(List<JeproLabGroupModel> groups, List<Integer> selectedGroups){
        if(!groups.isEmpty()) {
            groupList = FXCollections.observableArrayList();
            Platform.runLater(() -> {
                for(JeproLabGroupModel group : groups) {
                    groupList.add(new JeproLabGroupController.JeproLabGroupRecord(group, selectedGroups));
                }
                groupPagination = new Pagination((groups.size()/JeproLabConfigurationSettings.LIST_LIMIT + 1), 0);
                groupPagination.setPageFactory(this::createGroupPages);
                jeproLabGroupWrapper.getChildren().clear();
                jeproLabGroupWrapper.getChildren().addAll(jeproLabGroupSearchWrapper, groupPagination);
            });
        }
    }

    private Node createGroupPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (laboratories.size() - 1));
        jeproLabAssociatedGroupTableView.setItems(FXCollections.observableArrayList(groupList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabAssociatedGroupTableView);
    } */
}