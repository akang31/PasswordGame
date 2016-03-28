package pwgame.passwordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class source extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PasswordData pwd = (PasswordData)getIntent().getParcelableExtra("pwd");
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        String[] instr = pwd.getInstructions();
        Log.e("E", instr.length+"!!!!!!");
        for (int x = 0; x  < instr.length; x++) {
            TextView add1 = (TextView) getLayoutInflater().inflate(R.layout.text_view_layout, null);
            add1.setText((x+1)+": "+instr[x]);
            Log.e("E", instr[x]);
            add1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.addView(add1);
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(ll);
        setContentView(sv);
        //setContentView(R.layout.activity_source);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_source, menu);
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
