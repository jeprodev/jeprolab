package com.jeprolab.assets.tools;


import com.jeprolab.models.JeproLabModel;

public class JeproLabTools {

    public static boolean isLoadedObject(JeproLabModel model, String key){
        return false;
    }

    public static void displayError(int errorCode, String errorMessage){

    }

    public static boolean isOrderBy(String order){
        String pattern = "/^[a-zA-Z0-9._-]+$/";
        return order.matches(pattern);
    }

    public static boolean isOrderWay(String way){
        return (way.toLowerCase().equals("asc") | way.toLowerCase().equals("desc"));
    }
    /*public static boolean (){}
    public static boolean (){}
    public static boolean (){}*/
}