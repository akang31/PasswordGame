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
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("start")) {
            ArrayList<Integer> start = getIntent().getIntegerArrayListExtra("start");
            ArrayList<Integer> end = getIntent().getIntegerArrayListExtra("end");
            for (int x = 0; x < start.size(); x++) {
                addLine(start.get(x), end.get(x));
            }
        }
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
                addLine(0,0);
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
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("instr")) {
            intent.putStringArrayListExtra("instr", getIntent().getStringArrayListExtra("instr"));
        }
        startActivity(intent);
    }
    private void addLine(int a, int b) {
        LinearLayout cur = new LinearLayout(this);
        Spinner from = new Spinner(this);
        ArrayAdapter spinAdapt1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fromEntries);
        from.setAdapter(spinAdapt1);
        from.setSelection(a);
        Spinner to = new Spinner(this);
        ArrayAdapter spinAdapt2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fromEntries);
        to.setAdapter(spinAdapt2);
        to.setSelection(b);
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
}
