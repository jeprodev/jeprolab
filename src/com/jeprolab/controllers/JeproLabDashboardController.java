package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabDashboardController extends JeproLabController{
    public ToggleGroup toggleButtonGroup;
    public ToggleButton monthButton, dayButton, yearButton, previousMonthButton, previousDayButton, previousYearButton;
    @FXML
    Pane dashboardPageWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        toggleButtonGroup = new ToggleGroup();
        dayButton = new ToggleButton(bundle.getString("JEPROLAB_DAY_LABEL"));
        dayButton.setToggleGroup(toggleButtonGroup);
        dayButton.getStyleClass().addAll("first");
        monthButton = new ToggleButton(bundle.getString("JEPROLAB_MONTH_LABEL"));
        monthButton.setToggleGroup(toggleButtonGroup);
        yearButton = new ToggleButton(bundle.getString("JEPROLAB_YEAR_LABEL"));
        yearButton.setToggleGroup(toggleButtonGroup);
        previousMonthButton = new ToggleButton(bundle.getString("JEPROLAB_PREVIOUS_MONTH_LABEL"));
        previousMonthButton.setToggleGroup(toggleButtonGroup);
        previousDayButton = new ToggleButton(bundle.getString("JEPROLAB_PREVIOUS_DAY_LABEL"));
        previousDayButton.setToggleGroup(toggleButtonGroup);
        previousYearButton = new ToggleButton(bundle.getString("JEPROLAB_PREVIOUS_YEAR_LABEL"));
        previousYearButton.getStyleClass().addAll("last", "capsule");
        previousYearButton.setToggleGroup(toggleButtonGroup);

        JeproLab.getInstance().getApplicationToolBarCommandWrapper().getChildren().setAll(dayButton, monthButton, yearButton, previousDayButton, previousMonthButton, previousYearButton);
    }


    @Override
    public void updateToolBar(){}
}