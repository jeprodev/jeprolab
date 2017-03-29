package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabRequestStatusController extends JeproLabController{
    private TableView<JeproLabRequestStatuesRecord> jeproLabRequestStatuesTableView;
    @FXML
    public HBox jeproLabRequestStatuesWrapper, jeproLabRequestStatusCommandWrapper;
    public VBox jeproLabRequestStatuesListWrapper;
    public JeproFormPanel jeproLabRequestAddStatusFormWrapper;
    public JeproFormPanelTitle jeproLabRequestStatusAddFormTitleWrapper;
    public JeproFormPanelContainer jeproLabRequestStatusAddFormTitleContainer;
    public GridPane jeproLabRequestStatusAddFormLayout;
    public Label jeproLabRequestStatusNameLabel, jeproLabRequestStatusInvoiceLabel, jeproLabRequestStatusSendEmailLabel;
    public Label jeproLabRequestStatusUnRemovableLabel, jeproLabRequestStatusDeletedLabel;
    public JeproMultiLangTextField jeproLabRequestStatusName;
    public JeproSwitchButton jeproLabRequestStatusInvoice, jeproLabRequestStatusSendEmail, jeproLabRequestStatusUnRemovable;
    public JeproSwitchButton jeproLabRequestStatusDeleted;
    public Button jeproLabRequestStatusSaveButton, jeproLabRequestStatusCancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 0.98 * JeproLab.APP_WIDTH;
        jeproLabRequestStatuesWrapper.setPrefWidth(formWidth);

        jeproLabRequestStatuesTableView = new TableView<>();

        TableColumn<JeproLabRequestStatuesRecord, String> jeproLabRequestStatuesIndexTableColumn = new TableColumn<>("#");
        jeproLabRequestStatuesIndexTableColumn.setPrefWidth(30);
        TableColumn<JeproLabRequestStatuesRecord, Boolean> jeproLabRequestStatuesCheckBoxTableColumn = new TableColumn<>();
        jeproLabRequestStatuesCheckBoxTableColumn.setPrefWidth(25);

        TableColumn<JeproLabRequestStatuesRecord, String> jeproLabRequestStatuesNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        TableColumn<JeproLabRequestStatuesRecord, HBox> jeproLabRequestStatuesActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabRequestStatuesActionTableColumn.setPrefWidth(60);
        //jeproLabRequestStatuesActionTableColumn.setCellFactory();

        jeproLabRequestStatuesTableView.getColumns().addAll(
            jeproLabRequestStatuesIndexTableColumn, jeproLabRequestStatuesCheckBoxTableColumn,
            jeproLabRequestStatuesNameTableColumn, jeproLabRequestStatuesActionTableColumn
        );

        jeproLabRequestStatusCommandWrapper.setPrefWidth(formWidth);
    }

    private static class JeproLabRequestStatuesRecord {

    }
}
