package pwgame.passwordgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class schemaBuilderInstructions extends AppCompatActivity {
    EditText test;
    LinearLayout text;
    HashMap<String, String> map;
    PasswordTrace trace = null;
    int[][] mapMaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ArrayList<ArrayList<Integer>>
        // ["MAP", "SET", "GOTO", "LABEL", "IF", "SHIFT", "PARSE", "END", "OUTPUT"]
        /**
         * [index, indexOfVariable*, String, statement(IF), index or #string]
         * MAP = [
         *
         */
        mapMaker = new int[26+2+10][2];
        mapMaker[0] = new int[]{'A', 26};
        mapMaker[1] = new int[]{'0', 10};
        for (int x = 0;x < 26; x++) {
            mapMaker[x+2] = new int[]{'A'+x, 1};
        }
        for (int x = 0; x < 10; x++) {
            mapMaker[x+2+26] = new int[]{'0'+x, 1};
        }
        super.onCreate(savedInstanceState);
        map = new HashMap<String, String>();
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("start")) {
            ArrayList<Integer> start = getIntent().getIntegerArrayListExtra("start");
            ArrayList<Integer> end = getIntent().getIntegerArrayListExtra("end");
            for (int x = 0; x < start.size(); x++) {
                for (int y = 0; y < mapMaker[start.get(x)][1]; y++) {
                    map.put(""+(char)(mapMaker[start.get(x)][0]+y), (char)(mapMaker[end.get(x)][0]+(int)(Math.random()*mapMaker[end.get(x)][1]))+"");
                }
            }
            for (String s : map.keySet()) {
                Log.e("!",s + ": " + map.get(s));
            }
        } else {
            for (int x = 0; x < 26; x++) {
                map.put((char) (x + 'A') + "", x % 10 + "");
            }
        }
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setWeightSum(1.0f);
        //text = new EditText(this);
        text = new LinearLayout(this);
        text.setOrientation(LinearLayout.VERTICAL);
        text.setGravity(Gravity.NO_GRAVITY);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.8f));
        Button add = new Button(this);
        add.setText("Add new line");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLine(text.getChildCount() - 1);
            }
        });
        text.addView(add);
        sv.addView(text);
        ll.addView(sv);
        test = new EditText(this);
        test.setGravity(Gravity.NO_GRAVITY);
        test.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        ll.addView(test);
        LinearLayout hold = new LinearLayout(this);
        hold.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f));
        hold.setWeightSum(1.0f);
        Button compile = new Button(this);
        compile.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f));
        compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compileAndTest();
            }
        });
        compile.setText("Test");
        Button trace = new Button(this);
        trace.setText("See Trace");
        trace.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f));
        trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTrace();
            }
        });
        hold.addView(compile);
        hold.addView(trace);
        ll.addView(hold);
        setContentView(ll);
    }
    private void updateLineNumbers() {
        for (int x = 0; x < text.getChildCount()-1; x++) {
            ((TextView)((LinearLayout)text.getChildAt(x)).getChildAt(0)).setText((x+1)+"");
        }
    }
    String[] manual;
    private void addLine(int loc) {
        LinearLayout ll = new LinearLayout(this);
        ll.setWeightSum(1.0f);
        TextView line = new TextView(this);
        line.setText((loc+1)+"");
        line.setGravity(Gravity.CENTER);
        line.setTextColor(Color.BLACK);
        line.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.05f));
        ll.addView(line);
        Spinner from = new Spinner(this);
        String[] fromEntries = new String[]{"MAP", "SET", "LABEL", "IF", "SHIFT", "PARSE", "OUTPUT", "LOOPSTART", "LOOPEND", "END"};
        manual = new String[]{"MAP [indexOfVariable or #STRING] [indexOfVariable]\n" +
                "If no valid mapping exists, simply set mem to empty string and indices to -2\n" +
                "Put mapping of FIRST var into SECOND var", "Set a variable to an expression: SET [indexOfVariable] [expression]\n" +
                "This works either character by character or on an integer basis.\n" +
                "e.g. With indices = {0,0} and mem = {\"ASD\", \"FGH\"}\n" +
                "SET 0 1 will result in mem = {\"FSD\", \"FGH\"}\n" +
                "Valid expressions:\n" +
                "[indexOfVariable*]\n" +
                "[indexOfVariable*] {+,-,*} [indexOfVariable*]\n" +
                "[indexOfVariable*] {+,-,*} [indexOfVariable*] [mX]  (where X is a number to mod by)\n" +
                "indexOfVariable*:\n" +
                "\"[0-9]+\"  = direct index and assumes it is the CHARACTER at the index specified by indices\n" +
                "\"&[0-9]+\" = the value in indices, the pointer index\n" +
                "\"#[0-9]+\" = direct number input\n" +
                "\"@[0-9]+\" = number at index (instead of whole number)", "Create a label: LABEL [name]", "IF [statement] [label]\n" +
                "Valid statements:\n" +
                "EQ [indexOfVariable*] [indexOfVariable*]\n" +
                "EOS [indexOfVariable] : checks if it is the end of string\n" +
                "VOWEL [indexOfVariable] : checks if the character is a vowel\n" +
                "![statement] : negate", "Shift a pointer in memory: SHIFT [indexOfVariable] [number shifted, signed] [wrap]\n" +
                "if anything is put in for wrap, the shift will wrap around.", "PARSE [indexOfVariable]: convert to int",
                "OUTPUT [indexOfVariable]: print character at index", "LOOPSTART [name]: start of a loop", "LOOPEND [name] [CONDITIONAL{if}]", "END: ends program"};
        ArrayAdapter spinAdapt1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fromEntries);
        from.setAdapter(spinAdapt1);
        from.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.3f));
        ll.addView(from);
        EditText edit = new EditText(this);
        edit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.35f));
        ll.addView(edit);
        Button man = new Button(this);
        man.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f));
        man.setText("?");
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout top = (LinearLayout)v.getParent();
                Spinner s = (Spinner)top.getChildAt(1);
                int index = s.getSelectedItemPosition();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(v.getContext(), manual[index], duration);
                toast.show();
            }
        });
        ll.addView(man);
        Button add = new Button(this);
        add.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f));
        add.setText("+");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout child = ((LinearLayout)(v.getParent()));
                int index = ((LinearLayout)child.getParent()).indexOfChild(child);
                addLine(index);
                updateLineNumbers();
            }
        });
        //ll.getChild
        ll.addView(add);
        Button remove = new Button(this);
        remove.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f));
        remove.setText("X");
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout child = ((LinearLayout) (v.getParent()));
                int index = ((LinearLayout) child.getParent()).indexOfChild(child);
                text.removeViewAt(index);
                updateLineNumbers();
            }
        });
        ll.addView(remove);
        text.addView(ll, loc);
    }
    private void compileAndTest() {
        //String txt = text.getText().toString();
        String txt = "";
        String[] instr = new String[text.getChildCount()-1];
        for (int x = 0; x < instr.length; x++) {
            instr[x] = ((Spinner)((LinearLayout)text.getChildAt(x)).getChildAt(1)).getSelectedItem().toString()+" ";
            instr[x] +=((EditText)((LinearLayout)text.getChildAt(x)).getChildAt(2)).getText().toString();
            instr[x].trim();
            Log.e("SDFSDF", instr[x]);
        }
        PasswordData data = new PasswordData(map,instr);
        String pass = data.getPassword(test.getText().toString(), true);
        trace = data.getFullTrace();
        CharSequence text = pass;
        if (pass == "") {
            text = "Error at line " + data.getFailLine();
        }
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }
    private void goTrace() {
        if (trace == null || trace.getMin() == 0) {
            return;
        }
        Intent intent = new Intent(this, traceDisplayActivity.class);
        intent.putExtra("trace", trace);
        startActivity(intent);
    }
    /*
            Variable = 0,1,... indices in string array

            To map a character: MAP [indexOfVariable or #STRING] [indexOfVariable]
                If no valid mapping exists, simply set mem to empty string and indices to -2
                Put mapping of FIRST var into SECOND var

            Set a variable to an expression: SET [indexOfVariable] [expression]
                This works either character by character or on an integer basis.
                    e.g. With indices = {0,0} and mem = {"ASD", "FGH"}
                        SET 0 1 will result in mem = {"FSD", "FGH"}
                Valid expressions:
                    [indexOfVariable*]
                    [indexOfVariable*] {+,-,*} [indexOfVariable*]
                    [indexOfVariable*] {+,-,*} [indexOfVariable*] [mX]  (where X is a number to mod by)
                indexOfVariable*:
                    "[0-9]+"  = direct index and assumes it is the CHARACTER at the index specified by indices
                    "&[0-9]+" = the value in indices, the pointer index
                    "#[0-9]+" = direct number input
                    "@[0-9]+" = number at index (instead of whole number)

                    e.g. if we have indices[0] = 1 and mem[0] = "HELLO", 0 would get us "E" while &0 would get us 1

            Go to some label: GOTO [label]

            Create a label: LABEL [name]

            Basic IF statement functionality: IF [statement] [label]
                Valid statements:
                    EQ [indexOfVariable*] [indexOfVariable*]
                    EOS [indexOfVariable] : checks if it is the end of string
                    VOWEL [indexOfVariable] : checks if the character is a vowel
                    ![statement] : negate

            Shift a pointer in memory: SHIFT [indexOfVariable] [number shifted, signed] [wrap]
                if anything is put in for wrap, the shift will wrap around.

            PARSE [indexOfVariable]: convert to int

            End password schema: END

            OUTPUT [indexOfVariable]

            LOG [indexOfVariable*]
                Debug purposes, print to console.

            Instructions must be space separated.
         */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schema_builder_instructions, menu);
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
