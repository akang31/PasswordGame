package pwgame.passwordgame;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

public class levelActivity extends AppCompatActivity {

    private TextView challenge;
    private EditText guess;
    private LinearLayout history;
    private PasswordData pwd;
    private int levelNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwd = getIntent().getExtras().getParcelable("PasswordData");
        levelNum = getIntent().getExtras().getInt("level")+1;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setWeightSum(2.0f);
        TextView fill1 = new TextView(this);
        fill1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.2f));
        ll.addView(fill1);
        TextView head = new TextView(this);
        head.setText("Level " + levelNum);
        head.setGravity(Gravity.CENTER);
        head.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.2f));
        ll.addView(head);
        TextView fill2 = new TextView(this);
        fill2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.3f));
        ll.addView(fill2);
        TextView hint = new TextView(this);
        hint.setText("SAMPLE --> " + getPassword("SAMPLE"));
        hint.setGravity(Gravity.CENTER);
        hint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.4f));
        ll.addView(hint);
        TextView fill3 = new TextView(this);
        fill3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
        ll.addView(fill3);
        Button GO = new Button(this);
        GO.setText("START");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0.2f);
        lp.gravity = Gravity.CENTER;
        GO.setLayoutParams(lp);
        GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createScreen();
            }
        });
        GO.setGravity(Gravity.CENTER);
        ll.addView(GO);
        TextView fill4 = new TextView(this);
        fill4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.2f));
        ll.addView(fill4);
        setContentView(ll);

        //setContentView(R.layout.activity_level);
    }
    private void createScreen() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setWeightSum(2.0f);
        //Upper Half: Challenge, Response Area, Button
        LinearLayout uh = new LinearLayout(this);
        uh.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
        uh.setWeightSum(2.0f);
        uh.setOrientation(LinearLayout.VERTICAL);
        challenge = new TextView(this);
        Collections.shuffle(Arrays.asList(pw));
        challenge.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
        challenge.setText(pw[index++]);
        challenge.setTextSize(30);
        challenge.setTypeface(null, Typeface.BOLD);
        challenge.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        uh.addView(challenge);
        guess = new EditText(this);
        guess.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.6f));
        uh.addView(guess);
        Button tryButton = new Button(this);
        tryButton.setText("Guess");
        tryButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.4f));
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        uh.addView(tryButton);
        ll.addView(uh);
        //Lower Half: History of Challenges, Attempts, and Responses
        LinearLayout lh = new LinearLayout(this);
        lh.setOrientation(LinearLayout.VERTICAL);
        lh.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
        lh.setWeightSum(5.0f);
        LinearLayout labelbar = new LinearLayout(this);
        labelbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
        labelbar.setWeightSum(3.0f);
        Button text1 = new Button(this);
        text1.setText("Challenge");
        text1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        Button text2 = new Button(this);
        text2.setText("Guess");
        text2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        Button text3 = new Button(this);
        text3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        text3.setText("Response");
        labelbar.addView(text1);
        labelbar.addView(text2);
        labelbar.addView(text3);
        lh.addView(labelbar);
        ScrollView sc = new ScrollView(this);
        history = new LinearLayout(this);
        history.setOrientation(LinearLayout.VERTICAL);
        sc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4.0f));
        sc.addView(history);
        lh.addView(sc);
        ll.addView(lh);
        setContentView(ll);
    }
    private int index = 0;
    private String[] pw = new String[]{"AMAZON", "FACEBOOK", "DROPBOX", "MSN", "INSTAGRAM", "ALIEXPRESS",
            "GOOGLE", "YOUTUBE", "BAIDU", "YAHOO", "WIKIPEDIA", "TENCENT", "TWITTER","TAOBAO", "LIVE",
            "SINA", "LINKEDIN", "EBAY", "YANDEX", "HAO", "BING", "APPLE", "BLOGSPOT", "ASK", "PINTEREST",
            "TMALL", "REDDIT", "MAILRU", "SOHU", "TUMBLR", "IMGUR", "MICROSOFT", "NETFLIX", "STACK", "CRAIGSLIST"};
    private void reset() {
        String curChal = challenge.getText().toString();
        String response = getPassword(curChal);
        String theirGuess = guess.getText().toString();
        if (response.equals(theirGuess)) {
            finish();
        }
        challenge.setText(pw[index++]);
        index %= pw.length;
        guess.setText("");
        LinearLayout add = new LinearLayout(this);
        add.setWeightSum(3.0f);
        Button add1 = new Button(this);
        add1.setText(curChal);
        add1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        Button add2 = new Button(this);
        add2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        add2.setText(theirGuess);
        add2.setTransformationMethod(null);
        Button add3 = new Button(this);
        add3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), traceDisplayActivity.class);
                intent.putExtra("trace", pwd.getTrace());
                startActivity(intent);
            }
        });
        add3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        add3.setTransformationMethod(null);
        add3.setText(response);
        add.addView(add1);
        add.addView(add2);
        add.addView(add3);
        history.addView(add);
    }
    private String getPassword(String chal) {
        return pwd.getPassword(chal, false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_level, menu);
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
