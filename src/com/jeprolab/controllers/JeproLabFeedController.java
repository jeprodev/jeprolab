package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 19/12/2014.
 */
public class JeproLabFeedController extends JeproLabController {
    private Button addFeedBtn;
    @FXML
    public TableView jeproLabFeedTableView;
    public TableColumn jeproLabFeedIndexColumn, jeproLabFeedCheckBoxColumn, jeproLabFeedTitleColumn, jeproLabFeedAuthorColumn, jeproLabFeedDescriptionColumn, jeproLabFeedActionColumn;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double tableViewWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = tableViewWidth - 60;
        jeproLabFeedTableView.setPrefWidth(tableViewWidth);
        jeproLabFeedTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);

        jeproLabFeedIndexColumn.setText("#");
        jeproLabFeedIndexColumn.setPrefWidth(35);
        jeproLabFeedCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedCheckBoxColumn.setPrefWidth(25);
        jeproLabFeedTitleColumn.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        jeproLabFeedTitleColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabFeedAuthorColumn.setText(bundle.getString("JEPROLAB_AUTHOR_LABEL"));
        jeproLabFeedAuthorColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabFeedDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabFeedDescriptionColumn.getStyleClass().add("text-left");
        jeproLabFeedDescriptionColumn.setPrefWidth(.6 * remainingWidth);
        jeproLabFeedActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabFeedActionColumn.setPrefWidth(0.1 * remainingWidth);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addFeedBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addFeedBtn);
    }
}