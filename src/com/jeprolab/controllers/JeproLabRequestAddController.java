package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproPhoneField;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabPopupTools;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabRequestAddController extends JeproLabController{
    private JeproLabRequestModel request;
    private JeproLabRequestModel.JeproLabSampleModel sample;
    private Map<Integer, String> sample_matrix = new HashMap<>();
    private Map<Integer, String> sampleConditions = new HashMap<>();
    private Map<Integer, String> contactList = new HashMap<>();
    private Map<Integer, String> requestStatus = new HashMap<>();
    private Button saveRequestBtn, cancelBtn, printCertificateBtn;
    private double fourthColumnWidth = 280;
    private final double subFormWidth = 0.96 * JeproLab.APP_WIDTH;
    private int firstContactId, secondContactId, thirdContactId, fourthContactId;
    private TableView<JeproLabSampleRecord> jeproLabSampleRecordTableView;
    private HBox jeproLabRequestSamplesSearchWrapper;
    private TextField jeproLabRequestSamplesSearchField;
    private ComboBox<String> jeproLabRequestSamplesSearchFilter;
    private Button jeproLabRequestSamplesSearchButton;
    private  ObservableList<JeproLabSampleRecord> sampleList;
    private final int displayedItems = JeproLabConfigurationSettings.LIST_LIMIT - 6;

    @FXML
    public JeproFormPanel jeproLabRequestFormWrapper;
    public JeproFormPanelTitle jeproLabRequestFormTitleWrapper;
    public JeproFormPanelContainer jeproLabRequestContentWrapper;
    public Label jeproLabCustomerCompanyNameLabel, jeproLabResultTransmissionLabel, jeproLabRequestFirstContactLabel, jeproLabRequestSecondContactLabel;
    public Label jeproLabRequestThirdContactLabel, jeproLabRequestFourthContactLabel, jeproLabSampleReferenceLabel, jeproLabSampleBundleLabel;
    public Label jeproLabSampleDesignationLabel, jeproLabSampleMatrixLabel, jeproLabSampleRemovalDateLabel, jeproLabSampleAnalyzeSelectorLabel;
    public Label jeproLabCustomerCompanyAddressLabel, jeproLabCustomerCompanyPhoneLabel, jeproLabCustomerCompanyFaxLabel, jeproLabRequestMainContactInfoLabel;
    public Label jeproLabRequestReferenceLabel, jeproLabRequestDelayLabel, jeproLabRequestMainContactInfoNameLabel, jeproLabRequestMainContactInfoMailLabel;
    public Label jeproLabRequestStatusLabel, jeproLabSampleConditionLabel;
    public ComboBox<String> jeproLabRequestFirstContact, jeproLabRequestSecondContact, jeproLabRequestThirdContact, jeproLabRequestFourthContact, jeproLabRequestDelay;
    public TextField jeproLabCustomerCompanyName, jeproLabSampleDesignation, jeproLabCustomerCompanyAddress, jeproLabCustomerCompanyAddressDetails;
    public TextField jeproLabRequestReference, jeproLabRequestMainContactInfoMail, jeproLabRequestMainContactInfoName, jeproLabSampleBundle;
    public TextField jeproLabSampleReference;
    public JeproPhoneField jeproLabCustomerCompanyPhone, jeproLabCustomerCompanyFax;
    public ComboBox<String> jeproLabSampleMatrix, jeproLabRequestStatus, jeproLabSampleCondition;
    public DatePicker jeproLabSampleRemovalDate;
    public GridPane jeproLabSampleAnalyzeSelector, jeproLabCustomerInformationLayout, jeproLabCustomerContactLayout, jeproLabSampleAddFormLayout;
    public HBox jeproLabCustomerCompanyPhoneWrapper, jeproLabRequestStatusWrapper;
    public Pane jeproLabCustomerInformationWrapper, jeproLabCustomerInformationTitleWrapper, jeproLabCustomerInformationContentWrapper;
    public Pane jeproLabRequestMainContactInfo, jeproLabSampleAddFormTitleWrapper, jeproLabSampleFormWrapper;
    public VBox jeproLabRequestSamplesList;
    public ScrollPane jeproLabSampleAnalyzeSelectorPaneWrapper;
    public Button jeproLabSaveSampleBtn, jeproLabCancelSampleBtn;
    public TabPane jeproLabRequestTabPane;
    public Tab jeproLabRequestInformationTab, jeproLabRequestDocumentTab, jeproLabRequestSampleTab;


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;

        jeproLabRequestFormWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabRequestFormWrapper.setLayoutY(10);
        jeproLabRequestFormWrapper.setPrefSize(formWidth, (rowHeight * (JeproLabConfigurationSettings.LIST_LIMIT )) + 40);
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_REQUEST_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        jeproLabRequestFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabRequestFormTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabRequestContentWrapper.setPrefSize(formWidth, 630);
        jeproLabRequestContentWrapper.setMinSize(formWidth, 630);
        jeproLabRequestContentWrapper.setMaxSize(formWidth, 630);
        jeproLabRequestContentWrapper.setLayoutY(40);

        VBox.setMargin(jeproLabCustomerInformationWrapper, new Insets(10, 0, 10, 0.01 * JeproLab.APP_WIDTH));

        jeproLabCustomerInformationWrapper.setPrefWidth(subFormWidth);
        jeproLabCustomerInformationWrapper.setLayoutX(0.02 * JeproLab.APP_WIDTH);

        jeproLabCustomerInformationTitleWrapper.setPrefSize(subFormWidth, 30);
        jeproLabCustomerInformationTitleWrapper.getStyleClass().add("form-panel-title-gray");

        jeproLabRequestMainContactInfo.getStyleClass().add("form-panel-title-gray");
        jeproLabRequestMainContactInfo.setPrefSize(500, 30);

        jeproLabCustomerInformationContentWrapper.setPrefWidth(subFormWidth);
        jeproLabCustomerInformationContentWrapper.setLayoutY(30);

        jeproLabRequestFirstContact.setPrefWidth(fourthColumnWidth);
        jeproLabRequestSecondContact.setPrefWidth(fourthColumnWidth);
        jeproLabRequestThirdContact.setPrefWidth(fourthColumnWidth);
        jeproLabRequestFourthContact.setPrefWidth(fourthColumnWidth);

        jeproLabSampleFormWrapper.setPrefSize(subFormWidth, (rowHeight * (JeproLabConfigurationSettings.LIST_LIMIT )));
        VBox.setMargin(jeproLabSampleFormWrapper, new Insets(0, 0, 10, 0.005 * JeproLab.APP_WIDTH));
        jeproLabRequestTabPane.setPrefWidth(formWidth);
        jeproLabRequestInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabRequestDocumentTab.setText(bundle.getString("JEPROLAB_DOCUMENTS_LABEL"));

        renderCompanyForm();
        renderContactForm();
        renderSampleForm();
        renderSampleListForm();
        setFormLabels();
    }

    private void renderCompanyForm(){
        jeproLabCustomerInformationLayout.getColumnConstraints().addAll(
            new ColumnConstraints(130), new ColumnConstraints(370)
        );
        jeproLabCustomerCompanyFax.setPrefWidth(150);
        jeproLabCustomerCompanyPhone.setPrefWidth(150);

        GridPane.setMargin(jeproLabCustomerCompanyNameLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabCustomerCompanyName, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabCustomerCompanyAddressLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabCustomerCompanyAddress, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabCustomerCompanyAddressDetails, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabCustomerCompanyPhoneLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabCustomerCompanyPhoneWrapper, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestMainContactInfo, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestMainContactInfoNameLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestMainContactInfoName, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestMainContactInfoMailLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestMainContactInfoMail, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestReferenceLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestReference, new Insets(5, 0, 5, 0));
        HBox.setMargin(jeproLabCustomerCompanyFaxLabel, new Insets(5, 0, 0, 10));
        HBox.setMargin(jeproLabCustomerCompanyFax, new Insets(0, 0, 0, 10));

        jeproLabRequestReference.setDisable(true);
    }

    private void renderContactForm(){
        jeproLabCustomerContactLayout.getColumnConstraints().addAll(
            new ColumnConstraints(30), new ColumnConstraints(fourthColumnWidth)
        );

        GridPane.setMargin(jeproLabResultTransmissionLabel, new Insets(7, 0, 8, 10));
        GridPane.setMargin(jeproLabRequestFirstContactLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestFirstContact, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestSecondContactLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestSecondContact, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestThirdContactLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestThirdContact, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestFourthContactLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabRequestFourthContact, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestDelayLabel, new Insets(10, 0, 9, 10));
        GridPane.setMargin(jeproLabRequestStatusLabel, new Insets(15, 0, 9, 0));
        GridPane.setMargin(jeproLabRequestStatus, new Insets(15, 0, 9, 0));
        GridPane.setMargin(jeproLabRequestDelay, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabRequestStatusWrapper, new Insets(15, 0, 5, 0));

        jeproLabRequestFirstContact.setPromptText(bundle.getString("JEPROLAB_SELECT_FIRST_CONTACT_LABEL"));
        jeproLabRequestSecondContact.setPromptText(bundle.getString("JEPROLAB_SELECT_SECOND_CONTACT_LABEL"));
        jeproLabRequestThirdContact.setPromptText(bundle.getString("JEPROLAB_SELECT_THIRD_CONTACT_LABEL"));
        jeproLabRequestFourthContact.setPromptText(bundle.getString("JEPROLAB_SELECT_FOURTH_CONTACT_LABEL"));

        jeproLabResultTransmissionLabel.setPrefWidth(subFormWidth - 520);
        jeproLabResultTransmissionLabel.setAlignment(Pos.CENTER);

        jeproLabRequestDelayLabel.setPrefWidth(subFormWidth - 520);
        jeproLabRequestDelayLabel.setAlignment(Pos.CENTER);

        jeproLabRequestDelay.setPrefWidth(fourthColumnWidth);
        jeproLabRequestDelay.getItems().addAll(
            bundle.getString("JEPROLAB_ANALYZE_REGULAR_DELAY_LABEL"),
            bundle.getString("JEPROLAB_ANALYZE_EXPRESS_DELAY_12_HOURS_LABEL"),
            bundle.getString("JEPROLAB_ANALYZE_EXPRESS_DELAY_24_HOURS_LABEL"),
            bundle.getString("JEPROLAB_ANALYZE_EXPRESS_DELAY_48_HOURS_LABEL")
        );
        jeproLabRequestDelay.setValue(bundle.getString("JEPROLAB_ANALYZE_REGULAR_DELAY_LABEL"));

        jeproLabRequestStatusLabel.setText(bundle.getString("JEPROLAB_REQUEST_STATUS_LABEL"));
        jeproLabRequestStatus.setPromptText(bundle.getString("JEPROLAB_SELECT_STATUS_LABEL"));
        requestStatus = JeproLabRequestModel.JeproLabRequestStatusModel.getRequestStatues();
        for(Map.Entry<Integer, String> entry : requestStatus.entrySet()){
            jeproLabRequestStatus.getItems().add(entry.getValue());
        }
    }

    private void renderSampleForm(){
        Label mainFormLabel = new Label(bundle.getString("JEPROLAB_CUSTOMER_INFORMATION_LABEL"));
        Label mainContactFormLabel = new Label(bundle.getString("JEPROLAB_MAIN_CONTACT_INFO_LABEL"));
        Label sampleFormLabel = new Label(bundle.getString("JEPROLAB_SAMPLE_IDENTIFICATION_LABEL"));
        sample = new JeproLabRequestModel.JeproLabSampleModel();

        mainFormLabel.setPrefWidth(subFormWidth);
        mainFormLabel.setAlignment(Pos.CENTER);
        mainFormLabel.getStyleClass().add("input-label");
        mainFormLabel.setLayoutY(4);
        mainContactFormLabel.setPrefWidth(500);
        mainContactFormLabel.setAlignment(Pos.CENTER);
        mainContactFormLabel.getStyleClass().add("input-label");
        mainContactFormLabel.setLayoutY(4);
        sampleFormLabel.setPrefWidth(subFormWidth);
        sampleFormLabel.setAlignment(Pos.CENTER);
        sampleFormLabel.getStyleClass().add("input-label");
        sampleFormLabel.setLayoutY(4);

        jeproLabCustomerInformationTitleWrapper.getChildren().addAll(mainFormLabel);
        jeproLabRequestMainContactInfo.getChildren().addAll(mainContactFormLabel);
        jeproLabSampleAddFormTitleWrapper.getChildren().addAll(sampleFormLabel);

        jeproLabSampleAnalyzeSelectorPaneWrapper.setPrefSize(490, 270);

        jeproLabSampleAddFormLayout.getColumnConstraints().addAll(new ColumnConstraints(140), new ColumnConstraints(240));
        jeproLabSampleAddFormTitleWrapper.setPrefSize(formWidth, 30);
        jeproLabSampleAddFormTitleWrapper.getStyleClass().add("form-panel-title-gray");
        VBox.setMargin(jeproLabSampleAddFormTitleWrapper, new Insets(5, 0, 0, 0));

        GridPane.setMargin(jeproLabSampleReferenceLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleReference, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleDesignationLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleDesignation, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleBundleLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleBundle, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleMatrixLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleMatrix, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleRemovalDateLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleRemovalDate, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorPaneWrapper, new Insets(5, 0, 15, 5));
        GridPane.setMargin(jeproLabSampleConditionLabel, new Insets(5, 0, 10, 10));
        jeproLabSampleAnalyzeSelectorLabel.setPrefWidth(380);
        jeproLabSampleAnalyzeSelectorLabel.setAlignment(Pos.CENTER);
        jeproLabSampleReference.setDisable(true);

        jeproLabSampleConditionLabel.setText(bundle.getString("JEPROLAB_RECEIVED_CONDITION_LABEL"));
        jeproLabSampleCondition.setPromptText(bundle.getString("JEPROLAB_SELECT_SAMPLE_CONDITION_LABEL"));
        sampleConditions = JeproLabRequestModel.JeproLabSampleReceptionConditionModel.getConditions(JeproLabContext.getContext().language.language_id);
        for(Map.Entry<Integer, String> condition : sampleConditions.entrySet()) {
            jeproLabSampleCondition.getItems().add(condition.getValue());
        }
        sample_matrix = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        jeproLabSampleMatrix.setPromptText(bundle.getString("JEPROLAB_SELECT_MATRIX_LABEL"));
        for(Map.Entry<Integer, String> matrix : sample_matrix.entrySet()){
            jeproLabSampleMatrix.getItems().add(matrix.getValue());
        }

        List<JeproLabAnalyzeModel> analyzesList = JeproLabAnalyzeModel.getAnalyzeList();
        jeproLabSampleAnalyzeSelector.getColumnConstraints().addAll(
            new ColumnConstraints(185),  new ColumnConstraints(185)
        );
        int analyzeIndex = 0;
        int analyzeRow = 0;
        for(JeproLabAnalyzeModel analyze : analyzesList){
            CheckBox analyzeCheck = new CheckBox(analyze.name.get("lang_" + context.language.language_id));
            jeproLabSampleAnalyzeSelector.add(analyzeCheck, analyzeIndex % 2, analyzeRow);
            GridPane.setMargin(analyzeCheck, new Insets(4,2,4,4));
            analyzeIndex++;
            if((analyzeIndex % 2) == 0){ analyzeRow++; }
            analyzeCheck.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    sample.addAnalyze(analyze.analyze_id);
                } else {
                    sample.removeAnalyze(analyze.analyze_id);
                }
            }));
        }

        if(sample != null && sample.sample_id > 0) {
            jeproLabSaveSampleBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        }else{
            jeproLabSaveSampleBtn.setText(bundle.getString("JEPROLAB_ADD_LABEL"));
            jeproLabSaveSampleBtn.setDisable(true);
        }
        jeproLabSaveSampleBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        jeproLabCancelSampleBtn.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
        jeproLabCancelSampleBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));

    }

    private void renderSampleListForm(){
        CheckBox checkAll = new CheckBox();
        jeproLabSampleRecordTableView = new TableView<>();
        jeproLabSampleRecordTableView.setPrefSize((formWidth - 420), rowHeight *(displayedItems));
        double remainingWidth = formWidth - 522;

        jeproLabRequestSamplesSearchWrapper = new HBox();

        jeproLabRequestSamplesSearchField = new TextField();
        jeproLabRequestSamplesSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabRequestSamplesSearchFilter = new ComboBox<>();
        jeproLabRequestSamplesSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabRequestSamplesSearchButton = new Button();
        jeproLabRequestSamplesSearchButton.getStyleClass().addAll("icon-btn", "search-btn");

        HBox.setMargin(jeproLabRequestSamplesSearchField, new Insets(5, 10, 5, 0));
        HBox.setMargin(jeproLabRequestSamplesSearchFilter, new Insets(5, 10, 5, 0));
        HBox.setMargin(jeproLabRequestSamplesSearchButton, new Insets(5, 10, 5, 0));

        jeproLabRequestSamplesSearchWrapper.getChildren().addAll(jeproLabRequestSamplesSearchField, jeproLabRequestSamplesSearchFilter, jeproLabRequestSamplesSearchButton);

        jeproLabRequestSampleTab.setText(bundle.getString("JEPROLAB_SAMPLES_LABEL"));


        TableColumn<JeproLabSampleRecord, Boolean> jeproLabSampleCheckBoxColumn = new TableColumn<>();
        jeproLabSampleCheckBoxColumn.setGraphic(checkAll);
        jeproLabSampleCheckBoxColumn.setPrefWidth(30);
        Callback<TableColumn<JeproLabSampleRecord, Boolean>, TableCell<JeproLabSampleRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCheckBoxCell();
        jeproLabSampleCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabSampleRecord, String> jeproLabSampleReferenceColumn = new TableColumn<>(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabSampleReferenceColumn.setPrefWidth(0.30 * remainingWidth);
        jeproLabSampleReferenceColumn.setCellValueFactory(new PropertyValueFactory<>("sampleReference"));

        TableColumn<JeproLabSampleRecord, String> jeproLabSampleDesignationColumn = new TableColumn<>(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleDesignationColumn.setPrefWidth(0.7 * remainingWidth);
        jeproLabSampleDesignationColumn.setCellValueFactory(new PropertyValueFactory<>("sampleDesignation"));

        TableColumn<JeproLabSampleRecord, HBox> jeproLabSampleActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabSampleActionColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabSampleRecord, HBox>, TableCell<JeproLabSampleRecord, HBox>> sampleActionFactory = param -> new JeproLabSampleActionCell();
        jeproLabSampleActionColumn.setCellFactory(sampleActionFactory);

        jeproLabSampleRecordTableView.getColumns().addAll(
            jeproLabSampleCheckBoxColumn, jeproLabSampleReferenceColumn,
            jeproLabSampleDesignationColumn, jeproLabSampleActionColumn
        );
    }

    private void setFormLabels(){
        /** Label setting **/
        jeproLabCustomerCompanyNameLabel.setText(bundle.getString("JEPROLAB_COMPANY_NAME_LABEL"));
        jeproLabCustomerCompanyNameLabel.getStyleClass().add("input-label");
        jeproLabResultTransmissionLabel.setText(bundle.getString("JEPROLAB_RESULT_TRANSMISSION_LABEL"));
        jeproLabResultTransmissionLabel.getStyleClass().add("input-label");
        jeproLabRequestFirstContactLabel.setText("1 ");
        jeproLabRequestFirstContactLabel.getStyleClass().add("input-label");
        jeproLabRequestSecondContactLabel.setText("2 ");
        jeproLabRequestSecondContactLabel.getStyleClass().add("input-label");
        jeproLabRequestThirdContactLabel.setText("3 ");
        jeproLabRequestThirdContactLabel.getStyleClass().add("input-label");
        jeproLabRequestFourthContactLabel.setText("4 ");
        jeproLabRequestFourthContactLabel.getStyleClass().add("input-label");
        jeproLabSampleReferenceLabel.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabSampleReferenceLabel.getStyleClass().add("input-label");
        jeproLabSampleBundleLabel.setText(bundle.getString("JEPROLAB_SAMPLE_BUNDLE_LABEL"));
        jeproLabSampleBundleLabel.getStyleClass().add("input-label");
        jeproLabSampleDesignationLabel.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleDesignationLabel.getStyleClass().add("input-label");
        jeproLabSampleMatrixLabel.setText(bundle.getString("JEPROLAB_MATRIX_LABEL"));
        jeproLabSampleMatrixLabel.getStyleClass().add("input-label");
        jeproLabSampleRemovalDateLabel.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleRemovalDateLabel.getStyleClass().add("input-label");
        jeproLabSampleAnalyzeSelectorLabel.setText(bundle.getString("JEPROLAB_SELECT_ANALYZES_LABEL"));
        jeproLabSampleAnalyzeSelectorLabel.getStyleClass().add("input-label");
        jeproLabRequestReferenceLabel.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabRequestReferenceLabel.getStyleClass().add("input-label");
        jeproLabRequestDelayLabel.setText(bundle.getString("JEPROLAB_REQUEST_DELAY_LABEL"));
        jeproLabRequestDelayLabel.getStyleClass().add("input-label");
        jeproLabCustomerCompanyPhoneLabel.setText(bundle.getString("JEPROLAB_PHONE_LABEL"));
        jeproLabCustomerCompanyPhoneLabel.getStyleClass().add("input-label");
        jeproLabCustomerCompanyFaxLabel.setText(bundle.getString("JEPROLAB_MOBILE_LABEL"));
        jeproLabCustomerCompanyFaxLabel.getStyleClass().add("input-label");
        jeproLabCustomerCompanyAddressLabel.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        jeproLabCustomerCompanyAddressLabel.getStyleClass().add("input-label");
        jeproLabRequestMainContactInfoNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabRequestMainContactInfoNameLabel.getStyleClass().add("input-label");
        jeproLabRequestMainContactInfoMailLabel.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        jeproLabRequestMainContactInfoMailLabel.getStyleClass().add("input-label");
    }

    @Override
    public void initializeContent(){
        initializeContent(0);
    }

    @Override
    public void initializeContent(int requestId) {
        if(requestId > 0) {
            Worker<Boolean> worker = new Task<Boolean>() {
                List<JeproLabCustomerModel> customersContact;
                JeproLabCustomerModel customer;
                JeproLabAddressModel address;
                List<JeproLabRequestModel.JeproLabSampleModel> samples;

                @Override
                protected Boolean call() throws Exception {
                    loadRequest(requestId);
                    if (request.request_id > 0 && request.request_id == requestId) {
                        customer = new JeproLabCustomerModel(request.customer_id);
                        address = JeproLabAddressModel.getAddressByCustomerId(request.customer_id, true);
                        customersContact = JeproLabCustomerModel.getCustomersByCompany(customer.company);
                        samples = JeproLabRequestModel.JeproLabSampleModel.getSamplesByRequestId(request.request_id);
                        return true;
                    }
                    return false;
                }

                @Override
                protected void failed() {
                    super.failed();
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    updateRequestData(customer, address, customersContact, samples);
                }

            };
            new Thread((Task) worker).start();
        }else{
            clearRequestForm();
        }
        this.updateToolBar();

        if(!context.employee.isSuperAdmin()){
            printCertificateBtn.setDisable(true);
        }
    }

    private void updateRequestData(JeproLabCustomerModel customer,  JeproLabAddressModel address, List<JeproLabCustomerModel> customersContact, List<JeproLabRequestModel.JeproLabSampleModel> samples) {
        sampleList = FXCollections.observableArrayList();
        sampleList.addAll(samples.stream().map(JeproLabSampleRecord::new).collect(Collectors.toList()));
        if (request.request_id > 0) {
            Platform.runLater(() -> {
                clearRequestForm();
                jeproLabCustomerCompanyName.setText(customer.company);

                jeproLabCustomerCompanyAddress.setText(address.address1);
                jeproLabCustomerCompanyAddressDetails.setText(address.address2);
                jeproLabRequestReference.setText(request.reference);
                jeproLabRequestMainContactInfoMail.setText(customer.email);
                jeproLabRequestMainContactInfoName.setText(customer.firstname + " " + customer.lastname);

                updateContacts(customersContact);
                jeproLabRequestDocumentTab.setDisable(false);
                jeproLabRequestSampleTab.setDisable(false);

                if (request.request_id > 0) {
                    saveRequestBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
                } else {
                    saveRequestBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
                }

                if (!samples.isEmpty()) {
                    double padding = 0.01 * JeproLab.APP_WIDTH;
                    Pagination requestSamplePagination = new Pagination((sampleList.size()/ displayedItems) + 1, 0);
                    requestSamplePagination.setPageFactory(this::createSamplePages);

                    VBox.setMargin(jeproLabRequestSamplesSearchWrapper, new Insets(5, padding, 5, padding));
                    VBox.setMargin(requestSamplePagination, new Insets(5, padding, 5, padding));

                    jeproLabRequestSamplesList.getChildren().clear();
                    jeproLabRequestSamplesList.getChildren().addAll(jeproLabRequestSamplesSearchWrapper, requestSamplePagination);
                }
            });
        }
    }

    private Node createSamplePages(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (sampleList.size()));
        jeproLabSampleRecordTableView.setItems(FXCollections.observableArrayList(sampleList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabSampleRecordTableView);
    }

    private void clearRequestForm(){
        jeproLabRequestReference.setText(JeproLabTools.createRequestReference());
        jeproLabRequestDocumentTab.setDisable(true);
        jeproLabRequestSampleTab.setDisable(true);
        jeproLabCustomerCompanyAddress.clear();
        jeproLabCustomerCompanyName.clear();
        jeproLabCustomerCompanyAddressDetails.clear();

        jeproLabRequestMainContactInfoMail.clear();
        jeproLabRequestMainContactInfoName.clear();
        updateContacts(null);
        jeproLabSampleRecordTableView.getItems().clear();

        jeproLabSampleReference.clear();
        jeproLabSampleDesignation.clear();
        jeproLabSampleMatrix.setValue(jeproLabSampleMatrix.getPromptText());
        //jeproLabSampleTestDate.setValue(LocalDate.now(new C));

        for(Node item : jeproLabSampleAnalyzeSelector.getChildren()){
            ((CheckBox)item).setSelected(false);
        }

    }

    private void loadRequest(int requestId){
        if(context == null) {
            context = JeproLabContext.getContext();
        }

        if(requestId > 0){
            request = new JeproLabRequestModel(requestId);
            if(request.request_id <= 0){
                request = new JeproLabRequestModel();
            }
        }else{
            request = new  JeproLabRequestModel();
        }
    }

    private void addEventListeners() {
        addRequestFormListeners();
        addSampleFormListeners();
    }

    private void addRequestFormListeners(){
        jeproLabCustomerCompanyName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                String companyName = jeproLabCustomerCompanyName.getText();
                List<JeproLabCustomerModel> customers = JeproLabCustomerModel.getCustomersByCompany(companyName);
                updateContacts(customers);
            }
        });

        jeproLabRequestFirstContact.valueProperty().addListener((observable, oldValue, newValue) -> {
            firstContactId = getContactId(newValue);
        });

        jeproLabRequestSecondContact.valueProperty().addListener((observable, oldValue, newValue) -> {
            secondContactId = getContactId(newValue);
        });

        jeproLabRequestThirdContact.valueProperty().addListener((observable, oldValue, newValue) -> {
            thirdContactId = getContactId(newValue);
        });

        jeproLabRequestFourthContact.valueProperty().addListener((observable, oldValue, newValue) -> {
            fourthContactId = getContactId(newValue);
        });

        jeproLabRequestDelay.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(bundle.getString("JEPROLAB_ANALYSE_REGULAR_DELAY_LABEL"))) {
                request.delay = 100;
            }else if(newValue.equals(bundle.getString("JEPROLAB_ANALYSE_EXPRESS_DELAY_12_HOURS_LABEL"))) {
                request.delay = 12;
            }else if(newValue.equals(bundle.getString("JEPROLAB_ANALYSE_EXPRESS_DELAY_24_HOURS_LABEL"))) {
                request.delay = 24;
            }else if(newValue.equals(bundle.getString("JEPROLAB_ANALYSE_EXPRESS_DELAY_48_HOURS_LABEL"))) {
                request.delay = 24;
            }
        });
    }

    private void addSampleFormListeners(){
        jeproLabSampleMatrix.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(sample == null){ sample = new JeproLabRequestModel.JeproLabSampleModel(); }
            sample_matrix.entrySet().stream().filter(entry -> entry.getValue().equals(newValue)).forEach(entry ->
                sample.matrix_id = entry.getKey()
            );
        });

        jeproLabSampleDesignation.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("") && oldValue.equals("")){
                String prefix = jeproLabRequestReference.getText();
                prefix = prefix.substring(prefix.length() - 3, prefix.length() - 1);
                jeproLabSampleReference.setText(JeproLabTools.generateSampleReference(prefix));
                sample.reference = jeproLabSampleReference.getText();
            }

            if(!newValue.equals("")){
                jeproLabSaveSampleBtn.setDisable(false);
            }else{
                jeproLabSaveSampleBtn.setDisable(true);
            }
            sample.designation = newValue;
        });

        jeproLabSampleCondition.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(sample == null){ sample = new JeproLabRequestModel.JeproLabSampleModel(); }

        });

        jeproLabSampleRemovalDate.setOnAction(evt -> {
            sample.removal_date = JeproLabTools.getDate(jeproLabSampleRemovalDate.getValue());
        });

        jeproLabSaveSampleBtn.setOnAction(evt -> {
            if(jeproLabSampleDesignation.getText().isEmpty()){
                //JeproLabPopupTools.displayMessage();
            }
            sample.add();

            if (sample.sample_id > 0){
                jeproLabSampleRecordTableView.getItems().add(new JeproLabSampleRecord(sample));
                clearSampleForm();
            }
        });

        jeproLabRequestStatus.valueProperty().addListener(((observable, oldValue, newValue) -> {
            requestStatus.entrySet().stream().filter(entry -> newValue.equals(entry.getValue())).forEach(entry -> {
                request.status_id = entry.getKey();
            });
        }));

        saveRequestBtn.setOnAction(evt -> {
            JeproLabCustomerModel customer = new JeproLabCustomerModel(request.customer_id);
            customer.company = jeproLabCustomerCompanyName.getText();
            String customerName = jeproLabRequestMainContactInfoName.getText();
            int separatorIndex = customerName.indexOf(" ");
            customer.firstname = customerName.substring(0, separatorIndex);
            customer.lastname = customerName.substring(separatorIndex + 1, customerName.length());
            customer.email = jeproLabRequestMainContactInfoMail.getText();
            customer.laboratory_id = context.laboratory.laboratory_id;
            customer.laboratory_group_id = context.laboratory.laboratory_group_id;
            customer.published = true;
            if (customer.customer_id > 0) {
                customer.update();
            } else {
                customer.add();
            }

            JeproLabAddressModel address = JeproLabAddressModel.getAddressByCustomerId(customer.customer_id, true);
            address.customer_id = customer.customer_id;
            address.address1 = jeproLabCustomerCompanyAddress.getText();
            address.address2 = jeproLabCustomerCompanyAddressDetails.getText();
            address.phone = jeproLabCustomerCompanyPhone.getText();
            address.mobile_phone = jeproLabCustomerCompanyFax.getText();
            if (address.address_id > 0) {
                address.update();
            } else {
                address.add();
            }
            request.first_contact_id = firstContactId;
            request.second_contact_id = secondContactId;
            request.third_contact_id = thirdContactId;
            request.fourth_contact_id = fourthContactId;

            request.customer_id = customer.customer_id;


            request.samples.addAll(jeproLabSampleRecordTableView.getItems().stream().map(JeproLabSampleRecord::getSampleIndex).collect(Collectors.toList()));

            if (request.request_id > 0) {
                request.update();
            } else {
                request.reference = jeproLabRequestReference.getText();
                request.add();
            }
        });

        printCertificateBtn.setOnAction(event -> {
            if(request.request_id > 0){
                //JeproLabDocument.createCertificateDocument(request.request_id);
            }
        });

        cancelBtn.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestForm);
            JeproLab.getInstance().getApplicationForms().requestForm.controller.initializeContent();
        });
    }

    private void clearSampleForm(){
        sample = new JeproLabRequestModel.JeproLabSampleModel();
        jeproLabSampleReference.setText("");
        jeproLabSampleDesignation.setText("");
        jeproLabSampleMatrix.setValue(jeproLabSampleMatrix.getPromptText());
        //jeproLabSampleTestDate.
        for(Node analyzeCheckBox :jeproLabSampleAnalyzeSelector.getChildren()){
            ((CheckBox)analyzeCheckBox).setSelected(false);
        }
        jeproLabSaveSampleBtn.setDisable(true);
    }

    private int getContactId(String value){
        for(Map.Entry<Integer, String> entry : contactList.entrySet()){
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return 0;
    }

    private void updateContacts(List<JeproLabCustomerModel> customers){
        jeproLabRequestFirstContact.getItems().clear();
        jeproLabRequestSecondContact.getItems().clear();
        jeproLabRequestThirdContact.getItems().clear();
        jeproLabRequestFourthContact.getItems().clear();
        if(customers != null){
            firstContactId = request.first_contact_id;
            secondContactId = request.second_contact_id;
            thirdContactId = request.third_contact_id;
            fourthContactId = request.fourth_contact_id;
            contactList.clear();
            for(JeproLabCustomerModel customer : customers) {
                contactList.put(customer.customer_id, customer.firstname + " " + customer.lastname.toUpperCase());
                jeproLabRequestFirstContact.getItems().add(customer.firstname + " " + customer.lastname.toUpperCase());
                jeproLabRequestSecondContact.getItems().add(customer.firstname + " " + customer.lastname.toUpperCase());
                jeproLabRequestThirdContact.getItems().add(customer.firstname + " " + customer.lastname.toUpperCase());
                jeproLabRequestFourthContact.getItems().add(customer.firstname + " " + customer.lastname.toUpperCase());

                if(request.first_contact_id == customer.customer_id) {
                    jeproLabRequestFirstContact.setValue(customer.firstname + " " + customer.lastname.toUpperCase());
                }else if(request.second_contact_id == customer.customer_id){
                    jeproLabRequestSecondContact.setValue(customer.firstname + " " + customer.lastname.toUpperCase());
                }else if(request.third_contact_id == customer.customer_id){
                    jeproLabRequestThirdContact.setValue(customer.firstname + " " + customer.lastname.toUpperCase());
                }else if(request.fourth_contact_id == customer.customer_id) {
                    jeproLabRequestFourthContact.setValue(customer.firstname + " " + customer.lastname.toUpperCase());
                }
            }
        }
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveRequestBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        printCertificateBtn = new Button(bundle.getString("JEPROLAB_CERTIFICATE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/printer.png"))));
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));

        commandWrapper.getChildren().addAll(saveRequestBtn, printCertificateBtn, cancelBtn);

        this.addEventListeners();
    }

    public class JeproLabSampleRecord {
        private SimpleStringProperty sampleReference, sampleDesignation;
        private SimpleIntegerProperty sampleIndex;

        public JeproLabSampleRecord(JeproLabRequestModel.JeproLabSampleModel sample){
            sampleIndex = new SimpleIntegerProperty(sample.sample_id);
            sampleReference = new SimpleStringProperty(sample.reference);
            sampleDesignation = new SimpleStringProperty(sample.designation);
        }

        public String getSampleReference(){
            return sampleReference.get();
        }

        public String getSampleDesignation(){
            return sampleDesignation.get();
        }

        public int getSampleIndex(){
            return sampleIndex.get();
        }
    }

    private class JeproLabCheckBoxCell extends TableCell<JeproLabSampleRecord, Boolean> {
        private CheckBox checkBox;

        public JeproLabCheckBoxCell(){
            checkBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean t, boolean empty){
            super.updateItem(t, empty);
            ObservableList<JeproLabSampleRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {

                setGraphic(checkBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private class JeproLabSampleActionCell extends TableCell<JeproLabSampleRecord, HBox> {
        protected HBox commandContainer;
        private Button editSample, deleteSample, detailButton;
        private final double btnSize = 18;

        public JeproLabSampleActionCell(){
            editSample = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editSample.setMaxSize(btnSize, btnSize);
            editSample.setMinSize(btnSize, btnSize);
            editSample.setPrefSize(btnSize, btnSize);
            editSample.getStyleClass().add("icon-btn");
            detailButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/view.png"))));
            detailButton.setMaxSize(btnSize, btnSize);
            detailButton.setMinSize(btnSize, btnSize);
            detailButton.setPrefSize(btnSize, btnSize);
            detailButton.getStyleClass().add("icon-btn");
            deleteSample = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteSample.setMaxSize(btnSize, btnSize);
            deleteSample.setMinSize(btnSize, btnSize);
            deleteSample.setPrefSize(btnSize, btnSize);
            deleteSample.getStyleClass().add("icon-btn");
            commandContainer = new HBox(10);
            commandContainer.setAlignment(Pos.CENTER);
            commandContainer.getChildren().addAll(editSample, deleteSample);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabSampleRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {
                int itemId = items.get(getIndex()).getSampleIndex();
                editSample.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().sampleForm);
                    JeproLab.getInstance().getApplicationForms().sampleForm.controller.initializeContent(itemId);
                });
                setGraphic(commandContainer);
                setAlignment(Pos.CENTER);
            }
        }
    }

}
