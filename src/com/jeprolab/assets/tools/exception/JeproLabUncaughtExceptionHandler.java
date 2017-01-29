package com.jeprolab.assets.tools.exception;

import com.jeprolab.JeproLab;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    private static Logger logger = Logger.getLogger(JeproLab.class);
    private static final Object loggerSynchronized = new Object();

    public void uncaughtException(Thread thread, Throwable exception){
        logExceptionMessage(Level.ERROR, JeproLab.getBundle().getString("JEPROLAB_ERROR_IN_THREAD_LABEL") + " " + thread.getName() + " : ", exception);

        if(JeproLab.getInstance().getApplicationForms() != null && JeproLab.getInstance().getUpdateInvoker() != null){
            logExceptionMessage(Level.ERROR, JeproLab.getBundle().getString("JEPROLAB_ERROR_IN_THREAD_LABEL") + " " + thread.getName() + " :", exception);
        }
    }

    public static void logExceptionMessage(Level level, Throwable exception){
        logExceptionMessage(level, exception.getMessage(), exception);
    }


    public static void logExceptionMessage(Level level, String title, Throwable exception){
        if(level.equals(Level.DEBUG)){
            synchronized (loggerSynchronized) {
                logger.debug(title, exception);
            }
        }else if(level.equals(Level.INFO)){
            synchronized (loggerSynchronized) {
                logger.debug(title, exception);
            }
        }if(level.equals(Level.WARN)){
            synchronized (loggerSynchronized) {
                logger.warn(title, exception);
            }
        }else if(level.equals(Level.ERROR)){
            synchronized (loggerSynchronized) {
                logger.error(title, exception);
            }
        }else if(level.equals(Level.FATAL)){
            synchronized (loggerSynchronized) {
                logger.fatal(title, exception);
            }
        }

    }
}
