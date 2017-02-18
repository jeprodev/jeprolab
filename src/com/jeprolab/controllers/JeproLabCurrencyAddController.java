package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.models.JeproLabCurrencyModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCurrencyAddController extends JeproLabController{
    private JeproLabCurrencyModel currency;
    private Button saveCurrencyBtn;
    @FXML
    public Label jeproLabCurrencyNameLabel, jeproLabCurrencyIsoCodeLabel, jeproLabCurrencyNumericIsoCodeLabel;
    public Label jeproLabCurrencySymbolLabel, jeproLabCurrencyConversionRateLabel, jeproLabCurrencyFormatLabel;
    public Label jeproLabCurrencyHasDecimalsLabel, jeproLabCurrencyHasSpacingLabel, jeproLabCurrencyPublishedLabel;
    public TextField jeproLabCurrencyName, jeproLabCurrencyIsoCode, jeproLabCurrencyNumericIsoCode, jeproLabCurrencySymbol;
    public TextField jeproLabCurrencyConversionRate;
    public ComboBox<String> jeproLabCurrencyFormat;
    public JeproSwitchButton jeproLabCurrencyPublished, jeproLabCurrencyHasSpacing, jeproLabCurrencyHasDecimals;
    public GridPane jeproLabCurrencyLayout;
    public JeproFormPanel jeproLabFormWrapper;
    public JeproFormPanelTitle jeproLabFormTitleWrapper;
    public JeproFormPanelContainer jeproLabFormContainerWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 500;

        jeproLabFormWrapper.setLayoutX((JeproLab.APP_WIDTH - formWidth)/2);
        jeproLabFormWrapper.setLayoutY(40);

        jeproLabFormTitleWrapper.setPrefSize(formWidth, 40);
        formTitleLabel.setPrefSize(formWidth, 40);
        jeproLabFormTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabFormContainerWrapper.setLayoutY(40);


        jeproLabCurrencyLayout.getColumnConstraints().addAll(
            new ColumnConstraints(150), new ColumnConstraints(250)
        );

        GridPane.setMargin(jeproLabCurrencyNameLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyName, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyIsoCodeLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyIsoCode, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyNumericIsoCodeLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyNumericIsoCode, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencySymbolLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencySymbol, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyConversionRateLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyConversionRate, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyFormatLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyFormat, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyHasDecimalsLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyHasDecimals, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyHasSpacingLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyHasSpacing, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabCurrencyPublishedLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabCurrencyPublished, new Insets(10, 0, 5, 0));

        jeproLabCurrencyNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabCurrencyIsoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        jeproLabCurrencyNumericIsoCodeLabel.setText(bundle.getString("JEPROLAB_NUMERIC_ISO_CODE_LABEL"));
        jeproLabCurrencySymbolLabel.setText(bundle.getString("JEPROLAB_CURRENCY_SYMBOL_LABEL"));
        jeproLabCurrencyConversionRateLabel.setText(bundle.getString("JEPROLAB_CONVERSION_RATE_LABEL"));
        jeproLabCurrencyFormatLabel.setText(bundle.getString("JEPROLAB_CURRENCY_FORMAT_LABEL"));
        jeproLabCurrencyHasDecimalsLabel.setText(bundle.getString("JEPROLAB_CURRENCY_HAS_DECIMALS_LABEL"));
        jeproLabCurrencyHasSpacingLabel.setText(bundle.getString("JEPROLAB_CURRENCY_HAS_SPACING_LABEL"));
        jeproLabCurrencyPublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
    }

    @Override
    public void initializeContent(){
        initializeContent(0);
    }

    @Override
    public void initializeContent(int currencyId){
        loadCurrency(currencyId);

        if(currency.currency_id > 0){
            formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));
            jeproLabCurrencyName.setText(currency.name);
            jeproLabCurrencyIsoCode.setText(currency.iso_code);
            jeproLabCurrencyNumericIsoCode.setText(String.valueOf(currency.iso_code_num));
            jeproLabCurrencySymbol.setText(currency.sign);
            jeproLabCurrencyConversionRate.setText(String.valueOf(currency.conversion_rate));
            jeproLabCurrencyFormat.setValue(String.valueOf(currency.format));
            jeproLabCurrencyHasDecimals.setSelected(currency.decimals > 0);
            jeproLabCurrencyHasSpacing.setSelected(currency.blank);
            jeproLabCurrencyPublished.setSelected(currency.published);
        }else{
            formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));
            jeproLabCurrencyName.setText("");
            jeproLabCurrencyIsoCode.setText("");
            jeproLabCurrencyNumericIsoCode.setText("");
            jeproLabCurrencySymbol.setText("");
            jeproLabCurrencyConversionRate.setText("");
            jeproLabCurrencyFormat.setValue("");
            jeproLabCurrencyHasDecimals.setSelected(false);
            jeproLabCurrencyHasSpacing.setSelected(false);
            jeproLabCurrencyPublished.setSelected(false);
        }
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveCurrencyBtn = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if(currency.currency_id > 0){ saveCurrencyBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL")); }
        commandWrapper.getChildren().addAll(saveCurrencyBtn);
        addCommandListener();
    }

    private void addCommandListener(){
        saveCurrencyBtn.setOnAction(event -> {
            currency.name = jeproLabCurrencyName.getText();
            currency.iso_code = jeproLabCurrencyIsoCode.getText();
            currency.iso_code_num = jeproLabCurrencyNumericIsoCode.getText();
            currency.sign = jeproLabCurrencySymbol.getText();
            currency.conversion_rate = Float.parseFloat(jeproLabCurrencyConversionRate.getText());
            //currency.format = jeproLabCurrency;
            currency.decimals = jeproLabCurrencyHasDecimals.isSelected() ? 1 : 0;
            currency.blank = jeproLabCurrencyHasSpacing.isSelected();
            currency.published = jeproLabCurrencyPublished.isSelected();

            if(currency.currency_id > 0){
                currency.update();
            }else{
                currency.save();
            }
        });
    }


    private void loadCurrency(int currencyId){
        if(currencyId > 0){
            if(currency != null && currency.currency_id != currencyId){
                currency = new JeproLabCurrencyModel(currencyId);
            }else{
                currency = new JeproLabCurrencyModel(currencyId);
            }
        }else{
            currency = new JeproLabCurrencyModel();
        }
    }
}
