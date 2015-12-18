package com.jeprolab.assets.tools.os;


public class JeproHalfAnOs extends JeproOperatingSystem {
    @Override
    public String getInstallDirectory(String name){
        return "C:\\" + name;
    }
}
