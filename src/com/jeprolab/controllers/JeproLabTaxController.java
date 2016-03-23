package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabTaxController extends JeproLabController{
    private Button addTaxBtn;
    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        initializeContent();
    }

    @Override
    public void initializeContent(){
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addTaxBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addTaxBtn);
    }
}
