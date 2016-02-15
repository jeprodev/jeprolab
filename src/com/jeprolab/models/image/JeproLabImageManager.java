package com.jeprolab.models.image;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabSettingModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;

/**
 *
 * Created by jeprodev on 13/02/2014.
 */
public class JeproLabImageManager {
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
/*
    public static function imageCopyReSampled(&$dst_image, $src_image, $dst_x, $dst_y, $src_x, $src_y, int destWidth, int destHeight, int srcWidth, int srcHeight){
        return imageCopyReSampled(3);
    }

    public static function imageCopyReSampled(&$dst_image, $src_image, $dst_x, $dst_y, $src_x, $src_y, int destWidth, int destHeight, int srcWidth, int srcHeight, int quality = 3){
        // Plug-and-Play fastimagecopyresampled function replaces much slower imagecopyresampled.
        // Just include this function and change all "imagecopyresampled" references to "fastimagecopyresampled".
        // Typically from 30 to 60 times faster when reducing high resolution images down to thumbnail size using the default quality setting.
        // Author: Tim Eckel - Date: 09/07/07 - Version: 1.1 - Project: FreeRingers.net - Freely distributable - These comments must remain.
        //
        // Optional "quality" parameter (defaults is 3). Fractional values are allowed, for example 1.5. Must be greater than zero.
        // Between 0 and 1 = Fast, but mosaic results, closer to 0 increases the mosaic effect.
        // 1 = Up to 350 times faster. Poor results, looks very similar to imagecopyresized.
        // 2 = Up to 95 times faster.  Images appear a little sharp, some prefer this over a quality of 3.
        // 3 = Up to 60 times faster.  Will give high quality smooth results very close to imagecopyresampled, just faster.
        // 4 = Up to 25 times faster.  Almost identical to imagecopyresampled for most images.
        // 5 = No speedup. Just uses imagecopyresampled, no advantage over imagecopyresampled.

        if (empty($src_image) || empty($dst_image) || quality <= 0) {
            return false;
        }

        if (quality < 5 && ((destW * quality) < srcWidth || (destHeight * quality) < srcHeight)) {
            $temp = imagecreatetruecolor(destWidth * quality + 1, destHeight * quality + 1);
            imagecopyresized($temp, $src_image, 0, 0, $src_x, $src_y, destWidth * quality + 1, destHeight * quality + 1, srcWidth, srcHeight);
            imagecopyresampled($dst_image, $temp, $dst_x, $dst_y, 0, 0, destWidth, destHeight, destWidth * quality, destHeight * quality);
            imagedestroy($temp);
        } else {
            imagecopyresampled($dst_image, $src_image, $dst_x, $dst_y, $src_x, $src_y, destW, destH, srcW, srcH);
        }
        return true;
    }

    public static boolean isRealImage(String fileName){
        return isRealImage(fileName, null, null);
    }

    public static boolean isRealImage(String fileName, String fileMimeType){
        return isRealImage(fileName, fileMimeType, null);
    }

    /**
     * Check if file is a real image
     *
     * @param fileName File path to check
     * @param fileMimeType File known mime type (generally from $_FILES)
     * @param mimeTypeList Allowed MIME types
     * @return bool
     */
    public static boolean isRealImage(String fileName, String fileMimeType, String[] mimeTypeList){
        // Detect mime content type
        String mimeType = null;
        if (mimeTypeList == null) {
            mimeTypeList = new String[5];
            mimeTypeList[0] = "image/gif";
            mimeTypeList[1] = "image/jpg";
            mimeTypeList[2] = "image/jpeg";
            mimeTypeList[3] = "image/pjpeg";
            mimeTypeList[4] = "image/png";
            mimeTypeList[5] = "image/x-png";
        }

        /*// Try 4 different methods to determine the mime type
        if (function_exists('getimagesize')) {
            $image_info = @getimagesize(fileName);

            if ($image_info) {
                mimeType = $image_info['mime'];
            } else {
                $file_mime_type = false;
            }
        } else if (function_exists('finfo_open')) {
            $const = defined('FILEINFO_MIME_TYPE') ? FILEINFO_MIME_TYPE : FILEINFO_MIME;
            $finfo = finfo_open($const);
            mimeType = finfo_file($finfo, fileName);
            finfo_close($finfo);
        } else if (function_exists('mime_content_type')) {
            mimeType = mime_content_type(fileName);
        } else if (function_exists('exec')) {
            mimeType = trim(exec('file -b --mime-type '.escapeshellarg(fileName)));
            if (!mimeType) {
                mimeType = trim(exec('file --mime '.escapeshellarg(fileName)));
            }
            if (!mimeType) {
                mimeType = trim(exec('file -bi '.escapeshellarg(fileName)));
            }
        }

        if ($file_mime_type && (mimeType == null || mimeType.equals("") || mimeType.equals("regular file") || mimeType.equals("text/plain"))) {
            mimeType = fileMimeType;
        }

        // For each allowed MIME type, we are looking for it inside the current MIME type
        foreach (mimeTypeList as $type) {
            if (strstr(mimeType, $type)) {
                return true;
            }
        } */

        return false;
    }

