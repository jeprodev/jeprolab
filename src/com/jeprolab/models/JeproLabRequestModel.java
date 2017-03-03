package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.controllers.JeproLabRequestSampleAddController;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabRequestModel extends JeproLabModel{
    public int request_id;

    public int customer_id;

    public int status_id;

    public int first_contact_id;

    public int second_contact_id;

    public int third_contact_id;

    public int fourth_contact_id;

    public int delay;

    public String reference;

    public Date date_add;

    public Date date_upd;

    public List<Integer> samples = new ArrayList<>();

    public static Map<Integer, String> sample_matrix = new HashMap<>();

    public static final int ROUND_ITEM = 1;
    public static final int ROUND_LINE = 2;
    public static final int ROUND_TOTAL = 3;

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

                //dataBaseObject.setQuery(query);
                ResultSet requestSet = dataBaseObject.loadObjectList(query);

                if(requestSet != null){
                    try{
                        if (requestSet.next()){
                            this.request_id = requestSet.getInt("request_id");
                            this.customer_id = requestSet.getInt("customer_id");
                            this.delay = requestSet.getInt("delay");
                            this.first_contact_id = requestSet.getInt("first_contact_id");
                            this.third_contact_id = requestSet.getInt("second_contact_id");
                            this.first_contact_id = requestSet.getInt("third_contact_id");
                            this.fourth_contact_id = requestSet.getInt("fourth_contact_id");
                            this.reference = requestSet.getString("reference");
                            this.date_add = requestSet.getDate("date_add");
                            this.date_upd = requestSet.getDate("date_upd");
                            this.samples = JeproLabSampleModel.getSampleIdsByRequestId(this.request_id);
                        }
                        JeproLabCache.getInstance().store(cacheKey, this);
                    }catch(SQLException ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                    }finally{
                        try {
                            JeproLabFactory.removeConnection(dataBaseObject);
                        }catch (Exception ignored) {
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                        }
                    }
                }
            }else{
                JeproLabRequestModel requestModel = (JeproLabRequestModel)JeproLabCache.getInstance().retrieve(cacheKey);
                this.request_id = requestModel.request_id;
                this.customer_id = requestModel.customer_id;
                this.delay = requestModel.delay;
                this.first_contact_id = requestModel.first_contact_id;
                this.second_contact_id = requestModel.second_contact_id;
                this.third_contact_id = requestModel.third_contact_id;
                this.fourth_contact_id = requestModel.fourth_contact_id;
                this.reference = requestModel.reference;
                this.samples = requestModel.samples;
                this.date_add = requestModel.date_add;
                this.date_upd = requestModel.date_upd;
            }
        }
    }

    public static List<JeproLabRequestModel> getRequests(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_request");

        //dataBaseObject.setQuery(query);

        ResultSet requestSet = dataBaseObject.loadObjectList(query);
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
                    request.date_add = requestSet.getDate("date_add");
                    requestList.add(request);
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try{
                    JeproLabFactory.removeConnection(dataBaseObject);
                }catch(Exception ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        return requestList;
    }

    public void add(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String addedDate = JeproLabTools.date();
        String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_request") + " ( " + dataBaseObject.quoteName("customer_id") + ", ";
        query += dataBaseObject.quoteName("reference") + ", " + dataBaseObject.quoteName("first_contact_id") + ", " + dataBaseObject.quoteName("second_contact_id");
        query += ", " + dataBaseObject.quoteName("third_contact_id")  + ", " + dataBaseObject.quoteName("fourth_contact_id") + ", " + dataBaseObject.quoteName("status_id");
        query += ", " + dataBaseObject.quoteName("delay") + ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd") + ") VALUES(";
        query += this.customer_id + ", " + dataBaseObject.quote(this.reference) + ", " + this.first_contact_id + ", " + this.second_contact_id + ", " + this.third_contact_id;
        query += ", " + this.fourth_contact_id + ", " + this.status_id + ", " + this.delay + ", " + dataBaseObject.quote(addedDate) + ", " + dataBaseObject.quote(addedDate) + ")";

        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, true);
        this.request_id = dataBaseObject.getGeneratedKey();

        /***
         * Adding samples for this request
         */
        for(Integer sampleId : samples) {
            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_request_sample") + "(" + dataBaseObject.quoteName("request_id") + ", ";
            query += dataBaseObject.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
            //dataBaseObject.setQuery(query);
            dataBaseObject.query(query, false);
        }
    }

    public void update(){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_request") + " SET " + dataBaseObject.quoteName("customer_id") + " = ";
        query += this.customer_id + ", " + dataBaseObject.quoteName("first_contact_id") + " = " + this.first_contact_id + ", ";
        query += dataBaseObject.quoteName("second_contact_id") + " = " + this.second_contact_id + ", " + dataBaseObject.quoteName("third_contact_id");
        query += " = " + this.fourth_contact_id + ", " + dataBaseObject.quoteName("status_id") + " = " + this.status_id + ", ";
        query += dataBaseObject.quoteName("delay") + " = " + this.delay + ", " + dataBaseObject.quoteName("date_upd") + " = ";
        query += dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + " WHERE " + dataBaseObject.quoteName("request_id") + " = " + this.request_id;

        //dataBaseObject.setQuery(query);
        dataBaseObject.query(query, false);

        List<Integer> requestSampleIds = JeproLabSampleModel.getSampleIdsByRequestId(this.request_id);
        for(Integer sampleId : requestSampleIds){
            if(!samples.contains(sampleId)){
                query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_request_sample") + " WHERE " + dataBaseObject.quoteName("request_id");
                query += " = " + this.request_id + " AND " + dataBaseObject.quoteName("sample_id") + " = " + sampleId;

                //dataBaseObject.setQuery(query);
                dataBaseObject.query(query, false);
            }
        }

        for(Integer sampleId : samples) {
            if(!requestSampleIds.contains(sampleId)) {
                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_request_sample") + "(" + dataBaseObject.quoteName("request_id") + ", ";
                query += dataBaseObject.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
                //dataBaseObject.setQuery(query);
                dataBaseObject.query(query, false);
            }
        }
    }

    public static List<JeproLabRequestModel> getRequestsByCustomerId(int customerId) {
        List<JeproLabRequestModel> requestIds = new ArrayList<>();
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        return requestIds;
    }

    public static String getReferenceByRequestId(int requestId) {
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }

        String query = "SELECT " + dataBaseObject.quoteName("reference") + " FROM " + dataBaseObject.quoteName("#__jeprolab_request");
        query += " WHERE " + dataBaseObject.quoteName("request_id") + " = " + requestId;

        //dataBaseObject.setQuery(query);
        return dataBaseObject.loadStringValue(query, "reference");
    }

    public static int getCustomerDiscounts(int customerId, int cartRuleId){
        String cacheKey = "jeprola_request_model_get_customer_discounts_" + customerId + "_" + cartRuleId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            //$result = (int)Db::getInstance()->getValue('
            String query = "SELECT COUNT(*) AS nb FROM " + dataBaseObject.quoteName("#__jeprolab_request") + " AS request LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_request_cart_rule") + " AS request_cart_rule ON (request_cart_rule.";
            query += dataBaseObject.quoteName("request_id") + " = request." + dataBaseObject.quoteName("request_id");
            query += " WHERE request." + dataBaseObject.quoteName("customer_id") + " = " + customerId + " AND request_cart_rule.";
            query += dataBaseObject.quoteName("cart_rule_id") + " = " + cartRuleId ;

            //dataBaseObject.setQuery(query);
            int discounts = (int)dataBaseObject.loadValue(query, "nb");
            JeproLabCache.getInstance().store(cacheKey, discounts);
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }

    public static  class JeproLabRequestStatusModel extends JeproLabModel{
        public int request_status_id;

        public Map<String, String> name = new HashMap<>();

        public static Map<Integer, String> getRequestStatues(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            Map<Integer, String> statues = new HashMap<>();
            String query = "SELECT status_lang." + dataBaseObject.quoteName("name") + ", status_lang." + dataBaseObject.quoteName("request_status_id");
            query += " FROM " + dataBaseObject.quoteName("#__jeprolab_request_status_lang") + " AS status_lang WHERE status_lang." + dataBaseObject.quoteName("lang_id");
            query += " = " + JeproLabContext.getContext().language.language_id;

            //dataBaseObject.setQuery(query);
            ResultSet statusSet = dataBaseObject.loadObjectList(query);

            if(statusSet != null){
                try{
                    while (statusSet.next()){
                        statues.put(statusSet.getInt("request_status_id"), statusSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally{
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch(Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return statues;
        }
    }

    public static class JeproLabSampleModel extends JeproLabModel{
        public int sample_id;

        public int request_id;

        public int matrix_id;

        public List<Integer> analyzes;

        private List<JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm> resultForms = new ArrayList<>();

        public String reference;

        public String lot;

        public int condition_id;

        public String designation;

        public String temperature = "0";

        public String temperature_unit = "celsius";

        public Date removal_date;

        public Date received_date;

        public Date test_date;

        public JeproLabSampleModel(){
            this(0);
        }

        public JeproLabSampleModel(int sampleId){
            analyzes = new ArrayList<>();
            if(sampleId > 0){
                String cacheKey = "jeprolab_sample_model_" + sampleId;
                if(!JeproLabCache.getInstance().isStored(cacheKey)){
                    if(dataBaseObject == null){
                        dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query = "SELECT sample.*, request_sample.*  FROM " + dataBaseObject.quoteName("#__jeprolab_sample") + " AS sample LEFT JOIN ";
                    query += dataBaseObject.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample.";
                    query += dataBaseObject.quoteName("sample_id") + " = request_sample." + dataBaseObject.quoteName("sample_id");
                    query += ")WHERE sample." + dataBaseObject.quoteName("sample_id") + " = " + sampleId;

                    //dataBaseObject.setQuery(query);
                    ResultSet sampleSet = dataBaseObject.loadObjectList(query);
                    if(sampleSet != null){
                        try{
                            if(sampleSet.next()){
                                this.sample_id = sampleSet.getInt("sample_id");
                                this.matrix_id = sampleSet.getInt("matrix_id");
                                this.request_id = sampleSet.getInt("request_id");
                                this.designation = sampleSet.getString("designation");
                                this.reference = sampleSet.getString("reference");
                                this.condition_id = sampleSet.getInt("condition_id");
                                this.lot = sampleSet.getString("lot");
                                this.removal_date = sampleSet.getDate("removal_date");
                                this.received_date = sampleSet.getDate("received_date");
                                this.test_date = sampleSet.getDate("test_date");
                                this.analyzes = JeproLabSampleModel.getSampleAnalyzes(this.sample_id);
                                JeproLabCache.getInstance().store(cacheKey, this);
                            }
                        }catch (SQLException ignored){
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }
                    }
                }else{
                    JeproLabSampleModel sample = (JeproLabSampleModel)JeproLabCache.getInstance().retrieve(cacheKey);
                    this.sample_id = sample.sample_id;
                    this.matrix_id = sample.matrix_id;
                    this.designation = sample.designation;
                    this.reference = sample.reference;
                    this.temperature = sample.temperature;
                    this.temperature_unit = sample.temperature_unit;
                    this.condition_id = sample.condition_id;
                    this.lot = sample.lot;
                    this.received_date = sample.received_date;
                    this.test_date = sample.test_date;
                    this.removal_date = sample.removal_date;
                    this.analyzes = sample.analyzes;
                }
            }
        }

        public static List<JeproLabSampleModel> getSamples(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_sample"); /* + " AS sample WHERE ";
            query += dataBaseObject.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + dataBaseObject.quoteName("sample_id");
            query += " = request_sample." + dataBaseObject.quoteName("sample_id") + " AND request_sample." + dataBaseObject.quoteName("request_id");
            query += " = " + requestId + ") WHERE sample." + dataBaseObject.quoteName("request_id") + " = " + requestId; */
            query += " LIMIT 0, 24";

            //dataBaseObject.setQuery(query);
            ResultSet sampleSet = dataBaseObject.loadObjectList(query);
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
                        sample.temperature = sampleSet.getString("temperature");
                        sample.temperature_unit = sampleSet.getString("temperature_unit");
                        sample.condition_id = sampleSet.getInt("condition_id");
                        sample.removal_date = sampleSet.getDate("removal_date");
                        sample.received_date = sampleSet.getDate("received_date");
                        sample.test_date = sampleSet.getDate("test_date");
                        sample.analyzes = JeproLabSampleModel.getSampleAnalyzes(sample.sample_id);
                        sampleList.add(sample);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally{
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return sampleList;
        }

        public static List<JeproLabSampleModel> getSamplesByRequestId(int requestId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT sample.* FROM " + dataBaseObject.quoteName("#__jeprolab_sample") + " AS sample LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + dataBaseObject.quoteName("sample_id");
            query += " = request_sample." + dataBaseObject.quoteName("sample_id") + " AND request_sample." + dataBaseObject.quoteName("request_id");
            query += " = " + requestId + ") WHERE request_sample." + dataBaseObject.quoteName("request_id") + " = " + requestId;

            //dataBaseObject.setQuery(query);
            ResultSet sampleSet = dataBaseObject.loadObjectList(query);
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
                        sample.removal_date = sampleSet.getDate("removal_date");
                        sample.analyzes = JeproLabSampleModel.getSampleAnalyzes(sample.sample_id);
                        sampleList.add(sample);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally{
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }

            return sampleList;
        }

        public static List<Integer> getSampleAnalyzes(int sampleId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            List<Integer> analyzeList = new ArrayList<>();

            String query = "SELECT " + dataBaseObject.quoteName("analyze_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_sample_analyze");
            query += " WHERE " + dataBaseObject.quoteName("sample_id") + " = " + sampleId;

            //dataBaseObject.setQuery(query);
            ResultSet analyzeSet = dataBaseObject.loadObjectList(query);

            if(analyzeSet != null){
                try{
                    while (analyzeSet.next()){
                        analyzeList.add(analyzeSet.getInt("analyze_id"));
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }
            }
            return analyzeList;
        }

        public void add(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String date = dataBaseObject.quote(JeproLabTools.date("YYYY-MM-DD", this.received_date));
            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_sample") + " ( " + dataBaseObject.quoteName("matrix_id") + ", ";
            query += dataBaseObject.quoteName("designation") + ", " + dataBaseObject.quoteName("reference") + ", " + dataBaseObject.quoteName("lot");
            query += ", " + dataBaseObject.quoteName("temperature") + ", " + dataBaseObject.quoteName("temperature_unit") + ", ";
            query += dataBaseObject.quoteName("condition_id") + ", " + dataBaseObject.quoteName("removal_date") + ", " + dataBaseObject.quoteName("received_date");
            query += ", " + dataBaseObject.quoteName("test_date") + ") VALUES(" + this.matrix_id + ", " + dataBaseObject.quote(this.designation) + ", ";
            query += dataBaseObject.quote(this.reference) + ", " + dataBaseObject.quote(this.lot) + ", " + dataBaseObject.quote(this.temperature) + ", ";
            query += dataBaseObject.quote(this.temperature_unit) + ", " + this.condition_id + ", " + date + ", " + date + ", " + date + ")";
;
            dataBaseObject.query(query, true);
            this.sample_id = dataBaseObject.getGeneratedKey();

            /***
             * Adding requested analyzes
             */
            for(Integer analyzeId : analyzes) {
                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_sample_analyze") + "(" + dataBaseObject.quoteName("sample_id") + ", ";
                query += dataBaseObject.quoteName("analyze_id") + ") VALUES(" + this.sample_id + ", " + analyzeId + ")";

                dataBaseObject.query(query, false);
            }
        }

        public void update(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_sample") + " SET " + dataBaseObject.quoteName("matrix_id") + " = " + this.matrix_id;
            query += ", " + dataBaseObject.quoteName("designation") + " = " + dataBaseObject.quote(this.designation) + ", " + dataBaseObject.quoteName("temperature") +  " = " ;
            query += dataBaseObject.quote(this.temperature) + ", "  + dataBaseObject.quoteName("temperature_unit") + " = " + dataBaseObject.quote(this.temperature_unit);
            query += ", " + dataBaseObject.quoteName("condition") + " = " + this.condition_id;
            query += " WHERE " + dataBaseObject.quoteName("sample_id") + " = " + this.sample_id;

            //dataBaseObject.setQuery(query);
            dataBaseObject.query(query, false);

            for(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm item : resultForms){
                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_sample_analyze") + " SET " + dataBaseObject.quoteName("unit") +  " = ";
                query += dataBaseObject.quote(item.getAnalyzeUnit()) +  ", "  + dataBaseObject.quoteName("method") + " = " + dataBaseObject.quote(item.getAnalyzeMethod());
                query += ", " + dataBaseObject.quoteName("result") + " = " + dataBaseObject.quote(item.getAnalyzeResult()) + ", " + dataBaseObject.quoteName("date_upd");
                query += " = " + dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + " WHERE " + dataBaseObject.quoteName("sample_id") + " = ";
                query += this.sample_id + " AND " + dataBaseObject.quoteName("analyze_id") + " = " + item.getAnalyzeId();

                //dataBaseObject.setQuery(query);
                dataBaseObject.query(query, false);
            }
        }

        public static void updateResult(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm item){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("result_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_sample_analyze_result");
            query += " WHERE " + dataBaseObject.quoteName("analyze_id") + " = " + item.getAnalyzeId() + " AND " + dataBaseObject.quoteName("sample_id");

            int resultId = (int)dataBaseObject.loadValue(query, "result_id");
            if(resultId <= 0) {
                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_sample_analyze_result") + "(" + dataBaseObject.quoteName("analyze_id");
                query += ", " + dataBaseObject.quoteName("sample_id") + ", " + dataBaseObject.quoteName("technician_id") + ", " + dataBaseObject.quoteName("unit");
                query += ", " + dataBaseObject.quoteName("method") + ", " + dataBaseObject.quoteName("value") + ", " + dataBaseObject.quoteName("validator_id");
                query += ", " + dataBaseObject.quoteName("date_add") + ", " + dataBaseObject.quoteName("date_upd") + ") VALUES (" + item.getAnalyzeId() + ", ";
                query += item.getSampleId() + ", " + JeproLabContext.getContext().employee.employee_id + ", " + dataBaseObject.quote(item.getAnalyzeUnit());
                query += ", " + dataBaseObject.quote(item.getAnalyzeMethod()) + ", " + dataBaseObject.quote(item.getAnalyzeResult()) + ", 0, ";
                query += dataBaseObject.quote(JeproLabTools.date()) + ", " + dataBaseObject.quote(JeproLabTools.date()) + ")";
            }else {
                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_sample_analyze_result") + " SET " + dataBaseObject.quoteName("technician_id");
                query += " = " + JeproLabContext.getContext().employee.employee_id + ", " + dataBaseObject.quoteName("method") + " = " ;
                query += dataBaseObject.quote(item.getAnalyzeMethod()) + ", " + dataBaseObject.quoteName("unit") + " = " + dataBaseObject.quote(item.getAnalyzeUnit());
                query += ", " + dataBaseObject.quoteName("value") + " = " + dataBaseObject.quote(item.getAnalyzeResult()) + ", " + dataBaseObject.quoteName("date_upd");
                query += " = " + dataBaseObject.quote(JeproLabTools.date()) + " WHERE " + dataBaseObject.quoteName("result_id") + " = " + resultId + " AND ";
                query += dataBaseObject.quoteName("sample_id") + " = " + item.getSampleId() + " AND " + dataBaseObject.quoteName("analayze_id") ;
                query += " = " + item.getAnalyzeId() ;
            }
            System.out.println(query);
            dataBaseObject.query(query, false);

        }

        public synchronized void removeAnalyze(int analyzeId) {
            int index = 0;
            for (Integer in : analyzes) {
                if (in == analyzeId) {
                    analyzes.remove(index);
                    break;
                }
                index++;
            }
        }


        public void addAnalyze(int analyzeId){
            synchronized(this) {
                analyzes.add(analyzeId);
            }
        }

        public void addResultForm(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm form){
            synchronized (this){
                resultForms.add(form);
            }
        }

        public void removeResultForm(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm form){
            synchronized (this){
                int index = 0;
                for(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm item : resultForms){
                    if(item.getAnalyzeId() == form.getAnalyzeId()){
                        resultForms.remove(index);
                        break;
                    }
                    index++;
                }
            }
        }

        public static List<Integer> getSampleIdsByRequestId(int requestId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("sample_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_request_sample");
            query += " WHERE " + dataBaseObject.quoteName("request_id") + " = " + requestId;

            //dataBaseObject.setQuery(query);
            ResultSet sampleIdSet = dataBaseObject.loadObjectList(query);
            List<Integer> sampleIds = new ArrayList<>();

            if(sampleIdSet != null){
                try{
                    while(sampleIdSet.next()){
                        sampleIds.add(sampleIdSet.getInt("sample_id"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return sampleIds;
        }

        public static Map<String, String> getSampleResult(int sampleId, int analyzeId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT sample.*, analyze_lang." + dataBaseObject.quoteName("name") + " AS name, analyze_method.";
            query += dataBaseObject.quoteName("threshold") + ", analyze_method." + dataBaseObject.quoteName("code");
            query += " AS method FROM " + dataBaseObject.quoteName("#__jeprolab_sample_analyze") + " sample LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (analyze_lang.";
            query += dataBaseObject.quoteName("analyze_id") + " = " + analyzeId + " AND analyze_lang." + dataBaseObject.quoteName("lang_id");
            query += " = " + JeproLabContext.getContext().language.language_id + ") LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_method");
            query += " AS analyze_method ON (analyze_method.method_id = sample.method_id)  WHERE " + dataBaseObject.quoteName("sample_id") + " = " ;
            query += sampleId + " AND sample." + dataBaseObject.quoteName("analyze_id") + " = " + analyzeId;

            //dataBaseObject.setQuery(query);
            ResultSet resultSet = dataBaseObject.loadObjectList(query);

            Map<String, String> result = new HashMap<>();
            if(resultSet != null){
                try{
                    if(resultSet.next()){
                        result.put("name", resultSet.getString("name"));
                        result.put("unit", resultSet.getString("unit"));
                        result.put("threshold", resultSet.getString("threshold"));
                        result.put("result", resultSet.getString("result"));
                        result.put("method", resultSet.getString("method"));
                        result.put("result", resultSet.getString("result"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch(Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return result;
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

        public JeproLabMatrixModel(int matrixId){ this(matrixId, 0); }

        public JeproLabMatrixModel(int matrixId, int langId){
            if(langId > 0){
                this.language_id = (JeproLabLanguageModel.checkLanguage(langId)) ? langId : JeproLabSettingModel.getIntValue("default_lang");
            }

            Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

            if(matrixId > 0){
                String cacheKey = "jeprolab_model_sample_matrix_" + matrixId + "_" + langId;

                if(!JeproLabCache.getInstance().isStored(cacheKey)){
                    if(dataBaseObject == null){
                        dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_matrix") + " AS matrix ";

                    if(langId > 0) {
                        query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix.";
                        query += dataBaseObject.quoteName("matrix_id") + " = matrix_lang." + dataBaseObject.quoteName("matrix_id");
                        query += " AND " + dataBaseObject.quoteName("lang_id") + " = " + langId + ") ";
                    }
                    query += " WHERE matrix." + dataBaseObject.quoteName("matrix_id") + " = " + matrixId;

                    ResultSet matrixSet = dataBaseObject.loadObjectList(query);
                    if(matrixSet != null){
                        try{
                            while(matrixSet.next()){
                                this.matrix_id = matrixSet.getInt("matrix_id");
                                this.name = new HashMap<>();
                                this.description = new HashMap<>();
                                if(langId > 0) {
                                    this.language_id = matrixSet.getInt("lang_id");
                                    this.name.put("lang_" + langId, matrixSet.getString("name"));
                                    this.description.put("lang_" + langId, matrixSet.getString("description"));
                                }else{
                                    query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_matrix_lang") + " WHERE ";
                                    query += dataBaseObject.quoteName("matrix_id") + " = " + matrixId;

                                    ResultSet matrixLangData = dataBaseObject.loadObjectList(query);
                                    while (matrixLangData.next()){
                                        int languageId = matrixLangData.getInt("lang_id");
                                        for(Object o : languages.entrySet()){
                                            JeproLabLanguageModel lang = (JeproLabLanguageModel)(((Map.Entry) o).getValue());
                                            if(lang.language_id == languageId){
                                                this.name.put("lang_" + languageId, matrixSet.getString("name"));
                                                this.description.put("lang_" + languageId, matrixSet.getString("description"));
                                            }

                                        }
                                    }
                                }
                                this.published = matrixSet.getInt("published") > 0;
                            }
                        }catch (SQLException ignored){
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }finally {
                            try {
                                JeproLabFactory.removeConnection(dataBaseObject);
                            }catch(Exception ignored){
                                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                            }
                        }
                    }
                }else {
                    JeproLabMatrixModel matrix = (JeproLabMatrixModel)JeproLabCache.getInstance().retrieve(cacheKey);
                    this.matrix_id = matrix.matrix_id;
                    this.language_id = matrix.language_id;
                    this.name = matrix.name;
                    this.description = matrix.description;
                    this.published = matrix.published;
                }
            }
        }

        public static Map<Integer, String> getMatrices(){
            return getMatrices(JeproLabContext.getContext().language.language_id);
        }

        public static Map<Integer,String> getMatrices(int langId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            Map<Integer,String> matrices = new HashMap<>();

            String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_matrix") + " AS matrix LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix_lang.";
            query += dataBaseObject.quoteName("matrix_id") +  " = matrix." + dataBaseObject.quoteName("matrix_id");
            query += " AND matrix_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE matrix.";
            query += dataBaseObject.quoteName("published") + " = 1";

            //dataBaseObject.setQuery(query);
            ResultSet matricesSet = dataBaseObject.loadObjectList(query);
            if(matricesSet != null){
                try{
                    while(matricesSet.next()){
                        matrices.put(matricesSet.getInt("matrix_id"), matricesSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }

            return matrices;
        }

        public static String getMatrixNameByMatrixId(int matrixId){
            return getMatrixNameByMatrixId(matrixId, JeproLabContext.getContext().language.language_id);
        }

        public static String getMatrixNameByMatrixId(int matrixId, int langId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("name") + " FROM " + dataBaseObject.quoteName("#__jeprolab_matrix_lang");
            query += " WHERE " + dataBaseObject.quoteName("matrix_id") + " = " + matrixId + " AND " + dataBaseObject.quoteName("lang_id");
            query += " = " + langId;

            //dataBaseObject.setQuery(query);
            return dataBaseObject.loadStringValue(query, "name");
        }

    }

    public static class JeproLabSampleReceptionConditionModel extends JeproLabModel{
        public int condition_id;

        public int language_id;

        public boolean published;

        public Map<String, String> name, description;

        public JeproLabSampleReceptionConditionModel(){
            this(0);
        }

        public JeproLabSampleReceptionConditionModel(int conditionId){
            this(conditionId, 0);
        }

        public JeproLabSampleReceptionConditionModel(int conditionId, int langId){
            if(langId > 0){
                this.language_id = (JeproLabLanguageModel.checkLanguage(langId)) ? langId : JeproLabSettingModel.getIntValue("default_lang");
            }

            if(conditionId > 0){
                String cacheKey = "jeprolab_model_sample_condition_" + conditionId + "_" + langId;

                Map<Integer, JeproLabLanguageModel> languages = JeproLabLanguageModel.getLanguages();

                if(!JeproLabCache.getInstance().isStored(cacheKey)){
                    if(dataBaseObject == null){
                        dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    }
                    String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_sample_condition") + " AS condition ";

                    if(langId > 0) {
                        query += " LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_sample_condition_lang") + " AS condition_lang ON (matrix.";
                        query += dataBaseObject.quoteName("sample_condition_id") + " = condition_lang." + dataBaseObject.quoteName("sample_condition_id");
                        query += " AND " + dataBaseObject.quoteName("lang_id") + " = " + langId + ") ";
                    }
                    query += " WHERE condition." + dataBaseObject.quoteName("sample_condition_id") + " = " + conditionId;

                    ResultSet conditionSet = dataBaseObject.loadObjectList(query);
                    if(conditionSet != null){
                        try{
                            while(conditionSet.next()){
                                this.condition_id = conditionSet.getInt("sample_condition_id");
                                this.name = new HashMap<>();
                                this.description = new HashMap<>();
                                if(langId > 0) {
                                    this.language_id = conditionSet.getInt("lang_id");
                                    this.name.put("lang_" + langId, conditionSet.getString("name"));
                                    this.description.put("lang_" + langId, conditionSet.getString("description"));
                                }else{
                                    query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_sample_condition_lang") + " WHERE ";
                                    query += dataBaseObject.quoteName("matrix_id") + " = " + conditionId;

                                    ResultSet matrixLangData = dataBaseObject.loadObjectList(query);
                                    while (matrixLangData.next()){
                                        int languageId = matrixLangData.getInt("lang_id");
                                        for(Object o : languages.entrySet()){
                                            JeproLabLanguageModel lang = (JeproLabLanguageModel)(((Map.Entry) o).getValue());
                                            if(lang.language_id == languageId){
                                                this.name.put("lang_" + languageId, conditionSet.getString("name"));
                                                this.description.put("lang_" + languageId, conditionSet.getString("description"));
                                            }

                                        }
                                    }
                                }
                                this.published = conditionSet.getInt("published") > 0;
                            }
                        }catch (SQLException ignored){
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }finally {
                            try {
                                JeproLabFactory.removeConnection(dataBaseObject);
                            }catch(Exception ignored){
                                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                            }
                        }
                    }
                }else {
                    JeproLabSampleReceptionConditionModel condition = (JeproLabSampleReceptionConditionModel)JeproLabCache.getInstance().retrieve(cacheKey);
                    this.condition_id = condition.condition_id;
                    this.language_id = condition.language_id;
                    this.name = condition.name;
                    this.description = condition.description;
                    this.published = condition.published;
                }
            }
        }

        public static Map<Integer, String> getConditions(int langId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeproLab_sample_condition") + " AS sample_condition LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_sample_condition_lang") + " AS condition_lang ON (condition_lang.";
            query += dataBaseObject.quoteName("sample_condition_id") + " = sample_condition." + dataBaseObject.quoteName("sample_condition_id");
            query += " AND condition_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE sample_condition.";
            query += dataBaseObject.quoteName("published") + " = 1";


            Map<Integer, String> conditions = new HashMap<>();
            ResultSet conditionSet = dataBaseObject.loadObjectList(query);
            if(conditionSet != null){
                try{
                    while(conditionSet.next()){
                        conditions.put(conditionSet.getInt("sample_condition_id"), conditionSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try{
                        JeproLabFactory.removeConnection(dataBaseObject);
                    }catch (Exception ignored){
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }

            return conditions;
        }

        public static String getConditionNameByConditionId(int conditionId){
            return getConditionNameByConditionId(conditionId, JeproLabContext.getContext().language.language_id);
        }

        public static String getConditionNameByConditionId(int conditionId, int langId){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("name") + " FROM " + dataBaseObject.quoteName("#__jeprolab_sample_condition");
            query += " WHERE " + dataBaseObject.quoteName("sample_condition_id") + " = " + conditionId + " AND " + dataBaseObject.quoteName("lang_id");
            query += " = " + langId;

            return dataBaseObject.loadStringValue(query, "name");
        }
    }
}
