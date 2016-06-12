package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabTaxController extends JeproLabController {
    private CheckBox checkAll;
    private Button addTaxButton;
    @FXML
    public TableView<JeproLabTaxRecord> jeproLabTaxTableView;
    public TableColumn<JeproLabTaxRecord, String> jeproLabTaxIndexColumn;
    public TableColumn<JeproLabTaxRecord, Boolean> jeproLabTaxCheckBoxColumn;
    public TableColumn<JeproLabTaxRecord, Button> jeproLabTaxStatusColumn;
    public TableColumn<JeproLabTaxRecord, String> jeproLabTaxNameColumn;
    public TableColumn<JeproLabTaxRecord, String> jeproLabTaxRateColumn;
    public TableColumn<JeproLabTaxRecord, HBox> jeproLabTaxActionColumn;
    public HBox jeproLabTaxSearchWrapper;
    public TextField jeproLabTaxSearch;
    public Button jeproLabTaxSearchBtn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 242;

        jeproLabTaxTableView.setPrefSize(formWidth, 600);
        VBox.setMargin(jeproLabTaxSearchWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabTaxTableView, new Insets(0, 0, 0, 0.01 * JeproLab.APP_WIDTH));

        jeproLabTaxIndexColumn.setText("#");
        jeproLabTaxIndexColumn.setPrefWidth(30);
        jeproLabTaxIndexColumn.setCellValueFactory(new PropertyValueFactory<>("taxIndex"));
        tableCellAlign(jeproLabTaxIndexColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        jeproLabTaxCheckBoxColumn.setPrefWidth(20);
        jeproLabTaxCheckBoxColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabTaxRecord, Boolean>, TableCell<JeproLabTaxRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabTaxCheckBoxColumn.setCellFactory(checkBoxFactory);
        tableCellAlign(jeproLabTaxCheckBoxColumn, Pos.CENTER);

        jeproLabTaxStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabTaxStatusColumn.setPrefWidth(50);
        Callback<TableColumn<JeproLabTaxRecord, Button>, TableCell<JeproLabTaxRecord, Button>> statusCellFactory = param -> new JeproLabStatusCell();
        jeproLabTaxStatusColumn.setCellFactory(statusCellFactory);
        tableCellAlign(jeproLabTaxStatusColumn, Pos.CENTER);

        jeproLabTaxNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabTaxNameColumn.setPrefWidth(remainingWidth);
        jeproLabTaxNameColumn.setCellValueFactory(new PropertyValueFactory<>("taxName"));
        tableCellAlign(jeproLabTaxNameColumn, Pos.CENTER_LEFT);

        jeproLabTaxRateColumn.setText(bundle.getString("JEPROLAB_RATE_LABEL"));
        jeproLabTaxRateColumn.setPrefWidth(80);
        jeproLabTaxRateColumn.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
        tableCellAlign(jeproLabTaxRateColumn, Pos.CENTER);

        jeproLabTaxActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabTaxActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabTaxRecord, HBox>, TableCell<JeproLabTaxRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabTaxActionColumn.setCellFactory(actionFactory);

        jeproLabTaxSearch.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));
        addTaxButton = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
    }

    @Override
    public void initializeContent(){
        List<JeproLabTaxModel> taxRecords = JeproLabTaxModel.getTaxList();
        System.out.println(taxRecords.size());
        ObservableList<JeproLabTaxRecord> taxRecordList = FXCollections.observableArrayList();
        if(!taxRecords.isEmpty()){
            taxRecordList.addAll(taxRecords.stream().map(JeproLabTaxRecord::new).collect(Collectors.toList()));
            jeproLabTaxTableView.setItems(taxRecordList);
        }
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();

        commandWrapper.getChildren().addAll(addTaxButton);
    }

    public static class JeproLabTaxRecord{
        private SimpleIntegerProperty taxIndex;
        private SimpleStringProperty taxName;
        private SimpleBooleanProperty taxPublished;

        public JeproLabTaxRecord(JeproLabTaxModel tax){
            taxIndex = new SimpleIntegerProperty(tax.tax_id);
            taxName = new SimpleStringProperty(tax.name.get("lang_" + JeproLabContext.getContext().language.language_id));
            taxPublished = new SimpleBooleanProperty(tax.published);
        }

        public int getTaxIndex(){
            return taxIndex.get();
        }

        public String getTaxName(){
            return taxName.get();
        }

        public boolean getTaxPublished(){
            return taxPublished.get();
        }
    }


    private static class JeproLabStatusCell extends TableCell<JeproLabTaxRecord, Button>{
        private Button statusButton;
        private final double btnSize = 18;

        public JeproLabStatusCell(){
            statusButton = new Button("");
            statusButton.setPrefSize(btnSize, btnSize);
            statusButton.setMaxSize(btnSize, btnSize);
            statusButton.setMinSize(btnSize, btnSize);
            statusButton.getStyleClass().addAll("icon-btn");

        }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabTaxRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getTaxPublished()) {
                    statusButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    statusButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(statusButton);
                setAlignment(Pos.CENTER);
            }
        }
    }


    private static class JeproLabCheckBoxCell extends TableCell<JeproLabTaxRecord, Boolean>{

    }


    private static class JeproLabActionCell extends TableCell<JeproLabTaxRecord, HBox>{
        private Button editTax, deleteTax;
        private HBox commandWrapper;
        private final double btnSize = 18;

        public JeproLabActionCell(){
            editTax = new Button("");
            editTax.setPrefSize(btnSize, btnSize);
            editTax.setMinSize(btnSize, btnSize);
            editTax.setMaxSize(btnSize, btnSize);
            editTax.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteTax = new Button("");
            deleteTax.setPrefSize(btnSize, btnSize);
            deleteTax.setMinSize(btnSize, btnSize);
            deleteTax.setMaxSize(btnSize, btnSize);
            deleteTax.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper = new HBox(8);
            commandWrapper.setAlignment(Pos.CENTER);
            commandWrapper.getChildren().addAll(editTax, deleteTax);
        }

        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabTaxRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getTaxIndex();
                editTax.setOnAction(event -> {
                    JeproLab.request.setRequest("tax_id=" + itemId);
                    try{
                        JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addTaxForm);
                        JeproLabContext.getContext().controller.initializeContent();
                    }catch (IOException ignored){
                        ignored.printStackTrace();
                    }
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}