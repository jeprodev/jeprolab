package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabAnalyzeController extends JeproLabController {
    private CheckBox checkAll;
    private Button addAnalyzeBtn;
    @FXML
    public TableView jeproLabAnalyzeTableView;
    public TableColumn jeproLabAnalyzeIndexColumn, jeproLabAnalyzeCheckBoxColumn, jeproLabAnalyzeNameColumn, jeproLabAnalyzeStatusColumn, jeproLabAnalyzeReferenceColumn;
    //public TableColumn  jeproLabAnalyzeColumn, jeproLabAnalyzeColumn, jeproLabAnalyzeColumn;
    public TableColumn jeproLabAnalyzeBasePriceColumn, jeproLabAnalyzeFinalPriceColumn, jeproLabAnalyzeCategoryColumn, jeproLabAnalyzeActionsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double remainingWidth = 0.98 * JeproLab.APP_WIDTH - 105;
        checkAll = new CheckBox();

        jeproLabAnalyzeTableView.setPrefSize(0.98 * JeproLab.APP_WIDTH, 600);
        jeproLabAnalyzeTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabAnalyzeTableView.setLayoutY(10);

        jeproLabAnalyzeIndexColumn.setText("#");
        jeproLabAnalyzeIndexColumn.setPrefWidth(30);

        jeproLabAnalyzeCheckBoxColumn.setGraphic(checkAll);
        jeproLabAnalyzeCheckBoxColumn.setPrefWidth(25);

        jeproLabAnalyzeStatusColumn.setText(bundle.getString("JEPROLAB_STATUS_LABEL"));
        jeproLabAnalyzeStatusColumn.setPrefWidth(60);

        jeproLabAnalyzeNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabAnalyzeNameColumn.setPrefWidth(0.3 * remainingWidth);

        jeproLabAnalyzeReferenceColumn.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabAnalyzeReferenceColumn.setPrefWidth(0.2 * remainingWidth);

        jeproLabAnalyzeBasePriceColumn.setText(bundle.getString("JEPROLAB_BASE_PRICE_LABEL"));
        jeproLabAnalyzeBasePriceColumn.setPrefWidth(0.1 * remainingWidth);

        jeproLabAnalyzeFinalPriceColumn.setText(bundle.getString("JEPROLAB_FINAL_PRICE_LABEL"));
        jeproLabAnalyzeFinalPriceColumn.setPrefWidth(0.1 * remainingWidth);

        jeproLabAnalyzeCategoryColumn.setText(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabAnalyzeCategoryColumn.setPrefWidth(0.2 * remainingWidth);

        jeproLabAnalyzeActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        jeproLabAnalyzeActionsColumn.setPrefWidth(0.1 * remainingWidth);
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addAnalyzeBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addAnalyzeBtn);
    }
}