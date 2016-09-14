package com.jeprolab.assets.extend.controls;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproPriceBox  extends HBox {
    private TextField priceBox;
    private Button currencyButton;

    public JeproPriceBox(){
        priceBox = new TextField();
        priceBox.setStyle("-fx-background-radius: 0, 0;");
        currencyButton = new Button("€");
        setSpacing(0);
        this.getChildren().addAll(priceBox, currencyButton);
    }
}
