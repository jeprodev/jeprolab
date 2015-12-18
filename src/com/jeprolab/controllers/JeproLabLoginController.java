package com.jeprolab.controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jeprolab.JeproLab;
import jeprolab.assets.config.JeproLabConfig;
import jeprolab.assets.db.JeproLabDataBaseManager;
import jeprolab.assets.extend.controls.FormPanel;
import jeprolab.assets.extend.controls.FormPanelContainer;
import jeprolab.assets.extend.controls.FormPanelTitle;
import jeprolab.assets.tools.JeproLabContext;
import jeprolab.models.JeproLabEmployeeModel;
import jeprolab.models.core.JeproLabApplication;
import jeprolab.models.core.JeproLabAuthenticationOption;
import jeprolab.models.core.JeproLabAuthenticationResponse;
import jeprolab.models.core.JeproLabFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class JeproLabLoginController implements Initializable{
    private ResourceBundle bundle;

    @FXML
    private Text loginTitleLabel;
    public Label userNameLabel, userPasswordLabel;
    public TextField userName;
    public PasswordField password;
    public Button loginButton, cancelButton;
    public FormPanel jeprolabLoginFormWrapper;
    public FormPanelTitle jeprolabLoginFormTitleWrapper;
    public FormPanelContainer jeprolabLoginFormContainerWrapper;

    @Override
    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 150;
        double inputColumnWidth = 200;
        //jeprolabLoginFormWrapper.getColumnConstraints().add(new ColumnConstraints(labelColumnWidth));
        //jeprolabLoginFormWrapper.getColumnConstraints().add(new ColumnConstraints(inputColumnWidth));
        double posX = (JeproLab.APP_WIDTH - (labelColumnWidth + inputColumnWidth))/2;
        jeprolabLoginFormWrapper.setLayoutX(posX);
        double posY = (JeproLab.APP_HEIGHT/4 );
        jeprolabLoginFormWrapper.setLayoutY(posY);
        loginTitleLabel.setText(bundle.getString("JEPROLAB_LOGIN_PANEL_TITLE"));
        loginTitleLabel.getStyleClass().add("form-title");
        loginTitleLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setMargin(userNameLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(userName, new Insets(5, 0, 15, 0));
        GridPane.setMargin(userPasswordLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(password, new Insets(5, 0, 15, 0));
        userNameLabel.setText(bundle.getString("JEPROLAB_USERNAME_LABEL"));
        userPasswordLabel.setText(bundle.getString("JEPROLAB_PASSWORD_LABEL"));

        loginButton.setText(bundle.getString("JEPROLAB_LOGIN_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
    }

    @FXML
    protected void handleSubmitButton(ActionEvent evt) throws IOException {
        JeproLabAuthenticationOption loginOptions = new JeproLabAuthenticationOption();
        boolean login = JeproLabApplication.login(userName.getText(), password.getText(), loginOptions);
        if(login){
            JeproLabContext context = JeproLabContext.getContext();
            //context.connection = con;
            context.employee = JeproLabFactory.getEmployee();
            JeproLab.getInstance().resetMenuAndToolBar();
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
        }else{
            System.out.println("unable to connect" );
        }
    }

    @FXML
    protected void handleCancelButton(ActionEvent evt){
        Platform.exit();
    }

    //todo code me
    public static boolean onUserLogin(JeproLabAuthenticationResponse response, JeproLabAuthenticationOption loginOptions){
        return true;
    }

    public static void loginFailed(JeproLabAuthenticationResponse response){

    }

    public static void onUserLogged(JeproLabAuthenticationOption loginOptions){

    }
}