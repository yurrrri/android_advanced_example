package mobile.example.lbs.geocodingtest;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Service도 AndroidManifest에 등록이 필요 => AndroidManifest.xml 확인
public class FetchAddressIntentService extends IntentService {

    final static String TAG = "FetchAddress";

    private Geocoder geocoder;
    private ResultReceiver receiver;

    public FetchAddressIntentService() {
        super("FetchLocationIntentService");
    }

//    intent에 Intent를 부를때 지정한 intent 매개변수가 들어가게됨
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

//        MainActivity 가 전달한 Intent 로부터 위도/경도와 Receiver 객체 설정
        if (intent == null) return;
        double latitude = intent.getDoubleExtra(Constants.LAT_DATA_EXTRA, 0);
        double longitude = intent.getDoubleExtra(Constants.LNG_DATA_EXTRA, 0);
//       Receiver가 Parcelable 객체로 전달되었으므로 getParcelableExtra로 꺼냄
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        List<Address> addresses = null;

//        위도/경도에 해당하는 주소 정보를 Geocoder 에게 요청
        try {
//            위도, 경도를 실제 주소로 바꿔줌
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

//        결과로부터 주소 추출
//        주소가 없으면
        if (addresses == null || addresses.size()  == 0) {
            Log.e(TAG, getString(R.string.no_address_found));
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
        } else {
//           addressList의 address에는 주소, 우편번호, 전화번호 등 여러가지 정보가 담겨있음
            Address addressList = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

//            그중에서 주소만 뽑아내서 addressFragments에 저장함
            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                addressFragments.add(addressList.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
//                 TextUtils.join(개행문자, 문자열 list)
//                 문자열 list에 담긴 여러 문자열들을 첫 번째 매개변수인
//                 개행문자(여기서는 \n)로 구분해가며 하나의 문자열로 변환함
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }


//    ResultReceiver 에게 결과를 Bundle 형태로 전달
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }
}
