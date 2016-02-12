package com.jeprolab.controllers;


import com.jeprolab.JeproLab;

import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabEmployeeModel;
import com.jeprolab.models.JeproLabLaboratoryModel;
import com.jeprolab.models.JeproLabSettingModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sun.plugin2.jvm.RemoteJVMLauncher;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JeproLabCategoryController extends JeproLabController {
    CheckBox checkAll;
    Button addCategoryBtn;
    JeproLabCategoryModel category;
    List<JeproLabCategoryModel> categories;

    @FXML
    public TableView<JeproLabCategory> categoryTableView;
    public TableColumn categoryIndexColumn, categoryCheckBoxColumn, categoryStatusColumn, categoryNameColumn;
    public TableColumn categoryDescriptionColumn, categoryPositionColumn, categoryActionColumn;
    public Pagination pagination;


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

        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 105;
        categoryTableView.setPrefWidth(0.98 * JeproLab.APP_WIDTH);
        categoryTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        categoryTableView.setLayoutY(20);
        categoryIndexColumn.setText("#");
        categoryIndexColumn.setCellValueFactory(new PropertyValueFactory<JeproLabCategory, Integer>("categoryIndex"));
        checkAll = new CheckBox();
        categoryIndexColumn.setPrefWidth(30);
        categoryCheckBoxColumn.setGraphic(checkAll);
        categoryCheckBoxColumn.setPrefWidth(25);
        Callback<TableColumn, TableCell> checkBoxCellFactory = param -> new JeproLabCategoryCheckBoxCell();
        categoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);
        categoryStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        categoryStatusColumn.setPrefWidth(50);
        Callback<TableColumn, TableCell> statusCellFactory = param -> new JeproLabCategoryStatusCell();
        categoryStatusColumn.setCellFactory(statusCellFactory);
        categoryNameColumn.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        categoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<JeproLabCategory, String>("categoryName"));
        categoryDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        categoryDescriptionColumn.setPrefWidth(0.6 * remainingWidth);
        categoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<JeproLabCategory, String>("categoryDescription"));
        categoryPositionColumn.setText(bundle.getString("JEPROLAB_POSITION_LABEL"));
        categoryPositionColumn.setPrefWidth(0.06 * remainingWidth);
        categoryPositionColumn.setCellValueFactory(new PropertyValueFactory<JeproLabCategory, Integer>("categoryPosition"));
        categoryActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        categoryActionColumn.setPrefWidth(0.09 * remainingWidth);
        Callback<TableColumn, TableCell> actionFactory = param -> new JeproLabCategoryActionCell();
        categoryActionColumn.setCellFactory(actionFactory);

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
        categories = categoryModel.getCategoriesList();
        renderDetails();
        pagination = categoryModel.getPagination();
    }

    @Override
    protected void renderDetails(){
        if(categories.isEmpty()){

        }else{
            ObservableList categoryList = FXCollections.observableArrayList();
            for(JeproLabCategoryModel category : categories) {
                categoryList.add(new JeproLabCategory(category));
            }
            categoryTableView.setItems(categoryList);
        }
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addCategoryBtn = new Button(bundle.getString("JEPROLAB_ADD_LABEL"));
        commandWrapper.getChildren().addAll(addCategoryBtn);
    }

    public static class JeproLabCategory {
        private final SimpleIntegerProperty categoryIndex;
        private SimpleBooleanProperty categorySelected;
        //private SimpleBooleanProperty categoryStatus;
        private SimpleStringProperty categoryName;
        private SimpleStringProperty categoryDescription;
        private SimpleIntegerProperty categoryPosition;
        /*private SimpleObjectProperty<HBox> categoryActions;
        private Button editCategory, deleteCategory, viewCategory; */

        public JeproLabCategory(JeproLabCategoryModel category){
            int langId = JeproLabContext.getContext().language.language_id;
            JeproLabEmployeeModel employee = JeproLabContext.getContext().employee;
            categoryIndex = new SimpleIntegerProperty(category.category_id);
            categorySelected = new SimpleBooleanProperty(false);
            //categoryStatus = new SimpleBooleanProperty(category.published);
            categoryName = new SimpleStringProperty(category.name.get("lang_" + langId));
            categoryDescription = new SimpleStringProperty(category.description.get("lang_" + langId));
            categoryPosition = new SimpleIntegerProperty(category.position);
            //categoryActions = new SimpleObjectProperty(new HBox(3));
            /* /if(employee.canEdit("category", category.category_id)){
                editCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
                deleteCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
                //categoryActions
            //}
            viewCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/view.png"))));
*/
            addListener();
        }

        /** GETTERS **/
        public int getCategoryIndex(){
            return categoryIndex.get();
        }

        public boolean getCategorySelected(){
            return categorySelected.get();
        }

        /*public boolean getCategoryStatus(){
            return categoryStatus.get();
        }*/

        public String getCategoryName(){
            return categoryName.get();
        }

        public String getCategoryDescription(){
            return categoryDescription.get();
        }

        public int getCategoryPosition(){
            return categoryPosition.get();
        }

        /** SETTERS **/
        public void setCategoryIndex(int index){
            categoryIndex.set(index);
        }

        public void setCategorySelected(boolean selected){
            categorySelected.set(selected);
        }

        /*public void setCategoryStatus(boolean status){
            categoryStatus.set(status);
        }*/

        public void setCategoryName(String name){
            categoryName.set(name);
        }

        public void setCategoryDescription(String description){
            categoryDescription.set(description);
        }

        public void setCategoryPosition(int position){
            categoryPosition.set(position);
        }

        private void addListener(){

        }
    }

    public class JeproLabCategoryActionCell extends TableCell<JeproLabCategory, HBox>{
        private HBox commandContainer;
        private Button editCategory, deleteCategory, viewCategory;

        public JeproLabCategoryActionCell(){
            commandContainer = new HBox(3);
            final int btnSize = 22;
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
            viewCategory = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/view.png"))));
            viewCategory.setPrefSize(btnSize, btnSize);
            viewCategory.setMaxSize(btnSize, btnSize);
            viewCategory.setMinSize(btnSize, btnSize);
            viewCategory.getStyleClass().add("icon-btn");
            commandContainer.getChildren().addAll(editCategory, viewCategory, deleteCategory);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
            final ObservableList<JeproLabCategory> items = getTableView().getItems();

        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabCategory> items = getTableView().getItems();
            if((items != null) && (getIndex() < items.size())){
                setGraphic(commandContainer);
            }
        }
    }

    public class JeproLabCategoryCheckBoxCell extends TableCell<JeproLabCategory, Boolean>{
        private CheckBox checkBox;

        public JeproLabCategoryCheckBoxCell(){
            checkBox = new CheckBox();
            checkBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(!newValue){
                    commitEdit(checkBox.isSelected());
                }
            });
        }

        @Override
        public void commitEdit(Boolean it){
            super.commitEdit(it);
            final ObservableList<JeproLabCategory> items = this.getTableView().getItems();
            items.get(getIndex()).setCategorySelected(it);
            /*for(int i = 0; i < items.size(); i++){
                if(i == getIndex())
            }*/
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabCategory> items = this.getTableView().getItems();
            if((items != null) && (getIndex() < items.size())){
                setGraphic(checkBox);
            }
        }
    }

    public class JeproLabCategoryStatusCell extends TableCell<JeproLabCategory, Boolean>{
        private Button statusButton;

        public JeproLabCategoryStatusCell(){
            statusButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
            statusButton.getStyleClass().add("icon-btn");
            statusButton.setMinSize(22, 22);
            //statusButton.focusedProperty().addListener();
        }

        @Override
        public void commitEdit(Boolean it){
            super.commitEdit(it);
            final ObservableList<JeproLabCategory> items = this.getTableView().getItems();

        }

        @Override
        public void updateItem(Boolean it, boolean empty){
            super.updateItem(it, empty);
            final ObservableList<JeproLabCategory> items = this.getTableView().getItems();
            if((items != null) && (getIndex() < items.size())){
                setGraphic(statusButton);
            }
        }
    }
}