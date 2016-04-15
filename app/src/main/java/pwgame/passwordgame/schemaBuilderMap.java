package pwgame.passwordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class schemaBuilderMap extends AppCompatActivity {
    private String[] fromEntries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromEntries = new String[26+10+2];
        fromEntries[0] = "A-Z";
        fromEntries[1] = "0-9";
        for (int x = 0;x  < 26; x++) {
            fromEntries[x+2] = (char)('A'+x)+"";
        }
        for (int x = 0; x < 10; x++) {
            fromEntries[x+2+26] = x+"";
        }
        mapMenuCreate();
    }
    LinearLayout ll;
    private void mapMenuCreate() {
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        Button add = new Button(this);
        add.setText("Add new line");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLine();
            }
        });
        ll.addView(add);
        Button but = new Button(this);
        but.setText("Save");
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                finish();
            }
        });
        ll.addView(but);
        setContentView(ll);
    }
    private void saveData() {
        ArrayList<Integer> start = new ArrayList<Integer>();
        ArrayList<Integer> end = new ArrayList<Integer>();
        for (int x = 0; x < ll.getChildCount()-2; x++) {
            LinearLayout hold = (LinearLayout)ll.getChildAt(x);
            Spinner a = (Spinner)hold.getChildAt(0);
            Spinner b = (Spinner)hold.getChildAt(1);
            Log.e("!!", "" + a.getSelectedItemPosition());
            start.add(a.getSelectedItemPosition());
            end.add(b.getSelectedItemPosition());
        }
        Intent intent = new Intent(this, schemaBuilder.class);
        intent.putIntegerArrayListExtra("start", start);
        intent.putIntegerArrayListExtra("end", end);
        startActivity(intent);
    }
    private void addLine() {
        LinearLayout cur = new LinearLayout(this);
        Spinner from = new Spinner(this);
        ArrayAdapter spinAdapt1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fromEntries);
        from.setAdapter(spinAdapt1);
        Spinner to = new Spinner(this);
        ArrayAdapter spinAdapt2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fromEntries);
        to.setAdapter(spinAdapt2);
        cur.addView(from);
        cur.addView(to);
        Button del = new Button(this);
        del.setText("X");
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout child = ((LinearLayout) (v.getParent()));
                int index = ((LinearLayout) child.getParent()).indexOfChild(child);
                ll.removeViewAt(index);
            }
        });
        cur.addView(del);
        ll.addView(cur, ll.getChildCount()-2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schema_builder_map, menu);
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
