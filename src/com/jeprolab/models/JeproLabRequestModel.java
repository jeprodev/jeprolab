package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabCache;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
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

    public int carrier_id;

    public float total_paid;

    public String status_name;

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

        if(requestId > 0){
            String cacheKey = "jeprolab_request_model_"  + requestId;
            if(!JeproLabCache.getInstance().isStored(cacheKey)){
                String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request") + " AS request WHERE request." + JeproLabDataBaseConnector.quoteName("request_id") + " = " + requestId;

                JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
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
                        closeDataBaseConnection(dataBaseObject);
                    }
                }else{
                    closeDataBaseConnection(dataBaseObject);
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
        String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request");

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

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
                closeDataBaseConnection(dataBaseObject);
            }
        }else{
            closeDataBaseConnection(dataBaseObject);
        }
        return requestList;
    }

    public static List<JeproLabRequestModel> getLastRequests(){
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("request_id") + ", " + JeproLabDataBaseConnector.quoteName("reference") + " FROM ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_request") + " ORDER BY " + JeproLabDataBaseConnector.quoteName("date_add") + " DESC LIMIT 1, 5";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

        ResultSet requestSet = dataBaseObject.loadObjectList(query);
        List<JeproLabRequestModel> requestList = new ArrayList<>();
        if(requestSet != null){
            try{
                JeproLabRequestModel request;
                while(requestSet.next()){
                    request = new JeproLabRequestModel();
                    request.request_id = requestSet.getInt("request_id");
                    request.reference = requestSet.getString("reference");
                    requestList.add(request);
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }else{
            closeDataBaseConnection(dataBaseObject);
        }
        return requestList;
    }

    public void add(){
        String addedDate = JeproLabTools.date();
        String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request") + " ( " + JeproLabDataBaseConnector.quoteName("customer_id") + ", ";
        query += JeproLabDataBaseConnector.quoteName("reference") + ", " + JeproLabDataBaseConnector.quoteName("first_contact_id") + ", " + JeproLabDataBaseConnector.quoteName("second_contact_id");
        query += ", " + JeproLabDataBaseConnector.quoteName("third_contact_id")  + ", " + JeproLabDataBaseConnector.quoteName("fourth_contact_id") + ", " + JeproLabDataBaseConnector.quoteName("status_id");
        query += ", " + JeproLabDataBaseConnector.quoteName("delay") + ", " + JeproLabDataBaseConnector.quoteName("date_add") + ", " + JeproLabDataBaseConnector.quoteName("date_upd") + ") VALUES(";
        query += this.customer_id + ", " + JeproLabDataBaseConnector.quote(this.reference) + ", " + this.first_contact_id + ", " + this.second_contact_id + ", " + this.third_contact_id;
        query += ", " + this.fourth_contact_id + ", " + this.status_id + ", " + this.delay + ", " + JeproLabDataBaseConnector.quote(addedDate) + ", " + JeproLabDataBaseConnector.quote(addedDate) + ")";

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, true);
        this.request_id = dataBaseObject.getGeneratedKey();

        /***
         * Adding samples for this request
         */
        for(Integer sampleId : samples) {
            query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeproLab_request_sample") + "(" + JeproLabDataBaseConnector.quoteName("request_id") + ", ";
            query += JeproLabDataBaseConnector.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
            //dataBaseObject.setQuery(query);
            dataBaseObject.query(query, false, false, false);
        }
        closeDataBaseConnection(dataBaseObject);
    }

    public void update(){
        String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request") + " SET " + JeproLabDataBaseConnector.quoteName("customer_id") + " = ";
        query += this.customer_id + ", " + JeproLabDataBaseConnector.quoteName("first_contact_id") + " = " + this.first_contact_id + ", ";
        query += JeproLabDataBaseConnector.quoteName("second_contact_id") + " = " + this.second_contact_id + ", " + JeproLabDataBaseConnector.quoteName("third_contact_id");
        query += " = " + this.fourth_contact_id + ", " + JeproLabDataBaseConnector.quoteName("status_id") + " = " + this.status_id + ", ";
        query += JeproLabDataBaseConnector.quoteName("delay") + " = " + this.delay + ", " + JeproLabDataBaseConnector.quoteName("date_upd") + " = ";
        query += JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + " WHERE " + JeproLabDataBaseConnector.quoteName("request_id") + " = " + this.request_id;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        dataBaseObject.query(query, false);

        List<Integer> requestSampleIds = JeproLabSampleModel.getSampleIdsByRequestId(this.request_id);
        for(Integer sampleId : requestSampleIds){
            if(!samples.contains(sampleId)){
                query = "DELETE FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_sample") + " WHERE " + JeproLabDataBaseConnector.quoteName("request_id");
                query += " = " + this.request_id + " AND " + JeproLabDataBaseConnector.quoteName("sample_id") + " = " + sampleId;

                //dataBaseObject.setQuery(query);
                dataBaseObject.query(query, false, false, false);
            }
        }

        for(Integer sampleId : samples) {
            if(!requestSampleIds.contains(sampleId)) {
                query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeproLab_request_sample") + "(" + JeproLabDataBaseConnector.quoteName("request_id") + ", ";
                query += JeproLabDataBaseConnector.quoteName("sample_id") + ") VALUES(" + this.request_id + ", " + sampleId + ")";
                //dataBaseObject.setQuery(query);
                dataBaseObject.query(query, false, false, false);
            }
        }
        closeDataBaseConnection(dataBaseObject);
    }

    public static List<JeproLabRequestModel> getRequestsByCustomerId(int customerId) {
        List<JeproLabRequestModel> requestList = new ArrayList<>();

        int langId = JeproLabContext.getContext().language.language_id;

        String query = "SELECT request." + JeproLabDataBaseConnector.quoteName("request_id") + ", request." + JeproLabDataBaseConnector.quoteName("reference");
        query += ", request." + JeproLabDataBaseConnector.quoteName("total_paid") + ", request." + JeproLabDataBaseConnector.quoteName("date_add") + ", request.";
        query += JeproLabDataBaseConnector.quoteName("carrier_id") + ", request_status_lang." + JeproLabDataBaseConnector.quoteName("name") + " AS status_name FROM ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_request") + " AS request LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status");
        query += " AS request_status ON(request." + JeproLabDataBaseConnector.quoteName("status_id") + " = request_status." + JeproLabDataBaseConnector.quoteName("request_status_id");
        query += ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status_lang") + " AS request_status_lang ON (request_status.";
        query += JeproLabDataBaseConnector.quoteName("request_status_id") + " = request_status_lang." + JeproLabDataBaseConnector.quoteName("request_status_id") ;
        query += " AND request_status_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") WHERE request." + JeproLabDataBaseConnector.quoteName("customer_id");
        query += " = " + customerId;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet requestSet = dataBaseObject.loadObjectList(query);

        if(requestSet != null){
            try{
                JeproLabRequestModel request;
                while(requestSet.next()){
                    request = new JeproLabRequestModel();
                    request.request_id = requestSet.getInt("request_id");
                    request.customer_id = customerId;
                    request.reference = requestSet.getString("reference");
                    request.total_paid = requestSet.getFloat("total_paid");
                    request.carrier_id = requestSet.getInt("carrier_id");
                    request.status_name = requestSet.getString("status_name");
                    //request.reference = requestSet.getString("reference");
                    request.date_add = requestSet.getDate("date_add");
                    requestList.add(request);
                }
            }catch (SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                closeDataBaseConnection(dataBaseObject);
            }
        }else{
            closeDataBaseConnection(dataBaseObject);
        }
        return requestList;
    }

    public static String getReferenceByRequestId(int requestId) {
        String query = "SELECT " + JeproLabDataBaseConnector.quoteName("reference") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request");
        query += " WHERE " + JeproLabDataBaseConnector.quoteName("request_id") + " = " + requestId;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

        String reference = dataBaseObject.loadStringValue(query, "reference");
        closeDataBaseConnection(dataBaseObject);
        return reference;
    }

    public static int getCustomerDiscounts(int customerId, int cartRuleId){
        String cacheKey = "jeprola_request_model_get_customer_discounts_" + customerId + "_" + cartRuleId;
        if (!JeproLabCache.getInstance().isStored(cacheKey)){
            //$result = (int)Db::getInstance()->getValue('
            String query = "SELECT COUNT(*) AS nb FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request") + " AS request LEFT JOIN ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_request_cart_rule") + " AS request_cart_rule ON (request_cart_rule.";
            query += JeproLabDataBaseConnector.quoteName("request_id") + " = request." + JeproLabDataBaseConnector.quoteName("request_id");
            query += " WHERE request." + JeproLabDataBaseConnector.quoteName("customer_id") + " = " + customerId + " AND request_cart_rule.";
            query += JeproLabDataBaseConnector.quoteName("cart_rule_id") + " = " + cartRuleId ;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            int discounts = (int)dataBaseObject.loadValue(query, "nb");
            closeDataBaseConnection(dataBaseObject);
            JeproLabCache.getInstance().store(cacheKey, discounts);
        }
        return (int)JeproLabCache.getInstance().retrieve(cacheKey);
    }


    public static  class JeproLabRequestStatusModel extends JeproLabModel{
        public int request_status_id;

        public int language_id;

        public Map<String, String> name = new HashMap<>();

        public boolean send_invoice;

        public boolean send_email;

        public boolean un_removable;

        public boolean deleted;

        public JeproLabRequestStatusModel(){ this(0); }

        public JeproLabRequestStatusModel(int requestStatusId){
            this(requestStatusId, 0);
        }

        public JeproLabRequestStatusModel(int requestStatusId, int langId){
            if(langId > 0){
                this.language_id = JeproLabLanguageModel.checkLanguage(langId) ? langId : JeproLabSettingModel.getIntValue("default_lang");
            }

            if(requestStatusId > 0){
                String cacheKey = "jeprolab_request_status_model_" + requestStatusId + (langId > 0 ? "_lang_" + langId : 0);

                if(!JeproLabCache.getInstance().isStored(cacheKey)){

                    String query = "SELECT request_status.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status") + " AS request_status ";
                    query += " WHERE request_status." + JeproLabDataBaseConnector.quoteName("request_status_id") + " = " + requestStatusId;

                    JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
                    ResultSet requestStatusSet = dataBaseObject.loadObjectList(query);

                    if(requestStatusSet != null){
                        try{
                            int languageId;
                            if(requestStatusSet.next()){
                                this.request_status_id = requestStatusSet.getInt("request_status_id");
                                this.send_invoice = requestStatusSet.getInt("invoice") > 0;
                                this.send_email = requestStatusSet.getInt("send_mail") > 0;
                                this.un_removable = requestStatusSet.getInt("unremovable") > 0;
                                this.deleted = requestStatusSet.getInt("deleted") > 0;

                                query = "SELECT request_status_lang.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status_lang");
                                query += " AS request_status_lang WHERE request_status_lang." + JeproLabDataBaseConnector.quoteName("request_status_id");
                                query += " = " + requestStatusId + (langId > 0 ? " AND request_status_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId : " ");

                                ResultSet requestStatusLangSet = dataBaseObject.loadObjectList(query);

                                if(requestStatusLangSet != null){
                                    this.name = new HashMap<>();
                                    while(requestStatusLangSet.next()){
                                        languageId = requestStatusLangSet.getInt("lang_id");
                                        this.name.put("lang_" + languageId, requestStatusLangSet.getString("name"));
                                    }
                                }
                            }
                        }catch(SQLException ignored){
                            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                        }finally {
                            closeDataBaseConnection(dataBaseObject);
                        }
                    }else{
                        closeDataBaseConnection(dataBaseObject);
                    }
                }else{
                    JeproLabRequestStatusModel status = (JeproLabRequestStatusModel)JeproLabCache.getInstance().retrieve(cacheKey);
                    this.request_status_id = status.request_status_id;
                    this.send_invoice = status.send_invoice;
                    this.send_email = status.send_email;
                    this.deleted = status.deleted;
                    this.un_removable = status.un_removable;
                    this.name = status.name;
                }
            }

        }

        public static Map<Integer, String> getRequestStatues(){
            Map<Integer, String> statues = new HashMap<>();
            String query = "SELECT status_lang." + JeproLabDataBaseConnector.quoteName("name") + ", status_lang." + JeproLabDataBaseConnector.quoteName("request_status_id");
            query += " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status_lang") + " AS status_lang WHERE status_lang." + JeproLabDataBaseConnector.quoteName("lang_id");
            query += " = " + JeproLabContext.getContext().language.language_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet statusSet = dataBaseObject.loadObjectList(query);

            if(statusSet != null){
                try{
                    while (statusSet.next()){
                        statues.put(statusSet.getInt("request_status_id"), statusSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally{
                    closeDataBaseConnection(dataBaseObject);
                }
            }else{
                closeDataBaseConnection(dataBaseObject);
            }
            return statues;
        }

        public static List<JeproLabRequestStatusModel> getStatues(){
            return getStatues(JeproLabContext.getContext().language.language_id);
        }

        public static List<JeproLabRequestStatusModel> getStatues(int langId){
            String query = "SELECT request_status.*, request_status_lang." + JeproLabDataBaseConnector.quoteName("name") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status") ;
            query += " AS request_status LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_status_lang") + " AS request_status_lang ON (request_status.";
            query += JeproLabDataBaseConnector.quoteName("request_status_id") + " = request_status_lang." + JeproLabDataBaseConnector.quoteName("request_status_id") + " AND request_status_lang.";
            query += JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") ORDER BY request_status." + JeproLabDataBaseConnector.quoteName("request_status_id");

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet statusSet = dataBaseObject.loadObjectList(query);
            List<JeproLabRequestStatusModel> statues = new ArrayList<>();

            if(statusSet != null){
                try{
                    JeproLabRequestStatusModel status;
                    while(statusSet.next()){
                        status = new JeproLabRequestStatusModel();
                        status.request_status_id = statusSet.getInt("request_status_id");
                        status.send_email = statusSet.getInt("send_mail") > 0;
                        status.send_invoice = statusSet.getInt("invoice") > 0;
                        status.un_removable = statusSet.getInt("unremovable") >  0;
                        status.name = new HashMap<>();
                        status.name.put("lang_" + langId, statusSet.getString("name"));
                        status.deleted = statusSet.getInt("deleted") > 0;

                        statues.add(status);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }else{
                closeDataBaseConnection(dataBaseObject);
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
                    String query = "SELECT sample.*, request_sample.*  FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample") + " AS sample LEFT JOIN ";
                    query += JeproLabDataBaseConnector.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample.";
                    query += JeproLabDataBaseConnector.quoteName("sample_id") + " = request_sample." + JeproLabDataBaseConnector.quoteName("sample_id");
                    query += ")WHERE sample." + JeproLabDataBaseConnector.quoteName("sample_id") + " = " + sampleId;

                    JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
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
                        }finally {
                            closeDataBaseConnection(dataBaseObject);
                        }
                    }else{
                        closeDataBaseConnection(dataBaseObject);
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

            String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample"); /* + " AS sample WHERE ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + JeproLabDataBaseConnector.quoteName("sample_id");
            query += " = request_sample." + JeproLabDataBaseConnector.quoteName("sample_id") + " AND request_sample." + JeproLabDataBaseConnector.quoteName("request_id");
            query += " = " + requestId + ") WHERE sample." + JeproLabDataBaseConnector.quoteName("request_id") + " = " + requestId; */
            query += " LIMIT 0, 24";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
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
                    closeDataBaseConnection(dataBaseObject);
                }
            }
            return sampleList;
        }

        public static List<JeproLabSampleModel> getSamplesByRequestId(int requestId){
            String query = "SELECT sample.* FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample") + " AS sample LEFT JOIN ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_request_sample") + " AS request_sample ON (sample." + JeproLabDataBaseConnector.quoteName("sample_id");
            query += " = request_sample." + JeproLabDataBaseConnector.quoteName("sample_id") + " AND request_sample." + JeproLabDataBaseConnector.quoteName("request_id");
            query += " = " + requestId + ") WHERE request_sample." + JeproLabDataBaseConnector.quoteName("request_id") + " = " + requestId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
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
                    closeDataBaseConnection(dataBaseObject);
                }
            }

            return sampleList;
        }

        public static List<Integer> getSampleAnalyzes(int sampleId){
            List<Integer> analyzeList = new ArrayList<>();

            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("analyze_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_analyze");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("sample_id") + " = " + sampleId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet analyzeSet = dataBaseObject.loadObjectList(query);

            if(analyzeSet != null){
                try{
                    while (analyzeSet.next()){
                        analyzeList.add(analyzeSet.getInt("analyze_id"));
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }else {
                closeDataBaseConnection(dataBaseObject);
            }
            return analyzeList;
        }

        public void add(){
            String date = JeproLabDataBaseConnector.quote(JeproLabTools.date("YYYY-MM-DD", this.received_date));
            String query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample") + " ( " + JeproLabDataBaseConnector.quoteName("matrix_id") + ", ";
            query += JeproLabDataBaseConnector.quoteName("designation") + ", " + JeproLabDataBaseConnector.quoteName("reference") + ", " + JeproLabDataBaseConnector.quoteName("lot");
            query += ", " + JeproLabDataBaseConnector.quoteName("temperature") + ", " + JeproLabDataBaseConnector.quoteName("temperature_unit") + ", ";
            query += JeproLabDataBaseConnector.quoteName("condition_id") + ", " + JeproLabDataBaseConnector.quoteName("removal_date") + ", " + JeproLabDataBaseConnector.quoteName("received_date");
            query += ", " + JeproLabDataBaseConnector.quoteName("test_date") + ") VALUES(" + this.matrix_id + ", " + JeproLabDataBaseConnector.quote(this.designation) + ", ";
            query += JeproLabDataBaseConnector.quote(this.reference) + ", " + JeproLabDataBaseConnector.quote(this.lot) + ", " + JeproLabDataBaseConnector.quote(this.temperature) + ", ";
            query += JeproLabDataBaseConnector.quote(this.temperature_unit) + ", " + this.condition_id + ", " + date + ", " + date + ", " + date + ")";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            dataBaseObject.query(query, true, false, false);
            this.sample_id = dataBaseObject.getGeneratedKey();

            /***
             * Adding requested analyzes
             */
            for(Integer analyzeId : analyzes) {
                query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeproLab_sample_analyze") + "(" + JeproLabDataBaseConnector.quoteName("sample_id") + ", ";
                query += JeproLabDataBaseConnector.quoteName("analyze_id") + ") VALUES(" + this.sample_id + ", " + analyzeId + ")";

                dataBaseObject.query(query, false, false, false);
            }
            closeDataBaseConnection(dataBaseObject);
        }

        public void update(){
            String query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample") + " SET " + JeproLabDataBaseConnector.quoteName("matrix_id") + " = " + this.matrix_id;
            query += ", " + JeproLabDataBaseConnector.quoteName("designation") + " = " + JeproLabDataBaseConnector.quote(this.designation) + ", " + JeproLabDataBaseConnector.quoteName("temperature") +  " = " ;
            query += JeproLabDataBaseConnector.quote(this.temperature) + ", "  + JeproLabDataBaseConnector.quoteName("temperature_unit") + " = " + JeproLabDataBaseConnector.quote(this.temperature_unit);
            query += ", " + JeproLabDataBaseConnector.quoteName("condition") + " = " + this.condition_id;
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("sample_id") + " = " + this.sample_id;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            dataBaseObject.query(query, false, false, false);

            for(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm item : resultForms){
                query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_analyze") + " SET " + JeproLabDataBaseConnector.quoteName("unit") +  " = ";
                query += JeproLabDataBaseConnector.quote(item.getAnalyzeUnit()) +  ", "  + JeproLabDataBaseConnector.quoteName("method") + " = " + JeproLabDataBaseConnector.quote(item.getAnalyzeMethod());
                query += ", " + JeproLabDataBaseConnector.quoteName("result") + " = " + JeproLabDataBaseConnector.quote(item.getAnalyzeResult()) + ", " + JeproLabDataBaseConnector.quoteName("date_upd");
                query += " = " + JeproLabDataBaseConnector.quote(JeproLabTools.date("yyyy-MM-dd hh:mm:ss")) + " WHERE " + JeproLabDataBaseConnector.quoteName("sample_id") + " = ";
                query += this.sample_id + " AND " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + item.getAnalyzeId();

                //dataBaseObject.setQuery(query);
                dataBaseObject.query(query, false, false, false);
            }
            closeDataBaseConnection(dataBaseObject);
        }

        public static void updateResult(JeproLabRequestSampleAddController.JeproLabSampleAnalyzeForm item){
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("result_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_analyze_result");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + item.getAnalyzeId() + " AND " + JeproLabDataBaseConnector.quoteName("sample_id");

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            int resultId = (int)dataBaseObject.loadValue(query, "result_id");
            if(resultId <= 0) {
                query = "INSERT INTO " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_analyze_result") + "(" + JeproLabDataBaseConnector.quoteName("analyze_id");
                query += ", " + JeproLabDataBaseConnector.quoteName("sample_id") + ", " + JeproLabDataBaseConnector.quoteName("technician_id") + ", " + JeproLabDataBaseConnector.quoteName("unit");
                query += ", " + JeproLabDataBaseConnector.quoteName("method") + ", " + JeproLabDataBaseConnector.quoteName("value") + ", " + JeproLabDataBaseConnector.quoteName("validator_id");
                query += ", " + JeproLabDataBaseConnector.quoteName("date_add") + ", " + JeproLabDataBaseConnector.quoteName("date_upd") + ") VALUES (" + item.getAnalyzeId() + ", ";
                query += item.getSampleId() + ", " + JeproLabContext.getContext().employee.employee_id + ", " + JeproLabDataBaseConnector.quote(item.getAnalyzeUnit());
                query += ", " + JeproLabDataBaseConnector.quote(item.getAnalyzeMethod()) + ", " + JeproLabDataBaseConnector.quote(item.getAnalyzeResult()) + ", 0, ";
                query += JeproLabDataBaseConnector.quote(JeproLabTools.date()) + ", " + JeproLabDataBaseConnector.quote(JeproLabTools.date()) + ")";
            }else {
                query = "UPDATE " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_analyze_result") + " SET " + JeproLabDataBaseConnector.quoteName("technician_id");
                query += " = " + JeproLabContext.getContext().employee.employee_id + ", " + JeproLabDataBaseConnector.quoteName("method") + " = " ;
                query += JeproLabDataBaseConnector.quote(item.getAnalyzeMethod()) + ", " + JeproLabDataBaseConnector.quoteName("unit") + " = " + JeproLabDataBaseConnector.quote(item.getAnalyzeUnit());
                query += ", " + JeproLabDataBaseConnector.quoteName("value") + " = " + JeproLabDataBaseConnector.quote(item.getAnalyzeResult()) + ", " + JeproLabDataBaseConnector.quoteName("date_upd");
                query += " = " + JeproLabDataBaseConnector.quote(JeproLabTools.date()) + " WHERE " + JeproLabDataBaseConnector.quoteName("result_id") + " = " + resultId + " AND ";
                query += JeproLabDataBaseConnector.quoteName("sample_id") + " = " + item.getSampleId() + " AND " + JeproLabDataBaseConnector.quoteName("analayze_id") ;
                query += " = " + item.getAnalyzeId() ;
            }
            dataBaseObject.query(query, false);
            closeDataBaseConnection(dataBaseObject);
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
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("sample_id") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_request_sample");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("request_id") + " = " + requestId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

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
                    closeDataBaseConnection(dataBaseObject);
                }
            }else{
                closeDataBaseConnection(dataBaseObject);
            }
            return sampleIds;
        }

        public static Map<String, String> getSampleResult(int sampleId, int analyzeId){
            String query = "SELECT sample.*, analyze_lang." + JeproLabDataBaseConnector.quoteName("name") + " AS name, analyze_method.";
            query += JeproLabDataBaseConnector.quoteName("threshold") + ", analyze_method." + JeproLabDataBaseConnector.quoteName("code");
            query += " AS method FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_analyze") + " sample LEFT JOIN ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_analyze_lang") + " AS analyze_lang ON (analyze_lang.";
            query += JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId + " AND analyze_lang." + JeproLabDataBaseConnector.quoteName("lang_id");
            query += " = " + JeproLabContext.getContext().language.language_id + ") LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_method");
            query += " AS analyze_method ON (analyze_method.method_id = sample.method_id)  WHERE " + JeproLabDataBaseConnector.quoteName("sample_id") + " = " ;
            query += sampleId + " AND sample." + JeproLabDataBaseConnector.quoteName("analyze_id") + " = " + analyzeId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

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
                    closeDataBaseConnection(dataBaseObject);
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
                    String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_matrix") + " AS matrix ";

                    if(langId > 0) {
                        query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix.";
                        query += JeproLabDataBaseConnector.quoteName("matrix_id") + " = matrix_lang." + JeproLabDataBaseConnector.quoteName("matrix_id");
                        query += " AND " + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") ";
                    }
                    query += " WHERE matrix." + JeproLabDataBaseConnector.quoteName("matrix_id") + " = " + matrixId;

                    JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

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
                                    query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_matrix_lang") + " WHERE ";
                                    query += JeproLabDataBaseConnector.quoteName("matrix_id") + " = " + matrixId;

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
                            closeDataBaseConnection(dataBaseObject);
                        }
                    }else{
                        closeDataBaseConnection(dataBaseObject);
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
            Map<Integer,String> matrices = new HashMap<>();

            String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_matrix") + " AS matrix LEFT JOIN ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_matrix_lang") + " AS matrix_lang ON (matrix_lang.";
            query += JeproLabDataBaseConnector.quoteName("matrix_id") +  " = matrix." + JeproLabDataBaseConnector.quoteName("matrix_id");
            query += " AND matrix_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") WHERE matrix.";
            query += JeproLabDataBaseConnector.quoteName("published") + " = 1";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            ResultSet matricesSet = dataBaseObject.loadObjectList(query);
            if(matricesSet != null){
                try{
                    while(matricesSet.next()){
                        matrices.put(matricesSet.getInt("matrix_id"), matricesSet.getString("name"));
                    }
                }catch (SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    closeDataBaseConnection(dataBaseObject);
                }
            }

            return matrices;
        }

        public static String getMatrixNameByMatrixId(int matrixId){
            return getMatrixNameByMatrixId(matrixId, JeproLabContext.getContext().language.language_id);
        }

        public static String getMatrixNameByMatrixId(int matrixId, int langId){
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("name") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_matrix_lang");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("matrix_id") + " = " + matrixId + " AND " + JeproLabDataBaseConnector.quoteName("lang_id");
            query += " = " + langId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            String name =  dataBaseObject.loadStringValue(query, "name");
            closeDataBaseConnection(dataBaseObject);
            return name;
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
                    String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_condition") + " AS condition ";

                    if(langId > 0) {
                        query += " LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_condition_lang") + " AS condition_lang ON (matrix.";
                        query += JeproLabDataBaseConnector.quoteName("sample_condition_id") + " = condition_lang." + JeproLabDataBaseConnector.quoteName("sample_condition_id");
                        query += " AND " + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") ";
                    }
                    query += " WHERE condition." + JeproLabDataBaseConnector.quoteName("sample_condition_id") + " = " + conditionId;

                    JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

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
                                    query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_condition_lang") + " WHERE ";
                                    query += JeproLabDataBaseConnector.quoteName("matrix_id") + " = " + conditionId;

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
                            closeDataBaseConnection(dataBaseObject);
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
            String query = "SELECT * FROM " + JeproLabDataBaseConnector.quoteName("#__jeproLab_sample_condition") + " AS sample_condition LEFT JOIN ";
            query += JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_condition_lang") + " AS condition_lang ON (condition_lang.";
            query += JeproLabDataBaseConnector.quoteName("sample_condition_id") + " = sample_condition." + JeproLabDataBaseConnector.quoteName("sample_condition_id");
            query += " AND condition_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + ") WHERE sample_condition.";
            query += JeproLabDataBaseConnector.quoteName("published") + " = 1";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

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
                    closeDataBaseConnection(dataBaseObject);
                }
            }

            return conditions;
        }

        public static String getConditionNameByConditionId(int conditionId){
            return getConditionNameByConditionId(conditionId, JeproLabContext.getContext().language.language_id);
        }

        public static String getConditionNameByConditionId(int conditionId, int langId){
            String query = "SELECT " + JeproLabDataBaseConnector.quoteName("name") + " FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_sample_condition");
            query += " WHERE " + JeproLabDataBaseConnector.quoteName("sample_condition_id") + " = " + conditionId + " AND " + JeproLabDataBaseConnector.quoteName("lang_id");
            query += " = " + langId;

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();

            String name = dataBaseObject.loadStringValue(query, "name");
            closeDataBaseConnection(dataBaseObject);
            return name;
        }
    }
}
