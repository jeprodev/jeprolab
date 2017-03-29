package com.jeprolab.views.modules;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.controllers.JeproLabController;
import com.jeprolab.models.JeproLabRequestModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 25/03/2016.
 */
public class JeproLabDashboardRequestModule extends JeproLabDashboardModule {
    private TableView requestTableView;

    public JeproLabDashboardRequestModule() {
        super();

        requestTableView = new TableView<>();
        this.formContentWrapper.getChildren().setAll(requestTableView);
    }

    @Override
    public void setModuleSize(double width, double height){
        super.setModuleSize(width, height);
        requestTableView.setPrefSize(this.formContentWrapper.getPrefWidth() - 10, (JeproLabController.rowHeight * 5) + 42);
        requestTableView.setLayoutX(5);
        TableColumn<DashBoardRequestRecord, String> requestReferenceTableColumn = new TableColumn<>(JeproLab.getBundle().getString("JEPROLAB_REFERENCE_LABEL"));
        requestReferenceTableColumn.setPrefWidth(this.formContentWrapper.getPrefWidth() - 42);
        requestReferenceTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestReference"));

        TableColumn<DashBoardRequestRecord, Button> requestViewButton = new TableColumn<>();
        Callback<TableColumn<DashBoardRequestRecord, Button>, TableCell<DashBoardRequestRecord, Button>> viewCellFactory = params -> new DashBoardRequestViewCellFactory();
        requestViewButton.setCellFactory(viewCellFactory);
        requestViewButton.setPrefWidth(30);

        requestTableView.getColumns().addAll(requestReferenceTableColumn, requestViewButton);
    }

    @Override
    public void initializeContent(){
        Worker<Boolean>  worker = new Task<Boolean>() {
            List<JeproLabRequestModel> requests;
            @Override
            protected Boolean call() throws Exception {
                if(isCancelled()){
                    return false;
                }
                requests = JeproLabRequestModel.getLastRequests();
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
                updateTableView(requests);
            }
        };

        JeproLab.getInstance().executor.submit((Task)worker);

    }

    private void updateTableView(List<JeproLabRequestModel> requests){
        if(requests != null){
            ObservableList<DashBoardRequestRecord> requestRecords = FXCollections.observableArrayList();
            requestRecords.addAll(requests.stream().map(DashBoardRequestRecord::new).collect(Collectors.toList()));
            Platform.runLater(() -> requestTableView.setItems(requestRecords));
        }
    }

    public static class DashBoardRequestRecord {
        private SimpleIntegerProperty requestIndex;
        private SimpleStringProperty requestReference;

        public DashBoardRequestRecord(JeproLabRequestModel request){
            requestIndex = new SimpleIntegerProperty(request.request_id);
            requestReference = new SimpleStringProperty(request.reference);
        }

        public int getRequestIndex(){ return requestIndex.get(); }

        public String getRequestReference(){ return requestReference.get(); }
    }

    private static class DashBoardRequestViewCellFactory extends TableCell<DashBoardRequestRecord, Button>{
        private Button viewButton;

        public DashBoardRequestViewCellFactory(){
            viewButton = new Button();
            viewButton.setPrefSize(JeproLabController.btnSize, JeproLabController.btnSize);
            viewButton.setMinSize(JeproLabController.btnSize, JeproLabController.btnSize);
            viewButton.setMaxSize(JeproLabController.btnSize, JeproLabController.btnSize);
            viewButton.getStyleClass().addAll("icon-btn", "search-btn") ;
        }

        @Override
        public void commitEdit(Button item){ super.commitEdit(item); }

        @Override
        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<DashBoardRequestRecord> items = getTableView().getItems();
            if(items != null && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getRequestIndex();
                viewButton.setOnAction(evt -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addRequestForm);
                    JeproLab.getInstance().getApplicationForms().addRequestForm.controller.initializeContent(itemId);
                });
                setGraphic(viewButton);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
