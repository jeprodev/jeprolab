package com.jeprolab.models;

import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabImageModel extends JeproLabModel{
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

    /** @var string image extension * /

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
                ResultSet imageSet = dataBaseObject.loadObjectList();

                try {
                    if (imageSet.next()){
                        this.image_id = imageSet.getInt("image_id");
                        this.analyze_id = imageSet.getInt("analyze_id");
                        this.position = imageSet.getInt("position");
                        this.cover = imageSet.getInt("cover") > 0;
                        if (langId <= 0 ){
                            query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_image_lang") + " WHERE image_id = " + imageId;

                            dataBaseObject.setQuery(query);
                            ResultSet imageLangSet = dataBaseObject.loadObjectList();
                            String legend;
                            int languageId;
                            Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();
                            for (Object o : languages.entrySet()) {
                                Map.Entry lang = (Map.Entry) o;
                                JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                                languageId = imageLangSet.getInt("lang_id");
                                legend = imageSet.getString("legend");
                                if (langId == language.language_id) {
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
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }else {
                JeproLabImageModel image = (JeproLabImageModel) JeproLabCache.getInstance().retrieve(cacheKey);
                this.image_id = image.image_id;
                //this. = image.
                this.legend = image.legend;
            }
        }

        this.image_dir = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY;
        //this.source_index = JeproLabConfigurationSettings.JEPROLAB_ANALYZE_IMAGE_DIRECTORY.'index.php';
    }

    public boolean delete(){
        /*if (!parent::delete()) {
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
        WHERE `id_product` = '.(int)this.id_product.' ORDER BY position ASC');*/

        return true;
    }

    /**
     * Return first image (by position) associated with a product attribute
     *
     * @param labId JeproLabLaboratoryModel ID
     * @param langId Language ID
     * @param analyzeId Product ID
     * @param analyzeAttributeId Product Attribute ID
     * @return array
     */
    public static Map<String, String> getBestImageAttribute(int labId, int langId, int analyzeId, int analyzeAttributeId){
        String cacheKey = "jeprolab_image_getBestImageAttribute_" + analyzeId + "_" + analyzeAttributeId + "_" + langId + "_" + langId;
        Map<String, String> rowData = new HashMap<>();
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT image_lab." + dataBaseObject.quoteName("image_id") + ", image_lang." + dataBaseObject.quoteName("legend");
            query += " FROM " + dataBaseObject.quoteName("#__jeprolab_image") + " AS image INNER JOIN " + dataBaseObject.quoteName("#__jeprolab_image_lab");
            query += " AS image_lab (image." + dataBaseObject.quoteName("image_id") + " = image_lab." + dataBaseObject.quoteName("image_id");
            query += " AND image_lab." + dataBaseObject.quoteName("lab_id") + " = " + labId + ") INNER JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_attribute_image");
            query += " AS analyze_attribute_image ON(analyze_attribute_image." + dataBaseObject.quoteName("image_id") + " = image.";
            query += dataBaseObject.quoteName("image_id") + " AND analyze_attribute_id." + dataBaseObject.quoteName("analyze_attribute_id");
            query += " = " + analyzeAttributeId + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_image_lang") + " AS image_lang ON (image_lang.";
            query += dataBaseObject.quoteName("image_id") + " = image_lab." + dataBaseObject.quoteName("image_id") + " AND image_lang.";
            query += dataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE image." + dataBaseObject.quoteName("analyze_id") + " = ";
            query += analyzeId + " ORDER BY image." + dataBaseObject.quoteName("position") + " ASC ";

            dataBaseObject.setQuery(query);
            ResultSet rowDataSet = dataBaseObject.loadObjectList();

            if(rowDataSet != null){
                try{
                    if(rowDataSet.next()){
                        rowData.put("image_id", rowDataSet.getString("image_id"));
                        rowData.put("legend", rowDataSet.getString("legend"));
                        JeproLabCache.getInstance().store(cacheKey, rowData);
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }
            }
        } else {
            rowData = (Map<String, String>)(JeproLabCache.getInstance().retrieve(cacheKey));
        }
        return rowData;
    }





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


        private static boolean is_passed = false;

        /**
         * @var array Image types cache
         */
        protected static Map<String, List<JeproLabImageTypeModel>>images_types_cache = new HashMap<>();

        protected static Map<String, JeproLabImageTypeModel>images_types_name_cache = new HashMap<>();

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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            if (!JeproLabImageTypeModel.images_types_cache.containsKey(type)) {
                String where = " WHERE 1 ";
                if (!type.equals("")){
                    where += " AND " + dataBaseObject.quote(type) + " = 1 ";
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_image_type") + where + "ORDER BY ";

                if (orderBySize) {
                    query += dataBaseObject.quoteName("width") + " DESC, " + dataBaseObject.quoteName("height") + " DESC, ";
                }
                query += dataBaseObject.quoteName("name") + " ASC ";

                dataBaseObject.setQuery(query);
                ResultSet imageTypeSet = dataBaseObject.loadObjectList();
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
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }

                JeproLabImageTypeModel.images_types_cache.put(type, list);
            }
            return JeproLabImageTypeModel.images_types_cache.get(type);
        }

        public static JeproLabImageTypeModel getByNameType(String name){
            return getByNameType(name, null, 0);
        }

        public static JeproLabImageTypeModel getByNameType(String name, String type){
            return getByNameType(name, type, 0);
        }

        /**
         * Finds image type definition by name and type
         * @param name name
         * @param type type
         */
        public static JeproLabImageTypeModel getByNameType(String name, String type, int order){
            //is_passed = false;

            String cacheKey = name + "_" + type + "_" + order;
            if (!JeproLabImageTypeModel.images_types_name_cache.containsKey(cacheKey) && !JeproLabImageTypeModel.is_passed){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_image_type");
                dataBaseObject.setQuery(query);
                ResultSet resultSet = dataBaseObject.loadObjectList();

                List<String> types = new ArrayList<>();
                types.add("analyzes");
                types.add("categories");
                types.add("manufacturers");
                types.add("suppliers");
                types.add("scenes");
                types.add("laboratories");
                int total = types.size();
                if(resultSet != null) {
                    try {
                        JeproLabImageTypeModel imageType;
                        while (resultSet.next()) {
                            imageType = new JeproLabImageTypeModel();
                            imageType.image_type_id = resultSet.getInt("image_type_id");
                            imageType.name = resultSet.getString("name");
                            imageType.width = resultSet.getInt("width");
                            imageType.height = resultSet.getInt("height");
                            imageType.analyzes = resultSet.getInt("analyzes") > 0;
                            imageType.categories = resultSet.getInt("categories") > 0;
                            imageType.manufacturers = resultSet.getInt("manufactures") > 0;
                            imageType.suppliers = resultSet.getInt("suppliers") > 0;
                            imageType.technicians = resultSet.getInt("technicians") > 0;
                            imageType.scenes = resultSet.getInt("scenes") > 0;
                            imageType.laboratories = resultSet.getInt("laboratories") > 0;
                            //foreach($result as $value) {
                            for (int i = 0; i < total; i++) {
                                JeproLabImageTypeModel.images_types_name_cache.put(resultSet.getString("name") + "_" + types.get(i) + "_" , imageType);
                                //JeproLabImageTypeModel.images_types_name_cache.put(resultSet.getString("name") + "_" + types.get(i) + "_" + value, imageType);
                            }
                            //}
                        }
                    }catch(SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally {
                        try {
                            JeproLabFactory.removeConnection(dataBaseObject);
                        }catch (Exception ignored) {
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                        }
                    }
                }
                is_passed = true;
            }
            JeproLabImageTypeModel result = null;
            if (JeproLabImageTypeModel.images_types_name_cache.containsKey(cacheKey)){
                result = JeproLabImageTypeModel.images_types_name_cache.get(cacheKey);
            }
            return result;
        }

        public static String getFormattedName(String name){
            String themeName = JeproLabContext.getContext().laboratory.theme_name;
            String nameWithoutThemeName = JeproLabTools.strReplace(name, "_" + themeName, "");
            nameWithoutThemeName = JeproLabTools.strReplace(nameWithoutThemeName, themeName + "_", "");

            //check if the theme name is already in $name if yes only return $name
            if (!JeproLabTools.strStr(name, themeName).equals("") && JeproLabImageTypeModel.getByNameType(name) != null) {
                return name;
            } else if (JeproLabImageTypeModel.getByNameType(nameWithoutThemeName + "_" + themeName) != null){
                return nameWithoutThemeName + "_" + themeName;
            } else if (JeproLabImageTypeModel.getByNameType(themeName + "_" + nameWithoutThemeName) != null){
                return themeName + "_" + nameWithoutThemeName;
            } else{
                return nameWithoutThemeName + "_default";
            }
        }
    }


    public static class JeproLabImageManager {
        static final int ERROR_FILE_NOT_EXIST = 1;
        static final int ERROR_FILE_WIDTH     = 2;
        static final int ERROR_MEMORY_LIMIT   = 3;

        protected static int static_error = 0;

        protected static int png_quality = -2;
        protected static int jpeg_quality = 2;

        public static String thumbNail(String imagePath, String cacheImageKey, int size){
            return thumbNail(imagePath, cacheImageKey, size, "jpg", true, true);
        }

        public static String thumbNail(String imagePath, String cacheImageKey, int size, String imageType){
            return thumbNail(imagePath, cacheImageKey, size, imageType, true, true);
        }

        public static String thumbNail(String imagePath, String cacheImageKey, int size, String imageType, boolean disableCache){
            return thumbNail(imagePath, cacheImageKey, size, imageType, disableCache, true);
        }

        /**
         * Generate a cached thumbnail for object lists (eg. carrier, order statuses...etc)
         *
         * @param imagePath Real image filename
         * @param cacheImageKey Cached filename
         * @param size Desired size
         * @param imageType Image type
         * @param disableCache When turned on a timestamp will be added to the image URI to disable the HTTP cache
         * @param regenerate When turned on and the file already exist, the file will be regenerated
         * @return string
         */
        public static String thumbNail(String imagePath, String cacheImageKey, int size, String imageType, boolean disableCache, boolean regenerate){
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return "";
            }
            File cacheImageFile = new File(JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + cacheImageKey);
            if (cacheImageFile.exists() && regenerate) {
                imageFile.deleteOnExit();
            }

            if (regenerate || !imageFile.exists()){
                try {
                    BufferedImage buffer = ImageIO.read(imageFile);
                    // Evaluate the memory required to resize the image: if it's too much, you can't resize it.
                    if (!JeproLabImageManager.checkImageMemoryLimit(imagePath)) {
                        return "";
                    }
                    int width = buffer.getWidth();
                    int height = buffer.getHeight();
                    int maxWidth = size * 3;

                    // Size is already ok
                    if (height < size && width <= maxWidth) {
                        JeproLabTools.copy(imagePath, JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + cacheImageKey);
                    }else {
                        // We need to resize * /
                        int widthRatio = (width / (height / size));
                        if (widthRatio > maxWidth) {
                            widthRatio = maxWidth;
                            size = height / (width / maxWidth);
                        }

                        JeproLabImageManager.resize(imagePath, JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + cacheImageKey, widthRatio, size, imageType);
                    }
                } catch (IOException ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }

            }
            return JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + cacheImageKey; // + ($disable_cache ? '?time='.time() : '').'" alt="" class="imgm img-thumbnail" />';
        }

        public static boolean checkImageMemoryLimit(String imagePath){
            /*try {
            BufferedImage buffer = ImageIO.read(new File(imagePath));
            int width = buffer.getWidth();
            int height = buffer.getHeight();
            //int a = buffer.$infos =
            if (!is_array($infos) || !isset($infos['bits'])) {
                return true;
            }

            $memory_limit = JeproLabTools.getMemoryLimit();
            // memory_limit == -1 => unlimited memory
            if (function_exists('memory_get_usage') && (int)$memory_limit != -1) {
                $current_memory = memory_get_usage();
                $channel = isset($infos['channels']) ? ($infos['channels'] / 8) : 1;

                // Evaluate the memory required to resize the image: if it's too much, you can't resize it.
                // For perfs, avoid computing static maths formulas in the code. pow(2, 16) = 65536 ; 1024 * 1024 = 1048576
                if (($infos[0] * $infos[1] * $infos['bits'] * $channel + 65536) * 1.8 + $current_memory > $memory_limit - 1048576) {
                    return false;
                }
            }

            return true;
        }catch(IOException ignored){

        } */
            return true;
        }

        public static boolean resize(String sourceFile, String destinationFile){
            return resize(sourceFile, destinationFile, 0, 0, "jpg", false, 0, 0, 0, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth){
            return resize(sourceFile, destinationFile, destWidth, 0, "jpg", false, 0, 0, 0, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight){
            return resize(sourceFile, destinationFile, destWidth, destHeight, "jpg", false, 0, 0, 0, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType){
            return resize(sourceFile, destinationFile, destWidth, destHeight, fileType, false, 0, 0, 0, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType, boolean forceType){
            return resize(sourceFile, destinationFile, destWidth, destHeight, fileType, forceType, 0, 0, 0, 5, 0, 0);
        }
        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType, boolean forceType, int error){
            return resize(sourceFile, destinationFile, destWidth, destHeight, fileType, forceType, error, 0, 0, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType, boolean forceType, int error, int targetWidth){
            return resize(sourceFile, destinationFile, destWidth, destHeight, fileType, forceType, error, targetWidth, 0, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType, boolean forceType, int error, int targetWidth, int targetHeight){
            return resize(sourceFile, destinationFile, destWidth, destHeight, fileType, forceType, error, targetWidth, targetHeight, 5, 0, 0);
        }

        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType, boolean forceType, int error, int targetWidth, int targetHeight, int quality, int srcWidth){
            return resize(sourceFile, destinationFile, destWidth, destHeight, fileType, forceType, error, targetWidth, targetHeight, quality, srcWidth, 0);
        }

        /**
         * Resize, cut and optimize image
         *
         * @param sourceFile   Image object from $_FILE
         * @param destinationFile   Destination filename
         * @param destWidth  Desired width (optional)
         * @param destHeight Desired height (optional)
         * @param fileType image type
         * @param forceType force image type
         * @param error
         * @param targetWidth
         * @param targetHeight
         * @param quality
         * @param srcWidth
         * @param srcHeight
         * @return bool Operation result
         */
        public static boolean resize(String sourceFile, String destinationFile, int destWidth, int destHeight, String fileType, boolean forceType, int error, int targetWidth, int targetHeight, int quality, int srcWidth, int srcHeight){
        /*if (PHP_VERSION_ID < 50300) {
            clearstatcache();
        } else {
            clearstatcache(true, $src_file);
        } */


            try {
                File imageFile = new File(sourceFile);
                if (!imageFile.exists() || imageFile.length() <= 0) {
                    //return !($error = JeproLabImageManager.ERROR_FILE_NOT_EXIST);
                }
                BufferedImage buffer = ImageIO.read(imageFile);
                int tmpWidth = buffer.getWidth();
                int tmpHeight = buffer.getHeight();
                //list($tmp_width, $tmp_height, $type) = getimagesize($src_file);
                int rotate = 0;
            /*if (function_exists('exif_read_data') && function_exists('mb_strtolower')) {
                $exif = @exif_read_data($src_file) ;

                if ($exif && isset($exif['Orientation'])) {
                    switch ($exif['Orientation']) {
                        case 3:
                            srcWidth = tmpWidth;
                            srcHeight = tmpHeight;
                            rotate = 180;
                            break;

                        case 6:
                            srcWidth = tmpHeight;
                            srcHeight = tmpWidth;
                            rotate = -90;
                            break;

                        case 8:
                            srcWidth = tmpHeight;
                            srcHeight = tmpWidth;
                            rotate = 90;
                            break;

                        default:
                            srcWidth = tmpWidth;
                            srcHeight = tmpHeight;
                    }
                } else {
                    srcWidth = tmpWidth;
                    srcHeight = tmpHeight;
                }
            } else {
                srcWidth = tmpWidth;
                srcHeight = tmpHeight;
            }

            // If PS_IMAGE_QUALITY is activated, the generated image will be a PNG with .jpg as a file extension.
            // This allow for higher quality and for transparency. JPG source files will also benefit from a higher quality
            // because JPG re-encoding by GD, even with max quality setting, degrades the image.
            if (JeproLabSettingModel.getStringValue("image_quality").equals("png_all") || (JeproLabSettingModel.getStringValue("image_quality").equals("png") && $type == IMAGETYPE_PNG) && !forceType) {
                fileType = "png";
            }

            if (srcWidth <= 0) {
                JeproLabTools.displayError(500, JeproLab.getBundle().getString("JEPROLAB_IMAGE_ERROR_FILE_WIDTH_LABEL"));
                static_error = JeproLabImageManager.ERROR_FILE_WIDTH;
                return !($error = JeproLabImageManager.ERROR_FILE_WIDTH);
            }

            if (destWidth <= 0) {
                destWidth = srcWidth;
            }
            if (destHeight <= 0) {
                destHeight = srcHeight;
            }

            int widthRatio = destWidth / srcWidth;
            int heightRatio = destHeight / srcHeight;

            $ps_image_generation_method = Configuration::get ('PS_IMAGE_GENERATION_METHOD');
            if (widthRatio > 1 && heightRatio > 1) {
                $next_width = srcWidth;
                $next_height = srcHeight;
            } else {
                if ($ps_image_generation_method == 2 || (!$ps_image_generation_method && widthRatio > heightRatio)) {
                    $next_height = destHeight;
                    $next_width = round((srcWidth * $next_height) / srcHeight);
                    destWidth = (!$ps_image_generation_method ? destWidth : $next_width);
                } else {
                    $next_width = destWidth;
                    $next_height = round(srcHeight * destWidth / srcWidth);
                    destHeight =  (!$ps_image_generation_method ? destHeight : $next_height);
                }
            }

            if (!JeproLabImageManager.checkImageMemoryLimit($src_file)) {
                return !($error = JeproLabImageManager.ERROR_MEMORY_LIMIT);
            }

            $tgt_width = destWidth;
            $tgt_height = destHeight;

            $dest_image = imagecreatetruecolor(destWidth, destHeight);

            // If image is a PNG and the output is PNG, fill with transparency. Else fill with white background.
            if ($file_type == 'png' && $type == IMAGETYPE_PNG) {
                imagealphablending($dest_image, false);
                imagesavealpha($dest_image, true);
                $transparent = imagecolorallocatealpha($dest_image, 255, 255, 255, 127);
                imagefilledrectangle($dest_image, 0, 0, destWidth, destHeight, $transparent);
            } else {
                $white = imagecolorallocate($dest_image, 255, 255, 255);
                imagefilledrectangle($dest_image, 0, 0, destWidth, destHeight, $white);
            }

            $src_image = JeproLabImageManager.create ($type, $src_file);
            if ($rotate) {
                $src_image = imagerotate($src_image, $rotate, 0);
            }

            if (destWidth >= srcWidth && destHeight >= srcHeight) {
                imagecopyresized($dest_image, $src_image, (int) ((destWidth - $next_width) / 2), (int) ((destHeight - $next_height) / 2), 0, 0, $next_width, $next_height, srcWidth, srcHeight);
            } else {
                JeproLabImageManager.imageCopyReSampled($dest_image, $src_image, (int) ((destWidth - $next_width) / 2), (int) ((destHeight - $next_height) / 2), 0, 0, $next_width, $next_height, srcWidth, srcHeight, quality);
            }
            $write_file = JeproLabImageManager.write ($file_type, $dest_image, $dst_file);
            @imagedestroy($src_image) ;
            return $write_file; */
            }catch(IOException ignored){

            }
            return true;
        }
    }
}
