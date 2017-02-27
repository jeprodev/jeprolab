package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabFeedModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeedBacksController extends JeproLabController{
    private CheckBox checkAll;
    private TableView<JeproLabFeedBackRecord> jeproLabFeedBackTableView;
    private HBox jeproLabFeedBackSearchWrapper;
    private TextField jeproLabFeedBackSearchField;
    private ComboBox<String> jeproLabFeedBackSearchFilter;
    private Button jeproLabFeedBackSearchButton;
    private ObservableList<JeproLabFeedBackRecord> feedBacksList;

    @FXML
    public VBox jeproLabFeedBackTableViewWrapper;



    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH ;
        double colWidth = 45;
        double remainingWidth = formWidth - (14 * colWidth) - 59;
        double rotateAngle = 270;

        jeproLabFeedBackTableView = new TableView<>();
        jeproLabFeedBackTableView.setPrefSize(formWidth, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);
        jeproLabFeedBackTableView.setId("feedback-table-view");
        VBox.setMargin(jeproLabFeedBackTableView, new Insets(5, 0, 5, (0.01 * JeproLab.APP_WIDTH)));

        TableColumn<JeproLabFeedBackRecord, String> jeproLabFeedBackIndexColumn = new TableColumn<>("#");
        jeproLabFeedBackIndexColumn.setPrefWidth(35);
        tableCellAlign(jeproLabFeedBackIndexColumn, Pos.CENTER_RIGHT);
        jeproLabFeedBackIndexColumn.setCellValueFactory(new PropertyValueFactory<>("feedBackIndex"));

        checkAll = new CheckBox();
        TableColumn<JeproLabFeedBackRecord, Boolean> jeproLabFeedBackCheckBoxColumn = new TableColumn<>();
        jeproLabFeedBackCheckBoxColumn.setGraphic(checkAll);
        jeproLabFeedBackCheckBoxColumn.setPrefWidth(22);
        Callback<TableColumn<JeproLabFeedBackRecord, Boolean>, TableCell<JeproLabFeedBackRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabFeedBackCheckBoxCellFactory();
        jeproLabFeedBackCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabFeedBackRecord, String> jeproLabFeedBackCustomerColumn = new TableColumn<>(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabFeedBackCustomerColumn.setPrefWidth(remainingWidth);
        jeproLabFeedBackCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("feedBackCustomer"));
        tableCellAlign(jeproLabFeedBackCustomerColumn, Pos.CENTER_LEFT);

        Label jeproLabFeedBackEnjoyWorkingWithUsColumnLabel = new Label(bundle.getString("JEPROLAB_ENJOY_WORKING_WITH_US_LABEL"));
        jeproLabFeedBackEnjoyWorkingWithUsColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackEnjoyWorkingWithUsColumn = new TableColumn<>();
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setGraphic(jeproLabFeedBackEnjoyWorkingWithUsColumnLabel);
        jeproLabFeedBackEnjoyWorkingWithUsColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setPrefWidth(colWidth);
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> enjoyWorkingWithUsFactory = params -> new JeproLabFeedBackWorkingWithUsCellFactory();
        jeproLabFeedBackEnjoyWorkingWithUsColumn.setCellFactory(enjoyWorkingWithUsFactory);

        Label jeproLabFeedBackStaffCourtesyColumnLabel = new Label(bundle.getString("JEPROLAB_STAFF_COURTESY_LABEL"));
        jeproLabFeedBackStaffCourtesyColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackStaffCourtesyColumn = new TableColumn<>();
        jeproLabFeedBackStaffCourtesyColumn.setGraphic(jeproLabFeedBackStaffCourtesyColumnLabel);
        jeproLabFeedBackStaffCourtesyColumn.setPrefWidth(colWidth);
        jeproLabFeedBackStaffCourtesyColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> staffCourtesyFactory = param -> new JeproLabFeedBackCourtesyCellFactory();
        jeproLabFeedBackStaffCourtesyColumn.setCellFactory(staffCourtesyFactory);

        Label jeproLabFeedBackTeamAvailabilitiesColumnLabel = new Label(bundle.getString("JEPROLAB_TEAM_AVAILABILITIES_LABEL"));
        jeproLabFeedBackTeamAvailabilitiesColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackTeamAvailabilitiesColumn = new TableColumn<>();
        jeproLabFeedBackTeamAvailabilitiesColumn.setGraphic(jeproLabFeedBackTeamAvailabilitiesColumnLabel);
        jeproLabFeedBackTeamAvailabilitiesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackTeamAvailabilitiesColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> teamAvailabilityCellFactory = params -> new JeproLabFeedBackTeamAvailableCellFactory();
        jeproLabFeedBackTeamAvailabilitiesColumn.setCellFactory(teamAvailabilityCellFactory);

        Label jeproLabFeedBackTeamAbilitiesColumnLabel = new Label(bundle.getString("JEPROLAB_TEAM_ABILITIES_LABEL"));
        jeproLabFeedBackTeamAbilitiesColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackTeamAbilitiesColumn = new TableColumn<>();
        jeproLabFeedBackTeamAbilitiesColumn.setGraphic(jeproLabFeedBackTeamAbilitiesColumnLabel);
        jeproLabFeedBackTeamAbilitiesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackTeamAbilitiesColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> teamAbilitiesCellFactory = params -> new JeproLabFeedBackTeamAbilitiesCellFactory();
        jeproLabFeedBackTeamAbilitiesColumn.setCellFactory(teamAbilitiesCellFactory);

        Label jeproLabFeedBackProblemSupportColumnLabel = new Label(bundle.getString("JEPROLAB_PROBLEM_SUPPORT_LABEL"));
        jeproLabFeedBackProblemSupportColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackProblemSupportColumn = new TableColumn<>();
        jeproLabFeedBackProblemSupportColumn.setGraphic(jeproLabFeedBackProblemSupportColumnLabel);
        jeproLabFeedBackProblemSupportColumn.setPrefWidth(colWidth);
        jeproLabFeedBackProblemSupportColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> problemSupportCellFactory = params -> new JeproLabFeedBackProductSupportCellFactory();
        jeproLabFeedBackProblemSupportColumn.setCellFactory(problemSupportCellFactory);

        Label jeproLabFeedBackRecommendOurServicesColumnLabel = new Label(bundle.getString("JEPROLAB_RECOMMEND_OUR_SERVICE_LABEL"));
        jeproLabFeedBackRecommendOurServicesColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Boolean> jeproLabFeedBackRecommendOurServicesColumn = new TableColumn<>();
        jeproLabFeedBackRecommendOurServicesColumn.setGraphic(jeproLabFeedBackRecommendOurServicesColumnLabel);
        jeproLabFeedBackRecommendOurServicesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackRecommendOurServicesColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Boolean>, TableCell<JeproLabFeedBackRecord, Boolean>> recommendOurServicesCellFactory = param -> new JeproLabFeedBackRecommendOurServicesCellFactory();
        jeproLabFeedBackRecommendOurServicesColumn.setCellFactory(recommendOurServicesCellFactory);

        Label jeproLabFeedBackReuseOurServicesColumnLabel = new Label(bundle.getString("JEPROLAB_REUSE_OUR_SERVICES_LABEL"));
        jeproLabFeedBackReuseOurServicesColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Boolean> jeproLabFeedBackReuseOurServicesColumn = new TableColumn<>();
        jeproLabFeedBackReuseOurServicesColumn.setGraphic(jeproLabFeedBackReuseOurServicesColumnLabel);
        jeproLabFeedBackReuseOurServicesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackReuseOurServicesColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Boolean>, TableCell<JeproLabFeedBackRecord, Boolean>> reuseOurServicesCellFactory = params -> new JeproLabFeedBackReuseOurServicesCellFactory();
        jeproLabFeedBackReuseOurServicesColumn.setCellFactory(reuseOurServicesCellFactory);

        Label jeproLabFeedBackServiceSpeedColumnLabel = new Label(bundle.getString("JEPROLAB_SERVICES_SPEED_LABEL"));
        jeproLabFeedBackServiceSpeedColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackServiceSpeedColumn = new TableColumn<>();
        jeproLabFeedBackServiceSpeedColumn.setGraphic(jeproLabFeedBackServiceSpeedColumnLabel);
        jeproLabFeedBackServiceSpeedColumn.setPrefWidth(colWidth);
        jeproLabFeedBackServiceSpeedColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> servicesSpeedCellFactory = params -> new JeproLabFeedBackServicesSpeedCellFactory();
        jeproLabFeedBackServiceSpeedColumn.setCellFactory(servicesSpeedCellFactory);

        Label jeproLabFeedBackSampleDeliveryColumnLabel = new Label(bundle.getString("JEPROLAB_SAMPLE_DELIVERY_LABEL"));
        jeproLabFeedBackSampleDeliveryColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackSampleDeliveryColumn = new TableColumn<>();
        jeproLabFeedBackSampleDeliveryColumn.setGraphic(jeproLabFeedBackSampleDeliveryColumnLabel);
        jeproLabFeedBackSampleDeliveryColumn.setPrefWidth(colWidth);
        jeproLabFeedBackSampleDeliveryColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> sampleDeliveryCellFactory = params ->  new JeproLabFeedBackSampleDeliveryCellFactory();
        jeproLabFeedBackSampleDeliveryColumn.setCellFactory(sampleDeliveryCellFactory);

        Label jeproLabFeedBackSubmissionColumnLabel = new Label(bundle.getString("JEPROLAB_SUBMISSION_LABEL"));
        jeproLabFeedBackSubmissionColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackSubmissionColumn = new TableColumn<>();
        jeproLabFeedBackSubmissionColumn.setGraphic(jeproLabFeedBackSubmissionColumnLabel);
        jeproLabFeedBackSubmissionColumn.setPrefWidth(colWidth);
        jeproLabFeedBackSubmissionColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>>  submissionCellFactory = params -> new JeproLabFeedBackSubmissionCellFactory();
        jeproLabFeedBackSubmissionColumn.setCellFactory(submissionCellFactory);

        Label jeproLabFeedBackReportsQualityColumnLabel = new Label(bundle.getString("JEPROLAB_REPORTS_QUALITY_LABEL"));
        jeproLabFeedBackReportsQualityColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackReportsQualityColumn = new TableColumn<>();
        jeproLabFeedBackReportsQualityColumn.setGraphic(jeproLabFeedBackReportsQualityColumnLabel);
        jeproLabFeedBackReportsQualityColumn.setPrefWidth(colWidth);
        jeproLabFeedBackReportsQualityColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> reportQualityCellFactory = param -> new JeproLabFeedBackReportQualityCellFactory();
        jeproLabFeedBackReportsQualityColumn.setCellFactory(reportQualityCellFactory);

        Label jeproLabFeedBackAnalyseSpeedColumnLabel = new Label(bundle.getString("JEPROLAB_ANALYZE_SPEED_LABEL"));
        jeproLabFeedBackAnalyseSpeedColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackAnalyseSpeedColumn = new TableColumn<>();
        jeproLabFeedBackAnalyseSpeedColumn.setGraphic(jeproLabFeedBackAnalyseSpeedColumnLabel);
        jeproLabFeedBackAnalyseSpeedColumn.setPrefWidth(colWidth);
        jeproLabFeedBackAnalyseSpeedColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> analyzeSpeedBackCellFactory = params -> new JeproLabFeedBackAnalyzeSpeedCellFactory();
        jeproLabFeedBackAnalyseSpeedColumn.setCellFactory(analyzeSpeedBackCellFactory);

        Label jeproLabFeedBackOnlineServicesColumnLabel = new Label(bundle.getString("JEPROLAB_ONLINE_SERVICES_LABEL"));
        jeproLabFeedBackOnlineServicesColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackOnlineServicesColumn = new TableColumn<>();
        jeproLabFeedBackOnlineServicesColumn.setGraphic(jeproLabFeedBackOnlineServicesColumnLabel);
        jeproLabFeedBackOnlineServicesColumn.setPrefWidth(colWidth);
        jeproLabFeedBackOnlineServicesColumn.getStyleClass().add("rotate-header");
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> onlineServicesCellFactory = params-> new JeproLabFeedBackOnlineServicesCellFactory();
        jeproLabFeedBackOnlineServicesColumn.setCellFactory(onlineServicesCellFactory);

        Label jeproLabFeedBackGlobalQualityColumnLabel = new Label(bundle.getString("JEPROLAB_GLOBAL_QUALITY_LABEL"));
        jeproLabFeedBackGlobalQualityColumnLabel.setRotate(rotateAngle);
        TableColumn<JeproLabFeedBackRecord, Button> jeproLabFeedBackGlobalQualityColumn = new TableColumn<>();
        jeproLabFeedBackGlobalQualityColumn.setGraphic(jeproLabFeedBackGlobalQualityColumnLabel);
        jeproLabFeedBackGlobalQualityColumn.getStyleClass().add("rotate-header");
        jeproLabFeedBackGlobalQualityColumn.setPrefWidth(colWidth);
        Callback<TableColumn<JeproLabFeedBackRecord, Button>, TableCell<JeproLabFeedBackRecord, Button>> globalQualityCellFactory = params -> new JeproLabFeedBackGlobalQualityCellFactory();
        jeproLabFeedBackGlobalQualityColumn.setCellFactory(globalQualityCellFactory);

        jeproLabFeedBackTableView.getColumns().addAll(
            jeproLabFeedBackIndexColumn, jeproLabFeedBackCheckBoxColumn, jeproLabFeedBackCustomerColumn,
            jeproLabFeedBackEnjoyWorkingWithUsColumn, jeproLabFeedBackStaffCourtesyColumn, jeproLabFeedBackTeamAvailabilitiesColumn,
            jeproLabFeedBackTeamAbilitiesColumn, jeproLabFeedBackProblemSupportColumn, jeproLabFeedBackRecommendOurServicesColumn,
            jeproLabFeedBackReuseOurServicesColumn, jeproLabFeedBackServiceSpeedColumn, jeproLabFeedBackSampleDeliveryColumn,
            jeproLabFeedBackSubmissionColumn, jeproLabFeedBackReportsQualityColumn, jeproLabFeedBackAnalyseSpeedColumn,
            jeproLabFeedBackOnlineServicesColumn, jeproLabFeedBackGlobalQualityColumn
        );

        jeproLabFeedBackSearchWrapper = new HBox(5);

        jeproLabFeedBackSearchField = new TextField();
        jeproLabFeedBackSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabFeedBackSearchFilter = new ComboBox<>();
        jeproLabFeedBackSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabFeedBackSearchButton = new Button();
        jeproLabFeedBackSearchButton.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabFeedBackSearchWrapper.getChildren().addAll(
            jeproLabFeedBackSearchField, jeproLabFeedBackSearchFilter, jeproLabFeedBackSearchButton
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>(){
            List<JeproLabFeedModel.JeproLabFeedBackModel> feedBacks;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){ return false; }
                feedBacks = JeproLabFeedModel.JeproLabFeedBackModel.getFeedBacks();
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
                updateFeedBackTableView(feedBacks);
            }
        };

        new Thread((Task)worker).start();
    }

    private void updateFeedBackTableView(List<JeproLabFeedModel.JeproLabFeedBackModel> feedBacks){
        feedBacksList = FXCollections.observableArrayList();
        feedBacksList.addAll(feedBacks.stream().map(JeproLabFeedBackRecord::new).collect(Collectors.toList()));
        double padding = 0.01 * JeproLab.APP_WIDTH;

        if(!feedBacksList.isEmpty()){
            Platform.runLater(() -> {
                Pagination jeproLabFeedBacksPagination = new Pagination((feedBacksList.size()/ JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabFeedBacksPagination.setPageFactory(this::createFeedBackPages);
                jeproLabFeedBackTableViewWrapper.getChildren().clear();
                VBox.setMargin(jeproLabFeedBackSearchWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabFeedBacksPagination, new Insets(5, padding, 5, padding));

                jeproLabFeedBackTableViewWrapper.getChildren().addAll(jeproLabFeedBackSearchWrapper, jeproLabFeedBacksPagination);
            });
        }else{
            Platform.runLater(() -> {
                VBox.setMargin(jeproLabFeedBackSearchWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabFeedBackTableView, new Insets(5, padding, 5, padding));
                jeproLabFeedBackTableViewWrapper.getChildren().clear();
                jeproLabFeedBackTableViewWrapper.getChildren().addAll(jeproLabFeedBackSearchWrapper, jeproLabFeedBackTableView);
            });
        }
    }

    private Node createFeedBackPages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (feedBacksList.size()));
        jeproLabFeedBackTableView.setItems(FXCollections.observableArrayList(feedBacksList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabFeedBackTableView);
    }


    public static class JeproLabFeedBackRecord{
        private SimpleIntegerProperty feedBackIndex;
        private SimpleStringProperty feedBackCustomer;
        private SimpleStringProperty feedBackEnjoyWorkingWithUs;
        private SimpleStringProperty feedBackStaffCourtesy;
        private SimpleStringProperty feedBackTeamAbilities;
        private SimpleStringProperty feedBackTeamAvailability;
        private SimpleStringProperty feedBackProblemSupport;
        private SimpleBooleanProperty feedBackReuseOurServices;
        private SimpleBooleanProperty feedBackRecommendOurServices;
        private SimpleStringProperty feedBackSampleDeliverySpeed;
        private SimpleStringProperty feedBackSubmission;
        private SimpleStringProperty feedBackReportsQuality;
        private SimpleStringProperty feedBackAnalyzeSpeed;
        private SimpleStringProperty feedBackOnlineServices;

        public JeproLabFeedBackRecord(JeproLabFeedModel.JeproLabFeedBackModel feedback){
            feedBackIndex = new SimpleIntegerProperty(feedback.feedback_id);
            feedBackCustomer = new SimpleStringProperty(feedback.customer_name);
            feedBackEnjoyWorkingWithUs = new SimpleStringProperty(feedback.enjoy_working_with_us);
            feedBackStaffCourtesy = new SimpleStringProperty(feedback.staff_courtesy);
            feedBackTeamAbilities = new SimpleStringProperty(feedback.team_abilities);
            feedBackTeamAvailability = new SimpleStringProperty(feedback.team_availability);
            feedBackProblemSupport = new SimpleStringProperty(feedback.problem_support);
            feedBackReuseOurServices = new SimpleBooleanProperty(feedback.reuse_our_services);
            feedBackRecommendOurServices = new SimpleBooleanProperty(feedback.recommend_our_services);
            feedBackSampleDeliverySpeed = new SimpleStringProperty(feedback.sample_delivery_speed);
            feedBackSubmission = new SimpleStringProperty(feedback.submission);
            feedBackReportsQuality = new SimpleStringProperty(feedback.reports_quality );
            feedBackAnalyzeSpeed = new SimpleStringProperty(feedback.analyze_speed);
            feedBackOnlineServices = new SimpleStringProperty(feedback.online_services);
        }

        public int getFeedBackIndex(){
            return feedBackIndex.get();
        }

        public String getFeedBackCustomer() {
            return feedBackCustomer.get();
        }

        public String getFeedBackEnjoyWorkingWithUs(){
            return feedBackEnjoyWorkingWithUs.get();
        }

        public String getFeedBackStaffCourtesy(){ return feedBackStaffCourtesy.get(); }

        public String getFeedBackTeamAbilities(){ return feedBackTeamAbilities.get(); }

        public String getFeedBackTeamAvailability(){ return feedBackTeamAvailability.get(); }

        public String getFeedBackProblemSupport(){ return feedBackProblemSupport.get(); }

        public boolean getFeedBackReuseOurServices(){ return feedBackReuseOurServices.get(); }

        public boolean getFeedBackRecommendOurServices(){ return feedBackRecommendOurServices.get(); }

        public String getFeedBackSampleDeliverySpeed(){ return feedBackSampleDeliverySpeed.get(); }

        public String getFeedBackSubmission(){ return feedBackSubmission.get(); }

        public String getFeedBackReportsQuality(){ return feedBackReportsQuality.get(); }

        public String getFeedBackAnalyzeSpeed(){ return feedBackAnalyzeSpeed.get(); }

        public String getFeedBackOnlineServices(){ return feedBackOnlineServices.get(); }
    }

    public static class JeproLabFeedBackCheckBoxCellFactory extends TableCell<JeproLabFeedBackRecord, Boolean> {
        private CheckBox checkFeedBack;

        public JeproLabFeedBackCheckBoxCellFactory(){
            checkFeedBack = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item);}

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(checkFeedBack);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabFeedBackWorkingWithUsCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {
        private Button enjoyWorkingWithUsBtn;

        public JeproLabFeedBackWorkingWithUsCellFactory(){
            enjoyWorkingWithUsBtn = new Button();
            enjoyWorkingWithUsBtn.setMinSize(btnSize, btnSize);
            enjoyWorkingWithUsBtn.setMaxSize(btnSize, btnSize);
            enjoyWorkingWithUsBtn.setPrefSize(btnSize, btnSize);
            enjoyWorkingWithUsBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getFeedBackEnjoyWorkingWithUs().equals("satisfy")) {
                    enjoyWorkingWithUsBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    enjoyWorkingWithUsBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }
                setGraphic(enjoyWorkingWithUsBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabFeedBackCourtesyCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackCourtesyCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackTeamAvailableCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackTeamAvailableCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackTeamAbilitiesCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackTeamAbilitiesCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackProductSupportCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackProductSupportCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackRecommendOurServicesCellFactory extends TableCell<JeproLabFeedBackRecord, Boolean> {

        public JeproLabFeedBackRecommendOurServicesCellFactory(){

        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item);}

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackReuseOurServicesCellFactory extends TableCell<JeproLabFeedBackRecord, Boolean> {

        public JeproLabFeedBackReuseOurServicesCellFactory(){

        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item);}

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackServicesSpeedCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackServicesSpeedCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackSampleDeliveryCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackSampleDeliveryCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackSubmissionCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackSubmissionCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackReportQualityCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackReportQualityCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackAnalyzeSpeedCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackAnalyzeSpeedCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }


    public static class JeproLabFeedBackOnlineServicesCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackOnlineServicesCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }

    public static class JeproLabFeedBackGlobalQualityCellFactory extends TableCell<JeproLabFeedBackRecord, Button> {

        public JeproLabFeedBackGlobalQualityCellFactory(){

        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabFeedBackRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){

            }
        }
    }
}
