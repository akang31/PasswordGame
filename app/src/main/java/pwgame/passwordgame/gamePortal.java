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
import java.util.ArrayList;
import java.util.HashMap;

public class gamePortal extends AppCompatActivity {

    private class levelButton extends Button {
        private PasswordData pdata;
        private int level;
        public levelButton(Context ctxt) {
            super(ctxt);
        }
        public levelButton(Context ctxt, String text, PasswordData pdata, int level) {
            super(ctxt);
            setText(text);
            this.level=level;
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
                //intent.putExtra("PasswordData", pd);
                //intent.putExtra("level", level);
                intent.putParcelableArrayListExtra("pwd", all);
                intent.putExtra("levelNum", level);
                startActivity(intent);
            }
        }
    }

    protected ArrayList<PasswordData> all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int rows = 5;
        int cols = 5;
        all = getIntent().<PasswordData>getParcelableArrayListExtra("pwd");

        PasswordData[] pwd = new PasswordData[rows*cols];
       // createLevels(pwd);
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
                levelButton a = new levelButton(this, (x*cols+y+1)+"",pwd[x*cols+y], x*cols+y);
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
