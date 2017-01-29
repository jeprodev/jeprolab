package com.jeprolab;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabUpdater;
import com.jeprolab.assets.tools.JeproWindowsToolBar;
import com.jeprolab.assets.tools.events.JeproLabEventManager;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.data.cache.JeproLabDataCacheUpdateInvoker;
import com.jeprolab.models.*;
import com.jeprolab.views.JeproLabApplicationForms;
import com.jeprolab.views.JeproLabSplashForm;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabMainFrame  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        JeproLabConfig.initialize();
        primaryStage.initStyle(StageStyle.UNDECORATED);

        applicationForms = new JeproLabApplicationForms();

        JeproLabSplashForm splashForm = new JeproLabSplashForm();

        //context.employee = new JeproLabEmployeeModel();
        scene = new Scene(splashForm, SPLASH_WIDTH, SPLASH_HEIGHT);

        //redirect user to the login form
        //menuBar.setFormVisible(false);
        //applicationToolBar.setVisible(false);

        //goToForm(applicationForms.loginForm);
        //JeproLabConfig.initialize();
        instance = this;
        appStage = primaryStage;
        primaryStage.setScene(scene);
        //splashForm.createView(); */
        primaryStage.show();
    }
 /**
    public void renderApplication(){
        initialize();
        JeproWindowsToolBar windowsToolBar = new JeproWindowsToolBar(appStage);


        appStage.setScene(scene);

        //appStage.show();
    }

    public void initialize(){
        Worker<Boolean> worker = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                JeproLabLanguageModel language = null;
                JeproLabUpdater.checkForNewVersion(JeproLabConfig.INSTALLED_APP_VERSION);
                context = JeproLabContext.getContext();

                return null;
            }
        };
    /*

         * /

        //}

        //appProps = ResourceBundle.getBundle("com.jeprolab.resources.props.installer");

        userInfoBtn = new Button();
        userInfoBtn.getStyleClass().addAll("first");



        //context.cookie = JeproLabCookie.getCookie();


        if(!lang.equals("")){

        }




        is_initialized = true;
    }













    public ToolBar getApplicationToolBar(){
        return applicationToolBar;
    }

    public HBox getApplicationToolBarCommandWrapper(){
        return applicationToolBarCommandWrapper;
    }



    public synchronized void initializeEngine(){
        if(!is_initialized){
            /** setting up exception handler ** /
            installExceptionHandler();

            /**
             * create global event Manager
             *** /
            eventManager = new JeproLabEventManager();




        }
    }

    /**
     * Installs an uncaught exception handler that logs errors to the message as well as to the console.
     * /
    private void installExceptionHandler(){
        try {
            commonDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            JeproLabUncaughtExceptionHandler handler = new JeproLabUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }catch(SecurityException ignored){
            commonDefaultUncaughtExceptionHandler = null;
            Logger logger = Logger.getLogger(JeproLab.class);
            logger.warn(bundle.getString("JEPROLAB_HAS_NO_PERMISSION_TO_INSTALL_CUSTOM_EXCEPTION_HANDLER_WILL_RUN_WITHOUT_ONE_MESSAGE"), ignored);
        }
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        JeproLabSplashForm splashForm = new JeproLabSplashForm();
        scene = new Scene(, SPLASH_WIDTH, SPLASH_HEIGHT);

    }

    /**
     *
     * @param args
     * /
    public static void main(String[] args) {
        try{

            if(args.length == 0){
                //JeproLabConfigPersistence.getInstance().createBlank();
                //JeproLabMainFrame.createInstance();
            }launch(args);
        }
    }*/
}
