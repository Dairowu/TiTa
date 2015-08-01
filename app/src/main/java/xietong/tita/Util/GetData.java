package xietong.tita.Util;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xietong.tita.R;

/**
 * Created by TeenTeam on 2015/7/31.
 */
public class GetData {

    List<Map<String,Object>> list;
    int id[]=new int[]{R.drawable.music,R.drawable.found,R.drawable.button_like,R.drawable.btn_info,R.drawable.setting};

    public List<Map<String,Object>> getDate(String[] data){
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
