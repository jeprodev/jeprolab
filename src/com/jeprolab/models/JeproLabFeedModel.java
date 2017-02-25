package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeedModel extends JeproLabModel {
    public static class JeproLabFeedBackModel extends JeproLabModel{
        public int feedback_id;

        /*public $enjoy_working_with_us;

        public  $staff_courtesy;

        public $team_abilities;

        public $team_availability;

        public $problem_support;

        public $general_comment;

        public $online_services;

        public $global_quality;

        public $analyze_speed;

        public $submission;

        public $sample_delivery_speed;

        public $service_speed;

        public $recommend_our_services;

        public $reuse_our_services;

        public $help_us_improve_our_service;

        public $how_do_you_learn_about_us;

        public $service_comment_or_suggestion;
*/
        public String customer_name;

        public String customer_phone;

        public String customer_email;

        public String customer_company;

        //public $reports_quality; */

        public static List<JeproLabFeedBackModel> getFeedBacks(){
            return getFeedBacks(null);
        }

        public static List<JeproLabFeedBackModel> getFeedBacks(JeproLabContext context){
            if(dataBaseObject == null){
                dataBaseObject = JeproLabFactory.getDataBaseConnector();
            }

            String query = "SELECT feedback." + dataBaseObject.quoteName("feedback_id") + ", feedback." + dataBaseObject.quoteName("request_service_id");
            query += ", feedback." + dataBaseObject.quoteName("enjoy_working_with_us") + ", feedback." + dataBaseObject.quoteName("staff_courtesy");
            query += ", feedback." + dataBaseObject.quoteName("team_abilities") +  ", feedback." + dataBaseObject.quoteName("problem_support");
            query += ", feedback." + dataBaseObject.quoteName("team_availability") + ", feedback." + dataBaseObject.quoteName("reuse_our_services");
            query += ", feedback." + dataBaseObject.quoteName("recommend_our_services") + ", feedback." + dataBaseObject.quoteName("services_speed");
            query += ", feedback." + dataBaseObject.quoteName("sample_delivery_speed") + ", feedback." + dataBaseObject.quoteName("submission");
            query += ", feedback." + dataBaseObject.quoteName("reports_quality") + ", feedback." + dataBaseObject.quoteName("analyze_speed") + ", feedback.";
            query += dataBaseObject.quoteName("online_services") + ", feedback." + dataBaseObject.quoteName("customer_id") + ", feedback.";
            query += dataBaseObject.quoteName("global_quality") + " , CONCAT(customer." + dataBaseObject.quoteName("firstname")  + ", ' ', customer.";
            query += dataBaseObject.quoteName("lastname") + ") AS customer_name FROM " + dataBaseObject.quoteName("#__jeprolab_feedback") + " AS feedback ";
            query += "LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_customer") + " AS customer ON (customer." + dataBaseObject.quoteName("customer_id");
            query += " = feedback." + dataBaseObject.quoteName("customer_id") + ") ";

            ResultSet feedBackSet = dataBaseObject.loadObjectList(query);

            List<JeproLabFeedBackModel> feedBacks = new ArrayList<>();

            if(feedBackSet != null){
                JeproLabFeedBackModel feedBack;
                try{
                    while(feedBackSet.next()){
                        feedBack = new JeproLabFeedBackModel();
                        feedBack.feedback_id = feedBackSet.getInt("feedback_id");
                        feedBack.customer_name = feedBackSet.getString("customer_name");
                        /*feedBack = feedBackSet.get("");
                        feedBack = feedBackSet.get("");
                        feedBack = feedBackSet.get("");*/
                        feedBacks.add(feedBack);
                    }
                }catch(SQLException ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
                }finally {
                    try {
                        JeproLabFactory.removeConnection(dataBaseObject);
                    } catch (Exception ignored) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                    }
                }
            }
            return feedBacks;
        }

    }
}
