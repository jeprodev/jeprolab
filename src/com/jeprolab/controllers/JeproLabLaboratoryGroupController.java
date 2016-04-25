package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabLaboratoryGroupController  extends JeproLabController {
    @FXML
    public TableView jeproLabLaboratoryGroupTableView;
    public TableColumn jeproLabLaboratoryGroupIndexColumn, jeproLabLaboratoryGroupCheckBoxColumn, jeproLabLaboratoryGroupNameColumn;
    public TableColumn jeproLabLaboratoryGroupShareCustomerColumn, jeproLabLaboratoryGroupShareRequestColumn, jeproLabLaboratoryGroupShareResultsColumn;
    public TableColumn jeproLabLaboratoryGroupShareStockColumn, jeproLabLaboratoryGroupActionColumn;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double remainingWidth = 0.98 * JeproLab.APP_WIDTH - 55;
        jeproLabLaboratoryGroupTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, 600);
        jeproLabLaboratoryGroupTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabLaboratoryGroupTableView.setLayoutY(10);

        jeproLabLaboratoryGroupIndexColumn.setText("#");
        jeproLabLaboratoryGroupIndexColumn.setPrefWidth(30);
        jeproLabLaboratoryGroupCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabLaboratoryGroupCheckBoxColumn.setPrefWidth(25);
        jeproLabLaboratoryGroupNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryGroupNameColumn.setPrefWidth(0.40 * remainingWidth);
        jeproLabLaboratoryGroupShareCustomerColumn.setText(bundle.getString("JEPROLAB_SHARE_CUSTOMER_LABEL"));
        jeproLabLaboratoryGroupShareCustomerColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabLaboratoryGroupShareRequestColumn.setText(bundle.getString("JEPROLAB_SHARE_REQUEST_LABEL"));
        jeproLabLaboratoryGroupShareRequestColumn.setPrefWidth(0.10 * remainingWidth);
        jeproLabLaboratoryGroupShareResultsColumn.setText(bundle.getString("JEPROLAB_SHARE_RESULTS_LABEL"));
        jeproLabLaboratoryGroupShareResultsColumn.setPrefWidth(0.10 * remainingWidth);
        jeproLabLaboratoryGroupShareStockColumn.setText(bundle.getString("JEPROLAB_SHARE_STOCK_LABEL"));
        jeproLabLaboratoryGroupShareStockColumn.setPrefWidth(0.10 * remainingWidth);
        jeproLabLaboratoryGroupActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabLaboratoryGroupActionColumn.setPrefWidth(0.15 * remainingWidth);
    }

}
