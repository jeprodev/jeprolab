package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.*;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.*;
import com.jeprolab.models.core.JeproLabRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAnalyzeAddController extends JeproLabController {
    private Label jeproLabFormTitle;
    private final double inputColumnWidth = 280;
    private final double labelColumnWidth = 150;
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
    public Tab jeproLabAnalyzeTechnicianTabForm;

    public HBox jeproLabAnalyzeSpecificPriceLabIdWrapper, jeproLabAnalyzeDelayWrapper;

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

    public JeproLabPriceBox jeproLabAnalyzeWholeSalePrice, jeproLabAnalyzePriceTaxExcluded, jeproLabAnalyzePriceUseEcoTax, jeproLabAnalyzePriceTaxIncluded;
    public JeproLabPriceBox jeproLabAnalyzeSpecificPrice;

    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        double formWidth = 0.92 * JeproLab.APP_WIDTH;
        //double centerGrid = (formWidth - (labelColumnWidth + inputColumnWidth))/2;
        double posX = (JeproLab.APP_WIDTH / 2) - (formWidth) / 2;
        double posY = 15;

        jeproLabFormTitle = new Label(bundle.getString("JEPROLAB_ADD_NEW_ANALYSE_LABEL"));
        jeproLabFormTitle.getStyleClass().add("input-label");
        jeproLabAddAnalyseFormWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutX(.02 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormWrapper.setLayoutY(20);
        jeproLabAddAnalyseFormTitleWrapper.setPrefSize(0.96 * JeproLab.APP_WIDTH, 40);
        jeproLabAddAnalyseFormTitleWrapper.getChildren().add(jeproLabFormTitle);
        jeproLabAddAnalyseFormContainerWrapper.setPrefWidth(0.96 * JeproLab.APP_WIDTH);
        jeproLabAddAnalyseFormContainerWrapper.setLayoutY(40);

        jeproLabAnalyzeTabPane.setPrefWidth(0.96 * JeproLab.APP_WIDTH);

        /** Setting and laying out analyze information tab **/
        jeproLabAnalyzeInformationLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth),
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth)
        );
        jeproLabAnalyzeInformationLayout.setLayoutX(posX);
        jeproLabAnalyzeInformationLayout.setLayoutY(posY);

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

        GridPane.setMargin(jeproLabAnalyzeImageChooserLabel, new Insets(5, 10, 10, 15));
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

        jeproLabAnalyzeName.setWidth(310);
        GridPane.setMargin(jeproLabAnalyzeShortDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeShortDescription.setTextAreaPrefSize(770, 55);
        GridPane.setMargin(jeproLabAnalyzeDescription, new Insets(10, 0, 0, 0));
        jeproLabAnalyzeDescription.setTextAreaPrefSize(770, 85);

        GridPane.setMargin(jeproLabAnalyzeSlider, new Insets(15, 0, 10, 0));
        jeproLabAnalyzeSlider.setSliderPrefHeight(100);
        jeproLabAnalyzeSlider.setSliderPrefWidth(JeproLab.APP_WIDTH - 300);

        jeproLabAnalyzeSeoTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeAssociationTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeImageTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeShippingTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeTechnicianTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzeOptionLabel.setText(bundle.getString("JEPROLAB_OPTIONS_LABEL"));
        jeproLabAnalyzeOptionLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeVisibilityLabel.setText(bundle.getString("JEPROLAB_VISIBILITY_LABEL"));
        jeproLabAnalyzeVisibilityLabel.getStyleClass().add("input-label");
        jeproLabAnalyzeAvailableForOrder.setText(bundle.getString("JEPROLAB_AVAILABLE_FOR_ORDER_LABEL"));
        jeproLabAnalyzeAvailableForOrder.getStyleClass().add("input-label");

        /** Setting and laying out Attached file form **/
        jeproLabAnalyzeAttachedFileTabForm.setText(bundle.getString("JEPROLAB_ATTACHED_FILES_LABEL"));
        jeproLabAnalyzeAttachedFileTabForm.setClosable(false);


        initializeContent();
        addEventListener();
        updateToolBar();
    }

    @Override
    public void initializeContent() {
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        JeproLabRequest request = JeproLab.request;
        this.loadObject(true);

        /*$this->addToolBar();
        $this->sideBar = JHtmlSidebar::render();
*/
        boolean displayCommonField = false;
        boolean displayMultiLabCheckBoxes = false;
        int multiLabCheck = 0;
        if (JeproLabLaboratoryModel.isFeaturePublished()) {
            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT) {
                displayMultiLabCheckBoxes = true;
                multiLabCheck = request.getIntValue("multi_lab_check", 0);
            }

            if (JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.ALL_CONTEXT) {
                //$bullet_common_field = '<i class="icon-circle text-orange"></i>';
                displayCommonField = true;
            }
        }

        languages = JeproLabLanguageModel.getLanguages();
        defaultLanguageId = JeproLabSettingModel.getIntValue("default_lang");
        displayMultiLabCheckBoxes = (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.LAB_CONTEXT);
        getCombinationImages();
/*
        if(analyze.analyze_id > 0){
            analyzeId = analyze.analyze_id;
        }else{
            analyzeId = JeproLab.request.getIntValue("analyze_id");
        }
*/
        //$upload_max_file_size = JeproLabTools::getOctets(ini_get('upload_max_filesize'));
        //$upload_max_file_size = ($upload_max_file_size/1024)/1024;

        boolean countryDisplayTaxLabel = context.country.display_tax_label;
        boolean hasCombinations = analyze.hasAttributes() > 0;
        boolean analyzeExistsInLab = true;

        if (analyze.analyze_id > 0 && JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && !analyze.isAssociatedToLaboratory(context.laboratory.laboratory_id)) {
            analyzeExistsInLab = false;

            JeproLabAnalyzeModel defaultAnalyze = new JeproLabAnalyzeModel();
        }
