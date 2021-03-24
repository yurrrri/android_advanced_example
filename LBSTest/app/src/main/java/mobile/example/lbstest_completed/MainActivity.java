package mobile.example.lbstest_completed;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	private final static String TAG = "MainActivity";
	private final static int MY_PERMISSIONS_REQ_LOC = 100;

	private TextView tvDisplay;
	private TextView tvLocations;
	private int clickedButton;		/*버튼을 눌렀을 때 권한요청으로 실행이 넘어갈 경우를 대비해 클릭한 버튼 기억*/

	private LocationManager locManager;
	private String bestProvider;
	private Geocoder geocoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvDisplay = (TextView) findViewById(R.id.tvDisplay);
		tvLocations = (TextView) findViewById(R.id.tvLocations);

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		/*Passive 가 아닌 GPS 또는 Network provider 중 선택이 필요할 경우 기준 설정*/
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
//		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
//		criteria.setAltitudeRequired(false);
//		criteria.setCostAllowed(false);
//		bestProvider = locManager.getBestProvider(criteria, true);

		bestProvider = LocationManager.GPS_PROVIDER;
		geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

	}


	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnLast:
				clickedButton = R.id.btnLast;
				tvLocations.setText("최종 실행 위치: ");
				getLastLocation();
			break;
			case R.id.btnStart:
				clickedButton = R.id.btnStart;
				tvDisplay.setText("Best Provider: " + bestProvider);
				getCurrentLocation();
			break;
		}

	}

	/*현재 사용 중인 Provider 로부터 전달 받은 최종 위치의 주소 확인 - 권한 확인 필요*/
	private void getLastLocation() {
		if (checkPermission()) {
			List<Address> addresses = null;
			ArrayList<String> addressFragments;

			Location lastLocation = locManager.getLastKnownLocation(bestProvider);
			double latitude = lastLocation.getLatitude();
			double longitude = lastLocation.getLongitude();
			tvDisplay.setText("위도: " + latitude + " 경도: " + longitude);

			//        위도/경도에 해당하는 주소 정보를 Geocoder 에게 요청
			try {
//            위도, 경도를 실제 주소로 바꿔줌
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			//      주소 추출
//        주소가 없으면
			if (addresses == null || addresses.size() == 0) {
				Log.e(TAG, getString(R.string.no_address_found));
			} else {
//           addressList의 address에는 주소, 우편번호, 전화번호 등 여러가지 정보가 담겨있음
				Address addressList = addresses.get(0);
				addressFragments = new ArrayList<String>();

//            그중에서 주소만 뽑아내서 addressFragments에 저장함
				for (int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
					addressFragments.add(addressList.getAddressLine(i));
				}

				for (int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
					tvLocations.setText(addressFragments.get(i));
				}
			}
		}
	}

	/*위치 조사 시작 - 권한 확인 필요*/
	private void getCurrentLocation() {
		if (checkPermission()) {
			locManager.requestLocationUpdates(bestProvider, 5000, 10, locListener);
		}
	}

	@Override
    protected void onPause() {
        super.onPause();
        /*위치 조사 종료 - 반드시 추가!*/
		locManager.removeUpdates(locListener);
    }


	/* 위치 정보 수신 리스너 생성 */
	LocationListener locListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			List<Address> addresses = null;
			ArrayList<String> addressFragments;

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			tvDisplay.setText("위도: " + latitude + " 경도: " + longitude);

			//        위도/경도에 해당하는 주소 정보를 Geocoder 에게 요청
			try {
//            위도, 경도를 실제 주소로 바꿔줌
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			//      주소 추출
//        주소가 없으면
			if (addresses == null || addresses.size() == 0) {
				Log.e(TAG, getString(R.string.no_address_found));
			} else {
//           addressList의 address에는 주소, 우편번호, 전화번호 등 여러가지 정보가 담겨있음
				Address addressList = addresses.get(0);
				addressFragments = new ArrayList<String>();

//            그중에서 주소만 뽑아내서 addressFragments에 저장함
				for (int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
					addressFragments.add(addressList.getAddressLine(i));
				}
				for (int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
					tvLocations.setText(addressFragments.get(i));
				}
			}
		}

		//아래 세가지 메소드는 구현할필요 x
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {		}

		@Override
		public void onProviderEnabled(String provider) {		}

		@Override
		public void onProviderDisabled(String provider) {		}
	};


	/*위치 관련 권한 확인 메소드 - 필요한 부분이 여러 곳이므로 메소드로 구성*/
    /*ACCESS_FINE_LOCATION - 상세 위치 확인에 필요한 권한
    ACCESS_COARSE_LOCATION - 대략적 위치 확인에 필요한 권한*/
    private boolean checkPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
								Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQ_LOC);
			    return false;
			} else
				return true;
		}
		return false;
	}

	//권한 승인 요청이 처리되면 자동 호출
	/*권한승인 요청에 대한 사용자의 응답 결과에 따른 수행*/
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch(requestCode) {
			case MY_PERMISSIONS_REQ_LOC:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
						&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
					/*권한을 승인받았을 때 수행하여야 하는 동작 지정*/
					//처음에 권한을 승인받을때 코드가 비어있으므로 아무런 동작x

				} else {
					/*사용자에게 권한 제약에 따른 안내*/
					Toast.makeText(this, "Permissions are not granted.", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
}

