package pwgame.passwordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class mainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void library(View view) {
        Intent intent = new Intent(this, schemaLibrary.class);
        startActivity(intent);
    }

    public void gamePortal(View view) {
        Intent intent = new Intent(this, introduction.class);
    /**
        PasswordData[] arr = new PasswordData[25];
        createLevels(arr);
        ArrayList<PasswordData> send = new ArrayList<PasswordData>();
        for (int x = 0; x < arr.length; x++) {
            if (arr[x] != null) {
                send.add(arr[x]);
            }
        }
        intent.putParcelableArrayListExtra("pwd", send);
        intent.putExtra("levelNumber", 0);
       **/
        startActivity(intent);
        /**
        Intent intent = new Intent(this, gamePortal.class);
        startActivity(intent);
        **/
    }

    public void builder(View view) {
        Intent intent = new Intent(this, schemaBuilder.class);
        startActivity(intent);
    }
}
