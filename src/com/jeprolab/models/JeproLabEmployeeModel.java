package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.helpers.JeproLabEmployeeHelper;
import com.jeprolab.models.core.JeproLabAccess;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabEmployeeModel extends JeproLabModel{
    public int employee_id ;

    public int profile_id;

    public int language_id;

    public int laboratory_id;

    public String email;

    public String password;

    public String password_clear;

    public String name;

    public String username;

    public boolean is_logged = false;

    protected boolean is_root = false;

    public boolean is_blocked = false;

    public boolean send_email = false;

    public String activation;

    public Date last_reset_time;

    public boolean require_reset;

    public boolean guest;

    public Date date_add, date_up, last_visit;

    protected JeproLabEmployeeHelper.JeproLabEmployeeWrapperHelper employee_helper;

    public JeproLabEmployeeModel(){
        this(0, 0, 0, null);
    }

    public JeproLabEmployeeModel(int employeeId){
        this(employeeId, 0, 0);
    }

    public JeproLabEmployeeModel(int employeeId, int langId){
        this(employeeId, langId, 0, null);
    }

    public JeproLabEmployeeModel(int employeeId, int langId, int labId){
        this(employeeId, langId, 0, null);
    }

    public JeproLabEmployeeModel(int employeeId, int langId, int labId, JeproLabEmployeeHelper.JeproLabEmployeeWrapperHelper employeeWrapperHelper){
        if(employeeWrapperHelper == null){
            employee_helper = new JeproLabEmployeeHelper.JeproLabEmployeeWrapperHelper();
        }
        this.employee_helper = employeeWrapperHelper;

        if(langId > 0){
            this.language_id = JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if(labId > 0){
            this.laboratory_id = labId;
            this.get_lab_from_context = false;
        }

        if(employeeId > 0){
            this.loadEmployee(employeeId);
        }else{
            this.employee_id = 0;
            this.send_email = false;
            this.guest = true;
        }
    }

    public void loadEmployee(int employeeId){
        /** loading employee from database given it id **/
        String cacheKey = "jeprolab_employee_model_" + employeeId + "_" + this.language_id + "_" + this.laboratory_id;
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(!JeproLabCache.getInstance().isStored(cacheKey)){
            String query = "SELECT employee.* FROM " + dataBaseObject.quoteName("#__users") + " AS employee ";
            String where = "";
            if(this.language_id > 0){
                query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_employee_lang") + " AS employee_lang ON (employee.id = ";
                query += " employee_lang.employee_id AND employee_lang.lang_id = " + this.language_id + ") ";
                if(this.laboratory_id > 0){
                    where += " AND employee_lang.lab_id = " + this.laboratory_id;
                }
            }

            /** laboratory association  **/
            if(JeproLabLaboratoryModel.isTableAssociated("employee")){
                query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_employee_lab") + " AS lab ON (employee.id = lab.employee_id";
                query += " AND lab.lab_id = " + this.laboratory_id + ") ";
            }
            query += " WHERE employee.id = " + employeeId + where;
            dataBaseObject.setQuery(query);
            ResultSet result = dataBaseObject.loadObjectList();
            try{
                while(result.next()){
                    this.employee_id = result.getInt("id");
                    this.name = result.getString("name");
                    this.username = result.getString("username");
                    this.password = result.getString("password");
                    this.email = result.getString("email");
                    JeproLabCache.getInstance().store(cacheKey, this);
                }
            }catch (SQLException ignored){
                JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored) {
                    JeproLabTools.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }else{
            JeproLabEmployeeModel employee = (JeproLabEmployeeModel) JeproLabCache.getInstance().retrieve(cacheKey);
            this.employee_id = employee.employee_id;
            //this = employee;
            this.name = employee.name;
            this.password = employee.password;
            this.username = employee.username;
            this.email = employee.email;
        }

        this.guest = this.employee_id == 0;
    }

    public static Map<String, String> getEmployeeIdAndPasswordByUsername(String userName){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("id") + ", " + dataBaseObject.quoteName("password") + " FROM ";
        query += dataBaseObject.quoteName("#__users") + " WHERE " + dataBaseObject.quoteName("username") + " = ";
        query += dataBaseObject.quote(userName);

        dataBaseObject.setQuery(query);
        ResultSet credentialSet = dataBaseObject.loadObjectList();
        Map<String, String> credential = new HashMap<>();
        if(credentialSet != null){
            try{
                if(credentialSet.next()){
                    credential.put("id", credentialSet.getString("id"));
                    credential.put("password", credentialSet.getString("password"));
                }
            }catch(SQLException ignored){
                JeproLabTools.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    JeproLabTools.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return credential;
    }

    public static int getEmployeeIdByUsername(String userName){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("id") + " FROM " + dataBaseObject.quoteName("#__users");
        query += " WHERE " + dataBaseObject.quoteName("username") + " = " + dataBaseObject.quote(userName);

        dataBaseObject.setQuery(query, 0, 1);

        int id = (int)dataBaseObject.loadValue("id");
        return (id > 0) ? id : 0;
    }

    public boolean authorize(String action){
        return authorize(action, null);
    }

    public boolean authorize(String action, String assetName){
        int rootId;
        if(!this.is_root){
            rootId = JeproLabEmployeeModel.getEmployeeIdByUsername(JeproLabSettingModel.getStringValue("root_user_name"));
            if(this.employee_id > 0 && this.employee_id == rootId){
                this.is_root = true;
            }else{
                /*/Get all administrator groups against witch the user is mapped
                List identities = this.getAuthorisedGroups();
                if(JeproLabAccess.getAssetRules(true).allow("core.admin", identities)) {
                    this.is_root = true;
                    return true;
                }*/
            }
        }
        return this.is_root || JeproLabAccess.check(this.employee_id, action, assetName);
    }

    public boolean canEdit(String table, int objectId){
        return true;
    }

    public boolean isSuperAdmin(){ return true;}

    //todo code me
    public void setCookieLogin(boolean setCookie){

    }


}
