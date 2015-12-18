package com.jeprolab.views.installer;

import com.jeprolab.JeproLab;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public abstract class JeproLabInstallerForm extends VBox {
    public final Label formTitle;
    protected ResourceBundle bundle;
    protected Pane formWrapper = null;
    public boolean isCreated = false;

    public JeproLabInstallerForm(String name){
        this.formTitle = new Label(name);
        this.formTitle.getStyleClass().setAll("form-title");
        this.bundle = JeproLab.getBundle();
    }

    public abstract Node createView();
}
