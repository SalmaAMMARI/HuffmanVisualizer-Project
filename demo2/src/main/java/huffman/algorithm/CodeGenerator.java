package huffman.algorithm;

import java.util.HashMap;

import huffman.model.HuffmanNode;

public class CodeGenerator {
    public static void generateCode(HuffmanNode tree,HashMap<Character,String> codeMap,String encoding){

        if (tree.isLeaf()){
            codeMap.put(tree.getCharacter(),encoding);
            return;
        }
        generateCode(tree.getLeft(), codeMap, encoding+"0");
        generateCode(tree.getRight(), codeMap, encoding+"1");

    }
}