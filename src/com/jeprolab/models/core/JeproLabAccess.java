package com.jeprolab.models.core;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by jeprodev on 06/07/2016.
 */
public class JeproLabAccess {
    public static int root_id;
    public static boolean check(int userId, String action, String assetName){
        //action = action.replaceAll("\#[\s\-]+#]", ".");
        if(assetName != null && !assetName.equals("")) {
            //assetName = assetName.replaceAll("[\s\-]+#]", ".");
        }

        if(assetName == null || assetName.isEmpty()){
            JeproLabDataBaseConnector connector = JeproLabFactory.getDataBaseConnector();
            //String query =
        }
        return true;
    }

    public int getRootId(){
        if(JeproLabAccess.root_id > 0){
            return JeproLabAccess.root_id;
        }

        JeproLabDataBaseConnector connector = JeproLabFactory.getDataBaseConnector();
        String query = "SELECT " + connector.quoteName("id") + " FROM " + connector.quoteName("#__access") + " WHERE " + connector.quoteName("parent_id") + " = 0 ";

        connector.setQuery(query);

        ResultSet assetResultSet = connector.loadObjectList();
        int countAssets = 0;

        if(assetResultSet != null){
            try{
                if(assetResultSet.next()){
                    JeproLabAccess.root_id = assetResultSet.getInt("id");
                    return JeproLabAccess.root_id;
                }

            }catch (SQLException ignored){
                ignored.printStackTrace();
            }finally {
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }

        /**
         * Check for a unique record with lft = 0
         * */
        query = "SELECT " + connector.quoteName("id") + " FROM " + connector.quoteName("#__assets") + " WHERE " + connector.quoteName("lft") + " = 0 ";

        connector.setQuery(query);
        assetResultSet = connector.loadObjectList();
        if(assetResultSet != null){
            countAssets = 0;
            try{
                while(assetResultSet.next()){
                    countAssets += 1;
                    if(countAssets == 1){
                        JeproLabAccess.root_id = assetResultSet.getInt("id");
                    }
                }
            }catch (SQLException ignored){
                ignored.printStackTrace();
            }
        }
        JeproLabAccess.root_id = 0;
        return JeproLabAccess.root_id;
    }

    public static void getAssetRules(String asset, boolean recursive){
        JeproLabDataBaseConnector dataBaseConnector = JeproLabFactory.getDataBaseConnector();
        Pattern pattern = Pattern.compile("([0-9]*)");
        Matcher matcher = pattern.matcher(asset);

        String query = "SELECT " + (recursive ? "asset_rec." : "asset.") + dataBaseConnector.quoteName("rules") + " FROM ";
        query += dataBaseConnector.quoteName("#__assets") + " AS asset ";
        if(recursive ){
            query += " LEFT JOIN " + dataBaseConnector.quoteName("#__assets") + " AS asset_rec ON asset_rec." + dataBaseConnector.quoteName("lft");
            query += " <= asset." + dataBaseConnector.quoteName("lft") + " AND asset_rec." + dataBaseConnector.quoteName("rgt") + " >= asset.";
            query += dataBaseConnector.quoteName("rgt");
        }

        if(matcher.matches()) {
            query += " WHERE asset." + dataBaseConnector.quoteName("id") + " = " + Integer.parseInt(asset);
        }else{
            query += " WHERE asset." +  dataBaseConnector.quoteName("name") + " = " + dataBaseConnector.quote(asset);
        }

        if(recursive) {
            query += " GROUP BY asset_rec." + dataBaseConnector.quoteName("id") + ", asset_rec." + dataBaseConnector.quoteName("rules");
            query += ", asset_rec." + dataBaseConnector.quoteName("left");
        }else{
            query += " GROUP BY asset_rec." + dataBaseConnector.quoteName("id") + ", asset_rec." + dataBaseConnector.quoteName("rules");
            query += ", asset_rec." + dataBaseConnector.quoteName("left");
        }

        dataBaseConnector.setQuery(query);
        ResultSet assetRuleSet = dataBaseConnector.loadObjectList();

        if(assetRuleSet == null){
            JeproLabAccess access = new JeproLabAccess();
            query = "SELECT " + dataBaseConnector.quoteName("rules") + " FROM " + dataBaseConnector.quoteName("#__assets") ;
            query += " WHERE " + dataBaseConnector.quoteName("id") + " = " + access.getRootId();

            dataBaseConnector.quoteName(query);
            dataBaseConnector.loadObjectList();
        }

    }

    /*public static int getRootId(){

    }*/


    public static class JeproLabAccessRules{

    }
}