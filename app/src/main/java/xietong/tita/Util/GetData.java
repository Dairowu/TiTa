package xietong.tita.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TeenTeam on 2015/7/31.
 */
public class GetData {

    List<Map<String,Object>> list;


    public List<Map<String,Object>> getDate(String[] data,int []id){
        list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<data.length;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("music", data[i]);
            map.put("icon", id[i]);
            list.add(map);
        }

        return list;
    }

}
