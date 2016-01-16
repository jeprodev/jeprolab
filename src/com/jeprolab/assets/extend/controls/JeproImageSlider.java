package com.jeprolab.assets.extend.controls;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;


/**
 *
 * Created by jeprodev on 12/01/2014.
 */
public class JeproImageSlider extends HBox {
    private Button previousBtn, nextBtn;

    public JeproImageSlider(){
        previousBtn = new Button();
        nextBtn = new Button();
        this.getChildren().addAll(previousBtn, nextBtn);
    }
}