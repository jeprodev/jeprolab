package com.jeprolab.models;

import java.util.Map;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabAttributeModel extends JeproLabModel {
    public int attribute_id;
    /** @var int Group id which attribute belongs */
    public int attribute_group_id;

    /** @var string Name */
    public Map<String, String> name;
    public String color;
    public int position;
    public boolean is_default;
}
