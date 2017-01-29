package com.jeprolab.assets.config;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import org.apache.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabConfig {
    private static JeproLabConfig instance = null;
    public static String DATA_BASE_HOST;
    public static String DATA_BASE_NAME;
    public static String DATA_BASE_DRIVER;
    public static String DATA_BASE_PREFIX;
    public static String DATA_BASE_PORT_NUMBER;
    public static String DATA_BASE_USER_NAME;
    public static String DATA_BASE_PASSWORD;
    public static String APP_INSTALLED_DIRECTORY;
    public static String APP_UPDATE_URL;
    public static String INSTALLED_APP_VERSION;
    public static String INSTALLED_APP_PACKAGE;
    public static String CERTIFICATE_LOGO;
    public static String APPLICATION_WEBSITE;

    public static void initialize(){
        retrieveConfig();
    }

    private static void retrieveConfig(){
        //Properties configProp = new Properties();
        FileInputStream inputStream = null;

        //try{
            File configFile = new File(JeproLab.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            //String configPath = configFile.getParentFile().getAbsolutePath() + File.separator + "config" + File.separator + "config.properties";
            ResourceBundle configProp = ResourceBundle.getBundle("com.jeprolab.resources.config.config");

            /*inputStream = new FileInputStream(configPath);
            configProp.load(inputStream);
            DATA_BASE_HOST = configProp.getProperty("DATA_BASE_HOST");
            DATA_BASE_NAME = configProp.getProperty("DATA_BASE_NAME");
            DATA_BASE_DRIVER = configProp.getProperty("DATA_BASE_MANAGER");
            DATA_BASE_PREFIX = configProp.getProperty("DATA_BASE_PREFIX");
            DATA_BASE_PORT_NUMBER = configProp.getProperty("DATA_BASE_PORT_NUMBER");
            DATA_BASE_USER_NAME = configProp.getProperty("DATA_BASE_USER_NAME");
            DATA_BASE_PASSWORD = configProp.getProperty("DATA_BASE_PASSWORD");
            APP_INSTALLED_DIRECTORY = configProp.getProperty("APPLICATION_INSTALL_DIRECTORY");
            APP_UPDATE_URL = configProp.getProperty("APPLICATION_UPDATE_URL");
            INSTALLED_APP_PACKAGE = configProp.getProperty("APPLICATION_INSTALLED_VERSION");
            INSTALLED_APP_PACKAGE = configProp.getProperty("APPLICATION_INSTALLED_PACKAGE");
            CERTIFICATE_LOGO = configProp.getProperty("APPLICATION_CERTIFICATE_LOGO_PATH");
            APPLICATION_WEBSITE = configProp.getProperty("APPLICATION_WEBSITE_URL"); */

            DATA_BASE_HOST = configProp.getString("DATA_BASE_HOST");
            DATA_BASE_NAME = configProp.getString("DATA_BASE_NAME");
            DATA_BASE_DRIVER = configProp.getString("DATA_BASE_MANAGER");
            DATA_BASE_PREFIX = configProp.getString("DATA_BASE_PREFIX");
            DATA_BASE_PORT_NUMBER = configProp.getString("DATA_BASE_PORT_NUMBER");
            DATA_BASE_USER_NAME = configProp.getString("DATA_BASE_USER_NAME");
            DATA_BASE_PASSWORD = configProp.getString("DATA_BASE_PASSWORD");
            APP_INSTALLED_DIRECTORY = configProp.getString("APPLICATION_INSTALL_DIRECTORY");
            APP_UPDATE_URL = configProp.getString("APPLICATION_UPDATE_URL");
            INSTALLED_APP_PACKAGE = configProp.getString("APPLICATION_INSTALLED_VERSION");
            INSTALLED_APP_PACKAGE = configProp.getString("APPLICATION_INSTALLED_PACKAGE");
            CERTIFICATE_LOGO = configProp.getString("APPLICATION_CERTIFICATE_LOGO_PATH");
            APPLICATION_WEBSITE = configProp.getString("APPLICATION_WEBSITE_URL");
        /*}catch (IOException  ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        } finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch(IOException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }
            }
        } */
    }

    public int getLifeTime(){
        return 0;
    }

    public String getSessionHandler(){
        return "";
    }

    public static void setConfig(String configFilePath){

    }

    public static JeproLabConfig getConfiguration(){
        if(instance == null) {
            instance = new JeproLabConfig();
        }
        return instance;
    }
}
