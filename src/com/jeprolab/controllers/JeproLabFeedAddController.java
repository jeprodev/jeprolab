package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeedAddController extends JeproLabController{
    private Label jeproLabEditFeedLabel;
    @FXML
    public JeproFormPanel jeproLabFeedEditForm;
    public JeproFormPanelTitle jeproLabFeedPaneTitle;
    public JeproFormPanelContainer jeproLabFeedPaneContainer;
    public GridPane jeproLabFeedLayout;
    public Label jeproLabFeedTitleLabel, jeproLabFeedLinkLabel, jeproLabFeedDescriptionLabel, jeproLabFeedAuthorLabel;
    public TextField jeproLabFeedAuthor;
    public JeproMultiLangTextField jeproLabFeedTitle, jeproLabFeedLink;
    public JeproMultiLangTextArea jeproLabFeedDescription;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 720;

        jeproLabFeedLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150), new ColumnConstraints(560)
        );

        jeproLabFeedEditForm.setLayoutX((JeproLab.APP_WIDTH - 710)/2);
        jeproLabFeedEditForm.setLayoutY(60);
        jeproLabEditFeedLabel = new Label(bundle.getString("JEPROLAB_ADD_NEW_FEED_LABEL"));
        jeproLabEditFeedLabel.setPrefSize(formWidth, 40);
        jeproLabEditFeedLabel.getStyleClass().add("form-title");
        jeproLabFeedPaneTitle.setPrefSize(formWidth, 40);
        jeproLabFeedPaneTitle.getChildren().add(jeproLabEditFeedLabel);

        jeproLabFeedPaneContainer.setLayoutY(40);
        jeproLabFeedPaneContainer.setPrefSize(formWidth, 350);

        jeproLabFeedTitleLabel.setText(bundle.getString("JEPROLAB_FEED_TITLE_LABEL"));
        jeproLabFeedLinkLabel.setText(bundle.getString("JEPROLAB_FEED_LINK_LABEL"));
        jeproLabFeedDescriptionLabel.setText(bundle.getString("JEPROLAB_FEED_DESCRIPTION_LABEL"));
        jeproLabFeedAuthorLabel.setText(bundle.getString("JEPROLAB_FEED_AUTHOR_LABEL"));

        GridPane.setMargin(jeproLabFeedTitleLabel,new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabFeedTitle,new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabFeedAuthorLabel,new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabFeedAuthor,new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabFeedLinkLabel,new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabFeedLink,new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabFeedDescriptionLabel,new Insets(5, 10, 5, 15));
        GridPane.setValignment(jeproLabFeedDescriptionLabel, VPos.TOP);
        GridPane.setMargin(jeproLabFeedDescription,new Insets(5, 10, 5, 0));
    }

    @Override
    public void initializeContent(int feedId){
        if(feedId > 0){

        }
    }
}
