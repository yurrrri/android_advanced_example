package mobile.example.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDBHelper extends SQLiteOpenHelper {

	private final String TAG = "ContactDBHelper";

//	DB 이름
	private final static String DB_NAME = "contact_db";
	//	외부에서 사용할 테이블 명, 컬럼명 선언
	public final static String TABLE_NAME = "contact_table";
	public final static String COL_NAME = "name";
	public final static String COL_PHONE = "phone";
	public final static String COL_CAT = "category";

//	생성자 : DB 파일 명 및 DB 버전(DB를 처음 생성할 경우 1) 지정
	public ContactDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

//	테이블 생성 => 앱을 처음 설치해서, 처음 db 호출시(GetReadable, GetWritable 메소드 호출시) 이후에는 호출되지 않는다 !!!
//	따라서 테이블 구조를 바꿔야할 필요가 있는 경우에 앱을 기기에서 제거후 재실행할 필요가 있다.
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createSql = "create table " + TABLE_NAME+"(_id integer primary key autoincrement, "+COL_NAME+" TEXT, "+COL_PHONE+" TEXT, " + COL_CAT+" TEXT);";
		Log.d(TAG, createSql);
		db.execSQL(createSql);

		//필요하다면 여기에 샘플 추가
		db.execSQL("insert into " + TABLE_NAME + " values (null, '이유리', '2570', '친구');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '정수길', '8230', '친구');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '이명선', '3429', '가족');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '이승환', '8293', '가족');");

	}

//	테이블 구조를 변경할 경우에 -> 비워놔도 무방
//	기존 테이블을 삭제하고 테이블을 다시 생성함(onCreate(db) 호출))
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}