package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabApplication;
import com.jeprolab.models.core.JeproLabAuthentication;
import com.jeprolab.models.core.JeproLabFactory;
//import com.mysql.jdbc.log.NullLogger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabLoginController extends JeproLabController{
    private Worker<Boolean> worker;

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
        Worker<Boolean> worker = new Task<Boolean>() {
            boolean login = false;
            @Override
            protected Boolean call() throws Exception {
                if(!isCancelled()) {
                    JeproLabAuthentication.JeproLabAuthenticationOption loginOption = new JeproLabAuthentication.JeproLabAuthenticationOption();
                    loginOption.action = "core.login.admin";
                    login = JeproLabApplication.login(userName.getText(), password.getText(), loginOption);

                    return true;
                }
                return false;
            }

            @Override
            protected void cancelled(){
                super.cancelled();
            }

            @Override
            protected void failed(){
                super.failed();
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateGui(login);
            }
        };


        loginButton.setOnAction(evt -> {
            loginButton.setDisable(true);
            if(!JeproLab.getInstance().is_initialized){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, new Throwable("You should instantiate the application in order to log in and use it "));
            }
            JeproLab.getInstance().executor.submit((Task)worker);
        });

        /* /((Task)worker).setOnFailed( evt -> {
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, worker.getException());
        });*/

        cancelButton.setOnAction(evt -> {
            userName.setText("");
            password.setText("");
            JeproLab.getInstance().resetMenuAndToolBar(false);
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().loginForm);
        });
    }

    private void loginButtonClicked(){


    }

    private void updateGui(boolean login){
        Platform.runLater(() -> {
            if(login) {
                JeproLabContext context = JeproLabContext.getContext();
                context.employee = JeproLabFactory.getEmployee();
                JeproLab.getInstance().resetMenuAndToolBar(true);
                if (context.last_form != null) {
                    JeproLab.getInstance().goToForm(context.last_form);
                } else {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
                }
            } else {
                jeproLabLoginErrorMessageWrapper.setText(bundle.getString("JEPROLAB_YOU_ARE_NOT_ALLOW_TO_LOG_MESSAGE"));
            }
        });
    }


    //todo code me
    public static boolean onUserLogin(JeproLabAuthentication.JeproLabAuthenticationResponse response, JeproLabAuthentication.JeproLabAuthenticationOption loginOptions){
        return true;
    }

    public static void loginFailed(JeproLabAuthentication.JeproLabAuthenticationResponse response){

    }

    public static void onUserLogged(JeproLabAuthentication.JeproLabAuthenticationOption loginOptions){
        Platform.runLater(() -> {
            JeproLabContext context = JeproLabContext.getContext();
            context.employee = loginOptions.employee;
            JeproLab.getInstance().getUserInfoBtn().setText(context.employee.username + " ");
            JeproLab.getInstance().getUserInfoBtn().setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/logout.png"))));
            JeproLab.getInstance().getUserInfoBtn().setContentDisplay(ContentDisplay.RIGHT);
        });
    }

    public static void onUserLoginFailure(JeproLabAuthentication.JeproLabAuthenticationResponse response){

    }
}