package mobile.example.alarmtest;

import android.content.*;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		//Toast.makeText(context, "one time!", Toast.LENGTH_LONG).show();
		// Notification 출력
		NotificationCompat.Builder builder
				= new NotificationCompat.Builder(context, "MY_CHANNEL")
				.setSmallIcon(R.drawable.ic_stat_name)
				.setContentTitle("기상시간")
				.setContentText("일어나! 공부할 시간이야!")
//                        setStyle : 메시지가 잘리지 않도록 할때
				//.setStyle(new NotificationCompat.BigTextStyle().bigText("기본적인 알람의 메시지보다 더 많은 양의 내용을 알림에 표시하고자 할 때 메시지가 잘리지 않도록 함"))
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				//.setContentIntent(intent)
				.setAutoCancel(true); //터치하면 알림이 닫히도록 설정

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

		int notificationId = 100;
		notificationManager.notify(notificationId, builder.build());
	}
}