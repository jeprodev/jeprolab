package com.jeprolab.models;



import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

import com.jeprolab.assets.tools.JeproLabContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JeproLabEmployeeModel  extends JeproLabModel{
    public boolean isLogged = false;

    public int employee_id ;

    public int profile_id;

    public String password;

    public JeproLabEmployeeModel(){
        this(0);
    }

    public JeproLabEmployeeModel(int customerId){
        this(customerId, 0);
    }

    public JeproLabEmployeeModel(int customerId, int langId){
        this(customerId, langId, 0);
    }

    public JeproLabEmployeeModel(int employeeId, int langId, int labId){

    }

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

    public void save(){

    }

    //todo code me
    public void setCookieLogin(boolean setCookie){

    }
}