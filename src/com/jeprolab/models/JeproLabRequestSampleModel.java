package com.jeprolab.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 27/03/2014.
 */
public class JeproLabRequestSampleModel extends JeproLabModel{
    public int sample_id;

    public int customer_id;

    public int matrix_id;

    public List<Integer> sample_analyzes;

    public String sample_reference;

    public String sample_designation;

    public Date test_date;

    public static Map<Integer, String> sample_matrix = new HashMap<>();
}