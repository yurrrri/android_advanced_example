package ddwucom.mobile.myonepick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class UpdatePlaceActivity extends AppCompatActivity {

    long id;

    EditText edtCategory;
    EditText edtName;
    EditText edtReview;
    EditText edtRate;

    PlaceDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_place);

        edtCategory = findViewById(R.id.edtCategory);
        edtName = findViewById(R.id.edtName);
        edtReview = findViewById(R.id.edtReview);
        edtRate = findViewById(R.id.edtRate);

        helper = new PlaceDBHelper(this);

//       이전 액티비티에서 id를 전달받아서,
        id = getIntent().getLongExtra("id", 1);
        // 데이터 읽어오기 => 전달받은 id로 검색
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = "_id=?";
        String[] selectArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(helper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            edtCategory.setText(cursor.getString(cursor.getColumnIndex("category")));
            edtName.setText(cursor.getString(cursor.getColumnIndex("name")));
            edtReview.setText(cursor.getString(cursor.getColumnIndex("review")));
            edtRate.setText(cursor.getString(cursor.getColumnIndex("rate")));
        }
        cursor.close();
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateNewPlace:
//                DB 데이터 업데이트 작업 수행
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();

                row.put(helper.COL_CATEGORY, edtCategory.getText().toString());
                row.put(helper.COL_NAME, edtName.getText().toString());
                row.put(helper.COL_REVIEW, edtReview.getText().toString());
                row.put(helper.COL_RATE, edtRate.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[] { String.valueOf(id) };
                db.update(helper.TABLE_NAME, row, whereClause, whereArgs);
                helper.close();

                setResult(RESULT_OK);
                break;
            case R.id.btnUpdateNewPlaceClose:
//                DB 데이터 업데이트 작업 취소
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}