package ddwu.com.mobile.example.lbs.placetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

//일반인이 만든 라이브러리
import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "MainActivity";
    final static int PERMISSION_REQ_CODE = 100;

    /*UI*/
    private EditText etKeyword;
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;

    /*DATA*/
    private PlacesClient placesClient;

    //ssac 영등포 캠퍼스 주소
    private Double current_lat = 37.5178704231165;
    private Double current_lng = 126.88646244184045;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKeyword = findViewById(R.id.etKeyword);
        mapLoad();

//        Places 초기화
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearch:
                if (etKeyword.getText().toString().equals("")){
                    Toast.makeText(this, "'카페'나 '검색'을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                if (etKeyword.getText().toString().equals("카페")) {
                    searchStart(PlaceType.CAFE);
                } else if (etKeyword.getText().toString().equals("식당")) {
                    searchStart(PlaceType.RESTAURANT);
                }
                break;
        }
    }

    /*입력된 유형의 주변 정보를 검색*/
    private void searchStart(String type) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.api_key))
                .latlng(current_lat,
                        current_lng)
                .radius(100)
                .type(type)
                .build()
                .execute();
    }

    private void callDetailActivity(Place place) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("name",place.getName());
        intent.putExtra("phone",place.getPhoneNumber());
        intent.putExtra("address",place.getAddress());

        startActivity(intent);
    }

    /*Place ID 의 장소에 대한 세부정보 획득*/
    private void getPlaceDetail(String placeId) {
//      상세 정보로 요청할 정보의 유형 지정
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);
//       요청 생성
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
//       요청 성공시 처리 리스너 연결
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                Place place = response.getPlace();
                callDetailActivity(place);
//        요청 실패시 처리 리스너 연결
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException){
                    ApiException apiException = (ApiException) e;
                    int statusCode = apiException.getStatusCode(); //상태에 따른 코드값 확인
                    Log.e(TAG, "Place not found: "+ statusCode +" "+e.getMessage());
                }
            }
        });
    }


    PlacesListener placesListener = new PlacesListener() {
        @Override
        public void onPlacesFailure(PlacesException e) { }

        @Override
        public void onPlacesStart() { }

        // 이 함수가 제일 중요 !
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            Log.d(TAG, "Adding Markers");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //   마커 추가
                    for (noman.googleplaces.Place place:places){
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        //   marker의 settag를 사용하여 place id 보관
                        newMarker.setTag(place.getPlaceId());
                        Log.d(TAG, place.getName()+" : "+place.getPlaceId());
                    }
                }
            });
        }

        @Override
        public void onPlacesFinished() { }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        Log.d(TAG, "Map ready");

        if (checkPermission()){ //permission이 승인되면,
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(MainActivity.this,
                        String.format("현재 위치:(%f, %f)", location.getLatitude(), location.getLongitude()),
                        Toast.LENGTH_SHORT).show();
                current_lat = location.getLatitude();
                current_lng = location.getLongitude();
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                current_lat = latLng.latitude;
                current_lng = latLng.longitude;
            }
        });

//    마커의 윈도우를 클릭했을 때 장소의 상세 정보를 보여줌 => getPlaceDetail()
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                getPlaceDetail(placeId);
            }
        });

        //markeroption 준비 => 지도가 준비될 때 load
        markerOptions = new MarkerOptions();
    }

    /*구글맵을 멤버변수로 로딩 => onCreate에서 실행 */
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // 매개변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }

    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




    /*구글이 현재 위치의 주변 정보를 확인하는 기본 방법 사용 메소드*/

/*
    private void startCPSearch() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ID);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).

        if (checkPermission()) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                            Log.i(TAG, String.format("Place ID: %s", placeLikelihood.getPlace().getId()));
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getLikelihood()));
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        }
    }
*/

}
