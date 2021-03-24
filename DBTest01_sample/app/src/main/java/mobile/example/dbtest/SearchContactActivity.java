package mobile.example.dbtest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchContactActivity extends Activity {

	ContactDBHelper helper;
	EditText etSearchName;
	TextView tvSearchResult;
	Button btnSearchContact;
	Button btnClose;
	ContactDBManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_contact);

		helper = new ContactDBHelper(this);
		manager = new ContactDBManager(this);

		etSearchName = findViewById(R.id.etSearchName);
		tvSearchResult = findViewById(R.id.tvSearchResult);
		btnSearchContact = findViewById(R.id.btnSearchContact);
		btnClose = findViewById(R.id.btnClose);

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSearchContact:
				String result = manager.searchContact(etSearchName.getText().toString());
				tvSearchResult.setText(result);
				break;
			case R.id.btnClose:
				Toast.makeText(getApplicationContext(), "검색 취소", Toast.LENGTH_LONG).show();
				finish();
				break;
		}
	}
}
