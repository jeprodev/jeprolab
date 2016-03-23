package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabCurrencyModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCurrencyAddController extends JeproLabController {
    private JeproLabCurrencyModel currency;
    @FXML
    public Label currencyNameLabel, isoCodeLabel, numericIsoCodeLabel, currencySymbolLabel, conversionRateLabel, currencyFormatLabel, hasDecimalsLabel, spacingLabel, publishedLabel, formTitleLabel;
    public TextField currencyName, isoCode, numericIsoCode, currencySymbol, conversionRate;
    public ComboBox currencyFormat;
    public JeproSwitchButton hasDecimals, hasSpacing, published;
    public GridPane jeproLabCurrencyLayout;
    public Button saveButton, cancelButton;
    public JeproFormPanel jeproLabFormWrapper;
    public JeproFormPanelTitle jeproLabFormTitleWrapper;
    public JeproFormPanelContainer jeproLabFormContainerWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double labelColumnWidth = 180;
        double inputColumnWidth = 300;
        double formWidth = (labelColumnWidth + inputColumnWidth) + 30;
        double posX = (JeproLab.APP_WIDTH/2) - (formWidth)/2;
        double posY = 25;

        jeproLabCurrencyLayout.getColumnConstraints().addAll(new ColumnConstraints(labelColumnWidth -25), new ColumnConstraints(inputColumnWidth -25));
        jeproLabCurrencyLayout.setVgap(10);
        jeproLabCurrencyLayout.setHgap(10);

        jeproLabFormWrapper.setLayoutX(posX);
        jeproLabFormWrapper.setLayoutY(posY);

        jeproLabFormTitleWrapper.setPrefSize(formWidth, 40);

        jeproLabFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabFormContainerWrapper.setLayoutY(40);

        formTitleLabel = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CURRENCY_LABEL"));
        formTitleLabel.getStyleClass().add("form-title");
        jeproLabFormTitleWrapper.getChildren().addAll(formTitleLabel);

        currencyNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        currencyNameLabel.getStyleClass().add("input-label");
        isoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_LABEL"));
        isoCodeLabel.getStyleClass().add("input-label");
        numericIsoCodeLabel.setText(bundle.getString("JEPROLAB_ISO_CODE_NUM_LABEL"));
        numericIsoCodeLabel.getStyleClass().add("input-label");
        currencySymbolLabel.setText(bundle.getString("JEPROLAB_SYMBOL_LABEL"));
        currencySymbolLabel.getStyleClass().add("input-label");
        conversionRateLabel.setText(bundle.getString("JEPROLAB_RATE_LABEL"));
        conversionRateLabel.getStyleClass().add("input-label");
        currencyFormatLabel.setText(bundle.getString("JEPROLAB_FORMAT_LABEL"));
        currencyFormatLabel.getStyleClass().add("input-label");
        hasDecimalsLabel.setText(bundle.getString("JEPROLAB_HAS_DECIMALS_LABEL"));
        hasDecimalsLabel.getStyleClass().add("input-label");
        spacingLabel.setText(bundle.getString("JEPROLAB_SPACING_LABEL"));
        spacingLabel.getStyleClass().add("input-label");
        publishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        publishedLabel.getStyleClass().add("input-label");

        /**
         * GridPane styling
         */
        GridPane.setMargin(currencyNameLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(isoCodeLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(numericIsoCodeLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(currencySymbolLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(conversionRateLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(currencyFormatLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(hasDecimalsLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(spacingLabel, new Insets(15, 0, 15, 15));
        GridPane.setMargin(publishedLabel, new Insets(15, 0, 35, 15));
        GridPane.setMargin(published, new Insets(15, 0, 35, 0));

        initializeContent();
    }

    @Override
    public void initializeContent(){
        loadObject(false);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (currency.currency_id > 0) {
            saveButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        commandWrapper.getChildren().addAll(saveButton, cancelButton);
    }

    /**
     * Load class supplier using identifier in $_GET (if possible)
     * otherwise return an empty supplier, or die
     *
     * @param option Return an empty supplier if load fail
     */
    public void loadObject(boolean option){
        int currencyId = JeproLab.request.getRequest().containsKey("currency_id") ? Integer.parseInt(JeproLab.request.getRequest().get("currency_id")) : 0;
        if (currencyId > 0) {
            if (this.currency == null) {
                this.currency = new JeproLabCurrencyModel(currencyId);
            }
            //if (JeproshopTools::isLoadedObject($this->currency, 'currency_id'))
            //return $this->currency;
            // throw exception
            //JeproLabTools.displayError(500, "The currency cannot be loaded (or not found)");
        } else if (option) {
            if (this.currency == null)
                this.currency = new JeproLabCurrencyModel();

        } else {
            this.context.controller.has_errors = true;
            this.currency = new JeproLabCurrencyModel();
            //JeproLabTools.displayError(500, "The currency cannot be loaded (the identifier is missing or invalid)");
        }
    }
}