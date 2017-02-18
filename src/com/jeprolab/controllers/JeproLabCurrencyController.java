package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabCurrencyModel;
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
public class JeproLabCurrencyController extends JeproLabController{
    private CheckBox checkAll;
    private Button addCurrencyBtn;
    @FXML
    public Button jeproLabCurrencySearchBtn;
    public HBox jeproLabCurrencySearchWrapper;
    public TextField jeproLabCurrencySearch;
    public TableView<JeproLabCurrencyRecord> jeproLabCurrencyTableView;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyIndexColumn;
    public TableColumn<JeproLabCurrencyRecord, Boolean> jeproLabCurrencyCheckBoxColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyNameColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyIsoCodeColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyNumericIsoCodeColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencySymbolColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyConversionRateColumn;
    public TableColumn<JeproLabCurrencyRecord, String> jeproLabCurrencyFormatColumn;
    public TableColumn<JeproLabCurrencyRecord, Button> jeproLabCurrencyHasDecimalsColumn;
    public TableColumn<JeproLabCurrencyRecord, Button> jeproLabCurrencyHasSpacingColumn;
    public TableColumn<JeproLabCurrencyRecord, Button> jeproLabCurrencyStatusColumn;
    public TableColumn<JeproLabCurrencyRecord, HBox> jeproLabCurrencyActionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 757;

        VBox.setMargin(jeproLabCurrencySearchWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabCurrencyTableView, new Insets(0, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        jeproLabCurrencyTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, 600);

        jeproLabCurrencySearch.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabCurrencyIndexColumn.setText("#");
        jeproLabCurrencyIndexColumn.setPrefWidth(30);
        jeproLabCurrencyIndexColumn.setCellValueFactory(new PropertyValueFactory<>("currencyIndex"));
        tableCellAlign(jeproLabCurrencyIndexColumn, Pos.CENTER_RIGHT);

        checkAll =  new CheckBox();
        jeproLabCurrencyCheckBoxColumn.setGraphic(checkAll);
        jeproLabCurrencyCheckBoxColumn.setPrefWidth(20);
        //jeproLabCurrencyCheckBoxColumn;
        Callback<TableColumn<JeproLabCurrencyRecord, Boolean>, TableCell<JeproLabCurrencyRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabCurrencyCheckBoxColumn.setCellFactory(checkBoxFactory);

        jeproLabCurrencyNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabCurrencyNameColumn.setPrefWidth(remainingWidth);
        jeproLabCurrencyNameColumn.setCellValueFactory(new PropertyValueFactory<>("currencyName"));

        jeproLabCurrencyIsoCodeColumn.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        jeproLabCurrencyIsoCodeColumn.setPrefWidth(70);
        jeproLabCurrencyIsoCodeColumn.setCellValueFactory(new PropertyValueFactory<>("currencyIsoCode"));
        tableCellAlign(jeproLabCurrencyIsoCodeColumn, Pos.CENTER);

        jeproLabCurrencyNumericIsoCodeColumn.setText(bundle.getString("JEPROLAB_NUMERIC_ISO_CODE_LABEL"));
        jeproLabCurrencyNumericIsoCodeColumn.setPrefWidth(140);
        jeproLabCurrencyNumericIsoCodeColumn.setCellValueFactory(new PropertyValueFactory<>("currencyNumericIsoCode"));
        tableCellAlign(jeproLabCurrencyNumericIsoCodeColumn, Pos.CENTER);

        jeproLabCurrencySymbolColumn.setText(bundle.getString("JEPROLAB_SYMBOL_LABEL"));
        jeproLabCurrencySymbolColumn.setPrefWidth(50);
        jeproLabCurrencySymbolColumn.setCellValueFactory(new PropertyValueFactory<>("currencySymbol"));
        tableCellAlign(jeproLabCurrencySymbolColumn, Pos.CENTER);

        jeproLabCurrencyConversionRateColumn.setText(bundle.getString("JEPROLAB_RATE_LABEL"));
        jeproLabCurrencyConversionRateColumn.setPrefWidth(60);
        jeproLabCurrencyConversionRateColumn.setCellValueFactory(new PropertyValueFactory<>("currencyConversionRate"));
        tableCellAlign(jeproLabCurrencyConversionRateColumn, Pos.CENTER);

        jeproLabCurrencyFormatColumn.setText(bundle.getString("JEPROLAB_FORMAT_LABEL"));
        jeproLabCurrencyFormatColumn.setPrefWidth(100);
        jeproLabCurrencyFormatColumn.setCellValueFactory(new PropertyValueFactory<>("currencyFormat"));
        tableCellAlign(jeproLabCurrencyFormatColumn, Pos.CENTER);

        jeproLabCurrencyHasDecimalsColumn.setText(bundle.getString("JEPROLAB_HAS_DECIMALS_LABEL"));
        jeproLabCurrencyHasDecimalsColumn.setPrefWidth(90);
        //jeproLabCurrencyHasDecimalsColumn;
        Callback<TableColumn<JeproLabCurrencyRecord, Button>, TableCell<JeproLabCurrencyRecord, Button>> hasDecimalFactory = param -> new JeproLabHasDecimalCell();
        jeproLabCurrencyHasDecimalsColumn.setCellFactory(hasDecimalFactory);

        jeproLabCurrencyHasSpacingColumn.setText(bundle.getString("JEPROLAB_HAS_SPACING_LABEL"));
        jeproLabCurrencyHasSpacingColumn.setPrefWidth(80);
        //jeproLabCurrencyHasSpacingColumn;
        Callback<TableColumn<JeproLabCurrencyRecord, Button>, TableCell<JeproLabCurrencyRecord, Button>> hasSpacingFactory = param -> new JeproLabHasSpacingCell();
        jeproLabCurrencyHasSpacingColumn.setCellFactory(hasSpacingFactory);

        jeproLabCurrencyStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabCurrencyStatusColumn.setPrefWidth(55);
        Callback<TableColumn<JeproLabCurrencyRecord, Button>, TableCell<JeproLabCurrencyRecord, Button>> statusFactory = param -> new JeproLabStatusCell();
        jeproLabCurrencyStatusColumn.setCellFactory(statusFactory);

        jeproLabCurrencyActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabCurrencyActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabCurrencyRecord, HBox>, TableCell<JeproLabCurrencyRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabCurrencyActionColumn.setCellFactory(actionFactory);
    }

