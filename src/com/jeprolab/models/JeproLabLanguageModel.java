package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
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

    public JeproLabLanguageModel(int langId){
        super();
        if(langId > 0){
            String cacheKey = "jeprolab_language_model_" + langId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__languages") + " lang WHERE lang.lang_id = ";
                query += langId ;

                dataBaseObject.setQuery(query);
                ResultSet languageDataSet = dataBaseObject.loadObjectList();
                //String default_language = JeproLabLanguageModel.getDefaultLanguage().language_code;
                int defaultLangId = JeproLabSettingModel.getIntValue("default_lang");
                try{
                    while(languageDataSet.next()){
                        this.language_id = languageDataSet.getInt("lang_id");
                        this.language_code = languageDataSet.getString("lang_code");
                        this.name = languageDataSet.getString("title");
                        this.iso_code = languageDataSet.getString("sef");
                        this.is_default = this.language_id == defaultLangId;
                        this.published = (languageDataSet.getInt("published") > 0) ;
                    }

                    JeproLabCache.getInstance().store(cacheKey, this);
                }catch (SQLException ignored){
                    JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
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

    public static boolean checkLanguage(int lang_id){
        return JeproLabLanguageModel.LANGUAGES.containsKey(lang_id);
    }

    /**
     * Return iso code from id
     *
     * @param langId Language ID
     * @return string Iso code
     */
    public static String getIsoCodeByLanguageId(int langId){
        if (LANGUAGES.containsKey(langId)) {
            return LANGUAGES.get(langId).iso_code;
        }
        return "";
    }

    public static void loadLanguages() {
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        JeproLabLanguageModel.LANGUAGES = new HashMap<>();

        String query = " SELECT * FROM " + dataBaseObject.quoteName("#__languages") ;

        dataBaseObject.setQuery(query);
        ResultSet languages = dataBaseObject.loadObjectList();

        String default_language = JeproLabLanguageModel.getDefaultLanguage().language_code;
        if(languages != null) {
            try {
                while (languages.next()) {
                    JeproLabLanguageModel lang = new JeproLabLanguageModel();
                    lang.language_id = languages.getInt("lang_id");
                    lang.language_code = languages.getString("lang_code");
                    lang.name = languages.getString("title");
                    lang.iso_code = languages.getString("sef");

                    lang.published = (languages.getInt("published") > 0);
                    lang.is_default = lang.language_code.equals(default_language);

                    JeproLabLanguageModel.LANGUAGES.put(lang.language_id, lang);
                }
            } catch (SQLException ignored) {
                JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored) {
                    JeproLabTools.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
    }

    private static JeproLabLanguageModel getDefaultLanguage(){
        return new JeproLabLanguageModel(JeproLabSettingModel.getIntValue("default_lang"));
    }

    public static Map<Integer, JeproLabLanguageModel> getLanguages() {
        if (!LANGUAGES.isEmpty()) {
            JeproLabLanguageModel.loadLanguages();
        }
        return LANGUAGES;
    }

    public static JeproLabLanguageModel getLanguageByIsoCode(String code){
        if(LANGUAGES == null){
            loadLanguages();
        }
        JeproLabLanguageModel language = null;
        for(Map.Entry entry : LANGUAGES.entrySet()){
            if(((JeproLabLanguageModel)(entry.getValue())).iso_code.equals(code)){
                language = (JeproLabLanguageModel)(entry.getValue());
                break;
            }
        }
        return language;
    }
}
