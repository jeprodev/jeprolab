package com.jeprolab.views.modules;

import com.jeprolab.JeproLab;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * Created by jeprodev on 25/03/2016.
 */
public class JeproLabDashboardModule extends Pane {
    protected Pane formTitleWrapper, formContentWrapper;
    protected HBox titleContentWrapper;
    protected Label titleWrapper;

    public JeproLabDashboardModule(){
        formContentWrapper = new Pane();
        formTitleWrapper = new Pane();
        titleContentWrapper = new HBox(5);
        formTitleWrapper.getChildren().add(titleContentWrapper);

        titleWrapper = new Label();
        titleWrapper.getStyleClass().add("module-label");
        formTitleWrapper.getStyleClass().add("module-title-container");
        formContentWrapper.getStyleClass().add("module-panel-container");
        this.getChildren().addAll(formTitleWrapper, formContentWrapper);
    }

    public JeproLabDashboardModule(String formTitle, String iconPath){
        formContentWrapper = new Pane();
        formTitleWrapper = new Pane();
        titleContentWrapper = new HBox(5);
        formTitleWrapper.getChildren().add(titleContentWrapper);

        titleWrapper = new Label();
        titleWrapper.getStyleClass().add("module-label");
        formTitleWrapper.getStyleClass().add("module-title-container");
        formContentWrapper.getStyleClass().add("module-panel-container");
        this.getChildren().addAll(formTitleWrapper, formContentWrapper);

        titleWrapper.setText(formTitle);
        ImageView icon = new ImageView(new Image(JeproLab.class.getResourceAsStream(iconPath)));
        titleContentWrapper.getChildren().addAll(icon, titleWrapper);
    }

    protected void setTitleAndIcon(String formTitle, String iconPath){
        titleWrapper.setText(formTitle);
        ImageView icon = new ImageView(new Image(JeproLab.class.getResourceAsStream(iconPath)));
        titleContentWrapper.getChildren().addAll(icon, titleWrapper);
    }

    public void setModuleSize(double width, double height){
        this.setPrefSize(width, height);
        this.formContentWrapper.setLayoutY(20);
        this.formContentWrapper.setPrefSize(width, height - 20);
        this.formTitleWrapper.setPrefSize(width, 20);
    }

    public void initializeContent(){}
}
