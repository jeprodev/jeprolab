package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabCurrencyModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryAddController extends JeproLabController{
    private JeproLabCountryModel country;
    private CheckBox checkAll;
    private Button saveButton, cancelButton;
    private TableView<JeproLabCountryStateRecord> jeproLabStateTableView;
    private HBox jeproLabCountryStateSearchWrapper;
    private TextField jeproLabCountryStateSearchField;
    private ComboBox<String> jeproLabCountryStateSearchFilter;
    private Button jeproLabCountryStateSearchBtn;
    private ObservableList<JeproLabCountryStateRecord> statesList;

    @FXML
    public Label formTitleLabel, countryZoneLabel, defaultCurrencyLabel, zipCodeFormatLabel, addressLayoutFormatLabel;
    public Label needZipCodeLabel, publishedLabel, displayTaxLabelLabel, needIdentificationNumberLabel, containsStatesLabel;
    public Label callPrefixLabel, isoCodeLabel, countryNameLabel;
    public JeproSwitchButton needZipCode, published, displayTaxLabel, needIdentificationNumber, containsStates;
    public TextField isoCode, callPrefix, zipCodeFormat;
    public TextArea addressLayoutFormat;
    public ComboBox<String> countryZone, defaultCurrency;
    public JeproMultiLangTextField countryName;
    public JeproFormPanelContainer countryFormContainerWrapper;
    public JeproFormPanelTitle countryFormTitleWrapper;
    public JeproFormPanel countryFormPanelWrapper;
    public GridPane jeproLabAddCountryFormLayout;
    public TabPane jeproLabCountryTabPane;
    public Tab jeproLabCountryFormTab, jeproLabCountryStatesTab;
    public VBox jeproLabStateTableViewWrapper;

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

        jeproLabCountryFormTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabCountryStatesTab.setText(bundle.getString("JEPROLAB_STATES_LABEL"));


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

        initializeStateTableView();
    }

    @Override
    public void initializeContent(){
        initializeContent(0);
    }

    @Override
    public void initializeContent(int countryId) {
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabCountryModel.JeproLabStateModel> states;
            List<JeproLabCountryModel.JeproLabZoneModel> zones;
            List<JeproLabCurrencyModel> currencies;

            @Override
            protected Boolean call() throws Exception {
                if (isCancelled()) {
                    return false;
                }
                loadCountry(countryId, false);
                zones = JeproLabCountryModel.JeproLabZoneModel.getZones(true);
                currencies = JeproLabCurrencyModel.getCurrencies();
                states = country.getCountryStates();
                return true;
            }

            @Override
            protected void failed() {
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateIInformation(country, zones, currencies, states);
            }
        };
        new Thread((Task) worker).start();
    }


    private void updateIInformation(JeproLabCountryModel country, List<JeproLabCountryModel.JeproLabZoneModel> zones, List<JeproLabCurrencyModel> currencies, List<JeproLabCountryModel.JeproLabStateModel> states){
        if(country != null && country.country_id > 0) {
            Platform.runLater(() -> {
                countryZone.setPrefWidth(180);
                countryZone.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
                countryZone.getItems().clear();
                for (JeproLabCountryModel.JeproLabZoneModel zone : zones) {
                    countryZone.getItems().add(zone.name);
                    if (country.country_id > 0 && zone.zone_id == country.zone_id) {
                        countryZone.setValue(zone.name);
                    }
                }

                defaultCurrency.setPrefWidth(180);
                defaultCurrency.getItems().clear();
                defaultCurrency.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SELECT_LABEL"));
                for (JeproLabCurrencyModel currency : currencies) {
                    defaultCurrency.getItems().add(currency.name);
                    if (country.country_id > 0 && currency.currency_id == country.currency_id) {
                        defaultCurrency.setValue(currency.name);
                    }
                }

                formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_COUNTRY_LABEL"));
                countryName.setText(country.name);
                callPrefix.setText(country.call_prefix);
                zipCodeFormat.setText(country.zip_code_format);
                isoCode.setText(country.iso_code);
                published.setSelected(country.published);
                //addressLayoutFormat.setText(country.);
                needIdentificationNumber.setSelected(country.need_identification_number);
                containsStates.setSelected(country.contains_states);
                displayTaxLabel.setSelected(country.display_tax_label);

                statesList = FXCollections.observableArrayList();
                statesList.addAll(states.stream().map(JeproLabCountryStateRecord::new).collect(Collectors.toList()));

                if(statesList.isEmpty()){
                    setEmptyTableView(jeproLabStateTableViewWrapper, jeproLabCountryStateSearchWrapper, jeproLabStateTableView);
                }else{
                    double padding = 0;
                    Pagination jeproLabStatesPagination = new Pagination((statesList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                    jeproLabStatesPagination.setPageFactory(this::createStatesPages);
                    VBox.setMargin(jeproLabCountryStateSearchWrapper, new Insets(5, padding, 5, padding));
                    VBox.setMargin(jeproLabStatesPagination, new Insets(5, padding, 5, padding));
                }
            });
        }else {
            Platform.runLater(() -> {
                countryName.setText(null);
                callPrefix.setText("");
                zipCodeFormat.setText("");
                isoCode.setText("");
                published.setSelected(true);
                //addressLayoutFormat.setText(country.);
                needIdentificationNumber.setSelected(true);
                containsStates.setSelected(true);
                displayTaxLabel.setSelected(true);
            });
        }

    }

    private void initializeStateTableView(){
        double remainingWidth = formWidth - 228;
        jeproLabStateTableView = new TableView<>();
        VBox.setMargin(jeproLabStateTableView, new Insets(0, 0, 0, 0));
        jeproLabStateTableView.setPrefSize(formWidth, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);

        TableColumn<JeproLabCountryStateRecord, String> jeproLabStateIndexColumn = new TableColumn<>();
        jeproLabStateIndexColumn.setPrefWidth(30);
        tableCellAlign(jeproLabStateIndexColumn, Pos.CENTER_RIGHT);
        jeproLabStateIndexColumn.setCellValueFactory(new PropertyValueFactory<>("stateIndex"));

        TableColumn<JeproLabCountryStateRecord, Boolean> jeproLabStateCheckBoxColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATE_NAME_LABEL"));
        checkAll = new CheckBox();
        jeproLabStateCheckBoxColumn.setGraphic(checkAll);
        jeproLabStateCheckBoxColumn.setPrefWidth(22);
        Callback<TableColumn<JeproLabCountryStateRecord, Boolean>, TableCell<JeproLabCountryStateRecord, Boolean>> checkBoxFactory = param -> new JeproLabCountryStateCheckBoxCell();
        jeproLabStateCheckBoxColumn.setCellFactory(checkBoxFactory);

        TableColumn<JeproLabCountryStateRecord, Boolean> jeproLabStateStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabStateStatusColumn.setPrefWidth(45);
        Callback<TableColumn<JeproLabCountryStateRecord, Boolean>, TableCell<JeproLabCountryStateRecord, Boolean>> statusFactory = param -> new JeproLabCountryStateStatusCell();
        jeproLabStateStatusColumn.setCellFactory(statusFactory);

        TableColumn<JeproLabCountryStateRecord, String> jeproLabStateNameColumn = new TableColumn<>();
        jeproLabStateNameColumn.setPrefWidth(0.4 * remainingWidth);
        tableCellAlign(jeproLabStateNameColumn, Pos.CENTER_LEFT);
        jeproLabStateNameColumn.setCellValueFactory(new PropertyValueFactory<>("stateStateNameName"));

        TableColumn<JeproLabCountryStateRecord, String> jeproLabStateIsoCodeColumn = new TableColumn<>(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        jeproLabStateIsoCodeColumn.setPrefWidth(60);
        tableCellAlign(jeproLabStateIsoCodeColumn, Pos.CENTER);
        jeproLabStateIsoCodeColumn.setCellValueFactory(new PropertyValueFactory<>("stateIsoCodeName"));

        TableColumn<JeproLabCountryStateRecord, String> jeproLabStateTaxBehaviorColumn = new TableColumn<>(bundle.getString("JEPROLAB_TAX_BEHAVIOR_LABEL"));
        tableCellAlign(jeproLabStateTaxBehaviorColumn, Pos.CENTER);
        jeproLabStateTaxBehaviorColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabStateTaxBehaviorColumn.setCellValueFactory(new PropertyValueFactory<>("stateTaxBehavior"));

        TableColumn<JeproLabCountryStateRecord, HBox> jeproLabStateActionsColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabStateActionsColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabCountryStateRecord, HBox>, TableCell<JeproLabCountryStateRecord, HBox>> actionsFactory = param -> new JeproLabCountryStateActionCell();
        jeproLabStateActionsColumn.setCellFactory(actionsFactory);

        jeproLabStateTableView.getColumns().addAll(
            jeproLabStateIndexColumn, jeproLabStateCheckBoxColumn, jeproLabStateStatusColumn,
            jeproLabStateNameColumn, jeproLabStateIsoCodeColumn, jeproLabStateTaxBehaviorColumn,
            jeproLabStateActionsColumn
        );
        jeproLabCountryStateSearchField = new TextField();
        jeproLabCountryStateSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabCountryStateSearchFilter = new ComboBox<>();
        jeproLabCountryStateSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabCountryStateSearchBtn = new Button();
        jeproLabCountryStateSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabCountryStateSearchWrapper = new HBox(10);
        jeproLabCountryStateSearchWrapper.getChildren().addAll(
            jeproLabCountryStateSearchField, jeproLabCountryStateSearchFilter, jeproLabCountryStateSearchBtn
        );
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
        saveButton.setOnAction(evt -> {
            country.name = countryName.getDataContent();
            country.call_prefix = callPrefix.getText();
            country.zip_code_format = zipCodeFormat.getText();
            country.iso_code = isoCode.getText();
            country.published = published.isSelected();
            country.need_identification_number = needIdentificationNumber.isSelected();
            country.contains_states = containsStates.isSelected();
            country.display_tax_label = displayTaxLabel.isSelected();

            if(country.country_id > 0) {
                country.update();
            }else{
                country.save();
            }
        });
    }

    /**
     * Load class supplier using identifier in $_GET (if possible)
     * otherwise return an empty supplier, or die
     *
     * @param option Return an empty supplier if load fail     *
     */
    private void loadCountry(int countryId, boolean option){
        if(context == null){
            context = JeproLabContext.getContext();
        }

        if (countryId > 0){
            if (this.country == null) {
                this.country = new JeproLabCountryModel(countryId);
            }
        } else if (option) {
            if (this.country == null)
                this.country = new JeproLabCountryModel();
        } else {
            this.context.controller.has_errors = true;
            country = new JeproLabCountryModel();
        }
    }

    private Node createStatesPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (statesList.size()));
        jeproLabStateTableView.setItems(FXCollections.observableArrayList(statesList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabStateTableView);
    }


    public static class JeproLabCountryStateRecord{
        private SimpleIntegerProperty stateIdndex;
        private SimpleStringProperty stateName, stateCountryName, stateZoneName, stateIsoCode;
        private SimpleBooleanProperty statePublished;

        public JeproLabCountryStateRecord(JeproLabCountryModel.JeproLabStateModel state){
            stateIdndex = new SimpleIntegerProperty(state.state_id);
            stateName = new SimpleStringProperty(state.name);
            stateIsoCode = new SimpleStringProperty(state.iso_code);
            statePublished = new SimpleBooleanProperty(state.published);
        }
    }

    private class JeproLabCountryStateCheckBoxCell extends TableCell<JeproLabCountryStateRecord, Boolean>{

    }

    private class JeproLabCountryStateStatusCell extends TableCell<JeproLabCountryStateRecord, Boolean>{
        private CheckBox zoneCheckBox;

        public JeproLabCountryStateStatusCell(){
            zoneCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean it){
            super.commitEdit(it);
        }

        @Override
        public void updateItem(Boolean item, boolean it){
            super.updateItem(item, it);
            final ObservableList items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(zoneCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private class JeproLabCountryStateActionCell extends TableCell<JeproLabCountryStateRecord, HBox> {

    }
}
