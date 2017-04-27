package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabRequestAddStatusController extends JeproLabController{
    private Button jeproLabRequestStatusSaveButton, jeproLabRequestStatusCancelButton;
    @FXML
    public JeproFormPanel jeproLabRequestAddStatusFormWrapper;
    public JeproFormPanelTitle jeproLabRequestStatusAddFormTitleWrapper;
    public JeproFormPanelContainer jeproLabRequestStatusAddFormTitleContainer;
    public GridPane jeproLabRequestStatusAddFormLayout;
    public Label jeproLabRequestStatusNameLabel, jeproLabRequestStatusInvoiceLabel, jeproLabRequestStatusSendEmailLabel;
    public Label jeproLabRequestStatusUnRemovableLabel, jeproLabRequestStatusDeletedLabel;
    public JeproMultiLangTextField jeproLabRequestStatusName;
    public JeproSwitchButton jeproLabRequestStatusInvoice, jeproLabRequestStatusSendEmail, jeproLabRequestStatusUnRemovable;
    public JeproSwitchButton jeproLabRequestStatusDeleted;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double formContainerWidth = 510;

        jeproLabRequestAddStatusFormWrapper.setPrefWidth(formContainerWidth);
        jeproLabRequestAddStatusFormWrapper.setLayoutX((JeproLab.APP_WIDTH - formContainerWidth)/2);
        jeproLabRequestAddStatusFormWrapper.setLayoutY(60);
        jeproLabRequestStatusAddFormTitleWrapper.setPrefSize(formContainerWidth, 40);
        jeproLabRequestStatusAddFormTitleContainer.setPrefWidth(formContainerWidth);
        jeproLabRequestStatusAddFormTitleContainer.setLayoutY(40);
        jeproLabRequestStatusAddFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(200), new ColumnConstraints(280)
        );

        Insets labelInset = new Insets(10, 0, 10, 20);
        Insets inputInset = new Insets(10, 0, 10, 20);

        jeproLabRequestStatusNameLabel.setText(bundle.getString("JEPROLAB_STATUS_NAME_LABEL"));
        jeproLabRequestStatusInvoiceLabel.setText(bundle.getString("JEPROLAB_SEND_INVOICE_LABEL"));
        jeproLabRequestStatusSendEmailLabel.setText(bundle.getString("JEPROLAB_SEND_EMAIL_LABEL"));
        jeproLabRequestStatusUnRemovableLabel.setText(bundle.getString("JEPROLAB_UN_REMOVABLE_LABEL"));
        jeproLabRequestStatusDeletedLabel.setText(bundle.getString("JEPROLAB_DELETED_LABEL"));

        GridPane.setMargin(jeproLabRequestStatusNameLabel, labelInset);
        GridPane.setMargin(jeproLabRequestStatusName, inputInset);
        GridPane.setMargin(jeproLabRequestStatusInvoiceLabel, labelInset);
        GridPane.setMargin(jeproLabRequestStatusInvoice, inputInset);
        GridPane.setMargin(jeproLabRequestStatusSendEmailLabel, labelInset);
        GridPane.setMargin(jeproLabRequestStatusSendEmail, inputInset);
        GridPane.setMargin(jeproLabRequestStatusUnRemovableLabel, labelInset);
        GridPane.setMargin(jeproLabRequestStatusUnRemovable, inputInset);
        GridPane.setMargin(jeproLabRequestStatusDeletedLabel, labelInset);
        GridPane.setMargin(jeproLabRequestStatusDeleted, inputInset);
    }


    @Override
    public void initializeContent(int requestStatusId){

        Worker<Boolean> worker = new Task<Boolean>() {
            JeproLabRequestModel.JeproLabRequestStatusModel status;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()) {
                    return false;
                }
                status = new JeproLabRequestModel.JeproLabRequestStatusModel(requestStatusId);

                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateRequestStatusForm(status);
            }
        };
        JeproLab.getInstance().executor.submit((Task)worker);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
    }

    private  void updateRequestStatusForm(JeproLabRequestModel.JeproLabRequestStatusModel status){
        if(status.request_status_id > 0){
            Platform.runLater(() -> {
                jeproLabRequestStatusName.setText(status.name);
                jeproLabRequestStatusInvoice.setSelected(status.send_invoice);
                jeproLabRequestStatusSendEmail.setSelected(status.send_email);
                jeproLabRequestStatusUnRemovable.setSelected(status.un_removable);
                jeproLabRequestStatusDeleted.setSelected(status.deleted);
            });
        }else{
            Platform.runLater(() -> {
                jeproLabRequestStatusName.clearFields();
                jeproLabRequestStatusInvoice.setSelected(false);
                jeproLabRequestStatusSendEmail.setSelected(true);
                jeproLabRequestStatusUnRemovable.setSelected(true);
                jeproLabRequestStatusDeleted.setSelected(false);
            });
        }
    }
}
