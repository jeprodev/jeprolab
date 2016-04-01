package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabRequestController extends JeproLabController{
    @FXML
    public TableView<JeproLabRequestRecord> requestTableView;
    public TableColumn<JeproLabRequestRecord, String> requestTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){}

    @Override
    public void initializeContent(){}

    @Override
    public void updateToolBar(){}

    public static class JeproLabRequestRecord{

    }
}