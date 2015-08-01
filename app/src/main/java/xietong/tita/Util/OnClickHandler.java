package xietong.tita.Util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by TeenTeam on 2015/7/31.
 * 用于处理左边列表点击后事项的处理
 */
public class OnClickHandler implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listview = null;
        selectItem(position, listview);
    }
    private void selectItem(int position,ListView listview){

    }
}
