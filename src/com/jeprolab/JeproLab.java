package com.jeprolab;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.*;
//import com.jeprolab.models.*;
import com.jeprolab.assets.tools.events.JeproLabEventManager;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.data.cache.JeproLabDataCacheUpdateInvoker;
import com.jeprolab.models.*;
import com.jeprolab.models.core.JeproLabRequest;
import com.jeprolab.views.JeproLabApplicationForms;

import com.jeprolab.views.JeproLabSplashForm;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLab  extends Application {
    private static String APPLICATION_NAME;
    private static String APPLICATION_DESCRIPTION;
    public boolean is_initialized = false;
    private static JeproLab instance;
    private JeproLabEventManager eventManager;

    public static final double APP_WIDTH = 1000;
    public static final double APP_HEIGHT = 800;

    public static final double SPLASH_WIDTH = 520;
    public static final double SPLASH_HEIGHT = 294;

    private static final int NUMBER_OF_THREADS = 4;

    private static ResourceBundle bundle, appProps;
    private String lang = "";
    private Thread.UncaughtExceptionHandler commonDefaultUncaughtExceptionHandler;

    private JeproLabApplicationForms applicationForms;
    private Stage appStage;
    private JeproLabApplicationForms.JeproLabApplicationForm currentForm;
    private VBox formsContainer;
    private Pane formWrapper;
    private Node currentFormView;
    private boolean changingForm = false;
    private Stack<JeproLabApplicationForms.JeproLabApplicationForm> history = new Stack<>();
    private Stack<JeproLabApplicationForms.JeproLabApplicationForm> forwardHistory = new Stack<>();
    private JeproLabContext context;
    private JeproLabApplicationForms.JeproLabApplicationForm menuBar;
    private ToolBar applicationToolBar, windowsBar;
    private HBox applicationToolBarCommandWrapper, userInfoWrapper;
    private Button userInfoBtn;
    private Scene scene;
    private JeproLabDataCacheUpdateInvoker updateInvoker;

    public ExecutorService executor;

    public Scene getScene(){ return scene; }

    public static ResourceBundle getBundle(){ return bundle; }

    public static synchronized JeproLab getInstance(){
        if(instance == null){
            new JeproLab();
        }
        return instance;
    }

    public JeproLabApplicationForms getApplicationForms(){
        return applicationForms;
    }

    public Stage getApplicationStage(){
        return appStage;
    }

    public Button getUserInfoBtn(){
        return userInfoBtn;
    }

    public JeproLabDataCacheUpdateInvoker getUpdateInvoker(){
        return updateInvoker;
    }

    @Override
    public void init() throws Exception {
        try{
            notifyPreloader(new Preloader.ProgressNotification(0));
            JeproLabConfig.initialize();
            instance = this;
            is_initialized = true;
            bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
            JeproLab.APPLICATION_NAME = "Jeprolab " + JeproLabConfig.INSTALLED_APP_VERSION;
            JeproLab.APPLICATION_DESCRIPTION = JeproLab.APPLICATION_NAME + bundle.getString("JEPROLAB_APPLICATION_DESCRIPTION_LABEL");

            JeproLabUpdater.checkForNewVersion(JeproLabConfig.INSTALLED_APP_VERSION);
            context = JeproLabContext.getContext();
            context.laboratory = JeproLabLaboratoryModel.initialize();

            notifyPreloader(new  Preloader.ProgressNotification(0.05));
            JeproLabSettingModel.loadSettings();

            JeproLabLanguageModel.loadLanguages();
            notifyPreloader(new  Preloader.ProgressNotification(0.15));

            /** setting default country **/
            context.country = new JeproLabCountryModel(JeproLabSettingModel.getIntValue("default_country"), JeproLabSettingModel.getIntValue("default_lang"));

            notifyPreloader(new  Preloader.ProgressNotification(0.20));
            RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            List<String> arguments = runtimeMxBean.getInputArguments();

            for (String argument : arguments){
                if (argument.contains("Duser.language")) {
                    String[] args = argument.split("=");
                    lang = args[1];
                    break;
                }
            }

             JeproLabLanguageModel language = null;
            if(!lang.equalsIgnoreCase("")) {
                bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
                language = JeproLabLanguageModel.getLanguageByIsoCode(lang);
            } else {
                bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
            }

            //request = JeproLabRequest.getInstance();

            if(language == null || language.language_id <= 0){
                language = new JeproLabLanguageModel(JeproLabSettingModel.getIntValue("default_lang"));
            }

            context.language = language;
            int currencyId = (context.currency != null) ? context.currency.currency_id : JeproLabSettingModel.getIntValue("default_currency");
            context.currency = new JeproLabCurrencyModel(currencyId);

            applicationToolBar = new ToolBar();
            applicationToolBarCommandWrapper = new HBox();
            applicationToolBarCommandWrapper.getStyleClass().setAll("segmented-command-wrapper");

            applicationToolBar.getStyleClass().add("jeprolab-toolbar");
            userInfoWrapper = new HBox();
            userInfoWrapper.getStyleClass().setAll("segmented-command-wrapper");
            userInfoBtn = new Button();
            userInfoWrapper.getChildren().addAll(userInfoBtn);

            Region toolBarSpacer = new Region();
            HBox.setHgrow(toolBarSpacer, Priority.ALWAYS);

            applicationToolBar.getItems().addAll(applicationToolBarCommandWrapper, toolBarSpacer, userInfoWrapper);

            menuBar = new JeproLabApplicationForms.JeproLabApplicationForm("menu/menu.fxml");
            notifyPreloader(new  Preloader.ProgressNotification(0.25));

            applicationForms = new JeproLabApplicationForms();

            double step =((0.7/applicationForms.forms.size()));

            double currentStep = 0.25;
            for (JeproLabApplicationForms.JeproLabApplicationForm form : applicationForms.forms) {
                currentStep = currentStep + step;
                form.createView();
                notifyPreloader(new Preloader.ProgressNotification(currentStep));
            }

            formWrapper = new Pane();
            formsContainer = new VBox(0);
            formsContainer.getChildren().addAll(menuBar.createViewNode(), applicationToolBar, formWrapper);

            if(context.employee == null || !context.employee.is_logged) {
                menuBar.setFormVisible(false);
                applicationToolBar.setVisible(false);
                goToForm(applicationForms.loginForm);
            } else {
                menuBar.setFormVisible(true);
                applicationToolBar.setVisible(true);
                goToForm(applicationForms.dashBoardForm);
            }

            scene = new Scene(formsContainer, APP_WIDTH, APP_HEIGHT);
            scene.getStylesheets().setAll(JeproLab.class.getResource("resources/css/jeprolab.css").toExternalForm());

            notifyPreloader(new Preloader.ProgressNotification(1));
        }catch (Exception ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            Platform.exit();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        appStage = primaryStage;
        appStage.setResizable(false);
        appStage.getIcons().add(new Image(JeproLab.class.getResourceAsStream("resources/images/microscope.png")));
        appStage.setTitle(bundle.getString("JEPROLAB_SITE_MANAGER_TITLE"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        try {
            if(!executor.isShutdown() || !executor.isTerminated()){
                executor.shutdown();
            }
            Platform.exit();
        }catch (Exception ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
        }
    }

    public void goToForm(JeproLabApplicationForms.JeproLabApplicationForm form){
        goToForm(form, true, false, true);
    }

    private void goToForm(JeproLabApplicationForms.JeproLabApplicationForm form, boolean addHistory, boolean force, boolean swapViews){
        if (form != null && (force || form != currentForm)) {
            changingForm = true;
            if (swapViews) {

                Node view = form.createViewNode();
                if (view == null) {
                    view = new Region();
                }

                currentForm = form;
                formWrapper.getChildren().setAll(view);
                currentFormView = view;
            }

            if (addHistory && currentForm != null) {
                history.push(currentForm);
                forwardHistory.clear();
            }

            changingForm = false;
        } else {
            JeproLabContext.getContext().controller.initializeContent();
        }
    }

    public void resetMenuAndToolBar(boolean value){
        applicationToolBar.setVisible(value);
        menuBar.setFormVisible(value);
    }

    public HBox getApplicationToolBarCommandWrapper() {
        return applicationToolBarCommandWrapper;
    }

    public static void main(String[] args){
        LauncherImpl.launchApplication(JeproLab.class, JeproLabPreloader.class, args);
    }
}