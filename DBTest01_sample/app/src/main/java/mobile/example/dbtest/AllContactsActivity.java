package mobile.example.dbtest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AllContactsActivity extends Activity {
	final int UPDATE_CODE = 200;
	final int INSERT_CODE = 300;

	private ListView lvContacts = null;

	private ArrayAdapter<ContactDto> adapter;
	private ContactDBHelper helper;
	private ArrayList<ContactDto> contactList;
	private ContactDBManager contactDBManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);

		helper = new ContactDBHelper(this);
		contactList = new ArrayList<ContactDto>();
		lvContacts = (ListView)findViewById(R.id.lvContacts);
//		adapter에 레이아웃과 원본 데이터 전달
		adapter = new ArrayAdapter<ContactDto>(this, android.R.layout.simple_list_item_1, contactList);
		contactDBManager = new ContactDBManager(this);

//		리스트뷰에 위의 adapter를 세팅하면 리스트뷰에 레이아웃과 원본 데이터대로 아답터를 띄워줌
		lvContacts.setAdapter(adapter);

		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactDto dto = contactList.get(position);
				Intent intent = new Intent(AllContactsActivity.this, UpdateContactActivity.class);
				intent.putExtra("contact", dto);
				startActivityForResult(intent, UPDATE_CODE);
			}
		});

		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int pos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
				builder.setTitle("연락처 삭제")
						.setMessage(contactList.get(pos).getName()+"를(을) 삭제하시겠습니까?")
						.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (contactDBManager.removeContact(contactList.get(pos).getId())) {
									Toast.makeText(AllContactsActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
									contactList.clear();
									contactList.addAll(contactDBManager.getAllContacts());
									adapter.notifyDataSetChanged();
								} else {
									Toast.makeText(AllContactsActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
								}
								}
							})
						.setNegativeButton("취소", null)
						.show();
				return true;
			}
		});
	}

	//화면이 보여지기 전에 호출되는 메소드
	@Override
	protected void onResume() {
		super.onResume();

		contactList.clear(); //액티비티를 백그라운드로 띄운 상태에서 다시 액티비티를 실행시키면 onResume이 실행되어
							 //기존 리스트에 데이터를 또 추가를 하는 것이기 때문에 중복이 됨
							// 따라서 지워주고 다시 처음부터 불러오는 작업이 필요함
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME, null);
		// 메소드 사용시
		// db.query(ContactDBHelper.TABLE_NAME, null, null, null, null, ull, null, null);

		while (cursor.moveToNext()){
			ContactDto dto = new ContactDto();
			dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			dto.setName(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
			dto.setPhone(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
			dto.setCategory(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_CAT)));
			contactList.add(dto);
		}

		adapter.notifyDataSetChanged();

		cursor.close();
		helper.close();

		//return contactList;
		/* 데이터 읽기 -> Manager 사용시
		contactList.addAll(contactDBManager.getAllContacts());
		adapter.notifyDataSetChanged();
		 */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					String contact = data.getStringExtra("contact");
					Toast.makeText(this,  contact+" 수정 완료", Toast.LENGTH_SHORT).show();
					break;
				case RESULT_CANCELED:
					Toast.makeText(this, "수정 취소", Toast.LENGTH_SHORT).show();
					break;
			}
		}
		else if (requestCode==INSERT_CODE){
			switch (resultCode){
				case RESULT_OK:
					Toast.makeText(this, "추가 완료", Toast.LENGTH_SHORT).show();
					break;
				case RESULT_CANCELED:
					Toast.makeText(this, "추가 취소", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
}




