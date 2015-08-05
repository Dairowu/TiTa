package Lrc;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LyricProcess {

	private List<LyricObject> lrcList = new ArrayList<LyricObject>();
	private LyricObject lyricObject = new LyricObject();
	//用来判断该路径下是否是文件
	private boolean blLrc = false;
	//歌词高亮显示的下标
	private int lrcIndex;
	public void readLRC(String path){

		try {
			File f = new File(path);
			if(!f.isFile()){
				blLrc=false;
				return;
			}
			blLrc = true;
			//创建一个文件输入流对象
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while((s = br.readLine()) != null) {
				//替换字符
				s = s.replace("[", "");
				s = s.replace("]", "@");

				//分离“@”字符
				String splitLrcData[] = s.split("@");

				if(splitLrcData.length > 1) {
					lyricObject.lrcline=splitLrcData[1];
					//处理歌词取得歌曲的时间
					int lrcTime = time2Str(splitLrcData[0]);
					lyricObject.begintime = lrcTime;
					//添加进列表数组
					lrcList.add(lyricObject);
					//新创建歌词内容对象
					lyricObject = new LyricObject();
				}else {
					lyricObject.lrcline="";
					//处理歌词取得歌曲的时间
					int lrcTime = time2Str(splitLrcData[0]);
					lyricObject.begintime = lrcTime;
					//添加进列表数组
					lrcList.add(lyricObject);
					//新创建歌词内容对象
					lyricObject = new LyricObject();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("info", "length" + lrcList.size());
		LyricObject oldval  = null;
		for(int i = 0;i<lrcList.size();i++){
			LyricObject val = lrcList.get(i);
			if(oldval==null){
				oldval=val;
			}
			else{
				int timeline ;
				timeline = val.begintime-oldval.begintime;
				oldval.timeline = timeline;
				oldval = val;
			}
		}
		Log.i("info", "length" + lrcList.size());
	}

	public int time2Str(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");

		String timeData[] = timeStr.split("@"); //将时间分隔成字符串数组

		//分离出分、秒并转换为整型
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		//计算上一行与下一行的时间转换为毫秒数
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public int selectIndex(int time){
		if(!blLrc){
			return 0;
		}
		int index = 0;
		for (int i = 0;i<lrcList.size();i++){
			LyricObject temp = lrcList.get(i);
			if(temp.begintime<time&&(temp.timeline+temp.begintime)>time){
				index=i;
				break;
			}
		}

		if(index<0){
			return 0;
		}
		return index;
	}

	public List<LyricObject> getLrcList() {
		return lrcList;
	}

	public boolean getBlLrc(){return blLrc;}
}

