package sss;

import sss.engine.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Saeid Dadkhah on 2016-06-08 12:21 AM.
 * Project: SaeidSearchSystem
 */
public class SaeidEngine {

    public static final int NORMALIZER = 20;
    public static final int WORD_NUM_PER_CLASS = 15;

    private static final String PERSIAN_DELIMITER = "\\s+|,\\s*|\\.\\s*|[(\\p{Punct}|؛|،)\\s]+";
    private static final String ENGLISH_DELIMITER = "\\s+|,\\s*|\\.\\s*";

    // External classes
    private StopWords stopWords;
    private org.tartarus.snowball.ext.englishStemmer englishStemmer;
    private KMeans clusters;
    private NaïveBayesClassifier naïveBayesClassifier;

    // Index and class lists
    private Dictionary wordDictionary;
    private ArrayList<IndexInfo> indexIndex;
    private ArrayList<Integer> documentClass;
    private ArrayList<ArrayList<IndexInfo>> index;
    private ArrayList<IndexInfo> invertedIndexIndex;
    private ArrayList<ArrayList<IndexInfo>> invertedIndex;

    // Weight matrix
    private double[][] weightMatrix;

    // Mode
    private int mode;

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) {
        int mode = SSS.MODE_INDEX_20_NEWS_GROUPS;
        if (mode == SSS.MODE_INDEX_ONE_FILE) {
            System.out.println("===========((sss.SaeidEngine Index one file mode test))===========");
            SaeidEngine saeidEngine = new SaeidEngine(SSS.MODE_INDEX_ONE_FILE);

            System.out.println("===========((Adding docs))===========");
            try {
                saeidEngine.addDoc("hello saeid! saeid my name is saeid! :)", 1, 1);
                saeidEngine.addDoc("hi saeid! his name is mostafa! :(", 2, 1);
                saeidEngine.addDoc("hello! his name is sajjad! :|", 3, 1);
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
            ArrayList<Integer> res = null;
            try {
                res = saeidEngine.search("name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert res != null;
            for (int i = 0; i < res.size(); i++)
                System.out.println(i + ": " + res.get(i));
        } else if (mode == SSS.MODE_INDEX_20_NEWS_GROUPS) {
            System.out.println("===========((sss.SaeidEngine Index one file mode test))===========");
            SaeidEngine saeidEngine = new SaeidEngine(SSS.MODE_INDEX_20_NEWS_GROUPS);

            System.out.println("===========((Adding docs))===========");
            try {
                saeidEngine.addDoc("hello saeid! my saeid is saeid name saeid! :)", 1, 0);
                saeidEngine.addDoc("hi saeid! his name is mostafa! :(", 2, 0);
                saeidEngine.addDoc("hello! his is sajjad! :|", 3, 0);
                saeidEngine.addDoc("hi hello sajjad mostafa! :|", 7, 0);

                saeidEngine.addDoc("computers works well computers! :| computers are computers, they compute!", 4, 1);
                saeidEngine.addDoc("computers enjoy well, computers never suck", 5, 1);
                saeidEngine.addDoc("computers works bugy", 6, 1);

                saeidEngine.trainClassifier(2, 0.6);
                System.out.println(saeidEngine.classify("saeiding"));
                System.out.println(saeidEngine.classify("saeided"));
                System.out.println(saeidEngine.classify("computing"));
                System.out.println(saeidEngine.classify("work"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SaeidEngine(int mode) {
        this.mode = mode;
        init();
    }

    private void init() {
        wordDictionary = new Dictionary();
        index = new ArrayList<>();
        indexIndex = new ArrayList<>();
        documentClass = new ArrayList<>();
        invertedIndex = new ArrayList<>();
        invertedIndexIndex = new ArrayList<>();

        switch (mode) {
            case SSS.MODE_INDEX_ONE_FILE:
                stopWords = new StopWords(StopWords.PERSIAN);
                break;
            case SSS.MODE_INDEX_20_NEWS_GROUPS:
                stopWords = new StopWords(StopWords.ENGLISH);
                englishStemmer = new org.tartarus.snowball.ext.englishStemmer();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void addDoc(String doc, int docId, int documentClass) throws Exception {
        if (mode != SSS.MODE_INDEX_ONE_FILE && mode != SSS.MODE_INDEX_20_NEWS_GROUPS)
            throw new Exception("You are not currently in index mode.");
        String[] words = doc.split(PERSIAN_DELIMITER);

        indexIndex.add(new IndexInfo(docId, words.length));
        this.documentClass.add(documentClass);
        ArrayList<IndexInfo> iis = new ArrayList<>();
        index.add(iis);
        for (String word : words) {
            // Stemming
            if (mode == SSS.MODE_INDEX_20_NEWS_GROUPS) {
                englishStemmer.setCurrent(word);
                englishStemmer.stem();
                word = englishStemmer.getCurrent();
            }
            // Ignore if stop word
            if (stopWords.isStopWord(word))
                continue;
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

    public void finishIndexing() {
        makeWeightMatrix();
        clustering();
        mode = SSS.MODE_SEARCH;
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

    private void clustering() {
        /*
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
        */
        clusters = new KMeans(weightMatrix, (int) Math.sqrt(index.size()), 10);
    }

    public void trainClassifier(int numOfClasses, double trainExamplesRatio) {
        System.out.println("Scoring features");
        int[][] frequency = new int[numOfClasses][invertedIndex.size()]; // frequency of words in each class in score

        for (int i = 0; i < indexIndex.size(); i++) {
            int idx;
            for (int j = 0; j < index.get(i).size(); j++) {
                idx = findIndex(invertedIndexIndex, index.get(i).get(j).getId());
                frequency[documentClass.get(i)][idx] += index.get(i).get(j).getNum();
            }
        }

        double[][] score = new double[numOfClasses][invertedIndex.size()];
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                if (frequency[i][j] > NORMALIZER)
                    score[i][j] = ((double) frequency[i][j] * frequency[i][j] / invertedIndexIndex.get(j).getNum());
                else
                    score[i][j] = 0;
            }
        }

        ArrayList<ArrayList<IndexInfo>> words = new ArrayList<>();
        ArrayList<ArrayList<Double>> scores = new ArrayList<>();
        ArrayList<ArrayList<Integer>> frequencies = new ArrayList<>();

        for (int i = 0; i < score.length; i++) {
            words.add(new ArrayList<>());
            scores.add(new ArrayList<>());
            frequencies.add(new ArrayList<>());
            for (int j = 0; j < score[i].length; j++) {
                int idx = 0;
                while (idx < scores.get(i).size()
                        && (score[i][j] < scores.get(i).get(idx)
                        || (score[i][j] == scores.get(i).get(idx)
                        && frequency[i][j] < frequencies.get(i).get(idx)))
                        && idx < WORD_NUM_PER_CLASS) {
//                    System.out.println(idx < scores.get(i).size());
//                    System.out.println(score[i][j] < scores.get(i).get(idx));
//                    System.out.println(idx < WORD_NUM_PER_CLASS);
                    idx++;
                }
//                System.out.println(idx < scores.get(i).size());
//                if (idx < scores.get(i).size())
//                    System.out.println(score[i][j] < scores.get(i).get(idx));
//                if (idx < scores.get(i).size() && score[i][j] < scores.get(i).get(idx))
//                    System.out.println(idx < WORD_NUM_PER_CLASS);
                if (idx < WORD_NUM_PER_CLASS) {
                    words.get(i).add(idx, invertedIndexIndex.get(j));
                    scores.get(i).add(idx, score[i][j]);
                    frequencies.get(i).add(idx, frequency[i][j]);
                }
            }
        }

        System.out.println("Selecting features");
        HashSet<Integer> selected = new HashSet<>();
        for (ArrayList<IndexInfo> word : words) {
            for (int j = 0, k = 0; j < WORD_NUM_PER_CLASS; j++, k++) {
                //noinspection SuspiciousMethodCalls
                while (selected.contains(word.get(k)))
                    k++;
                selected.add(word.get(k).getId());
            }
        }

        words.clear();
        scores.clear();
        frequencies.clear();
        System.gc();

        ArrayList<Integer> selectedWords = selected.stream().collect(Collectors.toCollection(ArrayList::new));
        /*
        for (Integer aSelected : selected) {
            selectedWords.add(aSelected);
        }
        */

        System.out.println("Building matrix");
        int[][] matrix = new int[index.size()][numOfClasses * WORD_NUM_PER_CLASS];
        for (int i = 0; i < selectedWords.size(); i++) {
            for (int j = 0; j < index.size(); j++) {
                int wordIndexInDoc = findIndex(index.get(j), selectedWords.get(i));
                if (wordIndexInDoc == -1)
                    matrix[j][i] = 0;
                else
                    matrix[j][i] = index.get(j).get(wordIndexInDoc).getNum();
            }
        }
//        for (String k : wordDictionary.keySet()) {
//            for (int j = 0; j < index.size(); j++) {
//                int wordId = wordDictionary.getId(k);
//                int wordIndexInDoc = findIndex(index.get(j), wordId);
//                if (wordIndexInDoc == -1)
//                    continue;
//                matrix[j][findIndex(invertedIndexIndex, wordId)] = index.get(j).get(wordIndexInDoc).getNum();
//            }
//        }
        ClassifierEvaluator classifierEvaluator = new ClassifierEvaluator(matrix, documentClass, numOfClasses, trainExamplesRatio);
        double s = 0;
        for (int i = 0; i < numOfClasses; i++) {
            System.out.println(i
                    + ":\n\tP: " + classifierEvaluator.getPrecision(i)
                    + "\n\tR: " + classifierEvaluator.getRecall(i)
                    + "\n\tF: " + classifierEvaluator.getF1Score(i));
            s += classifierEvaluator.getF1Score(i);
        }
        System.out.println(s);
        naïveBayesClassifier = classifierEvaluator.getNaïveBayesClassifier();
    }

    public int classify(String doc) {
        return naïveBayesClassifier.classify(vectorMaker(doc));
    }

    private int[] vectorMaker(String string) {
        String[] words = string.split(ENGLISH_DELIMITER);

        int[] vector = new int[invertedIndexIndex.size()];
        for (String word : words) {
            englishStemmer.setCurrent(word);
            englishStemmer.stem();
            word = englishStemmer.getCurrent();
            if (stopWords.isStopWord(word))
                continue;
            int wordIndex = findIndex(invertedIndexIndex, wordDictionary.getId(word));
            if (wordIndex != -1) {
                vector[wordIndex] += 1;
            }
        }
        return vector;
    }

    public ArrayList<Integer> search(String query) throws Exception {
        if (mode != SSS.MODE_SEARCH)
            throw new Exception("You are not currently in search mode.");

        int[] iVector = vectorMaker(query);
        double[] vector = new double[iVector.length];
        for (int i = 0; i < iVector.length; i++)
            vector[i] = iVector[i];

        ArrayList<Integer> indices = clusters.getDocs(vector);

        // Sorting result
        ArrayList<Double> scores = new ArrayList<>();
        ArrayList<Integer> secondIndices = new ArrayList<>();
        double score;
        for (int i = 0; i < indices.size(); i++) {
            score = 0;
            for (int j = 0; j < vector.length; j++)
                score += vector[j] * weightMatrix[i][j];
            score = score / indexIndex.get(indices.get(i)).getNum();
            int j = 0;
            while (j < scores.size() && scores.get(j) > score)
                j++;
            scores.add(j, score);
            secondIndices.add(j, indices.get(i));
        }

        // Index to id
        /*
        for (Integer index : secondIndices)
            result.add(indexIndex.get(index).getId());
            */
        return secondIndices.stream().map(index1 -> indexIndex.get(index1).getId()).collect(Collectors.toCollection(ArrayList::new));
    }

    private int findIndex(ArrayList<IndexInfo> al, Object o) {
        for (int i = 0; i < al.size(); i++)
            if (al.get(i).equals(o))
                return i;
        return -1;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
