package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JeproLabImageModel extends JeproLabModel {
    /** @var int Image ID */
    public int image_id;

    /** @var int analyze ID */
    public int analyze_id;

    public int language_id;

    /** @var int Position used to order images of the same product */
    public int position;

    /** @var bool Image is cover */
    public boolean cover;

    /** @var string Legend */
    public Map<String, String> legend = new HashMap<>();

    /** @var string image extension */
    public String image_format = "jpg";

    /** @var string path to index.php file to be copied to new image folders * /
    public String source_index;

    /** @var string image folder */
    protected String folder;
    
    protected String image_dir;

    /** @var string image path without extension */
    protected String existing_path;

    /** @var int access rights of created folders (octal) */
    protected static int access_rights = 0775;

    /*
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'image',
                    'primary' => 'id_image',
                    'multilang' => true,
                    'fields' => array(
                    'id_product' => array('type' => self::TYPE_INT, 'shop' => 'both', 'validate' => 'isUnsignedId', 'required' => true),
    'position' =>    array('type' => self::TYPE_INT, 'validate' => 'isUnsignedInt'),
    'cover' =>        array('type' => self::TYPE_BOOL, 'allow_null' => true, 'validate' => 'isBool', 'shop' => true),
    'legend' =>    array('type' => self::TYPE_STRING, 'lang' => true, 'validate' => 'isGenericName', 'size' => 128),
    ),
            );
*/
    protected static Map<String, Map<String, Integer>> _cacheGetSize = new HashMap<>();

    public JeproLabImageModel(){
        this(0, 0);
    }

    public JeproLabImageModel(int imageId){
        this(imageId, 0);
    }

    public JeproLabImageModel(int imageId, int langId){
        if(langId > 0){
            this.language_id = JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if(imageId > 0){
            /** load category from data base if id provided **/
            String cacheKey = "jeprolab_model_image_" + imageId + "_" + langId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_image") + " AS image ";
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_image_lang") + " AS image_lang ON ";
                    query += "(image." + dataBaseObject.quoteName("image_id") + " = image_lang." + dataBaseObject.quoteName("image_id");
                    query += " AND image." + dataBaseObject.quoteName("lang_id") + " = " + langId + ") ";
                }
                query += "WHERE image." + dataBaseObject.quoteName("image_id") + " = " + imageId;

                dataBaseObject.setQuery(query);
                ResultSet imageSet = dataBaseObject.loadObject();

                try {
                    if (imageSet.next()){
                        this.image_id = imageSet.getInt("image_id");
                        this.analyze_id = imageSet.getInt("analyze_id");
                        this.position = imageSet.getInt("position");
                        this.cover = imageSet.getInt("cover") > 0;
                        if (langId <= 0 ){
                            query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_image_lang") + " WHERE image_id = " + imageId;

                            dataBaseObject.setQuery(query);
                            ResultSet imageLangSet = dataBaseObject.loadObject();
                            String legend;
                            int languageId;
                            Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();
                            Iterator langIt = languages.entrySet().iterator();
                            while(langIt.hasNext()){
                                Map.Entry lang = (Map.Entry)langIt.next();
                                JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
                                languageId = imageLangSet.getInt("lang_id");
                                legend = imageSet.getString("legend");
                                if(langId == language.language_id){
                                    this.legend.put("lang_" + languageId, legend);
                                }
                            }
                        }else{
                            this.language_id = imageSet.getInt("lang_id");
                            this.legend.put("lang_" + langId, imageSet.getString("legend"));
                        }

                        JeproLabCache.getInstance().store(cacheKey, this);
                    }
                }catch (SQLException ignored){

                }
            }else {
                JeproLabImageModel image = (JeproLabImageModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.image_id = image.image_id;
                //this. = image.
                this.legend = image.legend;
            }
        }

        this.image_dir = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY;
        //this.source_index = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.'index.php';
    }
/*
    public boolean add($autodate = true, $null_values = false) {
        if (this.position <= 0) {
            this.position = JeproLabImageModel.getHighestPosition(this.analyze_id) + 1;
        }

        return parent::add($autodate, $null_values);
    }

    public function update($null_values = false){

        return parent::update($null_values);
    }

    public boolean delete(){
        if (!parent::delete()) {
        return false;
        }

        if (this.hasMultiLaboratoryEntries()) {
            return true;
        }

        if (!this.deleteAnalyzeAttributeImage() || !this.deleteImage()) {
            return false;
        }

        // update positions
        Db::getInstance()->execute('SET @position:=0', false);
        Db::getInstance()->execute('UPDATE `'._DB_PREFIX_.'image` SET position=(@position:=@position+1)
        WHERE `id_product` = '.(int)this.id_product.' ORDER BY position ASC');

        return true;
    }

    /**
     * Return first image (by position) associated with a product attribute
     *
     * @param labId ID
     * @param langId Language ID
     * @param analyzeId Analyze ID
     * @param analyzeAttributeId Analyze Attribute ID
     * @return array
     * /
    public static function getBestImageAttribute(int labId, int langId, int analyzeId, int analyzeAttributeId){
        String cacheKey = "jeprolab_image_get_best_image_attribute_" + analyzeId + "_" + analyzeAttributeId + "_" + langId + "_" + labId;

        if (!JeproLabCache.getInstance().isStored(cacheKey)) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT image_shop." + staticDataBaseObject.quoteName("image_id") + " AS image_id, image_lang." + staticDataBaseObject.quoteName("legend");
            query += " FROM " + staticDataBaseObject.quoteName("#__jeprolab_image") + " AS image INNER JOIN " + staticDataBaseObject.quoteName("#__jeprolab_image_lab");
            query += " AS image_shop ON (image.image_id = image_lab.image_id AND image_lab.lab_id = " + labId  + ") INNER JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_image") + " AS analyze_attribute_image ON (analyze_attribute_image.";
            query += staticDataBaseObject.quoteName("image_id") + " = image." + staticDataBaseObject.quoteName("image_id") + " AND analyze_attribute_image.";
            query += staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId + ") LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_image_lang");
            query += " AS image_lang ON (image_lab." + staticDataBaseObject.quoteName("image_id") + " = image_lang." + staticDataBaseObject.quoteName("image_id") + " AND image_lang.";
            query += staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE image." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId ;
            query += " ORDER BY image." + staticDataBaseObject.quoteName("position") + " ASC";

            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObject();
            try{
                if(resultSet.next()){
                    //todo retrieve fie
                }
            }catch(SQLException ignored){

            }

            JeproLabCache.getInstance().store(cacheKey, $row);
        } else {
            $row = JeproLabCache.getInstance().retrieve(cacheKey);
        }
        return $row;
    }

    public static List<JeproLabImageModel> getImages(int langId, int analyzeId){
        return getImages(langId, analyzeId, 0);
    }

    /**
     * Return available images for a product
     *
     * @param langId Language ID
     * @param analyzeId Analyze ID
     * @param analyzeAttributeId Analyze Attribute ID
     * @return array Images
     * /
    public static List<JeproLabImageModel> getImages(int langId, int analyzeId, int analyzeAttributeId){
        String attributeFilter = (analyzeAttributeId > 0 ? " AND attribute_image." + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_image") +  " AS image LEFT JOIN " ;
        query += staticDataBaseObject.quoteName(" #__jeprolab_image_lang") + " AS image_lang ON (image." + staticDataBaseObject.quoteName("image_id");
        query += " = image_lang." + staticDataBaseObject.quoteName("image_id") + ")";

        if (analyzeAttributeId > 0) {
            query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_image") + " AS attribute_image ON (image.";
            query += staticDataBaseObject.quoteName("image_id") + " = attribute_image." + staticDataBaseObject.quoteName("image_id") + ")";
        }

        query += " WHERE image." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND image_lang." ;
        query += staticDataBaseObject.quoteName("lang_id") + " = " + langId + attributeFilter  + " ORDER BY image.";
        query += staticDataBaseObject.quoteName("position") + " ASC";

        staticDataBaseObject.setQuery(query);
        ResultSet imageSet =staticDataBaseObject.loadObject();
        List<JeproLabImageModel> imageList = new ArrayList<>();
        try{
            JeproLabImageModel image;
            while(imageSet.next()){
                image = new JeproLabImageModel();
                if(analyzeAttributeId > 0){

                }
                imageList.add(image);
            }
        }catch (SQLException ignored){

        }
        return imageList;
    }

    public static boolean hasImages(int langId, int analyzeId){
        return hasImages(langId, analyzeId, 0);
    }

    /**
     * Check if a product has an image available
     *
     * @param langId Language ID
     * @param analyzeId Analyze ID
     * @param analyzeAttributeId Analyze Attribute ID
     * @return bool
     * /
    public static boolean hasImages(int langId, int analyzeId, int analyzeAttributeId){
        String attributeFilter = (analyzeAttributeId > 0 ? " AND attribute_image." + staticDataBaseObject.quoteName("analyze_attribute_id") + " = " + analyzeAttributeId : "");
        String query = "SELECT 1 FROM " + staticDataBaseObject.quoteName("#__jeprolab_image") + " AS image LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_image_lang");
        query += " AS image_lang ON (image." + staticDataBaseObject.quoteName("image_id") + " = image_lang." + staticDataBaseObject.quoteName("image_id") + ")";

        if (analyzeAttributeId > 0) {
            query += " LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_attribute_image") + " AS attribute_image ON (image.";
            query += staticDataBaseObject.quoteName("image_id") + " = attribute_image." + staticDataBaseObject.quoteName("image_id") + ")";
        }

        query += " WHERE image." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND image_lang." + staticDataBaseObject.quoteName("lang_id");
        query += " = " + langId + attributeFilter;
        staticDataBaseObject.quoteName(query);
        return staticDataBaseObject.loadValue("1") > 0;
    }

    /**
     * Return Images
     *
     * @return array Images
     * /
    public static List getAllImages(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT " + staticDataBaseObject.quoteName("image_id") + ", " + staticDataBaseObject.quoteName("analyze_id") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_image") + " ORDER BY " + staticDataBaseObject.quoteName("image_id") + " ASC ";

        staticDataBaseObject.setQuery(query);
        ResultSet imageSet = staticDataBaseObject.loadObject();
        List<JeproLabImageModel> imageList = new ArrayList<>();
        try{
            JeproLabImageModel image;
            while(imageSet.next()){
                image = new JeproLabImageModel();
                image.image_id = imageSet.getInt("image_id");
                image.analyze_id = imageSet.getInt("analyze_id");
                imageList.add(image);
            }
        }catch (SQLException ignored){

        }
        return imageList;
    }

    /**
     * Return number of images for a product
     *
     * @param analyzeId ID
     * @return int number of images
     * /
    public static int getImagesTotal(int analyzeId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT COUNT(" + staticDataBaseObject.quoteName("image_id") + ") AS total FROM "  + staticDataBaseObject.quoteName("#__jeprolab_image");
        query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
        staticDataBaseObject.setQuery(query);

        return (int)staticDataBaseObject.loadValue("total");
    }

    /**
     * Return highest position of images for a product
     *
     * @param analyzeId ID
     * @return int highest position of images
     * /
    public static int getHighestPosition(int analyzeId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT MAX(" + staticDataBaseObject.quoteName("position") + ") AS max FROM " + staticDataBaseObject.quoteName("#__jeprolab_image");
        query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
        staticDataBaseObject.setQuery(query);
        return (int)staticDataBaseObject.loadValue("max");
    }

    /**
     * Delete product cover
     *
     * @param analyzeId ID
     * @return bool result
     * /
    public static boolean deleteCover(int analyzeId){
        if (analyzeId <= 0) {
            JeproLabTools.displayError(500, "");
        }

        if (file_exists(_PS_TMP_IMG_DIR_.'product_'.$id_product.'.jpg')) {
        unlink(_PS_TMP_IMG_DIR_.'product_'.$id_product.'.jpg');
        }

        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_image") + " SET " + staticDataBaseObject.quoteName("cover");
        query += " = NULL WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
        staticDataBaseObject.setQuery(query);
        boolean result = staticDataBaseObject.query();

        query = "UPDATE " + staticDataBaseObject.quoteName("#__jeprolab_image_lab") + " AS image_lab SET image_lab." + staticDataBaseObject.quoteName("cover");
        query += "= NULL WHERE image_lab.lab_id IN (" .implode(',', array_map('intval', Shop::getContextListShopID()));
        query += ") AND image_lab." + staticDataBaseObject.quoteName("analyze_id") + " = " + analyzeId;
        staticDataBaseObject.setQuery(query);

        result &= staticDataBaseObject.query();
        return result;
    }

    /*
     *Get product cover
     *
     * @param int $id_product Product ID
     * @return bool result
     * /
    public static boolean getCover(int analyzeId){
        return Db::getInstance()->getRow('
            SELECT * FROM `'._DB_PREFIX_.'image_shop` image_shop
        WHERE image_shop.`id_product` = '.(int)$id_product.'
        AND image_shop.`cover`= 1');
    }

    /**
     *Get global product cover
     *
     * @param int $id_product Product ID
     * @return bool result
     * /
    public static function getGlobalCover(int analyzeId){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_image") + " AS image WHERE image.";
        query += staticDataBaseObject.quote("analyze_id") + " = " + analyzeId + " AND image." + staticDataBaseObject.quoteName("cover") + " = 1 ";
        staticDataBaseObject.setQuery(query);
        return Db::getInstance()->getRow('
            );
    }

    /**
     * Copy images from a product to another
     *
     * @param int $id_product_old Source product ID
     * @param bool $id_product_new Destination product ID
     * /
    public static function duplicateAnalyzeImages(int oldAnalyzeId, int newAnalyzeId, $combination_images){
        List imagesTypes = JeproLabImageTypeModel.getImagesTypes("products");
        String query = "SELECT " + staticDataBaseObject.quoteName("image_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_image");
        query += " WHERE " + staticDataBaseObject.quoteName("analyze_id") + " = " + oldAnalyzeId;

        staticDataBaseObject.setQuery(query);

        ResultSet resultSet = staticDataBaseObject.loadObject();
        try {
            while(resultSet.next()) {
                JeproLabImageModel oldImage = new JeproLabImageModel(resultSet.getInt("image_id"));
                JeproLabImageModel newImage = clone oldImage;
                newImage.image_id = 0;
                newImage.analyze_id = newAnalyzeId;

                // A new id is generated for the cloned image when calling add()
                if (newImage.add()){
                    String newPath = newImage.getPathForCreation();
                    String waterMarkHash = JeproLabSettingModel.getStringValue("water_mark_hash");
                    int legacyImage = JeproLabSettingModel.getIntValue("legacy_image");

                    for(JeproLabImageTypeModel imageType  : imagesTypes) {
                        if (file_exists(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY + oldImage.getExistingImagePath() + "_" + imageType.name + ".jpg")){
                            if (legacyImage <= 0){
                                newImage.createImageFolder();
                            }
                            JeproLabTools.copy(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY + oldImage.getExistingImagePath()+ "_" + imageType.name + ".jpg", newPath + "_" + imageType.name + ".jpg");
                            if (!waterMarkHash.equals("")){
                                JeproLabTools.copy(
                                        JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY + oldImage.getExistingImagePath() + " _" +imageType.name + "_" + waterMarkHash + ".jpg",
                                        newPath + "_" + imageType.name + "_" + waterMarkHash + ".jpg");
                            }
                        }
                    }

                    if (file_exists(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY + oldImage.getExistingImagePath() + ".jpg")){
                        JeproLabTools.copy(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY + oldImage.getExistingImagePath() + ".jpg", newPath + ".jpg");
                    }

                    JeproLabImageModel.replaceAttributeImageAssociationId($combination_images, oldImage.image_id, newImage.image_id);

                    // Duplicate shop associations for images
                    newImage.duplicateLaboratories(oldAnalyzeId);
                } else {
                    return false;
                }
            }
        }catch (SQLException ignored){

        }
        return JeproLabImageModel.duplicateAttributeImageAssociations($combination_images);
    }

    protected static function replaceAttributeImageAssociationId(&$combination_images, $saved_id, int imageId){
        if (!isset($combination_images['new']) || !is_array($combination_images['new'])) {
            return;
        }
        foreach ($combination_images['new'] as analyzeAttributeId => $image_ids) {
        foreach ($image_ids as $key => $image_id) {
            if ((int)$image_id == (int)$saved_id) {
                $combination_images['new'][analyzeAttributeId][$key] = (int)$id_image;
            }
        }
    }
    }

    /*
     * Duplicate product attribute image associations
     * @param int analyzeAttributeId_old
     * @return bool
     * /
    public static function duplicateAttributeImageAssociations($combination_images)
    {
        if (!isset($combination_images['new']) || !is_array($combination_images['new'])) {
            return true;
        }
        $query = 'INSERT INTO `'._DB_PREFIX_.'product_attribute_image` (`id_product_attribute`, `id_image`) VALUES ';
        foreach ($combination_images['new'] as analyzeAttributeId => $image_ids) {
        foreach ($image_ids as $image_id) {
            $query .= '('.(int)analyzeAttributeId.', '.(int)$image_id.'), ';
        }
    }
        $query = rtrim($query, ', ');
        return DB::getInstance()->execute($query);
    }

    /**
     * Change an image position and update relative positions
     *
     * @param way position is moved up if 0, moved down if 1
     * @param position new position of the moved image
     * @return boolean success
     * /
    public boolean updatePosition(int way, int position){
        if (this.image_id <= 0 || position <= 0) {
            return false;
        }
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_image") + " SET " + dataBaseObject.quoteName("position") + " = ";
        query += dataBaseObject.quoteName("position") + (way > 0 ? "- 1" : "+ 1" ) + " WHERE " + dataBaseObject.quoteName("position") ;
        query += (way > 0 ? " > " + this.position + " AND " + dataBaseObject.quoteName("position") + " <= " + position :  " < " + this.position + " AND " + dataBaseObject.quoteName("position") + " >= " + position);
        query += " AND " + dataBaseObject.quoteName("analyze_id") + " = " + this.analyze_id;
        dataBaseObject.setQuery(query);
        boolean result = dataBaseObject.query();

        // < and > statements rather than BETWEEN operator
        // since BETWEEN is treated differently according to databases

        query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_image") + " SET " + dataBaseObject.quoteName("position") + " = " + position;
        query += " WHERE " + dataBaseObject.quoteName("image_id") + " = " + this.image_id;
        dataBaseObject.setQuery(query);
        result &= dataBaseObject.query();

        return result;
    }

    public static Map<String,Integer> getSize(String type){
        if (!JeproLabImageModel._cacheGetSize.containsKey(type) || JeproLabImageModel._cacheGetSize.get(type) == null){
            if (staticDataBaseObject == null) {
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("width") + ", " + staticDataBaseObject.quoteName("height") + " FROM ";
            query += staticDataBaseObject.quoteName("__jeprolab_image_type") + " WHERE " + staticDataBaseObject.quoteName("name") + " = ";
            query += staticDataBaseObject.quote(type);
            staticDataBaseObject.setQuery(query);
            ResultSet sizeSet = staticDataBaseObject.loadObject();

            try {
                if (sizeSet.next()) {
                    Map<String, Integer> entry = new HashMap<>();
                    entry.put("width", sizeSet.getInt("width"));
                    entry.put("height", sizeSet.getInt("height"));
                    JeproLabImageModel._cacheGetSize.put(type, entry);
                }
            } catch (SQLException ignored) {

            }
        }
        return JeproLabImageModel._cacheGetSize.get(type);
    }

    public static int getWidth($params, &$smarty){
        return JeproLabImageModel.getSize($params['type']).get("width");
    }

    public static int getHeight($params, &$smarty){
        return JeproLabImageModel.getSize($params['type']).get("height");
    }

    /**
     * Clear all images in tmp dir
     * /
    public static function clearTmpDir() {
        foreach(scandir(_PS_TMP_IMG_DIR_)as $d) {
            if (preg_match('/(.*)\.jpg$/', $d)) {
                unlink(_PS_TMP_IMG_DIR_.$d);
            }
        }
    }

    /**
     * Delete Image - Product attribute associations for this image
     * /
    public boolean deleteAnalyzeAttributeImage(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute_image") ;
        query += " WHERE " + dataBaseObject.quoteName("image_id") + " = " + this.image_id;
        dataBaseObject.setQuery(query);
        return dataBaseObject.query();
    }

    public boolean deleteImage(){
        return  deleteImage(false);
    }

    /**
     * Delete the product image from disk and remove the containing folder if empty
     * Handles both legacy and new image filesystems
     * /
    public boolean deleteImage(boolean forceDelete){
        if (this.image_id <= 0) {
            return false;
        }

        // Delete base image
        if (file_exists(this.image_dir+ this.getExistingImagePath() + '.' + this.image_format)) {
            unlink(this.image_dir + this.getExistingImagePath()+ "." + this.image_format);
        } else {
            return false;
        }

        List filesToDelete = new ArrayList<>();

        // Delete auto-generated images
        String imageTypes = JeproLabImageTypeModel.getImagesTypes();
        foreach ($image_types as $image_type) {
            $files_to_delete[] = this.image_dir.this.getExistingImagePath().'-'.$image_type['name'].'.'.this.image_format;
            if (Configuration::get('WATERMARK_HASH')) {
                $files_to_delete[] = this.image_dir.this.getExistingImagePath().'-'.$image_type['name'].'-'.Configuration::get('WATERMARK_HASH').'.'.this.image_format;
            }
        }

        // Delete watermark image
        $files_to_delete[] = this.image_dir.this.getExistingImagePath().'-watermark.'.this.image_format;
        // delete index.php
        $files_to_delete[] = this.image_dir.this.getImgFolder().'index.php';
        // Delete tmp images
        $files_to_delete[] = _PS_TMP_IMG_DIR_.'product_'.this.id_product.'.'.this.image_format;
        $files_to_delete[] = _PS_TMP_IMG_DIR_.'product_mini_'.this.id_product.'.'.this.image_format;

        foreach ($files_to_delete as $file) {
        if (file_exists($file) && !@unlink($file)) {
            return false;
        }
    }

        // Can we delete the image folder?
        if (is_dir(this.image_dir.this.getImgFolder())) {
        $delete_folder = true;
        foreach (scandir(this.image_dir.this.getImgFolder()) as $file) {
            if (($file != '.' && $file != '..')) {
                $delete_folder = false;
                break;
            }
        }
    }
        if (isset($delete_folder) && $delete_folder) {
            @rmdir(this.image_dir + this.getImageFolder());
        }

        return true;
    }

    public static function deleteAllImages(String path){
        deleteAllImages(path, "jpg");
    }

    /**
     * Recursively deletes all product images in the given folder tree and removes empty folders.
     *
     * @param path folder containing the product images to delete
     * @param format image format
     * @return bool success
     * /
    public static boolean deleteAllImages(String path, String format){
        if (!$path || !$format || !is_dir($path)) {
            return false;
        }
        foreach (scandir($path) as $file) {
            if (preg_match('/^[0-9]+(\-(.*))?\.'.$format.'$/', $file)) {
                unlink($path.$file);
            } elseif (is_dir($path.$file) && (preg_match('/^[0-9]$/', $file))) {
                JeproLabImageModel.deleteAllImages($path.$file.'/', format);
            }
        }

        // Can we remove the image folder?
        if (is_numeric(basename($path))) {
            $remove_folder = true;
            foreach (scandir($path) as $file) {
                if (($file != '.' && $file != '..' && $file != 'index.php')) {
                    $remove_folder = false;
                    break;
                }
            }

            if ($remove_folder) {
                // we're only removing index.php if it's a folder we want to delete
                if (file_exists($path.'index.php')) {
                    @unlink($path.'index.php');
                }
                @rmdir($path);
            }
        }

        return true;
    }

    /**
     * Returns image path in the old or in the new filesystem
     *
     * @return string image path
     * /
    public String getExistingImagePath(){
        if (this.image_id <= 0) {
            return "";
        }

        if (this.existing_path == null || this.existing_path.equals("")) {
            if (Configuration::get('PS_LEGACY_IMAGES') && file_exists(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.this.id_product.'-'.this.id.'.'.this.image_format)) {
                this.existing_path = this.analyze_id + "_" + this.image_id;
            } else {
                this.existing_path = this.getImagePath();
            }
        }

        return this.existing_path;
    }

    /**
     * Returns the path to the folder containing the image in the new filesystem
     *
     * @return string path to folder
     * /
    public String getImageFolder(){
        if (this.image_id <= 0) {
            return null;
        }

        if (this.folder == null || this.folder.equals("")) {
            this.folder = JeproLabImageModel.getStaticImageFolder(this.image_id);
        }

        return this.folder;
    }

    /**
     * Create parent folders for the image in the new filesystem
     *
     * @return bool success
     * /
    public boolean createImageFolder(){
        if (this.image_id <= 0) {
            return false;
        }

        if (!file_exists(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.this.getImgFolder())) {
        // Apparently sometimes mkdir cannot set the rights, and sometimes chmod can't. Trying both.
        $success = @mkdir(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.this.getImgFolder(), self::$access_rights, true);
        $chmod = @chmod(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.this.getImgFolder(), self::$access_rights);

        // Create an index.php file in the new folder
        if (($success || $chmod)
                && !file_exists(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.this.getImgFolder().'index.php')
        && file_exists(this.source_index)) {
            return @copy(this.source_index, JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.this.getImgFolder().'index.php');
        }
    }
        return true;
    }

    /**
     * Returns the path to the image without file extension
     *
     * @return string path
     * /
    public String getImagePath(){
        if (this.image_id <= 0){
            return null;
        }

        return this.getImageFolder() + this.image_id;
    }

    /**
     * Returns the path to the folder containing the image in the new filesystem
     *
     * @param imageId image id
     * @return string path to folder
     * /
    public static String getStaticImageFolder(int imageId){
        if (imageId <= 0){
            return "";
        }
        $folders = str_split((string)$id_image);
        return implode('/', $folders).'/';
    }

    /**
     * Move all legacy product image files from the image folder root to their subfolder in the new filesystem.
     * If max_execution_time is provided, stops before timeout and returns string "timeout".
     * If any image cannot be moved, stops and returns "false"
     *
     * @param int $max_execution_time
     * @return mixed success or timeout
     * /
    public static function moveToNewFileSystem($max_execution_time = 0)
    {
        $start_time = time();
        $image = null;
        $tmp_folder = 'duplicates/';
        foreach (scandir(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY) as $file) {
        // matches the base product image or the thumbnails
        if (preg_match('/^([0-9]+\-)([0-9]+)(\-(.*))?\.jpg$/', $file, $matches)) {
            // don't recreate an image object for each image type
            if (!$image || $image->id !== (int)$matches[2]) {
                $image = new Image((int)$matches[2]);
            }
            // image exists in DB and with the correct product?
            if (Validate::isLoadedObject($image) && $image->id_product == (int)rtrim($matches[1], '-')) {
                // create the new folder if it does not exist
                if (!$image->createImgFolder()) {
                    return false;
                }

                // if there's already a file at the new image path, move it to a dump folder
                // most likely the preexisting image is a demo image not linked to a product and it's ok to replace it
                $new_path = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.$image->getImgPath().(isset($matches[3]) ? $matches[3] : '').'.jpg';
                if (file_exists($new_path)) {
                    if (!file_exists(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.$tmp_folder)) {
                        @mkdir(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.$tmp_folder, self::$access_rights);
                        @chmod(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.$tmp_folder, self::$access_rights);
                    }
                    $tmp_path = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.$tmp_folder.basename($file);
                    if (!@rename($new_path, $tmp_path) || !file_exists($tmp_path)) {
                        return false;
                    }
                }
                // move the image
                if (!@rename(JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.$file, $new_path) || !file_exists($new_path)) {
                    return false;
                }
            }
        }
        if ((int)$max_execution_time != 0 && (time() - $start_time > (int)$max_execution_time - 4)) {
            return 'timeout';
        }
    }
        return true;
    }

    /**
     * Try to create and delete some folders to check if moving images to new file system will be possible
     *
     * @return bool success
     * /
    public static function testFileSystem()
    {
        $safe_mode = Tools::getSafeModeStatus();
        if ($safe_mode) {
            return false;
        }
        $folder1 = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.'testfilesystem/';
        $test_folder = $folder1.'testsubfolder/';
        // check if folders are already existing from previous failed test
        if (file_exists($test_folder)) {
            @rmdir($test_folder);
            @rmdir($folder1);
        }
        if (file_exists($test_folder)) {
            return false;
        }

        @mkdir($test_folder, self::$access_rights, true);
        @chmod($test_folder, self::$access_rights);
        if (!is_writeable($test_folder)) {
            return false;
        }
        @rmdir($test_folder);
        @rmdir($folder1);
        if (file_exists($folder1)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the path where a product image should be created (without file format)
     *
     * @return string path
     * /
    public String getPathForCreation() {
        if (this.image_id <= 0) {
            return "";
        }
        String path;
        if (JeproLabSettingModel.getIntValue("legacy_images") > 0){
            if (this.analyze_id <= 0) {
                return "";
            }
            path = this.analyze_id + "_" + this.image_id;
        }else{
            path = this.getImagePath();
            this.createImageFolder();
        }
        return JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY + path;
    }



    public static class JeproLabImageTypeModel extends JeproLabModel{
        public int image_type_id;

        /** @var string Name * /
        public String name;

        /** @var int Width * /
        public int width;

        /** @var int Height * /
        public int height;

        /** @var bool Apply to products * /
        public boolean analyzes;

        /** @var int Apply to categories * /
        public boolean categories;

        /** @var int Apply to manufacturers * /
        public boolean manufacturers;

        /** @var int Apply to suppliers * /
        public boolean suppliers;

        /** @var int Apply to scenes * /
        public boolean scenes;

        /** @var boolean Apply to laboratories * /
        public boolean laboratories;

        public boolean technicians;

        /*
         * @see ObjectModel::$definition
         * /
        public static $definition = array(
                'table' => 'image_type',
                        'primary' => 'id_image_type',
                        'fields' => array(
                        'name' =>        array('type' => self::TYPE_STRING, 'validate' => 'isImageTypeName', 'required' => true, 'size' => 64),
        'width' =>        array('type' => self::TYPE_INT, 'validate' => 'isImageSize', 'required' => true),
        'height' =>        array('type' => self::TYPE_INT, 'validate' => 'isImageSize', 'required' => true),
        'categories' =>    array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'products' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'manufacturers' =>    array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'suppliers' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'scenes' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'stores' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        ),
                );

        /**
         * @var array Image types cache
         * /
        protected static Map<String, List<JeproLabImageTypeModel>>images_types_cache = new HashMap<>();

        protected  static boolean is_passed = false;

        protected static Map<String> images_types_name_cache = new HashMap<>();

        //protected $webserviceParameters = array();

        public JeproLabImageTypeModel(){

        }

        public static List<JeproLabImageTypeModel> getImagesTypes(){
            return getImagesTypes(null, false);
        }

        public static List<JeproLabImageTypeModel> getImagesTypes(String type){
            return getImagesTypes(type, false);
        }

        /**
         * Returns image type definitions
         *
         * @param type Image type
         * @param orderBySize order by size
         * @return array Image type definitions
         * /
        public static List<JeproLabImageTypeModel> getImagesTypes(String type, boolean orderBySize){
            if (!JeproLabImageTypeModel.images_types_cache.containsKey(type)){
                String where = " WHERE 1";
                if (!type.equals("")){
                    where += " AND " + staticDataBaseObject.quoteName(type, true) + " = 1 ";
                }
                String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_image_type") + where + " ORDER BY ";
                if (orderBySize) {
                    query +=  staticDataBaseObject.quoteName("width") + " DESC, " + staticDataBaseObject.quoteName("height") + " DESC, ";
                    query += staticDataBaseObject.quoteName("name") + "ASC";
                } else {
                    query += staticDataBaseObject.quoteName("name") + " ASC";
                }
                List<JeproLabImageTypeModel> imageTypeList = new ArrayList<>();
                staticDataBaseObject.setQuery(query);
                ResultSet imageTypeSet = staticDataBaseObject.loadObject();
                try{
                    JeproLabImageTypeModel imageType;
                    while(imageTypeSet.next()){
                        imageType = new JeproLabImageTypeModel();
                        imageType.image_type_id = imageTypeSet.getInt("image_type_id");
                        imageType.name = imageTypeSet.getString("name");
                        imageType.width = imageTypeSet.getInt("width");
                        imageType.height = imageTypeSet.getInt("height");
                        imageType.analyzes = imageTypeSet.getInt("analyzes") > 0;
                        imageType.categories = imageTypeSet.getInt("categories") > 0;
                        imageType.manufacturers = imageTypeSet.getInt("manufacturers") > 0;
                        imageType.suppliers = imageTypeSet.getInt("suppliers") > 0;
                        imageType.suppliers = imageTypeSet.getInt("suppliers") > 0;
                        imageType.scenes = imageTypeSet.getInt("scenes") > 0;
                        imageType.technicians = imageTypeSet.getInt("technicians") > 0;
                        imageType.laboratories = imageTypeSet.getInt("laboratories") > 0;
                        imageTypeList.add(imageType);
                    }
                }catch(SQLException ignored){

                }
                JeproLabImageTypeModel.images_types_cache.put(type, imageTypeList);
            }
            return JeproLabImageTypeModel.images_types_cache.get(type);
        }

        /**
         * Check if type already is already registered in database
         *
         * @param typeName Name
         * @return int Number of results found
         * /
        public static int typeAlreadyExists(String typeName){
            if (!JeproLabTools.isImageTypeName(typeName)){
                JeproLabTools.displayError(500, "");
            }
            String query = "SELECT " + staticDataBaseObject.quoteName("image_type_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_image_type");
            query +=  " AS image_type WHERE " + staticDataBaseObject.quoteName("name") + " = " + staticDataBaseObject.quote(typeName);

            int total = 0;
            staticDataBaseObject.setQuery(query);
            ResultSet resultSet = staticDataBaseObject.loadObject();
            try{
                while(resultSet.next()){
                    total += 1;
                }
            }catch(SQLException ignored){
                total = 0;
            }
            return total;
        }

        public static function getByNameAndType(String name){
            getByNameAndType(name, null, 0);
        }

        public static function getByNameAndType(String name, String type){
            getByNameAndType(name, type, 0);
        }

        /**
         * Finds image type definition by name and type
         * @param name
         * @param type
         * /
        public static function getByNameAndType(String name, String type, int order){
            String cacheKey = name + "_" + type + "_" + order;
            if (!JeproLabImageTypeModel.images_types_name_cache.containsKey(cacheKey) && !is_passed) {
                String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_image_type");
                staticDataBaseObject.setQuery(query);
                ResultSet resultSet = staticDataBaseObject.loadObject();

                int total = 7;
                String types[] = new String[total];
                types[0] = "products";
                types[1] = "categories";
                types[2] = "manufacturers";
                types[3] = "suppliers";
                types[4] = "scenes";
                types[5] = "stores";

                try {
                    while(resultSet.next()) {
                        foreach($result as $value) {
                            for (int i = 0; i < total; i++) {
                                JeproLabImageTypeModel.images_types_name_cache.put($result['name'] + "_" + types[i]. '_'.$value]=$result;
                            }
                        }
                    }
                }catch(SQLException ignored){

                }
                is_passed = true;
            }

            $return = false;
            if (JeproLabImageTypeModel.images_types_name_cache.containsKey(cacheKey)){
                $return = JeproLabImageTypeModel.images_types_name_cache.get(cacheKey);
            }
            return $return;
        }

        public static String getFormatedName(String name){
            String themeName = JeproLabContext.getContext().laboratory.theme_name;
            String nameWithoutThemeName = str_replace(array('_'.$theme_name, $theme_name.'_'), '', $name);

            //check if the theme name is already in $name if yes only return $name
            if (strstr($name, $theme_name) && JeproLabImageTypeModel.getByNameAndType(name)) {
                return name;
            } else if (JeproLabImageTypeModel.getByNameAndType(nameWithoutThemeName + "_" + themeName)) {
                return nameWithoutThemeName + "_" + themeName;
            } else if (JeproLabImageTypeModel.getByNameAndType(themeName + "_" + nameWithoutThemeName)) {
                return themeName + "_" + nameWithoutThemeName;
            } else {
                return nameWithoutThemeName + "_default";
            }
        }
    } */

    public static class JeproLabImageTypeModel extends JeproLabModel {
        public int image_type_id;

        /** @var string Name */
        public String name;

        /** @var int Width */
        public int width;

        /** @var int Height */
        public int height;

        /** @var bool Apply to products */
        public boolean analyzes;

        /** @var int Apply to categories */
        public boolean categories;

        /** @var int Apply to manufacturers */
        public boolean manufacturers;

        /** @var int Apply to suppliers */
        public boolean suppliers;

        /** @var int Apply to scenes */
        public boolean scenes;

        public boolean technicians;
        /** @var int Apply to store */
        public boolean laboratories;

        /*
         * @see ObjectModel::$definition
         * /
        public static $definition = array(
                'table' => 'image_type',
                        'primary' => 'id_image_type',
                        'fields' => array(
                        'name' =>        array('type' => self::TYPE_STRING, 'validate' => 'isImageTypeName', 'required' => true, 'size' => 64),
        'width' =>        array('type' => self::TYPE_INT, 'validate' => 'isImageSize', 'required' => true),
        'height' =>        array('type' => self::TYPE_INT, 'validate' => 'isImageSize', 'required' => true),
        'categories' =>    array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'products' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'manufacturers' =>    array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'suppliers' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'scenes' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        'stores' =>        array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
        ),
                );

        /**
         * @var array Image types cache
         */
        protected static Map<String, List<JeproLabImageTypeModel>>images_types_cache = new HashMap<>();

        /*protected static $images_types_name_cache = array();

        protected $webserviceParameters = array();
*/
        public static List<JeproLabImageTypeModel> getImagesTypes() {
            return getImagesTypes("", false);
        }

        public static List<JeproLabImageTypeModel> getImagesTypes(String type) {
            return getImagesTypes(type, false);
        }

        /**
         * Returns image type definitions
         *
         * @param type Image type
         * @param orderBySize order by size
         * @return array Image type definitions
         */
        public static List<JeproLabImageTypeModel> getImagesTypes(String type, boolean orderBySize) {
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            if (!JeproLabImageTypeModel.images_types_cache.containsKey(type)) {
                String where = " WHERE 1 ";
                if (!type.equals("")){
                    where += " AND " + staticDataBaseObject.quote(type) + " = 1 ";
                }
                String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_image_type") + where + "ORDER BY ";

                if (orderBySize) {
                    query += staticDataBaseObject.quoteName("width") + " DESC, " + staticDataBaseObject.quoteName("height") + " DESC, ";
                }
                query += staticDataBaseObject.quoteName("name") + " ASC ";

                staticDataBaseObject.setQuery(query);
                ResultSet imageTypeSet = staticDataBaseObject.loadObject();
                List<JeproLabImageTypeModel> list = new ArrayList<>();
                try{
                    JeproLabImageTypeModel imageType;
                    while(imageTypeSet.next()){
                        imageType = new JeproLabImageTypeModel();
                        imageType.image_type_id = imageTypeSet.getInt("image_type_id");
                        imageType.name = imageTypeSet.getString("name");
                        imageType.width = imageTypeSet.getInt("width");
                        imageType.height = imageTypeSet.getInt("height");
                        imageType.analyzes = imageTypeSet.getInt("analyzes") > 0;
                        imageType.categories = imageTypeSet.getInt("categories") > 0;
                        imageType.manufacturers = imageTypeSet.getInt("manufactures") > 0;
                        imageType.suppliers = imageTypeSet.getInt("suppliers") > 0;
                        imageType.technicians = imageTypeSet.getInt("technicians") > 0;
                        imageType.scenes = imageTypeSet.getInt("scenes") > 0;
                        imageType.laboratories = imageTypeSet.getInt("laboratories") > 0;
                        list.add(imageType);
                    }
                }catch (SQLException ignored){

                }

                JeproLabImageTypeModel.images_types_cache.put(type, list);
            }
            return JeproLabImageTypeModel.images_types_cache.get(type);
        }

        /**
         * Check if type already is already registered in database
         *
         * @param string $typeName Name
         * @return int Number of results found
         * /
        public static function typeAlreadyExists($type_name)
        {
            if (!Validate::isImageTypeName($type_name)) {
            die(Tools::displayError());
        }

            Db::getInstance()->executeS('
                SELECT `id_image_type`
                FROM `'._DB_PREFIX_.'image_type`
            WHERE `name` = \''.pSQL($type_name).'\'');

            return Db::getInstance()->NumRows();
        }

        /**
         * Finds image type definition by name and type
         * @param string $name
         * @param string $type
         * /
        public static function getByNameNType($name, $type = null, $order = 0)
        {
            static $is_passed = false;

            if (!isset(self::$images_types_name_cache[$name.'_'.$type.'_'.$order]) && !$is_passed) {
            $results = Db::getInstance()->ExecuteS('SELECT * FROM `'._DB_PREFIX_.'image_type`');

            $types = array('products', 'categories', 'manufacturers', 'suppliers', 'scenes', 'stores');
            $total = count($types);

            foreach ($results as $result) {
                foreach ($result as $value) {
                    for ($i = 0; $i < $total; ++$i) {
                        self::$images_types_name_cache[$result['name'].'_'.$types[$i].'_'.$value] = $result;
                    }
                }
            }

            $is_passed = true;
        }

            $return = false;
            if (isset(self::$images_types_name_cache[$name.'_'.$type.'_'.$order])) {
            $return = self::$images_types_name_cache[$name.'_'.$type.'_'.$order];
        }
            return $return;
        }

        public static function getFormatedName($name)
        {
            $theme_name = Context::getContext()->shop->theme_name;
            $name_without_theme_name = str_replace(array('_'.$theme_name, $theme_name.'_'), '', $name);

            //check if the theme name is already in $name if yes only return $name
            if (strstr($name, $theme_name) && self::getByNameNType($name)) {
            return $name;
        } elseif (self::getByNameNType($name_without_theme_name.'_'.$theme_name)) {
            return $name_without_theme_name.'_'.$theme_name;
        } elseif (self::getByNameNType($theme_name.'_'.$name_without_theme_name)) {
            return $theme_name.'_'.$name_without_theme_name;
        } else {
            return $name_without_theme_name.'_default';
        }
        }*/
    }
}