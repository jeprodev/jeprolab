package com.jeprolab.views;


import com.jeprolab.JeproLab;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabSplashForm extends StackPane{
    private ProgressBar progressBar;
    // /private Timeline
    public JeproLabSplashForm(){
        progressBar = new ProgressBar();
        ImageView backgroundImage = new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/splash-screen.jpg")));
        Pane progressBarWrapper = new Pane();
        progressBar.setLayoutX(10);
        progressBar.setLayoutY(200);
        progressBar.setPrefSize(JeproLab.SPLASH_WIDTH - 20, 5);
        progressBarWrapper.getChildren().add(progressBar);
        this.getChildren().addAll(backgroundImage, progressBarWrapper);
        //JeproLab.getInstance()
    }


    public void createView(){
        JeproLab.getInstance().getApplicationForms().renderForms();
        progressBar.progressProperty().bind(JeproLab.getInstance().getApplicationForms().worker.progressProperty());
        new Thread((Runnable)JeproLab.getInstance().getApplicationForms().worker).start();
    }
}
