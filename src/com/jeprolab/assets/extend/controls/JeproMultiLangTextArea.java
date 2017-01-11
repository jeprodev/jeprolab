package com.jeprolab.assets.extend.controls;

import com.jeprolab.models.JeproLabLanguageModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproMultiLangTextArea extends Pane {
    private Map<Integer, JeproLabLanguageModel> languages;
    private TextArea[] fields;
    private StackPane fieldsPane;
    private ComboBox<String> languageSelector;

    public JeproMultiLangTextArea(){
        HBox multiLangFieldWrapper = new HBox();
        int index = 0;
        languages = JeproLabLanguageModel.getLanguages();
        fields = new TextArea[languages.size()];
        fieldsPane = new StackPane();

        languageSelector = new ComboBox<>();
        languageSelector.setPrefWidth(75);
        Iterator langIt = languages.entrySet().iterator();

        while(langIt.hasNext()){
            Map.Entry lang = (Map.Entry)langIt.next();
            JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
            TextArea field = new TextArea();
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

    public TextArea[] getFields(){
        return fields;
    }

    public Map<String, String> getDataContent(){
        Map<String, String> content = new HashMap<>();
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }
        for(Object o : languages.entrySet()){
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
            for(TextArea field : fields){
                if(field.getId().equals("language_" + language.language_id)){
                    content.put("lang_" + language.language_id, field.getText());
                }
            }
        }
        return content;
    }

    public void clearFields(){
        for (TextArea field : fields) {
            field.setText("");
        }
    }

    public String getFieldContent(int langId){
        for(TextArea field : fields){
            if(field.getId().equals("language_" + langId)){
                return (field.getText() == (null) ? " " : field.getText()) ;
            }
        }
        return "";
    }

    public void setText(Map<String, String> value){
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }
        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
            for (TextArea field : fields) {
                if (field.getId().equals("language_" + language.language_id) && value != null) {
                    field.setText(value.containsKey("lang_" + language.language_id) ? value.get("lang_" + language.language_id) : "");
                }else{
                    if(value == null){
                        field.setText("");
                    }
                }
            }
        }
    }

    public void  setTextPrefSize(double width, double height){
        width -= 75;
        for (TextArea field : fields) {
            field.setPrefSize(width, height);
            field.setWrapText(true);
        }
    }
}
