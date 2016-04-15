package pwgame.passwordgame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by Abraham on 2/7/2016.
 */

public class PasswordTrace implements Parcelable {
    private ArrayList<String> trace;
    private String challenge;
    private ArrayList<ArrayList<String>> memHist;
    private ArrayList<ArrayList<Integer>> indHist;
    private ArrayList<String> output;
    public static Parcelable.Creator<PasswordTrace> CREATOR = new Creator<PasswordTrace>() {
        public PasswordTrace createFromParcel(Parcel source) {
            return new PasswordTrace(source);
        }
        public PasswordTrace[] newArray(int size) {
            return new PasswordTrace[size];
        }
    };
    public PasswordTrace(String challenge, ArrayList<String> trace, ArrayList<ArrayList<String>> memHist, ArrayList<ArrayList<Integer>> indHist, ArrayList<String> output) {
        this.challenge=challenge;
        this.trace=trace;
        this.memHist=memHist;
        this.indHist=indHist;
        this.output=output;
    }
    public int getMin() {
        return Math.min(Math.min(trace.size(), memHist.size()), Math.min(indHist.size(), output.size()));
    }
    public PasswordTrace(Parcel source) {
        challenge = source.readString();
        int size = source.readInt();
        String[] temp = new String[size];
        source.readStringArray(temp);
        trace = new ArrayList<String>(Arrays.asList(temp));

        size = source.readInt();
        int size1 = source.readInt();
        memHist = new ArrayList<ArrayList<String>>();
        for (int x = 0; x< size; x++) {
            temp = new String[size1];
            source.readStringArray(temp);
            memHist.add(new ArrayList<String>(Arrays.asList(temp)));
        }

        size = source.readInt();
        size1 = source.readInt();
        indHist = new ArrayList<ArrayList<Integer>>();
        for (int x = 0; x < size; x++) {
            int[] read = new int[size1];
            source.readIntArray(read);
            Integer[] rd = new Integer[size1];
            for (int a = 0; a < size1; a++) {
                rd[a] = read[a];
            }
            indHist.add(new ArrayList<Integer>(Arrays.asList(rd)));
        }

        size = source.readInt();
        temp = new String[size];
        source.readStringArray(temp);
        output = new ArrayList<String>(Arrays.asList(temp));
    }
    public String getChallenge() {
        return challenge;
    }
    public ArrayList<String> getTrace() {
        return trace;
    }
    public ArrayList<ArrayList<String>> getMemoryTrace() {
        return memHist;
    }
    public ArrayList<ArrayList<Integer>> getPointerTrace() {
        return indHist;
    }
    public ArrayList<String> getOutputTrace() {
        return output;
    }
    private int HUM = -1;
    public int getHUM() {
        if (HUM != -1) {
            return HUM;
        }
        int count = 0;
        for (int x = 0; x < trace.size(); x++) {
            String str = trace.get(x).substring(0, trace.get(x).indexOf(" "));
            if (!str.equals("IF")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(challenge);
        dest.writeInt(trace.size());
        if (trace.size() > 0) {
            String[] arr = new String[trace.size()];
            arr = trace.toArray(arr);
            dest.writeStringArray(arr);
        }
        dest.writeInt(memHist.size());
        if (memHist.size() > 0) {
            dest.writeInt(memHist.get(0).size());
            for (ArrayList<String> s : memHist) {
                String[] arr = new String[s.size()];
                arr = s.toArray(arr);
                dest.writeStringArray(arr);
            }
        }

        dest.writeInt(indHist.size());
        if (indHist.size() > 0) {
            dest.writeInt(indHist.get(0).size());
            for (ArrayList<Integer> s : indHist) {
                int[] arr1 = new int[s.size()];
                for (int x = 0; x < s.size(); x++) {
                    arr1[x] = s.get(x);
                }
                dest.writeIntArray(arr1);
            }
        }

        dest.writeInt(output.size());
        if (output.size() > 0) {
            String[] arr = new String[output.size()];
            arr = output.toArray(arr);
            dest.writeStringArray(arr);
        }
    }
}