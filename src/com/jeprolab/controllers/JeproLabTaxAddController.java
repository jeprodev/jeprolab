package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabTaxAddController extends JeproLabController{
    private Button cancelBtn, saveTaxBtn;

    @FXML
    public JeproFormPanel jeproLabFormPanelWrapper;
    public JeproFormPanelTitle jeproLabFormPanelTitle;
    public JeproFormPanelContainer jeproLabFormPanelContainer;
    public GridPane jeproLabTaxLayout;
    public Label jeproLabTaxNameLabel, jeproLabTaxRateLabel, jeproLabPublishedLabel;
    public TextField jeproLabTaxRate;
    public JeproMultiLangTextField jeproLabTaxName;
    public JeproSwitchButton jeproLabPublished;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabTaxLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25)
        );

        jeproLabFormPanelWrapper.setLayoutX(posX);
        jeproLabFormPanelWrapper.setLayoutY(posY);

        jeproLabFormPanelTitle.setPrefSize(formWidth, 40);

        jeproLabFormPanelContainer.setPrefWidth(formWidth);
        jeproLabFormPanelContainer.setLayoutY(40);

        GridPane.setMargin(jeproLabTaxNameLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabTaxName, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabTaxRateLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabTaxRate, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabPublishedLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabPublished, new Insets(15, 10, 15, 10));

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
        commandWrapper.setSpacing(5);
        saveTaxBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveTaxBtn, cancelBtn);
    }
}