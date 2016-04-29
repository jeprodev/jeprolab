package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabRequestSampleController extends JeproLabController {
    @FXML
    public VBox jeproLabSamplesFormWrapper;
    public TableView<JeproLabSampleItemRecord> jeproLabSamplesTableView;
    public TableColumn<JeproLabSampleItemRecord, Integer> jeproLabSampleIndexTableColumn;
    public TableColumn<JeproLabSampleItemRecord, Boolean> jeproLabSampleCheckBoxTableColumn;
    public TableColumn<JeproLabSampleItemRecord, String> jeproLabSampleDesignationTableColumn;
    public TableColumn<JeproLabSampleItemRecord, String> jeproLabSampleRemovalDateTableColumn;
    public TableColumn<JeproLabSampleItemRecord, String> jeproLabSampleMatrixTableColumn;
    public TableColumn<JeproLabSampleItemRecord, HBox> jeproLabSampleActionTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 110;

        jeproLabSamplesTableView.setPrefSize(formWidth, 600);
        VBox.setMargin(jeproLabSamplesTableView, new Insets(5, 0, 0, (0.01 * JeproLab.APP_WIDTH)));
        jeproLabSamplesTableView.setLayoutY(10);

        jeproLabSampleIndexTableColumn.setPrefWidth(30);
        jeproLabSampleIndexTableColumn.setText("#");
        jeproLabSampleIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("sampleIndex"));
        tableCellAlign(jeproLabSampleIndexTableColumn, Pos.CENTER_RIGHT);

        CheckBox checkAll = new CheckBox();
        jeproLabSampleCheckBoxTableColumn.setPrefWidth(20);
        jeproLabSampleCheckBoxTableColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabSampleItemRecord, Boolean>, TableCell<JeproLabSampleItemRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabSampleCheckBoxTableColumn.setCellFactory(checkBoxFactory);

        jeproLabSampleDesignationTableColumn.setPrefWidth(0.6 * remainingWidth);
        jeproLabSampleDesignationTableColumn.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleDesignationTableColumn.setCellValueFactory(new PropertyValueFactory<>("sampleDesignation"));

        jeproLabSampleRemovalDateTableColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabSampleRemovalDateTableColumn.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleRemovalDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("sampleRemovalDate"));
        tableCellAlign(jeproLabSampleRemovalDateTableColumn, Pos.CENTER);

        jeproLabSampleMatrixTableColumn.setPrefWidth(0.1 * remainingWidth);
        jeproLabSampleMatrixTableColumn.setText(bundle.getString("JEPROLAB_MATRIX_LABEL"));
        jeproLabSampleMatrixTableColumn.setCellValueFactory(new PropertyValueFactory<>("sampleMatrix"));
        tableCellAlign(jeproLabSampleMatrixTableColumn, Pos.CENTER);

        jeproLabSampleActionTableColumn.setPrefWidth(70);
        jeproLabSampleActionTableColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        Callback<TableColumn<JeproLabSampleItemRecord, HBox>, TableCell<JeproLabSampleItemRecord, HBox>> actionCellFactory = param -> new JeproLabActionCell();
        jeproLabSampleActionTableColumn.setCellFactory(actionCellFactory);
    }

    @Override
    public void initializeContent(){
        List<JeproLabRequestModel.JeproLabSampleModel> samples = JeproLabRequestModel.JeproLabSampleModel.getSamples();
        ObservableList<JeproLabSampleItemRecord> analyzeList = FXCollections.observableArrayList();
        if(!samples.isEmpty()){
            analyzeList.addAll(samples.stream().map(JeproLabSampleItemRecord::new).collect(Collectors.toList()));
            jeproLabSamplesTableView.setItems(analyzeList);
        }
        updateToolBar();
    }

    public static class JeproLabSampleItemRecord{
        private SimpleStringProperty sampleDesignation, sampleRemovalDate, sampleMatrix;
        private SimpleIntegerProperty sampleIndex;

        public JeproLabSampleItemRecord(JeproLabRequestModel.JeproLabSampleModel sample){
            sampleDesignation = new SimpleStringProperty(sample.designation);
            sampleRemovalDate = new SimpleStringProperty(JeproLabTools.date("dd-MM-yyyy", sample.removal_date));
            sampleMatrix = new SimpleStringProperty(JeproLabRequestModel.JeproLabMatrixModel.getMatrixNameByMatrixId(sample.matrix_id));
            sampleIndex = new SimpleIntegerProperty(sample.sample_id);
        }

        public int getSampleIndex(){
            return sampleIndex.get();
        }

        public String getSampleDesignation(){
            return sampleDesignation.get();
        }

        public String getSampleRemovalDate(){
            return sampleRemovalDate.get();
        }

        public String getSampleMatrix(){
            return sampleMatrix.get();
        }
    }

    public static class JeproLabCheckBoxCell extends TableCell<JeproLabSampleItemRecord, Boolean>{
        private CheckBox sampleCheckBox;

        public JeproLabCheckBoxCell(){
            sampleCheckBox = new CheckBox();
        }

        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean t, boolean empty){
            super.updateItem(t, empty);
            ObservableList<JeproLabSampleItemRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                int itemId = items.get(getIndex()).getSampleIndex();
                setGraphic(sampleCheckBox);
            }
        }
    }

    public static class JeproLabActionCell extends TableCell<JeproLabSampleItemRecord, HBox>{
        private HBox commandContainer;
        private Button editSample, deleteSample;
        private double btnSize = 18;

        public JeproLabActionCell(){
            commandContainer = new HBox(4);
            editSample = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editSample.setMinSize(btnSize, btnSize);
            editSample.setMaxSize(btnSize, btnSize);
            editSample.setPrefSize(btnSize, btnSize);
            editSample.getStyleClass().add("icon-btn");
            deleteSample = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteSample.setMinSize(btnSize, btnSize);
            deleteSample.setMaxSize(btnSize, btnSize);
            deleteSample.setPrefSize(btnSize, btnSize);
            deleteSample.getStyleClass().add("icon-btn");

            commandContainer.setAlignment(Pos.CENTER);
            commandContainer.getChildren().addAll(editSample, deleteSample);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox i, boolean empty){
            super.updateItem(i, empty);
            ObservableList<JeproLabSampleItemRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                int itemId = items.get(getIndex()).getSampleIndex();
                editSample.setOnAction(event -> {
                    try {
                        JeproLab.request.setRequest("sample_id=" + itemId);
                        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().sampleForm);
                        JeproLabContext.getContext().controller.initializeContent();
                    }catch (IOException ignored){
                        ignored.printStackTrace();
                    }
                });
                setGraphic(commandContainer);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
