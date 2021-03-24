package ddwucom.mobile.myonepick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class InsertPlaceActivity extends AppCompatActivity {

    EditText edtCategory;
    EditText edtName;
    EditText edtReview;
    EditText edtRate;

    PlaceDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_place);

        edtCategory = findViewById(R.id.edtCategory);
        edtName = findViewById(R.id.edtName);
        edtReview = findViewById(R.id.edtReview);
        edtRate = findViewById(R.id.edtRate);

        helper = new PlaceDBHelper(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAddNewPlace:
//			DB 데이터 삽입 작업 수행
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();

                row.put(helper.COL_CATEGORY, edtCategory.getText().toString());
                row.put(helper.COL_NAME, edtName.getText().toString());
                row.put(helper.COL_REVIEW, edtReview.getText().toString());
                row.put(helper.COL_RATE, edtRate.getText().toString());

                db.insert(helper.TABLE_NAME, null, row);

                helper.close();
                break;
            case R.id.btnAddNewPlaceClose:
//			DB 데이터 삽입 취소 수행
                Toast.makeText(this, "추가 취소", Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }
}