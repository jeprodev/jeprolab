package com.jeprolab.models;

import com.jeprolab.assets.tools.JeproLabContext;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabStockAvailableModel extends JeproLabModel {
    /** @var int identifier of the current product */
    public int analyze_id;

    /** @var int identifier of product attribute if necessary */
    public int analyze_attribute_id;

    /** @var int the shop associated to the current product and corresponding quantity */
    public int laboratory_id;

    /** @var int the group shop associated to the current product and corresponding quantity */
    public int laboatory_group_id;

    /** @var int the quantity available for sale */
    public int quantity = 0;

    /** @var bool determine if the available stock value depends on physical stock */
    public boolean depends_on_stock = false;

    /** @var bool determine if a product is out of stock - it was previously in Product class */
    public boolean out_of_stock = false;

    public static String addSqlLaboratoryRestriction(){
        return addSqlLaboratoryRestriction(null, "");
    }

    public static String addSqlLaboratoryRestriction(JeproLabLaboratoryModel lab){
        return addSqlLaboratoryRestriction(lab, "");
    }

    /**
     * Add an sql restriction for shops fields - specific to StockAvailable
     *
     * @param lab Optional : The shop ID
     * @param alias Optional : The current table alias
     *
     * @return string|DbQuery DbQuery object or the sql restriction string
     */
    public static String addSqlLaboratoryRestriction(JeproLabLaboratoryModel lab, String alias){
        JeproLabContext context = JeproLabContext.getContext();

        if (!alias.equals("")) {
            alias += ".";
        }
        JeproLabLaboratoryModel.JeproLabLaboratoryGroupModel labGroup;

        // if there is no $id_shop, gets the context one
        // get shop group too
        if (lab == null || lab.laboratory_id == context.laboratory.laboratory_id) {
            if (JeproLabLaboratoryModel.getLabContext() == JeproLabLaboratoryModel.GROUP_CONTEXT) {
                labGroup = JeproLabLaboratoryModel.getContextLaboratoryGroup();
            } else {
                labGroup = context.laboratory.getLaboratoryGroup();
            }
            lab = context.laboratory;
        } else if(lab.laboratory_id != context.laboratory.laboratory_id){
            /** @var Shop $shop */
            labGroup = lab.getLaboratoryGroup();
        } else {
            //lab = new JeproLabLaboratoryModel($shop);
            labGroup = lab.getLaboratoryGroup();
        }
        String query;

        // if quantities are shared between shops of the group
        if (labGroup.share_stocks) {
            query = " AND " + alias + "lab_group_id = " + labGroup.laboratory_group_id + " AND " + alias + "lab_id = 0 ";

        } else {
            query = " AND " + alias + "lab_id = " + lab.laboratory_id + " AND " + alias + "lab_group_id = 0 " ;
        }

        return query;
    }
}
