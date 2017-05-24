package com.jeprolab.assets.extend.controls;

import javafx.geometry.Insets;
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

        currencyButton = new Button("");
        setSpacing(-1);

        HBox.setMargin(priceBox, new Insets(5, 0, 5, 0));
        HBox.setMargin(currencyButton, new Insets(5, 0, 5, 0));
        this.getChildren().addAll(priceBox, currencyButton);

        priceBox.getStyleClass().addAll("append-button-field");
        priceBox.setPrefWidth(130);
        currencyButton.getStyleClass().addAll("append-button-btn");
    }

    public float getPrice(){
        return Float.parseFloat(priceBox.getText());
    }
}
