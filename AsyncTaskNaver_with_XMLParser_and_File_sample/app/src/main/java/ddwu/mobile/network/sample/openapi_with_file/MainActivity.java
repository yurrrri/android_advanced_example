package ddwu.mobile.network.sample.openapi_with_file;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ddwu.mobile.network.sample.openapi_with_file.R;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    EditText etTarget;
    ListView lvList;
    String apiAddress;

    String query;

    MyBookAdapter adapter;
    ArrayList<NaverBookDto> resultList;
    NaverBookXmlParser parser;
//    networkManager : 네트워크 관련 기능들 모아 클래스화
    NaverNetworkManager networkManager;
//    파일 관련 작업 매니저 별도 생성
    ImageFileManager imgFileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTarget = findViewById(R.id.etTarget);
        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList();
        adapter = new MyBookAdapter(this, R.layout.listview_book, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new NaverBookXmlParser();
        networkManager = new NaverNetworkManager(this);

//        clientid와 clientsecret은 반드시 필요하지 않은 정보이기 떄문에 별도의 getter setter로 설정하도록함
        networkManager.setClientId(getResources().getString(R.string.client_id));
        networkManager.setClientSecret(getResources().getString(R.string.client_secret));
        imgFileManager = new ImageFileManager(this);

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                /*롱클릭한 항목의 이미지 주소를 가져와 내부 메모리에 지정한 이미지 파일을 외부저장소로 이동
                * ImageFileManager 의 이동 기능 사용
                * 이동을 성공할 경우 파일 명, 실패했을 경우 null 을 반환하므로 해당 값에 따라 Toast 출력*/
                String imgAddress = resultList.get(position).getImageLink();
                String fileName = imgFileManager.moveFileToExt(imgAddress);
                if (fileName!=null)
                    Toast.makeText(MainActivity.this, fileName+"이 외부저장소로 이동되었습니다.", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 임시 파일 삭제
        imgFileManager.clearTemporaryFiles();
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearch:
                query = etTarget.getText().toString();  // UTF-8 인코딩 필요(한글일 경우 고려)
                // OpenAPI 주소와 query 조합 후 서버에서 데이터를 가져옴
                // 가져온 데이터는 파싱 수행 후 어댑터에 설정
                try{
//                   asynctask 실행
                    new NetworkAsyncTask().execute(apiAddress+
                            URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                break;
        }
    }


    class NetworkAsyncTask extends AsyncTask<String, Integer, ArrayList<NaverBookDto>> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected ArrayList<NaverBookDto> doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            // networking
            result = networkManager.downloadContents(address);
            if (result==null) return null;

            Log.d(TAG, result);
            // parsing
            resultList = parser.parse(result);

            for (NaverBookDto dto : resultList){
                Bitmap bitmap = networkManager.downloadImage(dto.getImageLink());
                if (bitmap!=null) imgFileManager.saveBitmapToTemporary(bitmap, dto.getImageLink());
            }

            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<NaverBookDto> resultList) {
            adapter.setList(resultList);    // Adapter 에 결과 List 를 설정 후 notify
            progressDlg.dismiss();
        }
    }
}
