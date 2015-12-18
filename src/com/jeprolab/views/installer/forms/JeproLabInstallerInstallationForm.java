package com.jeprolab.views.installer.forms;

import com.jeprolab.JeproLab;
import com.jeprolab.views.installer.JeproInstallerThread;
import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class JeproLabInstallerInstallationForm extends JeproLabInstallerForm {
    public ProgressBar installerProgress;
    public Label progressIndicator, installingLabel;
    public TextArea installationDetails;
    private JeproInstallerThread installerThread;

    public JeproLabInstallerInstallationForm(String name) {
        super(name);
    }

    @Override
    public Node createView() {
        if(!isCreated || formWrapper == null){
            VBox formContainer = new VBox(20);
            formContainer.setPadding(new Insets(10, 4, 4, 4));
            HBox installerIndicatorWrapper = new HBox(15);
            progressIndicator = new Label("0%");
            installingLabel = new Label(bundle.getString("JEPROLAB_INSTALLED_LABEL"));
            installationDetails = new TextArea();
            installerProgress = new ProgressBar();
            formWrapper = new Pane();
            installerIndicatorWrapper.getChildren().addAll(progressIndicator, installingLabel);
            formContainer.getChildren().addAll(installerIndicatorWrapper, installerProgress, installationDetails);
            formWrapper.setPadding(new Insets(30, 4, 10, 4));
            formWrapper.getChildren().add(formContainer);
        }
        return formWrapper;
    }

    public void setItemsWidth(double formWidth){
        installerProgress.setPrefWidth(formWidth);
        installationDetails.setPrefWidth(formWidth);
    }

    public void setMaximum(final int max){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //installerProgress.setMa
            }
        });
    }

    public void advance(final double value){
        try{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    installerProgress.setProgress(installerProgress.getProgress() + value);
                }
            });
            Thread.yield();
        }catch (Exception ignored){}
    }

    public void done(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /** application installed go to summary page **/ //TODO
            }
        });
    }

    public void message(final String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setText(message);
            }
        });
    }

    public void error(final String msg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //dispose();
                installationDetails.setText(msg + JeproLab.getBundle().getString("JEPROLAB_INSTALLATION_ABORTED_MESSAGE")); //TODO to be replaced with dialog box
                System.exit(1);
            }
        });
    }

    public void setThread(JeproInstallerThread thread){
        this.installerThread = thread;
    }
}