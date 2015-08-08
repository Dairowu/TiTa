package xietong.tita;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import guide.dmemd;

/**
 * Created by acer-PC on 2015/8/6.
 * 登录时只显示一张图片
 *
 */
public class ActivityStart extends Activity {
    boolean isFirstIn = false;

    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;

    private static final String SHAREDPREFERENCE_NAME = "first_pref";
    private static final long SPLASH_DELAY_MILLS = 3000;

    private Handler mhandler = new Handler(){

        public void handleMessage(Message msg){
            switch(msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    private void goGuide(){
        Intent intent = new Intent(ActivityStart.this, dmemd.class);
        startActivity(intent);
        finish();
    }
    private void goHome(){

        Intent intent = new Intent(ActivityStart.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //把这个Activity添加，退出应用时才会finish
        FinishApp.addActivity(this);

        init();

        //如果换肤的结果是为空的话
        //第一次使用时默认加载背景存储到sharedPreference
        String string = getSharedPreferences("music_play", MODE_WORLD_READABLE)
                .getString("background", "");
        SharedPreferences.Editor editor = getSharedPreferences("music_play", MODE_WORLD_READABLE).edit();
        if (string.equals("")) {
            Resources r = getResources();
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(R.drawable.bg) + "/"
                    + r.getResourceTypeName(R.drawable.bg) + "/"
                    + r.getResourceEntryName(R.drawable.bg));
            editor.putString("background", uri.toString());
            editor.commit();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //加载音乐到list
                    Utils.loadFromSD(ActivityStart.this);
                    startService(new Intent(ActivityStart.this, LockService.class));
//                    Intent intent = new Intent(ActivityStart.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    Thread.sleep(1400);
//                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void init() {
        SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if(!isFirstIn){
            mhandler.sendEmptyMessageDelayed(GO_HOME,SPLASH_DELAY_MILLS);
        } else{
            mhandler.sendEmptyMessageDelayed(GO_GUIDE,SPLASH_DELAY_MILLS);
        }
        editor.putBoolean("isFirstIn",false);
        editor.commit();
    }
}
