package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabSettingModel;
import com.jeprolab.models.JeproLabStockModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabAnalyzeController extends JeproLabController{
    private CheckBox checkAll;
    private Button addAnalyzeBtn;
    private ObservableList<JeproLabAnalyzeRecord> analyzeList;
    private TableView<JeproLabAnalyzeRecord> jeproLabAnalyzeTableView;

    private Pagination jeproLabAnalyzePagination;
    private HBox jeproLabAnalyzeSearchWrapper;
    private TextField jeproLabAnalyzeSearch;
    private Button jeproLabAnalyzeSearchBtn;

    @FXML
    public JeproFormPanel jeproLabAnalyzeFormPanel;


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 180;
        checkAll = new CheckBox();

        jeproLabAnalyzeTableView = new TableView<>();
        jeproLabAnalyzeTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, 600);
        jeproLabAnalyzeTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabAnalyzeTableView.setLayoutY(10);

        jeproLabAnalyzeSearch = new TextField();
        jeproLabAnalyzeSearchBtn = new Button();
        jeproLabAnalyzeSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabAnalyzeSearchWrapper = new HBox(10);
        jeproLabAnalyzeSearchWrapper.getChildren().addAll(jeproLabAnalyzeSearch, jeproLabAnalyzeSearchBtn);

        VBox.setMargin(jeproLabAnalyzeSearchWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabAnalyzeTableView, new Insets(0, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        jeproLabAnalyzeSearch.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        TableColumn<JeproLabAnalyzeRecord, Integer> jeproLabAnalyzeIndexColumn = new TableColumn<>("#");
        jeproLabAnalyzeIndexColumn.setPrefWidth(30);
        tableCellAlign(jeproLabAnalyzeIndexColumn, Pos.CENTER_RIGHT);
        jeproLabAnalyzeIndexColumn.setCellValueFactory(new PropertyValueFactory<>("analyzeIndex"));

        TableColumn<JeproLabAnalyzeRecord, Boolean> jeproLabAnalyzeCheckBoxColumn = new TableColumn<>();
        jeproLabAnalyzeCheckBoxColumn.setGraphic(checkAll);
        jeproLabAnalyzeCheckBoxColumn.setPrefWidth(25);
        Callback<TableColumn<JeproLabAnalyzeRecord, Boolean>, TableCell<JeproLabAnalyzeRecord, Boolean>> checkBoxCellFactory = param -> new JeproLabCheckBoxCell();
        jeproLabAnalyzeCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabAnalyzeRecord, Boolean> jeproLabAnalyzeStatusColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabAnalyzeStatusColumn.setPrefWidth(55);
        Callback<TableColumn<JeproLabAnalyzeRecord, Boolean>, TableCell<JeproLabAnalyzeRecord, Boolean>> statusCellFactory = param -> new JeproLabStatusCell();
        jeproLabAnalyzeStatusColumn.setCellFactory(statusCellFactory);

        TableColumn<JeproLabAnalyzeRecord, String> jeproLabAnalyzeNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabAnalyzeNameColumn.setPrefWidth(0.32 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeNameColumn, Pos.CENTER_LEFT);
        jeproLabAnalyzeNameColumn.setCellValueFactory(new PropertyValueFactory<>("analyzeName"));

        TableColumn<JeproLabAnalyzeRecord, String> jeproLabAnalyzeReferenceColumn = new TableColumn<>(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabAnalyzeReferenceColumn.setPrefWidth(0.22 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeReferenceColumn, Pos.CENTER);
        jeproLabAnalyzeReferenceColumn.setCellValueFactory(new PropertyValueFactory<>("analyzeReference"));

        TableColumn<JeproLabAnalyzeRecord, String> jeproLabAnalyzeBasePriceColumn = new TableColumn<>(bundle.getString("JEPROLAB_BASE_PRICE_LABEL"));
        jeproLabAnalyzeBasePriceColumn.setPrefWidth(0.12 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeBasePriceColumn, Pos.CENTER);
        jeproLabAnalyzeBasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("analyzeBasePrice"));

        TableColumn<JeproLabAnalyzeRecord, String> jeproLabAnalyzeFinalPriceColumn = new TableColumn<>(bundle.getString("JEPROLAB_FINAL_PRICE_LABEL"));
        jeproLabAnalyzeFinalPriceColumn.setPrefWidth(0.12 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeFinalPriceColumn, Pos.CENTER);
        jeproLabAnalyzeFinalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("analyzePrice"));

        TableColumn<JeproLabAnalyzeRecord, String> jeproLabAnalyzeCategoryColumn = new TableColumn<>(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabAnalyzeCategoryColumn.setPrefWidth(0.217 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeCategoryColumn, Pos.CENTER_LEFT);
        jeproLabAnalyzeCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("analyzeCategory"));

        TableColumn<JeproLabAnalyzeRecord, HBox> jeproLabAnalyzeActionsColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabAnalyzeActionsColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabAnalyzeRecord, HBox>, TableCell<JeproLabAnalyzeRecord, HBox>> actionsCellFactory = param -> new JeproLabActionCell();
        jeproLabAnalyzeActionsColumn.setCellFactory(actionsCellFactory);

        jeproLabAnalyzeTableView.getColumns().addAll(
            jeproLabAnalyzeIndexColumn, jeproLabAnalyzeCheckBoxColumn, jeproLabAnalyzeStatusColumn, jeproLabAnalyzeNameColumn,
            jeproLabAnalyzeReferenceColumn, jeproLabAnalyzeCategoryColumn, jeproLabAnalyzeBasePriceColumn, jeproLabAnalyzeFinalPriceColumn,
            jeproLabAnalyzeActionsColumn
        );
    }

    @Override
    public void initializeContent(){
        Worker<List<JeproLabAnalyzeModel>> worker = new Task<List<JeproLabAnalyzeModel>>(){
            List<JeproLabAnalyzeModel> analyzes;
            @Override
            protected List<JeproLabAnalyzeModel> call() throws Exception {
                if(isCancelled()){
                    return null;
                }
                analyzes = JeproLabAnalyzeModel.getAnalyzeList();
                return analyzes;
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateTableView(analyzes);
            }

            @Override
            protected void cancelled(){
                super.cancelled();
            }

            @Override
            protected void failed(){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }
        };
        new Thread((Task)worker).start();
        updateToolBar();
    }


    private void updateTableView(List<JeproLabAnalyzeModel> analyzes){
        analyzeList = FXCollections.observableArrayList();
        analyzeList.addAll(analyzes.stream().map(JeproLabAnalyzeRecord::new).collect(Collectors.toList()));
        Platform.runLater(() -> {
            if(!analyzeList.isEmpty()) {
                //jeproLabAnalyzeTableView.getItems().clear();
                //jeproLabAnalyzeTableView.setItems(analyzeList);
                jeproLabAnalyzePagination = new Pagination((analyzes.size()/ JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabAnalyzePagination.setPageFactory(this::createPage);
                VBox container = new VBox(5);
                container.getChildren().addAll(jeproLabAnalyzeSearchWrapper, jeproLabAnalyzePagination);
                jeproLabAnalyzeFormPanel.getChildren().add(container);
            }
        });
    }

    private Node createPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (analyzeList.size()));
        jeproLabAnalyzeTableView.setItems(FXCollections.observableArrayList(analyzeList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabAnalyzeTableView);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addAnalyzeBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addAnalyzeBtn.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeForm);
            JeproLab.getInstance().getApplicationForms().addAnalyzeForm.controller.initializeContent();
        });
        commandWrapper.getChildren().addAll(addAnalyzeBtn);
    }

    public static class JeproLabAnalyzeRecord {
        private SimpleIntegerProperty analyzeId;
        private SimpleIntegerProperty analyzeIndex;
        private SimpleBooleanProperty analyzeSelected;
        private SimpleBooleanProperty analyzePublished;
        private SimpleStringProperty analyzeName, analyzeReference, analyzeCategory, analyzePrice, analyzeBasePrice;

        public JeproLabAnalyzeRecord(JeproLabAnalyzeModel analyze){
            int langId = JeproLabContext.getContext().language.language_id;
            analyzeId = new SimpleIntegerProperty(analyze.analyze_id);
            analyzeIndex = new SimpleIntegerProperty(analyze.analyze_id);
            analyzeSelected = new SimpleBooleanProperty(false);
            analyzePublished = new SimpleBooleanProperty(analyze.published);
            analyzeName = new SimpleStringProperty(analyze.name.get("lang_" + langId));
            analyzeReference = new SimpleStringProperty(analyze.reference);
            analyzeCategory = new SimpleStringProperty(analyze.category_name);
            analyzePrice = new SimpleStringProperty(Float.toString(analyze.analyze_price.price));
            analyzeBasePrice = new SimpleStringProperty(Float.toString(analyze.base_price));
        }

        /**
         * Getters
         */
        public int getAnalyzeId(){
            return analyzeId.get();
        }

        public int getAnalyzeIndex(){
            return analyzeIndex.get();
        }

        public boolean getAnalyzeSelescted(){
            return analyzeSelected.get();
        }

        public String getAnalyzeName(){
            return analyzeName.get();
        }

        public String getAnalyzeReference(){
            return analyzeReference.get().toUpperCase();
        }

        public boolean getAnalyzePublished(){
            return analyzePublished.get();
        }

        public String getAnalyzeCategory(){
            return analyzeCategory.get();
        }

        public String getAnalyzePrice(){
            return analyzePrice.get();
        }

        public String getAnalyzeBasePrice(){
            return analyzeBasePrice.get();
        }

        /*public void delete(){
            JeproLabAnalyzeModel analyze = new JeproLabAnalyzeModel(getAnalyzeId(), true);
            if(analyze.analyze_id > 0){
                if(JeproLabSettingModel.getIntValue("advanced_stock_management") > 0 && analyze.advanced_stock_management){
                    JeproLabStockModel.JeproLabStockManager stockManager = JeproLabStockModel.JeproLabStockManagerFactory.getManager();
                    int quantity = stockManager.getAnalyzePhysicalQuantities(analyze.analyze_id, 0);
                    int realQuantity = stockManager.getAnalyzeRealQuantities(analyze.analyze_id, 0);

                    if(quantity > 0 || realQuantity > quantity){
                        JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_YOU_CANNOT_DELETE_THIS_ANALYZE_BECAUSE_THERE_STILL_PRODUCT_TO_RUN_IT_MESSAGE"));
                        JeproLab.getInstance().getApplicationForms().addAnalyzeForm.controller.has_errors = true;
                    }
                }

                if(!JeproLabContext.getContext().controller.has_errors){
                    if(analyze.delete()){
                        int categoryId = JeproLab.request.getRequest().containsKey("category_id") ? Integer.parseInt(JeproLab.request.getRequest().get("category_id")) : 0;
                        //Logger

                    }else{
                        JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_AN_ERROR_OCCURRED_WHILE_DELETING_ANALYZE_MESSAGE"));
                        JeproLabContext.getContext().controller.has_errors = true;
                    }
                }
            }else{
                JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_AN_ERROR_WHILE_DELETING_THE_ANALYZE_MESSAGE") + "\n" + JeproLab.getBundle().getString("JEPROLAB_UNABLE_TO_LOAD_ANALYZE_LABEL"));
            }
        }*/
    }

    public static class JeproLabCheckBoxCell extends TableCell<JeproLabAnalyzeRecord, Boolean>{
        private CheckBox analyzeCheckBox;

        public JeproLabCheckBoxCell(){
            analyzeCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabAnalyzeRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() < items.size())){
                setGraphic(analyzeCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabStatusCell extends TableCell<JeproLabAnalyzeRecord, Boolean>{
        private Button analyzeStatus;

        public JeproLabStatusCell(){
            analyzeStatus = new Button();
            int buttonSize = 18;
            analyzeStatus.setMinSize(buttonSize, buttonSize);
            analyzeStatus.setMaxSize(buttonSize, buttonSize);
            analyzeStatus.setPrefSize(buttonSize, buttonSize);
            analyzeStatus.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Boolean t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabAnalyzeRecord> items = getTableView().getItems();
            int ind = getIndex();
            if((items != null) && (ind >= 0 && ind < items.size())){
                if(items.get(ind).getAnalyzePublished()) {
                    analyzeStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    analyzeStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(analyzeStatus);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabActionCell extends TableCell<JeproLabAnalyzeRecord, HBox> {
        private HBox commandContainer;
        private Button editAnalyze, deleteAnalyze;

        public JeproLabActionCell() {
            commandContainer = new HBox(4);
            final int btnSize = 22;
            editAnalyze = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editAnalyze.setPrefSize(btnSize, btnSize);
            editAnalyze.setMaxSize(btnSize, btnSize);
            editAnalyze.setMinSize(btnSize, btnSize);
            editAnalyze.getStyleClass().add("icon-btn");

            deleteAnalyze = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteAnalyze.setPrefSize(btnSize, btnSize);
            deleteAnalyze.setMaxSize(btnSize, btnSize);
            deleteAnalyze.setMinSize(btnSize, btnSize);
            deleteAnalyze.getStyleClass().add("icon-btn");
            commandContainer.setAlignment(Pos.CENTER);
            commandContainer.getChildren().addAll(editAnalyze, deleteAnalyze);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
            //final ObservableList<JeproLabAnalyzeRecord> items = getTableView().getItems();
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabAnalyzeRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getAnalyzeIndex();
                editAnalyze.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeForm);
                    JeproLab.getInstance().getApplicationForms().addAnalyzeForm.controller.initializeContent(itemId);
                });
                deleteAnalyze.setOnAction(event -> {
                    Worker<Boolean> worker = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            if(isCancelled()){
                                return false;
                            }
                            JeproLabAnalyzeModel analyzeModel = new JeproLabAnalyzeModel(itemId, false);
                            return analyzeModel.delete();
                        }

                        @Override
                        protected void succeeded(){
                            super.succeeded();
                        }

                        @Override
                        protected void failed(){
                            super.failed();
                        }

                        @Override
                        protected void cancelled(){
                            super.cancelled();
                        }
                    };
                    new Thread((Task)worker).start();
                    JeproLabAnalyzeRecord record = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(record);
                    //todo ObservableList<JeproLabAnalyzeRecord> list = getTableView().getItems();
                    //getTableView().getItems().clear();
                    //getTableView().setItems(list);

                });
                setGraphic(commandContainer);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
