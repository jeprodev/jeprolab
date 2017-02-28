package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabFeedModel;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeedController extends JeproLabController {
    private TableView<JeproLabFeedRecord> jeproLabFeedTableView;
    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = 0.98 * JeproLab.APP_WIDTH;
        double remainingWidth = formWidth - 120;
        jeproLabFeedTableView = new TableView<>();

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedIndexTableColumn = new TableColumn<>("#");
        jeproLabFeedIndexTableColumn.setPrefWidth(30);
        jeproLabFeedIndexTableColumn.getStyleClass().addAll("editable", "resizable");
        tableCellAlign(jeproLabFeedIndexTableColumn, Pos.CENTER_RIGHT);

        TableColumn<JeproLabFeedRecord, Boolean> jeproLabCheckBoxTableColumn = new TableColumn<>();
        jeproLabCheckBoxTableColumn.setPrefWidth(20);
        jeproLabCheckBoxTableColumn.getStyleClass().addAll("editable", "resizable");
        Callback<TableColumn<JeproLabFeedRecord, Boolean>, TableCell<JeproLabFeedRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabFeedCheckBoxCellFactory();
        jeproLabCheckBoxTableColumn.setCellFactory(checkBoxCellFactory);
        //jeproLabFeedCheckBoxTableColumn" / >
        TableColumn<JeproLabFeedRecord, String> jeproLabFeedAuthorTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedAuthorTableColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabFeedAuthorTableColumn.getStyleClass().addAll("editable", "resizable");
        tableCellAlign(jeproLabFeedAuthorTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedTitleTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedTitleTableColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabFeedTitleTableColumn.getStyleClass().addAll("editable", "resizable");
        tableCellAlign(jeproLabFeedTitleTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabFeedRecord, String> jeproLabFeedDescriptionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_LABEL"));
        jeproLabFeedDescriptionTableColumn.setPrefWidth(0.6 * remainingWidth);
        jeproLabFeedDescriptionTableColumn.getStyleClass().addAll("editable", "resizable");
        tableCellAlign(jeproLabFeedDescriptionTableColumn, Pos.CENTER_LEFT);

        TableColumn<JeproLabFeedRecord, HBox> jeproLabFeedActionTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabFeedActionTableColumn.setPrefWidth(70);
        jeproLabFeedActionTableColumn.getStyleClass().addAll("editable", "resizable");
        Callback<TableColumn<JeproLabFeedRecord, HBox>, TableCell<JeproLabFeedRecord, HBox>> actionCellFactory = params -> new JeproLabFeedActionCellFactory();
        jeproLabFeedActionTableColumn.setCellFactory(actionCellFactory);


    }

    public static class JeproLabFeedRecord{
        public JeproLabFeedRecord(JeproLabFeedModel feed){

        }
    }

    public static class JeproLabFeedCheckBoxCellFactory extends TableCell<JeproLabFeedRecord, Boolean>{}
    public static class JeproLabFeedActionCellFactory extends TableCell<JeproLabFeedRecord, HBox>{}

}
