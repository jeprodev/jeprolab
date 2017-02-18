package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.*;
import com.jeprolab.models.core.JeproLabRequest;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabAnalyzeAddController extends JeproLabController{
    private boolean analyze_exists_in_laboratory = true;
    private boolean display_common_field = false;
    private boolean display_multi_laboratory_check_boxes = false;
    private final double inputColumnWidth = 280;
    private final double labelColumnWidth = 150;
    private JeproLabAnalyzeModel analyze;
    private int defaultLanguageId;
    //private JeproLabRequest request;
    private Button saveAnalyzeBtn, cancelBtn;
    private Map<Integer, JeproLabLanguageModel> languages;
    private final double posX = 0.03 * JeproLab.APP_WIDTH;
    private final double posY = 15;

    @FXML
    public JeproFormPanel jeproLabAddAnalyseFormWrapper;
    public JeproFormPanelTitle jeproLabAddAnalyseFormTitleWrapper;
    public JeproFormPanelContainer jeproLabAddAnalyseFormContainerWrapper;
    public JeproImageSlider jeproLabAnalyzeSlider;

    public GridPane jeproLabAnalyzeInformationLayout, jeproLabAnalyzePriceLayout, jeproLabSpecificPricePaneLayout, jeproLabAnalyzeOptionLayout;
    public GridPane jeproLabAnalyzeMethodSelectorLayout;
    public Pane jeproLabSpecificPricePaneContent, jeproLabAnalyzePricePane, jeproLabSpecificPricePaneWrapper, jeproLabSpecificPricePaneTitle;
    public ScrollPane jeproLabAnalyzeSpecificPriceModification;
    public TabPane jeproLabAnalyzeTabPane;
    public Tab jeproLabAnalyzeInformationTabForm, jeproLabAnalyzePriceTabForm, jeproLabAnalyzeAttachedFileTabForm;
    public Tab jeproLabAnalyzeSeoTabForm, jeproLabAnalyzeAssociationTabForm, jeproLabAnalyzeImageTabForm, jeproLabAnalyzeShippingTabForm;
    public Tab jeproLabAnalyzeTechnicianTabForm, jeproLabAnalyzeSpecificPriceTabForm, jeproLabAnalyzeMethodSelectorTab;

    public HBox jeproLabAnalyzeSpecificPriceLabIdWrapper, jeproLabAnalyzeDelayWrapper, jeproLabSpecificPricePriorityWrapper;
    public HBox jeproLabAnalyzeSpecificPriceModificationCommandWrapper;

    public Label jeproLabAnalyzeNameLabel, jeproLabAnalyzePublishedLabel, jeproLabAnalyzeReferenceLabel, jeproLabAnalyzeImageChooserLabel;
    public Label jeproLabAnalyzeShortDescriptionLabel, jeproLabAnalyzeDescriptionLabel, jeproLabAnalyzeImagesLabel, jeproLabAnalyzeTagLabel;
    public Label jeproLabAnalyzeSpecificPriceModificationLabel, jeproLabAnalyzeEan13Label, jeproLabAnalyzeUpcLabel, jeproLabAnalyzeRedirectLabel;
    public Label jeproLabAnalyzeVisibilityLabel, jeproLabAnalyzeOptionLabel, jeproLabAnalyzeWholeSalePriceLabel, jeproLabAnalyzePriceTaxExcludedLabel;
    public Label jeproLabAnalyzePriceTaxRuleLabel, jeproLabAnalyzePriceUseEcoTaxLabel, jeproLabAnalyzePriceTaxIncludedLabel, jeproLabAnalyzeUnitPriceLabel;
    public Label jeproLabAnalyzeFinalPriceWithoutTaxLabel, jeproLabAnalyzeSpecificPriceLabIdLabel, jeproLabAnalyzeSpecificPriceCustomerIdLabel;
    public Label jeproLabAnalyzeSpecificPriceCombinationLabel, jeproLabAnalyzeApplyDiscountOfLabel, jeproLabAnalyzeSpecificPriceFromLabel;
    public Label jeproLabAnalyzeFinalPriceWithoutTax, jeproLabAnalyzeSpecificPriceToLabel, jeproLabAnalyzeStartingAtLabel, jeproLabAnalyzeSpecificPriceLabel;
    public Label jeproLabAnalyzeDelayLabel, jeproLabDaysLabel, jeproLabAnalyzeSpecificPricePriorityLabel, jeproLabAnalyzeMethodSelectorLabel;
    public Label jeproLabAnalyzeDelayHoursLabel, jeproLabAnalyzeApplyDiscountAfterTaxLabel;
    public TextField jeproLabAnalyzeReference, jeproLabAnalyzeEan13, jeproLabAnalyzeUpc, jeproLabAnalyzeStartingAt, jeproLabAnalyzeDelay;
    public TextField jeproLabAnalyzeDelayHours;
    public ComboBox<String> jeproLabAnalyzeRedirect, jeproLabAnalyzeVisibility, jeproLabAnalyzeApplyDiscountOf;
    public JeproMultiLangTextArea jeproLabAnalyzeShortDescription, jeproLabAnalyzeDescription;
    public CheckBox jeproLabAnalyzeLeaveBasePrice, jeproLabAnalyzeOnSale, jeproLabAnalyzeShowPrice, jeproLabAnalyzeAvailableForOrder, jeproLabAnalyzeIsOnSale;
    public DatePicker jeproLabAnalyzeSpecificPriceFrom, jeproLabAnalyzeSpecificPriceTo;
    public JeproMultiLangTextField jeproLabAnalyzeName, jeproLabAnalyzeTags;
    public JeproSwitchButton jeproLabAnalyzePublished;
    public JeproImageChooser jeproLabAnalyzeImageChooser;
    public HBox jeproLabAnalyzePriceTaxRuleWrapper, jeproLabAnalyzeUnitPriceWrapper, jeproLabAnalyzeSpecificPriceCombinationWrapper;
    public HBox jeproLabAnalyzeSpecificPriceCustomerIdWrapper, jeproLabAnalyzePriceCommandWrapper;
    public JeproPriceBox jeproLabAnalyzeWholeSalePrice, jeproLabAnalyzePriceTaxExcluded, jeproLabAnalyzePriceUseEcoTax, jeproLabAnalyzePriceTaxIncluded;
    public JeproPriceBox jeproLabAnalyzeSpecificPrice;
    public Button jeproLabAnalyzeSpecificPriceModificationCommandSaveButton, jeproLabAnalyzeSpecificPriceModificationCommandCancelButton;
    public ScrollPane jeproLabAnalyzeMethodScrollPane;
    public TableView<JeproLabAnalyzeImageRecord> jeproLabAnalyzeImageListTableView;
    public TableColumn<JeproLabAnalyzeImageRecord, ImageView> jeproLabAnalyzeImageIconTableColumn;
    public TableColumn<JeproLabAnalyzeImageRecord, String> jeproLabAnalyzeImageCaptionTableColumn, jeproLabAnalyzeImagePositionTableColumn;
    public TableColumn<JeproLabAnalyzeImageRecord, Button> jeproLabAnalyzeImageCoverTableColumn, jeproLabAnalyzeImageActionTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = 0.94 * JeproLab.APP_WIDTH;
        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_ANALYZE_LABEL"));
        formTitleLabel.setPrefWidth(formWidth);
        formTitleLabel.setAlignment(Pos.CENTER);
        formTitleLabel.getStyleClass().add("form-title");

        jeproLabAddAnalyseFormWrapper.setPrefWidth(formWidth);
        jeproLabAddAnalyseFormWrapper.setLayoutX(.03 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutY(10);

        jeproLabAddAnalyseFormTitleWrapper.setPrefSize(formWidth, 35);
        jeproLabAddAnalyseFormTitleWrapper.getChildren().add(formTitleLabel);
        jeproLabAddAnalyseFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabAddAnalyseFormContainerWrapper.setLayoutY(35);

        jeproLabAnalyzeTabPane.setPrefWidth(formWidth);

        setFormsLabel();
        renderInformationTab();
        renderPriceTab();
        renderSpecificPriceTab();

        context.controller = this;
    }

    @Override
    public void initializeContent(){
        initializeContent(0);
    }

    @Override
    public void initializeContent(int analyzeId){
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        //request = JeproLab.request;
        this.loadAnalyze(true, analyzeId);

        renderMethodTab();
        renderAssociationTab();
        renderImageTab();
        updateToolBar();
        addEventListener();

    }

    private void updateFormContent(JeproLabAnalyzeModel data){
        analyze = data;
        if(analyze.analyze_id > 0){
            formTitleLabel.setText(bundle.getString("JEPROLAB_EDIT_LABEL") + " " + bundle.getString("JEPROLAB_ANALYZE_LABEL"));
        }

        if (analyze.analyze_id > 0) {
            saveAnalyzeBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveAnalyzeBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }

        if(analyze.analyze_id > 0){
            jeproLabAnalyzePriceTabForm.setDisable(false);
            //jeproLabAnalyzeAttachedFileTabForm.setDisable(false);
            //jeproLabAnalyzeSeoTabForm.setDisable(false);
            jeproLabAnalyzeAssociationTabForm.setDisable(false);
            jeproLabAnalyzeImageTabForm.setDisable(false);
            //jeproLabAnalyzeShippingTabForm.setDisable(false);
            //jeproLabAnalyzeTechnicianTabForm.setDisable(false);
            jeproLabAnalyzeSpecificPriceTabForm.setDisable(false);
            jeproLabAnalyzeMethodSelectorTab.setDisable(false);
            updateInformationContent();
            updateMethodContent();
            updatePriceContent();
            updateSpecificPriceContent();
            updateImagesContent();
        }else {
            jeproLabAnalyzePriceTabForm.setDisable(true);
            //jeproLabAnalyzeAttachedFileTabForm.setDisable(true);
            //jeproLabAnalyzeSeoTabForm.setDisable(true);
            jeproLabAnalyzeAssociationTabForm.setDisable(true);
            jeproLabAnalyzeImageTabForm.setDisable(true);
            //jeproLabAnalyzeShippingTabForm.setDisable(true);
            //jeproLabAnalyzeTechnicianTabForm,
            jeproLabAnalyzeSpecificPriceTabForm.setDisable(true);
            jeproLabAnalyzeMethodSelectorTab.setDisable(true);
        }
    }

    private void updateInformationContent(){
        /** info data setting **/
        jeproLabAnalyzeReference.setText(analyze.reference);
        jeproLabAnalyzeUpc.setText(analyze.upc);
        jeproLabAnalyzeEan13.setText(analyze.ean13);
        jeproLabAnalyzePublished.setSelected(analyze.published);
        jeproLabAnalyzeOnSale.setSelected(analyze.on_sale);
        jeproLabAnalyzeAvailableForOrder.setSelected(analyze.available_for_order);
        jeproLabAnalyzeShowPrice.setSelected(analyze.show_price);
        jeproLabAnalyzeName.setText(analyze.name);
        jeproLabAnalyzeShortDescription.setText(analyze.short_description);
        jeproLabAnalyzeDescription.setText(analyze.description);
        //jeproLabAnalyzeSlider.setImages();
        jeproLabAnalyzeDelay.setText(String.valueOf((int)analyze.delay/24));
        analyze.tags = JeproLabTagModel.getAnalyzeTags(analyze.analyze_id);

        switch (analyze.redirect_type) {
            case "301":
                jeproLabAnalyzeRedirect.setValue(bundle.getString("JEPROLAB_301_LABEL"));
                break;
            case "302":
                jeproLabAnalyzeRedirect.setValue(bundle.getString("JEPROLAB_302_LABEL"));
                break;
            case "404":
            default:
                jeproLabAnalyzeRedirect.setValue(bundle.getString("JEPROLAB_302_LABEL"));
                break;
        }

        switch (analyze.visibility) {
            case "catalog":
                jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_CATALOG_LABEL"));
                break;
            case "both":
                jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_BOTH_LABEL"));
                break;
            case "search":
                jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_SEARCH_LABEL"));
                break;
            case "none":
                jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_NONE_LABEL"));
                break;
        }
    }

    private void updateMethodContent(){
        CheckBox methodCheckBox;
        List<JeproLabAnalyzeModel.JeproLabMethodModel>  methods = JeproLabAnalyzeModel.JeproLabMethodModel.getMethods();

        int rowIndex = 0, index = 0;
        for(JeproLabAnalyzeModel.JeproLabMethodModel method : methods){
            methodCheckBox = new CheckBox(method.name + " " + method.code);
            jeproLabAnalyzeMethodSelectorLayout.add(methodCheckBox, index % 4, rowIndex);

            GridPane.setMargin(methodCheckBox, new Insets(10, 10, 10, 10));
            index++;
            if((index % 4) == 0){
                rowIndex++;
            }

            if(analyze.analyze_id > 0 && analyze.analyzeMethods.contains(method.method_id)){
                methodCheckBox.setSelected(true);

            }

            methodCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue){
                    analyze.addMethod(method.method_id);
                    //methodCheckBox.getStyleClass().add("analyze-method");
                }else{
                    analyze.removeMethod(method.method_id);
                    //methodCheckBox.getStyleClass().remove("analyze-method");
                }
            }));
        }
    }

    private void updatePriceContent(){}

    private void updateSpecificPriceContent(){}

    private void updateImagesContent(){}

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
        GridPane.setMargin(jeproLabDaysLabel, new Insets(10, 10, 30, 15));
        GridPane.setMargin(jeproLabAnalyzeDelayHoursLabel, new Insets(10, 10, 30, 15));

        jeproLabAnalyzeName.setWidth(310);
        GridPane.setMargin(jeproLabAnalyzeShortDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeShortDescription.setTextPrefSize(770, 55);
        GridPane.setMargin(jeproLabAnalyzeDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeDescription.setTextPrefSize(770, 85);

        GridPane.setMargin(jeproLabAnalyzeSlider, new Insets(15, 0, 10, 0));
        jeproLabAnalyzeSlider.setSliderPrefHeight(100);
        jeproLabAnalyzeSlider.setSliderPrefWidth(JeproLab.APP_WIDTH - 200);

        jeproLabAnalyzeRedirect.getItems().add(bundle.getString("JEPROLAB_404_LABEL"));
        jeproLabAnalyzeRedirect.getItems().add(bundle.getString("JEPROLAB_301_LABEL"));
        jeproLabAnalyzeRedirect.getItems().add(bundle.getString("JEPROLAB_302_LABEL"));

        jeproLabAnalyzeVisibility.getItems().add(bundle.getString("JEPROLAB_BOTH_LABEL"));
        jeproLabAnalyzeVisibility.getItems().add(bundle.getString("JEPROLAB_CATALOG_LABEL"));
        jeproLabAnalyzeVisibility.getItems().add(bundle.getString("JEPROLAB_SEARCH_LABEL"));
        jeproLabAnalyzeVisibility.getItems().add(bundle.getString("JEPROLAB_NONE_LABEL"));

        jeproLabAnalyzeDelay.setPrefWidth(80);
        jeproLabAnalyzeDelayHours.setPrefWidth(80);
    }

    private void renderAssociationTab(){
        jeproLabAnalyzeAssociationTabForm.setText(bundle.getString("JEPROLAB_CATEGORIES_LABEL"));
    }

    private void renderImageTab(){
        jeproLabAnalyzeImageTabForm.setText(bundle.getString("JEPROLAB_IMAGES_LABEL"));
        jeproLabAnalyzeImageListTableView.setPrefSize(0.98 * formWidth, 520);
        VBox.setMargin(jeproLabAnalyzeImageListTableView, new Insets(10, (0.01 * formWidth), 0, (0.01 * formWidth)));

        jeproLabAnalyzeImageIconTableColumn.setText(bundle.getString("JEPROLAB_ICON_LABEL"));
        jeproLabAnalyzeImageIconTableColumn.setPrefWidth(100);
        Callback<TableColumn<JeproLabAnalyzeImageRecord, ImageView>, TableCell<JeproLabAnalyzeImageRecord, ImageView>> iconFactory = param -> new JeproLabAnalyzeImageImageViewCell();
        jeproLabAnalyzeImageIconTableColumn.setCellFactory(iconFactory);
        tableCellAlign(jeproLabAnalyzeImageIconTableColumn, Pos.CENTER);

        jeproLabAnalyzeImageCaptionTableColumn.setText(bundle.getString("JEPROLAB_CAPTION_LABEL"));
        jeproLabAnalyzeImageCaptionTableColumn.setPrefWidth((0.98 * formWidth) - 280);
        jeproLabAnalyzeImageCaptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("imageCaption"));

        jeproLabAnalyzeImagePositionTableColumn.setText(bundle.getString("JEPROLAB_POSITION_LABEL"));
        jeproLabAnalyzeImagePositionTableColumn.setPrefWidth(60);
        jeproLabAnalyzeImagePositionTableColumn.setCellValueFactory(new PropertyValueFactory<>("imagePosition"));
        tableCellAlign(jeproLabAnalyzeImagePositionTableColumn, Pos.CENTER);

        jeproLabAnalyzeImageCoverTableColumn.setText(bundle.getString("JEPROLAB_COVER_LABEL"));
        jeproLabAnalyzeImageCoverTableColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabAnalyzeImageRecord, Button>, TableCell<JeproLabAnalyzeImageRecord, Button>> isCoverFactory = param -> new JeproLabAnalyzeImageCoverCell();
        jeproLabAnalyzeImageCoverTableColumn.setCellFactory(isCoverFactory);
        tableCellAlign(jeproLabAnalyzeImageCoverTableColumn, Pos.CENTER);

        jeproLabAnalyzeImageActionTableColumn.setPrefWidth(60);
        Callback<TableColumn<JeproLabAnalyzeImageRecord, Button>, TableCell<JeproLabAnalyzeImageRecord, Button>> actionFactory = param -> new JeproLabAnalyzeImageActionCell();
        jeproLabAnalyzeImageActionTableColumn.setCellFactory(actionFactory);
        tableCellAlign(jeproLabAnalyzeImageActionTableColumn, Pos.CENTER);
    }

    private void renderMethodTab(){
        int columnWidth = (int)((0.93 * JeproLab.APP_WIDTH) / 4);

        jeproLabAnalyzeMethodScrollPane.setPrefSize(formWidth - 10, 550);
        jeproLabAnalyzeMethodSelectorLabel.setPrefSize(formWidth - 10, 30);
        jeproLabAnalyzeMethodSelectorLabel.setAlignment(Pos.CENTER);

        VBox.setMargin(jeproLabAnalyzeMethodSelectorLabel, new Insets(0, 0, 0, 5));
        VBox.setMargin(jeproLabAnalyzeMethodScrollPane, new Insets(0, 0, 0, 5));

        jeproLabAnalyzeMethodSelectorLayout.getColumnConstraints().addAll(
            new ColumnConstraints(columnWidth), new ColumnConstraints(columnWidth),
            new ColumnConstraints(columnWidth), new ColumnConstraints(columnWidth)
        );
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
        List<JeproLabPriceModel.JeproLabSpecificPriceModel> specificPrices;
        String specificPricePriorities = "";
        if(analyze != null && analyze.analyze_id > 0) {
            specificPrices = JeproLabPriceModel.JeproLabSpecificPriceModel.getSpecificPricesByAnalyzeId(analyze.analyze_id);
            specificPricePriorities = JeproLabPriceModel.JeproLabSpecificPriceModel.getPriority(analyze.analyze_id);
        }else{
            specificPrices = new ArrayList<>();

        }
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
            //labsColumn.ge
            specificPriceTableView.getColumns().add(labsColumn);
        }
        specificPriceTableView.getColumns().addAll(currenciesColumn, countriesColumn, groupsColumn, customerColumn);
        specificPriceTableView.getColumns().addAll(fixedPriceColumn, impactColumn, periodColumn, fromColumn, actionsColumn);

        if(analyze != null && analyze.analyze_id > 0) {
            if ((specificPrices != null) && !specificPrices.isEmpty()) {
                ObservableList<JeproLabSpecificPriceRecord> specificPriceRecords = FXCollections.observableArrayList();
                specificPriceRecords.addAll(specificPrices.stream().map(JeproLabSpecificPriceRecord::new).collect(Collectors.toList()));
                specificPriceTableView.getItems().addAll(specificPriceRecords);
            }
        }

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
        GridPane.setMargin(jeproLabAnalyzeSpecificPricePriorityLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeSpecificPrice, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeLeaveBasePrice, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeIsOnSale, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeSpecificPriceModificationCommandWrapper, new Insets(15, 0, 15, 0));
        GridPane.setMargin(jeproLabSpecificPricePriorityWrapper, new Insets(8, 0, 10, 0));

        jeproLabAnalyzeStartingAt.setPrefWidth(80);
        jeproLabAnalyzeStartingAt.setMaxWidth(80);
        jeproLabAnalyzeStartingAt.setMinWidth(80);

        jeproLabAnalyzeSpecificPriceModificationCommandSaveButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        jeproLabAnalyzeSpecificPriceModificationCommandSaveButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        jeproLabAnalyzeSpecificPriceModificationCommandCancelButton.setText(bundle.getString("JEPROLAB_CANCEL_LABEL"));
        jeproLabAnalyzeSpecificPriceModificationCommandCancelButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));

        jeproLabAnalyzeApplyDiscountOf.setPromptText("----");
        jeproLabAnalyzeApplyDiscountOf.getItems().addAll(bundle.getString("JEPROLAB_PERCENTAGE_LABEL"), bundle.getString("JEPROLAB_AMOUNT_LABEL"));

        jeproLabAnalyzeSpecificPriceModificationLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_LABEL"));
        jeproLabAnalyzeSpecificPriceTabForm.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_LABEL"));

        jeproLabAnalyzeSpecificPriceModification.setContent(specificPriceTableView);
        jeproLabAnalyzeSpecificPriceModification.setPrefSize(formWidth, 240);
        jeproLabAnalyzeSpecificPriceModification.setMaxSize(formWidth, 240);
        jeproLabAnalyzeSpecificPriceModification.setMinSize(formWidth, 240);
        jeproLabSpecificPricePriorityWrapper.getChildren().addAll(priorFirst, priorSecond, priorThird, priorFourth);
    }

    private void applyTaxToEcoTax(){
        if(analyze.eco_tax > 0){
            analyze.eco_tax = JeproLabTools.roundPrice(analyze.eco_tax * (1 + JeproLabTaxModel.getAnalyzeEcoTaxRate() / 100), 2);
        }
    }

    private void addEventListener(){
        jeproLabAnalyzeDelay.addEventFilter(KeyEvent.KEY_TYPED , JeproLabTools.numericValidation(2));
        jeproLabAnalyzeEan13.addEventFilter(KeyEvent.KEY_TYPED, JeproLabTools.codeValidation(13));
        jeproLabAnalyzeUpc.addEventFilter(KeyEvent.KEY_TYPED, JeproLabTools.codeValidation(12));

        saveAnalyzeBtn.setOnAction(evt -> {
            if (jeproLabAnalyzeVisibility.getValue().equals(bundle.getString("JEPROLAB_CATALOG_LABEL"))) {
                analyze.visibility = "catalog";
            } else if (jeproLabAnalyzeVisibility.getValue().equals(bundle.getString("JEPROLAB_SEARCH_LABEL"))) {
                analyze.visibility = "search";
            } else if (jeproLabAnalyzeVisibility.getValue().equals(bundle.getString("JEPROLAB_NONE_LABEL"))) {
                analyze.visibility = "none";
            } else {
                analyze.visibility = "both";
            }

            if (jeproLabAnalyzeRedirect.getValue().equals(bundle.getString("JEPROLAB_301_LABEL"))) {
                analyze.redirect_type = "301";
            } else if (jeproLabAnalyzeRedirect.getValue().equals(bundle.getString("JEPROLAB_302_LABEL"))) {
                analyze.redirect_type = "302";
            } else {
                analyze.redirect_type = "404";
            }

            analyze.reference = jeproLabAnalyzeReference.getText();
            analyze.ean13 = jeproLabAnalyzeEan13.getText();
            analyze.upc = jeproLabAnalyzeUpc.getText();
            analyze.published = jeproLabAnalyzePublished.isSelected();
            analyze.available_for_order = jeproLabAnalyzeAvailableForOrder.isSelected();
            analyze.show_price = jeproLabAnalyzeShowPrice.isSelected();
            analyze.on_sale = jeproLabAnalyzeOnSale.isSelected();
            analyze.delay = (Integer.parseInt(jeproLabAnalyzeDelay.getText()) * 24) +  Integer.parseInt(jeproLabAnalyzeDelayHours.getText());
            if(languages == null){
                languages = JeproLabLanguageModel.getLanguages();
            }

            for (Object o : languages.entrySet()) {
                Map.Entry lang = (Map.Entry) o;
                JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                analyze.name.put("lang_" + language.language_id, jeproLabAnalyzeName.getFieldContent(language.language_id));
                analyze.description.put("lang_" + language.language_id, jeproLabAnalyzeDescription.getFieldContent(language.language_id));
                analyze.short_description.put("lang_" + language.language_id, jeproLabAnalyzeShortDescription.getFieldContent(language.language_id));
                analyze.tags.put("lang_" + language.language_id, jeproLabAnalyzeTags.getFieldContent(language.language_id));
            }

            if (analyze.analyze_id > 0) {
                analyze.update();
            } else {
                analyze.save();
            }
        });

        cancelBtn.setOnAction(evt -> {
            analyze = null;
            jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_CATALOG_LABEL"));
            jeproLabAnalyzeRedirect.setValue(bundle.getString("JEPROLAB_301_LABEL"));
            jeproLabAnalyzeReference.clear();
            jeproLabAnalyzeEan13.clear();
            jeproLabAnalyzeUpc.clear();
            jeproLabAnalyzePublished.setSelected(false);
            jeproLabAnalyzeAvailableForOrder.setSelected(false);
            jeproLabAnalyzeShowPrice.setSelected(false);
            jeproLabAnalyzeOnSale.setSelected(false);
            jeproLabAnalyzeDelay.clear();
            jeproLabAnalyzeDelayHours.clear();
            jeproLabAnalyzeName.clearFields();
            jeproLabAnalyzeDescription.clearFields();
            jeproLabAnalyzeShortDescription.clearFields();
            jeproLabAnalyzeTags.clearFields();

            JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().analyzeForm);
            JeproLab.getInstance().getApplicationForms().analyzeForm.controller.initializeContent();

            //todo continue
        });
    }

    private void setFormsLabel(){
        jeproLabAnalyzeNameLabel.setText(bundle.getString("JEPROLAB_ANALYZE_NAME_LABEL"));
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
        jeproLabAnalyzeDelayHoursLabel.setText(bundle.getString("JEPROLAB_HOURS_LABEL"));
        jeproLabAnalyzeDelayHoursLabel.getStyleClass().add("input-label");
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
        GridPane.setValignment(jeproLabAnalyzeApplyDiscountOfLabel, VPos.TOP);
        jeproLabAnalyzeSpecificPricePriorityLabel.setText(bundle.getString("JEPROLAB_PRIORITY_LABEL"));
        jeproLabAnalyzeSpecificPricePriorityLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeApplyDiscountAfterTaxLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_AFTER_TAX_LABEL"));
        jeproLabAnalyzeSpecificPriceLabIdLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_LABORATORY_ID_LABEL"));
        jeproLabAnalyzeSpecificPriceLabIdLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceCustomerIdLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_CUSTOMER_ID_LABEL"));
        jeproLabAnalyzeSpecificPriceCustomerIdLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceCombinationLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_COMBINATION_LABEL"));
        jeproLabAnalyzeSpecificPriceCombinationLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeStartingAtLabel.setText(bundle.getString("JEPROLAB_STARTING_AT_LABEL"));
        jeproLabAnalyzeStartingAtLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeSpecificPriceLabel.setText(bundle.getString("JEPROLAB_APPLY_DISCOUNT_OF_LABEL"));
        jeproLabAnalyzeSpecificPriceLabel.getStyleClass().add("input-label");


/*
        jeproLabAnalyzeSeoTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeAssociationTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeImageTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeShippingTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeTechnicianTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL")); */

        /**
         * Method form labels
         */
        jeproLabAnalyzeMethodSelectorTab.setText(bundle.getString("JEPROLAB_METHOD_LABEL"));
        jeproLabAnalyzeMethodSelectorLabel.setText(bundle.getString("JEPROLAB_SELECT_METHODS_RELATED_TO_THE_ANALYZE_LABEL"));
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveAnalyzeBtn = new Button(bundle.getString("JEPROLAB_UPDATE_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));

        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));

        commandWrapper.getChildren().addAll(saveAnalyzeBtn, cancelBtn);
    }

    private void loadAnalyze(boolean option, int analyzeId) {
        Worker<Boolean> worker = new Task<Boolean>(){
            JeproLabAnalyzeModel data = null;
            @Override
            protected Boolean call() throws Exception{
                boolean isLoaded = false;
                if (analyzeId > 0) {
                    if (data == null) {
                        data = new JeproLabAnalyzeModel(analyzeId);
                    }

                    if (data.analyze_id <= 0) {
                        JeproLabTools.displayError(500, bundle.getString("JEPROLAB_ANALYZE_NOT_FOUND_MESSAGE"));
                        isLoaded = false;
                    } else {
                        isLoaded = true;
                    }
                } else if (option) {
                    if (data == null) {
                        data = new JeproLabAnalyzeModel();
                    }
                } else {
                    JeproLabTools.displayError(500, bundle.getString("JEPROSHOP_ANALYZE_DOES_NOT_EXIST_MESSAGE"));
                    isLoaded = false;
                }

                //specified
                if (isLoaded && data.analyze_id > 0) {
                    if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && JeproLabLaboratoryModel.isFeaturePublished() && !data.isAssociatedToLaboratory()) {
                        data = new JeproLabAnalyzeModel(data.analyze_id, false, 0, data.default_laboratory_id);
                    }
                    data.loadStockData();
                }
                return isLoaded;
            }

            @Override
            protected void failed(){
                super.failed();
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, exceptionProperty().getValue());
            }

            @Override
            protected void succeeded(){
                super.failed();
                updateFormContent(data);
            }

            @Override
            protected void cancelled(){
                super.failed();
            }
        };

        new Thread((Task)worker).start();
    }

    private static class JeproLabSpecificPriceRecord {
        private SimpleStringProperty ruleColumn, impactColumn, attributeNameColumn;
        private SimpleStringProperty periodColumn;
        private boolean can_delete_specific_prices = true;
        private static List<JeproLabCurrencyModel> currencies;
        //private SimpleObjectProperty<HBox> actionColumn;

        public JeproLabSpecificPriceRecord(JeproLabPriceModel.JeproLabSpecificPriceModel specificPrice){
            JeproLabPriceModel.JeproLabSpecificPriceRuleModel specificPriceRule = new JeproLabPriceModel.JeproLabSpecificPriceRuleModel(specificPrice.specific_price_id);
            String ruleName = (specificPriceRule.specific_price_rule_id > 0 ? specificPriceRule.name : "--");
            ruleColumn= new SimpleStringProperty(ruleName);

            JeproLabCurrencyModel currentCurrency = new JeproLabCurrencyModel();
            for(JeproLabCurrencyModel currency : currencies){
                if(currency.currency_id == specificPrice.currency_id){
                    currentCurrency = currency;
                }
            }

            if(specificPrice.reduction_type.equals("percentage")){
                impactColumn = new SimpleStringProperty("- " + specificPrice.reduction * 100 + " %");
            }else if(specificPrice.reduction > 0){
                impactColumn = new SimpleStringProperty("- " + JeproLabTools.displayPrice(JeproLabTools.roundPrice(specificPrice.reduction, 2), currentCurrency));
            }else{
                impactColumn = new SimpleStringProperty("--");
            }

            if(specificPrice.from.toString().equals("0000-00-00 00:00:00") && specificPrice.to.toString().equals("0000-00-00 00:00:00")){
                periodColumn = new SimpleStringProperty(JeproLab.getBundle().getString("JEPROLAB_UNLIMITED_LABEL"));
            }else{
                periodColumn = new SimpleStringProperty(
                    JeproLab.getBundle().getString("JEPROLAB_FROM_LABEL") + " " + (!specificPrice.from.toString().equals("0000-00-00 00:00:00") ? specificPrice.from.toString() : "0000-00-00 00:00:00" ) + "\n"
                        + JeproLab.getBundle().getString("JEPROLAB_TO_LABEL") + " " + (!specificPrice.to.toString().equals("0000-00-00 00:00:00") ? specificPrice.to.toString() : "0000-00-00 00:00:00" )
                );
            }

            if(specificPrice.analyze_attribute_id > 0){
                JeproLabCombinationModel combination = new JeproLabCombinationModel(specificPrice.analyze_attribute_id);
                List<JeproLabAttributeModel> attributes = combination.getAttributesName(JeproLabContext.getContext().language.language_id);
                String attributeName = "";
                for(JeproLabAttributeModel attribute : attributes){
                    attributeName += attribute.name.get("lang_" + JeproLabContext.getContext().language.language_id) + " - ";
                }
                attributeName = attributeName.endsWith(" - ") ? attributeName.substring(0, attributeName.length() - 3) : attributeName;
                attributeNameColumn = new SimpleStringProperty(attributeName);
            }else{
                attributeNameColumn = new SimpleStringProperty(JeproLab.getBundle().getString("JEPROLAB_ALL_COMBINATION_LABEL"));
            }

            if(specificPrice.customer_id > 0){
                JeproLabCustomerModel customer = new JeproLabCustomerModel(specificPrice.customer_id);
                String customerFullName = customer.firstname + " " + customer.lastname;
            }

            //if(specificPrice.laboratory_id <= 0 || JeproLabLaboratoryModel.getContextListLaboratoryIds().contains(specificPrice.laboratory_id))
            if(JeproLabLaboratoryModel.isFeaturePublished()){
                can_delete_specific_prices = ((JeproLabContext.getContext().employee.getAssociatedLaboratories().size() > 1 && specificPrice.laboratory_id <= 0) || specificPrice.laboratory_id > 0);
            }
        }

        public String getRuleColumn(){
            return ruleColumn.get();
        }

        public static void setCurrencies(List<JeproLabCurrencyModel> currencyList){
            currencies = currencyList;
        }
    }

    public static class JeproLabAnalyzeImageRecord{
        private SimpleStringProperty imageCaption;
        private SimpleIntegerProperty imageIndex, imagePosition;
        private SimpleBooleanProperty imageIsCover;

        public JeproLabAnalyzeImageRecord(JeproLabImageModel image){
            imageIndex = new SimpleIntegerProperty(image.image_id);
            imagePosition = new SimpleIntegerProperty(image.position);
            imageCaption = new SimpleStringProperty(image.legend.get("lang_" + JeproLabContext.getContext().language.language_id));
            imageIsCover = new SimpleBooleanProperty(image.cover);
        }

        public boolean isCoverImage(){
            return imageIsCover.get();
        }

        public int getImageIndex(){
            return imageIndex.get();
        }

        public String getImagePosition(){
            return String.valueOf(imagePosition.get());
        }

        public String getImageCaption(){
            return imageCaption.get();
        }
    }

    public static class JeproLabAnalyzeImageImageViewCell extends TableCell<JeproLabAnalyzeImageRecord, ImageView>{
        public JeproLabAnalyzeImageImageViewCell(){

        }

        public void updateItem(ImageView item, boolean empty){
            super.updateItem(item, empty);
        }
    }

    public static class JeproLabAnalyzeImageCoverCell extends TableCell<JeproLabAnalyzeImageRecord, Button>{
        private HBox commandWrapper;
        private Button switchCoverStatus;

        public JeproLabAnalyzeImageCoverCell(){
            commandWrapper = new HBox();
            switchCoverStatus = new Button("");
            commandWrapper.getChildren().add(switchCoverStatus);
        }

        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabAnalyzeImageRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getImageIndex();
                switchCoverStatus.setOnAction(event -> {
                    if(items.get(getIndex()).isCoverImage()) {
                        switchCoverStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/published.png"))));
                    }else{
                        switchCoverStatus.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
                    }
                });
                setAlignment(Pos.CENTER);
                setGraphic(commandWrapper);
            }
        }
    }

    public static class JeproLabAnalyzeImageActionCell extends TableCell<JeproLabAnalyzeImageRecord, Button> {
        private Button deleteImage;
        private HBox commandWrapper;

        public JeproLabAnalyzeImageActionCell(){
            deleteImage = new Button("");
            deleteImage.getStyleClass().add("trash-btn");
            commandWrapper = new HBox(0);
        }

        public void updateItem(Button item, boolean empty){
            super.updateItem(item, empty);
            ObservableList<JeproLabAnalyzeImageRecord> items = getTableView().getItems();
            if((items != null) && (getIndex() >= 0 && getIndex() < items.size())){
                int itemId = items.get(getIndex()).getImageIndex();
                deleteImage.setOnAction(event -> {
                    JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addAddressForm);
                    JeproLab.getInstance().getApplicationForms().addAddressForm.controller.initializeContent(itemId);

                });
                setGraphic(commandWrapper);
                setAlignment(Pos.CENTER);
            }
        }
    }
}
