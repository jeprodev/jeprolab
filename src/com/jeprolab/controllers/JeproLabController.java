package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabController implements Initializable {
    protected ResourceBundle bundle;

    protected JeproLabContext context;

    public String task = "";

    protected static boolean isInitialized = false;

    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        context = JeproLabContext.getContext();

        if(!isInitialized){

        }

    }

    public static boolean isStored(String cache_id){
        return false;
    }

    protected void initializeContent() {
        if (!this.viewAccess()) {
            JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_YOU_DO_NOT_HAVE_PERMISSION_TO_VIEW_THIS_PAGE_MESSAGE"));
        }

        if (task.equals("edit") || task.equals("add")) {
            this.renderEditForm();
        } else if (task.equals("view")) {
            this.renderViewForm();
        } else if (task.equals("display") || task.equals("")) {
            this.renderDetails();
        }//if(task.equals())
    }

    protected void renderDetails(){}

    protected void renderViewForm(){}

    protected void renderEditForm(){}

    public void updateToolBar(){}

    private boolean viewAccess(){
        return true; //to do to be improved
    }
}