package com.jeprolab.models;


import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

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
public class JeproLabFeedModel extends JeproLabModel {
    public int feed_id;

    public int language_id;

    public Map<String, String> feed_title;

    public Map<String, String> feed_link;

    public Map<String, String> feed_description;

    public String feed_author;

    public static List<JeproLabFeedModel> getFeeds(){
        return getFeeds(JeproLabContext.getContext().language.language_id);
    }

    public static List<JeproLabFeedModel> getFeeds(int langId){
        return getFeeds(langId, "feed_id");
    }

    public static List<JeproLabFeedModel> getFeeds(int langId, String orderBy){
        return getFeeds(langId, orderBy, "ASC");
    }


    public static List<JeproLabFeedModel> getFeeds(int langId, String orderBy, String orderWay){
        
        //orderBy = orderBy.replace("`", "");

        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_feeds") + " AS feed LEFT JOIN ";
        query += JeproLabDataBaseConnector.quoteName("#__jeprolab_feeds_lang") + " AS feed_lang ON(feed." + JeproLabDataBaseConnector.quoteName("feed_id");
        query += " = feed_lang." + JeproLabDataBaseConnector.quoteName("feed_id") + " AND feed_lang." + JeproLabDataBaseConnector.quoteName("lang_id");
        query += " = "  + langId + ") WHERE feed_lang." + JeproLabDataBaseConnector.quoteName("lang_id") + " = " + langId + " ORDER BY ";
        query += (orderBy.replace("`", "").equals("feed_id") ? "feed." : "") + orderBy + " " + orderWay ;

        JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
        ResultSet feedSet = dataBaseObject.loadObjectList(query);

        List<JeproLabFeedModel> feeds = new ArrayList<>();

        if(feedSet != null){
            try {
                JeproLabFeedModel feed;
                while (feedSet.next()){
                    feed = new JeproLabFeedModel();
                    feed.feed_id = feedSet.getInt("feed_id");
                    feed.feed_author = feedSet.getString("feed_author");
                    feed.feed_link = new HashMap<>();
                    feed.feed_link.put("lang_" + langId, feedSet.getString("feed_link"));
                    feed.feed_title = new HashMap<>();
                    feed.feed_title.put("lang_" + langId, feedSet.getString("feed_title"));
                    feed.feed_description = new HashMap<>();
                    feed.feed_description.put("lang_" + langId, feedSet.getString("feed_description"));
                    feeds.add(feed);
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
        return feeds;
    }

    public static class JeproLabFeedBackModel extends JeproLabModel{
        public int feedback_id;

        public int customer_id;

        public String enjoy_working_with_us;

        public  String staff_courtesy;

        public String team_abilities;

        public String team_availability;

        public String problem_support;

        public String general_comment;

        public String online_services;

        public String global_quality;

        public String analyze_speed;

        public String submission;

        public String sample_delivery_speed;

        public String service_speed;

        public boolean recommend_our_services;

        public boolean reuse_our_services;

        public String help_us_improve_our_service;

        public String how_do_you_learn_about_us;

        public String service_comment_or_suggestion;

        public String customer_name;

        public String customer_phone;

        public String customer_email;

        public String customer_company;

        public String reports_quality;

        public static List<JeproLabFeedBackModel> getFeedBacks(){
            return getFeedBacks(null);
        }

        public static List<JeproLabFeedBackModel> getFeedBacks(JeproLabContext context){
            String query = "SELECT feedback." + JeproLabDataBaseConnector.quoteName("feedback_id") + ", feedback." + JeproLabDataBaseConnector.quoteName("request_service_id");
            query += ", feedback." + JeproLabDataBaseConnector.quoteName("enjoy_working_with_us") + ", feedback." + JeproLabDataBaseConnector.quoteName("staff_courtesy");
            query += ", feedback." + JeproLabDataBaseConnector.quoteName("team_abilities") +  ", feedback." + JeproLabDataBaseConnector.quoteName("problem_support");
            query += ", feedback." + JeproLabDataBaseConnector.quoteName("team_availability") + ", feedback." + JeproLabDataBaseConnector.quoteName("reuse_our_services");
            query += ", feedback." + JeproLabDataBaseConnector.quoteName("recommend_our_services") + ", feedback." + JeproLabDataBaseConnector.quoteName("services_speed");
            query += ", feedback." + JeproLabDataBaseConnector.quoteName("sample_delivery_speed") + ", feedback." + JeproLabDataBaseConnector.quoteName("submission");
            query += ", feedback." + JeproLabDataBaseConnector.quoteName("reports_quality") + ", feedback." + JeproLabDataBaseConnector.quoteName("analyze_speed") + ", feedback.";
            query += JeproLabDataBaseConnector.quoteName("online_services") + ", feedback." + JeproLabDataBaseConnector.quoteName("customer_id") + ", feedback.";
            query += JeproLabDataBaseConnector.quoteName("global_quality") + " , CONCAT(customer." + JeproLabDataBaseConnector.quoteName("firstname")  + ", ' ', customer.";
            query += JeproLabDataBaseConnector.quoteName("lastname") + ") AS customer_name FROM " + JeproLabDataBaseConnector.quoteName("#__jeprolab_feedback") + " AS feedback ";
            query += "LEFT JOIN " + JeproLabDataBaseConnector.quoteName("#__jeprolab_customer") + " AS customer ON (customer." + JeproLabDataBaseConnector.quoteName("customer_id");
            query += " = feedback." + JeproLabDataBaseConnector.quoteName("customer_id") + ") ";

            JeproLabDataBaseConnector dataBaseObject = JeproLabFactory.getDataBaseConnector();
            ResultSet feedBackSet = dataBaseObject.loadObjectList(query);

            List<JeproLabFeedBackModel> feedBacks = new ArrayList<>();

            if(feedBackSet != null){
                JeproLabFeedBackModel feedBack;
                try{
                    while(feedBackSet.next()){
                        feedBack = new JeproLabFeedBackModel();
                        feedBack.feedback_id = feedBackSet.getInt("feedback_id");
                        feedBack.customer_name = feedBackSet.getString("customer_name");
                        feedBack.enjoy_working_with_us = feedBackSet.getString("enjoy_working_with_us");
                        feedBack.staff_courtesy = feedBackSet.getString("staff_courtesy");
                        feedBack.team_abilities = feedBackSet.getString("team_abilities");
                        feedBack.team_availability = feedBackSet.getString("team_availability");
                        feedBack.problem_support = feedBackSet.getString("problem_support");
                        feedBack.reuse_our_services = feedBackSet.getInt("reuse_our_services") > 0;
                        feedBack.recommend_our_services = feedBackSet.getInt("recommend_our_services") > 0;
                        feedBack.sample_delivery_speed = feedBackSet.getString("sample_delivery_speed");
                        feedBack.submission = feedBackSet.getString("submission");
                        feedBack.reports_quality = feedBackSet.getString("reports_quality");
                        feedBack.analyze_speed = feedBackSet.getString("analyze_speed");
                        feedBack.online_services = feedBackSet.getString("online_services");
                        feedBack.customer_id = feedBackSet.getInt("customer_id");
                        //feedBack = feedBackSet.get("");
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
