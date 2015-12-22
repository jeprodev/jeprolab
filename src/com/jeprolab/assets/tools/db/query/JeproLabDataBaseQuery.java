package com.jeprolab.assets.tools.db.query;


import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public abstract class JeproLabDataBaseQuery {
    /**
     * JeproLabDataBaseConnector
     */
    protected JeproLabDataBaseConnector dbc = null;

    /** String query **/
    protected String sqlQuery = null;

    /**
     * JeproLabDataBaseQueryElement
     */
    protected JeproLabDataBaseQueryElement element = null;
    protected JeproLabDataBaseQueryElement selectQuery = null;
    protected JeproLabDataBaseQueryElement deleteQuery = null;
    protected JeproLabDataBaseQueryElement updateQuery = null;
    protected JeproLabDataBaseQueryElement insertQuery = null;
    protected JeproLabDataBaseQueryElement fromQuery = null;
    protected JeproLabDataBaseQueryElement joinQuery = null;
    protected JeproLabDataBaseQueryElement setStatement = null;
    protected JeproLabDataBaseQueryElement whereQuery = null;
    protected JeproLabDataBaseQueryElement groupQuery = null;
    protected JeproLabDataBaseQueryElement havingQuery = null;
    protected JeproLabDataBaseQueryElement columnsQuery = null;
    protected JeproLabDataBaseQueryElement valuesQuery = null;
    protected JeproLabDataBaseQueryElement orderQuery = null;
    protected JeproLabDataBaseQueryElement callQuery = null;
    //protected JeproLabDataBaseQueryElement execQuery = null;
    protected JeproLabDataBaseQueryElement unionQuery = null;
    protected JeproLabDataBaseQueryElement unionAllQuery = null;
    protected JeproLabDataBaseQueryElement execQuery = null;
    protected JeproLabDataBaseQueryElement autoIncrementField = null;

    protected String queryType = "";

    /** Int the offset for the result set **/
    protected int queryOffset;

    /** Int the limit for the result set **/
    protected int queryLimit;


    public JeproLabDataBaseQuery(){
        this(null);
    }
    /**
     *
     * @param dataBaseConnector
     */
    public JeproLabDataBaseQuery(JeproLabDataBaseConnector dataBaseConnector){
        dbc = dataBaseConnector;
    }

    public String queryToString(){
        if(this.sqlQuery != null && !this.sqlQuery.equals("")){
            return this.sqlQuery;
        }
        String query = "";
        switch (this.queryType){
            case "element":
                query += this.element;
                break;
            case "select":
                query += this.selectQuery.toString() + this.fromQuery.toString();
                if(this.joinQuery.isSet()){
                    //Special case for joins
                    for(String join : this.joinQuery.elements){
                        query += join;
                    }
                }

                if(this.whereQuery.isSet()){
                    query += this.whereQuery;
                }

                if(this.groupQuery.isSet()){
                    query += this.groupQuery;
                }

                if(this.havingQuery.isSet()){
                    query += this.whereQuery;
                }

                if(this.orderQuery.isSet()){
                    query += this.groupQuery;
                }
                break;
            case "union":
                query += this.unionQuery;
                break;
            case "unionAll":
                query += this.unionAllQuery;
                break;
            case "delete":
                //query += this.deleteQuery + this.fromQuery;
                if(this.joinQuery.isSet()){
                    for(String join : this.joinQuery.elements){
                        query += join;
                    }
                }

                if (this.whereQuery.isSet()) {
                    query += this.whereQuery;
                }
                break;
            case "update":
                query += this.updateQuery;

                if(this.joinQuery.isSet()){
                    for(String join : this.joinQuery.elements){
                        query += join;
                    }
                }

                query += this.setStatement;

                if (this.whereQuery.isSet()) {
                    query += this.whereQuery;
                }
                break;
            case "insert":
                query += this.insertQuery;
                if(this.setStatement.isSet()){
                    query += this.setStatement;
                }else if(this.valuesQuery.isSet()){
                    if(this.columnsQuery.isSet()){
                        query += this.columnsQuery;
                    }
                    ArrayList<String> elements = this.valuesQuery.getElements();

                    /*if(!elements[0] instanceof JeproLabDataBaseQuery){
                        query +=  " VALUES ";
                    }*/
                    query += this.valuesQuery;
                }
                break;
            case "call":
                query += this.callQuery;
            case "exec":
                query += this.execQuery;
                break;
        }

        if(this instanceof JeproLabDataBaseQueryLimitable){
            query = this.processLimit(query, this.queryLimit, this.queryOffset);
        }
        return  query;
    }

    /**
     * clear data from th query or a specific clause
     * @param clause
     * @return
     */
    public JeproLabDataBaseQuery clear(String clause){
        this.sqlQuery = null;
        switch(clause){
            case "select" :
                this.selectQuery = null;
                this.queryType = null;
                break;
            case "delete" :
                this.deleteQuery = null;
                this.queryType = null;
                break;
            case "update" :
                this.updateQuery = null;
                this.queryType = null;
                break;
            case "insert" :
                this.insertQuery = null;
                this.queryType = null;
                this.autoIncrementField = null;
                break;
            case "from" :
                this.fromQuery = null;
                break;
            case "join" :
                this.joinQuery = null;
                break;
            case "set" :
                this.setStatement = null;
                break;
            case "where" :
                this.whereQuery = null;
                break;
            case "group" :
                this.groupQuery = null;
                break;
            case "having" :
                this.havingQuery = null;
                break;
            case "order" :
                this.orderQuery = null;
                break;
            case "columns" :
                this.columnsQuery = null;
                break;
            case "values" :
                this.valuesQuery = null;
                break;
            case "exec" :
                this.execQuery = null;
                this.queryType = null;
                break;
            case "call" :
                this.callQuery = null;
                this.queryType = null;
                break;
            case "limit" :
                this.queryLimit = 0;
            case "offset" :
                this.queryOffset = 0;
            case "union" :
                this.unionQuery = null;
                break;
            case "unionAll" :
                this.unionAllQuery = null;
                break;
            default :
                this.queryType = null;
                this.selectQuery = null;
                this.deleteQuery = null;
                this.updateQuery = null;
                this.insertQuery = null;
                this.fromQuery = null;
                this.joinQuery = null;
                this.setStatement = null;
                this.whereQuery = null;
                this.groupQuery = null;
                this.havingQuery = null;
                this.orderQuery = null;
                this.columnsQuery = null;
                this.valuesQuery = null;
                this.autoIncrementField = null;
                this.execQuery = null;
                this.callQuery = null;
                this.unionQuery = null;
                this.unionAllQuery = null;
                this.queryLimit = 0;
                this.queryOffset = 0;
                break;
        }
        return this;
    }

    protected abstract String processLimit(String query);
    protected abstract String processLimit(String query, int limit);
    protected abstract String processLimit(String query, int limit, int offset);

    public abstract String quote(String field);


    public static class JeproLabDataBaseQueryElement {
        /**
         * String the name of the element
         */
        protected String name = null;

        /**
         * Array List of
         */
        protected ArrayList<String> elements;

        protected String elementGlue;

        private boolean containsData = false;

        public JeproLabDataBaseQueryElement(String eltName, ArrayList<String> elts, String eltGlue){
            this.elements = new ArrayList<>();
            this.name = eltName;
            this.elementGlue = eltGlue;

            this.appendElements(elts);
        }

        public boolean isSet(){
            return containsData;
        }

        public ArrayList<String> getElements(){
            return this.elements;
        }

        public void appendElements(ArrayList<String> elts){

        }
    }
}