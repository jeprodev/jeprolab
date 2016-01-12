package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 11/01/2014.
 */
public class JeproLabCategoryAddController extends JeproLabController{
    @FXML
    public Label jeproLabCategoryNameLabel, jeproLabPublishedCategoryLabel, jeproLabCategoryParentLabel, jeproLabCategoryDescriptionLabel;
    public Label jeproLabCategoryImageChooserLabel, jeproLabCategoryMetaTileLabel, jeproLabCategoryMetaDescriptionLabel;
    public Label jeproLabCategoryMetaKeyWordLabel, jeproLabCategoryLinkRewriteLabel, jeproLabCategoryIsRootLabel, jeproLabCategoryAssociatedLabsLabel;
    public Label jeproLabCategoryAllowedGroupLabel, jeproLabCategoryLabel, jeproLabAddCategoryFormTitle;
    public TextArea jeproLabCategoryDescription;

    public JeproFormPanel jeproLabCategoryFormWrapper;
    public JeproFormPanelTitle jeproLabCategoryFormTitleWrapper;
    public JeproFormPanelContainer jeproLabCategoryFormContainerWrapper;
    public JeproSwitchButton jeproLabPublishedCategory;
    public JeproMultiLangTextField jeproLabCategoryName;
    public GridPane jeproLabCategoryFormLayout;

    public JeproImageChooser jeproLabCategoryImageChooser;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = 2 * (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;
        jeproLabCategoryFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabCategoryFormWrapper.setLayoutX(posX);
        jeproLabCategoryFormWrapper.setLayoutY(posY);

        jeproLabCategoryFormContainerWrapper.setLayoutY(40);
        jeproLabCategoryFormContainerWrapper.setPrefWidth(formWidth);

        jeproLabCategoryFormLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25),
                new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25)
        );
        jeproLabCategoryFormLayout.setLayoutX(15);

        jeproLabAddCategoryFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " +  bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabCategoryNameLabel.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        jeproLabCategoryNameLabel.getStyleClass().add("input-label");
        jeproLabPublishedCategoryLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabPublishedCategoryLabel.getStyleClass().add("input-label");
        jeproLabCategoryParentLabel.setText(bundle.getString("JEPROLAB_PARENT_LABEL"));
        jeproLabCategoryParentLabel.getStyleClass().add("input-label");
        jeproLabCategoryDescriptionLabel.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabCategoryDescriptionLabel.getStyleClass().add("input-label");
        jeproLabCategoryImageChooserLabel.setText(bundle.getString("JEPROLAB_CHOOSE_IMAGE_LABEL"));
        jeproLabCategoryImageChooserLabel.getStyleClass().add("input-label");
        jeproLabCategoryMetaTileLabel.setText(bundle.getString("JEPROLAB_META_TITLE_LABEL"));
        jeproLabCategoryMetaTileLabel.getStyleClass().add("input-label");
        jeproLabCategoryMetaDescriptionLabel.setText(bundle.getString("JEPROLAB_META_DESCRIPTION_LABEL"));
        jeproLabCategoryMetaDescriptionLabel.getStyleClass().add("input-label");
        jeproLabCategoryMetaKeyWordLabel.setText(bundle.getString("JEPROLAB_META_KEYWORD_LABEL"));
        jeproLabCategoryMetaKeyWordLabel.getStyleClass().add("input-label");
        jeproLabCategoryLinkRewriteLabel.setText(bundle.getString("JEPROLAB_LINK_REWRITE_LABEL"));
        jeproLabCategoryLinkRewriteLabel.getStyleClass().add("input-label");
        jeproLabCategoryIsRootLabel.setText(bundle.getString("JEPROLAB_IS_ROOT_LABEL"));
        jeproLabCategoryIsRootLabel.getStyleClass().add("input-label");
        jeproLabCategoryAssociatedLabsLabel.setText(bundle.getString("JEPROLAB_ASSOCIATED_LABORATORIES_LABEL"));
        jeproLabCategoryAssociatedLabsLabel.getStyleClass().add("input-label");
        jeproLabCategoryAllowedGroupLabel.setText(bundle.getString("JEPROLAB_ALLOWED_GROUP_LABEL"));
        jeproLabCategoryAllowedGroupLabel.getStyleClass().add("input-label");
        jeproLabCategoryLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryLabel.getStyleClass().add("input-label");

        GridPane.setMargin(jeproLabCategoryNameLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabPublishedCategoryLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryParentLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryDescriptionLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryImageChooserLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryMetaTileLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryMetaDescriptionLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryMetaKeyWordLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryLinkRewriteLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryIsRootLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryAssociatedLabsLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryAllowedGroupLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryLabel, new Insets(5, 0, 15, 0));
    }
}