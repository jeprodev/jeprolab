package com.jeprolab.assets.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabUpdater {
    public enum Mode {FILE, URL}
    public enum Action {MOVE, DELETE, EXECUTE}

    public static String getLatestVersion() throws Exception{
        //String data = getData(versionUrl);

        return ""; //data.substring(data.indexOf("[version]") + 9, data.indexOf("[/version]"));
    }

    public static void checkForNewVersion(String currentVersion){
        try {
            String lastVersion = getLatestVersion();
            System.out.println(lastVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getData(String address){
        try {
            URL appUrl = new URL(address);

            InputStream html = appUrl.openStream();

            int c = 0;
            StringBuilder builder = new StringBuilder("");

            while (c != -1) {
                c = html.read();
                builder.append((char) c);
            }
            return builder.toString();
        }catch (IOException ex){
            return "";
        }
    }
}
