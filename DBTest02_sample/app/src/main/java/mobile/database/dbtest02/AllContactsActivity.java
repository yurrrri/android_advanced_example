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
	SimpleCursorAdapter adapter;
//	MyCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ContactDBHelper(this);

//		SimpleCursorAdapter 객체 생성
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[]{ContactDBHelper.COL_NAME, ContactDBHelper.COL_PHONE},
				new int[] {android.R.id.text1, android.R.id.text2},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

//		MyCurSorAdapter 객체 생성
//		adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);
		lvContacts.setAdapter(adapter);

//		리스트 뷰 클릭 처리
		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				execSQL("delete from contact_table where id = "+id+";");
				Intent intent = new Intent(AllContactsActivity.this, UpdateActivity.class);
				intent.putExtra("id", id);
				startActivityForResult(intent, UPDATE_CODE);
			}
		});

//		리스트 뷰 롱클릭 처리
		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//				각 레코드의 id값을 일단 저장
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
//								다이얼로그가 충력 후 종료가 되면 onResume이 실행되지 않아 DB가 갱신되지않으므로,
//								직접 화면 갱신을 해주기 위해서 onResume()을 호출한다.
//								onResume 부분의 코드를 다시 여기에 그대로 가져와도 됨
								onResume();
							}
						})
						.setNegativeButton("취소", null)
						.show();
//				처리가 끝났으니 return true; 처리
				return true;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
//       DB에서 데이터를 읽어와 Adapter에 설정
//		수정 취소시 데이터를 다시 읽어오지 않도록 flag변수를 설정해서, flag변수가 TRUE일 경우(수정이 완료 된 경우)에만 onResume실행하여 화면 갱신
//		flag변수가 FALSE인 경우면 onResume이 실행되지 않으므로 화면이 갱신되지 X
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

//	수정 확인/취소 확인 변수를 결과로 전달받음
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					Toast.makeText(this,  "수정 완료", Toast.LENGTH_SHORT).show();
//					update_flag : 수정 확인/취소에 따라 onResume 수행을 조절하기 위한 변수
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