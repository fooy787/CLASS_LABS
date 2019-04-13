import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class WordCounter {
    HashMap<String, Integer> HM;
    public WordCounter(String filename) {
        HM = new HashMap();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.ready()) {
                String line = br.readLine(); //Reads a line
                line = line.trim(); //Trims the line
                line = line.toLowerCase();
                if(line.contains("!") | line.contains(",") | line.contains(".")){
                    line = line.substring(0, line.length() - 1);
                }//End if
                if (!HM.containsKey(line)) {
                    HM.put(line, 1);
                } else {
                    int oldVal = HM.get(line);
                    HM.replace(line, oldVal + 1);
                }//End else
            }//End while
        } catch (java.io.IOException e) {
            System.out.print("Error file does not exist.");
        }//End Catch
    }//End constructor


    public String BruteForceMethod(int n){
        String s = "";
        String[] stringArray = new String[n];
        int[] numArray = new int[n];
        for(int i = 0; i < n; i++){
            numArray[i] = 0;
        }

        for(String key : HM.keySet() ){
            int tempVal = HM.get(key);
            curBreak:
            for(int i = 0; i < n; i++){
                if (tempVal >= numArray[i]){
                    stringArray[i] = key;
                    System.arraycopy(stringArray, i + 1, stringArray, i + 2, n - i);
                    numArray[i] = tempVal;
                    System.arraycopy(numArray, i + 1, numArray, i + 2, n - i);
                    break curBreak;
                }
            }
        }
        for(int i = 0; i < n; i++){
            s+=stringArray[i] +":";
            s+=numArray[i]+",";
        }
        return s;
    }
}//End Word counter class