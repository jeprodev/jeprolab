package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAnalyzeAddController extends JeproLabController{
    private Label jeproLabFormTitle;

    @FXML
    public JeproFormPanel jeproLabAddAnalyseFormWrapper;
    public JeproFormPanelTitle jeproLabAddAnalyseFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddAnalyseFormContainerWrapper;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;

        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth))/2;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_ANALYSE_LABEL"));
        //jeproLabAddAnalyseFormWrapper;
        //jeproLabAddAnalyseFormTitleWrapper;
        //jeproLabAddAnalyseFormContainerWrapper;
    }
}