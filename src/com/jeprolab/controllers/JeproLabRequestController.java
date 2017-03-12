package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.application.Platform;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabRequestController extends JeproLabController{
    private Button addRequestBtn;
    private TableView<JeproLabRequestRecord> requestTableView;
    private CheckBox checkAll;
    private ComboBox<String> jeproLabRequestSearchFilter;
    private ObservableList<JeproLabRequestRecord> requestList;
    private Pagination jeproLabRequestPagination;
    private HBox jeproLabRequestSearchWrapper;

    @FXML
    public VBox jeproLabFormPanelWrapper;


    public TextField jeproLabRequestSearchField;
    public Button jeproLabRequestSearchFieldButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 184;

        requestTableView = new TableView<>();
        requestTableView.setPrefSize(formWidth, (rowHeight * JeproLabConfigurationSettings.LIST_LIMIT));
        VBox.setMargin(requestTableView, new Insets(5, 0, 0, (0.01 * JeproLab.APP_WIDTH)));

        TableColumn<JeproLabRequestRecord, String> requestIndexTableColumn = new TableColumn<>("#");
        requestIndexTableColumn.setPrefWidth(30);
        requestIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestIndex"));
        tableCellAlign(requestIndexTableColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        TableColumn<JeproLabRequestRecord, Boolean> requestCheckBoxTableColumn = new TableColumn<>();
        requestCheckBoxTableColumn.setPrefWidth(22);
        requestCheckBoxTableColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabRequestRecord, Boolean>, TableCell<JeproLabRequestRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCellFactory();
        requestCheckBoxTableColumn.setCellFactory(checkBoxFactory);

        TableColumn<JeproLabRequestRecord, String> requestOrderIdTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_REQUEST_REFERENCE_LABEL"));
        requestOrderIdTableColumn.setPrefWidth(0.18 * remainingWidth);
        requestOrderIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestReference"));
        tableCellAlign(requestOrderIdTableColumn, Pos.CENTER);

        TableColumn<JeproLabRequestRecord, String> requestCustomerTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_CUSTOMER_NAME_LABEL"));
        requestCustomerTableColumn.setPrefWidth(0.30 * remainingWidth);
        requestCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestCustomerName"));
        tableCellAlign(requestCustomerTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabRequestRecord, String> requestTotalTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_TOTAL_LABEL"));
        requestTotalTableColumn.setPrefWidth(0.1 * remainingWidth);
        requestTotalTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestTotalPrice"));
        tableCellAlign(requestTotalTableColumn, Pos.CENTER);

        TableColumn<JeproLabRequestRecord, HBox> requestCarrierColumn = new TableColumn<>(bundle.getString("JEPROLAB_CARRIER_LABEL"));
        requestCarrierColumn.setPrefWidth(0.15 * remainingWidth);
        Callback<TableColumn<JeproLabRequestRecord, HBox>, TableCell<JeproLabRequestRecord, HBox>> carrierCellFactory = params -> new JeproLabRequestCarrierCellFactory();
        requestCarrierColumn.setCellFactory(carrierCellFactory);

        TableColumn<JeproLabRequestRecord, String> requestCreationDateColumn = new TableColumn<>(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));
        requestCreationDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestCreatedDate"));
        tableCellAlign(requestCreationDateColumn, Pos.CENTER);
        requestCreationDateColumn.setPrefWidth(0.15 * remainingWidth);

        TableColumn<JeproLabRequestRecord, String> requestStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        requestStatusColumn.setPrefWidth(0.12 * remainingWidth);
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        tableCellAlign(requestStatusColumn, Pos.CENTER);

        TableColumn<JeproLabRequestRecord, Button> requestOnlineColumn = new TableColumn<>(bundle.getString("JEPROLAB_ONLINE_LABEL"));
        requestOnlineColumn.setPrefWidth(65);
        Callback<TableColumn<JeproLabRequestRecord, Button>, TableCell<JeproLabRequestRecord, Button>> onlineAvailabilityFactory = params -> new JeproLabOnlineAvailabilityCellFactory();
        requestOnlineColumn.setCellFactory(onlineAvailabilityFactory);

        TableColumn<JeproLabRequestRecord, HBox> requestActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        requestActionColumn.setPrefWidth(65);
        Callback<TableColumn<JeproLabRequestRecord, HBox>, TableCell<JeproLabRequestRecord, HBox>> actionCellFactory = param -> new JeproLabActionCellFactory();
        requestActionColumn.setCellFactory(actionCellFactory);

        jeproLabRequestSearchWrapper = new HBox(5);

        jeproLabRequestSearchField = new TextField();
        jeproLabRequestSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_REQUEST_LABEL"));

        jeproLabRequestSearchFieldButton = new Button();
        jeproLabRequestSearchFieldButton.getStyleClass().addAll("icon-btn");
        jeproLabRequestSearchFieldButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/search-icon.png"))));

        jeproLabRequestSearchFilter = new ComboBox<>();
        jeproLabRequestSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        HBox.setMargin(jeproLabRequestSearchField, new Insets(5, 5, 5, 0.01 * JeproLab.APP_WIDTH));
        HBox.setMargin(jeproLabRequestSearchFilter, new Insets(5, 5, 5, 5));
        HBox.setMargin(jeproLabRequestSearchFieldButton, new Insets(5, 5, 5, 5));



        jeproLabRequestSearchWrapper.getChildren().addAll(jeproLabRequestSearchField, jeproLabRequestSearchFilter, jeproLabRequestSearchFieldButton);
        requestTableView.getColumns().addAll(
            requestIndexTableColumn, requestCheckBoxTableColumn, requestOrderIdTableColumn, requestCustomerTableColumn,
            requestTotalTableColumn, requestCarrierColumn, requestCreationDateColumn, requestStatusColumn,
            requestOnlineColumn, requestActionColumn
        );
    }

    @Override
    public void initializeContent() {
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabRequestModel> requests;

            @Override
            protected Boolean call() throws Exception {
                requests = JeproLabRequestModel.getRequests();
                return true;
            }

            @Override
            protected void failed() {
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateRequest(requests);
            }
        };
        new Thread((Task) worker).start();
        updateToolBar();
        addEventListeners();
    }

    private void updateRequest(List<JeproLabRequestModel> requests){
        requestList = FXCollections.observableArrayList();
        requestList.addAll(requests.stream().map(JeproLabRequestRecord::new).collect(Collectors.toList()));

        if(!requests.isEmpty()){
            Platform.runLater(() -> {
                double padding = 0.01 * JeproLab.APP_WIDTH;
                jeproLabRequestPagination = new Pagination((requestList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabRequestPagination.setPageFactory(this::createRequestPage);

                jeproLabFormPanelWrapper.getChildren().clear();
                VBox.setMargin(jeproLabRequestSearchWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabRequestPagination, new Insets(5, padding, 5, padding));
                jeproLabFormPanelWrapper.getChildren().addAll(jeproLabRequestSearchWrapper, jeproLabRequestPagination);
            });
        }

    }

    private Node createRequestPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (requestList.size()));
        requestTableView.setItems(FXCollections.observableArrayList(requestList.subList(fromIndex, toIndex)));
        return new Pane(requestTableView);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        addRequestBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addRequestBtn);
    }

    private void addEventListeners(){
        addRequestBtn.setOnAction(event -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
            JeproLab.getInstance().getApplicationForms().addRequestForm.controller.initializeContent();
        });
    }

    public static class JeproLabRequestRecord {
        private SimpleStringProperty requestReference, requestCreatedDate, requestTotalPrice;
        private SimpleStringProperty requestCustomerName, requestStatus;
        private SimpleIntegerProperty requestIndex;

        public JeproLabRequestRecord(JeproLabRequestModel request){
            requestIndex = new SimpleIntegerProperty(request.request_id);
            requestReference = new SimpleStringProperty(request.reference);
            requestCreatedDate = new SimpleStringProperty(JeproLabTools.date("dd-MM-yyyy", request.date_add));
            requestCustomerName = new SimpleStringProperty(JeproLabCustomerModel.getNameByCustomerId(request.customer_id));
            requestStatus = new SimpleStringProperty(request.status_name);
        }

        public int getRequestIndex(){
            return requestIndex.get();
        }

        public String getRequestReference(){
            return requestReference.get().toUpperCase();
        }

        public String getRequestCreatedDate(){
            return requestCreatedDate.get();
        }

        public String getRequestCustomerName(){
            return  requestCustomerName.get();
        }

        public String getRequestStatus(){
            return requestStatus.get();
        }
    }

    public static class JeproLabRequestCarrierCellFactory extends TableCell<JeproLabRequestRecord, HBox>{

    }


    public static class JeproLabOnlineAvailabilityCellFactory extends TableCell<JeproLabRequestRecord, Button>{
        private Button onlineAvailabilityButton;

        public JeproLabOnlineAvailabilityCellFactory(){
            onlineAvailabilityButton = new Button();
            onlineAvailabilityButton.setPrefSize(btnSize, btnSize);
            onlineAvailabilityButton.setMinSize(btnSize, btnSize);
            onlineAvailabilityButton.setMaxSize(btnSize, btnSize);
            onlineAvailabilityButton.getStyleClass().addAll("icon-btn");
        }

        @Override
        public void commitEdit(Button t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Button t, boolean empty){
            super.updateItem(t, empty);
            ObservableList<JeproLabRequestRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(onlineAvailabilityButton);
            }
        }
    }


    public static class JeproLabCheckBoxCellFactory extends TableCell<JeproLabRequestRecord, Boolean> {
        private CheckBox requestCheckBox;

        public JeproLabCheckBoxCellFactory(){
            requestCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean t, boolean empty){
            super.updateItem(t, empty);
            ObservableList<JeproLabRequestRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(requestCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabActionCellFactory extends TableCell<JeproLabRequestRecord, HBox>{
        private HBox commandContainer;
        private Button editRequest, deleteRequest;
        private final double btnSize = 18;

        public JeproLabActionCellFactory(){
            commandContainer = new HBox(10);
            editRequest = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editRequest.setMinSize(btnSize, btnSize);
            editRequest.setMaxSize(btnSize, btnSize);
            editRequest.setPrefSize(btnSize, btnSize);
            editRequest.getStyleClass().add("icon-btn");
            deleteRequest = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteRequest.setMinSize(btnSize, btnSize);
            deleteRequest.setMaxSize(btnSize, btnSize);
            deleteRequest.setPrefSize(btnSize, btnSize);
            deleteRequest.getStyleClass().add("icon-btn");
            commandContainer.setAlignment(Pos.CENTER);
            commandContainer.getChildren().addAll(editRequest, deleteRequest);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox t, boolean empty){
            super.updateItem(t, empty);
            ObservableList<JeproLabRequestRecord> items = getTableView().getItems();

            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getRequestIndex();
                editRequest.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
                    JeproLab.getInstance().getApplicationForms().addRequestForm.controller.initializeContent(itemId);
                });

                deleteRequest.setOnAction(event ->{

                });
                setGraphic(commandContainer);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
