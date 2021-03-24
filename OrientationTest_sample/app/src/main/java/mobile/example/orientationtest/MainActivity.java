package mobile.example.orientationtest;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	private TextView tvText;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;

	private float[] mGravity;
	private float[] mGeomagnetic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvText = (TextView)findViewById(R.id.tvText);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnStart:
			sensorManager.registerListener(mSensorListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
			sensorManager.registerListener(mSensorListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
			break;
		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(mSensorListener);
	}		
	
	SensorEventListener mSensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
				mGravity = event.values.clone();
			if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
				mGeomagnetic = event.values.clone();
			}
			if (mGravity!=null && mGeomagnetic!=null){
				float rotationMatrix[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, mGravity, mGeomagnetic);
				if (success){
					float values[] = new float[3];
					SensorManager.getOrientation(rotationMatrix, values);
					for (int i=0; i<values.length; i++){
						Double degrees = Math.toDegrees(values[i]);
						values[i] = degrees.floatValue();
					}
					float azimuth = values[0];
					float pitch = values[1];
					float roll = values[2];
					String result = String.format(Locale.KOREA, "Azimuth: %f\nPitch: %f\nRoll: %f",
							azimuth, pitch, roll);
					tvText.setText(result);
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
}
