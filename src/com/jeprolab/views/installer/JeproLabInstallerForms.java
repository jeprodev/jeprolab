package com.jeprolab.views.installer;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.os.JeproOperatingSystem;
import com.jeprolab.assets.tools.os.JeproOsTask;
import com.jeprolab.views.installer.forms.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ResourceBundle;
import java.util.Stack;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabInstallerForms extends Pane{
    ResourceBundle bundle;
    private JeproLabInstaller installer;
    private JeproOsTask[] osTasks;
    public JeproLabInstallerIntroductionForm introductionForm;
    public JeproLabInstallerDirectoryForm directoryForm;
    public JeproLabInstallerLicenceForm licenceForm;
    public JeproLabInstallerSettingsForm settingsForm;
    public JeproLabInstallerAdditionalTaskForm additionalTaskForm;
    public JeproLabInstallerSummaryForm summaryForm;
    public JeproLabInstallerInstallationForm installationForm;
    public RadioButton introductionLabel, licenseLabel, installationDirectoryLabel, applicationSettingLabel, additionalTaskLabel, installationLabel, summaryLabel;
    private Node currentView;
    private StackPane formsContainer;
    private Button backButton, nextButton, cancelButton;
    private JeproLabInstallerForm currentForm;
    private Stack<JeproLabInstallerForm> history = new Stack<>();
    private Stack<JeproLabInstallerForm> forwardHistory = new Stack<>();
    private boolean changingForm = false;
    private final int steps = 6;
    private int currentStep = 0;
    private Label installerLabel, installerCurrentStepLabel;

    public JeproLabInstallerForms(){
        bundle = JeproLab.getBundle();
        installer = new JeproLabInstaller();

        VBox installerFormWrapper = new VBox();
        HBox installerHeaderWrapper = new HBox(35);
        HBox installerContainerWrapper = new HBox(2);
        VBox installerNavigationWrapper = new VBox(15);
        HBox installerCommandWrapper = new HBox();

        /** header layout **/
        installerHeaderWrapper.getStyleClass().add("installer-header");
        installerHeaderWrapper.setPadding(new Insets(3, 5, 3, 5));
        Image logoImg =  new Image(JeproLab.class.getResourceAsStream("resources/images/logo.png"));
        ImageView logoImage = new ImageView();
        logoImage.setImage(logoImg);

        VBox installerTitleWrapper = new VBox(10);
        installerLabel = new Label(bundle.getString("JEPROLAB_INSTALLER_TITLE"));
        installerLabel.setId("installer-title");
        installerCurrentStepLabel = new Label();
        installerCurrentStepLabel.setPadding(new Insets(5, 0, 5, 40));
        installerCurrentStepLabel.setId("current-step-title");
        installerTitleWrapper.getChildren().addAll(installerLabel, installerCurrentStepLabel);
        installerHeaderWrapper.getChildren().addAll(logoImage, installerTitleWrapper);

        ToggleGroup installationSteps = new ToggleGroup();
        introductionLabel = new RadioButton(bundle.getString("JEPROLAB_INTRODUCTION_LABEL"));
        licenseLabel = new RadioButton(bundle.getString("JEPROLAB_LICENSE_LABEL"));
        installationDirectoryLabel = new RadioButton(bundle.getString("JEPROLAB_INSTALL_TO_LABEL"));
        applicationSettingLabel = new RadioButton(bundle.getString("JEPROLAB_SETTINGS_LABEL"));
        additionalTaskLabel = new RadioButton(bundle.getString("JEPROLAB_ADDITIONAL_TASK_LABEL"));
        installationLabel = new RadioButton(bundle.getString("JEPROLAB_INSTALLATION_LABEL"));
        summaryLabel = new RadioButton(bundle.getString("JEPROLAB_SUMMARY_LABEL"));
        introductionLabel.getStyleClass().setAll("installation-step", "radio-button");
        licenseLabel.getStyleClass().setAll("installation-step", "radio-button");
        installationDirectoryLabel.getStyleClass().setAll("installation-step", "radio-button");
        applicationSettingLabel.getStyleClass().setAll("installation-step", "radio-button");
        installationLabel.getStyleClass().setAll("installation-step", "radio-button");
        additionalTaskLabel.getStyleClass().setAll("installation-step", "radio-button");
        summaryLabel.getStyleClass().setAll("installation-step", "radio-button");
        introductionLabel.setToggleGroup(installationSteps);
        introductionLabel.setSelected(true);
        licenseLabel.setToggleGroup(installationSteps);
        additionalTaskLabel.setToggleGroup(installationSteps);
        installationDirectoryLabel.setToggleGroup(installationSteps);
        applicationSettingLabel.setToggleGroup(installationSteps);
        installationLabel.setToggleGroup(installationSteps);
        summaryLabel.setToggleGroup(installationSteps);

        introductionForm = new JeproLabInstallerIntroductionForm(bundle.getString("JEPROLAB_INSTALLER_WELCOME_MESSAGE"));
        introductionForm.setId("introduction-form");
        directoryForm = new JeproLabInstallerDirectoryForm(bundle.getString("JEPROLAB_DIRECTORY_LABEL"));
        directoryForm.setId("directory-form");
        licenceForm = new JeproLabInstallerLicenceForm(bundle.getString("JEPROLAB_LICENCE_LABEL"));
        licenceForm.setId("licence-form");
        settingsForm = new JeproLabInstallerSettingsForm(bundle.getString("JEPROLAB_SETTINGS_LABEL"));
        settingsForm.setId("settings-form");
        additionalTaskForm = new JeproLabInstallerAdditionalTaskForm(bundle.getString("JEPROLAB_ADDITIONAL_TASK_LABEL"));
        additionalTaskForm.setId("additional-task");
        summaryForm = new JeproLabInstallerSummaryForm(bundle.getString("JEPROLAB_SUMMARY_LABEL"));
        summaryForm.setId("summary-form");
        installationForm = new JeproLabInstallerInstallationForm(bundle.getString("JEPROLAB_INSTALLATION_LABEL"));
        installationForm.setId("installation-form");

        formsContainer = new StackPane();
        formsContainer.setPrefSize(JeproLab.APP_INSTALLER_WIDTH - 180, 285);
        formsContainer.getChildren().addAll(introductionForm, directoryForm, licenceForm,settingsForm, additionalTaskForm, installationForm, summaryForm);

        backButton = new Button(bundle.getString("JEPROLAB_BACK_LABEL"));
        backButton.setDisable(true);
        nextButton = new Button(bundle.getString("JEPROLAB_NEXT_LABEL"));
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"));

        installerNavigationWrapper.setPrefSize(180, 285);
        installerNavigationWrapper.setPadding(new Insets(5, 5, 5, 5));
        installerNavigationWrapper.getChildren().addAll(introductionLabel, licenseLabel, installationDirectoryLabel, applicationSettingLabel, additionalTaskLabel, installationLabel, summaryLabel);
        installerContainerWrapper.setPrefSize(JeproLab.APP_INSTALLER_WIDTH, 285);
        formsContainer.setLayoutX(180);
        formsContainer.setLayoutY(80);
        installerContainerWrapper.getChildren().addAll(installerNavigationWrapper, formsContainer);
        installerCommandWrapper.setPrefSize(JeproLab.APP_INSTALLER_WIDTH, 45);
        installerCommandWrapper.setPadding(new Insets(0, 15, 4, 0));
        installerCommandWrapper.setAlignment(Pos.CENTER_RIGHT);
        installerCommandWrapper.setSpacing(10);
        installerCommandWrapper.getChildren().addAll(backButton, nextButton, cancelButton);
        installerFormWrapper.getChildren().addAll(installerHeaderWrapper, installerContainerWrapper, installerCommandWrapper);
        setPrefWidth(JeproLab.APP_INSTALLER_WIDTH );
        goToForm(introductionForm);
        getChildren().add(installerFormWrapper);
        installerEventHandler();

        String operatingSystemClass = JeproOperatingSystem.getOperatingSystem().getClass().getName();
        String completedInfo = "done-" + operatingSystemClass.substring(operatingSystemClass.indexOf('$') + 1) + ".html";
    }

    public void goToForm(JeproLabInstallerForm form){
        goToForm(form, true, false, true);
    }

    private void goToForm(JeproLabInstallerForm form, boolean addHistory, boolean force, boolean swapViews){
        if(form != null && !changingForm){
            if(force || form != currentForm){
                changingForm = true;
                if(swapViews){
                    Node view = form.createView();
                    if(view == null){
                        view = new Region();
                    }

                    if(force || view != currentView){
                        formsContainer.getChildren().setAll(view);
                        installerCurrentStepLabel.setText(form.formTitle.getText());
                        currentView = view;
                    }
                }

                // add page to history
                if (addHistory && currentForm !=null) {
                    history.push(currentForm);
                    forwardHistory.clear();
                }
                //form.updateCommand();
                currentForm = form;
                setEventHandler(form);
                changingForm = false;
            }
        }
    }

    private void setEventHandler(JeproLabInstallerForm form){
        switch (form.getId()){
            case "introduction-form" :
                break;
            case "directory-form" :
                if(form.isCreated) {
                    directoryForm.getInstallInSelector().getSelectedDirectory().textProperty().addListener((observable, oldValue, newValue) -> {

                    });
                }
                break;
            case "licence-form" :
                break;
            case "settings-form" :
                break;
            case "installation-form" :
                break;
            case "summary-form" :
                break;
        }
    }

    private void installerEventHandler(){
        backButton.setOnMouseClicked(evt -> {
            /** process mouse clicked event **/
            switchBackward();
        });

        nextButton.setOnMouseClicked(evt -> {
            /** process mouse clicked event **/
            switchForward();
        });

        cancelButton.setOnMouseClicked(evt -> {
            /** process mouse clicked event **/
            Platform.exit();
        });


    }

    private void switchBackward(){
        if(currentStep > 0){
            switch (currentStep){
                case 1 :
                    /** back to introduction form **/
                    introductionLabel.setSelected(true);
                    backButton.setDisable(true);
                    goToForm(introductionForm);
                    break;
                case 2 :
                    /** back to license form **/
                    backButton.setDisable(false);
                    goToForm(licenceForm);
                    break;
                case 3 :
                    /** back to installation directory form **/
                    goToForm(directoryForm);
                    break;
                case 4 :
                    /** back to settings form **/
                    goToForm(settingsForm);
                    nextButton.setText(bundle.getString("JEPROLAB_NEXT_LABEL"));
                    break;
                default:
                    /** disable backward button **/
                    backButton.setDisable(true);
                    break;
            }
            currentStep = currentStep - 1;
        }else{
            backButton.setDisable(true);
        }
    }

    private void switchForward(){
        if(currentStep < steps){
            switch (currentStep){
                case 0 :
                    /*** move to step 1 **/
                    introductionLabel.getStyleClass().add("done");
                    licenseLabel.setSelected(true);
                    backButton.setDisable(false);
                    goToForm(licenceForm);
                    if(licenceForm.acceptLicense.isSelected()){
                        nextButton.setDisable(false);
                    }else{
                        nextButton.setDisable(true);
                    }
                    licenceForm.licenseToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                        if(licenceForm.licenseToggle.getSelectedToggle() != null){
                            if(licenceForm.acceptLicense.isSelected()){
                                nextButton.setDisable(false);
                            }else if(licenceForm.declineLicense.isSelected()){
                                nextButton.setDisable(true);
                            }
                        }
                    });
                    break;
                case 1 :
                    /*** move to step installation directory selector **/
                    licenseLabel.getStyleClass().add("done");
                    installationDirectoryLabel.setSelected(true);
                    backButton.setDisable(false);
                    goToForm(directoryForm);
                    break;
                case 2 :
                    /*** move to step  **/
                    installationDirectoryLabel.getStyleClass().add("done");
                    applicationSettingLabel.setSelected(true);
                    backButton.setDisable(false);
                    nextButton.setDisable(true);
                    goToForm(settingsForm);
                    listenSettingChange();
                    break;
                case 3 :
                    /** move to additional task configuration **/
                    applicationSettingLabel.getStyleClass().add("done");
                    additionalTaskLabel.setSelected(true);
                    backButton.setDisable(false);
                    nextButton.setText(bundle.getString("JEPROLAB_INSTALL_LABEL"));
                    goToForm(additionalTaskForm);
                    break;
                case 4 :
                    /*** move to installation step  **/
                    applicationSettingLabel.getStyleClass().add("done");
                    installationLabel.setSelected(true);
                    nextButton.setDisable(true);
                    nextButton.setText(bundle.getString("JEPROLAB_INSTALLING_LABEL"));
                    backButton.setDisable(true);
                    cancelButton.setDisable(true);
                    goToForm(installationForm);
                    installationForm.setItemsWidth(300);
                    break;
                case 5 :
                    installationLabel.getStyleClass().add("done");
                    summaryLabel.setSelected(true);
                    nextButton.setText(bundle.getString("JEPROLAB_FINISHED_LABEL"));
                    cancelButton.setDisable(true);
                    backButton.setDisable(true);
                    goToForm(summaryForm);
                    break;
            }
            currentStep = currentStep + 1;
        }
    }

    private void listenSettingChange(){
        settingsForm.hostName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!settingsForm.hostName.getText().equals("")){
                if(settingsForm.dataBaseName.getText().equals("") || settingsForm.tablePrefix.getText().equals("") || settingsForm.dataBasePortNumber.getText().equals("") || settingsForm.dataBaseUserName.getText().equals("") || settingsForm.dataBaseUserPassword.getText().equals("")){
                    nextButton.setDisable(true);
                }else{
                    nextButton.setDisable(false);
                }
            }else{
                nextButton.setDisable(true);
            }
        });

        settingsForm.dataBaseName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!settingsForm.dataBaseName.getText().equals("")){
                if(settingsForm.hostName.getText().equals("") || settingsForm.tablePrefix.getText().equals("") || settingsForm.dataBasePortNumber.getText().equals("") || settingsForm.dataBaseUserName.getText().equals("") || settingsForm.dataBaseUserPassword.getText().equals("")){
                    nextButton.setDisable(true);
                }else{
                    nextButton.setDisable(false);
                }
            }else{
                nextButton.setDisable(true);
            }
        });

        settingsForm.tablePrefix.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!settingsForm.tablePrefix.getText().equals("")){
                if(settingsForm.hostName.getText().equals("") || settingsForm.dataBaseName.getText().equals("") || settingsForm.dataBasePortNumber.getText().equals("") || settingsForm.dataBaseUserName.getText().equals("") || settingsForm.dataBaseUserPassword.getText().equals("")){
                    nextButton.setDisable(true);
                }else{
                    nextButton.setDisable(false);
                }
            }else{
                nextButton.setDisable(true);
            }
        });

        settingsForm.dataBasePortNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!settingsForm.dataBasePortNumber.getText().equals("")){
                if(settingsForm.hostName.getText().equals("") || settingsForm.tablePrefix.getText().equals("") || settingsForm.dataBaseName.getText().equals("") || settingsForm.dataBaseUserName.getText().equals("") || settingsForm.dataBaseUserPassword.getText().equals("")){
                    nextButton.setDisable(true);
                }else{
                    nextButton.setDisable(false);
                }
            }else{
                nextButton.setDisable(true);
            }
        });

        settingsForm.dataBaseUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!settingsForm.dataBaseUserName.getText().equals("")){
                if(settingsForm.hostName.getText().equals("") || settingsForm.tablePrefix.getText().equals("") || settingsForm.dataBasePortNumber.getText().equals("") || settingsForm.dataBaseUserPassword.getText().equals("") || settingsForm.dataBaseName.getText().equals("")){
                    nextButton.setDisable(true);
                }else{
                    nextButton.setDisable(false);
                }
            }else{
                nextButton.setDisable(true);
            }
        });

        settingsForm.dataBaseUserPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!settingsForm.dataBaseUserPassword.getText().equals("")){
                if(settingsForm.hostName.getText().equals("") || settingsForm.tablePrefix.getText().equals("") || settingsForm.dataBasePortNumber.getText().equals("") || settingsForm.dataBaseUserName.getText().equals("") || settingsForm.dataBaseName.getText().equals("")){
                    nextButton.setDisable(true);
                }else{
                    nextButton.setDisable(false);
                }
            }else{
                nextButton.setDisable(true);
            }
        });
    }
}