package com.jeprolab.controllers;

import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 24/01/2014.
 */
public class JeproLabFeedAddController extends JeproLabController{

    @FXML
    public Label jeproLabFeedTitleLabel, jeproLabFeedLinkLabel, jeproLabFeedDescriptionLabel, jeproLabFeedAuthorLabel;
    public GridPane jeproLabFeedLayout;
    public JeproFormPanel jeproLabFeedEditForm;
    public JeproFormPanelTitle jeproLabFeedPaneTitle;
    public JeproFormPanelContainer jeproLabFeedPaneContainer;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;

        jeproLabFeedTitleLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedLinkLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedDescriptionLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedAuthorLabel.setText(bundle.getString("JEPROLAB_LABEL"));
    }
}