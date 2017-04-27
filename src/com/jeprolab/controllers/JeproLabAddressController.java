package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabAddressModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabAddressController extends JeproLabController{
    private ObservableList<JeproLabAddressRecord> addressList;
    private CheckBox selectAll;
    private Button addAddressBtn, jeproLabAddressSearchBtn;
    private TableView<JeproLabAddressRecord> jeproLabAddressesTableView;
    private Pagination  jeproLabAddressesPagination;
    private HBox jeproLabAddressSearchWrapper;
    private TextField jeproLabAddressSearchField;
    private ComboBox<String> jeproLabAddressSearchFilter;

    @FXML
    public VBox jeproLabAddressContainerWrapper;
    //public Button jeproLabAddAddressButton, jeproLabDeleteAddressesButton;


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        double layoutWidth = (0.98 * JeproLab.APP_WIDTH) - 59;
        selectAll = new CheckBox();

        jeproLabAddressSearchWrapper = new HBox(10);

        jeproLabAddressSearchField = new TextField();
        jeproLabAddressSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabAddressSearchFilter = new ComboBox<>();
        jeproLabAddressSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabAddressSearchBtn = new Button();
        jeproLabAddressSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabAddressSearchWrapper.getChildren().addAll(jeproLabAddressSearchField, jeproLabAddressSearchFilter, jeproLabAddressSearchBtn);

        //jeproLabAddressContainerWrapper = new VBox();


        jeproLabAddressesTableView = new TableView<>();
        jeproLabAddressesTableView.setPrefWidth(JeproLab.APP_WIDTH * 0.98);
        jeproLabAddressesTableView.setPrefHeight((rowHeight * JeproLabConfigurationSettings.LIST_LIMIT) + 6);

        //jeproLabAddressesList.setLayoutY(10);
        //jeproLabAddressesList.setLayoutX(padding);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressIndexColumn = new TableColumn<>("#");
        jeproLabAddressIndexColumn.setPrefWidth(35);
        jeproLabAddressIndexColumn.setCellValueFactory(new PropertyValueFactory<>("addressIndex"));
        tableCellAlign(jeproLabAddressIndexColumn, Pos.CENTER_RIGHT);

        TableColumn<JeproLabAddressRecord, Boolean> jeproLabAddressCheckBoxColumn = new TableColumn<>();
        jeproLabAddressCheckBoxColumn.setPrefWidth(20);
        jeproLabAddressCheckBoxColumn.setGraphic(selectAll);
        Callback<TableColumn<JeproLabAddressRecord, Boolean>, TableCell<JeproLabAddressRecord, Boolean>> checkBoxFactory = param -> new JeproLabAddressCheckBoxCellFactory();
        jeproLabAddressCheckBoxColumn.setCellFactory(checkBoxFactory);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressLastNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        jeproLabAddressLastNameColumn.setPrefWidth(0.14 * layoutWidth);
        //jeproLabAddressLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("addressLastName"));
        //tableCellAlign(jeproLabAddressLastNameColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabAddressRecord, HBox> jeproLabAddressActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabAddressActionColumn.setPrefWidth(0.08 * layoutWidth);
        Callback<TableColumn<JeproLabAddressRecord, HBox>, TableCell<JeproLabAddressRecord, HBox>> actionFactory = param -> new JeproLabAddressActionCellFactory();
        jeproLabAddressActionColumn.setCellFactory(actionFactory);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressCountryColumn = new TableColumn<>(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        jeproLabAddressCountryColumn.setPrefWidth(0.1 * layoutWidth);
        jeproLabAddressCountryColumn.setCellValueFactory(new PropertyValueFactory<>("addressCountry"));
        tableCellAlign(jeproLabAddressCountryColumn, Pos.CENTER);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressFirstNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        jeproLabAddressFirstNameColumn.setPrefWidth(0.13 * layoutWidth);
        jeproLabAddressFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("addressFirstName"));
        tableCellAlign(jeproLabAddressFirstNameColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressDetailColumn = new TableColumn<>(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        jeproLabAddressDetailColumn.setPrefWidth(0.32 * layoutWidth);
        jeproLabAddressDetailColumn.setCellValueFactory(new PropertyValueFactory<>("addressDetail"));
        tableCellAlign(jeproLabAddressDetailColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressZipCodeColumn = new TableColumn<>(bundle.getString("JEPROLAB_ZIP_CODE_LABEL"));
        jeproLabAddressZipCodeColumn.setPrefWidth(0.10 * layoutWidth);
        jeproLabAddressZipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("addressZipCode"));
        tableCellAlign(jeproLabAddressZipCodeColumn, Pos.CENTER);

        TableColumn<JeproLabAddressRecord, String> jeproLabAddressCityColumn = new TableColumn<>(bundle.getString("JEPROLAB_CITY_LABEL"));
        jeproLabAddressCityColumn.setPrefWidth(0.13 * layoutWidth);
        jeproLabAddressCityColumn.setCellValueFactory(new PropertyValueFactory<>("addressCity"));
        tableCellAlign(jeproLabAddressCityColumn, Pos.CENTER);

        jeproLabAddressesTableView.getColumns().addAll(
            jeproLabAddressIndexColumn, jeproLabAddressCheckBoxColumn, jeproLabAddressLastNameColumn, jeproLabAddressFirstNameColumn,
            jeproLabAddressZipCodeColumn, jeproLabAddressCityColumn, jeproLabAddressDetailColumn, jeproLabAddressCountryColumn,
            jeproLabAddressActionColumn
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabAddressModel> addresses;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }

                addresses = JeproLabAddressModel.getAddresses();
                return true;
            }

            @Override
            protected void succeeded(){
                updateTableView(addresses);
            }

            @Override
            protected void cancelled(){
                super.cancelled();
            }

            @Override
            protected void failed(){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }
        };

        new Thread((Task)worker).start();
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addAddressBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addAddressBtn.setOnAction(evt ->{
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
            JeproLab.getInstance().getApplicationForms().addAddressForm.controller.initializeContent();
        });
        commandWrapper.getChildren().addAll(addAddressBtn);
    }

    private void updateTableView(List<JeproLabAddressModel> addresses){
        addressList = FXCollections.observableArrayList();
        addressList.addAll(addresses.stream().map(JeproLabAddressRecord::new).collect(Collectors.toList()));

        Platform.runLater(() -> {
            double padding = 0.01 * JeproLab.APP_WIDTH;
            if(!addressList.isEmpty()){
                jeproLabAddressesPagination = new Pagination((addressList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabAddressesPagination.setPageFactory(this::createAddressPage);

                jeproLabAddressContainerWrapper.getChildren().clear();
                VBox.setMargin(jeproLabAddressSearchWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabAddressesPagination, new Insets(5, padding, 5, padding));
                jeproLabAddressContainerWrapper.getChildren().addAll(jeproLabAddressSearchWrapper, jeproLabAddressesPagination);
            }else{
                jeproLabAddressContainerWrapper.getChildren().clear();
                VBox.setMargin(jeproLabAddressSearchWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabAddressesTableView, new Insets(5, padding, 5, padding));
                jeproLabAddressContainerWrapper.getChildren().addAll(jeproLabAddressSearchWrapper, jeproLabAddressesTableView);
            }
        });
    }

    private Node createAddressPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (addressList.size()));
        jeproLabAddressesTableView.setItems(FXCollections.observableArrayList(addressList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabAddressesTableView);
    }


    public static class JeproLabAddressRecord {
        private SimpleIntegerProperty addressIndex;
        private SimpleStringProperty addressLastName, addressFirstName, addressDetail, addressZipCode, addressCity, addressCountry;

        public JeproLabAddressRecord(JeproLabAddressModel address){
            this(address, new ArrayList<>());
        }

        public JeproLabAddressRecord(JeproLabAddressModel address, List<Integer> selectedAddresses){
            addressIndex = new SimpleIntegerProperty(address.address_id);
            addressLastName = new SimpleStringProperty(address.lastname);
            addressFirstName = new SimpleStringProperty(address.firstname);
            addressDetail = new SimpleStringProperty(address.address1);
            addressZipCode = new SimpleStringProperty(address.postcode);
            addressCity = new SimpleStringProperty(address.city);
            addressCountry = new SimpleStringProperty(address.country);
        }

        public int getAddressIndex(){
            return addressIndex.get();
        }

        public String getAddressLastName() {
            return addressLastName.get();
        }

        public String getAddressFirstName() {
            return addressFirstName.get();
        }

        public String getAddressDetail() {
            return addressDetail.get();
        }

        public String getAddressZipCode() {
            return addressZipCode.get();
        }

        public String getAddressCity() {
            return addressCity.get();
        }

        public String getAddressCountry(){
            return addressCountry.get();
        }
    }

    /**
     *
     */
    public static class JeproLabAddressCheckBoxCellFactory extends TableCell<JeproLabAddressRecord, Boolean>{
        private CheckBox addressCheckBox;

        public JeproLabAddressCheckBoxCellFactory(){
            addressCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabAddressRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(addressCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    /**
     *
     */
    public static class JeproLabAddressActionCellFactory extends TableCell<JeproLabAddressRecord, HBox> {
        private Button editAddress, deleteAddress;
        private HBox commandWrapper;
        private double btnSize = 18;

        public JeproLabAddressActionCellFactory(){
            editAddress = new Button("");
            editAddress.setPrefSize(btnSize, btnSize);
            editAddress.setMinSize(btnSize, btnSize);
            editAddress.setMaxSize(btnSize, btnSize);
            editAddress.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteAddress = new Button("");
            deleteAddress.setPrefSize(btnSize, btnSize);
            deleteAddress.setMinSize(btnSize, btnSize);
            deleteAddress.setMaxSize(btnSize, btnSize);
            deleteAddress.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper = new HBox(8);
            commandWrapper.setAlignment(Pos.CENTER);
            commandWrapper.getChildren().addAll(editAddress, deleteAddress);
        }

        @Override
        public void commitEdit(HBox item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabAddressRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getAddressIndex();
                editAddress.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
                    JeproLab.getInstance().getApplicationForms().addAddressForm.controller.initializeContent(itemId);
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