/*
        if(context.controller.default_form_language){
            languages = JeproLabLanguageModel.getLanguages();
        }

        if($app->input->get('submit_form_ajax')){
            $this->context.controller->use_ajax = true;
        } */
        //$this->helper = new JeproLabHelper();

        /** prepare fields data **/
        this.initInformationForm();
        this.initPriceForm();
        this.initAssociationsForm();
        this.initAttributesForm();
        this.initQuantitiesForm();
        this.initImagesForm();
        this.initCustomizationsForm();
        this.initFeaturesForm();
        this.initSuppliersForm();
        this.initShippingForm();
        this.initAttachmentForm();
        //this->assign('current_shop_url', $this->context.shop->getBaseURL());
    }

    private void initInformationForm() {
        String analyzeNameRedirected = JeproLabAnalyzeModel.getAnalyzeName(analyze.analyze_redirected_id, 0, context.language.language_id);

        //$this->assignRef('product_name_redirected', $product_name_redirected);
        /*
         * Form for adding a virtual product like software, mp3, etc...
         * /
        $product_download = new JeproLabProductDownloadModelProductDownload();
        $product_download_id = $product_download->getIdFromProductId($this->product->product_id);
        if ($product_download_id){
            $product_download = new JeproLabProductDownloadModelProductDownload($product_download_id);
        }
        $this->product->productDownload = $product_download;
*/
        int cacheDefaultAttribute = analyze.cache_default_attribute;

        /*$product_props = array();
        // global informations
        array_push($product_props, 'reference', 'ean13', 'upc',	'available_for_order', 'show_price', 'online_only',	'manufacturer_id');

        // specific / detailed information
        array_push($product_props,
                // physical product
                'width', 'height', 'weight', 'published',
                // virtual product
                'is_virtual', 'cache_default_attribute',
                // customization
                'uploadable_files', 'text_fields'
        );
        // prices
        array_push($product_props,
                'price', 'wholesale_price', 'tax_rules_group_id', 'unit_price_ratio', 'on_sale', 'unity',
                'minimal_quantity', 'additional_shipping_cost', 'available_now', 'available_later', 'available_date'
        );

        if(JeproLabSettingModelSetting::getValue('use_eco_tax')){
            array_push($product_props, 'ecotax');
        }

        $this->product->name['class'] = 'updateCurrentText';
        if (!$this->product->product_id || JeproLabSettingModelSetting::getValue('force_friendly_product')){
            $this->product->name['class'] .= ' copy2friendlyUrl';
        }

        $images = JeproLabImageModelImage::getImages($this->context.language->lang_id, $this->product->product_id);

        if (is_array($images)){
            foreach ($images as $k => $image){
                //$images[$k]->src = $this->context.controller->getImageLink($this->product->link_rewrite[$this->context.language->lang_id], $this->product->product_id.'-'.$image->image_id, 'small_default'); echo $images[$k]->src;
            }
           //$this->assignRef('product_images', $images);
        }*/
        List<JeproLabImageModel.JeproLabImageTypeModel> imagesTypes = JeproLabImageModel.JeproLabImageTypeModel.getImagesTypes("analyzes");
        //$this->assignRef('imagesTypes', $imagesTypes);

        analyze.tags = JeproLabTagModel.getAnalyzeTags(analyze.analyze_id);

        int analyzeType = JeproLab.request.getIntValue("analyze_type", analyze.getType());
        //$this->assignRef('product_type', $product_type);
        boolean isInPack = JeproLabAnalyzePackModel.isPacked(analyze.analyze_id);
        //$this->assignRef('is_in_pack', $is_in_pack);

        boolean checkAnalyzeAssociationAjax = false;
        if (JeproLabLaboratoryModel.isFeaturePublished() && JeproLabLaboratoryModel.getLabContext() != JeproLabLaboratoryModel.ALL_CONTEXT) {
            checkAnalyzeAssociationAjax = true;
        }

        //String isoTinyMce = context.language.iso_code;
        //$iso_tiny_mce = (file_exists(JURI::base() . '/components/com_jeproshop/assets/javascript/tiny_mce/langs/'.$iso_tiny_mce.'.js') ? $iso_tiny_mce : 'en');
        //$this->assignRef('iso_tiny_mce', $iso_tiny_mce);
        //$this->assignRef('check_product_association_ajax', $check_product_association_ajax);
        getCombinationImages();
        //$this->assignRef('combinationImagesJs', $combinationImageJs); */


        if (analyze.analyze_id > 0) {
            jeproLabAnalyzePublished.setSelected(analyze.published);
            jeproLabAnalyzeOnSale.setSelected(analyze.on_sale);
            jeproLabAnalyzeAvailableForOrder.setSelected(analyze.available_for_order);
            jeproLabAnalyzeShowPrice.setSelected(analyze.show_price);
            jeproLabAnalyzeName.setText(analyze.name);
            jeproLabAnalyzeShortDescription.setText(analyze.short_description);
            jeproLabAnalyzeDescription.setText(analyze.description);
        }

        String[] redirection = new String[3];
        redirection[0] = "404";
        redirection[1] = "301";
        redirection[2] = "302";
        jeproLabAnalyzeRedirect.getItems().clear();
        for (String label : redirection) {
            jeproLabAnalyzeRedirect.getItems().add(bundle.getString("JEPROLAB_" + label + "_LABEL"));
            if (analyze.analyze_id > 0 && label.equals(analyze.redirect_type)) {
                jeproLabAnalyzeRedirect.setValue(bundle.getString("JEPROLAB_" + label + "_LABEL"));
            } else if (label.equals("404")) {
                jeproLabAnalyzeRedirect.setValue(bundle.getString("JEPROLAB_" + label + "_LABEL"));
            }
        }

        String[] visibilities = new String[4];
        visibilities[0] = "both";
        visibilities[1] = "catalog";
        visibilities[2] = "search";
        visibilities[3] = "none";
        jeproLabAnalyzeVisibility.getItems().clear();
        for (String label : visibilities) {
            jeproLabAnalyzeVisibility.getItems().add(bundle.getString("JEPROLAB_" + label.toUpperCase() + "_LABEL"));
            if (analyze.analyze_id > 0 && label.equals(analyze.visibility)) {
                jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_" + label.toUpperCase() + "_LABEL"));
            } else if (label.equals("both")) {
                jeproLabAnalyzeVisibility.setValue(bundle.getString("JEPROLAB_" + label.toUpperCase() + "_LABEL"));
            }
        }
    }

    private void initImagesForm() {
        /* if ((bool)$this->product->product_id){
            if ($this->product_exists_in_shop){
                $shops = false;
                if (JeproLabLaboratoryModel::isFeaturePublished()){
                    $shops = JeproLabLaboratoryModel::getShops();
                }
                if ($shops){
                    foreach ($shops as $key => $shop){
                        if (!$this->product->isAssociatedToShop($shop->shop_id)){
                            unset($shops[$key]);
                        }
                    }
                }
               //$this->assignRef('shops', $shops);
                $db = JFactory::getDBO();
                $app = JFactory::getApplication();

                $query = "SELECT COUNT(product_id) FROM " . $db->quoteName('#__jeproshop_image');
                $query .= " WHERE product_id = " .(int)$this->product->product_id;
                $db->setQuery($query);
                $count_images = $db->loadResult();

                $images = JeproLabImageModelImage::getImages($this->context.language->lang_id, $this->product->product_id);
                foreach ($images as $k => $image){
                    $images[$k] = new JeproLabImageModelImage($image->image_id);
                }

                if ($this->context.shop->getShopContext() == JeproLabLaboratoryModel::CONTEXT_SHOP){
                    $current_shop_id = (int)$this->context.shop->shop_id;
                }else{
                    $current_shop_id = 0;
                }

                $languages = JeproLabLanguageModelLanguage::getLanguages(true);
                $image_uploader = new JeproLabImageUploader('file');
                $image_link = JRoute::_('index.php?option=com_jeproshop&view=product&ajax=1&product_id=' . (int)$this->product->product_id .'&task=add_product_image');
                $image_uploader->setMultiple(!(JeproLabTools::getUserBrowser() == 'Apple Safari' && JeproLabTools::getUserPlatform() == 'Windows'))
                ->setUseAjax(true)->setUrl($image_link);


               //$this->assignRef('countImages', $count_images);
                /*$this->assignRef(
                        'id_product' => (int)Tools::getValue('id_product'),
                        'id_category_default' => (int)$this->_category->id, * /
               //$this->assignRef('images', $images);
                /*'iso_lang' => $languages[0]['iso_code'],
                'token' =>  $this->token,
                'table' => $this->table,* /
                $image_size = ((int)JeproLabSettingModelSetting::getValue('product_picture_max_size') / 1024 / 1024);
               //$this->assignRef('max_image_size', $image_size);
                $virtualProductFilenameAttribute = (string)$app->input->get('virtual_product_filename_attribute');
               //$this->assignRef('up_filename', $virtualProductFilenameAttribute);
                //'currency' => $this->context.currency,
               //$this->assignRef('current_shop_id', $current_shop_id);
                //		'languages' => $this->_languages,
                //		'default_language' => (int)Configuration::get('PS_LANG_DEFAULT'),
                $imageUploader = $image_uploader->render();
               //$this->assignRef('image_uploader', $imageUploader);
                //));

                $type = JeproLabImageTypeModelImageType::getByNameNType('%', 'products', 'height');
                if (isset($type->name)){
                    $imageType = $type->name;
                }else{
                    $imageType = 'small_default';
                }
               //$this->assignRef('image_type', $imageType);
            }
            else
                $this->displayWarning($this->l('You must save the product in this shop before adding images.'));
        } */
    }

    private void initFeaturesForm() {
        //if (!$this->context.controller->default_form_language){ $this->context.controller->getLanguages(); }

        /*$data = $this->createTemplate($this->tpl_form);
        $data->assign('default_form_language', $this->default_form_language); * /

        if (!JeproLabFeatureModelFeature::isFeaturePublished()){
            $this->displayWarning($this->l('This feature has been disabled. ').' <a href="index.php?tab=AdminPerformance&token='.Tools::getAdminTokenLite('AdminPerformance').'#featuresDetachables">'.$this->l('Performances').'</a>');
        }else{
            if ($this->product->product_id){
                if ($this->product_exists_in_shop){
                    $features = JeproLabFeatureModelFeature::getFeatures($this->context.language->lang_id, (JeproLabLaboratoryModel::isFeaturePublished() && JeproLabLaboratoryModel::getShopContext() == JeproLabLaboratoryModel::CONTEXT_SHOP));

                    foreach ($features as $k => $feature){
                        $features[$k]->current_item = false;
                        $features[$k]->val = array();

                        $custom = true;
                        foreach ($this->product->getFeatures() as $products){
                            if ($products->feature_id == $features->feature_id){
                                $features[$k]->current_item = $products->feature_value_id;
                            }
                        }
                        $features[$k]->featureValues = JeproLabFeatureValueModelFeatureValue::getFeatureValuesWithLang($this->context.language->lang_id, (int)$feature->feature_id);
                        if (count($features[$k]->featureValues)){
                            foreach ($features[$k]->featureValues as $value){
                                if ($features[$k]->current_item == $value->feature_value_id){
                                    $custom = false;
                                }
                            }
                        }
                        if ($custom){
                            $features[$k]->val = JeproLabFeatureValueModelFeatureValue::getFeatureValueLang($features[$k]->current_item);
                        }
                    }

                   //$this->assignRef('available_features', $features);

                    /*$data->assign('product', $obj);
                    $data->assign('link', $this->context.link);
                    $data->assign('languages', $this->_languages);
                    $data->assign('default_form_language', $this->default_form_language); * /
                }
                else
                    $this->displayWarning($this->l('You must save the product in this shop before adding features.'));
            }
            else
                $this->displayWarning($this->l('You must save this product before adding features.')); * /
        }
*/
    }

    private void initAttachmentForm() {
        /*if (!$this->context.controller->default_form_language){
            $this->languages = $this->context.controller->getLanguages();
        }

        if ((bool)$this->product->product_id){
            if ($this->product_exists_in_shop){
                $attachment_name = array();
                $attachment_description = array();
                foreach ($this->languages as $language){
                    $attachment_name[$language->lang_id] = '';
                    $attachment_description[$language->lang_id] = '';
                }

                $iso_tiny_mce = (file_exists(COM_JEPROSHOP_JS_DIR . DIRECTORY_SEPARATOR .'tiny_mce/langs/'. $this->context.language->iso_code .'.js') ? $this->context.language->iso_code : 'en');

                $attachment_link = JRoute::_('index.php?option=com_jeproshop&view=product&ajax=1&task=add_attachment&product_id=' . (int)$this->product->product_id);
                $attachment_uploader = new JeproLabFileUploader('attachment_file');
                $attachment_uploader->setMultiple(false)->setUseAjax(true)->setUrl($attachment_link)
                ->setPostMaxSize((JeproLabSettingModelSetting::getValue('attachment_maximum_size') * 1024 * 1024));
                //->setTemplate('attachment_ajax.tpl');
                /*
                        $data->assign(array(
                                'obj' => $obj,
                                'table' => $this->table,
                                'ad' => __PS_BASE_URI__.basename(_PS_ADMIN_DIR_),
                                'iso_tiny_mce' => $iso_tiny_mce,
                                'languages' => $this->_languages,
                                'id_lang' => $this->context.language->id,; * /
                $attachments_1 = JeproLabAttachmentModelAttachment::getAttachments($this->context.language->lang_id, $this->product->product_id, true);
               //$this->assignRef('attachments_1', $attachments_1);
                $attachments_2 = JeproLabAttachmentModelAttachment::getAttachments($this->context.language->lang_id, $this->product->product_id, false);
               //$this->assignRef('attachments_2', $attachments_2);
               //$this->assignRef('attachment_name', $attachment_name);
               //$this->assignRef('attachment_description', $attachment_description);
                $attachment_maximum_size = JeproLabSettingModelSetting::getValue('attachment_maximum_size');
               //$this->assignRef('attachment_maximum_size', $attachment_maximum_size);
                $attachment_uploader = $attachment_uploader->render();
               //$this->assignRef('attachment_uploader', $attachment_uploader);
            }else
                $this->displayWarning($this->l('You must save the product in this shop before adding attachements.'));
        }
        else
            $this->displayWarning($this->l('You must save this product before adding attachements.')); */
    }

    private void initCustomizationsForm() { /*
        if ((bool)$this->product->product_id){
            if ($this->product_exists_in_shop){
                $labels = $this->product->getCustomizationFields();

                $has_file_labels = (int)$this->product->uploadable_files;
                $has_text_labels = (int)$this->product->text_fields;

               //$this->assignRef('has_file_labels', $has_file_labels);
                $displayFileLabels = $this->displayLabelFields($obj, $labels, JeproLabSettingModelSetting::getValue('default_lang'), JeproLabAnalyzeModel.::CUSTOMIZE_FILE);
               //$this->assignRef('display_file_labels', $displayFileLabels);
               //$this->assignRef('has_text_labels', $has_text_labels);
                $displayTextLabels = $this->displayLabelFields($obj, $labels, JeproLabSettingModelSetting::getValue('default_lang'), JeproLabAnalyzeModel.::CUSTOMIZE_TEXT_FIELD);
               //$this->assignRef('display_text_labels', $displayTextLabels);
                $uploadable_files = (int)($this->product->uploadable_files ? (int)$this->product->uploadable_files : '0');
               //$this->assignRef('uploadable_files', $uploadable_files);
                $text_fields = (int)($this->product->text_fields ? (int)$this->product->text_fields : '0');
               //$this->assignRef('text_fields', $text_fields);

            }
            else
                $this->displayWarning($this->l('You must save the product in this shop before adding customization.'));
        }
        else
            $this->displayWarning($this->l('You must save this product before adding customization.')); */

    }

    private void initQuantitiesForm() {
        /*if(!$this->context.controller->default_form_language){
            $this->languages = $this->context.controller->getLanguages();
        }

        if($this->product->product_id){
            if($this->product_exists_in_shop){
                //Get all product_attribute_id
                $attributes = $this->product->getAttributesResume($this->context.language->lang_id);
                if(empty($attributes)){
                    $attributes[] = new JObject();
                    $attributes[0]->set('product_attribute_id', 0);
                    $attributes[0]->set('attribute_designation', '');
                }

                /** get available quantities ** /
                $available_quantity = array();
                $product_designation = array();

                foreach($attributes as $attribute){
                    $product_attribute_id = is_object($attribute) ? $attribute->product_attribute_id : $attribute['product_attribute_id'];
                    $attribute_designation = is_object($attribute) ? $attribute->attribute_designation : $attribute['attribute_designation'];
                    // Get available quantity for the current product attribute in the current shop
                    $available_quantity[$product_attribute_id] = JeproLabStockAvailableModelStockAvailable::getQuantityAvailableByProduct((int)$this->product->product_id,
                            $product_attribute_id);
                    // Get all product designation
                    $product_designation[$product_attribute_id] = rtrim(
                            $this->product->name[$this->context.language->lang_id].' - '.$attribute_designation, ' - '
                    );
                }

                $show_quantities = true;
                $shop_context = JeproLabLaboratoryModel::getShopContext();
                $shop_group = new JeproLabShopGroupModelShopGroup((int)JeproLabLaboratoryModel::getContextShopGroupID());

                // if we are in all shops context, it's not possible to manage quantities at this level
                if (JeproLabLaboratoryModel::isFeaturePublished() && $shop_context == JeproLabLaboratoryModel::CONTEXT_ALL){
                    $show_quantities = false;
                    // if we are in group shop context
                }elseif (JeproLabLaboratoryModel::isFeaturePublished() && $shop_context == JeproLabLaboratoryModel::CONTEXT_GROUP){
                    // if quantities are not shared between shops of the group, it's not possible to manage them at group level
                    if (!$shop_group->share_stock){ $show_quantities = false; }
                }else{
                    // if we are in shop context
                    // if quantities are shared between shops of the group, it's not possible to manage them for a given shop
                    if ($shop_group->share_stock){ $show_quantities = false; }
                }

                $stock_management = JeproLabSettingModelSetting::getValue('stock_management');
               //$this->assignRef('stock_management', $stock_management);
                $has_attribute = $this->product->hasAttributes();
               //$this->assignRef('has_attribute', $has_attribute);
                // Check if product has combination, to display the available date only for the product or for each combination
                $db = JFactory::getDBO();
                if(JeproLabCombinationModelCombination::isFeaturePublished()){
                    $query = "SELECT COUNT(product_id) FROM " . $db->quoteName('#__jeproshop_product_attribute') . " WHERE ";
                    $query .= " product_id = " . (int)$this->product->product_id;
                    $db->setQuery($query);
                    $countAttributes = (int)$db->loadResult();
                }else{
                    $countAttributes = false;
                }
               //$this->assignRef('count_attributes', $countAttributes);
                // if advanced stock management is active, checks associations
                $advanced_stock_management_warning = false;
                if (JeproLabSettingModelSetting::getValue('advanced_stock_management') && $this->product->advanced_stock_management){
                    $product_attributes = JeproLabAnalyzeModel.::getProductAttributesIds($this->product->product_id);
                    $warehouses = array();

                    if (!$product_attributes){
                        $warehouses[] = JeproLabWarehouseModelWarehouse::getProductWarehouseList($this->product->product_id, 0);
                    }

                    foreach ($product_attributes as $product_attribute){
                        $ws = JeproLabWarehouseModelWarehouse::getProductWarehouseList($this->product->product_id, $product_attribute->product_attribute_id);
                        if ($ws){
                            $warehouses[] = $ws;
                        }
                    }
                    $warehouses = JeproLabTools::arrayUnique($warehouses);

                    if (empty($warehouses)){
                        $advanced_stock_management_warning = true;
                    }
                }

                if ($advanced_stock_management_warning){
                    JError::raiseWarning(500, JText::_('If you wish to use the advanced stock management, you must:'));
                    JError::raiseWarning(500, '- ' . JText::_('associate your products with warehouses.'));
                    JError::raiseWarning(500, '- ' . JText::_('associate your warehouses with carriers.'));
                    JError::raiseWarning(500, '- ' . JText::_('associate your warehouses with the appropriate shops.'));
                }

                $pack_quantity = null;

                // if product is a pack
                if (JeproLabProductPack::isPack($this->product->product_id)){
                    $items = JeproLabProductPack::getItems((int)$this->product->product_id, JeproLabSettingModelSetting::getValue('default_lang'));

                    // gets an array of quantities (quantity for the product / quantity in pack)
                    $pack_quantities = array();
                    foreach ($items as $item){
                        if (!$item->isAvailableWhenOutOfStock((int)$item->out_of_stock)){
                            $pack_id_product_attribute = JeproLabAnalyzeModel.::getDefaultAttribute($item->product_id, 1);
                            $pack_quantities[] = JeproLabAnalyzeModel.::getQuantity($item->id, $pack_id_product_attribute) / ($item->pack_quantity !== 0 ? $item->pack_quantity : 1);
                        }
                    }

                    // gets the minimum
                    if (count($pack_quantities)){
                        $pack_quantity = $pack_quantities[0];
                        foreach ($pack_quantities as $value){
                            if ($pack_quantity > $value){
                                $pack_quantity = $value;
                            }
                        }
                    }

                    if (!JeproLabWarehouseModelWarehouse::getPackWarehouses((int)$this->product->product_id))
                    $this->displayWarning($this->l('You must have a common warehouse between this pack and its product.'));
                }

               //$this->assignRef('attributes', $attributes);
               //$this->assignRef('available_quantity', $available_quantity);
               //$this->assignRef('pack_quantity', $pack_quantity);
                $stock_management_active = JeproLabSettingModelSetting::getValue('advanced_stock_management');
               //$this->assignRef('stock_management_active', $stock_management_active);
               //$this->assignRef('product_designation', $product_designation);
               //$this->assignRef('show_quantities', $show_quantities);
                $order_out_of_stock = JeproLabSettingModelSetting::getValue('allow_out_of_stock_ordering');
               //$this->assignRef('order_out_of_stock', $order_out_of_stock);
                /*'token_preferences' => Tools::getAdminTokenLite('AdminPPreferences'),
                'token' => $this->token,
                'languages' => $this->_languages,
                'id_lang' => $this->context.language->id
        ));* /
            }else{
                JError::raiseWarning(500, JText::_('You must save the product in this shop before managing quantities.'));
            }
        }else{
            JError::raiseWarning(500, JText::_('You must save this product before managing quantities.'));
        } */
    }

    private void initShippingForm() {
        /*$dimension_unit = JeproLabSettingModelSetting::getValue('dimension_unit');
       //$this->assignRef('dimension_unit', $dimension_unit);
        $weight_unit = JeproLabSettingModelSetting::getValue('weight_unit');
       //$this->assignRef('weight_unit', $weight_unit);
        $carrier_list = $this->getCarrierList();
       //$this->assignRef('carrier_list', $carrier_list); */
    }
