package com.jeprolab.assets.tools;

import com.jeprolab.JeproLab;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproWindowsButtons extends HBox {
    private final Stage stage;

    public JeproWindowsButtons(final Stage stag){
        super(0);
        this.stage = stag;
        ImageView closeImageView = new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/close_btn.png")));
        Button closeButton = new Button("", closeImageView);
        closeButton.setPrefSize(12, 12);
        closeButton.setId("window-close");

        closeButton.setOnMouseClicked(actionEvent -> Platform.exit());

        ImageView minimizeImageView = new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/minimize_btn.png")));
        Button minimizeButton = new Button("", minimizeImageView);
        minimizeButton.setPrefSize(12, 12);
        minimizeButton.setId("window-minimize");
        minimizeButton.setOnMouseClicked(event -> stage.setIconified(true));

        closeButton.getStyleClass().add("window-command-btn");
        minimizeButton.getStyleClass().add("window-command-btn");
        this.getChildren().addAll(closeButton, minimizeButton);
    }
}
