package ddwu.mobile.week6.basicfiletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

//    파일 이름을 상수로 지정
    final static String IN_FILE_NAME = "infile.txt";
    final static String EXT_FILE_NAME = "extfile.txt";
    final static String CACHE_FILE_NAME = "cachefile.txt";

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.etText);

        SharedPreferences pref = getSharedPreferences("config", 0);
//        text_data key값인 값을 가져옴, 두 번째 값은 default값 : " "
        etText.setText(pref.getString("text_data", ""));
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pref = getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = pref.edit();

//        text_data가 key값인 값을 저장함
        editor.putString("text_data", etText.getText().toString());
//        값을 저장하기 위해 반드시 실행!!!!
        editor.commit();
    }

    public void onInClick(View v) {
        switch(v.getId()) {
            case R.id.btnInWrite:
                String data = etText.getText().toString();
//                내부 저장소에 파일 생성
//                File saveFile = new File(getFilesDir(), IN_FILE_NAME);
                try {
//                    파일용 Output 스트림 생성 => 생성한 파일을 출력 스트림에 연결
//                    FileOutputStream fos = new FileOutputStream(saveFile);

//                   내부 저장소에 파일 생성과 스트림 연결을 한 번에 수행
//                   이미 파일이 존재하면, 파일 내용을 추가하고자 할 경우 Context.MODE_APPEND 사용
                    FileOutputStream fos = openFileOutput(IN_FILE_NAME, Context.MODE_APPEND);
                    fos.write(data.getBytes());
                    fos.flush();
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btnInRead:
                String path = getFilesDir().getPath()+"/"+IN_FILE_NAME;
                File readFile = new File(path);
//               입출력 예외사항(IOException)을 처리하기 위해 try catch문 추가
                try {
                    FileReader fileReader = new FileReader(readFile);
                    BufferedReader br = new BufferedReader(fileReader);
                    String line = "";
                    String result = "";
                    while ((line = br.readLine()) != null) {
                        result+=line;
                    }
                    etText.setText(result);
                    br.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btnInDelete:
                File inFiles = getFilesDir();
//                files : 내부 저장소의 모든 파일을 배열로 반환한 것
                File[] files = inFiles.listFiles();
                for (File target:files){
                    target.delete();
                }
                break;
        }
    }

    public void onExtClick(View v) {
        switch (v.getId()) {
            case R.id.btnExtWrite:
//                외부저장소에 쓰기가 가능한지 확인해서 가능하면,
                if (isExternalStorageWritable()){
//                    mnt/sdcard/Android/data/패키지명/pictures/myalbum 폴더 생성
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "myalbum");
                    if (!file.mkdirs()){
                        Log.e(TAG, "Directory not created");
                    }
                    String data = etText.getText().toString();
                    File saveFile = new File(file.getPath(), EXT_FILE_NAME);
                    try {
                        FileOutputStream fos = new FileOutputStream(saveFile);
//                    append : 기존 파일에 내용 추가
//                    아래는 내부저장소에 파일을 쓸때
//                      FileOutputStream fos = openFileOutput(EXT_FILE_NAME, Context.MODE_APPEND);
                        fos.write(data.getBytes());
                        fos.flush();
                        fos.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btnExtRead:
                //     외부저장소 읽기가 가능한지 확인해서 가능하면,
                if (isExternalStorageReadable()) {
                    String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/myalbum/" + EXT_FILE_NAME;
                    File readFile = new File(path);
                    try {
                        FileReader fileReader = new FileReader(readFile);
                        BufferedReader br = new BufferedReader(fileReader);
                        String line = "";
                        String result = "";
                        while ((line = br.readLine()) != null) {
                            result += line;
                        }
                        etText.setText(result);
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btnExtDelete:
                File exFiles = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/myalbum/");
                File[] files = exFiles.listFiles();
                for (File target:files){
                    target.delete();
                }
                break;
        }
    }

    public void onCacheClick(View v) {
        switch(v.getId()) {
            case R.id.btnCacheWrite:
                String data = etText.getText().toString();
//                getCacheDir() : 내부저장소(앱 전용)의 캐시폴더
//                getExternalCacheDir() : 외부 저장소의 캐시 폴더

                File saveFile = new File(getCacheDir(), CACHE_FILE_NAME);
                try {
                    FileOutputStream fos = new FileOutputStream(saveFile);
//                    append : 기존 파일에 내용 추가
//                    FileOutputStream fos = openFileOutput(IN_FILE_NAME, Context.MODE_APPEND);
                    fos.write(data.getBytes());
                    fos.flush();
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btnCacheRead:
                String path = getCacheDir() + "/" + CACHE_FILE_NAME;
                File readFile = new File(path);
                try {
                    FileReader fileReader = new FileReader(readFile);
                    BufferedReader br = new BufferedReader(fileReader);
                    String line = "";
                    String result = "";
                    while ((line = br.readLine()) != null) {
                        result+=line;
                    }
                    etText.setText(result);
                    br.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btnCacheDelete:
                File cacheFiles = getCacheDir();
                File[] files = cacheFiles.listFiles();
                for (File target:files){
                    target.delete();
                }
                break;
        }
    }

    // Checks if a volume containing external storage is available
// for read and write.
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    // Checks if a volume containing external storage is available to at least read.
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}