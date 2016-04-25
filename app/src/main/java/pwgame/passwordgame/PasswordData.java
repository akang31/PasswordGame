package pwgame.passwordgame;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Created by Abraham on 9/23/2015.
 */
public class PasswordData implements Parcelable
{
    private String description;
    private int SIZE_OF_MEMORY = 3;
    private String[] hints;
    private int hintIndex = 0;
    private ArrayList<String> trace;
    private ArrayList<ArrayList<String>> memHist;
    private ArrayList<ArrayList<Integer>> indHist;
    private ArrayList<String> out;
    private int Q = 2;
    private final String[] VOWEL_VALUES = new String[]{"A", "E", "I", "O", "U", "a", "e", "i", "o", "u"};
    private final HashSet<String> VOWELS = new HashSet<String>(Arrays.asList(VOWEL_VALUES));
    private HashMap<String, String> preProcess;
    private String[] instructions;
    public static Parcelable.Creator<PasswordData> CREATOR = new Creator<PasswordData>() {
        public PasswordData createFromParcel(Parcel source) {
            return new PasswordData(source);
        }
        public PasswordData[] newArray(int size) {
            return new PasswordData[size];
        }
    };
    public PasswordData() {
        preProcess = new HashMap<String, String>();
        instructions = new String[10];
    }
    public PasswordData(Parcel source) {
        int lenOfPreprocess = source.readInt();
        String[] preprocessHolder= new String[lenOfPreprocess];
        source.readStringArray(preprocessHolder);
        preProcess = new HashMap<String, String>();
        for (int x = 0; x < preprocessHolder.length/2; x++) {
            preProcess.put(preprocessHolder[2*x], preprocessHolder[2*x+1]);
        }
        int lenOfInstructions = source.readInt();
        instructions = new String[lenOfInstructions];
        source.readStringArray(instructions);
        description = source.readString();
        Q = source.readInt();
        SIZE_OF_MEMORY = source.readInt();
        int lenOfHints = source.readInt();
        if (lenOfHints != 0) {
            hints = new String[lenOfHints];
            source.readStringArray(hints);
        }
    }
    public PasswordData(HashMap<String, String> preProcess, String[] instructions) {
        this.preProcess = preProcess;
        this.instructions = instructions;
    }
    public PasswordData(HashMap<String, String> preProcess, String[] instructions, String description) {
        this.preProcess = preProcess;
        this.instructions = instructions;
        this.description = description;
    }
    public PasswordData(HashMap<String, String> preProcess, String[] instructions, int Q) {
        this.preProcess = preProcess;
        this.instructions = instructions;
        this.Q = Q;
    }
    public PasswordData(HashMap<String, String> preProcess, String[] instructions, String description, int Q) {
        this.preProcess = preProcess;
        this.instructions = instructions;
        this.Q = Q;
        this.description = description;
    }
    public PasswordData(HashMap<String, String> preProcess, String[] instructions, String description, int Q, int k) {
        this.preProcess = preProcess;
        this.instructions = instructions;
        this.Q = Q;
        this.description = description;
        this.SIZE_OF_MEMORY = k;
        Log.e("SIZE CHANGED!", SIZE_OF_MEMORY+"");
    }
    public String getHint() {
        if (hints == null || hints.length == 0) {
            return "No hints";
        } else {
            hintIndex %= hints.length;
            return hints[hintIndex++];
        }
    }
    public void setHints(String[] hts) {
        this.hints=hts;
    }
    public String getDescription() {
        return description;
    }
    public int getQ() {
        return Q;
    }
    private class Result {
        private int index;
        private String output;
        public Result(int index, String output) {
            this.index=index;
            this.output=output;
        }
        public int getIndex() {
            return index;
        }
        public String getOutput() {
            return output;
        }
    }
    public ArrayList<String> getTrace() {
        return trace;
    }
    public PasswordTrace getFullTrace() {
        return new PasswordTrace(chlg, trace, memHist, indHist, out);
    }
    public String[] getInstructions() {
        return instructions;
    }
    private String chlg= "";
    private int failLine = -1;
    public int getFailLine() {
        return failLine;
    }
    public String getPassword(String challenge, boolean includeTrace) {
        String password = "";
        chlg = challenge;
        int index = 0;
        int[] indices = new int[SIZE_OF_MEMORY];
        String[] mem = new String[SIZE_OF_MEMORY];
        Log.e("SIZE!", SIZE_OF_MEMORY +"");
        indices[0] = 0;
        mem[0] = challenge;
        int count = 0;
        trace = new ArrayList<String>();
        memHist = new ArrayList<ArrayList<String>>();
        indHist = new ArrayList<ArrayList<Integer>>();
        out = new ArrayList<String>();
        String curOut = "";
        if (instructions.length == 0) {
            failLine = 0;
            return "";
        }
        while (!done(index)) {
            Log.e("RUNNING", index+"");
            Result res = new Result(0,"");
            try {
                res = run(index, indices, mem);
            } catch(Exception e) {
                Log.e("SDFSDF","Failure during command run");
                failLine = index;
                return "";
            }
            try {
                if (true) {
                    //Hack: index < res.getIndex(). Assumes forward action implies taking an if statement. Very easy to make counterexample, but should work for now
                    String tr = tracify(instructions[index], mem, indices, index < res.getIndex());
                    if (!tr.equals("")) {
                        trace.add(tr);
                        memHist.add(new ArrayList<String>(Arrays.asList(Arrays.copyOf(mem, mem.length))));
                        Integer[] copy = new Integer[indices.length];
                        for (int x = 0; x < indices.length; x++) {
                            copy[x] = indices[x];
                        }
                        indHist.add(new ArrayList<Integer>(Arrays.asList(copy)));
                        if (tr.substring(0, 6).equalsIgnoreCase("output")) {
                            int ind = Integer.parseInt(instructions[index].substring(instructions[index].indexOf(" ")+1));
                            if (indices[ind] >= 0) {
                                curOut += mem[ind].charAt(indices[ind]);
                            } else {
                                curOut += Integer.parseInt(mem[ind]);
                            }
                        }
                        out.add(curOut);
                    }
                }
            } catch(Exception e) {
                Log.e("SDFSDF", "Failure during trace creation");
                failLine = index;
                return "";
            }
            index = res.getIndex();
            if (index >= instructions.length) {
                failLine = index;
                return "";
            }
            password += res.getOutput();
            count++;
        }
        Log.e("Counter", ""+count);
        Log.e("Ratio", ""+((double)count)/challenge.length());
        return password;
    }
    public String indexOfVariableToString(String ind) {
        if (ind.charAt(0) == '&') {
            return "pointer index of variable " + ind.substring(1);
        } else if (ind.charAt(0) == '#') {
            return "number " + ind.substring(1);
        } else if (ind.charAt(0) == '@') {
            return "number at variable " + ind.substring(1);
        } else {
            return "variable " + ind;
        }
    }
    public String valueOfIndexOfVariable(String ind, String[] mem, int[] indices) {
        if (ind.charAt(0) == '&') {
            return ""+indices[Integer.parseInt(ind.substring(1))];
        } else if (ind.charAt(0) == '#') {
            return ind.substring(1);
        } else if (ind.charAt(0) == '@') {
            return mem[Integer.parseInt(ind.substring(1))];
        } else {
            if (indices[Integer.parseInt(ind)] == -1) {
                return mem[Integer.parseInt(ind)];
            }
            return mem[Integer.parseInt(ind)].charAt(indices[Integer.parseInt(ind)])+"";
        }
    }
    public String traceIf(StringTokenizer s1, String[] mem, int[] ind) {
        String op = s1.nextToken();
        String ret = "IF ";
        if (op.charAt(0) == '!') {
            ret += "NOT ";
            op = op.substring(1);
        }
        if (op.equals("EQ")) {
            ret += valueOfIndexOfVariable(s1.nextToken(), mem, ind) + " == " + valueOfIndexOfVariable(s1.nextToken(), mem, ind);
        } else if (op.equals("EOS")) {
            ret += "variable " + s1.nextToken() + " at end of string";
        } else if (op.equals("VOWEL")) {
            ret += valueOfIndexOfVariable(s1.nextToken(), mem, ind) + " is a vowel";
        } else if (op.equals("CONTAINS")) {
            ret += "map contains " + valueOfIndexOfVariable(s1.nextToken(),mem,ind);
        } else {
            ret += valueOfIndexOfVariable(op, mem, ind) + s1.nextToken() + valueOfIndexOfVariable(s1.nextToken(), mem, ind);
        }
        return ret;
    }
    public String tracify(String instr, String[] mem, int[] ind, boolean ifUsed) {
        StringTokenizer s1 = new StringTokenizer(instr);
        String start = s1.nextToken();
        String ret = "";
        if (start.equals("MAP")) {
            ret += "MAP(";
            String next = valueOfIndexOfVariable(s1.nextToken(), mem, ind);
            ret += next;
            ret += ") = " + preProcess.get(next);
        } else if (start.equals("SET")) {
            ret = "SET variable " + s1.nextToken() + " to " + indexOfVariableToString(s1.nextToken());
            if (s1.hasMoreTokens()) {
                ret += " " + s1.nextToken() + " " + indexOfVariableToString(s1.nextToken());
            }
            if (s1.hasMoreTokens()) {
                ret += "mod " + s1.nextToken().substring(1);
            }
        } else if (start.equals("GOTO")) {
        } else if (start.equals("LABEL")) {
        } else if (start.equals("IF")) {
            ret = traceIf(s1, mem, ind);
        } else if (start.equals("LOOPSTART")) {
            ret += instr;
        } else if (start.equals("LOOPEND")) {
            String name1 = s1.nextToken();
            if (evalIf(s1,mem,ind)) {
                s1 = new StringTokenizer(instr);
                s1.nextToken();
                s1.nextToken();
                ret = "LOOPEND "+name1 + " " + traceIf(s1, mem, ind);
            }
        } else if (start.equals("SHIFT")) {
            ret = "SHIFT variable " + s1.nextToken() + " by " + s1.nextToken();
            if (s1.hasMoreTokens()) {
                ret += " with wraparound";
            }
        } else if (start.equals("PARSE")) {
        } else if (start.equals("END")) {
        } else if (start.equals("OUTPUT")) {
            ret = start + " variable " + s1.nextToken();
        }
        return ret;
    }
    public String tracifyVerbose(String instr) {
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

            PARSE : convert to int

            End password schema: END

            OUTPUT [indexOfVariable]

            LOG [indexOfVariable*]
                Debug purposes, print to console.

            Instructions must be space separated.
         */
        StringTokenizer s1 = new StringTokenizer(instr);
        String start = s1.nextToken();
        String ret = "";
        if (start.equals("MAP")) {
            ret += "MAP ";
            String next = s1.nextToken();
            if (next.charAt(0) == '#') {
                ret += "\""+ next.substring(1)+"\"";
            } else {
                ret += "variable " + next;
            }
            ret += " into variable " + s1.nextToken();
        } else if (start.equals("SET")) {
            ret = "SET variable " + s1.nextToken() + " to " + indexOfVariableToString(s1.nextToken());
            if (s1.hasMoreTokens()) {
                ret += " " + s1.nextToken() + " " + indexOfVariableToString(s1.nextToken());
            }
            if (s1.hasMoreTokens()) {
                ret += "mod " + s1.nextToken().substring(1);
            }
        } else if (start.equals("GOTO")) {
            ret = "GOTO label " + s1.nextToken();
        } else if (start.equals("LABEL")) {
            ret = instr;
        } else if (start.equals("IF")) {
            ret = "IF ";
            String next = s1.nextToken();
            if (next.charAt(0) == '!') {
                next = next.substring(1);
                ret += "not ";
            }
            if (next.equals("EQ")) {
                ret += indexOfVariableToString(s1.nextToken()) + " equals " + indexOfVariableToString(s1.nextToken());
            } else if (next.equals("EOS")) {
                ret += "variable " + s1.nextToken() + " is at end of string";
            } else if (next.equals("VOWEL")) {
                ret += "variable " + s1.nextToken() + " is a vowel";
            } else if (next.equals("CONTAINS")) {

            }
            ret += " then goto label " + s1.nextToken();
        } else if (start.equals("SHIFT")) {
            ret = "SHIFT variable " + s1.nextToken() + " by " + s1.nextToken();
            if (s1.hasMoreTokens()) {
                ret += " with wraparound";
            }
        } else if (start.equals("PARSE")) {
            ret = "PARSE variable " + s1.nextToken() + " into integer";
        } else if (start.equals("END")) {
            ret = "END";
        } else if (start.equals("OUTPUT")) {
            ret = start + " variable " + s1.nextToken();
        }
        return ret;
    }
    /**
     *
     *
     * @param index
     * @return
     */
    private boolean done(int index) {
        return instructions[index].trim().equals("END");
    }
    private int findLabel(String label, String keyword) {
        for (int x = 0; x < instructions.length; x++) {
            StringTokenizer s2 = new StringTokenizer(instructions[x]);
            if (s2.nextToken().equals(keyword) && s2.nextToken().equals(label)) {
                return x;
            }
        }
        return -1;
    }
    private boolean evalIf(StringTokenizer s1, String[] mem, int[] indices) {
        String tok = s1.nextToken();
        boolean flip = false;
        boolean rett = false;
        if (tok.charAt(0) == '!') {
            flip = true;
            tok = tok.substring(1);
        }
        if (tok.equals("EOS")) {
            int nxt = Integer.parseInt(s1.nextToken());
            rett = indices[nxt] >= mem[nxt].length();
            Log.e("IF EOS", rett+"");
        } else if (tok.equals("VOWEL")) {
            int nxt = Integer.parseInt(s1.nextToken());
            rett = VOWELS.contains(mem[nxt].charAt(indices[nxt])+"");
        } else if (tok.equals("EQ")) {
            char[] c1 = new char[]{0};
            char[] c2 = new char[]{0};
            variable(c1, s1.nextToken(), indices, mem);
            variable(c2, s1.nextToken(), indices, mem);
            rett = c1[0] == c2[0];
            Log.e("IF EQ", (int)c1[0] + " " + (int)c2[0] + " " + rett);
        } else if (tok.equals("CONTAINS")) {
            int nxt = Integer.parseInt(s1.nextToken());
            rett = preProcess.containsKey(mem[nxt].charAt(indices[nxt])+"");
        } else {
            String op = s1.nextToken();
            String var2 = s1.nextToken();
            String val1 = valueOfIndexOfVariable(tok, mem, indices);
            String val2 = valueOfIndexOfVariable(var2, mem, indices);
            int quant = val1.compareTo(val2);
            if (op.equals("==")) {
                rett = quant==0;
            } else if (op.equals(">")) {
                rett = quant > 0;
            } else if (op.equals(">=") || op.equals("=>")) {
                rett = quant >= 0;
            } else if (op.equals("<")) {
                rett = quant < 0;
            } else if (op.equals("<=") || op.equals("=<")) {
                rett = quant <= 0;
            }else if (op.equals("!=") || op.equals("><") || op.equals("<>")) {
                rett = quant != 0;
            }
        }
        return rett^flip;
    }
    private Result run(int ind, int[] indices, String[] mem) throws Exception {
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
                    CONTAINS [indexOfVariable] : checks if current map contains character at variable
                    ![statement] : negate

            LOOPSTART [loopName]
            LOOPEND [loopName] [CONDITION*]
                Creates a loop named loopName. The condition follows same syntax as IF
                If loopend is met, the loop ends. If not, it goes back to LOOPSTART

            Shift a pointer in memory: SHIFT [indexOfVariable] [number shifted, signed] [wrap]
                if anything is put in for wrap, the shift will wrap around.

            PARSE : convert to int

            End password schema: END

            OUTPUT [indexOfVariable]

            LOG [indexOfVariable*]
                Debug purposes, print to console.

            Instructions must be space separated.
         */
        String ins = instructions[ind];
        StringTokenizer s1 = new StringTokenizer(ins);
        String swtch = s1.nextToken();
        Result ret = new Result(ind+1, "");
        switch (swtch) {
            case "IF":
                if (evalIf(s1, mem, indices)) {
                    ret = new Result(findLabel(s1.nextToken(), "LABEL"),"");
                }
                break;
            case "LOOPSTART":
                break;
            case "LOOPEND":
                String name1 = s1.nextToken();
                if (!s1.hasMoreTokens() || s1.hasMoreTokens() && !evalIf(s1,mem,indices)) {
                    ret = new Result(findLabel(name1, "LOOPSTART")+1, "");
                }
                break;
            case "SHIFT":
                int ind2 = Integer.parseInt(s1.nextToken());
                int quantShift = Integer.parseInt(s1.nextToken());
                indices[ind2] += quantShift;
                if (s1.hasMoreTokens()) {
                    indices[ind2] = (indices[ind2]+mem[ind2].length())%mem[ind2].length();
                }
                break;
            case "PARSE":
                int ind1 = Integer.parseInt(s1.nextToken());
                indices[ind1] = -1;
                break;
            case "GOTO":
                String next = s1.nextToken();
                ret = new Result(findLabel(next, "LABEL"),"");
                break;
            case "MAP":
                // CHECK FIRST
                Log.e("F", "MAP!");
                String next1 = s1.nextToken();
                if (next1.charAt(0) == '#') {
                    int next2 = Integer.parseInt(s1.nextToken());
                    mem[next2] = preProcess.get(next1.substring(1));
                    indices[next2] = 0;
                    break;
                }
                int char1 = Integer.parseInt(next1);
                int char2 = Integer.parseInt(s1.nextToken());
                if (preProcess.containsKey(mem[char1].charAt(indices[char1])+"")) {
                    Log.e("MAP", "Contains Key: " + mem[char1].charAt(indices[char1]));
                    mem[char2] = preProcess.get(mem[char1].charAt(indices[char1]) + "");
                    indices[char2] = 0;
                } else {
                    Log.e("MAP", "Does not contain key.");
                    mem[char2] = "NONE";
                    indices[char2] = -2;
                }
                Log.e("MAP", mem[char2]);
                break;
            case "SET":
                int index = Integer.parseInt(s1.nextToken());
                set(index, s1, indices, mem);
                break;
            case "OUTPUT":
                try {
                    int indx1 = Integer.parseInt(s1.nextToken());
                    if (indices[indx1] >= 0) {
                        ret = new Result(ind + 1, mem[indx1].charAt(indices[indx1]) + "");
                    } else {
                        ret = new Result(ind + 1, mem[indx1]);
                    }
                } catch (Exception e) {
                    Log.e("Invalid argument", "eval");
                }
                break;
            case "LOG":
                char[] pr = new char[]{0};
                if (variable(pr, s1.nextToken(), indices, mem)) {
                    Log.e("LOG", (int)pr[0] + "");
                } else {
                    Log.e("LOG", pr[0]+"");
                }
                break;
            case "LABEL":
                break;
           // case "END":
            //    break;
            default:
                throw new RuntimeException();
        }
        return ret;
    }
    /*
     Returns true if the variable is an integer and false if it is a character.
       res contains the value of the integer or character cast to a character.
       Used to decode indexOfVariable mentioned in SET

     */
    private boolean variable(char[] res, String strindex1, int[] indices, String[] mem) {
        char val1 = (char) 0;
        boolean isInt = false;
        if (strindex1.charAt(0) == '&') {
            isInt = true;
            val1 = (char) indices[Integer.parseInt(strindex1.substring(1))];
        } else if (strindex1.charAt(0) == '#') {
            isInt = true;
            val1 = (char) Integer.parseInt(strindex1.substring(1));
        } else if (strindex1.charAt(0) == '@'){
            isInt = true;
            int ind = Integer.parseInt(strindex1.substring(1));
            val1 = (char) (mem[ind].charAt(indices[ind])-'0');
        } else {
            int index1 = Integer.parseInt(strindex1);
            if (indices[index1] == -1) {
                isInt = true;
                val1 = (char)Integer.parseInt(mem[index1]);
            } else {
                val1 = mem[index1].charAt(indices[index1]);
            }
        }
        res[0] = val1;
        return isInt;
    }
    private void set(int index, StringTokenizer s1, int[] indices, String[] mem) {
        char[] val1 = new char[]{0};
        boolean isInt1 = variable(val1, s1.nextToken(), indices, mem);
        String op = "";
        int index2 = -1;
        // Case where there is an expression
        if (s1.hasMoreTokens()) {
            op = s1.nextToken();
            char[] val2 = new char[]{0};
            boolean isInt2 = variable(val2, s1.nextToken(), indices, mem);
            // When both are integers, then the index is set to an integer
            if (isInt1 && isInt2) {
                indices[index] = -1;
                if (op.equals("+")) {
                    mem[index] = ((int)val1[0]+(int)val2[0])+"";
                } else if (op.equals("*")) {
                    mem[index] = ((int)val1[0]*(int)val2[0])+"";
                } else if (op.equals("-")) {
                    mem[index] = ((int)val1[0]-(int)val2[0])+"";
                }
            // Otherwise, there is a character, so treat it as moving character (e.g. a + 1 = b)
            } else {
                int a = Math.max((int)val1[0], (int)val2[0]);
                boolean isLowerCase = a > 'Z';
                if (op.equals("+")) {
                    a = (int)val1[0]+(int)val2[0];
                } else if (op.equals("-")) {
                    a = (int)val1[0]-(int)val2[0];
                }
                if (isLowerCase && (a > 'z' || a < 'a')) {
                    a = (a-'a'+26)%26+'a';
                } else if (!isLowerCase && (a > 'Z' || a < 'A')) {
                    a = (a-'A'+26)%26+'A';
                }
                mem[index] = (char)a+"";
                indices[index] = 0;
                //char[] tempArr = mem[index].toCharArray();
                //tempArr[indices[index]] = (char)a;
                //mem[index] = new String(tempArr);
            }
        // CASE OF SIMPLY SETTING
        } else {
            if (isInt1) {
                indices[index] = -1;
                mem[index] = (int)val1[0]+"";
            } else {
                mem[index] = val1[0]+"";
                indices[index] = 0;
                //char[] tempArr = mem[index].toCharArray();
                //tempArr[indices[index]] = val1[0];
                //mem[index] = new String(tempArr);
            }
        }
        // Mod as requested
        if (s1.hasMoreTokens()) {
            int mod = Integer.parseInt(s1.nextToken().substring(1));
            mem[index] = Integer.parseInt(mem[index])%mod + "";
        }
    }
    private int convert(String s) {
        if (s.equals("ps")) {
            return 0;
        } else if (s.equals("cc")) {
            return 1;
        } else if (s.equals("ns")) {
            return 2;
        } else if (s.equals("os")) {
            return 3;
        } else {
            try {
                return Integer.parseInt(s) + 4;
            } catch (Exception e) {
                return -1;
            }
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(preProcess.size()*2);
        String[] put = new String[preProcess.size()*2];
        int counter = 0;
        for (String key : preProcess.keySet()) {
            put[counter++] = key;
            put[counter++] = preProcess.get(key);
        }
        dest.writeStringArray(put);
        dest.writeInt(instructions.length);
        dest.writeStringArray(instructions);
        dest.writeString(description);
        dest.writeInt(Q);
        dest.writeInt(SIZE_OF_MEMORY);
        if (hints != null) {
            dest.writeInt(hints.length);
            dest.writeStringArray(hints);
        } else {
            dest.writeInt(0);
        }
    }
}
