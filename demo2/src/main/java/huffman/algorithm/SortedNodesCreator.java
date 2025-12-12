package huffman.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huffman.model.HuffmanNode;

public class SortedNodesCreator {
    public static List<HuffmanNode> sort(String initial_text){
        List<Character> text = TextToListOfCharacter.TextFormatModifier(initial_text);

        HashMap<Character,Integer> map = FrequencyCounter.getFrequency(text);
        List<HuffmanNode> res = new ArrayList<>();

        for (Map.Entry<Character,Integer> e : map.entrySet()){
            HuffmanNode node = new HuffmanNode(e.getKey(),e.getValue());
            res.add(node);
        }
        return MergeSort.mergeSort(res);
    }
}
