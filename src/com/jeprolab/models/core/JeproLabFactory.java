package com.jeprolab.models.core;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.JeproLabEmployeeModel;

/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public abstract class JeproLabFactory {
    private static JeproLabDataBaseConnector dataBaseConnector;

    private static JeproLabSession appSession;

    public static JeproLabApplication application = null;

    public static JeproLabConfig appConfig;

    public static JeproLabDataBaseConnector getDataBaseConnector(){
        if(dataBaseConnector == null){
            dataBaseConnector = new JeproLabDataBaseConnector(JeproLabConfig.dataBaseHost, JeproLabConfig.dataBaseManager, JeproLabConfig.dataBaseName);
        }
        return dataBaseConnector;
    }

    /**
     * Get Lab employee
     * Returns the global {@link JeproLabEmployeeModel} object, only creating it if does'nt already exist.
     *
     * @return an employee object with no id
     */
    public static JeproLabEmployeeModel getEmployee(){
        return getEmployee(0);
    }

    /**
     * Get Lab employee
     * Returns the global {@link JeproLabEmployeeModel} object, only creating it if does'nt already exist.
     *
     * @param employeeId integer employee id of the employee to be loaded
     * @return JeproLabEmployeeModel object
     */
    public static JeproLabEmployeeModel getEmployee(int employeeId){
        JeproLabEmployeeModel employee = JeproLabFactory.getSession(null).getEmployee();

        if(employeeId == 0){
            if(employee == null) {
                employee = new JeproLabEmployeeModel();
            }
        }else if((employee == null) || employeeId > 0 || (employee.employee_id != employeeId)){
            employee = new JeproLabEmployeeModel(employeeId);
        }
        return employee;
    }

    /**
     * Get current session object
     *
     * Returns the global {@link JeproLabSession} object, only creating it if doesn't already exist
     * @return JeproLabSession object
     */
    public static JeproLabSession getSession(JeproLabSessionOption sessionOptions){
        if(appSession == null){
            appSession = JeproLabFactory.createSession(sessionOptions);
        }
        return appSession;
    }

    /**
     * create a session object
     *
     * @param  sessionOptions
     *
     * @return JeproLabSession object
     */
    protected static JeproLabSession createSession(JeproLabSessionOption sessionOptions){
        JeproLabConfig config = JeproLabFactory.getConfig();
        String sessionHandler = config.getSessionHandler();

        if(sessionOptions == null){
            sessionOptions = new JeproLabSessionOption();
        }

        //configuration time in minute
        sessionOptions.expire =  config.getLifeTime() > 0 ? config.getLifeTime() * 60 : 900;

        appSession = JeproLabSession.getInstance(sessionHandler, sessionOptions);

        if(appSession.getState().equals("expired")){
            appSession.restart();
        }
        return appSession;
    }

    /**
     * return application configuration object
     * @return
     */
    public static JeproLabConfig getConfig(){
        return getConfig("");
    }

    public static JeproLabConfig getConfig(String filePath){
        if(appConfig == null){
            if(filePath == null || filePath.equals("")){
                filePath = "config.properties";
            }
            JeproLabConfig.setConfig(filePath);
            appConfig = JeproLabConfig.getConfiguration();
        }
        return appConfig;
    }
}