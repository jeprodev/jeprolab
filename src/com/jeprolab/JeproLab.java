package com.jeprolab;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.JeproLabUpdater;
import com.jeprolab.assets.tools.JeproWindowsButtons;
//import com.jeprolab.models.*;
import com.jeprolab.models.*;
import com.jeprolab.views.JeproLabApplicationForms;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Level;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLab extends Application {
    public boolean is_initialized = false;
    private static JeproLab instance;

    public static final double APP_WIDTH = 1000;
    public static final double APP_HEIGHT = 800;

    private static ResourceBundle bundle, appProps;
    private String lang = "";

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
    private Button userInfoBtn, userLogOutBtn;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");

        menuBar = new JeproLabApplicationForms.JeproLabApplicationForm("menu/menu.fxml");
        applicationToolBar = new ToolBar();

        formWrapper = new Pane();

        applicationToolBarCommandWrapper = new HBox();

        appStage = primaryStage;

        formsContainer = new VBox(0);
        formsContainer.getChildren().addAll(menuBar.createView(), applicationToolBar, formWrapper);

        Parent root = formsContainer;
        //context.employee = new JeproLabEmployeeModel();
        scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().setAll(JeproLab.class.getResource("resources/css/jeprolab.css").toExternalForm());

        //redirect user to the login form
        //menuBar.setFormVisible(false);
        applicationToolBar.setVisible(false);
        applicationForms = new JeproLabApplicationForms();
        goToForm(applicationForms.loginForm);

        instance = this;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize(){
    /*    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();

        for (String argument : arguments) {
            System.out.println(argument);
            if (argument.contains("Duser.language")) {
                String[] args = argument.split("=");
                lang = args[1];
                break;
            }
        }

        if (lang.equalsIgnoreCase("en")) {
            bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
        } else { */

        //}

        //appProps = ResourceBundle.getBundle("com.jeprolab.resources.props.installer");

        JeproLabLanguageModel language = null;

        JeproLabConfig.initialize();
        JeproLabUpdater.checkForNewVersion(JeproLabConfig.INSTALLED_APP_VERSION);
        context = JeproLabContext.getContext();
        context.laboratory = JeproLabLaboratoryModel.initialize();

        JeproLabSettingModel.loadSettings();

        JeproLabLanguageModel.loadLanguages();

        //context.cookie = JeproLabCookie.getCookie();

        /** setting default country **/
        context.country = new JeproLabCountryModel(JeproLabSettingModel.getIntValue("default_country"), JeproLabSettingModel.getIntValue("default_lang"));

        if(!lang.equals("")){
            language = JeproLabLanguageModel.getLanguageByIsoCode(lang);
        }

        if(language == null || language.language_id <= 0){
            language = new JeproLabLanguageModel(JeproLabSettingModel.getIntValue("default_lang"));
        }

        context.language = language;
        int currencyId = (context.currency != null) ? context.currency.currency_id : JeproLabSettingModel.getIntValue("default_currency");
        context.currency = new JeproLabCurrencyModel(currencyId);
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

    public void goToForm(JeproLabApplicationForms.JeproLabApplicationForm form){
        goToForm(form, true, false, true);
    }

    private void goToForm(JeproLabApplicationForms.JeproLabApplicationForm form, boolean addHistory, boolean force, boolean swapViews){
        try {
            if (form != null && (force || form != currentForm)) {
                changingForm = true;
                if (swapViews) {
                    //JeproLabContext.getContext().task = task;
                    Node view = form.createView();
                    if (view == null) {
                        view = new Region();
                    }
                    //if(force || view != currentFormView)
                    currentForm = form;
                    formWrapper.getChildren().setAll(view);
                    currentFormView = view;
                }
                //currentForm.controller.task = task;
                // add page to history
                if (addHistory && currentForm != null) {
                    history.push(currentForm);
                    forwardHistory.clear();
                }
                //update info
                //currentForm.updateFormCommand();
                changingForm = false;
            } else {
                JeproLabContext.getContext().controller.initializeContent();
            }
        }catch (IOException ignored){
            JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
        }
    }

    public ToolBar getApplicationToolBar(){
        return applicationToolBar;
    }

    public HBox getApplicationToolBarCommandWrapper(){
        return applicationToolBarCommandWrapper;
    }

    public Button getUserInfoBtn(){
        return userInfoBtn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}