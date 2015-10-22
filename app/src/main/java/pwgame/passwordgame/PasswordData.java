package pwgame.passwordgame;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Abraham on 9/23/2015.
 */
public class PasswordData implements Parcelable
{
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
    }
    public PasswordData(HashMap<String, String> preProcess, String[] instructions) {
        this.preProcess = preProcess;
        this.instructions = instructions;
    }
    public String getPassword(String challenge) {
        String carry = "0";
        String password = "";
        for (int x = 0; x < challenge.length(); x++) {
            String[] temp = recurse(carry, challenge.substring(x,x+1));
            carry = temp[0];
            password += temp[1];
        }
        return password;
    }

    /**
     * This method encapsulates the recursive calculation done by an individual at each step
     * @param pastStep The string passed by the past step (e.g. a running sum)
     * @param curChar The current character in the string
     * @return A string array of length 2 where the first is the string to be passed to the next step
     *      and the second is the string to be outputted
     */
    private String[] recurse(String pastStep, String curChar) {
        String nextStepData = "";
        String outputString = "";
        String[] data = new String[]{pastStep, curChar, nextStepData, outputString, "", "", ""};
        for (int x = 0; x < instructions.length; x++) {
            Log.e("F", instructions[x]);
            eval(instructions[x], data);
        }
        Log.e("E", data[2] + "!"+data[3]);
        return new String[]{data[2], data[3]};
    }
    private void resetVars(String[] data) {
        for (int x = 4; x < data.length; x++) {
            data[x] = "";
        }
    }
    private void eval(String ins, String[] data) {
        /*
            Variable = 0,1,... indices in string array
            Data:
            Previous Step Data = ps
            Current Character = cc
            Next Step Data = ns
            Output String = os

            To map a character: MAP [characterToBeMapped] [indexOfVariable]
                If no valid mapping exists, simply set to empty string
            Set a variable to an expression: SET [indexOfVariable] [expression]
                Valid expressions:
                    [indexOfVariable/Data]
                    [indexOfVariable/Data] {+,-,*} [indexOfVariable/Data]
                    [indexOfVariable/Data] {+,-,*} [indexOfVariable/Data] [mX]  (where X is a number to mod by)
            OUTPUT [indexOfVariable]
            PASS [indexOfVariable]

            Instructions must be space separated.
         */
        StringTokenizer s1 = new StringTokenizer(ins);
        String swtch = s1.nextToken();
        switch (swtch) {
            case "MAP":
                Log.e("F", "MAP!");
                int mapchar = convert(s1.nextToken());
                try {
                    int index = convert(s1.nextToken());
                    if (preProcess.containsKey(data[mapchar])) {
                        Log.e("F", "MAPPED!");
                        data[index] = preProcess.get(data[mapchar]);
                        Log.e("F", data[index]);
                    } else {
                        Log.e("E", "NOT MAPPED");
                    }
                } catch (Exception e) {
                    Log.e("Invalid argument", "eval");
                }
                break;
            case "SET":
                int index = convert(s1.nextToken());
                int index1 = convert(s1.nextToken());
                String op = "";
                int index2 = -1;
                if (s1.hasMoreTokens()) {
                    op = s1.nextToken();
                    index2 = convert(s1.nextToken());
                    try {
                        if (op.equals("+")) {
                            data[index] = Integer.parseInt(data[index1]) + Integer.parseInt(data[index2]) + "";
                        } else if (op.equals("*")) {
                            data[index] = Integer.parseInt(data[index1]) * Integer.parseInt(data[index2]) + "";
                        } else if (op.equals("-")) {
                            data[index] = Integer.parseInt(data[index1]) - Integer.parseInt(data[index2]) + "";
                        }
                    } catch (Exception e) {
                        Log.e("E", "Number problemos");
                       // data[index] = Integer.parseInt(data[]);
                    }
                } else {
                    data[index] = data[index1];
                }
                if (s1.hasMoreTokens()) {
                    int mod = Integer.parseInt(s1.nextToken().substring(1));
                    data[index] = Integer.parseInt(data[index])%mod + "";
                }
                break;
            case "OUTPUT":
                try {
                    int ind = convert(s1.nextToken());
                    data[3] = data[ind];
                } catch (Exception e) {
                    Log.e("Invalid argument", "eval");
                }
                break;
            case "PASS":
                try {
                    int indx = convert(s1.nextToken());
                    data[2] = data[indx];
                } catch (Exception e) {
                    Log.e("Invalid argument", "eval");
                }
                break;
            default:
                Log.e("Invalid switch", "eval");
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
    }
}
