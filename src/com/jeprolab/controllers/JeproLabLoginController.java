package com.jeprolab.controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.text.TextAlignment;
import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.core.JeproLabApplication;
import com.jeprolab.models.core.JeproLabAuthenticationOption;
import com.jeprolab.models.core.JeproLabAuthenticationResponse;
import com.jeprolab.models.core.JeproLabFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabLoginController implements Initializable{
    private ResourceBundle bundle;
    private Label loginTitleLabel;

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

    @Override
    public void initialize(URL location , ResourceBundle resource){
        double labelColumnWidth = 150;
        double inputColumnWidth = 200;
        bundle = resource;
        loginTitleLabel = new Label(bundle.getString("JEPROLAB_LOGIN_PANEL_TITLE"));
        loginTitleLabel.setPrefWidth(labelColumnWidth + inputColumnWidth);
        loginTitleLabel.setAlignment(Pos.BASELINE_CENTER);
        jeprolabLoginFormTitleWrapper.setPrefSize(labelColumnWidth + inputColumnWidth, 40);
        loginTitleLabel.setLayoutY(8);
        jeprolabLoginFormTitleWrapper.getChildren().add(loginTitleLabel);
        jeproLabLoginGridPane.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );
        double posX = (JeproLab.APP_WIDTH/2) - (labelColumnWidth + inputColumnWidth)/2;
        jeprolabLoginFormWrapper.setLayoutX(posX);
        double posY = (JeproLab.APP_HEIGHT/4 );
        jeprolabLoginFormWrapper.setLayoutY(posY);

        loginTitleLabel.getStyleClass().add("form-title");
        loginTitleLabel.setTextAlignment(TextAlignment.CENTER);
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
        jeprolabLoginFormContainerWrapper.setLayoutY(40);
        jeprolabLoginFormContainerWrapper.setPadding(new Insets(20, 10, 20, 10));
        jeproLabLoginCommandWrapper.setPrefWidth(labelColumnWidth + inputColumnWidth);
        jeproLabLoginCommandWrapper.setPadding(new Insets(10, 10, 10, 60));
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

    public static void onUserLoginFailure(JeproLabAuthenticationResponse response){

    }
}