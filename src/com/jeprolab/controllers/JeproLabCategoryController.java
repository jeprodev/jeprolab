package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabEmployeeModel;
import com.jeprolab.models.JeproLabLaboratoryModel;
import com.jeprolab.models.JeproLabSettingModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCategoryController extends JeproLabController{
    CheckBox checkAll;
    Button addCategoryBtn, addRootCategoryButton;
    private HBox jeproLabCategorySearchWrapper;
    private TextField jeproLabCategorySearchField;
    private ComboBox<String> jeproLabCategorySearchBy;
    private Button jeproLabCategorySearchBtn;
    private Pagination jeproLabCategoryPagination;
    private ObservableList<JeproLabCategoryRecord> categoryList; // = FXCollections.observableArrayList();
    private TableView<JeproLabCategoryRecord> jeproLabCategoryTableView;


    @FXML
    public VBox jeproLabCategoryFormPanel;


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 108;

        jeproLabCategoryTableView = new TableView<>();
        jeproLabCategoryTableView.setPrefWidth(0.98 * JeproLab.APP_WIDTH);
        jeproLabCategoryTableView.setPrefHeight(rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);

        jeproLabCategorySearchWrapper = new HBox(10);

        jeproLabCategorySearchField = new TextField();
        jeproLabCategorySearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabCategorySearchBy = new ComboBox<>();
        //jeproLabCategorySearchBy
        jeproLabCategorySearchBtn = new Button();
        jeproLabCategorySearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabCategorySearchWrapper.getChildren().addAll(jeproLabCategorySearchField, jeproLabCategorySearchBy, jeproLabCategorySearchBtn);

        jeproLabCategoryTableView.setLayoutY(20);
        TableColumn<JeproLabCategoryRecord, Integer> jeproLabCategoryIndexColumn = new TableColumn<>("#");
        jeproLabCategoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("categoryIndex"));
        jeproLabCategoryIndexColumn.setPrefWidth(30);
        tableCellAlign(jeproLabCategoryIndexColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        TableColumn<JeproLabCategoryRecord, Boolean> jeproLabCategoryCheckBoxColumn = new TableColumn<>();
        jeproLabCategoryCheckBoxColumn.setGraphic(checkAll);
        jeproLabCategoryCheckBoxColumn.setPrefWidth(25);
        Callback<TableColumn<JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCategoryCheckBoxCellFactory();
        jeproLabCategoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabCategoryRecord, Boolean> jeproLabCategoryStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabCategoryStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryRecord, Boolean>> statusCellFactory = param -> new JeproLabCategoryStatusCellFactory();
        jeproLabCategoryStatusColumn.setCellFactory(statusCellFactory);

        TableColumn<JeproLabCategoryRecord, String> jeproLabCategoryNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        jeproLabCategoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        jeproLabCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<JeproLabCategoryRecord, String> jeproLabCategoryDescriptionColumn = new TableColumn<>(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabCategoryDescriptionColumn.setPrefWidth(0.6 * remainingWidth);
        jeproLabCategoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));

        TableColumn<JeproLabCategoryRecord, Integer> jeproLabCategoryPositionColumn = new TableColumn<>(bundle.getString("JEPROLAB_POSITION_LABEL"));
        jeproLabCategoryPositionColumn.setPrefWidth(0.06 * remainingWidth);
        tableCellAlign(jeproLabCategoryPositionColumn, Pos.CENTER);
        jeproLabCategoryPositionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryPosition"));

        TableColumn<JeproLabCategoryRecord, HBox> jeproLabCategoryActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabCategoryActionColumn.setPrefWidth(0.09 * remainingWidth);
        Callback<TableColumn<JeproLabCategoryRecord, HBox>, TableCell<JeproLabCategoryRecord, HBox>> actionFactory = param -> new JeproLabCategoryActionCellFactory();
        jeproLabCategoryActionColumn.setCellFactory(actionFactory);

        jeproLabCategoryTableView.getColumns().addAll(
            jeproLabCategoryIndexColumn,
            jeproLabCategoryCheckBoxColumn,
            jeproLabCategoryStatusColumn,
            jeproLabCategoryNameColumn,
            jeproLabCategoryDescriptionColumn,
            jeproLabCategoryPositionColumn,
            jeproLabCategoryActionColumn
        );
    }

    @Override
    public void initializeContent(){
        initializeContent(JeproLabSettingModel.getIntValue("root_category"));
    }

    @Override
    public void initializeContent(int categoryId){
        Worker<Boolean> worker = new Task<Boolean>(){
            JeproLabCategoryModel category = null;
            List<JeproLabCategoryModel> categories;
            @Override
            public Boolean call() throws Exception{
                if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                    this.category = new JeproLabCategoryModel(context.laboratory.category_id);
                    categories = this.category.getSubCategories(context.language.language_id);
                } else if (JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && JeproLabSettingModel.getIntValue("multi_lab_feature_active") > 1 && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() > 1) {
                    categories = JeproLabCategoryModel.getCategoriesWithoutParent();
                } else {
                    this.category = new JeproLabCategoryModel(JeproLabSettingModel.getIntValue("root_category"));
                    categories = this.category.getSubCategories(context.language.language_id);
                }

                /** getting and showing items list ** /
                List<JeproLabCategoryModel> categoryTree ;
                if(!JeproLabLaboratoryModel.isFeaturePublished() || JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && context.category.category_id > 0){
                    JeproLabCategoryModel categoryTr = JeproLabCategoryModel.getTopCategory();
                    categoryTree = new ArrayList<>();
                    categoryTree.add(categoryTr);
                }else{
                    categoryTree = this.category.getParentCategories();
                    JeproLabCategoryModel categoryTr = categoryTree.get(categoryTree.size() - 1);
                    if(!JeproLabLaboratoryModel.isFeaturePublished() && (categoryTr != null && categoryTr.parent_id != 0) ){

                    }
                }*/
                return true;
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                try {
                    renderDetails(categories);
                } catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }
            }

            @Override
            protected void cancelled(){
                super.cancelled();
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }
        };
        new Thread((Task)worker).start();

        updateToolBar();

    }

    private void redirectToCategoryForm(int categoryId){
        Platform.runLater(() -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent(categoryId);
        });
    }


    private void renderDetails(List<JeproLabCategoryModel> categories)throws Exception{
        if(!categories.isEmpty()) {
            categoryList = FXCollections.observableArrayList();
            categoryList.addAll(categories.stream().map(JeproLabCategoryRecord::new).collect(Collectors.toList()));

            Platform.runLater(() -> {
                jeproLabCategoryPagination = new Pagination((categoryList.size() / JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabCategoryPagination.setPageFactory(this::createPages);
                jeproLabCategoryFormPanel.getChildren().clear();
                VBox.setMargin(jeproLabCategorySearchWrapper, new Insets(5, 0.01 * JeproLab.APP_WIDTH, 5, 0.01 * JeproLab.APP_WIDTH));
                VBox.setMargin(jeproLabCategoryPagination, new Insets(0, 0.01 * JeproLab.APP_WIDTH, 5, 0.01 * JeproLab.APP_WIDTH));
                jeproLabCategoryFormPanel.getChildren().addAll(jeproLabCategorySearchWrapper, jeproLabCategoryPagination);
            });
        }
    }

    private Node createPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (categoryList.size()));
        jeproLabCategoryTableView.setItems(FXCollections.observableArrayList(categoryList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabCategoryTableView);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(10);
        addCategoryBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addRootCategoryButton = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ROOT_CATEGORY_LABEL"));
        addRootCategoryButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));

        if(JeproLabContext.getContext().employee.isSuperAdmin()) {
            addRootCategoryButton.setDisable(false);
        }else{
            addRootCategoryButton.setDisable(true);
        }
        commandWrapper.getChildren().addAll(addCategoryBtn, addRootCategoryButton);
        addCommandEventLister();
    }

    private void addCommandEventLister(){
        addCategoryBtn.setOnAction(evt ->{
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent();
        });

        addRootCategoryButton.setOnAction(event -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent();
            ((JeproLabCategoryAddController)(JeproLab.getInstance().getApplicationForms().addCategoryForm.controller)).enableRootCategory(true);
        });
    }


    public static class JeproLabCategoryRecord {
        private SimpleIntegerProperty categoryIndex;
        private SimpleBooleanProperty categorySelected;
        private SimpleBooleanProperty categoryStatus;
        private SimpleStringProperty categoryName;
        private SimpleStringProperty categoryDescription;
        private SimpleIntegerProperty categoryPosition;

        public JeproLabCategoryRecord(JeproLabCategoryModel category) {
            this(category, new ArrayList<>());
        }

        public JeproLabCategoryRecord(JeproLabCategoryModel category, List<Integer> selectedCats){
            int langId = JeproLabContext.getContext().language.language_id;
            JeproLabEmployeeModel employee = JeproLabContext.getContext().employee;
            categoryIndex = new SimpleIntegerProperty(category.category_id);
            categorySelected = new SimpleBooleanProperty(false);
            categoryStatus = new SimpleBooleanProperty(category.published);
            categoryName = new SimpleStringProperty(category.name.get("lang_" + langId));
            categoryDescription = new SimpleStringProperty(category.description.get("lang_" + langId));
            categoryPosition = new SimpleIntegerProperty(category.position);
        }

        /**
         * GETTERS
         **/
        public int getCategoryIndex() {
            return categoryIndex.get();
        }

        public boolean getCategorySelected() {
            return categorySelected.get();
        }

        public boolean getCategoryStatus() {
            return categoryStatus.get();
        }

        public String getCategoryName() {
            return categoryName.get();
        }

        public String getCategoryDescription() {
            return categoryDescription.get();
        }

        public int getCategoryPosition() {
            return categoryPosition.get();
        }

        /**
         * SETTERS
         **/
        public void setCategoryIndex(int index) {
            categoryIndex.set(index);
        }

        public void setCategorySelected(boolean selected) {
            categorySelected.set(selected);
        }

        public void setCategoryStatus(boolean status) {
            categoryStatus.set(status);
        }

        public void setCategoryName(String name) {
            categoryName.set(name);
        }

        public void setCategoryDescription(String description) {
            categoryDescription.set(description);
        }

        public void setCategoryPosition(int position) {
            categoryPosition.set(position);
        }

        public static class JeproLabStatusCellFactory extends TableCell<JeproLabCategoryRecord, Boolean> {
            private Button statusButton;

            public JeproLabStatusCellFactory() {
                statusButton = new Button("");
                statusButton.getStyleClass().add("icon-btn");
                statusButton.setMinSize(18, 18);
            }

            @Override
            public void commitEdit(Boolean it) {
                super.commitEdit(it);
                //final ObservableList<JeproLabCategory> items = this.getTableView().getItems();

            }

            @Override
            public void updateItem(Boolean it, boolean empty) {
                super.updateItem(it, empty);
                final ObservableList<JeproLabCategoryRecord> items = this.getTableView().getItems();
                if ((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                    if (items.get(getIndex()).getCategoryStatus()) {
                        statusButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                    } else {
                        statusButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                    }
                    setGraphic(statusButton);
                    setAlignment(Pos.CENTER);
                    statusButton.setOnMouseClicked(evt -> {
                        JeproLabCategoryModel cat = new JeproLabCategoryModel(items.get(getIndex()).getCategoryIndex());
                        cat.toggleStatus();

                        items.get(getIndex()).setCategoryStatus(cat.published);
                        if (items.get(getIndex()).getCategoryStatus()) {
                            statusButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                        } else {
                            statusButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                        }
                    });
                }
            }
        }
    }


    public static class JeproLabCategoryCheckBoxCellFactory extends TableCell<JeproLabCategoryRecord, Boolean> {
        private CheckBox checkBox;

        public JeproLabCategoryCheckBoxCellFactory() {
            checkBox = new CheckBox();
            checkBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    commitEdit(checkBox.isSelected());
                }
            });
        }

        @Override
        public void commitEdit(Boolean it) {
            super.commitEdit(it);
            /*final ObservableList<JeproLabCategoryRecord> items = this.getTableView().getItems();
            items.get(getIndex()).setCategorySelected(it);
            /*for(int i = 0; i < items.size(); i++){
                if(i == getIndex())
            }*/
        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            final ObservableList<JeproLabCategoryRecord> items = this.getTableView().getItems();
            if ((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                setGraphic(checkBox);
            }
            //rowHeight = getTableRow().getHeight();
        }
    }


    public static class JeproLabCategoryActionCellFactory extends TableCell<JeproLabCategoryRecord, HBox> {
        private HBox commandContainer;
        private Button editCategory, deleteCategory;

        JeproLabCategoryActionCellFactory() {
            commandContainer = new HBox(10);
            final int btnSize = 20;
            editCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editCategory.setPrefSize(btnSize, btnSize);
            editCategory.setMaxSize(btnSize, btnSize);
            editCategory.setMinSize(btnSize, btnSize);
            editCategory.getStyleClass().add("icon-btn");

            deleteCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteCategory.setPrefSize(btnSize, btnSize);
            deleteCategory.setMaxSize(btnSize, btnSize);
            deleteCategory.setMinSize(btnSize, btnSize);
            deleteCategory.getStyleClass().add("icon-btn");

            commandContainer.getChildren().addAll(editCategory, deleteCategory);
        }

        @Override
        public void commitEdit(HBox t) {
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty) {
            super.updateItem(item, empty);
            final ObservableList<JeproLabCategoryRecord> items = getTableView().getItems();
            if ((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                int categoryId = items.get(getIndex()).getCategoryIndex();
                editCategory.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
                    JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent(categoryId);
                });

                deleteCategory.setOnMouseClicked(event -> {
                    //if(JeproLabCategoryModel.staticDelete(categoryId)){

                    //}
                    JeproLabTools.displayWarning(300, JeproLab.getBundle().getString("JEPROLAB_DELETE_CATEGORY_LABEL"));
                    JeproLabCategoryModel cat = new JeproLabCategoryModel(categoryId);
                    if (cat.delete()) {
                        items.remove(items.get(getIndex()));
                        getTableView().getItems().remove(getIndex());
                    }
                });
                commandContainer.setAlignment(Pos.CENTER);
                setGraphic(commandContainer);
            }
        }
    }

    public static class JeproLabCategoryStatusCellFactory extends TableCell<JeproLabCategoryRecord, Boolean>{
        private Button categoryStatusBtn;

        public JeproLabCategoryStatusCellFactory(){
            categoryStatusBtn = new Button();
            categoryStatusBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Boolean b){
            super.commitEdit(b);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabCategoryRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getCategoryStatus()) {
                    categoryStatusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                } else {
                    categoryStatusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(categoryStatusBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

}
