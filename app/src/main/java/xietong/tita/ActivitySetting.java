package xietong.tita;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by acer-PC on 2015/8/6.
 * 用来设置歌词界面是否常亮
 * 用来设置是否显示锁屏
 */
public class ActivitySetting extends Activity {

    Button bnBack;
    //歌词界面常亮的开关
    Switch switchWake;
    //锁屏是否显示歌词的开关
    Switch switchLock;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bnBack = (Button) findViewById(R.id.bnSettingBack);
        switchWake = (Switch) findViewById(R.id.switchWake);
        switchLock = (Switch) findViewById(R.id.switchLock);

        switchWake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("isCheck",isChecked);
                editor.commit();
            }
        });

        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("isLock",isChecked);
                editor.commit();
            }
        });

        bnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySetting.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    //在重新返回这个界面时都会更新
    @Override
    protected void onResume() {
        sharedPreferences = getSharedPreferences("music_play", MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        isChecked = sharedPreferences.getBoolean("isCheck",true);
        switchWake.setChecked(isChecked);

        switchLock.setChecked(getSharedPreferences("music_play", MODE_WORLD_READABLE).getBoolean("isLock",true));
        super.onResume();
    }
}
