package downloadmp3;


import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Dao.Song;
import DownLoadUtils.HttpFileDownloader;
import DownLoadUtils.ToastNetState;
import JsonParse.JsonParse;
import xietong.tita.FinishApp;
import xietong.tita.MainActivity;
import xietong.tita.R;
import xietong.tita.Utils;

/**
 * �下载显示界面。
 * @author Administrator
 *
 */

public class DownloadMp3 extends ListActivity implements OnClickListener{

	Button bt_serch;//搜索按钮
	EditText editText;//输入框
	Button bt_back;
	TextView tv ;
	ProgressBar progressBar;//加载时显示的进度条����
	List<Song> list = null;
	JsonParse jsonParse = new JsonParse();
	HttpFileDownloader downloader = new HttpFileDownloader();
	ToastNetState netState;
	ListView  listView;
	DownTask task;
	RelativeLayout layoutThis;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_mp3);
		FinishApp.addActivity(this);

		bt_serch = (Button) findViewById(R.id.button_search);
		bt_back = (Button)findViewById(R.id.button_back);
        editText = (EditText) findViewById(R.id.et_search);
		layoutThis = (RelativeLayout) findViewById(R.id.layout_search_song);
        tv = (TextView) findViewById(R.id.tv);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = getListView();
        Context context = getApplicationContext();
		netState = new ToastNetState(DownloadMp3.this);

		bt_serch.setOnClickListener(this);
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DownloadMp3.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});
    }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(DownloadMp3.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	protected void onPause(){
		super.onPause();
		if(task!=null&&task.getStatus()==AsyncTask.Status.RUNNING){
			task.cancel(true);
		}
	}

	@Override
	public void onClick(View v) {
		
		String query = editText.getText().toString();
	    
	    if(!netState.isNetAvailable()){
	    	//给用户提示网络状态
	    	Toast.makeText(DownloadMp3.this, "请检查网络设置", Toast.LENGTH_LONG).show();
	    }else{
	    	if(query.equals("")){
	    		Toast.makeText(DownloadMp3.this, "您还未输入要搜索的歌名", Toast.LENGTH_LONG).show();
	    	}
	    	else{
	    		String sourse = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
					"from=webapp_music&method=baidu.ting.search.catalogSug" +
					"&format=json&callback=&query=<"+URLEncoder.encode(query)+">";
	    		task = new DownTask(DownloadMp3.this);
	    		task.execute(sourse);
			}
	    }
	}
	
	//利用异步操作得到第一次的解析后的json数据，并以列表的形式显示
	class DownTask extends AsyncTask<String,Void,List<Song>>{

		Context mContext;
		public DownTask (Context ctx){
			mContext = ctx;
		}
		
		@Override
		protected List<Song> doInBackground(String... params) {

			if(!task.isCancelled()) {
				StringBuffer stringBuffer = downloader.downJson(params[0], mContext);

				list = jsonParse.parseSongJson(stringBuffer.toString());
			}
				
			return list;
		}
		
		@Override
		protected void onPostExecute(List<Song> result) {
			tv.setText("");
			progressBar.setVisibility(View.GONE);
			List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
			for(int i = 0;i < result.size();i++){
				if(task.isCancelled()){
					break;
				}
				Map<String,Object> listItem = new HashMap<String,Object>();
				listItem.put("id", i+1);
				listItem.put("songName", result.get(i).getSongName());
				listItem.put("artistName", result.get(i).getArtistName());
				listItems.add(listItem);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(DownloadMp3.this,listItems,R.layout.songitems,
					new String[]{"id","songName","artistName"},
					new int[]{R.id.tv_lineNumber,R.id.tv_songName,R.id.tv_artistName});
			setListAdapter(simpleAdapter);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(task.isCancelled()){
				tv.setText("");
				progressBar.setVisibility(View.GONE);
			}
			tv.setText("加载中");
			progressBar.setVisibility(View.VISIBLE);
		}
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(DownloadMp3.this,DownloadService.class);
		intent.putExtra("songId", list.get(position).getSongId());
		startService(intent);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onResume() {
		//换肤
		Drawable drawable = Utils.getDrawableBackground(DownloadMp3.this);
		if (drawable != null) {
			layoutThis.setBackground(drawable);
		}
		super.onResume();
	}
}
