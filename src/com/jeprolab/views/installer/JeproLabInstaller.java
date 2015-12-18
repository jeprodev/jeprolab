package com.jeprolab.views.installer;


import com.jeprolab.views.installer.forms.JeproLabInstallerInstallationForm;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.jeprolab.JeproLab;
//import com.jeprolab.assets.extend.installer.forms.JeproLabInstallerInstallationForm;
import com.jeprolab.assets.tools.os.JeproOperatingSystem;
import com.jeprolab.assets.tools.os.JeproOsTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class JeproLabInstaller {
    private static ResourceBundle props;
    private byte[] buffer;
    private static JeproLabInstaller instance;
    private boolean isGUI = false;
    public JeproOsTask[] osTasks;

    public JeproLabInstaller(){
        props = JeproLab.getApplicationProperties();
        buffer = new byte[32768];
        instance = this;
    }

    public void install(JeproLabInstallerForms installerForms){
        ArrayList components = new ArrayList();
        int size = 0;

        /* for(int i = 0; i < ; i++){
            if(((JCheckBox)comp.getComponent(i))
                    .getModel().isSelected())
            {
                size += installer.getIntegerProperty(
                        "comp." + ids.elementAt(i) + ".real-size");
                components.addElement(installer.getProperty(
                        "comp." + ids.elementAt(i) + ".fileset"));
            }
        } */

        String installDirectory = installerForms.directoryForm.getInstallInSelector().getSelectedDirectory().getText();
        Map osTaskDirectories = installerForms.directoryForm.getInstallInSelector().osTaskDirectories;
        for (Object o : osTaskDirectories.keySet()) {
            JeproOsTask osTask = (JeproOsTask) o;
            String dir = ((TextField) osTaskDirectories.get(osTask)).getText();
            if (dir != null && dir.trim().length() != 0) {
                osTask.setEnabled(true);
                osTask.setDirectory(dir);
            } else {
                osTask.setEnabled(false);
            }
        }

        osTasks = JeproOperatingSystem.getOperatingSystem().getOsTasks(this);
        JeproInstallerThread thread = new JeproInstallerThread(this, installerForms.installationForm, installDirectory, osTasks, size, null);
        installerForms.installationForm.setThread(thread);
        thread.start();
    }

    public void installerChecker(){
        String javaVersion = System.getProperty("java.version");
        props = JeproLab.getApplicationProperties();

        if(javaVersion.compareTo("1.8") < 0){
            String errorMessage = JeproLab.getBundle().getString("JEPROLAB_YOUR_ARE_RUNNING_JAVA_VERSION_LABEL") + javaVersion;
            errorMessage += " " + JeproLab.getBundle().getString("JEPROLAB_FROM_LABEL") + " " + System.getProperty("java.vendor");
            errorMessage += ".\n" + JeproLab.getBundle().getString("JEPROLAB_THIS_INSTALLER_REQUIRES_JAVA_1_8_OR_LATER_MESSAGE");
            errorAndExit(isGUI, errorMessage);
        }

        if(pathHasExclamation()){
            String errorMessage = JeproLab.getBundle().getString("JEPROLAB_YOU")  + "\n"
                    + JeproLab.getBundle().getString("JEPROLAB_") + "\n"
                    + JeproLab.getBundle().getString("JEPROLAB_") + "\n"
                    + JeproLab.getBundle().getString("JEPROLAB_PLEASE_MOVE_THE_INSTALLER_SOME_WHERE_");
            errorAndExit(isGUI, errorMessage);
        }

        props = JeproLab.getApplicationProperties();


        buffer = new byte[32768];
    }

    /*public JeproLabGUIInstaller install(){
        this.isGUI = true;
        installerChecker();
        return new JeproLabGUIInstaller();
    }

    public void install(boolean isTerminal){
        if(isTerminal){
            JeproLabTerminalInstaller.install();
        }else{
            System.err.println(JeproLab.getBundle().getString("Usage:"));
            System.err.println(JeproLab.getBundle().getString("java -jar <installer JAR>"));
            System.err.println(JeproLab.getBundle().getString("java -jar <installer JAR> text"));
            System.err.println(JeproLab.getBundle().getString("java -jar <installer JAR> auto"
                    + " <install dir> [unix-script=<dir>] [unix-man=<dir>]"));
            System.err.println(JeproLab.getBundle().getString("text parameter starts installer in text-only mode."));
            System.err.println(JeproLab.getBundle().getString("auto parameter starts installer in non-interactive mode."));
        }
    } */

    private boolean pathHasExclamation(){
        /*class JeproInstaller = JeproLabInstaller.class;
        ProtectionDomain domain = JeproInstaller.getProtectionDomain();
        SourceCode source = domain.getSource();
        URL sourceUrl = source.getLocation();

        return sourceUrl.toString().contains("!"); */ return false;
    }

    public String getProperty(String name){
        return props.getString(name);
    }

    public int getIntegerProperty(String name){
        try{
            return Integer.parseInt(props.getString(name));
        }catch(Exception excpt){
            return -1;
        }
    }

    public void copy(InputStream in, String outFile, JeproLabInstallerInstallationForm progress) throws IOException {
        File outputFile = new File(outFile);
        JeproOperatingSystem.getOperatingSystem().makeDirectories(outputFile.getParent());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
        int count;

        for(;;){
            count = in.read(buffer, 0, Math.min(in.available(), buffer.length));
            if(count == -1 || count == 0){ break; }
            out.write(buffer, 0, count);
            if(progress != null){
                progress.advance(count);
            }
        }
        out.close();
    }

    private void errorAndExit(boolean isGUI, String errorMessage){
        if(isGUI){
            TextArea messageContainer = new TextArea(errorMessage);
            //JeproDialogs.showMessageDialog(null, messageContainer, "JeproLab installer error....", JeproDialogs.ERROR_MESSAGE);
        }else{
            System.err.println(errorMessage);
        }
        System.exit(1);
    }

    public static JeproLabInstaller getInstance() {
        if(instance == null){
            instance = new JeproLabInstaller();
        }
        return instance;
    }
}