package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabRequestSampleAddController extends JeproLabController{
    JeproLabRequestModel.JeproLabSampleModel sample;
    private Button saveBtn, printTicket, cancelBtn, saveAndBackToRequestFormBtn;
    private Map<Integer, String> matrices, conditions;
    private static final double analyzeContainerWidth = 150;
    private static final double analyzeResultContainerWidth = 150;
    private static final double analyzeUnitContainerWidth = 110;
    private static final double analyzeMethodContainerWidth = 100;

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
    public Label jeproLabSampleReceiveConditionLabel, jeproLabSampleTemperatureLabel, jeproLabSampleSymbolLabel;
    public Label jeproLabSampleBundleLabel;
    public TextField jeproLabSampleDesignation, jeproLabSampleReference, jeproLabSampleTemperature, jeproLabSampleBundle;
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

        formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_SAMPLE_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        formTitleLabel.setAlignment(Pos.CENTER);
        jeproLabSampleFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabSampleFormTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabSampleContentWrapper.setPrefWidth(formWidth);
        jeproLabSampleContentWrapper.setLayoutY(40);

        jeproLabSampleTabPane.setPrefWidth(formWidth);

        jeproLabSampleInfoLayout.getColumnConstraints().addAll(
            new ColumnConstraints(180), new ColumnConstraints(formWidth - 240)
        );

        jeproLabSampleResultTabLayout.getColumnConstraints().addAll(
            new ColumnConstraints(120), new ColumnConstraints(formWidth - 150)
        );

        GridPane.setMargin(jeproLabSampleRequestReferenceLabel, new Insets(15, 5, 10, 10));
        GridPane.setMargin(jeproLabSampleRequestReference, new Insets(15, 5, 10, 0));
        GridPane.setMargin(jeproLabSampleDesignationLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleDesignation, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleReferenceLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleReference, new Insets(15, 5, 5, 0));
        GridPane.setMargin(jeproLabSampleBundleLabel, new Insets(15, 5, 5, 10));
        GridPane.setMargin(jeproLabSampleBundle, new Insets(15, 5, 5, 0));
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
        jeproLabSampleBundleLabel.setText(bundle.getString("JEPROLAB_SAMPLE_BUNDLE_LABEL"));
        jeproLabSampleReferenceLabel.setText(bundle.getString("JEPROLAB_SAMPLE_REFERENCE_LABEL"));
        jeproLabSampleRemovalDateLabel.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleReceivedDateLabel.setText(bundle.getString("JEPROLAB_RECEIVED_DATE_LABEL"));
        jeproLabSampleReceiveConditionLabel.setText(bundle.getString("JEPROLAB_RECEIVED_CONDITION_LABEL"));
        jeproLabSampleTemperatureLabel.setText(bundle.getString("JEPROLAB_TEMPERATURE_LABEL"));
        jeproLabSampleAnalyzeSelectorLabel.setText(bundle.getString("JEPROLAB_ANALYZES_LABEL"));
        jeproLabSampleTestDateLabel.setText(bundle.getString("JEPROLAB_TEST_DATE_LABEL"));
        jeproLabSampleMatrixLabel.setText(bundle.getString("JEPROLAB_SAMPLE_MATRIX_LABEL"));

        matrices = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        jeproLabSampleMatrix.setMinWidth(200);
        jeproLabSampleMatrix.setPromptText(bundle.getString("JEPROLAB_SELECT_MATRIX_LABEL"));
        for(Map.Entry<Integer, String> matrix : matrices.entrySet()){
            jeproLabSampleMatrix.getItems().add(matrix.getValue());
        }

        conditions = JeproLabRequestModel.JeproLabSampleReceptionConditionModel.getConditions(JeproLabContext.getContext().language.language_id);
        jeproLabSampleReceiveCondition.setPromptText(bundle.getString("JEPROLAB_SELECT_SAMPLE_CONDITION_LABEL"));
        jeproLabSampleReceiveCondition.setMinWidth(200);
        for(Map.Entry<Integer, String> condition : conditions.entrySet()){
            jeproLabSampleReceiveCondition.getItems().add(condition.getValue());
        }


        jeproLabSampleTemperatureUnit.getItems().addAll(
            bundle.getString("JEPROLAB_CELSIUS_LABEL"), bundle.getString("JEPROLAB_FAHRENHEIT_LABEL"), bundle.getString("JEPROLAB_KELVIN_LABEL")
        );
        jeproLabSampleTemperatureUnit.setValue(bundle.getString("JEPROLAB_CELSIUS_LABEL"));

        jeproLabSampleTemperature.setPrefWidth(80);
        jeproLabSampleTemperature.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(jeproLabSampleTemperature, new Insets(5, 0, 5, 0));
        HBox.setMargin(jeproLabSampleSymbolLabel, new Insets(6, 0, 5, 0));
        HBox.setMargin(jeproLabSampleTemperatureUnit, new Insets(5, 0, 5, 0));
    }


    @Override
    public void initializeContent(int sampleId){
        sample = null;
        List<JeproLabAnalyzeModel> analyzesList = JeproLabAnalyzeModel.getAnalyzeList();
        loadSample(sampleId);
       // Map<Integer, String> matrices = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        if(sample.sample_id > 0){
            jeproLabSampleRequestReference.setText(JeproLabRequestModel.getReferenceByRequestId(sample.request_id));
            jeproLabSampleReference.setText(sample.reference);
            jeproLabSampleReference.setDisable(true);
            jeproLabSampleDesignation.setText(sample.designation);
            jeproLabSampleBundle.setText(sample.lot);
            jeproLabSampleMatrix.setValue(matrices.get(sample.matrix_id));
            jeproLabSampleReceiveCondition.setValue(conditions.get(sample.condition_id));
            jeproLabSampleRemovalDate.setValue(JeproLabTools.getLocaleDate(sample.removal_date));
            jeproLabSampleReceivedDate.setValue(JeproLabTools.getLocaleDate(sample.received_date));
            jeproLabSampleTestDate.setValue(JeproLabTools.getLocaleDate(sample.test_date));
            jeproLabSampleTemperature.setText(sample.temperature);
            jeproLabSampleTemperatureUnit.setValue(sample.temperature_unit);
        }

        jeproLabSampleResultTabLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150)
        );
        int analyzeRow = 1;

        GridPane headerLayout = new GridPane();
        headerLayout.getColumnConstraints().addAll(
            new ColumnConstraints(analyzeContainerWidth), new ColumnConstraints(analyzeUnitContainerWidth),
            new ColumnConstraints(analyzeMethodContainerWidth), new ColumnConstraints(analyzeResultContainerWidth)
        );
        Label analyzeLabel = new Label(bundle.getString("JEPROLAB_ANALYZE_LABEL"));
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
            JeproLabSampleAnalyzeForm analyzeCheckForm = new JeproLabSampleAnalyzeForm(analyze, sample.sample_id);
            analyzeCheckForm.disableForm(true);
            if(sample.analyzes.contains(analyze.analyze_id)){
                analyzeCheckForm.setSelected(true);
                analyzeCheckForm.disableForm(false);
                analyzeCheckForm.getAnalyzeCheck().selectedProperty().addListener(((observable, oldValue, newValue) -> {
                    if(newValue) {
                        sample.addAnalyze(analyzeCheckForm.getAnalyzeId());
                        sample.addResultForm(analyzeCheckForm);
                    }else{
                        sample.removeAnalyze(analyzeCheckForm.getAnalyzeId());
                        sample.removeResultForm(analyzeCheckForm);
                    }
                }));
            }
            jeproLabSampleAnalyzeSelector.add(analyzeCheckForm, 0, analyzeRow);
            GridPane.setMargin(analyzeCheckForm, new Insets(8, 2, 4, 4));
            analyzeRow++;
        }
        if(sample.sample_id <= 0){
            printTicket.setDisable(true);
        }

        updateToolBar();
        addEventListeners();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        saveAndBackToRequestFormBtn = new Button(bundle.getString("JEPROLAB_SAVE_AND_BACK_TO_REQUEST_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        printTicket = new Button(bundle.getString("JEPROLAB_TICKET_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/printer.png"))));
        if (sample.sample_id > 0) {
            saveBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveBtn, saveAndBackToRequestFormBtn, printTicket, cancelBtn);
    }

    private void addEventListeners(){
        saveBtn.setOnAction(event ->  saveSample() );

        saveAndBackToRequestFormBtn.setOnAction(evt -> {
            saveSample();
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
            JeproLab.getInstance().getApplicationForms().addCategoryForm.controller.initializeContent(sample.request_id);
        });

        jeproLabSampleTemperatureUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
            double current = Double.parseDouble(jeproLabSampleTemperature.getText());
            String from = "celsius", to = "kelvin";
            if(oldValue.equals(bundle.getString("JEPROLAB_CELSIUS_LABEL"))){
                from = "celsius";
            }else if(oldValue.equals(bundle.getString("JEPROLAB_KELVIN_LABEL"))){
                from = "kelvin";
            }else if(oldValue.equals(bundle.getString("JEPROLAB_FAHRENHEIT_LABEL"))){
                from = "fahrenheit";
            }

            if(newValue.equals(bundle.getString("JEPROLAB_CELSIUS_LABEL"))){
                to = "celsius";
            }else if(newValue.equals(bundle.getString("JEPROLAB_FAHRENHEIT_LABEL"))){
                to = "fahrenheit";
            }else if(newValue.equals(bundle.getString("JEPROLAB_KELVIN_LABEL"))){
                to = "kelvin";
            }
            jeproLabSampleTemperature.setText(String.valueOf(JeproLabTools.convertTemperature(current, from, to)));
            if(sample != null){
                sample.temperature = jeproLabSampleTemperature.getText();
                sample.temperature_unit = newValue;
            }
        });

        printTicket.setOnAction(event -> {
            //JeproLabDocument.createTicketDocument(sample.sample_id);
        });
    }

    private void loadSample(int sampleId) {

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

    private int getMatrixId(String matrixName){
        for(Map.Entry<Integer, String> matrix : matrices.entrySet()){
            if(matrixName.equals(matrix.getValue())){
                return matrix.getKey();
            }
        }
        return 0;
    }

    private int getConditionId(String conditionName){
        if(conditionName != null) {
            for (Map.Entry<Integer, String> condition : conditions.entrySet()) {
                if (conditionName.equals(condition.getValue())) {
                    return condition.getKey();
                }
            }
        }
        return 0;
    }



    private void saveSample(){
        sample.designation = jeproLabSampleDesignation.getText();
        sample.matrix_id = getMatrixId(jeproLabSampleMatrix.getValue());
        sample.removal_date = JeproLabTools.getDate(jeproLabSampleRemovalDate.getValue());
        sample.received_date = JeproLabTools.getDate(jeproLabSampleReceivedDate.getValue());
        sample.test_date = JeproLabTools.getDate(jeproLabSampleTestDate.getValue());

        sample.condition_id = getConditionId(jeproLabSampleReceiveCondition.getValue());
        sample.temperature = jeproLabSampleTemperature.getText();
        sample.temperature_unit = jeproLabSampleTemperatureUnit.getValue();

        if(sample.sample_id > 0){
            sample.update();
        }else{
            //sample.reference;
            sample.add();
        }
    }

    public static class JeproLabSampleAnalyzeForm extends HBox {
        private static JeproLabContext context = JeproLabContext.getContext();
        private Map<Integer, String> methods;
        private CheckBox analyzeCheck;
        private TextField jeproLabResultUnit, jeproLabAnalyzeResult;
        private Button jeproLabAnalyzePrintTicketBtn, jeproLabAnalyzeUpdateResultBtn;
        private int analyze_id;
        private ComboBox<String> jeproLabMethod;
        private int sample_id;

        public JeproLabSampleAnalyzeForm(JeproLabAnalyzeModel analyze, int sampleId){
            int jeproLabResultUnitWidth = 80;
            analyzeCheck = new CheckBox(analyze.name.get("lang_" + context.language.language_id));
            jeproLabAnalyzePrintTicketBtn = new Button(JeproLab.getBundle().getString("JEPROLAB_PRINT_TICKET_LABEL"));
            jeproLabAnalyzePrintTicketBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/printer.png"))));
            jeproLabAnalyzeUpdateResultBtn = new Button(JeproLab.getBundle().getString("JEPROLAB_UPDATE_LABEL"));
            jeproLabAnalyzeUpdateResultBtn.getStyleClass().addAll("save-btn");
            jeproLabAnalyzeUpdateResultBtn.setDisable(true);


            this.analyze_id = analyze.analyze_id;
            this.sample_id = sampleId;

            GridPane formLayout = new GridPane();
            jeproLabResultUnit = new TextField();
            jeproLabAnalyzeResult = new TextField();
            jeproLabResultUnit.setPrefWidth(jeproLabResultUnitWidth);
            jeproLabResultUnit.setMinWidth(jeproLabResultUnitWidth);
            jeproLabMethod = new ComboBox<>();
            jeproLabMethod.setPrefWidth(100);

            methods = analyze.getMethods();
            if(methods.size() > 0) {
                for (Map.Entry<Integer, String> entry : methods.entrySet()) {
                    jeproLabMethod.getItems().add(entry.getValue());
                }
            }else{
                analyzeCheck.setDisable(true);
            }

            formLayout.add(analyzeCheck, 0, 0);
            formLayout.add(jeproLabResultUnit, 1, 0);
            formLayout.add(jeproLabMethod, 2, 0);
            formLayout.add(jeproLabAnalyzeResult, 3, 0);
            formLayout.add(jeproLabAnalyzePrintTicketBtn, 4, 0);
            formLayout.add(jeproLabAnalyzeUpdateResultBtn, 5, 0);

            GridPane.setMargin(analyzeCheck, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabResultUnit, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabMethod, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabAnalyzeResult, new Insets(0, 5, 0, 5));
            GridPane.setMargin(jeproLabAnalyzePrintTicketBtn, new Insets(0, 5, 0, 15));
            GridPane.setMargin(jeproLabAnalyzeUpdateResultBtn, new Insets(0, 5, 0, 15));


            formLayout.getColumnConstraints().addAll(
                new ColumnConstraints(JeproLabRequestSampleAddController.analyzeContainerWidth),
                new ColumnConstraints(JeproLabRequestSampleAddController.analyzeUnitContainerWidth),
                new ColumnConstraints(JeproLabRequestSampleAddController.analyzeMethodContainerWidth),
                new ColumnConstraints(JeproLabRequestSampleAddController.analyzeResultContainerWidth)
            );
            this.setSpacing(0);
            this.getChildren().add(formLayout);

            addEventListeners();
        }

        public void setSelected(boolean isSelected){
            analyzeCheck.setSelected(isSelected);
        }

        public void disableForm(boolean disable){
            jeproLabResultUnit.setDisable(disable);
            jeproLabAnalyzeResult.setDisable(disable);
            jeproLabMethod.setDisable(disable);
            jeproLabAnalyzePrintTicketBtn.setDisable(disable);
        }

        public String getAnalyzeMethod(){
            return jeproLabMethod.getValue();
        }

        public String getAnalyzeResult(){
            return jeproLabAnalyzeResult.getText();
        }

        public String getAnalyzeUnit(){
            return jeproLabResultUnit.getText();
        }

        public void setAnalyzeMethod(String value){
            jeproLabMethod.setValue(value);
        }

        public void setAnalyzeReult(String result){
            jeproLabAnalyzeResult.setText(result);
        }

        public void setAnalyzeUnit(String unit){
            jeproLabResultUnit.setText(unit);
        }

        public int getAnalyzeId(){
            return analyze_id;
        }

        public int getSampleId(){
            return sample_id;
        }

        public CheckBox getAnalyzeCheck(){
            return analyzeCheck;
        }

        private void addEventListeners(){
            analyzeCheck.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue){
                    disableForm(false);
                }else{
                    disableForm(true);
                    if(!jeproLabAnalyzeUpdateResultBtn.isDisabled()){
                        jeproLabAnalyzeUpdateResultBtn.setDisable(true);
                    }
                }
            }));

            jeproLabResultUnit.textProperty().addListener((observable, oldValue, newValue) ->
                disableUpdateButton(newValue.equals(oldValue))
            );

            jeproLabMethod.valueProperty().addListener((observable, oldValue, newValue) ->
                disableUpdateButton(newValue.equals(oldValue))
            );

            jeproLabAnalyzeResult.textProperty().addListener((observable, oldValue, newValue) ->
                disableUpdateButton(newValue.equals(oldValue))
            );

            jeproLabAnalyzeUpdateResultBtn.setOnAction(event -> {
                jeproLabAnalyzeUpdateResultBtn.setDisable(true);
                JeproLabRequestModel.JeproLabSampleModel.updateResult(this);

            });
        }

        private void disableUpdateButton(boolean value){
            jeproLabAnalyzeUpdateResultBtn.setDisable(value);
        }
    }
}
