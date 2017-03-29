package com.jeprolab.views.modules;

import com.jeprolab.JeproLab;

/**
 *
 * Created by jeprodev on 25/03/2016.
 */
public class JeproLabDashboardProjectionModule extends JeproLabDashboardModule {
    public JeproLabDashboardProjectionModule(){
        super();
        setTitleAndIcon(JeproLab.getBundle().getString("JEPROLAB_PROJECTION_LABEL"), "resources/images/edit.png");
    }
}
