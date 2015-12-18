package com.jeprolab.assets.tools.os;


import com.jeprolab.views.installer.JeproLabInstaller;

import java.io.File;
import java.io.IOException;

public abstract class JeproOperatingSystem {
    private static JeproOperatingSystem operatingSystem;

    public abstract String getInstallDirectory(String name);

    public JeproOsTask[] getOsTasks(JeproLabInstaller install){
        return new JeproOsTask[0];
    }

    public void makeDirectories(String directory) throws IOException, IOException {
        File file = new File(directory);
        if(!file.exists()){ file.mkdirs(); }
    }

    public static JeproOperatingSystem getOperatingSystem(){
        if(operatingSystem != null){
            return operatingSystem;
        }

        if(System.getProperty("mrj.version") != null){
            operatingSystem = new JeproMacOS();
        }else{
            String osName = System.getProperty("os.name");
            if(osName.contains("Windows")){
                operatingSystem = new JeproWindows();
            }else if(osName.contains("OS/2")){
                operatingSystem = new JeproHalfAnOs();
            }else if(osName.contains("VMS")){
                operatingSystem = new JeproVMS();
            }else{
                operatingSystem = new JeproUnix();
            }
        }
        return operatingSystem;
    }
}