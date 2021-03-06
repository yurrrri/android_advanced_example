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
//            ????????? ????????? snippet??? ?????????
            centerMarker.setSnippet(String.format("??????:%.6f, ??????:%.6f", currentLoc.latitude, currentLoc.longitude));
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
//            ?????? ????????? ??????
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
//            ???????????? ???????????? ???????????? ?????????????????? ??????????????? ??????
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(currentLoc);
            options.title("?????? ??????");
            options.snippet(String.format("??????:%.6f, ??????:%.6f", currentLoc.latitude, currentLoc.longitude));
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            centerMarker = mGoogleMap.addMarker(options);
            markerList.add(centerMarker); //???????????? ??????
//            showInfoWindow ????????? ????????? ???????????? ???????????? ?????????
            centerMarker.showInfoWindow();

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(MainActivity.this, "?????? : "+ marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Toast.makeText(MainActivity.this, "??????: "+latLng.latitude+" ?????? : " +latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    LatLng currentLoc = new LatLng(latLng.latitude, latLng.longitude);

                    MarkerOptions options = new MarkerOptions();
                    options.position(currentLoc);
                    options.title("?????? ??????");
                    options.snippet(String.format("??????:%.6f, ??????:%.6f", currentLoc.latitude, currentLoc.longitude));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    Marker newMarker;
                    newMarker = mGoogleMap.addMarker(options);
                    markerList.add(newMarker);

                    newMarker.showInfoWindow();
                }
            });
        }
    };

    /* ?????? permission ?????? */
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
//               ???????????? ??????????????? ?????? ??? ?????? ??????
                locationUpdate();
            }
            else{
//                ????????? ????????? ??? ???????????? ??????
                Toast.makeText(this, "??? ????????? ?????? ?????? ????????? ?????????",Toast.LENGTH_SHORT ).show();
            }
        }

    }
}