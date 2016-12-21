package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 28/02/2014.
 */
public class JeproLabThemeModel extends JeproLabModel {
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

    public static List<JeproLabThemeModel> getThemes(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_theme") + " ORDER BY " + dataBaseObject.quoteName("theme_name");
        dataBaseObject.setQuery(query);

        ResultSet themesSet = dataBaseObject.loadObjectList();
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
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return themeList;
    }
}
