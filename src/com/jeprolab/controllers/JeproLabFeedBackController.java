package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public class JeproLabFeedBackController extends JeproLabController{
    @FXML
    public TableView jeproLabFeedBackTableView;
    public TableColumn jeproLabFeedBackIndexColumn, jeproLabFeedBackCheckBoxColumn, jeproLabFeedBackCustomerColumn;
    public TableColumn jeproLabFeedBackEnjoyWorkingWithUsColumn, jeproLabFeedBackStaffCourtesyColumn, jeproLabFeedBackTeamAvailabilitiesColumn;
    public TableColumn jeproLabFeedBackTeamAbilitiesColumn, jeproLabFeedBackProblemSupportColumn, jeproLabFeedBackRecommendOurServicesColumn;
    public TableColumn jeproLabFeedBackReuseOurServicesColumn, jeproLabFeedBackServiceSpeedColumn, jeproLabFeedBackSampleDeliveryColumn;
    public TableColumn jeproLabFeedBackSubmissionColumn, jeproLabFeedBackReportsQualityColumn, jeproLabFeedBackAnalyseSpeedColumn;
    public TableColumn jeproLabFeedBackOnlineServicesColumn, jeproLabFeedBackGlobalQualityColumn;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double tableWidth = JeproLab.APP_WIDTH ;
        double remainingWidth = tableWidth - 60;

        jeproLabFeedBackTableView.setPrefWidth(tableWidth);
        jeproLabFeedBackIndexColumn.setText("#");
        jeproLabFeedBackIndexColumn.setPrefWidth(35);
        jeproLabFeedBackIndexColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackCheckBoxColumn.setPrefWidth(25);
        jeproLabFeedBackCustomerColumn.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setText(bundle.getString("JEPROLAB_ENJOY_WORKING_WITH_US_LABEL"));
        jeproLabFeedBackEnjoyWorkingWithUsColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackStaffCourtesyColumn.setText(bundle.getString("JEPROLAB_STAFF_COURTESY_LABEL"));
        jeproLabFeedBackStaffCourtesyColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackTeamAvailabilitiesColumn.setText(bundle.getString("JEPROLAB_TEAM_AVAILABILITIES_LABEL"));
        jeproLabFeedBackTeamAvailabilitiesColumn.getStyleClass().add("rotate-header");;
        jeproLabFeedBackTeamAbilitiesColumn.setText(bundle.getString("JEPROLAB_TEAM_ABILITIES_LABEL"));
        jeproLabFeedBackTeamAbilitiesColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackProblemSupportColumn.setText(bundle.getString("JEPROLAB_PROBLEM_SUPPORT_LABEL"));
        jeproLabFeedBackProblemSupportColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackRecommendOurServicesColumn.setText(bundle.getString("JEPROLAB_RECOMMEND_OUR_SERVICE_LABEL"));
        jeproLabFeedBackRecommendOurServicesColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackReuseOurServicesColumn.setText(bundle.getString("JEPROLAB_REUSE_OUR_SERVICES_LABEL"));
        jeproLabFeedBackReuseOurServicesColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackServiceSpeedColumn.setText(bundle.getString("JEPROLAB_SERVICES_SPEED_LABEL"));
        jeproLabFeedBackServiceSpeedColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackSampleDeliveryColumn.setText(bundle.getString("JEPROLAB_SAMPLE_DELIVERY_LABEL"));
        jeproLabFeedBackSampleDeliveryColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackSubmissionColumn.setText(bundle.getString("JEPROLAB_SUBMISSION_LABEL"));
        jeproLabFeedBackSubmissionColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackReportsQualityColumn.setText(bundle.getString("JEPROLAB_REPORTS_QUALITY_LABEL"));
        jeproLabFeedBackReportsQualityColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackAnalyseSpeedColumn.setText(bundle.getString("JEPROLAB_ANALYSE_SPEED_LABEL"));
        jeproLabFeedBackAnalyseSpeedColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackOnlineServicesColumn.setText(bundle.getString("JEPROLAB_ONLINE_SERVICES_LABEL"));
        jeproLabFeedBackOnlineServicesColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackGlobalQualityColumn.setText(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        jeproLabFeedBackGlobalQualityColumn.getStyleClass().add("rotate-header");
        /*Label gc = new Label(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        gc.setRotate(50);
        jeproLabFeedBackGlobalQualityColumn.setGraphic(gc); */
    }
}