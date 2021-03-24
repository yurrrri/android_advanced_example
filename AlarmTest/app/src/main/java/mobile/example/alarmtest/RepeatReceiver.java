package mobile.example.alarmtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Build;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RepeatReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Hi all!", Toast.LENGTH_SHORT).show();
		// notification 생성
	}
}
