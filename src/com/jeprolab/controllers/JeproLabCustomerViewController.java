package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import jeprolab.assets.extend.controls.FormPanelTitle;

public class JeproLabCustomerViewController extends JeproLabController{
    @FXML
    public Label customerTitleLabel, customerTitle, customerAgeLabel, customerAge, customerRegistrationDateLabel;
    public Label customerRegistrationDate, customerLastVisitLabel, customerLastVisit, customerLanguageLabel, customerLanguage;
    public Label customerLaboratoryNameLabel, customerLaboratoryName, customerOptinLabel, customerOptin, customerIsActiveLabel;
    public Label customerIsActive;

    public FormPanelTitle customerInformationFormTitle, customerOrdersPanelTitle, customerAddressPanelTitle, customerGroupsPanelTitle;
    public FormPanelTitle customerVouchersPanelTitle, customerPrivateNotePanelTitle, customerMessagesPanelTitle, customerRequestPanelTitle;

    public TableColumn customerAddressIndexColumn, customerAddressCompanyColumn, customerAddressNameColumn, customerAddressAddressColumn;
    public TableColumn customerAddressCountryColumn, customerAddressPhoneNumberColumn, customerAddressActionColumn, customerOrderReferenceColumn;
    public TableColumn customerOrderColumn, customerOrderPaymentColumn, customerOrderStatusColumn, customerOrderAnalysesColumn;
    public TableColumn customerOrderTotalSpentColumn;

    public Text customerPrivateNoteWarning;

    public TextArea customerPrivateNoteMessage;

    public Button savePrivateMessageButton;


    @Override
    public void updateToolBar(){}
}