package downloadmp3;

import android.app.TabActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

import SQLite.DatabaseHelper;
import xietong.tita.R;

public class DownSongListActivity extends TabActivity {

    ListView lv_success;
    ListView lv_failure;
    DatabaseHelper dbHelper = new DatabaseHelper(this,"songDown");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_song_list);

        lv_success = (ListView)findViewById(R.id.lv_success);
        lv_failure = (ListView)findViewById(R.id.lv_failure);

        Cursor cursor = dbHelper.getReadableDatabase().query("down_song",new String[]{"_id","songName","artistName"},
                null,null,null,null,null);

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
           DownSongListActivity.this,R.layout.songitems,cursor,new String[]{"_id","songName","artistName"},
                new int[]{R.id.tv_lineNumber,R.id.tv_songName,R.id.tv_artistName}
        );
        lv_success.setAdapter(simpleCursorAdapter);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1")
                .setIndicator("下载成功")
                .setContent(R.id.lv_success);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab1")
                .setIndicator("正在下载")
                .setContent(R.id.lv_failure);
        tabHost.addTab(tab2);
    }
}
