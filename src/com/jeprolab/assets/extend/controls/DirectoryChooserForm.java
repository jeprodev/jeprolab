package com.jeprolab.assets.extend.controls;


import com.jeprolab.assets.tools.os.JeproOperatingSystem;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class DirectoryChooserForm extends HBox {
    private TextField selectedDirectory;
    private Button selectorButton;
    private Stage callerStage;
    public Map osTaskDirectories;

    public DirectoryChooserForm(Stage caller){
        this(caller, "...");
    }

    public DirectoryChooserForm(Stage caller, String btnLabel){
        selectedDirectory = new TextField();
        selectedDirectory.setText(JeproOperatingSystem.getOperatingSystem().getInstallDirectory("jeprolab"));
        selectorButton = new Button(btnLabel);
        callerStage = caller;
        setSpacing(5);
        setPadding(new Insets(20, 3, 5, 3));
        getChildren().addAll(selectedDirectory, selectorButton);
        directoryChooserEventHandler();
    }

    private void directoryChooserEventHandler(){
        selectorButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedPath = directoryChooser.showDialog(callerStage);

            if (selectedPath == null) {
                selectedDirectory.setText(JeproOperatingSystem.getOperatingSystem().getInstallDirectory("jeprolab"));
            } else {
                selectedDirectory.setText(selectedPath.getAbsolutePath());
            }
        });
    }

    public void setSelectedDirectoryWidth(double width){
        if(selectedDirectory != null){
            selectedDirectory.setPrefWidth(width);
        }
    }

    public TextField getSelectedDirectory(){
        return selectedDirectory;
    }
}