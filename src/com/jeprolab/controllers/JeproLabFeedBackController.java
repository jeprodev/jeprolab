package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    private CheckBox checkAll;
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
        double tableWidth = 0.99 * JeproLab.APP_WIDTH ;
        checkAll = new CheckBox();
        double remainingWidth = tableWidth - 180;
        double colWidth = 0.0714 * remainingWidth;

        jeproLabFeedBackTableView.setPrefWidth(tableWidth);
        jeproLabFeedBackTableView.setLayoutX(0.005 * JeproLab.APP_WIDTH);
        jeproLabFeedBackTableView.setLayoutY(20);
        jeproLabFeedBackIndexColumn.setText("#");
        jeproLabFeedBackIndexColumn.setPrefWidth(30);

        //jeproLabFeedBackIndexColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackCheckBoxColumn.setGraphic(checkAll);
        jeproLabFeedBackCheckBoxColumn.setPrefWidth(25);
        jeproLabFeedBackCustomerColumn.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabFeedBackCustomerColumn.setPrefWidth(120);
        Text jeproLabFeedBackEnjoyWorkingWithUsLabel = new Text(bundle.getString("JEPROLAB_ENJOY_WORKING_WITH_US_LABEL")); //.getStyleClass().add("rotate-header");
        jeproLabFeedBackEnjoyWorkingWithUsLabel.setRotate(-60);
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setGraphic(jeproLabFeedBackEnjoyWorkingWithUsLabel);
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setPrefWidth(colWidth);
        //jeproLabFeedBackEnjoyWorkingWithUsColumn.getCellData(0)
        Text jeproLabFeedBackStaffCourtesyLabel = new Text(bundle.getString("JEPROLAB_STAFF_COURTESY_LABEL"));
        jeproLabFeedBackStaffCourtesyLabel.setRotate(-60);
        jeproLabFeedBackStaffCourtesyColumn.setGraphic(jeproLabFeedBackStaffCourtesyLabel);
        jeproLabFeedBackStaffCourtesyColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackTeamAvailabilitiesLabel = new Text(bundle.getString("JEPROLAB_TEAM_AVAILABILITIES_LABEL"));
        jeproLabFeedBackTeamAvailabilitiesLabel.setRotate(-60);
        jeproLabFeedBackTeamAvailabilitiesColumn.setGraphic(jeproLabFeedBackTeamAvailabilitiesLabel);
        jeproLabFeedBackTeamAvailabilitiesColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackTeamAbilitiesLabel = new Text(bundle.getString("JEPROLAB_TEAM_ABILITIES_LABEL"));
        jeproLabFeedBackTeamAbilitiesLabel.setRotate(-60);
        jeproLabFeedBackTeamAbilitiesColumn.setGraphic(jeproLabFeedBackTeamAbilitiesLabel);
        jeproLabFeedBackTeamAbilitiesColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackProblemSupportLabel = new Text(bundle.getString("JEPROLAB_PROBLEM_SUPPORT_LABEL"));
        jeproLabFeedBackProblemSupportLabel.setRotate(-60);
        jeproLabFeedBackProblemSupportColumn.setGraphic(jeproLabFeedBackProblemSupportLabel);
        jeproLabFeedBackProblemSupportColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackRecommendOurServicesLabel = new Text(bundle.getString("JEPROLAB_RECOMMEND_OUR_SERVICE_LABEL"));
        jeproLabFeedBackRecommendOurServicesLabel.setRotate(-60);
        jeproLabFeedBackRecommendOurServicesColumn.setGraphic(jeproLabFeedBackRecommendOurServicesLabel);
        jeproLabFeedBackRecommendOurServicesColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackReuseOurServicesLabel = new Text(bundle.getString("JEPROLAB_REUSE_OUR_SERVICES_LABEL"));
        jeproLabFeedBackReuseOurServicesLabel.setRotate(-60);
        jeproLabFeedBackReuseOurServicesColumn.setGraphic(jeproLabFeedBackReuseOurServicesLabel);
        jeproLabFeedBackReuseOurServicesColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackServiceSpeedLabel = new Text(bundle.getString("JEPROLAB_SERVICES_SPEED_LABEL"));
        jeproLabFeedBackServiceSpeedLabel.setRotate(-60);
        jeproLabFeedBackServiceSpeedColumn.setGraphic(jeproLabFeedBackServiceSpeedLabel);
        jeproLabFeedBackServiceSpeedColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackSampleDeliveryLabel = new Text(bundle.getString("JEPROLAB_SAMPLE_DELIVERY_LABEL"));
        jeproLabFeedBackSampleDeliveryLabel.setRotate(-60);
        jeproLabFeedBackSampleDeliveryColumn.setGraphic(jeproLabFeedBackSampleDeliveryLabel);
        jeproLabFeedBackSampleDeliveryColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackSubmissionLabel = new Text(bundle.getString("JEPROLAB_SUBMISSION_LABEL"));
        jeproLabFeedBackSubmissionLabel.setRotate(-60);
        jeproLabFeedBackSubmissionColumn.setGraphic(jeproLabFeedBackSubmissionLabel);
        jeproLabFeedBackSubmissionColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackReportsQualityLabel = new Text(bundle.getString("JEPROLAB_REPORTS_QUALITY_LABEL"));
        jeproLabFeedBackReportsQualityLabel.setRotate(-60);
        jeproLabFeedBackReportsQualityColumn.setGraphic(jeproLabFeedBackReportsQualityLabel);
        jeproLabFeedBackReportsQualityColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackAnalyseSpeedLabel = new Text(bundle.getString("JEPROLAB_ANALYSE_SPEED_LABEL"));
        jeproLabFeedBackAnalyseSpeedLabel.setRotate(-60);
        jeproLabFeedBackAnalyseSpeedColumn.setGraphic(jeproLabFeedBackAnalyseSpeedLabel);
        jeproLabFeedBackAnalyseSpeedColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackOnlineServicesLabel = new Text(bundle.getString("JEPROLAB_ONLINE_SERVICES_LABEL"));
        jeproLabFeedBackOnlineServicesLabel.setRotate(-60);
        jeproLabFeedBackOnlineServicesColumn.setGraphic(jeproLabFeedBackOnlineServicesLabel);
        jeproLabFeedBackOnlineServicesColumn.setPrefWidth(colWidth);
        Text jeproLabFeedBackGlobalQualityLabel = new Text(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        jeproLabFeedBackGlobalQualityLabel.setRotate(-60);
        jeproLabFeedBackGlobalQualityColumn.setGraphic(jeproLabFeedBackGlobalQualityLabel);

        /*Label gc = new Label(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        gc.setRotate(50);
        jeproLabFeedBackGlobalQualityColumn.setGraphic(gc); */
    }
}