package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabController implements Initializable {
    public boolean has_errors = false;
    protected ResourceBundle bundle;
    protected JeproLabContext context;
    protected double formWidth;
    protected double labelColumnWidth = 150;
    protected double inputColumnWidth = 200;
    protected Label formTitleLabel;
    public static final double rowHeight = 25;

    public boolean multi_laboratory_context;

    public String laboratory_link_type = "";

    protected static boolean isInitialized = false;

    public static final double btnSize = 22;

    public static final int NUMBER_OF_LINES = 20;

    private Logger logging = Logger.getLogger(JeproLab.class);

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        context.controller = this;

        formTitleLabel = new Label();
        formTitleLabel.getStyleClass().add("form-title");
        formTitleLabel.setAlignment(Pos.CENTER);
    }

    public void initializeContent(){ }

    public void initializeContent(int itemId){ }

    public void updateToolBar(){}

    public void clearForm(){}

    /**
     * align text of the cell to right in the specified column
     *
     * @param col table column to be aligned to right
     */
    protected void tableCellAlign(TableColumn col, Pos alignment) {
        col.setCellFactory(param -> {
            TableCell cell = new TableCell() {
                @Override
                public void updateItem(Object item, boolean empty) {
                    if (item != null) {
                        setText(item.toString());
                    }
                }
            };
            cell.setAlignment(alignment);

            return cell;
        });
    }

    protected void setEmptyTableView(VBox wrapper, HBox searchWrapper, TableView tableView){
        double padding = 0.01 * JeproLab.APP_WIDTH;
        wrapper.getChildren().clear();
        VBox.setMargin(searchWrapper, new Insets(5, padding, 5, padding));
        VBox.setMargin(tableView, new Insets(5, padding, 5, padding));
        wrapper.getChildren().addAll(searchWrapper, tableView);
    }

    protected void setTableViewContent(VBox wrapper, HBox searchWrapper, Pagination pagination){
        double padding = 0.01 * JeproLab.APP_WIDTH;
        VBox.setMargin(searchWrapper, new Insets(5, padding, 5, padding));
        VBox.setMargin(pagination, new Insets(5, padding, 5, padding));
        wrapper.getChildren().clear();
        wrapper.getChildren().addAll(searchWrapper, pagination);
    }

    protected void addInformation(String message){
        logging.error(message);
    }
}
