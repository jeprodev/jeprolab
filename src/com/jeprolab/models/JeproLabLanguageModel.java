package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JeproLabLanguageModel extends JeproLabModel{
    public int language_id;

    public String name;

    public String iso_code;

    public String language_code;

    public boolean is_default = false;

    public boolean published;

    public boolean is_rtl = true;

    public static Map<Integer, JeproLabLanguageModel> LANGUAGES;

    public JeproLabLanguageModel(){
        this(0);
    }

    public JeproLabLanguageModel(int lang_id){
        super();
        if(lang_id > 0){
            String cacheKey = "jeprolab_language_model_" + lang_id;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__languages") + " lang WHERE lang.lang_id = ";
                query += lang_id ;

                dataBaseObject.setQuery(query);
                ResultSet language_data = dataBaseObject.loadObject();
                //String default_language = JeproLabLanguageModel.getDefaultLanguage().language_code;
                try{
                    while(language_data.next()){
                        this.language_id = language_data.getInt("lang_id");
                        this.language_code = language_data.getString("lang_code");
                        this.name = language_data.getString("title");
                        this.iso_code = language_data.getString("sef");
                        this.is_default = this.language_id == JeproLabSettingModel.getIntValue("default_lang");
                        this.published = (language_data.getInt("published") > 0) ;
                    }

                    JeproLabCache.getInstance().store(cacheKey, this);
                }catch (SQLException ignored){

                }
            }else{
                JeproLabLanguageModel language = (JeproLabLanguageModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.language_id = language.language_id;
                this.language_code = language.language_code;
                this.name = language.name;
                this.iso_code = language.iso_code;
                this.is_default = language.is_default;
                this.published = language.published;
            }
        }
    }

    public static void loadLanguages() {
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        JeproLabLanguageModel.LANGUAGES = new HashMap<>();

        String query = " SELECT * FROM " + staticDataBaseObject.quoteName("#__languages") ;

        staticDataBaseObject.setQuery(query);
        ResultSet languages = staticDataBaseObject.loadObject();

        String default_language = JeproLabLanguageModel.getDefaultLanguage().language_code;
        //if(languages)
        try{
            while(languages.next()){
                JeproLabLanguageModel lang = new JeproLabLanguageModel();
                lang.language_id =  languages.getInt("lang_id");
                lang.language_code = languages.getString("lang_code");
                lang.name = languages.getString("title");
                lang.iso_code = languages.getString("sef");

                lang.published = (languages.getInt("published") > 0) ;
                lang.is_default = lang.language_code.equals(default_language);

                JeproLabLanguageModel.LANGUAGES.put(lang.language_id, lang);
            }
        }catch (SQLException ignored){
            ignored.printStackTrace();
        }
    }

    private static JeproLabLanguageModel getDefaultLanguage(){
        return new JeproLabLanguageModel(JeproLabSettingModel.getIntValue("default_lang"));
    }

    public static boolean checkLanguage(int lang_id){
        if(JeproLabLanguageModel.LANGUAGES.containsKey(lang_id)){
            return true;
        }
        return false;
    }

    public static Map<Integer, JeproLabLanguageModel> getLanguages(){
        if(!LANGUAGES.isEmpty()){
            JeproLabLanguageModel.loadLanguages();
        }
    /*
        $default_language = JeproLabLanguageModel.getDefaultLanguage();

        $languages = array();
        foreach(self::$_LANGUAGES as $language){
            if($published && !$language->published || ($shop_id && !isset($language->shops[(int)$shop_id]))){ continue; }

            if($default_language->getTag() == $language->language_code ){$language->is_default = 1; }

            $languages[] = $language;
        }
        return $languages;*/
        return LANGUAGES;
    }

    public boolean isAssociatedToLab(){
        return  isAssociatedToLab(0);
    }

    public boolean isAssociatedToLab(int labId){
        if(labId == 0){
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }
        boolean isAssociated = false;

        String cacheId =  "jeprolab_model_lab_language_" + this.language_id + "_" + labId;
        if(!JeproLabCache.getInstance().isStored(cacheId)){
            JeproLabDataBaseConnector dataBaseConnector = JeproLabFactory.getDataBaseConnector();
            String query = "SELECT lab_id FROM " + dataBaseConnector.quoteName("#__jeprolab_language_lab") + " WHERE ";
            query += dataBaseConnector.quoteName("lang_id") + " = " + this.language_id + " AND " + dataBaseConnector.quoteName("lab_id") + labId;

            dataBaseConnector.setQuery(query);
            ResultSet object = dataBaseConnector.loadObject();
            try{
                while(object.next()){
                    isAssociated = object.getInt("lab_id") > 0;
                }
            }catch(SQLException exc){
                isAssociated = false;
            }
            JeproLabCache.getInstance().store(cacheId, isAssociated);
        }else{
            isAssociated = (boolean)JeproLabCache.getInstance().retrieve(cacheId);
        }
        return isAssociated;
    }


    public static class JeproLabLanguage{
        private int language_id;
        private String language_name;

        public JeproLabLanguage(JeproLabLanguageModel language){
            this(language.language_id, language.name);
        }

        public JeproLabLanguage(int id, String name){
            language_id = id;
            language_name = name;
        }

        public String getName(){
            return this.language_name;
        }

        /*public void setLanguageName(String name){
            this.language_name = name;
        }*/

        public int getLanguageId(){
            return this.language_id;
        }

        /*public void setLanguangeI(int lang_id){
            this.language_id = lang_id;
        }*/
    }
}