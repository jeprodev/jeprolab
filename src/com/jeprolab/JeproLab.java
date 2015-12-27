package com.jeprolab;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabCookie;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.JeproLabWindowsButtons;
import com.jeprolab.models.*;
import com.jeprolab.views.application.JeproLabApplicationForm;
import com.jeprolab.views.application.JeproLabApplicationForms;
import com.jeprolab.views.installer.JeproLabInstallerForms;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class JeproLab extends Application {
    private static JeproLab instance;
    private JeproLabApplicationForms applicationForms;
    private Stage appStage;
    private JeproLabApplicationForm currentForm;
    private VBox formsContainer;
    private Pane formWrapper;
    private Node currentFormView;
    private boolean changingForm = false;
    private Stack<JeproLabApplicationForm> history = new Stack();
    private Stack<JeproLabApplicationForm> forwardHistory = new Stack();
    private JeproLabContext context;
    private JeproLabApplicationForm menuBar;
    private ToolBar applicationToolBar, windowsBar;

    private static ResourceBundle bundle, appProps;
    private String lang = "";

    public static final double APP_WIDTH = 1000;
    public static final double APP_HEIGHT = 800;
    public static final int APP_INSTALLER_WIDTH = 543;
    public static final int APP_INSTALLER_HEIGHT = 410;

    private Scene scene;
    public static File configurationFile;

    @Override
    public void start(Stage primaryStage) throws Exception {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();

        for (String argument : arguments) {
            if (argument.contains("Duser.language")) {
                String[] args = argument.split("=");
                lang = args[1];
                break;
            }
        }

        if (lang.equalsIgnoreCase("en")) {
            bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
        } else {
            bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
        }

        appProps = ResourceBundle.getBundle("com.jeprolab.resources.props.installer");


        /**
         * check if configuration file exist, if not means that the application is not installed so start installer gui
         */
        configurationFile = new File(JeproLab.class.getResource("assets/config/config.properties").toURI());

        if(!(configurationFile.isFile())){
            Parent root = new JeproLabInstallerForms();
            scene = new Scene(root, APP_INSTALLER_WIDTH, APP_INSTALLER_HEIGHT);
            scene.getStylesheets().setAll(JeproLab.class.getResource("assets/css/jeprolab.css").toExternalForm());
        }else{
            appStage = primaryStage;
            initialize();
            menuBar = new JeproLabApplicationForm("menu/menu.fxml");

            applicationToolBar = new ToolBar();
            formWrapper = new Pane();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            windowsBar = new ToolBar();

            //setting windows toolbar items
            final JeproLabWindowsButtons windowsButtons = new JeproLabWindowsButtons(primaryStage);
            windowsBar.setId("windows-bar");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);
            Label jeproLabApplicationTitle = new Label(bundle.getString("JEPROLAB_SITE_MANAGER_TITLE"));
            jeproLabApplicationTitle.setId("application-title");
            windowsBar.getItems().addAll(windowsButtons, spacer, jeproLabApplicationTitle, spacer2);

            formsContainer = new VBox(0);

            formsContainer.getChildren().addAll(windowsBar, menuBar.createView(), applicationToolBar, formWrapper);
            applicationForms = new JeproLabApplicationForms();

            if(context.employee == null || !context.employee.isLogged){
                //redirect user to the login form
                menuBar.setFormVisible(false);
                applicationToolBar.setVisible(false);
                //TO DO reset to this and hide next goToForm(applicationForms.loginForm);
                goToForm(applicationForms.addressesForm);
            }else{
                //redirect user to the dashboard
                menuBar.setFormVisible(true);
                applicationToolBar.setVisible(true);
            }
            Parent root = formsContainer;

            scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
            scene.getStylesheets().setAll(JeproLab.class.getResource("assets/css/jeprolab.css").toExternalForm());
        }
        instance = this;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initialize() {
        JeproLabLanguageModel language = null;
        JeproLabConfig.initialize();
        context = JeproLabContext.getContext();
        context.laboratory = JeproLabLaboratoryModel.initialize();

        JeproLabSettingModel.loadSettings();

        JeproLabLanguageModel.loadLanguages();

        context.cookie = JeproLabCookie.getCookie();

        //Todo manage connexion life time

        /** setting default country **/
        context.country = new JeproLabCountryModel(JeproLabSettingModel.getIntValue("default_country"), JeproLabSettingModel.getIntValue("default_lang"));

        if(context.cookie.lang_id > 0){
            language = new JeproLabLanguageModel(context.cookie.lang_id);
        }

        if(language == null || !JeproLabTools.isLoadedObject(language, "lang_id")){
            language = new JeproLabLanguageModel(JeproLabSettingModel.getIntValue("default_lang"));
        }

        context.language = language;
        int currency_id = (context.cookie.currency_id > 0) ? context.cookie.currency_id : JeproLabSettingModel.getIntValue("default_currency_id");
        context.currency = new JeproLabCurrencyModel(currency_id);
    }

    public Scene getScene(){ return scene; }

    public static ResourceBundle getBundle(){ return bundle; }

    public static JeproLab getInstance(){ return instance; }

    public void resetMenuAndToolBar(){
        applicationToolBar.setVisible(true);
        menuBar.setFormVisible(true);
    }

    public JeproLabApplicationForms getApplicationForms(){
        return applicationForms;
    }

    public Stage getAppStage(){
        return appStage;
    }

    public static ResourceBundle getApplicationProperties(){
        if(appProps == null){
            appProps = ResourceBundle.getBundle("jeprolab.resources.props.installer");
        }
        return appProps;
    }

    public void goToForm(JeproLabApplicationForm form) throws IOException {
        goToForm(form, true, false, true);
    }

    private void goToForm(JeproLabApplicationForm form, boolean addHistory, boolean force, boolean swapViews) throws IOException {
        if(form != null && (force || form != currentForm)){
            changingForm = true;
            if(swapViews) {
                Node view = form.createView();
                if (view == null) {
                    view = new Region();
                }
                //if(force || view != currentFormView)
                currentForm = form;
                formWrapper.getChildren().setAll(view);
                currentFormView = view;
            }

            // add page to history
            if(addHistory && currentForm != null){
                history.push(currentForm);
                forwardHistory.clear();
            }
            //update info
            //done
            changingForm = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}