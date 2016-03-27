package com.jeprolab.controllers;

import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 27/03/2014.
 */
public class JeproLabAddRequestController extends JeproLabController {
    @FXML
    public JeproFormPanel jeproLabRequestFormWrapper;
    public JeproFormPanelTitle jeproLabRequestFormTitleWrapper;
    public JeproFormPanelContainer jeproLabRequestContentWrapper;
    public Label jeproLabCustomerCompanyNameLabel, jeproLabResultTransmissionLabel, jeproLabRequestFirstContactLabel, jeproLabRequestSecondContactLabel;
    public Label jeproLabRequestThirdContactLabel, jeproLabRequestFourthContactLabel;
    public ComboBox <String> jeproLabRequestFirstContact, jeproLabRequestSecondContact, jeproLabRequestThirdContact, jeproLabRequestFourthContact;
    public TextField jeproLabCustomerCompanyName;
    public Pane jeproLabCustomerInformationWrapper, jeproLabCustomerInformationTitleWrapper, jeproLabCustomerInformationContentWrapper;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
    }
}