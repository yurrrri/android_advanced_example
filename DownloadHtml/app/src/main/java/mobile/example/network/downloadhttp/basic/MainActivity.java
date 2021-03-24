package mobile.example.network.downloadhttp.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

	EditText etUrl;
	TextView tvResult;
	ImageView imageView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = findViewById(R.id.etUrl);
        tvResult = findViewById(R.id.tvResult);
        imageView = findViewById(R.id.imageView);

/*        네트워크 제약 사항 적용을 해제하기 위해 사용
		실습 시 테스트용으로만 사용하며 추후 스레드 또는 AsyncTask 사용 방법으로 대체할 것		*/
		StrictMode.ThreadPolicy pol
				= new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(pol);
	}

	public void onClick(View v) {
		//처음 : init_address의 값, 사용자가 입력한 url값
		String address = etUrl.getText().toString();

		String imageAddress = getResources().getString(R.string.image_url);
		//		values/strings.xml에서
//		<resources>
//    		<string name="app_name">Download HTML Basic</string>
//    		<string name="init_address">https://www.google.com</string>
//    		<string name="image_url">https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png</string>
//		</resources>
// 로 저장되어있음

//		네트워크 사용 가능 여부 확인
		if (!isOnline()) {
			Toast.makeText(this, "Network is not available!", Toast.LENGTH_SHORT).show();
			return;
		}

		switch (v.getId()) {
			case R.id.btnDownload:
				if (!address.equals("")) {
//					address의 응답 결과인 inputStream을 string으로 반환
					String result = downloadContents(address);
					tvResult.setText(result);
				}
				break;
			case R.id.btnImgDownload:
//				이미지를 다운로드 한 후 readStreamToBitmap() 호출
				Bitmap bitmap = downloadImage(imageAddress);
				imageView.setImageBitmap(bitmap);
				break;
		}
	}

//	네트워크 사용 가능 여부 확인
	private boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}


//	문자열 형태의 웹 주소를 입력으로 서버 응답(InputStream)을 문자열로 만들어 반환
	private String downloadContents(String address){		// 이미지일 경우 Bitmap 반환
		HttpsURLConnection conn = null;
		InputStream stream = null;
		String result = null;
		int responseCode = 200;

		try {
			//String(address)를 url 객체로 만들어 url의 http 연결 확보(open)
			URL url = new URL(address);
			conn = (HttpsURLConnection)url.openConnection(); //서버 연결 설정

			//연결 정보 설정
			conn.setReadTimeout(5000); //읽기 타임아웃 지정 => 5초
			conn.setConnectTimeout(5000); //쓰기 타임아웃 지정 => 5초
			conn.setRequestMethod("GET"); //default - 연결 방식 지정(GET방식으로 서버에 요청해서 데이터를 받아오겠다)
			conn.setDoInput(true); //default - 서버 응답 지정(데이터를 InputStream으로 받아오겠다), 생략 가능

			conn.connect(); // 통신 링크 열기 - 트래픽 발생, 생략 가능 => 데이터를 이때 받아옴

			responseCode = conn.getResponseCode(); //서버 전송 및 응답 결과 수신
			if (responseCode != HttpsURLConnection.HTTP_OK) { //HTTP_OK : 200; => 정상적으로 요청 전달
				throw new IOException("HTTP error code: " + responseCode);
			}

			stream = conn.getInputStream(); //응답 결과 스트림
			//readStream : 응답 결과 스트림을 어떤 데이터 타입으로 가져올 것인가?
			// 사용자가 정의하는 함수. 여기서는 string으로 읽어오게 구현
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
//			inputstream -> string
			InputStreamReader inputStreamReader = new InputStreamReader(stream);
//			string을 bufferedreader를 사용하여 읽어옴
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

//		string builder -> string으로
		return result.toString();
	}

	private Bitmap downloadImage(String address){		// 이미지일 경우 Bitmap 반환
		HttpsURLConnection conn = null;
		InputStream stream = null;
		Bitmap bitmap = null; // 이미지
		int responseCode = 200;

		try {
			//String(address)를 url 객체로 만들어 url의 http 연결 확보(open)
			URL url = new URL(address);
			conn = (HttpsURLConnection)url.openConnection();

			//연결 정보 설정
			conn.setReadTimeout(5000); //읽기 타임아웃 지정 => 5초
			conn.setConnectTimeout(5000); //쓰기 타임아웃 지정 => 5초
			conn.setRequestMethod("GET"); //default - 연결 방식 지정(GET방식으로 서버에 요청해서 데이터를 받아오겠다)
			conn.setDoInput(true); //default - 서버 응답 지정(데이터를 InputStream으로 받아오겠다), 생략 가능

			conn.connect(); // 통신 링크 열기 - 트래픽 발생, 생략 가능 => 데이터를 이때 받아옴

			responseCode = conn.getResponseCode(); //서버 전송 및 응답 결과 수신
			if (responseCode != HttpsURLConnection.HTTP_OK) { //HTTP_OK : 200; => 정상적으로 요청 전달
				throw new IOException("HTTP error code: " + responseCode);
			}

			stream = conn.getInputStream(); //응답 결과 스트림
			//readStream : 응답 결과 스트림을 어떤 데이터 타입으로 가져올 것인가?
			// 사용자가 정의하는 함수. 여기서는 Bitmap으로 읽어오게 구현
			// 이미지일 경우 readStreamToBitmap 사용
			bitmap = readStreamToBitmap(stream);
		} catch (MalformedURLException e) {
			Toast.makeText(this, "주소 오류", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NetworkOnMainThreadException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try { stream.close(); }
				catch (IOException e) { e.printStackTrace(); }
			}
			if (conn != null) conn.disconnect();
		}
		return bitmap;
	}

//	stream을 bitmap 이미지로
	private Bitmap readStreamToBitmap(InputStream stream) {
		return BitmapFactory.decodeStream(stream);
	}
}
