package com.jeprolab.assets.tools;

import java.io.InputStream;
import java.net.URL;

/**
 *
 * Created by jeprodev on 02/05/2014.
 */
public class JeproLabUpdater {
    private final static String versionUrl = "localhost/jeprodev/index.php?option=com_jeproshop&view=product&task=view&product_id=51";
    private final static String historyUrl = "localhost/jeprodev/index.php?option=com_jeproshop&view=product&task=view&product_id=51";

    public static String getLatestVesrion() throws Exception{
        String data = getData(versionUrl);
        return data.substring(data.indexOf("[version]") + 9, data.indexOf("[/version]"));
    }

    public static String getWhatsNew() throws Exception{
        String data = getData(historyUrl);
        return data.substring(data.indexOf("[history]") + 9, data.indexOf("[/history]"));
    }

    public static String getData(String address) throws Exception{
        URL appUrl = new URL(address);

        InputStream html = null;
        html = appUrl.openStream();

        int c = 0;
        StringBuilder builder = new StringBuilder("");

        while (c != -1){
            c = html.read();
            builder.append((char)c);
        }
        return builder.toString();
    }
}