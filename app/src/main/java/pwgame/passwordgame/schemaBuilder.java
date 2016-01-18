package pwgame.passwordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class schemaBuilder extends AppCompatActivity {

    private HashMap<String, String> map;
    private ArrayList<String> rules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = new HashMap<String, String>();
        rules = new ArrayList<String>();
        rules.add("MAP cc 0");
        rules.add("OUTPUT 0");
        mainMenuCreate();
        //setContentView(R.layout.activity_schema_builder);
    }

    private void mainMenuCreate() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        Button toMap = new Button(this);
        toMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mapMenuCreate();
            }
        });
        toMap.setText("Edit Mapping");
        Button toSchema = new Button(this);
        toSchema.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               schemaMenuCreate();
           }
        });
        toSchema.setText("Edit Rules");
        RelativeLayout t1 = new RelativeLayout(this);
        t1.setGravity(Gravity.CENTER);
        RelativeLayout t2 = new RelativeLayout(this);
        t2.setGravity(Gravity.CENTER);
        t1.addView(toMap);
        t2.addView(toSchema);
        ll.addView(t1);
        ll.addView(t2);
        setContentView(ll);
    }

    private void mapMenuCreate() {
        LinearLayout ll = new LinearLayout(this);
        Spinner from = new Spinner(this);
        String[] fromEntries = new String[]{"A-Z", "0-9"};
        ArrayAdapter spinAdapt1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fromEntries);
        from.setAdapter(spinAdapt1);
        Spinner to = new Spinner(this);
        String[] toEntries = new String[]{"A-Z", "0-9"};
        ArrayAdapter spinAdapt2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, toEntries);
        to.setAdapter(spinAdapt2);
        ll.addView(from);
        ll.addView(to);
        Button but = new Button(this);
        but.setText("Save");
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenuCreate();
            }
        });
        ll.addView(but);
        setContentView(ll);
    }

    private void schemaMenuCreate() {
        LinearLayout overall = new LinearLayout(this);
        overall.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(this);
        tv.setText("YOUR SCHEMA");
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        rules.add("ADD NEW RULE");
        rules.add("SAVE");
        for (int x = 0; x < rules.size(); x++) {
            Button b = new Button(this);
            b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            b.setText(rules.get(x));
            if (rules.get(x).equals("SAVE")) {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainMenuCreate();
                    }
                });
            } else if (rules.get(x).equals("ADD NEW RULE")) {

            } else {

            }
            ll.addView(b);
        }
        rules.remove(rules.size()-1);
        rules.remove(rules.size()-1);
        overall.addView(ll);
        setContentView(overall);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schema_builder, menu);
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
