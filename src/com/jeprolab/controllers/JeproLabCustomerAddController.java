package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.models.JeproLabCustomerModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCustomerAddController extends JeproLabController {
    private ResourceBundle bundle;
    private Label jeproLabAddCustomerFormTitle;
    private Button saveButton, cancelButton;
    private JeproLabCustomerModel customer;

    @FXML
    public Label customerTitleLabel, customerFirstNameLabel, customerLastNameLabel, customerBusinessInternetLabel;
    public Label customerBusinessPhoneLabel, customerPhoneLabel, customerMobilePhoneLabel, customerEmailLabel;
    public Label customerWebsiteLabel, customerZoneLabel, customerCountryLabel, customerStateLabel, customerCityLabel;
    public Label customerAddressDetailLabel, customerAddressNumberLabel, customerAddressStreetNameLabel;
    public TextField customerAddressStreetName;
    public TextField customerFirstName, customerLastName, customerWebsite, customerState, customerCity, customerAddressNumber;
    public JeproPhoneField customerPhone, customerMobilePhone;
    public ComboBox<String> customerTitle;
    public ComboBox customerZone, customerCountry, customerStreetType;
    public CheckBox customerNewsLetter, customerAllowAds;
    public JeproEmailField customerEmail;
    //public Button saveButton, cancelButton;
    public ImageView customerImageView;
    public GridPane jeproLabAddCustomerFormLayout;

    public JeproFormPanel jeproLabAddCustomerFormWrapper;
    public JeproFormPanelTitle jeproLabAddCustomerFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddCustomerFormContainerWrapper;


    public void initialize(URL location , ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 180;
        double inputColumnWidth = 250;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth))/2;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabAddCustomerFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_CUSTOMER_LABEL"));
        jeproLabAddCustomerFormTitle.getStyleClass().add("form-title");
        jeproLabAddCustomerFormTitle.setPrefWidth(formWidth);
        jeproLabAddCustomerFormTitle.setTextAlignment(TextAlignment.CENTER);
        jeproLabAddCustomerFormTitleWrapper.getChildren().add(jeproLabAddCustomerFormTitle);
        jeproLabAddCustomerFormTitleWrapper.setPrefSize(formWidth, 40);
        //jeproLabAddCustomerFormTitleWrapper

        jeproLabAddCustomerFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddCustomerFormContainerWrapper.setLayoutY(40);


        jeproLabAddCustomerFormWrapper.setPrefWidth(formWidth);
        jeproLabAddCustomerFormWrapper.setLayoutX(posX);
        jeproLabAddCustomerFormWrapper.setLayoutY(posY);

        jeproLabAddCustomerFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );
        jeproLabAddCustomerFormLayout.setLayoutX(20);


        customerTitleLabel.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitleLabel.getStyleClass().add("input-label");
        customerTitle.getItems().addAll(
             bundle.getString("JEPROLAB_MR_LABEL"), bundle.getString("JEPROLAB_MRS_LABEL"), bundle.getString("JEPROLAB_MISS_LABEL")
        );

        customerFirstNameLabel.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerFirstNameLabel.getStyleClass().add("input-label");
        customerLastNameLabel.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerLastNameLabel.getStyleClass().add("input-label");
        customerBusinessInternetLabel.setText(bundle.getString("JEPROLAB_BUSINESS_INTERNET_LABEL"));
        customerBusinessInternetLabel.getStyleClass().add("input-label");
        customerBusinessPhoneLabel.setText(bundle.getString("JEPROLAB_BUSINESS_PHONE_LABEL"));
        customerBusinessPhoneLabel.getStyleClass().add("input-label");
        customerPhoneLabel.setText(bundle.getString("JEPROLAB_PHONE_LABEL"));
        customerPhoneLabel.getStyleClass().add("input-label");
        customerMobilePhoneLabel.setText(bundle.getString("JEPROLAB_MOBILE_PHONE_LABEL"));
        customerMobilePhoneLabel.getStyleClass().add("input-label");
        customerEmailLabel.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerEmailLabel.getStyleClass().add("input-label");
        customerWebsiteLabel.setText(bundle.getString("JEPROLAB_WEBSITE_LABEL"));
        customerWebsiteLabel.getStyleClass().add("input-label");
        customerZoneLabel.setText(bundle.getString("JEPROLAB_ZONE_LABEL"));
        customerZoneLabel.getStyleClass().add("input-label");
        customerCountryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        customerCountryLabel.getStyleClass().add("input-label");
        customerStateLabel.setText(bundle.getString("JEPROLAB_STATE_LABEL"));
        customerStateLabel.getStyleClass().add("input-label");
        customerCityLabel.setText(bundle.getString("JEPROLAB_CITY_LABEL"));
        customerCityLabel.getStyleClass().add("input-label");
        customerAddressDetailLabel.setText(bundle.getString("JEPROLAB_ADDRESS_DETAIL_LABEL"));
        customerAddressDetailLabel.getStyleClass().add("input-label");
        customerAddressNumberLabel.setText(bundle.getString("JEPROLAB_ADDRESS_NUMBER_LABEL"));
        customerAddressNumberLabel.getStyleClass().add("input-label");
        customerAddressStreetNameLabel.setText(bundle.getString("JEPROLAB_STREET_NAME_LABEL"));
        customerAddressStreetNameLabel.getStyleClass().add("input-label");
        customerNewsLetter.setText(bundle.getString("JEPROLAB_ADD_CUSTOMER_TO_NEWS_LETTER_LABEL"));
        customerAllowAds.setText(bundle.getString("JEPROLAB_RECEIVE_ADS_FROM_PARTNERS_LABEL"));
        /*saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL")); */

        GridPane.setMargin(customerTitleLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerFirstNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerLastNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerLastNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerBusinessInternetLabel, new Insets(15, 0, 15, 0));
        GridPane.setMargin(customerBusinessPhoneLabel, new Insets(15, 0, 15, 0));
        GridPane.setMargin(customerMobilePhoneLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerPhoneLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerZoneLabel, new Insets(10, 30, 10, 10));
        GridPane.setMargin(customerWebsiteLabel, new Insets(10, 30, 20, 30));
        GridPane.setMargin(customerCountryLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerStateLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerCityLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerAddressDetailLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerAddressNumberLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerAddressStreetNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerNewsLetter, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerAllowAds, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerEmailLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerAllowAds, new Insets(10, 30, 10, 0));
        isInitialized = true;
    }

    @Override
    protected void initializeContent(){

    }

    public void handleSaveButton(ActionEvent actionEvent) {
    }

    public void handleCancelButton(ActionEvent actionEvent) {
    }

    @Override
    public void updateToolBar(){
        saveButton = new Button(JeproLab.getBundle().getString("JEPROLAB_SAVE_LABEL"));
        cancelButton = new Button(JeproLab.getBundle().getString("JEPROLAB_CANCEL_LABEL"));

        /*saveButton.setOnMouseClicked(EventHandler<;? MouseEvent>(){
            @Override

        });*/
    }
}
