package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabLaboratoryModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
public class JeproLabLaboratoryGroupAddController extends JeproLabController{
    private Button saveLaboatoryGroupButton, cancelLaboratoryGroupButton;
    private JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel laboratoryGroup;
    @FXML
    public JeproFormPanel jeproLabAddLaboratoryGroupPanelWrapper;
    public JeproFormPanelTitle jeproLabAddLaboratoryGroupPanelTitleWrapper;
    public JeproFormPanelContainer jeproLabAddLaboratoryGroupPanelContainerWrapper;
    public GridPane jeproLabAddLaboratoryGroupLayout;
    public JeproSwitchButton jeproLabShareCustomer, jeproLabShareRequest, jeproLabShareResults, jeproLabPublished;
    public JeproSwitchButton jeproLabDeleted, jeproLabShareStocks;

    public Label jeproLabLaboratoryGroupNameLabel, jeproLabShareCustomerLabel, jeproLabShareRequestsLabel;
    public Label jeproLabShareResultsLabel, jeproLabPublishedLabel, jeproLabShareStocksLabel, jeproLabDeletedLabel;
    public TextField jeproLabLaboratoryGroupName;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        formWidth = (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabAddLaboratoryGroupLayout.getColumnConstraints().addAll(
            new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25)
        );

        jeproLabAddLaboratoryGroupPanelWrapper.setLayoutX(posX);
        jeproLabAddLaboratoryGroupPanelWrapper.setLayoutY(posY);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_LABORATORY_GROUP_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        jeproLabAddLaboratoryGroupPanelTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabAddLaboratoryGroupPanelTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabAddLaboratoryGroupPanelContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddLaboratoryGroupPanelContainerWrapper.setLayoutY(40);

        jeproLabLaboratoryGroupNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryGroupNameLabel.getStyleClass().add("input-label");
        jeproLabShareCustomerLabel.setText(bundle.getString("JEPROLAB_SHARE_CUSTOMER_LABEL"));
        jeproLabShareCustomerLabel.getStyleClass().add("input-label");
        jeproLabShareRequestsLabel.setText(bundle.getString("JEPROLAB_SHARE_REQUEST_LABEL"));
        jeproLabShareRequestsLabel.getStyleClass().add("input-label");
        jeproLabShareResultsLabel.setText(bundle.getString("JEPROLAB_SHARE_RESULTS_LABEL"));
        jeproLabShareResultsLabel.getStyleClass().add("input-label");
        jeproLabPublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabPublishedLabel.getStyleClass().add("input-label");
        jeproLabShareStocksLabel.setText(bundle.getString("JEPROLAB_SHARE_STOCK_LABEL"));
        jeproLabShareStocksLabel.getStyleClass().add("input-label");
        jeproLabDeletedLabel.setText(bundle.getString("JEPROLAB_DELETED_LABEL"));
        jeproLabDeletedLabel.getStyleClass().add("input-label");

        GridPane.setMargin(jeproLabLaboratoryGroupNameLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabLaboratoryGroupName, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabShareCustomerLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabShareCustomer, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabShareRequestsLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabShareRequest, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabShareResultsLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabShareResults, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabPublishedLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabPublished, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabShareStocksLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabShareStocks, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabDeletedLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabDeleted, new Insets(15, 10, 15, 10));
    }

    @Override
    public void initializeContent(int labGroupId){
        Worker<Boolean> worker = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                laboratoryGroup = new JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel(labGroupId);
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
                updateForm();
            }
        };
        JeproLab.getInstance().executor.submit((Task)worker);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        saveLaboatoryGroupButton = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"));
        saveLaboatoryGroupButton.getStyleClass().add("save-icon");

        cancelLaboratoryGroupButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"));
        cancelLaboratoryGroupButton.getStyleClass().add("cross-icon");
        commandWrapper.getChildren().addAll(saveLaboatoryGroupButton, cancelLaboratoryGroupButton);
    }

    private void updateForm(){
        Platform.runLater(() -> {
            jeproLabLaboratoryGroupName.setText(laboratoryGroup.name);
            jeproLabShareCustomer.setSelected(laboratoryGroup.share_customers);
            jeproLabShareRequest.setSelected(laboratoryGroup.share_requests);
            jeproLabShareResults.setSelected(laboratoryGroup.share_results);
            jeproLabShareStocks.setSelected(laboratoryGroup.share_stocks);
            jeproLabPublished.setSelected(laboratoryGroup.published);
            jeproLabDeleted.setSelected(laboratoryGroup.deleted);
        });
    }
}
