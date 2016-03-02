package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/03/2014.
 */
public class JeproLabCountryZoneController extends JeproLabController{
    private CheckBox checkAll;
    public JeproFormPanel jeproLabAddZonePanelWrapper;
    public TableView jeproLabZoneTableView;
    public TableColumn jeproLabZoneIndexColumn, jeproLabZoneCheckBoxColumn, jeproLabZoneNameColumn, jeproLabZoneAllowDeliveryColumn, jeproLabZoneActionsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        checkAll = new CheckBox();
        double remainingWidth = JeproLab.APP_WIDTH - 55;
        jeproLabZoneTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, 600);
        jeproLabZoneTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabZoneTableView.setLayoutY(10);

        jeproLabZoneIndexColumn.setText("#");
        jeproLabZoneIndexColumn.setPrefWidth(30);
        jeproLabZoneCheckBoxColumn.setGraphic(checkAll);
        jeproLabZoneCheckBoxColumn.setPrefWidth(25);
        jeproLabZoneNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabZoneNameColumn.setPrefWidth(0.6 * remainingWidth);
        jeproLabZoneAllowDeliveryColumn.setText(bundle.getString("JEPROLAB_ALLOW_DELIVERY_LABEL"));
        jeproLabZoneAllowDeliveryColumn.setPrefWidth(0.25 * remainingWidth);
        jeproLabZoneActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabZoneActionsColumn.setPrefWidth(0.15 * remainingWidth);

    }
}