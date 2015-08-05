package JsonParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Dao.Song;
import Dao.SongList;


/**
 *  解析json数据�
 * @author Administrator
 *
 */

public class JsonParse {

	private String json1;
	private String json2;	
	
	/**
	 * 第一次解析主要是为了得到songId,可以用其访问到歌曲、歌词、图片的下载
	 * @return 返回一个包含几条歌曲信息的列表
	 */
	public List<Song> parseSongJson(String json1){
		
		List<Song>  songData = new ArrayList<Song>();
		
		try {
			JSONObject object = new JSONObject(json1);
			
			JSONArray songs = object.getJSONArray("song");
			
			for(int i = 0;i<songs.length();i++){
				
				Song songObject = new Song();
				JSONObject song = songs.getJSONObject(i);
				
				int songId = song.getInt("songid");
				String songName = song.getString("songname");
				int has_mv = song.getInt("has_mv");
				String artistName = song.getString("artistname");
				
				songObject.setSongId(songId);
				songObject.setSongName(songName);
				songObject.setHas_mv(has_mv);
				songObject.setArtistName(artistName);
				
				songData.add(songObject);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return songData;
	}
	
	/**
	 * 第二次解析可以得到歌曲、歌词、图片下载的URL
	 * @return 包含每首歌曲各种资源下载的URL列表
	 */
	
	public List<SongList> parseDownSongJson(String json2){
		
		List<SongList>  songLists= new ArrayList<SongList>();
		
		try {
			JSONObject object = new JSONObject(json2);
			JSONObject data = object.getJSONObject("data");
			JSONArray songList= data.getJSONArray("songList");
			
			for(int i = 0;i<songList.length();i++){
				
				SongList songObject = new SongList();
				JSONObject song = songList.getJSONObject(i);
				
				String songName = song.getString("songName");
				String artistName = song.getString("artistName");
				String albumName = song.getString("albumName");
				String songPicSmall = song.getString("songPicSmall");
				String songPicBig = song.getString("songPicBig");
				String songPicRadio = song.getString("songPicRadio");
				String lrcLink = song.getString("lrcLink");
				String songLink = song.getString("songLink");
				int time = song.getInt("time");
				int size = song.getInt("size");
				
				songObject.setSongName(songName);
				songObject.setArtistName(artistName);
				songObject.setAlbumName(albumName);
				songObject.setSongPicSmall(songPicSmall);
				songObject.setSongPicBig(songPicBig);
				songObject.setSongPicRadio(songPicRadio);
				songObject.setLrcLink(lrcLink);
				songObject.setSongLink(songLink);
				songObject.setTime(time);
				songObject.setSize(size);
				
				songLists.add(songObject);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return songLists;
	}
	
}
