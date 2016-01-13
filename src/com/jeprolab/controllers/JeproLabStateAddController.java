package com.jeprolab.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabStateAddController extends JeproLabController{
    @FXML
    public Label stateNameLabel, isoCodeLabel, countryZoneLabel, countryLabel, publishedLabel, formTitleLabel;
    public TextField stateName, isoCode;
    public ComboBox countryZone, country;
    public JeproSwitchButton published;
    public Button saveButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        stateNameLabel.setText(bundle.getString("JEPROLAB_STATE_NAME_LABEL"));
        isoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        countryZoneLabel.setText(bundle.getString("JEPROLAB_COUNTRY_ZONE_LABEL"));
        countryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        publishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_STATE_LABEL"));
        saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
    }

    @Override
    public void updateToolBar(){}
}