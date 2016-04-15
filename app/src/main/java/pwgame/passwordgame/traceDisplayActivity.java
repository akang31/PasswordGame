package pwgame.passwordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class traceDisplayActivity extends AppCompatActivity {

    private PasswordTrace trace;
    private class SwitchBackListener implements View.OnClickListener {
        private int index;
        private String left,right;
        private ArrayList<String> mem;
        private ArrayList<Integer> ind;
        public SwitchBackListener(int index, ArrayList<String> mem, ArrayList<Integer> ind, String left, String right) {
            this.index=index;
            this.mem=mem;
            this.ind=ind;
            this.left=left;
            this.right=right;
        }

        @Override
        public void onClick(View v) {
            LinearLayout temp = new LinearLayout(v.getContext());
            temp.setWeightSum(2.0f);
            Button add = new Button(v.getContext());
            add.setText(left);
            add.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add.setOnClickListener(new SwitchListener(index, mem, ind, left, right));
            add.setTransformationMethod(null);
            temp.addView(add);
            add = new Button(v.getContext());
            add.setText(right);
            add.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add.setOnClickListener(new SwitchListener(index, mem, ind, left, right));
            add.setTransformationMethod(null);
            temp.addView(add);
            ll.removeViewAt(index);
            ll.addView(temp, index);
        }
    }
    private class SwitchListener implements View.OnClickListener {
        private int index;
        private String left,right;
        private ArrayList<String> mem;
        private ArrayList<Integer> ind;
        public SwitchListener(int index, ArrayList<String> mem, ArrayList<Integer> ind, String left, String right) {
            this.index=index;
            this.mem=mem;
            this.ind=ind;
            this.left=left;
            this.right=right;
        }
        @Override
        public void onClick(View v) {
            ll.removeViewAt(index);
            LinearLayout add = new LinearLayout(v.getContext());
            add.setWeightSum(mem.size()*1.0f);
            Log.e("SIZE", mem.size()+"");
            for (int x = 0; x < mem.size(); x++) {
                Button add1 = new Button(v.getContext());
                String txt = "";
                if (mem.get(x) == null || ind.get(x) < 0 || ind.get(x) >= mem.get(x).length()) {
                    txt = mem.get(x);
                    add1.setText(txt);
                } else {
                    txt = mem.get(x).substring(0, ind.get(x)) + "<big><b>" + mem.get(x).charAt(ind.get(x)) + "</b></big>" + mem.get(x).substring(ind.get(x)+1);
                    add1.setText(Html.fromHtml(txt));
                }
                add1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                add1.setOnClickListener(new SwitchBackListener(index, mem, ind, left, right));
                add1.setTransformationMethod(null);
                add.addView(add1);
            }
            ll.addView(add, index);
        }
    }
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trace = getIntent().getExtras().getParcelable("trace");
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);


        LinearLayout temp1 = new LinearLayout(this);
        temp1.setWeightSum(2.0f);
        TextView a3 = new TextView(this);
        a3.setText("HUM Value");
        a3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        TextView a4 = new TextView(this);
        int mod = trace.getHUM()%trace.getChallenge().length();
        //a4.setText(trace.getHUM() + " = " + trace.getHUM()/trace.getChallenge().length()+"n" + (mod==0?"":"+"+mod));
        a4.setText(trace.getHUM()+"");
        a4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        a3.setGravity(Gravity.CENTER);
        a4.setGravity(Gravity.CENTER);
        temp1.addView(a3);
        temp1.addView(a4);
        ll.addView(temp1);

        LinearLayout temp = new LinearLayout(this);
        temp.setWeightSum(2.0f);
        TextView a1 = new TextView(this);
        a1.setText("Instruction");
        a1.setGravity(Gravity.CENTER);
        a1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        TextView a2 = new TextView(this);
        a2.setText("Output");
        a2.setGravity(Gravity.CENTER);
        a2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        temp.addView(a1);
        temp.addView(a2);
        ll.addView(temp);



        for (int x = 0; x < trace.getOutputTrace().size(); x++) {
            temp = new LinearLayout(this);
            temp.setWeightSum(2.0f);
            Button add = new Button(this);
            add.setText(trace.getTrace().get(x));
            add.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add.setOnClickListener(new SwitchListener(x+2, trace.getMemoryTrace().get(x), trace.getPointerTrace().get(x), trace.getTrace().get(x), trace.getOutputTrace().get(x)));
            add.setTransformationMethod(null);
            temp.addView(add);
/*
            for (int y = 0; y < trace.getMemoryTrace().get(x).size(); y++) {
                add = new TextView(this);
                add.setText(trace.getMemoryTrace().get(x).get(y));
                temp.addView(add);
            }
*/
            add = new Button(this);
            add.setText(trace.getOutputTrace().get(x));
            add.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add.setOnClickListener(new SwitchListener(x+2, trace.getMemoryTrace().get(x), trace.getPointerTrace().get(x), trace.getTrace().get(x), trace.getOutputTrace().get(x)));
            add.setTransformationMethod(null);
            temp.addView(add);

            ll.addView(temp);
        }


        ScrollView sv = new ScrollView(this);
        sv.addView(ll);
        setContentView(sv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trace_display, menu);
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
