package pwgame.passwordgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Abraham on 4/25/2016.
 */
public class DBPasswordData {
    private String name, instructions, map;
    public DBPasswordData() {

    }
    public DBPasswordData(String name, String map, String instructions) {
        this.name=name;
        this.map=map;
        this.instructions=instructions;
    }
    public DBPasswordData(ArrayList<Integer> start, ArrayList<Integer> end) {
        map="";
        for (int x = 0; x < start.size(); x++) {
            map+=start.get(x)+","+end.get(x);
            if (x != start.size()-1) {
                map+=",";
            }
        }
    }
    public PasswordData createPasswordData() {
        return new PasswordData(generateMap(),instructions.split(","));
    }
    private int[][] mapMaker;
    private void makeMapMaker() {
        if (mapMaker == null) {
            mapMaker = new int[26+2+10][2];
            mapMaker[0] = new int[]{'A', 26};
            mapMaker[1] = new int[]{'0', 10};
            for (int x = 0;x < 26; x++) {
                mapMaker[x+2] = new int[]{'A'+x, 1};
            }
            for (int x = 0; x < 10; x++) {
                mapMaker[x+2+26] = new int[]{'0'+x, 1};
            }
        }
    }
    public HashMap<String,String> generateMap() {
        makeMapMaker();
        HashMap<String,String> ret = new HashMap<String,String>();
        StringTokenizer s1 = new StringTokenizer(map, ",");
        while (s1.hasMoreTokens()) {
            int a = Integer.parseInt(s1.nextToken());
            int b = Integer.parseInt(s1.nextToken());
            for (int y = 0; y < mapMaker[a][1]; y++) {
                ret.put("" + (char) (mapMaker[a][0] + y), (char) (mapMaker[b][0] + (int) (Math.random() * mapMaker[b][1])) + "");
            }
        }
        return ret;
    }
    public String getName() {
        return name;
    }
    public String getInstructions() {
        return instructions;
    }
    public String getMap() {
        return map;
    }
}
