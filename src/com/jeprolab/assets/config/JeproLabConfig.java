package com.jeprolab.assets.config;

import com.jeprolab.JeproLab;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;


public class JeproLabConfig {
    public static String dataBaseHost;
    public static String dataBaseName;
    public static String dataBaseManager;
    public static String dataBasePrefix;
    public static String dataBasePortNumber;
    public static String dataBaseUserName;
    public static String dataBasePassword;
    public static String appInstallDirectory;
    //public static String e;

    public static void initialize(){
        Properties configProp = new Properties();
        FileInputStream inputStream = null;

        try{
            inputStream = new FileInputStream(new File(JeproLab.class.getResource("assets/config/config.properties").toURI()));
            configProp.load(inputStream);
            dataBaseHost = configProp.getProperty("DATA_BASE_HOST");
            dataBaseName = configProp.getProperty("DATA_BASE_NAME");
            dataBaseManager = configProp.getProperty("DATA_BASE_MANAGER");
            dataBasePrefix = configProp.getProperty("DATA_BASE_PREFIX");
            dataBasePortNumber = configProp.getProperty("DATA_BASE_PORT_NUMBER");
            dataBaseUserName = configProp.getProperty("DATA_BASE_USER_NAME");
            dataBasePassword = configProp.getProperty("DATA_BASE_PASSWORD");
            appInstallDirectory = configProp.getProperty("APPLICATION_INSTALL_DIRECTORY");
        }catch (IOException | URISyntaxException excpt){
            excpt.printStackTrace();
        } finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //todo edit us
    public String getCookiePath(){
        return "";
    }

    public String getCookieDomain(){
        return "";
    }

    public static void setConfig(String configFilePath){

    }

    public static JeproLabConfig getConfiguration(){
        return new JeproLabConfig();
    }

    public int getLifeTime(){
        return 0;
    }

    public String getSessionHandler(){
        return "";
    }

    public static int getListLimit(){
        return 20;
    }
}