package ddwu.mobile.network.sample.openapi_with_file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NaverNetworkManager {

    private Context context;
    private String clientId;
    private String clientSecret;

    public NaverNetworkManager(Context context) {
        this.context = context;
    }

    // 네이버 OpenAPI 사용 시 지정
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }


    /* 주소(address)에 접속하여 문자열 데이터를 수신한 후 반환 */
    public String downloadContents(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        String result = null;

        if (!isOnline()) return null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToString(stream);
            if (stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        return result;
    }


    /* 주소를 전달받아 bitmap 다운로드 후 반환 */
    public Bitmap downloadImage(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        Bitmap result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToBitmap(stream);
            if (stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        return result;
    }


//    downloadContents() 와 downloadImage()를 하나의 메소드로 구현할 경우
//    사용 시 매개변수로 String or Image 를 구분, 반환 값은 타입캐스팅 필요
//    public Object download(String address, boolean isImage) {
//        HttpURLConnection conn = null;
//        InputStream stream = null;
//        Object result = null;
//
//        try {
//            URL url = new URL(address);
//            conn = (HttpURLConnection)url.openConnection();
//            stream = getNetworkConnection(conn);
//            if (isImage) {
//                result = readStreamToBitmap(stream);
//            } else {
//                result = readStreamToString(stream);
//            }
//            if (stream != null) stream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) conn.disconnect();
//        }
//
//        return result;
//    }

    /* InputStream을 전달받아 비트맵으로 변환 후 반환 */
    private Bitmap readStreamToBitmap(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    private String readStreamToString(InputStream stream){
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

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

    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환
     * 네이버용을 수정 - ClientID, ClientSeceret 추가 strings.xml 에서 읽어옴*/
    private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {

        // 클라이언트 아이디 및 시크릿 그리고 요청 URL 선언
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if (clientId != null && clientSecret != null) {
            conn.setRequestProperty("X-Naver-Client-Id", clientId);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);
        }

        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        return conn.getInputStream();
    }
}
