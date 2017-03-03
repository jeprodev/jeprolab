package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabFeedModel;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.log4j.Level;


import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeedController extends JeproLabController {
    private TableView<JeproLabFeedRecord> jeproLabFeedTableView;
    private TextField jeproLabFeedSearchField;
    private ComboBox<String>jeproLabFeedSearchFilter;
    private Button jeproLabFeedSearchBtn, addFeedBtn;
    private HBox jeproLabFeedSearchWrapper;
    private ObservableList<JeproLabFeedRecord> feedList;
    private CheckBox checkAll;

    @FXML
    public VBox jeproLabFeedListWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 120;

        jeproLabFeedSearchField = new TextField();
        jeproLabFeedSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));

        jeproLabFeedSearchFilter = new ComboBox<>();
        jeproLabFeedSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));

        jeproLabFeedSearchBtn = new Button();
        jeproLabFeedSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabFeedSearchWrapper = new HBox(5);
        jeproLabFeedSearchWrapper.getChildren().addAll(
            jeproLabFeedSearchField, jeproLabFeedSearchFilter, jeproLabFeedSearchBtn
        );

        jeproLabFeedTableView = new TableView<>();
        jeproLabFeedTableView.setPrefSize(formWidth, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedIndexTableColumn = new TableColumn<>("#");
        jeproLabFeedIndexTableColumn.setPrefWidth(30);
        jeproLabFeedIndexTableColumn.setEditable(false);
        jeproLabFeedIndexTableColumn.setResizable(false);
        jeproLabFeedIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("feedIndex"));
        tableCellAlign(jeproLabFeedIndexTableColumn, Pos.CENTER_RIGHT);

        checkAll = new CheckBox();
        TableColumn<JeproLabFeedRecord, Boolean> jeproLabCheckBoxTableColumn = new TableColumn<>();
        jeproLabCheckBoxTableColumn.setPrefWidth(20);
        jeproLabCheckBoxTableColumn.setGraphic(checkAll);
        jeproLabCheckBoxTableColumn.setEditable(false);
        jeproLabCheckBoxTableColumn.setResizable(false);
        Callback<TableColumn<JeproLabFeedRecord, Boolean>, TableCell<JeproLabFeedRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabFeedCheckBoxCellFactory();
        jeproLabCheckBoxTableColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedAuthorTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_AUTHOR_LABEL"));
        jeproLabFeedAuthorTableColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabFeedAuthorTableColumn.setEditable(false);
        jeproLabFeedAuthorTableColumn.setResizable(false);
        jeproLabFeedAuthorTableColumn.setCellValueFactory(new PropertyValueFactory<>("feedAuthor"));
        tableCellAlign(jeproLabFeedAuthorTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedTitleTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_TITLE_LABEL"));
        jeproLabFeedTitleTableColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabFeedTitleTableColumn.setEditable(false);
        jeproLabFeedTitleTableColumn.setResizable(false);
        jeproLabFeedTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("feedTitle"));
        tableCellAlign(jeproLabFeedTitleTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedDescriptionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        jeproLabFeedDescriptionTableColumn.setPrefWidth(0.6 * remainingWidth);
        jeproLabFeedDescriptionTableColumn.setEditable(false);
        jeproLabFeedDescriptionTableColumn.setResizable(false);
        jeproLabFeedDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("feedDescription"));
        tableCellAlign(jeproLabFeedDescriptionTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabFeedRecord, HBox> jeproLabFeedActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabFeedActionTableColumn.setPrefWidth(70);
        jeproLabFeedActionTableColumn.setEditable(false);
        jeproLabFeedActionTableColumn.setResizable(false);
        Callback<TableColumn<JeproLabFeedRecord, HBox>, TableCell<JeproLabFeedRecord, HBox>> actionCellFactory = params -> new JeproLabFeedActionCellFactory();
        jeproLabFeedActionTableColumn.setCellFactory(actionCellFactory);

        jeproLabFeedTableView.getColumns().addAll(
            jeproLabFeedIndexTableColumn, jeproLabCheckBoxTableColumn, jeproLabFeedAuthorTableColumn,
            jeproLabFeedTitleTableColumn, jeproLabFeedDescriptionTableColumn, jeproLabFeedActionTableColumn
        );
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabFeedModel> feeds;
            @Override
            protected Boolean call() throws Exception{
                if(isCancelled()){
                    return false;
                }

                feeds = JeproLabFeedModel.getFeeds();
                return true;
            }

            @Override
            public boolean isCancelled() {
                return super.isCancelled();
            }

            @Override
            public void failed() {
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            public void succeeded() {
                super.succeeded();
                updateFeedTableView(feeds);
            }

        };
        new Thread((Task)worker).start();
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addFeedBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        addFeedBtn.setOnAction(evt -> {
            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addFeedForm);
            JeproLab.getInstance().getApplicationForms().addFeedForm.controller.initializeContent();
        });
        commandWrapper.getChildren().addAll(addFeedBtn);
    }

    private void updateFeedTableView(List<JeproLabFeedModel> feeds){
        double padding = 0.01 * JeproLab.APP_WIDTH;
        feedList = FXCollections.observableArrayList();
        feedList.addAll(feeds.stream().map(JeproLabFeedRecord::new).collect(Collectors.toList()));

        if(feedList.isEmpty()){
            Platform.runLater(() -> {
                VBox.setMargin(jeproLabFeedSearchWrapper, new Insets(5, padding, 10, padding));
                VBox.setMargin(jeproLabFeedTableView, new Insets(5, padding, 10, padding));

                jeproLabFeedListWrapper.getChildren().clear();
                jeproLabFeedListWrapper.getChildren().addAll(jeproLabFeedSearchWrapper, jeproLabFeedTableView);
            });
        }else{
            Platform.runLater(() -> {
                Pagination jeproLabPagination = new Pagination((feedList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabPagination.setPageFactory(this::createFeedPage);

                VBox.setMargin(jeproLabFeedSearchWrapper, new Insets(5, padding, 10, padding));
                VBox.setMargin(jeproLabPagination, new Insets(5, padding, 10, padding));

                jeproLabFeedListWrapper.getChildren().clear();
                jeproLabFeedListWrapper.getChildren().addAll(jeproLabFeedSearchWrapper, jeproLabPagination);
            });
        }
    }

    private Node createFeedPage(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (feedList.size()));
        jeproLabFeedTableView.setItems(FXCollections.observableArrayList(feedList.subList(fromIndex, toIndex)));
        return new Pane(jeproLabFeedTableView);
    }

    public static class JeproLabFeedRecord{
        private SimpleIntegerProperty feedIndex;
        private SimpleStringProperty feedAuthor;
        private SimpleStringProperty feedTitle;
        private SimpleStringProperty feedDescription;
        private SimpleStringProperty feedLink;

        public JeproLabFeedRecord(JeproLabFeedModel feed){
            int langId = JeproLabContext.getContext().language.language_id;
            feedIndex = new SimpleIntegerProperty(feed.feed_id);
            feedAuthor = new SimpleStringProperty(feed.feed_author);
            feedTitle = new SimpleStringProperty((feed.feed_title.containsKey("lang_" + langId) ? feed.feed_title.get("lang_" + langId) : ""));
            feedDescription = new SimpleStringProperty((feed.feed_description.containsKey("lang_" + langId) ? feed.feed_description.get("lang_" + langId) : ""));
            feedLink = new SimpleStringProperty((feed.feed_link.containsKey("lang_" + langId) ? feed.feed_link.get("lang_" + langId) : "www.jeprodev.net"));
        }

        public int getFeedIndex(){ return feedIndex.get(); }
        public String getFeedAuthor(){ return feedAuthor.get(); }
        public String getFeedTitle(){ return feedTitle.get(); }
        public String getFeedDescription(){ return feedDescription.get(); }
        public String getFeedLink(){ return feedLink.get(); }
    }

    public static class JeproLabFeedCheckBoxCellFactory extends TableCell<JeproLabFeedRecord, Boolean>{
        private CheckBox checkFeed;

        public JeproLabFeedCheckBoxCellFactory(){
            checkFeed = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(Boolean item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabFeedRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                setGraphic(checkFeed);
                setAlignment(Pos.CENTER);
            }
        }
    }

    public static class JeproLabFeedActionCellFactory extends TableCell<JeproLabFeedRecord, HBox>{
        private HBox commandWrapper;
        private Button editBtn, hyperLinkBtn, deleteBtn;

        public JeproLabFeedActionCellFactory(){
            commandWrapper = new HBox(4);
            editBtn = new Button();
            editBtn.setPrefSize(btnSize, btnSize);
            editBtn.setMaxSize(btnSize, btnSize);
            editBtn.setMinSize(btnSize, btnSize);
            editBtn.getStyleClass().addAll("icon-btn", "edit-btn");

            hyperLinkBtn = new Button();
            hyperLinkBtn.setPrefSize(btnSize, btnSize);
            hyperLinkBtn.setMaxSize(btnSize, btnSize);
            hyperLinkBtn.setMinSize(btnSize, btnSize);
            hyperLinkBtn.getStyleClass().addAll("icon-btn", "hyper-link-btn");

            deleteBtn = new Button();
            deleteBtn.setPrefSize(btnSize, btnSize);
            deleteBtn.setMaxSize(btnSize, btnSize);
            deleteBtn.setMinSize(btnSize, btnSize);
            deleteBtn.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper.getChildren().addAll(editBtn, hyperLinkBtn, deleteBtn);
            commandWrapper.setAlignment(Pos.CENTER);
        }

        @Override
        public void commitEdit(HBox item){
            super.commitEdit(item);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);

            ObservableList<JeproLabFeedRecord> items = getTableView().getItems();

            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getFeedIndex();
                editBtn.setOnAction(evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addFeedForm);
                    JeproLab.getInstance().getApplicationForms().addFeedForm.controller.initializeContent(itemId);
                });

                hyperLinkBtn.setOnAction(evt -> {
                    try{
                        System.out.println(new URL(items.get(getIndex()).getFeedLink()).toURI());
                        Desktop.getDesktop().browse(new URL(items.get(getIndex()).getFeedLink()).toURI());
                    } catch(URISyntaxException | IOException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                });

                deleteBtn.setOnAction(evt -> {

                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }

}
