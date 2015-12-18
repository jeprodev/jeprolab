package com.jeprolab.views.installer;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.os.JeproOsTask;
import com.jeprolab.assets.tools.os.JeproServerKiller;
import com.jeprolab.views.installer.forms.JeproLabInstallerInstallationForm;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JeproInstallerThread extends Thread {
    private JeproLabInstaller installer;
    private JeproOsTask[] osTasks;
    private JeproLabInstallerInstallationForm installationForm;
    private int installSize;
    private String installationDirectory;
    private ArrayList<String> components;

    public JeproInstallerThread(JeproLabInstaller install, JeproLabInstallerInstallationForm progressBarForm, String instDir, JeproOsTask[] osT, int size, ArrayList<String> comps){
        super("Install thread");

        this.installer = install;
        this.installationForm = progressBarForm;
        this.installationDirectory = instDir;
        this.osTasks = osT;
        installSize = size;
        components = comps;
    }

    public void run(){
        installationForm.setMaximum(installSize /1024);

        /** return value ignored : already signaled in ServerKiller **/
        installationForm.installingLabel.setVisible(false);
        installationForm.message(JeproLab.getBundle().getString("JEPROLAB_STOPPING_ANY_JEPRODEV_SERVER"));
        JeproServerKiller.quitJeproServer();

        try{
            /** install user-selected package **/
            for(int i = 0; i < components.size(); i++){
                String component = components.get(i);
                installationForm.message(JeproLab.getBundle().getString("JEPROLAB_INSTALLING_LABEL") + " " + component);
                installComponent(component);
            }

            for(JeproOsTask osTask : osTasks){
                installationForm.message(JeproLab.getBundle().getString("JEPROLAB_PERFORMING_TASK_LABEL") + " " + osTask.getName());
                osTask.perform(installationDirectory, components);
            }
        }catch (FileNotFoundException fnf){
            installationForm.error(JeproLab.getBundle().getString("JEPROLAB_THE_INSTALLER_COULD_NOT_CREATE_THE_DESTINATION_DIRECTORY_MESSAGE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        installationForm.done();
    }

    private void installComponent(String name) throws  IOException {
        InputStream input = new BufferedInputStream(getClass().getResourceAsStream(name + ".tar.bz2"));
        input.read();
        input.read();
    }
}
