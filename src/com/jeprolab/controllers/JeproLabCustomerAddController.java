package com.jeprolab.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import jeprolab.assets.extend.controls.EmailField;
import jeprolab.assets.extend.controls.PhoneField;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCustomerAddController extends JeproLabController {
    private ResourceBundle bundle;

    @FXML
    public Label customerTitleLabel, customerFirstNameLabel, customerLastNameLabel, customerBusinessInternetLabel;
    public Label customerBusinessPhoneLabel, customerPhoneLabel, customerMobilePhoneLabel, customerEmailLabel;
    public Label customerWebsiteLabel, customerZoneLabel, customerCountryLabel, customerStateLabel, customerCityLabel;
    public Label customerAddressDetailLabel, customerAddressNumberLabel, customerAddressStreetNameLabel;
    public TextField customerAddressStreetName;
    public TextField customerFirstName, customerLastName, customerWebsite, customerState, customerCity, customerAddressNumber;
    public PhoneField customerPhone, customerMobilePhone;
    public ComboBox<Pair<String, String>> customerTitle;
    public ComboBox customerZone, customerCountry, customerStreetType;
    public CheckBox customerNewsLetter, customerAllowAds;
    public EmailField customerEmail;
    public Button saveButton, cancelButton;
    public ImageView customerImageView;


    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        GridPane.setMargin(customerLastNameLabel, new Insets(5, 30, 0, 0));
        customerTitleLabel.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitle.getItems().addAll(new Pair<>("mr", bundle.getString("JEPROLAB_MR_LABEL")),
                new Pair<>("mrs", bundle.getString("JEPROLAB_MRS_LABEL")),
                new Pair<>("miss", bundle.getString("JEPROLAB_MISS_LABEL")));
        customerFirstNameLabel.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerLastNameLabel.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerBusinessInternetLabel.setText(bundle.getString("JEPROLAB_BUSINESS_INTERNET_LABEL"));
        customerBusinessPhoneLabel.setText(bundle.getString("JEPROLAB_BUSINESS_PHONE_LABEL"));
        customerPhoneLabel.setText(bundle.getString("JEPROLAB_PHONE_LABEL"));
        customerMobilePhoneLabel.setText(bundle.getString("JEPROLAB_MOBILE_PHONE_LABEL"));
        customerEmailLabel.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerWebsiteLabel.setText(bundle.getString("JEPROLAB_WEBSITE_LABEL"));
        customerZoneLabel.setText(bundle.getString("JEPROLAB_ZONE_LABEL"));
        customerCountryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        customerStateLabel.setText(bundle.getString("JEPROLAB_STATE_LABEL"));
        customerCityLabel.setText(bundle.getString("JEPROLAB_CITY_LABEL"));
        customerAddressDetailLabel.setText(bundle.getString("JEPROLAB_ADDRESS_DETAIL_LABEL"));
        customerAddressNumberLabel.setText(bundle.getString("JEPROLAB_ADDRESS_NUMBER_LABEL"));
        customerAddressStreetNameLabel.setText(bundle.getString("JEPROLAB_STREET_NAME_LABEL"));
        customerNewsLetter.setText(bundle.getString("JEPROLAB_ADD_CUSTOMER_TO_NEWS_LETTER_LABEL"));
        customerAllowAds.setText(bundle.getString("JEPROLAB_RECEIVE_ADS_FROM_PARTNERS_LABEL"));
        saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
    }

    public void handleSaveButton(ActionEvent actionEvent) {
    }

    public void handleCancelButton(ActionEvent actionEvent) {
    }

    @Override
    public void updateToolBar(){}
}