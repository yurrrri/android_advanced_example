package mobile.example.network.toyasynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    TextView textView;
    TextAsyncTask textAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart1:
//                AsyncTask 생성 후 실행
                textAsyncTask = new TextAsyncTask();
                textAsyncTask.execute("hi!", "hello!");     // 매개변수 전달, strings[] 에 전달됨
                Toast.makeText(this, "AsyncTask Start!", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnStop:
                if (textAsyncTask != null) textAsyncTask.cancel(true);     // AsyncTask 중지
                break;
            case R.id.btnStart2:
                Toast.makeText(this, "Click!!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


//    String 타입의 매개변수를 전달받음, Integer 타입으로 진행상태 정보 표시, Integer 타입의 결과 생성
    class TextAsyncTask extends AsyncTask<String, Integer, Integer> {

        final static String TAG = "TextAsyncTask";

//      asynctask.execute 실행 후, 순서 1 => 작업 수행 전 초기화 작업이 필요시
//    메인 쓰레드와 다른 실행 흐름이 아니므로 TextView에 setText실행 가능
        @Override
        protected void onPreExecute() {
            textView.setText(textView.getText() + "\nonPreExecute() is performed!" + "\n");
        }

//        순서 2
      /*  필수 구현 => 비동기 방식으로 수행해야 할 작업
        excute(...) 에 입력한 매개변수를 배열 형태로 전달  */
        @Override
        protected Integer doInBackground(String... strings) { //strings : 배열
            Log.d(TAG, " Async start! " + strings[0] + " " + strings[1]);

//            AsyncTask에서 해야할 작업
            int sum = 0;
            for (int i=1; i <= 100; i++) {
                sum += i;
                Log.d(TAG, "value: " + i);
//             onProgressUpdate에 매개변수 전달하여 호출
                publishProgress(sum, i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            return sum; //결과를 Integer로 반환
        }

//        진행 상태를 doInBackgrouund의 publishProgress() 로부터 전달받음
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "sum: " + values[0]);
            textView.setText("value: " + values[1] + "\n");
        }

//        @Override
//        protected void onCancelled(Integer n) {
//            textView.setText(textView.getText() + "Stop button is clicked!! " + n);
//        }

//        AsyncTask 중지 시 호출
        @Override
        protected void onCancelled() {
            textView.setText(textView.getText() + "Interrupted!");
            Log.d(TAG, "Stop button is clicked!");
        }

//        순서 3 : 실행이 된 후
//        doInBackground() 가 반환한 결과값을 전달 받음
        @Override
        protected void onPostExecute(Integer n) {
            textView.setText(textView.getText() + "sum: " + n + "\n");
        }
    }
}

