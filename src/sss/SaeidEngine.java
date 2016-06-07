package sss;

import sss.engine.Dictionary;
import sss.engine.IndexInfo;
import sss.engine.KMeans;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Saeid Dadkhah on 2016-06-08 12:21 AM.
 * Project: SaeidSearchSystem
 */
public class SaeidEngine {

    private Dictionary wordDictionary;
    private ArrayList<IndexInfo> indexIndex;
    private ArrayList<ArrayList<IndexInfo>> index;
    private ArrayList<IndexInfo> invertedIndexIndex;
    private ArrayList<ArrayList<IndexInfo>> invertedIndex;

    private KMeans clusters;

    private double[][] weightMatrix;
    private int mode;

    public static void main(String[] args) {
        System.out.println("===========((sss.LuceneEngine Lucene mode test))===========");
        SaeidEngine saeidEngine = new SaeidEngine();

        System.out.println("===========((Adding 1st doc))===========");
        try {
            saeidEngine.addDoc("hello! my name is saeid! :)", 1);
            saeidEngine.addDoc("hi! his name is mostafa! :(", 2);
            saeidEngine.addDoc("hello! his name is sajjad! :|", 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<String> keySet = saeidEngine.wordDictionary.keySet();
        for (String key : keySet)
            System.out.println(key + ": <" + key + ">");
        System.out.println();
        for (ArrayList<IndexInfo> iis : saeidEngine.invertedIndex) {
            for (IndexInfo ii : iis)
                System.out.print(ii.getId() + ": " + ii.getNum() + ", ");
            System.out.println();
        }

        saeidEngine.finishIndexing();

        System.out.println("Searching");
        ArrayList<Integer> res = saeidEngine.search("name");
        for (int i = 0; i < res.size(); i++)
            System.out.println(i + ": " + res.get(i));
    }

    public SaeidEngine(){
        init();
    }

    private void init() {
        wordDictionary = new Dictionary();
        index = new ArrayList<>();
        indexIndex = new ArrayList<>();
        invertedIndex = new ArrayList<>();
        invertedIndexIndex = new ArrayList<>();
    }

    public void addDoc(String doc, int docId) throws Exception {
        String[] words = doc.split("\\s+|,\\s*|\\.\\s*");

        indexIndex.add(new IndexInfo(docId, words.length));
        ArrayList<IndexInfo> iis = new ArrayList<>();
        index.add(iis);
        for (String word : words) {
            Integer wordId = wordDictionary.getId(word);
            // Add to dictionary if it doesn't exist.
            if (wordId == null) {
                wordId = wordDictionary.genNewId();
                wordDictionary.add(word, wordId);
                invertedIndexIndex.add(new IndexInfo(wordId, 0));
                invertedIndex.add(new ArrayList<>());
            }

            // Index
            int wordIndex = findIndex(iis, wordId);
            if (wordIndex == -1) {
                iis.add(new IndexInfo(wordId, 1));
            } else {
                iis.get(wordIndex).increaseNum(1);
            }

            // Inverted index
            wordIndex = findIndex(invertedIndexIndex, wordId);
            invertedIndexIndex.get(wordIndex).increaseNum(1);
            int docIndex = findIndex(invertedIndex.get(wordIndex), docId);
            if (docIndex == -1) {
                invertedIndex.get(wordIndex).add(new IndexInfo(docId, 1));
            } else {
                invertedIndex.get(wordIndex).get(docIndex).increaseNum(1);
            }
        }
    }

    public void finishIndexing(){
        mode = SSS.MODE_SEARCH;
        makeWeightMatrix();
//            clustering();
    }

    private void makeWeightMatrix() {
        weightMatrix = new double[index.size()][invertedIndex.size()];
        for (String k : wordDictionary.keySet()) {
            for (int j = 0; j < index.size(); j++) {
                int wordIndex = findIndex(invertedIndexIndex, wordDictionary.getId(k));
                weightMatrix[j][wordIndex] = wordWeightInDoc(wordIndex, j);
            }
        }
    }

    private void clustering() {
        int[][] matrix = new int[index.size()][invertedIndex.size()];
        for (String k : wordDictionary.keySet()) {
            for (int j = 0; j < index.size(); j++) {
                int wordId = wordDictionary.getId(k);
                int wordIndexInDoc = findIndex(index.get(j), wordId);
                if (wordIndexInDoc == -1)
                    continue;
                matrix[j][findIndex(invertedIndexIndex, wordId)] = index.get(j).get(wordIndexInDoc).getNum();
            }
        }
        clusters = new KMeans(matrix, (int) Math.sqrt(index.size()), 10);
    }

    public double wordWeightInDoc(int wordIndex, int docIndex) {
        int docId = indexIndex.get(docIndex).getId();
        int docIndexInWord = findIndex(invertedIndex.get(wordIndex), docId);
        if (docIndexInWord == -1)
            return 0;

        int nIJ = invertedIndex.get(wordIndex).get(docIndexInWord).getNum();
        int nMaxJ = 0;
        for (int i = 0; i < index.get(docIndex).size(); i++) {
            int num = index.get(docIndex).get(i).getNum();
            if (nMaxJ < num)
                nMaxJ = num;
        }
//        int n = index.size();
        int nI = invertedIndex.get(wordIndex).size();
        return nIJ * Math.log((double) index.size() / nI) / nMaxJ;
    }

    public ArrayList<Integer> search(String query) {
        String[] words = query.split("\\s+|,\\s*|\\.\\s*");

        ArrayList<Integer> result = new ArrayList<>();
        for (String word : words) {
            int wordIndex = findIndex(invertedIndexIndex, wordDictionary.getId(word));
            if (wordIndex != -1) {
                for (int j = 0; j < invertedIndex.get(wordIndex).size(); j++)
                    result.add(invertedIndex.get(wordIndex).get(j).getId());
            }
        }
        return result;
    }

    private int findIndex(ArrayList<IndexInfo> al, Object o) {
        for (int i = 0; i < al.size(); i++)
            if (al.get(i).equals(o))
                return i;
        return -1;
    }

}
