package com.jeprolab.models;

import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 28/02/2014.
 */
public class JeproLabThemeModel extends JeproLabModel{
    public int theme_id;
    public String name;
    public String directory;
    public boolean responsive;
    public boolean default_left_column;
    public boolean default_right_column;
    public int analyze_per_page;

    public static final String JEPROLAB_CACHE_FILE_CUSTOMER_THEMES_LIST = "/config/xml/customer_themes_list.xml";

    public static final String JEPROLAB_CACHE_FILE_MUST_HAVE_THEMES_LIST = "/config/xml/must_have_themes_list.xml";

    public static final String JEPROLAB_UPLOADED_THEME_DIR_NAME = "uploaded";

    /** @var int access rights of created folders (octal) */
    public static int access_rights = 0775;

    /* *
     * @see ObjectModel::$definition
     * /
    public static $definition = array(
            'table' => 'theme',
                    'primary' => 'id_theme',
                    'fields' => array(
                    'name' => array('type' => self::TYPE_STRING, 'validate' => 'isGenericName', 'size' => 64, 'required' => true),
    'directory' => array('type' => self::TYPE_STRING, 'validate' => 'isDirName', 'size' => 64, 'required' => true),
    'responsive' => array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
    'default_left_column' => array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
    'default_right_column' => array('type' => self::TYPE_BOOL, 'validate' => 'isBool'),
    'product_per_page' => array('type' => self::TYPE_INT, 'validate' => 'isInt')
    ),
            );
*/
    public static List<JeproLabThemeModel> getThemes(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_theme") + " ORDER BY " + staticDataBaseObject.quoteName("theme_name");
        staticDataBaseObject.setQuery(query);

        ResultSet themesSet = staticDataBaseObject.loadObject();
        List<JeproLabThemeModel> themeList = new ArrayList<>();
        if(themesSet != null){
            try{
                JeproLabThemeModel theme;
                while(themesSet.next()){
                    theme = new JeproLabThemeModel();
                    theme.theme_id = themesSet.getInt("theme_id");
                    theme.name = themesSet.getString("theme_name");
                    theme.directory = themesSet.getString("directory");
                    theme.responsive = themesSet.getInt("responsive") > 0;
                    theme.default_left_column = themesSet.getInt("default_left_column") > 0;
                    theme.default_right_column = themesSet.getInt("default_right_column") > 0;
                    theme.analyze_per_page = themesSet.getInt("analyze_per_page");
                    themeList.add(theme);
                }
            }catch (SQLException ignored){
ignored.printStackTrace();
            }
        }
        return themeList;
    }
/*
    public static function getAllThemes($excluded_ids = false)
    {
        $themes = new PrestaShopCollection('Theme');

        if (is_array($excluded_ids) && !empty($excluded_ids)) {
            $themes->where('id_theme', 'notin', $excluded_ids);
        }

        $themes->orderBy('name');
        return $themes;
    }

    /**
     * return an array of all available theme (installed or not)
     *
     * @param bool $installed_only
     * @return array string (directory)
     * /
    public static function getAvailable($installed_only = true)
    {
        static $dirlist = array();
        $available_theme = array();

        if (empty($dirlist)) {
            $themes = scandir(_PS_ALL_THEMES_DIR_);
            foreach ($themes as $theme) {
                if (is_dir(_PS_ALL_THEMES_DIR_.DIRECTORY_SEPARATOR.$theme) && $theme[0] != '.') {
                    $dirlist[] = $theme;
                }
            }
        }

        if ($installed_only) {
            $themes = Theme::getThemes();
            foreach ($themes as $theme_obj) {
                /** @var Theme $theme_obj * /
                $themes_dir[] = $theme_obj->directory;
            }

            foreach ($dirlist as $theme) {
                if (false !== array_search($theme, $themes_dir)) {
                    $available_theme[] = $theme;
                }
            }
        } else {
            $available_theme = $dirlist;
        }

        return $available_theme;
    }

    /**
     * check if a theme is used by a shop
     *
     * @return bool
     * /
    public function isUsed()
    {
        return Db::getInstance()->getValue('SELECT count(*)
            FROM '._DB_PREFIX_.'shop WHERE id_theme = '.(int)$this->id);
    }

    /**
     * add only theme if the directory exists
     *
     * @param bool $null_values
     * @param bool $autodate
     * @return bool Insertion result
     * /
    public function add($autodate = true, $null_values = false)
    {
        if (!is_dir(_PS_ALL_THEMES_DIR_.$this->directory)) {
        return false;
    }

        return parent::add($autodate, $null_values);
    }

    /**
     * Checks if theme exists (by folder) and returns Theme object.
     *
     * @param string $directory
     *
     * @return bool|Theme
     * /
    public static function getByDirectory($directory)
    {
        if (is_string($directory) && strlen($directory) > 0 && file_exists(_PS_ALL_THEMES_DIR_.$directory) && is_dir(_PS_ALL_THEMES_DIR_.$directory)) {
            $id_theme = (int)Db::getInstance()->getValue('SELECT id_theme FROM '._DB_PREFIX_.'theme WHERE directory="'.pSQL($directory).'"');

            return $id_theme ? new Theme($id_theme) : false;
        }

        return false;
    }

    public static function getInstalledThemeDirectories()
    {
        $list = array();
        $tmp = Db::getInstance()->executeS('SELECT `directory` FROM '._DB_PREFIX_.'theme');
        foreach ($tmp as $t) {
        $list[] = $t['directory'];
    }

        return $list;
    }

    public static function getThemeInfo($id_theme)
    {
        $theme = new Theme((int)$id_theme);
        $theme_arr = array();

        if (file_exists(_PS_ROOT_DIR_.'/config/xml/themes/'.$theme->directory.'.xml')) {
        $config_file = _PS_ROOT_DIR_.'/config/xml/themes/'.$theme->directory.'.xml';
    } elseif ($theme->name == 'default-bootstrap') {
        $config_file = _PS_ROOT_DIR_.'/config/xml/themes/default.xml';
    } else {
        $config_file = false;
    }

        if ($config_file) {
            $theme_arr['theme_id'] = (int)$theme->id;
            $xml_theme = @simplexml_load_file($config_file);

            if ($xml_theme !== false) {
                foreach ($xml_theme->attributes() as $key => $value) {
                    $theme_arr['theme_'.$key] = (string)$value;
                }

                foreach ($xml_theme->author->attributes() as $key => $value) {
                    $theme_arr['author_'.$key] = (string)$value;
                }

                if ($theme_arr['theme_name'] == 'default-bootstrap') {
                    $theme_arr['tc'] = Module::isEnabled('themeconfigurator');
                }
            }
        } else {
            // If no xml we use data from database
            $theme_arr['theme_id'] = (int)$theme->id;
            $theme_arr['theme_name'] = $theme->name;
            $theme_arr['theme_directory'] = $theme->directory;
        }

        return $theme_arr;
    }

    public static function getNonInstalledTheme()
    {
        $installed_theme_directories = Theme::getInstalledThemeDirectories();
        $not_installed_theme = array();
        foreach (glob(_PS_ALL_THEMES_DIR_.'*', GLOB_ONLYDIR) as $theme_dir) {
        $dir = basename($theme_dir);
        $config_file = _PS_ALL_THEMES_DIR_.$dir.'/config.xml';
        if (!in_array($dir, $installed_theme_directories) && @filemtime($config_file)) {
            if ($xml_theme = @simplexml_load_file($config_file)) {
                $theme = array();
                foreach ($xml_theme->attributes() as $key => $value) {
                    $theme[$key] = (string)$value;
                }

                if (!empty($theme)) {
                    $not_installed_theme[] = $theme;
                }
            }
        }
    }

        return $not_installed_theme;
    }

    /**
     * update the table PREFIX_theme_meta for the current theme
     * @param array $metas
     * @param bool  $full_update If true, all the meta of the theme will be deleted prior the insert, otherwise only the current $metas will be deleted
     *
     * /
    public function updateMetas($metas, $full_update = false)
    {
        if ($full_update) {
            Db::getInstance()->delete('theme_meta', 'id_theme='.(int)$this->id);
        }

        $values = array();
        if ($this->id > 0) {
            foreach ($metas as $meta) {
                if (!$full_update) {
                    Db::getInstance()->delete('theme_meta', 'id_theme='.(int)$this->id.' AND id_meta='.(int)$meta['id_meta']);
                }

                $values[] = array(
                        'id_theme'     => (int)$this->id,
                        'id_meta'      => (int)$meta['id_meta'],
                        'left_column'  => (int)$meta['left'],
                        'right_column' => (int)$meta['right']
                );
            }
            Db::getInstance()->insert('theme_meta', $values);
        }
    }

    public function hasColumns($page)
    {
        return Db::getInstance()->getRow('
            SELECT IFNULL(left_column, default_left_column) as left_column, IFNULL(right_column, default_right_column) as right_column
            FROM '._DB_PREFIX_.'theme t
        LEFT JOIN '._DB_PREFIX_.'theme_meta tm ON (t.id_theme = tm.id_theme)
        LEFT JOIN '._DB_PREFIX_.'meta m ON (m.id_meta = tm.id_meta)
        WHERE t.id_theme ='.(int)$this->id.' AND m.page = "'.pSQL($page).'"');
    }

    public function hasColumnsSettings($page)
    {
        return (bool)Db::getInstance()->getValue('
            SELECT m.`id_meta`
            FROM '._DB_PREFIX_.'theme t
        LEFT JOIN '._DB_PREFIX_.'theme_meta tm ON (t.id_theme = tm.id_theme)
        LEFT JOIN '._DB_PREFIX_.'meta m ON (m.id_meta = tm.id_meta)
        WHERE t.id_theme ='.(int)$this->id.' AND m.page = "'.pSQL($page).'"');
    }

    public function hasLeftColumn($page = null)
    {
        return (bool)Db::getInstance()->getValue(
            'SELECT IFNULL(
        (
                SELECT left_column
        FROM '._DB_PREFIX_.'theme t
        LEFT JOIN '._DB_PREFIX_.'theme_meta tm ON ( t.id_theme = tm.id_theme )
        LEFT JOIN '._DB_PREFIX_.'meta m ON ( m.id_meta = tm.id_meta )
        WHERE t.id_theme ='.(int)$this->id.'
        AND m.page = "'.pSQL($page).'" ) , default_left_column
        )
        FROM '._DB_PREFIX_.'theme
        WHERE id_theme ='.(int)$this->id
        );
    }

    public function hasRightColumn($page = null)
    {
        return (bool)Db::getInstance()->getValue(
            'SELECT IFNULL(
        (
                SELECT right_column
        FROM '._DB_PREFIX_.'theme t
        LEFT JOIN '._DB_PREFIX_.'theme_meta tm ON ( t.id_theme = tm.id_theme )
        LEFT JOIN '._DB_PREFIX_.'meta m ON ( m.id_meta = tm.id_meta )
        WHERE t.id_theme ='.(int)$this->id.'
        AND m.page = "'.pSQL($page).'" ) , default_right_column
        )
        FROM '._DB_PREFIX_.'theme
        WHERE id_theme ='.(int)$this->id);
    }

    /**
     * @return array|bool
     * /
    public function getMetas()
    {
        if (!Validate::isUnsignedId($this->id) || $this->id == 0) {
        return false;
    }

        return Db::getInstance()->executeS('SELECT * FROM '._DB_PREFIX_.'theme_meta WHERE id_theme = '.(int)$this->id);
    }

    /**
     * @return bool
     * /
    public function removeMetas()
    {
        if (!Validate::isUnsignedId($this->id) || $this->id == 0) {
        return false;
    }

        return Db::getInstance()->delete('theme_meta', 'id_theme = '.(int)$this->id);
    }

    public function toggleResponsive()
    {
        // Object must have a variable called 'responsive'
        if (!array_key_exists('responsive', $this)) {
            throw new PrestaShopException('property "responsive" is missing in object '.get_class($this));
        }

        // Update only responsive field
        $this->setFieldsToUpdate(array('responsive' => true));

        // Update active responsive on object
        $this->responsive = !(int)$this->responsive;

        // Change responsive to active/inactive
        return $this->update(false);
    }

    public function toggleDefaultLeftColumn()
    {
        if (!array_key_exists('default_left_column', $this)) {
            throw new PrestaShopException('property "default_left_column" is missing in object '.get_class($this));
        }

        $this->setFieldsToUpdate(array('default_left_column' => true));

        $this->default_left_column = !(int)$this->default_left_column;

        return $this->update(false);
    }

    public function toggleDefaultRightColumn()
    {
        if (!array_key_exists('default_right_column', $this)) {
            throw new PrestaShopException('property "default_right_column" is missing in object '.get_class($this));
        }

        $this->setFieldsToUpdate(array('default_right_column' => true));

        $this->default_right_column = !(int)$this->default_right_column;

        return $this->update(false);
    }*/
}
