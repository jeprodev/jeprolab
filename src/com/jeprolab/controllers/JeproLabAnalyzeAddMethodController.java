package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabAnalyzeModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabAnalyzeAddMethodController extends JeproLabController{
    private Button saveBtn, cancelBtn;
    private JeproLabAnalyzeModel.JeproLabMethodModel method;
    @FXML
    public Label jeproLabAnalyzeMethodDesignationLabel, jeproLabAnalyzeMethodCodeLabel, jeproLabAnalyzeMethodThresholdLabel, jeproLabAnalyzeMethodThresholdUnitLabel;
    public TextField jeproLabAnalyzeMethodDesignation, jeproLabAnalyzeMethodCode, jeproLabAnalyzeMethodThreshold, jeproLabAnalyzeMethodThresholdUnit;
    public JeproFormPanel jeproLabMethodFormWrapper;
    public JeproFormPanelTitle jeproLabMethodFormTitleWrapper;
    public JeproFormPanelContainer jeproLabMethodFormContentWrapper;
    public GridPane jeproLabMethodFormLayout;
    public HBox jeproLabAnalyzeMethodThresholdWrapper;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 500;

        jeproLabMethodFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150), new ColumnConstraints(260)
        );

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_METHOD_LABEL"));
        formTitleLabel.setPrefWidth(formWidth);

        jeproLabMethodFormWrapper.setPrefWidth(formWidth);
        jeproLabMethodFormWrapper.setLayoutX((JeproLab.APP_WIDTH - formWidth)/2);
        jeproLabMethodFormWrapper.setLayoutY(40);
        jeproLabMethodFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabMethodFormTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabMethodFormContentWrapper.setLayoutY(40);
        jeproLabMethodFormContentWrapper.setPrefWidth(formWidth);

        jeproLabAnalyzeMethodDesignationLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabAnalyzeMethodCodeLabel.setText(bundle.getString("JEPROLAB_CODE_LABEL"));
        jeproLabAnalyzeMethodThresholdLabel.setText(bundle.getString("JEPROLAB_DETECTION_THRESHOLD_LABEL"));
        jeproLabAnalyzeMethodThresholdUnitLabel.setText("/");

        jeproLabAnalyzeMethodThresholdUnit.setPrefWidth(110);
        jeproLabAnalyzeMethodThreshold.setPrefWidth(120);

        GridPane.setMargin(jeproLabAnalyzeMethodDesignationLabel, new Insets(5, 10, 10, 10));
        GridPane.setMargin(jeproLabAnalyzeMethodDesignation, new Insets(10, 10, 10, 0));
        GridPane.setMargin(jeproLabAnalyzeMethodCodeLabel, new Insets(5, 10, 10, 10));
        GridPane.setMargin(jeproLabAnalyzeMethodCode, new Insets(10, 10, 10, 0));
        GridPane.setMargin(jeproLabAnalyzeMethodThresholdLabel, new Insets(5, 10, 10, 10));
        GridPane.setMargin(jeproLabAnalyzeMethodThresholdWrapper, new Insets(10, 10, 30, 0));

        //GridPane.setMargin(new Insets(10, 10, 10, 10));
        //GridPane.setMargin(new Insets(10, 10, 10, 10));
        //GridPane.setMargin(new Insets(10, 10, 10, 10));
    }

    @Override
    public void initializeContent(){
        initializeContent(0);
    }

    @Override
    public void initializeContent(int methodId){
        if(context == null){
            context = JeproLabContext.getContext();
        }
        this.loadMethod(methodId);

        jeproLabAnalyzeMethodDesignation.setText(method.name);
        jeproLabAnalyzeMethodCode.setText(method.code);
        jeproLabAnalyzeMethodThreshold.setAlignment(Pos.CENTER_RIGHT);
        jeproLabAnalyzeMethodThresholdUnit.setAlignment(Pos.CENTER_RIGHT);
        jeproLabAnalyzeMethodThreshold.setText(String.valueOf(method.threshold));
        jeproLabAnalyzeMethodThresholdUnit.setText(method.unit);

        updateToolBar();
        addEventListener();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveBtn = new Button();
        saveBtn.getStyleClass().add("save-btn");
        if(method.method_id > 0){
            saveBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        }else{
            saveBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"));
        cancelBtn.getStyleClass().add("cross-btn");
        commandWrapper.getChildren().addAll(saveBtn, cancelBtn);
    }

    private void addEventListener(){
        saveBtn.setOnAction(event -> {
            method.name = jeproLabAnalyzeMethodDesignation.getText();
            method.code = jeproLabAnalyzeMethodCode.getText();
            method.threshold = Float.parseFloat(jeproLabAnalyzeMethodThreshold.getText());
            method.unit = jeproLabAnalyzeMethodThresholdUnit.getText();

            if(method.method_id > 0){
                method.update();
            }else{
                method.save();
            }
        });

        cancelBtn.setOnAction(event -> {

        });
    }

    private void loadMethod(int methodId){
        if(methodId > 0){
            if(method == null || method.method_id != methodId){
                method = new JeproLabAnalyzeModel.JeproLabMethodModel(methodId);
            }
        }else{
            method = new JeproLabAnalyzeModel.JeproLabMethodModel();
        }
    }
}
