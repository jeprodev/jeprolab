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

    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
    }

    public static boolean isStored(String cache_id){
        return false;
    }

    public void updateToolBar(){}
}