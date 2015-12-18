package com.jeprolab.models;


import javafx.scene.control.Pagination;

public class JeproLabCountryModel  extends JeproLabModel{
    public int country_id = 0;

    public int lang_id = 0;

    public int lab_id = 0;

    public int zone_id = 0;

    public int currency_id = 0;

    public char[] isoCode = new char[3];

    public String name;

    public int callPrefix;

    public boolean published = false;

    public boolean containsStates = false;

    public boolean needIdentificationNumber = false;

    public boolean needZipCode = false;

    public boolean displayTaxLabel = false;

    public boolean getLabFromContext = false;

    public boolean multiLang = true;

    public boolean multiLangLab = true;

    public String zipCodeFormat;

    private Pagination pagination = null;

    public JeproLabCountryModel(){
        this(0, 0, 0);
    }

    public JeproLabCountryModel(int country_id){
        this(country_id, 0, 0);
    }

    public JeproLabCountryModel(int country_id, int lang_id){
        this(country_id, lang_id, 0);
    }

    public JeproLabCountryModel(int country_id, int lang_id, int lab_id){

    }
}