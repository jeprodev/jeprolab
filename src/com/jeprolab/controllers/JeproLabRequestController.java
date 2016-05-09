package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabRequestController extends JeproLabController {
    private Button addRequestBtn;
    @FXML
    public VBox jeproLabFormPanelWrapper;
    public TableView<JeproLabRequestRecord> requestTableView;
    public TableColumn<JeproLabRequestRecord, String> requestIndexTableColumn;
    public TableColumn<JeproLabRequestRecord, Boolean> requestCheckBoxTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestOrderIdTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCustomerTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestTotalTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCarrierColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCreationDateColumn;
    public TableColumn<JeproLabRequestRecord, String> requestStatusColumn;
    public TableColumn<JeproLabRequestRecord, String> requestOnlineColumn;
    public TableColumn<JeproLabRequestRecord, HBox> requestActionColumn;
    public TextField jeproLabRequestSearchField;
    public Button jeproLabRequestSearchFieldButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 184;

        /*jeproLabFormPanelWrapper.setPrefWidth(formWidth);
        jeproLabFormPanelWrapper.setLayoutY(10);
        jeproLabFormPanelWrapper.setLayoutX;*/

        requestTableView.setPrefSize(formWidth, 600);
        VBox.setMargin(requestTableView, new Insets(5, 0, 0, (0.01 * JeproLab.APP_WIDTH)));

        requestIndexTableColumn.setPrefWidth(30);
        requestIndexTableColumn.setText("#");
        requestIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestIndex"));
        tableCellAlign(requestIndexTableColumn, Pos.CENTER_RIGHT);

        CheckBox checkAll = new CheckBox();
        requestCheckBoxTableColumn.setPrefWidth(22);
        requestCheckBoxTableColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabRequestRecord, Boolean>, TableCell<JeproLabRequestRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        requestCheckBoxTableColumn.setCellFactory(checkBoxFactory);

        requestOrderIdTableColumn.setText(bundle.getString("JEPROLAB_REQUEST_REFERENCE_LABEL"));
        requestOrderIdTableColumn.setPrefWidth(0.18 * remainingWidth);
        requestOrderIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestReference"));
        tableCellAlign(requestOrderIdTableColumn, Pos.CENTER);

        requestCustomerTableColumn.setText(bundle.getString("JEPROLAB_CUSTOMER_NAME_LABEL"));
        requestCustomerTableColumn.setPrefWidth(0.30 * remainingWidth);
        requestCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestCustomerName"));
        tableCellAlign(requestCustomerTableColumn, Pos.CENTER_LEFT);

        requestTotalTableColumn.setText(bundle.getString("JEPROLAB_TOTAL_LABEL"));
        requestTotalTableColumn.setPrefWidth(0.1 * remainingWidth);
        requestCarrierColumn.setText(bundle.getString("JEPROLAB_CARRIER_LABEL"));
        requestCarrierColumn.setPrefWidth(0.15 * remainingWidth);
        requestCreationDateColumn.setText(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));
        requestCreationDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestCreatedDate"));
        tableCellAlign(requestCreationDateColumn, Pos.CENTER);
        requestCreationDateColumn.setPrefWidth(0.15 * remainingWidth);

        requestStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        requestStatusColumn.setPrefWidth(0.12 * remainingWidth);
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        tableCellAlign(requestStatusColumn, Pos.CENTER);

        requestOnlineColumn.setText(bundle.getString("JEPROLAB_ONLINE_LABEL"));
        requestOnlineColumn.setPrefWidth(65);

        requestActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        requestActionColumn.setPrefWidth(65);
        Callback<TableColumn<JeproLabRequestRecord, HBox>, TableCell<JeproLabRequestRecord, HBox>> actionCellFactory = param -> new JeproLabActionCell();
        requestActionColumn.setCellFactory(actionCellFactory);

        HBox.setMargin(jeproLabRequestSearchField, new Insets(5, 5, 5, 0.01 * JeproLab.APP_WIDTH));
        HBox.setMargin(jeproLabRequestSearchFieldButton, new Insets(5, 5, 5, 5));
        jeproLabRequestSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_REQUEST_LABEL"));
        jeproLabRequestSearchFieldButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/search-icon.png"))));
    }

    @Override
    public void initializeContent(){
        List<JeproLabRequestModel> requests = JeproLabRequestModel.getRequests();
        ObservableList<JeproLabRequestRecord> requestList = FXCollections.observableArrayList();
        if(!requests.isEmpty()){
            requestList.clear();
            requestList.addAll(requests.stream().map(JeproLabRequestRecord::new).collect(Collectors.toList()));
            requestTableView.getItems().clear();
            requestTableView.setItems(requestList);
        }
        updateToolBar();
        addEventListeners();
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
            try {
                JeproLab.request.getRequest().clear();
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
                JeproLabContext.getContext().controller.initializeContent();
            }catch (IOException  ignored){
                ignored.printStackTrace();
            }
        });
    }

    public static class JeproLabRequestRecord{
        private SimpleStringProperty requestReference, requestCreatedDate;
        private SimpleStringProperty requestCustomerName, requestStatus;
        private SimpleIntegerProperty requestIndex, requestId;

        public JeproLabRequestRecord(JeproLabRequestModel request){
            requestIndex = new SimpleIntegerProperty(request.request_id);
            requestReference = new SimpleStringProperty(request.reference);
            requestCreatedDate = new SimpleStringProperty(JeproLabTools.date("dd-MM-yyyy", request.date_add));
            requestCustomerName = new SimpleStringProperty(JeproLabCustomerModel.getNameByCustomerId(request.customer_id));
            requestStatus = new SimpleStringProperty();
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

    public static class JeproLabCheckBoxCell extends TableCell<JeproLabRequestRecord, Boolean>{
        private CheckBox requestCheckBox;

        public JeproLabCheckBoxCell(){
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

    public static class JeproLabActionCell extends TableCell<JeproLabRequestRecord, HBox>{
        private HBox commandContainer;
        private Button editRequest, deleteRequest;
        private final double btnSize = 18;

        public JeproLabActionCell(){
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
                    JeproLab.request.setRequest("request_id=" + itemId);
                    try {
                        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
                        JeproLabContext.getContext().controller.initializeContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteRequest.setOnAction(event ->{

                });
                setGraphic(commandContainer);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
