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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    JeproLabCategoryModel category;
    List<JeproLabCategoryModel> categories;

    @FXML
    public TableView<JeproLabCategoryRecord> categoryTableView;
    public TableColumn<JeproLabCategoryRecord, Boolean> categoryCheckBoxColumn;
    public TableColumn<JeproLabCategoryRecord, Boolean> categoryStatusColumn;
    public TableColumn<JeproLabCategoryRecord, String> categoryDescriptionColumn, categoryNameColumn;
    public TableColumn<JeproLabCategoryRecord, Integer> categoryIndexColumn, categoryPositionColumn;
    public TableColumn<JeproLabCategoryRecord, HBox> categoryActionColumn;
    public Pagination pagination;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 108;
        categoryTableView.setPrefWidth(0.98 * JeproLab.APP_WIDTH);
        categoryTableView.setPrefHeight(rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);
        VBox.setMargin(categoryTableView, new Insets(0, 0, 0, (0.01 * JeproLab.APP_WIDTH)));

        categoryTableView.setLayoutY(20);
        categoryIndexColumn.setText("#");
        categoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("categoryIndex"));
        checkAll = new CheckBox();
        categoryIndexColumn.setPrefWidth(30);
        categoryCheckBoxColumn.setGraphic(checkAll);
        categoryCheckBoxColumn.setPrefWidth(25);

        Callback<TableColumn<JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCheckBoxCell();
        categoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);
        categoryStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        categoryStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabCategoryRecord, Boolean>, TableCell<JeproLabCategoryRecord, Boolean>> statusCellFactory = param -> new JeproLabStatusCell();
        categoryStatusColumn.setCellFactory(statusCellFactory);
        categoryNameColumn.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        categoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        categoryDescriptionColumn.setPrefWidth(0.6 * remainingWidth);

        categoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));
        categoryPositionColumn.setText(bundle.getString("JEPROLAB_POSITION_LABEL"));
        categoryPositionColumn.setPrefWidth(0.06 * remainingWidth);
        tableCellAlign(categoryPositionColumn, Pos.CENTER);
        categoryPositionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryPosition"));

        categoryActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        categoryActionColumn.setPrefWidth(0.09 * remainingWidth);
        Callback<TableColumn<JeproLabCategoryRecord, HBox>, TableCell<JeproLabCategoryRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        categoryActionColumn.setCellFactory(actionFactory);
    }

    @Override
    public void initializeContent(){
        int categoryId = JeproLab.request.getIntValue("category_id");
        String task = JeproLab.request.getValue("task");
        System.out.println("category_id " + categoryId);
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
            if (category != null && !category.isAssociatedToLaboratory() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT) {
                task = "edit";
                this.context.category = category;
                /*try {
                    //JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
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
            JeproLab.request.setRequest("view=category&task=edit&category_id=" + context.laboratory.getCategoryId() + "&" + JeproLabTools.getCategoryToken() + "=1");
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
        //categories = this.category.getSubCategories(context.language.language_id);
        renderDetails();
        pagination = JeproLabCategoryModel.getPagination();
        updateToolBar();

    }


    protected void renderDetails(){
        if(!categories.isEmpty()){
            ObservableList<JeproLabCategoryRecord> categoryList = FXCollections.observableArrayList();
            categoryList.addAll(categories.stream().map(JeproLabCategoryRecord::new).collect(Collectors.toList()));
            categoryTableView.getItems().clear();
            categoryTableView.setItems(categoryList);
        }
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
            //try {
                JeproLab.request.getRequest().clear();
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
                JeproLabContext.getContext().controller.initializeContent();
            /*}catch (IOException ignored){
                ignored.printStackTrace();
            } */
        });

        addRootCategoryButton.setOnAction(event -> {
            //try {
                JeproLab.request.getRequest().clear();
                JeproLab.request.setRequest("is_root_category=1");
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
                JeproLabContext.getContext().controller.initializeContent();
            /*}catch (IOException ignored){
                ignored.printStackTrace();
            }*/
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
