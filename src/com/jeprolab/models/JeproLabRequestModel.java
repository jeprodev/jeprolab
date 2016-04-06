package com.jeprolab.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 27/03/2014.
 */
public class JeproLabRequestModel extends JeproLabModel{
    public int request_id;

    public int customer_id;

    public int main_contact_id;

    public String reference;

    public static class JeproLabSampleModel extends JeproLabModel {
        public int sample_id;

        public int request_id;

        public int matrix_id;

        public List<Integer> analyzes;

        public String reference;

        public String designation;

        public Date removal_date;

        public static Map<Integer, String> sample_matrix = new HashMap<>();
    }

    public static class JeproLabMatrixModel extends JeproLabModel{

    }
}