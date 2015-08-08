package info;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by CJJ on 2015/8/6.
 */
public class MusicInfo implements Serializable, Parcelable {

//    protected MusicInfo(Parcel in) {
//    }

    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String artist;
    private String album;
    private String path;
    private String duration;
    private String size;
    private String album_img_path;
//    private String lyric_file_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAlbum_img_path() {
        return album_img_path;
    }

    public void setAlbum_img_path(String album_img_path) {
        this.album_img_path = album_img_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.album);
        dest.writeString(this.album_img_path);
        dest.writeString(this.artist);
        dest.writeString(this.path);
        dest.writeString(this.title);
        dest.writeString(this.size);
        dest.writeString(this.duration);
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo music = new MusicInfo();
            music.setTitle(source.readString());
            music.setArtist(source.readString());
            music.setAlbum(source.readString());
            music.setPath(source.readString());
            music.setDuration(source.readString());
            music.setSize(source.readString());
            music.setAlbum_img_path(source.readString());
//            music.setLyric_file_name(source.readString());
            return music;
//            return new MusicInfo(sourse);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };
}
