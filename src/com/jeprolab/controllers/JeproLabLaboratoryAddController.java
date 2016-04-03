package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 07/01/15.
 */
public class JeproLabLaboratoryAddController extends JeproLabController {
    private JeproLabLaboratoryModel laboratory;
    @FXML
    public JeproFormPanel jeproLabLaboratoryPanelWrapper;
    public JeproFormPanelTitle jeproLabLaboratoryPanelTitleWrapper;
    public JeproFormPanelContainer jeproLabLaboratoryPanelContainerWrapper;

    public TabPane jeproLabLaboratoryTab;
    public Tab jeproLabLaboratoryInformation, jeproLabLaboratoryUrls;
    public Label jeproLabLaboratoryNameLabel, jeproLabLaboratoryGroupLabel, jeproLabLaboratoryCategoryLabel, jeproLabLaboratoryThemeLabel;
    public Label jeproLabLaboratoryPublishedLabel, jeproLabLaboratoryDomainLabel, jeproLabLaboratorySslDomainLabel, jeproLabLaboratoryPhysicalUriLabel;
    public Label jeproLabLaboratoryMainUrlLabel, jeproLabLaboratoryVirtualUriLabel;

    public TextField jeproLabLaboratoryName, jeproLabLaboratoryDomain, jeproLabLaboratorySslDomain, jeproLabLaboratoryPhysicalUri;
    public TextField jeproLabLaboratoryVirtualUri;
    public ComboBox<String> jeproLabLaboratoryGroup, jeproLabLaboratoryCategory, jeproLabLaboratoryTheme;

    public GridPane jeproLabLaboratoryUrlsLayout, jeproLabLaboratoryInformationLayout;

    public JeproSwitchButton jeproLabLaboratoryPublished, jeproLabLaboratoryMainUrl;
    public TableView jeproLabLaboratoryUrlsTableView;
    public TableColumn jeproLabUrlIndexColumn, jeproLabUrlCheckBoxColumn, jeproLabUrlDomainColumn, jeproLabUrlSslDomainColumn, jeproLabUrlPhysicalUriColumn;
    public TableColumn jeproLabUrlVirtualUriColumn, jeproLabUrlMainColumn, jeproLabUrlActionColumn;
    public HBox jeproLabLaboratoryUrlCommandWrapper;
    public Button jeproLabLaboratoryUrlSaveButton, jeproLabLaboratoryUrlCancelButton;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double labelColumnWidth = 150;
        double inputColumnWidth = 300;
        double formWidth = 2 *(labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;
        double remainingWidth = (0.98 * formWidth) - 55;

        jeproLabLaboratoryPanelWrapper.setPrefWidth(formWidth);
        jeproLabLaboratoryPanelWrapper.setLayoutX(posX);
        jeproLabLaboratoryPanelWrapper.setLayoutY(posY);

        jeproLabLaboratoryPanelTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabLaboratoryPanelContainerWrapper.setPrefWidth(formWidth);
        jeproLabLaboratoryPanelContainerWrapper.setLayoutY(40);

        jeproLabLaboratoryTab.setPrefWidth(formWidth);

        jeproLabLaboratoryUrlsTableView.setPrefSize(0.98 * formWidth, 240);
        jeproLabLaboratoryUrlsTableView.setLayoutX(0.01 * formWidth);

        jeproLabLaboratoryNameLabel.setText(bundle.getString("JEPROLAB_LABORATORY_NAME_LABEL"));
        jeproLabLaboratoryNameLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryGroupLabel.setText(bundle.getString("JEPROLAB_LABORATORY_GROUP_LABEL"));
        jeproLabLaboratoryGroupLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryCategoryLabel.setText(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabLaboratoryCategoryLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryThemeLabel.setText(bundle.getString("JEPROLAB_THEME_LABEL"));
        jeproLabLaboratoryThemeLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryPublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabLaboratoryPublishedLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryDomainLabel.setText(bundle.getString("JEPROLAB_DOMAIN_LABEL"));
        jeproLabLaboratoryDomainLabel.getStyleClass().add("input-label");
        jeproLabLaboratorySslDomainLabel.setText(bundle.getString("JEPROLAB_SSL_DOMAIN_LABEL"));
        jeproLabLaboratorySslDomainLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryPhysicalUriLabel.setText(bundle.getString("JEPROLAB_PHYSICAL_URI_LABEL"));
        jeproLabLaboratoryPhysicalUriLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryVirtualUriLabel.setText(bundle.getString("JEPROLAB_VIRTUAL_URI_LABEL"));
        jeproLabLaboratoryVirtualUriLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryMainUrlLabel.setText(bundle.getString("JEPROLAB_IS_MAIN_URL_LABEL"));
        jeproLabLaboratoryMainUrlLabel.getStyleClass().add("input-label");
        jeproLabLaboratoryInformation.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabLaboratoryUrls.setText(bundle.getString("JEPROLAB_LABORATORY_URLS_LABEL"));

        jeproLabUrlIndexColumn.setText("#");
        jeproLabUrlCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabUrlDomainColumn.setText(bundle.getString("JEPROLAB_DOMAIN_LABEL"));
        jeproLabUrlSslDomainColumn.setText(bundle.getString("JEPROLAB_SSL_DOMAIN_LABEL"));
        jeproLabUrlPhysicalUriColumn.setText(bundle.getString("JEPROLAB_PHYSICAL_URI_LABEL"));
        jeproLabUrlVirtualUriColumn.setText(bundle.getString("JEPROLAB_VIRTUAL_URI_LABEL"));
        jeproLabUrlMainColumn.setText(bundle.getString("JEPROLAB_IS_MAIN_URL_LABEL"));
        jeproLabUrlActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));

