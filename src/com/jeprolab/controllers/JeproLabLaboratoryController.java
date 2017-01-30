package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabLaboratoryModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabLaboratoryController extends JeproLabController {
    private CheckBox checkAll;
    @FXML
    public TableView<JeproLabLaboratoryRecord> jeproLabLaboratoryTableView;
    public TableColumn jeproLabLaboratoryIndexColumn, jeproLabLaboratoryCheckBoxColumn, jeproLabLaboratoryNameColumn, jeproLabLaboratoryGroupColumn;
    public TableColumn jeproLabLaboratoryCategoryColumn, jeproLabLaboratoryThemeColumn, jeproLabLaboratoryPublishedColumn, jeproLabLaboratoryActionsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = (0.98 * JeproLab.APP_WIDTH);
        double remainingWidth = formWidth - 52;

        jeproLabLaboratoryTableView.setPrefWidth(formWidth);
        VBox.setMargin(jeproLabLaboratoryTableView, new Insets(5, 0, 0, (0.01 * JeproLab.APP_WIDTH)));
        checkAll = new CheckBox();
        jeproLabLaboratoryIndexColumn.setText("#");
        jeproLabLaboratoryIndexColumn.setPrefWidth(30);
        jeproLabLaboratoryCheckBoxColumn.setGraphic(checkAll);
        jeproLabLaboratoryCheckBoxColumn.setPrefWidth(22);
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

    public static class JeproLabLaboratoryRecord{
        public JeproLabLaboratoryRecord(JeproLabLaboratoryModel laboratory){

        }
    }
}
