package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabRequestController extends JeproLabController{
    @FXML
    public JeproFormPanel jeproLabFormPanelWrapper;
    public TableView<JeproLabRequestRecord> requestTableView;
    public TableColumn<JeproLabRequestRecord, String> requestIndexTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCheckBoxTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestOrderIdTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCustomerTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestTotalTableColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCarrierColumn;
    public TableColumn<JeproLabRequestRecord, String> requestCreationDateColumn;
    public TableColumn<JeproLabRequestRecord, String> requestOnlineColumn;
    public TableColumn<JeproLabRequestRecord, String> requestActionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 182;

        jeproLabFormPanelWrapper.setPrefWidth(formWidth);
        jeproLabFormPanelWrapper.setLayoutY(10);
        jeproLabFormPanelWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);

        requestTableView.setPrefWidth(formWidth);

        requestIndexTableColumn.setPrefWidth(30);
        requestIndexTableColumn.setText("#");

        CheckBox checkAll = new CheckBox();
        requestCheckBoxTableColumn.setPrefWidth(22);
        requestCheckBoxTableColumn.setGraphic(checkAll);

        requestOrderIdTableColumn.setText(bundle.getString("JEPROLAB_REQUEST_REFERENCE_LABEL"));
        requestOrderIdTableColumn.setPrefWidth(0.25 * remainingWidth);
        requestCustomerTableColumn.setText(bundle.getString("JEPROLAB_CUSTOMER_NAME_LABEL"));
        requestCustomerTableColumn.setPrefWidth(0.35 * remainingWidth);
        requestTotalTableColumn.setText(bundle.getString("JEPROLAB_TOTAL_LABEL"));
        requestTotalTableColumn.setPrefWidth(0.1 * remainingWidth);
        requestCarrierColumn.setText(bundle.getString("JEPROLAB_CARRIER_LABEL"));
        requestCarrierColumn.setPrefWidth(0.15 * remainingWidth);
        requestCreationDateColumn.setText(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));
        requestCreationDateColumn.setPrefWidth(0.15 * remainingWidth);
        requestOnlineColumn.setText(bundle.getString("JEPROLAB_ONLINE_LABEL"));
        requestOnlineColumn.setPrefWidth(65);
        requestActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        requestActionColumn.setPrefWidth(65);
    }

    @Override
    public void initializeContent(){}

    @Override
    public void updateToolBar(){}

    public static class JeproLabRequestRecord{

    }
}