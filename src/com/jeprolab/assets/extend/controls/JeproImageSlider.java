package com.jeprolab.assets.extend.controls;

import com.jeprolab.JeproLab;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


/**
 *
 * Created by jeprodev on 12/01/2014.
 */
public class JeproImageSlider extends HBox {
    private Button previousBtn, nextBtn;
    private Pane imageWrapper;

    public JeproImageSlider(){
        previousBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/previous.png"))));
        nextBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/next.png"))));
        imageWrapper = new Pane();
        this.getChildren().addAll(previousBtn, imageWrapper, nextBtn);
    }

    public void setSliderPrefHeight(double height){
        this.setPrefHeight(height);
        this.previousBtn.setPrefHeight(height);
        this.nextBtn.setPrefHeight(height);
    }

    public void setSliderPrefWidth(double width){
        this.setPrefWidth(width);
        this.imageWrapper.setPrefWidth(width - 2 * (previousBtn.getWidth()) - 1);
    }
}