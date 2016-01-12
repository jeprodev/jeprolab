package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAnalyzeAddController extends JeproLabController{
    private Label jeproLabFormTitle;

    @FXML
    public JeproFormPanel jeproLabAddAnalyseFormWrapper;
    public JeproFormPanelTitle jeproLabAddAnalyseFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddAnalyseFormContainerWrapper;

    public Tab jeproLabAnalyzeInformationTabForm, jeproLabAnalyzeAttachedFileTabForm;

    public Label jeproLabAnalyzeNameLabel, jeproLabAnalyzePublishedLabel, jeproLabAnalyzeReferenceLabel, jeproLabAnalyzeImageChooserLabel;
    public Label jeproLabAnalyzeShortDescriptionLabel, jeproLabAnalyzeDescriptionLabel, jeproLabAnalyzeImagesLabel, jeproLabAnalyzeTagLabel;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;

        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth))/2;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_ANALYSE_LABEL"));
        jeproLabFormTitle.getStyleClass().add("input-label");
        jeproLabAddAnalyseFormWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutX(.02 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutY(20);
        jeproLabAddAnalyseFormTitleWrapper.setPrefSize(0.96 * JeproLab.APP_WIDTH, 40);
        jeproLabAddAnalyseFormTitleWrapper.getChildren().add(jeproLabFormTitle);
        jeproLabAddAnalyseFormContainerWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormContainerWrapper.setLayoutY(40);

        jeproLabAnalyzeInformationTabForm.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabAnalyzeAttachedFileTabForm.setText(bundle.getString("JEPROLAB_ATTACHED_FILES_LABEL"));
    }
}