/*
    protected function getCarrierList(){
        /*$carrier_list = JeproLabCarrierModelCarrier::getCarriers($this->context.language->lang_id, false, false, false, null, JeproLabCarrierModelCarrier::JEPROSHOP_ALL_CARRIERS);
        if ($this->product){
            $carrier_selected_list = $this->product->getCarriers();
            foreach ($carrier_list as &$carrier){
                foreach ($carrier_selected_list as $carrier_selected){
                    if ($carrier_selected->reference_id == $carrier->reference_id){
                        $carrier->selected = true;
                        continue;
                    }
                }
            }
        }
        return $carrier_list; * /
    }*/

    private void initAttributesForm() {
        /* if(!JeproLabCombinationModelCombination::isFeaturePublished()){
            $settingPanelLink = '<a href="#" >' . JText::_('COM_JEPROSHOP_PERFORMANCE_LABEL') . '</a>';
            JError::raiseWarning(500, JText::_('COM_JEPROSHOP_FEATURE_HAS_BEEN_DISABLED_MESSAGE') . $settingPanelLink);
        }elseif(JeproLabTools::isLoadedObject($this->product, 'product_id')){
            if($this->product_exists_in_shop){
                if($this->product->is_virtual){
                    JError:raiseWarning(500, JText::_('COM_JEPROSHOP_VIRTUAL_PRODUCT_CANNOT_HAVE_COMBINATIONS'));
                }else{
                    $attribute_js = array();
                    $attributes = JeproLabAttributeModelAttribute::getAttributes($this->context.language->lang_id, true);
                    if($attributes){
                        foreach($attributes as $key => $attribute){
                            $attribute_js[$attribute->attribute_group_id][$attribute->attribute_id] = $attribute->name;
                        }
                    }
                   //$this->assignRef('attributeJs', $attribute_js);
                    $attributes_groups =  JeproLabAttributeGroupModelAttributeGroup::getAttributesGroups($this->context.language->lang_id);
                   //$this->assignRef('attributes_groups',$attributes_groups);

                    $images = JeproLabImageModelImage::getImages($this->context.language->lang_id, $this->product->product_id);
                    $weight_unit = JeproLabSettingModelSetting::getValue('weight_unit');
                   //$this->assignRef('weight_unit', $weight_unit);
                    $reasons = JeproLabStockMovementReasonModelStockMovementReason::getStockMovementReasons();
                   //$this->assignRef('reasons', $reasons);
                    //$this->assignRef('minimal_quantity', );
                   //$this->assignRef('available_date', $available_date);
                    $stock_mvt_default_reason = JeproLabSettingModelSetting::getValue('default_stock_mvt_reason');
                   //$this->assignRef('default_stock_mvt_reason', $stock_mvt_default_reason);

                    $i = 0;
                    /*$type = JeproLabImageTypeModelImageType::getByNameNType('%', 'products', 'height');
                    if (isset($type->name)){
                        $data->assign('imageType', $type['name']);
                    }else
                        $data->assign('imageType', 'small_default'); * /
                    //$this->assignRef('imageWidth', (isset($image_type->width) ? (int)($image_type->width) : 64) + 25);
                    foreach ($images as $k => $image){
                        $images[$k]->obj = new JeproLabImageModelImage($image->image_id);
                        ++$i;
                    }
                   //$this->assignRef('attribute_images', $images);
                    $attributeList = $this->renderAttributesList($this->product, $this->currency);
                   //$this->assignRef('list', $attributeList);
                    $combination_exists = (JeproLabLaboratoryModel::isFeaturePublished() && (JeproLabLaboratoryModel::getContextShopGroup()->share_stock) && count(JeproLabAttributeGroupModelAttributeGroup::getAttributesGroups($this->context.language->lang_id)) > 0 && $this->product->hasAttributes());
                   //$this->assignRef('combination_exists', $combination_exists);
                }
            }
        } */
    }

    private void initAssociationsForm() {
        /* $app = JFactory::getApplication();
        /** prepare category tree ** /
        $root = JeproLabCategoryModelCategory::getRootCategory();

        $default_category_id = $this->context.cookie->products_filter_category_id ? $this->context.cookie->products_filter_category_id : JeproLabContext::getContext()->shop->category_id;
        $categoryBox = $app->input->get('category_box', array($default_category_id));
        if(!$this->product->product_id || !$this->product->isAssociatedToShop()){
            $selected_category = JeproLabCategoryModelCategory::getCategoryInformations($categoryBox, $this->context.controller->default_form_language);
        }else{
            if($categoryBox){
                $selected_category = JeproLabCategoryModelCategory::getCategoryInformations($categoryBox);
            }else{
                $selected_category = JeproLabAnalyzeModel.::getProductCategoriesFull($this->product->product_id, $this->context.controller->default_form_language);
            }
        }

        // Multishop block
        $feature_shop_active = JeproLabLaboratoryModel::isFeaturePublished();
       //$this->assignRef('feature_shop_published', $feature_shop_active);

        /** Accessories ** /
        $accessories = JeproLabAnalyzeModel.::getAccessoriesLight($this->context.language->lang_id, $this->product->product_id);
        $postAccessories = $app->input->get('input_accessories');
        if($postAccessories){
            $postAccessoriesTab = explode('-', $postAccessories);
            foreach($postAccessoriesTab as $accessory_id){
                $accessory = JeproLabAnalyzeModel.::getAccessoryById($accessory_id);
                if(!$this->hasThisAccessory($accessory_id, $accessories) && $accessory){
                    $accessories[] = $accessory;
                }
            }
        }
       //$this->assignRef('accessories', $accessories);
        $this->product->manufacturer_name = JeproLabManufacturerModelManufacturer::getNameById($this->product->manufacturer_id);

        $categories = array();
        foreach($selected_category as $key => $category){
            $categories[] = $key;
        }
        $manufacturers = JeproLabManufacturerModelManufacturer::getManufacturers($this->context.language->lang_id);
        $categories_tree = new JeproLabCategoriesTree('associated_categories_tree', JText::_('COM_JEPROSHOP_ASSOCIATED_CATEGORIES_LABEL'));
        $categories_tree->setTreeLayout('associated_categories')->setRootCategory((int)$root->category_id)->setUseCheckBox(true)->setSelectedCategories($categories);

       //$this->assignRef('manufacturers', $manufacturers);
        $selected_category_ids = implode(',', array_keys($selected_category));
       //$this->assignRef('selected_category_ids', $selected_category_ids);
       //$this->assignRef('selected_category', $selected_category);
        $categoryId = $this->product->getDefaultCategoryId();
       //$this->assignRef('default_category_id', $categoryId);
        $category_tree = $categories_tree->render();
       //$this->assignRef('category_tree', $category_tree);
        $is_shop_context = JeproLabLaboratoryModel::getShopContext() == JeproLabLaboratoryModel::CONTEXT_SHOP;
       //$this->assignRef('is_shop_context', $is_shop_context); */
    }

    private void initSuppliersForm() {
        /*if ($this->product->product_id){
            if ($this->product_exists_in_shop){
                // Get all id_product_attribute
                $attributes = $this->product->getAttributesResume($this->context.language->lang_id);
                if (empty($attributes)){
                    $attribute = new JeproLabAttributeModelAttribute();
                    $attribute->product_id = $this->product->product_id;
                    $attribute->product_attribute_id = 0;
                    $attribute->attribute_designation = '';
                    $attributes[] = $attribute;
                }
                $product_designation = array();

                foreach ($attributes as $attribute){
                    $product_designation[$attribute->product_attribute_id] = rtrim(
                            $this->product->name[$this->context.language->lang_id] . ' - '. $attribute->attribute_designation, ' - '
                    );
                }

                // Get all available suppliers
                $suppliers = JeproLabSupplierModelSupplier::getSuppliers();

                // Get already associated suppliers
                $associated_suppliers = JeproLabProductSupplierModelProductSupplier::getSupplierCollection($this->product->product_id);

                // Get already associated suppliers and force to retrieve product declinations
                $product_supplier_collection = JeproLabProductSupplierModelProductSupplier::getSupplierCollection($this->product->product_id, false);

                $default_supplier = 0;
                if(count($suppliers) > 0){
                    foreach ($suppliers as &$supplier){
                        $supplier->is_selected = false;
                        $supplier->is_default = false;

                        foreach ($associated_suppliers as $associated_supplier){
                            if ($associated_supplier->supplier_id == $supplier->supplier_id){
                                $associated_supplier->name = $supplier->name;
                                $supplier->is_selected = true;

                                if ($this->product->supplier_id == $supplier->supplier_id){
                                    $supplier->is_default = true;
                                    $default_supplier = $supplier->supplier_id;
                                }
                            }
                        }
                    }
                }

               //$this->assignRef('attributes', $attributes);
               //$this->assignRef('suppliers', $suppliers);
               //$this->assignRef('default_supplier', $default_supplier);
               //$this->assignRef('associated_suppliers', $associated_suppliers);
               //$this->assignRef('associated_suppliers_collection', $product_supplier_collection);
               //$this->assignRef('product_designation', $product_designation);
                /*$this->assignRef(			'currencies' => Currency::getCurrencies(),

                            'link' => $this->context.link,
                            'token' => $this->token,));* /
                $default_currency_id = JeproLabSettingModelSetting::getValue('default_currency');
               //$this->assignRef('default_currency_id', $default_currency_id);

            }
            else
                $this->displayWarning($this->l('You must save the product in this shop before managing suppliers.'));
        }
        else
            $this->displayWarning($this->l('You must save this product before managing suppliers.'));*/

        //$this->tpl_form_vars['custom_form'] = $data->fetch();
    }

    private void initPriceForm() {
        if (context == null) {
            context = JeproLabContext.getContext();
        }
        /** Setting and laying out Attached file form **/
        jeproLabAnalyzePriceTabForm.setText(bundle.getString("JEPROLAB_PRICE_LABEL"));
        jeproLabAnalyzePriceLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth),
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth)
        );

        jeproLabSpecificPricePaneLayout.getColumnConstraints().addAll(
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth),
                new ColumnConstraints(labelColumnWidth), new ColumnConstraints(inputColumnWidth)
        );
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
        //GridPane.setMargin(jeproLabAnalyzeUnitPriceLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeUnitPriceWrapper, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeFinalPriceWithoutTaxLabel, new Insets(5, 10, 5, 10));
        GridPane.setMargin(jeproLabAnalyzeFinalPriceWithoutTax, new Insets(5, 10, 5, 0));
        GridPane.setMargin(jeproLabAnalyzeIsOnSale, new Insets(5, 10, 5, 0));
        //GridPane.setMargin(, new Insets(5, 10, 5, 10));
        //GridPane.setMargin(, new Insets(5, 10, 5, 10));

        if (analyze.analyze_id > 0) {
            List<JeproLabLaboratoryModel> laboratories = JeproLabLaboratoryModel.getLaboratories();
            List<JeproLabCountryModel> countries = JeproLabCountryModel.getCountries(context.language.language_id);
            List<JeproLabGroupModel> groups = JeproLabGroupModel.getGroups(context.language.language_id);
            List<JeproLabCurrencyModel> currencies = JeproLabCurrencyModel.getCurrencies();
            ResultSet attributeGroups = analyze.getAttributesGroups(context.language.language_id);
            List combinations = new ArrayList<>();
            if (attributeGroups != null) {
                /*try {
                    while(attributeGroups.next()) {
                        $combinations[$attribute -> product_attribute_id] = new JObject();
                        $combinations[$attribute -> product_attribute_id]->
                        product_attribute_id = $attribute -> product_attribute_id;
                        if (!isset($combinations[$attribute -> product_attribute_id]->attributes)){
                            $combinations[$attribute -> product_attribute_id]->attributes = '';
                        }
                        if (isset($combinations[$attribute -> product_attribute_id])) {
                            $combinations[$attribute -> product_attribute_id]->
                            attributes. = $attribute -> attribute_name + " - ";

                            $combinations[$attribute -> product_attribute_id]->price = JeproLabTools::displayPrice (
                                    JeproLabTools.convertPrice(
                                            JeproLabAnalyzeModel.getStaticPrice((int) $this -> product -> product_id, false, $attribute -> product_attribute_id),
                                            context.currency
                                    ), context.currency);
                        }
                    }
                }catch(SQLException ignored){

                }

                foreach($combinations as $combination){
                    if(isset($combination->attributes )){
                        $combination->attributes = rtrim($combination->attributes, ' - ');
                    }
                }*/
            }
            displaySpecificPriceModificationForm(context.currency, laboratories, currencies, countries, groups);
            //$this->assignRef('specific_price_modification_form', $specificPriceModificationForm);
            //$this->assignRef('ecotax_tax_excluded', $this->product->ecotax);
            //$this->applyTaxToEcotax();

            //$this->assignRef('shops', $shops);
            boolean adminOneLaboratories = context.employee.getAssociatedLaboratories().size() >= 1;
            //$this->assignRef('admin_one_shop', $admin_one_shop);
            //$this->assignRef('currencies', $currencies);
            //$this->assignRef('currency', $this->context.currency);
            //$this->assignRef('countries', $countries);
            //$this->assignRef('groups', $groups);
            //$this->assignRef('combinations', $combinations);
            boolean multiShop = JeproLabLaboratoryModel.isFeaturePublished();
            //$this->assignRef('multi_shop', $multiShop);
        } else {
            //JeproLabTools.displayWarning(500, bundle.getString("JEPROLAB_YOU_MUST_SAVE_THIS_ANALYZE_BEFORE_ADDING_SPECIFIC_PRICING_MESSAGE"));
            analyze.tax_rules_group_id = JeproLabAnalyzeModel.getMostUsedTaxRulesGroupId();
            //$this->assignRef('ecotax_tax_excluded', 0);
        }
        boolean useTax = JeproLabSettingModel.getIntValue("use_tax") > 0;
        //$this->assignRef('use_tax', $use_tax);
        boolean useEcotax = JeproLabSettingModel.getIntValue("use_eco_tax") > 0;
        //$this->assignRef('use_ecotax', $use_ecotax);
        List<JeproLabTaxRulesGroupModel> taxRulesGroups = JeproLabTaxRulesGroupModel.getTaxRulesGroups(true);
        //$this->assignRef('tax_rules_groups', $tax_rules_groups);
        Map<Integer, Float> taxesRatesByGroup = JeproLabTaxRulesGroupModel.getAssociatedTaxRatesByCountryId(context.country.country_id);
        //$this->assignRef('taxesRatesByGroup', $taxesRatesByGroup);
        float ecotaxTaxRate = JeproLabTaxModel.getAnalyzeEcotaxRate();
        //$this->assignRef('ecotaxTaxRate', $ecotaxTaxRate);
        boolean taxExcludeTaxOption = JeproLabTaxModel.excludeTaxOption();
        //$this->assignRef('tax_exclude_tax_option', $tax_exclude_tax_option);

        analyze.price = JeproLabTools.convertPrice(analyze.price, context.currency.currency_id, true, context);
        float unitPrice = 0;
        if (analyze.unit_price_ratio != 0) {
            unitPrice = JeproLabTools.roundPrice(analyze.price / analyze.unit_price_ratio, 2);
        }
        //$this->assignRef('unit_price', $unit_price); */
    }

    /*
        protected void displayLabelFields(&$obj, &$labels, $default_language, $type){
            /*$content = '';
            $type = (int)($type);
            $labelGenerated = array(JeproLabAnalyzeModel.::CUSTOMIZE_FILE => (isset($labels[JeproLabAnalyzeModel.::CUSTOMIZE_FILE]) ? count($labels[JeproLabAnalyzeModel.::CUSTOMIZE_FILE]) : 0), JeproLabAnalyzeModel.::CUSTOMIZE_TEXT_FIELD => (isset($labels[JeproLabAnalyzeModel.::CUSTOMIZE_TEXT_FIELD]) ? count($labels[JeproLabAnalyzeModel.::CUSTOMIZE_TEXT_FIELD]) : 0));

            $fieldIds = $this->product->getCustomizationFieldIds($labels, $labelGenerated, $obj);
            if (isset($labels[$type]))
                foreach ($labels[$type] as $id_customization_field => $label)
            $content .= $this->displayLabelField($label, $default_language, $type, $fieldIds, (int)($id_customization_field));
            return $content; * /
        } * /
    */
    private void displaySpecificPriceModificationForm(JeproLabCurrencyModel defaultCurrency, List<JeproLabLaboratoryModel> laboratories, List<JeproLabCurrencyModel> currencies, List<JeproLabCountryModel> countries, List<JeproLabGroupModel> groups) {
        TableView specificPriceTableView = new TableView();
        TableColumn rulesColumn, combinationColumn, labsColumn, currenciesColumn, countriesColumn, groupsColumn, customerColumn;
        TableColumn fixedPriceColumn, impactColumn, periodColumn, actionsColumn, fromColumn;
        boolean multiLab = JeproLabLaboratoryModel.isFeaturePublished();
        jeproLabAnalyzeSpecificPriceModificationLabel.setText(bundle.getString("JEPROLAB_SPECIFIC_PRICE_LABEL"));

        if (analyze != null) {
            List<JeproLabSpecificPriceModel> specificPrices = JeproLabSpecificPriceModel.getSpecificPricesByAnalyzeId(analyze.analyze_id);
            String specificPricePriorities = JeproLabSpecificPriceModel.getPriority(analyze.analyze_id);

            rulesColumn = new TableColumn(bundle.getString("JEPROLAB_RULES_LABEL"));
            combinationColumn = new TableColumn(bundle.getString("JEPROLAB_COMBINATION_LABEL"));
            fromColumn = new TableColumn(bundle.getString("JEPROLAB_FROM_LABEL"));
            currenciesColumn = new TableColumn(bundle.getString("JEPROLAB_CURRENCIES_LABEL"));
            countriesColumn = new TableColumn(bundle.getString("JEPROLAB_COUNTRIES_LABEL"));
            groupsColumn = new TableColumn(bundle.getString("JEPROLAB_GROUPS_LABEL"));
            customerColumn = new TableColumn(bundle.getString("JEPROLAB_CUSTOMER_LABEL"));
            fixedPriceColumn = new TableColumn(bundle.getString("JEPROLAB_FIXED_PRICE_LABEL"));
            impactColumn = new TableColumn(bundle.getString("JEPROLAB_IMPACT_LABEL"));
            periodColumn = new TableColumn(bundle.getString("JEPROLAB_PERIOD_LABEL"));
            actionsColumn = new TableColumn(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
            //$app = JFactory::getApplication ();
            float taxRate = analyze.getTaxesRate(JeproLabAddressModel.initialize());
            /* $tmp = array();
            foreach($shops as $shop) {
                $tmp[$shop -> shop_id] = $shop;
            }
            $shops = $tmp;            $tmp = array();
            foreach($currencies as $currency) {
                $tmp[$currency -> currency_id] = $currency;
            }
            $currencies = $tmp;          $tmp = array();
            foreach($countries as $country) {
                $tmp[$country -> country_id] = $country;
            }
            $countries = $tmp;       $tmp = array();
            foreach($groups as $group) {
                $tmp[$group -> group_id] = $group;
            }
            $groups = $tmp;*/
            specificPriceTableView.getColumns().addAll(rulesColumn, combinationColumn);

            if (multiLab) {
                labsColumn = new TableColumn(bundle.getString("JEPROLAB_LABORATORIES_LABEL"));
                specificPriceTableView.getColumns().add(labsColumn);
            }
            specificPriceTableView.getColumns().addAll(currenciesColumn, countriesColumn, groupsColumn, customerColumn);
            specificPriceTableView.getColumns().addAll(fixedPriceColumn, impactColumn, periodColumn, fromColumn, actionsColumn);
/*

        if(!is_array($specificPrices) || !count($specificPrices)){
            $content .= '<tr><td class="text-center" colspan="13" ><i class="icon-warning-sign"></i>&nbsp;';
            $content .= JText::_('COM_JEPROSHOP_NO_SPECIFIC_PRICES_MESSAGE') . '</td></tr>';
        }else{
            $i = 0;
            foreach ($specificPrices as $specificPrice){
                $currentSpecificCurrency = $currencies[($specificPrice->currency_id ? $specificPrice->currency_id : $default_currency->currency_id)];
                if ($specificPrice->reduction_type == 'percentage'){
                    $impact = '- '.($specificPrice->reduction * 100).' %';
                }elseif ($specificPrice->reduction > 0){
                    $impact = '- '. JeproLabTools::displayPrice(Tools::ps_round($specificPrice->reduction, 2), $currentSpecificCurrency);
                }else{
                    $impact = '--';
                }
                if ($specificPrice->from == '0000-00-00 00:00:00' && $specificPrice->to == '0000-00-00 00:00:00')
                    $period = JText::_('COM_JEPROSHOP_UNLIMITED_LABEL');
                else
                $period = JText::_('COM_JEPROSHOP_FROM_LABEL') .' '.($specificPrice->from != '0000-00-00 00:00:00' ? $specificPrice['from'] : '0000-00-00 00:00:00').'<br />'.$this->l('To').' '.($specificPrice['to'] != '0000-00-00 00:00:00' ? $specificPrice['to'] : '0000-00-00 00:00:00');

                if ($specificPrice->product_attribute_id){
                    $combination = new JeproLabCombinationModelCombination((int)$specificPrice->product_attribute_id);
                    $attributes = $combination->getAttributesName((int)$this->context.language->lang_id);
                    $attributes_name = '';
                    foreach ($attributes as $attribute){
                        $attributes_name .= $attribute->name .' - ';
                    }
                    $attributes_name = rtrim($attributes_name, ' - ');
                }else{
                    $attributes_name = JText::_('COM_JEPROSHOP_ALL_COMBINATIONS_LABEL');
                }

                $rule = new JeproLabSpecificPriceRuleModelSpecificPriceRule((int)$specificPrice->specific_price_rule_id);
                $rule_name = ($rule->specific_price_rule_id ? $rule->name : '--');

                if ($specificPrice->customer_id){
                    $customer = new JeproLabCustomerModelCustomer((int)$specificPrice->customer_id);
                    if (JeproLabTools::isLoadedObject($customer, 'customer_id'))
                    $customer_full_name = $customer->firstname.' '.$customer->lastname;
                    unset($customer);
                }

                if (!$specificPrice->shop_id || in_array($specificPrice->shop_id, JeoroshopShopModelShop::getContextListShopID())){
                    $content .= '<tr class="row_'.($i % 2 ? '0' : '1').'"><td>'.$rule_name.'</td><td>'.$attributes_name.'</td>';

                    $can_delete_specific_prices = true;
                    if (JeproLabLaboratoryModel::isFeaturePublished()){
                        $sp_shop_id = $specificPrice->shop_id;
                        $can_delete_specific_prices = (count($this->context.employee->getAssociatedShops()) > 1 && !$sp_shop_id) || $sp_shop_id;
                        $content .= '<td>'.($sp_shop_id ? $shops[$sp_shop_id]['name'] : JText::_('COM_JEPROSHOP_ALL_SHOPS_LABEL')).'</td>';
                    }
                    $price = JeproLabTools::roundPrice($specificPrice->price, 2);
                    $fixed_price = ($price == JeproLabTools::roundPrice($this->product->price, 2) || $specificPrice->price == -1) ? '--' : JeproLabTools::displayPrice($price, $current_specific_currency);
                    $content .= '<td>'.($specificPrice->currency_id ? $currencies[$specificPrice->currency_id]->name : JText::_('COM_JEPROSHOP_ALL_CURRENCIES_LABEL')).'</td>';
                    $content .= '<td>'.($specificPrice->country_id ? $countries[$specificPrice->country_id]->name : JText::_('COM_JEPROSHOP_ALL_COUNTRIES_LABEL')).'</td>';
                    $content .= '<td>'.($specificPrice->group_id ? $groups[$specificPrice->group_id]->name : JText::_('COM_JEPROSHOP_ALL_GROUPS_LABEL')).'</td>';
                    $content .= '<td title="' . JText::_('COM_JEPROSHOP_ID_LABEL') . ' '.$specificPrice->customer_id .'">'.(isset($customer_full_name) ? $customer_full_name : JText::_('COM_JEPROSHOP_ALL_CUSTOMERS_LABEL')).'</td>';
                    $content .= '<td>'.$fixed_price.'</td><td>'.$impact.'</td><td>'.$period.'</td><td>'.$specificPrice->from_quantity .'</th>';
                    $content .= '<td>'.((!$rule->specific_price_rule_id && $can_delete_specific_prices) ? '<a class="btn btn-default" name="delete_link" href="'. JRoute::_('index.php?option=com_jeproshop&view=price&product_id='.(int)$app->input->get('product_id').'&task=delete_specific_price&specific_price_id='.(int)($specificPrice->specific_price_id).'&' . JSession::getFormToken() .'=1') . '"><i class="icon-trash"></i></a>': '').'</td>';
                    $content .= '</tr>';
                    $i++;
                    unset($customer_full_name);
                }
            }
        }
        $content .= '</tbody></table>';
        // Not use id_customer
        if ($specificPricePriorities[0] == 'customer_id')
            unset($specificPricePriorities[0]);
        // Reindex array starting from 0
        $specificPricePriorities = array_values($specificPricePriorities);

        $content .= '<div class="panel"><div class="panel-title" >'. JText::_('Priority management').'</div><div class="panel-content well" ><div class="alert alert-info">';
        $content .= JText::_('Sometimes one customer can fit into multiple price rules. Priorities allow you to define which rule applies to the customer.') . '</div>';
        $content .= '<div class="input-group" ><select id="jform_specific_price_priority_1" name="price_field[specific_price_priority[]]" class="middle_size" ><option value="shop_id"';
        $content .= ($specificPricePriorities[0] == 'shop_id' ? ' selected="selected"' : '').'>'. JText::_('COM_JEPROSHOP_SHOP_LABEL').'</option><option value="currency_id"';
        $content .= ($specificPricePriorities[0] == 'currency_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_CURRENCY_LABEL').'</option><option value="country_id"';
        $content .= ($specificPricePriorities[0] == 'country_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_COUNTRY_LABEL').'</option><option value="group_id"';
        $content .= ($specificPricePriorities[0] == 'group_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_GROUP_LABEL').'</option></select>&nbsp;<span class="';
        $content .= 'input-group-addon"><i class="icon-chevron-right"></i></span>&nbsp;<select name="price_field[specific_price_priority[]]" class="middle_size" ><option value="shop_id"';
        $content .= ($specificPricePriorities[1] == 'shop_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_SHOP_LABEL').'</option><option value="currency_id"';
        $content .= ($specificPricePriorities[1] == 'currency_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_CURRENCY_LABEL').'</option><option value="country_id"';
        $content .= ($specificPricePriorities[1] == 'country_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_COUNTRY_LABEL').'</option><option value="group_id"';
        $content .= ($specificPricePriorities[1] == 'group_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_GROUP_LABEL').'</option></select>&nbsp;<span class="';
        $content .= 'input-group-addon"><i class="icon-chevron-right"></i></span>&nbsp;<select name="price_field[specific_price_priority[]]" class="middle_size" ><option value="shop_id"';
        $content .= ($specificPricePriorities[2] == 'shop_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_SHOP_LABEL').'</option><option value="currency_id"';
        $content .= ($specificPricePriorities[2] == 'currency_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_CURRENCY_LABEL').'</option><option value="country_id"';
        $content .= ($specificPricePriorities[2] == 'country_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_COUNTRY_LABEL').'</option><option value="group_id"';
        $content .= ($specificPricePriorities[2] == 'group_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_GROUP_LABEL').'</option></select><span class="';
        $content .= 'input-group-addon"><i class="icon-chevron-right"></i></span>&nbsp;<select name="price_field[specific_price_priority[]]" class="middle_size" ><option value="shop_id"';
        $content .= ($specificPricePriorities[3] == 'shop_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_SHOP_LABEL').'</option><option value="currency_id"';
        $content .= ($specificPricePriorities[3] == 'currency_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_CURRENCY_LABEL').'</option><option value="country_id"';
        $content .= ($specificPricePriorities[3] == 'country_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_COUNTRY_LABEL').'</option><option value="group_id"';
        $content .= ($specificPricePriorities[3] == 'group_id' ? ' selected="selected"' : '').'>'.JText::_('COM_JEPROSHOP_GROUP_LABEL').'</option></select></div></div></div>';
        $content .= '<p class="checkbox"><label for="jform_specific_price_priority_to_all"><input type="checkbox" name="price_field[specific_price_priority_to_all]" id="jform_specific_';
        $content .= 'price_priority_to_all" />'. JText::_('Apply to all products').'</label></p>';
        /*<div class="form-group">
            <label class="control-label col-lg-3" for="specificPricePriority1">'.$this->l('Priorities').'</label>
             col-lg-9">



            </div>
        </div>
        <div class="form-group">
            <div class="col-lg-9 col-lg-offset-3">

            </div>
        </div>
        <div class="panel-footer">
                <a href="'.$this->context.link->getAdminLink('AdminProducts').'" class="btn btn-default"><i class="process-icon-cancel"></i> '.$this->l('Cancel').'</a>
                <button id="product_form_submit_btn"  type="submit" name="submitAddproduct" class="btn btn-default pull-right"><i class="process-icon-save"></i> '.$this->l('Save') .'</button>
                <button id="product_form_submit_btn"  type="submit" name="submitAddproductAndStay" class="btn btn-default pull-right"><i class="process-icon-save"></i> '.$this->l('Save and stay') .'</button>
            </div>
        </div>
        '; * /

        $content .= '<script type="text/javascript">var currencies = new Array(); currencies[0] = new Array(); ';
        $content .= 'currencies[0]["sign"] = "'.$default_currency->sign.'"; currencies[0]["format"] = '.$default_currency->format.'; ';
        foreach ($currencies as $currency){
            $content .= '
            currencies['. $currency->currency_id .'] = new Array();
            currencies['. $currency->currency_id .']["sign"] = "'. $currency->sign . '";
            currencies['. $currency->currency_id .']["format"] = '.$currency->format . ';';
        }
        $content .= '</script>';
        return $content; */
            jeproLabAnalyzeSpecificPriceModification.getChildren().add(specificPriceTableView);
        }
    }

    private void getCombinationImages() {
        /*if (!$this->loadObject(true)){ return; }
        $content = 'var combination_images = new Array();';
        $allCombinationImages = $this->product->getCombinationImages($this->context.language->lang_id);
        if(!$allCombinationImages){ return $content; }

        foreach ($allCombinationImages as $product_attribute_id => $combination_images){
            $i = 0;
            $content .= 'combination_images['.(int)$product_attribute_id.'] = new Array();';
            foreach ($combination_images as $combination_image){
                $content .= 'combination_images['.(int)$product_attribute_id.']['.$i++.'] = '.(int)$combination_image->image_id .';';
            }
        }
        return $content; */
    }

    //protected void productMultiShopCheckFields($product_tab){
        /*$scriptReturned = '';
        if(isset($this->display_multishop_checkboxes) && $this->display_multishop_checkboxes){
            $scriptReturned .= '<input style="float: none;" /><input type="checkbox" style="vertical-align:text-bottom" ';
            $scriptReturned .=' onclick="$(\'#jform_product_tab_content_' . $product_tab . ' input[name^=\'multi_shop_check[\']\').';
            $scriptReturned .= 'attr(\'checked\', this.checked); ProductMultiShop.checkAll' . $product_tab . '(); " />'; //]
            $scriptReturned .= JText::_('COM_JEPROSHOP_PRODUCT_PAGE_EDITING_MESSAGE') . '</label>';
        }
        return $scriptReturned; */
    //}

    //protected void productMultiShopCheckbox($field, $type){
        /*$scriptReturned = '';
        if(isset($this->display_multishop_checkboxes) && $this->display_multishop_checkboxes){
            if(isset($this->multilang) && $this->multilang){
                if(isset($this->checkbox_only)){
                    foreach($this->languages as $language){
                        $scriptReturned .= '<input type="checkbox" name="multi_shop_check[' . $field . '][' . $language->lang_id . ']"';
                        $scriptReturned .= 'value="1" onclick="ProductMultiShop.checkField(this.checked, \'' . $field . '_' . $language->lang_id;
                        $scriptReturned .= '\', \'' . $type . '\' )" ';
                        if(!empty($this->multiShopCheck[$field][$language->lang_id])){
                            $scriptReturned .= 'checked="checked" ';
                        }
                        $scriptReturned .= ' />';
                    }
                }else{
                    $scriptReturned .= '<div class="multi_shop_product_checkbox" >';
                    foreach($this->languages as $language){
                        $scriptReturned .= '<div class="multi_shop_lang_' . $language->lang_id . '" ';
                        if(!$language->is_default){
                            $scriptReturned .= 'style="display:none; "';
                        }
                        $scriptReturned .= ' ><input type="checkbox" name="jform[multi_shop_check[' . $field . '][' . $language->lang_id . ']]';
                        $scriptReturned .= ' value="1" onclick="ProductMultiShop.checkField(this.checked, \'' . $field .'_' . $language->lang_id;
                        $scriptReturned .= '\', \'' . $type . '\' ); "';
                        if(!empty($this->multishop_check[$field][$language->lang_id])){
                            $scriptReturned .= ' checked="checked" ';
                        }
                        $scriptReturned .= '/></div>';
                    }
                    $scriptReturned .= '</div>';
                }
            }else{
                if(isset($this->checkbox_only)){
                    $scriptReturned .= '<input type="checkbox" name="jform[multi_shop_check[' . $field . ']" value="1" ';
                    $scriptReturned .= ' onclick="ProductMultiShop.checkField(this.checked, \'' . $field . '\', \'' . $type .'\' ); "';
                    if(!empty($this->multishop_check[$field])){
                        $scriptReturned .= ' checked="checked" ';
                    }
                    $scriptReturned .= '/>';
                }else{
                    $scriptReturned .= '<div class="multi_shop_product_checkbox"><input type="checkbox" name="jform[multi_shop_check[';
                    $scriptReturned .= $field . ']" value="1" onclick="ProductMultiShop.checkField(this.checked, \'' . $field . '\', \'' . $type .'\' ); "';
                    if(!empty($this->multishop_check[$field])){
                        $scriptReturned .= ' checked="checked" ';
                    }
                    $scriptReturned .= ' /></div>';
                }
            }
        }
        return $scriptReturned; */
    //}
/*
    public function renderAttributesList($product, $currency){

    } */

    /*private boolean loadObject(){
        return loadObject(false);
    }*/

    private boolean loadObject(boolean option) {
        int analyzeId = JeproLab.request.getRequest().containsKey("analyze_id") ? Integer.parseInt(JeproLab.request.getRequest().get("analyze_id")) : 0;

        boolean isLoaded = false;
        if (analyzeId > 0) {
            if (analyze == null) {
                analyze = new JeproLabAnalyzeModel(analyzeId);
            }

            if (analyze.analyze_id <= 0) {
                JeproLabTools.displayError(500, bundle.getString("JEPROLAB_ANALYZE_NOT_FOUND_MESSAGE"));
                isLoaded = false;
            } else {
                isLoaded = true;
            }
        } else if (option) {
            if (analyze == null) {
                analyze = new JeproLabAnalyzeModel();
            }
        } else {
            JeproLabTools.displayError(500, bundle.getString("JEPROSHOP_ANALYZE_DOES_NOT_EXIST_MESSAGE"));
            isLoaded = false;
        }

        //specified
        if (isLoaded && analyze.analyze_id > 0) {
            if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.LAB_CONTEXT && JeproLabLaboratoryModel.isFeaturePublished() && !analyze.isAssociatedToLaboratory()) {
                analyze = new JeproLabAnalyzeModel(analyze.analyze_id, false, 0, analyze.default_laboratory_id);
            }
            analyze.loadStockData();
        }
        return isLoaded;
    }

    @Override
    public void updateToolBar() {
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveAnalyzeBtn = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (analyze.analyze_id > 0) {
            saveAnalyzeBtn.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveAnalyzeBtn.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelBtn = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));
        saveAnalyzeBtn.setOnMouseClicked(evt -> {
            String analyzeVisibility;
            if (jeproLabAnalyzeVisibility.getValue().equals(bundle.getString("JEPROLAB_CATALOG_LABEL"))) {
                analyzeVisibility = "catalog";
            } else if (jeproLabAnalyzeVisibility.getValue().equals(bundle.getString("JEPROLAB_SEARCH_LABEL"))) {
                analyzeVisibility = "search";
            } else if (jeproLabAnalyzeVisibility.getValue().equals(bundle.getString("JEPROLAB_NONE_LABEL"))) {
                analyzeVisibility = "none";
            } else {
                analyzeVisibility = "both";
            }

            String analyzeRedirect;
            if (jeproLabAnalyzeRedirect.getValue().equals(bundle.getString("JEPROLAB_301_LABEL"))) {
                analyzeRedirect = "301";
            } else if (jeproLabAnalyzeRedirect.getValue().equals(bundle.getString("JEPROLAB_302_LABEL"))) {
                analyzeRedirect = "302";
            }else{
                analyzeRedirect = "404";
            }

            String post = "reference=" + jeproLabAnalyzeReference.getText() + "&ean13=" + jeproLabAnalyzeEan13.getText() + "&upc=" + jeproLabAnalyzeUpc.getText();
            post += "&published=" + (jeproLabAnalyzePublished.isSelected() ? "1" : "0") + "&redirect_type=" + jeproLabAnalyzeVisibility.getValue() + "&available_for_order=";
            post += (jeproLabAnalyzeAvailableForOrder.isSelected() ? "1" : "0") + "&show_price=" + (jeproLabAnalyzeShowPrice.isSelected() ? "1" : "0") + "&visibility=";
            post += analyzeVisibility + "&redirect_id=" + analyzeRedirect + "&delay=" + (jeproLabAnalyzeDelay.getText().equals("") ? 0 : jeproLabAnalyzeDelay.getText());

            JeproLab.request.setPost(post);

            if (analyze.analyze_id > 0) {
                analyze.update();
            } else {
                //JeproLabAnalyzeModel
                int savedId = analyze.save();
            }
        });
        commandWrapper.getChildren().addAll(saveAnalyzeBtn, cancelBtn);
    }

    private void addEventListener(){
        jeproLabAnalyzeDelay.addEventFilter(KeyEvent.KEY_TYPED , JeproLabTools.numericValidation(2));
        jeproLabAnalyzeUpc.addEventFilter(KeyEvent.KEY_TYPED, JeproLabTools.upcValidation());
    }
}