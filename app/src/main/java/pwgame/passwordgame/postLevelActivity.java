package pwgame.passwordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        int score = 1;
        LinearLayout ll = new LinearLayout(this);
        ll.setWeightSum(1.0f);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView top = new TextView(this);
        top.setText("Level " + (levelNum + 1) + ",   Score:" + score + "   Q:" + pwd.getQ());
        top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        ll.addView(top);

        TextView desc = new TextView(this);
        desc.setText(pwd.getDescription());
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
        ArrayList<String> challenges = getIntent().getStringArrayListExtra("challenges");
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
                Intent intent = new Intent(v.getContext(), levelActivity.class);
                intent.putParcelableArrayListExtra("pwd", getIntent().<PasswordData>getParcelableArrayListExtra("pwd"));
                intent.putExtra("levelNum", getIntent().getExtras().getInt("levelNumber") + 1);
                startActivity(intent);
                finish();
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Level " + (getIntent().getExtras().getInt("levelNumber")+1) + " feedback");
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
        getMenuInflater().inflate(R.menu.menu_post_level, menu);
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
