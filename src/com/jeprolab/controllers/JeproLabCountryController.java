package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCountryController extends JeproLabController {
    private Button addCountryBtn, addStateBtn;
    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        initializeContent();
    }

    @Override
    protected void initializeContent(){
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addCountryBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addStateBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addCountryBtn, addStateBtn);
    }
}