package mobile.database.dbtest02;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AllContactsActivity extends AppCompatActivity {
	final int UPDATE_CODE = 200;
	boolean update_flag = TRUE;

	ListView lvContacts = null;
	ContactDBHelper helper;
	Cursor cursor;
	MyCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ContactDBHelper(this);

//		  SimpleCursorAdapter 객체 생성
//		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[]{ContactDBHelper.COL_NAME, ContactDBHelper.COL_PHONE},
//				new int[] {android.R.id.text1, android.R.id.text2},
//				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);
		lvContacts.setAdapter(adapter);

//		리스트 뷰 클릭 처리
		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(AllContactsActivity.this, UpdateActivity.class);
				intent.putExtra("id", id);
				startActivityForResult(intent, UPDATE_CODE);
			}
		});

//		리스트 뷰 롱클릭 처리
		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final long _id = id;
				AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
				builder.setTitle("연락처 삭제")
						.setMessage("삭제하시겠습니까?")
						.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SQLiteDatabase db = helper.getWritableDatabase();
								String whereClause = "_id=?";
								String[] whereArgs = new String[] { String.valueOf(_id) };
								db.delete(helper.TABLE_NAME, whereClause,whereArgs);
								helper.close();
								onResume();
							}
						})
						.setNegativeButton("취소", null)
						.show();
				return true;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
//		수정 취소시 데이터를 다시 읽어오지 않도록
		if (update_flag) {
			SQLiteDatabase db = helper.getReadableDatabase();
			cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);

			Log.d("onResume", "db읽기 실행");

			adapter.changeCursor(cursor);
			helper.close();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//        cursor 사용 종료
		if (cursor != null) cursor.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					Toast.makeText(this,  "수정 완료", Toast.LENGTH_SHORT).show();
					update_flag = TRUE;
					break;
				case RESULT_CANCELED:
					Toast.makeText(this, "수정 취소", Toast.LENGTH_SHORT).show();
					update_flag = FALSE;
					break;
			}
		}
	}
}