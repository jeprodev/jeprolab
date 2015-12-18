package com.jeprolab.views.installer.forms;

import com.jeprolab.JeproLab;
import com.jeprolab.views.installer.JeproLabInstallerForm;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


import java.util.ResourceBundle;


public class JeproLabInstallerSettingsForm extends JeproLabInstallerForm {
    private ResourceBundle bundle;
    public TextField hostName, dataBaseName, tablePrefix, dataBaseUserName, dataBasePortNumber;
    public PasswordField dataBaseUserPassword;
    public ComboBox<String> dataBaseManager;


    public JeproLabInstallerSettingsForm(String name) {
        super(name);
    }

    @Override
    public Node createView() {
        if(!isCreated || formWrapper == null){
            formWrapper = new Pane();
            GridPane settingWrapper = new GridPane();
            bundle = JeproLab.getBundle();

            Label hostNameLabel = new Label(bundle.getString("JEPROLAB_HOST_NAME_LABEL"));
            Label dataBaseUrlLabel = new Label(bundle.getString("JEPROLAB_DATA_BASE_URL_LABEL"));
            Label dataBaseManagerLabel = new Label(bundle.getString("JEPROLAB_DATA_BASE_MANAGER_LABEL"));
            Label tablePrefixLabel = new Label(bundle.getString("JEPROLAB_TABLE_PREFIX_LABEL"));
            Label dataBaseUserNameLabel = new Label(bundle.getString("JEPROLAB_USERNAME_LABEL"));
            Label dataBaseUserPasswordLabel = new Label(bundle.getString("JEPROLAB_PASSWORD_LABEL"));
            Label dataBasePortNumberLabel = new Label(bundle.getString("JEPROLAB_PORT_LABEL"));
            hostName = new TextField();
            hostName.setPrefWidth(220);
            dataBaseName = new TextField();
            dataBaseName.setPrefWidth(220);
            dataBaseManager = new ComboBox<>();
            dataBaseManager.getItems().addAll(
                   "MySql", "Oracle", "DB2", "Sybase"
            );
            dataBaseManager.setValue("MySql");
            tablePrefix = new TextField();
            tablePrefix.setPrefWidth(80);
            dataBasePortNumber = new TextField();
            dataBasePortNumber.setPrefWidth(80);
            dataBaseUserName = new TextField();
            dataBaseUserPassword = new PasswordField();

            HBox dataBasePrefixWrapper = new HBox(20);
            dataBasePrefixWrapper.getChildren().addAll(tablePrefix, dataBasePortNumberLabel, dataBasePortNumber);
            dataBasePrefixWrapper.setAlignment(Pos.CENTER_LEFT);
            GridPane.setMargin(hostNameLabel, new Insets(5, 5, 10, 0));
            GridPane.setMargin(hostName, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseUrlLabel, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseName, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseManagerLabel, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseManager, new Insets(5, 5, 10, 0));
            GridPane.setMargin(tablePrefixLabel, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBasePrefixWrapper, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseUserNameLabel, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseUserName, new Insets(5, 5, 10, 0));
            //GridPane.setMargin(tablePrefix, new Insets(5, 5, 15, 0));
            GridPane.setMargin(dataBaseUserPasswordLabel, new Insets(5, 5, 10, 0));
            GridPane.setMargin(dataBaseUserPassword, new Insets(5, 5, 10, 0));

            settingWrapper.add(hostNameLabel, 0, 0);
            settingWrapper.add(hostName, 1, 0);
            settingWrapper.add(dataBaseUrlLabel, 0, 1);
            settingWrapper.add(dataBaseName, 1, 1);
            settingWrapper.add(dataBaseManagerLabel, 0, 2);
            settingWrapper.add(dataBaseManager, 1, 2);
            settingWrapper.add(tablePrefixLabel, 0, 3);
            settingWrapper.add(dataBasePrefixWrapper, 1, 3);
            settingWrapper.add(dataBaseUserNameLabel, 0, 4);
            settingWrapper.add(dataBaseUserName, 1, 4);
            settingWrapper.add(dataBaseUserPasswordLabel, 0, 5);
            settingWrapper.add(dataBaseUserPassword, 1, 5);

            formWrapper.getChildren().add(settingWrapper);
            isCreated = true;
        }
        return formWrapper;
    }
}