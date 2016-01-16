package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

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
    public JeproImageSlider jeproLabAnalyzeSlider;

    public GridPane jeproLabAnalyzeInformationLayout;

    public TabPane jeproLabAnalyzeTabPane;
    public Tab jeproLabAnalyzeInformationTabForm, jeproLabAnalyzeAttachedFileTabForm;

    public Label jeproLabAnalyzeNameLabel, jeproLabAnalyzePublishedLabel, jeproLabAnalyzeReferenceLabel, jeproLabAnalyzeImageChooserLabel;
    public Label jeproLabAnalyzeShortDescriptionLabel, jeproLabAnalyzeDescriptionLabel, jeproLabAnalyzeImagesLabel, jeproLabAnalyzeTagLabel;
    public TextField jeproLabAnalyzeReference;
    public TextArea jeproLabAnalyzeShortDescription, jeproLabAnalyzeDescription;
    public JeproMultiLangTextField jeproLabAnalyzeName, jeproLabAnalyzeTags;
    public JeproSwitchButton jeproLabAnalyzePublished;
    public JeproImageChooser jeproLabAnalyzeImageChooser;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;

        double labelColumnWidth = 150;
        double inputColumnWidth = 370;
        double formWidth = 0.92 * JeproLab.APP_WIDTH;
        double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth))/2;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 15;
        double columnConstraintWidth = (formWidth / 4) - 10;

        jeproLabFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_ANALYSE_LABEL"));
        jeproLabFormTitle.getStyleClass().add("input-label");
        jeproLabAddAnalyseFormWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutX(.02 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutY(20);
        jeproLabAddAnalyseFormTitleWrapper.setPrefSize(0.96 * JeproLab.APP_WIDTH, 40);
        jeproLabAddAnalyseFormTitleWrapper.getChildren().add(jeproLabFormTitle);
        jeproLabAddAnalyseFormContainerWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormContainerWrapper.setLayoutY(40);

        jeproLabAnalyzeTabPane.setPrefWidth(0.96 * JeproLab.APP_WIDTH);

        jeproLabAnalyzeInformationLayout.getColumnConstraints().addAll(
                new ColumnConstraints(columnConstraintWidth), new ColumnConstraints(columnConstraintWidth),
                new ColumnConstraints(columnConstraintWidth), new ColumnConstraints(columnConstraintWidth)
        );
        jeproLabAnalyzeInformationLayout.setLayoutX(posY);

        /*** Tab Information **/
        jeproLabAnalyzeInformationTabForm.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        GridPane.setMargin(jeproLabAnalyzeNameLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzePublishedLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeReferenceLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeImageChooserLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeShortDescriptionLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeDescriptionLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeImagesLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeTagLabel, new Insets(5, 10, 30, 15));

        jeproLabAnalyzeNameLabel.setText(bundle.getString("JEPROLAB_ANALYSE_NAME_LABEL"));
        jeproLabAnalyzeNameLabel.getStyleClass().add("input-label");
        jeproLabAnalyzePublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabAnalyzePublishedLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeReferenceLabel.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabAnalyzeReferenceLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeImageChooserLabel.setText(bundle.getString("JEPROLAB_CHOOSE_IMAGE_LABEL"));
        jeproLabAnalyzeImageChooserLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeShortDescriptionLabel.setText(bundle.getString("JEPROLAB_SHORT_DESCRIPTION_LABEL"));
        GridPane.setValignment(jeproLabAnalyzeShortDescriptionLabel, VPos.TOP);
        jeproLabAnalyzeShortDescriptionLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeDescriptionLabel.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        GridPane.setValignment(jeproLabAnalyzeDescriptionLabel, VPos.TOP);
        jeproLabAnalyzeDescriptionLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeImagesLabel.setText(bundle.getString("JEPROLAB_IMAGES_LABEL"));
        jeproLabAnalyzeImagesLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeTagLabel.setText(bundle.getString("JEPROLAB_TAG_LABEL"));
        jeproLabAnalyzeTagLabel.getStyleClass().add("input-label");

        GridPane.setMargin(jeproLabAnalyzeShortDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeShortDescription.setPrefHeight(55);
        GridPane.setMargin(jeproLabAnalyzeDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeDescription.setPrefHeight(85);
        jeproLabAnalyzeAttachedFileTabForm.setText(bundle.getString("JEPROLAB_ATTACHED_FILES_LABEL"));

    }
}