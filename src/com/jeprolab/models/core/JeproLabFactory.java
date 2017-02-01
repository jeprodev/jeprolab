package com.jeprolab.models.core;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.JeproLabEmployeeModel;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFactory {
    private static List<JeproLabDataBaseConnector> dataBaseConnectorPool = new ArrayList<>();

    private static final int NUMBER_OF_CONNECTION = 5;

    private static JeproLabSession appSession;

    public static JeproLabConfig appConfig;

    public static JeproLabDataBaseConnector getDataBaseConnector() {
        JeproLabDataBaseConnector connector;
        if (!checkIfConnectionIsFull()){
            connector = new JeproLabDataBaseConnector(JeproLabConfig.DATA_BASE_HOST,
                JeproLabConfig.DATA_BASE_DRIVER, JeproLabConfig.DATA_BASE_NAME);
            if(addConnector(connector)){
                return connector;
            }
        }/*else{
            /*Worker<JeproLabDataBaseConnector> worker = new Task<JeproLabDataBaseConnector>() {
                @Override
                protected JeproLabDataBaseConnector call() throws Exception {
                    JeproLabDataBaseConnector connect = new JeproLabDataBaseConnector(JeproLabConfig.DATA_BASE_HOST,
                        JeproLabConfig.DATA_BASE_DRIVER, JeproLabConfig.DATA_BASE_NAME);
                    while (true){
                        if(!checkIfConnectionIsFull() && addConnector(connect)){
                            return connect;
                        }
                        Thread.sleep(2000);
                    }
                    //return null;
                }
            };

            new Thread((Task)worker).start();

            ((Task)worker).setOnSucceeded(evt -> {
                connector = worker.valueProperty()
;            });* /
        } */
        return null;
    }

    private synchronized static boolean checkIfConnectionIsFull(){
        return dataBaseConnectorPool.size() >= NUMBER_OF_CONNECTION;
    }

    private synchronized static boolean addConnector(JeproLabDataBaseConnector connector){
        if(dataBaseConnectorPool.size() < NUMBER_OF_CONNECTION && !dataBaseConnectorPool.contains(connector)){
            dataBaseConnectorPool.add(connector);
            return true;
        }
        return false;
    }

    public static synchronized void removeConnection(JeproLabDataBaseConnector connector){
        if(dataBaseConnectorPool.contains(connector)){
            connector.closeConnexion();
            dataBaseConnectorPool.remove(connector);
        }
    }


    /**
     * Get Lab employee
     * Returns the global {@link JeproLabEmployeeModel} object, only creating it if does'nt already exist.
     *
     * @return an employee object with no id
     */
    public static JeproLabEmployeeModel getEmployee() {
        return getEmployee(0);
    }

    /**
     * Get Lab employee
     * Returns the global {@link JeproLabEmployeeModel} object, only creating it if does'nt already exist.
     *
     * @param employeeId integer employee id of the employee to be loaded
     * @return JeproLabEmployeeModel object
     */
    public static JeproLabEmployeeModel getEmployee(int employeeId) {
        JeproLabEmployeeModel employee = JeproLabFactory.getSession(null).getEmployee();

        if (employeeId == 0) {
            if (employee == null) {
                employee = new JeproLabEmployeeModel();
            }
        } else if ((employee == null) || employeeId > 0 || (employee.employee_id != employeeId)) {
            employee = new JeproLabEmployeeModel(employeeId);
        }
        return employee;
    }

    /**
     * Get current session object
     * <p>
     * Returns the global {@link JeproLabSession} object, only creating it if doesn't already exist
     *
     * @return JeproLabSession object
     */
    public static JeproLabSession getSession(JeproLabSession.JeproLabSessionOption sessionOptions) {
        if (appSession == null) {
            appSession = JeproLabFactory.createSession(sessionOptions);
        }
        return appSession;
    }

    /**
     * create a session object
     *
     * @param sessionOptions
     * @return JeproLabSession object
     */
    protected static JeproLabSession createSession(JeproLabSession.JeproLabSessionOption sessionOptions) {
        JeproLabConfig config = JeproLabFactory.getConfig();
        String sessionHandler = config.getSessionHandler();

        if (sessionOptions == null) {
            sessionOptions = new JeproLabSession.JeproLabSessionOption();
        }

        //configuration time in minute
        sessionOptions.expire = config.getLifeTime() > 0 ? config.getLifeTime() * 60 : 900;

        appSession = JeproLabSession.getInstance(sessionHandler, sessionOptions);

        if (appSession.getState().equals("expired")) {
            appSession.restart();
        }
        return appSession;
    }

    /**
     * return application configuration object
     *
     * @return
     */
    public static JeproLabConfig getConfig() {
        return getConfig("");
    }

    public static JeproLabConfig getConfig(String filePath) {
        if (appConfig == null) {
            if (filePath == null || filePath.equals("")) {
                filePath = "config.properties";
            }
            JeproLabConfig.setConfig(filePath);
            appConfig = JeproLabConfig.getConfiguration();
        }
        return appConfig;
    }



}
