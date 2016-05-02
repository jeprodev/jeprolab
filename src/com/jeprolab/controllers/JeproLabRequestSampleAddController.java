package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabLanguageModel;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabRequestSampleAddController extends JeproLabController {
    JeproLabRequestModel.JeproLabSampleModel sample;
    @FXML
    public JeproFormPanel jeproLabSampleFormWrapper;
    public JeproFormPanelTitle jeproLabSampleFormTitleWrapper;
    public JeproFormPanelContainer jeproLabSampleContentWrapper;
    public TabPane jeproLabSampleTabPane;
    public Tab jeproLabSampleInfoTab, jeproLabSampleResultTab;
    public GridPane jeproLabSampleInfoLayout, jeproLabSampleAnalyzeSelector, jeproLabSampleResultTabLayout;
    public Label jeproLabSampleRequestReferenceLabel, jeproLabSampleRequestReference, jeproLabSampleDesignationLabel;
    public Label jeproLabSampleReferenceLabel, jeproLabSampleRemovalDateLabel, jeproLabSampleAnalyzeSelectorLabel;
    public Label jeproLabSampleMatrixLabel, jeproLabSampleTestDateLabel, jeproLabSampleReceivedDateLabel;
    public Label jeproLabSampleReceiveConditionLabel, jeproLabSampleTemperatureLabel;
    public TextField jeproLabSampleDesignation, jeproLabSampleReference, jeproLabSampleTemperature;
    public ScrollPane jeproLabSampleAnalyzeSelectorContainer;
    public ComboBox<String> jeproLabSampleMatrix, jeproLabSampleReceiveCondition, jeproLabSampleTemperatureUnit;
    public DatePicker jeproLabSampleTestDate, jeproLabSampleReceivedDate, jeproLabSampleRemovalDate;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;

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

        GridPane.setMargin(jeproLabSampleRequestReferenceLabel, new Insets(15, 5, 10, 10));
        GridPane.setMargin(jeproLabSampleRequestReference, new Insets(15, 5, 10, 0));
        GridPane.setMargin(jeproLabSampleDesignationLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleDesignation, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleReferenceLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleReference, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleRemovalDateLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleRemovalDate, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleReceivedDateLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleReceivedDate, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleReceiveConditionLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleReceiveCondition, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleTemperatureLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleTemperature, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorLabel, new Insets(15, 5, 5, 10));
        GridPane.setValignment(jeproLabSampleAnalyzeSelectorLabel, VPos.TOP);
        GridPane.setMargin(jeproLabSampleTestDateLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleMatrixLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorContainer, new Insets(15, 5, 5, 0));

        jeproLabSampleAnalyzeSelectorContainer.setPrefHeight(JeproLab.APP_HEIGHT - 300);

        jeproLabSampleInfoTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabSampleResultTab.setText(bundle.getString("JEPROLAB_RESULT_LABEL"));
        jeproLabSampleRequestReferenceLabel.setText(bundle.getString("JEPROLAB_REQUEST_REFERENCE_LABEL"));
        jeproLabSampleDesignationLabel.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleReferenceLabel.setText(bundle.getString("JEPROLAB_SAMPLE_REFERENCE_LABEL"));
        jeproLabSampleRemovalDateLabel.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleReceivedDateLabel.setText(bundle.getString("JEPROLAB_RECEIVED_DATE_LABEL"));
        jeproLabSampleReceiveConditionLabel.setText(bundle.getString("JEPROLAB_RECEIVED_CONDITION_LABEL"));
        jeproLabSampleTemperatureLabel.setText(bundle.getString("JEPROLAB_TEMPERATURE_LABEL"));
        jeproLabSampleAnalyzeSelectorLabel.setText(bundle.getString("JEPROLAB_ANALYSES_LABEL"));
        jeproLabSampleTestDateLabel.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleMatrixLabel.setText(bundle.getString("JEPROLAB_SAMPLE_MATRIX_LABEL"));

        Map<Integer, String> matrices = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        jeproLabSampleMatrix.setPromptText(bundle.getString("JEPROLAB_SELECT_MATRIX_LABEL"));
        for(Map.Entry<Integer, String> matrix : matrices.entrySet()){
            jeproLabSampleMatrix.getItems().add(matrix.getValue());
        }

        jeproLabSampleTemperatureUnit.getItems().addAll(
                bundle.getString("JEPROLAB_CELSIUS_LABEL"), bundle.getString("JEPROLAB_FAHRENHEIT_LABEL"), bundle.getString("JEPROLAB_KELVIN_LABEL")
        );
        jeproLabSampleTemperatureUnit.setValue(bundle.getString("JEPROLAB_CELSIUS_LABEL"));
        jeproLabSampleTemperatureUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
            double current = Double.parseDouble(jeproLabSampleTemperature.getText());
            String from = "celsius", to = "kelvin";
            if(oldValue.equals(bundle.getString("JEPROLAB_CELSIUS_LABEL"))){
                from = "celsius";
            }else if(oldValue.equals(bundle.getString("JEPROLAB_FAHRENHEIT_LABEL"))){
                from = "fahrenheit";
            }else if(oldValue.equals(bundle.getString("JEPROLAB_KELVIN_LABEL"))){
                from = "kelvin";
            }

            if(newValue.equals(bundle.getString("JEPROLAB_CELSIUS_LABEL"))){
                to = "celsius";
            }else if(newValue.equals(bundle.getString("JEPROLAB_FAHRENHEIT_LABEL"))){
                to = "fahrenheit";
            }else if(newValue.equals(bundle.getString("JEPROLAB_KELVIN_LABEL"))){
                to = "kelvin";
            }

            jeproLabSampleTemperature.setText(String.valueOf(JeproLabTools.convertTemperature(current, from, to)));
        });
        jeproLabSampleTemperature.setPrefWidth(80);
        jeproLabSampleTemperature.setAlignment(Pos.CENTER_RIGHT);
    }

    @Override
    public void initializeContent(){
        List<JeproLabAnalyzeModel> analyzesList = JeproLabAnalyzeModel.getAnalyzeList();
        loadSample();
        Map<Integer, String> matrices = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        if(sample.sample_id > 0){
            jeproLabSampleReference.setText(sample.reference);
            jeproLabSampleReference.setDisable(true);
            jeproLabSampleDesignation.setText(sample.designation);
            jeproLabSampleMatrix.setValue(matrices.get(sample.matrix_id));
        }

        jeproLabSampleResultTabLayout.getColumnConstraints().addAll(
                new ColumnConstraints(150)
        );
        int analyzeRow = 1;

        GridPane headerLayout = new GridPane();
        Label analyzeLabel = new Label(bundle.getString("JEPROLAB_ANALYSE_LABEL"));
        analyzeLabel.getStyleClass().add("input-label");
        analyzeLabel.setAlignment(Pos.CENTER);
        analyzeLabel.setPrefWidth(80);
        headerLayout.add(analyzeLabel, 0, 0);

        Label analyzeUnitLabel = new Label(bundle.getString("JEPROLAB_UNIT_LABEL"));
        analyzeUnitLabel.getStyleClass().add("input-label");
        analyzeUnitLabel.setAlignment(Pos.CENTER);
        analyzeUnitLabel.setPrefWidth(50);
        headerLayout.add(analyzeUnitLabel, 1, 0);

        Label analyzeMethodLabel = new Label(bundle.getString("JEPROLAB_METHOD_LABEL"));
        analyzeMethodLabel.getStyleClass().add("input-label");
        analyzeMethodLabel.setAlignment(Pos.CENTER);
        analyzeMethodLabel.setPrefWidth(90);
        headerLayout.add(analyzeMethodLabel, 2, 0);

        Label analyzeResultLabel = new Label(bundle.getString("JEPROLAB_RESULT_LABEL"));
        analyzeResultLabel.getStyleClass().add("input-label");
        analyzeResultLabel.setAlignment(Pos.CENTER);
        analyzeResultLabel.setPrefWidth(90);
        headerLayout.add(analyzeResultLabel, 3, 0);

        jeproLabSampleAnalyzeSelector.add(headerLayout, 0, 0);
        for(JeproLabAnalyzeModel analyze : analyzesList){
            JeproLabSampleAnalyzeForm analyzeCheckForm = new JeproLabSampleAnalyzeForm(analyze);
            if(sample.analyzes.contains(analyze.analyze_id)){ analyzeCheckForm.setSelected(true); }
            jeproLabSampleAnalyzeSelector.add(analyzeCheckForm, 0, analyzeRow);
            GridPane.setMargin(analyzeCheckForm, new Insets(8, 2, 4, 4));
            analyzeRow++;
        }
    }

    private void loadSample() {
        int sampleId = JeproLab.request.getRequest().containsKey("sample_id") ? Integer.parseInt(JeproLab.request.getRequest().get("sample_id")) : 0;

        boolean isLoaded = false;
        if (sampleId > 0) {
            if (sample == null) {
                sample = new JeproLabRequestModel.JeproLabSampleModel(sampleId);
            }

            if (sample.sample_id <= 0) {
                JeproLabTools.displayError(500, bundle.getString("JEPROLAB_SAMPLE_NOT_FOUND_MESSAGE"));
                isLoaded = false;
            } else {
                isLoaded = true;
            }
        } else {
            if (sample == null) {
                sample = new JeproLabRequestModel.JeproLabSampleModel();
            }
        }

        /*//specified
        if (isLoaded && analyze.analyze_id > 0) {
            if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && JeproLabLaboratoryModel.isFeaturePublished() && !analyze.isAssociatedToLaboratory()) {
                sample = new JeproLabSampleModel(analyze.analyze_id, false, 0, analyze.default_laboratory_id);
            }
            analyze.loadStockData();
        }*/
    }

    private static class JeproLabSampleAnalyzeForm extends HBox {
        private static JeproLabContext context = JeproLabContext.getContext();
        private  Map<Integer, String> methods;
        private CheckBox analyzeCheck;
        private TextField jeproLabResultUnit, jeproLabAnalyzeResult;
        public ComboBox<String> jeproLabMethod;

        public JeproLabSampleAnalyzeForm(JeproLabAnalyzeModel analyze){
            int jeproLabResultUnitWidth = 50;
            analyzeCheck = new CheckBox(analyze.name.get("lang_" + context.language.language_id));
            GridPane formLayout = new GridPane();
            jeproLabResultUnit = new TextField();
            jeproLabAnalyzeResult = new TextField();
            jeproLabResultUnit.setPrefWidth(jeproLabResultUnitWidth);
            jeproLabMethod = new ComboBox<>();
            jeproLabMethod.setPrefWidth(100);

            methods = analyze.getMethods();
            for(Map.Entry<Integer, String> entry : methods.entrySet()){
                jeproLabMethod.getItems().add(entry.getValue());
            }

            formLayout.add(analyzeCheck, 0, 0);
            formLayout.add(jeproLabResultUnit, 1, 0);
            formLayout.add(jeproLabMethod, 2, 0);
            formLayout.add(jeproLabAnalyzeResult, 3, 0);

            GridPane.setMargin(analyzeCheck, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabResultUnit, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabMethod, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabAnalyzeResult, new Insets(0, 5, 0, 5));

            formLayout.getColumnConstraints().addAll(
                    new ColumnConstraints(80), new ColumnConstraints(jeproLabResultUnitWidth + 5),
                    new ColumnConstraints(110), new ColumnConstraints(200)
            );
            this.setSpacing(0);
            this.getChildren().add(formLayout);
        }

        public void setSelected(boolean isSelected){
            analyzeCheck.setSelected(isSelected);
        }
    }

}
