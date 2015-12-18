package com.jeprolab.models;


import jeprolab.assets.db.JeproLabDataBaseManager;
import jeprolab.assets.tools.JeproLabContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JeproLabEmployeeModel  extends JeproLabModel{
    public boolean isLogged = false;

    public static JeproLabEmployeeModel getEmployeeFromLogin(String userName, String password){

        Connection conn = JeproLabContext.getContext().connection;
        try {
            if(conn != null) {
                JeproLabDataBaseManager dbManager = JeproLabDataBaseManager.getInstance();
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM users  WHERE username = " + JeproLabDataBaseManager.quote(userName) + " AND password = ";
                query += dbManager.quote(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JeproLabEmployeeModel employee;
        if(conn != null){


            return employee;
        }else{
            return null;
        }
    }

    //todo code me
    public void setCookieLogin(boolean setCookie){

    }
}