package huffman.algorithm;

import java.util.HashMap;
import java.util.List;

public class FrequencyCounter {
    public static HashMap<Character,Integer> getFrequency(List<Character> text){
        HashMap<Character,Integer> res = new HashMap<>();
        for (int i=0; i<text.size(); i++){
            Character curr_char = text.get(i);
            if (res.containsKey(curr_char)){
                res.put(curr_char,res.get(curr_char)+1);
            }
            else{
                res.put(curr_char,1);
            }
        }
        return res;
    }
}
