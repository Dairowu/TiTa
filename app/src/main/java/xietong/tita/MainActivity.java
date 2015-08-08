package xietong.tita;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import downloadmp3.DownSongListActivity;
import downloadmp3.DownloadMp3;

/**
 * Created by acer-PC on 2015/8/2.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int DIALOG_DISMISS = 0;// 对话框消失
    public static final int DIALOG_MENU_INFO = 1;// 歌曲详情对话框
    public static final int DIALOG_DELETE = 2;// 歌曲删除对话框

    DrawerLayout drawerLayout;
    //定义中间四个按钮
    Button bnLocal, bndownLoad, bnLike, bnList;

    //定义底部切歌的处理
    ImageButton bnNext, bnPlay, bnLast;

    RelativeLayout mini_relativeLayout;

    //底部按钮的广播事件
    ListServiceReceiver receiver;
    //显示底部的歌名显示
    TextView textArtist, textTitle;

    NestedScrollView nestedScrollView;
    LinearLayout layoutThis;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FinishApp.addActivity(this);

        init();

        //设置actionBar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置actionBar的返回按钮
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        //添加toolbar的按钮监听
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        //设置toolbar的标题
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collLayout);
        //设置toolBar的标题
        collapsingToolbar.setTitle("TiTa");

        //设置侧滑菜单点击事件的监听
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationListener());
        }

        //开启service
        startService(new Intent(MainActivity.this, MusicService.class));

        //初始化广播
        MyNotification.prepareNotification(MainActivity.this);
//        MyNotification.rePreareNotify(MainActivity.this);
        Log.e("MainActivity", "调用OnCreate");

        //开启service
        startService(new Intent(MainActivity.this, MusicService.class));
        //打开应用时提醒在哪首歌
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //显示打开应用时处于哪首歌
                String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
                String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
                MyNotification.showNotifica(title, artist, null);

                MyNotification.showNotifyButton();
            }
        }).start();

        String artist = null;
        String title = null;
        //修改显示
        if (Utils.getCurrentSong()>=Utils.getList().size()){
            artist = Utils.getList().get(0).get("songArtist").toString();
            title = Utils.getList().get(0).get("songTitle").toString();
        }
        else{
            artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
            title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        }
        textArtist.setText(artist);
        textTitle.setText(title);

    }

    public void init() {

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        bndownLoad = (Button) findViewById(R.id.bnDownLoad);
        bnLike = (Button) findViewById(R.id.bnLike);
        bnList = (Button) findViewById(R.id.bnList);
        bnLocal = (Button) findViewById(R.id.bnLocalMusic);
        bnNext = (ImageButton) findViewById(R.id.local_miniplayer_next);
        bnLast = (ImageButton) findViewById(R.id.local_miniplayer_last);
        bnPlay = (ImageButton) findViewById(R.id.local_miniplayer_play);
        textArtist = (TextView) findViewById(R.id.local_miniplayer_artist);
        textTitle = (TextView) findViewById(R.id.local_miniplayer_song);

        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);

        mini_relativeLayout = (RelativeLayout) findViewById(R.id.local_miniplayer_layout);
        mini_relativeLayout.setOnClickListener(this);

        bnPlay.setOnClickListener(this);
        bnNext.setOnClickListener(this);
        bnLast.setOnClickListener(this);
        bnLocal.setOnClickListener(this);
        bnList.setOnClickListener(this);
        bnLike.setOnClickListener(this);
        bndownLoad.setOnClickListener(this);

//        Cursor cursor;
//        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, MediaStore.Audio.AudioColumns.TITLE);
//        MyBaseAdapter baseAdapter = new MyBaseAdapter(MainActivity.this, cursor);
//        Utils.setAdapter(baseAdapter);

        //表示从Service接受到的Broadcast将在ServiceReciver处理
        receiver = new ListServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MAIN);
        registerReceiver(receiver, intentFilter);

    }

    //用来处理按钮的点击事件
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //点击本地音乐
            case R.id.bnLocalMusic:
                Intent intentToList = new Intent(MainActivity.this, LocalMusicSongListActivity.class);
                intentToList.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentToList);
                break;

            //点击下载列表
            case R.id.bnDownLoad:
                Intent intent = new Intent(MainActivity.this, DownSongListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;

            //点击我的喜欢
            case R.id.bnLike:
                break;

            //点击我的列表
            case R.id.bnList:
                break;

            case R.id.local_miniplayer_next:
                Utils.bnSendBroadcast(MainActivity.this, Utils.BN_NEXT);
                break;

            case R.id.local_miniplayer_last:
                Utils.bnSendBroadcast(MainActivity.this, Utils.BN_LAST);
                break;

            case R.id.local_miniplayer_play:
                //修改播放按钮的显示
                if (Utils.getStatus() != Utils.PLAYING) {
                    bnPlay.setSelected(true);
                } else {
                    bnPlay.setSelected(false);
                }
                Utils.bnSendBroadcast(MainActivity.this, Utils.BN_PLAY);
                break;

            case R.id.local_miniplayer_layout:
                Intent intentToLrc = new Intent(MainActivity.this, TiTa.class);
                intentToLrc.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentToLrc);
                break;
        }

    }

    //处理抽屉item点击事件
    private class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            String msg = "";
            switch (menuItem.getItemId()) {
                //点击音乐按钮
                case R.id.nav_music:
                    break;

                //点击退出
                case R.id.nav_exit:
                    FinishApp.finishAllActivity(MainActivity.this);
                    break;

                //点击喜欢
                case R.id.nav_like:
                    break;

                //点击关于
                case R.id.nav_about:
                    Intent intentToInfo = new Intent(MainActivity.this, AppInfoActivity.class);
                    intentToInfo.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentToInfo);
                    break;

                //点击换肤
                case R.id.nav_background:
                    Intent intentToBackground = new Intent(MainActivity.this, ChangeBackgroungActivity.class);
                    intentToBackground.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentToBackground);
                    break;

                //点击设置
                case R.id.nav_setting:
                    Intent intentToSetting = new Intent(MainActivity.this,ActivitySetting.class);
                    intentToSetting.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentToSetting);
                    break;

            }
            //关闭抽屉
            drawerLayout.closeDrawers();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //id为home的图标代表是左上角第一个图标
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.toolbar_search:
                    Intent intent = new Intent(MainActivity.this, DownloadMp3.class);
                    startActivity(intent);
                    msg += "点击了搜索歌曲的按钮";
                    break;
                case R.id.toolbar_others:
                    msg += "点击了其他设置的按钮";
                    break;
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            return false;
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {

        String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        textArtist.setText(artist);
        textTitle.setText(title);
        drawerLayout.closeDrawers();

        if (Utils.getStatus() != Utils.PLAYING) {
            bnPlay.setSelected(false);
        } else {
            bnPlay.setSelected(true);
        }
        super.onResume();

    }

    @Override
    protected void onStop() {
//        unregisterReceiver(receiver);
        Log.e("MainActivity","onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.e("MainA","onRestart");
//        receiver = new ListServiceReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Utils.ACTION_TO_MAIN);
//        registerReceiver(receiver, intentFilter);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        //注销广播
        unregisterReceiver(receiver);
        MyNotification.unRegistNotifyReceiver(MainActivity.this);
        Log.e("onDestroy", "destroying");
        super.onDestroy();
    }


    //需要监听返回按钮，默认返回时会销毁Activity，但是并不需要销毁Activity
    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
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
            if (Utils.getStatus() != Utils.PLAYING) {
                bnPlay.setSelected(false);
            } else {
                bnPlay.setSelected(true);
            }
        }
    }

    //处理悬浮按钮的点击事件
    public void FloatBnUser(View view) {

        Intent intent = new Intent(MainActivity.this, UserLogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

}
