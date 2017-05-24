package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.apache.log4j.Level;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 *
 * Created by jeprodev on 11/04/2017.
 */
public class JeproLabCarrierAddController extends JeproLabController{
    private static final int NUMBER_OF_ITEMS = 8;
    private Button saveCarrierButton, cancelCarrierButton;
    private Image jeproLabCarrierLogoImage;
    private TableView<JeproLabCountryZoneController.JeproLabZoneRecord> jeproLabAssociatedZonesListView;
    private ObservableList<JeproLabCountryZoneController.JeproLabZoneRecord> jeproLabCarrierZonesList;
    private TableView<JeproLabGroupController.JeproLabGroupRecord> jeproLabAssociatedGroupListView;
    private ObservableList<JeproLabGroupController.JeproLabGroupRecord> jeproLabAssociatedGroupList;
    private ToggleGroup jeproLabShippingMethodToggleGroup;
    private Map<Integer, String> taxData = new ConcurrentHashMap<>();
    private JeproLabCarrierModel carrier;
    private Label formLabel;
    private List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxRulesGroups;

    @FXML
    public JeproFormPanel jeproLabCarrierAddFormWrapper;
    public JeproFormPanelTitle jeproLabCarrierAddFormTitle;
    public JeproFormPanelContainer jeproLabCarrierAddFormContainer;
    public Label jeproLabCarrierNameLabel, jeproLabCarrierDeliveryDelayLabel, jeproLabCarrierSpeedGradeLabel;
    public Label jeproLabCarrierCarrierLogoLabel, jeproLabCarrierTrackingUrlLabel, jeproLabCarrierPublishedLabel;
    public Label jeproLabCarrierAddHandlingCostLabel, jeproLabCarrierFreeShippingLabel, jeproLabCarrierShippingMethodLabel;
    public Label jeproLabCarrierOutOfRangeBehaviorLabel, jeproLabCarrierTaxLabel, jeproLabCarrierSupportedZonesLabel;
    public Label jeproLabCarrierMaximumWidthLabel, jeproLabCarrierMaximumHeightLabel, jeproLabCarrierMaximumDepthLabel;
    public Label jeproLabCarrierMaximumWeightLabel, jeproLabCarrierAssociatedGroupsLabel, jeproLabCarrierReferenceLabel;
    public Label jeproLabCarrierDeletedLabel, jeproLabCarrierIsModuleLabel, jeproLabCarrierNeedRangeLabel;
    public Label jeproLabCarrierExternalModuleNameLabel, jeproLabCarrierShippingExternalLabel, jeproLabCarrierDeliveryDelayPeriodLabel;
    public JeproAppendButton jeproLabCarrierMaximumWidth, jeproLabCarrierMaximumHeight, jeproLabCarrierMaximumDepth, jeproLabCarrierMaximumWeight;
    public Pane jeproLabCarrierAssociatedGroupWrapper, jeproLabCarrierSupportedZonesWrapper;
    public TextField jeproLabCarrierName, jeproLabCarrierReference, jeproLabCarrierSpeedGrade, jeproLabCarrierTrackingUrl;
    public TextField jeproLabCarrierExternalModuleName, jeproLabCarrierDeliveryDelay, jeproLabCarrierPosition;
    public JeproImageChooser jeproLabCarrierCarrierLogo;
    public ComboBox<String> jeproLabCarrierDeliveryDelayPeriod;
    public ComboBox<String> jeproLabCarrierTax, jeproLabCarrierOutOfRangeBehavior, jeproLabCarrierShippingMethod;
    public ImageView jeproLabCarrierLogoImageView;
    public TabPane jeproLabCarrierAddTabPane;
    public Tab jeproLabAddCarrierInformationTab, jeproLabCarrierSizeSettingTab, jeproLabCarrierCostTab;
    public GridPane jeproLabCarrierInformationFormLayout, jeproLabCarrierAddCostFormLayout, jeproLabCarrierSizeSettingFormLayout;
    public JeproSwitchButton jeproLabCarrierPublished, jeproLabCarrierAddHandlingCost, jeproLabCarrierFreeShipping;
    public JeproSwitchButton jeproLabCarrierDeleted, jeproLabCarrierIsModule, jeproLabCarrierNeedRange, jeproLabCarrierShippingExternal;
    public HBox jeproLabCarrierDeliveryDelayWrapper, jeproLabCarrierShippingMethodWrapper;
    //public ToggleButton jeproLabCarrierShippingMethodAccordingToTotalPrice, jeproLabCarrierShippingMethodAccordingToTotalWeight;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 0.98 * JeproLab.APP_WIDTH;

