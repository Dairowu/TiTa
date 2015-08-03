package xietong.tita;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        bndownLoad = (Button) findViewById(R.id.bnDownLoad);
        bnLike = (Button) findViewById(R.id.bnLike);
        bnList = (Button) findViewById(R.id.bnList);
        bnLocal = (Button) findViewById(R.id.bnLocalMusic);
        bnNext = (ImageButton) findViewById(R.id.local_miniplayer_next);
        bnLast = (ImageButton) findViewById(R.id.local_miniplayer_last);
        bnPlay = (ImageButton) findViewById(R.id.local_miniplayer_play);

        mini_relativeLayout = (RelativeLayout) findViewById(R.id.local_miniplayer_layout);
        mini_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"跳转",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,TiTa.class));
            }
        });

        bnPlay.setOnClickListener(this);
        bnNext.setOnClickListener(this);
        bnLast.setOnClickListener(this);
        bnLocal.setOnClickListener(this);
        bnList.setOnClickListener(this);
        bnLike.setOnClickListener(this);
        bndownLoad.setOnClickListener(this);

        //加载音乐到list
        Utils.loadFromSD(MainActivity.this);

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

    }

    //用来处理按钮的点击事件
    @Override
    public void onClick(View view) {

        String msg = "";
        switch (view.getId()) {
            //点击本地音乐
            case R.id.bnLocalMusic:
                startActivity(new Intent(MainActivity.this,LocalMusicSongListActivity.class));
                msg = "本地";
                break;

            //点击下载列表
            case R.id.bnDownLoad:
                msg = "下载";
                break;

            //点击我的喜欢
            case R.id.bnLike:
                msg = "喜欢";
                break;

            //点击我的列表
            case R.id.bnList:
                msg = "列表";
                break;

            case R.id.local_miniplayer_next:
                Utils.bnSendBroadcast(MainActivity.this,Utils.BN_NEXT);
                msg += "下一首";
                break;

            case R.id.local_miniplayer_last:
                Utils.bnSendBroadcast(MainActivity.this,Utils.BN_LAST);
                msg += "上一首";
                break;

            case R.id.local_miniplayer_play:
                Utils.bnSendBroadcast(MainActivity.this,Utils.BN_PLAY);
                msg += "暂停";
                break;
        }
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    //加载RemoteViews并且设置打开应用时跳出的Notification
//    private void prepareRemoteViews() {
//        //初始化remoteView
//        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
//
//        //注册Action，并为按钮添加监听，发送广播
//        Intent intentLast = new Intent(Utils.NOTIFICATION_ITEM_BUTTON_LAST);
//        PendingIntent pendingIntentlast = PendingIntent.getBroadcast(TiTa.this, 0,
//                intentLast, 0);
//        remoteViews.setOnClickPendingIntent(R.id.notifyPrevious, pendingIntentlast);
//
//        Intent intentNext = new Intent(Utils.NOTIFICATION_ITEM_BUTTON_NEXT);
//        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(TiTa.this, 0,
//                intentNext, 0);
//        remoteViews.setOnClickPendingIntent(R.id.notifyNext, pendingIntentNext);
//
//
//        Intent intentPause = new Intent(Utils.NOTIFICATION_ITEM_BUTTON_PLAY);
//        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(TiTa.this, 0,
//                intentPause, 0);
//        remoteViews.setOnClickPendingIntent(R.id.notifyPlay, pendingIntentPause);
//
//        Intent intentActivity = new Intent(TiTa.this, TiTa.class);
//        PendingIntent pendingIntentActivity = PendingIntent.getActivity(TiTa.this, 0,
//                intentActivity, 0);
//        remoteViews.setOnClickPendingIntent(R.id.notifLayout, pendingIntentActivity);
//
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        //打开应用时的Notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(TiTa.this)
//                .setSmallIcon(R.drawable.music)
//                .setContentTitle("")
//                .setTicker("传递好音乐")
//                .setContentInfo("");
//        notificationManager.notify(notifyId, builder.build());
//
//        //打开应用时提醒在哪首歌
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                //显示打开应用时处于哪首歌
//                showNotifica();
//            }
//        }).start();
//
//    }
//
//    //显示Notification
//    private void showNotifica() {
//
//        remoteViews.setTextViewText(R.id.songName, songLists.get(Utils.getCurrentSong()).get("songTitle").toString());
//        remoteViews.setTextViewText(R.id.songer, songLists.get(Utils.getCurrentSong()).get("songArtist").toString());
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(TiTa.this)
//                .setContent(remoteViews)
//                .setSmallIcon(R.drawable.music)
//                .setOngoing(true)
//                .setContentTitle("")
//                .setTicker(songLists.get(Utils.getCurrentSong()).get("songShow").toString())
//                .setContentInfo("");
//
//        notificationManager.notify(notifyId, builder.build());
//    }



    //注册Notification广播
//    notifyReceiver = new NotifyReceiver();//----注册广播
//    IntentFilter intentNotify = new IntentFilter();
//    intentNotify.addAction(Utils.NOTIFICATION_ITEM_BUTTON_LAST);
//    intentNotify.addAction(Utils.NOTIFICATION_ITEM_BUTTON_PLAY);
//    intentNotify.addAction(Utils.NOTIFICATION_ITEM_BUTTON_NEXT);
//    registerReceiver(notifyReceiver, intentNotify);

    //处理从Notification传来的广播
    public class NotifyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intentNotify = new Intent(Utils.ACTION_TO_MUSICSERVICE);
            String action = intent.getAction();
            if (action.equals(Utils.NOTIFICATION_ITEM_BUTTON_LAST)) {//----通知栏播放按钮响应事件
                intentNotify.putExtra("buttonChoose", Utils.BN_LAST);

            } else if (action.equals(Utils.NOTIFICATION_ITEM_BUTTON_PLAY)) {//----通知栏播放按钮响应事件
                intentNotify.putExtra("buttonChoose", Utils.BN_PLAY);
            } else if (action.equals(Utils.NOTIFICATION_ITEM_BUTTON_NEXT)) {//----通知栏下一首按钮响应事件
                intentNotify.putExtra("buttonChoose", Utils.BN_NEXT);
            }
            intentNotify.putExtra("progress", 0);
            context.sendBroadcast(intentNotify);
        }

    }
}
