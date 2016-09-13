package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabFeedAddController extends JeproLabController {
    private Button saveFeedBtn, cancelBtn;

    @FXML
    public Label jeproLabFeedTitleLabel, jeproLabFeedLinkLabel, jeproLabFeedDescriptionLabel, jeproLabFeedAuthorLabel;
    public TextField jeproLabFeedAuthor, jeproLabFeedLink;
    public JeproMultiLangTextField jeproLabFeedTitle;
    public JeproMultiLangTextArea jeproLabFeedDescription;
    public GridPane jeproLabFeedLayout;
    public JeproFormPanel jeproLabFeedEditForm;
    public JeproFormPanelTitle jeproLabFeedPaneTitle;
    public JeproFormPanelContainer jeproLabFeedPaneContainer;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        double labelColumnWidth = 150;
        double inputColumnWidth = 450;
        formWidth = (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabFeedLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth - 25), new ColumnConstraints(inputColumnWidth - 25)
        );

        jeproLabFeedEditForm.setLayoutX(posX);
        jeproLabFeedEditForm.setLayoutY(posY);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_FEED_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        jeproLabFeedPaneTitle.setPrefSize(formWidth, 40);
        jeproLabFeedPaneTitle.getChildren().add(formTitleLabel);

        jeproLabFeedPaneContainer.setPrefWidth(formWidth);
        jeproLabFeedPaneContainer.setLayoutY(40);

        jeproLabFeedTitleLabel.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        jeproLabFeedTitleLabel.getStyleClass().add("input-label");
        jeproLabFeedLinkLabel.setText(bundle.getString("JEPROLAB_LINK_LABEL"));
        jeproLabFeedLinkLabel.getStyleClass().add("input-label");
        jeproLabFeedDescriptionLabel.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabFeedDescriptionLabel.getStyleClass().add("input-label");
        jeproLabFeedAuthorLabel.setText(bundle.getString("JEPROLAB_AUTHOR_LABEL"));
        jeproLabFeedAuthorLabel.getStyleClass().add("input-label");

        jeproLabFeedTitle.setWidth(480);
        jeproLabFeedDescription.setTextPrefSize(480, 100);

        GridPane.setMargin(jeproLabFeedTitleLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabFeedTitle, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabFeedLinkLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabFeedLink, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabFeedDescriptionLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabFeedDescription, new Insets(15, 10, 15, 10));
        GridPane.setMargin(jeproLabFeedAuthorLabel, new Insets(15, 10, 15, 20));
        GridPane.setMargin(jeproLabFeedAuthor, new Insets(15, 10, 15, 10));
        GridPane.setValignment(jeproLabFeedDescriptionLabel, VPos.TOP);
    }

    @Override
    public void initializeContent(){
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(5);
        saveFeedBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveFeedBtn, cancelBtn);
    }
}
