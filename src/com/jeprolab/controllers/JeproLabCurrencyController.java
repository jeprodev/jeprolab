package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabCurrencyController extends JeproLabController{
    @FXML
    public TableView<JeproLabCurrencyRecord> jeproLabCurrencyTableView;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyIndexColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyColumn;
    public TableColumn<JeproLabCurrencyRecord, HBox> jeproLabCurrencyActionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
    }

    public static class JeproLabCurrencyRecord{

    }
}