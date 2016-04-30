package pwgame.passwordgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


public class search extends Activity {

    LinearLayout results;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setWeightSum(1.0f);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout sea = new LinearLayout(this);
        sea.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0 , 0.1f));
        sea.setWeightSum(1.0f);
        search = new EditText(this);
        search.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f));
        sea.addView(search);
        Button sear = new Button(this);
        sear.setText("Search");
        sear.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f));
        sear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        sea.addView(sear);
        ll.addView(sea);
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.9f));
        results = new LinearLayout(this);
        sv.addView(results);
        ll.addView(sv);
        setContentView(ll);
    }
    ArrayList<DBPasswordData> all;
    String query;
    private void doSearch() {
        query = search.getText().toString();
        Firebase.setAndroidContext(this);
        Firebase root = new Firebase("https://passwordgame.firebaseio.com/");
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> ds = dataSnapshot.getChildren().iterator();
                all = new ArrayList<DBPasswordData>();
                Log.e("!", "search pulled");
                while (ds.hasNext()) {
                    DBPasswordData cg = ds.next().getValue(DBPasswordData.class);
                    if (cg.getName().contains(query)) {
                        all.add(cg);
                        Log.e("!", "ADDED");
                    }
                    Log.e("!", cg.getName() + "");
                }
                updateResults();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void updateResults() {
        results.removeAllViews();
        results.setOrientation(LinearLayout.VERTICAL);
        results.setWeightSum(1.0f);
        for (int x = 0; x < all.size(); x++) {
            LinearLayout temp = new LinearLayout(this);

            temp.setBackgroundResource(R.drawable.layout_bg);
            temp.setWeightSum(1.0f);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));

            TextView t = new TextView(this);
            t.setText(all.get(x).getName());
            t.setTextColor(Color.BLACK);
            t.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f));
            t.setGravity(Gravity.CENTER);
            temp.addView(t);
            Button bt1 = new Button(this);
            bt1.setText("Play");
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("!!", "clicked");
                    LinearLayout first = ((LinearLayout) v.getParent());
                    int index = ((LinearLayout) first.getParent()).indexOfChild(first);
                    Log.e("!!", index + "");
                    Intent intent = new Intent(v.getContext(), levelActivity.class);
                    Log.e("!", all.get(index).getInstructions());
                    Log.e("!", all.get(index).getMap());
                    intent.putExtra("PasswordData", all.get(index).createPasswordData());
                    intent.putExtra("level", 10);
                    startActivity(intent);
                }
            });
            bt1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
            temp.addView(bt1);
            Button bt2 = new Button(this);
            bt2.setText("Edit");
            bt2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("!!", "clicked");
                    LinearLayout first = ((LinearLayout) v.getParent());
                    int index = ((LinearLayout) first.getParent()).indexOfChild(first);
                    Log.e("!!", index + "");
                    Intent intent = new Intent(v.getContext(), schemaBuilder.class);
                    Log.e("!", all.get(index).getInstructions());
                    Log.e("!", all.get(index).getMap());
                    DBPasswordData cur = all.get(index);
                    ArrayList<Integer> start = new ArrayList<Integer>();
                    ArrayList<Integer> end = new ArrayList<Integer>();
                    ArrayList<String> instr = new ArrayList<String>();
                    StringTokenizer s1 = new StringTokenizer(cur.getMap(), ",");
                    while (s1.hasMoreTokens()) {
                        start.add(Integer.parseInt(s1.nextToken()));
                        end.add(Integer.parseInt(s1.nextToken()));
                    }
                    s1 = new StringTokenizer(cur.getInstructions(), ",");
                    while (s1.hasMoreElements()) {
                        instr.add(s1.nextToken());
                    }
                    intent.putIntegerArrayListExtra("start", start);
                    intent.putIntegerArrayListExtra("end", end);
                    intent.putStringArrayListExtra("instr", instr);

                    //.putExtra("PasswordData", all.get(index).createPasswordData());
                    //intent.putExtra("level", 10);
                    startActivity(intent);
                }
            });
            temp.addView(bt2);

            results.addView(temp);
        }
    }
}
