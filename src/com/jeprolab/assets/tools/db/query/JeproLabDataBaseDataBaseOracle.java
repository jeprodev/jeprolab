package com.jeprolab.assets.tools.db.query;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabDataBaseDataBaseOracle extends JeproLabDataBaseQuery{
    @Override
    protected String processLimit(String query) {
        return null;
    }

    @Override
    protected String processLimit(String query, int limit) {
        return null;
    }

    @Override
    protected String processLimit(String query, int limit, int offset) {
        return null;
    }

    @Override
    public String quote(String field) {
        return null;
    }
}