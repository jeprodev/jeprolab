package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabLaboratoryModel;
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
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.log4j.Level;

import javax.swing.text.StyledEditorKit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabLaboratoryController extends JeproLabController {
    private CheckBox checkAll;
    private TableView<JeproLabLaboratoryRecord> jeproLabLaboratoryTableView;
    private ObservableList<JeproLabLaboratoryRecord> jeproLabLaboratoryList;
    private Pagination jeproLabLaboratoryPagination;
    private HBox jeproLabLaboratoriesSearchWrapper;
    private TextField jeproLabLaboratorySearchField;
    private ComboBox jeproLabLaboratorySearchBy;
    private Button jeproLabLaboratorySearchBtn;

    @FXML
    public VBox jeproLabLaboratoryWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = (0.98 * JeproLab.APP_WIDTH);
        double remainingWidth = formWidth - 197;

        jeproLabLaboratoriesSearchWrapper = new HBox(5);
        jeproLabLaboratorySearchField = new TextField();
        jeproLabLaboratorySearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));
        
        jeproLabLaboratorySearchBy = new ComboBox();
        jeproLabLaboratorySearchBy.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));
        jeproLabLaboratorySearchBtn = new Button();
        jeproLabLaboratorySearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabLaboratoriesSearchWrapper.getChildren().addAll(
            jeproLabLaboratorySearchField, jeproLabLaboratorySearchBy, jeproLabLaboratorySearchBtn
        );

        jeproLabLaboratoryTableView = new TableView<>();
        jeproLabLaboratoryTableView.setPrefWidth(formWidth);
        jeproLabLaboratoryTableView.setPrefHeight(rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);
        checkAll = new CheckBox();

        TableColumn<JeproLabLaboratoryRecord, Integer> jeproLabLaboratoryIndexColumn = new TableColumn<>("#");
        jeproLabLaboratoryIndexColumn.setPrefWidth(30);
        tableCellAlign(jeproLabLaboratoryIndexColumn, Pos.CENTER_RIGHT);
        jeproLabLaboratoryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryIndex"));

        TableColumn<JeproLabLaboratoryRecord, Boolean> jeproLabLaboratoryCheckBoxColumn = new TableColumn<>();
        jeproLabLaboratoryCheckBoxColumn.setGraphic(checkAll);
        jeproLabLaboratoryCheckBoxColumn.setPrefWidth(25);
        tableCellAlign(jeproLabLaboratoryCheckBoxColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabLaboratoryRecord, Boolean>, TableCell<JeproLabLaboratoryRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCheckBoxCellFactory();
        jeproLabLaboratoryCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabLaboratoryRecord, String> jeproLabLaboratoryNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryNameColumn.setPrefWidth(0.3 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryNameColumn, Pos.CENTER_LEFT);
        jeproLabLaboratoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryName"));

        TableColumn<JeproLabLaboratoryRecord, String> jeproLabLaboratoryGroupColumn = new TableColumn<>(bundle.getString("JEPROLAB_LABORATORY_GROUP_LABEL"));
        jeproLabLaboratoryGroupColumn.setPrefWidth(0.25 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryGroupColumn, Pos.CENTER_LEFT);
        jeproLabLaboratoryGroupColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryGroup"));

        TableColumn<JeproLabLaboratoryRecord, String> jeproLabLaboratoryCategoryColumn = new TableColumn<>(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabLaboratoryCategoryColumn.setPrefWidth(0.25 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryCategoryColumn, Pos.CENTER);
        jeproLabLaboratoryCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryCategory"));

        TableColumn<JeproLabLaboratoryRecord, String> jeproLabLaboratoryThemeColumn = new TableColumn<>(bundle.getString("JEPROLAB_THEME_LABEL"));
        jeproLabLaboratoryThemeColumn.setPrefWidth(0.2 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryThemeColumn, Pos.CENTER);
        jeproLabLaboratoryThemeColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryTheme"));

        TableColumn<JeproLabLaboratoryRecord, Button> jeproLabLaboratoryPublishedColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabLaboratoryPublishedColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabLaboratoryRecord, Button>, TableCell<JeproLabLaboratoryRecord, Button>> statusCellFactory = params -> new JeproLabStatusCellFactory();
        jeproLabLaboratoryPublishedColumn.setCellFactory(statusCellFactory);

        TableColumn<JeproLabLaboratoryRecord, HBox> jeproLabLaboratoryActionsColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabLaboratoryActionsColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabLaboratoryRecord, HBox>, TableCell<JeproLabLaboratoryRecord, HBox>> actionsCellFactory = params -> new JeproLabActionCellFactory();
        jeproLabLaboratoryActionsColumn.setCellFactory(actionsCellFactory);

        jeproLabLaboratoryTableView.getColumns().addAll(
            jeproLabLaboratoryIndexColumn, jeproLabLaboratoryCheckBoxColumn, jeproLabLaboratoryPublishedColumn,
            jeproLabLaboratoryNameColumn, jeproLabLaboratoryGroupColumn, jeproLabLaboratoryCategoryColumn,
            jeproLabLaboratoryThemeColumn, jeproLabLaboratoryActionsColumn
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>(){
            List<JeproLabLaboratoryModel> laboratories;
            @Override
            protected Boolean call() throws Exception {
                laboratories = JeproLabLaboratoryModel.getLaboratories();
                return true;
            }

            @Override
            protected void cancelled(){ super.cancelled(); }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateTableView(laboratories);
            }
        };
        new Thread((Task)worker).start();
    }

    private void updateTableView(List<JeproLabLaboratoryModel> laboratories){
        if(!laboratories.isEmpty()){
            jeproLabLaboratoryList = FXCollections.observableArrayList();
            Platform.runLater(() -> {
                for(JeproLabLaboratoryModel lab : laboratories){
                    jeproLabLaboratoryList.add(new JeproLabLaboratoryRecord(lab));
                }
                jeproLabLaboratoryPagination = new Pagination((jeproLabLaboratoryList.size()/NUMBER_OF_LINES) + 1, 0);
                jeproLabLaboratoryPagination.setPageFactory(this::createLaboratoryPages);
                jeproLabLaboratoryWrapper.getChildren().clear();
                jeproLabLaboratoryWrapper.getChildren().addAll(jeproLabLaboratoriesSearchWrapper, jeproLabLaboratoryPagination);
                VBox.setMargin(jeproLabLaboratoriesSearchWrapper, new Insets(10, 0.01 * formWidth, 5, 0.01 * formWidth));
                VBox.setMargin(jeproLabLaboratoryPagination, new Insets(10, 0.01 * formWidth, 5, 0.01 * formWidth));

            });
        }
    }

    private Node createLaboratoryPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + NUMBER_OF_LINES, (jeproLabLaboratoryList.size()));
        jeproLabLaboratoryTableView.setItems(FXCollections.observableArrayList(jeproLabLaboratoryList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabLaboratoryTableView);
    }


    public static class JeproLabLaboratoryRecord{
        private SimpleIntegerProperty laboratoryIndex;
        private SimpleStringProperty laboratoryName, laboratoryCategory, laboratoryGroup, laboratoryTheme;
        private SimpleBooleanProperty laboratorySelected;
        private SimpleBooleanProperty laboratoryStatus;

        public JeproLabLaboratoryRecord(JeproLabLaboratoryModel laboratory){
            this(laboratory, new ArrayList<>());
        }

        public JeproLabLaboratoryRecord(JeproLabLaboratoryModel laboratory, List<Integer> selectedLabs){
            laboratoryIndex = new SimpleIntegerProperty(laboratory.laboratory_id);
            laboratoryName = new SimpleStringProperty(laboratory.name);
            laboratoryCategory = new SimpleStringProperty(JeproLabCategoryModel.getCategoryNameById(laboratory.getCategoryId(), JeproLabContext.getContext().language.language_id));
            laboratoryTheme = new SimpleStringProperty(laboratory.theme_name);
            laboratoryGroup = new SimpleStringProperty(laboratory.laboratory_group.name);
            if(selectedLabs.contains(laboratory.laboratory_id)){
                laboratorySelected = new SimpleBooleanProperty(true);
            }else{
                laboratorySelected = new SimpleBooleanProperty(false);
            }
            laboratoryStatus = new SimpleBooleanProperty(laboratory.published);
        }

        public int getLaboratoryIndex(){ return laboratoryIndex.get(); }

        public String getLaboratoryName(){ return laboratoryName.get(); }

        public String getLaboratoryCategory(){ return laboratoryCategory.get(); }

        public String getLaboratoryGroup(){ return laboratoryGroup.get(); }

        public String getLaboratoryTheme(){
            return laboratoryTheme.get();
        }

        public boolean getLaboratorySelected(){
            return laboratorySelected.get();
        }

        public boolean getLaboratoryStatus(){
            return laboratoryStatus.get();
        }
    }

    public static class JeproLabCheckBoxCellFactory extends TableCell<JeproLabLaboratoryRecord, Boolean>{
        private CheckBox laboratoryCheckBox;

        public JeproLabCheckBoxCellFactory(){
            laboratoryCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabLaboratoryRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() < items.size())){
                setGraphic(laboratoryCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabStatusCellFactory extends TableCell<JeproLabLaboratoryRecord, Button>{
        private Button laboratoryStatusBtn;

        public JeproLabStatusCellFactory(){
            laboratoryStatusBtn = new Button();
            laboratoryStatusBtn.setPrefSize(btnSize, btnSize);
            laboratoryStatusBtn.setMaxSize(btnSize, btnSize);
            laboratoryStatusBtn.setMinSize(btnSize, btnSize);
            laboratoryStatusBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button t){ super.commitEdit(t); }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabLaboratoryRecord> items = getTableView().getItems();

            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getLaboratoryStatus()) {
                    laboratoryStatusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    laboratoryStatusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(laboratoryStatusBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabActionCellFactory extends TableCell<JeproLabLaboratoryRecord, HBox>{
        private HBox commandWrapper;
        private Button editLaboratoryBtn, deleteLaboratoryBtn;

        public JeproLabActionCellFactory(){
            commandWrapper = new HBox(4);
            editLaboratoryBtn = new Button();
            editLaboratoryBtn.setPrefSize(btnSize, btnSize);
            editLaboratoryBtn.setMaxSize(btnSize, btnSize);
            editLaboratoryBtn.setMinSize(btnSize, btnSize);
            editLaboratoryBtn.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteLaboratoryBtn = new Button();
            deleteLaboratoryBtn.setPrefSize(btnSize, btnSize);
            deleteLaboratoryBtn.setMaxSize(btnSize, btnSize);
            deleteLaboratoryBtn.setMinSize(btnSize, btnSize);
            deleteLaboratoryBtn.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper.setAlignment(Pos.CENTER);
            commandWrapper.getChildren().addAll(editLaboratoryBtn, deleteLaboratoryBtn);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabLaboratoryRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                int itemId = items.get(getIndex()).getLaboratoryIndex();
                editLaboratoryBtn.setOnAction(evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoryForm);
                    JeproLab.getInstance().getApplicationForms().addLaboratoryForm.controller.initializeContent(itemId);
                });

                deleteLaboratoryBtn.setOnAction(evt -> {
                    Worker<Boolean> worker = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            if(isCancelled()){
                                return false;
                            }
                            JeproLabLaboratoryModel lab = new JeproLabLaboratoryModel(itemId);
                            return lab.delete();
                        }

                        @Override
                        protected void succeeded(){
                            super.succeeded();
                        }

                        @Override
                        protected void failed(){
                            super.failed();
                        }

                        @Override
                        protected void cancelled(){
                            super.cancelled();
                        }
                    };

                    new Thread((Task)worker).start();
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }

    }
}
