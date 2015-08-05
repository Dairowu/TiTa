package xietong.tita;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Lrc.LyricObject;
import Lrc.LyricProcess;
import Lrc.LyricView;

/**
 * by LZC
 */
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
    //自定义显示歌词的view
    private LyricView lyricView;
    //用来对歌词文件进行处理
    private LyricProcess lyricProcess = new LyricProcess();
    //泡泡窗弹出后供用户选择下载图片和歌词
    private PopupWindow popupWindow = null;

    ImageButton buttonPlay, buttonNext, buttonLast;
    Button bnLrcBack;

    TextView lrcTitle, lrcArtist;
    //根布局
    RelativeLayout layoutThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lrc);
        init();
        //启动后台播放音乐的Service
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        //显示当前歌曲的总时长
        textTimeAll.setText(Utils.millsToMinute());
        //加载播放模式的Spinner
        loadPlayMode();
        spinner_mode.setSelection(Utils.play_mode);

    }

    //初始化按鈕并添加監聽
    public void init() {
        seekBarTime = (SeekBar) findViewById(R.id.seekBarTime);
        textTimeNow = (TextView) findViewById(R.id.textSongTimeNow);
        textTimeAll = (TextView) findViewById(R.id.textSongDuration);
        lrcArtist = (TextView) findViewById(R.id.lrcArtist);
        lrcTitle = (TextView) findViewById(R.id.lrcTitle);
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonLast = (ImageButton) findViewById(R.id.buttonLast);
        buttonNext = (ImageButton) findViewById(R.id.buttonNext);
        bnLrcBack = (Button) findViewById(R.id.bnLrcBack);
        textProgressChange = (TextView) findViewById(R.id.textProgressChange);
        spinner_mode = (Spinner) findViewById(R.id.play_mode);
        lyricView = (LyricView)findViewById(R.id.lrc);
        //用来修改界面
        layoutThis = (RelativeLayout) findViewById(R.id.layout_lrc);

        String songPath = Utils.getList().get(Utils.getCurrentSong()).get("songPath").toString();
        searchLrc(songPath);

        //初始化播放时间以及歌曲长度
        textTimeNow.setText("0:00");
        buttonPlay.setOnClickListener(this);
        buttonLast.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        seekBarTime.setOnSeekBarChangeListener(new seekBarListener());

        //设置歌名可滚动
        lrcArtist.setMovementMethod(ScrollingMovementMethod.getInstance());
        lrcTitle.setMovementMethod(ScrollingMovementMethod.getInstance());

        //添加监听
        bnLrcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TiTa.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    //    加载播放模式设置的的Spinner
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
                Utils.setPlayMode(i);
                String[] mode = new String[]{"顺序播放","随机播放","单曲循环"};
                Toast.makeText(TiTa.this,mode[i],Toast.LENGTH_LONG).show();
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
                intent.putExtra("buttonChoose", Utils.BN_PLAY);
                break;

            case R.id.buttonLast:
                intent.putExtra("buttonChoose", Utils.BN_LAST);
                lyricView.invalidate();
                break;

            case R.id.buttonNext:
                intent.putExtra("buttonChoose", Utils.BN_NEXT);
                lyricView.invalidate();
                break;
        }
        intent.putExtra("progress", seekBarTime.getProgress());
        sendBroadcast(intent);
    }

    //处理从Service传来的广播
    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int freshProgress = intent.getIntExtra("freshProgress", -1);
            //如果傳過來的信息不是要求刷新seekBar
            if (freshProgress == -1) {
                int status = intent.getIntExtra("status", -1);
                int progress = intent.getIntExtra("progress", 0);

                songStatus = status;

                //修改seekBar和歌曲时长
                seekBarTime.setMax(Utils.seekbarMax());
                textTimeAll.setText(Utils.millsToMinute());

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

                //修改標題欄的信息
                String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
                String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
                //获取新的路径
                String songPath = Utils.getList().get(Utils.getCurrentSong()).get("songPath").toString();
                searchLrc(songPath);
                lrcArtist.setText(artist);
                lrcTitle.setText(title);

            } else {
                Log.e("過來的消息",""+freshProgress);
                Log.i("info","freshProgress"+freshProgress);
                Log.i("info","index"+lyricProcess.selectIndex(freshProgress));
                lyricView.setIndex(lyricProcess.selectIndex(freshProgress));
                lyricView.invalidate();
                seekBarTime.setProgress(freshProgress);
            }
        }
    }

    //seekBar事件处理
    private class seekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            //添加判断是因为在点击下一首时会触发这个事件
            // 当选择切歌时，会使这个为0
            if (seekBar.getProgress() == 0) {
                textProgressChange.setText("");
            }
            //如果是由拖动seekBar引起的
            else if (Utils.isTouchSeekBar) {
                textProgressChange.setText(Utils.progressToShow(progress));
            }

            textTimeNow.setText(Utils.progressToShow(progress));
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TiTa.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {

        String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        lrcArtist.setText(artist);
        lrcTitle.setText(title);

        //回鶻seekBar的最大值
        seekBarTime.setMax(Utils.seekbarMax());

        //表示从Service接受到的Broadcast将在ServiceReciver处理
        receiver = new ServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MAIN);
        registerReceiver(receiver, intentFilter);

        //执行换肤
        Drawable drawable = Utils.getDrawableBackground(TiTa.this);
        if (drawable != null) {
            layoutThis.setBackground(drawable);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    /**
     * 将歌曲文件读取进来并进行处理
     */
    public void searchLrc(String songPath){
        //歌词路径
        String lrcPath = songPath;
        lrcPath = lrcPath.substring(0,songPath.length()-4).trim() +  ".lrc";
        lyricProcess.readLRC(lrcPath);
        lyricView.setBlLrc(lyricProcess.getBlLrc());
        List<LyricObject> list = lyricProcess.getLrcList();
        Log.i("info","length"+list.size());
        lyricView.setlrc_list(list);
    }

}
