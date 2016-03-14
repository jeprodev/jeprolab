package com.jeprolab.assets.extend.controls;

import com.jeprolab.models.JeproLabLanguageModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/06/2014.
 */
public class JeproMultiLangTextField extends Pane{
    private Map<Integer, JeproLabLanguageModel> languages;
    private TextField [] fields;
    private StackPane fieldsPane;
    private ComboBox languageSelector;

    public JeproMultiLangTextField(){
        HBox multiLangFieldWrapper = new HBox();
        int index = 0;
        languages = JeproLabLanguageModel.getLanguages();
        fields = new TextField[languages.size()];
        fieldsPane = new StackPane();

        languageSelector = new ComboBox<>();
        languageSelector.setPrefWidth(75);
        Iterator langIt = languages.entrySet().iterator();
        while(langIt.hasNext()){
            Map.Entry lang = (Map.Entry)langIt.next();
            JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
            TextField field = new TextField();
            field.setId("language_" + language.language_id);
            fieldsPane.getChildren().add(field);
            fields[index] = field;
            index++;
            languageSelector.getItems().add(language.language_code);
            if(language.is_default){
                languageSelector.setValue(language.language_code);
            }
        }
        multiLangFieldWrapper.getChildren().addAll(fieldsPane, languageSelector);
        this.getChildren().add(multiLangFieldWrapper);
    }

    @Override
    public void  setWidth(double width){
        width -= 75;
        for (TextField field : fields) {
            field.setPrefWidth(width);
        }
    }

    public void setText(Map<String, String> value){
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }
        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            for (TextField field : fields) {
                if (field.getId().equals("language_" + language.language_id) && value != null) {
                    field.setText(value.containsKey("lang_" + language.language_id) ? value.get("lang_" + language.language_id) : "");
                }
            }
        }
    }

    public String getFieldContent(int langId){
        for(TextField field : fields){
            if(field.getId().equals("language_" + langId)){
                return (field.getText() == (null) ? " " : field.getText()) ;
            }
        }
        return "";
    }
}