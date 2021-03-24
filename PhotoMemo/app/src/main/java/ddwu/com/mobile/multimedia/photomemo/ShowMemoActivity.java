package ddwu.com.mobile.multimedia.photomemo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowMemoActivity extends AppCompatActivity {

    final static String TAG = "ShowMemoActivity";

    MemoDBHelper helper;
    ImageView ivPhoto;
    TextView tvMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memo);

//        MainActivity 에서 전달 받은 _id 값을 사용하여 DB 레코드를 가져온 후 ImageView 와 TextView 설정




    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
