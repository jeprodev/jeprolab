package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCarrierModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 11/04/2017.
 */
public class JeproLabCarrierController extends JeproLabController{
    private Button addNewCarrierButton;

    private CheckBox checkAll;
    private TableView<JeproLabCarrierRecord> jeproLabCarrierRecordTableView;
    private HBox jeproLabCarrierSearchWrapper;
    private TextField jeproLabCarrierSearchField;
    private ComboBox<String> jeproLabCarrierSearchFilter;
    private Button jeproLabCarrierSearchBtn;
    private ObservableList<JeproLabCarrierRecord> carrierList;


    @FXML
    public VBox jeproLabCarrierTableViewWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;

        jeproLabCarrierRecordTableView = new TableView<>();
        jeproLabCarrierRecordTableView.setPrefSize(formWidth, (rowHeight * JeproLabConfigurationSettings.LIST_LIMIT) + 25);

        jeproLabCarrierSearchField = new TextField();
        jeproLabCarrierSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabCarrierSearchFilter = new ComboBox<>();
        jeproLabCarrierSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabCarrierSearchBtn = new Button();
        jeproLabCarrierSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabCarrierSearchWrapper = new HBox(5);
        jeproLabCarrierSearchWrapper.getChildren().addAll(
            jeproLabCarrierSearchField, jeproLabCarrierSearchFilter, jeproLabCarrierSearchBtn
        );

