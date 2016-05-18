package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabTaxGroupsController extends JeproLabController {
    private CheckBox checkAll;

    @FXML
    public JeproFormPanel jeproLabFormPanelWrapper;
    public TextField jeproLabTaxRulesGroupSearch;
    public HBox jeproLabTaxRulesGroupSearchWrapper;
    public Button jeproLabTaxRulesGroupSearchBtn;
    public TableView<JeproLabTaxRulesGroupRecord> jeproLabTaxRulesGroupTableView;
    public TableColumn<JeproLabTaxRulesGroupRecord, String> jeproLabTaxRulesGroupIndexColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, Boolean> jeproLabTaxRulesGroupCheckBoxColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, String> jeproLabTaxRulesGroupNameColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, Button> jeproLabTaxRulesGroupStatusColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, HBox> jeproLabTaxRulesGroupActionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 170;

        VBox.setMargin(jeproLabTaxRulesGroupSearchWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabTaxRulesGroupTableView, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));

        jeproLabTaxRulesGroupTableView.setPrefWidth(formWidth);

        jeproLabTaxRulesGroupIndexColumn.setPrefWidth(30);
        jeproLabTaxRulesGroupIndexColumn.setText("#");
        jeproLabTaxRulesGroupIndexColumn.setCellValueFactory(new PropertyValueFactory<>("taxRulesGroupIndex"));
        tableCellAlign(jeproLabTaxRulesGroupIndexColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        jeproLabTaxRulesGroupCheckBoxColumn.setGraphic(checkAll);
        jeproLabTaxRulesGroupCheckBoxColumn.setPrefWidth(20);
        //jeproLabTaxRulesGroupCheckBoxColumn;
        Callback<TableColumn<JeproLabTaxRulesGroupRecord, Boolean>, TableCell<JeproLabTaxRulesGroupRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabTaxRulesGroupCheckBoxColumn.setCellFactory(checkBoxFactory);

        jeproLabTaxRulesGroupNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabTaxRulesGroupNameColumn.setPrefWidth(remainingWidth);
        jeproLabTaxRulesGroupNameColumn.setCellValueFactory(new PropertyValueFactory<>("taxRulesGroupName"));

        jeproLabTaxRulesGroupStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabTaxRulesGroupStatusColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabTaxRulesGroupRecord, Button>, TableCell<JeproLabTaxRulesGroupRecord, Button>> statusFactory = param -> new JeproLabStatusCell();
        jeproLabTaxRulesGroupStatusColumn.setCellFactory(statusFactory);

        jeproLabTaxRulesGroupActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabTaxRulesGroupActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabTaxRulesGroupRecord, HBox>, TableCell<JeproLabTaxRulesGroupRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabTaxRulesGroupActionColumn.setCellFactory(actionFactory);
        /*
        fbx18596903
                kaingoof
                */
    }

    @Override
    public void initializeContent(){

    }

    public static class JeproLabTaxRulesGroupRecord{

    }

    private static class JeproLabCheckBoxCell extends TableCell<JeproLabTaxRulesGroupRecord, Boolean>{

    }

    private static class JeproLabStatusCell extends TableCell<JeproLabTaxRulesGroupRecord, Button>{

    }

    public static class JeproLabActionCell extends TableCell<JeproLabTaxRulesGroupRecord, HBox>{

    }
}
