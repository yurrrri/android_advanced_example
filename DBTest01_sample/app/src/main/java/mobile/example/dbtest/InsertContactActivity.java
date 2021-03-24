package mobile.example.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsertContactActivity extends Activity {

	ContactDBHelper helper;
	EditText etName;
	EditText etPhone;
	EditText etCategory;
	ContactDBManager manager;
	ContactDto dto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

//      DBHelper 생성
		helper = new ContactDBHelper(this);
		manager = new ContactDBManager(this);

		dto = new ContactDto();
		
		etName = (EditText)findViewById(R.id.edtName);
		etPhone = (EditText)findViewById(R.id.edtPhone);
		etCategory = (EditText)findViewById(R.id.edtCat);
	}
	
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnNewContactSave:
//				manager 사용안하고 여기서 바로 추가할 경우
				SQLiteDatabase db = helper.getWritableDatabase();

				ContentValues row = new ContentValues();
				row.put(helper.COL_NAME, etName.getText().toString());
				row.put(helper.COL_PHONE, etPhone.getText().toString());
				row.put(helper.COL_CAT, etCategory.getText().toString());

//				db.insert는 반환값이 있어 오류 여부 확인 가능
				db.insert(helper.TABLE_NAME, null, row);
//				SQL문 사용시
//				db.execSQL("insert into " + helper.TABLE_NAME + " values (null, '" + etName.getText().toString() + "', '" + etPhone.getText().toString() + "', '" + etCategory.getText().toString() + "');");
				helper.close();

				/* DTO와 Manager 사용 경우
				dto.setName(etName.getText().toString());
				dto.setCategory(etCategory.getText().toString());
				dto.setPhone(etPhone.getText().toString());

				boolean result = manager.addNewContact(dto);

				if (result) {    // 정상수행
					Intent resultIntent = new Intent();
					resultIntent.putExtra("contact", dto.getName());
					setResult(RESULT_OK, resultIntent);
					finish();
				} else {        // 정상수행 X
					Toast.makeText(this, "새로운 연락처 추가 실패!", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btnNewContactClose:
				setResult(RESULT_CANCELED);
				break;
		}*/
				finish();
		}
	}
}