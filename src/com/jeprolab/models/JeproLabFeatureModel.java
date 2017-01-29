package com.jeprolab.models;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabFeatureModel extends JeproLabModel {

    public static class JeproLabFeatureValueModel extends  JeproLabModel{
        public int feature_value_id;
        /** @var int Group id which attribute belongs */
        public int feature_id;

        public int analyze_id;

        /** @var string Name */
        public String value;

        /** @var bool Custom */
        public boolean custom = false;

        public static boolean isFeaturePublished(){
            return JeproLabSettingModel.getIntValue("feature_feature_active") > 0;
        }
    }
}
