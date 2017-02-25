package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.application.Platform;
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
import javafx.scene.layout.*;
import javafx.util.Callback;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCustomerAddController extends JeproLabController{
    private Button saveButton, cancelButton;
    private JeproLabCustomerModel customer;
    private JeproLabAddressModel address;
    private List<JeproLabCountryModel> countries;
    private List<JeproLabCountryModel.JeproLabZoneModel> zones;
    private List<JeproLabAddressModel.JeproLabStreetTypeModel> streetTypes;
    private TableView<JeproLabRequestController.JeproLabRequestRecord> jeproLabRequestTableView;
    private TableView<JeproLabAddressController.JeproLabAddressRecord> jeproLabAddressesListTableView;
    private CheckBox selectAll;
    private TextField jeproLabRequestSearchField, jeproLabAddressSearchField;
    private Button jeproLabRequestSearchFieldButton, jeproLabAddressSearchFieldButton;
    private ComboBox<String> jeproLabRequestSearchFieldFilter, jeproLabAddressSearchFieldFilter;
    private HBox jeproLabRequestSearchWrapper, jeproLabAddressSearchFieldWrapper;
    private VBox jeproLabCustomerRequestWrapper, jeproLabCustomerAddressWrapper;
    private ObservableList<JeproLabRequestController.JeproLabRequestRecord> requestList;
    private ObservableList<JeproLabAddressController.JeproLabAddressRecord> addressList;
    private static final int displayedItems = JeproLabConfigurationSettings.LIST_LIMIT - 5;

    @FXML
    public Label customerTitleLabel, customerFirstNameLabel, customerLastNameLabel, customerBusinessInternetLabel;
    public Label customerBusinessPhoneLabel, customerPhoneLabel, customerMobilePhoneLabel, customerEmailLabel;
    public Label customerWebsiteLabel, customerZoneLabel, customerCountryLabel, customerStateLabel, customerCityLabel;
    public Label customerAddressDetailLabel, customerAddressNumberLabel, customerAddressDeaLabel, customerAddressLabel;
    public TextField customerFirstName, customerLastName, customerWebsite, customerAddress, customerAddressDetail;
    public JeproPhoneField customerPhone, customerMobilePhone;
    public ComboBox<String> customerTitle;
    public ComboBox<String> customerZone, customerCountry, customerStreetType;
    public CheckBox customerNewsLetter, customerAllowAds;
    public JeproEmailField customerEmail;
    public ImageView customerImageView;
    public GridPane jeproLabAddCustomerFormLayout;
    public Pane customerImageViewWrapper, jeproLabCustomerInformationPane;
    public JeproFormPanel jeproLabAddCustomerFormWrapper;
    public JeproFormPanelTitle jeproLabAddCustomerFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddCustomerFormContainerWrapper;
    public TabPane jeproLabCustomerTabPane;
    public Tab jeproLabCustomerInformationTab, jeproLabCustomerAddressesTab, jeproLabCustomerRequestTab;


    @Override
    public void initialize(URL location , ResourceBundle resource) {
        super.initialize(location, resource);

        double labelColumnWidth = 160;
        double inputColumnWidth = 250;
        //formWidth = 2 * (labelColumnWidth + inputColumnWidth) + 30;
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth)) / 2;
        double posX = (JeproLab.APP_WIDTH / 2) - (formWidth) / 2;
        double posY = 25;

        jeproLabAddCustomerFormWrapper.setPrefWidth(formWidth);
        jeproLabAddCustomerFormWrapper.setLayoutX(posX);
        jeproLabAddCustomerFormWrapper.setLayoutY(posY);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_CUSTOMER_LABEL"));
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setPrefSize(formWidth, 35);
        formTitleLabel.setAlignment(Pos.CENTER);
        jeproLabAddCustomerFormTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabAddCustomerFormTitleWrapper.setPrefSize(formWidth, 40);

        jeproLabAddCustomerFormContainerWrapper.setPrefSize(formWidth, 600);
        jeproLabAddCustomerFormContainerWrapper.setLayoutY(40);

        jeproLabCustomerTabPane.setPrefWidth(formWidth);

        jeproLabCustomerInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));

        VBox.setMargin(jeproLabAddCustomerFormLayout, new Insets(5, 10, 5, 30));
        jeproLabAddCustomerFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25),
            new ColumnConstraints(labelColumnWidth - 15), new ColumnConstraints(inputColumnWidth - 25)
        );
        jeproLabAddCustomerFormLayout.setLayoutX(20);


        customerTitleLabel.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitleLabel.getStyleClass().add("input-label");
        customerTitle.setPromptText(bundle.getString("JEPROLAB_SELECT_TITLE_LABEL"));
        customerTitle.getItems().addAll(
            bundle.getString("JEPROLAB_MR_LABEL"), bundle.getString("JEPROLAB_MRS_LABEL"), bundle.getString("JEPROLAB_MISS_LABEL")
        );

        customerFirstNameLabel.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerFirstNameLabel.getStyleClass().add("input-label");
        customerLastNameLabel.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerLastNameLabel.getStyleClass().add("input-label");
        customerBusinessInternetLabel.setText(bundle.getString("JEPROLAB_BUSINESS_INTERNET_LABEL"));
        customerBusinessInternetLabel.getStyleClass().add("input-label");
        customerBusinessInternetLabel.setAlignment(Pos.CENTER);
        customerBusinessInternetLabel.setPrefSize(JeproLab.APP_WIDTH - 50, 30);
        customerBusinessPhoneLabel.setText(bundle.getString("JEPROLAB_BUSINESS_PHONE_LABEL"));
        customerBusinessPhoneLabel.getStyleClass().add("input-label");
        customerBusinessPhoneLabel.setAlignment(Pos.CENTER);
        customerBusinessPhoneLabel.setPrefSize(JeproLab.APP_WIDTH - 50, 30);
        customerPhoneLabel.setText(bundle.getString("JEPROLAB_PHONE_LABEL"));
        customerPhoneLabel.getStyleClass().add("input-label");
        customerMobilePhoneLabel.setText(bundle.getString("JEPROLAB_MOBILE_PHONE_LABEL"));
        customerMobilePhoneLabel.getStyleClass().add("input-label");
        customerEmailLabel.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerEmailLabel.getStyleClass().add("input-label");
        customerWebsiteLabel.setText(bundle.getString("JEPROLAB_WEBSITE_LABEL"));
        customerWebsiteLabel.getStyleClass().add("input-label");
        customerZoneLabel.setText(bundle.getString("JEPROLAB_ZONE_LABEL"));
        customerZoneLabel.getStyleClass().add("input-label");
        customerCountryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        customerCountryLabel.getStyleClass().add("input-label");
        //customerStateLabel.setText(bundle.getString("JEPROLAB_STATE_LABEL"));

        customerAddressLabel.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        //customerCityLabel.getStyleClass().add("input-label");
        customerAddressDetailLabel.setText(bundle.getString("JEPROLAB_ADDRESS_DETAIL_LABEL"));
        customerAddressDetailLabel.getStyleClass().add("input-label");
        //customerAddressNumberLabel.setText(bundle.getString("JEPROLAB_ADDRESS_NUMBER_LABEL"));
        //customerAddressNumberLabel.getStyleClass().add("input-label");
        //customerAddressStreetNameLabel.setText(bundle.getString("JEPROLAB_STREET_NAME_LABEL"));
        //customerAddressStreetNameLabel.getStyleClass().add("input-label");
        customerNewsLetter.setText(bundle.getString("JEPROLAB_ADD_CUSTOMER_TO_NEWS_LETTER_LABEL"));
        customerAllowAds.setText(bundle.getString("JEPROLAB_RECEIVE_ADS_FROM_PARTNERS_LABEL"));

        /*customerStreetType.setPromptText(bundle.getString("JEPROLAB_SELECT_STREET_TYPE_LABEL"));
        streetTypes = JeproLabAddressModel.JeproLabStreetTypeModel.getStreetTypes(context.language.language_id);
        for(JeproLabAddressModel.JeproLabStreetTypeModel streetType : streetTypes) {
            customerStreetType.getItems().addAll( streetType.name.get("lang_" + context.language.language_id));
        } */

        countries = JeproLabCountryModel.getCountries(context.language.language_id, 0, true);
        zones = JeproLabCountryModel.JeproLabZoneModel.getZones(true);


        customerCountry.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));

        for(JeproLabCountryModel country : countries){
            customerCountry.getItems().add(country.name.get("lang_" + context.language.language_id));
        }

        customerZone.setPrefWidth(120);
        customerZone.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabCountryModel.JeproLabZoneModel zone : zones) {
            customerZone.getItems().add(zone.name);
        }

        GridPane.setMargin(customerTitleLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerFirstNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerLastNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerLastNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerBusinessInternetLabel, new Insets(15, 0, 15, 0));
        GridPane.setMargin(customerBusinessPhoneLabel, new Insets(15, 0, 15, 0));
        GridPane.setMargin(customerMobilePhoneLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerPhoneLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerZoneLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerWebsiteLabel, new Insets(10, 30, 20, 30));
        GridPane.setMargin(customerCountryLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerAddress, new Insets(15, 10, 15, 0));
        //GridPane.setMargin(customerCityLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerAddressDetailLabel, new Insets(10, 30, 10, 0));
        //GridPane.setMargin(customerAddressNumberLabel, new Insets(10, 30, 10, 30));
        //GridPane.setMargin(customerAddressStreetNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerNewsLetter, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerAllowAds, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerEmailLabel, new Insets(10, 30, 10, 0));
        //GridPane.setMargin(customerAllowAds, new Insets(10, 30, 10, 0));
        isInitialized = true;

        renderRequestList();
        renderAddressList();
    }

    private void renderRequestList(){
        double remainingWidth = (0.98 *formWidth) - 187;
        jeproLabCustomerRequestTab.setText(bundle.getString("JEPROLAB_REQUESTS_LABEL"));

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, String> jeproLabRequestIndexTableColumn = new TableColumn<>("#");
        jeproLabRequestIndexTableColumn.setPrefWidth(30);
        jeproLabRequestIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestIndex"));
        tableCellAlign(jeproLabRequestIndexTableColumn, Pos.CENTER_RIGHT);

        CheckBox checkAll = new CheckBox();
        TableColumn<JeproLabRequestController.JeproLabRequestRecord, Boolean> jeproLabRequestCheckBoxTableColumn = new TableColumn<>();
        jeproLabRequestCheckBoxTableColumn.setPrefWidth(22);
        jeproLabRequestCheckBoxTableColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabRequestController.JeproLabRequestRecord, Boolean>, TableCell<JeproLabRequestController.JeproLabRequestRecord, Boolean>> checkBoxFactory = param -> new JeproLabRequestController.JeproLabCheckBoxCellFactory();
        jeproLabRequestCheckBoxTableColumn.setCellFactory(checkBoxFactory);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, String> jeproLabRequestRequestIdTableColumn  = new TableColumn<>(bundle.getString("JEPROLAB_REQUEST_REFERENCE_LABEL"));
        jeproLabRequestRequestIdTableColumn.setPrefWidth(0.45 * remainingWidth);
        jeproLabRequestRequestIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestReference"));
        tableCellAlign(jeproLabRequestRequestIdTableColumn, Pos.CENTER);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, String> jeproLabRequestTotalTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_TOTAL_LABEL"));
        jeproLabRequestTotalTableColumn.setPrefWidth(0.1 * remainingWidth);
        jeproLabRequestTotalTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestTotalPrice"));
        tableCellAlign(jeproLabRequestTotalTableColumn, Pos.CENTER);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, HBox> jeproLabRequestCarrierColumn = new TableColumn<>(bundle.getString("JEPROLAB_CARRIER_LABEL"));
        jeproLabRequestCarrierColumn.setPrefWidth(0.15 * remainingWidth);
        Callback<TableColumn<JeproLabRequestController.JeproLabRequestRecord, HBox>, TableCell<JeproLabRequestController.JeproLabRequestRecord, HBox>> carrierCellFactory = params -> new JeproLabRequestController.JeproLabRequestCarrierCellFactory();
        jeproLabRequestCarrierColumn.setCellFactory(carrierCellFactory);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, String> jeproLabRequestCreationDateColumn = new TableColumn<>(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));
        jeproLabRequestCreationDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestCreatedDate"));
        tableCellAlign(jeproLabRequestCreationDateColumn, Pos.CENTER);
        jeproLabRequestCreationDateColumn.setPrefWidth(0.15 * remainingWidth);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, String> jeproLabRequestStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabRequestStatusColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabRequestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        tableCellAlign(jeproLabRequestStatusColumn, Pos.CENTER);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, Button> jeproLabRequestOnlineColumn = new TableColumn<>(bundle.getString("JEPROLAB_ONLINE_LABEL"));
        jeproLabRequestOnlineColumn.setPrefWidth(65);
        Callback<TableColumn<JeproLabRequestController.JeproLabRequestRecord, Button>, TableCell<JeproLabRequestController.JeproLabRequestRecord, Button>> onlineCellFactory = params -> new JeproLabRequestController.JeproLabOnlineAvailabilityCellFactory();
        jeproLabRequestOnlineColumn.setCellFactory(onlineCellFactory);

        TableColumn<JeproLabRequestController.JeproLabRequestRecord, HBox> jeproLabRequestActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabRequestActionColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabRequestController.JeproLabRequestRecord, HBox>, TableCell<JeproLabRequestController.JeproLabRequestRecord, HBox>> actionCellFactory = param -> new JeproLabRequestController.JeproLabActionCellFactory();
        jeproLabRequestActionColumn.setCellFactory(actionCellFactory);

        jeproLabRequestTableView = new TableView<>();
        jeproLabRequestTableView.setPrefSize(0.98 * formWidth, rowHeight * displayedItems);
        jeproLabRequestTableView.getColumns().addAll(
            jeproLabRequestIndexTableColumn, jeproLabRequestCheckBoxTableColumn, jeproLabRequestRequestIdTableColumn,
            jeproLabRequestTotalTableColumn, jeproLabRequestCarrierColumn, jeproLabRequestCreationDateColumn,
            jeproLabRequestStatusColumn, jeproLabRequestOnlineColumn, jeproLabRequestActionColumn
        );

        jeproLabRequestSearchField = new TextField();
        jeproLabRequestSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_REQUEST_LABEL"));

        jeproLabRequestSearchFieldFilter = new ComboBox<>();

        jeproLabRequestSearchFieldButton = new Button("");
        jeproLabRequestSearchFieldButton.getStyleClass().addAll("icon-btn", "search-btn");

        HBox.setMargin(jeproLabRequestSearchField, new Insets(5, 5, 5, 0.01 * JeproLab.APP_WIDTH));
        HBox.setMargin(jeproLabRequestSearchFieldFilter, new Insets(5, 5, 5, 10));
        HBox.setMargin(jeproLabRequestSearchFieldButton, new Insets(5, 5, 5, 10));

        jeproLabRequestSearchWrapper = new HBox();
        jeproLabRequestSearchWrapper.getChildren().addAll(
            jeproLabRequestSearchField, jeproLabRequestSearchFieldFilter, jeproLabRequestSearchFieldButton
        );

        jeproLabCustomerRequestWrapper = new VBox();

        jeproLabCustomerRequestTab.setContent(jeproLabCustomerRequestWrapper);
    }

    private void renderAddressList(){
        double layoutWidth = (0.98 * formWidth) - 57;
        selectAll = new CheckBox();

        jeproLabCustomerAddressesTab.setText(bundle.getString("JEPROLAB_ADDRESSES_LABEL"));

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, String> jeproLabAddressIndexTableColumn = new TableColumn<>("#");
        jeproLabAddressIndexTableColumn.setPrefWidth(35);
        jeproLabAddressIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("addressIndex"));
        tableCellAlign(jeproLabAddressIndexTableColumn, Pos.CENTER_RIGHT);

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, Boolean> jeproLabAddressCheckBoxTableColumn = new TableColumn<>();
        jeproLabAddressCheckBoxTableColumn.setPrefWidth(20);
        jeproLabAddressCheckBoxTableColumn.setGraphic(selectAll);
        Callback<TableColumn<JeproLabAddressController.JeproLabAddressRecord, Boolean>, TableCell<JeproLabAddressController.JeproLabAddressRecord, Boolean>> checkBoxFactory = param -> new JeproLabAddressController.JeproLabAddressCheckBoxCellFactory();
        jeproLabAddressCheckBoxTableColumn.setCellFactory(checkBoxFactory);

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, HBox> jeproLabAddressActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabAddressActionTableColumn.setPrefWidth(0.1 * layoutWidth);
        Callback<TableColumn<JeproLabAddressController.JeproLabAddressRecord, HBox>, TableCell<JeproLabAddressController.JeproLabAddressRecord, HBox>> actionFactory = param -> new JeproLabAddressController.JeproLabAddressActionCellFactory();
        jeproLabAddressActionTableColumn.setCellFactory(actionFactory);

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, String> jeproLabAddressCountryTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        jeproLabAddressCountryTableColumn.setPrefWidth(0.15 * layoutWidth);
        jeproLabAddressCountryTableColumn.setCellValueFactory(new PropertyValueFactory<>("addressCountry"));
        tableCellAlign(jeproLabAddressCountryTableColumn, Pos.CENTER);

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, String> jeproLabAddressDetailTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        jeproLabAddressDetailTableColumn.setPrefWidth(0.50 * layoutWidth);
        jeproLabAddressDetailTableColumn.setCellValueFactory(new PropertyValueFactory<>("addressDetail"));
        tableCellAlign(jeproLabAddressDetailTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, String> jeproLabAddressZipCodeTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ZIP_CODE_LABEL"));
        jeproLabAddressZipCodeTableColumn.setPrefWidth(0.10 * layoutWidth);
        jeproLabAddressZipCodeTableColumn.setCellValueFactory(new PropertyValueFactory<>("addressZipCode"));
        tableCellAlign(jeproLabAddressZipCodeTableColumn, Pos.CENTER);

        TableColumn<JeproLabAddressController.JeproLabAddressRecord, String> jeproLabAddressCityTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_CITY_LABEL"));
        jeproLabAddressCityTableColumn.setPrefWidth(0.15 * layoutWidth);
        jeproLabAddressCityTableColumn.setCellValueFactory(new PropertyValueFactory<>("addressCity"));
        tableCellAlign(jeproLabAddressCityTableColumn, Pos.CENTER);

        jeproLabAddressesListTableView = new TableView<>();
        jeproLabAddressesListTableView.setPrefSize(0.98 * formWidth, displayedItems * rowHeight);
        jeproLabAddressesListTableView.getColumns().addAll(
            jeproLabAddressIndexTableColumn, jeproLabAddressCheckBoxTableColumn, jeproLabAddressDetailTableColumn,
            jeproLabAddressZipCodeTableColumn, jeproLabAddressCityTableColumn, jeproLabAddressCountryTableColumn,
            jeproLabAddressActionTableColumn
        );

        jeproLabAddressSearchField = new TextField();
        jeproLabAddressSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabAddressSearchFieldFilter = new ComboBox<>();
        jeproLabAddressSearchFieldFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabAddressSearchFieldButton = new Button("");
        jeproLabAddressSearchFieldButton.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabAddressSearchFieldWrapper = new HBox();

        HBox.setMargin(jeproLabAddressSearchField, new Insets(5, 10, 5, 0));
        HBox.setMargin(jeproLabAddressSearchFieldFilter, new Insets(5, 10, 5, 0));
        HBox.setMargin(jeproLabAddressSearchFieldButton, new Insets(5, 10, 5, 0));

        jeproLabAddressSearchFieldWrapper.getChildren().addAll(
            jeproLabAddressSearchField, jeproLabAddressSearchFieldFilter, jeproLabAddressSearchFieldButton
        );

        jeproLabCustomerAddressWrapper = new VBox();
        jeproLabCustomerAddressesTab.setContent(jeproLabCustomerAddressWrapper);
    }

    @Override
    public void initializeContent(int customerId) {
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabRequestModel> requests;
            List<JeproLabAddressModel> addresses;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){ return false; }
                loadCustomer(customerId);
                if(customer.customer_id > 0 && customer.customer_id == customerId) {
                    requests = JeproLabRequestModel.getRequestsByCustomerId(customerId);
                    addresses = customer.getAddresses(JeproLabContext.getContext().language.language_id);
                }
                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateCustomerForm();
                updateCustomerRequests(requests);
                updateCustomerAddresses(addresses);
            }
        };

        new Thread((Task)worker).start();

        final int[] zoneId = {0};
        /*if(address.country_id == country.country_id){
                zoneId = country.zone_id;
                customerCountry.setValue(country.name.get("lang_" + context.language.language_id));
            }*//*if(zone.zone_id == zoneId){
                jeproLabAddressCountryZone.setValue(zone.name);
            }*/
        /*if (customer.customer_id > 0) {

            // todo

        } else {
            address = new JeproLabAddressModel();
            customerTitle.setValue(bundle.getString("JEPROLAB_MR_LABEL"));
        }*/
        updateToolBar();
    }

    private void updateCustomerForm(){
        if(customer != null && customer.customer_id > 0){
            Platform.runLater(() -> {
                formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_LABEL") + " " + bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
                customerTitle.setValue(bundle.getString("JEPROLAB_" + customer.title.toUpperCase() + "_LABEL"));
                customerFirstName.setText(customer.firstname);
                customerLastName.setText(customer.lastname);
                customerNewsLetter.setSelected(customer.news_letter);
                customerAllowAds.setSelected(customer.allow_ads);

                address = JeproLabAddressModel.getAddressByCustomerId(customer.customer_id, true);
                countries.stream().filter(country -> country.country_id == address.country_id).forEach(country -> {
                    customerCountry.setValue(country.name.get("lang_" + context.language.language_id));
                    zones.stream().filter(zone -> zone.zone_id == country.zone_id).forEach(zone -> {
                        customerZone.setValue(zone.name);
                    });
                });
                customerAddress.setText(address.address1);
                customerAddressDetail.setText(address.address2);
                customerPhone.setText(address.phone);
                customerMobilePhone.setText(address.mobile_phone);
                customerWebsite.setText(customer.website);
                customerEmail.setText(customer.email);
                saveButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
            });
        }
    }

    private void updateCustomerRequests(List<JeproLabRequestModel> requests) {
        requestList = FXCollections.observableArrayList();
        requestList.addAll(requests.stream().map(JeproLabRequestController.JeproLabRequestRecord::new).collect(Collectors.toList()));

        Platform.runLater(() -> {
            Pagination jeproLabRequestsPagination = new Pagination((requestList.size()/displayedItems) + 1, 0);
            jeproLabRequestsPagination.setPageFactory(this::createRequestPage);
            jeproLabCustomerRequestWrapper.getChildren().clear();
            VBox.setMargin(jeproLabRequestsPagination, new Insets(0.01 * formWidth));
            jeproLabCustomerRequestWrapper.getChildren().addAll(jeproLabRequestSearchWrapper, jeproLabRequestsPagination);
        });
    }

    private void updateCustomerAddresses(List<JeproLabAddressModel> addresses) {
        addressList = FXCollections.observableArrayList();
        addressList.addAll(addresses.stream().map(JeproLabAddressController.JeproLabAddressRecord::new).collect(Collectors.toList()));

        Platform.runLater(() -> {
            double padding = 0.01 * formWidth;
            Pagination jeproLabAddressPagination = new Pagination((addressList.size()/displayedItems) + 1, 0);
            jeproLabAddressPagination.setPageFactory(this::createAddressesPage);

            VBox.setMargin(jeproLabAddressSearchFieldWrapper, new Insets(5, 10, 5, padding));
            VBox.setMargin(jeproLabAddressPagination, new Insets(5, 10, 5, padding));
            jeproLabCustomerAddressWrapper.getChildren().clear();
            jeproLabCustomerAddressWrapper.getChildren().addAll(jeproLabAddressSearchFieldWrapper, jeproLabAddressPagination);
        });
    }

    private Node createRequestPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (requestList.size()));
        jeproLabRequestTableView.setItems(FXCollections.observableArrayList(requestList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabRequestTableView);
    }

    private Node createAddressesPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (addressList.size()));
        jeproLabAddressesListTableView.setItems(FXCollections.observableArrayList(addressList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabAddressesListTableView);
    }


    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveButton = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));

        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveButton, cancelButton);
        addEventListeners();
    }

    /**
     * Load class object using identifier in $_GET (if possible)
     * otherwise return an empty object, or die     *
     *
     * @return object|boolean
     */
    private void loadCustomer(int customerId){
        if (customerId > 0){
            if (customer == null) {
                customer = new JeproLabCustomerModel(customerId);
            }

            if((customer.customer_id != customerId)){
                //todo set notify the user does not exist
            }
        }else{
            customer = new JeproLabCustomerModel();
        }
    }

    private void addEventListeners(){
        customerZone.valueProperty().addListener((observable, oldValue, newValue) -> {
            int zoneId = JeproLabCountryModel.JeproLabZoneModel.getZoneIdByName(newValue);
            if(context == null){
                context = JeproLabContext.getContext();
            }
            List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountriesByZoneId(zoneId, context.language.language_id);
            customerCountry.getItems().clear();
            for(JeproLabCountryModel country : countries){
                customerCountry.getItems().addAll(country.name.get("lang_" + context.language.language_id));
            }
        });

        customerCountry.valueProperty().addListener(((observable, oldValue, newValue) -> {
            countries.stream().filter(country -> country.name.get("lang_" + context.language.language_id).equals(newValue)).forEach(country -> {
                address.country_id = country.country_id;
            });
        }));

        saveButton.setOnAction(event -> {
            if(customerTitle.getValue().toLowerCase().equals(bundle.getString("JEPROLAB_MR_LABEL").toLowerCase())){
                customer.title = "mr";
            }else if(customerTitle.getValue().toLowerCase().equals(bundle.getString("JEPROLAB_MRS_LABEL").toLowerCase())) {
                customer.title = "mrs";
            }else if(customerTitle.getValue().toLowerCase().equals(bundle.getString("JEPROLAB_MISS_LABEL").toLowerCase())){
                customer.title = "miss";
            }

            customer.firstname = customerFirstName.getText();
            customer.lastname = customerLastName.getText();
            customer.website = customerWebsite.getText();
            customer.email = customerEmail.getText();

            address.firstname = customerFirstName.getText();
            address.lastname = customerLastName.getText();
            address.address1 = customerAddress.getText();
            address.address2 = customerAddressDetail.getText();
            address.phone = customerPhone.getText();
            address.mobile_phone = customerMobilePhone.getText();

            if(customer.customer_id > 0){
                customer.update();
                address.update();
            }else{
                customer.add();
                address.customer_id = customer.customer_id;
                address.add();
            }
        });

        customerNewsLetter.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            customer.news_letter = newValue;
        }));
        //cancelButton = new Button(JeproLab.getBundle().getString("JEPROLAB_CANCEL_LABEL"));
    }
}
