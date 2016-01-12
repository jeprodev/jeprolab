package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    public JeproFormPanel jeproLabCategoryFormWrapper;
    public JeproFormPanelTitle jeproLabCategoryFormTitleWrapper;
    public JeproFormPanelContainer jeproLabCategoryFormContainerWrapper;
    public GridPane jeproLabCategoryFormLayout;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabCategoryFormLayout.getColumnConstraints().addAll(new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25));

        jeproLabAddCategoryFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " +  bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabCategoryNameLabel.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        jeproLabPublishedCategoryLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabCategoryParentLabel.setText(bundle.getString("JEPROLAB_PARENT_LABEL"));
        jeproLabCategoryDescriptionLabel.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabCategoryImageChooserLabel.setText(bundle.getString("JEPROLAB_CHOOSE_IMAGE_LABEL"));
        jeproLabCategoryMetaTileLabel.setText(bundle.getString("JEPROLAB_META_TITLE_LABEL"));
        jeproLabCategoryMetaDescriptionLabel.setText(bundle.getString("JEPROLAB_META_DESCRIPTION_LABEL"));
        jeproLabCategoryMetaKeyWordLabel.setText(bundle.getString("JEPROLAB_META_KEYWORD_LABEL"));
        jeproLabCategoryLinkRewriteLabel.setText(bundle.getString("JEPROLAB_LINK_REWRITE_LABEL"));
        jeproLabCategoryIsRootLabel.setText(bundle.getString("JEPROLAB_IS_ROOT_LABEL"));
        jeproLabCategoryAssociatedLabsLabel.setText(bundle.getString("JEPROLAB_ASSOCIATED_LABORATORIES_LABEL"));
        jeproLabCategoryAllowedGroupLabel.setText(bundle.getString("JEPROLAB_ALLOWED_GROUP_LABEL"));
        jeproLabCategoryLabel.setText(bundle.getString("JEPROLAB_LABEL"));
    }
}