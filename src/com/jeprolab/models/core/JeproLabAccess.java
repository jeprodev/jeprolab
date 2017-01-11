package com.jeprolab.models.core;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

/**
 *
 * Created by jeprodev on 09/01/2016.
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
}
