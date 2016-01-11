package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCategoryController extends JeproLabController {
    @FXML
    public TableView categoryTableView;
    public TableColumn categoryIndexColumn, categoryCheckBoxColumn, categoryStatusColumn, categoryNameColumn;
    public TableColumn categoryDescriptionColumn, categoryPositionColumn, categoryActionColumn;
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        double remainingWidth = JeproLab.APP_WIDTH - 75;
        categoryTableView.setPrefWidth(0.98 * JeproLab.APP_WIDTH);
        categoryTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        categoryTableView.setLayoutY(20);
        categoryIndexColumn.setText("#");
        categoryIndexColumn.setPrefWidth(20);
        categoryCheckBoxColumn.setText("");
        categoryCheckBoxColumn.setPrefWidth(25);
        categoryStatusColumn.setText("");
        categoryStatusColumn.setPrefWidth(30);
        categoryNameColumn.setText(bundle.getString("JEPROLAB_CATEGORY_NAME_LABEL"));
        categoryNameColumn.setPrefWidth(0.15 * remainingWidth);
        categoryDescriptionColumn.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        categoryDescriptionColumn.setPrefWidth(0.6 * remainingWidth);
        categoryPositionColumn.setText(bundle.getString("JEPROLAB_POSITION_LABEL"));
        categoryPositionColumn.setPrefWidth(0.06 * remainingWidth);
        categoryActionColumn.setPrefWidth(0.08 * remainingWidth);
    }

    @Override
    public void updateToolBar(){}
}