package com.jeprolab.assets.extend.controls;

import com.jeprolab.models.JeproLabLanguageModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 *
 * Created by jeprodev on 09/06/2014.
 */
public class JeproMultiLangTextField extends Pane{
    private static List languages;
    private TextField [] fields;
    private StackPane fieldsPane;
    private ComboBox languageSelector;

    public JeproMultiLangTextField(){
        HBox multiLangFieldWrapper = new HBox();
        languageSelector = new ComboBox();
        fieldsPane = new StackPane();
        languages = JeproLabLanguageModel.getLanguages();
        multiLangFieldWrapper.getChildren().addAll(fieldsPane, languageSelector);
        this.getChildren().add(multiLangFieldWrapper);
    }
}