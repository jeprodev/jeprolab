package com.jeprolab.controllers;


import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabDashboardController extends JeproLabController{
    @FXML
    Pane dashboardPageWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
    }


    @Override
    public void updateToolBar(){}
}