package pwgame.passwordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class schemaLibrary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout temp = new RelativeLayout(this);
        temp.setGravity(Gravity.CENTER);
        TextView txt = new TextView(this);
        txt.setText("Library");
        temp.addView(txt);
        ll.addView(temp);
        int count = 5;
        for (int x = 0; x < count; x++) {
            LinearLayout disp = new LinearLayout(this);
            disp.setWeightSum(1.0f);
            Button disp1 = new Button(this);
            disp1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
            disp1.setText((x + 1) + "");
            disp.addView(disp1);
            Button disp2 = new Button(this);
            disp2.setText("Sample Schema Number " + (x + 1));
            disp2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f));
            disp2.setBackgroundResource(R.drawable.back);
            disp.addView(disp2);
            ll.addView(disp);
        }
        setContentView(ll);
        //setContentView(R.layout.activity_schema_library);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schema_library, menu);
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
