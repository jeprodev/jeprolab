package com.jeprolab.models;

import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabTools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabImageModel extends JeproLabModel{

    public static class JeproLabImageManager {
        static final int ERROR_FILE_NOT_EXIST = 1;
        static final int ERROR_FILE_WIDTH     = 2;
        static final int ERROR_MEMORY_LIMIT   = 3;

        protected static int static_error = 0;

        protected static int png_quality = -2;
        protected static int jpeg_quality = -2;

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
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return JeproLabConfigurationSettings.JEPROLAB_TMP_IMAGE_DIRECTORY + cacheImageKey; // + ($disable_cache ? '?time='.time() : '').'" alt="" class="imgm img-thumbnail" />';
        }

        /*
     * Check if memory limit is too long or not
     *
     * @param imagePath
     * @return bool
     */
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

        } */ return true;
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
