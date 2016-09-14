package com.jeprolab.assets.extend.controls;

import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabLanguageModel;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproMultiLangTextField extends Pane {
    private Map<Integer, JeproLabLanguageModel> languages;
    private TextField[] fields;
    private StackPane fieldsPane;
    private ComboBox<String> languageSelector;

    public JeproMultiLangTextField(){
        HBox multiLangFieldWrapper = new HBox();
        int index = 0;
        languages = JeproLabLanguageModel.getLanguages();
        fields = new TextField[languages.size()];
        fieldsPane = new StackPane();

        languageSelector = new ComboBox<>();
        languageSelector.setPrefWidth(75);
        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            TextField field = new TextField();
            field.setId("language_" + language.language_id);
            fieldsPane.getChildren().add(field);
            fields[index] = field;
            index++;
            languageSelector.getItems().add(language.language_code);
            if (language.is_default) {
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

    public void  setTextPrefSize(double width, double height){
        width -= 75;
        for (TextField field : fields) {
            field.setPrefSize(width, height);
        }
    }

    public void setText(Map<String, String> value){
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }
        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
            for (TextField field : fields) {
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

    public void clearFields(){
        for (TextField field : fields) {
            field.setText("");
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

    public Map<String, String> getDataContent(){
        Map<String, String> content = new HashMap<>();
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }
        for(Object o : languages.entrySet()){
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel)lang.getValue();
            for(TextField field : fields){
                if(field.getId().equals("language_" + language.language_id)){
                    content.put("lang_" + language.language_id, field.getText());
                }
            }
        }
        return content;
    }

    public void setTextAlignment(String... alignClass){
        for(TextField field : fields){
            field.getStyleClass().addAll(alignClass);
        }
    }

    public TextField[] getFields(){
        return fields;
    }

}
