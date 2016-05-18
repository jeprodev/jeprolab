package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
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
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabTaxController extends JeproLabController {
    private CheckBox checkAll;
    @FXML
    public TableView<JeproLabTaxRecord> jeproLabTaxTableView;
    public TableColumn<JeproLabTaxRecord, String> jeproLabTaxIndexColumn;
    public TableColumn<JeproLabTaxRecord, Boolean> jeproLabTaxCheckBoxColumn;
    public TableColumn<JeproLabTaxRecord, Button> jeproLabTaxStatusColumn;
    public TableColumn<JeproLabTaxRecord, String> jeproLabTaxNameColumn;
    public TableColumn<JeproLabTaxRecord, String> jeproLabTaxRateColumn;
    public TableColumn<JeproLabTaxRecord, HBox> jeproLabTaxActionColumn;
    public HBox jeproLabTaxSearchWrapper;
    public TextField jeproLabTaxSearch;
    public Button jeproLabTaxSearchBtn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 240;

        jeproLabTaxTableView.setPrefSize(formWidth, 600);
        VBox.setMargin(jeproLabTaxSearchWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabTaxTableView, new Insets(0, 0, 0, 0.01 * JeproLab.APP_WIDTH));

        jeproLabTaxIndexColumn.setText("#");
        jeproLabTaxIndexColumn.setPrefWidth(30);
        jeproLabTaxIndexColumn.setCellValueFactory(new PropertyValueFactory<>("taxIndex"));
        tableCellAlign(jeproLabTaxIndexColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        jeproLabTaxCheckBoxColumn.setPrefWidth(20);
        jeproLabTaxCheckBoxColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabTaxRecord, Boolean>, TableCell<JeproLabTaxRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabTaxCheckBoxColumn.setCellFactory(checkBoxFactory);
        tableCellAlign(jeproLabTaxCheckBoxColumn, Pos.CENTER);

        jeproLabTaxStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabTaxStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabTaxRecord, Button>, TableCell<JeproLabTaxRecord, Button>> statusCellFactory = param -> new JeproLabStatusCell();
        jeproLabTaxStatusColumn.setCellFactory(statusCellFactory);
        tableCellAlign(jeproLabTaxStatusColumn, Pos.CENTER);

        jeproLabTaxNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabTaxNameColumn.setPrefWidth(remainingWidth);
        jeproLabTaxNameColumn.setCellValueFactory(new PropertyValueFactory<>("taxName"));
        tableCellAlign(jeproLabTaxNameColumn, Pos.CENTER_LEFT);

        jeproLabTaxRateColumn.setText(bundle.getString("JEPROLAB_RATE_LABEL"));
        jeproLabTaxRateColumn.setPrefWidth(80);
        jeproLabTaxRateColumn.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
        tableCellAlign(jeproLabTaxRateColumn, Pos.CENTER);

        jeproLabTaxActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabTaxActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabTaxRecord, HBox>, TableCell<JeproLabTaxRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabTaxActionColumn.setCellFactory(actionFactory);

        jeproLabTaxSearch.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));
    }

    @Override
    public void initializeContent(){

    }

    public static class JeproLabTaxRecord{

    }


    protected static class JeproLabStatusCell extends TableCell<JeproLabTaxRecord, Button>{
        private Button statusButton;

        public JeproLabStatusCell(){

        }
    }


    private static class JeproLabCheckBoxCell extends TableCell<JeproLabTaxRecord, Boolean>{

    }


    private static class JeproLabActionCell extends TableCell<JeproLabTaxRecord, HBox>{

    }
}