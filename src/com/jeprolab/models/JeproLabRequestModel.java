package com.jeprolab.models;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.controllers.JeproLabRequestSampleAddController;
import com.jeprolab.models.core.JeproLabFactory;

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

                dataBaseObject.setQuery(query);
                ResultSet requestSet = dataBaseObject.loadObjectList();

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
                            this.date_add = JeproLabTools.getDate(requestSet.getString("date_add"));
                            this.date_upd = JeproLabTools.getDate(requestSet.getString("date_upd"));
                            this.samples = JeproLabSampleModel.getSampleIdsByRequestId(this.request_id);
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

        dataBaseObject.setQuery(query);

        ResultSet requestSet = dataBaseObject.loadObjectList();
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

        dataBaseObject.setQuery(query);
        dataBaseObject.query(true);
        this.request_id = dataBaseObject.getGeneratedKey();

        /***
         * Adding samples for this request
         */
        for(Integer sampleId : samples) {
            query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_request_sample") + "(" + dataBaseObject.quoteName("request_id") + ", ";
            query += dataBaseObject.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
            dataBaseObject.setQuery(query);
            dataBaseObject.query(false);
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

        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

        List<Integer> requestSampleIds = JeproLabSampleModel.getSampleIdsByRequestId(this.request_id);
        for(Integer sampleId : requestSampleIds){
            if(!samples.contains(sampleId)){
                query = "DELETE FROM " + dataBaseObject.quoteName("#__jeprolab_request_sample") + " WHERE " + dataBaseObject.quoteName("request_id");
                query += " = " + this.request_id + " AND " + dataBaseObject.quoteName("sample_id") + " = " + sampleId;

                dataBaseObject.setQuery(query);
                dataBaseObject.query(false);
            }
        }

        for(Integer sampleId : samples) {
            if(!requestSampleIds.contains(sampleId)) {
                query = "INSERT INTO " + dataBaseObject.quoteName("#__jeproLab_request_sample") + "(" + dataBaseObject.quoteName("request_id") + ", ";
                query += dataBaseObject.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
                dataBaseObject.setQuery(query);
                dataBaseObject.query(false);
            }
        }
    }

    public static List<Integer> getRequestsByCustomerId(int customerId) {
        List<Integer> requestIds = new ArrayList<>();
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

        dataBaseObject.setQuery(query);
        return dataBaseObject.loadStringValue("reference");
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

            dataBaseObject.setQuery(query);
            int discounts = (int)dataBaseObject.loadValue("nb");
            JeproLabCache.getInstance().store(cacheKey, discounts);
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }


    /**
     *
     * Created by jeprodev on 18/06/2014.
     */
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

            dataBaseObject.setQuery(query);
            ResultSet statusSet = dataBaseObject.loadObjectList();

            if(statusSet != null){
                try{
                    while (statusSet.next()){
                        statues.put(statusSet.getInt("request_status_id"), statusSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally{
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch(Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
            return statues;
        }
    }


    /**
     *
     * Created by jeprodev on 18/06/2014.
     */
    public static class JeproLabSampleModel extends JeproLabModel{
        public int sample_id;

        public int request_id;

        public int matrix_id;

        public List<Integer> analyzes;

        private List<JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm> resultForms = new ArrayList<>();

        public String reference;

        public String lot;

        public String condition;

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

                    dataBaseObject.setQuery(query);
                    ResultSet sampleSet = dataBaseObject.loadObjectList();
                    if(sampleSet != null){
                        try{
                            if(sampleSet.next()){
                                this.sample_id = sampleSet.getInt("sample_id");
                                this.matrix_id = sampleSet.getInt("matrix_id");
                                this.request_id = sampleSet.getInt("request_id");
                                this.designation = sampleSet.getString("designation");
                                this.reference = sampleSet.getString("reference");
                                this.condition = sampleSet.getString("condition");
                                this.lot = sampleSet.getString("lot");
                                this.removal_date = JeproLabTools.getDate(sampleSet.getString("removal_date"));
                                this.received_date = JeproLabTools.getDate(sampleSet.getString("received_date"));
                                this.test_date = JeproLabTools.getDate(sampleSet.getString("test_date"));
                                this.analyzes = JeproLabSampleModel.getSampleAnalyzes(this.sample_id);
                                JeproLabCache.getInstance().store(cacheKey, this);
                            }
                        }catch (SQLException ignored){
                            ignored.printStackTrace();
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
                    this.condition = sample.condition;
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

            dataBaseObject.setQuery(query);
            ResultSet sampleSet = dataBaseObject.loadObjectList();
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
                        sample.condition = sampleSet.getString("condition");
                        sample.removal_date = JeproLabTools.getDate(sampleSet.getString("removal_date"));
                        sample.received_date = JeproLabTools.getDate(sampleSet.getString("received_date"));
                        sample.test_date = JeproLabTools.getDate(sampleSet.getString("test_date"));
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT sample.* FROM " + dataBaseObject.quoteName("#__jeprolab_sample") + " AS sample LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + dataBaseObject.quoteName("sample_id");
            query += " = request_sample." + dataBaseObject.quoteName("sample_id") + " AND request_sample." + dataBaseObject.quoteName("request_id");
            query += " = " + requestId + ") WHERE request_sample." + dataBaseObject.quoteName("request_id") + " = " + requestId;

            dataBaseObject.setQuery(query);
            ResultSet sampleSet = dataBaseObject.loadObjectList();
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            List<Integer> analyzeList = new ArrayList<>();

            String query = "SELECT " + dataBaseObject.quoteName("analyze_id") + " FROM " + dataBaseObject.quoteName("#__jeprolab_sample_analyze");
            query += " WHERE " + dataBaseObject.quoteName("sample_id") + " = " + sampleId;

            dataBaseObject.setQuery(query);
            ResultSet analyzeSet = dataBaseObject.loadObjectList();

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
            String query = "INSERT INTO " + dataBaseObject.quoteName("#__jeprolab_sample") + " ( " + dataBaseObject.quoteName("matrix_id") + ", ";
            query += dataBaseObject.quoteName("designation") + ", " + dataBaseObject.quoteName("reference") + ", " + dataBaseObject.quoteName("removal_date");
            query += ") VALUES(" + this.matrix_id + ", " + dataBaseObject.quote(this.designation) + ", " + dataBaseObject.quote(this.reference) + ", ";
            query += dataBaseObject.quote(JeproLabTools.date("YYYY-MM-DD", this.removal_date)) + ")";

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

        public void update(){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }
            String query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_sample") + " SET " + dataBaseObject.quoteName("matrix_id") + " = " + this.matrix_id;
            query += ", " + dataBaseObject.quoteName("designation") + " = " + dataBaseObject.quote(this.designation) + ", " + dataBaseObject.quoteName("temperature") +  " = " ;
            query += dataBaseObject.quote(this.temperature) + ", "  + dataBaseObject.quoteName("temperature_unit") + " = " + dataBaseObject.quote(this.temperature_unit);
            query += ", " + dataBaseObject.quoteName("condition") + " = " + dataBaseObject.quote(this.condition);
            query += " WHERE " + dataBaseObject.quoteName("sample_id") + " = " + this.sample_id;

            dataBaseObject.setQuery(query);
            dataBaseObject.query(false);

            for(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm item : resultForms){
                query = "UPDATE " + dataBaseObject.quoteName("#__jeprolab_sample_analyze") + " SET " + dataBaseObject.quoteName("unit") +  " = ";
                query += dataBaseObject.quote(item.getAnalyzeUnit()) +  ", "  + dataBaseObject.quoteName("method") + " = " + dataBaseObject.quote(item.getAnalyzeMethod());
                query += ", " + dataBaseObject.quoteName("result") + " = " + dataBaseObject.quote(item.getAnalyzeResult()) + ", " + dataBaseObject.quoteName("date_upd");
                query += " = " + dataBaseObject.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + " WHERE " + dataBaseObject.quoteName("sample_id") + " = ";
                query += this.sample_id + " AND " + dataBaseObject.quoteName("analyze_id") + " = " + item.getAnalyzeId();

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

            dataBaseObject.setQuery(query);
            ResultSet sampleIdSet = dataBaseObject.loadObjectList();
            List<Integer> sampleIds = new ArrayList<>();

            if(sampleIdSet != null){
                try{
                    while(sampleIdSet.next()){
                        sampleIds.add(sampleIdSet.getInt("sample_id"));
                    }
                }catch (SQLException ignored){
                    ignored.printStackTrace();
                }finally {
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch (Exception ignored){
                        ignored.printStackTrace();
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

            dataBaseObject.setQuery(query);
            ResultSet resultSet = dataBaseObject.loadObjectList();

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
                    ignored.printStackTrace();
                }finally {
                    try{
                        JeproLabDataBaseConnector.getInstance().closeConnexion();
                    }catch(Exception ignored0){
                        ignored0.printStackTrace();
                    }
                }
            }
            return result;
        }


    }


    /**
     *
     * Created by jeprodev on 18/06/2014.
     */
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

            String query = "SELECT * FROM " + dataBaseObject.quoteName("#__jeprolab_matrix") + " AS matrix LEFT JOIN ";
            query += dataBaseObject.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix_lang.";
            query += dataBaseObject.quoteName("matrix_id") +  " = matrix." + dataBaseObject.quoteName("matrix_id");
            query += " AND matrix_lang." + dataBaseObject.quoteName("lang_id") + " = " + langId + ") WHERE matrix.";
            query += dataBaseObject.quoteName("published") + " = 1";

            dataBaseObject.setQuery(query);
            ResultSet matricesSet = dataBaseObject.loadObjectList();
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
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT " + dataBaseObject.quoteName("name") + " FROM " + dataBaseObject.quoteName("#__jeprolab_matrix_lang");
            query += " WHERE " + dataBaseObject.quoteName("matrix_id") + " = " + matrixId + " AND " + dataBaseObject.quoteName("lang_id");
            query += " = " + langId;

            dataBaseObject.setQuery(query);
            return dataBaseObject.loadStringValue("name");
        }
    }
}
