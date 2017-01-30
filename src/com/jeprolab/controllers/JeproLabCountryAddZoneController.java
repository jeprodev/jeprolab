package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabLaboratoryModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryAddZoneController extends JeproLabController{
    JeproLabCountryModel.JeproLabZoneModel zone;
    private Button saveButton, cancelButton;


    @FXML
    public Label jeproLabZoneNameLabel, jeproLabZoneAllowDeliveryLabel;
    public TextField jeproLabZoneName;
    public JeproSwitchButton jeproLabAllowDeliveryZone;
    public GridPane jeproLabZoneLayout;
    public TabPane jeproLabAddZoneTabPaneWrapper;
    public Tab jeproLabZoneInformationTab, jeproLabZoneAssociatedLabTab;
    public JeproFormPanel jeproLabAddZonePanelWrapper;
    public JeproFormPanelTitle jeproLabAddZonePanelTitleWrapper;
    public JeproFormPanelContainer jeproLabAddZonePanelContainerWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 180;
        double inputColumnWidth = 300;
        formWidth = (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabAddZonePanelWrapper.setPrefWidth(formWidth);
        jeproLabAddZonePanelWrapper.setLayoutX(posX);
        jeproLabAddZonePanelWrapper.setLayoutY(posY);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setPrefWidth(formWidth);
        formTitleLabel.setAlignment(Pos.CENTER);
        jeproLabAddZonePanelTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabAddZonePanelTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabAddZonePanelContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddZonePanelContainerWrapper.setLayoutY(40);
        jeproLabAddZoneTabPaneWrapper.setPrefWidth(formWidth);

        jeproLabZoneNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabZoneNameLabel.getStyleClass().add("input-label");
        jeproLabZoneAllowDeliveryLabel.setText(bundle.getString("JEPROLAB_ALLOW_DELIVERY_LABEL"));
        jeproLabZoneAllowDeliveryLabel.getStyleClass().add("input-label");
        jeproLabZoneInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabZoneAssociatedLabTab.setText(bundle.getString("JEPROLAB_ASSOCIATED_LABORATORIES_LABEL"));

        /**
         * GridPane styling
         */
        GridPane.setMargin(jeproLabZoneNameLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(jeproLabZoneName, new Insets(10, 0, 10, 15));
        GridPane.setMargin(jeproLabZoneAllowDeliveryLabel, new Insets(10, 0, 10, 15));
        GridPane.setMargin(jeproLabAllowDeliveryZone, new Insets(10, 0, 10, 15));
    }

    @Override
    public void initializeContent(){
        this.loadZone(false);
        if(zone != null && zone.zone_id > 0){
            formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
            jeproLabZoneName.setText(zone.name);
            jeproLabAllowDeliveryZone.setSelected(zone.allow_delivery);
        }

        if(!JeproLabLaboratoryModel.isFeaturePublished()){
            jeproLabZoneAssociatedLabTab.setDisable(true);
        }else{
            jeproLabZoneAssociatedLabTab.setDisable(false);
        }

        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (zone.zone_id > 0) {
            saveButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
        } else {
            saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL") + " " + bundle.getString("JEPROLAB_ZONE_LABEL"));
        }
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveButton, cancelButton);
        addCommandListener();
    }

    private void addCommandListener(){
        saveButton.setOnAction(evt -> {
            zone.name = jeproLabZoneName.getText();
            zone.allow_delivery = jeproLabAllowDeliveryZone.isSelected();

            boolean result = false;

            if(zone.zone_id > 0){
                result = zone.update();
            }else{
                result = zone.save();
            }

            if(result){
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().zonesForm);
                JeproLab.getInstance().getApplicationForms().zonesForm.controller.initializeContent();

                JeproLabTools.displayBarMessage(500, JeproLab.getBundle().getString("JEPROLAB_ZONE_UPDATED_MESSAGE"));
            }
        });

        cancelButton.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().zonesForm);
            JeproLab.getInstance().getApplicationForms().zonesForm.controller.initializeContent();
        });
    }

    public void loadZone(boolean opt){
        int zoneId = JeproLab.request.getRequest().containsKey("zone_id") ? Integer.parseInt(JeproLab.request.getRequest().get("zone_id")) : 0;
        if (zoneId > 0) {
            if (zone == null || zone.zone_id <= 0) {
                zone = new JeproLabCountryModel.JeproLabZoneModel(zoneId);
            }
        } else if (opt) {
            if (this.zone == null || zone.zone_id <= 0) {
                zone = new JeproLabCountryModel.JeproLabZoneModel();
            }
        }else {
            zone = new JeproLabCountryModel.JeproLabZoneModel();

        }
    }
}
