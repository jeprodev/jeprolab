package com.jeprolab.assets.tools.os;




import com.jeprolab.views.installer.JeproLabInstaller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JeproWindows  extends JeproOperatingSystem {
    @Override
    public String getInstallDirectory(String name){
        String programDirectory = System.getenv("ProgramFiles");
        if(programDirectory == null){
            programDirectory = "%ProgramFiles%";
        }
        return programDirectory + "\\" + name;
    }

    public JeproOsTask[] getOsTasks(JeproLabInstaller installer){
        return new JeproOsTask[]{};
    }

    public class JeproOsTaskLauncher extends JeproOsTask {
        public JeproOsTaskLauncher(JeproLabInstaller installer){
            super(installer, "jeprolab_launcher");
        }

        @Override
        public String getDefaultDirectory(JeproLabInstaller installer){
            return null;
        }

        @Override
        public void perform(String installerDirectory, ArrayList fileSets){
            if(enabled && fileSets.contains("jepro-windows")){
                File executable = new File(installerDirectory, "jeprolab.exe");
                if(executable.exists()){
                    String[] args = { executable.getPath(), "/i", System.getProperty("java.home") + File.separator + "bin"};
                    try{
                        Runtime.getRuntime().exec(args).waitFor();
                    }catch(IOException | InterruptedException ignored){}
                }
            }
        }
    }
}