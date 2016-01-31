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
        if(staticDataBaseObject == null){
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        int total = 0;
        ResultSet addresses = null;
        boolean useLimit = true;
        if(limit <= 0){ useLimit = false; }
        String orderByFilter = orderBy.replace("`", "");
        try {
            do {
                String query = "SELECT SQL_CALC_FOUND_ROWS address." + staticDataBaseObject.quoteName("address_id");
                query += ", address." + staticDataBaseObject.quoteName("firstname") + ", address." + staticDataBaseObject.quoteName("lastname");
                query += ", address." + staticDataBaseObject.quoteName("address1") + ", address." + staticDataBaseObject.quoteName("postcode");
                query += ", address." + staticDataBaseObject.quoteName("city") + ", country_lang." + staticDataBaseObject.quoteName("name");
                query += " AS country FROM " + staticDataBaseObject.quoteName("#__jeprolab_address") + " AS address LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_country_lang") + " AS country_lang ON(country_lang.";
                query += staticDataBaseObject.quoteName("country_id") + " = address." + staticDataBaseObject.quoteName("country_id");
                query += " AND country_lang." + staticDataBaseObject.quoteName("lang_id") + " = " + langId + ") LEFT JOIN ";
                query += staticDataBaseObject.quoteName("#__jeprolab_customer") + " AS customer ON address." + staticDataBaseObject.quoteName("customer_id");
                query += " = customer." + staticDataBaseObject.quoteName("customer_id") + " WHERE address.customer_id != 0 ";
                query += JeproLabLaboratoryModel.addSqlRestriction(JeproLabLaboratoryModel.SHARE_CUSTOMER, "customer");
                query += " ORDER BY " + (orderByFilter.equals("address_id") ? "address." : "") + orderBy + " " + orderWay;

                staticDataBaseObject.setQuery(query);
                addresses = staticDataBaseObject.loadObject();

                while (addresses.next()) {
                    total++;
                }

                query += (useLimit ? " LIMIT " + limitStart + ", " + limit : " ");

                staticDataBaseObject.setQuery(query);
                addresses = staticDataBaseObject.loadObject();

                if(useLimit){
                    limitStart = limitStart - limit;
                    if(limitStart < 0){ break; }
                }else {
                    break;
                }
            } while (addresses == null);

            pagination = new Pagination();

        }catch (SQLException ignored){
            ignored.printStackTrace();
        }
        return  addresses;
    }
}