package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabPopupTools;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.JeproLabCurrencyModel;
import com.jeprolab.models.JeproLabLanguageModel;
import com.jeprolab.models.JeproLabSettingModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.apache.log4j.Level;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFileManagementController extends JeproLabController{
    private Button saveConfigBtn;
    @FXML
    public JeproFormPanel jeproLabFileManagementPaneWrapper;
    public Label jeproLabFileManagementTemporallyImageRepositoryLabel, jeproLabFileManagementCategoryImageRepositoryLabel;
    public Label jeproLabFileManagementAnalyzeImageRepositoryLabel, jeproLabFileManagementCustomerImageRepositoryLabel;
    public Label jeproLabFileManagementImageRepositoryLabel, jeproLabFileManagementCurrencyFeedUrlLabel;
    public Label jeproLabFileManagementMethodsImageRepositoryLabel;
    public TextField jeproLabFileManagementTemporallyImageRepository, jeproLabFileManagementCategoryImageRepository;
    public TextField jeproLabFileManagementAnalyzeImageRepository, jeproLabFileManagementCustomerImageRepository;
    public TextField jeproLabFileManagementMethodsImageRepository;
    public TextField jeproLabFileManagementImageRepository;
    public ComboBox<String> jeproLabFileManagementCurrencyFeedUrl;
    public TabPane jeproLabFileManagementTabPane;
    public Tab jeproLabFileManagementImageRepositoriesTab;
    public GridPane jeproLabFleManagementFormLayout;


    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        jeproLabFileManagementTabPane.setPrefSize(800, 600);
        jeproLabFileManagementTabPane.setLayoutX((JeproLab.APP_WIDTH - 800)/2);
        jeproLabFileManagementTabPane.setLayoutY(40);

        jeproLabFleManagementFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150), new ColumnConstraints(600)
        );

        jeproLabFileManagementImageRepositoriesTab.setText(bundle.getString("JEPROLAB_FOLDER_AND_LINK_LABEL"));

        jeproLabFileManagementTemporallyImageRepositoryLabel.setText(bundle.getString("JEPROLAB_TEMPORALLY_LABEL"));
        jeproLabFileManagementCategoryImageRepositoryLabel.setText(bundle.getString("JEPROLAB_CATEGORY_LABEL"));
        jeproLabFileManagementAnalyzeImageRepositoryLabel.setText(bundle.getString("JEPROLAB_ANALYZE_LABEL"));
        jeproLabFileManagementCustomerImageRepositoryLabel.setText(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        jeproLabFileManagementCurrencyFeedUrlLabel.setText(bundle.getString("JEPROLAB_CURRENCY_FEED_URL_LABEL"));
        jeproLabFileManagementMethodsImageRepositoryLabel.setText(bundle.getString("JEPROLAB_ANALYZE_METHODS_LABEL"));
        jeproLabFileManagementImageRepositoryLabel.setText(bundle.getString("JEPROLAB_TO_LABEL"));

        GridPane.setMargin(jeproLabFileManagementTemporallyImageRepositoryLabel, new Insets(15, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementTemporallyImageRepository, new Insets(15, 15, 5, 0));
        GridPane.setMargin(jeproLabFileManagementCategoryImageRepositoryLabel, new Insets(5, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementCategoryImageRepository, new Insets(5, 15, 5, 0));
        GridPane.setMargin(jeproLabFileManagementAnalyzeImageRepositoryLabel, new Insets(5, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementAnalyzeImageRepository, new Insets(5, 15, 5, 0));
        GridPane.setMargin(jeproLabFileManagementCustomerImageRepositoryLabel, new Insets(5, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementCustomerImageRepository, new Insets(5, 15, 5, 0));
        GridPane.setMargin(jeproLabFileManagementCurrencyFeedUrlLabel, new Insets(5, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementCurrencyFeedUrl, new Insets(5, 15, 5, 0));
        GridPane.setMargin(jeproLabFileManagementMethodsImageRepositoryLabel, new Insets(5, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementMethodsImageRepository, new Insets(5, 15, 5, 0));
        GridPane.setMargin(jeproLabFileManagementImageRepositoryLabel, new Insets(5, 15, 5, 20));
        GridPane.setMargin(jeproLabFileManagementImageRepository, new Insets(5, 15, 5, 0));

        setCurrencyFeedUrls();
    }

    @Override
    public void initializeContent(){
        Worker<Boolean> worker = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception{
                if(isCancelled()){
                    return false;
                }
                JeproLabConfigurationSettings.updateConfig();
                return true;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                updateConfigurationForm();
            }
        };
        new Thread((Task)worker).start();
        updateToolBar();
        addEventListeners();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveConfigBtn = new Button(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        saveConfigBtn.getStyleClass().add("save-btn");
        saveConfigBtn.setDisable(true);
        commandWrapper.getChildren().addAll(saveConfigBtn);
    }

    private void addEventListeners(){
        jeproLabFileManagementTemporallyImageRepository.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                saveConfigBtn.setDisable(false);
            }
        }));

        jeproLabFileManagementAnalyzeImageRepository.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                saveConfigBtn.setDisable(false);
            }
        }));

        jeproLabFileManagementMethodsImageRepository.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                saveConfigBtn.setDisable(false);
            }
        }));

        jeproLabFileManagementCategoryImageRepository.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                saveConfigBtn.setDisable(false);
            }
        }));

        jeproLabFileManagementCurrencyFeedUrl.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                String prefix = newValue.substring(newValue.length() - 7, newValue.length() - 4);
                if(!prefix.equals(JeproLabCurrencyModel.getCurrencyIsoCodeByCurrencyId(JeproLabSettingModel.getIntValue("default_currency")))){
                    JeproLabPopupTools.displayMessage(Alert.AlertType.INFORMATION,
                        bundle.getString("JEPROLAB_CURRENCY_FEED_URL_CHANGE_LABEL"),
                        bundle.getString("JEPROLAB_CURRENCY_FEED_URL_CHANGE_WARNING_MESSAGE"));
                }
                saveConfigBtn.setDisable(false);
            }
        }));

        jeproLabFileManagementCustomerImageRepository.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                saveConfigBtn.setDisable(false);
            }
        }));

        saveConfigBtn.setOnAction(evt -> {
            JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY = jeproLabFileManagementCategoryImageRepository.getText();
            JeproLabConfigurationSettings.JEPROLAB_CUSTOMER_IMAGE_DIRECTORY = jeproLabFileManagementCustomerImageRepository.getText();
            String currencyFeedUrl = jeproLabFileManagementCurrencyFeedUrl.getValue();
            String prefix = currencyFeedUrl.substring(currencyFeedUrl.length() - 7, currencyFeedUrl.length() - 4);
            currencyFeedUrl = currencyFeedUrl.substring(("(" + bundle.getString("JEPROLAB_" + prefix.toUpperCase() + "_LABEL") + ") - ").length(), currencyFeedUrl.length());

            JeproLabConfigurationSettings.JEPROLAB_CURRENCY_FEED_URL = currencyFeedUrl;
            JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY = jeproLabFileManagementTemporallyImageRepository.getText();
            if(JeproLabConfigurationSettings.saveConfig()){
                saveConfigBtn.setDisable(true);
            }

            Worker<Boolean> worker = new Task<Boolean>() {
                private String fileDestination;
                @Override
                protected Boolean call() throws Exception {
                    if(isCancelled()){
                        return false;
                    }
                    String url = jeproLabFileManagementCurrencyFeedUrl.getValue();
                    url = url.substring(("(" + bundle.getString("JEPROLAB_" + prefix.toUpperCase() + "_LABEL") + ") - ").length(), url.length());
                    File file = new File(JeproLab.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                    fileDestination = file.getAbsolutePath() + File.separator + "config" + File.separator + "currencies" + File.separator + prefix + ".xml";
                    return JeproLabTools.retrieveXmlFile(url, fileDestination);
                }

                @Override
                protected void failed(){
                    super.failed();
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
                }

                @Override
                protected void succeeded(){
                    super.succeeded();
                    JeproLabCurrencyModel.updateCurrencyRates(fileDestination);
                }
            };

            new Thread((Task)worker).start();
        });
    }

    private void updateConfigurationForm(){
        Platform.runLater(() -> {
            jeproLabFileManagementTemporallyImageRepository.setText(JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY);
            jeproLabFileManagementCategoryImageRepository.setText(JeproLabConfigurationSettings.JEPROLAB_CATEGORY_IMAGE_DIRECTORY);
            jeproLabFileManagementAnalyzeImageRepository.setText(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY);
            jeproLabFileManagementCustomerImageRepository.setText(JeproLabConfigurationSettings.JEPROLAB_CUSTOMER_IMAGE_DIRECTORY);
            //jeproLabFileManagementImageRepository.setText(JeproLabConfigurationSettings.);
        });
    }

    private void setCurrencyFeedUrls(){
        jeproLabFileManagementCurrencyFeedUrl.setPromptText(bundle.getString("JEPROLAB_SELECT_CURRENCY_FEED_URL_LABEL"));
        jeproLabFileManagementCurrencyFeedUrl.getItems().clear();
        jeproLabFileManagementCurrencyFeedUrl.getItems().addAll(
            "(" + bundle.getString("JEPROLAB_USD_LABEL") + ") - " + "http://www.floatrates.com/daily/usd.xml",
            "(" + bundle.getString("JEPROLAB_EUR_LABEL") + ") - " + "http://www.floatrates.com/daily/eur.xml",
            "(" + bundle.getString("JEPROLAB_GBP_LABEL") + ") - " + "http://www.floatrates.com/daily/gbp.xml",
            "(" + bundle.getString("JEPROLAB_AUD_LABEL") + ") - " + "http://www.floatrates.com/daily/aud.xml",
            "(" + bundle.getString("JEPROLAB_CAD_LABEL") + ") - " + "http://www.floatrates.com/daily/cad.xml",
            "(" + bundle.getString("JEPROLAB_JPY_LABEL") + ") - " + "http://www.floatrates.com/daily/jpy.xml",
            "(" + bundle.getString("JEPROLAB_CHF_LABEL") + ") - " + "http://www.floatrates.com/daily/chf.xml",
            "(" + bundle.getString("JEPROLAB_DZD_LABEL") + ") - " + "http://www.floatrates.com/daily/dzd.xml",
            "(" + bundle.getString("JEPROLAB_ARS_LABEL") + ") - " + "http://www.floatrates.com/daily/ars.xml",
            "(" + bundle.getString("JEPROLAB_AMD_LABEL") + ") - " + "http://www.floatrates.com/daily/amd.xml",
            "(" + bundle.getString("JEPROLAB_AZN_LABEL") + ") - " + "http://www.floatrates.com/daily/azn.xml",
            "(" + bundle.getString("JEPROLAB_BSD_LABEL") + ") - " + "http://www.floatrates.com/daily/bsd.xml",
            "(" + bundle.getString("JEPROLAB_BHD_LABEL") + ") - " + "http://www.floatrates.com/daily/bhd.xml",
            "(" + bundle.getString("JEPROLAB_BBD_LABEL") + ") - " + "http://www.floatrates.com/daily/bbd.xml",
            "(" + bundle.getString("JEPROLAB_BYR_LABEL") + ") - " + "http://www.floatrates.com/daily/byr.xml",
            "(" + bundle.getString("JEPROLAB_BYN_LABEL") + ") - " + "http://www.floatrates.com/daily/byn.xml",
            "(" + bundle.getString("JEPROLAB_BOB_LABEL") + ") - " + "http://www.floatrates.com/daily/bob.xml",
            "(" + bundle.getString("JEPROLAB_BWP_LABEL") + ") - " + "http://www.floatrates.com/daily/bwp.xml",
            "(" + bundle.getString("JEPROLAB_BRL_LABEL") + ") - " + "http://www.floatrates.com/daily/brl.xml",
            "(" + bundle.getString("JEPROLAB_BND_LABEL") + ") - " + "http://www.floatrates.com/daily/bnd.xml",
            "(" + bundle.getString("JEPROLAB_BGN_LABEL") + ") - " + "http://www.floatrates.com/daily/bgn.xml",
            "(" + bundle.getString("JEPROLAB_XAF_LABEL") + ") - " + "http://www.floatrates.com/daily/xaf.xml",
            "(" + bundle.getString("JEPROLAB_XPF_LABEL") + ") - " + "http://www.floatrates.com/daily/xpf.xml",
            "(" + bundle.getString("JEPROLAB_CLP_LABEL") + ") - " + "http://www.floatrates.com/daily/clp.xml",
            "(" + bundle.getString("JEPROLAB_CNY_LABEL") + ") - " + "http://www.floatrates.com/daily/cny.xml",
            "(" + bundle.getString("JEPROLAB_COP_LABEL") + ") - " + "http://www.floatrates.com/daily/cop.xml",
            "(" + bundle.getString("JEPROLAB_CRC_LABEL") + ") - " + "http://www.floatrates.com/daily/crc.xml",
            "(" + bundle.getString("JEPROLAB_HRK_LABEL") + ") - " + "http://www.floatrates.com/daily/hrk.xml",
            "(" + bundle.getString("JEPROLAB_CZK_LABEL") + ") - " + "http://www.floatrates.com/daily/czk.xml",
            "(" + bundle.getString("JEPROLAB_DKK_LABEL") + ") - " + "http://www.floatrates.com/daily/dkk.xml",
            "(" + bundle.getString("JEPROLAB_DOP_LABEL") + ") - " + "http://www.floatrates.com/daily/dop.xml",
            "(" + bundle.getString("JEPROLAB_XCD_LABEL") + ") - " + "http://www.floatrates.com/daily/xcd.xml",
            "(" + bundle.getString("JEPROLAB_EGP_LABEL") + ") - " + "http://www.floatrates.com/daily/egp.xml",
            "(" + bundle.getString("JEPROLAB_FJD_LABEL") + ") - " + "http://www.floatrates.com/daily/fjd.xml",
            "(" + bundle.getString("JEPROLAB_GHS_LABEL") + ") - " + "http://www.floatrates.com/daily/ghs.xml",
            "(" + bundle.getString("JEPROLAB_GTQ_LABEL") + ") - " + "http://www.floatrates.com/daily/gtq.xml",
            "(" + bundle.getString("JEPROLAB_HNL_LABEL") + ") - " + "http://www.floatrates.com/daily/hnl.xml",
            "(" + bundle.getString("JEPROLAB_HKD_LABEL") + ") - " + "http://www.floatrates.com/daily/hkd.xml",
            "(" + bundle.getString("JEPROLAB_HUF_LABEL") + ") - " + "http://www.floatrates.com/daily/huf.xml",
            "(" + bundle.getString("JEPROLAB_ISK_LABEL") + ") - " + "http://www.floatrates.com/daily/isk.xml",
            "(" + bundle.getString("JEPROLAB_INR_LABEL") + ") - " + "http://www.floatrates.com/daily/inr.xml",
            "(" + bundle.getString("JEPROLAB_IDR_LABEL") + ") - " + "http://www.floatrates.com/daily/idr.xml",
            "(" + bundle.getString("JEPROLAB_ILS_LABEL") + ") - " + "http://www.floatrates.com/daily/ils.xml",
            "(" + bundle.getString("JEPROLAB_JMD_LABEL") + ") - " + "http://www.floatrates.com/daily/jmd.xml",
            "(" + bundle.getString("JEPROLAB_JOD_LABEL") + ") - " + "http://www.floatrates.com/daily/jod.xml",
            "(" + bundle.getString("JEPROLAB_KZT_LABEL") + ") - " + "http://www.floatrates.com/daily/kzt.xml",
            "(" + bundle.getString("JEPROLAB_KWD_LABEL") + ") - " + "http://www.floatrates.com/daily/kwd.xml",
            "(" + bundle.getString("JEPROLAB_KGS_LABEL") + ") - " + "http://www.floatrates.com/daily/kgs.xml",
            "(" + bundle.getString("JEPROLAB_LVL_LABEL") + ") - " + "http://www.floatrates.com/daily/lvl.xml",
            "(" + bundle.getString("JEPROLAB_LBP_LABEL") + ") - " + "http://www.floatrates.com/daily/lbp.xml",
            "(" + bundle.getString("JEPROLAB_LYD_LABEL") + ") - " + "http://www.floatrates.com/daily/lyd.xml",
            "(" + bundle.getString("JEPROLAB_LTL_LABEL") + ") - " + "http://www.floatrates.com/daily/ltl.xml",
            "(" + bundle.getString("JEPROLAB_MYR_LABEL") + ") - " + "http://www.floatrates.com/daily/myr.xml",
            "(" + bundle.getString("JEPROLAB_MUR_LABEL") + ") - " + "http://www.floatrates.com/daily/mur.xml",
            "(" + bundle.getString("JEPROLAB_MXN_LABEL") + ") - " + "http://www.floatrates.com/daily/mxn.xml",
            "(" + bundle.getString("JEPROLAB_MDL_LABEL") + ") - " + "http://www.floatrates.com/daily/mdl.xml",
            "(" + bundle.getString("JEPROLAB_MAD_LABEL") + ") - " + "http://www.floatrates.com/daily/mad.xml",
            "(" + bundle.getString("JEPROLAB_MMK_LABEL") + ") - " + "http://www.floatrates.com/daily/mmk.xml",
            "(" + bundle.getString("JEPROLAB_NPR_LABEL") + ") - " + "http://www.floatrates.com/daily/npr.xml",
            "(" + bundle.getString("JEPROLAB_ANG_LABEL") + ") - " + "http://www.floatrates.com/daily/ang.xml",
            "(" + bundle.getString("JEPROLAB_TWD_LABEL") + ") - " + "http://www.floatrates.com/daily/twd.xml",
            "(" + bundle.getString("JEPROLAB_TMT_LABEL") + ") - " + "http://www.floatrates.com/daily/tmt.xml",
            "(" + bundle.getString("JEPROLAB_NZD_LABEL") + ") - " + "http://www.floatrates.com/daily/nzd.xml",
            "(" + bundle.getString("JEPROLAB_NIO_LABEL") + ") - " + "http://www.floatrates.com/daily/nio.xml",
            "(" + bundle.getString("JEPROLAB_NGN_LABEL") + ") - " + "http://www.floatrates.com/daily/ngn.xml",
            "(" + bundle.getString("JEPROLAB_NOK_LABEL") + ") - " + "http://www.floatrates.com/daily/nok.xml",
            "(" + bundle.getString("JEPROLAB_OMR_LABEL") + ") - " + "http://www.floatrates.com/daily/omr.xml",
            "(" + bundle.getString("JEPROLAB_PKR_LABEL") + ") - " + "http://www.floatrates.com/daily/pkr.xml",
            "(" + bundle.getString("JEPROLAB_PAB_LABEL") + ") - " + "http://www.floatrates.com/daily/pab.xml",
            "(" + bundle.getString("JEPROLAB_PGK_LABEL") + ") - " + "http://www.floatrates.com/daily/pgk.xml",
            "(" + bundle.getString("JEPROLAB_PYG_LABEL") + ") - " + "http://www.floatrates.com/daily/pyg.xml",
            "(" + bundle.getString("JEPROLAB_PEN_LABEL") + ") - " + "http://www.floatrates.com/daily/pen.xml",
            "(" + bundle.getString("JEPROLAB_PHP_LABEL") + ") - " + "http://www.floatrates.com/daily/php.xml",
            "(" + bundle.getString("JEPROLAB_PLN_LABEL") + ") - " + "http://www.floatrates.com/daily/pln.xml",
            "(" + bundle.getString("JEPROLAB_QAR_LABEL") + ") - " + "http://www.floatrates.com/daily/qar.xml",
            "(" + bundle.getString("JEPROLAB_RON_LABEL") + ") - " + "http://www.floatrates.com/daily/ron.xml",
            "(" + bundle.getString("JEPROLAB_RUB_LABEL") + ") - " + "http://www.floatrates.com/daily/rub.xml",
            "(" + bundle.getString("JEPROLAB_SAR_LABEL") + ") - " + "http://www.floatrates.com/daily/sar.xml",
            "(" + bundle.getString("JEPROLAB_RSD_LABEL") + ") - " + "http://www.floatrates.com/daily/rsd.xml",
            "(" + bundle.getString("JEPROLAB_SGD_LABEL") + ") - " + "http://www.floatrates.com/daily/sgd.xml",
            "(" + bundle.getString("JEPROLAB_ZAR_LABEL") + ") - " + "http://www.floatrates.com/daily/zar.xml",
            "(" + bundle.getString("JEPROLAB_KRW_LABEL") + ") - " + "http://www.floatrates.com/daily/krw.xml",
            "(" + bundle.getString("JEPROLAB_LKR_LABEL") + ") - " + "http://www.floatrates.com/daily/lkr.xml",
            "(" + bundle.getString("JEPROLAB_SEK_LABEL") + ") - " + "http://www.floatrates.com/daily/sek.xml",
            "(" + bundle.getString("JEPROLAB_TJS_LABEL") + ") - " + "http://www.floatrates.com/daily/tjs.xml",
            "(" + bundle.getString("JEPROLAB_THB_LABEL") + ") - " + "http://www.floatrates.com/daily/thb.xml",
            "(" + bundle.getString("JEPROLAB_TTD_LABEL") + ") - " + "http://www.floatrates.com/daily/ttd.xml",
            "(" + bundle.getString("JEPROLAB_TND_LABEL") + ") - " + "http://www.floatrates.com/daily/tnd.xml",
            "(" + bundle.getString("JEPROLAB_TRY_LABEL") + ") - " + "http://www.floatrates.com/daily/try.xml",
            "(" + bundle.getString("JEPROLAB_AED_LABEL") + ") - " + "http://www.floatrates.com/daily/aed.xml",
            "(" + bundle.getString("JEPROLAB_UAH_LABEL") + ") - " + "http://www.floatrates.com/daily/uah.xml",
            "(" + bundle.getString("JEPROLAB_UYU_LABEL") + ") - " + "http://www.floatrates.com/daily/uyu.xml",
            "(" + bundle.getString("JEPROLAB_UZS_LABEL") + ") - " + "http://www.floatrates.com/daily/uzs.xml",
            "(" + bundle.getString("JEPROLAB_VEF_LABEL") + ") - " + "http://www.floatrates.com/daily/vef.xml",
            "(" + bundle.getString("JEPROLAB_VND_LABEL") + ") - " + "http://www.floatrates.com/daily/vnd.xml",
            "(" + bundle.getString("JEPROLAB_XOF_LABEL") + ") - " + "http://www.floatrates.com/daily/xof.xml"
        );

        String currencyFeedUrl = JeproLabConfigurationSettings.JEPROLAB_CURRENCY_FEED_URL;
        String prefix = currencyFeedUrl.substring(currencyFeedUrl.length() - 7, currencyFeedUrl.length() - 4);
        currencyFeedUrl = "(" + bundle.getString("JEPROLAB_" + prefix.toUpperCase() + "_LABEL") + ") - " + currencyFeedUrl;
        jeproLabFileManagementCurrencyFeedUrl.setValue(currencyFeedUrl);
    }
}
