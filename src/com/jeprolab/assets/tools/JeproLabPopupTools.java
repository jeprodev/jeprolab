package com.jeprolab.assets.tools;


import com.jeprolab.JeproLab;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import org.apache.log4j.Level;

public class JeproLabPopupTools {
    private static final double POPUP_WIDTH = 300;
    private static final double POPUP_HEIGHT = 250;
    private static Popup messagePopup;
    private static Label messageTitle;
    private static VBox contentWrapper;
    private static HBox commandWrapper, informationWrapper;
    private static Button noButton, yesButton, cancelButton;
    private static Alert warningAlert;

    public static void getAlert(Alert.AlertType type, String title, String headerText, String message){
        warningAlert =  new Alert(type);
        warningAlert.setTitle(title);
        warningAlert.setHeaderText(headerText);
        warningAlert.setContentText(message);
        warningAlert.showAndWait();
    }

    public static void displayMessage(Level level, String title, String message){
        if (messagePopup == null) {
            createPopupGui();
        }

        messageTitle.setText(title);

        setInformationAndCommandContent(level, message);


        messagePopup.show(JeproLab.getInstance().getApplicationStage());
    }

    private static void setInformationAndCommandContent(Level level, String message){
        Text informationText = new Text(message);
        informationText.setWrappingWidth(240);
        informationText.setTextAlignment(TextAlignment.CENTER);

        if(level.equals(Level.INFO)){
            commandWrapper.getChildren().addAll(noButton, yesButton);
        }else if(level.equals(Level.WARN)){
            commandWrapper.getChildren().addAll(noButton, yesButton);
        }else if(level.equals(Level.ERROR)){
            commandWrapper.getChildren().addAll(noButton, yesButton);
        }else if(level.equals(Level.DEBUG)){
            commandWrapper.getChildren().addAll(noButton, yesButton);
        }else if(level.equals(Level.FATAL)){
            commandWrapper.getChildren().addAll(noButton, yesButton);
        }

        commandWrapper.getChildren().add(cancelButton);
    }

    private static void createPopupGui(){
        messagePopup = new Popup();
        messagePopup.setWidth(POPUP_WIDTH);
        messagePopup.setHeight(POPUP_HEIGHT);
        messagePopup.setX(JeproLab.getInstance().getApplicationStage().getX() + ((JeproLab.APP_WIDTH - POPUP_WIDTH)/2));
        messagePopup.setY(JeproLab.getInstance().getApplicationStage().getY() + ((JeproLab.APP_HEIGHT - POPUP_HEIGHT)/2));
        //messagePopup.

        contentWrapper = new VBox(5);

        messageTitle = new Label();
        commandWrapper = new HBox(8);
        informationWrapper = new HBox(2);

        noButton = new Button(JeproLab.getBundle().getString("JEPROLAB_NO_LABEL"));
        yesButton = new Button(JeproLab.getBundle().getString("JEPROLAB_OK_LABEL"));
        cancelButton = new Button(JeproLab.getBundle().getString("JEPROLAB_CANCEL_LABEL"));

        contentWrapper.getChildren().addAll(messageTitle, informationWrapper, commandWrapper);
        messagePopup.getContent().addAll(contentWrapper);
    }
}
