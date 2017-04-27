package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 *
 * Created by jeprodev on 11/04/2017.
 */
public class JeproLabCarrierAddController extends JeproLabController{
    private static final int NUMBER_OF_ITEMS = 8;
    private Image jeproLabCarrierLogoImage;
    private TableView<JeproLabCountryZoneController.JeproLabZoneRecord> jeproLabAssociatedZonesListView;
    private ObservableList<JeproLabCountryZoneController.JeproLabZoneRecord> jeproLabCarrierZonesList;
    private TableView<JeproLabGroupController.JeproLabGroupRecord> jeproLabAssociatedGroupListView;
    private ObservableList<JeproLabGroupController.JeproLabGroupRecord> jeproLabAssociatedGroupList;
    private ToggleGroup jeproLabShippingMethodToggleGroup;


    @FXML
    public JeproFormPanel jeproLabCarrierAddFormWrapper;
    public JeproFormPanelTitle jeproLabCarrierAddFormTitle;
    public JeproFormPanelContainer jeproLabCarrierAddFormContainer;
    public Label jeproLabCarrierNameLabel, jeproLabCarrierDeliveryDelayLabel, jeproLabCarrierSpeedGradeLabel;
    public Label jeproLabCarrierCarrierLogoLabel, jeproLabCarrierTrackingUrlLabel, jeproLabCarrierPublishedLabel;
    public Label jeproLabCarrierAddHandlingCostLabel, jeproLabCarrierFreeShippingLabel, jeproLabCarrierShippingMethodLabel;
    public Label jeproLabCarrierOutOfRangeBehaviorLabel, jeproLabCarrierTaxLabel, jeproLabCarrierSupportedZonesLabel;
    public Label jeproLabCarrierMaximumWidthLabel, jeproLabCarrierMaximumHeightLabel, jeproLabCarrierMaximumDepthLabel;
    public Label jeproLabCarrierMaximumWeightLabel, jeproLabCarrierAssociatedGroupsLabel;
    public JeproAppendButton jeproLabCarrierMaximumWidth, jeproLabCarrierMaximumHeight, jeproLabCarrierMaximumDepth, jeproLabCarrierMaximumWeight;
    public Pane jeproLabCarrierAssociatedGroupWrapper, jeproLabCarrierSupportedZonesWrapper;
    public TextField jeproLabCarrierName, jeproLabCarrierSpeedGrade, jeproLabCarrierTrackingUrl;
    public JeproImageChooser jeproLabCarrierCarrierLogo;
    public JeproMultiLangTextField jeproLabCarrierDeliveryDelay;
    public ComboBox<String> jeproLabCarrierTax, jeproLabCarrierOutOfRangeBehavior;
    public ImageView jeproLabCarrierLogoImageView;
    public TabPane jeproLabCarrierAddTabPane;
    public Tab jeproLabAddCarrierInformationTab, jeproLabCarrierSizeSettingTab, jeproLabCarrierCostTab;
    public GridPane jeproLabCarrierInformationFormLayout, jeproLabCarrierAddCostFormLayout, jeproLabCarrierSizeSettingFormLayout;
    public JeproSwitchButton jeproLabCarrierPublished, jeproLabCarrierAddHandlingCost, jeproLabCarrierFreeShipping;
    public HBox jeproLabCarrierShippingMethodWrapper;
    public ToggleButton jeproLabCarrierShippingMethodAccordingToTotalPrice, jeproLabCarrierShippingMethodAccordingToTotalWeight;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);

        formWidth = 0.98 * JeproLab.APP_WIDTH;

        jeproLabCarrierAddFormWrapper.setPrefWidth(formWidth);
        jeproLabCarrierAddFormWrapper.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabCarrierAddFormWrapper.setLayoutY(40);
        jeproLabCarrierAddFormTitle.setPrefSize(formWidth, 40);
        jeproLabCarrierAddFormContainer.setPrefWidth(formWidth);
        jeproLabCarrierAddFormContainer.setLayoutY(40);

        jeproLabCarrierAddTabPane.setPrefWidth(formWidth - 150);

        jeproLabAddCarrierInformationTab.setText(bundle.getString("JEPROLAB_INFORMATION_LABEL"));
        jeproLabCarrierSizeSettingTab.setText(bundle.getString("JEPROLAB_SIZE_SETTINGS_LABEL"));
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
        GridPane.setMargin(jeproLabCarrierShippingMethodWrapper, inputInsets);
        GridPane.setMargin(jeproLabCarrierOutOfRangeBehaviorLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierOutOfRangeBehavior, inputInsets);
        GridPane.setMargin(jeproLabCarrierTaxLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierTax, inputInsets);
        GridPane.setMargin(jeproLabCarrierSupportedZonesLabel, labelInsets);
        GridPane.setMargin(jeproLabCarrierSupportedZonesWrapper, inputInsets);
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

        GridPane.setValignment(jeproLabCarrierSupportedZonesLabel, VPos.TOP);
        GridPane.setValignment(jeproLabCarrierAssociatedGroupsLabel, VPos.TOP);

        jeproLabCarrierLogoImageView.setFitWidth(140);
        jeproLabCarrierLogoImageView.setFitHeight(140);

        HBox.setMargin(jeproLabCarrierLogoImageView, new Insets(25, 5, 5, 5));

        jeproLabShippingMethodToggleGroup = new ToggleGroup();
        jeproLabCarrierShippingMethodAccordingToTotalPrice.setText(bundle.getString("JEPROLAB_ACCORDING_TO_TOTAL_PRICE_LABEL"));
        jeproLabCarrierShippingMethodAccordingToTotalWeight.setText(bundle.getString("JEPROLAB_ACCORDING_TO_TOTAL_WEIGHT_LABEL"));
        jeproLabCarrierShippingMethodAccordingToTotalPrice.setToggleGroup(jeproLabShippingMethodToggleGroup);
        jeproLabCarrierShippingMethodAccordingToTotalWeight.setToggleGroup(jeproLabShippingMethodToggleGroup);

        jeproLabCarrierOutOfRangeBehavior.setPromptText(bundle.getString("JEPROLAB_SELECT_OUT_OF_RANGE_BEHAVIOR_LABEL"));
        jeproLabCarrierOutOfRangeBehavior.getItems().addAll(
            bundle.getString("JEPROLAB_TAKE_THE_UPPER_RANGE_LABEL"),
            bundle.getString("JEPROLAB_DEACTIVATE_CARRIER_LABEL")
        );
        renderAssociatedZonesTableView();
        renderAssociatedGroupsTableView();
    }

    @Override
    public void initializeContent(int carrierId){
        jeproLabCarrierMaximumWidth.setButtonLabel(JeproLabSettingModel.getStringValue("width_unit"));
        jeproLabCarrierMaximumHeight.setButtonLabel(JeproLabSettingModel.getStringValue("height_unit"));
        jeproLabCarrierMaximumDepth.setButtonLabel(JeproLabSettingModel.getStringValue("depth_unit"));
        jeproLabCarrierMaximumWeight.setButtonLabel(JeproLabSettingModel.getStringValue("weight_unit"));


        jeproLabCarrierLogoImage = new Image(JeproLab.class.getResourceAsStream("resources/images/3d-truck.png"));
        jeproLabCarrierLogoImageView.setImage(jeproLabCarrierLogoImage);
        List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxGroups = JeproLabTaxModel.JeproLabTaxRulesGroupModel.getTaxRulesGroups(true);
        for(JeproLabTaxModel.JeproLabTaxRulesGroupModel taxRulesGroup : taxGroups) {
            jeproLabCarrierTax.getItems().add(taxRulesGroup.name);
        }


        if(carrierId > 0){
            Worker<Boolean> worker = new Task<Boolean>() {
                JeproLabCarrierModel carrier;
                List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxes;
                List<Integer> zoneIds;
                List<Integer> groupIds;
                @Override
                protected Boolean call() throws Exception {
                    if(isCancelled()){
                        return false;
                    }
                    carrier = new JeproLabCarrierModel(carrierId);
                    taxes = JeproLabTaxModel.JeproLabTaxRulesGroupModel.getTaxRulesGroups(true);
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
                    updateCarrierForm(carrier, taxes, groupIds, zoneIds);
                }
            };

            JeproLab.getInstance().executor.submit((Task)worker);
        }
    }

    @Override
    public void updateToolBar(){

    }

    private void updateCarrierForm(JeproLabCarrierModel carrier, List<JeproLabTaxModel.JeproLabTaxRulesGroupModel> taxes, List<Integer> groupIds, List<Integer> zoneIds){
        if(carrier != null && carrier.carrier_id > 0){

            Platform.runLater(() -> {
                jeproLabCarrierMaximumWidth.setValue(String.valueOf(carrier.max_width));
                jeproLabCarrierMaximumHeight.setValue(String.valueOf(carrier.max_height));
                jeproLabCarrierMaximumDepth.setValue(String.valueOf(carrier.max_depth));
                jeproLabCarrierMaximumWeight.setValue(String.valueOf(carrier.max_weight));
                //public Pane jeproLabCarrierAssociatedGroupWrapper, jeproLabCarrierSupportedZonesWrapper;
                jeproLabCarrierName.setText(carrier.name);
                jeproLabCarrierSpeedGrade.setText(String.valueOf(carrier.grade));
                jeproLabCarrierTrackingUrl.setText(carrier.url);
                //public JeproImageChooser jeproLabCarrierCarrierLogo;
                jeproLabCarrierDeliveryDelay.setText(carrier.delay);
                jeproLabCarrierTax.getItems().clear();
                for(JeproLabTaxModel.JeproLabTaxRulesGroupModel taxRulesGroup : taxes) {
                    jeproLabCarrierTax.getItems().add(taxRulesGroup.name);
                    if(taxRulesGroup.tax_rules_group_id == carrier.tax_rules_group_id){
                        jeproLabCarrierTax.setValue(taxRulesGroup.name);
                    }
                }
                //if(carrier.range_behavior == JeproLabCarrierModel.)
                //jeproLabCarrierOutOfRangeBehavior.setValue();
                if(carrier.logo_path != null) {
                    //jeproLabCarrierLogoImageView;
                }
                jeproLabCarrierPublished.setSelected(carrier.published);
                jeproLabCarrierAddHandlingCost.setSelected(carrier.add_handling_cost);
                jeproLabCarrierFreeShipping.setSelected(carrier.is_free);
                if(carrier.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_PRICE) {
                    jeproLabCarrierShippingMethodAccordingToTotalPrice.setSelected(true);
                }else if(carrier.shipping_method == JeproLabCarrierModel.JEPROLAB_SHIPPING_METHOD_WEIGHT){
                    jeproLabCarrierShippingMethodAccordingToTotalWeight.setSelected(true);
                }
            });
        }
    }

    private void renderAssociatedZonesTableView(){
        jeproLabAssociatedZonesListView = new TableView<>();
        //// TODO: 13/04/2017 build range

        List<JeproLabCountryModel.JeproLabZoneModel> zones = JeproLabCountryModel.JeproLabZoneModel.getZones(false);

    }

    private void renderAssociatedGroupsTableView(){
        jeproLabAssociatedGroupListView = new TableView<>();
        double tableWidth = 300;
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
        groupNameTableColumn.setPrefWidth(tableWidth - 60);
        groupNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));

        jeproLabAssociatedGroupListView.getColumns().addAll(
            groupCheckBoxTableColumn, groupIndexTableColumn, groupNameTableColumn
        );

        jeproLabAssociatedGroupListView.setPrefSize(tableWidth, (rowHeight * NUMBER_OF_ITEMS) + 25);

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
