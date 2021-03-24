package ddwu.mobile.example.newlocationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    private TextView textView;

    /*이하의 클래스를 사용하기 위해서는 build.gradle dependencies 에
    com.google.android.gms:play-services-location:17.1.0 포함 필수*/
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;    /* 위치 수신 조건을 설정할 때 사용 */

    private boolean requestingLocationUpdates;  /* 위치 수신 중인지 확인 용도 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        /*위치 정보 탐색조건 코드를 별도의 메소드로 분리*/
        createLocationRequest();

        /*FusedLocationProviderClient - 기본 클래스인 LocationManager 의 역할을 수행*/
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*LocationCallback - 기본 클래스인 LocationListener 역할*/
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "in location callback");
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    textView.setText( String.format("Lat: %f  Lng: %f", location.getLatitude(), location.getLongitude()) );
                }
            }
        };

        requestingLocationUpdates = false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (requestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                if (!requestingLocationUpdates) {
                    requestingLocationUpdates = true;
                    startLocationUpdates();
                }
                break;
        }
    }


    private void startLocationUpdates() {
        if (checkPermission()) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return false;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission required.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}