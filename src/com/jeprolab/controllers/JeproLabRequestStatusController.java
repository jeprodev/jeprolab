package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabRequestModel;
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
import javafx.scene.layout.*;
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
public class JeproLabRequestStatusController extends JeproLabController{
    private TableView<JeproLabRequestStatusRecord> jeproLabRequestStatuesTableView;
    private ObservableList<JeproLabRequestStatusRecord> statuesList;
    private HBox jeproLabRequestStatusSearchWrapper;
    private TextField jeproLabRequestStatusSearchField;
    private ComboBox<String> jeproLabRequestStatusSearchFilter;
    private Button jeproLabRequestStatusSearchBtn;
    @FXML
    //public HBox jeproLabRequestStatuesWrapper;
    public VBox jeproLabRequestStatuesListWrapper;


    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 328;
        /*jeproLabRequestStatuesWrapper.setPrefWidth(formWidth);
        jeproLabRequestStatuesWrapper.setSpacing(50);
        jeproLabRequestStatuesWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabRequestStatuesWrapper.setLayoutY(20);*/

        jeproLabRequestStatuesTableView = new TableView<>();
        jeproLabRequestStatuesTableView.setPrefSize(formWidth, rowHeight * JeproLabConfigurationSettings.LIST_LIMIT);
        //jeproLabRequestStatuesTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);

        TableColumn<JeproLabRequestStatusRecord, String> jeproLabRequestStatuesIndexTableColumn = new TableColumn<>("#");
        jeproLabRequestStatuesIndexTableColumn.setPrefWidth(30);
        tableCellAlign(jeproLabRequestStatuesIndexTableColumn, Pos.CENTER_RIGHT);
        jeproLabRequestStatuesIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatusIndex"));

        TableColumn<JeproLabRequestStatusRecord, Boolean> jeproLabRequestStatuesCheckBoxTableColumn = new TableColumn<>();
        jeproLabRequestStatuesCheckBoxTableColumn.setPrefWidth(25);
        Callback<TableColumn<JeproLabRequestStatusRecord, Boolean>, TableCell<JeproLabRequestStatusRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabRequestStatusCheckBoxCellFactory();
        jeproLabRequestStatuesCheckBoxTableColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabRequestStatusRecord, String> jeproLabRequestStatuesNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabRequestStatuesNameTableColumn.setPrefWidth(remainingWidth);
        jeproLabRequestStatuesNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatusName"));

        TableColumn<JeproLabRequestStatusRecord,Boolean> jeproLabRequestStatuesInvoiceTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_INVOICE_LABEL"));
        jeproLabRequestStatuesInvoiceTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabRequestStatusRecord, Boolean>, TableCell<JeproLabRequestStatusRecord, Boolean>> invoiceCellFactory = params -> new JeproLabRequestStatusInvoiceCellFactory();
        jeproLabRequestStatuesInvoiceTableColumn.setCellFactory(invoiceCellFactory);

        TableColumn<JeproLabRequestStatusRecord, Boolean> jeproLabRequestStatuesEmailTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        jeproLabRequestStatuesEmailTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabRequestStatusRecord, Boolean>, TableCell<JeproLabRequestStatusRecord, Boolean>> emailCellFactory = params -> new JeproLabRequestStatusEmailCellFactory();
        jeproLabRequestStatuesEmailTableColumn.setCellFactory(emailCellFactory);

        TableColumn<JeproLabRequestStatusRecord, Boolean> jeproLabRequestStatuesDeletedTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_DELETED_LABEL"));
        jeproLabRequestStatuesDeletedTableColumn.setPrefWidth(70);
        Callback<TableColumn<JeproLabRequestStatusRecord, Boolean>, TableCell<JeproLabRequestStatusRecord, Boolean>> deletedCellFactory = params -> new JeproLabRequestStatusDeletedCellFactory();
        jeproLabRequestStatuesEmailTableColumn.setCellFactory(deletedCellFactory);

        TableColumn<JeproLabRequestStatusRecord, HBox> jeproLabRequestStatuesActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabRequestStatuesActionTableColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabRequestStatusRecord, HBox>, TableCell<JeproLabRequestStatusRecord, HBox>> actionsCellFactory = params -> new JeproLabRequestStatusActionCellFactory();
        jeproLabRequestStatuesActionTableColumn.setCellFactory(actionsCellFactory);

        jeproLabRequestStatuesTableView.getColumns().addAll(
            jeproLabRequestStatuesIndexTableColumn, jeproLabRequestStatuesCheckBoxTableColumn, jeproLabRequestStatuesNameTableColumn,
            jeproLabRequestStatuesInvoiceTableColumn, jeproLabRequestStatuesEmailTableColumn, jeproLabRequestStatuesDeletedTableColumn,
            jeproLabRequestStatuesActionTableColumn
        );


