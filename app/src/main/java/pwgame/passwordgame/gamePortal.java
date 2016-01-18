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

        //Case 1: Fixed Password
        HashMap<String, String> map1 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map1.put(""+(char)('A'+x), "aA1@bB2$");
        }
        String[] instr1 = new String[]{"MAP 0 0", "LABEL START", "OUTPUT 0", "SHIFT 0 1", "IF !EOS 0 START", "END"};
        pwd[0] = new PasswordData(map1, instr1);

        //Case 2: Fixed password + add exact challenge
        HashMap<String, String> map2 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map2.put(""+(char)('A'+x), "aA@1");
        }
        String[] instr2 = new String[]{"MAP 0 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1", "LABEL START2", "OUTPUT 0", "SHIFT 0 1", "IF !EOS 0 START2", "END"};
        pwd[1] = new PasswordData(map2, instr2);

        //Case 3: Add one
        HashMap<String, String> map3 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map3.put(""+(char)('A'+x), ""+(char)('A'+(x+1)%26));
        }
        String[] instr3 = new String[]{"LABEL START", "MAP 0 1", "OUTPUT 1", "SHIFT 0 1", "IF !EOS 0 START", "END"};
        pwd[2] = new PasswordData(map3, instr3);


        //Case 4: Start at Second Vowel
        HashMap<String, String> map4 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map4.put(""+(char)('A'+x), ""+(char)('A'+(x+1)%26));
        }
        String[] instr4 = new String[]{"IF !VOWEL 0 V", "SHIFT 1 1", "LABEL V", "SHIFT 0 1", "IF EOS 0 W-",  "IF !VOWEL 0 V", "SHIFT 1 1", "IF EQ &1 #2 W", "GOTO V",
                                        "LABEL W-", "SHIFT 0 -1 MOD",
                                        "LABEL W", "SHIFT 0 -1 MOD", "SET 2 &0", "PARSE 2", "LOG 2", "SHIFT 0 1 MOD",
                                        "LABEL W+", "MAP 0 1", "OUTPUT 1", "IF EQ &0 2 END", "SHIFT 0 1 MOD", "GOTO W+",
                                        "LABEL END", "END"};
        pwd[3] = new PasswordData(map4, instr4);

        //Case 5: Output first letter after first vowel
        HashMap<String, String> map5 = new HashMap<String, String>();
        String[] instr5 = new String[]{"LABEL START", "IF VOWEL 0 END", "SHIFT 0 1", "IF EOS 0 END2", "GOTO START",
                                        "LABEL END", "SHIFT 0 1 MOD", "OUTPUT 0", "END",
                                        "LABEL END2", "SHIFT 0 -1 MOD", "OUTPUT 0", "END"};
        pwd[4] = new PasswordData(map5, instr5);

        //Case 6: Output first letter whose capitalization contains a vertical line segment
        HashMap<String, String> map6 = new HashMap<String, String>();
        char[] letters = new char[]{'B', 'D', 'E', 'F', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'R', 'T', 'U'};
        for (char a : letters) {
            map6.put(a+"", " ");
        }
        String[] instr6 = new String[]{ "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START", "LABEL END", "SHIFT 0 -1", "OUTPUT 0", "END"};
        pwd[5] = new PasswordData(map6, instr6);

        //Case 7: Output first letter whose capitalization contains a vertical line segment
        HashMap<String, String> map7 = new HashMap<String, String>();
        char[] letters1 = new char[]{'B', 'C', 'D', 'E', 'G', 'P', 'T', 'V', 'Z'};
        for (char a : letters1) {
            map7.put(a+"", " ");
        }
        String[] instr7 = new String[]{ "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START", "LABEL END", "SHIFT 0 -1", "OUTPUT 0", "END"};
        pwd[6] = new PasswordData(map7, instr7);

        // Skip Game 8

        //Case 9:  Telephone number schema
        HashMap<String, String> map9 = new HashMap<String, String>();
        String phonenumber = "4324324324";
        map9.put("@", phonenumber);
        String[] instr9 = new String[]{"MAP #@ 1", "LABEL START", "IF EOS 0 END", "SET 2 @1 + 0", "SHIFT 0 1", "SHIFT 1 1", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[8] = new PasswordData(map9, instr9);

        //Case 10: Wrap aroudn telephone number schema
        HashMap<String, String> map10 = new HashMap<String, String>();
        map10.put("@", phonenumber);
        String[] instr10 = new String[]{"MAP #@ 1", "LABEL START", "IF EOS 1 END", "SET 2 @1 + 0", "SHIFT 0 1 MOD", "SHIFT 1 1", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[9] = new PasswordData(map10, instr10);
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
