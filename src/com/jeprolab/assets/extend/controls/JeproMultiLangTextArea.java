package com.jeprolab.assets.extend.controls;

import com.jeprolab.models.JeproLabLanguageModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Map;

/**
 *
 * Created by jeprodev on 24/01/2014.
 */
public class JeproMultiLangTextArea extends Pane {
    private Map<Integer, JeproLabLanguageModel> languages;
    private TextArea [] fields;
    private StackPane fieldsPane;
    private ComboBox<String> languageSelector;

    public JeproMultiLangTextArea() {
        HBox multiLangFieldWrapper = new HBox();
        int index = 0;
        languages = JeproLabLanguageModel.getLanguages();
        fields = new TextArea[languages.size()];
        fieldsPane = new StackPane();

        languageSelector = new ComboBox<>();
        languageSelector.setPrefWidth(75);
        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            TextArea field = new TextArea();
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


    public void setTextAreaPrefSize(double width, double height){
        width = width - 75;
        for (TextArea field : fields) {
            field.setPrefSize(width, height);
        }
    }

    public void setText(Map<String, String> value){
        if(languages == null){
            languages = JeproLabLanguageModel.getLanguages();
        }
        for (Object o : languages.entrySet()) {
            Map.Entry lang = (Map.Entry) o;
            JeproLabLanguageModel language = (JeproLabLanguageModel) lang.getValue();
            for (TextArea field : fields) {
                if (field.getId().equals("language_" + language.language_id) && value != null){
                    field.setText(value.containsKey("lang_" + language.language_id) ? value.get("lang_" + language.language_id) : "");
                }
            }
        }
    }

    public void clearFields(){
        for (TextArea field : fields) {
            field.setText("");
        }
    }

    public String getFieldContent(int langId){
        for(TextArea field : fields){
            if(field.getId().equals("language_" + langId)){
                return (field.getText() == null) ? " " : field.getText();
            }
        }
        return "";
    }

}
