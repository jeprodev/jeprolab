package com.jeprolab.models;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;
import javafx.scene.control.Pagination;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAddressModel extends JeproLabModel {
    private static Pagination pagination;

    public static ResultSet getAddressList(){
        int limit = JeproLabConfig.getListLimit();
        return getAddressList(limit);
    }

    public static ResultSet getAddressList(int limit){
        return getAddressList(limit, 0);
    }

    public static ResultSet getAddressList(int limit, int limitStart){
        return getAddressList(limit, limitStart, 1);
    }

    public static ResultSet getAddressList(int limit, int limitStart, int langId){
        return getAddressList(limit, limitStart, langId, "address_id");
    }

    public static ResultSet getAddressList(int limit, int limitStart, int langId, String orderBy){
        return getAddressList(limit, limitStart, langId, orderBy, "ASC");
    }

    public static ResultSet getAddressList(int limit, int limitStart, int langId, String orderBy, String orderWay){
        JeproLabDataBaseConnector dbc = JeproLabFactory.getDataBaseConnector();
        int total = 0;
        ResultSet addresses = null;
        boolean useLimit = true;
        if(limit <= 0){ useLimit = false; }
        String orderByFilter = orderBy.replace("`", "");
        try {
            do {
                String query = "SELECT SQL_CALC_FOUND_ROWS address." + dbc.quoteName("address_id") + ", address." + dbc.quoteName("firstname");
                query += ", address." + dbc.quoteName("lastname") + ", address." + dbc.quoteName("address1") + ", address." + dbc.quoteName("postcode");
                query += ", address." + dbc.quoteName("city") + ", country_lang." + dbc.quoteName("name") + " AS country FROM " + dbc.quoteName("#__jeprolab_address");
                query += " AS address LEFT JOIN " + dbc.quoteName("#__jeprolab_country_lang") + " AS country_lang ON(country_lang." + dbc.quoteName("country_id");
                query += " = address." + dbc.quoteName("country_id") + " AND country_lang." + dbc.quoteName("lang_id") + " = " + langId + ") LEFT JOIN ";
                query += dbc.quoteName("#__jeprolab_customer") + " AS customer ON address." + dbc.quoteName("customer_id") + " = customer.";
                query += dbc.quoteName("customer_id") + " WHERE address.customer_id != 0 " + JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER, "customer");
                query += " ORDER BY " + (orderByFilter.equals("address_id") ? "address." : "") + orderBy + " " + orderWay;

                dbc.setQuery(query);
                addresses = dbc.loadObject();

                while (addresses.next()) {
                    total++;
                }

                query += (useLimit ? " LIMIT " + limitStart + ", " + limit : " ");

                dbc.setQuery(query);
                addresses = dbc.loadObject();

                if(useLimit){
                    limitStart = limitStart - limit;
                    if(limitStart < 0){ break; }
                }else {
                    break;
                }
            } while (addresses == null);

            pagination = new Pagination();

        }catch (SQLException io){

        }
        return  addresses;
    }
}