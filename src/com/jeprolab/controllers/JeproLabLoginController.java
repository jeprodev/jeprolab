package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabApplication;
import com.jeprolab.models.core.JeproLabAuthentication;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabLoginController extends JeproLabController{
    @FXML
    public Label userNameLabel, userPasswordLabel;
    public TextField userName;
    public PasswordField password;
    public Button loginButton, cancelButton;
    public JeproFormPanel jeprolabLoginFormWrapper;
    public JeproFormPanelTitle jeprolabLoginFormTitleWrapper;
    public JeproFormPanelContainer jeprolabLoginFormContainerWrapper;
    public GridPane jeproLabLoginGridPane;
    public HBox jeproLabLoginCommandWrapper;
    public Text jeproLabLoginErrorMessageWrapper;

    @Override
    public void initialize(URL location , ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = labelColumnWidth + inputColumnWidth;
        formTitleLabel.setText(bundle.getString("JEPROLAB_LOGIN_PANEL_TITLE"));
        formTitleLabel.setPrefWidth(formWidth);
        formTitleLabel.setPrefSize(formWidth, 40);

        jeprolabLoginFormTitleWrapper.setPrefSize(formWidth, 40);
        jeprolabLoginFormTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabLoginGridPane.getColumnConstraints().addAll(
            new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );

        jeprolabLoginFormWrapper.setPrefWidth(JeproLab.APP_WIDTH * 0.7);
        jeprolabLoginFormWrapper.setLayoutX(JeproLab.APP_WIDTH * 0.15);
        jeprolabLoginFormWrapper.setLayoutY(80);

        double posX = ((JeproLab.APP_WIDTH/2) - (labelColumnWidth + inputColumnWidth)/2) - (0.15 * JeproLab.APP_WIDTH);
        jeprolabLoginFormTitleWrapper.setLayoutX(posX);

        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setAlignment(Pos.CENTER);

        userName.setText("jeproQxT");
        password.setText("qxtbljwm");

        GridPane.setMargin(userNameLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(userName, new Insets(5, 0, 15, 0));
        GridPane.setMargin(userPasswordLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(password, new Insets(5, 0, 15, 0));
        GridPane.setHalignment(userNameLabel, HPos.RIGHT);
        GridPane.setHalignment(userPasswordLabel, HPos.RIGHT);
        userNameLabel.setText(bundle.getString("JEPROLAB_USERNAME_LABEL"));
        userNameLabel.getStyleClass().add("input-label");
        userPasswordLabel.setText(bundle.getString("JEPROLAB_PASSWORD_LABEL"));

        userPasswordLabel.getStyleClass().add("input-label");

        jeprolabLoginFormContainerWrapper.setPrefWidth(labelColumnWidth + inputColumnWidth);
        jeprolabLoginFormContainerWrapper.setLayoutX(posX);
        jeprolabLoginFormContainerWrapper.setLayoutY(40);
        jeprolabLoginFormContainerWrapper.setPadding(new Insets(20, 10, 20, 10));
        jeproLabLoginCommandWrapper.setPrefWidth(labelColumnWidth + inputColumnWidth);
        jeproLabLoginCommandWrapper.setPadding(new Insets(10, 10, 10, 60));
        loginButton.setText(bundle.getString("JEPROLAB_LOGIN_LABEL"));
        cancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));

        jeproLabLoginErrorMessageWrapper.setLayoutY(280);
        jeproLabLoginErrorMessageWrapper.setWrappingWidth(0.7 * JeproLab.APP_WIDTH);
        jeproLabLoginErrorMessageWrapper.setTextAlignment(TextAlignment.CENTER);

        setEventHandler();
    }

    private void setEventHandler(){
        loginButton.setOnAction(evt -> {
            if(!JeproLab.getInstance().is_initialized){
                JeproLab.getInstance().initialize();
            }
            JeproLabAuthentication.JeproLabAuthenticationOption loginOption = new JeproLabAuthentication.JeproLabAuthenticationOption();
            loginOption.action = "core.login.admin";
            boolean login = JeproLabApplication.login(userName.getText(), password.getText(), loginOption);
            if(login){
                JeproLabContext context = JeproLabContext.getContext();
                context.employee = JeproLabFactory.getEmployee();
                JeproLab.getInstance().resetMenuAndToolBar();
                if(context.last_form != null) {
                    JeproLab.getInstance().goToForm(context.last_form);
                }else{
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
                }
            }else{
                jeproLabLoginErrorMessageWrapper.setText(bundle.getString("JEPROLAB_YOU_ARE_NOT_ALLOW_TO_LOG_MESSAGE"));
            }
        });

        cancelButton.setOnAction(evt -> {
            Platform.exit();
        });
    }


    //todo code me
    public static boolean onUserLogin(JeproLabAuthentication.JeproLabAuthenticationResponse response, JeproLabAuthentication.JeproLabAuthenticationOption loginOptions){
        return true;
    }

    public static void loginFailed(JeproLabAuthentication.JeproLabAuthenticationResponse response){

    }

    public static void onUserLogged(JeproLabAuthentication.JeproLabAuthenticationOption loginOptions){
        JeproLabContext context = JeproLabContext.getContext();
        context.employee = loginOptions.employee;
        JeproLab.getInstance().getUserInfoBtn().setText(context.employee.username + " ");
    }

    public static void onUserLoginFailure(JeproLabAuthentication.JeproLabAuthenticationResponse response){

    }
}