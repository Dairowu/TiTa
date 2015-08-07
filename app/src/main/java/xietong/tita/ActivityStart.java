package xietong.tita;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by acer-PC on 2015/8/6.
 * 登录时只显示一张图片
 */
public class ActivityStart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FinishApp.addActivity(this);

        //如果换肤的结果是为空的话
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
                    Intent intent = new Intent(ActivityStart.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startService(new Intent(ActivityStart.this, LockService.class));
                    Thread.sleep(1400);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
