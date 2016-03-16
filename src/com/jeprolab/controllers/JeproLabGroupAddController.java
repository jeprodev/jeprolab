package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabGroupAddController extends JeproLabController{
    @FXML
    public JeproFormPanel jeproLabGroupFormWrapper;
    public JeproFormPanelTitle jeproLabGroupFormTitleWrapper;
    public JeproFormPanelContainer jeproLabGroupFormContainerWrapper;
    public GridPane groupInformationWrapper, groupModulesWrapper;
    public Label createNewGroupFormTitleLabel, jeproLabGroupNameLabel, jeproLabGroupReductionLabel, jeproLabGroupPriceDisplayMethodLabel;
    public Label jeproLabGroupShowPricesLabel;
    public JeproMultiLangTextField jeproLabGroupName;
    public JeproAppendButton jeproLabGroupReduction;
    public ComboBox jeproLabGroupPriceDisplayMethod;
    public JeproSwitchButton jeproLabGroupShowPrices;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 180;
        double inputColumnWidth = 250;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 15;

        jeproLabGroupFormWrapper.setPrefWidth(formWidth);
        jeproLabGroupFormWrapper.setLayoutX(posX);
        jeproLabGroupFormWrapper.setLayoutY(posY);

        jeproLabGroupFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabGroupFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabGroupFormContainerWrapper.setLayoutY(40);

        groupInformationWrapper.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );

        groupModulesWrapper.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(80),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(80),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(80)
        );
    }

    @Override
    public void updateToolBar(){}
}