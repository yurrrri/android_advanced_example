package ddwucom.mobile.ma02_20161048;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaceDBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "mypick_db";
	public final static String TABLE_NAME = "mypick_table";
	public final static String COL_ID = "_id";
	public final static String COL_LOCATION = "location";
	public final static String COL_CATEGORY = "category";
    public final static String COL_NAME = "name";
    public final static String COL_REVIEW = "review";
    public final static String COL_RATE = "rate";

	public PlaceDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
				+ COL_LOCATION + " TEXT, " + COL_CATEGORY + " TEXT, " + COL_NAME + " TEXT, " + COL_REVIEW + " TEXT, " + COL_RATE +  " TEXT);");
	
//		샘플 데이터
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '서울', '맛집', '배떡', '로제떡볶이 맛집', 4);");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '서울', '맛집', '육회한연어', '연어+육회세트 최고', 4.5);");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '서울', '카페', '라라브레드', '아보카도 새우의 역습 맛있음', 4);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table " + TABLE_NAME);
		onCreate(db);
	}
}