    public static boolean isCorrectImageFileExtension(String fileName){
        return isCorrectImageFileExtension(fileName, null);
    }
    /**
     * Check if image file extension is correct
     *
     * @param fileName Real filename
     * @param authorizedExtensions list of authorized extensions
     * @return bool True if it's correct
     */
    public static boolean isCorrectImageFileExtension(String fileName, String[] authorizedExtensions){
        // Filter on file extension
        if (authorizedExtensions == null) {
            authorizedExtensions = new String[5];
            authorizedExtensions[0] = "gif";
            authorizedExtensions[1] = "jpg";
            authorizedExtensions[2] = "jpeg";
            authorizedExtensions[3] = "jpe";
            authorizedExtensions[4] = "png";
        }
        String[] nameExplode = fileName.split(".");
        if(nameExplode.length >= 2) {
            String currentExtension = nameExplode[nameExplode.length - 1];
            if (!Arrays.asList(authorizedExtensions).contains(currentExtension)){
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /*
     * Validate image upload (check image type and weight)
     *
     * @param array $file Upload $_FILE value
     * @param int $max_file_size Maximum upload size
     * @return bool|string Return false if no error encountered
     * /
    public static boolean validateUpload($file, $max_file_size = 0, $types = null) {
        if ((int)$max_file_size > 0 && $file['size'] > (int)$max_file_size) {
            return sprintf(Tools::displayError('Image is too large (%1$d kB). Maximum allowed: %2$d kB'), $file['size'] / 1024, $max_file_size / 1024);
        }
        if (!ImageManager::isRealImage($file['tmp_name'], $file['type']) || !JeproLabImageManager.isCorrectImageFileExtension($file['name'], $types) || preg_match('/\%00/', $file['name'])) {
            return Tools::displayError('Image format not recognized, allowed formats are: .gif, .jpg, .png');
        }
        if ($file['error']) {
            return JeproLabTools.displayError('Error while uploading image; please change your server\'s settings. (Error code: %s)'), $file['error']);
        }
        return false;
    }

    public static boolean validateIconUpload($file){
        return validateIconUpload(file, 0);
    }
    /**
     * Validate icon upload
     *
     * @param array $file Upload $_FILE value
     * @param maxFileSize Maximum upload size
     * @return bool|string Return false if no error encountered
     * /
    public static boolean validateIconUpload($file, int maxFileSize){
        if (maxFileSize > 0 && $file['size'] > maxFileSize) {
            return sprintf(
                    Tools::displayError('Image is too large (%1$d kB). Maximum allowed: %2$d kB'),
                    $file['size'] / 1000,
                    $max_file_size / 1000
            );
        }
        if (substr($file['name'], -4) != ".ico") {
            return Tools::displayError('Image format not recognized, allowed formats are: .ico');
        }
        if ($file['error']) {
            return Tools::displayError('Error while uploading image; please change your server\'s settings.');
        }
        return false;
    }

    /**
     * Cut image
     *
     * @param array $src_file Origin filename
     * @param string $dst_file Destination filename
     * @param int destWidth Desired width
     * @param int destHeight Desired height
     * @param string $file_type
     * @param int $dst_x
     * @param int $dst_y
     *
     * @return bool Operation result
     * /
    public static function cut($src_file, $dst_file, destWidth = null, destHeight = null, $file_type = 'jpg', $dst_x = 0, $dst_y = 0) {
        if (!file_exists($src_file)) {
            return false;
        }

        // Source information
        $src_info = getimagesize($src_file);
        $src = array(
                'width' => $src_info[0],
                'height' => $src_info[1],
            'ressource' => ImageManager::create($src_info[2], $src_file),
        );

        // Destination information
        $dest = array();
        $dest['x'] = $dst_x;
        $dest['y'] = $dst_y;
        $dest['width'] = !is_null(destWidth) ? destWidth : $src['width'];
        $dest['height'] = !is_null(destHeight) ? destHeight : $src['height'];
        $dest['ressource'] = ImageManager::createWhiteImage($dest['width'], $dest['height']);

        $white = imagecolorallocate($dest['ressource'], 255, 255, 255);
        imagecopyresampled($dest['ressource'], $src['ressource'], 0, 0, $dest['x'], $dest['y'], $dest['width'], $dest['height'], $dest['width'], $dest['height']);
        imagecolortransparent($dest['ressource'], $white);
        $return = ImageManager::write($file_type, $dest['ressource'], $dst_file);
        @imagedestroy($src['ressource']);
        return    $return;
    }

    /**
     * Create an image with GD extension from a given type
     *
     * @param string $type
     * @param string fileName
     * @return resource
     * /
    public static function create($type, fileName)
    {
        switch ($type) {
            case IMAGETYPE_GIF :
                return imagecreatefromgif(fileName);
            break;

            case IMAGETYPE_PNG :
                return imagecreatefrompng(fileName);
            break;

            case IMAGETYPE_JPEG :
            default:
                return imagecreatefromjpeg(fileName);
            break;
        }
    }

    /**
     * Create an empty image with white background
     *
     * @param width image width
     * @param height image height
     * @return resource
     * /
    public static function createWhiteImage(int width, int height){
        $image = imagecreatetruecolor($width, $height);
        $white = imagecolorallocate($image, 255, 255, 255);
        imagefill($image, 0, 0, $white);
        return $image;
    }

    /**
     * Generate and write image
     *
     * @param type image type
     * @param resource $resource
     * @param fileName String file name
     * @return bool
     * /
    public static boolean write(String type, $resource, String fileName){
        if (png_quality < 0) {
            png_quality = JeproLabSettingModel.getIntValue("png_quality");
        }

        if (jpeg_quality < 0) {
            jpeg_quality = JeproLabSettingModel.getIntValue("jpeg_quality");
        }
        int quality;
        switch (type) {
            case "gif":
                $success = imagegif($resource, fileName);
                break;

            case "png":
                quality = (png_quality <= 0 ? 7 : png_quality);
                $success = imagepng($resource, fileName, (int)quality);
                break;

            case "jpg":
            case "jpeg":
            default:
                quality = (jpeg_quality <= 0 ? 90 : jpeg_quality);
                imageinterlace($resource, 1); /// make it PROGRESSIVE
                $success = imagejpeg($resource, fileName, (int)quality);
                break;
        }
        imagedestroy($resource);
        @chmod(fileName, 0664);
        return $success;
    }

    /**
     * Return the mime type by the file extension
     *
     * @param fileName String file name
     * @return string
     * /
    public static String getMimeTypeByExtension(String fileName){
        String[] types = new String[4];
        types[0] = "gif";
        types[1] = "jpg";
        types[2] = "jpeg";
        types[3] = "png";
        String extension = fileName.substring(fileName.indexOf(".") + 1);

        String mimeType = null;
        if (Arrays.asList(types).contains(extension)){
                mimeType = "image/" + extension;
        }

        if (mimeType == null) {
            mimeType = "image/jpeg";
        }

        return mimeType;
    } */
}