        TableColumn<JeproLabCarrierRecord, String> jeproLabCarrierIndexTableColumn = new TableColumn<>("#");
        jeproLabCarrierIndexTableColumn.setPrefWidth(30);
        tableCellAlign(jeproLabCarrierIndexTableColumn, Pos.CENTER_RIGHT);
        jeproLabCarrierIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("carrierIndex"));

        TableColumn<JeproLabCarrierRecord, Boolean> jeproLabCarrierCheckBoxTableColumn = new TableColumn<>();
        checkAll = new CheckBox();
        jeproLabCarrierCheckBoxTableColumn.setGraphic(checkAll);
        jeproLabCarrierCheckBoxTableColumn.setPrefWidth(25);
        tableCellAlign(jeproLabCarrierCheckBoxTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabCarrierRecord, Boolean>, TableCell<JeproLabCarrierRecord, Boolean>> carrierCheckBoxFactory = param -> new JeproLabCarrierCheckBoxCellFactory();
        jeproLabCarrierCheckBoxTableColumn.setCellFactory(carrierCheckBoxFactory);

        TableColumn<JeproLabCarrierRecord, String> jeproLabCarrierNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabCarrierNameTableColumn.setPrefWidth(formWidth - 497);
        tableCellAlign(jeproLabCarrierNameTableColumn, Pos.CENTER_LEFT);
        jeproLabCarrierNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("carrierName"));

        TableColumn<JeproLabCarrierRecord, String> jeproLabCarrierReferenceTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabCarrierReferenceTableColumn.setPrefWidth(120);
        tableCellAlign(jeproLabCarrierReferenceTableColumn, Pos.CENTER);
        jeproLabCarrierReferenceTableColumn.setCellValueFactory(new PropertyValueFactory<>("carrierReference"));

        TableColumn<JeproLabCarrierRecord, Button> jeproLabCarrierIsFreeTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_IS_FREE_LABEL"));
        jeproLabCarrierIsFreeTableColumn.setPrefWidth(60);
        tableCellAlign(jeproLabCarrierIsFreeTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabCarrierRecord, Button>, TableCell<JeproLabCarrierRecord, Button>> carrierIsFreeCellFactory = params -> new JeproLabCarrierIsFreeCellFactory();
        jeproLabCarrierIsFreeTableColumn.setCellFactory(carrierIsFreeCellFactory);

        TableColumn<JeproLabCarrierRecord, Button> jeproLabCarrierNeedRangeTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NEED_RANGE_LABEL"));
        jeproLabCarrierNeedRangeTableColumn.setPrefWidth(120);
        tableCellAlign(jeproLabCarrierNeedRangeTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabCarrierRecord, Button>, TableCell<JeproLabCarrierRecord, Button>> carrierNeedRangeCellFactory = params -> new JeproLabCarrierNeedRangeCellFactory();
        jeproLabCarrierNeedRangeTableColumn.setCellFactory(carrierNeedRangeCellFactory);

        //TableColumn<JeproLabCarrierRecord, String> jeproLabCarrierTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_LABEL"));
        TableColumn<JeproLabCarrierRecord, HBox> jeproLabCarrierGradeTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_GRADE_RATE_LABEL"));
        jeproLabCarrierGradeTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabCarrierRecord, HBox>, TableCell<JeproLabCarrierRecord, HBox>> gradeCellFactory = params -> new JeproLabCarrierGradeCellFactory();
        jeproLabCarrierGradeTableColumn.setCellFactory(gradeCellFactory);

        TableColumn<JeproLabCarrierRecord, HBox> jeproLabCarrierActionsTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabCarrierActionsTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabCarrierRecord, HBox>, TableCell<JeproLabCarrierRecord, HBox>> actionsCellFactory = params -> new JeproLabCarrierActionsCellFactory();
        jeproLabCarrierActionsTableColumn.setCellFactory(actionsCellFactory);

        jeproLabCarrierRecordTableView.getColumns().addAll(
            jeproLabCarrierIndexTableColumn, jeproLabCarrierCheckBoxTableColumn, jeproLabCarrierNameTableColumn,
            jeproLabCarrierReferenceTableColumn, jeproLabCarrierIsFreeTableColumn, jeproLabCarrierNeedRangeTableColumn,
            jeproLabCarrierGradeTableColumn, jeproLabCarrierActionsTableColumn
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabCarrierModel> carriers;

            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                carriers = JeproLabCarrierModel.getCarriers(JeproLabContext.getContext().language.language_id);
                System.out.println("taille " + carriers.size());
                return true;
            }

            @Override
            public void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            public void succeeded(){
                updateTableView(carriers);
            }
        };
        JeproLab.getInstance().executor.submit((Task)worker);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        addNewCarrierButton = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"));
        addNewCarrierButton.getStyleClass().addAll("save-btn");

        commandWrapper.getChildren().clear();
        commandWrapper.getChildren().addAll(addNewCarrierButton);
        setEventHandler();
    }

    private void setEventHandler(){
        addNewCarrierButton.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCarrierForm);
            JeproLab.getInstance().getApplicationForms().addCarrierForm.controller.initializeContent();
        });
    }

    private void updateTableView(List<JeproLabCarrierModel> carriers){
        carrierList = FXCollections.observableArrayList();
        carrierList.addAll(carriers.stream().map(JeproLabCarrierRecord::new).collect(Collectors.toList()));

        if(carrierList.isEmpty()){
            setEmptyTableView(jeproLabCarrierTableViewWrapper, jeproLabCarrierSearchWrapper, jeproLabCarrierRecordTableView);
        }else{
            Pagination jeproLabCarrierPagination = new Pagination((carrierList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
            jeproLabCarrierPagination.setPageFactory(this::createPageFactory);
            setTableViewContent(jeproLabCarrierTableViewWrapper, jeproLabCarrierSearchWrapper, jeproLabCarrierPagination);
        }
    }

    private Node createPageFactory(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (carrierList.size()));
        jeproLabCarrierRecordTableView.setItems(FXCollections.observableArrayList(carrierList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabCarrierRecordTableView);
    }

    public static class JeproLabCarrierRecord {
        private SimpleIntegerProperty carrierIndex, carrierGrade;
        private SimpleStringProperty carrierName;
        private SimpleStringProperty carrierReference;


        public JeproLabCarrierRecord(JeproLabCarrierModel carrier){
            carrierIndex = new SimpleIntegerProperty(carrier.carrier_id);
            carrierGrade = new SimpleIntegerProperty(carrier.grade);
            carrierReference = new SimpleStringProperty(carrier.reference);
            carrierName = new SimpleStringProperty(carrier.name);
        }

        public int getCarrierIndex(){ return carrierIndex.get(); }

        public int getCarrierGrade(){ return carrierGrade.get(); }

        public String getCarrierName(){ return carrierName.get(); }

        public String getCarrierReference(){ return carrierReference.get().toUpperCase(); }
    }

    public static class JeproLabCarrierIsFreeCellFactory extends TableCell<JeproLabCarrierRecord, Button>{
        @Override
        public void commitEdit(Button item){ super.commitEdit(item); }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
        }
    }

    public static class JeproLabCarrierNeedRangeCellFactory extends TableCell<JeproLabCarrierRecord, Button>{
        @Override
        public void commitEdit(Button item){ super.commitEdit(item); }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
        }
    }

    public static class JeproLabCarrierCheckBoxCellFactory extends TableCell<JeproLabCarrierRecord, Boolean> {
        private CheckBox carrierCheckBox;

        public JeproLabCarrierCheckBoxCellFactory(){
            carrierCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCarrierRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                setAlignment(Pos.CENTER);
                setGraphic(carrierCheckBox);
            }
        }
    }

    public static class JeproLabCarrierGradeCellFactory extends TableCell<JeproLabCarrierRecord, HBox>{
        private HBox gradeCommandWrapper;
        private Button oneOutOfFiveBtn, twoOutOfFiveBtn, threeOutOfFiveBtn, fourOutOfFiveBtn, fiveOutOfFiveBtn;

        public JeproLabCarrierGradeCellFactory(){
            oneOutOfFiveBtn = new Button();
            oneOutOfFiveBtn.setPrefSize(btnSize, btnSize);
            oneOutOfFiveBtn.setMaxSize(btnSize, btnSize);
            oneOutOfFiveBtn.setMinSize(btnSize, btnSize);
            oneOutOfFiveBtn.getStyleClass().add("icon-btn");

            twoOutOfFiveBtn = new Button();
            twoOutOfFiveBtn.setPrefSize(btnSize, btnSize);
            twoOutOfFiveBtn.setMaxSize(btnSize, btnSize);
            twoOutOfFiveBtn.setMinSize(btnSize, btnSize);
            twoOutOfFiveBtn.getStyleClass().add("icon-btn");

            threeOutOfFiveBtn = new Button();
            threeOutOfFiveBtn.setPrefSize(btnSize, btnSize);
            threeOutOfFiveBtn.setMaxSize(btnSize, btnSize);
            threeOutOfFiveBtn.setMinSize(btnSize, btnSize);
            threeOutOfFiveBtn.getStyleClass().add("icon-btn");

            fourOutOfFiveBtn = new Button();
            fourOutOfFiveBtn.setPrefSize(btnSize, btnSize);
            fourOutOfFiveBtn.setMaxSize(btnSize, btnSize);
            fourOutOfFiveBtn.setMinSize(btnSize, btnSize);
            fourOutOfFiveBtn.getStyleClass().add("icon-btn");

            fiveOutOfFiveBtn = new Button();
            fiveOutOfFiveBtn.setPrefSize(btnSize, btnSize);
            fiveOutOfFiveBtn.setMaxSize(btnSize, btnSize);
            fiveOutOfFiveBtn.setMinSize(btnSize, btnSize);
            fiveOutOfFiveBtn.getStyleClass().add("icon-btn");

            gradeCommandWrapper = new HBox(0);
            gradeCommandWrapper.setAlignment(Pos.CENTER);
            gradeCommandWrapper.getChildren().addAll(oneOutOfFiveBtn, twoOutOfFiveBtn, threeOutOfFiveBtn, fourOutOfFiveBtn, fiveOutOfFiveBtn);
        }

        @Override
        public void commitEdit(HBox item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCarrierRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                int grade = items.get(getIndex()).getCarrierGrade();
                setGraphic(gradeCommandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabCarrierActionsCellFactory extends TableCell<JeproLabCarrierRecord, HBox>{
        private HBox commandWrapper;
        private Button editButton, deleteButton;

        public JeproLabCarrierActionsCellFactory(){
            editButton = new Button();
            editButton.setPrefSize(btnSize, btnSize);
            editButton.setMinSize(btnSize, btnSize);
            editButton.setMaxSize(btnSize, btnSize);
            editButton.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteButton = new Button();
            deleteButton.setPrefSize(btnSize, btnSize);
            deleteButton.setMinSize(btnSize, btnSize);
            deleteButton.setMaxSize(btnSize, btnSize);
            deleteButton.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper = new HBox(5);
            commandWrapper.setAlignment(Pos.CENTER);
            commandWrapper.getChildren().addAll(editButton, deleteButton);
        }

        @Override
        public void commitEdit(HBox item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            ObservableList<JeproLabCarrierRecord> items = getTableView().getItems();
            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getCarrierIndex();
                editButton.setOnAction(evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCarrierForm);
                    JeproLab.getInstance().getApplicationForms().addCarrierForm.controller.initializeContent(itemId);
                });

                deleteButton.setOnAction(evt -> {

                });

                setAlignment(Pos.CENTER);
                setGraphic(commandWrapper);
            }
        }
    }
}
