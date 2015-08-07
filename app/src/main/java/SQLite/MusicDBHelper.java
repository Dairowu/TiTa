package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import constant.DbFinal;
import info.MusicInfo;
import xietong.tita.Utils;

/**
 * Created by CJJ on 2015/8/6.
 */
public class MusicDBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db = this.getWritableDatabase();

    public MusicDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建本地曲库表，我喜欢，专辑，歌手
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbFinal.TABLE_LOCALMUSIC
                + " (" + DbFinal.LOCAL_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + DbFinal.LOCAL_TITLE
                + " TEXT UNIQUE NOT NULL," + DbFinal.LOCAL_ARTIST
                + " TEXT," + DbFinal.LOCAL_ALBUM
                + " TEXT," + DbFinal.LOCAL_PATH
                + " TEXT NOT NULL," + DbFinal.LOCAL_DURATION
                + " LONG NOT NULL," + DbFinal.LOCAL_FILE_SIZE
                + " LONG NOT NULL," + DbFinal.LOCAL_LRC_TITLE
                + " TEXT" + DbFinal.LOCAL_LRC_PATH
                + " TEXT" + DbFinal.LOCAL_ALBUM_IMG_TITLE
                + " TEXT" + DbFinal.LOCAL_ALBUM_IMG_PATH
                + " TEXT" + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbFinal.TABLE_FAVORITES
                + " (" + DbFinal.FAVORITES_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + DbFinal.FAVORITES_LOCAL_ID
                + " INTEGER UNIQUE NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbFinal.TABLE_LOCALMUSIC);
        db.execSQL("DROP TABLE IF EXISTS " + DbFinal.TABLE_FAVORITES);
        onCreate(db);
    }

    /**
     * charu 插入本地音乐列表
     *
     * @param music
     * @return
     */
    public Long insertLocal(MusicInfo music) {
        ContentValues values = new ContentValues();
        values.put(DbFinal.LOCAL_TITLE, music.getTitle());
        values.put(DbFinal.LOCAL_ARTIST, music.getArtist());
        values.put(DbFinal.LOCAL_ALBUM, music.getAlbum());
        values.put(DbFinal.LOCAL_PATH, music.getPath());
        values.put(DbFinal.LOCAL_DURATION, music.getDuration());
        values.put(DbFinal.LOCAL_FILE_SIZE, music.getSize());
//        values.put(DbFinal.LOCAL_LRC_TITLE, music.getLyric_file_name());
        Long i = db.insert(DbFinal.TABLE_LOCALMUSIC, null, values);
        return i;
    }

    /**
     * 插入“我喜欢”列表
     *
     * @param music
     * @return
     */
    public Long insertFav(MusicInfo music) {
        ContentValues values = new ContentValues();
        values.put(DbFinal.FAVORITES_LOCAL_ID, music.getId());
        Long i = db.insert(DbFinal.TABLE_FAVORITES, null, values);
        return i;
    }


    /**
     * 查询“我喜欢”数据库
     *
     * @return Cursor 查询“我喜欢”数据库返回
     */
    public Cursor queryFavByID() {
        Cursor cur = db.query(DbFinal.TABLE_FAVORITES, null, null, null, null,
                null, DbFinal.LOCAL_ID + " asc");
        return cur;
    }

    /**
     * 根据“我喜欢”查到的id查询本地数据库，得到歌曲信息
     *
     * @return Cursor 查询本地数据库返回
     */
    public Cursor queryFavFromLocal() {
        Cursor idCursor = db.query(DbFinal.TABLE_FAVORITES, null, null, null,
                null, null, DbFinal.LOCAL_ID + " asc");
        String selection = DbFinal.LOCAL_ID + "=?";
        String selectionArgs[] = new String[idCursor.getCount()];
        if (idCursor.getCount() != 0) {
            idCursor.moveToFirst();
            Log.e(Utils.TAG, "查询到的总数" + idCursor.getCount() + "数组长度"
                    + selectionArgs.length);
            int i = 0;
            do {
                selectionArgs[i] = String.valueOf(idCursor.getInt(idCursor
                        .getColumnIndex(DbFinal.FAVORITES_LOCAL_ID)));
                Log.e(Utils.TAG, i + "========" + selectionArgs[i]);
                i++;
                idCursor.moveToNext();
            } while (!idCursor.isAfterLast());
        }
        Cursor cur = db.query(DbFinal.TABLE_LOCALMUSIC, null, selection,
                selectionArgs, null, null, DbFinal.LOCAL_ID + " asc");
        return cur;
    }

    /**
     * 根据查询本地数据库得到的cursor得到歌曲信息集合
     *
     * @param curLocal 本地数据库查询到的cursor
     * @return ArrayList<MusicInfo> 歌曲实体类集合
     */
    public ArrayList<MusicInfo> getMusicListFromLocal(Cursor curLocal) {

        if (curLocal.getCount() != 0) {
            curLocal.moveToFirst();
            ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
            do {
                MusicInfo music = new MusicInfo();
                music.setTitle(curLocal.getString(curLocal
                        .getColumnIndex(DbFinal.LOCAL_TITLE)));
                music.setArtist(curLocal.getString(curLocal
                        .getColumnIndex(DbFinal.LOCAL_ARTIST)));
                music.setAlbum(curLocal.getString(curLocal
                        .getColumnIndex(DbFinal.LOCAL_ALBUM)));
                music.setPath(curLocal.getString(curLocal
                        .getColumnIndex(DbFinal.LOCAL_PATH)));
                music.setDuration(curLocal.getLong(curLocal
                        .getColumnIndex(DbFinal.LOCAL_DURATION)));
                music.setSize(curLocal.getLong(curLocal
                        .getColumnIndex(DbFinal.LOCAL_FILE_SIZE)));
//                music.setLyric_file_name(curLocal.getString(curLocal
//                        .getColumnIndex(DbFinal.LOCAL_LRC_TITLE)));
                musicList.add(music);
                curLocal.moveToNext();
            } while (!curLocal.isAfterLast());
            return musicList;
        }
        return null;

    }

    /**
     * 删除本地数据库相应歌曲条目
     *
     * @param title
     * @return
     */
    public int delLocal(String title) {
        int i = db.delete(DbFinal.TABLE_LOCALMUSIC, DbFinal.LOCAL_TITLE + "=?",
                new String[]{title});
        return i;
    }

    /**
     * 删除“我喜欢”列表中相应歌曲条目
     *
     * @param id
     * @return
     */
    public int delFav(int id) {
        int i = db.delete(DbFinal.TABLE_FAVORITES, DbFinal.FAVORITES_ID + "=?",
                new String[]{id + ""});
        return i;
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }

        super.close();
    }
}
