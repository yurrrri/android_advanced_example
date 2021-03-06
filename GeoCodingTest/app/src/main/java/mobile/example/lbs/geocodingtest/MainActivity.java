package mobile.example.lbs.geocodingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AddressResultReceiver addressResultReceiver;
    private LatLngResultReceiver latLngResultReceiver;

    /*UI*/
    EditText etLat;
    EditText etLng;
    EditText etAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLat = findViewById(R.id.etLat);
        etLng = findViewById(R.id.etLon);
        etAddress = findViewById(R.id.etAddress);

//        IntentService가 생성하는 결과 수신용 ResultReceiver
        addressResultReceiver = new AddressResultReceiver(new Handler());
        latLngResultReceiver = new LatLngResultReceiver(new Handler());

    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnFindAddress:
                if (etLat.getText().toString().equals("")) etLat.setText(etLat.getHint());
                if (etLng.getText().toString().equals("")) etLng.setText(etLng.getHint());
                startAddressService();
                break;
            case R.id.btnLatLon:
                if (etAddress.getText().toString().equals("")) etAddress.setText(etAddress.getHint());
                startLatLngService();
                break;
            case R.id.btnClear:
                etAddress.setText("");
                etLat.setText("");
                etLng.setText("");
        }
    }


    /* 위도/경도 → 주소 변환 IntentService 실행 */
    private void startAddressService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        String latitude = etLat.getText().toString();
        String longitude = etLng.getText().toString();
//       여러 구성요소(클래스 등)에서 동일한 상수를 사용해야 할 경우 지금과 같이
//        상수값을 보관하는 클래스 사용하여 일관성 유지(Constants 클래스)

//       startService의 결과를 받아올 receiver
//       adressResultReceiver가 Parcelable 형식으로 전달됨
        intent.putExtra(Constants.RECEIVER, addressResultReceiver);
//        위도와 경도 정보
        intent.putExtra(Constants.LAT_DATA_EXTRA, Double.valueOf(latitude));
        intent.putExtra(Constants.LNG_DATA_EXTRA, Double.valueOf(longitude));
//       서비스 실행 => intent 실행
        startService(intent);
    }



    /* 주소 → 위도/경도 변환 IntentService 실행 */
    private void startLatLngService() {
        String address = etAddress.getText().toString();
        Intent intent = new Intent(this, FetchLatLngIntentService.class);
        intent.putExtra(Constants.RECEIVER, latLngResultReceiver);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, address);
        startService(intent);
    }



    /* 위도/경도 → 주소 변환 ResultReceiver */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

//       receiver.send가 실행되면 수행되는 메소드
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String addressOutput = null;

            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (addressOutput == null) addressOutput = "";
                etAddress.setText(addressOutput);
            }
        }
    }


    /* 주소 → 위도/경도 변환 ResultReceiver */
    class LatLngResultReceiver extends ResultReceiver {
        public LatLngResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String lat;
            String lng;
            ArrayList<LatLng> latLngList = null;

            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                latLngList = (ArrayList<LatLng>) resultData.getSerializable(Constants.RESULT_DATA_KEY);
                if (latLngList == null) {
                    lat = (String) etLat.getHint();
                    lng = (String) etLng.getHint();
                } else {
                    LatLng latlng = latLngList.get(0);
                    lat = String.valueOf(latlng.latitude);
                    lng = String.valueOf(latlng.longitude);
                }

                etLat.setText(lat);
                etLng.setText(lng);
            } else {
                etLat.setText(getString(R.string.no_address_found));
                etLng.setText(getString(R.string.no_address_found));
            }
        }
    }


}
