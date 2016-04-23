package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLang;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabCurrencyModel;
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
public class JeproLabCountryAddController extends JeproLabController {
    private JeproLabCountryModel country;
    @FXML
    public Label formTitleLabel, countryZoneLabel, defaultCurrencyLabel, zipCodeFormatLabel, addressLayoutFormatLabel;
    public Label needZipCodeLabel, publishedLabel, displayTaxLabelLabel, needIdentificationNumberLabel, containsStatesLabel;
    public Label callPrefixLabel, isoCodeLabel, countryNameLabel;
    public JeproSwitchButton needZipCode, published, displayTaxLabel, needIdentificationNumber, containsStates;
    public TextField isoCode, callPrefix, zipCodeFormat;
    public TextArea addressLayoutFormat;
    public ComboBox<String> countryZone, defaultCurrency;
    public JeproMultiLang<TextField> countryName;
    public Button saveButton, cancelButton;
    public JeproFormPanelContainer countryFormContainerWrapper;
    public JeproFormPanelTitle countryFormTitleWrapper;
    public JeproFormPanel countryFormPanelWrapper;
    public GridPane jeproLabAddCountryFormLayout;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 200;
        double inputColumnWidth = 270;
        formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
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


        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_COUNTRY_LABEL"));
        countryFormTitleWrapper.getChildren().add(formTitleLabel);
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setPrefWidth(formWidth);
        formTitleLabel.setAlignment(Pos.CENTER);
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


        /**
         * GridPane styling
         */
        GridPane.setMargin(displayTaxLabelLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(countryZoneLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(defaultCurrencyLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(zipCodeFormatLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(needIdentificationNumberLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(containsStatesLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(callPrefixLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(isoCodeLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(countryNameLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(addressLayoutFormatLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(publishedLabel, new Insets(5, 0, 10, 15));
        GridPane.setMargin(needZipCodeLabel, new Insets(5, 0, 10, 15));
        GridPane.setValignment(addressLayoutFormatLabel, VPos.TOP);

        //initializeContent();
    }

    @Override
    public void initializeContent(){
        loadCountry(false);
        List<JeproLabCountryModel.JeproLabZoneModel> zones = JeproLabCountryModel.JeproLabZoneModel.getZones(true);
        countryZone.setPrefWidth(120);
        countryZone.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabCountryModel.JeproLabZoneModel zone : zones) {
            countryZone.getItems().add(zone.name);
            if(country.country_id > 0 && zone.zone_id == country.zone_id){
                countryZone.setValue(zone.name);
            }
        }

        List<JeproLabCurrencyModel> currencies = JeproLabCurrencyModel.getCurrencies();
        defaultCurrency.setPrefWidth(120);
        defaultCurrency.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
        for(JeproLabCurrencyModel currency : currencies) {
            defaultCurrency.getItems().add(currency.name);
            if(country.country_id > 0 && currency.currency_id == country.currency_id){
                defaultCurrency.setValue(currency.name);
            }
        }
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (country.country_id > 0) {
            saveButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveButton, cancelButton);
        addCommandListener();
    }

    private void addCommandListener(){

    }

    /**
     * Load class supplier using identifier in $_GET (if possible)
     * otherwise return an empty supplier, or die
     *
     * @param option Return an empty supplier if load fail     *
     */
    public void loadCountry(boolean option){
        if(context == null){
            context = JeproLabContext.getContext();
        }
        int countryId = JeproLab.request.getPost().containsKey("country_id") ? Integer.parseInt(JeproLab.request.getPost().get("country_id")) : 0;
        if (countryId > 0){
            if (this.country == null) {
                this.country = new JeproLabCountryModel(countryId);
            }
            //if (JeproshopTools::isLoadedObject($this->country, 'country_id'))
            //return $this->country;
            // throw exception
            //JError::raiseError(500, 'The country cannot be loaded (or not found)');
            //return false;
        } else if (option) {
            if (this.country == null)
                this.country = new JeproLabCountryModel();
        } else {
            this.context.controller.has_errors = true;
            country = new JeproLabCountryModel();
            //JError::raiseError('The country cannot be loaded (the identifier is missing or invalid)');
        }
    }
}
