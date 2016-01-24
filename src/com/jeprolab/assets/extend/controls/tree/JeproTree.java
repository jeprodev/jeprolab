package com.jeprolab.assets.extend.controls.tree;

import com.jeprolab.assets.tools.JeproLabContext;
import javafx.scene.layout.Pane;

/**
 *
 * Created by jeprodev on 24/01/2015.
 */
public class JeproTree extends Pane {
    protected  String DEFAULT_TEMPLATE = "tree";

    protected String DEFAULT_HEADER_TEMPLATE = "tree_header";

    protected String DEFAULT_NODE_FOLDER_TEMPLATE = "tree_node_folder";

    protected  String DEFAULT_NODE_ITEM_TEMPLATE = "tree_node_ template";

    protected JeproLabContext context;

    protected int language_id;

    protected String treeTemplate;

    protected String treeTitle;

    protected boolean useCheckBox;

    protected boolean useSearchBox;

    public JeproTree(){

    }

    public JeproTree setTreeLang(int langId){
        this.language_id = langId;
        return this;
    }

    public JeproTree setTreeTitle(String value){
        this.treeTitle = value;
        return this;
    }

    public JeproTree setTreeTemplate(String template){
        this.treeTemplate = template;
        return this;
    }

    public JeproTree setUseCheckBox(boolean useCheckBox){
        this.useCheckBox = useCheckBox;
        return this;
    }

    public JeproTree setUseSearch(boolean useSearch){
        this.useSearchBox = useSearch;
        return this;
    }
}