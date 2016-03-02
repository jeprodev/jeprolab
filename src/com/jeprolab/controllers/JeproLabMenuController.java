package com.jeprolab.controllers;


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
    public Menu dashBoardMenu, catalogMenu, customerMenu, helpMenu, feedsMenu, settingMenu;
    public Menu categorySubMenu, customersSubMenu, addressesSubMenu, groupsSubMenu, currentSubRequestMenu, threadsSubMenu, contactsSubMenu;
    public Menu analyzeSubMenu, attachmentsSubMenu, feedsSubMenu, feedBackSubMenu, laboratorySubMenu, laboratoryGroupSubMenu;
    public Menu localisationSubMenu, taxesSubMenu, countriesSubMenu, zonesSubMenu, statesSubMenu, currenciesSubMenu;
    public MenuItem addAnalyseSubMenuItem, dashBoardMenuItem;
    public MenuItem addCustomerMenuItem, customersSubMenuItem;
    public MenuItem addAttachmentSubMenuItem, attachmentSubMenuItem;
    public MenuItem addCategoryMenuItem, categorySubMenuItem, addFeedMenuItem, feedBackMenuItem, customersMenuItem;
    public MenuItem aboutJeproLabMenuItem, checkForJeproLabUpdate;
    public MenuItem addLaboratorySubMenuItem, addLaboratoryGroupSubMenuItem;
    public MenuItem analyzeSubMenuItem, attachmentMenuItem;
    public MenuItem addressesSubMenuItem, addAddressMenuItem;
    public MenuItem groupsMenuItem, addGroupMenuItem, currentRequestMenuItem;
    public MenuItem threadsMenuItem, contactsSubMenuItem;
    public MenuItem feedsSubMenuItem;
    public MenuItem zoneListSubMenuItem;
    public MenuItem addTaxSubMenuItem, addTaxGroupSubMenuItem, addCountrySubMenu, editZoneSubMenu, addStateSubMenu, addCurrencySubMenu;

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
        addCategoryMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        analyzeSubMenu.setText(bundle.getString("JEPROLAB_ANALYSES_LABEL"));
        addAnalyseSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ANALYSE_LABEL"));
        attachmentsSubMenu.setText(bundle.getString("JEPROLAB_ATTACHMENTS_LABEL"));
        addAttachmentSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " "  + bundle.getString("JEPROLAB_ATTACHMENT_LABEL"));

        /**
         *
         */
        customerMenu.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        customersSubMenu.setText(bundle.getString("JEPROLAB_CUSTOMERS_LABEL"));
        addCustomerMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CUSTOMER_LABEL"));

        addressesSubMenu.setText(bundle.getString("JEPROLAB_ADDRESSES_LABEL"));
        addAddressMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ADDRESS_LABEL"));

        groupsSubMenu.setText(bundle.getString("JEPROLAB_GROUPS_LABEL"));
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
        addFeedMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_FEED_LABEL"));
        feedBackMenuItem.setText(bundle.getString("JEPROLAB_FEEDBACK_LABEL"));

        /**
         *
         */
        settingMenu.setText(bundle.getString("JEPROLAB_SETTINGS_LABEL"));
        laboratorySubMenu.setText(bundle.getString("JEPROLAB_LABORATORIES_LABEL"));
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
        localisationSubMenu.setText(bundle.getString("JEPROLAB_LOCALISATION_SETTINGS_LABEL"));
        taxesSubMenu.setText(bundle.getString("JEPROLAB_TAXES_LABEL"));
        countriesSubMenu.setText(bundle.getString("JEPROLAB_COUNTRIES_LABEL"));
        zonesSubMenu.setText(bundle.getString("JEPROLAB_ZONES_LABEL"));
        statesSubMenu.setText(bundle.getString("JEPROLAB_STATES_LABEL"));
        currenciesSubMenu.setText(bundle.getString("JEPROLAB_CURRENCIES_LABEL"));
        addTaxSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_TAX_LABEL"));
        addTaxGroupSubMenuItem.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_TAX_GROUP_LABEL"));
        addCountrySubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        editZoneSubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
        addStateSubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_STATE_LABEL"));
        addCurrencySubMenu.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));
    }

    public void handleDashBoardMenuEvent(ActionEvent event)throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
    }

    public void handleCategoriesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().categoryForm);
    }


    public void handleAddCategoryMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCategoryForm);
    }


    public void handleAnalyzesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
    }


    public void handleAddAnalyseMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeForm);
    }


    public void handleAttachmentsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().attachmentForm);
    }

    public void handleAddAttachmentMenuEvent(ActionEvent event) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAttachmentForm);
    }


    public void handleCustomersMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().customersForm);
    }


    public void handleAddNewCustomerMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCustomerForm);
    }


    public void handleAddressesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addressesForm);
    }


    public void handleAddNewAddressMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
    }


    public void handleGroupsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().groupsForm);
    }


    public void handleAddGroupMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addGroupForm);
    }


    public void handleRequestsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }


    public void handleCustomerThreadsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }


    public void handleContactsMenuEvents(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }


    public void handleFeedsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().feedsForm);
    }

    public void handleAddFeedMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addFeedForm);
    }

    public void handleFeedBackMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().feedBackForm);
    }

    public void handleViewFeedBackMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().viewFeedBackForm);
    }


    public void handleLaboratoriesMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().laboratoriesForm);
    }

    public void handleAddNewLaboratoryMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoryForm);
    }

    public void handleLaboratoryGroupsMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().laboratoriesGroupsForm);
    }

    public void handleAddNewLaboratoryGroupMenuEvent(ActionEvent evt) throws IOException{
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoriesGroupForm);
    }


    public void handleCustomerMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }


    public void handleHelpMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }

    public void handleAddNewZoneMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addZoneForm);
    }

    public void handleAddNewCountryMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCountryForm);
    }

    public void handleAddNewStateMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addStateForm);
    }

    public void handleAddNewCurrencyMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCurrencyForm);
    }

    public void handleAddNewTaxMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxForm);
    }

    public void handleAddNewTaxGroupMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxGroupForm);
    }

    public void handleZoneListMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().zonesForm);
    }

    public void handleCheckForJeproLabUpdate(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().updaterForm);
    }

    @Override
    public void updateToolBar(){}
}