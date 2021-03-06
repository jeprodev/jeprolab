package com.jeprolab.views.application;

import com.jeprolab.JeproLab;
import com.jeprolab.controllers.JeproLabController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabApplicationForm{
    private boolean isCreated = false;
    private Pane formWrapper = null;
    private String formLayoutPath;
    public JeproLabController controller;

    public JeproLabApplicationForm(String layoutPath){
        String layoutBasePath = "views/application/forms/";
        formLayoutPath = layoutBasePath + layoutPath;
    }

    public Node createView() throws IOException {
        if(!isCreated || formWrapper == null) {
            formWrapper = new Pane();
            URL location = JeproLab.class.getResource(formLayoutPath);
            FXMLLoader formLoader = new FXMLLoader();
            formLoader.setLocation(location);
            formLoader.setBuilderFactory(new JavaFXBuilderFactory());
            formLoader.setResources(JeproLab.getBundle());
            formWrapper = formLoader.load(location.openStream());
            controller = formLoader.getController();
            controller.task = "add";
            isCreated = true;
        }
        return formWrapper;
    }

    public void setFormVisible(boolean visible){
        formWrapper.setVisible(visible);
    }

    public void updateFormCommand(){
        //controller.updateToolBar();
    }
}
