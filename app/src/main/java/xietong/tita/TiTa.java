package xietong.tita;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by LZC
 * */
public class TiTa extends Activity implements View.OnClickListener {

    ServiceReceiver receiver;
    //用来修改播放模式
    Spinner spinner_mode;
    //用来显示进度
    SeekBar seekBarTime;
    TextView textTimeNow, textTimeAll;
    TextView textProgressChange;
    //用来判断线程是否需要进入长期睡眠
    int songStatus;
    //用来确定当前播放的时间
    int songNow = 0;
    //判断是否是第一首放的歌
    boolean isFirstplay = true;

    public static final int notifyId = 100;
    ListView listView;
    ImageButton buttonPlay, buttonNext, buttonLast;

    //用来定时修改播放时间的Thread
    Thread thread;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lrc);

        seekBarTime = (SeekBar) findViewById(R.id.seekBarTime);
        textTimeNow = (TextView) findViewById(R.id.textSongTimeNow);
        textTimeAll = (TextView) findViewById(R.id.textSongDuration);
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonLast = (ImageButton) findViewById(R.id.buttonLast);
        buttonNext = (ImageButton) findViewById(R.id.buttonNext);
        textProgressChange = (TextView) findViewById(R.id.textProgressChange);
        spinner_mode = (Spinner) findViewById(R.id.play_mode);

        //初始化播放时间以及歌曲长度
        textProgressChange.setAlpha(0);
        textTimeNow.setText("0:00");
        //添加监听
        buttonPlay.setOnClickListener(this);
        buttonLast.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        seekBarTime.setOnSeekBarChangeListener(new seekBarListener());

        //表示从Service接受到的Broadcast将在ServiceReciver处理
        receiver = new ServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MAIN);
        registerReceiver(receiver, intentFilter);

        //启动后台播放音乐的Service
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        //显示当前歌曲的总时长
        textTimeAll.setText(Utils.millsToMinute());

        //加载播放模式的Spinner
        loadPlayMode();

        //通过SharedPreference来初始化
        sharedPreferences = getSharedPreferences("music_play", MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        Utils.setCurrentSong(sharedPreferences.getInt("finalSong", 0));
        seekBarTime.setProgress(sharedPreferences.getInt("progress", 0));
        spinner_mode.setSelection(sharedPreferences.getInt("playMode", 0));

    }

    //加载播放模式设置的的Spinner
    private void loadPlayMode() {
        String[] modeText = new String[]{" 顺序播放", " 随机播放", " 单曲循环"};
        int[] imagesId = new int[]{R.drawable.play_mode_order,
                R.drawable.play_mode_random,
                R.drawable.play_mode_repeat,
        };
        List<Map<String, Object>> spinnerList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < imagesId.length; i++) {
            Map<String, Object> spinnerItem = new HashMap<String, Object>();
            spinnerItem.put("modeView", imagesId[i]);
            spinnerItem.put("modeText", modeText[i]);
            spinnerList.add(spinnerItem);
        }
        SimpleAdapter spinnerAdapter = new SimpleAdapter(TiTa.this, spinnerList, R.layout.layout_play_mode,
                new String[]{"modeView", "modeText"}, new int[]{R.id.mode_view, R.id.mode_text});
        spinner_mode.setAdapter(spinnerAdapter);
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.setPlayMode(TiTa.this, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Activity上的按钮点击事件
    @Override
    public void onClick(View view) {

        Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
        switch (view.getId()) {
            case R.id.buttonPlay:
                intent.putExtra("buttonChoose", Utils.BN_LAST);
                intent.putExtra("buttonChoose", Utils.BN_PLAY);
                break;
            case R.id.buttonLast:
                intent.putExtra("buttonChoose", Utils.BN_LAST);
                break;
            case R.id.buttonNext:
                intent.putExtra("buttonChoose", Utils.BN_NEXT);
                break;
        }
        intent.putExtra("progress", seekBarTime.getProgress());
        sendBroadcast(intent);
    }

    //处理从Service传来的广播
    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra("status", -1);
            songStatus = status;

            if (isFirstplay) {
                thread = new Thread(new MyRunnable());
                thread.start();
                isFirstplay = false;
            }

            //修改seekBar和歌曲时长
            seekBarTime.setMax(Utils.seekbarMax());
            textTimeAll.setText(Utils.millsToMinute());

            int progress = intent.getIntExtra("progress", 0);
            if (progress == 0) {
                textTimeNow.setText("0:00");
                songNow = 0;
                Utils.getAdapter().setSelectItem(Utils.getCurrentSong());
                Utils.getAdapter().notifyDataSetChanged();

            } else {
                songNow = progress;
                textTimeNow.setText(Utils.progressToShow(progress));
            }

            seekBarTime.setProgress(progress);

            //此处应该修改按钮的显示
            switch (status) {
                case Utils.PLAYING:
                    break;
                case Utils.PAUSING:
                    textProgressChange.setText("");
                    break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //seekBar事件处理
    private class seekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            //添加判断是因为在点击下一首时会触发这个事件
            // 当时选择切歌时，会使这个为0
            if (seekBar.getProgress() == 0) {
                textProgressChange.setText("");
            }
            //如果是由拖动seekBar引起的
            else if (Utils.isTouchSeekBar) {
                textProgressChange.setText(Utils.progressToShow(i));
            }

            textTimeNow.setText(Utils.progressToShow(i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            textProgressChange.setText(Utils.progressToShow(seekBar.getProgress()));

            //用来判断是否需要在progressChangeText上显示时间
            Utils.isTouchSeekBar = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            textProgressChange.setText("");

            Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
            intent.putExtra("progress", seekBar.getProgress());
            intent.putExtra("buttonChoose", Utils.BN_PROGRESS);
            sendBroadcast(intent);

            Utils.isTouchSeekBar = false;
        }
    }

    //线程执行
    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            //歌曲时间单位是秒
            //每0.2秒刷新一次
            int i = 1;
            while (true) {

                //如果歌曲处于暂停状态，则使线程进入睡眠
                //每0.1秒醒来一次，检查用户是否已经重新开始播放了
                if (songStatus == Utils.PAUSING) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //如果线程不是处于睡眠状态
                //0.2秒刷新一次，并且每过一秒就使progress前进一次
                else {
                    try {
                        Thread.sleep(200);
                        if (i == 5) {
                            seekBarTime.setProgress(songNow);
                            songNow+=1000;
                            i = 0;
                        }
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt("finalSong",Utils.getCurrentSong());
        editor.putInt("playMode", Utils.play_mode);
        editor.putInt("progress", seekBarTime.getProgress());
        editor.commit();

    }
}
