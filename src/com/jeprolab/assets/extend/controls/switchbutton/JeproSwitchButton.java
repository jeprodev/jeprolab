package com.jeprolab.assets.extend.controls.switchbutton;


import com.jeprolab.JeproLab;
import javafx.scene.control.CheckBox;

/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public class JeproSwitchButton extends CheckBox {
    public JeproSwitchButton(){
        this("");
    }

    public JeproSwitchButton(final String label){
        super(label);
        getStylesheets().add(JeproLab.class.getResource("assets/css/switchbutton.css").toExternalForm());
        setSkin(new JeproSwitchButtonSkin(this));
    }
}