package mobile.example.network.requesthttp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etDate;
    EditText etPerPage;
    EditText etNation;
    TextView tvResult;
    String url;
    HashMap<String, String> optionMap; //매개 변수저장 HashMap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 매개변수
        etDate = findViewById(R.id.etDate); //날짜
        etPerPage = findViewById(R.id.etPerPage); //결과 row 개수 지정
        etNation = findViewById(R.id.etNation); //영화 국가 지정

        tvResult = findViewById(R.id.tvResult);
        url = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml?key=3f8026dbde674a8c266c9945563d4fc5";

        optionMap = new HashMap<String, String>();

        /*        네트워크 제약 사항 적용을 해제하기 위해 사용
		실습 시 테스트용으로만 사용하며 추후 스레드 또는 AsyncTask 사용 방법으로 대체할 것		*/
        StrictMode.ThreadPolicy pol
                = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(pol);
    }

    public void onClick(View v) {
        String date = etDate.getText().toString();
        String perPage = etPerPage.getText().toString();
        String nation = etNation.getText().toString();

//        parameter로 쓸 key-value 쌍을 map에 집어넣기
        optionMap.put("targetDt", date);
        optionMap.put("itemPerPage", perPage);
        optionMap.put("repNationCd", nation);

        if (!isOnline()) {
            Toast.makeText(this, "Network is not available!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.btn_request:
                if (!date.equals("")) {
                    url+="&targetDt="+optionMap.get("targetDt");
                }
                if (!perPage.equals("")){
                    url+="&itemPerPage="+optionMap.get("itemPerPage");
                }
                if (!nation.equals("")){
                    url+="&repNationCd="+optionMap.get("repNationCd");
                }
                String result = downloadContents(url);
                tvResult.setText(result);
                break;
        }
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //	문자열 형태의 웹 주소를 입력으로 서버 응답을 문자열로 만들어 반환
    private String downloadContents(String address){
        HttpsURLConnection conn = null;
        InputStream stream = null;
        String result = null;
        int responseCode = 200;

        try { //프로그램 실행
            //open
            URL url = new URL(address);
            conn = (HttpsURLConnection)url.openConnection();

            //연결 정보 설정
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect(); // 생략 가능

            responseCode = conn.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) { //HTTP_OK : 200;
                throw new IOException("HTTP error code: " + responseCode);
            }

            stream = conn.getInputStream();
            //readStream : 사용자가 정의하는 함수. 여기서는 string으로 읽어오게 구현
            result = readStream(stream);
        } catch (MalformedURLException e) { //예외처리
            Toast.makeText(this, "주소 오류", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } finally { //어떤 예외가 발생하더라도 반드시 실행되어야하는 부분
            if (stream != null) {
                try { stream.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
            if (conn != null) conn.disconnect();
        }
        return result;
    }

    //	stream을 문자열로 만드는 메소드
    public String readStream(InputStream stream){
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

//			stream을 문자열로 완성
            while (readLine != null) {
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}