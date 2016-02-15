package pwgame.passwordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class mainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
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
        String[] instr0 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1", "END"};
        pwd[0] = new PasswordData(map0, instr0, "Fixed password.", 2);

        //Case 2: Fixed password + add exact challenge
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("@","aB7@");
        String[] instr1 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1", "LABEL START2", "OUTPUT 0", "SHIFT 0 1", "IF !EOS 0 START2", "END"};
        pwd[1] = new PasswordData(map1, instr1, "Fixed password and website name.", 2);

        //Case 3: Start from last character
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("@", "aB7@");
        String[] instr2 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1",
                "LABEL START2", "IF EOS 0 START3", "SHIFT 0 1", "GOTO START2",
                "LABEL START3", "SHIFT 0 -1", "SET 2 &0",
                "LABEL START4", "OUTPUT 0", "SHIFT 0 1 MOD", "IF !EQ &0 2 START4", "END"};
        pwd[2] = new PasswordData(map2, instr2, "Fixed password and website name, but start from the last character.", 2);

        //Case 4: Letters to Digits Schema
        HashMap<String, String> map3 = new HashMap<String, String>();
        for (int x = 0;x  < 26; x++) {
            map3.put((char)('A'+x)+"", (int)(Math.random()*10)+"");
        }
        map3.put("@", "aB7@");
        String[] instr3 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1", "LABEL START", "IF EOS 0 END", "MAP 0 1", "OUTPUT 1", "SHIFT 0 1", "GOTO START", "LABEL END", "END"};
        pwd[3] = new PasswordData(map3, instr3, "Have a map from letters and digits and output the value of each letter.", 6);

        //Case 5: Running Sum Schema
        HashMap<String, String> map4 = new HashMap<String, String>();
        for (int x = 0;x  < 26; x++) {
            map4.put((char)('A'+x)+"", (int)(Math.random()*10)+"");
        }
        map4.put("@", "aB7@");
        String[] instr4 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1",
                "SET 2 #0", "LABEL START", "IF EOS 0 END", "MAP 0 1", "PARSE 1", "SHIFT 0 1",
                "SET 2 2 + 1 m10", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[4] = new PasswordData(map4, instr4, "Using the map from letters to digits, maintain a running sum and output it mod 10.", 6);

        //Case 6: Vowels only
        HashMap<String,String> map5 = new HashMap<String,String>();
        map5.put("A", "1");
        map5.put("E", "2");
        map5.put("I", "3");
        map5.put("O", "4");
        map5.put("U", "5");
        map5.put("@", "aB7@");
        String[] instr5 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1",
                "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START", "OUTPUT 1", "GOTO START", "LABEL END", "END"};
        pwd[5] = new PasswordData(map5, instr5, "Map only vowels to digits and output only when the letter is a vowel.", 6);

        //Case 6: Vowels only, running sum
        HashMap<String,String> map6 = new HashMap<String,String>();
        map6.put("A", "1");
        map6.put("E", "2");
        map6.put("I", "3");
        map6.put("O", "4");
        map6.put("U", "5");
        map6.put("@", "aB7@");
        String[] instr6 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1",
                "SET 2 #0", "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START",
                "PARSE 1", "SET 2 2 + 1 m10", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[6] = new PasswordData(map6, instr6, "Map vowels to digits and maintain a running sum of the values and output only on vowels.", 6);

        //Case 7: Vowels only, running sum, start from last
        HashMap<String,String> map7 = new HashMap<String,String>();
        map7.put("A", "1");
        map7.put("E", "2");
        map7.put("I", "3");
        map7.put("O", "4");
        map7.put("U", "5");
        map7.put("@", "aB7@");
        String[] instr7 = new String[]{"MAP #@ 1", "LABEL START1", "OUTPUT 1", "SHIFT 1 1", "IF !EOS 1 START1",
                "SET 2 #0", "LABEL START", "IF EOS 0 END", "MAP 0 1", "SHIFT 0 1", "IF EQ &1 #-2 START",
                "PARSE 1", "SET 2 2 + 1 m10", "OUTPUT 2", "GOTO START", "LABEL END", "END"};
        pwd[7] = new PasswordData(map7, instr7, "");

        //Case 1: Fixed Password
        HashMap<String, String> map10 = new HashMap<String, String>();
        map10.put("@", "aA1@bB2$");
        /**
         *  indices[0] = 0;
         *  mem[0] = "CHALLENGE";
         *  MAP 0 0 (map "C" to "aA1@bB2$")
         *  indices[0] = 0;
         *  mem[0] = "aA1@bB2$";
         */
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
    }
    public void library(View view) {
        Intent intent = new Intent(this, schemaLibrary.class);
        startActivity(intent);
    }

    public void gamePortal(View view) {
        Intent intent = new Intent(this, introduction.class);
        PasswordData[] arr = new PasswordData[25];
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
        /**
        Intent intent = new Intent(this, gamePortal.class);
        startActivity(intent);
        **/
    }

    public void builder(View view) {
        Intent intent = new Intent(this, schemaBuilder.class);
        startActivity(intent);
    }
}
