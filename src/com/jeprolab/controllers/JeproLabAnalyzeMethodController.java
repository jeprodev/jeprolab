package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.models.JeproLabAnalyzeModel;
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
public class JeproLabAnalyzeMethodController extends JeproLabController {
    private CheckBox checkAll;
    private Button addMethodBtn;
    @FXML
    public HBox jeproLabSearchMethodWrapper;
    public TextField jeproLabSearchMethod;
    public Button jeproLabSearchMethodBtn;
    public JeproFormPanel jeproLabAnalyzeMethodFormWrapper;
    public TableView<JeproLabMethodRecord> jeproLabAnalyzeMethodTableView;
    public TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodIndexColumn;
    public TableColumn<JeproLabMethodRecord, Boolean> jeproLabAnalyzeMethodCheckBoxColumn;
    public TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodDesignationColumn;
    public TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodCodeColumn;
    public TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodThresholdColumn;
    public TableColumn<JeproLabMethodRecord, String> jeproLabAnalyzeMethodThresholdUnitColumn;
    public TableColumn<JeproLabMethodRecord, HBox> jeproLabAnalyzeMethodActionColumn;

    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 112;
        checkAll = new CheckBox();

        jeproLabAnalyzeMethodTableView.setPrefSize(formWidth, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);
        VBox.setMargin(jeproLabSearchMethodWrapper, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));
        VBox.setMargin(jeproLabAnalyzeMethodTableView, new Insets(5, 0, 0, 0.01 * JeproLab.APP_WIDTH));

        jeproLabSearchMethod.setPromptText(JeproLab.getBundle().getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabAnalyzeMethodIndexColumn.setPrefWidth(30);
        jeproLabAnalyzeMethodIndexColumn.setText("#");
        tableCellAlign(jeproLabAnalyzeMethodIndexColumn, Pos.CENTER_RIGHT);
        jeproLabAnalyzeMethodIndexColumn.setCellValueFactory(new PropertyValueFactory<>("methodIndex"));

        jeproLabAnalyzeMethodCheckBoxColumn.setPrefWidth(20);
        jeproLabAnalyzeMethodCheckBoxColumn.setGraphic(checkAll);
        Callback<TableColumn<JeproLabMethodRecord, Boolean>, TableCell<JeproLabMethodRecord, Boolean>> checkBoxFactory = param -> new JeproLabCheckBoxCell();
        jeproLabAnalyzeMethodCheckBoxColumn.setCellFactory(checkBoxFactory);

        jeproLabAnalyzeMethodDesignationColumn.setText(bundle.getString("JEPROLAB_DESIGNATION_LABEL"));
        jeproLabAnalyzeMethodDesignationColumn.setPrefWidth(0.64 * remainingWidth);
        tableCellAlign(jeproLabAnalyzeMethodDesignationColumn, Pos.CENTER_LEFT);
        jeproLabAnalyzeMethodDesignationColumn.setCellValueFactory(new PropertyValueFactory<>("methodDesignation"));

        jeproLabAnalyzeMethodCodeColumn.setPrefWidth(0.12 * remainingWidth);
        jeproLabAnalyzeMethodCodeColumn.setText(bundle.getString("JEPROLAB_CODE_LABEL"));
        tableCellAlign(jeproLabAnalyzeMethodCodeColumn, Pos.CENTER);
        jeproLabAnalyzeMethodCodeColumn.setCellValueFactory(new PropertyValueFactory<>("methodCode"));

        jeproLabAnalyzeMethodThresholdColumn.setPrefWidth(0.12 * remainingWidth);
        jeproLabAnalyzeMethodThresholdColumn.setText(bundle.getString("JEPROLAB_THRESHOLD_LABEL"));
        tableCellAlign(jeproLabAnalyzeMethodThresholdColumn, Pos.CENTER);
        jeproLabAnalyzeMethodThresholdColumn.setCellValueFactory(new PropertyValueFactory<>("methodThreshold"));

        jeproLabAnalyzeMethodThresholdUnitColumn.setPrefWidth(0.12 * remainingWidth);
        jeproLabAnalyzeMethodThresholdUnitColumn.setText(bundle.getString("JEPROLAB_UNIT_LABEL"));
        tableCellAlign(jeproLabAnalyzeMethodThresholdUnitColumn, Pos.CENTER);
        jeproLabAnalyzeMethodThresholdUnitColumn.setCellValueFactory(new PropertyValueFactory<>("methodThresholdUnit"));

        jeproLabAnalyzeMethodActionColumn.setPrefWidth(60);
        jeproLabAnalyzeMethodActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        //jeproLabAnalyzeMethodActionColumn;
        Callback<TableColumn<JeproLabMethodRecord, HBox>, TableCell<JeproLabMethodRecord, HBox>> actionFactory = param -> new JeproLabActionCell();
        jeproLabAnalyzeMethodActionColumn.setCellFactory(actionFactory);
    }

    @Override
    public void initializeContent(){
        List<JeproLabAnalyzeModel.JeproLabMethodModel> analyzes = JeproLabAnalyzeModel.JeproLabMethodModel.getMethods();
        ObservableList<JeproLabMethodRecord> analyzeList = FXCollections.observableArrayList();
        if(!analyzes.isEmpty()){
            analyzeList.addAll(analyzes.stream().map(JeproLabMethodRecord::new).collect(Collectors.toList()));
            jeproLabAnalyzeMethodTableView.setItems(analyzeList);
        }
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