        jeproLabCarrierAddFormWrapper.setPrefWidth(formWidth);
        jeproLabCarrierAddFormWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabCarrierAddFormWrapper.setLayoutY(40);
        jeproLabCarrierAddFormTitle.setPrefSize(formWidth, 40);
        formLabel = new Label(bundle.getString("JEPROLAB_ADD_NEW_LABEL") + " " + bundle.getString("JEPROLAB_CARRIER_LABEL"));
        formLabel.getStyleClass().addAll("form-title");
        formLabel.setPrefSize(formWidth, 38);
        jeproLabCarrierAddFormTitle.getChildren().addAll(formLabel);
        jeproLabCarrierAddFormContainer.setPrefWidth(formWidth);
        jeproLabCarrierAddFormContainer.setLayoutY(40);

        jeproLabCarrierAddTabPane.setPrefWidth(formWidth - 150);

        jeproLabAddCarrierInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabCarrierSizeSettingTab.setText(bundle.getString("JEPROLAB_SIZE_SETTINGS_LABEL"));
        jeproLabCarrierReferenceLabel.setText(bundle.getString("JEPROLAB_REFERENCE_LABEL"));
        jeproLabCarrierDeletedLabel.setText(bundle.getString("JEPROLAB_DELETED_LABEL"));
        jeproLabCarrierCostTab.setText(bundle.getString("JEPROLAB_COST_SETTINGS_LABEL"));
        jeproLabCarrierNameLabel.setText(bundle.getString("JEPROLAB_CARRIER_NAME_LABEL"));
        jeproLabCarrierDeliveryDelayLabel.setText(bundle.getString("JEPROLAB_CARRIER_DELIVERY_DELAY_LABEL"));
        jeproLabCarrierSpeedGradeLabel.setText(bundle.getString("JEPROLAB_SPEED_GRADE_LABEL"));
        jeproLabCarrierCarrierLogoLabel.setText(bundle.getString("JEPROLAB_CARRIER_LOGO_LABEL"));
        jeproLabCarrierTrackingUrlLabel.setText(bundle.getString("JEPROLAB_TRACKING_URL_LABEL"));
        jeproLabCarrierPublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
        jeproLabCarrierAddHandlingCostLabel.setText(bundle.getString("JEPROLAB_ADD_HANDLING_COST_LABEL"));
        jeproLabCarrierFreeShippingLabel.setText(bundle.getString("JEPROLAB_FREE_SHIPPING_LABEL"));
        jeproLabCarrierShippingMethodLabel.setText(bundle.getString("JEPROLAB_SHIPPING_METHOD_LABEL"));
        jeproLabCarrierOutOfRangeBehaviorLabel.setText(bundle.getString("JEPROLAB_OUT_OF_RANGE_BEHAVIOR_LABEL"));
        jeproLabCarrierTaxLabel.setText(bundle.getString("JEPROLAB_TAX_LABEL"));
        jeproLabCarrierSupportedZonesLabel.setText(bundle.getString("JEPROLAB_SUPPORTED_ZONES_LABEL"));
        jeproLabCarrierMaximumWidthLabel.setText(bundle.getString("JEPROLAB_MAXIMUM_WIDTH_LABEL"));
        jeproLabCarrierMaximumHeightLabel.setText(bundle.getString("JEPROLAB_MAXIMUM_HEIGHT_LABEL"));
        jeproLabCarrierMaximumDepthLabel.setText(bundle.getString("JEPROLAB_MAXIMUM_DEPTH_LABEL"));
        jeproLabCarrierMaximumWeightLabel.setText(bundle.getString("JEPROLAB_MAXIMUM_WEIGHT_LABEL"));
        jeproLabCarrierAssociatedGroupsLabel.setText(bundle.getString("JEPROLAB_ASSOCIATED_GROUPS_LABEL"));
        jeproLabCarrierExternalModuleNameLabel.setText(bundle.getString("JEPROLAB_EXTERNAL_SHIPPING_MODULE_NAME_LABEL"));
        jeproLabCarrierShippingExternalLabel.setText(bundle.getString("JEPROLAB_EXTERNAL_SHIPPING_LABEL"));
        jeproLabCarrierNeedRangeLabel.setText(bundle.getString("JEPROLAB_NEED_RANGE_LABEL"));
        jeproLabCarrierIsModuleLabel.setText(bundle.getString("JEPROLAB_IS_MODULE_LABEL"));
        jeproLabCarrierDeliveryDelayPeriod.setPromptText(bundle.getString("JEPROLAB_SELECT_PERIOD_LABEL"));
        jeproLabCarrierDeliveryDelayPeriod.setPrefWidth(120);
        jeproLabCarrierDeliveryDelayPeriod.getItems().addAll(
            bundle.getString("JEPROLAB_MINUTES_LABEL"), bundle.getString("JEPROLAB_HOURS_LABEL"),
            bundle.getString("JEPROLAB_DAYS_LABEL"), bundle.getString("JEPROLAB_WEEKS_LABEL"),
            bundle.getString("JEPROLAB_MONTHS_LABEL"), bundle.getString("JEPROLAB_YEARS_LABEL")
        );
        jeproLabCarrierDeliveryDelay.setPrefWidth(60);

