package com.jeprolab.assets.tools.db.query;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public interface JeproLabDataBaseQueryLimitable {
    /**
     *
     * @param query
     * @param limit
     * @return
     */
    String processLimit(String query, int limit);

    /**
     *
     * @param query
     * @param limit
     * @param offset
     * @return
     */
    String processLimit(String query, int limit, int offset);

    /**
     *
     * @return
     */
    JeproLabDataBaseQuery setLimit();

    /**
     *
     * @param limit
     * @return
     */
    JeproLabDataBaseQuery setLimit(int limit);

    /**
     *
     * @param limit
     * @param offset
     * @return
     */
    JeproLabDataBaseQuery setLimit(int limit, int offset);
}