package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabGroupModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabGroupController extends JeproLabController{
    private CheckBox groupCheckAll;
    private TableView<JeproLabGroupRecord> jeproLabGroupTableView;
    private Button groupsSearchBtn, addGroupBtn;
    private TextField groupsSearchField;
    private ComboBox<String> groupsSearchFilter;
    private HBox jeproLabGroupSearchWrapper;
    private Pagination jeproLabGroupPagination;

    private ObservableList<JeproLabGroupRecord> groupList;

    @FXML
    public VBox jeproLabGroupWrapper;
    
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 127;

        groupCheckAll = new CheckBox();

        groupsSearchBtn = new Button();
        groupsSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        groupsSearchField = new TextField();
        groupsSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        groupsSearchFilter = new ComboBox<>();
        groupsSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabGroupSearchWrapper = new HBox(5);
        jeproLabGroupSearchWrapper.getChildren().addAll(groupsSearchField, groupsSearchFilter, groupsSearchBtn);


        TableColumn<JeproLabGroupRecord, Integer> jeproLabGroupIndexTableColumn = new TableColumn<>("#");
        jeproLabGroupIndexTableColumn.setPrefWidth(30);
        tableCellAlign(jeproLabGroupIndexTableColumn, Pos.CENTER_RIGHT);
        jeproLabGroupIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupIndex"));

        TableColumn<JeproLabGroupRecord, Boolean> jeproLabGroupCheckBoxTableColumn = new TableColumn<>();
        jeproLabGroupCheckBoxTableColumn.setPrefWidth(25);
        tableCellAlign(jeproLabGroupCheckBoxTableColumn, Pos.CENTER);
        jeproLabGroupCheckBoxTableColumn.setGraphic(groupCheckAll);
        Callback<TableColumn<JeproLabGroupRecord, Boolean>, TableCell<JeproLabGroupRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabGroupCheckBoxCellFactory();
        jeproLabGroupCheckBoxTableColumn.setCellFactory(checkBoxCellFactory);

        /*TableColumn<JeproLabGroupRecord, Boolean> jeproLabGroupStatusTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabGroupStatusTableColumn.setPrefWidth(55);
        tableCellAlign(jeproLabGroupStatusTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabGroupRecord, Boolean>, TableCell<JeproLabGroupRecord, Boolean>> statusCellFactory = params -> new JeproLabGroupStatusCellFactory();
        jeproLabGroupStatusTableColumn.setCellFactory(statusCellFactory);*/

        TableColumn<JeproLabGroupRecord, String> jeproLabGroupNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabGroupNameTableColumn.setPrefWidth(0.22 * remainingWidth);
        tableCellAlign(jeproLabGroupNameTableColumn, Pos.CENTER_LEFT);
        jeproLabGroupNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));

        TableColumn<JeproLabGroupRecord, String> jeproLabGroupReductionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_REDUCTION_LABEL"));
        jeproLabGroupReductionTableColumn.setPrefWidth(0.2 * remainingWidth);
        tableCellAlign(jeproLabGroupReductionTableColumn, Pos.CENTER);
        jeproLabGroupReductionTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupReduction"));

        TableColumn<JeproLabGroupRecord, Integer> jeproLabGroupMembersTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_MEMBERS_LABEL"));
        jeproLabGroupMembersTableColumn.setPrefWidth(0.20 * remainingWidth);
        tableCellAlign(jeproLabGroupMembersTableColumn, Pos.CENTER);
        jeproLabGroupMembersTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupMembers"));

        TableColumn<JeproLabGroupRecord, Button> jeproLabGroupDisplayPriceTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_SHOW_PRICE_LABEL"));
        jeproLabGroupDisplayPriceTableColumn.setPrefWidth(0.20 * remainingWidth);
        tableCellAlign(jeproLabGroupDisplayPriceTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabGroupRecord, Button>, TableCell<JeproLabGroupRecord, Button>> displayPriceFactory = params -> new JeproLabGroupDisplayPriceCellFactory();
        jeproLabGroupDisplayPriceTableColumn.setCellFactory(displayPriceFactory);

        TableColumn<JeproLabGroupRecord, String> jeproLabGroupCreatedDateTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));
        jeproLabGroupCreatedDateTableColumn.setPrefWidth(0.18 * remainingWidth);
        tableCellAlign(jeproLabGroupCreatedDateTableColumn, Pos.CENTER);
        jeproLabGroupCreatedDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupCreationDate"));

        TableColumn<JeproLabGroupRecord, HBox> jeproLabGroupActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabGroupActionTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabGroupRecord, HBox>, TableCell<JeproLabGroupRecord, HBox>> actionCellFactory = params -> new JeproLabGroupActionCellFactory();
        jeproLabGroupActionTableColumn.setCellFactory(actionCellFactory);

        jeproLabGroupTableView = new TableView<>();
        jeproLabGroupTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, JeproLabConfigurationSettings.LIST_LIMIT * rowHeight);
        jeproLabGroupTableView.getColumns().addAll(
            jeproLabGroupIndexTableColumn, jeproLabGroupCheckBoxTableColumn, jeproLabGroupNameTableColumn,
            jeproLabGroupReductionTableColumn, jeproLabGroupMembersTableColumn, jeproLabGroupDisplayPriceTableColumn,
            jeproLabGroupCreatedDateTableColumn, jeproLabGroupActionTableColumn
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabGroupModel> groups;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                groups = JeproLabGroupModel.getGroups(JeproLabContext.getContext().language.language_id);
                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void cancelled(){
                super.cancelled();
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                System.out.println(groups.size());
                updateTableView(groups);
            }
        };
        new Thread((Task)worker).start();
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addGroupBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addGroupBtn.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addGroupForm);
            JeproLab.getInstance().getApplicationForms().addGroupForm.controller.initializeContent();
        });
        commandWrapper.getChildren().addAll(addGroupBtn);
    }

    private void updateTableView(List<JeproLabGroupModel> groups){
        groupList = FXCollections.observableArrayList();
        groupList.addAll(groups.stream().map(JeproLabGroupRecord::new).collect(Collectors.toList()));
        Platform.runLater(() -> {
            if(!groupList.isEmpty()){
                jeproLabGroupPagination = new Pagination((groupList.size()/ JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabGroupPagination.setPageFactory(this::createGroupPage);
                jeproLabGroupWrapper.getChildren().clear();
                VBox.setMargin(jeproLabGroupSearchWrapper, new Insets(5, 0.01 * JeproLab.APP_WIDTH, 5, 0.01*JeproLab.APP_WIDTH));
                VBox.setMargin(jeproLabGroupPagination, new Insets(5, 0.01 * JeproLab.APP_WIDTH, 5, 0.01*JeproLab.APP_WIDTH));
                jeproLabGroupWrapper.getChildren().addAll(jeproLabGroupSearchWrapper, jeproLabGroupPagination);
            }
        });
    }

    private Node createGroupPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (groupList.size()));
        jeproLabGroupTableView.setItems(FXCollections.observableArrayList(groupList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabGroupTableView);
    }
    
    public static class JeproLabGroupRecord {
        private SimpleIntegerProperty groupIndex;
        private SimpleIntegerProperty groupMembers;
        private SimpleStringProperty groupName;
        private SimpleStringProperty groupReduction;
        private SimpleBooleanProperty groupDisplayPrice;
        private SimpleStringProperty groupCreationDate;
        private SimpleBooleanProperty groupSelected;
        private SimpleBooleanProperty groupStatus;

        public JeproLabGroupRecord(JeproLabGroupModel group){
            this(group, new ArrayList<>());
        }

        public JeproLabGroupRecord(JeproLabGroupModel group, List<Integer> selectedGroups){
            int langId = JeproLabContext.getContext().language.language_id;
            groupIndex = new SimpleIntegerProperty(group.group_id);
            groupMembers = new SimpleIntegerProperty(group.members);
            groupName = new SimpleStringProperty(group.name.get("lang_" + langId));
            groupReduction = new SimpleStringProperty();
            groupDisplayPrice = new SimpleBooleanProperty(group.show_prices);
            //groupStatus = new SimpleBooleanProperty(group.p)
            groupCreationDate = new SimpleStringProperty();
            groupSelected = new SimpleBooleanProperty();
        }

        public int getGroupIndex(){
            return groupIndex.get();
        }

        public int getGroupMembers(){
            return groupMembers.get();
        }

        public String getGroupName(){
            return groupName.get();
        }

        public String getGroupReduction(){
            return groupReduction.get();
        }

        public boolean getGroupDisplayPrice(){
            return groupDisplayPrice.get();
        }

        public String getGroupCreationDate(){
            return groupCreationDate.get();
        }

        public boolean getGroupSelected(){
            return groupSelected.get();
        }

    }

    public static class JeproLabGroupCheckBoxCellFactory extends TableCell<JeproLabGroupRecord, Boolean>{
        private CheckBox groupCheckBox;

        public JeproLabGroupCheckBoxCellFactory(){
            groupCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item);}

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                setAlignment(Pos.CENTER);
                setGraphic(groupCheckBox);
            }
        }
    }

    public static class JeproLabGroupStatusCellFactory extends TableCell<JeproLabGroupRecord, Boolean>{
        private Button groupStatusBtn;

        public JeproLabGroupStatusCellFactory() {
            groupStatusBtn = new Button();
            groupStatusBtn.setPrefSize(btnSize, btnSize);
            groupStatusBtn.setMinSize(btnSize, btnSize);
            groupStatusBtn.setMaxSize(btnSize, btnSize);
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item);}

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                //if(items.get(getIndex()).)
                setAlignment(Pos.CENTER);
                setGraphic(groupStatusBtn);
            }
        }
    }

    public static class JeproLabGroupDisplayPriceCellFactory extends TableCell<JeproLabGroupRecord, Button>{
        private Button groupShowPriceBtn;

        public JeproLabGroupDisplayPriceCellFactory(){
            groupShowPriceBtn = new Button();
            groupShowPriceBtn.setPrefSize(btnSize, btnSize);
            groupShowPriceBtn.setMaxSize(btnSize, btnSize);
            groupShowPriceBtn.setMinSize(btnSize, btnSize);
            groupShowPriceBtn.getStyleClass().add("icon-btn");
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item);}

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            final ObservableList<JeproLabGroupRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                if(items.get(getIndex()).getGroupDisplayPrice()) {
                    groupShowPriceBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    groupShowPriceBtn.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setAlignment(Pos.CENTER);
                setGraphic(groupShowPriceBtn);
            }
        }
    }

    public static class JeproLabGroupActionCellFactory extends TableCell<JeproLabGroupRecord, HBox>{
        private HBox commandWrapper;
        private Button editGroupBtn, deleteGroupBtn;

        public JeproLabGroupActionCellFactory(){
            commandWrapper = new HBox(4);

            editGroupBtn = new Button();
            editGroupBtn.setPrefSize(btnSize, btnSize);
            editGroupBtn.setMinSize(btnSize, btnSize);
            editGroupBtn.setMaxSize(btnSize, btnSize);
            editGroupBtn.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteGroupBtn = new Button();
            deleteGroupBtn.setPrefSize(btnSize, btnSize);
            deleteGroupBtn.setMinSize(btnSize, btnSize);
            deleteGroupBtn.setMaxSize(btnSize, btnSize);
            deleteGroupBtn.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper.setAlignment(Pos.CENTER);
            commandWrapper.getChildren().addAll(editGroupBtn, deleteGroupBtn);
        }

        @Override
        public void commitEdit(HBox t){ super.commitEdit(t);}

        @Override
        public void updateItem(HBox t, boolean empty){
            super.updateItem(t, empty);
            final ObservableList<JeproLabGroupRecord> items = getTableView().getItems();
            int index = getIndex();
            if((items != null) && (index >= 0 && index < items.size())){

                setAlignment(Pos.CENTER);
                setGraphic(commandWrapper);
            }
        }
    }
}
