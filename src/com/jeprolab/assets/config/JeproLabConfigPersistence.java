package com.jeprolab.assets.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabConfigPersistence {
    private static final JeproLabConfigPersistence instance  = new JeproLabConfigPersistence();

    private final XStream xStrem;
    private File configFile;

    private JeproLabConfigPersistence(){
        xStrem = new XStream(new DomDriver());
    }

    public static JeproLabConfigPersistence getInstance(){
        return instance;
    }
}
