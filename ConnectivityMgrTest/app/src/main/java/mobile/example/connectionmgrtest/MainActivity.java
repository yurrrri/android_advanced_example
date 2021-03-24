package mobile.example.connectionmgrtest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String DEBUG_TAG = "NetworkStatusExample";

	TextView tvOutput;
	ConnectivityManager connMgr;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvOutput = findViewById(R.id.tvOutput);
        connMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

		// 강의교재 내용을 여기에 작성 후 로그를 확인해볼 것
		boolean isWifiConn = false;
		boolean isMobileConn = false;

		//getAllNetWorks : 네트워크 모듈 객체
		//여기서 하나하나 꺼내오면서 network의 타입 조사
		for (Network network : connMgr.getAllNetworks()){
			NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
				isWifiConn |= networkInfo.isConnected();
			}
			if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
				isMobileConn |= networkInfo.isConnected();
			}
		}
		Log.d(DEBUG_TAG, "Wifi connected : "+isWifiConn);
		Log.d(DEBUG_TAG, "Mobile Connected : " +isMobileConn);

    }
    
    public void onClick(View v) {
//    	클릭할때마다 새로운 Stringbuilder 생성 (빈 문자열에 내용 추가)
    	StringBuilder result = new StringBuilder();
    		
    	switch(v.getId()) {
    	case R.id.btnAllInfo:
//    		기기가 제공하는 모든 Network 타입 반환
    		NetworkInfo[] netInfo = connMgr.getAllNetworkInfo();
    		for (NetworkInfo n : netInfo) {
    			result.append(n.toString() + "\n\n");
    		}
    		break;
    	case R.id.btnActiveInfo:
    		//활성화 상태의 network 반환
    		NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();
    		result.append(activeNetInfo);
    		
    		break;
    	}
    	tvOutput.setText(result);
    }
}
