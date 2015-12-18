package com.jeprolab.assets.tools.os;


public class JeproVMS extends JeproOperatingSystem {
    @Override
    public String getInstallDirectory(String name){
        return "./" + name.toLowerCase();
    }
}
