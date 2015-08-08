package DownLoadUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import Dao.Song;
import Dao.SongList;
import JsonParse.JsonParse;

/**
 * Created by Administrator on 2015/8/6.
 */
public class DownPictureOrLrc implements Runnable{

    private  int flag;
    private Context context;
    private String name;
    private Handler handler;
    List<Song> list = null;
    List<SongList> list2 = null;
    JsonParse jsonParse = new JsonParse();
    HttpFileDownloader downloader = new HttpFileDownloader();
    ToastNetState netState;

    public DownPictureOrLrc(int flag,Context context,String name,Handler handler){
        this.flag = flag;
        this.context=context;
        this.name = name;
        this.handler = handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Music";
        netState = new ToastNetState(context);
        if(!netState.isNetAvailable()){
            //给用户提示网络状态
            Toast.makeText(context, "请检查网络设置", Toast.LENGTH_LONG).show();
        }else {
            String sourse = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
                    "from=webapp_music&method=baidu.ting.search.catalogSug" +
                    "&format=json&callback=&query=<" + URLEncoder.encode(name) + ">";
            StringBuffer stringBuffer = downloader.downJson(sourse, context);
            list = jsonParse.parseSongJson(stringBuffer.toString());
            if (list.size() > 0) {
                sourse = "http://music.baidu.com/data/music/links?songIds=" + list.get(0).getSongId();
                stringBuffer = downloader.downJson(sourse, context);
                list2 = jsonParse.parseDownSongJson(stringBuffer.toString());
                if(list2.size()>0) {
                    Message msg = new Message();
                    if (flag == 1) {
                        sourse = "http://musicdata.baidu.com" + list2.get(0).getLrcLink();
                        int i = downloader.downTextFile(sourse, path, name + ".lrc");
                        if (i == 1) {
                            msg.what = 0x111;
                            msg.arg1 = i;
                            msg.obj = name;
                            handler.sendMessage(msg);
                            Toast.makeText(context, "下载成功", Toast.LENGTH_LONG).show();
                        } else if (i == 0) {
                            Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
                        } else {
                            msg.what = 0x111;
                            msg.arg1 = flag;
                            handler.sendMessage(msg);
                            Toast.makeText(context, "文件已存在", Toast.LENGTH_LONG).show();
                        }
                    } else if (flag == 2) {
                        int i = downloader.downBinaryFile(list2.get(0).getSongPicRadio(), path, name + ".jpg", handler);
                        if (i == 1) {
                            msg.what = 0x111;
                            msg.arg1 = flag;
                            msg.obj = name;
                            handler.sendMessage(msg);
                            Toast.makeText(context, "下载成功", Toast.LENGTH_LONG).show();
                        } else if (i == 0) {
                            Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
                        } else {
                            msg.what = 0x111;
                            msg.arg1 = flag;
                            msg.obj = name;
                            handler.sendMessage(msg);
                            Toast.makeText(context, "文件已存在", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Toast.makeText(context, "没有搜索到", Toast.LENGTH_LONG).show();
                }
            }else{
                        Toast.makeText(context, "没有搜索到", Toast.LENGTH_LONG).show();
            }
        }
        Looper.loop();
    }
}
