package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabCountryModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabCountryStatesController extends JeproLabController {
    private CheckBox checkAll;
    @FXML
    public TableView<JeproLabStateRecord> jeproLabStateTableView;
    public TableColumn<JeproLabStateRecord, String> jeproLabStateIndexColumn;
    public TableColumn<JeproLabStateRecord, Boolean> jeproLabStateCheckBoxColumn;
    public TableColumn<JeproLabStateRecord, Boolean> jeproLabStateStatusColumn;
    public TableColumn<JeproLabStateRecord, String> jeproLabStateCountryNameColumn;
    public TableColumn<JeproLabStateRecord, String> jeproLabStateZoneNameColumn;
    public TableColumn<JeproLabStateRecord, String> jeproLabStateNameColumn;
    public TableColumn<JeproLabStateRecord, String> jeproLabStateIsoCodeColumn;
    public TableColumn<JeproLabStateRecord, String> jeproLabStateTaxBehaviorColumn;
    public TableColumn<JeproLabStateRecord, HBox> jeproLabStateActionsColumn;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 218;

        VBox.setMargin(jeproLabStateTableView, new Insets(0, 0, 0, (0.01 * JeproLab.APP_WIDTH)));
        jeproLabStateTableView.setPrefSize(formWidth, 600);

        jeproLabStateIndexColumn.setPrefWidth(30);
        jeproLabStateIndexColumn.setText("#");
        tableCellAlign(jeproLabStateIndexColumn, Pos.CENTER_RIGHT);
        jeproLabStateIndexColumn.setCellValueFactory(new PropertyValueFactory<>("stateId"));
        checkAll = new CheckBox();
        jeproLabStateCheckBoxColumn.setGraphic(checkAll);
        jeproLabStateCheckBoxColumn.setPrefWidth(22);
        Callback<TableColumn<JeproLabStateRecord, Boolean>, TableCell<JeproLabStateRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabStateCheckBoxColumn.setCellFactory(checkBoxFactory);
        jeproLabStateStatusColumn.setPrefWidth(45);
        jeproLabStateStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        Callback<TableColumn<JeproLabStateRecord, Boolean>, TableCell<JeproLabStateRecord, Boolean>> statusFactory = param -> new JeproLabStatusCell();
        jeproLabStateStatusColumn.setCellFactory(statusFactory);
        jeproLabStateNameColumn.setPrefWidth(0.4 * remainingWidth);
        jeproLabStateNameColumn.setText(bundle.getString("JEPROLAB_STATE_NAME_LABEL"));
        tableCellAlign(jeproLabStateNameColumn, Pos.CENTER_LEFT);
        jeproLabStateNameColumn.setCellValueFactory(new PropertyValueFactory<>("stateStateNameName"));
        jeproLabStateIsoCodeColumn.setPrefWidth(60);
        jeproLabStateIsoCodeColumn.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        tableCellAlign(jeproLabStateIsoCodeColumn, Pos.CENTER);
        jeproLabStateIsoCodeColumn.setCellValueFactory(new PropertyValueFactory<>("stateIsoCodeName"));
        jeproLabStateCountryNameColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabStateCountryNameColumn.setText(bundle.getString("JEPROLAB_COUNTRY_NAME_LABEL"));
        tableCellAlign(jeproLabStateCountryNameColumn, Pos.BASELINE_LEFT);
        jeproLabStateCountryNameColumn.setCellValueFactory(new PropertyValueFactory<>("stateCountryName"));
        tableCellAlign(jeproLabStateZoneNameColumn, Pos.BASELINE_LEFT);
        jeproLabStateZoneNameColumn.setText(bundle.getString("JEPROLAB_ZONE_NAME_LABEL"));
        jeproLabStateZoneNameColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabStateZoneNameColumn.setCellValueFactory(new PropertyValueFactory<>("stateZoneName"));
        jeproLabStateTaxBehaviorColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabStateTaxBehaviorColumn.setText(bundle.getString("JEPROLAB_TAX_BEHAVIOR_LABEL"));
        tableCellAlign(jeproLabStateTaxBehaviorColumn, Pos.CENTER);
        jeproLabStateTaxBehaviorColumn.setCellValueFactory(new PropertyValueFactory<>("stateTaxBehavior"));
        jeproLabStateActionsColumn.setPrefWidth(60);
        jeproLabStateActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        Callback<TableColumn<JeproLabStateRecord, HBox>, TableCell<JeproLabStateRecord, HBox>> actionsFactory = param -> new JeproLabActionCell();
        jeproLabStateActionsColumn.setCellFactory(actionsFactory);
    }

    @Override
    public void initializeContent(){

    }

    @Override
    public void updateToolBar(){

    }

    public class JeproLabStateRecord{
        private SimpleIntegerProperty stateId;
        private SimpleStringProperty stateName, stateCountryName, stateZoneName, stateIsoCode;
        private SimpleBooleanProperty statePublished;

        public JeproLabStateRecord(JeproLabCountryModel.JeproLabStateModel state){

        }
    }

    private class JeproLabCheckBoxCell extends TableCell<JeproLabStateRecord, Boolean>{

    }

    private class JeproLabStatusCell extends TableCell<JeproLabStateRecord, Boolean>{
        private CheckBox zoneCheckBox;

        public JeproLabStatusCell(){
            zoneCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean it){
            super.commitEdit(it);
        }

        @Override
        public void updateItem(Boolean item, boolean it){
            super.updateItem(item, it);
            final ObservableList items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(zoneCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }
}