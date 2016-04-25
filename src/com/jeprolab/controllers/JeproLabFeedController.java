package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabFeedController extends JeproLabController {
    private Button addFeedBtn;
    private CheckBox checkAll;
    @FXML
    public TableView<JeproLabFeedRecord> jeproLabFeedTableView;
    public TableColumn jeproLabFeedIndexColumn, jeproLabFeedCheckBoxColumn, jeproLabFeedTitleColumn, jeproLabFeedAuthorColumn, jeproLabFeedDescriptionColumn, jeproLabFeedActionColumn;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 57;

        jeproLabFeedTableView.setPrefSize(formWidth, 600);
        jeproLabFeedTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);

        jeproLabFeedIndexColumn.setText("#");
        jeproLabFeedIndexColumn.setPrefWidth(35);
        checkAll = new CheckBox();
        jeproLabFeedCheckBoxColumn.setGraphic(checkAll);
        jeproLabFeedCheckBoxColumn.setPrefWidth(22);
        jeproLabFeedTitleColumn.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        jeproLabFeedTitleColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabFeedAuthorColumn.setText(bundle.getString("JEPROLAB_AUTHOR_LABEL"));
        jeproLabFeedAuthorColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabFeedDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        tableCellAlign(jeproLabFeedDescriptionColumn, Pos.CENTER_LEFT);
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

    public static class JeproLabFeedRecord {

    }
}