package com.jeprolab.assets.tools;


import java.sql.Connection;
import java.sql.DriverManager;

public class JeproLabDataBaseConnector {
    private static JeproLabDataBaseConnector instance = null;
    private Connection connexion = null;
    private String dataBaseUrl;
    private  String dataBaseDriver;

    public Connection getConnexion(String userName, String passWord){
        try{
            Class.forName(dataBaseDriver).newInstance();
            connexion = DriverManager.getConnection(dataBaseUrl, userName, passWord);
        }catch(Exception e){
            System.out.println("unable to get connection to the data base");
        }finally {
            if(connexion != null){
                try{
                    connexion.close();
                }catch (Exception ex){
                    System.out.println("unable to close connection ");
                }
            }
        }
        return connexion;
    }

    /*public void setDataBaseUrl(String manager, String hostName, String dataBaseName, int portNumber){
        switch (manager) {
            case "mysql":
                dataBaseDriver = "com.mysql.jdbc.Driver";
                dataBaseUrl = "jdbc:mysql://" + hostName + "/" + dataBaseName;
                break;
            case "oracle":
                dataBaseDriver = "oracle.jdbc.driver.OracleDriver";
                dataBaseUrl = "jdbc:oracle:thin:@" + hostName + ":" + portNumber + ":" + dataBaseName;
                break;
            case "db2":
                dataBaseDriver = "COM.ibm.db2.jdbc.net.DB2Driver";
                dataBaseUrl = "jdbc:db2:" + hostName + ":" + portNumber + "/" + dataBaseName;
                break;
            case "sybase":
                dataBaseDriver = "com.sybase.jdbc.SybDriver";
                dataBaseUrl = "jdbc:sybase:Tds:" + hostName + ":" + portNumber + "/" + dataBaseName;
                break;
        }
    }*/

    public static JeproLabDataBaseConnector getDataBaseObject(){
        return instance;
    }

    public String quoteName(String key){
        return key;
    }
}