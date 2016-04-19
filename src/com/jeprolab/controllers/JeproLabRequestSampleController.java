package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 19/04/14.
 */
public class JeproLabRequestSampleController extends JeproLabController {
    @FXML
    public JeproFormPanel jeproLabSamplesFormWrapper;
    public TableView<JeproLabSampleItemRecord> jeproLabSamplesTableView;
    public TableColumn<JeproLabSampleItemRecord, Integer> jeproLabSampleIndexTableColumn;
    public TableColumn<JeproLabSampleItemRecord, CheckBox> jeproLabSampleCheckBoxTableColumn;
    public TableColumn<JeproLabSampleItemRecord, String> jeproLabSampleDesignationTableColumn;
    public TableColumn<JeproLabSampleItemRecord, String> jeproLabSampleRemovalDateTableColumn;
    //public TableColumn<JeproLabSampleItemRecord, String> jeproLabSampleTableColumn;
    public TableColumn<JeproLabSampleItemRecord, Integer> jeproLabSampleMatrixTableColumn;
    public TableColumn<JeproLabSampleItemRecord, HBox> jeproLabSampleActionTableColumn;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 110;

        jeproLabSamplesTableView.setPrefSize(formWidth, JeproLab.APP_HEIGHT - 150);
        jeproLabSamplesTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabSamplesTableView.setLayoutY(10);

        jeproLabSampleIndexTableColumn.setPrefWidth(30);
        jeproLabSampleIndexTableColumn.setText("#");

        CheckBox checkAll = new CheckBox();
        jeproLabSampleCheckBoxTableColumn.setPrefWidth(20);
        jeproLabSampleCheckBoxTableColumn.setGraphic(checkAll);

        jeproLabSampleDesignationTableColumn.setPrefWidth(0.6 * remainingWidth);
        jeproLabSampleDesignationTableColumn.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleRemovalDateTableColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabSampleRemovalDateTableColumn.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        //jeproLabSampleTableColumn;
        jeproLabSampleActionTableColumn.setPrefWidth(70);
        jeproLabSampleActionTableColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
    }

    public void initializeContent(){

    }

    private static class JeproLabSampleItemRecord{

    }
}