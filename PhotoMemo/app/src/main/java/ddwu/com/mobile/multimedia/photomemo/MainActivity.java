package ddwu.com.mobile.multimedia.photomemo;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SimpleCursorAdapter memoAdapter;
    Cursor cursor;
    MemoDBHelper helper;
    ListView lvMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MemoDBHelper(this);

//        어댑터에 SimpleCursorAdapter 연결
//        memoAdapter = new SimpleCursorAdapter( /* 매개변수 설정 */);


        lvMemo = (ListView)findViewById(R.id.lv_memo);
        lvMemo.setAdapter(memoAdapter);

        lvMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long _id) {
//                ShowMemoActivity 호출

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        DB 에서 모든 레코드를 가져와 Adapter에 설정

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
//                AddMemoActivity 호출


                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}
