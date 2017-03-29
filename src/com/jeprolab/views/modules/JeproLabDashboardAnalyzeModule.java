package com.jeprolab.views.modules;

import com.jeprolab.JeproLab;

/**
 *
 * Created by jeprodev on 25/03/2016.
 */
public class JeproLabDashboardAnalyzeModule extends JeproLabDashboardModule {
    public JeproLabDashboardAnalyzeModule(){
        super();
        setTitleAndIcon(JeproLab.getBundle().getString("JEPROLAB_ANALYZES_LABEL"), "resources/images/edit.png");
    }
}
