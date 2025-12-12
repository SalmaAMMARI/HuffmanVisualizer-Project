package huffman.algorithm;

import huffman.model.HuffmanTree;

public class Test {
    public static void main(String[] args) {
        String myText = "Huffman coding @ is a popular algorithm used for lossless data compression. It was developed by David A. Huffman while he was a Ph.D. student at MIT and was published in 1952. The primary goal of Huffman coding is to compress data efficiently by assigning shorter codes to more frequent symbols or characters and longer codes to less frequent ones.";
        HuffmanTree myTree = TreeBuilder.buildHuffmanTree(myText);

        for (var x : myTree.getConstructionSteps()){
            System.out.println(x);
        }
        myTree.generateMycode();
        System.out.println(myTree.getCode());
        String compression = Compressor.compress(myText, myTree);
        System.out.println(Decompressor.decompress(compression, myTree));
    }
}