        /*jeproLabRequestStatusCommandWrapper.setPrefWidth(formContainerWidth);
        jeproLabRequestStatusCommandWrapper.setSpacing(40);
        jeproLabRequestStatusCommandWrapper.setAlignment(Pos.CENTER);

        jeproLabRequestStatusSaveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        jeproLabRequestStatusSaveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        jeproLabRequestStatusSaveButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        jeproLabRequestStatusCancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
        jeproLabRequestStatusCancelButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));


        GridPane.setMargin(jeproLabRequestStatusCommandWrapper, new Insets(20, 0, 15, 0));
*/
        jeproLabRequestStatusSearchWrapper = new HBox(10);
        jeproLabRequestStatusSearchField = new TextField();
        jeproLabRequestStatusSearchField.setPromptText(bundle.getString("JEPROLAB_SEARCH_LABEL"));
        jeproLabRequestStatusSearchFilter  = new ComboBox<>();
        jeproLabRequestStatusSearchFilter.setPromptText(bundle.getString("JEPROLAB_SEARCH_BY_LABEL"));
        jeproLabRequestStatusSearchBtn = new Button();
        jeproLabRequestStatusSearchBtn.getStyleClass().addAll("icon-btn", "search-btn");

        jeproLabRequestStatusSearchWrapper.getChildren().addAll(
            jeproLabRequestStatusSearchField, jeproLabRequestStatusSearchFilter, jeproLabRequestStatusSearchBtn
        );
    }


    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>() {
            List<JeproLabRequestModel.JeproLabRequestStatusModel> statues;

            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                statues = JeproLabRequestModel.JeproLabRequestStatusModel.getStatues();
                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.failed();
                updateStatuesTableView(statues);
            }
        };
        JeproLab.getInstance().executor.submit((Task)worker);
        updateToolBar();
    }


    private void updateStatuesTableView(List<JeproLabRequestModel.JeproLabRequestStatusModel> statues){
        statuesList = FXCollections.observableArrayList();
        if(statues!= null && !statues.isEmpty()){
            statuesList.addAll(statues.stream().map(JeproLabRequestStatusRecord::new).collect(Collectors.toList()));
            Platform.runLater(() -> {
                Pagination jeproLabPagination = new Pagination((statuesList.size()/JeproLabConfigurationSettings.LIST_LIMIT) + 1, 0);
                jeproLabPagination.setPageFactory(this::createPageFactory);
                setTableViewContent(jeproLabRequestStatuesListWrapper, jeproLabRequestStatusSearchWrapper, jeproLabPagination);
            });
        }
    }

    private Node createPageFactory(int pageIndex){
        int fromIndex = pageIndex * JeproLabConfigurationSettings.LIST_LIMIT;
        int toIndex = Math.min(fromIndex + JeproLabConfigurationSettings.LIST_LIMIT, (statuesList.size()));
        jeproLabRequestStatuesTableView.setItems(FXCollections.observableArrayList(statuesList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabRequestStatuesTableView);
    }


    public static class JeproLabRequestStatusRecord {
        private SimpleIntegerProperty requestStatusIndex;
        private SimpleStringProperty requestStatusName;
        private SimpleBooleanProperty requestStatusSendEmail;
        private SimpleBooleanProperty requestStatusSendInvoice;
        private SimpleBooleanProperty requestStatusDeleted;
        private SimpleBooleanProperty requestStatusUnRemovable;

        public JeproLabRequestStatusRecord(JeproLabRequestModel.JeproLabRequestStatusModel status){
            requestStatusIndex = new SimpleIntegerProperty(status.request_status_id);
            requestStatusName = new SimpleStringProperty(status.name.get("lang_" + JeproLabContext.getContext().language.language_id));
            requestStatusSendEmail = new SimpleBooleanProperty(status.send_email);
            requestStatusSendInvoice = new SimpleBooleanProperty(status.send_invoice);
            requestStatusDeleted = new SimpleBooleanProperty(status.deleted);
            requestStatusUnRemovable = new SimpleBooleanProperty(status.un_removable);
        }

        public int getRequestStatusIndex(){ return requestStatusIndex.get(); }

        public String getRequestStatusName(){ return requestStatusName.get(); }

        public boolean getRequestStatusSendEmail(){ return requestStatusSendEmail.get(); }

        public boolean getRequestStatusSendInvoice(){ return requestStatusSendInvoice.get(); }

        public boolean getRequestStatusDeleted(){ return requestStatusDeleted.get(); }

        public boolean getRequestStatusUnRemovable(){ return requestStatusUnRemovable.get(); }
    }

    private static class JeproLabRequestStatusCheckBoxCellFactory extends TableCell<JeproLabRequestStatusRecord, Boolean>{
        private CheckBox requestCheckBox ;
        public JeproLabRequestStatusCheckBoxCellFactory(){
            requestCheckBox = new CheckBox();
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item); }

        @Override
        public void updateItem(Boolean item, boolean empty){
            final ObservableList<JeproLabRequestStatusRecord> items = getTableView().getItems();
            if((items != null) && getIndex() >= 0 && getIndex() < items.size()){
                setGraphic(requestCheckBox);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabRequestStatusEmailCellFactory extends TableCell<JeproLabRequestStatusRecord, Boolean>{
        private Button emailButton;

        public JeproLabRequestStatusEmailCellFactory(){
            emailButton = new Button();
            emailButton.setPrefSize(btnSize, btnSize);
            emailButton.setMaxSize(btnSize, btnSize);
            emailButton.setMinSize(btnSize, btnSize);

            emailButton.getStyleClass().addAll("icon-btn");
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item); }

        @Override
        public void updateItem(Boolean item, boolean empty){
            final ObservableList<JeproLabRequestStatusRecord> items = getTableView().getItems();
            if((items != null) && getIndex() >= 0 && getIndex() < items.size()){
                if(items.get(getIndex()).getRequestStatusSendInvoice()) {
                    emailButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    emailButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(emailButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabRequestStatusInvoiceCellFactory extends TableCell<JeproLabRequestStatusRecord, Boolean>{
        private Button invoiceButton;

        public JeproLabRequestStatusInvoiceCellFactory(){
            invoiceButton = new Button();
            invoiceButton.setPrefSize(btnSize, btnSize);
            invoiceButton.setMaxSize(btnSize, btnSize);
            invoiceButton.setMinSize(btnSize, btnSize);

            invoiceButton.getStyleClass().addAll("icon-btn");
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item); }

        @Override
        public void updateItem(Boolean item, boolean empty){
            final ObservableList<JeproLabRequestStatusRecord> items = getTableView().getItems();
            if((items != null) && getIndex() >= 0 && getIndex() < items.size()){
                int itemId = items.get(getIndex()).getRequestStatusIndex();
                if(items.get(getIndex()).getRequestStatusSendInvoice()) {
                    invoiceButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    invoiceButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                setGraphic(invoiceButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabRequestStatusDeletedCellFactory extends TableCell<JeproLabRequestStatusRecord, Boolean>{
        private Button deletedButton;

        public JeproLabRequestStatusDeletedCellFactory(){
            deletedButton = new Button();
            deletedButton.setPrefSize(btnSize, btnSize);
            deletedButton.setMaxSize(btnSize, btnSize);
            deletedButton.setMinSize(btnSize, btnSize);

            deletedButton.getStyleClass().addAll("icon-btn");
        }

        @Override
        public void commitEdit(Boolean item){ super.commitEdit(item); }

        @Override
        public void updateItem(Boolean item, boolean empty){
            final ObservableList<JeproLabRequestStatusRecord> items = getTableView().getItems();
            if((items != null) && getIndex() >= 0 && getIndex() < items.size()){
                if(items.get(getIndex()).getRequestStatusDeleted()){
                    deletedButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                }else{
                    deletedButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                }
                int itemId = items.get(getIndex()).getRequestStatusIndex();

                setGraphic(deletedButton);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private static class JeproLabRequestStatusActionCellFactory extends TableCell<JeproLabRequestStatusRecord, HBox>{
        private HBox commandWrapper;
        private Button editStatusBtn, deleteStatusBtn;

        public JeproLabRequestStatusActionCellFactory(){
            commandWrapper = new HBox(8);

            editStatusBtn = new Button();
            editStatusBtn.setPrefSize(btnSize, btnSize);
            editStatusBtn.setMaxSize(btnSize, btnSize);
            editStatusBtn.setMinSize(btnSize, btnSize);
            editStatusBtn.getStyleClass().addAll("icon-btn", "edit-btn");

            deleteStatusBtn = new Button();
            deleteStatusBtn.setPrefSize(btnSize, btnSize);
            deleteStatusBtn.setMaxSize(btnSize, btnSize);
            deleteStatusBtn.setMinSize(btnSize, btnSize);
            deleteStatusBtn.getStyleClass().addAll("icon-btn", "delete-btn");

            commandWrapper.getChildren().addAll(editStatusBtn, deleteStatusBtn);
        }

        @Override
        public void commitEdit(HBox item){ super.commitEdit(item); }

        @Override
        public void updateItem(HBox item, boolean empty){
            final ObservableList<JeproLabRequestStatusRecord> items = getTableView().getItems();
            if((items != null) && getIndex() >= 0 && getIndex() < items.size()){
                int itemId = items.get(getIndex()).getRequestStatusIndex();
                editStatusBtn.setOnAction(evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().requestAddStatusForm);
                    JeproLab.getInstance().getApplicationForms().requestAddStatusForm.controller.initializeContent(itemId);
                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
