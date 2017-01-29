package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.models.core.JeproLabFactory;
import org.apache.log4j.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabTagModel extends JeproLabModel{
    /** @var int Language id */
    public int language_id;

    /** @var string Name */
    public Map<String, String> name;

    public JeproLabTagModel(){
        this(0, null, 0);
    }

    public JeproLabTagModel(int tagId){
        this(tagId, null, 0);
    }

    public JeproLabTagModel(int tagId, Map<String, String> tagName){
        this(tagId, tagName, 0);
    }

    public JeproLabTagModel(int tagId, Map<String, String> tagName, int langId){

    }

    public static Map<String, String> getAnalyzeTags(int analyzeId) {
        if (dataBaseObject == null) {
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tag." + dataBaseObject.quoteName("lang_id") + ", tag." + dataBaseObject.quoteName("name") + " FROM ";
        query += dataBaseObject.quoteName("#__jeprolab_tag") + " AS tag LEFT JOIN " + dataBaseObject.quoteName("#__jeprolab_analyze_tag");
        query += " AS analyze_tag ON (analyze_tag.tag_id = tag.tag_id) WHERE analyze_tag." + dataBaseObject.quoteName("analyze_id") + "= ";
        query += analyzeId;

        dataBaseObject.setQuery(query);
        ResultSet tagSet = dataBaseObject.loadObjectList();
        Map<String, String> result = new HashMap<>();
        if (tagSet != null){
            try {
                while (tagSet.next()) {
//todo
                }
            }catch (SQLException ignored) {
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
            }finally{
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }
        /*if (!$tmp = Db::getInstance (_PS_USE_SQL_SLAVE_) -> executeS(''))
        {
            return false;
        }
        $result = array();
        foreach($tmp as $tag) {
            $result[$tag['id_lang']][]=$tag['name'];
        }*/
        return result;
    }

    public static boolean deleteTagsForAnalyze(int analyzeId){
        if(dataBaseObject == null){
            dataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tag_id FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_tag") + " WHERE analyze_id = " + analyzeId;
        dataBaseObject.setQuery(query);
        ResultSet tagsSet = dataBaseObject.loadObjectList();

        query = "DELETE " + dataBaseObject.quoteName("#__jeprolab_analyze_tag") + " WHERE analyzeId = " + analyzeId;
        dataBaseObject.setQuery(query);
        boolean result = dataBaseObject.query(false);

        query = "DELETE " + dataBaseObject.quoteName("#__jeprolab_tag") + " NOT EXISTS (SELECT 1 FROM " + dataBaseObject.quoteName("#__jeprolab_analyze_tag");
        query += " WHERE " + dataBaseObject.quoteName("#__jeprolab_analyze_tag") + ".tag_id = " + dataBaseObject.quoteName("#__jeprolab_analyze_tag") + ".tag_id)";

        dataBaseObject.setQuery(query);
        dataBaseObject.query(false);

        List<Integer> tagList = new ArrayList<>();
        if(tagsSet != null){
            try{
                while(tagsSet.next()){
                    tagList.add(tagsSet.getInt("tag_id"));
                }
            }catch(SQLException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }finally {
                try {
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch (Exception ignored) {
                    JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.WARN, ignored);
                }
            }
        }

        if (tagList.size() > 0) {
            JeproLabTagModel.updateTagCount(tagList);
        }
        return result;
    }

    public static void updateTagCount(List<Integer> tagList){
        /*if (!Module::getBatchMode()) {
        if ($tag_list != null) {
            $tag_list_query = ' AND pt.id_tag IN ('.implode(',', $tag_list).')';
            Db::getInstance()->execute('DELETE pt FROM `'._DB_PREFIX_.'tag_count` pt WHERE 1=1 '.$tag_list_query);
        } else {
            $tag_list_query = '';
        }



        Db::getInstance()->execute('REPLACE INTO `'._DB_PREFIX_.'tag_count` (id_group, id_tag, id_lang, id_shop, counter)
        SELECT cg.id_group, pt.id_tag, pt.id_lang, id_shop, COUNT(pt.id_tag) AS times
        FROM `'._DB_PREFIX_.'product_tag` pt
        INNER JOIN `'._DB_PREFIX_.'product_shop` product_shop
        USING (id_product)
        JOIN (SELECT DISTINCT id_group FROM `'._DB_PREFIX_.'category_group`) cg
        WHERE product_shop.`active` = 1
        AND EXISTS(SELECT 1 FROM `'._DB_PREFIX_.'category_product` cp
        LEFT JOIN `'._DB_PREFIX_.'category_group` cgo ON (cp.`id_category` = cgo.`id_category`)
        WHERE cgo.`id_group` = cg.id_group AND product_shop.`id_product` = cp.`id_product`)
        '.$tag_list_query.'
        GROUP BY pt.id_tag, pt.id_lang, cg.id_group, id_shop ORDER BY NULL');
        Db::getInstance()->execute('REPLACE INTO `'._DB_PREFIX_.'tag_count` (id_group, id_tag, id_lang, id_shop, counter)
        SELECT 0, pt.id_tag, pt.id_lang, id_shop, COUNT(pt.id_tag) AS times
        FROM `'._DB_PREFIX_.'product_tag` pt
        INNER JOIN `'._DB_PREFIX_.'product_shop` product_shop
        USING (id_product)
        WHERE product_shop.`active` = 1
        '.$tag_list_query.'
        GROUP BY pt.id_tag, pt.id_lang, id_shop ORDER BY NULL');
    }*/
    }

}
