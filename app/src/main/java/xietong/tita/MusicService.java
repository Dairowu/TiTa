package xietong.tita;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by acer-PC on 2015/8/3.
 */
public class MusicService extends Service{

    List<Map<String, Object>> songList = Utils.getList();
    MediaPlayer mediaPlayer;
    int currentSong;
    List<Map<String, Object>> songLists;
    int status = Utils.STOPPING;
    MyServiceReceiver receiver;

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

                currentSong = Utils.getNextSong();
                Intent intent = new Intent(Utils.ACTION_TO_MAIN);
                sendBroadcast(intent);
                prepareAndPlay(currentSong);
            }
        });

        //ע�������������MainActivity����Broadcast
        receiver = new MyServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MUSICSERVICE);
        registerReceiver(receiver, intentFilter);

    }

    private void prepareAndPlay(int currentSong) {
        mediaPlayer.reset();
        String path = songList.get(currentSong).get("songPath").toString();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //������ͣ������ȸ����¼�
    public class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            currentSong = Utils.getCurrentSong();
            int progress = intent.getIntExtra("progress",0);
            int buttonChoose = intent.getIntExtra("buttonChoose", -1);
            switch (buttonChoose) {
                case Utils.BN_PLAY:
                    if (status == Utils.STOPPING) {
                        prepareAndPlay(currentSong);
                        mediaPlayer.seekTo(progress);
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
                    currentSong = Utils.getLastSong();
                    prepareAndPlay(currentSong);
                    status = Utils.PLAYING;
                    progress = 0;
                    break;

                case Utils.BN_NEXT:
                    currentSong = Utils.getNextSong();
                    prepareAndPlay(currentSong);
                    status = Utils.PLAYING;
                    progress = 0;
                    break;

                case Utils.ITEM:
                    prepareAndPlay(currentSong);
                    status = Utils.PLAYING;
                    progress = 0;
                    break;

                case Utils.BN_PROGRESS:
                    mediaPlayer.seekTo(progress);
                    status = Utils.PLAYING;
                    break;
                default:
                    Log.e("Musicservice", "�˴���������");
            }

            Intent intent1 = new Intent(Utils.ACTION_TO_MAIN);
            intent1.putExtra("progress",progress);
            intent1.putExtra("status", status);
            sendBroadcast(intent1);
        }
    }

}
