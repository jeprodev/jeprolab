package com.jeprolab.assets.tools;

import com.jeprolab.JeproLab;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproWindowsToolBar extends ToolBar {
    private JeproWindowsButtons windowsButtons;

    public JeproWindowsToolBar(final Stage appStage){
        this.setId("windows-bar");
        Region spacer = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Label applicationTitle = new Label(JeproLab.getBundle().getString("JEPROLAB_SITE_MANAGER_TITLE"));
        applicationTitle.setId("application-title");
        windowsButtons = new JeproWindowsButtons(appStage);
        this.getItems().addAll(spacer, applicationTitle, spacer2, windowsButtons);
    }
}
