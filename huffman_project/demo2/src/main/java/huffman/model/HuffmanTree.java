package huffman.model;

import java.util.List;

import huffman.algorithm.CodeGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanTree {
    private HuffmanNode root;
    private List<List<HuffmanNode>> constructionSteps = new ArrayList<>();
    private HashMap<Character,String> code = new HashMap<>();

    public HuffmanNode getRoot(){return root;}
    public void setRoot(HuffmanNode root){this.root = root;}
    public HashMap<Character,String> getCode(){return code;}
    public List<List<HuffmanNode>> getConstructionSteps(){
        return constructionSteps;
    }
    public void addConstructionSteps(List<HuffmanNode> constructionSteps){
        this.constructionSteps.add(constructionSteps);
    }
    public void generateMycode(){
        CodeGenerator.generateCode(root,code,"");
    }
}