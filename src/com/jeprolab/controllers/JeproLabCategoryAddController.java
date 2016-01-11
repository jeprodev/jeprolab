package com.jeprolab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
    public Label jeproLabCategoryMetaKeyWordLabel, jeproLabCategoryLinkRewriteLabel, jeproLabCategoryIsRootLabel, jeproLabCategoryAssociatedShopLabel;
    public Label jeproLabCategoryAllowedGroupLabel, jeproLabCategoryLabel;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        jeproLabCategoryNameLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabPublishedCategoryLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryParentLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryDescriptionLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryImageChooserLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryMetaTileLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryMetaDescriptionLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryMetaKeyWordLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryLinkRewriteLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryIsRootLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryAssociatedShopLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryAllowedGroupLabel.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabCategoryLabel.setText(bundle.getString("JEPROLAB_LABEL"));
    }
}