package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCountryModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryZoneController extends JeproLabController{
    private CheckBox checkAll;
    private Button addZoneButton, cancelButton;
    private ObservableList<JeproLabZoneRecord> zoneList;


    @FXML
    public JeproFormPanel jeproLabAddZonePanelWrapper;
    public TableView<JeproLabZoneRecord> jeproLabZoneTableView;
    public TableColumn<JeproLabZoneRecord, Integer> jeproLabZoneIndexColumn;
    public TableColumn<JeproLabZoneRecord, Boolean> jeproLabZoneCheckBoxColumn;
    public TableColumn<JeproLabZoneRecord, String> jeproLabZoneNameColumn;
    public TableColumn<JeproLabZoneRecord, Boolean> jeproLabZoneAllowDeliveryColumn;
    public TableColumn<JeproLabZoneRecord, HBox> jeproLabZoneActionsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        checkAll = new CheckBox();
        double remainingWidth = (int)(0.98 *JeproLab.APP_WIDTH) - 217;
        jeproLabZoneTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, 600);
        jeproLabZoneTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabZoneTableView.setLayoutY(10);

        jeproLabZoneIndexColumn.setText("#");
        jeproLabZoneIndexColumn.setPrefWidth(30);
        jeproLabZoneIndexColumn.setCellValueFactory(new PropertyValueFactory<>("zoneId"));
        jeproLabZoneCheckBoxColumn.setGraphic(checkAll);
        jeproLabZoneCheckBoxColumn.setPrefWidth(25);
        tableCellAlign(jeproLabZoneCheckBoxColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabZoneRecord, Boolean>, TableCell<JeproLabZoneRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabZoneCheckBoxColumn.setCellFactory(checkBoxFactory);
        jeproLabZoneNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabZoneNameColumn.setPrefWidth(remainingWidth);
        tableCellAlign(jeproLabZoneNameColumn, Pos.CENTER_LEFT);
        jeproLabZoneNameColumn.setCellValueFactory(new PropertyValueFactory<>("zoneName"));
        jeproLabZoneAllowDeliveryColumn.setText(bundle.getString("JEPROLAB_ALLOW_DELIVERY_LABEL"));
        jeproLabZoneAllowDeliveryColumn.setPrefWidth(100);
        tableCellAlign(jeproLabZoneAllowDeliveryColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabZoneRecord, Boolean>, TableCell<JeproLabZoneRecord, Boolean>>  allowDeliveryFactory = param -> new JeproLabAllowDeliveryCell();
        jeproLabZoneAllowDeliveryColumn.setCellFactory(allowDeliveryFactory);
        jeproLabZoneActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabZoneActionsColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabZoneRecord, HBox>, TableCell<JeproLabZoneRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabZoneActionsColumn.setCellFactory(actionFactory);
    }

    @Override
    public void initializeContent(){
        if(zoneList == null){
            zoneList = FXCollections.observableArrayList();
        }
        List<JeproLabCountryModel.JeproLabZoneModel> zones = JeproLabCountryModel.JeproLabZoneModel.getZones();
        if(zones != null){
            jeproLabZoneTableView.getItems().clear();
            zoneList.clear();
            zoneList.addAll(zones.stream().map(JeproLabZoneRecord::new).collect(Collectors.toList()));
            jeproLabZoneTableView.setItems(zoneList);
        }

        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        addZoneButton = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addZoneButton, cancelButton);
        addEventListener();
    }

    private void addEventListener(){
        addZoneButton.setOnAction(event ->{
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addZoneForm);
            JeproLab.getInstance().getApplicationForms().addZoneForm.controller.initializeContent();
        });

        cancelButton.setOnAction(event -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().dashBoardForm);
            JeproLab.getInstance().getApplicationForms().dashBoardForm.controller.initializeContent();
        });
    }

    public static class JeproLabZoneRecord{
        private SimpleIntegerProperty zoneIndex;
        private SimpleStringProperty zoneName;
        private SimpleBooleanProperty allowDelivery;

        public JeproLabZoneRecord(JeproLabCountryModel.JeproLabZoneModel zone){
            zoneIndex = new SimpleIntegerProperty(zone.zone_id);
            zoneName = new SimpleStringProperty(zone.name);
            allowDelivery = new SimpleBooleanProperty(zone.allow_delivery);
        }

        public int getZoneIndex(){
            return zoneIndex.get();
        }

        public String getZoneName(){
            return zoneName.get();
        }

        public boolean getAllowDelivery(){
            return allowDelivery.get();
        }
    }

    protected static class JeproLabActionCell extends TableCell<JeproLabZoneRecord, HBox>{
        private HBox commandContainer;
        public final double btnSize = 22;
        private Button editZone, deleteZone;

        public JeproLabActionCell(){
            commandContainer = new HBox(3);
            editZone = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editZone.setPrefSize(btnSize, btnSize);
            editZone.setMaxSize(btnSize, btnSize);
            editZone.setMinSize(btnSize, btnSize);
            editZone.getStyleClass().add("icon-btn");

            deleteZone = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteZone.setPrefSize(btnSize, btnSize);
            deleteZone.setMaxSize(btnSize, btnSize);
            deleteZone.setMinSize(btnSize, btnSize);
            deleteZone.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabZoneRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getZoneIndex();
                editZone.setOnAction( evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addZoneForm);
                    JeproLab.getInstance().getApplicationForms().addZoneForm.controller.initializeContent(itemId);
                });
                commandContainer.getChildren().addAll(editZone, deleteZone);
                this.getTableRow().setPrefHeight(JeproLabController.rowHeight);
                setGraphic(commandContainer);
            }
        }
    }

    public static class JeproLabCheckBoxCell extends TableCell<JeproLabZoneRecord, Boolean>{
        private CheckBox zoneCheckBox;

        public JeproLabCheckBoxCell(){
            zoneCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean it){
            super.commitEdit(it);
        }

        @Override
        public void updateItem(Boolean item, boolean it){
            super.updateItem(item, it);
            final ObservableList items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(zoneCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public class JeproLabAllowDeliveryCell extends TableCell<JeproLabZoneRecord, Boolean> {
        private Button zoneAllowDelivery;

        public JeproLabAllowDeliveryCell(){
            zoneAllowDelivery = new Button("");
            zoneAllowDelivery.setPrefSize(btnSize, btnSize);
            zoneAllowDelivery.setMaxSize(btnSize, btnSize);
            zoneAllowDelivery.setMinSize(btnSize, btnSize);
            zoneAllowDelivery.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Boolean it){
            super.commitEdit(it);
        }

        @Override
        public void updateItem(Boolean item, boolean it){
            super.updateItem(item, it);
            final ObservableList<JeproLabZoneRecord> items = this.getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                if((items.get(getIndex())).getAllowDelivery()) {
                    zoneAllowDelivery.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    zoneAllowDelivery.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(zoneAllowDelivery);
                setAlignment(Pos.CENTER);
            }
        }
    }

}
