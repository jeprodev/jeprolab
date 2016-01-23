package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproPhoneField;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabZoneModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import jdk.internal.cmm.SystemResourcePressureImpl;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JeproLabAddressAddController extends JeproLabController{
    @FXML
    public JeproFormPanelTitle jeproLabAddAddressFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddAddressFormContainerWrapper;
    public JeproFormPanel jeproLabAddAddressFormWrapper;
    public GridPane jeproLabAddAddressFormLayout;

    public Label jeproLabAddressFormTitle, jeproLabAddressCustomerLabel, jeproLabAddressCompanyLabel, jeproLabAddressLastNameLabel;
    public Label jeproLabAddressFirstNameLabel, jeproLabAddressVatNumberLabel, jeproLabAddressAddressLabel, jeproLabAddressAddress1Label;
    public Label jeproLabAddressPostCodeLabel, jeproLabAddressCityLabel, jeproLabAddressCountryLabel, jeproLabAddressOtherLabel;
    public Label jeproLabAddressMobilePhoneLabel, jeproLabAddressPhoneLabel;

    public TextField jeproLabAddressCompany, jeproLabAddressLastName, jeproLabAddressFirstName, jeproLabAddressVatNumber;
    public TextField jeproLabAddressAddress1, jeproLabAddressAddress, jeproLabAddressPostCode, jeproLabAddressCity;

    public TextArea jeproLabAddressOther;

    public JeproPhoneField jeproLabAddressPhone, jeproLabAddressMobilePhone;

    public HBox jeproLabAddressCountryWrapper;

    public ComboBox jeproLabAddressCountryZone, jeproLabAddressCountry;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;
        jeproLabAddressFormTitle = new Label(bundle.getString("JEPROLAB_ADD_ADDRESS_LABEL"));
        jeproLabAddAddressFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabAddAddressFormTitleWrapper.getChildren().add(jeproLabAddressFormTitle);
        jeproLabAddressFormTitle.getStyleClass().add("form-title");
        jeproLabAddressFormTitle.setPrefWidth(formWidth);
        jeproLabAddressFormTitle.setTextAlignment(TextAlignment.CENTER);
        jeproLabAddressFormTitle.setLayoutY(8);

        jeproLabAddAddressFormWrapper.setLayoutX(posX);
        jeproLabAddAddressFormWrapper.setLayoutY(posY);
        jeproLabAddAddressFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddAddressFormContainerWrapper.setLayoutY(40);
        jeproLabAddAddressFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );

        /**
         * GridPane styling
         */
        GridPane.setMargin(jeproLabAddressCustomerLabel, new Insets(5, 0, 15, 15));
        //GridPane.setMargin(jeproLabAddressCustomer, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressCompanyLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressCompany, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressLastNameLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressLastName, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressFirstNameLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressFirstName, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressVatNumberLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressVatNumber, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressAddressLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressAddress, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressAddress1Label, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressAddress1, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressPostCodeLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressPostCode, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressCityLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressCity, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressOtherLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressCountryLabel, new Insets(5, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressCountryZone, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressCountry, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabAddressMobilePhoneLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabAddressPhoneLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabAddressOther,  new Insets(5, 0, 25, 0));
        GridPane.setValignment(jeproLabAddressOtherLabel, VPos.TOP);

        jeproLabAddressCustomerLabel.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabAddressCustomerLabel.getStyleClass().add("input-label");
        jeproLabAddressCompanyLabel.setText(bundle.getString("JEPROLAB_COMPANY_LABEL"));
        jeproLabAddressCompanyLabel.getStyleClass().add("input-label");
        jeproLabAddressLastNameLabel.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        jeproLabAddressLastNameLabel.getStyleClass().add("input-label");
        jeproLabAddressFirstNameLabel.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        jeproLabAddressFirstNameLabel.getStyleClass().add("input-label");
        jeproLabAddressVatNumberLabel.setText(bundle.getString("JEPROLAB_VAT_NUMBER_LABEL"));
        jeproLabAddressVatNumberLabel.getStyleClass().add("input-label");
        jeproLabAddressAddressLabel.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        jeproLabAddressAddressLabel.getStyleClass().add("input-label");
        jeproLabAddressAddress1Label.setText(bundle.getString("JEPROLAB_ADDRESS_1_LABEL"));
        jeproLabAddressAddress1Label.getStyleClass().add("input-label");
        jeproLabAddressPostCodeLabel.setText(bundle.getString("JEPROLAB_ZIP_CODE_LABEL"));
        jeproLabAddressPostCodeLabel.getStyleClass().add("input-label");
        jeproLabAddressCityLabel.setText(bundle.getString("JEPROLAB_CITY_LABEL"));
        jeproLabAddressCityLabel.getStyleClass().add("input-label");
        jeproLabAddressCountryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        jeproLabAddressCountryLabel.getStyleClass().add("input-label");
        jeproLabAddressOtherLabel.setText(bundle.getString("JEPROLAB_OTHER_LABEL"));
        jeproLabAddressOtherLabel.getStyleClass().add("input-label");
        jeproLabAddressMobilePhoneLabel.setText(bundle.getString("JEPROLAB_MOBILE_PHONE_LABEL"));
        jeproLabAddressMobilePhoneLabel.getStyleClass().add("input-label");
        jeproLabAddressPhoneLabel.setText(bundle.getString("JEPROLAB_PHONE_LABEL"));
        jeproLabAddressPhoneLabel.getStyleClass().add("input-label");
        //jeproLabAddressCustomer);

        JeproLabContext context = JeproLabContext.getContext();

        List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountries(context.language.language_id, true);
        List<JeproLabZoneModel> zones = JeproLabZoneModel.getZones(true);
        jeproLabAddressCountry.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabCountryModel country : countries){
            jeproLabAddressCountry.getItems().add(country.name.get("lang_1"));
        }

        jeproLabAddressCountryZone.setPrefWidth(120);
        jeproLabAddressCountryZone.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabZoneModel zone : zones) {
            jeproLabAddressCountryZone.getItems().add(zone.name);
        }

    }

    @Override
    public void updateToolBar(){}
}