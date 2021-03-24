package ddwucom.mobile.basicmaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSION_REQ_CODE = 100;

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;

    private Marker centerMarker;
    private List<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        markerList = new ArrayList<Marker>();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnStart:
                locationUpdate();
                break;
            case R.id.btnStop:
                locationManager.removeUpdates(locationListener);
                break;
        }
    }

    private void locationUpdate(){
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            centerMarker.setPosition(currentLoc);
//            위치가 바뀌면 snippet도 바뀌게
            centerMarker.setSnippet(String.format("위도:%.6f, 경도:%.6f", currentLoc.latitude, currentLoc.longitude));
            centerMarker.showInfoWindow();
            //pOptions.add(currentLoc);
            //mGoogleMap.addPolyline(pOptions);
        }
    };

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            LatLng currentLoc = new LatLng(37.606320, 127.041808);
//            바로 위치로 이동
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
//            초기화된 위치에서 이동하는 애니메이션을 보여주면서 이동
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(currentLoc);
            options.title("현재 위치");
            options.snippet(String.format("위도:%.6f, 경도:%.6f", currentLoc.latitude, currentLoc.longitude));
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            centerMarker = mGoogleMap.addMarker(options);
            markerList.add(centerMarker); //리스트에 추가
//            showInfoWindow 생략시 마커를 터치해야 윈도우가 나타남
            centerMarker.showInfoWindow();

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(MainActivity.this, "주소 : "+ marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Toast.makeText(MainActivity.this, "위도: "+latLng.latitude+" 경도 : " +latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    LatLng currentLoc = new LatLng(latLng.latitude, latLng.longitude);

                    MarkerOptions options = new MarkerOptions();
                    options.position(currentLoc);
                    options.title("현재 위치");
                    options.snippet(String.format("위도:%.6f, 경도:%.6f", currentLoc.latitude, currentLoc.longitude));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    Marker newMarker;
                    newMarker = mGoogleMap.addMarker(options);
                    markerList.add(newMarker);

                    newMarker.showInfoWindow();
                }
            });
        }
    };

    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSION_REQ_CODE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//               퍼미션을 획득하였을 경우 맵 로딩 실행
                locationUpdate();
            }
            else{
//                퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함",Toast.LENGTH_SHORT ).show();
            }
        }

    }
}