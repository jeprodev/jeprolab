package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabLaboratoryController extends JeproLabController{
    @FXML
    public TableView jeproLabLaboratoryTableView;
    public TableColumn jeproLabLaboratoryIndexColumn, jeproLabLaboratoryCheckBoxColumn, jeproLabLaboratoryNameColumn, jeproLabLaboratoryGroupColumn;
    public TableColumn jeproLabLaboratoryCategoryColumn, jeproLabLaboratoryThemeColumn, jeproLabLaboratoryPublishedColumn, jeproLabLaboratoryActionsColumn;

    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 55;

        jeproLabLaboratoryTableView.setPrefWidth(.98 * JeproLab.APP_WIDTH);
        jeproLabLaboratoryTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabLaboratoryTableView.setLayoutY(10);
        jeproLabLaboratoryIndexColumn.setText("#");
        jeproLabLaboratoryIndexColumn.setPrefWidth(30);
        jeproLabLaboratoryCheckBoxColumn.setText(bundle.getString("JEPROLAB_LABEL"));
        jeproLabLaboratoryCheckBoxColumn.setPrefWidth(25);
        jeproLabLaboratoryNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabLaboratoryNameColumn.setPrefWidth(0.25 * remainingWidth);
        jeproLabLaboratoryGroupColumn.setText(bundle.getString("JEPROLAB_LABORATORY_GROUP_LABEL"));
        jeproLabLaboratoryGroupColumn.setPrefWidth(0.25 * remainingWidth);
        jeproLabLaboratoryCategoryColumn.setText(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabLaboratoryCategoryColumn.setPrefWidth(0.2 * remainingWidth);
        jeproLabLaboratoryThemeColumn.setText(bundle.getString("JEPROLAB_THEME_LABEL"));
        jeproLabLaboratoryThemeColumn.setPrefWidth(0.1 * remainingWidth);
        jeproLabLaboratoryPublishedColumn.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabLaboratoryPublishedColumn.setPrefWidth(0.1 * remainingWidth);
        jeproLabLaboratoryActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabLaboratoryActionsColumn.setPrefWidth(0.1 * remainingWidth);
    }
}