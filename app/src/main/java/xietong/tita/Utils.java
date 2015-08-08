package xietong.tita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by acer-PC on 2015/8/3.
 */
public class Utils {

    public static final String TAG = "Tita";
    private static List<Map<String, Object>> songList = new ArrayList<Map<String, Object>>();

    //当前是第几首歌
    private static int currentSong = 0;

    //用来获取当前处于的列表
    private static MyBaseAdapter baseAdapter;

    //当前播放模式
    //0为顺序播放，1为随机播放，2为单曲循环
    public static int play_mode;

    //主Activity的Receiver接收该Action
    public final static String ACTION_TO_MAIN = "com.example.broadcast.change_title";
    //MusicService里面的Reciver接收的Action
    public final static String ACTION_TO_MUSICSERVICE = "com.example.broadcast.change_song";

    //用来标志点击的按钮
    public final static int BN_PROGRESS = 0x10;//点击seeBar时发生
    public final static int ITEM = 0x11;//点击歌曲时发生
    public final static int BN_PLAY = 0x12;
    public final static int BN_NEXT = 0x13;
    public final static int BN_LAST = 0x14;

    //用来标志歌曲播放的状态
    public final static int STOPPING = 0x15;//一开始进入应用时处于的状态
    public final static int PAUSING = 0x16;
    public final static int PLAYING = 0x20;
    public static int status = STOPPING;

    //判断是否是由拖动seekBar引起
    public static boolean isTouchSeekBar = false;

    //标志Notification发送的广播
    public final static String NOTIFICATION_ITEM_BUTTON_LAST
            = "com.example.notification.NotifyReceiver.last";//----通知栏上一首按钮
    public final static String NOTIFICATION_ITEM_BUTTON_PLAY
            = "com.example.notification.NotifyReceiver.play";//----通知栏播放按钮
    public final static String NOTIFICATION_ITEM_BUTTON_NEXT
            = "com.example.notification.NotifyReceiver.next";//----通知栏下一首按钮

    List<Map<String,MyBaseAdapter>> list = new ArrayList<Map<String, MyBaseAdapter>>();

    //返回歌单
    public static List<Map<String, Object>> getList() {
        return songList;
    }

    public static int getCurrentSong() {
        return currentSong;
    }

    public static void setCurrentSong(int nowSong) {
        currentSong = nowSong;
    }

    //用来把毫秒转化成分，显示在歌曲时长里
    public static String millsToMinute() {
        int minute = 0;
        int second = 0;
        String mills;
        mills = songList.get(currentSong).get("songDuration").toString();
        second = Integer.parseInt(mills) / 1000;
        minute = second / 60;
        second %= 60;
        if (second > 9)
            return minute + ":" + second;
        else
            return minute + ":0" + second;
    }

    /**
     * 歌曲信息的转化为“--：--”时钟格式的方法
     * @param duration
     * @return
     */
    public static String millsToMinute(String duration) {
        int minute = 0;
        int second = 0;
        second = Integer.parseInt(duration) / 1000;
        minute = second / 60;
        second %= 60;
        if (second > 9)
            return minute + ":" + second;
        else
            return minute + ":0" + second;
    }
    //用来设置seekBar的最大值
    public static int seekbarMax() {
        int mill = 0;
        String mills;
        mills = songList.get(currentSong).get("songDuration").toString();
        mill = Integer.parseInt(mills);
        return mill;
    }

    //当seekBar拖动时，显示的时间
    public static String progressToShow(int progress) {
        int minute = 0;
        int second = 0;
        second = progress/1000;
        minute = second / 60;
        second %= 60;
        if (second < 10) {
            return minute + ":0" + second;
        } else return minute + ":" + second;
    }

    public static void setPlayMode(int i) {

        switch (i) {
            case 0:
                //顺序播放
                play_mode = 0;
                break;
            case 1:
                //随机播放
                play_mode = 1;
                break;
            case 2:
                //单曲循环
                play_mode = 2;
        }
    }

    public static int getNextSong() {
        switch (play_mode) {
            case 0:
                if (currentSong == songList.size() - 1) {
                    currentSong = 0;
                } else currentSong++;
                break;
            case 1:
                Random random = new Random();
                currentSong = random.nextInt(songList.size() - 1);
                break;
            case 2:
                break;
        }
        return currentSong;
    }

    public static int getLastSong() {
        switch (play_mode) {
            case 0:
                if (currentSong == 0) {
                    currentSong = songList.size() - 1;
                } else currentSong--;
                break;
            case 1:
                Random random = new Random();
                currentSong = random.nextInt(songList.size() - 1);
                break;
            case 2:
                break;
        }
        return currentSong;
    }

    public static void loadFromSD(Context context) {
        //查询SD卡上的所有音乐
        Cursor cursor;
        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.AudioColumns.TITLE);
        //为了把音乐加载到SongList，这样可以在Service处直接获得音乐列表

        //执行加入歌曲
        while (cursor.moveToNext()) {


            String songTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
            String songArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String songAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
            String songDuration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
            String songDisplay = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
            String songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            String songSize = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE));

            //用来显示在列表界面的歌曲名称
            String songShow = "";
            if(songDisplay!=null) {
                songShow = songDisplay.substring(0, songDisplay.indexOf("."));
            };
            Map<String, Object> song = new HashMap<String, Object>();
            song.put("songTitle", songTitle);
            song.put("songArtist", songArtist);
            song.put("songAlbum", songAlbum);
            song.put("songDuration", songDuration);
            song.put("songDisplay", songDisplay);
            song.put("songPath", songPath);
            song.put("songSize", songSize);
            song.put("songShow", songShow);
            songList.add(song);
        }
        cursor.close();
    }

    /**
     *
     * @param whichBn
     *        判断是谁发出来的操作，包括上下按钮，暂停键
     */
    public static void bnSendBroadcast(Context context, int whichBn){

        Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);

        switch (whichBn){
            //点击上一首
            case BN_LAST:intent.putExtra("buttonChoose", Utils.BN_LAST);
                break;
            case BN_NEXT:intent.putExtra("buttonChoose", Utils.BN_NEXT);
                break;
            case BN_PLAY:intent.putExtra("buttonChoose", Utils.BN_PLAY);
                break;

        }
        context.sendBroadcast(intent);
    }

    public static MyBaseAdapter getAdapter(){
        return baseAdapter;
    }

    public static void setAdapter(MyBaseAdapter adapter){
        baseAdapter = adapter;
    }

    public static Drawable getDrawableBackground(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_play", context.MODE_WORLD_READABLE);
        String background = sharedPreferences.getString("background", "");
        if (background != "") {
            Uri uri = Uri.parse(background);
            Drawable drawable = null;
            try {
                drawable = Drawable.createFromStream(context.getContentResolver().openInputStream(uri),null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return drawable;
        }
        return null;
    }

    public static int getStatus(){
        return status;
    }

    public static void setStatus(int status1){
        status = status1;
    }

}
