package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabCountryModel extends JeproLabModel{
    public int country_id = 0;

    public int language_id = 0;

    public int laboratory_id = 0;

    public int zone_id = 0;

    public int currency_id = 0;

    public String iso_code = "";

    public Map<String, String> name;

    public String call_prefix;

    public boolean published = false;

    public boolean contains_states = false;

    public boolean need_identification_number = false;

    public boolean need_zip_code = false;

    public String zip_code_format;

    public boolean display_tax_label = false;

    public JeproLabCountryModel(){
        this(0, 0, 0);
    }

    public JeproLabCountryModel(int countryId){
        this(countryId, 0, 0);
    }

    public JeproLabCountryModel(int countryId, int langId){
        this(countryId, langId, 0);
    }

    public JeproLabCountryModel(int countryId, int langId, int labId){
        if(langId > 0){
            this.language_id = (JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang"));
        }

        if(labId > 0){
            this.laboratory_id = labId;
            this.get_lab_from_context = false;
        }

        if(this.laboratory_id <= 0){
            this.laboratory_id = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        name = new HashMap<>();
        Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

        if(countryId > 0){
            String cacheKey = "jeprolab_country_model_" + countryId + '_' + langId + '_' + labId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_country") + " AS country ";

                //Get language data
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON (country_lang.";
                    query +=  dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id");
                    query += " AND country_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ")";
                }

                if(labId < 0 && JeproLabLaboratoryModel.isTableAssociated("country")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_country_lab") + " AS country_lab ON (country_lab.";
                    query += dataBaseObject.quoteName("country_id") + " = country." + dataBaseObject.quoteName("country_id") + " AND country_lab.";
                    query += dataBaseObject.quoteName("lab_id") + " = " + labId + ") ";
                }
                query += " WHERE country.country_id = " + countryId;

                dataBaseObject.setQuery(query);
                ResultSet countryData = dataBaseObject.loadObjectList();

                try {
                    if(countryData.next()) {
                        this.country_id =  countryData.getInt("country_id");
                        this.zone_id = countryData.getInt("zone_id");
                        this.currency_id = countryData.getInt("currency_id");
                        this.iso_code = countryData.getString("iso_code");
                        this.call_prefix = countryData.getString("call_prefix");
                        this.published = countryData.getInt("published") > 0;
                        this.contains_states = countryData.getInt("contains_states") > 0;
                        this.need_identification_number = countryData.getInt("need_identification_number") > 0;
                        this.need_zip_code = countryData.getInt("need_zip_code") > 0;
                        this.zip_code_format = countryData.getString("zip_code_format");
                        this.display_tax_label = countryData.getInt("display_tax_label") > 0;

                        if (langId <= 0) {
                            query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_country_lang");
                            query += " WHERE " + dataBaseObject.quoteName("country_id") + " = " + country_id;

                            dataBaseObject.setQuery(query);
                            ResultSet countryLangData = dataBaseObject.loadObjectList();
                            while(countryLangData.next()) {
                                int languageId = countryLangData.getInt("lang_id");
                                String countryName = countryLangData.getString("name");
                                for (Object o : languages.entrySet()) {
                                    Map.Entry lang = (Map.Entry) o;
                                    JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
                                    if (languageId == language.language_id) {
                                        name.put("lang_" + languageId, countryName);
                                    }
                                }
                            }
                        }else{
                            name.put("lang_" + langId, countryData.getString("name"));
                        }
                        JeproLabCache.getInstance().store(cacheKey, this);
                    }
                }catch (SQLException ignored){
                    JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored) {
                        JeproLabTools.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }else{
                JeproLabCountryModel country = (JeproLabCountryModel) JeproLabCache.getInstance().retrieve(cacheKey);
                this.country_id = country.country_id;
                this.zone_id = country.country_id;
                this.currency_id = country.currency_id;
                this.iso_code = country.iso_code;
                this.call_prefix = country.call_prefix;
                this.published = country.published;
                this.contains_states = country.contains_states;
                this.need_zip_code = country.need_zip_code;
                this.need_identification_number = country.need_identification_number;
                this.zip_code_format = country.zip_code_format;
                this.display_tax_label = country.display_tax_label;
                this.name = country.name;
            }
        }
    }

}
