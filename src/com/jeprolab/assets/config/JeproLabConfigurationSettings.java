package com.jeprolab.assets.config;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.mysql.fabric.xmlrpc.base.Params;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Level;

import java.io.*;
import java.util.Properties;
import org.apache.commons.configuration2.PropertiesConfiguration;

/**
 *
 * Created by jeprodev on 18/06/2016.
 */
public class JeproLabConfigurationSettings {
    public static final int JEPROLAB_ROUND_UP_PRICE = 1;
    public static final int JEPROLAB_ROUND_DOWN_PRICE = 0;

    public static final int JEPROLAB_TAX_EXCLUDED = 3;
    public static final int JEPROLAB_TAX_INCLUDED = 7;

    public static int JEPROLAB_PRICE_DISPLAY_PRECISION = 2;


    public static String JEPROLAB_CATEGORY_IMAGE_DIRECTORY = "";
    public static String JEPROLAB_ANALYZE_IMAGE_DIRECTORY = "";
    public static String JEPROLAB_TMP_IMAGE_DIRECTORY = "media/com_jeproshop/im";
    public static String JEPROLAB_CUSTOMER_IMAGE_DIRECTORY = "media/com_jeproshop/images/customers/";
    public static String JEPROLAB_MAILING_DIRECTORY = "";
    public static String JEPROLAB_CURRENCY_FEED_URL = "http://www.floatrates.com/daily/usd.xml";

    public static int LIST_LIMIT = 24;

    public static void updateConfig(){
        File managementFile;
        FileInputStream inputStream = null;
        Properties managementProp;
        try{
            managementFile = new File(JeproLab.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String managementPath = managementFile.getAbsolutePath() + File.separator + "com" + File.separator + "jeprolab" +  File.separator;
            managementPath += "assets" + File.separator + "config" + File.separator + "manage.properties";

            inputStream = new FileInputStream(managementPath);
            managementProp = new Properties();
            managementProp.load(inputStream);

            JEPROLAB_CATEGORY_IMAGE_DIRECTORY = managementProp.getProperty("JEPROLAB_CATEGORY_IMAGE_DIRECTORY");
            JEPROLAB_ANALYZE_IMAGE_DIRECTORY = managementProp.getProperty("JEPROLAB_ANALYZE_IMAGE_DIRECTORY");
            JEPROLAB_TMP_IMAGE_DIRECTORY = managementProp.getProperty("JEPROLAB_TMP_IMAGE_DIRECTORY");
            JEPROLAB_CUSTOMER_IMAGE_DIRECTORY = managementProp.getProperty("JEPROLAB_CUSTOMER_IMAGE_DIRECTORY");
            JEPROLAB_MAILING_DIRECTORY = managementProp.getProperty("JEPROLAB_MAILING_DIRECTORY");
            JEPROLAB_CURRENCY_FEED_URL = managementProp.getProperty("JEPROLAB_CURRENCY_FEED_URL");
        } catch (IOException ignored) {
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }
            }
        }
    }

    public static boolean saveConfig(){
        File managementFile;
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
        Configuration managementProp;
        try {
            managementFile = new File(JeproLab.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String managementPath = managementFile.getAbsolutePath() + File.separator + "com" + File.separator + "jeprolab" + File.separator;
            managementPath += "assets" + File.separator + "config" + File.separator + "manage.properties";

            builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(params.properties()
            .setFileName(managementPath).setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

            managementProp = builder.getConfiguration();

            managementProp.setProperty("JEPROLAB_CATEGORY_IMAGE_DIRECTORY", JEPROLAB_CATEGORY_IMAGE_DIRECTORY);
            managementProp.setProperty("JEPROLAB_ANALYZE_IMAGE_DIRECTORY", JEPROLAB_ANALYZE_IMAGE_DIRECTORY);
            managementProp.setProperty("JEPROLAB_TMP_IMAGE_DIRECTORY", JEPROLAB_TMP_IMAGE_DIRECTORY);
            managementProp.setProperty("JEPROLAB_CUSTOMER_IMAGE_DIRECTORY", JEPROLAB_CUSTOMER_IMAGE_DIRECTORY);
            managementProp.setProperty("JEPROLAB_MAILING_DIRECTORY", JEPROLAB_MAILING_DIRECTORY);
            managementProp.setProperty("JEPROLAB_CURRENCY_FEED_URL", JEPROLAB_CURRENCY_FEED_URL);
            builder.save();
        }catch (ConfigurationException ignored) {
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }
        return true;
    }
}
