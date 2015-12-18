package com.jeprolab.views.installer.forms;

import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class JeproLabInstallerSummaryForm extends JeproLabInstallerForm {
    public JeproLabInstallerSummaryForm(String name) {
        super(name);
    }
    public CheckBox readMeFile, launchApp;

    @Override
    public Node createView() {
        if(!isCreated || formWrapper == null){
            formWrapper = new Pane();
            VBox formContainer = new VBox(5);
            readMeFile = new CheckBox(bundle.getString("JEPROLAB_READ_ME_FILE_LABEL"));
            launchApp = new CheckBox(bundle.getString("JEPROLAB_LAUNCH_APP_LABEL"));

            formContainer.getChildren().addAll(readMeFile, launchApp);
            formWrapper.getChildren().add(formContainer);
        }
        return formWrapper;
    }
}