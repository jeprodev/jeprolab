package com.jeprolab.assets.tools;


public class JeproLabCookie {
    public int lang_id;

    public int employee_id;

    public int currency_id;

    public int customer_id;

    public int analyzes_filter_category_id = 0;

    public boolean secure = false;

    public String path = "";

    public String domain = "";

    public static JeproLabCookie getCookie(){
        return new JeproLabCookie();
    }

    //todo to be correct
    public String get(String field){
        return "";
    }
}