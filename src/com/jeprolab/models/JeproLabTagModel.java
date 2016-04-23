package com.jeprolab.models;

import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.core.JeproLabFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by jeprodev on 04/02/16.
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
        if (staticDataBaseObject == null) {
            staticDataBaseObject = JeproLabFactory.getDataBaseConnector();
        }
        String query = "SELECT tag." + staticDataBaseObject.quoteName("lang_id") + ", tag." + staticDataBaseObject.quoteName("name") + " FROM ";
        query += staticDataBaseObject.quoteName("#__jeprolab_tag") + " AS tag LEFT JOIN " + staticDataBaseObject.quoteName("#__jeprolab_analyze_tag");
        query += " AS analyze_tag ON (analyze_tag.tag_id = tag.tag_id) WHERE analyze_tag." + staticDataBaseObject.quoteName("analyze_id") + "= ";
        query += analyzeId;

        staticDataBaseObject.setQuery(query);
        ResultSet tagSet = staticDataBaseObject.loadObjectList();
        Map<String, String> result = new HashMap<>();
        if (tagSet != null){
            try {
                while (tagSet.next()) {
//todo
                }
            }catch (SQLException ignored) {
                ignored.printStackTrace();
            }finally{
                try{
                    JeproLabDataBaseConnector.getInstance().closeConnexion();
                }catch(Exception ignored){
                    ignored.printStackTrace();
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
}