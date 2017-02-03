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
        VBox.setMargin(jeproLabCategoryTableView, new Insets(0, 0, 0, (0.01 * JeproLab.APP_WIDTH)));

        jeproLabCategorySearchWrapper = new HBox();

        jeproLabCategoryTableView.setLayoutY(20);
        TableColumn<JeproLabCategoryRecord, Integer> jeproLabCategoryIndexColumn = new TableColumn<>("#");
        jeproLabCategoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("categoryIndex"));
        checkAll = new CheckBox();
        jeproLabCategoryIndexColumn.setPrefWidth(30);

        TableColumn<JeproLabCategoryRecord, Boolean> jeproLabCategoryCheckBoxColumn = new TableColumn<>();
        jeproLabCategoryCheckBoxColumn.setGraphic(checkAll);
        jeproLabCategoryCheckBoxColumn.setPrefWidth(25);
        Callback<TableColumn<JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCheckBoxCell();
        jeproLabCategoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabCategoryRecord, Boolean> jeproLabCategoryStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabCategoryStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryRecord, Boolean>> statusCellFactory = param -> new JeproLabStatusCell();
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
        Callback<TableColumn<JeproLabCategoryRecord, HBox>, TableCell<JeproLabCategoryRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
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
        initializeContent(0);
    }

    @Override
    public void initializeContent(int categoryId){
        Worker<Boolean> worker = new Task<Boolean>(){
            JeproLabCategoryModel category;
            List<JeproLabCategoryModel> categories;
            @Override
            public Boolean call() throws Exception{
                String task = JeproLab.request.getValue("task");
                if (context.category != null && !task.equals("delete")) {
                    this.category = context.category;
                } else {
                    if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                        this.category = new JeproLabCategoryModel(context.laboratory.category_id);
                    } else if (JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1 && JeproLabSettingModel.getIntValue("multi_lab_feature_active") > 1 && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() > 1) {
                        this.category = JeproLabCategoryModel.getTopCategory();
                    } else {
                        this.category = new JeproLabCategoryModel(JeproLabSettingModel.getIntValue("root_category"));
                    }
                }

                if(!isInitialized){
                    isInitialized = true;
                    if (category != null && category.category_id > 0 && !category.isAssociatedToLaboratory() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                        context.category = category;
                        redirectToCategoryForm(category.category_id);
                        return cancel();
                    }
                }

                if(categoryId > 0){
                    this.category = new JeproLabCategoryModel(categoryId);
                }else{
                    if(JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                        this.category = new JeproLabCategoryModel(context.laboratory.category_id);
                    }else if((JeproLabCategoryModel.getCategoriesWithoutParent().size() > 1) && (JeproLabSettingModel.getIntValue("multi_lab_feature_active") > 0) && JeproLabLaboratoryModel.getLaboratories(true, 0, true).size() != 1){
                        this.category = JeproLabCategoryModel.getTopCategory();
                    }else{
                        this.category = new JeproLabCategoryModel(JeproLabSettingModel.getIntValue("root_category"));
                    }
                }

                if(category.category_id > 0 && !category.isAssociatedToLaboratory() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT){
                    redirectToCategoryForm(context.laboratory.getCategoryId());
                    return cancel();
                }

                /** getting and showing items list **/
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
                }
                categories = JeproLabCategoryModel.getCategories();
                return true;
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                renderDetails(categories);
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

        /*if (context.category != null && !task.equals("delete")) {
            this.category = context.category;
        } else {

        }*/

        updateToolBar();

    }

    private void redirectToCategoryForm(int categoryId){
        Platform.runLater(() -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent(categoryId);
        });
    }


    protected void renderDetails(List<JeproLabCategoryModel> categories){
        Platform.runLater(() ->{
            if(!categories.isEmpty()){
                categoryList.addAll(categories.stream().map(JeproLabCategoryRecord::new).collect(Collectors.toList()));
                jeproLabCategoryPagination = new Pagination((categoryList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabCategoryPagination.setPageFactory(this::createPages);
                jeproLabCategoryFormPanel.getChildren().clear();
                jeproLabCategoryFormPanel.getChildren().addAll(jeproLabCategorySearchWrapper, jeproLabCategoryPagination);
                //jeproLabCategoryFormPanel.getChildren().addAll(container);
                //categoryTableView.getItems().clear();
                //categoryTableView.setItems(categoryList);
                //jeproLabcategoryTableView
            }
        });
    }

    private Node createPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (categoryList.size() - 1));
        jeproLabCategoryTableView.setItems(FXCollections.observableArrayList(categoryList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabCategoryTableView);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(10);
        addCategoryBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addRootCategoryButton = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ROOT_CATEGORY_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));

        commandWrapper.getChildren().addAll(addCategoryBtn, addRootCategoryButton);
        addCommandEventLister();
    }

    private void addCommandEventLister(){
        addCategoryBtn.setOnAction(evt ->{
            JeproLab.request.getRequest().clear();
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLabContext.getContext().controller.initializeContent();
        });

        addRootCategoryButton.setOnAction(event -> {
            JeproLab.request.getRequest().clear();
            JeproLab.request.setRequest("is_root_category=1");
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLabContext.getContext().controller.initializeContent();
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
            int langId = JeproLabContext.getContext().language.language_id;
            JeproLabEmployeeModel employee = JeproLabContext.getContext().employee;
            categoryIndex = new SimpleIntegerProperty(category.category_id);
            categorySelected = new SimpleBooleanProperty(false);
            categoryStatus = new SimpleBooleanProperty(category.published);
            categoryName = new SimpleStringProperty(category.name.get("lang_" + langId));
            categoryDescription = new SimpleStringProperty(category.description.get("lang_" + langId));
            categoryPosition = new SimpleIntegerProperty(category.position);
        }

        public JeproLabCategoryRecord(JeproLabCategoryModel category, List<Integer> selectedCats){

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

        public static class JeproLabStatusCell extends TableCell<JeproLabCategoryRecord, Boolean> {
            private Button statusButton;

            public JeproLabStatusCell() {
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


    public static class JeproLabCheckBoxCell extends TableCell<JeproLabCategoryRecord, Boolean> {
        private CheckBox checkBox;

        public JeproLabCheckBoxCell() {
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
            final ObservableList<JeproLabCategoryRecord> items = this.getTableView().getItems();
            items.get(getIndex()).setCategorySelected(it);
            /*for(int i = 0; i < items.size(); i++){
                if(i == getIndex())
            }*/
        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            final ObservableList<JeproLabCategoryRecord> items = this.getTableView().getItems();
            if ((items != null) && (getIndex() < items.size())) {
                setGraphic(checkBox);
            }
            //rowHeight = getTableRow().getHeight();
        }
    }


    public static class JeproLabActionCell extends TableCell<JeproLabCategoryRecord, HBox> {
        private HBox commandContainer;
        private Button editCategory, deleteCategory;

        JeproLabActionCell() {
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

            /*viewCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/view.png"))));
            viewCategory.setPrefSize(btnSize, btnSize);
            viewCategory.setMaxSize(btnSize, btnSize);
            viewCategory.setMinSize(btnSize, btnSize);
            viewCategory.getStyleClass().add("icon-btn"); */

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
                    JeproLab.request.setRequest("category_id=" + categoryId + "&task=edit&" + JeproLabTools.getCountryToken() + "=1");
                    //try {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
                    JeproLabContext.getContext().controller.initializeContent();
                        /*} catch (IOException ignored) {
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }*/
                });

                /*viewCategory.setOnAction(event -> {

                    System.out.println("editit function");
                    System.out.println("editit function");
                });
                viewCategory.setOnMouseEntered(event -> {

                    //tooltip.setText(bundle.getString("JEPROLAB_VIEW_CATEGORY_MESSAGE"));
                    System.out.println("editit function");
                }); */
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
                /*deleteCategory.setOnMouseEntered(event -> {
                    //tooltip.setText(bundle.getString("JEPROLAB_DELETE_CATEGORY_MESSAGE"));
                    System.out.println("editit mouse enter function");
                });*/
                commandContainer.setAlignment(Pos.CENTER);
                setGraphic(commandContainer);
            }
        }
    }

    public static class JeproLabStatusCell extends TableCell<JeproLabCategoryRecord, Boolean>{
        @Override
        public void commitEdit(Boolean b){
            super.commitEdit(b);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){

        }
    }

}
