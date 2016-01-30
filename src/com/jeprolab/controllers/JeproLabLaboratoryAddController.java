package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 07/01/15.
 */
public class JeproLabLaboratoryAddController extends JeproLabController {
    @FXML
    public JeproFormPanel jeproLabLaboratoryPanelWrapper;
    public JeproFormPanelTitle jeproLabLaboratoryPanelTitleWrapper;
    public JeproFormPanelContainer jeproLabLaboratoryPanelContainerWrapper;

    public TabPane jeproLabLaboratoryTab;
    public Tab jeproLabLaboratoryInformation, jeproLabLaboratoryUrls;
    public Label jeproLabLaboratoryNameLabel, jeproLabLaboratoryGroupLabel, jeproLabLaboratoryCategoryLabel, jeproLabLaboratoryThemeLabel;
    public Label jeproLabLaboratoryLabel, jeproLabLaboratoryDomainLabel, jeproLabLaboratorySslDomainLabel, jeproLabLaboratoryPhysicalUriLabel;
    public Label jeproLabLaboratoryMainUrlLabel;

    public TextField jeproLabLaboratoryName, jeproLabLaboratoryDomain, jeproLabLaboratorySslDomain, jeproLabLaboratoryPhysicalUri;
    public ComboBox jeproLabLaboratoryGroup, jeproLabLaboratoryCategory, jeproLabLaboratoryTheme;

    public JeproSwitchButton jeproLabLaboratoryPublished, jeproLabLaboratoryMainUrl;
    public TableView jeproLabLaboratoryUrlsTableView;
    public TableColumn jeproLabUrlIndexColumn, jeproLabUrlCheckBoxColumn, jeproLabUrlDomainColumn, jeproLabUrlSslDomainColumn, jeproLabUrlPhysicalUriColumn;
    public TableColumn jeproLabUrlVirtualUriColumn, jeproLabUrlMainColumn, jeproLabUrlActionColumn;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabLaboratoryPanelWrapper.setPrefWidth(formWidth);
        jeproLabLaboratoryPanelWrapper.setLayoutX(posX);
        jeproLabLaboratoryPanelWrapper.setLayoutY(posY);

        jeproLabLaboratoryPanelTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabLaboratoryPanelContainerWrapper.setPrefWidth(formWidth);
        jeproLabLaboratoryPanelContainerWrapper.setLayoutY(40);

        jeproLabLaboratoryTab.setPrefWidth(formWidth);

        jeproLabLaboratoryUrlsTableView.setPrefWidth(0.98 * formWidth);
        jeproLabLaboratoryUrlsTableView.setLayoutX(0.01 * formWidth);
    }
}