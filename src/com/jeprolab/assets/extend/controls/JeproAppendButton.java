package com.jeprolab.assets.extend.controls;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproAppendButton extends HBox{
    private TextField dataField;
    private Button buttonLabel;

    public JeproAppendButton(){
        dataField = new TextField();
        buttonLabel = new Button();
        setSpacing(-1);

        HBox.setMargin(dataField, new Insets(5, 0, 5, 0));
        HBox.setMargin(buttonLabel, new Insets(5, 0, 5, 0));

        getChildren().addAll(dataField, buttonLabel);
        dataField.getStyleClass().addAll("append-button-field");
        dataField.setPrefWidth(130);
        buttonLabel.getStyleClass().addAll("append-button-btn");
        buttonLabel.setDisable(true);
    }

    public void setValue(String value){
        dataField.setText(value);
    }

    public void setButtonLabel(String label){
        buttonLabel.setText(label);
    }

    public String getValue(){ return dataField.getText(); }
}
