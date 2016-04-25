package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproPhoneField;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabRequestAddController extends JeproLabController{
    private JeproLabRequestModel request;
    private JeproLabRequestModel.JeproLabSampleModel sample;
    private Map<Integer, String> sample_matrix = new HashMap<>();
    private Button saveRequestBtn, cancelBtn;
    private double fourthColumnWidth = 280;
    private final double formWidth = 0.98 * JeproLab.APP_WIDTH;
    private final double subFormWidth = 0.96 * JeproLab.APP_WIDTH;
    private int firstContactId, secondContactId, thirdContactId, fourthContactId;

    @FXML
    public JeproFormPanel jeproLabRequestFormWrapper;
    public JeproFormPanelTitle jeproLabRequestFormTitleWrapper;
    public JeproFormPanelContainer jeproLabRequestContentWrapper;
    public Label jeproLabCustomerCompanyNameLabel, jeproLabResultTransmissionLabel, jeproLabRequestFirstContactLabel, jeproLabRequestSecondContactLabel;
    public Label jeproLabRequestThirdContactLabel, jeproLabRequestFourthContactLabel, jeproLabSampleReferenceLabel;
    public Label jeproLabSampleDesignationLabel, jeproLabSampleMatrixLabel, jeproLabSampleTestDateLabel, jeproLabSampleAnalyzeSelectorLabel;
    public Label jeproLabCustomerCompanyAddressLabel, jeproLabCustomerCompanyPhoneLabel, jeproLabCustomerCompanyFaxLabel, jeproLabRequestMainContactInfoLabel;
    public Label jeproLabRequestReferenceLabel, jeproLabRequestDelayLabel, jeproLabRequestMainContactInfoNameLabel, jeproLabRequestMainContactInfoMailLabel;
    public ComboBox<String> jeproLabRequestFirstContact, jeproLabRequestSecondContact, jeproLabRequestThirdContact, jeproLabRequestFourthContact, jeproLabRequestDelay;
    public TextField jeproLabCustomerCompanyName, jeproLabSampleDesignation, jeproLabCustomerCompanyAddress, jeproLabCustomerCompanyAddressDetails;
    public TextField jeproLabRequestReference, jeproLabRequestMainContactInfoMail, jeproLabRequestMainContactInfoName;
    public TextField jeproLabSampleReference;
    public JeproPhoneField jeproLabCustomerCompanyPhone, jeproLabCustomerCompanyFax;
    public ComboBox<String> jeproLabSampleMatrix;
    public DatePicker jeproLabSampleTestDate;
    public GridPane jeproLabSampleAnalyzeSelector, jeproLabCustomerInformationLayout, jeproLabCustomerContactLayout, jeproLabSampleAddFormLayout;
    public HBox jeproLabCustomerCompanyPhoneWrapper;
    public Pane jeproLabCustomerInformationWrapper, jeproLabCustomerInformationTitleWrapper, jeproLabCustomerInformationContentWrapper;
    public Pane jeproLabRequestMainContactInfo, jeproLabSampleAddFormTitleWrapper;
    public ScrollPane jeproLabSampleFormWrapper;
    public TableView<JeproLabSampleRecord> jeproLabSampleRecordTableView;
    public TableColumn<JeproLabSampleRecord, Boolean> jeproLabSampleCheckBoxColumn;
    public TableColumn<JeproLabSampleRecord, String> jeproLabSampleReferenceColumn;
    public TableColumn<JeproLabSampleRecord, String> jeproLabSampleDesignationColumn;
    public TableColumn<JeproLabSampleRecord, HBox> jeproLabSampleActionColumn;
    public Button jeproLabSaveSampleBtn, jeproLabCancelSampleBtn;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        jeproLabRequestFormWrapper.setPrefWidth(formWidth);
        jeproLabRequestFormWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabRequestFormWrapper.setLayoutY(10);
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_REQUEST_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        jeproLabRequestFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabRequestFormTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabRequestContentWrapper.setPrefWidth(formWidth);
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

        jeproLabSampleFormWrapper.setPrefSize(subFormWidth, 260);
        VBox.setMargin(jeproLabSampleFormWrapper, new Insets(0, 0, 10, 0.01 * JeproLab.APP_WIDTH));

        renderCompanyForm();
        renderContactForm();
        renderSampleForm();
        renderSampleListForm();
        setFormLabels();
    }

    private void renderCompanyForm(){
        jeproLabCustomerInformationLayout.getColumnConstraints().addAll(
                new ColumnConstraints(150), new ColumnConstraints(350)
        );

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
        GridPane.setMargin(jeproLabRequestDelay, new Insets(5, 0, 5, 0));

        jeproLabResultTransmissionLabel.setPrefWidth(subFormWidth - 520);
        jeproLabResultTransmissionLabel.setAlignment(Pos.CENTER);

        jeproLabRequestDelayLabel.setPrefWidth(subFormWidth - 520);
        jeproLabRequestDelayLabel.setAlignment(Pos.CENTER);

        jeproLabRequestDelay.setPrefWidth(fourthColumnWidth);
        jeproLabRequestDelay.getItems().addAll(
                bundle.getString("JEPROLAB_ANALYSE_REGULAR_DELAY_LABEL"),
                bundle.getString("JEPROLAB_ANALYSE_EXPRESS_DELAY_12_HOURS_LABEL"),
                bundle.getString("JEPROLAB_ANALYSE_EXPRESS_DELAY_24_HOURS_LABEL"),
                bundle.getString("JEPROLAB_ANALYSE_EXPRESS_DELAY_48_HOURS_LABEL")
        );
        jeproLabRequestDelay.setValue(bundle.getString("JEPROLAB_ANALYSE_REGULAR_DELAY_LABEL"));
    }

    private void renderSampleForm(){
        Label mainFormLabel = new Label(bundle.getString("JEPROLAB_CUSTOMER_INFORMATION_LABEL"));
        Label mainContactFormLabel = new Label(bundle.getString("JEPROLAB_MAIN_CONTACT_INFO_LABEL"));
        Label sampleFormLabel = new Label(bundle.getString("JEPROLAB_SAMPLE_IDENTIFICATION_LABEL"));

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

        jeproLabSampleAddFormLayout.getColumnConstraints().addAll(new ColumnConstraints(140), new ColumnConstraints(240));
        jeproLabSampleAddFormTitleWrapper.setPrefSize(subFormWidth, 30);
        jeproLabSampleAddFormTitleWrapper.getStyleClass().add("form-panel-title-gray");
        VBox.setMargin(jeproLabSampleAddFormTitleWrapper, new Insets(0, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        GridPane.setMargin(jeproLabSampleReferenceLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleReference, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleDesignationLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleDesignation, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleMatrixLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleMatrix, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleTestDateLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleTestDate, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabSampleAnalyzeSelectorLabel, new Insets(5, 0, 5, 10));
        GridPane.setMargin(jeproLabSampleAnalyzeSelector, new Insets(5, 0, 15, 5));
        jeproLabSampleAnalyzeSelectorLabel.setPrefWidth(380);
        jeproLabSampleAnalyzeSelectorLabel.setAlignment(Pos.CENTER);
        jeproLabSampleReference.setDisable(true);

        Map<Integer, String> matrices = JeproLabRequestModel.JeproLabMatrixModel.getMatrices();
        jeproLabSampleMatrix.setPromptText(bundle.getString("JEPROLAB_SELECT_MATRIX_LABEL"));
        for(Map.Entry<Integer, String> matrix : matrices.entrySet()){
            jeproLabSampleMatrix.getItems().add(matrix.getValue());
        }
        List<JeproLabAnalyzeModel> analyzesList = JeproLabAnalyzeModel.getAnalyzeList();
        jeproLabSampleAnalyzeSelector.getColumnConstraints().addAll(
                new ColumnConstraints(120),
                new ColumnConstraints(120),
                new ColumnConstraints(120)
        );
        int analyzeIndex = 0;
        int analyzeRow = 0;
        for(JeproLabAnalyzeModel analyze : analyzesList){
            CheckBox analyzeCheck = new CheckBox(analyze.name.get("lang_" + context.language.language_id));
            jeproLabSampleAnalyzeSelector.add(analyzeCheck, analyzeIndex % 3, analyzeRow);
            GridPane.setMargin(analyzeCheck, new Insets(4,2,4,4));
            analyzeIndex++;
            if((analyzeIndex % 3) == 0){ analyzeRow++; }
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
        jeproLabSampleRecordTableView.setPrefWidth(formWidth - 440);
        double remainingWidth = formWidth - 520;

        jeproLabSampleCheckBoxColumn.setGraphic(checkAll);
        jeproLabSampleCheckBoxColumn.setPrefWidth(20);
        Callback<TableColumn<JeproLabSampleRecord, Boolean>, TableCell<JeproLabSampleRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCheckBoxCell();
        jeproLabSampleCheckBoxColumn.setCellFactory(checkBoxCellFactory);
        jeproLabSampleReferenceColumn.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabSampleReferenceColumn.setPrefWidth(0.30 * remainingWidth);
        jeproLabSampleDesignationColumn.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleDesignationColumn.setPrefWidth(0.7 * remainingWidth);
        jeproLabSampleActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabSampleActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabSampleRecord, HBox>, TableCell<JeproLabSampleRecord, HBox>> sampleActionFactory = param -> new JeproLabSampleActionCell();
        jeproLabSampleActionColumn.setCellFactory(sampleActionFactory);
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
        //jeproLabSampleReference.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabSampleReference.getStyleClass().add("input-label");
        jeproLabSampleDesignationLabel.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabSampleDesignationLabel.getStyleClass().add("input-label");
        jeproLabSampleMatrixLabel.setText(bundle.getString("JEPROLAB_MATRIX_LABEL"));
        jeproLabSampleMatrixLabel.getStyleClass().add("input-label");
        jeproLabSampleTestDateLabel.setText(bundle.getString("JEPROLAB_REMOVAL_DATE_LABEL"));
        jeproLabSampleTestDateLabel.getStyleClass().add("input-label");
        jeproLabSampleAnalyzeSelectorLabel.setText(bundle.getString("JEPROLAB_SELECT_ANALYSES_LABEL"));
        jeproLabSampleAnalyzeSelectorLabel.getStyleClass().add("input-label");
        jeproLabRequestReferenceLabel.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabRequestReferenceLabel.getStyleClass().add("input-label");
        jeproLabRequestDelayLabel.setText(bundle.getString("JEPROLAB_REQUEST_DELAY_LABEL"));
        jeproLabRequestDelayLabel.getStyleClass().add("input-label");
        jeproLabCustomerCompanyPhoneLabel.setText(bundle.getString("JEPROLAB_PHONE_LABEL"));
        jeproLabCustomerCompanyPhoneLabel.getStyleClass().add("input-label");
        jeproLabCustomerCompanyFaxLabel.setText(bundle.getString("JEPROLAB_FAX_LABEL"));
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
        loadRequest();
        List<JeproLabCustomerModel> customersContact;
        if(request.request_id > 0){
            jeproLabCustomerCompanyName.setText(request.customer.company);
            JeproLabAddressModel address = JeproLabAddressModel.getAddressByCustomerId(request.customer_id, true);
            jeproLabCustomerCompanyAddress.setText(address.address1);
            jeproLabCustomerCompanyAddressDetails.setText(address.address2);
            jeproLabRequestReference.setText(request.reference);
            jeproLabRequestMainContactInfoMail.setText(request.customer.email);
            jeproLabRequestMainContactInfoName.setText(request.customer.firstname + " " + request.customer.lastname);

            customersContact = JeproLabCustomerModel.getCustomersByCompany(request.customer.company);
            jeproLabRequestFirstContact.getItems().clear();
            jeproLabRequestSecondContact.getItems().clear();
            jeproLabRequestThirdContact.getItems().clear();
            jeproLabRequestFourthContact.getItems().clear();
            firstContactId = request.first_contact_id;
            secondContactId = request.second_contact_id;
            thirdContactId = request.third_contact_id;
            fourthContactId = request.fourth_contact_id;

            for(JeproLabCustomerModel customer : customersContact) {
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

        }else{
            jeproLabRequestReference.setText(JeproLabTools.createRequestReference());
            //customersContact = JeproLabCustomerModel.getCustomersByCompany("jeprodesign");
        }
        this.updateToolBar();
        this.addEventListeners();
    }

    private void loadRequest(){
        int requestId = JeproLab.request.getRequest().containsKey("request_id") ? Integer.parseInt(JeproLab.request.getRequest().get("request_id")) : 0;

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

    private void addEventListeners(){

    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveRequestBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (request.request_id > 0) {
            saveRequestBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveRequestBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
            saveRequestBtn.setOnMouseClicked(evt -> {
                String requestParams = "first_contact_id=" + firstContactId + "&second_contact_id=" + secondContactId + "&third_contact_id=";
                requestParams += thirdContactId + "&fourth_contact_id=" + fourthContactId + "";

                JeproLab.request.setRequest(requestParams);
                request.update();
            });
        }
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));

        commandWrapper.getChildren().addAll(saveRequestBtn, cancelBtn);
    }

    public class JeproLabSampleRecord {

    }

    private class JeproLabCheckBoxCell extends TableCell<JeproLabSampleRecord, Boolean> {

    }

    private class JeproLabSampleActionCell extends TableCell<JeproLabSampleRecord, HBox> {

    }
}
