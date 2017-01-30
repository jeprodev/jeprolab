package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeedBacksController extends JeproLabController{
    @FXML
    public TableView<JeproLabFeedBackRecord> jeproLabFeedBackTableView;
    public TableColumn<JeproLabFeedBackRecord, String> jeproLabFeedBackIndexColumn;
    public TableColumn<JeproLabFeedBackRecord, Boolean> jeproLabFeedBackCheckBoxColumn;
    public TableColumn<JeproLabFeedBackRecord, String> jeproLabFeedBackCustomerColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackEnjoyWorkingWithUsColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackStaffCourtesyColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackTeamAvailabilitiesColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackTeamAbilitiesColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackProblemSupportColumn;
    public TableColumn<JeproLabFeedBackRecord, Boolean> jeproLabFeedBackRecommendOurServicesColumn;
    public TableColumn<JeproLabFeedBackRecord, Boolean> jeproLabFeedBackReuseOurServicesColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackServiceSpeedColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackSampleDeliveryColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackSubmissionColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackReportsQualityColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackAnalyseSpeedColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackOnlineServicesColumn;
    public TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackGlobalQualityColumn;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH ;
        double colWidth = 45;
        double remainingWidth = formWidth - (14 * colWidth) - 59;
        double rotateAngle = 270;
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> enjoyWorkingWithUsFactory, staffCourtesyFactory, teamAvailability;
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> teamAbilitiesFactory, problemSupportFactory, sampleDeliveryFactory;
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> submissionFactory, reportQualityFactory, serviceSpeedFactory;
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> analyzeSpeedFactory, onlineServicesFactory, globalQualityFactory;


        jeproLabFeedBackTableView.setPrefWidth(formWidth);
        jeproLabFeedBackTableView.setId("feedback-table-view");
        VBox.setMargin(jeproLabFeedBackTableView, new Insets(5, 0, 5, (0.01 * JeproLab.APP_WIDTH)));
        jeproLabFeedBackIndexColumn.setText("#");
        jeproLabFeedBackIndexColumn.setPrefWidth(35);
        //jeproLabFeedBackIndexColumn.getStyleClass().add("rotate-header");
        //jeproLabFeedBackCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedBackCheckBoxColumn.setPrefWidth(22);

        jeproLabFeedBackCustomerColumn.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabFeedBackCustomerColumn.setPrefWidth(remainingWidth);

        Label jeproLabFeedBackEnjoyWorkingWithUsColumnLabel = new Label(bundle.getString("JEPROLAB_ENJOY_WORKING_WITH_US_LABEL"));
        jeproLabFeedBackEnjoyWorkingWithUsColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setGraphic(jeproLabFeedBackEnjoyWorkingWithUsColumnLabel);
        jeproLabFeedBackEnjoyWorkingWithUsColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setPrefWidth(colWidth);
        enjoyWorkingWithUsFactory = param -> new JeproLabFeedBackCell();
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setCellFactory(enjoyWorkingWithUsFactory);

        Label jeproLabFeedBackStaffCourtesyColumnLabel = new Label(bundle.getString("JEPROLAB_STAFF_COURTESY_LABEL"));
        jeproLabFeedBackStaffCourtesyColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackStaffCourtesyColumn.setGraphic(jeproLabFeedBackStaffCourtesyColumnLabel);
        jeproLabFeedBackStaffCourtesyColumn.setPrefWidth(colWidth);
        jeproLabFeedBackStaffCourtesyColumn.getStyleClass().add("rotate-header");
        staffCourtesyFactory = param -> new JeproLabFeedBackCell();
        jeproLabFeedBackStaffCourtesyColumn.setCellFactory(staffCourtesyFactory);

        Label jeproLabFeedBackTeamAvailabilitiesColumnLabel = new Label(bundle.getString("JEPROLAB_TEAM_AVAILABILITIES_LABEL"));
        jeproLabFeedBackTeamAvailabilitiesColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackTeamAvailabilitiesColumn.setGraphic(jeproLabFeedBackTeamAvailabilitiesColumnLabel);
        jeproLabFeedBackTeamAvailabilitiesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackTeamAvailabilitiesColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackTeamAbilitiesColumnLabel = new Label(bundle.getString("JEPROLAB_TEAM_ABILITIES_LABEL"));
        jeproLabFeedBackTeamAbilitiesColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackTeamAbilitiesColumn.setGraphic(jeproLabFeedBackTeamAbilitiesColumnLabel);
        jeproLabFeedBackTeamAbilitiesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackTeamAbilitiesColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackProblemSupportColumnLabel = new Label(bundle.getString("JEPROLAB_PROBLEM_SUPPORT_LABEL"));
        jeproLabFeedBackProblemSupportColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackProblemSupportColumn.setGraphic(jeproLabFeedBackProblemSupportColumnLabel);
        jeproLabFeedBackProblemSupportColumn.setPrefWidth(colWidth);
        jeproLabFeedBackProblemSupportColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackRecommendOurServicesColumnLabel = new Label(bundle.getString("JEPROLAB_RECOMMEND_OUR_SERVICE_LABEL"));
        jeproLabFeedBackRecommendOurServicesColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackRecommendOurServicesColumn.setGraphic(jeproLabFeedBackRecommendOurServicesColumnLabel);
        jeproLabFeedBackRecommendOurServicesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackRecommendOurServicesColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackReuseOurServicesColumnLabel = new Label(bundle.getString("JEPROLAB_REUSE_OUR_SERVICES_LABEL"));
        jeproLabFeedBackReuseOurServicesColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackReuseOurServicesColumn.setGraphic(jeproLabFeedBackReuseOurServicesColumnLabel);
        jeproLabFeedBackReuseOurServicesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackReuseOurServicesColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackServiceSpeedColumnLabel = new Label(bundle.getString("JEPROLAB_SERVICES_SPEED_LABEL"));
        jeproLabFeedBackServiceSpeedColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackServiceSpeedColumn.setGraphic(jeproLabFeedBackServiceSpeedColumnLabel);
        jeproLabFeedBackServiceSpeedColumn.setPrefWidth(colWidth);
        jeproLabFeedBackServiceSpeedColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackSampleDeliveryColumnLabel = new Label(bundle.getString("JEPROLAB_SAMPLE_DELIVERY_LABEL"));
        jeproLabFeedBackSampleDeliveryColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackSampleDeliveryColumn.setGraphic(jeproLabFeedBackSampleDeliveryColumnLabel);
        jeproLabFeedBackSampleDeliveryColumn.setPrefWidth(colWidth);
        jeproLabFeedBackSampleDeliveryColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackSubmissionColumnLabel = new Label(bundle.getString("JEPROLAB_SUBMISSION_LABEL"));
        jeproLabFeedBackSubmissionColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackSubmissionColumn.setGraphic(jeproLabFeedBackSubmissionColumnLabel);
        jeproLabFeedBackSubmissionColumn.setPrefWidth(colWidth);
        jeproLabFeedBackSubmissionColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackReportsQualityColumnLabel = new Label(bundle.getString("JEPROLAB_REPORTS_QUALITY_LABEL"));
        jeproLabFeedBackReportsQualityColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackReportsQualityColumn.setGraphic(jeproLabFeedBackReportsQualityColumnLabel);
        jeproLabFeedBackReportsQualityColumn.setPrefWidth(colWidth);
        jeproLabFeedBackReportsQualityColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackAnalyseSpeedColumnLabel = new Label(bundle.getString("JEPROLAB_ANALYZE_SPEED_LABEL"));
        jeproLabFeedBackAnalyseSpeedColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackAnalyseSpeedColumn.setGraphic(jeproLabFeedBackAnalyseSpeedColumnLabel);
        jeproLabFeedBackAnalyseSpeedColumn.setPrefWidth(colWidth);
        jeproLabFeedBackAnalyseSpeedColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackOnlineServicesColumnLabel = new Label(bundle.getString("JEPROLAB_ONLINE_SERVICES_LABEL"));
        jeproLabFeedBackOnlineServicesColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackOnlineServicesColumn.setGraphic(jeproLabFeedBackOnlineServicesColumnLabel);
        jeproLabFeedBackOnlineServicesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackOnlineServicesColumn.getStyleClass().add("rotate-header");

        Label jeproLabFeedBackGlobalQualityColumnLabel = new Label(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        jeproLabFeedBackGlobalQualityColumnLabel.setRotate(rotateAngle);
        jeproLabFeedBackGlobalQualityColumn.setGraphic(jeproLabFeedBackGlobalQualityColumnLabel);
        jeproLabFeedBackGlobalQualityColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackGlobalQualityColumn.setPrefWidth(colWidth);
        /*Label gc = new Label(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        gc.setRotate(50);
        jeproLabFeedBackGlobalQualityColumn.setGraphic(gc); */

    }


    public static class JeproLabFeedBackRecord{

    }

    public static class JeproLabFeedBackCell extends TableCell<JeproLabFeedBackRecord, Button> {

    }
}
