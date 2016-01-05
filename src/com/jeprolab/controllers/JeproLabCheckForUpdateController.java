package com.jeprolab.controllers;

import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabUpdater;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/01/2015.
 */
public class JeproLabCheckForUpdateController extends JeproLabController{
    @FXML
    public JeproFormPanel jeproLabCheckForUpdateWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        Text versionWrapper = new Text();
        versionWrapper.setLayoutY(10);
        versionWrapper.setLayoutY(10);
        jeproLabCheckForUpdateWrapper.getChildren().add(versionWrapper);
        try{
            String data = JeproLabUpdater.getData("http://jeprodev/index.php");
            versionWrapper.setText(data);
            //System.out.println(JeproLabUpdater.getLatestVersion());
        }catch (Exception exp){ exp.printStackTrace();}
    }
}