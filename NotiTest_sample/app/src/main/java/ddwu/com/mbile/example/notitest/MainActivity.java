package ddwu.com.mbile.example.notitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnNoti:
                Intent intent = new Intent(this, NotiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                NotificationCompat.Builder builder
                        = new NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("알림의 제목")
                        .setContentText("기본적인 알람의 메시지보다 더 많은 양의 내용을 알림에 표시하고자 할 때 메시지가 잘리지 않도록 함")
//                        setStyle : 메시지가 잘리지 않도록 할때
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("기본적인 알람의 메시지보다 더 많은 양의 내용을 알림에 표시하고자 할 때 메시지가 잘리지 않도록 함"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true); //터치하면 알림이 닫히도록 설정

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                int notificationId = 100;
                notificationManager.notify(notificationId, builder.build());

                break;
        }
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);       // strings.xml 에 채널명 기록
            String description = getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
            int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
            notificationManager.createNotificationChannel(channel);
        }
    }

}
