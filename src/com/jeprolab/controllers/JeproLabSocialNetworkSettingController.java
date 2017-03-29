package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 28/03/2017.
 */
public class JeproLabSocialNetworkSettingController extends JeproLabController{
    private Button saveConfigBtn;
    @FXML
    public JeproFormPanel jeproLabSocialNetworkSettingFormWrapper;
    public JeproFormPanelContainer jeproLabSocialNetworkSettingFormContentWrapper;
    public JeproFormPanelTitle jeproLabSocialNetworkSettingFormTitleWrapper;
    public TabPane jeproLabSocialNetworkSettingTabPane;
    public Tab jeproLabFaceBookSettingTab, jeproLabTwitterSettingTab, jeproLabSettingTab;
    public GridPane jeproLabFaceBookSettingLayout, jeproLabTwitterSettingLayout;
    public Label jeproLabFacebookSettingIdFieldLabel, jeproLabFacebookSettingAccessTokenUrlFieldLabel, jeproLabLabel;
    public Label jeproLabFacebookApplicationPageFieldLabel;
    public TextField jeproLabFacebookSettingIdField, jeproLabFacebookApplicationPageField, jeproLab;
    public TextArea jeproLabFacebookSettingAccessTokenUrlField;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        jeproLabSocialNetworkSettingFormWrapper.setPrefWidth(formWidth);
        jeproLabSocialNetworkSettingFormWrapper.setLayoutY(15);
        jeproLabSocialNetworkSettingFormWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabSocialNetworkSettingFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabSocialNetworkSettingFormContentWrapper.setLayoutY(40);
        jeproLabSocialNetworkSettingFormContentWrapper.setPrefWidth(formWidth);

        jeproLabSocialNetworkSettingTabPane.setPrefWidth(formWidth);
        jeproLabFaceBookSettingTab.setText(bundle.getString("JEPROLAB_FACEBOOK_LABEL"));
        jeproLabFaceBookSettingTab.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/globe.png"))));

        jeproLabFaceBookSettingLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150), new ColumnConstraints(formWidth - 180)
        );
        jeproLabFaceBookSettingLayout.setLayoutX(15);

        Insets labelInsets = new Insets(10, 10, 5, 20);
        Insets inputInsets = new Insets(10, 0, 5, 0);

        jeproLabFacebookSettingIdFieldLabel.setText(bundle.getString("JEPROLAB_APPLICATION_FACEBOOK_ID_LABEL"));
        jeproLabFacebookSettingIdField.setDisable(true);

        GridPane.setMargin(jeproLabFacebookSettingIdFieldLabel, labelInsets);
        GridPane.setMargin(jeproLabFacebookSettingIdField, inputInsets);
        GridPane.setMargin(jeproLabFacebookSettingAccessTokenUrlFieldLabel, labelInsets);
        GridPane.setValignment(jeproLabFacebookSettingAccessTokenUrlFieldLabel, VPos.TOP);
        GridPane.setMargin(jeproLabFacebookSettingAccessTokenUrlField, inputInsets);
        GridPane.setMargin(jeproLabLabel, labelInsets);
        GridPane.setMargin(jeproLab, inputInsets);

        //twitter
        jeproLabTwitterSettingTab.setText(bundle.getString("JEPROLAB_TWITTER_LABEL"));
        jeproLabTwitterSettingTab.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/globe.png"))));

        jeproLabTwitterSettingLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150), new ColumnConstraints(formWidth - 10)
        );
        jeproLabTwitterSettingLayout.setLayoutX(15);

        /*jeproLabTwitterSetting
        jeproLabTwitterSetting
        jeproLabTwitterSetting*/

    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception{
                if(isCancelled()){
                    return false;
                }

                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.succeeded();
            }
        };
        JeproLab.getInstance().executor.submit((Task)worker);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveConfigBtn = new Button(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        saveConfigBtn.setDisable(true);
        saveConfigBtn.getStyleClass().add("save-btn");
        commandWrapper.getChildren().addAll(saveConfigBtn);
    }

    private void addEventListeners(){
        saveConfigBtn.setOnAction(evt -> {

        });
    }
}
