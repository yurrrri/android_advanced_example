package mobile.database.dbtest02;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    long id;
    EditText etName;
    EditText etPhone;
    EditText etCategory;

    ContactDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCategory = findViewById(R.id.etCategory);

        helper = new ContactDBHelper(this);

//       이전 acticity에서 id값 전달받아 사용
        id = getIntent().getLongExtra("id", 1);
        // 데이터 읽어오기
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = "_id=?";
        String[] selectArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(helper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            etName.setText(cursor.getString(cursor.getColumnIndex("name")));
            etPhone.setText(cursor.getString(cursor.getColumnIndex("phone")));
            etCategory.setText(cursor.getString(cursor.getColumnIndex("category")));
        }
        cursor.close();
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateContact:
//                DB 데이터 업데이트 작업 수행
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();

                row.put(helper.COL_NAME, etName.getText().toString());
                row.put(helper.COL_PHONE, etPhone.getText().toString());
                row.put(helper.COL_CATEGORY, etCategory.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[] { String.valueOf(id) };
                db.update(helper.TABLE_NAME, row, whereClause, whereArgs);
                helper.close();

//               수정 확인/취소를 확인할 수 있는 변수를 결과로 전달
                setResult(RESULT_OK);
                break;
            case R.id.btnUpdateContactClose:
//                DB 데이터 업데이트 작업 취소
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}
