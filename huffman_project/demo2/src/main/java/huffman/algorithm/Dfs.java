package huffman.algorithm;

import huffman.model.HuffmanNode;

public class Dfs {
    public static void dfs(HuffmanNode root){
        if (root == null){return;}
        System.out.println(root);
        dfs(root.getLeft());
        dfs(root.getRight());
    }
}