package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.*;
import com.jeprolab.models.core.JeproLabRequest;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAnalyzeAddController extends JeproLabController {
    private boolean analyzeExistsInLab = true;
    private boolean displayCommonField = false;
    private boolean displayMultiLabCheckBoxes = false;
    private final double inputColumnWidth = 280;
    private final double labelColumnWidth = 150;
    private final double formWidth = 0.92 * JeproLab.APP_WIDTH;
    private final double posX = (JeproLab.APP_WIDTH / 2) - (formWidth) / 2;
    private final double posY = 15;
    private JeproLabAnalyzeModel analyze;
    private int defaultLanguageId;
    private Button saveAnalyzeBtn, cancelBtn;
    private Map<Integer, JeproLabLanguageModel> languages;
    public JeproLabPriceBox jeproLabAnalyzeUnitPrice;

    @FXML
    public JeproFormPanel jeproLabAddAnalyseFormWrapper;
    public JeproFormPanelTitle jeproLabAddAnalyseFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddAnalyseFormContainerWrapper;
    public JeproImageSlider jeproLabAnalyzeSlider;

    public GridPane jeproLabAnalyzeInformationLayout, jeproLabAnalyzePriceLayout, jeproLabSpecificPricePaneLayout, jeproLabAnalyzeOptionLayout;
    public Pane jeproLabAnalyzeSpecificPriceModification, jeproLabAnalyzePricePane, jeproLabSpecificPricePaneWrapper, jeproLabSpecificPricePaneTitle;
    public Pane jeproLabSpecificPricePaneContent;
    public TabPane jeproLabAnalyzeTabPane;
    public Tab jeproLabAnalyzeInformationTabForm, jeproLabAnalyzePriceTabForm, jeproLabAnalyzeAttachedFileTabForm;
    public Tab jeproLabAnalyzeSeoTabForm, jeproLabAnalyzeAssociationTabForm, jeproLabAnalyzeImageTabForm, jeproLabAnalyzeShippingTabForm;
    public Tab jeproLabAnalyzeTechnicianTabForm, jeproLabAnalyzeSpecificPriceTabForm;

    public HBox jeproLabAnalyzeSpecificPriceLabIdWrapper, jeproLabAnalyzeDelayWrapper, jeproLabSpecificPricePriorityWrapper;

    public Label jeproLabAnalyzeNameLabel, jeproLabAnalyzePublishedLabel, jeproLabAnalyzeReferenceLabel, jeproLabAnalyzeImageChooserLabel;
    public Label jeproLabAnalyzeShortDescriptionLabel, jeproLabAnalyzeDescriptionLabel, jeproLabAnalyzeImagesLabel, jeproLabAnalyzeTagLabel;
    public Label jeproLabAnalyzeSpecificPriceModificationLabel, jeproLabAnalyzeEan13Label, jeproLabAnalyzeUpcLabel, jeproLabAnalyzeRedirectLabel;
    public Label jeproLabAnalyzeVisibilityLabel, jeproLabAnalyzeOptionLabel, jeproLabAnalyzeWholeSalePriceLabel, jeproLabAnalyzePriceTaxExcludedLabel;
    public Label jeproLabAnalyzePriceTaxRuleLabel, jeproLabAnalyzePriceUseEcoTaxLabel, jeproLabAnalyzePriceTaxIncludedLabel, jeproLabAnalyzeUnitPriceLabel;
    public Label jeproLabAnalyzeFinalPriceWithoutTaxLabel, jeproLabAnalyzeSpecificPriceLabIdLabel, jeproLabAnalyzeSpecificPriceCustomerIdLabel;
    public Label jeproLabAnalyzeSpecificPriceCombinationLabel, jeproLabAnalyzeApplyDiscountOfLabel, jeproLabAnalyzeSpecificPriceFromLabel;
    public Label jeproLabAnalyzeFinalPriceWithoutTax, jeproLabAnalyzeSpecificPriceToLabel, jeproLabAnalyzeStartingAtLabel, jeproLabAnalyzeSpecificPriceLabel;
    public Label jeproLabAnalyzeDelayLabel, jeproLabDaysLabel;
    public TextField jeproLabAnalyzeReference, jeproLabAnalyzeEan13, jeproLabAnalyzeUpc, jeproLabAnalyzeStartingAt, jeproLabAnalyzeDelay;
    public ComboBox<String> jeproLabAnalyzeRedirect, jeproLabAnalyzeVisibility, jeproLabAnalyzeApplyDiscountOf;
    public JeproMultiLangTextArea jeproLabAnalyzeShortDescription, jeproLabAnalyzeDescription;
    public CheckBox jeproLabAnalyzeLeaveBasePrice, jeproLabAnalyzeOnSale, jeproLabAnalyzeShowPrice, jeproLabAnalyzeAvailableForOrder, jeproLabAnalyzeIsOnSale;
    public DatePicker jeproLabAnalyzeSpecificPriceFrom, jeproLabAnalyzeSpecificPriceTo;
    public JeproMultiLangTextField jeproLabAnalyzeName, jeproLabAnalyzeTags;
    public JeproSwitchButton jeproLabAnalyzePublished;
    public JeproImageChooser jeproLabAnalyzeImageChooser;
    public HBox jeproLabAnalyzePriceTaxRuleWrapper, jeproLabAnalyzeUnitPriceWrapper, jeproLabAnalyzeSpecificPriceCombinationWrapper;
    public HBox jeproLabAnalyzeSpecificPriceCustomerIdWrapper;
    public JeproLabPriceBox jeproLabAnalyzeWholeSalePrice, jeproLabAnalyzePriceTaxExcluded, jeproLabAnalyzePriceUseEcoTax, jeproLabAnalyzePriceTaxIncluded;
    public JeproLabPriceBox jeproLabAnalyzeSpecificPrice;

    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        Label jeproLabFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_ANALYSE_LABEL"));
        jeproLabFormTitle.getStyleClass().add("form-title");
        jeproLabFormTitle.setPrefWidth(formWidth);
        jeproLabFormTitle.setAlignment(Pos.CENTER);

        jeproLabAddAnalyseFormWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutX(.02 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutY(20);
        jeproLabAddAnalyseFormTitleWrapper.setPrefSize(0.96 * JeproLab.APP_WIDTH, 35);
        jeproLabAddAnalyseFormTitleWrapper.getChildren().add(jeproLabFormTitle);
        jeproLabAddAnalyseFormContainerWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormContainerWrapper.setLayoutY(35);

        jeproLabAnalyzeTabPane.setPrefWidth(0.96 * JeproLab.APP_WIDTH);

        setFormsLabel();
        renderInformationTab();
        renderPriceTab();
        renderSpecificPriceTab();

        context.controller = this;
    }

    private void renderInformationTab(){
        jeproLabAnalyzeInformationLayout.setLayoutX(posX);
        jeproLabAnalyzeInformationLayout.setLayoutY(posY);

        /** Setting and laying out analyze information tab **/
        jeproLabAnalyzeInformationLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth),
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth)
        );

        /*** Tab Information **/
        jeproLabAnalyzeInformationTabForm.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabAnalyzeInformationTabForm.setClosable(false);

        GridPane.setMargin(jeproLabAnalyzeNameLabel, new Insets(10, 10, 5, 15));
        GridPane.setMargin(jeproLabAnalyzeName, new Insets(15, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzePublishedLabel, new Insets(10, 10, 5, 15));
        GridPane.setMargin(jeproLabAnalyzePublished, new Insets(15, 10, 5, 0));

        GridPane.setMargin(jeproLabAnalyzeReferenceLabel, new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabAnalyzeReference, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeRedirectLabel, new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabAnalyzeRedirect, new Insets(5, 10, 5, 0));

        GridPane.setMargin(jeproLabAnalyzeUpcLabel, new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabAnalyzeUpc, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeEan13Label, new Insets(5, 10, 5, 15));
        GridPane.setMargin(jeproLabAnalyzeEan13, new Insets(5, 10, 5, 0));

        GridPane.setMargin(jeproLabAnalyzeImageChooserLabel, new Insets(10, 10, 10, 15));
        GridPane.setMargin(jeproLabAnalyzeImageChooser, new Insets(15, 10, 10, 0));
        GridPane.setMargin(jeproLabAnalyzeShortDescriptionLabel, new Insets(5, 10, 15, 15));
        GridPane.setMargin(jeproLabAnalyzeDescriptionLabel, new Insets(5, 10, 10, 15));

        GridPane.setMargin(jeproLabAnalyzeImagesLabel, new Insets(5, 10, 10, 15));
        GridPane.setMargin(jeproLabAnalyzeVisibilityLabel, new Insets(5, 10, 10, 15));
        GridPane.setMargin(jeproLabAnalyzeVisibility, new Insets(5, 10, 10, 0));
        GridPane.setMargin(jeproLabAnalyzeOptionLabel, new Insets(5, 10, 10, 15));
        GridPane.setMargin(jeproLabAnalyzeOnSale, new Insets(5, 10, 10, 0));
        GridPane.setMargin(jeproLabAnalyzeTagLabel, new Insets(5, 10, 30, 15));

        GridPane.setMargin(jeproLabAnalyzeDelayLabel, new Insets(5, 10, 30, 15));
        GridPane.setHalignment(jeproLabAnalyzeDelayLabel, HPos.RIGHT);
        GridPane.setMargin(jeproLabDaysLabel, new Insets(5, 10, 30, 15));

        jeproLabAnalyzeName.setWidth(310);
        GridPane.setMargin(jeproLabAnalyzeShortDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeShortDescription.setTextAreaPrefSize(770, 55);
        GridPane.setMargin(jeproLabAnalyzeDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeDescription.setTextAreaPrefSize(770, 85);

        GridPane.setMargin(jeproLabAnalyzeSlider, new Insets(15, 0, 10, 0));
        jeproLabAnalyzeSlider.setSliderPrefHeight(100);
        jeproLabAnalyzeSlider.setSliderPrefWidth(JeproLab.APP_WIDTH - 200);
    }

    private void renderPriceTab(){
        if (context == null) {
            context = JeproLabContext.getContext();
        }

        /** Setting and laying out Attached file form **/
        jeproLabAnalyzePriceTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzePriceLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth),
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth)
        );
    }

    private void renderSpecificPriceTab(){
        //List<JeproLabSpecificPriceModel> specificPrices = JeproLabSpecificPriceModel.getSpecificPricesByAnalyzeId(analyze.analyze_id);
        //String specificPricePriorities = JeproLabSpecificPriceModel.getPriority(analyze.analyze_id);
        Label specificPricePriorityLabel = new Label(bundle.getString("JEPROLAB_PRICE_PRIORITY_LABEL"));
        specificPricePriorityLabel.getStyleClass().add("input-label");

        ObservableList<String> priorityList = FXCollections.observableArrayList(
                bundle.getString("JEPROLAB_LABORATORY_LABEL"), bundle.getString("JEPROLAB_CURRENCY_LABEL"),
                bundle.getString("JEPROLAB_COUNTRY_LABEL"), bundle.getString("JEPROLAB_GROUP_LABEL")
        );

        boolean multiLab = JeproLabLaboratoryModel.isFeaturePublished();

        TableView<JeproLabSpecificPriceRecord> specificPriceTableView = new TableView<>();
        TableColumn rulesColumn = new TableColumn(bundle.getString("JEPROLAB_RULES_LABEL"));
        TableColumn combinationColumn = new TableColumn(bundle.getString("JEPROLAB_COMBINATION_LABEL"));
        TableColumn fromColumn = new TableColumn(bundle.getString("JEPROLAB_FROM_LABEL"));
        TableColumn currenciesColumn = new TableColumn(bundle.getString("JEPROLAB_CURRENCIES_LABEL"));
        TableColumn countriesColumn = new TableColumn(bundle.getString("JEPROLAB_COUNTRIES_LABEL"));
        TableColumn groupsColumn = new TableColumn(bundle.getString("JEPROLAB_GROUPS_LABEL"));
        TableColumn customerColumn = new TableColumn(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
        TableColumn fixedPriceColumn = new TableColumn(bundle.getString("JEPROLAB_FIXED_PRICE_LABEL"));
        TableColumn impactColumn = new TableColumn(bundle.getString("JEPROLAB_IMPACT_LABEL"));
        TableColumn periodColumn = new TableColumn(bundle.getString("JEPROLAB_PERIOD_LABEL"));
        TableColumn actionsColumn = new TableColumn(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        TableColumn labsColumn = new TableColumn(bundle.getString("JEPROLAB_LABORATORIES_LABEL"));


        ComboBox<String> priorFirst = new ComboBox<>(priorityList);
        ComboBox<String> priorSecond = new ComboBox<>(priorityList);
        ComboBox<String> priorThird = new ComboBox<>(priorityList);
        ComboBox<String> priorFourth = new ComboBox<>(priorityList);
        specificPriceTableView.setPrefSize(0.94 * JeproLab.APP_WIDTH, 250);
        specificPriceTableView.setLayoutX(0.01 * JeproLab.APP_WIDTH);

        jeproLabSpecificPricePaneLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth),
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth)
        );

        specificPriceTableView.getColumns().addAll(rulesColumn, combinationColumn, labsColumn);

        if (multiLab) {
            specificPriceTableView.getColumns().add(labsColumn);
        }
        specificPriceTableView.getColumns().addAll(currenciesColumn, countriesColumn, groupsColumn, customerColumn);
        specificPriceTableView.getColumns().addAll(fixedPriceColumn, impactColumn, periodColumn, fromColumn, actionsColumn);

        /*if(specificPrices != null & !specificPrices.isEmpty()){
            ObservableList<JeproLabSpecificPriceRecord> specificPriceRecords = FXCollections.observableArrayList();
            specificPriceRecords.addAll(specificPrices.stream().map(JeproLabSpecificPriceRecord::new).collect(Collectors.toList()));
            specificPriceTableView.getItems().addAll(specificPriceRecords);
        }*/

        GridPane.setMargin(jeproLabAnalyzeSpecificPriceLabIdLabel, new Insets(10, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceLabIdWrapper, new Insets(10, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceCustomerIdLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceCustomerIdWrapper, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceCombinationLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceCombinationWrapper, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeStartingAtLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeStartingAt, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeApplyDiscountOfLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeApplyDiscountOf, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeWholeSalePriceLabel, new Insets(10, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeWholeSalePrice, new Insets(10, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzePriceTaxExcluded, new Insets(10, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzePriceTaxRuleLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzePriceTaxRuleWrapper, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzePriceUseEcoTax, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzePriceTaxIncludedLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzePriceTaxIncluded, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceFromLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceToLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceFrom, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceTo, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeUnitPriceWrapper, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeFinalPriceWithoutTaxLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeFinalPriceWithoutTax, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeIsOnSale, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPrice, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeLeaveBasePrice, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeIsOnSale, new Insets(5, 10, 5, 0));

        jeproLabAnalyzeSpecificPriceModificationLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_LABEL"));
        jeproLabAnalyzeSpecificPriceTabForm.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_LABEL"));

        jeproLabAnalyzeSpecificPriceModification.getChildren().add(specificPriceTableView);
    }

    //private void renderTab(){}
    /*private void renderTab(){}
    private void renderTab(){}*/

    private void applyTaxToEcoTax(){
        if(analyze.eco_tax > 0){
            analyze.eco_tax = JeproLabTools.roundPrice(analyze.eco_tax * (1 + JeproLabTaxModel.getAnalyzeEcoTaxRate()/ 100), 2);
        }
    }

    private void addEventListener(){
        jeproLabAnalyzeDelay.addEventFilter(KeyEvent.KEY_TYPED , JeproLabTools.numericValidation(2));
        jeproLabAnalyzeEan13.addEventFilter(KeyEvent.KEY_TYPED, JeproLabTools.codeValidation(13));
        jeproLabAnalyzeUpc.addEventFilter(KeyEvent.KEY_TYPED, JeproLabTools.codeValidation(12));
    }

    private void setFormsLabel(){
        jeproLabAnalyzeNameLabel.setText(bundle.getString("JEPROLAB_ANALYSE_NAME_LABEL"));
        jeproLabAnalyzeNameLabel.getStyleClass().add("input-label");
        jeproLabAnalyzePublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabAnalyzePublishedLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeReferenceLabel.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabAnalyzeReferenceLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeImageChooserLabel.setText(bundle.getString("JEPROLAB_CHOOSE_IMAGE_LABEL"));
        jeproLabAnalyzeImageChooserLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeShortDescriptionLabel.setText(bundle.getString("JEPROLAB_SHORT_DESCRIPTION_LABEL"));
        GridPane.setValignment(jeproLabAnalyzeShortDescriptionLabel, VPos.TOP);
        jeproLabAnalyzeShortDescriptionLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeDescriptionLabel.setText(bundle.getString("JEPROLAB_DESCRIPTION_LABEL"));
        GridPane.setValignment(jeproLabAnalyzeDescriptionLabel, VPos.TOP);
        jeproLabAnalyzeDescriptionLabel.getStyleClass().add("input-label");
        GridPane.setValignment(jeproLabAnalyzeImagesLabel, VPos.TOP);
        jeproLabAnalyzeImagesLabel.setText(bundle.getString("JEPROLAB_IMAGES_LABEL"));
        jeproLabAnalyzeImagesLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeTagLabel.setText(bundle.getString("JEPROLAB_TAG_LABEL"));
        jeproLabAnalyzeTagLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeShowPrice.setText(bundle.getString("JEPROLAB_SHOW_PRICE_LABEL"));
        jeproLabAnalyzeShowPrice.getStyleClass().add("input-label");
        jeproLabAnalyzeOnSale.setText(bundle.getString("JEPROLAB_ON_SALE_LABEL"));
        jeproLabAnalyzeOnSale.getStyleClass().add("input-label");
        jeproLabAnalyzeEan13Label.setText(bundle.getString("JEPROLAB_EAN13_LABEL"));
        jeproLabAnalyzeEan13Label.getStyleClass().add("input-label");
        jeproLabAnalyzeUpcLabel.setText(bundle.getString("JEPROLAB_UPC_LABEL"));
        jeproLabAnalyzeUpcLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeRedirectLabel.setText(bundle.getString("JEPROLAB_REDIRECT_TO_LABEL"));
        jeproLabAnalyzeRedirectLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeDelayLabel.setText(bundle.getString("JEPROLAB_DELAYS_LABEL"));
        jeproLabAnalyzeDelayLabel.getStyleClass().add("input-label");
        jeproLabDaysLabel.setText(bundle.getString("JEPROLAB_DAYS_LABEL"));
        jeproLabDaysLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeOptionLabel.setText(bundle.getString("JEPROLAB_OPTIONS_LABEL"));
        jeproLabAnalyzeOptionLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeVisibilityLabel.setText(bundle.getString("JEPROLAB_VISIBILITY_LABEL"));
        jeproLabAnalyzeVisibilityLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeAvailableForOrder.setText(bundle.getString("JEPROLAB_AVAILABLE_FOR_ORDER_LABEL"));
        jeproLabAnalyzeAvailableForOrder.getStyleClass().add("input-label");

        jeproLabAnalyzeWholeSalePriceLabel.setText(bundle.getString("JEPROLAB_WHOLESALE_PRICE_LABEL"));
        jeproLabAnalyzeWholeSalePriceLabel.getStyleClass().add("input-label");
        jeproLabAnalyzePriceTaxExcludedLabel.setText(bundle.getString("JEPROLAB_PRICE_TAX_EXCLUDED_LABEL"));
        jeproLabAnalyzePriceTaxExcludedLabel.getStyleClass().add("input-label");
        jeproLabAnalyzePriceTaxRuleLabel.setText(bundle.getString("JEPROLAB_TAX_RULES_LABEL"));
        jeproLabAnalyzePriceTaxRuleLabel.getStyleClass().add("input-label");
        jeproLabAnalyzePriceUseEcoTaxLabel.setText(bundle.getString("JEPROLAB_ECOTAX_LABEL"));
        jeproLabAnalyzePriceUseEcoTaxLabel.getStyleClass().add("input-label");
        jeproLabAnalyzePriceTaxIncludedLabel.setText(bundle.getString("JEPROLAB_PRICE_TAX_INCLUDED_LABEL"));
        jeproLabAnalyzePriceTaxIncludedLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeUnitPriceLabel.setText(bundle.getString("JEPROLAB_UNIT_PRICE_LABEL"));
        jeproLabAnalyzeUnitPriceLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeFinalPriceWithoutTaxLabel.setText(bundle.getString("JEPROLAB_PRICE_WITHOUT_TAX_LABEL"));
        jeproLabAnalyzeFinalPriceWithoutTaxLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeIsOnSale.setText(bundle.getString("JEPROLAB_ON_SALE_LABEL"));
        jeproLabAnalyzeIsOnSale.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceModificationLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_MODIFICATION_LABEL"));
        jeproLabAnalyzeSpecificPriceModificationLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceModificationLabel.setPadding(new Insets(10, 10, 10, 10));
        jeproLabAnalyzeSpecificPriceFromLabel.setText(bundle.getString("JEPROLAB_FROM_LABEL"));
        jeproLabAnalyzeSpecificPriceFromLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceToLabel.setText(bundle.getString("JEPROLAB_TO_LABEL"));
        jeproLabAnalyzeSpecificPriceToLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeLeaveBasePrice.setText(bundle.getString("JEPROLAB_LEAVE_BASE_PRICE_LABEL"));
        jeproLabAnalyzeLeaveBasePrice.getStyleClass().add("input-label");
        jeproLabAnalyzeApplyDiscountOfLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeApplyDiscountOfLabel.getStyleClass().add("input-label");
        //todo jeproLabAnalyzeApplyDiscountOfLabel;
        jeproLabAnalyzeSpecificPriceLabIdLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeSpecificPriceLabIdLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceCustomerIdLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeSpecificPriceCustomerIdLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceCombinationLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeSpecificPriceCombinationLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeStartingAtLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeStartingAtLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeSpecificPriceLabel.getStyleClass().add("input-label");


/*
        jeproLabAnalyzeSeoTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeAssociationTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeImageTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeShippingTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeTechnicianTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL")); */
    }

    private static class JeproLabSpecificPriceRecord {
        public JeproLabSpecificPriceRecord(JeproLabSpecificPriceModel specificPrice){

        }
    }
}