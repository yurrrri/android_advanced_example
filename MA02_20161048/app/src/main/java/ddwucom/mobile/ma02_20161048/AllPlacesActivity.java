package ddwucom.mobile.ma02_20161048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Boolean.TRUE;

public class AllPlacesActivity extends AppCompatActivity {

    final int UPDATE_CODE = 200;

    ListView lvMyPicks = null;
    PlaceDBHelper helper;

    Cursor cursor;
    MyCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_places);
        lvMyPicks = (ListView) findViewById(R.id.lvMyPicks);

        helper = new PlaceDBHelper(this);

        adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);
        lvMyPicks.setAdapter(adapter);

//		리스트 뷰 클릭 처리
        lvMyPicks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllPlacesActivity.this, PlaceInfoActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, UPDATE_CODE);
            }
        });

//		리스트 뷰 롱클릭 처리
        lvMyPicks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long _id = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(AllPlacesActivity.this);
                builder.setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String whereClause = "_id=?";
                                String[] whereArgs = new String[]{String.valueOf(_id)};
                                db.delete(helper.TABLE_NAME, whereClause, whereArgs);
                                helper.close();
                                onResume(); //삭제 후 화면 갱신
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
//      DB에서 데이터를 읽어와 Adapter에 설정
//		수정 취소시 데이터를 다시 읽어오지 않도록
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery("select * from " + PlaceDBHelper.TABLE_NAME, null);

            //Log.d("onResume", "onResume 실행");

            adapter.changeCursor(cursor);
            helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
    }
}
