package com.jeprolab.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabUpdaterController extends JeproLabController {
    @FXML
    public Button updateButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        updateButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
    }

    @Override
    public void updateToolBar(){}
}
