package com.jeprolab.controllers;


import javafx.fxml.FXML;

//import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCustomerController extends JeproLabController{
    private ResourceBundle bundle;

    @FXML
    public Button addNewCustomerButton;
    public TextField customerSearchBox;
    public TableView customersTableView;
    public TableColumn customerListNumberColumn, customerCheckBoxColumn, customerTitleColumn, customerNameColumn;
    public TableColumn customerFistNameColumn, customerLastVisitColumn, customerDateAddColumn, customerAllowAdsColumn;
    public TableColumn customerActiveColumn, customerCompanyColumn, customerEmailAddressColumn,customerReportColumn, customerActionsColumn;

    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        customerListNumberColumn.setText("#");
        customerCheckBoxColumn.setText("");
        customerTitleColumn.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerNameColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerFistNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerLastVisitColumn.setText(bundle.getString("JEPROLAB_LAST_VISIT_LABEL"));
        customerDateAddColumn.setText(bundle.getString("JEPROLAB_DATE_ADD_LABEL"));
        customerAllowAdsColumn.setText(bundle.getString("JEPROLAB_ALLOWS_ADS_LABEL"));
        customerActiveColumn.setText(bundle.getString("JEPROLAB_ACTIVE_LABEL"));
        customerCompanyColumn.setText(bundle.getString("JEPROLAB_COMPANY_LABEL"));
        customerEmailAddressColumn.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerReportColumn.setText(bundle.getString("JEPROLAB_REPORT_LABEL"));
        customerActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        addNewCustomerButton.setText(bundle.getString("JEPROLAB_ADD_NEW_CUSTOMER_LABEL"));
    }

    @Override
    public void updateToolBar(){}
}