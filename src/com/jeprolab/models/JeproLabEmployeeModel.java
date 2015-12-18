package com.jeprolab.models;


import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

import com.jeprolab.assets.tools.JeproLabContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JeproLabEmployeeModel  extends JeproLabModel{
    public boolean isLogged = false;

    public static JeproLabEmployeeModel getEmployeeFromLogin(String userName, String password){

        Connection conn = JeproLabContext.getContext().connection;
        try {
            if(conn != null) {
                JeproLabDataBaseConnector dbManager = JeproLabDataBaseConnector.getInstance();
                Statement stmt = conn.createStatement();
                //String query = "SELECT * FROM users  WHERE username = " + JeproLabDataBaseConnector.quote(userName) + " AND password = ";
                //query += dbManager.quote(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JeproLabEmployeeModel employee;
        if(conn != null){


            return null; //employee;
        }else{
            return null;
        }
    }

    //todo code me
    public void setCookieLogin(boolean setCookie){

    }
}