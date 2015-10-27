package pwgame.passwordgame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class gamePortal extends AppCompatActivity {

    private class levelButton extends Button {
        private PasswordData pdata;
        public levelButton(Context ctxt) {
            super(ctxt);
        }
        public levelButton(Context ctxt, String text, PasswordData pdata) {
            super(ctxt);
            setText(text);
            this.pdata = pdata;
            setOnClickListener(new clickListener(pdata));
        }
        private class clickListener implements OnClickListener {
            private PasswordData pd;
            public clickListener(PasswordData pd) {
                this.pd = pd;
            }
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), levelActivity.class);
                intent.putExtra("PasswordData", pd);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int rows = 5;
        int cols = 5;
        PasswordData[] pwd = new PasswordData[rows*cols];
        createLevels(pwd);
        LinearLayout overall = new LinearLayout(this);
        overall.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout temp = new RelativeLayout(this);
        TextView txt = new TextView(this);
        txt.setText("LEVELS");
        temp.setGravity(Gravity.CENTER);
        temp.addView(txt);
        overall.addView(temp);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setWeightSum(rows*1.0f);
        for (int x = 0; x < rows; x++) {
            LinearLayout tl = new LinearLayout(this);
            tl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
            tl.setWeightSum(1.0f);
            for (int y = 0; y < cols; y++) {
                levelButton a = new levelButton(this, (x*cols+y+1)+"",pwd[x*cols+y]);
                int id = View.generateViewId();
                a.setId(id);
                LinearLayout.LayoutParams set = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f/cols);
                a.setLayoutParams(set);
                tl.addView(a);
            }
            ll.addView(tl);
        }
        overall.addView(ll);
        setContentView(overall);

//        setContentView(R.layout.activity_game_portal);
    }

    private void createLevels(PasswordData[] pwd) {
        //HashMap<String, String> map, String[] instructions

        //Case 1: Alphabet permutation of off by one
        HashMap<String, String> map1 = new HashMap<String, String>();
        for (int x = 0; x < 25; x++) {
            map1.put(""+(char)('A'+x), ""+(char)('A'+x+1));
        }
        map1.put("Z", "A");
        String[] instr1 = new String[]{"MAP cc 0", "OUTPUT 0"};
        pwd[0] = new PasswordData(map1, instr1);

        //Case 2: Alphabet permutation off by two
        HashMap<String, String> map2 = new HashMap<String, String>();
        for (int x = 0; x < 24; x++) {
            map2.put(""+(char)('A'+x), ""+(char)('A'+x+2));
        }
        map2.put("Y", "A");
        map2.put("Z", "B");
        String[] instr2 = new String[]{"MAP cc 0", "OUTPUT 0"};
        pwd[1] = new PasswordData(map2, instr2);

        //Case 3: Partial Alphabet Mapping
        HashMap<String, String> map3 = new HashMap<String, String>();
        for (int x = 0; x < 10; x++) {
            map3.put(""+(char)('A'+x), ""+(char)('A'+x+5));
        }
        String[] instr3 = new String[]{"MAP cc 0", "OUTPUT 0"};
        pwd[2] = new PasswordData(map3, instr3);


        //Case 4: Partial Alphabet to Digit Mapping
        HashMap<String, String> map4 = new HashMap<String, String>();
        for (int x = 0; x < 10; x++) {
            map4.put(""+(char)('A'+x), x+"");
        }
        String[] instr4 = new String[]{"MAP cc 0", "OUTPUT 0"};
        pwd[3] = new PasswordData(map4, instr4);

        //Case 5: Running Sum mod 10
        HashMap<String, String> map5 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map5.put(""+(char)('A'+x), ""+x%10);
        }
        String[] instr5 = new String[]{"MAP cc 0", "SET 0 0 + ps m10", "OUTPUT 0", "PASS 0"};
        pwd[4] = new PasswordData(map5, instr5);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_portal, menu);
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
