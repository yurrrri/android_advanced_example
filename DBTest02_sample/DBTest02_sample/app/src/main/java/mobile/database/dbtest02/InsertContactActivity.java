package mobile.database.dbtest02;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InsertContactActivity extends AppCompatActivity {

	EditText etName;
	EditText etPhone;
	EditText etCategory;

	ContactDBHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

		etName = findViewById(R.id.editText1);
		etPhone = findViewById(R.id.editText2);
		etCategory = findViewById(R.id.editText3);

		helper = new ContactDBHelper(this);
	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAddNewContact:
//			DB 데이터 삽입 작업 수행
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues row = new ContentValues();

			row.put(helper.COL_NAME, etName.getText().toString());
			row.put(helper.COL_PHONE, etPhone.getText().toString());
			row.put(helper.COL_CATEGORY, etCategory.getText().toString());

			db.insert(helper.TABLE_NAME, null, row);

			helper.close();
			break;
		case R.id.btnAddNewContactClose:
//			DB 데이터 삽입 취소 수행
			Toast.makeText(this, "추가 취소", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
	}
}
