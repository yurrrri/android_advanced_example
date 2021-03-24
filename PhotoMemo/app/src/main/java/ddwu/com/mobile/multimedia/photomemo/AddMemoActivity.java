package ddwu.com.mobile.multimedia.photomemo;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMemoActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 200;

    private String mCurrentPhotoPath;

    ImageView ivPhoto;
    EditText etMemo;

    MemoDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        helper = new MemoDBHelper(this);

        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
        etMemo = (EditText)findViewById(R.id.etMemo);

        ivPhoto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    외부 카메라 호출

                    return true;
                }
                return false;
            }
        });

    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSave:
//                DB에 촬영한 사진의 파일 경로 및 메모 저장

                Toast.makeText(this, "Save!", Toast.LENGTH_SHORT).show();
            case R.id.btnCancel:
                finish();
                break;
        }
    }




}
