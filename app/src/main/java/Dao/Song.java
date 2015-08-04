package Dao;

public class Song {

	private int songId;
	private String songName;
	private int has_mv;
	private String artistName;
	
	public int getSongId() {
		return songId;
	}
	public void setSongId(int songId) {
		this.songId = songId;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public int getHas_mv() {
		return has_mv;
	}
	public void setHas_mv(int has_mv) {
		this.has_mv = has_mv;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

}
