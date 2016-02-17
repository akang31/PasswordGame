package pwgame.passwordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class endGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        String[] text = new String[]{"Congratulations on completing the game!"};
        for (int x = 0;x  < text.length; x++) {
            TextView t = new TextView(this);
            t.setText(text[x]);
            t.setTransformationMethod(null);
            ll.addView(t);
        }
        Button ret = new Button(this);
        ret.setText("Return to Menu");
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), introduction.class);
                startActivity(intent);
            }
        });
        ll.addView(ret);
        Button feedback = new Button(this);
        feedback.setText("Send Feedback");
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"akang31@gatech.edu", "vempala@cc.gatech.edu"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Password Game Feedback");
                try {
                    startActivity(Intent.createChooser(intent, "Send feedback"));
                } catch (Exception e) {
                    Toast.makeText(endGame.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll.addView(feedback);
        setContentView(ll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_game, menu);
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
