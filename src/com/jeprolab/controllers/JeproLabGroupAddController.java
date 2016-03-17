package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabGroupAddController extends JeproLabController{
    @FXML
    public JeproFormPanel jeproLabGroupFormWrapper;
    public JeproFormPanelTitle jeproLabGroupFormTitleWrapper;
    public JeproFormPanelContainer jeproLabGroupFormContainerWrapper;
    public GridPane groupInformationWrapper, groupModulesWrapper;
    public Label createNewGroupFormTitleLabel, jeproLabGroupNameLabel, jeproLabGroupReductionLabel, jeproLabGroupPriceDisplayMethodLabel;
    public Label jeproLabGroupShowPricesLabel, jeproLabGroupSocialNetworkSharingLabel, jeproLabGroupBannersBlockLabel, jeproLabGroupBankWireLabel;
    public Label jeproLabGroupBestSalesLabel, jeproLabGroupCartBlockLabel, jeproLabGroupStatisticsBlockLabel, jeproLabGroupCurrenciesLabel;
    public Label jeproLabGroupCategoryBlockLabel, jeproLabGroupBrowserAndOperatingSystemLabel, jeproLabGroupDashBoardStatisticsLabel;
    public Label jeproLabGroupOnlineVisitorsLabel, jeproLabGroupInformationLetterLabel, jeproLabGroupAffiliatedWebSiteLabel;
    public Label jeproLabGroupCustomerDetailsLabel, jeproLabGroupAnalyzeDetailsLabel, jeproLabGroupRequestsLabel, jeproLabGroupLaboratorySearchLabel;
    public Label jeproLabGroupAvailabilitiesLabel;
    public JeproMultiLangTextField jeproLabGroupName;
    public JeproAppendButton jeproLabGroupReduction;
    public ComboBox jeproLabGroupPriceDisplayMethod;
    public JeproSwitchButton jeproLabGroupShowPrices;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 180;
        double inputColumnWidth = 250;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 15;

        jeproLabGroupFormWrapper.setPrefWidth(formWidth);
        jeproLabGroupFormWrapper.setLayoutX(posX);
        jeproLabGroupFormWrapper.setLayoutY(posY);

        jeproLabGroupFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabGroupFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabGroupFormContainerWrapper.setLayoutY(40);

        groupInformationWrapper.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );

        jeproLabGroupNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabGroupNameLabel.getStyleClass().add("input-label");
        jeproLabGroupReductionLabel.setText(bundle.getString("JEPROLAB_REDUCTION_LABEL"));
        jeproLabGroupReductionLabel.getStyleClass().add("input-label");
        jeproLabGroupPriceDisplayMethodLabel.setText(bundle.getString("JEPROLAB_PRICE_DISPLAY_METHOD_LABEL"));
        jeproLabGroupPriceDisplayMethodLabel.getStyleClass().add("input-label");
        jeproLabGroupShowPricesLabel.setText(bundle.getString("JEPROLAB_DISPLAY_PRICES_LABEL"));
        jeproLabGroupShowPricesLabel.getStyleClass().add("input-label");

        GridPane.setMargin(jeproLabGroupNameLabel, new Insets(10, 10, 10, 20));
        GridPane.setMargin(jeproLabGroupName, new Insets(10, 10, 10, 0));
        GridPane.setMargin(jeproLabGroupReductionLabel, new Insets(10, 10, 10, 20));
        GridPane.setMargin(jeproLabGroupReduction, new Insets(10, 10, 10, 0));
        GridPane.setMargin(jeproLabGroupPriceDisplayMethodLabel, new Insets(10, 10, 10, 20));
        GridPane.setMargin(jeproLabGroupPriceDisplayMethod, new Insets(10, 10, 10, 0));
        GridPane.setMargin(jeproLabGroupShowPricesLabel, new Insets(10, 10, 10, 20));
        GridPane.setMargin(jeproLabGroupShowPrices, new Insets(10, 10, 10, 0));

        groupModulesWrapper.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(80),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(80),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(80)
        );

        jeproLabGroupSocialNetworSharinLabel.setText(bundle.getString("JEPROLAB_SOCIAL_NETWORK_SHARING_LABEL"));
        jeproLabGroupSocialNetworkSharingLabel.getStyleClass().add("input-label");
        jeproLabGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupLabel.getStyleClass().add("input-label");
        jeproLabGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupLabel.getStyleClass().add("input-label");
        jeproLabGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupLabel.getStyleClass().add("input-label");
        jeproLabGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupLabel.getStyleClass().add("input-label");
        jeproLabGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupLabel.getStyleClass().add("input-label");
        jeproLabGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupLabel.getStyleClass().add("input-label");

        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLabel, new Insets(10, 10, 5, 20));
    }

    @Override
    public void updateToolBar(){}
}