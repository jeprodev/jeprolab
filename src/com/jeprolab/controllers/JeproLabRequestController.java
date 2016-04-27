package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabRequestController extends JeproLabController {
    @FXML
    public VBox jeproLabFormPanelWrapper;
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
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 182;

        /*jeproLabFormPanelWrapper.setPrefWidth(formWidth);
        jeproLabFormPanelWrapper.setLayoutY(10);
        jeproLabFormPanelWrapper.setLayoutX;*/

        requestTableView.setPrefWidth(formWidth);
        VBox.setMargin(requestTableView, new Insets(5, 0, 0, (0.01 * JeproLab.APP_WIDTH)));

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
