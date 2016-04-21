package com.jeprolab.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabController implements Initializable {
    protected ResourceBundle bundle;
    protected double formWidth;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
    }

    public void initializeContent(){

    }
}