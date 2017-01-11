package com.jeprolab.controllers;

import com.jeprolab.assets.tools.JeproLabContext;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabController implements Initializable {
    public boolean has_errors = false;
    protected ResourceBundle bundle;
    protected JeproLabContext context;
    protected double formWidth;
    protected double labelColumnWidth = 150;
    protected double inputColumnWidth = 200;
    protected Label formTitleLabel;
    public static final double rowHeight = 25;

    public boolean multi_laboratory_context;

    public String laboratory_link_type = "";

    protected static boolean isInitialized = false;

    public final double btnSize = 22;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        context.controller = this;

        formTitleLabel = new Label();
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setAlignment(Pos.CENTER);
    }

    public void initializeContent(){ }

    public void updateToolBar(){}
}
