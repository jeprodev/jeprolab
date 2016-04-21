package com.jeprolab.views;

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
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabApplicationForm implements Runnable {
    private boolean isCreated = false;
    private Pane formWrapper = null;
    private String formLayoutPath;
    public JeproLabController controller;

    public JeproLabApplicationForm(String layoutPath){
        String layoutBasePath = "views/forms/";
        formLayoutPath = layoutBasePath + layoutPath;
    }


    @Override
    public void run() {

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

            isCreated = true;
        }
        return formWrapper;
    }

    public void setFormVisible(boolean visible){
        formWrapper.setVisible(visible);
    }
}