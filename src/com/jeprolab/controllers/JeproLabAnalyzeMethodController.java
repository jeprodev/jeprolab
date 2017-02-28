package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabAnalyzeModel;
import javafx.application.Platform;
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
public class JeproLabAnalyzeMethodController extends JeproLabController {
    private CheckBox checkAll;
    private Button addMethodBtn;
    private HBox jeproLabSearchMethodWrapper;
    private TextField jeproLabSearchMethodField;
    private ComboBox<String> jeproLabSearchMethodFilter;
    private Button jeproLabSearchMethodBtn;
    private TableView<JeproLabMethodRecord> jeproLabAnalyzeMethodTableView;
    private ObservableList<JeproLabMethodRecord> methodsList;

    @FXML
    public JeproFormPanel jeproLabAnalyzeMethodFormWrapper;
    public VBox jeproLabAnalyzeMethodWrapper;


    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 112;
        checkAll = new CheckBox();

        jeproLabAnalyzeMethodTableView = new TableView<>();
        jeproLabAnalyzeMethodTableView.setPrefSize(formWidth, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);

        TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodIndexColumn = new TableColumn<>("#");
        jeproLabAnalyzeMethodIndexColumn.setPrefWidth(30);
        tableCellAlign(jeproLabAnalyzeMethodIndexColumn, Pos.CENTER_RIGHT);
        jeproLabAnalyzeMethodIndexColumn.setCellValueFactory(new PropertyValueFactory<>("methodIndex"));

        TableColumn<JeproLabMethodRecord, Boolean> jeproLabAnalyzeMethodCheckBoxColumn = new TableColumn<>();
        jeproLabAnalyzeMethodCheckBoxColumn.setPrefWidth(20);
        jeproLabAnalyzeMethodCheckBoxColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabMethodRecord, Boolean>, TableCell<JeproLabMethodRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabAnalyzeMethodCheckBoxColumn.setCellFactory(checkBoxFactory);

        TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodDesignationColumn = new TableColumn<>(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabAnalyzeMethodDesignationColumn.setPrefWidth(0.64 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeMethodDesignationColumn, Pos.CENTER_LEFT);
        jeproLabAnalyzeMethodDesignationColumn.setCellValueFactory(new PropertyValueFactory<>("methodDesignation"));

        TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodCodeColumn = new TableColumn<>(bundle.getString("JEPROLAB_CODE_LABEL"));
        jeproLabAnalyzeMethodCodeColumn.setPrefWidth(0.12 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeMethodCodeColumn, Pos.CENTER);
        jeproLabAnalyzeMethodCodeColumn.setCellValueFactory(new PropertyValueFactory<>("methodCode"));

        TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodThresholdColumn = new TableColumn<>(bundle.getString("JEPROLAB_THRESHOLD_LABEL"));
        jeproLabAnalyzeMethodThresholdColumn.setPrefWidth(0.12 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeMethodThresholdColumn, Pos.CENTER);
        jeproLabAnalyzeMethodThresholdColumn.setCellValueFactory(new PropertyValueFactory<>("methodThreshold"));

        TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodThresholdUnitColumn = new TableColumn<>(bundle.getString("JEPROLAB_UNIT_LABEL"));
        jeproLabAnalyzeMethodThresholdUnitColumn.setPrefWidth(0.12 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeMethodThresholdUnitColumn, Pos.CENTER);
        jeproLabAnalyzeMethodThresholdUnitColumn.setCellValueFactory(new PropertyValueFactory<>("methodThresholdUnit"));

        TableColumn<JeproLabMethodRecord, HBox> jeproLabAnalyzeMethodActionColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabAnalyzeMethodActionColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabMethodRecord, HBox>, TableCell<JeproLabMethodRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabAnalyzeMethodActionColumn.setCellFactory(actionFactory);

        jeproLabAnalyzeMethodTableView.getColumns().addAll(
            jeproLabAnalyzeMethodIndexColumn, jeproLabAnalyzeMethodCheckBoxColumn, jeproLabAnalyzeMethodDesignationColumn,
            jeproLabAnalyzeMethodCodeColumn, jeproLabAnalyzeMethodThresholdColumn,
            jeproLabAnalyzeMethodThresholdUnitColumn,
            jeproLabAnalyzeMethodActionColumn
        );

        jeproLabSearchMethodField = new TextField();
        jeproLabSearchMethodField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabSearchMethodFilter = new ComboBox<>();
        jeproLabSearchMethodFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabSearchMethodBtn = new Button("");
        jeproLabSearchMethodBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabSearchMethodWrapper = new HBox(5);
        jeproLabSearchMethodWrapper.getChildren().addAll(jeproLabSearchMethodField, jeproLabSearchMethodFilter, jeproLabSearchMethodBtn);
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabAnalyzeModel.JeproLabMethodModel> methods;

            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                methods = JeproLabAnalyzeModel.JeproLabMethodModel.getMethods();
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
                updateMethodsTableView(methods);
            }
        };

        new Thread((Task)worker).start();

        /*ObservableList<JeproLabMethodRecord> analyzeList = FXCollections.observableArrayList();
        if(!analyzes.isEmpty()){
            analyzeList.addAll(analyzes.stream().map(JeproLabMethodRecord::new).collect(Collectors.toList()));
            jeproLabAnalyzeMethodTableView.setItems(analyzeList);
        }*/
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addMethodBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL")); //, new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addMethodBtn.getStyleClass().add("add-btn");
        addMethodBtn.setOnAction(event ->{
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm);
            JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm.controller.initializeContent();

        });
        commandWrapper.getChildren().addAll(addMethodBtn);
    }

    private void updateMethodsTableView(List<JeproLabAnalyzeModel.JeproLabMethodModel> methods){
        methodsList = FXCollections.observableArrayList();
        methodsList.addAll(methods.stream().map(JeproLabMethodRecord::new).collect(Collectors.toList()));
        double padding = 0.01 * JeproLab.APP_WIDTH;
        if(!methodsList.isEmpty()){

            Platform.runLater(() -> {
                jeproLabAnalyzeMethodWrapper.getChildren().clear();
                Pagination jeproLabPagination = new Pagination((methodsList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabPagination.setPageFactory(this::createMethodsPage);

                VBox.setMargin(jeproLabSearchMethodWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabPagination, new Insets(5, padding, 5, padding));

                jeproLabAnalyzeMethodWrapper.getChildren().addAll(jeproLabSearchMethodWrapper, jeproLabPagination);
            });
        }else {
            Platform.runLater(() -> {
                jeproLabAnalyzeMethodWrapper.getChildren().clear();
                VBox.setMargin(jeproLabSearchMethodWrapper, new Insets(5, padding, 5, padding));
                VBox.setMargin(jeproLabAnalyzeMethodTableView, new Insets(5, padding, 5, padding));

                jeproLabAnalyzeMethodWrapper.getChildren().addAll(jeproLabSearchMethodWrapper, jeproLabAnalyzeMethodTableView);
            });
        }
    }

    private Node createMethodsPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (methodsList.size()));
        jeproLabAnalyzeMethodTableView.setItems(FXCollections.observableArrayList(methodsList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabAnalyzeMethodTableView);
    }

    public static class JeproLabMethodRecord{
        public SimpleStringProperty methodDesignation, methodCode, methodThreshold, methodThresholdUnit;
        public SimpleIntegerProperty methodIndex;

        public JeproLabMethodRecord(JeproLabAnalyzeModel.JeproLabMethodModel method){
            methodDesignation = new SimpleStringProperty(method.name);
            methodCode = new SimpleStringProperty(method.code);
            methodThreshold = new SimpleStringProperty(String.valueOf(method.threshold));
            methodThresholdUnit = new SimpleStringProperty(method.unit);
            methodIndex = new SimpleIntegerProperty(method.method_id);
        }

        public String getMethodDesignation(){
            return methodDesignation.get();
        }

        public String getMethodCode(){
            return methodCode.get();
        }

        public String getMethodThreshold(){
            return methodThreshold.get();
        }

        public String getMethodThresholdUnit(){
            return methodThresholdUnit.get();
        }

        public int getMethodIndex(){
            return methodIndex.get();
        }
    }

    public class JeproLabCheckBoxCell extends TableCell<JeproLabMethodRecord, Boolean>{
        private CheckBox methodSelected;

        public JeproLabCheckBoxCell(){
            methodSelected = new CheckBox();
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabMethodRecord> items = getTableView().getItems();
            int ind = getIndex();
            if((items != null) && (ind >= 0 && ind < items.size())){
                /*if(items.get(ind).getAnalyzePublished()) {
                    analyzeStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    analyzeStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }*/
                setGraphic(methodSelected);
                setAlignment(Pos.CENTER);
            }
        }

    }

    public class JeproLabActionCell extends TableCell<JeproLabMethodRecord, HBox>{
        private Button editBtn, deleteBtn;
        private HBox commandWrapper;
        private final int btnSize = 20;

        public JeproLabActionCell(){
            editBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/edit.png"))));
            editBtn.setPrefSize(btnSize, btnSize);
            editBtn.setMinSize(btnSize, btnSize);
            editBtn.setMaxSize(btnSize, btnSize);
            editBtn.getStyleClass().add("icon-btn");

            deleteBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/trash-icon.png"))));
            deleteBtn.setMinSize(btnSize, btnSize);
            deleteBtn.setMaxSize(btnSize, btnSize);
            deleteBtn.setPrefSize(btnSize, btnSize);
            deleteBtn.getStyleClass().add("icon-btn");

            commandWrapper =  new HBox(10);
            commandWrapper.getChildren().addAll(editBtn, deleteBtn);
            commandWrapper.setAlignment(Pos.CENTER);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabMethodRecord> items = getTableView().getItems();
            int index = getIndex();
            if((items != null) && (index >= 0 && index < items.size())){
                int itemId = items.get(index).getMethodIndex();
                editBtn.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm);
                    JeproLab.getInstance().getApplicationForms().addAnalyzeMethodForm.controller.initializeContent(itemId);
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
