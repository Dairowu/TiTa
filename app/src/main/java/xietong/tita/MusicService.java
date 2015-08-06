package xietong.tita;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by acer-PC on 2015/8/3.
 */
public class MusicService extends Service {

    List<Map<String, Object>> songList = Utils.getList();
    MediaPlayer mediaPlayer;
    static int lastProgress;
    int currentSong;
    int status = Utils.STOPPING;
    MyServiceReceiver receiver;
    int songNow;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //用来显示通知栏的切歌
    String title;
    String artist;
    boolean isFinish = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        songList = Utils.getList();
        currentSong = Utils.getCurrentSong();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                songNow = 0;
                currentSong = Utils.getNextSong();
                Intent intent = new Intent(Utils.ACTION_TO_MAIN);
                sendBroadcast(intent);
                prepareAndPlay(currentSong);
            }
        });

        new Thread(new MyRunnable()).start();

        Log.e("service", "創建+++++");
        //ע�������������MainActivity����Broadcast
        receiver = new MyServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MUSICSERVICE);
        registerReceiver(receiver, intentFilter);

        //通过SharedPreference来初始化
        sharedPreferences = getSharedPreferences("music_play", MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getInt("finalSong", 0) >= Utils.getList().size()) {
            Utils.setCurrentSong(0);
            lastProgress = 0;
        } else {
            Utils.setCurrentSong(sharedPreferences.getInt("finalSong", 0));
            lastProgress = sharedPreferences.getInt("progress", 0);
        }
        Utils.setPlayMode(sharedPreferences.getInt("playMode", 0));

        MyNotification.showNotifica("尊享属于自己的音乐播放器","",null);


        //打开应用时提醒在哪首歌
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //显示打开应用时处于哪首歌
                title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
                artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
                MyNotification.showNotifica(title, artist, null);
            }
        }).start();

    }

    private void prepareAndPlay(int currentSong) {
        mediaPlayer.reset();

        //显示通知栏
        title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        MyNotification.showNotifica(title, artist, null);
        String path = songList.get(currentSong).get("songPath").toString();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收点击上下首、播放暂停的按钮，以及拖动条的拖动事件
    public class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean b = intent.getBooleanExtra("finish",false);
            if (b){
                Log.e("musicService","停止运行");
                unregisterReceiver(receiver);
                mediaPlayer.stop();
                stopSelf();
            }
            isFinish = b;

            currentSong = Utils.getCurrentSong();
            int progress = intent.getIntExtra("progress", 0);
            int buttonChoose = intent.getIntExtra("buttonChoose", -1);
            switch (buttonChoose) {
                case Utils.BN_PLAY:
                    if (status == Utils.STOPPING) {
                        prepareAndPlay(currentSong);
                        songNow = lastProgress;
                        mediaPlayer.seekTo(lastProgress);
                        status = Utils.PLAYING;
                    } else if (status == Utils.PLAYING) {
                        mediaPlayer.pause();
                        status = Utils.PAUSING;
                    } else if (status == Utils.PAUSING) {
                        mediaPlayer.start();
                        status = Utils.PLAYING;
                    }
                    break;

                case Utils.BN_LAST:
                    songNow = 0;
                    currentSong = Utils.getLastSong();
                    prepareAndPlay(currentSong);
                    status = Utils.PLAYING;
                    progress = 0;
                    break;

                case Utils.BN_NEXT:
                    songNow = 0;
                    currentSong = Utils.getNextSong();
                    prepareAndPlay(currentSong);
                    status = Utils.PLAYING;
                    progress = 0;
                    break;

                case Utils.ITEM:
                    prepareAndPlay(currentSong);
                    songNow = 0;
                    status = Utils.PLAYING;
                    progress = 0;
                    Log.e("MUSIC Service","service item");
                    break;

                case Utils.BN_PROGRESS:
                    mediaPlayer.seekTo(progress);
                    songNow = progress;
                    break;
                default:
                    Log.e("Musicservice", "�˴���������");
            }

            Intent intent1 = new Intent(Utils.ACTION_TO_MAIN);
            intent1.putExtra("progress", progress);
            intent1.putExtra("status", status);
            sendBroadcast(intent1);
        }
    }

    //线程执行
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            Intent intent1 = new Intent(Utils.ACTION_TO_MAIN);
            //歌曲时间单位是秒
            //每0.2秒刷新一次
            int i = 1;
            while (!isFinish) {
                //如果歌曲处于暂停状态，则使线程进入睡眠
                //每0.1秒醒来一次，检查用户是否已经重新开始播放了
                if (status == Utils.PAUSING || status == Utils.STOPPING) {
                    try {
                        Thread.sleep(100);
//                        editor.putInt("playMode", Utils.play_mode);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //如果线程不是处于睡眠状态
                //0.2秒刷新一次，并且每过一秒就使progress前进一次
                else {
                    try {

                        Thread.sleep(480);
//                        if (i == 5) {
                            songNow+=480;
                            Log.e("service線程","後台運行");
                            intent1.putExtra("freshProgress",songNow);
                            sendBroadcast(intent1);
//                            i = 0;

                            //這些是為了使sharedPreference能夠保存最新的信息
                            editor.putInt("finalSong", Utils.getCurrentSong());
                            editor.putInt("playMode", Utils.play_mode);
                            editor.putInt("progress", songNow);
                            editor.commit();
//                        }
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}
