package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.jeprolab.assets.extend.controls.JeproSwitchButton;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCurrencyAddController extends JeproLabController {
    @FXML
    public Label currencyNameLabel, isoCodeLabel, numericIsoCodeLabel, currencySymbolLabel, conversionRateLabel, currencyFormatLabel, hasDecimalsLabel, spacingLabel, publishedLabel, formTitleLabel;
    public TextField currencyName, isoCode, numericIsoCode, currencySymbol, conversionRate;
    public ComboBox currencyFormat;
    public JeproSwitchButton hasDecimals, hasSpacing, published;
    public Button saveButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        currencyNameLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        isoCodeLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        numericIsoCodeLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        currencySymbolLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        conversionRateLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        currencyFormatLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        hasDecimalsLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        spacingLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        publishedLabel.setText(bundle.getString("JEPROLAB_LABEL"));
    }

    @Override
    public void updateToolBar(){}
}