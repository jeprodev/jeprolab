package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabCustomerModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabCustomerAddController extends JeproLabController{
    private Button saveButton, cancelButton;
    private JeproLabCustomerModel customer;
    private JeproLabAddressModel address;
    private List<JeproLabCountryModel> countries;
    private List<JeproLabCountryModel.JeproLabZoneModel> zones;
    private List<JeproLabAddressModel.JeproLabStreetTypeModel> streetTypes;

    @FXML
    public Label customerTitleLabel, customerFirstNameLabel, customerLastNameLabel, customerBusinessInternetLabel;
    public Label customerBusinessPhoneLabel, customerPhoneLabel, customerMobilePhoneLabel, customerEmailLabel;
    public Label customerWebsiteLabel, customerZoneLabel, customerCountryLabel, customerStateLabel, customerCityLabel;
    public Label customerAddressDetailLabel, customerAddressNumberLabel, customerAddressDeaLabel, customerAddressLabel;
    public TextField customerFirstName, customerLastName, customerWebsite, customerAddress, customerAddressDetail;
    public JeproPhoneField customerPhone, customerMobilePhone;
    public ComboBox<String> customerTitle;
    public ComboBox<String> customerZone, customerCountry, customerStreetType;
    public CheckBox customerNewsLetter, customerAllowAds;
    public JeproEmailField customerEmail;
    public ImageView customerImageView;
    public GridPane jeproLabAddCustomerFormLayout;
    public Pane customerImageViewWrapper, jeproLabCustomerInformationPane;
    public JeproFormPanel jeproLabAddCustomerFormWrapper;
    public JeproFormPanelTitle jeproLabAddCustomerFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddCustomerFormContainerWrapper;
    public TabPane jeproLabCustomerTabPane;
    public Tab jeproLabCustomerInformationTab;

    @Override
    public void initialize(URL location , ResourceBundle resource) {
        super.initialize(location, resource);

        double labelColumnWidth = 160;
        double inputColumnWidth = 250;
        formWidth = 2 * (labelColumnWidth + inputColumnWidth) + 30;
        double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth)) / 2;
        double posX = (JeproLab.APP_WIDTH / 2) - (formWidth) / 2;
        double posY = 25;

        jeproLabAddCustomerFormWrapper.setPrefWidth(formWidth);
        jeproLabAddCustomerFormWrapper.setLayoutX(posX);
        jeproLabAddCustomerFormWrapper.setLayoutY(posY);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_CUSTOMER_LABEL"));
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setPrefSize(formWidth, 35);
        formTitleLabel.setAlignment(Pos.CENTER);
        jeproLabAddCustomerFormTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabAddCustomerFormTitleWrapper.setPrefSize(formWidth, 40);

        jeproLabAddCustomerFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddCustomerFormContainerWrapper.setLayoutY(40);

        jeproLabCustomerTabPane.setPrefWidth(formWidth);

        jeproLabCustomerInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));

        VBox.setMargin(jeproLabAddCustomerFormLayout, new Insets(5, 10, 5, 30));
        jeproLabAddCustomerFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25),
                new ColumnConstraints(labelColumnWidth - 15), new ColumnConstraints(inputColumnWidth - 25)
        );
        jeproLabAddCustomerFormLayout.setLayoutX(20);


        customerTitleLabel.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitleLabel.getStyleClass().add("input-label");
        customerTitle.setPromptText(bundle.getString("JEPROLAB_SELECT_TITLE_LABEL"));
        customerTitle.getItems().addAll(
                bundle.getString("JEPROLAB_MR_LABEL"), bundle.getString("JEPROLAB_MRS_LABEL"), bundle.getString("JEPROLAB_MISS_LABEL")
        );

        customerFirstNameLabel.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerFirstNameLabel.getStyleClass().add("input-label");
        customerLastNameLabel.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerLastNameLabel.getStyleClass().add("input-label");
        customerBusinessInternetLabel.setText(bundle.getString("JEPROLAB_BUSINESS_INTERNET_LABEL"));
        customerBusinessInternetLabel.getStyleClass().add("input-label");
        customerBusinessInternetLabel.setAlignment(Pos.CENTER);
        customerBusinessInternetLabel.setPrefSize(JeproLab.APP_WIDTH - 50, 30);
        customerBusinessPhoneLabel.setText(bundle.getString("JEPROLAB_BUSINESS_PHONE_LABEL"));
        customerBusinessPhoneLabel.getStyleClass().add("input-label");
        customerBusinessPhoneLabel.setAlignment(Pos.CENTER);
        customerBusinessPhoneLabel.setPrefSize(JeproLab.APP_WIDTH - 50, 30);
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
        //customerStateLabel.setText(bundle.getString("JEPROLAB_STATE_LABEL"));

        customerAddressLabel.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        //customerCityLabel.getStyleClass().add("input-label");
        customerAddressDetailLabel.setText(bundle.getString("JEPROLAB_ADDRESS_DETAIL_LABEL"));
        customerAddressDetailLabel.getStyleClass().add("input-label");
        //customerAddressNumberLabel.setText(bundle.getString("JEPROLAB_ADDRESS_NUMBER_LABEL"));
        //customerAddressNumberLabel.getStyleClass().add("input-label");
        //customerAddressStreetNameLabel.setText(bundle.getString("JEPROLAB_STREET_NAME_LABEL"));
        //customerAddressStreetNameLabel.getStyleClass().add("input-label");
        customerNewsLetter.setText(bundle.getString("JEPROLAB_ADD_CUSTOMER_TO_NEWS_LETTER_LABEL"));
        customerAllowAds.setText(bundle.getString("JEPROLAB_RECEIVE_ADS_FROM_PARTNERS_LABEL"));

        /*customerStreetType.setPromptText(bundle.getString("JEPROLAB_SELECT_STREET_TYPE_LABEL"));
        streetTypes = JeproLabAddressModel.JeproLabStreetTypeModel.getStreetTypes(context.language.language_id);
        for(JeproLabAddressModel.JeproLabStreetTypeModel streetType : streetTypes) {
            customerStreetType.getItems().addAll( streetType.name.get("lang_" + context.language.language_id));
        } */

        countries = JeproLabCountryModel.getCountries(context.language.language_id, true);
        zones = JeproLabCountryModel.JeproLabZoneModel.getZones(true);


        customerCountry.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));

        for(JeproLabCountryModel country : countries){
            customerCountry.getItems().add(country.name.get("lang_" + context.language.language_id));
        }

        customerZone.setPrefWidth(120);
        customerZone.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabCountryModel.JeproLabZoneModel zone : zones) {
            customerZone.getItems().add(zone.name);
        }

        GridPane.setMargin(customerTitleLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerFirstNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerLastNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerLastNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerBusinessInternetLabel, new Insets(15, 0, 15, 0));
        GridPane.setMargin(customerBusinessPhoneLabel, new Insets(15, 0, 15, 0));
        GridPane.setMargin(customerMobilePhoneLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerPhoneLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerZoneLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerWebsiteLabel, new Insets(10, 30, 20, 30));
        GridPane.setMargin(customerCountryLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerAddress, new Insets(15, 10, 15, 0));
        //GridPane.setMargin(customerCityLabel, new Insets(10, 30, 10, 30));
        GridPane.setMargin(customerAddressDetailLabel, new Insets(10, 30, 10, 0));
        //GridPane.setMargin(customerAddressNumberLabel, new Insets(10, 30, 10, 30));
        //GridPane.setMargin(customerAddressStreetNameLabel, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerNewsLetter, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerAllowAds, new Insets(10, 30, 10, 0));
        GridPane.setMargin(customerEmailLabel, new Insets(10, 30, 10, 0));
        //GridPane.setMargin(customerAllowAds, new Insets(10, 30, 10, 0));
        isInitialized = true;
    }

    @Override
    public void initializeContent() {
        loadCustomer();
        final int[] zoneId = {0};
/*if(address.country_id == country.country_id){
                zoneId = country.zone_id;
                customerCountry.setValue(country.name.get("lang_" + context.language.language_id));
            }*//*if(zone.zone_id == zoneId){
                jeproLabAddressCountryZone.setValue(zone.name);
            }*/
        if (customer.customer_id > 0) {
            customerTitle.setValue(bundle.getString("JEPROLAB_" + customer.title.toUpperCase() + "_LABEL"));
            customerFirstName.setText(customer.firstname);
            customerLastName.setText(customer.lastname);
            customerNewsLetter.setSelected(customer.news_letter);
            // todo customerAllowAds.setSelected(customer.all);
            address = JeproLabAddressModel.getAddressByCustomerId(customer.customer_id, true);
            countries.stream().filter(country -> country.country_id == address.country_id).forEach(country -> {
                customerCountry.setValue(country.name.get("lang_" + context.language.language_id));
                zones.stream().filter(zone -> zone.zone_id == country.zone_id).forEach(zone -> {
                    customerZone.setValue(zone.name);
                });
            });
            customerAddress.setText(address.address1);
            customerAddressDetail.setText(address.address2);
            customerPhone.setText(address.phone);
            customerMobilePhone.setText(address.mobile_phone);
            customerWebsite.setText(customer.website);
            customerEmail.setText(customer.email);
        } else {

        }
        updateToolBar();
        addEventListeners();
    }

    public void handleSaveButton(ActionEvent actionEvent) {
    }

    public void handleCancelButton(ActionEvent actionEvent) {
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (customer.customer_id > 0) {
            saveButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveButton, cancelButton);
    }

    /**
     * Load class object using identifier in $_GET (if possible)
     * otherwise return an empty object, or die     *
     *
     * @return object|boolean
     */
    private void loadCustomer(){
		/*if (!isset($this->customer) || empty($this->customer))
			return true;
		*/
        //$app = JFactory::getApplication();
        int customerId = JeproLab.request.getRequest().containsKey("customer_id") ? Integer.parseInt(JeproLab.request.getRequest().get("customer_id")) : 0;
        customerId = 1;
        if (customerId > 0){
            if (customer == null)
                customer = new JeproLabCustomerModel(customerId);
            if ((customer.customer_id != customerId)){
                //todo set notify the user does not exist
            }
            // throw exception
            //$this->errors[] = Tools::displayError('The object cannot be loaded (or found)');
            //return false;
        }else{
            if (customer == null)
                customer = new JeproLabCustomerModel();
            //return $this->customer;
        }
    }

    private void addEventListeners(){
        customerZone.valueProperty().addListener((observable, oldValue, newValue) -> {
            int zoneId = JeproLabCountryModel.JeproLabZoneModel.getZoneIdByName(newValue);
            if(context == null){
                context = JeproLabContext.getContext();
            }
            List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountriesByZoneId(zoneId, context.language.language_id);
            customerCountry.getItems().clear();
            for(JeproLabCountryModel country : countries){
                customerCountry.getItems().addAll(country.name.get("lang_" + context.language.language_id));
            }
        });

        customerCountry.valueProperty().addListener(((observable, oldValue, newValue) -> {
            countries.stream().filter(country -> country.name.get("lang_" + context.language.language_id).equals(newValue)).forEach(country -> {
                address.country_id = country.country_id;
            });
        }));

        saveButton.setOnAction(event -> {
            if(customerTitle.getValue().toLowerCase().equals(bundle.getString("JEPROLAB_MR_LABEL").toLowerCase())){
                customer.title = "mr";
            }else if(customerTitle.getValue().toLowerCase().equals(bundle.getString("JEPROLAB_MRS_LABEL").toLowerCase())) {
                customer.title = "mrs";
            }else if(customerTitle.getValue().toLowerCase().equals(bundle.getString("JEPROLAB_MISS_LABEL").toLowerCase())){
                customer.title = "miss";
            }

            customer.firstname = customerFirstName.getText();
            customer.lastname = customerLastName.getText();
            customer.website = customerWebsite.getText();
            customer.email = customerEmail.getText();

            address.firstname = customerFirstName.getText();
            address.lastname = customerLastName.getText();
            address.address1 = customerAddress.getText();
            address.address2 = customerAddressDetail.getText();
            address.phone = customerPhone.getText();
            address.mobile_phone = customerMobilePhone.getText();

            if(customer.customer_id > 0){
                customer.update();
                address.update();
            }else{
                customer.add();
                address.customer_id = customer.customer_id;
                address.add();
            }
        });
        //cancelButton = new Button(JeproLab.getBundle().getString("JEPROLAB_CANCEL_LABEL"));
    }
}
