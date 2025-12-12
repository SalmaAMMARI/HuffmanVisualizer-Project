package huffman.algorithm;

import huffman.model.HuffmanNode;
import huffman.model.HuffmanTree;
import java.util.*;

public class TreeBuilder {
    public static HuffmanTree buildHuffmanTree(String text){
        HuffmanTree res = new HuffmanTree();
        List<HuffmanNode> list_nodes = SortedNodesCreator.sort(text);
        LinkedList<HuffmanNode> ll_nodes = new LinkedList<>(list_nodes);

        while (ll_nodes.size()>1){
            List<HuffmanNode> actualStep = new ArrayList<>(ll_nodes);
            res.addConstructionSteps(actualStep);
            HuffmanNode node1 = ll_nodes.removeFirst();
            HuffmanNode node2 = ll_nodes.removeFirst();

            int new_frequency =  node1.getFrequency()+node2.getFrequency();
            HuffmanNode new_node = new HuffmanNode(null,new_frequency);
            new_node.setLeft(node1);
            new_node.setRight(node2);

            ListIterator<HuffmanNode> it = ll_nodes.listIterator();
            boolean found = false;
            while (it.hasNext() && !found){
                HuffmanNode compareNode = it.next();
                if (compareNode.getFrequency() > new_frequency){
                    it.previous();
                    it.add(new_node);
                    found = true;
                }
            }

            if (!found){
                it.add(new_node);
            }

        }
        List<HuffmanNode> actualStep = new ArrayList<>(ll_nodes);
        res.addConstructionSteps(actualStep);
        res.setRoot(ll_nodes.get(0));
        return res;
    }
}
