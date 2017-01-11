package com.jeprolab.assets.tools;

import com.jeprolab.JeproLab;

import javafx.scene.control.Alert;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabTools {
    private static Logger logger = Logger.getLogger(JeproLab.class);
    private static Alert dialogBox = null;

    public static Date getDate(){
        return  new Date(System.currentTimeMillis());
    }

    public static void displayError(int errorCode, String errorMessage){
        if(dialogBox == null){
            dialogBox = new Alert(Alert.AlertType.ERROR);
        }else{
            dialogBox.setAlertType(Alert.AlertType.ERROR);
        }
        dialogBox.setTitle(JeproLab.getBundle().getString("JEPROLAB_AN_ERROR_OCCURRED_LABEL"));
        dialogBox.setContentText(errorMessage);
        dialogBox.showAndWait();
    }

    public static void logExceptionMessage(Level level, Throwable exception) {
        //try {
        File logFile = new File(JeproLab.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String path = logFile.getParentFile().getAbsolutePath() + File.separator + "logs" + File.separator;
        FileAppender appender;

        if (level.equals(Level.OFF)) {
                /*appender = new FileAppender(new XMLLayout(), path + " jeprolab_off_logs.txt");
                appender.setName("JeproLabOffLogs"); */
            //logger.addAppender(exception, exception);
        } else if (level.equals(Level.DEBUG)) {
                /*appender = new FileAppender(new XMLLayout(), path + "jeprolab_debug_logs.txt");
                appender.setName("JeproLabDebugLogs");
                logger.addAppender(appender); */
            logger.debug(exception, exception);
        } else if (level.equals(Level.INFO)) {
                /*appender = new FileAppender(new XMLLayout(), path + "jeprolab_info_logs.txt");
                appender.setName("JeproLabInfoLogs");
                logger.addAppender(appender); */
            logger.info(exception, exception);
        } else if (level.equals(Level.WARN)) {
                /*appender = new FileAppender(new XMLLayout(), path + "jeprolab_warning_logs.txt");
                appender.setName("JeproLabWarningLogs");
                logger.addAppender(appender); */
            logger.warn(exception, exception);
        } else if (level.equals(Level.ERROR)) {
                /*appender = new FileAppender(new XMLLayout(), path + "jeprolab_error_logs.txt");
                appender.setName("JeproLabErrorLogs");
                logger.addAppender(appender); */
            logger.error(exception, exception);
        } else if (level.equals(Level.FATAL)) {
                /*appender = new FileAppender(new XMLLayout(), path + "jeprolab_fatal_logs.txt");
                appender.setName("JeproLabFatalLogs");
                logger.addAppender(appender); */
            logger.fatal(exception, exception);
        }
        /*}catch (IOException ignored){

        }*/
    }
}
