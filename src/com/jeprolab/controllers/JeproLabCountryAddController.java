package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

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
    public JeproFormPanelContainer countryFormContainerWrapper;
    public JeproFormPanelTitle countryFormTitleWrapper;
    public JeproFormPanel countryFormPanelWrapper;
    public GridPane jeproLabAddCountryFormLayout;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 200;
        double inputColumnWidth = 270;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        countryFormPanelWrapper.setLayoutX(posX);
        countryFormPanelWrapper.setLayoutY(posY);
        countryFormContainerWrapper.setPrefWidth(formWidth);
        countryFormContainerWrapper.setLayoutY(40);
        countryFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabAddCountryFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );

        formTitleLabel = new Label(bundle.getString("JEPROLAB_ADD_NEW_COUNTRY_LABEL"));
        countryFormTitleWrapper.getChildren().add(formTitleLabel);
        formTitleLabel.getStyleClass().add("form-title");
        countryZoneLabel.setText(bundle.getString("JEPROLAB_COUNTRY_ZONE_LABEL"));
        countryZoneLabel.getStyleClass().add("input-label");
        defaultCurrencyLabel.setText(bundle.getString("JEPROLAB_DEFAULT_CURRENCY_LABEL"));
        defaultCurrencyLabel.getStyleClass().add("input-label");
        zipCodeFormatLabel.setText(bundle.getString("JEPROLAB_ZIP_CODE_FORMAT_LABEL"));
        zipCodeFormatLabel.getStyleClass().add("input-label");
        addressLayoutFormatLabel.setText(bundle.getString("JEPROLAB_ADDRESS_LAYOUT_FORMAT_LABEL"));
        addressLayoutFormatLabel.getStyleClass().add("input-label");
        needZipCodeLabel.setText(bundle.getString("JEPROLAB_NEED_ZIP_CODE_LABEL"));
        needZipCodeLabel.getStyleClass().add("input-label");
        publishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        publishedLabel.getStyleClass().add("input-label");
        displayTaxLabelLabel.setText(bundle.getString("JEPROLAB_DISPLAY_TAX_LABEL"));
        displayTaxLabelLabel.getStyleClass().add("input-label");
        needIdentificationNumberLabel.setText(bundle.getString("JEPROLAB_NEED_IDENTIFICATION_NUMBER_LABEL"));
        needIdentificationNumberLabel.getStyleClass().add("input-label");
        containsStatesLabel.setText(bundle.getString("JEPROLAB_CONTAINS_STATES_LABEL"));
        containsStatesLabel.getStyleClass().add("input-label");
        callPrefixLabel.setText(bundle.getString("JEPROLAB_CALL_PREFIX_LABEL"));
        callPrefixLabel.getStyleClass().add("input-label");
        isoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        isoCodeLabel.getStyleClass().add("input-label");
        countryNameLabel.setText(bundle.getString("JEPROLAB_COUNTRY_NAME_LABEL"));
        countryNameLabel.getStyleClass().add("input-label");
        /*needZipCode, published, displayTaxLabel, needIdentificationNumber, containsStates;
        isoCode, callPrefix, zipCodeFormat;
        public TextArea addressLayoutFormat;
        public ComboBox countryZone, defaultCurrency;
        public MultiLangTextField countryName; */
        saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));

        /**
         * GridPane styling
         */
        GridPane.setMargin(countryZoneLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(countryZoneLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(defaultCurrencyLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(zipCodeFormatLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(needIdentificationNumberLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(containsStatesLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(callPrefixLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(isoCodeLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(countryNameLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(addressLayoutFormatLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(publishedLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(needZipCodeLabel, new Insets(10, 0, 10, 15));
        //GridPane.setMargin(displayTaxLabel, new Insets(10, 0, 10, 15));
    }

    @Override
    public void updateToolBar(){}
}