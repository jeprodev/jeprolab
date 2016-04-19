package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 19/04/15.
 */
public class JeproLabRequestAddSampleController extends JeproLabController{
    @FXML
    public JeproFormPanel jeproLabSampleFormWrapper;
    public JeproFormPanelTitle jeproLabSampleFormTitleWrapper;
    public JeproFormPanelContainer jeproLabSampleContentWrapper;
    public TabPane jeproLabSampleTabPane;
    public Tab jeproLabSampleInfoTab, jeproLabSampleResultTab;
    public GridPane jeproLabSampleInfoLayout, jeproLabSampleAnalyzeSelector, jeproLabSampleResultTabLayout;
    public Label jeproLabSampleRequestReferenceLabel, jeproLabSampleRequestReference, jeproLabSampleDesignationLabel;
    public Label jeproLabSampleReferenceLabel, jeproLabSampleLabel, jeproLabSampleAnalyzeSelectorLabel, jeproLabSampleTestDateLabel;
    public Label jeproLabSampleMatrixLabel;
    public TextField jeproLabSampleDesignation, jeproLabSampleReference;
    public ScrollPane jeproLabSampleAnalyzeSelectorContainer;
    public ComboBox<String> jeproLabSampleMatrix;
    public DatePicker jeproLabSampleTestDate;

    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        double formWidth = 0.98 * JeproLab.APP_WIDTH;

        jeproLabSampleFormWrapper.setPrefWidth(formWidth);
        jeproLabSampleFormWrapper.setLayoutY(10);
        jeproLabSampleFormWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabSampleFormTitleWrapper.setPrefSize(formWidth, 40);

        jeproLabSampleContentWrapper.setPrefWidth(formWidth);
        jeproLabSampleContentWrapper.setLayoutY(40);

        jeproLabSampleTabPane.setPrefWidth(formWidth);

        jeproLabSampleInfoLayout.getColumnConstraints().addAll(
                new ColumnConstraints(180), new ColumnConstraints(formWidth - 240)
        );

        jeproLabSampleResultTabLayout.getColumnConstraints().addAll(
                new ColumnConstraints(180), new ColumnConstraints(formWidth - 240)
        );

        GridPane.setMargin(jeproLabSampleRequestReferenceLabel, new Insets(5, 5, 10, 10));
        GridPane.setMargin(jeproLabSampleRequestReference, new Insets(5, 5, 10, 0));
        GridPane.setMargin(jeproLabSampleDesignationLabel, new Insets(5, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleDesignation, new Insets(5, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleReferenceLabel, new Insets(5, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleReference, new Insets(5, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleLabel, new Insets(5, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorLabel, new Insets(15, 5, 5, 10));
        GridPane.setValignment(jeproLabSampleAnalyzeSelectorLabel, VPos.TOP);
        GridPane.setMargin(jeproLabSampleTestDateLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleMatrixLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorContainer, new Insets(15, 5, 5, 0));

        jeproLabSampleAnalyzeSelectorContainer.setPrefHeight(JeproLab.APP_HEIGHT - 300);

        jeproLabSampleInfoTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabSampleResultTab.setText(bundle.getString("JEPROLAB_RESULT_LABEL"));

        jeproLabSampleRequestReferenceLabel.setText(bundle.getString("JEPROLAB_REQUEST_REFERENCE_LABEL"));
        //jeproLabSampleRequestReference.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabSampleDesignationLabel.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleReferenceLabel.setText(bundle.getString("JEPROLAB_SAMPLE_REFERENCE_LABEL"));
        jeproLabSampleLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabSampleAnalyzeSelectorLabel.setText(bundle.getString("JEPROLAB_ANALYSES_LABEL"));
        jeproLabSampleTestDateLabel.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleMatrixLabel.setText(bundle.getString("JEPROLAB_SAMPLE_MATRIX_LABEL"));

        Map<Integer, String> matrices = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        jeproLabSampleMatrix.setPromptText(bundle.getString("JEPROLAB_SELECT_MATRIX_LABEL"));
        for(Map.Entry<Integer, String> matrix : matrices.entrySet()){
            jeproLabSampleMatrix.getItems().add(matrix.getValue());
        }
    }

    @Override
    public void initializeContent(){
        List<JeproLabAnalyzeModel> analyzesList = JeproLabAnalyzeModel.getAnalyzeList();

        jeproLabSampleResultTabLayout.getColumnConstraints().addAll(
                new ColumnConstraints(150)
        );
        int analyzeRow = 0;
        for(JeproLabAnalyzeModel analyze : analyzesList){
            CheckBox analyzeCheck = new CheckBox(analyze.name.get("lang_" + context.language.language_id));
            jeproLabSampleAnalyzeSelector.add(analyzeCheck, 0, analyzeRow);
            GridPane.setMargin(analyzeCheck, new Insets(8, 2, 4, 4));
            analyzeRow++;
        }
    }
}
