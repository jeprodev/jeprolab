package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabController implements Initializable {
    protected ResourceBundle bundle;

    protected JeproLabContext context;

    protected Tooltip tooltip;

    public String task = "";

    public boolean has_errors = false;

    public boolean multi_lab_context;

    public String lab_link_type = "";

    protected static boolean isInitialized = false;

    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        context = JeproLabContext.getContext();
        context.controller = this;
        tooltip = new Tooltip();
        //this.updateToolBar();

        if(!isInitialized){

        }

    }

    public static boolean isStored(String cache_id){
        return false;
    }

    public void initializeContent() {
        if (!this.viewAccess()) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_YOU_DO_NOT_HAVE_PERMISSION_TO_VIEW_THIS_PAGE_MESSAGE"));
        }

        /*if (task.equals("edit") || task.equals("add")) {
            this.renderEditForm();
        } else if (task.equals("view")) {
            this.renderViewForm();
        } else if (task.equals("display") || task.equals("")) {
            this.renderDetails();
        }//if(task.equals()) */
    }

    protected void renderDetails(){}

    protected void renderViewForm(){}

    protected void renderEditForm(){}

    public void updateToolBar(){}

    private boolean viewAccess(){
        return true; //to do to be improved
    }

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
}