        jeproLabUrlIndexColumn.setPrefWidth(30);
        jeproLabUrlCheckBoxColumn.setPrefWidth(25);
        jeproLabUrlDomainColumn.setPrefWidth(0.18 * remainingWidth);
        jeproLabUrlSslDomainColumn.setPrefWidth(0.18 * remainingWidth);
        jeproLabUrlPhysicalUriColumn.setPrefWidth(0.18 * remainingWidth);
        jeproLabUrlVirtualUriColumn.setPrefWidth(0.18 * remainingWidth);
        jeproLabUrlMainColumn.setPrefWidth(0.18 * remainingWidth);
        jeproLabUrlActionColumn.setPrefWidth(0.1 * remainingWidth);

        GridPane.setMargin(jeproLabLaboratoryNameLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryName, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryGroupLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryGroup, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryCategoryLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryCategory, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryThemeLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryTheme, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryPublishedLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryPublished, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryDomainLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryDomain, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratorySslDomainLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratorySslDomain, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryPhysicalUriLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryPhysicalUri, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryVirtualUriLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryVirtualUri, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryMainUrlLabel, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryMainUrl, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryUrlCommandWrapper, new Insets(10, 10, 10, 10));
        GridPane.setMargin(jeproLabLaboratoryUrlsTableView, new Insets(10, 10, 10, 10));

        jeproLabLaboratoryUrlSaveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));;
        jeproLabLaboratoryUrlSaveButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));;
        jeproLabLaboratoryUrlCancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));;
        jeproLabLaboratoryUrlCancelButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));;

        //initializeContent();
    }

    @Override
    public void initializeContent() {
        super.initializeContent();
        if(this.context == null){
            this.context = JeproLabContext.getContext();
        }
        int labId = JeproLab.request.getRequest().containsKey("laboratory_id") ? Integer.parseInt(JeproLab.request.getRequest().get("laboratory_id")) : 0;
        //int rootCategoryId = JeproLabSettingModel.getIntValue("root_category");
        laboratory = new JeproLabLaboratoryModel(labId);
        List<JeproLabLaboratoryGroupModel> labGroups = JeproLabLaboratoryGroupModel.getLaboratoryGroups();
        if(labGroups != null){
            for(JeproLabLaboratoryGroupModel labGroup : labGroups){
                jeproLabLaboratoryGroup.getItems().add(labGroup.name);
                if(laboratory.laboratory_id > 0 && laboratory.laboratory_group_id == labGroup.laboratory_group_id){
                    jeproLabLaboratoryGroup.setValue(labGroup.name);

                }else {
                    jeproLabLaboratoryGroup.setPromptText(bundle.getString("JEPROLAB_SELECT_LABORATORY_GROUP_LABEL"));
                }
            }
        }

        List<JeproLabCategoryModel> labCategories = JeproLabCategoryModel.getHomeCategories(this.context.language.language_id);
        if(labCategories != null){
            for(JeproLabCategoryModel category : labCategories){
                jeproLabLaboratoryCategory.getItems().add(category.name.get("lang_" + context.language.language_id));
                if(laboratory.laboratory_id > 0 && laboratory.getCategoryId() == category.category_id){
                    jeproLabLaboratoryCategory.setValue(category.name.get("lang_" + context.language.language_id));

                }else {
                    jeproLabLaboratoryCategory.setPromptText(bundle.getString("JEPROLAB_SELECT_ROOT_CATEGORY_LABEL"));
                }
            }
        }
        List<JeproLabThemeModel> themes = JeproLabThemeModel.getThemes();
        if(labCategories != null){
            for(JeproLabThemeModel theme : themes){
                jeproLabLaboratoryTheme.getItems().add(theme.name);
                if(laboratory.laboratory_id > 0 && laboratory.theme_id == theme.theme_id){
                    jeproLabLaboratoryTheme.setValue(theme.name);

                }else {
                    jeproLabLaboratoryTheme.setPromptText(bundle.getString("JEPROLAB_SELECT_THEME_LABEL"));
                }
            }
        }

        if(laboratory.laboratory_id > 0){
            jeproLabLaboratoryPublished.setSelected(laboratory.published);
        }
    }
}