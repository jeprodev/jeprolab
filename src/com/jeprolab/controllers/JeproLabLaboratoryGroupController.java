package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 08/01/16.
 */
public class JeproLabLaboratoryGroupController extends JeproLabController {
    @FXML
    public TableColumn jeproLabLaboratoryGroupIndexColumn, jeproLabLaboratoryGroupCheckBoxColumn, jeproLabLaboratoryGroupNameColumn;
    public TableColumn jeproLabLaboratoryGroupShareCustomerColumn, jeproLabLaboratoryGroupShareRequestColumn, jeproLabLaboratoryGroupShareResultsColumn;
    public TableColumn jeproLabLaboratoryGroupColumn, jeproLabLaboratoryGroupActionColumn;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        jeproLabLaboratoryGroupIndexColumn.setText("#");
        jeproLabLaboratoryGroupCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabLaboratoryGroupNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryGroupShareCustomerColumn.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabLaboratoryGroupShareRequestColumn.setText(bundle.getString("JEPROLAB_SHARE_REQUEST_LABEL"));
        jeproLabLaboratoryGroupShareResultsColumn.setText(bundle.getString("JEPROLAB_SHARE_RESULTS_LABEL"));
        jeproLabLaboratoryGroupColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabLaboratoryGroupActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
    }
}