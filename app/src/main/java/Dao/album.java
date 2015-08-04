package Dao;

public class album {
	
    	 private int albumId;	
    	 private String albumName;
		 private String artistName; 
		 private String artistPic;
		 
		 public int getAlbumId() {
			return albumId;
		}
		public void setAlbumId(int albumId) {
			this.albumId = albumId;
		}
		public String getAlbumName() {
			return albumName;
		}
		public void setAlbumName(String albumName) {
			this.albumName = albumName;
		}
		public String getArtistName() {
			return artistName;
		}
		public void setArtistName(String artistName) {
			this.artistName = artistName;
		}
		public String getArtistPic() {
			return artistPic;
		}
		public void setArtistPic(String artistPic) {
			this.artistPic = artistPic;
		}

}
