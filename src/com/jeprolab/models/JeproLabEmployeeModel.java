package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabEmployeeModel extends JeproLabModel {
    public boolean isLogged = false;

    public int employee_id ;

    public int profile_id;

    public int language_id;

    public int laboratory_id;

    public String email;

    public String password;

    public String name;

    public String username;

    public boolean getLabFromContext = false;

    public static boolean multiLangLab = true;

    public JeproLabEmployeeModel(){
        this(0, 0, 0);
    }

    public JeproLabEmployeeModel(int employeeId){
        this(employeeId, 0, 0);
    }

    public JeproLabEmployeeModel(int employeeId, int langId){
        this(employeeId, langId, 0);
    }

    public JeproLabEmployeeModel(int employeeId, int langId, int labId){
        if(langId > 0){
            this.language_id = JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang");
        }

        if(labId > 0){
            this.laboratory_id = labId;
            this.getLabFromContext = false;
        }

        if(employeeId > 0){
            /** loading employee from database given it id **/
            String cacheKey = "jeprolab_employee_model_" + employeeId + "_" + langId + "_" + labId;
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__users") + " AS employee ";
                String where = "";
                if(langId > 0){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_user_lang") + " AS employee_lang ON (employee.id = ";
                    query += " employee_lang.employee_id AND employee_lang.lang_id = " + langId + ") ";
                    if(this.laboratory_id > 0 && !JeproLabEmployeeModel.multiLangLab){
                        where += " AND employee_lang.lab_id = " + this.laboratory_id;
                    }
                }

                /** laboratory association  **/
                if(JeproLabLaboratoryModel.isTableAssociated("employee")){
                    query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_employee_lab") + " AS lab ON (employee.id = shop.employee_id";
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
                    ignored.printStackTrace();
                }finally {
                    try {
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored) {
                        ignored.printStackTrace();
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
        }
    }

    public List<Integer> getAssociatedLaboratories(){
        if (!JeproLabLaboratoryModel.isTableAssociated("employee")) {
            return new ArrayList<>();
        }

        List<Integer> labs = new ArrayList<>();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT lab_id FROM " + staticDataBaseObject.quoteName("#__jeproLab_employee_shop") + " WHERE employee_id = " + this.employee_id;
        dataBaseObject.setQuery(query);
        ResultSet labSet = staticDataBaseObject.loadObjectList();
        if(labSet != null){
            try{
                while(labSet.next()){
                    labs.add(labSet.getInt("lab_id"));
                }
            }catch(SQLException ignored){

            }
        }
        return labs;
    }

    public void save(){

    }

    //todo code me
    public void setCookieLogin(boolean setCookie){

    }

    public boolean canEdit(String table, int objectId){
        return true;
    }

    public boolean isSuperAdmin(){ return true;}


}
