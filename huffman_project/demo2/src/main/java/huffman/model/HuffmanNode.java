package huffman.model;

public class HuffmanNode {
    private Character character;
    private int frequency;
    private HuffmanNode left;
    private HuffmanNode right;

    public Character getCharacter() { return character; }
    public void setCharacter(Character character) { this.character = character; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public HuffmanNode getLeft() { return left; }
    public void setLeft(HuffmanNode left) { this.left = left; }

    public HuffmanNode getRight() { return right; }
    public void setRight(HuffmanNode right) { this.right = right; }


    public HuffmanNode(Character character,int frequency){
        this.character = character;
        this.frequency = frequency;
    }

    public boolean isLeaf(){
        return (this.left == null) && (this.right == null);
    }


    @Override
    public String toString() {
        return "["+character+","+frequency+"]";
    }
}
