package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCountryModel;
import com.jeprolab.models.JeproLabSettingModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryAddStateController extends JeproLabController {
    private JeproLabCountryModel.JeproLabStateModel state;
    private Map<Integer, String> zones, countries;
    @FXML
    public JeproFormPanel jeproLabStateFormWrapper;
    public JeproFormPanelTitle jeproLabStateFormTitleWrapper;
    public JeproFormPanelContainer jeproLabStateFormContentWrapper;
    public GridPane jeproLabStateLayout;
    public Label jeproLabStateNameLabel, jeproLabStateIsoCodeLabel, jeproLabStateCountryLabel, jeproLabStatePublishedLabel, jeproLabStateCountryZoneLabel;
    //public jeproLabState;
    public TextField jeproLabStateName, jeproLabStateIsoCode;
    public ComboBox<String> jeproLabStateCountry, jeproLabStateCountryZone;
    public JeproSwitchButton jeproLabStatePublished;


    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        Label formTitle = new Label(bundle.getString("JEPROLAB_ADD_STATE_LABEL"));

        double labelConstrains = 150;
        double inputConstrains = 250;
        formWidth = inputConstrains + labelConstrains + 100;
        formTitle.getStyleClass().add("form-title");
        formTitle.setPrefSize(formWidth, 38);

        jeproLabStateLayout.getColumnConstraints().addAll(
            new ColumnConstraints(labelConstrains), new ColumnConstraints(inputConstrains)
        );

        jeproLabStateFormWrapper.setPrefWidth(formWidth);
        jeproLabStateFormWrapper.setLayoutX((JeproLab.APP_WIDTH - formWidth)/2);
        jeproLabStateFormWrapper.setLayoutY(40);
        jeproLabStateFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabStateFormContentWrapper.setPrefWidth(formWidth);
        jeproLabStateFormContentWrapper.setLayoutY(40);

        jeproLabStateFormTitleWrapper.getChildren().addAll(formTitle);

        jeproLabStateNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabStateIsoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        jeproLabStateCountryZoneLabel.setText(bundle.getString("JEPROLAB_COUNTRY_ZONE_LABEL"));
        jeproLabStateCountryLabel.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        jeproLabStatePublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));

        GridPane.setMargin(jeproLabStateNameLabel, new Insets(5, 10, 5, 20));
        GridPane.setMargin(jeproLabStateName, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabStateIsoCodeLabel, new Insets(5, 10, 5, 20));
        GridPane.setMargin(jeproLabStateIsoCode, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabStateCountryZoneLabel, new Insets(5, 10, 5, 20));
        GridPane.setMargin(jeproLabStateCountryZone, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabStateCountryLabel, new Insets(5, 10, 5, 20));
        GridPane.setMargin(jeproLabStateCountry, new Insets(5, 0, 5, 0));
        GridPane.setMargin(jeproLabStatePublishedLabel, new Insets(5, 10, 5, 20));
        GridPane.setMargin(jeproLabStatePublished, new Insets(5, 0, 5, 0));

        JeproLabCountryModel defaultCountry =  new JeproLabCountryModel(JeproLabSettingModel.getIntValue("default_country"));
        List<JeproLabCountryModel.JeproLabZoneModel> zoneList = JeproLabCountryModel.JeproLabZoneModel.getZones();
        zones = new HashMap<>();
        jeproLabStateCountryZone.setPromptText(bundle.getString("JEPROLAB_SELECT_ZONE_LABEL"));
        for(JeproLabCountryModel.JeproLabZoneModel zone : zoneList){
            zones.put(zone.zone_id, zone.name);
            jeproLabStateCountryZone.getItems().add(zone.name);
            if(defaultCountry.zone_id == zone.zone_id){
                jeproLabStateCountryZone.setValue(zone.name);
            }
        }

        int langId = JeproLabContext.getContext().language.language_id;
        List<JeproLabCountryModel> countryList = JeproLabCountryModel.getCountriesByZoneId(defaultCountry.zone_id, langId);
        countries = new HashMap<>();
        jeproLabStateCountry.setPromptText(bundle.getString("JEPROLAB_SELECT_COUNTRY_LABEL"));
        for(JeproLabCountryModel country : countryList){
            countries.put(country.country_id, country.name.get("lang_" + langId));
            jeproLabStateCountry.getItems().add(country.name.get("lang_" + langId));
            if(country.country_id == defaultCountry.country_id) {
                jeproLabStateCountry.getItems().add(country.name.get("lang_" + langId));
            }
        }
    }

    @Override
    public void initializeContent(int stateId){
        Worker<Boolean> worker = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                loadState(stateId);
                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateStateForm();
            }
        };

        new Thread((Task)worker).start();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
    }

    private void updateStateForm(){
        Platform.runLater(() -> {
            if(state != null && state.state_id > 0){
                jeproLabStateName.setText(state.name);
                jeproLabStateIsoCode.setText(state.iso_code);
                jeproLabStateCountry.setValue(JeproLabCountryModel.getCountryNameByCountryId(JeproLabContext.getContext().language.language_id, state.zone_id));
                jeproLabStateCountryZone.setValue(JeproLabCountryModel.JeproLabZoneModel.getNameByZoneId(state.state_id));
                //jeproLabState;
                jeproLabStatePublished.setSelected(state.published);
            }
        });
    }

    private void loadState(int stateId){
        if(stateId > 0){
            state = new JeproLabCountryModel.JeproLabStateModel(stateId);
            if(state.state_id != stateId){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, new Throwable(bundle.getString("JEPROLAB_INVALID_ELEMENT_MESSAGE")));
                state.state_id = 0;
            }
        }else{
            state = new JeproLabCountryModel.JeproLabStateModel();
        }
    }
}
