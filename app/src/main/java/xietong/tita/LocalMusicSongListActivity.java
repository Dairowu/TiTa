package xietong.tita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

/**
 * Created by acer-PC on 2015/8/3.
 */
public class LocalMusicSongListActivity extends Activity {

    List<Map<String, Object>> songLists;
    MyBaseAdapter listAdapter;
    ListView localListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music);

        localListView = (ListView) findViewById(R.id.local_listview);
        songLists = Utils.getList();
        listAdapter = new MyBaseAdapter(LocalMusicSongListActivity.this,songLists);
        localListView.setAdapter(listAdapter);
        localListView.setOnItemClickListener(new ListItemClick());

        Utils.setAdapter(listAdapter);
    }

    private class ListItemClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }

    //当点击歌曲列表项时
    private class itemClickListe implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //判断点击的项是不是当前正在播放的音乐
            if (Utils.getCurrentSong() == i) {
                Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
                intent.putExtra("buttonChoose", Utils.BN_PLAY);
//                intent.putExtra("progress", seekBarTime.getProgress());
                sendBroadcast(intent);

            } else {
                Utils.setCurrentSong(i);
                Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
                intent.putExtra("buttonChoose", Utils.ITEM);
                sendBroadcast(intent);
            }
        }
    }

    //设置浮动按钮的监听
    public void checkin(View view){
        if (Utils.getCurrentSong() < 5||Utils.getCurrentSong()>songLists.size()-4)
            localListView.setSelection(Utils.getCurrentSong());
        else localListView.setSelection(Utils.getCurrentSong()-4);
    }
}
