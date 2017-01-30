package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.models.JeproLabTaxModel;
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
public class JeproLabTaxGroupsController extends JeproLabController{
    private CheckBox checkAll;
    private Button addTaxRulesGroup;

    @FXML
    public JeproFormPanel jeproLabFormPanelWrapper;
    public TextField jeproLabTaxRulesGroupSearch;
    public HBox jeproLabTaxRulesGroupSearchWrapper;
    public Button jeproLabTaxRulesGroupSearchBtn;
    public TableView<JeproLabTaxRulesGroupRecord> jeproLabTaxRulesGroupTableView;
    public TableColumn<JeproLabTaxRulesGroupRecord, String> jeproLabTaxRulesGroupIndexColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, Boolean> jeproLabTaxRulesGroupCheckBoxColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, String> jeproLabTaxRulesGroupNameColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, Button> jeproLabTaxRulesGroupStatusColumn;
    public TableColumn<JeproLabTaxRulesGroupRecord, HBox> jeproLabTaxRulesGroupActionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 172;

        VBox.setMargin(jeproLabTaxRulesGroupSearchWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabTaxRulesGroupTableView, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));

        jeproLabTaxRulesGroupTableView.setPrefSize(formWidth, 600);

        jeproLabTaxRulesGroupIndexColumn.setPrefWidth(30);
        jeproLabTaxRulesGroupIndexColumn.setText("#");
        jeproLabTaxRulesGroupIndexColumn.setCellValueFactory(new PropertyValueFactory<>("taxRulesGroupIndex"));
        tableCellAlign(jeproLabTaxRulesGroupIndexColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        jeproLabTaxRulesGroupCheckBoxColumn.setGraphic(checkAll);
        jeproLabTaxRulesGroupCheckBoxColumn.setPrefWidth(20);
        //jeproLabTaxRulesGroupCheckBoxColumn;
        Callback<TableColumn<JeproLabTaxRulesGroupRecord, Boolean>, TableCell<JeproLabTaxRulesGroupRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabTaxRulesGroupCheckBoxColumn.setCellFactory(checkBoxFactory);

        jeproLabTaxRulesGroupNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabTaxRulesGroupNameColumn.setPrefWidth(remainingWidth);
        jeproLabTaxRulesGroupNameColumn.setCellValueFactory(new PropertyValueFactory<>("taxRulesGroupName"));

        jeproLabTaxRulesGroupStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabTaxRulesGroupStatusColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabTaxRulesGroupRecord, Button>, TableCell<JeproLabTaxRulesGroupRecord, Button>> statusFactory = param -> new JeproLabStatusCell();
        jeproLabTaxRulesGroupStatusColumn.setCellFactory(statusFactory);

        jeproLabTaxRulesGroupActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabTaxRulesGroupActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabTaxRulesGroupRecord, HBox>, TableCell<JeproLabTaxRulesGroupRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabTaxRulesGroupActionColumn.setCellFactory(actionFactory);
        /*
        fbx18596903
                kaingoof
                */
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addTaxRulesGroup = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addTaxRulesGroup);
    }

    @Override
    public void initializeContent(){
        List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxRulesGroupRecords = JeproLabTaxModel.JeproLabTaxRulesGroupModel.getTaxRulesGroups();
        ObservableList<JeproLabTaxRulesGroupRecord> taxRulesGroupRecordList = FXCollections.observableArrayList();
        if(!taxRulesGroupRecords.isEmpty()){
            taxRulesGroupRecordList.addAll(taxRulesGroupRecords.stream().map(JeproLabTaxRulesGroupRecord::new).collect(Collectors.toList()));
            jeproLabTaxRulesGroupTableView.getItems().clear();
            jeproLabTaxRulesGroupTableView.setItems(taxRulesGroupRecordList);
        }
        updateToolBar();
    }

    /**
     *
     * Created by jeprodev on 02/02/2014.
     */
    public static class JeproLabTaxRulesGroupRecord{
        private SimpleIntegerProperty taxRulesGroupIndex;
        private SimpleStringProperty taxRulesGroupName;
        private SimpleBooleanProperty taxRulesGroupPublished;

        public JeproLabTaxRulesGroupRecord(JeproLabTaxModel.JeproLabTaxRulesGroupModel taxRulesGroup){
            taxRulesGroupIndex = new SimpleIntegerProperty(taxRulesGroup.tax_rules_group_id);
            taxRulesGroupName = new SimpleStringProperty(taxRulesGroup.name);
            taxRulesGroupPublished = new SimpleBooleanProperty(taxRulesGroup.published);
        }

        public int getTaxRulesGroupIndex(){
            return taxRulesGroupIndex.get();
        }

        public String getTaxRulesGroupName(){
            return taxRulesGroupName.get();
        }

        public boolean getTaxRulesGroupPublished(){
            return taxRulesGroupPublished.get();
        }
    }

    /**
     *
     * Created by jeprodev on 02/02/2014.
     */
    private static class JeproLabCheckBoxCell extends TableCell<JeproLabTaxRulesGroupRecord, Boolean>{
        private CheckBox taxRulesGroupCheckBox;

        public JeproLabCheckBoxCell(){
            taxRulesGroupCheckBox = new CheckBox();
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabTaxRulesGroupRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(taxRulesGroupCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    /**
     *
     * Created by jeprodev on 02/02/2014.
     */
    private static class JeproLabStatusCell extends TableCell<JeproLabTaxRulesGroupRecord, Button> {
        private Button statusBtn;
        private final double btnSize = 18;

        public JeproLabStatusCell(){
            statusBtn = new Button("");
            statusBtn.setMaxSize(btnSize, btnSize);
            statusBtn.setMinSize(btnSize, btnSize);
            statusBtn.setPrefSize(btnSize, btnSize);
            statusBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabTaxRulesGroupRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getTaxRulesGroupPublished()) {
                    statusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    statusBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(statusBtn);
                setAlignment(Pos.CENTER);
            }
        }
    }

    /**
     *
     * Created by jeprodev on 02/02/2014.
     */
    private static class JeproLabActionCell extends TableCell<JeproLabTaxRulesGroupRecord, HBox>{
        private Button editTaxRulesGroup, deleteTaxRulesGroup;
        private HBox commandWrapper;
        private final double btnSize = 18;

        public JeproLabActionCell(){
            editTaxRulesGroup = new Button("");
            editTaxRulesGroup.getStyleClass().addAll("edit-btn", "icon-btn");
            editTaxRulesGroup.setPrefSize(btnSize, btnSize);
            editTaxRulesGroup.setMinSize(btnSize, btnSize);
            editTaxRulesGroup.setMaxSize(btnSize, btnSize);

            deleteTaxRulesGroup = new Button("");
            deleteTaxRulesGroup.getStyleClass().addAll("delete-btn", "icon-btn");
            deleteTaxRulesGroup.setPrefSize(btnSize, btnSize);
            deleteTaxRulesGroup.setMaxSize(btnSize, btnSize);
            deleteTaxRulesGroup.setMinSize(btnSize, btnSize);

            commandWrapper = new HBox(8);
            commandWrapper.setAlignment(Pos.CENTER);
            commandWrapper.getChildren().addAll(editTaxRulesGroup, deleteTaxRulesGroup);
        }

        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabTaxRulesGroupRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getTaxRulesGroupIndex();
                editTaxRulesGroup.setOnAction(event -> {
                    JeproLab.request.setRequest("tax_rules_group_id=" + itemId);
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxGroupForm);
                    JeproLab.getInstance().getApplicationForms().addTaxGroupForm.controller.initializeContent();
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
