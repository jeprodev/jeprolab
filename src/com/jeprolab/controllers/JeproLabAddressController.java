package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabAddressController extends JeproLabController {
    @FXML
    public HBox jeproLabAddressListCommandWrapper;
    public Button jeproLabAddAddressButton, jeproLabDeleteAddressesButton;
    public TableColumn addressIndexColumn, addressCheckBoxColumn, addressLastColumn, addressActionColumn, addressCountryColumn;
    public TableColumn addressFirstNameColumn, addressDetailColumn, addressZipCodeColumn, addressCityColumn;


    public void initialize(URL location, ResourceBundle resourceBundle){
        bundle = resourceBundle;
        /*jeproLabAddAddressButton.setText(bundle.getString("JEPROLAB_ADD_LABEL"));
        jeproLabDeleteAddressesButton.setText(bundle.getString("JEPROLAB_DELETE_LABEL"));
        addressIndexColumn.setText("#");
        addressCheckBoxColumn.setText(bundle.getString("JEPROLAB_")); // replace with check box
        addressLastColumn.setText(bundle.getString("JEPROLAB_"));
        addressActionColumn.setText(bundle.getString("JEPROLAB_"));
        addressCountryColumn.setText(bundle.getString("JEPROLAB_"));
        addressFirstNameColumn.setText(bundle.getString("JEPROLAB_"));
        addressDetailColumn.setText(bundle.getString("JEPROLAB_"));
        addressZipCodeColumn.setText(bundle.getString("JEPROLAB_"));
        addressCityColumn.setText(bundle.getString("JEPROLAB_CITY_LABEL")); */
    }

    @Override
    public void updateToolBar(){}
}