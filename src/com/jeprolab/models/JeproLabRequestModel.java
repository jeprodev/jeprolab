package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.controllers.JeproLabRequestAddController;
import com.jeprolab.models.core.JeproLabFactory;
import com.jeprolab.models.core.JeproLabRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabRequestModel extends JeproLabModel{
    public int request_id;

    public int customer_id;

    public int address_id;

    public int first_contact_id;

    public int second_contact_id;

    public int third_contact_id;

    public int fourth_contact_id;

    public String reference;

    public Date date_add;

    public Date date_upd;

    public List<Integer> samples = new ArrayList<>();

    public static Map<Integer, String> sample_matrix = new HashMap<>();

    public JeproLabRequestModel(){
        this(0);
    }

    public JeproLabRequestModel(int requestId){
        if(sample_matrix == null || sample_matrix.isEmpty()) {
            sample_matrix = JeproLabMatrixModel.getMatrices();
        }

        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        if(requestId > 0){
            String cacheKey = "jeprolab_request_model_"  + requestId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_request") + " AS request WHERE request." + dataBaseObject.quoteName("request_id") + " = " + requestId;

                dataBaseObject.setQuery(query);
                ResultSet requestSet = dataBaseObject.loadObjectList();

                if(requestSet != null){
                    try{
                        if (requestSet.next()){
                            this.request_id = requestSet.getInt("request_id");
                            this.customer_id = requestSet.getInt("customer_id");
                            //this.main_contact_id = requestSet.getInt("main_contact_id");
                            this.first_contact_id = requestSet.getInt("first_contact_id");
                            this.third_contact_id = requestSet.getInt("second_contact_id");
                            this.first_contact_id = requestSet.getInt("third_contact_id");
                            this.fourth_contact_id = requestSet.getInt("fourth_contact_id");
                            this.reference = requestSet.getString("reference");
                            this.date_add = JeproLabTools.getDate(requestSet.getString("date_add"));
                            this.date_upd = JeproLabTools.getDate(requestSet.getString("date_upd"));
                        }
                        JeproLabCache.getInstance().store(cacheKey, this);
                    }catch(SQLException ignored){
                        ignored.printStackTrace();
                    }finally{
                        try {
                            JeproLabDataBaseConnector.getInstance().closeConnexion();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                JeproLabRequestModel requestModel = (JeproLabRequestModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.request_id = requestModel.request_id;
                this.customer_id = requestModel.customer_id;
                //this.main_contact_id = requestModel.main_contact_id;
                this.first_contact_id = requestModel.first_contact_id;
                this.second_contact_id = requestModel.second_contact_id;
                this.third_contact_id = requestModel.third_contact_id;
                this.fourth_contact_id = requestModel.fourth_contact_id;
                this.reference = requestModel.reference;
                //this.customer = requestModel.customer;
                this.date_add = requestModel.date_add;
                this.date_upd = requestModel.date_upd;
            }
        }
    }

    public static List<JeproLabRequestModel> getRequests(){
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_request");

        staticDataBaseObject.setQuery(query);

        ResultSet requestSet = staticDataBaseObject.loadObjectList();
        List<JeproLabRequestModel>  requestList = new ArrayList<>();
        if(requestSet != null){
            try{
                JeproLabRequestModel request;
                while(requestSet.next()){
                    request = new JeproLabRequestModel();
                    request.request_id = requestSet.getInt("request_id");
                    request.customer_id = requestSet.getInt("customer_id");
                    request.first_contact_id = requestSet.getInt("first_contact_id");
                    request.second_contact_id = requestSet.getInt("second_contact_id");
                    request.third_contact_id = requestSet.getInt("third_contact_id");
                    request.third_contact_id = requestSet.getInt("third_contact_id");
                    request.reference = requestSet.getString("reference");
                    request.date_add = JeproLabTools.getDate(requestSet.getString("date_add"));
                    requestList.add(request);
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();                }
            }
        }
        return requestList;
    }

    public void update(){

    }

    public void add(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String addedDate = JeproLabTools.date();
        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_request") + " ( " + dataBaseObject.quoteName("customer_id");
        query += ", " + dataBaseObject.quoteName("reference") + ", " + dataBaseObject.quoteName("first_contact_id");
        query += ", " + dataBaseObject.quoteName("second_contact_id") + ", " + dataBaseObject.quoteName("third_contact_id")  + ", " + dataBaseObject.quoteName("fourth_contact_id");
        query += ", " +dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd") + ") VALUES(" + this.customer_id + ", ";
        query += dataBaseObject.quote(this.reference) + ", " + this.first_contact_id + ", " + this.second_contact_id + ", ";
        query += this.third_contact_id + ", " + this.fourth_contact_id + ", " + dataBaseObject.quote(addedDate) + ", " + dataBaseObject.quote(addedDate) + ")";

        dataBaseObject.setQuery(query);
        dataBaseObject.query(true);
        this.request_id = dataBaseObject.getGeneratedKey();

        /***
         * Adding requested analyzes
         */
        for(Integer sampleId : samples) {
            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_request_sample") + "(" + dataBaseObject.quoteName("request_id") + ", ";
            query += dataBaseObject.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
            dataBaseObject.setQuery(query);
            dataBaseObject.query(false);
        }
    }

    public static List<Integer> getRequestsByCustomerId(int customerId) {
        List<Integer> requestIds = new ArrayList<>();
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        return requestIds;
    }



    public static class JeproLabSampleModel extends JeproLabModel{
        public int sample_id;

        //public int request_id;

        public int matrix_id;

        public List<Integer> analyzes;

        public String reference;

        public String designation;

        public Date removal_date;

        public JeproLabSampleModel(){
            this(0);
        }

        public JeproLabSampleModel(int sampleId){
            analyzes = new ArrayList<>();
            if(sampleId > 0){
                if(dataBaseObject == null){
                    dataBaseObject = JeproLabFactory.getDataBaseConnector();
                }
                String cacheKey = "jeprolab_sample_model_" + sampleId;
                if(!JeproLabCache.getInstance().isStored(cacheKey)){
                    String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_sample") + " AS sample WHERE sample." + dataBaseObject.quoteName("sample_id") + " = " + sampleId;
                }

            }
        }

        public static List<JeproLabSampleModel> getSamples(){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_sample"); /* + " AS sample WHERE ";
            query += staticDataBaseObject.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + staticDataBaseObject.quoteName("sample_id");
            query += " = request_sample." + staticDataBaseObject.quoteName("sample_id") + " AND request_sample." + staticDataBaseObject.quoteName("request_id");
            query += " = " + requestId + ") WHERE sample." + staticDataBaseObject.quoteName("sample_id") + " = " + requestId; */

            staticDataBaseObject.setQuery(query);
            ResultSet sampleSet = staticDataBaseObject.loadObjectList();
            List<JeproLabSampleModel> sampleList = new ArrayList<>();
            if(sampleSet != null){
                try{
                    JeproLabSampleModel sample;
                    while(sampleSet.next()){
                        sample = new JeproLabSampleModel();
                        sample.sample_id = sampleSet.getInt("sample_id");
                        sample.matrix_id = sampleSet.getInt("matrix_id");
                        sample.designation = sampleSet.getString("designation");
                        sample.reference = sampleSet.getString("reference");
                        sample.removal_date = JeproLabTools.getDate(sampleSet.getString("removal_date"));
                        sample.analyzes = JeproLabSampleModel.getSampleAnalyzes(sample.sample_id);
                        sampleList.add(sample);
                    }
                }catch(SQLException ignored){
                    ignored.printStackTrace();
                }finally{
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }

            return sampleList;
        }

        public static List<JeproLabSampleModel> getSamplesByRequestId(int requestId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT sample.* FROM " + staticDataBaseObject.quoteName("#__jeprolab_sample") + " AS sample LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + staticDataBaseObject.quoteName("sample_id");
            query += " = request_sample." + staticDataBaseObject.quoteName("sample_id") + " AND request_sample." + staticDataBaseObject.quoteName("request_id");
            query += " = " + requestId + ") WHERE sample." + staticDataBaseObject.quoteName("sample_id") + " = " + requestId;

            staticDataBaseObject.setQuery(query);
            ResultSet sampleSet = staticDataBaseObject.loadObjectList();
            List<JeproLabSampleModel> sampleList = new ArrayList<>();
            if(sampleSet != null){
                try{
                    JeproLabSampleModel sample;
                    while(sampleSet.next()){
                        sample = new JeproLabSampleModel();
                        sample.sample_id = sampleSet.getInt("sample_id");
                        sample.matrix_id = sampleSet.getInt("matrix_id");
                        sample.designation = sampleSet.getString("designation");
                        sample.reference = sampleSet.getString("reference");
                        sample.removal_date = JeproLabTools.getDate(sampleSet.getString("removal_date"));
                        sample.analyzes = JeproLabSampleModel.getSampleAnalyzes(sample.sample_id);
                        sampleList.add(sample);
                    }
                }catch(SQLException ignored){
                    ignored.printStackTrace();
                }finally{
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }

            return sampleList;
        }

        public static List<Integer> getSampleAnalyzes(int sampleId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            List<Integer> analyzeList = new ArrayList<>();

            String query = "SELECT " + staticDataBaseObject.quoteName("analyze_id") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_sample_analyze");
            query += " WHERE " + staticDataBaseObject.quoteName("sample_id") + " = " + sampleId;

            staticDataBaseObject.setQuery(query);
            ResultSet analyzeSet = staticDataBaseObject.loadObjectList();

            if(analyzeSet != null){
                try{
                    while (analyzeSet.next()){
                        analyzeList.add(analyzeSet.getInt("analyze_id"));
                    }
                }catch(SQLException ignored){
                    ignored.printStackTrace();
                }
            }
            return analyzeList;
        }

        public void add(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String removalDate = "2014-06-12";
            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_sample") + " ( " + dataBaseObject.quoteName("matrix_id") + ", ";
            query += dataBaseObject.quoteName("designation") + ", " + dataBaseObject.quoteName("reference") + ", " + dataBaseObject.quoteName("removal_date");
            query += ") VALUES(" + this.matrix_id + ", " + dataBaseObject.quote(this.designation) + ", " + dataBaseObject.quote(this.reference) + ", ";
            query += dataBaseObject.quote(removalDate) + ")";

            dataBaseObject.setQuery(query);
            dataBaseObject.query(true);
            this.sample_id = dataBaseObject.getGeneratedKey();

            /***
             * Adding requested analyzes
             */
            for(Integer analyzeId : analyzes) {
                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_sample_analyze") + "(" + dataBaseObject.quoteName("sample_id") + ", ";
                query += dataBaseObject.quoteName("analyze_id") + ") VALUES(" + this.sample_id + ", " + analyzeId + ")";
                dataBaseObject.setQuery(query);
                dataBaseObject.query(false);
            }
        }

        public void removeAnalyze(int analyzeId){
            synchronized(this) {
                int index = 0;
                for (Integer i : analyzes) {
                    if (i == analyzeId) {
                        analyzes.remove(index);
                        break;
                    }
                    index++;
                }
            }
        }

        public void addAnalyze(int analyzeId){
            synchronized(this) {
                analyzes.add(analyzeId);
            }
        }
    }


    public static class JeproLabMatrixModel  extends JeproLabModel{
        public int matrix_id;

        public int language_id;

        public boolean published;

        public Map<String, String> name, description;

        public JeproLabMatrixModel(){
            this(0);
        }

        public JeproLabMatrixModel(int matrixId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            if(matrixId > 0){

            }
        }

        public static Map<Integer,String> getMatrices() {
            Map<Integer,String> matrices = new HashMap<>();
            int langId = JeproLab.request.getRequest().containsKey("lang_id") ? Integer.parseInt(JeproLab.request.getRequest().get("lang_id")) : JeproLabContext.getContext().language.language_id;

            String query = "SELECT * FROM " + staticDataBaseObject.quoteName("#__jeprolab_matrix") + " AS matrix LEFT JOIN ";
            query += staticDataBaseObject.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix_lang.";
            query += staticDataBaseObject.quoteName("matrix_id") +  " = matrix." + staticDataBaseObject.quoteName("matrix_id");
            query += " AND matrix_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE matrix.";
            query += staticDataBaseObject.quoteName("published") + " = 1";

            staticDataBaseObject.setQuery(query);
            ResultSet matricesSet = staticDataBaseObject.loadObjectList();
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

        public static String getMatrixNameByMatrixId(int matrixId){
            return getMatrixNameByMatrixId(matrixId, JeproLabContext.getContext().language.language_id);
        }

        public static String getMatrixNameByMatrixId(int matrixId, int langId){
            if(staticDataBaseObject == null){
                staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + staticDataBaseObject.quoteName("name") + " FROM " + staticDataBaseObject.quoteName("#__jeprolab_matrix_lang");
            query += " WHERE " + staticDataBaseObject.quoteName("matrix_id") + " = " + matrixId + " AND " + staticDataBaseObject.quoteName("lang_id");
            query += " = " + langId;

            staticDataBaseObject.setQuery(query);
            return staticDataBaseObject.loadStringValue("name");
        }
    }
}
