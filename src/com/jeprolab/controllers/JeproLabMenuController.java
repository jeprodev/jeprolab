package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabSettingModel;
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
    public Menu requestComplaintMenu, requestRefundMenu, requestStatusMenu, requestMessageMenu, requestSampleMenu;
    public MenuItem addAnalyseSubMenuItem, dashBoardMenuItem, sampleListMenuItem, sampleAddNewMenuItem;
    public MenuItem addCustomerMenuItem, customersSubMenuItem;
    public MenuItem addAttachmentSubMenuItem, attachmentSubMenuItem;
    public MenuItem addCategoryMenuItem, categorySubMenuItem, addFeedMenuItem, feedBackMenuItem, customersMenuItem;
    public MenuItem aboutJeproLabMenuItem, checkForJeproLabUpdate;
    public MenuItem addLaboratorySubMenuItem, addLaboratoryGroupSubMenuItem, laboratorySubMenuItem;
    public MenuItem analyzeSubMenuItem;
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
        requestStatusMenu.setText(bundle.getString("JEPROLAB_REQUEST_LABEL") + " " + bundle.getString("JEPROLAB_STATUS_LABEL"));
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
    }

    /**
     *
     */
    public void initializeContent(){
        addDashboardListeners();
        addCatalogListeners();
        addCustomerListeners();
        addRequestsListeners();
        addFeedsListeners();
        addSettingsListeners();
        addHelpListeners();
    }

    /**
     * Managing dashboard menu and sub-menus
     */
    private void addDashboardListeners(){
        dashBoardMenuItem.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
            JeproLabContext.getContext().controller.initializeContent();
        });
    }

    private void addCatalogListeners(){
        categorySubMenuItem.setOnAction(evt -> {
            JeproLabContext.getContext().category.category_id = JeproLabSettingModel.getIntValue("root_category");
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().categoryForm);
            JeproLabContext.getContext().controller.initializeContent();
        });

        addCategoryMenuItem.setOnAction(evt -> {
            //JeproLab.request.getRequest().clear();
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
            JeproLabContext.getContext().controller.initializeContent();
        });
    }

    private void addRequestsListeners(){}

    private void addFeedsListeners(){}

    private void addSettingsListeners(){}

    private void addHelpListeners(){}

    private void addCustomerListeners(){}
/*
    private void addListeners(){

 onAction="#handleCategoriesMenuEvent"
 onAction="#handleAddCategoryMenuEvent"
  onAction="#handleAnalyzesMenuEvent"
  onAction="#handleAddAnalyseMenuEvent"
  onAction="#handleAnalyzeMethodsMenuEvent"
  onAction="#handleAnalyzeAddMethodsMenuEvent"
  onAction="#handleAttachmentsMenuEvent"
  onAction="#handleAddAttachmentMenuEvent"
  onAction="#handleCustomersMenuEvent"
  onAction="#handleAddNewCustomerMenuEvent"
  onAction="#handleAddressesMenuEvent"
  onAction="#handleAddNewAddressMenuEvent"
  onAction="#handleGroupsMenuEvent"
  onAction="#handleAddGroupMenuEvent"
  onAction="#handleCustomerThreadsMenuEvent"
  onAction="#handleContactsMenuEvents"
  onAction="#handleRequestAddBillMenuEvent"
  onAction="#handleRequestComplaintsMenuEvent"
   onAction="#handleRequestAddNewMenuEvent"
    onAction="#handleRequestsMenuEvent"
     onAction="#handleFeedsMenuEvent"
      onAction="#handleLaboratoriesMenuEvent"
       onAction="#handleFeedBackMenuEvent"
  onAction="#handleRequestAddNewComplaintMenuEvent"
  onAction="#handleRequestRefundsMenuEvent"
  onAction="#handleRequestAddRefundMenuEvent"
  onAction="#handleRequestStatusMenuEvent"
  onAction="#handleRequestAddStatusMenuEvent"
  onAction="#handleRequestMessagesMenuEvent"
  onAction="#handleRequestAddMessageMenuEvent"
   onAction="#handleHelpMenuEvent"
   onAction="#handleAddNewTaxGroupMenuEvent"
   onAction="#handleTaxesGroupMenuEvent"
   onAction="#handleAddNewTaxMenuEvent"
   onAction="#handleTaxesMenuEvent"
   onAction="#handleAddNewCurrencyMenuEvent"
   onAction="#handleCurrenciesMenuEvent"
   onAction="#handleCheckForJeproLabUpdate"
    onAction="#handleAddNewZoneMenuEvent"
    onAction="#handleZoneListMenuEvent"
    onAction="#handleAddNewStateMenuEvent"
    onAction="#handleStateMenuEvent"
    onAction="#handleAddNewCountryMenuEvent"
    onAction="#handleCountryMenuEvent"
    onAction="#handleAddNewLaboratoryGroupMenuEvent"
    onAction="#handleLaboratoryGroupsMenuEvent"
     onAction="#handleAddNewLaboratoryMenuEvent"
     onAction="#handleAddFeedMenuEvent"
    }

    private void addListeners(){

    }
*/
}