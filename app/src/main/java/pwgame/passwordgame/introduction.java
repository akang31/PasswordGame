package pwgame.passwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class introduction extends AppCompatActivity {

    private int levelCount = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] desc = new String[]{"Welcome to the Password Game!"," ", "The purpose of this game is to familiarize you with various methods of generating passwords based on website names",
                "You will be given website names and asked to generate passwords based on the pattern for that level",
                "After trying these levels, we encourage you to create your own games and perhaps even use a schema to replace or improve existing passwords."};
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        for (int x = 0; x < desc.length; x++) {
            TextView t = new TextView(this);
            t.setTextColor(Color.BLACK);
            t.setTextSize(20);
            t.setText(desc[x]);
            ll.addView(t);
        }
        Button go = new Button(this);
        go.setText("Start Playing");
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), levelActivity.class);
                //intent.putParcelableArrayListExtra("pwd", getIntent().<PasswordData>getParcelableArrayListExtra("pwd"));
                PasswordData[] arr = new PasswordData[levelCount];
                createLevels(arr);
                ArrayList<PasswordData> send = new ArrayList<PasswordData>();
                for (int x = 0; x < arr.length; x++) {
                    if (arr[x] != null) {
                        send.add(arr[x]);
                    }
                }
                intent.putParcelableArrayListExtra("pwd", send);
                intent.putExtra("levelNumber", 0);
                startActivity(intent);
                //finish();
            }
        });
        ll.addView(go);
        String[] header = new String[]{"Level1", "Par Score", "High Score"};
        LinearLayout head = new LinearLayout(this);
        head.setWeightSum(3.0f);
        PasswordData[] all = new PasswordData[levelCount];
        createLevels(all);
        for (int x = 0; x < header.length; x++) {
            Button a1 = new Button(this);
            a1.setText("Level " + (x+1));
            a1.setTransformationMethod(null);
            a1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            head.addView(a1);
        }
        ll.addView(head);
        for (int x = 0; x < levelCount; x++) {
            LinearLayout add = new LinearLayout(this);
            add.setWeightSum(3.0f);

            TextView a1 = new TextView(this);
            a1.setText("Level " + (x + 1));
            a1.setTransformationMethod(null);
            a1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add.addView(a1);

            TextView a2 = new TextView(this);
            a2.setText(all[x].getQ()+"");
            a2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            add.addView(a2);

            TextView a3 = new TextView(this);
            SharedPreferences sp = getSharedPreferences("scores", 0);
            if (sp.contains("level"+x)) {
                a3.setText(sp.getInt("level"+x,-1)+"");
            } else {
                a3.setText(" - ");
            }
            add.addView(a3);

            ll.addView(add);
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(ll);
        setContentView(sv);
    }

    private void createLevels(PasswordData[] pwd) {
        //HashMap<String, String> map, String[] instructions

        //Case 1: Fixed Password
        HashMap<String, String> map0 = new HashMap<String, String>();
        map0.put("@", "aB7@superman");
        /**
         *  indices[0] = 0;
         *  mem[0] = "CHALLENGE";
         *  MAP 0 0 (map "C" to "aA1@bB2$")
         *  indices[0] = 0;
         *  mem[0] = "aA1@bB2$";
         */
        //String[] instr0 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1", "END"};
        String[] instr0 = new String[]{"MAP #@ 1", "LOOPSTART l1", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l1 EOS 1", "END"};
        pwd[0] = new PasswordData(map0, instr0, "Fixed password.", 2);

        //Case 2: Fixed password + add exact challenge
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("@","aB7@");
        String[] instr1 = new String[]{"LOOPSTART l1", "OUTPUT 0", "SHIFT 0 1", "LOOPEND l1 EOS 0","MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[1] = new PasswordData(map1, instr1, "Fixed password and website name.", 2);

        //Case 3: Start from last character
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("@", "aB7@");
        String[] instr2 = new String[]{"LOOPSTART l1", "SHIFT 0 1", "LOOPEND l1 EOS 0",
                "LABEL START3", "SHIFT 0 -1", "SET 2 &0",
                "LOOPSTART l2", "OUTPUT 0", "SHIFT 0 1 MOD", "LOOPEND l2 EQ &0 2",
                "MAP #@ 1", "LOOPSTART l3", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l3 EOS 1", "END"};
        pwd[2] = new PasswordData(map2, instr2, "Fixed password and website name, but start from the last character.", 2);

        //Case 4: Letters to Digits Schema
        HashMap<String, String> map3 = new HashMap<String, String>();
        for (int x = 0;x  < 26; x++) {
            map3.put((char)('A'+x)+"", "2");
        }
        String[] vowels = new String[]{"A", "E", "I", "O", "U"};
        for (int x = 0; x < vowels.length; x++) {
            map3.put(vowels[x], "1");
        }
        map3.put("@", "aB7@");
        String[] instr3 = new String[]{"LOOPSTART l1", "MAP 0 1", "OUTPUT 1", "SHIFT 0 1","LOOPEND l1 EOS 0", "MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[3] = new PasswordData(map3, instr3, "Have a map from letters to digits, consonants are 2 and vowels are 1, and output the value of each letter.", 6);

        //Case 5: Running Sum Schema
        HashMap<String, String> map4 = new HashMap<String, String>();
        for (int x = 0;x  < 26; x++) {
            map4.put((char)('A'+x)+"", "2");
        }
        for (int x = 0; x < vowels.length; x++) {
            map4.put(vowels[x], "1");
        }
        map4.put("@", "aB7@");
        String[] instr4 = new String[]{"SET 2 #0", "LOOPSTART l1", "MAP 0 1", "PARSE 1", "SHIFT 0 1",
                "SET 2 2 + 1 m10", "OUTPUT 2", "LOOPEND l1 EOS 0",
                "MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[4] = new PasswordData(map4, instr4, "Using the map from letters to digits (once again consonants are 2 and vowels are 1), maintain a running sum and output it mod 10.", 6);

        //Case 6: Vowels only
        HashMap<String,String> map5 = new HashMap<String,String>();
        map5.put("A", "1");
        map5.put("E", "2");
        map5.put("I", "3");
        map5.put("O", "4");
        map5.put("U", "5");
        map5.put("@", "aB7@");
        String[] instr5 = new String[]{"LOOPSTART l1", "IF !CONTAINS 0 START", "MAP 0 1", "OUTPUT 1", "LABEL START", "SHIFT 0 1", "LOOPEND l1 EOS 0",
                "MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[5] = new PasswordData(map5, instr5, "Map only vowels to digits and output only when the letter is a vowel.", 6);

        //Case 7: Vowels only, running sum
        HashMap<String,String> map6 = new HashMap<String,String>();
        map6.put("A", "1");
        map6.put("E", "2");
        map6.put("I", "3");
        map6.put("O", "4");
        map6.put("U", "5");
        map6.put("@", "aB7@");
        String[] instr6 = new String[]{"SET 2 #0", "LOOPSTART l1", "IF !CONTAINS 0 GO", "MAP 0 1", "PARSE 1", "SET 2 2 + 1 m10", "OUTPUT 2",
                "LABEL GO", "SHIFT 0 1", "LOOPEND l1 EOS 0",
                "MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[6] = new PasswordData(map6, instr6, "Map vowels to digits and maintain a running sum of the values mod 10 and output only on vowels.", 6);

        //Case 8: Vowels only, running sum, start from last
        HashMap<String,String> map7 = new HashMap<String,String>();
        map7.put("A", "1");
        map7.put("E", "2");
        map7.put("I", "3");
        map7.put("O", "4");
        map7.put("U", "5");
        map7.put("@", "aB7@");
        String[] instr7 = new String[]{"LOOPSTART l1", "SHIFT 0 1", "LOOPEND l1 EOS 0", "SHIFT 0 -1", "SET 3 &0",
                "SET 2 #0", "LOOPSTART l2", "IF !CONTAINS 0 GO", "MAP 0 1", "PARSE 1", "SET 2 2 + 1 m10", "OUTPUT 2", "LABEL GO", "SHIFT 0 1 mod", "LOOPEND l2 EQ &0 3",
                "MAP #@ 1", "LOOPSTART l3", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l3 EOS 1", "END"};
        pwd[7] = new PasswordData(map7, instr7, "Starting from the last character, map vowels to digits and maintain a running sum of values mod 10, outputting only on vowels.", 6, 4);

        //Case 9: All letters digit map
        HashMap<String,String> map8 = new HashMap<String,String>();
        for (int x = 0;x  < 26; x++) {
            map8.put((char)('A'+x)+"", (int)(Math.random()*10)+"");
        }
        map8.put("@", "aB7@");
        String[] instr8 = new String[]{"LOOPSTART l1", "IF !CONTAINS 0 START", "MAP 0 1", "OUTPUT 1", "LABEL START", "SHIFT 0 1", "LOOPEND l1 EOS 0",
                "MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[8] = new PasswordData(map8, instr8, "Map letters to digits and output.", 6);

        //Case 10: All letters digit starting from back
        HashMap<String,String> map9 = new HashMap<String,String>();
        for (int x = 0;x  < 26; x++) {
            map9.put((char)('A'+x)+"", (int)(Math.random()*10)+"");
        }
        map9.put("@", "aB7@");
        String[] instr9 = new String[]{"LOOPSTART l3", "SHIFT 0 1", "LOOPEND l3 EOS 0", "SHIFT 0 -1", "SET 2 &0",
                "LOOPSTART l1", "MAP 0 1", "OUTPUT 1", "SHIFT 0 1 mod", "LOOPEND l1 EQ &0 2",
                "MAP #@ 1", "LOOPSTART l2", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l2 EOS 1", "END"};
        pwd[9] = new PasswordData(map9, instr9, "Map letters to digit and output but start from last character.", 6);

        //Case 11: Vowels only, running sum, start from last
        HashMap<String,String> map10 = new HashMap<String,String>();
        for (int x = 0;x  < 26; x++) {
            map10.put((char)('A'+x)+"", (int)(Math.random()*10)+"");
        }
        map10.put("@", "aB7@");
        String[] instr10 = new String[]{"LOOPSTART l1", "SHIFT 0 1", "LOOPEND l1 EOS 0", "SHIFT 0 -1", "SET 3 &0",
                "SET 2 #0", "LOOPSTART l2", "IF !CONTAINS 0 GO", "MAP 0 1", "PARSE 1", "SET 2 2 + 1 m10", "OUTPUT 2", "LABEL GO", "SHIFT 0 1 mod", "LOOPEND l2 EQ &0 3",
                "MAP #@ 1", "LOOPSTART l3", "OUTPUT 1", "SHIFT 1 1", "LOOPEND l3 EOS 1", "END"};
        pwd[10] = new PasswordData(map10, instr10, "Starting from the last character, map characters to digits and maintain a running sum of values mod 10.", 6, 4);

        /**
        //Case 1: Fixed Password
        HashMap<String, String> map10 = new HashMap<String, String>();
        map10.put("@", "aA1@bB2$");

        String[] instr10 = new String[]{"MAP #@ 1", "LABEL START", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START", "END"};
        pwd[10] = new PasswordData(map10, instr10);

        //Case 2: Fixed password + add exact challenge
        HashMap<String, String> map11 = new HashMap<String, String>();
        map11.put("@","aA@1");
        String[] instr11 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1", "LABEL START2", "OUTPUT 0", "SHIFT 0 1", "IF !EOS 0 START2", "END"};
        pwd[11] = new PasswordData(map11, instr11);

        //Case 3: Add one
        HashMap<String, String> map12 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map12.put(""+(char)('A'+x), ""+(char)('A'+(x+1)%26));
        }
        String[] instr12 = new String[]{"LABEL START", "MAP 0 1", "OUTPUT 1", "SHIFT 0 1", "IF !EOS 0 START", "END"};
        pwd[12] = new PasswordData(map12, instr12);


        //Case 4: Start at Second Vowel
        HashMap<String, String> map13 = new HashMap<String, String>();
        for (int x = 0; x < 26; x++) {
            map13.put(""+(char)('A'+x), ""+(char)('A'+(x+1)%26));
        }
        String[] instr13 = new String[]{"IF !VOWEL 0 V", "SHIFT 1 1", "LABEL V", "SHIFT 0 1", "IF EOS 0 W-",  "IF !VOWEL 0 V", "SHIFT 1 1", "IF EQ &1 #2 W", "GOTO V",
                "LABEL W-", "SHIFT 0 -1 MOD",
                "LABEL W", "SHIFT 0 -1 MOD", "SET 2 &0", "PARSE 2", "SHIFT 0 1 MOD",
                "LABEL W+", "MAP 0 1", "OUTPUT 1", "IF EQ &0 2 END", "SHIFT 0 1 MOD", "GOTO W+",
                "LABEL END", "END"};
        pwd[13] = new PasswordData(map13, instr13);

        //Case 5: Output first letter after first vowel
        HashMap<String, String> map14 = new HashMap<String, String>();
        String[] instr14 = new String[]{"LABEL START", "IF VOWEL 0 END", "SHIFT 0 1", "IF EOS 0 END2", "GOTO START",
                "LABEL END", "SHIFT 0 1 MOD", "OUTPUT 0", "END",
                "LABEL END2", "SHIFT 0 -1 MOD", "OUTPUT 0", "END"};
        pwd[14] = new PasswordData(map14, instr14);

        //Case 6: Output first letter whose capitalization contains a vertical line segment
        HashMap<String, String> map15 = new HashMap<String, String>();
        char[] letters = new char[]{'B', 'D', 'E', 'F', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'R', 'T', 'U'};
        for (char a : letters) {
            map15.put(a+"", " ");
        }
        String[] instr15 = new String[]{ "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START", "LABEL END", "SHIFT 0 -1", "OUTPUT 0", "END"};
        pwd[15] = new PasswordData(map15, instr15);

        //Case 7: Output first letter whose capitalization contains a vertical line segment
        HashMap<String, String> map16 = new HashMap<String, String>();
        char[] letters1 = new char[]{'B', 'C', 'D', 'E', 'G', 'P', 'T', 'V', 'Z'};
        for (char a : letters1) {
            map16.put(a+"", " ");
        }
        String[] instr16 = new String[]{ "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START", "LABEL END", "SHIFT 0 -1", "OUTPUT 0", "END"};
        pwd[16] = new PasswordData(map16, instr16);

        // Skip Game 8

        //Case 9:  Telephone number schema
        HashMap<String, String> map18 = new HashMap<String, String>();
        String phonenumber = "4324324324";
        map18.put("@", phonenumber);
        String[] instr18 = new String[]{"MAP #@ 1", "LABEL START", "IF EOS 0 END", "SET 2 @1 + 0", "SHIFT 0 1", "SHIFT 1 1", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[18] = new PasswordData(map18, instr18);

        //Case 10: Wrap aroudn telephone number schema
        HashMap<String, String> map19 = new HashMap<String, String>();
        map19.put("@", phonenumber);
        String[] instr19 = new String[]{"MAP #@ 1", "LABEL START", "IF EOS 1 END", "SET 2 @1 + 0", "SHIFT 0 1 MOD", "SHIFT 1 1", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[19] = new PasswordData(map19, instr19);
        **/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_introduction, menu);
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
