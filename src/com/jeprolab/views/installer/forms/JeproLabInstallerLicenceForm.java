package com.jeprolab.views.installer.forms;

import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class JeproLabInstallerLicenceForm extends JeproLabInstallerForm {
    public RadioButton acceptLicense, declineLicense;
    public ToggleGroup licenseToggle;

    public JeproLabInstallerLicenceForm(String name) {
        super(name);
    }

    @Override
    public Node createView() {
        if(!isCreated || formWrapper == null) {
            formWrapper = new Pane();
            licenseToggle = new ToggleGroup();
            VBox formContainer = new VBox(5);
            //formTitle = new Label(bundle.getString("JEPROLAB_LICENCE_LABEL"));
            Text readLicense = new Text(bundle.getString("JEPROLAB_READ_LICENCE_MESSAGE"));
            TextArea licenceContainer = new TextArea();
            StringBuilder preamble = new StringBuilder(bundle.getString("GNU_LICENCE_PREAMBLE_LABEL") + "\n\n");
            //preamble
            licenceContainer.setText(preamble.toString() + bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_1_LABEL") + "\n\n" +
                            bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_2_LABEL") + "\n\n" + bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_3_LABEL") + "\n\n" +
                            bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_4_LABEL") + "\n\n" + bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_5_LABEL") + "\n\n" +
                            bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_6_LABEL") + "\n\n" + bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_7_LABEL") + "\n\n" +
                            bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_8_LABEL") + "\n\n" + bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_9_LABEL") + "\n\n" +
                            bundle.getString("GNU_LICENCE_PREAMBLE_TEXT_PRG_10_LABEL")
            );
            licenceContainer.setPrefSize(350, 160);
            licenceContainer.setWrapText(true);
            licenceContainer.setLayoutX(0);
            acceptLicense = new RadioButton(bundle.getString("JEPROLAB_ACCEPT_LICENCE_LABEL"));
            declineLicense = new RadioButton(bundle.getString("JEPROLAB_DECLINE_LICENCE_LABEL"));
            declineLicense.setSelected(true);
            acceptLicense.setToggleGroup(licenseToggle);
            declineLicense.setToggleGroup(licenseToggle);
            formContainer.setLayoutX(0);
            HBox agreementWrapper = new HBox(40);
            agreementWrapper.setPadding(new Insets(5, 0, 5, 0));
            agreementWrapper.getChildren().addAll(acceptLicense, declineLicense);

            formContainer.getChildren().addAll(readLicense, licenceContainer, agreementWrapper);
            formWrapper.getChildren().setAll(formContainer);
            addEventListener();
        }
        return formWrapper;
    }

    private void addEventListener(){

    }
}