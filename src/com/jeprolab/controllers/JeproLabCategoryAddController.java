package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.extend.controls.tree.JeproCategoryTree;
import com.jeprolab.assets.tools.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCategoryModel;
import com.jeprolab.models.JeproLabGroupModel;
import com.jeprolab.models.JeproLabSettingModel;
import com.jeprolab.models.image.JeproLabImageManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.URL;
import java.util.List;
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
    public Label jeproLabCategoryAllowedGroupLabel, jeproLabCategoryLabel, jeproLabAddCategoryFormTitle, jeproLabCategoryInformationLabel;
    public JeproMultiLangTextArea jeproLabCategoryDescription;
    public JeproCategoryTree jeproLabCategoryParent;
    public JeproFormPanel jeproLabCategoryFormWrapper;
    public JeproFormPanelTitle jeproLabCategoryFormTitleWrapper;
    public JeproFormPanelContainer jeproLabCategoryFormContainerWrapper;
    public JeproSwitchButton jeproLabPublishedCategory, jeproLabCategoryIsRoot;
    public JeproMultiLangTextField jeproLabCategoryName, jeproLabCategoryMetaTile,  jeproLabCategoryMetaKeyWord;
    public JeproMultiLangTextField jeproLabCategoryMetaDescription, jeproLabCategoryLinkRewrite;
    public GridPane jeproLabCategoryFormLayout, jeproLabCategoryAssociatedLaboratoriesLayout, jeproLabCategoryAssociatedGroupLayout;
    public TabPane jeproLabCategoryTabPane;
    public Tab jeproLabCategoryInformationTab, jeproLabCategoryAssociatedLaboratoriesTab, jeproLabCategoryAssociatedGroupTab;

    public JeproImageChooser jeproLabCategoryImageChooser;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
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
        jeproLabCategoryFormLayout.setLayoutY(15);
        jeproLabCategoryTabPane.setPrefWidth(formWidth);

        jeproLabCategoryInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabCategoryInformationLabel.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabCategoryAssociatedLaboratoriesTab.setText(bundle.getString("JEPROLAB_ASSOCIATED_SHOPS_LABEL"));
        jeproLabCategoryAssociatedGroupTab.setText(bundle.getString("JEPROLAB_ASSOCIATED_GROUPS_LABEL"));
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

        jeproLabCategoryDescription.setTextAreaPrefSize(760, 90);

        GridPane.setMargin(jeproLabCategoryNameLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabPublishedCategoryLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryParentLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryDescriptionLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryImageChooserLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryMetaTileLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryMetaDescriptionLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryMetaKeyWordLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryLinkRewriteLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryIsRootLabel, new Insets(5, 0, 15, 20));
        GridPane.setMargin(jeproLabCategoryAssociatedLabsLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryAllowedGroupLabel, new Insets(5, 0, 15, 10));
        GridPane.setMargin(jeproLabCategoryLabel, new Insets(5, 0, 15, 0));
        GridPane.setMargin(jeproLabCategoryDescription, new Insets(10, 0, 15, 0));

        initializeContent();
    }

    @Override
    protected void initializeContent(){
        if(context == null) {
            context = JeproLabContext.getContext();
        }
        int labId = context.laboratory.laboratory_id;
        JeproLabCategoryModel category = this.loadCategory();
        if(category != null){
            int selectedCategoryId;
            if((category.parent_id > 0) && category.isParentCategoryAvailable(labId)){
               selectedCategoryId = category.parent_id;
            }else {
                selectedCategoryId = JeproLab.request.getIntValue("parent_id", JeproLabCategoryModel.getRootCategory().category_id);
            }
            String imagePath = JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY +  category.category_id + ".jpg";
            String imageLink = JeproLabImageManager.thumbNail(imagePath, "category_" + category.category_id + ".jpg", 350, "jpg", true, true);
            File imageFile = new File(imagePath);
            int imageSize = (imageFile.exists() && !imageFile.isDirectory()) ? (int)(imageFile.length()/1000) : 0;

            boolean sharedCategory = ((category.category_id > 0) &&  category.hasMultiLabEntries();

        }

        JeproLabGroupModel unidentifiedGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("unidentified_group"));
        JeproLabGroupModel guestGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("guest_group"));
        JeproLabGroupModel customerGroup = new JeproLabGroupModel(JeproLabSettingModel.getIntValue("customer_group"));

        List<JeproLabGroupModel> groups = JeproLabGroupModel.getGroups(context.language.language_id);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveCategoryBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"));
        commandWrapper.getChildren().addAll(saveCategoryBtn);
    }

    private JeproLabCategoryModel loadCategory(){
        int categoryId = JeproLab.request.getIntValue("category_id");
        if(context == null) {
            context = JeproLabContext.getContext();
        }

        JeproLabCategoryModel category;
        if(categoryId > 0){
            category = new JeproLabCategoryModel(categoryId);
            if(category.category_id <= 0){
                JeproLabTools.displayError(500, bundle.getString("JEPROLAB_CATEGORY_NOT_FOUND_MESSAGE"));
                return null;
            }
            return category;
        }else{
            JeproLabTools.displayError(500, bundle.getString("JEPROLAB_CATEGORY_DOES_NOT_EXIST_MESSAGE"));
            return null;
        }
    }
}