    @Override
    public void  initializeContent(){
        List<JeproLabCurrencyModel> analyzes = JeproLabCurrencyModel.getCurrencies();
        ObservableList<JeproLabCurrencyRecord> analyzeList = FXCollections.observableArrayList();
        if(!analyzes.isEmpty()){
            analyzeList.addAll(analyzes.stream().map(JeproLabCurrencyRecord::new).collect(Collectors.toList()));
            jeproLabCurrencyTableView.setItems(analyzeList);
        }
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addCurrencyBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addCurrencyBtn);
        addCommandEventListener();
    }

    private void addCommandEventListener(){
        addCurrencyBtn.setOnAction(event -> {
            try{
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCurrencyForm);
                JeproLabContext.getContext().controller.initializeContent();
            }catch(Exception ignored){
                ignored.printStackTrace();
            }
        });
    }

    public static class JeproLabCurrencyRecord{
        private SimpleIntegerProperty currencyIndex, currencyFormat, currencyHasDecimals;
        private SimpleStringProperty currencyName, currencyIsoCode, currencySymbol, currencyConversionRate, currencyNumericIsoCode;
        private SimpleBooleanProperty currencyHasSpacing, currencyStatus;

        public JeproLabCurrencyRecord(JeproLabCurrencyModel currency){
            currencyIndex = new SimpleIntegerProperty(currency.currency_id);
            currencyName = new SimpleStringProperty(currency.name);
            currencyIsoCode = new SimpleStringProperty(currency.iso_code);
            currencyNumericIsoCode = new SimpleStringProperty(currency.iso_code_num);
            currencySymbol =  new SimpleStringProperty(currency.sign);
            currencyConversionRate = new SimpleStringProperty(String.valueOf(currency.conversion_rate));
            currencyFormat = new SimpleIntegerProperty(currency.format);
            currencyHasDecimals = new SimpleIntegerProperty(currency.decimals);
            currencyHasSpacing = new SimpleBooleanProperty(currency.blank);
            currencyStatus = new SimpleBooleanProperty(currency.published);
        }

        public int getCurrencyIndex(){
            return currencyIndex.get();
        }

        public int getCurrencyFormat(){
            return currencyFormat.get();
        }

        public String getCurrencyNumericIsoCode(){
            return  currencyNumericIsoCode.get();
        }

        public String getCurrencyName(){
            return currencyName.get();
        }

        public String getCurrencyIsoCode(){
            return currencyIsoCode.get();
        }

        public String getCurrencySymbol(){
            return currencySymbol.get();
        }

        public String getCurrencyConversionRate(){
            return currencyConversionRate.get();
        }

        public int getCurrencyHasDecimals(){
            return currencyHasDecimals.get();
        }

        public boolean getCurrencyHasSpacing(){
            return currencyHasSpacing.get();
        }

        public boolean getCurrencyStatus(){
            return currencyStatus.get();
        }
    }

    private static class JeproLabHasDecimalCell extends TableCell<JeproLabCurrencyRecord, Button> {
        private Button hasDecimalButton;

        public JeproLabHasDecimalCell(){
            hasDecimalButton = new Button();
            hasDecimalButton.setPrefSize(18, 18);
            hasDecimalButton.setMinSize(18, 18);
            hasDecimalButton.setMaxSize(18, 18);
            hasDecimalButton.getStyleClass().addAll("icon-btn");
        }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabCurrencyRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getCurrencyHasDecimals() > 0) {
                    hasDecimalButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    hasDecimalButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(hasDecimalButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabStatusCell extends TableCell<JeproLabCurrencyRecord, Button> {
        private Button currencyStatus;

        public JeproLabStatusCell(){
            currencyStatus = new Button();
            currencyStatus.setPrefSize(18, 18);
            currencyStatus.setMinSize(18, 18);
            currencyStatus.setMaxSize(18, 18);
            currencyStatus.getStyleClass().add("icon-btn");
        }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabCurrencyRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getCurrencyStatus()) {
                    currencyStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    currencyStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(currencyStatus);
                setAlignment(Pos.CENTER);
            }
        }
    }


    private static class JeproLabHasSpacingCell extends TableCell<JeproLabCurrencyRecord, Button> {
        private Button hasSpacingButton;

        public JeproLabHasSpacingCell(){
            hasSpacingButton = new Button();
            hasSpacingButton.setPrefSize(18, 18);
            hasSpacingButton.setMinSize(18, 18);
            hasSpacingButton.setMaxSize(18, 18);
            hasSpacingButton.getStyleClass().addAll("icon-btn");
        }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabCurrencyRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getCurrencyHasSpacing()) {
                    hasSpacingButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    hasSpacingButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(hasSpacingButton);
                setAlignment(Pos.CENTER);
            }
        }
    }


    private static class JeproLabCheckBoxCell extends TableCell<JeproLabCurrencyRecord, Boolean> {
        private CheckBox itemCheckBox;

        public JeproLabCheckBoxCell(){
            itemCheckBox = new CheckBox();
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabCurrencyRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(itemCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }


    private static class JeproLabActionCell extends TableCell<JeproLabCurrencyRecord, HBox> {
        private Button editCurrency, deleteCurrency;
        private HBox commandWrapper;
        private final double btnSize = 18;

        public JeproLabActionCell(){
            editCurrency = new Button();
            editCurrency.setPrefSize(btnSize, btnSize);
            editCurrency.setMinSize(btnSize, btnSize);
            editCurrency.setMaxSize(btnSize, btnSize);
            editCurrency.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteCurrency = new Button();
            deleteCurrency.setPrefSize(btnSize, btnSize);
            deleteCurrency.setMinSize(btnSize, btnSize);
            deleteCurrency.setMaxSize(btnSize, btnSize);
            deleteCurrency.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper = new HBox(8);
            commandWrapper.getChildren().addAll(editCurrency, deleteCurrency);
            commandWrapper.setAlignment(Pos.CENTER);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabCurrencyRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getCurrencyIndex();
                editCurrency.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCurrencyForm);
                    JeproLab.getInstance().getApplicationForms().addCurrencyForm.controller.initializeContent(itemId);
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
