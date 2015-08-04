package Lrc;

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
	private LyricObject lyricObject;

	public void readLRC(String path){
		File f = new File(path.replace(".mp3", ".lrc"));

		try {
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
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LyricObject oldval  = null;
		for(int i = 0;i<lrcList.size();i++){
			LyricObject val = lrcList.get(i);
			if(oldval==null){
				oldval=val;
			}
			else{
				LyricObject item1= new LyricObject();
				item1  = oldval;
				item1.timeline = val.begintime-oldval.begintime;
				lrcList.add(item1);
				i++;
				oldval = val;
			}
		}

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

	public List<LyricObject> getLrcList() {
		return lrcList;
	}

}

