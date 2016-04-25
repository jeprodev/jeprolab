package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabLaboratoryGroupAddController extends JeproLabController {
    @FXML
    public JeproFormPanel jeproLabAddLaboratoryGroupPanelWrapper;
    public JeproFormPanelTitle jeproLabAddLaboratoryGroupPanelTitleWrapper;
    public JeproFormPanelContainer jeproLabAddLaboratoryGroupPanelContainerWrapper;
    public GridPane jeproLabAddLaboratoryGroupLayout;
    public JeproSwitchButton jeproLabShareCustomer, jeproLabShareRequest, jeproLabShareResults, jeproLabPublished;

    public Label jeproLabLaboratoryGroupNameLabel, jeproLabShareCustomerLabel, jeproLabShareRequestsLabel;
    public Label jeproLabShareResultsLabel, jeproLabPublishedLabel;
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
    }

}
