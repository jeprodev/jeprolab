package com.jeprolab.views.installer.forms;

import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;



public class JeproLabInstallerIntroductionForm extends JeproLabInstallerForm {

    public JeproLabInstallerIntroductionForm(String name) {
        super(name);
    }

    @Override
    public Node createView() {
        if(!isCreated || formWrapper == null) {
            formWrapper = new Pane();
            VBox formContainer = new VBox(15);
            formContainer.setPadding(new Insets(30, 5, 5, 5));
            Text introductionText = new Text();
            introductionText.setText(
                    bundle.getString("JEPROLAB_INSTALLER_NOTE_MESSAGE") + "\n\n" +
                bundle.getString("JEPROLAB_COPYRIGHT_MESSAGE")
            );
            introductionText.setLayoutY(0);
            introductionText.prefWidth(350);
            introductionText.setWrappingWidth(350);
            formContainer.getChildren().addAll(introductionText);
            formWrapper.getChildren().add(formContainer);
            isCreated = true;
        }
        return formWrapper;
    }
}