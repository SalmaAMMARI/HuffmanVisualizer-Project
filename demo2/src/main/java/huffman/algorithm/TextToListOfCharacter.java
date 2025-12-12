package huffman.algorithm;

import java.util.ArrayList;
import java.util.List;

public class TextToListOfCharacter {
    public static List<Character> TextFormatModifier(String text){
        List<Character> res = new ArrayList<>();
        for (char x : text.toCharArray()){
            res.add(Character.valueOf(x));
        }
        return res;
    }
}