        double inputColumnConstrains = formWidth - 340;
        jeproLabCarrierInformationFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(170), new ColumnConstraints(inputColumnConstrains)
        );
        jeproLabCarrierAddCostFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(170), new ColumnConstraints(inputColumnConstrains)
        );
        jeproLabCarrierSizeSettingFormLayout.getColumnConstraints().addAll(
            new ColumnConstraints(170), new ColumnConstraints(inputColumnConstrains)
        );

        jeproLabCarrierTax.setPrefWidth(180);
        jeproLabCarrierTax.setPromptText(bundle.getString("JEPROLAB_SELECT_TAX_LABEL"));

        jeproLabCarrierShippingMethod.getItems().addAll(
            bundle.getString("JEPROLAB_SHIPPING_METHOD_DEFAULT_LABEL"),
            bundle.getString("JEPROLAB_SHIPPING_METHOD_FREE_LABEL"),
            bundle.getString("JEPROLAB_SHIPPING_METHOD_PRICE_LABEL"),
            bundle.getString("JEPROLAB_SHIPPING_METHOD_WEIGHT_LABEL")
        );
        jeproLabCarrierShippingMethod.setValue(bundle.getString("JEPROLAB_SHIPPING_METHOD_DEFAULT_LABEL"));

        Insets labelInsets = new Insets(5, 10, 5, 20);
        Insets inputInsets = new Insets(5, 10, 5, 0);

        GridPane.setMargin(jeproLabCarrierNameLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierName, inputInsets);
        GridPane.setMargin(jeproLabCarrierDeliveryDelayLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierDeliveryDelay, inputInsets);
        GridPane.setMargin(jeproLabCarrierSpeedGradeLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierSpeedGrade, inputInsets);
        GridPane.setMargin(jeproLabCarrierCarrierLogoLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierCarrierLogo, inputInsets);
        GridPane.setMargin(jeproLabCarrierTrackingUrlLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierTrackingUrl, inputInsets);
        GridPane.setMargin(jeproLabCarrierPublishedLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierPublished, inputInsets);
        GridPane.setMargin(jeproLabCarrierAddHandlingCostLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierAddHandlingCost, inputInsets);
        GridPane.setMargin(jeproLabCarrierFreeShippingLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierFreeShipping, inputInsets);
        GridPane.setMargin(jeproLabCarrierShippingMethodLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierShippingMethod, inputInsets);
        GridPane.setMargin(jeproLabCarrierOutOfRangeBehaviorLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierOutOfRangeBehavior, inputInsets);
        GridPane.setMargin(jeproLabCarrierTaxLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierTax, inputInsets);
        GridPane.setMargin(jeproLabCarrierSupportedZonesLabel, new Insets(10, 10, 5, 20));
        GridPane.setMargin(jeproLabCarrierSupportedZonesWrapper, new Insets(10, 10, 5, 0));
        GridPane.setMargin(jeproLabCarrierMaximumWidthLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierMaximumWidth, inputInsets);
        GridPane.setMargin(jeproLabCarrierMaximumHeightLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierMaximumHeight, inputInsets);
        GridPane.setMargin(jeproLabCarrierMaximumDepthLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierMaximumDepth, inputInsets);
        GridPane.setMargin(jeproLabCarrierMaximumWeightLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierMaximumWeight, inputInsets);
        GridPane.setMargin(jeproLabCarrierAssociatedGroupsLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierAssociatedGroupWrapper, inputInsets);
        GridPane.setMargin(jeproLabCarrierReferenceLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierReference, inputInsets);
        GridPane.setMargin(jeproLabCarrierDeletedLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierDeleted, inputInsets);
        GridPane.setMargin(jeproLabCarrierExternalModuleNameLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierShippingExternalLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierNeedRangeLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierIsModuleLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierDeliveryDelayWrapper, inputInsets);

        GridPane.setValignment(jeproLabCarrierSupportedZonesLabel, VPos.TOP);
        GridPane.setValignment(jeproLabCarrierAssociatedGroupsLabel, VPos.TOP);

        jeproLabCarrierLogoImageView.setFitWidth(140);
        jeproLabCarrierLogoImageView.setFitHeight(140);

        HBox.setMargin(jeproLabCarrierLogoImageView, new Insets(25, 5, 5, 5));

        /*jeproLabShippingMethodToggleGroup = new ToggleGroup();
        jeproLabCarrierShippingMethodAccordingToTotalPrice.setText(bundle.getString("JEPROLAB_ACCORDING_TO_TOTAL_PRICE_LABEL"));
        jeproLabCarrierShippingMethodAccordingToTotalWeight.setText(bundle.getString("JEPROLAB_ACCORDING_TO_TOTAL_WEIGHT_LABEL"));
        jeproLabCarrierShippingMethodAccordingToTotalPrice.setToggleGroup(jeproLabShippingMethodToggleGroup);
        jeproLabCarrierShippingMethodAccordingToTotalWeight.setToggleGroup(jeproLabShippingMethodToggleGroup);
*/
        jeproLabCarrierOutOfRangeBehavior.setPromptText(bundle.getString("JEPROLAB_SELECT_OUT_OF_RANGE_BEHAVIOR_LABEL"));
        jeproLabCarrierOutOfRangeBehavior.getItems().addAll(
            bundle.getString("JEPROLAB_TAKE_THE_UPPER_RANGE_LABEL"),
            bundle.getString("JEPROLAB_DEACTIVATE_CARRIER_LABEL")
        );
        renderAssociatedZonesTableView();
        renderAssociatedGroupsTableView();
    }

    @Override
    public void initializeContent(){
        initializeContent(0);
    }

    @Override
    public void initializeContent(int carrierId){
        jeproLabCarrierMaximumWidth.setButtonLabel(JeproLabSettingModel.getStringValue("width_unit"));
        jeproLabCarrierMaximumHeight.setButtonLabel(JeproLabSettingModel.getStringValue("height_unit"));
        jeproLabCarrierMaximumDepth.setButtonLabel(JeproLabSettingModel.getStringValue("depth_unit"));
        jeproLabCarrierMaximumWeight.setButtonLabel(JeproLabSettingModel.getStringValue("weight_unit"));


        jeproLabCarrierLogoImage = new Image(JeproLab.class.getResourceAsStream("resources/images/3d-truck.png"));
        jeproLabCarrierLogoImageView.setImage(jeproLabCarrierLogoImage);
        taxRulesGroups = JeproLabTaxModel.JeproLabTaxRulesGroupModel.getTaxRulesGroups(true);
        for(JeproLabTaxModel.JeproLabTaxRulesGroupModel taxRulesGroup : taxRulesGroups) {
            jeproLabCarrierTax.getItems().add(taxRulesGroup.name);
        }

        if(carrierId > 0){
            Worker<Boolean> worker = new Task<Boolean>() {
                //JeproLabCarrierModel carrier;
                //List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxGroups;
                List<Integer> zoneIds;
                List<Integer> groupIds;
                @Override
                protected Boolean call() throws Exception {
                    if(isCancelled()){
                        return false;
                    }
                    carrier = new JeproLabCarrierModel(carrierId);
                    //taxGroups = JeproLabTaxModel.JeproLabTaxRulesGroupModel.getTaxRulesGroups(true);
                    return true;
                }

                @Override
                public void failed(){
                    super.failed();
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, exceptionProperty().getValue());
                }

                @Override
                public void succeeded(){
                    super.succeeded();
                    updateCarrierForm(groupIds, zoneIds);
                }
            };

            JeproLab.getInstance().executor.submit((Task)worker);
        }else {
            carrier = new JeproLabCarrierModel();
            clearForm();
        }
        updateToolBar();
        addEventListener();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        saveCarrierButton = new Button(bundle.getString("JEPROLAB_SAVE_LABEL"));
        saveCarrierButton.setGraphic(new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));

        cancelCarrierButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"));
        cancelCarrierButton.getStyleClass().add("cross-btn");
        commandWrapper.setSpacing(5);
        commandWrapper.getChildren().addAll(saveCarrierButton, cancelCarrierButton);
    }

    @Override
    public void clearForm(){
        jeproLabCarrierReference.clear();
        jeproLabCarrierName.clear();
    }

    private void addEventListener(){
        saveCarrierButton.setOnAction(evt -> {
            carrier.reference = jeproLabCarrierReference.getText();
            carrier.name = jeproLabCarrierName.getText();
            carrier.tax_rules_group_id = jeproLabCarrierTax.getValue() == null ? 0 : getTaxRulesGroupIdByName(jeproLabCarrierTax.getValue());

            carrier.published = jeproLabCarrierPublished.isSelected();
            carrier.deleted = jeproLabCarrierDeleted.isSelected();
            carrier.url = JeproLabTools.checkUrlFormat(jeproLabCarrierTrackingUrl.getText());
            carrier.add_handling_cost = jeproLabCarrierAddHandlingCost.isSelected();
            if(jeproLabCarrierOutOfRangeBehavior.getValue() == (null)){
                carrier.range_behavior = JeproLabCarrierModel.JEPROLAB_DEACTIVATE_CARRIER;
            }else if(jeproLabCarrierOutOfRangeBehavior.getValue().equals(bundle.getString("JEPROLAB_TAKE_THE_UPPER_RANGE_LABEL"))){
                carrier.range_behavior = JeproLabCarrierModel.JEPROLAB_TAKE_THE_UPPER_RANGE;
            }else {
                carrier.range_behavior = JeproLabCarrierModel.JEPROLAB_DEACTIVATE_CARRIER;
            }
            int langId;
            for(Map.Entry entry : JeproLabLanguageModel.LANGUAGES.entrySet()) {
                langId = ((JeproLabLanguageModel)entry.getValue()).language_id;
                carrier.delay.put("lang_" + langId, jeproLabCarrierDeliveryDelay.getText()); // + " " + periodLabel);
            }
            carrier.is_module = jeproLabCarrierIsModule.isSelected();
            carrier.is_free = jeproLabCarrierFreeShipping.isSelected();
            carrier.shipping_external = jeproLabCarrierShippingExternal.isSelected();
            carrier.need_range = jeproLabCarrierNeedRange.isSelected();
            carrier.external_module_name = jeproLabCarrierExternalModuleName.getText();
            if(jeproLabCarrierShippingMethod.getValue().equals(bundle.getString("JEPROLAB_SHIPPING_METHOD_WEIGHT_LABEL"))) {
                carrier.shipping_method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT;
            }else if(jeproLabCarrierShippingMethod.getValue().equals(bundle.getString("JEPROLAB_SHIPPING_METHOD_PRICE_LABEL"))) {
                carrier.shipping_method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE;
            }else if(jeproLabCarrierShippingMethod.getValue().equals(bundle.getString("JEPROLAB_SHIPPING_METHOD_DEFAULT_LABEL"))) {
                carrier.shipping_method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_DEFAULT;
            }else if(jeproLabCarrierShippingMethod.getValue().equals(bundle.getString("JEPROLAB_SHIPPING_METHOD_FREE_LABEL"))) {
                carrier.shipping_method = JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_FREE;
            }

            carrier.max_width = Float.parseFloat(jeproLabCarrierMaximumWidth.getValue());
            carrier.max_height = Float.parseFloat(jeproLabCarrierMaximumHeight.getValue());
            carrier.max_depth = Float.parseFloat(jeproLabCarrierMaximumDepth.getValue());
            carrier.max_weight = Float.parseFloat(jeproLabCarrierMaximumWeight.getValue());
            carrier.grade = Integer.parseInt(jeproLabCarrierSpeedGrade.getText());

            if(carrier.carrier_id > 0){
                carrier.update();
            }else{
                carrier.add();
            }
        });
    }

    private void resetTaxData(List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxes){
        taxRulesGroups = taxes;
        jeproLabCarrierTax.getItems().clear();
        for(JeproLabTaxModel.JeproLabTaxRulesGroupModel tax : taxes){
            jeproLabCarrierTax.getItems().add(tax.name);
        }
    }

    private int getTaxRulesGroupIdByName(String name){
        for(JeproLabTaxModel.JeproLabTaxRulesGroupModel tax : taxRulesGroups){
            System.out.println(name);
            if(tax.name.toLowerCase().equals(name.toLowerCase())){
                return tax.tax_rules_group_id;
            }
        }
        return 0;
    }

    private void updateCarrierForm(List<Integer> groupIds, List<Integer> zoneIds){
        if(carrier != null && carrier.carrier_id > 0){

            Platform.runLater(() -> {
                formLabel.setText(bundle.getString("JEPROLAB_EDT_LABEL") + " " + bundle.getString("JEPROLAB_CARRIER_LABEL"));
                jeproLabCarrierMaximumWidth.setValue(String.valueOf(carrier.max_width));
                jeproLabCarrierMaximumHeight.setValue(String.valueOf(carrier.max_height));
                jeproLabCarrierMaximumDepth.setValue(String.valueOf(carrier.max_depth));
                jeproLabCarrierMaximumWeight.setValue(String.valueOf(carrier.max_weight));
                jeproLabCarrierReference.setText(carrier.reference);
                //public Pane jeproLabCarrierAssociatedGroupWrapper, jeproLabCarrierSupportedZonesWrapper;
                jeproLabCarrierName.setText(carrier.name);
                jeproLabCarrierSpeedGrade.setText(String.valueOf(carrier.grade));
                jeproLabCarrierTrackingUrl.setText(carrier.url);
                //public JeproImageChooser jeproLabCarrierCarrierLogo;
                //jeproLabCarrierDeliveryDelay.setText(carrier.delay);
                /*jeproLabCarrierTax.getItems().clear();
                for(JeproLabTaxModel.JeproLabTaxRulesGroupModel taxRulesGroup : taxes) {
                    jeproLabCarrierTax.getItems().add(taxRulesGroup.name);
                    if(taxRulesGroup.tax_rules_group_id == carrier.tax_rules_group_id){
                        jeproLabCarrierTax.setValue(taxRulesGroup.name);
                    }
                }*/
                //if(carrier.range_behavior == JeproLabCarrierModel.)
                //jeproLabCarrierOutOfRangeBehavior.setValue();
                if(carrier.logo_path != null) {
                    //jeproLabCarrierLogoImageView;
                }
                jeproLabCarrierPublished.setSelected(carrier.published);
                jeproLabCarrierAddHandlingCost.setSelected(carrier.add_handling_cost);
                jeproLabCarrierFreeShipping.setSelected(carrier.is_free);
                if(carrier.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
                    jeproLabCarrierShippingMethod.setValue(bundle.getString("JEPROLAB_SHIPPING_METHOD_PRICE_LABEL"));
                }else if(carrier.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT) {
                    jeproLabCarrierShippingMethod.setValue(bundle.getString("JEPROLAB_SHIPPING_METHOD_WEIGHT_LABEL"));
                }else if(carrier.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_DEFAULT) {
                    jeproLabCarrierShippingMethod.setValue(bundle.getString("JEPROLAB_SHIPPING_METHOD_DEFAULT_LABEL"));
                }else if(carrier.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_FREE){
                    jeproLabCarrierShippingMethod.setValue(bundle.getString("JEPROLAB_SHIPPING_METHOD_FREE_LABEL"));
                }
                saveCarrierButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
            });
        }
    }

    private void renderAssociatedZonesTableView(){
        jeproLabAssociatedZonesListView = new TableView<>();
        jeproLabAssociatedZonesListView.setPrefSize(600, (NUMBER_OF_ITEMS * rowHeight) + 24);

        List<JeproLabCountryModel.JeproLabZoneModel> zones = JeproLabCountryModel.JeproLabZoneModel.getZones(false);
        ObservableList<JeproLabCountryZoneController.JeproLabZoneRecord> zoneList = FXCollections.observableArrayList();
        zoneList.addAll(zones.stream().map(JeproLabCountryZoneController.JeproLabZoneRecord::new).collect(Collectors.toList()));

        TableColumn<JeproLabCountryZoneController.JeproLabZoneRecord, String> jeproLabAssociatedZoneIndexTableColumn = new TableColumn<>("#");
        jeproLabAssociatedZoneIndexTableColumn.setPrefWidth(30);
        tableCellAlign(jeproLabAssociatedZoneIndexTableColumn, Pos.CENTER_RIGHT);
        jeproLabAssociatedZoneIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("zoneIndex"));

        TableColumn<JeproLabCountryZoneController.JeproLabZoneRecord, Boolean> jeproLabAssociatedZoneCheckBoxTableColumn = new TableColumn<>();
        jeproLabAssociatedZoneCheckBoxTableColumn.setPrefWidth(25);
        tableCellAlign(jeproLabAssociatedZoneCheckBoxTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabCountryZoneController.JeproLabZoneRecord, Boolean>, TableCell<JeproLabCountryZoneController.JeproLabZoneRecord, Boolean>> checkBoxCellFactory = params -> new JeproLabCountryZoneController.JeproLabCheckBoxCell();
        jeproLabAssociatedZoneCheckBoxTableColumn.setCellFactory(checkBoxCellFactory);

        TableColumn<JeproLabCountryZoneController.JeproLabZoneRecord, String> jeproLabAssociatedZoneNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        tableCellAlign(jeproLabAssociatedZoneNameTableColumn, Pos.CENTER_LEFT);
        jeproLabAssociatedZoneNameTableColumn.setPrefWidth(543);
        jeproLabAssociatedZoneNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("zoneName"));

        jeproLabAssociatedZonesListView.getColumns().addAll(
            jeproLabAssociatedZoneIndexTableColumn,
            jeproLabAssociatedZoneCheckBoxTableColumn,
            jeproLabAssociatedZoneNameTableColumn
        );

        jeproLabAssociatedZonesListView.getItems().clear();
        jeproLabAssociatedZonesListView.getItems().addAll(zoneList);

        jeproLabCarrierSupportedZonesWrapper.getChildren().clear();
        jeproLabCarrierSupportedZonesWrapper.getChildren().add(jeproLabAssociatedZonesListView);
    }

    private void renderAssociatedGroupsTableView(){
        jeproLabAssociatedGroupListView = new TableView<>();
        double tableWidth = 600;
        TableColumn<JeproLabGroupController.JeproLabGroupRecord, Boolean> groupCheckBoxTableColumn = new TableColumn<>();
        groupCheckBoxTableColumn.setPrefWidth(25);
        tableCellAlign(groupCheckBoxTableColumn, Pos.CENTER);
        Callback<TableColumn<JeproLabGroupController.JeproLabGroupRecord, Boolean>, TableCell<JeproLabGroupController.JeproLabGroupRecord, Boolean>> groupCheckBoxFactory = param -> new JeproLabGroupController.JeproLabGroupCheckBoxCellFactory();
        groupCheckBoxTableColumn.setCellFactory(groupCheckBoxFactory);

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, String> groupIndexTableColumn = new TableColumn<>("#");
        groupIndexTableColumn.setPrefWidth(30);
        tableCellAlign(groupIndexTableColumn, Pos.CENTER_RIGHT);
        groupIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupIndex"));

        TableColumn<JeproLabGroupController.JeproLabGroupRecord, String> groupNameTableColumn = new TableColumn<>(bundle.getString("JEPROLAB_NAME_LABEL"));
        tableCellAlign(groupNameTableColumn, Pos.CENTER_LEFT);
        groupNameTableColumn.setPrefWidth(543);
        groupNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));

        jeproLabAssociatedGroupListView.getColumns().addAll(
            groupCheckBoxTableColumn, groupIndexTableColumn, groupNameTableColumn
        );

        jeproLabAssociatedGroupListView.setPrefSize(tableWidth, (rowHeight * NUMBER_OF_ITEMS) + 24);

        List<JeproLabGroupModel> groups = JeproLabGroupModel.getGroups(JeproLabContext.getContext().language.language_id);
        jeproLabAssociatedGroupList = FXCollections.observableArrayList();
        jeproLabAssociatedGroupList.addAll(groups.stream().map(JeproLabGroupController.JeproLabGroupRecord::new).collect(Collectors.toList()));

        Pagination jeproLabGroupPagination = new Pagination((jeproLabAssociatedGroupList.size()/ NUMBER_OF_ITEMS) + 1, 0);
        jeproLabGroupPagination.setPageFactory(this::createGroupPages);
        jeproLabCarrierAssociatedGroupWrapper.getChildren().add(jeproLabGroupPagination);
    }

    private Node createGroupPages(int pageIndex){
        int fromIndex = pageIndex * NUMBER_OF_ITEMS;
        int toIndex = Math.min(fromIndex + NUMBER_OF_ITEMS, (jeproLabAssociatedGroupList.size()));
        jeproLabAssociatedGroupListView.setItems(FXCollections.observableArrayList(jeproLabAssociatedGroupList.subList(fromIndex, toIndex)));

        return new Pane(jeproLabAssociatedGroupListView);
    }
}
