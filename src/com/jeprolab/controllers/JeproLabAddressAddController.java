package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabCountryModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabAddressAddController extends JeproLabController {
    private Button cancelBtn, saveAddressBtn;
    private JeproLabAddressModel address;
    @FXML
    public JeproFormPanelTitle jeproLabAddAddressFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddAddressFormContainerWrapper;
    public JeproFormPanel jeproLabAddAddressFormWrapper;
    public JeproCustomerField jeproLabAddressCustomer;
    public GridPane jeproLabAddAddressFormLayout;

    public Label jeproLabAddressCustomerLabel, jeproLabAddressCompanyLabel, jeproLabAddressLastNameLabel;
    public Label jeproLabAddressFirstNameLabel, jeproLabAddressVatNumberLabel, jeproLabAddressAddressLabel, jeproLabAddressAddress1Label;
    public Label jeproLabAddressPostCodeLabel, jeproLabAddressCityLabel, jeproLabAddressCountryLabel, jeproLabAddressOtherLabel;
    public Label jeproLabAddressMobilePhoneLabel, jeproLabAddressPhoneLabel;// jeproLabAddressLabel, jeproLabAddressLabel;

    public TextField jeproLabAddressCompany, jeproLabAddressLastName, jeproLabAddressFirstName, jeproLabAddressVatNumber;
    public TextField jeproLabAddressAddress1, jeproLabAddressAddress, jeproLabAddressPostCode, jeproLabAddressCity;

    public TextArea jeproLabAddressOther;
    public JeproPhoneField jeproLabAddressPhone, jeproLabAddressMobilePhone;
    public HBox jeproLabAddressCountryWrapper;
    public ComboBox<String> jeproLabAddressCountryZone, jeproLabAddressCountry;

    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;

        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 15;
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_ADDRESS_LABEL"));
        jeproLabAddAddressFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabAddAddressFormTitleWrapper.getChildren().add(formTitleLabel);
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setPrefSize(formWidth, 40);
        formTitleLabel.setAlignment(Pos.CENTER);

        jeproLabAddAddressFormWrapper.setLayoutX(posX);
        jeproLabAddAddressFormWrapper.setLayoutY(posY);
        jeproLabAddAddressFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddAddressFormContainerWrapper.setLayoutY(40);
        jeproLabAddAddressFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25),
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25)
        );

        jeproLabAddressCountryZone.setMinWidth(150);
        jeproLabAddressCountry.setMinWidth(270);

        /**
         * GridPane styling
         */
        GridPane.setMargin(jeproLabAddressCustomerLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(jeproLabAddressCustomer, new Insets(15, 0, 15, 0));
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
        context.controller = this;
    }

    @Override
    public void initializeContent(){
        JeproLabContext context = JeproLabContext.getContext();
        address = null;
        loadAddress(true);
        List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountries(context.language.language_id, true);
        List<JeproLabCountryModel.JeproLabZoneModel> zones = JeproLabCountryModel.JeproLabZoneModel.getZones(true);
        jeproLabAddressCountry.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        int zoneId = 0;
        for(JeproLabCountryModel country : countries){
            jeproLabAddressCountry.getItems().add(country.name.get("lang_" + context.language.language_id));
            if(address.country_id == country.country_id){
                zoneId = country.zone_id;
                jeproLabAddressCountry.setValue(country.name.get("lang_" + context.language.language_id));
            }
        }

        jeproLabAddressCountryZone.setPrefWidth(120);
        jeproLabAddressCountryZone.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabCountryModel.JeproLabZoneModel zone : zones) {
            jeproLabAddressCountryZone.getItems().add(zone.name);
            if(zone.zone_id == zoneId){
                jeproLabAddressCountryZone.setValue(zone.name);
            }
        }

        if(address.address_id > 0){
            jeproLabAddressCompany.setText(address.company);
            jeproLabAddressLastName.setText(address.lastname);
            jeproLabAddressFirstName.setText(address.firstname);
            jeproLabAddressVatNumber.setText(address.vat_number);
            jeproLabAddressAddress1.setText(address.address2);
            jeproLabAddressAddress.setText(address.address1);
            jeproLabAddressPostCode.setText(address.postcode);
            jeproLabAddressCity.setText(address.city);
            jeproLabAddressPhone.setText(address.phone);
            jeproLabAddressMobilePhone.setText(address.mobile_phone);
            jeproLabAddressOther.setText(address.other);
        }

        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveAddressBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (address.address_id > 0) {
            saveAddressBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveAddressBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveAddressBtn, cancelBtn);
        addCommandListener();
    }

    private void addCommandListener(){
        saveAddressBtn.setOnMouseClicked(evt -> {
            int countryId = JeproLabCountryModel.getCountryIdByCountryName(jeproLabAddressCountry.getValue());
            String request = "company=" + jeproLabAddressCompany.getText() + "&lastname=" + jeproLabAddressLastName.getText() + "&firstname=" + jeproLabAddressFirstName.getText();
            request += "&vatnumber=" + jeproLabAddressVatNumber.getText() + "&country_id=" + countryId ;

            JeproLab.request.setPost(request);

            //label.setText("Selected: " + listView.getSelectionModel().getSelectedItems());
            if(address.address_id > 0){
                address.update();
            }else{
                address.add();
            }
        });

        cancelBtn.setOnMouseClicked(evt -> {
            /*label.setText("Selected: " +
                listView.getSelectionModel().getSelectedItems()); */
        });

        jeproLabAddressCountryZone.valueProperty().addListener((observable, oldValue, newValue) -> {
            int zoneId = JeproLabCountryModel.JeproLabZoneModel.getZoneIdByName(newValue);
            if(context == null){
                context = JeproLabContext.getContext();
            }
            List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountriesByZoneId(zoneId, context.language.language_id);
            jeproLabAddressCountry.getItems().clear();
            for(JeproLabCountryModel country : countries){
                jeproLabAddressCountry.getItems().addAll(country.name.get("lang_" + context.language.language_id));
            }
        });
    }

    private void loadAddress(boolean option){
        int addressId = JeproLab.request.getRequest().containsKey("address_id") ? Integer.parseInt(JeproLab.request.getRequest().get("address_id")) : 0;
        if(addressId > 0){
            if(this.address == null){
                this.address = new JeproLabAddressModel(addressId);
            }
        }else if(option){
            if(this.address == null){
                this.address = new JeproLabAddressModel();
            }
        }else{
            this.address = new JeproLabAddressModel();
        }
    }
}
