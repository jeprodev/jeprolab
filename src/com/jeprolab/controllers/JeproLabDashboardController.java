package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.views.modules.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabDashboardController extends JeproLabController {
    private static final int MODULE_ROWS = 5;
    @FXML
    public JeproFormPanel jeproLabDashboardPageWrapper;
    public GridPane jeproLabDashboardFormWrapperLayout;
    public HBox jeproLabDashboardDateWrapper, jeproLabDashboard, jeproLabDashboardPeriodSelectorWrapper;
    public Label jeproLabPeriodFromDateLabel, jeproLabPeriodDateToLabel;
    public DatePicker jeproLabPeriodFromDate, jeproLabPeriodToDate;
    public JeproLabDashboardAnalyzeModule jeproLabDashboardAnalyzeModule;
    public JeproLabDashboardProjectionModule jeproLabDashboardProjectionModule;
    public JeproLabDashboardRequestModule jeproLabDashboardRequestModule;
    public JeproLabDashboardBestSalesModule jeproLabDashboardBestSalesModuleModule;
    public JeproLabDashboardLastConnectionModule jeproLabDashboardLastConnectionModule;
    public JeproLabDashboardInformationBoxesModule jeproLabDashboardInformationBoxesModule;
    //public jeproLabDashboard;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double moduleHeight = (MODULE_ROWS * rowHeight);
        double moduleMinWidth = ((0.98 * JeproLab.APP_WIDTH)/3) - 5;
        double columnConstrains = (0.98 * JeproLab.APP_WIDTH)/9;

        jeproLabDashboardFormWrapperLayout.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabDashboardFormWrapperLayout.getColumnConstraints().addAll(
            new ColumnConstraints(columnConstrains), new ColumnConstraints(columnConstrains),
            new ColumnConstraints(columnConstrains), new ColumnConstraints(columnConstrains),
            new ColumnConstraints(columnConstrains), new ColumnConstraints(columnConstrains),
            new ColumnConstraints(columnConstrains), new ColumnConstraints(columnConstrains),
            new ColumnConstraints(columnConstrains)
        );

        jeproLabPeriodFromDateLabel.setText(bundle.getString("JEPROLAB_FROM_LABEL"));
        jeproLabPeriodDateToLabel.setText(bundle.getString("JEPROLAB_TO_LABEL"));

        jeproLabDashboardDateWrapper.setPrefWidth(JeproLab.APP_WIDTH);
        jeproLabDashboardDateWrapper.setAlignment(Pos.CENTER_RIGHT);

        jeproLabPeriodFromDate.setPrefWidth(120);
        jeproLabPeriodToDate.setPrefWidth(120);

        GridPane.setMargin(jeproLabDashboardPeriodSelectorWrapper, new Insets(10, 10, 5, 10));

        jeproLabDashboardAnalyzeModule.setModuleSize(moduleMinWidth, moduleHeight + 70);
        jeproLabDashboardProjectionModule.setModuleSize(moduleMinWidth, moduleHeight + 70);
        jeproLabDashboardRequestModule.setModuleSize(moduleMinWidth, moduleHeight  + 70);
        jeproLabDashboardBestSalesModuleModule.setModuleSize(7 * (moduleMinWidth/3), (2 * moduleHeight) + 70);
        jeproLabDashboardLastConnectionModule.setModuleSize(2 * (moduleMinWidth/3), (2 * moduleHeight) + 70);
        jeproLabDashboardInformationBoxesModule.setModuleSize((0.98 * JeproLab.APP_WIDTH), 130);

        jeproLabDashboardAnalyzeModule.initializeContent();
        jeproLabDashboardProjectionModule.initializeContent();
        jeproLabDashboardRequestModule.initializeContent();
        jeproLabDashboardBestSalesModuleModule.initializeContent();
        jeproLabDashboardLastConnectionModule.initializeContent();
        //jeproLabDashboardInformationBoxesModule.initializeContent();
    }

    private void createBottomGrid(){

    }
}
