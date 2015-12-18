package com.jeprolab.assets.tools;


public class JeproLabCookie {
    public int lang_id;

    public int employee_id;

    public int currency_id;

    public int customer_id;

    public static JeproLabCookie getCookie(){
        return new JeproLabCookie();
    }
}