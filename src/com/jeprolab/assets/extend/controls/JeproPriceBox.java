package com.jeprolab.assets.extend.controls;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproPriceBox extends HBox {
    private TextField priceBox;
    private Button currencyButton;

    public JeproPriceBox(){
        priceBox = new TextField();
        priceBox.setStyle("-fx-background-radius: 0, 0;");
        currencyButton = new Button("€");
        setSpacing(0);
        this.getChildren().addAll(priceBox, currencyButton);
    }

    public float getPrice(){
        return Float.parseFloat(priceBox.getText());
    }
}
