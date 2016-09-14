package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.views.JeproLabApplicationForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
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

        /**
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
        requestAddNewComplaintMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_COMPLAINT_LABEL") + " " + bundle.getString("JEPROLAB__LABEL"));
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

    public void handleCustomerThreadsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().customerThreadsForm);
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

    public void handleStateMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().stateForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleCurrenciesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().currencyForm);
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

    public void handleCountryMenuEvent(ActionEvent event) throws IOException {
        JeproLab.request.getRequest().clear();
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().countryForm);
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

    public void handleRequestsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleRequestAddNewMenuEvent(ActionEvent event) {
        processAddEvent(JeproLab.getInstance().getApplicationForms().addRequestForm);
    }

    public void handleRequestBillsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestBillsForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleRequestAddBillMenuEvent(ActionEvent event){
        processAddEvent(JeproLab.getInstance().getApplicationForms().requestAddBillForm);
    }

    public void handleRequestComplaintsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestComplainsForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleRequestAddNewComplaintMenuEvent(ActionEvent event){
        processAddEvent(JeproLab.getInstance().getApplicationForms().requestAddComplainForm);
    }

    public void handleRequestRefundsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestComplainsForm);
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleRequestAddRefundMenuEvent(ActionEvent event){
        processAddEvent(JeproLab.getInstance().getApplicationForms().requestAddRefundForm);
    }

    public void handleRequestStatusMenuEvent(ActionEvent event){
        processAddEvent(JeproLab.getInstance().getApplicationForms().requestStatusForm);
    }

    public void handleRequestAddStatusMenuEvent(ActionEvent event){}
    public void handleRequestMessagesMenuEvent(ActionEvent event){}
    public void handleRequestAddMessageMenuEvent(ActionEvent event){}

    private void processAddEvent(JeproLabApplicationForm form){
        try {
            JeproLab.request.getRequest().clear();
            JeproLab.getInstance().goToForm(form);
            JeproLabContext.getContext().controller.initializeContent();
        }catch (IOException  ignored){
            ignored.printStackTrace();
        }
    }

    public void handleAnalyzeMethodsMenuEvent(ActionEvent event) {
        try {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeMethodForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JeproLabContext.getContext().controller.initializeContent();
    }

    public void handleAnalyzeAddMethodsMenuEvent(ActionEvent event) {
        processAddEvent(JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm);
    }
}