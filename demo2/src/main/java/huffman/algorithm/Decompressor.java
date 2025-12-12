package huffman.algorithm;

import java.util.HashMap;
import java.util.Map;

import huffman.model.HuffmanTree;

public class Decompressor {
    public static String decompress(String encodedText,HuffmanTree tree){
        HashMap<String,Character> codeToLetter = new HashMap<>();
        for (Map.Entry<Character,String> e : tree.getCode().entrySet()){
            codeToLetter.put(e.getValue(),e.getKey());
        }
        StringBuilder res = new StringBuilder();
        String curr = "";
        for (int i=0; i<encodedText.length(); i++){
            curr += encodedText.charAt(i);
            if (codeToLetter.containsKey(curr)){
                res.append(codeToLetter.get(curr));
                curr = "";
            }
        }
        return res.toString();
    }
}