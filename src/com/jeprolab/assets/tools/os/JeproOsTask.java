package com.jeprolab.assets.tools.os;


import jeprolab.assets.extend.installer.JeproLabInstaller;

import java.io.IOException;
import java.util.ArrayList;

public abstract class JeproOsTask {
    protected JeproLabInstaller installer;
    protected String name;
    protected String label, directory;
    protected boolean enabled;

    public JeproOsTask(JeproLabInstaller install, String taskName){
        this.installer = install;
        this.name = taskName;
        //this.label = installer.getProperty("JEPROLAB_OS_TASK_" + taskName.toUpperCase() + "_LABEL");
        this.directory = getDefaultDirectory(install);
        this.enabled = true;
    }

    public String getDefaultDirectory(JeproLabInstaller installer){
        return null;
    }

    public String getDirectory(){ return directory; }

    public boolean isEnabled(){
        return enabled;
    }

    public String getName(){ return name; }

    public String getLabel(){ return label; }

    public void setEnabled(boolean enable){
        this.enabled = enable;
    }

    public void setDirectory(String dir){
        this.directory = dir;
    }

    public abstract void perform(String installDir, ArrayList fileSets) throws IOException;
}