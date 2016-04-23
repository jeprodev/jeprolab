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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabCountryAddZoneController extends JeproLabController {
    JeproLabCountryModel.JeproLabZoneModel zone;


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
            jeproLabZoneName.setText(zone.name);
            jeproLabAllowDeliveryZone.setSelected(zone.allow_delivery);
        }
        if(!JeproLabLaboratoryModel.isFeaturePublished()){
            jeproLabZoneAssociatedLabTab.setDisable(true);
        }else{
            jeproLabZoneAssociatedLabTab.setDisable(false);
        }
    }

    @Override
    public void updateToolBar(){}

    public boolean loadZone(boolean opt){
        int zoneId = JeproLab.request.getRequest().containsKey("zone_id") ? Integer.parseInt(JeproLab.request.getRequest().get("zone_id")) : 0;
        if (zoneId > 0) {
            if (zone == null || zone.zone_id <= 0) {
                zone = new JeproLabCountryModel.JeproLabZoneModel(zoneId);
            }
            if (zone.zone_id > 0)
                return true;
            // throw exception
            JeproLabTools.displayError(500, "The zone cannot be loaded (or not found)");
            return false;
        } else if (opt) {
            if (this.zone == null || zone.zone_id <= 0) {
                zone = new JeproLabCountryModel.JeproLabZoneModel();
            }
            return true;
        }else {
            /*this.context.controller.has_errors = true;
            JeproLabTools.displayError(500, "The zone cannot be loaded (the identifier is missing or invalid)"); */
            return false;
        }
    }
}
