package com.jeprolab.views.installer.forms;


import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.jeprolab.JeproLab;

import java.util.ResourceBundle;

public class JeproLabInstallerAdditionalTaskForm extends JeproLabInstallerForm {
    public CheckBox createDesktopShortCut, createLaunchBarIcon, autoStart;
    private ResourceBundle bundle;

    public JeproLabInstallerAdditionalTaskForm(String name) {
        super(name);
    }

    @Override
    public Node createView() {
        if(!isCreated || formWrapper == null){
            formWrapper = new Pane();
            VBox additionalTaskWrapper = new VBox(15);
            additionalTaskWrapper.setPadding(new Insets(30, 4, 4, 4));
            bundle = JeproLab.getBundle();
            Text selectAdditionalTaskMessage = new Text(bundle.getString("JEPROLAB_SELECT_ADDITIONAL_TASK_MESSAGE"));
            Label additionalIconsLabel = new Label(bundle.getString("JEPROLAB_ADDITIONAL_ICONS_LABEL"));
            Label autoStartLabel = new Label(bundle.getString("JEPROLAB_AUTO_START_LABEL"));

            createDesktopShortCut = new CheckBox(bundle.getString("JEPROLAB_CREATE_DESKTOP_SHORTCUT_LABEL"));
            createLaunchBarIcon = new CheckBox(bundle.getString("JEPROLAB_CREATE_DESKTOP_LAUNCH_BAR_ICON_LABEL"));
            autoStart = new CheckBox(bundle.getString("JEPROLAB_AUTO_START_LABEL"));
            additionalTaskWrapper.getChildren().addAll(selectAdditionalTaskMessage, additionalIconsLabel, createDesktopShortCut, createLaunchBarIcon, autoStartLabel, autoStart);
            formWrapper.getChildren().add(additionalTaskWrapper);
            isCreated = true;
        }
        return formWrapper;
    }
}