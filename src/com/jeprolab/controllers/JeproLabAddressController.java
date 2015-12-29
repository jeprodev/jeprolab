package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabAddressModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;

public class JeproLabAddressController extends JeproLabController {
    private ObservableList<JeproLabAddressRecord> addressList;

    @FXML
    public Button jeproLabAddAddressButton, jeproLabDeleteAddressesButton;
    public TableView<JeproLabAddressRecord> jeproLabAddressesList;
    public TableColumn<JeproLabAddressRecord, String> addressIndexColumn;
    public TableColumn addressCheckBoxColumn, addressLastColumn, addressActionColumn, addressCountryColumn;
    public TableColumn addressFirstNameColumn, addressDetailColumn, addressZipCodeColumn, addressCityColumn;
    public AnchorPane jeproLabAddressContainer;

    public void initialize(URL location, ResourceBundle resourceBundle){
        bundle = resourceBundle;
        double padding = 0.01 * JeproLab.APP_WIDTH;
        double layoutWidth = (0.98 * JeproLab.APP_WIDTH) - 160;
        jeproLabAddAddressButton = new Button(bundle.getString("JEPROLAB_ADD_LABEL"));
        jeproLabDeleteAddressesButton = new Button(bundle.getString("JEPROLAB_DELETE_LABEL"));
        jeproLabAddressContainer.setPrefSize(JeproLab.APP_WIDTH * 0.98, JeproLab.APP_HEIGHT - 160);
        jeproLabAddressesList.setPrefSize(JeproLab.APP_WIDTH * 0.98, JeproLab.APP_HEIGHT - 160);
        jeproLabAddressesList.setLayoutY(10);
        jeproLabAddressesList.setLayoutX(padding);
        //jeproLabAddressContainer.setPadding(new Insets(10, padding, 5, padding));
        addressIndexColumn.setText("#");
        addressIndexColumn.setPrefWidth(34);
        //addressIndexColumn.setCellFactory(new PropertyValueFactory<JeproLabAddressRecord, String>("addressIndex"));
        //addressCheckBoxColumn.setText(bundle.getString("JEPROLAB_")); // replace with check box
        addressCheckBoxColumn.setPrefWidth(24);
        addressLastColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        addressLastColumn.setPrefWidth(0.2 * layoutWidth);
        //addressLastColumn.
        addressActionColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        addressActionColumn.setPrefWidth(100);
        addressCountryColumn.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        addressCountryColumn.setPrefWidth(0.1 * layoutWidth);
        addressFirstNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        addressFirstNameColumn.setPrefWidth(0.2 * layoutWidth);
        addressDetailColumn.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        addressDetailColumn.setPrefWidth(0.25 * layoutWidth);
        addressZipCodeColumn.setText(bundle.getString("JEPROLAB_ZIP_CODE_LABEL"));
        addressZipCodeColumn.setPrefWidth(0.10 * layoutWidth);
        addressCityColumn.setText(bundle.getString("JEPROLAB_CITY_LABEL"));
        addressCityColumn.setPrefWidth(0.15 * layoutWidth);

        addressList = FXCollections.observableArrayList();

        /**
         * add list command in toolBar
         */

        /**
         * Retrieve items for the list
         */
        ResultSet results = JeproLabAddressModel.getAddressList();
        try{
            int index = 0;
            JeproLabAddressRecord addressRecord;

            while(results.next()){
                addressRecord = new JeproLabAddressRecord(results, index);
                addressList.add(index, addressRecord);
                index++;
            }
        }catch (SQLException e){

        }
        System.out.println(addressList);
        this.jeproLabAddressesList.setItems(addressList);
    }

    @Override
    public void updateToolBar(){
        ToolBar appToolBar = JeproLab.getInstance().getApplicationToolBar();
        for(Node child : appToolBar.getItems()){
            //appToolBar.remo
        }
    }

    private static class JeproLabAddressRecord {
        private SimpleIntegerProperty addressIndex;
        private SimpleStringProperty addressLastName, addressFirstName, addressDetail, addressCity, addressCountry;
        private SimpleStringProperty addressZipCode;

        public JeproLabAddressRecord(ResultSet addressItem, int index){
            try{
                this.addressIndex = new SimpleIntegerProperty(index);
                this.addressFirstName = new SimpleStringProperty(addressItem.getString("firstname"));
                this.addressLastName = new SimpleStringProperty(addressItem.getString("lastname"));
                this.addressDetail = new SimpleStringProperty(addressItem.getString("address1"));
                this.addressCity = new SimpleStringProperty(addressItem.getString("city"));
                this.addressZipCode = new SimpleStringProperty(addressItem.getString("postcode"));
                this.addressCountry = new SimpleStringProperty(addressItem.getString("country"));
            }catch(SQLException err){

            }
        }

        public int getAddressIndex(){
            return addressIndex.get();
        }

        public String getAddressLastName(){
            return addressLastName.get();
        }

        public String getAddressFirstName(){
            return addressFirstName.get();
        }

        public String getAddressDetail(){
            return addressDetail.get();
        }

        public String getAddressCity(){
            return addressCity.get();
        }

        public String getAddressCountry(){
            return addressCountry.get();
        }

        public String getAddressZipCode(){
            return addressZipCode.get();
        }
    }
}