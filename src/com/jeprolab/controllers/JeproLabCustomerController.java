package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabCustomerModel;
import javafx.beans.property.SimpleBooleanProperty;
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
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabCustomerController extends JeproLabController {
    private Button addCustomerBtn;

    @FXML
    public VBox jeproLabCustomerListWrapper;
    public Button searchCustomerButton;
    public HBox jeproLabCustomerSearchToolBar;
    public TextField customerSearchBox;
    public TableView<JeproLabCustomerRecord> customersTableView;
    public TableColumn<JeproLabCustomerRecord, Integer> customerListNumberColumn;
    public TableColumn<JeproLabCustomerRecord, String> customerTitleColumn, customerNameColumn;
    public TableColumn<JeproLabCustomerRecord, Boolean>customerCheckBoxColumn, customerAllowAdsColumn, customerActiveColumn;
    public TableColumn<JeproLabCustomerRecord, String> customerFistNameColumn, customerLastVisitColumn, customerDateAddColumn;
    public TableColumn<JeproLabCustomerRecord,String> customerCompanyColumn, customerReportColumn;
    public TableColumn<JeproLabCustomerRecord, Button> customerEmailAddressColumn;
    public TableColumn<JeproLabCustomerRecord, HBox> customerActionsColumn;

    @Override
    public void initialize(URL location , ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = .98 * JeproLab.APP_WIDTH;
        double remainingWidth = (formWidth - 272);

        customersTableView.setPrefSize(formWidth, 600);
        customersTableView.setLayoutX(.01 * JeproLab.APP_WIDTH);
        //customersTableView.setLayoutY(50);
        customerListNumberColumn.setText("#");
        customerListNumberColumn.setPrefWidth(30);
        customerListNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerIndex"));
        tableCellAlign(customerListNumberColumn, Pos.CENTER_RIGHT);

        CheckBox checkAll = new CheckBox();
        customerCheckBoxColumn.setGraphic(checkAll);
        customerCheckBoxColumn.setPrefWidth(20);
        Callback<TableColumn<JeproLabCustomerRecord, Boolean>, TableCell<JeproLabCustomerRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        customerCheckBoxColumn.setCellFactory(checkBoxFactory);

        customerTitleColumn.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitleColumn.setPrefWidth(45);
        customerTitleColumn.setCellValueFactory(new PropertyValueFactory<>("customerTitle"));

        customerNameColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerNameColumn.setPrefWidth(.18 * remainingWidth);
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastName"));

        customerFistNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerFistNameColumn.setPrefWidth(.18 * remainingWidth);
        customerFistNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerFirstName"));

        customerLastVisitColumn.setText(bundle.getString("JEPROLAB_LAST_VISIT_LABEL"));
        customerLastVisitColumn.setPrefWidth(0.11 * remainingWidth);
        customerLastVisitColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastVisit"));
        customerDateAddColumn.setText(bundle.getString("JEPROLAB_DATE_ADD_LABEL"));
        customerDateAddColumn.setPrefWidth(.15 * remainingWidth);
        customerDateAddColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddedDate"));
        tableCellAlign(customerDateAddColumn, Pos.CENTER);
        customerAllowAdsColumn.setText(bundle.getString("JEPROLAB_ALLOWS_ADS_LABEL"));
        Callback<TableColumn<JeproLabCustomerRecord, Boolean>, TableCell<JeproLabCustomerRecord, Boolean>> allowAdsFactory = param -> new JeproLabAllowAdsCell();
        customerAllowAdsColumn.setCellFactory(allowAdsFactory);
        customerAllowAdsColumn.setPrefWidth(.13 * remainingWidth);
        customerActiveColumn.setText(bundle.getString("JEPROLAB_ACTIVE_LABEL"));
        Callback<TableColumn<JeproLabCustomerRecord, Boolean>, TableCell<JeproLabCustomerRecord, Boolean>> activeFactory = param -> new JeproLabActiveCell();
        customerActiveColumn.setCellFactory(activeFactory);
        customerActiveColumn.setPrefWidth(55);
        customerCompanyColumn.setText(bundle.getString("JEPROLAB_COMPANY_LABEL"));
        customerCompanyColumn.setPrefWidth(.15 * remainingWidth);
        customerCompanyColumn.setCellValueFactory(new PropertyValueFactory<>("customerCompany"));
        customerEmailAddressColumn.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerEmailAddressColumn.setPrefWidth(55);
        Callback<TableColumn<JeproLabCustomerRecord, Button>, TableCell<JeproLabCustomerRecord, Button>> emailFactory = param -> new JeproLabEmailCell();
        customerEmailAddressColumn.setCellFactory(emailFactory);
        customerReportColumn.setText(bundle.getString("JEPROLAB_REPORT_LABEL"));
        customerReportColumn.setPrefWidth(0.1 * remainingWidth);
        customerReportColumn.setCellValueFactory(new PropertyValueFactory<>("customerReport"));
        customerActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        customerActionsColumn.setPrefWidth(65);
        Callback<TableColumn<JeproLabCustomerRecord, HBox>, TableCell<JeproLabCustomerRecord, HBox>> actionsFactory = param -> new JeproLabActionCell();
        customerActionsColumn.setCellFactory(actionsFactory);

        VBox.setMargin(jeproLabCustomerSearchToolBar, new Insets(5, 0, 5, (0.01 * JeproLab.APP_WIDTH)));
        VBox.setMargin(customersTableView, new Insets(0, 0, 5, (0.01 * JeproLab.APP_WIDTH)));
    }

    @Override
    public void initializeContent(){
        List<JeproLabCustomerModel> customers = JeproLabCustomerModel.getCustomers();
        ObservableList<JeproLabCustomerRecord> analyzeList = FXCollections.observableArrayList();
        if(!customers.isEmpty()){
            analyzeList.addAll(customers.stream().map(JeproLabCustomerRecord::new).collect(Collectors.toList()));
            customersTableView.setItems(analyzeList);
        }
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addCustomerBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addCustomerBtn.setOnAction(event -> {
            try {
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCustomerForm);
                context.controller.initializeContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        commandWrapper.getChildren().addAll(addCustomerBtn);
    }

    public static class JeproLabCustomerRecord{
        private SimpleStringProperty customerTitle, customerLastName, customerFirstName;
        private SimpleStringProperty customerLastVisit, customerAddedDate, customerCompany;
        private SimpleIntegerProperty customerIndex;
        private SimpleBooleanProperty customerAllowAds, customerCurrentStatus;

        public JeproLabCustomerRecord(JeproLabCustomerModel customer){
            customerTitle = new SimpleStringProperty(customer.title);
            customerFirstName = new SimpleStringProperty(customer.firstname);
            customerLastName = new SimpleStringProperty(customer.lastname);
            customerLastVisit = new SimpleStringProperty("");
            customerAddedDate = new SimpleStringProperty(JeproLabTools.date("dd-MM-yyyy", customer.date_add));
            customerCompany = new SimpleStringProperty(customer.company);
            customerIndex = new SimpleIntegerProperty(customer.customer_id);
            customerAllowAds = new SimpleBooleanProperty(customer.allow_ads);
            customerCurrentStatus = new SimpleBooleanProperty(customer.published);
        }

        /**
         * GETTERS
         */
        public int getCustomerIndex(){
            return customerIndex.get();
        }

        public String getCustomerTitle(){
            return JeproLab.getBundle().getString("JEPROLAB_" + customerTitle.get().toUpperCase() + "_LABEL");
        }

        public String getCustomerLastName(){
            return customerLastName.get();
        }

        public String getCustomerFirstName(){
            return customerFirstName.get();
        }

        public String getCustomerLastVisit(){
            return customerLastVisit.get();
        }

        public String getCustomerAddedDate(){
            return customerAddedDate.get();
        }

        public String getCustomerCompany(){
            return customerCompany.get();
        }

        public boolean getCustomerAllowAds(){
            return customerAllowAds.get();
        }

        public boolean getCustomerCurrentStatus(){
            return customerCurrentStatus.get();
        }
    }

    private static class JeproLabCheckBoxCell extends TableCell<JeproLabCustomerRecord, Boolean>{
        private CheckBox customerCheckBox;

        public JeproLabCheckBoxCell(){
            customerCheckBox = new CheckBox();
        }
        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCustomerRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {

                setGraphic(customerCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabAllowAdsCell extends TableCell<JeproLabCustomerRecord, Boolean>{
        private Button allowAdsBtn;

        public JeproLabAllowAdsCell(){
            allowAdsBtn = new Button("");
            allowAdsBtn.setMinSize(18, 18);
            allowAdsBtn.setMaxSize(18, 18);
            allowAdsBtn.setPrefSize(18, 18);
            allowAdsBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCustomerRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                if(items.get(getIndex()).getCustomerAllowAds()) {
                    allowAdsBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    allowAdsBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(allowAdsBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabActiveCell extends TableCell<JeproLabCustomerRecord, Boolean>{
        private Button activeBtn;

        public JeproLabActiveCell(){
            activeBtn = new Button("");
            activeBtn.setMinSize(18, 18);
            activeBtn.setMaxSize(18, 18);
            activeBtn.setPrefSize(18, 18);
            activeBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCustomerRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                if(items.get(getIndex()).getCustomerCurrentStatus()) {
                    activeBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    activeBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(activeBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabEmailCell extends TableCell<JeproLabCustomerRecord, Button>{
        private  Button emailBtn;

        public JeproLabEmailCell(){
            emailBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/mail.png"))));
            emailBtn.setMinSize(18, 18);
            emailBtn.setMaxSize(18, 18);
            emailBtn.setPrefSize(18, 18);
            emailBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCustomerRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {

                setGraphic(emailBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabActionCell extends TableCell<JeproLabCustomerRecord, HBox>{
        protected HBox commandContainer;
        private Button editCustomer, deleteCustomer;
        private double btnSize = 18;

        public JeproLabActionCell(){
            commandContainer = new HBox(10);
            editCustomer = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editCustomer.setPrefSize(btnSize, btnSize);
            editCustomer.setMinSize(btnSize, btnSize);
            editCustomer.setMaxSize(btnSize, btnSize);
            editCustomer.getStyleClass().add("icon-btn");

            /*viewCustomer = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/view.png"))));
            viewCustomer.setPrefSize(btnSize, btnSize);
            viewCustomer.setMinSize(btnSize, btnSize);
            viewCustomer.setMaxSize(btnSize, btnSize);
            viewCustomer.getStyleClass().add("icon-btn");*/

            deleteCustomer = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteCustomer.setPrefSize(btnSize, btnSize);
            deleteCustomer.setMinSize(btnSize, btnSize);
            deleteCustomer.setMaxSize(btnSize, btnSize);
            deleteCustomer.getStyleClass().add("icon-btn");

            commandContainer.getChildren().addAll(editCustomer, deleteCustomer);
            commandContainer.setAlignment(Pos.CENTER);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCustomerRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                int itemId = items.get(getIndex()).getCustomerIndex();
                editCustomer.setOnAction(event -> {
                    JeproLab.request.setRequest("customer_id=" + itemId);
                    try{
                        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCustomerForm);
                        JeproLabContext.getContext().controller.initializeContent();
                    }catch (IOException ignored){
                        ignored.printStackTrace();
                    }
                });
                deleteCustomer.setOnAction(event -> {

                });
                setGraphic(commandContainer);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
