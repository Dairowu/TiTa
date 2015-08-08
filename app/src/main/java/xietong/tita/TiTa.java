package xietong.tita;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DownLoadUtils.DownPictureOrLrc;
import Lrc.LyricObject;
import Lrc.LyricProcess;
import Lrc.LyricView;
import Picture.ProcessPicture;

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
    //用来标示是下载图片还是歌词
    int DOWNLRC  = 1;
    int DOWNPICTURE = 2;
    //自定义显示歌词的view
    private LyricView lyricView;
    //用来对歌词文件进行处理
    private LyricProcess lyricProcess = new LyricProcess();
    //泡泡窗弹出后供用户选择下载图片和歌词
    private PopupWindow popupWindow = null;
    //下载图片或者歌词的工具类
    DownPictureOrLrc downPictureOrLrc;
    ProcessPicture processPicture = new ProcessPicture();
    Bitmap bitmap;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Music";

    ImageButton buttonPlay, buttonNext, buttonLast;
    Button bnLrcBack, bnLrcShare;

    TextView lrcTitle, lrcArtist;
    //根布局
    RelativeLayout layoutThis;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

   public Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            if(msg.what==0x111){
                if(msg.arg1==DOWNLRC){
                    searchLrc((String) msg.obj);
                    lyricView.invalidate();
                }else if(msg.arg1==DOWNPICTURE){
                    layoutThis.invalidate();
                    changeBackground((String) msg.obj);
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lrc);
        FinishApp.addActivity(TiTa.this);

        init();

//        //启动后台播放音乐的Service
//        Intent intent = new Intent(this, MusicService.class);
//        startService(intent);
        //显示当前歌曲的总时长
        textTimeAll.setText(Utils.millsToMinute());
        //加载播放模式的Spinner
        loadPlayMode();
        spinner_mode.setSelection(Utils.play_mode);
    }

    //初始化按鈕并添加監聽
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
        bnLrcShare = (Button) findViewById(R.id.bn_lrc_share);
        textProgressChange = (TextView) findViewById(R.id.textProgressChange);
        spinner_mode = (Spinner) findViewById(R.id.play_mode);
        lyricView = (LyricView) findViewById(R.id.lrc);
        //用来修改界面
        layoutThis = (RelativeLayout) findViewById(R.id.layout_lrc);

//        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
//        changeBackground(title);
//        searchLrc(title);

        //初始化播放时间以及歌曲长度
        textTimeNow.setText("0:00");
        buttonPlay.setOnClickListener(this);
        buttonLast.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        bnLrcShare.setOnClickListener(this);
        seekBarTime.setOnSeekBarChangeListener(new seekBarListener());

        //设置歌名可滚动
        lrcArtist.setMovementMethod(ScrollingMovementMethod.getInstance());
        lrcTitle.setMovementMethod(ScrollingMovementMethod.getInstance());

        //添加监听
        bnLrcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TiTa.this, MainActivity.class);
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
                String[] mode = new String[]{"顺序播放", "随机播放", "单曲循环"};
                Toast.makeText(TiTa.this, mode[i], Toast.LENGTH_LONG).show();
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
                //修改播放按钮的显示
                if (Utils.getStatus() != Utils.PLAYING) {
                    buttonPlay.setSelected(true);
                } else {
                    buttonPlay.setSelected(false);
                }
                intent.putExtra("buttonChoose", Utils.BN_PLAY);
                break;

            case R.id.buttonLast:
                intent.putExtra("buttonChoose", Utils.BN_LAST);
                break;

            case R.id.buttonNext:
                intent.putExtra("buttonChoose", Utils.BN_NEXT);
                break;
            //点击分享
            case R.id.bn_lrc_share:
                String share = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString()+"-"
                        +Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
                Intent intentToShare = new Intent();
                intentToShare.setAction(Intent.ACTION_SEND);
                intentToShare.setType("text/*");
                intentToShare.putExtra(Intent.EXTRA_TEXT, "我正在使用TiTa听 " + share);
                startActivity(Intent.createChooser(intentToShare, "分享你正在听的歌曲"));
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

                lyricView.setHandler(handler);
                lyricView.setTile(title);
                lyricView.setOnClickListener(lyricView);

                searchLrc(title);
                lyricView.invalidate();
                changeBackground(title);
                layoutThis.invalidate();
                lrcArtist.setText(artist);
                lrcTitle.setText(title);

                //修改播放按钮的显示
                if (Utils.getStatus() != Utils.PLAYING) {
                    buttonPlay.setSelected(false);
                } else {
                    buttonPlay.setSelected(true);
                }

            } else {
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
        Intent intent = new Intent(TiTa.this, MainActivity.class);
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
        searchLrc(title);
        lyricView.invalidate();
        changeBackground(title);
        layoutThis.invalidate();

        //修改播放按钮的显示
        if (Utils.getStatus() != Utils.PLAYING) {
            buttonPlay.setSelected(false);
        } else {
            buttonPlay.setSelected(true);
        }

        //获取系统屏幕常亮的权限
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TiTa");
//        在释放之前，屏幕一直亮着
        boolean isCheck = getSharedPreferences("music_play", MODE_WORLD_READABLE).getBoolean("isCheck", true);
        if (isCheck) {
            wakeLock.acquire();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        //使屏幕恢复正常显示时间
        boolean isCheck = getSharedPreferences("music_play", MODE_WORLD_READABLE).getBoolean("isCheck", true);
        if (isCheck) {
            wakeLock.release();
        }
        super.onPause();
    }

    /**
     * 将歌曲文件读取进来并进行处理
     */
    public void searchLrc(String title){
        //歌词路径
        String lrcPath = path + File.separator+title +  ".lrc";
        lyricProcess.readLRC(lrcPath);
        lyricView.setBlLrc(lyricProcess.getBlLrc());
        List<LyricObject> list = lyricProcess.getLrcList();
        lyricView.setlrc_list(list);
        lyricView.invalidate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Drawable searchPicture(String title){
            String picturePath = path +File.separator+ title +  ".jpg";
            processPicture.readPciture(picturePath);
            bitmap = processPicture.getBitmap();
            if(bitmap!=null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                return drawable;
            }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void changeBackground(String title){
        //执行换肤
        Drawable drawable = Utils.getDrawableBackground(TiTa.this);
        Drawable artistBitmap = searchPicture(title);
        Log.i("info","drawable"+drawable.toString());
        Log.i("info","drawable"+drawable.toString());
//        Log.i("info","artistBitmap"+artistBitmap.toString());
        if (artistBitmap!=null){
            layoutThis.setBackground(artistBitmap);
            Log.e("TiTa", "artistBitmap");
        }
        else  {
            Log.e("TiTa","drawable");
            layoutThis.setBackground(drawable);
        }
    }

}
