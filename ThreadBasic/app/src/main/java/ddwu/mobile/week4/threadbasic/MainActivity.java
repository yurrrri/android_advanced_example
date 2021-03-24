package ddwu.mobile.week4.threadbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.etText);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
//              1) 객체 생성 및 생성자의 매개변수로 handler 객체 전달
                TestThread t = new TestThread(handler);

//               아래 TestThread 실행 ->
//               TestThread의 run을 실행(Log에 i를 찍는 것)하면서 아래 기능들도 수행
//                즉, TestThread의 기능과 EditText를 띄우고 Toast를 띄우는것 동시에 실행
                t.start();

                /*Thread를 사용하지 않고 이렇게만 수행했을 경우 비동기 처리가 아니므로
                Log에 99까지 다찍을때까지 기다렸다가 settext하고 Toast를 띄움
                for (int i=0; i < 100; i++) {
                    Log.d(TAG, "i: " + i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                etText.setText("Thread start!");
                Toast.makeText(this, "Running!", Toast.LENGTH_SHORT).show();
                 */
                etText.setText("Thread start!");
                Toast.makeText(this, "Running!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    Handler : 쓰레드 사이에 메시지와 runnable 객체를 주고받을 수 있도록 하는 객체
    Handler handler = new Handler() {
        @Override
//     6)  handler가 전달되었을 때(sendMessage) 실행되는 메소드
        public void handleMessage(Message msg) {
            int i = msg.arg1; //Thread B에서 저장한 i값
            etText.setText("i: " + i);
        }
    };

// 상속을 통한 Thread 사용
    class TestThread extends Thread {
    // 2) 핸들러를 멤버 변수로 만들어서,
        Handler handler;

        // handler 보관
        public TestThread(Handler handler) {
            this.handler = handler;
        }

//     3) Thread 작업 실행
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                Log.d(TAG, "i: " + i);

            //   쓰레드는 화면의 요소 제어가 불가능함 ->etText.setText("i:" + i)는 error !!!!!
                //   위 코드에 의하면, 화면 ui는 메인 쓰레드가 관리하고 있는데 별도의 쓰레드가 메인 쓰레드에 관여하는 것이 됨.
                //    이를 금지시킴 ! => 별도의 쓰레드에서 메인 쓰레드의 UI 항목을 접근할 수 없음
                //    etText.setText("i:" + i);

                //   => 따라서, 수신한 데이터를 UI 쓰레드에 전달하는 방법이 Handler

                //  4) Handler에 전달할 message 생성
                Message msg = Message.obtain();
                //  메시지에 값 저장
                msg.arg1 = i;
                //   5) 메시지 핸들러에 전송
                handler.sendMessage(msg);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

