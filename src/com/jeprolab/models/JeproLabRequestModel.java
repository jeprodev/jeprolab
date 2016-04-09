package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 27/03/2014.
 */
public class JeproLabRequestModel extends JeproLabModel{
    public int request_id;

    public int customer_id;

    public int main_contact_id;

    public int first_contact_id;

    public int second_contact_id;

    public int third_contact_id;

    public int fourth_contact_id;

    public String reference;

    public JeproLabCustomerModel main_customer;

    public Date date_add;

    public Date adte_upd;

    public static Map<Integer, String> sample_matrix = new HashMap<>();

    public JeproLabRequestModel(){

    }

    public JeproLabRequestModel(int requestId){
        if(sample_matrix == null || sample_matrix.isEmpty()) {
            sample_matrix = JeproLabMatrixModel.getMatrices();
        }
    }

    public static class JeproLabSampleModel extends JeproLabModel {
        public int sample_id;

        public int request_id;

        public int matrix_id;

        public List<Integer> analyzes;

        public String reference;

        public String designation;

        public Date removal_date;

    }

    public static class JeproLabMatrixModel extends JeproLabModel{
        public int matrix_id;

        public int language_id;

        public boolean published;

        public Map<String, String> name, description;

        public static Map<Integer,String> getMatrices() {
            Map<Integer,String> matrices = new HashMap<>();
            int langId = JeproLab.request.getRequest().containsKey("lang_id") ? Integer.parseInt(JeproLab.request.getRequest().get("lang_id")) : JeproLabContext.getContext().language.language_id;

            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_matrix") + " AS matrix LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix_lang.";
            query += staticDataBaseObject.quoteName("matrix_id") +  " = matrix." + staticDataBaseObject.quoteName("matrix_id");
            query += " AND matrix_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE matrix.";
            query += staticDataBaseObject.quoteName("published") + " = 1";

            staticDataBaseObject.setQuery(query);
            ResultSet matricesSet = staticDataBaseObject.loadObject();
            if(matricesSet != null){
                try{
                    while(matricesSet.next()){
                        matrices.put(matricesSet.getInt("matrix_id"), matricesSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }
            }

            return matrices;
        }
    }
}