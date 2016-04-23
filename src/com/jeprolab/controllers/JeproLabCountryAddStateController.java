package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabCountryAddStateController extends JeproLabController {
    private Button saveButton, cancelButton;

    @FXML
    public JeproFormPanel jeproLabStateFormWrapper;
    public JeproFormPanelTitle jeproLabStateFormTitleWrapper;
    public JeproFormPanelContainer jeproLabStateFormContentWrapper;
    public Label jeproLabStateNameLabel, jeproLabStateIsoCodeLabel, jeproLabStateCountryZoneLabel;
    public Label jeproLabStateCountryLabel, jeproLabStatePublishedLabel;
    public TextField jeproLabStateName, jeproLabStateIsoCode;
    public ComboBox<String> jeproLabStateCountryZone, jeproLabStateCountry;
    public JeproSwitchButton jeproLabStatePublished;
    public GridPane jeproLabStateLayout;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double inputColumnLabelWidth = 180;
        double inputColumnWidth = 200;
        formWidth = inputColumnWidth + inputColumnLabelWidth + 80;

        jeproLabStateFormWrapper.setPrefWidth(formWidth);
        jeproLabStateFormWrapper.setLayoutX((JeproLab.APP_WIDTH - formWidth)/2);
        jeproLabStateFormWrapper.setLayoutY(50);
        jeproLabStateFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabStateFormTitleWrapper.getChildren().add(formTitleLabel);
        formTitleLabel.setPrefSize(formWidth, 40);

        jeproLabStateFormContentWrapper.setPrefWidth(formWidth);
        jeproLabStateFormContentWrapper.setLayoutY(40);
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setAlignment(Pos.CENTER);

        jeproLabStateLayout.getColumnConstraints().addAll(
                new ColumnConstraints(inputColumnLabelWidth), new ColumnConstraints(inputColumnWidth)
        );

        jeproLabStateNameLabel.setText(bundle.getString("JEPROLAB_STATE_NAME_LABEL"));
        jeproLabStateIsoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        jeproLabStateCountryZoneLabel.setText(bundle.getString("JEPROLAB_COUNTRY_ZONE_LABEL"));
        jeproLabStateCountryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        jeproLabStatePublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_STATE_LABEL"));
        saveButton = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"));
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"));

        GridPane.setMargin(jeproLabStateNameLabel, new Insets(5, 0, 5, 20));
        GridPane.setMargin(jeproLabStateIsoCodeLabel, new Insets(5, 0, 5, 20));
        GridPane.setMargin(jeproLabStateCountryZoneLabel, new Insets(5, 0, 5, 20));
        GridPane.setMargin(jeproLabStateCountryLabel, new Insets(5, 0, 5, 20));
        GridPane.setMargin(jeproLabStatePublishedLabel, new Insets(5, 0, 25, 20));
    }

    @Override
    public void initializeContent(){

    }

    @Override
    public void updateToolBar(){

    }
}