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
    public Menu analyzeSubMenu, attachmentsSubMenu, feedsSubMenu, feedBackSubMenu;
    public MenuItem addAnalyseSubMenuItem, dashBoardMenuItem;
    public MenuItem addCustomerMenuItem;
    public MenuItem addAttachmentSubMenuItem;
    public MenuItem addCategoryMenuItem, aboutJeproLabMenuItem, addFeedMenuItem, feedBackMenuItem, customersMenuItem;
    public MenuItem analyzeMenuItem, attachmentMenuItem;
    public MenuItem addressesMenuItem, addAddressMenuItem;
    public MenuItem groupsMenuItem, addGroupMenuItem, currentRequestMenuItem;
    public MenuItem threadsMenuItem, contactsMenuItem;

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
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addFeedForm);
    }


    public void handleCustomerMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }


    public void handleHelpMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
    }

    @Override
    public void updateToolBar(){}
}