package com.jeprolab.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.JeproSwitchButton;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCountryAddController extends JeproLabController{
    @FXML
    public Label formTitleLabel, countryZoneLabel, defaultCurrencyLabel, zipCodeFormatLabel, addressLayoutFormatLabel;
    public Label needZipCodeLabel, publishedLabel, displayTaxLabelLabel, needIdentificationNumberLabel, containsStatesLabel;
    public Label callPrefixLabel, isoCodeLabel, countryNameLabel;
    public JeproSwitchButton needZipCode, published, displayTaxLabel, needIdentificationNumber, containsStates;
    public TextField isoCode, callPrefix, zipCodeFormat;
    public TextArea addressLayoutFormat;
    public ComboBox countryZone, defaultCurrency;
    public JeproMultiLangTextField countryName;
    public Button saveButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_COUNTRY_LABEL"));
        countryZoneLabel.setText(bundle.getString("JEPROLAB_COUNTRY_ZONE_LABEL"));
        defaultCurrencyLabel.setText(bundle.getString("JEPROLAB_DEFAULT_CURRENCY_LABEL"));
        zipCodeFormatLabel.setText(bundle.getString("JEPROLAB_ZIP_CODE_FORMAT_LABEL"));
        addressLayoutFormatLabel.setText(bundle.getString("JEPROLAB_ADDRESS_LAYOUT_FORMAT_LABEL"));
        needZipCodeLabel.setText(bundle.getString("JEPROLAB_NEED_ZIP_CODE_LABEL"));
        publishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        displayTaxLabelLabel.setText(bundle.getString("JEPROLAB_DISPLAY_TAX_LABEL"));
        needIdentificationNumberLabel.setText(bundle.getString("JEPROLAB_NEED_IDENTIFICATION_NUMBER_LABEL"));
        containsStatesLabel.setText(bundle.getString("JEPROLAB_CONTAINS_STATES_LABEL"));
        callPrefixLabel.setText(bundle.getString("JEPROLAB_CALL_PREFIX_LABEL"));
        isoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        countryNameLabel.setText(bundle.getString("JEPROLAB_COUNTRY_NAME_LABEL"));
        /*needZipCode, published, displayTaxLabel, needIdentificationNumber, containsStates;
        isoCode, callPrefix, zipCodeFormat;
        public TextArea addressLayoutFormat;
        public ComboBox countryZone, defaultCurrency;
        public MultiLangTextField countryName; */
        saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
    }

    @Override
    public void updateToolBar(){}
}