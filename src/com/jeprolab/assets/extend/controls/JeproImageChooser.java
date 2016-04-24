package com.jeprolab.assets.extend.controls;

import com.jeprolab.JeproLab;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproImageChooser extends Pane {
    TextField imageContainer;
    Button imageSelector;

    public JeproImageChooser(){
        HBox imageChooserWrapper = new HBox(0);
        imageContainer = new TextField();
        imageContainer.setPrefWidth(220);
        imageSelector = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/generic_folder.png"))));
        imageSelector.getStyleClass().add("icon-btn");
        imageChooserWrapper.getChildren().addAll(imageContainer, imageSelector);
        this.getChildren().addAll(imageChooserWrapper);
    }
}
