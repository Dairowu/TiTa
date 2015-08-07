package xietong.tita;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by acer-PC on 2015/8/3.
 */
public class LocalMusicSongListActivity extends Activity implements View.OnClickListener {

    List<Map<String, Object>> songLists;
    MyBaseAdapter listAdapter;
    ListView localListView;

    ListServiceReceiver receiver;

    //定义底部切歌的处理
    ImageButton bnNext, bnPlay, bnLast,bnBack;

    TextView textArtist,textTitle;

    RelativeLayout mini_relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music);
        FinishApp.addActivity(this);

        bnNext = (ImageButton) findViewById(R.id.local_miniplayer_next);
        bnLast = (ImageButton) findViewById(R.id.local_miniplayer_last);
        bnPlay = (ImageButton) findViewById(R.id.local_miniplayer_play);
        bnBack = (ImageButton) findViewById(R.id.local_actionbar_back);
        textArtist = (TextView) findViewById(R.id.local_miniplayer_artist);
        textTitle = (TextView) findViewById(R.id.local_miniplayer_song);
        bnPlay.setOnClickListener(this);
        bnNext.setOnClickListener(this);
        bnLast.setOnClickListener(this);
        bnBack.setOnClickListener(this);

        mini_relativeLayout = (RelativeLayout) findViewById(R.id.local_miniplayer_layout);
        mini_relativeLayout.setOnClickListener(this);

        //加載listView
        localListView = (ListView) findViewById(R.id.local_listview);
        songLists = Utils.getList();

        listAdapter = Utils.getAdapter();
//                new MyBaseAdapter(LocalMusicSongListActivity.this, songLists);
        localListView.setAdapter(listAdapter);
        localListView.setOnItemClickListener(new ListItemClick());

//        Utils.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_miniplayer_next:
                Utils.bnSendBroadcast(LocalMusicSongListActivity.this, Utils.BN_NEXT);
                break;

            case R.id.local_miniplayer_last:
                Utils.bnSendBroadcast(LocalMusicSongListActivity.this, Utils.BN_LAST);
                break;

            case R.id.local_miniplayer_play:
                //修改播放按钮的显示
                if (Utils.getStatus()!=Utils.PLAYING){
                    bnPlay.setSelected(true);
                }
                else {
                    bnPlay.setSelected(false);
                }
                Utils.bnSendBroadcast(LocalMusicSongListActivity.this, Utils.BN_PLAY);
                break;

            case R.id.local_miniplayer_layout:
                Intent intent = new Intent(LocalMusicSongListActivity.this,TiTa.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.local_actionbar_back:
                Intent intent1 = new Intent(LocalMusicSongListActivity.this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
                break;
        }
    }


    //当点击歌曲列表项时
    private class ListItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
            //判断点击的项是不是当前正在播放的音乐
            if (Utils.getCurrentSong() == i) {
                intent.putExtra("buttonChoose", Utils.BN_PLAY);
                sendBroadcast(intent);
                Log.e("TiTa", "列表項點擊11" + i);
            } else {
                Utils.setCurrentSong(i);
                intent.putExtra("buttonChoose", Utils.ITEM);
                sendBroadcast(intent);
                Log.e("TiTa", "列表項點擊" + i);
            }
            Utils.getAdapter().setSelectItem(i);

        }


    }

    //设置浮动按钮的监听
    public void checkin(View view) {
        if (Utils.getCurrentSong() < 5 || Utils.getCurrentSong() > songLists.size() - 4)
            localListView.setSelection(Utils.getCurrentSong());
        else localListView.setSelection(Utils.getCurrentSong() - 4);
    }

    @Override
    protected void onResume() {

        String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        textArtist.setText(artist);
        textTitle.setText(title);
        //修改按钮显示
        if (Utils.getStatus()!=Utils.PLAYING){
            bnPlay.setSelected(false);
        }
        else {
            bnPlay.setSelected(true);
        }

        //表示从Service接受到的Broadcast将在ServiceReciver处理
        receiver = new ListServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MAIN);
        registerReceiver(receiver, intentFilter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        //注销监听
        unregisterReceiver(receiver);
        super.onPause();
    }

    //处理从Service传来的广播
    public class ListServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
            String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
            textArtist.setText(artist);
            textTitle.setText(title);
            //修改播放按钮的显示
            if (Utils.getStatus()!=Utils.PLAYING){
                bnPlay.setSelected(false);
            }
            else {
                bnPlay.setSelected(true);
            }
        }
    }
}
