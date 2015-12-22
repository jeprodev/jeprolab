package com.jeprolab.assets.tools.db.query;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabDataBaseDataBaseMysqli extends JeproLabDataBaseQuery implements JeproLabDataBaseQueryLimitable {

    /**
     * Method to modify a query already in string format with the needed additions to make the query limited to a
     * particular number of results, or start at a particular.
     * @param query the request string
     *
     * @return String query object
     */
    public String processLimit(String query){
        return processLimit(query, 0);
    }

    /**
     * Method to modify a query already in string format with the needed additions to make the query limited to a
     * particular number of results, or start at a particular
     *
     * @param query the request string
     * @param limit the limit for the result set
     *
     * @return String query object
     */
    public String processLimit(String query, int limit){
        return processLimit(query, 0, 0);
    }

    /**
     * Method to modify a query already in string format with the needed additions to make the query limited to a
     * particular number of results, or start at a particular.
     *
     * @param query the request string
     * @param limit the limit for the result set
     * @param offset the offset for the result set
     *
     * @return String query object
     */
    public String processLimit(String query, int limit, int offset){
        if(limit > 0 || offset > 0){
            query += " LIMIT " + offset + ", " + limit;
        }
        return query;
    }

    @Override
    public String quote(String field) {
        return null;
    }

    /**
     * Concatenates an array of column names of values
     * @param values An array of value to be concatenate.
     *
     * @return String concatenated values
     */
    public String concatenate(String[] values){
        return concatenate(values, "");
    }

    /**
     * Concatenates an array of column names of values
     * @param values An array of value to be concatenate.
     * @param separator As separator to place between each value.
     *
     * @return String concatenated values
     */
    public String concatenate(String[] values, String separator){
        String concatQuery;
        if( separator == null || !separator.equals("")){
            concatQuery = "CONCAT_WS(" + this.quote(separator);
            for(String value : values){
                concatQuery +=  ", " + value;
            }
            concatQuery += ")";
        }else{
            concatQuery = " CONCAT(" ;
            for(String value : values){
                concatQuery +=  ", " + value;
            }
            concatQuery += ")";
        }
        return concatQuery;
    }

    /**
     * Sets the offset and limit for the result set, if the database driver supports it.
     *
     * Usage : query.setLimit(100, 0);
     *         query.setLimit(100, 0);
     */
    public JeproLabDataBaseQuery setLimit(){
        return this.setLimit(0, 0);
    }

    /**
     * Sets the offset and limit for the result set, if the database driver supports it.
     *
     * Usage : query.setLimit(100);
     *         query.setLimit(50);
     */
    public JeproLabDataBaseQuery setLimit(int limit){
        return this.setLimit(limit, 0);
    }

    /**
     * Sets the offset and limit for the result set, if the database driver supports it.
     *
     * Usage : query.setLimit(100, 0);
     *         query.setLimit(50, 50);
     */
    public JeproLabDataBaseQuery setLimit(int limit, int offset){
        this.queryLimit = limit;
        this.queryOffset = offset;
        return this;
    }

    /**
     * Return correct regexp operator for mysqli. Ensure that the regexp operator is mysqli compatible
     */
    public String regexp(String value){
        return " REGEXP " + value;
    }
}