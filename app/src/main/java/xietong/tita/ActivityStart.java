package xietong.tita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by acer-PC on 2015/8/6.
 * 登录时只显示一张图片
 *
 */
public class ActivityStart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FinishApp.addActivity(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //加载音乐到list
                    Utils.loadFromSD(ActivityStart.this);
                    Intent intent = new Intent(ActivityStart.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    Thread.sleep(1500);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
