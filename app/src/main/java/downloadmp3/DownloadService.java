package downloadmp3;


import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import Dao.SongList;
import JsonParse.JsonParse;
import DownLoadUtils.HttpFileDownloader;
import SQLite.DatabaseHelper;

/**
 * 从服务端第二次得到json数据并解析���ݲ�����
 * @author Administrator
 *
 */
public class DownloadService extends IntentService{

	static int count = 0;
	static int successAmount = 0;
	static int faiureAmount = 0;
	
	List<SongList> list = null;
	HttpFileDownloader downloader = new HttpFileDownloader();
	NotificationManager nm;
	
	public DownloadService() {
		super("DownloadService");
	}
	
	private Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			
		}
		
	};
	
	/**
	 * list存放了每首歌解析之后的数据������
	 */

	@Override
	protected void onHandleIntent(Intent intent) {
		
		DatabaseHelper databaseHelper = new DatabaseHelper(this,"songDown");
		
		JsonParse jsonParse = new JsonParse();
		int songId = intent.getIntExtra("songId", 0);
		String sourse = "http://music.baidu.com/data/music/links?songIds=" + songId;
		
		StringBuffer stringBuffer = downloader.downJson(sourse, getApplicationContext());
		
		list = jsonParse.parseDownSongJson(stringBuffer.toString());
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Music";
		ContentValues values = new ContentValues();
		values.put("songPath", path);
		values.put("songName",list.get(0).getSongName());
		values.put("artistName",list.get(0).getArtistName());
		databaseHelper.getWritableDatabase().insert("down_song", null, values);
		
		DownloadRunnable downloadRunnable = new DownloadRunnable(list,path,handler);
		Thread thread = new Thread(downloadRunnable);
		thread.start();
		
		Message msg = new Message();
		msg.what = 0x111;
		handler.sendMessage(msg);
		
		}
	
	class DownloadRunnable implements Runnable{

		private List<SongList> list;
		private String path;
		private Handler handler;
		
		public DownloadRunnable(List<SongList> list,String path,Handler handler){
			this.list = list;
			this.path = path;
			this.handler = handler;
		}
		
		@Override
		public void run() {
			Looper.prepare();
			Message msg = new Message();
			int flag = downloader.downBinaryFile(list.get(0).getSongLink(), path,list.get(0).getSongName()+".mp3",handler);
			if(flag==1){
				msg.what = 0x100;
				msg.arg1 = 1;
				handler.sendMessage(msg);
			Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_LONG).show();
			}else if(flag==0){
				msg.what = 0x000;
				msg.arg1 = 1;
				handler.sendMessage(msg);
				Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_LONG).show();
			}else{
				msg.what = 0x010;
				msg.arg1 = 1;
				handler.sendMessage(msg);
				Toast.makeText(DownloadService.this, "文件已存在", Toast.LENGTH_LONG).show();
			}
			Looper.loop();
		}
	}
}



