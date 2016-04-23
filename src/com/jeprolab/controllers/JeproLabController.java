package com.jeprolab.controllers;

import com.jeprolab.assets.tools.JeproLabContext;
import com.sun.javafx.scene.control.skin.TableCellSkin;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabController implements Initializable {
    protected boolean has_errors = false;
    protected ResourceBundle bundle;
    protected JeproLabContext context;
    protected double formWidth;
    protected Label formTitleLabel;
    public static final double rowHeight = 25;

    public final double btnSize = 22;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        bundle = resource;
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        context.controller = this;

        formTitleLabel = new Label();
    }

    public void initializeContent(){ }

    public void updateToolBar(){}

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


    protected abstract static class JeproLabRecord {
        protected abstract ObservableList getActionContent();

    }

    protected static class JeproLabActionCell<T> extends TableCell<T, HBox>{
        protected HBox commandContainer;


        public JeproLabActionCell(){
            commandContainer = new HBox(3);
        }

        public void setCommandContainer(List<Button> content){
            commandContainer.getChildren().addAll(content);
        }

        @Override
        public void commitEdit(HBox t){
            super.commitEdit(t);
        }

        @Override
        public void updateItem(HBox item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<T> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())) {

                setGraphic(commandContainer);
            }
        }
    }
}