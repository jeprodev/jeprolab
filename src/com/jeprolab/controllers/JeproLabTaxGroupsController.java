package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 05/03/2014.
 */
public class JeproLabTaxGroupsController extends JeproLabController{
    private Button addTaxGroupBtn;
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
        addTaxGroupBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addTaxGroupBtn);
    }
}
