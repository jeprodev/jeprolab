package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCountryModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryController extends JeproLabController{
    private Button addCountryBtn, addStateBtn;
    private CheckBox checkAll;
    private ObservableList<JeproLabCountryRecord> countryList;

    @FXML
    public TableView<JeproLabCountryRecord> jeproLabCountryTableView;
    public TableColumn<JeproLabCountryRecord, String> jeproLabCountryIndexColumn;
    public TableColumn<JeproLabCountryRecord, Boolean> jeproLabCountryStatusColumn;
    public TableColumn<JeproLabCountryRecord, Boolean> jeproLabCountryCheckBoxColumn;
    public TableColumn<JeproLabCountryRecord, String> jeproLabCountryNameColumn;
    public TableColumn<JeproLabCountryRecord, String> jeproLabCountryIsoCodeColumn;
    public TableColumn<JeproLabCountryRecord, String> jeproLabCountryCallPrefixColumn;
    public TableColumn<JeproLabCountryRecord, String> jeproLabCountryZoneColumn;
    public TableColumn<JeproLabCountryRecord, HBox> jeproLabCountryActionsColumn;
    public HBox jeproLabCountrySearchWrapper;
    public TextField jeproLabCountrySearch;
    public Button jeproLabCountrySearchBtn;


    public  void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 294;

        jeproLabCountryTableView.setPrefSize(formWidth, 600);
        VBox.setMargin(jeproLabCountrySearchWrapper, new Insets(10, 0, 0,(0.01 * JeproLab.APP_WIDTH)));
        VBox.setMargin(jeproLabCountryTableView, new Insets(0, 0, 0,(0.01 * JeproLab.APP_WIDTH)));

        //jeproLabCountryTableView;
        jeproLabCountryIndexColumn.setText("#");
        jeproLabCountryIndexColumn.setPrefWidth(30);
        jeproLabCountryIndexColumn.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        tableCellAlign(jeproLabCountryIndexColumn, Pos.BASELINE_RIGHT);
        checkAll = new CheckBox();
        jeproLabCountryCheckBoxColumn.setPrefWidth(22);
        jeproLabCountryCheckBoxColumn.setGraphic(checkAll);
        tableCellAlign(jeproLabCountryCheckBoxColumn, Pos.CENTER);
        jeproLabCountryCheckBoxColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabCountryRecord, Boolean>, TableCell<JeproLabCountryRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabCountryCheckBoxColumn.setCellFactory(checkBoxFactory);
        jeproLabCountryStatusColumn.setPrefWidth(60);
        jeproLabCountryStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        tableCellAlign(jeproLabCountryStatusColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabCountryRecord, Boolean>, TableCell<JeproLabCountryRecord, Boolean>> statusFactory = param -> new JeproLabStatusCell();
        jeproLabCountryStatusColumn.setCellFactory(statusFactory);
        jeproLabCountryNameColumn.setPrefWidth(0.7 * remainingWidth);
        jeproLabCountryNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        tableCellAlign(jeproLabCountryNameColumn, Pos.CENTER_LEFT);
        jeproLabCountryNameColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        jeproLabCountryIsoCodeColumn.setPrefWidth(60);
        jeproLabCountryIsoCodeColumn.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        jeproLabCountryIsoCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryIsoCode"));
        tableCellAlign(jeproLabCountryIsoCodeColumn, Pos.CENTER);
        jeproLabCountryCallPrefixColumn.setPrefWidth(70);
        jeproLabCountryCallPrefixColumn.setText(bundle.getString("JEPROLAB_CALL_PREFIX_LABEL"));
        jeproLabCountryCallPrefixColumn.setCellValueFactory(new PropertyValueFactory<>("countryCallPrefix"));
        tableCellAlign(jeproLabCountryCallPrefixColumn, Pos.CENTER);
        jeproLabCountryZoneColumn.setPrefWidth(0.3 * remainingWidth);
        jeproLabCountryZoneColumn.setText(bundle.getString("JEPROLAB_ZONE_LABEL"));
        jeproLabCountryZoneColumn.setCellValueFactory(new PropertyValueFactory<>("countryZone"));
        tableCellAlign(jeproLabCountryZoneColumn, Pos.CENTER);
        jeproLabCountryActionsColumn.setPrefWidth(50);
        jeproLabCountryActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        Callback<TableColumn<JeproLabCountryRecord, HBox>, TableCell<JeproLabCountryRecord, HBox>> actionsFactory = param -> new JeproLabActionCell();
        jeproLabCountryActionsColumn.setCellFactory(actionsFactory);
    }

    @Override
    public void initializeContent(){
        if(countryList == null) {
            countryList = FXCollections.observableArrayList();
        }

        List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountries(context.language.language_id);
        if(countries != null){
            countryList.clear();
            countryList.addAll(countries.stream().map(JeproLabCountryRecord::new).collect(Collectors.toList()));
        }
        jeproLabCountryTableView.setItems(countryList);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        addCountryBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addStateBtn = new Button(bundle.getString("JEPROLAB_ADD_STATE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addCountryBtn, addStateBtn);
    }


    public static class JeproLabCountryRecord{
        private SimpleIntegerProperty countryId;
        private SimpleBooleanProperty countryPublished;
        private SimpleStringProperty countryName, countryIsoCode, countryCallPrefix, countryZone;

        public JeproLabCountryRecord(JeproLabCountryModel country){
            countryId = new SimpleIntegerProperty(country.country_id);
            countryName = new SimpleStringProperty(country.name.get("lang_" + JeproLabContext.getContext().language.language_id));
            countryIsoCode = new SimpleStringProperty(country.iso_code);
            countryCallPrefix = new SimpleStringProperty(country.call_prefix);
            String zoneName = JeproLabCountryModel.JeproLabZoneModel.getNameByZoneId(country.zone_id);
            countryZone = new SimpleStringProperty(zoneName);
            countryPublished = new SimpleBooleanProperty(country.published);
        }

        public int getCountryId(){
            return countryId.get();
        }

        public String getCountryName(){
            return countryName.get();
        }

        public String getCountryIsoCode(){
            return countryIsoCode.get();
        }

        public String getCountryCallPrefix(){
            return countryCallPrefix.get();
        }

        public String getCountryZone(){
            return countryZone.get();
        }

        public boolean getCountryPublished(){
            return countryPublished.get();
        }
    }

    private class JeproLabActionCell extends TableCell<JeproLabCountryRecord, HBox>{
        protected HBox commandContainer;
        public final double btnSize = 22;
        private Button editBtn, deleteBtn;

        public JeproLabActionCell(){
            commandContainer = new HBox(4);
            commandContainer.setAlignment(Pos.CENTER);
            editBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editBtn.setPrefSize(btnSize, btnSize);
            editBtn.setMaxSize(btnSize, btnSize);
            editBtn.setMinSize(btnSize, btnSize);
            editBtn.getStyleClass().add("icon-btn");

            deleteBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteBtn.setPrefSize(btnSize, btnSize);
            deleteBtn.setMaxSize(btnSize, btnSize);
            deleteBtn.setMinSize(btnSize, btnSize);
            deleteBtn.getStyleClass().add("icon-btn");
            commandContainer.getChildren().addAll(editBtn, deleteBtn);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabCountryRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId =  items.get(getIndex()).getCountryId();
                editBtn.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCountryForm);
                    JeproLab.getInstance().getApplicationForms().addCountryForm.controller.initializeContent(itemId);
                });
                this.getTableRow().setPrefHeight(JeproLabController.rowHeight);
                setGraphic(commandContainer);
            }
        }

    }

    private class JeproLabCheckBoxCell extends TableCell<JeproLabCountryRecord, Boolean>{
        private CheckBox countryCheckBox;

        public JeproLabCheckBoxCell(){
            countryCheckBox = new CheckBox();
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
                setGraphic(countryCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private class JeproLabStatusCell extends TableCell<JeproLabCountryRecord, Boolean>{
        private Button statusBtn;

        public JeproLabStatusCell(){
            statusBtn = new Button();
            int buttonSize = 18;
            statusBtn.setMinSize(buttonSize, buttonSize);
            statusBtn.setMaxSize(buttonSize, buttonSize);
            statusBtn.setPrefSize(buttonSize, buttonSize);
            statusBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabCountryRecord> items = getTableView().getItems();
            int ind = getIndex();
            if((items != null) && (ind >= 0 && ind < items.size())){
                if(items.get(ind).getCountryPublished()) {
                    statusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    statusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(statusBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
