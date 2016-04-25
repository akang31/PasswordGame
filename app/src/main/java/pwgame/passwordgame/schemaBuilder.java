package pwgame.passwordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class schemaBuilder extends AppCompatActivity {

    private HashMap<String, String> map;
    private ArrayList<String> rules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainMenuCreate();
        //setContentView(R.layout.activity_schema_builder);
    }
    EditText ed;
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
        ll.addView(toMap);
        ll.addView(toSchema);
        ed = new EditText(this);
        ll.addView(ed);
        Firebase.setAndroidContext(this);
        Button saveToDB = new Button(this);
        saveToDB.setText("Save to Database");
        saveToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase("https://passwordgame.firebaseio.com");
                if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("start") && getIntent().getExtras().containsKey("instr")) {
                    ArrayList<Integer> start = getIntent().getIntegerArrayListExtra("start");
                    ArrayList<Integer> end = getIntent().getIntegerArrayListExtra("end");
                    ArrayList<String> instr = getIntent().getStringArrayListExtra("instr");
                    String a1 = "";
                    for (int x = 0; x < start.size(); x++) {
                        a1 += start.get(x) + "," + end.get(x);
                        if (x != start.size()-1) {
                            a1 += ",";
                        }
                    }
                    String a2 = "";
                    for (int x = 0; x < instr.size()-1; x++) {
                        a2 += instr.get(x)+",";
                    }
                    a2 += instr.get(instr.size()-1);
                    DBPasswordData dbpwd = new DBPasswordData(ed.getText().toString(), a1, a2);
                    Firebase temp = ref.push();
                    temp.setValue(dbpwd);
                    finish();
                }

            }
        });
        ll.addView(saveToDB);
        setContentView(ll);
    }

    private void mapMenuCreate() {
        Intent intent = new Intent(this, schemaBuilderMap.class);
        //include map
        startActivity(intent);
        finish();
    }

    private void schemaMenuCreate() {
        Intent intent = new Intent(this,schemaBuilderInstructions.class);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("start")) {
            intent.putIntegerArrayListExtra("start",getIntent().getIntegerArrayListExtra("start"));
            intent.putIntegerArrayListExtra("end",getIntent().getIntegerArrayListExtra("end"));
        }
        startActivity(intent);
        finish();
/**
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
 **/
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
