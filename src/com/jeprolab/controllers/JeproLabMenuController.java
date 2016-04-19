package com.jeprolab.controllers;


import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.views.application.JeproLabApplicationForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import com.jeprolab.JeproLab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabMenuController extends JeproLabController {
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
    public MenuItem threadsSubMenuItem, contactsSubMenuItem;
    public MenuItem feedsSubMenuItem;
    public MenuItem zoneListSubMenuItem;
    public MenuItem addTaxSubMenuItem, addTaxGroupSubMenuItem, taxesGroupSubMenuItem, taxesSubMenuItem;
    public MenuItem addCountrySubMenu, editZoneSubMenu, addStateSubMenu, addCurrencySubMenu;
    public MenuItem requestListMenuItem, requestAddNewMenuItem, requestBillsMenuItem, requestAddBillMenuItem;
    public MenuItem requestComplaintMenuItem, requestAddNewComplaintMenuItem, requestRefundsMenuItem, requestAddNewRefundsMenuItem;
    public MenuItem requestStatusMenuItem, requestAddStatusMenuItem, requestMessagesMenuItem, requestAddMessageMenuItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        jeproLabMenuBar.setPrefWidth(JeproLab.APP_WIDTH);
        dashBoardMenu.setText(bundle.getString("JEPROLAB_DASHBOARD_LABEL"));
        dashBoardMenuItem.setText(bundle.getString("JEPROLAB_DASHBOARD_LABEL"));

        /**
         *
         */
        catalogMenu.setText(bundle.getString("JEPROLAB_CATALOGS_LABEL"));
        categorySubMenu.setText(bundle.getString("JEPROLAB_CATEGORIES_LABEL"));
        categorySubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        addCategoryMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        analyzeSubMenu.setText(bundle.getString("JEPROLAB_ANALYSES_LABEL"));
        analyzeSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_ANALYSE_LABEL"));
        addAnalyseSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ANALYSE_LABEL"));
        attachmentsSubMenu.setText(bundle.getString("JEPROLAB_ATTACHMENTS_LABEL"));
        attachmentSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_ATTACHMENT_LABEL"));
        addAttachmentSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " "  + bundle.getString("JEPROLAB_ATTACHMENT_LABEL"));

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
        addLaboratoryGroupSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_LABORATORY_GROUP_LABEL"));

        /*categoryMenuItem.setText(bundle.getString("JEPROLAB_CATEGORIES_LABEL"));
        aboutJeproLabMenuItem.setText(bundle.getString("JEPROLAB_ABOUT_JEPROLAB_LABEL"));
        feedsMenuItem.setText(bundle.getString("JEPROLAB_FEEDS_LABEL"));
        feedBackMenuItem.setText(bundle.getString("JEPROLAB_FEEDBACK_LABEL"));
        customersMenuItem
        analyzeMenuItem.setText(bundle.getString("JEPROLAB_ANALYSES_LABEL"));
        attachmentMenuItem.setText(bundle.getString("JEPROLAB_ATTACHMENTS_LABEL"));
        addressesMenuItem
        groupsMenuItem
        currentRequestMenuItem
        threadsMenuItem
        contactsMenuItem */

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
        requestAddNewComplaintMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_COMPLAINT_LABEL") + " " + bundle.getString("JEPROLAB__LABEL"));
        requestRefundsMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_REFUNDS_LABEL"));
        requestAddNewRefundsMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_REFUND_LABEL"));

        /**
         * Sample Menu
         */
        requestSampleMenu.setText(bundle.getString("JEPROLAB_SAMPLES_LABEL"));
        sampleListMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_SAMPLES_LABEL"));
        sampleAddNewMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_SAMPLE_LABEL"));
        /**
         *
         */
        localisationSubMenu.setText(bundle.getString("JEPROLAB_LOCALISATION_SETTINGS_LABEL"));
        taxesSubMenu.setText(bundle.getString("JEPROLAB_TAXES_LABEL"));
        countriesSubMenu.setText(bundle.getString("JEPROLAB_COUNTRIES_LABEL"));
        zonesSubMenu.setText(bundle.getString("JEPROLAB_ZONES_LABEL"));
        statesSubMenu.setText(bundle.getString("JEPROLAB_STATES_LABEL"));
        currenciesSubMenu.setText(bundle.getString("JEPROLAB_CURRENCIES_LABEL"));
        addTaxSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_TAX_LABEL"));
        taxesSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_TAX_LABEL"));
        taxesGroupSubMenuItem.setText(bundle.getString("JEPROLAB_LIST_OF_LABEL") + " " + bundle.getString("JEPROLAB_TAX_GROUP_LABEL"));
        addTaxGroupSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_TAX_GROUP_LABEL"));
        addCountrySubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        editZoneSubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
        addStateSubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_STATE_LABEL"));
        addCurrencySubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));

        updateToolBar();
    }

    public void handleDashBoardMenuEvent(ActionEvent event)throws IOException{
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleCategoriesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().categoryForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddCategoryMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAnalyzesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddAnalyseMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAttachmentsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().attachmentForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddAttachmentMenuEvent(ActionEvent event) throws IOException{
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAttachmentForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleCustomersMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().customersForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewCustomerMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCustomerForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddressesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addressesForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewAddressMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleGroupsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().groupsForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddGroupMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addGroupForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleRequestsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleCustomerThreadsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleContactsMenuEvents(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleFeedsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().feedsForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddFeedMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addFeedForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleFeedBackMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().feedBackForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleViewFeedBackMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().viewFeedBackForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleLaboratoriesMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().laboratoriesForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewLaboratoryMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoryForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleLaboratoryGroupsMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().laboratoriesGroupsForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewLaboratoryGroupMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoriesGroupForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleCustomerMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
        JeproLabContext.getContext().controller.initializeContent();
    }


    public void handleHelpMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewZoneMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addZoneForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewCountryMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCountryForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewStateMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addStateForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewCurrencyMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCurrencyForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewTaxMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddNewTaxGroupMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxGroupForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleSampleMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().sampleListForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAddSampleMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().sampleForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleZoneListMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().zonesForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleTaxesMenuEvent(ActionEvent event) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().taxesForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleTaxesGroupMenuEvent(ActionEvent event) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().taxGroupForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleCheckForJeproLabUpdate(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().updaterForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleRequestAddNewMenuEvent(ActionEvent event) throws  IOException {
        processAddEvent(JeproLab.getInstance().getApplicationForms().addRequestForm);
    }

    public void handleRequestBillsMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestAddBillMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestComplaintsMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestAddNewComplaintMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestRefundsMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestAddRefundMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestStatusMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestAddStatusMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestMessagesMenuEvent(ActionEvent event) throws  IOException {}
    public void handleRequestAddMessageMenuEvent(ActionEvent event) throws  IOException {}

    private void processAddEvent(JeproLabApplicationForm form) throws IOException{
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(form);
        JeproLabContext.getContext().controller.initializeContent();
    }

    @Override
    public void updateToolBar(){}
}