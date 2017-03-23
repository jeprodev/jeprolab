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
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryStatesController extends JeproLabController{
    private CheckBox checkAll;
    @FXML



    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;


/*


        jeproLabStateIndexColumn.setText("#");



        jeproLabStateStatusColumn.setText();


        jeproLabStateNameColumn.setText();


        jeproLabStateIsoCodeColumn.setText();

        jeproLabStateCountryNameColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabStateCountryNameColumn.setText(bundle.getString("JEPROLAB_COUNTRY_NAME_LABEL"));
        tableCellAlign(jeproLabStateCountryNameColumn, Pos.BASELINE_LEFT);
        jeproLabStateCountryNameColumn.setCellValueFactory(new PropertyValueFactory<>("stateCountryName"));
        tableCellAlign(jeproLabStateZoneNameColumn, Pos.BASELINE_LEFT);
        jeproLabStateZoneNameColumn.setText(bundle.getString("JEPROLAB_ZONE_NAME_LABEL"));
        jeproLabStateZoneNameColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabStateZoneNameColumn.setCellValueFactory(new PropertyValueFactory<>("stateZoneName"));

        jeproLabStateTaxBehaviorColumn.setText();


        jeproLabStateActionsColumn.setText(); */

    }

    @Override
    public void initializeContent(){

    }

    @Override
    public void updateToolBar(){

    }


}
