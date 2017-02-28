package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabLaboratoryModel;
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
public class JeproLabLaboratoryGroupController extends JeproLabController{
    private TableView<JeproLabLaboratoryGroupRecord> jeproLabLaboratoryGroupTableView;
    private HBox jeproLabLaboratoryGroupSearchFieldWrapper;
    private TextField jeproLabLaboratoryGroupSearchField;
    private ComboBox<String> jeproLabLaboratoryGroupSearchFilter;
    private Button jeproLabLaboratoryGroupSearchButton;
    private ObservableList<JeproLabLaboratoryGroupRecord> labGroupsList;
    private CheckBox checkAll;

    @FXML
    public VBox jeproLabLaboratoryGroupPanel;


    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double remainingWidth = (int)(0.98 * JeproLab.APP_WIDTH) - 125;

        jeproLabLaboratoryGroupTableView = new TableView<>();
        jeproLabLaboratoryGroupTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);
        //jeproLabLaboratoryGroupTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        //jeproLabLaboratoryGroupTableView.setLayoutY(10);

        checkAll = new CheckBox();
        TableColumn<JeproLabLaboratoryGroupRecord, String> jeproLabLaboratoryGroupIndexColumn = new TableColumn<>("#");
        jeproLabLaboratoryGroupIndexColumn.setPrefWidth(30);
        jeproLabLaboratoryGroupIndexColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryGroupIndex"));
        tableCellAlign(jeproLabLaboratoryGroupIndexColumn, Pos.CENTER_RIGHT);

        TableColumn<JeproLabLaboratoryGroupRecord, Boolean> jeproLabLaboratoryGroupCheckBoxColumn = new TableColumn<>();
        jeproLabLaboratoryGroupCheckBoxColumn.setPrefWidth(25);
        jeproLabLaboratoryGroupCheckBoxColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabLaboratoryGroupRecord, Boolean>, TableCell<JeproLabLaboratoryGroupRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabLaboratoryGroupCheckBoxCellFactory();
        jeproLabLaboratoryGroupCheckBoxColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabLaboratoryGroupRecord, String> jeproLabLaboratoryGroupNameColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryGroupNameColumn.setPrefWidth(0.40 * remainingWidth);
        tableCellAlign(jeproLabLaboratoryGroupNameColumn, Pos.CENTER_LEFT);
        jeproLabLaboratoryGroupNameColumn.setCellValueFactory(new PropertyValueFactory<>("laboratoryGroupName"));

        TableColumn<JeproLabLaboratoryGroupRecord, Button> jeproLabLaboratoryGroupShareCustomerColumn = new TableColumn<>(bundle.getString("JEPROLAB_SHARE_CUSTOMER_LABEL"));
        jeproLabLaboratoryGroupShareCustomerColumn.setPrefWidth(0.15 * remainingWidth);
        Callback<TableColumn<JeproLabLaboratoryGroupRecord, Button>, TableCell<JeproLabLaboratoryGroupRecord, Button>> shareCustomerCellFactory = param -> new JeproLabLaboratoryGroupShareCustomerCellFactory();
        jeproLabLaboratoryGroupShareCustomerColumn.setCellFactory(shareCustomerCellFactory);

        TableColumn<JeproLabLaboratoryGroupRecord, Button> jeproLabLaboratoryGroupShareRequestColumn = new TableColumn<>(bundle.getString("JEPROLAB_SHARE_REQUEST_LABEL"));
        jeproLabLaboratoryGroupShareRequestColumn.setPrefWidth(0.15 * remainingWidth);
        Callback<TableColumn<JeproLabLaboratoryGroupRecord, Button>, TableCell<JeproLabLaboratoryGroupRecord, Button>> shareRequestCellFactory = param -> new JeproLabLaboratoryGroupShareRequestsCellFactory();
        jeproLabLaboratoryGroupShareRequestColumn.setCellFactory(shareRequestCellFactory);

        TableColumn<JeproLabLaboratoryGroupRecord, Button> jeproLabLaboratoryGroupShareResultsColumn = new TableColumn<>(bundle.getString("JEPROLAB_SHARE_RESULTS_LABEL"));
        jeproLabLaboratoryGroupShareResultsColumn.setPrefWidth(0.15 * remainingWidth);
        Callback<TableColumn<JeproLabLaboratoryGroupRecord, Button>, TableCell<JeproLabLaboratoryGroupRecord, Button>> shareResultsCellFactory = param -> new JeproLabLaboratoryGroupShareResultsCellFactory();
        jeproLabLaboratoryGroupShareResultsColumn.setCellFactory(shareResultsCellFactory);

        TableColumn<JeproLabLaboratoryGroupRecord, Button> jeproLabLaboratoryGroupShareStockColumn = new TableColumn<>(bundle.getString("JEPROLAB_SHARE_STOCK_LABEL"));
        jeproLabLaboratoryGroupShareStockColumn.setPrefWidth(0.15 * remainingWidth);
        Callback<TableColumn<JeproLabLaboratoryGroupRecord, Button>, TableCell<JeproLabLaboratoryGroupRecord, Button>>  shareStockCellFactory = param -> new JeproLabLaboratoryGroupShareStockCellFactory();
        jeproLabLaboratoryGroupShareStockColumn.setCellFactory(shareStockCellFactory);

        TableColumn<JeproLabLaboratoryGroupRecord, HBox> jeproLabLaboratoryGroupActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabLaboratoryGroupActionColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabLaboratoryGroupRecord, HBox>, TableCell<JeproLabLaboratoryGroupRecord, HBox>> actionCellFactory = param -> new JeproLabLaboratoryGroupActionCellFactory();
        jeproLabLaboratoryGroupActionColumn.setCellFactory(actionCellFactory);

        jeproLabLaboratoryGroupTableView.getColumns().addAll(
            jeproLabLaboratoryGroupIndexColumn,
            jeproLabLaboratoryGroupCheckBoxColumn,
            jeproLabLaboratoryGroupNameColumn,
            jeproLabLaboratoryGroupShareCustomerColumn,
            jeproLabLaboratoryGroupShareRequestColumn,
            jeproLabLaboratoryGroupShareResultsColumn,
            jeproLabLaboratoryGroupShareStockColumn,
            jeproLabLaboratoryGroupActionColumn
        );

        jeproLabLaboratoryGroupSearchField = new TextField();

        jeproLabLaboratoryGroupSearchFilter = new ComboBox<>();
        jeproLabLaboratoryGroupSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));
        jeproLabLaboratoryGroupSearchButton = new Button();
        jeproLabLaboratoryGroupSearchButton.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabLaboratoryGroupSearchFieldWrapper = new HBox(5);
        jeproLabLaboratoryGroupSearchFieldWrapper.getChildren().addAll(
            jeproLabLaboratoryGroupSearchField,
            jeproLabLaboratoryGroupSearchFilter,
            jeproLabLaboratoryGroupSearchButton
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel> labLaboratoryGroupModelList;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                labLaboratoryGroupModelList = JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel.getLaboratoryGroups();
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
                updateLaboratoryGroupTableView(labLaboratoryGroupModelList);
            }
        };
        new Thread((Task)worker).start();
        updateToolBar();
    }

    @Override
    public void updateToolBar(){

    }

    private void updateLaboratoryGroupTableView(List<JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel> labGroups){
        labGroupsList = FXCollections.observableArrayList();
        labGroupsList.addAll(labGroups.stream().map(JeproLabLaboratoryGroupRecord::new).collect(Collectors.toList()));
        double padding = 0.01 * JeproLab.APP_WIDTH;
        if(!labGroupsList.isEmpty()) {
            Platform.runLater(() -> {
                Pagination jeproLabPagination = new Pagination((labGroupsList.size() / JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabPagination.setPageFactory(this::createLaboratoryGroupPage);

                VBox.setMargin(jeproLabLaboratoryGroupSearchFieldWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabPagination, new Insets(5, padding, 5, padding));

                jeproLabLaboratoryGroupPanel.getChildren().clear();
                jeproLabLaboratoryGroupPanel.getChildren().addAll(jeproLabLaboratoryGroupSearchFieldWrapper, jeproLabPagination);
            });
        }else{
            Platform.runLater(() -> {
                VBox.setMargin(jeproLabLaboratoryGroupTableView, new Insets(5, padding, 5, padding));
                jeproLabLaboratoryGroupPanel.getChildren().clear();
                VBox.setMargin(jeproLabLaboratoryGroupSearchFieldWrapper, new Insets(5, padding, 5, padding));

                jeproLabLaboratoryGroupPanel.getChildren().addAll(jeproLabLaboratoryGroupSearchFieldWrapper, jeproLabLaboratoryGroupTableView);
            });
        }
    }

    private Node createLaboratoryGroupPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (labGroupsList.size()));
        jeproLabLaboratoryGroupTableView.setItems(FXCollections.observableArrayList(labGroupsList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabLaboratoryGroupTableView);
    }

    public static class JeproLabLaboratoryGroupRecord{
        private SimpleIntegerProperty laboratoryGroupIndex;
        private SimpleStringProperty laboratoryGroupName;
        private SimpleBooleanProperty laboratoryGroupShareCustomer, laboratoryGroupShareRequest, laboratoryGroupShareResults, laboratoryGroupShareStock;

        public JeproLabLaboratoryGroupRecord(JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel labLaboratoryGroup){
            laboratoryGroupIndex = new SimpleIntegerProperty(labLaboratoryGroup.laboratory_group_id);
            laboratoryGroupName = new SimpleStringProperty(labLaboratoryGroup.name);
            laboratoryGroupShareCustomer = new SimpleBooleanProperty(labLaboratoryGroup.share_customers);
            laboratoryGroupShareRequest = new SimpleBooleanProperty(labLaboratoryGroup.share_requests);
            laboratoryGroupShareResults = new SimpleBooleanProperty(labLaboratoryGroup.share_results);
            laboratoryGroupShareStock = new SimpleBooleanProperty(labLaboratoryGroup.share_stocks);
        }

        public int getLaboratoryGroupIndex(){
            return laboratoryGroupIndex.get();
        }

        public String getLaboratoryGroupName(){
            return laboratoryGroupName.get();
        }

        public boolean getLaboratoryGroupShareCustomer(){ return laboratoryGroupShareCustomer.get(); }

        public boolean getLaboratoryGroupShareResults(){ return laboratoryGroupShareResults.get(); }

        public boolean getLaboratoryGroupShareRequest(){ return laboratoryGroupShareRequest.get(); }

        public boolean getLaboratoryGroupShareStock(){ return laboratoryGroupShareStock.get(); }
    }

    public static class JeproLabLaboratoryGroupCheckBoxCellFactory extends TableCell<JeproLabLaboratoryGroupRecord, Boolean>{
        private CheckBox checkBox;
        public JeproLabLaboratoryGroupCheckBoxCellFactory(){
            checkBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item);}

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabLaboratoryGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(checkBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabLaboratoryGroupShareCustomerCellFactory extends TableCell<JeproLabLaboratoryGroupRecord, Button>{
        private Button shareCustomerButton;

        public JeproLabLaboratoryGroupShareCustomerCellFactory(){
            shareCustomerButton = new Button("");
            shareCustomerButton.setPrefSize(btnSize, btnSize);
            shareCustomerButton.setMinSize(btnSize, btnSize);
            shareCustomerButton.setMaxSize(btnSize, btnSize);
            shareCustomerButton.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabLaboratoryGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getLaboratoryGroupShareCustomer()) {
                    shareCustomerButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    shareCustomerButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                shareCustomerButton.setOnAction(evt -> {

                });
                setGraphic(shareCustomerButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabLaboratoryGroupShareRequestsCellFactory extends TableCell<JeproLabLaboratoryGroupRecord, Button>{
        private Button shareRequestButton;

        public JeproLabLaboratoryGroupShareRequestsCellFactory(){
            shareRequestButton = new Button("");
            shareRequestButton.setPrefSize(btnSize, btnSize);
            shareRequestButton.setMinSize(btnSize, btnSize);
            shareRequestButton.setMaxSize(btnSize, btnSize);
            shareRequestButton.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabLaboratoryGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getLaboratoryGroupShareRequest()) {
                    shareRequestButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    shareRequestButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                shareRequestButton.setOnAction(evt -> {

                });
                setGraphic(shareRequestButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabLaboratoryGroupShareResultsCellFactory extends TableCell<JeproLabLaboratoryGroupRecord, Button>{
        private Button shareResultButton;

        public JeproLabLaboratoryGroupShareResultsCellFactory(){
            shareResultButton = new Button("");
            shareResultButton.setPrefSize(btnSize, btnSize);
            shareResultButton.setMinSize(btnSize, btnSize);
            shareResultButton.setMaxSize(btnSize, btnSize);
            shareResultButton.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabLaboratoryGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getLaboratoryGroupShareResults()) {
                    shareResultButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    shareResultButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                shareResultButton.setOnAction(evt -> {

                });
                setGraphic(shareResultButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabLaboratoryGroupShareStockCellFactory extends TableCell<JeproLabLaboratoryGroupRecord, Button>{
        private Button shareStockButton;

        public JeproLabLaboratoryGroupShareStockCellFactory(){
            shareStockButton = new Button("");
            shareStockButton.setPrefSize(btnSize, btnSize);
            shareStockButton.setMinSize(btnSize, btnSize);
            shareStockButton.setMaxSize(btnSize, btnSize);
            shareStockButton.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabLaboratoryGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getLaboratoryGroupShareStock()) {
                    shareStockButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    shareStockButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                shareStockButton.setOnAction(evt -> {

                });
                setGraphic(shareStockButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabLaboratoryGroupActionCellFactory extends TableCell<JeproLabLaboratoryGroupRecord, HBox>{
        private HBox commandWrapper;
        private Button editButton, deleteButton;

        public JeproLabLaboratoryGroupActionCellFactory(){
            editButton = new Button("");
            editButton.setPrefSize(btnSize, btnSize);
            editButton.setMinSize(btnSize, btnSize);
            editButton.setMaxSize(btnSize, btnSize);
            editButton.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteButton = new Button("");
            deleteButton.setPrefSize(btnSize, btnSize);
            deleteButton.setMinSize(btnSize, btnSize);
            deleteButton.setMaxSize(btnSize, btnSize);
            deleteButton.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper = new HBox(10);
            commandWrapper.getChildren().addAll(editButton, deleteButton);
        }

        @Override
        public void commitEdit(HBox item){ super.commitEdit(item);}

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabLaboratoryGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getLaboratoryGroupIndex();
                editButton.setOnAction(evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addLaboratoriesGroupForm);
                    JeproLab.getInstance().getApplicationForms().addLaboratoriesGroupForm.controller.initializeContent(itemId);
                });

                deleteButton.setOnAction(evt -> {

                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
