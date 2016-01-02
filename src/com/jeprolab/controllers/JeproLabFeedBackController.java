package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public class JeproLabFeedBackController extends JeproLabController{
    @FXML
    public TableColumn jeproLabFeedBackIndexLabel, jeproLabFeedBackCheckBoxLabel, jeproLabFeedBackCustomerLabel;
    public TableColumn jeproLabFeedBackEnjoyWorkingWithUsLabel, jeproLabFeedBackStaffCourtesyLabel, jeproLabFeedBackTeamAvailabilitiesLabel;
    public TableColumn jeproLabFeedBackTeamAbilitiesLabel, jeproLabFeedBackProblemSupportLabel, jeproLabFeedBackRecommendOurServicesLabel;
    public TableColumn jeproLabFeedBackReuseOurServicesLabel, jeproLabFeedBackServiceSpeedLabel, jeproLabFeedBackSampleDeliveryLabel;
    public TableColumn jeproLabFeedBackSubmissionLabel, jeproLabFeedBackReportsQualityLabel, jeproLabFeedBackAnalyseSpeedLabel;
    public TableColumn jeproLabFeedBackOnlineServicesLabel, jeproLabFeedBackGlobalQualityLabel;

    public void initialize(URL location, ResourceBundle resource){
        jeproLabFeedBackIndexLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackIndexLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackCheckBoxLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackCustomerLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackEnjoyWorkingWithUsLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackEnjoyWorkingWithUsLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackStaffCourtesyLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackStaffCourtesyLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackTeamAvailabilitiesLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackTeamAvailabilitiesLabel.getStyleClass().add("rotate-header");;
        jeproLabFeedBackTeamAbilitiesLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackTeamAbilitiesLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackProblemSupportLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackProblemSupportLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackRecommendOurServicesLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackRecommendOurServicesLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackReuseOurServicesLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackReuseOurServicesLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackServiceSpeedLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackServiceSpeedLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackSampleDeliveryLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackSampleDeliveryLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackSubmissionLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackSubmissionLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackReportsQualityLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackReportsQualityLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackAnalyseSpeedLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackAnalyseSpeedLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackOnlineServicesLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackOnlineServicesLabel.getStyleClass().add("rotate-header");
        jeproLabFeedBackGlobalQualityLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackGlobalQualityLabel.getStyleClass().add("rotate-header");
    }
}