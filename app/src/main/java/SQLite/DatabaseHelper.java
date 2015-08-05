package SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	private static final int VERSION = 1;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public DatabaseHelper(Context context, String name, int version){
		this(context,name,null,version);
	}
	
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table song(_id integer primary key autoincrement , songId integer , songName varchar(30) , has_mv integer , artistName varchar(30))");
		db.execSQL("create table down_song(_id integer " +
				"primary key autoincrement , songName varchar(30) , artistName varchar(30) ," +
				" akbumName varchar(30) , songPicSmall varchar(100) , songPicBig varchar(100) , " +
				"songPicRadio varchar(100) , lrcLink varchar(100) , songLink varchar(100)," +
				"songSize integer , songDuration integer , songPath varchar(30))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
