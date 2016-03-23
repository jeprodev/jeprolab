package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.models.JeproLabGroupModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabGroupAddController extends JeproLabController{
    public JeproLabGroupModel group;
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
    public ComboBox<String> jeproLabGroupPriceDisplayMethod;
    public JeproSwitchButton jeproLabGroupShowPrices, jeproLabGroupSocialNetworkSharing, jeproLabGroupBannersBlock, jeproLabGroupBankWire;
    public JeproSwitchButton jeproLabGroupBestSales, jeproLabGroupCartBlock, jeproLabGroupStatisticsBlock, jeproLabGroupCurrencies;
    public JeproSwitchButton jeproLabGroupCategoryBlock, jeproLabGroupBrowserAndOperatingSystem, jeproLabGroupDashBoardStatistics;
    public JeproSwitchButton jeproLabGroupOnlineVisitors, jeproLabGroupInformationLetter, jeproLabGroupAffiliatedWebSite;
    public JeproSwitchButton jeproLabGroupCustomerDetails, jeproLabGroupAnalyzeDetails, jeproLabGroupRequests, jeproLabGroupLaboratorySearch;
    public JeproSwitchButton jeproLabGroupAvailabilities;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 180;
        double inputColumnWidth = 250;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 15;

        createNewGroupFormTitleLabel = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " "  + bundle.getString("JEPROLAB_GROUP_LABEL"));

        jeproLabGroupFormWrapper.setPrefWidth(formWidth);
        jeproLabGroupFormWrapper.setLayoutX(posX);
        jeproLabGroupFormWrapper.setLayoutY(posY);

        jeproLabGroupFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabGroupFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabGroupFormContainerWrapper.setLayoutY(40);

        groupInformationWrapper.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25),
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25)
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
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(80),
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(80),
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(80)
        );

        jeproLabGroupSocialNetworkSharingLabel.setText(bundle.getString("JEPROLAB_SOCIAL_NETWORK_SHARING_LABEL"));
        jeproLabGroupSocialNetworkSharingLabel.getStyleClass().add("input-label");
        jeproLabGroupBannersBlockLabel.setText(bundle.getString("JEPROLAB_BANNERS_BLOCK_LABEL"));
        jeproLabGroupBannersBlockLabel.getStyleClass().add("input-label");
        jeproLabGroupBankWireLabel.setText(bundle.getString("JEPROLAB_BANK_WIRE_LABEL"));
        jeproLabGroupBankWireLabel.getStyleClass().add("input-label");
        jeproLabGroupBestSalesLabel.setText(bundle.getString("JEPROLAB_BEST_SALES_LABEL"));
        jeproLabGroupBestSalesLabel.getStyleClass().add("input-label");
        jeproLabGroupCartBlockLabel.setText(bundle.getString("JEPROLAB_CART_BLOCK_LABEL"));
        jeproLabGroupCartBlockLabel.getStyleClass().add("input-label");
        jeproLabGroupStatisticsBlockLabel.setText(bundle.getString("JEPROLAB_STATISTICS_BLOCK_LABEL"));
        jeproLabGroupStatisticsBlockLabel.getStyleClass().add("input-label");
        jeproLabGroupCurrenciesLabel.setText(bundle.getString("JEPROLAB_CURRENCIES_LABEL"));
        jeproLabGroupCurrenciesLabel.getStyleClass().add("input-label");
        jeproLabGroupDashBoardStatisticsLabel.setText(bundle.getString("JEPROLAB_DASHBOARD_STATISTICS_LABEL"));
        jeproLabGroupDashBoardStatisticsLabel.getStyleClass().add("input-label");
        jeproLabGroupCategoryBlockLabel.setText(bundle.getString("JEPROLAB_CATEGORY_BLOCK_LABEL"));
        jeproLabGroupCategoryBlockLabel.getStyleClass().add("input-label");
        jeproLabGroupBrowserAndOperatingSystemLabel.setText(bundle.getString("JEPROLAB_BROWSER_AND_OPERATING_SYSTEM_LABEL"));
        jeproLabGroupBrowserAndOperatingSystemLabel.getStyleClass().add("input-label");
        jeproLabGroupAffiliatedWebSiteLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupAffiliatedWebSiteLabel.getStyleClass().add("input-label");
        jeproLabGroupInformationLetterLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupInformationLetterLabel.getStyleClass().add("input-label");
        jeproLabGroupOnlineVisitorsLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabGroupOnlineVisitorsLabel.getStyleClass().add("input-label");
        jeproLabGroupCustomerDetailsLabel.setText(bundle.getString("JEPROLAB_ONLINE_VISITORS_LABEL"));
        jeproLabGroupCustomerDetailsLabel.getStyleClass().add("input-label");
        jeproLabGroupAnalyzeDetailsLabel.setText(bundle.getString("JEPROLAB_ANALYZE_DETAILS_LABEL"));
        jeproLabGroupAnalyzeDetailsLabel.getStyleClass().add("input-label");
        jeproLabGroupRequestsLabel.setText(bundle.getString("JEPROLAB_REQUESTS_LABEL"));
        jeproLabGroupRequestsLabel.getStyleClass().add("input-label");
        jeproLabGroupLaboratorySearchLabel.setText(bundle.getString("JEPROLAB_LABORATORY_SEARCH_LABEL"));
        jeproLabGroupLaboratorySearchLabel.getStyleClass().add("input-label");
        jeproLabGroupAvailabilitiesLabel.setText(bundle.getString("JEPROLAB_AVAILABILITIES_LABEL"));
        jeproLabGroupAvailabilitiesLabel.getStyleClass().add("input-label");

        GridPane.setMargin(jeproLabGroupBrowserAndOperatingSystemLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupAffiliatedWebSiteLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupInformationLetterLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupOnlineVisitorsLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupCustomerDetailsLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupAnalyzeDetailsLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupRequestsLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupLaboratorySearchLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupAvailabilitiesLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupCategoryBlockLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupDashBoardStatisticsLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupCurrenciesLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupStatisticsBlockLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupCartBlockLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupBestSalesLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupBankWireLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupBannersBlockLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabGroupSocialNetworkSharingLabel, new Insets(10, 10, 5, 20));

        initializeContent();
    }

    @Override
    public void initializeContent(){
        jeproLabGroupPriceDisplayMethod.setItems(
                FXCollections.observableArrayList(
                        bundle.getString("JEPROLAB_TAX_INCLUDED_LABEL"),
                        bundle.getString("JEPROLAB_TAX_EXCLUDED_LABEL")
                )
        );
        
        //if(group.group_id > 0) {
            /*jeproLabGroupSocialNetworkSharing, jeproLabGroupBannersBlock, jeproLabGroupBankWire;
            jeproLabGroupBestSales, jeproLabGroupCartBlock, jeproLabGroupStatisticsBlock, jeproLabGroupCurrencies;
            jeproLabGroupCategoryBlock, jeproLabGroupBrowserAndOperatingSystem, jeproLabGroupDashBoardStatistics;
            jeproLabGroupOnlineVisitors, jeproLabGroupInformationLetter, jeproLabGroupAffiliatedWebSite;
            jeproLabGroupCustomerDetails, jeproLabGroupAnalyzeDetails, jeproLabGroupRequests, jeproLabGroupLaboratorySearch;
            jeproLabGroupAvailabilities;*/
        //}
    }

    @Override
    public void updateToolBar(){}
}