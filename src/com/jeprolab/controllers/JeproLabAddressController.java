package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAddressModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
public class JeproLabAddressController extends JeproLabController{
    private ObservableList<JeproLabAddressRecord> addressList;
    private CheckBox selectAll;
    private Button addAddressBtn;

    @FXML
    public Button jeproLabAddAddressButton, jeproLabDeleteAddressesButton;
    public TableView<JeproLabAddressRecord> jeproLabAddressesList;
    public TableColumn<JeproLabAddressRecord, String> addressIndexColumn;
    public TableColumn<JeproLabAddressRecord, Boolean> addressCheckBoxColumn;
    public TableColumn<JeproLabAddressRecord, String> addressLastNameColumn;
    public TableColumn<JeproLabAddressRecord, HBox> addressActionColumn, addressCountryColumn;
    public TableColumn<JeproLabAddressRecord, String> addressFirstNameColumn;
    public TableColumn<JeproLabAddressRecord, String> addressDetailColumn, addressZipCodeColumn, addressCityColumn;
    public JeproFormPanel jeproLabAddressContainer;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        double padding = 0.01 * JeproLab.APP_WIDTH;
        double layoutWidth = (0.98 * JeproLab.APP_WIDTH) - 59;
        selectAll = new CheckBox();

        jeproLabAddressContainer.setPrefSize(JeproLab.APP_WIDTH * 0.98, JeproLab.APP_HEIGHT - 160);
        jeproLabAddressesList.setPrefSize(JeproLab.APP_WIDTH * 0.98, JeproLab.APP_HEIGHT - 160);
        jeproLabAddressesList.setLayoutY(10);
        jeproLabAddressesList.setLayoutX(padding);

        addressIndexColumn.setText("#");
        addressIndexColumn.setPrefWidth(35);
        addressIndexColumn.setCellValueFactory(new PropertyValueFactory<>("addressIndex"));
        tableCellAlign(addressIndexColumn, Pos.CENTER_RIGHT);

        addressCheckBoxColumn.setPrefWidth(20);
        addressCheckBoxColumn.setGraphic(selectAll);
        Callback<TableColumn<JeproLabAddressRecord, Boolean>, TableCell<JeproLabAddressRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        addressCheckBoxColumn.setCellFactory(checkBoxFactory);

        addressLastNameColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        addressLastNameColumn.setPrefWidth(0.14 * layoutWidth);
        addressLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("addressLastName"));
        tableCellAlign(addressLastNameColumn, Pos.CENTER_LEFT);

        addressActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        addressActionColumn.setPrefWidth(0.08 * layoutWidth);
        Callback<TableColumn<JeproLabAddressRecord, HBox>, TableCell<JeproLabAddressRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        addressActionColumn.setCellFactory(actionFactory);

        addressCountryColumn.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        addressCountryColumn.setPrefWidth(0.1 * layoutWidth);
        addressCountryColumn.setCellValueFactory(new PropertyValueFactory<>("addressCountry"));
        tableCellAlign(addressCountryColumn, Pos.CENTER);

        addressFirstNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        addressFirstNameColumn.setPrefWidth(0.13 * layoutWidth);
        addressFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("addressFirstName"));
        tableCellAlign(addressFirstNameColumn, Pos.CENTER_LEFT);

        addressDetailColumn.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        addressDetailColumn.setPrefWidth(0.32 * layoutWidth);
        addressDetailColumn.setCellValueFactory(new PropertyValueFactory<>("addressDetail"));
        tableCellAlign(addressDetailColumn, Pos.CENTER_LEFT);

        addressZipCodeColumn.setText(bundle.getString("JEPROLAB_ZIP_CODE_LABEL"));
        addressZipCodeColumn.setPrefWidth(0.10 * layoutWidth);
        addressZipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("addressZipCode"));
        tableCellAlign(addressZipCodeColumn, Pos.CENTER);

        addressCityColumn.setText(bundle.getString("JEPROLAB_CITY_LABEL"));
        addressCityColumn.setPrefWidth(0.13 * layoutWidth);
        addressCityColumn.setCellValueFactory(new PropertyValueFactory<>("addressCity"));
        tableCellAlign(addressCityColumn, Pos.CENTER);

    }

    @Override
    public void initializeContent(){
        List<JeproLabAddressModel> analyzes = JeproLabAddressModel.getAddresses();
        ObservableList<JeproLabAddressRecord> analyzeList = FXCollections.observableArrayList();
        if(!analyzes.isEmpty()){
            analyzeList.addAll(analyzes.stream().map(JeproLabAddressRecord::new).collect(Collectors.toList()));
            jeproLabAddressesList.setItems(analyzeList);
        }
        updateToolBar();
        //addressList = FXCollections.observableArrayList();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addAddressBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addAddressBtn.setOnAction(evt ->{
            try {
                JeproLab.request.getRequest().clear();
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
                JeproLabContext.getContext().controller.initializeContent();
            }catch (IOException ignored){
                ignored.printStackTrace();
            }
        });
        commandWrapper.getChildren().addAll(addAddressBtn);
    }

    public static class JeproLabAddressRecord {
        private SimpleIntegerProperty addressIndex;
        private SimpleStringProperty addressLastName, addressFirstName, addressDetail, addressZipCode, addressCity, addressCountry;

        public JeproLabAddressRecord(JeproLabAddressModel address){
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
    private static class JeproLabCheckBoxCell extends TableCell<JeproLabAddressRecord, Boolean>{
        private CheckBox addressCheckBox;

        public JeproLabCheckBoxCell(){
            addressCheckBox = new CheckBox();
        }

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
    private static class JeproLabActionCell extends TableCell<JeproLabAddressRecord, HBox>{
        private Button editAddress, deleteAddress;
        private HBox commandWrapper;
        private double btnSize = 18;

        public JeproLabActionCell(){
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

        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabAddressRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getAddressIndex();
                editAddress.setOnAction(event -> {
                    JeproLab.request.setRequest("address_id=" + itemId);
                    try{
                        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
                        JeproLabContext.getContext().controller.initializeContent();
                    }catch (IOException ignored){
                        ignored.printStackTrace();
                    }
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}