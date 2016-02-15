package pwgame.passwordgame;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class introduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] desc = new String[]{"Welcome to the Password Game!"," ", "The purpose of this game is to familiarize you with various methods of generating passwords based on website names",
                "You will be given website names and asked to generate passwords based on the pattern for that level",
                "After trying these levels, we encourage you to create your own games and perhaps even use a schema to replace or improve existing passwords."};
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        for (int x = 0; x < desc.length; x++) {
            TextView t = new TextView(this);
            t.setTextColor(Color.BLACK);
            t.setTextSize(20);
            t.setText(desc[x]);
            ll.addView(t);
        }
        Button go = new Button(this);
        go.setText("Start Playing");
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), levelActivity.class);
                intent.putParcelableArrayListExtra("pwd", getIntent().<PasswordData>getParcelableArrayListExtra("pwd"));
                intent.putExtra("levelNumber", 0);
                startActivity(intent);
                finish();
            }
        });
        ll.addView(go);
        setContentView(ll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_introduction, menu);
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
}
