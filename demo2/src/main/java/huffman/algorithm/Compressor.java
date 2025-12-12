package huffman.algorithm;

import java.util.HashMap;

import huffman.model.HuffmanTree;

public class Compressor {
    public static String compress(String text,HuffmanTree tree){
        StringBuilder res = new StringBuilder();
        HashMap<Character,String> code = tree.getCode();
        for (char c : text.toCharArray()){
            res.append(code.get(c));
        }
        return res.toString();
    }
}