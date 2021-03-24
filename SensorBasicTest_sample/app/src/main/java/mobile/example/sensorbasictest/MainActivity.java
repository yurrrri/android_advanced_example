package mobile.example.sensorbasictest;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private TextView tvText;
	private SensorManager sensorManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvText = (TextView)findViewById(R.id.tvText);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSensor:
			String result = "";

			List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
//			가속도에 해당하는 여러 센서중에 기본값 센서를 반환하고 싶을때?
			sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			for (Sensor sensor : sensorList){
				String sensorSpec = String.format("Sensor Name: %s\nSensor Type: %s\n\n",
						sensor.getName(), sensor.getType());
				result+=sensorSpec;
			}
			
			tvText.setText(result);
			
			break;
		}
	}
	
	
}
