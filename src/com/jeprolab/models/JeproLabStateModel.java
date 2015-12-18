package com.jeprolab.models;


import javafx.scene.control.Pagination;

public class JeproLabStateModel extends JeproLabModel{
    public int state_id = 0;

    public int country_id = 0;

    public int zone_id = 0;

    public char[] isoCode = new char[2];

    public String name;

    public boolean published = false;

    private Pagination pagination = null;

    public JeproLabStateModel(){
        this(0);
    }

    public JeproLabStateModel(int stateId){

    }
}