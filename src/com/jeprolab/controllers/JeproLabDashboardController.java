package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabDashboardController extends JeproLabController{
    public Button monthButton, dayButton, yearButton, previousMonthButton, previousDayButton, previousYearButton;
    @FXML
    Pane dashboardPageWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        dayButton = new Button(bundle.getString("JEPROLAB_DAY_LABEL"));
        dayButton.getStyleClass().addAll("first");
        monthButton = new Button(bundle.getString("JEPROLAB_MONTH_LABEL"));

        yearButton = new Button(bundle.getString("JEPROLAB_YEAR_LABEL"));
        previousMonthButton = new Button(bundle.getString("JEPROLAB_PREVIOUS_MONTH_LABEL"));
        previousDayButton = new Button(bundle.getString("JEPROLAB_PREVIOUS_DAY_LABEL"));
        previousYearButton = new Button(bundle.getString("JEPROLAB_PREVIOUS_YEAR_LABEL"));
        previousYearButton.getStyleClass().addAll("last", "capsule");

        JeproLab.getInstance().getApplicationToolBarCommandWrapper().getChildren().setAll(dayButton, monthButton, yearButton, previousDayButton, previousMonthButton, previousYearButton);
    }


    @Override
    public void updateToolBar(){}
}