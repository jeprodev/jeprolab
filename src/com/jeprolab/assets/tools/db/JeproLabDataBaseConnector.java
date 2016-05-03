package com.jeprolab.assets.tools.db;

import com.jeprolab.assets.config.JeproLabConfig;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabDataBaseConnector {
    private String dataBaseUserName;
    private String dataBasePassWord;
    private String dataBaseName;
    private String dataBaseUrl;
    private int dataBasePortNumber = 3306;
    private static JeproLabDataBaseConnector instance;
    private int generatedKey = -2;

    private Connection connection;
    private String hostName;
    private String driverName;

    private String queryRequest;

    public JeproLabDataBaseConnector(String server, String driver, String dataBase) {
        hostName = server;
        dataBaseName = dataBase;
        setDriverManager(driver, dataBase, dataBasePortNumber);
        instance = this;
    }

    public void setDriverManager(String driver, String dataBaseName, int portNumber){
        switch(driver){
            case "mysql" :
                driverName = "com.mysql.jdbc.Driver";
                dataBaseUrl = "jdbc:mysql://" + hostName + ":" + portNumber + "/" + dataBaseName;
                break;
            case "oracle" :
                driverName = "oracle.jdbc.driver.OracleDriver";
                dataBaseUrl = "jdbc:oracle:thin:@" + hostName + ":" + portNumber + ":" + dataBaseName;
                break;
            case "db2" :
                driverName = "COM.ibm.db2.jdbc.net.DB2Driver";
                dataBaseUrl = "jdbc:db2:" + hostName + ":" + portNumber + "/" + dataBaseName;
                break;
            case "sybase" :
                driverName = "com.sybase.jdbc.SybDriver";
                dataBaseUrl = "jdbc:sybase:tds:" + hostName + ":" + portNumber + "/" + dataBaseName;
                break;
            default:
                System.out.println("Please select a data base driver");
                break;
        }
    }

    public Connection getConnexion(){
        if(connection == null){
            try {
                /** JDBC Registration **/
                Class.forName(driverName);

                connection = DriverManager.getConnection(dataBaseUrl, JeproLabConfig.dataBaseUserName, JeproLabConfig.dataBasePassword);

                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
            }
        }else{
            return connection;
        }
    }

    public String quote(String msg){
        return quote(msg, true);
    }

    public String quote(String msg, boolean escape){
        return (escape ? "\'" : "'") + msg + (escape ? "\'" : "'");
    }

    public ArrayList<String> quote(ArrayList<String> fields){
        return quote(fields, true);
    }


    public ArrayList<String> quote(ArrayList<String> fields, boolean escape){
        ArrayList<String> quotedField = new ArrayList<>();
        for (String field : fields){
            quotedField.add((escape ? "\'" : "'") + field + (escape ? "\'" : "'"));
        }
        return quotedField;
    }

    public String quoteName(String msg){
        return  "`" + msg + "`";
    }

    public String quoteName(String msg, boolean setAs, String alias){
        String quoted = "`" + msg + "`";
        String quotedAs = "";
        //if(setAs != null && !setAs.equals("")){
        quotedAs += " AS "; // + this.quoteNameStr();
        //}
        return quoted + quotedAs;
    }

    public String quoteNameStr(String[] msgText){

        String parts = "";
        for(String msg : msgText){
            if(msg == null){
                continue;
            }
            parts += ".`" + msg + "`";
        }
        return parts;
    }

    public String quoteBaseName(String baseName){
        return baseName;
    }

    public String getDataBaseName(){
        return dataBaseName;
    }

    public static JeproLabDataBaseConnector getInstance() throws Exception {
        if(instance == null){
            throw new Exception("you can't continue without connection");
        }
        return  instance;
    }

    public void setQuery(String query){
        setQuery(query, 0, 0);
    }

    public void setQuery(String query, int offSet, int limit){
        this.queryRequest = query;
        this.queryRequest = this.queryRequest.replace("#__", JeproLabConfig.dataBasePrefix);
        if(limit > 0 || offSet > 0){
            queryRequest += " LIMIT " + offSet + ", " + limit;
        }
    }

    public ResultSet loadObjectList(){
        ResultSet results;
        try {
            /** JDBC Registration **/
            Class.forName(driverName);

            connection = DriverManager.getConnection(dataBaseUrl, JeproLabConfig.dataBaseUserName , JeproLabConfig.dataBasePassword);
            results = connection.createStatement().executeQuery(queryRequest);

            queryRequest = null;
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean query(boolean isInsert){
        try{
            /** JDBC Registration **/
            Class.forName(driverName);

            connection = DriverManager.getConnection(dataBaseUrl, JeproLabConfig.dataBaseUserName , JeproLabConfig.dataBasePassword);
            if (isInsert) {
                PreparedStatement statement = connection.prepareStatement(queryRequest, Statement.RETURN_GENERATED_KEYS);
                boolean result = statement.execute();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                generatedKey = generatedKeys.getInt(1);
                statement.close();
                return result;
            }else {
                return connection.createStatement().execute(queryRequest);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }catch (Exception exc){
            exc.printStackTrace();
            return false;
        }
    }

    public double loadValue(String key){
        ResultSet result = loadObjectList();
        try{
            if(result.next()){
                return result.getDouble(key);
            }
        }catch(SQLException ignored) {
            return 0;
        }
        return 0;
    }

    public String loadStringValue(String key){
        ResultSet result = loadObjectList();
        try{
            if(result.next()){
                return result.getString(key);
            }
        }catch(SQLException ignored) {
            return "";
        }finally {
            closeConnexion();
        }
        return "";
    }

    public void closeConnexion(){
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public int getGeneratedKey(){
        return generatedKey;
    }


}
