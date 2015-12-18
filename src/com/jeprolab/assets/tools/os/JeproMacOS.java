package com.jeprolab.assets.tools.os;


public class JeproMacOS extends JeproUnix {
    @Override
    public String getInstallDirectory(String name){
        return "/Applications/" + name;
    }

    @Override
    public String getExtraClassPath(){
        return "/System/Library/Java/:";
    }
}