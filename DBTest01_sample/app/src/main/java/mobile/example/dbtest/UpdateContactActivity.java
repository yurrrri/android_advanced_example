package mobile.example.dbtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateContactActivity extends AppCompatActivity {

    ContactDto dto;

    EditText edtName;
    EditText edtPhone;
    EditText edtCat;

    ContactDBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

//        전 액티비티에서 넘겨받은 데이터
        dto = (ContactDto) getIntent().getSerializableExtra("contact");
        manager = new ContactDBManager(this);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtCat = findViewById(R.id.edtCat);

//        먼저 화면에서 수정할 주소록의 정보를 보여줌
        edtName.setText(dto.getName());
        edtPhone.setText(dto.getPhone());
        edtCat.setText(dto.getCategory());
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateContactSave:
                dto.setName(edtName.getText().toString());
                dto.setPhone(edtPhone.getText().toString());
                dto.setCategory(edtCat.getText().toString());

                if (manager.updateContact(dto)) {
                    Intent intent = new Intent();
                    intent.putExtra("contact", dto.getName());
                    setResult(RESULT_OK, intent);
                }
                else
                    setResult(RESULT_CANCELED);
                break;
            case R.id.btnUpdateContactClose:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}