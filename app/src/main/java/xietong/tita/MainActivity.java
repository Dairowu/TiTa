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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by acer-PC on 2015/8/2.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    //定义中间四个按钮
    Button bnLocal, bndownLoad, bnLike, bnList;

    //定义底部切歌的处理
    ImageButton bnNext,bnPlay,bnLast;

    RelativeLayout mini_relativeLayout;

    //底部按钮的广播事件
    ListServiceReceiver receiver;
    //显示底部的歌名显示
    TextView textArtist,textTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //初始化广播
        MyNotification.prepareNotification(MainActivity.this);
        Log.e("MainActivity","调用OnCreate");

        startService(new Intent(MainActivity.this,MusicService.class));
    }

    public void init(){

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

        mini_relativeLayout = (RelativeLayout) findViewById(R.id.local_miniplayer_layout);
        mini_relativeLayout.setOnClickListener( this);

        bnPlay.setOnClickListener(this);
        bnNext.setOnClickListener(this);
        bnLast.setOnClickListener(this);
        bnLocal.setOnClickListener(this);
        bnList.setOnClickListener(this);
        bnLike.setOnClickListener(this);
        bndownLoad.setOnClickListener(this);
        //加载音乐到list
        Utils.loadFromSD(MainActivity.this);

        MyBaseAdapter baseAdapter = new MyBaseAdapter(MainActivity.this, Utils.getList());
        Utils.setAdapter(baseAdapter);

    }

    //用来处理按钮的点击事件
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //点击本地音乐
            case R.id.bnLocalMusic:
                startActivity(new Intent(MainActivity.this,LocalMusicSongListActivity.class));
                break;

            //点击下载列表
            case R.id.bnDownLoad:
                break;

            //点击我的喜欢
            case R.id.bnLike:
                break;

            //点击我的列表
            case R.id.bnList:
                break;

            case R.id.local_miniplayer_next:
                Utils.bnSendBroadcast(MainActivity.this,Utils.BN_NEXT);
                break;

            case R.id.local_miniplayer_last:
                Utils.bnSendBroadcast(MainActivity.this,Utils.BN_LAST);
                break;

            case R.id.local_miniplayer_play:
                Utils.bnSendBroadcast(MainActivity.this,Utils.BN_PLAY);
                break;

            case R.id.local_miniplayer_layout:
                startActivity(new Intent(MainActivity.this,TiTa.class));
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
                    msg += "音乐";
                    break;

                //点击发现
                case R.id.nav_find:
                    msg += "发现";
                    break;

                //点击喜欢
                case R.id.nav_like:
                    msg += "喜欢";
                    break;

                //点击关于
                case R.id.nav_about:
                    msg += "关于";
                    break;

                //点击换肤
                case R.id.nav_background:
                    msg += "换肤";
                    break;

                //点击设置
                case R.id.nav_setting:
                    msg += "设置";
                    break;
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {

        String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        textArtist.setText(artist);
        textTitle.setText(title);

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
        }
    }

}
