package ddwucom.mobile.ma02_20161048;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//   메뉴 생성
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.opSearch:
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                break;
            case R.id.opMap:
                Intent map = new Intent(this, MapActivity.class);
                startActivity(map);
                break;
        }
        return true;
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnRestaurantList:
                Intent restaurantIntent = new Intent(this, AllPlacesActivity.class);
                restaurantIntent.putExtra("restaurant", "restaurant");
                startActivity(restaurantIntent);
                break;
            case R.id.btnCafeList:
                Intent cafeIntent = new Intent(this, AllPlacesActivity.class);
                cafeIntent.putExtra("cafe", "cafe");
                startActivity(cafeIntent);
                break;
        }
    }
}