package com.jeprolab.assets.extend.controls.tree;

import com.jeprolab.assets.tools.JeproLabContext;
import javafx.scene.layout.Pane;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproTree extends Pane {
    protected  String DEFAULT_TEMPLATE = "tree";

    protected String DEFAULT_HEADER_TEMPLATE = "tree_header";

    protected String DEFAULT_NODE_FOLDER_TEMPLATE = "tree_node_folder";

    protected  String DEFAULT_NODE_ITEM_TEMPLATE = "tree_node_item";

    protected JeproLabContext context;

    protected int language_id;

    protected String tree_template;

    protected String tree_title = "";

    protected String node_folder_template, node_item_template;

    protected boolean use_check_box;

    protected boolean use_search_box;

    protected boolean use_tool_bar = true, use_laboratory_restriction = false;

    protected Pane toolBarPane, contentPane;

    protected double tree_width;

    public JeproTree(){
        setPrefHeight(230);
        contentPane = new Pane();
        //contentPane.getStyleClass().add("form-panel-container");
    }

    public JeproTree setTreeLanguage(int langId){
        this.language_id = langId;
        return this;
    }

    public JeproTree setTreeTitle(String value){
        this.tree_title = value;
        return this;
    }

    public JeproTree setTreeTemplate(String template){
        this.tree_template = template;
        return this;
    }

    public JeproTree setUseCheckBox(boolean useCheckBox){
        this.use_check_box = useCheckBox;
        return this;
    }

    public JeproTree setUseSearchBox(boolean useSearchBox){
        this.use_search_box = useSearchBox;
        return this;
    }

    public JeproTree setTreeWidth(double treeWidth) {
        this.tree_width = treeWidth;
        return this;
    }

    public boolean useLaboratoryRestriction(){
        return this.use_laboratory_restriction;
    }

    /*** GETTERS **/
    public JeproLabContext getContext(){
        if(this.context == null){
            this.context = JeproLabContext.getContext();
        }
        return this.context;
    }

    public String getTreeTitle(){
        return this.tree_title;
    }

    public double getTreeWidth(){
        return this.tree_width;
    }

    public String getTreeTemplate(){
        return tree_template;
    }

    public int getTreeLanguage(){
        if(this.language_id <= 0){
            this.setTreeLanguage(this.getContext().employee.language_id);
        }
        return this.language_id;
    }

    public boolean useSearchBox(){
        return this.use_search_box;
    }

    public boolean useCheckBox(){
        return this.use_check_box;
    }

    public boolean useToolBar(){
        return this.use_tool_bar;
    }
}
