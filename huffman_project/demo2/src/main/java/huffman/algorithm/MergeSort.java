package huffman.algorithm;

import java.util.ArrayList;
import java.util.List;

import huffman.model.HuffmanNode;

public class MergeSort {
    public static List<HuffmanNode> merge(List<HuffmanNode> A,List<HuffmanNode> B){
        List<HuffmanNode> res = new ArrayList<>();
        int i=0; int j=0;

        while (i<A.size() && j<B.size()){
            if (A.get(i).getFrequency() <= B.get(j).getFrequency()){
                res.add(A.get(i));
                i++;
            }
            else{
                res.add(B.get(j));
                j++;
            }
        }

        while (i<A.size()){
            res.add(A.get(i));
            i++;
        }

        while (j<B.size()){
            res.add(B.get(j));
            j++;
        }
        return res;
    }

    public static List<HuffmanNode> mergeSort(List<HuffmanNode> A){
        if (A.size() <= 1){return A;}
        int mid = A.size()/2;
        return merge(mergeSort(A.subList(0, mid)),mergeSort(A.subList(mid, A.size())));
    }
}
