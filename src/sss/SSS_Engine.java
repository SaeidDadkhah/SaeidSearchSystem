package sss;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import sss.engine.Dictionary;
import sss.engine.IndexInfo;
import sss.engine.KMeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Saeid Dadkhah on 2016-03-10 5:30 PM.
 * Project: SSS
 */

public class SSS_Engine {

    public static final int MODE_LUCENE = 0;
    public static final int MODE_INDEX = 1;
    public static final int MODE_SEARCH = 2;

    public static final int F_TYPE_UNKNOWN = -1;
    public static final int F_TYPE_TOKENIZE = 0;
    public static final int F_TYPE_NOT_TOKENIZE = 1;

    private static final int HITS = 10;

    private int mode;

    // Lucene mode variables;
    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter iw;
    private IndexSearcher is;

    // Index and search mode variables;
    private Dictionary wordDictionary;
    private ArrayList<IndexInfo> indexIndex;
    private ArrayList<ArrayList<IndexInfo>> index;
    private ArrayList<IndexInfo> invertedIndexIndex;
    private ArrayList<ArrayList<IndexInfo>> invertedIndex;

    private double[][] weightMatrix;
    private KMeans clusters;

    public static void main(String[] args) {
        int check = MODE_INDEX;
        if (check == MODE_LUCENE) {
            System.out.println("===========((sss.SSS_Engine Lucene mode test))===========");
            SSS_Engine spssEngine = new SSS_Engine(MODE_LUCENE);

            System.out.println("===========((Adding 1st doc))===========");
            ArrayList<String> fields = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            fields.add("type");
            values.add("jpg");
            types.add(F_TYPE_NOT_TOKENIZE);
            fields.add("writer");
            values.add("saeid");
            types.add(F_TYPE_NOT_TOKENIZE);
            fields.add("body");
            values.add("go world");
            types.add(F_TYPE_TOKENIZE);
            try {
                spssEngine.addDoc(fields, values, types);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("===========((Adding 2nd doc))===========");
            fields.clear();
            values.clear();
            types.clear();
            fields.add("type");
            values.add("jpg");
            types.add(F_TYPE_NOT_TOKENIZE);
            fields.add("Writer");
            values.add("Mostafa");
            types.add(F_TYPE_NOT_TOKENIZE);
            fields.add("body");
            values.add("bye world");
            types.add(F_TYPE_NOT_TOKENIZE);
            fields.add("pm");
            values.add("kalle zard");
            types.add(F_TYPE_NOT_TOKENIZE);
            try {
                spssEngine.addDoc(fields, values, types);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("===========((Finishing Indexing))===========");
            spssEngine.finishIndexing();

            System.out.println("===========((Start Searching))===========");
            try {
                SSS_Engine.showRes(spssEngine.luceneSearch("going"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (check == MODE_INDEX) {
            System.out.println("===========((sss.SSS_Engine Lucene mode test))===========");
            SSS_Engine spssEngine = new SSS_Engine(MODE_INDEX);

            System.out.println("===========((Adding 1st doc))===========");
            try {
                spssEngine.addDoc("hello! my name is saeid! :)", 1);
                spssEngine.addDoc("hi! his name is mostafa! :(", 2);
                spssEngine.addDoc("hello! his name is sajjad! :|", 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Set<String> keySet = spssEngine.wordDictionary.keySet();
            for (String key : keySet)
                System.out.println(key + ": <" + key + ">");
            System.out.println();
            for (ArrayList<IndexInfo> iis : spssEngine.invertedIndex) {
                for (IndexInfo ii : iis)
                    System.out.print(ii.getId() + ": " + ii.getNum() + ", ");
                System.out.println();
            }

            System.out.println("Searching");
            ArrayList<Integer> res = spssEngine.saeidSearch("name");
            for (int i = 0; i < res.size(); i++)
                System.out.println(i + ": " + res.get(i));

            spssEngine.finishIndexing();
        }
    }

    public SSS_Engine(int mode) { // stem stop word
        this.mode = mode;
        switch (mode) {
            case MODE_LUCENE:
                luceneInit();
                break;
            case MODE_INDEX:
                indexInit();
                break;
            case MODE_SEARCH:
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void luceneInit() {
//        analyzer = new StandardAnalyzer();
        analyzer = new EnglishAnalyzer();
//        analyzer = new SnowballAnalyzer(Version.LUCENE_5_5_0, "SPA");
        directory = new RAMDirectory();
        IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
        iw = null;
        try {
            iw = new IndexWriter(directory, iwConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        is = null;
    }

    private void indexInit() {
        wordDictionary = new Dictionary();
        index = new ArrayList<>();
        indexIndex = new ArrayList<>();
        invertedIndex = new ArrayList<>();
        invertedIndexIndex = new ArrayList<>();
    }

    public boolean addDoc(ArrayList<String> fields, ArrayList<String> values, ArrayList<Integer> types)
            throws Exception {
        // Checking Arguments
        if (mode != MODE_LUCENE)
            throw new Exception("You are not currently in lucene mode.");
        if (!iw.isOpen())
            throw new Exception("Index Writer is not open.");
        if (fields.size() != values.size() || fields.size() != types.size())
            throw new Exception("Size Mismatch.");

        // Making Document
        Document d = new Document();
        for (int i = 0; i < fields.size(); i++)
            switch (types.get(i)) {
                case F_TYPE_NOT_TOKENIZE:
                    d.add(new StringField(fields.get(i).toLowerCase(), values.get(i).toLowerCase(), Field.Store.YES));
                    break;
                case F_TYPE_TOKENIZE:
                    d.add(new TextField(fields.get(i).toLowerCase(), values.get(i).toLowerCase(), Field.Store.YES));
                    break;
                case F_TYPE_UNKNOWN:
                    System.out.println(fields.get(i));
                    throw new Exception("Unknown Type: " + i);
//                    break;
                default:
                    throw new Exception("Illegal Type: " + i);
            }

        // Adding Document
        try {
            iw.addDocument(d);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addDoc(String doc, int docId) throws Exception {
        if (mode != MODE_INDEX)
            throw new Exception("You are not currently in index mode.");
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

    private int findIndex(ArrayList<IndexInfo> al, Object o) {
        for (int i = 0; i < al.size(); i++)
            if (al.get(i).equals(o))
                return i;
        return -1;
    }

    public void finishIndexing() {
        if (mode == MODE_LUCENE) {
            if (!iw.isOpen())
                return;
            try {
                iw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                is = new IndexSearcher(DirectoryReader.open(directory));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode == MODE_INDEX) {
            mode = MODE_SEARCH;
            makeWeightMatrix();
            clustering();
            System.out.println();
        } else if (mode == MODE_SEARCH) {
        }
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

    public Document[] luceneSearch(String query) throws Exception {
        if (mode != MODE_LUCENE)
            throw new Exception("You are not currently in lucene mode.");

        query = query.toLowerCase();
        if (is == null)
            throw new Exception("Index Searcher is not open");

        query = query.replaceAll("and", "AND");
        query = query.replaceAll("and", "OR");
        Query q;
        try {
            q = new QueryParser("body", analyzer).parse(query);
            System.out.println(q);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Document[0];
        }
        ScoreDoc[] docs = is.search(q, HITS).scoreDocs;
        Document[] result = new Document[docs.length];
        for (int i = 0; i < docs.length; i++)
            result[i] = is.doc(docs[i].doc);
        return result;
    }

    public ArrayList<Integer> saeidSearch(String query) {
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

    public static void showRes(Document[] res) {
        System.out.println("hits: " + res.length);
        for (int i = 0; i < res.length; i++) {
            System.out.println("Res: " + (i + 1));
            for (int j = 0; j < res[i].getFields().size(); j++) {
                String field = res[i].getFields().get(j).name();
                System.out.println(field + ": " + res[i].get(field));
            }
            System.out.println();
        }
    }

}
