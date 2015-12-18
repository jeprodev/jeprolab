package com.jeprolab.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import jeprolab.JeproLab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabMenuController extends JeproLabController {
    @FXML
    public VBox jeproLabMenuBarWrapper;
    public MenuBar jeproLabMenuBar;
    public Menu dashBoardMenu, catalogMenu, customerMenu, helpMenu, feedsMenu, settingMenu;
    public MenuItem categoryMenuItem, aboutJeproLabMenuItem, feedsMenuItem, feedBackMenuItem, customersMenuItem;
    public MenuItem analyzeMenuItem, attachmentMenuItem, addressesMenuItem, groupsMenuItem, currentRequestMenuItem;
    public MenuItem threadsMenuItem, contactsMenuItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        jeproLabMenuBar.setPrefWidth(JeproLab.APP_WIDTH);
        dashBoardMenu.setText(bundle.getString("JEPROLAB_DASHBOARD_LABEL"));
        catalogMenu.setText(bundle.getString("JEPROLAB_CATALOGS_LABEL"));
        customerMenu.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        helpMenu.setText(bundle.getString("JEPROLAB_HELP_LABEL"));
        feedsMenu.setText(bundle.getString("JEPROLAB_FEEDS_LABEL"));
        settingMenu.setText(bundle.getString("JEPROLAB_SETTINGS_LABEL"));

        categoryMenuItem.setText(bundle.getString("JEPROLAB_CATEGORIES_LABEL"));
        aboutJeproLabMenuItem.setText(bundle.getString("JEPROLAB_ABOUT_JEPROLAB_LABEL"));
        feedsMenuItem.setText(bundle.getString("JEPROLAB_FEEDS_LABEL"));
        feedBackMenuItem.setText(bundle.getString("JEPROLAB_FEEDBACK_LABEL"));
        customersMenuItem.setText(bundle.getString("JEPROLAB_CUSTOMERS_LABEL"));
        analyzeMenuItem.setText(bundle.getString("JEPROLAB_ANALYSES_LABEL"));
        attachmentMenuItem.setText(bundle.getString("JEPROLAB_ATTACHMENTS_LABEL"));
        addressesMenuItem.setText(bundle.getString("JEPROLAB_ADDRESSES_LABEL"));
        groupsMenuItem.setText(bundle.getString("JEPROLAB_GROUPS_LABEL"));
        currentRequestMenuItem.setText(bundle.getString("JEPROLAB_REQUESTS_LABEL"));
        threadsMenuItem.setText(bundle.getString("JEPROLAB_THREADS_LABEL"));
        contactsMenuItem.setText(bundle.getString("JEPROLAB_CONTACTS_LABEL"));
    }

    public void handleCategoriesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().categoryForm);
    }


    public void handleAnalyzesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
    }


    public void handleAttachmentsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().attachmentForm);
    }


    public void handleCustomersMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().customersForm);
    }


    public void handleAddressesMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addressesForm);
    }


    public void handleGroupsMenuEvent(ActionEvent event) throws IOException {
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().groupsForm);
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
        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
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