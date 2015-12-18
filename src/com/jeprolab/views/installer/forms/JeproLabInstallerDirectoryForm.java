package com.jeprolab.views.installer.forms;


import com.jeprolab.assets.extend.controls.DirectoryChooserForm;
import com.jeprolab.assets.extend.controls.JeproFieldSet;
import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.jeprolab.JeproLab;

public class JeproLabInstallerDirectoryForm extends JeproLabInstallerForm {
    private DirectoryChooserForm installInSelector;
    public Label requiredSpace, availableSpace;

    public JeproLabInstallerDirectoryForm(String name) {
        super(name);
    }

    @Override
    public Node createView(){
        if(!isCreated || formWrapper == null) {
            formWrapper = new Pane();
            VBox formContainer = new VBox(10);
            formContainer.setPadding(new Insets(30, 4, 4, 4));
            Text targetDirectoryMessage = new Text(bundle.getString("JEPROLAB_INSTALL_TARGET_DIRECTORY_LABEL"));
            targetDirectoryMessage.setWrappingWidth(330);
            JeproFieldSet selectorFormWrapper = new JeproFieldSet(bundle.getString("JEPROLAB_INSTALL_TO_LABEL"));
            Text defaultCreatorMessage = new Text(bundle.getString("JEPROLAB_DEFAULT_INSTALL_DIRECTORY_LABEL"));
            installInSelector = new DirectoryChooserForm(JeproLab.getInstance().getAppStage());
            installInSelector.setSelectedDirectoryWidth(300);
            selectorFormWrapper.setContent(installInSelector);
            //selectorFormWrapper.getChildren().addAll(installInSelector);
            GridPane storageInfoWrapper = new GridPane();
            requiredSpace = new Label();
            availableSpace = new Label();
            Label requiredSpaceLabel = new Label(bundle.getString("JEPROLAB_REQUIRED_STORAGE_LABEL"));
            Label availableSpaceLabel = new Label(bundle.getString("JEPROLAB_AVAILABLE_SPACE_LABEL"));
            GridPane.setMargin(requiredSpaceLabel, new Insets(5, 0, 5, 0));
            GridPane.setMargin(requiredSpace, new Insets(5, 0, 5, 0));
            GridPane.setMargin(availableSpaceLabel, new Insets(5, 0, 5, 0));
            GridPane.setMargin(availableSpace, new Insets(5, 0, 5, 0));
            storageInfoWrapper.add(requiredSpaceLabel, 0, 0);
            storageInfoWrapper.add(requiredSpace, 1, 0);
            storageInfoWrapper.add(availableSpaceLabel, 0, 1);
            storageInfoWrapper.add(availableSpace, 1, 1);
            formContainer.getChildren().addAll(targetDirectoryMessage, selectorFormWrapper, defaultCreatorMessage, storageInfoWrapper);
            formWrapper.getChildren().add(formContainer);
            isCreated = true;
        }
        return formWrapper;
    }

    public DirectoryChooserForm getInstallInSelector(){
        return installInSelector;
    }
}
