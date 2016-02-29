package pwgame.passwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class postLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int levelNum = getIntent().getExtras().getInt("levelNumber");
        PasswordData pwd = getIntent().<PasswordData>getParcelableArrayListExtra("pwd").get(levelNum);
        ArrayList<String> challenges = getIntent().getStringArrayListExtra("challenges");
        int score = challenges.size();
        SharedPreferences sp = getSharedPreferences("scores", 0);
        if (sp.contains("level"+levelNum)){
            int a = sp.getInt("level"+levelNum, 1000000);
            if (score < a) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("level"+levelNum, score);
                editor.commit();
            }
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("level"+levelNum, score);
            editor.commit();
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("lastLevel", levelNum);
        editor.commit();
        LinearLayout ll = new LinearLayout(this);
        ll.setWeightSum(1.0f);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView top = new TextView(this);
        top.setText("Level " + (levelNum + 1) + ",   Score:" + score + "   Par Score:" + pwd.getQ());
        top.setTextSize(18);
        top.setTextColor(Color.BLUE);
        top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        ll.addView(top);

        TextView congrats = new TextView(this);
        congrats.setText("Well Done!!");
        congrats.setTextColor(Color.RED);
        congrats.setTextSize(20);
        congrats.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        ll.addView(congrats);

        TextView desc = new TextView(this);
        desc.setText(pwd.getDescription());
        desc.setTextSize(18);
        desc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.2f));
        ll.addView(desc);

        LinearLayout outerLin = new LinearLayout(this);
        outerLin.setOrientation(LinearLayout.VERTICAL);
        LinearLayout addz = new LinearLayout(this);
        addz.setWeightSum(3.0f);

        Button add11 = new Button(this);
        add11.setText("Challenge");
        add11.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        addz.addView(add11);

        Button add12 = new Button(this);
        add12.setText("Guess");
        add12.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        addz.addView(add12);

        Button add13 = new Button(this);
        add13.setText("Response");
        add13.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        addz.addView(add13);

        outerLin.addView(addz);

        ScrollView sv = new ScrollView(this);
        LinearLayout in = new LinearLayout(this);
        in.setOrientation(LinearLayout.VERTICAL);
        ArrayList<String> guesses = getIntent().getStringArrayListExtra("guesses");
        ArrayList<String> answers = getIntent().getStringArrayListExtra("answers");
        for (int x = 0; x < challenges.size(); x++) {
            LinearLayout add = new LinearLayout(this);
            add.setWeightSum(3.0f);

            Button add1 = new Button(this);
            add1.setText(challenges.get(x));
            add1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add1.setTransformationMethod(null);
            add.addView(add1);

            Button add2 = new Button(this);
            add2.setText(guesses.get(x));
            add2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add2.setTransformationMethod(null);
            add.addView(add2);

            Button add3 = new Button(this);
            add3.setText(answers.get(x));
            add3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add3.setTransformationMethod(null);
            add.addView(add3);

            in.addView(add);
        }
        sv.addView(in);
        outerLin.addView(sv);
        outerLin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
        ll.addView(outerLin);

        Button nextLevel = new Button(this);
        nextLevel.setText("Next Level");
        nextLevel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        nextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PasswordData> arr = getIntent().<PasswordData>getParcelableArrayListExtra("pwd");
                int levelNum = getIntent().getExtras().getInt("levelNumber")+1;
                Log.e("Check", levelNum + " " + arr.size());
                if (levelNum >= arr.size()) {
                    Intent intent = new Intent(v.getContext(), endGame.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(v.getContext(), levelActivity.class);
                    intent.putParcelableArrayListExtra("pwd", arr);
                    intent.putExtra("levelNum", getIntent().getExtras().getInt("levelNumber") + 1);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ll.addView(nextLevel);

        Button feedback = new Button(this);
        feedback.setText("Send Feedback");
        feedback.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"akang31@gatech.edu", "vempala@cc.gatech.edu"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Password Game: Level " + (getIntent().getExtras().getInt("levelNumber")+1) + " feedback");
                try {
                    startActivity(Intent.createChooser(intent, "Send feedback"));
                } catch (Exception e) {
                    Toast.makeText(postLevelActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll.addView(feedback);

        setContentView(ll);
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

        Intent intent = new Intent(this, introduction.class);
        intent.putParcelableArrayListExtra("pwd",getIntent().<PasswordData>getParcelableArrayListExtra("pwd"));
        startActivity(intent);
        finish();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
