package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabSettingModel;
import com.jeprolab.views.JeproLabApplicationForms;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabMenuController extends JeproLabController{
    @FXML
    public VBox jeproLabMenuBarWrapper;
    public MenuBar jeproLabMenuBar;
    public Menu dashBoardMenu, catalogMenu, customerMenu, helpMenu, feedsMenu, settingMenu, requestMenu, requestBillsMenu;
    public Menu categorySubMenu, customersSubMenu, addressesSubMenu, groupsSubMenu, currentSubRequestMenu, threadsSubMenu, contactsSubMenu;
    public Menu analyzeSubMenu, attachmentsSubMenu, feedsSubMenu, feedBackSubMenu, laboratorySubMenu, laboratoryGroupSubMenu;
    public Menu localisationSubMenu, taxesSubMenu, countriesSubMenu, zonesSubMenu, statesSubMenu, currenciesSubMenu;
    public Menu requestComplaintMenu, requestRefundMenu, requestMessageMenu, requestSampleMenu, laboratoryCarrierSubMenu;
    public MenuItem addAnalyseSubMenuItem, dashBoardMenuItem, sampleListMenuItem, sampleAddNewMenuItem;
    public MenuItem addCustomerMenuItem, customersSubMenuItem;
    public MenuItem addAttachmentSubMenuItem, attachmentSubMenuItem;
    public MenuItem addCategoryMenuItem, categorySubMenuItem, addFeedMenuItem, feedBackMenuItem, customersMenuItem;
    public MenuItem aboutJeproLabMenuItem, checkForJeproLabUpdate;
    public MenuItem addLaboratorySubMenuItem, addLaboratoryGroupSubMenuItem, laboratorySubMenuItem;
    public MenuItem analyzeSubMenuItem, laboratoryCarrierSubMenuItem, addLaboratoryCarrierSubMenuItem;
    public MenuItem addressesSubMenuItem, addAddressMenuItem;
    public MenuItem groupsSubMenuItem, addGroupMenuItem, currentRequestMenuItem;
    public MenuItem customerThreadsSubMenuItem, contactsSubMenuItem;
    public MenuItem feedsSubMenuItem, methodSubMenuItem, addMethodSubMenuItem;
    public MenuItem zoneListSubMenuItem, laboratoryGroupSubMenuItem;
    public MenuItem addTaxSubMenuItem, addTaxGroupSubMenuItem, taxesGroupSubMenuItem, taxesSubMenuItem;
    public MenuItem countrySubMenuItem, addCountrySubMenu, editZoneSubMenu, addStateSubMenuItem, statesSubMenuItem, currenciesSubMenuItem, addCurrencySubMenuItem;
    public MenuItem requestListMenuItem, requestAddNewMenuItem, requestBillsMenuItem, requestAddBillMenuItem;
    public MenuItem requestComplaintMenuItem, requestAddNewComplaintMenuItem, requestRefundsMenuItem, requestAddNewRefundsMenuItem;
    public MenuItem requestStatusMenuItem, requestAddStatusMenuItem, requestMessagesMenuItem, requestAddMessageMenuItem;
    public MenuItem fileManagementMenuItem, socialNetworksMenuItem;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        jeproLabMenuBar.setPrefWidth(JeproLab.APP_WIDTH);
        dashBoardMenu.setText(bundle.getString("JEPROLAB_DASHBOARD_LABEL"));
        dashBoardMenuItem.setText(bundle.getString("JEPROLAB_DASHBOARD_LABEL"));

        /*
         *
         */
        catalogMenu.setText(bundle.getString("JEPROLAB_CATALOGS_LABEL"));
        categorySubMenu.setText(bundle.getString("JEPROLAB_CATEGORIES_LABEL"));
        categorySubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        addCategoryMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        analyzeSubMenu.setText(bundle.getString("JEPROLAB_ANALYZES_LABEL"));
        analyzeSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_ANALYZE_LABEL"));
        addAnalyseSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ANALYZE_LABEL"));
        attachmentsSubMenu.setText(bundle.getString("JEPROLAB_ATTACHMENTS_LABEL"));
        attachmentSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_ATTACHMENT_LABEL"));
        addAttachmentSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " "  + bundle.getString("JEPROLAB_ATTACHMENT_LABEL"));
        methodSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_METHODS_LABEL"));
        addMethodSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") +  " " + bundle.getString("JEPROLAB_METHOD_LABEL"));

        /**
         *
         */
        customerMenu.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        customersSubMenu.setText(bundle.getString("JEPROLAB_CUSTOMERS_LABEL"));
        customersSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        addCustomerMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CUSTOMER_LABEL"));

        addressesSubMenu.setText(bundle.getString("JEPROLAB_ADDRESSES_LABEL"));
        addressesSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_ADDRESSES_LABEL"));
        addAddressMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ADDRESS_LABEL"));

        groupsSubMenu.setText(bundle.getString("JEPROLAB_GROUPS_LABEL"));
        groupsSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_GROUPS_LABEL"));
        addGroupMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_GROUP_LABEL"));

        currentSubRequestMenu.setText(bundle.getString("JEPROLAB_REQUESTS_LABEL"));
        threadsSubMenu.setText(bundle.getString("JEPROLAB_THREADS_LABEL"));
        customerThreadsSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_THREADS_LABEL"));
        contactsSubMenu.setText(bundle.getString("JEPROLAB_CONTACTS_LABEL"));

        /**
         *
         */
        helpMenu.setText(bundle.getString("JEPROLAB_HELP_LABEL"));
        aboutJeproLabMenuItem.setText(bundle.getString("JEPROLAB_ABOUT_JEPROLAB_LABEL"));
        checkForJeproLabUpdate.setText(bundle.getString("JEPROLAB_CHECK_FOR_UPDATE_LABEL"));

        /**
         *
         */
        feedsMenu.setText(bundle.getString("JEPROLAB_FEEDS_LABEL"));
        feedsSubMenu.setText(bundle.getString("JEPROLAB_FEEDS_LABEL"));
        feedsSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_FEEDS_LABEL"));
        addFeedMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_FEED_LABEL"));
        feedBackMenuItem.setText(bundle.getString("JEPROLAB_FEEDBACK_LABEL"));

        /**
         *
         */
        settingMenu.setText(bundle.getString("JEPROLAB_SETTINGS_LABEL"));
        laboratorySubMenu.setText(bundle.getString("JEPROLAB_LABORATORIES_LABEL"));
        laboratorySubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_LABORATORIES_LABEL"));
        addLaboratorySubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_LABORATORY_LABEL"));
        laboratoryGroupSubMenu.setText(bundle.getString("JEPROLAB_LABORATORIES_GROUPS_LABEL"));
        laboratoryGroupSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_LABORATORIES_GROUPS_LABEL"));
        addLaboratoryGroupSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_LABORATORY_GROUP_LABEL"));

        /**
         *
         */
        requestMenu.setText(bundle.getString("JEPROLAB_REQUESTS_LABEL"));
        requestBillsMenu.setText(bundle.getString("JEPROLAB_REQUEST_LABEL") + " " + bundle.getString("JEPROLAB_BILLS_LABEL"));
        requestComplaintMenu.setText(bundle.getString("JEPROLAB_REQUEST_LABEL") + " " + bundle.getString("JEPROLAB_COMPLAINTS_LABEL"));
        requestRefundMenu.setText(bundle.getString("JEPROLAB_REQUEST_LABEL") + " " + bundle.getString("JEPROLAB_REFUNDS_LABEL"));
        //requestStatusMenu.setText(bundle.getString("JEPROLAB_REQUEST_LABEL") + " " + bundle.getString("JEPROLAB_STATUS_LABEL"));
        requestMessageMenu.setText(bundle.getString("JEPROLAB_REQUEST_LABEL") + " " + bundle.getString("JEPROLAB_MESSAGES_LABEL"));
        requestListMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_REQUESTS_LABEL"));
        requestAddNewMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_REQUEST_LABEL"));
        requestBillsMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_BILLS_LABEL"));
        requestAddBillMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_BILL_LABEL"));
        requestStatusMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_STATUS_LABEL"));
        requestAddStatusMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_STATUS_LABEL"));
        requestMessagesMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_MESSAGES_LABEL"));
        requestAddMessageMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_MESSAGE_LABEL"));
        requestComplaintMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_COMPLAINTS_LABEL"));
        requestAddNewComplaintMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_COMPLAINT_LABEL"));
        requestRefundsMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_REFUNDS_LABEL"));
        requestAddNewRefundsMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_REFUND_LABEL"));

        /*requestSampleMenu.setText(bundle.getString("JEPROLAB_SAMPLES_LABEL"));
        sampleListMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_SAMPLES_LABEL"));
        sampleAddNewMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_SAMPLE_LABEL"));
        /**
         * <!Menu fx:id="requestSampleMenu" >
         <MenuItem fx:id="sampleListMenuItem"  onAction="#handleSampleMenuEvent" />
         <MenuItem fx:id="sampleAddNewMenuItem"  onAction="#handleAddSampleMenuEvent" />
         <-Menu-->
         */
        localisationSubMenu.setText(bundle.getString("JEPROLAB_LOCALISATION_SETTINGS_LABEL"));
        taxesSubMenu.setText(bundle.getString("JEPROLAB_TAXES_LABEL"));
        countriesSubMenu.setText(bundle.getString("JEPROLAB_COUNTRIES_LABEL"));
        countrySubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_COUNTRIES_LABEL"));
        zoneListSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));

        //statesSubMenu.setText(bundle.getString("JEPROLAB_STATES_LABEL"));
        currenciesSubMenu.setText(bundle.getString("JEPROLAB_CURRENCIES_LABEL"));
        addTaxSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_TAX_LABEL"));
        taxesSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_TAX_LABEL"));
        taxesGroupSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_TAX_GROUP_LABEL"));
        addTaxGroupSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_TAX_GROUP_LABEL"));
        addCountrySubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        editZoneSubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
        statesSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_STATES_LABEL"));
        addStateSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_STATE_LABEL"));
        currenciesSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));
        addCurrencySubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));

        fileManagementMenuItem.setText(bundle.getString("JEPROLAB_FILE_MANAGEMENT_LABEL"));
        socialNetworksMenuItem.setText(bundle.getString("JEPROLAB_SOCIAL_NETWORK_LABEL"));

        laboratoryCarrierSubMenu.setText(bundle.getString("JEPROLAB_CARRIERS_LABEL"));
        laboratoryCarrierSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_CARRIERS_LABEL"));
        addLaboratoryCarrierSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CARRIER_LABEL"));

        addDashboardListeners();
        addCatalogListeners();
        addCustomerListeners();
        addRequestsListeners();
        addFeedsListeners();
        addSettingsListeners();
        addHelpListeners();
    }

    /**
     *
     */
    public void initializeContent(){

    }

    /**
     * Managing dashboard menu and sub-menus
     */
    private void addDashboardListeners(){
        dashBoardMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
            JeproLab.getInstance().getApplicationForms().dashBoardForm.controller.initializeContent();
        });
    }

    private void addCatalogListeners(){
        categorySubMenuItem.setOnAction(evt -> {
            if(JeproLabContext.getContext().category == null || JeproLabContext.getContext().category.category_id <= 0) {
                JeproLabContext.getContext().category = new JeproLabCategoryModel(JeproLabSettingModel.getIntValue("root_category"));
            }
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().categoryForm);
            JeproLab.getInstance().getApplicationForms().categoryForm.controller.initializeContent();
        });

        addCategoryMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent();
        });

        analyzeSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
            JeproLab.getInstance().getApplicationForms().analyzeForm.controller.initializeContent();
        });

        addAnalyseSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeForm);
            JeproLab.getInstance().getApplicationForms().addAnalyzeForm.controller.initializeContent();
        });

        methodSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeMethodForm);
            JeproLab.getInstance().getApplicationForms().analyzeMethodForm.controller.initializeContent();
        });

        addMethodSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm);
            JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm.controller.initializeContent();
        });

        attachmentSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().attachmentForm);
            JeproLab.getInstance().getApplicationForms().attachmentForm.controller.initializeContent();
        });

        addAttachmentSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAttachmentForm);
            JeproLab.getInstance().getApplicationForms().addAttachmentForm.controller.initializeContent();
        });
    }

    private void addRequestsListeners(){
        requestListMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
            JeproLab.getInstance().getApplicationForms().requestForm.controller.initializeContent();
        });

        requestAddNewMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
            JeproLab.getInstance().getApplicationForms().addRequestForm.controller.initializeContent();
        });

        requestBillsMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestBillsForm);
            JeproLab.getInstance().getApplicationForms().requestBillsForm.controller.initializeContent();
        });

        requestAddBillMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestAddBillForm);
            JeproLab.getInstance().getApplicationForms().requestAddBillForm.controller.initializeContent();
        });

        requestComplaintMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestComplainsForm);
            JeproLab.getInstance().getApplicationForms().requestAddComplainForm.controller.initializeContent();
        });

        requestAddNewComplaintMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestAddComplainForm);
            JeproLab.getInstance().getApplicationForms().requestAddComplainForm.controller.initializeContent();
        });

        requestRefundsMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestRefundsForm);
            JeproLab.getInstance().getApplicationForms().requestRefundsForm.controller.initializeContent();
        });

        requestAddNewRefundsMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestAddRefundForm);
            JeproLab.getInstance().getApplicationForms().requestAddRefundForm.controller.initializeContent();
        });

        requestStatusMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestStatusForm);
            JeproLab.getInstance().getApplicationForms().requestStatusForm.controller.initializeContent();
        });

        requestAddStatusMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestAddStatusForm);
            JeproLab.getInstance().getApplicationForms().requestAddStatusForm.controller.initializeContent();
        });

        requestMessagesMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestMessagesForm);
            JeproLab.getInstance().getApplicationForms().requestMessagesForm.controller.initializeContent();
        });

        requestAddMessageMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestAddMessagesForm);
            JeproLab.getInstance().getApplicationForms().requestAddMessagesForm.controller.initializeContent();
        });
    }

    private void addFeedsListeners(){
        feedsSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().feedsForm);
            JeproLab.getInstance().getApplicationForms().feedsForm.controller.initializeContent();
        });

        addFeedMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addFeedForm);
            JeproLab.getInstance().getApplicationForms().addFeedForm.controller.initializeContent();
        });

        feedBackMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().feedBackForm);
            JeproLab.getInstance().getApplicationForms().feedBackForm.controller.initializeContent();
        });
    }

    private void addSettingsListeners(){
        laboratorySubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().laboratoriesForm);
            JeproLab.getInstance().getApplicationForms().laboratoriesForm.controller.initializeContent();
        });

        addLaboratorySubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoryForm);
            JeproLab.getInstance().getApplicationForms().addLaboratoryForm.controller.initializeContent();
        });

        laboratoryGroupSubMenu.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().laboratoriesGroupsForm);
            JeproLab.getInstance().getApplicationForms().laboratoriesGroupsForm.controller.initializeContent();
        });

        addLaboratoryGroupSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoriesGroupForm);
            JeproLab.getInstance().getApplicationForms().addLaboratoriesGroupForm.controller.initializeContent();
        });

        countrySubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().countryForm);
            JeproLab.getInstance().getApplicationForms().countryForm.controller.initializeContent();
        });

        addCountrySubMenu.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCountryForm);
            JeproLab.getInstance().getApplicationForms().addCountryForm.controller.initializeContent();
        });

        statesSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().stateForm);
            JeproLab.getInstance().getApplicationForms().stateForm.controller.initializeContent();
        });

        addStateSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addStateForm);
            JeproLab.getInstance().getApplicationForms().addStateForm.controller.initializeContent();
        });

        zoneListSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().zonesForm);
            JeproLab.getInstance().getApplicationForms().zonesForm.controller.initializeContent();
        });

        editZoneSubMenu.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addZoneForm);
            JeproLab.getInstance().getApplicationForms().addZoneForm.controller.initializeContent();
        });

        currenciesSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().currencyForm);
            JeproLab.getInstance().getApplicationForms().currencyForm.controller.initializeContent();
        });

        addCurrencySubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCurrencyForm);
            JeproLab.getInstance().getApplicationForms().addCurrencyForm.controller.initializeContent();
        });

        taxesSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().taxesForm);
            JeproLab.getInstance().getApplicationForms().taxesForm.controller.initializeContent();
        });

        addTaxSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxForm);
            JeproLab.getInstance().getApplicationForms().addTaxForm.controller.initializeContent();
        });

        taxesGroupSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().taxGroupForm);
            JeproLab.getInstance().getApplicationForms().taxGroupForm.controller.initializeContent();
        });

        addTaxGroupSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxGroupForm);
            JeproLab.getInstance().getApplicationForms().addTaxGroupForm.controller.initializeContent();
        });

        laboratoryCarrierSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().carriersForm);
            JeproLab.getInstance().getApplicationForms().carriersForm.controller.initializeContent();
        });

        addLaboratoryCarrierSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCarrierForm);
            JeproLab.getInstance().getApplicationForms().addCarrierForm.controller.initializeContent(0);
        });
    }

    private void addHelpListeners(){
        fileManagementMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().fileManagementForm);
            JeproLab.getInstance().getApplicationForms().fileManagementForm.controller.initializeContent();
        });

        aboutJeproLabMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().aboutJeproLabForm);
            JeproLab.getInstance().getApplicationForms().aboutJeproLabForm.controller.initializeContent();
        });

        checkForJeproLabUpdate.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().updaterForm);
            JeproLab.getInstance().getApplicationForms().updaterForm.controller.initializeContent();
        });

        socialNetworksMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().socialNetworkSettingForm);
            JeproLab.getInstance().getApplicationForms().socialNetworkSettingForm.controller.initializeContent();
        });
    }

    private void addCustomerListeners(){
        customersSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().customersForm);
            JeproLab.getInstance().getApplicationForms().customersForm.controller.initializeContent();
        });

        addCustomerMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCustomerForm);
            JeproLab.getInstance().getApplicationForms().addCustomerForm.controller.initializeContent();
        });

        addressesSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addressesForm);
            JeproLab.getInstance().getApplicationForms().addressesForm.controller.initializeContent();
        });

        addAddressMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
            JeproLab.getInstance().getApplicationForms().addAddressForm.controller.initializeContent();
        });

        groupsSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().groupsForm);
            JeproLab.getInstance().getApplicationForms().groupsForm.controller.initializeContent();
        });

        addGroupMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addGroupForm);
            JeproLab.getInstance().getApplicationForms().addGroupForm.controller.initializeContent();
        });

        customerThreadsSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().customerThreadsForm);
            JeproLab.getInstance().getApplicationForms().customerThreadsForm.controller.initializeContent();
        });

        contactsSubMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().contactsForm);
            JeproLab.getInstance().getApplicationForms().contactsForm.controller.initializeContent();
        });
    }
}