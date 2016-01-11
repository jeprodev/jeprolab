package com.jeprolab.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCategoryController extends JeproLabController {
    @FXML
    public TableView categoryTableView;
    public TableColumn categoryIndexColumn, categoryCheckBoxColumn, categoryStatusColumn, categoryNameColumn;
    public TableColumn categoryDescriptionColumn, categoryPositionColumn, categoryActionColumn;
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
    }

    @Override
    public void updateToolBar(){}
}